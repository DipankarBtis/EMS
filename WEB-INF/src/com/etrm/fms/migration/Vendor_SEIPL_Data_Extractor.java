
package com.etrm.fms.migration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.SystemErrorLogger;

public class Vendor_SEIPL_Data_Extractor {

	/*	public static void main(String[] args) {
			Vendor_Excel_Extractor ex = new Vendor_Excel_Extractor();
			ex.getmail_list();
			ex.getCustomerTraderList();
			ex.makeDirectory();
			ex.init();
		}
	
	}
	
	class Vendor_Excel_Extractor {*/

	String db_src_file_name = "Vendor_SEIPL_Data_Extractor.java";
	String function_nm = "";
	
	String migration_setup_dir = "";
	
	String sysDateTime = "";
	
	//private static final Logger LOGGER = Logger.getLogger(Vendor_Excel_Extractor.class.getName());
	String fname = "";
	String fname_error = "";
	
	//bellow fname1  is for csv file function start & function end only 
   //logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn); (-E : EXTRACTOR) 
	String fname1 = "";

	DataMigration_Logger logger = new DataMigration_Logger();
	Migration_Plants_Exceptions mpe =new Migration_Plants_Exceptions();

	String queryString = "", queryString1 = "";
	Connection conn;
	ResultSet rset, rset1;
	PreparedStatement stmt, stmt1;

	//String customer_delete = "", trader_delete = "", customer_map = "", trader_map = "", transporter_map = "", counterparty_map = "", meter_map= "";
	String dbline = "", username = "", encrypted = "", password = "";
	String columns = "", filename = "", value = "";
	
	String checked_values = "", msg = "", msg_type = "";
	
	String delta_FromDt = null;
	String delta_ToDt = null;
	String start_end_dt = null;
	
	String abbr = "", cd = "", seq_no = "", entity = "", eff_dt = "", address_type = "", tax_code = "", tax_nm = "";
	
	String dir_flag = "N";

	int nrow = 0;
	int count = 0;

	XSSFWorkbook workbook = null;
	XSSFSheet spreadsheet = null;
	XSSFRow row;
	Cell cell;
	
	FileOutputStream fileOut = null;

