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
	var counterparty_cd = document.forms[0].counterparty_cd.value; 
	var transporter_cd = document.forms[0].transporter_cd.value; 
	var transporter_plant_seq = document.forms[0].transporter_plant_seq.value;
	var bu_seq = document.forms[0].bu_seq.value;	// SagarB20250922 Added this variable for showing BU list
	var chk_diff = document.forms[0].chk_diff;
	
	if(chk_diff.checked)
	{
		chk_diff="Y";
	}
	else
	{
		chk_diff="N";
	}
	
	var u = document.forms[0].u.value;
	
	if(from_dt!=null && to_dt!=null)
   	{
	   	if(trim(from_dt)!="" && trim(to_dt)!="")
	   	{
	   		var tmp = from_dt.split("/")
	   		var tmp1 = to_dt.split("/")
	   	 	var date1 = new Date(tmp[1]+"/"+tmp[0]+"/"+tmp[2]);
         	var date2 = new Date(tmp1[1]+"/"+tmp1[0]+"/"+tmp1[2]);
         	
         	var time_difference = date2.getTime() - date1.getTime();
         	var days_difference = time_difference / (1000 * 60 * 60 * 24);
         	
         	if(parseInt(days_difference)+1 <= 90)
         	{	
				var value = compareDate(from_dt,to_dt);
				if(value!=1)
				{
					var url = "rpt_nom_alloc.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd+
							"&transporter_cd="+transporter_cd+"&transporter_plant_seq="+transporter_plant_seq+"&chk_diff="+chk_diff+"&bu_seq="+bu_seq+"&u="+u;	// SagarB20250922 Added bu_seq for showing BU list

					document.getElementById("loading").style.visibility = "visible";
					location.replace(url);
				}
				else
				{
					alert("Please ensure From Date <= To Date !");
				    // return false;	SagarB20250929
				}
         	}
         	else
         	{
         		alert("Date range can not exceed more than 90 days!!");
			    // return false;	SagarB20250929
         	}
	   	}
	   	else
	   	{ 
	    	alert("Please Select From and To Dates...");
		    // return false;	SagarB20250929
	   	}
	}
	else
	{
		alert("Please Select From and To Dates...");
	    // return false;	SagarB20250929
	}
}

function nextDate(day_no)
{
	//var clearance = document.forms[0].clearance.value;
	
	var dt = document.forms[0].gas_dt.value;
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
		
		document.forms[0].gas_dt.value=to_dt;
		
		//refresh(clearance);
		refresh();
	}
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value; 
	var transporter_cd = document.forms[0].transporter_cd.value; 
	var transporter_plant_seq = document.forms[0].transporter_plant_seq.value;
	var bu_seq = document.forms[0].bu_seq.value;	// SagarB20250922 Added this variable for showing BU
	var bu_abbr=document.forms[0].bu_seq[document.forms[0].bu_seq.selectedIndex].text;	// SagarB20250922 Added this variable for showing BU
	var chk_diff = document.forms[0].chk_diff;
	
	if(chk_diff.checked)
	{
		chk_diff="Y";
	}
	else
	{
		chk_diff="N";
	}
	
	var url = "xls_nom_alloc.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd+
		"&transporter_cd="+transporter_cd+"&transporter_plant_seq="+transporter_plant_seq+"&chk_diff="+chk_diff+"&bu_seq="+bu_seq+"&bu_abbr="+bu_abbr;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

// Get value after Page Refresh()
String from_dt = request.getParameter("from_dt")==null?nextdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?nextdate:request.getParameter("to_dt");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String transporter_cd=request.getParameter("transporter_cd")==null?"0":request.getParameter("transporter_cd");
String transporter_plant_seq=request.getParameter("transporter_plant_seq")==null?"0":request.getParameter("transporter_plant_seq");
String bu_seq=request.getParameter("bu_seq")==null?"0":request.getParameter("bu_seq");	// SagarB20250922 Added this variable for showing BU list
String chk_diff = request.getParameter("chk_diff")==null?"":request.getParameter("chk_diff");

