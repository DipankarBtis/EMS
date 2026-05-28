<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page import="java.util.*" %>
<%@ include file="../util/common_js.jsp"%>
<script>
function nextDate(day_no)
{
	var dt = document.forms[0].report_dt.value;
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
		
		document.forms[0].report_dt.value=to_dt;
		
		refresh();
	}
}

function refresh()
{
	var report_dt=document.forms[0].report_dt;
	var u=document.forms[0].u.value;
	var sysdate = document.forms[0].sysdate.value;
	var prev_report_dt = document.forms[0].prev_report_dt.value;
	
	var tmp=report_dt.value.split("/");
	var tmp1=sysdate.split("/");
	var temp_report_dt = new Date(tmp[1]+"/"+tmp[0]+"/"+tmp[2]);
 	var temp_sysdate = new Date(tmp1[1]+"/"+tmp1[0]+"/"+tmp1[2]);
	
	var flag=true;
	if(temp_report_dt>temp_sysdate)
	{
		flag=false;
	}
	
	if(flag==true)
	{
		var url="rpt_unsetteled.jsp?report_dt="+report_dt.value+"&u="+u;
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert("Report Date can't exceed System Date ("+sysdate+")!");
		setTimeout(function () {
	        report_dt.value = prev_report_dt;
		}, 0);
		return false;
	}
}

