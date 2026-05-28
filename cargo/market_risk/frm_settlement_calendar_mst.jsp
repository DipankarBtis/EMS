<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20230613 : Developed Form for Settlement Calendar-->
<head>
<style>
.inputWrapper {
    height: 32px;
    width: 32px;
    overflow: hidden;
    position: relative;
    cursor: pointer;
    /*Using a background color, but you can use a background image to represent a button*/
    /* background-color: #DDF; */
}
.fileInput {
    cursor: pointer;
    height: 100%;
    width: 75px;
    position:absolute;
    top: 0;
    right: 0;
    z-index: 99;
    /*This makes the button huge. If you want a bigger button, increase the font size*/
    font-size:50px;
    /*Opacity settings for all browsers*/
    opacity: 0;
    -moz-opacity: 0;
    filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0)
}
</style>

<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh()
{
	var settle_curve = document.forms[0].settle_curve.value;
	var year = document.forms[0].year.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_settlement_calendar_mst.jsp?settle_curve="+settle_curve+"&year="+year+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function importData()
{
  let input = document.createElement('input');
  input.type = 'file';
  input.onchange = _ => 
  {
    // you can use this method to get file and perform respective operations
            let files =   Array.from(input.files);
  };
}

function upload()
{
	var opration = document.forms[0].opration.value;
	
	document.forms[0].opration.value="UPLOAD";
	
	if(file.files.length == 0 )
	{
	}else
	{
		if(opration)
		{
			var file_nm = document.getElementById('file');
			if(file_nm.files.item(0).name.includes(".xls") || file_nm.files.item(0).name.includes(".xlsx"))
			{
				var upload = confirm("Do you want to upload "+file_nm.files.item(0).name+" ?")
				if(upload==true)
				{
					document.forms[0].submit();
				}
				else
				{
					alert("Uploading Cancelled!!");
				}
			}
			else
			{
				var rejact = alert("Uploading file should be in .xls or .xlsx format!!!!")
			}
		}		
	}
}
function exportToXls()
{
	var settle_curve = document.forms[0].settle_curve.value;
	var year = document.forms[0].year.value;
	
	var url = "xls_settlement_calendar_mst.jsp?fileName=Settlement Calendar.xls&settle_curve="+settle_curve+"&year="+year;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
int currentYear = utildate.getCurrentYear();

String settle_curve = request.getParameter("settle_curve")==null?"0":request.getParameter("settle_curve");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");

dbmarket.setCallFlag("SETTLEMENT_MST");
dbmarket.setSettle_curve(settle_curve);
dbmarket.setYear(year);
dbmarket.init();

Vector VCONT_MONTH = dbmarket.getVCONT_MONTH(); 
Vector VCURVE_NM = dbmarket.getVCURVE_NM(); 
Vector VSETTLE_START_DT = dbmarket.getVSETTLE_START_DT();
Vector VSETTLE_END_DT = dbmarket.getVSETTLE_END_DT();
Vector VSETTLE_DT = dbmarket.getVSETTLE_DT();
Vector VSETTLE_CURVE = dbmarket.getVSETTLE_CURVE();
Vector VINDEX = dbmarket.getVINDEX();
Vector VSUB_INDEX = dbmarket.getVSUB_INDEX();
Vector VSETTLE_TYPE = dbmarket.getVSETTLE_TYPE();
Vector VYEAR = dbmarket.getVYEAR();
Vector VTEMP_YEAR = dbmarket.getVTEMP_YEAR();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_MarketRisk" enctype="multipart/form-data">
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
							Price Curve Settlement Calendar
						</div>	
						<div align="right" onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="d-flex justify-content-end">
							<%if(write_access.equals("Y")){%>
								<div class="col-auto">
									<div class="btn-group">
										<div class="btn btn-outline-secondary subbtngrp" title="Sample Download" >
										 	<a href="../sample/Sample_Settlement_Calendar_CurveNm.xlsx"><i style="color: blue;" class="fa fa-download"></i>&nbsp;Sample</a>
										</div>
										<div class="btn btn-outline-secondary subbtngrp" onclick="importData();" onchange="upload();" class="inputWrapper" title="Import Settlement" >
											<input class="fileInput" type="file" name="File" id="file" ><i class="fa fa-upload" aria-hidden="true"></i>&nbsp;Upload
										</div>
									</div>
								</div>
								<%}%>
							</div>
						</div>
					</div>	
					<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2" align="left">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Year</b></label>&nbsp;
								</div>
								<div class="col">
				      				<div class="btn-group" >
										<select class="form-select form-select-sm" name="year" onchange="refresh()">
											<option value="0">--Select--</option>
											<%for(int i=0; i<VYEAR.size(); i++) {%>
												<option value='<%=VYEAR.elementAt(i)%>'><%=VYEAR.elementAt(i)%></option>
											<%} %>
										</select>
									</div>
									<script>
										document.forms[0].year.value="<%=year%>"
									</script>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2" align="left">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Price Curve</b></label>&nbsp;
								</div>
								<div class="col">
									<div class="btn-group">
										<select class="form-select form-select-sm" name="settle_curve" onchange="refresh()">
											<option value="0">--Select--</option>
											<%for(int i=0; i<VSETTLE_TYPE.size(); i++) {%>
												<option value='<%=VSETTLE_TYPE.elementAt(i)%>'><%=VSETTLE_TYPE.elementAt(i)%></option>
											<%}%>
										</select>
									</div>
									<script>
										document.forms[0].settle_curve.value="<%=settle_curve%>"
									</script>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%int j=0,k=0,l=0,m=0;
					if(VCURVE_NM.size()>0){
					for(int i=0; i<VCURVE_NM.size(); i++)
					{ 
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
						%>						
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=l%>">
	   										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    			<%=VCURVE_NM.elementAt(i)%>
								      		</button>	
								    	</h2>
								    	
										<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
								        		<%if(VCONT_MONTH.size()>0){ %>
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<!-- <th></th> -->
																	<th>Contract Month</th>
																	<th>Settle Start Date</th>
																	<th>Settle End Date</th>
																	<th>Settle Date</th>
																	<th>Price Curve</th>
																</tr>
															</thead>
															<tbody>
																<%k=0; for(l=l; l<VCONT_MONTH.size(); l++)
																{
																	k+=1;
																%>
																	<tr id="r<%=i%>">
																		<td align="center"><%=VCONT_MONTH.elementAt(l) %></td>
																		<td align="center"><%=VSETTLE_START_DT.elementAt(l) %></td>
																		<td align="center"><%=VSETTLE_END_DT.elementAt(l) %></td>
																		<td align="center"><%=VSETTLE_DT.elementAt(l)%></td>
																		<td align="center"><%=VCURVE_NM.elementAt(i)%></td>
																	</tr>
																	<%if(k==index){
																		l=l+1;
																		break;} %>
																<%} %>
															</tbody>
														</table>
													</div>
													<%}else{ %>
														<div align="center"><%=utilmsg.infoMessage("<b>No Contract available at Selected Year</b>")%></div>
													<%} %>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					<%}%>
					<%}else{ %>
						<div align="center"><%=utilmsg.infoMessage("<b>No Curve available at Selected Year</b>")%></div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" id="option" value="SETTLEMENT_DTL">
<input type="hidden" name="opration" value="INSERT" id="opration">
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