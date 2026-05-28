<%@page import="com.etrm.fms.util.DateUtil"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function setActiveInactive(obj,index)
{
	if(obj.checked)
	{
		document.getElementById("lb"+index).innerHTML="Active";
		document.getElementById("status_flag"+index).value="Y";
	}
	else
	{
		document.getElementById("lb"+index).innerHTML="In-Active";
		document.getElementById("status_flag"+index).value="N";
	}
}

function addrow()
{
	var max_seq = document.getElementById("item_size").value;
	var prev_minus= document.getElementById("minus"+max_seq);
	var prev_plus= document.getElementById("plus"+max_seq);
	var new_seq_no = parseInt(max_seq)+1;
	var id_seq_no = parseInt(new_seq_no)-1;
	
	var tab_name = document.getElementById("itemTab");
	var row_new = document.createElement("tr"); 
	row_new.id = 'row'+new_seq_no;
	
	
	var td01 = document.createElement("td");
	td01.align='center';
	var div01 = document.createElement("DIV");
	div01.style.width='100px';
	var div001 = document.createElement("DIV");
	div001.className="input-group input-group-sm";
	var input01 = document.createElement("input")
	input01.className="form-control form-control-sm date fmsdtpick";
	input01.name = "from_dt";
	input01.id = "from_dt"+id_seq_no;
	input01.type = "text";
	input01.maxlength = "10";
	input01.value = "";
	input01.setAttribute("onchange","validateDate(this);checkDateRange("+id_seq_no+",'F');");
	//input01.setAttribute("onblur","validateDate(this);checkFromToDate("+id_seq_no+");");
	
	var span01 = document.createElement("span")
	span01.className="input-group-text";
	var i01=document.createElement("i");
	i01.className="fa fa-calendar fa-lg";
	
	span01.appendChild(i01);
	div001.appendChild(input01);
	div001.appendChild(span01);
	div01.appendChild(div001);
	
	var td02 = document.createElement("td");
	td02.align='center';
	var div02 = document.createElement("DIV");
	div02.style.width='100px';
	var div002 = document.createElement("DIV");
	div002.className="input-group input-group-sm";
	var input02 = document.createElement("input")
	input02.className="form-control form-control-sm date fmsdtpick";
	input02.name = "to_dt";
	input02.id = "to_dt"+id_seq_no;
	input02.type = "text";
	input02.maxlength = "10";
	input02.value = "";
	input02.setAttribute("onchange","validateDate(this);checkDateRange("+id_seq_no+",'T');");
	//input02.setAttribute("onblur","validateDate(this);checkFromToDate("+id_seq_no+");");
	var span02 = document.createElement("span")
	span02.className="input-group-text";
	var i02=document.createElement("i");
	i02.className="fa fa-calendar fa-lg";
	
	span02.appendChild(i02);
	div002.appendChild(input02);
	div002.appendChild(span02);
	div02.appendChild(div002);
	
	var td03 = document.createElement("td");
	td03.align='center';
	var div03 = document.createElement("DIV");
	div03.style.width='100px';
	var input03 = document.createElement("input")
	input03.name = "csoc";
	input03.id = "csoc"+id_seq_no;
	input03.type = "text";
	input03.className="form-control form-control-sm";
	input03.setAttribute("onblur","checkNumber1(this,10,2);");
	input03.style.textAlign = "right"
	input03.maxlength = "20";
	
	div03.appendChild(input03);
	
	var td05 = document.createElement("td");
	var div05 = document.createElement("DIV");
	div05.align='center';
	var input05 = document.createElement("textarea")
	input05.name = "remark";
	input05.id = "remark"+id_seq_no;
	input05.type = "text";
	input05.className="form-control form-control-sm";
	input05.maxlength = "150";
	input05.value = "";
	input05.cols = "75";
	input05.rows = "1";
	input05.style.width='250px';
	
	div05.appendChild(input05);
	
	var td06 = document.createElement("td");
	td06.className="text-center"
	var input06 = document.createElement("a")
	input06.setAttribute("onclick","removeRow('"+row_new.id+"','"+new_seq_no+"');");
	input06.id = "minus"+new_seq_no;
	var i06=document.createElement("i");
	i06.className="fa fa-minus-circle fa-2x";
	
	var input06_1 = document.createElement("a")
	input06_1.setAttribute("onclick","addrow();");
	input06_1.id = "plus"+new_seq_no;
	var i06_1=document.createElement("i");
	i06_1.className="fa fa-plus-circle fa-2x";
	
	input06.appendChild(i06);
	input06_1.appendChild(i06_1);
	
	td01.appendChild(div01);
	td02.appendChild(div02);
	td03.appendChild(div03);
	td05.appendChild(div05);
	td06.appendChild(input06);
	td06.appendChild(document.createTextNode("      "))
	td06.appendChild(input06_1);
	
	row_new.appendChild(td01);
	row_new.appendChild(td02);
	row_new.appendChild(td03);
	row_new.appendChild(td05);
	row_new.appendChild(td06);
	
	tab_name.appendChild(row_new);
	
	document.getElementById("item_size").value=new_seq_no;
	
	var tempFromDt=document.getElementById("from_dt"+id_seq_no);
	var tempToDt=document.getElementById("to_dt"+id_seq_no);
	
	FmsDatePicker()
	
	if(parseInt(new_seq_no) > 1)
	{
		prev_minus.style.display="none";
		prev_plus.style.display="none";
	}
	else
	{
		prev_plus.style.display="none";
	}
}

