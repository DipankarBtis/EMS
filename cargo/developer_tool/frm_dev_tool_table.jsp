<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setActiveInactive(obj, seq_no, lb, flag)
{
	if(obj.checked)
	{
		document.getElementById(lb+seq_no).innerHTML="Yes";
		document.getElementById(flag+seq_no).value="Y";
	}
	else
	{
		document.getElementById(lb+seq_no).innerHTML="No";
		document.getElementById(flag+seq_no).value="N";
	}
}

function enable_disable(obj,index)
{
	if(obj.checked)
	{
		document.getElementById("tbl_nm"+index).disabled=false;
		document.getElementById("archive_flag"+index).disabled=false;
		document.getElementById("archive_active"+index).disabled=false;
		
		document.getElementById("archival_logic"+index).disabled=false;
		document.getElementById("archival_logic"+index).style.pointerEvents = "auto";
	}
	else
	{
		document.getElementById("tbl_nm"+index).disabled=true;
		document.getElementById("archive_flag"+index).disabled=true;
		document.getElementById("archive_active"+index).disabled=true;
		
		document.getElementById("archival_logic"+index).disabled=true;
		document.getElementById("archival_logic"+index).style.pointerEvents = "none";
	}
}

function doSubmit()
{
	var a = confirm("Do you want to Submit?")
	if(a)
	{
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].submit();
	}
}

function doArchive(tbl_nm,retenstion_dt,archiveLogic)
{
	var a = confirm("Do you want to Archive "+tbl_nm+"?")
	if(a)
	{
		document.forms[0].archive_tbl_nm.value=tbl_nm;
		document.forms[0].retention_dt.value=retenstion_dt;
		document.forms[0].archiveLogic.value=archiveLogic;
		document.forms[0].option.value="ARCHIVE_TBL"
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].submit();
	}
}

