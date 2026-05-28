<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var segmentType = document.forms[0].segmentType.value;
	var ld_status = document.forms[0].ld_status.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_late_deal.jsp?counterparty_cd="+counterparty_cd+"&u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt+
			"&segmentType="+segmentType+"&ld_status="+ld_status;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function lateDealEntry()
{
	 const LDECheckbox = document.getElementById("LDE_CheckBox");
	 var ld_status = document.forms[0].ld_status.value;
	  
	  if (LDECheckbox.checked) 
	  {
		  LDECheckbox.value = "Late Deal Entry";
		  document.forms[0].ld_status.value = "Late Deal Entry";
	  } 
	  else 
	  {
		  document.forms[0].ld_status.value = "0";
	  }
}
</script>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.contract_master.DB_ContractMaster_Report" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchaseSum" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String rpt_month=request.getParameter("rpt_month")==null?"0":request.getParameter("rpt_month");
String rpt_year=request.getParameter("rpt_year")==null?"0":request.getParameter("rpt_year");
String ld_status=request.getParameter("ld_status")==null?"0":request.getParameter("ld_status");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

dbmarket.setCallFlag("LATE_DEAL_RPT");
dbmarket.setComp_cd(owner_cd);
dbmarket.setFrom_dt(from_dt);
dbmarket.setTo_dt(to_dt);
dbmarket.setSegmentType(segmentType);
dbmarket.setCounterparty_cd(counterparty_cd);
dbmarket.init();

purchaseSum.setCallFlag("PURCHASE_SUMMARY");
purchaseSum.setComp_cd(owner_cd);
purchaseSum.setFrom_dt(from_dt);
purchaseSum.setTo_dt(to_dt);
purchaseSum.setSegmentType(segmentType);
purchaseSum.setCounterparty_cd(counterparty_cd);
purchaseSum.init();

contract.setCallFlag("CONTRACT_SUMMARY");
contract.setComp_cd(owner_cd);
contract.setFrom_dt(from_dt);
contract.setTo_dt(to_dt);
contract.setSegmentType(segmentType);
contract.setCounterparty_cd(counterparty_cd);
contract.init();

Vector VPURCHASE_MST_COUNTERPARTY_CD = purchaseSum.getVMST_COUNTERPARTY_CD();
Vector VPURCHASE_MST_COUNTERPARTY_NM = purchaseSum.getVMST_COUNTERPARTY_NM();
Vector VPURCHASE_MST_COUNTERPARTY_ABBR = purchaseSum.getVMST_COUNTERPARTY_ABBR();

Vector VPURCHASE_COUNTERPARTY_CD = dbmarket.getVPURCHASE_COUNTERPARTY_CD();
Vector VPURCHASE_COUNTERPARTY_NM = dbmarket.getVPURCHASE_COUNTERPARTY_NM();
Vector VPURCHASE_COUNTERPARTY_ABBR = dbmarket.getVPURCHASE_COUNTERPARTY_ABBR();
Vector VPURCHASE_CONT_NO = dbmarket.getVPURCHASE_CONT_NO();
Vector VPURCHASE_CONT_REV_NO = dbmarket.getVPURCHASE_CONT_REV_NO();
Vector VPURCHASE_CONT_NAME = dbmarket.getVPURCHASE_CONT_NAME();
Vector VPURCHASE_START_DT = dbmarket.getVPURCHASE_START_DT();
Vector VPURCHASE_END_DT = dbmarket.getVPURCHASE_END_DT();
Vector VPURCHASE_TCQ = dbmarket.getVPURCHASE_TCQ();
Vector VPURCHASE_DCQ = dbmarket.getVPURCHASE_DCQ();
Vector VPURCHASE_DDA_DT = dbmarket.getVPURCHASE_DDA_DT();
Vector VPURCHASE_DDA_TIME = dbmarket.getVPURCHASE_DDA_TIME();
Vector VPURCHASE_ENT_DT = dbmarket.getVPURCHASE_ENT_DT();
Vector VPURCHASE_ENT_BY = dbmarket.getVPURCHASE_ENT_BY();
Vector VPURCHASE_CONTRACT_TYPE = dbmarket.getVPURCHASE_CONTRACT_TYPE();
Vector VPURCHASE_DISPLAY_SEGMENT = dbmarket.getVPURCHASE_DISPLAY_SEGMENT();
Vector VPURCHASE_TEMP_SEGMENT_TYPE = dbmarket.getVPURCHASE_TEMP_SEGMENT_TYPE();
Vector VPURCHASE_SEGMENT_TYPE = dbmarket.getVPURCHASE_SEGMENT_TYPE();
Vector VPURCHASE_DISPLAY_SEGMENT_TYP = dbmarket.getVPURCHASE_DISPLAY_SEGMENT_TYP();
Vector VPURCHASE_INDEX = dbmarket.getVPURCHASE_INDEX();

