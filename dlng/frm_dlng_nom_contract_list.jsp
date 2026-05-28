<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh(active_status)
{
	var gas_dt = document.forms[0].gas_dt.value;
	var nomination_freq = document.forms[0].nomination_freq.value;
	var nomination_type = document.forms[0].nomination_type.value;
	
	var url = "../dlng/frm_dlng_nom_contract_list.jsp?active_status="+active_status+"&gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+"&nomination_type="+nomination_type;

// 	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh_date()
{
	var gas_dt = document.forms[0].gas_dt.value;
	var nomination_freq = document.forms[0].nomination_freq.value;
	var nomination_type = document.forms[0].nomination_type.value;
	var active_status = document.forms[0].active_status.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var sysdate = document.forms[0].sysdate.value;
	
	var temp_from_dt = from_dt.split("/")[2]+from_dt.split("/")[1]+from_dt.split("/")[0];
	var temp_to_dt = to_dt.split("/")[2]+to_dt.split("/")[1]+to_dt.split("/")[0];
	var temp_sysdate = sysdate.split("/")[2]+sysdate.split("/")[1]+sysdate.split("/")[0];
	
	var flg=true;
	if(temp_to_dt >= temp_sysdate)
	{
		alert("To Date must be less than Current Date");
		flg=false;
	}
	var flag=checkDateRangeOnApply(document.forms[0].from_dt,document.forms[0].to_dt);
	if(flag && flg)
	{
		if(trim(from_dt)!="" && trim(to_dt)!="")
		{
			if(flag==true && flg==true)
			{
			var url = "../dlng/frm_dlng_nom_contract_list.jsp?active_status="+active_status+"&gas_dt="+gas_dt+"&nomination_freq="+nomination_freq+
					"&nomination_type="+nomination_type+"&from_dt="+from_dt+"&to_dt="+to_dt;
			location.replace(url);
			}
		}
	}
}
function setValue(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type,cargo_no)
{
	window.opener.setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type,cargo_no);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_Dlng_ContractMgmt" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String active_status = request.getParameter("active_status")==null?"Y":request.getParameter("active_status");
String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
String nomination_freq=request.getParameter("nomination_freq")==null?"W":request.getParameter("nomination_freq");
String nomination_type=request.getParameter("nomination_type")==null?"B":request.getParameter("nomination_type");

String sysdate=utildate.getSysdate();
String days="-30";
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

if(from_dt.equals(sysdate) && to_dt.equals(sysdate))
{
	from_dt=utildate.getDate(sysdate, days);
	to_dt=utildate.getPreviousDate();
}

contract.setCallFlag("DLNG_NOM_CONTRACT_LIST");
contract.setGas_dt(gas_dt);
contract.setNomination_freq(nomination_freq);
contract.setComp_cd(owner_cd);
contract.setNomination_type(nomination_type);
contract.setActive_status(active_status);
contract.setFrom_dt(from_dt);
contract.setTo_dt(to_dt);
contract.init();

Vector VBUYER_CD = contract.getVBUYER_CD();
Vector VBUYER_NAME = contract.getVBUYER_NAME();
Vector VBUYER_ABBR = contract.getVBUYER_ABBR();
Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONT_REV_NO = contract.getVCONT_REV_NO();
Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV_NO = contract.getVAGMT_REV_NO();
Vector VTCQ = contract.getVTCQ();
Vector VQTY_UNIT = contract.getVQTY_UNIT();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VCONT_NAME = contract.getVCONT_NAME();
Vector VCONT_STATUS = contract.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = contract.getVCONT_STATUS_FLG();
Vector VFCC_FLAG = contract.getVFCC_FLAG();
Vector VCONT_REF_NO = contract.getVCONT_REF_NO();
Vector VRATE_FORMULA = contract.getVRATE_FORMULA();
Vector VIS_ALLOCATED = contract.getVIS_ALLOCATED();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VSUPPLIED_MMBTU = contract.getVSUPPLIED_MMBTU();
Vector VDIS_CONT_MAPPING = contract.getVDIS_CONT_MAPPING();
Vector VCONTRACT_TYPE_NM = contract.getVCONTRACT_TYPE_NM();
Vector VCARGO_NO = contract.getVCARGO_NO();

%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	DLNG Contract List
				    </div>
				</div>
				<div class="card-header">
					<ul class="nav nav-tabs card-header-tabs px-3">
						<li class="nav-item">
							<span class="nav-link  <%if(active_status.equals("Y")) {%>active<%} %>" 
							onclick="refresh('Y');" style="cursor:default;">Active</span>
						</li>
						<li class="nav-item">
							<span class="nav-link <%if(active_status.equals("N")) {%>active<%} %>" 
							onclick="refresh('N');refresh_date();" style="cursor:default;">In-active</span>
						</li>
					</ul>
				</div>
				<br>
				<div class="card-body cdbody">
					<div class="row mb-2">
					    <div class="col-12">
					        <div class="row g-2 align-items-center">
					        <% if(active_status.equals("N")) { %>
					            <div class="col-12 col-sm-auto">
					                <label class="form-label mb-0"><b>From</b></label>
					            </div>
					            <div class="col-12 col-sm-3 col-md-2">
					                <div class="input-group input-group-sm">
					                	<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
										onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
										<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					                </div>
					            </div>
					            <div class="col-12 col-sm-auto">
					                <label class="form-label mb-0"><b>To</b></label>
					            </div>
					            <div class="col-12 col-sm-3 col-md-2">
					                <div class="input-group input-group-sm" >
										<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
										onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
										<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
									</div>
					            </div>
					            <div class="col-12 col-sm-auto">
					                <button type="button" class="btn btn-warning com-btn" onclick="refresh_date();">
					                    Apply Filters
					                </button>
					            </div>
					        <% } %>
					            <div class="col-12 col-sm col-md-3 ms-auto">
					            	<input class="form-control form-control-sm" type="text" id="globalSearch" onkeyup="globalSearchTable(this,'0')" placeholder="Search.." />
					            </div>
					        </div>
					    </div>
					</div>
					<br>
					<div class="col-sm-12 col-xs-12 col-md-12">
						<div class="table-responsive">
							<table class="table table-bordered ems_sorttbl" id="ems_table0">
								<thead id="ems_tbsort0">
									<tr valign="top">
										<th>Select</th>
										<th class="ems_thsort0">Customer</th>											
										<th class="ems_thsort0">Contact#</th>
										<th class="ems_thsort0">Contract Type</th>
										<th class="ems_thsort0">Contract Rev#</th>
										<th class="ems_thsort0">Contract/ Trade Ref#</th>											
										<th class="ems_thsort0">Start - End Date</th>
										<th class="ems_thsort0">Rate</th>
										<th class="ems_thsort0">TCQ</th>
										<th class="ems_thsort0">Allocation Status</th>
										<th class="ems_thsort0">Status</th>
										<th class="ems_thsort0">Supplied MMBTU</th>
									</tr>
								</thead>
								<tbody>
								<%if(VBUYER_CD.size() > 0){ %>
									<%for(int i=0; i<VBUYER_CD.size(); i++){ %>
									<tr>
										<td align="center">
											<input type="radio" <%if(!VCONT_STATUS_FLG.elementAt(i).equals("Y") && !VFCC_FLAG.elementAt(i).equals("Y")){ %>disabled<%}
											else if(!VIS_ALLOCATED.elementAt(i).equals("Y")){%>disabled<%} %>
											onclick="setValue('<%=VBUYER_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>',
												'<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
												'<%=VCONT_REV_NO.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>','<%=VEND_DT.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>'
												,'<%=VCARGO_NO.elementAt(i)%>')">
										</td>
										<td align="center" title="<%=VBUYER_NAME.elementAt(i)%>"><%=VBUYER_ABBR.elementAt(i)%></td>																					
										<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
										<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%></td>
										<td align="center"><%=VCONT_REV_NO.elementAt(i)%></td>
										<td><%=VCONT_REF_NO.elementAt(i) %></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="right"><%=VRATE_FORMULA.elementAt(i)%></td>
										<td align="right"><%=VTCQ.elementAt(i)%>&nbsp;<%=VQTY_UNIT.elementAt(i)%></td>
										<td align="center">
											<%if(VIS_ALLOCATED.elementAt(i).equals("Y")){ %>
							    			<i class="fa fa-check-circle fa-2x" style="color:green;"></i>
							    			<%}else{ %>
							    			<i class="fa fa-times-circle fa-2x" style="color:red;"></i>
							    			<%} %>
										</td>
										<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
										<td align="right"><%=VSUPPLIED_MMBTU.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="13" align="center">
											<%=utilmsg.infoMessage("<b>Supply Contact List is not Available!</b>") %>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="" align="right">
						<input type="button" class="btn btn-warning com-btn" value="Close" onclick="window.close();">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="gas_dt" value="<%=gas_dt%>">
<input type="hidden" name="active_status" value="<%=active_status%>">
<input type="hidden" name="nomination_freq" value="<%=nomination_freq%>">
<input type="hidden" name="nomination_type" value="<%=nomination_type%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

</form>
</body>
</html>