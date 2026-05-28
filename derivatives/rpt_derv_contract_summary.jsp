
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
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var flag=true;
	var msg="";
	
	var count = compareDate(from_dt,to_dt);
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_derv_contract_summary.jsp?counterparty_cd="+counterparty_cd+
				"&u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var sysdate = document.forms[0].sysdate.value;
	
	var url = "xls_derv_contract_summary.jsp?fileName=DerivativesContractSummary "+sysdate+".xls&counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Report" id="derv_contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

derv_contract.setCallFlag("DERV_CONTRACT_SUMMARY");
derv_contract.setComp_cd(owner_cd);
derv_contract.setCounterparty_cd(counterparty_cd);
derv_contract.setFrom_dt(from_dt);
derv_contract.setTo_dt(to_dt);
derv_contract.init();

Vector VCOUNTERPARTY_CD = derv_contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = derv_contract.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = derv_contract.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = derv_contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = derv_contract.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = derv_contract.getVMST_COUNTERPARTY_ABBR();
Vector VQTY = derv_contract.getVQTY();
Vector VQTY_UNIT = derv_contract.getVQTY_UNIT();
Vector VSIGNING_DT = derv_contract.getVSIGNING_DT();
Vector VSTART_DT = derv_contract.getVSTART_DT();
Vector VEND_DT = derv_contract.getVEND_DT();
Vector VRATE = derv_contract.getVRATE();
Vector VRATE_UNIT = derv_contract.getVRATE_UNIT();
Vector VDISP_DEAL_ID = derv_contract.getVDISP_DEAL_ID();
Vector VCONT_REF = derv_contract.getVCONT_REF();
Vector VENT_DT = derv_contract.getVENT_DT();
Vector VENT_BY = derv_contract.getVENT_BY();
Vector VPRICE_TYPE = derv_contract.getVPRICE_TYPE();
Vector VPLANT_UNIT = derv_contract.getVPLANT_UNIT();
Vector BU_PLANT_NM = derv_contract.getVBU_PLANT();
Vector VINDEX = derv_contract.getVINDEX();
Vector VDUE_DATE = derv_contract.getVDUE_DATE();
Vector VEXCHANGE_RATE = derv_contract.getVEXCHANGE_RATE();
Vector VEXCHNG_RATE_NM = derv_contract.getVEXCHNG_RATE_NM();
Vector VTRADE_DT = derv_contract.getVTRADE_DT();
Vector VTOTAL_QTY = derv_contract.getVTOTAL_QTY();
Vector VBUY_SELL = derv_contract.getVBUY_SELL();
Vector VCURVE_NM = derv_contract.getVCURVE_NM();
Vector VPROD_NM = derv_contract.getVPROD_NM();
Vector VCONV_FACTOR = derv_contract.getVCONV_FACTOR();
Vector VDUE_DATE_IN = derv_contract.getVDUE_DATE_IN();
Vector VMONTH_YEAR = derv_contract.getVMONTH_YEAR();
Vector VHOLIDAY_STATE = derv_contract.getVHOLIDAY_STATE();
Vector VINSTRUMENT_STATUS = derv_contract.getVINSTRUMENT_STATUS();

String totalQty = derv_contract.getTotalQty();

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
				    		Derivatives Contract Summary Report 
	   	 				</div>
	   	 				<a>
							<span class="input-group-text">
							 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
							</span>
						</a>
				    </div>
				</div>      	
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
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
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);checkDateRange(this,document.forms[0].to_dt);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);checkDateRange(document.forms[0].from_dt,this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    			<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="filterbysearch">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Trader
											<div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_nm" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract#
											<div align="center"><input class="form-control form-control-sm" type="text" id="contract" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract Ref
											<div align="center"><input class="form-control form-control-sm" type="text" id="contract_ref" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Trade Date</th>
										<th>Trade Confirmation Date</th>
										<th>Trade Month/Year</th>
										<th>Contract Period</th>
										<th>Status
											<div align="center"><input class="form-control form-control-sm" type="text" id="status" onkeyup="Search(this,'8');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Product
											<div align="center"><input class="form-control form-control-sm" type="text" id="product" onkeyup="Search(this,'9');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Curve Name
											<div align="center"><input class="form-control form-control-sm" type="text" id="curve" onkeyup="Search(this,'10');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Buy/Sell
											<div align="center"><input class="form-control form-control-sm" type="text" id="buy_sell" onkeyup="Search(this,'11');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Price Type</th>																			
										<th>Contract Price</th>
										<th>Currency</th>
										<th>Qty</th>
										<th>Qty Unit</th>
										<th>Payment Due Period(Days)</th>
										<th>Holiday State</th>
										<th>Conversion Factor</th>
										<th>Business Unit 
											<div align="center"><input class="form-control form-control-sm" type="text" id="bu" onkeyup="Search(this,'20');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Customer Plants
											<div align="center"><input class="form-control form-control-sm" type="text" id="plant" onkeyup="Search(this,'21');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Contract Enter By</th>
										<th>Contract Enter Date</th>
									</tr>
								</thead>
								<tbody>
									<%int k=0;
									if(VCOUNTERPARTY_CD.size() > 0){ %>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size(); i++){ 
										k+=1;
										%>
											<tr>
												<td align="center"><%=k%></td>
												<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
												<td align="center"><%=VDISP_DEAL_ID.elementAt(i)%></td>
												<td align="center"><%=VCONT_REF.elementAt(i)%></td>
												<td align="center"><%=VTRADE_DT.elementAt(i)%></td>
												<td align="center"><%=VSIGNING_DT.elementAt(i)%></td>
												<td align="center"><%=VMONTH_YEAR.elementAt(i) %></td>
												<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
												<td align="center"><%=VINSTRUMENT_STATUS.elementAt(i) %></td>
												<td align="center"><%=VPROD_NM.elementAt(i) %></td>
												<td align="center"><%=VCURVE_NM.elementAt(i) %></td>
												<td align="center"><%=VBUY_SELL.elementAt(i) %></td>
												<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
												<td align="right"><%=VRATE.elementAt(i)%></td>
												<td align="center"><%=VRATE_UNIT.elementAt(i)%></td>
												<td align="right"><%=VQTY.elementAt(i)%></td>
												<td align="center"><%=VQTY_UNIT.elementAt(i)%></td>
												<td><%=VDUE_DATE.elementAt(i) %> <%=VDUE_DATE_IN.elementAt(i) %></td>
												<td><%=VHOLIDAY_STATE.elementAt(i) %></td>
												<td align="right"><%=VCONV_FACTOR.elementAt(i) %></td>
												<td><%=BU_PLANT_NM.elementAt(i) %></td>
												<td><%=VPLANT_UNIT.elementAt(i) %></td>
												<td align="center"><%=VENT_BY.elementAt(i)%></td>
												<td align="center"><%=VENT_DT.elementAt(i)%></td>
											</tr>
										<%} %>
										<tr>
											<td colspan="15" align="right"><b>Total :</b></td>
											<td align="right"><b><%=totalQty %></b></td>
											<td colspan="8"></td>
										</tr>
									<%}else{ %>
										<tr>
											<td colspan="24" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
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

<input type="hidden" name="sysdate" value="<%=sysdate%>">

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
<script>
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
</script>
</html>