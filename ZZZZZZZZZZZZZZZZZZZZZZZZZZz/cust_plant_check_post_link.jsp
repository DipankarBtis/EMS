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
	
	var checkPost=document.forms[0].checkPost.value;
	var customer_plant=document.forms[0].customer_plant.value;
	var eff_dt=document.forms[0].eff_dt.value;
	
	var msg="";
	var flag=true;
	
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
		var a = confirm("Do you want to "+opration+" the Fill Station Details?");
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

function doModify(VFILL_STATION_CD,VFILL_STATION_NM,VFILL_STATION_ABBR,VEFF_DT,VSTATUS)
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
	document.forms[0].fill_station_cd.value=VFILL_STATION_CD;
	document.forms[0].fill_station_name.value=VFILL_STATION_NM;
	document.forms[0].fill_station_abbr.value=VFILL_STATION_ABBR;
	document.forms[0].eff_dt.value=VEFF_DT;
	document.forms[0].status_flag.value=VSTATUS;
	
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	document.getElementById("flexSwitchCheckChecked").checked=true;
	document.forms[0].fill_station_name.value="";
	document.forms[0].fill_station_abbr.value="";
	document.forms[0].eff_dt.value="";
	
	document.forms[0].opration.value="INSERT";
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

function selectTrader()
{
	var customer_cd = document.forms[0].customer_cd.value;
	if(customer_cd=="0" || customer_cd=="")
	{
		alert("Select Customer first!");
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Master" id="dlngmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String comp_abbr = utilBean.getCompanyAbbr(owner_cd);

dlngmaster.setCallFlag("CUST_PLANT_CHKPOST_LINK");
dlngmaster.setOpration(opration);
dlngmaster.setCounterparty_cd(counterparty_cd);
dlngmaster.setComp_cd(owner_cd);
dlngmaster.init();

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
Vector VENTITY_TYPE = dlngmaster.getVENTITY_TYPE();
Vector VLINKED_PLANT_SEQ = dlngmaster.getVLINKED_PLANT_SEQ();
Vector VLINKED_CHKPOST_CD = dlngmaster.getVLINKED_CHKPOST_CD();
Vector VEFF_DT = dlngmaster.getVEFF_DT();
Vector VLINKED_PLANT_NAME = dlngmaster.getVLINKED_PLANT_NAME();
Vector VLINKED_PLANT_ABBR = dlngmaster.getVLINKED_PLANT_ABBR();
Vector VLINKED_CHECKPOST_NM = dlngmaster.getVLINKED_CHECKPOST_NM();

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
				    		CheckPost-2-Customer Plant Link Master
	   	 				</div>
					 	<!-- <a href="../dlng/xls_truck_transport_mst.jsp?fileName=Truck Transporter Details.xls" download="Truck Transporter Details">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a> -->
				    </div>
				</div>
				<div class="card-body cdbody">
				<%if(write_access.equals("Y")){ %>
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
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_NM.elementAt(i)%> - <%=VCOUNTERPARTY_ABBR.elementAt(i)%></option>
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Add/Modify CheckPost-2-Customer Plant Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" 
									<%if(counterparty_cd.equals("0")){ %>
									onclick="selectTrader();"
									<%}else{ %>
									data-bs-toggle="modal" 
									data-bs-target="#addFillingStation"
									<%} %>
									onclick="doClear();">Link CheckPost-2-Customer Plant </label>
							</div>
						</div>
					</div><%} %>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="filling_master">
								<thead>
									<tr>
										<th></th>
										<th>Legal Entity</th>
										<th>Plant name<br><div align="center"><input class="form-control form-control-sm" type="text" id="name" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Plant ABBR<br><div align="center"><input class="form-control form-control-sm" type="text" id="abbr" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
										<th>CheckPost Name</th>
										<th>Effective Date<br><div align="center"><input class="form-control form-control-sm" type="text" id="st" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
									</tr>
								</thead>
								<tbody>
									<%if(VLINKED_CHKPOST_CD.size()>0){ %>
										<%for(int i=0; i<VLINKED_CHKPOST_CD.size(); i++){ %>
										<tr>
											<%if(write_access.equals("Y")){ %><td align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#addFillingStation" 
													onclick="doModify();">
													</i>
												</font>
											</td><%} %>
											<td align="center"><%=VLEGAL_ENTITY.elementAt(i) %></td>
											<td align="center"><%=VLINKED_PLANT_NAME.elementAt(i) %></td>
											<td align="center"><%=VLINKED_PLANT_ABBR.elementAt(i) %></td>
											<td align="center"><%=VLINKED_CHECKPOST_NM.elementAt(i) %></td>
											<td align="center"><%=VEFF_DT.elementAt(i) %></td>
										</tr>
										<%} %>
									<%}else{ %>
									<tr>
										<td colspan="6" align="center">
										<%=utilmsg.infoMessage("<b>No Checkpost linked to Plant!</b>")%>
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
					Add/Modify CheckPost-2-Customer Plant Link 
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
				      				<select class="form-select form-select-sm" name="customer_plant" >
										<option value="0">--Select--</option>
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
				      				<select class="form-select form-select-sm" name="checkPost">
										<option value="0">--Select--</option>
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
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
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

<input type="hidden" name="option" value="CUST_PLANT_CHKPOST_LINK">
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

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filling_master");
  	
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