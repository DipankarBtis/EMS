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
	var buy_sale = document.forms[0].buy_sale.value;
	
	var url = "../ltcora/frm_ltcora_cargo_cont_list.jsp?active_status="+active_status+"&counterparty_cd="+counterparty_cd+"&buy_sell="+buy_sale;

// 	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh_date()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var buy_sale = document.forms[0].buy_sale.value;
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
			var url = "../ltcora/frm_ltcora_cargo_cont_list.jsp?active_status="+active_status+"&counterparty_cd="+counterparty_cd+"&buy_sell="+buy_sale+
					"&from_dt="+from_dt+"&to_dt="+to_dt;
			location.replace(url);
			}
		}
	}
}
function setValue(countpty_cd,cont_no,cont_rev_no,cont_type,disp_cont_no,cont_ref_no,cont_name,no_cargo,agmt_no,contract_type,agmt_rev,agmt_type,buy_sell,start_dt,end_dt,sug_per)
{
	window.opener.setContDetail(countpty_cd,cont_no,cont_rev_no,cont_type,disp_cont_no,cont_ref_no,cont_name,no_cargo,agmt_no,contract_type,agmt_rev,agmt_type,buy_sell,start_dt,end_dt,sug_per);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="ltcora" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String active_status = request.getParameter("active_status")==null?"Y":request.getParameter("active_status");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String buy_sale=request.getParameter("buy_sell")==null?"":request.getParameter("buy_sell");
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

ltcora.setCallFlag("LTCORA_CONTRACT_LIST");
ltcora.setCounterparty_cd(counterparty_cd);
ltcora.setComp_cd(owner_cd);
ltcora.setBuy_sale(buy_sale);
ltcora.setActive_status(active_status);
ltcora.setFrom_dt(from_dt);
ltcora.setTo_dt(to_dt);
ltcora.init();

// System.out.println(counterparty_cd+"::"+owner_cd+"::"+buy_sale+"::"+active_status+"::"+from_dt+"::"+to_dt);

Vector VCOUNTERPARTY_CD = ltcora.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = ltcora.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = ltcora.getVCOUNTERPARTY_ABBR();
Vector VCONT_NO = ltcora.getVCONT_NO();
Vector VCONT_REV_NO = ltcora.getVCONT_REV_NO();
Vector VAGMT_NO = ltcora.getVAGMT_NO();
Vector<String> VCONT_TYPE = ltcora.getVCONT_TYPE();
Vector VAGMT_REV_NO = ltcora.getVAGMT_REV_NO();
Vector VAGMT_TYPE = ltcora.getVAGMT_TYPE();
Vector VSTART_DT = ltcora.getVSTART_DT();
Vector VEND_DT = ltcora.getVEND_DT();
Vector VCONT_NAME = ltcora.getVCONT_NAME();
Vector VCONT_STATUS = ltcora.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = ltcora.getVCONT_STATUS_FLG();
Vector VCONT_CARGO_NO = ltcora.getVCONT_CARGO_NO();
Vector VCONT_REF_NO = ltcora.getVCONT_REF_NO();
Vector VDEAL_MAPPING = ltcora.getVDEAL_MAPPING();
Vector VBUY_SELL = ltcora.getVBUY_SELL();
Vector VSUG_PER = ltcora.getVSUG_PER();
Vector VCONTRACT_TYPE_NM = ltcora.getVCONTRACT_TYPE_NM();
%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	LTCORA CN/Period List
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
										<!--<th>Contact Rev#</th> -->
										<th class="ems_thsort0">Contract Ref#</th>
										<th class="ems_thsort0">Status</th>
										<th class="ems_thsort0">Contact Type</th>
										<th class="ems_thsort0">Contract Name</th>
										<th class="ems_thsort0">Start - End Date</th>
										<th class="ems_thsort0">No. of Cargo</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size()> 0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
									<tr>
										<td align="center"><input type="radio" onclick="setValue('<%=VCOUNTERPARTY_CD.elementAt(i) %>'
										,'<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>'
										,'<%=contract_type%>','<%=VDEAL_MAPPING.elementAt(i)%>'
										,'<%=VCONT_REF_NO.elementAt(i)%>','<%=VCONT_NAME.elementAt(i)%>',
										'<%=VCONT_CARGO_NO.elementAt(i)%>',
										'<%=VAGMT_NO.elementAt(i)%>',
										'<%=VCONT_TYPE.elementAt(i)%>',
										'<%=VAGMT_REV_NO.elementAt(i)%>',
										'<%=VAGMT_TYPE.elementAt(i)%>',
										'<%=VBUY_SELL.elementAt(i)%>',
										'<%=VSTART_DT.elementAt(i)%>',
										'<%=VEND_DT.elementAt(i)%>',
										'<%=VSUG_PER.elementAt(i)%>')" <%if(!VCONT_STATUS.elementAt(i).equals("Approved")&&!VCONT_STATUS_FLG.elementAt(i).equals("C")&&!VCONT_STATUS_FLG.elementAt(i).equals("R")&&!VCONT_STATUS_FLG.elementAt(i).equals("T")){ %>disabled title="Contract is not Approved." <%}%>></td>											
										<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
										<td align="center"><%=VDEAL_MAPPING.elementAt(i)%></td>
										<%--<td align="center"><%=VCONT_REV_NO.elementAt(i)%></td> --%>
										<td><%=VCONT_REF_NO.elementAt(i) %></td>
										<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
										<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%></td>
										<td><%=VCONT_NAME.elementAt(i) %></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>	
										<td align="center"><%=VCONT_CARGO_NO.elementAt(i)%></td>											
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="10" align="center">
											<%=utilmsg.infoMessage("<b>LTCORA CN/Period List not Available!</b>") %>
										</td>
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
<input type="hidden" name="buy_sale" value="<%=buy_sale%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

</form>
</body>
</html>