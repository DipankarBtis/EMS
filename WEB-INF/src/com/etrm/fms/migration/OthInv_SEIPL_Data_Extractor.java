
package com.etrm.fms.migration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.apache.poi.ss.formula.ptg.ValueOperatorPtg;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.SystemErrorLogger;
import com.itextpdf.text.log.SysoCounter;

public class OthInv_SEIPL_Data_Extractor {

	/*	public static void main(String[] args) {
			OthInv_Excel_Extractor ex = new OthInv_Excel_Extractor();
			ex.getmail_list();
			ex.getCustomerTraderList();
			ex.makeDirectory();
			ex.init();
		}
	
	}
	
	class OthInv_Excel_Extractor {*/

	String db_src_file_name = "OthInv_SEIPL_Data_Extractor.java";
	String function_nm = "";
	
	String migration_setup_dir = "";
	
	String sysDateTime = "";
	
	String fname = "";
	String fname_error = "";
	String fname1 = "";

	DataMigration_Logger logger = new DataMigration_Logger();
	Migration_Plants_Exceptions mpe =new Migration_Plants_Exceptions();

	static DateUtil utilDate = new DateUtil();
	NumberFormat nf = new DecimalFormat("###########0.00");

	String queryString = "", queryString1 = "";
	Connection conn;
	ResultSet rset, rset1;
	PreparedStatement stmt, stmt1;
	File file1 = null;

	String dbline = "", username = "", encrypted = "", password = "";
	String columns = "", filename = "", value = "";
	
	String checked_values = "", msg = "", msg_type = "";
	
	String delta_FromDt = null;
	String delta_ToDt = null;
	String start_end_dt = null;
	
	String abbr = "", cd = "", seq_no = "", entity = "", eff_dt = "", address_type = "", tax_code = "", tax_nm = "",name="";
	
	String dir_flag = "N";

	int nrow = 0;
	int count = 0;

	XSSFWorkbook workbook = null;
	XSSFSheet spreadsheet = null;
	XSSFRow row;
	Cell cell;
	
	FileOutputStream fileOut = null;

