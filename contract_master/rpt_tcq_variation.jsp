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
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var flag=true;
	var msg="";
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_tcq_variation.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
				"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_master.DB_ContractMaster_Report" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

contract.setCallFlag("TCQ_VARIATION");
contract.setComp_cd(owner_cd);
contract.setSegmentType(segmentType);
contract.setCounterparty_cd(counterparty_cd);
contract.setFrom_dt(from_dt);
contract.setTo_dt(to_dt);
contract.init();

Vector VSEGMENT = contract.getVSEGMENT();
Vector VSEGMENT_TYPE = contract.getVSEGMENT_TYPE();
Vector VMST_COUNTERPARTY_CD = contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = contract.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = contract.getVMST_COUNTERPARTY_NM();
Vector VTEMP_SEGMENT = contract.getVTEMP_SEGMENT();
Vector VINDEX = contract.getVINDEX();
Vector VTEMP_SEGMENT_TYPE = contract.getVTEMP_SEGMENT_TYPE();
Vector VCOUNTERPARTY_CD = contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = contract.getVCOUNTERPARTY_ABBR();
Vector VDIS_CONT_MAPPING = contract.getVDIS_CONT_MAPPING();
Vector VCONT_REF = contract.getVCONT_REF();
Vector VSIGNING_DT = contract.getVSIGNING_DT();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VIS_ALLOCATED = contract.getVIS_ALLOCATED();
Vector VCONT_STATUS = contract.getVCONT_STATUS();
Vector VFINAL_TCQ = contract.getVFINAL_TCQ();
Vector VTCQ = contract.getVTCQ();
Vector VVARIATION_TCQ = contract.getVVARIATION_TCQ();
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
				    		Contract TCQ Variation 
	   	 				</div>
	   	 				<a href="../contract_master/xls_tcq_variation.jsp?fileName=TcqVariationReport.xls&company_cd=<%=comp_cd %>&from_dt=<%=from_dt %>&to_dt=<%=to_dt %>&segmentType=<%=segmentType %>&counterparty_cd=<%=counterparty_cd %>" download="TCQ Variation Report.xls">
						 	<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</a>
				    </div>
				</div>      	
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Contract</b></label>
								</div>
								<div class="col-auto">
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
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col-auto">
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
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onchange="validateDate(this);checkDateRange(this,document.forms[0].to_dt);refresh();" onblur="validateDate(this);checkDateRange(this,document.forms[0].to_dt);"  >
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onchange="validateDate(this);checkDateRange(document.forms[0].from_dt,this);refresh();" onblur="validateDate(this);checkDateRange(document.forms[0].from_dt,this);"  >
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
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
										<th>Sr#</th>
										<th>Customer</th>
										<th>Contract#</th>
										<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("X")){ %>
										<th>Trade Ref#</th>
										<%}else{ %>
										<th>Contract Ref#</th>
										<%} %>
										<th>Signing Date</th>
										<th>Contract Period</th>
										<!-- <th>Contract Closure Date</th> -->
										<th>Status</th>	
										<th>Final TCQ <br>(MMBTU)</th>																			
										<th>Contractual TCQ<br>(MMBTU)</th>
										<th>Variation<br>(MMBTU)</th>
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
											<td><%=VSIGNING_DT.elementAt(i)%></td>
											<td><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<!-- <td></td> -->
											<td <%if(VIS_ALLOCATED.elementAt(i).equals("Y")){ %>style="background:#99ffcc;"<%}else{ %>style="background:#ffffcc;"<%} %>>
												<%=VCONT_STATUS.elementAt(i)%>
											</td>
											<td align="right"><%=VFINAL_TCQ.elementAt(i) %></td>
											<td align="right"><%=VTCQ.elementAt(i) %></td>
											<td align="right"><%=VVARIATION_TCQ.elementAt(i) %></td>
										</tr>
										<%if(k==index)
										{
											i=i+1;
											break;
										}%>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
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