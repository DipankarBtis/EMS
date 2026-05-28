<%@page import= "java.util.*"%>
<%@page import= "java.io.*"%>
<%@page import= "org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.ss.usermodel.*"%>
<%@page import= "org.apache.poi.ss.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function redirectToReconReport()
{	
	var u=document.forms[0].u.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var url="rpt_sap_recon_consolidate_report.jsp?u="+u+"&month="+month+"&year="+year;

	location.replace(url);
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.extn_interface.DataBean_sap_interface" id="db_intrface" scope="request"></jsp:useBean>

<%@ include file="../util/common_js.jsp"%>
<%
String sysdate=utildate.getSysdate();
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String month=request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year=request.getParameter("year")==null?""+currentYear:request.getParameter("year");
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
String gl_cd = request.getParameter("gl_cd")==null?"0":request.getParameter("gl_cd");
String tds = request.getParameter("tds")==null?"Y":request.getParameter("tds");
String tcs = request.getParameter("tcs")==null?"Y":request.getParameter("tcs");
String tax = request.getParameter("tax")==null?"Y":request.getParameter("tax");

if(month.length() == 1)
{
	month="0"+month; 
}

String temp_sysdt = sysdate.split("/")[0]+"_"+sysdate.split("/")[1]+"_"+sysdate.split("/")[2];
String main_folder="";
if(!owner_cd.equals(""))
{
	main_folder=CommonVariable.work_dir+owner_cd;
}
File MainDir = new File(application.getRealPath("/")+main_folder);
if(!MainDir.exists()) 
{
	MainDir.mkdir();
}
String sub_folder=""+CommonVariable.sap_reports;
File SubDir = new File(application.getRealPath("/")+main_folder+File.separator+sub_folder);
if(!SubDir.exists()) 
{
	SubDir.mkdir();
}

String path = File.separator+main_folder+File.separator+sub_folder+File.separator;
//String path =File.separator+"work_data"+owner_cd+File.separator;
String file_path = request.getRealPath(path);

//String path =File.separator+"work_data"+owner_cd+File.separator;
//String file_path = request.getRealPath(path);


HSSFWorkbook workbook = new HSSFWorkbook();		
HSSFSheet sheet1 = workbook.createSheet("SAP Recon (Consolidate) Report");
HSSFRow rowheader = sheet1.createRow((short)0);	
rowheader.createCell((short) 0).setCellValue("SAP Recon (Consolidate) Report ");
sheet1.addMergedRegion(new CellRangeAddress(0,0,0,12));
int l = 0;
int x = 0;
int m=0;
int row=0;
db_intrface.setCallFlag("SAP_RECON_CONSOLIDATE_RPT");
db_intrface.setComp_cd(owner_cd);
db_intrface.setCounterparty_cd(counterparty_cd);
db_intrface.setMonth(month);
db_intrface.setYear(year);
db_intrface.setEntity_role(entity_role);
db_intrface.setGl_cd(gl_cd);
db_intrface.setTds_flag(tds);
db_intrface.setTcs_flag(tcs);
db_intrface.setTax_flag(tax);
db_intrface.init();

String amt_sum = db_intrface.getAmt_sum();
String amt_usd_sum = db_intrface.getAmt_usd_sum();
String sap_sum = db_intrface.getSap_sum();
String sap_sum_usd = db_intrface.getSap_Sum_Usd();
String delta_sap_sum=db_intrface.getDelta_Sap_Sum();
String abs_delta_sap_sum = db_intrface.getAbs_delta_sap_sum();

Vector VCON_GL = db_intrface.getVCON_GL();
Vector VCON_CURR = db_intrface.getVCON_CURR();
Vector VCON_COCD = db_intrface.getVCON_COCD();
Vector VCON_PERIOD=db_intrface.getVCON_PERIOD();
Vector VCON_GL_DECR=db_intrface.getVCON_GL_DECR();
Vector VCON_YEAR=db_intrface.getVCON_YEAR();
Vector VCON_MONTH=db_intrface.getVCON_MONTH();
Vector VCON_EMS_AMT = db_intrface.getVCON_EMS_AMT();
Vector VCON_EMS_AMT_USD = db_intrface.getVCON_EMS_AMT_USD();
Vector VCON_SAP_AMT = db_intrface.getVCON_SAP_AMT();
Vector VCON_SAP_AMT_USD = db_intrface.getVCON_SAP_AMT_USD();
Vector VCON_DIFF_AMT = db_intrface.getVCON_DIFF_AMT();
Vector VCON_ABS_DIFF_AMT = db_intrface.getVCON_ABS_DIFF_AMT();

//Table Headers 
HSSFRow rowhead2 = sheet1.createRow((short) ++row);
rowhead2.createCell((short) 0).setCellValue("CoCd");
rowhead2.createCell((short) 1).setCellValue("Period");  
rowhead2.createCell((short) 2).setCellValue("GL Account");  
rowhead2.createCell((short) 3).setCellValue("GL Description");
rowhead2.createCell((short) 4).setCellValue("Currency");
rowhead2.createCell((short) 5).setCellValue("Year");
rowhead2.createCell((short) 6).setCellValue("Month"); 
rowhead2.createCell((short) 7).setCellValue("EMS USD Value");
rowhead2.createCell((short) 8).setCellValue("SAP USD Value");
rowhead2.createCell((short) 9).setCellValue("EMS INR Value");
rowhead2.createCell((short) 10).setCellValue("SAP INR Value");
rowhead2.createCell((short) 11).setCellValue("Difference Value");
rowhead2.createCell((short) 12).setCellValue("ABS Difference Value");

for(int i=0;i<13;i++)
{
	sheet1.autoSizeColumn(i);	
}

if(VCON_GL.size()>0)
{
	for(int i=0;i<VCON_GL.size();i++)
	{
		HSSFRow rowBody = sheet1.createRow(++row);
		rowBody.createCell((short) 0).setCellValue(""+VCON_COCD.elementAt(i));
		rowBody.createCell((short) 1).setCellValue(""+VCON_PERIOD.elementAt(i));
		rowBody.createCell((short) 2).setCellValue(""+VCON_GL.elementAt(i));
		rowBody.createCell((short) 3).setCellValue(""+VCON_GL_DECR.elementAt(i) );
		rowBody.createCell((short) 4).setCellValue(""+VCON_CURR.elementAt(i));
		rowBody.createCell((short) 5).setCellValue(""+VCON_YEAR.elementAt(i));
		rowBody.createCell((short) 6).setCellValue(""+VCON_MONTH.elementAt(i) );
		if(!VCON_EMS_AMT_USD.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 7).setCellValue(Double.parseDouble(""+VCON_EMS_AMT_USD.elementAt(i)));
		}
		else
		{
			rowBody.createCell((short) 7).setCellValue("");
		}
		if(!VCON_SAP_AMT_USD.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 8).setCellValue(Double.parseDouble(""+VCON_SAP_AMT_USD.elementAt(i)));
		}
		else
		{
			rowBody.createCell((short) 8).setCellValue("");
		}
		if(!VCON_EMS_AMT.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 9).setCellValue(Double.parseDouble(""+VCON_EMS_AMT.elementAt(i)));
		}
		else
		{
			rowBody.createCell((short) 9).setCellValue("");
		}
		if(!VCON_SAP_AMT.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 10).setCellValue(Double.parseDouble(""+VCON_SAP_AMT.elementAt(i)));
		}
		else
		{
			rowBody.createCell((short) 10).setCellValue("");
		}
		if(!VCON_DIFF_AMT.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 11).setCellValue(Double.parseDouble(""+VCON_DIFF_AMT.elementAt(i)));
		}
		else
		{
			rowBody.createCell((short)11).setCellValue("");
		}
		if(!VCON_ABS_DIFF_AMT.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 12).setCellValue(Double.parseDouble(""+VCON_ABS_DIFF_AMT.elementAt(i)));
		}
		else
		{
			rowBody.createCell((short)12).setCellValue("");
		}
	}
	HSSFRow row_head2 = sheet1.createRow((short)++row);
	row_head2.createCell((short) 0).setCellValue("Total: ");
	sheet1.addMergedRegion(new CellRangeAddress(row,row,0,6));
	row_head2.createCell((short) 7).setCellValue(Double.parseDouble(amt_usd_sum));
	row_head2.createCell((short) 8).setCellValue(Double.parseDouble(sap_sum_usd));
	row_head2.createCell((short) 9).setCellValue(Double.parseDouble(amt_sum));
	row_head2.createCell((short) 10).setCellValue(Double.parseDouble(sap_sum));
	row_head2.createCell((short) 11).setCellValue(Double.parseDouble(delta_sap_sum));
	row_head2.createCell((short) 12).setCellValue(Double.parseDouble(abs_delta_sap_sum));
	for(int i=0;i<13;i++)
	{
		sheet1.autoSizeColumn(i);	
	}
}
else
{
	HSSFRow row_head2 = sheet1.createRow((short)++row);
	row_head2.createCell((short) 0).setCellValue("No Records found for the given date range!");
	sheet1.addMergedRegion(new CellRangeAddress(row,row,0,12));
}
String fileName = file_path+File.separator+"SAP_Recon_Consolidate_Report_"+temp_sysdt+".xls";
String downloadFilePath="../"+main_folder+"/"+sub_folder+"/"+"SAP_Recon_Consolidate_Report_"+temp_sysdt+".xls";
FileOutputStream fileOut = new FileOutputStream(fileName);  

int numberOfSheets = workbook.getNumberOfSheets();
    
workbook.write(fileOut);
fileOut.close();
 %>
  <body>
 <form action="">
<a id="multisheetIcon"></a>

<input type="hidden" name="year" value="<%=year%>">
<input type="hidden" name="month" value="<%=month%>">
<input type="hidden" name="tax" value="<%=tax%>">
<input type="hidden" name="tcs" value="<%=tcs%>">
<input type="hidden" name="tds" value="<%=tds%>">
<input type="hidden" name="entity_role" value="<%=entity_role%>">
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
<%-- document.getElementById("multisheetIcon").href = "../work_data<%=owner_cd%>/SAP Recon Consolidate Report.xls"; --%>
document.getElementById("multisheetIcon").href = "<%=downloadFilePath%>";
document.getElementById("multisheetIcon").click();
if(confirm("Generated Successfully!"))
{
	redirectToReconReport();
}
</script>
<%}else{%>
<script type="text/javascript">
alert("Not Generated");
redirectToReconReport();
</script>
<%} %>
</body>
</html>