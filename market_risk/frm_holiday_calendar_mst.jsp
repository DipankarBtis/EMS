<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20230615 : Developed Form for Holiday Calendar-->
<head>
<style>
.inputWrapper {
    height: 32px;
    width: 32px;
    overflow: hidden;
    position: relative;
    cursor: pointer;
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
	var holiday_type = document.forms[0].holiday_type.value;
	var year = document.forms[0].year.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_holiday_calendar_mst.jsp?holiday_type="+holiday_type+"&year="+year+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var flag = true;
	var msg = "";
	var holiday_dt = document.forms[0].holiday_dt.value
	var holiday_nm = document.forms[0].holiday_nm.value
	var status = document.forms[0].status_flag.value
	
	document.forms[0].opration.value="INSERT";
	
	if(status == "Y")
	{
		status="";
	}
	else
	{
		status="\nStatus : Cancelled";
	}
	
	if(holiday_dt == "" || holiday_dt == " " || holiday_dt == null)
	{
		msg="Please Select Holiday Date!!";
		flag=false;
	}
	if(holiday_nm == "" || holiday_nm == " " || holiday_nm == null)
	{
		msg+="\nPlease Enter Holiday Name!!";
		flag=false;
	}
	
	if(msg!=""){
		alert(msg);
	}
	
	else
	{
		if(flag)
		{
			var a = confirm("Holiday Dt : "+holiday_dt+"\nHoliday Name : "+holiday_nm+""+status+"\n\nDo you want to Submit Holiday Details ???");
			
			if(a)
			{
				document.forms[0].submit();
			}
		}	
	}
	document.forms[0].curve_nm.readOnly = false;
	document.forms[0].curve_nm.disabled = false;
	document.forms[0].curve_nm.style.pointerEvents = "";
}

function modify(index,VHOLIDAY_DT,VHOLIDAY_NM,VCURVE_NM,VHOLIDAY_FLAG)
{
	document.forms[0].holiday_dt.value=VHOLIDAY_DT
	document.forms[0].holiday_dt.readOnly = true;
	document.forms[0].holiday_dt.style.pointerEvents = "none";
	document.forms[0].holiday_nm.value=VHOLIDAY_NM
	document.forms[0].curve_nm.value=VCURVE_NM
	document.forms[0].curve_hidd_nm.value=VCURVE_NM
	document.forms[0].curve_nm.readOnly = true;
	document.forms[0].curve_nm.disabled = true;
	document.forms[0].curve_nm.style.pointerEvents = "none";
	
	if(VHOLIDAY_FLAG=='Y')
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="Cancel";
		document.getElementById("status_flag").value="N";
	}
}

function doClear()
{
	document.forms[0].holiday_dt.value="";
	document.forms[0].holiday_nm.value="";
	document.forms[0].curve_nm.value="";
	
	document.forms[0].holiday_dt.readOnly = false;
	document.forms[0].holiday_dt.style.pointerEvents = "auto";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status_flag").value="Y";
}

function setActiveInactive(obj)
{
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.getElementById("lb").innerHTML="Cancel";
		document.getElementById("status_flag").value="N";
	}
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
	var holiday_type = document.forms[0].holiday_type.value;
	var year = document.forms[0].year.value;
	
	var url = "xls_holiday_calendar_mst.jsp?fileName=Holiday Calendar.xls&holiday_type="+holiday_type+"&year="+year;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String curr_year=""+utildate.getCurrentYear();
String holiday_type = request.getParameter("holiday_type")==null?"0":request.getParameter("holiday_type");
String year=request.getParameter("year")==null?curr_year:request.getParameter("year");

dbmarket.setCallFlag("HOLIDAY_CLAND_MST");
dbmarket.setHoliday_type(holiday_type);
dbmarket.setYear(year);
dbmarket.init();

