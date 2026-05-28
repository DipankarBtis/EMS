<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Arth patel 20250102 : Form for Truck Driver Master-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var u = document.forms[0].u.value;
	
	var url = "frm_truck_driver_mst.jsp?u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var driver_name = document.forms[0].driver_name.value;
	var driver_mobile = document.forms[0].driver_mobile.value;
	var driver_dob = document.forms[0].driver_dob.value;
	var license_no = document.forms[0].license_no.value;
	var license_type = document.forms[0].license_type.value;
	var license_from_dt = document.forms[0].license_from_dt.value;
	var license_to_dt = document.forms[0].license_to_dt.value;
	var license_iss_state = document.forms[0].license_iss_state.value;
	var eff_dt = document.forms[0].eff_dt.value;
	document.forms[0].link_trans.disabled=false;
	document.forms[0].truck_trans_cd.disabled=false;
	
	var msg="";
	var flag=true;
	
	if(trim(driver_name)=="")
	{
		msg+="Enter Drive Name!\n";
		flag=false;
	}
	/* if(trim(driver_mobile)=="")
	{
		msg+="Enter Mobile Number!\n";
		flag=false;
	} */
	if(trim(driver_dob)=="")
	{
		msg+="Enter Drive Date of Birth!\n";
		flag=false;
	}
	if(trim(eff_dt)=="")
	{
		msg+="Enter Eff date!\n";
		flag=false;
	}
	if(trim(license_no)=="")
	{
		msg+="Enter License No.!\n";
		flag=false;
	}
	if(trim(license_type)=="")
	{
		msg+="Enter License type!\n";
		flag=false;
	}
	if(trim(license_from_dt)=="")
	{
		msg+="Enter License From date!\n";
		flag=false;
	}
	if(trim(license_to_dt)=="")
	{
		msg+="Enter License to date!\n";
		flag=false;
	}
	if(trim(license_iss_state)=="")
	{
		msg+="Select License Issue State!\n";
		flag=false;
	}
	
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" the Driver Details?");
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

function doModify(VDRIVER_CD,VDRIVER_NAME,VSTATUS,VDRIVER_ADDR,VDRIVER_DOB,VDRIVER_MOBILE,VLICENSE_NO,VLICENSE_TYPE,
				VLICENSE_FROM_DT,VLICENSE_TO_DT,VLICENSE_ISSUE_STATE,VLICENSE_FILE_NAME,VLINKED_TRUCK_TRANS_CD,VLINKED_EFF_DT,VLINKED_FLAG,VEFF_DT,VLAST_RELEASE_DT)
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
	document.forms[0].driver_cd.value=VDRIVER_CD;
	document.forms[0].driver_name.value=VDRIVER_NAME;
	document.forms[0].driver_addr.value=VDRIVER_ADDR;
	document.forms[0].driver_dob.value=VDRIVER_DOB;
	document.forms[0].driver_mobile.value=VDRIVER_MOBILE;
	document.forms[0].license_no.value=VLICENSE_NO;
	document.forms[0].license_type.value=VLICENSE_TYPE;
	document.forms[0].license_from_dt.value=VLICENSE_FROM_DT;
	document.forms[0].license_to_dt.value=VLICENSE_TO_DT;
	document.forms[0].license_iss_state.value=VLICENSE_ISSUE_STATE;
	document.forms[0].status_flag.value=VSTATUS;
	document.forms[0].eff_dt.value=VEFF_DT;
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
	document.forms[0].driver_cd.value="";
	document.forms[0].driver_name.value="";
	document.forms[0].driver_addr.value="";
	document.forms[0].driver_dob.value="";
	document.forms[0].driver_mobile.value="";
	document.forms[0].license_no.value="";
	document.forms[0].license_type.value="";
	document.forms[0].license_from_dt.value="";
	document.forms[0].license_to_dt.value="";
	document.forms[0].license_iss_state.value="";
	document.forms[0].file_upload.value="";
	document.forms[0].eff_dt.value="";
	
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

