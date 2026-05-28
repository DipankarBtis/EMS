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
	var segmentType = document.forms[0].segmentType.value;
	
	var msg = document.forms[0].msg.value;
	var msg_type = document.forms[0].msg_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_tcq_request.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
			"&segmentType="+segmentType+"&msg="+msg+"&msg_type="+msg_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function opnerRefresh(msg,msg_type)
{
	document.forms[0].msg.value=msg;
	document.forms[0].msg_type.value=msg_type;
	
	refresh()
}

var newWindow;
function doModifyTCQ(counterparty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,contract_type,rate,rate_unit,tcq_sign,var_tcq,tcq,counterparty_abbr,deal_map)
{
	var u = document.forms[0].u.value;
	
	if(tcq_sign=="+")
	{
		tcq_sign="plus"
	}
	else if(tcq_sign=="-")
	{
		tcq_sign="minus"
	}
	
	var msg="";
	var flag=true;
	
	if(trim(tcq_sign)=="")
	{
		msg+="Select TCQ Sign!\n";
		flag=false;	
	}
	if(trim(var_tcq)=="")
	{
		msg+="Enter TCQ Qty for Modification!\n";
		flag=false;	
	}
	if(trim(rate) == "")
	{
		msg+="Gas Price Missing!\n";
		flag=false;
	}
	if(trim(rate_unit) == "")
	{
		msg+="Rate Unit Missing!\n";
		flag=false;
	}
	
	if(flag)
	{
		var url = "frm_tcq_modification.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+
				"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&tcq_sign="+tcq_sign+"&tcq="+tcq+
				"&rate_unit="+rate_unit+"&var_tcq="+var_tcq+"&rate="+rate+"&counterparty_abbr="+counterparty_abbr+"&deal_map="+deal_map+
				"&u="+u;;
		
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Contract Billing Detail","top=10,left=10,width=1300,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Contract Billing Detail","top=10,left=10,width=1300,height=900,scrollbars=1");
		}
	}
	else
	{
		alert(msg);
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

contract.setCallFlag("TCQ_MODIFICATION_REQUEST");
contract.setComp_cd(owner_cd);
contract.setSegmentType(segmentType);
contract.setCounterparty_cd(counterparty_cd);
contract.init();

Vector VSEGMENT = contract.getVSEGMENT();
Vector VSEGMENT_TYPE = contract.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = contract.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = contract.getVTEMP_SEGMENT_TYPE();
Vector VINDEX = contract.getVINDEX();

Vector VMST_COUNTERPARTY_CD = contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = contract.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = contract.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = contract.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = contract.getVCOUNTERPARTY_ABBR();
Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV_NO = contract.getVAGMT_REV_NO();
Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONT_REV_NO = contract.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VCONT_REF_NO = contract.getVCONT_REF_NO();
Vector VDIS_CONT_MAPPING = contract.getVDIS_CONT_MAPPING();
Vector VCONT_STATUS_FLG = contract.getVCONT_STATUS_FLG();
Vector VCONT_STATUS = contract.getVCONT_STATUS();
Vector VRATE_FORMULA = contract.getVRATE_FORMULA();
Vector VRATE = contract.getVRATE();
Vector VRATE_UNIT = contract.getVRATE_UNIT();
Vector VTCQ_SIGN = contract.getVTCQ_SIGN();
Vector VVAR_TCQ_QTY = contract.getVVAR_TCQ_QTY();
Vector VPRICE_TYPE = contract.getVPRICE_TYPE();
Vector VTCQ = contract.getVTCQ();
Vector VRATE_UNIT_NM = contract.getVRATE_UNIT_NM();
Vector VREMARK = contract.getVREMARK();
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
				    		Contract TCQ Modification Request
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Contract</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="segmentType" onchange="refresh()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VSEGMENT.size();i++){ %>
										<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].segmentType.value="<%=segmentType%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col-auto">
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
					</div>
				</div>
				<div class="card-body cdbody">
					<%int i=0;int k=0;
					for(int j=0; j<VTEMP_SEGMENT.size(); j++){ 
						int index = Integer.parseInt(""+VINDEX.elementAt(j));
					if(j!=0)
					{
					%>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div> 
					<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTEMP_SEGMENT.elementAt(j) %></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Customer</th>
										<th>Contract#</th>
										<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("X")){ %>
										<th>Trade Ref#</th>
										<%}else{ %>
										<th>Contract Ref#</th>
										<%} %>
										<th>Contract Period</th>
										<th>Status</th>	
										<th>Price Type</th>																			
										<th>Contract Price</th>
										<th>Currency/MMBTU</th>
										<th>TCQ <br>(MMBTU)</th>
										<th>Delta TCQ Sign</th>
										<th>Delta TCQ <br>(MMBTU)</th>
										<th>Action</th>
									</tr>									
								</thead>
								<tbody>
								<%k=0;
								if(index > 0){ %>
									<%for(i=i;i<VCOUNTERPARTY_CD.size(); i++){ 
									k+=1;
									%>
										<tr>
											<td align="center"><%=k%></td>
											<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
											<td><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
											<td><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
											<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
											<td align="right"><%=VRATE.elementAt(i)%></td>
											<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
											<td align="right"><%=VTCQ.elementAt(i)%></td>
											<td align="center"><%=VTCQ_SIGN.elementAt(i)%></td>
											<td align="right" title="<%=VREMARK.elementAt(i)%>"><%=VVAR_TCQ_QTY.elementAt(i)%></td>
											<td align="center">
											<%if(write_access.equals("Y")){ %>
												<input type="button" class="btn btn-warning com-btn" value="Modify TCQ" style="border-radius: 50px;" 
												onclick="doModifyTCQ('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>',
														'<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
														'<%=VRATE.elementAt(i)%>','<%=VRATE_UNIT.elementAt(i)%>','<%=VTCQ_SIGN.elementAt(i)%>',
														'<%=VVAR_TCQ_QTY.elementAt(i)%>','<%=VTCQ.elementAt(i)%>','<%=VCOUNTERPARTY_ABBR.elementAt(i)%>','<%=VDIS_CONT_MAPPING.elementAt(i)%>');">
											<%}else{ %>
												<input type="button" class="btn btn-warning com-btn" value="Modify TCQ" style="border-radius: 50px;" disabled>
											<%} %>
											</td>
										</tr>
										<%if(k==index)
										{
											i=i+1;
											break;
										}%>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="14" align="center"><%=utilmsg.infoMessage("<b>No TCQ Modification Requested!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="msg" value="">
<input type="hidden" name="msg_type" value="">

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