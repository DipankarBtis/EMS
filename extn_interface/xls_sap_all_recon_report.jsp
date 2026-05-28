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
	var counterparty_cd=document.forms[0].counterparty_cd.value;
	var from_dt= document.forms[0].from_dt.value;
	var to_dt= document.forms[0].to_dt.value;
	var entity_role= document.forms[0].entity_role.value;
	var tds = document.forms[0].tds;
	var tcs = document.forms[0].tcs;
	var tax = document.forms[0].tax;
	var url = "rpt_sap_recon_report.jsp?entity_role="+entity_role+"&from_dt="+from_dt+"&to_dt="+to_dt
			+"&counterparty_cd="+counterparty_cd+"&tds="+tds.value+"&tcs="+tcs.value+"&tax="+tax.value+"&u="+u;
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
String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
//String counterparty_cd = "0";
String from_dt = request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt = request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String entity_role = request.getParameter("entity_role")==null?"0":request.getParameter("entity_role");
String gl_cd = request.getParameter("gl_cd")==null?"0":request.getParameter("gl_cd");
String tds = request.getParameter("tds")==null?"Y":request.getParameter("tds");
String tcs = request.getParameter("tcs")==null?"Y":request.getParameter("tcs");
String tax = request.getParameter("tax")==null?"Y":request.getParameter("tax");

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

HSSFWorkbook workbook = new HSSFWorkbook();		
HSSFSheet sheet1 = workbook.createSheet("SAP Recon Report");
HSSFRow rowheader = sheet1.createRow((short)0);	
rowheader.createCell((short) 0).setCellValue("SAP Recon Report ");
sheet1.addMergedRegion(new CellRangeAddress(0,0,0,22));
int l = 0;
int x = 0;
int m=0;
int row=0;

db_intrface.setCallFlag("SAP_RECON_RPT");
db_intrface.setComp_cd(owner_cd);
db_intrface.setCounterparty_cd("0");
db_intrface.setFrom_dt(from_dt);
db_intrface.setTo_dt(to_dt);
db_intrface.setEntity_role(entity_role);
db_intrface.setGl_cd(gl_cd);
db_intrface.setTds_flag(tds);
db_intrface.setTcs_flag(tcs);
db_intrface.setTax_flag(tax);
db_intrface.init(); 

String amt_sum = db_intrface.getAmt_sum();
String qty_sum = db_intrface.getQty_sum();
String amt_usd_sum = db_intrface.getAmt_usd_sum();
String sap_sum = db_intrface.getSap_sum();
String delta_sap_sum=db_intrface.getDelta_Sap_Sum();
String sap_sum_usd=db_intrface.getSap_Sum_Usd();
String delta_sap_sum_usd = db_intrface.getDelta_Sap_Sum_Usd();

//fetching vectors 
Vector VCOUNTERPARTY_CD = db_intrface.getVCOUNTERPARTY_CD();
Vector VCO_CD = db_intrface.getVCO_CD();
Vector VDEAL_MAP = db_intrface.getVDEAL_MAP();
Vector VINVOICE_NO = db_intrface.getVINVOICE_NO();
Vector VACCOUNT_PERIOD_YR = db_intrface.getVACCOUNT_PERIOD_YR();
Vector VACCOUNT_PERIOD_MONTH = db_intrface.getVACCOUNT_PERIOD_MONTH();
Vector VPERIOD_START_DT = db_intrface.getVPERIOD_START_DT();
Vector VPERIOD_END_DT = db_intrface.getVPERIOD_END_DT();
Vector VGL_CD = db_intrface.getVGL_CD();
Vector VGL_DESC = db_intrface.getVGL_DESC();
Vector VVENDOR_CD = db_intrface.getVVENDOR_CD();
Vector VCURRENCY = db_intrface.getVCURRENCY();
Vector VDOC_TYPE = db_intrface.getVDOC_TYPE();
Vector VPK = db_intrface.getVPK();
Vector VDOC_NO=db_intrface.getVDOC_NO();
Vector VITEMTEXT=db_intrface.getVITEMTEXT();
Vector VTRANS_TYPE=db_intrface.getVTRANS_TYPE();
Vector VSAP_APPROVED_BY=db_intrface.getVSAP_APPROVED_BY();
Vector VPOST_DT = db_intrface.getVPOST_DT();
Vector VDOC_DT = db_intrface.getVDOC_DT();
Vector VALLOC_QTY = db_intrface.getVALLOC_QTY();
Vector VAMT = db_intrface.getVAMT(); 
Vector VUSD_AMT = db_intrface.getVUSD_AMT();
Vector VSAP_VALUE_INR = db_intrface.getVSAP_VALUE_INR();
Vector VDELTA_SAP_VALUE_INR = db_intrface.getVDELTA_SAP_VALUE_INR();
Vector VSAP_VALUE_USD = db_intrface.getVSAP_VALUE_USD();
Vector VDELTA_SAP_VALUE_USD = db_intrface.getVDELTA_SAP_VALUE_USD();
Vector VVENDOR_NM = db_intrface.getVVENDOR_NM();

