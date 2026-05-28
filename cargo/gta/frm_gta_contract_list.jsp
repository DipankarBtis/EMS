<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(agmtno,agmt_revno,contno,cont_revno)
{
	window.opener.setContractDetail(agmtno,agmt_revno,contno,cont_revno);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GtaMaster" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");

gta.setCallFlag("GTA_CONTRACT_LIST");
gta.setComp_cd(owner_cd);
gta.setCounterparty_cd(counterparty_cd);
gta.setContract_type(contract_type);
gta.init();

Vector VAGMT_NO = gta.getVAGMT_NO();
Vector VAGMT_REV_NO = gta.getVAGMT_REV_NO();
Vector VCONT_NO = gta.getVCONT_NO();
Vector VCONT_REV_NO = gta.getVCONT_REV_NO();
Vector VSTART_DT = gta.getVSTART_DT();
Vector VEND_DT = gta.getVEND_DT();
Vector VDIS_DEAL_NO = gta.getVDIS_DEAL_NO();
Vector VCONT_REF_NO = gta.getVCONT_REF_NO();
Vector VCT_REF_NO = gta.getVCT_REF_NO();
Vector VLINKED_SALES_CONT = gta.getVLINKED_SALES_CONT();
Vector VBU_PLANT_NM =gta.getVBU_PLANT_NM();

Vector VENTRY_POINT_NAME = gta.getVENTRY_POINT_NAME();
Vector VEXIT_POINT_NAME = gta.getVEXIT_POINT_NAME();
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
				    		Gas Transportation Contract List
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
											<th>Contract#</th>
											<th>Contract Ref#</th>				    		
									    	<th>Contract Period</th>
									    	<th>Business Unit</th>
									    	<th>Entry Point</th>
									    	<th>Exit Point</th>
									    	<%if(contract_type.equals("C")){ %>
									    	<th>Linked Sales Contract</th>
									    	<%} %>
									    </tr>
									</thead>
									<tbody>
									<%if(VCONT_NO.size() > 0){ %>
										<%for(int i=0;i<VCONT_NO.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="radio" onclick="setValue('<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>');">
											</td>
											<td align="center"><font color='blue'><%=VDIS_DEAL_NO.elementAt(i)%></font></td>
											<td><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td><%=VBU_PLANT_NM.elementAt(i)%></td>
											<td><%=VENTRY_POINT_NAME.elementAt(i)%></td>
											<td><%=VEXIT_POINT_NAME.elementAt(i)%></td>
											<%if(contract_type.equals("C")){ %>
											<td><%=VLINKED_SALES_CONT.elementAt(i)%></td>
											<%} %>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td align="center" <%if(contract_type.equals("C")){ %>colspan="8"<%}else{ %>colspan="7"<%} %>><%=utilmsg.infoMessage("<b>No GT Contract is Created!</b>") %></td>
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

</body>
</html>