<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(seq_no,ct_ref_no)
{
	window.opener.setCtSeqNo(seq_no,ct_ref_no);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GtaMaster" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
//String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
String entry_point = request.getParameter("entry_point")==null?"":request.getParameter("entry_point");
String exit_point = request.getParameter("exit_point")==null?"":request.getParameter("exit_point");
String customer_cd = request.getParameter("customer_cd")==null?"":request.getParameter("customer_cd");
String sales_cont_map = request.getParameter("sales_cont_map")==null?"":request.getParameter("sales_cont_map");
String bu_unit = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");

String counterparty_cd ="";
String transporter_cd = "";
String transporter_plant_seq = "";
String counterparty_plant_seq = "";

if(!entry_point.equals(""))
{
	String[] split=entry_point.split("-");
	transporter_cd=split[0];
	transporter_plant_seq=split[1];
}
if(!exit_point.equals(""))
{
	String[] split=exit_point.split("-");
	counterparty_cd=split[1];
	counterparty_plant_seq=split[2];
}

gta.setCallFlag("GTC_CT_LIST_MST");
gta.setComp_cd(owner_cd);
gta.setContract_type(contract_type);
gta.setFrom_dt(start_dt);
gta.setTo_dt(end_dt);
gta.setCounterparty_cd(counterparty_cd);
gta.setTransporter_cd(transporter_cd);
gta.setTransporter_plant_seq(transporter_plant_seq);
gta.setCounterparty_plant_seq(counterparty_plant_seq);
gta.setCustomer_cd(customer_cd);
gta.setSales_cont_id(sales_cont_map);
gta.setBu_unit(bu_unit);
gta.init();

Vector VCT_REF_MST = gta.getVCT_REF_MST();
Vector VUTR_REF_MST = gta.getVUTR_REF_MST();
Vector VSEQ_NO = gta.getVSEQ_NO();
Vector VSTART_DT = gta.getVSTART_DT();
Vector VEND_DT = gta.getVEND_DT();
%>

<body>
<%@ include file="../home/loading.jsp"%>
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
				    		CT Reference List From <%=start_dt%> To <%=end_dt%>
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
											<th>CT#</th>				    		
									    	<th>UTR#</th>
									    	<th>Start-End Date</th>
										</tr>
									</thead>
									<tbody>
									<%if(VSEQ_NO.size()>0){ %>
										<%for(int i=0; i<VSEQ_NO.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="radio" class="form-check-input" name="cont_mapping" 
												onclick="setValue('<%=VSEQ_NO.elementAt(i)%>','<%=VCT_REF_MST.elementAt(i)%>');">
											</td>
											<td align="center"><%=VCT_REF_MST.elementAt(i)%></td>
											<td><%=VUTR_REF_MST.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="8" align="center"><%=utilmsg.infoMessage("<b>CT Reference not Available!</b>") %></td>
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