//Send Value to Java
cont_mgmt.setCallFlag("NOM_ALLOC");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setFrom_dt(from_dt);
cont_mgmt.setTo_dt(to_dt);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setTransporter_cd(transporter_cd);
cont_mgmt.setTransporter_plant_seq(transporter_plant_seq);
cont_mgmt.setBu_seq(bu_seq);		// SagarB20250922 Added this variable for showing BU list
cont_mgmt.setChk_diff(chk_diff);
cont_mgmt.init();

// Get Vectors from Java
Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VMST_TRANSPORTER_CD = cont_mgmt.getVMST_TRANSPORTER_CD();
Vector VMST_TRANSPORTER_NM = cont_mgmt.getVMST_TRANSPORTER_NM();
Vector VMST_TRANSPORTER_ABBR = cont_mgmt.getVMST_TRANSPORTER_ABBR();
Vector VMST_TRANSPORTER_PLANT_SEQ = cont_mgmt.getVMST_TRANSPORTER_PLANT_SEQ();
Vector VMST_TRANSPORTER_PLANT_ABBR = cont_mgmt.getVMST_TRANSPORTER_PLANT_ABBR();


Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_PLANT_SEQ = cont_mgmt.getVTRANSPORTER_PLANT_SEQ();
Vector VTRANSPORTER_PLANT_ABBR = cont_mgmt.getVTRANSPORTER_PLANT_ABBR();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();
Vector VBU_CD = cont_mgmt.getVBU_CD();
Vector VBU_PLANT_SEQ = cont_mgmt.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = cont_mgmt.getVBU_PLANT_ABBR();

Vector VNOM_REV_NO = cont_mgmt.getVNOM_REV_NO();
Vector VGEN_TIME = cont_mgmt.getVGEN_TIME();
Vector VGEN_DT = cont_mgmt.getVGEN_DT();
Vector VBASE = cont_mgmt.getVBASE();
Vector VGCV = cont_mgmt.getVGCV();
Vector VNCV = cont_mgmt.getVNCV();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_SCM = cont_mgmt.getVQTY_SCM();
Vector VDCQ = cont_mgmt.getVDCQ();
Vector VMDCQ_QTY = cont_mgmt.getVMDCQ_QTY();
Vector VCONT_NAME = cont_mgmt.getVCONT_NAME();
Vector VCONT_REF = cont_mgmt.getVCONT_REF();

Vector VTAX_DTL = cont_mgmt.getVTAX_DTL();

Vector VCONT_NO = cont_mgmt.getVCONT_NO();
Vector VCONT_REV_NO = cont_mgmt.getVCONT_REV_NO();
Vector VAGMT_NO = cont_mgmt.getVAGMT_NO();
Vector VAGMT_REV_NO = cont_mgmt.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = cont_mgmt.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = cont_mgmt.getVDIS_CONT_MAPPING();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();
Vector VGAS_DT = cont_mgmt.getVGAS_DT();

