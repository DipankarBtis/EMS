<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Arth patel 20250102 : Form for Link Truck to Transporter-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var u = document.forms[0].u.value;
	
	var url = "frm_link_truck_transporter.jsp?u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var truck_reg_no = document.forms[0].truck_reg_no.value;
	var eff_dt = document.forms[0].eff_dt.value;
	var truck_trans_cd = document.forms[0].truck_trans_cd.value;
	var deLink_trans = document.forms[0].deLink_trans.value;
	var truck_eff_dt = document.forms[0].truck_eff_dt.value;

	var msg="";
	var flag=true;
	
	if(trim(truck_eff_dt)!="")
	{
		var isAllowedDt = compareDate(eff_dt,truck_eff_dt);
		
		if(parseInt(isAllowedDt)>=2)
		{
			msg+="Eff Date should be less than or equal to truck eff date "+truck_eff_dt+"!\n";
			flag=false;
		}
	}
	
	if(trim(truck_reg_no)=="")
	{
		msg+="Enter Truck Reg No.!\n";
		flag=false;
	}
	if(trim(truck_trans_cd)=="")
	{
		msg+="Select Truck Transporter!\n";
		flag=false;
	}
	if(trim(eff_dt)=="")
	{
		msg+="Enter Eff date!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a;
		if(deLink_trans=="Y")
		{
			var b = confirm("Do you want to DeLink truck from truck transporter ?");
			if(b)
			{
				a=confirm("By Delinking truck from truck transporter, the Driver linked to this Truck will be Delinked!\n\nDo you want to continue?");
			}
		}
		else
		{
			a = confirm("Do you want to "+opration+" the Truck Transporter Link Details?");
		}
		document.forms[0].truck_trans_cd.disabled=false;
		
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
	document.forms[0].truck_cd.value="";
	document.forms[0].truck_reg_no.value="";
	document.forms[0].truck_trans_cd.value="";
	document.forms[0].truck_trans_cd.disabled=false;
	document.forms[0].eff_dt.value="";
	document.forms[0].last_eff_dt.value="";
	document.forms[0].release_dt.value="";
	document.forms[0].remark.value="";
	document.forms[0].last_release_dt.value="";
	document.forms[0].deLink_trans.checked=false;
	document.forms[0].overwrite_flg.value="N";
	document.forms[0].link_seq_no.value="";
	document.forms[0].max_nom_dt.value="";
	document.getElementById("chk_div").style.visibility='hidden';
	deLink_Trans();
	document.getElementById("active_opt").style.display='block';
	document.getElementById("all_opt").style.display='none';
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
		checkMaxNomDt();
	}
	else
	{
		document.forms[0].deLink_trans.value="N";
		document.getElementById("rel_dt").style.visibility='hidden';
		document.forms[0].eff_dt.readOnly=false;
		document.forms[0].eff_dt.style.pointerEvents = "";
	}
}

