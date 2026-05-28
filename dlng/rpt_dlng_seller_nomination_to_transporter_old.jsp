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
	
	var url = "rpt_dlng_seller_nomination_to_transporter.jsp?gas_dt="+gas_dt+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function refresh_rpt(msg,msg_type)
{
	var gas_dt = document.forms[0].gas_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_dlng_seller_nomination_to_transporter.jsp?gas_dt="+gas_dt+"&msg="+msg+"&msg_type="+msg_type+"&u="+u;

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

function doSubmit(index,COUNTERPARTY_CD,TRUCK_CD)
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
			document.forms[0].transporter_cd.value=COUNTERPARTY_CD;
		 	document.forms[0].truck_cd.value=TRUCK_CD;
		 	document.forms[0].remark.value=rmk;
		 	document.forms[0].submit();
		}
	}
}

var newWindow;
function viewSellerNomiToTrans(index,COUNTERPARTY_CD,TRUCK_CD,file,PDF_NAME,contact_person)
{
	var contact_person_cd = contact_person.value;
	
	var gas_dt = document.forms[0].gas_dt.value;
	var u = document.forms[0].u.value;
	if(file=="XLS")
	{
		
		url = "rpt_dlng_view_seller_nomination_to_transporter.jsp?gas_dt="+gas_dt+"&counterparty_cd="+COUNTERPARTY_CD+"&truck_cd="+TRUCK_CD+	
		"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
		
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
	}
	else
	{
		if(trim(contact_person_cd)!="")
		{
			//var url = "";
			if(file=="MAIL")
			{
				url = "frm_dlng_seller_nom_to_trans_send_mail.jsp?gas_dt="+gas_dt+"&counterparty_cd="+COUNTERPARTY_CD+"&truck_cd="+TRUCK_CD+	
				"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
				
				if(!newWindow || newWindow.closed)
				{
					newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
				}
				else
				{
					newWindow.close();
					newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
				}
			}
			else if(file=="PDF" && PDF_NAME!="")
			{
				var a = confirm("Existing PDF file will be overwritten!\nDo you want to continue?")
				if(a)
				{
					url = "rpt_dlng_view_seller_nomination_to_transporter.jsp?gas_dt="+gas_dt+"&counterparty_cd="+COUNTERPARTY_CD+"&truck_cd="+TRUCK_CD+	
					"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
					
					if(!newWindow || newWindow.closed)
					{
						newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
					}
					else
					{
						newWindow.close();
						newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
					}
				}
			}
			else
			{
				url = "rpt_dlng_view_seller_nomination_to_transporter.jsp?gas_dt="+gas_dt+"&counterparty_cd="+COUNTERPARTY_CD+"&truck_cd="+TRUCK_CD+	
				"&contact_person_cd="+contact_person_cd+"&file="+file+"&u="+u;
				
				if(!newWindow || newWindow.closed)
				{
					newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
				}
				else
				{
					newWindow.close();
					newWindow = window.open(url,"Seller Nomination To CusTransportertomer","top=10,left=10,width=1100,height=900,scrollbars=1");
				}
			}
			
		}
		else
		{
			alert("Select Contact Person for associated ROW("+(parseInt(index)+1)+")");
		}
	}
	
}

function chkContactPerson()
{
	var i = parseInt('0');
	var trans_cnt = document.forms[0].trans_cnt.value;
	for(var k=0; k<parseInt(trans_cnt); k++)
	{
		var chk = document.getElementsByName('chk'+k);
		var contact_person_cd=document.getElementsByName('contact_person_cd'+k);
		
		if(chk!=undefined)
		{
			if(chk.length!=undefined)
			{
				for(var m=0;m<chk.length;m++)
				{
					if(document.getElementById('chk'+k+i).checked==true)
					{ 
						if(trim(document.getElementById('contact_person_cd'+k+i).value) == "")
						{
							alert("Select Addressee first for ROW - "+(parseInt(i)+1));
							document.getElementById('chk'+k+i).checked=false;
						}
					}
					i++;
				}
			}
		}
	}
}

