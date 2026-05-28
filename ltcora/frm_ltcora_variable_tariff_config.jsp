<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function addrow()
{
	var max_seq = document.getElementById("item_size").value;
	//alert(max_seq)
	var prev_minus= document.getElementById("minus"+max_seq);
	var prev_plus= document.getElementById("plus"+max_seq);
	var prev_div_till_end= document.getElementById("div_till_end"+max_seq);
	var new_seq_no = parseInt(max_seq)+1;
	var id_seq_no = parseInt(new_seq_no)-1;
	var storage_tariff_unit_nm = document.forms[0].storage_tariff_unit_nm.value;
	//alert(new_seq_no)
	
	var tab_name = document.getElementById("itemTab");
	var row_new = document.createElement("tr"); 
	row_new.id = 'row'+new_seq_no;
	
	
	var td01 = document.createElement("td");
	td01.align='center';
	var div01 = document.createElement("DIV");
	div01.style.width='100px';
	var input01 = document.createElement("input")
	
	input01.name = "from_days";
	input01.id = "from_days"+id_seq_no;
	input01.type = "text";
	input01.className="form-control form-control-sm";
	input01.setAttribute("onblur","checkDateRange("+id_seq_no+",'F')");
	input01.style.textAlign = "right"
	input01.maxlength = "10";
	//input01.setAttribute("onblur","validateDate(this);checkFromToDate("+id_seq_no+");");
	
	//div001.appendChild(span01);
	div01.appendChild(input01);
	
	var td02 = document.createElement("td");
	td02.align='center';
	var div02 = document.createElement("DIV");
	div02.style.width='100px';
	var input02 = document.createElement("input")
	
	input02.name = "to_days";
	input02.id = "to_days"+id_seq_no;
	input02.type = "text";
	input02.className="form-control form-control-sm";
	input02.setAttribute("onblur","checkDateRange("+id_seq_no+",'T')");
	input02.style.textAlign = "right"
	input02.maxlength = "10";
	div02.appendChild(input02);
	
	var divTillEnd = document.createElement("div");
	divTillEnd.id = "div_till_end" + new_seq_no;
	divTillEnd.style.display = "none";

	var checkbox = document.createElement("input");
	checkbox.type = "checkbox";
	checkbox.className = "form-check-input";
	checkbox.name = "till_end";
	checkbox.id = "till_end" + new_seq_no;
	checkbox.value = "Y";
	checkbox.setAttribute("onclick", "disableChk();");

	var label = document.createElement("b");
	label.innerHTML = "Till End";

	divTillEnd.appendChild(checkbox);
	divTillEnd.appendChild(document.createTextNode(" "));
	divTillEnd.appendChild(label);
	
	var td03 = document.createElement("td");
	td03.align='center';
	var div03 = document.createElement("DIV");
	div03.style.width='100px';
	var input03 = document.createElement("input")
	input03.name = "tariff_value";
	input03.id = "tariff_value"+id_seq_no;
	input03.type = "text";
	input03.className="form-control form-control-sm";
	input03.setAttribute("onblur","checkNumber1(this,10,2);checkTariffValue(this);");
	input03.style.textAlign = "right"
	input03.maxlength = "20";
	
	div03.appendChild(input03);
	
	var td04 = document.createElement("td");
	td04.align='center';
	var div04 = document.createElement("DIV");
	div04.innerHTML = storage_tariff_unit_nm.trim();
	
	var td05 = document.createElement("td");
	td05.className="text-center"
	var input05 = document.createElement("a")
	input05.setAttribute("onclick","removeRow('"+row_new.id+"','"+new_seq_no+"');");
	input05.id = "minus"+new_seq_no;
	var i05=document.createElement("i");
	i05.className="fa fa-minus-circle fa-2x";
	
	var input05_1 = document.createElement("a")
	input05_1.setAttribute("onclick","addrow();");
	input05_1.id = "plus"+new_seq_no;
	var i05_1=document.createElement("i");
	i05_1.className="fa fa-plus-circle fa-2x";
	
	input05.appendChild(i05);
	input05_1.appendChild(i05_1);
	
	td01.appendChild(div01);
	var flexContainer = document.createElement("div");
	flexContainer.style.display = "flex";
	flexContainer.style.alignItems = "center";
	flexContainer.style.justifyContent = "center";

	// Add spacing between input and checkbox
	divTillEnd.style.marginLeft = "10px";
	divTillEnd.style.whiteSpace = "nowrap";

	flexContainer.appendChild(div02);
	flexContainer.appendChild(divTillEnd);
	td02.appendChild(flexContainer);
	td03.appendChild(div03);
	td04.appendChild(div04);
	td05.appendChild(input05);
	td05.appendChild(document.createTextNode("      "))
	td05.appendChild(input05_1);
	
	row_new.appendChild(td01);
	row_new.appendChild(td02);
	row_new.appendChild(td03);
	row_new.appendChild(td04);
	row_new.appendChild(td05);
	
	tab_name.appendChild(row_new);
	
	document.getElementById("item_size").value=new_seq_no;
	
	var tempFromDt=document.getElementById("from_days"+id_seq_no);
	var tempToDt=document.getElementById("to_days"+id_seq_no);
	document.getElementById("div_till_end" + new_seq_no).style.display = "";
	
	if(parseInt(new_seq_no) > 1)
	{
		prev_minus.style.display="none";
		prev_plus.style.display="none";
		prev_div_till_end.style.display="none";
	}
	else
	{
		prev_plus.style.display="none";
		prev_div_till_end.style.display="none";
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
			document.getElementById("div_till_end"+(parseInt(seq_no)-1)).style.display="";
		}
		else
		{
			document.getElementById("minus"+(parseInt(seq_no)-1)).style.display="none";
			document.getElementById("plus"+(parseInt(seq_no)-1)).style.display="";
			document.getElementById("div_till_end"+(parseInt(seq_no)-1)).style.display="";
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
	var itemSize = parseInt(document.getElementById("item_size").value);
    var chk = document.getElementById("till_end" + itemSize);
	var from_days = document.forms[0].from_days;
	var to_days = document.forms[0].to_days;
	var tariff_value = document.forms[0].tariff_value;
	var u = document.forms[0].u.value;
	
	var msg="";
	var flag=true;
	
	if(from_days!=null && from_days!=undefined)
	{
		if(from_days.length!=undefined)
		{
			for(var i=0;i<from_days.length;i++)
			{
				if(trim(from_days[i].value) == "")
				{
					msg+="Enter From Days for Row - "+(parseInt(i)+1)+"!\n";
					flag=false;
				}
				if(trim(to_days[i].value) == "") {
				    var itemSize = parseInt(document.getElementById("item_size").value);
				    var tillEndChk = document.getElementById("till_end" + (i + 1));

				    if (!((i + 1) == itemSize && tillEndChk && tillEndChk.checked)) 
				    {
				        msg += "Enter To Days for Row - " + (parseInt(i) + 1) + "!\n";
				        flag = false;
				    }
				}

				if(trim(tariff_value[i].value) == "")
				{
					msg+="Enter Tariff Value for Row - "+(parseInt(i)+1)+"!\n";
					flag=false;
				}
			}
  		}
		else
		{
			if(trim(from_days.value) == "")
			{
				msg+="Enter From Days!\n";
				flag=false;
			}
			var itemSize = parseInt(document.getElementById("item_size").value);
		    var tillEndChk = document.getElementById("till_end1");

		    if(trim(to_days.value) == "")
		    {
		        if (!(itemSize == 1 && tillEndChk && tillEndChk.checked))
		        {
		            msg += "Enter To Days!\n";
		            flag = false;
		        }
		    }
			if(trim(tariff_value.value) == "")
			{
				msg+="Enter Tariff Value!\n";
				flag=false;
			}
		}
 	}
	else
	{
		msg+="Enter Mandatory Data!";
		flag=false;
	}
	
	var daysOverWrite_count = 0;
    var dateRanges = [];

    if (from_days.length != undefined)
    {
    	for (var i = 0; i < from_days.length; i++)
    	{
    		var fromDate = trim(from_days[i].value);
            var toDate = trim(to_days[i].value);
        
            if (fromDate && toDate)
            {
            	if (isDateRangeOverlapping(fromDate, toDate, dateRanges))
            	{
            		daysOverWrite_count++;
                    msg += "Variable Tariff Days Timeline already covered, Please check days range for Row - " + (i + 1) + "!\n";
                    flag = false;
                }
            	else 
            	{
                    dateRanges.push({ from: fromDate, to: toDate });
                }
            }
        }
    } 
    else 
    {
        var fromDate = trim(from_days.value);
        var toDate = trim(to_days.value);
        if (fromDate && toDate)
        {
        	if (isDateRangeOverlapping(fromDate, toDate, dateRanges))
        	{
        		daysOverWrite_count++;
        		msg += "Variable Tariff Days Timeline already covered, Please check days range!\n";
        		flag = false;
        	} 
        	else
        	{
        		dateRanges.push({ from: fromDate, to: toDate });
            }
        }
    }

	if(flag)
	{
		var a= confirm("Do you want to Submit Variable Tariff?");
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

function isDateRangeOverlapping(fromDate, toDate, dateRanges) 
{
    for (var i = 0; i < dateRanges.length; i++) 
    {
        var existingRange = dateRanges[i];
        // Convert to Date objects for comparison
        var existingFrom = new Date(existingRange.from);
        var existingTo = new Date(existingRange.to);
        var newFrom = new Date(fromDate);
        var newTo = new Date(toDate);

        // Check if the new range overlaps with the existing range
        if ((newFrom <= existingTo) && (newTo >= existingFrom))
        {
            return true; // Overlap found
        }
    }
    return false; // No overlap
}

function checkDateRange(index,dt_flag)
{
	var start_dt=document.forms[0].start_dt.value;
	var end_dt=document.forms[0].end_dt.value;
	
	var from_days=document.getElementById("from_days"+index).value;
	var to_days=document.getElementById("to_days"+index).value;
	
	var flag=true;
	var toDays=parseInt(to_days, 10);
	
	var startDate = parseDate(start_dt);
	var endDateForDif = parseDate(end_dt);
	
	var endDate = new Date(startDate);
    endDate.setDate(startDate.getDate() + toDays);
    var year = endDate.getFullYear();
    var month = String(endDate.getMonth() + 1).padStart(2, '0');
    var day = String(endDate.getDate()).padStart(2, '0');
    
    var formattedDate = day+"/"+month+"/"+year;
    
    var splitcn_start_dt = start_dt.split("/");
    var splitTo_end_dt = formattedDate.split("/");
	var splitcn_end_dt = end_dt.split("/");
	
	var temp_to_end_dt = splitTo_end_dt[2]+splitTo_end_dt[1]+splitTo_end_dt[0];
	var temp_cn_end_dt = splitcn_end_dt[2]+splitcn_end_dt[1]+splitcn_end_dt[0];
	var temp_cn_start_dt = splitcn_start_dt[2]+splitcn_start_dt[1]+splitcn_start_dt[0];
	
	const diffTime = Math.abs(endDateForDif - startDate);
	const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

	if((from_days!="" && trim(from_days) != "" && from_days != null) && (to_days!="" && trim(to_days) != "" && to_days != null))
	{
		if(dt_flag=="F")
		{
			var count = compareDate(from_days,to_days);
			if(parseInt(count) == 1)
			{
				alert("From Days should be less or equal To Days!")
				document.getElementById("from_days"+index).value="";
				flag=false;
			}
		}
		else if(dt_flag=="T")
		{
			var count = compareDate(from_days,to_days);
			if(parseInt(count) == 1)
			{
				alert("To Days should be grater or equal From Days!")
				document.getElementById("to_days"+index).value="";
				flag=false;
			}
		}
		
		if(temp_to_end_dt>temp_cn_end_dt)
	   	{
	   		alert("To Days Can not go Beyond Contract end date ("+end_dt+"), maximum allowed To Days is ("+diffDays+")!")
	   		document.getElementById("to_days"+index).value="";
			flag=false;
	   	}
	}
}

function negativeValue(days)
{
	
	if(days.value <= 0)
	{
		alert("Only > Zero Value Allowed!")
		days.value="";
	}
}

function checkTariffValue(input) 
{
    var value = parseFloat(input.value);
    if (isNaN(value)) 
    {
        input.value = "";
        return;
    }

    var index = parseInt(match[0], 10);
    var itemSize = parseInt(document.getElementById("item_size").value);
    var tillEndChk = document.getElementById("till_end" + (index + 1));

    if (value <= 0) 
    {
        if (!((index + 1) === itemSize && tillEndChk && tillEndChk.checked)) 
        {
            alert("Only > Zero Value Allowed!");
            input.value = "";
        }
    }
}


function parseDate(dateStr) 
{
    var parts = dateStr.split('/');
    if (parts.length === 3) 
    {
        var day = parseInt(parts[0], 10);
        var month = parseInt(parts[1], 10) - 1;
        var year = parseInt(parts[2], 10);
        return new Date(year, month, day);
    }
    return null;
}

function disableChk() 
{
    var itemSize = parseInt(document.getElementById("item_size").value);
    var chk = document.getElementById("till_end" + itemSize);
    var toDaysField = document.getElementById("to_days" + (itemSize - 1));
    var plusIcon = document.getElementById("plus" + itemSize);
    var minusIcon = document.getElementById("minus" + itemSize);

    if (chk && chk.checked) 
    {
    	toDaysField.value="";
        toDaysField.disabled = true;
        plusIcon.style.display = "none";
        minusIcon.style.display = "none";
    } 
    else 
    {
        toDaysField.disabled = false;
        plusIcon.style.display = "";
        if (itemSize > 1) 
        {
            minusIcon.style.display = "";
        }
    }
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.ltcora.DataBean_LtcoraMaster" id="ltcora" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev_no = request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev_no = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String buy_sale = request.getParameter("buy_sale")==null?"":request.getParameter("buy_sale");
String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
String storage_tariff_unit = request.getParameter("storage_tariff_unit")==null?"":request.getParameter("storage_tariff_unit");
String storage_tariff = request.getParameter("storage_tariff")==null?"":request.getParameter("storage_tariff");

String cont_start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
String cont_end_dt=request.getParameter("end_dt")==null?"":request.getParameter("end_dt");

ltcora.setCallFlag("LTCORA_VARIABLE_TERIFF");
ltcora.setCounterparty_cd(counterparty_cd);
ltcora.setComp_cd(owner_cd);
ltcora.setAgmt_no(agmt_no);
ltcora.setAgmt_rev_no(agmt_rev_no);
ltcora.setCont_no(cont_no);
ltcora.setCont_rev_no(cont_rev_no);
ltcora.setContract_type(contract_type);
ltcora.setAgreement_type(agreement_type);
ltcora.setBuy_sale(buy_sale);
ltcora.init();

Vector VSEQ_NO = ltcora.getVSEQ_NO();
Vector VFROM_DT = ltcora.getVFROM_DT();
Vector VTO_DT = ltcora.getVTO_DT();
Vector VSTORAGE_RATE = ltcora.getVSTORAGE_RATE();
Vector VTILL_END = ltcora.getVTILL_END();

String storage_tariff_unit_nm="";
if(storage_tariff_unit.equals("1"))
{
	storage_tariff_unit_nm = "INR/MMBTU";
}
else if(storage_tariff_unit.equals("2"))
{
	storage_tariff_unit_nm = "USD/MMBTU";
}
%>
<body onload="disableChk();">
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
					    	Variable Storage Tariff [Contract Period : <%=cont_start_dt %>-<%=cont_end_dt %>]
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th>From Days</th>
										<th>To Days</th>
										<th>Tariff</th>
										<th>Unit</th>
										<th></th>
									</tr>
								</thead>
								<tbody id="itemTab">
								<%if(VFROM_DT.size() > 0){ %>
									<%for(int i=0;i<VFROM_DT.size(); i++){%>
									<tr id="row<%=i+1%>">
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="from_days" id="from_days<%=i%>" value="<%=VFROM_DT.elementAt(i)%>" style="text-align:right;" onblur="negativeValue(this);checkDateRange(<%=i %>,'F');">
				      						</div>
										</td>
										<td align="center">
											<div style="display: flex; align-items: center; justify-content: center;">
												<input type="text" class="form-control form-control-sm" name="to_days" id="to_days<%=i%>" value="<%=VTO_DT.elementAt(i)%>" style="text-align:right; width:100px;" onblur="negativeValue(this); checkDateRange(<%=i %>, 'T');">
										
												<div id="div_till_end<%=i+1%>" style="<%if((VSEQ_NO.size() - 1) == i){ %>margin-left:10px;<%}else{%>display:none;<%} %> white-space: nowrap;">
													<label style="margin: 0; display: flex; align-items: center;">
														<input type="checkbox" class="form-check-input" name="till_end" id="till_end<%=i+1%>" value="Y" onclick="disableChk();" <% if (VTILL_END.elementAt(i).equals("Y")) { %>checked<% } %>>
														<span style="margin-left: 4px;"><b>Till End</b></span>
													</label>
												</div>
											</div>
										</td>
										<td align="center">
											<div style="width:100px;">
												<input type="text" class="form-control form-control-sm" name="tariff_value" id="tariff_value<%=i%>" style="text-align:right;" value="<%=VSTORAGE_RATE.elementAt(i) %>" onblur="checkNumber1(this,10,2);checkTariffValue(this);">
											</div>
										</td>
										<td align="center"><%=storage_tariff_unit_nm %></td>
										<td align="center">
											<a onclick="removeRow('row<%=i+1%>','<%=i+1%>');" id="minus<%=i+1%>" <%if((i+1) == 1){ %>style="display:none"<%} %><%else if((VSEQ_NO.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
												<i class="fa fa-minus-circle fa-2x"></i>
											</a>&nbsp;
											<a onclick="addrow();" id="plus<%=i+1%>" <%if((VSEQ_NO.size() - 1) == i){ %><%}else{%>style="display:none"<%} %>>
												<i class="fa fa-plus-circle fa-2x"></i>
											</a>
										</td>
									</tr>
									<%} %>
								<%}else{ %>	
									<tr>
										<td colspan="4" align="center"><%=utilmsg.infoMessage("<b>Variable Teriff Not Configured!</b>") %></td>
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

<input type="hidden" name="option" value="VARIABLE_TERIFF">

<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="agmt_no" value="<%=agmt_no%>">
<input type="hidden" name="agmt_rev_no" value="<%=agmt_rev_no%>">
<input type="hidden" name="cont_no" value="<%=cont_no%>">
<input type="hidden" name="cont_rev_no" value="<%=cont_rev_no%>">
<input type="hidden" name="contract_type" value="<%=contract_type%>">
<input type="hidden" name="start_dt" value="<%=cont_start_dt%>">
<input type="hidden" name="end_dt" value="<%=cont_end_dt%>">
<input type="hidden" name="agreement_type" value="<%=agreement_type%>">
<input type="hidden" name="buy_sale" value="<%=buy_sale%>">
<input type="hidden" name="storage_tariff_unit_nm" value="<%=storage_tariff_unit_nm%>">
<input type="hidden" name="storage_tariff_unit" value="<%=storage_tariff_unit%>">
<input type="hidden" name="storage_tariff" value="<%=storage_tariff%>">

<input type="hidden" name="item_size" id="item_size" value="<%=VSEQ_NO.size()%>">

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