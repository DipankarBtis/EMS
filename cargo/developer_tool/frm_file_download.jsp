<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh(index)
{
	var path = document.getElementById("filePath"+index).value;
	var dwn_folder = document.forms[0].dwn_folder.value;
	
	var u = document.forms[0].u.value;
	
	if(path!="")
	{
		var url = "frm_file_download.jsp?&u="+u+"&path="+path+"&dwn_folder="+dwn_folder;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert("File Path Missing!")
	}
}

function refresh1()
{
	var path = document.forms[0].path.value;
	var dwn_folder = document.forms[0].dwn_folder.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_file_download.jsp?&u="+u+"&dwn_folder="+dwn_folder;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function downloadFile(index)
{
	var file_name = document.getElementById("fileName"+index).value;
	var dwn_path = document.forms[0].dwn_path.value;
	var file_path = document.forms[0].file_path.value;
	
	var dwnfile_path = (dwn_path+"\\"+file_name).replace(/\\/g,"/");
	var downloadLink = document.getElementById("fileDwnIcon"+index);
	
	if(file_path!="")
	{
		document.getElementById("fileDwnIcon"+index).href = "../"+dwnfile_path;
	}
	else
	{
		document.getElementById("fileDwnIcon"+index).href = ""+file_path+file_name;
	}
	downloadLink.setAttribute("download", file_name);
}

function goBack()
{
	var path_sub_sub = document.forms[0].path_sub_sub.value;
	var dwn_folder = document.forms[0].dwn_folder.value;
	
	var u = document.forms[0].u.value;
	
	var url = "frm_file_download.jsp?path="+path_sub_sub+"&u="+u+"&dwn_folder="+dwn_folder;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.developer_tool.DB_DeveloperTool" id="dev_tool" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String path = request.getParameter("path")==null?"":request.getParameter("path");
String dwn_path = request.getParameter("dwn_path")==null?"":request.getParameter("dwn_path");
String dwn_folder = request.getParameter("dwn_folder")==null?"DRPT":request.getParameter("dwn_folder");
String path_sub_sub = request.getParameter("path_sub_sub")==null?"":request.getParameter("path_sub_sub");
String path_sub = "";
if(dwn_folder.equals("SAP"))
{
	path_sub = ""+CommonVariable.work_dir+owner_cd+"/"+"sap_xml"+"/"+path;
	
	dwn_path = CommonVariable.work_dir+owner_cd+"/"+"sap_xml"+"/"+path;
}
else if(dwn_folder.equals("ACK SAP"))
{
	path_sub = ""+CommonVariable.work_dir+owner_cd+"/"+"in_sap_xml"+"/"+path;
	
	dwn_path = CommonVariable.work_dir+owner_cd+"/"+"in_sap_xml"+"/"+path;
}
else if(dwn_folder.equals("DRPT"))
{
	path_sub = ""+CommonVariable.work_dir+owner_cd+"/"+"daily_reports"+"/"+path;
	
	dwn_path = CommonVariable.work_dir+owner_cd+"/"+"daily_reports"+"/"+path;
}
else if(dwn_folder.equals("ZEMA"))
{
	path_sub = ""+"ZEMA_XML"+"/"+path;
	
	dwn_path = "ZEMA_XML"+"/"+path;
}

String file_path = request.getRealPath(path_sub);

dev_tool.setCallFlag("FILE_DOWNLOAD");
dev_tool.setPath(file_path);
dev_tool.setPath_sub(file_path);
dev_tool.setPath_sub_sub(path);
dev_tool.init();

Vector VFOLDER = dev_tool.getVFOLDER();
Vector VUPDATE_ON = dev_tool.getVUPDATE_ON();
Vector VBYTES = dev_tool.getVBYTES();
Vector VPATH = dev_tool.getVPATH();
Vector VTYPE = dev_tool.getVTYPE();

String temp_path = dev_tool.getTemp_path();
%>
<body>
<%@ include file="../home/header.jsp"%>

<form method="post" action="../servlet/Frm_DeveloperTool" enctype="multipart/form-data">
<div class="box-body">
	<div class="row">
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Download Files
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">					
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12" align="center">
									<div class="btn-group">
										<select class="btn btn-outline-secondary subbtngrp" name="dwn_folder" onchange="refresh1();">
											<option value="SAP">SAP XML</option>
											<option value="ACK SAP">ACK SAP XML</option>
											<option value="DRPT">Daily Reports</option>
											<option value="ZEMA">ZEMA Price XML</option>											
										</select>
									</div>
									<script>document.forms[0].dwn_folder.value="<%=dwn_folder%>"</script>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">	
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead id="tbsearch">
									<tr>
										<%if(write_access.equals("Y")){ %><th></th><%} %>
										<th>Folder/Files</th>
										<th>Last Updated On</th>
										<th>Bytes</th>
									</tr>
								</thead>
								<tbody>
									<%if(VFOLDER.size()>0) {%>
									<%for(int i=0; i<VFOLDER.size(); i++){ %>
									<tr>
										<%if(write_access.equals("Y")){ %><td align="center">
										<%if(VTYPE.elementAt(i).equals("F")){ %>
											<font title="Click to Download">
												<a id="fileDwnIcon<%=i%>"><i onclick="downloadFile(<%=i%>);" class="fa fa-download fa-lg" aria-hidden="true"></i></a>
											</font>
											<input type="hidden" name="fileName" id="fileName<%=i%>" value="<%=VFOLDER.elementAt(i)%>">
										<%} %>
										</td><%} %>
										<td title="<%=VPATH.elementAt(i)%>">
											<%if(VTYPE.elementAt(i).equals("D")){ %>
											<a onclick="refresh(<%=i%>);">
												<font color="#ffcc00"><i class='fa fa-folder fa-lg'></i></font>&nbsp;<%=VFOLDER.elementAt(i)%>
											</a>
											<input type="hidden" name="filePath" id="filePath<%=i%>" value="<%=VPATH.elementAt(i)%>">
											<%}else{%>
												<i class='fa fa-file-text fa-lg'></i>&nbsp;<%=VFOLDER.elementAt(i)%>
											<%} %>
										</td>
										<td align="center"><%=VUPDATE_ON.elementAt(i)%></td>
										<td align="right"><%=VBYTES.elementAt(i)%></td>
									</tr>
									<%} %>
									<%}else{ %>
									<tr>
										<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>No File is Available!</b>")%></td>
									</tr>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-2 col-xs-2"></div>
	</div>
</div>

<input type="hidden" name="option" value="FILE_UPLOAD">
<input type="hidden" name="file_path" value="<%=file_path%>">
<input type="hidden" name="path_sub" value="">
<input type="hidden" name="path_sub_sub" value="<%=temp_path%>">
<input type="hidden" name="path" value="<%=path%>">
<input type="hidden" name="dwn_path" value="<%=dwn_path%>">

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
		if(title == "")
		{
			//$(this).html(title+'<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
		}
		else
		{
			$(this).html(title+'<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:100px"/></div>');
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