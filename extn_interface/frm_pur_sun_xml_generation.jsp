<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var segment = document.forms[0].segment.value;
	var count = compareDate(from_dt,to_dt);
	var flag=true;
	if(parseInt(count) == 1)
	{
		msg+="To date ("+to_dt+") must be greater than from date ("+from_dt+")!\n";	
		flag=false;
	}
	
	var u = document.forms[0].u.value;
	
	if(flag==true)
	{
		var url = "../extn_interface/frm_pur_sun_xml_generation.jsp?u="+u+
				"&from_dt="+from_dt+"&to_dt="+to_dt+"&segment="+segment;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert("To date ("+to_dt+") must be greater than from date ("+from_dt+")!\n");
	}
}

function generateSunXML()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var account_code = document.forms[0].account_code;
	var flag=true;
	var msg="";
	if(account_code!=null || account_code!=undefined)
	{
		if(account_code.length!=undefined)
		{
			for(var i=0;i<account_code.length;i++)
			{
				if(account_code[i].value=='' || account_code[i].value==null)
				{
					msg="Configure Account Code!\n";
					flag=false;
					break;
				}
			}
		}
		else
		{
			if(account_code.value=='')
			{
				msg="Configure Account Code!\n";
				flag=false;
			}
		}
	}
	
	if(flag==true)
	{
		var a=confirm("Do you want to generate sun xml for period "+from_dt+" to "+to_dt);
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

</script>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sun_interface" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");

accounting.setCallFlag("REMITTANCE_SUN_XML_GENERATION");
accounting.setComp_cd(owner_cd);
accounting.setFrom_dt(from_dt);
accounting.setTo_dt(to_dt);
accounting.setSegment(segment);
accounting.init();

Vector VSEGMENT = accounting.getVSEGMENT();
Vector VSEGMENT_TYPE = accounting.getVSEGMENT_TYPE();
Vector VTEMP_SEGMENT = accounting.getVTEMP_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = accounting.getVTEMP_SEGMENT_TYPE();

