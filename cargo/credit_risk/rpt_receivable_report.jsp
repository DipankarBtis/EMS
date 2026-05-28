<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh(clearance)
{
	var rpt_dt = document.forms[0].rpt_dt.value;

	var u = document.forms[0].u.value;
	
	var url = "rpt_receivable_report.jsp?u="+u+"&rpt_dt="+rpt_dt+"&clearance="+clearance;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}
function exportToXls()
{
	var rpt_dt = document.forms[0].rpt_dt.value;
	
	var url = "xls_receivable_report.jsp?fileName=Receivable Report "+rpt_dt+".xls&rpt_dt="+rpt_dt;

	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.credit_risk.DB_CR_ReceivableReport" id="dbcredit" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String previousDate=utildate.getPreviousDate();
String rpt_dt = request.getParameter("rpt_dt")==null?previousDate:request.getParameter("rpt_dt");

dbcredit.setCallFlag("RECEIVABLE_REPORT");
dbcredit.setComp_cd(owner_cd);
//dbcredit.setRpt_dt(rpt_dt);
dbcredit.init();

Vector VCOUNTERPARTY_NM = dbcredit.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcredit.getVCOUNTERPARTY_ABBR();
Vector VCOLL_CATEGORY = dbcredit.getVCOLL_CATEGORY();
Vector VCATEGORY = dbcredit.getVCATEGORY();
Vector VBUSINESS = dbcredit.getVBUSINESS();
Vector VLEGAL_ENTITY = dbcredit.getVLEGAL_ENTITY();
Vector VDOC_NO = dbcredit.getVDOC_NO();
Vector VINVOICE_NO = dbcredit.getVINVOICE_NO();
Vector VREF_K1 = dbcredit.getVREF_K1();
Vector VREF_K2 = dbcredit.getVREF_K2();
Vector VREF_K3 = dbcredit.getVREF_K3();
Vector VDEAL_ASSIGNMENT = dbcredit.getVDEAL_ASSIGNMENT();
Vector VCONT_TYPE = dbcredit.getVCONT_TYPE();
Vector VTEXT = dbcredit.getVTEXT();
Vector VBA = dbcredit.getVBA();
Vector VNET_DUE_DT = dbcredit.getVNET_DUE_DT();
Vector VAMT_DC = dbcredit.getVAMT_DC();
Vector VAMT_INR = dbcredit.getVAMT_INR();
Vector VINV_TYPE = dbcredit.getVINV_TYPE();
Vector VDESK_NAME = dbcredit.getVDESK_NAME();
Vector VRES_COLLECTION_PRTY = dbcredit.getVRES_COLLECTION_PRTY();
Vector VRTL_GPL_TRADER = dbcredit.getVRTL_GPL_TRADER();
Vector VCLRNG_DOC = dbcredit.getVCLRNG_DOC();
Vector VCLRNG_DT = dbcredit.getVCLRNG_DT();
Vector VWBS_PNL = dbcredit.getVWBS_PNL();
Vector VBL_DT = dbcredit.getVBL_DT();
Vector VCOUNTERPARTY_CATEGORY = dbcredit.getVCOUNTERPARTY_CATEGORY();
Vector VINVOICE_TYPE = dbcredit.getVINVOICE_TYPE();
Vector VCURRANCY = dbcredit.getVCURRANCY();
Vector VAMT_USD = dbcredit.getVAMT_USD();
Vector VSTATUS = dbcredit.getVSTATUS();
Vector VOVERDUE_COZ = dbcredit.getVOVERDUE_COZ();
Vector VAGING = dbcredit.getVAGING();
Vector VARREARS_DAYS = dbcredit.getVARREARS_DAYS();
Vector VDUE_AMT_USD = dbcredit.getVDUE_AMT_USD();
Vector VDUE_AMT = dbcredit.getVDUE_AMT();
Vector VCO_CODE = dbcredit.getVCO_CODE();
Vector VCO_ABBR = dbcredit.getVCO_ABBR();

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
				    		Receivable Report 
	   	 				</div>
	   	 				<div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div align="center">
						<div class="col-sm-2 col-xs-2 col-md-2">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Report Date</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="rpt_dt" value="<%=rpt_dt%>" disabled="disabled" size="8" maxLength="10" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						<input type="hidden" name="report_dt" >
		      						</div>
		      						<script>
										document.forms[0].report_dt.value="<%=rpt_dt%>"
									</script>
				    			</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table id="search_by_filter" class="table table-bordered" id="filterbysearch">
							<thead>
								<tr>
									<th align="center">Sr#</th>
									<th align="center">CO CODE</th>
									<th align="center">CUSTOMER ABBR<br><div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_abbr" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">CUSTOMER NAME<br><div align="center"><input class="form-control form-control-sm" type="text" id="counterparty_nm" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">COLLECTIONS CATEGORY</th>
									<th align="center">CUSTOMER CATEGORY</th>
									<th align="center">TYPE OF INVOICE</th>
									<th align="center">CATEGORY</th>
									<th align="center">BUSINESS</th>
									<th align="center">LEGAL ENTITY</th>
									<th align="center">DOC.NO.</th>
									<th align="center">REF.<br><div align="center"><input class="form-control form-control-sm" type="text" id="ref_id" onkeyup="Search(this,'11');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">REF.KEY 1</th>
									<th align="center">REF. KEY 2</th>
									<th align="center">REF. KEY 3</th>
									<th align="center">ASSIGNMENT</th>
									<th align="center">TYPE</th>
									<th align="center">TEXT</th>
									<th align="center">BA</th>
									<th align="center">NET DUE DT</th>
									<th align="center">AMOUNT IN DC</th>
									<th align="center">CURR.</th>
									<th align="center">AMT IN LOC.CUR.</th>
									<th align="center">AMOUNT IN USD</th>
									<th align="center" style="background-color: #000066; color: white">DUE AMOUNT INR</th>
									<th align="center" style="background-color: #000066; color: white">DUE AMOUNT USD</th>
									<th align="center">ARRERS (DAYS)</th>
									<th align="center">INV TYPE</th>
									<th align="center">AGEING</th>
									<th align="center">DESK NAME</th>
									<th align="center">RESPONSIBLE COLLECTION PARTY</th>
									<th align="center">RTL/GPL/TRADER NAME</th>
									<th align="center">STATUS<br><div align="center"><input class="form-control form-control-sm" type="text" id="status" onkeyup="Search(this,'32');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">CLRNG DOC.</th>
									<th align="center">CLEARING DATE</th>
									<th align="center">WBS ELEMENT P&L ITEM</th>
									<th align="center">B/L DATE<br><div align="center"><input class="form-control form-control-sm" type="text" id="b/l_dtId" onkeyup="Search(this,'36');" placeholder="Search.." style="width:100px"/></div></th>
									<th align="center">REASON FOR OVERDUE</th>
								</tr>
							</thead>
							<tbody>
								<%if(VCOUNTERPARTY_NM.size()>0){
									for(int i=0;i<VCOUNTERPARTY_NM.size();i++){%>
									<tr>
										<td align="center"><%=i+1 %></td>
										<td align="center"><%=VCO_ABBR.elementAt(i)%></td>
										<td align="center"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
										<td align="center"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
										<td align="center"><%=VCOLL_CATEGORY.elementAt(i)%></td>
										<td align="center"><%=VCOUNTERPARTY_CATEGORY.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_TYPE.elementAt(i)%></td>
										<td align="center"><%=VCATEGORY.elementAt(i)%></td>
										<td align="center"><%=VBUSINESS.elementAt(i)%></td>
										<td align="center"><%=VLEGAL_ENTITY.elementAt(i)%></td>
										<td align="center"><%=VDOC_NO.elementAt(i)%></td>
										<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
										<td align="center"><%=VREF_K1.elementAt(i)%></td>
										<td align="center"><%=VREF_K2.elementAt(i)%></td>
										<td align="center"><%=VREF_K3.elementAt(i)%></td>
										<td align="center"><%=VDEAL_ASSIGNMENT.elementAt(i)%></td>
										<td align="center"><%=VCONT_TYPE.elementAt(i)%></td>
										<td align="center"><%=VTEXT.elementAt(i)%></td>
										<td align="center"><%=VBA.elementAt(i)%></td>
										<td align="center"><%=VNET_DUE_DT.elementAt(i)%></td>
										<td align="right"><%=VAMT_DC.elementAt(i)%></td>
										<td align="center"><%=VCURRANCY.elementAt(i)%></td>
										<td align="right"><%=VAMT_INR.elementAt(i)%></td>
										<td align="right"><%=VAMT_USD.elementAt(i)%></td>
										<td align="right" style="background-color: #b3f0ff"><%=VDUE_AMT.elementAt(i)%></td>
										<td align="right" style="background-color: #b3f0ff"><%=VDUE_AMT_USD.elementAt(i)%></td>
										<td align="center"><%=VARREARS_DAYS.elementAt(i)%></td>
										<td align="center"><%=VINV_TYPE.elementAt(i)%></td>
										<td align="center"><%=VAGING.elementAt(i)%></td>
										<td align="center"><%=VDESK_NAME.elementAt(i)%></td>
										<td align="center"><%=VRES_COLLECTION_PRTY.elementAt(i)%></td>
										<td align="center"><%=VRTL_GPL_TRADER.elementAt(i)%></td>
										<td align="center" >
											<span <%if(VSTATUS.elementAt(i).equals("Coming due")){ %>
												class="alert alert-success"
											<%}else if(VSTATUS.elementAt(i).equals("Overdue")){ %>
												class="alert alert-danger"<%} %>
											><b><%=VSTATUS.elementAt(i)%></b></span>
										</td>
										<td align="center"><%=VCLRNG_DOC.elementAt(i)%></td>
										<td align="center"><%=VCLRNG_DT.elementAt(i)%></td>
										<td align="center"><%=VWBS_PNL.elementAt(i)%></td>
										<td align="center"><%=VBL_DT.elementAt(i)%></td>
										<td align="center"><%=VOVERDUE_COZ.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="38" align="center"><%=utilmsg.infoMessage("<b>No Receivable Data is Available!</b>") %></td>
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