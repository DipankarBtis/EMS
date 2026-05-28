<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Arth patel 20250118 : Form for CheckPost-2-Customer Plant Link Master-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh(opration)
{
	var counterparty_cd = document.forms[0].customer_cd.value;
	var u = document.forms[0].u.value;
	var url = "cust_plant_check_post_link.jsp?opration="+opration+"&u="+u+"&counterparty_cd="+counterparty_cd;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	document.forms[0].customer_plant.disabled=false;
	document.forms[0].checkPost.disabled=false;
	//var customer_cd=document.forms[0].customer_cd.value;
	var checkPost=document.forms[0].checkPost.value;
	var customer_plant=document.forms[0].customer_plant.value;
	var eff_dt=document.forms[0].eff_dt.value;
	var deLink_trans = document.forms[0].deLink_trans.value;
	
	var msg="";
	var flag=true;
	
	/* if(trim(customer_cd)=="")
	{
		msg+="Select Customer!\n";
		flag=false;
	} */
	if(trim(customer_plant)=="")
	{
		msg+="Select Customer Plant!\n";
		flag=false;
	}
	if(trim(checkPost)=="")
	{
		msg+="Select CheckPost!\n";
		flag=false;
	}
	if(trim(eff_dt)=="")
	{
		msg+="Enter Effective date!\n";
		flag=false;
	}
	
	if(flag)
	{
		
		var a;
		if(deLink_trans=="Y")
		{
			var b = confirm("Do you want to DeLink CheckPost from Customer Plant ?");
			if(b)
			{
				a=confirm("CheckPost will Delink from customer Plant with Immediate Effect!\n\nDo you want to continue?");
			}
		}
		else
		{
			a = confirm("Do you want to "+opration+" the CheckPost To Customer Plant Link Details?");
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

function doModify(VLEGAL_ENTITY,VCOUNTPTY_CD,VLINKED_PLANT_SEQ,VLINKED_CHKPOST_CD,VEFF_DT)
{
	document.forms[0].comp_abbr.value=VLEGAL_ENTITY;
	//document.forms[0].customer_cd.value=VCOUNTPTY_CD;
	document.forms[0].customer_plant.value=VLINKED_PLANT_SEQ;
	document.forms[0].eff_dt.value=VEFF_DT;
	document.forms[0].last_eff_dt.value=VEFF_DT;
	document.forms[0].checkPost.value=VLINKED_CHKPOST_CD;
	document.forms[0].customer_plant.disabled=true;
	document.forms[0].checkPost.disabled=true;
	document.getElementById("chk_div").style.visibility='visible';
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	//document.forms[0].customer_cd.value="";
	document.forms[0].customer_plant.value="";
	document.forms[0].eff_dt.value="";
	document.forms[0].checkPost.value="";
	document.forms[0].customer_plant.disabled=false;
	document.forms[0].checkPost.disabled=false;
	document.getElementById("chk_div").style.visibility='hidden';
	document.forms[0].deLink_trans.checked=false;
	document.forms[0].last_eff_dt.value="";
	
	deLink_Trans();
	document.forms[0].opration.value="INSERT";
}

function deLink_Trans()
{
	var deLink_trans = document.forms[0].deLink_trans;
	var last_eff_dt = document.forms[0].last_eff_dt.value;
	if(deLink_trans.checked)
	{
		document.forms[0].deLink_trans.value="Y";
		document.getElementById("rel_dt").style.visibility='visible';
		document.forms[0].eff_dt.value=last_eff_dt;
		document.forms[0].eff_dt.readOnly=true;
		document.forms[0].eff_dt.style.pointerEvents = "none";
	}
	else
	{
		document.forms[0].deLink_trans.value="N";
		document.getElementById("rel_dt").style.visibility='hidden';
		document.forms[0].eff_dt.readOnly=false;
		document.forms[0].eff_dt.style.pointerEvents = "";
	}
}

function checkReleasedate()
{
	var release_dt = document.forms[0].release_dt.value;
	var eff_dt = document.forms[0].eff_dt.value;
	var sysdate = document.forms[0].sysdate.value;
	
	var splitrelease_dt = release_dt.split("/");
	var spliteff_dt = eff_dt.split("/");
	var splitsysdate = sysdate.split("/");
	
	var temp_release_dt = splitrelease_dt[2]+splitrelease_dt[1]+splitrelease_dt[0];
	var temp_eff_dt = spliteff_dt[2]+spliteff_dt[1]+spliteff_dt[0];
	var temp_sysdate = splitsysdate[2]+splitsysdate[1]+splitsysdate[0];
	
	if(release_dt!="")
	{
		if(temp_release_dt < temp_eff_dt)
		{
			alert("Release date ("+release_dt+") should be gretar than or equals to Eff date ("+eff_dt+") !");
			document.forms[0].release_dt.value="";
			return false;
		}
		if(temp_release_dt > temp_sysdate) //to comapre with System date....
		{
			alert("Release date ("+release_dt+") should be less than or equals to System date ("+sysdate+") !");
			document.forms[0].release_dt.value="";
			return false;
		}
	}
}

function selectCustomer()
{
	var customer_cd = document.forms[0].customer_cd.value;
	if(customer_cd=="0" || customer_cd=="")
	{
		alert("Select Customer first!");
	}
}

function validateEffDt()
{
	var eff_dt = document.forms[0].eff_dt.value;
	var sysdate = document.forms[0].sysdate.value;
	
	var spliteff_dt = eff_dt.split("/");
	var splitsysdate = sysdate.split("/");
	
	var temp_eff_dt = spliteff_dt[2]+spliteff_dt[1]+spliteff_dt[0];
	var temp_sysdate = splitsysdate[2]+splitsysdate[1]+splitsysdate[0];
	
	if(eff_dt!="")
	{
		if(temp_eff_dt > temp_sysdate) //to comapre with System date....
		{
			alert("Eff date ("+eff_dt+") should be less than or equals to System date ("+sysdate+") !");
			document.forms[0].eff_dt.value="";
			return false;
		}
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Master" id="dlngmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

dlngmaster.setCallFlag("CUST_PLANT_CHKPOST_LINK");
dlngmaster.setOpration(opration);
dlngmaster.setCounterparty_cd(counterparty_cd);
dlngmaster.setComp_cd(owner_cd);
dlngmaster.init();

String comp_abbr = dlngmaster.getComp_abbr();
Vector VCOUNTERPARTY_CD = dlngmaster.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlngmaster.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlngmaster.getVCOUNTERPARTY_ABBR();

Vector VPLANT_SEQ_NO = dlngmaster.getVPLANT_SEQ_NO();
Vector VPLANT_NAME = dlngmaster.getVPLANT_NAME();
Vector VPLANT_ABBR = dlngmaster.getVPLANT_ABBR();
Vector VCHECKPOST_CD = dlngmaster.getVCHECKPOST_CD();
Vector VCHECKPOST_NAME = dlngmaster.getVCHECKPOST_NAME();

Vector VLEGAL_ENTITY = dlngmaster.getVLEGAL_ENTITY();
Vector VCOUNTPTY_CD = dlngmaster.getVCOUNTPTY_CD();
Vector VCOUNTPTY_NM = dlngmaster.getVCOUNTPTY_NM();
Vector VENTITY_TYPE = dlngmaster.getVENTITY_TYPE();
Vector VLINKED_PLANT_SEQ = dlngmaster.getVLINKED_PLANT_SEQ();
Vector VLINKED_CHKPOST_CD = dlngmaster.getVLINKED_CHKPOST_CD();
Vector VEFF_DT = dlngmaster.getVEFF_DT();
Vector VLINKED_PLANT_NAME = dlngmaster.getVLINKED_PLANT_NAME();
Vector VLINKED_PLANT_ABBR = dlngmaster.getVLINKED_PLANT_ABBR();
Vector VLINKED_CHECKPOST_NM = dlngmaster.getVLINKED_CHECKPOST_NM();
Vector VDELINKED_CHECKPOST_NM = dlngmaster.getVDELINKED_CHECKPOST_NM();
Vector VDELINKED_PLANT_ABBR = dlngmaster.getVDELINKED_PLANT_ABBR();
Vector VDELINKED_PLANT_NAME = dlngmaster.getVDELINKED_PLANT_NAME();
Vector VDL_DURATION = dlngmaster.getVDL_DURATION();
Vector VDL_COUNTPTY_CD = dlngmaster.getVDL_COUNTPTY_CD();
Vector VDL_COUNTPTY_NM = dlngmaster.getVDL_COUNTPTY_NM();
Vector VDL_LEGAL_ENTITY = dlngmaster.getVDL_LEGAL_ENTITY();

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
				    		CheckPost To Customer Plant Link Master
	   	 				</div>
					 	<!-- <a href="../dlng/xls_truck_transport_mst.jsp?fileName=Truck Transporter Details.xls" download="Truck Transporter Details">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a> -->
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Customer<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="customer_cd" onchange="refresh('<%=opration%>');" id="select_box">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].customer_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Add CheckPost To Customer Plant Details</label>
					</div>
					<%if(write_access.equals("Y")){ %>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" 
									<%if(counterparty_cd.equals("0")){ %>
									onclick="selectCustomer();"
									<%}else{ %>
									data-bs-toggle="modal" 
									data-bs-target="#addFillingStation"
									<%} %>
									onclick="doClear();">Link CheckPost To Customer Plant </label>
							</div>
						</div>
					</div><%} %>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="linked_table">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Legal Entity</th>
										<th>Customer Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="lcname" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Plant Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="lname" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Plant ABBR<br><div align="center"><input class="form-control form-control-sm" type="text" id="labbr" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
										<th>CheckPost Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="lchkname" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Effective Date<br><div align="center"><input class="form-control form-control-sm" type="text" id="lst" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
									</tr>
								</thead>
								<tbody>
									<%if(VLINKED_CHKPOST_CD.size()>0){ %>
										<%for(int i=0; i<VLINKED_CHKPOST_CD.size(); i++){%>
										<tr>
											<td align="center">
												<%-- <%if(write_access.equals("Y")){ %>
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#addFillingStation" 
													onclick="getPlantlist('<%=VCOUNTPTY_CD.elementAt(i)%>','<%=owner_cd%>');doModify('<%=VLEGAL_ENTITY.elementAt(i)%>','<%=VCOUNTPTY_CD.elementAt(i)%>','<%=VLINKED_PLANT_SEQ.elementAt(i)%>','<%=VLINKED_CHKPOST_CD.elementAt(i)%>','<%=VEFF_DT.elementAt(i)%>');">
													</i>
												</font>
												<%} %> --%>
												<%=i+1 %>.
											</td>
											<td align="center"><%=VLEGAL_ENTITY.elementAt(i) %></td>
											<td align="center"><%=VCOUNTPTY_NM.elementAt(i) %></td>
											<td align="center"><%=VLINKED_PLANT_NAME.elementAt(i) %></td>
											<td align="center"><%=VLINKED_PLANT_ABBR.elementAt(i) %></td>
											<td align="center"><%=VLINKED_CHECKPOST_NM.elementAt(i) %></td>
											<td align="center"><%=VEFF_DT.elementAt(i) %></td>
										</tr>
										<%} %>
									<%}else{ %>
									<tr>
										<td colspan="7" align="center">
										<%=utilmsg.infoMessage("<b>No Checkpost linked to Plant!</b>")%>
										</td>
									</tr>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row" style="display: none;">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5" style="display: none;">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> DeLinked CheckPost To Customer Plant Details</label>
					</div>
					<div class="row m-b-5"  style="display: none;">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="delinked_table">
								<thead>
									<tr>
										<th>Sr No</th>
										<th>Legal Entity</th>
										<th>Customer Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="dcname" onkeyup="Search1(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Plant name<br><div align="center"><input class="form-control form-control-sm" type="text" id="dname" onkeyup="Search1(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Plant ABBR<br><div align="center"><input class="form-control form-control-sm" type="text" id="dabbr" onkeyup="Search1(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
										<th>CheckPost Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="dchknm" onkeyup="Search1(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Linked Duration<br><div align="center"><input class="form-control form-control-sm" type="text" id="dst" onkeyup="Search1(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
									</tr>
								</thead>
								<tbody>
									<%if(VDELINKED_CHECKPOST_NM.size()>0){ 
										int k=0;%>
										<%for(int i=0; i<VDELINKED_CHECKPOST_NM.size(); i++){ 
											k++;%>
										<tr>
											<td align="center"><%=k %></td>
											<td align="center"><%=VDL_LEGAL_ENTITY.elementAt(i) %></td>
											<td align="center"><%=VDL_COUNTPTY_NM.elementAt(i) %></td>
											<td align="center"><%=VDELINKED_PLANT_NAME.elementAt(i) %></td>
											<td align="center"><%=VDELINKED_PLANT_ABBR.elementAt(i) %></td>
											<td align="center"><%=VDELINKED_CHECKPOST_NM.elementAt(i) %></td>
											<td align="center"><%=VDL_DURATION.elementAt(i) %></td>
										</tr>
										<%} %>
									<%}else{ %>
									<tr>
										<td colspan="7" align="center">
										<%=utilmsg.infoMessage("<b>No Checkpost DeLinked to Plant!</b>")%>
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
<div class="modal fade" id="addFillingStation" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add CheckPost To Customer Plant Link 
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal" onclick="doClear()">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Legal Entity<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<input type="text" class="form-control form-control-sm" name="comp_abbr" value="<%=comp_abbr%>" readonly style="pointer-events:none">
						    	</div>
				  			</div>
						</div>
      				</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Plant<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="customer_plant" id="customer_plant" onblur="sameCheckpostPlant();">
										<option value="">--Select--</option>
										<%for(int i=0;i<VPLANT_SEQ_NO.size();i++){ %>
										<option value="<%=VPLANT_SEQ_NO.elementAt(i)%>"><%=VPLANT_NAME.elementAt(i)%> - <%=VPLANT_ABBR.elementAt(i)%></option>
										<%} %>
									</select>
				    			</div>
				  			</div>
						</div>
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>CheckPost<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="checkPost" onblur="sameCheckpostPlant()">
										<option value="">--Select--</option>
										<%for(int i=0;i<VCHECKPOST_CD.size();i++){ %>
										<option value="<%=VCHECKPOST_CD.elementAt(i)%>"><%=VCHECKPOST_NAME.elementAt(i)%></option>
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
			      						onblur="validateDate(this);" onchange="validateDate(this);validateEffDt();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div></div>
					</div>
					<div class="row m-b-5" id="chk_div" style="visibility: hidden">
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
								<label class="form-label">
									<input type="checkbox" class="form-check-input" name="deLink_trans" value="N" onClick="deLink_Trans()">&nbsp;<b>DeLink CheckPost</b>
								</label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" style="visibility: hidden" id="rel_dt">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
			      					<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="release_dt" value="<%=sysdate%>" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);checkReleasedate();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						<input type="hidden" name="last_release_dt" value="">
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

<input type="hidden" name="option" value="CUST_PLANT_CHKPOST_LINK">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="comp_cd" value="<%=owner_cd%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="last_eff_dt" value="">

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

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("linked_table");
  	
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

function Search1(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("delinked_table");
  	
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

/* function getPlantlist(cust_cd,comp_code)
{
	alert("Ajax Function...........")
	var customer_cd = cust_cd;
	var comp_cd = comp_code;
	var plant_seq;
	var plant_nm;
	var plant_abbr;
	$.post("../servlet/DB_Dlng_Ajax?customer_cd="+customer_cd+"&comp_cd="+comp_cd+"&setCallType=CUSTPLANTLIST", function(responseJson) {
		//console.log(responseJson);
		$.each(responseJson, function(index, json) {
			$.each(json.PLANT_DTL, function(index_1, json_1) {
				
				plant_seq=json_1.PLANT_SEQ;
				plant_nm=json_1.PLANT_NM;
				plant_abbr=json_1.PLANT_ABBR;
				
				const selectElement = document.getElementById('customer_plant');
				selectElement.options.length=0;
			    const seloption = document.createElement('option');
			    seloption.value = "";
			    seloption.textContent = "--Select--";
			    
			    selectElement.appendChild(seloption);
				alert("plant_seq..."+plant_seq)
				plant_seq.forEach((element,index) => 
			    {
		            const option = document.createElement('option');
		            option.value = trim(plant_seq[index]); // Set the value of the option to the truck code
		            option.textContent = trim(plant_nm[index]); // Set the text to the truck registration number
		           
		            selectElement.appendChild(option);
			    });
				
			});
		});
	});
	var a, i, options;
    a = document.getElementById("customer_plant");
    options = "";
    for (i = 0; i < a.length; i++) {
       options = options + "\n " + "("+a.options[i].value+")"+a.options[i].text;
    }
    alert("DropDown Options: "+options);
} */

function sameCheckpostPlant()
{
	var opration = document.forms[0].opration.value;
	
	var customer_cd = document.forms[0].customer_cd.value;
	var customer_plant = document.forms[0].customer_plant.value;
	var comp_cd = document.forms[0].comp_cd.value;
	var checkPost = document.forms[0].checkPost.value;
	
	var info="";
	
	$.post("../servlet/DB_Dlng_Ajax?setCallType=IsSameCustPlantCheckPost&customer_cd="+customer_cd+"&comp_cd="+comp_cd+"&checkPost="+checkPost+"&customer_plant="+customer_plant+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.CHECKPOSTPLANT, function(index_1, json_1) {
				if(parseInt(json_1.CUSTPLANT) > 0)
				{
					info+="Selected CheckPost Already Linked With Selected Plant";
					alert(info);
					document.forms[0].customer_plant.value="";
					document.forms[0].checkPost.value="";
				}
			});
		});
	});
	
	return info;
}
</script>

</form>
</body>
</html>