Vector VSETTLE_TYPE = dbmarket.getVSETTLE_TYPE();
Vector VHOLIDAY_NM = dbmarket.getVHOLIDAY_NM();
Vector VHOLIDAY_DT = dbmarket.getVHOLIDAY_DT();
Vector VHOLIDAY_STATUS = dbmarket.getVHOLIDAY_STATUS();
Vector VCURVE_NM = dbmarket.getVCURVE_NM();
Vector VINDEX = dbmarket.getVINDEX();
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
							Price Curve Holiday Calendar
						</div>	
						<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="d-flex justify-content-end">
								<div class="form-group row">
								<%if(write_access.equals("Y")){%>
									<div class="col-auto">
										<div class="btn-group">
											<div class="btn btn-outline-secondary subbtngrp" style="color:green; position:relative;" title="Sample Download" >
											 	<a href="../sample/Sample_Holiday_Calendar_CurveNm.xlsx"><i style="color: blue;" class="fa fa-download"></i>&nbsp;Sample</a>
											</div>
											<div class="btn btn-outline-secondary subbtngrp" onclick="importData();" onchange="upload();" position:relative;" class="inputWrapper" title="Import Holiday Calendar" >
												<input class="fileInput" type="file" name="File" id="file" ><i class="fa fa-upload" aria-hidden="true"></i>&nbsp;Upload
											</div>
										</div>
									</div>
									<%}%>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Year</b></label>
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
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Price Curve</b></label>
								</div>
								<div class="col">
									<div class="btn-group">
										<select class="form-select form-select-sm" name="holiday_type" onchange="refresh()">
											<option value="0">--All--</option>
											<%for(int i=0; i<VSETTLE_TYPE.size(); i++) {%>
												<option value='<%=VSETTLE_TYPE.elementAt(i)%>'><%=VSETTLE_TYPE.elementAt(i)%></option>
											<%}%>
										</select>
									</div>
								</div>
								<script>
									document.forms[0].holiday_type.value="<%=holiday_type%>"
								</script>
							</div>	
						</div>
						
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="col">
						<div class="d-flex flex-row-reverse bd-highlight">
							<div class="p-2 bd-highlight">
								<div class="btn-group">
									<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#myModal" onclick="doClear();">Add Holiday</label>
								</div>
							</div>
						</div>
					</div>
					<%if(VCURVE_NM.size()>0){ %>
					<%int j=0,k=0,l=0,m=0;
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
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th></th>
																	<th>Holiday Name</th>
																	<th>Holiday Date</th>
																	<th>Curve Name</th>
																	<th>Holiday Status</th>
																</tr>
															</thead>
															<tbody>
															<%k=0; for(l=l; l<VHOLIDAY_DT.size(); l++)
															{
																k+=1;
															%>
																<tr id="r<%=i%>">
																	<td width="5%" align="center">
																		<div align="center">
																			<font title="Click to Edit" style="color:var(--header_color)">
																				<i class="fa fa-edit fa-lg" 
																				data-bs-toggle="modal" data-bs-target="#myModal" 
																				onclick="modify('<%=l%>','<%=VHOLIDAY_DT.elementAt(l)%>','<%=VHOLIDAY_NM.elementAt(l)%>','<%=VCURVE_NM.elementAt(i)%>','<%=VHOLIDAY_STATUS.elementAt(l)%>')"></i>
																			</font>
																		</div>
																	</td>
																	<td align="center"><%=VHOLIDAY_NM.elementAt(l) %></td>
																	<td align="center"><%=VHOLIDAY_DT.elementAt(l) %></td>
																	<td align="center"><%=VCURVE_NM.elementAt(i) %></td>
																	<td align="center">
																		<div align="center">
																			<font style="color:<%if(VHOLIDAY_STATUS.elementAt(l).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
																				<i class="fa fa-circle fa-lg" ></i>
																				&nbsp;
																			</font>
																			<%if(VHOLIDAY_STATUS.elementAt(l).equals("Y")){%>
																			Active
																			<%}else{ %>
																			Cancelled
																			<%} %>
																		</div>
																	</td>
																</tr>
																<%if(k==index){
																	l=l+1;
																	break;} %>
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
						</div>
					<%}%>
				<%}else{ %>
					&nbsp;
					<div align="center"><%=utilmsg.infoMessage("<b>Holiday Calendar is not Available!</b>")%></div>
				<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- MODEL -->
<div class="modal fade" id="myModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Holiday
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Holiday Name<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="holiday_nm" id="holiday_nm" maxlength="100">
					    		</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Holiday Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="holiday_dt" id="holiday_dt" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
	      						</div>
				    		</div>
						</div>
					</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Price Curve</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<select class="form-select form-select-sm" name="curve_nm">
					    				<option value="" selected="selected" >--Select--</option>
					    				<%for(int i=0;i<VSETTLE_TYPE.size(); i++){ %>
										<option value="<%=VSETTLE_TYPE.elementAt(i)%>"><%=VSETTLE_TYPE.elementAt(i)%></option>
										<%} %>
					    			</select>
					    		</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Status</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="form-check form-switch">
										<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="setActiveInactive(this);">
									  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
									  		Active
									  	</label>
									  	<input type="hidden" name="status_flag" id="status_flag" value="Y">
									</div>
				    			</div>
				  			</div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
					<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
      		</div>
      	</div>
   </div>
</div>

<input type="hidden" name="option" id="option" value="HOLIDAY_CLAND_DTL">
<input type="hidden" name="curve_hidd_nm" value="">
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