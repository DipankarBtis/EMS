<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20230504 : Added Import/Export/Sample File Functionality-->
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

<script>

function refresh()
{
	var year = document.forms[0].year.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_holiday_mst.jsp?year="+year+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
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

function doSubmit()
{
	var flag = true;
	var msg = "";
	var holiday_dt = document.forms[0].holiday_dt.value
	var holiday_day = document.forms[0].holiday_day.value
	var holiday_nm = document.forms[0].holiday_nm.value
	var status = document.forms[0].status_flag.value
	var state = document.forms[0].state_cd.value
	
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
	if(holiday_day == "" || holiday_day == " " || holiday_day == null)
	{
		msg+="\nPlease Enter Holiday Day!!";
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
	
	else{
	
		if(state == "" || state == "00")
		{
			var state_conf = confirm("There is no State selected for : "+holiday_nm+"\n\nDo you want to Submit "+holiday_nm+" as National Holiday ???");
			
			if(state_conf)
			{
				document.forms[0].submit();
			}
		}		
		else if(flag)
		{
			var a = confirm("Holiday Dt : "+holiday_dt+"\nHoliday Name : "+holiday_nm+""+status+"\n\nDo you want to Submit Holiday Details ???");
			
			if(a)
			{
				document.forms[0].submit();
			}
		}	
	}
}

function modify(index,VHOLIDAY_DT,VHOLIDAY_DAY,VHOLIDAY_NM,VHOLI_STATE_CD,VHOLIDAY_FLAG)
{
	document.forms[0].holiday_dt.value=VHOLIDAY_DT
	document.forms[0].holiday_dt.readOnly = true;
	document.forms[0].holiday_dt.style.pointerEvents = "none";
	document.forms[0].holiday_day.value=VHOLIDAY_DAY
	if(VHOLIDAY_DAY != "")
	{
		document.forms[0].holiday_day.style.pointerEvents = "none";
	}
	document.forms[0].holiday_nm.value=VHOLIDAY_NM
	document.forms[0].state_cd.value=VHOLI_STATE_CD
	
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
	
	/* var rdindex = document.forms[0].rdindex.value;
	if(rdindex != "")
	{
		document.getElementById("r"+rdindex).style.background="#EDEDED";
	}
	
	document.forms[0].rdindex.value=index;
	document.getElementById("r"+index).style.background="#ffff80"; */
}

function doClear()
{
	document.forms[0].holiday_dt.value="";
	document.forms[0].holiday_day.value="";
	document.forms[0].holiday_nm.value="";
	document.forms[0].state_cd.value="";
	
	document.forms[0].holiday_dt.readOnly = false;
	document.forms[0].holiday_dt.style.pointerEvents = "auto";
	document.forms[0].holiday_day.style.pointerEvents = "auto";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status_flag").value="Y";
}

function getDayName()
{
	var dt = document.forms[0].holiday_dt.value;
	if(dt != "")
	{
	   	var split = dt.split("/");
		var d_dt = split[0];
		var m_dt = split[1];
		var y_dt = split[2];
		var dt1 = new Date(y_dt+"-"+m_dt+"-"+d_dt);
		
		var weekdays = new Array(7);
        weekdays[0] = "Sunday";
        weekdays[1] = "Monday";
        weekdays[2] = "Tuesday";
        weekdays[3] = "Wednesday";
        weekdays[4] = "Thursday";
        weekdays[5] = "Friday";
        weekdays[6] = "Saturday";
        var r = weekdays[dt1.getDay()];
        document.forms[0].holiday_day.value = r;
        document.forms[0].holiday_day.style.pointerEvents = "none";
    }
	else
	{
		document.forms[0].holiday_day.value = "";
	}
}

function exportToXls()
{
	var year = document.forms[0].year.value;
	
	var url = "xls_holiday_mst.jsp?fileName=Holiday_Master.xls&year="+year;

	location.replace(url);
}

function importData()
{
	  let input = document.createElement('input');
	  input.type = 'file';
	  input.onchange = _ => {
	    // you can use this method to get file and perform respective operations
	            let files =   Array.from(input.files);
	            console.log(files);
	       };
	  //input.click();	  
}

function upload()
{
	var opration = document.forms[0].opration.value;
	
	document.forms[0].opration.value="UPLOAD";
	
	console.log("Upload function worked");
	
	if(file.files.length == 0 )
	{
		console.log("Nooooooo File selected!!!!");
	}else
	{
		console.log("File selected!!!!");
		if(opration)
		{
			document.forms[0].submit();
		}		
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
int currentYear = utildate.getCurrentYear();

String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String operation = request.getParameter("operation")==null?""+currentYear:request.getParameter("operation");


dbmaster.setCallFlag("HOLIDAY_MST");
dbmaster.setYear(year);
dbmaster.init();

Vector VHOLIDAY_DT = dbmaster.getVHOLIDAY_DT();
Vector VHOLIDAY_NM = dbmaster.getVHOLIDAY_NM();
Vector VHOLIDAY_DAY = dbmaster.getVHOLIDAY_DAY();
Vector VHOLIDAY_FLAG = dbmaster.getVHOLIDAY_FLAG();
Vector VHOLI_STATE_CD = dbmaster.getVHOLI_STATE_CD();
Vector VHOLI_STATE_NM = dbmaster.getVHOLI_STATE_NM();

Vector VSTATE_CD =dbmaster.getVSTATE_CD();
Vector VSTATE_NM =dbmaster.getVSTATE_NM();

Vector VYear = dbmaster.getVYear();

%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_master" enctype="multipart/form-data">

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
							Holiday Master
						</div>	
						<!--Harsh Maheta 20230312 : XLS file for Excel Export functionality-->
						<div onclick="exportToXls();" style="color:green; position:absolute;position:absolute; right:102px">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					 	<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!year.equals("")){%>btnactive<%}%>" name="year" onchange="refresh()">
								<%for(int i=0; i<VYear.size(); i++) {%>
									<option value='<%=VYear.elementAt(i)%>'><%=VYear.elementAt(i)%></option>
								<%} %>
							</select>
						</div>
						<script>
							document.forms[0].year.value="<%=year%>"
						</script>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="d-flex justify-content-end">
								<div class="form-group row">
									<div class="col-auto">
										<div class="btn-group">
											<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#myModal" onclick="doClear();">Add New Holiday</label>
										</div>
									</div>
									<%if(write_access.equals("Y")){%>
									<div class="col-auto">
										<div class="btn-group">
											<div class="btn btn-outline-secondary subbtngrp" style="color:green; position:relative;" title="Sample Download" >
											 	<a href="../sample/Sample_Holiday_Calander.xls"><i style="color: blue;" class="fa fa-download"></i>&nbsp;Sample</a>
											</div>
											<div class="btn btn-outline-secondary subbtngrp" onclick="importData();" onchange="upload();" position:relative;" class="inputWrapper" title="Import Holidays" >
												<input class="fileInput" type="file" name="File" id="file" ><i class="fa fa-upload" aria-hidden="true"></i>&nbsp;Upload
											</div>
										</div>
									</div>
									<%}%>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>
										<th>Holiday Date</th>
										<th>
											Holiday Name
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Holiday Name" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>	
										</th>
										<th>Holiday Day</th>
										<th>
											State
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_State" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
								<%if(VHOLIDAY_DT.size() > 0){ %>
									<%for(int i=0; i<VHOLIDAY_DT.size(); i++){ %>
									<tr id="r<%=i%>">
										<td width="5%" align="center">
											<div align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" 
													data-bs-toggle="modal" data-bs-target="#myModal" 
													onclick="modify('<%=i%>','<%=VHOLIDAY_DT.elementAt(i)%>','<%=VHOLIDAY_DAY.elementAt(i)%>','<%=VHOLIDAY_NM.elementAt(i)%>','<%=VHOLI_STATE_CD.elementAt(i)%>','<%=VHOLIDAY_FLAG.elementAt(i)%>')"></i>
												</font>
											</div>
										</td>
										<td align="center"><%=VHOLIDAY_DT.elementAt(i) %></td>
										<td align="center"><%=VHOLIDAY_NM.elementAt(i) %></td>
										<td align="center"><%=VHOLIDAY_DAY.elementAt(i) %></td>
										<td align="center"><%=VHOLI_STATE_NM.elementAt(i)%></td>
										<td align="center">
											<div align="center">
												<font style="color:<%if(VHOLIDAY_FLAG.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VHOLIDAY_FLAG.elementAt(i).equals("Y")){%>
												Active
												<%}else{ %>
												Cancelled
												<%} %>
											</div>
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="6" align="center">
											<%=utilmsg.infoMessage("<b>Holiday List is not Available!</b>")%>
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
							<label class="form-label"><b>State</b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<select class="form-select form-select-sm" name="state_cd">
					    				<option value="00" selected="selected" >--Select--</option>
					    				<%for(int i=0;i<VSTATE_CD.size(); i++){ %>
										<option value="<%=VSTATE_CD.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
										<%} %>
					    			</select>
					    		</div>
				  			</div>
						</div>
					</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="holiday_dt" id="holiday_dt" maxLength="10" 
			      						onblur="validateDate(this);getDayName();" 
			      						onchange="validateDate(this);getDayName();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
	      						</div>
	      						<div class="col">
	      							<select class="form-select form-select-sm" name="holiday_day" id="holiday_day">
					    				<option value="">--Select--</option>
					    				<option value="Monday">Monday</option>
					    				<option value="Tuesday">Tuesday</option>
					    				<option value="Wednesday">Wednesday</option>
					    				<option value="Thursday">Thursday</option>
					    				<option value="Friday">Friday</option>
					    				<option value="Saturday">Saturday</option>
					    				<option value="Sunday">Sunday</option>
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

<input type="hidden" name="option" id="option" value="HOLIDAY_DTL">
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

<script>
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
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