<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var frq_type = document.forms[0].nomination_type.value
	var report_dt = document.forms[0].report_dt.value
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_buy_buyer_nom_to_trader.jsp?u="+u+
			"&frq_type="+frq_type+"&report_dt="+report_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit(index,COUNTERPARTY_CD,CONT_NO,AGMT_NO,CONTRACT_TYPE,PLANT_SEQ,BU_PLANT_SEQ)
{
	var rmk = document.getElementById('rmk'+index).value;
	var frq_type = document.forms[0].nomination_type.value
	
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
		 	document.forms[0].bu_plant_seq.value=BU_PLANT_SEQ;
		 	document.forms[0].remark.value=rmk;
		 	document.forms[0].nomination_type.value=frq_type;
		 	document.forms[0].submit();
		}
	}
}
var newWindow="";
function printPDF(index,COUNTERPARTY_CD,CONT_NO,AGMT_NO,CONTRACT_TYPE,PLANT_SEQ,BU_PLANT_SEQ,from_dt,to_dt,month_nm)
{
	var report_dt = document.forms[0].report_dt.value;
	var rmk = document.getElementById('rmk'+index).value;
	var to_contact = document.getElementById('to_contact'+index).value;
	var from_contact = document.getElementById('from_contact'+index).value;
	var nomination_type = document.forms[0].nomination_type.value
	var frq_type = document.forms[0].nomination_type.value
	var u = document.forms[0].u.value;
	
	var url_main = "rpt_buy_buyer_nom_to_trader.jsp?u="+u+
	"&frq_type="+frq_type+"&report_dt="+report_dt;
	
	if(trim(to_contact) == "0")
	{
		alert("Select To Contact Person for ROW - "+(parseInt(index)+1))
	}
	else if(trim(from_contact) == "0")
	{
		alert("Select From Contact Person for ROW - "+(parseInt(index)+1))
	}
	else
	{
		var url = "../purchase/view_buyer_nom_to_trader.jsp?report_dt="+report_dt+"&counterparty_cd="+COUNTERPARTY_CD+
		"&cont_no="+CONT_NO+"&agmt_no="+AGMT_NO+"&contract_type="+CONTRACT_TYPE+"&plant_seq="+PLANT_SEQ+"&bu_plant_seq="+BU_PLANT_SEQ+"&frq_type="+nomination_type+
		"&from_dt="+from_dt+"&to_dt="+to_dt+"&month_nm="+month_nm+"&url_main="+url_main;

		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open("../purchase/view_buyer_nom_to_trader.jsp?report_dt="+report_dt+"&counterparty_cd="+COUNTERPARTY_CD+"&cont_no="+CONT_NO+"&agmt_no="+AGMT_NO+"&contract_type="+CONTRACT_TYPE+"&plant_seq="+PLANT_SEQ+"&bu_plant_seq="+BU_PLANT_SEQ+"&to_contact="+to_contact+"&from_contact="+from_contact+"&rmk="+rmk+"&frq_type="+nomination_type+"&from_dt="+from_dt+"&to_dt="+to_dt+"&month_nm="+month_nm+"&url_main="+url_main,"Buy_Buyer_Nomination","top=10,left=70,width=900,height=700,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow= window.open("../purchase/view_buyer_nom_to_trader.jsp?report_dt="+report_dt+"&counterparty_cd="+COUNTERPARTY_CD+"&cont_no="+CONT_NO+"&agmt_no="+AGMT_NO+"&contract_type="+CONTRACT_TYPE+"&plant_seq="+PLANT_SEQ+"&bu_plant_seq="+BU_PLANT_SEQ+"&to_contact="+to_contact+"&from_contact="+from_contact+"&rmk="+rmk+"&frq_type="+nomination_type+"&from_dt="+from_dt+"&to_dt="+to_dt+"&month_nm="+month_nm+"&url_main="+url_main,"Buy_Buyer_Nomination","top=10,left=70,width=900,height=700,scrollbars=1");
		}
	}
}

