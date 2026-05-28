<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Arth patel 20250102 : Form for Truck Master-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var u = document.forms[0].u.value;
	
	var url = "frm_truck_mst.jsp?u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var truck_reg_no = document.forms[0].truck_reg_no.value;
	var truck_type = document.forms[0].truck_type.value;
	var truck_vol_m3 = document.forms[0].truck_vol_m3.value;
	var eff_dt = document.forms[0].eff_dt.value;
	var truck_vol_mt = document.forms[0].truck_vol_mt.value;
	var load_capacity = document.forms[0].load_capacity.value;
	document.forms[0].link_trans.disabled=false;
	document.forms[0].truck_trans_cd.disabled=false;
	
	var msg="";
	var flag=true;
	
	if(trim(truck_reg_no)=="")
	{
		msg+="Enter Truck Reg No.!\n";
		flag=false;
	}
	if(trim(truck_type)=="")
	{
		msg+="Select Truck type!\n";
		flag=false;
	}
	if(trim(eff_dt)=="")
	{
		msg+="Enter Eff date!\n";
		flag=false;
	}
	if(trim(truck_vol_m3)=="" || trim(truck_vol_mt)=="")
	{
		msg+="Enter Truck Volume!\n";
		flag=false;
	}
	if(trim(load_capacity)=="")
	{
		msg+="Enter Load Capacity!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" the Truck Details?");
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

function doModify(VTRUCK_CD,VTRUCK_REG_NO,VTRUCK_TYPE,VEFF_DT,VTRUCK_VOL_M3,VTRUCK_VOL_MT,VTRUCK_LOAD_CAP,VSTATUS,VLINKED_TRUCK_TRANS_CD,VLINKED_EFF_DT,VLINKED_FLAG,VLAST_RELEASE_DT)
{
	if(VSTATUS=="N")
	{
		document.getElementById("flexSwitchCheckChecked").checked=false;
		document.getElementById("lb").innerHTML="Inactive";
	}
	else if(VSTATUS=="Y")
	{
		document.getElementById("flexSwitchCheckChecked").checked=true;
		document.getElementById("lb").innerHTML="Active";
	}
	document.forms[0].truck_cd.value=VTRUCK_CD;
	document.forms[0].truck_reg_no.value=VTRUCK_REG_NO;
	document.forms[0].truck_type.value=VTRUCK_TYPE;
	document.forms[0].truck_vol_m3.value=VTRUCK_VOL_M3;
	document.forms[0].eff_dt.value=VEFF_DT;
	document.forms[0].truck_vol_mt.value=VTRUCK_VOL_MT;
	document.forms[0].load_capacity.value=VTRUCK_LOAD_CAP;
	document.forms[0].status_flag.value=VSTATUS;
	
	if(VLINKED_FLAG=="Y")
	{
		document.forms[0].link_trans.checked=true;
		document.forms[0].truck_trans_cd.value=VLINKED_TRUCK_TRANS_CD;
		document.forms[0].truck_trans_eff_dt.value=VLINKED_EFF_DT;
		document.forms[0].linked_eff_dt.value=VLINKED_EFF_DT;
		document.forms[0].linked_flg.value=VLINKED_FLAG;
		document.forms[0].link_trans.disabled=true;
		document.forms[0].truck_trans_cd.disabled=true;
	}
	document.getElementById('doLink_effdt').title="Last Release Date : "+VLAST_RELEASE_DT;
	link_Trans();
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	var sysdate=document.forms[0].sysdate.value;
	
	document.getElementById("flexSwitchCheckChecked").checked=true;
	document.forms[0].truck_cd.value="";
	document.forms[0].truck_reg_no.value="";
	document.forms[0].truck_type.value="";
	document.forms[0].truck_vol_m3.value="";
	document.forms[0].eff_dt.value="";
	document.forms[0].truck_vol_mt.value="";
	document.forms[0].load_capacity.value="";
	document.forms[0].opration.value="INSERT";
	document.forms[0].link_trans.checked=false;
	document.forms[0].truck_trans_cd.value="";
	document.forms[0].truck_trans_eff_dt.value=sysdate;
	document.forms[0].linked_eff_dt.value=sysdate;
	document.forms[0].linked_flg.value="";
	document.forms[0].link_trans.disabled=false;
	document.forms[0].truck_trans_cd.disabled=false;
	link_Trans();
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
		document.getElementById("lb").innerHTML="Inactive";
		document.getElementById("status_flag").value="N";
	}
}

