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
	
	var url = "frm_cont_closure_request.jsp?u="+u+
			"&segmentType="+segmentType+"&counterparty_cd="+counterparty_cd+"&msg="+msg+"&msg_type="+msg_type;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function applyClosure(counterpty_cd,agmtNo,agmtRev,contNo,contRev,contract_type,rate,rate_unit,tcq_sign,var_tcq,tcq,end_dt,start_dt,counterparty_abbr,deal_map,j,i,closure_type,buySale,agmtType)
{
	var u = document.forms[0].u.value;
	var clsr_eff_dt = document.getElementById("closure_eff_dt"+"_"+j+"_"+i);
	var customer_cd = document.forms[0].customer_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var cont_type = document.forms[0].cont_type;
	var clsr_type = document.forms[0].clsr_type;
	var closure_note = document.forms[0].closure_note;
	var closure_note1 = document.getElementById("closure_note_"+j+"_"+i);
	var agmt_type = document.forms[0].agmt_type;
	var buy_sale = document.forms[0].buy_sale;
	
	var flag =true;
	var msg="";
	
	if(agmtType=='A')
	{
		if(clsr_eff_dt.value=='')
		{
			msg+="Eff. Date can't be null!!\n";
			flag=false;
		}
		if(closure_note1.value=='')
		{
			msg+="Closure Note can't be null!\n";
			flag=false;
		}
		if(flag==true)
		{
			var cnf = "";
			if(closure_type=='Y')
			{
				cnf="Do you want to Close the contract("+deal_map+")?";
			}
			else if(closure_type=='R')
			{
				cnf="Do you want to Terminate the contract("+deal_map+")?";
			}
			var a = confirm(cnf);
			if(a)
			{
				customer_cd.value = counterpty_cd;
				agmt_no.value=agmtNo;
				agmt_rev_no.value=agmtRev;
				cont_no.value=contNo;
				cont_rev_no.value=contRev;
				cont_type.value = contract_type;
				clsr_type.value = closure_type;
				closure_note.value = closure_note1.value;
				agmt_type.value=agmtType;
				buy_sale.value=buySale;
				document.forms[0].closure_eff_dt.value = clsr_eff_dt.value;
				
				document.forms[0].action = "../servlet/Frm_LtcoraMaster";
				document.forms[0].method="post";
				document.forms[0].option.value = "APPROVE_REJECT_CLOSURE_TERMINATION";
				document.forms[0].opration.value = "APPROVE";
				document.forms[0].submit();
			}
		}
		else
		{
			alert(msg);
		}
	}
	else
	{
		if(tcq_sign=="+")
		{
			tcq_sign="plus";
		}
		else if(tcq_sign=="-")
		{
			tcq_sign="minus";
		}
		var use_flag = "C"; //for contract closure
		
		if(clsr_eff_dt.value=='')
		{
			msg+="Eff. Date can't be null!!\n";
			flag=false;
		}
		if(closure_note1.value=='')
		{
			msg+="Closure Note can't be null!\n";
			flag=false;
		}
		
		var url = "frm_tcq_modification.jsp?counterparty_cd="+counterpty_cd+"&contract_type="+contract_type+"&cont_no="+contNo+
		"&cont_rev_no="+contRev+"&agmt_no="+agmtNo+"&agmt_rev_no="+agmtRev+"&tcq_sign="+tcq_sign+"&tcq="+tcq+
		"&rate_unit="+rate_unit+"&var_tcq="+var_tcq+"&rate="+rate+"&counterparty_abbr="+counterparty_abbr+"&deal_map="+deal_map+
		"&u="+u+"&end_dt="+end_dt+"&use_flag="+use_flag+"&start_dt="+start_dt+"&closure_eff_dt="+clsr_eff_dt.value+"&closure_note="+closure_note1.value+"&closure_type="+closure_type;
		
		if(flag==true)
		{
			if(parseInt(var_tcq)!=0)
			{
				if(!newWindow || newWindow.closed)
				{
					newWindow = window.open(url,"Apply Closure","top=10,left=10,width=1300,height=900,scrollbars=1");
				}
				else
				{
					newWindow.close();
					newWindow = window.open(url,"Apply Closure","top=10,left=10,width=1300,height=900,scrollbars=1");
				}
			}
			else
			{
				var cnf = "";
				if(closure_type=='Y')
				{
					cnf="Do you want to Close the contract("+deal_map+")?";
				}
				else if(closure_type=='R')
				{
					cnf="Do you want to Terminate the contract("+deal_map+")?";
				}
				a = confirm(cnf);
				if(a)
				{
					customer_cd.value = counterpty_cd;
					agmt_no.value=agmtNo;
					agmt_rev_no.value=agmtRev;
					cont_no.value=contNo;
					cont_rev_no.value=contRev;
					cont_type.value = contract_type;
					clsr_type.value = closure_type;
					closure_note.value = closure_note1.value;
					
					document.forms[0].action = "../servlet/Frm_EnergyBank";
					document.forms[0].method="post";
					document.forms[0].option.value = "APPROVE_CLOSURE";
					document.forms[0].closure_eff_dt.value = clsr_eff_dt.value;
					document.forms[0].tcq.value = tcq;
					document.forms[0].submit(); 
				}
				
			}
		}
		else
		{
			alert(msg);
		}
	}
	
}

