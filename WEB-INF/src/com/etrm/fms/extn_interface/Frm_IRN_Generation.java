package com.etrm.fms.extn_interface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

@WebServlet("/servlet/Frm_IRN_Generation")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class Frm_IRN_Generation extends HttpServlet
{
	static String db_src_file_name="Frm_IRN_Generation.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_IRN_Generation";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	private static String query3 = null;
	private static String query4 = null;
	private static PreparedStatement stmt = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	private static PreparedStatement stmt5 = null;
	private static PreparedStatement stmt6 = null;
	private static PreparedStatement stmt7 = null;
	private static PreparedStatement stmt8 = null;
	
	private static ResultSet rset = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	private static ResultSet rset5 = null;
	private static ResultSet rset6 = null;
	private static ResultSet rset7 = null;
	private static ResultSet rset8 = null;
	
	public static String form_id = "0";
	public static String form_nm = "";
	public static String mod_cd = "0";
	public static String mod_nm = "";
	public static String u = "";
	
	public static String old_value="";
	public static String new_value="";
	
	public static String emp_cd="";
	public static String comp_cd="";
	public static String comp_abbr="";
	public static String emp_nm="";
	public static String ip="";
	
	public static String commonUrl_pra="";
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static TaxCalculator TaxCalc = new TaxCalculator(); 
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	static NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		synchronized(this)
		{
			String function_nm="doPost()";
			HttpSession session = request.getSession();
			if(session.getAttribute("emp_uid")==null || session.getAttribute("emp_uid")=="")
			{
				url="../sess/Expire.jsp";
			}
			else
			{
				try
				{
					Context Context = new InitialContext();
					Context envContext  = (Context)Context.lookup("java:/comp/env");
					DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
					
					if(ds != null)
					{
						dbcon = ds.getConnection();				
					}
					else
					{
						System.out.println("Data Source Not Found");
					}
					if(dbcon != null)
					{
						dbcon.setAutoCommit(false);
						
						form_id=request.getParameter("form_cd")==null?"0":request.getParameter("form_cd");
						form_nm=request.getParameter("form_nm")==null?"":request.getParameter("form_nm");
						mod_cd=request.getParameter("mod_cd")==null?"0":request.getParameter("mod_cd");
						mod_nm=request.getParameter("mod_nm")==null?"":request.getParameter("mod_nm");
						
						emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
						comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
						comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
						emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
						ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
						u=request.getParameter("u")==null?"":request.getParameter("u");
						
						new_value="";
						old_value="";
						
						option=request.getParameter("option")==null?"":request.getParameter("option");
						
						commonUrl_pra = "&u="+u;
						
						if(option.equalsIgnoreCase("GENERATE_EXCEL_FOR_IRN"))
						{
							GenerateExcelForIRN(request);
						}
						else if(option.equalsIgnoreCase("UPLOAD_RESPONSE_IRN_EXCEL"))
						{
							UploadResponseIRNExcel(request);
						}
						else if(option.equalsIgnoreCase("OTH_GENERATE_EXCEL_FOR_IRN"))
						{
							GenerateExcelForOthIRN(request);
						}
						else if(option.equalsIgnoreCase("OTH_UPLOAD_RESPONSE_IRN_EXCEL"))
						{
							UploadResponseOthIRNExcel(request);
						}
						
						dbcon.close();
					}
					dbcon=null;
				}
				catch(Exception e)
				{
					new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					url=CommonVariable.errorpage_url+"?e="+e;
				}
				finally
				{
					if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
					if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
					if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
					if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
					if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
					if(rset5 != null){try {rset5.close();}catch(SQLException e){System.out.println("rset5 is not close " + e);}}
					if(rset6 != null){try {rset6.close();}catch(SQLException e){System.out.println("rset6 is not close " + e);}}
					if(rset7 != null){try {rset7.close();}catch(SQLException e){System.out.println("rset7 is not close " + e);}}
					if(rset8 != null){try {rset8.close();}catch(SQLException e){System.out.println("rset8 is not close " + e);}}
					if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
					if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
					if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
					if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
					if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
					if(stmt5 != null){try {stmt5.close();}catch(SQLException e){System.out.println("stmt5 is not close " + e);}}
					if(stmt6 != null){try {stmt6.close();}catch(SQLException e){System.out.println("stmt6 is not close " + e);}}
					if(stmt7 != null){try {stmt7.close();}catch(SQLException e){System.out.println("stmt7 is not close " + e);}}
					if(stmt8 != null){try {stmt8.close();}catch(SQLException e){System.out.println("stmt8 is not close " + e);}}
					if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
				}
			}
			
			try
			{
				response.sendRedirect(url);
			}
			catch(IOException e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
		}
	}
	
	private void GenerateExcelForIRN(HttpServletRequest request) throws SQLException 
	{
		String function_nm="GenerateExcelForIRN()";
		msg="";
		msg_type="";
		url="";
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		try
		{
			String invoice_no[] = request.getParameterValues("invoice_no");
			String counterparty_cd[] = request.getParameterValues("counterparty_cd");
			String contract_type[] = request.getParameterValues("contract_type");
			String sys_or_ff_inv[] = request.getParameterValues("sys_or_ff_inv");
			String inv_type[] = request.getParameterValues("inv_type");
			
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
			String fileName="",sheetnm="";
			
			if(invoice_no!=null)
			{
				String sysdate_time=utilDate.getSysdateWithTime24hrWithSS();
				String systime=sysdate_time.replaceAll(" ", "-");
				systime=systime.replaceAll(":", "");
				systime=systime.replaceAll("/", "");
				
				fileName ="INVOICE-"+month+"-"+year+"-"+systime+".xlsx";
				sheetnm="INVOICE-"+month+"-"+year+"-"+systime+"";
				
				String work_data=utilBean.getAutomationKeyDetail(dbcon, "WORK_DATA_"+comp_cd);
				
				String file_path=work_data+File.separator+"IRN_EXCEL"+File.separator+"Generated";
				
				if(!new File(file_path).exists())
				{
					new File(file_path).mkdirs();
				}
				
				file_path=file_path+File.separator+fileName;
	            XSSFSheet sheet = workbook.createSheet(sheetnm); 
	            Row rowhead = sheet.createRow((short)0);
	            int cellno=0;
	            rowhead.createCell((short)cellno++).setCellValue("LocationGstin");
	            rowhead.createCell((short)cellno++).setCellValue("LocationName");
	            rowhead.createCell((short)cellno++).setCellValue("ReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("LiabilityDischargeReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("ITCClaimReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("Purpose");
	            rowhead.createCell((short)cellno++).setCellValue("AutoPushOrGenerate");
	            rowhead.createCell((short)cellno++).setCellValue("SupplyType");
	            rowhead.createCell((short)cellno++).setCellValue("Irn");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentType");
	            rowhead.createCell((short)cellno++).setCellValue("TransactionType");
	            rowhead.createCell((short)cellno++).setCellValue("TransactionNature");
	            rowhead.createCell((short)cellno++).setCellValue("TransactionTypeDescription");
	            rowhead.createCell((short)cellno++).setCellValue("TaxpayerType");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentSeriesCode");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromGstin");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromLegalName");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromVendorCode");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromCity");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromPincode");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromPhone");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromEmail");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromGstin");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromVendorCode");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromCity");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromPincode");
	            rowhead.createCell((short)cellno++).setCellValue("BillToGstin");
	            rowhead.createCell((short)cellno++).setCellValue("BillToLegalName");
	            rowhead.createCell((short)cellno++).setCellValue("BillToTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("BillToVendorCode");
	            rowhead.createCell((short)cellno++).setCellValue("BillToAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("BillToAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("BillToCity");
	            rowhead.createCell((short)cellno++).setCellValue("BillToStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("BillToPincode");
	            rowhead.createCell((short)cellno++).setCellValue("BillToPhone");
	            rowhead.createCell((short)cellno++).setCellValue("BillToEmail");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToGstin");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToLegalName");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToVendorCode");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToCity");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToPincode");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentType");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentMode");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentAmount");
	            rowhead.createCell((short)cellno++).setCellValue("AdvancePaidAmount");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentDate");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentRemarks");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentTerms");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentInstruction");
	            rowhead.createCell((short)cellno++).setCellValue("PayeeName");
	            rowhead.createCell((short)cellno++).setCellValue("PayeeAccountNumber");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentAmountDue");
	            rowhead.createCell((short)cellno++).setCellValue("Ifsc");
	            rowhead.createCell((short)cellno++).setCellValue("CreditTransfer");
	            rowhead.createCell((short)cellno++).setCellValue("DirectDebit");
	            rowhead.createCell((short)cellno++).setCellValue("CreditDays");
	            rowhead.createCell((short)cellno++).setCellValue("CreditAvailedDate");
	            rowhead.createCell((short)cellno++).setCellValue("CreditReversalDate");
	            rowhead.createCell((short)cellno++).setCellValue("RefDocumentRemarks");
	            rowhead.createCell((short)cellno++).setCellValue("RefDocumentPeriodStartDate");
	            rowhead.createCell((short)cellno++).setCellValue("RefDocumentPeriodEndDate");
	            rowhead.createCell((short)cellno++).setCellValue("RefPrecedingDocumentDetails");
	            rowhead.createCell((short)cellno++).setCellValue("RefContractDetails");
	            rowhead.createCell((short)cellno++).setCellValue("AdditionalSupportingDocumentDetails");
	            rowhead.createCell((short)cellno++).setCellValue("BillNumber");
	            rowhead.createCell((short)cellno++).setCellValue("BillDate");
	            rowhead.createCell((short)cellno++).setCellValue("PortCode");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentCurrencyCode");
	            rowhead.createCell((short)cellno++).setCellValue("DestinationCountry");
	            rowhead.createCell((short)cellno++).setCellValue("ExportDuty");
	            rowhead.createCell((short)cellno++).setCellValue("Pos");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentValue");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentValueInForeignCurrency");
	            rowhead.createCell((short)cellno++).setCellValue("RoundOffAmount");
	            rowhead.createCell((short)cellno++).setCellValue("DifferentialPercentage");
	            rowhead.createCell((short)cellno++).setCellValue("ReverseCharge");
	            rowhead.createCell((short)cellno++).setCellValue("ClaimRefund");
	            rowhead.createCell((short)cellno++).setCellValue("UnderIgstAct");
	            rowhead.createCell((short)cellno++).setCellValue("RefundEligibility");
	            rowhead.createCell((short)cellno++).setCellValue("ECommerceGstin");
	            rowhead.createCell((short)cellno++).setCellValue("TDSGSTIN");
	            rowhead.createCell((short)cellno++).setCellValue("PnrOrUniqueNumber");
	            rowhead.createCell((short)cellno++).setCellValue("AvailProvisionalItc");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalGstin");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalDocumentType");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalAmountDeducted");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalPortCode");
	            rowhead.createCell((short)cellno++).setCellValue("TransportDateTime");
	            rowhead.createCell((short)cellno++).setCellValue("TransporterId");
	            rowhead.createCell((short)cellno++).setCellValue("TransporterName");
	            rowhead.createCell((short)cellno++).setCellValue("TransportMode");
	            rowhead.createCell((short)cellno++).setCellValue("Distance");
	            rowhead.createCell((short)cellno++).setCellValue("TransportDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("TransportDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("VehicleNumber");
	            rowhead.createCell((short)cellno++).setCellValue("VehicleType");
	            rowhead.createCell((short)cellno++).setCellValue("ToEmailAddresses");
	            rowhead.createCell((short)cellno++).setCellValue("ToMobileNumbers");
	            rowhead.createCell((short)cellno++).setCellValue("JWOriginalDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("JWOriginalDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("JWDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("JWDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("Custom1");
	            rowhead.createCell((short)cellno++).setCellValue("Custom2");
	            rowhead.createCell((short)cellno++).setCellValue("Custom3");
	            rowhead.createCell((short)cellno++).setCellValue("Custom4");
	            rowhead.createCell((short)cellno++).setCellValue("Custom5");
	            rowhead.createCell((short)cellno++).setCellValue("Custom6");
	            rowhead.createCell((short)cellno++).setCellValue("Custom7");
	            rowhead.createCell((short)cellno++).setCellValue("Custom8");
	            rowhead.createCell((short)cellno++).setCellValue("Custom9");
	            rowhead.createCell((short)cellno++).setCellValue("Custom10");
	            rowhead.createCell((short)cellno++).setCellValue("SerialNumber");
	            rowhead.createCell((short)cellno++).setCellValue("IsService");
	            rowhead.createCell((short)cellno++).setCellValue("Hsn");
	            rowhead.createCell((short)cellno++).setCellValue("ProductCode");
	            rowhead.createCell((short)cellno++).setCellValue("ItemName");
	            rowhead.createCell((short)cellno++).setCellValue("ItemDescription");
	            rowhead.createCell((short)cellno++).setCellValue("NatureOfJWDone");
	            rowhead.createCell((short)cellno++).setCellValue("Barcode");
	            rowhead.createCell((short)cellno++).setCellValue("Uqc");
	            rowhead.createCell((short)cellno++).setCellValue("Quantity");
	            rowhead.createCell((short)cellno++).setCellValue("FreeQuantity");
	            rowhead.createCell((short)cellno++).setCellValue("LossUnitOfMeasure");
	            rowhead.createCell((short)cellno++).setCellValue("LossTotalQuantity");
	            rowhead.createCell((short)cellno++).setCellValue("Rate");
	            rowhead.createCell((short)cellno++).setCellValue("CessRate");
	            rowhead.createCell((short)cellno++).setCellValue("StateCessRate");
	            rowhead.createCell((short)cellno++).setCellValue("CessNonAdvaloremRate");
	            rowhead.createCell((short)cellno++).setCellValue("PricePerQuantity");
	            rowhead.createCell((short)cellno++).setCellValue("DiscountAmount");
	            rowhead.createCell((short)cellno++).setCellValue("GrossAmount");
	            rowhead.createCell((short)cellno++).setCellValue("OtherCharges");
	            rowhead.createCell((short)cellno++).setCellValue("TaxableValue");
	            rowhead.createCell((short)cellno++).setCellValue("PreTaxValue");
	            rowhead.createCell((short)cellno++).setCellValue("IgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("SgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CessAmount");
	            rowhead.createCell((short)cellno++).setCellValue("StateCessAmount");
	            rowhead.createCell((short)cellno++).setCellValue("StateCessNonAdvaloremAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CessNonAdvaloremAmount");
	            rowhead.createCell((short)cellno++).setCellValue("OrderLineReference");
	            rowhead.createCell((short)cellno++).setCellValue("OriginCountry");
	            rowhead.createCell((short)cellno++).setCellValue("ItemTotal");
	            rowhead.createCell((short)cellno++).setCellValue("ItemAttributeDetails");
	            rowhead.createCell((short)cellno++).setCellValue("TaxType");
	            rowhead.createCell((short)cellno++).setCellValue("BatchNameNumber");
	            rowhead.createCell((short)cellno++).setCellValue("BatchExpiryDate");
	            rowhead.createCell((short)cellno++).setCellValue("WarrantyDate");
	            rowhead.createCell((short)cellno++).setCellValue("ItcEligibility");
	            rowhead.createCell((short)cellno++).setCellValue("ItcIgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("ItcCgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("ItcSgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("ItcCessAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem1");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem2");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem3");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem4");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem5");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem6");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem7");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem8");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem9");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem10");
	            /*if(Vb2c.contains("Y")){
	            	rowhead.createCell((short)cellno++).setCellValue("UpiId");
	            }*/
	            
	            String company_nm=utilBean.getCompanyName(dbcon, comp_cd);
	            
	            int k=0;
            	int serial_no=0;
            	
	            for(int i=0;i<invoice_no.length;i++)
	            {
	            	if(sys_or_ff_inv[i].equals("FF"))
	            	{
	            		queryString="SELECT BU_UNIT,ADDR_FLAG,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
		            			+ "NET_PAYABLE_AMT,ALLOC_QTY,GROSS_AMT_INR,TAX_AMT,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,"
		            			+ "(ALLOC_QTY) SUG_QTY,TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),NULL,NULL,SUB_INV_TYPE,NULL,NULL,NULL,TAX_STRUCT_CD "
		    					+ "FROM FMS_FFLOW_INV_MST "
		    					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND COUNTERPARTY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? ";
	            	}
	            	else if(sys_or_ff_inv[i].equals("DFF"))
	            	{
	            		queryString= "SELECT BU_UNIT,ADDR_FLAG,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
		            			+ "NET_PAYABLE_AMT,ALLOC_QTY,GROSS_AMT_INR,TAX_AMT,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,"
		            			+ "(ALLOC_QTY) SUG_QTY,TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),NULL,NULL,SUB_INV_TYPE,SAC_CD,NULL,NULL,TAX_STRUCT_CD "
		    					+ "FROM FMS_DLNG_FFLOW_INV_MST "
		    					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND COUNTERPARTY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? ";
	            	}
	            	else if(sys_or_ff_inv[i].equals("DSRV"))
	            	{
	            		queryString="SELECT BU_UNIT,PLANT_SEQ,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
		            			+ "NET_PAYABLE_AMT,QTY,GROSS_AMT,TAX_AMT,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INV_FLAG,"
		            			+ "(QTY) SUG_QTY,TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),SALE_PRICE,SALE_PRICE_UNIT,NULL,SAC_CD,QTY_UNIT,NULL,TAX_STRUCT_CD "
		    					+ "FROM FMS_DLNG_SVC_INVOICE_MST "
		    					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND COUNTERPARTY_CD=? AND INVOICE_NO=? ";
	            	}
	            	else
	            	{
	            		queryString="SELECT BU_UNIT,PLANT_SEQ,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
		            			+ "NET_PAYABLE_AMT,ALLOC_QTY,GROSS_AMT,TAX_AMT,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INV_FLAG,"
		            			+ "SUG_QTY,TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),SALE_PRICE,SALE_PRICE_UNIT,NULL,NULL,NULL,CRITERIA,TAX_STRUCT_CD "
		    					+ "FROM FMS_INVOICE_MST "
		    					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND COUNTERPARTY_CD=? AND INVOICE_NO=? ";
	            	}
	            	int st_count=0;
	    			stmt=dbcon.prepareStatement(queryString);
	    			stmt.setString(++st_count, comp_cd);
	    			stmt.setString(++st_count, contract_type[i]);
	    			stmt.setString(++st_count, counterparty_cd[i]);
	    			stmt.setString(++st_count, invoice_no[i]);
	    			if(sys_or_ff_inv[i].equals("FF") || sys_or_ff_inv[i].equals("DFF"))
	            	{
	    				stmt.setString(++st_count, inv_type[i]);	
	            	}
	    			rset=stmt.executeQuery();
	    			while(rset.next())
	    			{
	    				String countpty_nm=utilBean.getCounterpartyName(dbcon, counterparty_cd[i]);
	    				
	    				String bu_unit=rset.getString(1)==null?"":rset.getString(1);
	    				String plant_seq=rset.getString(2)==null?"":rset.getString(2);
	    				if(sys_or_ff_inv[i].equals("FF") || sys_or_ff_inv[i].equals("DFF"))
	    				{
		    				if(!plant_seq.equals("R") && !plant_seq.equals("B") && !plant_seq.equals("C"))
		    				{
		    					plant_seq=plant_seq.substring(1,plant_seq.length());
		    				}
	    				}
	    				
	    				String inv_dt=rset.getString(3)==null?"":rset.getString(3);
	    				String ReturnPeriod="";
	    				if(!inv_dt.equals(""))
	    				{
	    					String[] split = inv_dt.split("/");
	    					ReturnPeriod=split[1]+""+split[2];
	    				}
	    				
	    				String net_amt=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
	    				String qty=rset.getString(5)==null?"":nf.format(rset.getDouble(5));
	    				String gross_amt=rset.getString(6)==null?"":nf.format(rset.getDouble(6));
	    				String tax_amt=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
	    				
	    				String state_tin=rset.getString(8)==null?"":rset.getString(8);
	    				String inv_seq=rset.getString(9)==null?"":rset.getString(9);
	    				String fin_yr=rset.getString(10)==null?"":rset.getString(10);
	    				
	    				String sub_inv_type=rset.getString(16)==null?"":rset.getString(16);
	    				String sel_sac_cd=rset.getString(17)==null?"":rset.getString(17);
	    				String qty_unit=rset.getString(18)==null?"":rset.getString(18); //IT'S IS USED FOR DLNG SERVICE INVOICE(TMS) ONLY
	    				String criteria=rset.getString(19)==null?"":rset.getString(19); 
	    				String tax_struct_cd=rset.getString(20)==null?"":rset.getString(20); 
	    				
	    				String price="";
	    				if(!gross_amt.equals("") && !qty.equals(""))
	    				{
	    					if(Double.parseDouble(qty) > 0)
	    					{
	    						price=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(qty));
	    					}
	    					else
	    					{
	    						price=nf.format(0);
	    					}
	    				}
	    				
	    				String tax_rate="";
	    				if(!gross_amt.equals("") && !tax_amt.equals(""))
	    				{
	    					if(Double.parseDouble(tax_amt) > 0)
	    					{
	    						tax_rate=nf.format((Double.parseDouble(tax_amt)/Double.parseDouble(gross_amt))*100);
	    					}
	    					else
	    					{
	    						tax_rate=nf.format(0);
	    					}
	    				}
	    				
	    				String item_desc="";
	    				String inv_flag=rset.getString(11)==null?"":rset.getString(11);
	    				String period_end_dt=rset.getString(13)==null?"":rset.getString(13);
	    				
	    				String sac_no="999799";
	    				String sac_desc="Other Miscellaneous services - Other Services n.e.c.";
	    				if(inv_flag.equals("TLU"))
	    				{
	    					item_desc="TLU Charges";
	    					
	    					query1="SELECT SAC_CODE,SAC_DESC "
	    							+ "FROM FMS_SAC_MST "
	    							+ "WHERE SAC_CD=? ";
	    					stmt1=dbcon.prepareStatement(query1);
	    					stmt1.setString(1, sel_sac_cd);
	    					rset1=stmt1.executeQuery();
	    					if(rset1.next())
	    					{
	    						sac_no=rset1.getString(1)==null?"":rset1.getString(1);
			    				sac_desc=rset1.getString(2)==null?"":rset1.getString(2);
	    					}
	    					rset1.close();
	    					stmt1.close();
	    					
	    				}
	    				else if(sys_or_ff_inv[i].equals("DSRV"))
    	            	{						
	    					item_desc="Transport Management Services Charge";
	 
	    					query1="SELECT SAC_CODE,SAC_DESC "
	    							+ "FROM FMS_SAC_MST "
	    							+ "WHERE SAC_CD=? ";
	    					stmt1=dbcon.prepareStatement(query1);
	    					stmt1.setString(1, sel_sac_cd);
	    					rset1=stmt1.executeQuery();
	    					if(rset1.next())
	    					{
	    						sac_no=rset1.getString(1)==null?"":rset1.getString(1);
			    				sac_desc=rset1.getString(2)==null?"":rset1.getString(2);
	    					}
	    					rset1.close();
	    					stmt1.close();
    	            	}
	    				else if(inv_flag.equals("UG") || sub_inv_type.equals("UG"))
	    				{
	    					if(!gross_amt.equals("") && !tax_amt.equals(""))
		    				{
	    						net_amt=nf.format(Double.parseDouble(tax_amt)+Double.parseDouble(gross_amt));
		    				}
	    					
	    					qty=rset.getString(12)==null?"":nf.format(rset.getDouble(12));
	    					if(sys_or_ff_inv[i].equals("FF"))
	    	            	{
	    						price=nf3.format(0);
	    						if(!gross_amt.equals("") && !qty.equals(""))
			    				{
	    							if(Double.parseDouble(qty) > 0)
	    							{
	    								price=nf3.format(Double.parseDouble(gross_amt)/Double.parseDouble(qty));
	    							}
			    				}
	    	            	}
	    					else
	    					{
	    						price=rset.getString(14)==null?"":nf.format(rset.getDouble(14));
	    					}
	    					
	    					item_desc="SUG for LNG discharged"; //AS DISCUSSED WITH VIJAY ON 18-06-2025
	    				}
	    				else if ((inv_flag.equals("CR") || inv_flag.equals("DR")) && sys_or_ff_inv[i].equals("S")) 
	    				{
	    					qty=qty.equals("")?"":nf.format(Math.abs(Double.parseDouble(qty)));
	    					gross_amt=gross_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(gross_amt)));
	    					tax_amt=tax_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(tax_amt)));
	    					net_amt=net_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(net_amt)));
	    					
	    					if(criteria.contains("QTY"))
	    					{
	    						if(!gross_amt.equals("") && !qty.equals(""))
	    	    				{
	    	    					if(Double.parseDouble(qty) > 0)
	    	    					{
	    	    						price=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(qty));
	    	    					}
	    	    					else
	    	    					{
	    	    						price=nf.format(0);
	    	    					}
	    	    				}
	    					}
	    					else
	    					{
	    						if(criteria.contains("TAXP") && criteria.split("#").length == 1)
		    					{
		    						double taxFactor = TaxCalc.TaxFactor(dbcon, tax_struct_cd);
		    						
		    						if(!tax_amt.equals(""))
		    						{
		    							if(taxFactor > 0 && Double.parseDouble(tax_amt) > 0)
		    							{
		    								gross_amt=nf.format(Double.parseDouble(tax_amt)/(taxFactor/100));
		    							}
		    							else
		    							{
		    								gross_amt=nf.format(0);
		    							}
		    						}
		    						
		    						if(!gross_amt.equals("") && !tax_amt.equals(""))
		    						{
		    							net_amt=nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(tax_amt));
		    						}
		    					}
	    						
	    						query1="SELECT A.ALLOC_QTY "
	    								+ "FROM FMS_INV_CRDR_REF A, FMS_INVOICE_MST B "
	    								+ "WHERE A.COMPANY_CD=? AND B.INVOICE_NO=? "
	    								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR "
	    								+ "AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ ";
	    						stmt1=dbcon.prepareStatement(query1);
		    					stmt1.setString(1, comp_cd);
		    					stmt1.setString(2, invoice_no[i]);
		    					rset1=stmt1.executeQuery();
		    					if(rset1.next())
		    					{
		    						qty=rset1.getString(1)==null?"":nf.format(rset1.getDouble(1));
		    					}
		    					rset1.close();
		    					stmt1.close();
		    					
		    					if(!gross_amt.equals("") && !qty.equals(""))
			    				{
			    					if(Double.parseDouble(qty) > 0 && Double.parseDouble(gross_amt) > 0)
			    					{
			    						price=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(qty));
			    					}
			    					else
			    					{
			    						price=nf.format(0);
			    					}
			    				}
	    					}
	    					
	    					if(!gross_amt.equals("") && !tax_amt.equals(""))
    	    				{
    	    					if(Double.parseDouble(tax_amt) > 0)
    	    					{
    	    						tax_rate=nf.format((Double.parseDouble(tax_amt)/Double.parseDouble(gross_amt))*100);
    	    					}
    	    					else
    	    					{
    	    						tax_rate=nf.format(0);
    	    					}
    	    				}
	    					
	    					item_desc="Natural Gas (Regasified)";
						}
	    				else if(inv_flag.equals("ST") )
	    				{
	    					if(sys_or_ff_inv[i].equals("FF"))
	    	            	{
	    						
	    	            	}
	    					else
	    					{
		    					query1="SELECT SUM(OPEN_BALANCE_QTY) "
		    							+ "FROM FMS_INV_STORAGE_CRG_DTL "
		    							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		    							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND DAY_DISCOUNT=? ";
		    					stmt1=dbcon.prepareStatement(query1);
		    					stmt1.setString(1, comp_cd);
		    					stmt1.setString(2, state_tin);
		    					stmt1.setString(3, fin_yr);
		    					stmt1.setString(4, inv_seq);
		    					stmt1.setString(5, "N");
		    					rset1=stmt1.executeQuery();
		    					if(rset1.next())
		    					{
		    						qty=rset1.getString(1)==null?"":nf.format(rset1.getDouble(1));
		    					}
		    					rset1.close();
		    					stmt1.close();
	    					}
	    					
	    					if(!gross_amt.equals("") && !qty.equals(""))
		    				{
		    					price=nf3.format(Double.parseDouble(gross_amt)/Double.parseDouble(qty));
		    				}
	    					
	    					item_desc="Storage Charges For The Extended Storage Duration";
	    				}
	    				else if(inv_flag.equals("DI") || sub_inv_type.equals("DI"))
	    				{
	    					item_desc="Deficiency Payment";
	    					sac_no="999794";
	    					sac_desc="Agreeing to tolerate an act";
	    				}
	    				else
	    				{
	    					if(inv_flag.equals("LP"))
		    				{
	    						item_desc="Late Payment";
	    						price=gross_amt; //AS PER VIJAY FEEDBACK ON EMAIL ON 20251205
	    						qty="1"; //AS PER VIJAY FEEDBACK ON EMAIL ON 20251205
		    				}
	    					else
	    					{
	    						item_desc="Natural Gas (Regasified)";
	    					}
	    				}
	    				
	    				String igst_amt="";
	    				String cgst_amt="";
	    				String sgst_amt="";
	    				if(sys_or_ff_inv[i].equals("FF"))
	    				{
	    					query1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_FFLOW_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INVOICE_TYPE=? ";
	    				}
	    				else if(sys_or_ff_inv[i].equals("DFF"))
	    				{
	    					query1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_DLNG_FFLOW_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INVOICE_TYPE=? ";
	    				}
	    				else if(sys_or_ff_inv[i].equals("DSRV"))
	    				{
	    					query1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_DLNG_SVC_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
	    				}
	    				else
	    				{
		    				query1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
	    				}
	    				st_count=0;
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++st_count, comp_cd);
						stmt1.setString(++st_count, state_tin);
						stmt1.setString(++st_count, fin_yr);
						stmt1.setString(++st_count, inv_seq);
						if(sys_or_ff_inv[i].equals("FF") || sys_or_ff_inv[i].equals("DFF"))
		            	{
		    				stmt1.setString(++st_count, inv_type[i]);
		            	}
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							String tax_descr=rset1.getString(2)==null?"":rset1.getString(2);
							if(tax_descr.toUpperCase().startsWith("IGST"))
							{
								igst_amt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
								
								igst_amt=igst_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(igst_amt)));
							}
							else if(tax_descr.toUpperCase().startsWith("CGST"))
							{
								cgst_amt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
								
								cgst_amt=cgst_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(cgst_amt)));
							}
							else if(tax_descr.toUpperCase().startsWith("SGST"))
							{
								sgst_amt=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
								
								sgst_amt=sgst_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(sgst_amt)));
							}
						}
						rset1.close();
						stmt1.close();
	    				
	    				String plant_gstin_no="";
	    				String bu_gstin_no="";
	    				//for counterparty plant
	    				query1 = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
	    						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
	    						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
	    						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD='1003' " //GSTIN Number cd
	    						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
	    				stmt1 = dbcon.prepareStatement(query1);
	    				stmt1.setString(1, counterparty_cd[i]);
	    				stmt1.setString(2, "C");
	    				stmt1.setString(3, plant_seq);
	    				stmt1.setString(4, comp_cd);
	    				rset1 = stmt1.executeQuery();
	    				if(rset1.next())
	    				{
	    					String no = rset1.getString(1)==null?"":rset1.getString(1);
	    					String nm = rset1.getString(3)==null?"":rset1.getString(3);
	    					
	    					plant_gstin_no=no;
	    				}
	    				rset1.close();
	    				stmt1.close();
	    				
	    				//for bu unit
	    				query1 = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
	    						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
	    						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
	    						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD='1003' " //GSTIN Number cd
	    						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
	    				stmt1 = dbcon.prepareStatement(query1);
	    				stmt1.setString(1, comp_cd);
	    				stmt1.setString(2, "B");
	    				stmt1.setString(3, bu_unit);
	    				stmt1.setString(4, comp_cd);
	    				rset1 = stmt1.executeQuery();
	    				if(rset1.next())
	    				{
	    					String no = rset1.getString(1)==null?"":rset1.getString(1);
	    					String nm = rset1.getString(3)==null?"":rset1.getString(3);
	    					
	    					bu_gstin_no=no;
	    				}
	    				rset1.close();
	    				stmt1.close();
	    				
	    				String TransactionType=plant_gstin_no.equals("")?"B2C":"B2B";
	    				
	    				HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(dbcon,comp_cd, "B", comp_cd, bu_unit);
	    				String bu_plantAddress=""+bu_plant_detail.get("plant_address");
	    				String bu_plantCity=""+bu_plant_detail.get("plant_city");
	    				String bu_plantState=""+bu_plant_detail.get("plant_state");
	    				String bu_plantPin=""+bu_plant_detail.get("plant_pin");
	    				String bu_plantNm=""+bu_plant_detail.get("plant_name");
	    				
	    				HashMap plant_detail=utilBean.getCounterpartyPlantDetail(dbcon,comp_cd, "C", counterparty_cd[i], plant_seq);
	    				String plantAddress=""+plant_detail.get("plant_address");
	    				String plantCity=""+plant_detail.get("plant_city");
	    				String plantState=""+plant_detail.get("plant_state");
	    				String plantPin=""+plant_detail.get("plant_pin");
	    				
	    				String bu_statCode=utilBean.getState_TIN(dbcon, comp_cd, comp_cd, "B", bu_unit);
	    				String plant_statCode=utilBean.getState_TIN(dbcon, comp_cd, counterparty_cd[i], "C", plant_seq);
	    				
	    				String documentType="INV";
	    				if(inv_flag.equals("CR"))
	    				{
	    					documentType="CRN";
	    				}
	    				else if(inv_flag.equals("DR") || inv_flag.equals("LP"))
	    				{
	    					documentType="DBN";
	    				}
	    				
	    				k=k+1;
	    				//serial_no=serial_no+1;
	    				serial_no=1;
			            int row_no=k;
			            Row row = sheet.createRow((short)row_no);
			            int cellno1=0;
			            row.createCell((short)cellno1++).setCellValue(""+bu_gstin_no);//LocationGstin
			            row.createCell((short)cellno1++).setCellValue(""+company_nm);//LocationName
			            row.createCell((short)cellno1++).setCellValue(""+ReturnPeriod);//ReturnPeriod
			            row.createCell((short)cellno1++).setCellValue("");//LiabilityDischargeReturnPeriod
			            row.createCell((short)cellno1++).setCellValue("");//ITCClaimReturnPeriod
			            row.createCell((short)cellno1++).setCellValue("EINV");//Purpose
			            row.createCell((short)cellno1++).setCellValue("");//AutoPushOrGenerate
			            row.createCell((short)cellno1++).setCellValue("S");//SupplyType //supply type S for sales and P for purchase 
			            row.createCell((short)cellno1++).setCellValue("");//Irn
			            row.createCell((short)cellno1++).setCellValue(documentType);//DocumentType
			            row.createCell((short)cellno1++).setCellValue(""+TransactionType);//TransactionType
			            row.createCell((short)cellno1++).setCellValue("");//TransactionNature
			            row.createCell((short)cellno1++).setCellValue("");//TransactionTypeDescription
			            row.createCell((short)cellno1++).setCellValue("");//TaxpayerType
			            row.createCell((short)cellno1++).setCellValue(""+invoice_no[i]);//DocumentNumber
			            row.createCell((short)cellno1++).setCellValue("");//DocumentSeriesCode
			            row.createCell((short)cellno1++).setCellValue(""+inv_dt);//DocumentDate
			            row.createCell((short)cellno1++).setCellValue(""+bu_gstin_no);//BillFromGstin
			            row.createCell((short)cellno1++).setCellValue(""+company_nm);//BillFromLegalName
			            row.createCell((short)cellno1++).setCellValue("");//BillFromTradeName
			            row.createCell((short)cellno1++).setCellValue("");//BillFromVendorCode
			            row.createCell((short)cellno1++).setCellValue(""+bu_plantAddress);//BillFromAddress1
			            row.createCell((short)cellno1++).setCellValue("");//BillFromAddress2
			            row.createCell((short)cellno1++).setCellValue(""+bu_plantCity);//BillFromCity
			            row.createCell((short)cellno1++).setCellValue(""+bu_statCode);//BillFromStateCode
			            row.createCell((short)cellno1++).setCellValue(""+bu_plantPin);//BillFromPincode
			            row.createCell((short)cellno1++).setCellValue("");//BillFromPhone
			            row.createCell((short)cellno1++).setCellValue("");//BillFromEmail
			            row.createCell((short)cellno1++).setCellValue("");//DispatchFromGstin
			            row.createCell((short)cellno1++).setCellValue("");//DispatchFromTradeName
			            row.createCell((short)cellno1++).setCellValue("");//DispatchFromVendorCode
			            row.createCell((short)cellno1++).setCellValue("");//DispatchFromAddress1
			            row.createCell((short)cellno1++).setCellValue("");//DispatchFromAddress2
			            row.createCell((short)cellno1++).setCellValue("");//DispatchFromCity
			            row.createCell((short)cellno1++).setCellValue("");//DispatchFromStateCode
			            row.createCell((short)cellno1++).setCellValue("");//DispatchFromPincode
			            row.createCell((short)cellno1++).setCellValue(""+plant_gstin_no);//BillToGstin
			            row.createCell((short)cellno1++).setCellValue(""+countpty_nm);//BillToLegalName
			            row.createCell((short)cellno1++).setCellValue("");//BillToTradeName
			            row.createCell((short)cellno1++).setCellValue("");//BillToVendorCode
			            row.createCell((short)cellno1++).setCellValue(""+plantAddress);//BillToAddress1
			            row.createCell((short)cellno1++).setCellValue("");//BillToAddress2
			            row.createCell((short)cellno1++).setCellValue(""+plantCity);//BillToCity
			            row.createCell((short)cellno1++).setCellValue(""+plant_statCode);//BillToStateCode
			            row.createCell((short)cellno1++).setCellValue(""+plantPin);//BillToPincode
			            row.createCell((short)cellno1++).setCellValue("");//BillToPhone
			            row.createCell((short)cellno1++).setCellValue("");//BillToEmail
			            row.createCell((short)cellno1++).setCellValue("");//ShipToGstin
			            row.createCell((short)cellno1++).setCellValue("");//ShipToLegalName
			            row.createCell((short)cellno1++).setCellValue("");//ShipToTradeName
			            row.createCell((short)cellno1++).setCellValue("");//ShipToVendorCode
			            row.createCell((short)cellno1++).setCellValue("");//ShipToAddress1
			            row.createCell((short)cellno1++).setCellValue("");//ShipToAddress2
			            row.createCell((short)cellno1++).setCellValue("");//ShipToCity
			            row.createCell((short)cellno1++).setCellValue("");//ShipToStateCode
			            row.createCell((short)cellno1++).setCellValue("");//ShipToPincode
			            row.createCell((short)cellno1++).setCellValue("");//PaymentType
			            row.createCell((short)cellno1++).setCellValue("");//PaymentMode
			            row.createCell((short)cellno1++).setCellValue("");//PaymentAmount
			            row.createCell((short)cellno1++).setCellValue("");//AdvancePaidAmount
			            row.createCell((short)cellno1++).setCellValue("");//PaymentDate
			            row.createCell((short)cellno1++).setCellValue("");//PaymentRemarks
			            row.createCell((short)cellno1++).setCellValue("");//PaymentTerms
			            row.createCell((short)cellno1++).setCellValue("");//PaymentInstruction
			            row.createCell((short)cellno1++).setCellValue("");//PayeeName
			            row.createCell((short)cellno1++).setCellValue("");//PayeeAccountNumber
			            row.createCell((short)cellno1++).setCellValue("");//PaymentAmountDue
			            row.createCell((short)cellno1++).setCellValue("");//Ifsc
			            row.createCell((short)cellno1++).setCellValue("");//CreditTransfer
			            row.createCell((short)cellno1++).setCellValue("");//DirectDebit
			            row.createCell((short)cellno1++).setCellValue("");//CreditDays
			            row.createCell((short)cellno1++).setCellValue("");//CreditAvailedDate
			            row.createCell((short)cellno1++).setCellValue("");//CreditReversalDate
			            row.createCell((short)cellno1++).setCellValue("");//RefDocumentRemarks
			            row.createCell((short)cellno1++).setCellValue("");//RefDocumentPeriodStartDate
			            row.createCell((short)cellno1++).setCellValue("");//RefDocumentPeriodEndDate
			            row.createCell((short)cellno1++).setCellValue("");//RefPrecedingDocumentDetails
			            row.createCell((short)cellno1++).setCellValue("");//RefContractDetails
			            row.createCell((short)cellno1++).setCellValue("");//AdditionalSupportingDocumentDetails
			            row.createCell((short)cellno1++).setCellValue("");//BillNumber
			            row.createCell((short)cellno1++).setCellValue("");//BillDate
			            row.createCell((short)cellno1++).setCellValue("");//PortCode
			            row.createCell((short)cellno1++).setCellValue("");//DocumentCurrencyCode
			            row.createCell((short)cellno1++).setCellValue("");//DestinationCountry
			            row.createCell((short)cellno1++).setCellValue("");//ExportDuty
			            //row.createCell((short)cellno1++).setCellValue(""+bu_statCode);//Pos - place of supply
			            row.createCell((short)cellno1++).setCellValue(""+plant_statCode);//Pos - place of supply //WILL BE CUSTOMER PLANT STATE AS DISCUSSED BY JAYASRI MAM WITH VIJAY ON 20250725
			            row.createCell((short)cellno1++).setCellValue(""+net_amt);//DocumentValue //net amount in INR
			            row.createCell((short)cellno1++).setCellValue("");//DocumentValueInForeignCurrency
			            row.createCell((short)cellno1++).setCellValue("");//RoundOffAmount
			            row.createCell((short)cellno1++).setCellValue("");//DifferentialPercentage
			            row.createCell((short)cellno1++).setCellValue("N");//ReverseCharge
			            row.createCell((short)cellno1++).setCellValue("N");//ClaimRefund
			            row.createCell((short)cellno1++).setCellValue("N");//UnderIgstAct
			            row.createCell((short)cellno1++).setCellValue("N");//RefundEligibility
			            row.createCell((short)cellno1++).setCellValue("");//ECommerceGstin
			            row.createCell((short)cellno1++).setCellValue("");//TDSGSTIN
			            row.createCell((short)cellno1++).setCellValue("");//PnrOrUniqueNumber
			            row.createCell((short)cellno1++).setCellValue("");//AvailProvisionalItc
			            row.createCell((short)cellno1++).setCellValue("");//OriginalGstin
			            row.createCell((short)cellno1++).setCellValue("");//OriginalStateCode
			            row.createCell((short)cellno1++).setCellValue("");//OriginalTradeName
			            row.createCell((short)cellno1++).setCellValue("");//OriginalDocumentType
			            row.createCell((short)cellno1++).setCellValue("");//OriginalDocumentNumber
			            row.createCell((short)cellno1++).setCellValue("");//OriginalDocumentDate
			            row.createCell((short)cellno1++).setCellValue("");//OriginalReturnPeriod
			            row.createCell((short)cellno1++).setCellValue("");//OriginalAmountDeducted
			            row.createCell((short)cellno1++).setCellValue("");//OriginalPortCode
			            row.createCell((short)cellno1++).setCellValue("");//TransportDateTime
			            row.createCell((short)cellno1++).setCellValue("");//TransporterId
			            row.createCell((short)cellno1++).setCellValue("");//TransporterName
			            row.createCell((short)cellno1++).setCellValue("");//TransportMode
			            row.createCell((short)cellno1++).setCellValue("");//Distance
			            row.createCell((short)cellno1++).setCellValue("");//TransportDocumentNumber
			            row.createCell((short)cellno1++).setCellValue("");//TransportDocumentDate
			            row.createCell((short)cellno1++).setCellValue("");//VehicleNumber
			            row.createCell((short)cellno1++).setCellValue("");//VehicleType
			            row.createCell((short)cellno1++).setCellValue("");//ToEmailAddresses
			            row.createCell((short)cellno1++).setCellValue("");//ToMobileNumbers
			            row.createCell((short)cellno1++).setCellValue("");//JWOriginalDocumentNumber
			            row.createCell((short)cellno1++).setCellValue("");//JWOriginalDocumentDate
			            row.createCell((short)cellno1++).setCellValue("");//JWDocumentNumber
			            row.createCell((short)cellno1++).setCellValue("");//JWDocumentDate
			            row.createCell((short)cellno1++).setCellValue("");//Custom1
			            row.createCell((short)cellno1++).setCellValue("");//Custom2
			            row.createCell((short)cellno1++).setCellValue("");//Custom3
			            row.createCell((short)cellno1++).setCellValue("");//Custom4
			            row.createCell((short)cellno1++).setCellValue("");//Custom5
			            row.createCell((short)cellno1++).setCellValue("");//Custom6
			            row.createCell((short)cellno1++).setCellValue("");//Custom7
			            row.createCell((short)cellno1++).setCellValue("");//Custom8
			            row.createCell((short)cellno1++).setCellValue("");//Custom9
			            row.createCell((short)cellno1++).setCellValue("");//Custom10
			            row.createCell((short)cellno1++).setCellValue(""+serial_no);//SerialNumber
			            row.createCell((short)cellno1++).setCellValue("Y");//IsService //IF service inv then Y else N
			            row.createCell((short)cellno1++).setCellValue(sac_no);//Hsn //LTCORA SAC NO
			            row.createCell((short)cellno1++).setCellValue("");//ProductCode
			            row.createCell((short)cellno1++).setCellValue(sac_desc);//ItemName
			            row.createCell((short)cellno1++).setCellValue(item_desc);//ItemDescription
			            row.createCell((short)cellno1++).setCellValue("");//NatureOfJWDone
			            row.createCell((short)cellno1++).setCellValue("");//Barcode
			            if(sys_or_ff_inv[i].equals("DSRV") && qty_unit.equals("0")) //IT'S IS USED FOR DLNG SERVICE INVOICE(TMS) ONLY
    	            	{
			            	row.createCell((short)cellno1++).setCellValue("NOS");//Uqc
    	            	}
			            else
			            {
			            	row.createCell((short)cellno1++).setCellValue("OTH");//Uqc
			            }
			            row.createCell((short)cellno1++).setCellValue(""+qty);//Quantity
			            row.createCell((short)cellno1++).setCellValue("");//FreeQuantity
			            row.createCell((short)cellno1++).setCellValue("");//LossUnitOfMeasure
			            row.createCell((short)cellno1++).setCellValue("");//LossTotalQuantity
			            row.createCell((short)cellno1++).setCellValue(""+tax_rate);//Rate //TAX RATE
			            row.createCell((short)cellno1++).setCellValue("");//CessRate
			            row.createCell((short)cellno1++).setCellValue("");//StateCessRate
			            row.createCell((short)cellno1++).setCellValue("");//CessNonAdvaloremRate
			            row.createCell((short)cellno1++).setCellValue(""+price);//PricePerQuantity
			            row.createCell((short)cellno1++).setCellValue("");//DiscountAmount
			            row.createCell((short)cellno1++).setCellValue(""+gross_amt);//GrossAmount
			            row.createCell((short)cellno1++).setCellValue("");//OtherCharges
			            row.createCell((short)cellno1++).setCellValue(""+gross_amt);//TaxableValue
			            row.createCell((short)cellno1++).setCellValue("");//PreTaxValue
			            row.createCell((short)cellno1++).setCellValue(""+igst_amt);//IgstAmount
			            row.createCell((short)cellno1++).setCellValue(""+cgst_amt);//CgstAmount
			            row.createCell((short)cellno1++).setCellValue(""+sgst_amt);//SgstAmount
			            row.createCell((short)cellno1++).setCellValue("");//CessAmount
			            row.createCell((short)cellno1++).setCellValue("");//StateCessAmount
			            row.createCell((short)cellno1++).setCellValue("");//StateCessNonAdvaloremAmount
			            row.createCell((short)cellno1++).setCellValue("");//CessNonAdvaloremAmount
			            row.createCell((short)cellno1++).setCellValue("");//OrderLineReference
			            row.createCell((short)cellno1++).setCellValue("");//OriginCountry
			            row.createCell((short)cellno1++).setCellValue(""+net_amt);//ItemTotal //net amt
			            row.createCell((short)cellno1++).setCellValue("");//ItemAttributeDetails
			            row.createCell((short)cellno1++).setCellValue("");//TaxType
			            row.createCell((short)cellno1++).setCellValue("");//BatchNameNumber
			            row.createCell((short)cellno1++).setCellValue("");//BatchExpiryDate
			            row.createCell((short)cellno1++).setCellValue("");//WarrantyDate
			            row.createCell((short)cellno1++).setCellValue("");//ItcEligibility
			            row.createCell((short)cellno1++).setCellValue("");//ItcIgstAmount
			            row.createCell((short)cellno1++).setCellValue("");//ItcCgstAmount
			            row.createCell((short)cellno1++).setCellValue("");//ItcSgstAmount
			            row.createCell((short)cellno1++).setCellValue("");//ItcCessAmount
			            row.createCell((short)cellno1++).setCellValue("");//CustomItem1
			            row.createCell((short)cellno1++).setCellValue("");//CustomItem2
			            row.createCell((short)cellno1++).setCellValue("");//CustomItem3
			            row.createCell((short)cellno1++).setCellValue("");//CustomItem4
			            row.createCell((short)cellno1++).setCellValue("");//CustomItem5
			            row.createCell((short)cellno1++).setCellValue("");//CustomItem6
			            row.createCell((short)cellno1++).setCellValue("");//CustomItem7
			            row.createCell((short)cellno1++).setCellValue("");//CustomItem8
			            row.createCell((short)cellno1++).setCellValue("");//CustomItem9
			            row.createCell((short)cellno1++).setCellValue("");//CustomItem10
			            
			            /*row.createCell((short)cellno1++).setCellValue("");//supplier gstin no
			            row.createCell((short)cellno1++).setCellValue("");//supplier nm
			            row.createCell((short)cellno1++).setCellValue("");//month and year
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("EINV");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("S");//supply type S for sales and p for purchase
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("B2B");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");//invoice num	
			            row.createCell((short)cellno1++).setCellValue("");//invoice num
			            row.createCell((short)cellno1++).setCellValue("");//invoice date
			            row.createCell((short)cellno1++).setCellValue("");//supplier gstin no
			            row.createCell((short)cellno1++).setCellValue("");//supplier nm
			            row.createCell((short)cellno1++).setCellValue("");//supplier nm
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");//supplier city
			            row.createCell((short)cellno1++).setCellValue(""); //supplier state code
			            row.createCell((short)cellno1++).setCellValue("");//supplier pin code
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue(""); //cust_gstin_no
			            row.createCell((short)cellno1++).setCellValue(""); //cust_nm
			            row.createCell((short)cellno1++).setCellValue(""); //cust_nm
			            row.createCell((short)cellno1++).setCellValue(""); //cust_nm
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");//cust city
			            row.createCell((short)cellno1++).setCellValue("");//cust state code
			            row.createCell((short)cellno1++).setCellValue("");//cust pin code
			            row.createCell((short)cellno1++).setCellValue("");//cust phone num
			            row.createCell((short)cellno1++).setCellValue("");//cust email
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			         	row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");//POS //Enabled by MCL 20210415
			            row.createCell((short)cellno1++).setCellValue(""); //Net amount inr
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("N");
			            row.createCell((short)cellno1++).setCellValue("N");
			            row.createCell((short)cellno1++).setCellValue("N");
			            row.createCell((short)cellno1++).setCellValue("N");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");//cust email address 
			            row.createCell((short)cellno1++).setCellValue("");//cust phone num
			            row.createCell((short)cellno1++).setCellValue("");//cust phone num
			            row.createCell((short)cellno1++).setCellValue("");//cust phone num
			            row.createCell((short)cellno1++).setCellValue("");//cust phone num
			            row.createCell((short)cellno1++).setCellValue("");//cust phone num
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("Y");
			            row.createCell((short)cellno1++).setCellValue("");//sac_no
			            row.createCell((short)cellno1++).setCellValue("");//sac_no
			            row.createCell((short)cellno1++).setCellValue("");//sac_description
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("OTH");
			            row.createCell((short)cellno1++).setCellValue("");//Inv quanityt
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");//tax amount inr not rounded off
			            row.createCell((short)cellno1++).setCellValue("");//tax amount inr not rounded off
			            row.createCell((short)cellno1++).setCellValue("");//tax comp
			            row.createCell((short)cellno1++).setCellValue("");
				        row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");//net amount
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");*/
			            /*if(Vb2c.elementAt(i).equals("Y")){
			            	if(supp_cd.elementAt(i).equals("2")){
			            		row.createCell((short)cellno1++).setCellValue("haziraport@sc");
			            	}else{
			            		row.createCell((short)cellno1++).setCellValue("seipl@sc");
			            	}
			            	
			            }*/
	    			}
	    			rset.close();
	    			stmt.close();
	            }
	            
	            String realPath = request.getServletContext().getRealPath("");
	            String canonicalPath_files = new File(file_path).getCanonicalPath();
	            
	            //System.out.println("realPath = "+realPath);
	            //System.out.println("canonicalPath_files = "+canonicalPath_files);
		        if(!canonicalPath_files.startsWith(realPath))
		        {
		        	throw new IOException("Entry is outside of the target directory!");
		        }
		        else
		        {
		            try (FileOutputStream fileOut = new FileOutputStream(file_path))
		            {
		            	workbook.write(fileOut);
		            }
		        }
	            
	            if(k>0)
	            {
	            	msg = "Successful! - "+fileName+" Created for IRN Generation!";
					msg_type="S";
	            }
	            else
	            {
	            	msg = "Failed! - No Invoice found for IRN Generation!";
					msg_type="E";
	            }
			}
			else
			{
				msg = "Failed! - No Invoice found for IRN Generation!";
				msg_type="E";
			}
			
			url = "../extn_interface/frm_generate_irn.jsp?month="+month+"&year="+year+"&invoice_type="+invoice_type+
					"&msg="+msg+"&msg_type="+msg_type+"&file_nm="+fileName+commonUrl_pra;
			
		}
		catch(Exception e)
		{
			//dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! - IRN Generation Failed";
		}
		finally 
		{
			if(workbook != null)
			{
				try 
				{
					workbook.close();
					workbook=null;
				}
				catch(IOException  e)
				{
					new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				}
			}
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void UploadResponseIRNExcel(HttpServletRequest request) throws SQLException 
	{
		String function_nm="UploadResponseIRNExcel()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
			String file_name="";
	        String fileName="";
	        
	        int success=0;
	        int failed=0;
	        
	        for (Part part : request.getParts()) 
	        {
	        	fileName = extractFileName(part);
	        	// refines the fileName in case it is an absolute path
			    fileName = new File(fileName).getName();
			    if(!fileName.equals("") && fileName.endsWith(".xlsx") )
			    {
			    	file_name=fileName;
			    	
			    	String work_data=utilBean.getAutomationKeyDetail(dbcon, "WORK_DATA_"+comp_cd);
			    	String file_path=work_data+File.separator+"IRN_EXCEL"+File.separator+"Response";
					
					if(!new File(file_path).exists())
					{
						new File(file_path).mkdirs();
					}
					
					file_path=file_path+File.separator+fileName;
					
					part.write(file_path);
					try(FileInputStream file = new FileInputStream(new File(file_path));
						     XSSFWorkbook workbook = new XSSFWorkbook(file))
					{
						XSSFSheet sheet = workbook.getSheetAt(0);
						Iterator<Row> rowIterator = sheet.iterator();
						while (rowIterator.hasNext()) 
						{
							Row row = rowIterator.next();
							//For each row, iterate through all the columns
							Iterator<Cell> cellIterator = row.cellIterator();
							int row_num=row.getRowNum();
							String inv_no="",irn_no="",SignedQRCode="",gstin_no="",QRCode="";
							if(row_num>0)
							{
								while (cellIterator.hasNext()) 
								{
									Cell cell = cellIterator.next();
									int rw_ind=cell.getRowIndex();
									
									if(cell.getColumnIndex()==2 || cell.getColumnIndex()==7 || cell.getColumnIndex()==11 || cell.getColumnIndex()==12 || cell.getColumnIndex()==0 )
									{
										if(cell.getColumnIndex()==2)
										{
											inv_no=cell.getStringCellValue();
										} 
										else if(cell.getColumnIndex()==7)
										{
											irn_no=cell.getStringCellValue();
										}
										else if(cell.getColumnIndex()==11)
										{
											SignedQRCode=cell.getStringCellValue();
										}
										else if(cell.getColumnIndex()==0)
										{
											gstin_no=cell.getStringCellValue();
										}
										else if(cell.getColumnIndex()==12)
										{
											QRCode=cell.getStringCellValue();
										}
									}			
								}
								if(!irn_no.equals("") && (!inv_no.equals("")) && (!SignedQRCode.equals("")))
								{
									queryString="SELECT COMPANY_CD,BU_UNIT "
					    					+ "FROM FMS_INVOICE_MST "
					    					+ "WHERE INVOICE_NO=? "
					    					+ "UNION ALL " //AS DISCUSSED WITH JAYASRI MAM, STORE FFLOW IRN DETAIL IN A SAME TABLE 20250710
					    					+ "SELECT COMPANY_CD,BU_UNIT "
					    					+ "FROM FMS_FFLOW_INV_MST "
					    					+ "WHERE INVOICE_NO=? "
					    					+ "UNION ALL " 
					    					+ "SELECT COMPANY_CD,BU_UNIT "
					    					+ "FROM FMS_DLNG_FFLOW_INV_MST "
					    					+ "WHERE INVOICE_NO=? "
					    					+ "UNION ALL " 
					    					+ "SELECT COMPANY_CD,BU_UNIT "
					    					+ "FROM FMS_DLNG_SVC_INVOICE_MST "
					    					+ "WHERE INVOICE_NO=? ";
					    			stmt=dbcon.prepareStatement(queryString);
					    			stmt.setString(1, inv_no);
					    			stmt.setString(2, inv_no);
					    			stmt.setString(3, inv_no);
					    			stmt.setString(4, inv_no);
					    			rset=stmt.executeQuery();
					    			if(rset.next())
					    			{
					    				String companyCd = rset.getString(1)==null?"":rset.getString(1);
					    				String bu_unit = rset.getString(2)==null?"":rset.getString(2);
					    				
					    				//for bu unit
					    				String bu_gstin_no="";
					    				query1 = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
					    						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
					    						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
					    						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD='1003' " //GSTIN Number cd
					    						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
					    				stmt1 = dbcon.prepareStatement(query1);
					    				stmt1.setString(1, comp_cd);
					    				stmt1.setString(2, "B");
					    				stmt1.setString(3, bu_unit);
					    				stmt1.setString(4, comp_cd);
					    				rset1 = stmt1.executeQuery();
					    				if(rset1.next())
					    				{
					    					String no = rset1.getString(1)==null?"":rset1.getString(1);
					    					String nm = rset1.getString(3)==null?"":rset1.getString(3);
					    					
					    					bu_gstin_no=no;
					    				}
					    				rset1.close();
					    				stmt1.close();
					    				
					    				if(!companyCd.equals("") && bu_gstin_no.equals(gstin_no))
					    				{
					    					int count_irn=0;
					    					query1="SELECT COUNT(*) "
					    							+ "FROM FMS_INVOICE_IRN_DTL "
					    							+ "WHERE COMPANY_CD=? AND INVOICE_NO=?";
											stmt1 = dbcon.prepareStatement(query1);
					           	    		stmt1.setString(1, companyCd); 
					           	    		stmt1.setString(2, inv_no); 
											rset1=stmt1.executeQuery();
											if(rset1.next())
											{
												count_irn=rset1.getInt(1);
											}
											rset1.close();
											stmt1.close();
											
											if(count_irn==0)
											{
												query1="INSERT INTO FMS_INVOICE_IRN_DTL (COMPANY_CD,INVOICE_NO,IRN_NO,XLS_FILE_NM,SIGN_QR_CODE,ENT_BY,ENT_DT)"
														+ " VALUES (?,?,?,?,?,?,SYSDATE)";
												stmt1 = dbcon.prepareStatement(query1);
						           	    		stmt1.setString(1, companyCd); 
						           	    		stmt1.setString(2, inv_no); 
						           	    		stmt1.setString(3, irn_no);
						           	    		stmt1.setString(4, fileName); 
						           	    		stmt1.setString(5, SignedQRCode); 
						           	    		stmt1.setString(6, emp_cd); 
												stmt1.executeUpdate();
												stmt1.close();
												dbcon.commit();
												
												success++;
											}
											else
											{
												failed++;
											}
					    				}
					    				else
					    				{
					    					failed++;
					    				}
					    			}
					    			rset.close();
					    			stmt.close();
								}
								else
								{
									failed++;
								}
							}
						}
					}
			    }
	        }
	        
	        if(!file_name.equals(""))
	        {
	        	msg = "Successful! - "+file_name+" IRN Response Imported Successfully ("+success+" Success and "+failed+" Failed)!";
				msg_type="S";
	        }
	        else
	        {
	        	msg = "Failed! - IRN Response file not found for Upload!";
				msg_type="E";
	        }
	        
	        url = "../extn_interface/frm_generate_irn.jsp?month="+month+"&year="+year+"&invoice_type="+invoice_type+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! - IRN Response Upload Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void GenerateExcelForOthIRN(HttpServletRequest request) throws SQLException 
	{
		String function_nm="GenerateExcelForOthIRN()";
		msg="";
		msg_type="";
		url="";
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		try
		{
			String invoice_no[] = request.getParameterValues("invoice_no");
			String counterparty_cd[] = request.getParameterValues("counterparty_cd");
			String contract_type[] = request.getParameterValues("contract_type");
			String sys_or_ff_inv[] = request.getParameterValues("sys_or_ff_inv");
			String inv_type[] = request.getParameterValues("inv_type");
			
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
			String fileName="",sheetnm="";
			
			if(invoice_no!=null)
			{
				String sysdate_time=utilDate.getSysdateWithTime24hrWithSS();
				String systime=sysdate_time.replaceAll(" ", "-");
				systime=systime.replaceAll(":", "");
				systime=systime.replaceAll("/", "");
				
				fileName ="OTH_INVOICE-"+month+"-"+year+"-"+systime+".xlsx";
				sheetnm="OTH_INVOICE-"+month+"-"+year+"-"+systime+"";
				
				String work_data=utilBean.getAutomationKeyDetail(dbcon, "WORK_DATA_"+comp_cd);
				
				String file_path=work_data+File.separator+"IRN_EXCEL"+File.separator+"Oth_Generated";
				
				if(!new File(file_path).exists())
				{
					new File(file_path).mkdirs();
				}
				
				file_path=file_path+File.separator+fileName;
	            XSSFSheet sheet = workbook.createSheet(sheetnm); 
	            Row rowhead = sheet.createRow((short)0);
	            int cellno=0;
	            rowhead.createCell((short)cellno++).setCellValue("LocationGstin");
	            rowhead.createCell((short)cellno++).setCellValue("LocationName");
	            rowhead.createCell((short)cellno++).setCellValue("ReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("LiabilityDischargeReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("ITCClaimReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("Purpose");
	            rowhead.createCell((short)cellno++).setCellValue("AutoPushOrGenerate");
	            rowhead.createCell((short)cellno++).setCellValue("SupplyType");
	            rowhead.createCell((short)cellno++).setCellValue("Irn");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentType");
	            rowhead.createCell((short)cellno++).setCellValue("TransactionType");
	            rowhead.createCell((short)cellno++).setCellValue("TransactionNature");
	            rowhead.createCell((short)cellno++).setCellValue("TransactionTypeDescription");
	            rowhead.createCell((short)cellno++).setCellValue("TaxpayerType");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentSeriesCode");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromGstin");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromLegalName");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromVendorCode");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromCity");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromPincode");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromPhone");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromEmail");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromGstin");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromVendorCode");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromCity");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromPincode");
	            rowhead.createCell((short)cellno++).setCellValue("BillToGstin");
	            rowhead.createCell((short)cellno++).setCellValue("BillToLegalName");
	            rowhead.createCell((short)cellno++).setCellValue("BillToTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("BillToVendorCode");
	            rowhead.createCell((short)cellno++).setCellValue("BillToAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("BillToAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("BillToCity");
	            rowhead.createCell((short)cellno++).setCellValue("BillToStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("BillToPincode");
	            rowhead.createCell((short)cellno++).setCellValue("BillToPhone");
	            rowhead.createCell((short)cellno++).setCellValue("BillToEmail");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToGstin");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToLegalName");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToVendorCode");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToCity");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToPincode");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentType");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentMode");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentAmount");
	            rowhead.createCell((short)cellno++).setCellValue("AdvancePaidAmount");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentDate");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentRemarks");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentTerms");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentInstruction");
	            rowhead.createCell((short)cellno++).setCellValue("PayeeName");
	            rowhead.createCell((short)cellno++).setCellValue("PayeeAccountNumber");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentAmountDue");
	            rowhead.createCell((short)cellno++).setCellValue("Ifsc");
	            rowhead.createCell((short)cellno++).setCellValue("CreditTransfer");
	            rowhead.createCell((short)cellno++).setCellValue("DirectDebit");
	            rowhead.createCell((short)cellno++).setCellValue("CreditDays");
	            rowhead.createCell((short)cellno++).setCellValue("CreditAvailedDate");
	            rowhead.createCell((short)cellno++).setCellValue("CreditReversalDate");
	            rowhead.createCell((short)cellno++).setCellValue("RefDocumentRemarks");
	            rowhead.createCell((short)cellno++).setCellValue("RefDocumentPeriodStartDate");
	            rowhead.createCell((short)cellno++).setCellValue("RefDocumentPeriodEndDate");
	            rowhead.createCell((short)cellno++).setCellValue("RefPrecedingDocumentDetails");
	            rowhead.createCell((short)cellno++).setCellValue("RefContractDetails");
	            rowhead.createCell((short)cellno++).setCellValue("AdditionalSupportingDocumentDetails");
	            rowhead.createCell((short)cellno++).setCellValue("BillNumber");
	            rowhead.createCell((short)cellno++).setCellValue("BillDate");
	            rowhead.createCell((short)cellno++).setCellValue("PortCode");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentCurrencyCode");
	            rowhead.createCell((short)cellno++).setCellValue("DestinationCountry");
	            rowhead.createCell((short)cellno++).setCellValue("ExportDuty");
	            rowhead.createCell((short)cellno++).setCellValue("Pos");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentValue");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentValueInForeignCurrency");
	            rowhead.createCell((short)cellno++).setCellValue("RoundOffAmount");
	            rowhead.createCell((short)cellno++).setCellValue("DifferentialPercentage");
	            rowhead.createCell((short)cellno++).setCellValue("ReverseCharge");
	            rowhead.createCell((short)cellno++).setCellValue("ClaimRefund");
	            rowhead.createCell((short)cellno++).setCellValue("UnderIgstAct");
	            rowhead.createCell((short)cellno++).setCellValue("RefundEligibility");
	            rowhead.createCell((short)cellno++).setCellValue("ECommerceGstin");
	            rowhead.createCell((short)cellno++).setCellValue("TDSGSTIN");
	            rowhead.createCell((short)cellno++).setCellValue("PnrOrUniqueNumber");
	            rowhead.createCell((short)cellno++).setCellValue("AvailProvisionalItc");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalGstin");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalDocumentType");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalAmountDeducted");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalPortCode");
	            rowhead.createCell((short)cellno++).setCellValue("TransportDateTime");
	            rowhead.createCell((short)cellno++).setCellValue("TransporterId");
	            rowhead.createCell((short)cellno++).setCellValue("TransporterName");
	            rowhead.createCell((short)cellno++).setCellValue("TransportMode");
	            rowhead.createCell((short)cellno++).setCellValue("Distance");
	            rowhead.createCell((short)cellno++).setCellValue("TransportDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("TransportDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("VehicleNumber");
	            rowhead.createCell((short)cellno++).setCellValue("VehicleType");
	            rowhead.createCell((short)cellno++).setCellValue("ToEmailAddresses");
	            rowhead.createCell((short)cellno++).setCellValue("ToMobileNumbers");
	            rowhead.createCell((short)cellno++).setCellValue("JWOriginalDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("JWOriginalDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("JWDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("JWDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("Custom1");
	            rowhead.createCell((short)cellno++).setCellValue("Custom2");
	            rowhead.createCell((short)cellno++).setCellValue("Custom3");
	            rowhead.createCell((short)cellno++).setCellValue("Custom4");
	            rowhead.createCell((short)cellno++).setCellValue("Custom5");
	            rowhead.createCell((short)cellno++).setCellValue("Custom6");
	            rowhead.createCell((short)cellno++).setCellValue("Custom7");
	            rowhead.createCell((short)cellno++).setCellValue("Custom8");
	            rowhead.createCell((short)cellno++).setCellValue("Custom9");
	            rowhead.createCell((short)cellno++).setCellValue("Custom10");
	            rowhead.createCell((short)cellno++).setCellValue("SerialNumber");
	            rowhead.createCell((short)cellno++).setCellValue("IsService");
	            rowhead.createCell((short)cellno++).setCellValue("Hsn");
	            rowhead.createCell((short)cellno++).setCellValue("ProductCode");
	            rowhead.createCell((short)cellno++).setCellValue("ItemName");
	            rowhead.createCell((short)cellno++).setCellValue("ItemDescription");
	            rowhead.createCell((short)cellno++).setCellValue("NatureOfJWDone");
	            rowhead.createCell((short)cellno++).setCellValue("Barcode");
	            rowhead.createCell((short)cellno++).setCellValue("Uqc");
	            rowhead.createCell((short)cellno++).setCellValue("Quantity");
	            rowhead.createCell((short)cellno++).setCellValue("FreeQuantity");
	            rowhead.createCell((short)cellno++).setCellValue("LossUnitOfMeasure");
	            rowhead.createCell((short)cellno++).setCellValue("LossTotalQuantity");
	            rowhead.createCell((short)cellno++).setCellValue("Rate");
	            rowhead.createCell((short)cellno++).setCellValue("CessRate");
	            rowhead.createCell((short)cellno++).setCellValue("StateCessRate");
	            rowhead.createCell((short)cellno++).setCellValue("CessNonAdvaloremRate");
	            rowhead.createCell((short)cellno++).setCellValue("PricePerQuantity");
	            rowhead.createCell((short)cellno++).setCellValue("DiscountAmount");
	            rowhead.createCell((short)cellno++).setCellValue("GrossAmount");
	            rowhead.createCell((short)cellno++).setCellValue("OtherCharges");
	            rowhead.createCell((short)cellno++).setCellValue("TaxableValue");
	            rowhead.createCell((short)cellno++).setCellValue("PreTaxValue");
	            rowhead.createCell((short)cellno++).setCellValue("IgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("SgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CessAmount");
	            rowhead.createCell((short)cellno++).setCellValue("StateCessAmount");
	            rowhead.createCell((short)cellno++).setCellValue("StateCessNonAdvaloremAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CessNonAdvaloremAmount");
	            rowhead.createCell((short)cellno++).setCellValue("OrderLineReference");
	            rowhead.createCell((short)cellno++).setCellValue("OriginCountry");
	            rowhead.createCell((short)cellno++).setCellValue("ItemTotal");
	            rowhead.createCell((short)cellno++).setCellValue("ItemAttributeDetails");
	            rowhead.createCell((short)cellno++).setCellValue("TaxType");
	            rowhead.createCell((short)cellno++).setCellValue("BatchNameNumber");
	            rowhead.createCell((short)cellno++).setCellValue("BatchExpiryDate");
	            rowhead.createCell((short)cellno++).setCellValue("WarrantyDate");
	            rowhead.createCell((short)cellno++).setCellValue("ItcEligibility");
	            rowhead.createCell((short)cellno++).setCellValue("ItcIgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("ItcCgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("ItcSgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("ItcCessAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem1");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem2");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem3");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem4");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem5");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem6");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem7");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem8");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem9");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem10");
	            /*if(Vb2c.contains("Y")){
	            	rowhead.createCell((short)cellno++).setCellValue("UpiId");
	            }*/
	            
	            String company_nm=utilBean.getCompanyName(dbcon, comp_cd);
	            
	            int k=0;
            	int serial_no=0;
            	
	            for(int i=0;i<invoice_no.length;i++)
	            {
	            	queryString="SELECT NULL,NULL,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
	            			+ "NET_PAYABLE,NULL,GROSS_AMT,NULL,NULL,INVOICE_SEQ,FINANCIAL_YEAR,NULL,"
	            			+ "NULL,TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),SALE_PRICE,SALE_PRICE_UNIT,NULL,NULL,NULL,TAX_AMT_INR,"
	            			+ "VENDOR_TYPE,SUPPLIER_CD,SALE_PRICE,INVOICE_CATEGORY,BU_UNIT,INVOICE_CATEGORY "
	    					+ "FROM FMS_OTH_INVOICE_MST "
	    					+ "WHERE COMPANY_CD=? AND VENDOR_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=?";
	            	int st_count=0;
	    			stmt=dbcon.prepareStatement(queryString);
	    			stmt.setString(++st_count, comp_cd);
	    			stmt.setString(++st_count, counterparty_cd[i]);
	    			stmt.setString(++st_count, invoice_no[i]);
	    			stmt.setString(++st_count, sys_or_ff_inv[i]);
	    			rset=stmt.executeQuery();
	    			while(rset.next())
	    			{
	    				serial_no=0;
	    				String inv_dt=rset.getString(3)==null?"":rset.getString(3);
	    				String ReturnPeriod="";
	    				if(!inv_dt.equals(""))
	    				{
	    					String[] split = inv_dt.split("/");
	    					ReturnPeriod=split[1]+""+split[2];
	    				}
	    				
	    				String net_amt=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
	    				String qty=rset.getString(5)==null?"":nf.format(rset.getDouble(5));
	    				String gross_amt=rset.getString(6)==null?"":nf.format(rset.getDouble(6));
	    				String tax_amt=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
	    				String state_tin=rset.getString(8)==null?"":rset.getString(8);
	    				String inv_seq=rset.getString(9)==null?"":rset.getString(9);
	    				String fin_yr=rset.getString(10)==null?"":rset.getString(10);
	    				 
	    				String sub_inv_type=rset.getString(16)==null?"":rset.getString(16);
	    				String qty_unit=rset.getString(18)==null?"":rset.getString(18); //IT'S IS USED FOR DLNG SERVICE INVOICE(TMS) ONLY
	    				
	    				String inv_flag=rset.getString(11)==null?"":rset.getString(11);
	    				String period_end_dt=rset.getString(13)==null?"":rset.getString(13);
	    				double total_tax_amt=rset.getDouble(19);//
	    				String entity_role=rset.getString(20)==null?"":rset.getString(20);
	    				String supp_cd=rset.getString(21)==null?"":rset.getString(21);
	    				String tax_cat = rset.getString(23)==null?"S":rset.getString(23);
	    				String bu_unit = rset.getString(24)==null?"S":rset.getString(24);
	    						
	    				String plantAddress="";
	    				String plantCity="";
	    				String plantState="";
	    				String plantPin="";
	    				String price="";
	    				String tax_rate="";
	    				String bu_plantAddress="";
	    				String bu_plantCity="";
	    				String bu_plantState="";
	    				String bu_plantPin="";
	    				String plant_gstin_no="";
	    				String bu_gstin_no="";
	    				tax_amt = ""+total_tax_amt;
	    				String documentType="INV";
	    				String item_desc="";
	    				String sac_no="";
	    				String sac_cd="";
	    				String hsn_code="";
	    				String sac_desc="";
	    				String tax_type="";
	    				double item_tax_amt=0;
	    				String tax_struct_cd="";
	    				String amt = "";
	    				String igst_amt="";
	    				String cgst_amt="";
	    				String sgst_amt="";
	    				String TransactionType="";
	    				double total = 0;
	    				double tax = 0;
	    				String flag = "";
	    				String inv_cat = "";
	    				double temp = 0;
	    				
	    				String bu_statCode="";
	    				String plant_statCode="";
	    				String countpty_nm="";
	    				
	    				String queryString2="SELECT SAC_CODE,ITEM_DESCRIPTION,TAX_CD,TAX_STRUCT_CD,GRT,HSN_CODE,SALE_PRICE,QUANTITY,ITEM_AMT,ITEM_TAX_AMT,CARGO_AMOUNT,INV_FLAG,REF_NO "
		    					+ "FROM FMS_OTH_INVOICE_DTL "
		    					+ "WHERE COMPANY_CD=? AND VENDOR_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? "
		    					+ "AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ?"
		    					+ "";
		            	int st_count2=0;
		    			stmt2=dbcon.prepareStatement(queryString2);
		    			stmt2.setString(++st_count2, comp_cd);
		    			stmt2.setString(++st_count2, counterparty_cd[i]);
		    			stmt2.setString(++st_count2, invoice_no[i]);
		    			stmt2.setString(++st_count2, sys_or_ff_inv[i]);
		    			stmt2.setString(++st_count2, supp_cd);
		    			stmt2.setString(++st_count2, inv_seq);
		    			stmt2.setString(++st_count2, fin_yr);
		    			rset2=stmt2.executeQuery();
		    			while(rset2.next())
		    			{
		    				sac_cd = rset2.getString(1)==null?"":rset2.getString(1);
		    				item_desc = rset2.getString(2)==null?"":rset2.getString(2);
		    				tax_type = rset2.getString(3)==null?"":rset2.getString(3);
		    				tax_struct_cd = rset2.getString(4)==null?"":rset2.getString(4);
		    				
		    				if (sys_or_ff_inv[i].equals("HSA")) 
		    				{
								qty = rset2.getString(5) == null ? "" : nf.format(rset2.getDouble(5));
							}
		    				else if(sys_or_ff_inv[i].equals("AHPL") || sys_or_ff_inv[i].equals("COSTRH") || 
		    						sys_or_ff_inv[i].equals("NPR")) 
		    				{
		    					qty = "0.00";
		    				}
		    				else if(sys_or_ff_inv[i].equals("HS"))
		    				{
		    					temp += rset2.getDouble(8);
		    					qty = nf.format(temp);
		    				}
		    				else 
		    				{
		    					qty = rset2.getString(8) == null ? "" : nf.format(rset2.getDouble(8));
		    				}
		    				
		    				if(sys_or_ff_inv[i].equals("GA"))
		    				{
		    					qty = "0.00";
		    					gross_amt = rset2.getString(9)==null?"":nf.format(rset2.getDouble(9));	
		    					price = "0.00";
		    				}
							hsn_code = rset2.getString(6)==null?"":rset2.getString(6);
							
							if(sys_or_ff_inv[i].equals("SFA"))
							{
								price = rset2.getString(7)==null?"":nf.format(rset2.getDouble(7));
								gross_amt = rset2.getString(9)==null?"":nf.format(rset2.getDouble(9));	
							}
							else if(sys_or_ff_inv[i].equals("AHPL") || sys_or_ff_inv[i].equals("COSTRH") || 
									sys_or_ff_inv[i].equals("NPR")) 
							{
								price = "0.00";
							}
							else if(sys_or_ff_inv[i].equals("HS") && (!invoice_no[i].startsWith("C") || !invoice_no[i].startsWith("D")))
							{
								price = rset2.getString(7)==null?"":nf.format(rset2.getDouble(7));
								
								if(!gross_amt.equals("") && !qty.equals(""))
			    				{
			    					if(Double.parseDouble(qty) > 0)
			    					{
			    						price=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(qty));
			    					}
			    					else
			    					{
			    						price=nf.format(0);
			    					}
			    				}
								
							}
							else if(sys_or_ff_inv[i].equals("HSA"))
							{
								price = rset2.getString(7)==null?"":nf.format(rset2.getDouble(7));
							}
							amt = rset2.getString(9)==null?"":nf.format(rset2.getDouble(9));
							item_tax_amt = rset2.getDouble(10);
							inv_flag= rset2.getString(12)==null?"":rset2.getString(12);
							String cr_dr_ref_no= rset2.getString(13)==null?"":rset2.getString(13);
							
							if(inv_flag.equals("CR"))
		    				{
		    					documentType="CRN";
		    				}
		    				else if(inv_flag.equals("DR"))
		    				{
		    					documentType="DBN";
		    				}
							
							if(tax_struct_cd.equals("") && (inv_flag.equals("CR") || inv_flag.equals("DR")))
							{
								String queryString3="SELECT TAX_CD,TAX_STRUCT_CD "
				    					+ "FROM FMS_OTH_INVOICE_DTL "
				    					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				            	int st_count3=0;
				    			stmt3=dbcon.prepareStatement(queryString3);
				    			stmt3.setString(++st_count3, comp_cd);
				    			stmt3.setString(++st_count3, cr_dr_ref_no);
				    			rset3=stmt3.executeQuery();
				    			if(rset3.next())
				    			{
				    				tax_type = rset3.getString(1)==null?"":rset3.getString(1);
				    				tax_struct_cd = rset3.getString(2)==null?"":rset3.getString(2);
				    			}
				    			rset3.close();
				    			stmt3.close();
							}
							
							if(tax_cat.equals("P"))
							{
								flag = "N";
							}
							else 
							{
								flag = "Y";
							}
							
							if ((sys_or_ff_inv[i].equals("HSA")) && (invoice_no[i].startsWith("C") || invoice_no[i].startsWith("D"))) 
							{
								query1 = "SELECT GRT,QUANTITY " 
										+ "FROM FMS_OTH_INV_CRDR_REF " 
										+ "WHERE COMPANY_CD=? AND VENDOR_CD=? AND INVOICE_TYPE=? "
				    					+ "AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ?";
								st_count2=0;
								stmt1 = dbcon.prepareStatement(query1);
								stmt1.setString(++st_count2, comp_cd);
				    			stmt1.setString(++st_count2, counterparty_cd[i]);
				    			stmt1.setString(++st_count2, sys_or_ff_inv[i]);
				    			stmt1.setString(++st_count2, supp_cd);
				    			stmt1.setString(++st_count2, inv_seq);
				    			stmt1.setString(++st_count2, fin_yr);
								rset1 = stmt1.executeQuery();
								if (rset1.next()) 
								{
									if (rset2.getString(5).equals("0"))
									{
										qty = nf.format(rset1.getDouble(1));
										
									}
									if (Double.doubleToRawLongBits(Double.parseDouble(qty)) < Double.doubleToRawLongBits(0)) 
									{
										qty = "" + (-1) * Double.parseDouble(qty);
									} 
									if(Double.doubleToRawLongBits(Double.parseDouble(price))<Double.doubleToRawLongBits(0))
									{
										price=""+(-1)*Double.parseDouble(price);
									}
								
								}
								rset1.close();
								stmt1.close();
							}

							if ((sys_or_ff_inv[i].equals("HS")) && (invoice_no[i].startsWith("C") || invoice_no[i].startsWith("D"))) 
							{
								query1 = "SELECT QUANTITY " 
										+ "FROM FMS_OTH_INV_CRDR_REF " 
										+ "WHERE COMPANY_CD=? AND VENDOR_CD=? AND INVOICE_TYPE=? "
				    					+ "AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ?";
								st_count2=0;
								stmt1 = dbcon.prepareStatement(query1);
								stmt1.setString(++st_count2, comp_cd);
				    			stmt1.setString(++st_count2, counterparty_cd[i]);
				    			stmt1.setString(++st_count2, sys_or_ff_inv[i]);
				    			stmt1.setString(++st_count2, supp_cd);
				    			stmt1.setString(++st_count2, inv_seq);
				    			stmt1.setString(++st_count2, fin_yr);
								rset1 = stmt1.executeQuery();
								if (rset1.next()) 
								{
									qty = nf.format(rset1.getDouble(1));
										
									if (Double.doubleToRawLongBits(Double.parseDouble(qty)) < Double.doubleToRawLongBits(0)) 
									{
										qty = "" + (-1) * Double.parseDouble(qty);
									} 
								}
								rset1.close();
								stmt1.close();
								
								if(!gross_amt.equals("") && !qty.equals(""))
			    				{
			    					if(Double.parseDouble(qty) > 0)
			    					{
			    						price=nf.format(Double.parseDouble(gross_amt)/Double.parseDouble(qty));
			    					}
			    					else
			    					{
			    						price=nf.format(0);
			    					}
			    				}
							}
							
							if((sys_or_ff_inv[i].equals("SFA")) && (invoice_no[i].startsWith("C") || invoice_no[i].startsWith("D")) && qty.equals("0.00"))
							{
								query1 = "SELECT QUANTITY " 
										+ "FROM FMS_OTH_INV_CRDR_REF " 
										+ "WHERE COMPANY_CD=? AND VENDOR_CD=? AND INVOICE_TYPE=? "
				    					+ "AND SUPPLIER_CD = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND ITEM_DESCRIPTION = ? ";
								st_count2=0;
								stmt1 = dbcon.prepareStatement(query1);
								stmt1.setString(++st_count2, comp_cd);
				    			stmt1.setString(++st_count2, counterparty_cd[i]);
				    			stmt1.setString(++st_count2, sys_or_ff_inv[i]);
				    			stmt1.setString(++st_count2, supp_cd);
				    			stmt1.setString(++st_count2, inv_seq);
				    			stmt1.setString(++st_count2, fin_yr);
				    			stmt1.setString(++st_count2, item_desc);
								rset1 = stmt1.executeQuery();
								if (rset1.next()) 
								{
									qty = nf.format(rset1.getDouble(1));
									
									if (Double.doubleToRawLongBits(Double.parseDouble(qty)) < Double.doubleToRawLongBits(0)) 
									{
										qty = "" + (-1) * Double.parseDouble(qty);
									} 
								}
							}
							
							query1="SELECT SAC_CODE,SAC_DESC "
	    							+ "FROM FMS_SAC_MST "
	    							+ "WHERE SAC_CD=? ";
	    					stmt1=dbcon.prepareStatement(query1);
	    					stmt1.setString(1, sac_cd);
	    					rset1=stmt1.executeQuery();
	    					if(rset1.next())
	    					{
	    						sac_no=rset1.getString(1)==null?"":rset1.getString(1);
			    				sac_desc=rset1.getString(2)==null?"":rset1.getString(2);
	    					}
	    					rset1.close();
	    					stmt1.close();
	    					
	    					if(sys_or_ff_inv[i].equals("GA"))
	    					{
	    						hsn_code = sac_no;
	    					}
	    					
		    				if(!gross_amt.equals("") && !tax_amt.equals(""))
		    				{
		    					query1  = "SELECT A.DESCR "
		    							+ "FROM FMS_TAX_STRUCTURE A "
		    							+ "WHERE A.TAX_STR_CD=? AND A.PAY_RECV= ? AND A.TAX_CATEGORY = ? "
		    							+ "AND A.APP_DATE=(SELECT MAX(B.APP_DATE) FROM FMS_TAX_STRUCTURE B "
					    				+ "WHERE A.TAX_STR_CD=B.TAX_STR_CD AND B.APP_DATE<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
		    					stmt1=dbcon.prepareStatement(query1);
		    					stmt1.setString(1, tax_struct_cd);
		    					stmt1.setString(2, "R");
		    					stmt1.setString(3, tax_cat);
		    					rset1=stmt1.executeQuery();
		    					if(rset1.next())
		    					{
		    						tax_rate = rset1.getString(1)==null?"":rset1.getString(1);
		    						
		    						if(tax_rate.contains("C"))
		    						{
		    							tax_rate = tax_rate.split(" ")[1];
		    							tax_rate = tax_rate.split("%")[0];
		    							tax = Double.parseDouble(tax_rate) * 2;
		    							
		    							if (sys_or_ff_inv[i].equals("SFA") || sys_or_ff_inv[i].equals("GA")) 
		    							{
		    								if(inv_cat.equals("P"))
		    								{
			    								cgst_amt = "" + nf.format(item_tax_amt / 2);
												sgst_amt = "" + nf.format(item_tax_amt / 2);
												
												total = item_tax_amt+Double.parseDouble(amt);
		    								}
		    								else 
		    								{
		    									cgst_amt = "" + nf.format((Double.parseDouble(tax_rate) * Double.parseDouble(amt)) / 100);
												sgst_amt = "" + nf.format((Double.parseDouble(tax_rate) * Double.parseDouble(amt)) / 100);
												
												total = Double.parseDouble(cgst_amt)+Double.parseDouble(sgst_amt)+Double.parseDouble(amt);
		    								}
		    							}
		    							else 
		    							{
		    								cgst_amt = "" + nf.format(total_tax_amt / 2);
											sgst_amt = "" + nf.format(total_tax_amt / 2);
		    							}
		    							if(inv_flag.equals("CR") || inv_flag.equals("DR"))
		    							{
			    							if(Double.doubleToRawLongBits(Double.parseDouble(cgst_amt))<Double.doubleToRawLongBits(0))
											{
												cgst_amt=""+(-1)*Double.parseDouble(cgst_amt);
											}
											if(Double.doubleToRawLongBits(Double.parseDouble(sgst_amt))<Double.doubleToRawLongBits(0))
											{
												sgst_amt=""+(-1)*Double.parseDouble(sgst_amt);
											}
		    							}
		    						}
		    						else if(tax_rate.contains("I"))
		    						{
		    							tax_rate = tax_rate.split(" ")[1];
		    							tax_rate = tax_rate.split("%")[0];
		    							tax = Double.parseDouble(tax_rate);
		    							
		    							if (sys_or_ff_inv[i].equals("SFA") || sys_or_ff_inv[i].equals("GA")) 
		    							{ 
		    								
		    								if(inv_cat.equals("P"))
		    								{
		    									igst_amt = "" + nf.format(item_tax_amt);
		    									total = item_tax_amt+Double.parseDouble(amt);
		    								}
		    								else 
		    								{
		    									igst_amt = "" + nf.format(Double.parseDouble(tax_rate) * Double.parseDouble(amt));
		    									total = Double.parseDouble(igst_amt)+Double.parseDouble(amt);
		    								}
		    								
		    							}
		    							else 
		    							{
		    								igst_amt = "" + nf.format(total_tax_amt);
		    							}
		    							if(inv_flag.equals("CR") || inv_flag.equals("DR"))
		    							{
			    							if(Double.doubleToRawLongBits(Double.parseDouble(igst_amt))<Double.doubleToRawLongBits(0))
											{
												igst_amt=""+(-1)*Double.parseDouble(igst_amt);
											}
		    							}
		    						}
		    						
		    					}
		    					rset1.close();
		    					stmt1.close();
		    				}
	
							
							if(inv_flag.equals("CR") || inv_flag.equals("DR"))
							{
								if (Double.doubleToRawLongBits(Double.parseDouble(qty)) < Double.doubleToRawLongBits(0)) 
								{
									qty = "" + (-1) * Double.parseDouble(qty);
								} 
								if(Double.doubleToRawLongBits(Double.parseDouble(""+total))<Double.doubleToRawLongBits(0))
								{
									total= -1*total;
								}
								if(Double.doubleToRawLongBits(Double.parseDouble(gross_amt))<Double.doubleToRawLongBits(0))
								{
									gross_amt=""+(-1)*Double.parseDouble(gross_amt);
								}
								if(Double.doubleToRawLongBits(Double.parseDouble(price))<Double.doubleToRawLongBits(0))
								{
									price=""+(-1)*Double.parseDouble(price);
								}
								if(Double.doubleToRawLongBits(Double.parseDouble(tax_amt))<Double.doubleToRawLongBits(0))
								{
									tax_amt=""+(-1)*Double.parseDouble(tax_amt);;
								}
								if(Double.doubleToRawLongBits(Double.parseDouble(net_amt))<Double.doubleToRawLongBits(0))
								{
									net_amt=""+(-1)*Double.parseDouble(net_amt);
								}
								if(Double.doubleToRawLongBits(total_tax_amt)<Double.doubleToRawLongBits(0))
								{
									total_tax_amt=(-1)*total_tax_amt;
								}
							}
							
							gross_amt = nf.format(Double.parseDouble(gross_amt));
							tax_amt = nf.format(Double.parseDouble(tax_amt));
							total_tax_amt = Double.parseDouble(nf.format(total_tax_amt));
							net_amt = nf.format(Double.parseDouble(net_amt));
		    				
		    				if(!sys_or_ff_inv[i].equals("GA"))
		    				{
			    				String queryString3="SELECT GSTIN_NO,ENTITY_NAME "
			    						+ "FROM FMS_OTH_ENTITY_MST "
			    						+ "WHERE ENTITY_CD=? AND ENTITY_TYPE = ? ";
			    				stmt3=dbcon.prepareStatement(queryString3);
			    				stmt3.setString(1, supp_cd);
			    				stmt3.setString(2, "S");
			    				rset3=stmt3.executeQuery();
			    				if(rset3.next())
			    				{
			    					bu_gstin_no = rset3.getString(1)==null?"":rset3.getString(1);
			    					company_nm = rset3.getString(2)==null?"":rset3.getString(2);
			    				}
			    				rset3.close();
			    				stmt3.close();
		    				}
		    				else 
		    				{
		    					String queryString3  = "SELECT COMPANY_NM "
		    							+ "FROM FMS_COMPANY_OWNER_MST "
		    							+ "WHERE COMPANY_ABBR='SEIPL' AND STATUS = 'Y' ";
		    					stmt3=dbcon.prepareStatement(queryString3);
		    					rset3=stmt3.executeQuery();
		    					while(rset3.next())
		    					{
		    						company_nm = rset3.getString(1)==null?"":rset3.getString(1);
		    					}
		    					rset3.close();
		    					stmt3.close();
		    					
		    					queryString3 = "SELECT A.STAT_NO "
		    							+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B  "
		    							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.ENTITY = 'B' AND A.PLANT_SEQ_NO=? "
		    							+ "AND A.STAT_CD = B.STAT_CD AND B.STAT_TYPE = 'R' AND B.STAT_NM = 'GSTIN' ";
		    					stmt1 = dbcon.prepareStatement(queryString3);
		    					stmt1.setString(1, supp_cd);
		    					stmt1.setString(2, supp_cd);
		    					stmt1.setString(3, bu_unit);
		    					rset1 = stmt1.executeQuery();
		    					if(rset1.next())
		    					{
		    						bu_gstin_no = rset1.getString(1)==null?"":rset1.getString(1);
		    					}
		    					stmt1.close();
		    					rset1.close();
		    				}
	
		    				String queryString4="SELECT GSTIN_NO,ENTITY_NAME "
		    						+ "FROM FMS_OTH_ENTITY_MST "
		    						+ "WHERE ENTITY_CD=? AND ENTITY_TYPE = ? ";
		    				stmt4=dbcon.prepareStatement(queryString4);
		    				stmt4.setString(1, counterparty_cd[i]);
		    				stmt4.setString(2, entity_role);
		    				rset4=stmt4.executeQuery();
		    				if(rset4.next())
		    				{
		    					plant_gstin_no = rset4.getString(1)==null?"":rset4.getString(1);
		    					countpty_nm=rset4.getString(2)==null?"":rset4.getString(2);
		    				}
		    				rset4.close();
		    				stmt4.close();
		    				
		    				String business_flag ="";
		    				queryString4="SELECT A.BUSINESS_FLAG "
		    						+ "FROM FMS_OTH_ENTITY_MST A "
		    						+ "WHERE A.ENTITY_CD = ? AND A.ENTITY_TYPE = ? "
		    						+ "AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_MST B WHERE A.ENTITY_CD = B.ENTITY_CD AND B.ENTITY_TYPE=A.ENTITY_TYPE) ";
		    				stmt4=dbcon.prepareStatement(queryString4);
		    				stmt4.setString(1, counterparty_cd[i]);
		    				stmt4.setString(2, entity_role);
		    				rset4=stmt4.executeQuery();
		    				if(rset4.next())
		    				{
		    					business_flag = rset4.getString(1)==null?"":rset4.getString(1);
		    				}
		    				rset4.close();
		    				stmt4.close();
		    				
		    				TransactionType=business_flag.equals("C")?"B2C":"B2B";
		    				
		    				if(!sys_or_ff_inv[i].equals("GA"))
		    				{
		    					String query = "SELECT ADDR,CITY,PIN,NULL,STATE "
			    						+ "FROM FMS_OTH_ENTITY_ADDR_MST "
			    						+ "WHERE ENTITY_CD = ? AND ENTITY_TYPE = ? AND ADDRESS_TYPE='R'";
			    				stmt5 = dbcon.prepareStatement(query);
			    				stmt5.setString(1,supp_cd);
			    				stmt5.setString(2,"S");
			    				rset5 = stmt5.executeQuery();
			    				if(rset5.next())
			    				{
			    					bu_plantAddress = rset5.getString(1)==null?"":rset5.getString(1);
			    					bu_plantCity = rset5.getString(2)==null?"":rset5.getString(2);
			    					bu_plantPin= rset5.getString(3)==null?"":rset5.getString(3);
			    					bu_plantState=rset5.getString(5)==null?"":rset5.getString(5);
			    				}
			    				stmt5.close();
			    				rset5.close();
		    				}
		    				else 
		    				{
		    					String query = "SELECT A.PLANT_ADDR,A.PLANT_CITY,A.PLANT_PIN,A.PLANT_STATE "
		    							+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
		    							+ "WHERE A.COMPANY_CD=? AND A.ENTITY = ? AND A.SEQ_NO = ? "
		    							+ "AND A.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE A.COMPANY_CD = C.COMPANY_CD "
		    							+ "AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND A.ENTITY = C.ENTITY AND A.SEQ_NO = C.SEQ_NO)";
			    				stmt5 = dbcon.prepareStatement(query);
			    				stmt5.setString(1,supp_cd);
			    				stmt5.setString(2,"B");
			    				stmt5.setString(3,bu_unit);
			    				rset5 = stmt5.executeQuery();
			    				if(rset5.next())
			    				{
			    					bu_plantAddress = rset5.getString(1)==null?"":rset5.getString(1);
			    					bu_plantCity = rset5.getString(2)==null?"":rset5.getString(2);
			    					bu_plantPin= rset5.getString(3)==null?"":rset5.getString(3);
			    					bu_plantState=rset5.getString(4)==null?"":rset5.getString(4);
			    				}
			    				stmt5.close();
			    				rset5.close();
		    				}
		    				
		    				String query1 = "SELECT ADDR,CITY,PIN,NULL,STATE "
		    						+ "FROM FMS_OTH_ENTITY_ADDR_MST "
		    						+ "WHERE ENTITY_CD = ? AND ENTITY_TYPE = ? AND ADDRESS_TYPE='R'";
		    				stmt6 = dbcon.prepareStatement(query1);
		    				stmt6.setString(1,counterparty_cd[i]);
		    				stmt6.setString(2,entity_role);
		    				rset6 = stmt6.executeQuery();
		    				if(rset6.next())
		    				{
		    					plantAddress = rset6.getString(1)==null?"":rset6.getString(1);
		    					plantCity = rset6.getString(2)==null?"":rset6.getString(2);
		    					plantPin= rset6.getString(3)==null?"":rset6.getString(3);
		    					plantState=rset6.getString(5)==null?"":rset6.getString(5);
		    				}
		    				stmt6.close();
		    				rset6.close();
		    				
		    				
		    				query = "SELECT TIN "
		    						+ "FROM FMS_STATE_MST "
		    						+ "WHERE STATE_NM=? "
		    						+ "ORDER BY STATE_NM";
		    				stmt7 = dbcon.prepareStatement(query);
		    				stmt7.setString(1, bu_plantState);
		    				rset7 = stmt7.executeQuery();
		    				if(rset7.next())
		    				{
		    					bu_statCode= rset7.getString(1)==null?"":rset7.getString(1);
		    				}
		    				stmt7.close();
		    				rset7.close();
	
		    				query = "SELECT TIN "
		    						+ "FROM FMS_STATE_MST "
		    						+ "WHERE STATE_NM=? "
		    						+ "ORDER BY STATE_NM";
		    				stmt8 = dbcon.prepareStatement(query);
		    				stmt8.setString(1, plantState);
		    				rset8 = stmt8.executeQuery();
		    				if(rset8.next())
		    				{
		    					plant_statCode= rset8.getString(1)==null?"":rset8.getString(1);
		    				}
		    				stmt8.close();
		    				rset8.close();
		    				
		    				
	    				
		    				if (sys_or_ff_inv[i].equals("SFA") || sys_or_ff_inv[i].equals("GA")) 
		    				{
		    					serial_no++;
								k = k + 1;
								//serial_no=serial_no+1;
								int row_no = k;
								Row row = sheet.createRow((short) row_no);
								int cellno1 = 0;
								row.createCell((short) cellno1++).setCellValue("" + bu_gstin_no);//LocationGstin
								row.createCell((short) cellno1++).setCellValue("" + company_nm);//LocationName
								row.createCell((short) cellno1++).setCellValue("" + ReturnPeriod);//ReturnPeriod
								row.createCell((short) cellno1++).setCellValue("");//LiabilityDischargeReturnPeriod
								row.createCell((short) cellno1++).setCellValue("");//ITCClaimReturnPeriod
								row.createCell((short) cellno1++).setCellValue("EINV");//Purpose
								row.createCell((short) cellno1++).setCellValue("");//AutoPushOrGenerate
								row.createCell((short) cellno1++).setCellValue("S");//SupplyType //supply type S for sales and P for purchase 
								row.createCell((short) cellno1++).setCellValue("");//Irn
								row.createCell((short) cellno1++).setCellValue(documentType);//DocumentType
								row.createCell((short) cellno1++).setCellValue("" + TransactionType);//TransactionType
								row.createCell((short) cellno1++).setCellValue("");//TransactionNature
								row.createCell((short) cellno1++).setCellValue("");//TransactionTypeDescription
								row.createCell((short) cellno1++).setCellValue("");//TaxpayerType
								row.createCell((short) cellno1++).setCellValue("" + invoice_no[i]);//DocumentNumber
								row.createCell((short) cellno1++).setCellValue("");//DocumentSeriesCode
								row.createCell((short) cellno1++).setCellValue("" + inv_dt);//DocumentDate
								row.createCell((short) cellno1++).setCellValue("" + bu_gstin_no);//BillFromGstin
								row.createCell((short) cellno1++).setCellValue("" + company_nm);//BillFromLegalName
								row.createCell((short) cellno1++).setCellValue("");//BillFromTradeName
								row.createCell((short) cellno1++).setCellValue("");//BillFromVendorCode
								row.createCell((short) cellno1++).setCellValue("" + bu_plantAddress);//BillFromAddress1
								row.createCell((short) cellno1++).setCellValue("");//BillFromAddress2
								row.createCell((short) cellno1++).setCellValue("" + bu_plantCity);//BillFromCity
								row.createCell((short) cellno1++).setCellValue("" + bu_statCode);//BillFromStateCode
								row.createCell((short) cellno1++).setCellValue("" + bu_plantPin);//BillFromPincode
								row.createCell((short) cellno1++).setCellValue("");//BillFromPhone
								row.createCell((short) cellno1++).setCellValue("");//BillFromEmail
								row.createCell((short) cellno1++).setCellValue("");//DispatchFromGstin
								row.createCell((short) cellno1++).setCellValue("");//DispatchFromTradeName
								row.createCell((short) cellno1++).setCellValue("");//DispatchFromVendorCode
								row.createCell((short) cellno1++).setCellValue("");//DispatchFromAddress1
								row.createCell((short) cellno1++).setCellValue("");//DispatchFromAddress2
								row.createCell((short) cellno1++).setCellValue("");//DispatchFromCity
								row.createCell((short) cellno1++).setCellValue("");//DispatchFromStateCode
								row.createCell((short) cellno1++).setCellValue("");//DispatchFromPincode
								row.createCell((short) cellno1++).setCellValue("" + plant_gstin_no);//BillToGstin
								row.createCell((short) cellno1++).setCellValue("" + countpty_nm);//BillToLegalName
								row.createCell((short) cellno1++).setCellValue("");//BillToTradeName
								row.createCell((short) cellno1++).setCellValue("");//BillToVendorCode
								row.createCell((short) cellno1++).setCellValue("" + plantAddress);//BillToAddress1
								row.createCell((short) cellno1++).setCellValue("");//BillToAddress2
								row.createCell((short) cellno1++).setCellValue("" + plantCity);//BillToCity
								row.createCell((short) cellno1++).setCellValue("" + plant_statCode);//BillToStateCode
								row.createCell((short) cellno1++).setCellValue("" + plantPin);//BillToPincode
								row.createCell((short) cellno1++).setCellValue("");//BillToPhone
								row.createCell((short) cellno1++).setCellValue("");//BillToEmail
								row.createCell((short) cellno1++).setCellValue("");//ShipToGstin
								row.createCell((short) cellno1++).setCellValue("");//ShipToLegalName
								row.createCell((short) cellno1++).setCellValue("");//ShipToTradeName
								row.createCell((short) cellno1++).setCellValue("");//ShipToVendorCode
								row.createCell((short) cellno1++).setCellValue("");//ShipToAddress1
								row.createCell((short) cellno1++).setCellValue("");//ShipToAddress2
								row.createCell((short) cellno1++).setCellValue("");//ShipToCity
								row.createCell((short) cellno1++).setCellValue("");//ShipToStateCode
								row.createCell((short) cellno1++).setCellValue("");//ShipToPincode
								row.createCell((short) cellno1++).setCellValue("");//PaymentType
								row.createCell((short) cellno1++).setCellValue("");//PaymentMode
								row.createCell((short) cellno1++).setCellValue("");//PaymentAmount
								row.createCell((short) cellno1++).setCellValue("");//AdvancePaidAmount
								row.createCell((short) cellno1++).setCellValue("");//PaymentDate
								row.createCell((short) cellno1++).setCellValue("");//PaymentRemarks
								row.createCell((short) cellno1++).setCellValue("");//PaymentTerms
								row.createCell((short) cellno1++).setCellValue("");//PaymentInstruction
								row.createCell((short) cellno1++).setCellValue("");//PayeeName
								row.createCell((short) cellno1++).setCellValue("");//PayeeAccountNumber
								row.createCell((short) cellno1++).setCellValue("");//PaymentAmountDue
								row.createCell((short) cellno1++).setCellValue("");//Ifsc
								row.createCell((short) cellno1++).setCellValue("");//CreditTransfer
								row.createCell((short) cellno1++).setCellValue("");//DirectDebit
								row.createCell((short) cellno1++).setCellValue("");//CreditDays
								row.createCell((short) cellno1++).setCellValue("");//CreditAvailedDate
								row.createCell((short) cellno1++).setCellValue("");//CreditReversalDate
								row.createCell((short) cellno1++).setCellValue("");//RefDocumentRemarks
								row.createCell((short) cellno1++).setCellValue("");//RefDocumentPeriodStartDate
								row.createCell((short) cellno1++).setCellValue("");//RefDocumentPeriodEndDate
								row.createCell((short) cellno1++).setCellValue("");//RefPrecedingDocumentDetails
								row.createCell((short) cellno1++).setCellValue("");//RefContractDetails
								row.createCell((short) cellno1++).setCellValue("");//AdditionalSupportingDocumentDetails
								row.createCell((short) cellno1++).setCellValue("");//BillNumber
								row.createCell((short) cellno1++).setCellValue("");//BillDate
								row.createCell((short) cellno1++).setCellValue("");//PortCode
								if (sys_or_ff_inv[i].equals("HSA")) 
								{
									row.createCell((short) cellno1++).setCellValue("USD");//DocumentCurrencyCode
								} 
								else 
								{
									if(TransactionType.equals("B2C"))
									{
										row.createCell((short) cellno1++).setCellValue("INR");
									}
									else 
									{
										row.createCell((short) cellno1++).setCellValue("");
									}
								}
								if (sys_or_ff_inv[i].equals("HSA")) 
								{
									row.createCell((short) cellno1++).setCellValue("IN");//DestinationCountry
								} 
								else 
								{
									if(TransactionType.equals("B2C"))
									{
										row.createCell((short) cellno1++).setCellValue("IN");
									}
									else 
									{
										row.createCell((short) cellno1++).setCellValue("");
									}
								}
								row.createCell((short) cellno1++).setCellValue("");//ExportDuty
								//row.createCell((short)cellno1++).setCellValue(""+bu_statCode);//Pos - place of supply
								row.createCell((short) cellno1++).setCellValue("" + plant_statCode);//Pos - place of supply //WILL BE CUSTOMER PLANT STATE AS DISCUSSED BY JAYASRI MAM WITH VIJAY ON 20250725
								if (sys_or_ff_inv[i].equals("SFA") || sys_or_ff_inv[i].equals("GA")) //DocumentValue //net amount in INR
								{
									row.createCell((short) cellno1++).setCellValue(total);//Itemamt
								} 
								else 
								{
									row.createCell((short) cellno1++).setCellValue("" + net_amt);//ItemTotal //net amt
								}
								//row.createCell((short) cellno1++).setCellValue("" + net_amt);
								row.createCell((short) cellno1++).setCellValue("");//DocumentValueInForeignCurrency
								row.createCell((short) cellno1++).setCellValue("");//RoundOffAmount
								row.createCell((short) cellno1++).setCellValue("");//DifferentialPercentage
								row.createCell((short) cellno1++).setCellValue("N");//ReverseCharge
								row.createCell((short) cellno1++).setCellValue("N");//ClaimRefund
								row.createCell((short) cellno1++).setCellValue("N");//UnderIgstAct
								row.createCell((short) cellno1++).setCellValue("N");//RefundEligibility
								row.createCell((short) cellno1++).setCellValue("");//ECommerceGstin
								row.createCell((short) cellno1++).setCellValue("");//TDSGSTIN
								row.createCell((short) cellno1++).setCellValue("");//PnrOrUniqueNumber
								row.createCell((short) cellno1++).setCellValue("");//AvailProvisionalItc
								row.createCell((short) cellno1++).setCellValue("");//OriginalGstin
								row.createCell((short) cellno1++).setCellValue("");//OriginalStateCode
								row.createCell((short) cellno1++).setCellValue("");//OriginalTradeName
								row.createCell((short) cellno1++).setCellValue("");//OriginalDocumentType
								row.createCell((short) cellno1++).setCellValue("");//OriginalDocumentNumber
								row.createCell((short) cellno1++).setCellValue("");//OriginalDocumentDate
								row.createCell((short) cellno1++).setCellValue("");//OriginalReturnPeriod
								row.createCell((short) cellno1++).setCellValue("");//OriginalAmountDeducted
								row.createCell((short) cellno1++).setCellValue("");//OriginalPortCode
								row.createCell((short) cellno1++).setCellValue("");//TransportDateTime
								row.createCell((short) cellno1++).setCellValue("");//TransporterId
								row.createCell((short) cellno1++).setCellValue("");//TransporterName
								row.createCell((short) cellno1++).setCellValue("");//TransportMode
								row.createCell((short) cellno1++).setCellValue("");//Distance
								row.createCell((short) cellno1++).setCellValue("");//TransportDocumentNumber
								row.createCell((short) cellno1++).setCellValue("");//TransportDocumentDate
								row.createCell((short) cellno1++).setCellValue("");//VehicleNumber
								row.createCell((short) cellno1++).setCellValue("");//VehicleType
								row.createCell((short) cellno1++).setCellValue("");//ToEmailAddresses
								row.createCell((short) cellno1++).setCellValue("");//ToMobileNumbers
								row.createCell((short) cellno1++).setCellValue("");//JWOriginalDocumentNumber
								row.createCell((short) cellno1++).setCellValue("");//JWOriginalDocumentDate
								row.createCell((short) cellno1++).setCellValue("");//JWDocumentNumber
								row.createCell((short) cellno1++).setCellValue("");//JWDocumentDate
								row.createCell((short) cellno1++).setCellValue("");//Custom1
								row.createCell((short) cellno1++).setCellValue("");//Custom2
								row.createCell((short) cellno1++).setCellValue("");//Custom3
								row.createCell((short) cellno1++).setCellValue("");//Custom4
								row.createCell((short) cellno1++).setCellValue("");//Custom5
								row.createCell((short) cellno1++).setCellValue("");//Custom6
								row.createCell((short) cellno1++).setCellValue("");//Custom7
								row.createCell((short) cellno1++).setCellValue("");//Custom8
								row.createCell((short) cellno1++).setCellValue("");//Custom9
								row.createCell((short) cellno1++).setCellValue("");//Custom10
								row.createCell((short) cellno1++).setCellValue("" + serial_no);//SerialNumber
								row.createCell((short) cellno1++).setCellValue(flag);//IsService //IF service inv then Y else N
								if (sys_or_ff_inv[i].equals("SFA")) {
									row.createCell((short) cellno1++).setCellValue(hsn_code);//Hsn
								} else {
									row.createCell((short) cellno1++).setCellValue(sac_no);//LTCORA SAC NO
								}
								row.createCell((short) cellno1++).setCellValue("");//ProductCode
								if (sys_or_ff_inv[i].equals("SFA")) {
									row.createCell((short) cellno1++).setCellValue(item_desc);//ItemDescription
								} else {
									row.createCell((short) cellno1++).setCellValue(sac_desc);//ItemName
								}
								row.createCell((short) cellno1++).setCellValue(item_desc);//ItemDescription
								row.createCell((short) cellno1++).setCellValue("");//NatureOfJWDone
								row.createCell((short) cellno1++).setCellValue("");//Barcode
								row.createCell((short) cellno1++).setCellValue("OTH");//Uqc
								row.createCell((short) cellno1++).setCellValue("" + qty);//Quantity
								row.createCell((short) cellno1++).setCellValue("");//FreeQuantity
								row.createCell((short) cellno1++).setCellValue("");//LossUnitOfMeasure
								row.createCell((short) cellno1++).setCellValue("");//LossTotalQuantity
								row.createCell((short) cellno1++).setCellValue("" + tax);//Rate //TAX RATE
								row.createCell((short) cellno1++).setCellValue("");//CessRate
								row.createCell((short) cellno1++).setCellValue("");//StateCessRate
								row.createCell((short) cellno1++).setCellValue("");//CessNonAdvaloremRate
								row.createCell((short) cellno1++).setCellValue("" + price);//PricePerQuantity
								row.createCell((short) cellno1++).setCellValue("");//DiscountAmount
								row.createCell((short) cellno1++).setCellValue("" + gross_amt);//GrossAmount
								row.createCell((short) cellno1++).setCellValue("");//OtherCharges
								row.createCell((short) cellno1++).setCellValue("" + gross_amt);//TaxableValue
								row.createCell((short) cellno1++).setCellValue("");//PreTaxValue
								row.createCell((short) cellno1++).setCellValue("" + igst_amt);//IgstAmount
								row.createCell((short) cellno1++).setCellValue("" + cgst_amt);//CgstAmount
								row.createCell((short) cellno1++).setCellValue("" + sgst_amt);//SgstAmount
								row.createCell((short) cellno1++).setCellValue("");//CessAmount
								row.createCell((short) cellno1++).setCellValue("");//StateCessAmount
								row.createCell((short) cellno1++).setCellValue("");//StateCessNonAdvaloremAmount
								row.createCell((short) cellno1++).setCellValue("");//CessNonAdvaloremAmount
								row.createCell((short) cellno1++).setCellValue("");//OrderLineReference
								row.createCell((short) cellno1++).setCellValue("");//OriginCountry
								if (sys_or_ff_inv[i].equals("SFA") || sys_or_ff_inv[i].equals("GA")) 
								{
									row.createCell((short) cellno1++).setCellValue(total);//Itemamt
								} 
								else 
								{
									row.createCell((short) cellno1++).setCellValue("" + net_amt);//ItemTotal //net amt
								}
								row.createCell((short) cellno1++).setCellValue("");//ItemAttributeDetails
								row.createCell((short) cellno1++).setCellValue("");//TaxType
								row.createCell((short) cellno1++).setCellValue("");//BatchNameNumber
								row.createCell((short) cellno1++).setCellValue("");//BatchExpiryDate
								row.createCell((short) cellno1++).setCellValue("");//WarrantyDate
								row.createCell((short) cellno1++).setCellValue("");//ItcEligibility
								row.createCell((short) cellno1++).setCellValue("");//ItcIgstAmount
								row.createCell((short) cellno1++).setCellValue("");//ItcCgstAmount
								row.createCell((short) cellno1++).setCellValue("");//ItcSgstAmount
								row.createCell((short) cellno1++).setCellValue("");//ItcCessAmount
								row.createCell((short) cellno1++).setCellValue("");//CustomItem1
								row.createCell((short) cellno1++).setCellValue("");//CustomItem2
								row.createCell((short) cellno1++).setCellValue("");//CustomItem3
								row.createCell((short) cellno1++).setCellValue("");//CustomItem4
								row.createCell((short) cellno1++).setCellValue("");//CustomItem5
								row.createCell((short) cellno1++).setCellValue("");//CustomItem6
								row.createCell((short) cellno1++).setCellValue("");//CustomItem7
								row.createCell((short) cellno1++).setCellValue("");//CustomItem8
								row.createCell((short) cellno1++).setCellValue("");//CustomItem9
								row.createCell((short) cellno1++).setCellValue("");//CustomItem10
							}
		    				else 
		    				{
		    					if (invoice_no.length==1) 
		    					{
									serial_no = 0;
								}
		    				}
				            
		    			}
		    			rset2.close();
		    			stmt2.close();  
		    			
		    			if (!sys_or_ff_inv[i].equals("SFA") && !sys_or_ff_inv[i].equals("GA")) {
		    				serial_no++;
							k = k + 1;
							//serial_no=serial_no+1;
							int row_no = k;
							Row row = sheet.createRow((short) row_no);
							int cellno1 = 0;
							row.createCell((short) cellno1++).setCellValue("" + bu_gstin_no);//LocationGstin
							row.createCell((short) cellno1++).setCellValue("" + company_nm);//LocationName
							row.createCell((short) cellno1++).setCellValue("" + ReturnPeriod);//ReturnPeriod
							row.createCell((short) cellno1++).setCellValue("");//LiabilityDischargeReturnPeriod
							row.createCell((short) cellno1++).setCellValue("");//ITCClaimReturnPeriod
							row.createCell((short) cellno1++).setCellValue("EINV");//Purpose
							row.createCell((short) cellno1++).setCellValue("");//AutoPushOrGenerate
							row.createCell((short) cellno1++).setCellValue("S");//SupplyType //supply type S for sales and P for purchase 
							row.createCell((short) cellno1++).setCellValue("");//Irn
							row.createCell((short) cellno1++).setCellValue(documentType);//DocumentType
							row.createCell((short) cellno1++).setCellValue("" + TransactionType);//TransactionType
							row.createCell((short) cellno1++).setCellValue("");//TransactionNature
							row.createCell((short) cellno1++).setCellValue("");//TransactionTypeDescription
							row.createCell((short) cellno1++).setCellValue("");//TaxpayerType
							row.createCell((short) cellno1++).setCellValue("" + invoice_no[i]);//DocumentNumber
							row.createCell((short) cellno1++).setCellValue("");//DocumentSeriesCode
							row.createCell((short) cellno1++).setCellValue("" + inv_dt);//DocumentDate
							row.createCell((short) cellno1++).setCellValue("" + bu_gstin_no);//BillFromGstin
							row.createCell((short) cellno1++).setCellValue("" + company_nm);//BillFromLegalName
							row.createCell((short) cellno1++).setCellValue("");//BillFromTradeName
							row.createCell((short) cellno1++).setCellValue("");//BillFromVendorCode
							row.createCell((short) cellno1++).setCellValue("" + bu_plantAddress);//BillFromAddress1
							row.createCell((short) cellno1++).setCellValue("");//BillFromAddress2
							row.createCell((short) cellno1++).setCellValue("" + bu_plantCity);//BillFromCity
							row.createCell((short) cellno1++).setCellValue("" + bu_statCode);//BillFromStateCode
							row.createCell((short) cellno1++).setCellValue("" + bu_plantPin);//BillFromPincode
							row.createCell((short) cellno1++).setCellValue("");//BillFromPhone
							row.createCell((short) cellno1++).setCellValue("");//BillFromEmail
							row.createCell((short) cellno1++).setCellValue("");//DispatchFromGstin
							row.createCell((short) cellno1++).setCellValue("");//DispatchFromTradeName
							row.createCell((short) cellno1++).setCellValue("");//DispatchFromVendorCode
							row.createCell((short) cellno1++).setCellValue("");//DispatchFromAddress1
							row.createCell((short) cellno1++).setCellValue("");//DispatchFromAddress2
							row.createCell((short) cellno1++).setCellValue("");//DispatchFromCity
							row.createCell((short) cellno1++).setCellValue("");//DispatchFromStateCode
							row.createCell((short) cellno1++).setCellValue("");//DispatchFromPincode
							row.createCell((short) cellno1++).setCellValue("" + plant_gstin_no);//BillToGstin
							row.createCell((short) cellno1++).setCellValue("" + countpty_nm);//BillToLegalName
							row.createCell((short) cellno1++).setCellValue("");//BillToTradeName
							row.createCell((short) cellno1++).setCellValue("");//BillToVendorCode
							row.createCell((short) cellno1++).setCellValue("" + plantAddress);//BillToAddress1
							row.createCell((short) cellno1++).setCellValue("");//BillToAddress2
							row.createCell((short) cellno1++).setCellValue("" + plantCity);//BillToCity
							row.createCell((short) cellno1++).setCellValue("" + plant_statCode);//BillToStateCode
							row.createCell((short) cellno1++).setCellValue("" + plantPin);//BillToPincode
							row.createCell((short) cellno1++).setCellValue("");//BillToPhone
							row.createCell((short) cellno1++).setCellValue("");//BillToEmail
							row.createCell((short) cellno1++).setCellValue("");//ShipToGstin
							row.createCell((short) cellno1++).setCellValue("");//ShipToLegalName
							row.createCell((short) cellno1++).setCellValue("");//ShipToTradeName
							row.createCell((short) cellno1++).setCellValue("");//ShipToVendorCode
							row.createCell((short) cellno1++).setCellValue("");//ShipToAddress1
							row.createCell((short) cellno1++).setCellValue("");//ShipToAddress2
							row.createCell((short) cellno1++).setCellValue("");//ShipToCity
							row.createCell((short) cellno1++).setCellValue("");//ShipToStateCode
							row.createCell((short) cellno1++).setCellValue("");//ShipToPincode
							row.createCell((short) cellno1++).setCellValue("");//PaymentType
							row.createCell((short) cellno1++).setCellValue("");//PaymentMode
							row.createCell((short) cellno1++).setCellValue("");//PaymentAmount
							row.createCell((short) cellno1++).setCellValue("");//AdvancePaidAmount
							row.createCell((short) cellno1++).setCellValue("");//PaymentDate
							row.createCell((short) cellno1++).setCellValue("");//PaymentRemarks
							row.createCell((short) cellno1++).setCellValue("");//PaymentTerms
							row.createCell((short) cellno1++).setCellValue("");//PaymentInstruction
							row.createCell((short) cellno1++).setCellValue("");//PayeeName
							row.createCell((short) cellno1++).setCellValue("");//PayeeAccountNumber
							row.createCell((short) cellno1++).setCellValue("");//PaymentAmountDue
							row.createCell((short) cellno1++).setCellValue("");//Ifsc
							row.createCell((short) cellno1++).setCellValue("");//CreditTransfer
							row.createCell((short) cellno1++).setCellValue("");//DirectDebit
							row.createCell((short) cellno1++).setCellValue("");//CreditDays
							row.createCell((short) cellno1++).setCellValue("");//CreditAvailedDate
							row.createCell((short) cellno1++).setCellValue("");//CreditReversalDate
							row.createCell((short) cellno1++).setCellValue("");//RefDocumentRemarks
							row.createCell((short) cellno1++).setCellValue("");//RefDocumentPeriodStartDate
							row.createCell((short) cellno1++).setCellValue("");//RefDocumentPeriodEndDate
							row.createCell((short) cellno1++).setCellValue("");//RefPrecedingDocumentDetails
							row.createCell((short) cellno1++).setCellValue("");//RefContractDetails
							row.createCell((short) cellno1++).setCellValue("");//AdditionalSupportingDocumentDetails
							row.createCell((short) cellno1++).setCellValue("");//BillNumber
							row.createCell((short) cellno1++).setCellValue("");//BillDate
							row.createCell((short) cellno1++).setCellValue("");//PortCode
							if (sys_or_ff_inv[i].equals("HSA")) 
							{
								row.createCell((short) cellno1++).setCellValue("USD");//DocumentCurrencyCode
							} 
							else 
							{
								if(TransactionType.equals("B2C"))
								{
									row.createCell((short) cellno1++).setCellValue("INR");
								}
								else 
								{
									row.createCell((short) cellno1++).setCellValue("");
								}
							}
							if (sys_or_ff_inv[i].equals("HSA")) 
							{
								row.createCell((short) cellno1++).setCellValue("IN");//DestinationCountry
							} 
							else 
							{
								if(TransactionType.equals("B2C"))
								{
									row.createCell((short) cellno1++).setCellValue("IN");
								}
								else 
								{
									row.createCell((short) cellno1++).setCellValue("");
								}
							}
							row.createCell((short) cellno1++).setCellValue("");//ExportDuty
							//row.createCell((short)cellno1++).setCellValue(""+bu_statCode);//Pos - place of supply
							row.createCell((short) cellno1++).setCellValue("" + plant_statCode);//Pos - place of supply //WILL BE CUSTOMER PLANT STATE AS DISCUSSED BY JAYASRI MAM WITH VIJAY ON 20250725
							row.createCell((short) cellno1++).setCellValue("" + net_amt);//DocumentValue //net amount in INR
							row.createCell((short) cellno1++).setCellValue("");//DocumentValueInForeignCurrency
							row.createCell((short) cellno1++).setCellValue("");//RoundOffAmount
							row.createCell((short) cellno1++).setCellValue("");//DifferentialPercentage
							row.createCell((short) cellno1++).setCellValue("N");//ReverseCharge
							row.createCell((short) cellno1++).setCellValue("N");//ClaimRefund
							row.createCell((short) cellno1++).setCellValue("N");//UnderIgstAct
							row.createCell((short) cellno1++).setCellValue("N");//RefundEligibility
							row.createCell((short) cellno1++).setCellValue("");//ECommerceGstin
							row.createCell((short) cellno1++).setCellValue("");//TDSGSTIN
							row.createCell((short) cellno1++).setCellValue("");//PnrOrUniqueNumber
							row.createCell((short) cellno1++).setCellValue("");//AvailProvisionalItc
							row.createCell((short) cellno1++).setCellValue("");//OriginalGstin
							row.createCell((short) cellno1++).setCellValue("");//OriginalStateCode
							row.createCell((short) cellno1++).setCellValue("");//OriginalTradeName
							row.createCell((short) cellno1++).setCellValue("");//OriginalDocumentType
							row.createCell((short) cellno1++).setCellValue("");//OriginalDocumentNumber
							row.createCell((short) cellno1++).setCellValue("");//OriginalDocumentDate
							row.createCell((short) cellno1++).setCellValue("");//OriginalReturnPeriod
							row.createCell((short) cellno1++).setCellValue("");//OriginalAmountDeducted
							row.createCell((short) cellno1++).setCellValue("");//OriginalPortCode
							row.createCell((short) cellno1++).setCellValue("");//TransportDateTime
							row.createCell((short) cellno1++).setCellValue("");//TransporterId
							row.createCell((short) cellno1++).setCellValue("");//TransporterName
							row.createCell((short) cellno1++).setCellValue("");//TransportMode
							row.createCell((short) cellno1++).setCellValue("");//Distance
							row.createCell((short) cellno1++).setCellValue("");//TransportDocumentNumber
							row.createCell((short) cellno1++).setCellValue("");//TransportDocumentDate
							row.createCell((short) cellno1++).setCellValue("");//VehicleNumber
							row.createCell((short) cellno1++).setCellValue("");//VehicleType
							row.createCell((short) cellno1++).setCellValue("");//ToEmailAddresses
							row.createCell((short) cellno1++).setCellValue("");//ToMobileNumbers
							row.createCell((short) cellno1++).setCellValue("");//JWOriginalDocumentNumber
							row.createCell((short) cellno1++).setCellValue("");//JWOriginalDocumentDate
							row.createCell((short) cellno1++).setCellValue("");//JWDocumentNumber
							row.createCell((short) cellno1++).setCellValue("");//JWDocumentDate
							row.createCell((short) cellno1++).setCellValue("");//Custom1
							row.createCell((short) cellno1++).setCellValue("");//Custom2
							row.createCell((short) cellno1++).setCellValue("");//Custom3
							row.createCell((short) cellno1++).setCellValue("");//Custom4
							row.createCell((short) cellno1++).setCellValue("");//Custom5
							row.createCell((short) cellno1++).setCellValue("");//Custom6
							row.createCell((short) cellno1++).setCellValue("");//Custom7
							row.createCell((short) cellno1++).setCellValue("");//Custom8
							row.createCell((short) cellno1++).setCellValue("");//Custom9
							row.createCell((short) cellno1++).setCellValue("");//Custom10
							row.createCell((short) cellno1++).setCellValue("" + serial_no);//SerialNumber
							row.createCell((short) cellno1++).setCellValue("Y");//IsService //IF service inv then Y else N
							if (sys_or_ff_inv[i].equals("SFA")) {
								row.createCell((short) cellno1++).setCellValue(hsn_code);//Hsn
							} else {
								row.createCell((short) cellno1++).setCellValue(sac_no);//LTCORA SAC NO
							}
							row.createCell((short) cellno1++).setCellValue("");//ProductCode
							if (sys_or_ff_inv[i].equals("SFA")) {
								row.createCell((short) cellno1++).setCellValue(item_desc);//ItemDescription
							} else {
								row.createCell((short) cellno1++).setCellValue(sac_desc);//ItemName
							}
							row.createCell((short) cellno1++).setCellValue(item_desc);//ItemDescription
							row.createCell((short) cellno1++).setCellValue("");//NatureOfJWDone
							row.createCell((short) cellno1++).setCellValue("");//Barcode
							row.createCell((short) cellno1++).setCellValue("OTH");//Uqc
							row.createCell((short) cellno1++).setCellValue("" + qty);//Quantity
							row.createCell((short) cellno1++).setCellValue("");//FreeQuantity
							row.createCell((short) cellno1++).setCellValue("");//LossUnitOfMeasure
							row.createCell((short) cellno1++).setCellValue("");//LossTotalQuantity
							row.createCell((short) cellno1++).setCellValue("" + tax);//Rate //TAX RATE
							row.createCell((short) cellno1++).setCellValue("");//CessRate
							row.createCell((short) cellno1++).setCellValue("");//StateCessRate
							row.createCell((short) cellno1++).setCellValue("");//CessNonAdvaloremRate
							row.createCell((short) cellno1++).setCellValue("" + price);//PricePerQuantity
							row.createCell((short) cellno1++).setCellValue("");//DiscountAmount
							row.createCell((short) cellno1++).setCellValue("" + gross_amt);//GrossAmount
							row.createCell((short) cellno1++).setCellValue("");//OtherCharges
							row.createCell((short) cellno1++).setCellValue("" + gross_amt);//TaxableValue
							row.createCell((short) cellno1++).setCellValue("");//PreTaxValue
							row.createCell((short) cellno1++).setCellValue("" + igst_amt);//IgstAmount
							row.createCell((short) cellno1++).setCellValue("" + cgst_amt);//CgstAmount
							row.createCell((short) cellno1++).setCellValue("" + sgst_amt);//SgstAmount
							row.createCell((short) cellno1++).setCellValue("");//CessAmount
							row.createCell((short) cellno1++).setCellValue("");//StateCessAmount
							row.createCell((short) cellno1++).setCellValue("");//StateCessNonAdvaloremAmount
							row.createCell((short) cellno1++).setCellValue("");//CessNonAdvaloremAmount
							row.createCell((short) cellno1++).setCellValue("");//OrderLineReference
							row.createCell((short) cellno1++).setCellValue("");//OriginCountry
							if (sys_or_ff_inv[i].equals("SFA")) {
								row.createCell((short) cellno1++).setCellValue(total);//Itemamt
							} else {
								row.createCell((short) cellno1++).setCellValue("" + net_amt);//ItemTotal //net amt
							}
							row.createCell((short) cellno1++).setCellValue("");//ItemAttributeDetails
							row.createCell((short) cellno1++).setCellValue("");//TaxType
							row.createCell((short) cellno1++).setCellValue("");//BatchNameNumber
							row.createCell((short) cellno1++).setCellValue("");//BatchExpiryDate
							row.createCell((short) cellno1++).setCellValue("");//WarrantyDate
							row.createCell((short) cellno1++).setCellValue("");//ItcEligibility
							row.createCell((short) cellno1++).setCellValue("");//ItcIgstAmount
							row.createCell((short) cellno1++).setCellValue("");//ItcCgstAmount
							row.createCell((short) cellno1++).setCellValue("");//ItcSgstAmount
							row.createCell((short) cellno1++).setCellValue("");//ItcCessAmount
							row.createCell((short) cellno1++).setCellValue("");//CustomItem1
							row.createCell((short) cellno1++).setCellValue("");//CustomItem2
							row.createCell((short) cellno1++).setCellValue("");//CustomItem3
							row.createCell((short) cellno1++).setCellValue("");//CustomItem4
							row.createCell((short) cellno1++).setCellValue("");//CustomItem5
							row.createCell((short) cellno1++).setCellValue("");//CustomItem6
							row.createCell((short) cellno1++).setCellValue("");//CustomItem7
							row.createCell((short) cellno1++).setCellValue("");//CustomItem8
							row.createCell((short) cellno1++).setCellValue("");//CustomItem9
							row.createCell((short) cellno1++).setCellValue("");//CustomItem10
						}
	    			}
	    			rset.close();
	    			stmt.close();
	            }
	            
	            String realPath = request.getServletContext().getRealPath("");
	            String canonicalPath_files = new File(file_path).getCanonicalPath();
	            
	            //System.out.println("realPath = "+realPath);
	            //System.out.println("canonicalPath_files = "+canonicalPath_files);
		        if(!canonicalPath_files.startsWith(realPath))
		        {
		        	throw new IOException("Entry is outside of the target directory!");
		        }
		        else
		        {
		            try (FileOutputStream fileOut = new FileOutputStream(file_path))
		            {
		            	workbook.write(fileOut);
		            }
		        }
	            
	            if(k>0)
	            {
	            	msg = "Successful! - "+fileName+" Created for IRN Generation!";
					msg_type="S";
	            }
	            else
	            {
	            	msg = "Failed! - No Invoice found for IRN Generation!";
					msg_type="E";
	            }
			}
			else
			{
				msg = "Failed! - No Invoice found for IRN Generation!";
				msg_type="E";
			}
			
			url = "../extn_interface/frm_generate_oth_irn.jsp?month="+month+"&year="+year+"&invoice_type="+invoice_type+
					"&msg="+msg+"&msg_type="+msg_type+"&file_nm="+fileName+commonUrl_pra;
			
		}
		catch(Exception e)
		{
			//dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! - IRN Generation Failed";
		}
		finally 
		{
			if(workbook != null)
			{
				try 
				{
					workbook.close();
					workbook=null;
				}
				catch(IOException  e)
				{
					new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				}
			}
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void UploadResponseOthIRNExcel(HttpServletRequest request) throws SQLException 
	{
		String function_nm="UploadResponseOthIRNExcel()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String month = request.getParameter("month")==null?"":request.getParameter("month");
			String year = request.getParameter("year")==null?"":request.getParameter("year");
			String invoice_type=request.getParameter("invoice_type")==null?"":request.getParameter("invoice_type");
			
			String file_name="";
	        String fileName="";
	        
	        int success=0;
	        int failed=0;
	        
	        for (Part part : request.getParts()) 
	        {
	        	fileName = extractFileName(part);
	        	// refines the fileName in case it is an absolute path
			    fileName = new File(fileName).getName();
			    if(!fileName.equals("") && fileName.endsWith(".xlsx") )
			    {
			    	file_name=fileName;
			    	
			    	String work_data=utilBean.getAutomationKeyDetail(dbcon, "WORK_DATA_"+comp_cd);
			    	String file_path=work_data+File.separator+"IRN_EXCEL"+File.separator+"Oth_Response";
					
					if(!new File(file_path).exists())
					{
						new File(file_path).mkdirs();
					}
					
					file_path=file_path+File.separator+fileName;
					
					part.write(file_path);
					try(FileInputStream file = new FileInputStream(new File(file_path));
						     XSSFWorkbook workbook = new XSSFWorkbook(file))
					{
						XSSFSheet sheet = workbook.getSheetAt(0);
						Iterator<Row> rowIterator = sheet.iterator();
						while (rowIterator.hasNext()) 
						{
							Row row = rowIterator.next();
							//For each row, iterate through all the columns
							Iterator<Cell> cellIterator = row.cellIterator();
							int row_num=row.getRowNum();
							String inv_no="",irn_no="",SignedQRCode="",gstin_no="",QRCode="";
							if(row_num>0)
							{
								while (cellIterator.hasNext()) 
								{
									Cell cell = cellIterator.next();
									int rw_ind=cell.getRowIndex();
									
									if(cell.getColumnIndex()==2 || cell.getColumnIndex()==7 || cell.getColumnIndex()==11 || cell.getColumnIndex()==12 || cell.getColumnIndex()==0 )
									{
										if(cell.getColumnIndex()==2)
										{
											inv_no=cell.getStringCellValue();//DocumentNumber
										} 
										else if(cell.getColumnIndex()==7)//Irn
										{
											irn_no=cell.getStringCellValue();
										}
										else if(cell.getColumnIndex()==11)//SignedQRCode
										{
											SignedQRCode=cell.getStringCellValue();
										}
										else if(cell.getColumnIndex()==0)//LocationGstin
										{
											gstin_no=cell.getStringCellValue();
										}
										else if(cell.getColumnIndex()==12)//QRCode
										{
											QRCode=cell.getStringCellValue();
										}
									}			
								}
								if(!irn_no.equals("") && (!inv_no.equals("")) && (!SignedQRCode.equals("")))
								{
									queryString="SELECT COMPANY_CD "
					    					+ "FROM FMS_OTH_INVOICE_DTL "
					    					+ "WHERE INVOICE_NO=? ";
					    			stmt=dbcon.prepareStatement(queryString);
					    			stmt.setString(1, inv_no);
					    			rset=stmt.executeQuery();
					    			if(rset.next())
					    			{
					    				String companyCd = rset.getString(1)==null?"":rset.getString(1);
					    				
					    				if(!companyCd.equals(""))
					    				{
					    					int count_irn=0;
					    					query1="SELECT COUNT(*) "
					    							+ "FROM FMS_OTH_INV_IRN_DTL "
					    							+ "WHERE COMPANY_CD=? AND INVOICE_NO=?";
											stmt1 = dbcon.prepareStatement(query1);
					           	    		stmt1.setString(1, companyCd); 
					           	    		stmt1.setString(2, inv_no); 
											rset1=stmt1.executeQuery();
											if(rset1.next())
											{
												count_irn=rset1.getInt(1);
											}
											rset1.close();
											stmt1.close();
											
											if(count_irn==0)
											{
												query1="INSERT INTO FMS_OTH_INV_IRN_DTL (COMPANY_CD,INVOICE_NO,IRN_NO,XLS_FILE_NM,SIGN_QR_CODE,ENT_BY,ENT_DT)"
														+ " VALUES (?,?,?,?,?,?,SYSDATE)";
												stmt1 = dbcon.prepareStatement(query1);
						           	    		stmt1.setString(1, companyCd); 
						           	    		stmt1.setString(2, inv_no); 
						           	    		stmt1.setString(3, irn_no);
						           	    		stmt1.setString(4, fileName); 
						           	    		stmt1.setString(5, SignedQRCode); 
						           	    		stmt1.setString(6, emp_cd); 
												stmt1.executeUpdate();
												stmt1.close();
												dbcon.commit();
												
												success++;
											}
											else
											{
												failed++;
											}
					    				}
					    				else
					    				{
					    					failed++;
					    				}
					    			}
					    			rset.close();
					    			stmt.close();
								}
								else
								{
									failed++;
								}
							}
						}
					}
			    }
	        }
	        
	        if(!file_name.equals(""))
	        {
	        	msg = "Successful! - "+file_name+" IRN Response Imported Successfully ("+success+" Success and "+failed+" Failed)!";
				msg_type="S";
	        }
	        else
	        {
	        	msg = "Failed! - IRN Response file not found for Upload!";
				msg_type="E";
	        }
	        
	        url = "../extn_interface/frm_generate_oth_irn.jsp?month="+month+"&year="+year+"&invoice_type="+invoice_type+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);			
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error In Exception! - IRN Response Upload Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	
	private String extractFileName(Part part) 
    {		
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        //System.out.println("items*******"+items.length);
        String filenm = "";
        for (String s : items) 
        {
            if (s.trim().startsWith("filename") || s.trim().startsWith("meet_file")) 
            {
           	//System.out.println("s*****"+s);
                filenm = s.substring(s.indexOf("=") + 2, s.length()-1);
            }       
        }
        return filenm;
    }
}