double totalQty=purchaseSum.getTotalQty();
double UnloadedtotalQty=purchaseSum.getUnloadedtotalQty();
double AvailabletotalQty=purchaseSum.getAvailabletotalQty();

Vector VSELL_MST_COUNTERPARTY_CD = contract.getVMST_COUNTERPARTY_CD();
Vector VSELL_MST_COUNTERPARTY_NM = contract.getVMST_COUNTERPARTY_NM();
Vector VSELL_MST_COUNTERPARTY_ABBR = contract.getVMST_COUNTERPARTY_ABBR();

Vector VSELL_COUNTERPARTY_CD = dbmarket.getVSELL_COUNTERPARTY_CD();
Vector VSELL_COUNTERPARTY_NM = dbmarket.getVSELL_COUNTERPARTY_NM();
Vector VSELL_COUNTERPARTY_ABBR = dbmarket.getVSELL_COUNTERPARTY_ABBR();
Vector VSELL_CONT_NO = dbmarket.getVSELL_CONT_NO();
Vector VSELL_CONT_REV_NO = dbmarket.getVSELL_CONT_REV_NO();
Vector VSELL_START_DT = dbmarket.getVSELL_START_DT();
Vector VSELL_END_DT = dbmarket.getVSELL_END_DT();
Vector VSELL_SIGNING_DT = dbmarket.getVSELL_SIGNING_DT();
Vector VSELL_ENT_DT = dbmarket.getVSELL_ENT_DT();
Vector VSELL_ENT_BY = dbmarket.getVSELL_ENT_BY();
Vector VSELL_DIS_CONT_MAPPING = dbmarket.getVSELL_DIS_CONT_MAPPING();
Vector VSELL_SEGMENT = dbmarket.getVSELL_SEGMENT();
Vector VSELL_SEGMENT_TYPE = dbmarket.getVSELL_SEGMENT_TYPE();
Vector VSELL_TEMP_SEGMENT = dbmarket.getVSELL_TEMP_SEGMENT();
Vector VSELL_TEMP_SEGMENT_TYPE = dbmarket.getVSELL_TEMP_SEGMENT_TYPE();
Vector VSELL_INDEX = dbmarket.getVSELL_INDEX();
Vector VSELL_TCQ = dbmarket.getVSELL_TCQ();
Vector VSELL_DCQ = dbmarket.getVSELL_DCQ();
Vector VSELL_DDA_DT = dbmarket.getVSELL_DDA_DT();
Vector VSELL_DDA_TIME = dbmarket.getVSELL_DDA_TIME();

Vector VPURCHASE_LATE_DEAL_FLAG = dbmarket.getVPURCHASE_LATE_DEAL_FLAG();
Vector VSELL_LATE_DEAL_FLAG = dbmarket.getVSELL_LATE_DEAL_FLAG();