function opnerRefresh(msg,msg_type)
{
	document.forms[0].msg.value=msg;
	document.forms[0].msg_type.value=msg_type;
	
	refresh();
}

function rejectClosure(counterpty_cd,agmtNo,agmtRev,contNo,contRev,contract_type,deal_map,buySale,agmtType,closure_type)
{
	var cnf = "";
	if(closure_type=='Y')
	{

		cnf="Do you want to reject contract ("+deal_map+") closure?";
	}
	else if(closure_type=='R')
	{
		cnf="Do you want to reject contract ("+deal_map+") termination?";
	}
	var a=confirm(cnf);
	var customer_cd = document.forms[0].customer_cd;
	var agmt_no = document.forms[0].agmt_no;
	var agmt_rev_no = document.forms[0].agmt_rev_no;
	var cont_no = document.forms[0].cont_no;
	var cont_rev_no = document.forms[0].cont_rev_no;
	var cont_type = document.forms[0].cont_type;
	var agmt_type=document.forms[0].agmt_type;
	var buy_sale=document.forms[0].buy_sale;
	if(a)
	{
		customer_cd.value = counterpty_cd;
		agmt_no.value=agmtNo;
		agmt_rev_no.value=agmtRev;
		cont_no.value=contNo;
		cont_rev_no.value=contRev;
		cont_type.value = contract_type;
		agmt_type.value = agmtType;
		buy_sale.value=buySale;
		if(agmt_type.value=='A')
		{
			document.forms[0].action = "../servlet/Frm_LtcoraMaster";
			document.forms[0].method="post";
			document.forms[0].option.value = "APPROVE_REJECT_CLOSURE_TERMINATION";
			document.forms[0].opration.value = "REJECT";
			document.forms[0].submit();
		}
		else
		{
			document.forms[0].action = "../servlet/Frm_ContracMaster";
			document.forms[0].method="post";
			document.forms[0].option.value = "REJECT_CLOSURE";
			document.forms[0].submit();
		}
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.contract_master.DataBean_ContractMaster" id="contract" scope="request"></jsp:useBean>
<%
String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

contract.setCallFlag("CONT_CLOSURE_REQUEST");
contract.setComp_cd(owner_cd);
contract.setSegmentType(segmentType);
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
Vector VINDEX = contract.getVINDEX();
Vector VTCQ_SIGN = contract.getVTCQ_SIGN();
Vector VVAR_TCQ_QTY = contract.getVVAR_TCQ_QTY();
Vector VREMARK = contract.getVREMARK();
Vector VCLOSURE_REQ_FLAG = contract.getVCLOSURE_REQ_FLAG();
Vector VDLV = contract.getVDLV();
Vector VBUY_SALE = contract.getVBUY_SALE();
Vector VAGMT_TYPE = contract.getVAGMT_TYPE();
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
			    		Contract Closure|Termination Request
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
									<th rowspan="2"><b>Closure Eff. Date<span class="s-red">*</span></b></th>
									<th rowspan="2"><b>Closure Note<span class="s-red">*</span></b></th>
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
										<td><%=VDIS_CONT_MAPPING.elementAt(i)%><%=VDLV.elementAt(i)%></td>
										<td><%=VCONTRACT_TYPE_NM.elementAt(i) %></td>
										<td><%=VCONT_REF_NO.elementAt(i)%></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
										<td align="right"><%=VRATE.elementAt(i)%></td>
										<td align="center"><%=VRATE_UNIT_NM.elementAt(i)%></td>
										<td align="right"><%=VTCQ.elementAt(i)%></td>
										<td align="right"><%=VSUPPLIED_MMBTU.elementAt(i) %></td>
										<td align="center"><%=VTCQ_SIGN.elementAt(i) %></td>
										<td align="right"><%=VVAR_TCQ_QTY.elementAt(i) %></td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="closure_eff_dt_<%=j%>_<%=i%>" id="closure_eff_dt_<%=j%>_<%=i%>" value="<%=VEND_DT.elementAt(i)%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div>
					    					</div>
										</td>
										<td align="center">
											<div style="width:200px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm" name="closure_note<%=i%>" id="closure_note_<%=j%>_<%=i%>" value="<%=VREMARK.elementAt(i) %>" maxLength="60">
												</div>
											</div>
										</td>
										<td align="center">
										<%if(write_access.equals("Y")){%>
											<input type="button" class="btn btn-warning com-btn" value="<%if(VCLOSURE_REQ_FLAG.elementAt(i).equals("Y")){%>Apply Closure<%}else if(VCLOSURE_REQ_FLAG.elementAt(i).equals("R")){%>Apply Termination<%} %>" style="border-radius: 50px;"
											onclick="applyClosure('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i) %>',
											'<%=VAGMT_REV_NO.elementAt(i) %>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i) %>',
											'<%=VRATE.elementAt(i) %>','<%=VRATE_UNIT.elementAt(i)%>','<%=VTCQ_SIGN.elementAt(i)%>','<%=VVAR_TCQ_QTY.elementAt(i) %>','<%=VTCQ.elementAt(i) %>','<%=VEND_DT.elementAt(i) %>','<%=VSTART_DT.elementAt(i) %>','<%=VCOUNTERPARTY_ABBR.elementAt(i)%>','<%=VDIS_CONT_MAPPING.elementAt(i)%>','<%=j %>','<%=i %>' 
											,'<%=VCLOSURE_REQ_FLAG.elementAt(i) %>','<%=VBUY_SALE.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>');" />
										
										<%}else{ %>
											<input type="button" class="btn btn-warning com-btn" value="Apply Closure" style="border-radius: 50px;" disabled>
										<%} %>
										</td>
										<td>
											<%if(write_access.equals("Y")){%>
											<input type="button" class="btn btn-warning com-btn" value="<%if(VCLOSURE_REQ_FLAG.elementAt(i).equals("Y")){%>Reject Closure<%}else if(VCLOSURE_REQ_FLAG.elementAt(i).equals("R")){%>Reject Termination<%} %>" style="border-radius: 50px;"
											onclick="rejectClosure('<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VAGMT_NO.elementAt(i) %>',
												'<%=VAGMT_REV_NO.elementAt(i) %>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i) %>','<%=VDIS_CONT_MAPPING.elementAt(i)%>'
												,'<%=VBUY_SALE.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>','<%=VCLOSURE_REQ_FLAG.elementAt(i)%>');">
										
											<%}else{ %>
												<input type="button" class="btn btn-warning com-btn" value="Reject Closure" style="border-radius: 50px;" disabled>
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
									<td colspan="17" align="center"><%=utilmsg.infoMessage("<b>No Closure Requested!</b>") %></td>
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

<input type="hidden" name="msg" value="">
<input type="hidden" name="msg_type" value="">

<input type="hidden" name="customer_cd" value="">
<input type="hidden" name="agmt_no" value="">
<input type="hidden" name="agmt_rev_no" value="">
<input type="hidden" name="cont_no" value="">
<input type="hidden" name="cont_rev_no" value="">
<input type="hidden" name="cont_type" value="">
<input type="hidden" name="option" value="">
<input type="hidden" name="closure_eff_dt" value="">
<input type="hidden" name="tcq" value="">
<input type="hidden" name="clsr_type" value="">
<input type="hidden" name="closure_note" value="">
<input type="hidden" name="agmt_type" value="">
<input type="hidden" name="buy_sale" value="">
<input type="hidden" name="opration" value="">

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