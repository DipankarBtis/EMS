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
	var segmentType = document.forms[0].segmentType.value;
	var prev_segmentType = document.forms[0].prev_segmentType.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var customer_cd = document.forms[0].customer_cd.value;
	
	var flag=true;
	var msg="";
	
	var count = compareDate(from_dt,to_dt);
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	
	if(prev_segmentType != segmentType)
	{
		customer_cd="0";
	}
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_gta_contract_summary.jsp?counterparty_cd="+counterparty_cd+"&u="+u+"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt+
				"&customer_cd="+customer_cd;
	
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
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var segmentType = document.forms[0].segmentType.value;
	var prev_segmentType = document.forms[0].prev_segmentType.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var customer_cd = document.forms[0].customer_cd.value;
	
	var url = "xls_gta_contract_summary.jsp?fileName=CT_Parking_Contract_Summary_Report.xls&counterparty_cd="+counterparty_cd+"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt+
	"&customer_cd="+customer_cd;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DB_GtaMaster_Report" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String customer_cd=request.getParameter("customer_cd")==null?"0":request.getParameter("customer_cd");

gta.setCallFlag("GTA_CONTRACT_SUMMARY");
gta.setComp_cd(owner_cd);
gta.setSegmentType(segmentType);
gta.setCounterparty_cd(counterparty_cd);
gta.setFrom_dt(from_dt);
gta.setTo_dt(to_dt);
gta.setCustomer_cd(customer_cd);
gta.init();

Vector VCOUNTERPARTY_CD = gta.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = gta.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = gta.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = gta.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = gta.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = gta.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = gta.getVCONT_NO();
Vector VCONT_REV_NO = gta.getVCONT_REV_NO();
Vector VAGMT_NO = gta.getVAGMT_NO();
Vector VAGMT_REV_NO = gta.getVAGMT_REV_NO();
Vector VMDQ = gta.getVMDQ();
Vector VENTRY_POINT_NAME = gta.getVENTRY_POINT_NAME();
Vector VEXIT_POINT_NAME = gta.getVEXIT_POINT_NAME();
Vector VSTART_DT = gta.getVSTART_DT();
Vector VEND_DT = gta.getVEND_DT();
Vector VDIS_CONT_MAPPING = gta.getVDIS_CONT_MAPPING();
Vector VCONT_REF = gta.getVCONT_REF();
Vector VENT_DT = gta.getVENT_DT();
Vector VENT_BY = gta.getVENT_BY();
Vector VALLOC_MIN_DT = gta.getVALLOC_MIN_DT();
Vector VALLOC_MAX_DT = gta.getVALLOC_MAX_DT();
Vector VBU_POINT = gta.getVBU_POINT();

Vector VENTRY_QTY_MMBTU = gta.getVENTRY_QTY_MMBTU();
Vector VENTRY_QTY_MMSCM = gta.getVENTRY_QTY_MMSCM();
Vector VEXIT_QTY_MMBTU = gta.getVEXIT_QTY_MMBTU();
Vector VEXIT_QTY_MMSCM = gta.getVEXIT_QTY_MMSCM();

Vector VSEGMENT = gta.getVSEGMENT();
Vector VSEGMENT_TYPE = gta.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = gta.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = gta.getVTEMP_SEGMENT_TYPE();
Vector VINDEX = gta.getVINDEX();

Vector VIMBALANCE_QTY = gta.getVIMBALANCE_QTY();
Vector VLINKED_SALES_CONT = gta.getVLINKED_SALES_CONT();

Vector VCUSTOMER_CD = gta.getVCUSTOMER_CD();
Vector VCUSTOMER_NM = gta.getVCUSTOMER_NM();
Vector VCUSTOMER_ABBR = gta.getVCUSTOMER_ABBR();
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
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
				    		CT | Parking Contract Summary Report 
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>      	
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Contract</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="segmentType" onchange="refresh()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VSEGMENT.size();i++){ %>
										<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].segmentType.value="<%=segmentType%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Transporter</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3" <%if(!segmentType.equals("C")){ %>style="display:none;"<%} %>>  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="customer_cd" onchange="refresh()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCUSTOMER_CD.size();i++){ %>
										<option value="<%=VCUSTOMER_CD.elementAt(i)%>"><%=VCUSTOMER_ABBR.elementAt(i)%> - <%=VCUSTOMER_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].customer_cd.value="<%=customer_cd%>"</script>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%int i=0;int k=0;
					for(int j=0; j<VTEMP_SEGMENT.size(); j++){ 
						int index = Integer.parseInt(""+VINDEX.elementAt(j));
					if(j!=0)
					{
					%>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div> 
					<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTEMP_SEGMENT.elementAt(j) %></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th rowspan="2">Sr#</th>
										<th rowspan="2">Transporter</th>
										<th rowspan="2">Contract#</th>
										<th rowspan="2">Contract Ref#</th>
										<th rowspan="2">Contract Period</th>
										<th rowspan="2">Business Unit</th>
										<th rowspan="2">Entry - Exit Point</th>
										<th rowspan="2">Contract Enter By/Dt </th>
										<th rowspan="2">MDQ<br>(MMBTU)</th>
										<th rowspan="2">Allocation<br>Start Date</th>
										<th rowspan="2" style="background:#000066;color:white;">Last<br>Allocation Date</th>
										<th colspan="2">Entry Energy</th>
										<th colspan="2">Exit Energy</th>
										<th rowspan="2">Cumulative<br>Imbalance</th>
										<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("K")){ %>
										<th rowspan="2" style="background:#b3f0ff;">Linked CT Contract</th>
										<%}else{ %>
										<th rowspan="2" style="background:#b3f0ff;">Linked Sales Contract</th>
										<%} %>
									</tr>
									<tr>
										<th>MMBTU</th>
										<th>MMSCM</th>
										<th>MMBTU</th>
										<th>MMSCM</th>
									</tr>
								</thead>
								<tbody>
								<%k=0;
								if(index > 0){ %>
									<%for(i=i;i<VCOUNTERPARTY_CD.size(); i++){ 
									k+=1;
									%>
										<tr>
											<td align="center"><%=k%></td>
											<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
											<td><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
											<td><%=VCONT_REF.elementAt(i)%></td>
											<td><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td><%=VBU_POINT.elementAt(i) %></td>
											<td align="center"><%=VENTRY_POINT_NAME.elementAt(i) %> - <%=VEXIT_POINT_NAME.elementAt(i) %></td>
											<td align="center"><%=VENT_BY.elementAt(i)%><br><%=VENT_DT.elementAt(i)%></td>
											<td align="right"><%=VMDQ.elementAt(i)%></td>
											<td align="center"><%=VALLOC_MIN_DT.elementAt(i)%></td>
											<td align="center"><%=VALLOC_MAX_DT.elementAt(i)%></td>
											<td align="right"><%=VENTRY_QTY_MMBTU.elementAt(i)%></td>
											<td align="right"><%=VENTRY_QTY_MMSCM.elementAt(i)%></td>
											<td align="right"><%=VEXIT_QTY_MMBTU.elementAt(i)%></td>
											<td align="right"><%=VEXIT_QTY_MMSCM.elementAt(i)%></td>
											<td align="right"><%=VIMBALANCE_QTY.elementAt(i)%></td>
											<td style="background:#b3f0ff;"><%=VLINKED_SALES_CONT.elementAt(i)%></td>
										</tr>
										<%if(k==index)
										{
											i=i+1;
											break;
										}%>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="17" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
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

<input type="hidden" name="prev_segmentType" value="<%=segmentType%>">

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