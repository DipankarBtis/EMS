<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function refresh()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
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
				var url="rpt_lng_cargo_forex_dtl.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&u="+u;
				document.getElementById("loading").style.visibility = "visible";
				location.replace(url);
			}
		}
	}
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var temp_fromdt = from_dt.replaceAll("/", "_"); 
	var temp_todt = to_dt.replaceAll("/", "_"); 
	
	var owner_cd = document.forms[0].owner_cd.value;
	var owner_abbr = document.forms[0].owner_abbr.value;
	
	var filename =filename = owner_abbr+"_LNG_Forex_Detail.xls";
	var url = "xls_lng_cargo_forex_dtl.jsp?fileName="+filename+"&from_dt="+from_dt+"&to_dt="+to_dt+"&owner_cd="+owner_cd;
	location.replace(url);
} 

</script>
<%@ include file="../util/common_js.jsp"%>
<jsp:useBean class="com.etrm.fms.purchase.DB_PurchaseReports" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
</head>
<%
String sysdate=utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

purchase.setCallFlag("LNG_CARGO_DTL");
purchase.setComp_cd(owner_cd);
purchase.setFrom_dt(from_dt);
purchase.setTo_dt(to_dt);
purchase.init();

Vector VCOUNTERPTY_CD = purchase.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_NM = purchase.getVCOUNTERPTY_NM();
Vector VCOUNTERPTY_ABBR = purchase.getVCOUNTERPTY_ABBR();

Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VAGMT_REV_NO = purchase.getVAGMT_REV_NO();
Vector VCONT_NO = purchase.getVCONT_NO();
Vector VCONT_REV_NO = purchase.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VDEAL_NO = purchase.getVDEAL_NO();
Vector VINVOICE_NO = purchase.getVINVOICE_NO();
Vector VALLOC_QTY = purchase.getVALLOC_QTY();
Vector VSALES_PRICE = purchase.getVSALES_PRICE();
Vector VEXCHNAGE_RATE = purchase.getVEXCHNAGE_RATE();
Vector VEXCHNAGE_RATE_DATE = purchase.getVEXCHNAGE_RATE_DATE();

Vector VARRIVAL_DT = purchase.getVARRIVAL_DT();
Vector VSHIP_NM = purchase.getVSHIP_NM();
Vector VEXCH_1 = purchase.getVEXCH_1();
Vector VEXCH_2 = purchase.getVEXCH_2();
Vector VEXCH_3 = purchase.getVEXCH_3();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex  justify-content-between">
						<div class="topheader">
							LNG Cargo Forex Details
						</div>
					<div class="col-auto" onclick="exportToXls();" style="color:green;">
						<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					</div>	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row"></div>
						</div>
					<div class="col-sm-4 col-xs-4 col-md-4">
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
						</div>
					</div>
					<div class="col-sm-3 col-xs-3 col-md-3">
						<div class="form-group row">
							<div class="col-auto">
								<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
							</div>
						</div>
					</div>
					<div class="col-md-2 col-sm-0 col-xs-0"></div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover ">
								<thead>
									<tr>
										 <th>Cargo Arrival Date</th>
										 <th>Cargo Name</th>
										 <th>Name of LNG Supplier</th>
										 <th>Invoice No#</th>
										 <th>LNG Quantity(MMBTU)</th>
										 <th>LNG Price(USD/MMBTU)</th>
										 <th>Exchange Rate</th>
										 <th>Exchange Rate Date</th>
										 <th>SBI FIRST RATE TT BUY</th>
										 <th>SBI FIRST RATE TT SELL</th>
										 <th>SBI FIRST RATE TT BUY SELL</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPTY_CD.size()>0){ %>
								<%int K=0;%>
								<% for(int i=0;i<VCOUNTERPTY_CD.size();i++){ 
								K+=1; %>
									<tr>
										<td align="center"><%=VARRIVAL_DT.elementAt(i) %></td>
										<td align="center"><%=VSHIP_NM.elementAt(i) %></td>
										<td align="center" title="<%=VCOUNTERPTY_NM.elementAt(i)%>"><%=VCOUNTERPTY_ABBR.elementAt(i) %></td>
										<td align="center" title="<%=VDEAL_NO.elementAt(i)%>"><%=VINVOICE_NO.elementAt(i) %></td>
										<td align="right"><%=VALLOC_QTY.elementAt(i) %></td>
										<td align="right"><%=VSALES_PRICE.elementAt(i) %></td>
										<td align="right"><%=VEXCHNAGE_RATE.elementAt(i) %></td>
										<td align="center"><%=VEXCHNAGE_RATE_DATE.elementAt(i) %></td>
										<td align="right"><%=VEXCH_1.elementAt(i) %></td>
										<td align="right"><%=VEXCH_2.elementAt(i) %></td>
										<td align="right"><%=VEXCH_3.elementAt(i) %></td>
									</tr>
								<%}%>
								<%} else{ %>
									<tr>
										<td colspan = "11" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated!</b>") %></td>
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
<input type="hidden" name="owner_cd" value="<%=owner_cd%>">
<input type="hidden" name="owner_abbr" value="<%=owner_abbr%>">

</form>
</body>
</html>