function exportToXls()
{
	var report_dt=document.forms[0].report_dt.value;
	var sysdate=document.forms[0].sysdate.value;
	var sysdate1=sysdate.replace('/','_');
	
	var fileName="Unsettle_Report_"+sysdate1+".xls";
	var url="xls_unsetteled.jsp?report_dt="+report_dt+"&fileName="+fileName;
	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_Unsettled_Report" id="unset_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String report_dt = request.getParameter("report_dt")==null?sysdate:request.getParameter("report_dt");
unset_rpt.setCallFlag("UNSETTELED_RPT");
unset_rpt.setReport_dt(report_dt);
unset_rpt.init();

String cutoff_dt = unset_rpt.getCutoff_dt();
String info = unset_rpt.getInfo();

Vector VMST_COMPANY_CD = unset_rpt.getVMST_COMPANY_CD();
Vector VMST_COMPANY_ABBR = unset_rpt.getVMST_COMPANY_ABBR();
Vector VMST_COMPANY_NM = unset_rpt.getVMST_COMPANY_NM();

Vector VCOMPANY_CD = unset_rpt.getVCOMPANY_CD();
Vector VCOMPANY_ABBR = unset_rpt.getVCOMPANY_ABBR();
Vector VCOMPANY_NM = unset_rpt.getVCOMPANY_NM();
Vector VCOMP_INDEX = unset_rpt.getVCOMP_INDEX();
Vector VCOUNTERPARTY_CD = unset_rpt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = unset_rpt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = unset_rpt.getVCOUNTERPARTY_NM();
Vector VAGMT_NO = unset_rpt.getVAGMT_NO();
Vector VAGMT_REV = unset_rpt.getVAGMT_REV();
Vector VCONT_NO = unset_rpt.getVCONT_NO();
Vector VCONT_REV = unset_rpt.getVCONT_REV();
Vector VCONTRACT_TYPE = unset_rpt.getVCONTRACT_TYPE();
Vector VDISPLAY_DEAL_MAP = unset_rpt.getVDISPLAY_DEAL_MAP();
Vector VCONT_REF = unset_rpt.getVCONT_REF();
Vector VINVOICE_TYPE = unset_rpt.getVINVOICE_TYPE();
Vector VBILLING_FREQ_NM = unset_rpt.getVBILLING_FREQ_NM();
Vector VPERIOD_START_DT = unset_rpt.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = unset_rpt.getVPERIOD_END_DT();
Vector VINVOICE_DT = unset_rpt.getVINVOICE_DT();
Vector VACC_COLOR = unset_rpt.getVACC_COLOR();
Vector VINVOICE_DUE_DT = unset_rpt.getVINVOICE_DUE_DT();
Vector VSETTELED_AMT = unset_rpt.getVSETTELED_AMT();
Vector VSETTELED_CURR = unset_rpt.getVSETTELED_CURR();
Vector VDEAL_TYPE = unset_rpt.getVDEAL_TYPE();
Vector VCONT_START_DT = unset_rpt.getVCONT_START_DT();
Vector VCONT_END_DT = unset_rpt.getVCONT_END_DT();
Vector VPLANT_SEQ_NO = unset_rpt.getVPLANT_SEQ_NO();
Vector VBU_SEQ_NO = unset_rpt.getVBU_SEQ_NO();
Vector VPLANT_ABBR = unset_rpt.getVPLANT_ABBR();
Vector VBU_ABBR = unset_rpt.getVBU_ABBR();
Vector VDOC_STATUS = unset_rpt.getVDOC_STATUS();
Vector VOPS_FLAG = unset_rpt.getVOPS_FLAG();
Vector VCARGO_NO = unset_rpt.getVCARGO_NO();
Vector VINV_FLAG = unset_rpt.getVINV_FLAG();
Vector VSPLIT_FLAG = unset_rpt.getVSPLIT_FLAG();
Vector VSPLIT_VAL = unset_rpt.getVSPLIT_VAL();
Vector VBOE_NO = unset_rpt.getVBOE_NO();
Vector VBOE_NM = unset_rpt.getVBOE_NM();
Vector VREPORT_DT = unset_rpt.getVREPORT_DT();
Vector VNEXT_STEP = unset_rpt.getVNEXT_STEP();
Vector VINVOICE_NO = unset_rpt.getVINVOICE_NO();
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
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Un-Settled Report
					    </div>
					    <div class="col-auto">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x" onclick="exportToXls();" ></i></span>
						</div>
					</div>
				</div>
				<!-- Applicable Filters -->
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-10 col-xs-10 col-md-10">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>Report Date</b></label>
									</div>
									<div class="col-auto">
										<div class="input-group input-group-sm" >
					      					<span class="input-group-text" onclick="nextDate('-1');" title="click for Back Date"><i class="fa fa-backward fa-lg"></i></span>
						      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="report_dt" id="report_dt" value="<%=report_dt%>" maxLength="10" 
						      				onchange="validateDate(this);refresh();">
						      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      					<span class="input-group-text" onclick="nextDate('1');" title="click for Next Date"><i class="fa fa-forward fa-lg"></i></span>
					      				</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
	    					<div class="col-md-12 col-sm-12 col-xs-12">
		    					<div align="right">
		    						<input class="form-control form-control-sm" type="text" id="globalSearch" onkeyup="globalSearchTable(this,0)" placeholder="Search.." style="width:200px"/>
		    					</div>
	    					</div>
						<div class="col-md-12 col-sm-12 col-xs-12">
	    					<div class="table-responsive">
								<table class="table table-bordered ems_sorttbl" id="ems_table0">
									<thead id="ems_tbsort0">
										<tr>
											<th>Sr#</th>
											<th class="ems_thsort0">Report Date</th>
											<th class="ems_thsort0">Legal Entity</th>
											<th class="ems_thsort0">Business Unit</th>
											<th class="ems_thsort0">Counterparty</th>
											<th class="ems_thsort0">Plant</th>
											<th class="ems_thsort0">Contract#</th>
											<th class="ems_thsort0">Contract Ref/Trade Ref</th>
											<th class="ems_thsort0">Invoice#</th>
											<th class="ems_thsort0">Doc Status</th>
											<th class="ems_thsort0">Next Step</th>
											<th class="ems_thsort0">COM OPS Approved</th>
											<th class="ems_thsort0">Invoice Type</th>
											<th class="ems_thsort0">Billing Period</th>
											<th class="ems_thsort0">Deal Type</th>
											<th class="ems_thsort0">Settle Amount</th>
											<th class="ems_thsort0">Currency</th>
											<th class="ems_thsort0">Invoice Date</th>
											<th class="ems_thsort0">Payment Due Date</th>
											<th class="ems_thsort0">Billing Cycle</th>
											<!-- <th>OPS Approved</th> -->
											<th class="ems_thsort0">Contract Period</th>
										</tr>
									</thead>
									<tbody>
									<% if(VCOUNTERPARTY_CD.size() > 0){ %>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){%>
										<tr>
											<td align="center"><%=i+1 %></td>
											<%-- <td align="center"><%=report_dt%></td> --%>
											<td align="center"><%=VREPORT_DT.elementAt(i)%></td>
											<td align="center" title="<%=VCOMPANY_NM.elementAt(i)%>"><%=VCOMPANY_ABBR.elementAt(i) %></td> 
											<td align="center"><%=VBU_ABBR.elementAt(i)%></td>
											<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i) %></td>
											<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
											<td align="center">
												<%=VDISPLAY_DEAL_MAP.elementAt(i) %>
												<%if(VSPLIT_FLAG.elementAt(i).equals("Y")){ %>
														<font style="background:#ff99ff;">[Split <%=VSPLIT_VAL.elementAt(i)%>%]</font>
												<%} %>	
												<%if(!VBOE_NO.elementAt(i).equals("0")){ %>
													<font style="background:#ff99ff;"><%=VBOE_NM.elementAt(i)%></font>
													<%if(VINV_FLAG.elementAt(i).equals("F")){ %>
														<font style="background:#ccff99;">Final</font>
													<%} %>
												<%} %>	
											</td>
											<td align="center"><%=VCONT_REF.elementAt(i) %></td>
											<td align="center"><%=VINVOICE_NO.elementAt(i) %></td>
											<td align="center"><%=VDOC_STATUS.elementAt(i) %></td>
											<td align="center"><%=VNEXT_STEP.elementAt(i) %></td>
											<td align="center"><%=VOPS_FLAG.elementAt(i) %></td>
											<td align="center"><%=VINVOICE_TYPE.elementAt(i) %></td>
											<td align="center"><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></td>
											<td align="center"><%=VDEAL_TYPE.elementAt(i) %></td>
											<td align="right"><%=VSETTELED_AMT.elementAt(i) %></td>
											<td align="center"><%=VSETTELED_CURR.elementAt(i) %></td>
											<td align="center" <%if(VACC_COLOR.elementAt(i).toString().equals("R")){%>style="color:red;"<%} %>><%=VINVOICE_DT.elementAt(i) %></td>
											<td align="center" <%if(VACC_COLOR.elementAt(i).toString().equals("R")){%>style="color:red;"<%} %>><%=VINVOICE_DUE_DT.elementAt(i) %></td>
											<td align="center">
												<span 
		    									<%if(VBILLING_FREQ_NM.elementAt(i).equals("1st-Fortnight")){ %>
		    										class="alert alert-info"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("2nd-Fortnight")){ %>
		    										class="alert alert-warning"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("1st-Weekly")){ %>
		    										class="alert" style="background:#eeccff;color: #660099;"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("2nd-Weekly")){ %>
		    										class="alert alert-dark"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("3rd-Weekly")){ %>
		    										class="alert alert-success"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("4th-Weekly")){ %>
		    										class="alert alert-danger"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("5th-Weekly")){ %>
		    										class="alert" style="background:#e6ccff;color:#330066;"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Monthly")){ %>
		    										class="alert alert-primary"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Other")){ %>
		    										class="alert" style="background:#b3ffb3;color: #008000;"
		    									<%}else if(VBILLING_FREQ_NM.elementAt(i).equals("Delivery Period")){ %>
		    										class="alert" style="background:#ff80bf;color:#33001a;"	
		    									<%} %>
		    									><b><%=VBILLING_FREQ_NM.elementAt(i)%></b></span>																								
											</td>																		
											<td align="center"><%=VCONT_START_DT.elementAt(i)%>-<%=VCONT_END_DT.elementAt(i)%></td>
										</tr>
									<%}%>
									<%}else{ %>
										<tr>
											<td colspan="21" align="center"><%=utilmsg.infoMessage("<b>No Un-Setteled data found!</b>") %></td>
										</tr>
									<%} %> 
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="left"><%=utilmsg.infoMessage(info) %></div>
   					</div>					
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

<input type="hidden" name="sysdate" value="<%=sysdate%>">
<input type="hidden" name="prev_report_dt" value="<%=report_dt%>">
<input type="hidden" name="acc_size" value="<%=VCOMP_INDEX.size()%>">

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