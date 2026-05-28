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
		url = "rpt_dlng_trucks_slot_wise_dtl.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt;
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
	
	var url = "xls_dlng_trucks_slot_wise_dtl.jsp?fileName=DLNG Delivery Report.xls&from_dt="+from_dt+"&to_dt="+to_dt;

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

cont_mgmt.setCallFlag("DLNG_TRUCKS_SLOT_WISE_DTL");
cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setFrom_dt(from_dt);
cont_mgmt.setTo_dt(to_dt);
//cont_mgmt.setGas_dt(gas_dt);
cont_mgmt.init();

Vector VCOUNTERPARTY_CD = cont_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = cont_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = cont_mgmt.getVCOUNTERPARTY_NM();
Vector VTRANSPORTER_CD = cont_mgmt.getVTRANSPORTER_CD();
Vector VTRANSPORTER_ABBR = cont_mgmt.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_NM = cont_mgmt.getVTRANSPORTER_NM();
Vector VCONT_BU_PLANT_SEQ = cont_mgmt.getVCONT_BU_PLANT_SEQ();
Vector VCONT_BU_PLANT_MAP = cont_mgmt.getVCONT_BU_PLANT_MAP();
Vector VCOUNTERPARTY_PLANT_SEQ = cont_mgmt.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_ABBR = cont_mgmt.getVCOUNTERPARTY_PLANT_ABBR();
Vector VTITTLE_DISP_CONT_NO = cont_mgmt.getVTITTLE_DISP_CONT_NO();

Vector VLEAGAL_ENTITY =  cont_mgmt.getVLEAGAL_ENTITY();
Vector VTRUCK_REG_NUM =  cont_mgmt.getVTRUCK_REG_NUM();
Vector VTRUCK_CD =  cont_mgmt.getVTRUCK_CD();
Vector VTRUCK_TRANS_CD =  cont_mgmt.getVTRUCK_TRANS_CD();
Vector VGAS_DT =  cont_mgmt.getVGAS_DT();
Vector VFILLING_ST_CD =  cont_mgmt.getVFILLING_ST_CD();
Vector VFILLING_ST_ABBR =  cont_mgmt.getVFILLING_ST_ABBR();
Vector VFILLING_ST_NM =  cont_mgmt.getVFILLING_ST_NM();
Vector VBAY_CD =  cont_mgmt.getVBAY_CD();
Vector VBAY_NM =  cont_mgmt.getVBAY_NM();
Vector VSLOT_DLT =  cont_mgmt.getVSLOT_DLT();
Vector VNEXT_AVAIL_HRS =  cont_mgmt.getVNEXT_AVAIL_HRS();
Vector VOCCUPIED_START_TIME =  cont_mgmt.getVOCCUPIED_START_TIME();
Vector VOCCUPIED_END_TIME =  cont_mgmt.getVOCCUPIED_END_TIME();
Vector VASSIGNED_DRIVER =  cont_mgmt.getVASSIGNED_DRIVER();
Vector VCURR_STATUS =  cont_mgmt.getVCURR_STATUS();

%>
<body onload="">
<%@ include file="../home/header.jsp"%>
<form>

<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Truck Usage Details
					    </div>
					    <!-- <div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div> -->
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-4 col-xs-4 col-md-4">  
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
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
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered table-hover" id="filterbysearch">
							<thead>
								<tr>
									<th rowspan="2">Gas Date<div align="center"><input class="form-control form-control-sm" type="text" id="gasDt" onkeyup="Search(this,'0');" placeholder="Search.." style="width:100px"/></div></th>
									<th rowspan="2">Truck#<div align="center"><input class="form-control form-control-sm" type="text" id="truckNum" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
									<th rowspan="2">Current Status<div align="center"><input class="form-control form-control-sm" type="text" id="currStatus" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th rowspan="2">Legal Entity</th>
									<th rowspan="2">Customer</th>
									<th rowspan="2">Contract#</th>
									<th rowspan="2">Plant - BU</th>
									<th rowspan="2">Transporter</th>
									<th colspan="3">Filling Station Association</th>
									<th rowspan="2">Next Available<br>(In Hrs)</th>
									<th rowspan="2">Available After</th>
									<th rowspan="2">Assigned Driver</th>
								</tr>
								<tr>
									<th>Filling Station</th>
									<th>Bay</th>
									<th>Slot</th>
								</tr>
							</thead>
							<tbody>
								<%if(VGAS_DT.size()>0){ %>
								<%for(int l=0;l<VGAS_DT.size();l++){ %>
								<tr>
									<td align="center">
										<%=VGAS_DT.elementAt(l)%>
									</td>
									<td align="center">
										<%=VTRUCK_REG_NUM.elementAt(l)%>
									</td>
									<td align="center">
										<span 
	    									<%if(VCURR_STATUS.elementAt(l).equals("C")){ %>
	    										class="alert alert-secondary"
	    									<%}else if(VCURR_STATUS.elementAt(l).equals("B")){ %>
	    										class="alert alert-warning"
	    									<%}else if(VCURR_STATUS.elementAt(l).equals("A")){ %>
	    										class="alert alert-info"
	    									<%}%>
	    									><b>
	    									<%if(VCURR_STATUS.elementAt(l).equals("A")){ %>
	    										Allocated
	    									<%}else if(VCURR_STATUS.elementAt(l).equals("B")){ %>
	    										Scheduled
	    									<%}else if(VCURR_STATUS.elementAt(l).equals("C")){ %>
	    										Nominated
	    									<%}%>
	    									</b>
	    								</span>
									</td>
									<td align="center">
										<%=VLEAGAL_ENTITY.elementAt(l)%>
									</td>
									<td align="center" title="<%=VCOUNTERPARTY_ABBR.elementAt(l)%>">
										<%=VCOUNTERPARTY_NM.elementAt(l)%>
									</td>
									<td align="center">
										<%=VTITTLE_DISP_CONT_NO.elementAt(l)%>
									</td>
									<td align="center">
										<%=VCONT_BU_PLANT_MAP.elementAt(l)%>
									</td>
									<td align="center" title="<%=VTRANSPORTER_NM.elementAt(l)%>">
										<%=VTRANSPORTER_ABBR.elementAt(l)%>
									</td>
									<td align="center" title="<%=VFILLING_ST_NM.elementAt(l)%>">
										<%=VFILLING_ST_ABBR.elementAt(l)%>
									</td>
									<td align="center">
										<%=VBAY_NM.elementAt(l)%>
									</td>
									<td align="center">
										<%=VSLOT_DLT.elementAt(l)%>
									</td>
									<td align="center">
										<%=VNEXT_AVAIL_HRS.elementAt(l)%>
									</td>
									<td align="center">
										<%=VOCCUPIED_END_TIME.elementAt(l)%>
									</td>
									<td align="center">
										<%=VASSIGNED_DRIVER.elementAt(l)%>
									</td>
								</tr>
								<%} %>
								<%}else{ %>
									<tr>
										<td colspan="14">
											<div align="center"><%=utilmsg.infoMessage("<b>No Data available for the Selected Gas Period!</b>") %></div>
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