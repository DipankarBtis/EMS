<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var segmentType = document.forms[0].segmentType.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var flag=true;
	var msg="";
	
	var count = compareDate(from_dt,to_dt);
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	
	var read_access = document.forms[0].read_access.value;
	var write_access = document.forms[0].write_access.value;
	var check_access = document.forms[0].check_access.value;
	var print_access = document.forms[0].print_access.value;
	var delete_access = document.forms[0].delete_access.value;
	var audit_access = document.forms[0].audit_access.value;
	var authorize_access = document.forms[0].authorize_access.value;
	var approve_access = document.forms[0].approve_access.value;
	var execute_access = document.forms[0].execute_access.value;
	var formCd = document.forms[0].form_cd.value;
	var formNm = document.forms[0].form_nm.value;
	var mod_cd = document.forms[0].mod_cd.value;
	var mod_nm = document.forms[0].mod_nm.value;
	
	if(flag)
	{
		var url = "rpt_purchase_summary.jsp?counterparty_cd="+counterparty_cd+"&form_cd="+formCd+"&form_nm="+formNm+
				"&mod_cd="+mod_cd+"&mod_nm="+mod_nm+"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt+
				"&read_access="+read_access+"&write_access="+write_access+"&check_access="+check_access+
				"&print_access="+print_access+"&delete_access="+delete_access+"&audit_access="+audit_access+
				"&authorize_access="+authorize_access+"&approve_access="+approve_access+"&execute_access="+execute_access;
	
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
	var sysdate = document.forms[0].sysdate.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var segmentType = document.forms[0].segmentType.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	sysdate = sysdate.toString();
	sysdate = sysdate.split('/').join('');
	
	var url = "xls_purchase_summary.jsp?fileName=Purchse Summary "+sysdate+".xls&counterparty_cd="+counterparty_cd+"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchaseSum" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

purchaseSum.setCallFlag("PURCHASE_SUMMARY");
purchaseSum.setComp_cd(owner_cd);
purchaseSum.setSegmentType(segmentType);
purchaseSum.setCounterparty_cd(counterparty_cd);
purchaseSum.setFrom_dt(from_dt);
purchaseSum.setTo_dt(to_dt);
purchaseSum.init();

Vector VCOUNTERPARTY_CD = purchaseSum.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = purchaseSum.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = purchaseSum.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = purchaseSum.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = purchaseSum.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = purchaseSum.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = purchaseSum.getVCONT_NO();
Vector VCONT_REV_NO = purchaseSum.getVCONT_REV_NO();
Vector VCONT_NAME = purchaseSum.getVCONT_NAME();
Vector VSTART_DT = purchaseSum.getVSTART_DT();
Vector VEND_DT = purchaseSum.getVEND_DT();
Vector VRATE = purchaseSum.getVRATE();
Vector VRATE_UNIT = purchaseSum.getVRATE_UNIT();
Vector VCONT_STATUS = purchaseSum.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = purchaseSum.getVCONT_STATUS_FLG();
Vector VPRICE_TYPE = purchaseSum.getVPRICE_TYPE();
Vector VBOOKED_QTY = purchaseSum.getVBOOKED_QTY();
Vector VAGMT_NO = purchaseSum.getVAGMT_NO();
Vector VAGMT_REV_NO = purchaseSum.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = purchaseSum.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = purchaseSum.getVCONTRACT_TYPE_NM();
Vector VMIN_ALLOC_DT = purchaseSum.getVMIN_ALLOC_DT();
Vector VMAX_ALLOC_DT = purchaseSum.getVMAX_ALLOC_DT();
Vector VUNLOADED_QTY = purchaseSum.getVUNLOADED_QTY();
Vector VAVAILABLE_FOR_SALE = purchaseSum.getVAVAILABLE_FOR_SALE();
Vector VBU_POINT=purchaseSum.getVBU_POINT();
Vector VTRADER_PLANT=purchaseSum.getVTRADER_PLANT();
Vector VCONT_REF_NO=purchaseSum.getVCONT_REF_NO();

Vector VDISPLAY_SEGMENT = purchaseSum.getVDISPLAY_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = purchaseSum.getVTEMP_SEGMENT_TYPE();
Vector VSEGMENT_TYPE = purchaseSum.getVSEGMENT_TYPE();
Vector VDISPLAY_SEGMENT_TYP = purchaseSum.getVDISPLAY_SEGMENT_TYP();
Vector VINDEX = purchaseSum.getVINDEX();

double totalQty=purchaseSum.getTotalQty();
double UnloadedtotalQty=purchaseSum.getUnloadedtotalQty();
double AvailabletotalQty=purchaseSum.getAvailabletotalQty();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
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
					    	Purchase Summary Report
					    </div>
					    <div align="right" onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
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
										<option value="0">--All--</option>
										<%for(int i=0;i<VDISPLAY_SEGMENT_TYP.size();i++){ %>
										<option value="<%=VDISPLAY_SEGMENT_TYP.elementAt(i)%>"><%=VDISPLAY_SEGMENT.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].segmentType.value="<%=segmentType%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--All--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_ABBR.size();i++){ %>
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
				<div class="card-body cdbody">
					<%int i=0;int k=0;
					for(int j=0; j<VTEMP_SEGMENT_TYPE.size(); j++){ 
						int index = Integer.parseInt(""+VINDEX.elementAt(j));
						String seg_type="";
						if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("D")){
							seg_type="Domestic NG Purchase";
						}
						else if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("I"))
						{
							seg_type="IGX Purchase";
						}
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=seg_type%></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Counterparty</th>
										<th>Contract#</th>
										<%if(VDISPLAY_SEGMENT_TYP.elementAt(j).equals("I")){ %>
										<th>Trade Ref#</th>
										<%}else{ %>
										<th>Cont Ref#</th>
										<%} %>
										<th>Contract Period</th>
										<th>Status</th>
										<th>Price Type</th>
										<th>Price</th>
										<th>Currency/MMBTU</th>
										<th>Business Unit</th>
										<th>Trader Plants</th>
										<th>Allocation Start Date</th>
										<th>Last Allocation Date</th>
										<th>MMBTU Booked</th>
										<th>MMBTU Unloaded</th>
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
											<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
											<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCONTRACT_TYPE.elementAt(i)%><%=VCONT_NO.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
											<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
											<td align="right"><%=VRATE.elementAt(i)%></td>
											<td align="center"><%=VRATE_UNIT.elementAt(i)%></td>
											<td align="center"><%=VBU_POINT.elementAt(i)%></td>
											<td align="center"><%=VTRADER_PLANT.elementAt(i)%></td>
											<td align="center"><%=VMIN_ALLOC_DT.elementAt(i) %></td>
											<td align="center"><%=VMAX_ALLOC_DT.elementAt(i) %></td>
											<td align="right"><%=VBOOKED_QTY.elementAt(i)%></td>
											<td align="right"><%=VUNLOADED_QTY.elementAt(i)%></td>
										</tr>
										<%
											if(k==index)
											{
												i=i+1;
												break;
											}%>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
								<%-- <tbody>
									<tr>
										<td colspan="12" align="right">Total :</td>
										<td align="right"><%=totalQty%></td>
										<td align="right"><%=UnloadedtotalQty%></td>
										<td align="right"><%=AvailabletotalQty%></td>
									</tr>
								</tbody> --%>
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
</form>
</body>
</html>