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
	var mst_contract_type = document.forms[0].mst_contract_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_seller_nomination_to_customer.jsp?gas_dt="+gas_dt+"&contract_type="+mst_contract_type+"&u="+u;

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

function doSubmit(index,COUNTERPARTY_CD,CONT_NO,AGMT_NO,CONTRACT_TYPE,PLANT_SEQ)
{
	var rmk = document.getElementById('rmk'+index).value;
	
	if(trim(rmk) == "")
	{
		alert("Enter Remark for ROW - "+(parseInt(index)+1))
	}
	else
	{
		var a = confirm("Do you want to Submit?")
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].counterparty_cd.value=COUNTERPARTY_CD;
		 	document.forms[0].cont_no.value=CONT_NO;
		 	document.forms[0].agmt_no.value=AGMT_NO;
		 	document.forms[0].contract_type.value=CONTRACT_TYPE;
		 	document.forms[0].plant_seq.value=PLANT_SEQ;
		 	document.forms[0].remark.value=rmk;
		 	document.forms[0].submit();
		}
	}
}

var newWindow;
function viewSellerNomiToCust(index,COUNTERPARTY_CD,CONT_NO,AGMT_NO,CONTRACT_TYPE,PLANT_SEQ,file)
{
	var contact_person_cd = document.getElementById("contact_person_cd"+index).value;
	
	var gas_dt = document.forms[0].gas_dt.value;
	var u = document.forms[0].u.value;
	
	if(trim(contact_person_cd)!="")
	{
		var url = "";
		if(file=="MAIL")
		{
			url = "frm_seller_nom_to_cust_send_mail.jsp?gas_dt="+gas_dt+"&contract_type="+CONTRACT_TYPE+
				"&counterparty_cd="+COUNTERPARTY_CD+"&agmt_no="+AGMT_NO+"&cont_no="+CONT_NO+"&plant_seq="+PLANT_SEQ+	
				"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
		}
		else
		{
			url = "rpt_view_seller_nomination_to_customer.jsp?gas_dt="+gas_dt+"&contract_type="+CONTRACT_TYPE+
				"&counterparty_cd="+COUNTERPARTY_CD+"&agmt_no="+AGMT_NO+"&cont_no="+CONT_NO+"&plant_seq="+PLANT_SEQ+	
				"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
		}
		
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Seller Nomination To Customer","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Seller Nomination To Customer","top=10,left=10,width=1100,height=900,scrollbars=1");
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
String nextdate = utildate.getNextDate();

String gas_dt = request.getParameter("gas_dt")==null?nextdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "1");
String contract_type = request.getParameter("contract_type")==null?"0":request.getParameter("contract_type");

String path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.nom_to_customer_pdf_path;
String file_path = request.getRealPath(path);

cont_mgmt.setCallFlag("SELLER_NOMINATION_TO_CUSTOMER");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.setContract_type(contract_type);
cont_mgmt.setFile_path(file_path);
cont_mgmt.init();

Vector VMST_CONTRACT_TYPE = cont_mgmt.getVMST_CONTRACT_TYPE();
Vector VMST_CONTRACT_TYPE_NM = cont_mgmt.getVMST_CONTRACT_TYPE_NM();

Vector VCONTRACT_TYPE = cont_mgmt.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = cont_mgmt.getVCONTRACT_TYPE_NM();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();
Vector VDIS_CONT_MAPPING = cont_mgmt.getVDIS_CONT_MAPPING();
Vector VAGMT_NO = cont_mgmt.getVAGMT_NO();
Vector VCONT_NO = cont_mgmt.getVCONT_NO();
Vector VCONT_REF = cont_mgmt.getVCONT_REF();
Vector VINDEX = cont_mgmt.getVINDEX();

Vector VCONTACT_PERSON = cont_mgmt.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = cont_mgmt.getVCONTACT_PERSON_CD();
Vector VSEL_CONTACT_PERSON_CD = cont_mgmt.getVSEL_CONTACT_PERSON_CD();
Vector VREMARK = cont_mgmt.getVREMARK();
Vector VPDF_EXISTS = cont_mgmt.getVPDF_EXISTS();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_ContractMgmt">

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
					    	Daily Seller Nomination To Customer
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
									<label class="form-label"><b>Contract Type</b></label>
								</div>
			    				<div class="col">
			    					<select class="form-select form-select-sm" name="mst_contract_type" onchange="refresh()">
										<option value="0">All</option>
										<%for(int i=0;i<VMST_CONTRACT_TYPE.size();i++){ %>
										<option value="<%=VMST_CONTRACT_TYPE.elementAt(i)%>"><%=VMST_CONTRACT_TYPE_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].mst_contract_type.value="<%=contract_type%>"</script>
			    				</div>
			    			</div>
			    		</div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%if(VCONTRACT_TYPE.size()>0){ %>
					<%int j=0,k=0;
					for(int i=0; i<VCONTRACT_TYPE.size(); i++)
					{ 
						String contType=""+VCONTRACT_TYPE.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					<%if(i!=0){ %>
					&nbsp;
					<%} %>
						<div class="row m-b-5">
							<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VCONTRACT_TYPE_NM.elementAt(i)%></label>
						</div>
						<div class="row">
							<div class="table-responsive">
								<table class="table table-bordered table-hover">
									<thead>
										<tr>
											<th rowspan="2"></th>
											<th rowspan="2">Customer</th>
											<th rowspan="2">Contract#<br>[Contract/Trade Ref#]</th>
											<th rowspan="2">Plant Name</th>
											<th rowspan="2">Contact Person</th>
											<th rowspan="2" colspan="2">Remarks</th>
											<th colspan="4">Report</th>
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
										<%for(j=j;j<VCOUNTERPARTY_CD.size(); j++) 
										{
											k+=1;
										%>
											<tr>
												<td></td>
												<td title="<%=VCOUNTERPARTY_NM.elementAt(j)%>"><%=VCOUNTERPARTY_ABBR.elementAt(j) %></td>
												<td>
													<font color="blue"><%=VDIS_CONT_MAPPING.elementAt(j)%></font>
													<%if(!VCONT_REF.elementAt(j).equals("")){ %>
													<br>[<%=VCONT_REF.elementAt(j)%>]	
													<%} %>
												</td>
												<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(j)%></td>
												<td align="center">
													<div class="row" style="width:150px;">
														<select class="form-select form-select-sm" name="contact_person_cd" id="contact_person_cd<%=j%>">
														<%for(int l=0; l<((Vector)VCONTACT_PERSON_CD.elementAt(j)).size(); l++){ %>
															<option value="<%=((Vector)VCONTACT_PERSON_CD.elementAt(j)).elementAt(l)%>"><%=((Vector)VCONTACT_PERSON.elementAt(j)).elementAt(l)%></option>
														<%} %>
														</select>
														<script>document.getElementById("contact_person_cd<%=j%>").value="<%=VSEL_CONTACT_PERSON_CD.elementAt(j)%>"</script>
													</div>
												</td>
												<td align="center">
													<div class="row" style="width:250px;">
														<textarea class="form-control form-control-sm"  name="rmk" id="rmk<%=j%>" rows="2" maxlength="150"><%=VREMARK.elementAt(j)%></textarea>
													</div>
												</td>
												<td align="center">
													<input type="button" class="btn btn-warning btn-sm com-btn" value="Submit" 
													onclick="doSubmit('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=contType%>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>')">
												</td>
												<td align="center">
													<i class="fa fa-eye fa-2x" 
													onclick="viewSellerNomiToCust('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=contType%>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>','HTML')"></i>
												</td>
												<td align="center">
												<%if(print_access.equals("Y")){ %>
													<i class="fa fa-file-pdf-o fa-2x pdf_icon" 
													onclick="viewSellerNomiToCust('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=contType%>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>','PDF')"></i>
												<%}else{ %>
													<i class="fa fa-file-pdf-o fa-2x pdf_icon" style="color:grey;"></i>
												<%} %>
												</td>
												<td align="center">
												<%if(print_access.equals("Y")){ %>
													<i class="fa fa-file-excel-o fa-2x excel_icon" 
													onclick="viewSellerNomiToCust('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=contType%>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>','XLS')"></i>
												<%}else{ %>
													<i class="fa fa-file-excel-o fa-2x excel_icon" style="color:grey;"></i>
												<%} %>
												</td>
												<td align="center">
													<%if(VPDF_EXISTS.elementAt(j).equals("Y") && execute_access.equals("Y")){ %>
													<i class="fa fa-envelope-o fa-2x mail_icon" onclick="viewSellerNomiToCust('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=contType%>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>','MAIL');"></i>
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
						<%=utilmsg.infoMessage("<b>Nomination is not Done for Selected Gas Day!</b>") %>
					</div>
				<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="SELLER_NOM_TO_CUST_REMARK">
<input type="hidden" name="counterparty_cd" value="">
<input type="hidden" name="cont_no" value="">
<input type="hidden" name="agmt_no" value="">
<input type="hidden" name="contract_type" value="">
<input type="hidden" name="plant_seq" value="">
<input type="hidden" name="remark" value="">

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