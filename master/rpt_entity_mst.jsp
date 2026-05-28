<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20230321 : Report for Entity Master-->
<head>
<script>
function refresh(opration)
{
	var counterparty_cd ="0";
	if(opration == "MODIFY")
	{
		counterparty_cd = document.forms[0].counterparty_cd.value;
	}
	var entity_role = document.forms[0].entity_role.value;
	var prev_entity_role = document.forms[0].prev_entity_role.value;
	
	if(entity_role != prev_entity_role)
	{
		counterparty_cd ="0";
	}
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_entity_mst.jsp?opration="+opration+"&u="+u+"&counterparty_cd="+counterparty_cd+"&entity_role="+entity_role;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
}

function multiSheetGen()
{
	var entity_role = document.forms[0].entity_role.value;
	var u = document.forms[0].u.value;
	
	
	if(entity_role!="0")
	{
		document.forms[0].sel_entity_role.value=entity_role;
		var counterparty_cd = document.forms[0].counterparty_cd.value;
		var entity_role = document.forms[0].entity_role.value; 

		var url = "xls_all_entity_mst.jsp?fileName=Entity_Master_Report.xls&u="+u+"&counterparty_cd="+counterparty_cd+"&entity_role="+entity_role;
		location.replace(url);
	}
	else
	{
		alert("Please Select Entity Role");
	}
}
function downloadFile()
{
	var entity_role = document.forms[0].entity_role.value;
	var comp_cd = document.forms[0].comp_cd.value;
	var entity_role_nm="";
	if(entity_role!="0")
	{
		if(entity_role == "B" ) {
			entity_role_nm = ("Business Owner"); 
		}
		else if (entity_role==("C")){
			entity_role_nm = ("Customer"); 
		}
		else if (entity_role==("T")){
			entity_role_nm = ("Trader"); 
		}
		else if (entity_role==("R")){
			entity_role_nm = ("Trasporter"); 
		}
		/* by HP
		var multiSheetDwn = confirm("Do you want to download multisheet excel for "+entity_role_nm+" ?")
		if(multiSheetDwn==true)
		{
			document.getElementById("multisheetIcon").href = "../work_data"+comp_cd+"/"+"All_"+entity_role_nm+"_Entity_Report.xls";
		}
		*/
	}
}
function exportToXls()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var entity_role = document.forms[0].entity_role.value; 
	
	var url = "xls_entity_mst.jsp?fileName=Entity_Master_Report.xls&counterparty_cd="+counterparty_cd+"&entity_role="+entity_role;

	location.replace(url);
}
</script>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.master.DB_Counterparty_Report" id="dbcounterpty" scope="request"></jsp:useBean>

<%
String opration=request.getParameter("opration")==null?"MODIFY":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");

if(entity_role.equals("B"))
{
	if(!owner_cd.equals(""))
	{
		counterparty_cd=owner_cd;
	}
}
if(entity_role.equals("G"))
{
	counterparty_cd="1";
}

dbcounterpty.setCallFlag("ENTITY_MASTER");
dbcounterpty.setOpration(opration);
dbcounterpty.setCounterparty_cd(counterparty_cd);
dbcounterpty.setEntity_role(entity_role);
dbcounterpty.setComp_cd(owner_cd);
dbcounterpty.init();

Vector VCOUNTERPARTY_CD = dbcounterpty.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = dbcounterpty.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = dbcounterpty.getVCOUNTERPARTY_ABBR();
Vector VPLANT_SEQ_NO = dbcounterpty.getVPLANT_SEQ_NO(); 
Vector VPLANT_EFF_DT = dbcounterpty.getVPLANT_EFF_DT();
Vector VPLANT_NAME = dbcounterpty.getVPLANT_NAME();
Vector VPLANT_ABBR = dbcounterpty.getVPLANT_ABBR();
Vector VPLANT_ADDR = dbcounterpty.getVPLANT_ADDR();
Vector VPLANT_STATE = dbcounterpty.getVPLANT_STATE();
Vector VPLANT_ZONE = dbcounterpty.getVPLANT_ZONE();
Vector VPLANT_ZONE_NM = dbcounterpty.getVPLANT_ZONE_NM();
Vector VPLANT_CITY = dbcounterpty.getVPLANT_CITY();
Vector VPLANT_PIN = dbcounterpty.getVPLANT_PIN();
Vector VPLANT_SECTOR = dbcounterpty.getVPLANT_SECTOR();
Vector VPLANT_SECTOR_NM = dbcounterpty.getVPLANT_SECTOR_NM();
Vector VPLANT_STATUS = dbcounterpty.getVPLANT_STATUS();
Vector VTAX_ID = dbcounterpty.getVTAX_ID(); 

