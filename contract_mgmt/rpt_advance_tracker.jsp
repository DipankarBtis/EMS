<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
var newWindow;
function openContList()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var url="rpt_ledger_contract_list.jsp?counterparty_cd="+counterparty_cd;
	
	if(!newWindow || newWindow.closed)
	{
		newWindow = window.open(url,"Supply Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
	else
	{
		newWindow.close();
		newWindow = window.open(url,"Supply Contract List","top=10,left=10,width=1100,height=600,scrollbars=1");
	}
}

function setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type)
{
	var u = document.forms[0].u.value;
	
	var url = "rpt_advance_tracker.jsp?counterparty_cd="+countpty_cd+"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+
			"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&st_dt="+st_dt+"&end_dt="+end_dt+
			"&u="+u+"&contract_type="+cont_type;//+"&cargo_no="+cargo_no;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Report" id="cont_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String yestdate = utildate.getPreviousDate();

String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");

String st_dt = request.getParameter("st_dt")==null?"":request.getParameter("st_dt");
String en_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");


cont_mgmt.setComp_cd(owner_cd);
cont_mgmt.setCounterparty_cd(counterparty_cd);
cont_mgmt.setAgmt_no(agmt_no);
cont_mgmt.setAgmt_rev_no(agmt_rev_no);
cont_mgmt.setCont_no(cont_no);
cont_mgmt.setCont_rev_no(cont_rev_no);
cont_mgmt.setContract_type(contract_type);
cont_mgmt.setCallFlag("ADVANCE_TRACKER");
cont_mgmt.init();

Vector VMST_COUNTERPARTY_CD = cont_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = cont_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = cont_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VGAS_DT = cont_mgmt.getVGAS_DT();
Vector VDESC = cont_mgmt.getVDESC();
Vector VCREDIT = cont_mgmt.getVCREDIT();
Vector VDEBIT = cont_mgmt.getVDEBIT();
Vector VBALANCE_AMT = cont_mgmt.getVBALANCE_AMT();

String cont_ref_no=cont_mgmt.getCont_ref_no();
String dealDisplayMap=cont_mgmt.getDealDisplayMap();
String counterparty_nm=cont_mgmt.getCounterparty_nm();
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
					    	Advance | Payment Tracker
					    </div>
					    <a href="../contract_mgmt/xls_advance_tracker.jsp?fileName=Advance_Tracker.xls&counterparty_cd=<%=counterparty_cd%>
					    &cont_no=<%=cont_no %>&cont_rev_no=<%=cont_rev_no%>&agmt_no=<%=agmt_no%>&agmt_rev_no=<%=agmt_rev_no %>
					    &contract_type=<%=contract_type%>&st_dt=<%=st_dt%>&end_dt=<%=en_dt%>" download="Advance Tracker">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<label class="form-label"><b>Counterparty</b></label>
								</div>
							</div>
						</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh();">
										<option value="">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-sm-12 col-xs-12 col-md-12">
				    				<input type="button" class="btn btn-info btn-sm select_btn" value="Select Contract" onclick="openContList();" style="font-weight: bold;">
				    			</div>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				    				<textarea class="form-control" rows="4" readonly style="font-weight:bold;">Counterparty : <%=counterparty_nm %>&#13;&#10;Deal Map : <%=dealDisplayMap%>&#13;&#10;Cont/Trade Ref : <%=cont_ref_no%>&#13;&#10;Contract Duration : <%=st_dt%> - <%=en_dt%></textarea>				    				
				    			</div>
				  			</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered serchtbl" id="example0">
									<thead id="tbsearch0">
										<tr>
											<th>Sr. No.</th>
											<th class="tbser0">Date</th>
											<th class="tbser0">Description</th>
											<th>Debit (INR)</th>
											<th>Credit (INR)</th>
											<th>Balance (INR)</th>											
										</tr>
									</thead>
									<tbody>
									<%if(VGAS_DT.size() > 0){ int sr_count = 0;%>										
										<%for(int i=VGAS_DT.size()-1; i>=0; i--){ sr_count++;%>										
										<tr>
											<td align="center"><%=sr_count%></td>									
											<td align="center"><%=VGAS_DT.elementAt(i)%></td>
											<td align="left"><%=VDESC.elementAt(i)%></td>
											<td align="right" <%if(!VDEBIT.elementAt(i).equals("-")){%> style="color: red;" <%}%>><%=VDEBIT.elementAt(i)%></td>
											<td align="right" <%if(!VCREDIT.elementAt(i).equals("-")){%> style="color: green;" <%}%>><%=VCREDIT.elementAt(i)%></td>
											<td align="right" <%if(VBALANCE_AMT.elementAt(i).toString().contains("-") ){%> style="color: red;" <%}else{%>style="color: green;"<%}%>>
												<%=VBALANCE_AMT.elementAt(i)%>
											</td>
										</tr>
										<%} %>
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

<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
<script>
$(document).ready(function() {	
	$('.serchtbl').each(function(k){
		$('#tbsearch'+k).each(function(j){						
			$('#tbsearch'+k+' th').each(function(i){
				var title = $(this).text();
				if($(this).hasClass('tbser'+k))
				{
					$(this).html(title+'<div align="center"><input type="text" class="form-control form-control-sm" id="table_'+title+''+k+'" onkeyup="Search(this,'+i+','+k+');" placeholder="Search '+title+'" style="width:100px"/></div>');
				}
			});		
		});
	});
});
	
function Search(obj, indx, tblid) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example"+tblid);
  	
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