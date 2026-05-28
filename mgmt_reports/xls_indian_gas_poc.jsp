
<%@page import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@ page import="org.apache.poi.ss.usermodel.*" %>
<%@ page import="org.apache.poi.xssf.usermodel.XSSFWorkbook" %>
<%@ page import="org.apache.poi.ss.util.CellRangeAddress" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.io.IOException" %>
<%@page import= "java.io.*"%>
<%@page import= "org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.ss.usermodel.*"%>
<%@page import= "org.apache.poi.ss.util.*"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function redirectToNCF()
{
	var counterparty_cd = document.forms[0].counterparty_cd.value;
	var from_dt = document.forms[0].from_dt.value;
	var to_dt = document.forms[0].to_dt.value;
	
	var u = document.forms[0].u.value;
	
	var url = "rpt_indian_gas_poc.jsp?counterparty_cd="+counterparty_cd+"&u="+u+
	"&from_dt="+from_dt+"&to_dt="+to_dt;

	location.replace(url);
}
</script>
<%@ include file="../util/common_js.jsp"%>
</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="db_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
owner_cd="";

if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

db_mgmt.setCallFlag("IND_GAS_POC");
db_mgmt.setComp_cd(owner_cd);
db_mgmt.setCounterparty_cd(counterparty_cd);
db_mgmt.setFrom_dt(from_dt);
db_mgmt.setTo_dt(to_dt);
db_mgmt.init();

Vector VMST_COUNTERPARTY_CD = db_mgmt.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = db_mgmt.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = db_mgmt.getVMST_COUNTERPARTY_ABBR();

Vector VCO_CODE = db_mgmt.getVCO_CODE();
Vector VCO_ABBR = db_mgmt.getVCO_ABBR();
Vector VCO_NM = db_mgmt.getVCO_NM();
Vector VCOUNTERPARTY_NM = db_mgmt.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = db_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_CATEGORY = db_mgmt.getVCOUNTERPARTY_CATEGORY();
Vector VDELIVERY_MONTH = db_mgmt.getVDELIVERY_MONTH();
Vector VDELIVERY_YEAR = db_mgmt.getVDELIVERY_YEAR();
Vector VVOLUME = db_mgmt.getVVOLUME();
Vector VTRADE_DT = db_mgmt.getVTRADE_DT();
Vector VTRADE_TYPE = db_mgmt.getVTRADE_TYPE();
Vector VBUY_SALE = db_mgmt.getVBUY_SALE();
Vector VUNIT_OF_MEASURE = db_mgmt.getVUNIT_OF_MEASURE();
Vector VINSTRUMENT_INDICATOR = db_mgmt.getVINSTRUMENT_INDICATOR();
Vector VDEAL_NUMBER = db_mgmt.getVDEAL_NUMBER();
Vector VDEAL_REF = db_mgmt.getVDEAL_REF();
Vector VNCF_CATEGORY = db_mgmt.getVNCF_CATEGORY();

String comp_abbr = db_mgmt.getComp_abbr();
double buy_sum = db_mgmt.getBuy_sum();
double sell_sum = db_mgmt.getSell_sum();

NumberFormat nf1 = new DecimalFormat("###########0.00");

Workbook workbook = new XSSFWorkbook();
Sheet sheet = workbook.createSheet("NCF Report");

//Set cell styles
CellStyle headerStyle = workbook.createCellStyle();
Font headerFont = workbook.createFont();
headerFont.setBold(true);
headerStyle.setFont(headerFont);

Row headerRow = sheet.createRow(0);
headerRow.createCell(0).setCellValue("NCF Report (" + from_dt + " - " + to_dt + ")");
sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));

Row tableHeaderRow = sheet.createRow(1);
String[] headers = 
{
    "Sr#", "Legal Entity ABBR", "Legal Entity", "Counterparty ABBR", "Counterparty", 
    "Deal Id#", "Trade Dt", "Trade Type", "Buy/Sale", "Delivery Month", 
    "Delivery Year", "Volume", "Unit Of Measure", "Instrument Indicator", 
    "Deal Ref#", "NCF Category"
};

for (int i = 0; i < headers.length; i++) 
{
    tableHeaderRow.createCell(i).setCellValue(headers[i]);
}

for (Cell cell : tableHeaderRow)
{
    cell.setCellStyle(headerStyle);
}

int rowNum = 2;

