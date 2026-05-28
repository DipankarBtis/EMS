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

	var flag=true;
	var msg="";
	
	var count = compareDate(from_dt,to_dt);
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_pseudo_transfer.jsp?&u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt;
	
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
	var from_dt = document.forms[0].from_dt;
	var to_dt = document.forms[0].to_dt;
	var comp_abbr = document.forms[0].comp_abbr.value;
	
	sysdate = sysdate.toString();
	sysdate = sysdate.split('/').join('');
	
	var url = "xls_pseudo_transfer.jsp?fileName="+comp_abbr+"-Pseudo Transfer Report "+sysdate+".xls&from_dt="+from_dt.value+"&to_dt="+to_dt.value;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_EnergyBank" id="energyBank" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

energyBank.setCallFlag("PSEUDO_TRANSFER_REPORT");
energyBank.setComp_cd(owner_cd);
energyBank.setFrom_Dt(from_dt);
energyBank.setTo_Dt(to_dt);
energyBank.init();

String comp_abbr = energyBank.getComp_Abbr();

Vector VCOUNTERPARTY_CD = energyBank.getVCOUNTERPARTY_CD();
Vector VTRANSFER_TYPE = energyBank.getVTRANSFER_TYPE();
Vector VENT_DT = energyBank.getVENT_DT();
Vector VEMP_NM = energyBank.getVEMP_NM();
Vector VALLOC_QTY_TO_CUST = energyBank.getVALLOC_QTY_TO_CUST();
Vector VSOURCE_CARGO_DTL = energyBank.getVSOURCE_CARGO_DTL();
Vector VDESTINATION_CARGO_DTL = energyBank.getVDESTINATION_CARGO_DTL();
Vector VCUST_DISPLAY_CONT_DTL = energyBank.getVCUST_DISPLAY_CONT_DTL();
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
					    	Pseudo Transfer Report
					    </div>
					    <div align="right" onclick="exportToXls();" style="color:green;">
								<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-4 col-xs-4 col-md-4">  
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
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt %>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
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
										<th>Sr#</th>
										<!-- <th>Pseudo Cargo</th> -->										
										<th>Source#</th>
										<th>Destination#</th>
										<th>Volume Transferred</th>
										<th>Transfer Type</th>
										<th>Enter Date</th>
										<th>Enter By</th>
									</tr>
								</thead>
								<tbody>
									<%if(VCOUNTERPARTY_CD.size()!=0){
										for(int i=0;i<VCOUNTERPARTY_CD.size();i++){
									%>
									<tr>
										<td align="center"><%=i+1%></td>
										<%-- <td align="center"><%=VCUST_DISPLAY_CONT_DTL.elementAt(i) %></td> --%>
										<td align="center"><%=VSOURCE_CARGO_DTL.elementAt(i) %></td>
										<td align="center"><%=VDESTINATION_CARGO_DTL.elementAt(i)%></td>
										<td align="center"><%=VALLOC_QTY_TO_CUST.elementAt(i)%></td>
										<td align="center"><%=VTRANSFER_TYPE.elementAt(i)%></td>
										<td align="center"><%=VENT_DT.elementAt(i)%></td>
										<td align="center"><%=VEMP_NM.elementAt(i)%></td>
									</tr>
									<%}
									}else{%>
										<tr>
											<td colspan="7" align="center"><%=utilmsg.infoMessage("<b>No Pseudo Logs are Available!</b>") %></td>
										</tr>
									<%}%>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row ">
						<div align="center"><%=utilmsg.infoMessage("<b>Note: Negative value indicates MMBTU returned to Pseudo Cargo!</b>") %></div>
   					</div>
   				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="comp_abbr" value="<%=comp_abbr%>">
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