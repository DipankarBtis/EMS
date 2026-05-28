<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Vector"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
/* function refresh()
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
				var url = "../extn_interface/frm_oth_inv_sun_xml_approval.jsp?u="+u+
						"&from_dt="+from_dt+"&to_dt="+to_dt;
			
				document.getElementById("loading").style.visibility = "visible";
				location.replace(url);
			}
		}
	}
		
} */

function generateSunXML(app_flag)
{
	var account_code = document.forms[0].account_code;
	var flag=true;
	var msg="";
	if(app_flag=="A")
	{
		document.forms[0].option.value="SUN_XML";
	}
	else if(app_flag=="R")
	{
		document.forms[0].option.value="RE_APP_SUN_XML";
	}
	
	if(account_code!=null || account_code!=undefined)
	{
		if(account_code.length!=undefined)
		{
			for(var i=0;i<account_code.length;i++)
			{
				if(account_code[i].value=='' || account_code[i].value==null)
				{
					msg="Configure Account Code!\n";
					flag=false;
					break;
				}
			}
		}
		else
		{
			if(account_code.value=='')
			{
				msg="Configure Account Code!\n";
				flag=false;
			}
		}
	}
	
	if(flag==true)
	{
		var a=confirm("On your Approval SUN XML will be generated!\n\nDo you want to Proceed and Generate SUN XML?");
		if(a)
		{
			//alert("On your Approval SUN XML will be generated!");
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}
function Do_close(accroid,msg,msg_type)
{
	window.opener.refershPar(accroid,msg,msg_type);
	window.close();
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_oth_inv_sun_interface" id="accounting" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();

String invoice_no=request.getParameter("invoice_no")==null?sysdate:request.getParameter("invoice_no");
String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
String sun_flag =request.getParameter("sun_flag")==null?"":request.getParameter("sun_flag");
String operation =request.getParameter("operation")==null?"":request.getParameter("operation");
String inv_type =request.getParameter("inv_type")==null?"":request.getParameter("inv_type");

String workDir=CommonVariable.work_dir+owner_cd;
String sunxml_dir=CommonVariable.sun_xml;
String filePath = request.getServletContext().getRealPath(workDir+"/"+sunxml_dir+"/");
String appPath=workDir+"/"+sunxml_dir+"/";

if(sun_flag.equals("Y"))
{	
	accounting.setCallFlag("PARSE_SUN_XML");
}
else 
{
	accounting.setCallFlag("OTH_INV_SUN_XML_GEN");
}
accounting.setComp_cd(owner_cd);
accounting.setInvoice_no(invoice_no);
accounting.setInv_type(inv_type);
accounting.setFile_path(filePath);
accounting.init();

Vector VINVOICE_NO=accounting.getVINVOICE_NO();
Vector VJOURNAL_TYPE=accounting.getVJOURNAL_TYPE();
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
Vector VTRANS_AMT= accounting.getVTRANS_AMT();
Vector VACCOUNT_TYPE_NM= accounting.getVACCOUNT_TYPE_NM();


String sun_appr_by = accounting.getSunApprovedBy();
String sun_appr_dt = accounting.getSunApprovedDt();
%>
<body onload="<%if(!msg.equals("")){ %>Do_close('<%=accroid%>','<%=msg%>','<%=msg_type%>');<%} %>">
<%@ include file="../home/loading.jsp"%>
<form method="post" action="../servlet/Frm_oth_inv_sun_interface">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%-- <%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			} %> --%>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Other Invoice SUN XML Generation
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
										<th>Account Code Description</th>
										<th>Account Period</th>
										<th>Transaction Date</th>
										<th>Journal Type</th>
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
									<%if(VINVOICE_NO.size()>0){ %>
										<%for(int i=0; i<VINVOICE_NO.size();i++){%>
											<tr>
												<td align="center">
													<input type="hidden" name="account_code" value="<%=VACCOUNT_CD.elementAt(i) %>">
													<%=VACCOUNT_CD.elementAt(i) %>
												</td>
												<td align="center"><%=VACCOUNT_TYPE_NM.elementAt(i)%></td>
												<td align="center"><%=VPERIOD_START_DT.elementAt(i)%></td>
												<td align="center"><%=VAPPROVAL_DT.elementAt(i) %></td>
												<td align="center"><%=VJOURNAL_TYPE.elementAt(i) %></td>
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
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter d-flex justify-content-center align-items-center">
					 <%if(sun_flag.equals("Y")) {%>
						  <div class="d-flex justify-content-end">
					 	 	Approved By : <%=sun_appr_by%><br>&nbsp;Approved On : <%=sun_appr_dt%>
						 </div>	
					<%} else {%>
						 <div >
							<input type="button" class="btn btn-warning com-btn" value="Approve" onclick="generateSunXML('A');">
						 </div>
					 <%} %>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="">

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
<input type="hidden" name="invoice_no" value="<%=invoice_no%>">
<input type="hidden" name="accroid" value="<%=accroid%>">
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