function sendMail(index,COUNTERPARTY_CD,CONT_NO,AGMT_NO,CONTRACT_TYPE,PLANT_SEQ,BU_PLANT_SEQ,from_dt,to_dt,month_nm)
{
	var report_dt = document.forms[0].report_dt.value;
	var rmk = document.getElementById('rmk'+index).value;
	var to_contact = document.getElementById('to_contact'+index).value;
	var from_contact = document.getElementById('from_contact'+index).value;
	var nomination_type = document.forms[0].nomination_type.value
	
	if(trim(to_contact) == "0")
	{
		alert("Select To Contact Person for ROW - "+(parseInt(index)+1))
	}
	else if(trim(from_contact) == "0")
	{
		alert("Select From Contact Person for ROW - "+(parseInt(index)+1))
	}
	else
	{
		var url = "../purchase/frm_buy_nom_to_trader_send_mail.jsp?report_dt="+report_dt+"&counterparty_cd="+COUNTERPARTY_CD+
		"&cont_no="+CONT_NO+"&agmt_no="+AGMT_NO+"&contract_type="+CONTRACT_TYPE+"&plant_seq="+PLANT_SEQ+"&bu_plant_seq="+BU_PLANT_SEQ+"&frq_type="+nomination_type+
		"&from_dt="+from_dt+"&to_dt="+to_dt+"&month_nm="+month_nm;
	
		if(!newWindow || newWindow.closed)
		{
			newWindow= window.open("../purchase/frm_buy_nom_to_trader_send_mail.jsp?report_dt="+report_dt+"&counterparty_cd="+COUNTERPARTY_CD+"&cont_no="+CONT_NO+"&agmt_no="+AGMT_NO+"&contract_type="+CONTRACT_TYPE+"&plant_seq="+PLANT_SEQ+"&bu_plant_seq="+BU_PLANT_SEQ+"&to_contact="+to_contact+"&from_contact="+from_contact+"&rmk="+rmk+"&frq_type="+nomination_type+"&from_dt="+from_dt+"&to_dt="+to_dt+"&month_nm="+month_nm,"Buy_Buyer_Nomination","top=10,left=70,width=900,height=700,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow= window.open("../purchase/frm_buy_nom_to_trader_send_mail.jsp?report_dt="+report_dt+"&counterparty_cd="+COUNTERPARTY_CD+"&cont_no="+CONT_NO+"&agmt_no="+AGMT_NO+"&contract_type="+CONTRACT_TYPE+"&plant_seq="+PLANT_SEQ+"&bu_plant_seq="+BU_PLANT_SEQ+"&to_contact="+to_contact+"&from_contact="+from_contact+"&rmk="+rmk+"&frq_type="+nomination_type+"&from_dt="+from_dt+"&to_dt="+to_dt+"&month_nm="+month_nm,"Buy_Buyer_Nomination","top=10,left=70,width=900,height=700,scrollbars=1");
		}
		window.opener.refresh();
	}
}
function viewPDF(url)
{
	window.open(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DB_Buy_Nom_Alloc_Mgmt" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String frq_type=request.getParameter("frq_type")==null?"0":request.getParameter("frq_type");
String report_dt=request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
String to_contact = request.getParameter("to_contact")==null?"0":request.getParameter("to_contact");
String from_contact = request.getParameter("from_contact")==null?"0":request.getParameter("from_contact");

String month_nm="";
if(!report_dt.equals("") && frq_type.equals("M"))
{
	month_nm=utildate.getMonthName(report_dt);
}

String path ="/"+CommonVariable.work_dir+owner_cd+"//purchase//buyer_nom_to_trader//";
String file_path = request.getRealPath(path);

purchase.setCallFlag("RPT_BUYER_NOM_TO_TRADER");
purchase.setReport_dt(report_dt);
purchase.setComp_cd(owner_cd);
purchase.setFrq_type(frq_type);
purchase.setFile_path(file_path);
purchase.init();

String from_date = purchase.getFrom_date();
String to_date = purchase.getTo_date();

Vector VCOUNTERPARTY_CD = purchase.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = purchase.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = purchase.getVCOUNTERPARTY_ABBR();

Vector VPLANT_SEQ = purchase.getVPLANT_SEQ();
Vector VPLANT_ABBR = purchase.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = purchase.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = purchase.getVBU_PLANT_ABBR();
Vector VCONT_REF = purchase.getVCONT_REF();
Vector VTO_CONTACT_PERSON_SEQ_NO = purchase.getVTO_CONTACT_PERSON_SEQ_NO();
Vector VSEL_TO_CONTACT_PERSON_SEQ_NO = purchase.getVSEL_TO_CONTACT_PERSON_SEQ_NO();
Vector VTO_CONTACT_PERSON = purchase.getVTO_CONTACT_PERSON();
Vector VFROM_CONTACT_PERSON_SEQ_NO = purchase.getVFROM_CONTACT_PERSON_SEQ_NO();
Vector VSEL_FROM_CONTACT_PERSON_SEQ_NO = purchase.getVSEL_FROM_CONTACT_PERSON_SEQ_NO();
Vector VFROM_CONTACT_PERSON = purchase.getVFROM_CONTACT_PERSON();
Vector VCONT_NO = purchase.getVCONT_NO();
Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VREMARK = purchase.getVREMARK();
Vector VPDF_EXISTS = purchase.getVPDF_EXISTS();
Vector VPDF_FILE_NAME = purchase.getVPDF_FILE_NAME();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd+"//purchase//buyer_nom_to_trader//";
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_Buy_Nom_Alloc_Mgmt">

<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			}%>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
							Buyer Nomination To Trader
						</div>
					 	<div class="btn-group">
							<select class="btn btn-outline-secondary btngrp <%if(!frq_type.equals("0")){ %>btnactive<%} %>" name="nomination_type" onchange="refresh();">
								<option value="0">Select Nomination</option>
								<option value="M">Monthly</option>
				    			<option value="W">Weekly</option>
				    			<option value="D">Daily</option>
				    		</select>
						</div>
						<script>
							document.forms[0].nomination_type.value="<%=frq_type%>"
						</script>						  	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
				    			<label class="form-label"><b>Report Date</b></label>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
			    				<div class="input-group input-group-sm" >
				      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="report_dt" id="report_dt" value="<%=report_dt%>" maxLength="10" 
				      				onchange="validateDate(this);refresh();" autocomplete="off">
				      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      				</div>
				    		</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<label><b>
								<%if(frq_type.equals("M")){ %>
								<font color="blue">Buyer Nomination for <%=month_nm%> <%=report_dt.substring(6,report_dt.length())%></font>
								<%}else if(frq_type.equals("W")){ %>
								<font color="blue" title="Monday - Sunday">Weekly Buyer Nomination (<%=from_date%> - <%=to_date%>)</font>
								<%}else if(frq_type.equals("D")){ %>
								<font color="blue">Buyer Nomination for <%=report_dt%></font>
								<%} %>
								</b></label>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th rowspan="2"></th>
										<th rowspan="2">Trader Name</th>
										<th rowspan="2">Contract Reference</th>
										<th colspan="2">To</th>
										<th colspan="2">From</th>
										<th rowspan="2">Remark</th>
										<th colspan="2">Reports</th>
									</tr>
									<tr>
										<th>Delivery Point</th>
										<th>Contact Person</th>
										<th>Business Unit</th>
										<th>Contact Person</th>
										<th>PDF</th>
										<th>EMAIL</th>
									</tr>
								</thead>
								<tbody>
								<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
									<tr>
										<td align="center"><%=i+1%></td>
										<td><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
										<td><%=VCONT_REF.elementAt(i)%></td>
										<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
										<td align="center">
											<div class="row" style="width:150px;">
												<select class="form-select form-select-sm" name="to_contact" id="to_contact<%=i%>">
													<option value="0" selected="selected">--Select--</option>
												<%for(int j=0; j<((Vector)VTO_CONTACT_PERSON_SEQ_NO.elementAt(i)).size(); j++){ %>
													<option value="<%=((Vector)VTO_CONTACT_PERSON_SEQ_NO.elementAt(i)).elementAt(j)%>"><%=((Vector)VTO_CONTACT_PERSON.elementAt(i)).elementAt(j)%></option>
												<%} %>
												</select>
												<script>document.getElementById("to_contact<%=i%>").value="<%=VSEL_TO_CONTACT_PERSON_SEQ_NO.elementAt(i)%>"</script>
											</div>
										</td>
										<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
										<td align="center">
											<div class="row" style="width:150px;">
												<select class="form-select form-select-sm" name="from_contact" id="from_contact<%=i%>">
													<option value="0" selected="selected">--Select--</option>
												<%for(int j=0; j<((Vector)VFROM_CONTACT_PERSON_SEQ_NO.elementAt(i)).size(); j++){ %>
													<option value="<%=((Vector)VFROM_CONTACT_PERSON_SEQ_NO.elementAt(i)).elementAt(j)%>"><%=((Vector)VFROM_CONTACT_PERSON.elementAt(i)).elementAt(j)%></option>
												<%} %>
												</select>
												<script>document.getElementById("from_contact<%=i%>").value="<%=VSEL_FROM_CONTACT_PERSON_SEQ_NO.elementAt(i)%>"</script>
											</div>
										</td>
										<td align="center" style="width:300px;">
											<div class="row" style="width:300px;">
												<div class="col">
													<textarea class="form-control form-control-sm"  name="rmk" id="rmk<%=i%>" rows="2" maxlength="150"><%=VREMARK.elementAt(i)%></textarea>
												</div>
												<div class="col">
												<%if(write_access.equals("Y")){ %>
													<input type="button" class="btn btn-warning com-btn" value="Submit" 
													onclick="doSubmit('<%=i%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
													'<%=VPLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>')">
												<%}else{ %>
													<input type="button" class="btn btn-warning com-btn" value="Submit" 
													onclick="doSubmit('<%=i%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
													'<%=VPLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>')" disabled>
												<%} %>
													
												</div>
											</div>
										</td>
										<td align="center">
											<%if(print_access.equals("Y")){ %>
												<%if(!VPDF_EXISTS.elementAt(i).equals("Y")){ %>
													<font color="red">
														<i class="fa fa-file-pdf-o fa-2x" 
														onclick="printPDF('<%=i%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
														'<%=VPLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=from_date%>','<%=to_date%>','<%=month_nm%>')"></i>
													</font>
												<%}else{%>
													<font color="black">
														<i class="fa fa-eye fa-2x" 
														onclick="viewPDF('<%=file_url%><%=VPDF_FILE_NAME.elementAt(i)%>')"></i>
													</font>
												<%} %>
											<%}else{ %>
											<font color="grey">
												<i class="fa fa-file-pdf-o fa-2x" ></i>
											</font>
											<%} %>
										</td>
										<td align="center">
										<%if(VPDF_EXISTS.elementAt(i).equals("Y") && execute_access.equals("Y")){ %>
											<font color="blue">
												<i class="fa fa-envelope-o fa-2x"
												onclick="sendMail('<%=i%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VCONT_NO.elementAt(i)%>','<%=VAGMT_NO.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>',
												'<%=VPLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','<%=from_date%>','<%=to_date%>','<%=month_nm%>')"></i>
											</font>
											<%}else{ %>
											<font color="grey">
												<i class="fa fa-envelope-o fa-2x"></i>
											</font>
											<%} %>
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
<input type="hidden" name="option" value="BUYER_NOM_TO_TRD_RMK">
<input type="hidden" name="counterparty_cd" value="">
<input type="hidden" name="cont_no" value="">
<input type="hidden" name="agmt_no" value="">
<input type="hidden" name="contract_type" value="">
<input type="hidden" name="plant_seq" value="">
<input type="hidden" name="bu_plant_seq" value="">
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