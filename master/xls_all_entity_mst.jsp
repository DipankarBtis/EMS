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

String fileName = "All_" + entity_role_nm + "_Entity_Report.xls";
response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

HSSFWorkbook workbook = new HSSFWorkbook();
int l = 0;
int m = 0;
int x = 0;

for (int a = 0; a < VCOUNTERPARTY_CD.size(); a++) 
{
    HSSFSheet sheet = workbook.createSheet(VCOUNTERPARTY_ABBR.elementAt(a).toString());
    int currentRow = 0;

    HSSFRow row0 = sheet.createRow(currentRow++);
    sheet.setColumnWidth(0, 5000);
    sheet.setColumnWidth(1, 8000);
    sheet.setColumnWidth(2, 5000);
    sheet.setColumnWidth(3, 8000);

    HSSFCell cell0 = row0.createCell(0);
    sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 2));  
    HSSFCell cell1 = row0.createCell(3);
    sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));  

    cell0.setCellValue(entity_role_nm + " : " + VCOUNTERPARTY_NM.elementAt(a));
    cell1.setCellValue(VSTATUS.elementAt(a).equals("Y") ? "Status: Active" : "Status: Deactivated");

    HSSFRow row2 = sheet.createRow(currentRow++);
    row2.createCell(0).setCellValue("Counterparty Details");
    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

    HSSFRow row3 = sheet.createRow(currentRow++);
    row3.createCell(0).setCellValue("Name");
    row3.createCell(1).setCellValue(VCOUNTERPARTY_NM.elementAt(a).toString());
    row3.createCell(2).setCellValue("Eff-Date");
    row3.createCell(3).setCellValue(VEFF_DT.elementAt(a).toString());

    HSSFRow row4 = sheet.createRow(currentRow++);
    row4.createCell(0).setCellValue("Abbreviation");
    row4.createCell(1).setCellValue(VCOUNTERPARTY_ABBR.elementAt(a).toString());
    row4.createCell(2).setCellValue("Clearance");
    row4.createCell(3).setCellValue(VCLEARANCE.elementAt(a).toString());

    HSSFRow row5 = sheet.createRow(currentRow++);
    row5.createCell(0).setCellValue("PAN No");
    row5.createCell(1).setCellValue(VPAN.elementAt(a).toString());
    row5.createCell(2).setCellValue("PAN Eff Date");
    row5.createCell(3).setCellValue(VPAN_DT.elementAt(a).toString());

    HSSFRow row6 = sheet.createRow(currentRow++);
    row6.createCell(0).setCellValue("Web-Site Address");
    row6.createCell(1).setCellValue(VWEB_ADD.elementAt(a).toString());
    row6.createCell(2).setCellValue("Category");
    row6.createCell(3).setCellValue(VCATEGORY.elementAt(a).toString());

    HSSFRow row7 = sheet.createRow(currentRow++);
    sheet.addMergedRegion(new CellRangeAddress(currentRow-1, currentRow, 0, 0)); 
    row7.createCell(0).setCellValue("KYC Remark");
    row7.createCell(1).setCellValue(VREMARK.elementAt(a).toString());
    sheet.addMergedRegion(new CellRangeAddress(currentRow-1, currentRow, 2, 2)); 
    row7.createCell(2).setCellValue("Requester/Approver Notes");
    row7.createCell(3).setCellValue(VRQAP_NOTES.elementAt(a).toString());
    currentRow++;

    for (int k = 0; k < 3; k++) 
    {
        String title = k == 0
                       ? "Registered Address & Communication Details"
                       : k == 1
                         ? "Correspondence Address & Communication Details"
                         : "Billing Address & Communication Details";

        HSSFRow titleRow = sheet.createRow(currentRow++);
        titleRow.createCell(0).setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(currentRow-1, currentRow-1, 0, 3));

        HSSFRow rAddr = sheet.createRow(currentRow++);
        rAddr.createCell(0).setCellValue("Address");
        rAddr.createCell(1).setCellValue(VADD_ADDRES.elementAt(m).toString());
        rAddr.createCell(2).setCellValue("Eff Date");
        rAddr.createCell(3).setCellValue(VADD_REG_EFF_DT.elementAt(m).toString());

        HSSFRow rCity = sheet.createRow(currentRow++);
        rCity.createCell(0).setCellValue("City");
        rCity.createCell(1).setCellValue(VADD_CITY.elementAt(m).toString());
        rCity.createCell(2).setCellValue("Zone");
        rCity.createCell(3).setCellValue(VADD_ZONE.elementAt(m).toString());

        HSSFRow rState = sheet.createRow(currentRow++);
        rState.createCell(0).setCellValue("State/Province");
        rState.createCell(1).setCellValue(VADD_STATE.elementAt(m).toString());
        rState.createCell(2).setCellValue("Country");
        rState.createCell(3).setCellValue(VADD_COUNTRY.elementAt(m).toString());

        HSSFRow rPin = sheet.createRow(currentRow++);
        rPin.createCell(0).setCellValue("PIN/ZIP Code");
        rPin.createCell(1).setCellValue(VADD_PIN.elementAt(m).toString());
        rPin.createCell(2).setCellValue("Alternate Phone");
        rPin.createCell(3).setCellValue(VADD_ALT_PHONE.elementAt(m).toString());

        HSSFRow rContact = sheet.createRow(currentRow++);
        rContact.createCell(0).setCellValue("Phone");
        rContact.createCell(1).setCellValue(VADD_PHONE.elementAt(m).toString());
        rContact.createCell(2).setCellValue("Fax-1");
        rContact.createCell(3).setCellValue(VADD_FAX1.elementAt(m).toString());

        HSSFRow rCell = sheet.createRow(currentRow++);
        rCell.createCell(0).setCellValue("Cell");
        rCell.createCell(1).setCellValue(VADD_MOBILE.elementAt(m).toString());
        rCell.createCell(2).setCellValue("Email");
        rCell.createCell(3).setCellValue(VADD_EMAIL.elementAt(m).toString());

        HSSFRow rFax2 = sheet.createRow(currentRow++);
        rFax2.createCell(0).setCellValue("Fax-2");
        rFax2.createCell(1).setCellValue(VADD_FAX2.elementAt(m).toString());
        currentRow++;
        m++;
    }

    if (!Arrays.asList("G", "V", "H", "S").contains(entity_role)) 
    {
        HSSFRow headerPlant = sheet.createRow(currentRow++);
        headerPlant.createCell(0).setCellValue("Plant(s)/Office(s) Address & Communication Details");
        sheet.addMergedRegion(new CellRangeAddress(currentRow-1, currentRow-1, 0, 3));

        for (int i = 0; i < Integer.parseInt(VPLANT_INDEX.elementAt(a).toString()); i++) 
        {
            HSSFRow r1 = sheet.createRow(currentRow++);
            r1.createCell(0).setCellValue("Plant Name");
            r1.createCell(1).setCellValue(VPLANT_NAME.elementAt(l).toString());
            r1.createCell(2).setCellValue("State");
            r1.createCell(3).setCellValue(VPLANT_STATE.elementAt(l).toString());

            HSSFRow r2 = sheet.createRow(currentRow++);
            r2.createCell(0).setCellValue("Plant ABBR");
            r2.createCell(1).setCellValue(VPLANT_ABBR.elementAt(l).toString());
            r2.createCell(2).setCellValue("Zone");
            r2.createCell(3).setCellValue(VPLANT_ZONE_NM.elementAt(l).toString());

            HSSFRow r3 = sheet.createRow(currentRow++);
            r3.createCell(0).setCellValue("Address");
            r3.createCell(1).setCellValue(VPLANT_ADDR.elementAt(l).toString());
            r3.createCell(2).setCellValue("Sector");
            r3.createCell(3).setCellValue(VPLANT_SECTOR_NM.elementAt(l).toString());

            HSSFRow r4 = sheet.createRow(currentRow++);
            r4.createCell(0).setCellValue("City");
            r4.createCell(1).setCellValue(VPLANT_CITY.elementAt(l).toString());
            r4.createCell(2).setCellValue("Eff Date");
            r4.createCell(3).setCellValue(VPLANT_EFF_DT.elementAt(l).toString());

            HSSFRow r5 = sheet.createRow(currentRow++);
            r5.createCell(0).setCellValue("Pin");
            r5.createCell(1).setCellValue(VPLANT_PIN.elementAt(l).toString());
            r5.createCell(2).setCellValue("Tax/ID");
            r5.createCell(3).setCellValue(VTAX_ID.elementAt(l).toString());

            currentRow++;
            l++;
        }
    } 
    else 
    {
        HSSFRow headerBU = sheet.createRow(currentRow++);
        headerBU.createCell(0).setCellValue("Business Unit Address & Communication Details");
        sheet.addMergedRegion(new CellRangeAddress(currentRow-1, currentRow-1, 0, 3));

        for (int i = 0; i < Integer.parseInt(VBU_INDEX.elementAt(a).toString()); i++) {
            HSSFRow r1 = sheet.createRow(currentRow++);
            r1.createCell(0).setCellValue("Name");
            r1.createCell(1).setCellValue(VBU_PLANT_NAME.elementAt(x).toString());
            r1.createCell(2).setCellValue("Abbreviation");
            r1.createCell(3).setCellValue(VBU_PLANT_ABBR.elementAt(x).toString());

            HSSFRow r2 = sheet.createRow(currentRow++);
            r2.createCell(0).setCellValue("Address");
            r2.createCell(1).setCellValue(VBU_PLANT_ADDR.elementAt(x).toString());
            r2.createCell(2).setCellValue("City");
            r2.createCell(3).setCellValue(VBU_PLANT_CITY.elementAt(x).toString());

            HSSFRow r3 = sheet.createRow(currentRow++);
            r3.createCell(0).setCellValue("Pin");
            r3.createCell(1).setCellValue(VBU_PLANT_PIN.elementAt(x).toString());
            r3.createCell(2).setCellValue("State");
            r3.createCell(3).setCellValue(VBU_PLANT_STATE.elementAt(x).toString());

            HSSFRow r4 = sheet.createRow(currentRow++);
            r4.createCell(0).setCellValue("Zone");
            r4.createCell(1).setCellValue(VBU_PLANT_ZONE.elementAt(x).toString());
            r4.createCell(2).setCellValue("Eff Date");
            r4.createCell(3).setCellValue(VBU_PLANT_EFF_DT.elementAt(x).toString());

            HSSFRow r5 = sheet.createRow(currentRow++);
            r5.createCell(0).setCellValue("Tax/ID");
            r5.createCell(1).setCellValue(VBU_TAX_ID.elementAt(x).toString());

            currentRow++;
            x++;
        }
    }
}

workbook.write(response.getOutputStream());
response.getOutputStream().flush();
workbook.close();
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