	public void init() {

        function_nm="init()";
		try {
			
			getmail_list();
//			getCustomerTraderList();
			makeDirectory();
			
			fname = "DataLogs/Extractor/Vendor_SEIPL_Data_Extractor(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Extractor/Vendor_SEIPL_Data_Extractor_Error(log)"+sysDateTime+".csv";
			
			fname = migration_setup_dir + fname;
			fname_error = migration_setup_dir + fname_error;
			
			fname1 = "DataLogs/Script_Status(log).csv";	
			fname1 = migration_setup_dir + fname1;

			Preferences preferences =  Preferences.userRoot().node("/processFlag");

			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("FMS8 DBLINE:"+dbline);
			conn = DriverManager.getConnection(dbline, username, password);
			
			
			if (conn != null && dir_flag.equals("Y") && !checked_values.equals("")) {

	    		preferences.put("Flag", "0");
				
				if (checked_values.contains("FMS_VENDOR_MST")) {
					FMS_VENDOR_MST();
				}
				
				if (checked_values.contains("FMS_VENDOR_ADDR_MST")) {
					FMS_VENDOR_ADDR_MST();
				}
				
				if (checked_values.contains("FMS_OTHER_INVOICE_MST")) {
					FMS_OTHER_INVOICE_MST();
				}
				
				if (checked_values.contains("FMS_OTHER_INVOICE_DTL")) {
					FMS_OTHER_INVOICE_DTL();
				}
				
	    		preferences.put("Flag", "1");
				conn.close();
				conn = null;
			
			}
			else {
				msg = "No Checkbox was selected. Extraction Terminated.";
				msg_type = "E";
			}

		} catch (Exception e) {
			//LOGGER.log(Level.WARNING, "Error in Vendor_SEIPL_Data_Extractor.java -> Vendor_Excel_Extractor -> init()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
		} 
		finally {
			try {
				if (rset != null) {
					try {
						rset.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Vendor_SEIPL_Data_Extractor.java -> Vendor_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (rset1 != null) {
					try {
						rset1.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Vendor_SEIPL_Data_Extractor.java -> Vendor_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Vendor_SEIPL_Data_Extractor.java -> Vendor_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt1 != null) {
					try {
						stmt1.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Vendor_SEIPL_Data_Extractor.java -> Vendor_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Vendor_SEIPL_Data_Extractor.java -> Vendor_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				
			} catch (Exception e) {
				//LOGGER.log(Level.WARNING, "Error in Vendor_SEIPL_Data_Extractor.java -> Vendor_Excel_Extractor -> init()  ", e);
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				
				msg = "One of the Functions faced an Error. Extraction Terminated.";
				msg_type = "E";
			}
		}

	}

	
	public void FMS_VENDOR_MST() throws SQLException,IOException 
	{
		function_nm="FMS_VENDOR_MST()";
		try
		{
			System.out.println("<<START>><<FMS_VENDOR_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_VENDOR_MST>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="VENDOR_CD,EFF_DT,VENDOR_NM,VENDOR_ABBR,GST_TIN_NO,GST_TIN_DT,CST_TIN_NO,CST_TIN_DT,"
					+ "PAN_NO,PAN_ISSUE_DT,TAN_NO,TAN_ISSUE_DT,GSTIN_NO,GSTIN_DT,WEB_ADDR,NOTES,"
					+ "PAYEE_ACCOUNT_NO,IFSC,PAYEE_NM,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;

			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = "SELECT VENDOR_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),VENDOR_NAME,VENDOR_ABBR,GST_TIN_NO,TO_CHAR(GST_TIN_DT,'DD/MM/YYYY HH24:MI:SS'),CST_TIN_NO,TO_CHAR(CST_TIN_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "PAN_NO,TO_CHAR(PAN_ISSUE_DT,'DD/MM/YYYY HH24:MI:SS'),TAN_NO,TO_CHAR(TAN_ISSUE_DT,'DD/MM/YYYY HH24:MI:SS'),GSTIN_NO,TO_CHAR(GSTIN_DT,'DD/MM/YYYY HH24:MI:SS'),WEB_ADDR,NOTES,"
					+ "PAYEE_ACCOUNT_NO,IFSC,PAYEE_NM,EMP_CD,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,'2',NULL  "
					+ "FROM FMS8_VENDOR_MST WHERE  (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY VENDOR_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "VENDOR_CD,EFF_DT,TIMESTAMP", conn);
			while(rset.next())
			{
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if (i==0) {
						cd=value;
						cell.setCellValue("'" + value + "'");
					}
					else if (i == 1) {
						eff_dt=value;
						cell.setCellValue("'" + value + "'");
					}
					else
					{
						cell.setCellValue("'" + value + "'");
					}
				}
				
				count++;
				logger.data(fname, ( cd+ "," + eff_dt + "," ), conn, "");
				
			}
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_VENDOR_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_VENDOR_MST>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_VENDOR_MST>>");
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
	
	public void FMS_VENDOR_ADDR_MST() throws SQLException,IOException 
	{
		function_nm="FMS_VENDOR_ADDR_MST()";
		try
		{
			System.out.println("<<START>><<FMS_VENDOR_ADDR_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_VENDOR_ADDR_MST>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="VENDOR_CD,EFF_DT,ADDRESS_TYPE,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;

			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = "SELECT VENDOR_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDRESS_TYPE,ADDR,CITY,PIN,"
					+ "STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,EMP_CD,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL   "
					+ "FROM FMS8_VENDOR_ADDRESS_MST "
					+ "WHERE  (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY VENDOR_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "VENDOR_CD,ADDRESS_TYPE,EFF_DT,TIMESTAMP", conn);
			while(rset.next())
			{
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if (i==0) {
						cd=value;
						cell.setCellValue("'" + value + "'");
					}
					else if (i == 1) {
						eff_dt=value;
						cell.setCellValue("'" + value + "'");
					}else if (i == 2) {
						address_type=value;
						cell.setCellValue("'" + value + "'");
					}
					else
					{
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, ( cd+ "," +address_type+","+ eff_dt + "," ), conn, "");
				
			}
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_VENDOR_ADDR_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_VENDOR_ADDR_MST>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_VENDOR_ADDR_MST>>");
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

	public void FMS_OTHER_INVOICE_MST() throws SQLException,IOException 
	{
		function_nm="FMS_OTHER_INVOICE_MST()";
		try
		{
			System.out.println("<<START>><<FMS_OTHER_INVOICE_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_OTHER_INVOICE_MST>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="COMPANY_CD,VENDOR_CD,CONTRACT_TYPE,PERIOD_START_DT,PERIOD_END_DT,INV_SEQ_NO,FINANCIAL_YEAR,"
					+ "INVOICE_DT,EXCHG_RATE_DT,EXCHG_RATE_VALUE,GROSS_AMT_INR,GROSS_AMT_USD,NET_AMT_INR,TAX_AMT_INR,FLAG,INV_CUR_FLAG,SALE_PRICE_FLAG,CHECKED_FLAG,CHECKED_BY,"
					+ "CHECKED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,PDF_INV_DTL,NEW_INV_SEQ_NO,SUPPLIER_CD,"
					+ "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;
			
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = "SELECT '2', CUSTOMER_CD, CONTRACT_TYPE, TO_CHAR(PERIOD_START_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'), HLPL_INV_SEQ_NO, FINANCIAL_YEAR, "
					+ "TO_CHAR(INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(EXCHG_RATE_DT, 'DD/MM/YYYY'), EXCHG_RATE_VALUE, GROSS_AMT_INR, GROSS_AMT_USD, NET_AMT_INR, TAX_AMT_INR, FLAG, INV_CUR_FLAG, SALES_PRICE_FLAG, CHECKED_FLAG, CHECKED_BY, "
					+ "TO_CHAR(CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_FLAG, APPROVED_BY, TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), PDF_INV_DTL, NEW_INV_SEQ_NO, SUPPLIER_CD, "
					+ "EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL "
					+ "FROM FMS7_INVOICE_MST WHERE CONTRACT_TYPE IN ('1', '2', '3', 'X', 'Y', 'Z', 'N') AND  (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY CUSTOMER_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "VENDOR_CD,CONTRACT_TYPE,PERIOD_START_DT,PERIOD_END_DT,INV_SEQ_NO,FINANCIAL_YEAR,SUPPLIER_CD,TIMESTAMP", conn);
			while(rset.next())
			{
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if (i == 24) {
						if (rset.getString(3).equals("X")) {
							value = rset.getString(26).equals("1") ? ("RCL"+value) : ("RCP"+value);
						}
						else if (rset.getString(3).equals("Y") && !value.contains("F")) {
							value = "F"+value;
						}
						else if (rset.getString(3).equals("Z") && rset.getString(26).equals("1")) {
							value = value.contains("F") ? value : ("F"+value);
						}
						else if (rset.getString(3).equals("Z") && rset.getString(26).equals("2")) {
							value = value.contains("P") ? value : ("P"+value);
						}
						else if (rset.getString(3).equals("1")) {
							value = value.contains("P") ? value : ("P"+value);
						}
						else if (rset.getString(3).equals("2")) {
							value = value.contains("P") ? value : ("P"+value);
						}
						else if (rset.getString(3).equals("N") && rset.getString(26).equals("1")) {
							value = value.contains("F") ? value : ("F"+value);
						}
						else if (rset.getString(3).equals("N") && rset.getString(26).equals("2")) {
							value = value.contains("P") ? value : ("P"+value);
						}
					}
					else if (i == 25) {
						value = value.equals("1") ? "SEIPL" : "HPPL";
					}
					
					cell.setCellValue("'" + value + "'");
					
				}
				
				count++;
				logger.data(fname, ( rset.getString(2) + "," + rset.getString(3)  + "," + rset.getString(4)  + "," + rset.getString(5)  + "," + rset.getString(6)  + "," + rset.getString(7)  + "," + rset.getString(24) + "," ), conn, "");
				
			}
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_OTHER_INVOICE_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_OTHER_INVOICE_MST>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_OTHER_INVOICE_MST>>");
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

	public void FMS_OTHER_INVOICE_DTL() throws SQLException,IOException 
	{
		function_nm="FMS_OTHER_INVOICE_DTL()";
		try
		{
			System.out.println("<<START>><<FMS_OTHER_INVOICE_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_OTHER_INVOICE_DTL>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="COMPANY_CD,INV_SEQ_NO,SEQ_NO,ITEM_DESCRIPTION,RATE,FLAG,TAX_DESCR,FINANCIAL_YEAR,CONTRACT_TYPE,EFF_DT,HSN_CODE,"
					+ "SAC_CODE,REMARK,REMARK1,REMARK2,REMARK3,PURCHASE_NO,REFERENCE_NO,VESSEL_CD,GRT,VENDOR_CD,VESSEL_AGENT,"
					+ "IMPORTER,QUANTITY,CARGO_DT,CARGO_AMOUNT,RATE_CGST,RATE_SGST,RATE_IGST,TAX_CD,GATE_PASS_NO,HRS_BERTHING,"
					+ "TIME_SLOTS_BERTHING,FLAG_SAC,SALE_NO,PACER_NO,VENDOR_SUPP_INV_REF,UAM_NO,CARGO_TYPE,SUPPLIER_CD,CESS_RATE,"
					+ "CESS_AMOUNT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;
			
			String seq_no = "";
			Map<String, Integer> map_id = new HashMap<String, Integer>();
			
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = "SELECT '2', INV_SEQ_NO, '0', ITEM_DESCRIPTION, RATE, FLAG, TAX_DETAILS, FINANCIAL_YEAR, CONTRACT_TYPE, TO_CHAR(EFF_DT, 'DD/MM/YYYY'), HSN_CODE, "
					+ "SAC_CODE, REMARK, REMARK1, REMARK2, REMARK3, PURCHASE_NO, REFERENCE_NO, VESSEL_CD, GRT, CUSTOMER_NM, VESSEL_AGENT, "
					+ "IMPORTER, QUANTITY, TO_CHAR(CARGO_DT, 'DD/MM/YYYY HH24:MI:SS'), CARGO_AMOUNT, RATE_CGST, RATE_SGST ,RATE_IGST, TAX_CD, GATE_PASS_NO, HRS_BERTHING, "
					+ "TIME_SLOTS_BERTHING, FLAG_SAC, SALE_NO, PACER_NO, VENDOR_SUPP_INV_REF, UAM_NO, CARGO_TYPE, SUPPLIER_CD, CESS_RATE, "
					+ "CESS_AMOUNT, ENTER_BY, TO_CHAR(ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL FROM FMS8_OTHER_INVOICE_DTL WHERE CONTRACT_TYPE != 'E' ";
			stmt = conn.prepareStatement(queryString);
//			System.out.println(queryString);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "COMPANY_CD,INV_SEQ_NO,SEQ_NO,FINANCIAL_YEAR,CONTRACT_TYPE,EFF_DT,SUPPLIER_CD,TIMESTAMP", conn);
			while(rset.next())
			{
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if (i == 2) {
						if (map_id.containsKey(rset.getString(2)+rset.getString(8)+rset.getString(9)+rset.getString(10)+rset.getString(40))) {
							seq_no = (map_id.get(rset.getString(2)+rset.getString(8)+rset.getString(9)+rset.getString(10)+rset.getString(40)) + 1) + "";
							value = seq_no;
							map_id.put(rset.getString(2)+rset.getString(8)+rset.getString(9)+rset.getString(10)+rset.getString(40), Integer.parseInt(seq_no));
						}
						else {
							seq_no = "0";
							map_id.put(rset.getString(2)+rset.getString(8)+rset.getString(9)+rset.getString(10)+rset.getString(40), Integer.parseInt(seq_no));
						}
					}
					else if (i == 6) {
						if (value.contains("CGST")) {
							value = value.replace("%", "%, ");
						}
						value = value.replaceAll("-", " ");
					}
					else if (i==20) {
						queryString1 = "SELECT VENDOR_CD FROM FMS8_VENDOR_MST WHERE VENDOR_NAME = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, value);
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							value = rset1.getString(1);
						}
						else {
							value = "0";
						}
						rset1.close();
						stmt1.close();
					}
					else if (i==39) {
						if (value.equals("1")) {
							value = "SEIPL";
						}
						else {
							value = "HPPL";
						}
					}
					
					cell.setCellValue("'" + value + "'");
					
				}
				
				count++;
				logger.data(fname, ( rset.getString(1) + "," + seq_no + "," + rset.getString(2)  + "," + rset.getString(7)  + "," + rset.getString(8)  + "," + rset.getString(9)  + "," + rset.getString(39) + "," ), conn, "");
				
			}
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_OTHER_INVOICE_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_OTHER_INVOICE_DTL>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_OTHER_INVOICE_DTL>>");
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
			// LOGGER.log(Level.WARNING, "Error in Vendor_SEIPL_Data_Extractor.java -> Vendor_Excel_Extractor -> getmail_list() ", e);
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
