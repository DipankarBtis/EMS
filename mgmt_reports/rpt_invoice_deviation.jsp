<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>

function refresh() 
{
    var from_dt = document.forms[0].from_dt.value;
    var to_dt = document.forms[0].to_dt.value;
    var temp_from_dt = document.forms[0].temp_from_dt.value;
    var temp_to_dt = document.forms[0].temp_to_dt.value;
    var counterparty_cd = document.forms[0].counterparty_cd.value;
    
    var flag = true;
    var msg = "";

    var flag=checkDateRangeOnApply(document.forms[0].from_dt,document.forms[0].to_dt);
    var u = document.forms[0].u.value;

    if(flag)
   	{
   		if(trim(from_dt)!="" && trim(to_dt)!="")
   		{
   			if (flag==true) 
         {
   				var url = "rpt_invoice_deviation.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd;
                   document.getElementById("loading").style.visibility = "visible";
                   location.replace(url);
         }
   	   }
   	}
}

function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch");
  	
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

function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
    var to_dt = document.forms[0].to_dt.value;
    var owner_abbr = document.forms[0].owner_abbr.value;
    var counterparty_cd = document.forms[0].counterparty_cd.value;
    var fileName = owner_abbr+"_Invoice_Deviation_"+from_dt+"_To_"+to_dt+".xls";
    
    var url  = "xls_invoice_deviation.jsp?fileName="+fileName+"&from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd;
    location.replace(url);
}
</script>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="mgmt_reports" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdate  = utildate.getSysdate();
String firstDate="01"+sysdate.substring(2, sysdate.length());

String from_dt = request.getParameter("from_dt")==null?""+firstDate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?""+sysdate:request.getParameter("to_dt");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

mgmt_reports.setCallFlag("INVOICE_DEVIATION");
mgmt_reports.setComp_cd(owner_cd);
mgmt_reports.setFrom_dt(from_dt);
mgmt_reports.setTo_dt(to_dt);
mgmt_reports.setCounterparty_cd(counterparty_cd);
mgmt_reports.init();

Vector VCOUNTERPTY_CD = mgmt_reports.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_NM = mgmt_reports.getVCOUNTERPTY_NM();
Vector VCOUNTERPTY_ABBR = mgmt_reports.getVCOUNTERPTY_ABBR();
Vector VINVOICE_NO = mgmt_reports.getVINVOICE_NO();
Vector VINVOICE_DT = mgmt_reports.getVINVOICE_DT();
Vector VDUE_DT_FLG = mgmt_reports.getVDUE_DT_FLG();
Vector VDUE_DT_REMARK = mgmt_reports.getVDUE_DT_REMARK();
Vector VEXCHNG_RATE_FLAG = mgmt_reports.getVEXCHNG_RATE_FLAG();
Vector VEXCHNG_DT_REMARK = mgmt_reports.getVEXCHNG_DT_REMARK();
Vector VBILLING_FREQ_FLAG = mgmt_reports.getVBILLING_FREQ_FLAG();
Vector VBILLING_DT_REMARK = mgmt_reports.getVBILLING_DT_REMARK();
Vector VCONT_NO = mgmt_reports.getVCONT_NO();
Vector VCOUNTERPARTY_CD = mgmt_reports.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = mgmt_reports.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = mgmt_reports.getVCOUNTERPARTY_ABBR();
Vector VCHK_FLG = mgmt_reports.getVCHK_FLG();
%>

