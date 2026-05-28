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
	//var transporter_cd = document.forms[0].transporter_cd.value; 
	//var transporter_truck = document.forms[0].transporter_truck.value;
	var bu_seq = document.forms[0].bu_seq.value;	// SagarB20250922 Added this variable for showing BU list
	var chk_diff = document.forms[0].chk_diff;
	
	if(chk_diff.checked)
	{
		chk_diff="Y";
	}
	else
	{
		chk_diff="N";
	}
	
	var u = document.forms[0].u.value;
	
	if(from_dt!=null && to_dt!=null)
   	{
	   	if(trim(from_dt)!="" && trim(to_dt)!="")
	   	{
	   		var tmp = from_dt.split("/")
	   		var tmp1 = to_dt.split("/")
	   	 	var date1 = new Date(tmp[1]+"/"+tmp[0]+"/"+tmp[2]);
         	var date2 = new Date(tmp1[1]+"/"+tmp1[0]+"/"+tmp1[2]);
         	
         	var time_difference = date2.getTime() - date1.getTime();
         	var days_difference = time_difference / (1000 * 60 * 60 * 24);
         	
         	if(parseInt(days_difference)+1 <= 180)
         	{	
				var value = compareDate(from_dt,to_dt);
				if(value!=1)
				{
					var url = "rpt_dlng_nom_alloc.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd+
							"&chk_diff="+chk_diff+"&bu_seq="+bu_seq+"&u="+u;	// SagarB20250922 Added bu_seq variable for showing BU list

					document.getElementById("loading").style.visibility = "visible";
					location.replace(url);
				}
				else
				{
					alert("Please ensure From Date <= To Date !");
				    // return false;	SagarB20250929
				}
         	}
         	else
         	{
         		alert("Date range can not exceed more than 90 days!!");
			    // return false;	SagarB20250929
         	}
	   	}
	   	else
	   	{ 
	    	alert("Please Select From and To Dates...");
		    // return false;	SagarB20250929
	   	}
	}
	else
	{
		alert("Please Select From and To Dates...");
	    // return false;	SagarB20250929
	}
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
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value; 
	//var transporter_cd = document.forms[0].transporter_cd.value; 
	//var transporter_truck = document.forms[0].transporter_truck.value;
	var chk_diff = document.forms[0].chk_diff;
	var bu_seq = document.forms[0].bu_seq.value;	// SagarB20250922 Added this variable for showing BU
	var bu_abbr=document.forms[0].bu_seq[document.forms[0].bu_seq.selectedIndex].text;	// SagarB20250922 Added this variable for showing BU
	
	if(chk_diff.checked)
	{
		chk_diff="Y";
	}
	else
	{
		chk_diff="N";
	}
	
	var url = "xls_dlng_nom_alloc.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd+"&chk_diff="+chk_diff+"&bu_seq="+bu_seq+"&bu_abbr="+bu_abbr;		// SagarB20250922 Added bu_seq variable for showing BU list

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String nextdate = utildate.getNextDate();

// Get value after Page Refresh()
String from_dt = request.getParameter("from_dt")==null?nextdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?nextdate:request.getParameter("to_dt");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String transporter_cd=request.getParameter("transporter_cd")==null?"0":request.getParameter("transporter_cd");
String transporter_truck=request.getParameter("transporter_truck")==null?"0":request.getParameter("transporter_truck");
String chk_diff = request.getParameter("chk_diff")==null?"":request.getParameter("chk_diff");
String bu_seq=request.getParameter("bu_seq")==null?"0":request.getParameter("bu_seq");	// SagarB20250922 Added this variable for showing BU list

