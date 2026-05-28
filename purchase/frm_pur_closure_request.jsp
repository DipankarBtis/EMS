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
	
	//var msg = document.forms[0].msg.value;
	//var msg_type = document.forms[0].msg_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_pur_closure_request.jsp?u="+u+
			"&segmentType="+segmentType+"&counterparty_cd="+counterparty_cd;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function approve_rejectClosure(counterpty_cd,agmt,agmt_rev,cont,cont_rev,cont_type,cont_map,j,i,end_dt,bal_qty,clsr_req,clsr_sign,req_flg,buySale,agmtType)
{
	var trader_cd=document.forms[0].trader_cd;
	var agmt_no=document.forms[0].agmt_no;
	var agmt_rev_no=document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var contract_type = document.forms[0].contract_type;
	var closure_dt = document.forms[0].closure_dt;
	var closure_qty = document.forms[0].closure_qty;
	var closure_remark = document.getElementById("closure_remark_"+j+"_"+i);
	var closure_request_flag = document.forms[0].closure_request_flag;
	var closure_note = document.forms[0].closure_note;
	var agmt_type = document.forms[0].agmt_type;
	var buy_sale = document.forms[0].buy_sale;
	
	if(agmtType=='L')
	{
		var msg="";
		var flag=true;
		var closure_eff_dt = document.getElementById("closure_eff_dt_"+j+"_"+i);
		if(closure_eff_dt.value == '')
		{
			msg="Closure Eff. Date can't be null!\n";
			closure_eff_dt.value=end_dt;
			flag=false
		}
		if(closure_remark.value=='' && clsr_req=='APPROVE')
		{
			msg+="Closure Remark can't be null!\n";
			flag=false;
		}
		if(flag==true)
		{
			var cnf = "";
			if(req_flg=='Y')
			{
				cnf="Do you want to "+clsr_req.toLowerCase()+" contract("+cont_map+") closure?";
			}
			else if(req_flg=='R')
			{
				cnf="Do you want to "+clsr_req.toLowerCase()+" contract("+cont_map+") termination?";
			}
			var a = confirm(cnf);
			if(a)
			{
				trader_cd.value = counterpty_cd;
				agmt_no.value=agmt;
				agmt_rev_no.value=agmt_rev;
				cont_no.value=cont;
				cont_rev_no.value=cont_rev;
				contract_type.value = cont_type;
				//clsr_type.value = req_flg;
				closure_request_flag.value = req_flg;
				closure_note.value = closure_remark.value;
				agmt_type.value=agmtType;
				buy_sale.value=buySale;
				closure_dt.value = closure_eff_dt.value;
				
				document.forms[0].action = "../servlet/Frm_LtcoraMaster";
				document.forms[0].method="post";
				document.forms[0].option.value = "APPROVE_REJECT_CLOSURE_TERMINATION";
				document.forms[0].opration.value=clsr_req;
				document.forms[0].submit();
			}
		}
		else
		{
			alert("Else part execution"+msg);
		}
	}
	else
	{
		var closure_eff_dt = document.getElementById("closure_eff_dt_"+j+"_"+i);
		if(clsr_sign=='-')
		{
			bal_qty=(-1)*parseFloat(bal_qty);
		}
		var flag=true;
		var msg="";
		if(closure_eff_dt.value == '')
		{
			msg="Closure Eff. Date can't be null!\n";
			closure_eff_dt.value=end_dt;
			flag=false
		}
		if(closure_remark.value=='' && clsr_req=='APPROVE')
		{
			msg+="Closure Remark can't be null!\n";
			flag=false;
		}
		if(flag==true)
		{
			
			a = confirm("Do you want to "+clsr_req.toLowerCase()+" the closure request for contract#("+cont_map+")");
			if(a)
			{
				trader_cd.value=counterpty_cd;
				agmt_no.value=agmt;
				agmt_rev_no.value=agmt_rev;
				cont_no.value=cont;
				cont_rev_no.value=cont_rev;
				contract_type.value=cont_type;
				closure_dt.value=closure_eff_dt.value;
				closure_qty.value=bal_qty;
				closure_note.value=closure_remark.value;
				closure_request_flag.value=req_flg;
				document.forms[0].opration.value=clsr_req;
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].submit();
			}
		}
		else
		{
			alert(msg);
		}
	}
}

