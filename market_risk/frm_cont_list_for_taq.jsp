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
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_cont_list_for_taq.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var dcqWindow;
function variableTaqConfig(account,countpty_cd,agmt,cont,cont_type,start_dt,end_dt)
{
	var u = document.forms[0].u.value;
	var url = "frm_mr_var_taq_config.jsp?account="+account+"&counterparty_cd="+countpty_cd+"&contract_type="+cont_type+
			"&agmt_no="+agmt+"&cont_no="+cont+"&start_dt="+start_dt+"&end_dt="+end_dt+"&u="+u;
	
	if(!dcqWindow || dcqWindow.closed)
	{
		dcqWindow = window.open(url,"Variable TAQ","top=10,left=100,width=700,height=500,scrollbars=1");
	}
	else
	{
		dcqWindow.close();
		dcqWindow = window.open(url,"Variable TAQ","top=10,left=100,width=700,height=500,scrollbars=1");
	}
	
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;		
	var url = "xls_cont_list_for_taq.jsp?from_dt="+from_dt+"&to_dt="+to_dt;
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_VariablePricing" id="market_risk" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdt = utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdt:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdt:request.getParameter("to_dt");

market_risk.setCallFlag("TAQ_CONT_LIST");
market_risk.setComp_cd(owner_cd);
market_risk.setFrom_dt(from_dt);
market_risk.setTo_dt(to_dt);
market_risk.init();

Vector VCOUNTERPARTY_CD = market_risk.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = market_risk.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = market_risk.getVCOUNTERPARTY_ABBR();
Vector VCONTRACT_TYPE = market_risk.getVCONTRACT_TYPE();
Vector VDIS_CONTRACT_TYPE = market_risk.getVDIS_CONTRACT_TYPE();
Vector VCONT_NO = market_risk.getVCONT_NO();
Vector VCONT_REV_NO = market_risk.getVCONT_REV_NO();
Vector VAGMT_NO = market_risk.getVAGMT_NO();
Vector VAGMT_REV_NO = market_risk.getVAGMT_REV_NO();
//Vector VCARGO_NO = market_risk.getVCARGO_NO();
Vector VDISPLAY_DEAL_MAP = market_risk.getVDISPLAY_DEAL_MAP();
Vector VCONT_REF_NO = market_risk.getVCONT_REF_NO();
Vector VSIGNING_DT = market_risk.getVSIGNING_DT();
Vector VSTART_DT = market_risk.getVSTART_DT();
Vector VEND_DT = market_risk.getVEND_DT();
Vector VACCOUNT = market_risk.getVACCOUNT();
Vector VTCQ = market_risk.getVTCQ();
Vector VDCQ = market_risk.getVDCQ();
Vector VSUPPLIED_QTY_MMBTU = market_risk.getVSUPPLIED_QTY_MMBTU();
Vector VBALANCE_QTY_MMBTU = market_risk.getVBALANCE_QTY_MMBTU();
Vector VASSESSED_QTY_MMBTU =market_risk.getVASSESSED_QTY_MMBTU();
Vector VTAQ_CONFIGURED = market_risk.getVTAQ_CONFIGURED();
Vector VTAQ_REMARK = market_risk.getVTAQ_REMARK();
Vector VTAQ_DETAIL = market_risk.getVTAQ_DETAIL();
Vector VENTERED_BY = market_risk.getVENTERED_BY();

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
					    	Trader Assessed Quantity 
					    </div>
					    <div>
					    	<div class="form-group row">
								
								<div class="col-auto">
				   	 				<span class="input-group-text">
									 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
									</span>	
								</div>
							</div> 					    
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-5 col-xs-5 col-md-5"></div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>From</b></label>
							</div>
						</div>
						<div class="col-auto">
		      				<div class="input-group input-group-sm">
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
	      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
      						</div>
		    			</div>
						<div class="col-auto">
							<label class="form-label"><b>To</b></label>
						</div>
						<div class="col-auto">
		      				<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
	      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
      						</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="filterbysearch">
								<thead id="tbsearch">
									<tr>
										<th>Sr#</th>
										<th>Account</th>										
										<th>Counterparty</th>
										<th>Contract Type</th>
										<th>Contract#</th>
										<th>Contract/Trade Ref#</th>
										<th>Contract Period</th>
										<th>Contractual DCQ</th>
										<th>Contractual TCQ</th>
										<th>Supplied Quantity (MMBTU)</th>										
										<th>Balanced Quantity (MMBTU)</th>										
										<th>TAQ Config</th>
										<th>TAQ Details</th>
										<th>TAQ Remarks</th>
										<th>TAQ Last Updated by</th>
										<th>TAQ (Supplied MMBTU incld.)</th>
									</tr>
								</thead>
								<tbody>
								<%if(VCOUNTERPARTY_CD.size()>0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
									<tr>
										<td align="center"><%=i+1%></td>
										<td align="center">
											<span
											<%if(VACCOUNT.elementAt(i).equals("Buy")){ %>
	    										class="alert" style="background: #ffccff; color: #cc00cc;"
	    									<%}else { %>
	    										class="alert alert-primary"
	    									<%}%>><b><%=VACCOUNT.elementAt(i)%></b></span>
		    							</td>
										<td title="<%=VCOUNTERPARTY_ABBR.elementAt(i)%>"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
										<td><%=VDIS_CONTRACT_TYPE.elementAt(i)%> </td>
										<td align="center"><%=VDISPLAY_DEAL_MAP.elementAt(i)%></td>
										<td><%=VCONT_REF_NO.elementAt(i)%></td>
										<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
										<td align="right"><%=VDCQ.elementAt(i) %></td>
										<td align="right"><%=VTCQ.elementAt(i)%></td>
										<td align="right"><%=VSUPPLIED_QTY_MMBTU.elementAt(i)%></td>
										<td align="right"><%=VBALANCE_QTY_MMBTU.elementAt(i)%></td>											
										<td align="center">										
											<input type="button" 
											<%if (VTAQ_CONFIGURED.elementAt(i).toString().equals("N")) {%> class="btn btn-sm request_btn" value="Add TAQ" <%} else {%> class="btn btn-sm config_btn"  value="Edit TAQ" <%}%>
											onclick="variableTaqConfig('<%=VACCOUNT.elementAt(i)%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>',
													'<%=VAGMT_NO.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>',
													'<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>',
													'<%=VEND_DT.elementAt(i)%>');">										
										</td>	
										<td><%=VTAQ_DETAIL.elementAt(i) %></td>
										<td><%=VTAQ_REMARK.elementAt(i) %></td>
										<td align="center"><%=VENTERED_BY.elementAt(i) %></td>
										<td align="right"><%=VASSESSED_QTY_MMBTU.elementAt(i) %></td>	
									</tr>
									<%} %>
									<%}else{ %>
										<tr>
											<td colspan="16">
												<div align="center"><%=utilmsg.infoMessage("<b>No Exposure for the Report Date!</b>")%></div>
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

<script>
filterbysearch

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch");
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	if (td) 
    	{
      		txtValue = td.textContent || td.innerText;
      		if (txtValue.toLocaleLowerCase().indexOf(filter) > -1) {
        		tr[i].style.display = "";
        		count++;
      		} else {
      			tr[i].style.display = "none";
      		}
    	}
  	}
}

$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "")
		{
		}
		else if(title == "Sr#" || title == "TAQ Config")
		{
			//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});
</script>
</body>
</html>