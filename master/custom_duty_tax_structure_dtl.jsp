<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh()
{
	var u = document.forms[0].u.value;
	
	var url = "custom_duty_tax_structure_dtl.jsp.jsp?u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function openTaxStructMst(type,tax_pay_recv)
{
	var newWindow;
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open("frm_tax_structure_list.jsp?type="+type+"&tax_pay_recv="+tax_pay_recv,"Custom Duty Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open("frm_tax_structure_list.jsp?type="+type+"&tax_pay_recv="+tax_pay_recv,"Custom Duty Tax Structure List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setTaxStructDetail(index,tax_struct_cd,tax_struct_nm,tax_struct_eff_dt,type,sap_tax_code)
{
	
		document.getElementById("sap_tax_code").value=sap_tax_code;
		document.getElementById("tax_struct_cd").value=tax_struct_cd;
		document.getElementById("tax_struct_nm").value=tax_struct_nm;
		document.getElementById("dis_tax_struct_nm").value=tax_struct_nm+" since "+tax_struct_eff_dt;
		document.getElementById("tax_struct_eff_dt").value=tax_struct_eff_dt;
}

function doSubmit()
{
	var msg="";
	var flag=true;
	
	var tax_struct_cd = document.forms[0].tax_struct_cd.value;
	var eff_dt = document.forms[0].eff_dt.value;
	
	
	if(trim(tax_struct_cd)=="")
	{
		msg+="Please Select tax Structure !\n";
		flag=false;
	}
	if(trim(eff_dt)=="")
	{
		msg+="Please Enter Effective Date !\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to Submit?")
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

function checkEffectiveDate(obj,obj1)
{
	var eff_dt = obj.value;
	var eff_dt1 = obj1.value;
	
	var flag=true;
	
	if((eff_dt!="" && trim(eff_dt) != "" && eff_dt != null))
	{
		if((eff_dt1!="" && trim(eff_dt1) != "" && eff_dt1 != null))
		{
			var value = compareDate(eff_dt,eff_dt1);
			if(parseInt(value) == 2)
		  	{
		    	alert("Effective Date ("+eff_dt+") must be Greater Or Equal To Tax Commencement Date ("+eff_dt1+")!");
		    	flag=false;
		    	document.forms[0].eff_dt.value="";
		  	}
		}
	}
	
	return flag;
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
		    	document.forms[0].eff_dt.value="";
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
				    	document.forms[0].eff_dt.value="";
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
			    	document.forms[0].eff_dt.value="";
			  	}
			}
		}
	}
	
	return flag;
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
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbMaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String opration=request.getParameter("opration")==null?"MODIFY":request.getParameter("opration");

dbMaster.setCallFlag("CUSTOM_DUTY_TAX_MST");
dbMaster.setComp_cd(owner_cd);
dbMaster.init();

Vector VEFF_DT = dbMaster.getVEFF_DT();
Vector VTAX_STRUCT_CD = dbMaster.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = dbMaster.getVTAX_STRUCT_NM();
Vector VTAX_STRUCT_APP_DT = dbMaster.getVTAX_STRUCT_APP_DT();
Vector VTAX_STRUCT_RMK = dbMaster.getVTAX_STRUCT_RMK();
Vector VSAP_TAX_CODE = dbMaster.getVSAP_TAX_CODE();
Vector VENT_BY = dbMaster.getVENT_BY();
Vector VDISP_TAX_STRUCT_NM = dbMaster.getVDISP_TAX_STRUCT_NM();

String tax_pay_recv ="P";//Only Payable
String last_eff_dt = dbMaster.getLast_eff_dt();

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
							Custom Duty Tax Structure Details
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="ser_example">
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
											<td onclick="hide_show('tbody','hidCont');">
												<span id="hidCont" class="fa fa-expand" title="Click here to show Tax summary"></span>&nbsp;&nbsp;
												Custom Duty
											</td>
											<td align="center">
												<div style="width:50px;">
													<input type="text" class="form-control form-control-sm" name="sap_tax_code" id="sap_tax_code" value="" readOnly style="background:<%//=VSER_COLOR.elementAt(i)%>">
													<input type="hidden" class="form-control form-control-sm" name="tax_struct_cd" id="tax_struct_cd" value="" readOnly style="background:<%//=VSER_COLOR.elementAt(i)%>">
												</div>
											</td>
											<td align="center">
												<div style="width:250px;">
													<input type="text" class="form-control form-control-sm" name="dis_tax_struct_nm" id="dis_tax_struct_nm" value="" readOnly style="background:<%//=VSER_COLOR.elementAt(i)%>">
													<input type="hidden" class="form-control form-control-sm" name="tax_struct_nm" id="tax_struct_nm" value="" readOnly style="background:<%//=VSER_COLOR.elementAt(i)%>">
													<input type="hidden" class="form-control form-control-sm" name="tax_struct_eff_dt" id="tax_struct_eff_dt" value="" readOnly style="background:<%//=VSER_COLOR.elementAt(i)%>">
													<input type="hidden" name="last_eff_dt" id="last_eff_dt" value="<%=last_eff_dt %>" >
												</div>
											</td>
											<td align="center">
												<div style="width:100px;">
													<div class="input-group input-group-sm" >
														<input type="text" class="form-control form-control-sm date fmsdtpick" name="eff_dt" id="eff_dt" value="" onchange="checkEffectiveDt(this,document.forms[0].tax_struct_eff_dt,document.forms[0].last_eff_dt);" style="background:<%//=VSER_COLOR.elementAt(i)%>" autocomplete="off">
														<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
													</div>
												</div>
											</td>
											<td align="center">
												<div style="width:200px;">
													<input type="text" class="form-control form-control-sm" name="tax_struct_rmk" id="tax_struct_rmk" value="" style="background:<%//=VSER_COLOR.elementAt(i)%>">
												</div>
											</td>
											<td align="center">
												<div style="width:50px;">
													<input type="button" class="btn btn-info btn-sm select_btn" value="Select" onclick="openTaxStructMst('P','<%=tax_pay_recv%>');">
												</div>
											</td>
										</tr>
										<tbody id="tbody" style="display:none;">
											<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
												<td colspan="1" rowspan="<%=VTAX_STRUCT_CD.size()+1 %>" style="background:white;"></td>
												<td>SAP Tax Code</td>
												<td>Tax Structure Details</td>
												<td>Eff Date</td>
												<td>Remark</td>
												<td>Enter By</td>
											</tr>
									<%int j=0;int k=0;int m=0;int n=0;%>
									<%for(int i=0; i<VTAX_STRUCT_CD.size(); i++){ 
										//int size = Integer.parseInt(""+VSER_INDEX.elementAt(i));
									%>
										<tr>
											<td align="center"><%=VSAP_TAX_CODE.elementAt(i) %></td>
											<td><%=VDISP_TAX_STRUCT_NM.elementAt(i) %></td>
											<td align="center"><%=VEFF_DT.elementAt(i) %>
											</td>
											<td><%=VTAX_STRUCT_RMK.elementAt(i)%></td>
											<td align="center"><%=VENT_BY.elementAt(i) %></td>
										</tr>
									<%} %>
									</tbody>
								</tbody>
							</table>
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
</div>

<input type="hidden" name="option" value="CUSTOM_DUTY_TAX_MST">
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
</form>
</body>
</html>