/* HSSFRow headSpace=sheet1.createRow((short) ++row);
headSpace.createCell((short) 0).setCellValue("");
sheet1.addMergedRegion(new CellRangeAddress(row,row,0,22)); */

//First row
/* HSSFRow rowhead1 = sheet1.createRow((short) ++row);	
rowhead1.createCell((short) 0).setCellValue(""+VENITIY_ROLE_NM.elementAt(a));
sheet1.addMergedRegion(new CellRangeAddress(row,row,0,22)); */

//Table Headers 
HSSFRow rowhead2 = sheet1.createRow((short) ++row);
rowhead2.createCell((short) 0).setCellValue("CoCd");
rowhead2.createCell((short) 1).setCellValue("Contract Ref#");  
rowhead2.createCell((short) 2).setCellValue("EMS Reference#");  
rowhead2.createCell((short) 3).setCellValue("Year");
rowhead2.createCell((short) 4).setCellValue("Billing Peroid");
rowhead2.createCell((short) 5).setCellValue("Month");
//rowhead2.createCell((short) 6).setCellValue("Vendor Cd"); 
rowhead2.createCell((short) 6).setCellValue("GL"); 
rowhead2.createCell((short) 7).setCellValue("GL Description");
rowhead2.createCell((short) 8).setCellValue("Currency");
rowhead2.createCell((short) 9).setCellValue("Doc.Type");
rowhead2.createCell((short) 10).setCellValue("P/Ky");
rowhead2.createCell((short) 11).setCellValue("SAP Document No");
rowhead2.createCell((short) 12).setCellValue("GL Line Item- Text");
rowhead2.createCell((short) 13).setCellValue("Transaction Type");
rowhead2.createCell((short) 14).setCellValue("User Name");
rowhead2.createCell((short) 15).setCellValue("Amt in Loc -USD");
rowhead2.createCell((short) 16).setCellValue("Amt in Doc Currency-INR");
rowhead2.createCell((short) 17).setCellValue("Document Date");
rowhead2.createCell((short) 18).setCellValue("Posting Date");
rowhead2.createCell((short) 19).setCellValue("Quantity");
rowhead2.createCell((short) 20).setCellValue("SAP Code");
rowhead2.createCell((short) 21).setCellValue("Vendor Name");
rowhead2.createCell((short) 22).setCellValue("Amt in Loc -USD");
rowhead2.createCell((short) 23).setCellValue("Amt in Loc-INR");
rowhead2.createCell((short) 24).setCellValue("Difference in Loc -USD");
rowhead2.createCell((short) 25).setCellValue("Difference in Loc -INR");
/* rowhead2.createCell((short) 22).setCellValue("Difference Value EMS and SAP"); */
for(int i=0;i<26;i++)
{
	sheet1.autoSizeColumn(i);	
}

