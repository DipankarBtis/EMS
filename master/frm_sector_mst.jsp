<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var filter_status = document.forms[0].filter_status.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_sector_mst.jsp?u="+u+"&filter_status="+filter_status;

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
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="N";
	}
}

function doModify(sector_cd,sector_nm,sector_abbr,sector_type,status_flag,eff_dt,demand_sector_cd,supply_sector_cd)
{
	document.forms[0].sector_cd.value=sector_cd;
	document.forms[0].sector_nm.value=sector_nm;
	document.forms[0].sector_abbr.value=sector_abbr;
	document.forms[0].sector_type.value=sector_type;
	document.forms[0].eff_dt.value = eff_dt;
	document.forms[0].demand_sector_cd.value=demand_sector_cd;
	document.forms[0].supply_sector_cd.value =supply_sector_cd;
	document.forms[0].detail_flag.value = "false";
	if(document.forms[0].eff_dt.value=="")
	{
		document.forms[0].add_chk.checked = false; 
		document.forms[0].add_chk.disabled = false;
		document.forms[0].eff_dt.disabled = true; 
		document.forms[0].demand_sector_cd.disabled = true; 
		document.forms[0].supply_sector_cd.disabled = true;
	}
	else 
	{
		document.forms[0].add_chk.checked = true;
		document.forms[0].add_chk.disabled = true;
		document.forms[0].eff_dt.disabled = false; 
		document.forms[0].demand_sector_cd.disabled = false; 
		document.forms[0].supply_sector_cd.disabled = false;
	}
	if(status_flag=='Y')
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="N";
	}
	
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	document.forms[0].sector_cd.value="";
	document.forms[0].sector_nm.value="";
	document.forms[0].sector_abbr.value="";
	document.forms[0].sector_type.value="Y";
	document.forms[0].demand_sector_cd.value="";
	document.forms[0].supply_sector_cd.value="";
	document.forms[0].eff_dt.value="";
	document.forms[0].detail_flag.value = "";
	document.forms[0].active.checked=true;
	document.forms[0].add_chk.checked=false;
	document.forms[0].add_chk.disabled = false;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status_flag").value="Y";
	
	document.forms[0].opration.value="INSERT";
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var sector_nm = document.forms[0].sector_nm;
	var sector_abbr = document.forms[0].sector_abbr;
	var sector_type = document.forms[0].sector_type;
	var sector_cd = document.forms[0].sector_cd;
	var demand_sector_cd = document.forms[0].demand_sector_cd;
	var supply_sector_cd = document.forms[0].supply_sector_cd;
	var eff_dt = document.forms[0].eff_dt;
	var flag = true;
	var msg = "";
	var detail_flag = document.forms[0].detail_flag.value;	
	
	if(opration=="MODIFY")
	{
		if(sector_cd.value == "" || trim(sector_cd.value) == "")
		{
			flag=false;
			msg+="Sector CD Missing!\n";
		}
	}
	if(sector_nm.value == "" || trim(sector_nm.value) == "")
	{
		flag=false;
		msg+="Enter Sector Name!\n";
	}
	if(sector_abbr.value == "" || trim(sector_abbr.value) == "")
	{
		flag=false;
		msg+="Enter Sector ABBR!\n";
	}
	if(sector_type.value == "" || trim(sector_type.value) == "")
	{
		flag=false;
		msg+="Select Sector Type!\n";
	}
	if(detail_flag=="true" || document.forms[0].add_chk.checked===true)
	{
		if(eff_dt.value == "" || trim(eff_dt.value)=="")
		{
			flag=false;
			msg+="Enter Eff Date!\n"
		}
		if(demand_sector_cd.value == "" || trim(demand_sector_cd.value)=="")
		{
			flag=false;
			msg+="Enter Demand Sector Code!\n"
		}
		if(supply_sector_cd.value == "" || trim(supply_sector_cd.value)=="")
		{
			flag=false;
			msg+="Enter Supply Sector Code!\n"
		}
	}
	if(flag)
	{
		var a;
		if(opration=="MODIFY")
		{
			a= confirm("Do you Want to Modify?");
		}
		else
		{
			a= confirm("Do you Want to Submit?");
		}
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}
function hide_show(id1,id2)
{	
	if(document.getElementById(id1).style.display=='none'){
		document.getElementById(id1).style.display='table-row-group';
		document.getElementById(id2).className='fa fa-compress';
	}else{
		document.getElementById(id1).style.display='none';
		document.getElementById(id2).className='fa fa-expand';
	}
} 
function enabledSectorDetails()
{
	var checkBox = document.forms[0].add_chk;
	if(checkBox.checked)
	{
		document.forms[0].detail_flag.value = "true";
		document.forms[0].eff_dt.disabled = false; 
		document.forms[0].demand_sector_cd.disabled = false; 
		document.forms[0].supply_sector_cd.disabled = false;
	}
	else
	{
		document.forms[0].detail_flag.value = "false";
		document.forms[0].eff_dt.disabled = true; 
		document.forms[0].demand_sector_cd.disabled = true; 
		document.forms[0].supply_sector_cd.disabled = true;
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String filter_status = request.getParameter("filter_status")==null?"0":request.getParameter("filter_status");

dbmaster.setCallFlag("SECTOR_MST");
dbmaster.setFilter_status(filter_status);
dbmaster.init();

Vector VSECTOR_CD = dbmaster.getVSECTOR_CD();
Vector VSECTOR_NAME	= dbmaster.getVSECTOR_NAME();
Vector VSECTOR_ABBR = dbmaster.getVSECTOR_ABBR();
Vector VSECTOR_TYPE = dbmaster.getVSECTOR_TYPE();
Vector VSTATUS_FLAG = dbmaster.getVSTATUS_FLAG();
Vector VSECTOR_TYPE_NM = dbmaster.getVSECTOR_TYPE_NM();
Vector VDEMAND_SECTOR_CD = dbmaster.getVDEMAND_SECTOR_CD();
Vector VSUPPLY_SECTOR_CD = dbmaster.getVSUPPLY_SECTOR_CD();
Vector VEFF_DT = dbmaster.getVEFF_DT();
Vector VINDEX = dbmaster.getVINDEX();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_master">

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
					<div class="topheader">
						Energy Sector Master
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#myModal" onclick="doClear();">Add New Sector</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>
										<th>
											Sector Name
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Sector Name" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Sector Abbr
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Sector Abbr" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Sector Type
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Sector Type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Status
											<br>
											<div align="center">
												<select class="form-select form-select-sm" name="filter_status" onchange="refresh();" style="width:100px">
										    		<option value="0">--Select--</option>
										    		<option value="Y">Active</option>
										    		<option value="N">In-Active</option>
										    	</select>
										    	<script>document.forms[0].filter_status.value="<%=filter_status%>"</script>
									    	</div>
										</th>
										<th>
											Eff Date
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Sector Eff_dt" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Demand Sector Code
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Sector Demand" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Supply Sector Code
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Sector Supply" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div>
										</th>
									</tr>
								</thead>
								<tbody>
									<% int j=0;
									for(int i=0; i<VSECTOR_CD.size(); i++){
										int size = Integer.parseInt(VINDEX.elementAt(i).toString());
										%>
									<tr>
										<td width="5%" align="center">
											<div align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" 
													data-bs-toggle="modal" data-bs-target="#myModal" 
													onclick="doModify('<%=VSECTOR_CD.elementAt(i)%>','<%=VSECTOR_NAME.elementAt(i)%>','<%=VSECTOR_ABBR.elementAt(i)%>',
													'<%=VSECTOR_TYPE.elementAt(i)%>','<%=VSTATUS_FLAG.elementAt(i)%>','<%=VEFF_DT.elementAt(j) %>','<%=VDEMAND_SECTOR_CD.elementAt(j) %>','<%=VSUPPLY_SECTOR_CD.elementAt(j) %>');"></i>
												</font>
											</div>
										</td>
										<%if(size>0){%>
										<td onclick="hide_show('tbody<%=i %>','hidCont<%=i%>');">
		    								<%=VSECTOR_NAME.elementAt(i)%>&nbsp;&nbsp;&nbsp;<span id="hidCont<%=i%>" class="fa fa-expand" title="Click here to show Energy Sector summary"></span>
		    							</td>
		    							<%}else{%>
										<td><%=VSECTOR_NAME.elementAt(i)%></td>
										<%}%>
										<td><%=VSECTOR_ABBR.elementAt(i)%></td>
										<td align="center"><%=VSECTOR_TYPE_NM.elementAt(i)%></td>
										<td>
											<div>
												<font style="color:<%if(VSTATUS_FLAG.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VSTATUS_FLAG.elementAt(i).equals("Y")){%>
												Active
												<%}else{ %>
												In-Active
												<%} %>
											</div>
										</td>
										<td><%=VEFF_DT.elementAt(j) %></td>
										<td><%=VDEMAND_SECTOR_CD.elementAt(j) %></td>
										<td><%=VSUPPLY_SECTOR_CD.elementAt(j) %></td>
									</tr>
									<%if(size>0){%>
										<tbody id="tbody<%=i%>" style="display:none;">
											<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
													<td colspan="5" rowspan="<%=size+1%>" style="background:white;"></td>
													<td>Eff Date</td>
													<td>Demand Sector Code</td>
													<td>Supply Sector Code</td>
											</tr>
											<%
											int k=0;
											while(size>k)
											{
												j++;
											%>
												<tr>
													<td><%=VEFF_DT.elementAt(j) %></td>
													<td><%=VDEMAND_SECTOR_CD.elementAt(j) %></td>
													<td><%=VSUPPLY_SECTOR_CD.elementAt(j) %></td>
												</tr>
											<%k++;} %>
										</tbody>	
		    						<%}%>
									<%j++;} %>
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
<!-- 	<div class="modal-dialog modal-dialog-scrollable modal-lg"> -->
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Sector Detail
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Sector Name<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="sector_nm" maxlength="50" onblur="isExistSector();">
					    			<input type="hidden" class="form-control form-control-sm" name="sector_cd" >
					    		</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Sector ABBR<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="sector_abbr" maxlength="50" onblur="isExistSectorABBR();">
					    		</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Sector Type<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<select class="form-select form-select-sm" name="sector_type">
					    				<option value="Y">Re-seller/CGD/LDC</option>
									    <option value="P">End Customer</option>
									    <option value="O">Others</option>
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
<!-- 					<div class="row m-b-5"></div> -->
					<!-- Details -->
					<div class="row m-b-5 mt-3">
							<label class="form-label subheader"><input class="form-check-input" type="checkbox" name="add_chk" onclick="enabledSectorDetails()">&nbsp<i class="fa fa-snowflake-o"></i> Add/Modify Sector Code Detail</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" disabled>
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Demand Sector Code<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="demand_sector_cd" maxlength="50" disabled>
					    		</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Supply Sector Code<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
					    			<input type="text" class="form-control form-control-sm" name="supply_sector_cd" maxlength="50" disabled>
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
					

