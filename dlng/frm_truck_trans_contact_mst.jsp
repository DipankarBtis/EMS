
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh(opration)
{
	var truck_trans_cd = document.forms[0].truck_trans_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_truck_trans_contact_mst.jsp?opration="+opration+"&u="+u+"&truck_trans_cd="+truck_trans_cd;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit()
{
	var truck_trans_cd = document.forms[0].truck_trans_cd.value;
	var person_name = document.forms[0].person_name.value;
	var eff_dt = document.forms[0].eff_dt.value;
	var designation = document.forms[0].designation.value;
	var email = document.forms[0].email.value;
	
	var opration = document.forms[0].opration.value;
	
	var address_type= document.forms[0].address_type;
	var count_addr_type=parseInt("0");
	
	var msg="";
	var flag=true;
	
	if(truck_trans_cd=="" || truck_trans_cd=="0" || trim(truck_trans_cd)=="")
	{
		msg+="Select Truck Transporter!\n";
		flag=false
	}
	if(person_name=="" || trim(person_name)=="")
	{
		msg+="Enter Person Name!\n";
		flag=false
	}
	if(eff_dt=="" || trim(eff_dt)=="")
	{
		msg+="Enter Eff Date!\n";
		flag=false
	}
	if(email=="" || trim(email)=="")
	{
		msg+="Enter Email-Id!\n";
		flag=false
	}
	
	if(address_type!=null && address_type.length!=undefined)
 	{
  		for(var i=0;i<address_type.length;i++)
  		{
 			if(address_type[i].checked)
   			{
 				count_addr_type=parseInt(count_addr_type)+1;
   			} 
  		} 
 	}
 	else if(address_type!=null)
 	{
 		if(address_type.checked)
   		{
 			count_addr_type=parseInt(count_addr_type)+1;
   		}
 	}
	
	if(parseInt(count_addr_type) == 0)
	{
		msg+="Select atleast one(1) address type for Submit!\n";
		flag=false
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" Truck transporter Contact Detail?")
		if(a)
		{
			document.forms[0].option.value="TRUCK_TRANS_CONTACT_DETAILS"
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
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
		document.getElementById("lb").innerHTML="Inactive";
		document.getElementById("status").value="N";
	}
}

function setActiveInactive2(obj,index)
{
	if(obj.checked)
	{
		document.getElementById("lb_flag"+index).innerHTML="Active";
		document.getElementById("flag"+index).value="Y";
	}
	else
	{
		document.getElementById("lb_flag"+index).innerHTML="Inactive";
		document.getElementById("flag"+index).value="N";
	}
}

function doModify(SEQ_NO,PERSON_NM,EFF_DT,DESIGNATION,STATUS,ADDR_FLAG,ADD_ADDRESS,PHONE,MOBILE,FAX1,FAX2,EMAIL,NOM,JT,INV,FM,PM,OTHER,RM,FLAG)
{
	document.forms[0].person_name.value=PERSON_NM;
	document.forms[0].seq_no.value=SEQ_NO;
	document.forms[0].eff_dt.value=EFF_DT;
	document.forms[0].designation.value=DESIGNATION;
	if(STATUS=='Y')
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status").value="Y";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="Inactive";
		document.getElementById("status").value="N";
	}
	
	var address_type= document.forms[0].address_type;
	var additional_address = document.forms[0].additional_address;
	var phone = document.forms[0].phone;
	var fax1 = document.forms[0].fax1;
	var fax2 = document.forms[0].fax2;
	
	var sepAddrFlag = ADDR_FLAG.split("@@");
	var sepAddrLine = ADD_ADDRESS.split("@@");
	var sepPhone = PHONE.split("@@");
	var sepFax1 = FAX1.split("@@");
	var sepFax2 = FAX2.split("@@");
	var sepFlag = FLAG.split("@@");
	
	if(address_type!=null && address_type.length!=undefined)
 	{
  		for(var i=0;i<address_type.length;i++)
  		{
   			for(var j=0;j<sepAddrFlag.length;j++)
   			{
     			if(address_type[i].value == sepAddrFlag[j])
     			{
     				address_type[i].checked = true;
     				address_type[i].style.pointerEvents = "none";
     				additional_address[i].value=sepAddrLine[j];
     				phone[i].value=sepPhone[j];
     				fax1[i].value=sepFax1[j];
     				fax2[i].value=sepFax2[j];
     				
     				if(sepFlag[j]=='Y')
     				{
     					document.forms[0].active_flag[i].checked=true;
     					document.getElementById("lb_flag"+i).innerHTML="Active";
     					document.getElementById("flag"+i).value="Y";
     				}
     				else
     				{
     					document.forms[0].active_flag[i].checked=false;
     					document.getElementById("lb_flag"+i).innerHTML="Inactive";
     					document.getElementById("flag"+i).value="N";
     				}
     				
     				enable_disable(address_type[i],i);
     			}
   			} 
  		} 
 	}
 	else if(address_type!=null)
 	{
   		for(var j=0;j<sepAddrFlag.length;j++)
   		{
   			if(address_type.value == sepAddrFlag[j])
     		{
   				address_type.checked = true;
   				address_type.style.pointerEvents = "none";
   				additional_address.value=sepAddrLine[j];
 				phone.value=sepPhone[j];
 				fax1.value=sepFax1[j];
 				fax2.value=sepFax2[j];
 				
 				if(sepFlag[j]=='Y')
 				{
 					document.forms[0].active_flag.checked=true;
 					document.getElementById("lb_flag0").innerHTML="Active";
 					document.getElementById("flag0").value="Y";
 				}
 				else
 				{
 					document.forms[0].active_flag.checked=false;
 					document.getElementById("lb_flag0").innerHTML="Inactive";
 					document.getElementById("flag0").value="N";
 				}
 				
 				enable_disable(address_type,"0");
     		}
   		} 
 	}
	
	document.forms[0].mobile.value=MOBILE;
	document.forms[0].email.value=EMAIL;
	if(NOM=="Y")
	{
		document.forms[0].nom.checked=true;
	}
	else
	{
		document.forms[0].nom.checked=false;	
	}
	
	if(INV=="Y")
	{
		document.forms[0].inv.checked=true;
	}
	else
	{
		document.forms[0].inv.checked=false;	
	}
	
	if(JT=="Y")
	{
		document.forms[0].jt.checked=true;
	}
	else
	{
		document.forms[0].jt.checked=false;	
	}
	
	if(FM=="Y")
	{
		document.forms[0].fm.checked=true;
	}
	else
	{
		document.forms[0].fm.checked=false;	
	}
	
	if(PM=="Y")
	{
		document.forms[0].pm.checked=true;
	}
	else
	{
		document.forms[0].pm.checked=false;	
	}
	
	if(OTHER=="Y")
	{
		document.forms[0].other.checked=true;
	}
	else
	{
		document.forms[0].other.checked=false;	
	}
	
	if(RM=="Y")
	{
		document.forms[0].rm.checked=true;
	}
	else
	{
		document.forms[0].rm.checked=false;	
	}
	
	document.forms[0].opration.value="MODIFY";
}

