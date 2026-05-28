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
	
	var u = document.forms[0].u.value;
	var sel_bu = document.forms[0].sel_bu.value;//RG20250922 for adding BU filter
	if(from_dt!=null && to_dt!=null)
   	{
		if(trim(from_dt)!="" && trim(to_dt)!="")
	   	{
			var value = compareDate(from_dt,to_dt);
			if(value!=1)
			{
				var url = "rpt_dlng_allocation_to_customer.jsp?counterparty_cd="+counterparty_cd+
						"&u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt+"&sel_bu_plant="+sel_bu;
			
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

function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	var sel_bu = document.forms[0].sel_bu.value;//RG20250922 for adding BU filter
	var sel_bu_plant_abbr=document.forms[0].sel_bu[document.forms[0].sel_bu.selectedIndex].text;
	
	var url = "xls_dlng_allocation_to_customer.jsp?counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt+"&sel_bu_plant="+sel_bu+"&sel_bu_plant_abbr="+sel_bu_plant_abbr; //RG20250922

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String sel_bu_plant=request.getParameter("sel_bu_plant")==null?"0":request.getParameter("sel_bu_plant");//RG20250922 for adding BU filter

dlng.setCallFlag("DLNG_ALLOCATION_TO_CUSTOMER");
dlng.setComp_cd(owner_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setBu_plant(sel_bu_plant);//RG20250922 for adding BU filter
dlng.init();

Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();

Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_NM = dlng.getVCOUNTERPARTY_PLANT_NM();

Vector VGAS_DT = dlng.getVGAS_DT();
Vector VQTY_MMBTU = dlng.getVQTY_MMBTU();
Vector VQTY_SCM = dlng.getVQTY_SCM();
Vector VTOTAL_QTY_MMBTU = dlng.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_SCM = dlng.getVTOTAL_QTY_SCM();

Vector VINDEX = dlng.getVINDEX();

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
				    		DLNG Customer Allocation (By Plant)
	   	 				</div>
	   	 				<div>
							<div class="btn-group" onclick="exportToXls();">
								<label><i class="fa fa-file-excel-o fa-2x excel_icon"></i></label>
							</div>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
<!-- 						<div class="col-sm-3 col-xs-3 col-md-3"></div> --><!-- RG20250924 commented due to alignment issues  -->
						<div class="col-sm-3 col-xs-3 col-md-3">
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>From</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
								<div class="col-auto">
									<label class="form-label"><b>To</b></label>
								</div>
								<div class="col">
				      				<div class="input-group input-group-sm" >
			      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" 
			      						onblur="validateDate(this);" onchange="validateDate(this);" autocomplete="off">
			      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
		      						</div>
				    			</div>
							</div>
						</div>
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Customer</b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="counterparty_cd" >
										<option value="0">--All--</option>
										<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
										<%} %>
									</select>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
								</div>
							</div>
						</div>
						<!-- RG20250922 for adding BU wise filter-->
						<div class="col-sm-3 col-xs-3 col-md-3">  
							<div class="form-group row">
								<div class="col-auto">
									<label class="form-label"><b>Business Unit </b></label>
								</div>
								<div class="col">
									<select class="form-select form-select-sm" name="sel_bu" >
									<option value="0">--All--</option>
									 <%for(int i=0; i<VBU_PLANT_SEQ_NO.size(); i++){ %>
									<option value="<%=VBU_PLANT_SEQ_NO.elementAt(i)%>"><%=VBU_PLANT_ABBR.elementAt(i)%></option>
									<%} %> 
									</select>
									<script>document.forms[0].sel_bu.value="<%=sel_bu_plant%>"</script>
								</div>
							</div>
						</div>
						<div class="col-md-3 col-sm-3 col-xs-3">
							<div class="form-group row">
								<div class="col-auto">
									<input type="button" class="btn btn-warning com-btn" value="Apply Filters" onclick="refresh();">
					  			</div>
					  		</div>
					  	</div>
						<!-- RG20250922 -->
						<div class="col-sm-3 col-xs-3 col-md-3"></div>
					</div>
				</div>
				<div class="card-body cdbody">
				<%if(VCOUNTERPARTY_CD.size()>0){ %>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
						<%int k=0,l=0,p=0;
						for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ 
							int index=Integer.parseInt(""+VINDEX.elementAt(i));
							int plant_size=((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size();
						%>
							<div class="accordion">
								<div class="accordion-item accor_item">
									<h2 class="accordion-header" id="heading<%=i%>">
										<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=i%>" aria-expanded="false" aria-controls="collapse<%=i%>">
							    			<%=VCOUNTERPARTY_ABBR.elementAt(i)%>&nbsp;-&nbsp;<%=VCOUNTERPARTY_NM.elementAt(i)%>
							      		</button>
							    	</h2>
							    	<div id="collapse<%=i%>" class="accordion-collapse collapse" aria-labelledby="heading<%=i%>">
							      		<div class="accordion-body accor-body">
							      			<div class="row">
												<div class="table-responsive">
													<table class="table table-bordered table-hover">
														<thead>
															<tr>
																<th rowspan="3">Gas Day</th>
																<th rowspan="2" colspan="2">Total Quantity Supplied</th>
																<th colspan="<%=plant_size*2%>">Total Quantity Supplied To Plant</th>
															</tr>
															<tr>
																<%for(int j=0;j<plant_size;j++){ %>
																<th colspan="2"><%=((Vector) VCOUNTERPARTY_PLANT_NM.elementAt(i)).elementAt(j)%></th>
																<%} %>
															</tr>
															<tr>
																<th>MMBTU</th>
																<th>MT</th>
																<%for(int j=0;j<plant_size;j++){ %>
																<th>MMBTU</th>
																<th>MT</th>
																<%} %>
															</tr>
														</thead>
														<tbody>
														<%int n=0;
														for(k=k;k<VGAS_DT.size();k++){ 
															n+=1;
														%>
															<tr>
																<td align="center"><%=VGAS_DT.elementAt(k)%></td>
																<%int m=0;
																for(l=l;l<VQTY_MMBTU.size();l++){ 
																	m+=1;
																%>
																	<td align="right"><%=VQTY_MMBTU.elementAt(l)%></td>
																	<td align="right"><%=VQTY_SCM.elementAt(l)%></td>
																	<%if((plant_size+1) == m){
																		l++;
																		break;
																	} %>
																<%} %>
															</tr>
															<%if(index == n){%>
															<tr style="font-weight:bold;">
																<td align="right">Total&nbsp;:&nbsp;</td>
																<%int o=0;
																for(p=p;p<VTOTAL_QTY_MMBTU.size();p++){ 
																	o+=1;
																%>
																	<td align="right"><%=VTOTAL_QTY_MMBTU.elementAt(p)%></td>
																	<td align="right"><%=VTOTAL_QTY_SCM.elementAt(p)%></td>
																	<%if((plant_size+1) == o){
																		p++;
																		break;
																	} %>
																<%} %>
															</tr>
																<%k++;
																break;
															} %>
														<%} %>
														</tbody>
													</table>
												</div>
											</div>
							      		</div>
							      	</div>
								</div>
							</div>
						<%} %>
						</div>
					</div>
				<%}else{ %>
					<div align="center">
						<%=utilmsg.infoMessage("<b>Allocation not Done for the Selected Date Range!</b>")%>
					</div>
				<%} %>
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