<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--Arth patel 20250102 : Form for Link Driver to Truck-->
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var u = document.forms[0].u.value;
	
	var url = "frm_link_truck_driver.jsp?u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var driver_name = document.forms[0].driver_name.value;
	var eff_dt = document.forms[0].eff_dt.value;
	var truck_cd = document.forms[0].truck_cd.value;
	var deLink_trans = document.forms[0].deLink_trans.value;
	var msg="";
	var flag=true;
	
	if(trim(driver_name)=="")
	{
		msg+="Enter Diver Name.!\n";
		flag=false;
	}
	if(trim(truck_cd)=="")
	{
		msg+="Select Truck!\n";
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
			var b = confirm("Do you want to DeLink the Truck Driver Link Details?");
			if(b)
			{
				a=confirm("Do you want to continue?");
			}
		}
		else
		{
			a = confirm("Do you want to "+opration+" the Truck Driver Link Details?");
		}
		document.forms[0].truck_cd.disabled=false;
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

function doModify(VDRIVER_CD_LINKED,VDRIVER_NAME_LINKED,VTRUCK_CD_LINKED,VEFF_DT_LINKED,VRELEASE_DT_LINKED,VREMARK_LINKED,link_seq_no,VLINKED_LAST_RELEASE_DT)
{
	document.forms[0].driver_cd.value=VDRIVER_CD_LINKED;
	document.forms[0].driver_name.value=VDRIVER_NAME_LINKED;
	document.forms[0].truck_cd.value=VTRUCK_CD_LINKED;
	document.forms[0].eff_dt.value=VEFF_DT_LINKED;
	document.forms[0].last_eff_dt.value=VEFF_DT_LINKED;
	//document.forms[0].release_dt.value=VRELEASE_DT_LINKED;
	document.forms[0].remark.value=VREMARK_LINKED;
	document.forms[0].link_seq_no.value=link_seq_no;
	document.forms[0].truck_cd.disabled=true;
	document.forms[0].last_release_dt.value=VLINKED_LAST_RELEASE_DT;
	
	document.getElementById("chk_div").style.visibility='visible';
	
	document.forms[0].opration.value="MODIFY";
}

function doLink(VDRIVER_CD,VDRIVER_NAME,sysdate,VLAST_RELEASE_DT,VLINK_DRIVER_TRANS_EFF_DT)
{
	document.forms[0].driver_cd.value=VDRIVER_CD;
	document.forms[0].driver_name.value=VDRIVER_NAME;
	document.forms[0].eff_dt.value=sysdate;
	document.forms[0].last_release_dt.value=VLAST_RELEASE_DT;
	document.forms[0].driver_trans_eff_dt.value=VLINK_DRIVER_TRANS_EFF_DT;
	
	document.forms[0].opration.value="INSERT";
}

function doClear()
{
	document.forms[0].driver_cd.value="";
	document.forms[0].driver_name.value="";
	document.forms[0].truck_cd.value="";
	document.forms[0].eff_dt.value="";
	document.forms[0].last_eff_dt.value="";
	document.forms[0].release_dt.value="";
	document.forms[0].remark.value="";
	document.forms[0].truck_cd.disabled=false;
	document.forms[0].deLink_trans.checked=false;
	document.forms[0].link_seq_no.value="";
	document.forms[0].last_release_dt.value="";
	document.forms[0].overwrite_flg.value="N";
	document.forms[0].driver_trans_eff_dt.value="";
	document.getElementById("chk_div").style.visibility='hidden';
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
		if(temp_release_dt > temp_sysdate)
		{
			alert("Release date ("+release_dt+") should be less than or equals to System date ("+sysdate+") !");
			document.forms[0].release_dt.value="";
			return false;
		}
	}
}