function doClear()
{
	document.forms[0].person_name.value="";
	document.forms[0].seq_no.value="";
	document.forms[0].eff_dt.value="";
	document.forms[0].designation.value="";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status").value="Y";
	
	document.forms[0].mobile.value="";
	document.forms[0].nom.checked=false;
	document.forms[0].inv.checked=false;
	document.forms[0].jt.checked=false;
	document.forms[0].fm.checked=false;	
	document.forms[0].pm.checked=false;
	document.forms[0].other.checked=false;	
	document.forms[0].rm.checked=false;
	
	var address_type = document.forms[0].address_type;
	var additional_address = document.forms[0].additional_address;
	var phone = document.forms[0].phone;
	var fax1 = document.forms[0].fax1;
	var fax2 = document.forms[0].fax2;
	var flag = document.forms[0].flag;
	
	if(address_type!=null && address_type.length!=undefined)
 	{
  		for(var i=0;i<address_type.length;i++)
  		{
  			address_type[i].checked=false;
  			address_type[i].style.pointerEvents = "auto";
  			additional_address[i].value="";
  			phone[i].value="";
  			fax1[i].value="";
  			fax2[i].value="";
  			
  			document.forms[0].active_flag[i].checked=true;
  			document.getElementById("lb_flag"+i).innerHTML="Active";
  			document.getElementById("flag"+i).value="Y";
  			
  			enable_disable(address_type[i],i);
  		} 
 	}
 	else if(address_type!=null)
 	{
 		address_type.checked=false;
 		address_type.style.pointerEvents = "auto";
		additional_address.value="";
		phone.value="";
		fax1.value="";
		fax2.value="";
		
		document.forms[0].active_flag.checked=true;
		document.getElementById("lb_flag0").innerHTML="Active";
		document.getElementById("flag0").value="Y";
		
		enable_disable(address_type,"0");
 	}
	
	document.forms[0].opration.value="INSERT";
}