function removeRow(row_id, seq_no)
{
	var row_cnt = document.forms[0].item_size.value;
	
	if(parseInt(row_cnt) == parseInt(seq_no))
	{
		if((parseInt(seq_no)-1) > 1)
		{
			document.getElementById("minus"+(parseInt(seq_no)-1)).style.display="";
			document.getElementById("plus"+(parseInt(seq_no)-1)).style.display="";
		}
		else
		{
			document.getElementById("minus"+(parseInt(seq_no)-1)).style.display="none";
			document.getElementById("plus"+(parseInt(seq_no)-1)).style.display="";
		}
		
		if(parseFloat(row_cnt) > 0)
		{
			document.forms[0].item_size.value = parseFloat(row_cnt)-1;
		}
		var row = document.getElementById(row_id);
		row.parentNode.removeChild(row);
	}
	else
	{
		alert("Please First Remove Last Row!!");
	}
}

function doSubmit()
{
	var from_dt = document.forms[0].from_dt;
	var to_dt = document.forms[0].to_dt;
	var csoc = document.forms[0].csoc;
	var u = document.forms[0].u.value;
	
	var msg="";
	var flag=true;
	
	if(from_dt!=null && from_dt!=undefined)
	{
		if(from_dt.length!=undefined)
		{
			for(var i=0;i<from_dt.length;i++)
			{
				if(trim(from_dt[i].value) == "")
				{
					msg+="Enter From Date for Row - "+(parseInt(i)+1)+"!\n";
					flag=false;
				}
				if(trim(to_dt[i].value) == "")
				{
					msg+="Enter To Date for Row - "+(parseInt(i)+1)+"!\n";
					flag=false;
				}
				if(trim(csoc[i].value) == "")
				{
					msg+="Enter CSOC for Row - "+(parseInt(i)+1)+"!\n";
					flag=false;
				}
			}
  		}
		else
		{
			if(trim(from_dt.value) == "")
			{
				msg+="Enter From Date!\n";
				flag=false;
			}
			if(trim(to_dt.value) == "")
			{
				msg+="Enter To Date!\n";
				flag=false;
			}
			if(trim(csoc.value) == "")
			{
				msg+="Enter CSOC!\n";
				flag=false;
			}
		}
 	}
	else
	{
		msg+="Enter Mandatory Data!";
		flag=false;
	}
	
	if(flag)
	{
		var a= confirm("Do you want to Submit Variable CSOC?");
		if(a)
		{
			document.forms[0].submit();
		}
	}
	else
	{
		alert(msg);
	}
}

