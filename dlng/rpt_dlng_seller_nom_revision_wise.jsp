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
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_dlng_seller_nom_revision_wise.jsp?gas_dt="+gas_dt+"&u="+u+"&counterparty_cd="+counterparty_cd;

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

function exportToXls()
{
	var gas_dt = document.forms[0].gas_dt.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var url = "xls_dlng_seller_nom_revision_wise.jsp?gas_dt="+gas_dt+"&counterparty_cd="+counterparty_cd;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

String gas_dt = request.getParameter("gas_dt")==null?nextdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "-1");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

dlng.setCallFlag("DLNG_SELLER_NOM_REV_WISE");
dlng.setComp_cd(owner_cd);
dlng.setGas_dt(gas_dt);
dlng.setCounterparty_cd(counterparty_cd);
dlng.init();

Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VTRANSPORTER_CD = dlng.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = dlng.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_TRUCK = dlng.getVTRANSPORTER_TRUCK();
Vector VTRANSPORTER_TRUCK_NO = dlng.getVTRANSPORTER_TRUCK_NO();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = dlng.getVCOUNTERPARTY_PLANT_ABBR();
Vector VBU_CD = dlng.getVBU_CD();
Vector VBU_PLANT_SEQ = dlng.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = dlng.getVBU_PLANT_ABBR();

Vector VNOM_REV_NO = dlng.getVNOM_REV_NO();
Vector VGEN_TIME = dlng.getVGEN_TIME();
Vector VGEN_DT = dlng.getVGEN_DT();
Vector VBASE = dlng.getVBASE();
Vector VGCV = dlng.getVGCV();
Vector VNCV = dlng.getVNCV();
Vector VQTY_MMBTU = dlng.getVQTY_MMBTU();
Vector VQTY_SCM = dlng.getVQTY_SCM();
//Vector VNOM_COLOR = dlng.getVNOM_COLOR();
Vector VDCQ = dlng.getVDCQ();
Vector VMDCQ_QTY = dlng.getVMDCQ_QTY();
Vector VCONT_NAME = dlng.getVCONT_NAME();
Vector VCONT_REF = dlng.getVCONT_REF();

Vector VCONT_NO = dlng.getVCONT_NO();
Vector VCONT_REV_NO = dlng.getVCONT_REV_NO();
Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VAGMT_REV_NO = dlng.getVAGMT_REV_NO();
Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();

Vector VINDEX = dlng.getVINDEX();
Vector VSUB_INDEX = dlng.getVSUB_INDEX();
Vector VBUYER_NOM_REV_NO = dlng.getVBUYER_NOM_REV_NO();
Vector VBUYER_NOM = dlng.getVBUYER_NOM();
Vector VCONTRACT_TYPE_NM = dlng.getVCONTRACT_TYPE_NM();
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
					    	DLNG Daily Seller Nomination (Revision Wise)
					    </div>
					    <div>
					  		<div class="btn-group" onclick="exportToXls();">
								<label><i class="fa fa-file-excel-o fa-2x excel_icon"></i></label>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--All--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_ABBR.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
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
				<div class="card-body cdbody">
				<%if(VCOUNTERPARTY_CD.size()>0)
				{ %>	
					<%int j=0,k=0,l=0,m=0;
					for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
					{ 
						
						String cp_cd=""+VCOUNTERPARTY_CD.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%k=0;
						if(index > 0){ %>
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading<%=l%>">
   												<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
							    		<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
							      		</button>
							    	</h2>
							    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
							      		<div class="accordion-body accor-body">
							        		<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered table-hover">
														<thead>
															<tr>
																<th rowspan="2">Gen Day</th>
																<th rowspan="2">Gen Time</th>
																<th rowspan="2">Rev#</th>
																<th rowspan="2">Transporter</th>
																<th rowspan="2">Truck#</th>
																<th rowspan="2">Customer Plant</th>
																<th rowspan="2">Business Unit</th>
																<th rowspan="2">Contract Type</th>
																<th rowspan="2">Contract#<br>[Contract/Trade Ref#]</th>
																<th rowspan="2">DCQ</th>
																<th rowspan="2">Buyer Nomination (Rev)<br>(MMBTU)</th>
																<th rowspan="2">Energy (MMBTU)</th>
																<th rowspan="2">Energy (MT)</th>
																<th colspan="2">Calorific Value Base<br>KCal/SCM</th>																
															</tr>
															<tr>
																<th>GCV</th>
																<th>NCV</th>
															</tr>
														</thead>
														<tbody>
															<%m=0;
															if(index>0){ %>
																<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
																{ 
																	m+=1;
																%>
																	<tr>
																		<td align="center"><%=VGEN_DT.elementAt(l) %></td>
																		<td align="center"><%=VGEN_TIME.elementAt(l) %></td>
																		<td align="center"><b><%=VNOM_REV_NO.elementAt(l) %></b></td>
																		<td align="center"><%=VTRANSPORTER_ABBR.elementAt(l)%></td>
																		<td align="center"><%=VTRANSPORTER_TRUCK_NO.elementAt(l)%></td>
																		<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(l)%></td>
																		<td align="center"><%=VBU_PLANT_ABBR.elementAt(l)%></td>
																		<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(l)%></td>
																		<td>
																			<%=VDIS_CONT_MAPPING.elementAt(l)%>
																			<%if(!VCONT_REF.elementAt(l).equals("")){%>
																				<br>(<%=VCONT_REF.elementAt(l)%>)
																			<%} %>
																		</td>
																		<td align="right"><%=VDCQ.elementAt(l) %></td>
																		<td align="right"><%=VBUYER_NOM.elementAt(l)%>&nbsp;<%if(!VBUYER_NOM_REV_NO.elementAt(l).equals("")){ %>(<%=VBUYER_NOM_REV_NO.elementAt(l)%>)<%} %></td>																			
																		<td align="right"><%=VQTY_MMBTU.elementAt(l) %></td>
																		<td align="right"><%=VQTY_SCM.elementAt(l) %></td>
																		<td align="right"><%=VGCV.elementAt(l)%></td>
																		<td align="right"><%=VNCV.elementAt(l)%></td>
																	</tr>
																	
																	<%if(m==index)
																	{%>
																		<%l=l+1;
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
					<%} %>
				<%}else{ %>
					<div align="center">
					<%=utilmsg.infoMessage("<b>No Seller Nomination Done for Selected Gas Day!</b>") %>
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