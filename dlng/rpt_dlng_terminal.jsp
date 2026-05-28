<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--   Created By Harsh Maheta on 20250721 : Terminal Report for DLNG-->
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{	
	//var from_dt=document.forms[0].from_dt.value;
	//var to_dt=document.forms[0].to_dt.value;
	var fill_station=document.forms[0].fill_station.value;
	var gas_dt=document.forms[0].gas_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url="rpt_dlng_terminal.jsp?gas_dt="+gas_dt+
			//"&to_dt="+to_dt+
			"&fill_station="+fill_station+
		"&u="+u;
	
	location.replace(url);
}

function hide_show(id1,id2)
{	
	if(document.getElementById(id1).style.display=='none'){
		document.getElementById(id1).style.display='table-row-group';
		document.getElementById(id2).className='fa fa-compress';
	}else{
		document.getElementById(id1).style.display='none';
		document.getElementById(id2).className='fa fa-expand';
	}
	
	if(document.forms[0].prev_display.value != "" && document.forms[0].prev_display1.value !="")
	{
		if(document.forms[0].prev_display.value != id1 && document.forms[0].prev_display1.value != id2)
		{
			document.getElementById(document.forms[0].prev_display.value).style.display='none';
			document.getElementById(document.forms[0].prev_display1.value).className='fa fa-expand';
		}
	}
	document.forms[0].prev_display.value=id1;
	document.forms[0].prev_display1.value=id2;
} 

