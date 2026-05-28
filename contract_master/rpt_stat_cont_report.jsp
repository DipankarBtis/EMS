
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var agmtType = document.forms[0].agmtType.value;
	var prev_agmtType = document.forms[0].prev_agmtType.value;
	var counterparty_cd = document.forms[0].counterparty_cd;
	var u = document.forms[0].u.value;
	
	if(prev_agmtType!=agmtType)
	{
		counterparty_cd.value="0";
	}
	
	var url = "rpt_stat_cont_report.jsp?u="+u+"&agmtType="+agmtType+"&counterparty_cd="+counterparty_cd.value;
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var agmtType = document.forms[0].agmtType.value;
	var counterparty_cd = document.forms[0].counterparty_cd;
	var comp_abbr = document.forms[0].comp_abbr.value;
	var sysdate = document.forms[0].sysdate.value;
	
	var fileName = comp_abbr+" Statistical Report of all contract "+sysdate+".xls";
	var url = "xls_stat_cont_report.jsp?agmtType="+agmtType+"&counterparty_cd="+counterparty_cd.value+"&fileName="+fileName;
	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_master.DB_ContractMaster_Report" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String agmtType = request.getParameter("agmtType")==null?"0":request.getParameter("agmtType");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

contract.setCallFlag("ALL_CONT_STAT_REPORT");
contract.setComp_cd(owner_cd);
contract.setAgmtType(agmtType);
contract.setCounterparty_cd(counterparty_cd);
contract.init();

String comp_abbr = contract.getComp_abbr();

Vector VMST_COUNTERPARTY_CD = contract.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = contract.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = contract.getVMST_COUNTERPARTY_NM();

Vector VAGMT_NO = contract.getVAGMT_NO();
Vector VAGMT_REV = contract.getVAGMT_REV_NO();
Vector VAGMT_DISP_MAP = contract.getVAGMT_DISP_MAP();
Vector VAGMT_START_DT = contract.getVAGMT_START_DT();
Vector VAGMT_END_DT = contract.getVAGMT_END_DT();
Vector VEXPIRY_STATUS = contract.getVEXPIRY_STATUS();

Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONT_REV_NO = contract.getVCONT_REV_NO();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VSIGNING_DT = contract.getVSIGNING_DT();
Vector VSTART_DT = contract.getVSTART_DT();
Vector VEND_DT = contract.getVEND_DT();
Vector VCONT_STATUS_FLG = contract.getVCONT_STATUS_FLG();
Vector VCONT_STATUS = contract.getVCONT_STATUS();
Vector VDIS_CONT_MAPPING = contract.getVDIS_CONT_MAPPING();
Vector VINDEX = contract.getVINDEX();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			}%>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
				    		Statistical Report of All Contracts
	   	 				</div>
	   	 				<a>
							<span class="input-group-text">
							 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
							</span>
						</a>
				    </div>
				</div> 
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Agreement</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name=agmtType onchange="refresh()">
										<option value="0">--Select--</option>
										<option value="F">FGSA</option>
										<option value="T">LOA</option>
										<option value="A">LTCORA</option>
										<%-- <%for(int i=0;i<VSEGMENT.size();i++){ %>
										<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
										<%} %> --%>
									</select>
									<script>document.forms[0].agmtType.value="<%=agmtType%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%if(!agmtType.equals("0")){%>
						<%if(VAGMT_NO.size()>0){%>
							<%int j=0,k=0;
							for(int i=0;i<VAGMT_NO.size();i++){
								int index = Integer.parseInt(""+VINDEX.elementAt(i));
							%>
								<div class="row">
								 	<div class="col-md-12 col-sm-12 col-xs-12">
								 		<div class="accordion">
								 			<div class="accordion-item accor_item">
								 				<h2 class="accordion-header" id="heading1">
													<button name="sub_module_cd" class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=i%>" aria-expanded="false" aria-controls="collapse<%=i%>"><%=VAGMT_DISP_MAP.elementAt(i)%>&nbsp;&nbsp;<%if(!agmtType.equals("T")){%>(Rev no:<%=VAGMT_REV.elementAt(i)%>)<%} %>&nbsp;&nbsp;&nbsp;[<%=VAGMT_START_DT.elementAt(i)%>-<%=VAGMT_END_DT.elementAt(i)%>]</button>	
										    	</h2>
										    	<div id="collapse<%=i%>" class="accordion-collapse collapse" aria-labelledby="heading1">
										    		<div class="accordion-body accor-body">
										    			<div class="row">
										    				<div class="table-responsive">
										    					<table class="table table-bordered table-hover" id="example<%=i%>">
										    						<thead id="tbsearch<%=i%>">
										    							<tr>
										    								<th>Sr#</th>
										    								<th>Contract#</th>
										    								<th>Contract Rev. No.</th>
										    								<th>Signing Date</th>
										    								<th>Start Date</th>
										    								<th>End Date</th>
										    								<th>Status</th>
										    							</tr>
										    						</thead>
										    						<tbody>
										    							<%k=0;
										    							if(index>0){
										    							%>
										    								<%for(j=j;j<VCONT_NO.size();j++){
										    								k+=1;%>
												    							<tr>
												    								<td align="center"><%=k%></td>
												    								<td align="center"><%=VDIS_CONT_MAPPING.elementAt(j) %></td>
												    								<td align="center"><%=VCONT_REV_NO.elementAt(j) %></td>
												    								<td align="center"><%=VSIGNING_DT.elementAt(j) %></td>
												    								<td align="center"><%=VSTART_DT.elementAt(j) %></td>
												    								<td align="center"><%=VEND_DT.elementAt(j) %></td>
												    								<td align="center"><%=VCONT_STATUS.elementAt(j) %></td>
												    							</tr>
										    									<%if(k==index){
										    										j=j+1;
																					break;
										    									}%>
										    								<%} %>
										    							<%}else{%>
										    								<tr>
											    								<td colspan="7" align="center">
											    									<%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %>
										    									</td>
										    								</tr>
										    							<%}%>
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
							<%} %>
						<%}else{%>
							 <div class="row">
							 	<div class="col-md-12 col-sm-12 col-xs-12">
							 		<div align="center">
							 			<%=utilmsg.infoMessage("<b>No Agreement is Available!</b>") %>
							 		</div>
							 	</div>
							 </div>
						<%} %>
					<%}else{%>
							 <div class="row">
							 	<div class="col-md-12 col-sm-12 col-xs-12">
							 		<div align="center">
							 			<%=utilmsg.infoMessage("<b>Please select any Agreement!</b>") %>
							 		</div>
							 	</div>
							 </div>
					<%} %>
				</div>						
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="prev_agmtType" value="<%=agmtType%>">
<input type="hidden" name="comp_abbr" value="<%=comp_abbr%>">
<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="acc_size" value="<%=VAGMT_NO.size()%>">


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
	var acc_size=document.forms[0].acc_size.value; 
	for(k=0;k<acc_size;k++)
	{
		$('#tbsearch'+k+' th').each(function(i){
			//alert(i)
			var title = $(this).text();
			if(title == "Sr#")
			{
				//$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
			}
			else
			{
				$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'_'+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:100px"/></div>');
			}
		});
	}
	
});

function Search(obj, indx,k) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+k);
  	
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