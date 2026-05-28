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
	var segment = document.forms[0].segment.value;
	
	var msg = document.forms[0].msg.value;
	var msg_type = document.forms[0].msg_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_cont_reopen_req.jsp?u="+u+
			"&segmentType="+segmentType+"&counterparty_cd="+counterparty_cd+"&msg="+msg+"&msg_type="+msg_type+"&segment="+segment;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function opnerRefresh(msg,msg_type)
{
	document.forms[0].msg.value=msg;
	document.forms[0].msg_type.value=msg_type;
	
	refresh();
}

var newWindow;
function doSubmit(counterpty_cd,agmtNo,agmtRev,contNo,contRev,contract_type,cont_map,approval_type,tcq_sign,tcq,rate,rate_unit,var_tcq,counterparty_abbr,clsr_dt,clsr_nt,buySale,agmtType)
{
	var customer_cd = document.forms[0].customer_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var cont_type = document.forms[0].cont_type;
	var u = document.forms[0].u.value;
	var delta_tcq_sign = document.forms[0].delta_tcq_sign;
	var delta_tcq = document.forms[0].delta_tcq;
	var closure_eff_dt = document.forms[0].closure_eff_dt;
	var closure_note = document.forms[0].closure_note;
	var agmt_type = document.forms[0].agmt_type;
	var buy_sale = document.forms[0].buy_sale;
	
	if(agmtType=='A')
	{
		var a = confirm("Do you want to Re-Open contract#("+cont_map+")?")
		if(a)
		{
			customer_cd.value=counterpty_cd;
			agmt_no.value=agmtNo;
			agmt_rev_no.value = agmtRev;
			cont_no.value = contNo;
			cont_rev_no.value = contRev;
			cont_type.value = contract_type;
			closure_eff_dt.value = clsr_dt;
			closure_note.value = clsr_nt;
			agmt_type.value = agmtType;
			buy_sale.value = buySale;
			
			document.forms[0].action = "../servlet/Frm_LtcoraMaster";
			document.forms[0].method="post";
			document.forms[0].option.value = "REOPEN";
			document.forms[0].opration.value=approval_type;
			document.forms[0].submit();
		}
	}
	else
	{
		if(tcq_sign == '-')
		{
			tcq_sign="plus";		//if closure sign is -ve means supplied_qty<tcq which means we need to reallocate delta qty.
		}
		else if(tcq_sign == '+')
		{
			tcq_sign="minus";		//if closure sign is -ve means supplied_qty<tcq which means we need to reallocate delta qty.
		}
		var use_flag = "O"; //for closed contract re-open
		
		var url = "frm_tcq_modification.jsp?counterparty_cd="+counterpty_cd+"&contract_type="+contract_type+"&cont_no="+contNo+
		"&cont_rev_no="+contRev+"&agmt_no="+agmtNo+"&agmt_rev_no="+agmtRev+"&tcq_sign="+tcq_sign+"&tcq="+tcq+
		"&rate_unit="+rate_unit+"&var_tcq="+var_tcq+"&rate="+rate+"&counterparty_abbr="+counterparty_abbr+"&deal_map="+cont_map+
		"&u="+u+"&use_flag="+use_flag;
		
		if(tcq_sign=='plus' && approval_type!='REJECT' && parseFloat(var_tcq)!=0)
		{
			if(!newWindow || newWindow.closed)
			{
				newWindow = window.open(url,"Re-Open Contract","top=10,left=10,width=1300,height=900,scrollbars=1");
			}
			else
			{
				newWindow.close();
				newWindow = window.open(url,"Re-Open Contract","top=10,left=10,width=1300,height=900,scrollbars=1");
			}
		}
		else
		{
			var temp = "New Contract Revision will be Submitted Along With Re-Open Apporval!\n\nDo You Want To Revise The Contract Details, With ";
			var cnf_msg = approval_type=='APPROVE'?temp:'Do you want to ';
			var a = confirm(cnf_msg+approval_type.toLowerCase()+" the Re-Open request for the contract("+cont_map+")?");
			if(a)
			{
				customer_cd.value=counterpty_cd;
				agmt_no.value=agmtNo;
				agmt_rev_no.value = agmtRev;
				cont_no.value = contNo;
				cont_rev_no.value = contRev;
				cont_type.value = contract_type;
				delta_tcq_sign.value = tcq_sign;
				delta_tcq.value = var_tcq;
				closure_eff_dt.value = clsr_dt;
				closure_note.value = clsr_nt;
				
				document.forms[0].action = "../servlet/Frm_ContracMaster";
				document.forms[0].method="post";
				document.forms[0].option.value = "REOPEN";
				document.forms[0].opration.value=approval_type;
				document.forms[0].submit();
			}
		}
	}
}

