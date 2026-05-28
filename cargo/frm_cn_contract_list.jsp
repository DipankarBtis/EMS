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
	var fcc_flag = document.forms[0].fcc_flag.value;
	
	var url = "../cargo/frm_cn_contract_list.jsp?active_status="+active_status+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&fcc_flag="+fcc_flag;

// 	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function refresh_date()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var contract_type = document.forms[0].contract_type.value;
	var fcc_flag = document.forms[0].fcc_flag.value;
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
			var url = "../cargo/frm_cn_contract_list.jsp?active_status="+active_status+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+
					  "&fcc_flag="+fcc_flag+"&from_dt="+from_dt+"&to_dt="+to_dt;
			location.replace(url);
			}
		}
	}
}
function setValue(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,agmt_base)
{
	window.opener.setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,agmt_base);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.cargo.DataBean_Cargo_mst" id="cargo_list" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String active_status = request.getParameter("active_status")==null?"Y":request.getParameter("active_status");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String fcc_flag = request.getParameter("fcc_flag")==null?"":request.getParameter("fcc_flag");

String sysdate=utildate.getSysdate();
String days="-30";
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

if(from_dt.equals(sysdate) && to_dt.equals(sysdate))
{
	from_dt=utildate.getDate(sysdate, days);
	to_dt=utildate.getPreviousDate();
}

cargo_list.setCallFlag("CN_CONT_LIST");
if(fcc_flag!=null && fcc_flag.equals("Y"))
{
	cargo_list.setCallFlag("CN_CONT_LIST_FCC");
}
cargo_list.setCounterparty_cd(counterparty_cd);
cargo_list.setContract_type(contract_type);
cargo_list.setComp_cd(owner_cd);
cargo_list.setActive_status(active_status);
cargo_list.setFrom_dt(from_dt);
cargo_list.setTo_dt(to_dt);
cargo_list.init();

Vector VBUYER_CD = cargo_list.getVBUYER_CD();
Vector VBUYER_NAME = cargo_list.getVBUYER_NAME();
Vector VCONT_NO = cargo_list.getVCONT_NO();
Vector VCONT_REV_NO = cargo_list.getVCONT_REV_NO();
Vector VAGMT_NO = cargo_list.getVAGMT_NO();
Vector VAGMT_REV_NO = cargo_list.getVAGMT_REV_NO();
Vector VSTART_DT = cargo_list.getVSTART_DT();
Vector VEND_DT = cargo_list.getVEND_DT();
Vector VCONT_NAME = cargo_list.getVCONT_NAME();
Vector VCONT_DISP_NAME = cargo_list.getVCONT_DISP_NAME();
Vector VCONT_STATUS = cargo_list.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = cargo_list.getVCONT_STATUS_FLG();
Vector VCONT_REF_NO = cargo_list.getVCONT_REF_NO();
Vector VAGMT_BASE = cargo_list.getVAGMT_BASE();
%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="topheader">
						Confirmation Notice(CN) List
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
										<%--<th>Contract Rev#</th>--%>
										<th class="ems_thsort0"><%if(contract_type.equals("I")){ %>Trade Ref#<%}else{%>Contract Ref#<%} %></th>
										<th class="ems_thsort0">Status</th>
										<th class="ems_thsort0">Contract Name</th>
										<th class="ems_thsort0">Start - End Date</th>
									</tr>
								</thead>
								<tbody>
									<%if(VBUYER_CD.size() > 0){ %>
									<%for(int i=0; i<VBUYER_CD.size(); i++){ %>
									<tr>
										<td align="center"><input type="radio" onclick="setValue('<%=VBUYER_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VAGMT_BASE.elementAt(i)%>')"></td>
										<td><%=VBUYER_NAME.elementAt(i)%></td>
										<td align="center"><%=VCONT_DISP_NAME.elementAt(i) %></td>
										<%--<td align="center"><%=VCONT_REV_NO.elementAt(i)%></td>--%>
										<td><%=VCONT_REF_NO.elementAt(i) %></td>
										<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
										<td><%=VCONT_NAME.elementAt(i)%></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
									</tr>
									<%} %>
									<%}else{ %>
									<tr>
										<td colspan="9" align="center">
											<%=utilmsg.infoMessage("<b>CN Contact List is not Available!</b>") %>
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
<input type="hidden" name="fcc_flag" value="<%=fcc_flag%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
</form>
</body>
</html>