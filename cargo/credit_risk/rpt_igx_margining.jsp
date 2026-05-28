<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script src="../js/chart_js/chart.js"></script>
<script>

function refresh()
{
	var from_dt = document.forms[0].from_dt.value;
	var bg_value = document.forms[0].bg_value.value;
	var bg_validity = document.forms[0].bg_validity.value;
	var bg_ref = document.forms[0].bg_ref.value;

	var u = document.forms[0].u.value;
	
		var url = "rpt_igx_margining.jsp?u="+u+"&from_dt="+from_dt+"&bg_value="+bg_value+"&bg_validity="+bg_validity+"&bg_ref="+bg_ref;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function showTableRow1()
{
	var img_val = document.getElementById("image").className;
	if(img_val == "fa fa-expand")
	{
		var ele = document.getElementsByClassName("trhide");
		for (var i = 0; i < ele.length; i++)
		{
			ele[i].style.display = "table-row-group";
		}
		document.getElementById("image").className="fa fa-compress";
	}
	else
	{
		var ele = document.getElementsByClassName("trhide");
		for (var i = 0; i < ele.length; i++)
		{
        	ele[i].style.display = "none";
		}
		document.getElementById("image").className='fa fa-expand';
	}	
}
function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var bg_value = document.forms[0].bg_value.value;
	var bg_validity = document.forms[0].bg_validity.value;
	var bg_ref = document.forms[0].bg_ref.value;
	
	var url = "xls_igx_margining.jsp?fileName=IGX Margining Report "+from_dt+".xls&from_dt="+from_dt+
			"&bg_value="+bg_value+"&bg_validity="+bg_validity+"&bg_ref="+bg_ref;

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ExposureTracking" id="cr_report" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String prev_date=utildate.getPreviousDate();
String from_dt = request.getParameter("from_dt")==null?prev_date:request.getParameter("from_dt");

cr_report.setCallFlag("IGX_MARGIN");
cr_report.setComp_cd(owner_cd);
cr_report.setFrom_dt(from_dt);
cr_report.init();

Vector VSTART_DT = cr_report.getVSTART_DT();
Vector VEND_DT = cr_report.getVEND_DT();
Vector VCONTRACT_TYPE = cr_report.getVCONTRACT_TYPE();
Vector VCOUNTERPARTY_CD = cr_report.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM  = cr_report.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = cr_report.getVCOUNTERPARTY_ABBR();
Vector VTCQ = cr_report.getVTCQ();
Vector VDCQ = cr_report.getVDCQ();
Vector VCONT_REF_NO = cr_report.getVCONT_REF_NO();
Vector VTRADE_REF_NO = cr_report.getVTRADE_REF_NO();
Vector VSIGNING_DT = cr_report.getVSIGNING_DT();
Vector VRATE = cr_report.getVRATE();
Vector VRATE_UNIT = cr_report.getVRATE_UNIT();
Vector VFCC_BY = cr_report.getVFCC_BY();
Vector VFCC_DT = cr_report.getVFCC_DT();
Vector VENT_BY = cr_report.getVENT_BY();
Vector VENT_DT = cr_report.getVENT_DT();
Vector VAGMT_BASE = cr_report.getVAGMT_BASE();
Vector VPOST_MARGIN = cr_report.getVPOST_MARGIN();
Vector VDISPLAY_DEAL_MAP = cr_report.getVDISPLAY_DEAL_MAP();
Vector VACCOUNT = cr_report.getVACCOUNT();
Vector VBG_DROPOFF_DT = cr_report.getVBG_DROPOFF_DT();
Vector VTRADE_VALUE = cr_report.getVTRADE_VALUE();
Vector VPOST_TRADE_MARGIN = cr_report.getVPOST_TRADE_MARGIN();
Vector VPRICE_TYPE = cr_report.getVPRICE_TYPE();
Vector VTAX = cr_report.getVTAX();
Vector VTEMP_REPORT_DT = cr_report.getVTEMP_REPORT_DT();
Vector VMARGIN_AVAIL = cr_report.getVMARGIN_AVAIL();
Vector VMARGIN_USED = cr_report.getVMARGIN_USED();
Vector VBG_VALUE = cr_report.getVBG_VALUE();
Vector VTEMP_MARGIN_USED = cr_report.getVTEMP_MARGIN_USED();

//HashMap<String, String> IGXDealNo = cr_report.getIGXDealNo();
HashMap<String, HashMap> HIGX_MARGIN_USED = cr_report.getHIGX_MARGIN_USED();
HashMap<String, HashMap> HIGX_BUYSELL = cr_report.getHIGX_BUYSELL();
HashMap<String, HashMap> HCOLOR = cr_report.getHCOLOR();
HashMap<String, String> HIGX_DEAL_NO = cr_report.getHIGX_DEAL_NO();

String bg_validity = cr_report.getBg_validity();
String bg_ref_no = cr_report.getBg_ref_no();
String bg_value = cr_report.getBg_value();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="">
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
				    		IGX Margining Report 
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Report Date :</b></label>
									</div>
									<div class="col-auto">
					      				<div class="input-group input-group-sm" >
				      				   		<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" onchange="refresh()" maxLength="10" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      						<input type="hidden" name="report_dt" >
			      						</div>
					    			</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-1 col-xs-1 col-md-1">
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">
							<label class="form-label"><b>BG reference</b></label>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="bg_ref" value="<%=bg_ref_no%>" readonly>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-1 col-xs-1 col-md-1">
							<label class="form-label"><b>BG Validity</b></label>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="bg_validity" value="<%=bg_validity%>" readonly>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-1 col-xs-1 col-md-1">
							<label class="form-label"><b>BG Value</b></label>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="bg_value" value="<%=bg_value%>" readonly>
		      						</div>
				    			</div>
				    		</div>
				    	</div>
					</div>&nbsp;
					<div class="table-responsive">
						<table class="table table-bordered">
							<tr>
								<td>
									<div align="center">
										<canvas id="myChart" style="width:100%;max-width:1000px"></canvas>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th></th>
									<th colspan="2" ><b>Date</b></th>
									<%for(int i=0; i<VTEMP_REPORT_DT.size(); i++){ %>
										<th  <%if(VTEMP_REPORT_DT.elementAt(i).equals(from_dt)){ %>style="background:#000066;color:white;"<%} %>><b><%=VTEMP_REPORT_DT.elementAt(i) %></b></th>
									<%} %>
								</tr>
							</thead>
							<tbody>
								<tr>	
									<td></td>
									<td colspan="2" style="color:var(--tb_header_font_color);"><b>BG Value</b></td>
									<%for(int i=0; i<VBG_VALUE.size(); i++){ %>
										<td <%if(VTEMP_REPORT_DT.elementAt(i).equals(from_dt)){ %>style="background:#a6ff4d;"<%} %>><%=VBG_VALUE.elementAt(i) %></td>
									<%} %>
								</tr>
								<tr>
									<td>
										<input type="hidden" name="img_val1" id="img_val1" value="D">
										<i style="vertical-align:bottom; middle;" id="image" class="fa fa-expand" onclick="showTableRow1()"></i>
									</td>
									
									<td colspan="2" style="color:var(--tb_header_font_color);"><b>Margin Used</b><br><span style="color:#000066;font-size:10px;text-transform: none;">*upto end of Exposure period</span>
									</td>
									<%for(int i=0; i<VMARGIN_USED.size(); i++){ %>
										<td <%if(VTEMP_REPORT_DT.elementAt(i).equals(from_dt)){ %>style="background:#a6ff4d;"<%} %>><%=VMARGIN_USED.elementAt(i) %></td>
									<%} %>
								</tr>
								<tbody class="trhide" id="trhide" style="display:none;">
								<%
								Iterator o = HIGX_BUYSELL.keySet().iterator();
								int rowc = 0;
								while (o.hasNext()) 
							    { 
								    String key = (String) o.next();
								    HashMap DealNo = HIGX_BUYSELL.get(key);
									Iterator o1 = DealNo.keySet().iterator();
									String rowcolor2= "";
									while (o1.hasNext()) 
									{
										String keyNm1 = (String) o1.next();
										
										String deal_no = (String) DealNo.get(keyNm1);
									    if(rowc%2==0) {
									    	rowcolor2="#E0EEE0";
										}else{ 
											rowcolor2="";
										}
									    rowc+=1;
									    %>
									    <tr >
									    	<td style="background:#f0f0f5;"></td>
											<td align="left" style="background:#f0f0f5;"><%=HIGX_DEAL_NO.get(keyNm1)%></td>
											<td align="center" style="background:#f0f0f5;"><%=deal_no%></td>	
											<%HashMap info = HIGX_MARGIN_USED.get(key+keyNm1);
											HashMap info_color = HCOLOR.get(key+keyNm1);%>
											<%for(int i=0; i<VTEMP_REPORT_DT.size(); i++){ %>
											<td align="right" style="background:#f0f0f5;color:green;"><font color="<%=info_color.get(""+VTEMP_REPORT_DT.elementAt(i))%>"><%=info.get(""+VTEMP_REPORT_DT.elementAt(i))%></font></td>
											<%} %>
										</tr>
									<%} %>
								<%} %>
								</tbody>
								<tr>
									<td></td>
									<td colspan="2" style="color:var(--tb_header_font_color);"><b>Margin Available</b></td>
									<%for(int i=0; i<VMARGIN_AVAIL.size(); i++){ %>
										<td <%if(VTEMP_REPORT_DT.elementAt(i).equals(from_dt)){ %>style="background:#a6ff4d;"<%} %>><%=VMARGIN_AVAIL.elementAt(i) %></td>
									<%} %>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="col-sm-12 col-xs-12 col-md-12">
						<div class="form-group row">
							<div class="col-auto">
								<label class="topheader">IGX Margining Report(Contract Wise Details)</label>
							</div>
						</div>
					</div>&nbsp;
					<div class="table-responsive">
						<table class="table table-bordered" id="filterbysearch">
							<thead>
								<tr>
									<th>SR#</th>
									<th align="center">Counterparty Name</th>
									<th align="center">Contract#</th>
									<th align="center">Trade Ref#</th>
									<th align="center">Contract Ref#</th>
									<th align="center">Buy/Sell</th>
									<th align="center">Deal Date</th>
									<th align="center">Payment Due Date</th>
									<th align="center">Start Date-End Date</th>
									<th align="center">Price Type</th>
									<th align="center">Sales Rate (INR)</th>
									<th align="center">tax(%)</th>
									<th align="center">Post Trade Margin(%)</th>
									<th align="center">Total Volume(MMBTU)</th>
									<th align="center">BG Dropoff Date</th>
									<th align="center">Trade Value(INR)</th>
									<th align="center">Post Trade Margin(INR)</th>
								</tr>
							</thead>
							<tbody>
							<%if(VCOUNTERPARTY_CD.size()>0){
								int j=0;%>
								<%for(int i=0;i<VCOUNTERPARTY_CD.size(); i++){
								j++;%>
									<tr>
										<td align="center"><%=j %></td>
										<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i) %></td>
										<td align="center"><%=VDISPLAY_DEAL_MAP.elementAt(i) %></td>
										<td align="center"><%=VTRADE_REF_NO.elementAt(i) %></td>
										<td align="center"><%=VCONT_REF_NO.elementAt(i) %></td>
										<td align="center"><%=VACCOUNT.elementAt(i) %></td>
										<td align="center"><%=VSIGNING_DT.elementAt(i) %></td>
										<td align="center">D+2</td>
										<td align="center"><%=VSTART_DT.elementAt(i) %> - <%=VEND_DT.elementAt(i) %></td>
										<td align="center"><%=VPRICE_TYPE.elementAt(i) %></td>
										<td align="right"><%=VRATE.elementAt(i) %></td>
										<td align="center"><%=VTAX.elementAt(i) %></td>
										<td align="center"><%=VPOST_MARGIN.elementAt(i) %></td>
										<td align="right"><%=VTCQ.elementAt(i) %></td>
										<td align="center"><%=VBG_DROPOFF_DT.elementAt(i) %></td>
										<td align="right"><%=VTRADE_VALUE.elementAt(i) %></td>
										<td align="right"><%=VPOST_TRADE_MARGIN.elementAt(i) %></td>
									</tr>
								<%} %>
							<%}else{ %>
								<tr><td colspan="17" align="center"><%=utilmsg.infoMessage("<b>IGX Margining Report Is Not Available for Selected Date!</b>") %></td></tr>
							<%} %>
							</tbody>
						</table>
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
</body>

