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
	
	var url = "rpt_dlng_seller_nomination_to_customer.jsp?gas_dt="+gas_dt+"&contract_type="+mst_contract_type+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh_rpt(msg,msg_type)
{
	var gas_dt = document.forms[0].gas_dt.value;
	var mst_contract_type = document.forms[0].mst_contract_type.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_dlng_seller_nomination_to_customer.jsp?gas_dt="+gas_dt+"&contract_type="+mst_contract_type+"&msg="+msg+"&msg_type="+msg_type+"&u="+u;

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

function doSubmit(index,COUNTERPARTY_CD,CONT_NO,AGMT_NO,CONTRACT_TYPE,PLANT_SEQ,CARGO_NO)
{
	var rmk = document.getElementById('rmk'+index).value;
	var contact_person_cd = document.getElementById('contact_person_cd'+index).value;
	
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
		 	document.forms[0].cargo_no.value=CARGO_NO;
		 	document.forms[0].addressed_to.value=contact_person_cd;
		 	document.forms[0].submit();
		}
	}
}

var newWindow;
function viewSellerNomiToCust(index,COUNTERPARTY_CD,CONT_NO,AGMT_NO,CONTRACT_TYPE,PLANT_SEQ,file,CARGO_NO,PDF_NAME,BU_PLANT_SEQ)
{
	var contact_person_cd = document.getElementById("contact_person_cd"+index).value;
	var rmk = document.getElementById('rmk'+index).value;
	var gas_dt = document.forms[0].gas_dt.value;
	var u = document.forms[0].u.value;
	var url="";
	if(file=="XLS")
	{
		url = "rpt_dlng_view_seller_nomination_to_customer.jsp?gas_dt="+gas_dt+"&contract_type="+CONTRACT_TYPE+
		"&counterparty_cd="+COUNTERPARTY_CD+"&agmt_no="+AGMT_NO+"&cont_no="+CONT_NO+"&plant_seq="+PLANT_SEQ+	
		"&file="+file+"&cargo_no="+CARGO_NO+"&remark="+rmk+"&bu_plant_seq="+BU_PLANT_SEQ+"&u="+u;
		
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
		if(trim(contact_person_cd)!="")
		{
			//var url = "";
			if(file=="MAIL")
			{
				url = "frm_dlng_seller_nom_to_cust_send_mail.jsp?gas_dt="+gas_dt+"&contract_type="+CONTRACT_TYPE+
					"&counterparty_cd="+COUNTERPARTY_CD+"&agmt_no="+AGMT_NO+"&cont_no="+CONT_NO+"&plant_seq="+PLANT_SEQ+	
					"&contact_person_cd="+contact_person_cd+"&file="+file+"&cargo_no="+CARGO_NO+"&remark="+rmk+"&bu_plant_seq="+BU_PLANT_SEQ+"&u="+u;
				
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
			else if(file=="PDF" && PDF_NAME!="")
			{
				var a = confirm("Existing PDF file will be overwritten!\nDo you want to continue?")
				if(a)
				{
					url = "rpt_dlng_view_seller_nomination_to_customer.jsp?gas_dt="+gas_dt+"&contract_type="+CONTRACT_TYPE+
					"&counterparty_cd="+COUNTERPARTY_CD+"&agmt_no="+AGMT_NO+"&cont_no="+CONT_NO+"&plant_seq="+PLANT_SEQ+	
					"&contact_person_cd="+contact_person_cd+"&file="+file+"&cargo_no="+CARGO_NO+"&remark="+rmk+"&bu_plant_seq="+BU_PLANT_SEQ+"&u="+u;
					
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
			}
			else
			{
				url = "rpt_dlng_view_seller_nomination_to_customer.jsp?gas_dt="+gas_dt+"&contract_type="+CONTRACT_TYPE+
					"&counterparty_cd="+COUNTERPARTY_CD+"&agmt_no="+AGMT_NO+"&cont_no="+CONT_NO+"&plant_seq="+PLANT_SEQ+	
					"&contact_person_cd="+contact_person_cd+"&file="+file+"&cargo_no="+CARGO_NO+"&remark="+rmk+"&bu_plant_seq="+BU_PLANT_SEQ+"&u="+u;
				
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
			
		}
		else
		{
			alert("Select Contact Person for associated ROW("+(parseInt(index)+1)+")");
		}
	}
}

function openPDF(url)
{
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
	var all_agmt_no="";
	var all_cont_no="";
	var all_plant_seq="";
	var all_contact_person_cd="";
	var all_cargo_no="";
	var all_rmk="";
	
	var counterparty_cd=document.forms[0].counterpartyCd;
	var cont_no=document.forms[0].contNo;
	var agmt_no=document.forms[0].agmtNo;
	var plant_seq=document.forms[0].plantSeq;
	var contract_type=document.forms[0].contractTyp;
	var cargo_no=document.forms[0].cargoNo;
	var rmk=document.forms[0].rmk;
	var contact_person_cd=document.forms[0].contact_person_cd;
	
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
					all_agmt_no=agmt_no.value;
					all_cont_no=cont_no.value;
					all_plant_seq=plant_seq.value;
					all_contact_person_cd=contact_person_cd.value;
					all_cargo_no=cargo_no.value;
					all_rmk=rmk.value;
		        }
	          	else
	          	{
	          		all_contract_type=all_contract_type+"@@"+contract_type.value;
					all_counterparty_cd=all_counterparty_cd+"@@"+counterparty_cd.value;
					all_agmt_no=all_agmt_no+"@@"+agmt_no.value;
					all_cont_no=all_cont_no+"@@"+cont_no.value;
					all_plant_seq=all_plant_seq+"@@"+plant_seq.value;
					all_contact_person_cd=all_contact_person_cd+"@@"+contact_person_cd.value;
					all_cargo_no=all_cargo_no+"@@"+cargo_no.value;
					all_rmk=all_rmk+"@@"+rmk.value;
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
						all_agmt_no=agmt_no[i].value;
						all_cont_no=cont_no[i].value;
						all_plant_seq=plant_seq[i].value;
						all_contact_person_cd=contact_person_cd[i].value;
						all_cargo_no=cargo_no[i].value;
						all_rmk=rmk[i].value;
			        }
		          	else
		          	{
		          		all_contract_type=all_contract_type+"@@"+contract_type[i].value;
						all_counterparty_cd=all_counterparty_cd+"@@"+counterparty_cd[i].value;
						all_agmt_no=all_agmt_no+"@@"+agmt_no[i].value;
						all_cont_no=all_cont_no+"@@"+cont_no[i].value;
						all_plant_seq=all_plant_seq+"@@"+plant_seq[i].value;
						all_contact_person_cd=all_contact_person_cd+"@@"+contact_person_cd[i].value;
						all_cargo_no=all_cargo_no+"@@"+cargo_no[i].value;
						all_rmk=all_rmk+"@@"+rmk[i].value;
	          		}
					flag=true;
				}
			}
		}
	}
	
	if(flag==true)
	{
		url = "rpt_dlng_view_seller_nomination_to_customer.jsp?gas_dt="+gas_dt+"&contract_type="+all_contract_type+
		"&counterparty_cd="+all_counterparty_cd+"&agmt_no="+all_agmt_no+"&cont_no="+all_cont_no+"&plant_seq="+all_plant_seq+	
		"&contact_person_cd="+all_contact_person_cd+"&file="+file+"&cargo_no="+all_cargo_no+"&remark="+all_rmk+"&u="+u;
	
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
		alert("Select Nominations for which PDF have to be generated!");
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
	var all_agmt_no="";
	var all_cont_no="";
	var all_plant_seq="";
	var all_contact_person_cd="";
	var all_cargo_no="";
	var all_rmk="";
	var all_bu_plant_seq="";
	
	var counterparty_cd=document.forms[0].counterpartyCd;
	var cont_no=document.forms[0].contNo;
	var agmt_no=document.forms[0].agmtNo;
	var plant_seq=document.forms[0].plantSeq;
	var contract_type=document.forms[0].contractTyp;
	var cargo_no=document.forms[0].cargoNo;
	var rmk=document.forms[0].rmk;
	var contact_person_cd=document.forms[0].contact_person_cd;
	var bu_plant_seq=document.forms[0].bu_plant_seq;
	
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
					all_agmt_no=agmt_no.value;
					all_cont_no=cont_no.value;
					all_plant_seq=plant_seq.value;
					all_contact_person_cd=contact_person_cd.value;
					all_cargo_no=cargo_no.value;
					all_rmk=rmk.value;
					all_bu_plant_seq=bu_plant_seq.value;
		        }
	          	else
	          	{
	          		all_contract_type=all_contract_type+"@@"+contract_type.value;
					all_counterparty_cd=all_counterparty_cd+"@@"+counterparty_cd.value;
					all_agmt_no=all_agmt_no+"@@"+agmt_no.value;
					all_cont_no=all_cont_no+"@@"+cont_no.value;
					all_plant_seq=all_plant_seq+"@@"+plant_seq.value;
					all_contact_person_cd=all_contact_person_cd+"@@"+contact_person_cd.value;
					all_cargo_no=all_cargo_no+"@@"+cargo_no.value;
					all_rmk=all_rmk+"@@"+rmk.value;
					all_bu_plant_seq=all_bu_plant_seq+"@@"+bu_plant_seq.value;
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
						all_agmt_no=agmt_no[i].value;
						all_cont_no=cont_no[i].value;
						all_plant_seq=plant_seq[i].value;
						all_contact_person_cd=contact_person_cd[i].value;
						all_cargo_no=cargo_no[i].value;
						all_rmk=rmk[i].value;
						all_bu_plant_seq=bu_plant_seq[i].value;
			        }
		          	else
		          	{
		          		all_contract_type=all_contract_type+"@@"+contract_type[i].value;
						all_counterparty_cd=all_counterparty_cd+"@@"+counterparty_cd[i].value;
						all_agmt_no=all_agmt_no+"@@"+agmt_no[i].value;
						all_cont_no=all_cont_no+"@@"+cont_no[i].value;
						all_plant_seq=all_plant_seq+"@@"+plant_seq[i].value;
						all_contact_person_cd=all_contact_person_cd+"@@"+contact_person_cd[i].value;
						all_cargo_no=all_cargo_no+"@@"+cargo_no[i].value;
						all_rmk=all_rmk+"@@"+rmk[i].value;
						all_bu_plant_seq=all_bu_plant_seq+"@@"+bu_plant_seq[i].value;
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
			url = "frm_dlng_seller_nom_to_cust_send_mail.jsp?gas_dt="+gas_dt+"&contract_type="+all_contract_type+
			"&counterparty_cd="+all_counterparty_cd+"&agmt_no="+all_agmt_no+"&cont_no="+all_cont_no+"&plant_seq="+all_plant_seq+	
			"&contact_person_cd="+all_contact_person_cd+"&file="+file+"&cargo_no="+all_cargo_no+"&remark="+all_rmk+"&bu_plant_seq="+all_bu_plant_seq+"&u="+u;
		
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
	}
	else
	{
		alert("Select Nominations for which mail have to be Sent!");
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

String gas_dt = request.getParameter("gas_dt")==null?nextdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "1");
String contract_type = request.getParameter("contract_type")==null?"0":request.getParameter("contract_type");