if (VCO_ABBR.size() > 0)
{
    for (int i = 0; i < VCO_ABBR.size(); i++)
    {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(i + 1);
        row.createCell(1).setCellValue(""+VCO_ABBR.get(i));
        row.createCell(2).setCellValue(""+VCO_NM.get(i));
        row.createCell(3).setCellValue(""+VCOUNTERPARTY_ABBR.get(i));
        row.createCell(4).setCellValue(""+VCOUNTERPARTY_NM.get(i));

        String dealId = ""+VDEAL_NUMBER.get(i);
        String partBeforeBracket = dealId;
        String partAfterBracket = "";
        
        int indexOfBracket = dealId.indexOf('<');
        if (indexOfBracket != -1) 
        {
            partBeforeBracket = dealId.substring(0, indexOfBracket).trim();
            partAfterBracket = dealId.substring(indexOfBracket).trim();
        }
        
        row.createCell(5).setCellValue(partBeforeBracket);

        row.createCell(6).setCellValue(""+VTRADE_DT.get(i));
        row.createCell(7).setCellValue(""+VTRADE_TYPE.get(i));
        row.createCell(8).setCellValue(""+VBUY_SALE.get(i));
        row.createCell(9).setCellValue("01/"+VDELIVERY_MONTH.get(i)+"/"+VDELIVERY_YEAR.get(i));
        row.createCell(10).setCellValue("01"+"/01/"+VDELIVERY_YEAR.get(i));
        row.createCell(11).setCellValue(Double.parseDouble(""+(VVOLUME.get(i))));//nf1.format
        row.createCell(12).setCellValue(VUNIT_OF_MEASURE.get(i).equals("1") ? "MMBTU" : "");
        row.createCell(13).setCellValue(""+VINSTRUMENT_INDICATOR.get(i));
        row.createCell(14).setCellValue(""+VDEAL_REF.get(i));
        row.createCell(15).setCellValue(""+VNCF_CATEGORY.get(i));
    }

    // Add Total Buy Volume row
    Row totalBuyVolumeRow = sheet.createRow(rowNum++);
    totalBuyVolumeRow.createCell(10).setCellValue("Total Buy Volume:");
    totalBuyVolumeRow.createCell(11).setCellValue(Double.parseDouble(nf1.format(buy_sum)));
    totalBuyVolumeRow.createCell(12).setCellValue("MMBTU");

    // Add Total Sell Volume row
    Row totalSellVolumeRow = sheet.createRow(rowNum++);
    totalSellVolumeRow.createCell(10).setCellValue("Total Sell Volume:");
    totalSellVolumeRow.createCell(11).setCellValue(Double.parseDouble(nf1.format(sell_sum)));
    totalSellVolumeRow.createCell(12).setCellValue("MMBTU");

} 
else
{
    // No data available
    Row noDataRow = sheet.createRow(rowNum++);
    noDataRow.createCell(0).setCellValue("No NCF Data is Available!");
    sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 15));
}

for (int i = 0; i < headers.length; i++)
{
    sheet.autoSizeColumn(i);
}

String sysdt = utildate.getSysdate().replace("/","");

String path =File.separator+"work_data"+owner_cd+File.separator;
String file_path = request.getRealPath(path);
String filenm = "NCF_Report_"+sysdt+".xlsx";

String filePath = file_path+File.separator+filenm;


//Try and Catch block used for this part of code : Due to In FMS8 parts like this might give vulnerability issue. 
try
{
	FileOutputStream fileOut = new FileOutputStream(filePath);
    workbook.write(fileOut);
	fileOut.close();
}
catch (Exception e)
{
	// TODO: handle exception
}

%>
<body>
<form action="">

<a id="rptDownloadBtn"></a>
<input type="hidden" name="comp_abbr" value="<%=comp_abbr%>">
<input type="hidden" name="counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="from_dt" value="<%=from_dt%>">
<input type="hidden" name="to_dt" value="<%=to_dt%>">

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

<%if(new File(file_path).exists()){ %>
<script type="text/javascript">

document.getElementById("rptDownloadBtn").href = "../work_data<%=owner_cd%>/NCF_Report_<%=sysdt%>.xlsx";
document.getElementById("rptDownloadBtn").click();
if(confirm("Generated Successfully!"))
{
	redirectToNCF();
}
</script>
<%}else{%>
<script type="text/javascript">
alert("Not Generated");
redirectToNCF();
</script>
<%} %>
</body>
</html>