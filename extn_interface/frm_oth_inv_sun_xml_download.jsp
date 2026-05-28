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
	
	var u = document.forms[0].u.value;
	
	var flag=true;
	var msg="";
	var flag=checkDateRangeOnApply(document.forms[0].from_dt,document.forms[0].to_dt);
	if(flag)
	{
		if(trim(from_dt)!="" && trim(to_dt)!="")
		{
			if(flag==true)
			{
				var url = "../extn_interface/frm_oth_inv_sun_xml_download.jsp?u="+u+
						"&from_dt="+from_dt+"&to_dt="+to_dt;
			
				document.getElementById("loading").style.visibility = "visible";
				location.replace(url);
			}
		}
	}
		
}

function selectFile(indx)
{
	var select_rd=document.forms[0].select_rd;
	var file=document.forms[0].file;
	var file_nm=document.forms[0].file_nm;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var u = document.forms[0].u.value;
	var total_file = document.forms[0].total_file.value;
	
	if(total_file>1)
	{
		file.value=file_nm[indx].value;
	}
	else
	{
		file.value=file_nm.value;
	}
	
	var url = "../extn_interface/frm_oth_inv_sun_xml_download.jsp?u="+u+
	"&from_dt="+from_dt+"&to_dt="+to_dt+"&file="+file.value+"&select_rd="+select_rd.value+"&index="+indx;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function downloadXML(indx)
{
	var appPath = document.forms[0].appPath.value;
	var download = document.getElementById("download"+indx);
	var file_nm=document.forms[0].file_nm;
	var total_file = document.forms[0].total_file.value;
	
	var file_name="";
	if(total_file>1)
	{
		file_name=file_nm[indx].value;
	}
	else
	{
		file_name=file_nm.value;
	}
	
	var dwnfile_path = (appPath+file_name).replace(/\\/g,"/");
	if(appPath!='')
	{
		document.getElementById("download"+indx).href= "../"+dwnfile_path;
	}
	download.setAttribute("download", file_name);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_oth_inv_sun_interface" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String firstDtOfMonth=utildate.getFirstDateOfMonth();
String sysdate = utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?firstDtOfMonth:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String segment=request.getParameter("segment")==null?"":request.getParameter("segment");
String select_rd=request.getParameter("select_rd")==null?"":request.getParameter("select_rd");
String file=request.getParameter("file")==null?"":request.getParameter("file");
String index=request.getParameter("index")==null?"-1":request.getParameter("index");

String workDir=CommonVariable.work_dir+owner_cd;
String sunxml_dir=CommonVariable.sun_xml;
String filePath = request.getServletContext().getRealPath(workDir+"/"+sunxml_dir+"/");
String appPath=workDir+"/"+sunxml_dir+"/";

accounting.setCallFlag("OTH_INV_SUN_XML_DOWNLOAD");
accounting.setComp_cd(owner_cd);
accounting.setXml_gen_from_dt(from_dt);
accounting.setXml_gen_to_dt(to_dt);
accounting.setFileNm(file);
accounting.setFile_path(filePath);
accounting.init();

Vector VJOURNAL_TYPE_NM = accounting.getVJOURNAL_TYPE_NM();
Vector VFILE_NM = accounting.getVSUN_FILE_NM();
		
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
Vector VTRANS_AMT=accounting.getVTRANS_AMT();
%>
<body>
<%@ include file="../home/header.jsp"%>
<%if(!owner_cd.equals("2")) {%>
<div class="box-body">
	<div class="row">
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<div class="card cardmain">
				<div class="card-header cdheader ">
				</div>
				<div class="card-body cdbody">
					<div class="alert alert-info">
						<div class="row">
							<div class="col-sm-12 col-xs-12 col-md-12">
								<div class="form-group row" align="center">
									<label class="form-label"  style="font-size:40px;font-weight: 700;"><i class='fa fa-exclamation-circle fa-lg'></i> Feature Not Supported</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
				</div>
			</div>   
		</div>
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
	</div>
</div>
<%}else{ %>
<form method="post" action="../servlet/Frm_oth_inv_sun_interface">
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
					    	Other Invoice SUN XML Download 
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>From</b></label>
									</div>
									<div class="col">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
									<div class="col-auto">
										<label class="form-label"><b>To</b></label>
									</div>
									<div class="col">
										<div class="input-group input-group-sm" >
											<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
											onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
											<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
									<div class="col">
										<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
				  					</div>
								</div>
							</div>
						</div>
							<%-- <%if(execute_access.equals("Y")){ %>
							<!-- <div class="col-sm-3 col-xs-3 col-md-3"></div> -->
							<div class="col-sm-3 col-xs-3 col-md-3">  
								<div class="d-flex justify-content-end">
									<div class="form-group row">
										<div class="col-auto">
											<!-- <input type="checkbox" class="form-check-input" name="chk"> -->
											<b>Generate SUN XML</b>		
										</div>								
										<div class="col-auto">						
											<i class="fa fa-play-circle-o fa-2x" <%if(VINVOICE_NO.size() > 0){%>style="color: red;" onclick="generateSunXML();" title="Generate Sun XML"<%}else{%>style="pointer-events: none;color:grey;"<%} %> ></i>
										</div>
									</div>	
								</div>	
							</div> 	
							<%} %> --%>
					</div>
				</div>
				
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i>List of XML files for download</label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example1">
								<thead id="tbsearch1">
									<tr>
										<th></th>
										<th>Journal Type</th>
										<th>XML File Name</th>
										<%if(write_access.equals("Y")){ %><th></th><%} %>
									</tr>
								</thead>
								<tbody>
									<%if(VJOURNAL_TYPE_NM.size()>0){%>
										<%for(int i=0;i<VJOURNAL_TYPE_NM.size();i++){%>
											<tr>
												<td align="center">
													<input type="radio" name="select_rd" onclick="selectFile('<%=i%>');"
													<%if(Integer.parseInt(index)==i){%>checked<%} %>>
												</td>
												<td align="center"><%=VJOURNAL_TYPE_NM.elementAt(i) %></td>
												<td align="center">
													<%=VFILE_NM.elementAt(i)%>
													<input type="hidden" name="file_nm" value="<%=VFILE_NM.elementAt(i)%>">
												</td>
												<%if(write_access.equals("Y")){%>
													<td align="center"><a id="download<%=i%>"><i class="fa fa-download fa-lg" aria-hidden="true" <%if(Integer.parseInt(index)==i){%>style="color:green;" onclick="downloadXML('<%=i%>')"<%}else{%>style="color=grey;"<%} %>></i></a></td>
												<%}%>
											</tr>
										<%} %>
									<%}else{%>
										<tr>
											<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>No XML File generated for selected period!</b>") %></td>
										</tr>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead id="tbsearch">
									<tr>
										<th>Account Code</th>
										<th>Account Period</th>
										<th>Transaction Date</th>
										<th>Transaction Reference</th>
										<th>Description</th>
										<th>Base Amount</th>
										<th>COA Codes <br>[Cash Analysis Code]</th>
										<th>Debit/Credit</th>
										<th>Currency Code</th>
										<th>Employee Code</th>
										<th>Cost Center Code</th>
										<th>Business Unit</th>
										<th>TDS</th>
										<th>Due Date</th>
										<th>Invoice Date</th>
									</tr>
								</thead>
								<tbody>
								<%if(!select_rd.equals("")){%>
									<%if(VINVOICE_NO.size()>0){%>
										<%for(int i=0; i<VINVOICE_NO.size();i++){%>
											<tr>
												<td align="center"><%=VACCOUNT_CD.elementAt(i) %></td>
												<td align="center"><%=VPERIOD_START_DT.elementAt(i)%></td>
												<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
												<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
												<td><%=VDESC.elementAt(i) %></td>
												<td align="right"><%=VBASE_AMT.elementAt(i) %></td>
												<td align="center"><%=VCOA_CD.elementAt(i) %></td>
												<td align="center"><%=VDEBIT_CREDIT.elementAt(i) %></td>
												<td align="center"><%=VCURRENCY_CD.elementAt(i) %></td>
												<td align="center"><%=VEMPLOYEE_CD.elementAt(i) %></td>
												<td align="center"><%=VCOST_CTR_CD.elementAt(i) %></td>
												<td align="center"><%=VBU_UNIT_CD.elementAt(i) %></td>
												<td align="center"><%=VCODE.elementAt(i)%></td>
												<td align="center"><%=VINVOICE_DUE_DT.elementAt(i) %></td>
												<td align="center"><%=VINVOICE_DT.elementAt(i) %></td>
											</tr>
										<%} %>
									<%}else{%>
										<tr>
											<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>No Invoice is Generated for Selected Period!</b>") %></td>
										</tr>
									<%} %>
								<%}else{%>
									<tr>
										<td colspan="15" align="center"><%=utilmsg.infoMessage("<b>No Files available for download!</b>") %></td>
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

<input type="hidden" name="file" value="<%=file%>">
<input type="hidden" name="total_file" value="<%=VJOURNAL_TYPE_NM.size()%>">
<input type="hidden" name="appPath" value="<%=appPath %>">

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
<%} %>
</form>
</body>
<script>
$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "Sr#")
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