String path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.nom_to_customer_pdf_path;
String file_path = request.getRealPath(path);

dlng.setCallFlag("DLNG_SELLER_NOMINATION_TO_CUSTOMER");
dlng.setComp_cd(owner_cd);
dlng.setGas_dt(gas_dt);
dlng.setContract_type(contract_type);
dlng.setFile_path(file_path);
dlng.init();

Vector VMST_CONTRACT_TYPE = dlng.getVMST_CONTRACT_TYPE();
Vector VMST_CONTRACT_TYPE_NM = dlng.getVMST_CONTRACT_TYPE_NM();

Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();
Vector VCONTRACT_TYPE_NM = dlng.getVCONTRACT_TYPE_NM();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = dlng.getVCOUNTERPARTY_PLANT_ABBR();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();
Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VCONT_NO = dlng.getVCONT_NO();
Vector VCONT_REF = dlng.getVCONT_REF();
Vector VINDEX = dlng.getVINDEX();

Vector VCONTACT_PERSON = dlng.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = dlng.getVCONTACT_PERSON_CD();
Vector VSEL_CONTACT_PERSON_CD = dlng.getVSEL_CONTACT_PERSON_CD();
Vector VREMARK = dlng.getVREMARK();
Vector VPDF_EXISTS = dlng.getVPDF_EXISTS();
Vector VCARGO_NO = dlng.getVCARGO_NO();
Vector VADDRESSED_PERSON = dlng.getVADDRESSED_PERSON();
Vector VPDF_NAME = dlng.getVPDF_NAME();
Vector VPDF_PATH = dlng.getVPDF_PATH();
Vector VEMAIL_FLAG = dlng.getVEMAIL_FLAG();

