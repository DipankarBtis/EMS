<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
//Below functions are added by Pratham Bhatt for paganation 
function changePG(pg)
{
	var page_no = pg;
	var total_pages = document.forms[0].total_pages.value;
	var filter_itm_app = document.forms[0].filter_itm_app.value;
	var filter_po_app = document.forms[0].filter_po_app.value;
	var filter_status = document.forms[0].filter_status.value;
	var filter_root_cause = document.forms[0].filter_root_cause.value;
	var filter_priority = document.forms[0].filter_priority.value;
	var filter_incident_type = document.forms[0].filter_incident_type.value;
	var initiated_By = document.forms[0].initiated_By.value;
	var u = document.forms[0].u.value;
	var url = "frm_incident_mst.jsp?u="+u+"&filter_incident_type="+filter_incident_type+"&filter_root_cause="+filter_root_cause+"&filter_itm_app="+filter_itm_app+"&filter_po_app="+filter_po_app+
	"&filter_status="+filter_status+"&filter_priority="+filter_priority+"&initiated_By="+initiated_By+"&page_no="+page_no;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function nextPage()
{
	var page_no = document.forms[0].page_no.value;
	var total_pages = document.forms[0].total_pages.value;
	var filter_itm_app = document.forms[0].filter_itm_app.value;
	var filter_po_app = document.forms[0].filter_po_app.value;
	var filter_status = document.forms[0].filter_status.value;
	var filter_root_cause = document.forms[0].filter_root_cause.value;
	var filter_priority = document.forms[0].filter_priority.value;
	var filter_incident_type = document.forms[0].filter_incident_type.value;
	var initiated_By = document.forms[0].initiated_By.value;
	if(page_no<total_pages)
	{
		page_no=parseInt(page_no)+1;
		var u = document.forms[0].u.value;
		
		var url = "frm_incident_mst.jsp?u="+u+"&filter_incident_type="+filter_incident_type+"&filter_root_cause="+filter_root_cause+"&filter_itm_app="+filter_itm_app+"&filter_po_app="+filter_po_app+
				"&filter_status="+filter_status+"&filter_priority="+filter_priority+"&initiated_By="+initiated_By+"&page_no="+page_no;
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

function previousPage()
{
	var page_no = document.forms[0].page_no.value;	
	var total_pages = document.forms[0].total_pages.value;
	var filter_itm_app = document.forms[0].filter_itm_app.value;
	var filter_po_app = document.forms[0].filter_po_app.value;
	var filter_status = document.forms[0].filter_status.value;
	var filter_root_cause = document.forms[0].filter_root_cause.value;
	var filter_priority = document.forms[0].filter_priority.value;
	var filter_incident_type = document.forms[0].filter_incident_type.value;
	var initiated_By = document.forms[0].initiated_By.value;
	if(page_no>1)
	{
		page_no=parseInt(page_no)-1;
		var u = document.forms[0].u.value;
		
		var url = "frm_incident_mst.jsp?u="+u+"&filter_incident_type="+filter_incident_type+"&filter_root_cause="+filter_root_cause+"&filter_itm_app="+filter_itm_app+"&filter_po_app="+filter_po_app+
				"&filter_status="+filter_status+"&filter_priority="+filter_priority+"&initiated_By="+initiated_By+"&page_no="+page_no;
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

//till here 

function refresh()
{
	var filter_itm_app = document.forms[0].filter_itm_app.value;
	var filter_po_app = document.forms[0].filter_po_app.value;
	var filter_status = document.forms[0].filter_status.value;
	var filter_root_cause = document.forms[0].filter_root_cause.value;
	var filter_priority = document.forms[0].filter_priority.value;
	var filter_incident_type = document.forms[0].filter_incident_type.value;
	var initiated_By = document.forms[0].initiated_By.value;
	var page_no=document.forms[0].page_no.value;
	var u = document.forms[0].u.value;
	
	var url = "frm_incident_mst.jsp?u="+u+"&filter_incident_type="+filter_incident_type+
			"&filter_root_cause="+filter_root_cause+"&filter_itm_app="+filter_itm_app+"&filter_po_app="+filter_po_app+
			"&filter_status="+filter_status+"&filter_priority="+filter_priority+"&initiated_By="+initiated_By;//+"&page_no="+page_no;


	document.getElementById("loading").style.visibility = "visible";
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

function doSubmit()
{
	var incident_type = document.forms[0].incident_type.value;
	var incident_id = document.forms[0].incident_id.value;
	var assign_to = document.forms[0].assign_to.value;
	var priority = document.forms[0].priority.value;
	var target_dt = document.forms[0].target_dt.value;
	var incident_title = document.forms[0].incident_title.value;
	var incident_detail = document.forms[0].incident_detail.value;
	var status = document.forms[0].status.value;
	
	var opration = document.forms[0].opration.value;
	
	var msg="";
	var flag=true;
	
	if(trim(incident_type)=="")
	{
		msg+="Select Incident Type!\n";
		flag=false;
	}
	if(opration=="MODIFY")
	{
		if(trim(incident_id)=="")
		{
			msg+="Enter Incident ID!\n";
			flag=false;
		}
	}
	if(trim(assign_to)=="" || assign_to=="0")
	{
		msg+="Select Assign To!\n";
		flag=false;
	}
	if(trim(priority)=="")
	{
		msg+="Select Priority!\n";
		flag=false;
	}
	if(trim(target_dt)=="")
	{
		msg+="Select Target Date!\n";
		flag=false;
	}
	if(trim(incident_title)=="")
	{
		msg+="Enter Incident Title!\n";
		flag=false;
	}
	if(trim(incident_detail)=="")
	{
		msg+="Enter Incident Detail!\n";
		flag=false;
	}
	if(trim(status)=="")
	{
		msg+="Select Status!\n";
		flag=false;
	}
	
	if(flag)
	{
		var a = confirm("Do you want to "+opration+" the Incident?");
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

function isGolive(sysdt)
{
	var isLive = document.forms[0].status.value;
	
	if(isLive != "LIVE")
	{
		document.forms[0].prod_rollout_dt.value = "";
		document.forms[0].go_live_dt.value = "";
	}
	else
	{
		document.forms[0].prod_rollout_dt.value = sysdt;
		document.forms[0].go_live_dt.value = sysdt;
	}
}

function doClear()
{
	document.forms[0].incident_type.value="BUG";
	document.forms[0].incident_id.value="";
	document.forms[0].assign_to.value="0";
	document.forms[0].priority.value="Normal";
	document.forms[0].target_dt.value="";
	document.forms[0].incident_title.value="";
	document.forms[0].incident_detail.value="";
	document.forms[0].status.value="New";
	document.forms[0].root_cause.value="";
	document.forms[0].prod_rollout_dt.value="";
	document.forms[0].prod_app_dt.value="";
	document.forms[0].rollout_schedule.value="";
	
	document.forms[0].status.style.pointerEvents = "none";
	document.forms[0].opration.value="INSERT";
	
	document.getElementById("eventBlock").style.display="none";
}
function doModify(INCIDENT_ID,INCIDENT_TYPE,PRIORITY,TARGET_DT,ASSIGN_TO,INCIDENT_TITLE,STATUS,ROOT_CAUSE,PROD_ROLLOUT_DT,PROD_APP_DT,VROLLOUT_SCHEDULE)
{
	document.forms[0].incident_type.value=INCIDENT_TYPE;
	document.forms[0].incident_id.value=INCIDENT_ID;
	document.forms[0].assign_to.value=ASSIGN_TO;
	document.forms[0].priority.value=PRIORITY;
	document.forms[0].target_dt.value=TARGET_DT;
	document.forms[0].incident_title.value=INCIDENT_TITLE;
	document.forms[0].incident_detail.value="";
	document.forms[0].status.value=STATUS;
	document.forms[0].root_cause.value=ROOT_CAUSE;
	document.forms[0].prod_rollout_dt.value=PROD_ROLLOUT_DT;
	document.forms[0].prod_app_dt.value=PROD_APP_DT;
	
	if(VROLLOUT_SCHEDULE!="")
	{
		document.forms[0].rollout_schedule.value=VROLLOUT_SCHEDULE;
	}
	else
	{
		document.forms[0].rollout_schedule.value="";
	}
	document.forms[0].status.style.pointerEvents = "auto";
	
	document.forms[0].opration.value="MODIFY";
	
	document.getElementById("eventBlock").style.display="flex";
}

function itmApprove(incident_id,incident_title,incident_type)
{
	document.forms[0].incident_id.value=incident_id;
	document.forms[0].incident_title.value=incident_title;
	document.forms[0].incident_type.value=incident_type;
	document.forms[0].opration.value="ITM_APP";
	
	if(incident_id != null)
	{
		var a = confirm("Do you want to enable ITM Approval for Incident "+incident_id+ " ?");
		
		if(a)
		{
			document.forms[0].itm_app_flag.value='Y';
			
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
}

function poApprove(incident_id,incident_title,incident_type)
{
	document.forms[0].incident_id.value=incident_id;
	document.forms[0].incident_title.value=incident_title;
	document.forms[0].incident_type.value=incident_type;
	document.forms[0].opration.value="PO_APP";
	
	if(incident_id != null)
	{
		var a = confirm("Do you want to enable PO Approval for Incident "+incident_id+ " ?");
		
		if(a)
		{
			document.forms[0].po_app_flag.value='Y';
			
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
}

function checkSpChar()
{
	var incident_title = document.forms[0].incident_title.value;
	var iChars = "!@#$%^&*()+=-[]\\\';,./{}|\":<>?";

	for (var i = 0; i < incident_title.length; i++) 
	{
	    if (iChars.indexOf(incident_title.charAt(i)) != -1) 
	    {
	        alert ("Special Characters not allowed in Incident Title !");
	        document.forms[0].incident_title.value="";
	        return false;
	        
	    }
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.incident.DataBean_Incident" id="db_incident" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdt = utildate.getSysdate();

String filter_itm_app = request.getParameter("filter_itm_app")==null?"0":request.getParameter("filter_itm_app");
String filter_po_app = request.getParameter("filter_po_app")==null?"0":request.getParameter("filter_po_app");
String filter_status = request.getParameter("filter_status")==null?"0":request.getParameter("filter_status");
String filter_root_cause = request.getParameter("filter_root_cause")==null?"0":request.getParameter("filter_root_cause");
String filter_priority = request.getParameter("filter_priority")==null?"0":request.getParameter("filter_priority");
String filter_incident_type = request.getParameter("filter_incident_type")==null?"0":request.getParameter("filter_incident_type");
String filter_initiated_By = request.getParameter("initiated_By")==null?"0":request.getParameter("initiated_By");

String page_no = request.getParameter("page_no")==null?"1":request.getParameter("page_no");

db_incident.setCallFlag("INCIDENT_MST");
db_incident.setComp_cd(owner_cd);
db_incident.setFilter_itm_app(filter_itm_app);
db_incident.setFilter_po_app(filter_po_app);
db_incident.setFilter_initiated_by(filter_initiated_By);
db_incident.setFilter_root_cause(filter_root_cause);
db_incident.setFilter_status(filter_status);
db_incident.setFilter_priority(filter_priority);
db_incident.setFilter_incident_type(filter_incident_type);
db_incident.setEmp_cd(emp_cd);
db_incident.setPage_no(page_no);	//Added by Pratham Bhatt for pagenation
db_incident.init();

Vector VINCIDENT_ID = db_incident.getVINCIDENT_ID();
Vector VINCIDENT_TYPE = db_incident.getVINCIDENT_TYPE();
Vector VINCIDENT_TITLE = db_incident.getVINCIDENT_TITLE();
Vector VPRIORITY = db_incident.getVPRIORITY();
Vector VINCIDENT_DTL = db_incident.getVINCIDENT_DTL();
Vector VTARGET_DT = db_incident.getVTARGET_DT();
Vector VASSIGN_TO = db_incident.getVASSIGN_TO();
Vector VASSIGN_TO_NM = db_incident.getVASSIGN_TO_NM();
Vector VLIVE_DT = db_incident.getVLIVE_DT();
Vector VSTATUS = db_incident.getVSTATUS();
Vector VITM_APP_FLAG = db_incident.getVITM_APP_FLAG();
Vector VITM_APP_BY = db_incident.getVITM_APP_BY();
Vector VITM_APP_DT = db_incident.getVITM_APP_DT();
Vector VPO_APP_FLAG = db_incident.getVPO_APP_FLAG();
Vector VPO_APP_BY = db_incident.getVPO_APP_BY();
Vector VPO_APP_DT = db_incident.getVPO_APP_DT();
Vector VENT_BY = db_incident.getVENT_BY();
Vector VENT_DT = db_incident.getVENT_DT();
Vector VMODIFY_BY = db_incident.getVMODIFY_BY();
Vector VMODIFY_DT = db_incident.getVMODIFY_DT();
Vector VROOT_CAUSE = db_incident.getVROOT_CAUSE();
Vector VPROD_APP_DT = db_incident.getVPROD_APP_DT();
Vector VPROD_ROLLOUT_DT = db_incident.getVPROD_ROLLOUT_DT();
Vector VROLLOUT_SCHEDULE = db_incident.getVROLLOUT_SCHEDULE();

Vector VDTL_SEQ_NO = db_incident.getVDTL_SEQ_NO();
Vector VDTL_ENT_BY = db_incident.getVDTL_ENT_BY();
Vector VDTL_ENT_DT = db_incident.getVDTL_ENT_DT();
Vector VDTL_INCIDENT_DTL = db_incident.getVDTL_INCIDENT_DTL();
Vector VDTL_ASSIGN_TO = db_incident.getVDTL_ASSIGN_TO();
Vector VDTL_ATTACHMENT = db_incident.getVDTL_ATTACHMENT();
Vector VDTL_STATUS = db_incident.getVDTL_STATUS();
Vector VATTACHMENT_PATH = db_incident.getVATTACHMENT_PATH();
Vector VDTL_ROOT_CAUSE = db_incident.getVDTL_ROOT_CAUSE();
Vector VDTL_ITM_APP_FLAG = db_incident.getVDTL_ITM_APP_FLAG();
Vector VDTL_ITM_APP_BY = db_incident.getVDTL_ITM_APP_BY();
Vector VDTL_ITM_APP_DT = db_incident.getVDTL_ITM_APP_DT();
Vector VDTL_PO_APP_FLAG = db_incident.getVDTL_PO_APP_FLAG();
Vector VDTL_PO_APP_BY = db_incident.getVDTL_PO_APP_BY();
Vector VDTL_PO_APP_DT= db_incident.getVDTL_PO_APP_DT();

Vector VINDEX = db_incident.getVINDEX();
Vector VEMP_CD = db_incident.getVEMP_CD();
Vector VEMP_NM = db_incident.getVEMP_NM();

Vector VFILTER_STATUS = db_incident.getVFILTER_STATUS();
Vector VFILTER_ROOT_CAUSE = db_incident.getVFILTER_ROOT_CAUSE();
Vector VFILTER_COUNT_ROOT_CAUSE = db_incident.getVFILTER_COUNT_ROOT_CAUSE();
Vector VFILTER_COUNT_STATUS = db_incident.getVFILTER_COUNT_STATUS();
Vector VFILTER_PRIORITY = db_incident.getVFILTER_PRIORITY();
Vector VFILTER_COUNT_PRIORITY = db_incident.getVFILTER_COUNT_PRIORITY();
Vector VFILTER_INCIDENT_TYPE = db_incident.getVFILTER_INCIDENT_TYPE();
Vector VFILTER_COUNT_INCIDENT_TYPE = db_incident.getVFILTER_COUNT_INCIDENT_TYPE();
Vector VFILTER_INITIATED_BY = db_incident.getVFILTER_INITIATED_BY();
Vector VFILTER_INITIATED_BY_CD = db_incident.getVFILTER_INITIATED_BY_CD();
Vector VFILTER_COUNT_INITIATED_BY = db_incident.getVFILTER_COUNT_INITIATED_BY();

//String isSupAdmn = db_incident.getIsSupAdmn();

int res = db_incident.getTotalPages();
int totalEntries = db_incident.getTotalEntries();
int startNo = db_incident.getStartEntryNo();
int endNo = db_incident.getEndEntryNo();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Incident" enctype="multipart/form-data">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}else if(msg_type.equals("F")){%>
				<div class="fadealert"><%= utilmsg.warningMessage(msg)%></div>
			<%}
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		Incident Tracker 
	   	 				</div>
					 	<a href="../incident/xls_incident_mst.jsp?fileName=IncidentTrackerReport.xls&filter_status=<%=filter_status%>&filter_root_cause=<%=filter_root_cause %>&filter_priority=<%=filter_priority%>&filter_incident_type=<%=filter_incident_type %>&initiated_By=<%=filter_initiated_By %>&filter_itm_app=<%=filter_itm_app%>&filter_po_app=<%=filter_po_app %>" download="Incident Tracker">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-12 col-xs-12 col-md-12" align="right">
							<div class="btn-group">
								<label class="btn btn-outline-secondary subbtngrp" data-bs-toggle="modal" data-bs-target="#IncidentModal" onclick="doClear();fetchTargetDate();">Add New Incident</label><!-- fetchIncidentDtl(); -->
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="search_by_filter">
									<thead><!--  id="tbsearch"> -->										
										<tr>
											<th rowspan="2"></th>
											<th rowspan="2">Incident ID
												<!-- <br><div align="center"><input class="form-control form-control-sm" type="text" id="inci_ID" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div> -->
											</th>
											<th rowspan="2">
												Priority
											<br>
									    	<div align="center">
										    	<select class="form-select form-select-sm" name="filter_priority" onchange="refresh();" style="width:75px;" >
										    		<option value="0">All</option>
											    	<%for(int i=0; i<VFILTER_PRIORITY.size(); i++){ %>
											    		<option value="<%=VFILTER_PRIORITY.elementAt(i)%>"><%=VFILTER_PRIORITY.elementAt(i)%>&nbsp;(<%=VFILTER_COUNT_PRIORITY.elementAt(i)%>)</option>
											    	<%} %>
										    	</select>
									    	</div>
									    	<script>document.forms[0].filter_priority.value="<%=filter_priority%>"</script>
											</th>
									    	<th rowspan="2">
									    	Incident Type
									    	<br>
									    	<div align="center">
										    	<select class="form-select form-select-sm" name="filter_incident_type" onchange="refresh();" style="width:75px;" >
										    		<option value="0">All</option>
											    	<%for(int i=0; i<VFILTER_INCIDENT_TYPE.size(); i++){ %>
											    		<option value="<%=VFILTER_INCIDENT_TYPE.elementAt(i)%>"><%=VFILTER_INCIDENT_TYPE.elementAt(i)%>&nbsp;(<%=VFILTER_COUNT_INCIDENT_TYPE.elementAt(i)%>)</option>
											    	<%} %>
										    	</select>
									    	</div>
									    	<script>document.forms[0].filter_incident_type.value="<%=filter_incident_type%>"</script>
									    	</th>				    		
									    	<th rowspan="2">Incident Title</th>
									    	<th rowspan="2">Incident Detail</th>
									    	<th rowspan="2">Target Date</th>
									    	<th rowspan="2">Assign To</th>
									    	<th rowspan="2">
									    	Status
									    	<br>
									    	<div align="center">
										    	<select class="form-select form-select-sm" name="filter_status" onchange="refresh();" >
										    		<option value="0">All</option>
											    	<%for(int i=0; i<VFILTER_STATUS.size(); i++){ %>
											    		<option value="<%=VFILTER_STATUS.elementAt(i)%>"><%=VFILTER_STATUS.elementAt(i)%>&nbsp;(<%=VFILTER_COUNT_STATUS.elementAt(i)%>)</option>
											    	<%} %>
										    	</select>
									    	</div>
									    	<script>document.forms[0].filter_status.value="<%=filter_status%>"</script>
									    	</th>
									    	<th rowspan="2">
									    	Resolution
									    	<br>
									    	<div align="center">
										    	<select class="form-select form-select-sm" name="filter_root_cause" onchange="refresh();" >
										    		<option value="0">All</option>
											    	<%for(int i=0; i<VFILTER_ROOT_CAUSE.size(); i++){ %>
											    		<option value="<%=VFILTER_ROOT_CAUSE.elementAt(i)%>">
											    			<%if(VFILTER_ROOT_CAUSE.elementAt(i).equals("IMP")){%>
											    				Implemented
											    			<%}else if(VFILTER_ROOT_CAUSE.elementAt(i).equals("ADNAB")){%>
										    					As designed/Not A BUG
										    				<%}else if(VFILTER_ROOT_CAUSE.elementAt(i).equals("WAP")){%>
										    					Workaround Provided
										    				<%}else if(VFILTER_ROOT_CAUSE.elementAt(i).equals("SNI")){%>
										    					System/Network Issue
										    				<%}%>&nbsp;(<%=VFILTER_COUNT_ROOT_CAUSE.elementAt(i)%>)</option>
											    	<%} %>
										    	</select>
									    	</div>
									    	<script>document.forms[0].filter_root_cause.value="<%=filter_root_cause%>"</script>
									    	</th>
									    	<th rowspan="2">
									    	Initiated By
									    	<br>
										    	<select class="form-select form-select-sm" name="initiated_By" onchange="refresh();" >
							      					<option value="0">--Select--</option>
							      					<%for(int i=0; i<VFILTER_INITIATED_BY.size(); i++){ %>
							      					<option value="<%=VFILTER_INITIATED_BY_CD.elementAt(i)%>"><%=VFILTER_INITIATED_BY.elementAt(i)%>&nbsp;(<%=VFILTER_COUNT_INITIATED_BY.elementAt(i)%>)</option>
							      					<%} %>
							      				</select>
							      			<script>document.forms[0].initiated_By.value="<%=filter_initiated_By%>"</script>
									    	</th>
									    	<th colspan="2">Initial Approval</th>
									    	
									    	<th rowspan="2">Last Modified By</th>
									    	<th colspan="3">Production Rollout Details</th>
										</tr>
										<tr>
											<th>ITM
									    	<br>
									    	<div align="center">
										    	<select class="form-select form-select-sm" name="filter_itm_app" onchange="refresh();" >
										    		<option value="0">All</option>
										    		<option value="Y">Yes</option>
										    		<option value="N">No</option>
										    	</select>
									    	</div>
									    	<script>document.forms[0].filter_itm_app.value="<%=filter_itm_app%>"</script>
									    	</th>
									    	<th>BAO
									    	<br>
									    	<div align="center">
										    	<select class="form-select form-select-sm" name="filter_po_app" onchange="refresh();" >
										    		<option value="0">All</option>
										    		<option value="Y">Yes</option>
										    		<option value="N">No</option>
										    	</select>
									    	</div>
									    	<script>document.forms[0].filter_po_app.value="<%=filter_po_app%>"</script>
									    	</th>
									    	<th rowspan="1">Approval Date</th>
									    	<th rowspan="1">Go Live Date</th>
									    	<th rowspan="1">Go Live Schedule</th>
										</tr>
									</thead>
									<tbody id="mainTbody">
									<%int j=0;int k=0;
									if(VINCIDENT_ID.size()>0){%>
										<%for(int i=0; i<VINCIDENT_ID.size(); i++)
										{ 
										int size = Integer.parseInt(""+VINDEX.elementAt(i));
										%>
											<tr>
												<td align="center">
												<%if(!VSTATUS.elementAt(i).equals("Close")){ %>
													<font title="Click to Edit" style="color:var(--header_color)">
														<i class="fa fa-edit fa-lg" data-bs-toggle="modal" data-bs-target="#IncidentModal" 
														onclick="doModify('<%=VINCIDENT_ID.elementAt(i)%>','<%=VINCIDENT_TYPE.elementAt(i)%>',
																		'<%=VPRIORITY.elementAt(i)%>','<%=VTARGET_DT.elementAt(i)%>',
																		'<%=VASSIGN_TO.elementAt(i)%>','<%=VINCIDENT_TITLE.elementAt(i)%>','<%=VSTATUS.elementAt(i)%>',
																		'<%=VROOT_CAUSE.elementAt(i)%>','<%=VPROD_ROLLOUT_DT.elementAt(i)%>','<%=VPROD_APP_DT.elementAt(i)%>','<%=VROLLOUT_SCHEDULE.elementAt(i) %>');fetchEventDtl();">
														</i>
													</font>
												<%} %>	
												</td>
												<td onclick="hide_show('tbody<%=i %>','hidCont<%=i%>');" align="center">
		    										<%=VINCIDENT_ID.elementAt(i)%>&nbsp;&nbsp;&nbsp;<span id="hidCont<%=i%>" class="fa fa-expand" title="Click here to show Incident summary"></span>
		    									</td>
		    									<td align="center"><%=VPRIORITY.elementAt(i)%></td>
		    									<td align="center"><%=VINCIDENT_TYPE.elementAt(i)%></td>
		    									<td style="white-space:normal;"><%=VINCIDENT_TITLE.elementAt(i)%></td>
		    									<td style="white-space:normal;"><%=VINCIDENT_DTL.elementAt(i).toString().replace("\n", "<br/>")%></td>
		    									<td align="center"><%=VTARGET_DT.elementAt(i)%></td>
		    									<td align="center"><%=VASSIGN_TO_NM.elementAt(i)%></td>
		    									<td valign="middle"  align="center">
		    										<span 
			    									<%if(VSTATUS.elementAt(i).equals("New")){ %>
			    										class="alert alert-info"
			    									<%}else if(VSTATUS.elementAt(i).equals("In Progress")){ %>
			    										class="alert alert-warning"
			    									<%}else if(VSTATUS.elementAt(i).equals("Awaiting Customer")){ %>
			    										class="alert" style="background:#ffdb99;color: #ff4000;"
			    									<%}else if(VSTATUS.elementAt(i).equals("Close")){ %>
			    										class="alert alert-dark"
			    									<%}else if(VSTATUS.elementAt(i).equals("Solution Provided")){ %>
			    										class="alert alert-success"
			    									<%}else if(VSTATUS.elementAt(i).equals("Awaiting BIPL")){ %>
			    										class="alert alert-danger"
			    									<%}else if(VSTATUS.elementAt(i).equals("Enhancement Required")){ %>
			    										class="alert" style="background:#e6ccff;color:#330066;"
			    									<%}else if(VSTATUS.elementAt(i).equals("UAT Completed")){ %>
			    										class="alert alert-primary"
			    									<%}else if(VSTATUS.elementAt(i).equals("On Hold By Customer")){ %>
			    										class="alert alert-secondary"
			    									<%}else if(VSTATUS.elementAt(i).equals("Under UAT")){ %>
			    										class="alert" style="background:#b3ffb3;color: #008000;"
			    									<%}else if(VSTATUS.elementAt(i).equals("LIVE")){ %>
			    										class="alert" style="background:#b3ffb3;color: #008000;"
			    									<%} %>
			    									><b><%=VSTATUS.elementAt(i)%></b></span>
		    									</td>
		    									<td valign="middle" align="center">
			    									<%if(VROOT_CAUSE.elementAt(i).equals("IMP")){%>
			    										<span class="alert alert-success"><b>Implemented</b></span>
			    									<%}else if(VROOT_CAUSE.elementAt(i).equals("ADNAB")){%>
			    										<span class="alert alert-secondary"><b>As designed/Not A BUG</b></span>
			    									<%}else if(VROOT_CAUSE.elementAt(i).equals("WAP")){%>
			    										<span class="alert alert-primary"><b>Workaround Provided</b></span>
			    									<%}else if(VROOT_CAUSE.elementAt(i).equals("SNI")){%>
				 										<span class="alert alert-info" ><b>System/Network Issue</b></span>
				 									<%}%>
			 									</td>
			 									<td align="center"><%=VENT_BY.elementAt(i)%><br><%=VENT_DT.elementAt(i) %></td>
			 									<td align="center" valign="middle">
		    										<%if(VITM_APP_FLAG.elementAt(i).equals("Y")){ %>
														<i title="" style="color: green" class="fa fa-thumbs-up fa-2x" aria-hidden="true"></i>
														<br><%=VITM_APP_BY.elementAt(i)%>
														<br><%=VITM_APP_DT.elementAt(i)%>
		    										<%}else if(VITM_APP_FLAG.elementAt(i).equals("")){ %>
		    											<i style="color: grey"  class="fa fa-thumbs-up fa-2x" aria-hidden="true"<%if(approve_access.equals("Y")){ %>title="Click To Approve Incident"  onclick="itmApprove('<%=VINCIDENT_ID.elementAt(i)%>','<%=VINCIDENT_TITLE.elementAt(i)%>','<%=VINCIDENT_TYPE.elementAt(i)%>')"<%} %>></i>
		    											<br><%=VITM_APP_BY.elementAt(i)%>
														<br><%=VITM_APP_DT.elementAt(i)%>
		    										<%}%>
		    									</td>
		    									<td align="center" valign="middle">
		    										<%if(VPO_APP_FLAG.elementAt(i).equals("Y")){ %>
														<i title="" style="color: green" class="fa fa-thumbs-up fa-2x" aria-hidden="true"></i>
														<br><%=VPO_APP_BY.elementAt(i)%>
														<br><%=VPO_APP_DT.elementAt(i)%>
		    										<%}else if(VPO_APP_FLAG.elementAt(i).equals("")){ %>
		    											<i style="color: grey"  class="fa fa-thumbs-up fa-2x" aria-hidden="true" <%if(approve_access.equals("Y")){ %> title="Click To Approve Incident" onclick="poApprove('<%=VINCIDENT_ID.elementAt(i)%>','<%=VINCIDENT_TITLE.elementAt(i)%>','<%=VINCIDENT_TYPE.elementAt(i)%>')"<%} %>></i>
		    											<br><%=VPO_APP_BY.elementAt(i)%>
														<br><%=VPO_APP_DT.elementAt(i)%>
		    										<%}%>
		    									</td>
		    									<%-- <td align="center"><%=VLIVE_DT.elementAt(i)%></td> --%>
		    									<td align="center"><%=VMODIFY_BY.elementAt(i)%><br><%=VMODIFY_DT.elementAt(i)%></td>
		    									<td align="center"><%=VPROD_APP_DT.elementAt(i)%></td>
		    									<td align="center"><%=VPROD_ROLLOUT_DT.elementAt(i)%></td>
		    									<td align="center">
		    									<%if(VROLLOUT_SCHEDULE.elementAt(i).equals("U")){%>
		    										URGENT
		    									<% }else if(VROLLOUT_SCHEDULE.elementAt(i).equals("S")){%>
		    										SCHEDULED
		    									<% } %>
		    									</td>
											</tr>
											<%if(size>0){k=0;%>
											<tbody id="tbody<%=i%>" style="display:none;">
												<tr style="text-align:center;font-weight:bold;background:#bce6ff;color:#0c63e4;">
													<td colspan="3" rowspan="<%=size+1%>" style="background:white;"></td>
													<td>Event#</td>
													<td>Modified By</td>
													<td>Detail</td>
													<td>Attachment</td>
													<td>Assigned To</td>
													<td>Status</td>
													<td>Resolution</td>
													<td colspan="7" rowspan="<%=size+1%>" style="background:white;"></td>
												</tr>
												<%for(j=j; j<VDTL_INCIDENT_DTL.size(); j++){ 
												k+=1;
												%>
													<tr>
														<td align="center"><%=VDTL_SEQ_NO.elementAt(j)%></td>
														<td align="center"><%=VDTL_ENT_BY.elementAt(j)%><br><%=VDTL_ENT_DT.elementAt(j)%></td>
														<td style="white-space:normal;" width="450px"><%=VDTL_INCIDENT_DTL.elementAt(j).toString().replace("\n", "<br/>")%></td>
														<td>
														<%if(!VATTACHMENT_PATH.elementAt(j).equals("")){ %>
															<a href="<%=VATTACHMENT_PATH.elementAt(j)%>" download><font color="blue"><%=VDTL_ATTACHMENT.elementAt(j) %>&nbsp;<i class="fa fa-arrow-circle-down fa-lg"></i></font></a>
														<%} %>
														</td>
														<td align="center"><%=VDTL_ASSIGN_TO.elementAt(j) %></td>
				    									<td align="center"><%=VDTL_STATUS.elementAt(j) %></td>
														<td valign="middle" align="center">
					    									<%if(VDTL_ROOT_CAUSE.elementAt(j).equals("IMP")){%>
					    										Implemented
					    									<%}else if(VDTL_ROOT_CAUSE.elementAt(j).equals("ADNAB")){%>
					    										As designed/Not A BUG
					    									<%}else if(VDTL_ROOT_CAUSE.elementAt(j).equals("WAP")){%>
					    										Workaround Provided
					    									<%}else if(VDTL_ROOT_CAUSE.elementAt(j).equals("SNI")){%>
						 										System/Network Issue
						 									<%}else if(VDTL_ROOT_CAUSE.elementAt(j).equals("CI")){%>
						 										Configuration Issue	
						 									<%}%>
					 									</td>
													</tr>
												<%
													if(k==size)
													{
														j=j+1;
														break;
													}
												} %>
											</tbody>
											<%} %>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>No Inciednt Configured!</b>") %></td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<!-- for appending the pagination  -->
					<!-- https://getbootstrap.com/docs/4.0/components/pagination/  -->
					<div class="contianer">
					<div></div>
					<div></div>
						<nav aria-label="Page navigation example">
						  	<ul class="pagination justify-content-center">
							  	<li class="page-item">
							  	<%if(startNo==endNo){ %>
							  		<span class="page-link text-dark">Showing entry <%=startNo %> of total entries:<%=totalEntries %> &nbsp&nbsp&nbsp</span>
							  	<%}else{ %>
							  		<span class="page-link text-dark">Showing entries <%=startNo %>-<%=endNo %> of total entries:<%=totalEntries %> &nbsp&nbsp&nbsp</span>
							  	<%} %>
							  	</li>
						   	 <li class="page-item" id="prev_link">
						      	<a class="page-link" href="#" aria-label="Previous"   onclick="previousPage()">
						        	<span aria-hidden="true">&laquo;</span>
						        	<span class="sr-only">Previous</span>
						      	</a>
						    	</li>
						    	<%for(int i=1; i<res+1;i++){%>
						    	<li class="page-item" id="selected_<%=i%>">
						    		<a class="page-link" href="#" id="link_<%=i%>" onclick="changePG('<%=i %>')">
						    			<%=i %>
						    		</a>
						    	</li>
						    	<%}%>
						    	<li class="page-item" id="next_link">
						      		<a class="page-link" href="#" aria-label="Next"  onclick="nextPage()">
						        	<span aria-hidden="true">&raquo;</span>
						        	<span class="sr-only">Next</span>
						      	</a>
						    	</li>
						 	 </ul>
						</nav>
					</div>
					<!--  till here -->
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="IncidentModal" data-bs-backdrop="static" data-bs-keyboard="false">
	<div class="modal-dialog modal-dialog-scrollable modal-xl">
    	<div class="modal-content">
    		<div class="modal-header cdheader">
        		<div class="topheader">
					Incident
				</div>
        		<input type="button" class="btn-close" data-bs-dismiss="modal">
      		</div>
      		<div class="modal-body mdbody">
      			<div class="cdbody">
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Incident<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-auto">
				      				<select class="form-select form-select-sm" name="incident_type">
										<option value="BUG">BUG</option>
										<option value="ENH">ENH</option>
										<option value="FEA">FEA</option>
										<option value="QUE">QUE</option>
									</select>
						    	</div>
						    	<div class="col-auto">
						    		<input type="text" class="form-control form-control-sm" name="incident_id" value="" readOnly style="font-weight:bold;">
						    	</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Assign To<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="assign_to">
				      					<option value="0">--Select--</option>
				      					<%for(int i=0; i<VEMP_CD.size(); i++){ %>
				      					<option value="<%=VEMP_CD.elementAt(i)%>"><%=VEMP_NM.elementAt(i)%></option>
				      					<%} %>
				      				</select>
				    			</div>
				  			</div>
						</div>
					</div>
      				<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Priority<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="priority" onchange="fetchTargetDate();">
				      					<option value="Normal">Normal</option>
										<option value="High">High</option>
										<option value="Critical">Critical</option>
										<option value="Low">Low</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Target Completion<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="target_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Incident Title<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<textarea class="form-control form-control-sm" name="incident_title" cols="75" rows="1" onkeyup="checkSpChar()"></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Incident Detail<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<textarea class="form-control form-control-sm" name="incident_detail" cols="75" rows="3"></textarea>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5" id="eventBlock" style="display:none;">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Event Detail</b></label>
				  			</div>
						</div>
						<div class="col-sm-10 col-xs-10 col-md-10">  
							<div class="form-group row">
				    			<div class="col-sm-10 col-xs-10 col-md-10">
				    				<div id="event" style="max-height: 250px;width:100%;display: block;overflow-x:auto;white-space: nowrap;"></div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Attachment</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
				      					<input type="file" class="form-control form-control-sm" name="attachment" id="attachment">
					      				<span class="input-group-text"><i class="fa fa-upload fa-lg"></i></span>
					      			</div>
				    			</div>
				  			</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Status<span class="s-red">*</span></b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="status">
				      					<option value="New">New</option>
										<option value="In Progress">In Progress</option>
										<option value="Solution Provided">Solution Provided</option>
										<option value="Awaiting Customer">Awaiting Customer</option>
										<option value="Awaiting BIPL">Awaiting BIPL</option>
										<option value="Enhancement Required">Enhancement Required</option>
										<option value="Close">Close</option>
										<option value="On Hold By Customer">On Hold By Customer</option>
										<option value="Under UAT">Under UAT</option>
										<option value="UAT Completed">UAT Completed</option>
										<option value="LIVE">LIVE</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Resolution</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="root_cause">
				      					<option value="">--Select--</option>
				      					<option value="IMP">Implemented</option>
										<option value="ADNAB">As designed/Not A BUG</option>
										<option value="WAP">Workaround Provided</option>
										<option value="SNI">System/Network Issue</option>
										<option value="CI">Configuration Issue</option>										
				      				</select>
				    			</div>
				  			</div>
						</div>
					</div>&nbsp;
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Production Rollout Details</label>
					</div>	
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Approval Date</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="prod_app_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" <%if(!execute_access.equals("Y")){ %>readonly style="pointer-events: none;"<%} %>>
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
				  			</div>
						</div>
						
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Go Live Schedule</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="rollout_schedule" <%if(!execute_access.equals("Y")){ %> style="pointer-events: none; background-color: #e9ecef;"<%} %>>
				      					<option value="" selected="selected">--Select--</option>
				      					<option value="S">SCHEDULED</option>
				      					<option value="U">URGENT</option>
				      				</select>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2"> 
							<div class="form-group row">
				    			<label class="form-label"><b>Go Live Date</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="prod_rollout_dt" value="" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off" <%if(!execute_access.equals("Y")){ %>readonly style="pointer-events: none;"<%} %>>
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
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

<input type="hidden" name="option" value="INCIDENT_MST">
<input type="hidden" name="opration" value="INSERT">
<input type="hidden" name="po_app_flag" value="">
<input type="hidden" name="itm_app_flag" value="">
<input type="hidden" name="go_live_dt" value="">
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

<input type="hidden" name="page_no" value ="<%=page_no%>">
<input type="hidden" name="total_pages" value ="<%=res%>">

<script>
/*function fetchIncidentDtl()
{
	$.post("../servlet/DB_Incident_Ajax?setCallType=INCIDENT_DTL", function(responseJson) {
		//console.log(responseJson);
		$.each(responseJson, function(index, json) {
			$.each(json.INCIDENT_DTL, function(index_1, json_1) {
				if(json_1.INCIDENT_ID!=undefined)
				{
					document.forms[0].incident_id.value=json_1.INCIDENT_ID;
				}
			});
		});
	});
}
*/

function fetchTargetDate()
{
	var priority = document.forms[0].priority.value;
	$.post("../servlet/DB_Incident_Ajax?priority="+priority+"&setCallType=TARGET_DT", function(responseJson) {
		//console.log(responseJson);
		$.each(responseJson, function(index, json) {
			$.each(json.INCIDENT_DTL, function(index_1, json_1) {
				
				document.forms[0].target_dt.value=json_1.TARGET_DT;
			});
		});
	});
}

function fetchEventDtl()
{
	var incident_id = document.forms[0].incident_id.value;
	$.post("../servlet/DB_Incident_Ajax?incident_id="+incident_id+"&setCallType=EVENT_DTL", function(responseJson) {
		//console.log(responseJson);
		$.each(responseJson, function(index, json) {
			var display="";
			$.each(json.EVENT_DTL, function(index_1, json_1) {
				
				display+="<b style='color:blue;'>*</b>&nbsp;<label>"+json_1.EventBy+"</label> [<label>"+json_1.EventDate+"</label>] :: <br/>";
				while(json_1.EventDtl.includes("\n")){ 
					json_1.EventDtl = json_1.EventDtl.replace("\n", "<br/>");
				}
				display+="<label>"+json_1.EventDtl+"</label><br/>";
			});
			document.getElementById('event').innerHTML=display;
		});
	});
}

$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "")
		{
			$(this).html("");
		}
		else
		{
			$(this).html(title+'<br><div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
		}
	});
	
});

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
  	/* var tbody=table.getElementsByTagName("tbody");
  	alert(tbody.length)
  	for (var r = 0; r < tbody.length; r++) 
  	{
  		alert(tbody[r].id);
  		if(trim(tbody[r].id) == "")
		{alert("here")
  			mainTbody
  	 */		tr = document.getElementById("mainTbody").getElementsByTagName("tr");
			//alert(tr.length)
  		  	for (i = 0; i < tr.length; i++) 
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
		/* }
  		else
  		{
  			/* var id1='tbody'+r;
  			var id2='hidCont'+r;
  			
  			document.getElementById(id1).style.display='none';
  			document.getElementById(id2).className='fa fa-expand'; */
  		/*}
  	} */
}
var page_no = document.forms[0].page_no.value;	
var total_pages = document.forms[0].total_pages.value;
var sel = document.getElementById("selected_"+page_no);
sel.setAttribute("class", "page-item active");
document.getElementById("link_"+page_no).disabled = true;
document.getElementById("link_"+page_no).style.pointerEvents = "none";
if(page_no==total_pages)
{
	document.getElementById("next_link").setAttribute("class", "page-item disabled");
}
else if(page_no==1)
{
	document.getElementById("prev_link").setAttribute("class", "page-item disabled");
}
if(total_pages==1)
{
	document.getElementById("next_link").setAttribute("class", "page-item disabled");
	document.getElementById("prev_link").setAttribute("class", "page-item disabled");	
}


</script>

</form>
<script>

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("search_by_filter");
  	
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