</script>
</head>

<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchase" scope="request"></jsp:useBean>
<%

String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

purchase.setCallFlag("CLOSURE_REQUEST");
purchase.setComp_cd(owner_cd);
purchase.setSegmentType(segmentType);
purchase.setCounterparty_cd(counterparty_cd);
purchase.init();

Vector VTEMP_SEGMENT_TYPE = purchase.getVTEMP_SEGMENT_TYPE();
Vector VTEMP_SEGMENT = purchase.getVTEMP_SEGMENT();
Vector VDISPLAY_SEGMENT_TYP = purchase.getVDISPLAY_SEGMENT_TYP();
Vector VDISPLAY_SEGMENT = purchase.getVDISPLAY_SEGMENT();
Vector VMST_COUNTERPARTY_CD = purchase.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = purchase.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = purchase.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = purchase.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
Vector VDEAL_MAP = purchase.getVDEAL_MAP();
Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VAGMT_REV_NO = purchase.getVAGMT_REV_NO();
Vector VCONT_NO = purchase.getVCONT_NO();
Vector VCONT_REV_NO = purchase.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = purchase.getVCONTRACT_TYPE_NM();
Vector VSTART_DT = purchase.getVSTART_DT();
Vector VEND_DT = purchase.getVEND_DT();
Vector VTCQ = purchase.getVTCQ();
Vector VRATE = purchase.getVRATE();
Vector VRATE_UNIT = purchase.getVRATE_UNIT();
Vector VRECEIVED_QTY = purchase.getVRECEIVED_QTY();
Vector VBALANCE_QTY = purchase.getVBALANCE_QTY();
Vector VPRICE_TYPE = purchase.getVPRICE_TYPE();
Vector VCONT_REF_NO = purchase.getVCONT_REF_NO();
Vector VBALANCE_SIGN = purchase.getVBALANCE_SIGN();
Vector VINDEX = purchase.getVINDEX();
Vector VCLOSURE_REQUEST_FLG=purchase.getVCLOSURE_REQUEST_FLG();
Vector VCLOSURE_NOTE=purchase.getVCLOSURE_NOTE();
Vector VBUY_SALE=purchase.getVBUY_SALE();
Vector VAGMT_TYPE=purchase.getVAGMT_TYPE();
Vector VCLOSEURE_EFF_DT=purchase.getVCLOSEURE_EFF_DT();
%>

