<%@page import= "java.util.*"%>
<%@page import= "java.io.*"%>
<%@page import= "org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.ss.usermodel.*"%>
<%@page import= "org.apache.poi.ss.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--Harsh Maheta 20230321 : XLS file for Excel Export functionality-->
<head>
<script>
function redirectToEntityMst()
{
	var entity_role = document.forms[0].entity_role.value;
	var read_access = document.forms[0].read_access.value;
	var write_access = document.forms[0].write_access.value;
	var check_access = document.forms[0].check_access.value;
	var print_access = document.forms[0].print_access.value;
	var delete_access = document.forms[0].delete_access.value;
	var audit_access = document.forms[0].audit_access.value;
	var authorize_access = document.forms[0].authorize_access.value;
	var approve_access = document.forms[0].approve_access.value;
	var execute_access = document.forms[0].execute_access.value;
	var formCd = document.forms[0].form_cd.value;
	var formNm = document.forms[0].form_nm.value;
	var mod_cd = document.forms[0].mod_cd.value;
	var mod_nm = document.forms[0].mod_nm.value;
	
	var url = "rpt_entity_mst.jsp?&form_cd="+formCd+"&form_nm="+formNm+
	"&mod_cd="+mod_cd+"&mod_nm="+mod_nm+"&entity_role="+entity_role+
	"&read_access="+read_access+"&write_access="+write_access+"&check_access="+check_access+
	"&print_access="+print_access+"&delete_access="+delete_access+"&audit_access="+audit_access+
	"&authorize_access="+authorize_access+"&approve_access="+approve_access+"&execute_access="+execute_access;
	location.replace(url);
}
</script>
<%@ include file="../util/common_js.jsp"%>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.master.DB_Counterparty_Report" id="dbcounterpty" scope="request"></jsp:useBean>

<%
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");

dbcounterpty.setCallFlag("ENTITY_EXCEL_MASTER");
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

Vector VPLANT_INDEX = dbcounterpty.getVPLANT_INDEX();
Vector VENTITY_INDEX = dbcounterpty.getVENTITY_INDEX();

Vector VCOUNTRY_CODE = dbcounterpty.getVCOUNTRY_CODE();
Vector VCOUNTRY_NM = dbcounterpty.getVCOUNTRY_NM();
Vector VISO_CODE = dbcounterpty.getVISO_CODE();
Vector VTIN = dbcounterpty.getVTIN();
Vector VSTATE_CODE = dbcounterpty.getVSTATE_CODE();
Vector VSTATE_NM = dbcounterpty.getVSTATE_NM();

Vector VCOUNTERPTY_CD = dbcounterpty.getVCOUNTERPTY_CD();
Vector VCOUNTERPTY_NM = dbcounterpty.getVCOUNTERPTY_NM();
Vector VENTITY_ROLE = dbcounterpty.getVENTITY_ROLE();
Vector VREMARK = dbcounterpty.getVREMARK();
Vector VREQ_BY = dbcounterpty.getVREQ_BY();
Vector VREQ_DT = dbcounterpty.getVREQ_DT();
Vector VSEQ_NO = dbcounterpty.getVSEQ_NO(); 
Vector VCLEARANCE = dbcounterpty.getVCLEARANCE();

Vector VPAN = dbcounterpty.getVPAN();
Vector VPAN_DT = dbcounterpty.getVPAN_DT();
Vector VWEB_ADD = dbcounterpty.getVWEB_ADD();
Vector VKYC = dbcounterpty.getVKYC();
Vector VCATEGORY = dbcounterpty.getVCATEGORY();
Vector VRQAP_NOTES = dbcounterpty.getVRQAP_NOTES();
Vector VALT_PHONE = dbcounterpty.getVALT_PHONE();
Vector VIGX = dbcounterpty.getVIGX();
Vector VCUSTOMER = dbcounterpty.getVCUSTOMER();
Vector VTRADER = dbcounterpty.getVTRADER();
Vector VTRASPORTER = dbcounterpty.getVTRASPORTER();
Vector VSTATUS = dbcounterpty.getVSTATUS();