function openPdfFile(url)
{
	window.open(url);
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
	
	if(file_upload.files.length == 0)
	{
	}
	else
	{
		if(opration)
		{
			var file_nm = document.getElementById('file_upload');
			if(file_nm.files.item(0).name.includes(".pdf"))
			{
			}
			else
			{
				var rejact = alert("Uploading file should be in .pdf format!!!!")
				document.forms[0].file_upload.value="";
			}
		}		
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
function exportToXls()
{
	var url = "xls_truck_driver_mst.jsp?fileName=DLNG Truck Driver Report.xls";

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Master" id="dlngmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.dlng.UtilBean_DLNG" id="utilBean_dlng" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
dlngmaster.setCallFlag("TRUCK_DRIVER_MST");
dlngmaster.init();

Vector VDRIVER_CD = dlngmaster.getVDRIVER_CD();
Vector VDRIVER_NAME = dlngmaster.getVDRIVER_NAME();
Vector VSTATUS = dlngmaster.getVDRIVER_STATUS();
Vector VDRIVER_ADDR = dlngmaster.getVDRIVER_ADDR();
Vector VDRIVER_DOB = dlngmaster.getVDRIVER_DOB();
Vector VDRIVER_MOBILE = dlngmaster.getVDRIVER_MOBILE();
Vector VLICENSE_NO = dlngmaster.getVLICENSE_NO();
Vector VLICENSE_TYPE = dlngmaster.getVLICENSE_TYPE();
Vector VLICENSE_FROM_DT = dlngmaster.getVLICENSE_FROM_DT();
Vector VLICENSE_TO_DT = dlngmaster.getVLICENSE_TO_DT();
Vector VLICENSE_ISSUE_STATE_NAME = dlngmaster.getVLICENSE_ISSUE_STATE_NAME();
Vector VLICENSE_ISSUE_STATE_CD = dlngmaster.getVLICENSE_ISSUE_STATE_CD();
Vector VLICENSE_FILE_NAME = dlngmaster.getVLICENSE_FILE_NAME();
Vector VSTATE_CD = dlngmaster.getVSTATE_CD();
Vector VSTATE_NM = dlngmaster.getVSTATE_NM();
Vector VTRUCK_TRANS_CD = dlngmaster.getVTRUCK_TRANS_CD();
Vector VTRUCK_TRANS_NAME = dlngmaster.getVTRUCK_TRANS_NAME();
Vector VTRUCK_TRANS_ABBR = dlngmaster.getVTRUCK_TRANS_ABBR();
Vector VLINKED_TRUCK_TRANS_CD = dlngmaster.getVLINKEDTRUCKTRANS();
Vector VLINKED_TRUCK_TRANS_NAME = dlngmaster.getVLINKEDTRUCKTRANSNAME();
Vector VLINKED_EFF_DT = dlngmaster.getVLINKEDEFFDT();
Vector VLINKED_FLAG = dlngmaster.getVLINKEDFLAG();
Vector VALLTRUCK_TRANS_CD = dlngmaster.getVALLTRUCK_TRANS_CD();
Vector VALLTRUCK_TRANS_NAME = dlngmaster.getVALLTRUCK_TRANS_NAME();
Vector VEFF_DT = dlngmaster.getVDRIVER_EFF_DT();
Vector VLAST_RELEASE_DT = dlngmaster.getVLAST_RELEASE_DT();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+"//DLNG//driver_license//";
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_DLNG_Master" enctype="multipart/form-data">
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
				    		Truck Driver Master
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
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#TruckDriverModal" onclick="doClear()">Add New Truck Driver</label>
							</div>
						</div>
					</div><%} %>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="truck_driver">
									<thead>
										<tr>
											<%if(write_access.equals("Y")){ %><th></th><%} %>
											<th>Driver Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="name" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Driver DOB<br><div align="center"><input class="form-control form-control-sm" type="text" id="dob" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Address</th>
									    	<th>Driver Mobile No</th>			    		
									    	<th>Effective Date</th>			    		
									    	<th>License No<br><div align="center"><input class="form-control form-control-sm" type="text" id="no" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
									    	<th>License Type<br><div align="center"><input class="form-control form-control-sm" type="text" id="typ" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>
									    	<th>License Period</th>
									    	<th>License Issue State<br><div align="center"><input class="form-control form-control-sm" type="text" id="st" onkeyup="Search(this,'9');" placeholder="Search.." style="width:100px"/></div></th>
									    	<th>Linked Transporter</th>
									    	<th>License File</th>
										</tr>
									</thead>
									<tbody id="mainTbody">
									<%int j=0;int k=0;
									if(VDRIVER_CD.size()>0){%>
										<%for(int i=0; i<VDRIVER_CD.size(); i++){ %>
											<tr>
												<%if(write_access.equals("Y")){ %>
												<td align="center">
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#TruckDriverModal" 
														onclick="doModify('<%=VDRIVER_CD.elementAt(i)%>','<%=VDRIVER_NAME.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>','<%=VDRIVER_ADDR.elementAt(i)%>'
														,'<%=VDRIVER_DOB.elementAt(i)%>','<%=VDRIVER_MOBILE.elementAt(i)%>','<%=VLICENSE_NO.elementAt(i)%>','<%=VLICENSE_TYPE.elementAt(i)%>'
														,'<%=VLICENSE_FROM_DT.elementAt(i)%>','<%=VLICENSE_TO_DT.elementAt(i)%>','<%=VLICENSE_ISSUE_STATE_CD.elementAt(i)%>','<%=VLICENSE_FILE_NAME.elementAt(i)%>'
														,'<%=VLINKED_TRUCK_TRANS_CD.elementAt(i)%>','<%=VLINKED_EFF_DT.elementAt(i)%>','<%=VLINKED_FLAG.elementAt(i)%>','<%=VEFF_DT.elementAt(i)%>','<%=VLAST_RELEASE_DT.elementAt(i)%>');">
														</i>
													</font>
												</td><%} %>
												<td align="center">
													<div align="center">
														<font style="color:<%if(VSTATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
															<i class="fa fa-circle fa-lg" ></i>
															&nbsp;
														</font>
														<%=VDRIVER_NAME.elementAt(i)%>
													</div>
												</td>
		    									<td align="center"><%=VDRIVER_DOB.elementAt(i)%></td>
		    									<td align="center"><%=VDRIVER_ADDR.elementAt(i)%></td>
		    									<td align="center"><%=VDRIVER_MOBILE.elementAt(i)%></td>
		    									<td align="center"><%=VEFF_DT.elementAt(i)%></td>
		    									<td align="center"><%=VLICENSE_NO.elementAt(i)%></td>
		    									<td align="center"><%=VLICENSE_TYPE.elementAt(i) %></td>
		    									<td align="center"><%=VLICENSE_FROM_DT.elementAt(i) %>-<%=VLICENSE_TO_DT.elementAt(i) %></td>
		    									<td align="center"><%=VLICENSE_ISSUE_STATE_NAME.elementAt(i) %></td>
		    									<td align="center"><%=VLINKED_TRUCK_TRANS_NAME.elementAt(i) %></td>
		    									
		    									<td align="center">	
		    									<%if(!VLICENSE_FILE_NAME.elementAt(i).equals("")){ %>																					
													<i class="fa fa-eye fa-2x" title="<%=VLICENSE_FILE_NAME.elementAt(i)%>"
													onclick="openPdfFile('<%=file_url%><%=VLICENSE_FILE_NAME.elementAt(i)%>')"
													>
													</i>
													<%} %>
													<%-- <%=VLICENSE_FILE_NAME.elementAt(i) %>		 --%>						
												</td>
											</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="9" align="center"><%=utilmsg.infoMessage("<b>No Truck Driver Data Available!</b>") %></td>
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
<div class="modal fade" id="TruckDriverModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Truck Driver Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="doClear()">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Driver Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<input type="text" class="form-control form-control-sm" name="driver_name" value=""  maxLength="100">
						    		<input type="hidden" name="driver_cd" value="" >
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
				    			<label class="form-label"><b>Mobile No.</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
			      					<div class="input-group input-group-sm" >
							    		<input type="text" class="form-control form-control-sm" name="driver_mobile" value=""  maxLength="10">
							    	</div>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Date of Birth<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="driver_dob" value="" maxLength="10" 
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
				    			<label class="form-label"><b>Address</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="driver_addr" value=""  maxLength="250">
				    			</div>
				  			</div>
						</div>
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
					</div>
					<div>
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
							&nbsp;
							</div>
						</div>
						<div class="row m-b-5 subheader">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="form-group row" align="left">
					    			<label class="form-label"><b>License Detail</b></label>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>License No.<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
										<input type="text" class="form-control form-control-sm" name="license_no" value="" onBlur="isExistLicenseNo()" maxLength="15">
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>License Type<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
					    				<input type="text" class="form-control form-control-sm" name="license_type" value="" maxLength="25" >
					    			</div>
					  			</div>
							</div>
						</div>
						<div class="row m-b-5">
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>License From Date<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
										<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="license_from_dt" value="" maxLength="10" 
				      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2">  
								<div class="form-group row">
					    			<label class="form-label"><b>License To Date<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
										<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="license_to_dt" value="" maxLength="10" 
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
					    			<label class="form-label"><b>License Issue State<span class="s-red">*</span></b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
										<select class="form-select form-select-sm" name="license_iss_state">
					      					<option value="">--Select--</option>
					      					<%for(int i=0; i<VSTATE_CD.size(); i++){ %>
					      						<option value="<%=VSTATE_CD.elementAt(i)%>"><%=VSTATE_NM.elementAt(i)%></option>
					      					<%} %>
					      				</select>
					    			</div>
					  			</div>
							</div>
							<div class="col-sm-2 col-xs-2 col-md-2" id="upload_field_lable">  
								<div class="form-group row">
					    			<label class="form-label"><b>Upload License</b></label>
					  			</div>
							</div>
							<div class="col-sm-4 col-xs-4 col-md-4" id="upload_field">  
								<div class="form-group row">
					    			<div class="col-sm-12 col-xs-12 col-md-12">
										<div class="input-group input-group-sm" >
					      					<input type="file" class="form-control form-control-sm" name="file_upload" id="file_upload" onclick="importData();" onchange="upload();" class="inputWrapper">
						      				<span class="input-group-text"><i class="fa fa-upload fa-lg"></i></span>
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

<input type="hidden" name="option" value="TRUCK_DRIVER_MST">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
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

<script>
function isExistLicenseNo()
{
	var opration = document.forms[0].opration.value;
	
	var driver_cd ="0";
	if(opration == "MODIFY")
	{
		driver_cd = document.forms[0].driver_cd.value;
	}
	var license_no = document.forms[0].license_no.value;
	
	var info="";
	
	$.post("../servlet/DB_Dlng_Ajax?setCallType=isExistLicenseNo&driver_cd="+driver_cd+"&license_no="+license_no+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.DRIVER_DTL, function(index_1, json_1) {
				if(parseInt(json_1.LICENSE_NO) > 0)
				{
					info+="License Number is already Exist!\n";
				}
				if(info!="")
				{
					alert(info)
					document.forms[0].license_no.value="";
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
  	table = document.getElementById("truck_driver");
  	
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
</form>
</body>
</html>