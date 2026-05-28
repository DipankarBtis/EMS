<%@page import= "java.util.*"%>
<%@page import= "java.io.*"%>
<%@page import= "org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.ss.usermodel.*"%>
<%@page import= "org.apache.poi.ss.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>

<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="mgmt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String accroid=request.getParameter("accroid")==null?"":request.getParameter("accroid");
String bu_select=request.getParameter("bu_select")==null?"0":request.getParameter("bu_select");

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");
String month_to=request.getParameter("month_to")==null?""+currentMonth:request.getParameter("month_to");
String year_to=request.getParameter("year_to")==null?""+currentYear:request.getParameter("year_to");

if(month.length() == 1)
{
	month="0"+month; 
}
if(month_to.length() == 1)
{
	month_to="0"+month_to; 
}
String from_dt = "01/"+month+"/"+year;
String to_dt=utildate.getLastDateOfMonth(month_to,year_to);

String owner_cd="";

if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}

String owner_abbr="";
if(session.getAttribute("comp_abbr")==null||session.getAttribute("comp_abbr")==""||session.getAttribute("comp_abbr").toString().equals("null"))
{
	owner_abbr="";
}  
else
{
	owner_abbr=""+session.getAttribute("comp_abbr");
}
mgmt_rpt.setCallFlag("VOLUME_REPORT");
mgmt_rpt.setComp_cd(owner_cd);
mgmt_rpt.setFrom_dt(from_dt);
mgmt_rpt.setTo_dt(to_dt);
mgmt_rpt.setBu_select(bu_select);
mgmt_rpt.init();


Vector VINVOICE_LIST_ABBR = mgmt_rpt.getVINVOICE_LIST_ABBR();
Vector VINVOICE_LIST_NAME = mgmt_rpt.getVINVOICE_LIST_NAME();
Vector VINDEX = mgmt_rpt.getVINDEX();

Vector VCOUNTERPARTY_CD = mgmt_rpt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = mgmt_rpt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = mgmt_rpt.getVCOUNTERPARTY_NM();

Vector VPUR_COUNTERPARTY_CD = mgmt_rpt.getVPUR_COUNTERPARTY_CD();
Vector VPUR_COUNTERPARTY_ABBR = mgmt_rpt.getVPUR_COUNTERPARTY_ABBR();
Vector VPUR_COUNTERPARTY_NM = mgmt_rpt.getVPUR_COUNTERPARTY_NM();

Vector VDEAL_NO = mgmt_rpt.getVDEAL_NO();
Vector VCONT_REF_NO = mgmt_rpt.getVCONT_REF_NO();
Vector VAGMT_BASE = mgmt_rpt.getVAGMT_BASE();
Vector VINVOICE_NO = mgmt_rpt.getVINVOICE_NO();
Vector VALLOC_QTY = mgmt_rpt.getVALLOC_QTY();

Vector VPUR_INDEX = mgmt_rpt.getVPUR_INDEX();
Vector VPUR_DEAL_NO = mgmt_rpt.getVPUR_DEAL_NO();
Vector VPUR_CONT_REF_NO = mgmt_rpt.getVPUR_CONT_REF_NO();
Vector VPUR_INVOICE_NO = mgmt_rpt.getVPUR_INVOICE_NO();
Vector VPUR_ALLOC_QTY = mgmt_rpt.getVPUR_ALLOC_QTY();

Vector VTOTAL_QTY = mgmt_rpt.getVTOTAL_QTY();
Vector VPUR_TOTAL_QTY = mgmt_rpt.getVPUR_TOTAL_QTY();
Vector VOPEN_QTY = mgmt_rpt.getVOPEN_QTY();
Vector VCLOSE_QTY = mgmt_rpt.getVCLOSE_QTY();

Vector VMONTH = mgmt_rpt.getVMONTH();
%>
<body>
<%
response.setHeader("Content-Disposition","attachment; filename=" + fileName );

HSSFWorkbook workbook = new HSSFWorkbook();

HSSFFont font11 = workbook.createFont();
font11.setFontName("Calibri");
font11.setFontHeightInPoints((short) 11);

