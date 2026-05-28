<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(accroid)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var pdf_type="O";
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	}
	
	var msg="";
	var flag = true;
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "../other_invoice/frm_other_inv_crdr_preparation.jsp?&u="+u+"&pdf_type="+pdf_type+
				"&month="+month+"&year="+year+"&accroid="+accroid;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg)
	}
}

function refershPar(accroid,sub_msg,msg_type)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var pdf_type="O";
	var u = document.forms[0].u.value;
	
	if(document.getElementById("pdf_type_"+accroid)!=null && document.getElementById("pdf_type_"+accroid)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+accroid).value;
	} 
	
	
	var url = "../other_invoice/frm_other_inv_crdr_preparation.jsp?&u="+u+"&month="+month+"&year="+year+"&pdf_type="+pdf_type+"&accroid="+accroid+"&msg="+sub_msg+"&msg_type="+msg_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
	
} 

var newWindow;
function openCreditDebitInv(accroid,inv_type,jspPath)
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var u = document.forms[0].u.value;
	
	var url = jspPath+"?u="+u+"&month="+month+"&year="+year+"&operation=INSERT&accroid="+accroid+"&invoice_type="+inv_type;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Other Invoice Credit/Debit","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Other Invoice Credit/Debit","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function openActivity1(comp_cd,inv_seq,inv_type,inv_type_nm,operation,type,supp_cd,fin_year,sel_inv_no,cr_dr_type,jspPath)
{
	var u = document.forms[0].u.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var url = jspPath+"?comp_cd="+comp_cd+"&invoice_seq="+inv_seq+"&invoice_type="+inv_type+"&month="+month+"&year="+year+"&operation="+operation+
			"&accroid="+inv_type_nm+"&u="+u+"&type="+type+"&supp_cd="+supp_cd+"&financial_year="+fin_year+"&sel_inv_no="+sel_inv_no+"&cr_dr_type="+cr_dr_type;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Credit/Debit","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Credit/Debit","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function openActivity3(comp_cd,inv_seq,inv_type,inv_type_nm,operation,supp_cd,fin_year,sel_inv_no,cr_dr_type,jspPath)
{
	var u = document.forms[0].u.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var url = jspPath+"?comp_cd="+comp_cd+"&invoice_seq="+inv_seq+"&invoice_type="+inv_type+"&month="+month+"&year="+year+"&operation="+operation+
	"&accroid="+inv_type_nm+"&u="+u+"&supp_cd="+supp_cd+"&financial_year="+fin_year+"&sel_inv_no="+sel_inv_no+"&cr_dr_type="+cr_dr_type;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Prepare Credit/Debit","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Prepare Credit/Debit","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}

function printPDF(comp_cd,inv_no,jspPath,inv_type,inv_type_nm,index,sel_inv_no,cr_dr_type)
{
	
	var print_access = document.forms[0].print_access.value;
	var pdf_type="";
	var is_print="1";
	if(document.getElementById("pdf_type_"+inv_type_nm)!=null && document.getElementById("pdf_type_"+inv_type_nm)!=undefined)
	{
		pdf_type = document.getElementById("pdf_type_"+inv_type_nm).value;
	}
	var all_pdf_type = document.getElementById("all_pdf_type"+index).value;
	
	if(pdf_type == "All")
	{
		pdf_type=all_pdf_type;
	}
	var u = document.forms[0].u.value;
	
	if(print_access=="N")
	{
		alert("You don't have Print Rights!");	
	}
	else
	{
		var url = jspPath+"?comp_cd="+comp_cd+"&inv_no="+inv_no+"&inv_type="+inv_type+"&accroid="+inv_type_nm+"&pdf_type="+pdf_type
				+"&sel_inv_no="+sel_inv_no+"&cr_dr_type="+cr_dr_type+"&is_print="+is_print+"&u="+u;
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"PDF Other Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"PDF Other Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
	}
}
function openPdfFile(url)
{
	window.open(url);
}

function openAllPdfFile(fin_yr,supplier_cd,inv_seq,inv_type)
{
	var url = "rpt_oth_inv_view_all_pdf.jsp?financial_year="+fin_yr+"&supplier_cd="+supplier_cd+"&invoice_seq="+inv_seq+"&invoice_type="+inv_type;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"PDF Other Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"PDF Other Invoice","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
}
function doGenXML(invoice_no,accroid,flag,inv_type)
{
	var u = document.forms[0].u.value;
	var url = "../extn_interface/frm_oth_inv_sun_xml_approval.jsp?&accroid="+accroid+"&invoice_no="+invoice_no+"&sun_flag="+flag+"&inv_type="+inv_type+"&u="+u;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Sales SAP Approval","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Sales SAP Approval","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}
function doReGenXML(invoice_no,accroid,flag,inv_type)
{
	var u = document.forms[0].u.value;
	var url = "../extn_interface/frm_oth_inv_sun_xml_re_approval.jsp?&accroid="+accroid+"&invoice_no="+invoice_no+"&sun_flag="+flag+"&inv_type="+inv_type+"&u="+u;

	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Sales SAP Approval","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Sales SAP Approval","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}
function downloadXML(file_name, indx)
{
    var appPath = document.forms[0].appPath.value;
    var download = document.getElementById("download" + indx);

    // Build file path
    var dwnfile_path = (appPath + file_name).replace(/\\/g, "/");

    if (appPath !== '') {
        download.href = "../" + dwnfile_path;
    }

    download.setAttribute("download", file_name);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.other_invoice.DataBean_Other_Invoice" id="other_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
int currentYear = 0;
int currentMonth = 0;
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	currentMonth=Integer.parseInt(temp[1]);
	currentYear=Integer.parseInt(temp[2]);
}
int filter_start_year = CommonVariable.filter_start_year;

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
String pdf_type=request.getParameter("pdf_type")==null?"O":request.getParameter("pdf_type");

if(month.length() == 1)
{
	month="0"+month; 
}

other_inv.setCallFlag("EXISTING_CRDR_LIST");
other_inv.setComp_cd(owner_cd);
other_inv.setMonth(month);
other_inv.setYear(year);
other_inv.setPrint_pdf_type(pdf_type);
other_inv.setView_pdf_type(pdf_type);
other_inv.init();

Vector VINVOICE_TYPE = other_inv.getVINVOICE_TYPE();
Vector VINVOICE_TYPE_NM = other_inv.getVINVOICE_TYPE_NM();
Vector VINVOICE_LIST_NM = other_inv.getVINVOICE_LIST_NM();
Vector VCRDR_CHECK_APPROVE = other_inv.getVCRDR_CHECK_APPROVE();
Vector VINDEX = other_inv.getVINDEX();
Vector VPDF_TYPE = other_inv.getVPDF_TYPE();
Vector VENDOR_NAME = other_inv.getVVENDOR_NAME();
Vector VENDOR_CD = other_inv.getVENDORCD();
Vector VCRDR_NO = other_inv.getVCRDR_NO();
Vector VINVOICE_DT = other_inv.getVINVOICE_DT();
Vector VCHECK_FLAG = other_inv.getVCHECK_FLAG();
Vector VAPPROVE_FLAG = other_inv.getVAPPROVE_FLAG();
Vector VPDF_FILE_NAME = other_inv.getVPDF_FILE_NAME();
Vector VPDF_FILE_PATH = other_inv.getVPDF_FILE_PATH();
Vector VFIN_YEAR = other_inv.getVFIN_YEAR();
Vector VINVOICE_SEQ = other_inv.getVINVOICE_SEQ();
Vector VSUPPLIER_CD = other_inv.getVSUPPLIER_CD();
Vector VSUPPLIER_NM = other_inv.getVSUPPLIER_NM();
Vector VINV_TYPE = other_inv.getVINV_TYPE();
Vector VIS_IRN_GENERATED = other_inv.getVIS_IRN_GENERATED();
Vector VINV_FLAG = other_inv.getVINV_FLAG();
Vector VREF_INV_NO = other_inv.getVREF_INV_NO();
Vector VSUN_APPROVAL_FLAG = other_inv.getVSUN_APPROVAL_FLAG();
Vector VXML_FILE_NAME = other_inv.getVXML_FILE_NAME();
Vector VCRDR_CRITERIA = other_inv.getVCRDR_CRITERIA();
Vector VCRDR_TYPE = other_inv.getVCRDR_TYPE();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;
String appPath=CommonVariable.work_dir+owner_cd+"//"+CommonVariable.sun_xml+"//";
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
					    	Other Invoice Credit/Debit Generation
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="form-group row">
								<div class="col-md-4 col-sm-4 col-xs-4"></div>
								<div class="col-md-3 col-sm-3 col-xs-3">
									<!-- <div class="d-flex justify-content-center"> -->
										<div class="form-group row">
											<div class="col-auto">
												<label class="form-label"><b>Month/Year</b></label>
								  			</div>
								  			<div class="col">
								  				<select class="form-select form-select-sm" name="month" onchange="">
													<option value="01" label="January">January</option>
													<option value="02" label="February">February</option>
													<option value="03" label="March">March</option>
													<option value="04" label="April">April</option>
													<option value="05" label="May">May</option>
													<option value="06" label="June">June</option>
													<option value="07" label="July">July</option>
													<option value="08" label="August">August</option>
													<option value="09" label="September">September</option>
													<option value="10" label="October">October</option>
													<option value="11" label="November">November</option>
													<option value="12" label="December">December</option>
												</select>
												<script>document.forms[0].month.value="<%=month%>"</script>
											</div>
											<div class="col">
								  				<select class="form-select form-select-sm" name="year" onchange="">
								  					<%for(int i=(currentYear+1); i > filter_start_year;i--){ %>
														<option value="<%=i%>"><%=i%></option>
													<%} %>
												</select>
												<script>document.forms[0].year.value="<%=year%>"</script>
											</div>
										</div>
									<!-- </div> -->
								</div>
								<div class="col-md-2 col-sm-2 col-xs-2">
									<div class="col">
										<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
				  					</div>
								</div>
						  		<div class="col-sm-3 col-xs-3 col-md-3">
							  		<div class="d-flex justify-content-end">
							  			<div class="form-group row">
											<div class="col-auto">
												<div class="btn-group">
													<a href="<%=url%>pdf_signer//PDFSigner.jar" download>
														<label class="btn btn-outline-secondary subbtngrp">
															<i class="fa fa-pencil-square-o"></i>&nbsp;Sign PDF
														</label>
													</a>
												</div>
											</div>
											<div class="col-auto">
											</div>
										</div>
									</div>
					  			</div>
						  	</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%int i=0,k=0,l=0,m=0;
				for(int j=0; j<VINVOICE_TYPE_NM.size(); j++)
				{
					int index=Integer.parseInt(""+VINDEX.elementAt(j));
					String heading = ""+VINVOICE_TYPE_NM.elementAt(j);
					%>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="<%=heading%>">
   										<button name="sub_module_cd" class="accordion-button <%if(!accroid.equals(heading)){%>collapsed<%}%> accor-btn" type="button" 
   											data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="<%if(!accroid.equals(heading)){%>true<%}else{%>false<%} %>" aria-controls="collapse<%=j%>">
							    			<%=VINVOICE_TYPE_NM.elementAt(j)%> Credit/Debit Note&nbsp;<font color="blue">(<%=index%> Items)</font>
							      		</button>	
							    	</h2>
									<div id="collapse<%=j%>" class="accordion-collapse collapse <%if(accroid.equals(heading)){%>show<%}%>" aria-labelledby="<%=heading%>">
							      		<div class="accordion-body accor-body">
							      			<div class="row m-b-5">
							      				<div class="col-sm-3 col-xs-3 col-md-3">
												</div>
												<div class="col-sm-3 col-xs-3 col-md-3">
												</div>
												<div class="col-sm-3 col-xs-3 col-md-3">
												</div>
													<div class="col-sm-3 col-xs-3 col-md-3" align="right">
														<div class="btn-group">
															<label class="btn btn-outline-secondary subbtngrp" onclick="openCreditDebitInv('<%=VINVOICE_TYPE_NM.elementAt(j)%>','<%=VINVOICE_TYPE.elementAt(j)%>','<%=VINVOICE_LIST_NM.elementAt(j)%>');"><i class="fa fa-plus-circle"></i>&nbsp;Create New Credit/Debit</label>
														</div>
													</div>
												</div>
											<div class="row">
												<div class="col-md-12 col-sm-12 col-xs-12">
													<div class="table-responsive">
														<table class="table table-bordered table-hover" id="example<%=j%>">
														<thead>
															<tr>
																<th rowspan="2">SR. No.</th>
																<th rowspan="2">Vendor
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="vendor<%=j%>" onkeyup="Search(this,'1','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Supplier
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="supp<%=j%>" onkeyup="Search(this,'2','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Credit/Debit Note Type
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="dt<%=j%>" onkeyup="Search(this,'4','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Credit/Debit Note Date
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="dt<%=j%>" onkeyup="Search(this,'4','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Criteria
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="dt<%=j%>" onkeyup="Search(this,'4','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Credit/Debit#<br>[Ref#]
																	<br><div align="center"><input class="form-control form-control-sm" type="text" id="inv_no<%=j%>" onkeyup="Search(this,'3','<%=j%>');" placeholder="Search.." style="width:100px"/></div>
																</th>
																<th rowspan="2">Modify/View IRP</th>
																<th rowspan="2">Check IRP</th>
																<th rowspan="2">Approve IRP</th>
																<th rowspan="2">IRN Status</th>
																<th colspan="2" align="center">
																	<div align="center">
																		<select class="form-select form-select-sm" name="pdf_type" id="pdf_type_<%=heading%>" style="width:80px;" onchange="refresh('<%=heading%>');">
																			<option value="O">Original</option>
																			<option value="D">Duplicate</option>
																			<option value="All">All</option>
																		</select>
																		<script>document.getElementById("pdf_type_<%=heading%>").value="<%=pdf_type%>"</script>
																	</div>
																</th>
																<th colspan="3" align="center">
																	SUN Interface
																</th>
															</tr>
															<tr>
																<th>Print PDF</th>
																<th>View PDF</th>
																<th>Approval |  <br>XML Generation</th>
																<th>Re-Approval |  <br>XML Generation</th>
																<th>XML Download</th>
															</tr>
														</thead>
														<tbody>
														<%k=0;
														if(index>0)
														{
															for(i=i; i<VENDOR_CD.size(); i++)
															{
																k+=1;
															%>
																<tr>
																	<td align="center">
																		<%=k%>
																	</td>
																	
																	<td align="center" title="VENDOR_CD: <%= VENDOR_CD.elementAt(i)%>"><%=VENDOR_NAME.elementAt(i) %></td>
																	<td align="center" title="SUPPLIER_CD: <%= VSUPPLIER_CD.elementAt(i)%>"><%=VSUPPLIER_NM.elementAt(i) %></td>
																	<td align="center"><%=VCRDR_TYPE.elementAt(i) %></td>
																	<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
																	<td align="center"><%=VCRDR_CRITERIA.elementAt(i) %></td>
																	<td align="center"> <%=VCRDR_NO.elementAt(i) %>
																		<br>
																			<font color="blue">[Ref# <%=VREF_INV_NO.elementAt(i)%>]</font>
																	</td>
																	<td align="center">
																	<%if(VCHECK_FLAG.elementAt(i).equals("Y") || VAPPROVE_FLAG.elementAt(i).equals("Y")){ %>
																		<i class="fa fa-eye fa-2x"
																		onclick="openActivity1('<%=comp_cd%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(j) %>','<%=VINVOICE_TYPE_NM.elementAt(j) %>','MODIFY','VIEW','<%=VSUPPLIER_CD.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VREF_INV_NO.elementAt(i)%>','<%=VINV_FLAG.elementAt(i) %>','<%=VINVOICE_LIST_NM.elementAt(j)%>')"
																		style="color:black;">
																		</i>
																		<%}else{ %>
																		<i class="fa fa-pencil fa-2x"
																		onclick="openActivity1('<%=comp_cd%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(j) %>','<%=VINVOICE_TYPE_NM.elementAt(j) %>','MODIFY','EDIT','<%=VSUPPLIER_CD.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VREF_INV_NO.elementAt(i)%>','<%=VINV_FLAG.elementAt(i) %>','<%=VINVOICE_LIST_NM.elementAt(j)%>')"
																		style="<%if(VAPPROVE_FLAG.elementAt(i).equals("Y")){ %>
																			pointer-events: none; opacity: .65;color: gray;
																					<%} else {%>
																						color:#ff9900;
																					<%}%>">
																		</i>
																		<%} %>
																	</td>
																	<td align="center">
																		<i class="fa fa-stethoscope fa-2x"
																		onclick="openActivity3('<%=comp_cd%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(j)%>','<%=VINVOICE_TYPE_NM.elementAt(j) %>','CHECK','<%=VSUPPLIER_CD.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VREF_INV_NO.elementAt(i)%>','<%=VINV_FLAG.elementAt(i) %>','<%=VCRDR_CHECK_APPROVE.elementAt(j)%>')"
																		style="<%if(VAPPROVE_FLAG.elementAt(i).equals("Y")){ %>
																			pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:#ff3399;
																				<%}%>">												
																		</i>
																	</td>
																	<td align="center">
																		<i class="fa fa-thumbs-o-up fa-2x"
																		onclick="openActivity3('<%=comp_cd%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(j)%>','<%=VINVOICE_TYPE_NM.elementAt(j) %>','APPROVE','<%=VSUPPLIER_CD.elementAt(i) %>','<%=VFIN_YEAR.elementAt(i) %>','<%=VREF_INV_NO.elementAt(i)%>','<%=VINV_FLAG.elementAt(i) %>','<%=VCRDR_CHECK_APPROVE.elementAt(j)%>')"
																		<%if(VINVOICE_TYPE.elementAt(j).equals("COSTR")){ %>
																		style="<%if(!VPDF_FILE_NAME.elementAt(i).equals("") || !VCHECK_FLAG.elementAt(i).equals("Y") || !pdf_type.equals("O")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:blue;
																				<%}%>"
																		<%}else{ %>
																			style="<%if(!VPDF_FILE_NAME.elementAt(i).equals("") || !VCHECK_FLAG.elementAt(i).equals("Y") || !pdf_type.equals("O") || VIS_IRN_GENERATED.elementAt(i).equals("Y")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																				<%} else{%>
																				color:blue;
																				<%}%>"
																		<%} %>		
																		>		
																		</i>
																	</td>
																	<td align="center">
																		<%if(VINVOICE_TYPE.elementAt(j).equals("COSTR")){ %>
																			<span class="fa-stack fa-lg">
																				<i class="fa fa-qrcode fa-stack-1x"></i>
																				<i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																			</span>
																		<%}else{ %>
																			<i class="fa fa-qrcode fa-2x"
																				<%if(VIS_IRN_GENERATED.elementAt(i).equals("Y")) {%>
																				title="IRN Generated!"
																				style="color: #0099cc;"
																				<%}else{ %>
																				title="Generation of IRN is Pending!"
																				style="opacity: .65; color: gray;"
																				<%} %>	
																			></i>
																		<%} %>
																	</td>
																	<td align="center">																			
																		<i class="fa fa-print fa-2x"
																		 onclick="printPDF('<%=comp_cd%>','<%=VCRDR_NO.elementAt(i)%>','pdf_other_invoice_cr_dr.jsp','<%=VINVOICE_TYPE.elementAt(j) %>','<%=VINVOICE_TYPE_NM.elementAt(j)%>','<%=i%>','<%=VREF_INV_NO.elementAt(i)%>','<%=VINV_FLAG.elementAt(i) %>')" 
																		<%if(!VINVOICE_TYPE.elementAt(j).equals("COSTR") && !VINVOICE_TYPE.elementAt(j).equals("NPR")) {%>
																			style="<%if(!VAPPROVE_FLAG.elementAt(i).equals("Y") || VPDF_TYPE.elementAt(i).equals(pdf_type) || !VPDF_FILE_NAME.elementAt(i).equals("") || !VIS_IRN_GENERATED.elementAt(i).equals("Y")){ %>
																					pointer-events: none; opacity: .65; color: gray;
																					<%} else{%>
																					color:#800000;
																					<%}%>"
																			<%}else{ %>
																				
																				style="<%if(!VAPPROVE_FLAG.elementAt(i).equals("Y") || VPDF_TYPE.elementAt(i).equals(pdf_type) || !VPDF_FILE_NAME.elementAt(i).equals("")) {%>
																					pointer-events: none; opacity: .65; color: gray;
																				<%} else {%>
																					color:#800000;
																				<%}%>"
																			<%} %>
																			>												
																		</i>	
																		<input type="hidden" name="all_pdf_type" id="all_pdf_type<%=i%>" value="<%=VPDF_TYPE.elementAt(i)%>">											
																	</td>
																	<td align="center">																			
																		<i class="fa fa-file-pdf-o fa-2x"
																		<%if(pdf_type.equals("All")){ %>
																			onclick="openAllPdfFile('<%=VFIN_YEAR.elementAt(i) %>',
																				'<%=VSUPPLIER_CD.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINV_TYPE.elementAt(i)%>')"
																			<%}else{ %>
																				onclick="openPdfFile('<%=file_url%><%=VPDF_FILE_PATH.elementAt(i)%><%=VPDF_FILE_NAME.elementAt(i)%>')" 
																			<%} %>
																		style="<%if(VPDF_FILE_NAME.elementAt(i).equals("")){ %>
																			pointer-events: none; opacity: .65; color: gray;
																		<%} else{%>
																			color:red;
																		<%}%>">												
																		</i>												
																	</td>
																	<td align="center">
																		<div class="email-icon-wrapper d-flex justify-content-center" >
																			<%if(VINVOICE_TYPE.elementAt(j).equals("NPR") || VINVOICE_TYPE.elementAt(j).equals("GA") || VPDF_FILE_NAME.elementAt(i).equals("")){ %>
																				<span class="fa-stack" onclick=""
																					 style="position: relative;pointer-events: none;">
																			            <i class="fa fa-file-code-o fa-stack-2x" style="position:absolute; left:0.0em; top:0.0em; color:gray;"></i>
																						<i class="fa fa-sun-o fa-stack-1x" style="position:absolute; left:0.9em; top:0.6em; color:grey; padding:2px;"></i>
																				</span>
																			<%} else {%>
																				<span class="fa-stack" onclick="doGenXML('<%=VCRDR_NO.elementAt(i) %>','<%=VINVOICE_TYPE_NM.elementAt(j)%>','<%=VSUN_APPROVAL_FLAG.elementAt(i) %>','<%=VINVOICE_TYPE.elementAt(j) %>');"
																					 style="position: relative;pointer-events: auto;">
																					 <%if(VSUN_APPROVAL_FLAG.elementAt(i).equals("Y")) {%>
																					 	<i class="fa fa-eye fa-2x fa-stack-2x" style="position:absolute; left:0.0em; top:0.0em; color:brown;"></i>
																					 <%} else {%>
																			            <i class="fa fa-file-code-o fa-stack-2x" style="position:absolute; left:0.0em; top:0.0em; color:orange;"></i>
																						<i class="fa fa-sun-o fa-stack-1x" style="position:absolute; left:0.9em; top:0.6em; color:red; padding:2px;"></i>
																					<%} %>
																				</span>
																			<%} %> 
																			<!-- <span class="fa-stack fa-lg">
																	  			<i class="fa fa-eye fa-stack-1x"></i>
																	  			<i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																			</span> -->
																		</div>
																	</td>
																	<td align="center">
																		<div class="email-icon-wrapper d-flex justify-content-center" >
																			 <%if(VPDF_FILE_NAME.elementAt(i).equals("") || VSUN_APPROVAL_FLAG.elementAt(i).equals("")){ %>
																				<span class="fa-stack" onclick=""
																					 style="position: relative;pointer-events: none;">
																		            <i class="fa fa-file-code-o fa-stack-2x" style="position:absolute; left:0.0em; top:0.0em; color:grey;"></i>
																					<i class="fa fa-sun-o fa-stack-1x" style="position:absolute; left:0.9em; top:0.6em; color:grey; padding:2px;"></i>
																				</span>
																			<%}else {%>
																				<span class="fa-stack" onclick="doReGenXML('<%=VCRDR_NO.elementAt(i) %>','<%=VINVOICE_TYPE_NM.elementAt(j)%>','<%=VSUN_APPROVAL_FLAG.elementAt(i) %>','<%=VINVOICE_TYPE.elementAt(j) %>');"
																					 style="position: relative;pointer-events: auto;">
																		            <i class="fa fa-file-code-o fa-stack-2x" style="position:absolute; left:0.0em; top:0.0em; color:#00b0e6;"></i>
																					<i class="fa fa-sun-o fa-stack-1x" style="position:absolute; left:0.9em; top:0.6em; color:red; padding:2px;"></i>
																				</span>
																			<%} %>
																			<!-- <span class="fa-stack fa-lg">
																	  			<i class="fa fa-eye fa-stack-1x"></i>
																	  			<i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																			</span> -->
																		</div>
																	</td>
																	<td align="center" valign="middle">
								 										<!-- <span class="fa-stack fa-lg">
																  			<i class="fa fa-download fa-stack-1x"></i>
																  			<i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
																		</span>  -->
																		<a id="download<%=i%>">
																			<i class="fa fa-download fa-lg fa-2x"
																				<%if(!VXML_FILE_NAME.elementAt(i).equals("")) {%>
																					onclick="downloadXML('<%=VXML_FILE_NAME.elementAt(i)%>','<%=i%>')"
																					style ="color: green;"
																				<%} else {%>
																					style ="pointer-events: none; opacity: .65; color: gray;"
																				<%} %>>
																			</i>
																		</a>
																	</td>		
																</tr>
																<%if(k==index){
																	i=i+1;
																	break;
																} %>
															<%} 
														}else{%>
															<tr>
																<td align="center" colspan="16"><%=utilmsg.infoMessage("<b>No CR/DR for "+VINVOICE_TYPE_NM.elementAt(j)+" Generated!</b>") %></td>
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
					</div>
				<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="appPath" value="<%=appPath%>">

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

<input type="hidden" name="accroid" value="">

</form>
<%} %>
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