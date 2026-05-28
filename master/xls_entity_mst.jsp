<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20230321 : XLS file for Excel Export functionality-->
<head>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.master.DB_Counterparty_Report" id="dbcounterpty" scope="request"></jsp:useBean>

<%
String opration=request.getParameter("opration")==null?"MODIFY":request.getParameter("opration");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String owner_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

if(entity_role.equals("B"))
{
	if(!owner_cd.equals(""))
	{
		counterparty_cd=owner_cd;
	}
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
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

<%if(status.equals("Y")){status="Status: Active";}else if(status.equals("N")){status="Status: Deactivated";}%>

<div class="d-flex justify-content-between">
	<table width="100%" border="1" frame="void">
		<tr>
			<th style="background:#f1f1f1" colspan="3" rowspan="2" align="center"><%=entity%>: <%=name%></th><th style="background:#f1f1f1" rowspan="2" align="center"><%=status%></th>
		</tr>
	</table>
	<table  width="100%" border="1" class="table table-bordered table-hover">
		<thead style = "vertical-align: middle">										
			<tr>
				<th colspan = 4  style="background:<%=color%>"><div align="left" style= "color:black;font-size:16px;"> Counterparty Details</div></th>																								
			</tr>
			<tr>
				<th>Name</th><td ><div align="left" style="vertical-align: middle; "><%=name%></div></td><th>Eff Date</th><td><div align="left" style="vertical-align: middle; "><%=eff_dt %></div></td>
			</tr>
			<tr>
				<th>Abbreviation</th><td><div align="left" style="vertical-align: middle; "><%=abbr %></div></td><th>Clearance</th><td><div align="left" style="vertical-align: middle; "><%=clearance%></div></td>
			</tr>
			<tr>
				<th>PAN No</th><td><div align="left" style="vertical-align: middle; "><%=pan_no%></div></td><th>PAN Eff Date</th><td><div align="left" style="vertical-align: middle; "><%=pan_dt %></div></td>
			</tr>
			<tr>
				<th>Web-Site Address</th><td><div align="left" style="vertical-align: middle; "><%=web_addr %></div></td><th>Category</th><td><div align="left" style="vertical-align: middle; "><%=category%></div></td>
			</tr>
			<tr>
				<th >KYC Remark</th><td><div align="left" style="vertical-align: middle; "><%=notes%></div></td><th>Requester/Approver Notes</th><td><div align="left" style="vertical-align: middle; "><%=requester_approver_note %></div></td>
			</tr>
			<%if(entity_role.equals("B")){ %>
			<tr>
				<th>Bank Details Config</th><td colspan = 3><%=bank_formula %></td>
			</tr>
			<%} %>									
			<%for(int k=0;k<3;k++){ %>
			<tr>
				<th colspan =4 style="background:<%=color%>"><%if(k==0){ %>
				<div align="left" style="color:black;font-size:16px;"> Registered Address & Communication Details</div>
				<%}else if(k==1){ %>
					<div align="left" style="color:black;font-size:16px;"> Correspondence Address & Communication Details</div>
				<%}else if(k==2){ %>
					<div align="left" style="color:black;font-size:16px;"> Billing Address & Communication Details</div>
				<%} %></th>
			</tr>																				
			<tr>
				<th>Address</th><td><div align="left" style="vertical-align: middle;"><%=ADDRESS[k] %></div></td><th>Eff Date</th><td><div align="left" style="vertical-align: middle; "><%=REG_EFF_DT[k] %></div></td>
			</tr>
			<tr>
				<th>City</th><td><div align="left" style="vertical-align: middle;"><%=CITY[k] %></div></td><th>Zone</th><td><div align="left" style="vertical-align: middle; "><%=ZONE[k]%></div></td>
			</tr>
			<tr>
				<th>State/Province</th><td><div align="left" style="vertical-align: middle; "><%=STATE[k] %></div></td><th>Country</th><td><div align="left" style="vertical-align: middle; "><%=COUNTRY[k] %></div></td>
			</tr>
			<tr>
				<th>PIN/ZIP Code</th><td><div align="left" style="vertical-align: middle; "><%=PIN[k] %></div></td><th>Alternate Phone</th><td><div align="left" style="vertical-align: middle; "><%=ALT_PHONE[k] %></div></td>
			</tr>
			<tr>
				<th>Phone</th><td><div align="left" style="vertical-align: middle; "><%=PHONE[k]%></div></td><th>Fax-1</th><td><div align="left" style="vertical-align: middle; "><%=FAX1[k]%></div></td>
			</tr>
			<tr>
				<th>Cell</th><td><div align="left" style="vertical-align: middle; "><%=MOBILE[k]%></div></td><th>E-mail</th><td><div align="left" style="vertical-align: middle; "><%=EMAIL[k] %></div></td>
			</tr>
			<tr>
				<th>Fax-2</th><td><div align="left" style="vertical-align: middle; "><%=FAX2[k] %></div></td><th></th><th></th>
			</tr>
			<%} %>
		<%if(!entity_role.equals("G") && !entity_role.equals("V") && !entity_role.equals("H") && !entity_role.equals("S")){ %>
			<tr align="left">
				<th colspan = 4 style="background:<%=color%>"><div align="left" style="color:black;font-size:16px;">Plant(s)/Office(s) Address & Communication Details</div></th>
			</tr>			
			<%if(VPLANT_SEQ_NO.size()>0){ %>
				<%for(int i=0; i<VPLANT_SEQ_NO.size(); i++){ %>
			<thead style = "vertical-align: middle">														
				<tr>
					<th>Plant Name</th><td><div align="left" style="vertical-align: middle; "><%=VPLANT_NAME.elementAt(i)%></div></td><th>State</th><td><div align="left" style="vertical-align: middle; "><%=VPLANT_STATE.elementAt(i) %></div></td>
				</tr>
				<tr>
					<th>Plant ABBR	</th><td><div align="left" style="vertical-align: middle; "><%=VPLANT_ABBR.elementAt(i) %></div></td><th>Zone</th><td><div align="left" style="vertical-align: middle; "><%=VPLANT_ZONE_NM.elementAt(i) %></div></td>
				</tr>
				<tr>
					<th>Address</th><td><div align="left" style="vertical-align: middle; "><%=VPLANT_ADDR.elementAt(i) %></div></td><th>Sector</th><td><div align="left" style="vertical-align: middle; "><%=VPLANT_SECTOR_NM.elementAt(i) %></div></td>
				</tr>
				<tr>
					<th>City</th><td><div align="left" style="vertical-align: middle; "><%=VPLANT_CITY.elementAt(i) %></div></td><th>Eff Date</th><td><div align="left" style="vertical-align: middle; "><%=VPLANT_EFF_DT.elementAt(i) %></div></td>
				</tr>
				<tr>
					<th>Pin</th><td><div align="left" style="vertical-align: middle; "><%=VPLANT_PIN.elementAt(i) %></div></td><th>Tax/ID</th><td><div align="left" style="vertical-align: middle; "><%=VTAX_ID.elementAt(i)%></div></td>
				</tr>
				<%if(VPLANT_SEQ_NO.size()>1){ %>
				<tr>
					<th colspan = 4>&nbsp;</th>
				</tr>
					<%} %>									
					<%} %>
				<%} else{%>	
				<tr>
					<td colspan="4" align="center">
						<%=utilmsg.infoMessage("<b>No Plant Configured!</b>")%>
					</td>
				</tr>
				<%} %>	
			<%} %>
			<%if(entity_role.equals("R") || entity_role.equals("G") || entity_role.equals("V") || entity_role.equals("H") || entity_role.equals("S")){ %>
				<tr align="left">
				<th colspan = 4 style="background:<%=color%>"><div align="left" style="color:black;font-size:16px;">Business Unit Address & Communication Details</div></th>
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
	</table>
</div>
																										
<input type="hidden" name="option" value="ENTITY_MST">
<input type="hidden" name="opration" value="<%=opration%>">
<input type="hidden" name="prev_entity_role" value="<%=entity_role%>">
<input type="hidden" name="old_value" value="">

<input type="hidden" name="business_unit" value="<%=business_unit%>">
<input type="hidden" name="status" value="<%=status%>">
															
</body>
</html>