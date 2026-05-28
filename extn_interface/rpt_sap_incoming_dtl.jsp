
<%@page import="java.util.*"%>
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
	
	var url = "rpt_sap_incoming_dtl.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var url = "xls_sap_incoming_dtl.jsp?fileName=SAP Incoming Details.xls&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sap_interface" id="dbmgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdt = utildate.getSysdate();
String from_dt=request.getParameter("from_dt")==null?sysdt:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdt:request.getParameter("to_dt");

dbmgmt.setCallFlag("SAP_INCOMING_DTL");
dbmgmt.setComp_cd(owner_cd);
dbmgmt.setFrom_dt(from_dt);
dbmgmt.setTo_dt(to_dt);
dbmgmt.init();


Vector VBUKRS = dbmgmt.getVBUKRS();
Vector VKUNNR = dbmgmt.getVKUNNR();
Vector VBELNR = dbmgmt.getVBELNR();
Vector VGJAHR = dbmgmt.getVGJAHR();
Vector VBLDAT = dbmgmt.getVBLDAT();
Vector VSGTXT = dbmgmt.getVSGTXT();
Vector VSHKZG = dbmgmt.getVSHKZG();
Vector VWRBTR = dbmgmt.getVWRBTR();
Vector VWAERS = dbmgmt.getVWAERS();
Vector VDMBTR = dbmgmt.getVDMBTR();
Vector VHWAER = dbmgmt.getVHWAER();
Vector VZUKEY = dbmgmt.getVZUKEY();
Vector VZDTYP = dbmgmt.getVZDTYP();
Vector VHKONT = dbmgmt.getVHKONT();
Vector VAUGBL = dbmgmt.getVAUGBL();
Vector VXBLNR = dbmgmt.getVXBLNR();
Vector VRSTGR = dbmgmt.getVRSTGR();
Vector VAUGDT = dbmgmt.getVAUGDT();
Vector VBLART = dbmgmt.getVBLART();
Vector VZUONR = dbmgmt.getVZUONR();
Vector VNAME = dbmgmt.getVNAME();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="d-flex justify-content-between">
						<div class="topheader">
				    		SAP Incoming Details
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-5 col-xs-5 col-md-5"></div>
						<div class="col-auto">
							<div class="form-group row">
								<label class="form-label"><b>From</b></label>
							</div>
						</div>
						<div class="col-auto">
		      				<div class="input-group input-group-sm">
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
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered" id="example">
								<thead id="tbsearch">
									<tr>
										<th align="center">Sr#</th>
										<th align="center">Company Code <br>(BUKRS)</th><!-- BUKRS -->
										<th align="center">Customer Code<br>(KUNNR)</th><!-- KUNNR -->
										<th align="center">Customer Name<br>(NAME)</th><!-- NAME -->
										<th align="center">Document Number<br>(BELNR)</th><!-- BELNR -->
										<th align="center">Fiscal Year<br>(GJAHR)</th><!-- GJAHR -->
										<th align="center">Document Date<br>(BLDAT)</th><!-- BLDAT -->
										<th align="center">Text<br>(SGTXT)</th><!-- SGTXT -->
										<th align="center">Debit/Credit<br>(SHKZG)</th><!-- SHKZG -->
										<th align="center">Amount<br>(WRBTR)</th><!-- WRBTR -->
										<th align="center">Currency<br>(WAERS)</th><!-- WAERS -->
										<th align="center">Amount in Local Currency<br>(DMBTR)</th><!-- DMBTR -->
										<th align="center">Local Currency<br>(HWAER)</th><!-- HWAER -->
										<th align="center">Unique Character combination<br>(ZUKEY)</th><!-- ZUKEY -->
										<th align="center">Type of Transaction<br>(ZDTYP)</th><!-- ZDTYP -->
										<th align="center">General Ledger<br>(HKONT)</th><!-- HKONT -->
										<th align="center">Clearing Document<br>(AUGBL)</th><!-- AUGBL -->
										<th align="center">Reference<br>(XBLNR)</th><!-- XBLNR -->
										<th align="center">Reason Code<br>(RSTGR)</th><!-- RSTGR -->
										<th align="center">Clearing Document Date<br>(AUGDT)</th><!-- AUGDT -->
										<th align="center">Document Type<br>(BLART)</th><!-- BLART  -->
										<th align="center">Assignment Number<br>(ZUONR)</th><!-- ZUONR -->
									</tr>
								</thead>
								<tbody>
									<%if(VBUKRS.size()>0){ %>
									<%for(int i=0;i<VBUKRS.size();i++){ %>
									<tr>
										<td align="center"><%=i+1%></td>
										<td align="center"><%=VBUKRS.elementAt(i)%></td>
										<td align="center"><%=VKUNNR.elementAt(i)%></td>
										<td align="center"><%=VNAME.elementAt(i)%></td>
										<td align="center"><%=VBELNR.elementAt(i)%></td>
										<td align="center"><%=VGJAHR.elementAt(i)%></td>
										<td align="center"><%=VBLDAT.elementAt(i)%></td>
										<td align="center"><%=VSGTXT.elementAt(i)%></td>
										<td align="center"><%=VSHKZG.elementAt(i)%></td>
										<td align="right"><%=VWRBTR.elementAt(i)%></td>
										<td align="center"><%=VWAERS.elementAt(i)%></td>
										<td align="right"><%=VDMBTR.elementAt(i)%></td>
										<td align="center"><%=VHWAER.elementAt(i)%></td>
										<td align="center"><%=VZUKEY.elementAt(i)%></td>
										<td align="center"><%=VZDTYP.elementAt(i)%></td>
										<td align="center"><%=VHKONT.elementAt(i)%></td>
										<td align="center"><%=VAUGBL.elementAt(i)%></td>
										<td align="center"><%=VXBLNR.elementAt(i)%></td>
										<td align="center"><%=VRSTGR.elementAt(i)%></td>
										<td align="center"><%=VAUGDT.elementAt(i)%></td>
										<td align="center"><%=VBLART.elementAt(i)%></td>
										<td align="center"><%=VZUONR.elementAt(i)%></td>
									</tr>
									<%} %>
									<%}else{ %>
									<tr>
										<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No SAP incoming deatils Available!</b>") %></td>
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
$(document).ready(function() {
	
	$('#tbsearch th').each(function(i){
		//alert(i)
		var title = $(this).text();
		if(title == "Sr#")
		{
			$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+'" onkeyup="Search(this,'+i+');" placeholder="Search '+title+'" style="width:40px"/></div>');
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
</body>
</html>
