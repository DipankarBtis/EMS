
<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import= "org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.ss.usermodel.*"%>
<%@page import= "org.apache.poi.ss.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
</script>
</head>
<jsp:useBean class="com.etrm.fms.mgmt_reports.DataBean_MGMT_Reports" id="mgmt_rpt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DataBean_Pdf_Generation" id="pdfFile" scope="request"/>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
//String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
//String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
String owner_logo=""+session.getAttribute("comp_logo")==null?"":""+session.getAttribute("comp_logo");
//String emp_cd=""+session.getAttribute("emp_cd")==null?"":""+session.getAttribute("emp_cd");

String qqNo = request.getParameter("qqNo")==null?"":request.getParameter("qqNo");
String qqDt = request.getParameter("qqDt")==null?"":request.getParameter("qqDt");
String vesselNm = request.getParameter("vesselNm")==null?"":request.getParameter("vesselNm");
String startDt = request.getParameter("startDt")==null?"":request.getParameter("startDt");
String endDt = request.getParameter("endDt")==null?"":request.getParameter("endDt");
String boeNo = request.getParameter("boeNo")==null?"":request.getParameter("boeNo");
String boeDt = request.getParameter("boeDt")==null?"":request.getParameter("boeDt");
String unloadedQty = request.getParameter("unloadedQty")==null?"":request.getParameter("unloadedQty");
String sugQty = request.getParameter("sugQty")==null?"":request.getParameter("sugQty");
String regassQty = request.getParameter("regassQty")==null?"":request.getParameter("regassQty");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String dealMap = request.getParameter("dealMap")==null?"":request.getParameter("dealMap");
String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
String contact_person = request.getParameter("contact_person")==null?"":request.getParameter("contact_person");
String gen_type = request.getParameter("gen_type")==null?"":request.getParameter("gen_type");


mgmt_rpt.setCallFlag("ENERGY_STATEMENT");
mgmt_rpt.setComp_cd(owner_cd);
mgmt_rpt.setCounterparty_cd(counterparty_cd);
mgmt_rpt.init();

HashMap<String, String> VIEW_LTCORA_INFO = mgmt_rpt.getVIEW_LTCORA_INFO();
HashMap<String, String> VIEW_LTCORA_CUSTOMER_INFO = mgmt_rpt.getVIEW_LTCORA_CUSTOMER_INFO();
	

String emp= VIEW_LTCORA_INFO.get("contact_person"+contact_person)==null?"":VIEW_LTCORA_INFO.get("contact_person"+contact_person);
String emp_name =session.getAttribute("emp_nm")==null?"":""+session.getAttribute("emp_nm");

VIEW_LTCORA_CUSTOMER_INFO.put("qqNo",qqNo);
VIEW_LTCORA_CUSTOMER_INFO.put("qqDt",qqDt);
VIEW_LTCORA_CUSTOMER_INFO.put("vesselNm",vesselNm);
VIEW_LTCORA_CUSTOMER_INFO.put("startDt",startDt);
VIEW_LTCORA_CUSTOMER_INFO.put("endDt",endDt);
VIEW_LTCORA_CUSTOMER_INFO.put("boeNo",boeNo);
VIEW_LTCORA_CUSTOMER_INFO.put("boeDt",boeDt);
VIEW_LTCORA_CUSTOMER_INFO.put("unloadedQty",unloadedQty);
VIEW_LTCORA_CUSTOMER_INFO.put("sugQty",sugQty);
VIEW_LTCORA_CUSTOMER_INFO.put("regassQty",regassQty);
VIEW_LTCORA_CUSTOMER_INFO.put("dealMap",dealMap);
VIEW_LTCORA_CUSTOMER_INFO.put("signing_dt",signing_dt);
VIEW_LTCORA_CUSTOMER_INFO.put("contact_person",emp_name);%>