function showTruck(VLINK_TRUCK_CD, VLINKED_TRUCK_REG_NO, VLINKED_TRUCK_TRANS_CD, trans_cd) 
{
	
	const selectElement = document.getElementById('truck_cd');
	selectElement.options.length=0;
    const seloption = document.createElement('option');
    seloption.value = "0";
    seloption.textContent = "--Select--";
    
    selectElement.appendChild(seloption);
	
	const transCdArray = VLINKED_TRUCK_TRANS_CD.replace(/^\[|\]$/g, '').split(','); // Use any appropriate delimiter
	const truckCdArray = VLINK_TRUCK_CD.replace(/^\[|\]$/g, '').split(','); // Use any appropriate delimiter
	const truckRegNoArray = VLINKED_TRUCK_REG_NO.replace(/^\[|\]$/g, '').split(','); // Use any appropriate delimiter
    const matchedTruckCd=[];
	const matchedTruckRegNo=[];
	
	transCdArray.forEach((element,index) => 
    {
    	var vector_trans_cd=trim(element);
        if (vector_trans_cd == trans_cd) 
        {
            const option = document.createElement('option');
            option.value = trim(truckCdArray[index]); // Set the value of the option to the truck code
            option.textContent = trim(truckRegNoArray[index]); // Set the text to the truck registration number
           
            selectElement.appendChild(option);
        } 
    });
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

function validateDriverTransEffDt()
{
	var driver_trans_eff_dt = document.forms[0].driver_trans_eff_dt.value;
	var eff_dt = document.forms[0].eff_dt.value;
	
	var splitdriver_trans_eff_dt = driver_trans_eff_dt.split("/");
	var spliteff_dt = eff_dt.split("/");
	
	var temp_driver_trans_eff_dt = splitdriver_trans_eff_dt[2]+splitdriver_trans_eff_dt[1]+splitdriver_trans_eff_dt[0];
	var temp_eff_dt = spliteff_dt[2]+spliteff_dt[1]+spliteff_dt[0];
	
	if(driver_trans_eff_dt!="")
	{
		if(temp_eff_dt < temp_driver_trans_eff_dt)
		{
			alert("Eff date ("+eff_dt+") should be gretar than or equals to Driver-Transporter Link date ("+driver_trans_eff_dt+") !");
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
<jsp:useBean class="com.etrm.fms.dlng.UtilBean_DLNG" id="utilBean_dlng" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
dlngmaster.setCallFlag("LINK_TRUCK_DRIVER");
dlngmaster.setComp_cd(owner_cd);
dlngmaster.init();

Vector VTRUCK_TRANS_CD = dlngmaster.getVTRUCK_TRANS_CD();
Vector VTRUCK_TRANS_NAME = dlngmaster.getVTRUCK_TRANS_NAME();
Vector VINDEX = dlngmaster.getVINDEX();

Vector VLINK_DRIVER_CD = dlngmaster.getVLINK_DRIVER_CD();
Vector VLINKED_DRIVER_STATUS = dlngmaster.getVLINKED_DRIVER_STATUS();
Vector VLINKED_DRIVER_LICENSE = dlngmaster.getVLINKED_DRIVER_LICENSE();
Vector VLINKED_DRIVER_LICENSE_DURATION = dlngmaster.getVLINKED_DRIVER_LICENSE_DURATION();
Vector VLINKED_DRIVER_NAME = dlngmaster.getVLINKED_DRIVER_NAME();
Vector VDRIVER_INDEX = dlngmaster.getVDRIVER_INDEX();
Vector VTRUCK_INDEX = dlngmaster.getVTRUCK_INDEX();
Vector VDRIVER_INDEX_LINKED = dlngmaster.getVDRIVER_INDEX_LINKED();
Vector VLINK_TRUCK_CD = dlngmaster.getVLINK_TRUCK_CD();
Vector VLINKED_TRUCK_REG_NO = dlngmaster.getVLINKED_TRUCK_REG_NO();
Vector VLINKED_TRUCK_TRANS_CD = dlngmaster.getVLINKED_TRUCK_TRANS_CD();

Vector VTRUCK_CD_LINKED = dlngmaster.getVTRUCK_CD_LINKED();
Vector VTRUCK_REG_NO_LINKED = dlngmaster.getVTRUCK_REG_NO_LINKED();
Vector VDRIVER_CD_LINKED = dlngmaster.getVDRIVER_CD_LINKED();
Vector VEFF_DT_LINKED = dlngmaster.getVEFF_DT_LINKED();
Vector VRELEASE_DT_LINKED = dlngmaster.getVRELEASE_DT_LINKED();
Vector VREMARK_LINKED = dlngmaster.getVREMARK_LINKED();
Vector VDRIVER_STATUS_LINKED = dlngmaster.getVDRIVER_STATUS_LINKED();
Vector VDRIVER_LICENSE_LINKED = dlngmaster.getVDRIVER_LICENSE_LINKED();
Vector VDRIVER_LICENSE_DURATION_LINKED = dlngmaster.getVDRIVER_LICENSE_DURATION_LINKED();
Vector VDRIVER_NAME_LINKED = dlngmaster.getVDRIVER_NAME_LINKED();
Vector VLINK_SEQ = dlngmaster.getVLINK_SEQ();
Vector VLINK_DRIVER_TRANS_EFF_DT = dlngmaster.getVLINK_DRIVER_TRANS_EFF_DT();
Vector VLINKED_LAST_RELEASE_DT = dlngmaster.getVLINKED_LAST_RELEASE_DT();
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
				    		Link Truck To Driver
	   	 				</div>
					 	<!-- <a href="../dlng/xls_truck_transport_mst.jsp?fileName=Truck Transporter Details.xls" download="Truck Transporter Details">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a> -->
				    </div>
				</div>
				<div class="card-body cdbody">
					<%int i=0,j=0,k=0,l=0,m=0,p=0,q=0,n=0;
					for(int a=0; a<VTRUCK_TRANS_CD.size(); a++)
					{ 
						String trans_cd=""+VTRUCK_TRANS_CD.elementAt(a);
						int index=Integer.parseInt(""+VINDEX.elementAt(a));
					%>
					&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTRUCK_TRANS_NAME.elementAt(a)%></label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading_linked<%=a%>">
										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse_linked<%=a%>" aria-expanded="false" aria-controls="collapse_linked<%=a%>">
										Linked Driver
							      		</button>
							    	</h2>
							    	<div id="collapse_linked<%=a%>" class="accordion-collapse collapse" aria-labelledby="heading_linked<%=a%>">
							      		<div class="accordion-body accor-body">
							        		<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered" id="example_linked">
														<thead>
															<tr>
																<%if(write_access.equals("Y")){ %><th>DeLink/Modify</th><%} %>
																<th>Driver Name</th>
														    	<th>Truck Reg. No.</th>
														    	<th>Driver License</th>
														    	<th>Driver License Duration</th>			    		
														    	<th>Link Eff Date</th>
														    	<th>Release Date</th>
															</tr>
														</thead>
														<tbody id="mainTbody">
														<%n=0;
														if(Integer.parseInt(""+VDRIVER_INDEX_LINKED.elementAt(a))>0){%>
															<%for(i=i; i<VDRIVER_CD_LINKED.size(); i++){ 
																//String truck_reg_no=utilBean_dlng.getTruckRegNo(""+VTRUCK_CD_LINKED.elementAt(i));
																int driver_index_linked=Integer.parseInt(""+VDRIVER_INDEX_LINKED.elementAt(a));
																n+=1;
															%>
																<tr>
																	<%if(write_access.equals("Y")){ %><td align="center">
																	<%if(VDRIVER_STATUS_LINKED.elementAt(i).equals("Y")){ %>
																		<font title="Click to Edit" style="color:var(--header_color)">
																			<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#TruckModal" 
																			onclick="showTruck('<%=VLINK_TRUCK_CD %>','<%=VLINKED_TRUCK_REG_NO %>','<%=VLINKED_TRUCK_TRANS_CD %>','<%=trans_cd %>');doModify('<%=VDRIVER_CD_LINKED.elementAt(i)%>','<%=VDRIVER_NAME_LINKED.elementAt(i)%>','<%=VTRUCK_CD_LINKED.elementAt(i)%>'
																			,'<%=VEFF_DT_LINKED.elementAt(i)%>','<%=VRELEASE_DT_LINKED.elementAt(i)%>','<%=VREMARK_LINKED.elementAt(i)%>','<%=VLINK_SEQ.elementAt(i)%>','<%=VLINKED_LAST_RELEASE_DT.elementAt(i)%>');">
																			</i>
																		</font>
																		<%} %>
																	</td><%} %>
																	<td align="center">
																		<div align="center">
																			<font style="color:<%if(VDRIVER_STATUS_LINKED.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
																				<i class="fa fa-circle fa-lg" ></i>
																				&nbsp;
																			</font>
																			<%=VDRIVER_NAME_LINKED.elementAt(i) %>
																		</div>
																	</td>
							    									<td align="center"><%=VTRUCK_REG_NO_LINKED.elementAt(i) %></td>
							    									<td align="center"><%=VDRIVER_LICENSE_LINKED.elementAt(i) %></td>
							    									<td align="center"><%=VDRIVER_LICENSE_DURATION_LINKED.elementAt(i) %></td>
							    									<td align="center"><%=VEFF_DT_LINKED.elementAt(i) %></td>
							    									<td align="center"><%=VRELEASE_DT_LINKED.elementAt(i) %></td>
																</tr>
																<%if(n==driver_index_linked)
																{
																	i=i+1;
																	break;
																} %>
															<%} %>
														<%}else{ %>
															<tr>
																<td colspan="7" align="center"><%=utilmsg.infoMessage("<b>No Driver Linked To Truck!</b>") %></td>
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
									<h2 class="accordion-header" id="heading_available<%=a%>">
										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse_available<%=a%>" aria-expanded="false" aria-controls="collapse_available<%=a%>">
										Available Driver
							      		</button>
							    	</h2>
							    	<div id="collapse_available<%=a%>" class="accordion-collapse collapse" aria-labelledby="heading_available<%=a%>">
							      		<div class="accordion-body accor-body">
							        		<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered" id="example">
														<thead>
															<tr>
																<%if(write_access.equals("Y")){ %><th>Link</th><%} %>
																<th>Driver Name</th>
														    	<th>License</th>
														    	<th>License Duration</th>
														    	<th>Driver-Transporter Link Date</th>			    		
															</tr>
														</thead>
														<tbody id="mainTbody">
														<%m=0;
														if(Integer.parseInt(""+VDRIVER_INDEX.elementAt(a))>0){%>
															<%for(p=p; p<VLINK_DRIVER_CD.size(); p++){ 
																int driver_index=Integer.parseInt(""+VDRIVER_INDEX.elementAt(a));
																m+=1;
															%>
																<tr>
																	<%if(write_access.equals("Y")){ %><td align="center">
																		<%if(VLINKED_DRIVER_STATUS.elementAt(p).equals("Y")){ %>
																			<font title="Click to Edit" style="color:var(--header_color)">
																				<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#TruckModal" 
																				onclick="showTruck('<%=VLINK_TRUCK_CD %>','<%=VLINKED_TRUCK_REG_NO %>','<%=VLINKED_TRUCK_TRANS_CD %>','<%=trans_cd %>');doLink('<%=VLINK_DRIVER_CD.elementAt(p)%>','<%=VLINKED_DRIVER_NAME.elementAt(p)%>','<%=sysdate%>','<%=VLAST_RELEASE_DT.elementAt(p)%>','<%=VLINK_DRIVER_TRANS_EFF_DT.elementAt(p)%>');">
																				</i>
																			</font>
																		<%} %>
																	</td><%} %>
																	<td align="center">
																		<div align="center">
																			<font style="color:<%if(VLINKED_DRIVER_STATUS.elementAt(p).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
																				<i class="fa fa-circle fa-lg" ></i>
																				&nbsp;
																			</font>
																			<%=VLINKED_DRIVER_NAME.elementAt(p)%>
																		</div>
																	</td>
							    									<td align="center"><%=VLINKED_DRIVER_LICENSE.elementAt(p)%></td>
							    									<td align="center"><%=VLINKED_DRIVER_LICENSE_DURATION.elementAt(p)%></td>
							    									<td align="center"><%=VLINK_DRIVER_TRANS_EFF_DT.elementAt(p)%></td>
																</tr>
																<%if(m==driver_index)
																{
																	p=p+1;
																	break;
																} %>
															<%}  %>
														<%}else{ %>
															<tr>
																<td colspan="5" align="center"><%=utilmsg.infoMessage("<b>No Driver Available To Link!</b>") %></td>
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
					<%} %>
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
					Link Truck To Driver
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
						    		<input type="text" class="form-control form-control-sm" name="driver_name" value="" readonly>
						    		<input type="hidden" name="driver_cd" value="" >
						    	</div>
				  			</div>
						</div>
					</div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Truck<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
						    	<div class="col-sm-12 col-xs-12 col-md-12">
						    		<select class="form-select form-select-sm" name="truck_cd" id="truck_cd" onchange="checkforEffDtTransptrCd()">
				      					<option value="">--Select--</option>
				      					<%-- <%int n=0,x=0;
				      					for(x=0; x<VLINK_TRUCK_CD.size(); x++){%>
				      						<option value="<%=VLINK_TRUCK_CD.elementAt(x)%>" id="truck_cd_option"><%=VLINKED_TRUCK_REG_NO.elementAt(x)%></option>
				      					<%} %> --%>
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
			      						onblur="validateDate(this);checkforEffDtTransptrCd();" onchange="validateDate(this);checkforEffDtTransptrCd();validateLastReleaseDt();validateDriverTransEffDt();" autocomplete="off" >
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
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
									<input type="checkbox" class="form-check-input" name="deLink_trans" value="N" onClick="deLink_Trans()">&nbsp;<b>DeLink Truck</b>
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

<input type="hidden" name="option" value="LINK_TRUCK_DRIVER">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="overwrite_flg" value="N">
<input type="hidden" name="link_seq_no" value="">
<input type="hidden" name="driver_trans_eff_dt" value="">
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
	var truck_cd = document.forms[0].truck_cd.value;
	var driver_cd = document.forms[0].driver_cd.value;
	
	var info="";
	
	$.post("../servlet/DB_Dlng_Ajax?setCallType=IsLinkedDriverTruckEffDt&eff_dt="+eff_dt+"&truck_cd="+truck_cd+"&driver_cd="+driver_cd+
			"&opration="+opration, 
		function(responseJson) {
		$.each(responseJson, function(index, json) {
			$.each(json.LINKED_DRIVER_TRUCK_EFFDT, function(index_1, json_1) {
				if(parseInt(json_1.TRUCK_EFFDT) > 0)
				{
					info+="Driver has been Linked with Truck on same effective date earlier !\nDo you want to Link again?";
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
</script>

</form>
</body>
</html>