function hide_show(id1,id2)
{	
	if(document.getElementById(id1).style.display=='none')
	{
		document.getElementById(id1).style.display='table-row-group';
		document.getElementById(id2).className='fa fa-compress';
	}
	else
	{
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

function enable_disable(obj,index)
{
	var additional_address = document.getElementById("additional_address"+index);
	var phone = document.getElementById("phone"+index);
	var fax1 = document.getElementById("fax1"+index);
	var fax2 = document.getElementById("fax2"+index);
	var flag = document.getElementById("flag"+index);
	
	if(obj.checked)
	{
		additional_address.disabled=false;
		phone.disabled=false;
		fax1.disabled=false;
		fax2.disabled=false;
		flag.disabled=false;
	}
	else
	{
		additional_address.disabled=true;
		phone.disabled=true;
		fax1.disabled=true;
		fax2.disabled=true;
		flag.disabled=true;
	}
}

function selectTransporter()
{
	var truck_trans_cd = document.forms[0].truck_trans_cd.value;
	if(truck_trans_cd=="0" || truck_trans_cd=="")
	{
		alert("Select Truck transporter!");
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Master" id="dlngmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String opration=request.getParameter("opration")==null?"INSERT":request.getParameter("opration");
String truck_trans_cd = request.getParameter("truck_trans_cd")==null?"0":request.getParameter("truck_trans_cd");

dlngmaster.setCallFlag("TRUCK_TRANS_CONTACT_DETAILS");
dlngmaster.setOpration(opration);
dlngmaster.setTruck_trans_cd(truck_trans_cd);
dlngmaster.setComp_cd(owner_cd);
dlngmaster.init();

Vector VTRUCK_TRANS_CD = dlngmaster.getVTRUCK_TRANS_CD();
Vector VTRUCK_TRANS_ABBR = dlngmaster.getVTRUCK_TRANS_ABBR();
Vector VTRUCK_TRANS_NAME = dlngmaster.getVTRUCK_TRANS_NAME();

Vector VADDRESS_TYPE = dlngmaster.getVADDRESS_TYPE();
Vector VADDRESS_NAME = dlngmaster.getVADDRESS_NAME();

Vector VSEQ_NO = dlngmaster.getVSEQ_NO();
Vector VCONTACT_EFF_DT = dlngmaster.getVCONTACT_EFF_DT();
Vector VPERSON_NM = dlngmaster.getVPERSON_NM();
Vector VDESIGNATION = dlngmaster.getVDESIGNATION();
Vector VPHONE = dlngmaster.getVPHONE();
Vector VMOBILE = dlngmaster.getVMOBILE();
Vector VFAX1 = dlngmaster.getVFAX1();
Vector VFAX2 = dlngmaster.getVFAX2();
Vector VEMAIL = dlngmaster.getVEMAIL();
Vector VADDR_FLAG = dlngmaster.getVADDR_FLAG();
Vector VADDR_TYPE = dlngmaster.getVADDR_TYPE();
Vector VADD_ADDRESS = dlngmaster.getVADD_ADDRESS();
Vector VNOM = dlngmaster.getVNOM();
Vector VINV = dlngmaster.getVINV();
Vector VJT = dlngmaster.getVJT();
Vector VFM = dlngmaster.getVFM();
Vector VPM = dlngmaster.getVPM();
Vector VOTHER = dlngmaster.getVOTHER();
Vector VRM = dlngmaster.getVRM();
Vector VCONTACT_STATUS = dlngmaster.getVCONTACT_STATUS();

Vector VINDEX = dlngmaster.getVINDEX();
Vector VFLAG = dlngmaster.getVFLAG();

Vector VTEMP_PHONE = dlngmaster.getVTEMP_PHONE();
Vector VTEMP_FAX1 = dlngmaster.getVTEMP_FAX1();
Vector VTEMP_FAX2 = dlngmaster.getVTEMP_FAX2();
Vector VTEMP_ADDR_FLAG = dlngmaster.getVTEMP_ADDR_FLAG();
Vector VTEMP_ADD_ADDRESS = dlngmaster.getVTEMP_ADD_ADDRESS();
Vector VTEMP_FLAG = dlngmaster.getVTEMP_FLAG();

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
							Truck Transporter Contact Master
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Truck Transporter<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="truck_trans_cd" onchange="refresh('<%=opration%>');" id="select_box">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VTRUCK_TRANS_CD.size();i++){ %>
										<option value="<%=VTRUCK_TRANS_CD.elementAt(i)%>"><%=VTRUCK_TRANS_ABBR.elementAt(i)%> - <%=VTRUCK_TRANS_NAME.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].truck_trans_cd.value="<%=truck_trans_cd%>"</script>
									
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
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Add/Modify Truck Transporter Contact Details</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" 
									<%if(truck_trans_cd.equals("0")){ %>
									onclick="selectTransporter();"
									<%}else{ %>
									data-bs-toggle="modal" 
									data-bs-target="#myModal"
									<%} %>
									onclick="doClear();">Add New Contact Detail</label>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="trans_contact">
								<thead>
									<tr>
										<th rowspan="2"></th>
										<th rowspan="2">Eff Date</th>
										<th rowspan="2">Status</th>
										<th rowspan="2">Person Name<br><div align="center"><input class="form-control form-control-sm" type="text" id="name" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Designation<br><div align="center"><input class="form-control form-control-sm" type="text" id="des" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Mobile</th>
										<th rowspan="2">Email<br><div align="center"><input class="form-control form-control-sm" type="text" id="mail" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Address Type</th>
										<th rowspan="2">Additional Address Line</th>
										<th rowspan="2">Phone</th>
										<th rowspan="2">Fax 1</th>
										<th rowspan="2">Fax 2</th>
										<th colspan="1">Notification Type</th>
									</tr>
									<tr>
										<th title="Nomination">NOM</th>
										<!-- <th title="Join Ticket/Allocation">JT/AO</th>
										<th title="Invoice">INV</th>
										<th title="Forced Measure">FM</th>
										<th title="Preventive Measure">PM</th>
										<th title="?">Other</th>
										<th title="Remittance">RM</th> -->
									</tr>
								</thead>
								<tbody>
								<%int j=0;int k=0;
								if(VSEQ_NO.size()>0){ %>
								<%for(int i=0;i<VSEQ_NO.size(); i++){ 
									int size = Integer.parseInt(""+VINDEX.elementAt(i));
								%>
									<tr>
										<td>
											<div align="center">
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" 
													data-bs-toggle="modal" data-bs-target="#myModal"
													onclick="doClear();doModify('<%=VSEQ_NO.elementAt(i)%>','<%=VPERSON_NM.elementAt(i)%>','<%=VCONTACT_EFF_DT.elementAt(i)%>',
													'<%=VDESIGNATION.elementAt(i)%>','<%=VCONTACT_STATUS.elementAt(i)%>','<%=VTEMP_ADDR_FLAG.elementAt(i)%>',
													'<%=VTEMP_ADD_ADDRESS.elementAt(i)%>','<%=VTEMP_PHONE.elementAt(i)%>','<%=VMOBILE.elementAt(i)%>',
													'<%=VTEMP_FAX1.elementAt(i)%>','<%=VTEMP_FAX2.elementAt(i)%>','<%=VEMAIL.elementAt(i)%>',
													'<%=VNOM.elementAt(i)%>','<%=VJT.elementAt(i)%>','<%=VINV.elementAt(i)%>',
													'<%=VFM.elementAt(i)%>','<%=VPM.elementAt(i)%>','<%=VOTHER.elementAt(i)%>','<%=VRM.elementAt(i)%>',
													'<%=VTEMP_FLAG.elementAt(i)%>');"></i>
												</font>
											</div>
										</td>
										<td align="center"><%=VCONTACT_EFF_DT.elementAt(i)%></td>
										<td align="center">
											<div align="center">
												<font style="color:<%if(VCONTACT_STATUS.elementAt(i).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VCONTACT_STATUS.elementAt(i).equals("Y")){%>
												Active
												<%}else{ %>
												Inactive
												<%} %>
											</div>
										</td>
										<td><%=VPERSON_NM.elementAt(i)%></td>
										<td><%=VDESIGNATION.elementAt(i)%></td>
										<td align="center"><%=VMOBILE.elementAt(i)%></td>
										<td><%=VEMAIL.elementAt(i)%></td>
										<td colspan="5" style="background:#bce6ff;" onclick="hide_show('tbody<%=i %>','hidCont<%=i%>');" valign="middle" align="left">
    										<span id="hidCont<%=i%>" class="fa fa-expand" title="Click here to show Address Type"></span>
    									</td>
										<td align="center"><%=VNOM.elementAt(i)%></td>
										<%-- <td align="center"><%=VJT.elementAt(i)%></td>
										<td align="center"><%=VINV.elementAt(i)%></td>
										<td align="center"><%=VFM.elementAt(i)%></td>
										<td align="center"><%=VPM.elementAt(i)%></td>
										<td align="center"><%=VOTHER.elementAt(i)%></td>
										<td align="center"><%=VRM.elementAt(i)%></td> --%>
									</tr>
									<%if(size>0){k=0;%>
									<tbody id="tbody<%=i%>" style="display:none;">
										<%for(j=j; j<VADDR_TYPE.size(); j++){ 
										k+=1;
										%>
											<tr>
												<%if(k==1){ %><td colspan="7" rowspan="<%=size%>" style="background:white;"></td><%} %>
												<td align="left">
													<font style="color:<%if(VFLAG.elementAt(j).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
														<i class="fa fa-circle fa-lg" ></i>
														&nbsp;
													</font>
													<%=VADDR_TYPE.elementAt(j)%>
												</td>
												<td><%=VADD_ADDRESS.elementAt(j)%></td>
												<td><%=VPHONE.elementAt(j)%></td>
												<td align="center"><%=VFAX1.elementAt(j)%></td>
												<td align="center"><%=VFAX2.elementAt(j) %></td>
												<%if(k==1){ %><td colspan="7" rowspan="<%=size%>" style="background:white;"></td><%}%>
											</tr>
										<%
											if(k==size)
											{
												j=j+1;
												break;
											}
										} %>
									</tbody>
									<%} %>
								<%} %>
								<%}else{ %>
									<tr>
										<td colspan="13" align="center">
										<%=utilmsg.infoMessage("<b>No Truck Transporter Contact Detail Configured!</b>")%>
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

<!-- MODEL -->
<div class="modal fade" id="myModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Add/Modify Truck Transporter Contact Details
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Person Name<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="person_name" value="" maxLength="100">
				      				<input type="hidden" class="form-control form-control-sm" name="seq_no" value="">
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
				      					<input type="hidden" name="old_plant_eff_dt" value="">
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" value="" 
			      						maxLength="10" onblur="validateDate(this);" 
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
				    			<label class="form-label"><b>Designation</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="designation" value="" maxLength="50">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Status</b></label>
				  			</div>
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
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Mobile</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="mobile" value="" maxLength="20">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Email<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<input type="text" class="form-control form-control-sm" name="email" value="" maxLength="40">
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Address Type</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th></th>
										<th>Address Type</th>
										<th>Additional Address Line</th>
										<th>Phone</th>
										<th>Fax 1</th>
										<th>Fax 2</th>
										<th>Flag</th>
									</tr>
								</thead>
								<tbody>
								<%for(int i=0; i<VADDRESS_TYPE.size(); i++){ %>
									<tr>
										<td align="center">
											<input type="checkbox" class="form-check-input" name="address_type" 
											value="<%=VADDRESS_TYPE.elementAt(i)%>" onclick="enable_disable(this,'<%=i%>')">
											
											<input type="hidden" name="to_nom" id="to_nom<%=i%>" disabled>
											<input type="hidden" name="to_inv" id="to_inv<%=i%>" disabled>
											<input type="hidden" name="to_fm" id="to_fm<%=i%>" disabled>
											<input type="hidden" name="to_pm" id="to_pm<%=i%>" disabled>
											<input type="hidden" name="to_jt" id="to_jt<%=i%>" disabled>
											<input type="hidden" name="to_other" id="to_other<%=i%>" disabled>
											<input type="hidden" name="to_rm" id="to_rm<%=i%>" disabled>
										</td>
										<td><b><%=VADDRESS_NAME.elementAt(i)%></b></td>
										<td align="center">
											<div style="width:300px;">
												<input type="text" class="form-control form-control-sm" name="additional_address" id="additional_address<%=i%>" value="" maxLength="100" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="phone" id="phone<%=i%>" value="" maxLength="20" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:150px;">
												<input type="text" class="form-control form-control-sm" name="fax1" id="fax1<%=i%>" value="" maxLength="20" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:150px;">
												<input type="text" class="form-control form-control-sm" name="fax2" id="fax2<%=i%>" value="" maxLength="20" disabled>
											</div>
										</td>
										<td align="center">
											<div class="form-check form-switch">
												<input class="form-check-input" name="active_flag" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="setActiveInactive2(this,'<%=i%>');">
											  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb_flag<%=i%>">
											  		Active
											  	</label>
											  	<input type="hidden" name="flag" id="flag<%=i%>" value="Y" disabled>
											</div>
										</td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Notification Type</label>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="form-group row">
								<div class="col-auto" align="center">
									<input type="checkbox" class="form-check-input" name="nom" value="Y">&nbsp;<b>NOM</b>
								</div>
								<div class="col-auto" align="center">
									<input type="hidden" class="form-check-input" name="jt" value="Y" disabled>&nbsp;<!-- <b>JT/AO</b> -->
								</div>
								<div class="col-auto" align="center">
									<input type="hidden" class="form-check-input" name="inv" value="Y" disabled>&nbsp;<!-- <b>INV</b> -->
								</div>
								<div class="col-auto" align="center">
									<input type="hidden" class="form-check-input" name="fm" value="Y" disabled>&nbsp;<!-- <b>FM</b> -->
								</div>
								<div class="col-auto" align="center">
									<input type="hidden" class="form-check-input" name="pm" value="Y" disabled>&nbsp;<!-- <b>PM</b> -->
								</div>
								<div class="col-auto" align="center">
									<input type="hidden" class="form-check-input" name="other" value="Y" disabled>&nbsp;<!-- <b>Other</b> -->
								</div>
								<div class="col-auto" align="center">
									<input type="hidden" class="form-check-input" name="rm" value="Y" disabled>&nbsp;<!-- <b>RM</b> -->
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

<input type="hidden" name="option" value="TRUCK_TRANS_CONTACT_DETAILS">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="old_value" value="">

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
<Script>
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("trans_contact");
  	
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
</Script>
</form>
</body>
</html>