function checkMaxNomDt()
{
	var release_dt = document.forms[0].release_dt.value;
	var max_nom_dt = document.forms[0].max_nom_dt.value;
	var sysdate = document.forms[0].sysdate.value;
	
	var splitrelease_dt = release_dt.split("/");
	var splitmax_nom_dt = max_nom_dt.split("/");
	var splitsysdate = sysdate.split("/");
	
	var temp_release_dt = splitrelease_dt[2]+splitrelease_dt[1]+splitrelease_dt[0];
	var temp_max_nom_dt = splitmax_nom_dt[2]+splitmax_nom_dt[1]+splitmax_nom_dt[0];
	var temp_sysdate = splitsysdate[2]+splitsysdate[1]+splitsysdate[0];
	
	if(release_dt!="" && max_nom_dt!="")
	{
		if(temp_release_dt <= temp_max_nom_dt)
		{
			alert("Release date ("+release_dt+") should be gretar than Last Nomination Date ("+max_nom_dt+") !");
			document.forms[0].release_dt.value="";
			return false;
		}
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

function validateLastReleaseDt()
{
	var last_release_dt = document.forms[0].last_release_dt.value;
	var eff_dt = document.forms[0].eff_dt.value;
	var sysdate = document.forms[0].sysdate.value;
	
	var splitlast_release_dt = last_release_dt.split("/");
	var spliteff_dt = eff_dt.split("/");
	var splitsysdate = sysdate.split("/");
	
	var temp_last_release_dt = splitlast_release_dt[2]+splitlast_release_dt[1]+splitlast_release_dt[0];
	var temp_eff_dt = spliteff_dt[2]+spliteff_dt[1]+spliteff_dt[0];
	var temp_sysdate = splitsysdate[2]+splitsysdate[1]+splitsysdate[0];
	
	if(last_release_dt!="")
	{
		if(temp_eff_dt < temp_last_release_dt)
		{
			alert("Eff date ("+eff_dt+") should be gretar than or equals to Last Release date ("+last_release_dt+") !");
			document.forms[0].eff_dt.value="";
			return false;
		}
		
	}
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
dlngmaster.setCallFlag("LINK_TRUCK_TRANS");
dlngmaster.setComp_cd(owner_cd);
dlngmaster.init();

Vector VTRUCK_TYPE = dlngmaster.getVTRUCK_TYPE();
Vector VEFF_DT = dlngmaster.getVEFF_DT();
Vector VSTATUS = dlngmaster.getVTRUCK_STATUS();
Vector VTRUCK_CD = dlngmaster.getVTRUCK_CD();
Vector VTRUCK_REG_NO = dlngmaster.getVTRUCK_REG_NO();
Vector VTRUCK_TRANS_CD = dlngmaster.getVTRUCK_TRANS_CD();
Vector VTRUCK_TRANS_NAME = dlngmaster.getVTRUCK_TRANS_NAME();
Vector VLAST_RELEASE_DT = dlngmaster.getVLAST_RELEASE_DT();

Vector VLINKED_TRUCK_TRANS_CD = dlngmaster.getVLINKED_TRUCK_TRANS_CD();
Vector VLINKED_TRUCK_TRANS_NAME = dlngmaster.getVLINKED_TRUCK_TRANS_NAME();
Vector VLINK_TRUCK_CD = dlngmaster.getVLINK_TRUCK_CD();
Vector VLINK_TRUCK_REG_NO = dlngmaster.getVLINK_TRUCK_REG_NO();
Vector VLINKED_EFF_DT = dlngmaster.getVLINKED_EFF_DT();
Vector VLINKED_RELEASE_DT = dlngmaster.getVLINKED_RELEASE_DT();
Vector VREMARK = dlngmaster.getVREMARK();
Vector VLINKED_TRUCK_STATUS = dlngmaster.getVLINKED_TRUCK_STATUS();
Vector VLINKED_LAST_RELEASE_DT = dlngmaster.getVLINKED_LAST_RELEASE_DT();
Vector VALLTRUCK_TRANS_CD = dlngmaster.getVALLTRUCK_TRANS_CD();
Vector VALLTRUCK_TRANS_NAME = dlngmaster.getVALLTRUCK_TRANS_NAME();
Vector VALLTRUCK_TRANS_ABBR = dlngmaster.getVALLTRUCK_TRANS_ABBR();
Vector VLINK_SEQ = dlngmaster.getVLINK_SEQ();
Vector VMAX_NOMINATION_DT = dlngmaster.getVMAX_NOMINATION_DT();
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
				    		Link Truck To Transporter
	   	 				</div>
					 	<!-- <a href="../dlng/xls_truck_transport_mst.jsp?fileName=Truck Transporter Details.xls" download="Truck Transporter Details">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a> -->
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading_linked">
										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse_linked" aria-expanded="false" aria-controls="collapse_linked">
										Linked Truck
							      		</button>
							    	</h2>
							    	<div id="collapse_linked" class="accordion-collapse collapse" aria-labelledby="heading_linked">
							      		<div class="accordion-body accor-body">
							        		<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered" id="example">
														<thead>
															<tr>
																<%if(write_access.equals("Y")){ %><th>DeLink/Modify</th><%} %>
																<th>Truck Reg. No.</th>
														    	<th>Transporter</th>			    		
														    	<th>Link Eff Date</th>
														    	<!-- <th>Release Date</th> -->
															</tr>
														</thead>
														<tbody id="mainTbody">
														<%
														if(VLINK_TRUCK_CD.size()>0){%>
															<%for(int i=0; i<VLINK_TRUCK_CD.size(); i++){%>
																<tr>
																	<%if(write_access.equals("Y")){ %><td align="center">
																		<%if(VLINKED_TRUCK_STATUS.elementAt(i).equals("Y")){ %>
																		<font title="Click to Edit" style="color:var(--header_color)">
																			<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#TruckModal" 
																			onclick="doModify('<%=VLINK_TRUCK_CD.elementAt(i)%>','<%=VLINKED_TRUCK_TRANS_CD.elementAt(i)%>','<%=VLINKED_EFF_DT.elementAt(i)%>'
																			,'<%=VLINKED_RELEASE_DT.elementAt(i)%>','<%=VREMARK.elementAt(i)%>','<%=VLINK_TRUCK_REG_NO.elementAt(i)%>','<%=VLINKED_LAST_RELEASE_DT.elementAt(i)%>'
																			,'<%=VLINKED_TRUCK_TRANS_NAME.elementAt(i)%>','<%=VLINK_SEQ.elementAt(i)%>','<%=VMAX_NOMINATION_DT.elementAt(i)%>');">
																			</i>
																		</font>
																		<%} %>
																	</td><%} %>
																	<td align="center">
																		<div align="center">
																			<font style="color:<%if(VLINKED_TRUCK_STATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
																				<i class="fa fa-circle fa-lg" ></i>
																				&nbsp;
																			</font>
																			<%=VLINK_TRUCK_REG_NO.elementAt(i)%>
																		</div>
																	</td>
							    									<td align="center"><%=VLINKED_TRUCK_TRANS_NAME.elementAt(i)%></td>
							    									<td align="center"><%=VLINKED_EFF_DT.elementAt(i)%></td>
							    									<%-- <td align="center"><%=VLINKED_RELEASE_DT.elementAt(i)%></td> --%>
																</tr>
															<%} %>
														<%}else{ %>
															<tr>
																<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>No Truck Linked To Transpoter!</b>") %></td>
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
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading_available">
										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse_available" aria-expanded="false" aria-controls="collapse_available">
										Available Truck
							      		</button>
							    	</h2>
							    	<div id="collapse_available" class="accordion-collapse collapse" aria-labelledby="heading_available">
							      		<div class="accordion-body accor-body">
							        		<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered" id="example">
														<thead>
															<tr>
																<%if(write_access.equals("Y")){ %><th>Link</th><%} %>
																<th>Truck Reg. No.</th>
														    	<th>Truck Type</th>			    		
														    	<th>Truck Eff Date</th>
															</tr>
														</thead>
														<tbody id="mainTbody">
														<%
														if(VTRUCK_CD.size()>0){%>
															<%for(int i=0; i<VTRUCK_CD.size(); i++){ 
															%>
																<tr>
																	<%if(write_access.equals("Y")){ %><td align="center">
																		<%if(VSTATUS.elementAt(i).equals("Y")){ %>
																		<font title="Click to Edit" style="color:var(--header_color)">
																			<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#TruckModal" 
																			onclick="doLink('<%=VTRUCK_CD.elementAt(i)%>','<%=VTRUCK_REG_NO.elementAt(i)%>','<%=sysdate%>','<%=VLAST_RELEASE_DT.elementAt(i)%>','<%=VEFF_DT.elementAt(i)%>');">
																			</i>
																		</font>
																		<%} %>
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
																</tr>
															<%} %>
														<%}else{ %>
															<tr>
																<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>No Truck Available To Link!</b>") %></td>
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
					Link Truck To Transporter
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
						    		<input type="text" class="form-control form-control-sm" name="truck_reg_no" value="" readonly>
						    		<input type="hidden" name="truck_cd" value="" >
						    	</div>
				  			</div>
						</div>
					</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Transporter<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<select class="form-select form-select-sm" name="truck_trans_cd" id="truck_trans_cd" onChange="checkforEffDtTransptrCd()"> 
				      					<option value="">--Select--</option>
				      					<%for(int i=0; i<VTRUCK_TRANS_CD.size(); i++){ %>
				      						<option value="<%=VTRUCK_TRANS_CD.elementAt(i)%>" id="active_opt"><%=VTRUCK_TRANS_NAME.elementAt(i)%></option>
				      					<%} %>
				      					<%for(int i=0; i<VALLTRUCK_TRANS_CD.size(); i++){ %>
				      						<option value="<%=VALLTRUCK_TRANS_CD.elementAt(i)%>" id="all_opt" style="display: none"><%=VALLTRUCK_TRANS_NAME.elementAt(i)%></option>
				      					<%} %>
				      				</select>
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
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="<%=sysdate %>" maxLength="10" 
			      						onblur="validateDate(this);checkforEffDtTransptrCd();" onchange="validateDate(this);validateLastReleaseDt();checkforEffDtTransptrCd();" autocomplete="off" >
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="truck_eff_dt" id="truck_eff_dt" value="">
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
				      				<input type="text" class="form-control form-control-sm" name="remark" value=""  maxlength="100">
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
									<input type="checkbox" class="form-check-input" name="deLink_trans" value="N" onClick="deLink_Trans()">&nbsp;<b>DeLink Transporter</b>
								</label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4" style="visibility: hidden" id="rel_dt">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
			      					<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="release_dt" value="<%=sysdate%>" maxLength="10" 
			      						onblur="validateDate(this);checkMaxNomDt();" onchange="validateDate(this);checkReleasedate();checkMaxNomDt();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						<input type="hidden" name="last_release_dt" value="">
		      						</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div id='doLink_effdt'></div>
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

<input type="hidden" name="option" value="LINK_TRUCK_TRANS">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="overwrite_flg" value="N">
<input type="hidden" name="link_seq_no" value="">
<input type="hidden" name="max_nom_dt" value="">
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
function checkforEffDtTransptrCd()
{
	var opration = document.forms[0].opration.value;
	
	var eff_dt = document.forms[0].eff_dt.value;
	var truck_trans_cd = document.forms[0].truck_trans_cd.value;
	var truck_cd = document.forms[0].truck_cd.value;
	
	var info="";
	
	$.post("../servlet/DB_Dlng_Ajax?setCallType=IsLinkedTruckTransEffDt&eff_dt="+eff_dt+"&truck_trans_cd="+truck_trans_cd+"&truck_cd="+truck_cd+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.LINKED_TRUCK_TRANS_EFFDT, function(index_1, json_1) {
				if(parseInt(json_1.TRANS_EFFDT) > 0)
				{
					info+="Truck has been Linked with transporter on same effective date earlier !\nDo you want to Link again?";
				}
				if(info!="")
				{
					var a=confirm(info);
					if(a)
					{
						document.forms[0].overwrite_flg.value="Y";
					}
					else
					{
						document.forms[0].eff_dt.value="";
					}
				}
			});
		});
	});
	
	return info;
}
function doLink(VTRUCK_CD,VTRUCK_REG_NO,sysdate,VLAST_RELEASE_D,VEFF_DT)
{
	var display;
	document.forms[0].truck_cd.value=VTRUCK_CD;
	document.forms[0].truck_reg_no.value=VTRUCK_REG_NO;
	document.forms[0].eff_dt.value=sysdate;
	document.forms[0].last_release_dt.value=VLAST_RELEASE_D;
	document.forms[0].truck_eff_dt.value=VEFF_DT;
	
	if(VLAST_RELEASE_D!="")
	{
		display = "<div align='center'><div class='container'><div class='alert alert-info'><i class='fa fa-info-circle fa-lg'></i>&nbsp;<b>Last Release Date:"+VLAST_RELEASE_D+"</b></div></div></div>";
	}
	else
	{
		display = "";
	}
	
	document.getElementById('doLink_effdt').innerHTML=display;
	
	document.forms[0].opration.value="INSERT";
}

function doModify(VLINK_TRUCK_CD,VLINKED_TRUCK_TRANS_CD,VLINKED_EFF_DT,VLINKED_RELEASE_DT,VREMARK,VTRUCK_REG_NO,VLINKED_LAST_RELEASE_D,truck_transe_name,link_seq_no,VMAX_NOMINATION_DT)
{
	document.forms[0].truck_cd.value=VLINK_TRUCK_CD;
	document.forms[0].truck_reg_no.value=VTRUCK_REG_NO;
	document.forms[0].eff_dt.value=VLINKED_EFF_DT;
	document.forms[0].last_eff_dt.value=VLINKED_EFF_DT;
	//document.forms[0].release_dt.value=VLINKED_RELEASE_DT;
	document.forms[0].remark.value=VREMARK;
	document.forms[0].last_release_dt.value=VLINKED_LAST_RELEASE_D;
	document.forms[0].truck_trans_cd.value=VLINKED_TRUCK_TRANS_CD;
	document.forms[0].link_seq_no.value=link_seq_no;
	document.forms[0].max_nom_dt.value=VMAX_NOMINATION_DT;
	document.forms[0].truck_trans_cd.disabled=true;
	document.getElementById("chk_div").style.visibility='visible';
	document.getElementById("active_opt").style.display='none';
	document.getElementById("all_opt").style.display='block';
	document.forms[0].opration.value="MODIFY";
	
	var display;
	if(VLINKED_LAST_RELEASE_D!="")
	{
		display = "<div align='center'><div class='container'><div class='alert alert-info'><i class='fa fa-info-circle fa-lg'></i>&nbsp;<b>Last Release Date:"+VLINKED_LAST_RELEASE_D+"</b></div></div></div>";
	}
	else
	{
		display = "";
	}
	
	document.getElementById('doLink_effdt').innerHTML=display;
}
</script>

</form>
</body>
</html>