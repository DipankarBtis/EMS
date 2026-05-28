<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Harsh Maheta 20230807 : Form for Tank Master-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function doSubmit(action)
{
	var tank_name = document.forms[0].tank_name.value;
	var eff_dt = document.forms[0].eff_dt.value;
	var tank_diameter = document.forms[0].tank_diameter.value;
	var tank_pi_tag = document.forms[0].tank_pi_tag.value;
	
	var ic_eff_dt = document.forms[0].ic_eff_dt.value;
	
	var opration = document.forms[0].opration.value;
	var validation = document.forms[0].validation.value
	
	var msg="";
	var flag=true;
	
	if(action == "TM")
	{
		document.forms[0].option.value = "TANK_MST";
		
		if(trim(tank_name)=="")
		{
			msg+="Enter tank Name!\n";
			flag=false;
		}
		
		if(trim(eff_dt)=="")
		{
			msg+="Enter Tank Effective Date!\n";
			flag=false;
		}
		
		if(trim(tank_diameter)=="")
		{
			msg+="Enter Tank Diameter!\n";
			flag=false;
		}
		
		if(validation=="F")
		{
			flag=false;
		}
		
		if(flag)
		{
			var a = confirm("Do you want to "+opration+" the tank Details?");
			if(a)
			{
				document.getElementById("loading").style.visibility = "visible";
				document.forms[0].submit();
			}
		}
		else if(trim(msg).length>0)
		{
			alert(msg);
		}
	}
	else if(action == "IC")
	{
		document.forms[0].option.value = "TANK_INTERNAL_CONSUMPTION_DTL";
		
		if(trim(ic_eff_dt)=="")
		{
			msg+="Enter Internal Consumption Effective Date!\n";
			flag=false;
		}
		
		if(flag)
		{
			var a = confirm("Do you want to submit the Internal Consumption Details for "+ic_eff_dt+" ?");
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
}

function setActiveInactive(obj)
{
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status").value="Y";
	}
	else
	{
		document.getElementById("lb").innerHTML="Cancel";
		document.getElementById("status").value="N";
	}
}

function doClear()
{
	document.forms[0].tank_name.value="";
	document.forms[0].eff_dt.value="";
	document.forms[0].tank_diameter.value="";
	document.forms[0].tank_t1_height.value="";
	document.forms[0].tank_t1_volume.value="";
	document.forms[0].tank_t2_height.value="";
	document.forms[0].tank_t2_volume.value="";
	document.forms[0].tank_d1_height.value="";
	document.forms[0].tank_d1_volume.value="";
	document.forms[0].tank_d2_height.value="";
	document.forms[0].tank_d2_volume.value="";
	document.forms[0].tank_pi_tag.value="";
	
	document.forms[0].opration.value="INSERT";
}

function doClearIcd()
{
	document.forms[0].ic_eff_dt.value="";
	document.forms[0].ic_percentage.value="";
	document.forms[0].ic_remark.value="";
	
	document.forms[0].opration.value="INSERT";
}

function doModify(VTANK_CD,VTANK_NAME,VEFF_DT,VSTATUS,VTANK_T1_VOLUME,VTANK_T1_HEIGHT,VTANK_T2_VOLUME,
		VTANK_T2_HEIGHT,VTANK_D1_VOLUME,VTANK_D1_HEIGHT,VTANK_D2_VOLUME,VTANK_D2_HEIGHT,VTANK_DIAMETER,VTANK_PI_TAG)
{
	if(VSTATUS=="N")
	{
		document.getElementById("flexSwitchCheckChecked").checked=false;
		document.getElementById("lb").innerHTML="Inactive";
		document.forms[0].status.value=VSTATUS;
	}
	else if(VSTATUS=="Y")
	{
		document.getElementById("flexSwitchCheckChecked").checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.forms[0].status.value=VSTATUS;
	}
	document.forms[0].tank_cd.value=VTANK_CD;
	document.forms[0].tank_name.value=VTANK_NAME;
	document.forms[0].eff_dt.value=VEFF_DT;
	document.forms[0].tank_diameter.value=VTANK_DIAMETER;
	document.forms[0].tank_t1_height.value=VTANK_T1_HEIGHT;
	document.forms[0].tank_t1_volume.value=VTANK_T1_VOLUME;
	document.forms[0].tank_t2_height.value=VTANK_T2_HEIGHT;
	document.forms[0].tank_t2_volume.value=VTANK_T2_VOLUME;
	document.forms[0].tank_d1_height.value=VTANK_D1_HEIGHT;
	document.forms[0].tank_d1_volume.value=VTANK_D1_VOLUME;
	document.forms[0].tank_d2_height.value=VTANK_D2_HEIGHT;
	document.forms[0].tank_d2_volume.value=VTANK_D2_VOLUME;
	document.forms[0].tank_pi_tag.value=VTANK_PI_TAG;
	
	document.forms[0].opration.value="MODIFY";
}
function doModifyIcd(VICD_EFF_DT,VICD_PERCENTAGE,VICD_REMARK)
{
	document.forms[0].ic_eff_dt.value=VICD_EFF_DT;
	document.forms[0].ic_percentage.value=VICD_PERCENTAGE;
	document.forms[0].ic_remark.value=VICD_REMARK;
	
	document.forms[0].opration.value="MODIFY";
}

function refresh()
{
	var ic_eff_dt = document.forms[0].ic_eff_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_tank_mst.jsp?u="+u+"&ic_eff_dt="+ic_eff_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function validateHeights() 
{
    const t1 = document.getElementById("tank_t1_height").value;
    const t2 = document.getElementById("tank_t2_height").value;
    const d2 = document.getElementById("tank_d2_height").value;
    const d1 = document.getElementById("tank_d1_height").value;

    const t1Value = t1 ? parseFloat(t1) : null;
    const t2Value = t2 ? parseFloat(t2) : null;
    const d2Value = d2 ? parseFloat(d2) : null;
    const d1Value = d1 ? parseFloat(d1) : null;

    const values = [t1Value, t2Value, d2Value, d1Value].filter(val => val !== null);

    let isValid = true;
    for (let i = 0; i < values.length - 1; i++)
    {
    	if (values[i] < values[i + 1])
    	{
    		isValid = false;
            break;
        }
    }

    if (isValid)
    {
    	 document.forms[0].validation.value="T";
    	 
    	 validateVolumes();
    }
    else
    {
    	let msg = "Tank Level Height Validation Failed!\n";

    	if (t1Value !== null && (t1Value < t2Value || t1Value < d1Value || t1Value < d2Value)) {
            msg += "\n- Tank Top Level-1 (T1) Height should be greater than other Levels!!";
        }
        if (t2Value !== null  && (t2Value < d2Value || t2Value < d1Value)) {
            msg += "\n- Tank Top Level-2 (T2) Height should be greater than Dead Stock Level-2 (D2) and Dead Stock Level-1 (D1)!!";
        }
        if (d2Value !== null  && d2Value < d1Value) {
            msg += "\n- Dead Stock Level-2 (D2) Height should be greater than Dead Stock Level-1 (D1).";
        }
    	
        alert(msg);
        document.getElementById("tank_t1_height").value = "";
        document.getElementById("tank_t2_height").value = "";
        document.getElementById("tank_d2_height").value = "";
        document.getElementById("tank_d1_height").value = "";
        
        document.forms[0].validation.value="F";
    }
}

function validateVolumes() 
{
    const t1_vol = document.getElementById("tank_t1_volume").value;
    const t2_vol = document.getElementById("tank_t2_volume").value;
    const d2_vol = document.getElementById("tank_d2_volume").value;
    const d1_vol = document.getElementById("tank_d1_volume").value;

    const t1Value = t1_vol ? parseFloat(t1_vol) : null;
    const t2Value = t2_vol ? parseFloat(t2_vol) : null;
    const d2Value = d2_vol ? parseFloat(d2_vol) : null;
    const d1Value = d1_vol ? parseFloat(d1_vol) : null;

    const values = [t1Value, t2Value, d2Value, d1Value].filter(val => val !== null);

    let isValid = true;
    for (let i = 0; i < values.length - 1; i++)
    {
    	if (values[i] < values[i + 1])
    	{
    		isValid = false;
            break;
        }
    }

    if (isValid)
    {
    	 document.forms[0].validation.value="T";
    }
    else
    {
    	let msg = "Tank Level Volume Validation Failed!\n";

    	if (t1Value !== null && (t1Value < t2Value || t1Value < d1Value || t1Value < d2Value)) {
            msg += "\n- Tank Top Level-1 (T1) Volume should be greater than other Levels!!";
        }
        if (t2Value !== null  && (t2Value < d2Value || t2Value < d1Value)) {
            msg += "\n- Tank Top Level-2 (T2) Volume should be greater than Dead Stock Level-2 (D2) and Dead Stock Level-1 (D1)!!";
        }
        if (d2Value !== null  && d2Value < d1Value) {
            msg += "\n- Dead Stock Level-2 (D2) Volume should be greater than Dead Stock Level-1 (D1).";
        }
    	
        alert(msg);
        document.getElementById("tank_t1_volume").value = "";
        document.getElementById("tank_t2_volume").value = "";
        document.getElementById("tank_d2_volume").value = "";
        document.getElementById("tank_d1_volume").value = "";
        
        document.forms[0].validation.value="F";
    }
}

function calcM3volume(id)
{
	/* const obj = document.getElementById(id+"_height").value;

	var tank_diameter = document.forms[0].tank_diameter.value;
	
    const objValue = obj ? parseFloat(obj) : null;
    
    const expectedVol = parseFloat(parseFloat((3.14*(objValue/1000))/4)*parseFloat(tank_diameter*tank_diameter));
    
    if(document.getElementById(id+"_volume").value == "" || document.getElementById(id+"_volume").value == null)
    {
    	document.getElementById(id+"_volume").value=expectedVol;
    } */
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.inventory.DataBean_TankTerminal" id="dbterminal" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysDt=utildate.getSysdate();

dbterminal.setCallFlag("TANK_MST");
dbterminal.setComp_cd(owner_cd);
//dbterminal.setIc_eff_dt(ic_eff_dt);
dbterminal.init();

Vector VTANK_CD = dbterminal.getVTANK_CD();
Vector VTANK_NAME = dbterminal.getVTANK_NAME();
Vector VEFF_DT = dbterminal.getVEFF_DT();
Vector VSTATUS = dbterminal.getVSTATUS();
Vector VTANK_T1_VOLUME = dbterminal.getVTANK_T1_VOLUME();
Vector VTANK_T1_HEIGHT = dbterminal.getVTANK_T1_HEIGHT();
Vector VTANK_T2_VOLUME = dbterminal.getVTANK_T2_VOLUME();
Vector VTANK_T2_HEIGHT = dbterminal.getVTANK_T2_HEIGHT();
Vector VTANK_D1_VOLUME = dbterminal.getVTANK_D1_VOLUME();
Vector VTANK_D1_HEIGHT = dbterminal.getVTANK_D1_HEIGHT();
Vector VTANK_D2_VOLUME = dbterminal.getVTANK_D2_VOLUME();
Vector VTANK_D2_HEIGHT = dbterminal.getVTANK_D2_HEIGHT();
Vector VTANK_DIAMETER = dbterminal.getVTANK_DIAMETER();
Vector VTANK_PI_TAG = dbterminal.getVTANK_PI_TAG();

Vector VICD_EFF_DT = dbterminal.getVICD_EFF_DT();
Vector VICD_PERCENTAGE = dbterminal.getVICD_PERCENTAGE();
Vector VICD_REMARK = dbterminal.getVICD_REMARK();

/* String int_cons_percentage = dbterminal.getInt_cons_percentage();
String int_cons_remark = dbterminal.getInt_cons_remark(); */

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_TankTerminal">
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
				    		LNG Tank Master
	   	 				</div>
					 	<a href="../inventory/xls_tank_mst.jsp?fileName=Tank Details.xls" download="Tank Details">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a>
				    </div>
				</div>
				<div class="card-body cdbody">
				<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#TankModal" onclick="doClear();">Add New Tank</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<%if(write_access.equals("Y")){ %><th rowspan="2"></th><%} %>
											<th rowspan="2">Tank Name</th>
											<th rowspan="2">Pi Tag</th>
											<th rowspan="2">Eff. Date</th>
											<th rowspan="2">Diameter (Millimetre)</th>
											<!-- <th rowspan="2">Status</th> -->
									    	<th colspan="2" title="T1 : Highest allowable oil level in the tank to prevent overflow.">Tank Top Level-1 (T1)</th>
									    	<th colspan="2" title="T2 : Slightly below T1, indicates operational threshold, such as, when to reduce inflow.">Tank Top Level-2 (T2)</th>
									    	<th colspan="2" title="D1 : Lowest point of usable oil in the tank.Oil below this level is considered Dead Stock, meaning it cannot be pumped out effectively.">Dead Stock Level-1 (D1)</th>
									    	<th colspan="2" title="D2 : Slightly above D1, indicates a buffer level to prevent damage of pumping systems or introduce air into the system.">Dead Stock Level-2 (D2)</th>
										</tr>
										<tr>
											<th>Height (Millimetre)</th>
											<th>Volume (M3 of LNG)</th>
											<!-- <th>Diameter (Millimetre)</th> -->
											<th>Height (Millimetre)</th>
											<th>Volume (M3 of LNG)</th>
											<!-- <th>Diameter (Millimetre)</th> -->
											<th>Height (Millimetre)</th>
											<th>Volume (M3 of LNG)</th>
											<!-- <th>Diameter (Millimetre)</th> -->
											<th>Height (Millimetre)</th>
											<th>Volume (M3 of LNG)</th>
											<!-- <th>Diameter (Millimetre)</th> -->
										</tr>
									</thead>
									<tbody id="mainTbody">
									<%int j=0;int k=0;
									if(VTANK_CD.size()>0){%>
										<%for(int i=0; i<VTANK_CD.size(); i++){ 
										%>
											<tr>
												<%if(write_access.equals("Y")){ %>
												<td align="center">
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#TankModal" 
														onclick="doModify('<%=VTANK_CD.elementAt(i)%>','<%=VTANK_NAME.elementAt(i)%>','<%=VEFF_DT.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>','<%=VTANK_T1_VOLUME.elementAt(i)%>',
														'<%=VTANK_T1_HEIGHT.elementAt(i)%>','<%=VTANK_T2_VOLUME.elementAt(i)%>','<%=VTANK_T2_HEIGHT.elementAt(i)%>','<%=VTANK_D1_VOLUME.elementAt(i)%>','<%=VTANK_D1_HEIGHT.elementAt(i)%>',
														'<%=VTANK_D2_VOLUME.elementAt(i)%>','<%=VTANK_D2_HEIGHT.elementAt(i)%>','<%=VTANK_DIAMETER.elementAt(i)%>','<%=VTANK_PI_TAG.elementAt(i)%>');">
														</i>
													</font>
												</td><%} %>
		    									<td align="center">
		    									<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font><%=VTANK_NAME.elementAt(i)%></td>
												<td align="center"><%=VTANK_PI_TAG.elementAt(i)%></td>
		    									<td align="center"><%=VEFF_DT.elementAt(i)%></td>
		    									<td align="center"><%=VTANK_DIAMETER.elementAt(i)%></td>
		    									<%-- <td align="center"><div align="center">
												<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VSTATUS.elementAt(i).equals("Y")){%>
												Active
												<%}else{ %>
												Cancelled
												<%} %>
											</div></td> --%>
		    									<td align="center"><%=VTANK_T1_HEIGHT.elementAt(i)%></td>
		    									<td align="center"><%=VTANK_T1_VOLUME.elementAt(i)%></td>
		    									<td align="center"><%=VTANK_T2_HEIGHT.elementAt(i)%></td>
		    									<td align="center"><%=VTANK_T2_VOLUME.elementAt(i)%></td>
		    									<td align="center"><%=VTANK_D1_HEIGHT.elementAt(i)%></td>
		    									<td align="center"><%=VTANK_D1_VOLUME.elementAt(i)%></td>
		    									<td align="center"><%=VTANK_D2_HEIGHT.elementAt(i)%></td>
		    									<td align="center"><%=VTANK_D2_VOLUME.elementAt(i)%></td>
											</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="13" align="center"><%=utilmsg.infoMessage("<b>No Tank Data Available!</b>") %></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			&nbsp;
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		Internal Consumption Details
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#ICDModal" onclick="doClearIcd();">Add Internal Consumption</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr>
											<th></th>
											<th>Effective Date</th>
											<th>Internal Consumption Percentage(%)</th>
											<th>Remark</th>
										</tr>
									</thead>
									<tbody id="mainTbody">
									<%//int j=0;int k=0;
									if(VICD_EFF_DT.size()>0){%>
										<%for(int i=0; i<VICD_EFF_DT.size(); i++){ 
										%>
											<tr>
												<td align="center">
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#ICDModal" 
														onclick="doModifyIcd('<%=VICD_EFF_DT.elementAt(i)%>','<%=VICD_PERCENTAGE.elementAt(i)%>','<%=VICD_REMARK.elementAt(i)%>');">
														</i>
													</font>
												</td>
		    									<td align="center"><%=VICD_EFF_DT.elementAt(i)%></td>
												<td align="center"><%=VICD_PERCENTAGE.elementAt(i)%></td>
		    									<td align="center"><%=VICD_REMARK.elementAt(i)%></td>
											</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>No Internal Consumption details Data Available!</b>") %></td>
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
<div class="modal fade" id="TankModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Tank Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<!-- <div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Tank Details</label>
					</div> -->
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Tank Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<input type="text" class="form-control form-control-sm" name="tank_name" value="" onblur="isExistNM();">
						    		<input type="hidden" name="tank_cd" value="">
						    	</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
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
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Diameter<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<input type="text" class="form-control form-control-sm" name="tank_diameter" value="" >
						    	</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Pi Tag</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<input type="text" class="form-control form-control-sm" name="tank_pi_tag" value="" maxlength="50" onblur="isExistPiTag();">
						    	</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
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
									  	<input type="hidden" name="status" id="status" value="Y">
									</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Tank Level Details</label>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="innerTab">
									<thead>
										<tr>
											<th>Tank Level</th>
											<th>Height (Millimetre)</th>
											<th>Volume (M3 of LNG)</th>
										</tr>
									</thead>
									<tbody id="mainTbody">
										<tr>
	    									<td align="center">Tank Top Level-1 (T1)</td>
	    									<td align="center">
												<div class="col-sm-4 col-xs-4 col-md-4">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="tank_t1_height" id="tank_t1_height" value="" onblur="calcM3volume('tank_t1');">
												    	</div>
										  			</div>
												</div>
											</td>
	    									<td align="center">
	    										<div class="col-sm-4 col-xs-4 col-md-4">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="tank_t1_volume" id="tank_t1_volume" value="" >
												    	</div>
										  			</div>
												</div>
	    									</td>
										</tr>
										<tr>
	    									<td align="center">Tank Top Level-2 (T2)</td>
	    									<td align="center">
	    										<div class="col-sm-4 col-xs-4 col-md-4">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="tank_t2_height" id="tank_t2_height" value="" onblur="calcM3volume('tank_t2');">
												    	</div>
										  			</div>
												</div>
	    									</td>
	    									<td align="center">
	    										<div class="col-sm-4 col-xs-4 col-md-4">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="tank_t2_volume" id="tank_t2_volume" value="" >
												    	</div>
										  			</div>
												</div>
	    									</td>
										</tr>
										<tr>
	    									<td align="center">Dead Stock Level-1 (D1)</td>
	    									<td align="center">
	    										<div class="col-sm-4 col-xs-4 col-md-4">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="tank_d1_height" id="tank_d1_height" value="" onblur="calcM3volume('tank_d1');">
												    	</div>
										  			</div>
												</div>
	    									</td>
	    									<td align="center">
	    										<div class="col-sm-4 col-xs-4 col-md-4">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="tank_d1_volume" id="tank_d1_volume" value="" >
												    	</div>
										  			</div>
												</div>
	    									</td>
										</tr>
										<tr>
	    									<td align="center">Dead Stock Level-2 (D2)</td>
	    									<td align="center">
	    										<div class="col-sm-4 col-xs-4 col-md-4">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="tank_d2_height" id="tank_d2_height" value="" onblur="calcM3volume('tank_d2');">
												    	</div>
										  			</div>
												</div>
	    									</td>
	    									<td align="center">
	    										<div class="col-sm-4 col-xs-4 col-md-4">  
													<div class="form-group row">
												    	<div class="col-sm-12 col-xs-12 col-md-12">
												    		<input type="text" class="form-control form-control-sm" name="tank_d2_volume" id="tank_d2_volume" value="" >
												    	</div>
										  			</div>
												</div>
	    									</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
					<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="validateHeights();doSubmit('TM');">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
      		</div>
      	</div>
	</div>
</div>

<div class="modal fade" id="ICDModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Internal Consumption Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Effective Date</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
			      				<div class="input-group input-group-sm" >
		      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="ic_eff_dt" id="ic_eff_dt"  value="" maxLength="10" 
		      						onblur="validateDate(this);" 
		      						onchange="validateDate(this);" autocomplete="off">
		      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
	      						</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Internal Consumption Percentage(%) </b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ic_percentage" id="ic_percentage" value="" maxLength="" onblur="negNumber(this);appPercentage(this);">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Remark</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<textarea class="form-control" name="ic_remark" cols="75" rows="1" maxlength="500"></textarea>
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
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit('IC');">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
		   	</div>
      	</div>
	</div>
</div>

<input type="hidden" name="option" value="TANK_MST">
<input type="hidden" name="opration" value="INSERT">

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
<input type="hidden" name="validation" value="">

<script type="text/javascript">
function isExistNM()
{
	var opration = document.forms[0].opration.value;
	
	var tank_cd ="0";
	if(opration == "MODIFY")
	{
		tank_cd = document.forms[0].tank_cd.value;
	}
	var tank_nm = document.forms[0].tank_name.value;
	
	var info="";
	
	$.post("../servlet/DB_Inventory_Ajax?setCallType=IsExistNM&tank_cd="+tank_cd+"&tank_nm="+tank_nm+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.TANK_DTL, function(index_1, json_1) {
				if(parseInt(json_1.NAME) > 0)
				{
					info+="Tank Name already Exist!\n";
				}
				if(info!="")
				{
					alert(info)
					document.forms[0].tank_name.value="";
				}
			});
		});
	});
	
	return info;
}

function isExistPiTag()
{
	var opration = document.forms[0].opration.value;
	
	var tank_cd ="0";
	if(opration == "MODIFY")
	{
		tank_cd = document.forms[0].tank_cd.value;
	}
	var tank_pi_tag = document.forms[0].tank_pi_tag.value;
	
	var info="";
	
	$.post("../servlet/DB_Inventory_Ajax?setCallType=IsExistPitag&tank_cd="+tank_cd+
			"&tank_pi_tag="+tank_pi_tag+"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.TANK_DTL, function(index_1, json_1) {
				if(parseInt(json_1.PI_TAG) > 0)
				{
					info+="Pi Tag already Exist!";
				}
				
				if(info!="")
				{
					alert(info)
					document.forms[0].tank_pi_tag.value="";
				}
			});
		});
	});
}
</script>
</form>
</body>
</html>