//Send Value to Java
dlng.setCallFlag("DLNG_NOM_ALLOC");
dlng.setComp_cd(owner_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setTransporter_cd(transporter_cd);
dlng.setTransporter_truck(transporter_truck);
dlng.setChk_diff(chk_diff);
dlng.setBu_seq(bu_seq);		// SagarB20250922 Added this variable for showing BU list
dlng.init();

// Get Vectors from Java
Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();

Vector VMST_TRANSPORTER_CD = dlng.getVMST_TRANSPORTER_CD();
Vector VMST_TRANSPORTER_NM = dlng.getVMST_TRANSPORTER_NM();
Vector VMST_TRANSPORTER_ABBR = dlng.getVMST_TRANSPORTER_ABBR();
Vector VMST_TRANSPORTER_TRUCK = dlng.getVMST_TRANSPORTER_TRUCK();
Vector VMST_TRANSPORTER_TRUCK_NO = dlng.getVMST_TRANSPORTER_TRUCK_NO();


Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VTRANSPORTER_CD = dlng.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = dlng.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_PLANT_SEQ = dlng.getVTRANSPORTER_TRUCK();
Vector VTRANSPORTER_PLANT_ABBR = dlng.getVTRANSPORTER_TRUCK_NO();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = dlng.getVCOUNTERPARTY_PLANT_ABBR();
Vector VBU_CD = dlng.getVBU_CD();
Vector VBU_PLANT_SEQ = dlng.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = dlng.getVBU_PLANT_ABBR();

// SagarB20250926 Adding below Vector to show BU Plant Abbr List
Vector VBu_Plant_Abbr_List = dlng.getVBu_Plant_Abbr_List();

Vector VNOM_REV_NO = dlng.getVNOM_REV_NO();
Vector VGEN_TIME = dlng.getVGEN_TIME();
Vector VGEN_DT = dlng.getVGEN_DT();
Vector VBASE = dlng.getVBASE();
Vector VGCV = dlng.getVGCV();
Vector VNCV = dlng.getVNCV();
Vector VQTY_MMBTU = dlng.getVQTY_MMBTU();
Vector VQTY_MT = dlng.getVQTY_MT();
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
Vector VGAS_DT = dlng.getVGAS_DT();

Vector VQTY_SELLER_MMBTU = dlng.getVQTY_SELLER_MMBTU();
Vector VQTY_SELLER_MT = dlng.getVQTY_SELLER_MT();
Vector VQTY_ALLOC_MMBTU = dlng.getVQTY_ALLOC_MMBTU();
Vector VQTY_ALLOC_MT = dlng.getVQTY_ALLOC_MT();
Vector VCOLOR = dlng.getVCOLOR();
Vector VCOLOR_ALLOC = dlng.getVCOLOR_ALLOC();
Vector VAGMT_BASE = dlng.getVAGMT_BASE();
Vector VCONTRACT_TYPE_NM = dlng.getVCONTRACT_TYPE_NM();

String total_buyer_mmbtu = dlng.getTotal_buyer_mmbtu();
String total_buyer_mt = dlng.getTotal_buyer_mt();
String total_seller_mmbtu = dlng.getTotal_seller_mmbtu();
String total_seller_mt = dlng.getTotal_seller_mt();
String total_alloc_mmbtu = dlng.getTotal_alloc_mmbtu();
String total_alloc_mt = dlng.getTotal_alloc_mt();
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
					    	DLNG Nomination vs Truck Loading
					    </div>
					    <div class="col-sm-9 col-xs-9 col-md-9">
					  		<div class="d-flex justify-content-end">
					  			<div class="form-group row">
									<div class="col-auto">
										<div class="btn-group" onclick="exportToXls();">
											<label><i class="fa fa-file-excel-o fa-2x excel_icon"></i></label>
										</div>
									</div>
								</div>
							</div>
					  	</div>		 
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-3 col-xs-3 col-md-3"> 	<!-- SagarB20250926 Changed col-sm-4 col-xs-4 col-md-4 to col-sm-3 col-xs-3 col-md-3  -->
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off"> <!-- SagarB20250926 Removed refresh() function -->
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off"> <!-- SagarB20250926 Removed refresh() function -->
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3"> 	<!-- SagarB20250926 Changed col-sm-4 col-xs-4 col-md-4 to col-sm-3 col-xs-3 col-md-3  -->
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" > <!-- SagarB20250926 Removed onchange function -->
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>	
						</div>	
							
						<!-- SagarB20250922 Added below block for showing BU list -->
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
						<div class="col-sm-2 col-xs-2 col-md-2">  	<!-- SagarB20250926 Changed col-sm-4 col-xs-4 col-md-4 to col-sm-2 col-xs-2 col-md-2  -->
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label">
										<b>
											<input type="checkbox" class="form-check-input" name="chk_diff" <%if(chk_diff.equals("Y")){%>checked<%}%>> <!-- SagarB20250926 Removed onchange function -->
											&nbsp;Nomination Difference Only
										</b>
									</label>
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
				<div class="card-body cdbody">
				<%if(VCOUNTERPARTY_PLANT_SEQ.size()>0)
				{ %>	
	      		<div class="accordion-body accor-body">
	        		<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="filterbysearch">
								<thead>
									<tr>
										<th rowspan="2">Gas Day<div align="center"><input class="form-control form-control-sm" type="text" id="gasDate" onkeyup="Search(this,'0');" placeholder="Search.." style="width:100px"/></div></th>																	
										<th rowspan="2">Customer</th>
										<th rowspan="2">Customer Plant</th>
										<th rowspan="2">BU</th>
										<th rowspan="2">Contract Type<div align="center"><input class="form-control form-control-sm" type="text" id="contType" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">Contract#<br>[Contract/Trade Ref#]<div align="center"><input class="form-control form-control-sm" type="text" id="contnum" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
										<th rowspan="2">DCQ</th>
										<th colspan="2">Buyer Nomination</th>	
										<th colspan="2">Seller Nomination</th>
										<th colspan="2">Truck Loading</th>																
									</tr>
									<tr>
										<th>MMBTU</th>
										<th>MT</th>
										<th>MMBTU</th>
										<th>MT</th>
										<th>MMBTU</th>
										<th>MT</th>
									</tr>
								</thead>
								<tbody>
									<%for(int l=0; l<VCOUNTERPARTY_PLANT_SEQ.size(); l++)
									{ %>
										<tr>
											<td align="center"><%=VGAS_DT.elementAt(l) %></td>
											<td align="center"><%=VCOUNTERPARTY_NM.elementAt(l)%></td>
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
											<td align="right"><%=VQTY_MMBTU.elementAt(l) %></td>
											<td align="right"><%=VQTY_MT.elementAt(l) %></td>
											<td align="right" style="color:<%=VCOLOR.elementAt(l)%>">
												<%=VQTY_SELLER_MMBTU.elementAt(l) %>
											</td>
											<td align="right"><%=VQTY_SELLER_MT.elementAt(l) %></td>
											<td align="right" style="color:<%=VCOLOR_ALLOC.elementAt(l)%>"><%=VQTY_ALLOC_MMBTU.elementAt(l) %></td>
											<td align="right"><%=VQTY_ALLOC_MT.elementAt(l) %></td>
										</tr>
									<%} %>
									<tr>
										<td colspan="7"><div align="right"><b>Total:</b></div></td>
										<td align="right"><b><%=total_buyer_mmbtu %></b></td>
										<td align="right"><b><%=total_buyer_mt %></b></td>
										<td align="right"><b><%=total_seller_mmbtu %></b></td>
										<td align="right"><b><%=total_seller_mt %></b></td>
										<td align="right"><b><%=total_alloc_mmbtu %></b></td>
										<td align="right"><b><%=total_alloc_mt %></b></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
	      		</div>
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
<script>
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
</body>
</html>