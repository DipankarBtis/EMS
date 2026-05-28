<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var gas_dt = document.forms[0].gas_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_seller_alloc_to_transporter.jsp?gas_dt="+gas_dt+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function nextDate(day_no)
{
	//var clearance = document.forms[0].clearance.value;
	
	var dt = document.forms[0].gas_dt.value;
	if(dt!="")
	{
	   	var split = dt.split("/");
		var d_dt = split[0];
		var m_dt = split[1];
		var y_dt = split[2];
		
		var dt1 = new Date(y_dt+"-"+m_dt+"-"+d_dt);
		if(day_no == "-1")
		{
			dt1.setDate(dt1.getDate()-1);
		}
		else
		{
			dt1.setDate(dt1.getDate()+1);
		}
		var day = dt1.getDate();
		if(parseInt(day) < 10)
		{
			day="0"+day;
		}
		var month = dt1.getMonth()+1;
		var year = dt1.getFullYear();
		if(parseInt(month) < 10)
		{
			month="0"+month;
		}
		var to_dt= day+"/"+month+"/"+year;
		
		document.forms[0].gas_dt.value=to_dt;
		
		//refresh(clearance);
		refresh();
	}
}
var newWindow;
function viewSellerAllocation(index,transporter_cd,tplant_seq,file)
{
	var contact_person_cd = document.getElementById("contact_person_cd"+index).value;
	
	var gas_dt = document.forms[0].gas_dt.value;
	var u = document.forms[0].u.value;
	
	if(trim(contact_person_cd)!="")
	{
		var url = "";
		if(file=="MAIL")
		{
			url = "frm_alloc_to_trans_send_mail.jsp?transporter_cd="+transporter_cd+"&tplant_seq="+tplant_seq+
				"&gas_dt="+gas_dt+"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
		}
		else
		{
			url = "rpt_view_seller_alloc_to_transporter.jsp?transporter_cd="+transporter_cd+"&tplant_seq="+tplant_seq+
				"&gas_dt="+gas_dt+"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
		}
		
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Seller Allocation To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Seller Allocation To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
	}
	else
	{
		alert("Select Contact Person for associated ROW("+(parseInt(index)+1)+")");
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DataBean_ContractMgmt" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String prevdate = utildate.getPreviousDate();

String gas_dt = request.getParameter("gas_dt")==null?prevdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "1");

String path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.alloc_to_trans_pdf_path;
String file_path = request.getRealPath(path);

cont_mgmt.setCallFlag("SELLER_ALLOC_TO_TRANSPORTER");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.setFile_path(file_path);
cont_mgmt.init();

Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_PLANT_SEQ = cont_mgmt.getVTRANSPORTER_PLANT_SEQ();
Vector VTRANSPORTER_PLANT_ABBR = cont_mgmt.getVTRANSPORTER_PLANT_ABBR();
Vector VCONTACT_PERSON = cont_mgmt.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = cont_mgmt.getVCONTACT_PERSON_CD();
Vector VSEL_CONTACT_PERSON_CD = cont_mgmt.getVSEL_CONTACT_PERSON_CD();
Vector VINDEX = cont_mgmt.getVINDEX();
Vector VPDF_EXISTS = cont_mgmt.getVPDF_EXISTS();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">
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
					    	Daily Allocation To Transporter
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gas Day<span class="s-red">*</span></b></label>
								</div>
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
				      					<span class="input-group-text" onclick="nextDate('-1');" title="click for Back Date"><i class="fa fa-backward fa-lg"></i></span>
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="gas_dt" id="gas_dt" value="<%=gas_dt%>" maxLength="10" 
					      				onchange="validateDate(this);refresh();">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      					<span class="input-group-text" onclick="nextDate('1');" title="click for Next Date"><i class="fa fa-forward fa-lg"></i></span>
				      				</div>
				    			</div>
				    		</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gen Day</b></label>
								</div>
			    				<div class="col">
				      				<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm date fmsdtpick" name="gen_dt" id="gen_dt" value="<%=gen_dt%>" maxLength="10" 
					      				onchange="validateDate(this);">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
				    			</div>
				    		</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%if(VTRANSPORTER_CD.size()>0){ %>
					<%int j=0,k=0;
					for(int i=0; i<VTRANSPORTER_CD.size(); i++)
					{ 
						String trans_cd=""+VTRANSPORTER_CD.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					<%if(i!=0){ %>
					&nbsp;
					<%} %>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTRANSPORTER_ABBR.elementAt(i)%></label>
						</div>
						<div class="row">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th rowspan="2"></th>
											<th rowspan="2">Transporter Grid</th>
											<th rowspan="2">Contact Person</th>
											<th colspan="4">Reports</th>
										</tr>
										<tr>	
											<th>View Detail</th>
											<th>PDF</th>
											<th>Excel</th>
											<th>Email</th>
										</tr>
									</thead>
									<tbody>
									<%k=0;
									if(index > 0){ %>
										<%for(j=j;j<VTRANSPORTER_PLANT_SEQ.size(); j++) 
										{
											String trans_plant_seq=""+VTRANSPORTER_PLANT_SEQ.elementAt(j);
											k+=1;
										%>
											<tr>
												<td></td>
												<td align="center"><%=VTRANSPORTER_PLANT_ABBR.elementAt(j)%></td>
												<td align="center">
													<div class="row" style="width:150px;">
													<select class="form-select form-select-sm" name="contact_person_cd" id="contact_person_cd<%=j%>">
													<%for(int l=0; l<((Vector)VCONTACT_PERSON_CD.elementAt(j)).size(); l++){ %>
														<option value="<%=((Vector)VCONTACT_PERSON_CD.elementAt(j)).elementAt(l)%>"><%=((Vector)VCONTACT_PERSON.elementAt(j)).elementAt(l)%></option>
													<%} %>
													</select>
													<script>document.getElementById("contact_person_cd<%=j%>").value="<%=VSEL_CONTACT_PERSON_CD.elementAt(j)%>"</script>
												</div>
												<td align="center">
													<i class="fa fa-eye fa-2x" onclick="viewSellerAllocation('<%=j%>','<%=trans_cd%>','<%=trans_plant_seq%>','HTML');"></i>
												</td>
												<td align="center">
												<%if(print_access.equals("Y")){ %>
													<i class="fa fa-file-pdf-o fa-2x pdf_icon" onclick="viewSellerAllocation('<%=j%>','<%=trans_cd%>','<%=trans_plant_seq%>','PDF');"></i>
												<%}else{ %>
													<i class="fa fa-file-pdf-o fa-2x pdf_icon" style="color:grey;"></i>
												<%} %>
												</td>
												<td align="center">
												<%if(print_access.equals("Y")){ %>
													<i class="fa fa-file-excel-o fa-2x excel_icon" onclick="viewSellerAllocation('<%=j%>','<%=trans_cd%>','<%=trans_plant_seq%>','XLS');"></i>
												<%}else{ %>
													<i class="fa fa-file-excel-o fa-2x excel_icon" style="color:grey;"></i>
												<%} %>
												</td>
												<td align="center">
													<%if(VPDF_EXISTS.elementAt(j).equals("Y") && execute_access.equals("Y")){ %>
													<i class="fa fa-envelope-o fa-2x mail_icon" onclick="viewSellerAllocation('<%=j%>','<%=trans_cd%>','<%=trans_plant_seq%>','MAIL');"></i>
													<%}else{ %>
													<i class="fa fa-envelope-o fa-2x" style="opacity: .65; color: gray;" title="PDF is not yet generated!"></i>
													<%} %>
												</td>
											</tr>
											<%if(k==index)
											{
												j=j+1;
												break;
											}%>
										<%} %>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					<%} %>
				<%}else{ %>
					<div align="center">
						<%=utilmsg.infoMessage("<b>Allocation not Done for the Selected Gas Day!</b>") %>
					</div>
				<%} %>
				</div>
			</div>
		</div>
	</div>
</div>


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