Vector VQTY_SELLER_MMBTU = cont_mgmt.getVQTY_SELLER_MMBTU();
Vector VQTY_SELLER_SCM = cont_mgmt.getVQTY_SELLER_SCM();
Vector VQTY_ALLOC_MMBTU = cont_mgmt.getVQTY_ALLOC_MMBTU();
Vector VQTY_ALLOC_SCM = cont_mgmt.getVQTY_ALLOC_SCM();
Vector VCOLOR = cont_mgmt.getVCOLOR();
Vector VCOLOR_ALLOC = cont_mgmt.getVCOLOR_ALLOC();
Vector VAGMT_BASE = cont_mgmt.getVAGMT_BASE();
Vector VCONTRACT_TYPE_NM = cont_mgmt.getVCONTRACT_TYPE_NM();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_ContractMgmt">

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
					    	Buyer vs Seller Nomination vs Allocation
					    </div>
					    <div class="col-sm-7 col-xs-7 col-md-7">
					  		<div class="d-flex justify-content-end">
					  			<div class="form-group row">
									<div class="col-auto">
										<div class="btn-group" onclick="exportToXls();">
											<label><i class="fa fa-file-excel-o fa-2x excel_icon"></i></label>
										</div>
									</div>
								</div>
							</div>
					  	</div>		 
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-5 col-xs-5 col-md-5">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">	<!-- SagarB20250925 Removed refresh() function -->
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off"><!-- SagarB20250925 Removed refresh() function -->
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">	
						<div class="col-sm-3 col-xs-3 col-md-3">  	
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" >		<!-- SagarB20250925 Removed refresh() function -->
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>	
						</div>		
						<div class="col-sm-2 col-xs-2 col-md-2">  	
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Transporter</b></label>
								</div>
								<div class="col-auto">
								<select class="form-select form-select-sm" name="transporter_cd">		<!-- SagarB20250925 Removed refresh() function -->
									<option value="0">--Select--</option>
									<%for(int i=0;i<VMST_TRANSPORTER_CD.size();i++){ %>
									<option value="<%=VMST_TRANSPORTER_CD.elementAt(i)%>"><%=VMST_TRANSPORTER_ABBR.elementAt(i)%> - <%=VMST_TRANSPORTER_NM.elementAt(i) %></option>
									<%} %>
								</select>
								<script>document.forms[0].transporter_cd.value="<%=transporter_cd%>"</script>
								</div>
							</div>
						</div>					
						<div class="col-sm-2 col-xs-2 col-md-2"> 	<!-- SagarB20250923 Changed col-sm-3 col-xs-3 col-md-3 to col-sm-2 col-xs-2 col-md-2 due to alignment issues-->
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Transporter Plant</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="transporter_plant_seq" >		<!-- SagarB20250925 Removed refresh() function -->
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_TRANSPORTER_PLANT_SEQ.size();i++){ %>
											<option value="<%=VMST_TRANSPORTER_PLANT_SEQ.elementAt(i)%>"><%=VMST_TRANSPORTER_PLANT_ABBR.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].transporter_plant_seq.value="<%=transporter_plant_seq%>"</script>
								</div>
							</div>
						</div>
						
						<!-- SagarB20250922 Added below block for showing BU list -->
						<div class="col-sm-2 col-xs-2 col-md-2">  	<!-- SagarB20250923 Changed col-sm-3 col-xs-3 col-md-3 to col-sm-2 col-xs-2 col-md-2 due to alignment issues-->  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Business Unit</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="bu_seq" >
										<option value="0">--Select--</option>
										<%for(int i=0;i<VBU_PLANT_SEQ.size();i++){ %>
									<option value="<%=VBU_PLANT_SEQ.elementAt(i)%>"><%=VBU_PLANT_ABBR.elementAt(i)%></option>
									<%} %>
								</select>
								<script>document.forms[0].bu_seq.value="<%=bu_seq%>"</script>
								</div>
							</div>
						</div>
						
						<div class="col-sm-3 col-xs-3 col-md-3">   	<!-- SagarB20250923 Changed col-sm-3 col-xs-3 col-md-3 to col-sm-2 col-xs-2 col-md-2 due to alignment issues--> 
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label">
										<b>
											<input type="checkbox" class="form-check-input" name="chk_diff" <%if(chk_diff.equals("Y")){%>checked<%}%>>		<!-- SagarB20250925 Removed refresh() function -->
											&nbsp;Nomination Difference Only
										</b>
									</label>
								</div>
								
								<!-- SagarB20250925 Added below button for page refresh -->
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
					  			</div>
							</div>
						</div>
					</div>
				</div>	
				<div class="card-body cdbody">
				<%if(VTRANSPORTER_CD.size()>0)
				{ %>	
					<%int j=0,k=0,l=0,m=0;
					for(int i=0; i<VTRANSPORTER_CD.size(); i++)
					{ 						
						String trans_cd=""+VTRANSPORTER_CD.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					<%if(i!=0){ %>&nbsp;<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTRANSPORTER_ABBR.elementAt(i)%></label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%k=0;
						if(index > 0){ %>
							<%for(j=j;j<VTRANSPORTER_PLANT_SEQ.size(); j++) 
							{
								String trans_plant_seq=""+VTRANSPORTER_PLANT_SEQ.elementAt(j);
								int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(j));
								k+=1;
							%>
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=l%>">
    												<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    		<%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th rowspan="2">Gas Day</th>																	
																	<th rowspan="2">Customer</th>
																	<th rowspan="2">Customer Plant</th>
																	<th rowspan="2">Contract Type</th>
																	<th rowspan="2">Contract#<br>[Contract/Trade Ref#]</th>
																	<th rowspan="2">DCQ</th>
																	<th colspan="2">Buyer Nomination</th>	
																	<th colspan="2">Seller Nomination</th>
																	<th colspan="2">Allocation</th>																
																</tr>
																<tr>
																	<th>MMBTU</th>
																	<th>SCM</th>
																	<th>MMBTU</th>
																	<th>SCM</th>
																	<th>MMBTU</th>
																	<th>SCM</th>
																</tr>
															</thead>
															<tbody>
																<%m=0;
																if(sub_index>0){ %>
																	<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
																	{ 
																		m+=1;
																	%>
																		<tr>
																			<td align="center"><%=VGAS_DT.elementAt(l) %></td>
																			<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(l)%></td>
																			<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%></td>
																			<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(l)%></td>
																			<td>
																			<%if(VCONTRACT_TYPE.elementAt(l).equals("O") || VCONTRACT_TYPE.elementAt(l).equals("Q")) {%>
																				<%=VDIS_CONT_MAPPING.elementAt(l)%>
																				<%if(!VCONT_REF.elementAt(l).equals("")){%>
																					<br>(<%=VCONT_REF.elementAt(l)%>)
																				<%} %>
																			<%}else{ %>
																				<%=VDIS_CONT_MAPPING.elementAt(l)%>
																				<%if(VAGMT_BASE.elementAt(l).equals("D")){ %>&nbsp;<font style="background:#a6ff4d">(DLV)</font><%} %>
																				<%if(!VCONT_REF.elementAt(l).equals("")){%>
																					<br>(<%=VCONT_REF.elementAt(l)%>)
																				<%} %>
																			<%} %>
																			</td>
																			<td align="right"><%=VDCQ.elementAt(l) %></td>																			
																			<td align="right"><%=VQTY_MMBTU.elementAt(l) %></td>
																			<td align="right"><%=VQTY_SCM.elementAt(l) %></td>
																			<td align="right" style="color:<%=VCOLOR.elementAt(l)%>"><%=VQTY_SELLER_MMBTU.elementAt(l) %></td>
																			<td align="right"><%=VQTY_SELLER_SCM.elementAt(l) %></td>
																			<td align="right" style="color:<%=VCOLOR_ALLOC.elementAt(l)%>"><%=VQTY_ALLOC_MMBTU.elementAt(l) %></td>
																			<td align="right"><%=VQTY_ALLOC_SCM.elementAt(l) %></td>
																		</tr>
																		
																		<%if(m==sub_index)
																		{%>
																			<%l=l+1;
																			break;
																		}%>
																	<%} %>
																<%} %>
															</tbody>
														</table>
													</div>
												</div>
								      		</div>
								    	</div>
								    </div>
								</div>
								<%if(k==index)
								{
									j=j+1;
									break;
								}%>
							<%} %>
						<%} %>
						</div>
					</div>
					<%} %>
				<%}else{ %>
					<div align="center">
					<%=utilmsg.infoMessage("<b>No Buyer Nomination Done for Selected Gas Day!</b>") %>
					</div>
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

</form>
</body>
</html>