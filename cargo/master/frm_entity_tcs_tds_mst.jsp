<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh()
{
	var counterparty_cd ="0";
	counterparty_cd = document.forms[0].counterparty_cd.value;
	var tax_app = document.forms[0].tax_app.value;
	
	var entity_role = document.forms[0].entity_role.value;
	var prev_entity_role = document.forms[0].prev_entity_role.value;
	
	if(entity_role != prev_entity_role)
	{
		counterparty_cd ="0";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "frm_entity_tcs_tds_mst.jsp?u="+u+"&counterparty_cd="+counterparty_cd+"&entity_role="+entity_role+"&tax_app="+tax_app;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function openTaxStructMst(type)
{
	var tax_app = document.forms[0].tax_app.value;
	
	var newWindow;
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_tcs_tds_tax_structure_list.jsp?type="+type+"&tax_app="+tax_app,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_tcs_tds_tax_structure_list.jsp?type="+type+"&tax_app="+tax_app,"Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setTaxStructDetail(index,tax_struct_cd,tax_struct_nm,tax_struct_eff_dt,type,sap_tax_code)
{
	var entity_role = document.forms[0].entity_role.value;
	
	if(type=="P")
	{
		document.getElementById("sap_tax_code").value=sap_tax_code;
		document.getElementById("tax_struct_cd").value=tax_struct_cd;
		document.getElementById("tax_struct_nm").value=tax_struct_nm;
		document.getElementById("dis_tax_struct_nm").value=tax_struct_nm+" since "+tax_struct_eff_dt;
		document.getElementById("tax_struct_eff_dt").value=tax_struct_eff_dt;
	}
	else if(type=="S")
	{
		document.getElementById("ser_sap_tax_code").value=sap_tax_code;
		document.getElementById("ser_tax_struct_cd").value=tax_struct_cd;
		document.getElementById("ser_tax_struct_nm").value=tax_struct_nm;
		document.getElementById("dis_ser_tax_struct_nm").value=tax_struct_nm+" since "+tax_struct_eff_dt;
		document.getElementById("ser_tax_struct_eff_dt").value=tax_struct_eff_dt;
	}
}

function doSubmit(type)
{
	var msg="";
	var flag=true;
	
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var invoice_type = document.forms[0].invoice_type;
	var tax_struct_cd = document.forms[0].tax_struct_cd;
	var tax_struct_nm = document.forms[0].tax_struct_nm;
	var tax_struct_eff_dt = document.forms[0].tax_struct_eff_dt;
	var eff_dt = document.forms[0].eff_dt;
	var last_eff_dt = document.forms[0].last_eff_dt;
	
	var ser_invoice_type = document.forms[0].ser_invoice_type;
	var ser_tax_struct_cd = document.forms[0].ser_tax_struct_cd;
	var ser_tax_struct_nm = document.forms[0].ser_tax_struct_nm;
	var ser_tax_struct_eff_dt = document.forms[0].ser_tax_struct_eff_dt;
	var ser_eff_dt = document.forms[0].ser_eff_dt;
	
	if(counterparty_cd=="0" || trim(counterparty_cd)=="")
	{
		msg+="Select Counterparty!\n";
		flag=false
	}	
	
	if(type=="P")
	{
		if(eff_dt!=null && eff_dt!=undefined)
		{
			if(eff_dt.length!=undefined)
			{
				for(var i=0;i<eff_dt.length;i++)
				{
					if(trim(tax_struct_cd[i].value)!="" && trim(tax_struct_nm[i].value)!="")
					{
						if(trim(invoice_type[i].value)=="" || invoice_type[i].value=="0")
						{
							msg+="Select Invoice Type\n";
							flag=false;
						}
						if(trim(eff_dt[i].value)=="")
						{
							msg+="Select Eff Date\n";
							flag=false;
						}
					}
				}
			}
			else
			{
				if(trim(tax_struct_cd.value)!="" && trim(tax_struct_nm.value)!="")
				{
					if(trim(invoice_type.value)=="" || invoice_type.value=="0")
					{
						msg+="Select Invoice Type\n";
						flag=false;
					}
					if(trim(eff_dt.value)=="")
					{
						msg+="Select Eff Date\n";
						flag=false;
					}
				}
			}
		}
	}
	else if(type=="S")
	{
		if(ser_eff_dt!=null && ser_eff_dt!=undefined)
		{
			if(ser_eff_dt.length!=undefined)
			{
				for(var i=0;i<ser_eff_dt.length;i++)
				{
					if(trim(ser_tax_struct_cd[i].value)!="" && trim(ser_tax_struct_nm[i].value)!="")
					{
						if(trim(ser_invoice_type[i].value)=="" || ser_invoice_type[i].value=="0")
						{
							msg+="Select Invoice Type\n";
							flag=false;
						}
						if(trim(ser_eff_dt[i].value)=="")
						{
							msg+="Select Eff Date\n";
							flag=false;
						}
					}
				}
			}
			else
			{
				if(trim(ser_tax_struct_cd.value)!="" && trim(ser_tax_struct_nm.value)!="")
				{
					if(trim(ser_invoice_type.value)=="" || ser_invoice_type.value=="0")
					{
						msg+="Select Invoice Type\n";
						flag=false;
					}
					if(trim(ser_eff_dt.value)=="")
					{
						msg+="Select Eff Date\n";
						flag=false;
					}
				}
			}
		}
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit?")
		if(a)
		{
			document.forms[0].type.value=type;
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
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

function checkEffectiveDt(obj,obj1,obj2)
{
	var eff_dt = obj.value;
	var eff_dt1 = obj1.value;
	var eff_dt2 = obj2.value;
	
	var flag=true;
	
	if((eff_dt!="" && trim(eff_dt) != "" && eff_dt != null))
	{
		if((eff_dt1!="" && trim(eff_dt1) != "" && eff_dt1 != null))
		{
			var value = compareDate(eff_dt,eff_dt1);
			if(parseInt(value) == 2)
		  	{
		    	alert("Effective Date ("+eff_dt+") must be Greater Or Equal To Tax Applied Date ("+eff_dt1+")!");
		    	flag=false;
		  	}
			else
			{
				if((eff_dt2!="" && trim(eff_dt2) != "" && eff_dt2 != null))
				{
					var value1 = compareDate(eff_dt,eff_dt2);
					if(parseInt(value1) != 1)
				  	{
				    	alert("Effective Date ("+eff_dt+") must be Greater than Last Effective Date ("+eff_dt2+")!");
				    	flag=false;
				  	}	
				}
			}
		}
		else
		{
			if((eff_dt2!="" && trim(eff_dt2) != "" && eff_dt2 != null))
			{
				var value1 = compareDate(eff_dt,eff_dt2);
				if(parseInt(value1) != 1)
			  	{
					alert("Effective Date ("+eff_dt+") must be Greater than Last Effective Date ("+eff_dt2+")!");
			    	flag=false;
			  	}
			}
		}
	}
	
	return flag;
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Counterparty" id="dbcounterpty" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"C":request.getParameter("entity_role");
String tax_app = request.getParameter("tax_app")==null?"TCS":request.getParameter("tax_app");

dbcounterpty.setCallFlag("ENTITY_TCS_TDS_MASTER");
dbcounterpty.setCounterparty_cd(counterparty_cd);
dbcounterpty.setEntity_role(entity_role);
dbcounterpty.setComp_cd(owner_cd);
dbcounterpty.setTax_app(tax_app);
dbcounterpty.init();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();

Vector VTAX_STRUCT_CD = dbcounterpty.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = dbcounterpty.getVTAX_STRUCT_NM();
Vector VTAX_STRUCT_APP_DT = dbcounterpty.getVTAX_STRUCT_APP_DT();
Vector VTAX_STRUCT_RMK = dbcounterpty.getVTAX_STRUCT_RMK();
Vector VINVOICE_TYPE = dbcounterpty.getVINVOICE_TYPE();
Vector VINVOICE_TYPE_NM = dbcounterpty.getVINVOICE_TYPE_NM();
Vector VENTERED_BY = dbcounterpty.getVENTERED_BY();
Vector VEFF_DT = dbcounterpty.getVEFF_DT();
Vector VSAP_TAX_CODE = dbcounterpty.getVSAP_TAX_CODE();

Vector VTEMP_TAX_STRUCT_CD = dbcounterpty.getVTEMP_TAX_STRUCT_CD();
Vector VTEMP_TAX_STRUCT_NM = dbcounterpty.getVTEMP_TAX_STRUCT_NM();
Vector VTEMP_TAX_STRUCT_APP_DT = dbcounterpty.getVTEMP_SER_TAX_STRUCT_APP_DT();
Vector VTEMP_TAX_STRUCT_RMK = dbcounterpty.getVTEMP_TAX_STRUCT_RMK();
Vector VTEMP_INDEX = dbcounterpty.getVTEMP_INDEX();
Vector VTEMP_ENTERED_BY=dbcounterpty.getVTEMP_ENTERED_BY();
Vector VTEMP_EFF_DT = dbcounterpty.getVTEMP_EFF_DT();
Vector VTEMP_SAP_TAX_CODE = dbcounterpty.getVTEMP_SAP_TAX_CODE();

Vector VSER_TAX_STRUCT_CD = dbcounterpty.getVSER_TAX_STRUCT_CD();
Vector VSER_TAX_STRUCT_NM = dbcounterpty.getVSER_TAX_STRUCT_NM();
Vector VSER_TAX_STRUCT_APP_DT = dbcounterpty.getVSER_TAX_STRUCT_APP_DT();
Vector VSER_TAX_STRUCT_RMK = dbcounterpty.getVSER_TAX_STRUCT_RMK();
Vector VSER_INVOICE_TYPE = dbcounterpty.getVSER_INVOICE_TYPE();
Vector VSER_INVOICE_TYPE_NM = dbcounterpty.getVSER_INVOICE_TYPE_NM();
Vector VSER_ENTERED_BY = dbcounterpty.getVSER_ENTERED_BY();
Vector VSER_EFF_DT = dbcounterpty.getVSER_EFF_DT();
Vector VSER_SAP_TAX_CODE = dbcounterpty.getVSER_SAP_TAX_CODE();

Vector VTEMP_SER_TAX_STRUCT_CD = dbcounterpty.getVTEMP_SER_TAX_STRUCT_CD();
Vector VTEMP_SER_TAX_STRUCT_NM = dbcounterpty.getVTEMP_SER_TAX_STRUCT_NM();
Vector VTEMP_SER_TAX_STRUCT_APP_DT = dbcounterpty.getVTEMP_SER_TAX_STRUCT_APP_DT();
Vector VTEMP_SER_TAX_STRUCT_RMK = dbcounterpty.getVTEMP_SER_TAX_STRUCT_RMK();
Vector VTEMP_SER_INDEX = dbcounterpty.getVTEMP_SER_INDEX();
Vector VTEMP_SER_ENTERED_BY=dbcounterpty.getVTEMP_SER_ENTERED_BY();
Vector VTEMP_SER_EFF_DT = dbcounterpty.getVTEMP_SER_EFF_DT();
Vector VTEMP_SER_SAP_TAX_CODE = dbcounterpty.getVTEMP_SER_SAP_TAX_CODE();

String inv_type_name= "";

if(entity_role.equals("C"))
{
		
}
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_counterparty">

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
							Entity TCS/TDS Master
						</div>
					 	<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh()">
								<option value="0">Select Entity Roles</option>
								<option value="C">Customer</option>
				    			<option value="T">Trader</option>
				    			<option value="R">Transporter</option>
				    			<option value="V">Vessel Agent</option>
				    			<option value="H">Custom House Agent</option>
				    			<option value="S">Surveyor</option>
				    			<option value="G">Gas Exchange</option>
							</select>
						</div>
						<script>
							document.forms[0].entity_role.value="<%=entity_role%>"
						</script>						  	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
				    			<div class="col">
				      				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();" id="select_box">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<%if(entity_role.equals("B")){ %>
									<script>document.forms[0].counterparty_cd.style.pointerEvents = "none";</script>
									<%}%>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Tax Applicable</b></label>
								</div>
								<div class="col-auto">
									<div class="btn-group">
										<select class="btn btn-outline-secondary subbtngrp btnactive" name="tax_app" onchange="refresh()">
											<option value="TCS">TCS</option>
											<option value="TDS">TDS</option>
										</select>
									</div>
									<script>document.forms[0].tax_app.value="<%=tax_app%>"</script>
								</div>
				    		</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%if(counterparty_cd.equals("0") || counterparty_cd.equals("")){ %>
	&nbsp;
	<div class="card cardmain">
		<div class="card-body cdbody">
			<div align="center">
				<%=utilmsg.infoMessage("<b>Select Counterparty!</b>")%>
			</div>
		</div>
	</div>
	<%}else{ %>
	<%if(!entity_role.equals("G") && !entity_role.equals("R") && !entity_role.equals("H") && !entity_role.equals("S") && !entity_role.equals("V")){ %>
	&nbsp;
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							<%if(entity_role.equals("C")){ %>Customer
							<%}else if(entity_role.equals("T")){ %>Trader
							<%}else if(entity_role.equals("R")){ %>Transporter<%}%> 
							TCS/TDS Structure Details (<font style="background:#99ffcc;font-size:20px;font-weight:bold;">For Tax/Retail Invoice</font>)
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Invoice Type</th>
										<th>SAP Tax Code</th>
										<th>Tax Structure Details</th>
										<th>Eff Date</th>
										<th>Remark</th>
										<th>Select Tax Structure</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td align="center">
											<div style="width:150px;">
												<select class="form-select form-select-sm" name="invoice_type">
													<option value="0">--Select--</option>
													<%if(entity_role.equals("C")){ %>
													<option value="S">Invoice Seq. S</option>
													<option value="CR">Invoice Seq. CR</option>
													<option value="DR">Invoice Seq. DR</option>
													<option value="CCR">Invoice Seq. CCR</option>
													<option value="CDR">Invoice Seq. CDR</option>
													<option value="LP">Invoice Seq. LP</option>
													<%} else if(entity_role.equals("T")){ %>
													<option value="S">Commodity</option>
													<option value="CR">Credit Note</option>
													<option value="DR">Debit Note</option>
													<option value="LP">Late Payment</option>
													<%} %>
												</select>
												<%-- <%if(VSER_INVOICE_TYPE.size()==1){ %>
													<script>document.forms[0].ser_invoice_type.value="<%=VSER_INVOICE_TYPE.elementAt(i)%>"</script>
												<%}else{ %>
													<script>document.forms[0].ser_invoice_type[<%=i%>].value="<%=VSER_INVOICE_TYPE.elementAt(i)%>"</script>
												<%} %> --%>
											</div>
										</td>
										<td align="center">
											<div style="width:50px;">
												<input type="text" class="form-control form-control-sm" name="sap_tax_code" id="sap_tax_code" value="" readOnly style="background:<%//=VCOLOR.elementAt(i)%>">
												<input type="hidden" class="form-control form-control-sm" name="tax_struct_cd" id="tax_struct_cd" value="" readOnly style="background:<%//=VCOLOR.elementAt(i)%>">
											</div>
										</td>
										<td align="center">
											<div style="width:250px;">
												<input type="text" class="form-control form-control-sm" name="dis_tax_struct_nm" id="dis_tax_struct_nm" value="" readOnly style="background:<%//=VCOLOR.elementAt(i)%>">
												<input type="hidden" class="form-control form-control-sm" name="tax_struct_nm" id="tax_struct_nm" value="" readOnly style="background:<%//=VCOLOR.elementAt(i)%>">
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="hidden" name="last_eff_dt" id="last_eff_dt" value="" style="background:<%//=VCOLOR.elementAt(i)%>">
												<input type="hidden" class="form-control form-control-sm" name="tax_struct_eff_dt" id="tax_struct_eff_dt" value="" readOnly style="background:<%//=VCOLOR.elementAt(i)%>">
												
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" id="eff_dt" 
													value="" style="background:<%//=VCOLOR.elementAt(i)%>" autocomplete="off"
													onchange="checkEffectiveDt(this,document.getElementById('tax_struct_eff_dt'),document.getElementById('last_eff_dt'));">
													<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
												</div>
											</div>
										</td>
										<td align="center">
											<div style="width:250px;">
												<input type="text" class="form-control form-control-sm" name="tax_struct_rmk" id="tax_struct_rmk" value="" style="background:<%//=VCOLOR.elementAt(i)%>">
											</div>
										</td>
										<td align="center">
											<div style="width:50px;">
												<input type="button" class="btn btn-info btn-sm select_btn" value="Select" onclick="openTaxStructMst('P');">
											</div>
										</td>
									</tr>
									<%{int m=0,n=0; %>
									<tbody>
										<%-- <tr style="text-align:center;font-weight:bold;">
											<td colspan="2" rowspan="<%=size+1%>"></td>
											<td></td>
											<td>Invoice Type</td>
											<td>Tax Code</td>
											<td>Tax Structure Details</td>
											<td>Eff Date</td>
											<td>Remark</td>
											<td>Enter By</td>
										</tr> --%>
										<%for(int j=0; j<VINVOICE_TYPE.size(); j++){ 
										int size1 = Integer.parseInt(""+VTEMP_INDEX.elementAt(j));
										%>
											<tr>
												<!-- <td colspan="4"></td> -->
												<td <%if(size1>1){ %>onclick="hide_show('tbody<%=j%>','hidCont<%=j%>');"<%} %>>
												<%if(size1>1){ %><span id="hidCont<%=j%>" class="fa fa-expand" title="Click here to show Tax summary"></span>&nbsp;&nbsp;<%} %>
												<%=VINVOICE_TYPE_NM.elementAt(j)%></td>
												<%-- <td align="center"><%=VSER_TAX_STRUCT_CD.elementAt(j)%></td> --%>
												<td align="center"><%=VSAP_TAX_CODE.elementAt(j)%></td>
												<td><%=VTAX_STRUCT_NM.elementAt(j)%></td>
												<td align="center"><%=VEFF_DT.elementAt(j)%></td>
												<td><%=VTAX_STRUCT_RMK.elementAt(j)%></td>
												<td align="center"><%=VENTERED_BY.elementAt(j)%></td>
											</tr>
											<%if(size1 > 1){n=0; %>
											<tbody id="tbody<%=j%>" style="display:none;">
												<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
													<td colspan="1" style="background:white;"></td>
													<td>SAP Tax Code</td>
													<td>Tax Structure Details</td>
													<td>Eff Date</td>
													<td>Remark</td>
													<td>Enter By</td>
												</tr>
												<%for(m=m; m<VTEMP_TAX_STRUCT_CD.size(); m++){ 
												n+=1;%>
													<tr>
														<td colspan="1" style="background:white;"></td>
														<%-- <td align="center"><%=VTEMP_SER_TAX_STRUCT_CD.elementAt(m)%></td> --%>
														<td align="center"><%=VTEMP_SAP_TAX_CODE.elementAt(m)%></td>
														<td><%=VTEMP_TAX_STRUCT_NM.elementAt(m)%></td>
														<td align="center"><%=VTEMP_EFF_DT.elementAt(m)%></td>
														<td><%=VTEMP_TAX_STRUCT_RMK.elementAt(m)%></td>
														<td align="center"><%=VTEMP_ENTERED_BY.elementAt(m) %></td>
													</tr>
													<%if(n==size1)
													{
														m=m+1;
														break;
													}
												}%>
											</tbody>
											<%}else{n=0;} %>
										<%} %>
									</tbody>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="modal-footer cdfooter">
	        		<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit('P');">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
	      		</div>
			</div>
		</div>
	</div>
	<%} %>
	&nbsp;
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							<%if(entity_role.equals("C")){ %>Customer
							<%}else if(entity_role.equals("T")){ %>Trader
							<%}else if(entity_role.equals("R")){ %>Transporter<%}%> Service TCS/TDS Structure Details (<font style="background:#99ffcc;font-size:20px;font-weight:bold;">For Service Invoice</font>)
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>Invoice Type</th>
										<th>SAP Tax Code</th>
										<th>Tax Structure Details</th>
										<th>Eff Date</th>
										<th>Remark</th>
										<th>Select Tax Structure</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td align="center">
											<div style="width:150px;">
												<select class="form-select form-select-sm" name="ser_invoice_type">
													<option value="0">--Select--</option>
													<%if(entity_role.equals("C")){ %>
													<option value="S">Invoice Seq. S</option>
													<option value="CR">Invoice Seq. CR</option>
													<option value="DR">Invoice Seq. DR</option>
													<option value="CCR">Invoice Seq. CCR</option>
													<option value="CDR">Invoice Seq. CDR</option>
													<option value="LP">Invoice Seq. LP</option>
													<%} else if(entity_role.equals("T")){ %>
													<option value="S">Commodity</option>
													<option value="SI">ReGas Invoice</option>
													<option value="CR">Credit Note</option>
													<option value="DR">Debit Note</option>
													<option value="LP">Late Payment</option>
													<%} else if(entity_role.equals("R")){ %>
													<option value="TC">Transmission Charges</option>
													<option value="IC">Imbalance Charges</option>
													<option value="DR">Debit Note</option>
													<option value="LP">Late Payment</option>
													<%}else if(entity_role.equals("H")){ %>
													<option value="CH">CH Agent Fee</option>
													<%}else if(entity_role.equals("S")){ %>
													<option value="SF">Surveyor Fee</option>
													<%}else if(entity_role.equals("V")){ %>
													<option value="VA">Vessel Agent Fee</option>
													<%} else if(entity_role.equals("G")){ %>
													<option value="TX">Transaction Charges</option>
													<option value="DR">Debit Note</option>
													<%} %>
												</select>
												<%-- <%if(VSER_INVOICE_TYPE.size()==1){ %>
													<script>document.forms[0].ser_invoice_type.value="<%=VSER_INVOICE_TYPE.elementAt(i)%>"</script>
												<%}else{ %>
													<script>document.forms[0].ser_invoice_type[<%=i%>].value="<%=VSER_INVOICE_TYPE.elementAt(i)%>"</script>
												<%} %> --%>
											</div>
										</td>
										<td align="center">
											<div style="width:50px;">
												<input type="text" class="form-control form-control-sm" name="ser_sap_tax_code" id="ser_sap_tax_code" value="" readOnly style="background:<%//=VSER_COLOR.elementAt(i)%>">
												<input type="hidden" class="form-control form-control-sm" name="ser_tax_struct_cd" id="ser_tax_struct_cd" value="" readOnly style="background:<%//=VSER_COLOR.elementAt(i)%>">
											</div>
										</td>
										<td align="center">
											<div style="width:250px;">
												<input type="text" class="form-control form-control-sm" name="dis_ser_tax_struct_nm" id="dis_ser_tax_struct_nm" value="" readOnly style="background:<%//=VSER_COLOR.elementAt(i)%>">
												<input type="hidden" class="form-control form-control-sm" name="ser_tax_struct_nm" id="ser_tax_struct_nm" value="" readOnly style="background:<%//=VSER_COLOR.elementAt(i)%>">
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
													<input type="text" class="form-control form-control-sm date fmsdtpick" name="ser_eff_dt" id="ser_eff_dt" value="" style="background:<%//=VSER_COLOR.elementAt(i)%>" autocomplete="off">
													<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
												</div>
												<input type="hidden" class="form-control form-control-sm" name="ser_tax_struct_eff_dt" id="ser_tax_struct_eff_dt" value="" readOnly style="background:<%//=VSER_COLOR.elementAt(i)%>">
											</div>
										</td>
										<td align="center">
											<div style="width:200px;">
												<input type="text" class="form-control form-control-sm" name="ser_tax_struct_rmk" id="ser_tax_struct_rmk" value="" style="background:<%//=VSER_COLOR.elementAt(i)%>">
											</div>
										</td>
										<td align="center">
											<div style="width:50px;">
												<input type="button" class="btn btn-info btn-sm select_btn" value="Select" onclick="openTaxStructMst('S');">
											</div>
										</td>
									</tr>
									<%{int m=0,n=0; %>
									<tbody>
										<%-- <tr style="text-align:center;font-weight:bold;">
											<td colspan="2" rowspan="<%=size+1%>"></td>
											<td></td>
											<td>Invoice Type</td>
											<td>Tax Code</td>
											<td>Tax Structure Details</td>
											<td>Eff Date</td>
											<td>Remark</td>
											<td>Enter By</td>
										</tr> --%>
										<%for(int j=0; j<VSER_INVOICE_TYPE.size(); j++){ 
										int size1 = Integer.parseInt(""+VTEMP_SER_INDEX.elementAt(j));
										%>
											<tr>
												<!-- <td colspan="4"></td> -->
												<td <%if(size1>1){ %>onclick="hide_show('ser_tbody<%=j%>','ser_hidCont<%=j%>');"<%} %>>
												<%if(size1>1){ %><span id="ser_hidCont<%=j%>" class="fa fa-expand" title="Click here to show Tax summary"></span>&nbsp;&nbsp;<%} %>
												<%=VSER_INVOICE_TYPE_NM.elementAt(j)%></td>
												<%-- <td align="center"><%=VSER_TAX_STRUCT_CD.elementAt(j)%></td> --%>
												<td align="center"><%=VSER_SAP_TAX_CODE.elementAt(j)%></td>
												<td><%=VSER_TAX_STRUCT_NM.elementAt(j)%></td>
												<td align="center"><%=VSER_EFF_DT.elementAt(j)%></td>
												<td><%=VSER_TAX_STRUCT_RMK.elementAt(j)%></td>
												<td align="center"><%=VSER_ENTERED_BY.elementAt(j)%></td>
											</tr>
											<%if(size1 > 1){n=0; %>
											<tbody id="ser_tbody<%=j%>" style="display:none;">
												<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
													<td colspan="1" style="background:white;"></td>
													<td>SAP Tax Code</td>
													<td>Tax Structure Details</td>
													<td>Eff Date</td>
													<td>Remark</td>
													<td>Enter By</td>
												</tr>
												<%for(m=m; m<VTEMP_SER_TAX_STRUCT_CD.size(); m++){ 
												n+=1;%>
													<tr>
														<td colspan="1" style="background:white;"></td>
														<%-- <td align="center"><%=VTEMP_SER_TAX_STRUCT_CD.elementAt(m)%></td> --%>
														<td align="center"><%=VTEMP_SER_SAP_TAX_CODE.elementAt(m)%></td>
														<td><%=VTEMP_SER_TAX_STRUCT_NM.elementAt(m)%></td>
														<td align="center"><%=VTEMP_SER_EFF_DT.elementAt(m)%></td>
														<td><%=VTEMP_SER_TAX_STRUCT_RMK.elementAt(m)%></td>
														<td align="center"><%=VTEMP_SER_ENTERED_BY.elementAt(m) %></td>
													</tr>
													<%if(n==size1)
													{
														m=m+1;
														break;
													}
												}%>
											</tbody>
											<%}else{n=0;} %>
										<%} %>
									</tbody>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="modal-footer cdfooter">
	        		<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit('S');">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
	      		</div>
			</div>
		</div>
	</div>
	<%} %>
</div>

<input type="hidden" name="option" value="ENTITY_TCS_TDS_MST">
<input type="hidden" name="prev_entity_role" value="<%=entity_role%>">

<input type="hidden" name="type" value="">

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
</html>