Vector VEFF_DT = dbcounterpty.getVEFF_DT();
Vector VADD_REG_EFF_DT = dbcounterpty.getVADD_REG_EFF_DT();
Vector VADD_ADDRESS_FLAG = dbcounterpty.getVADD_ADDRESS_FLAG();
Vector VADD_ADDRES = dbcounterpty.getVADD_ADDRES();
Vector VADD_CITY = dbcounterpty.getVADD_CITY();
Vector VADD_PIN = dbcounterpty.getVADD_PIN();
Vector VADD_STATE = dbcounterpty.getVADD_STATE();
Vector VADD_ZONE = dbcounterpty.getVADD_ZONE();
Vector VADD_COUNTRY = dbcounterpty.getVADD_COUNTRY();
Vector VADD_PHONE = dbcounterpty.getVADD_PHONE();
Vector VADD_MOBILE = dbcounterpty.getVADD_MOBILE();
Vector VADD_ALT_PHONE = dbcounterpty.getVADD_ALT_PHONE();
Vector VADD_FAX1 = dbcounterpty.getVADD_FAX1();
Vector VADD_FAX2 = dbcounterpty.getVADD_FAX2();
Vector VADD_EMAIL = dbcounterpty.getVADD_EMAIL();

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
Vector VBU_INDEX = dbcounterpty.getVBU_INDEX(); 

String tamp_role_nm="";
String entity_role_nm="";
if(entity_role.equals("B")) {
	entity_role_nm = ("Business Owner"); 
}
else if (entity_role.equals("C")){
	entity_role_nm = ("Customer"); 
}
else if (entity_role.equals("T")){
	entity_role_nm = ("Trader"); 
}
else if (entity_role.equals("R")){
	entity_role_nm = ("Trasporter"); 
}
else if (entity_role.equals("V")){
	entity_role_nm = ("Vessel Agent"); 
}
else if (entity_role.equals("H")){
	entity_role_nm = ("Custom House Agent"); 
}
else if (entity_role.equals("S")){
	entity_role_nm = ("Surveyor"); 
}
tamp_role_nm=entity_role;
String path =File.separator+"work_data"+owner_cd+File.separator;
String file_path = request.getRealPath(path);

