<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(customer_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,cont_type,dealNo,contRef)
{
	window.opener.setSalesContDetail(customer_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,cont_type,dealNo,contRef);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_svc_cont_master" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String entry_point = request.getParameter("entry_point")==null?"":request.getParameter("entry_point");
String exit_point = request.getParameter("exit_point")==null?"":request.getParameter("exit_point");
String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");

String counterparty_plant_seq = exit_point;


dlng.setCallFlag("SALES_CONT_LIST");
dlng.setComp_cd(owner_cd);
dlng.setFrom_dt(start_dt);
dlng.setTo_dt(end_dt);
dlng.setCounterparty_cd(customer_cd);
dlng.setCounterparty_plant_seq(counterparty_plant_seq);
dlng.setBu_unit(bu_unit);
dlng.init();

Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VAGMT_REV_NO = dlng.getVAGMT_REV_NO();
Vector VCONT_NO = dlng.getVCONT_NO();
Vector VCONT_REV_NO = dlng.getVCONT_REV_NO();
Vector VSTART_DT = dlng.getVSTART_DT();
Vector VEND_DT = dlng.getVEND_DT();
Vector VDIS_DEAL_NO = dlng.getVDIS_DEAL_NO();
Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();
Vector VCONT_REF_NO = dlng.getVCONT_REF_NO();

Vector VDELV_POINT = dlng.getVDELV_POINT();
Vector VBU_POINT = dlng.getVBU_POINT();
Vector VCUST_PLANT_POINT = dlng.getVCUST_PLANT_POINT();

Vector VTCQ = dlng.getVTCQ();
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
				    		Sales Contract List
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
									    	<th>Contract/Trade Ref#</th>
									    	<th>Contract Period</th>
									    	<th>Business Unit</th>
									    	<th>Filling Station</th>
									    	<th>Customer Plants</th>
									    	<th>TCQ</th>
										</tr>
									</thead>
									<tbody>
									<%if(VAGMT_NO.size()>0){ %>
										<%for(int i=0; i<VAGMT_NO.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="radio" name="rd" 
												onclick="setValue('<%=customer_cd%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>',
												'<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
												'<%=VDIS_DEAL_NO.elementAt(i)%>','<%=VCONT_REF_NO.elementAt(i)%>');">
											</td>
											<td align="center"><%=VDIS_DEAL_NO.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td><%=VBU_POINT.elementAt(i)%></td>
											<td><%=VDELV_POINT.elementAt(i)%></td>
											<td><%=VCUST_PLANT_POINT.elementAt(i)%></td>
											<td align="center"><%=VTCQ.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="8" align="center"><%=utilmsg.infoMessage("<b>DLNG Sales Contract is not Available!</b>") %></td>
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