Vector VBU_PLANT_SEQ = dlng.getVBU_PLANT_SEQ();

String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Dlng_ContractMgmt">

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
					    	DLNG Daily Seller Nomination To Customer
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
					<%int j=0,k=0;%>
					&nbsp;
					<div class="row m-b-5">
						<div class="d-flex justify-content-end">
						    <div class="email-icon-wrapper">
						        <span class="fa-stack fa-lg" style="position: relative;" title="Print all Selected" onclick="printAllPDF()">
						        	<i class="fa fa-print fa-stack-2x" style="position: absolute; left: -1.3em; top: -0.2em; color:var(--temp_data_highlight);"></i>
						            <i class="fa fa-print fa-stack-2x" style="position: absolute; left: -1.5em; top: 0em; color:#800000;"></i>
						        </span>
						    </div>
						    <div class="email-icon-wrapper">
						        <span class="fa-stack fa-lg" style="position: relative;" title="send all Selected" onclick="sendAllMail()">
						            <i class="fa fa-envelope fa-stack-2x" style="position: absolute; left: -0.4em; top: -0.2em; color:var(--temp_data_highlight);"></i>
						            <i class="fa fa-envelope fa-stack-2x text-primary" style="position: absolute; left: -0.7em; top: 0em;"></i>
						        </span>
						    </div>
						</div>
					</div>
					&nbsp;
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="filterbysearch">
								<thead>
									<tr>
										<th  valign="middle" rowspan="2"><input type="checkbox" class="form-check-input" name="chk_all" id="chk_all" onclick="checkAll(this);"></th>
										<th rowspan="2">Customer<div align="center"><input class="form-control form-control-sm" type="text" id="customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Plant Name<div align="center"><input class="form-control form-control-sm" type="text" id="plant_nm" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Contract Type<div align="center"><input class="form-control form-control-sm" type="text" id="cont_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Contract#<br>[Contract/Trade Ref#]<div align="center"><input class="form-control form-control-sm" type="text" id="contract" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Addressee</th>
										<th rowspan="2" colspan="2">Remarks</th>
										<th colspan="5">Report</th>
									</tr>
									<tr>	
										<th>View</th>
										<th>Excel</th>
										<th>Print PDF</th>
										<th>View PDF</th>
										<th>Email</th>
									</tr>
								</thead>
								<tbody>
								<%k=0;
								//if(index > 0)
								{ %>
									<%for(j=j;j<VCOUNTERPARTY_CD.size(); j++) 
									{
										k+=1;
										
										String address_person="";
										if(!VADDRESSED_PERSON.elementAt(j).equals(""))
										{
											address_person=""+VADDRESSED_PERSON.elementAt(j);
										}
										else
										{
											address_person=""+VSEL_CONTACT_PERSON_CD.elementAt(j);
										}
									%>
										<tr>
											<td align="center"><input type="checkbox" class="form-check-input" name="chk" id="chk<%=j%>" onclick="chkContactPerson(<%=j%>)">
												<input type="hidden" name="counterpartyCd" value="<%=VCOUNTERPARTY_CD.elementAt(j)%>">
												<input type="hidden" name="contNo" value="<%=VCONT_NO.elementAt(j)%>">
												<input type="hidden" name="agmtNo" value="<%=VAGMT_NO.elementAt(j)%>">
												<input type="hidden" name="contractTyp" value="<%=VCONTRACT_TYPE.elementAt(j) %>">
												<input type="hidden" name="plantSeq" value="<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>">
												<input type="hidden" name="cargoNo" value="<%=VCARGO_NO.elementAt(j)%>">
												<input type="hidden" name="bu_plant_seq" value="<%=VBU_PLANT_SEQ.elementAt(j)%>">
											</td>
											<td title="<%=VCOUNTERPARTY_ABBR.elementAt(j)%>"><%=VCOUNTERPARTY_NM.elementAt(j) %></td>
											<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(j)%></td>
											<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(j)%></td>
											<td>
												<font color="blue"><%=VDIS_CONT_MAPPING.elementAt(j)%></font>
												<%if(!VCONT_REF.elementAt(j).equals("")){ %>
												<br>[<%=VCONT_REF.elementAt(j)%>]	
												<%} %>
											</td>
											<td align="center">
												<div class="row" style="width:150px;">
													<select class="form-select form-select-sm" name="contact_person_cd" id="contact_person_cd<%=j%>">
													<%for(int l=0; l<((Vector)VCONTACT_PERSON_CD.elementAt(j)).size(); l++){ %>
														<option value="<%=((Vector)VCONTACT_PERSON_CD.elementAt(j)).elementAt(l)%>"><%=((Vector)VCONTACT_PERSON.elementAt(j)).elementAt(l)%></option>
													<%} %>
													</select>
													<script>document.getElementById("contact_person_cd<%=j%>").value="<%=address_person%>"</script>
												</div>
											</td>
											<td align="center">
												<div class="row" style="width:250px;">
													<textarea class="form-control form-control-sm"  name="rmk" id="rmk<%=j%>" rows="2" maxlength="150"><%=VREMARK.elementAt(j)%></textarea>
												</div>
											</td>
											<td align="center">
												<input type="button" class="btn btn-warning btn-sm com-btn" value="Submit" 
												onclick="doSubmit('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=VCONTRACT_TYPE.elementAt(j) %>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>','<%=VCARGO_NO.elementAt(j)%>')">
											</td>
											<td align="center">
												<i class="fa fa-eye fa-2x" 
												onclick="viewSellerNomiToCust('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=VCONTRACT_TYPE.elementAt(j) %>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>','HTML','<%=VCARGO_NO.elementAt(j)%>','<%=VPDF_NAME.elementAt(j)%>','<%=VBU_PLANT_SEQ.elementAt(j)%>')"></i>
											</td>
											<td align="center">
											<%if(print_access.equals("Y")){ %>
												<i class="fa fa-file-excel-o fa-2x excel_icon" 
												onclick="viewSellerNomiToCust('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=VCONTRACT_TYPE.elementAt(j) %>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>','XLS','<%=VCARGO_NO.elementAt(j)%>','<%=VPDF_NAME.elementAt(j)%>','<%=VBU_PLANT_SEQ.elementAt(j)%>')"></i>
											<%}else{ %>
												<i class="fa fa-file-excel-o fa-2x excel_icon" style="color:grey;"></i>
											<%} %>
											</td>
											<td align="center">
											<%if(print_access.equals("Y")){ %>
												<i class="fa fa-print fa-2x" style="color:#800000;"
												onclick="viewSellerNomiToCust('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=VCONTRACT_TYPE.elementAt(j) %>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>','PDF','<%=VCARGO_NO.elementAt(j)%>','<%=VPDF_NAME.elementAt(j)%>','<%=VBU_PLANT_SEQ.elementAt(j)%>')"></i>
											<%}else{ %>
												<i class="fa fa-print fa-2x" style="color:grey;"></i>
											<%} %>
											</td>
											<td align="center">
											<%if(!VPDF_NAME.elementAt(j).equals("")){ %>
												<i class="fa fa-file-pdf-o fa-2x pdf_icon" 
												onclick="openPDF('<%=file_url%><%=VPDF_PATH.elementAt(j)%><%=VPDF_NAME.elementAt(j)%>')"></i>
											<%}else{ %>
												<i class="fa fa-file-pdf-o fa-2x pdf_icon" style="color:grey;"></i>
											<%} %>
											</td>
											<td align="center">
												<%if(VEMAIL_FLAG.elementAt(j).equals("Y")){ %>
												<i class="fa fa-envelope-o fa-2x mail_icon" style="opacity: .65; color: green;" title="Mail already Sent!" onclick="viewSellerNomiToCust('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=VCONTRACT_TYPE.elementAt(j) %>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>','MAIL','<%=VCARGO_NO.elementAt(j)%>','<%=VPDF_NAME.elementAt(j)%>','<%=VBU_PLANT_SEQ.elementAt(j)%>');"></i>
												<%}else if(VPDF_EXISTS.elementAt(j).equals("Y") && execute_access.equals("Y")){ %>
												<i class="fa fa-envelope-o fa-2x mail_icon" onclick="viewSellerNomiToCust('<%=j%>','<%=VCOUNTERPARTY_CD.elementAt(j)%>','<%=VCONT_NO.elementAt(j)%>','<%=VAGMT_NO.elementAt(j)%>','<%=VCONTRACT_TYPE.elementAt(j) %>','<%=VCOUNTERPARTY_PLANT_SEQ.elementAt(j)%>','MAIL','<%=VCARGO_NO.elementAt(j)%>','<%=VPDF_NAME.elementAt(j)%>','<%=VBU_PLANT_SEQ.elementAt(j)%>');"></i>
												<%}else{ %>
												<i class="fa fa-envelope-o fa-2x" style="opacity: .65; color: gray;" title="PDF is not yet generated!"></i>
												<%} %>
											</td>
											
										</tr>
									<%} %>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
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

<input type="hidden" name="option" value="DLNG_SELLER_NOM_TO_CUST_REMARK">
<input type="hidden" name="counterparty_cd" value="">
<input type="hidden" name="cont_no" value="">
<input type="hidden" name="agmt_no" value="">
<input type="hidden" name="contract_type" value="">
<input type="hidden" name="plant_seq" value="">
<input type="hidden" name="remark" value="">
<input type="hidden" name="cargo_no" value="">
<input type="hidden" name="addressed_to" value="">
<input type="hidden" name="count" value="<%=VCOUNTERPARTY_CD.size()%>">

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