HSSFWorkbook workbook = new HSSFWorkbook();					
int l = 0;
int x = 0;
int m=0;
for(int a=0; a < VCOUNTERPARTY_CD.size(); a++)
{
	HSSFSheet sheet1 = workbook.createSheet(""+VCOUNTERPARTY_ABBR.elementAt(a));
							
	//First row
	HSSFRow rowhead1 = sheet1.createRow((short)0);	
	
	HSSFCell cell1 = rowhead1.createCell(0);			
	sheet1.addMergedRegion(new CellRangeAddress(0,1,0,2));
	
	HSSFCellStyle style1 = workbook.createCellStyle(); 
	sheet1.setColumnWidth(0, 5000);
	sheet1.setColumnWidth(1, 8000);
	sheet1.setColumnWidth(2, 5000);
	sheet1.setColumnWidth(3, 8000);
	
	HSSFCell cell2 = rowhead1.createCell(3);
	sheet1.addMergedRegion(new CellRangeAddress(0,1,3,3));
	
	cell1.setCellValue(entity_role_nm+" : "+VCOUNTERPARTY_NM.elementAt(a));
	
	if(VSTATUS.elementAt(a).equals("Y"))
	{
		String Status ="Status: Active"; 
		cell2.setCellValue(""+Status);
	}
	if(VSTATUS.elementAt(a).equals("N"))
	{
		String Status ="Status: Deactivated"; 
		cell2.setCellValue(""+Status);
	}
	//Second Row
	HSSFRow rowhead2 = sheet1.createRow((short)2);
	rowhead2.createCell((short) 0).setCellValue("Counterparty Details");
	sheet1.addMergedRegion(new CellRangeAddress(2,2,0,3));

	//Third row
	HSSFRow rowhead3 = sheet1.createRow((short)3);
	rowhead3.createCell((short) 0).setCellValue("Name");
	rowhead3.createCell((short) 1).setCellValue(""+VCOUNTERPARTY_NM.elementAt(a));
	rowhead3.createCell((short) 2).setCellValue("Eff-Date");
	rowhead3.createCell((short) 3).setCellValue(""+VEFF_DT.elementAt(a));
	
	//Fourth row 
	HSSFRow rowhead4 = sheet1.createRow((short)4);
	rowhead4.createCell((short) 0).setCellValue("Abbreviation");
	rowhead4.createCell((short) 1).setCellValue(""+VCOUNTERPARTY_ABBR.elementAt(a));
	rowhead4.createCell((short) 2).setCellValue("Clearance");
	rowhead4.createCell((short) 3).setCellValue(""+VCLEARANCE.elementAt(a));
	
	//Fifth row
	HSSFRow rowhead5 = sheet1.createRow((short)5);
	rowhead5.createCell((short) 0).setCellValue("PAN No");
	rowhead5.createCell((short) 1).setCellValue(""+VPAN.elementAt(a));
	rowhead5.createCell((short) 2).setCellValue("PAN Eff Date");
	rowhead5.createCell((short) 3).setCellValue(""+VPAN_DT.elementAt(a));
	
	//Sixth row
	HSSFRow rowhead6 = sheet1.createRow((short)6);
	rowhead6.createCell((short) 0).setCellValue("Web-Site Address");
	rowhead6.createCell((short) 1).setCellValue(""+VWEB_ADD.elementAt(a));
	rowhead6.createCell((short) 2).setCellValue("Category");
	rowhead6.createCell((short) 3).setCellValue(""+VCATEGORY.elementAt(a));
	
	//Seventh row
	HSSFRow rowhead7 = sheet1.createRow((short)7);
	rowhead7.createCell((short) 0).setCellValue("KYC Remark");
	sheet1.addMergedRegion(new CellRangeAddress(7,8,0,0));
	rowhead7.createCell((short) 1).setCellValue(""+VREMARK.elementAt(a));
	sheet1.addMergedRegion(new CellRangeAddress(7,8,1,1));
	rowhead7.createCell((short) 2).setCellValue("Requester/Approver Notes");
	sheet1.addMergedRegion(new CellRangeAddress(7,8,2,2));
	rowhead7.createCell((short) 3).setCellValue(""+VRQAP_NOTES.elementAt(a));
	sheet1.addMergedRegion(new CellRangeAddress(7,8,3,3));
	
	for(int k=0; k<3;k++) 
	{
		if(k==0)
		{
			String details_type = "Registered Address & Communication Details";
		
			//Nineth row
			HSSFRow rowhead9 = sheet1.createRow((short)9);
			rowhead9.createCell((short) 0).setCellValue(""+details_type);
			sheet1.addMergedRegion(new CellRangeAddress(9,9,0,3));
			
			//Tenth row
			HSSFRow rowhead10 = sheet1.createRow((short)10);
			rowhead10.createCell((short) 0).setCellValue("Address");
			rowhead10.createCell((short) 1).setCellValue(""+VADD_ADDRES.elementAt(m));
			rowhead10.createCell((short) 2).setCellValue("Eff Date");
			rowhead10.createCell((short) 3).setCellValue(""+VADD_REG_EFF_DT.elementAt(m));
			
			//Eleventh row
			HSSFRow rowhead11 = sheet1.createRow((short)11);
			rowhead11.createCell((short) 0).setCellValue("City");
			rowhead11.createCell((short) 1).setCellValue(""+VADD_CITY.elementAt(m));
			rowhead11.createCell((short) 2).setCellValue("Zone");
			rowhead11.createCell((short) 3).setCellValue(""+VADD_ZONE.elementAt(m));
			
			//12th row
			HSSFRow rowhead12 = sheet1.createRow((short)12);
			rowhead12.createCell((short) 0).setCellValue("State/Province");
			rowhead12.createCell((short) 1).setCellValue(""+VADD_STATE.elementAt(m));
			rowhead12.createCell((short) 2).setCellValue("Country");
			rowhead12.createCell((short) 3).setCellValue(""+VADD_COUNTRY.elementAt(m));
			
			//Thirteenth row
			HSSFRow rowhead13 = sheet1.createRow((short)13);
			rowhead13.createCell((short) 0).setCellValue("PIN/ZIP Code");
			rowhead13.createCell((short) 1).setCellValue(""+VADD_PIN.elementAt(m));
			rowhead13.createCell((short) 2).setCellValue("Alternate Phone");
			rowhead13.createCell((short) 3).setCellValue(""+VADD_ALT_PHONE.elementAt(m));
			
			//Fourteenth row
			HSSFRow rowhead14 = sheet1.createRow((short)14);
			rowhead14.createCell((short) 0).setCellValue("Phone");
			rowhead14.createCell((short) 1).setCellValue(""+VADD_PHONE.elementAt(m));
			rowhead14.createCell((short) 2).setCellValue("Fax-1");
			rowhead14.createCell((short) 3).setCellValue(""+VADD_FAX1.elementAt(m));
			
			//Fifteenth row
			HSSFRow rowhead15 = sheet1.createRow((short)15);
			rowhead15.createCell((short) 0).setCellValue("Cell");
			rowhead15.createCell((short) 1).setCellValue(""+VADD_MOBILE.elementAt(m));
			rowhead15.createCell((short) 2).setCellValue("E-mail");
			rowhead15.createCell((short) 3).setCellValue(""+VADD_EMAIL.elementAt(m));
			
			//Sixteenth row
			HSSFRow rowhead16 = sheet1.createRow((short)16);
			rowhead16.createCell((short) 0).setCellValue("Fax-2");
			rowhead16.createCell((short) 1).setCellValue(""+VADD_FAX2.elementAt(m));
			rowhead16.createCell((short) 2).setCellValue("");
			rowhead16.createCell((short) 3).setCellValue("");
			
		}
		else if(k==1)
		{
			String details_type = "Correspondence Address & Communication Details";
		
			//Seventeenth row
			HSSFRow rowhead17 = sheet1.createRow((short)17);
			rowhead17.createCell((short) 0).setCellValue(""+details_type);
			sheet1.addMergedRegion(new CellRangeAddress(17,17,0,3));
			
			//Tenth row
			HSSFRow rowhead18 = sheet1.createRow((short)18);
			rowhead18.createCell((short) 0).setCellValue("Address");
			rowhead18.createCell((short) 1).setCellValue(""+VADD_ADDRES.elementAt(m));
			rowhead18.createCell((short) 2).setCellValue("Eff Date");
			rowhead18.createCell((short) 3).setCellValue(""+VADD_REG_EFF_DT.elementAt(m));
			
			
			//Eleventh row
			HSSFRow rowhead19 = sheet1.createRow((short)19);
			rowhead19.createCell((short) 0).setCellValue("City");
			rowhead19.createCell((short) 1).setCellValue(""+VADD_CITY.elementAt(m));
			rowhead19.createCell((short) 2).setCellValue("Zone");
			rowhead19.createCell((short) 3).setCellValue(""+VADD_ZONE.elementAt(m));
			
			//Twelfth row
			HSSFRow rowhead20 = sheet1.createRow((short)20);
			rowhead20.createCell((short) 0).setCellValue("State/Province");
			rowhead20.createCell((short) 1).setCellValue(""+VADD_STATE.elementAt(m));
			rowhead20.createCell((short) 2).setCellValue("Country");
			rowhead20.createCell((short) 3).setCellValue(""+VADD_COUNTRY.elementAt(m));
			
			//Thirteenth row
			HSSFRow rowhead21 = sheet1.createRow((short)21);
			rowhead21.createCell((short) 0).setCellValue("PIN/ZIP Code");
			rowhead21.createCell((short) 1).setCellValue(""+VADD_PIN.elementAt(m));
			rowhead21.createCell((short) 2).setCellValue("Alternate Phone");
			rowhead21.createCell((short) 3).setCellValue(""+VADD_ALT_PHONE.elementAt(m));
			
			//Fourteenth row
			HSSFRow rowhead22 = sheet1.createRow((short)22);
			rowhead22.createCell((short) 0).setCellValue("Phone");
			rowhead22.createCell((short) 1).setCellValue(""+VADD_PHONE.elementAt(m));
			rowhead22.createCell((short) 2).setCellValue("Fax-1");
			rowhead22.createCell((short) 3).setCellValue(""+VADD_FAX1.elementAt(m));
			
			//Fifteenth row
			HSSFRow rowhead23 = sheet1.createRow((short)23);
			rowhead23.createCell((short) 0).setCellValue("Cell");
			rowhead23.createCell((short) 1).setCellValue(""+VADD_MOBILE.elementAt(m));
			rowhead23.createCell((short) 2).setCellValue("E-mail");
			rowhead23.createCell((short) 3).setCellValue(""+VADD_EMAIL.elementAt(m));
			
			//Sixteenth row
			HSSFRow rowhead24 = sheet1.createRow((short)24);
			rowhead24.createCell((short) 0).setCellValue("Fax-2");
			rowhead24.createCell((short) 1).setCellValue(""+VADD_FAX2.elementAt(m));
			rowhead24.createCell((short) 2).setCellValue("");
			rowhead24.createCell((short) 3).setCellValue("");
		}
		else if(k==2)
		{
			String details_type = " Billing Address & Communication Details";
		
			HSSFRow rowhead25 = sheet1.createRow((short)25);
			rowhead25.createCell((short) 0).setCellValue(""+details_type);
			sheet1.addMergedRegion(new CellRangeAddress(17,17,0,3));
			
			//Tenth row
			HSSFRow rowhead26 = sheet1.createRow((short)26);
			rowhead26.createCell((short) 0).setCellValue("Address");
			rowhead26.createCell((short) 1).setCellValue(""+VADD_ADDRES.elementAt(m));
			rowhead26.createCell((short) 2).setCellValue("Eff Date");
			rowhead26.createCell((short) 3).setCellValue(""+VADD_REG_EFF_DT.elementAt(m));
			
			
			//Eleventh row
			HSSFRow rowhead27 = sheet1.createRow((short)27);
			rowhead27.createCell((short) 0).setCellValue("City");
			rowhead27.createCell((short) 1).setCellValue(""+VADD_CITY.elementAt(m));
			rowhead27.createCell((short) 2).setCellValue("Zone");
			rowhead27.createCell((short) 3).setCellValue(""+VADD_ZONE.elementAt(m));
			
			//Twelfth row
			HSSFRow rowhead28 = sheet1.createRow((short)28);
			rowhead28.createCell((short) 0).setCellValue("State/Province");
			rowhead28.createCell((short) 1).setCellValue(""+VADD_STATE.elementAt(m));
			rowhead28.createCell((short) 2).setCellValue("Country");
			rowhead28.createCell((short) 3).setCellValue(""+VADD_COUNTRY.elementAt(m));
			
			//Thirteenth row
			HSSFRow rowhead29 = sheet1.createRow((short)29);
			rowhead29.createCell((short) 0).setCellValue("PIN/ZIP Code");
			rowhead29.createCell((short) 1).setCellValue(""+VADD_PIN.elementAt(m));
			rowhead29.createCell((short) 2).setCellValue("Alternate Phone");
			rowhead29.createCell((short) 3).setCellValue(""+VADD_ALT_PHONE.elementAt(m));
			
			//Fourteenth row
			HSSFRow rowhead30 = sheet1.createRow((short)30);
			rowhead30.createCell((short) 0).setCellValue("Phone");
			rowhead30.createCell((short) 1).setCellValue(""+VADD_PHONE.elementAt(m));
			rowhead30.createCell((short) 2).setCellValue("Fax-1");
			rowhead30.createCell((short) 3).setCellValue(""+VADD_FAX1.elementAt(m));
			
			//Fifteenth row
			HSSFRow rowhead31 = sheet1.createRow((short)31);
			rowhead31.createCell((short) 0).setCellValue("Cell");
			rowhead31.createCell((short) 1).setCellValue(""+VADD_MOBILE.elementAt(m));
			rowhead31.createCell((short) 2).setCellValue("E-mail");
			rowhead31.createCell((short) 3).setCellValue(""+VADD_EMAIL.elementAt(m));
			
			//Sixteenth row
			HSSFRow rowhead32 = sheet1.createRow((short)31);
			rowhead32.createCell((short) 0).setCellValue("Fax-2");
			rowhead32.createCell((short) 1).setCellValue(""+VADD_FAX2.elementAt(m));
			rowhead32.createCell((short) 2).setCellValue("");
			rowhead32.createCell((short) 3).setCellValue("");						
		}	
		m++;
	}
	
	int j = 33;
	
	if(!entity_role.equals("G") && !entity_role.equals("V") && !entity_role.equals("H") && !entity_role.equals("S"))
	{
		//33rd row
		HSSFRow rowhead33 = sheet1.createRow((short)33);
		rowhead33.createCell((short) 0).setCellValue("Plant(s)/Office(s) Address & Communication Details");
		sheet1.addMergedRegion(new CellRangeAddress(33,33,0,3));
		
		for(int i=0;i<Integer.parseInt(VPLANT_INDEX.elementAt(a)+"");i++) 
		{	
			//34th row
			j+=1;
			
			HSSFRow rowhead34 = sheet1.createRow((short)j);
			rowhead34.createCell((short) 0).setCellValue("Plant Name");
			rowhead34.createCell((short) 1).setCellValue(""+VPLANT_NAME.elementAt(l));
			rowhead34.createCell((short) 2).setCellValue("State");
			rowhead34.createCell((short) 3).setCellValue(""+VPLANT_STATE.elementAt(l));
			
			//35th row
			j+=1;
			HSSFRow rowhead35 = sheet1.createRow((short)j);
			rowhead35.createCell((short) 0).setCellValue("Plant ABBR");
			rowhead35.createCell((short) 1).setCellValue(""+VPLANT_ABBR.elementAt(l));
			rowhead35.createCell((short) 2).setCellValue("Zone");
			rowhead35.createCell((short) 3).setCellValue(""+VPLANT_ZONE_NM.elementAt(l));
			
			//36th row
			j+=1;
			HSSFRow rowhead36 = sheet1.createRow((short)j);
			rowhead36.createCell((short) 0).setCellValue("Address");
			rowhead36.createCell((short) 1).setCellValue(""+VPLANT_ADDR.elementAt(l));
			rowhead36.createCell((short) 2).setCellValue("Sector");
			rowhead36.createCell((short) 3).setCellValue(""+VPLANT_SECTOR_NM.elementAt(l));
			
			//37th row
			j+=1;
			HSSFRow rowhead37 = sheet1.createRow((short)j);
			rowhead37.createCell((short) 0).setCellValue("City");
			rowhead37.createCell((short) 1).setCellValue(""+VPLANT_CITY.elementAt(l));
			rowhead37.createCell((short) 2).setCellValue("Eff Date");
			rowhead37.createCell((short) 3).setCellValue(""+VPLANT_EFF_DT.elementAt(l));
			
			//38th row
			j+=1;
			HSSFRow rowhead38 = sheet1.createRow((short)j);
			rowhead38.createCell((short) 0).setCellValue("Pin");
			rowhead38.createCell((short) 1).setCellValue(""+VPLANT_PIN.elementAt(l));
			rowhead38.createCell((short) 2).setCellValue("Tax/ID");
			rowhead38.createCell((short) 3).setCellValue(""+VTAX_ID.elementAt(l));
			
			//39rd row
			j+=1;
			HSSFRow rowhead39 = sheet1.createRow((short)j);
			rowhead39.createCell((short) 0).setCellValue("");
			sheet1.addMergedRegion(new CellRangeAddress(j,j,0,3));
			
			l++;
		}
	}
	
	if(entity_role.equals("R") || entity_role.equals("G") || entity_role.equals("V") || entity_role.equals("H") || entity_role.equals("S"))
	{
		int plantAddOnCnt = j+1;
		
		//33rd row
		HSSFRow rowheadaftPlants33 = sheet1.createRow((short)plantAddOnCnt);
		rowheadaftPlants33.createCell((short) 0).setCellValue("Business Unit Address & Communication Details");
		sheet1.addMergedRegion(new CellRangeAddress(plantAddOnCnt,plantAddOnCnt,0,3));
				
		int k = plantAddOnCnt;
		for(int i=0;i<Integer.parseInt(VBU_INDEX.elementAt(a)+"");i++) 
		{	
			k+=1;
			
			HSSFRow rowheadaftPlants34 = sheet1.createRow((short)k);
			rowheadaftPlants34.createCell((short) 0).setCellValue("Name");
			rowheadaftPlants34.createCell((short) 1).setCellValue(""+VBU_PLANT_NAME.elementAt(x));
			rowheadaftPlants34.createCell((short) 2).setCellValue("Abbreviation");
			rowheadaftPlants34.createCell((short) 3).setCellValue(""+VBU_PLANT_ABBR.elementAt(x));
			
			k+=1;
			HSSFRow rowheadaftPlants35 = sheet1.createRow((short)k);
			rowheadaftPlants35.createCell((short) 0).setCellValue("Address");
			rowheadaftPlants35.createCell((short) 1).setCellValue(""+VBU_PLANT_ADDR.elementAt(x));
			rowheadaftPlants35.createCell((short) 2).setCellValue("City");
			rowheadaftPlants35.createCell((short) 3).setCellValue(""+VBU_PLANT_CITY.elementAt(x));
			
			k+=1;
			HSSFRow rowheadaftPlants36 = sheet1.createRow((short)k);
			rowheadaftPlants36.createCell((short) 0).setCellValue("Pin");
			rowheadaftPlants36.createCell((short) 1).setCellValue(""+VBU_PLANT_PIN.elementAt(x));
			rowheadaftPlants36.createCell((short) 2).setCellValue("State");
			rowheadaftPlants36.createCell((short) 3).setCellValue(""+VBU_PLANT_STATE.elementAt(x));
			
			k+=1;
			HSSFRow rowheadaftPlants37 = sheet1.createRow((short)k);
			rowheadaftPlants37.createCell((short) 0).setCellValue("Zone");
			rowheadaftPlants37.createCell((short) 1).setCellValue(""+VBU_PLANT_ZONE.elementAt(x));
			rowheadaftPlants37.createCell((short) 2).setCellValue("Eff Date");
			rowheadaftPlants37.createCell((short) 3).setCellValue(""+VBU_PLANT_EFF_DT.elementAt(x));
			
			k+=1;
			HSSFRow rowheadaftPlants38 = sheet1.createRow((short)k);
			rowheadaftPlants38.createCell((short) 0).setCellValue("Tax/ID");
			rowheadaftPlants38.createCell((short) 1).setCellValue(""+VBU_TAX_ID.elementAt(x));
			
			k+=1;
			HSSFRow rowheadaftPlants39 = sheet1.createRow((short)k);
			rowheadaftPlants39.createCell((short) 0).setCellValue("");
			sheet1.addMergedRegion(new CellRangeAddress(k,k,0,3));
			
			x++;
		}
	}
}				

