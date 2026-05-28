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
	var segmentType = document.forms[0].segmentType.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var flag=true;
	var msg="";
	
	var u = document.forms[0].u.value;
	
	if(flag)
	{
		var url = "rpt_dlng_tcq_variation.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
				"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt;
	
		document.getElementById("loading").style.visibility = "visible";
		location.replace(url);
	}
	else
	{
		alert(msg);
	}
}

</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
String firstDate="01/"+sysdate.substring(3, sysdate.length());

String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?firstDate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

dlng.setCallFlag("DLNG_TCQ_VARIATION");
dlng.setComp_cd(owner_cd);
dlng.setSegmentType(segmentType);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.init();

Vector VSEGMENT = dlng.getVSEGMENT();
Vector VSEGMENT_TYPE = dlng.getVSEGMENT_TYPE();
Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VTEMP_SEGMENT = dlng.getVTEMP_SEGMENT();
Vector VINDEX = dlng.getVINDEX();
Vector VTEMP_SEGMENT_TYPE = dlng.getVTEMP_SEGMENT_TYPE();
Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();
Vector VCONT_REF = dlng.getVCONT_REF();
Vector VSIGNING_DT = dlng.getVSIGNING_DT();
Vector VSTART_DT = dlng.getVSTART_DT();
Vector VEND_DT = dlng.getVEND_DT();
Vector VIS_ALLOCATED = dlng.getVIS_ALLOCATED();
Vector VCONT_STATUS = dlng.getVCONT_STATUS();
Vector VFINAL_TCQ = dlng.getVFINAL_TCQ();
Vector VTCQ = dlng.getVTCQ();
Vector VVARIATION_TCQ = dlng.getVVARIATION_TCQ();
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
				    		DLNG Contract TCQ Variation 
	   	 				</div>
	   	 				<a href="../dlng/xls_dlng_tcq_variation.jsp?fileName=DlngTcqVariationReport.xls&company_cd=<%=comp_cd %>&from_dt=<%=from_dt %>&to_dt=<%=to_dt %>&segmentType=<%=segmentType %>&counterparty_cd=<%=counterparty_cd %>" download="DLNG TCQ Variation Report.xls">
						 	<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
						</a>
				    </div>
				</div>      	
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Contract</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="segmentType" onchange="refresh()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VSEGMENT.size();i++){ %>
										<option value="<%=VSEGMENT_TYPE.elementAt(i)%>"><%=VSEGMENT.elementAt(i)%></option>
										<%} %>
									</select>
									<script>document.forms[0].segmentType.value="<%=segmentType%>"</script>
								</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh()">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
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
			      						onchange="validateDate(this);checkDateRange(this,document.forms[0].to_dt);refresh();" onblur="validateDate(this);checkDateRange(this,document.forms[0].to_dt);"  >
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onchange="validateDate(this);checkDateRange(document.forms[0].from_dt,this);refresh();" onblur="validateDate(this);checkDateRange(document.forms[0].from_dt,this);"  >
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
					</div>
				</div>
				<div class="card-body cdbody">
					<%int i=0;int k=0;
					for(int j=0; j<VTEMP_SEGMENT.size(); j++){ 
						int index = Integer.parseInt(""+VINDEX.elementAt(j));
					if(j!=0)
					{
					%>
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
						&nbsp;
						</div>
					</div> 
					<%} %>
					<div class="row m-b-5">
						<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <%=VTEMP_SEGMENT.elementAt(j) %></label>
					</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="filterbysearch_<%=j%>">
								<thead>
									<tr>
										<th>Sr#</th>
										<th>Customer<div align="center"><input class="form-control form-control-sm" type="text" id="contCp_<%=j %>" onkeyup="Search(this,'1','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
										<th>Contract#<div align="center"><input class="form-control form-control-sm" type="text" id="contNum_<%=j %>" onkeyup="Search(this,'2','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
										<%if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("X")){ %>
										<th>Trade Ref#<div align="center"><input class="form-control form-control-sm" type="text" id="contTrade_<%=j %>" onkeyup="Search(this,'3','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
										<%}else{ %>
										<th>Contract Ref#<div align="center"><input class="form-control form-control-sm" type="text" id="contRef_<%=j %>" onkeyup="Search(this,'3','<%=j %>');" placeholder="Search.." style="width:100px"/></div></th>
										<%} %>
										<th>Signing Date</th>
										<th>Contract Period</th>
										<!-- <th>Contract Closure Date</th> -->
										<th>Status</th>	
										<th>Final TCQ <br>(MMBTU)</th>																			
										<th>Contractual TCQ<br>(MMBTU)</th>
										<th>Variation<br>(MMBTU)</th>
									</tr>
								</thead>
								<tbody>
								<%k=0;
								if(index > 0){ %>
									<%for(i=i;i<VCOUNTERPARTY_CD.size(); i++){ 
									k+=1;
									%>
										<tr>
											<td align="center"><%=k%></td>
											<td><%=VCOUNTERPARTY_NM.elementAt(i)%></td>
											<td><%=VDIS_CONT_MAPPING.elementAt(i)%></td>
											<td><%=VCONT_REF.elementAt(i)%></td>
											<td><%=VSIGNING_DT.elementAt(i)%></td>
											<td><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<!-- <td></td> -->
											<td <%if(VIS_ALLOCATED.elementAt(i).equals("Y")){ %>style="background:#99ffcc;"<%}else{ %>style="background:#ffffcc;"<%} %>>
												<%=VCONT_STATUS.elementAt(i)%>
											</td>
											<td align="right"><%=VFINAL_TCQ.elementAt(i) %></td>
											<td align="right"><%=VTCQ.elementAt(i) %></td>
											<td align="right"><%=VVARIATION_TCQ.elementAt(i) %></td>
										</tr>
										<%if(k==index)
										{
											i=i+1;
											break;
										}%>
									<%} %>
								<%}else{ %>
									<tr>
										<td colspan="22" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<%} %>
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

</form>
<script>
function Search(obj, indx, j) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("filterbysearch_"+j);
  	
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