function checkDateRange(index,dt_flag)
{
	var start_dt=document.forms[0].start_dt.value;
	var end_dt=document.forms[0].end_dt.value;
	var ext_dt=document.forms[0].ext_dt.value;
	
	var eff_dt = ext_dt;
	
	if(trim(ext_dt) == "" || trim(ext_dt) == null)
	{
		eff_dt = end_dt;
	}
	
	var from_dt=document.getElementById("from_dt"+index).value;
	var to_dt=document.getElementById("to_dt"+index).value;
	
	var flag=true;
	if((from_dt!="" && trim(from_dt) != "" && from_dt != null) && (to_dt!="" && trim(to_dt) != "" && to_dt != null))
	{
		if(dt_flag=="F")
		{
			var count = compareDate(from_dt,to_dt);
			if(parseInt(count) == 1)
			{
				alert("From Date should be less or equal To Date!")
				document.getElementById("from_dt"+index).value="";
				flag=false;
			}
		}
		else if(dt_flag=="T")
		{
			var count = compareDate(from_dt,to_dt);
			if(parseInt(count) == 1)
			{
				alert("To Date should be grater or equal From Date!")
				document.getElementById("to_dt"+index).value="";
				flag=false;
			}
		}
	}
	
	if(flag)
	{
		var value = compareDate(start_dt,from_dt);
		var value1 = compareDate(eff_dt,to_dt);
		
		if(parseInt(value) == 1 && parseInt(value1) == 2)
	  	{
			flag=false;
	  	}
		else if(parseInt(value) == 1)
		{
			flag=false;
		}
		else if(parseInt(value1) == 2)
		{
			flag=false;
		}
		
		if(!flag)
		{
			alert("From Date "+from_dt+" & To Date "+to_dt+" not in Range of Cargo Period ("+start_dt+" - "+eff_dt+")!")
			document.getElementById("from_dt"+index).value="";
			document.getElementById("to_dt"+index).value="";
		}
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="dbltcora" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="dateUtil" scope="request"></jsp:useBean>

<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String storage_days = request.getParameter("storage_days")==null?"0":request.getParameter("storage_days");
String storage_ext_days = request.getParameter("storage_ext_days")==null?"0":request.getParameter("storage_ext_days");

String end_dt = dateUtil.getDate(start_dt, storage_days);
String ext_dt =dateUtil.getDate(end_dt, storage_ext_days);
if(storage_ext_days.equals("0") || storage_ext_days.equals("0"))
{
	ext_dt="";
}

String cargo_ref_no = request.getParameter("cargoRef")==null?"":request.getParameter("cargoRef");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_name = request.getParameter("cont_name")==null?"":request.getParameter("cont_name");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String cont_rev = request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
String agmt_rev = request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String buy_sell = request.getParameter("buy_sell")==null?"":request.getParameter("buy_sell");
String cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");

String disp_cont_no = request.getParameter("cargo_number_disp")==null?"":request.getParameter("cargo_number_disp");
String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");

dbltcora.setCallFlag("LTCORA_CONT_CARGO_CSOC");
dbltcora.setComp_cd(owner_cd);
dbltcora.setCounterparty_cd(counterparty_cd);
dbltcora.setContract_type(contract_type);
dbltcora.setAgmt_no(agmt_no);
dbltcora.setAgmt_type(agmt_type);
dbltcora.setCont_no(cont_no);
dbltcora.setCont_rev_no(cont_rev);
dbltcora.setAgmt_rev_no(agmt_rev);
dbltcora.setBuy_sale(buy_sell);
dbltcora.setCargo_no(cargo_no);
dbltcora.init();

Vector VCSOC_SEQ_NO = dbltcora.getVCSOC_SEQ_NO();
Vector VCSOC_FROM_DT = dbltcora.getVCSOC_FROM_DT();
Vector VCSOC_TO_DT = dbltcora.getVCSOC_TO_DT();
Vector VVAR_CSOC = dbltcora.getVVAR_CSOC();
Vector VCSOC_REMARK = dbltcora.getVCSOC_REMARK();

String counterparty_name=dbltcora.getCounterparty_name();
%>
<body>
<form method="post" action="../servlet/Frm_LtcoraMaster">
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
					    	Variable CSOC (<%=counterparty_name %> - <%=cargo_ref_no %>)
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>From Date</th>
										<th>To Date</th>
										<th>CSOC (MMBTU)</th>
										<!-- <th>Status</th> -->
										<th>Remark</th>
										<th></th>
									</tr>
								</thead>
								<tbody id="itemTab">
								<%if(VCSOC_FROM_DT.size() > 0){ %>
									<%for(int i=0;i<VCSOC_FROM_DT.size(); i++){%>
									<tr id="row<%=i+1%>">
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" id="from_dt<%=i%>" value="<%=VCSOC_FROM_DT.elementAt(i)%>" maxLength="10" 
						      						onchange="validateDate(this);checkDateRange('<%=i%>','F');" autocomplete="off">
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div>
				      						</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<div class="input-group input-group-sm" >
						      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" id="to_dt<%=i%>" value="<%=VCSOC_TO_DT.elementAt(i)%>" maxLength="10" 
						      						onchange="validateDate(this);checkDateRange('<%=i%>','T');" autocomplete="off">
						      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
					      						</div>
				      						</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="csoc" id="csoc<%=i%>" style="text-align:right;" value="<%=VVAR_CSOC.elementAt(i)%>" onblur="checkNumber1(this,10,2);">
											</div>
										</td>
										<%-- <td align="center">
											<div style="width:50px;">
												<div class="form-check form-switch">
													<input class="form-check-input" name="active" type="checkbox" role="switch" id="flexSwitchCheckChecked" <%if(VSTATUS.elementAt(i).equals("Y")){%>checked<%}%> onclick="setActiveInactive(this,'<%=i%>');">
												  	<label class="form-check-label" for="flexSwitchCheckChecked" id="lb<%=i%>">
												  		<%if(VSTATUS.elementAt(i).equals("Y")){%>Active<%}else{%>Inactive<%} %>
												  	</label>
												  	<input type="hidden" name="status_flag" id="status_flag<%=i%>" value="<%=VSTATUS.elementAt(i)%>">
												</div>
											</div>
										</td> --%>
										<td align="center">
											<textarea maxlength="500" class="form-control form-control-sm" name="remark" id="remark<%=i%>" cols="75" rows="1" style="width:250px;"><%=VCSOC_REMARK.elementAt(i)%></textarea>
										</td>
										<td align="center">
											<a onclick="removeRow('row<%=i+1%>','<%=i+1%>');" id="minus<%=i+1%>" <%if((i+1) == 1){ %>style="display:none"<%} %><%else if((VCSOC_SEQ_NO.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
												<i class="fa fa-minus-circle fa-2x"></i>
											</a>&nbsp;
											<a onclick="addrow();" id="plus<%=i+1%>" <%if((VCSOC_SEQ_NO.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
												<i class="fa fa-plus-circle fa-2x"></i>
											</a>
										</td>
									</tr>
									<%} %>
								<%}else{ %>	
									<tr>
										<td colspan="6" align="center"><%=utilmsg.infoMessage("<b>Variable CSOC Not Configured!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="d-flex justify-content-between">
						<input type="button" class="btn btn-warning com-btn" value="Reset" onclick="location.reload();">
						<%if(write_access.equals("Y")){ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" onclick="doSubmit();">
						<%}else{ %>
						<input type="button" class="btn btn-warning com-btn" value="Submit" disabled>
						<%} %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="option" value="LTCORA_CONT_CARGO_CSOC">

<input type="hidden" name="start_dt" value="<%=start_dt%>">
<input type="hidden" name="end_dt" value="<%=end_dt%>">
<input type="hidden" name="ext_dt" value="<%=ext_dt%>">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="buy_sell" id="buy_sell" value="<%=buy_sell%>">
<input type="hidden" name="agmt_no" id="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev" id="agmt_rev" value="<%=agmt_rev%>">
<input type="hidden" name="agmt_type" id="agmt_type" value="<%=agmt_type%>">
<input type="hidden" name="contract_type" id="contract_type" value="<%=contract_type%>">
<input type="hidden" name="cont_rev" id="cont_rev" value="<%=cont_rev%>">
<input type="hidden" name="cont_no" id="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cargo_no" id="cargo_no" value="<%=cargo_no%>">
<input type="hidden" name="item_size" id="item_size" value="<%=VCSOC_SEQ_NO.size()%>">

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
</html>