String fileName = file_path+File.separator+"All_"+entity_role_nm+"_Entity_Report"+".xls";

FileOutputStream fileOut = new FileOutputStream(fileName);  

int numberOfSheets = workbook.getNumberOfSheets();
    
workbook.write(fileOut);
fileOut.close(); 
	
%>
<body> <!-- onload="redirectToEntityMst();"> by HP-->
<form>
<%if(entity_role_nm.equals("Business Owner")) {
	entity_role = ("B"); 
}
else if (entity_role_nm.equals("Customer")){
	entity_role = ("C"); 
}
else if (entity_role_nm.equals("Trader")){
	entity_role = ("T"); 
}
else if (entity_role_nm.equals("Trasporter")){
	entity_role = ("R"); 
}
else if (entity_role_nm.equals("Vessel Agent")){
	entity_role = ("V"); 
}
else if (entity_role_nm.equals("Custom House Agent")){
	entity_role = ("H"); 
}
else if (entity_role_nm.equals("Surveyor")){
	entity_role = ("S"); 
}
%>
<a id="multisheetIcon"></a>

<input type="hidden" name="entity_role" value="<%=entity_role%>">
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
</form>

<%if(new File(file_path).exists()){ %>
<script type="text/javascript">

document.getElementById("multisheetIcon").href = "../work_data<%=owner_cd%>/All_<%=entity_role_nm%>_Entity_Report.xls";
document.getElementById("multisheetIcon").click();
if(confirm("Generated Successfully!"))
{
	redirectToEntityMst()
}
</script>
<%}else{%>
<script type="text/javascript">
alert("Not Generated");
redirectToEntityMst()
</script>
<%} %>
</body>
</html>