HSSFFont fontB11 = workbook.createFont();
fontB11.setBold(true);
fontB11.setFontName("Calibri");
fontB11.setFontHeightInPoints((short) 11);

HSSFFont fontRed11 = workbook.createFont();
fontRed11.setFontName("Calibri");
fontRed11.setFontHeightInPoints((short) 11);
fontRed11.setColor(IndexedColors.RED.getIndex());

HSSFCellStyle style11 = workbook.createCellStyle();
style11.setBorderTop(BorderStyle.THIN);
style11.setBorderBottom(BorderStyle.THIN);
style11.setBorderLeft(BorderStyle.THIN);
style11.setBorderRight(BorderStyle.THIN);
style11.setFont(font11);

HSSFCellStyle styleB11 = workbook.createCellStyle();
styleB11.setBorderTop(BorderStyle.THIN);
styleB11.setBorderBottom(BorderStyle.THIN);
styleB11.setBorderLeft(BorderStyle.THIN);
styleB11.setBorderRight(BorderStyle.THIN);
styleB11.setFont(fontB11);

HSSFCellStyle styleRed11 = workbook.createCellStyle();
styleRed11.cloneStyleFrom(style11);
styleRed11.setFont(fontRed11);

HSSFCellStyle styleR11 = workbook.createCellStyle();
styleR11.cloneStyleFrom(styleB11);
styleR11.setAlignment(HorizontalAlignment.RIGHT);
styleR11.setVerticalAlignment(VerticalAlignment.CENTER);

HSSFCellStyle styleCB11 = workbook.createCellStyle();
styleCB11.setAlignment(HorizontalAlignment.CENTER);
styleCB11.setVerticalAlignment(VerticalAlignment.CENTER);
styleCB11.setBorderTop(BorderStyle.THIN);
styleCB11.setBorderBottom(BorderStyle.THIN);
styleCB11.setBorderLeft(BorderStyle.THIN);
styleCB11.setBorderRight(BorderStyle.THIN);
styleCB11.setFont(fontB11);

HSSFCellStyle styleCenter = workbook.createCellStyle();
styleCenter.setAlignment(HorizontalAlignment.CENTER);
styleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
styleCenter.setBorderTop(BorderStyle.THIN);
styleCenter.setBorderBottom(BorderStyle.THIN);
styleCenter.setBorderLeft(BorderStyle.THIN);
styleCenter.setBorderRight(BorderStyle.THIN);

HSSFFont BoldTitle = workbook.createFont();
BoldTitle.setFontName("Calibri");
BoldTitle.setBold(true);                          
BoldTitle.setFontHeightInPoints((short) 20);      
styleCenter.setFont(BoldTitle);

int i = 0;