function approve_reject_activation(counterpty_cd,agmtNo,agmtRev,agmtType,disp_map,counterpty_abbr,approval_type,j,i)
{
	var customer_cd = document.forms[0].customer_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var u = document.forms[0].u.value;
	var agmt_type = document.forms[0].agmt_type;
	var activate_note = document.forms[0].activate_note;
	var activation_note = document.getElementById("activation_note_"+j+"_"+i).value;
	
	var conf_msg = approval_type=='APPROVE'?"Do You want to approve the activation of agreement ("+counterpty_abbr+"-"+disp_map+")?":"Do You want to reject the activation of agreement ("+counterpty_abbr+"-"+disp_map+")?";
	
	var a=confirm(conf_msg);
	if(a)
	{
		customer_cd.value=counterpty_cd;
		agmt_no.value=agmtNo;
		agmt_rev_no.value = agmtRev;
		agmt_type.value=agmtType;
		activate_note.value=activation_note;
		
		document.forms[0].action = "../servlet/Frm_ContracMaster";
		document.forms[0].method="post";
		document.forms[0].option.value = "Activate_AGMT";
		document.forms[0].opration.value=approval_type;
		document.forms[0].submit();
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="contract" scope="request"></jsp:useBean>
<%
String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String segment=request.getParameter("segment")==null?"0":request.getParameter("segment");

if(segment.equals("1"))
{
	if(segmentType.equals("S")||segmentType.equals("L")||segmentType.equals("X"))
	{
		segmentType="0";
	}
}
else if(segment.equals("0"))
{
	if(segmentType.equals("F"))
	{
		segmentType="0";
	}
}

contract.setCallFlag("CONT_REOPEN_REQUEST");
contract.setComp_cd(owner_cd);
contract.setSegmentType(segmentType);
contract.setSegment(segment);
contract.setCounterparty_cd(counterparty_cd);
contract.init();

Vector VSEGMENT = contract.getVSEGMENT();
Vector VSEGMENT_TYPE = contract.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = contract.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = contract.getVTEMP_SEGMENT_TYPE();

Vector VMST_COUNTERPARTY_CD = contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = contract.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = contract.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = contract.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = contract.getVCOUNTERPARTY_NM();

Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV_NO = contract.getVAGMT_REV_NO();
Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONT_REV_NO = contract.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = contract.getVCONTRACT_TYPE_NM();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VCONT_REF_NO = contract.getVCONT_REF_NO();
Vector VDIS_CONT_MAPPING = contract.getVDIS_CONT_MAPPING();
Vector VRATE = contract.getVRATE();
Vector VRATE_UNIT = contract.getVRATE_UNIT();
Vector VRATE_UNIT_NM = contract.getVRATE_UNIT_NM();
Vector VPRICE_TYPE = contract.getVPRICE_TYPE();
Vector VTCQ = contract.getVTCQ();
Vector VSUPPLIED_MMBTU = contract.getVSUPPLIED_MMBTU();
Vector VCLOSURE_DT = contract.getVCLOSURE_DT();
Vector VTCQ_SIGN = contract.getVTCQ_SIGN();
Vector VVAR_TCQ_QTY = contract.getVVAR_TCQ_QTY();
Vector VREMARK = contract.getVREMARK();
Vector VSIGNING_DT = contract.getVSIGNING_DT();

Vector VAGMT_TYPE = contract.getVAGMT_TYPE();
Vector VBUY_SALE = contract.getVBUY_SALE();

Vector VINDEX = contract.getVINDEX();
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
		</div>
		<div class="card cardmain">
			<div class="card-header cdheader">
				<div class="d-flex justify-content-between">
					<div class="topheader">
			    		Re-Open Contract|Agreement Request
   	 				</div>
   	 				<div class="btn-group">
   	 					<select class="btn btn-outline-secondary btngrp btnactive"  name="segment" onchange="refresh()">
   	 						<option value="0">Contract</option>
							<option value="1">Agreement</option>
   	 					</select>
   	 				</div>
			    </div>
 				<script>document.forms[0].segment.value="<%=segment%>"</script>
			</div>
			<div class="card-body cdbody">
				<div class="row">
					<%-- <div class="col-sm-3 col-xs-3 col-md-3">
						<div class="form-group row">
							<!-- <div class="col-auto">
								<label class="form-label"><b>Contract/Agreement</b></label>
							</div> -->
							<div class="col-auto">
								<select class="form-select form-select-sm" name="segment" onchange="refresh()">
									<option value="0">Contract</option>
									<option value="1">Agreement</option>
								</select>
								<script>document.forms[0].segment.value="<%=segment%>"</script>
							</div>
						</div>
					</div> --%>
					<div class="col-sm-3 col-xs-3 col-md-3">
						<div class="form-group row">
							<div class="col-auto">
								<label class="form-label">
									<%if(segment.equals("0")){%>
									<b>Contract</b>
									<%}else if(segment.equals("1")){%>
									<b>Agreement</b>
									<%} %>
								</label>
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
				{%>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div> 
				<%}%>
				<div class="row m-b-5">
					<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTEMP_SEGMENT.elementAt(j) %></label>
				</div>
				<div class="row">
					<div class="table-responsive">
						<table class="table table-bordered table-hover">
							<thead>
								<tr>
									<th rowspan="2">Sr#</th>
									<th rowspan="2">Customer</th>
									<%if(segment.equals("1")){%>
										<th rowspan="2">Agreement#</th>
										<th rowspan="2">Agreement Type</th>
										<th rowspan="2">Agreement Ref#</th>
										<th rowspan="2">Signing Date</th>
										<th rowspan="2">Agreement Period</th>
										<th rowspan="2">Activation Request Date</th>
										<th rowspan="2">Activation Note <!-- <span class="s-red">*</span> --></th>
									<%}else{%>
										<th rowspan="2">Contract#</th>
										<th rowspan="2">Contract Type</th>
										<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("X")){ %>
										<th rowspan="2">Trade Ref#</th>
										<%}else{ %>
										<th rowspan="2">Contract Ref#</th>
										<%} %>
										<th rowspan="2">Contract Period</th>
										<th rowspan="2">Price Type</th>																			
										<th rowspan="2">Contract Price</th>
										<th rowspan="2">Currency/MMBTU</th>
										<th rowspan="2">TCQ <br>(MMBTU)</th>
										<th rowspan="2">Supplied<br>(MMBTU)</th>
										<th rowspan="2">Delta TCQ Sign</th>
										<th rowspan="2">Delta TCQ <br>(MMBTU)</th>
										<th rowspan="2">Closure Date</th>
										<th rowspan="2">Closure Note</th>
									<%}%>
									<th colspan="2">Action</th>
								</tr>
								<tr>
									<th>Approve</th>
									<th>Reject</th>
								</tr>
							</thead>
							<tbody>
							<%k=0;
							if(index > 0){ %>
								<%for(i=i;i<VCOUNTERPARTY_CD.size(); i++){ 
								k+=1;%>
									<tr>
										<td align="center"><%=k%></td>
										<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
										<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
										<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i) %></td>
										<td align="center"><%=VCONT_REF_NO.elementAt(i)%></td>
										<%if(segment.equals("1")){%>
											<td align=center>
												<div style="width:100px;">
													<div class="input-group input-group-sm" >
						      							<input type="text" class="form-control form-control-sm date fmsdtpick" name="sign_dt" value="<%=VSIGNING_DT.elementAt(i) %>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" disabled>
						      							<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      							</div>
					      						</div>
											</td>
										<%} %>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<%if(!segment.equals("1")){%>
											<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
											<td align="right"><%=VRATE.elementAt(i)%></td>
											<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
											<td align="right"><%=VTCQ.elementAt(i)%></td>
											<td align="right"><%=VSUPPLIED_MMBTU.elementAt(i) %></td>
											<td align="center"><%=VTCQ_SIGN.elementAt(i) %></td>
											<td align="right"><%=VVAR_TCQ_QTY.elementAt(i) %></td>
										<%} %>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
					      							<input type="text" class="form-control form-control-sm date fmsdtpick" name="activation_eff_dt" value="<%=VCLOSURE_DT.elementAt(i) %>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" disabled>
					      							<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      							</div>
				      						</div>
										</td>
										<td align="center">
											<div style="width:200px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm" name="activation_note" id="activation_note_<%=j%>_<%=i%>" value="<%=VREMARK.elementAt(i) %>" maxLength="50" <%if(segment.equals("0")){%>readOnly<%}%>>
												</div>
											</div>
										</td>
										<%if(segment.equals("1")){%>
											<td align="center">
												<input type="button" class="btn btn-warning com-btn" value="Approve" style="border-radius: 50px;"
												onclick="approve_reject_activation('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>'
												,'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VDIS_CONT_MAPPING.elementAt(i)%>','<%=VCOUNTERPARTY_ABBR.elementAt(i)%>','APPROVE',<%=j%>,<%=i%>);"
												>
											</td>
											<td align="center">
												<input type="button" class="btn btn-warning com-btn" value="Reject" style="border-radius: 50px;"
												onclick="approve_reject_activation('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>'
												,'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VDIS_CONT_MAPPING.elementAt(i)%>','<%=VCOUNTERPARTY_ABBR.elementAt(i)%>','REJECT',<%=j%>,<%=i%>);"
												>
											</td>
											
										<%}else{%>
											<td align="center">
												<input type="button" class="btn btn-warning com-btn" value="Approve Re-open" style="border-radius: 50px;"
												onclick="doSubmit('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
												'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
												'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VDIS_CONT_MAPPING.elementAt(i)%>','APPROVE','<%=VTCQ_SIGN.elementAt(i)%>','<%=VTCQ.elementAt(i)%>',
												'<%=VRATE.elementAt(i)%>','<%=VRATE_UNIT.elementAt(i)%>','<%=VVAR_TCQ_QTY.elementAt(i) %>','<%=VCOUNTERPARTY_ABBR.elementAt(i)%>','<%=VCLOSURE_DT.elementAt(i) %>'
												,'<%=VREMARK.elementAt(i) %>','<%=VBUY_SALE.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>');"/>
											</td>
											<td align="center">
												<input type="button" class="btn btn-warning com-btn" value="Reject Re-open" style="border-radius: 50px;"
												onclick="doSubmit('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>',
												'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>',
												'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VDIS_CONT_MAPPING.elementAt(i)%>','REJECT','<%=VTCQ_SIGN.elementAt(i) %>','<%=VTCQ.elementAt(i)%>',
												'<%=VRATE.elementAt(i)%>','<%=VRATE_UNIT.elementAt(i)%>','<%=VVAR_TCQ_QTY.elementAt(i) %>','<%=VCOUNTERPARTY_ABBR.elementAt(i)%>','<%=VCLOSURE_DT.elementAt(i) %>'
												,'<%=VREMARK.elementAt(i) %>','<%=VBUY_SALE.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>');"/>
											</td>
										<%}%>
									</tr>
									<%if(k==index)
									{
										i=i+1;
										break;
									}%>
								<%} %>
							<%}else{%>
								<tr>
									<%if(segment.equals("1")){%>
										<td colspan="11" align="center"><%=utilmsg.infoMessage("<b>No Activation Request found!</b>") %></td>
									<%}else{%>
										<td colspan="17" align="center"><%=utilmsg.infoMessage("<b>No Re-Open Contract Requested!</b>") %></td>
									<%} %>
								</tr>
							<%}%>
							</tbody>
						</table>
					</div>
				</div>
				<%} %>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="msg" value="">
<input type="hidden" name="msg_type" value="">

<input type="hidden" name="customer_cd" value="">
<input type="hidden" name="agmt_no" value="">
<input type="hidden" name="agmt_rev_no" value="">
<input type="hidden" name="cont_no" value="">
<input type="hidden" name="cont_rev_no" value="">
<input type="hidden" name="cont_type" value="">
<input type="hidden" name="option" value="">
<input type="hidden" name="opration" value="">
<input type="hidden" name="delta_tcq_sign" value="">
<input type="hidden" name="delta_tcq" value="">
<input type="hidden" name="closure_eff_dt" value="">
<input type="hidden" name="closure_note" value="">
<input type="hidden" name="agmt_type" value="">
<input type="hidden" name="activate_note" value=""> 
<input type="hidden" name="buy_sale" value="">

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