Vector VBU_PLANT_SEQ_NO = dbcounterpty.getVBU_PLANT_SEQ_NO();
Vector VBU_PLANT_NAME = dbcounterpty.getVBU_PLANT_NAME(); 
Vector VBU_PLANT_ABBR = dbcounterpty.getVBU_PLANT_ABBR(); 
Vector VBU_PLANT_ADDR = dbcounterpty.getVBU_PLANT_ADDR(); 
Vector VBU_PLANT_CITY = dbcounterpty.getVBU_PLANT_CITY(); 
Vector VBU_PLANT_PIN = dbcounterpty.getVBU_PLANT_PIN(); 
Vector VBU_PLANT_STATE = dbcounterpty.getVBU_PLANT_STATE(); 
Vector VBU_PLANT_ZONE = dbcounterpty.getVBU_PLANT_ZONE(); 
Vector VBU_PLANT_EFF_DT = dbcounterpty.getVBU_PLANT_EFF_DT(); 
Vector VBU_TAX_ID = dbcounterpty.getVBU_TAX_ID(); 

String reg_eff_dt=dbcounterpty.getReg_eff_dt();
String address=dbcounterpty.getAddress();
String city=dbcounterpty.getCity();
String pin=dbcounterpty.getPin();
String state=dbcounterpty.getState();
String zone=dbcounterpty.getZone();
String country=dbcounterpty.getCountry();
String phone=dbcounterpty.getPhone();
String mobile=dbcounterpty.getMobile();
String alt_phone=dbcounterpty.getAlt_phone();
String fax1=dbcounterpty.getFax1();
String fax2=dbcounterpty.getFax2();
String email=dbcounterpty.getEmail();

String name=dbcounterpty.getName();
String abbr=dbcounterpty.getAbbr();
String eff_dt=dbcounterpty.getEff_dt();
String pan_no=dbcounterpty.getPan_no();
String pan_dt=dbcounterpty.getPan_dt();
String notes=dbcounterpty.getNotes();
String clearance=dbcounterpty.getClearance();
String kyc_flg=dbcounterpty.getKyc_flg();
String igx_flg=dbcounterpty.getIgx_flg();
String business_unit=dbcounterpty.getBusiness_unit();
String status=dbcounterpty.getStatus();
String category=dbcounterpty.getCategory();
String web_addr=dbcounterpty.getWeb_addr();
String requester_approver_note=dbcounterpty.getRequester_approver_note();

String entity = dbcounterpty.getEntity();
String color = dbcounterpty.getColor();
String Tax_id = dbcounterpty.getTax_id();

String bank_eff_dt=dbcounterpty.getBank_eff_dt();
String bank_name=dbcounterpty.getBank_name();
String bank_account_no=dbcounterpty.getBank_account_no();
String bank_branch=dbcounterpty.getBank_branch();
String ifsc_code=dbcounterpty.getIfsc_code();
String bank_state=dbcounterpty.getBank_state();
String bank_formula=dbcounterpty.getBank_formula();

String[] REG_EFF_DT=dbcounterpty.getREG_EFF_DT();
String[] ADDRESS_FLAG=dbcounterpty.getADDRESS_FLAG();
String[] ADDRESS=dbcounterpty.getADDRESS();
String[] CITY=dbcounterpty.getCITY();
String[] PIN=dbcounterpty.getPIN();
String[] STATE=dbcounterpty.getSTATE();
String[] ZONE=dbcounterpty.getZONE();
String[] COUNTRY=dbcounterpty.getCOUNTRY();
String[] PHONE=dbcounterpty.getPHONE();
String[] MOBILE=dbcounterpty.getMOBILE();
String[] ALT_PHONE=dbcounterpty.getALT_PHONE();
String[] FAX1=dbcounterpty.getFAX1();
String[] FAX2=dbcounterpty.getFAX2();
String[] EMAIL=dbcounterpty.getEMAIL();

