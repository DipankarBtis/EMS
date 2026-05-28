<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var from_dt=document.forms[0].from_dt.value;
	var to_dt=document.forms[0].to_dt.value;
	var sysdate = document.forms[0].sysdate.value;
	
	var flag=checkDateRangeOnApply(document.forms[0].from_dt,document.forms[0].to_dt);
	
	if(flag)
	{
		if(trim(from_dt)!="" && trim(to_dt)!="")
		{
			var url = "rpt_azs_service_bus.jsp?from_dt="+from_dt+"&to_dt="+to_dt;

			document.getElementById("loading").style.visibility = "visible";
			location.replace(url);
		}
		else
		{
			var msg="";
			if(trim(from_dt)=="")
			{
				msg+="From date should be empty!\n";
			}
			if(trim(to_dt)=="")
			{
				msg+="To date should be empty!\n";
			}
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_azs_service" id="azs_service" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<% 
String sysdate=utildate.getSysdate();
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

azs_service.setCallFlag("BUS_API_RPT");
azs_service.setComp_cd(owner_cd);
azs_service.setFrom_dt(from_dt);
azs_service.setTo_dt(to_dt);
azs_service.init();

Vector VSEGMENT_CD = azs_service.getVSEGMENT_CD();
Vector VSEGMENT_NM = azs_service.getVSEGMENT_NM();

Vector VRECORD_ID = azs_service.getVRECORD_ID();
Vector VBLOB = azs_service.getVBLOB();
Vector VDATA_TYPE = azs_service.getVDATA_TYPE();
Vector VSUB_DATA_TYPE = azs_service.getVSUB_DATA_TYPE();
Vector VCOUNTERPARTY = azs_service.getVCOUNTERPARTY();
Vector VEMAIL_PATH = azs_service.getVEMAIL_PATH();
Vector VEMAIL_SUBJECT = azs_service.getVEMAIL_SUBJECT();
Vector VREPORT_DT = azs_service.getVREPORT_DT();
Vector VGAS_DAY = azs_service.getVGAS_DAY();
Vector VSL_NO = azs_service.getVSL_NO();
Vector VCTR_NO = azs_service.getVCTR_NO();
Vector VCUSTOMER_NM = azs_service.getVCUSTOMER_NM();
Vector VNOMINATION_QTY = azs_service.getVNOMINATION_QTY();
Vector VCONFIRM_QTY = azs_service.getVCONFIRM_QTY();
Vector VSCH_QTY = azs_service.getVSCH_QTY();
Vector VALLOC_QTY = azs_service.getVALLOC_QTY();
Vector VINDEX = azs_service.getVINDEX();
Vector VMSG_ID = azs_service.getVMSG_ID();
Vector VSEQ_NO = azs_service.getVSEQ_NO();
Vector VENT_DT = azs_service.getVENT_DT();
Vector VEMS_PROCESSED = azs_service.getVEMS_PROCESSED();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post">
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
					    	Azure Service Bus API Report
					    </div>
					    <!-- <div class="row justify-content-end">
					    	<div class="col-auto">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
							</div>
					    </div> -->
					</div>
				</div>
				<!-- Below body is for applying filters -->
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">
						</div>
						<div class="col-sm-7 col-xs-7 col-md-7">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From </b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm">
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" >
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
								</div>
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
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
					for(l=0;l<VSEGMENT_CD.size();l++){
						int index = Integer.parseInt(""+VINDEX.elementAt(l));
					%>
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading1">
											<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l %>" aria-expanded="false" aria-controls="collapse<%=l%>"><%=VSEGMENT_NM.elementAt(l) %>&nbsp;<font color="blue">(<%=index%> Items)</font></button>	
							    		</h2>
							    		<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading1">
							    			<div class="accordion-body accor-body">
							    				<div class="row">
							    					<%if(index>0){%>
							    						<div class="col-md-12 col-sm-12 col-xs-12">
									    					<div align="right">
									    						<input class="form-control form-control-sm" type="text" id="globalSearch" onkeyup="globalSearchTable(this,'<%=l %>')" placeholder="Search.." style="width:200px"/>
									    					</div>
								    					</div>
							    					<%} %>
							    					<div class="table-responsive">
														<table class="table table-bordered ems_sorttbl" id="ems_table<%=l%>">
															<thead id="ems_tbsort<%=l%>">
																<tr>
																	<th class="ems_thsort<%=l%>">Entry Date</th>
																	<th class="ems_thsort<%=l%>">Seq No.</th>
																	<th class="ems_thsort<%=l%>">Data Type </th>
																	<th class="ems_thsort<%=l%>">Sub Data Type</th>
																	<th class="ems_thsort<%=l%>">Counterparty</th>
																	<th class="ems_thsort<%=l%>">Report Date</th>
																	<th class="ems_thsort<%=l%>">Gas Day</th>
																	<th class="ems_thsort<%=l%>">SL No</th>
																	<th class="ems_thsort<%=l%>">CTR No</th>
																	<th class="ems_thsort<%=l%>">Customer Name</th>
																	<th class="ems_thsort<%=l%>">Nomination Qty</th>
																	<th class="ems_thsort<%=l%>">Confirm Qty</th>
																	<th class="ems_thsort<%=l%>">Schedule Qty</th>
																	<th class="ems_thsort<%=l%>">Allocation Qty</th>
																	<th class="ems_thsort<%=l%>">EMS Processed</th>
																	<th class="ems_thsort<%=l%>">Message Id</th>
																	<th class="ems_thsort<%=l%>">Record Id</th>
																	<th class="ems_thsort<%=l%>">Blob</th>
																	<th class="ems_thsort<%=l%>">Email Path</th>
																	<th class="ems_thsort<%=l%>">Email Subject</th>
																</tr>
															</thead>
															<tbody>
																<% k=0;
																if(index > 0){ %>
																	<%for(i=i; i<VRECORD_ID.size(); i++){
																	k+=1;
																	%>
																		<tr>
																			<td align="center"><%=VENT_DT.elementAt(i) %></td>
																			<td align="center"><%=VSEQ_NO.elementAt(i)%></td>
																			<td align="center"><%=VDATA_TYPE.elementAt(i) %></td>
																			<td align="center"><%=VSUB_DATA_TYPE.elementAt(i) %></td>
																			<td align="center"><%=VCOUNTERPARTY.elementAt(i) %></td>
																			<td align="center"><%=VREPORT_DT.elementAt(i) %></td>
																			<td align="center"><%=VGAS_DAY.elementAt(i) %></td>
																			<td align="center"><%=VSL_NO.elementAt(i) %></td>
																			<td align="center"><%=VCTR_NO.elementAt(i) %></td>
																			<td align="center"><%=VCUSTOMER_NM.elementAt(i) %></td>
																			<td align="right"><%=VNOMINATION_QTY.elementAt(i) %></td>
																			<td align="right"><%=VCONFIRM_QTY.elementAt(i) %></td>
																			<td align="right"><%=VSCH_QTY.elementAt(i) %></td>
																			<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
																			<td align="center"><%=VEMS_PROCESSED.elementAt(i) %></td>
																			<td align="center"><%=VMSG_ID.elementAt(i) %></td>
																			<td align="center"><%=VRECORD_ID.elementAt(i) %></td>
																			<td align="center"><%=VBLOB.elementAt(i) %></td>
																			<td align="center"><%=VEMAIL_PATH.elementAt(i) %></td>
																			<td align="center"><%=VEMAIL_SUBJECT.elementAt(i) %></td>
																		</tr>
																		<%if(k==index)
																		{i=i+1;
																			break;
																		} %>
																	<%} %>
																<%}else{%>
																	<tr>
																		<td colspan="20" align="center"><%=utilmsg.infoMessage("<b>No Records Found!</b>") %></td>
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
<input type="hidden" name="sysdate" value="<%=sysdate%>">

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