for (int j = 0; j < VINVOICE_LIST_NAME.size(); j++) {

    int index = Integer.parseInt("" + VINDEX.elementAt(j));
    String heading = "" + VINVOICE_LIST_ABBR.elementAt(j);
    String sal_qty = "" + VTOTAL_QTY.elementAt(j);
    String pur_qty = "" + VPUR_TOTAL_QTY.elementAt(j);
    String open_qty = "" + VOPEN_QTY.elementAt(j);
    String close_qty = "" + VCLOSE_QTY.elementAt(j);

    HSSFSheet sheet = workbook.createSheet(VINVOICE_LIST_NAME.elementAt(j).toString());
    
    int rowNum = 0;

    HSSFRow titleRow = sheet.createRow(rowNum++);
    HSSFCell cell0 = titleRow.createCell(0);
    CellRangeAddress titleRegion =new CellRangeAddress(rowNum-1, rowNum-1, 0, 10);
    sheet.addMergedRegion(titleRegion);

	RegionUtil.setBorderTop(BorderStyle.THIN, titleRegion, sheet);
	RegionUtil.setBorderBottom(BorderStyle.THIN, titleRegion, sheet);
	RegionUtil.setBorderLeft(BorderStyle.THIN, titleRegion, sheet);
	RegionUtil.setBorderRight(BorderStyle.THIN, titleRegion, sheet);
    
    cell0.setCellStyle(styleCenter);
    cell0.setCellValue("Volume Reconciliation Report - " + VINVOICE_LIST_NAME.elementAt(j));
    
    HSSFRow headingRow = sheet.createRow(rowNum++);
    HSSFCell hcell0 = headingRow.createCell(0);
    HSSFCell hcell1 = headingRow.createCell(1);
    HSSFCell hcell6 = headingRow.createCell(6);
    
    CellRangeAddress purheadRegion =new CellRangeAddress(rowNum-1, rowNum-1, 1, 5);
    CellRangeAddress salheadRegion =new CellRangeAddress(rowNum-1, rowNum-1, 6, 10);
    sheet.addMergedRegion(purheadRegion);
    sheet.addMergedRegion(salheadRegion);
    
	RegionUtil.setBorderTop(BorderStyle.THIN, purheadRegion, sheet);
	RegionUtil.setBorderBottom(BorderStyle.THIN, purheadRegion, sheet);
	RegionUtil.setBorderLeft(BorderStyle.THIN, purheadRegion, sheet);
	RegionUtil.setBorderRight(BorderStyle.THIN, purheadRegion, sheet);
	
	RegionUtil.setBorderTop(BorderStyle.THIN, salheadRegion, sheet);
	RegionUtil.setBorderBottom(BorderStyle.THIN, salheadRegion, sheet);
	RegionUtil.setBorderLeft(BorderStyle.THIN, salheadRegion, sheet);
	RegionUtil.setBorderRight(BorderStyle.THIN, salheadRegion, sheet);
    
    hcell0.setCellStyle(styleB11);
    hcell0.setCellValue("Month/Year");
    
    hcell1.setCellStyle(styleCB11);
    hcell1.setCellValue("SALES");
    
    hcell6.setCellStyle(styleCB11);
    hcell6.setCellValue("PURCHASE");
    
    HSSFRow headingRow0 = sheet.createRow(rowNum++);
    HSSFCell hcell00 = headingRow0.createCell(0);
    HSSFCell hcell01 = headingRow0.createCell(1);
    HSSFCell hcell02 = headingRow0.createCell(2);
    HSSFCell hcell03 = headingRow0.createCell(3);
    HSSFCell hcell04 = headingRow0.createCell(4);
    HSSFCell hcell05 = headingRow0.createCell(5);
    HSSFCell hcell06 = headingRow0.createCell(6);
    HSSFCell hcell07 = headingRow0.createCell(7);
    HSSFCell hcell08 = headingRow0.createCell(8);
    HSSFCell hcell09 = headingRow0.createCell(9);
    HSSFCell hcell010 = headingRow0.createCell(10);

    hcell00.setCellStyle(styleB11);
    hcell00.setCellValue("");
    
    hcell01.setCellStyle(styleCB11);
    hcell01.setCellValue("Counterparty");
    
    hcell02.setCellStyle(styleCB11);
    hcell02.setCellValue("Contract No");
    
    hcell03.setCellStyle(styleCB11);
    hcell03.setCellValue("Contract/Trade Ref#");
    
    hcell04.setCellStyle(styleCB11);
    hcell04.setCellValue("Invoice No");
    
    hcell05.setCellStyle(styleCB11);
    hcell05.setCellValue("Quantity(MMBTU)");
    
    hcell06.setCellStyle(styleCB11);
    hcell06.setCellValue("Counterparty");
    
    hcell07.setCellStyle(styleCB11);
    hcell07.setCellValue("Contract No");
    
    hcell08.setCellStyle(styleCB11);
    hcell08.setCellValue("Contract/Trade Ref#");
    
    hcell09.setCellStyle(styleCB11);
    hcell09.setCellValue("Invoice No");
    
    hcell010.setCellStyle(styleCB11);
    hcell010.setCellValue("Quantity(MMBTU)");
    
    HSSFRow openRow = sheet.createRow(rowNum++);
    HSSFCell openRow0 = openRow.createCell(0);
    HSSFCell openRow1 = openRow.createCell(1);
    HSSFCell openRow2 = openRow.createCell(2);
    HSSFCell openRow3 = openRow.createCell(3);
    HSSFCell openRow4 = openRow.createCell(4);
    HSSFCell openRow5 = openRow.createCell(5);
    HSSFCell openRow6 = openRow.createCell(6);
    HSSFCell openRow7 = openRow.createCell(7);
    HSSFCell openRow8 = openRow.createCell(8);
    HSSFCell openRow9 = openRow.createCell(9);
    HSSFCell openRow10 = openRow.createCell(10);
    
    openRow0.setCellStyle(styleB11); 
    openRow0.setCellValue("Opening Inventory");
   
    openRow1.setCellValue("");
    openRow1.setCellStyle(style11);

    openRow2.setCellValue("");
    openRow2.setCellStyle(style11);
    
    openRow3.setCellValue("");
    openRow3.setCellStyle(style11);

    openRow4.setCellValue("");
    openRow4.setCellStyle(style11);

    openRow5.setCellValue("");
    openRow5.setCellStyle(style11);

    openRow6.setCellValue("");
    openRow6.setCellStyle(style11);

    openRow7.setCellValue("");
    openRow7.setCellStyle(style11);

    openRow8.setCellValue("");
    openRow8.setCellStyle(style11);

    openRow9.setCellValue("");
    openRow9.setCellStyle(style11);

    openRow10.setCellStyle(styleB11); 
    openRow10.setCellValue(Double.parseDouble(open_qty));

    if (index > 0) 
    {
        int k = 0;
        for (; i < index + i; i++) 
        {
            k++;

            HSSFRow dataRow = sheet.createRow(rowNum++);
            HSSFCell col0 = dataRow.createCell(0);
            HSSFCell col1 = dataRow.createCell(1);
            HSSFCell col2 = dataRow.createCell(2);
            HSSFCell col3 = dataRow.createCell(3);
            HSSFCell col4 = dataRow.createCell(4);
            HSSFCell col5 = dataRow.createCell(5);
            HSSFCell col6 = dataRow.createCell(6);
            HSSFCell col7 = dataRow.createCell(7);
            HSSFCell col8 = dataRow.createCell(8);
            HSSFCell col9 = dataRow.createCell(9);
            HSSFCell col10 = dataRow.createCell(10);

            col0.setCellStyle(style11);       
            col0.setCellValue("" + VMONTH.elementAt(i));
            
            col1.setCellStyle(style11); 
            col1.setCellValue("" + VCOUNTERPARTY_NM.elementAt(i));

            col2.setCellStyle(style11); 
            col2.setCellValue("" + VDEAL_NO.elementAt(i));
                   
            col3.setCellStyle(style11); 
            col3.setCellValue("" + VCONT_REF_NO.elementAt(i));
                   
            col4.setCellStyle(style11); 
            col4.setCellValue("" + VINVOICE_NO.elementAt(i));
            
            col5.setCellStyle(style11);
            if(VALLOC_QTY.elementAt(i).equals(""))
            {
	            col5.setCellValue("");
            }
            else
            {
            	col5.setCellValue(Double.parseDouble("" + VALLOC_QTY.elementAt(i)));
            }
            
            col6.setCellStyle(style11);
            col6.setCellValue("" + VPUR_COUNTERPARTY_NM.elementAt(i));
                   
            col7.setCellStyle(style11);
            col7.setCellValue("" + VPUR_DEAL_NO.elementAt(i));
			
            
            if(VPUR_CONT_REF_NO.elementAt(i).toString().contains("<font color='red'>"))
            {
            	col8.setCellStyle(styleRed11);
            	col8.setCellValue("Adjustment");
            }
            else
            {
            	col8.setCellStyle(style11);
            	col8.setCellValue("" + VPUR_CONT_REF_NO.elementAt(i));
            }
            
            col9.setCellStyle(style11);
            col9.setCellValue("" + VPUR_INVOICE_NO.elementAt(i));
            
            col10.setCellStyle(style11);
            if(VPUR_ALLOC_QTY.elementAt(i).equals(""))
            {
            	col10.setCellValue("");
            }
            else
            {
            	col10.setCellValue(Double.parseDouble("" + VPUR_ALLOC_QTY.elementAt(i)));
            }
            
            
            for (int col = 0; col < 11; col++)
            {
                sheet.autoSizeColumn(col);
            }
            
            if (k == index) {
                i++;
                break;
            }
        }

        HSSFRow totalRow = sheet.createRow(rowNum++);
        HSSFCell totalRow0 = totalRow.createCell(0);
        HSSFCell totalRow5 = totalRow.createCell(5);
        HSSFCell totalRow10 = totalRow.createCell(10);
        
        CellRangeAddress total0 =new CellRangeAddress(rowNum-1, rowNum-1, 0, 4);
        CellRangeAddress total1 =new CellRangeAddress(rowNum-1, rowNum-1, 6, 9);
        sheet.addMergedRegion(total0);
        sheet.addMergedRegion(total1);
        
		RegionUtil.setBorderTop(BorderStyle.THIN, total0, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, total0, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, total0, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, total0, sheet);
		
		RegionUtil.setBorderTop(BorderStyle.THIN, total1, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, total1, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, total1, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, total1, sheet);
        
        totalRow0.setCellStyle(styleR11);
        totalRow0.setCellValue("Total(MMBTU):");
        
        totalRow5.setCellStyle(styleR11);
        totalRow5.setCellValue(Double.parseDouble(sal_qty));
        
        totalRow10.setCellStyle(styleR11);
        totalRow10.setCellValue(Double.parseDouble(pur_qty));
    }
    else
    {
    	titleRow = sheet.createRow(rowNum++);
        cell0 = titleRow.createCell(0);
        
 		CellRangeAddress noinv =new CellRangeAddress(rowNum-1, rowNum-1, 0, 10);
 		sheet.addMergedRegion(noinv);
        
		RegionUtil.setBorderTop(BorderStyle.THIN, noinv, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, noinv, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, noinv, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, noinv, sheet);
        
        cell0.setCellStyle(styleCB11);
        cell0.setCellValue("[ No Invoice is Generated! ]");
    }

    HSSFRow closeRow = sheet.createRow(rowNum++);
    HSSFCell closeRow0 = closeRow.createCell(0);
    HSSFCell closeRow1 = closeRow.createCell(1);
    HSSFCell closeRow2 = closeRow.createCell(2);
    HSSFCell closeRow3 = closeRow.createCell(3);
    HSSFCell closeRow4 = closeRow.createCell(4);
    HSSFCell closeRow5 = closeRow.createCell(5);
    HSSFCell closeRow6 = closeRow.createCell(6);
    HSSFCell closeRow7 = closeRow.createCell(7);
    HSSFCell closeRow8 = closeRow.createCell(8);
    HSSFCell closeRow9 = closeRow.createCell(9);
    HSSFCell closeRow10 = closeRow.createCell(10);
    
    closeRow0.setCellStyle(styleB11); 
    closeRow0.setCellValue("Closing Inventory");
    
    closeRow1.setCellValue("");
    closeRow1.setCellStyle(style11);

    closeRow2.setCellValue("");
    closeRow2.setCellStyle(style11);
    
    closeRow3.setCellValue("");
    closeRow3.setCellStyle(style11);

    closeRow4.setCellValue("");
    closeRow4.setCellStyle(style11);

    closeRow5.setCellValue("");
    closeRow5.setCellStyle(style11);

    closeRow6.setCellValue("");
    closeRow6.setCellStyle(style11);

    closeRow7.setCellValue("");
    closeRow7.setCellStyle(style11);

    closeRow8.setCellValue("");
    closeRow8.setCellStyle(style11);

    closeRow9.setCellValue("");
    closeRow9.setCellStyle(style11);
    
    closeRow10.setCellStyle(styleB11); 
    closeRow10.setCellValue(Double.parseDouble(close_qty));

    rowNum++; 
}

workbook.write(response.getOutputStream());
response.getOutputStream().flush();
workbook.close();
%>
</body>
</html>