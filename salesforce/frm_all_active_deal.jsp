<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var report_dt = document.forms[0].report_dt.value;

	var u = document.forms[0].u.value;
	
	var url = "frm_all_active_deal.jsp?u="+u+"&report_dt="+report_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function nextDate(day_no)
{
	var dt = document.forms[0].report_dt.value;
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
		
		document.forms[0].report_dt.value=to_dt;
		
		refresh();
	}
}
function xmlGeneration()
{
	var execute_access = document.forms[0].execute_access.value;
	
	document.forms[0].option.value = "GENERATE_CONTACT_DTL_XML";
	
	if(execute_access=="Y")
	{
		var a = confirm("Confirm Generation of SF XML ?");
		
		if(a)
		{
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
		//refresh();
	}
	else
	{
		alert("You don't have Execute Access!");
	}	
}

function chkAllCont(obj, checkbox_nm, flag_id)//dwn_checkbox,dwn_checkbox_flag
{
	if(checkbox_nm!=null && checkbox_nm.length!=undefined)
	{
		for(var i=0; i<checkbox_nm.length; i++)
		{
			if(obj.checked)
			{
				flag_id[i].disabled=false;
				checkbox_nm[i].checked=true;
				//flag_id[i].value="Y";
			}
			else
			{
				flag_id[i].disabled=true;
				checkbox_nm[i].checked=false;
				//flag_id[i].value="N";
			}
		}
	}
	else
	{
		if(obj.checked)
		{
			flag_id.disabled=false;
			checkbox_nm.checked=true;
			//flag_id.value="Y";
		}
		else
		{
			flag_id.disabled=true;
			checkbox_nm.checked=false;
			//flag_id.value="N";
		}
	}
}

function setAccessFlag(obj,id)
{
	if(obj.checked)
	{
		document.getElementById(id).disabled=false;
	}
	else
	{
		document.getElementById(id).disabled=true;
	}
}

function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var url = "xls_all_active_deal.jsp?fileName=Active Deals Report.xls&counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt;
	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.salesforce.DataBean_sf_interface" id="db_intrface" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String comp_abbr = utilBean.getCompanyAbbr(owner_cd);
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String report_dt = request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");

db_intrface.setCallFlag("SF_ACTIVE_DEAL");
db_intrface.setComp_cd(owner_cd);
db_intrface.setCounterparty_cd(counterparty_cd);
db_intrface.setReport_dt(report_dt);
db_intrface.init();

Vector VMST_COUNTERPARTY_CD = db_intrface.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = db_intrface.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = db_intrface.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD =  db_intrface.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM =  db_intrface.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR =  db_intrface.getVCOUNTERPARTY_ABBR();
Vector VCONTRACT_TYPE =  db_intrface.getVCONTRACT_TYPE();
Vector VCONT_NO =  db_intrface.getVCONT_NO();
Vector VCONT_REF_NO =  db_intrface.getVCONT_REF_NO();
Vector VTRADE_REF_NO =  db_intrface.getVTRADE_REF_NO();
Vector VCONT_START_DT =  db_intrface.getVCONT_START_DT();
Vector VCONT_END_DT =  db_intrface.getVCONT_END_DT();
Vector VCONT_STATUS =  db_intrface.getVCONT_STATUS();
Vector VLAST_MODIFY_DT =  db_intrface.getVLAST_MODIFY_DT();
Vector VCARGO_NO =  db_intrface.getVCARGO_NO();
Vector VINDEX =  db_intrface.getVINDEX();
Vector VSEGMENT_NM =  db_intrface.getVSEGMENT_NM();
Vector VSF_XML_STATUS =  db_intrface.getVSF_XML_STATUS();
Vector VCONT_XML_GEN_MAPPING =  db_intrface.getVCONT_XML_GEN_MAPPING();
Vector VCONT_SF_XML_GEN_DT =  db_intrface.getVCONT_SF_XML_GEN_DT();

%>
<body onload="">
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_sf_interface">
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
				    		Active Deals
	   	 				</div>
						<!-- <div class="col-auto">
					 		<span class="input-group-text"><a id="rptDownloadBtn"><i title="Export To Excel" onclick="exportToXls();" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></a></span>
						</div> -->
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<%-- <div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--All--</option>
										<!-- <option value="-1">SEMTIPL-STORAGE</option> -->
										<%for(int i=0;i<VMST_COUNTERPARTY_ABBR.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div> --%>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Report Date</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
					      					<span class="input-group-text" onclick="nextDate('-1');" title="click for Back Date"><i class="fa fa-backward fa-lg"></i></span>
						      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="report_dt" id="report_dt" value="<%=report_dt%>" maxLength="10" 
						      				onchange="validateDate(this);refresh();">
						      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      					<span class="input-group-text" onclick="nextDate('1');" title="click for Next Date"><i class="fa fa-forward fa-lg"></i></span>
					      				</div>
									</div>
								</div>												
							</div>	
						</div>						
						<%if(execute_access.equals("Y")){ %>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="d-flex justify-content-end">
								<div class="form-group row">
									<div class="col-auto">
										<b>Generate SF XML</b>		
									</div>								
									<div class="col-auto">						
										<i class="fa fa-play-circle-o fa-2x" style="color: red;" onclick="xmlGeneration();" title="Kick-off XML Generation Process"></i>
									</div>
								</div>	
							</div>
						</div> 	
						<%} %>	
					</div>
				</div>
				<div class="card-body cdbody">
					<%if(VSEGMENT_NM.size()>0){ %>
					<%int j=0,k=0,l=0,m=0;
					for(int i=0; i<VSEGMENT_NM.size(); i++)
					{ 
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
						%>						
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=l%>">
	   										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    			<%=VSEGMENT_NM.elementAt(i)%>
								      		</button>	
								    	</h2>
										<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover serchtbl" id="example<%=i%>">
															<thead id="tbsearch<%=i%>">
																<tr>
																	<th>Check All
																		<br><input type="checkbox"  id="check_all<%=i%>>" class="form-check-input" 
																		onclick="chkAllCont(this,document.forms[0].dwn_checkbox<%=i%>,document.forms[0].dwn_checkbox_flag<%=i%>)"> 
																	 </th>
																	<th class="tbser<%=i%>">Customer Name</th>
																	<th class="tbser<%=i%>">Deal Type</th>	
																	<th class="tbser<%=i%>">Deal#</th>	
																	<th class="tbser<%=i%>">Contract/Trade Ref#</th>	
																	<th class="tbser<%=i%>">Contract Duration</th>	
																	<!-- <th>Buy/Sell</th>	 --><!-- As only sell Deals will come -->
																	<th class="tbser<%=i%>">Status</th>					
																	<th class="tbser<%=i%>">Last Updated</th>
																	<th class="tbser<%=i%>">Sales-force XML Status</th>
																	<!-- <th>Download</th> -->	
																</tr>
															</thead>
															<tbody>
																<%if(VCOUNTERPARTY_CD.size()>0 && index>0){
																	k=0; 
																	for(l=l; l<VCOUNTERPARTY_CD.size();l++){
																		k+=1;%>
																	<tr>
																		<td align="center">
																			<div align="center">
																				<%if(VSF_XML_STATUS.elementAt(l).equals("N")){%>
																					<input type="checkbox" class="form-check-input" name="dwn_checkbox<%=i%>" id="dwn_checkbox<%=i%><%=l%>" 
																					onclick="setAccessFlag(this,'dwn_checkbox_flag<%=i%><%=l%>');">
																					<input type="hidden" name="dwn_checkbox_flag<%=i%>" id="dwn_checkbox_flag<%=i%><%=l%>" value="<%=VCONT_XML_GEN_MAPPING.elementAt(l)%>" disabled="disabled">
																				<%}else{ %>
																					<input type="checkbox" name="dis_dwn_chkbox" disabled="disabled" checked>
																				<%} %>
																			</div>
																		</td>
																		<td align="center"><%=VCOUNTERPARTY_NM.elementAt(l) %></td>
																		<td align="center"><%=VCONTRACT_TYPE.elementAt(l) %></td>
																		<td align="center"><%=VCONT_NO.elementAt(l) %></td>
																		<td align="center"><%=VCONT_REF_NO.elementAt(l) %></td>
																		<td align="center"><%=VCONT_START_DT.elementAt(l) %> - <%=VCONT_END_DT.elementAt(l) %></td>
																		<td align="center"><%=VCONT_STATUS.elementAt(l) %></td>
																		<td align="center"><%=VLAST_MODIFY_DT.elementAt(l) %></td>
																		<td align="left">
																			<!-- <div align="center"> -->
																				<font style="color:<%if(VSF_XML_STATUS.elementAt(l).equals("N")){%>#a6ff4d<%}else{%>#FFBF00<%}%>">
																					<i class="fa fa-circle fa-lg" ></i>
																					&nbsp;
																				</font>
																				<%=VCONT_SF_XML_GEN_DT.elementAt(l) %>
																			<!-- </div> -->
																		</td>
																		<!-- <td align="center"><i class="fa fa-download fa-2x" aria-hidden="true"></i></td> -->
																	</tr>
																	<%if(k==index){
																		l=l+1;
																		break;} %>
																	<%} %>
																<%}else{ %>
																	<tr>
																		<td colspan=9 align="center"><%=utilmsg.infoMessage("<b>No Active Deals are Available for "+VSEGMENT_NM.elementAt(i)+"!</b>") %></td>
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
						</div>
					<%}%>
					<%}%>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" name="comp_abbr" value="<%=comp_abbr%>">

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
<input type="hidden" name="option" value="GENERATE_CONTACT_DTL_XML">


</form>
<script>
$(document).ready(function() {	
	$('.serchtbl').each(function(k){
		$('#tbsearch'+k).each(function(j){						
			$('#tbsearch'+k+' th').each(function(i){
				var title = $(this).text();
				if($(this).hasClass('tbser'+k))
				{
					//if(i==21){i=i+3;}
					if(title == "Check All")
					{
						$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+''+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:40px"/></div>');
					}
					else
					{
						$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+''+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:100px"/></div>');
					}
				}
			});		
		});
	});
});
	
function Search(obj, indx, tblid) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+tblid);
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
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
</body>
</html>