<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function refresh() {
    var from_dt = document.forms[0].from_dt.value;
    var to_dt = document.forms[0].to_dt.value;
    var temp_from_dt = document.forms[0].temp_from_dt.value;
    var temp_to_dt = document.forms[0].temp_to_dt.value;
    
    var flag = true;
    var msg = "";

    if (from_dt != null && to_dt != null) 
    {
        if (trim(from_dt) != "" && trim(to_dt) != "") 
        {

            var partsFrom = from_dt.split("/");
            var partsTo = to_dt.split("/");

            var fromDate = new Date(partsFrom[2], partsFrom[1] - 1, partsFrom[0]);
            var toDate = new Date(partsTo[2], partsTo[1] - 1, partsTo[0]);

            if (toDate < fromDate) 
            {
                alert("To Date cannot be earlier than From Date.");
                flag =  false;
            }

            var diffMs = toDate - fromDate;
            var diffDays = diffMs / (1000 * 60 * 60 * 24);

            if (diffDays > 180) 
            {
                alert("Date range should not exceed 180 days.");

                flag =  false;
            }
            var u = document.forms[0].u.value;

            if (flag) 
            {
                var url = "rpt_digital_signature_summary.jsp?=" + u + "&from_dt=" + from_dt + "&to_dt=" + to_dt;

                document.getElementById("loading").style.visibility = "visible";
                location.replace(url);
            }

        } 
        else 
        {
            alert("Please Select From and To Dates...");
            document.forms[0].from_dt.value = temp_from_dt;
            document.forms[0].to_dt.value = temp_to_dt;
            flag =  false;
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
	var sysdate = document.forms[0].sysdate.value;
	
	var url = "xls_digital_signature_summary.jsp?fileName=Digital_Signature_Summary "+sysdate+".xls+&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}

function openPdfFile(url)
{
	window.open(url);
}
</script>
<jsp:useBean class="com.etrm.fms.accounting.DataBean_Accounting" id="contract" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

contract.setCallFlag("DIGITAL_SIGNATURE");
contract.setComp_cd(owner_cd);
contract.setFrom_dt(from_dt);
contract.setTo_dt(to_dt);
contract.init();


Vector VCOUNTERPARTY_CD = contract.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = contract.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = contract.getVCOUNTERPARTY_ABBR();

Vector VINVOICE_NO = contract.getVINVOICE_NO();
Vector VINVOICE_TYPE = contract.getVINVOICE_TYPE();
Vector VINVOICE_DT = contract.getVINVOICE_DT();
Vector VPDF_TYPE = contract.getVPDF_TYPE();
Vector VSIGNED_BY = contract.getVSIGNED_BY();
Vector VPDF_NAME = contract.getVPDF_NAME();
Vector VCONT_NO = contract.getVCONT_NO();
Vector VCONTRACT_TYPE = contract.getVCONTRACT_TYPE();
Vector VBU_STATE = contract.getVBU_STATE();
Vector VPDF_FILE_PATH = contract.getVPDF_FILE_PATH();
Vector VDEAL_NO = contract.getVDEAL_NO();
Vector VCONT_REF_NO = contract.getVCONT_REF_NO();
Vector VENT_BY = contract.getVENT_BY();


String context_nm = request.getContextPath();
String server_nm = request.getServerName();
String server_port = ""+request.getServerPort();
String url=CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//";
String file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm+"//"+CommonVariable.work_dir+owner_cd;
%>
<title>Digital Signature</title>
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
						<div class="topheader">Digital Signature Summary Report</div>
						<a>
							<span class="input-group-text">
							 	<i class="fa fa-file-excel-o fa-2x" style="color: green;" onclick="exportToXls();"></i>
							</span>
						</a>
					</div>
				</div>

				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="d-flex justify-content-center">
								<div class="form-group row">
									<div class="col-auto">
										<label class="form-label"><b>From</b></label>
									</div>
									<div class="col">
										<div class="input-group input-group-sm">
											<input type="text"
												class="form-control form-control-sm date fmsdtpick"
												name="from_dt" value="<%=from_dt %>" size="8"
												maxLength="10" onblur="validateDate(this);"
												onchange=""
												autocomplete="off"> <span class="input-group-text"><i
												class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
									<div class="col-auto">
										<label class="form-label"><b>To</b></label>
									</div>
									<div class="col">
										<div class="input-group input-group-sm">
											<input type="text"
												class="form-control form-control-sm date fmsdtpick"
												name="to_dt" value="<%=to_dt %>" size="8" maxLength="10"
												onblur="validateDate(this);"
												onchange=""
												autocomplete="off"> <span class="input-group-text"><i
												class="fa fa-calendar fa-lg"></i></span>
										</div>
									</div>
									<div class="col-auto">
										<input type="button" class="btn btn-warning com-btn"
											value="Apply Filter" onclick="refresh();">
									</div>
								</div>
							</div>
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
								<table class="table table-bordered table-hover" id="filterbysearch">
									<thead>
										<tr>
											<th>Sr#</th>
											<th>Customer<div align="center"><input class="form-control form-control-sm" type="text" id="customer" onkeyup="Search(this,'1');" placeholder="Search.." style="width:100px"/></div></th>
											<th>BU State<div align="center"><input class="form-control form-control-sm" type="text" id="bu_state" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Contract Type<div align="center"><input class="form-control form-control-sm" type="text" id="contract_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Contract#<div align="center"><input class="form-control form-control-sm" type="text" id="contract_no" onkeyup="Search(this,'4');" placeholder="Search.." style="width:100px"/></div></th>
											<th>PDF Name<div align="center"><input class="form-control form-control-sm" type="text" id="pdf_name" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></div></th>
											<th>View PDF</th>
											<th>Invoice No.<div align="center"><input class="form-control form-control-sm" type="text" id="invoice_no" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Invoice Type<div align="center"><input class="form-control form-control-sm" type="text" id="invoice_type" onkeyup="Search(this,'8');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Invoice Date<div align="center"><input class="form-control form-control-sm" type="text" id="invoice_dt" onkeyup="Search(this,'9');" placeholder="Search.." style="width:100px"/></div></th>
											<th>PDF Type<div align="center"><input class="form-control form-control-sm" type="text" id="pdf_type" onkeyup="Search(this,'10');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Signed by<div align="center"><input class="form-control form-control-sm" type="text" id="signed_by" onkeyup="Search(this,'11');" placeholder="Search.." style="width:100px"/></div></th>
											<th>Entered by<div align="center"><input class="form-control form-control-sm" type="text" id="entered_by" onkeyup="Search(this,'12');" placeholder="Search.." style="width:100px"/></div></th>
										</tr>
									</thead>
									<tbody>
									<%int k=0;
										if(VCOUNTERPARTY_CD.size() > 0){ %>
											<%for(int i=0;i<VCOUNTERPARTY_CD.size(); i++){ 
											k+=1;%>
											<tr>
												<td align="center"><%= k %></td>
												<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
												<td align="center"><%= VBU_STATE.elementAt(i) %></td>
												<td align="center"><%= VCONTRACT_TYPE.elementAt(i) %></td>
												<%if (VINVOICE_TYPE.elementAt(i).equals("DERIVATIVE INVOICE")) { %>
												<td align="center"><div style="width:400px; word-wrap: break-word; white-space: normal;"><%=VDEAL_NO.elementAt(i) %><br><% if(!VCONT_REF_NO.elementAt(i).equals("") && VCONT_REF_NO.elementAt(i)!=null){%>[<%=VCONT_REF_NO.elementAt(i) %>] <%} %></div></td>
												<%}else{ %>
												<td align="center"><%=VDEAL_NO.elementAt(i) %><br><% if(!VCONT_REF_NO.elementAt(i).equals("") && VCONT_REF_NO.elementAt(i)!=null){%>[<%=VCONT_REF_NO.elementAt(i) %>] <%} %></td>
												<%}  %>
												<td><%= VPDF_NAME.elementAt(i) %></td>
												<td align="center">
													<i class="fa fa-file-pdf-o fa-2x" aria-hidden="true" style="color: red;"
														onclick="openPdfFile('<%=file_url%><%=VPDF_FILE_PATH.elementAt(i)%><%=VPDF_NAME.elementAt(i)%>')">
													</i>
												</td>
												<td align="center"><%= VINVOICE_NO.elementAt(i) %></td>
												<td align="center"><%= VINVOICE_TYPE.elementAt(i) %></td>
												<td align="center"><%= VINVOICE_DT.elementAt(i) %></td>
												<td align="center"><%= VPDF_TYPE.elementAt(i) %></td>
												<td align="center"><%= VSIGNED_BY.elementAt(i) %></td>
												<td align="center"><%= VENT_BY.elementAt(i) %></td>	
											</tr>
										<%}%> 
									<%}else{ %>
										<tr>
											<td colspan="25" align="center"><%=utilmsg.infoMessage("<b>Digital Signature Data Not Available for Selected Period!</b>") %></td>
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

<input type="hidden" name="sysdate" value="<%=sysdate%>"> 
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
<input type="hidden" name="temp_from_dt" value="<%=from_dt%>">
<input type="hidden" name="temp_to_dt" value="<%=to_dt%>">
</form>
</body>
</html>