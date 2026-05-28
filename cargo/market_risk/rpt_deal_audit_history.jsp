<%@page import="com.etrm.fms.util.UtilBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!--Harsh Maheta 20230901 : Developed report for Deal audit history-->

<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script type="text/javascript">
function refresh()
{
	var from_dt=document.forms[0].from_dt.value;
	var to_dt=document.forms[0].to_dt.value;
	var counterparty_cd=document.forms[0].counterparty_cd.value;
	var status_flag = document.forms[0].status_flag.value;
	
	var u = document.forms[0].u.value;
	
	if(trim(from_dt)!="" &&  trim(to_dt)!="")
	{
		var url = "rpt_deal_audit_history.jsp?u="+u+"&counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt+"&status_flag="+status_flag;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
}

function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var status_flag = document.forms[0].status_flag.value;
	
	var url = "xls_deal_audit_history.jsp?fileName=Deal Audit History.xls&counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt+"&status_flag="+status_flag;

	location.replace(url);
}
function newDeals()
{
	 const status_Checkbox = document.getElementById("status_Checkbox");
	 var status_flag = document.forms[0].status_flag.value;
	 
	  if (status_Checkbox.checked) 
	  {
		  status_Checkbox.value = "New Deals";
		  document.forms[0].status_flag.value = "New Deals";
	  } 
	  else 
	  {
		  document.forms[0].status_flag.value = "0";
	  }
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.market_risk.DataBean_MarketRisk" id="dbmarket" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateutil" scope="page"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.UtilBean" id="utilBean" scope="request"></jsp:useBean>
<%

String firstDtOfMth = ""+dateutil.getFirstDateOfMonth();
String sysdate = ""+dateutil.getSysdate();
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");

String from_dt=request.getParameter("from_dt")==null?firstDtOfMth:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String status_flag =request.getParameter("status_flag")==null?sysdate:request.getParameter("status_flag");
String company_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");

dbmarket.setCallFlag("DEAL_AUDIT_HISTORY");
dbmarket.setFrom_dt(from_dt);
dbmarket.setCounterparty_cd(counterparty_cd);
dbmarket.setTo_dt(to_dt);
dbmarket.setStatus_flag(status_flag);
dbmarket.setComp_cd(company_cd);
dbmarket.init();

Vector VCONTP_NM = new Vector();
Vector VCONTP_ABBR = new Vector();

Vector VCOUNTERPARTY_CD = dbmarket.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbmarket.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbmarket.getVCOUNTERPARTY_ABBR();
Vector VDEAL_DETAILS = dbmarket.getVDEAL_DETAILS();
Vector VDEAL_NAME = dbmarket.getVDEAL_NAME();
Vector VCONT_NO = dbmarket.getVCONT_NO();
Vector VLAST_UPDATE = dbmarket.getVLAST_UPDATE();
Vector VLAST_UPDATE_BY = dbmarket.getVLAST_UPDATE_BY();
Vector VDEAL_STATUS = dbmarket.getVDEAL_STATUS();

%>
<body>
<%@ include file="../home/header.jsp"%>

<form>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
						<div class="topheader">
					    	Deal Audit History
					    </div>
					    <div align="right" onclick="exportToXls();" style="color:green;">
							<span class="input-group-text"><i title="Export to Excel" style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-15">
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
						<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<label class="form-label"><b>Form Date</b></label>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm" >
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
				    			</div>
				  			</div>
						</div>
						<!-- <div class="col-sm-3 col-xs-3 col-md-3"></div>
					</div>
					<div class="row m-b-15"> -->
						<!-- <div class="col-sm-3 col-xs-3 col-md-3"></div> -->
						<div class="col-sm-1 col-xs-1 col-md-1">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    			<label class="form-label"><b>To Date</b></label>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<div class="input-group input-group-sm">
					      				<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" maxLength="10" onblur="validateDate(this);" onchange="validateDate(this);refresh();" autocomplete="off">
					      				<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
				      				</div>
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
					</div>
					<div class="row m-b-15">
						<div class="col-auto">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--All--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++)
										{ 
											String cpt_nm = ""+utilBean.getCounterpartyName(""+VCOUNTERPARTY_CD.elementAt(i));
											String cpt_abbr = ""+utilBean.getCounterpartyABBR(""+VCOUNTERPARTY_CD.elementAt(i));
											VCONTP_NM.add(cpt_nm);
											VCONTP_ABBR.add(cpt_abbr);
										%>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCONTP_ABBR.elementAt(i)%> - <%=VCONTP_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<div class="col">  
							<div class="form-group row">
								<div class="float-right" align="right">
									<input style="vertical-align: center" type="checkbox" name="status_Checkbox" id="status_Checkbox" onclick="newDeals();" onchange="refresh();" <%if(status_flag.equals("New Deals")){ %>checked="checked"<%} %>>
									<input type="hidden" name="status_flag" id="status_flag" value="">
									<script>document.forms[0].status_flag.value="<%=status_flag%>"</script>
									<label class="form-label"><b>New Deals</b></label>
								</div>
							</div>
						</div>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered" id="search_by_filter">
							<thead>
								<tr>
									<th>Sr#</th>
									<th>Updated Date<br><div align="center"><input class="form-control form-control-sm" type="text" id="updt" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Updated By<br><div align="center"><input class="form-control form-control-sm" type="text" id="upby" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Counterparty<br><div align="center"><input class="form-control form-control-sm" type="text" id="cp" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Contract#<br><div align="center"><input class="form-control form-control-sm" type="text" id="contNo" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Contract/Trade Ref#<br><div align="center"><input class="form-control form-control-sm" type="text" id="contref" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Change/Activity<br><div align="center"><input class="form-control form-control-sm" type="text" id="Change_Activity" onkeyup="Search(this,'6');" placeholder="Search.." style="width:100px"/></div></th>
									<th>Contract Status<br><div align="center"><input class="form-control form-control-sm" type="text" id="status" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>
								</tr>
							</thead>
							<tbody id="mainTbody">
							<%if(VLAST_UPDATE.size()>0) {%>
								<%for(int i=0;i<VDEAL_DETAILS.size(); i++){%>
								<tr>
									<td><div><%=i+1%> </div></td>
									<td><div><%=VLAST_UPDATE.elementAt(i) %> </div></td>
									<td><div><%=VLAST_UPDATE_BY.elementAt(i) %> </div></td>
									<td title="<%=VCOUNTERPARTY_NM.elementAt(i) %>"><div><%=VCOUNTERPARTY_ABBR.elementAt(i)%></div></td>
									<td><div><%=VCONT_NO.elementAt(i)%></div></td>
									<td><div><%=VDEAL_NAME.elementAt(i) %> </div></td>
									<td><div><%=VDEAL_DETAILS.elementAt(i)%> </div></td>
									<td align="center">
										<span <%if(VDEAL_STATUS.elementAt(i).equals("New")){%> 
											class="alert alert-info"
										<%}else if(VDEAL_STATUS.elementAt(i).equals("Approved")){ %>
											class="alert" style="background:#b3ffb3;color: #008000;"
										<%}else if(VDEAL_STATUS.elementAt(i).equals("Not Approved")){%>
											class="alert alert-danger"
										<%}else if(VDEAL_STATUS.elementAt(i).equals("Pending Approval")){ %>
											class="alert alert-warning"
										<%} %>>
											<b><%=VDEAL_STATUS.elementAt(i)%></b>
										</span>
									</td>
								</tr>
								<%} %>
							<%}else{ %>
								<tr>
									<td colspan="8" align="center"><%=utilmsg.infoMessage("<b>No Change in Deal is Available!</b>") %></td>
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
</body>

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
</html>