String entity_role_nm = "";
%>
<body>
<%@ include file="../home/header.jsp"%>
<form method="post" action="../servlet/Frm_master">
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
							Entity Master Report
						</div>						
				    	<div class="row justify-content-end">
							<div class="col-auto">
						 		<span class="input-group-text"><a id="multisheetIcon"><i  <%if(counterparty_cd.equals("0")){ %>title="Multisheet Export To Excel" onclick="multiSheetGen();downloadFile();" <%}else{%>onclick="exportToXls();" title="Export To Excel"<%} %>style="color: green;" class="fa fa-file-excel-o fa-2x"></i></a></span>
							</div>						
							<div class="col-auto">
							 	<div class="btn-group">
									<select class="btn btn-outline-secondary btngrp <%if(!entity_role.equals("0")){%>btnactive<%}%>" name="entity_role" onchange="refresh('MODIFY');">
										<option value="0">Select Entity Roles</option>
										<option value="C">Customer</option>
						    			<option value="T">Trader</option>
						    			<option value="R">Transporter</option>
						    			<option value="V">Vessel Agent</option>
						    			<option value="H">Custom House Agent</option>
						    			<option value="S">Surveyor</option>
						    			<option value="B">Business Owner</option>
						    			<option value="G">Gas Exchange</option>
									</select>
								</div>
							</div>
						</div>
					</div>
						<script>
							document.forms[0].entity_role.value="<%=entity_role%>"
						</script>						  	
					</div>
				<div class="card-body cdbody">
					<div class="row m-b-5">
						<div class="col-sm-2 col-xs-2 col-md-2">  
							<div class="form-group row">
				    			<label class="form-label"><b>Select Counterparty</b></label>
				  			</div>
						</div>
						<div class="col-sm-4 col-xs-4 col-md-4">  
							<div class="form-group row">
				    			<div class="col-sm-12 col-xs-12 col-md-12">
				      				<select class="form-select form-select-sm" name="counterparty_cd" onchange="refresh('<%=opration%>');" id="select_box">
										<option value="0">--Select--</option>
										<%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
										<option value="<%=VCOUNTERPARTY_CD.elementAt(i)%>"><%=VCOUNTERPARTY_ABBR.elementAt(i)%> - <%=VCOUNTERPARTY_NM.elementAt(i)%></option>
										<%} %>
									</select>
									<%if(entity_role.equals("B")){ %>
									<script>document.forms[0].counterparty_cd.style.pointerEvents = "none";</script>
									<%}else if(entity_role.equals("G")){ %>
									<script>document.forms[0].counterparty_cd.style.pointerEvents = "none";</script>
									<%}%>
									<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
				    			</div>
				    		</div>
				    	</div>
				    	<div class="col-sm-4 col-xs-4 col-md-4">
				    	</div>
			    			<div class="col-auto">
			    				<%if(opration.equalsIgnoreCase("MODIFY")){ %>
									<%if(!counterparty_cd.equals("0")){ %>
										<%if(status.equals("N")){ %>
										<label class="form-label" style="color:red;"><b>Status: Deactivated</b></label>
										<%}else{ %>
										<label class="form-label" style="color:green;"><b>Status: Active</b></label>
										<%} %>
									<%} %>
								<%} %>
							</div>													
						</div>
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">															
								<thead style = "vertical-align: middle">
								<%if(!name.equals("")){ %>										
									<tr>
										<th colspan = 4><div align="left"><span class="badge rounded-pill" style="background:<%=color%>;color:black;font-size:12px;"><i class="fa fa-snowflake-o"></i> Counterparty Details</span></div></th>																								
									</tr>										
									<tr>
										<th>Name</th><td><div><%=name%></div></td><th>Eff Date</th><td><div><%=eff_dt %></div></td>
									</tr>
									<tr>
										<th>Abbreviation</th><td><div><%=abbr %></div></td><th>Clearance</th><td><div><%=clearance%></div></td>
									</tr>
									<tr>
										<th>PAN No</th><td><div><%=pan_no%></div></td><th>PAN Eff Date</th><td><div><%=pan_dt %></div></td>
									</tr>
									<tr>
										<th>Web-Site Address</th><td><div><%=web_addr %></div></td><th>Category</th><td><div><%=category%></div></td>
									</tr>
									<tr>
										<th >KYC Remark</th><td><div><%=notes%></div></td><th>Requester/Approver Notes</th><td><div><%=requester_approver_note %></div></td>
									</tr>
									<%if(entity_role.equals("B")){ %>
									<tr>
										<th>Bank Details Config</th><td colspan = 3><%=bank_formula %></td>
									</tr>
									<%} %>							
									<%} else{%>	
									<tr>
										<td colspan="11" align="center">
											<%=utilmsg.infoMessage("<b>Please Select Counterparty</b>")%>
										</td>
									</tr>
								<%} %>
								<%if(!name.equals("")){ %>											
									<%for(int k=0;k<3;k++){ %>
										<tr>
											<th colspan =4 ><%if(k==0){ %>
											<div align="left"><span class="badge rounded-pill" style="background:<%=color%>;color:black;font-size:12px;"><i class="fa fa-snowflake-o"></i> Registered Address & Communication Details</span></div>
											<%}else if(k==1){ %>
												<div align="left"><span class="badge rounded-pill" style="background:<%=color%>;color:black;font-size:12px;"><i class="fa fa-snowflake-o"></i> Correspondence Address & Communication Details</span></div>
											<%}else if(k==2){ %>
												<div align="left"><span class="badge rounded-pill" style="background:<%=color%>;color:black;font-size:12px;"><i class="fa fa-snowflake-o"></i> Billing Address & Communication Details</span></div>
											<%} %></th>
										</tr>																												
										<tr>
											<th>Address</th><td><%=ADDRESS[k] %></td><th>Eff Date</th><td><%=REG_EFF_DT[k] %></td>
										</tr>
										<tr>
											<th>City</th><td><%=CITY[k] %></td><th>Zone</th><td><%=ZONE[k]%></td>
										</tr>
										<tr>
											<th>State/Province</th><td><%=STATE[k] %></td><th>Country</th><td><%=COUNTRY[k] %></td>
										</tr>
										<tr>
											<th>PIN/ZIP Code</th><td><%=PIN[k] %></td><th>Alternate Phone</th><td><%=ALT_PHONE[k] %></td>
										</tr>
										<tr>
											<th>Phone</th><td><%=PHONE[k]%></td><th>Fax-1</th><td><%=FAX1[k]%></td>
										</tr>
										<tr>
											<th>Cell</th><td><%=MOBILE[k]%></td><th>E-mail</th><td><%=EMAIL[k] %></td>
										</tr>
										<tr>
											<th>Fax-2</th><td><%=FAX2[k] %></td><th></th><th></th>
										</tr>
									<%}%>							
										
									<%if(!entity_role.equals("G") && !entity_role.equals("V") && !entity_role.equals("H") && !entity_role.equals("S")){ %>
										<%if(entity_role.equals("R")){ %>
											<tr align="left">
												<th colspan = 4><div  align="left"><span class="badge rounded-pill" style="background:<%=color%>;color:black;font-size:12px;"><i class="fa fa-snowflake-o"></i>Entry/Exit Point Address & Communication Details</span></div></th>
											</tr>
										<%}else{ %>
											<tr align="left">
												<th colspan = 4><div  align="left"><span class="badge rounded-pill" style="background:<%=color%>;color:black;font-size:12px;"><i class="fa fa-snowflake-o"></i>Plant(s)/Office(s) Address & Communication Details</span></div></th>
											</tr>
										<%} %>			
										<%if(VPLANT_SEQ_NO.size()>0){ %>
												<%for(int i=0; i<VPLANT_SEQ_NO.size(); i++){%>
										<thead style = "vertical-align: middle">													
											<tr>
												<th>Plant Name</th><td><div><%=VPLANT_NAME.elementAt(i)%></div></td><th>State</th><td><div><%=VPLANT_STATE.elementAt(i) %></div></td>
											</tr>
											<tr>
												<th>Plant ABBR	</th><td><div><%=VPLANT_ABBR.elementAt(i) %></div></td><th>Zone</th><td><div><%=VPLANT_ZONE_NM.elementAt(i) %></div></td>
											</tr>
											<tr>
												<th>Address</th><td><div><%=VPLANT_ADDR.elementAt(i) %></div></td><th>Sector</th><td><div><%=VPLANT_SECTOR_NM.elementAt(i) %></div></td>
											</tr>
											<tr>
												<th>City</th><td><div><%=VPLANT_CITY.elementAt(i) %></div></td><th>Eff Date</th><td><div><%=VPLANT_EFF_DT.elementAt(i) %></div></td>
											</tr>
											<tr>
												<th>Pin</th><td><div><%=VPLANT_PIN.elementAt(i) %></div></td><th>Tax/ID</th><td><div><%=VTAX_ID.elementAt(i)%></div></td>
											</tr>
											<tr>
												<th colspan = 4>&nbsp;</th>
											</tr>									
												<%} %>
											<%} else{%>	
											<tr>
												<td colspan="11" align="center">
													<%=utilmsg.infoMessage("<b>No Plant Configured!</b>")%>
												</td>
											</tr>
											<%} %>
										<%} %>
										<%if(entity_role.equals("R") || entity_role.equals("G") || entity_role.equals("V") || entity_role.equals("H") || entity_role.equals("S")){ %>
											<tr align="left">
											<th colspan = 4><div  align="left"><span class="badge rounded-pill" style="background:<%=color%>;color:black;font-size:12px;"><i class="fa fa-snowflake-o"></i>Business Unit Address & Communication Details</span></div></th>
										</tr>	
										<thead style = "vertical-align: middle">
										<%if(VBU_PLANT_SEQ_NO.size()>0){ %>
											<%for(int i=0; i<VBU_PLANT_SEQ_NO.size(); i++){%>
											<tr>
												<th>Name</th><td><div><%=VBU_PLANT_NAME.elementAt(i)%></div></td><th>Abbreviation</th><td><div><%=VBU_PLANT_ABBR.elementAt(i) %></div></td>
											</tr>
											<tr>
												<th>Address	</th><td><div><%=VBU_PLANT_ADDR.elementAt(i) %></div></td><th>City</th><td><div><%=VBU_PLANT_CITY.elementAt(i) %></div></td>
											</tr>
											<tr>
												<th>Pin</th><td><div><%=VBU_PLANT_PIN.elementAt(i) %></div></td><th>State</th><td><div><%=VBU_PLANT_STATE.elementAt(i) %></div></td>
											</tr>
											<tr>
												<th>Zone</th><td><div><%=VBU_PLANT_ZONE.elementAt(i) %></div></td><th>Eff Date</th><td><div><%=VBU_PLANT_EFF_DT.elementAt(i) %></div></td>
											</tr>
											<tr>
												<th>Tax/ID</th><td colspan="3"><div><%=VBU_TAX_ID.elementAt(i)%></div></td>
											</tr>
											<tr>
												<th colspan = 4>&nbsp;</th>
											</tr>
												<%} %>
											<%} else{%>	
											<tr>
												<td colspan="11" align="center">
													<%=utilmsg.infoMessage("<b>No BU Configured!</b>")%>
												</td>
											</tr>
											<%} %>
										</thead>		
										<%} %>
									<%} %>		
							</table>
						</div>
					</div>								
				</div>
			</div>
		</div>
	</div>
</div>	
			
<input type="hidden" name="option" value="ENTITY_EXCEL_MST">
<input type="hidden" name="sel_entity_role" value="">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="prev_entity_role" value="<%=entity_role%>">
<input type="hidden" name="old_value" value="">

<input type="hidden" name="business_unit" value="<%=business_unit%>">
<input type="hidden" name="status" value="<%=status%>">

																
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
<input type="hidden" name="comp_cd" value="<%=owner_cd%>">
<input type="hidden" name="u" value="<%=u%>">

</form>
</body>
</html>