<script type="text/javascript">

var xValues = [100,200,300,400,500,600,700,800,900,1000];
var xValues = [];
var bg_value = [];
var margin_used = [];
var margin_avl=[];

<%for (int i = 0; i < VTEMP_REPORT_DT.size(); i++) {
	String temp = VTEMP_REPORT_DT.elementAt(i).toString().replaceAll("/", "");
	%>

	xValues.push(<%=temp%>);
	bg_value.push(<%=VBG_VALUE.elementAt(i)%>);
	margin_used.push(<%=VMARGIN_USED.elementAt(i)%>);
	margin_avl.push(<%=VMARGIN_AVAIL.elementAt(i)%>);
	  
	<%}%>

const dataLine = {
type: 'line',
  data: {
    labels: xValues,
    datasets: [{
        label: "BG Value",
        data: bg_value,
        backgroundColor: [
        	'rgba(0, 137, 132, 0.2)',
        	
        ],
        fill: true,
        borderColor: [
        	'rgba(50, 150, 255, 1)',
        ],
        borderWidth: 2,
        tension: 0.4
      },
      {
        label: "Margin Used",
        data: margin_used,
        backgroundColor: [
        	'rgba(105, 0, 132, 0.2)',
        ],
        fill: true,
        borderColor: [
        	'rgba(255, 99, 132, 0.8)',
        ],
        borderWidth: 2,
        tension: 0.4
      },
      {
          label: "Margin Available",
          data: margin_avl,
          backgroundColor: [
            'rgba(204, 255, 238, 0.3)',
          ],
          fill: true,
          borderColor: [
            'rgba(30, 229, 83, 0.8)',
          ],
          borderWidth: 2,
          tension: 0.4
        }
    ]
  },
  options: {
    responsive: true
  }
};

new Chart(document.getElementById('myChart'), dataLine);

</script>

</html>
