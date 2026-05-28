<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page import="java.util.*" %>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var u = document.forms[0].u.value;
	
	
		var url = "rpt_total_quantity_supplied.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&u="+u;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="mgmt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

mgmt_rpt.setCallFlag("TOTAL_SUPPLIED_QTY");
mgmt_rpt.setComp_cd(owner_cd);
mgmt_rpt.setFrom_dt(from_dt);
mgmt_rpt.setTo_dt(to_dt);
mgmt_rpt.init();

String total_mmbtu = mgmt_rpt.getTotal_mmbtu();
String total_scm = mgmt_rpt.getTotal_scm();
Vector VCOUNTERPARTY_CD = mgmt_rpt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = mgmt_rpt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = mgmt_rpt.getVCOUNTERPARTY_ABBR();
Vector VSUPPLIED_QTY_MMBTU = mgmt_rpt.getVSUPPLIED_QTY_MMBTU();
Vector VSUPPLIED_QTY_MMSCM = mgmt_rpt.getVSUPPLIED_QTY_MMSCM();
Vector VGAS_DT = mgmt_rpt.getVGAS_DT();
Vector VTOTALSUPPLIED_SCM = mgmt_rpt.getVTOTALSUPPLIED_SCM();
Vector VTOTALSUPPLIED_MMBTU = mgmt_rpt.getVTOTALSUPPLIED_MMBTU();
Vector VINDEX = mgmt_rpt.getVINDEX();
Vector VCOLOR = mgmt_rpt.getVCOLOR();
Vector V_COLOR = mgmt_rpt.getV_COLOR();
Vector VGRANDTOTALSUPPLIED_MMBTU = mgmt_rpt.getVGRANDTOTALSUPPLIED_MMBTU();
Vector VGRANDTOTALSUPPLIED_SCM = mgmt_rpt.getVGRANDTOTALSUPPLIED_SCM();
Vector VGRAND_COLOR = mgmt_rpt.getVGRAND_COLOR();
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
					    	Total Quantity Supplied
					    </div>
					    <a href="../contract_mgmt/xls_total_quantity_supplied.jsp?fileName=Total Quantity Supplied Report.xls&from_dt=<%=from_dt %>&to_dt=<%=to_dt %>" download="Total Quantity Supplied Report.xls" >
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a>
					</div>
				</div>
				<div class="card-body cdbody">
					<div align="center">
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);checkDateRange(this,document.forms[0].to_dt);" onchange="validateDate(this);checkDateRange(this,document.forms[0].to_dt);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);checkDateRange(document.forms[0].from_dt,this);" onchange="validateDate(this);checkDateRange(document.forms[0].from_dt,this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th></th>
										<th colspan="2" align="center">Total Quantity Supplied</th>
										<%for (int i=0; i<VCOUNTERPARTY_CD.size(); i++) {%>
											<th colspan="2" align="center">Total Quantity Supplied to <br><%=VCOUNTERPARTY_NM.elementAt(i) %></th>
										<%} %>
									</tr>
									<tr>
										<th>Gas Day Starting <br>Date/Time</th>
										<th align="center">MMBTU</th>
										<th align="center">SCM</th>
										<%for (int i=0; i<VCOUNTERPARTY_CD.size(); i++) {%>
											<th align="center">MMBTU</th>
											<th align="center">SCM</th>
										<%} %>
									</tr>
								</thead>
								<tbody>
								<%int a=0; int j=0; int k=0;
								for(j=0; j<VGAS_DT.size(); j++) {
								int index = Integer.parseInt(""+VINDEX.elementAt(j));
								k+=1;%>
									<tr>
										<td align="center"><b><%=VGAS_DT.elementAt(j) %></b></td>
										<td align="right" style="background:<%=V_COLOR.elementAt(j)%>;"><%=VTOTALSUPPLIED_MMBTU.elementAt(j) %></td>
										<td align="right" style="background:<%=V_COLOR.elementAt(j)%>;"><%=VTOTALSUPPLIED_SCM.elementAt(j) %></td>
										<%for (int i=0; i<VCOUNTERPARTY_CD.size(); i++) {%>
											<td align="right" style="background:<%=VCOLOR.elementAt(a)%>;"><%=VSUPPLIED_QTY_MMBTU.elementAt(a) %></td>
											<td align="right" style="background:<%=VCOLOR.elementAt(a)%>;"><%=VSUPPLIED_QTY_MMSCM.elementAt(a) %></td>
										<%a++;} %>
									</tr>
								<%} %>
									<tr>
										<td align="right"><b>Total :</b></td>
										<td align="right"><b><%=total_mmbtu %></b></td>
										<td align="right"><b><%=total_scm %></b></td>
										<%for (int i=0; i<VCOUNTERPARTY_CD.size(); i++) {%>
											<td align="right"><b><%=VGRANDTOTALSUPPLIED_MMBTU.elementAt(i) %></b></td>
											<td align="right"><b><%=VGRANDTOTALSUPPLIED_SCM.elementAt(i) %></b></td>
										<%} %>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
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