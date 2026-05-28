<%@page import="java.util.Vector"%>
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
	var contract_map = document.forms[0].contract_map.value;
	
	var prev_counterparty_cd = document.forms[0].prev_counterparty_cd.value;
	if(prev_counterparty_cd!=counterparty_cd)
	{
		contract_map="";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_credit_exceed_days_approval.jsp?u="+u+
			"&counterparty_cd="+counterparty_cd+"&contract_map="+contract_map;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var creditExceedWindow;
function creditExceedDaysConfig(cont_type,agmt,agmt_rev,cont,cont_rev,counterparty_cd,temp_start_dt,temp_end_dt)
{
	var isDealFm="N";
	
	var u = document.forms[0].u.value;
	
	var url = "../credit_risk/frm_credit_exceed_days.jsp?counterparty_cd="+counterparty_cd+"&cont_no="+cont+"&cont_rev_no="+cont_rev+
		"&agmt_no="+agmt+"&agmt_rev_no="+agmt_rev+"&contract_type="+cont_type+
		"&start_dt="+temp_start_dt+"&end_dt="+temp_end_dt+"&isDealFm="+isDealFm+
		"&u="+u;
	
	if(!creditExceedWindow || creditExceedWindow.closed)
	{
		creditExceedWindow = window.open(url,"Credit Exceed Days Configuration","top=10,left=100,width=1200,height=600,scrollbars=1");
	}
	else
	{
		creditExceedWindow.close();
		creditExceedWindow = window.open(url,"Credit Exceed Days Configuration","top=10,left=100,width=1200,height=600,scrollbars=1");
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_CreditRisk" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_map=request.getParameter("contract_map")==null?"":request.getParameter("contract_map");

credit_risk.setCallFlag("CONT_LIST_FOR_EXCEED_CREDIT");
credit_risk.setCounterparty_cd(counterparty_cd);
credit_risk.setCont_mapp(contract_map);
credit_risk.setComp_cd(owner_cd);

credit_risk.init();

Vector VCOUNTERPARTY_CD = credit_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NAME = credit_risk.getVCOUNTERPARTY_NAME();
Vector VCOUNTERPARTY_ABBR = credit_risk.getVCOUNTERPARTY_ABBR();
Vector VCONTRACT_TYPE = credit_risk.getVCONTRACT_TYPE();
Vector VCONT_NO = credit_risk.getVCONT_NO();
Vector VCONT_REV_NO = credit_risk.getVCONT_REV_NO();
Vector VAGMT_NO = credit_risk.getVAGMT_NO();
Vector VAGMT_REV_NO = credit_risk.getVAGMT_REV_NO();
Vector VSTART_DT = credit_risk.getVSTART_DT();
Vector VEND_DT = credit_risk.getVEND_DT();
Vector VCONT_REF_NO = credit_risk.getVCONT_REF_NO();
Vector VDIS_CONT_MAPP = credit_risk.getVDIS_CONT_MAPP();
Vector VCOUNT_CREDIT_LINE=credit_risk.getVCOUNT_CREDIT_LINE();

Vector VMST_COUNTERPARTY_CD=credit_risk.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = credit_risk.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = credit_risk.getVMST_COUNTERPARTY_NM();
Vector VCONT_MAP_LIST = credit_risk.getVCONT_MAP_LIST();
Vector VDIS_CONT_MAP_LIST = credit_risk.getVDIS_CONT_MAP_LIST();

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
					    	Credit Exceed Days
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
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
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Contract</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="contract_map" onchange="refresh()">
										<option value="">--All--</option>
										<%for(int i=0;i<VDIS_CONT_MAP_LIST.size();i++){ %>
										<option value="<%=VCONT_MAP_LIST.elementAt(i)%>"><%=VDIS_CONT_MAP_LIST.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].contract_map.value="<%=contract_map%>"</script>
								</div>
							</div>
						</div>
					</div>	
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						<%if(!counterparty_cd.equals("0")){ %>
							<div class="table-responsive">
								<table class="table table-bordered table-hover" id="example">
									<thead>
										<tr>
											<th>SR#</th>
											<th>Counterparty</th>
											<th>Contract#</th>
											<th>Contract/Trade Ref#</th>
											<th>Contract Period</th>
											<th>#Credit Lines</th>
											<th>Credit Exceed Days</th>
										</tr>
									</thead>
									<tbody>
									<%if(VCOUNTERPARTY_CD.size() > 0){ %>
										<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<tr>
											<td align="center"><%=i+1%></td>
											<td align="left"><%=VCOUNTERPARTY_NAME.elementAt(i)%></td>
											<td align="center"><%=VDIS_CONT_MAPP.elementAt(i)%></td>
											<td align="center"><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center" <%if(!VCOUNT_CREDIT_LINE.elementAt(i).equals("")) {%>style="background:#99ffcc;"<%} %>>
												<%=VCOUNT_CREDIT_LINE.elementAt(i)%>
											</td>
											<td align="center">
												<input type="button" name="credit_exceed_btn" class="btn btn-sm config_btn" value="Configure" 
												onclick="creditExceedDaysConfig('<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
												'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
												'<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>','<%=VEND_DT.elementAt(i)%>');">
											</td>
										</tr>
										<%}%>
									<%}else{%>
										<tr>
											<td colspan="7" align="center"><%=utilmsg.infoMessage("<b>No Contract for Selected Counterparty!</b>") %></td>
										</tr>
									<%}%>	
									</tbody>
								</table>
							</div>
						</div>
						<%}else{ %>
							<div colspan="7" align="center"><%=utilmsg.infoMessage("<b>Please Select any Counterparty!</b>") %></div>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="prev_counterparty_cd" value="<%=counterparty_cd%>">

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