function doDeleteArchiveTbl(tbl_nm)
{
	var a = confirm("Do you want to Archived Table "+tbl_nm+"?")
	if(a)
	{
		document.forms[0].archived_tbl_nm.value=tbl_nm;
		document.forms[0].option.value="DLETEL_ARCHIVED_TBL"
		document.getElementById("loading").style.visibility = "visible";
		document.forms[0].submit();
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.developer_tool.DB_DeveloperTool" id="dev_tool" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
dev_tool.setCallFlag("GRANT_TABLE");
dev_tool.init();

Vector VTABLE_NM = dev_tool.getVTABLE_NM();
Vector VROW = dev_tool.getVROW();
Vector VARCHIVE_FLAG = dev_tool.getVARCHIVE_FLAG();
Vector VARCHIVE_LOGIC = dev_tool.getVARCHIVE_LOGIC();
Vector VARCHIVE_LOGIC_VALUE = dev_tool.getVARCHIVE_LOGIC_VALUE();
Vector VARCHIVABLE_ROW = dev_tool.getVARCHIVABLE_ROW();
Vector VRETENTION_DT = dev_tool.getVRETENTION_DT();
Vector VARCHIVED_TABLE_NAME = dev_tool.getVARCHIVED_TABLE_NAME();
Vector VARCHIVED_ROW = dev_tool.getVARCHIVED_ROW();

String temp_path = dev_tool.getTemp_path();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_DeveloperTool">

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
					    	Dispose DB Data
					    </div>
					</div>
				</div>				
				<div class="card-body cdbody">
					<!-- <div class="accordion">
						<div class="accordion-item accor_item">
							<h2 class="accordion-header" id="heading">
								<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse" aria-expanded="false" aria-controls="collapse">
					    		Database Table List
					    		</button>
					    	</h2>
					    	<div id="collapse" class="accordion-collapse collapse" aria-labelledby="heading">
					      		<div class="accordion-body accor-body"> -->					
									<div class="row">
										<div class="table-responsive">
											<table class="table table-bordered table-hover" id="example">
												<thead id="tbsearch">
													<tr>
														<th>Sr#</th>
														<th>DataBase Table</th>
														<th>Retention Date</th>
														<th>#Rows</th>
														<th>Archive Data</th>
														<th>Archival Logic</th>
														<th>#Archivable Rows</th>																												
														<th>Archive</th>
														<th>Archived DataBase Table</th>
														<th>#Archived Rows</th>
														<th>Delete</th>
													</tr>
												</thead>
												<tbody>
													<%for(int i=0; i<VTABLE_NM.size(); i++){ %>
													<tr>
														<td>
															<div align="center">
																<input type="checkbox" class="form-check-input" name="chk" id="chk<%=i%>" onclick="enable_disable(this,'<%=i%>');">
															</div>
														</td>
														<td align="left">
															<%=VTABLE_NM.elementAt(i)%>
															<input type="hidden" name="tbl_nm" id="tbl_nm<%=i%>" value="<%=VTABLE_NM.elementAt(i)%>" disabled>
														</td>
														<td align="center"><%=VRETENTION_DT.elementAt(i)%></td>
														<td align="right"><%=VROW.elementAt(i)%></td>
														<td>
															<div class="form-check form-switch">
																<input class="form-check-input" name="archive_active" type="checkbox" role="switch" id="archive_active<%=i%>" <%if(VARCHIVE_FLAG.elementAt(i).equals("Y")){%>checked<%}%> onclick="setActiveInactive(this,'<%=i%>', 'archive_lb', 'archive_flag')" disabled>
															  	<label class="form-check-label" for="archive_active<%=i%>" id="archive_lb<%=i%>">
															  		<%if(VARCHIVE_FLAG.elementAt(i).equals("Y")){%>Yes<%}else{%>No<%} %>
															  	</label>
															  	<input type="hidden" name="archive_flag" id="archive_flag<%=i%>" value="<%=VARCHIVE_FLAG.elementAt(i)%>" disabled>
															</div>
														</td>
														<td align="center">
															<select class="form-select form-select-sm" name="archival_logic" id="archival_logic<%=i%>" style="pointer-events: none;" disabled>
					      										<option value="">--Select--</option>
					      										<%for(int j=0; j<((Vector)(VARCHIVE_LOGIC.elementAt(i))).size(); j++) { %>
					      										<option value="<%=((Vector)(VARCHIVE_LOGIC.elementAt(i))).elementAt(j)%>"><%=((Vector)(VARCHIVE_LOGIC.elementAt(i))).elementAt(j)%></option>
					      										<%} %>			    
					      									</select>
					      									<script>
					      									document.getElementById("archival_logic<%=i%>").value='<%=VARCHIVE_LOGIC_VALUE.elementAt(i)%>'
					      									</script>
														</td>
														<td align="right"><%=VARCHIVABLE_ROW.elementAt(i) %></td>
														<td>
															<div align="center">
															<i class="fa fa-archive fa-lg"
																<%if(Integer.parseInt(""+VARCHIVABLE_ROW.elementAt(i)) > 0  && VARCHIVED_TABLE_NAME.elementAt(i).equals("")) {%> 
																	style="color: blue"
																	onclick="doArchive('<%=VTABLE_NM.elementAt(i)%>','<%=VRETENTION_DT.elementAt(i)%>','<%=VARCHIVE_LOGIC_VALUE.elementAt(i)%>')"
																<%}else {%>
																	style="color: grey"
																<%} %>
															></i>	
															</div>
														</td>
														<td><%=VARCHIVED_TABLE_NAME.elementAt(i)%></td>
														<td align="right"><%=VARCHIVED_ROW.elementAt(i) %></td>
														<td>
															<div align="center">
																<i class="fa fa-trash-o fa-lg" 
																	<%if(Integer.parseInt(""+VARCHIVED_ROW.elementAt(i)) > 0) {%>
																		style="color:red;"
																		onclick="doDeleteArchiveTbl('<%=VARCHIVED_TABLE_NAME.elementAt(i)%>')"
																	<%}else{ %>
																		style="color:grey;"
																	<%} %>
																></i>
															</div>
														</td>
													</tr>
													<%} %>
												</tbody>
											</table>
										</div>
									</div>
								<!-- </div>
							</div>
						</div>
					</div> -->
				</div>	
				<div class="card-footer cdfooter text-center">
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
</div>

<input type="hidden" name="option" value="DATA_DISPOSAL">

<input type="hidden" name="archive_tbl_nm" value="">
<input type="hidden" name="retention_dt" value="">
<input type="hidden" name="archiveLogic" value="">
<input type="hidden" name="archived_tbl_nm" value="">

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
		if(title == "DataBase Table" || title == "Archive Data")
		{
			$(this).html(title+'<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:150px"/></div>');
		}
		/* else
		{
			$(this).html(title+'<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:150px"/></div>');
		} */
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