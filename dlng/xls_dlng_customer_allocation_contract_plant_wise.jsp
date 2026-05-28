<%@page import= "java.util.*"%>
<%@page import= "java.io.*"%>
<%@page import= "org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.ss.usermodel.*"%>
<%@page import= "org.apache.poi.ss.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<meta charset="ISO-8859-1">

<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
String sysdate=utildate.getSysdate();

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String deal_no=request.getParameter("deal_no")==null?"0":request.getParameter("deal_no");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String bu_plant_seq=request.getParameter("bu_plant_seq")==null?"0":request.getParameter("bu_plant_seq");
String plant_seq=request.getParameter("plant_seq")==null?"0":request.getParameter("plant_seq");
String agmt_no = "";
String cont_no="";
String cont_rev="";
String contract_type="";
if (deal_no != null && !deal_no.trim().equals("0") && !deal_no.trim().isEmpty()) 
{
     String[] parts = deal_no.split(":");
     agmt_no   = parts[0];
     cont_no   = parts[1];
     contract_type = parts[2];
}
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
dlng.setCallFlag("DLNG_ALLOC_TO_CUSTOMER_PLANT_CONTRACT_WISE");
dlng.setComp_cd(owner_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setBu_plant_seq(bu_plant_seq);
dlng.setPlant_seq(plant_seq);
dlng.setAgmt_no(agmt_no);
dlng.setCont_no(cont_no);
dlng.setContract_type(contract_type);
dlng.setDeal_no(deal_no);
dlng.init();

Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();
Vector VBU_PLANT_ABBR=dlng.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ=dlng.getVBU_PLANT_SEQ();
Vector VDIS_CONT_MAPPING=dlng.getVDIS_CONT_MAPPING();
Vector VCONT_REF=dlng.getVCONT_REF();
Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_NM = dlng.getVCOUNTERPARTY_PLANT_NM();

Vector VSEGMENT = dlng.getVSEGMENT();
Vector VSEGMENT_TYPE = dlng.getVSEGMENT_TYPE();
Vector VINDEX = dlng.getVINDEX();
Vector VCONT_NO = dlng.getVCONT_NO();

Vector VGAS_DT = dlng.getVGAS_DT();
Vector VQTY_MMBTU = dlng.getVQTY_MMBTU();
Vector VQTY_SCM = dlng.getVQTY_SCM();
Vector VTOTAL_QTY_MMBTU = dlng.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_SCM = dlng.getVTOTAL_QTY_SCM();

Vector VPLANT_NM = dlng.getVPLANT_NM();
Vector VPLANT_SEQ_NO = dlng.getVPLANT_SEQ_NO();
Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();
Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VCOLOR = dlng.getVCOLOR();

String deal_map = dlng.getDeal_map();
String cont_ref_no = dlng.getCont_ref_no();
int temp_count = dlng.getTemp_count();

%>

<body>
<%
response.setHeader("content-Disposition","attachment; filename="+fileName);

HSSFWorkbook workbook = new HSSFWorkbook();

HSSFFont font11 = workbook.createFont();
font11.setFontName("Calibri");
font11.setFontHeightInPoints((short) 11);

HSSFFont fontB11 = workbook.createFont();
fontB11.setBold(true);
fontB11.setFontName("Calibri");
fontB11.setFontHeightInPoints((short) 11);

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
BoldTitle.setFontHeightInPoints((short) 15);      
styleCenter.setFont(fontB11);

if(VMST_COUNTERPARTY_CD.size()>0)
{
    int k=0,l=0,p=0;
	for(int i=0;i<temp_count;i++)
	{ 
		int index=0;
		index=Integer.parseInt(""+VINDEX.elementAt(i));
		int plant_size=((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size();
		
		String sheetName =""; 
		
		if(deal_no.equals("0"))
		{
			sheetName = VCOUNTERPARTY_ABBR.elementAt(i)+"-"+"("+VDIS_CONT_MAPPING.elementAt(i)+"-["+VCONT_REF.elementAt(i)+"])";
		}
		else
		{
			sheetName = VCOUNTERPARTY_ABBR.elementAt(i)+"-"+"("+deal_map+"-["+cont_ref_no+"])";
		}
		sheetName = sheetName.replaceAll("[\\\\/:*?\\[\\]]", "");
	    
		HSSFSheet sheet = workbook.createSheet(sheetName);
	    int rowNum = 0;
	    
	    HSSFRow titleRow = sheet.createRow(rowNum++);
	    HSSFCell cell0 = titleRow.createCell(0);
	    CellRangeAddress titleRegion =new CellRangeAddress(rowNum-1, rowNum-1, 0, (plant_size*2)+2);
	    sheet.addMergedRegion(titleRegion);
	    
	    RegionUtil.setBorderTop(BorderStyle.THIN, titleRegion, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, titleRegion, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, titleRegion, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, titleRegion, sheet);
	    
	    cell0.setCellStyle(styleCenter);
	    cell0.setCellValue("DLNG Customer Allocation (By Contract And Plant) From "+from_dt+" To "+to_dt);
	    
	    String cont_map="";
	    if(deal_no.equals("0"))
		{
	    	cont_map = VCOUNTERPARTY_ABBR.elementAt(i)+"-"+VCOUNTERPARTY_NM.elementAt(i)+"-"+"("+VDIS_CONT_MAPPING.elementAt(i)+"-["+VCONT_REF.elementAt(i)+"])";
		}
		else
		{
			cont_map = VCOUNTERPARTY_ABBR.elementAt(i)+"-"+VCOUNTERPARTY_NM.elementAt(i)+"-"+"("+deal_map+"-["+cont_ref_no+"])";
		}
	    
	    HSSFRow abbrRow = sheet.createRow(rowNum++);
	    HSSFCell acell0 = abbrRow.createCell(0);
	    
	    CellRangeAddress counterPartyAbbr =new CellRangeAddress(rowNum-1, rowNum-1, 0, (plant_size*2)+2);
	    sheet.addMergedRegion(counterPartyAbbr);
	    
	    RegionUtil.setBorderTop(BorderStyle.THIN, counterPartyAbbr, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, counterPartyAbbr, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, counterPartyAbbr, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, counterPartyAbbr, sheet);
		
		acell0.setCellStyle(styleB11);
	    acell0.setCellValue(cont_map);
	    
	    HSSFRow hrow2 = sheet.createRow(rowNum++);
	    HSSFRow hrow3 = sheet.createRow(rowNum++);
	    HSSFRow hrow4 = sheet.createRow(rowNum++);
	
	    HSSFCell gasDayCell = hrow2.createCell(0);
	    gasDayCell.setCellValue("Gas Day");
	    gasDayCell.setCellStyle(styleCenter);
	    
	    sheet.setColumnWidth(0, 4000);
	
	    // create remaining cells for styling
	    hrow3.createCell(0).setCellStyle(styleCenter);
	    hrow4.createCell(0).setCellStyle(styleCenter);
	
	    CellRangeAddress gasDayRegion = new CellRangeAddress(2, 4, 0, 0);
	    sheet.addMergedRegion(gasDayRegion);
	
	    RegionUtil.setBorderTop(BorderStyle.THIN, gasDayRegion, sheet);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, gasDayRegion, sheet);
	    RegionUtil.setBorderLeft(BorderStyle.THIN, gasDayRegion, sheet);
	    RegionUtil.setBorderRight(BorderStyle.THIN, gasDayRegion, sheet);
	    
	    HSSFCell totalQtyCell = hrow2.createCell(1);
	    totalQtyCell.setCellValue("Total Quantity Supplied");
	    totalQtyCell.setCellStyle(styleCenter);
	
	    // create other merged cells
	    hrow2.createCell(2).setCellStyle(styleCenter);
	    hrow3.createCell(1).setCellStyle(styleCenter);
	    hrow3.createCell(2).setCellStyle(styleCenter);
	    
	    sheet.setColumnWidth(1, 5000);  
	    sheet.setColumnWidth(2, 5000);
	
	    CellRangeAddress totalQtyRegion = new CellRangeAddress(2, 3, 1, 2);
	    sheet.addMergedRegion(totalQtyRegion);
	
	    RegionUtil.setBorderTop(BorderStyle.THIN, totalQtyRegion, sheet);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, totalQtyRegion, sheet);
	    RegionUtil.setBorderLeft(BorderStyle.THIN, totalQtyRegion, sheet);
	    RegionUtil.setBorderRight(BorderStyle.THIN, totalQtyRegion, sheet);
	    
	    HSSFCell mmbtu = hrow4.createCell(1);
	    mmbtu.setCellValue("MMBTU");
	    mmbtu.setCellStyle(styleCenter);
	    
	    HSSFCell scm = hrow4.createCell(2);
	    scm.setCellValue("SCM");
	    scm.setCellStyle(styleCenter);
	    
	    int startCol = 3;
	    int endCol = startCol + (plant_size * 2) - 1;
	
	    HSSFCell plantHeaderCell = hrow2.createCell(startCol);
	    plantHeaderCell.setCellValue("Total Quantity Supplied To Plant");
	    plantHeaderCell.setCellStyle(styleCenter);
	
	    // create remaining cells in merged region
	    for (int c = startCol + 1; c <= endCol; c++) {
	    	hrow2.createCell(c).setCellStyle(styleCenter);
	    }
	
	    CellRangeAddress plantRegion = new CellRangeAddress(2, 2, startCol, endCol);
	    sheet.addMergedRegion(plantRegion);
	
	    RegionUtil.setBorderTop(BorderStyle.THIN, plantRegion, sheet);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, plantRegion, sheet);
	    RegionUtil.setBorderLeft(BorderStyle.THIN, plantRegion, sheet);
	    RegionUtil.setBorderRight(BorderStyle.THIN, plantRegion, sheet);
	    
	    int plantnmcol = startCol;
	    int widthCol = 3;
	    for(int j=0;j<plant_size;j++)
	    {
	    	String plantNm = (String)((Vector)VCOUNTERPARTY_PLANT_NM.elementAt(i)).elementAt(j);
	    	HSSFCell plantName = hrow3.createCell(plantnmcol);
	    	plantName.setCellValue(plantNm);
	    	plantName.setCellStyle(styleCenter);
	    	
	    	HSSFCell plantName1 = hrow3.createCell(plantnmcol+1);
	    	plantName.setCellStyle(styleCenter);
	    	
	    	CellRangeAddress plantNM = new CellRangeAddress(3, 3, plantnmcol, plantnmcol+1);
	        sheet.addMergedRegion(plantNM);
	
	        RegionUtil.setBorderTop(BorderStyle.THIN, plantNM, sheet);
	        RegionUtil.setBorderBottom(BorderStyle.THIN, plantNM, sheet);
	        RegionUtil.setBorderLeft(BorderStyle.THIN, plantNM, sheet);
	        RegionUtil.setBorderRight(BorderStyle.THIN, plantNM, sheet);
	        
	        HSSFCell plantmmbtu = hrow4.createCell(plantnmcol);
	        plantmmbtu.setCellValue("MMBTU");
	        plantmmbtu.setCellStyle(styleCenter);
	    	
	    	HSSFCell plantscm = hrow4.createCell(plantnmcol+1);
	    	plantscm.setCellValue("SCM");
	    	plantscm.setCellStyle(styleCenter);
	    	
	    	sheet.setColumnWidth(widthCol, 5000); 
	        sheet.setColumnWidth(widthCol + 1, 5000); 
	        widthCol += 2;
	    	
	    	plantnmcol += 2;
	    }
	    
	    int n=0;
    	for(k=k;k<VGAS_DT.size();k++)
    	{
    		n += 1;
    		
    		HSSFRow dataRow = sheet.createRow(rowNum++);
    		
    		HSSFCell gasDt = dataRow.createCell(0);
    		gasDt.setCellValue(""+VGAS_DT.elementAt(k));
    		gasDt.setCellStyle(style11);
    		
    		int m=0;
    		int mmscmCol=1;
    		for(l=l;l<VQTY_MMBTU.size();l++)
    		{
    		    m +=1;
    		  
    		    HSSFCell qtyMmbtu = dataRow.createCell(mmscmCol++);
    		    if(VQTY_MMBTU.elementAt(l).equals("-"))
                {
    		    	qtyMmbtu.setCellValue("-");
                }
                else
                {
                	qtyMmbtu.setCellValue(Double.parseDouble(VQTY_MMBTU.elementAt(l).toString()));
                }
    		    qtyMmbtu.setCellStyle(style11);
    		    
    		    HSSFCell qtyScm = dataRow.createCell(mmscmCol++);
    		    if(VQTY_SCM.elementAt(l).equals("-"))
                {
    		    	qtyScm.setCellValue("-");
                }
                else
                {
                	qtyScm.setCellValue(Double.parseDouble(VQTY_SCM.elementAt(l).toString()));
                }
    		    qtyScm.setCellStyle(style11);
    		    
    		    if((plant_size+1) == m)
    		    {
    		    	l++;
    		    	break;
    		    }
    		}
    		
    		if(index==n)
    		{
    			HSSFRow totalRow = sheet.createRow(rowNum++);
    			HSSFCell total = totalRow.createCell(0);
    			total.setCellValue("Total: ");
    			total.setCellStyle(styleB11);
    			 int colNo=1;
    			 int o=0;
    			for(p=p;p<VTOTAL_QTY_MMBTU.size();p++)
    			{
    				o +=1;
    				HSSFCell totalqtyMMBTU = totalRow.createCell(colNo++);
    				if(VTOTAL_QTY_MMBTU.elementAt(p).equals("-"))
	                {
    					totalqtyMMBTU.setCellValue("-");
	                }
	                else
	                {
	                	totalqtyMMBTU.setCellValue(Double.parseDouble(VTOTAL_QTY_MMBTU.elementAt(p).toString()));
	                }
    				totalqtyMMBTU.setCellStyle(styleB11);
	    			
	    			HSSFCell totalqtySCM = totalRow.createCell(colNo++);
	    			if(VTOTAL_QTY_SCM.elementAt(p).equals("-"))
	                {
	    				totalqtySCM.setCellValue("-");
	                }
	                else
	                {
	                	totalqtySCM.setCellValue(Double.parseDouble(VTOTAL_QTY_SCM.elementAt(p).toString()));
	                }
	    			totalqtySCM.setCellStyle(styleB11);
	    			
	    			if((plant_size+1) == o)
	    			{
	    				p++;
						break;
	    			}
    			}
    			k = k+1;
    			break;
    		}
    		
    	}
	}
}
else
{
	HSSFSheet sheet = workbook.createSheet("Nodata");
    int rowNum = 0;
    
	HSSFRow abbrRow = sheet.createRow(rowNum++);
    HSSFCell acell0 = abbrRow.createCell(0);
    
    CellRangeAddress counterPartyAbbr =new CellRangeAddress(rowNum-1, rowNum-1, 0, 11);
    sheet.addMergedRegion(counterPartyAbbr);
    
    RegionUtil.setBorderTop(BorderStyle.THIN, counterPartyAbbr, sheet);
	RegionUtil.setBorderBottom(BorderStyle.THIN, counterPartyAbbr, sheet);
	RegionUtil.setBorderLeft(BorderStyle.THIN, counterPartyAbbr, sheet);
	RegionUtil.setBorderRight(BorderStyle.THIN, counterPartyAbbr, sheet);
	
	acell0.setCellStyle(styleB11);
    acell0.setCellValue("Please Select Customer");
}

ServletOutputStream out1 = response.getOutputStream();
workbook.write(out1);
out1.flush();
out1.close(); 

/* workbook.write(response.getOutputStream());
response.getOutputStream().flush();
workbook.close(); */
%>

</body>
</html>