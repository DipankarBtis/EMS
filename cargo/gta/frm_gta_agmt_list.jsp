<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(agmt_no,agmt_rev_no,agmt_nm)
{
	window.opener.setAgreementDetail(agmt_no,agmt_rev_no,agmt_nm);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GtaMaster" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

gta.setCallFlag("GTA_AGREEMENT_LIST");
gta.setComp_cd(owner_cd);
gta.setCounterparty_cd(counterparty_cd);
gta.init();

Vector VCOUNTERPTY_CD = gta.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_NM = gta.getVCOUNTERPTY_NM();
Vector VAGMT_NO = gta.getVAGMT_NO();
Vector VAGMT_REV_NO = gta.getVAGMT_REV_NO();
Vector VTOTAL_QTY = gta.getVTOTAL_QTY();
Vector VUNIT_CD = gta.getVUNIT_CD();
Vector VSTART_DT = gta.getVSTART_DT();
Vector VEND_DT = gta.getVEND_DT();
Vector VCALC_BASE = gta.getVCALC_BASE();
Vector VSTATUS = gta.getVSTATUS();
Vector VAGMT_NAME = gta.getVAGMT_NAME();

%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		Gas Transportation Agreement List
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th></th>
											<th>Agreement#</th>
											<th>Status</th>				    		
									    	<th>Contract Period</th>
									    	<th>Total MMBTU</th>
									    	<th>Transported MMBTU</th>
									    	<th>Calorific Base</th>
									    </tr>
									</thead>
									<tbody>
									<%if(VAGMT_NO.size()>0){ %>
										<%for(int i=0;i<VAGMT_NO.size();i++){ %>
										<tr>
											<td align="center">
												<input type="radio" onclick="setValue('<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VAGMT_NAME.elementAt(i)%>')">
											</td>
											<td align="center"><%=VAGMT_NAME.elementAt(i)%></td>
											<td align="center">
												<div align="center">
													<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
														<i class="fa fa-circle fa-lg" ></i>
														&nbsp;
													</font>
													<%if(VSTATUS.elementAt(i).equals("Y")){%>
													Active
													<%}else{ %>
													In-Active
													<%} %>
												</div>
											</td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="right"><%=VTOTAL_QTY.elementAt(i)%></td>
											<td align="right"></td>
											<td align="center"><%=VCALC_BASE.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td align="center" colspan="7"><%=utilmsg.infoMessage("<b>No GTA Agreement is Created!</b>") %></td>
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
</div>
	
</form>		
</body>
</html>