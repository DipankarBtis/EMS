<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_CreditRisk" id="credit_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String cont_end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");

credit_risk.setCallFlag("EXCEED_CREDIT_DAYS_CONFIG");
credit_risk.setCounterparty_cd(counterparty_cd);
credit_risk.setComp_cd(owner_cd);
credit_risk.setAgmt_no(agmt_no);
credit_risk.setAgmt_rev_no(agmt_rev_no);
credit_risk.setCont_no(cont_no);
credit_risk.setCont_rev_no(cont_rev_no);
credit_risk.setContract_type(contract_type);
credit_risk.init();

String counterparty_abbr=credit_risk.getCounterparty_abbr();
String display_map_id = credit_risk.getDisplay_map_id();

Vector VFROM_DT = credit_risk.getVFROM_DT();
Vector VTO_DT = credit_risk.getVTO_DT();
Vector VSTATUS = credit_risk.getVSTATUS();
Vector VSTATUS_NM = credit_risk.getVSTATUS_NM();
Vector VSEQ_NO = credit_risk.getVSEQ_NO();
Vector VREMARK = credit_risk.getVREMARK();

%>
<body>
<%@ include file="../home/loading.jsp"%>
<form action="../servlet/Frm_CreditRisk" method="post">

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
					    	Exceed Credit Days <%=counterparty_abbr%> (<%=display_map_id%>) [Contract Period : <%=cont_start_dt %> - <%=cont_end_dt %>]
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>Sr#</th>										
										<th>From</th>
										<th>To</th>
										<th>Status</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody>
								<%if(VFROM_DT.size()>0) {%>
									<%for(int i=0;i<VFROM_DT.size(); i++){%>	
									<tr>
										<td align="center">
											<%=i+1%>
										</td>
										<td align="center">
											<input type="hidden" name="line_start_date" id="line_start_date<%=i%>" value="<%=VFROM_DT.elementAt(i)%>" >
											<input type="hidden" name="line_seq_no" id="line_seq_no<%=i%>" value="<%=VSEQ_NO.elementAt(i)%>" >
											<%=VFROM_DT.elementAt(i)%>
										</td>
										<td align="center">
											<input type="hidden" name="line_end_date" id="line_end_date<%=i%>" value="<%=VTO_DT.elementAt(i)%>" >
											<%=VTO_DT.elementAt(i)%>
										</td>																	
										<td align="center">
												<span class="
												<%if(VSTATUS.elementAt(i).equals("P")){ %>
													alert alert-primary
												<%}else if(VSTATUS.elementAt(i).equals("O")){  %>
													alert alert-success
												<%}else if(VSTATUS.elementAt(i).equals("C")){  %>
													alert alert-danger
												<%} %>
												"><b><%=VSTATUS_NM.elementAt(i)%></b></span>
										</td>
										<td align="left"><%=VREMARK.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="5" align="center"><%=utilmsg.infoMessage("<b>No Credit Exceed Days Configured for the Contract!</b>")%></td>
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

<input type="hidden" name="option" value="CREDIT_EXCEED_CONFIG">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_start_dt" value="<%=cont_start_dt%>">
<input type="hidden" name="cont_end_dt" value="<%=cont_end_dt%>">

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