<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Trader_Contract_Mst">
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
			    		Purchase Contract Closure|Terminate Request
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
							<div class="col">
								<select class="form-select form-select-sm" name="segmentType" onchange="refresh()">
									<option value="0">--Select--</option>
									<%for(int i=0;i<VDISPLAY_SEGMENT.size();i++){ %>
									<option value="<%=VDISPLAY_SEGMENT_TYP.elementAt(i)%>"><%=VDISPLAY_SEGMENT.elementAt(i)%></option>
									<%} %>
								</select>
								<script>document.forms[0].segmentType.value="<%=segmentType%>"</script>
							</div>
						</div>
					</div>
					<div class="col-sm-3 col-xs-3 col-md-3">  
						<div class="form-group row">
							<div class="col-auto">
								<label class="form-label"><b>Trader</b></label>
							</div>
							<div class="col">
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
					if(j!=0){%>
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
										<th rowspan="2">Trader</th>
										<th rowspan="2">Contract#</th>
										<th rowspan="2">Contract Type</th>
										<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("I")){ %>
										<th rowspan="2">Trade Ref#</th>
										<%}else{ %>
										<th rowspan="2">Contract Ref#</th>
										<%} %>
										<th rowspan="2">Contract Period</th>
										<th rowspan="2">Price Type</th>																			
										<th rowspan="2">Contract Price</th>
										<th rowspan="2">Currency/MMBTU</th>
										<th rowspan="2">TCQ <br>(MMBTU)</th>
										<th rowspan="2">Unloaded<br>(MMBTU)</th>
										<th rowspan="2">Closure Sign</th>
										<th rowspan="2">Closure<br>(MMBTU)</th>
										<th rowspan="2"><b>Closure Eff. Date<span class="s-red">*</span></b></th>
										<th rowspan="2"><b>Closure Remark<span class="s-red">*</span></b></th>
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
									k+=1;
									%>
										<tr>
											<td align="center"><%=k%></td>
											<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
											<td><%=VDEAL_MAP.elementAt(i)%></td>
											<td><%=VCONTRACT_TYPE_NM.elementAt(i) %></td>
											<td><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
											<td align="right"><%=VRATE.elementAt(i)%></td>
											<td align="center"><%=VRATE_UNIT.elementAt(i)%></td>
											<td align="right"><%=VTCQ.elementAt(i)%></td>
											<td align="right"><%=VRECEIVED_QTY.elementAt(i) %></td>
											<td align="center"><%=VBALANCE_SIGN.elementAt(i) %></td>
											<td align="right"><%=VBALANCE_QTY.elementAt(i) %></td>
											<td align="center">
												<div style="width:100px;">
													<div class="input-group input-group-sm" >
							      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="closure_eff_dt_<%=j%>_<%=i%>" id="closure_eff_dt_<%=j%>_<%=i%>" value="<%=VCLOSEURE_EFF_DT.elementAt(i)%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
							      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
						      						</div>
						    					</div>
											</td>
											
											<td>
												<div style="width:180px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm" id="closure_remark_<%=j%>_<%=i%>" value="<%=VCLOSURE_NOTE.elementAt(i) %>"  maxLength="60">
													</div>
												</div>
											</td>
											
											<td align="center">
												<input type="button" class="btn btn-warning com-btn" value="<%if(VCLOSURE_REQUEST_FLG.elementAt(i).equals("Y")){%>Apply Closure<%}else if(VCLOSURE_REQUEST_FLG.elementAt(i).equals("R")){%>Apply Termination<%}%>" style="border-radius: 50px;"
												onclick="approve_rejectClosure('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>',
												'<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
												'<%=VDEAL_MAP.elementAt(i)%>','<%=j %>','<%=i %>','<%=VEND_DT.elementAt(i) %>','<%=VBALANCE_QTY.elementAt(i)%>','APPROVE','<%=VBALANCE_SIGN.elementAt(i) %>','<%=VCLOSURE_REQUEST_FLG.elementAt(i) %>','<%=VBUY_SALE.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>');" />
											</td>
											<td>
												<input type="button" class="btn btn-warning com-btn" value="<%if(VCLOSURE_REQUEST_FLG.elementAt(i).equals("Y")){%>Reject Closure<%}else if(VCLOSURE_REQUEST_FLG.elementAt(i).equals("R")){%>Reject Termination<%} %>" style="border-radius: 50px;"
												onclick="approve_rejectClosure('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>',
												'<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
												'<%=VDEAL_MAP.elementAt(i)%>','<%=j %>','<%=i %>','<%=VEND_DT.elementAt(i) %>','<%=VBALANCE_QTY.elementAt(i)%>','REJECT','<%=VBALANCE_SIGN.elementAt(i) %>','<%=VCLOSURE_REQUEST_FLG.elementAt(i) %>','<%=VBUY_SALE.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>');">
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
										<td colspan="17" align="center"><%=utilmsg.infoMessage("<b>No Closure/Termination Requested!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				<%}%>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="trader_cd" value="">
<input type="hidden" name="agmt_no" value="">
<input type="hidden" name="agmt_rev_no" value="">
<input type="hidden" name="cont_no" value="">
<input type="hidden" name="cont_rev_no" value="">
<input type="hidden" name="contract_type" value="">
<input type="hidden" name="option" value="BUY_CLOSURE">
<input type="hidden" name="opration" value="">
<input type="hidden" name="closure_dt" value="">
<input type="hidden" name="closure_qty" value="">
<input type="hidden" name="closure_note" value="">
<input type="hidden" name="closure_request_flag" value="">
<input type="hidden" name="buy_sale" value=""> 
<input type="hidden" name="agmt_type" value=""> 

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