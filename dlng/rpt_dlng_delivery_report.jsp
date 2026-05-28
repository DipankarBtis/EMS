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
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var bu_seq = document.forms[0].bu_seq.value;	// SagarB20250922 Added this variable for showing BU list
	
	var msg="";
	var flag=true;
	
	var count = compareDate(from_dt,to_dt);
	
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	
	var u = document.forms[0].u.value;
	var url="";
	
	if(flag)
	{
		url = "rpt_dlng_delivery_report.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd+"&bu_seq="+bu_seq;	// SagarB20250922 Added this variable for showing BU list
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var bu_seq = document.forms[0].bu_seq.value;	// SagarB20250922 Added this variable for showing BU
	var bu_abbr=document.forms[0].bu_seq[document.forms[0].bu_seq.selectedIndex].text;	// SagarB20250922 Added this variable for showing BU
	
	var url = "xls_dlng_delivery_report.jsp?fileName=DLNG Delivery Report.xls&from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd+"&bu_seq="+bu_seq+"&bu_abbr="+bu_abbr;		// SagarB20250922 Added bu_seq variable for showing BU list

	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String prevdate = utildate.getPreviousDate();
String sysdate=utildate.getSysdate();

String gas_dt = request.getParameter("gas_dt")==null?prevdate:request.getParameter("gas_dt");
String gen_dt = utildate.getDate(gas_dt, "1");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String bu_seq=request.getParameter("bu_seq")==null?"0":request.getParameter("bu_seq");	// SagarB20250922 Added this variable for showing BU list

cont_mgmt.setCallFlag("DELIVERY_REPORT");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setFrom_dt(from_dt);
cont_mgmt.setTo_dt(to_dt);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setBu_seq(bu_seq);		// SagarB20250922 Added this variable for showing BU list
cont_mgmt.init();

Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VCONT_BU_PLANT_SEQ = cont_mgmt.getVCONT_BU_PLANT_SEQ();
Vector VCONT_BU_PLANT_MAP = cont_mgmt.getVCONT_BU_PLANT_MAP();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();
Vector VTITTLE_DISP_CONT_NO = cont_mgmt.getVTITTLE_DISP_CONT_NO();

Vector VQTY_MMBTU = cont_mgmt.getVQTY_MMBTU();
Vector VQTY_MT = cont_mgmt.getVQTY_MT();
Vector VCONT_REF = cont_mgmt.getVCONT_REF();
Vector VBUYER_NOM = cont_mgmt.getVBUYER_NOM();
Vector VSELLER_NOM = cont_mgmt.getVSELLER_NOM();

Vector VINDEX = cont_mgmt.getVINDEX();
Vector VSUB_INDEX = cont_mgmt.getVSUB_INDEX();

Vector VLOAD_START_DT = cont_mgmt.getVLOAD_START_DT();
Vector VLOAD_START_TIME = cont_mgmt.getVLOAD_START_TIME();
Vector VLOAD_END_DT = cont_mgmt.getVLOAD_END_DT();
Vector VLOAD_END_TIME = cont_mgmt.getVLOAD_END_TIME();

Vector VTRUCK_REG_NUM =  cont_mgmt.getVTRUCK_REG_NUM();
Vector VTRUCK_CD =  cont_mgmt.getVTRUCK_CD();
Vector VTRUCK_TRANS_CD =  cont_mgmt.getVTRUCK_TRANS_CD();
Vector VGAS_DT =  cont_mgmt.getVGAS_DT();

Vector VTOTAL_SELLER_MMBTU =  cont_mgmt.getVTOTAL_SELLER_MMBTU();
Vector VTOTAL_BUYER_MMBTU =  cont_mgmt.getVTOTAL_BUYER_MMBTU();
Vector VTOTAL_ALLOC_MMBTU =  cont_mgmt.getVTOTAL_ALLOC_MMBTU();
Vector VTOTAL_ALLOC_MT =  cont_mgmt.getVTOTAL_ALLOC_MT();
Vector VCONT_NAME =  cont_mgmt.getVCONT_NAME();
Vector VBU_PLANT_ABBR =  cont_mgmt.getVBU_PLANT_ABBR();

// SagarB20250922 Added below vector variable for showing BU list
Vector VBU_PLANT_SEQ = cont_mgmt.getVBU_PLANT_SEQ();
Vector VBu_Plant_Abbr_List = cont_mgmt.getVBu_Plant_Abbr_List();

%>
<body onload="">
<%@ include file="../home/header.jsp"%>
<form>

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
					    	Delivery Report
					    </div>
					    <div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
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
									<select class="form-select form-select-sm" name="counterparty_cd">	 <!-- SagarB20250926 Removed refresh() function -->
										<option value="0">--All--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_ABBR.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">	 <!-- SagarB20250926 Removed refresh() function -->
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">	 <!-- SagarB20250926 Removed refresh() function -->
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
							
						<!-- SagarB20250926 Added below block for showing BU list -->
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Business Unit</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="bu_seq" > 
										<option value="0">--Select--</option>
										<%for(int i=0;i<VBU_PLANT_SEQ.size();i++){ %>
									<option value="<%=VBU_PLANT_SEQ.elementAt(i)%>"><%=VBu_Plant_Abbr_List.elementAt(i)%></option>
									<%} %>
								</select>
								<script>document.forms[0].bu_seq.value="<%=bu_seq%>"</script>
								</div>
							</div>
						</div>	
						<!-- SagarB20250925 Added below button for page refresh -->
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
					  			</div>
					  		</div>
					  	</div>
					</div>
				</div>
				<%if(VTITTLE_DISP_CONT_NO.size() > 0){ %>
				<div class="card-body cdbody">
					<%int j=0,k=0,l=0,m=0,p=0,q=0;
					for(int i=0; i<VTITTLE_DISP_CONT_NO.size(); i++)
					{
						String disp_cont=""+VTITTLE_DISP_CONT_NO.elementAt(i);
						int index=Integer.parseInt(""+VINDEX.elementAt(i));
					%>
					<%if(i>0){ %>&nbsp;<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTITTLE_DISP_CONT_NO.elementAt(i)%></label>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%k=0;
						if(index > 0){ %>
							<%for(j=j;j<VCONT_BU_PLANT_SEQ.size(); j++) 
							{
								String trans_plant_seq=""+VCONT_BU_PLANT_SEQ.elementAt(j);
								int sub_index = Integer.parseInt(""+VSUB_INDEX.elementAt(j));
								k+=1;
							%>
								<input type="hidden" name="sub_index" value="<%=sub_index%>">
								<input type="hidden" name="trans_abbr" value="<%=VCONT_BU_PLANT_MAP.elementAt(j)%>">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=l%>">
    										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=l%>" aria-expanded="false" aria-controls="collapse<%=l%>">
								    			<font><%=VCONT_BU_PLANT_MAP.elementAt(j).toString().replace("@", "  <i class='fa fa-truck fa-lg fa-flip-horizontal' aria-hidden='true' style='pointer-events: none;color:#008080'></i>  ")%>&nbsp;&nbsp;
								    			</font>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=l%>" class="accordion-collapse collapse" aria-labelledby="heading<%=l%>">
								      		<div class="accordion-body accor-body">
								        		<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover" id="filterbysearch_<%=l%>">
															<thead>
																<tr>
																	<th rowspan="2">Gas Date<div align="center"><input class="form-control form-control-sm" type="text" id="gasDt_<%=l %>" onkeyup="Search(this,'0','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
																	<th rowspan="2">Contract#<div align="center"><input class="form-control form-control-sm" type="text" id="cont_<%=l %>" onkeyup="Search(this,'1','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
																	<th rowspan="2">Business Unit<div align="center"><input class="form-control form-control-sm" type="text" id="bu_seq_<%=l %>" onkeyup="Search(this,'2','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
																	<th rowspan="2">Truck#<div align="center"><input class="form-control form-control-sm" type="text" id="truckNum_<%=l %>" onkeyup="Search(this,'3','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
																	<th colspan="2">Nomination Qty<br>(MMBTU)</th>
																	<th colspan="2">Loaded Qty</th>
																	<th colspan="2">Arrival</th>
																</tr>
																<tr>
																	<th>Buyer Nom</th>
																	<th>Seller Nom</th>
																	<th>MMBTU</th>
																	<th>MT</th>
																	<th>Loading Start</th>
																	<th>Loading End</th>
																</tr>
															</thead>
															<tbody>
																<%m=0;
																if(sub_index>0)
																{ %>
																	<%for(l=l; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
																	{
																		m+=1;
																	%>
																		<tr>
																			<td align="center">
																				<%=VGAS_DT.elementAt(l)%>
																			</td>
																			<td align="center">
																				<%=VCONT_NAME.elementAt(l)%>
																			</td>
																			<td align="center">
																				<%=VBU_PLANT_ABBR.elementAt(l)%>
																			</td>
																			<td align="center">
																				<%=VTRUCK_REG_NUM.elementAt(l)%>
																			</td>
																			<td align="right"
																			<%if(!VSELLER_NOM.elementAt(l).equals("") && VBUYER_NOM.elementAt(l).equals("-")) {%>
																			style="color:blue"
																			title="Buyer nomination done without Truck!"
																			<%} %>
																			>
																				<%if(!VSELLER_NOM.elementAt(l).equals("") && VBUYER_NOM.elementAt(l).equals("-")) {%>
																					<%=VSELLER_NOM.elementAt(l)%>
																				<%}else{ %>
																					<%=VBUYER_NOM.elementAt(l)%>
																				<%} %>
																			</td>
																			<td align="right">
																				<%=VSELLER_NOM.elementAt(l)%>
																			</td>
																			<td align="right">
																				<%=VQTY_MMBTU.elementAt(l)%>
																			</td>
																			<td align="right">
																				<%=VQTY_MT.elementAt(l)%>
																			</td>
																			<td align="center">
																				<div>
																					<div class="row m-b-5">
																						<div class="col">
																							<%=VLOAD_START_DT.elementAt(l)%>
																		      				<%=VLOAD_START_TIME.elementAt(l)%>
															      						</div>
															      					</div>
															      				</div>
																			</td>
																			<td align="center">
																				<div>
																					<div class="row m-b-5">
																						<div class="col">
																							<%=VLOAD_END_DT.elementAt(l)%>
																		      				<%=VLOAD_END_TIME.elementAt(l)%>
															      						</div>
															      					</div>
															      				</div>
																			</td>
																		</tr>
																		
																		<%if(m==sub_index)
																		{%>
																			<%l=l+1;
																			break;
																		}%>
																	<%} %>
																	<tr>
																		<td colspan="4"><div align="right"><b>Total:</b></div></td>
																		<td align="right"><b><%=VTOTAL_BUYER_MMBTU.elementAt(j) %></b></td>
																		<td align="right"><b><%=VTOTAL_SELLER_MMBTU.elementAt(j) %></b></td>
																		<td align="right"><b><%=VTOTAL_ALLOC_MMBTU.elementAt(j) %></b></td>
																		<td align="right"><b><%=VTOTAL_ALLOC_MT.elementAt(j) %></b></td>
																		<td colspan="2"></td>
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
								<%if(k==index)
								{
									j=j+1;
									break;
								}%>
							<%} %>
						<%} %>
						</div>
					</div>
					<%} %>
				</div>
				<%}else{ %>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="center"><%=utilmsg.infoMessage("<b>No Delivery Data available for the Duration!!!</b>") %></div>
					</div>
				</div>
				<%} %>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="comp_cd" value="<%=owner_cd%>">
<input  type="hidden" name="mmbtu_to_tons" value="0.025219021687207">
<input type="hidden" name="mmbtu_to_m3" value="23.9">
<input type="hidden" name="m3_to_tonMMbtu" value="0.3531466672">
<input type="hidden" name="convt_mmbtu_to_mt" value="51.5">

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
<script>
function Search(obj, indx, l) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch_"+l);
  	
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
</body>
</html>