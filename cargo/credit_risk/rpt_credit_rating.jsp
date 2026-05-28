<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

</head>
<jsp:useBean class="com.etrm.fms.credit_risk.DataBean_CreditRisk" id="cr_report" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String clearance = request.getParameter("clearance")==null?"K":request.getParameter("clearance");
String report_dt = request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String sell_buy  = request.getParameter("sell_buy")==null?"S":request.getParameter("sell_buy");

String countpty_nm = utilBean.getCounterpartyName(counterparty_cd);

cr_report.setCallFlag("CREDIT_RATING");
cr_report.setClearance(clearance);
cr_report.setCounterparty_cd(counterparty_cd);
cr_report.setComp_cd(owner_cd);
cr_report.init();

Vector VCOUNTERPARTY_CD = cr_report.getVCOUNTERPARTY_CD();
Vector VCREDIT_RATING = cr_report.getVCREDIT_RATING();
Vector VPARENT_OWENERSHIP_NAME = cr_report.getVPARENT_OWENERSHIP_NAME();
Vector VPARENT_OWENERSHIP_ABBR = cr_report.getVPARENT_OWENERSHIP_ABBR();
Vector VRATING_EFF_DT = cr_report.getVRATING_EFF_DT();
Vector VPARENT_OWENERSHIP_CD = cr_report.getVPARENT_OWENERSHIP_CD();
Vector VPARENT_OWENERSHIP = cr_report.getVPARENT_OWENERSHIP();
Vector VREMARK = cr_report.getVREMARK();
Vector VSTATUS = cr_report.getVSTATUS();
Vector VLIMIT_ID = cr_report.getVLIMIT_ID();
Vector VPARENT_ENT_DT = cr_report.getVPARENT_ENT_DT();
Vector VPARENT_EXIT_DT = cr_report.getVPARENT_EXIT_DT();
Vector VREF_NO = cr_report.getVREF_NO();
Vector VENT_DT = cr_report.getVENT_DT();

%>
<body>

<form method="post" action="">
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
				    		Credit Rating Report For <%=countpty_nm %>
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="filterbysearch">
							<thead>
								<tr>
									<th>SR#</th>
									<th>Ref. No#</th>
									<th>Entry Date</th>
									<th>Credit rating</th>
									<th>Parent<br>Ownership Name</th>
									<th>Parent Ownership(%)</th>
									<th>Parent Entry Date</th>
									<th>Parent Exit Date</th>
									<th>Remark</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody>
							<%int k=0;
							if(VCOUNTERPARTY_CD.size()!=0)
							{ 
								for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){
								k+=1;
								%>
									<tr>
										<td align="center"><%=k %></td>
										<td align="center"><%=VREF_NO.elementAt(i) %></td>
										<td align="center"><%=VENT_DT.elementAt(i) %></td>
										<td align="center"><%=VCREDIT_RATING.elementAt(i) %></td>
										<td align="right"><%=VPARENT_OWENERSHIP_NAME.elementAt(i) %></td>
										<td align="right"><%=VPARENT_OWENERSHIP.elementAt(i) %></td>
										<td align="right"><%=VPARENT_ENT_DT.elementAt(i) %></td>
										<td align="right"><%=VPARENT_EXIT_DT.elementAt(i) %></td>
										<td align="right"><%=VREMARK.elementAt(i) %></td>
										<td align="right">
											<%if(VSTATUS.elementAt(i).equals("Y")){ %>
												Authorized
											<%}else if(VSTATUS.elementAt(i).equals("N")){  %>
												Unauthorized
											<%}%>
										</td>
									</tr>
								<%} %>
							<%}else{ %>
								<tr><td colspan="11" align="center"><%=utilmsg.infoMessage("<b>Credit rating data Not Available!</b>") %></td></tr>
							<%} %>
							</tbody>
						</table>
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