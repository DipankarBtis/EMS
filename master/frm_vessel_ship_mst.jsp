<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh(opration)
{
	var u = document.forms[0].u.value;
	
	var url = "frm_vessel_ship_mst.jsp?opration="+opration+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function checkValuePrecision(value,precision,maxVal)
{
	try
	{
		if(value == "" || value == null)
		{
			
		}
		else
		{
			var number = parseFloat(value);
	        if (isNaN(number)) {
	            alert("Value is not a valid number!!");
	        }
	        
			if(value.includes('.'))
			{
				 var parts = value.split('.');
				 var integerPart = parts[0];
				 var decimalPart = parts[1] || '';

				 if (decimalPart.length <= precision)
				 {
					 return true;
				 } 
				 else
				 {
					 alert("Provided Value should be in ("+maxVal+", "+precision+") format!!");
					 return false;
				 }
			}
		}
    }
	catch (error)
	{
        alert(error);
        return false;
    }
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	var ship_nm = document.forms[0].ship_nm.value;
	var ship_flag = document.forms[0].ship_flag.value;
	var imo_no = document.forms[0].imo_no.value;
	var eff_dt = document.forms[0].eff_dt.value;
	
	if(ship_nm=="" || ship_nm=="0" || trim(ship_nm)=="")
	{
		msg+="Enter Vessel Name!\n";
		flag=false
	}
	
	if(eff_dt=="" || eff_dt=="0" || trim(eff_dt)=="")
	{
		msg+="Enter Effective Date for Vessel!\n";
		flag=false
	}
	
	if(ship_flag=="" || ship_flag=="0" || trim(ship_flag)=="")
	{
		msg+="Enter Vessel Flag!\n";
		flag=false
	}
	
	if(imo_no=="" || imo_no=="0" || trim(imo_no)=="")
	{
		msg+="Enter Vessel IMO No!\n";
		flag=false
	}
	
	if(flag)
	{
		var a;
		
		a = confirm("Do you want to "+opration+" Vessel Detail?")

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

function doClear()
{
	document.forms[0].ship_cd.value="";
	document.forms[0].ship_nm.value="";
	document.forms[0].ship_call_sign.value="";
	document.forms[0].ship_flag.value="";
	document.forms[0].imo_no.value="";
	document.forms[0].class_soc.value="";
	document.forms[0].inmarsat_no.value="";
	document.forms[0].vessel_owner.value="";
	document.forms[0].vessel_operator.value="";
	document.forms[0].fax_no.value="";
	document.forms[0].telex_no.value="";
	document.forms[0].vessel_email.value="";
	document.forms[0].gross_tonnage.value="";
	document.forms[0].approx_cargo_cap.value="";
	document.forms[0].app_cargo_capecity_unit.value="0";
	document.forms[0].cap_per.value="";
	document.forms[0].items.value="";
	document.forms[0].eff_dt.value="";
	
	document.forms[0].opration.value="INSERT";
}

function doModify(VSHIP_CD,VSHIP_NAME,VSHIP_CALL_SIGN,VSHIP_FLAG,VSHIP_IMO_NO,VSHIP_CLASS_SOC,VINMARSAT_NO,
		VSHIP_OWNER_NAME,VSHIP_OPERATOR_NAME,VSHIP_FAX_NO,VSHIP_TELEX_NO,VSHIP_EMAIL,VGROSS_TONNAGE,
		VCARGO_CAPACITY,VVOLUME_UNIT,VPERCENTAGE_CAPACITY ,VSHIP_ITEM,VSHIP_EFF_DT)
{

	document.forms[0].ship_cd.value=VSHIP_CD;
	document.forms[0].ship_nm.value=VSHIP_NAME;
	document.forms[0].ship_call_sign.value=VSHIP_CALL_SIGN;
	document.forms[0].ship_flag.value=VSHIP_FLAG;
	document.forms[0].imo_no.value=VSHIP_IMO_NO;
	document.forms[0].class_soc.value=VSHIP_CLASS_SOC;
	document.forms[0].inmarsat_no.value=VINMARSAT_NO;
	document.forms[0].vessel_owner.value=VSHIP_OWNER_NAME;
	document.forms[0].vessel_operator.value=VSHIP_OPERATOR_NAME;
	document.forms[0].fax_no.value=VSHIP_FAX_NO;
	document.forms[0].telex_no.value=VSHIP_TELEX_NO;
	document.forms[0].vessel_email.value=VSHIP_EMAIL;
	document.forms[0].gross_tonnage.value=VGROSS_TONNAGE;
	document.forms[0].approx_cargo_cap.value=VCARGO_CAPACITY;
	document.forms[0].app_cargo_capecity_unit.value=VVOLUME_UNIT;
	document.forms[0].cap_per.value=VPERCENTAGE_CAPACITY;
	document.forms[0].items.value=VSHIP_ITEM;
	document.forms[0].eff_dt.value=VSHIP_EFF_DT;
	
	document.forms[0].opration.value="MODIFY";
}

var newWindow;
function openList()
{
	var url = "frm_ship_mst_list.jsp";

	if(!newWindow || newWindow.closed)
	{
		newWindow= window.open(url,"Vessel List","top=10,left=70,width=800,height=700,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow= window.open(url,"Vessel List","top=10,left=70,width=800,height=700,scrollbars=1");
	}
}

function refreshChild(ship_cd)
{
	var opration = document.forms[0].opration.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_vessel_ship_mst.jsp?ship_cd="+ship_cd+"&opration="+opration+
			"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var url = "xls_vessel_ship_mst.jsp?fileName=Vessel Master Report.xls";

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbcargo" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String ship_cd = request.getParameter("ship_cd")==null?"0":request.getParameter("ship_cd");

dbcargo.setCallFlag("VESSEL_MST");
dbcargo.setComp_cd(owner_cd);
//dbcargo.setShip_cd(ship_cd);
dbcargo.init();

String ship_name = dbcargo.getShip_name();
String ship_call_sign = dbcargo.getShip_call_sign();
String ship_flag = dbcargo.getShip_flag();
String ship_imo_no = dbcargo.getShip_imo_no();
String ship_class_soc = dbcargo.getShip_class_soc();
String inmarsat_no = dbcargo.getInmarsat_no();
String ship_owner_name = dbcargo.getShip_owner_name();
String ship_operator_name = dbcargo.getShip_operator_name();
String ship_fax_no = dbcargo.getShip_fax_no();
String ship_telex_no = dbcargo.getShip_telex_no();
String ship_email = dbcargo.getShip_email();
String gross_tonnage = dbcargo.getGross_tonnage();
String cargo_capacity = dbcargo.getCargo_capacity();
String volume_unit = dbcargo.getVolume_unit();
String percentage_capacity = dbcargo.getPercentage_capacity();
String ship_item = dbcargo.getShip_item();

Vector VSHIP_CD = dbcargo.getVSHIP_CD();
Vector VSHIP_NAME = dbcargo.getVSHIP_NAME();
Vector VSHIP_CALL_SIGN = dbcargo.getVSHIP_CALL_SIGN();
Vector VSHIP_FLAG = dbcargo.getVSHIP_FLAG();
Vector VSHIP_IMO_NO = dbcargo.getVSHIP_IMO_NO();
Vector VSHIP_CLASS_SOC = dbcargo.getVSHIP_CLASS_SOC();
Vector VINMARSAT_NO = dbcargo.getVINMARSAT_NO();
Vector VSHIP_OWNER_NAME = dbcargo.getVSHIP_OWNER_NAME();
Vector VSHIP_OPERATOR_NAME = dbcargo.getVSHIP_OPERATOR_NAME();
Vector VSHIP_FAX_NO = dbcargo.getVSHIP_FAX_NO();
Vector VSHIP_TELEX_NO = dbcargo.getVSHIP_TELEX_NO();
Vector VSHIP_EMAIL = dbcargo.getVSHIP_EMAIL();
Vector VGROSS_TONNAGE = dbcargo.getVGROSS_TONNAGE();
Vector VCARGO_CAPACITY = dbcargo.getVCARGO_CAPACITY();
Vector VVOLUME_UNIT = dbcargo.getVVOLUME_UNIT();
Vector VPERCENTAGE_CAPACITY = dbcargo.getVPERCENTAGE_CAPACITY();
Vector VSHIP_ITEM = dbcargo.getVSHIP_ITEM();
Vector VSHIP_EFF_DT = dbcargo.getVSHIP_EFF_DT();
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
					<div class="d-flex justify-content-between">
						<div class="topheader">
							Vessel Master
						</div>
						<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#vesselModal" onclick="doClear();">Add New Vessel</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead id="tbsearch">
										<tr valign="top">
											<%if(write_access.equals("Y")){ %>
											<th >Select</th>
											<%}else{ %>
											<th>Sr#</th>
											<%} %>
											<th >Vessel Name</th>
											<th >Eff Date</th>
											<th >Call Sign</th>
											<th >Flag</th>
											<th >IMO No#</th>
											<th >Class SOC</th>
											<th >Inmarsat No#</th>
											<th >Vessel Owner</th>
											<th >Vessel Operator</th>
											<th >FAX No#</th>
											<th >Telex No#</th>
											<th >Vessel's Email</th>
											<th >Gross Tonnage</th>
											<th >Approx Cargo Capecity</th>
											<th >Items</th>
										</tr>
									</thead>
									<tbody>
									<%if(VSHIP_CD.size() > 0){ %>
										<%for(int i=0; i<VSHIP_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#vesselModal" 
													onclick="doModify('<%=VSHIP_CD.elementAt(i) %>','<%=VSHIP_NAME.elementAt(i)%>','<%=VSHIP_CALL_SIGN.elementAt(i)%>','<%=VSHIP_FLAG.elementAt(i)%>',
															'<%=VSHIP_IMO_NO.elementAt(i)%>','<%=VSHIP_CLASS_SOC.elementAt(i)%>','<%=VINMARSAT_NO.elementAt(i)%>','<%=VSHIP_OWNER_NAME.elementAt(i)%>',
															'<%=VSHIP_OPERATOR_NAME.elementAt(i)%>','<%=VSHIP_FAX_NO.elementAt(i)%>','<%=VSHIP_TELEX_NO.elementAt(i)%>',
															'<%=VSHIP_EMAIL.elementAt(i)%>','<%=VGROSS_TONNAGE.elementAt(i)%>','<%=VCARGO_CAPACITY.elementAt(i)%>',
															'<%=VVOLUME_UNIT.elementAt(i)%>','<%=VPERCENTAGE_CAPACITY.elementAt(i) %>','<%=VSHIP_ITEM.elementAt(i)%>','<%=VSHIP_EFF_DT.elementAt(i)%>')">
													</i>
												</font>
											</td>
											<td><%=VSHIP_NAME.elementAt(i)%></td>
											<td align="center"><%=VSHIP_EFF_DT.elementAt(i)%></td>
											<td><%=VSHIP_CALL_SIGN.elementAt(i)%></td>
											<td><%=VSHIP_FLAG.elementAt(i)%></td>
											<td><%=VSHIP_IMO_NO.elementAt(i)%></td>
											<td><%=VSHIP_CLASS_SOC.elementAt(i)%></td>
											<td><%=VINMARSAT_NO.elementAt(i)%></td>
											<td><%=VSHIP_OWNER_NAME.elementAt(i)%></td>
											<td><%=VSHIP_OPERATOR_NAME.elementAt(i)%></td>
											<td><%=VSHIP_FAX_NO.elementAt(i)%></td>
											<td><%=VSHIP_TELEX_NO.elementAt(i)%></td>
											<td><%=VSHIP_EMAIL.elementAt(i)%></td>
											<td align="right"><%=VGROSS_TONNAGE.elementAt(i)%></td>
											<td align="right"><%=VCARGO_CAPACITY.elementAt(i)%> 
												<%if(VVOLUME_UNIT.elementAt(i).equals("1")){ %>
													SCM
												<%}else if(VVOLUME_UNIT.elementAt(i).equals("2")){ %>
													MMSCM
												<%}else if(VVOLUME_UNIT.elementAt(i).equals("3")){ %>
													MT
												<%} %>@ <%=VPERCENTAGE_CAPACITY.elementAt(i) %>%</td>
											<td align="center"><%=VSHIP_ITEM.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="16" align="center">
												<%=utilmsg.infoMessage("<b>Ship List is not Available!</b>") %>
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
</div>
<div class="modal fade" id="vesselModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Vessel
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<label class="form-label"><b>Vessel Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_nm" value="" maxLength="100">
				      				<input type="hidden" class="form-control form-control-sm" name="ship_cd" value="">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Eff Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" id="eff_dt" maxLength="10" 
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
							<div class="form-group row">
				    			<label class="form-label"><b>Call Sign</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="ship_call_sign" value="" maxLength="40" >
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Flag<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="ship_flag" value="" maxLength="40" >
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>IMO No:<span class="s-red">*</span> </b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="imo_no" value="" maxLength="40">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Class SOC: </b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="class_soc" value="" maxLength="40">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Inmarsat No:</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm" name="inmarsat_no" value="" maxLength="40" >
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Item</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="input-group input-group-sm" >
								<input type="text" class="form-control form-control-sm" name="items" id="items" value=""  placeholder="LNG" maxLength="25">
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Vessel Owner</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="vessel_owner" value="" maxLength="100">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Vessel Operator</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="vessel_operator" value="" maxLength="100" >
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>FAX No:</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="fax_no" value="" maxLength="25" >
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Telex No:</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="telex_no" value="" maxLength="25" >
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Gross Tonnage</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="input-group input-group-sm" >
								<input type="text" class="form-control form-control-sm" name="gross_tonnage" id="gross_tonnage" value=""  onblur="checkValuePrecision(this.value, '2','11');">
								<span class="input-group-text"><b>Tonnes</b></span>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Approx Cargo Capecity</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-auto">
				    				<div class="input-group input-group-sm" >
					      				<input type="text" class="form-control form-control-sm" name="approx_cargo_cap" value="" maxLength="11" onblur="checkValuePrecision(this.value, '2','11');">
					    				<select class="form-select form-select-sm" name="app_cargo_capecity_unit">
			      							<option value="0">--Select--</option>
			      							<option value="1">SCM</option>
			      							<option value="2">MMSCM</option>
			      							<option value="3">MT</option>
			      						</select>
			      					</div>
				    			</div>
				    			<div class="col-auto">
									<span style="font-size: 14px;">@</span>
					    		</div>
					    		<div class="col">
									<div class="input-group input-group-sm" >
										<input type="text" class="form-control form-control-sm" maxLength="5" name="cap_per" id="cap_per" value=""  onblur="checkValuePrecision(this.value, '2','5');">
										<span class="input-group-text"><b>%</b></span>
									</div>
								</div>
					  		</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Vessel's Email</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="vessel_email" value="" maxLength="50">
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

<input type="hidden" name="option" value="VESSEL_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

<input type="hidden" name="form_clearance" value="KYC">
<input type="hidden" name="temp_pan_no" value="">

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
$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "Select")
		{
			//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});

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
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
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