Vector VINVOICE_NO=accounting.getVINVOICE_NO();
Vector VJOURNAL_TYPE=accounting.getVJOURNAL_TYPE() ;
Vector VAPPROVAL_DT=accounting.getVAPPROVAL_DT() ;
Vector VLEDGER=accounting.getVLEDGER() ;
Vector VACCOUNT_CD=accounting.getVACCOUNT_CD() ;
Vector VPERIOD_START_DT=accounting.getVPERIOD_START_DT();
Vector VPERIOD_END_DT=accounting.getVPERIOD_END_DT();
Vector VBASE_AMT=accounting.getVBASE_AMT() ;
Vector VDEBIT_CREDIT=accounting.getVDEBIT_CREDIT();
Vector VREPORT_AMT=accounting.getVREPORT_AMT() ;
Vector VCURRENCY_CD=accounting.getVCURRENCY_CD();
Vector VEXCHNG_RATE=accounting.getVEXCHNG_RATE();
Vector VINVOICE_DT=accounting.getVINVOICE_DT() ;
Vector VDESC=accounting.getVDESC() ;
Vector VINVOICE_DUE_DT= accounting.getVINVOICE_DUE_DT();
Vector VCOST_CTR_CD=accounting.getVCOST_CTR_CD() ;
Vector VCOA_CD= accounting.getVCOA_CD();
Vector VCODE= accounting.getVCODE();
Vector VBU_UNIT_CD= accounting.getVBU_UNIT_CD();
Vector VGOOD_SERVICE=accounting.getVGOOD_SERVICE();		
Vector VREV_CHARGE= accounting.getVREV_CHARGE();
Vector VHSN_CD=accounting.getVHSN_CD() ;
Vector VPOS_CD=accounting.getVPOS_CD() ;
Vector VTAX_LINE_AMT= accounting.getVTAX_LINE_AMT();
Vector VSUPPLY_TYPE=accounting.getVSUPPLY_TYPE() ;
Vector VTOTAL_INV_AMT= accounting.getVTOTAL_INV_AMT();
Vector VEMPLOYEE_CD = accounting.getVEMPLOYEE_CD();
Vector VTRANS_AMT = accounting.getVTRANS_AMT();
Vector VDIS_CONT_MAPPING = accounting.getVDIS_CONT_MAPPING();
Vector VPROJECT_CD = accounting.getVPROJECT_CD();
Vector VINDEX = accounting.getVINDEX();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_sun_interface">
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
					    	Purchase SUN XML Generation
					    </div>
					    <div class="btn-group">
							<select class="btn btn-outline-secondary btngrp btnactive" name="segment" onchange="refresh();">
								<!-- <option value="">All</option> -->
								<option value="">--Select--</option>
								<%for(int i=0;i<VSEGMENT.size();i++){ %>
								<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
								<%} %>
							</select>
						</div>
						<script>document.forms[0].segment.value="<%=segment%>"</script>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-10 col-xs-10 col-md-10">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>From</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
									<div class="col-auto">
										<label class="form-label"><b>To</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
									
								</div>
							</div>
						</div>
							<%if(execute_access.equals("Y") && !segment.equals("")){ %>
							<div class="col-sm-6 col-xs-6 col-md-6"></div>
							<div class="col-sm-6 col-xs-6 col-md-6">  
								<div class="d-flex justify-content-end">
									<div class="form-group row">
										<div class="col-auto">
											<!-- <input type="checkbox" class="form-check-input" name="chk"> -->
											<b>Generate SUN XML</b>		
										</div>								
										<div class="col-auto">	
										<%if(owner_cd.equals("1")){ %>
											<span class="fa-stack fa-lg">
											  <i class="fa fa-play-circle-o fa-stack-1x"></i>
											  <i class="fa fa-ban fa-stack-2x" style="color:grey;"></i>
											</span>
										<%}else{%>						
											<i class="fa fa-play-circle-o fa-2x" <%if(VINVOICE_NO.size() > 0){%>style="color: red;" onclick="generateSunXML();" title="Generate Sun XML"<%}else{%>style="pointer-events: none;color:grey;"<%} %> ></i>
										<%} %>
										</div>
									</div>	
								</div>	
							</div> 	
							<%} %>
					</div>
				</div>
				<div class="card-body cdbody">
				<%
					if(!segment.equals("")){
					int k=0;
					int i=0;
					int l=0;			
					for(l=0;l<VTEMP_SEGMENT_TYPE.size();l++){
					int index = Integer.parseInt(""+VINDEX.elementAt(l)); %>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading1">
										<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l %>" aria-expanded="false" aria-controls="collapse<%=l%>"><%=VTEMP_SEGMENT.elementAt(l) %>&nbsp;<font color="blue">(<%=index%> Items)</font></button>	
							    	</h2>
							    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading1">
							    		<div class="accordion-body accor-body">
											<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered table-hover" id="example<%=l%>">
														<thead id="tbsearch<%=l%>">
															<tr>
																<th>Cargo#</th>
																<th>Journal Type</th>
																<th>Approval Date</th>
																<th>Ledger</th>
																<th>Account Code</th>
																<th>Account Period</th>
																<th>Base Amount</th>
																<th>Debit/Credit</th>
																<th>Transaction Amount</th>
																<th>Report Amount</th>
																<th>Memo Amount</th>
																<th>Currency Code</th>
																<th>Currency Rate</th>
																<th>Transaction Date</th>
																<th>Journal Source</th>
																<th>Transaction Reference</th>
																<th>Description</th>
																<th>Due Date</th>
																<th>Cost Center Code</th>
																<th>Employee Code</th>
																<th>COA Codes <br>[Cash Analysis Code]</th>
																<th>TDS, Sales and Service Tax Codes Analysis Code</th>
																<th>Business Unit Code Analysis Code</th>
																<!-- <th>Project Code</th>
																<th>Asst Compo</th>
																<th>Goods/Service</th>
																<th>Reverser Charge (%)</th>
																<th>HSN/SAC</th>
																<th>Place Of Supply</th>
																<th>Tax Line Amt</th>
																<th>Supply Type (IGST,SGST,CGST)</th>
																<th>Total Invoice Amt</th>
																<th>Original Invoice No For CR/DR</th> -->
															</tr>
														</thead>
														<tbody>
														<% k=0;
															if(index > 0){ %>
															<%for(i=i; i<VINVOICE_NO.size(); i++){
																boolean isEditable=false;
																k+=1;%>
																<tr>
																	<td align="center"><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
																	<td align="center"><%=VJOURNAL_TYPE.elementAt(i) %></td>
																	<td align="center"><%=VAPPROVAL_DT.elementAt(i) %></td>
																	<td align="center"><%=VLEDGER.elementAt(i) %></td>
																	<td align="center">
																		<input type="hidden" name="account_code" value="<%=VACCOUNT_CD.elementAt(i) %>">
																		<%=VACCOUNT_CD.elementAt(i) %>
																	</td>
																	<td align="center"><%=VPERIOD_START_DT.elementAt(i)%></td>
																	<td align="right"><%=VBASE_AMT.elementAt(i) %></td>
																	<td align="center"><%=VDEBIT_CREDIT.elementAt(i) %></td>
																	<td align="right"><%=VTRANS_AMT.elementAt(i) %></td>
																	<td align="right"><%=VREPORT_AMT.elementAt(i) %></td>
																	<td></td>	<!-- MEMO AMT -->
																	<td align="center"><%=VCURRENCY_CD.elementAt(i) %></td>
																	<td align="right"><%=VEXCHNG_RATE.elementAt(i)%></td>
																	<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
																	<td></td>	<!-- Journal Source -->
																	<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
																	<td><%=VDESC.elementAt(i) %></td>
																	<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
																	<td align="center"><%=VCOST_CTR_CD.elementAt(i) %></td>
																	<td align="center"><%=VEMPLOYEE_CD.elementAt(i) %></td>
																	<td align="center"><%=VCOA_CD.elementAt(i) %></td>
																	<td align="center"><%=VCODE.elementAt(i)%></td>
																	<td align="center"><%=VBU_UNIT_CD.elementAt(i) %></td>
																	<%-- <td></td>
																	<td></td>
																	<td align="center"><%=VGOOD_SERVICE.elementAt(i) %></td>
																	<td align="right"><%=VREV_CHARGE.elementAt(i) %></td>
																	<td align="center"><%=VHSN_CD.elementAt(i) %></td>
																	<td align="center"><%=VPOS_CD.elementAt(i) %></td>
																	<td align="right"><%=VTAX_LINE_AMT.elementAt(i) %></td>
																	<td align="center"><%=VSUPPLY_TYPE.elementAt(i) %></td>
																	<td align="right"><%=VTOTAL_INV_AMT.elementAt(i) %></td>
																	<td></td> --%>
																</tr>
																<%if(k==index)
																{i=i+1;
																	break;
																} %>
															<%}%>
														<%}else{ %>
															<tr>
																<td colspan="23" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Approved for Selected Period!</b>") %></td>
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
				<%} %>
				<%}else{%>
					<div colspan="<%=23%>" align="center">
							<%=utilmsg.infoMessage("<b>Please Select any Remittance!</b>") %>
					</div>
				<%}%>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="GENERATE_PUR_SUN_XML">
<input type="hidden" name="acc_size" value="<%=VINDEX.size()%>">

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

$(document).ready(function() {
	var acc_size=document.forms[0].acc_size.value; 
	for(k=0;k<acc_size;k++)
	{
		$('#tbsearch'+k+' th').each(function(i){
			//alert(i)
			var title = $(this).text();
			if(title == "Sr#")
			{
				//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
			}
			else
			{
				$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'_'+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:100px"/></div>');
			}
		});
	}
	
});

function Search(obj, indx,k) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+k);
  	
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
</html>
