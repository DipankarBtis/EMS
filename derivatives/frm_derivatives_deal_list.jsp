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
	var contract_type = document.forms[0].contract_type.value;
	
	var url = "../derivatives/frm_derivatives_deal_list.jsp?active_status="+active_status+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type;

// 	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh_date()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
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
			var url = "../derivatives/frm_derivatives_deal_list.jsp?active_status="+active_status+"&counterparty_cd="+counterparty_cd+
					"&contract_type="+contract_type+"&from_dt="+from_dt+"&to_dt="+to_dt;
			location.replace(url);
			}
		}
	}
}
function setValue(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no)
{
	window.opener.setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DataBean_Derivarives_mst" id="db_derivative" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String active_status = request.getParameter("active_status")==null?"Y":request.getParameter("active_status");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");

String sysdate=utildate.getSysdate();
String days="-30";
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

if(from_dt.equals(sysdate) && to_dt.equals(sysdate))
{
	from_dt=utildate.getDate(sysdate, days);
	to_dt=utildate.getPreviousDate();
}

db_derivative.setCallFlag("HEDGE_CONT_LIST");
db_derivative.setCounterparty_cd(counterparty_cd);
db_derivative.setContract_type(contract_type);
db_derivative.setComp_cd(owner_cd);
db_derivative.setActive_status(active_status);
db_derivative.setFrom_dt(from_dt);
db_derivative.setTo_dt(to_dt);
db_derivative.init();

Vector VBUYER_CD = db_derivative.getVBUYER_CD();
Vector VBUYER_NAME = db_derivative.getVBUYER_NAME();
Vector VCONT_NO = db_derivative.getVCONT_NO();
Vector VCONT_REV_NO = db_derivative.getVCONT_REV_NO();
Vector VAGMT_NO = db_derivative.getVAGMT_NO();
Vector VAGMT_REV_NO = db_derivative.getVAGMT_REV_NO();
Vector VSTART_DT = db_derivative.getVSTART_DT();
Vector VEND_DT = db_derivative.getVEND_DT();
Vector VCONT_NAME = db_derivative.getVCONT_NAME();
Vector VCONT_DISP_NAME = db_derivative.getVCONT_DISP_NAME();
Vector VCONT_STATUS = db_derivative.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = db_derivative.getVCONT_STATUS_FLG();
Vector VCONT_REF_NO = db_derivative.getVCONT_REF_NO();
Vector VTRADE_CONF_DT = db_derivative.getVTRADE_CONF_DT();
Vector VTRADE_CONF_TIME = db_derivative.getVTRADE_CONF_TIME();

%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	Derivative (Hedge) Deal List
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
										<th class="ems_thsort0">Counterparty</th>
										<th class="ems_thsort0">Contact#</th>
										<th class="ems_thsort0">Contract Rev#</th>
										<th class="ems_thsort0"><%if(contract_type.equals("I")){ %>Trade Ref#<%}else{%>Contract Ref#<%} %></th>
										<th class="ems_thsort0">Contract Name</th>
										<th class="ems_thsort0">Trade Date</th>
										<th class="ems_thsort0">Status</th>
									</tr>
								</thead>
								<tbody>
								<%if(VBUYER_CD.size() > 0){ %>
									<%for(int i=0; i<VBUYER_CD.size(); i++){ %>
									<tr>
										<td align="center"><input type="radio" onclick="setValue('<%=VBUYER_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>')"></td>
										<td><%=VBUYER_NAME.elementAt(i)%></td>
										<td align="center"><%=VCONT_DISP_NAME.elementAt(i) %></td>
										<td align="center"><%=VCONT_REV_NO.elementAt(i)%></td>
										<td><%=VCONT_REF_NO.elementAt(i) %></td>
										<td><%=VCONT_NAME.elementAt(i)%></td>
										<td align="center"><%=VTRADE_CONF_DT.elementAt(i)%>  <%=VTRADE_CONF_TIME.elementAt(i)%></td>
										<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="9" align="center">
											<%=utilmsg.infoMessage("<b>Hedge Contact List is not Available!</b>") %>
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

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="active_status" value="<%=active_status%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

</form>
</body>
</html>