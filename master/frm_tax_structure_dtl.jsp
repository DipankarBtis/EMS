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
		document.getElementById("status_flag").value="1";
	}
	else
	{
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="0";
	}
}

function setValue(obj,index)
{
	var tax_code = document.getElementById("tax_code"+index)
	var rate = document.getElementById("rate"+index)
	var tax_code_on = document.getElementById("tax_code_on"+index)
	var sap_tax_code_dtl = document.getElementById("sap_tax_code_dtl"+index)
	var sap_gl_dtl = document.getElementById("sap_gl_dtl"+index)
	
	if(obj.checked)
	{
		tax_code.disabled=false;
		rate.disabled=false;
		tax_code_on.disabled=false;
		
		sap_tax_code_dtl.disabled=false;
		sap_gl_dtl.disabled=false;
	}
	else
	{
		tax_code.disabled=true;
		rate.disabled=true;
		tax_code_on.disabled=true;
		
		sap_tax_code_dtl.disabled=true;
		sap_gl_dtl.disabled=true;
	}
}

function setTaxOn(obj,index)
{
	var tax_code_on = document.getElementById("tax_code_on"+index)
	if(obj.value=="2")
	{
		tax_code_on.readOnly=false;
	}
	else
	{
		tax_code_on.readOnly=true;
		tax_code_on.value="";
	}
}

function setDescription()
{
	var chk = document.forms[0].chk;
	var rate = document.forms[0].rate;
	var tax_code = document.forms[0].tax_code;
	var tax_code_on = document.forms[0].tax_code_on;
	var tax_alias = document.forms[0].tax_alias;
	
	var sap_tax_code_dtl = document.forms[0].sap_tax_code_dtl;
	var sap_gl_dtl = document.forms[0].sap_gl_dtl;
	
	var descr="";
	var descr_sap_taxcode="";
	var descr_sap_gl="";
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					if(descr == "")
					{
						descr += ""+tax_alias[i].value+" "+rate[i].value+"%";
					}
					else
					{
						descr += ", "+tax_alias[i].value+" "+rate[i].value+"%";
					}
					
					if(document.getElementById("tax_on2"+tax_code[i].value).checked ==true)
					{
						descr +=" on "+document.getElementById("tax_alias"+tax_code_on[i].value).value;
					}
					
					if(descr_sap_taxcode == "")
					{
						descr_sap_taxcode += ""+sap_tax_code_dtl[i].value+"";
					}
					else
					{
						descr_sap_taxcode += ", "+sap_tax_code_dtl[i].value+"";
					}
					
					if(descr_sap_gl == "")
					{
						descr_sap_gl += ""+sap_gl_dtl[i].value+"";
					}
					else
					{
						descr_sap_gl += ", "+sap_gl_dtl[i].value+"";
					}
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				if(descr == "")
				{
					descr += ""+tax_alias.value+" "+rate.value+"%";
				}
				else
				{
					descr += ", "+tax_alias.value+" "+rate.value+"%";
				}
				
				if(document.getElementById("tax_on2"+tax_code.value).checked ==true)
				{
					descr +=" on "+document.getElementById("tax_alias"+tax_code_on.value).value;
				}
				
				if(descr_sap_taxcode == "")
				{
					descr_sap_taxcode += ""+sap_tax_code_dtl.value+"";
				}
				else
				{
					descr_sap_taxcode += ", "+sap_tax_code_dtl.value+"";
				}
				
				if(descr_sap_gl == "")
				{
					descr_sap_gl += ""+sap_gl_dtl.value+"";
				}
				else
				{
					descr_sap_gl += ", "+sap_gl_dtl.value+"";
				}
			}
		}
	}
	
	document.forms[0].description.value=descr;
	document.forms[0].sap_tax_code.value=descr_sap_taxcode;
	document.forms[0].sap_gl.value=descr_sap_gl;
}

