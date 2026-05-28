<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

</head>
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");

String cont_start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String cont_end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");

contract.setCallFlag("SUPPLY_CONTRACT_DCQ_DTL");
contract.setCounterparty_cd(counterparty_cd);
contract.setComp_cd(owner_cd);
contract.setAgmt_no(agmt_no);
contract.setAgmt_rev_no(agmt_rev_no);
contract.setCont_no(cont_no);
contract.setCont_rev_no(cont_rev_no);
contract.setContract_type(contract_type);
contract.init();

Vector VSEQ_NO = contract.getVSEQ_NO();
Vector VFROM_DT = contract.getVFROM_DT();
Vector VTO_DT = contract.getVTO_DT();
Vector VDCQ = contract.getVDCQ();
Vector VREMARK = contract.getVREMARK();
Vector VSTATUS = contract.getVSTATUS();
%>
<body>
<form method="post" action="../servlet/Frm_ContracMaster">
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
					    	Variable DCQ 
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>From Date</th>
										<th>To Date</th>
										<th>DCQ (MMBTU)</th>
										<th>Status</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody id="itemTab">
								<%if(VFROM_DT.size() > 0){ %>
									<%for(int i=0;i<VFROM_DT.size(); i++){%>
									<tr id="row<%=i+1%>">
										<td align="center"><%=VFROM_DT.elementAt(i)%></td>
										<td align="center"><%=VTO_DT.elementAt(i)%></td>
										<td align="right"><%=VDCQ.elementAt(i)%></td>
										<td align="center"><%if(VSTATUS.elementAt(i).equals("Y")){%>Active<%}else{%>Inactive<%} %></td>
										<td align="left"><%=VREMARK.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>	
									<tr>
										<td colspan="5" align="center"><%=utilmsg.infoMessage("<b>Variable DCQ Not Configured!</b>") %></td>
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

<input type="hidden" name="option" value="VARIABLE_DCQ">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="start_dt" value="<%=cont_start_dt%>">
<input type="hidden" name="end_dt" value="<%=cont_end_dt%>">

<input type="hidden" name="item_size" id="item_size" value="<%=VSEQ_NO.size()%>">

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