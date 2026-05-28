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
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agreement_type = document.forms[0].agreement_type.value;
	var contract_type = document.forms[0].contract_type.value;
	
	var url = "../gta/frm_gta_contract_list.jsp?active_status="+active_status+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+"&contract_type="+contract_type;

// 	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh_date()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var agreement_type = document.forms[0].agreement_type.value;
	var contract_type = document.forms[0].contract_type.value;
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
			var url = "../gta/frm_gta_contract_list.jsp?active_status="+active_status+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
					"&agreement_type="+agreement_type+"&from_dt="+from_dt+"&to_dt="+to_dt;
			location.replace(url);
			}
		}
	}
}
function setValue(agmtno,agmt_revno,contno,cont_revno,agmt_type,countpty_cd,cont_type)
{
	window.opener.setContractDetail(agmtno,agmt_revno,contno,cont_revno,agmt_type,countpty_cd,cont_type);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_GtaMaster" id="gta" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String active_status = request.getParameter("active_status")==null?"Y":request.getParameter("active_status");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");

String sysdate=utildate.getSysdate();
String days="-30";
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

if(from_dt.equals(sysdate) && to_dt.equals(sysdate))
{
	from_dt=utildate.getDate(sysdate, days);
	to_dt=utildate.getPreviousDate();
}

gta.setCallFlag("GTA_CONTRACT_LIST");
gta.setComp_cd(owner_cd);
gta.setCounterparty_cd(counterparty_cd);
gta.setContract_type(contract_type);
gta.setAgreement_type(agreement_type);
gta.setActive_status(active_status);
gta.setFrom_dt(from_dt);
gta.setTo_dt(to_dt);
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
Vector VAGMT_TYPE =gta.getVAGMT_TYPE();
Vector VCOUNTERPARTY_CD =gta.getVCOUNTERPARTY_CD();
Vector VCONTRACT_TYPE =gta.getVCONTRACT_TYPE();
Vector VCOUNTERPARTY_NM =gta.getVCOUNTERPARTY_NM();

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
									<tr>
										<th></th>
										<th class="ems_thsort0">Counterparty</th>
										<th class="ems_thsort0">Contract#</th>
										<th class="ems_thsort0">Contract Ref#</th>				    		
								    	<th class="ems_thsort0">Contract Period</th>
								    	<th class="ems_thsort0">Business Unit</th>
								    	<th class="ems_thsort0">Entry Point</th>
								    	<th class="ems_thsort0">Exit Point</th>
								    	<%if(contract_type.equals("C")){ %>
								    	<th class="ems_thsort0">Linked Sales Contract</th>
								    	<%} %>
								    </tr>
								</thead>
								<tbody>
								<%if(VCONT_NO.size() > 0){ %>
									<%for(int i=0;i<VCONT_NO.size(); i++){ %>
									<tr>
										<td align="center">
											<input type="radio" onclick="setValue('<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VAGMT_TYPE.elementAt(i)%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>');">
										</td>
										<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
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
										<td align="center" <%if(contract_type.equals("C")){ %>colspan="9"<%}else{ %>colspan="8"<%} %>><%=utilmsg.infoMessage("<b>No GT Contract is Created!</b>") %></td>
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
	
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="active_status" value="<%=active_status%>">
<input type="hidden" name="agreement_type" value="<%=agreement_type%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
	
</form>		
</body>
</html>