String cut_off_time = dbmarket.getCut_off_time();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
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
					    	Late Deals Report
					    </div>
					    <div align="right" onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4"></div>
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
					</div>&nbsp;
					<div class="row m-b-5">
						<div class="col-auto">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Contract</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="segmentType" onchange="refresh()">
										<option value="0">--All--</option>
										<option value="D">Domestic NG Purchase</option>
										<option value="I">IGX Purchase</option>
										<option value="N">LNG Cargo</option>
										<option value="S">Supply Notice</option>
										<option value="L">Letter of Agreement</option>
										<option value="X">IGX Sell</option>
										<%-- <%for(int i=0;i<VPURCHASE_DISPLAY_SEGMENT_TYP.size();i++){ %>
										<option value="<%=VPURCHASE_DISPLAY_SEGMENT_TYP.elementAt(i)%>"><%=VPURCHASE_DISPLAY_SEGMENT.elementAt(i)%></option>
										<%} %>
										<%for(int j=0;j<VSELL_TEMP_SEGMENT_TYPE.size();j++){ %>
										<option value="<%=VSELL_TEMP_SEGMENT_TYPE.elementAt(j)%>"><%=VSELL_TEMP_SEGMENT.elementAt(j) %></option>
										<%} %> --%>
									</select>
									<script>document.forms[0].segmentType.value="<%=segmentType%>"</script>
								</div>
							</div>
						</div>
						<div class="col-auto">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--All--</option>
										<%for(int i=0;i<VPURCHASE_MST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VPURCHASE_MST_COUNTERPARTY_CD.elementAt(i)%>"><%=VPURCHASE_MST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VPURCHASE_MST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
										<%for(int i=0;i<VSELL_MST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VSELL_MST_COUNTERPARTY_CD.elementAt(i)%>"><%=VSELL_MST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VSELL_MST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col">  
							<div class="form-group row">
								<div class="float-right" align="right">
									<input style="vertical-align: center" type="checkbox" name="LDE_Checkbox" id="LDE_CheckBox" onclick="lateDealEntry();" onchange="refresh();" <%if(ld_status.equals("Late Deal Entry")){ %>checked="checked"<%} %>>
									<input type="hidden" name="ld_status" id="ld_status" value="">
									<script>document.forms[0].ld_status.value="<%=ld_status%>"</script>
									<label class="form-label"><b>Late Deal Entry</b></label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="left"><%=utilmsg.infoMessage("<b>Deals Done before "+cut_off_time+" HRS:MINS Of Local Time Should be Entered into FMS-NG by Midnight Local Time. <br><i class='fa fa-info-circle fa-lg'></i> Deals Done after "+cut_off_time+" HRS:MINS of Local Time to be Entered No Later Than 12 Noon of Following Business Day!!</b>") %></div>
					</div>
				</div>
				<%if(segmentType.equals("0")||segmentType.equals("D")||segmentType.equals("I")||segmentType.equals("N")){ %>
				<div class="card-body cdbody">
					<div class="accordion">
						<div class="accordion-item accor_item">
							<h2 class="accordion-header" id="heading1">
 								<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse1" aria-expanded="false" aria-controls="collapse1">Purchase</button>	
					    	</h2>
							<div id="collapse1" class="accordion-collapse collapse" aria-labelledby="heading1">
					      		<div class="accordion-body accor-body">
									<%int i=0;int k=0;
									for(int j=0; j<VPURCHASE_TEMP_SEGMENT_TYPE.size(); j++){ 
										int index = Integer.parseInt(""+VPURCHASE_INDEX.elementAt(j));
										String seg_type="";
										if(VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(j).equals("D")){
											seg_type="Domestic NG Purchase";
										}
										else if(VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(j).equals("I"))
										{
											seg_type="IGX Purchase";
										}
										else if(VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(j).equals("N"))
										{
											seg_type="LNG Cargo Purchase";
										}
									if(j!=0)
									{
									%>
									<div class="row">
										<div class="col-sm-12 col-xs-12 col-md-12">
										&nbsp;
										</div>
									</div> 
									<%} %>
									<div class="row m-b-5">
										<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=seg_type%></label>
									</div>
									<div class="row">
										<div class="table-responsive">
											<table id="pur_table" name="pur_table" class="table table-bordered table-hover">
												<thead>
													<tr>
														<th>Sr#</th>
														<th>Customer Name</th>
														<th>Deal No.</th>
														<th><%if(VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(j).equals("N")){%>Cargo Period<%}else{%>Contract Period<%} %></th>
														<th>TCQ<br>(MMBTU)</th>
														<th>DCQ<br>(MMBTU)</th>
														<th>Deal Done Date</th>
														<th>Deal Entry Date</th>
														<th>Entered By</th>
														<th>Late Deal Status</th>
													</tr>
												</thead>
												<tbody>
												<%k=0;int x=0;
												if(index > 0){ %>
													<%
													for(i=i;i<VPURCHASE_COUNTERPARTY_CD.size(); i++){ 
														k+=1;
													%>
													<tr <%if(!ld_status.equals("0")) {%> <%if(VPURCHASE_LATE_DEAL_FLAG.elementAt(i).equals("")){%>hidden="hidden"<%} %> <%} %>id="pur_row<%=i%>" name="pur_row">
														<td align="center">
															<%if(!ld_status.equals("0")) 
															{
																if(!VPURCHASE_LATE_DEAL_FLAG.elementAt(i).equals(""))
																{
																x+=1;
																
																%><%=x %>
																<%}
															}else{%><%=k%><%} %>
														</td>
														<td><%=VPURCHASE_COUNTERPARTY_ABBR.elementAt(i) %> - <%=VPURCHASE_COUNTERPARTY_NM.elementAt(i) %></td>
														<td align="center"><%if(!VPURCHASE_TEMP_SEGMENT_TYPE.elementAt(j).equals("N")){%><%=VPURCHASE_CONTRACT_TYPE.elementAt(i)%><%}%><%=VPURCHASE_CONT_NO.elementAt(i)%></td>
														<td align="center"><%=VPURCHASE_START_DT.elementAt(i)%> - <%=VPURCHASE_END_DT.elementAt(i)%></td>
														<td align="right"><%=VPURCHASE_TCQ.elementAt(i) %></td>
														<td align="right"><%=VPURCHASE_DCQ.elementAt(i) %></td>
														<td align="center"><%=VPURCHASE_DDA_DT.elementAt(i) %> <%=VPURCHASE_DDA_TIME.elementAt(i) %></td>
														<td align="center"><%=VPURCHASE_ENT_DT.elementAt(i) %></td>
														<td align="center"><%=VPURCHASE_ENT_BY.elementAt(i) %></td>
														<td align="center"<%if(VPURCHASE_LATE_DEAL_FLAG.elementAt(i).equals("Late Deal Entry")){ %> style="color: red"<%} %>><%=VPURCHASE_LATE_DEAL_FLAG.elementAt(i)%></td>
													</tr>
													<%
														if(k==index)
														{
															i=i+1;
															break;
														}%>
												<%} %>
												<%}else{ %>
													<tr>
														<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
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
				</div>
				<%} %>
				<%if(segmentType.equals("0")||segmentType.equals("S")||segmentType.equals("L")||segmentType.equals("X")){ %>
				<div class="card-body cdbody">
					<div class="accordion">
						<div class="accordion-item accor_item">
							<h2 class="accordion-header" id="heading2">
 								<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse2" aria-expanded="false" aria-controls="collapse2">Sell</button>	
					    	</h2>
							<div id="collapse2" class="accordion-collapse collapse" aria-labelledby="heading2">
					      		<div class="accordion-body accor-body">
									<%int l=0;int m=0;int k=0;
									for(int n=0; n<VSELL_TEMP_SEGMENT.size(); n++){ 
										int index = Integer.parseInt(""+VSELL_INDEX.elementAt(n));
									if(n!=0)
									{
									%>
									<div class="row">
										<div class="col-sm-12 col-xs-12 col-md-12">
										&nbsp;
										</div>
									</div> 
									<%} %>
									<div class="row m-b-5">
										<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VSELL_TEMP_SEGMENT.elementAt(n) %></label>
									</div>
									<div class="row">
										<div class="table-responsive">
											<table class="table table-bordered table-hover">
												<thead>
													<tr>
														<th>Sr#</th>
														<th>Customer Name</th>
														<th>Deal No.</th>
														<th>Contract Period</th>
														<th>TCQ<br>(MMBTU)</th>
														<th>DCQ<br>(MMBTU)</th>
														<th>Deal Done Date</th>
														<th>Deal Entry Date</th>
														<th>Entered By</th>
														<th>Late Deal Status</th>
													</tr>
												</thead>
												<tbody>
												<%k=0;int x=0;
												if(index > 0){ %>
													<%for(l=l;l<VSELL_COUNTERPARTY_CD.size(); l++){ 
													k+=1;
													%>
													<tr <%if(!ld_status.equals("0")) {%> <%if(VSELL_LATE_DEAL_FLAG.elementAt(l).equals("")){ %>hidden="hidden"<%} %> <%} %>>
														<td align="center">
															<%if(!ld_status.equals("0")) 
															{
																if(!VSELL_LATE_DEAL_FLAG.elementAt(l).equals(""))
																{
																x+=1;
																
																%><%=x %>
																<%}
															}else{%><%=k%><%} %>
														</td>
														<td><%=VSELL_COUNTERPARTY_ABBR.elementAt(l) %> - <%=VSELL_COUNTERPARTY_NM.elementAt(l) %></td>
														<td align="center"><%=VSELL_DIS_CONT_MAPPING.elementAt(l) %></td>
														<td align="center"><%=VSELL_START_DT.elementAt(l)%> - <%=VSELL_END_DT.elementAt(l)%></td>
														<td align="right"><%=VSELL_TCQ.elementAt(l)%></td>
														<td align="right"><%=VSELL_DCQ.elementAt(l) %></td>
														<td align="center"><%=VSELL_DDA_DT.elementAt(l) %>-<%=VSELL_DDA_TIME.elementAt(l) %></td>
														<td align="center"><%=VSELL_ENT_DT.elementAt(l)%></td>
														<td align="center"><%=VSELL_ENT_BY.elementAt(l)%></td>
														<td align="center" <%if(VSELL_LATE_DEAL_FLAG.elementAt(l).equals("Late Deal Entry")){ %> style="color: red"<%} %>><%=VSELL_LATE_DEAL_FLAG.elementAt(l)%></td>
													</tr>
													<%if(k==index)
													{
														l=l+1;
														break;
													}%>
												<%} %>
												<%}else{ %>
													<tr>
														<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
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
				</div>
			<%} %>
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
</html>