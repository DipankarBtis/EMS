<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script type="text/javascript">
function refresh()
{
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	
	var u = document.forms[0].u.value;
	
	var url = "../derivatives/frm_derv_inv_chng_req_aprv.jsp?&u="+u+"&month="+month+"&year="+year;
	
	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function doSubmit(action_flag,financial_year,bu_state_tin,invoice_seq,invoice_no,seq_no,invtype,accroid,inv_flag)
{
	var msg="";
	var flag=true;
	
	var approve_access=document.forms[0].approve_access.value;
	var write_access=document.forms[0].write_access.value;
	
	if(action_flag=='R')
	{
		if(write_access!='Y')
		{
			msg +="You don't have write access to Re-Print Request!\n";
			flag=false;
		}
	}
	else if(action_flag=='A' || action_flag=='X')
	{
		if(approve_access!='Y')
		{
			var action= action_flag=='A'?'Approve':'Reject';
			msg +="You dont't have approve access to "+action+" Re-Print Request!\n";
			flag=false;
		}
	}
	
	if(financial_year.trim() === "") 
    {
        msg += "Missing Financial Year!\n";
        flag = false;
    }
	if(bu_state_tin.trim() === "") 
    {
        msg += "Missing Business State TIN!\n";
        flag = false;
    }
	if(invoice_seq.trim() === "") 
    {
        msg += "Missing Invoice Seq!\n";
        flag = false;
    }
	
	if(flag)
	{
		var note = "Important: Once this request is approved:"+
			"\n- Print PDF option will be enabled for Re-Print."+
			"\n- Approval Stage will allow verification of PDF content, but approval/rejection actions will be disabled.";
		var a = false;
		if(action_flag=="R")
		{
			a = confirm(note+"\n\nPlease Update Master Data before Re-Print PDF.\n\nDo you want Generate Re-Print Request for "+invoice_no+"?");
		}
		else if(action_flag=="A")
		{
			a = confirm(note+"\n\nDo you want Approve Re-Print Request for "+invoice_no+"?");
		}
		else if(action_flag=="X")
		{
			a = confirm("Do you want Reject Re-Print Request for "+invoice_no+"?");
		} 
		
		if(a)
		{
			document.forms[0].financial_year.value=financial_year;
			document.forms[0].bu_state_tin.value=bu_state_tin;
			document.forms[0].invoice_seq.value=invoice_seq;
			document.forms[0].invoice_no.value=invoice_no;
			document.forms[0].action_flag.value=action_flag;
			document.forms[0].seq_no.value=seq_no;
			document.forms[0].invtype.value=invtype;
			document.forms[0].accroid.value=accroid;
			document.forms[0].inv_flag.value=inv_flag;
			
			document.getElementById("loading").style.visibility = "visible";
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg)
	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.derivatives.DB_Derivatives_Invoice" id="derv_inv" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
int currentYear = 0;
int currentMonth = 0;
if(!sysdate.equals(""))
{
	String[] temp = sysdate.split("/");
	currentMonth=Integer.parseInt(temp[1]);
	currentYear=Integer.parseInt(temp[2]);
}
int filter_start_year = CommonVariable.filter_start_year;

String accroid =request.getParameter("accroid")==null?"":request.getParameter("accroid");
String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");

if(month.length() == 1)
{
	month="0"+month; 
}

derv_inv.setCallFlag("DERV_INVOICE_CHANGE_REQUEST_APPROVAL");
derv_inv.setComp_cd(owner_cd);
derv_inv.setMonth(month);
derv_inv.setYear(year);
derv_inv.init();

Vector VMST_INV_TYPE_FLG = derv_inv.getVMST_INV_TYPE_FLG();
Vector VMST_INV_TYPE = derv_inv.getVMST_INV_TYPE();
Vector VINDEX = derv_inv.getVINDEX();

Vector VCOUNTERPARTY_CD = derv_inv.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = derv_inv.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = derv_inv.getVCOUNTERPARTY_NM();
Vector VDEAL_MAPPING = derv_inv.getVDEAL_MAPPING();
Vector VCONT_REF = derv_inv.getVCONT_REF();
Vector VPLANT_SEQ = derv_inv.getVPLANT_SEQ();
Vector VPLANT_ABBR = derv_inv.getVPLANT_ABBR();
Vector VBU_PLANT_SEQ = derv_inv.getVBU_PLANT_SEQ();
Vector VBU_PLANT_ABBR = derv_inv.getVBU_PLANT_ABBR();
Vector VPERIOD_START_DT = derv_inv.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = derv_inv.getVPERIOD_END_DT();
Vector VINV_FLAG = derv_inv.getVINV_FLAG();
Vector VINVOICE_TYPE = derv_inv.getVINVOICE_TYPE();
Vector VINVOICE_TYPE_NM = derv_inv.getVINVOICE_TYPE_NM();
Vector VINVOICE_DT = derv_inv.getVINVOICE_DT();
Vector VINVOICE_NO = derv_inv.getVINVOICE_NO();
Vector VBU_STATE_TIN = derv_inv.getVBU_STATE_TIN();
Vector VFIN_YEAR = derv_inv.getVFIN_YEAR();
Vector VINVOICE_SEQ = derv_inv.getVINVOICE_SEQ();
Vector VACTION_FLAG = derv_inv.getVACTION_FLAG();
Vector VSEQ_NO = derv_inv.getVSEQ_NO();
Vector VREMAINING_DAYS = derv_inv.getVREMAINING_DAYS();
Vector VCOUNT = derv_inv.getVCOUNT();
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_Derivatives_Invoice">
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
					    	Derivatives Invoice Change Request/Approval
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="form-group row">
								<div class="col-md-4 col-sm-0 col-xs-0"></div>
								<div class="col-md-4 col-sm-6 col-xs-6">
									<!-- <div class="d-flex justify-content-center"> -->
										<div class="form-group row">
											<div class="col-auto">
												<label class="form-label"><b>Month/Year</b></label>
								  			</div>
								  			<div class="col">
								  				<select class="form-select form-select-sm" name="month" onchange="refresh();">
													<option value="01" label="January">January</option>
													<option value="02" label="February">February</option>
													<option value="03" label="March">March</option>
													<option value="04" label="April">April</option>
													<option value="05" label="May">May</option>
													<option value="06" label="June">June</option>
													<option value="07" label="July">July</option>
													<option value="08" label="August">August</option>
													<option value="09" label="September">September</option>
													<option value="10" label="October">October</option>
													<option value="11" label="November">November</option>
													<option value="12" label="December">December</option>
												</select>
												<script>document.forms[0].month.value="<%=month%>"</script>
											</div>
											<div class="col">
								  				<select class="form-select form-select-sm" name="year" onchange="refresh();">
								  					<%for(int i=(currentYear+1); i > filter_start_year;i--){ %>
														<option value="<%=i%>"><%=i%></option>
													<%} %>
												</select>
												<script>document.forms[0].year.value="<%=year%>"</script>
											</div>
										</div>
									<!-- </div> -->
						  		</div>
						  		<div class="col-md-4 col-sm-0 col-xs-0"></div>
						  	</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%int i=0,k=0,l=0,m=0;
					for(int j=0; j<VMST_INV_TYPE.size(); j++)
					{ 
						int index=Integer.parseInt(""+VINDEX.elementAt(j));
						String heading = ""+VMST_INV_TYPE_FLG.elementAt(j);
						%>
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="<%=heading%>">
	   										<button name="sub_module_cd" class="accordion-button <%if(!accroid.equals(heading)){%>collapsed<%}%> accor-btn" type="button" 
	   											data-bs-toggle="collapse" data-bs-target="#collapse<%=j%>" aria-expanded="<%if(!accroid.equals(heading)){%>true<%}else{%>false<%} %>" aria-controls="collapse<%=j%>">
								    			<%=VMST_INV_TYPE.elementAt(j)%>&nbsp;<font color="blue" style="background: white;padding: 2px 5px 4px 5px;border-radius: 30px;">(<%=index%> Items)</font>
								      		</button>	
								    	</h2>
										<div id="collapse<%=j%>" class="accordion-collapse collapse <%if(accroid.equals(heading)){%>show<%}%>" aria-labelledby="<%=heading%>">
								      		<div class="accordion-body accor-body">
												<div class="row">
													<div class="col-md-12 col-sm-12 col-xs-12">
														<div class="table-responsive">
															<table class="table table-bordered table-hover serchtbl" id="example<%=j%>">
															<thead id="tbsearch<%=j%>">
																<tr>
																	<th class="tbser<%=j%>" rowspan="2">Customer</th>
																	<th class="tbser<%=j%>" rowspan="2">Invoice Type</th>
																	<th class="tbser<%=j%>" rowspan="2">Invoice No</th>
																	<th class="tbser<%=j%>" rowspan="2">Invoice Date</th>
																	<!-- <th class="tbser<%=j%>" rowspan="2">Billing Period</th>-->
																	<th class="tbser<%=j%>" rowspan="2">Contract No<br>[Contract/Trade Ref#]</th>
																	<th class="tbser<%=j%>" rowspan="2">Plant</th>
																	<th class="tbser<%=j%>" rowspan="2">Business Unit</th>
																	<th colspan="4">Re-Print PDF Action</th>
																</tr>
																<tr>
																	<th>Remaining Days : Count <br>(<%=sysdate%>)</th>
																	<th>Request</th>
																	<th colspan="2">Approval/Reject</th>
																</tr>
															</thead>
															<tbody>
																<%k=0;
																if(index>0)
																{
																	for(i=i; i<VCOUNTERPARTY_CD.size(); i++)
																	{
																	k+=1;
																	%>
																	<tr>
																		<td align="center" title="<%=VCOUNTERPARTY_NM.elementAt(i)%>">
																			<%=VCOUNTERPARTY_ABBR.elementAt(i)%>
																		</td>
																		<td align="center"><%=VINVOICE_TYPE_NM.elementAt(i)%></td>
																		<td align="center"><%=VINVOICE_NO.elementAt(i)%></td>
																		<td align="center"><%=VINVOICE_DT.elementAt(i)%></td>
																		<!-- <td align="center">
																			<font><%=VPERIOD_START_DT.elementAt(i)%> - <%=VPERIOD_END_DT.elementAt(i)%></font>
																		</td> -->
																		<td align="center"><div style="width:400px; word-wrap: break-word; white-space: normal;"><%=VDEAL_MAPPING.elementAt(i) %><br>[<%=VCONT_REF.elementAt(i) %>]</div></td>
																		<td align="center"><%=VPLANT_ABBR.elementAt(i)%></td>
																		<td align="center"><%=VBU_PLANT_ABBR.elementAt(i)%></td>
																		<td align="center"><%=VREMAINING_DAYS.elementAt(i) %> : <%=VCOUNT.elementAt(i).equals("")?"0":VCOUNT.elementAt(i)%></td>
																		<td align="center">
																			<input type="button" class="btn btn-sm request_btn" name="btn_submit" id="submit<%=i%>" value="Request" 
																			<%if(VREMAINING_DAYS.elementAt(i).toString().equals("0")) {%>disabled style="background: grey;color:black;border-color: grey"
																			<%}else if(VCOUNT.elementAt(i).toString().equals("2")) {%>disabled style="background: grey;color:black;border-color: grey"
																			<%}else if(VACTION_FLAG.elementAt(i).equals("P") || VACTION_FLAG.elementAt(i).equals("X")) {%> 
																			<%}else if(!VACTION_FLAG.elementAt(i).equals("")){%>disabled style="background: grey;color:black;border-color: grey"<%} %>
																				onclick="doSubmit('R','<%=VFIN_YEAR.elementAt(i)%>','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
																			'<%=VSEQ_NO.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=heading%>','<%=VINV_FLAG.elementAt(i)%>');">
																		</td>
																		<td align="center">
															   				<input type="button" class="btn btn-sm config_btn" name="approve" id="approve<%=i%>" value="Approve" 
															   				<%if(!VACTION_FLAG.elementAt(i).equals("R")) {%>disabled style="background: grey;color:black;border-color: grey"
															   				<%}else if(VREMAINING_DAYS.elementAt(i).equals("0")) {%>disabled style="background: grey;color:black;border-color: grey"<%} %> 
															   				onclick="doSubmit('A','<%=VFIN_YEAR.elementAt(i)%>','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
															   				'<%=VSEQ_NO.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=heading%>','<%=VINV_FLAG.elementAt(i)%>');">
															   			</td>
															   			<td align="center">	
															   				<input type="button" class="btn btn-danger btn-sm" name="reject" id="reject<%=i%>" value="Reject" 
															   				<%if(!VACTION_FLAG.elementAt(i).equals("R")) {%>disabled style="background: grey;color:black;border-color: grey;border-radius: 50px;font-weight: bold"
															   				<%}else if(VREMAINING_DAYS.elementAt(i).equals("0")) {%>disabled style="background: grey;color:black;border-color: grey;border-radius: 50px;font-weight: bold"
															   				<%}else{ %>style="border-radius: 50px;font-weight: bold;"<%} %>
															   				onclick="doSubmit('X','<%=VFIN_YEAR.elementAt(i)%>','<%=VBU_STATE_TIN.elementAt(i)%>','<%=VINVOICE_SEQ.elementAt(i)%>','<%=VINVOICE_NO.elementAt(i)%>',
															   				'<%=VSEQ_NO.elementAt(i)%>','<%=VINVOICE_TYPE.elementAt(i)%>','<%=heading%>','<%=VINV_FLAG.elementAt(i)%>');">
															   			</td>
																	</tr>
																	<%if(k==index){
																			i=i+1;
																			break;
																		} %>
																	<%} %>	
																<%}else{ %>
																	<tr>
																		<td align="center" colspan="11"><%=utilmsg.infoMessage("<b>No Invoice is Generated!</b>") %></td>
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
						</div>
					<%} %>	
				</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div align="left"><%=utilmsg.infoMessage("<b>Note:"
							+"<br>1. Re-print Requests permitted up to 30 days from Invoice Date!"
							+"<br>2. Re-print Requests permitted only Twice!</b>") %></div>
   					</div>					
   				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="DERV_INVOICE_CHANGE_ACTION">
<input type="hidden" name="sysdate" value="<%=sysdate%>">

<input type="hidden" name="financial_year" value="">
<input type="hidden" name="bu_state_tin" value="">
<input type="hidden" name="invoice_seq" value="">
<input type="hidden" name="invoice_no" value="">
<input type="hidden" name="action_flag" value="">
<input type="hidden" name="change_type" value="REPRINT_PDF">
<input type="hidden" name="seq_no" value="">
<input type="hidden" name="segment" value="DERV">

<input type="hidden" name="invtype" value="">
<input type="hidden" name="accroid" value="">
<input type="hidden" name="inv_flag" value="">

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