<input type="hidden" name="option" value="SECTOR_MST">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="detail_flag" value="false">
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
function isExistSector()
{
	var opration = document.forms[0].opration.value;
	
	var sector_cd ="0";
	if(opration == "MODIFY")
	{
		sector_cd = document.forms[0].sector_cd.value;
	}
	var name = document.forms[0].sector_nm.value;
	
	var info="";
	
	$.post("../servlet/DB_Master_Ajax?setCallType=IsExistSector&sector_cd="+sector_cd+"&sector_nm="+name+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.COUNTERPARTY_DTL, function(index_1, json_1) {
				if(parseInt(json_1.SECTOR_NAME) > 0)
				{
					info+="Sector Name is already Exist!\n";
				}
				if(info!="")
				{
					alert(info)
					document.forms[0].sector_nm.value="";
				}
			});
		});
	});
	
	return info;
}

function isExistSectorABBR()
{
	var opration = document.forms[0].opration.value;
	
	var sector_cd ="0";
	if(opration == "MODIFY")
	{
		sector_cd = document.forms[0].sector_cd.value;
	}
	var abbr = document.forms[0].sector_abbr.value;
	
	var info="";
	
	$.post("../servlet/DB_Master_Ajax?setCallType=IsExistSectorABBR&sector_cd="+sector_cd+"&sector_abbr="+abbr+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.COUNTERPARTY_DTL, function(index_1, json_1) {
				if(parseInt(json_1.SECTOR_ABBR) > 0)
				{
					info+="Sector ABBR is already Exist!\n";
				}
				if(info!="")
				{
					alert(info)
					document.forms[0].sector_abbr.value="";
				}
			});
		});
	});
	
	return info;
}


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