<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function refresh()
{
	var from_dt=document.forms[0].from_dt.value;
	var to_dt=document.forms[0].to_dt.value;
	var transptr_cd=document.forms[0].transptr_cd.value;
	var temp_from_dt = document.forms[0].temp_from_dt.value;
    var temp_to_dt = document.forms[0].temp_to_dt.value;
	
	var u = document.forms[0].u.value;
    var flag = true;
    var msg = "";

    if (from_dt != null && to_dt != null) 
    {
        if (trim(from_dt) != "" && trim(to_dt) != "") 
        {

            var partsFrom = from_dt.split("/");
            var partsTo = to_dt.split("/");

            var fromDate = new Date(partsFrom[2], partsFrom[1] - 1, partsFrom[0]);
            var toDate = new Date(partsTo[2], partsTo[1] - 1, partsTo[0]);

            if (toDate < fromDate) 
            {
                alert("To Date cannot be earlier than From Date.");
                flag =  false;
            }

            var diffMs = toDate - fromDate;
            var diffDays = diffMs / (1000 * 60 * 60 * 24);

            if (diffDays > 180) 
            {
                alert("Date range should not exceed 180 days.");

                flag =  false;
            }
            var u = document.forms[0].u.value;

            if (flag) 
            {
				var url="frm_transporter_ct_mst.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&transptr_cd="+transptr_cd+"&u="+u;

                document.getElementById("loading").style.visibility = "visible";
                location.replace(url);
            }
            else
            {
            	 document.forms[0].from_dt.value = temp_from_dt;
                 document.forms[0].to_dt.value = temp_to_dt;
            }
        } 
        else 
        {
            alert("Please Select From and To Dates...");
            document.forms[0].from_dt.value = temp_from_dt;
            document.forms[0].to_dt.value = temp_to_dt;
        }
    }
}

