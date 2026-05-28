<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;

	var flag=true;
	var msg="";
	
	var count = compareDate(from_dt,to_dt);
	if(parseInt(count) == 1)
	{
		msg+="From Date should be less or equal To Date!";
		flag=false;
	}
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_indian_gas_poc.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
				"&from_dt="+from_dt+"&to_dt="+to_dt;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}
function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var url = "xls_indian_gas_poc.jsp?fileName=NCF Report.xls&counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt;
	location.replace(url);
}
</script>

</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="db_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String comp_abbr = utilBean.getCompanyAbbr(owner_cd);
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

db_mgmt.setCallFlag("IND_GAS_POC");
db_mgmt.setComp_cd(owner_cd);
db_mgmt.setCounterparty_cd(counterparty_cd);
db_mgmt.setFrom_dt(from_dt);
db_mgmt.setTo_dt(to_dt);
db_mgmt.init();

Vector VMST_COUNTERPARTY_CD = db_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = db_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = db_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VCO_CODE = db_mgmt.getVCO_CODE();
Vector VCO_ABBR = db_mgmt.getVCO_ABBR();
Vector VCO_NM = db_mgmt.getVCO_NM();
Vector VCOUNTERPARTY_NM = db_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = db_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_CATEGORY = db_mgmt.getVCOUNTERPARTY_CATEGORY();
Vector VDELIVERY_MONTH = db_mgmt.getVDELIVERY_MONTH();
Vector VDELIVERY_YEAR = db_mgmt.getVDELIVERY_YEAR();
Vector VVOLUME = db_mgmt.getVVOLUME();
Vector VTRADE_DT = db_mgmt.getVTRADE_DT();
Vector VTRADE_TYPE = db_mgmt.getVTRADE_TYPE();
Vector VBUY_SALE = db_mgmt.getVBUY_SALE();
Vector VUNIT_OF_MEASURE = db_mgmt.getVUNIT_OF_MEASURE();
Vector VINSTRUMENT_INDICATOR = db_mgmt.getVINSTRUMENT_INDICATOR();
Vector VDEAL_NUMBER = db_mgmt.getVDEAL_NUMBER();
Vector VDEAL_REF = db_mgmt.getVDEAL_REF();
Vector VNCF_CATEGORY = db_mgmt.getVNCF_CATEGORY();

double buy_sum = db_mgmt.getBuy_sum();
double sell_sum = db_mgmt.getSell_sum();

NumberFormat nf = new DecimalFormat("###########0.00");
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
				    		NCF Report 
	   	 				</div>
	   	 				<!-- <div onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div> -->
						<div class="col-auto">
					 		<span class="input-group-text"><a id="rptDownloadBtn"><i title="Export To Excel" onclick="exportToXls();" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></a></span>
						</div>
						<%-- <a href="../mgmt_reports/xls_indian_gas_poc.jsp?fileName=NCF Report.xlsx&counterparty_cd=<%=counterparty_cd %>&from_dt=<%=from_dt %>&to_dt=<%=to_dt %>" download="NCF Report.xls">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a> --%>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--All--</option>
										<!-- <option value="-1">SEMTIPL-STORAGE</option> -->
										<%for(int i=0;i<VMST_COUNTERPARTY_ABBR.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
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
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="table-responsive">
						<table class="table table-bordered" id="example">
							<thead id="tbsearch">
								<tr>
									<th align="center">Sr#</th>
									<th align="center">Legal Entity ABBR</th>
									<th align="center">Legal Entity</th>
									<th align="center">Counterparty ABBR</th>
									<th align="center">Counterparty</th>
									<th align="center">Deal Id#</th>
									<th align="center">Trade Dt</th>
									<th align="center">Trade Type</th>
									<th align="center">Buy/Sale</th>
									<th align="center">Delivery Month</th>
									<th align="center">Delivery Year</th>
									<th align="center">Volume</th>
									<th align="center">Unit Of Measure</th>
									<th align="center">Instrument Indicator</th>
									<th align="center">Deal Ref#</th>
									<th align="center">NCF Category</th>
								</tr>
							</thead>
							<tbody>
								<%if(VCO_CODE.size()>0){
									for(int i=0;i<VCO_CODE.size();i++){%>
									<tr>
										<td align="center"><%=i+1 %></td>
										<td align="left"><%=VCO_ABBR.elementAt(i)%></td>
										<td align="left"><%=VCO_NM.elementAt(i)%></td>
										<td align="left"><%=VCOUNTERPARTY_ABBR.elementAt(i)%></td>
										<td align="left"><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
										<td align="center"><%=VDEAL_NUMBER.elementAt(i)%></td>
										<td align="center"><%=VTRADE_DT.elementAt(i)%></td>
										<td align="center"><%=VTRADE_TYPE.elementAt(i)%></td>
										<td align="center">
											<span
											<%if(VBUY_SALE.elementAt(i).equals("Buy")){ %>
	    										class="alert" style="background: #ffccff; color: #cc00cc;"
	    									<%}else { %>
	    										class="alert alert-primary"
	    									<%}%>><b><%=VBUY_SALE.elementAt(i)%></b></span>
		    							</td>
										<td align="center"><%=VDELIVERY_MONTH.elementAt(i)%></td>
										<td align="center"><%=VDELIVERY_YEAR.elementAt(i)%></td>
										<td align="right"><%=VVOLUME.elementAt(i)%></td>
										<td align="center"><%if(VUNIT_OF_MEASURE.elementAt(i).equals("1")){%>MMBTU<%} %></td>
										<td align="center"><%=VINSTRUMENT_INDICATOR.elementAt(i)%></td>
										<td align="center"><%=VDEAL_REF.elementAt(i)%></td>
										<td align="center"><%=VNCF_CATEGORY.elementAt(i) %></td>
									</tr>
									<%} %>
									<%-- <tr>
										<td align="right" colspan="11"><b>Total Buy Volume:</b></td>
										<td align="right" ><%=nf.format(buy_sum )%></td>
										<td align="center" colspan="4"></td>
									</tr>
									<tr>
										<td align="right" colspan="11"><b>Total Sell Volume:</b></td>
										<td align="right" ><%=nf.format(sell_sum) %></td>
										<td align="center" colspan="4"></td>
									</tr> --%>
								<%}else{ %>
									<tr>
										<td colspan=16 align="center"><%=utilmsg.infoMessage("<b>No Indain Gas POC Data is Available!</b>") %></td>
									</tr>
								<%} %>
							</tbody>
						</table>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<div align="center" class="col-sm-3 col-xs-3 col-md-3">   
							<div class="form-group row">
								<div align="right" class="col-auto"> 
									<b>Total Buy Volume : </b>
								</div>
								<div align="left" class="col">  
									<%=nf.format(buy_sum ) %> MMBTU
								</div>
							</div>
						</div>
						<div align="center" class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div align="right" class="col-auto">  
									<b>Total Sell Volume : </b>
								</div>
								<div align="left" class="col">  
									<%=nf.format(sell_sum ) %> MMBTU
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="comp_abbr" value="<%=comp_abbr%>">

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
</body>
</html>