<%
if(gen_type.equals("PDF")){
HttpServletRequest req = request;

pdfFile.setCallFlag("PDF_LTCORA_ENERGY_STATEMENT");
pdfFile.setRequest(req);
pdfFile.setComp_logo(owner_logo);
pdfFile.setComp_cd(owner_cd);
pdfFile.setEmp_cd(emp_cd);
pdfFile.setView_ltcora_info(VIEW_LTCORA_INFO);
pdfFile.setview_ltcora_customer_info(VIEW_LTCORA_CUSTOMER_INFO);
pdfFile.init();

String file_url = pdfFile.getFile_url();
String file_nm = pdfFile.getFile_nm();
String pdfpath = file_url+"/"+CommonVariable.work_dir+owner_cd+""+CommonVariable.ltcora_report_pdf_path+""+file_nm;
%>
<body>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="d-flex justify-content-between">
					    <div class="topheader">
					    	Energy Statement PDF
					    </div>
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="ratio ratio-1x1">
							  <iframe src="<%=pdfpath%>" title="Energy Statement PDF" allowfullscreen width="100%"></iframe>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
<%}else if(gen_type.equals("XLS")){
	
	String cust_address= VIEW_LTCORA_CUSTOMER_INFO.get("customerCity")+"";
	if(!VIEW_LTCORA_CUSTOMER_INFO.get("customerPin").toString().trim().equals(""))
	{
		cust_address+="-"+VIEW_LTCORA_CUSTOMER_INFO.get("customerPin");
	}
	if(!VIEW_LTCORA_CUSTOMER_INFO.get("customerCity").toString().trim().equals(""))
	{
		cust_address+=",";
	}
	
	String path =File.separator+"work_data"+owner_cd+File.separator;
	String file_path = request.getRealPath(path);

	HSSFWorkbook workbook = new HSSFWorkbook();		
	HSSFSheet sheet1 = workbook.createSheet(owner_abbr+" Energy Statement "+VIEW_LTCORA_CUSTOMER_INFO.get("customer_comp_abbr")+" "+dealMap);
	
	HSSFRow rowheader = sheet1.createRow((short)0);	
	rowheader.createCell((short) 0).setCellValue(owner_abbr+" Energy Statement "+VIEW_LTCORA_CUSTOMER_INFO.get("customer_comp_abbr")+" "+dealMap);
	sheet1.addMergedRegion(new CellRangeAddress(0,0,0,4));
	
	HSSFRow row1 = sheet1.createRow((short) 1);
	row1.createCell((short) 0).setCellValue("To: ");
	sheet1.addMergedRegion(new CellRangeAddress(1,1,0,4));
	sheet1.autoSizeColumn(1);		
	HSSFRow row2 = sheet1.createRow((short) 2);
	row2.createCell((short) 0).setCellValue(""+VIEW_LTCORA_CUSTOMER_INFO.get("contact_person_nm"));
	sheet1.addMergedRegion(new CellRangeAddress(2,2,0,4));
	sheet1.autoSizeColumn(1);		
	HSSFRow row3 = sheet1.createRow((short) 3);
	row3.createCell((short) 0).setCellValue(""+ VIEW_LTCORA_CUSTOMER_INFO.get("customer_comp_nm"));
	sheet1.addMergedRegion(new CellRangeAddress(3,3,0,4));
	sheet1.autoSizeColumn(1);		
	HSSFRow row4 = sheet1.createRow((short) 4);
	row4.createCell((short) 0).setCellValue(""+ VIEW_LTCORA_CUSTOMER_INFO.get("customerAddress"));
	sheet1.addMergedRegion(new CellRangeAddress(4,4,0,4));
	sheet1.autoSizeColumn(1);		
	HSSFRow row5 = sheet1.createRow((short) 5);
	row5.createCell((short) 0).setCellValue(""+cust_address);
	sheet1.addMergedRegion(new CellRangeAddress(5,5,0,4));
	sheet1.autoSizeColumn(1);		
	HSSFRow row6 = sheet1.createRow((short) 6);
	row6.createCell((short) 0).setCellValue(""+VIEW_LTCORA_CUSTOMER_INFO.get("customerState"));
	sheet1.addMergedRegion(new CellRangeAddress(6,6,0,4));
	sheet1.autoSizeColumn(1);		
	HSSFRow row7 = sheet1.createRow((short) 7);
	row7.createCell((short) 0).setCellValue("Tel: "+VIEW_LTCORA_CUSTOMER_INFO.get("contact_person_phone"));
	sheet1.addMergedRegion(new CellRangeAddress(7,7,0,4));
	sheet1.autoSizeColumn(1);		
	HSSFRow row8 = sheet1.createRow((short) 8);
	row8.createCell((short) 0).setCellValue("Email: "+VIEW_LTCORA_CUSTOMER_INFO.get("contact_person_fax"));
	sheet1.addMergedRegion(new CellRangeAddress(8,8,0,4));
	sheet1.autoSizeColumn(1);		
	
	HSSFRow blank=sheet1.createRow((short) 9);
	blank.createCell((short) 0).setCellValue("");
	sheet1.addMergedRegion(new CellRangeAddress(9,9,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow subject=sheet1.createRow((short) 10);
	subject.createCell((short) 1).setCellValue("Subject: Energy Statement for "+ VIEW_LTCORA_CUSTOMER_INFO.get("dealMap") +" dt."+sysdate);
	sheet1.addMergedRegion(new CellRangeAddress(10,10,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow blank1=sheet1.createRow((short) 11);
	blank1.createCell((short) 0).setCellValue("");
	sheet1.addMergedRegion(new CellRangeAddress(11,11,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow greet=sheet1.createRow((short) 12);
	greet.createCell((short) 0).setCellValue("Dear Sir/Madam,");
	sheet1.addMergedRegion(new CellRangeAddress(12,12,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow blank2=sheet1.createRow((short) 13);
	blank2.createCell((short) 0).setCellValue("");
	sheet1.addMergedRegion(new CellRangeAddress(13,13,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow blank3=sheet1.createRow((short) 14);
	blank3.createCell((short) 0).setCellValue("");
	sheet1.addMergedRegion(new CellRangeAddress(14,14,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow message1 = sheet1.createRow((short) 15);
	message1.createCell((short) 0).setCellValue("Please find below the statement showing the LNG unloaded quantities and the energy to be delivered to "+VIEW_LTCORA_CUSTOMER_INFO.get("customer_comp_nm"));
	sheet1.addMergedRegion(new CellRangeAddress(15,15,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow message2 = sheet1.createRow((short) 16);
	message2.createCell((short) 0).setCellValue(" as RLNG,under LTCORA dated "+VIEW_LTCORA_CUSTOMER_INFO.get("signing_dt")+" and subsequent side letters, therein.");
	sheet1.addMergedRegion(new CellRangeAddress(16,16,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow blank4=sheet1.createRow((short) 17);
	blank4.createCell((short) 0).setCellValue("");
	sheet1.addMergedRegion(new CellRangeAddress(17,17,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow title = sheet1.createRow((short) 18);
	title.createCell((short) 0).setCellValue("Energy Statement ["+owner_abbr+"-"+VIEW_LTCORA_CUSTOMER_INFO.get("customer_comp_abbr")+"]");
	sheet1.addMergedRegion(new CellRangeAddress(18,18,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow blank5=sheet1.createRow((short) 19);
	blank5.createCell((short) 0).setCellValue("");
	sheet1.addMergedRegion(new CellRangeAddress(19,19,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow details1 = sheet1.createRow((short) 20);
	details1.createCell((short) 0).setCellValue("Q & Q Certificate#");
	details1.createCell((short) 1).setCellValue(""+VIEW_LTCORA_CUSTOMER_INFO.get("qqNo"));
	details1.createCell((short) 2).setCellValue("Vessel Arrival Date:");
	details1.createCell((short) 3).setCellValue(""+VIEW_LTCORA_CUSTOMER_INFO.get("startDt"));
	
	for(int i=0;i<4;i++)
	{
		sheet1.autoSizeColumn(i);
	}
	
	HSSFRow details2 = sheet1.createRow((short) 21);
	details2.createCell((short) 0).setCellValue("Vessel Name:");
	details2.createCell((short) 1).setCellValue(""+VIEW_LTCORA_CUSTOMER_INFO.get("vesselNm"));
	details2.createCell((short) 2).setCellValue("Storage Period Start Date:");
	details2.createCell((short) 3).setCellValue(""+VIEW_LTCORA_CUSTOMER_INFO.get("startDt"));
	for(int i=0;i<4;i++)
	{
		sheet1.autoSizeColumn(i);
	}
	
	HSSFRow details3 = sheet1.createRow((short) 22);
	details3.createCell((short) 0).setCellValue("BOE No.:");
	details3.createCell((short) 1).setCellValue(""+VIEW_LTCORA_CUSTOMER_INFO.get("boeNo"));
	details3.createCell((short) 2).setCellValue("Storage Period End Date :");
	details3.createCell((short) 3).setCellValue(""+VIEW_LTCORA_CUSTOMER_INFO.get("endDt"));
	for(int i=0;i<4;i++)
	{
		sheet1.autoSizeColumn(i);
	}
	
	HSSFRow blank6=sheet1.createRow((short) 23);
	blank6.createCell((short) 0).setCellValue("");
	sheet1.addMergedRegion(new CellRangeAddress(23,23,0,4));
	sheet1.autoSizeColumn(1);
	
	HSSFRow details4 =sheet1.createRow((short) 24);
	details4.createCell((short) 0).setCellValue("");
	sheet1.addMergedRegion(new CellRangeAddress(24,24,0,2));
	details4.createCell((short) 3).setCellValue("MMBTU");
	for(int i=0;i<4;i++)
	{
		sheet1.autoSizeColumn(i);
	}
	
	HSSFRow details5 =sheet1.createRow((short) 25);
	details5.createCell((short) 0).setCellValue("LNG unloaded as per the certificate of Quantity & Quality");
	sheet1.addMergedRegion(new CellRangeAddress(25,25,0,2));
	details5.createCell((short) 3).setCellValue(VIEW_LTCORA_CUSTOMER_INFO.get("unloadedQty"));
	for(int i=0;i<4;i++)
	{
		sheet1.autoSizeColumn(i);
	}
	
	HSSFRow details6 =sheet1.createRow((short) 26);
	details6.createCell((short) 0).setCellValue("SUG as per LTCORA");
	sheet1.addMergedRegion(new CellRangeAddress(26,26,0,2));
	details6.createCell((short) 3).setCellValue(VIEW_LTCORA_CUSTOMER_INFO.get("sugQty"));
	for(int i=0;i<4;i++)
	{
		sheet1.autoSizeColumn(i);
	}
	
	HSSFRow details7 =sheet1.createRow((short) 27);
	details7.createCell((short) 0).setCellValue("LNG to be Regassified and delivered as RLNG to Gail till end of storage period ");
	sheet1.addMergedRegion(new CellRangeAddress(27,27,0,2));
	details7.createCell((short) 3).setCellValue(VIEW_LTCORA_CUSTOMER_INFO.get("regassQty"));
	for(int i=0;i<4;i++)
	{
		sheet1.autoSizeColumn(i);
	}
	
	HSSFRow signature =sheet1.createRow((short) 28);
	//signature.createCell((short) 0).setCellValue(""+VIEW_LTCORA_CUSTOMER_INFO.get("contact_person"));
	signature.createCell((short) 0).setCellValue(emp_name);
	sheet1.addMergedRegion(new CellRangeAddress(28,28,0,3));
	
	HSSFRow signature1 =sheet1.createRow((short) 29);
	signature1.createCell((short) 0).setCellValue("Authorised Signatory ");
	sheet1.addMergedRegion(new CellRangeAddress(29,29,0,3));
	for(int i=0;i<4;i++)
	{
		sheet1.autoSizeColumn(i);
	}
	
	HSSFRow compNm = sheet1.createRow((short) 30);
	compNm.createCell((short) 0).setCellValue(""+owner_nm);
	sheet1.addMergedRegion(new CellRangeAddress(30,30,0,3));
	for(int i=0;i<4;i++)
	{
		sheet1.autoSizeColumn(i);
	}
	
	String fileName = file_path+File.separator+owner_abbr+" Energy Statement "+VIEW_LTCORA_CUSTOMER_INFO.get("customer_comp_abbr")+" "+dealMap+".xls";

	FileOutputStream fileOut = new FileOutputStream(fileName);  

	int numberOfSheets = workbook.getNumberOfSheets();
	    
	workbook.write(fileOut);
	fileOut.close();
	%>
	<body>
		<a id="multisheetIcon"></a>
	</body>
<script type="text/javascript">
document.getElementById("multisheetIcon").href = "../work_data<%=owner_cd%>/<%=owner_abbr+" Energy Statement "+VIEW_LTCORA_CUSTOMER_INFO.get("customer_comp_abbr")+" "+dealMap+".xls"%>";
document.getElementById("multisheetIcon").click();
if(confirm("Generated Successfully!"))
{ 
	window.close();
}
</script>
<%}%>
</html>