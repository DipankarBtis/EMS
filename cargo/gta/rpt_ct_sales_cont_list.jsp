<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function doSubmit()
{
	var cont_mapping = document.forms[0].cont_mapping;
	
	var msg="";
	var flag=true;
	var u = document.forms[0].u.value;
	var chkFlg = false;
	if(cont_mapping!=null && cont_mapping!=undefined)
	{
		if(cont_mapping.length!=undefined)
		{
			for(var i=0;i<cont_mapping.length;i++)
			{
				if(cont_mapping[i].checked)
				{
					chkFlg = true;	
				}
			}
		}
		else
		{
			if(cont_mapping.checked)
			{
				chkFlg = true;	
			}
		}
	}
	if(chkFlg==false)
	{
		msg += "Please Select Atleast One Contract!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a= confirm("Do you Want to Submit?");
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg)
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_MapMaster" id="dbmapmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String transporter_cd = request.getParameter("transporter_cd")==null?"":request.getParameter("transporter_cd");
String transporter_plant_seq = request.getParameter("transporter_plant_seq")==null?"":request.getParameter("transporter_plant_seq");
String counterparty_plant_seq = request.getParameter("counterparty_plant_seq")==null?"":request.getParameter("counterparty_plant_seq");
String seq_no = request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");

dbmapmaster.setCallFlag("SALES_CONT_LIST");
dbmapmaster.setComp_cd(owner_cd);
dbmapmaster.setCounterparty_cd(counterparty_cd);
dbmapmaster.setCounterparty_plant_seq(counterparty_plant_seq);
dbmapmaster.setTransporter_cd(transporter_cd);
dbmapmaster.setTransporter_plant_seq(transporter_plant_seq);
dbmapmaster.setStart_dt(start_dt);
dbmapmaster.setEnd_dt(end_dt);
dbmapmaster.setSeq_no(seq_no);
dbmapmaster.setContract_type(contract_type);
dbmapmaster.init();

Vector VAGMT_NO = dbmapmaster.getVAGMT_NO();
Vector VAGMT_REV_NO = dbmapmaster.getVAGMT_REV_NO();
Vector VCONT_NO = dbmapmaster.getVCONT_NO();
Vector VCONT_REV_NO = dbmapmaster.getVCONT_REV_NO();
Vector VSTART_DT = dbmapmaster.getVSTART_DT();
Vector VEND_DT = dbmapmaster.getVEND_DT();
Vector VDIS_DEAL_NO = dbmapmaster.getVDIS_DEAL_NO();
Vector VCONTRACT_TYPE = dbmapmaster.getVCONTRACT_TYPE();
Vector VCONT_REF_NO = dbmapmaster.getVCONT_REF_NO();
Vector VCONT_MAPPING = dbmapmaster.getVCONT_MAPPING();
Vector VIS_ATTACHED = dbmapmaster.getVIS_ATTACHED();
%>
<body>
<%@ include file="../home/loading.jsp"%>

<form method="post" action="../servlet/Frm_MapMaster">
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
				    		Sales Contract List From <%=start_dt%> To <%=end_dt%>
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
										</tr>
									</thead>
									<tbody>
									<%if(VAGMT_NO.size()>0){ %>
										<%for(int i=0; i<VAGMT_NO.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="checkbox" class="form-check-input" name="cont_mapping" 
												value="<%=VCONT_MAPPING.elementAt(i)%>" <%if(VIS_ATTACHED.elementAt(i).equals("Y")){ %>checked<%} %>>
											</td>
											<td align="center"><%=VDIS_DEAL_NO.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="8" align="center"><%=utilmsg.infoMessage("<b>Sales Contract is not Available!</b>") %></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="MAP_CT_CONT_MAP">

<input type="hidden" name="transporter_cd" value="<%=transporter_cd%>">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="transporter_plant_seq" value="<%=transporter_plant_seq%>">
<input type="hidden" name="counterparty_plant_seq" value="<%=counterparty_plant_seq%>">
<input type="hidden" name="seq_no" value="<%=seq_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="start_dt" value="<%=start_dt%>">
<input type="hidden" name="end_dt" value="<%=end_dt%>">

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