</head>
<body>
<%@ include file="../home/header.jsp"%>
<form>
<div class="box-body">
  <div class="row">
    <div class="col-md-12 col-sm-12 col-xs-12">
      <div class="card cardmain">
         <div class="card-header cdheader">
			<div class="d-flex  justify-content-between">
			   <div class="topheader">Invoice Deviation</div>
				  <a>
					<span class="input-group-text">
					 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
					</span>
				  </a>
			</div>
		  </div>
		  <div class="card-body cdbody">
			<div class="row">
				<div class="col-sm-5 col-xs-5 col-md-5">
					<div class="form-group row">
						<div class="col-auto">
							<label class="form-label"><b>From</b></label>
						</div>
						<div class="col">
		      				<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
	      						onblur="validateDate(this);" onchange="" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
      						</div>
		    			</div>
						<div class="col-auto">
							<label class="form-label"><b>To</b></label>
						</div>
						<div class="col">
		      				<div class="input-group input-group-sm" >
	      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
	      						onblur="validateDate(this);" onchange="" autocomplete="off">
	      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
      						</div>
		    			</div>
					</div>
				</div>
				<div class="col-sm-4 col-xs-4 col-md-4">  
					<div class="form-group row">
						<div class="col-auto">
							<label class="form-label"><b>Customer</b></label>
						</div>
						<div class="col">
							<select class="form-select form-select-sm" name="counterparty_cd" onchange="">
								<option value="0">--Select--</option>
								<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
								<%
									if (VCOUNTERPARTY_ABBR.elementAt(i) != null 
									        && !VCOUNTERPARTY_ABBR.elementAt(i).toString().trim().isEmpty()
									        && VCOUNTERPARTY_NM.elementAt(i) != null
									        && !VCOUNTERPARTY_NM.elementAt(i).toString().trim().isEmpty()) {
									%>
									    <option value="<%= VCOUNTERPARTY_CD.elementAt(i) %>">
									        <%= VCOUNTERPARTY_ABBR.elementAt(i) %> - <%= VCOUNTERPARTY_NM.elementAt(i) %>
									    </option>
									<%
									}
                                     %>
								<%} %>
							</select>
							<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
						</div>
					</div>
				</div>
				<div class="col-auto">
					<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
				</div>
			</div>
		 </div>
		 <div class="card-body cdbody">
		  <div class="row">
			<div class="col-sm-12 col-xs-12 col-md-12">&nbsp;</div>
		  </div>
		  
		  <div class="col-md-12 col-sm-12 col-xs-12">
			<div class="row">
			 <div class="table-responsive">
			  <table class="table table-bordered table-hover" id="filterbysearch" >
				<thead>
					<tr>
						<th>Sr.no.</th>
						<th>Customer<div align="center"><input class="form-control form-control-sm" type="text" id="customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"></div></th>
						<th>Invoice No<div align="center"><input class="form-control form-control-sm" type="text" id="invoice_no" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px" ></div></th>
						<th>Invoice Date<div align="center"><input class="form-control form-control-sm" type="text" id="invoice_dt" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"></div></th>
						<th>Due Date Deviation</th>
						<th>Exchange Rate Deviation</th>
						<th>Billing Frequency Deviation</th>
					</tr>
				</thead>
				<tbody>
					<%int k=0;
						if(VCONT_NO.size() > 0){ %>
							<%for(int i=0;i<VCONT_NO.size(); i++){ 
							if((""+VDUE_DT_FLG.elementAt(i)).equalsIgnoreCase("Y") || (""+VEXCHNG_RATE_FLAG.elementAt(i)).equalsIgnoreCase("Y") 
									|| (""+VBILLING_FREQ_FLAG.elementAt(i)).equalsIgnoreCase("Y")){
								k+=1;%>
							<tr>
								<td align="right"><%= k %></td>
								<td><%=VCOUNTERPTY_NM.elementAt(i)%></td>
								<td align="left"><%= VINVOICE_NO.elementAt(i) %></td>
								<td align="center"><%= VINVOICE_DT.elementAt(i) %></td>
								<td align="left"><%= VDUE_DT_REMARK.elementAt(i) %></td>
								<td align="left"><%= VEXCHNG_DT_REMARK.elementAt(i) %></td>
								<td align="left"><%= VBILLING_DT_REMARK.elementAt(i) %></td>
							</tr>
						<% } 
							}%> 
					    <%}else{ %>
						<tr>
							<td colspan="7" align="center"><%=utilmsg.infoMessage("<b>No Deviation Data Available for Selected Period!</b>") %></td>
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
<input type="hidden" name="owner_abbr" value="<%=owner_abbr%>">
<input type="hidden" name="temp_from_dt" value="<%=from_dt%>">
<input type="hidden" name="temp_to_dt" value="<%=to_dt%>">

</form>
</body>
</html>