function doModify(actionFlag,tax_struct_cd,tax_struct_nm,tax_struct_app_dt,tax_struct_status,rmk,tax_category,sap_tax_code,sap_gl,pay_recv)
{
	document.forms[0].tax_struct_cd.value=tax_struct_cd;
	document.forms[0].description.value=tax_struct_nm;
	document.forms[0].eff_dt.value=tax_struct_app_dt;
	document.forms[0].remark.value=rmk;
	document.forms[0].sap_tax_code.value=sap_tax_code;
	document.forms[0].sap_gl.value=sap_gl;
	document.forms[0].pay_recv.value=pay_recv;
	
	if(tax_struct_status=='1')
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="1";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="0";
	}
	
	if(tax_category=="P")
	{
		document.forms[0].tax_category[0].checked=true;
	}
	else if(tax_category=="S")
	{
		document.forms[0].tax_category[1].checked=true;
	}
	
	if(actionFlag=="VIEW")
	{
		document.getElementById("submitId").style.display="none";
		document.forms[0].opration.value="VIEW";
	}
	else
	{
		document.getElementById("submitId").style.display="";
		document.forms[0].opration.value="MODIFY";
	}
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var description = document.forms[0].description.value
	var eff_dt = document.forms[0].eff_dt.value
	var sap_gl = document.forms[0].sap_gl.value
	var pay_recv = document.forms[0].pay_recv.value
	var u = document.forms[0].u.value;
	var chk = document.forms[0].chk;
	var rate = document.forms[0].rate;
	var tax_code = document.forms[0].tax_code;
	var tax_alias = document.forms[0].tax_alias;
	var tax_code_on = document.forms[0].tax_code_on;
	var sap_tax_code_dtl = document.forms[0].sap_tax_code_dtl;
	var sap_gl_dtl = document.forms[0].sap_gl_dtl;
	
	var msg="";
	var flag=true;
	
	if(trim(description) == "")
	{
		msg+="Enter Tax Structure!\n";
		flag=false;
	}
	if(trim(eff_dt) == "")
	{
		msg+="Enter Effectiv Date!\n";
		flag=false;
	}
	if(trim(pay_recv) == "")
	{
		msg+="Select Payable/Receivable!\n";
		flag=false;
	} 
	
	var count=parseInt("0");
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				if(chk[i].checked)
				{
					count++;
					if(trim(rate[i].value) == "")
					{
						msg+="Enter Rate for Tax Code#"+tax_code[i].value+"!\n";
						flag=false;
					}
					if(trim(sap_tax_code_dtl[i].value) == "")
					{
						msg+="Enter SAP TAX CODE for Tax Code#"+tax_code[i].value+"!\n";
						flag=false;
					}
					if(trim(sap_gl_dtl[i].value) == "")
					{
						msg+="Enter SAP GL for Tax Code#"+tax_code[i].value+"!\n";
						flag=false;
					}
					if(document.getElementById("tax_on2"+tax_code[i].value).checked==true)
					{
						if(trim(tax_code_on[i].value)=="")
						{
							msg+="Enter On Tax code for Tax Code#"+tax_code[i].value+"!\n";
							flag=false;
						}
					}
				}
			}
		}
		else
		{
			if(chk.checked)
			{
				count++;
				if(trim(rate.value) == "")
				{
					msg+="Enter Rate for Tax Code#"+tax_code.value+"!\n";
					flag=false;
				}
				if(trim(sap_tax_code_dtl.value) == "")
				{
					msg+="Enter SAP TAX CODE for Tax Code#"+tax_code.value+"!\n";
					flag=false;
				}
				if(trim(sap_gl_dtl.value) == "")
				{
					msg+="Enter SAP GL for Tax Code#"+tax_code.value+"!\n";
					flag=false;
				}
				if(document.getElementById("tax_on2"+tax_code.value).checked ==true)
				{
					if(trim(tax_code_on[i].value)=="")
					{
						msg+="Enter On Tax code for Tax Code#"+tax_code.value+"!\n";
						flag=false;
					}
				}
			}
		}
	}
	
	if(parseInt(count) == 0)
	{
		msg+="Please Select atleast ONE(1) Tax Detail!\n";
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

function doClear()
{
	document.forms[0].tax_struct_cd.value="";
	document.forms[0].description.value="";
	document.forms[0].eff_dt.value="";
	document.forms[0].remark.value="";
	document.forms[0].sap_tax_code.value="";
	document.forms[0].sap_gl.value="";
	document.forms[0].pay_recv.value="";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	
	document.forms[0].tax_category[0].checked=true;
	document.forms[0].tax_category[1].checked=false;
	
	var chk = document.forms[0].chk;
	var rate = document.forms[0].rate;
	var tax_code = document.forms[0].tax_code;
	var tax_code_on = document.forms[0].tax_code_on;
	var tax_alias = document.forms[0].tax_alias;
	var sap_tax_code_dtl = document.forms[0].sap_tax_code_dtl;
	var sap_gl_dtl = document.forms[0].sap_gl_dtl;
	
	if(chk!=null && chk!=undefined)
	{
		if(chk.length!=undefined)
		{
			for(var i=0;i<chk.length;i++)
			{
				chk[i].checked=false;
				rate[i].disabled=true;
				rate[i].value="";
				rate[i].style.background="";
				
				sap_tax_code_dtl[i].disabled=true;
				sap_tax_code_dtl[i].value="";
				sap_tax_code_dtl[i].style.background="";
				
				sap_gl_dtl[i].disabled=true;
				sap_gl_dtl[i].value="";
				sap_gl_dtl[i].style.background="";
				
				tax_code[i].disabled=true;
				
				document.getElementById("tax_on1"+tax_code[i].value).checked=true
				document.getElementById("tax_on2"+tax_code[i].value).checked=false
				
				tax_code_on[i].disabled=true;
				tax_code_on[i].readOnly=true;
				tax_code_on[i].value="";
				tax_code_on[i].style.background="";
			}
		}
		else
		{
			chk.checked=false;
			rate.disabled=true;
			rate.value="";
			rate.style.background="";
			
			sap_tax_code_dtl.disabled=true;
			sap_tax_code_dtl.value="";
			sap_tax_code_dtl.style.background="";
			
			sap_gl_dtl.disabled=true;
			sap_gl_dtl.value="";
			sap_gl_dtl.style.background="";
			
			tax_code.disabled=true;
			
			document.getElementById("tax_on1"+tax_code.value).checked=true
			document.getElementById("tax_on2"+tax_code.value).checked=false
			
			tax_code_on.disabled=true;
			tax_code_on.readOnly=true;
			tax_code_on.value="";
			tax_code_on.style.background="";
		}
	}	
	
	document.getElementById("submitId").style.display="";
	document.forms[0].opration.value="INSERT";
}

function exportToXls()
{	
	var url = "xls_tax_structure_dtl.jsp";
	
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
dbmaster.setCallFlag("TAX_STRUCTURE");
dbmaster.init();

Vector VTAX_STRUCT_CD = dbmaster.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = dbmaster.getVTAX_STRUCT_NM();
Vector VTAX_STRUCT_APP_DT = dbmaster.getVTAX_STRUCT_APP_DT();
Vector VTAX_STRUCT_STATUS = dbmaster.getVTAX_STRUCT_STATUS();
Vector VTAX_STRUCT_RMK = dbmaster.getVTAX_STRUCT_RMK();
Vector VPAY_RECV= dbmaster.getVPAY_RECV();
Vector VPAY_RECV_NM= dbmaster.getVPAY_RECV_NM();

Vector VMASTER_TAX_CATEGORY = dbmaster.getVMASTER_TAX_CATEGORY();
Vector VMASTER_TAX_CATEGORY_NM = dbmaster.getVMASTER_TAX_CATEGORY_NM();

Vector VTAX_CD = dbmaster.getVTAX_CD();
Vector VTAX_NM = dbmaster.getVTAX_NM();
Vector VTAX_ALIAS_NM = dbmaster.getVTAX_ALIAS_NM();
Vector VTAX_SHT_NM = dbmaster.getVTAX_SHT_NM();
Vector VTAX_APP_DT = dbmaster.getVTAX_APP_DT();
Vector VTAX_STATUS = dbmaster.getVTAX_STATUS();
Vector VTAX_CATEGORY = dbmaster.getVTAX_CATEGORY();
Vector VTAX_CATEGORY_NM = dbmaster.getVTAX_CATEGORY_NM();
Vector VSAP_TAX_CODE = dbmaster.getVSAP_TAX_CODE();
Vector VSAP_GL = dbmaster.getVSAP_GL();

Vector VINDEX = dbmaster.getVINDEX();
Vector VCOUNT = dbmaster.getVCOUNT();
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
							Tax Structure
						</div>
						<div>
					    	<div class="form-group row">
								
								<div class="col-auto">
				   	 				<span class="input-group-text">
									 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
									</span>	
								</div>
							</div> 					    
						</div>					  	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#myModal" onclick="doClear();">Add New Tax Structure</label>
							</div>
						</div>
					</div>
				</div>
				<%int i=0,k=0;
				for(int j=0; j<VMASTER_TAX_CATEGORY.size(); j++){ 
					int index = Integer.parseInt(""+VINDEX.elementAt(j));
				%>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VMASTER_TAX_CATEGORY_NM.elementAt(j) %></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>
										<th>Tax Structure<br>(Internal Code)</th>
										<th>
											Tax Structure
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Description" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>	
										</th>
										<th>Tax Category</th>
										<th>Commencement on</th>
										<th>SAP Tax Code</th>
										<th>SAP GL</th>
										<th>Status</th>
										<th>Payable / Receivable
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_pay_recv" onkeyup="Search(this,'8');" placeholder="Search.." style="width:100px"/></div>	
										</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody>
								<%k=0;
								if(index > 0){ %>
									<%for(i=i; i<VTAX_STRUCT_CD.size(); i++){ 
										k+=1;
									%>
									<tr>
										<td width="5%" align="center" title="<%if(Integer.parseInt(""+VCOUNT.elementAt(i))>0){ %>Entity Tax Structure Configured!<%}%>">
											<div align="center">
											<%if(Integer.parseInt(""+VCOUNT.elementAt(i))==0){ %>
												<font title="Click to Edit" style="color:var(--header_color)">
													<i class="fa fa-edit fa-lg" 
													data-bs-toggle="modal" data-bs-target="#myModal" 
													onclick="doClear();doModify('MODIFY','<%=VTAX_STRUCT_CD.elementAt(i)%>','<%=VTAX_STRUCT_NM.elementAt(i)%>','<%=VTAX_STRUCT_APP_DT.elementAt(i)%>',
													'<%=VTAX_STRUCT_STATUS.elementAt(i)%>','<%=VTAX_STRUCT_RMK.elementAt(i)%>','<%=VTAX_CATEGORY.elementAt(i)%>',
													'<%=VSAP_TAX_CODE.elementAt(i) %>','<%=VSAP_GL.elementAt(i) %>','<%=VPAY_RECV.elementAt(i) %>');
													fetchStructDtl('<%=VTAX_STRUCT_CD.elementAt(i)%>','<%=VTAX_STRUCT_APP_DT.elementAt(i)%>');"></i>
												</font>
											<%} else {%>
												<font title="Click to View">
													<i class="fa fa-eye fa-lg" 
													data-bs-toggle="modal" data-bs-target="#myModal" 
													onclick="doClear();doModify('VIEW','<%=VTAX_STRUCT_CD.elementAt(i)%>','<%=VTAX_STRUCT_NM.elementAt(i)%>','<%=VTAX_STRUCT_APP_DT.elementAt(i)%>',
													'<%=VTAX_STRUCT_STATUS.elementAt(i)%>','<%=VTAX_STRUCT_RMK.elementAt(i)%>','<%=VTAX_CATEGORY.elementAt(i)%>',
													'<%=VSAP_TAX_CODE.elementAt(i) %>','<%=VSAP_GL.elementAt(i)%>','<%=VPAY_RECV.elementAt(i) %>');
													fetchStructDtl('<%=VTAX_STRUCT_CD.elementAt(i)%>','<%=VTAX_STRUCT_APP_DT.elementAt(i)%>');"></i>	
												</font>											
											<%} %>
											</div>
										</td>
										<td align="center"><%=VTAX_STRUCT_CD.elementAt(i)%></td>
										<td><%=VTAX_STRUCT_NM.elementAt(i)%></td>
										<td align="center"><%=VTAX_CATEGORY_NM.elementAt(i)%></td>
										<td align="center"><%=VTAX_STRUCT_APP_DT.elementAt(i)%></td>
										<td align="center"><%=VSAP_TAX_CODE.elementAt(i)%></td>
										<td align="center"><%=VSAP_GL.elementAt(i)%></td>
										<td align="center">
											<div align="center">
												<font style="color:<%if(VTAX_STRUCT_STATUS.elementAt(i).equals("1")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VTAX_STRUCT_STATUS.elementAt(i).equals("1")){%>
												Active
												<%}else{ %>
												In-Active
												<%} %>
											</div>
										</td>
										<td align="center"><%=VPAY_RECV_NM.elementAt(i) %></td>
										<td><%=VTAX_STRUCT_RMK.elementAt(i)%></td>
									</tr>
									<%
										if(k==index)
										{
											i=i+1;
											break;
										}
									} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="10"><%=utilmsg.infoMessage("<b>No Tax Structure is Configured!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>	
				<%} %>
				<div class="card-footer cdfooter text-center">
					<font color="blue"><b>Note : Edit locked for Tax Structure Code in Use</b></font>
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
					Add/Modify Tax Structure Detail
				</div>
				<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Tax Category<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="radio" name="tax_category" value="P" checked>&nbsp;Product&nbsp;&nbsp;
									<input type="radio" name="tax_category" value="S">&nbsp;Service&nbsp;&nbsp;
				    			</div>
				    		</div>
				    	</div>				    							
				    </div>
      				<div class="row m-b-5">
      					<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Tax Structure<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="text" class="form-control form-control-sm" name="description" maxlength="200" readonly="readonly">
					    			<input type="hidden" class="form-control form-control-sm" name="tax_struct_cd" >
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Payable/Receivable<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
					    			<select class="form-select form-select-sm" name="pay_recv">
							    		<option value="">--Select--</option>
							    		<option value="P">Payable</option>
							    		<option value="R">Receivable</option>
							    	</select>					    			
				      			</div>
				  			</div>
						</div>
					</div>
      				<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Commencement on<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" id="eff_dt" maxLength="10" 
			      						onblur="validateDate(this);" 
			      						onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
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
									  	<input type="hidden" name="status_flag" id="status_flag" value="1">
									</div>
				      			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>SAP Tax Code</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input class="form-control form-control-sm"  name="sap_tax_code" readOnly>
				      			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>SAP GL<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input class="form-control form-control-sm"  name="sap_gl" readOnly>
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
				    				<textarea class="form-control form-control-sm"  name="remark" rows="1" maxlength="300"></textarea>
				      			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Select Component from Master Tax/s</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example1">
								<thead>
									<tr>
										<th></th>
										<th>Tax Code</th>
										<th>
											Tax Name
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Tax Name" onkeyup="Search1(this,'2');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>
											Alias
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Alias" onkeyup="Search1(this,'3');" placeholder="Search.." style="width:100px"/></div>
										</th>
										<th>Tax Abr.</th>
										<th>Rate (%)</th>
										<th>Tax On</th>
										<th>SAP Tax Code</th>
										<th>SAP GL</th>
									</tr>
								</thead>
								<tbody>
								<%if(VTAX_CD.size() > 0){ %>
									<%for(i=0; i<VTAX_CD.size(); i++){ %>
									<tr>
										<td width="5%" align="center">
											<input type="checkbox" name="chk" id="chk<%=VTAX_CD.elementAt(i)%>" class="form-check-input" onclick="setValue(this,'<%=VTAX_CD.elementAt(i)%>');setDescription()">
										</td>
										<td align="center">
											<%=VTAX_CD.elementAt(i)%>
											<input type="hidden" class="form-control form-control-sm" name="tax_code" id="tax_code<%=VTAX_CD.elementAt(i)%>" value="<%=VTAX_CD.elementAt(i)%>" disabled>
										</td>
										<td>
											<%=VTAX_NM.elementAt(i)%>
										</td>
										<td align="center">
											<%=VTAX_ALIAS_NM.elementAt(i)%>
											<input type="hidden" class="form-control form-control-sm" name="tax_alias" id="tax_alias<%=VTAX_CD.elementAt(i)%>" value="<%=VTAX_ALIAS_NM.elementAt(i)%>">
										</td>
										<td align="center"><%=VTAX_SHT_NM.elementAt(i)%></td>
										<td align="center">
											<div style="width:50px;">
												<input type="text" class="form-control form-control-sm" name="rate" id="rate<%=VTAX_CD.elementAt(i)%>"  onblur="checkNumber1(this,5,3);setDescription();" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:250px;">
												<div class="row m-b-5">
													<div class="col-auto">
														<input type="radio" name="tax_on<%=VTAX_CD.elementAt(i)%>" id="tax_on1<%=VTAX_CD.elementAt(i)%>" value="1" checked onclick="setTaxOn(this,'<%=VTAX_CD.elementAt(i)%>');setDescription();">&nbsp;On Value
													</div>
													<div class="col-auto">
														<input type="radio" name="tax_on<%=VTAX_CD.elementAt(i)%>" id="tax_on2<%=VTAX_CD.elementAt(i)%>" value="2" onclick="setTaxOn(this,'<%=VTAX_CD.elementAt(i)%>');setDescription();">&nbsp;On Tax Code
													</div>
													<div class="col">
														<input type="text" class="form-control form-control-sm" name="tax_code_on" id="tax_code_on<%=VTAX_CD.elementAt(i)%>" maxlength="6" disabled readOnly onblur="setDescription();">
													</div>
												</div>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input class="form-control form-control-sm"  name="sap_tax_code_dtl" id="sap_tax_code_dtl<%=VTAX_CD.elementAt(i)%>" maxlength="2" onblur="setDescription();" disabled>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input class="form-control form-control-sm"  name="sap_gl_dtl" id="sap_gl_dtl<%=VTAX_CD.elementAt(i)%>" maxlength="20" onblur="setDescription();" disabled>
											</div>
										</td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="9"><%=utilmsg.infoMessage("<b>No Tax is Configured!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer cdfooter" id="submitId">
        		<div class="d-flex justify-content-end">
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


<input type="hidden" name="option" value="TAX_STRUCTURE">
<input type="hidden" name="opration" value="INSERT">

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
  	table = document.getElementById("example1");
  	
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

function fetchStructDtl(struct_cd, struct_app_dt)
{
	if((trim(struct_cd) !="" && struct_cd!="0") && (trim(struct_app_dt) != ""))
	{
		document.getElementById("loading").style.visibility = "visible";
		$.post("../servlet/DB_Master_Ajax"+ "?struct_cd="+struct_cd+"&struct_app_dt="+struct_app_dt+"&setCallType=fetchTaxStructDtl", function(responseJson) 
		{
			//console.log(responseJson);
			$.each(responseJson, function(index, json) 
			{
				$.each(json.STRUCT_DTL, function(index_1, json_1) 
				{
					var chk = document.getElementById("chk"+json_1.TAX_CODE)
					var tax_code = document.getElementById("tax_code"+json_1.TAX_CODE)
					var rate = document.getElementById("rate"+json_1.TAX_CODE)
					var tax_code_on = document.getElementById("tax_code_on"+json_1.TAX_CODE)
					var sap_tax_code_dtl = document.getElementById("sap_tax_code_dtl"+json_1.TAX_CODE)
					var sap_gl_dtl = document.getElementById("sap_gl_dtl"+json_1.TAX_CODE)
					
					chk.checked=true;
					
					tax_code.disabled=false;
					rate.disabled=false;
					rate.value=json_1.FACTOR;
					rate.style.background="#99ffcc";
					
					sap_tax_code_dtl.disabled=false;
					sap_tax_code_dtl.value=json_1.SAP_TAX_CODE;
					sap_tax_code_dtl.style.background="#99ffcc";
					
					sap_gl_dtl.disabled=false;
					sap_gl_dtl.value=json_1.SAP_GL;
					sap_gl_dtl.style.background="#99ffcc";
					
					if(json_1.TAX_ON == "1")
					{
						document.getElementById("tax_on1"+json_1.TAX_CODE).checked=true
						document.getElementById("tax_on2"+json_1.TAX_CODE).checked=false
						
						tax_code_on.disabled=false;
						tax_code_on.readOnly=true;
						tax_code_on.value="";
					}
					else
					{
						document.getElementById("tax_on1"+json_1.TAX_CODE).checked=false
						document.getElementById("tax_on2"+json_1.TAX_CODE).checked=true
						
						tax_code_on.disabled=false;
						tax_code_on.readOnly=false;
						tax_code_on.value=json_1.TAX_ON_CD;
						tax_code_on.style.background="#99ffcc";
					}
				});
			});
		});
		document.getElementById("loading").style.display = "none";
	}
}
</script>
</html>