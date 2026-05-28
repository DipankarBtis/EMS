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
	
	var url = "rpt_daily_joint_ticket.jsp?gas_dt="+gas_dt+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh_rpt(msg,msg_type)
{
	var gas_dt = document.forms[0].gas_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_daily_joint_ticket.jsp?gas_dt="+gas_dt+"&msg="+msg+"&msg_type="+msg_type+"&u="+u;

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
function viewJT(index,counterparty_cd,contract_type,plant_seq,bu_plant_seq,file,pdf_name)
{
	var contact_person_cd = document.getElementById("contact_person_cd"+index).value;
	
	var gas_dt = document.forms[0].gas_dt.value;
	var u = document.forms[0].u.value;
	var url = "";
	if(file=="XLS")
	{
		url = "rpt_view_joint_ticket.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&plant_seq="+plant_seq+
		"&bu_plant_seq="+bu_plant_seq+"&gas_dt="+gas_dt+"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
		
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
	}
	else
	{
		if(trim(contact_person_cd)!="")
		{
			//var url = "";
			if(file=="MAIL")
			{
				url = "frm_send_mail.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&plant_seq="+plant_seq+
				"&bu_plant_seq="+bu_plant_seq+"&gas_dt="+gas_dt+"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
				
				if(!newWindow || newWindow.closed)
				{
					newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
				}
				else
				{
					newWindow.close();
					newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
				}
			}
			else if(file=="PDF" && pdf_name!="")
			{
				var a = confirm("Existing PDF file will be overwritten!\nDo you want to continue?")
				if(a)
				{
					url = "rpt_view_joint_ticket.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&plant_seq="+plant_seq+
					"&bu_plant_seq="+bu_plant_seq+"&gas_dt="+gas_dt+"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
					
					if(!newWindow || newWindow.closed)
					{
						newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
					}
					else
					{
						newWindow.close();
						newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
					}
				}
			}
			else
			{
				url = "rpt_view_joint_ticket.jsp?counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&plant_seq="+plant_seq+
				"&bu_plant_seq="+bu_plant_seq+"&gas_dt="+gas_dt+"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
				
				if(!newWindow || newWindow.closed)
				{
					newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
				}
				else
				{
					newWindow.close();
					newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
				}
			}
			
		}
		else
		{
			alert("Select Contact Person for associated ROW("+(parseInt(index)+1)+")");
		}
	}
}

function checkAll(obj)
{
	var chk = document.forms[0].chk;
	var contact_person_cd = document.forms[0].contact_person_cd;
	if(chk!=null && chk.length!=undefined)
	{
		for(var i=0; i<chk.length; i++)
		{
			if(obj.checked)
			{
				if(contact_person_cd[i].value != "")
				{
					chk[i].checked=true;
				}
			}
			else
			{
				chk[i].checked=false;
			}
		}
	}
	else
	{
		if(obj.checked)
		{
			if(contact_person_cd.value != "")
			{
				chk.checked=true;
			}
		}
		else
		{
			chk.checked=false;
		}
	}
}

function chkContactPerson(index)
{
	var chk = document.getElementById('chk'+index); 
	var contact_person_cd = document.getElementById('contact_person_cd'+index).value;
	var msg="";
	if(trim(contact_person_cd) == "")
	{
		alert("Select Addressee first for ROW - "+(parseInt(index)+1));
		chk.checked=false;
	}
}

function printAllPDF()
{
	var chk = document.forms[0].chk;
	var gas_dt = document.forms[0].gas_dt.value;
	var file="ALL_PDF";
	var u = document.forms[0].u.value;
	
	var all_contract_type="";
	var all_counterparty_cd="";
	var all_plant_seq="";
	var all_contact_person_cd="";
	var all_bu_seq="";
	
	var counterparty_cd=document.forms[0].counterpartyCd;
	var plant_seq=document.forms[0].plantSeq;
	var contract_type=document.forms[0].contractTyp;
	var contact_person_cd=document.forms[0].contact_person_cd;
	var bu_seq=document.forms[0].bu_seq;
	
	var flag=false;
	if(chk!=undefined)
	{
		if(chk.length==undefined)
		{
			if(chk.checked)
			{
				if(all_counterparty_cd=="")
		        {
					all_contract_type=contract_type.value;
					all_counterparty_cd=counterparty_cd.value;
					all_plant_seq=plant_seq.value;
					all_contact_person_cd=contact_person_cd.value;
					all_bu_seq=bu_seq.value;
		        }
	          	else
	          	{
	          		all_contract_type=all_contract_type+"@@"+contract_type.value;
					all_counterparty_cd=all_counterparty_cd+"@@"+counterparty_cd.value;
					all_plant_seq=all_plant_seq+"@@"+plant_seq.value;
					all_contact_person_cd=all_contact_person_cd+"@@"+contact_person_cd.value;
					all_bu_seq=all_bu_seq+"@@"+bu_seq.value;
          		}
	          	flag=true;
			}
		}
		else
		{
			for(var i=0;i<chk.length;i++)
			{
				if(document.getElementById('chk'+i).checked==true)
				{ 
					if(all_counterparty_cd=="")
			        {
						all_contract_type=contract_type[i].value;
						all_counterparty_cd=counterparty_cd[i].value;
						all_plant_seq=plant_seq[i].value;
						all_contact_person_cd=contact_person_cd[i].value;
						all_bu_seq=bu_seq[i].value;
			        }
		          	else
		          	{
		          		all_contract_type=all_contract_type+"@@"+contract_type[i].value;
						all_counterparty_cd=all_counterparty_cd+"@@"+counterparty_cd[i].value;
						all_plant_seq=all_plant_seq+"@@"+plant_seq[i].value;
						all_contact_person_cd=all_contact_person_cd+"@@"+contact_person_cd[i].value;
						all_bu_seq=all_bu_seq+"@@"+bu_seq[i].value;
	          		}
					flag=true;
				}
			}
		}
	}
	
	if(flag==true)
	{
		url = "rpt_view_joint_ticket.jsp?gas_dt="+gas_dt+"&contract_type="+all_contract_type+
		"&counterparty_cd="+all_counterparty_cd+"&plant_seq="+all_plant_seq+	
		"&contact_person_cd="+all_contact_person_cd+"&file="+file+"&bu_plant_seq="+all_bu_seq+"&u="+u;
	
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
	}
	else
	{
		alert("Select Joint Ticket for which PDF have to be generated!");
	}
}

function sendAllMail()
{
	var chk = document.forms[0].chk;
	var gas_dt = document.forms[0].gas_dt.value;
	var file="ALL_MAIL";
	var u = document.forms[0].u.value;
	
	var all_contract_type="";
	var all_counterparty_cd="";
	var all_plant_seq="";
	var all_contact_person_cd="";
	var all_bu_seq="";
	
	var counterparty_cd=document.forms[0].counterpartyCd;
	var plant_seq=document.forms[0].plantSeq;
	var contract_type=document.forms[0].contractTyp;
	var contact_person_cd=document.forms[0].contact_person_cd;
	var bu_seq=document.forms[0].bu_seq;
	
	var flag=false;
	if(chk!=undefined)
	{
		if(chk.length==undefined)
		{
			if(chk.checked)
			{
				if(all_counterparty_cd=="")
		        {
					all_contract_type=contract_type.value;
					all_counterparty_cd=counterparty_cd.value;
					all_plant_seq=plant_seq.value;
					all_contact_person_cd=contact_person_cd.value;
					all_bu_seq=bu_seq.value;
		        }
	          	else
	          	{
	          		all_contract_type=all_contract_type+"@@"+contract_type.value;
					all_counterparty_cd=all_counterparty_cd+"@@"+counterparty_cd.value;
					all_plant_seq=all_plant_seq+"@@"+plant_seq.value;
					all_contact_person_cd=all_contact_person_cd+"@@"+contact_person_cd.value;
					all_bu_seq=all_bu_seq+"@@"+bu_seq.value;
          		}
	          	flag=true;
			}
		}
		else
		{
			for(var i=0;i<chk.length;i++)
			{
				if(document.getElementById('chk'+i).checked==true)
				{ 
					if(all_counterparty_cd=="")
			        {
						all_contract_type=contract_type[i].value;
						all_counterparty_cd=counterparty_cd[i].value;
						all_plant_seq=plant_seq[i].value;
						all_contact_person_cd=contact_person_cd[i].value;
						all_bu_seq=bu_seq[i].value;
			        }
		          	else
		          	{
		          		all_contract_type=all_contract_type+"@@"+contract_type[i].value;
						all_counterparty_cd=all_counterparty_cd+"@@"+counterparty_cd[i].value;
						all_plant_seq=all_plant_seq+"@@"+plant_seq[i].value;
						all_contact_person_cd=all_contact_person_cd+"@@"+contact_person_cd[i].value;
						all_bu_seq=all_bu_seq+"@@"+bu_seq[i].value;
	          		}
					flag=true;
				}
			}
		}
	}
	
	if(flag==true)
	{
		var a = confirm("Email will be sent for generated PDF lines only?")
		if(a)
		{
			url = "frm_send_mail.jsp?gas_dt="+gas_dt+"&contract_type="+all_contract_type+
			"&counterparty_cd="+all_counterparty_cd+"&plant_seq="+all_plant_seq+	
			"&contact_person_cd="+all_contact_person_cd+"&file="+file+"&bu_plant_seq="+all_bu_seq+"&u="+u;
		
			if(!newWindow || newWindow.closed)
			{
				newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
			}
			else
			{
				newWindow.close();
				newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
			}
		}
	}
	else
	{
		alert("Select Joint Ticket for which mail have to be Sent!");
	}
}

function openPDF(url)
{
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Joint Ticket","top=10,left=10,width=1100,height=900,scrollbars=1");
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

String path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.join_ticket_pdf_path;
String file_path = request.getRealPath(path);

cont_mgmt.setCallFlag("JOINT_TICKET");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.setFile_path(file_path);
cont_mgmt.init();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCONT_NO = cont_mgmt.getVCONT_NO();
Vector VCONT_REV_NO = cont_mgmt.getVCONT_REV_NO();
Vector VAGMT_NO = cont_mgmt.getVAGMT_NO();
Vector VAGMT_REV_NO = cont_mgmt.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = cont_mgmt.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = cont_mgmt.getVCONTRACT_TYPE_NM();
Vector VDIS_CONT_MAPPING = cont_mgmt.getVDIS_CONT_MAPPING();
Vector VCONT_REF = cont_mgmt.getVCONT_REF();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();
Vector VBU_PLANT_SEQ = cont_mgmt.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = cont_mgmt.getVBU_PLANT_ABBR();
Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_SCM = cont_mgmt.getVQTY_SCM();
Vector VSELLER_NOM = cont_mgmt.getVSELLER_NOM();
Vector VCONTACT_PERSON = cont_mgmt.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = cont_mgmt.getVCONTACT_PERSON_CD();
Vector VSEL_CONTACT_PERSON_CD = cont_mgmt.getVSEL_CONTACT_PERSON_CD();
Vector VPDF_EXISTS = cont_mgmt.getVPDF_EXISTS();

Vector VADDRESSED_PERSON = cont_mgmt.getVADDRESSED_PERSON();
Vector VPDF_NAME = cont_mgmt.getVPDF_NAME();
Vector VPDF_PATH = cont_mgmt.getVPDF_PATH();
Vector VEMAIL_FLAG = cont_mgmt.getVEMAIL_FLAG();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;
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
					    	Daily Joint Ticket
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
				<%if(VCOUNTERPARTY_CD.size()>0){ %>
					<div class="row m-b-5">
						<div class="d-flex justify-content-end">
						    <div class="email-icon-wrapper">
						    	<%if(print_access.equals("Y")){ %>
						        <span class="fa-stack fa-lg" style="position: relative;" title="Print all Selected" onclick="printAllPDF()">
						        	<i class="fa fa-print fa-stack-2x" style="position: absolute; left: -1.3em; top: -0.2em; color:var(--temp_data_highlight);"></i>
						            <i class="fa fa-print fa-stack-2x" style="position: absolute; left: -1.5em; top: 0em; color:#800000;"></i>
						        </span>
						        <%} %>
						    </div>
						    <div class="email-icon-wrapper">
						    	<%if(execute_access.equals("Y")){ %>
						        <span class="fa-stack fa-lg" style="position: relative;" title="send all Selected" onclick="sendAllMail()">
						            <i class="fa fa-envelope fa-stack-2x" style="position: absolute; left: -0.4em; top: -0.2em; color:var(--temp_data_highlight);"></i>
						            <i class="fa fa-envelope fa-stack-2x text-primary" style="position: absolute; left: -0.7em; top: 0em;"></i>
						        </span>
						        <%} %>
						    </div>
						</div>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="filterbysearch">
								<thead>
									<tr>
										<th rowspan="2"><input type="checkbox" class="form-check-input" name="chk_all" id="chk_all" onclick="checkAll(this);"></th>
										<th rowspan="2">Customer<div align="center"><input class="form-control form-control-sm" type="text" id="customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Contract Type<div align="center"><input class="form-control form-control-sm" type="text" id="contract_type" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Customer Plant<div align="center"><input class="form-control form-control-sm" type="text" id="plant" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Business Unit<div align="center"><input class="form-control form-control-sm" type="text" id="bu" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Seller Nomination<br>(MMBTU)</th>
										<th colspan="2">Alloc Qty</th>
										<th rowspan="2">Contact Person</th>
										<th colspan="5">Reports</th>
									</tr>
									<tr>
										<th>MMBTU</th>
										<th>SCM</th>
										<th>View</th>
										<th>Excel</th>
										<th>Print PDF</th>
										<th>View PDF</th>
										<th>Email</th>
									</tr>
								</thead>
								<tbody>
								
									<%for(int i=0;i<VCOUNTERPARTY_CD.size(); i++){ 
										String address_person="";
										if(!VADDRESSED_PERSON.elementAt(i).equals(""))
										{
											address_person=""+VADDRESSED_PERSON.elementAt(i);
										}
										else
										{
											address_person=""+VSEL_CONTACT_PERSON_CD.elementAt(i);
										}
									%>
										<tr>
											<td align="center"><input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" onclick="chkContactPerson(<%=i%>)">
												<input type="hidden" name="counterpartyCd" value="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
												<input type="hidden" name="contractTyp" value="<%=VCONTRACT_TYPE.elementAt(i) %>">
												<input type="hidden" name="plantSeq" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(i)%>">
												<input type="hidden" name="bu_seq" value="<%=VBU_PLANT_SEQ.elementAt(i)%>">
											</td>
											<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
											<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%></td>
											<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(i)%></td>
											<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
											<td align="right"><%=VSELLER_NOM.elementAt(i)%></td>
											<td align="right"><%=VQTY_MMBTU.elementAt(i)%></td>
											<td align="right"><%=VQTY_SCM.elementAt(i)%></td>
											<td align="center">
												<div class="row" style="width:150px;">
													<select class="form-select form-select-sm" name="contact_person_cd" id="contact_person_cd<%=i%>">
													<%for(int j=0; j<((Vector)VCONTACT_PERSON_CD.elementAt(i)).size(); j++){ %>
														<option value="<%=((Vector)VCONTACT_PERSON_CD.elementAt(i)).elementAt(j)%>"><%=((Vector)VCONTACT_PERSON.elementAt(i)).elementAt(j)%></option>
													<%} %>
													</select>
												<script>document.getElementById("contact_person_cd<%=i%>").value="<%=address_person%>"</script>
											</div>
											</td>
											<td align="center">
												<i class="fa fa-eye fa-2x" onclick="viewJT('<%=i%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','HTML','<%=VPDF_NAME.elementAt(i)%>');"></i>
											</td>
											<td align="center">
											<%if(print_access.equals("Y")){ %>
												<i class="fa fa-file-excel-o fa-2x excel_icon" onclick="viewJT('<%=i%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','XLS','<%=VPDF_NAME.elementAt(i)%>');"></i>
											<%}else{ %>
												<i class="fa fa-file-excel-o fa-2x excel_icon" style="color:grey;"></i>
											<%} %>
											</td>
											<td align="center">
											<%if(print_access.equals("Y")){ %>
												<i class="fa fa-print fa-2x" style="color:#800000;" onclick="viewJT('<%=i%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','PDF','<%=VPDF_NAME.elementAt(i)%>');"></i>
											<%}else{ %>
												<i class="fa fa-print fa-2x" style="color:grey;"></i>
											<%} %>
											</td>
											<td align="center">
											<%if(!VPDF_NAME.elementAt(i).equals("")){ %>
												<i class="fa fa-file-pdf-o fa-2x pdf_icon" onclick="openPDF('<%=file_url%><%=VPDF_PATH.elementAt(i)%><%=VPDF_NAME.elementAt(i)%>')"></i>
											<%}else{ %>
												<i class="fa fa-file-pdf-o fa-2x pdf_icon" style="color:grey;"></i>
											<%} %>
											</td>
											<td align="center">
												<%if(VEMAIL_FLAG.elementAt(i).equals("Y")){ %>
												<i class="fa fa-envelope-o fa-2x mail_icon" style="opacity: .65; color: green;" title="Mail already Sent!" onclick="viewJT('<%=i%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','MAIL','<%=VPDF_NAME.elementAt(i)%>');"></i>
												<%}else if(VPDF_EXISTS.elementAt(i).equals("Y") && execute_access.equals("Y")){ %>
												<i class="fa fa-envelope-o fa-2x mail_icon" onclick="viewJT('<%=i%>','<%=VCOUNTERPARTY_CD.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(i)%>','<%=VBU_PLANT_SEQ.elementAt(i)%>','MAIL','<%=VPDF_NAME.elementAt(i)%>');"></i>
												<%}else{ %>
												<i class="fa fa-envelope-o fa-2x" style="opacity: .65; color: gray;" title="PDF is not yet generated!"></i>
												<%} %>
											</td>
										</tr>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%}else{ %>
						<div align="center">
							<%=utilmsg.infoMessage("<b>No Allocation Done for Selected Gas Day!</b>") %>
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
<script type="text/javascript">
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch");
  	
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