function exportToXls()
{
	//var from_dt = document.forms[0].from_dt.value;
	//var to_dt = document.forms[0].to_dt.value;
	var fill_station = document.forms[0].fill_station.value;
	var gas_dt = document.forms[0].gas_dt.value;
	
	var url = "xls_dlng_terminal.jsp?fileName=DLNG Terminal Report.xls&gas_dt="+gas_dt+
			//"&to_dt="+to_dt+
			"&fill_station="+fill_station;

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
</script>

<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="page"/>
<%
String nextdate=utildate.getNextDate();
//String firstDate="01/"+sysdate.substring(3, sysdate.length());

String fill_station=request.getParameter("fill_station")==null?"0":request.getParameter("fill_station");
String gas_dt=request.getParameter("gas_dt")==null?nextdate:request.getParameter("gas_dt");
//String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
//String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

dlng.setCallFlag("DLNG_TERMINAL_REPORT");
dlng.setComp_cd(owner_cd);
dlng.setFill_station(fill_station);
dlng.setGas_dt(gas_dt);
//dlng.setFrom_dt(from_dt);
//dlng.setTo_dt(to_dt);
dlng.init();

Vector VMST_FILLST_CD = dlng.getVMST_FILLST_CD();
Vector VMST_FILLST_NM = dlng.getVMST_FILLST_NM();
Vector VMST_FILLST_ABBR = dlng.getVMST_FILLST_ABBR();

Vector VGAS_DT = dlng.getVGAS_DT();
Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_NM = dlng.getVCOUNTERPARTY_PLANT_NM();
Vector VCOUNTERPARTY_PLANT_ABBR = dlng.getVCOUNTERPARTY_PLANT_ABBR();
Vector VNOM_REV_NO = dlng.getVNOM_REV_NO();
Vector VQTY_MMBTU = dlng.getVQTY_MMBTU();
Vector VQTY_MT = dlng.getVQTY_MT();
Vector VTRUCK_TRANS_CD = dlng.getVTRUCK_TRANS_CD();
Vector VTRUCK_CD = dlng.getVTRUCK_CD();
Vector VSLOT_START_TIME = dlng.getVSLOT_START_TIME();
Vector VARRIVAL_DT = dlng.getVARRIVAL_DT();
Vector VREMARK = dlng.getVREMARK();
Vector VDRIVER_NM = dlng.getVDRIVER_NM();
Vector VCHECKPOST_NM = dlng.getVCHECKPOST_NM();
Vector VTRANSPORTER_ABBR = dlng.getVTRANSPORTER_ABBR();
Vector VTRANSPORTER_NM = dlng.getVTRANSPORTER_NM();
Vector VTRUCK_REG_NUM = dlng.getVTRUCK_REG_NUM();
Vector VINDEX = dlng.getVINDEX();
Vector VBU_PLANT_SEQ = dlng.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = dlng.getVBU_PLANT_ABBR();
Vector VFILLING_ST_CD = dlng.getVFILLING_ST_CD();
Vector VFILLING_ST_ABBR = dlng.getVFILLING_ST_ABBR();
Vector VSLOT_DLT = dlng.getVSLOT_DLT();
Vector VBAY_CD = dlng.getVBAY_CD();
Vector VBAY_NM = dlng.getVBAY_NM();

Vector VDTL_NOM_REV_NO = dlng.getVDTL_NOM_REV_NO();
Vector VDTL_QTY_MMBTU = dlng.getVDTL_QTY_MMBTU();
Vector VDTL_QTY_MT = dlng.getVDTL_QTY_MT();
Vector VDTL_TRUCK_TRANS_CD = dlng.getVDTL_TRUCK_TRANS_CD();
Vector VDTL_TRUCK_CD = dlng.getVDTL_TRUCK_CD();
Vector VDTL_SLOT_START_TIME = dlng.getVDTL_SLOT_START_TIME();
Vector VDTL_ARRIVAL_DT = dlng.getVDTL_ARRIVAL_DT();
Vector VDTL_REMARK = dlng.getVDTL_REMARK();
Vector VDTL_DRIVER_NM = dlng.getVDTL_DRIVER_NM();
Vector VDTL_CHECKPOST_NM = dlng.getVDTL_CHECKPOST_NM();
Vector VDTL_TRANSPORTER_ABBR =dlng.getVDTL_TRANSPORTER_ABBR();
Vector VDTL_TRANSPORTER_NM =dlng.getVDTL_TRANSPORTER_NM();
Vector VDTL_TRUCK_REG_NUM =dlng.getVDTL_TRUCK_REG_NUM();
Vector VDTL_FILLING_ST_ABBR =dlng.getVDTL_FILLING_ST_ABBR();
Vector VDTL_BAY_NM =dlng.getVDTL_BAY_NM();
Vector VDTL_SLOT_DLT =dlng.getVDTL_SLOT_DLT();

%>

<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="">
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
				<div class="card-header cdheader topheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
						    Terminal Report
						</div>
						<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Filling Station</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="fill_station" onchange="refresh()">
										<option value="0">--All--</option>
										<%for(int i=0;i<VMST_FILLST_CD.size();i++){ %>
										<option value="<%=VMST_FILLST_CD.elementAt(i)%>"><%=VMST_FILLST_ABBR.elementAt(i)%> - <%=VMST_FILLST_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].fill_station.value="<%=fill_station%>"</script>
								</div>
							</div>
						</div>
						<%-- <div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div> --%>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Gas Day</b></label>
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
					<div class="table-responsive">
						<table class="table table-bordered" id="example">
							<thead id="tbsearch">
								<tr>
									<th rowspan="2">Sr No</th>
									<th rowspan="2">Gas Date
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_gas_date" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th rowspan="2">Customer Name
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_cp" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th rowspan="2">Business Unit
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_bu" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th rowspan="2">Customer Plant
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_cplant" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
									<th rowspan="2" title="Seller Nomination">Rev No
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_rev" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th colspan="2">Nomination Qty
									</th>
									<th rowspan="2">Transporter Name
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_trans" onkeyup="Search(this,'8');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th rowspan="2">Truck Reg No.
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_truckno" onkeyup="Search(this,'9');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th rowspan="2">Driver Name
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_driver" onkeyup="Search(this,'10');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th rowspan="2">Filling Station Association
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_filst" onkeyup="Search(this,'11');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th colspan="2">Scheduled Slot</th>
									<th rowspan="2">Check post
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_check_post" onkeyup="Search(this,'14');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th rowspan="2">Remarks</th>
								</tr>
								<tr>
									<th>MMBTU
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_mmbtu" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th>MT
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_mt" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th>Date<br>(DD/MM/YYYY)
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_slot_date" onkeyup="Search(this,'12');" placeholder="Search.." style="width:100px"/></div>
									</th>
									<th>Time<br>(HH:MM)
										<br><div align="center"><input class="form-control form-control-sm" type="text" id="filt_slot_time" onkeyup="Search(this,'13');" placeholder="Search.." style="width:100px"/></div>
									</th>
								</tr>
							</thead>
							<tbody id="mainTbody">
							<%int j=0;int k=0;
								if(VGAS_DT.size()>0)
								{
									for (int i=0;i<VGAS_DT.size();i++)
									{
											int size = Integer.parseInt(""+VINDEX.elementAt(i));
										%>
										<tr class = "">
											<td align="center"><%=i+1%> </td>
											<%-- <td <%if(size>0){ %>onclick="hide_show('tbody<%=i %>','hidCont<%=i%>');<%}%>"><%=i+1%> 
											&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
												<%if(size==0){ %>
												<%}else{%>
													<i style="align: right; display:null;" id="hidCont<%=i%>" class="fa fa-expand" title="Click here to show Details"></i>
												<%}%>
											</td> --%>
											<td align="center"><%=VGAS_DT.elementAt(i)%></td>
											<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
											<td align="center"><%=VBU_PLANT_ABBR.elementAt(i) %></td>
											<td align="center"><%=VCOUNTERPARTY_PLANT_ABBR.elementAt(i) %></td>
											<td align="center"><%=VNOM_REV_NO.elementAt(i)%></td>
											<td align="right"><%=VQTY_MMBTU.elementAt(i)%></td>
											<td align="right"><%=VQTY_MT.elementAt(i)%></td>
											<td align="center" title="<%=VTRANSPORTER_ABBR.elementAt(i)%>"><%=VTRANSPORTER_NM.elementAt(i)%></td>
											<td align="center"><%=VTRUCK_REG_NUM.elementAt(i)%></td>
											<td align="center"><%=VDRIVER_NM.elementAt(i)%></td>
											<td align="center">
												<%=VFILLING_ST_ABBR.elementAt(i)%> [<%=VBAY_NM.elementAt(i)%>]<br><%=VSLOT_DLT.elementAt(i)%>
											</td>
											<td align="center"><%=VARRIVAL_DT.elementAt(i)%></td>
											<td align="center"><%=VSLOT_START_TIME.elementAt(i)%></td>
											<td align="left"><%=VCHECKPOST_NM.elementAt(i)%></td>
											<td align="center"><%=VREMARK.elementAt(i)%></td>
										</tr>
										<%if(size>0)
										{k=0;%>
											<tbody id="tbody<%=i%>" style="display:none;">
												<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
													<td colspan="5" rowspan="<%=size+1%>" style="background:white;"></td>
												</tr>
												<%for(j=j; j<= VDTL_NOM_REV_NO.size(); j++)
												{
													k+=1;
												%>
												<tr>
													<td style="text-align:center"><%=VDTL_NOM_REV_NO.elementAt(j) %></td>
													<td align="right"><%=VDTL_QTY_MMBTU.elementAt(j)%></td>
													<td align="right"><%=VDTL_QTY_MT.elementAt(j)%></td>
													<td  align="center" title="<%=VDTL_TRANSPORTER_NM.elementAt(j)%>"><%=VDTL_TRANSPORTER_ABBR.elementAt(j)%></td>
													<td align="center"><%=VDTL_TRUCK_REG_NUM.elementAt(j)%></td>
													<td align="center"><%=VDTL_DRIVER_NM.elementAt(j)%></td>
													<td align="center"><%=VDTL_FILLING_ST_ABBR.elementAt(j)%> [<%=VDTL_BAY_NM.elementAt(j)%>]<br><%=VDTL_SLOT_DLT.elementAt(j)%></td>
													<td align="center"><%=VDTL_ARRIVAL_DT.elementAt(j)%></td>
													<td align="center"><%=VDTL_SLOT_START_TIME.elementAt(j)%></td>
													<td align="left"><%=VDTL_CHECKPOST_NM.elementAt(j)%></td>
													<td align="center"><%=VDTL_REMARK.elementAt(j)%></td>
												</tr>	
												<%if(k==size)
													{
														j=j+1;
														break;
													}
												}%>
											</tbody>
										<%} %>
									<%} %>
								<%}else{ %>
								<tr>
									<td colspan="16" align="center"><%=utilmsg.infoMessage("<b>No Data Available!</b>")%></td>
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

<input type="hidden" name="option" value="">
<input type="hidden" name="mod_type" value="">
<input type="hidden" name="emp_cd" value="">
<input type="hidden" name="user_nm" value="">
<input type="hidden" name="change_status" value="">
<input type="hidden" name="prev_display" value="">
<input type="hidden" name="prev_display1" value="">
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

<form method="post" action="../servlet/Frm_reset_password">

<input type="hidden" name="rpt_nm" value="USER_DTL">
<input type="hidden" name="option" value="RESET_PASSWORD">
<input type="hidden" name="comp_cd" value="">
<input type="hidden" name="inputUserID_nm" value="">
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
</head>

<script>
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
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