function checkAll(obj, chk, contact_person_cd)
{
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

function printAllPDF()
{
	var trans_cnt = document.forms[0].trans_cnt.value;
	var msg="";
	
	var i = parseInt('0');
	var selCnt = parseInt('0');
	
	var all_counterparty_cd="";
	var all_truck_cd="";
	var all_contact_person_cd="";
	
	for(var k=0; k<parseInt(trans_cnt); k++)
	{
		var chk = document.getElementsByName('chk'+k);
		var gas_dt = document.forms[0].gas_dt.value;
		var file="ALL_PDF";
		var u = document.forms[0].u.value;
		
		var counterparty_cd=document.getElementsByName('counterpartyCd'+k);
		var truck_cd=document.getElementsByName('truckCd'+k);
		var contact_person_cd=document.getElementsByName('contact_person_cd'+k);
		
		var flag=false;
		if(chk!=undefined)
		{
			if(chk.length==undefined)
			{
				if(chk.checked)
				{
					if(all_counterparty_cd=="")
			        {
						all_counterparty_cd=counterparty_cd.value;
						all_truck_cd=truck_cd.value;
						all_contact_person_cd=contact_person_cd.value;
			        }
		          	else
		          	{
						all_counterparty_cd=all_counterparty_cd+"@@"+counterparty_cd.value;
						all_truck_cd=all_truck_cd+"@@"+truck_cd.value;
						all_contact_person_cd=all_contact_person_cd+"@@"+contact_person_cd.value;
	          		}
		          	flag=true;
		          	selCnt = selCnt+1;
				}
			}
			else
			{
				for(var m=0;m<chk.length;m++)
				{
					if(document.getElementById('chk'+k+i).checked==true)
					{ 
						if(all_counterparty_cd=="")
				        {
							all_counterparty_cd=document.getElementById('counterpartyCd'+k+i).value;
							all_truck_cd=document.getElementById('truckCd'+k+i).value;
							all_contact_person_cd=document.getElementById('contact_person_cd'+k+i).value;
				        }
			          	else
			          	{
							all_counterparty_cd=all_counterparty_cd+"@@"+document.getElementById('counterpartyCd'+k+i).value;
							all_truck_cd=all_truck_cd+"@@"+document.getElementById('truckCd'+k+i).value;
							all_contact_person_cd=all_contact_person_cd+"@@"+document.getElementById('contact_person_cd'+k+i).value;
		          		}
						flag=true;
						selCnt = selCnt+1;
					}
					i++;
				}
			}
		}
	}
	
	if(selCnt>0)
	{
		url ="rpt_dlng_view_seller_nomination_to_transporter.jsp?gas_dt="+gas_dt+"&counterparty_cd="+all_counterparty_cd+"&truck_cd="+all_truck_cd+	
		"&contact_person_cd="+all_contact_person_cd+"&file="+file+"&u="+u;
	
		if(!newWindow || newWindow.closed)
		{
			newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
		else
		{
			newWindow.close();
			newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
		}
	}
	else
	{
		msg="Select Nominations for which PDF have to be generated!";
		alert(msg);
	}
}

function sendAllMail()
{
	var trans_cnt = document.forms[0].trans_cnt.value;
	var msg="";
	
	var i = parseInt('0');
	var selCnt = parseInt('0');
	
	var all_counterparty_cd="";
	var all_truck_cd="";
	var all_contact_person_cd="";
	
	for(var k=0; k<parseInt(trans_cnt); k++)
	{
		var chk = document.getElementsByName('chk'+k);
		var gas_dt = document.forms[0].gas_dt.value;
		var file="ALL_MAIL";
		var u = document.forms[0].u.value;
		
		var counterparty_cd=document.getElementsByName('counterpartyCd'+k);
		var truck_cd=document.getElementsByName('truckCd'+k);
		var contact_person_cd=document.getElementsByName('contact_person_cd'+k);
		
		var flag=false;
		if(chk!=undefined)
		{
			if(chk.length==undefined)
			{
				if(chk.checked)
				{
					if(all_counterparty_cd=="")
			        {
						all_counterparty_cd=counterparty_cd.value;
						all_truck_cd=truck_cd.value;
						all_contact_person_cd=contact_person_cd.value;
			        }
		          	else
		          	{
						all_counterparty_cd=all_counterparty_cd+"@@"+counterparty_cd.value;
						all_truck_cd=all_truck_cd+"@@"+truck_cd.value;
						all_contact_person_cd=all_contact_person_cd+"@@"+contact_person_cd.value;
	          		}
		          	flag=true;
		          	selCnt = selCnt+1;
				}
			}
			else
			{
				for(var m=0;m<chk.length;m++)
				{
					if(document.getElementById('chk'+k+i).checked==true)
					{ 
						if(all_counterparty_cd=="")
				        {
							all_counterparty_cd=document.getElementById('counterpartyCd'+k+i).value;
							all_truck_cd=document.getElementById('truckCd'+k+i).value;
							all_contact_person_cd=document.getElementById('contact_person_cd'+k+i).value;
				        }
			          	else
			          	{
							all_counterparty_cd=all_counterparty_cd+"@@"+document.getElementById('counterpartyCd'+k+i).value;
							all_truck_cd=all_plant_seq+"@@"+document.getElementById('truckCd'+k+i).value;
							all_contact_person_cd=all_contact_person_cd+"@@"+document.getElementById('contact_person_cd'+k+i).value;
		          		}
						flag=true;
						selCnt = selCnt+1;
					}
					i++;
				}
			}
		}
	}
	
	if(selCnt>0)
	{
		var a = confirm("Email will be sent for generated PDF lines only?")
		if(a)
		{
			url ="frm_dlng_seller_nom_to_trans_send_mail.jsp?gas_dt="+gas_dt+"&counterparty_cd="+all_counterparty_cd+"&truck_cd="+all_truck_cd+	
			"&contact_person_cd="+all_contact_person_cd+"&file="+file+"&u="+u;
		
			if(!newWindow || newWindow.closed)
			{
				newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
			}
			else
			{
				newWindow.close();
				newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
			}
		}
	}
	else
	{
		msg="Select Nominations for which mail have to be Sent!";
		alert(msg);
	}
}

function openPDF(url)
{
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Seller Nomination To Transporter","top=10,left=10,width=1100,height=900,scrollbars=1");
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

String path ="/"+CommonVariable.work_dir+owner_cd+"/"+CommonVariable.nom_to_transporter_pdf_path;
String file_path = request.getRealPath(path);

dlng.setCallFlag("DLNG_SELLER_NOMINATION_TO_TRANSPORTER");
dlng.setComp_cd(owner_cd);
dlng.setGas_dt(gas_dt);
dlng.setFile_path(file_path);
dlng.init();

Vector VTRANSPORTER_CD = dlng.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = dlng.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_NM = dlng.getVTRANSPORTER_NM();
Vector VTRANSPORTER_TRUCK = dlng.getVTRANSPORTER_TRUCK();
Vector VTRANSPORTER_TRUCK_NO = dlng.getVTRANSPORTER_TRUCK_NO();

Vector VCONTACT_PERSON = dlng.getVCONTACT_PERSON();
Vector VCONTACT_PERSON_CD = dlng.getVCONTACT_PERSON_CD();
Vector VSEL_CONTACT_PERSON_CD = dlng.getVSEL_CONTACT_PERSON_CD();
Vector VREMARK = dlng.getVREMARK();
Vector VINDEX = dlng.getVINDEX();
Vector VPDF_EXISTS = dlng.getVPDF_EXISTS();

Vector VADDRESSED_PERSON = dlng.getVADDRESSED_PERSON();
Vector VPDF_NAME = dlng.getVPDF_NAME();
Vector VPDF_PATH = dlng.getVPDF_PATH();
Vector VEMAIL_FLAG = dlng.getVEMAIL_FLAG();

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
					    	Daily Seller Nomination To Transporter
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
					</div>
				</div>
				<div class="card-body cdbody">&nbsp;
				<%if(VTRANSPORTER_CD.size() >0){ %>
					<div class="row">
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
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%int j=0,k=0;
						for(int i=0; i<VTRANSPORTER_CD.size(); i++)
						{ 
							String trans_cd=""+VTRANSPORTER_CD.elementAt(i);
							int index=Integer.parseInt(""+VINDEX.elementAt(i));
						%>
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading<%=i%>">
										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=i%>" aria-expanded="false" aria-controls="collapse<%=i%>">
							    			<%=VTRANSPORTER_ABBR.elementAt(i)%>&nbsp;-&nbsp;<%=VTRANSPORTER_NM.elementAt(i)%>
							      		</button>
							    	</h2>
							    	<div id="collapse<%=i%>" class="accordion-collapse collapse" aria-labelledby="heading<%=i%>">
							      		<div class="accordion-body accor-body">
											<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered table-hover">
														<thead>
															<tr>
																<th rowspan="2"><input type="checkbox" class="form-check-input" id="chk_all<%=i %>" onclick="checkAll(this,document.forms[0].chk<%=i%>,document.forms[0].contact_person_cd<%=i%>);"></th>
																<th rowspan="2">Transporter Truck</th>
																<th rowspan="2">Addressee</th>
																<!-- <th rowspan="2" colspan="2">Remarks</th> -->
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
														if(index > 0){ %>
															<%for(j=j;j<VTRANSPORTER_TRUCK.size(); j++) 
															{
																String address_person="";
																if(!VADDRESSED_PERSON.elementAt(j).equals(""))
																{
																	address_person=""+VADDRESSED_PERSON.elementAt(j);
																}
																else
																{
																	address_person=""+VSEL_CONTACT_PERSON_CD.elementAt(j);
																}
																k+=1;
															%>
																<tr>
																	<td align="center"><input type="checkbox" class="form-check-input" name="chk<%=i%>" id="chk<%=i %><%=j%>" onclick="chkContactPerson(this,<%=j%>,document.forms[0].contact_person_cd<%=i%>)">
																		<input type="hidden" name="counterpartyCd<%=i %>" id="counterpartyCd<%=i %><%=j%>" value="<%=trans_cd%>">
																	</td>
																	<td><%=VTRANSPORTER_TRUCK_NO.elementAt(j)%>
																		<input type="hidden" name="truckCd<%=i %>" id="truckCd<%=i %><%=j%>" value="<%=VTRANSPORTER_TRUCK.elementAt(j)%>">
																	</td>
																	<td align="center">
																		<div class="row" style="width:150px;">
																			<select class="form-select form-select-sm" name="contact_person_cd<%=i%>" id="contact_person_cd<%=i%><%=j%>">
																			<%for(int l=0; l<((Vector)VCONTACT_PERSON_CD.elementAt(j)).size(); l++){ %>
																				<option value="<%=((Vector)VCONTACT_PERSON_CD.elementAt(j)).elementAt(l)%>"><%=((Vector)VCONTACT_PERSON.elementAt(j)).elementAt(l)%></option>
																			<%} %>
																			</select>
																			<script>document.getElementById("contact_person_cd<%=i%><%=j%>").value="<%=address_person%>"</script>
																		</div>
																	</td>
																	<%-- <td align="center">
																		<div class="row" style="width:250px;">
																			<textarea class="form-control form-control-sm"  name="rmk" id="rmk<%=j%>" rows="1" maxlength="150"><%=VREMARK.elementAt(j)%></textarea>
																		</div>
																	</td>
																	<td align="center">
																		<input type="button" class="btn btn-warning btn-sm com-btn" value="Submit" 
																		onclick="doSubmit('<%=j%>','<%=trans_cd%>','<%=VTRANSPORTER_PLANT_SEQ.elementAt(j)%>')">
																	</td> --%>
																	<td align="center">
																		<i class="fa fa-eye fa-2x" 
																		onclick="viewSellerNomiToTrans('<%=j%>','<%=trans_cd%>','<%=VTRANSPORTER_TRUCK.elementAt(j)%>','HTML','<%=VPDF_NAME.elementAt(j)%>',document.getElementById('contact_person_cd<%=i%><%=j%>'))"></i>
																	</td>
																	<td align="center">
																	<%if(print_access.equals("Y")){ %>
																		<i class="fa fa-file-excel-o fa-2x excel_icon" 
																		onclick="viewSellerNomiToTrans('<%=j%>','<%=trans_cd%>','<%=VTRANSPORTER_TRUCK.elementAt(j)%>','XLS','<%=VPDF_NAME.elementAt(j)%>',document.getElementById('contact_person_cd<%=i%><%=j%>'))"></i>
																	<%}else{ %>
																		<i class="fa fa-file-excel-o fa-2x excel_icon" style="color:grey;"></i>
																	<%} %>
																	</td>
																	<td align="center">
																		<%if(print_access.equals("Y")){ %>
																			<i class="fa fa-print fa-2x" style="color:#800000;"
																			onclick="viewSellerNomiToTrans('<%=j%>','<%=trans_cd%>','<%=VTRANSPORTER_TRUCK.elementAt(j)%>','PDF','<%=VPDF_NAME.elementAt(j)%>',document.getElementById('contact_person_cd<%=i%><%=j%>'))"></i>
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
																		<i class="fa fa-envelope-o fa-2x mail_icon" style="opacity: .65; color: green;" title="Mail already Sent!" onclick="viewSellerNomiToTrans('<%=j%>','<%=trans_cd%>','<%=VTRANSPORTER_TRUCK.elementAt(j)%>','MAIL','<%=VPDF_NAME.elementAt(j)%>',document.getElementById('contact_person_cd<%=i%><%=j%>'));"></i>
																		<%}else if(VPDF_EXISTS.elementAt(j).equals("Y") && execute_access.equals("Y")){ %>
																		<i class="fa fa-envelope-o fa-2x mail_icon" onclick="viewSellerNomiToTrans('<%=j%>','<%=trans_cd%>','<%=VTRANSPORTER_TRUCK.elementAt(j)%>','MAIL','<%=VPDF_NAME.elementAt(j)%>',document.getElementById('contact_person_cd<%=i%><%=j%>'))"></i>
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
										</div>
									</div>
								</div>
							</div>
						<%} %>
						</div>
					</div>
				<%}else{ %>
					<div align="center">
						<%=utilmsg.infoMessage("<b>Nomination is not done for Selected Gas Day!</b>") %>
					</div>
				<%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="SELLER_NOM_TO_TRANS_REMARK">
<input type="hidden" name="transporter_cd" value="">
<input type="hidden" name="truck_cd" value="">
<input type="hidden" name="remark" value="">
<input type="hidden" name="trans_cnt" value="<%=VTRANSPORTER_CD.size()%>">

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