function doRefresh()
{
	var from_dt=document.forms[0].from_dt.value;
	var to_dt=document.forms[0].to_dt.value;
	var transptr_cd=document.forms[0].transptr_cd.value;
	
	var u = document.forms[0].u.value;
	
	var url="frm_transporter_ct_mst.jsp?from_dt="+from_dt+"&to_dt="+to_dt+"&transptr_cd="+transptr_cd+"&u="+u;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

var newWindow;
function openSalesContList(bu_unit,transporter_cd,transporter_plant_seq,contract_type,counterparty_cd,counterparty_plant_seq,start_dt,end_dt,seq_no)
{
	var u = document.forms[0].u.value;
	
	var url="rpt_ct_sales_cont_list.jsp?counterparty_cd="+counterparty_cd+"&transporter_cd="+transporter_cd+
			"&transporter_plant_seq="+transporter_plant_seq+"&counterparty_plant_seq="+counterparty_plant_seq+
			"&start_dt="+start_dt+"&end_dt="+end_dt+"&contract_type="+contract_type+"&seq_no="+seq_no+"&bu_unit="+bu_unit+
			"&u="+u;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Sales Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Sales Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setActiveInactive(obj)
{
	var max_nom_dt = document.forms[0].max_nom_dt.value;
	var gtc_max_dt = document.forms[0].gtc_max_dt.value;
	
	if(obj.checked)
	{
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		if(trim(max_nom_dt) != "")
		{
			alert("Nomination Entry Exist for the CTR, Inactivation Blocked!");
			
			obj.checked=true;
			document.getElementById("lb").innerHTML="Active";
			document.getElementById("status_flag").value="Y";
		}
		else if(trim(gtc_max_dt) != "")
		{
			alert("GTC Linked for the CTR, Inactivation Blocked!");
			
			obj.checked=true;
			document.getElementById("lb").innerHTML="Active";
			document.getElementById("status_flag").value="Y";
		}
		else
		{
			document.getElementById("lb").innerHTML="In-Active";
			document.getElementById("status_flag").value="N";
		}
	}
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

function doClear()
{
	document.forms[0].bu_unit.value="";
	document.forms[0].transporter_cd.value="0";
	document.forms[0].transporter_plant_seq.value="0";
	document.forms[0].counterparty_cd.value="0";
	document.forms[0].counterparty_plant_seq.value="0";
	document.forms[0].contract_type.value="0";
	document.forms[0].ct_ref.value="";
	document.forms[0].seq_no.value="";
	document.forms[0].utr.value="";
	//document.forms[0].start_dt.value="";
	//document.forms[0].end_dt.value="";
	document.forms[0].min_nom_dt.value="";
	document.forms[0].max_nom_dt.value="";
	document.forms[0].gtc_min_dt.value="";
	document.forms[0].gtc_max_dt.value="";
	
	$("input[name='start_dt']").datepicker("update", "");
	$("input[name='end_dt']").datepicker("update", "");
	
	document.forms[0].ct_ref.readOnly=false;
	
	document.forms[0].bu_unit.style.pointerEvents = "auto";
	document.forms[0].transporter_cd.style.pointerEvents = "auto";
	document.forms[0].transporter_plant_seq.style.pointerEvents = "auto";
	document.forms[0].contract_type.style.pointerEvents = "auto";
	document.forms[0].counterparty_cd.style.pointerEvents = "auto";
	document.forms[0].counterparty_plant_seq.style.pointerEvents = "auto";
	
	document.forms[0].active.checked=true;
	document.getElementById("lb").innerHTML="Active";
	document.getElementById("status_flag").value="Y";
			
	document.forms[0].opration.value="INSERT";
}

function doSubmit()
{
	var opration = document.forms[0].opration.value;
	
	var bu_unit = document.forms[0].bu_unit.value;
	var transporter_cd = document.forms[0].transporter_cd.value;
	var transporter_plant_seq = document.forms[0].transporter_plant_seq.value;
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var counterparty_plant_seq = document.forms[0].counterparty_plant_seq.value;
	var contract_type = document.forms[0].contract_type.value;
	var ct_ref = document.forms[0].ct_ref.value;
	var utr = document.forms[0].utr.value;
	var start_dt = document.forms[0].start_dt.value;
	var end_dt = document.forms[0].end_dt.value;
	
	var min_nom_dt = document.forms[0].min_nom_dt.value;
	var max_nom_dt = document.forms[0].max_nom_dt.value;
	
	var gtc_min_dt=document.forms[0].gtc_min_dt.value;
	var gtc_max_dt=document.forms[0].gtc_max_dt.value;
	
	var seq_no = document.forms[0].seq_no.value;
	
	var msg="";
	var flag=true;
	
	if(opration=="MODIFY")
	{
		if(trim(seq_no) == "")
		{
			msg+="Sequance No is missing!\n";
			flag=false;
		}
	}
	if(trim(bu_unit) == "")
	{
		msg+="Select Business Unit!\n";
		flag=false;
	}
	if(trim(transporter_cd) == "" || transporter_cd=="0")
	{
		msg+="Select Transporter!\n";
		flag=false;
	}
	
	if(trim(transporter_plant_seq) == "" || transporter_plant_seq=="0")
	{
		msg+="Select Transporter Plant!\n";
		flag=false;
	}
	
	if(trim(counterparty_cd) == "" || counterparty_cd=="0")
	{
		msg+="Select Customer!\n";
		flag=false;
	}
	
	if(trim(counterparty_plant_seq) == "" || counterparty_plant_seq=="0")
	{
		msg+="Select Customer Plant!\n";
		flag=false;
	}
	
	if(trim(ct_ref) == "")
	{
		msg+="Transporter CT Reference is missing!\n";
		flag=false;
	}
	
	if(trim(utr) == "")
	{
		msg+="Transporter UTR No is missing!\n";
		flag=false;
	}
	
	/*if(opration=="MODIFY")
	{
		if(trim(ct_ref) == "")
		{
			msg+="Customer Code No is missing!\n";
			flag=false;
		}
	}*/
	
	if(trim(start_dt) == "")
	{
		msg+="Select Start Date!\n";
		flag=false;
	} 
	
	if(trim(end_dt) == "")
	{
		msg+="Select End Date!\n";
		flag=false;
	} 
	
	if(opration=="MODIFY")
	{
		if(min_nom_dt != "" && max_nom_dt != "")
		{
			var val_dt = compareDate(start_dt,min_nom_dt);
			var val_dt_1 = compareDate(end_dt,max_nom_dt);
			
			if(val_dt == "1")
			{
				msg+="Start Date("+start_dt+") cann't be > Min Nomination date("+min_nom_dt+")!\n";
				flag=false;
			}
			
			if(val_dt_1 == "2")
			{
				msg+="End Date("+end_dt+") cann't be < Max Nomination date("+max_nom_dt+")!\n";
				flag=false;
			}
		}
		
		if(gtc_min_dt != "" && gtc_max_dt != "")
		{
			var val_dt = compareDate(start_dt,gtc_min_dt);
			var val_dt_1 = compareDate(end_dt,gtc_max_dt);
			
			if(val_dt != "0" || val_dt_1 != "0")
			{
				msg+="CT Period("+start_dt+"-"+end_dt+") should match Linked GTC Period("+gtc_min_dt+"-"+gtc_max_dt+")!\n";
				flag=false;
			}
		}
	}
	
	if(flag)
	{
		var a;
		if(opration=="MODIFY")
		{
			a= confirm("Do you Want to Modify?");
		}
		else
		{
			a= confirm("Do you Want to Submit?");
		}
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

function doModify(bu_unit,transporter_cd,transporter_plant_seq,contract_type,counterparty_cd,counterparty_plant_seq,ct_ref,utr,start_dt,end_dt,status,seq_no,min_nom_dt,max_nom_dt,gtc_min_dt,gtc_max_dt)
{
	document.forms[0].bu_unit.value=bu_unit;
	document.forms[0].transporter_cd.value=transporter_cd;
	document.forms[0].contract_type.value=contract_type;
	document.forms[0].counterparty_plant_seq.value=counterparty_plant_seq;
	document.forms[0].ct_ref.value = ct_ref;
	document.forms[0].seq_no.value = seq_no;
	document.forms[0].utr.value=utr;
	//document.forms[0].start_dt.value=start_dt;
	//document.forms[0].end_dt.value=end_dt;
	document.forms[0].min_nom_dt.value=min_nom_dt;
	document.forms[0].max_nom_dt.value=max_nom_dt;
	document.forms[0].gtc_min_dt.value=gtc_min_dt;
	document.forms[0].gtc_max_dt.value=gtc_max_dt;
	
	$("input[name='start_dt']").datepicker("update", start_dt);
	$("input[name='end_dt']").datepicker("update", end_dt);
	
	if(min_nom_dt=="" && max_nom_dt=="")
	{
		document.forms[0].ct_ref.readOnly=false;
	}
	else if(gtc_min_dt=="" && gtc_max_dt=="")
	{
		document.forms[0].ct_ref.readOnly=false;
	}
	else
	{
		document.forms[0].ct_ref.readOnly=true;
	}
	
	document.forms[0].bu_unit.style.pointerEvents = "none";
	document.forms[0].transporter_cd.style.pointerEvents = "none";
	document.forms[0].transporter_plant_seq.style.pointerEvents = "none";
	document.forms[0].contract_type.style.pointerEvents = "none";
	document.forms[0].counterparty_cd.style.pointerEvents = "none";
	document.forms[0].counterparty_plant_seq.style.pointerEvents = "none";
	
	// For the JSON part
	fetchTransporterPlantDtl(document.forms[0].transporter_cd)
	getExitTypeId(document.forms[0].contract_type);
	fetchCounterpartyDtl(document.forms[0].contract_type);
			
	var c_cd_obj=document.forms[0].counterparty_cd;	
	fetchPlantDtl(c_cd_obj)
	window.setTimeout(function() 
	{
		document.forms[0].transporter_plant_seq.value = transporter_plant_seq;
		document.forms[0].counterparty_cd.value=counterparty_cd;
		
		fetchPlantDtl(document.forms[0].counterparty_cd);
		window.setTimeout(function() 
		{
			document.forms[0].counterparty_plant_seq.value=counterparty_plant_seq;
		}, 500);
	}, 500);
	
	
	
	if(status=="Y")
	{
		document.forms[0].active.checked=true;
		document.getElementById("lb").innerHTML="Active";
		document.getElementById("status_flag").value="Y";
	}
	else
	{
		document.forms[0].active.checked=false;
		document.getElementById("lb").innerHTML="In-Active";
		document.getElementById("status_flag").value="N";
	}
	
	document.forms[0].opration.value="MODIFY";
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.gta.DataBean_MapMaster" id="dbmapmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String transporter_cd=request.getParameter("transporter_cd")==null?"0":request.getParameter("transporter_cd");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
//String counterparty_plant_seq=request.getParameter("counterparty_plant_seq")==null?"0":request.getParameter("counterparty_plant_seq");
String sysdate=utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String transptr_cd=request.getParameter("transptr_cd")==null?"0":request.getParameter("transptr_cd");

dbmapmaster.setCallFlag("MAP_TRANSPORTER_CT");
dbmapmaster.setComp_cd(owner_cd);

dbmapmaster.setTransporter_cd(transporter_cd);
dbmapmaster.setCounterparty_cd(counterparty_cd);
dbmapmaster.setFrom_dt(from_dt);
dbmapmaster.setTo_dt(to_dt);
dbmapmaster.setTransptr_Cd(transptr_cd);
dbmapmaster.init();

Vector VTRANSPORTER_CD = dbmapmaster.getVTRANSPORTER_CD();
Vector VTRANSPORTER_NM = dbmapmaster.getVTRANSPORTER_NM();
Vector VTRANSPORTER_ABBR = dbmapmaster.getVTRANSPORTER_ABBR();
Vector VCOUNTERPARTY_CD = dbmapmaster.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbmapmaster.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbmapmaster.getVCOUNTERPARTY_ABBR();
Vector VCUSTOMER_PLANT_SEQ = dbmapmaster.getVCUSTOMER_PLANT_SEQ();
Vector VCUSTOMER_PLANT_SEQ_NM = dbmapmaster.getVCUSTOMER_PLANT_SEQ_NM();

Vector VTRANSPTR_CD = dbmapmaster.getVTRANSPTR_CD();
Vector VTRANSPTR_NM = dbmapmaster.getVTRANSPTR_NM();
Vector VTRANSPTR_ABBR = dbmapmaster.getVTRANSPTR_ABBR();
Vector VTRANS_PLANT_SEQ = dbmapmaster.getVTRANS_PLANT_SEQ();
Vector VTRANS_PLANT_SEQ_NM = dbmapmaster.getVTRANS_PLANT_SEQ_NM();
Vector VCOUNTERPTY_CD = dbmapmaster.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_NM = dbmapmaster.getVCOUNTERPTY_NM();
Vector VCOUNTERPTY_ABBR = dbmapmaster.getVCOUNTERPTY_ABBR();
Vector VPLANT_SEQ = dbmapmaster.getVPLANT_SEQ();
Vector VPLANT_SEQ_NM = dbmapmaster.getVPLANT_SEQ_NM();
Vector VSEQ_NO = dbmapmaster.getVSEQ_NO();
Vector VBU_UNIT = dbmapmaster.getVBU_UNIT();
Vector VBU_UNIT_ABBR = dbmapmaster.getVBU_UNIT_ABBR();
Vector VDEAL_ATTACHMENT = dbmapmaster.getVDEAL_ATTACHMENT();
Vector VGTC_DEAL_ATTACHMENT = dbmapmaster.getVGTC_DEAL_ATTACHMENT();

Vector VSTATUS = dbmapmaster.getVSTATUS();
Vector VINDEX = dbmapmaster.getVINDEX();
Vector VEXITPOINT_TYPE = dbmapmaster.getVEXITPOINT_TYPE();
Vector VCT_REFERENCE = dbmapmaster.getVCT_REFERENCE();
Vector VUTR = dbmapmaster.getVUTR();
Vector VSTART_DT = dbmapmaster.getVSTART_DT();
Vector VEND_DT = dbmapmaster.getVEND_DT();

Vector VSUB_COUNTERPTY_NM = dbmapmaster.getVSUB_COUNTERPTY_NM();
Vector VSUB_PLANT_SEQ_NM = dbmapmaster.getVSUB_PLANT_SEQ_NM();

Vector VSUB_STATUS = dbmapmaster.getVSUB_STATUS();
Vector VSUB_INDEX = dbmapmaster.getVSUB_INDEX();

Vector VMIN_NOM_DT = dbmapmaster.getVMIN_NOM_DT();
Vector VMAX_NOM_DT = dbmapmaster.getVMAX_NOM_DT();
Vector VGTC_MIN_DT = dbmapmaster.getVGTC_MIN_DT();
Vector VGTC_MAX_DT = dbmapmaster.getVGTC_MAX_DT();

Vector VBU_CD = dbmapmaster.getVBU_CD();
Vector VBU_PLANT_NM = dbmapmaster.getVBU_PLANT_NM();
Vector VBU_PLANT_ABBR = dbmapmaster.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ_NO = dbmapmaster.getVBU_PLANT_SEQ_NO();

Vector VMST_TRANSPTR_CD = dbmapmaster.getVMST_TRANSPTR_CD();
Vector VMST_TRANSPTR_ABBR = dbmapmaster.getVMST_TRANSPTR_ABBR();
Vector VMST_TRANSPTR_NM = dbmapmaster.getVMST_TRANSPTR_NM();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_MapMaster">
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
				    		Transporter CT|UTR Master
	   	 				</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Transporter</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="transptr_cd">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_TRANSPTR_CD.size();i++){ %>
										<option value="<%=VMST_TRANSPTR_CD.elementAt(i)%>"><%=VMST_TRANSPTR_ABBR.elementAt(i)%> - <%=VMST_TRANSPTR_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].transptr_cd.value="<%=transptr_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt %>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt %>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
								</div>
							</div>
						</div>
					</div>
				</div>			
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
									<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#MapCTAndUTRModal" 
								onclick="doClear();">Add CT and UTR</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead id="tbsearch">
										<tr>
											<th></th>
											<th>Business Unit</th>
											<th>Transporter Entry Point</th>
											<th>Transporter Exit Point</th>				    		
									    	<th>Exit Point Type</th>
									    	<th title="Capacity Tranche Reference">CTR</th>				    		
											<th title="Unique Transaction Reference">UTR</th>
											<th>CT Period</th>
									    	<th>Status</th>
									    	<th>Link Contract</th>
									    	<th>Linked GTC</th>
									    </tr>
									</thead>
									<tbody>
										<%if(VCOUNTERPTY_CD.size()>0){ %>
											<%for(int j=0;j<VCOUNTERPTY_CD.size();j++){ 
												String transCd=""+VTRANSPTR_CD.elementAt(j);
											%>
												<tr>
													<td align="center" >
														<font title="Click to Edit" style="color:var(--header_color)">
															<i class="fa fa-edit fa-lg" 
															data-bs-toggle="modal" data-bs-target="#MapCTAndUTRModal" 
															onclick="doClear();doModify('<%=VBU_UNIT.elementAt(j)%>','<%=transCd%>','<%=VTRANS_PLANT_SEQ.elementAt(j)%>',
															'<%=VEXITPOINT_TYPE.elementAt(j)%>','<%=VCOUNTERPTY_CD.elementAt(j)%>',
															'<%=VPLANT_SEQ.elementAt(j)%>','<%=VCT_REFERENCE.elementAt(j)%>',
															'<%=VUTR.elementAt(j)%>','<%=VSTART_DT.elementAt(j)%>',
															'<%=VEND_DT.elementAt(j)%>','<%=VSTATUS.elementAt(j)%>','<%=VSEQ_NO.elementAt(j)%>',
															'<%=VMIN_NOM_DT.elementAt(j)%>','<%=VMAX_NOM_DT.elementAt(j)%>','<%=VGTC_MIN_DT.elementAt(j)%>','<%=VGTC_MAX_DT.elementAt(j)%>');"></i>
														</font>
													</td>
													<td align="center"><%=VBU_UNIT_ABBR.elementAt(j)%></td>
													<td><%=VTRANSPTR_ABBR.elementAt(j)%>&nbsp;-&nbsp;<%=VTRANS_PLANT_SEQ_NM.elementAt(j)%></td>
													<td><%=VCOUNTERPTY_ABBR.elementAt(j)%>&nbsp;-&nbsp;<%=VPLANT_SEQ_NM.elementAt(j)%></td>
													<td align="center">
														<%if (VEXITPOINT_TYPE.elementAt(j).equals("C")) {%>
															Customer
														<%} else if (VEXITPOINT_TYPE.elementAt(j).equals("R")) {%>
															Transporter
														<%}%>
													</td>
													<td><%=VCT_REFERENCE.elementAt(j)%></td>
													<td><%=VUTR.elementAt(j)%></td>
													<td align="center"><%=VSTART_DT.elementAt(j)%> - <%=VEND_DT.elementAt(j)%></td>
													<td align="center">
														<div align="center">
															<font style="color:<%if(VSTATUS.elementAt(j).equals("Y")){%>#a6ff4d<%}else{%>red<%}%>">
																<i class="fa fa-circle fa-lg" ></i>
																&nbsp;
															</font>
															<%if(VSTATUS.elementAt(j).equals("Y")){%>
															Active
															<%}else{ %>
															In-Active
															<%} %>
														</div>
													</td>
													<td>
													<%if (VEXITPOINT_TYPE.elementAt(j).equals("C")) {%>
													<%if(VSTATUS.elementAt(j).equals("Y")){%>
													<i class="fa fa-paperclip fa-2x" 
													onclick="openSalesContList('<%=VBU_UNIT.elementAt(j) %>','<%=transCd%>','<%=VTRANS_PLANT_SEQ.elementAt(j)%>',
															'<%=VEXITPOINT_TYPE.elementAt(j)%>','<%=VCOUNTERPTY_CD.elementAt(j)%>',
															'<%=VPLANT_SEQ.elementAt(j)%>','<%=VSTART_DT.elementAt(j)%>',
															'<%=VEND_DT.elementAt(j)%>','<%=VSEQ_NO.elementAt(j)%>');"></i>
													&nbsp;<%}%><%=VDEAL_ATTACHMENT.elementAt(j) %>		
													<%} %>
													</td>
													<td><%=VGTC_DEAL_ATTACHMENT.elementAt(j)%></td>
												</tr>
											<%} %>
										<%}else{ %>
											<tr>
												<td colspan="11" align="center"><%=utilmsg.infoMessage("<b>No Transporter Customer Code Added for given date!</b>") %></td>
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
									
<div class="modal fade" id="MapCTAndUTRModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-lg">
   		<div class="modal-content">
			<div class="modal-header cdheader">
	    		<div class="topheader">
					Add/Modify CTR and UTR
				</div>
	    		<input type="button" class="btn-close" data-bs-dismiss="modal">
	  		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Business Unit</label>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Business Unit<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="bu_unit">
										<option value="">--Select--</option>
										<%for(int i=0;i<VBU_PLANT_SEQ_NO.size();i++){ %>
										<option value="<%=VBU_PLANT_SEQ_NO.elementAt(i)%>"><%=VBU_PLANT_ABBR.elementAt(i)%></option>
										<%} %>
									</select>
								</div>
							</div>
						</div>
					</div>
					<br>
      				<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Entry Point</label>
					</div>					
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Transporter<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="transporter_cd" onchange="fetchTransporterPlantDtl(this);">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VTRANSPORTER_CD.size();i++){ %>
										<option value="<%=VTRANSPORTER_CD.elementAt(i)%>"><%=VTRANSPORTER_ABBR.elementAt(i)%> - <%=VTRANSPORTER_NM.elementAt(i)%></option>
										<%} %>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Transporter Plant<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="transporter_plant_seq" ;>
										<option value="0">--Select--</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<br>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Exit Point</label>
					</div>	
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Exit Point Type<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4"> 
							<div class="form-group row">
								<div class="col">
				    				<select class="form-select form-select-sm" name="contract_type" onchange="getExitTypeId(this);fetchCounterpartyDtl(this);">				    					
				    					<option value="0">--Select--</option>
				    					<option value="C">Customer</option>
				    					<option value="R">Transporter</option>
				    				</select>
				      			</div>				      			
							</div>
						</div>
					</div>	
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label" id="id_exit_type"><b>Customer<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty_cd" onchange="fetchPlantDtl(this);">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
								</div>
							</div>
						</div>					
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label" id="id_exit_type_plant"><b>Customer Plant<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<select class="form-select form-select-sm" name="counterparty_plant_seq">
										<option value="0">--Select--</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<br>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> CT and UTR Config</label>
					</div>	
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>CT Reference<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm" name="ct_ref" value="" readOnly>
				      					<input type="hidden" class="form-control form-control-sm" name="seq_no" value="">
				    				</div>
				    			</div>
							</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>UTR<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
				      					<input type="text" class="form-control form-control-sm" name="utr" value="">
				    				</div>
				    			</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Start Date<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="start_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(this,document.forms[0].end_dt,'F');" 
			      						onchange="validateDate(this);checkStartEndDate(this,document.forms[0].end_dt,'F');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
		      						<input type="hidden" name="min_nom_dt" value="">
		      						<input type="hidden" name="max_nom_dt" value="">
		      						<input type="hidden" name="gtc_min_dt" value="">
		      						<input type="hidden" name="gtc_max_dt" value="">
				    			</div>
				    		</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>End Date<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="end_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');" 
			      						onchange="validateDate(this);checkStartEndDate(document.forms[0].start_dt,this,'T');" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				    		</div>
						</div>
					</div>
					<div class="row m-b-5">	
						<div class="col-sm-2 col-xs-2 col-md-2">
							<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="form-check form-switch">
										<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" checked onclick="setActiveInactive(this);">
									  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb">
									  		Active
									  	</label>
									  	<input type="hidden" name="status_flag" id="status_flag" value="Y">
									</div>
				    			</div>
							</div>
						</div>
					</div>
				</div>
			</div>	
      		<div class="modal-footer cdfooter">
        		<div class="d-flex justify-content-between">
					<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
					<%if(write_access.equals("Y")){ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
					<%}else{ %>
					<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
					<%} %>
				</div>
      		</div>
      	</div>
	</div>
</div>


<script>


function getExitTypeId(obj)
{
	var id = document.getElementById("id_exit_type");
	var id1 = document.getElementById("id_exit_type_plant");
	
	if (obj.value == "C")
	{
		id.innerHTML="<b>Customer<span class='s-red'>*</span></b>";
		id1.innerHTML="<b>Customer Plant<span class='s-red'>*</span></b>";
	}
	else if (obj.value == "R")
	{
		id.innerHTML="<b>Transporter<span class='s-red'>*</span></b>";
		id1.innerHTML="<b>Transporter Plant<span class='s-red'>*</span></b>";
	}	
}


function fetchCounterpartyDtl(obj)
{
	document.getElementById("loading").style.visibility = "visible";
	$.post("../servlet/DB_MapMaster_Ajax?setCallType=COUNTERPARTY_DTL&contract_type="+obj.value, function(responseJson) {
		//console.log(responseJson);
		var option = "<option value='0'>---Select---</option>"	
		$.each(responseJson, function(index, json) {
			$.each(json.COUNTERPARTY_DTL, function(index_1, json_1) {
				option+="<option value="+json_1.COUNTERPARTY_CD+">"+json_1.COUNTERPARTY_NM+"</option>"
			});
		});
		document.forms[0].counterparty_cd.innerHTML=option;
	});
	document.getElementById("loading").style.visibility = "hidden";
}


function fetchTransporterPlantDtl(obj)
{
	document.getElementById("loading").style.visibility = "visible";
	$.post("../servlet/DB_MapMaster_Ajax?setCallType=TRANS_PLANT_DTL&transporter_cd="+obj.value, function(responseJson) {
		//console.log(responseJson);
		var option = "<option value='0'>---Select---</option>"	
		$.each(responseJson, function(index, json) {
			$.each(json.TRANS_PLANT_DTL, function(index_1, json_1) {
				option+="<option value="+json_1.SEQ_NO+">"+json_1.PLANT_NM+"</option>"
			});
		});
		document.forms[0].transporter_plant_seq.innerHTML=option;
	});
	document.getElementById("loading").style.visibility = "hidden";
}

function fetchPlantDtl(obj)
{
	var entity = document.forms[0].contract_type.value;
	document.getElementById("loading").style.visibility = "visible";
	$.post("../servlet/DB_MapMaster_Ajax?setCallType=PLANT_DTL&counterparty_cd="+obj.value+"&entity="+entity, function(responseJson) {
		//console.log(responseJson);
		var option = "<option value='0'>---Select---</option>"	
		$.each(responseJson, function(index, json) {
			$.each(json.PLANT_DTL, function(index_1, json_1) {
				option+="<option value="+json_1.SEQ_NO+">"+json_1.PLANT_NM+"</option>"
			});
		});
		document.forms[0].counterparty_plant_seq.innerHTML=option;
	});
	document.getElementById("loading").style.visibility = "hidden";
}
</script>									
<input type="hidden" name="option" value="MAP_CT_UTR_MST">
<input type="hidden" name="opration" value="INSERT">

<input type="hidden" name="prev_display" value="">
<input type="hidden" name="prev_display1" value="">

<input type="hidden" name="temp_from_dt" value="<%=from_dt%>">
<input type="hidden" name="temp_to_dt" value="<%=to_dt%>">

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
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "Sr#" || title == "")
		{
			//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});

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