function link_Trans()
{
	var link_trans = document.forms[0].link_trans;
	if(link_trans.checked)
	{
		document.forms[0].link_trans.value="Y";
		document.getElementById("trans_div").style.visibility='visible';
		document.getElementById("trans_div_1").style.visibility='visible';
		document.getElementById("trans_div_2").style.visibility='visible';
	}
	else
	{
		document.forms[0].link_trans.value="N";
		document.getElementById("trans_div").style.visibility='hidden';
		document.getElementById("trans_div_1").style.visibility='hidden';
		document.getElementById("trans_div_2").style.visibility='hidden';
	}
}

/*function textCounter(field, countfield, maxlimit)
{
	if(field.value.length > maxlimit)
	{
		field.value = field.value.substring(0, maxlimit);
	}
	else
	{
		countfield.value = maxlimit - field.value.length;
	}
}*/
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Master" id="dlngmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.dlng.UtilBean_DLNG" id="utilBean_dlng" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
dlngmaster.setCallFlag("TRUCK_MST");
dlngmaster.init();

Vector VTRUCK_TYPE = dlngmaster.getVTRUCK_TYPE();
Vector VEFF_DT = dlngmaster.getVEFF_DT();
Vector VSTATUS = dlngmaster.getVTRUCK_STATUS();
Vector VTRUCK_CD = dlngmaster.getVTRUCK_CD();
Vector VTRUCK_REG_NO = dlngmaster.getVTRUCK_REG_NO();
Vector VTRUCK_VOL_M3 = dlngmaster.getVTRUCK_VOL_M3();
Vector VTRUCK_VOL_MT = dlngmaster.getVTRUCK_VOL_MT();
Vector VTRUCK_LOAD_CAP = dlngmaster.getVTRUCK_LOAD_CAP();
Vector VACTIVE_TRUCK_TYPE = dlngmaster.getVACTIVE_TRUCK_TYPE();
Vector VTRUCK_TRANS_CD = dlngmaster.getVTRUCK_TRANS_CD();
Vector VTRUCK_TRANS_NAME = dlngmaster.getVTRUCK_TRANS_NAME();
Vector VTRUCK_TRANS_ABBR = dlngmaster.getVTRUCK_TRANS_ABBR();
Vector VLINKED_TRUCK_TRANS_CD = dlngmaster.getVLINKEDTRUCKTRANS();
Vector VLINKED_TRUCK_TRANS_NAME = dlngmaster.getVLINKEDTRUCKTRANSNAME();
Vector VLINKED_EFF_DT = dlngmaster.getVLINKEDEFFDT();
Vector VLINKED_FLAG = dlngmaster.getVLINKEDFLAG();
Vector VALLTRUCK_TRANS_CD = dlngmaster.getVALLTRUCK_TRANS_CD();
Vector VALLTRUCK_TRANS_NAME = dlngmaster.getVALLTRUCK_TRANS_NAME();
Vector VLAST_RELEASE_DT = dlngmaster.getVLAST_RELEASE_DT();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_DLNG_Master">
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
				    		Truck Master
	   	 				</div>
					 	<!-- <a href="../dlng/xls_truck_transport_mst.jsp?fileName=Truck Transporter Details.xls" download="Truck Transporter Details">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a> -->
				    </div>
				</div>
				<div class="card-body cdbody">
				<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#TruckModal" onclick="doClear();">Add New Truck</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="truck">
									<thead>
										<tr>
											<%if(write_access.equals("Y")){ %><th rowspan="2"></th><%} %>
											<th rowspan="2">Truck Reg. No.<br><div align="center"><input class="form-control form-control-sm" type="text" id="regno" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
									    	<th rowspan="2">Truck type<br><div align="center"><input class="form-control form-control-sm" type="text" id="typ" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>			    		
									    	<th rowspan="2">Eff Date<br><div align="center"><input class="form-control form-control-sm" type="text" id="effdt" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									    	<th colspan="3">Truck Capacity</th>
									    	<th rowspan="2">Linked Transporter</th>
										</tr>
										<tr>
											<th>M3</th>
											<th>MT</th>
											<th>Load(%)</th>
										</tr>
									</thead>
									<tbody id="mainTbody">
									<%int j=0;int k=0;
									if(VTRUCK_CD.size()>0){%>
										<%for(int i=0; i<VTRUCK_CD.size(); i++){%>
											<tr>
												<%if(write_access.equals("Y")){ %><td align="center">
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#TruckModal" 
														onclick="doModify('<%=VTRUCK_CD.elementAt(i)%>','<%=VTRUCK_REG_NO.elementAt(i)%>','<%=VTRUCK_TYPE.elementAt(i)%>','<%=VEFF_DT.elementAt(i)%>','<%=VTRUCK_VOL_M3.elementAt(i)%>','<%=VTRUCK_VOL_MT.elementAt(i)%>'
														,'<%=VTRUCK_LOAD_CAP.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>','<%=VLINKED_TRUCK_TRANS_CD.elementAt(i)%>','<%=VLINKED_EFF_DT.elementAt(i)%>','<%=VLINKED_FLAG.elementAt(i)%>','<%=VLAST_RELEASE_DT.elementAt(i)%>');">
														</i>
													</font>
												</td><%} %>
												<td align="center">
													<div align="center">
														<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
															<i class="fa fa-circle fa-lg" ></i>
															&nbsp;
														</font>
														<%=VTRUCK_REG_NO.elementAt(i)%>
													</div>
												</td>
		    									<td align="center"><%=VTRUCK_TYPE.elementAt(i)%></td>
		    									<td align="center"><%=VEFF_DT.elementAt(i)%></td>
		    									<td align="right"><%=VTRUCK_VOL_M3.elementAt(i) %></td>
		    									<td align="right"><%=VTRUCK_VOL_MT.elementAt(i) %></td>
		    									<td align="right"><%=VTRUCK_LOAD_CAP.elementAt(i) %></td>
		    									<td align="center"><%=VLINKED_TRUCK_TRANS_NAME.elementAt(i) %></td>
											</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="8" align="center"><%=utilmsg.infoMessage("<b>No Truck Type Data Available!</b>") %></td>
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
</div>
<div class="modal fade" id="TruckModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Truck Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="doClear()">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Truck Reg. No.<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<input type="text" class="form-control form-control-sm" name="truck_reg_no" value="" onBlur="isExistTruck();" maxLength="100">
						    		<input type="hidden" name="truck_cd" value="" >
						    	</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Truck Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<select class="form-select form-select-sm" name="truck_type" onchange="getTruckCapDtls();">
				      					<option value="">--Select--</option>
				      					<%for(int i=0; i<VACTIVE_TRUCK_TYPE.size(); i++){ %>
				      						<option value="<%=VACTIVE_TRUCK_TYPE.elementAt(i)%>"><%=VACTIVE_TRUCK_TYPE.elementAt(i)%></option>
				      					<%} %>
				      				</select>
						    	</div>
				  			</div>
						</div>
					</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Effective Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
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
					<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Volume<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm" name="truck_vol_m3" id="truck_vol_m3"  value=""  autocomplete="off" onblur="negNumber(this);" style="text-align: right" >
	      						<input type="hidden" class="form-control form-control-sm" name="temp_truck_vol_m3" id="temp_truck_vol_m3" value="">
	      						<span class="input-group-text"><b>M3</b></span>
	      						<input type="text" class="form-control form-control-sm" name="truck_vol_mt" id="truck_vol_mt"  value=""  autocomplete="off" onblur="negNumber(this);" style="text-align: right">
	      						<span class="input-group-text"><b>MT</b></span>
      						</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Load capacity<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
			      					<div class="input-group input-group-sm" >
							    		<input type="text" class="form-control form-control-sm" name="load_capacity" value="" onblur="negNumber(this);appPercentage(this);">
							    		<span class="input-group-text">%</span>
							    	</div>
								</div>
							</div>
						</div>
					</div>
					&nbsp;
					<div class="row m-b-5" id="chk_div">
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
								<label class="form-label">
									<input type="checkbox" class="form-check-input" name="link_trans" value="N" onClick="link_Trans();">&nbsp;<b>Link Transporter</b>
								</label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" style="visibility: hidden" id="trans_div">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
			      					<select class="form-select form-select-sm" name="truck_trans_cd" id="truck_trans_cd"> 
				      					<option value="">--Select--</option>
				      					<%for(int i=0; i<VTRUCK_TRANS_CD.size(); i++){ %>
				      						<option value="<%=VTRUCK_TRANS_CD.elementAt(i)%>" id="active_opt"><%=VTRUCK_TRANS_NAME.elementAt(i)%></option>
				      					<%} %>
				      					<%for(int i=0; i<VALLTRUCK_TRANS_CD.size(); i++){ %>
				      						<option value="<%=VALLTRUCK_TRANS_CD.elementAt(i)%>" id="all_opt" style="display: none"><%=VALLTRUCK_TRANS_NAME.elementAt(i)%></option>
				      					<%} %>
				      				</select>
				      				<input type="hidden" name="truck_trans_eff_dt" value="<%=sysdate %>" >
				      				<input type="hidden" name="linked_flg" value="" >
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2" style="visibility: hidden" id="trans_div_1"> 
							<div class="form-group row">
				    			<label class="form-label" id="doLink_effdt"><b>Link Effective Date</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12" style="visibility: hidden" id="trans_div_2">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="linked_eff_dt" value="<%=sysdate %>" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" style="pointer-events:none;" readonly>
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
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

<input type="hidden" name="option" value="TRUCK_MST">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="prev_display" value="">
<input type="hidden" name="prev_display1" value="">

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

<script>
function isExistTruck()
{
	var opration = document.forms[0].opration.value;
	
	var truck_cd ="0";
	if(opration == "MODIFY")
	{
		truck_cd = document.forms[0].truck_cd.value;
	}
	var truck_reg_no = document.forms[0].truck_reg_no.value;
	
	var info="";
	
	$.post("../servlet/DB_Dlng_Ajax?setCallType=IsExistTRUCK&truck_cd="+truck_cd+"&truck_reg_no="+truck_reg_no+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.TRUCK_DTL, function(index_1, json_1) {
				if(parseInt(json_1.TRUCK) > 0)
				{
					info+="Truck Reg No. is already Exist!\n";
				}
				if(info!="")
				{
					alert(info)
					document.forms[0].truck_reg_no.value="";
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
  	table = document.getElementById("truck");
  	
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

function getTruckCapDtls()
{
	var truck_type = document.forms[0].truck_type.value;
	$.post("../servlet/DB_Dlng_Ajax?truck_type="+truck_type+"&setCallType=TRUCKCAPDTL", function(responseJson) {
		//console.log(responseJson);
		$.each(responseJson, function(index, json) {
			$.each(json.TRUCK_CAP_DTL, function(index_1, json_1) {
				
				document.forms[0].truck_vol_m3.value=json_1.TRUCK_VOL_M3;
				document.forms[0].truck_vol_mt.value=json_1.TRUCK_VOL_MT;
				document.forms[0].load_capacity.value=json_1.TRUCK_LOAD_CAP;
					
			});
		});
	});
}
</script>

</form>
</body>
</html>