	public void init() 
	{

        function_nm="init()";
		try 
		{
			
			getmail_list();
			makeDirectory();
			
			fname = "DataLogs/Extractor/OthInv_SEIPL_Data_Extractor(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Extractor/OthInv_SEIPL_Data_Extractor_Error(log)"+sysDateTime+".csv";
			
			fname = migration_setup_dir + fname;
			fname_error = migration_setup_dir + fname_error;
			
			fname1 = "DataLogs/Script_Status(log).csv";	
			fname1 = migration_setup_dir + fname1;

			Preferences preferences =  Preferences.userRoot().node("/processFlag");

			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("FMS8 DBLINE:"+dbline);
			conn = DriverManager.getConnection(dbline, username, password);
			
			
			if (conn != null && dir_flag.equals("Y") && !checked_values.equals("")) 
			{

	    		preferences.put("Flag", "0");
				
				if (checked_values.contains("FMS_OTH_ENTITY_MST")) 
				{
					FMS_OTH_ENTITY_MST();
				}
				if (checked_values.contains("FMS_OTH_ENTITY_ADDR_MST")) 
				{
					FMS_OTH_ENTITY_ADDR_MST();
				}
				if(checked_values.contains("FMS_OTH_INVOICE_MST")) 
				{
					FMS_OTH_INVOICE_MST();
				}
				if(checked_values.contains("FMS_OTH_INVOICE_DTL")) 
				{
					FMS_OTH_INVOICE_DTL();
				}
				if(checked_values.contains("FMS_OTH_INV_CRDR_REF")) 
				{
					FMS_OTH_INV_CRDR_REF();
				}
				if(checked_values.contains("FMS_OTH_INV_FILE_DTL"))
				{
					FMS_OTH_INV_FILE_DTL();
				}
				if(checked_values.contains("FMS_OTH_INV_IRN_DTL"))
				{
					FMS_OTH_INV_IRN_DTL();
				}
				
				
	    		preferences.put("Flag", "1");
				conn.close();
				conn = null;
			
			}
			else {
				msg = "No Checkbox was selected. Extraction Terminated.";
				msg_type = "E";
			}

		} catch (Exception e) 
		{
			//LOGGER.log(Level.WARNING, "Error in OthInv_SEIPL_Data_Extractor.java -> OthInv_Excel_Extractor -> init()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
		} 
		finally 
		{
			try 
			{
				if (rset != null) 
				{
					try 
					{
						rset.close();
					} catch (SQLException e) 
					{
						//LOGGER.log(Level.WARNING, "Error in OthInv_SEIPL_Data_Extractor.java -> OthInv_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (rset1 != null) 
				{
					try {
						rset1.close();
					} catch (SQLException e) 
					{
						//LOGGER.log(Level.WARNING, "Error in OthInv_SEIPL_Data_Extractor.java -> OthInv_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt != null) 
				{
					try 
					{
						stmt.close();
					} catch (SQLException e) 
					{
						//LOGGER.log(Level.WARNING, "Error in OthInv_SEIPL_Data_Extractor.java -> OthInv_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt1 != null) 
				{
					try 
					{
						stmt1.close();
					} catch (SQLException e) 
					{
						//LOGGER.log(Level.WARNING, "Error in OthInv_SEIPL_Data_Extractor.java -> OthInv_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (conn != null) 
				{
					try {
						conn.close();
					} catch (SQLException e) 
					{
						//LOGGER.log(Level.WARNING, "Error in OthInv_SEIPL_Data_Extractor.java -> OthInv_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				
			} catch (Exception e) 
			{
				//LOGGER.log(Level.WARNING, "Error in OthInv_SEIPL_Data_Extractor.java -> OthInv_Excel_Extractor -> init()  ", e);
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				
				msg = "One of the Functions faced an Error. Extraction Terminated.";
				msg_type = "E";
			}
		}

	}

	
	public void FMS_OTH_ENTITY_MST() throws SQLException,IOException 
	{
		function_nm="FMS_OTH_ENTITY_MST()";
		try
		{
			System.out.println("<<START>><<FMS_OTH_ENTITY_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_OTH_ENTITY_MST>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "ENTITY_CD,ENTITY_TYPE,EFF_DT,ENTITY_NAME,ENTITY_ABBR,GST_TIN_NO,GST_TIN_DT,CST_TIN_NO,CST_TIN_DT,PAN_NO,PAN_ISSUE_DT,TAN_NO,TAN_ISSUE_DT,"
					+ "ADDL_NO,ADDL_ISSUE_DT,GSTIN_NO,GSTIN_DT,WEB_ADDR,NOTES,ACTIVE_FLAG,BUSINESS_FLAG,PAYEE_ACCOUNT_NO,IFSC,PAYEE_NM,ENT_BY,ENT_DT,"
					+ "MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;

			String flag = "";
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) 
			{
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = "SELECT NULL,'S',TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.SUPPLIER_NAME,A.SUPPLIER_ABBR,A.GST_TIN_NO,TO_CHAR(A.GST_TIN_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "A.CST_TIN_NO,TO_CHAR(A.CST_TIN_DT,'DD/MM/YYYY HH24:MI:SS'),A.PAN_NO,TO_CHAR(A.PAN_ISSUE_DT,'DD/MM/YYYY HH24:MI:SS'),A.TAN_NO,"
					+ "TO_CHAR(A.TAN_ISSUE_DT,'DD/MM/YYYY HH24:MI:SS'),A.ADDL_NO,TO_CHAR(A.ADDL_ISSUE_DT,'DD/MM/YYYY HH24:MI:SS'),A.GSTIN_NO,"
					+ "TO_CHAR(A.GSTIN_DT,'DD/MM/YYYY HH24:MI:SS'),A.WEB_ADDR,A.NOTES,'Y',NULL,NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "NULL,NULL,'2',NULL "
					+ "FROM FMS7_SUPPLIER_MST A WHERE A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD) "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.SUPPLIER_CD DESC";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "ENTITY_CD,ENTITY_CD,EFF_DT,TIMESTAMP", conn);
			
			int num = 1; 
			while(rset.next())
			{
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if (i==0) 
					{
						cd=num+"";
						cell.setCellValue("'" + num + "'");
						num++;
					}
					else if (i == 2) 
					{
						eff_dt=value;
						cell.setCellValue("'" + value + "'");
					}
					else
					{
						cell.setCellValue("'" + value + "'");
					}
				}
				
				count++;
				logger.data(fname, ( cd + "," + "S" + "," + eff_dt + "," ), conn, "");
				
			}
			stmt.close();
			rset.close();
			
			queryString = "SELECT A.VENDOR_CD,'V',TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.VENDOR_NAME,A.VENDOR_ABBR,A.GST_TIN_NO,TO_CHAR(A.GST_TIN_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "A.CST_TIN_NO,TO_CHAR(A.CST_TIN_DT,'DD/MM/YYYY HH24:MI:SS'),A.PAN_NO,TO_CHAR(A.PAN_ISSUE_DT,'DD/MM/YYYY HH24:MI:SS'),A.TAN_NO,"
					+ "TO_CHAR(A.TAN_ISSUE_DT,'DD/MM/YYYY HH24:MI:SS'),A.ADDL_NO,TO_CHAR(A.ADDL_ISSUE_DT,'DD/MM/YYYY HH24:MI:SS'),A.GSTIN_NO,"
					+ "TO_CHAR(A.GSTIN_DT,'DD/MM/YYYY HH24:MI:SS'),A.WEB_ADDR,A.NOTES,'Y',NULL,A.PAYEE_ACCOUNT_NO,A.IFSC,A.PAYEE_NM,A.EMP_CD,"
					+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,'2',NULL "
					+ "FROM FMS8_VENDOR_MST A WHERE A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS8_VENDOR_MST B WHERE A.VENDOR_CD = B.VENDOR_CD) "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.VENDOR_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "ENTITY_CD,ENTITY_CD,EFF_DT,TIMESTAMP", conn);
			 
			while(rset.next())
			{
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if (i==0) 
					{
						cd=value;
						cell.setCellValue("'" + value + "'");
					}
					else if (i == 2) 
					{
						eff_dt=value;
						cell.setCellValue("'" + value + "'");
					}
					else if(i == 20) 
					{
						if(rset.getString(22) == null || rset.getString(22).equals("")) 
						{
							flag = "B";
						}
						else 
						{
							flag = "C";
						}
						cell.setCellValue("'" + flag + "'");
					}
					else
					{
						cell.setCellValue("'" + value + "'");
					}
				}
				
				count++;
				logger.data(fname, ( cd + "," + "V" + "," + eff_dt + "," ), conn, "");
				
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_OTH_ENTITY_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_OTH_ENTITY_MST>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_OTH_ENTITY_MST>>");
			System.out.println();
				
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch(Exception e){
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}
	
	public void FMS_OTH_ENTITY_ADDR_MST() throws SQLException,IOException 
	{
		function_nm="FMS_OTH_ENTITY_ADDR_MST()";
		try
		{
			System.out.println("<<START>><<FMS_OTH_ENTITY_ADDR_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_OTH_ENTITY_ADDR_MST>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="ENTITY_CD,ENTITY_TYPE,EFF_DT,ADDRESS_TYPE,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,"
					+ "ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;

			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) 
			{
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
		
			queryString = "SELECT A.SUPPLIER_CD,'S',TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.ADDRESS_TYPE,A.ADDR,A.CITY,A.PIN,A.STATE,A.ZONE,A.COUNTRY,A.PHONE,A.MOBILE,"
					+ "A.ALT_PHONE,A.FAX_1,A.FAX_2,A.EMAIL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,'2',NULL   "
					+ "FROM FMS7_SUPPLIER_ADDRESS_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_ADDRESS_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD AND A.ADDRESS_TYPE = B.ADDRESS_TYPE) "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.SUPPLIER_CD DESC";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "ENTITY_CD,ENTITY_TYPE,ADDRESS_TYPE,EFF_DT,TIMESTAMP", conn);
			while(rset.next())
			{
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if (i==0) 
					{
						queryString1 = "SELECT SUPPLIER_ABBR FROM FMS7_SUPPLIER_MST WHERE SUPPLIER_CD = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(1));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						cell.setCellValue("'" + value + "'");
					}
					else if (i == 2) 
					{
						eff_dt=value;
						cell.setCellValue("'" + value + "'");
						
					}
					else if (i == 3) 
					{
						address_type=value;
						cell.setCellValue("'" + value + "'");
					}
					else
					{
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			queryString = "SELECT A.VENDOR_CD,'V',TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.ADDRESS_TYPE,A.ADDR,A.CITY,A.PIN,A.STATE,A.ZONE,A.COUNTRY,A.PHONE,A.MOBILE,"
					+ "A.ALT_PHONE,A.FAX_1,A.FAX_2,A.EMAIL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,'2',NULL  "
					+ "FROM FMS8_VENDOR_ADDRESS_MST A "
					+ "WHERE  A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS8_VENDOR_ADDRESS_MST B WHERE A.VENDOR_CD = B.VENDOR_CD AND A.ADDRESS_TYPE = B.ADDRESS_TYPE) "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.VENDOR_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "ENTITY_CD,ENTITY_TYPE,ADDRESS_TYPE,EFF_DT,TIMESTAMP", conn);
			while(rset.next())
			{
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if (i==0) 
					{
						cd=value;
						cell.setCellValue("'" + value + "'");
					}
					else if (i == 2) 
					{
						eff_dt=value;
						cell.setCellValue("'" + value + "'");
					}
					else if (i == 3) 
					{
						address_type=value;
						cell.setCellValue("'" + value + "'");
					}
					else
					{
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, ( cd + ","+ "V" + "," + address_type + "," + eff_dt + "," ), conn, "");
				
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_OTH_ENTITY_ADDR_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_OTH_ENTITY_ADDR_MST>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_OTH_ENTITY_ADDR_MST>>");
			System.out.println();
				
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch(Exception e)
		{
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_OTH_INVOICE_MST() throws SQLException,IOException 
	{
		function_nm="FMS_OTH_INVOICE_MST()";
		try
		{
			System.out.println("<<START>><<FMS_OTH_INVOICE_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_OTH_INVOICE_MST>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,DUE_DT,PERIOD_START_DT,PERIOD_END_DT,"
					+ "SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT_INR,INVOICE_RAISED_IN,REMARK,REMARK1,"
					+ "REMARK2,REMARK3,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,"
					+ "PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,"
					+ "FIN_SYS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,NET_PAYABLE,INVOICE_ID_SEQ,INVOICE_CATEGORY,VENDOR_TYPE,INV_FLAG,REF_NO,CRITERIA";

			String sup_cd="",pdf_inv="";
			String fname_csv = "", str = "",sale_unit="",remark="",remark1="",remark2="",remark3="",flag="";
			count = 0;

			fname_csv = migration_setup_dir + "EXPORT/FMS_OTH_INVOICE_MST_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			for (int i = 0; i < columns.split(",").length; i++) 
			{
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
		
			logger.checkpoint(fname, "COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,TIMESTAMP", conn);
			
			//Cost_Recharge
			queryString = "SELECT '2',A.SUPPLIER_CD,A.CUSTOMER_CD,'COSTR',A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO,A.NEW_INV_SEQ_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),NULL,A.INV_CUR_FLAG,A.GROSS_AMT_USD,"
					+ "A.EXCHG_RATE_CD,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.EXCHG_RATE_VALUE,A.GROSS_AMT_INR,A.TAX_AMT_INR,'1',B.REMARK,B.REMARK1,B.REMARK2,B.REMARK3,"
					+ "A.CHECKED_FLAG,A.CHECKED_BY,TO_CHAR(A.CHECKED_DT,'DD/MM/YYYY'),A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,TO_CHAR(A.AUTHORIZED_DT,'DD/MM/YYYY'),"
					+ "A.APPROVED_FLAG,A.APPROVED_BY,TO_CHAR(A.APPROVED_DT,'DD/MM/YYYY'),A.PDF_INV_DTL,A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'),"
					+ "A.PRINT_BY_TRI,TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'),A.PRINT_BY_DUP,TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,"
					+ "A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.NET_AMT_INR,A.HLPL_INV_SEQ_NO,'S','V','F',NULL,NULL  "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE A.CONTRACT_TYPE = 'X' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 6)
					{
						if(sup_cd.equals("1"))
						{
							value = "RCL"+value;
						}
						else if(sup_cd.equals("2"))
						{
							value = "RCP"+value;
						}
						str += value + ",";	
					}
					else if(i == 12)
					{
						sale_unit = rset.getString(13);
						if(sale_unit == null)
						{
							value = "1";
						}
						else 
						{
							value = rset.getString(13);
						}
						str += value + ",";
					}
					else if(i == 33)
					{
						pdf_inv = rset.getString(34);
						if(pdf_inv!=null) 
						{
							if(pdf_inv.contains("T")) 
							{
								pdf_inv = "T";
							}
							else if(pdf_inv.contains("D")) 
							{
								pdf_inv = "D";
							}
							else if(pdf_inv.contains("O")) 
							{
								pdf_inv ="O";
							}
							else 
							{
								pdf_inv = null;
							}
						}
						else 
						{
							pdf_inv = null;
						}
						
						str += pdf_inv + ",";
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ rset.getString(2) + "," + rset.getString(3) + "," + "COSTR" + "," + rset.getString(5) + "," + rset.getString(6) + ","), conn, "");
			}
			stmt.close();
			rset.close();
			
			//Cost_recharge_HPPL
			queryString = "SELECT '2',A.SUPPLIER_CD,A.CUSTOMER_CD,'COSTRH',A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO,A.NEW_INV_SEQ_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),NULL,A.INV_CUR_FLAG,A.GROSS_AMT_INR,"
					+ "A.EXCHG_RATE_CD,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.EXCHG_RATE_VALUE,A.GROSS_AMT_INR,A.TAX_AMT_INR,'1',B.REMARK,B.REMARK1,B.REMARK2,B.REMARK3,"
					+ "A.CHECKED_FLAG,A.CHECKED_BY,TO_CHAR(A.CHECKED_DT,'DD/MM/YYYY'),A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,TO_CHAR(A.AUTHORIZED_DT,'DD/MM/YYYY'),"
					+ "A.APPROVED_FLAG,A.APPROVED_BY,TO_CHAR(A.APPROVED_DT,'DD/MM/YYYY'),A.PDF_INV_DTL,A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'),"
					+ "A.PRINT_BY_TRI,TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'),A.PRINT_BY_DUP,TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,"
					+ "A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.NET_AMT_INR,A.HLPL_INV_SEQ_NO,'S','S','F',NULL,NULL  "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE A.CONTRACT_TYPE = 'Y' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 12)
					{
						sale_unit = rset.getString(13);
						if(sale_unit == null)
						{
							value = "1";
						}
						else 
						{
							value = rset.getString(13);
						}
						str += value + ",";
					}
					else if(i == 33)
					{
						pdf_inv = rset.getString(34);
						if(pdf_inv!=null) 
						{
							if(pdf_inv.contains("T")) 
							{
								pdf_inv = "T";
							}
							else if(pdf_inv.contains("D")) 
							{
								pdf_inv = "D";
							}
							else if(pdf_inv.contains("O")) 
							{
								pdf_inv ="O";
							}
							else 
							{
								pdf_inv = null;
							}
						}
						else 
						{
							pdf_inv = null;
						}
						
						str += pdf_inv + ",";
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ rset.getString(2) + "," + rset.getString(3) + "," + "COSTRH" + "," + rset.getString(5) + "," + rset.getString(6) + ","), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			//HPPL Shipping Agent
			queryString = "SELECT '2',A.SUPPLIER_CD,A.CUSTOMER_CD,'HSA',A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO,A.NEW_INV_SEQ_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),B.RATE,'2',A.GROSS_AMT_USD,"
					+ "A.EXCHG_RATE_CD,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.EXCHG_RATE_VALUE,A.GROSS_AMT_USD,A.TAX_AMT_INR,'2',B.REMARK,B.REMARK1,B.REMARK2,B.REMARK3,"
					+ "A.CHECKED_FLAG,A.CHECKED_BY,TO_CHAR(A.CHECKED_DT,'DD/MM/YYYY'),A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,TO_CHAR(A.AUTHORIZED_DT,'DD/MM/YYYY'),"
					+ "A.APPROVED_FLAG,A.APPROVED_BY,TO_CHAR(A.APPROVED_DT,'DD/MM/YYYY'),A.PDF_INV_DTL,A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'),"
					+ "A.PRINT_BY_TRI,TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'),A.PRINT_BY_DUP,TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,"
					+ "A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.NET_AMT_INR,A.HLPL_INV_SEQ_NO,'S','V','F',NULL,NULL  "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE A.CONTRACT_TYPE = '1' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 6)
					{
						if(!value.contains("P"))
						{
							value = value+"/P";
						}
						
						str += value + ",";	
					}
					else if(i == 33)
					{
						pdf_inv = rset.getString(34);
						if(pdf_inv!=null) 
						{
							if(pdf_inv.contains("T")) 
							{
								pdf_inv = "T";
							}
							else if(pdf_inv.contains("D")) 
							{
								pdf_inv = "D";
							}
							else if(pdf_inv.contains("O")) 
							{
								pdf_inv ="O";
							}
							else 
							{
								pdf_inv = null;
							}
						}
						else 
						{
							pdf_inv = null;
						}
						
						str += pdf_inv + ",";
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ rset.getString(2) + "," + rset.getString(3) + "," + "HSA" + "," + rset.getString(5) + "," + rset.getString(6) + ","), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			//PFA Invoice HPPL-SEIPL
			queryString = "SELECT '2',A.SUPPLIER_CD,A.CONTRACT_TYPE,'HS',A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO,A.NEW_INV_SEQ_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),NULL,'2',A.GROSS_AMT_USD,"
					+ "A.EXCHG_RATE_CD,TO_CHAR(A.USER_DEFINED_DAY,'DD/MM/YYYY'),A.EXCHG_RATE_VALUE,A.GROSS_AMT_INR,A.TAX_AMT_INR,'1',NULL,NULL,NULL,NULL,"
					+ "A.CHECKED_FLAG,A.CHECKED_BY,TO_CHAR(A.CHECKED_DT,'DD/MM/YYYY'),A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,TO_CHAR(A.AUTHORIZED_DT,'DD/MM/YYYY'),"
					+ "A.APPROVED_FLAG,A.APPROVED_BY,TO_CHAR(A.APPROVED_DT,'DD/MM/YYYY'),A.PDF_INV_DTL,A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'),"
					+ "A.PRINT_BY_TRI,TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'),A.PRINT_BY_DUP,TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,"
					+ "A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.NET_AMT_INR,A.HLPL_INV_SEQ_NO,'S','S','F',NULL,NULL  "
					+ "FROM FMS7_INVOICE_MST A "
					//+ " FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE A.CONTRACT_TYPE = '2' "
//					+ "AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
//					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.INVOICE_DT = B.EFF_DT "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";remark="";remark1="";remark2="";remark3="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if (i == 2) 
					{
						
						queryString1 = "SELECT CUSTOMER_NM,REMARK,REMARK1,REMARK2,REMARK3 "
								+ "FROM FMS8_OTHER_INVOICE_DTL  "
								+ "WHERE INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
								+ "AND CONTRACT_TYPE = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(6));
						stmt1.setString(2, rset.getString(5));
						stmt1.setString(3, rset.getString(2));
						stmt1.setString(4, rset.getString(3));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
							remark = rset1.getString(2) == null ? "null" : rset1.getString(2);
							remark1 = rset1.getString(3) == null ? "null" : rset1.getString(3);
							remark2 = rset1.getString(4) == null ? "null" : rset1.getString(4);
							remark3 = rset1.getString(5) == null ? "null" : rset1.getString(5);
							
							if(value.equals("Hazira LNG Private Limited"))
							{
								value = "3";
							}
							else 
							{
								value = "2";
							}
						}
						rset1.close();
						stmt1.close();
						
						remark = remark.replaceAll(",", " ");
						remark = remark.replaceAll("\n", " ");
						remark = remark.replaceAll("\r", " ");
						
						remark1 = remark1.replaceAll(",", " ");
						remark1 = remark1.replaceAll("\n", " ");
						remark1 = remark1.replaceAll("\r", " ");
						
						remark2 = remark2.replaceAll(",", " ");
						remark2 = remark2.replaceAll("\n", " ");
						remark2 = remark2.replaceAll("\r", " ");
						
						remark3 = remark3.replaceAll(",", " ");
						remark3 = remark3.replaceAll("\n", " ");
						remark3 = remark3.replaceAll("\r", " ");
						
						str += value + ",";		
					}
					else if(i == 6)
					{
						if(!value.contains("P"))
						{
							value = value+"/P";
						}
						
						str += value + ",";	
					}
					else if(i == 14)
					{
						value = rset.getString(15);
						
						if(value!=null) 
						{
							queryString1="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, value);
							rset1 = stmt1.executeQuery();
							if(rset1.next()) 
							   {	
								   name = rset1.getString(1);
								   name= name.toUpperCase();
								   if (name.contains("CUSTOMS RATE")) 
								   {
										name = "CUSTOM EXCHANGE RATE";
									}
									else if (name.contains("RBI REFERENCE"))
									{
										name = "RBI REFERENCE RATE";
									}
									else if (name.contains("SBI MUMBAI TT AVERAGE")) 
									{
										name = "SBI MUMBAI TT BUY SELL";
									}
									else if (name.contains("SBI TT BUYING")) 
									{
										name = "SBI RATE BUY";
									}
									else if (name.contains("SBI TT SELLING")) 
									{
										name = "SBI RATE SELL";
									}
									else if (name.contains("SBI TT BUY SELL")) 
									{
										name = "SBI RATE BUY SELL";
									}								
							   }					
						   stmt1.close();
						   rset1.close();
						   
						   str += name.trim() + ",";	
						}
						
					}
					else if(i == 20)
					{
						str += remark + ",";	
					}
					else if(i == 21)
					{
						str += remark1 + ",";	
					}
					else if(i == 22)
					{
						str += remark2 + ",";	
					}
					else if(i == 23)
					{
						str += remark3 + ",";	
					}
					
					else if(i == 33)
					{
						pdf_inv = rset.getString(34);
						if(pdf_inv!=null) 
						{
							if(pdf_inv.contains("T")) 
							{
								pdf_inv = "T";
							}
							else if(pdf_inv.contains("D")) 
							{
								pdf_inv = "D";
							}
							else if(pdf_inv.contains("O")) 
							{
								pdf_inv ="O";
							}
							else 
							{
								pdf_inv = null;
							}
						}
						else 
						{
							pdf_inv = null;
						}
						
						str += pdf_inv + ",";
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ rset.getString(2) + "," + rset.getString(3) + "," + "HS" + "," + rset.getString(5) + "," + rset.getString(6) + ","), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			//NPR
			queryString = "SELECT '2',A.SUPPLIER_CD,A.CUSTOMER_CD,'NPR',A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO,A.NEW_INV_SEQ_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),NULL,A.INV_CUR_FLAG,A.GROSS_AMT_USD,"
					+ "A.EXCHG_RATE_CD,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.EXCHG_RATE_VALUE,A.GROSS_AMT_INR,A.TAX_AMT_INR,'1',B.REMARK,B.REMARK1,B.REMARK2,B.REMARK3,"
					+ "A.CHECKED_FLAG,A.CHECKED_BY,TO_CHAR(A.CHECKED_DT,'DD/MM/YYYY'),A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,TO_CHAR(A.AUTHORIZED_DT,'DD/MM/YYYY'),"
					+ "A.APPROVED_FLAG,A.APPROVED_BY,TO_CHAR(A.APPROVED_DT,'DD/MM/YYYY'),A.PDF_INV_DTL,A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'),"
					+ "A.PRINT_BY_TRI,TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'),A.PRINT_BY_DUP,TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,"
					+ "A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.NET_AMT_INR,A.HLPL_INV_SEQ_NO,'S','V','F',NULL,NULL  "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE A.CONTRACT_TYPE = 'N' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
//					else if(i == 6)
//					{
//						if(sup_cd.equals("1"))
//						{
//							value = "RCL"+value;
//						}
//						else if(sup_cd.equals("2"))
//						{
//							value = "RCP"+value;
//						}
//						str += value + ",";	
//					}
					else if(i == 12)
					{
						sale_unit = rset.getString(13);
						if(sale_unit == null)
						{
							value = "1";
						}
						else 
						{
							value = rset.getString(13);
						}
						str += value + ",";
					}
					else if(i == 33)
					{
						pdf_inv = rset.getString(34);
						if(pdf_inv!=null) 
						{
							if(pdf_inv.contains("T")) 
							{
								pdf_inv = "T";
							}
							else if(pdf_inv.contains("D")) 
							{
								pdf_inv = "D";
							}
							else if(pdf_inv.contains("O")) 
							{
								pdf_inv ="O";
							}
							else 
							{
								pdf_inv = null;
							}
						}
						else 
						{
							pdf_inv = null;
						}
						
						str += pdf_inv + ",";
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ rset.getString(2) + "," + rset.getString(3) + "," + "NPR" + "," + rset.getString(5) + "," + rset.getString(6) + ","), conn, "");
			}
			stmt.close();
			rset.close();
			
			//Scrap Fixed Asset
			queryString = "SELECT '2',A.SUPPLIER_CD,A.CUSTOMER_CD,'SFA',A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO,A.NEW_INV_SEQ_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),NULL,'1',A.GROSS_AMT_INR,"
					+ "A.EXCHG_RATE_CD,NULL,A.EXCHG_RATE_VALUE,A.GROSS_AMT_INR,A.TAX_AMT_INR,'1',NULL,NULL,NULL,NULL,"
					+ "A.CHECKED_FLAG,A.CHECKED_BY,TO_CHAR(A.CHECKED_DT,'DD/MM/YYYY'),A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,TO_CHAR(A.AUTHORIZED_DT,'DD/MM/YYYY'),"
					+ "A.APPROVED_FLAG,A.APPROVED_BY,TO_CHAR(A.APPROVED_DT,'DD/MM/YYYY'),A.PDF_INV_DTL,A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'),"
					+ "A.PRINT_BY_TRI,TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'),A.PRINT_BY_DUP,TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,"
					+ "A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.NET_AMT_INR,A.HLPL_INV_SEQ_NO,NULL,'V','F',NULL,NULL  "
					+ "FROM FMS7_INVOICE_MST A "
					+ "WHERE A.CONTRACT_TYPE = 'Z' "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";remark="";remark1="";remark2="";remark3="";flag="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 6)
					{
						if(sup_cd.equals("2") && !value.contains("P"))
						{
							value = value+"/P";
						}
						
						str += value + ",";	
					}
					else if(i == 20)
					{
						
						queryString1 = "SELECT REMARK,REMARK1,REMARK2,REMARK3,FLAG_SAC "
								+ "FROM FMS8_OTHER_INVOICE_DTL  "
								+ "WHERE INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
								+ "AND CONTRACT_TYPE = 'Z' ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(6));
						stmt1.setString(2, rset.getString(5));
						stmt1.setString(3, rset.getString(2));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							remark = rset1.getString(1) == null ? "null" : rset1.getString(1);
							remark1 = rset1.getString(2) == null ? "null" : rset1.getString(2);
							remark2 = rset1.getString(3) == null ? "null" : rset1.getString(3);
							remark3 = rset1.getString(4) == null ? "null" : rset1.getString(4);
							flag = rset1.getString(5) == null ? "null" : rset1.getString(5);
							
						}
						rset1.close();
						stmt1.close();
						
						remark = remark.replaceAll(",", " ");
						remark = remark.replaceAll("\n", " ");
						remark = remark.replaceAll("\r", " ");
						
						remark1 = remark1.replaceAll(",", " ");
						remark1 = remark1.replaceAll("\n", " ");
						remark1 = remark1.replaceAll("\r", " ");
						
						remark2 = remark2.replaceAll(",", " ");
						remark2 = remark2.replaceAll("\n", " ");
						remark2 = remark2.replaceAll("\r", " ");
						
						remark3 = remark3.replaceAll(",", " ");
						remark3 = remark3.replaceAll("\n", " ");
						remark3 = remark3.replaceAll("\r", " ");
						
						
						str += remark + ",";	
					}
					else if(i == 21)
					{
						str += remark1 + ",";	
					}
					else if(i == 22)
					{
						str += remark2 + ",";	
					}
					else if(i == 23)
					{
						str += remark3 + ",";	
					}
					
					else if(i == 33)
					{
						pdf_inv = rset.getString(34);
						if(pdf_inv!=null) 
						{
							if(pdf_inv.contains("T")) 
							{
								pdf_inv = "T";
							}
							else if(pdf_inv.contains("D")) 
							{
								pdf_inv = "D";
							}
							else if(pdf_inv.contains("O")) 
							{
								pdf_inv ="O";
							}
							else 
							{
								pdf_inv = null;
							}
						}
						else 
						{
							pdf_inv = null;
						}
						
						str += pdf_inv + ",";
					}
					else if(i == 50)
					{
						str += flag + ",";
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ rset.getString(2) + "," + rset.getString(3) + "," + "SFA" + "," + rset.getString(5) + "," + rset.getString(6) + ","), conn, "");
			}
			stmt.close();
			rset.close();
			
			//AHPL Revenue Share
			queryString = "SELECT '2',A.SUPPLIER_CD,A.CUSTOMER_CD,'AHPL',A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO,A.NEW_INV_SEQ_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),NULL,'1',A.GROSS_AMT_INR_NEW,"
					+ "NULL,NULL,NULL,A.GROSS_AMT_INR,A.TAX_AMT_INR,'1',NULL,NULL,NULL,NULL,"
					+ "A.CHECKED_FLAG,A.CHECKED_BY,TO_CHAR(A.CHECKED_DT,'DD/MM/YYYY'),A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,TO_CHAR(A.AUTHORIZED_DT,'DD/MM/YYYY'),"
					+ "A.APPROVED_FLAG,A.APPROVED_BY,TO_CHAR(A.APPROVED_DT,'DD/MM/YYYY'),A.PDF_INV_DTL,A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'),"
					+ "A.PRINT_BY_TRI,TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'),A.PRINT_BY_DUP,TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,"
					+ "A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.NET_AMT_INR,A.HLPL_INV_SEQ_NO,'S','V','F',NULL,NULL  "
					+ "FROM FMS7_INVOICE_MST A "
					+ "WHERE A.CONTRACT_TYPE = '3' "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.FINANCIAL_YEAR,A.HLPL_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";remark="";remark1="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 20)
					{
						
						queryString1 = "SELECT REMARK,REMARK3 "
								+ "FROM FMS8_OTHER_INVOICE_DTL  "
								+ "WHERE INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
								+ "AND CONTRACT_TYPE = '3' ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(6));
						stmt1.setString(2, rset.getString(5));
						stmt1.setString(3, rset.getString(2));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							remark = rset1.getString(1) == null ? "null" : rset1.getString(1);
							remark1 = rset1.getString(2) == null ? "null" : rset1.getString(2);
							
						}
						rset1.close();
						stmt1.close();
						
						remark = remark.replaceAll(",", " ");
						remark = remark.replaceAll("\n", " ");
						remark = remark.replaceAll("\r", " ");
						
						remark1 = remark1.replaceAll(",", " ");
						remark1 = remark1.replaceAll("\n", " ");
						remark1 = remark1.replaceAll("\r", " ");
						
						str += remark + ",";	
					}
					else if(i == 21)
					{
						str += remark1 + ",";	
					}
					
					else if(i == 33)
					{
						pdf_inv = rset.getString(34);
						if(pdf_inv!=null) 
						{
							if(pdf_inv.contains("T")) 
							{
								pdf_inv = "T";
							}
							else if(pdf_inv.contains("D")) 
							{
								pdf_inv = "D";
							}
							else if(pdf_inv.contains("O")) 
							{
								pdf_inv ="O";
							}
							else 
							{
								pdf_inv = null;
							}
						}
						else 
						{
							pdf_inv = null;
						}
						
						str += pdf_inv + ",";
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ rset.getString(2) + "," + rset.getString(3) + "," + "SFA" + "," + rset.getString(5) + "," + rset.getString(6) + ","), conn, "");
			}
			stmt.close();
			rset.close();
			
			//Berthing CR/DR
			queryString = "SELECT '2',A.SUPPLIER_CD,B.CUSTOMER_CD,'HSA',B.DR_CR_FIN_YEAR,B.DR_CR_NO,B.DR_CR_DOC_NO,TO_CHAR(B.DR_CR_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(B.DR_CR_PAY_DUE_DT,'DD/MM/YYYY'),NULL,NULL,'0','2',NULL,"
					+ "NULL,NULL,NULL,'-' || B.DR_CR_GROSS_AMT_INR,'-' || B.DR_CR_TAX_AMT_INR,'2',B.REMARK_DTL,NULL,NULL,NULL,"
					+ "A.CHECKED_FLAG,A.CHECKED_BY,TO_CHAR(A.CHECKED_DT,'DD/MM/YYYY'),NULL,NULL,NULL,"
					+ "'Y',B.APRV_BY,TO_CHAR(B.APRV_DT,'DD/MM/YYYY'),B.PDF_INV_DTL,NULL, NULL,"
					+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "B.ENT_BY_DTL,TO_CHAR(B.ENT_DT_DTL,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,'-' || B.DR_CR_NET_AMT_INR,B.DR_CR_NO,'S','V',UPPER(B.DR_CR_FLAG),NULL,'HRS',"
					+ "B.CONTRACT_TYPE,B.HLPL_INV_SEQ_NO,B.FINANCIAL_YEAR,TO_CHAR(B.INVOICE_DT,'DD/MM/YYYY')  "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHINV_DR_CR_NOTE B "
					+ "WHERE A.CONTRACT_TYPE = '1' AND A.HLPL_INV_SEQ_NO = B.HLPL_INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.CUSTOMER_CD = B.CUSTOMER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.INVOICE_DT = B.INVOICE_DT AND B.CRITERIA!='Others--' "
					+ "AND (? IS NULL OR B.ENT_DT_DTL >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR B.ENT_DT_DTL <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY B.DR_CR_FIN_YEAR,B.DR_CR_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				String month= rset.getString(8).split("/")[1];
				String year=rset.getString(8).split("/")[2];

				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 9)
					{
						value = utilDate.getFirstDateOfMonth(month, year);
						str += value + ",";	
					}
					else if(i == 10)
					{
						value = utilDate.getLastDateOfMonth(month, year);
						str += value + ",";	
					}
					else if(i == 53)
					{
						queryString1 = "SELECT NEW_INV_SEQ_NO "
								+ "FROM FMS7_INVOICE_MST "
								+ "WHERE CONTRACT_TYPE = ? AND HLPL_INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND CUSTOMER_CD = ? "
								+ "AND SUPPLIER_CD = ? AND INVOICE_DT = TO_DATE(?, 'DD/MM/YYYY')";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(56));
						stmt1.setString(2, rset.getString(57));
						stmt1.setString(3, rset.getString(58));
						stmt1.setString(4, rset.getString(3));
						stmt1.setString(5, sup_cd);
						stmt1.setString(6, rset.getString(59));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";	
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ rset.getString(2) + "," + rset.getString(3) + "," + "HSA" + "," + rset.getString(5) + "," + rset.getString(6) + ","), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_OTH_INVOICE_MST>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_OTH_INVOICE_MST>>");
			System.out.println();
				
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch(Exception e)
		{
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_OTH_INVOICE_DTL() throws SQLException,IOException 
	{
		function_nm="FMS_OTH_INVOICE_DTL()";
		try
		{
			System.out.println("<<START>><<FMS_OTH_INVOICE_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_OTH_INVOICE_DTL>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_NO,INVOICE_SEQ,ITEM_DESCRIPTION,SALE_PRICE,SALE_PRICE_UNIT,"
					+ "TAX_STRUCT_CD,FINANCIAL_YEAR,HSN_CODE,SAC_CODE,PURCHASE_NO,REFERENCE_NO,VESSEL_CD,VESSEL_AGENT,VESSEL_FLAG,GRT,IMPORTER,QUANTITY,"
					+ "CARGO_DT,CARGO_AMOUNT,TAX_CD,GATE_PASS_NO,HRS_BERTHING,TIME_SLOTS_BERTHING,FLAG_SAC,SALE_NO,PACER_NO,VENDOR_SUPP_INV_REF,UAM_NO,"
					+ "CARGO_TYPE,CESS_RATE,CESS_AMOUNT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TAX_EFF_DT,DISCHARGE_PORT,SIGN,AMT_DESCRIPTION,ITEM_AMT,ITEM_FLAG,"
					+ "INV_FLAG,REF_NO,CRITERIA,ITEM_TAX_AMT,TOTAL_CESS_AMOUNT";

			String sup_cd="";
			String fname_csv = "", str = "",sale_unit="",tax="",tax_val="",sign="",item_des="",item_amt="";
			int seq_no = 0;
			BigDecimal tax_amt = BigDecimal.ZERO;
			BigDecimal rate = BigDecimal.ZERO;
			BigDecimal factor = new BigDecimal(2);
			String temp_no ="",cargo_type = "";
			count = 0;

			fname_csv = migration_setup_dir + "EXPORT/FMS_OTH_INVOICE_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			for (int i = 0; i < columns.split(",").length; i++) 
			{
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
		
			logger.checkpoint(fname, "COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,EFF_DT,SEQ_NO,INVOICE_SEQ,TIMESTAMP", conn);
			
			//Cost_Recharge
			queryString = "SELECT '2',B.SUPPLIER_CD,A.CUSTOMER_CD,'COSTR',TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),'1',A.NEW_INV_SEQ_NO,B.INV_SEQ_NO,B.ITEM_DESCRIPTION,B.RATE,A.INV_CUR_FLAG,"
					+ "B.TAX_DETAILS,B.FINANCIAL_YEAR,NULL,B.SAC_CODE,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "NULL,NULL,B.TAX_CD,NULL,NULL,NULL,NULL,NULL,B.PACER_NO,B.VENDOR_SUPP_INV_REF,NULL,"
					+ "NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "'F',NULL,NULL,NULL,NULL "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE B.CONTRACT_TYPE = 'X' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(2));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 6)
					{
						if(sup_cd.equals("1"))
						{
							value = "RCL"+value;
						}
						else if(sup_cd.equals("2"))
						{
							value = "RCP"+value;
						}
						str += value + ",";	
					}
					else if(i == 10)
					{
						sale_unit = rset.getString(11);
						if(sale_unit == null)
						{
							value = "1";
						}
						else 
						{
							value = rset.getString(11);
						}
						str += value + ",";
					}
					else if(i == 11)
					{
						value = rset.getString(12);
						value = value.replaceAll(",", "@");
						value = value.replaceAll(".0", "");
						value = value.replaceAll("-", " ");
						
						if(value.contains("CGST") && !value.contains("@"))
						{
							tax = value.split(" ")[1];
							tax = tax.split("%")[0];
							
							value = "CGST "+tax+"%@SGST "+tax+"%";
						}
						
						str += value + ",";		
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			//Cost_recharge_HPPL
			queryString = "SELECT '2',B.SUPPLIER_CD,A.CUSTOMER_CD,'COSTRH',TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),'1',A.NEW_INV_SEQ_NO,B.INV_SEQ_NO,B.ITEM_DESCRIPTION,B.RATE,A.INV_CUR_FLAG,"
					+ "B.TAX_DETAILS,B.FINANCIAL_YEAR,NULL,B.SAC_CODE,B.PURCHASE_NO,B.REFERENCE_NO,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "NULL,NULL,B.TAX_CD,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "'F',NULL,NULL,NULL,NULL "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE B.CONTRACT_TYPE = 'Y' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 10)
					{
						sale_unit = rset.getString(11);
						if(sale_unit == null)
						{
							value = "1";
						}
						else 
						{
							value = rset.getString(11);
						}
						str += value + ",";
					}
					else if(i == 11)
					{
						value = rset.getString(12);
						value = value.replaceAll(",", "@");
						value = value.replaceAll(".0", "");
						value = value.replaceAll("-", " ");
						
						if(value.contains("CGST") && !value.contains("@"))
						{
							tax = value.split(" ")[1];
							tax = tax.split("%")[0];
							
							value = "CGST "+tax+"%@SGST "+tax+"%";
						}
						
						str += value + ",";		
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			//HPPL Shipping Agent
			queryString = "SELECT '2',B.SUPPLIER_CD,A.CUSTOMER_CD,'HSA',TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),'1',A.NEW_INV_SEQ_NO,B.INV_SEQ_NO,B.ITEM_DESCRIPTION,B.RATE,'2',"
					+ "B.TAX_DETAILS,B.FINANCIAL_YEAR,NULL,B.SAC_CODE,NULL,NULL,B.VESSEL_CD,B.VESSEL_AGENT,B.VESSEL_FLAG,B.GRT,B.IMPORTER,B.QUANTITY,"
					+ "NULL,NULL,B.TAX_CD,NULL,B.HRS_BERTHING,B.TIME_SLOTS_BERTHING,NULL,NULL,NULL,NULL,NULL,"
					+ "NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "'F',NULL,NULL,NULL,NULL "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE B.CONTRACT_TYPE = '1' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 6)
					{
						if(!value.contains("P"))
						{
							value = value+"/P";
						}
						
						str += value + ",";	
					}
					else if(i == 11)
					{
						value = rset.getString(12);
						value = value.replaceAll(",", "@");
						value = value.replaceAll(".0", "");
						value = value.replaceAll("-", " ");
						
						if(value.contains("CGST") && !value.contains("@"))
						{
							tax = value.split(" ")[1];
							tax = tax.split("%")[0];
							
							value = "CGST "+tax+"%@SGST "+tax+"%";
						}
						
						str += value + ",";		
					}
					else if(i == 17)
					{
						queryString1 = "SELECT SHIP_NAME "
								+ "FROM FMS7_SHIP_MST  "
								+ "WHERE SHIP_CD = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(18));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";	
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			//PFA Invoice HPPL-SEIPL
			queryString = "SELECT '2',B.SUPPLIER_CD,B.CUSTOMER_NM,'HS',TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),NULL,A.NEW_INV_SEQ_NO,B.INV_SEQ_NO,B.ITEM_DESCRIPTION,B.RATE,'2',"
					+ "B.TAX_DETAILS,B.FINANCIAL_YEAR,NULL,B.SAC_CODE,NULL,B.REFERENCE_NO,B.VESSEL_CD,NULL,NULL,NULL,NULL,B.QUANTITY,"
					+ "TO_CHAR(B.CARGO_DT,'DD/MM/YYYY'),B.CARGO_AMOUNT,B.TAX_CD,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "B.CARGO_TYPE,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "'F',NULL,NULL,NULL,NULL "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE B.CONTRACT_TYPE = '2' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.NEW_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				cargo_type = rset.getString(35);
				str = "";sale_unit="";
				
				if(temp_no.equals(rset.getString(7)))
				{
					seq_no++;
				}
				else 
				{
					seq_no = 1;
					temp_no =  rset.getString(7);
				}
				
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 2)
					{
						value = rset.getString(3);
						if(value.equals("Hazira LNG Private Limited"))
						{
							value = "3";
						}
						else 
						{
							value = "2";
						}
						str += value + ",";
					}
					else if(i == 5)
					{
						
						str += seq_no + ",";
					}
					else if(i == 6)
					{
						if(!value.contains("P"))
						{
							value = value+"/P";
						}
						
						str += value + ",";	
					}
					else if(i == 10)
					{
						sale_unit = rset.getString(11);
						if(sale_unit == null)
						{
							value = "1";
						}
						else 
						{
							value = rset.getString(11);
						}
						str += value + ",";
					}
					else if(i == 11)
					{
						value = rset.getString(12);
						value = value.replaceAll(",", "@");
						value = value.replaceAll(".0", "");
						value = value.replaceAll("-", " ");
						
						if(value.contains("CGST") && !value.contains("@"))
						{
							tax = value.split(" ")[1];
							tax = tax.split("%")[0];
							
							value = "CGST "+tax+"%@SGST "+tax+"%";
						}
						
						str += value + ",";		
					}
					else if(i == 17)
					{
						queryString1 = "SELECT SHIP_NAME "
								+ "FROM FMS7_SHIP_MST  "
								+ "WHERE SHIP_CD = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(18) == null ? "null" : rset.getString(18));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";	
					}
					else if(i == 42)
					{
						if(cargo_type.equals("LTCORA"))
						{
							queryString1 = "SELECT DISCHARGE_PORT FROM FMS8_LNG_REGAS_CARGO_DTL WHERE CARGO_REF_NO=? AND QQ_DT=TO_DATE(?,'DD/MM/YYYY')";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1,rset.getString(17));
							stmt1.setString(2,rset.getString(24));
							rset1 = stmt1.executeQuery();
							if (rset1.next()) 
							{
								value = rset1.getString(1) == null ? "null" : rset1.getString(1);
							}
							
							rset1.close();
							stmt1.close();
						}
						else if(cargo_type.equals("SALES"))
						{
							queryString1 = "SELECT DISCHARGE_PORT FROM FMS7_CARGO_QQ_DTL WHERE CARGO_REF_NO=? AND QQ_DT=TO_DATE(?,'DD/MM/YYYY')";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1,rset.getString(17));
							stmt1.setString(2,rset.getString(24));
							rset1 = stmt1.executeQuery();
							if (rset1.next()) 
							{
								value = rset1.getString(1) == null ? "null" : rset1.getString(1);
							}
							rset1.close();
							stmt1.close();
						}
						
						if(value.equals("Hazira"))
						{
							value = "1";
						}
						else if(value.equals("Dahej"))
						{
							value = "2";
						}
						else 
						{
							value = "null";
						}
						
						str += value + ",";		
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			//NPR
			queryString = "SELECT '2',B.SUPPLIER_CD,A.CUSTOMER_CD,'NPR',TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),'1',A.NEW_INV_SEQ_NO,B.INV_SEQ_NO,B.ITEM_DESCRIPTION,B.RATE,A.INV_CUR_FLAG,"
					+ "B.TAX_DETAILS,B.FINANCIAL_YEAR,NULL,B.SAC_CODE,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "NULL,NULL,B.TAX_CD,NULL,NULL,NULL,NULL,NULL,B.PACER_NO,B.VENDOR_SUPP_INV_REF,NULL,"
					+ "NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "'F',NULL,NULL,NULL,NULL "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE B.CONTRACT_TYPE = 'N' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 6)
					{
						if(sup_cd.equals("HPPL") && !value.contains("P"))
						{
							value = value+"/P";
						}
						
						str += value + ",";	
					}
					else if(i == 10)
					{
						sale_unit = rset.getString(11);
						if(sale_unit == null)
						{
							value = "1";
						}
						else 
						{
							value = rset.getString(11);
						}
						str += value + ",";
					}
					else if(i == 11)
					{
						value = rset.getString(12);
						value = value.replaceAll(",", "@");
						value = value.replaceAll(".0", "");
						value = value.replaceAll("-", " ");
						
						if(value.contains("CGST") && !value.contains("@"))
						{
							tax = value.split(" ")[1];
							tax = tax.split("%")[0];
							
							value = "CGST "+tax+"%@SGST "+tax+"%";
						}
						
						str += value + ",";		
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			//Scrap Fixed Asset - S
			queryString = "SELECT '2',B.SUPPLIER_CD,A.CUSTOMER_CD,'SFA',TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),NULL,A.NEW_INV_SEQ_NO,B.INV_SEQ_NO,B.ITEM_DESCRIPTION,B.RATE,'1',"
					+ "B.TAX_DETAILS,B.FINANCIAL_YEAR,B.SAC_CODE,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,B.QUANTITY,"
					+ "NULL,NULL,B.TAX_CD,B.GATE_PASS_NO,NULL,NULL,NULL,B.SALE_NO,NULL,NULL,B.UAM_NO,"
					+ "NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,B.CARGO_AMOUNT,NULL,"
					+ "'F',NULL,NULL,NULL,NULL "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE B.CONTRACT_TYPE = 'Z' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND B.FLAG_SAC = 'S' "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.NEW_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				if(temp_no.equals(rset.getString(8)))
				{
					seq_no++;
				}
				else 
				{
					seq_no = 1;
					temp_no =  rset.getString(8);
				}
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 5)
					{
						str += seq_no + ",";
					}
					else if(i == 6)
					{
						if(sup_cd.equals("2") && !value.contains("P"))
						{
							value = value+"/P";
						}
						
						str += value + ",";	
					}
					else if(i == 11)
					{
						value = rset.getString(12);
						value = value.replaceAll(",", "@");
						value = value.replaceAll(".0", "");
						value = value.replaceAll("-", " ");
						
						if(value.contains("CGST") && !value.contains("@"))
						{
							tax = value.split(" ")[1];
							tax = tax.split("%")[0];
							
							value = "CGST "+tax+"%@SGST "+tax+"%";
						}
						
						str += value + ",";		
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			//Scrap Fixed Asset - P
			queryString = "SELECT '2',B.SUPPLIER_CD,A.CUSTOMER_CD,'SFA',TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),NULL,A.NEW_INV_SEQ_NO,B.INV_SEQ_NO,B.ITEM_DESCRIPTION,B.RATE,'1',"
					+ "B.TAX_DETAILS,B.FINANCIAL_YEAR,B.HSN_CODE,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,B.QUANTITY,"
					+ "NULL,NULL,B.TAX_CD,B.GATE_PASS_NO,NULL,NULL,NULL,B.SALE_NO,NULL,NULL,B.UAM_NO,"
					+ "NULL,B.CESS_RATE,NVL(B.CESS_AMOUNT,'0'),A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,B.CARGO_AMOUNT,NULL,"
					+ "'F',NULL,NULL,NULL,NVL(B.TOTAL_CESS_AMOUNT,'0') "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE B.CONTRACT_TYPE = 'Z' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND B.FLAG_SAC = 'P' "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY A.NEW_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				rate = rset.getBigDecimal(46);
				
				str = "";sale_unit="";tax_val="";
				
				if(temp_no.equals(rset.getString(8)))
				{
					seq_no++;
				}
				else 
				{
					seq_no = 1;
					temp_no =  rset.getString(8);
				}
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 5)
					{
						str += seq_no + ",";
					}
					else if(i == 6)
					{
						if(sup_cd.equals("2") && !value.contains("P"))
						{
							value = value+"/P";
						}
						
						str += value + ",";	
					}
					else if(i == 11)
					{
						value = rset.getString(12);
						value = value.replaceAll(",", "@");
						value = value.replaceAll(".0", "");
						value = value.replaceAll("-", " ");
						tax = value;
						
						if(value.contains("CGST") && !value.contains("@"))
						{
							tax = value.split(" ")[1];
							tax = tax.split("%")[0];
							
							value = "CGST "+tax+"%@SGST "+tax+"%";
							tax = "CGST "+tax+"%@SGST "+tax+"%";
						}
						
						str += value + ",";		
					}
					else if(i == 50)
					{
						tax_val = tax.split(" ")[1];
						tax_val = tax_val.split("%")[0];
						BigDecimal taxRate = new BigDecimal(tax_val); 
						if(tax.contains("CGST"))
						{
						 	tax_amt = BigDecimal.valueOf(Math.round(Double.parseDouble(""+rate.multiply(taxRate).divide(new BigDecimal("100")))));
						 	tax_amt = tax_amt.multiply(factor);
						}
						else
						{
							tax_amt = BigDecimal.valueOf(Math.round(Double.parseDouble(""+rate.multiply(taxRate).divide(new BigDecimal("100")))));
						}
						tax_amt = BigDecimal.valueOf(Math.round(Double.parseDouble(""+tax_amt)));
						
						str += tax_amt + ",";		
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			//AHPL
			queryString = "SELECT '2',B.SUPPLIER_CD,A.CUSTOMER_CD,'AHPL',TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),'1',A.NEW_INV_SEQ_NO,B.INV_SEQ_NO,B.ITEM_DESCRIPTION,NULL,'1',"
					+ "B.TAX_DETAILS,B.FINANCIAL_YEAR,NULL,B.SAC_CODE,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "NULL,A.GROSS_AMT_USD,B.TAX_CD,NULL,NULL,NULL,NULL,NULL,B.PACER_NO,B.VENDOR_SUPP_INV_REF,NULL,"
					+ "NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,NULL,'G',"
					+ "'F',NULL,NULL,NULL,NULL "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE B.CONTRACT_TYPE = '3' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND B.REMARK2 IS NULL "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 10)
					{
						sale_unit = rset.getString(11);
						if(sale_unit == null)
						{
							value = "1";
						}
						else 
						{
							value = rset.getString(11);
						}
						str += value + ",";
					}
					else if(i == 11)
					{
						value = rset.getString(12);
						value = value.replaceAll(",", "@");
						value = value.replaceAll(".0", "");
						value = value.replaceAll("-", " ");
						
						if(value.contains("CGST") && !value.contains("@"))
						{
							tax = value.split(" ")[1];
							tax = tax.split("%")[0];
							
							value = "CGST "+tax+"%@SGST "+tax+"%";
						}
						
						str += value + ",";		
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			//AHPL
			queryString = "SELECT '2',B.SUPPLIER_CD,A.CUSTOMER_CD,'AHPL',TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),'1',A.NEW_INV_SEQ_NO,B.INV_SEQ_NO,B.ITEM_DESCRIPTION,NULL,'1',"
					+ "B.TAX_DETAILS,B.FINANCIAL_YEAR,NULL,B.SAC_CODE,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "NULL,A.GROSS_AMT_USD,B.TAX_CD,NULL,NULL,NULL,NULL,NULL,B.PACER_NO,B.VENDOR_SUPP_INV_REF,NULL,"
					+ "NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,B.REMARK2,NULL,NULL,'G',"
					+ "'F',NULL,NULL,NULL,NULL "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE B.CONTRACT_TYPE = '3' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND B.REMARK2 IS NOT NULL "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 10)
					{
						sale_unit = rset.getString(11);
						if(sale_unit == null)
						{
							value = "1";
						}
						else 
						{
							value = rset.getString(11);
						}
						str += value + ",";
					}
					else if(i == 11)
					{
						value = rset.getString(12);
						value = value.replaceAll(",", "@");
						value = value.replaceAll(".0", "");
						value = value.replaceAll("-", " ");
						
						if(value.contains("CGST") && !value.contains("@"))
						{
							tax = value.split(" ")[1];
							tax = tax.split("%")[0];
							
							value = "CGST "+tax+"%@SGST "+tax+"%";
						}
						
						str += value + ",";		
					}
					else if(i == 43)
					{
						value  = rset.getString(44);
						sign = value.split("===")[0];
						item_des = value.split("===")[1];
						item_amt = value.split("===")[2];
						
						if(sign.equals("0"))
						{
							sign = "+";
						}
						else 
						{
							sign = "-";
						}
						
						str += sign + ",";	
					}
					else if(i == 44)
					{
						str += item_des + ",";	
					}
					else if(i == 45)
					{
						str += item_amt + ",";	
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			//AHPL
			queryString = "SELECT '2',B.SUPPLIER_CD,A.CUSTOMER_CD,'AHPL',TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),'2',A.NEW_INV_SEQ_NO,B.INV_SEQ_NO,B.ITEM_DESCRIPTION,NULL,'1',"
					+ "B.TAX_DETAILS,B.FINANCIAL_YEAR,NULL,B.SAC_CODE,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "NULL,A.INV_AMT_INR,B.TAX_CD,NULL,NULL,NULL,NULL,NULL,B.PACER_NO,B.VENDOR_SUPP_INV_REF,NULL,"
					+ "NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,NULL,'L',"
					+ "'F',NULL,NULL,NULL,NULL "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHER_INVOICE_DTL B "
					+ "WHERE B.CONTRACT_TYPE = '3' AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.SUPPLIER_CD = B.SUPPLIER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 10)
					{
						sale_unit = rset.getString(11);
						if(sale_unit == null)
						{
							value = "1";
						}
						else 
						{
							value = rset.getString(11);
						}
						str += value + ",";
					}
					else if(i == 11)
					{
						value = rset.getString(12);
						value = value.replaceAll(",", "@");
						value = value.replaceAll(".0", "");
						value = value.replaceAll("-", " ");
						
						if(value.contains("CGST") && !value.contains("@"))
						{
							tax = value.split(" ")[1];
							tax = tax.split("%")[0];
							
							value = "CGST "+tax+"%@SGST "+tax+"%";
						}
						
						str += value + ",";		
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			
			//Berthing DR/CR
			queryString = "SELECT '2',A.SUPPLIER_CD,B.CUSTOMER_CD,'HSA',TO_CHAR(B.DR_CR_DT,'DD/MM/YYYY'),'1',B.DR_CR_DOC_NO,B.DR_CR_NO,NULL,'0','2',"
					+ "NULL,B.DR_CR_FIN_YEAR,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,'0',"
					+ "NULL,NULL,NULL,NULL,B.DR_CR_HRS_BERTHING,B.DR_CR_SLOTS_BERTHING,NULL,NULL,NULL,NULL,NULL,"
					+ "NULL,NULL,NULL,B.ENT_BY_DTL,TO_CHAR(B.ENT_DT_DTL,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "UPPER(B.DR_CR_FLAG),A.NEW_INV_SEQ_NO,'HRS',NULL,NULL,A.HLPL_INV_SEQ_NO,A.FINANCIAL_YEAR "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHINV_DR_CR_NOTE B "
					+ "WHERE A.CONTRACT_TYPE = '1' AND A.HLPL_INV_SEQ_NO = B.HLPL_INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.CUSTOMER_CD = B.CUSTOMER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.INVOICE_DT = B.INVOICE_DT AND B.CRITERIA!='Others--' "
					+ "AND (? IS NULL OR B.ENT_DT_DTL >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR B.ENT_DT_DTL <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";sale_unit="";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 27)
					{
						double HRS = 0;
						queryString1 = "SELECT HRS_BERTHING "
								+ "FROM FMS8_OTHER_INVOICE_DTL  "
								+ "WHERE INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
								+ "AND CONTRACT_TYPE = '1' ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(53));
						stmt1.setString(2, rset.getString(54));
						stmt1.setString(3, rset.getString(2));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							HRS = rset1.getDouble(1);
						}
						rset1.close();
						stmt1.close();
						
						
						double HRS1 = rset.getDouble(28);
						
						value  = nf.format(HRS-HRS1);
						
						value = "-"+value;
						
						str += value + ",";		
					}
					else if(i == 28)
					{
						
						double time = 0;
						queryString1 = "SELECT TIME_SLOTS_BERTHING "
								+ "FROM FMS8_OTHER_INVOICE_DTL  "
								+ "WHERE INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
								+ "AND CONTRACT_TYPE = '1' ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(53));
						stmt1.setString(2, rset.getString(54));
						stmt1.setString(3, rset.getString(2));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							time = rset1.getDouble(1);
						}
						rset1.close();
						stmt1.close();
						
						
						double time1 = rset.getDouble(29);
						
						value  = nf.format(time-time1);
						
						value = "-"+value;
						
						str += value + ",";		
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_OTH_INVOICE_DTL>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_OTH_INVOICE_DTL>>");
			System.out.println();
				
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch(Exception e)
		{
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}
	
	
	public void FMS_OTH_INV_CRDR_REF() throws SQLException,IOException 
	{
		function_nm="FMS_OTH_INV_CRDR_REF()";
		try
		{
			System.out.println("<<START>><<FMS_OTH_INV_CRDR_REF>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_OTH_INV_CRDR_REF>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,SEQ_NO,INVOICE_SEQ,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,GRT,QUANTITY,CARGO_DT,CARGO_AMOUNT,HRS_BERTHING,TIME_SLOTS_BERTHING,"
					+ "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

			String sup_cd="";
			String fname_csv = "", str = "";
			count = 0;

			fname_csv = migration_setup_dir + "EXPORT/FMS_OTH_INV_CRDR_REF_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			for (int i = 0; i < columns.split(",").length; i++) 
			{
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
		
			logger.checkpoint(fname, "ENTITY_CD,ENTITY_TYPE,ADDRESS_TYPE,EFF_DT,TIMESTAMP", conn);
			
			//Berthing CR/DR
			queryString = "SELECT '2',A.SUPPLIER_CD,B.CUSTOMER_CD,'HSA',B.DR_CR_FIN_YEAR,'1',B.DR_CR_DOC_NO,NULL,B.SALE_PRICE,'2',NULL,"
					+ "NULL,NULL,NULL,'2',B.DR_CR_GROSS_AMT_INR,B.DR_CR_TAX_AMT_INR,NULL,B.DR_CR_NET_AMT_INR,NULL,B.TOTAL_QTY,NULL,NULL,"
					+ "B.DR_CR_HRS_BERTHING,B.DR_CR_SLOTS_BERTHING,B.ENT_BY_DTL,TO_CHAR(B.ENT_DT_DTL,'DD/MM/YYYY'),NULL,NULL,"
					+ "B.HLPL_INV_SEQ_NO,B.FINANCIAL_YEAR,B.GROSS_AMT_USD,A.TAX_AMT_INR,B.NET_AMT_INR "
					+ "FROM FMS7_INVOICE_MST A, FMS8_OTHINV_DR_CR_NOTE B "
					+ "WHERE A.CONTRACT_TYPE = '1' AND A.HLPL_INV_SEQ_NO = B.HLPL_INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.CUSTOMER_CD = B.CUSTOMER_CD "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.INVOICE_DT = B.INVOICE_DT AND B.CRITERIA!='Others--' "
					+ "AND (? IS NULL OR B.ENT_DT_DTL >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR B.ENT_DT_DTL <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				sup_cd = rset.getString(2);
				str = "";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					value = value.replaceAll(",", " ");
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					if (i==1) 
					{
						queryString1 = "SELECT A.SUPPLIER_ABBR "
								+ "FROM FMS7_SUPPLIER_MST A "
								+ "WHERE SUPPLIER_CD = ? AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, sup_cd);
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						str += value + ",";		
					}
					else if(i == 15)
					{
						double gross = rset.getDouble(32);
						double gross1 = rset.getDouble(16);
						
						value  = nf.format(gross-gross1);
						
						str += value + ",";	
					}
					else if(i == 16)
					{
						double tax = rset.getDouble(33);
						double tax1 = rset.getDouble(17);
						
						value  = nf.format(tax-tax1);
						
						str += value + ",";	
					}
					else if(i == 18)
					{
						double net = rset.getDouble(34);
						double net1 = rset.getDouble(19);
						
						value  = nf.format(net-net1);
						
						str += value + ",";	
					}
					else if(i == 19)
					{
						queryString1 = "SELECT GRT "
								+ "FROM FMS8_OTHER_INVOICE_DTL  "
								+ "WHERE INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
								+ "AND CONTRACT_TYPE = '1' ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(30));
						stmt1.setString(2, rset.getString(31));
						stmt1.setString(3, rset.getString(2));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) 
						{
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
					
						str += value + ",";	
					}
					else
					{
						str += value + ",";		
					}
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
			}
			stmt.close();
			rset.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_OTH_INV_CRDR_REF>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_OTH_INV_CRDR_REF>>");
			System.out.println();
				
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch(Exception e)
		{
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}
	
	
	public void FMS_OTH_INV_FILE_DTL() throws SQLException,IOException 
	{
		function_nm="FMS_OTH_INV_FILE_DTL()";
		try
		{
			System.out.println("<<START>><<FMS_OTH_INV_FILE_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_OTH_INV_FILE_DTL>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,"
					+ "SIGNED_DT,PDF_CONTENT,SF_GEN_DT,SIGNED_ENT_BY,SUPPLIER_CD";

			String fname_csv = "", str = "",pdf_nm="";
			count = 0;

			fname_csv = migration_setup_dir + "EXPORT/FMS_OTH_INV_FILE_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			for (int i = 0; i < columns.split(",").length; i++) 
			{
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
		
			logger.checkpoint(fname, "ENTITY_CD,ENTITY_TYPE,ADDRESS_TYPE,EFF_DT,TIMESTAMP", conn);
			
			queryString = "SELECT CONTRACT_TYPE,FINANCIAL_YEAR,HLPL_INV_SEQ_NO,SUPPLIER_CD,NEW_INV_SEQ_NO "
					+ "FROM FMS7_INVOICE_MST "
					+ "WHERE CONTRACT_TYPE IN ('1','2','3','X','Y','Z','N') " 
					+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY NEW_INV_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while(rset.next())
			{
				pdf_nm = "INVOICE-"+rset.getString(1)+"-"+rset.getString(2)+"-"+rset.getString(3)+"-"+rset.getString(4)+".pdf";
				
				queryString1 = "SELECT '2',NULL,NULL,NULL,INV_TYPE,PDF_INV_NM,NULL,TO_CHAR(CREATED_DT, 'DD/MM/YYYY'),NULL,NULL,PDF_SIGNED_FLAG,SIGNED_BY,"
						+ "TO_CHAR(SIGNED_DT, 'DD/MM/YYYY'),NULL,NULL,NULL,NULL "
						+ "FROM FMS8_INV_PDF_DTL "
						+ "WHERE PDF_INV_NM = ?";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, pdf_nm);
				rset1 = stmt1.executeQuery();
				
				while(rset1.next())
				{
					str = "";
					
					for (int i = 0; i < columns.split(",").length; i++) 
					{
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1);
						value = value.replaceAll(",", " ");
						value = value.replaceAll("\n", " ");
						value = value.replaceAll("\r", " ");
						
						if (i==1) 
						{
							value = rset.getString(5);
							
							if(rset.getString(1).equals("X"))
							{
								if(rset.getString(4).equals("1"))
								{
									value = "RCL"+value;
								}
								else if(rset.getString(4).equals("2"))
								{
									value = "RCP"+value;
								}
							}
							else if(rset.getString(1).equals("1") || rset.getString(1).equals("2"))
							{
								if(!rset.getString(5).contains("P"))
								{
									value = value+"/P";
								}
								else 
								{
									value = rset.getString(5);
								}
							}
							else if(rset.getString(1).equals("Z"))
							{
								if(rset.getString(4).equals("2") && !rset.getString(5).contains("P"))
								{
									value = value+"/P";
								}
							}
							str += value + ",";	
						}
						else if(i == 2)
						{
							value = rset.getString(1);
							
							if(value.equals("X"))
							{
								value = "COSTR";
							}
							else if(value.equals("Y"))
							{
								value = "COSTRH";
							}
							else if(value.equals("Z"))
							{
								value = "SFA";
							}
							else if(value.equals("1"))
							{
								value = "HSA";
							}
							else if(value.equals("2"))
							{
								value = "HS";
							}
							else if(value.equals("3"))
							{
								value = "AHPL";
							}
							else if(value.equals("N"))
							{
								value = "NPR";
							}
							
							str += value + ",";	
						}
						else if(i == 3)
						{
							value = rset.getString(2);
							str += value + ",";	
						}
						else if(i == 5)
						{
							value = rset1.getString(6);
							value = value.split(".pdf")[0];
							value = value+"-"+rset1.getString(5)+".pdf";
							str += value + ",";
						}
						else if(i == 16)
						{
							value = rset.getString(4);
							if(value.equals("1"))
							{
								value = "2";
							}
							else if(value.equals("2"))
							{
								value = "1";
							}
							str += value + ",";	
						}
						else
						{
							str += value + ",";		
						}
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
				}
				stmt1.close();
				rset1.close();
			}
			stmt.close();
			rset.close();

			
			//CRDR_PDF
			file1 = new File(migration_setup_dir + "DataLogs/FMSLIVE-SHIPPING-AGENT-CR-NOTE-PDF-LIST-20251208.csv");
			
			if(file1.exists()) 
			{
				String fileName = migration_setup_dir + "DataLogs/FMSLIVE-SHIPPING-AGENT-CR-NOTE-PDF-LIST-20251208.csv";
				String mon="",dt="",yr="",monthStr="",dtStr="",Date="";
				
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
				{
					String line = br.readLine();
					
					while ((line = br.readLine()) != null) 
					{
						pdf_nm="";
						for (int i = 0; i < line.split(",").length; i++)
					    {
							if(i == 0)
							{
								mon = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							if(i == 1)
							{
								dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							if(i == 2)
							{
								yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							if(i == 3)
							{
								pdf_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
					    }
						
						queryString1 = "SELECT '2',DR_CR_DOC_NO,'HSA',DR_CR_FIN_YEAR,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
								+ "NULL,NULL,NULL,NULL,'1' "
								+ "FROM FMS8_OTHINV_DR_CR_NOTE "
								+ "WHERE CONTRACT_TYPE = '1' AND FINANCIAL_YEAR = ? AND HLPL_INV_SEQ_NO = ? "
								+ "AND DR_CR_FLAG = ? AND CRITERIA!='Others--'";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, pdf_nm.split("-")[2]+"-"+pdf_nm.split("-")[3]);
						stmt1.setString(2, pdf_nm.split("-")[4]);
						stmt1.setString(3, pdf_nm.split("-")[7].substring(0, pdf_nm.split("-")[7].length()-4));
						rset1 = stmt1.executeQuery();
						
						while(rset1.next())
						{
							str = "";
							
							for (int i = 0; i < columns.split(",").length; i++) 
							{
								value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1);
								value = value.replaceAll(",", " ");
								value = value.replaceAll("\n", " ");
								value = value.replaceAll("\r", " ");
								
								if(i == 4)
								{
									value = pdf_nm.split("-")[6];
									str += value + ",";
								}
								else if (i == 5) 
								{
									str += pdf_nm + ",";	
								}
								else if(i == 7)
								{
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);
									int monthNumber = Month.from(formatter.parse(mon)).getValue();
									
									dtStr = String.format("%02d", Integer.parseInt(dt));
									monthStr = String.format("%02d", monthNumber);
									
									if(yr.contains(":"))
									{
										if(monthNumber>4)
										{
											yr = String.valueOf(Integer.parseInt(pdf_nm.split("-")[2]));
										}
										else 
										{
											yr = String.valueOf(Integer.parseInt(pdf_nm.split("-")[3]));
										}
									}	
									
									Date = dtStr+"/"+monthStr+"/"+yr;
									
									str += Date + ",";	
								}
								else
								{
									str += value + ",";		
								}
							}
							logger.insert_data(fname_csv, str, conn);
							count++;
							logger.data(fname, ( cd + ","+ "S" + "," + address_type + "," + eff_dt + "," ), conn, "");
							
						}
						stmt1.close();
						rset1.close();
					}
					br.close();
				}
			}
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_OTH_INV_FILE_DTL>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_OTH_INV_FILE_DTL>>");
			System.out.println();
				
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		
		}
		catch(Exception e)
		{
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_OTH_INV_IRN_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_OTH_INV_IRN_DTL()";
		
		try {

			logger.checkpoint(fname, "\n<<START>>,<<FMS_OTH_INV_IRN_DTL>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,INVOICE_NO,IRN_NO,XLS_FILE_NM,SIGN_QR_CODE,ENT_BY,ENT_DT";
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			count = 0;
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			queryString = " SELECT '2', NEW_INV_SEQ_NO, IRN_NO, XLS_FILE_NM, SIGN_QR_CODE, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
					+ "FROM FMS7_INVOICE_IRN_DTL WHERE CONTRACT_TYPE IN('X','Y','1','2','Z','3','N') ORDER BY CONTRACT_TYPE";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
						
			logger.checkpoint(fname, "COMPANY_CD,NEW_INV_SEQ_NO,IRN_NO,TIMESTAMP,", conn);
						
			while (rset.next()) {
				String irn_no="";
				irn_no = rset.getString(3);
				seq_no = rset.getString(2);
							
				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i+1) == null ? "null" : rset.getString(i+1);
								
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, ("2" + "," + seq_no + "," + irn_no + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_OTH_INV_IRN_DTL_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_OTH_INV_IRN_DTL>>,,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	
	
	public void getmail_list() {
		function_nm = "getmail_list()";
		
		try {
			String strline = "";

			File fsetup = new File(migration_setup_dir+"Migration_Setup.txt");
			String mail_list_path = fsetup.getAbsolutePath();
			
			try (FileInputStream f1 = new FileInputStream(mail_list_path)) {
			
				try (DataInputStream in = new DataInputStream(f1)) {
				
					try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) { 
		
						while ((strline = br.readLine()) != null) {
							if (strline.startsWith("dbline")) {
								String tmp[] = strline.split("dbline:");
								dbline = tmp[1].toString();
			
							}
							if (strline.startsWith("username")) {
								String tmp[] = strline.split("username:");
								username = tmp[1].toString();
							}
							if (strline.startsWith("password")) {
								String tmp[] = strline.split("password:");
								encrypted = tmp[1].toString();
								password = encrypted;
							}
						}
						
					}
				}
			}
			
		} catch (Exception e) {
			// LOGGER.log(Level.WARNING, "Error in OthInv_SEIPL_Data_Extractor.java -> OthInv_Excel_Extractor -> getmail_list() ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void makeDirectory() {
		function_nm = "makeDirectory()";
		try {
			String fName[] = {(migration_setup_dir+"EXPORT"), (migration_setup_dir+"DataLogs"), (migration_setup_dir+"DataLogs/RollBack"), (migration_setup_dir+"DataLogs/Extractor"), (migration_setup_dir+"DataLogs/Reader")};
			//String fName_Import = "IMPORT";
			
			for (int i = 0; i < fName.length; i++) {
				File directory = new File(fName[i]);
				
				if(!directory.exists()) {
	
					if(directory.mkdirs()) {
						System.out.println(fName[i]+" Directory Created Successfully.");
						dir_flag = "Y";
					}
					else {
						System.out.println("Failed to create Directory: " + fName[i]);
						dir_flag = "N";
					}
					
				}
				else {
					dir_flag = "Y";
					System.out.println(fName[i]+" Directory Already Exists.");
				}
			}
			
		} 
		catch(Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e); 
		}
	}

	// Setter-Getter methods
	public void setChecked_Values(String checked_val) {
		checked_values = checked_val;
	}
	
	public void setDelta_FromDt(String from_dt) {
		delta_FromDt = from_dt + " 00:00:00";
	}
	
	public void setDelta_ToDt(String to_dt) {
		delta_ToDt = to_dt + " 23:59:59";
	}
	
	public void setStart_End_Dt(String dt) {
		start_end_dt = dt;
	}
	
	public void setSysDateTime(String dt) {
		sysDateTime = dt;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public String getMsg_Type() {
		return msg_type;
	}
	
	public void setMigration_Setup_Dir(String dir) {
		migration_setup_dir = dir;
	}

}
