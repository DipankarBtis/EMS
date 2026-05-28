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
	
	var u = document.forms[0].u.value;
	var sel_bu = document.forms[0].sel_bu.value;//RG20250922 for adding BU filter 
	var sel_bu_plant_abbr=document.forms[0].sel_bu[document.forms[0].sel_bu.selectedIndex].text;
	if(from_dt!=null && to_dt!=null)
   	{
		if(trim(from_dt)!="" && trim(to_dt)!="")
	   	{
			var value = compareDate(from_dt,to_dt);
			if(value!=1)
			{
				var url = "rpt_dlng_customer_allocation_contract_wise.jsp?counterparty_cd="+counterparty_cd+
						"&u="+u+"&segmentType="+segmentType+"&from_dt="+from_dt+"&to_dt="+to_dt+"&sel_bu_plant="+sel_bu+"&sel_bu_plant_abbr="+sel_bu_plant_abbr; //RG20250922
			
				document.getElementById("loading").style.visibility = "visible";
				location.replace(url);
			}
			else
			{
				alert("Please ensure From Date <= To Date !");
			}
		}else
	   	{ 
	    	alert("Please Select From and To Dates...");
	   	}	
   	}else
   	{ 
    	alert("Please Select From and To Dates...");
   	}
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String sel_bu_plant=request.getParameter("sel_bu_plant")==null?"0":request.getParameter("sel_bu_plant");//RG20250922 for adding BU filter
String sel_bu_plant_abbr=request.getParameter("sel_bu_plant_abbr")==null?"":request.getParameter("sel_bu_plant_abbr");//RG20250922 for adding BU filter

dlng.setCallFlag("DLNG_ALLOCATION_CONTRACT_WISE");
dlng.setComp_cd(owner_cd);
dlng.setSegmentType(segmentType);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.setBu_plant(sel_bu_plant);//RG20250922 for adding BU filter
dlng.init();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();
Vector VDIS_CONT_MAPPING = dlng.getVDIS_CONT_MAPPING();
Vector VCONT_REF = dlng.getVCONT_REF();
Vector VSTART_DT = dlng.getVSTART_DT();
Vector VEND_DT = dlng.getVEND_DT();
Vector VAGMT_BASE = dlng.getVAGMT_BASE();

Vector VALLOCATION_DATA = dlng.getVALLOCATION_DATA();
Vector VCOLOR = dlng.getVCOLOR();

Vector VSEGMENT = dlng.getVSEGMENT();
Vector VSEGMENT_TYPE = dlng.getVSEGMENT_TYPE();
Vector VCONTRACT_TYPE_NM = dlng.getVCONTRACT_TYPE_NM();
Vector VTOTAL_MMBTU = dlng.getVTOTAL_MMBTU();
Vector VTOTAL_MT = dlng.getVTOTAL_MT();
Vector VBU_PLANT_ABBR=dlng.getVBU_PLANT_ABBR();//RG20250922
Vector VBU_PLANT_SEQ_NO=dlng.getVBU_PLANT_SEQ();//RG20250922
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
					    	DLNG Customer Allocation - Contract Wise
					    </div>
					    <a href="../dlng/xls_dlng_customer_allocation_contract_wise.jsp?fileName=DLNG Customer Allocation Contract Wise <%=sysdate %>.xls&segmentType=<%=segmentType%>&counterparty_cd=<%=counterparty_cd %>&from_dt=<%=from_dt%>&to_dt=<%=to_dt %>&sysdate=<%=sysdate%>&buplant_abbr=<%=sel_bu_plant_abbr%>&sel_bu_plant=<%=sel_bu_plant %>" download="DLNG Customer Allocation Contract Wise">
					 		<span class="input-group-text"><i style="color: green;" class="fa fa-file-excel-o fa-2x"></i></span>
					 	</a>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="counterparty_cd" >
										<option value="0">--Select--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>				
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Contract</b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="segmentType" >
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
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col-auto">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
						<!-- RG20250924 for adding BU wise filter-->
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Business Unit </b></label>
								</div>
								<div class="col-auto">
									<select class="form-select form-select-sm" name="sel_bu" >
									<option value="0">--Select--</option>
									  <%for(int i=0; i<VBU_PLANT_SEQ_NO.size(); i++){ %>
									<option value="<%=VBU_PLANT_SEQ_NO.elementAt(i)%>"><%=VBU_PLANT_ABBR.elementAt(i)%></option>
									<%} %> 
									</select>
									 <script>document.forms[0].sel_bu.value="<%=sel_bu_plant%>"</script> 
								</div>
							</div>
						</div>
						<div class="col-md-2 col-sm-2 col-xs-2">
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
					  			</div>
					  		</div>
					  	</div>
						<!-- RG20250922 -->
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th valign="middle">Customer</th>
									<%for(int i=0; i<VCOUNTERPARTY_CD.size(); i++){ %>
										<th colspan="2"><%=VCOUNTERPARTY_NM.elementAt(i) %></th>
									<%} %>
									</tr>
									<tr>
										<th valign="middle">Contract Type</th>
									<%for(int i=0; i<VCONTRACT_TYPE_NM.size(); i++){ %>
										<th colspan="2"><%=VCONTRACT_TYPE_NM.elementAt(i) %></th>
									<%} %>
									</tr>
									<tr>
										<th valign="middle">Contract#</th>
									<%for(int i=0; i<VDIS_CONT_MAPPING.size(); i++){ %>
										<th colspan="2"><%=VDIS_CONT_MAPPING.elementAt(i) %>
										<%if(VAGMT_BASE.elementAt(i).equals("D")){ %>&nbsp;<font style="background:#a6ff4d">(DLV)</font><%} %>
										<br>[<%=VCONT_REF.elementAt(i)%>]</th>
									<%} %>
									</tr>
									<tr>
										<th valign="middle">Contract Period</th>
									<%for(int i=0; i<VSTART_DT.size(); i++){ %>
										<th colspan="2"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></th>
									<%} %>
									</tr>
									<tr style="background: var(--header_color); color: var(--header_font_color);">
										<th valign="middle">Gas Day</th>
									<%for(int i=0; i<VSTART_DT.size(); i++){ %>
										<th>MMBTU</th>
										<th>MT</th>
									<%} %>
									</tr>
								</thead>
								<tbody>
								<%int k=0;
								int index=(VCOUNTERPARTY_CD.size()*2)+1;%>
								<%for(int j=0; j<VALLOCATION_DATA.size(); j++){ 
									k=k+1;
								%>
								<%if(k==1){ %><tr><%} %>
										<%if(k==1){%>
										<td align="center" style="background:<%=VCOLOR.elementAt(j)%>;"><b><%=VALLOCATION_DATA.elementAt(j)%></b></td>
										<%}else{%>
										<td align="right" style="background:<%=VCOLOR.elementAt(j)%>;"><%=VALLOCATION_DATA.elementAt(j)%></td>
										<%} %>	
									<%if(k==index){ %>
										</tr>
									<%	k=0;
										continue;
									}%>
								<%}%>
									<tr>
										<td align="right"><b>Total:</b></td>
										<%for(int i=0; i<VSTART_DT.size(); i++){ %>
											<td align="right"><b><%=VTOTAL_MMBTU.elementAt(i) %></b></td>
											<td align="right"><b><%=VTOTAL_MT.elementAt(i) %></b></td>
										<%} %>
									</tr>
								</tbody>
							</table>
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

</form>
</body>
</html>