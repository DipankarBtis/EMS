<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
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

function hide_show(id1,id2)
{	
	if(document.getElementById(id1).style.display=='none'){
		document.getElementById(id1).style.display='table-row-group';
		document.getElementById(id2).className='fa fa-compress';
	}else{
		document.getElementById(id1).style.display='none';
		document.getElementById(id2).className='fa fa-expand';
	}
	
	if(document.forms[0].prev_display.value != "" && document.forms[0].prev_display1.value !="")
	{
		if(document.forms[0].prev_display.value != id1 && document.forms[0].prev_display1.value != id2)
		{
			document.getElementById(document.forms[0].prev_display.value).style.display='none';
			document.getElementById(document.forms[0].prev_display1.value).className='fa fa-expand';
		}
	}
	document.forms[0].prev_display.value=id1;
	document.forms[0].prev_display1.value=id2;
}

function doClear()
{
	document.forms[0].transporter_cd.value="0";
	document.forms[0].counterparty_cd.value="0";
	document.forms[0].customer_plant_seq.value="0";
	document.forms[0].customer_code.value="";
	document.forms[0].effective_dt.value="";
	
	document.forms[0].transporter_cd.style.pointerEvents = "auto";
	document.forms[0].counterparty_cd.style.pointerEvents = "auto";
	document.forms[0].customer_plant_seq.style.pointerEvents = "auto";
	document.forms[0].effective_dt.style.pointerEvents = "auto";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status_flag").value="Y";
			
	document.forms[0].opration.value="INSERT";
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var transporter_cd = document.forms[0].transporter_cd.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var customer_plant_seq = document.forms[0].customer_plant_seq.value;
	var customer_code = document.forms[0].customer_code.value;
	var effective_dt = document.forms[0].effective_dt.value;
	var u = document.forms[0].u.value;
	var msg="";
	var flag=true;
	
	if(trim(transporter_cd) == "" || transporter_cd=="0")
	{
		msg+="Select Transporter!\n";
		flag=false;
	}
	if((trim(counterparty_cd) == "" || counterparty_cd=="0") && (trim(customer_plant_seq) == "" || customer_plant_seq=="0"))
	{
		msg+="Select Customer Plant!\n";
		flag=false;
	}
	if(opration=="MODIFY")
	{
		if(trim(customer_code) == "")
		{
			msg+="Customer Code No is missing!\n";
			flag=false;
		}
	}
	if(trim(effective_dt) == "")
	{
		msg+="Select Effecive Date!\n";
		flag=false;
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

function doModify(transporter_cd,counterparty_cd,customer_plant_seq,customer_code,effective_dt, status)
{
	
	document.forms[0].transporter_cd.value=transporter_cd;
	document.forms[0].counterparty_cd.value=counterparty_cd;
	document.forms[0].customer_code.value=customer_code;
	document.forms[0].effective_dt.value=effective_dt;	
	
	document.forms[0].transporter_cd.style.pointerEvents = "none";
	document.forms[0].counterparty_cd.style.pointerEvents = "none";
	document.forms[0].customer_plant_seq.style.pointerEvents = "none";
	document.forms[0].effective_dt.style.pointerEvents = "none";
	
	// For the JSON part
	var c_cd_obj=document.forms[0].counterparty_cd;	
	fetchCustPlantDtl(c_cd_obj)
	//alert(customer_plant_seq);
	window.setTimeout(function() 
	{
		document.forms[0].customer_plant_seq.value=customer_plant_seq;
	}, 500);
	
	if(status=="Y")
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

</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_MapMaster" id="dbmapmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String transporter_cd=request.getParameter("transporter_cd")==null?"0":request.getParameter("transporter_cd");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
//String customer_plant_seq=request.getParameter("customer_plant_seq")==null?"0":request.getParameter("customer_plant_seq");

dbmapmaster.setCallFlag("MAP_CUSTOMER_CODE");
dbmapmaster.setComp_cd(owner_cd);

dbmapmaster.setTransporter_cd(transporter_cd);
dbmapmaster.setCounterparty_cd(counterparty_cd);

dbmapmaster.init();


Vector VTRANSPORTER_CD = dbmapmaster.getVTRANSPORTER_CD();
Vector VTRANSPORTER_NM = dbmapmaster.getVTRANSPORTER_NM();
Vector VTRANSPORTER_ABBR = dbmapmaster.getVTRANSPORTER_ABBR();
Vector VCOUNTERPARTY_CD = dbmapmaster.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbmapmaster.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbmapmaster.getVCOUNTERPARTY_ABBR();
Vector VCUSTOMER_PLANT_SEQ = dbmapmaster.getVCUSTOMER_PLANT_SEQ();
Vector VCUSTOMER_PLANT_SEQ_NM = dbmapmaster.getVCUSTOMER_PLANT_SEQ_NM();

Vector VTRANSPTR_CD = dbmapmaster.getVTRANSPTR_CD();
Vector VTRANSPTR_NM = dbmapmaster.getVTRANSPTR_NM();
Vector VCOUNTERPTY_CD = dbmapmaster.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_NM = dbmapmaster.getVCOUNTERPTY_NM();
Vector VPLANT_SEQ = dbmapmaster.getVPLANT_SEQ();
Vector VPLANT_SEQ_NM = dbmapmaster.getVPLANT_SEQ_NM();
Vector VCUSTOMER_CODE = dbmapmaster.getVCUSTOMER_CODE();
Vector VEFF_DT = dbmapmaster.getVEFF_DT();
Vector VSTATUS = dbmapmaster.getVSTATUS();
Vector VINDEX = dbmapmaster.getVINDEX();

Vector VSUB_COUNTERPTY_NM = dbmapmaster.getVSUB_COUNTERPTY_NM();
Vector VSUB_PLANT_SEQ_NM = dbmapmaster.getVSUB_PLANT_SEQ_NM();
Vector VSUB_CUSTOMER_CODE = dbmapmaster.getVSUB_CUSTOMER_CODE();
Vector VSUB_EFF_DT = dbmapmaster.getVSUB_EFF_DT();
Vector VSUB_STATUS = dbmapmaster.getVSUB_STATUS();
Vector VSUB_INDEX = dbmapmaster.getVSUB_INDEX();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_MapMaster">
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
				    		Transporter Assigned Customer Code 
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#MapCustomerCodeModal" 
								onclick="doClear();">Add Customer Code</label>
							</div>
						</div>
					</div>
					<%if(VTRANSPTR_CD.size()>0){ %>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%int j=0,m=0;
						for(int i=0;i<VTRANSPTR_CD.size();i++){ 
							int index=Integer.parseInt(""+VINDEX.elementAt(i));
							String transCd=""+VTRANSPTR_CD.elementAt(i);
						%>
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading<%=i%>">
										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=i%>" aria-expanded="false" aria-controls="collapse<%=i%>">
							    			<%=VTRANSPTR_NM.elementAt(i)%>
							      		</button>
							    	</h2>
							    	<div id="collapse<%=i%>" class="accordion-collapse collapse" aria-labelledby="heading<%=i%>">
							      		<div class="accordion-body accor-body">
											<div class="row">
												<div class="col-sm-12 col-xs-12 col-md-12">
													<div class="table-responsive">
														<table class="table table-bordered" id="example<%=i%>">
															<thead>
																<tr>
																	<th></th>
																	<th>
																		Customer
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer<%=i%>" onkeyup="Search(this,'<%=i%>','1');" placeholder="Search.." style="width:100px"/></div>
																	</th>
																	<th>
																		Customer Plant
																		<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Customer Plant<%=i%>" onkeyup="Search(this,'<%=i%>','2');" placeholder="Search.." style="width:100px"/></div>
																	</th>				    		
															    	<th>Customer Code</th>
															    	<th>Effective Date</th>
															    	<th>Status</th>
															    </tr>
															</thead>
															<tbody>										
															<%int k=0;
															for(j=j;j<VCOUNTERPTY_CD.size();j++){ 
																k+=1;
																int sub_index=Integer.parseInt(""+VSUB_INDEX.elementAt(j));
															%>
																<tr>
																	<td align="center" >
																		<font title="Click to Edit" style="color:var(--header_color)">
																			<i class="fa fa-edit fa-lg" 
																			data-bs-toggle="modal" data-bs-target="#MapCustomerCodeModal" 
																			onclick="doClear();doModify('<%=transCd%>','<%=VCOUNTERPTY_CD.elementAt(j)%>',
																			'<%=VPLANT_SEQ.elementAt(j)%>','<%=VCUSTOMER_CODE.elementAt(j)%>',
																			'<%=VEFF_DT.elementAt(j)%>','<%=VSTATUS.elementAt(j)%>');"></i>
																		</font>
																	</td>
																	<td align="center" <%if(sub_index>0){ %>onclick="hide_show('tbody<%=j %>','hidCont<%=j%>');"<%} %>>
																		<%if(sub_index>0){ %>
																		<span id="hidCont<%=j%>" class="fa fa-expand" title="Click here to show Address Type"></span>
																		&nbsp;&nbsp;
																		<%} %>
																		<%=VCOUNTERPTY_NM.elementAt(j)%>
																	</td>
																	<td align="center"><%=VPLANT_SEQ_NM.elementAt(j)%></td>
																	<td align="center"><%=VCUSTOMER_CODE.elementAt(j)%></td>											
																	<td align="center"><%=VEFF_DT.elementAt(j)%></td>
																	<td align="center">
																		<div align="center">
																			<font style="color:<%if(VSTATUS.elementAt(j).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
																				<i class="fa fa-circle fa-lg" ></i>
																				&nbsp;
																			</font>
																			<%if(VSTATUS.elementAt(j).equals("Y")){%>
																			Active
																			<%}else{ %>
																			In-Active
																			<%} %>
																		</div>
																	</td>
																</tr>
																<%if(sub_index>0){int l=0;%>
																<tbody id="tbody<%=j%>" style="display:none;">
																	<%for(m=m; m<VSUB_COUNTERPTY_NM.size(); m++){ 
																	l+=1;
																	%>
																		<tr>
																			<%if(l==1){ %><td rowspan="<%=sub_index%>" style="background:white;"></td><%} %>
																			<td align="center"><%=VSUB_COUNTERPTY_NM.elementAt(m)%></td>
																			<td align="center"><%=VSUB_PLANT_SEQ_NM.elementAt(m)%></td>
																			<td align="center"><%=VSUB_CUSTOMER_CODE.elementAt(m)%></td>											
																			<td align="center"><%=VSUB_EFF_DT.elementAt(m)%></td>
																			<td align="center">
																				<div align="center">
																					<font style="color:<%if(VSUB_STATUS.elementAt(m).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
																						<i class="fa fa-circle fa-lg" ></i>
																						&nbsp;
																					</font>
																					<%if(VSUB_STATUS.elementAt(m).equals("Y")){%>
																					Active
																					<%}else{ %>
																					In-Active
																					<%} %>
																				</div>
																			</td>
																		</tr>
																	<%
																		if(l==sub_index)
																		{
																			m=m+1;
																			break;
																		}
																	} %>
																</tbody>
																<%} %>
																<%if(k==index){
																	j++;
																	break;
																} %>
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
						<%}%>
						</div>
					</div>
					<%}else{ %>
						<div align="center">
							<%=utilmsg.infoMessage("<b>No Transporter Customer Code Added!</b>") %>
						</div>
					<%} %>
				</div>
			</div>
		</div>
	</div>
</div>
									
<div class="modal fade" id="MapCustomerCodeModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
   		<div class="modal-content">
			<div class="modal-header cdheader">
	    		<div class="topheader">
					Add/Modify Customer Code
				</div>
	    		<input type="button" class="btn-close" data-bs-dismiss="modal">
	  		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Transporter<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="transporter_cd">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VTRANSPORTER_CD.size();i++){ %>
										<option value="<%=VTRANSPORTER_CD.elementAt(i)%>"><%=VTRANSPORTER_ABBR.elementAt(i)%> - <%=VTRANSPORTER_NM.elementAt(i)%></option>
										<%} %>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Customer<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="fetchCustPlantDtl(this);">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Customer Plant<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="customer_plant_seq">
										<option value="0">--Select--</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Customer Code<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm" name="customer_code" value="">
				    				</div>
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
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="effective_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(this,document.forms[0].end_dt,'F');" 
			      						onchange="validateDate(this);checkStartEndDate(this,document.forms[0].end_dt,'F');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
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
<script>
function fetchCustPlantDtl(obj)
{
	document.getElementById("loading").style.visibility = "visible";
	$.post("../servlet/DB_MapMaster_Ajax?setCallType=CUST_PLANT_DTL&counterparty_cd="+obj.value, function(responseJson) {
		console.log(responseJson);
		var option = "<option value='0'>---Select---</option>"	
		$.each(responseJson, function(index, json) {
			$.each(json.PLANT_DTL, function(index_1, json_1) {
				option+="<option value="+json_1.SEQ_NO+">"+json_1.PLANT_NM+"</option>"
			});
		});
		document.forms[0].customer_plant_seq.innerHTML=option;
	});
	document.getElementById("loading").style.visibility = "hidden";
}
</script>									
<input type="hidden" name="option" value="MAP_CUSTOMER_CODE_MST">
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
</form>
</body>

<script>
function Search(obj,tbl_id,indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+tbl_id);
  	
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