if(VCOUNTERPARTY_CD.size()>0)
{
	for(int i=0;i<VCOUNTERPARTY_CD.size();i++)
	{
		HSSFRow rowBody = sheet1.createRow(++row);
		rowBody.createCell((short) 0).setCellValue(""+VCO_CD.elementAt(i));
		rowBody.createCell((short) 1).setCellValue(""+VDEAL_MAP.elementAt(i));
		rowBody.createCell((short) 2).setCellValue(""+VINVOICE_NO.elementAt(i) );
		rowBody.createCell((short) 3).setCellValue(""+VACCOUNT_PERIOD_YR.elementAt(i));
		rowBody.createCell((short) 4).setCellValue(""+VPERIOD_START_DT.elementAt(i)+"-"+VPERIOD_END_DT.elementAt(i));
		rowBody.createCell((short) 5).setCellValue(""+VACCOUNT_PERIOD_MONTH.elementAt(i) );
		//rowBody.createCell((short) 6).setCellValue(""+VVENDOR_CD.elementAt(i) );
		rowBody.createCell((short) 6).setCellValue(""+VGL_CD.elementAt(i));
		rowBody.createCell((short) 7).setCellValue(""+VGL_DESC.elementAt(i) );
		rowBody.createCell((short) 8).setCellValue(""+VCURRENCY.elementAt(i) );
		rowBody.createCell((short) 9).setCellValue(""+VDOC_TYPE.elementAt(i) );
		rowBody.createCell((short) 10).setCellValue(""+VPK.elementAt(i) );
		rowBody.createCell((short) 11).setCellValue(""+VDOC_NO.elementAt(i) );
		rowBody.createCell((short) 12).setCellValue(""+VITEMTEXT.elementAt(i) );
		rowBody.createCell((short) 13).setCellValue(""+VTRANS_TYPE.elementAt(i) );
		rowBody.createCell((short) 14).setCellValue(""+VSAP_APPROVED_BY.elementAt(i) );
		if(VUSD_AMT.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 15).setCellValue("");
		}
		else
		{
			rowBody.createCell((short) 15).setCellValue(Double.parseDouble(""+VUSD_AMT.elementAt(i)));
		}
		if(VAMT.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 16).setCellValue("");
		}
		else
		{
			rowBody.createCell((short) 16).setCellValue(Double.parseDouble(""+VAMT.elementAt(i)));
		}
		rowBody.createCell((short) 17).setCellValue(""+VDOC_DT.elementAt(i) );
		rowBody.createCell((short) 18).setCellValue(""+VPOST_DT.elementAt(i) );
		if(VALLOC_QTY.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 19).setCellValue("");
		}
		else
		{
			rowBody.createCell((short) 19).setCellValue(Double.parseDouble(""+VALLOC_QTY.elementAt(i)));
		}
		rowBody.createCell((short) 20).setCellValue(""+VVENDOR_CD.elementAt(i) );
		rowBody.createCell((short) 21).setCellValue(""+VVENDOR_NM.elementAt(i));
		if(VSAP_VALUE_USD.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 22).setCellValue("");
		}
		else
		{
			rowBody.createCell((short) 22).setCellValue(Double.parseDouble(VSAP_VALUE_USD.elementAt(i).toString()));
		}
		if(VSAP_VALUE_INR.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 23).setCellValue("");
		}
		else
		{
			rowBody.createCell((short) 23).setCellValue(Double.parseDouble(VSAP_VALUE_INR.elementAt(i).toString()));
		}
		if(VDELTA_SAP_VALUE_USD.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 24).setCellValue("");
		}
		else
		{
			rowBody.createCell((short) 24).setCellValue(Double.parseDouble(VDELTA_SAP_VALUE_USD.elementAt(i).toString()));
		}
		if(VDELTA_SAP_VALUE_INR.elementAt(i).equals(""))
		{
			rowBody.createCell((short) 25).setCellValue("");
		}
		else
		{
			rowBody.createCell((short) 25).setCellValue(Double.parseDouble(VDELTA_SAP_VALUE_INR.elementAt(i).toString()));
		}
		sheet1.autoSizeColumn(i);		
	}
	HSSFRow row_head2 = sheet1.createRow((short)++row);
	row_head2.createCell((short) 0).setCellValue("Total: ");
	sheet1.addMergedRegion(new CellRangeAddress(row,row,0,14));
	row_head2.createCell((short) 15).setCellValue(Double.parseDouble(amt_usd_sum));
	row_head2.createCell((short) 16).setCellValue(Double.parseDouble(amt_sum));
	sheet1.addMergedRegion(new CellRangeAddress(row,row,17,18));
	row_head2.createCell((short) 19).setCellValue(Double.parseDouble(qty_sum));
	row_head2.createCell((short) 20).setCellValue("");
	sheet1.addMergedRegion(new CellRangeAddress(row,row,20,21));
	row_head2.createCell((short) 22).setCellValue(Double.parseDouble(sap_sum_usd));
	row_head2.createCell((short) 23).setCellValue(Double.parseDouble(sap_sum));
	row_head2.createCell((short) 24).setCellValue(Double.parseDouble(delta_sap_sum_usd));
	row_head2.createCell((short) 25).setCellValue(Double.parseDouble(delta_sap_sum));
	/* sheet1.addMergedRegion(new CellRangeAddress(row,row,20,21)); */
	for(int i=0;i<26;i++)
	{
		sheet1.autoSizeColumn(i);	
	}
}
else
{
	HSSFRow row_head2 = sheet1.createRow((short)++row);
	row_head2.createCell((short) 0).setCellValue("No Records found for the given date range!");
	sheet1.addMergedRegion(new CellRangeAddress(row,row,0,26));
}

String fileName = file_path+File.separator+"SAP_Recon_Report_"+temp_sysdt+".xls";
String downloadFilePath="../"+main_folder+"/"+sub_folder+"/"+"SAP_Recon_Report_"+temp_sysdt+".xls";
FileOutputStream fileOut = new FileOutputStream(fileName);  

int numberOfSheets = workbook.getNumberOfSheets();
    
workbook.write(fileOut);
fileOut.close();
	
 %>
 <body>
 <form action="">
<a id="multisheetIcon"></a>

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