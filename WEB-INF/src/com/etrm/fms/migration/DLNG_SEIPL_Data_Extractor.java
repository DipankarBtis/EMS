
package com.etrm.fms.migration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.SystemErrorLogger;


public class DLNG_SEIPL_Data_Extractor {

	String db_src_file_name = "DLNG_SEIPL_Data_Extractor.java";
	String function_nm = "";
	
	String migration_setup_dir = "";
	
	String sysDateTime = "";
	
	String fname = "";
	String fname_error = "";
	
	String fname1 = "";

	DataMigration_Logger logger = new DataMigration_Logger();
	Migration_Plants_Exceptions mpe =new Migration_Plants_Exceptions();

	String queryString = "", queryString1 = "", queryString2 = "", queryString3="", queryString4="" ;
	Connection conn;
	ResultSet rset, rset1, rset2, rset3, rset4;
	PreparedStatement stmt, stmt1, stmt2, stmt3, stmt4;

	String dbline = "", username = "", encrypted = "", password = "";
	String columns = "", filename = "", value = "";
	
	String checked_values = "", msg = "", msg_type = "",abbr="",map_id="";
	String cont_no="",cont_rev="",cont_type="",agmt_type="",cont_name="",fin_yr="",inv_seq="",tcs_tds="",state_code="",tcs_amt="",tds_amt="",tcs_date="",tds_date="",tax_code="";
	
	final String company_cd = "2";
	String cd = "", eff_dt = "",seq_no = "",no="",sn_req="",sn_close="",sn_dt="",sn_qty="",cont_ref="",rev="",map="",p_seq_no="", name="",cargo_ref="",dom_flag="",dom_flag1="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	
	String delta_FromDt = null;
	String delta_ToDt = null;
	String start_end_dt = null;
	
	String dir_flag = "N";

	int nrow = 0,ncell=0;
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
			makeDirectory();
			
			fname = "DataLogs/Extractor/DLNG_SEIPL_Data_Extractor(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Extractor/DLNG_SEIPL_Data_Extractor_Error(log)"+sysDateTime+".csv";
			
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
	    		
				if (checked_values.contains("FMS_TRUCK_TRANSPORTER_MST,")) {
					FMS_TRUCK_TRANSPORTER_MST();
				}
				if (checked_values.contains("FMS_TRUCK_TRANS_CONTACT_MST,")) {
					FMS_TRUCK_TRANS_CONTACT_MST();
				}
				if (checked_values.contains("FMS_TRUCK_MST,")) {
					FMS_TRUCK_MST();
				}
				if (checked_values.contains("FMS_TRUCK_DRIVER_MST,")) {
					FMS_TRUCK_DRIVER_MST();
				}
				if (checked_values.contains("FMS_FILLING_STATION_MST,")) {
					FMS_FILLING_STATION_MST();
				}
				if (checked_values.contains("FMS_CHECKPOST_MST,")) {
					FMS_CHECKPOST_MST();
				}
				if (checked_values.contains("FMS_TRUCK_TRANSPORTER_LINK,")) {
					FMS_TRUCK_TRANSPORTER_LINK();
				}
				if (checked_values.contains("FMS_TRUCK_DRIVER_TRANS_LINK,")) {
					FMS_TRUCK_DRIVER_TRANS_LINK();
				}
				if (checked_values.contains("FMS_TRUCK_DRIVER_LINK,")) {
					FMS_TRUCK_DRIVER_LINK();
				}
				if (checked_values.contains("FMS_LINK_CHECKPOST_PLANT,")) {
					FMS_LINK_CHECKPOST_PLANT();
				}
				if (checked_values.contains("FMS_AGMT_MST(DLNG),")) {
					FMS_AGMT_MST();
				}
				if (checked_values.contains("FMS_AGMT_FILLING_STN,")) {
					FMS_AGMT_FILLING_STN();
				}
				if (checked_values.contains("FMS_AGMT_PLANT(DLNG),")) {
					FMS_AGMT_PLANT();
				}
				if (checked_values.contains("FMS_AGMT_BILLING_DTL(DLNG),")) {
					FMS_AGMT_BILLING_DTL();
				}
				if (checked_values.contains("FMS_AGMT_TRUCK_TRANS,")) {
					FMS_AGMT_TRUCK_TRANS();
				}
				if (checked_values.contains("FMS_SUPPLY_CONT_MST(DLNG),")) {
					FMS_SUPPLY_CONT_MST();
				}
				if(checked_values.contains("FMS_SUPPLY_CONT_PLANT(DLNG),")) {
					FMS_SUPPLY_CONT_PLANT();
				}
				if (checked_values.contains("FMS_SUPPLY_CONT_FILLING_STN,")) {
					FMS_SUPPLY_CONT_FILLING_STN();
				}
				if (checked_values.contains("FMS_SUPPLY_CONT_LIABILITY(DLNG),")) { 
					FMS_SUPPLY_CONT_LIABILITY(); 					
				}
				if (checked_values.contains("FMS_SUPPLY_BILLING_DTL(DLNG),")) {
					FMS_SUPPLY_BILLING_DTL();
				}
				if (checked_values.contains("FMS_SUPPLY_CFORM_DTL(DLNG),")) {
					FMS_SUPPLY_CFORM_DTL();
				}
				if (checked_values.contains("FMS_CFORM_MST,")) {
					FMS_CFORM_MST();
				}
				if (checked_values.contains("FMS_CFORM_DTL,")) {
					FMS_CFORM_DTL();
				}
				if (checked_values.contains("FMS_SUPPLY_CONT_TRUCK_TRANS,")) {
					FMS_SUPPLY_CONT_TRUCK_TRANS();
				}
				if (checked_values.contains("FMS_SECURITY_DEAL_MAP(DLNG),")) {
					FMS_SECURITY_DEAL_MAP();
				}
				if (checked_values.contains("FMS_SECURITY_MST(DLNG),")) {
					FMS_SECURITY_MST();
				}
				
				if (checked_values.contains("LOG_FMS_SECURITY_MST(D),")) {
					LOG_FMS_SECURITY_MST();
				}
				
				if (checked_values.contains("FMS_SUPPLY_CONT_DCQ_DTL(DLNG),")) {
					FMS_SUPPLY_CONT_DCQ_DTL();
				}
				if (checked_values.contains("FMS_SUPPLY_PURCHASE_MAP_DTL(DLNG),")) {
					FMS_SUPPLY_PURCHASE_MAP_DTL();
				}
				if (checked_values.contains("FMS_SUPPLY_ALLOC_REVISED(DLNG),")) {
					FMS_SUPPLY_ALLOC_REVISED();
				}
				if (checked_values.contains("FMS_CONT_PRICE_DTL(DLNG),")) { 
					FMS_CONT_PRICE_DTL();
				}
				if (checked_values.contains("FMS_CONT_PRICE_MIN_DTL(DLNG),")) {
					FMS_CONT_PRICE_MIN_DTL();
				}
                if(checked_values.contains("FMS_AGMT_SVC_MST,")) {
					FMS_AGMT_SVC_MST();
				}
				if(checked_values.contains("FMS_SVC_CONT_MST,")) {
					FMS_SVC_CONT_MST();
				}
				if(checked_values.contains("FMS_SVC_CONT_MAP,")) {
					FMS_SVC_CONT_MAP();
				}
				if (checked_values.contains("FMS_DLNG_BUYER_NOM(D),")) {
					FMS_DLNG_BUYER_NOM();
				}
				if (checked_values.contains("FMS_DLNG_BUYER_NOM_DTL(D),")) {
					FMS_DLNG_BUYER_NOM_DTL();
				}	
//				if (checked_values.contains("FMS_DAILY_SELLER_NOM(DLNG),")) {
//					FMS_DAILY_SELLER_NOM();
//				}
				if (checked_values.contains("FMS_DLNG_SELLER_NOM_DTL(D),")) {
					FMS_DLNG_SELLER_NOM_DTL();
				}
				if (checked_values.contains("FMS_DLNG_ALLOC_MST(D),")) {     
					FMS_DLNG_ALLOC_MST();
				}
				if (checked_values.contains("FMS_DLNG_INVOICE_MST,")) {
					FMS_DLNG_INVOICE_MST();
				}
				if (checked_values.contains("FMS_DLNG_INV_FILE_DTL,")) {
					FMS_DLNG_INV_FILE_DTL();
				}
				if (checked_values.contains("FMS_DLNG_INV_PAY_RECV_DTL,")) {
					FMS_DLNG_INV_PAY_RECV_DTL();
				}
				if (checked_values.contains("FMS_DLNG_FFLOW_INV_MST,")) {
					FMS_DLNG_FFLOW_INV_MST();
				}
				if (checked_values.contains("FMS_DLNG_FFLOW_INV_DTL,")) {
					FMS_DLNG_FFLOW_INV_DTL();
				}
				if (checked_values.contains("FMS_DLNG_FFLOW_INV_FILE_DTL,")) {
					FMS_DLNG_FFLOW_INV_FILE_DTL();
				}
				if (checked_values.contains("FMS_DLNG_SVC_INVOICE_MST,")) {
					FMS_DLNG_SVC_INVOICE_MST();
				}
				if (checked_values.contains("FMS_DLNG_SVC_INVOICE_DTL,")) {
					FMS_DLNG_SVC_INVOICE_DTL();
				}
				if (checked_values.contains("FMS_DLNG_SVC_INV_FILE_DTL,")) {
					FMS_DLNG_SVC_INV_FILE_DTL();
				} 
				if (checked_values.contains("FMS_DLNG_INVOICE_MST_CR_DR,")) { 
                	FMS_DLNG_INVOICE_MST_CR_DR();
				}
				if (checked_values.contains("FMS_DLNG_INV_CRDR_REF,")) { 
                	FMS_DLNG_INV_CRDR_REF();
				}
				if (checked_values.contains("FMS_DLNG_INV_CRDR_FILE_DTL,")) { 
					FMS_DLNG_INV_CRDR_FILE_DTL();
				}
				if (checked_values.contains("FMS_DLNG_INVOICE_MST_LP,")) {
					FMS_DLNG_INVOICE_MST_LP();
				}
				if (checked_values.contains("FMS_DLNG_INV_FILE_DTL_LP,")) {
					FMS_DLNG_INV_FILE_DTL_LP();
				}
				if (checked_values.contains("FMS_DLNG_FFLOW_INV_MST_SERV,")) {
					FMS_DLNG_FFLOW_INV_MST_SERV();
				}
				if (checked_values.contains("FMS_DLNG_FFLOW_INV_FILE_DTL_SERV,")) {
					FMS_DLNG_FFLOW_INV_FILE_DTL_SERV();
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
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (rset1 != null) {
					try {
						rset1.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (rset2 != null) {
					try {
						rset2.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (rset3 != null) {
					try {
						rset3.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (rset4 != null) {
					try {
						rset4.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt1 != null) {
					try {
						stmt1.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt2 != null) {
					try {
						stmt2.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt3 != null) {
					try {
						stmt3.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt4 != null) {
					try {
						stmt4.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				
			} catch (Exception e) {
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				
				msg = "One of the Functions faced an Error. Extraction Terminated.";
				msg_type = "E";
			}
		}

	}

	public void FMS_TRUCK_TRANSPORTER_MST() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_TRANSPORTER_MST()";
		
		try {

			System.out.println("<<START>><<FMS_TRUCK_TRANSPORTER_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TRUCK_TRANSPORTER_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "TRUCK_TRANS_CD,EFF_DT,TRUCK_TRANS_NAME,TRUCK_TRANS_ABBR,ADDR,STATE,CITY,PIN,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";

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
			
			queryString = " SELECT TRANS_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), TRANS_NAME, TRANS_ABBR, WEB_ADDR, STATE, CITY, PINCODE, FLAG, '2', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM DLNG_TRANS_MST ORDER BY TRANS_CD";
			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "TRUCK_TRANS_CD,EFF_DT,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(1);
				eff_dt = rset.getString(2);
				
				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value.replaceAll("", "");
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cd + "," + eff_dt + ","), conn, "");

			}

			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_TRUCK_TRANSPORTER_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ","), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_TRANSPORTER_MST>>,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_TRUCK_TRANSPORTER_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_TRUCK_TRANS_CONTACT_MST() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_TRANS_CONTACT_MST()";
		
		try {

			System.out.println("<<START>><<FMS_TRUCK_TRANS_CONTACT_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TRUCK_TRANS_CONTACT_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,TRUCK_TRANS_CD,SEQ_NO,EFF_DT,CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,"
					+ "NOM_FLAG,JT_FLAG,INV_FLAG,RM_FLAG,FM_FLAG,PM_FLAG,OTHER_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ADDR_IS_ACTIVE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String addr="";
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT '2' ,TRANS_CD, SEQ_NO, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), CONTACT_PERSON, CONTACT_DESIG, PHONE, MOBILE, FAX_1, FAX_2, EMAIL, 'R', ADDL_ADDR_LINE, DEF_NOM_FLAG, DEF_JT_FLAG, DEF_INV_FLAG, NULL , DEF_FM_FLAG, DEF_PM_FLAG, DEF_OTHER_FLAG, ACTIVE_FLAG, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), EMP_CD, NULL, NULL, ACTIVE_FLAG FROM DLNG_TRANSPORTER_CONTACT_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY TRANS_CD";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,TRUCK_TRANS_CD,SEQ_NO,EFF_DT,ADDR_FLAG,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(2);
				seq_no = rset.getString(3);
				eff_dt = rset.getString(4);
				addr = rset.getString(12);
				
				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, ( company_cd + "," + cd + "," + seq_no + "," + eff_dt + ","+addr+","), conn, "");

			}

			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_TRUCK_TRANS_CONTACT_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_TRANS_CONTACT_MST>>,,,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_TRUCK_TRANS_CONTACT_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_TRUCK_MST() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_MST()";
		
		try {

			System.out.println("<<START>><<FMS_TRUCK_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TRUCK_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "TRUCK_CD,EFF_DT,TRUCK_REG_NUM,TRUCK_TYPE,TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";

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

			queryString = " SELECT TRUCK_ID, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), TRUCK_NM, TRUCK_TYPE, TANK_VOL_M3, TANK_VOL_TON, LOAD_CAP, STATUS, '2', '1',"
					+ " TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL , NULL, NULL FROM DLNG_TANK_TRUCK_MST ";
			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ENT_PROFILE,TRUCK_CD,EFF_DT,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(1);
				eff_dt = rset.getString(2);
				
				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "");

			}

			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_TRUCK_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_MST>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_TRUCK_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}
	
	public void FMS_TRUCK_DRIVER_MST() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_DRIVER_MST()";
		
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_DRIVER_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TRUCK_DRIVER_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "DRIVER_CD,DRIVER_NAME,DRIVER_ADDR,DRIVER_DOB,DRIVER_STATUS,DRIVER_MOBILE,LICENCE_NO,LICENCE_TYPE,LICENCE_FROM_DT,"
					+ "LICENCE_TO_DT,LICENCE_ISSUE_STATE,LICENCE_FILE_NAME,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE,EFF_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String addr = "";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT NULL, DRIVER_NAME, ADDRESS, TO_CHAR(DOB, 'DD/MM/YYYY HH24:MI:SS'), STATUS, CONTACT_NO, LICENSE_NO, LICENSE_TYPE,"
					+ " TO_CHAR(LICENSE_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(LICENSE_END_DT, 'DD/MM/YYYY HH24:MI:SS'), LICENSE_ISSUE_ST_CD, NULL, '2', ENT_BY,"
					+ " TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL , NULL, NULL, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM DLNG_DRIVER_MST"
					+ " WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ENT_PROFILE,LICENCE_NO,TIMESTAMP,", conn);
			
			while (rset.next()) {
				addr = rset.getString(3);
				no = rset.getString(7);
				
				row = spreadsheet.createRow(nrow++);
				value = "";
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					if(i == 11) {
						byte[] blobData = rset.getBytes(i + 1);
						if(blobData!=null) {
						String blob = new String(blobData, StandardCharsets.UTF_8);
						value = blob;
						}
						else {
							value = "";
						}
					}
					else {
						value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					}
					value = value.trim().equals("") ? "null" : value;
					value = value.replaceAll("\n", " ");
					value = value.replaceAll("\r", " ");
					
					
					if(i == 6 && no.length() > 15) {
						no = no.replaceAll(" ", "");
						if(no.length()>15) {
						no = no.substring(0,14);
						}
						value = no;
					}
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + no + "," ), conn, "");
				
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_DRIVER_MST>>,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TRUCK_DRIVER_MST>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_FILLING_STATION_MST() throws SQLException, IOException {
		function_nm = "FMS_FILLING_STATION_MST()";
		
		try {
			
			System.out.println("<<START>><<FMS_FILLING_STATION_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_FILLING_STATION_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_CD,FILL_STATION_CD,EFF_DT,FILL_STATION_NAME,FILL_STATION_ABBR,ACTIVE_FLAG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
					
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String fill_cd = "",entity_type = "";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT  NULL, STATION_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), STATION_NAME, STATION_ABBR, FLAG, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL  FROM DLNG_SUPPLY_STATION_MST ";
			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_CD,FILL_STATION_CD,EFF_DT,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(1);
//				entity_type = rset.getString(3);
				fill_cd = rset.getString(4);
				eff_dt = rset.getString(5);
				
				row = spreadsheet.createRow(nrow++);
				value = "";
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
					logger.data(fname, (cd + "," + fill_cd + "," + eff_dt + "," ), conn, "");
				
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_FILLING_STATION_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_FILLING_STATION_MST>>,,,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_FILLING_STATION_MST>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	
	
	public void FMS_CHECKPOST_MST() throws SQLException, IOException {
		function_nm = "FMS_CHECKPOST_MST()";
		
		try {
			
			System.out.println("<<START>><<FMS_CHECKPOST_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_CHECKPOST_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "CHKPOST_CD,CHKPOST_NAME,EFF_DT,STATE_CODE,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			
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
			
			queryString = " SELECT NULL, CHKPOST_NAME, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), STATE_CODE, 'Y', '2', ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM DLNG_CHECKPOST_MST ";
			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ENT_PROFILE,CHKPOST_CD,EFF_DT,TIMESTAMP,", conn);
			
			while (rset.next()) {
				no = rset.getString(1);
				eff_dt = rset.getString(3);
				row = spreadsheet.createRow(nrow++);
				value = "";
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + no + "," + eff_dt + " , " ), conn, "");
				
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_CHECKPOST_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_CHECKPOST_MST>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_CHECKPOST_MST>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_TRUCK_TRANSPORTER_LINK() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_TRANSPORTER_LINK()";
		
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_TRANSPORTER_LINK>>");
            logger.checkpoint(fname, "<<START>>,<<FMS_TRUCK_TRANSPORTER_LINK>>,,,", conn); 			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "TRUCK_TRANS_CD,TRUCK_CD,LINK_CD,EFF_DT,RELEASE_DT,REMARKS,R_REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			String status="",lic="";
			int rev_no=0;
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			Map<String, Integer> link_seq = new HashMap<String, Integer>();
			int seq = 1;
			
			nrow = 0;
			count = 0;
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT TRANS_CD, TRUCK_ID, STATUS, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, '2', '1',"
					+ " TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM DLNG_TANK_TRUCK_MST ";
			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
            logger.checkpoint(fname, "ENT_PROFILE,TRANS_CD,TRUCK_CD,EFF_DT,LINK_SEQ,TIMESTAMP,", conn);              			
			while (rset.next()) {
				cd = rset.getString(1);
				no = rset.getString(2);
				status = rset.getString(3);
				eff_dt = rset.getString(4);
//				rev = rset.getString(5);
//				rev_no = Integer.parseInt(rev);
//				lic = rset.getString(6);
//				if (status.equals("Y")) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					for (int i = 0; i < columns.split(",").length; i++) {

						cell = row.createCell(i);
						value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null"
								: rset.getString(i + 1).trim().replaceAll("'", "");
						value = value.trim().equals("") ? "null" : value;

						if (i == 0) {
							queryString2 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, cd);
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								value = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
						} 
						else if (i == 1) {
//							queryString2 = "SELECT TRUCK_NM FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_ID = ? ";
//							stmt2 = conn.prepareStatement(queryString2);
//							stmt2.setString(1, no);
//							rset2 = stmt2.executeQuery();
//							if (rset2.next()) {
//								value = rset2.getString(1);
//							}
//							rset2.close();
//							stmt2.close();
							
							value = no;
						}
						else if(i == 2) {
							if (link_seq.containsKey(no)) {
								seq=link_seq.get(no);
								seq=seq+1;
								link_seq.put(no,seq);
							} 
							else {
								seq= 1;
								link_seq.put(no,seq);
							}
				    		value = seq+"";
						}

						else if(i == 3) {
							value = eff_dt;
						}

						 

						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (company_cd + "," + cd + "," + no + "," + eff_dt + "," + seq + ","), conn, "");
//				}
				
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_TRUCK_TRANSPORTER_LINK_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_TRANSPORTER_LINK>>,,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TRUCK_TRANSPORTER_LINK>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_TRUCK_DRIVER_TRANS_LINK() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_DRIVER_TRANS_LINK()";
		
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_DRIVER_TRANS_LINK>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TRUCK_DRIVER_TRANS_LINK>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "TRUCK_TRANS_CD,DRIVER_CD,LINK_SEQ,EFF_DT,RELEASE_DT,REMARKS,R_REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			Map<String, Integer> link_seq = new HashMap<String, Integer>();
			int seq = 1;
			nrow = 0;
			count = 0;
			String status="",lic="";
			int rev_no = 0;
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT TRANS_CD, LICENSE_NO, STATUS, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, '2', ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, "
					+ "NULL, NULL FROM DLNG_DRIVER_MST ";
			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ENT_PROFILE,TRANS_CD,LICENSE_NO,EFF_DT,LINK_SEQ,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(1);
				lic = rset.getString(2);
				status = rset.getString(3);
				eff_dt = rset.getString(4);
//				rev = rset.getString(5);
//				rev_no = Integer.parseInt(rev);
//				no = rset.getString(6);
				
//				if (status.equals("Y")) {
					
				row = spreadsheet.createRow(nrow++);
				value = "";
					for (int i = 0; i < columns.split(",").length; i++) {

						cell = row.createCell(i);
						value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null"
								: rset.getString(i + 1).trim().replaceAll("'", "");
						value = value.trim().equals("") ? "null" : value;

						if (i == 0) {
							queryString2 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, cd);
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								value = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
						}
						else if(i == 1) {
							value = lic.replaceAll(" ", "");
							if(value.length()>15) {
								value = value.substring(0,14);
							}
//							value = lic;
						}
						else if(i == 2) {
							if (link_seq.containsKey(lic)) {
								seq=link_seq.get(lic);
								seq=seq+1;
								link_seq.put(lic,seq);
							} 
							else {
								seq= 1;
								link_seq.put(lic,seq);
							}
				    		value = seq+"";
						}
						
						else if(i == 3) {
							value = eff_dt;
						}


						cell.setCellValue("'" + value + "'");
					} 
				
				count++;
				logger.data(fname, (company_cd + "," + cd + "," + lic + "," + eff_dt + "," + seq + ","), conn, "");
//				}
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_TRANS_LINK_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_DRIVER_TRANS_LINK>>,,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TRUCK_DRIVER_TRANS_LINK>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_TRUCK_DRIVER_LINK() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_DRIVER_LINK()";
		
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_DRIVER_LINK>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TRUCK_DRIVER_LINK>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "TRUCK_CD,DRIVER_CD,LINK_SEQ,EFF_DT,RELEASE_DT,REMARKS,R_REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			Map<String, Integer> link_seq = new HashMap<String, Integer>();
			int seq = 1;
			nrow = 0;
			count = 0;
			String status="",lic="";
			int rev_no=0;
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT TRUCK_ID, LICENSE_NO, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, STATUS, TRANS_CD, NULL, '2', ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),"
					+ " NULL, NULL, NULL FROM DLNG_TRUCK_DRIVER_LINK_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ORDER BY STATUS, ENT_DT ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ENT_PROFILE,TRUCK_CD,DRIVER_CD,EFF_DT,TIMESTAMP,", conn);
			
			while (rset.next()) {
				no = rset.getString(1);
				cd = rset.getString(6);
				eff_dt = rset.getString(3);
//				rev = rset.getString(4);
//				rev_no = Integer.parseInt(rev);
				status = rset.getString(5);
				lic = rset.getString(2);
				
//				if (status.equals("Y")) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					for (int i = 0; i < columns.split(",").length; i++) {

						cell = row.createCell(i);
						value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null"
								: rset.getString(i + 1).trim().replaceAll("'", "");
						value = value.trim().equals("") ? "null" : value;

						if (i == 0) {
//							queryString2 = "SELECT TRUCK_NM FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_ID = ? ";
//							stmt2 = conn.prepareStatement(queryString2);
//							stmt2.setString(1, cd);
//							rset2 = stmt2.executeQuery();
//							if (rset2.next()) {
//								value = rset2.getString(1);
//							}
//							rset2.close();
//							stmt2.close();
							value = no;
						}
						else if(i == 1) {
							value = lic.replaceAll(" ", "");
							if(value.length()>15) {
								value = value.substring(0,14);
							}
//							value = lic;
						}
						else if(i == 2) {
							if (link_seq.containsKey(lic)) {
								seq=link_seq.get(lic);
								seq=seq+1;
								link_seq.put(lic,seq);
							} 
							else {
								seq= 1;
								link_seq.put(lic,seq);
							}
				    		value = seq+"";
						}
						else if(i == 3) {
							value = eff_dt;
						}

						else if (i == 4) { //EFF_DT
							if(status.equals("N")){
								queryString2 = "SELECT TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),A.REV_NO FROM DLNG_TRUCK_DRIVER_LINK_DTL A WHERE A.TRUCK_ID = ? "
										+ "AND A.TRANS_CD = ? AND A.LICENSE_NO = ? AND A.STATUS = ? AND A.REV_NO = (SELECT MAX(C.REV_NO) FROM DLNG_TRUCK_DRIVER_LINK_DTL C "
										+ "WHERE C.TRUCK_ID = A.TRUCK_ID AND C.TRANS_CD = A.TRANS_CD AND C.LICENSE_NO = A.LICENSE_NO AND C.STATUS = A.STATUS )";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, no);
								stmt2.setString(2, cd);
								stmt2.setString(3, lic);
								stmt2.setString(4, status);
								rset2 = stmt2.executeQuery();
								if (rset2.next() && rset2.getInt(2) > 1 ) {
									value = rset2.getString(1);
								}
								else if(rset2.getInt(2) == 1) {
									value = eff_dt;
								}
								else {
									value = null;
								}
								rset2.close();
								stmt2.close();
							}
							else {
								value = null;
							}
						
					}
						
//						else if (i == 4) {
//							value = status + "-" + rev_no;
//						} 
						
						else if (i == 5) {
							if (status.equals("Y")) {
								value = status + "-" + lic + "-FMS reference";
							} else {
								value = null;
							}
						}
						
						else if (i == 6) {
							if (status.equals("N")) {
								value = status + "-" + lic + "-FMS reference";
							} else {
								value = null;
							}
						}

						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (company_cd + "," + cd + "," + lic + "," + eff_dt + " , "), conn, "");
//				}
				
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_LINK_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_DRIVER_LINK>>,,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TRUCK_DRIVER_LINK>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_LINK_CHECKPOST_PLANT() throws SQLException, IOException {
		function_nm = "FMS_LINK_CHECKPOST_PLANT()";
		
		try {
			
			System.out.println("<<START>><<FMS_LINK_CHECKPOST_PLANT>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_LINK_CHECKPOST_PLANT>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,PLANT_SEQ_NO,CHKPOST_CD,REV_SEQ,EFF_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,RELEASE_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			Map<String, Integer> rev_seq = new HashMap<String, Integer>();
			nrow = 0;
			count = 0;
			String  type = "",ch_cd = "",combination="",plant="";
			int seq=0;
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT '2', CUSTOMER_CD, 'C', PLANT_CD, CHKPOST_CD, '0',TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM DLNG_LINK_CUST_CHECKPOST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY ENT_DT";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,PLANT_SEQ_NO,CHKPOST_CD,EFF_DT,REV_SEQ,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(2);
				type = rset.getString(3);
				plant = rset.getString(4);
				ch_cd = rset.getString(5);
				eff_dt = rset.getString(7);
				
				
				
				row = spreadsheet.createRow(nrow++);
				value = "";
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					
					if(i == 1) {
						queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							cd = value;
						}
						rset2.close();
						stmt2.close();
						map = cd+"-"+plant;
						if(mpe.customer_map.containsKey(map))
						{
							plant=mpe.customer_map.get(map);
						}

						plant=plant.split("-")[0];
						if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					    }
					}
					
					else if(i == 3) {
						value = plant;
					}
					
					else if( i == 4) {
						queryString2 = "SELECT CHKPOST_NAME FROM DLNG_CHECKPOST_MST WHERE CHKPOST_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, ch_cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							ch_cd = value;
						}
						rset2.close();
						stmt2.close();
					}
					
					else if(i == 5) {
						combination = cd+"-"+plant+"-"+eff_dt;
						if (rev_seq.containsKey(combination)) {
							seq=rev_seq.get(combination);
							seq=seq+1;
							rev_seq.put(combination,seq);
						} 
						else {
							seq= 0;
							rev_seq.put(combination,seq);
						}
			    		value = seq+"";
					}
					
					
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + cd + "," + type + "," + plant + " , " + ch_cd + "," + eff_dt + "," + seq + "," ), conn, "");
				
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_LINK_CHECKPOST_PLANT_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_LINK_CHECKPOST_PLANT>>,,,,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_LINK_CHECKPOST_PLANT>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_AGMT_MST() throws SQLException, IOException {
		function_nm = "FMS_AGMT_MST(DLNG)()";
		
		try {
			
			System.out.println("<<START>><<FMS_AGMT_MST(DLNG)>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_AGMT_MST(DLNG)>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,SIGNING_DT,START_DT,END_DT,RENEWAL_DT,AGMT_BASE,AGMT_TYP,STATUS,"
					+ "BUYER_NOM_CLAUSE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_CLAUSE,SELLER_NOM,SELLER_MONTH_NOM,"
					+ "SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,MEASUREMENT,MEAS_STANDARD,"
					+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
					+ "ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,FLAG,REV_DT,REMARKS,LIABILITY_CLAUSE,BILLING_CLAUSE,LC_CLAUSE,RENEWAL_FLAG,PRE_APPROVAL_DATE,"
					+ "PRE_APPROVAL,PRE_APPROVAL_BY,REOPEN_REQUEST_FLAG,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVAL_DT,REOPEN_REQUEST_BY,"
					+ "REOPEN_APPROVE_BY,REMARK,CONT_NAME,AGMT_REF_NO,BILLING_FLAG,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,MEAS_CLAUSE,"
					+ "SPEC_CLAUSE,LIABILITY,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String  type = "",counterparty="",abbr="",clause="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT '2', CUSTOMER_CD, 'D', FLSA_NO, REV_NO, TO_CHAR(SIGNING_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(START_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(END_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEWAL_DT, 'DD/MM/YYYY HH24:MI:SS'), FLSA_BASE, FLSA_TYPE, STATUS, BUYER_NOM_CLAUSE, BUYER_NOM, BUYER_MONTH_NOM, BUYER_WEEK_NOM, BUYER_DAILY_NOM, SELLER_NOM_CLAUSE, SELLER_NOM, SELLER_MONTH_NOM, SELLER_WEEK_NOM, SELLER_DAILY_NOM, DAY_DEF, DAY_START_TIME, DAY_END_TIME, MDCQ, MDCQ_PERCENTAGE, MEASUREMENT, MEAS_STANDARD, MEAS_TEMPERATURE, PRESSURE_MIN_BAR, PRESSURE_MAX_BAR, OFF_SPEC_GAS, SPEC_GAS_ENERGY_BASE, SPEC_GAS_MIN_ENERGY, SPEC_GAS_MAX_ENERGY, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, FLAG, TO_CHAR(REV_DT, 'DD/MM/YYYY HH24:MI:SS'), REMARKS, LIABILITY_CLAUSE, BILLING_CLAUSE, LC_CLAUSE, RENEWAL_FLAG, TO_CHAR(PRE_APPROVAL_DATE, 'DD/MM/YYYY HH24:MI:SS'), PRE_APPROVAL, PRE_APPROVAL_BY, REOPEN_REQUEST_FLAG, TO_CHAR(REOPEN_REQUEST_DT, 'DD/MM/YYYY HH24:MI:SS'), REOPEN_APPROVAL_FLAG, TO_CHAR(REOPEN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), REOPEN_REQUEST_BY, REOPEN_APPROVE_BY, REMARK, NULL,NULL,'N',NULL,NULL, '00:00',NULL,NULL,'N','N',NULL,'N','N'  FROM DLNG_FLSA_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(2);
				type = rset.getString(3);
				no = rset.getString(4);
				rev = rset.getString(5);
				row = spreadsheet.createRow(nrow++);
				value = "";
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					
					if(i == 1) {	//COUNTERPARTY_CD
						queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							if(mpe.counterparty_map.containsKey(value)) {
								value = mpe.counterparty_map.get(value);
							}
							counterparty = value;
						}
						rset2.close();
						stmt2.close();
					}
					else if(i == 33) {	//SPEC_GAS_ENERGY_BASE
						if(value.equals("1")) {
							value = "GCV";
						}
						else {
							value = "NCV";
						}
					}
					else if(i == 57) {	//CONT_NAME
						value = "SEIPL-" + counterparty + "-"+ rset.getString(3) + no + "-" + "REV" + rev;
					}
					
					else if(i == 58) {	//AGMT_REF_NO
						value = counterparty + "-" + no + "-" + rev;
					}
					
					else if(i == 59) {	//BILLING_FLAG
						queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE FLSA_NO = ? AND REV_NO = ? AND BUYER_CD = ?  AND CLAUSE_CD = '6' ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, no);
						stmt2.setString(2, rev);
						stmt2.setString(3, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							clause = rset2.getString(1);
						}
						else {
							clause = "0";
						}
						rset2.close();
						stmt2.close();
						
						if(clause.equals("6")) {
							value = "Y";
						}
					}
					else if(i == 65) {	//LIABILITY
						queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE FLSA_NO = ? AND REV_NO = ? AND BUYER_CD = ?  AND CLAUSE_CD = '1' ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, no);
						stmt2.setString(2, rev);
						stmt2.setString(3, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							clause = rset2.getString(1);
						}
						else {
							clause = "0";
						}
						rset2.close();
						stmt2.close();
						
						if(clause.equals("1")) {
							value = "Y";
						}
					}
					
					else if(i == 66) {	//TERMINATE_FLAG
						queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE FLSA_NO = ? AND REV_NO = ? AND BUYER_CD = ?  AND CLAUSE_CD = '9' ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, no);
						stmt2.setString(2, rev);
						stmt2.setString(3, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							clause = rset2.getString(1);
						}
						else {
							clause = "0";
						}
						rset2.close();
						stmt2.close();
						
						if(clause.equals("9")) {
							value = "Y";
						}
					}
					
					else if(i == 68) {	//TERMINATE_PLANED
						queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE FLSA_NO = ? AND REV_NO = ? AND BUYER_CD = ?  AND CLAUSE_CD = '7'  ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, no);
						stmt2.setString(2, rev);
						stmt2.setString(3, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							clause = rset2.getString(1);
						}
						else {
							clause = "0";
						}
						rset2.close();
						stmt2.close();
						
						if(clause.equals("7")) {
							value = "Y";
						}
					}
					
					else if(i == 69) {	//TERMINATE_FORCE
						queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE FLSA_NO = ? AND REV_NO = ? AND BUYER_CD = ?  AND  CLAUSE_CD = '8' ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, no);
						stmt2.setString(2, rev);
						stmt2.setString(3, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							clause = rset2.getString(1);
						}
						else {
							clause = "0";
						}
						rset2.close();
						stmt2.close();
						
						if(clause.equals("8")) {
							value = "Y";
						}
					}
					
					
//					if (mpe.counterparty_map.containsKey(value)) {
//						value =mpe.counterparty_map.get(value); 
//					}
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + counterparty + "," + type + "," + no + " , " + rev + "," ), conn, "");
				
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_AGMT_MST(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_MST(DLNG)>>,,,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_AGMT_MST(DLNG)>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_AGMT_FILLING_STN() throws SQLException, IOException {
		function_nm = "FMS_AGMT_FILLING_STN()";
		
		try {
			
			System.out.println("<<START>><<FMS_AGMT_FILLING_STN>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_AGMT_FILLING_STN>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,FILL_STATION_CD,ENT_BY,ENT_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String  type = "",counterparty="",abbr="",clause="",fill_cd="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT '2', A.CUSTOMER_CD, 'D', A.FLSA_NO, A.REV_NO, A.TRANSPORTER_CD, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM "
					+ "DLNG_FLSA_TRANSPORTER_MST A, DLNG_FLSA_MST B WHERE A.FLSA_NO = B.FLSA_NO AND A.CUSTOMER_CD = B.CUSTOMER_CD AND A.REV_NO = B.REV_NO AND"
					+ "(? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,FILL_STATION_CD,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(2);
				type = rset.getString(3);
				no = rset.getString(4);
				rev = rset.getString(5);
				row = spreadsheet.createRow(nrow++);
				value = "";
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					
					if(i == 1) {	//COUNTERPARTY_CD
						queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							if(mpe.counterparty_map.containsKey(value)) {
								value = mpe.counterparty_map.get(value);
							}
							counterparty = value;
						}
						rset2.close();
						stmt2.close();
					}
					else if(i == 5) {	//FILL_STATION_CD
						fill_cd = rset.getString(6);
						value = fill_cd;
					}
					
					
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + counterparty + "," + type + "," + no + " , " + rev + "," + fill_cd + "," ), conn, "");
				
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_AGMT_FILLING_STN_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_FILLING_STN>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_AGMT_FILLING_STN>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_AGMT_PLANT() throws SQLException, IOException {
		function_nm = "FMS_AGMT_PLANT(DLNG)()";
		
		try {
			
			System.out.println("<<START>><<FMS_AGMT_PLANT(DLNG)>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_AGMT_PLANT(DLNG)>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String  type = "",counterparty="",abbr="",clause="",plant="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT '2', A.CUSTOMER_CD, 'D', A.FLSA_NO, A.REV_NO, A.PLANT_SEQ_NO, NULL, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM "
					+ "DLNG_FLSA_PLANT_MST A, DLNG_FLSA_MST B WHERE A.FLSA_NO = B.FLSA_NO AND A.CUSTOMER_CD = B.CUSTOMER_CD AND A.REV_NO = B.REV_NO AND"
					+ "(? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(2);
				type = rset.getString(3);
				no = rset.getString(4);
				rev = rset.getString(5);
				plant = rset.getString(6);
				row = spreadsheet.createRow(nrow++);
				value = "";
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					
					if(i == 1) {	//COUNTERPARTY_CD
						queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							if(mpe.counterparty_map.containsKey(value)) {
								value = mpe.counterparty_map.get(value);
							}
							counterparty = value;
						}
						rset2.close();
						stmt2.close();
					}
					else if(i == 5) {	//PLANT_SEQ_NO
						
//						queryString2 = "SELECT PLANT_SHORT_ABBR FROM FMS7_CUSTOMER_PLANT_DTL WHERE CUSTOMER_CD = ? AND SEQ_NO = ? ";
//						stmt2 = conn.prepareStatement(queryString2);
//						stmt2.setString(1, cd);
//						stmt2.setString(2, plant);
//						rset2 = stmt2.executeQuery();
//						if (rset2.next()) {
//							plant = rset2.getString(1);
//							plant = plant.toUpperCase();
//
//						} else {
//							plant = null;
//						}
//						stmt2.close();
//						rset2.close();
						value = plant;
					}
					
					
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + counterparty + "," + type + "," + no + " , " + rev + "," + plant + "," ), conn, "");
				
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_AGMT_PLANT(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_PLANT(DLNG)>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_AGMT_PLANT(DLNG)>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_AGMT_BILLING_DTL() throws SQLException, IOException {
		function_nm = "FMS_AGMT_BILLING_DTL(DLNG)()";
		
		try {
			
			System.out.println("<<START>><<FMS_AGMT_BILLING_DTL(DLNG)>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_AGMT_BILLING_DTL(DLNG)>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,"
					+ "INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,"
					+ "EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String  type = "",counterparty="",abbr="",clause="",plant="",name="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT '2', A.CUSTOMER_CD, 'D', A.FLSA_NO, A.FLSA_REV_NO, A.BILLING_FREQ, A.FLAG, A.DUE_DATE, NULL, A.INVOICE_CUR_CD, A.PAYMENT_CUR_CD, A.INT_CAL_RATE_CD,"
					+ " A.INT_CAL_SIGN, A.INT_CAL_PERCENTAGE, A.EXCHNG_RATE_CD, A.EXCHNG_RATE_CAL, A.INV_CRITERIA, A.EXCHG_RATE_NOTE, A.TAX_STRUCT_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
					+ "A.EMP_CD, NULL, NULL, 'B', NULL, NULL, NULL, NULL, NULL FROM "
					+ "DLNG_FLSA_BILLING_DTL A, DLNG_FLSA_MST B WHERE A.FLSA_NO = B.FLSA_NO AND A.CUSTOMER_CD = B.CUSTOMER_CD AND A.FLSA_REV_NO = B.REV_NO AND"
					+ "(? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(2);
				type = rset.getString(3);
				no = rset.getString(4);
				rev = rset.getString(5);
				
				
				queryString1 = "SELECT PLANT_SEQ_NO FROM DLNG_FLSA_PLANT_MST WHERE FLSA_NO = ? AND CUSTOMER_CD = ? AND REV_NO = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, no);
				stmt1.setString(2, cd);
				stmt1.setString(3, rev);
				
				rset1 = stmt1.executeQuery();
				while(rset1.next()) {
				
				row = spreadsheet.createRow(nrow++);
				value = "";
				name = "";
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					
					if(i == 1) {	//COUNTERPARTY_CD
						queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							if(mpe.counterparty_map.containsKey(value)) {
								value = mpe.counterparty_map.get(value);
							}
							counterparty = value;
						}
						rset2.close();
						stmt2.close();
					}
					else if (i == 11) { //INT_CAL_RATE_CD

						queryString2 = "SELECT INT_RATE_NM FROM FMS7_CONT_INT_RATE_MST WHERE INT_RATE_CD=?";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, value);
						rset2 = stmt2.executeQuery();
						if (rset2.next()) {
							name = rset2.getString(1);
							name = name.toUpperCase();

						} else {
							name = null;
						}
						stmt2.close();
						rset2.close();
						cell = row.createCell(i);
//						cell.setCellValue("'" + name + "'");
						value = name;
					}
					else if(i == 14) {	//EXCHNG_RATE_CD
						queryString2="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, value);
						rset2 = stmt2.executeQuery();
						   if(rset2.next()) {	
							   name = rset2.getString(1);
							   name= name.toUpperCase();
							   if (name.contains("CUSTOMS RATE")) {
									name = "CUSTOM EXCHANGE RATE";
								}
								else if (name.contains("RBI REFERENCE")) {
									name = "RBI REFERENCE RATE";
								}
								else if (name.contains("SBI MUMBAI TT AVERAGE")) {
									name = "SBI MUMBAI TT BUY SELL";
								}
								else if (name.contains("SBI TT BUYING")) {
									name = "SBI RATE BUY";
								}
								else if (name.contains("SBI TT SELLING")) {
									name = "SBI RATE SELL";
								}
								else if (name.contains("SBI TT BUY SELL")) {
									name = "SBI RATE BUY SELL";
								}								
						   }								   								  
						   stmt2.close();
						   rset2.close();
						   value = name;
//						   cell.setCellValue("'"+name.trim()+"'");
					}
					else if(i == 27) {	//PLANT_SEQ_NO
						plant = rset1.getString(1);
//						queryString2 = "SELECT PLANT_SHORT_ABBR FROM FMS7_CUSTOMER_PLANT_DTL WHERE CUSTOMER_CD = ? AND SEQ_NO = ? ";
//						stmt2 = conn.prepareStatement(queryString2);
//						stmt2.setString(1, cd);
//						stmt2.setString(2, plant);
//						rset2 = stmt2.executeQuery();
//						if (rset2.next()) {
//							plant = rset2.getString(1);
//							plant = plant.toUpperCase();
//
//						} else {
//							plant = null;
//						}
//						stmt2.close();
//						rset2.close();
						value = plant;
					}
					
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + counterparty + "," + type + "," + no + " , " + rev + "," + plant + "," + name + "," ), conn, "");
				}
				rset1.close();
				stmt1.close();
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_AGMT_BILLING_DTL(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_BILLING_DTL(DLNG)>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_AGMT_BILLING_DTL(DLNG)>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_AGMT_TRUCK_TRANS() throws SQLException, IOException {
		function_nm = "FMS_AGMT_TRUCK_TRANS()";
		
		try {
			
			System.out.println("<<START>><<FMS_AGMT_TRUCK_TRANS>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_AGMT_TRUCK_TRANS>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,ENT_BY,ENT_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String  type = "",counterparty="",abbr="",clause="",trans_cd="",name="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString1 = "SELECT CUSTOMER_CD, FLSA_NO, REV_NO FROM DLNG_FLSA_MST ";
			stmt1 = conn.prepareStatement(queryString1);
			rset1 = stmt1.executeQuery();
			
			while(rset1.next()) {
				cd = rset1.getString(1);
				
				no = rset1.getString(2);
				rev = rset1.getString(3);
				
				
				name = cd+"-"+no+"-"+rev+"-";
				
				
			queryString = " SELECT DISTINCT(A.TRANS_CD), NULL, 'D', NULL, NULL, NULL, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM DLNG_CUST_TRANS_DTL A WHERE "
					+ "A.CUST_CD = ? AND A. MAPPING_ID LIKE ? "
					+ "AND A.ENT_DT = (SELECT MAX(B.ENT_DT) FROM DLNG_CUST_TRANS_DTL B WHERE B.MAPPING_ID LIKE ? AND A.TRANS_CD = B.TRANS_CD)"
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) " ;
			stmt = conn.prepareStatement(queryString);
			
			stmt.setString(1, cd);
			stmt.setString(2, name+"%");
			stmt.setString(3, name+"%");
			stmt.setString(4, delta_FromDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_ToDt);
			stmt.setString(7, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,TIMESTAMP,", conn);
			
			while (rset.next()) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					trans_cd = rset.getString(1);
					type = rset.getString(3);
					for (int i = 0; i < columns.split(",").length; i++) {
						
						cell = row.createCell(i);
						value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
						value = value.trim().equals("") ? "null" : value;
						
						if(i == 0) {	//COMPANY_CD(TRANSPORTER_CD)
							queryString2 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, trans_cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = rset2.getString(1);
								trans_cd = value;
							}
							rset2.close();
							stmt2.close();
						}
						else if(i == 1) {	//COUNTERPARTY_CD
							queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = rset2.getString(1);
								if(mpe.counterparty_map.containsKey(value)) {
									value = mpe.counterparty_map.get(value);
								}
								counterparty = value;
							}
							rset2.close();
							stmt2.close();
						}
						
						else if(i == 3) {	//AGMT_NO
							value = no;
						}
						
						else if(i == 4) {	//AGMT_REV
							value = rev;
						}
						
						
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (company_cd + "," + counterparty + "," + type + "," + no + " , " + rev + "," + trans_cd + "," ), conn, "");
				}
			
			stmt.close();
			rset.close();
		}
		stmt1.close();
		rset1.close();
		
		filename = migration_setup_dir + "EXPORT/FMS_AGMT_TRUCK_TRANS_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_TRUCK_TRANS>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_AGMT_TRUCK_TRANS>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	
	public void FMS_SUPPLY_CONT_MST() throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_CONT_MST(DLNG)()";
		
		try {
			
			System.out.println("<<START>><<FMS_SUPPLY_CONT_MST(DLNG)>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SUPPLY_CONT_MST(DLNG)>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,CONT_REF_NO,"
					+ "TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,"
					+ "RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,"
					+ "BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,"
					+ "DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,"
					+ "MODIFY_BY,CONT_STATUS,IS_ALLOCATED,DDA_DT,DDA_TIME,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,TXN_CHARGE,BUYER_NOM_CUTOFF,"
					+ "TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_CLOSE,TCQ_REQUEST_QTY,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,"
					+ "CHANGE_DATE_REQ,MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
					+ "OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,"
					+ "LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,SF_GEN_DT,"
					+ "CLOSURE_REQUEST_FLAG,CLOSE_EFF_DT,CLOSURE_ALLOC_QTY,CLOSURE_REMARK,ADV_ADJUST";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String  rev = "",abbr="",clause="",ent_dt="",date="",cont_status="",fcc_flag="",sn_ref="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,TIMESTAMP,", conn);

			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				//SN
				
				
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO,A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,'F',A.SN_NAME,A.SN_REF_NO,NULL,"
						+"TO_CHAR(A.SIGNING_DT, 'DD/MM/YYYY hh24:mi:ss'),A.SIGNING_TIME,TO_CHAR(A.START_DT, 'DD/MM/YYYY hh24:mi:ss'),"
						+"TO_CHAR(A.END_DT, 'DD/MM/YYYY hh24:mi:ss'),A.SN_BASE,'0',A.TCQ, A.DCQ,A.VARIATION_MODE,'1', A.RATE, A.RATE_UNIT,NULL,'15', A.TRANSPORTATION_CHARGE,'Y',"
						+"A.BUYER_MONTH_NOM, A.BUYER_WEEK_NOM, A.BUYER_DAILY_NOM,'Y', A.SELLER_MONTH_NOM, A.SELLER_WEEK_NOM,A.SELLER_DAILY_NOM, A.DAY_DEF, A.DAY_START_TIME,"
						+ " A.DAY_END_TIME,A.MDCQ, A.MDCQ_PERCENTAGE,'Y',A.FCC_BY,TO_CHAR(A.FCC_DATE, 'DD/MM/YYYY hh24:mi:ss') , A.REMARK,"		//FCC forcefully Y
						+"TO_CHAR(A.RENEWAL_DT, 'DD/MM/YYYY hh24:mi:ss'),TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'),A.EMP_CD,NULL,NULL, A.FCC_FLAG, 'Y',"
						+ "TO_CHAR(A.DDA_DT, 'DD/MM/YYYY hh24:mi:ss'),A.DDA_TIME,'Y','Y','4','14:00','1', A.TCQ_REQUEST_SIGN,A.TCQ_REQUEST_FLAG,A.TCQ_REQUEST_CLOSE,"
						+"A.TCQ_REQUEST_QTY, A.PRICE_REQUEST_FLAG, A.PRICE_APPROVE_FLAG,NULL,A.MEASUREMENT,NULL,A.MEAS_STANDARD,A.MEAS_TEMPERATURE,"
						+"A.PRESSURE_MIN_BAR, A.PRESSURE_MAX_BAR,A.OFF_SPEC_GAS,NULL,A.SPEC_GAS_ENERGY_BASE,A.SPEC_GAS_MIN_ENERGY,A.SPEC_GAS_MAX_ENERGY, "
						+ "'N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,A.SN_CLOSURE_DT,A.SN_CLOSURE_QTY,TO_CHAR(A.DEAL_ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, A.FORMULA_REMARK "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);

				rset1 = stmt1.executeQuery();
				while (rset1.next()) {					
					row = spreadsheet.createRow(nrow++);
					ncell=0;
//					cell = row.createCell(i);									
					
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					sn_ref = rset1.getString(8);
					ent_dt = rset1.getString(43);
					ent_dt = ent_dt.split(" ")[0];
					ent_dt = ent_dt.split("/")[2];
					ent_dt = ent_dt.substring(2);
					cont_type = "F";

				    if (mpe.counterparty_map.containsKey(abbr)) {
				    	 abbr =mpe.counterparty_map.get(abbr); 
				    }
						
				    cell = row.createCell(ncell++);
				    cell.setCellValue("'"+abbr+"/"+ent_dt+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);						
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						value = value.trim();
						
						if(i==1) {
							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
						    cell.setCellValue("'"+value+"'");
						}
						
						else if(i==7) {
				    		cont_name = abbr+"-FL"+no+"-FLREV"+rev+"-SN"+cont_no+"-SNREV"+cont_rev;
							value = cont_name;
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i==8) {
//							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							
							 if(sn_ref==null) {
								sn_ref = cont_no+"-"+cont_rev;
							}
							 value = sn_ref;
							
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 38) {
							queryString2 = "SELECT SN_CLOSURE_REQUEST,SN_CLOSURE_CLOSE,TO_CHAR(SN_CLOSURE_DT, 'DD/MM/YYYY hh24:mi:ss'),SN_CLOSURE_QTY FROM DLNG_SN_MST WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO =? AND SN_REV_NO =?";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(1));
							stmt2.setString(2, rset1.getString(2));
							stmt2.setString(3, rset1.getString(3));
							stmt2.setString(4, rset1.getString(4));
							stmt2.setString(5, rset1.getString(5));
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {			
								sn_req = rset2.getString(1);
								sn_close = rset2.getString(2);
								sn_dt = rset2.getString(3);
								sn_qty = rset2.getString(4);
								
								if(sn_req!=null) {
									if(sn_req.equals("Y") && sn_close.equals("Y")) {								
											sn_req = "A";
											cont_status = "C";
											fcc_flag = "Y";
									}
									else if(sn_req.equals("Y") && sn_close.equals("N")) {							
											sn_req = "Y";
											cont_status = "P";
											fcc_flag = "Y";	//FCC_FLAG FORCEFULLY APPROVED BP20250725...
									}
									
									else { 
										cont_status = "Y";
										sn_req = null;
										fcc_flag = rset1.getString(38);
									}
								}
								else if (rset1.getString(57)!=null && rset1.getString(58)!=null && rset1.getString(57).equals("Y") && rset1.getString(58).equals("N")){
									cont_status = "P";
									sn_req = null;
									fcc_flag = rset1.getString(38);
								}
								else if(rset1.getString(38)!=null) {
									cont_status = "Y";
									fcc_flag = rset1.getString(38);
								}
								else{
									cont_status = "P";
									fcc_flag = rset1.getString(38);
								}
							}
							rset2.close();
							stmt2.close();

							cell.setCellValue("'"+fcc_flag+"'");
						}
						
						else if(i == 41) {
							if(rset1.getString(41)!=null && rset1.getString(88)!=null) {
								value = rset1.getString(41)+"("+rset1.getString(88)+")" + " @" + cont_ref;
							}
							else if(rset1.getString(88) != null) {
								value = "("+rset1.getString(88)+")" + " @" + cont_ref;
							}
							else {
								value = "@" + cont_ref;
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 43) {
							value = rset1.getString(86);
							if(value==null) {
								value = rset1.getString(43);
							}else {
								value = rset1.getString(86);	
							}
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 47) { 
							cell.setCellValue("'"+cont_status+"'");	
						}

						else if(i == 76) {	//BILLING_FLAG
							queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE FLSA_NO = ? AND REV_NO = ? AND BUYER_CD = ?  AND CLAUSE_CD = '6' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, no);
							stmt2.setString(2, rev);
							stmt2.setString(3, cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = "Y";
							}
							else {
								value = "N";
							}
							rset2.close();
							stmt2.close();
							
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 74) {	//LIABILITY
							queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE FLSA_NO = ? AND REV_NO = ? AND BUYER_CD = ?  AND CLAUSE_CD = '1' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, no);
							stmt2.setString(2, rev);
							stmt2.setString(3, cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = "Y";
							}
							else {
								value = "N";
							}
							rset2.close();
							stmt2.close();
							
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 78) {	//TERMINATE_FLAG
							queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE FLSA_NO = ? AND REV_NO = ? AND BUYER_CD = ?  AND CLAUSE_CD = '9' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, no);
							stmt2.setString(2, rev);
							stmt2.setString(3, cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = "Y";
							}
							else {
								value = "N";
							}
							rset2.close();
							stmt2.close();
							
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 80) {	//TERMINATE_PLANED
							queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE FLSA_NO = ? AND REV_NO = ? AND BUYER_CD = ?  AND CLAUSE_CD = '7'  ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, no);
							stmt2.setString(2, rev);
							stmt2.setString(3, cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = "Y";
							}
							else {
								value = "N";
							}
							rset2.close();
							stmt2.close();
							
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 81) {	//TERMINATE_FORCE
							queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE FLSA_NO = ? AND REV_NO = ? AND BUYER_CD = ?  AND  CLAUSE_CD = '8' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, no);
							stmt2.setString(2, rev);
							stmt2.setString(3, cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = "Y";
							}
							else {
								value = "N";
							}
							rset2.close();
							stmt2.close();
							
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 83) {
							cell.setCellValue("'"+sn_req+"'");
							
						}
						else if(i == 84) {
							value = sn_dt;
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 85) {
							value = sn_qty;
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 87) {
							value = "N";
							cell.setCellValue("'"+value+"'");
						}
						
					else {
							cell.setCellValue("'"+value+"'");
					}
						
					}
					count++;
					logger.data(fname, (abbr + "," + no + "," + rev + ","+ cont_no + ","+ cont_rev + ","+ cont_type + ","), conn, "");
				}
				rset1.close();
				stmt1.close();
				
				//LOA
				
				queryString1 = "SELECT  A.CUSTOMER_CD, A.TENDER_NO,'0',A.LOA_NO,A.LOA_REV_NO,'E',A.LOA_NAME,A.LOA_REF_NO,NULL, "
						+ "TO_CHAR(A.SIGNING_DT, 'DD/MM/YYYY hh24:mi:ss'),A.SIGNING_TIME,TO_CHAR(A.START_DT, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_CHAR(A.END_DT, 'DD/MM/YYYY hh24:mi:ss'),A.LOA_BASE,'0',A.TCQ, A.DCQ, A.VARIATION_MODE, A.QUANTITY_UNIT, A.RATE, A.RATE_UNIT, NULL, '15', "
						+ "A.TRANSPORTATION_CHARGE, A.BUYER_NOM, A.BUYER_MONTH_NOM, A.BUYER_WEEK_NOM, A.BUYER_DAILY_NOM, A.SELLER_NOM, A.SELLER_MONTH_NOM, A.SELLER_WEEK_NOM, "
						+ "A.SELLER_DAILY_NOM, A.DAY_DEF, A.DAY_START_TIME, A.DAY_END_TIME, A.MDCQ, A.MDCQ_PERCENTAGE, 'Y', A.FCC_BY,"	//FCC forcefully Y
						+ "TO_CHAR(A.FCC_DATE, 'DD/MM/YYYY hh24:mi:ss') ,A.REMARK, TO_CHAR(A.RENEWAL_DT, 'DD/MM/YYYY hh24:mi:ss'),"
		 				+ "TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.FCC_FLAG, 'Y', TO_CHAR(A.DDA_DT, 'DD/MM/YYYY hh24:mi:ss'),"
		 				+ "A.DDA_TIME,'Y','Y','4','00:00','1',A.TCQ_REQUEST_SIGN,A.TCQ_REQUEST_FLAG,A.TCQ_REQUEST_CLOSE, "
		 				+ "A.TCQ_REQUEST_QTY, A.PRICE_REQUEST_FLAG, A.PRICE_APPROVE_FLAG,NULL,A.MEASUREMENT,NULL,A.MEAS_STANDARD,A.MEAS_TEMPERATURE,"
		 				+ "A.PRESSURE_MIN_BAR, A.PRESSURE_MAX_BAR,A.OFF_SPEC_GAS,NULL,A.SPEC_GAS_ENERGY_BASE,A.SPEC_GAS_MIN_ENERGY,A.SPEC_GAS_MAX_ENERGY, "
						+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TO_CHAR(A.DEAL_ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL  "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				
				rset1 = stmt1.executeQuery();
				while (rset1.next()) {					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;									
					sn_req="";
					abbr = rset.getString(1);
					no = rset1.getString(2);
//					rev = rset1.getString(3);
					cont_no= rset1.getString(4);
					cont_rev = rset1.getString(5);
					sn_ref = rset1.getString(8);
					ent_dt = rset1.getString(43);
					ent_dt = ent_dt.split(" ")[0];
					ent_dt = ent_dt.split("/")[2];
					ent_dt = ent_dt.substring(2);
//					System.out.println(date);
					cont_type = "E";

				    if (mpe.counterparty_map.containsKey(abbr)) {
				    	 abbr =mpe.counterparty_map.get(abbr); 
				    }
						
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"/"+ent_dt+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						value = value.trim();
						
						if(i == 1) {	//COUNTERPARTY_CD(CONT_REF)
							cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 2) {	//AGMT_NO
							value = no;
							cell.setCellValue("'"+value+"'");
						}
						else if(i==7) {
							cont_name = abbr+"-TENDER"+no+"-LOA"+cont_no+"-LOAREV"+cont_rev;
							value = cont_name;
							cell.setCellValue("'"+value+"'");
						}
						else if(i==8) {
//							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							
							 if(sn_ref==null) {
								sn_ref = cont_no+"-"+cont_rev;
							}
							 value = sn_ref;
							
							cell.setCellValue("'"+value+"'");
						}

						else if(i == 38) {
							queryString2 = "SELECT SN_CLOSURE_REQUEST,SN_CLOSURE_CLOSE,TO_CHAR(SN_CLOSURE_DT, 'DD/MM/YYYY hh24:mi:ss'),SN_CLOSURE_QTY FROM DLNG_SN_MST WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO =? AND SN_REV_NO =?";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(1));
							stmt2.setString(2, rset1.getString(2));
							stmt2.setString(3, rset1.getString(3));
							stmt2.setString(4, rset1.getString(4));
							stmt2.setString(5, rset1.getString(5));
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {			
								sn_req = rset2.getString(1);
								sn_close = rset2.getString(2);
								sn_dt = rset2.getString(3);
								sn_qty = rset2.getString(4);
								
								if(sn_req!=null) {
									if(sn_req.equals("Y") && sn_close.equals("Y")) {								
											sn_req = "A";
											cont_status = "C";
											fcc_flag = "Y";
									}
									else if(sn_req.equals("Y") && sn_close.equals("N")) {							
											sn_req = "Y";
											cont_status = "P";
											fcc_flag = "Y";	//FCC_FLAG FORCEFULLY APPROVED BP20250725...
									}
									
									else { 
										cont_status = "Y";
										sn_req = null;
										fcc_flag = rset1.getString(38);
									}
								}
								else if (rset1.getString(57)!=null && rset1.getString(58)!=null && rset1.getString(57).equals("Y") && rset1.getString(58).equals("N")){
									cont_status = "P";
									sn_req = null;
									fcc_flag = rset1.getString(38);
								}
								else if(rset1.getString(38)!=null) {
									cont_status = "Y";
									fcc_flag = rset1.getString(38);
								}
								else{
									cont_status = "P";
									fcc_flag = rset1.getString(38);
								}
							}
							rset2.close();
							stmt2.close();

							cell.setCellValue("'"+fcc_flag+"'");
						}
						

						else if(i == 41) {
							if(rset1.getString(41)!=null) {
								value = rset1.getString(41) + " @" + cont_ref;
							}
							else {
								value = "@" + cont_ref;
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 43) {
							value = rset1.getString(86);
							if(value==null) {
								value = rset1.getString(43);
							}else {
								value = rset1.getString(86);	
							}
							cell.setCellValue("'"+value+"'");
						}

						else if(i == 47) { 
							cell.setCellValue("'"+cont_status+"'");	
						}
						
						else if (i == 74) {	// LIABILITY_FLAG
							queryString2 = "SELECT CLAUSE_CD FROM DLNG_LOA_CLAUSE_MST WHERE BUYER_CD = ? AND TENDER_NO = ? AND LOA_NO = ? AND LOA_REV_NO = ? AND CLAUSE_CD = '1' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(1));
							stmt2.setString(2, rset1.getString(2));
							stmt2.setString(3, rset1.getString(4));
							stmt2.setString(4, rset1.getString(5));
//							stmt2.setString(5, rset1.getString(5));
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {								
								value = "Y";
							}
							else {
								value = "N";
							}
							rset2.close();
							stmt2.close();
							cell.setCellValue("'"+value+"'");
						}
						
						else if (i == 76) {	// BILLING_FLAG
							queryString2 = "SELECT CLAUSE_CD FROM DLNG_LOA_CLAUSE_MST WHERE BUYER_CD = ? AND TENDER_NO = ? AND LOA_NO =? AND LOA_REV_NO =? AND CLAUSE_CD = '6' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(1));
							stmt2.setString(2, rset1.getString(3));
//							stmt2.setString(3, rset1.getString(3));
							stmt2.setString(3, rset1.getString(4));
							stmt2.setString(4, rset1.getString(5));
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {								
								value = "Y";
							}
							else {
								value = "N";
							}
							rset2.close();
							stmt2.close();
							cell.setCellValue("'"+value+"'");
						}
						else if (i == 78) {	// TERMINATE_FLAG
							queryString2 = "SELECT CLAUSE_CD FROM DLNG_LOA_CLAUSE_MST WHERE BUYER_CD = ? AND TENDER_NO = ? AND LOA_NO =? AND LOA_REV_NO =? AND CLAUSE_CD = '9' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(1));
							stmt2.setString(2, rset1.getString(2));
//							stmt2.setString(3, rset1.getString(3));
							stmt2.setString(3, rset1.getString(4));
							stmt2.setString(4, rset1.getString(5));
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {								
								value = "Y";
							}
							else {
								value = "N";
							}
							rset2.close();
							stmt2.close();
							cell.setCellValue("'"+value+"'");
						}

						else if(i == 83) {
							cell.setCellValue("'"+sn_req+"'");
							
						}
						else if(i == 84) {
							value = sn_dt;
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 85) {
							value = sn_qty;
							cell.setCellValue("'"+value+"'");
						}

						else if(i == 87) {
							value = "N";
							cell.setCellValue("'"+value+"'");
						}
						
						else {
							cell.setCellValue("'"+value+"'");
							}
						
//						System.out.println(i +":"+ value);
					}
					count++;
//					logger.data(fname, (abbr + "," + no + "," + rev + ","+ cont_no + ","+ cont_rev + ","+ cont_type + ","), conn, "");

				}
				rset1.close();
				stmt1.close();
				}
//			rset.close();
			
            
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_MST(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_CONT_MST(DLNG)>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_SUPPLY_CONT_MST(DLNG)>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
//FMS_SUPPLY_CONT_FILLING_STN
	public void FMS_SUPPLY_CONT_FILLING_STN() throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_CONT_FILLING_STN()";
		
		try {
			
			System.out.println("<<START>><<FMS_SUPPLY_CONT_FILLING_STN>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SUPPLY_CONT_FILLING_STN>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,FILL_STATION_CD,ENT_BY,ENT_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String  rev = "",counterparty="",fill_cd="";
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
				//SN
				queryString1 = " SELECT '2', A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO,A.SN_REV_NO, 'F', A.TRANSPORTER_CD, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM "
						+ "DLNG_SN_TRANSPORTER_MST A, DLNG_SN_MST B WHERE A.SN_NO = B.SN_NO AND A.CUSTOMER_CD = B.CUSTOMER_CD AND A.SN_REV_NO = B.SN_REV_NO AND "
						+ "(? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, delta_FromDt);
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_ToDt);
				stmt1.setString(4, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,FILL_STATION_CD,TIMESTAMP,", conn);

				
                while (rset1.next()) {
				
				cd = rset1.getString(2);
				no = rset1.getString(3);
			    rev = rset1.getString(4);
				cont_no = rset1.getString(5);
				cont_rev = rset1.getString(6);
				cont_type = "F";
				
				row = spreadsheet.createRow(nrow++);
				ncell = 0;
				value = "";
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
					value = (rset1.getString(i + 1) == null ) ? "null" : rset1.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					
					if(i == 0) {	//COUNTERPARTY_CD
						queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							if(mpe.counterparty_map.containsKey(value)) {
								value = mpe.counterparty_map.get(value);
							}
							counterparty = value;
						}
						rset2.close();
						stmt2.close();
					}
					
					else if(i==1) {
						cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						value = cont_ref;
					    cell.setCellValue("'"+value+"'");
					}
					else if(i == 7) {	//FILL_STATION_CD
						fill_cd = rset1.getString(8);
						value = fill_cd;
						cell.setCellValue("'"+value+"'");
					}
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + counterparty + "," + no + "," + rev + "," + cont_no + ","+cont_rev+","+cont_type+","+ fill_cd + "," ), conn, "");
			}
                rset1.close();
                stmt1.close();
    			
    			
    			//LOA
    			queryString1 = "SELECT '2', A.CUSTOMER_CD, A.TENDER_NO,'0',A.LOA_NO, A.LOA_REV_NO,'E', A.TRANSPORTER_CD, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS')"
						+ "FROM DLNG_LOA_TRANSPORTER_MST A, DLNG_LOA_MST B WHERE A.TENDER_NO = B.TENDER_NO AND A.CUSTOMER_CD = B.CUSTOMER_CD AND A.LOA_REV_NO = B.LOA_REV_NO AND "
						+ "(? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, delta_FromDt);
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_ToDt);
				stmt1.setString(4, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
                while (rset1.next()) {
				
				cd = rset1.getString(2);
				no = rset1.getString(3);
				rev = rset1.getString(4);
				cont_no = rset1.getString(5);
				cont_rev = rset1.getString(6);
				cont_type = "E";
				
				row = spreadsheet.createRow(nrow++);
				ncell = 0;
				value = "";
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
					value = (rset1.getString(i + 1) == null ) ? "null" : rset1.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					
					if(i == 0) {	//COUNTERPARTY_CD
						queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							if(mpe.counterparty_map.containsKey(value)) {
								value = mpe.counterparty_map.get(value);
							}
							counterparty = value;
						}
						rset2.close();
						stmt2.close();
					}
					
					else if(i == 1) {	//COUNTERPARTY_CD(CONT_REF)
						cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
						value = cont_ref;
						cell.setCellValue("'"+value+"'");
					}
					else if(i == 7) {	//FILL_STATION_CD
						fill_cd = rset1.getString(8);
						value = fill_cd;
						cell.setCellValue("'"+value+"'");
					}
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + counterparty + "," + no + "," + rev + "," + cont_no + ","+cont_rev+","+cont_type+","+ fill_cd + "," ), conn, "");
			}
                rset1.close();
                stmt1.close();
                
                
                //SN FOR MAXIMUM REV (CLOSE CONTRACTS)
                String ent_by="",ent_dt="";
                		
                queryString1 = "SELECT CUSTOMER_CD, FLSA_NO, FLSA_REV_NO, SN_NO, SN_REV_NO FROM DLNG_SN_MST WHERE SN_CLOSURE_REQUEST IS NOT NULL AND SN_CLOSURE_REQUEST = 'Y' ";
                stmt1 =  conn.prepareStatement(queryString1);
                rset1 = stmt1.executeQuery();
                
                while(rset1.next()) {
                	cd = rset1.getString(1);
                	no = rset1.getString(2);
                	rev = rset1.getString(3);
                	cont_no = rset1.getString(4);
                	cont_rev = rset1.getString(5);
                	
                	
                	
                	queryString2 = "SELECT TRANSPORTER_CD, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM DLNG_SN_TRANSPORTER_MST WHERE CUSTOMER_CD = ? AND FLSA_NO = ? "
                			+ "AND SN_NO = ? ";
                	stmt2 = conn.prepareStatement(queryString2);
                	stmt2.setString(1, cd);
                	stmt2.setString(2, no);
                	stmt2.setString(3, cont_no);
                	rset2 = stmt2.executeQuery();
                	while(rset2.next()) {
                		
                		fill_cd = rset2.getString(1);
                		ent_by = rset2.getString(2);
                		ent_dt = rset2.getString(3);
                		
                	
                	row = spreadsheet.createRow(nrow++);
    				ncell = 0;
    				value = "";
    				
    				for (int i = 0; i < columns.split(",").length; i++) {
    					
    					cell = row.createCell(i);
//    					value = (rset2.getString(i + 1) == null ) ? "null" : rset2.getString(i + 1).trim().replaceAll("'", "");
//    					value = value.trim().equals("") ? "null" : value;
    					
    					
    					if(i == 0) {	//COUNTERPARTY_CD
    						queryString3 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
    						stmt3 = conn.prepareStatement(queryString3);
    						stmt3.setString(1, cd);
    						rset3 = stmt3.executeQuery();
    						if(rset3.next()) {
    							value = rset3.getString(1);
    							if(mpe.counterparty_map.containsKey(value)) {
    								value = mpe.counterparty_map.get(value);
    							}
    							counterparty = value;
    						}
    						rset3.close();
    						stmt3.close();
    					}
    					
    					else if(i==1) {
    						cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
    						value = cont_ref;
    					    cell.setCellValue("'"+value+"'");
    					}
    					else if(i==2) {	//AGMT_NO
    						value =  no;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i==3) {	//AGMT_REV
    						value =  rev;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i==4) {	//CONT_NO
    						value =  cont_no;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i==5) {	//CONT_REV
    						value =  cont_rev;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i==6) {	//CONTRACT_TYPE
    						value =  "F";
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i == 7) {	//FILL_STATION_CD
    						value = fill_cd;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i == 8) {	//ENT_BY
    						value = ent_by;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i == 9) {	//ENT_DT
    						value = ent_dt;
    						cell.setCellValue("'"+value+"'");
    					}
    					
    					cell.setCellValue("'" + value + "'");
    				}
    				count++;
    				logger.data(fname, (company_cd + "," + counterparty + "," + no + "," + rev + "," + cont_no + ","+cont_rev+","+cont_type+","+ fill_cd + "," ), conn, "");
                }
                	rset2.close();
                	stmt2.close();
                	
                }
                
                rset1.close();
                stmt1.close();
                		
                
                //LOA FOR MAXIMUM REV (CLOSE CONTRACTS)
                
                queryString1 = "SELECT CUSTOMER_CD, TENDER_NO, '0', LOA_NO, LOA_REV_NO FROM DLNG_LOA_MST WHERE LOA_CLOSURE_REQUEST IS NOT NULL AND LOA_CLOSURE_REQUEST = 'Y' ";
                stmt1 =  conn.prepareStatement(queryString1);
                rset1 = stmt1.executeQuery();
                
                while(rset1.next()) {
                	cd = rset1.getString(1);
                	no = rset1.getString(2);
                	rev = rset1.getString(3);
                	cont_no = rset1.getString(4);
                	cont_rev = rset1.getString(5);
                	
                	
                	
                	queryString2 = "SELECT TRANSPORTER_CD, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM DLNG_LOA_TRANSPORTER_MST WHERE CUSTOMER_CD = ? AND TENDER_NO = ? "
                			+ "AND LOA_NO = ? ";
                	stmt2 = conn.prepareStatement(queryString2);
                	stmt2.setString(1, cd);
                	stmt2.setString(2, no);
                	stmt2.setString(3, cont_no);
                	rset2 = stmt2.executeQuery();
                	while(rset2.next()) {
                		
                		fill_cd = rset2.getString(1);
                		ent_by = rset2.getString(2);
                		ent_dt = rset2.getString(3);
                		
                		
                		row = spreadsheet.createRow(nrow++);
                		ncell = 0;
                		value = "";
                		
                		for (int i = 0; i < columns.split(",").length; i++) {
                			
                			cell = row.createCell(i);
//    					value = (rset2.getString(i + 1) == null ) ? "null" : rset2.getString(i + 1).trim().replaceAll("'", "");
//    					value = value.trim().equals("") ? "null" : value;
                			
                			
                			if(i == 0) {	//COUNTERPARTY_CD
                				queryString3 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
                				stmt3 = conn.prepareStatement(queryString3);
                				stmt3.setString(1, cd);
                				rset3 = stmt3.executeQuery();
                				if(rset3.next()) {
                					value = rset3.getString(1);
                					if(mpe.counterparty_map.containsKey(value)) {
                						value = mpe.counterparty_map.get(value);
                					}
                					counterparty = value;
                				}
                				rset3.close();
                				stmt3.close();
                			}
                			
                			else if(i==1) {	//COUNTERPARTY_CD(CONT_REF)
                				cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
	    						value = cont_ref;
	    						cell.setCellValue("'"+value+"'");
    						}
                			else if(i==2) {	//AGMT_NO
                				value =  no;
                				cell.setCellValue("'"+value+"'");
                			}
                			else if(i==3) {	//AGMT_REV
                				value =  rev;
                				cell.setCellValue("'"+value+"'");
                			}
                			else if(i==4) {	//CONT_NO
                				value =  cont_no;
                				cell.setCellValue("'"+value+"'");
                			}
                			else if(i==5) {	//CONT_REV
                				value =  cont_rev;
                				cell.setCellValue("'"+value+"'");
                			}
                			else if(i==6) {	//CONTRACT_TYPE
                				value =  "E";
                				cell.setCellValue("'"+value+"'");
                			}
                			else if(i == 7) {	//FILL_STATION_CD
                				value = fill_cd;
                				cell.setCellValue("'"+value+"'");
                			}
                			else if(i == 8) {	//ENT_BY
                				value = ent_by;
                				cell.setCellValue("'"+value+"'");
                			}
                			else if(i == 9) {	//ENT_DT
                				value = ent_dt;
                				cell.setCellValue("'"+value+"'");
                			}
                			
                			cell.setCellValue("'" + value + "'");
                		}
                		count++;
                		logger.data(fname, (company_cd + "," + counterparty + "," + no + "," + rev + "," + cont_no + ","+cont_rev+","+cont_type+","+ fill_cd + "," ), conn, "");
                	}
                	rset2.close();
                	stmt2.close();
                	
                }
                
                rset1.close();
                stmt1.close();
                
//		}
//			rset.close();
//			stmt.close();
				
			
			filename = migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_FILLING_STN_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_CONT_FILLING_STN>>,,,,,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_SUPPLY_CONT_FILLING_STN>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}	
	
//FMS_SUPPLY_CONT_LIABILITY
public void FMS_SUPPLY_CONT_LIABILITY() throws SQLException, IOException {
		
		function_nm = "FMS_SUPPLY_CONT_LIABILITY(DLNG)()";
		
		try {

			logger.checkpoint(fname, "\n<<START>>,<<FMS_SUPPLY_CONT_LIABILITY(DLNG)>>,,,,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,LIAB_LQ_DAMG,"
					+ "LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_FROM,LQ_DAMG_TO,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_FROM,TAKE_PAY_TO,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,"
					+ "TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,"
					+ "MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String cont_type="",l_flag = "",l_price = "",l_promise = "",from_dt = "",to_dt = "",l_per = "",l_on = "",l_rmk = "";
			String t_flag = "",t_price = "",t_promise="",t_per = "",t_on = "",t_qty ="",t_q_unit ="",t_rmk = ""; 
			String m_flag = "",m_price = "",m_make = "",m_on = "",m_rec = "",m_rmk = "";	
			
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			

			logger.checkpoint(fname, "COUNTERPARTY_ABBR,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			//SN
			while (rset.next()) {					
			
				queryString1 = "SELECT A.CUSTOMER_CD,'D',A.FLSA_NO, A.FLSA_REV_NO,'F',A.SN_NO,A.SN_REV_NO,NULL,NULL,"
						+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "NULL,NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,NULL "
						+ "FROM DLNG_SN_MST A,FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ?"			
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);

				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {						
					row = spreadsheet.createRow(nrow++);
					ncell = 0;									
					
					abbr = rset.getString(1);
					no = rset1.getString(3);
					rev = rset1.getString(4);
					cont_no = rset1.getString(6);
					cont_rev = rset1.getString(7);
					cont_type = "F";
					
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
					}
					
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);															
							cell.setCellValue("'"+value+"'");
							
							if(i == 1) {
								cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
								value = cont_ref;
							}
							else if(i == 8) {
								queryString2 = "SELECT FLAG,PRICE_PER,PROMISE_QTY_FREQ,TO_CHAR(LD_FROM_DT, 'DD/MM/YYYY hh24:mi:ss'),"
										+ "TO_CHAR(LD_TO_DT, 'DD/MM/YYYY hh24:mi:ss'),LIABILITY_PER,DCQ_FLAG,PNDCQ_FLAG,MDCQ_FLAG,REMARKS"
										+ " FROM DLNG_SN_LD_DTL  WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ?";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, rset1.getString(1));
								stmt2.setString(2, no);
								stmt2.setString(3, rev);
								stmt2.setString(4, cont_no);
								stmt2.setString(5, cont_rev);
								rset2 = stmt2.executeQuery();													
								if (rset2.next()) {										
									l_flag = rset2.getString(1);
									l_price = rset2.getString(2);
									l_promise = rset2.getString(3);
									from_dt = rset2.getString(4);
									to_dt = rset2.getString(5);
									l_per = rset2.getString(6);
									if("Y".equals(rset2.getString(7)) || "Y".equals(rset2.getString(8)) || "Y".equals(rset2.getString(9))) {
										l_on = "L";
									}
									
									l_rmk = rset2.getString(10);
								}
								else {
									l_flag = null;
									l_price = null;
									l_promise = null;
									from_dt = null;
									to_dt = null;
									l_per = null;
									l_on = null;
									l_rmk = null;
								}
								rset2.close();
								stmt2.close();
								
								value = l_flag;
							}
							else if(i == 9) {
								value = l_price;
							}
							else if(i == 10) {
								value = l_promise;
							}
							else if(i == 11) {
								value = from_dt;
							}
							else if(i == 12) {
								value = to_dt;
							}
							else if(i == 13) {
								value = l_per;
							}
							else if(i == 14) {
								value = l_on;
							}
							else if(i == 15) {
								value = l_rmk;
							}
							else if(i == 16) {
								queryString2 = "SELECT FLAG,PRICE_PER,PROMISE_QTY_FREQ,TO_CHAR(TOP_FROM_DT, 'DD/MM/YYYY hh24:mi:ss'),"
										+ "TO_CHAR(TOP_TO_DT, 'DD/MM/YYYY hh24:mi:ss'),TOP_PER,ACTUAL_OBLIG_QTY,REMARKS"
										+ " FROM DLNG_SN_TOP_DTL  WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ?";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, rset1.getString(1));
								stmt2.setString(2, no);
								stmt2.setString(3, rev);
								stmt2.setString(4, cont_no);
								stmt2.setString(5, cont_rev);
								rset2 = stmt2.executeQuery();													
								if (rset2.next()) {									
									t_flag = rset2.getString(1);
									t_price = rset2.getString(2);
									t_promise = rset2.getString(3);
									from_dt = rset2.getString(4);
									to_dt = rset2.getString(5);
									t_per = rset2.getString(6);								
									t_on = "D";
									t_qty = rset2.getString(7);	
									t_q_unit = null;
									t_rmk = rset2.getString(8);
								}
								else {
									t_flag = null;
									t_price = null;
									t_promise = null;
									from_dt = null;
									to_dt = null;
									t_per = null;								
									t_on = null;
									t_qty = null;
									t_q_unit = null;
									t_rmk = null;
								}
								rset2.close();
								stmt2.close();
								
								value = t_flag;
							}
							else if(i == 17) {
								value = t_price;
							}
							else if(i == 18) {
								value = t_promise;
							}
							else if(i == 19) {
								value = from_dt;
							}
							else if(i == 20) {
								value = to_dt;
							}
							else if(i == 21) {
								value = t_per;
							}
							else if(i == 22) {
								value = t_on;
							}
							else if(i == 23) {
								value = t_qty;
							}
							else if(i == 24) {
								value = t_q_unit;
							}
							else if(i == 25) {
								value = t_rmk;
							}
							else if(i == 26) {
								queryString2 = "SELECT FLAG,PRICE_PER,MAKEUP_PERIOD,RECOVERY_PERIOD,REMARKS FROM DLNG_SN_MAKEUPGAS_DTL "
										+ " WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ?";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, rset1.getString(1));
								stmt2.setString(2, no);
								stmt2.setString(3, rev);
								stmt2.setString(4, cont_no);
								stmt2.setString(5, cont_rev);
								rset2 = stmt2.executeQuery();													
								if (rset2.next()) {									
									m_flag = rset2.getString(1);
									m_price = rset2.getString(2);
									m_make = rset2.getString(3);
									m_on = null;
									m_rec = rset2.getString(4);
									m_rmk = rset2.getString(5);
								}
								else {
									m_flag = null;
									m_price = null;
									m_make = null;
									m_on = null;
									m_rec = null;
									m_rmk = null;																
								}
								rset2.close();
								stmt2.close();
								
								value = m_flag;
							}
							else if(i == 27) {
								value = m_price;
							}
							else if(i == 28) {
								value = m_make;
							}
							else if(i == 29) {
								value = m_on;
							}
							else if(i == 30) {
								value = m_rec;
							}
							else if(i == 31) {
								value = m_rmk;
							}
																				
							cell.setCellValue("'"+value+"'");
					}
					count++;
					logger.data(fname, (abbr +"," + 'D' + "," + no + "," + rev + ","+ cont_no + ","+ cont_rev + ","+ cont_type + ","), conn, "");

				}
				rset1.close();
				stmt1.close();
				
			
			//LOA
						
		queryString1 = "SELECT A.CUSTOMER_CD,'D',A.TENDER_NO, '0','E',A.LOA_NO,A.LOA_REV_NO,NULL,NULL,"
				+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
				+ "NULL,NULL,NULL,NULL,A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,NULL "
				+ "FROM DLNG_LOA_MST A,FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ?"			
				+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
		stmt1 = conn.prepareStatement(queryString1);
		stmt1.setString(1, rset.getString(2));
		stmt1.setString(2, delta_FromDt);
		stmt1.setString(3, delta_FromDt);
		stmt1.setString(4, delta_ToDt);
		stmt1.setString(5, delta_ToDt);

		rset1 = stmt1.executeQuery();
		
		while (rset1.next()) {						
			row = spreadsheet.createRow(nrow++);
			ncell = 0;									
			
			abbr = rset.getString(1);
			no = rset1.getString(3);
			rev = "0";
			cont_no = rset1.getString(6);
			cont_rev = rset1.getString(7);
			cont_type = "E";
			
			if (mpe.counterparty_map.containsKey(abbr)) {
				abbr =mpe.counterparty_map.get(abbr); 
			}
			
			cell = row.createCell(ncell++);
			cell.setCellValue("'"+abbr+"'");

			for (int i = 1; i < columns.split(",").length; i++) {
				cell = row.createCell(ncell++);
				value = rset1.getString(i) == null ? "null" : rset1.getString(i);															
					cell.setCellValue("'"+value+"'");
					if(i == 1) {
						cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
						value = cont_ref;
					}
					if(i == 8) {
						queryString2 = "SELECT FLAG,PRICE_PER,PROMISE_QTY_FREQ,TO_CHAR(LD_FROM_DT, 'DD/MM/YYYY hh24:mi:ss'),"
								+ "TO_CHAR(LD_TO_DT, 'DD/MM/YYYY hh24:mi:ss'),LIABILITY_PER,DCQ_FLAG,PNDCQ_FLAG,MDCQ_FLAG,REMARKS"
								+ " FROM DLNG_LOA_LD_DTL  WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND LOA_NO = ? AND LOA_REV_NO = ?";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, rset1.getString(1));
						stmt2.setString(2, no);
						stmt2.setString(3, rev);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, cont_rev);
						rset2 = stmt2.executeQuery();													
						if (rset2.next()) {										
							l_flag = rset2.getString(1);
							l_price = rset2.getString(2);
							l_promise = rset2.getString(3);
							from_dt = rset2.getString(4);
							to_dt = rset2.getString(5);
							l_per = rset2.getString(6);
							if("Y".equals(rset2.getString(7)) || "Y".equals(rset2.getString(8)) || "Y".equals(rset2.getString(9))) {
								l_on = "L";
							}
							
							l_rmk = rset2.getString(10);
						}
						else {
							l_flag = null;
							l_price = null;
							l_promise = null;
							from_dt = null;
							to_dt = null;
							l_per = null;
							l_on = null;
							l_rmk = null;
						}
						rset2.close();
						stmt2.close();
						
						value = l_flag;
					}
					else if(i == 9) {
						value = l_price;
					}
					else if(i == 10) {
						value = l_promise;
					}
					else if(i == 11) {
						value = from_dt;
					}
					else if(i == 12) {
						value = to_dt;
					}
					else if(i == 13) {
						value = l_per;
					}
					else if(i == 14) {
						value = l_on;
					}
					else if(i == 15) {
						value = l_rmk;
					}
					else if(i == 16) {
						queryString2 = "SELECT FLAG,PRICE_PER,PROMISE_QTY_FREQ,TO_CHAR(TOP_FROM_DT, 'DD/MM/YYYY hh24:mi:ss'),"
								+ "TO_CHAR(TOP_TO_DT, 'DD/MM/YYYY hh24:mi:ss'),TOP_PER,ACTUAL_OBLIG_QTY,REMARKS"
								+ " FROM DLNG_LOA_TOP_DTL  WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND LOA_NO = ? AND LOA_REV_NO = ?";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, rset1.getString(1));
						stmt2.setString(2, no);
						stmt2.setString(3, rev);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, cont_rev);
						rset2 = stmt2.executeQuery();													
						if (rset2.next()) {									
							t_flag = rset2.getString(1);
							t_price = rset2.getString(2);
							t_promise = rset2.getString(3);
							from_dt = rset2.getString(4);
							to_dt = rset2.getString(5);
							t_per = rset2.getString(6);								
							t_on = "D";
							t_qty = rset2.getString(7);	
							t_q_unit = null;
							t_rmk = rset2.getString(8);
						}
						else {
							t_flag = null;
							t_price = null;
							t_promise = null;
							from_dt = null;
							to_dt = null;
							t_per = null;								
							t_on = null;
							t_qty = null;
							t_q_unit = null;
							t_rmk = null;
						}
						rset2.close();
						stmt2.close();
						
						value = t_flag;
					}
					else if(i == 17) {
						value = t_price;
					}
					else if(i == 18) {
						value = t_promise;
					}
					else if(i == 19) {
						value = from_dt;
					}
					else if(i == 20) {
						value = to_dt;
					}
					else if(i == 21) {
						value = t_per;
					}
					else if(i == 22) {
						value = t_on;
					}
					else if(i == 23) {
						value = t_qty;
					}
					else if(i == 24) {
						value = t_q_unit;
					}
					else if(i == 25) {
						value = t_rmk;
					}
					else if(i == 26) {
						queryString2 = "SELECT FLAG,PRICE_PER,MAKEUP_PERIOD,RECOVERY_PERIOD,REMARKS FROM DLNG_LOA_MAKEUPGAS_DTL "
								+ " WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND LOA_NO = ? AND LOA_REV_NO = ?";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, rset1.getString(1));
						stmt2.setString(2, no);
						stmt2.setString(3, rev);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, cont_rev);
						rset2 = stmt2.executeQuery();													
						if (rset2.next()) {									
							m_flag = rset2.getString(1);
							m_price = rset2.getString(2);
							m_make = rset2.getString(3);
							m_on = null;
							m_rec = rset2.getString(4);
							m_rmk = rset2.getString(5);
						}
						else {
							m_flag = null;
							m_price = null;
							m_make = null;
							m_on = null;
							m_rec = null;
							m_rmk = null;																
						}
						rset2.close();
						stmt2.close();
						
						value = m_flag;
					}
					else if(i == 27) {
						value = m_price;
					}
					else if(i == 28) {
						value = m_make;
					}
					else if(i == 29) {
						value = m_on;
					}
					else if(i == 30) {
						value = m_rec;
					}
					else if(i == 31) {
						value = m_rmk;
					}
																		
					cell.setCellValue("'"+value+"'");
			}
			count++;
			logger.data(fname, (abbr +"," + 'D' + "," + no + "," + rev + ","+ cont_no + ","+ cont_rev + ","+ cont_type + ","), conn, "");

		}
		rset1.close();
		stmt1.close();
	
			
			
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_LIABILITY(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_CONT_LIABILITY(DLNG)>>,,,,,,", conn);
			

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
	
//FMS_SUPPLY_CONT_PLANT(DLNG)
public void FMS_SUPPLY_CONT_PLANT() throws SQLException, IOException {
		
		function_nm = "FMS_SUPPLY_CONT_PLANT(DLNG)()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SUPPLY_CONT_PLANT(DLNG)>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT,TRANSPORTATION_CHARGE,MARKET_MARGIN,OTHER_CHARGES";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			int count = 0;
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			logger.checkpoint(fname, "COUNTERPARTY_ABBR,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				queryString1 = "SELECT DISTINCT(B.CUSTOMER_ABBR), A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO, 'F', A.PLANT_SEQ_NO, A.EMP_CD, "
						+ "TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, A.CUSTOMER_CD FROM DLNG_SN_PLANT_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					abbr = rset.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					cont_type = "S";
					p_seq_no = rset1.getString(7);
					cd = rset1.getString(13);
					
					map = abbr+"-"+p_seq_no;
					
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					if(mpe.customer_map.containsKey(map))
					{
						p_seq_no=mpe.customer_map.get(map);
					}
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						 
						if(i==1) {
							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
						    cell.setCellValue("'"+value+"'");
						}
						else if(i==7) //p_seq_no
						{
							p_seq_no=p_seq_no.split("-")[0];
							cell.setCellValue("'"+p_seq_no+"'");
						}
						else {	
						cell.setCellValue("'"+value+"'");
						}
						
					}

					count++;
					logger.data(fname, (abbr + "," + no + "," + rev + "," + p_seq_no + ","), conn, "");
					
//				}
//				rset1.close();
//				stmt1.close();
				
				//SN FROM BUYER NOMINATION
				//			name = cd+"-"+no+"-"+rev+"-";
				name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
				cont_type = "S";
				
				
				queryString2 = "SELECT DISTINCT(A.NOM_ID)"
						+ "FROM DLNG_DAILY_TRUCK_NOM_DTL A WHERE A.CONTRACT_TYPE = ? AND A.MAPPING_ID LIKE ? AND A.NOM_ID NOT LIKE ? " ;
				stmt2 = conn.prepareStatement(queryString2);
				
				stmt2.setString(1, cont_type);
				stmt2.setString(2, name+"%");
				stmt2.setString(3, name+"%"+rset1.getString(7));
				rset2 = stmt2.executeQuery();
				
				logger.checkpoint(fname, "COUNTERPARTY_ABBR,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rset2.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					abbr = rset.getString(1);
					p_seq_no = rset2.getString(1).split("-")[6];
					
					map = abbr+"-"+p_seq_no;
					
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					if(mpe.customer_map.containsKey(map))
					{
						p_seq_no=mpe.customer_map.get(map);
					}
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						 
						if(i==1) {
							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
						    cell.setCellValue("'"+value+"'");
						}
						else if(i==7) //p_seq_no
						{
							p_seq_no=p_seq_no.split("-")[0];
							cell.setCellValue("'"+p_seq_no+"'");
						}
						else {	
						cell.setCellValue("'"+value+"'");
						}
						
					}

					count++;
					logger.data(fname, (abbr + "," + no + "," + rev + "," + p_seq_no + ","), conn, "");
					}
					stmt2.close();
					rset2.close();
				}
				
				stmt1.close();
				rset1.close();
				
				
			
				
			//LOA				
				
				queryString1 = "SELECT DISTINCT(B.CUSTOMER_ABBR), A.TENDER_NO, '0' , A.LOA_NO, A.LOA_REV_NO, 'E', A.PLANT_SEQ_NO, A.EMP_CD, "
						+ "TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, A.CUSTOMER_CD FROM DLNG_LOA_PLANT_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					abbr = rset.getString(1);				
					no = rset1.getString(2);
					rev = "0";
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					p_seq_no = rset1.getString(7);
					cd = rset1.getString(13);
					
					map = abbr+"-"+p_seq_no;
					
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					if(mpe.customer_map.containsKey(map))
					{
						p_seq_no=mpe.customer_map.get(map);
					}
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						
						if(i == 1) {
							cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
							cell.setCellValue("'"+value+"'");
						}						
						if(i==7) //p_seq_no
						{
							p_seq_no=p_seq_no.split("-")[0];
							cell.setCellValue("'"+p_seq_no+"'");
						}
						else {	
						cell.setCellValue("'"+value+"'");
						}
						
					}

					count++;
					logger.data(fname, (abbr + "," + agmt_type + "," + no + "," + rev + "," + p_seq_no + ","), conn, "");
					
					//LOA FROM BUYER NOMINATION
					//			name = cd+"-"+no+"-"+rev+"-";
					name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
					cont_type = "L";
					
					
					queryString2 = "SELECT DISTINCT(A.NOM_ID)"
							+ "FROM DLNG_DAILY_TRUCK_NOM_DTL A WHERE A.CONTRACT_TYPE = ? AND A.MAPPING_ID LIKE ? AND A.NOM_ID NOT LIKE ? " ;
					stmt2 = conn.prepareStatement(queryString2);
					
					stmt2.setString(1, cont_type);
					stmt2.setString(2, name+"%");
					stmt2.setString(3, name+"%"+rset1.getString(7));
					rset2 = stmt2.executeQuery();
					
					logger.checkpoint(fname, "COUNTERPARTY_ABBR,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
					
					while (rset2.next()) {
						
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1);
						p_seq_no = rset2.getString(1).split("-")[6];
						
						map = abbr+"-"+p_seq_no;
						
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						
						cell = row.createCell(ncell++);
						cell.setCellValue("'"+abbr+"'");
						
						if(mpe.customer_map.containsKey(map))
						{
							p_seq_no=mpe.customer_map.get(map);
						}
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i);
							 
							if(i==1) {
								cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
								value = cont_ref;
							    cell.setCellValue("'"+value+"'");
							}
							else if(i==7) //p_seq_no
							{
								p_seq_no=p_seq_no.split("-")[0];
								cell.setCellValue("'"+p_seq_no+"'");
							}
							else {	
							cell.setCellValue("'"+value+"'");
							}
							
						}

						count++;
						logger.data(fname, (abbr + "," + no + "," + rev + "," + p_seq_no + ","), conn, "");
						}
						stmt2.close();
						rset2.close();
					
				}
				rset1.close();
				stmt1.close();
				
			}
			stmt.close();
			rset.close();
			
			//SN FOR MAXIMUM REV (CLOSE CONTRACTS)
            String ent_by="",ent_dt="",counterparty="";
            		
            queryString1 = "SELECT CUSTOMER_CD, FLSA_NO, FLSA_REV_NO, SN_NO, SN_REV_NO, EMP_CD, TO_CHAR(START_DT,'DD/MM/YYYY HH24:MI:SS') FROM DLNG_SN_MST WHERE SN_CLOSURE_REQUEST IS NOT NULL AND SN_CLOSURE_REQUEST = 'Y' ";
            stmt1 =  conn.prepareStatement(queryString1);
            rset1 = stmt1.executeQuery();
            
            while(rset1.next()) {
            	cd = rset1.getString(1);
            	no = rset1.getString(2);
            	rev = rset1.getString(3);
            	cont_no = rset1.getString(4);
            	cont_rev = rset1.getString(5);
            	cont_type = "S";
            	
        		ent_by = rset1.getString(6);
        		ent_dt = rset1.getString(7);
            	
            	name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
            	
            	queryString2 = "SELECT DISTINCT(A.NOM_ID)"
						+ "FROM DLNG_DAILY_TRUCK_NOM_DTL A WHERE A.CONTRACT_TYPE = ? AND A.MAPPING_ID LIKE ? " ;
				stmt2 = conn.prepareStatement(queryString2);
				
				stmt2.setString(1, cont_type);
				stmt2.setString(2, name+"%");
				rset2 = stmt2.executeQuery();
            	while(rset2.next()) {
            		
				p_seq_no = rset2.getString(1).split("-")[6];
            		
            	row = spreadsheet.createRow(nrow++);
				ncell = 0;
				value = "";
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
//					value = (rset2.getString(i + 1) == null ) ? "null" : rset2.getString(i + 1).trim().replaceAll("'", "");
//					value = value.trim().equals("") ? "null" : value;
					
					
					if(i == 0) {	//COUNTERPARTY_CD
						queryString3 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, cd);
						rset3 = stmt3.executeQuery();
						if(rset3.next()) {
							value = rset3.getString(1);
							map = value+"-"+p_seq_no;
							if(mpe.counterparty_map.containsKey(value)) {
								value = mpe.counterparty_map.get(value);
							}
							counterparty = value;
						}
						rset3.close();
						stmt3.close();
					}
					
					else if(i==1) {
						cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						value = cont_ref;
					    cell.setCellValue("'"+value+"'");
					}
					else if(i==2) {	//AGMT_NO
						value =  no;
						cell.setCellValue("'"+value+"'");
					}
					else if(i==3) {	//AGMT_REV
						value =  rev;
						cell.setCellValue("'"+value+"'");
					}
					else if(i==4) {	//CONT_NO
						value =  cont_no;
						cell.setCellValue("'"+value+"'");
					}
					else if(i==5) {	//CONT_REV
						value =  cont_rev;
						cell.setCellValue("'"+value+"'");
					}
					else if(i==6) {	//CONTRACT_TYPE
						value =  "F";
						cell.setCellValue("'"+value+"'");
					}
					else if(i == 7) {	//PLANT_SEQ_NO
						if(mpe.customer_map.containsKey(map))
    					{
    						p_seq_no=mpe.customer_map.get(map);
    					}
						p_seq_no=p_seq_no.split("-")[0];
						value = p_seq_no; 
						cell.setCellValue("'"+p_seq_no+"'");
					}
					else if(i == 8) {	//ENT_BY
						value = ent_by;
						cell.setCellValue("'"+value+"'");
					}
					else if(i == 9) {	//ENT_DT
						value = ent_dt;
						cell.setCellValue("'"+value+"'");
					}
					else if(i == 10 || i == 11 || i == 12) {
						value =  null;
						cell.setCellValue("'"+value+"'");
					}
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + counterparty + "," + no + "," + rev + "," + cont_no + ","+cont_rev+","+cont_type+","+ p_seq_no + "," ), conn, "");
            }
            	rset2.close();
            	stmt2.close();
            	
            }
            
            rset1.close();
            stmt1.close();
            		
            
            //LOA FOR MAXIMUM REV (CLOSE CONTRACTS)
            
            queryString1 = "SELECT CUSTOMER_CD, TENDER_NO, '0', LOA_NO, LOA_REV_NO, EMP_CD, TO_CHAR(START_DT,'DD/MM/YYYY HH24:MI:SS') FROM DLNG_LOA_MST WHERE LOA_CLOSURE_REQUEST IS NOT NULL AND LOA_CLOSURE_REQUEST = 'Y' ";
            stmt1 =  conn.prepareStatement(queryString1);
            rset1 = stmt1.executeQuery();
            
            while(rset1.next()) {
            	cd = rset1.getString(1);
            	no = rset1.getString(2);
            	rev = rset1.getString(3);
            	cont_no = rset1.getString(4);
            	cont_rev = rset1.getString(5);
            	cont_type = "L";
            	ent_by = rset1.getString(6);
        		ent_dt = rset1.getString(7);
            	
            	
            	
        		queryString2 = "SELECT DISTINCT(A.NOM_ID)"
						+ "FROM DLNG_DAILY_TRUCK_NOM_DTL A WHERE A.CONTRACT_TYPE = ? AND A.MAPPING_ID LIKE ? " ;
				stmt2 = conn.prepareStatement(queryString2);
				
				stmt2.setString(1, cont_type);
				stmt2.setString(2, name+"%");
				rset2 = stmt2.executeQuery();
            	while(rset2.next()) {
            		
				p_seq_no = rset2.getString(1).split("-")[6];
            		
            	row = spreadsheet.createRow(nrow++);
				ncell = 0;
				value = "";
            		
            		for (int i = 0; i < columns.split(",").length; i++) {
    					
    					cell = row.createCell(i);
//    					value = (rset2.getString(i + 1) == null ) ? "null" : rset2.getString(i + 1).trim().replaceAll("'", "");
//    					value = value.trim().equals("") ? "null" : value;
    					
    					
    					if(i == 0) {	//COUNTERPARTY_CD
    						queryString3 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
    						stmt3 = conn.prepareStatement(queryString3);
    						stmt3.setString(1, cd);
    						rset3 = stmt3.executeQuery();
    						if(rset3.next()) {
    							value = rset3.getString(1);
    							map = value+"-"+p_seq_no;
    							if(mpe.counterparty_map.containsKey(value)) {
    								value = mpe.counterparty_map.get(value);
    							}
    							counterparty = value;
    						}
    						rset3.close();
    						stmt3.close();
    					}
    					
    					else if(i==1) {
    						cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
    						value = cont_ref;
    					    cell.setCellValue("'"+value+"'");
    					}
    					else if(i==2) {	//AGMT_NO
    						value =  no;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i==3) {	//AGMT_REV
    						value =  rev;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i==4) {	//CONT_NO
    						value =  cont_no;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i==5) {	//CONT_REV
    						value =  cont_rev;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i==6) {	//CONTRACT_TYPE
    						value =  "E";
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i == 7) {	//PLANT_SEQ_NO
    						if(mpe.customer_map.containsKey(map))
        					{
        						p_seq_no=mpe.customer_map.get(map);
        					}
    						p_seq_no=p_seq_no.split("-")[0];
    						value = p_seq_no; 
    						cell.setCellValue("'"+p_seq_no+"'");
    					}
    					else if(i == 8) {	//ENT_BY
    						value = ent_by;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i == 9) {	//ENT_DT
    						value = ent_dt;
    						cell.setCellValue("'"+value+"'");
    					}
    					else if(i == 10 || i == 11 || i == 12) {
    						value =  null;
    						cell.setCellValue("'"+value+"'");
    					}
    					
    					cell.setCellValue("'" + value + "'");
    				}
            		count++;
            		logger.data(fname, (company_cd + "," + counterparty + "," + no + "," + rev + "," + cont_no + ","+cont_rev+","+cont_type+","+ p_seq_no + "," ), conn, "");
            	}
            	rset2.close();
            	stmt2.close();
            	
            }
            rset1.close();
            stmt1.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 

			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_CONT_PLANT(DLNG)>>,,,,", conn);
			
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
		}
		
	}
	
//FMS_SUPPLY_BILLING_DTL
public void FMS_SUPPLY_BILLING_DTL() throws SQLException, IOException {
	
	function_nm = "FMS_SUPPLY_BILLING_DTL(DLNG)()";
	
	try {

		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_SUPPLY_BILLING_DTL(DLNG)>>,,,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);

		columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,"
				+ "EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EFF_DT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE";
		workbook = new XSSFWorkbook();
		spreadsheet = workbook.createSheet("Sheet 1");

		nrow = 0;
		ncell = 0;
		count = 0;
		String plant_seq= "";
		
		row = spreadsheet.createRow(nrow++);
		
		// Inserting Column Names
		for (int i = 0; i < columns.split(",").length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columns.split(",")[i]);
		}
		
		//SN
		// Inserting Rest of the data	
		queryString = "SELECT A.CUSTOMER_CD,A.FLSA_NO,A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.PLANT_SEQ_NO,TO_CHAR(B.START_DT, 'DD/MM/YYYY') FROM DLNG_SN_PLANT_MST A, DLNG_SN_MST B WHERE "
				+ "A.CUSTOMER_CD = B.CUSTOMER_CD  AND A.FLSA_NO = B.FLSA_NO  AND A.FLSA_REV_NO = B.FLSA_REV_NO "
				+ "AND A.SN_NO = B.SN_NO AND B.SN_REV_NO = A.SN_REV_NO ";
		stmt = conn.prepareStatement(queryString);
		rset = stmt.executeQuery();
		while(rset.next())
		{
			cd=rset.getString(1);			
			no=rset.getString(2);
			rev=rset.getString(3);
			cont_no=rset.getString(4);
			cont_rev=rset.getString(5);
			plant_seq = rset.getString(6);
			eff_dt = rset.getString(7);
		
		queryString1 = "SELECT NULL, A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO, 'F', A.BILLING_FREQ,A.FLAG, A.DUE_DATE, NULL,"
				+ "A.INVOICE_CUR_CD, A.PAYMENT_CUR_CD, A.INT_CAL_RATE_CD, A.INT_CAL_SIGN, A.INT_CAL_PERCENTAGE,A.EXCHNG_RATE_CD, A.EXCHNG_RATE_CAL,"
				+ "A.INV_CRITERIA, A.EXCHG_RATE_NOTE, A.TAX_STRUCT_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD,"
				+ "NULL, NULL, 'B', NULL, NULL,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY'), NULL, NULL, NULL, NULL"
				+ " FROM DLNG_SN_BILLING_DTL A "
				+ " WHERE A.CUSTOMER_CD = ? AND A.FLSA_NO = ? AND A.FLSA_REV_NO = ? "
				+ "AND A.SN_NO = ? "
				+ "AND A.SN_REV_NO = ? "
				+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
				+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";

		stmt1 = conn.prepareStatement(queryString1);
		stmt1.setString(1, cd);
		stmt1.setString(2, no);
		stmt1.setString(3, rev);
		stmt1.setString(4, cont_no);
		stmt1.setString(5, cont_rev);
		stmt1.setString(6, delta_FromDt);
		stmt1.setString(7, delta_FromDt);
		stmt1.setString(8, delta_ToDt);
		stmt1.setString(9, delta_ToDt);
		rset1 = stmt1.executeQuery();
		logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP,", conn);
		
		while(rset1.next())
		{	
				

					row = spreadsheet.createRow(nrow++);																								
					for (int i = 0; i < columns.split(",").length; i++) {
						
						if (i == 0) {
						queryString3="SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD=?";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, cd);
						rset3 = stmt3.executeQuery();
						   if(rset3.next()) {
							   abbr = rset3.getString(1);
							   map = abbr + "-" + plant_seq;
							   if (mpe.counterparty_map.containsKey(abbr)) {
									abbr =mpe.counterparty_map.get(abbr); 
							
							   }
						    
						   }
						   stmt3.close();
						   rset3.close();
						   cell = row.createCell(i);
						    cell.setCellValue("'"+abbr+"'");
						}
						else if(i==1) {
							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							cell = row.createCell(i);
						    cell.setCellValue("'"+cont_ref+"'");
						}
						else if(i==13) {
							value = rset1.getString(14);

								queryString3="SELECT INT_RATE_NM FROM FMS7_CONT_INT_RATE_MST WHERE INT_RATE_CD=?";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								   if(rset3.next()) {	
									   name = rset3.getString(1);
									   name= name.toUpperCase(); 
									   if(name.equals("SB BR")) {
											name = "SBI BR";
										}
								   }							   								  
								   stmt3.close();
								   rset3.close();
								    cell = row.createCell(i);
								    cell.setCellValue("'"+name+"'");
						}
						else if(i == 16) {
							value = rset1.getString(17);
							queryString3="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, value);
							rset3 = stmt3.executeQuery();
							   if(rset3.next()) {	
								   name = rset3.getString(1);
								   name= name.toUpperCase();
								   if (name.contains("CUSTOMS RATE")) {
										name = "CUSTOM EXCHANGE RATE";
									}
									else if (name.contains("RBI REFERENCE")) {
										name = "RBI REFERENCE RATE";
									}
									else if (name.contains("SBI MUMBAI TT AVERAGE")) {
										name = "SBI MUMBAI TT BUY SELL";
									}
									else if (name.contains("SBI TT BUYING")) {
										name = "SBI RATE BUY";
									}
									else if (name.contains("SBI TT SELLING")) {
										name = "SBI RATE SELL";
									}
									else if (name.contains("SBI TT BUY SELL")) {
										name = "SBI RATE BUY SELL";
									}								
							   }								   								  
							   stmt3.close();
							   rset3.close();
							    cell = row.createCell(i);

							   cell.setCellValue("'"+name.trim()+"'");
						}
						
						else if(cont_rev.equals("0") && i == 28) {	//EFF_DT
							value = eff_dt;
							cell = row.createCell(i);
							cell.setCellValue("'"+value+"'");
						}
						
						else if (i== 31) {
							if (mpe.customer_map.containsKey(map)) {
								map =mpe.counterparty_map.get(map); 
								plant_seq = map.split("-")[0];
							}
							value = plant_seq;
							cell = row.createCell(i);
							cell.setCellValue("'"+value+"'");
						}
						
						else {
						value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
						cell = row.createCell(i);																					
						cell.setCellValue("'" + value + "'");									
						}
						
					}	
					count++;
					logger.data(fname, (cd + "," +cont_type+ "," +no+ "," +rev+"," +cont_no+ "," +cont_rev+ "," +plant_seq+","), conn, "");
														
			}					
			stmt1.close();
			rset1.close();

		//PLANT FROM BUYER NOMINATION
			plant_seq = rset.getString(6);
			name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
			cont_type = "S";
			queryString2 = "SELECT DISTINCT(A.NOM_ID)"
					+ "FROM DLNG_DAILY_TRUCK_NOM_DTL A WHERE A.CONTRACT_TYPE = ? AND A.MAPPING_ID LIKE ? AND A.NOM_ID NOT LIKE ? " ;
			stmt2 = conn.prepareStatement(queryString2);
			
			stmt2.setString(1, cont_type);
			stmt2.setString(2, name+"%");
			stmt2.setString(3, name+"%"+plant_seq);
			rset2 = stmt2.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
			
			while (rset2.next()) {
				
				plant_seq = rset2.getString(1).split("-")[6];
		
		queryString1 = "SELECT NULL, A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO, 'F', A.BILLING_FREQ,A.FLAG, A.DUE_DATE, NULL,"
				+ "A.INVOICE_CUR_CD, A.PAYMENT_CUR_CD, A.INT_CAL_RATE_CD, A.INT_CAL_SIGN, A.INT_CAL_PERCENTAGE,A.EXCHNG_RATE_CD, A.EXCHNG_RATE_CAL,"
				+ "A.INV_CRITERIA, A.EXCHG_RATE_NOTE, A.TAX_STRUCT_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD,"
				+ "NULL, NULL, 'B', NULL, NULL,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY'), NULL, NULL, NULL, NULL"
				+ " FROM DLNG_SN_BILLING_DTL A "
				+ " WHERE A.CUSTOMER_CD = ? AND A.FLSA_NO = ? AND A.FLSA_REV_NO = ? "
				+ "AND A.SN_NO = ? "
				+ "AND A.SN_REV_NO = ? "
				+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
				+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
		
		stmt1 = conn.prepareStatement(queryString1);
		stmt1.setString(1, cd);
		stmt1.setString(2, no);
		stmt1.setString(3, rev);
		stmt1.setString(4, cont_no);
		stmt1.setString(5, cont_rev);
		stmt1.setString(6, delta_FromDt);
		stmt1.setString(7, delta_FromDt);
		stmt1.setString(8, delta_ToDt);
		stmt1.setString(9, delta_ToDt);
		rset1 = stmt1.executeQuery();
		logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP,", conn);
		
		while(rset1.next())
		{	
			
			
			row = spreadsheet.createRow(nrow++);																								
			for (int i = 0; i < columns.split(",").length; i++) {
				
				if (i == 0) {
					queryString3="SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD=?";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, cd);
					rset3 = stmt3.executeQuery();
					if(rset3.next()) {
						abbr = rset3.getString(1);
						map = abbr + "-" + plant_seq;
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
							
						}
						
					}
					stmt3.close();
					rset3.close();
					cell = row.createCell(i);
					cell.setCellValue("'"+abbr+"'");
				}
				else if(i==1) {
					cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
					cell = row.createCell(i);
					cell.setCellValue("'"+cont_ref+"'");
				}
				else if(i==13) {
					value = rset1.getString(14);
					
					queryString3="SELECT INT_RATE_NM FROM FMS7_CONT_INT_RATE_MST WHERE INT_RATE_CD=?";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, value);
					rset3 = stmt3.executeQuery();
					if(rset3.next()) {	
						name = rset3.getString(1);
						name= name.toUpperCase(); 
						if(name.equals("SB BR")) {
							name = "SBI BR";
						}
					}							   								  
					stmt3.close();
					rset3.close();
					cell = row.createCell(i);
					cell.setCellValue("'"+name+"'");
				}
				else if(i == 16) {
					value = rset1.getString(17);
					queryString3="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, value);
					rset3 = stmt3.executeQuery();
					if(rset3.next()) {	
						name = rset3.getString(1);
						name= name.toUpperCase();
						if (name.contains("CUSTOMS RATE")) {
							name = "CUSTOM EXCHANGE RATE";
						}
						else if (name.contains("RBI REFERENCE")) {
							name = "RBI REFERENCE RATE";
						}
						else if (name.contains("SBI MUMBAI TT AVERAGE")) {
							name = "SBI MUMBAI TT BUY SELL";
						}
						else if (name.contains("SBI TT BUYING")) {
							name = "SBI RATE BUY";
						}
						else if (name.contains("SBI TT SELLING")) {
							name = "SBI RATE SELL";
						}
						else if (name.contains("SBI TT BUY SELL")) {
							name = "SBI RATE BUY SELL";
						}								
					}								   								  
					stmt3.close();
					rset3.close();
					cell = row.createCell(i);
					
					cell.setCellValue("'"+name.trim()+"'");
				}
				
				else if(cont_rev.equals("0") && i == 28) {	//EFF_DT
					value = eff_dt;
					cell = row.createCell(i);
					cell.setCellValue("'"+value+"'");
				}
				
				else if (i== 31) {
					if (mpe.customer_map.containsKey(map)) {
						map =mpe.counterparty_map.get(map); 
						plant_seq = map.split("-")[0];
					}
					value = plant_seq;
					cell = row.createCell(i);
					cell.setCellValue("'"+value+"'");
				}
				
				else {
					value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
					cell = row.createCell(i);																					
					cell.setCellValue("'" + value + "'");									
				}
				
			}	
			count++;
			logger.data(fname, (cd + "," +cont_type+ "," +no+ "," +rev+","), conn, "");
			
			}					
			stmt1.close();
			rset1.close();
		}
		stmt2.close();
		rset2.close();
		
		
	}
		rset.close();
		stmt.close();
			
			// LOA 
		queryString = "SELECT A.CUSTOMER_CD,A.TENDER_NO,'0',A.LOA_NO,A.LOA_REV_NO,A.PLANT_SEQ_NO,TO_CHAR(B.START_DT, 'DD/MM/YYYY') FROM DLNG_LOA_PLANT_MST A, DLNG_LOA_MST B WHERE "
				+ "A.CUSTOMER_CD = B.CUSTOMER_CD  AND A.TENDER_NO = B.TENDER_NO "
				+ "AND A.LOA_NO = B.LOA_NO AND B.LOA_REV_NO = A.LOA_REV_NO ";
		stmt = conn.prepareStatement(queryString);
		rset = stmt.executeQuery();
		while(rset.next())
		{
			cd=rset.getString(1);			
			no=rset.getString(2);
			rev=rset.getString(3);
			cont_no=rset.getString(4);
			cont_rev=rset.getString(5);
			plant_seq = rset.getString(6);
			eff_dt = rset.getString(7);
			
			queryString1 = "SELECT NULL, A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO, 'E', A.BILLING_FREQ,A.FLAG, A.DUE_DATE, NULL,"
					+ "A.INVOICE_CUR_CD, A.PAYMENT_CUR_CD, A.INT_CAL_RATE_CD, A.INT_CAL_SIGN, A.INT_CAL_PERCENTAGE,A.EXCHNG_RATE_CD, A.EXCHNG_RATE_CAL,"
					+ "A.INV_CRITERIA, A.EXCHG_RATE_NOTE, A.TAX_STRUCT_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD,"
					+ "NULL, NULL, 'B', NULL, NULL,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY'), NULL, NULL, NULL, NULL "
					+ " FROM DLNG_SN_BILLING_DTL A "
					+ "  WHERE A.CUSTOMER_CD = ? AND A.FLSA_NO = ? AND FLSA_REV_NO = ? AND A.SN_NO = ? "
					+ "  AND A.SN_REV_NO = ? AND A.CONT_TYPE ='L' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
					+ "  AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";

			stmt1 = conn.prepareStatement(queryString1);	
			stmt1.setString(1, cd);
			stmt1.setString(2, no);
			stmt1.setString(3, rev);
			stmt1.setString(4, cont_no);
			stmt1.setString(5, cont_rev);
			stmt1.setString(6, delta_FromDt);
			stmt1.setString(7, delta_FromDt);
			stmt1.setString(8, delta_ToDt);
			stmt1.setString(9, delta_ToDt);
			rset1 = stmt1.executeQuery();
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP,", conn);
			
			while(rset1.next())
			{	
				cd=rset1.getString(2);			
				no=rset1.getString(3);
				rev=rset1.getString(4);
				cont_no=rset1.getString(5);
				cont_rev=rset1.getString(6);	

					row = spreadsheet.createRow(nrow++);																								
					for (int i = 0; i < columns.split(",").length; i++) {
						
						if (i == 0) {
						queryString3="SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD=?";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, cd);
						rset3 = stmt3.executeQuery();
						   if(rset3.next()) {
						   abbr = rset3.getString(1); 
						   map = abbr + "-" + plant_seq;
						   if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
						   }
						   }
						   stmt3.close();
						   rset3.close();
						  
						   cell = row.createCell(i);
						    cell.setCellValue("'"+abbr+"'");
						}
						else if(i == 1) {
							cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
							cell = row.createCell(i);
						    cell.setCellValue("'" + cont_ref + "'");
						}
						else if(i==13) {
							value = rset1.getString(14);

								queryString3="SELECT INT_RATE_NM FROM FMS7_CONT_INT_RATE_MST WHERE INT_RATE_CD=?";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								   if(rset3.next()) {	
									   name = rset3.getString(1);
									   name= name.toUpperCase();
									   if(name.equals("SB BR")) {
											name = "SBI BR";
										}
								   }							   								  
								   stmt3.close();
								   rset3.close();
								    cell = row.createCell(i);
								    cell.setCellValue("'"+name+"'");
						}
						else if(i == 16) {
							value = rset1.getString(17);
							queryString3="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, value);
							rset3 = stmt3.executeQuery();
							   if(rset3.next()) {	
								   name = rset3.getString(1);
								   name= name.toUpperCase();
								   if (name.contains("CUSTOMS RATE")) {
										name = "CUSTOM EXCHANGE RATE";
									}
									else if (name.contains("RBI REFERENCE")) {
										name = "RBI REFERENCE RATE";
									}
									else if (name.contains("SBI MUMBAI TT AVERAGE")) {
										name = "SBI MUMBAI TT BUY SELL";
									}
									else if (name.contains("SBI TT BUYING")) {
										name = "SBI RATE BUY";
									}
									else if (name.contains("SBI TT SELLING")) {
										name = "SBI RATE SELL";
									}
									else if (name.contains("SBI TT BUY SELL")) {
										name = "SBI RATE BUY SELL";
									}								
							   }								   								  
							   stmt3.close();
							   rset3.close();
							    cell = row.createCell(i);

							   cell.setCellValue("'"+name.trim()+"'");
						}

						else if(cont_rev.equals("0") && i == 28) {	//EFF_DT
							value = eff_dt;
							cell = row.createCell(i);
							cell.setCellValue("'"+value+"'");
						}
						
						else if (i== 31) {
							if (mpe.customer_map.containsKey(map)) {
								map =mpe.counterparty_map.get(map); 
								plant_seq = map.split("-")[0];
							}
							value = plant_seq;
							cell = row.createCell(i);
							cell.setCellValue("'"+value+"'");
						}
						else {
						value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
						cell = row.createCell(i);																					
						cell.setCellValue("'" + value + "'");									
						}
						
					}	
					count++;
					logger.data(fname, (cd + "," +cont_type+ "," +no+ "," +rev+","), conn, "");
				
//				
//			}	
//				stmt2.close();
//				rset2.close();											
				}					
				stmt1.close();
				rset1.close();
				
				

				//PLANT FROM BUYER NOMINATION
					plant_seq = rset.getString(6);
					name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
					cont_type = "S";
					queryString2 = "SELECT DISTINCT(A.NOM_ID)"
							+ "FROM DLNG_DAILY_TRUCK_NOM_DTL A WHERE A.CONTRACT_TYPE = ? AND A.MAPPING_ID LIKE ? AND A.NOM_ID NOT LIKE ? " ;
					stmt2 = conn.prepareStatement(queryString2);
					
					stmt2.setString(1, cont_type);
					stmt2.setString(2, name+"%");
					stmt2.setString(3, name+"%"+plant_seq);
					rset2 = stmt2.executeQuery();
					
					logger.checkpoint(fname, "COUNTERPARTY_ABBR,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
					
					while (rset2.next()) {
						
						plant_seq = rset2.getString(1).split("-")[6];
				
				queryString1 = "SELECT NULL, A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO, 'F', A.BILLING_FREQ,A.FLAG, A.DUE_DATE, NULL,"
						+ "A.INVOICE_CUR_CD, A.PAYMENT_CUR_CD, A.INT_CAL_RATE_CD, A.INT_CAL_SIGN, A.INT_CAL_PERCENTAGE,A.EXCHNG_RATE_CD, A.EXCHNG_RATE_CAL,"
						+ "A.INV_CRITERIA, A.EXCHG_RATE_NOTE, A.TAX_STRUCT_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD,"
						+ "NULL, NULL, 'B', NULL, NULL,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY'), NULL, NULL, NULL, NULL"
						+ " FROM DLNG_SN_BILLING_DTL A "
						+ " WHERE A.CUSTOMER_CD = ? AND A.FLSA_NO = ? AND A.FLSA_REV_NO = ? "
						+ "AND A.SN_NO = ? "
						+ "AND A.SN_REV_NO = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, cd);
				stmt1.setString(2, no);
				stmt1.setString(3, rev);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, cont_rev);
				stmt1.setString(6, delta_FromDt);
				stmt1.setString(7, delta_FromDt);
				stmt1.setString(8, delta_ToDt);
				stmt1.setString(9, delta_ToDt);
				rset1 = stmt1.executeQuery();
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP,", conn);
				
				while(rset1.next())
				{	
					
					
					row = spreadsheet.createRow(nrow++);																								
					for (int i = 0; i < columns.split(",").length; i++) {
						
						if (i == 0) {
							queryString3="SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD=?";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, cd);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								abbr = rset3.getString(1);
								map = abbr + "-" + plant_seq;
								if (mpe.counterparty_map.containsKey(abbr)) {
									abbr =mpe.counterparty_map.get(abbr); 
									
								}
								
							}
							stmt3.close();
							rset3.close();
							cell = row.createCell(i);
							cell.setCellValue("'"+abbr+"'");
						}
						else if(i==1) {
							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							cell = row.createCell(i);
							cell.setCellValue("'"+cont_ref+"'");
						}
						else if(i==13) {
							value = rset1.getString(14);
							
							queryString3="SELECT INT_RATE_NM FROM FMS7_CONT_INT_RATE_MST WHERE INT_RATE_CD=?";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, value);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {	
								name = rset3.getString(1);
								name= name.toUpperCase(); 
								if(name.equals("SB BR")) {
									name = "SBI BR";
								}
							}							   								  
							stmt3.close();
							rset3.close();
							cell = row.createCell(i);
							cell.setCellValue("'"+name+"'");
						}
						else if(i == 16) {
							value = rset1.getString(17);
							queryString3="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, value);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {	
								name = rset3.getString(1);
								name= name.toUpperCase();
								if (name.contains("CUSTOMS RATE")) {
									name = "CUSTOM EXCHANGE RATE";
								}
								else if (name.contains("RBI REFERENCE")) {
									name = "RBI REFERENCE RATE";
								}
								else if (name.contains("SBI MUMBAI TT AVERAGE")) {
									name = "SBI MUMBAI TT BUY SELL";
								}
								else if (name.contains("SBI TT BUYING")) {
									name = "SBI RATE BUY";
								}
								else if (name.contains("SBI TT SELLING")) {
									name = "SBI RATE SELL";
								}
								else if (name.contains("SBI TT BUY SELL")) {
									name = "SBI RATE BUY SELL";
								}								
							}								   								  
							stmt3.close();
							rset3.close();
							cell = row.createCell(i);
							
							cell.setCellValue("'"+name.trim()+"'");
						}
						
						else if(cont_rev.equals("0") && i == 28) {	//EFF_DT
							value = eff_dt;
							cell = row.createCell(i);
							cell.setCellValue("'"+value+"'");
						}
						
						else if (i== 31) {
							if (mpe.customer_map.containsKey(map)) {
								map =mpe.counterparty_map.get(map); 
								plant_seq = map.split("-")[0];
							}
							value = plant_seq;
							cell = row.createCell(i);
							cell.setCellValue("'"+value+"'");
						}
						
						else {
							value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
							cell = row.createCell(i);																					
							cell.setCellValue("'" + value + "'");									
						}
						
					}	
					count++;
					logger.data(fname, (cd + "," +cont_type+ "," +no+ "," +rev+","), conn, "");
					
					}					
					stmt1.close();
					rset1.close();
				}
				stmt2.close();
				rset2.close();
				
		}
		rset.close();
		stmt.close();

				
					
		
		filename = migration_setup_dir + "EXPORT/FMS_SUPPLY_BILLING_DTL(DLNG)_"+start_end_dt+".xlsx";
		
		fileOut = new FileOutputStream(filename);  
		
		workbook.write(fileOut);
		fileOut.close(); 

		logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
		logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_BILLING_DTL(DLNG)>>,,,,", conn);
		
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
	}
	
}
	

//FMS_SUPPLY_CFORM_DTL
	public void FMS_SUPPLY_CFORM_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_SUPPLY_CFORM_DTL(DLNG)()";
		
		try {
	
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SUPPLY_CFORM_DTL(DLNG)>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
	
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,BU_SEQ,COMMODITY_TYPE,CFORM_FLAG,EFF_DT,ENT_DT,ENT_BY,MOD_DT,MOD_BY";
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
	
			nrow = 0;
			ncell = 0;
			count = 0;
			String plant_seq= "",eff_dt="";
			
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// SN 
			queryString = "SELECT DISTINCT(A.CUSTOMER_CD), A.EFF_DT  FROM FMS7_CUSTOMER_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) "
					+ "ORDER BY A.CUSTOMER_CD, A.EFF_DT DESC  ";
			        stmt = conn.prepareStatement(queryString);
					rset = stmt.executeQuery();
					while(rset.next())
					{
						cd=rset.getString(1);			
						queryString2 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY') "
								+ " FROM DLNG_SN_BILLING_DTL A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND A.CONT_TYPE = 'S' "
								+ "AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
								+ "AND B.CUSTOMER_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
								+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
	//					stmt2.setString(2, no);
	//					stmt2.setString(3, rev);
	//					stmt2.setString(4, cont_no);
	//					stmt2.setString(5, cont_rev);
						stmt2.setString(2, delta_FromDt);
						stmt2.setString(3, delta_FromDt);
						stmt2.setString(4, delta_ToDt);
						stmt2.setString(5, delta_ToDt);
						rset2 = stmt2.executeQuery();
						while(rset2.next()) {
							eff_dt = rset2.getString(6);
						
						queryString1 = "SELECT NULL, A.CUSTOMER_CD, A.AGR_NO, A.AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONTRACT_TYPE,A.PLANT_SEQ_NO,"
								+ "NULL, A.COMMODITY_TYPE, A.CFORM_FLAG, NULL,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.ENT_BY, NULL, NULL"
								+ " FROM FMS7_CFORM_CONTRACT_DTL A "
								+ " WHERE A.CUSTOMER_CD = ? AND A.AGR_NO = ? AND A.AGR_REV_NO = ? "
								+ "AND A.CONTRACT_NO = ? AND A.CONTRACT_REV_NO = ? AND A.CONTRACT_TYPE = 'S' "
								+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
								+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
	
						stmt1 = conn.prepareStatement(queryString1);	
						stmt1.setString(1, rset2.getString(1));
						stmt1.setString(2, rset2.getString(2));
						stmt1.setString(3, rset2.getString(3));
						stmt1.setString(4, rset2.getString(4));
						stmt1.setString(5, rset2.getString(5));
						stmt1.setString(6, delta_FromDt);
						stmt1.setString(7, delta_FromDt);
						stmt1.setString(8, delta_ToDt);
						stmt1.setString(9, delta_ToDt);
						rset1 = stmt1.executeQuery();
						logger.checkpoint(fname, "ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP,", conn);
						
						while(rset1.next())
						{	
							cd=rset1.getString(2);			
							no=rset1.getString(3);
							rev=rset1.getString(4);
							cont_no=rset1.getString(5);
							cont_rev=rset1.getString(6);	
							
							
							row = spreadsheet.createRow(nrow++);																								
								for (int i = 0; i < columns.split(",").length; i++) {
									
									if (i == 0) {
									queryString3="SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD=?";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, cd);
									rset3 = stmt3.executeQuery();
									   if(rset3.next()) {
									   abbr = rset3.getString(1); 
									   map = abbr + "-" + plant_seq;
									   if (mpe.counterparty_map.containsKey(abbr)) {
											abbr =mpe.counterparty_map.get(abbr); 
									   }
									   }
									   stmt3.close();
									   rset3.close();
									  
									   cell = row.createCell(i);
									    cell.setCellValue("'"+abbr+"'");
									}
									else if(i==1) {
										cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
										cell = row.createCell(i);
									    cell.setCellValue("'"+cont_ref+"'");
									}
									else if(i==6) {
										cont_type = rset1.getString(7);
										if(cont_type.equals("S")) {
											cont_type = "F";
										}
										cell = row.createCell(i);
										cell.setCellValue("'"+cont_type+"'");
									}
									
									else if(i == 11) {
										value = eff_dt;
										cell = row.createCell(i);
										cell.setCellValue("'"+value+"'");
									}
	
									else {
									value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
									cell = row.createCell(i);																					
									cell.setCellValue("'" + value + "'");									
									}
									
								}	
								count++;
								logger.data(fname, ("2"+","+cd + ","+no+ "," +rev+","+cont_no+","+cont_rev+","+"F"+","), conn, "");
							
	//						
				     												
							}					
							stmt1.close();
							rset1.close();
						}	
						stmt2.close();
						rset2.close();
					}
					rset.close();
					stmt.close();
				
				// LOA 
					queryString = "SELECT DISTINCT(A.CUSTOMER_CD), A.EFF_DT  FROM FMS7_CUSTOMER_MST A "
							+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) "
							+ "ORDER BY A.CUSTOMER_CD, A.EFF_DT DESC  ";
					        stmt = conn.prepareStatement(queryString);
							rset = stmt.executeQuery();
							while(rset.next())
							{
								cd=rset.getString(1);			
								queryString2 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY') "
										+ " FROM DLNG_SN_BILLING_DTL A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND A.CONT_TYPE = 'L' "
										+ "AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
										+ "AND B.CUSTOMER_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
										+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, cd);
	//							stmt2.setString(2, no);
	//							stmt2.setString(3, rev);
	//							stmt2.setString(4, cont_no);
	//							stmt2.setString(5, cont_rev);
								stmt2.setString(2, delta_FromDt);
								stmt2.setString(3, delta_FromDt);
								stmt2.setString(4, delta_ToDt);
								stmt2.setString(5, delta_ToDt);
								rset2 = stmt2.executeQuery();
								while(rset2.next()) {
									eff_dt = rset2.getString(6);
								
								queryString1 = "SELECT NULL, A.CUSTOMER_CD, A.AGR_NO, A.AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONTRACT_TYPE,A.PLANT_SEQ_NO,"
										+ "NULL, A.COMMODITY_TYPE, A.CFORM_FLAG, NULL,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.ENT_BY, NULL, NULL"
										+ " FROM FMS7_CFORM_CONTRACT_DTL A "
										+ " WHERE A.CUSTOMER_CD = ? AND A.AGR_NO = ? AND A.AGR_REV_NO = ? "
										+ "AND A.CONTRACT_NO = ? AND A.CONTRACT_REV_NO = ? AND A.CONTRACT_TYPE = 'L' "
										+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
										+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
	
								stmt1 = conn.prepareStatement(queryString1);	
								stmt1.setString(1, rset2.getString(1));
								stmt1.setString(2, rset2.getString(2));
								stmt1.setString(3, rset2.getString(3));
								stmt1.setString(4, rset2.getString(4));
								stmt1.setString(5, rset2.getString(5));
								stmt1.setString(6, delta_FromDt);
								stmt1.setString(7, delta_FromDt);
								stmt1.setString(8, delta_ToDt);
								stmt1.setString(9, delta_ToDt);
								rset1 = stmt1.executeQuery();
								logger.checkpoint(fname, "COLUMN_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP,", conn);
								
								while(rset1.next())
								{	
									cd=rset1.getString(2);			
									no=rset1.getString(3);
									rev=rset1.getString(4);
									cont_no=rset1.getString(5);
									cont_rev=rset1.getString(6);	
	
	
										row = spreadsheet.createRow(nrow++);																								
										for (int i = 0; i < columns.split(",").length; i++) {
											
											if (i == 0) {
											queryString3="SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD=?";
											stmt3 = conn.prepareStatement(queryString3);
											stmt3.setString(1, cd);
											rset3 = stmt3.executeQuery();
											   if(rset3.next()) {
											   abbr = rset3.getString(1); 
											   map = abbr + "-" + plant_seq;
											   if (mpe.counterparty_map.containsKey(abbr)) {
													abbr =mpe.counterparty_map.get(abbr); 
											   }
											   }
											   stmt3.close();
											   rset3.close();
											  
											   cell = row.createCell(i);
											    cell.setCellValue("'"+abbr+"'");
											}
											else if(i == 1) {
											cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
											cell = row.createCell(i);
										    cell.setCellValue("'" + cont_ref + "'");
										}
											else if(i==6) {
												cont_type = rset1.getString(7);
												if(cont_type.equals("L")) {
													cont_type = "E";
												}
												cell = row.createCell(i);
												cell.setCellValue("'"+cont_type+"'");
											}

											else if(i == 11) {
												value = eff_dt;
												cell = row.createCell(i);
												cell.setCellValue("'"+value+"'");
											}
			
											else {
											value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
											cell = row.createCell(i);																					
											cell.setCellValue("'" + value + "'");									
											}
											
										}	
										count++;
										logger.data(fname, ("2"+","+cd + ","+no+ "," +rev+","+cont_no+","+cont_rev+","+"E"+","), conn, "");
									
	//								
						     												
									}					
									stmt1.close();
									rset1.close();
								}	
								stmt2.close();
								rset2.close();
							}
							rset.close();
							stmt.close();
					
						
			
			filename = migration_setup_dir + "EXPORT/FMS_SUPPLY_CFORM_DTL(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
	
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_CFORM_DTL(DLNG)>>,,,,", conn);
			
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
		}
		
	}
	
	public void FMS_CFORM_MST() throws SQLException, IOException {
		
		function_nm = "FMS_CFORM_MST()";
		
		try {
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_CFORM_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,CFORM_CD,CFORM_NO,CFORM_DT,COUNTERPARTY_CD,FINANCIAL_YEAR,ISSUING_STATE,PERIOD_FROM,PERIOD_TO,CFORM_AMOUNT,CFORM_FILE,NO_OF_INVOICES,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			ncell = 0;
			count = 0;
			String state_code= "",cform_no="",ent_dt="";
			int cform_cd = 1;
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = "SELECT DISTINCT(A.CUSTOMER_CD), A.EFF_DT  FROM FMS7_CUSTOMER_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) "
					+ "ORDER BY A.CUSTOMER_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				cd=rset.getString(1);			
					
					queryString1 = "SELECT '2', NULL, A.CFORM_NO, TO_CHAR(A.CFORM_DT,'DD/MM/YYYY HH24:MI:SS'), A.CUSTOMER_CD, A.FINANCIAL_YEAR, A.ISSUING_STATE, "
							+ "TO_CHAR(A.PERIOD_FROM,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.PERIOD_TO,'DD/MM/YYYY HH24:MI:SS'), A.CFORM_AMOUNT, A.CFORM_FILE, "
							+ "A.NO_OF_INVOICES, A.ENT_BY, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL "
							+ " FROM FMS7_CFORM_MST A "
							+ " WHERE A.CUSTOMER_CD = ? ";
//							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					
					stmt1 = conn.prepareStatement(queryString1);	
					stmt1.setString(1, cd);
//					stmt1.setString(2, delta_FromDt);
//					stmt1.setString(3, delta_FromDt);
//					stmt1.setString(4, delta_ToDt);
//					stmt1.setString(5, delta_ToDt);
					rset1 = stmt1.executeQuery();
					logger.checkpoint(fname, "COMPANY_CD,CUSTOMER_CD,ABBR,CFORM_NO,ISSUING_STATE,ENT_DT,TIMESTAMP,", conn);
					
					while(rset1.next())
					{	
						cform_no = rset1.getString(3);
						state_code = rset1.getString(7);
						
						row = spreadsheet.createRow(nrow++);																								
						for (int i = 0; i < columns.split(",").length; i++) {
							
						if(i == 1) {	//CFORM_CD
							value = cform_cd+"";
							cform_cd++;
							cell= row.createCell(i);
							cell.setCellValue("'"+value+"'");
						}
							
						else if (i == 4) {	//COUNTERPARTY_CD
								queryString3="SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD=?";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									abbr = rset3.getString(1); 
									if (mpe.counterparty_map.containsKey(abbr)) {
										abbr =mpe.counterparty_map.get(abbr); 
									}
								}
								stmt3.close();
								rset3.close();
								
								cell = row.createCell(i);
								cell.setCellValue("'"+abbr+"'");
							}
							else if(i == 6) {	//ISSUING_STATE
								if(state_code.length()<2) {
									state_code = "0"+state_code;
								}
								
								queryString3="SELECT UPPER(STATE_NM) FROM STATE_MST WHERE STATE_CODE = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, state_code);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									state_code = rset3.getString(1); 
								}
								stmt3.close();
								rset3.close();
								
								cell = row.createCell(i);
								cell.setCellValue("'"+state_code+"'");
							}
							
							else {
								value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
								cell = row.createCell(i);																					
								cell.setCellValue("'" + value + "'");									
							}
							
						}	
						count++;
						logger.data(fname, ("2"+","+cd + ","+abbr+ "," +cform_no+","+state_code+","+ent_dt+","), conn, "");
						
						
					}					
					stmt1.close();
					rset1.close();
			}
			rset.close();
			stmt.close();
			
			
			
			
			filename = migration_setup_dir + "EXPORT/FMS_CFORM_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_CFORM_MST>>,,,,", conn);
			
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
		}
		
	}
	
	
	public void FMS_CFORM_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_CFORM_DTL()";
		
		try {
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_CFORM_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,CFORM_CD,INVOICE_NO,INVOICE_DT,INVOICE_AMOUNT,COMMODITY_TYPE,ENT_BY,ENT_DT";
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			ncell = 0;
			count = 0;
			String inv_no= "",inv_dt="",cform_no="",ent_dt="";
			
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
				queryString1 = "SELECT  '2', A.CFORM_NO, A.INVOICE_NO, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "A.INVOICE_AMOUNT, A.COMMODITY_TYPE, NULL, NULL "
						+ " FROM FMS7_CFORM_DTL A "
						+ " WHERE A.COMMODITY_TYPE = 'DLNG' ";
				
				stmt1 = conn.prepareStatement(queryString1);	
				rset1 = stmt1.executeQuery();
				logger.checkpoint(fname, "COMPANY_CD,CFORM_NO,INVOICE_NO,INVOICE_DT,ENT_DT,TIMESTAMP,", conn);
				
				while(rset1.next())
				{	
					cform_no = rset1.getString(2);
					inv_no = rset1.getString(3);
					inv_dt = rset1.getString(4);
					
					row = spreadsheet.createRow(nrow++);																								
					for (int i = 0; i < columns.split(",").length; i++) {
						
						if (i == 6) {	//ENT_BY
							queryString3="SELECT ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_CFORM_MST WHERE CFORM_NO = ? ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, cform_no);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								value = rset3.getString(1); 
								ent_dt = rset3.getString(2); 
							}
							stmt3.close();
							rset3.close();
							
							cell = row.createCell(i);
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 7) {	//ENT_DT
							cell = row.createCell(i);
							cell.setCellValue("'"+ent_dt+"'");
						}
						
						else {
							value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
							cell = row.createCell(i);																					
							cell.setCellValue("'" + value + "'");									
						}
						
					}	
					count++;
					logger.data(fname, ("2"+","+cform_no+","+inv_no+","+inv_dt+","+ent_dt+","), conn, "");
					
					
				}					
				stmt1.close();
				rset1.close();
			
			
			
			
			filename = migration_setup_dir + "EXPORT/FMS_CFORM_DTL_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_CFORM_DTL>>,,,,", conn);
			
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
		}
		
	}
	


//FMS_SUPPLY_CONT_TRUCK_TRANS
	public void FMS_SUPPLY_CONT_TRUCK_TRANS() throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_CONT_TRUCK_TRANS()";
		
		try {
			
			System.out.println("<<START>><<FMS_SUPPLY_CONT_TRUCK_TRANS>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SUPPLY_CONT_TRUCK_TRANS>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRANSPORTER_CD,ENT_BY,ENT_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String  type = "",counterparty="",abbr="",clause="",trans_cd="",name="",cont_type="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			//SN
			queryString1 = "SELECT CUSTOMER_CD,SN_NO,SN_REV_NO, FLSA_NO, FLSA_REV_NO FROM DLNG_SN_MST ORDER BY CUSTOMER_CD";
			stmt1 = conn.prepareStatement(queryString1);
			rset1 = stmt1.executeQuery();
			
			while(rset1.next()) {
				cd = rset1.getString(1);
				cont_no = rset1.getString(2);
				cont_rev = rset1.getString(3);
				no = rset1.getString(4);
				rev = rset1.getString(5);
				cont_type = "S";
				
	//			name = cd+"-"+no+"-"+rev+"-";
				name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
	
				queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, cd);
				rset2 = stmt2.executeQuery();
				if(rset2.next()) {
					value = rset2.getString(1);
					if(mpe.counterparty_map.containsKey(value)) {
						value = mpe.counterparty_map.get(value);
					}
					counterparty = value;
				}
				else {
					counterparty = "0";
				}
				rset2.close();
				stmt2.close();
				
				queryString = " SELECT DISTINCT(A.TRANS_CD), NULL, NULL, NULL, NULL, NULL, A.CONT_TYPE,NULL,A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM DLNG_CUST_TRANS_DTL A WHERE "
						+ "A.CONT_TYPE = 'S' AND A.CUST_CD = ? AND A. MAPPING_ID LIKE ? "
						+ "AND A.ENT_DT = (SELECT MAX(B.ENT_DT) FROM DLNG_CUST_TRANS_DTL B WHERE B.MAPPING_ID LIKE ? AND A.TRANS_CD = B.TRANS_CD)" ;
				stmt = conn.prepareStatement(queryString);
				
				stmt.setString(1, cd);
				stmt.setString(2, name+"%");
				stmt.setString(3, name+"%");
				rset = stmt.executeQuery();
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRANSPORTER_CD,TIMESTAMP,", conn);
				
				while (rset.next() && !counterparty.equals("0")){
						row = spreadsheet.createRow(nrow++);
						value = "";
						trans_cd = rset.getString(1);
						type = rset.getString(3);
						for (int i = 0; i < columns.split(",").length; i++) {
							
							cell = row.createCell(i);
							value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
							value = value.trim().equals("") ? "null" : value;
							
							
							if(i == 0) {	//COUNTERPARTY_CD
								
								value = counterparty;
							}
							else if(i==1) {
								cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
								value = cont_ref;
							}
		
							else if(i == 7) {	//COMPANY_CD(TRANSPORTER_CD)
								queryString2 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, trans_cd);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									value = rset2.getString(1);
									trans_cd = value;
								}
								rset2.close();
								stmt2.close();
							}
							else if(i == 2) {	//AGMT_NO
								value = no;
							}
							else if(i == 3) {	//AGMT_REV
								value = rev;
							}
							else if(i == 4) {	//CONT_NO
								value = cont_no;
							}
							else if(i == 5) {	//CONT_REV
								value = cont_rev;
							}
							else if(i==6) {
//								cont_type=rset.getString(7);
								if(cont_type.equals("S")) {
									cont_type = "F";
								}
								value = cont_type;
							}
							
							cell.setCellValue("'" + value + "'");
						}
						count++;
						logger.data(fname, (company_cd + "," + counterparty + "," +no + "," + rev + "," +cont_no+","+cont_rev+","+cont_type+","+trans_cd + "," ), conn, "");
					}
				
				stmt.close();
				rset.close();
//			}
//			stmt1.close();
//			rset1.close();
//			
//			
//			
//			queryString1 = "SELECT CUSTOMER_CD,SN_NO,SN_REV_NO, FLSA_NO, FLSA_REV_NO FROM DLNG_SN_MST ";
//			stmt1 = conn.prepareStatement(queryString1);
//			rset1 = stmt1.executeQuery();
//			
//			while(rset1.next()) {
//				
//				
//				cd = rset1.getString(1);
//				cont_no = rset1.getString(2);
//				cont_rev = rset1.getString(3);
//				no = rset1.getString(4);
//				rev = rset1.getString(5);
//				cont_type = "S";
				
				
				//SN FROM BUYER NOMINATION
				//			name = cd+"-"+no+"-"+rev+"-";
				name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
				cont_type = "S";
				
				
				queryString = "SELECT DISTINCT(A.TRANS_CD), NULL, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE,NULL,A.ENT_BY, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
						+ "FROM DLNG_DAILY_TRUCK_NOM_DTL A WHERE A.CONTRACT_TYPE = ? AND A.MAPPING_ID LIKE ? AND A.NOM_ID LIKE ? " ;
				stmt = conn.prepareStatement(queryString);
				
				stmt.setString(1, cont_type);
				stmt.setString(2, name+"%");
				stmt.setString(3, name+"%");
				rset = stmt.executeQuery();
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,TIMESTAMP,", conn);
				
				while (rset.next() && !counterparty.equals("0")) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					trans_cd = rset.getString(1);
//					type = rset.getString(3);
					for (int i = 0; i < columns.split(",").length; i++) {
						
						cell = row.createCell(i);
						value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
						value = value.trim().equals("") ? "null" : value;
						
						
						if(i == 0) {	//COUNTERPARTY_CD
//							queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
//							stmt2 = conn.prepareStatement(queryString2);
//							stmt2.setString(1, cd);
//							rset2 = stmt2.executeQuery();
//							if(rset2.next()) {
//								value = rset2.getString(1);
//								if(mpe.counterparty_map.containsKey(value)) {
//									value = mpe.counterparty_map.get(value);
//								}
//								counterparty = value;
//							}
//							else {
//								value = "0";
//							}
//							rset2.close();
//							stmt2.close();
							value = counterparty;
						}
						else if(i==1) {
							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
						}
						
						else if(i == 7) {	//COMPANY_CD(TRANSPORTER_CD)
							queryString2 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, trans_cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = rset2.getString(1);
								trans_cd = value;
							}
							rset2.close();
							stmt2.close();
						}
						else if(i == 2) {	//AGMT_NO
							value = no;
						}
						else if(i == 3) {	//AGMT_REV
							value = rev;
						}
						else if(i == 4) {	//CONT_NO
							value = cont_no;
						}
						else if(i == 5) {	//CONT_REV
							value = cont_rev;
						}
						else if(i==6) {
//							cont_type=rset.getString(7);
							if(cont_type.equals("S")) {
								cont_type = "F";
							}
							value = cont_type;
						}
						
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (company_cd + "," + counterparty + "," +no + "," + rev + "," +cont_no+","+cont_rev+","+cont_type+","+trans_cd + "," ), conn, "");
				}
				
				stmt.close();
				rset.close();
			}
			stmt1.close();
			rset1.close();
			
			
			//LOA
			queryString1 = "SELECT CUSTOMER_CD,LOA_NO,LOA_REV_NO,TENDER_NO,'0' FROM DLNG_LOA_MST ";
			stmt1 = conn.prepareStatement(queryString1);
			rset1 = stmt1.executeQuery();
			
			while(rset1.next()) {
				cd = rset1.getString(1);
				cont_no = rset1.getString(2);
				cont_rev = rset1.getString(3);
				no = rset1.getString(4);
				rev = rset1.getString(5);
				
				
		//		name = cd+"-"+no+"-"+rev+"-";
				name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
				queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, cd);
				rset2 = stmt2.executeQuery();
				if(rset2.next()) {
					value = rset2.getString(1);
					if(mpe.counterparty_map.containsKey(value)) {
						value = mpe.counterparty_map.get(value);
					}
					counterparty = value;
				}
				
				else {
					counterparty = "0";
				}
				rset2.close();
				stmt2.close();
				
				
			queryString = " SELECT DISTINCT(A.TRANS_CD), NULL, NULL, NULL, NULL, NULL, A.CONT_TYPE,NULL,A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM DLNG_CUST_TRANS_DTL A WHERE "
					+ "A.CONT_TYPE = 'L' AND A.CUST_CD = ? AND A. MAPPING_ID LIKE ? "
					+ "AND A.ENT_DT = (SELECT MAX(B.ENT_DT) FROM DLNG_CUST_TRANS_DTL B WHERE B.MAPPING_ID LIKE ? AND A.TRANS_CD = B.TRANS_CD)" ;
			stmt = conn.prepareStatement(queryString);
			
			stmt.setString(1, cd);
			stmt.setString(2, name+"%");
			stmt.setString(3, name+"%");
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,TIMESTAMP,", conn);
			
			while (rset.next() && !counterparty.equals("0")) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					trans_cd = rset.getString(1);
					type = rset.getString(3);
					for (int i = 0; i < columns.split(",").length; i++) {
						
						cell = row.createCell(i);
						value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
						value = value.trim().equals("") ? "null" : value;
						
						
						if(i == 0) {	//COUNTERPARTY_CD
							value = counterparty;
						}
						else if(i == 1) {
							cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
						}
		
						else if(i == 7) {	//COMPANY_CD(TRANSPORTER_CD)
							queryString2 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, trans_cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = rset2.getString(1);
								trans_cd = value;
							}
							rset2.close();
							stmt2.close();
						}
						else if(i == 2) {	//AGMT_NO
							value = no;
						}
						else if(i == 3) {	//AGMT_REV
							value = rev;
						}
						else if(i == 4) {	//CONT_NO
							value = cont_no;
						}
						else if(i == 5) {	//CONT_REV
							value = cont_rev;
						}
						else if(i==6) {
							cont_type=rset.getString(7);
							if(cont_type.equals("L")) {
								cont_type = "E";
							}
							value = cont_type;
						}
						
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (company_cd + "," + counterparty + "," +no + "," + rev + "," +cont_no+","+cont_rev+","+cont_type+","+trans_cd + "," ), conn, "");
				}
			
			stmt.close();
			rset.close();
//		}
//		stmt1.close();
//		rset1.close();
//	
//		
		
//		queryString1 = "SELECT CUSTOMER_CD,LOA_NO,LOA_REV_NO,TENDER_NO,'0' FROM DLNG_LOA_MST ";
//		stmt1 = conn.prepareStatement(queryString1);
//		rset1 = stmt1.executeQuery();
//		
//		while(rset1.next()) {
//			cd = rset1.getString(1);
//			cont_no = rset1.getString(2);
//			cont_rev = rset1.getString(3);
//			no = rset1.getString(4);
//			rev = rset1.getString(5);
			
			//LOA FROM BUYER NOMINATION
			cont_type = "L";
			
			//		name = cd+"-"+no+"-"+rev+"-";
			name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
			
			
			
			queryString = "SELECT DISTINCT(A.TRANS_CD), NULL, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE,NULL,A.ENT_BY, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
					+ "FROM DLNG_DAILY_TRUCK_NOM_DTL A WHERE A.CONTRACT_TYPE = ? AND A.MAPPING_ID LIKE ? AND A.NOM_ID LIKE ? " ;
			stmt = conn.prepareStatement(queryString);
			
			stmt.setString(1, cont_type);
			stmt.setString(2, name+"%");
			stmt.setString(3, name+"%");
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,TIMESTAMP,", conn);
			
			while (rset.next() && !counterparty.equals("0")) {
				row = spreadsheet.createRow(nrow++);
				value = "";
				trans_cd = rset.getString(1);
//				type = rset.getString(3);
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					
					if(i == 0) {	//COUNTERPARTY_CD
						queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							if(mpe.counterparty_map.containsKey(value)) {
								value = mpe.counterparty_map.get(value);
							}
							counterparty = value;
						}
						else {
							value = "0";
						}
						rset2.close();
						stmt2.close();
					}
					else if(i == 1) {
						cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
						value = cont_ref;
					}
					
					else if(i == 7) {	//COMPANY_CD(TRANSPORTER_CD)
						queryString2 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, trans_cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							trans_cd = value;
						}
						rset2.close();
						stmt2.close();
					}
					else if(i == 2) {	//AGMT_NO
						value = no;
					}
					else if(i == 3) {	//AGMT_REV
						value = rev;
					}
					else if(i == 4) {	//CONT_NO
						value = cont_no;
					}
					else if(i == 5) {	//CONT_REV
						value = cont_rev;
					}
					else if(i==6) {
//						cont_type=rset.getString(7);
						if(cont_type.equals("L")) {
							cont_type = "E";
						}
						value = cont_type;
					}
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + counterparty + "," +no + "," + rev + "," +cont_no+","+cont_rev+","+cont_type+","+trans_cd + "," ), conn, "");
			}
			
			stmt.close();
			rset.close();
		}
		stmt1.close();
		rset1.close();
		
		
		// SagarB20250914 Below SN block for max revision contracts with contracts as closed status
		//SN
		queryString1 = "SELECT CUSTOMER_CD,SN_NO,SN_REV_NO, FLSA_NO, FLSA_REV_NO FROM DLNG_SN_MST WHERE SN_CLOSURE_REQUEST IS NOT NULL AND SN_CLOSURE_REQUEST = 'Y' ORDER BY CUSTOMER_CD";
		stmt1 = conn.prepareStatement(queryString1);
		rset1 = stmt1.executeQuery();
		
		while(rset1.next()) {
			cd = rset1.getString(1);
			cont_no = rset1.getString(2);
			cont_rev = rset1.getString(3);
			no = rset1.getString(4);
			rev = rset1.getString(5);
			cont_type = "S";
			
//			name = cd+"-"+no+"-"+rev+"-";
			name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";

			queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, cd);
			rset2 = stmt2.executeQuery();
			if(rset2.next()) {
				value = rset2.getString(1);
				if(mpe.counterparty_map.containsKey(value)) {
					value = mpe.counterparty_map.get(value);
				}
				counterparty = value;
			}
			else {
				counterparty = "0";
			}
			rset2.close();
			stmt2.close();
			
			//SN FROM BUYER NOMINATION
			//			name = cd+"-"+no+"-"+rev+"-";
			name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
			cont_type = "S";
			
			
			queryString = "SELECT DISTINCT(A.TRANS_CD), NULL, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE,NULL,A.ENT_BY, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
					+ "FROM DLNG_DAILY_TRUCK_NOM_DTL A WHERE A.CONTRACT_TYPE = ? AND A.MAPPING_ID LIKE ? AND A.NOM_ID LIKE ? " ;
			stmt = conn.prepareStatement(queryString);
			
			stmt.setString(1, cont_type);
			stmt.setString(2, name+"%");
			stmt.setString(3, name+"%");
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,TIMESTAMP,", conn);
			
			while (rset.next() && !counterparty.equals("0")) {
				row = spreadsheet.createRow(nrow++);
				value = "";
				trans_cd = rset.getString(1);
//				type = rset.getString(3);
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					
					if(i == 0) {	//COUNTERPARTY_CD
//						queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
//						stmt2 = conn.prepareStatement(queryString2);
//						stmt2.setString(1, cd);
//						rset2 = stmt2.executeQuery();
//						if(rset2.next()) {
//							value = rset2.getString(1);
//							if(mpe.counterparty_map.containsKey(value)) {
//								value = mpe.counterparty_map.get(value);
//							}
//							counterparty = value;
//						}
//						else {
//							value = "0";
//						}
//						rset2.close();
//						stmt2.close();
						value = counterparty;
					}
					else if(i==1) {
						cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						value = cont_ref;
					}
					
					else if(i == 7) {	//COMPANY_CD(TRANSPORTER_CD)
						queryString2 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, trans_cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							trans_cd = value;
						}
						rset2.close();
						stmt2.close();
					}
					else if(i == 2) {	//AGMT_NO
						value = no;
					}
					else if(i == 3) {	//AGMT_REV
						value = rev;
					}
					else if(i == 4) {	//CONT_NO
						value = cont_no;
					}
					else if(i == 5) {	//CONT_REV
						value = cont_rev;
					}
					else if(i==6) {
//						cont_type=rset.getString(7);
						if(cont_type.equals("S")) {
							cont_type = "F";
						}
						value = cont_type;
					}
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + counterparty + "," +no + "," + rev + "," +cont_no+","+cont_rev+","+cont_type+","+trans_cd + "," ), conn, "");
			}
			
			stmt.close();
			rset.close();
		}
		stmt1.close();
		rset1.close();
		
		
		// SagarB20250914 Below LOA block for max revision contracts with contracts as closed status
		//LOA
		queryString1 = "SELECT CUSTOMER_CD,LOA_NO,LOA_REV_NO,TENDER_NO,'0' FROM DLNG_LOA_MST WHERE LOA_CLOSURE_REQUEST IS NOT NULL AND LOA_CLOSURE_REQUEST = 'Y' ORDER BY CUSTOMER_CD";
		stmt1 = conn.prepareStatement(queryString1);
		rset1 = stmt1.executeQuery();
		
		while(rset1.next()) {
			cd = rset1.getString(1);
			cont_no = rset1.getString(2);
			cont_rev = rset1.getString(3);
			no = rset1.getString(4);
			rev = rset1.getString(5);
			
			
	//		name = cd+"-"+no+"-"+rev+"-";
			name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
			queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, cd);
			rset2 = stmt2.executeQuery();
			if(rset2.next()) {
				value = rset2.getString(1);
				if(mpe.counterparty_map.containsKey(value)) {
					value = mpe.counterparty_map.get(value);
				}
				counterparty = value;
			}
			
			else {
				counterparty = "0";
			}
			rset2.close();
			stmt2.close();
			
			
		//LOA FROM BUYER NOMINATION
		cont_type = "L";
		
		//		name = cd+"-"+no+"-"+rev+"-";
		name = cd+"-"+no+"-"+rev+"-"+cont_no+"-";
		
		
		
		queryString = "SELECT DISTINCT(A.TRANS_CD), NULL, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE,NULL,A.ENT_BY, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
				+ "FROM DLNG_DAILY_TRUCK_NOM_DTL A WHERE A.CONTRACT_TYPE = ? AND A.MAPPING_ID LIKE ? AND A.NOM_ID LIKE ? " ;
		stmt = conn.prepareStatement(queryString);
		
		stmt.setString(1, cont_type);
		stmt.setString(2, name+"%");
		stmt.setString(3, name+"%");
		rset = stmt.executeQuery();
		
		logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,TIMESTAMP,", conn);
		
		while (rset.next() && !counterparty.equals("0")) {
			row = spreadsheet.createRow(nrow++);
			value = "";
			trans_cd = rset.getString(1);
//			type = rset.getString(3);
			for (int i = 0; i < columns.split(",").length; i++) {
				
				cell = row.createCell(i);
				value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
				value = value.trim().equals("") ? "null" : value;
				
				
				if(i == 0) {	//COUNTERPARTY_CD
					queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					rset2 = stmt2.executeQuery();
					if(rset2.next()) {
						value = rset2.getString(1);
						if(mpe.counterparty_map.containsKey(value)) {
							value = mpe.counterparty_map.get(value);
						}
						counterparty = value;
					}
					else {
						value = "0";
					}
					rset2.close();
					stmt2.close();
				}
				else if(i == 1) {
					cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
					value = cont_ref;
				}
				
				else if(i == 7) {	//COMPANY_CD(TRANSPORTER_CD)
					queryString2 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, trans_cd);
					rset2 = stmt2.executeQuery();
					if(rset2.next()) {
						value = rset2.getString(1);
						trans_cd = value;
					}
					rset2.close();
					stmt2.close();
				}
				else if(i == 2) {	//AGMT_NO
					value = no;
				}
				else if(i == 3) {	//AGMT_REV
					value = rev;
				}
				else if(i == 4) {	//CONT_NO
					value = cont_no;
				}
				else if(i == 5) {	//CONT_REV
					value = cont_rev;
				}
				else if(i==6) {
//					cont_type=rset.getString(7);
					if(cont_type.equals("L")) {
						cont_type = "E";
					}
					value = cont_type;
				}
				
				cell.setCellValue("'" + value + "'");
			}
			count++;
			logger.data(fname, (company_cd + "," + counterparty + "," +no + "," + rev + "," +cont_no+","+cont_rev+","+cont_type+","+trans_cd + "," ), conn, "");
		}
		
		stmt.close();
		rset.close();
	}
	stmt1.close();
	rset1.close();
	
		
		
		filename = migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_TRUCK_TRANS_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_CONT_TRUCK_TRANS>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_SUPPLY_CONT_TRUCK_TRANS>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
		
	public void FMS_SECURITY_DEAL_MAP() throws SQLException, IOException {
		function_nm = "FMS_SECURITY_DEAL_MAP(DLNG)()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SECURITY_DEAL_MAP>>,,,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			Map<String, Integer> seq = new HashMap<String, Integer>();
			
	    	columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,"
	    			+ "ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT";
	    	
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			int seq_no=1;
			String adv_seq="",counterparty_cd="";
			String prev_abbr="";
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
		
				
				queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
						+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
						+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				
				logger.checkpoint(fname, " COUNTERPARTY_CD, SEQ_NO, MAP_SEQ_NO, SEQ_REV_NO, GX ,  CONT_TYPE,TIMESTAMP", conn);
				
				while (rset.next()) {
					abbr = rset.getString(1) == null ? "null" : rset.getString(1);
					counterparty_cd = rset.getString(2) == null ? "null" : rset.getString(2);
					if(abbr!=null) {
						abbr= abbr.toUpperCase();
					}
					int num = 1; 
					//FOR SN WITHOUT ADV(CUSTOMER)
//					queryString1 = "SELECT A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B "
//							+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
//							+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
//							+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
//							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//					stmt1 = conn.prepareStatement(queryString1);
//					stmt1.setString(1, rset.getString(2));
//					stmt1.setString(2, delta_FromDt);
//					stmt1.setString(3, delta_FromDt);
//					stmt1.setString(4, delta_ToDt);	
//					stmt1.setString(5, delta_ToDt);
//					rset1 = stmt1.executeQuery();
				
//					while (rset1.next()) {
//						no = rset1.getString(1);
//						rev = rset1.getString(2);
//						cont_no = rset1.getString(3);
//						cont_rev = rset1.getString(4);
//						cd = rset1.getString(5);
//						
//						queryString3 = "SELECT CUSTOMER_CD, SEQ_NO, NULL, REF_NO, NULL, NULL, NULL, NULL, 'F', ENT_CD, "
//								+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, "
//								+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL "
//								+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
////								+ "AND SEC_TYPE NOT IN ('ADV') "
//								+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//								+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//						stmt3 = conn.prepareStatement(queryString3);
//						stmt3.setString(1, ("S"+no+"-"+rev+"-"+cont_no+"-"+cont_rev+"-DLNG"));
//						stmt3.setString(2, cd);
//						stmt3.setString(3, counterparty_cd);
//						stmt3.setString(4, delta_FromDt);
//						stmt3.setString(5, delta_FromDt);
//						stmt3.setString(6, delta_ToDt);
//						stmt3.setString(7, delta_ToDt);
//						rset3 = stmt3.executeQuery();
//						
//						
//						while (rset3.next()) {
//							
//							row = spreadsheet.createRow(nrow++);
//							ncell = 0;
//							
//							
//							cell = row.createCell(ncell++);
//							if (mpe.counterparty_map.containsKey(abbr)) {
//								abbr =mpe.counterparty_map.get(abbr); 
//						    }
//							cell.setCellValue("'"+abbr+"'");
//						
//							cell = row.createCell(ncell++);
//							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
//							cell.setCellValue("'"+cont_ref+"'");
////							cont_ref = rset3.getString(1);
////							cd = rset.getString(2) == null ? "null" : rset.getString(2);
////							cell.setCellValue("'"+cd+"'");
////							System.out.println(cd);
//							
//							for (int i = 2; i < columns.split(",").length; i++) {
//								cell = row.createCell(ncell++);
//								value = rset3.getString(i) == null ? "null" : rset3.getString(i);
//								
////								if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
////									if(abbr.equals(prev_abbr))
////									{
////										seq_no = num+"";
////									}
////									else {
////										seq_no="1";
////									}
////									cell.setCellValue("'"+seq_no+"'");									
////								}
//								 if (i == 3) {	// map_seq_no
//									cell.setCellValue("'"+1+"'");									
//								}
////								else if (i == 4) {	// sec_ref_no
////									value = abbr + "-S-" + num ;
////									cell.setCellValue("'"+value+"'");
////								}
//								else if (i == 5) {	// fgsa_no
//									cell.setCellValue("'"+rset1.getString(1)+"'");
//									num++;
//								}
//								else if (i == 6) {	// fgsa_no_rev
//									cell.setCellValue("'"+rset1.getString(2)+"'");
//								}
//								else if (i == 7) {	// sn_no
//									cell.setCellValue("'"+rset1.getString(3)+"'");
//								}
//								else if (i == 8) {	// sn_no_rev
//									cell.setCellValue("'"+rset1.getString(4)+"'");
//								}
//								else {
//									cell.setCellValue("'"+value+"'");
//								}
//								prev_abbr=abbr;
//							}
//							count++;
//							logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"E"+","), conn, "");
//						}
//						rset3.close();
//						stmt3.close();
//					}
//					rset1.close();
//					stmt1.close();
							
						//FOR SN WITH ADV
						queryString1 = "SELECT A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B "
								+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
								+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
								+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
								+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
								+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(2));
						stmt1.setString(2, delta_FromDt);
						stmt1.setString(3, delta_FromDt);
						stmt1.setString(4, delta_ToDt);	
						stmt1.setString(5, delta_ToDt);
						rset1 = stmt1.executeQuery();
					
						while (rset1.next()) {
							no = rset1.getString(1);
							rev = rset1.getString(2);
							cont_no = rset1.getString(3);
							cont_rev = rset1.getString(4);
							cd = rset1.getString(5);
							
							queryString2 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST "
									+ "WHERE "
//									+ "LINK LIKE ? AND "
									+ "((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? )";
							stmt2 = conn.prepareStatement(queryString2);
//							stmt2.setString(1, "%-DLNG");
							stmt2.setString(1, cd);
							stmt2.setString(2, counterparty_cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next())
							{
								seq_no = rset2.getInt(1);
							}
							else {
								seq_no=0;
							}
							rset2.close();
							stmt2.close();
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
							}
							
							if (!seq.containsKey(abbr)) {
								seq.put(abbr,seq_no);
							}
							
						queryString4 = "SELECT  NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'F', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "NULL, NULL, APPROVED_BY, TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL "
								+ " FROM DLNG_ADVC_PAY_MST "
								+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'S' ORDER BY SEQ_NO";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1,cd);
						stmt4.setString(2,no);
						stmt4.setString(3,rev);
						stmt4.setString(4,cont_no);
						stmt4.setString(5,cont_rev);
						
						rset4 = stmt4.executeQuery();
						while(rset4.next()) {
							row = spreadsheet.createRow(nrow++);
							ncell = 0;
							
							abbr = rset.getString(1) == null ? "null" : rset.getString(1);
							cell = row.createCell(ncell++);
							
							cell.setCellValue("'"+abbr+"'");
							
							cell = row.createCell(ncell++);
							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							cell.setCellValue("'"+cont_ref+"'");
							
							for (int i = 2; i < columns.split(",").length; i++) {
								cell = row.createCell(ncell++);
								value = rset4.getString(i) == null ? "null" : rset4.getString(i);
								
								if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
									if (seq.containsKey(abbr)) {
										seq_no=seq.get(abbr);
										seq_no=seq_no+1;
										seq.put(abbr,seq_no);
									} 
									else {
										seq_no= 1;
										seq.put(abbr,seq_no);
									}
									
									value = seq_no+"";
									cell.setCellValue("'"+value+"'");									
								}
								else if (i == 3) {	// map_seq_no
									cell.setCellValue("'"+1+"'");									
								}
								else if (i == 4) {	// sec_ref_no
									value = abbr + "-S-" + seq_no ;
//									value = abbr + "(" + adv_seq + ")";
									cell.setCellValue("'"+value+"'");
								}
								else if (i == 5) {	// fgsa_no
									cell.setCellValue("'"+no+"'");
									num++;
								}
								else if (i == 6) {	// fgsa_no_rev
									cell.setCellValue("'"+rev+"'");
								}
								else if (i == 7) {	// sn_no
									cell.setCellValue("'"+cont_no+"'");
								}
								else if (i == 8) {	// sn_no_rev
									cell.setCellValue("'"+cont_rev+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								prev_abbr=abbr;
							}
							count++;
							logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"E"+","), conn, "");
//						}
//						rset3.close();
//						stmt3.close();
					}
					rset4.close();
					stmt4.close();
					}
					rset1.close();
					stmt1.close();
					
					//FOR LOA WITH ADV
					queryString1 = "SELECT A.TENDER_NO,A.LOA_NO , A.LOA_REV_NO, A.CUSTOMER_CD FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B "
							+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
							+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
							+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, rset.getString(2));
					stmt1.setString(2, delta_FromDt);
					stmt1.setString(3, delta_FromDt);
					stmt1.setString(4, delta_ToDt);
					stmt1.setString(5, delta_ToDt);
					rset1 = stmt1.executeQuery();
				
					while (rset1.next()) {
						no = rset1.getString(1);
						rev = "0";
						cont_no = rset1.getString(2);
						cont_rev = rset1.getString(3);
						cd = rset1.getString(4);
						
						queryString2 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST "
								+ "WHERE "
//								+ "LINK LIKE ? AND "
								+ "((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? )";
						stmt2 = conn.prepareStatement(queryString2);
//						stmt2.setString(1, "%-DLNG");
						stmt2.setString(1, cd);
						stmt2.setString(2, counterparty_cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next())
						{
							seq_no = rset2.getInt(1);
						}
						else {
							seq_no=0;
						}
						rset2.close();
						stmt2.close();
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
						}
						seq.put(abbr,seq_no);
						
						queryString4 = "SELECT  NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'F', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "NULL, NULL, APPROVED_BY, TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL "
								+ " FROM DLNG_ADVC_PAY_MST "
								+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'L' ORDER BY SEQ_NO";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1,cd);
						stmt4.setString(2,no);
						stmt4.setString(3,rev);
						stmt4.setString(4,cont_no);
						stmt4.setString(5,cont_rev);
						
						rset4 = stmt4.executeQuery();
						while(rset4.next()) {
							row = spreadsheet.createRow(nrow++);
							ncell = 0;
							
							abbr = rset.getString(1) == null ? "null" : rset.getString(1);
							cell = row.createCell(ncell++);
							cell.setCellValue("'"+abbr+"'");
							
							cell = row.createCell(ncell++);
							cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
							cell.setCellValue("'"+cont_ref+"'");
							
							for (int i = 2; i < columns.split(",").length; i++) {
								cell = row.createCell(ncell++);
								value = rset4.getString(i) == null ? "null" : rset4.getString(i);
								
								if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
									if (seq.containsKey(abbr)) {
										seq_no=seq.get(abbr);
										seq_no=seq_no+1;
										seq.put(abbr,seq_no);
									} 
									else {
										seq_no= 1;
										seq.put(abbr,seq_no);
									}
									
									value = seq_no+"";
									cell.setCellValue("'"+value+"'");									
								}
								else if (i == 3) {	// map_seq_no
									cell.setCellValue("'"+1+"'");									
								}
								else if (i == 4) {	// sec_ref_no
									value = abbr + "-S-" + seq_no ;
//									value = abbr + "(" + adv_seq + ")";
									cell.setCellValue("'"+value+"'");
								}
								else if (i == 5) {	// fgsa_no
									cell.setCellValue("'"+no+"'");
									num++;
								}
								else if (i == 6) {	// fgsa_no_rev
									cell.setCellValue("'"+rev+"'");
								}
								else if (i == 7) {	// sn_no
									cell.setCellValue("'"+cont_no+"'");
								}
								else if (i == 8) {	// sn_no_rev
									cell.setCellValue("'"+cont_rev+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								prev_abbr=abbr;
							}
							count++;
							logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"E"+","), conn, "");
//						}
//						rset3.close();
//						stmt3.close();
						}
						rset4.close();
						stmt4.close();
					}
					rset1.close();
					stmt1.close();
					

					//FOR LOA(CUSTOMER)
//					queryString1 = "SELECT A.TENDER_NO,A.LOA_NO , A.LOA_REV_NO, A.CUSTOMER_CD FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B "
//							+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
//							+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
//							+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
//							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//					stmt1 = conn.prepareStatement(queryString1);
//					stmt1.setString(1, rset.getString(2));
//					stmt1.setString(2, delta_FromDt);
//					stmt1.setString(3, delta_FromDt);
//					stmt1.setString(4, delta_ToDt);
//					stmt1.setString(5, delta_ToDt);
//					rset1 = stmt1.executeQuery();
				
//					while (rset1.next()) {
//						no = rset1.getString(1);
//						rev = "0";
//						cont_no = rset1.getString(2);
//						cont_rev = rset1.getString(3);
//						cd = rset1.getString(4);
//						//FOR LOA WITHOUT ADV
//						queryString3 =  "SELECT CUSTOMER_CD, SEQ_NO, NULL, REF_NO, NULL, NULL, NULL, NULL, 'E', ENT_CD, "
//								+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, "
//								+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL "
//								+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
////								+ "AND SEC_TYPE NOT IN ('ADV') "
//								+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//								+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//						stmt3 = conn.prepareStatement(queryString3);
//						stmt3.setString(1, ("L"+no+"-0-"+cont_no+"-"+cont_rev+"-DLNG"));
//						stmt3.setString(2, cd);
//						stmt3.setString(3, counterparty_cd);
//						stmt3.setString(4, delta_FromDt);
//						stmt3.setString(5, delta_FromDt);
//						stmt3.setString(6, delta_ToDt);
//						stmt3.setString(7, delta_ToDt);
//						rset3 = stmt3.executeQuery();
//						
//						while (rset3.next()) {
//							row = spreadsheet.createRow(nrow++);
//							ncell = 0;
//							
//							abbr = rset.getString(1) == null ? "null" : rset.getString(1);
//							cell = row.createCell(ncell++);
//							if (mpe.counterparty_map.containsKey(abbr)) {
//								abbr =mpe.counterparty_map.get(abbr); 
//						    }
//							cell.setCellValue("'"+abbr+"'");
//						
//							cell = row.createCell(ncell++);
//							
//							cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
//							cell.setCellValue("'"+cont_ref+"'");
//							
//							for (int i = 2; i < columns.split(",").length; i++) {
//								cell = row.createCell(ncell++);
//								value = rset3.getString(i) == null ? "null" : rset3.getString(i);
////								 if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
////									if(abbr.equals(prev_abbr))
////									{
////										seq_no = num+"";
////									}
////									else {
////										seq_no="1";
////									}
////									cell.setCellValue("'"+seq_no+"'");									
////								}
//								if (i == 3) {	// map_seq_no
//									cell.setCellValue("'"+1+"'");									
//								}
////								else if (i == 4) {	// sec_ref_no
////									value = abbr + "-L-" + num ;
////									cell.setCellValue("'"+value+"'");
////									num++;
////								}
//								else {
//									cell.setCellValue("'"+value+"'");
//								}
//								prev_abbr=abbr;
//							}
//							count++;
//							logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"F"+","), conn, "");
//						}
//						rset3.close();
//						stmt3.close();
//
//						//FOR LOA WITH ADV
////						queryString4 = "SELECT EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_BY, TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), SEQ_NO "
////								+ " FROM DLNG_ADVC_PAY_MST "
////								+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'L' ORDER BY SEQ_NO";
////						stmt4 = conn.prepareStatement(queryString4);
////						stmt4.setString(1,cd);
////						stmt4.setString(2,no);
////						stmt4.setString(3,rev);
////						stmt4.setString(4,cont_no);
////						stmt4.setString(5,cont_rev);
////						rset4 = stmt4.executeQuery();
////						while(rset4.next()) {
////							adv_seq = rset4.getString(5);
////						queryString3 =  "SELECT CUSTOMER_CD, NULL, NULL, REF_NO, NULL, NULL, NULL, NULL, 'E', NULL, "
////								+ "NULL, NULL, NULL, NULL, "
////								+ "NULL, '0', NULL, 'K', NULL "
////								+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
////								+ "AND SEC_TYPE = 'ADV' "
////								+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
////								+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
////						stmt3 = conn.prepareStatement(queryString3);
////						stmt3.setString(1, ("L"+no+"-0-"+cont_no+"-"+cont_rev+"-DLNG"));
////						stmt3.setString(2, cd);
////						stmt3.setString(3, counterparty_cd);
////						stmt3.setString(4, delta_FromDt);
////						stmt3.setString(5, delta_FromDt);
////						stmt3.setString(6, delta_ToDt);
////						stmt3.setString(7, delta_ToDt);
////						rset3 = stmt3.executeQuery();
////						
////						while (rset3.next()) {
////							row = spreadsheet.createRow(nrow++);
////							ncell = 0;
////							
////							abbr = rset.getString(1) == null ? "null" : rset.getString(1);
////							cell = row.createCell(ncell++);
////							if (mpe.counterparty_map.containsKey(abbr)) {
////								abbr =mpe.counterparty_map.get(abbr); 
////							}
////							cell.setCellValue("'"+abbr+"'");
////							
////							cell = row.createCell(ncell++);
////							
////							cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
////							cell.setCellValue("'"+cont_ref+"'");
////							
////							for (int i = 2; i < columns.split(",").length; i++) {
////								cell = row.createCell(ncell++);
////								value = rset3.getString(i) == null ? "null" : rset3.getString(i);
////								if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
////									if(abbr.equals(prev_abbr))
////									{
////										seq_no = num+"";
////									}
////									else {
////										seq_no="1";
////									}
////									cell.setCellValue("'"+seq_no+"'");									
////								}
////								else if (i == 3) {	// map_seq_no
////									cell.setCellValue("'"+1+"'");									
////								}
////								else if (i == 4) {	// sec_ref_no
//////									value = abbr + "-L-" + num ;
////									value = value + "(" + adv_seq + ")";
////									cell.setCellValue("'"+value+"'");
//////									num++;
////								}
////								else if(i == 10) {	//ENT_BY
////									value = rset4.getString(1);
////									cell.setCellValue("'"+value+"'");
////								}
////								else if(i == 11) {	//ENT_DT
////									value = rset4.getString(2);
////									cell.setCellValue("'"+value+"'");
////								}
////								else if(i == 14) {	//APRV_BY
////									value = rset4.getString(3);
////									cell.setCellValue("'"+value+"'");
////								}
////								else if(i == 15) {	//APRV_DT
////									value = rset4.getString(4);
////									cell.setCellValue("'"+value+"'");
////								}
////								else {
////									cell.setCellValue("'"+value+"'");
////								}
////								prev_abbr=abbr;
////							}
////							count++;
////							logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"F"+","), conn, "");
////						}
////						rset3.close();
////						stmt3.close();
////						}
////						rset4.close();
////						stmt4.close();
//					}
//					rset1.close();
//					stmt1.close();
					
				}
				rset.close();
				stmt.close();


			
			filename = migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SECURITY_DEAL_MAP(DLNG)>>,,,,,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Purchase_SEIPL_Data_Extractor.java -> Purchase_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_SECURITY_MST() throws SQLException, IOException {
		function_nm = "FMS_SECURITY_MST(DLNG)()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SECURITY_MST(DLNG)>>,,,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,"
					+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,RECEIPT_DT,"
					+ "ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,"
					+ "APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD";
	    	
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			Map<String, Integer> seq = new HashMap<String, Integer>();
			nrow = 0;
			ncell = 0;
			count = 0;
			String counterparty_cd="";
			String prev_abbr="",remark="";
			int seq_no=1;
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, " COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX ,CONT_TYPE,TIMESTAMP", conn);
			while (rset.next()) {
				abbr = rset.getString(1) == null ? "null" : rset.getString(1);
				counterparty_cd = rset.getString(2) == null ? "null" : rset.getString(2);
				if(abbr!=null) {
					abbr= abbr.toUpperCase();
				}
				remark = "";
				
				//FOR SN
				int num = 1; 
//				queryString1 = "SELECT A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD "
//						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B "
//						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
//						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
//						+ "AND B.COUNTERPARTY_CD = ? "
//						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//				stmt1 = conn.prepareStatement(queryString1);
//				stmt1.setString(1, rset.getString(2));
//				stmt1.setString(2, delta_FromDt);
//				stmt1.setString(3, delta_FromDt);
//				stmt1.setString(4, delta_ToDt);
//				stmt1.setString(5, delta_ToDt);
//				rset1 = stmt1.executeQuery();
//
//				while (rset1.next()) {
//					no = rset1.getString(1);
//					rev = rset1.getString(2);
//					cont_no = rset1.getString(3);
//					cont_rev = rset1.getString(4);
//					cd = rset1.getString(5);
//					map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
					
					//SN WITHOUT ADV
//					queryString2 = "SELECT SEQ_NO,REF_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
//							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
//							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
//							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
//							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
//							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL "
//							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
////							+ "AND SEC_TYPE NOT IN ('ADV')"
//							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//					stmt2 = conn.prepareStatement(queryString2);
//					stmt2.setString(1, ("S"+no+"-"+rev+"-"+cont_no+"-"+cont_rev+"-DLNG"));
//					stmt2.setString(2, rset1.getString(5));
//					stmt2.setString(3, counterparty_cd);
//					stmt2.setString(4, delta_FromDt);
//					stmt2.setString(5, delta_FromDt);
//					stmt2.setString(6, delta_ToDt);
//					stmt2.setString(7, delta_ToDt);
//					rset2 = stmt2.executeQuery();
					
//					while(rset2.next()) {
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
//						
//						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
//						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
////						System.out.println(remark);
//						cell = row.createCell(ncell++);
//						if (mpe.counterparty_map.containsKey(abbr)) {
//							abbr =mpe.counterparty_map.get(abbr); 
//					    }
//						cell.setCellValue("'"+abbr+"'");
//						
//						cell = row.createCell(ncell++);
//						
//						cd = rset.getString(2) == null ? "null" : rset.getString(2);
//						cell.setCellValue("'"+map+"'");
//						
//						for (int i = 2; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
//							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
//						
////							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
////								if(abbr.equals(prev_abbr))
////								{
////									seq_no = num+"";
////								}
////								else {
////									seq_no="1";
////								}
////								cell.setCellValue("'"+seq_no+"'");									
////							}
////							else if (i == 3) {	// sec_ref_no
////								value = abbr + "-S-" + num ;
////								cell.setCellValue("'"+value+"'");
////								num++;
////							}
//							 if (i == 7) {	// GUARANTOR_CD
//								if (mpe.counterparty_map.containsKey(value)) {
//									value =mpe.counterparty_map.get(value); 
//							    }
//								if(value.contains("RIL"))
//								{
//									value="RIL";
//								}
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 8 && !value.equals("null")) {	// Currency
//								
//								value = value.equals("INR") ? "1" : "2";
//								cell.setCellValue("'"+value+"'");
//							}
//							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
//							
//								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
//								stmt3 = conn.prepareStatement(queryString3);
//								stmt3.setString(1, value);
//								rset3 = stmt3.executeQuery();
//								if(rset3.next()) {
//									cell.setCellValue("'"+rset3.getString(1)+"'");
//								}
//								else {
//									cell.setCellValue("'"+value+"'");
//								}
//								rset3.close();
//								stmt3.close();
//							}
//							else if (i == 25 && !value.equals("null")) {	// Status
//								if(value.equals("Pending")) {
//									value = "P";
//								}
//								else if(value.equals("In order")) {
//									value = "O";
//								}
//								else if(value.equals("Cancelled")) {
//									value = "C";
//								}
//								else if(value.equals("Pending for amendment")) {
//									value = "A";
//								}
//								else if(value.equals("Restated")) {
//									value = "R";
//								}
//								else if(value.equals("Dummy")) {
//									value = "D";
//								}
//								else if(value.equals("Expired")) {
//									value = "E";
//								}
//								cell.setCellValue("'"+value+"'");
//							}
////							else if(i == 26) {
////								if(remark.length()>150) {
////									remark = remark.substring(0,150);
////								}
////								value = remark;
////								cell.setCellValue("'"+value+"'");
////							}
//							else {
//								cell.setCellValue("'"+value+"'");
//							}
//							prev_abbr=abbr;
//						}
//						count++;
//						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"S"+","), conn, "");
//					}
//					rset2.close();
//					stmt2.close();
				
					//SN WITH ADV
//					String amt="",currency="",ent_by="",ent_dt="",apr_by="",apr_dt="",dr_cr="",pay_dt="",appr_flag="",pay_type="",adv_seq="";
//					queryString4 = "SELECT PAY_AMT, CURRENCY, REMARK, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_BY, "
//							+ "TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), DR_CR_FLAG, TO_CHAR(PAY_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_FLAG, PAY_TYPE, SEQ_NO "
//							+ " FROM DLNG_ADVC_PAY_MST "
//							+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'S' ORDER BY SEQ_NO";
//					stmt4 = conn.prepareStatement(queryString4);
//					stmt4.setString(1,cd);
//					stmt4.setString(2,no);
//					stmt4.setString(3,rev);
//					stmt4.setString(4,cont_no);
//					stmt4.setString(5,cont_rev);
//					rset4 = stmt4.executeQuery();
//					while(rset4.next()) {
//						amt = rset4.getString(1); 
//						currency =rset4.getString(2); 
//						remark =rset4.getString(3); 
//						ent_by =rset4.getString(4); 
//						ent_dt =rset4.getString(5); 
//						apr_by =rset4.getString(6); 
//						apr_dt =rset4.getString(7); 
//						dr_cr =rset4.getString(8); 
//						pay_dt =rset4.getString(9); 
//						appr_flag =rset4.getString(10); 
//						pay_type =rset4.getString(11); 
//						adv_seq = rset4.getString(12);
//						
//					queryString2 = "SELECT NULL,REF_NO,REF_NO,NVL(ISSUED, 'R'), 'ADV', 'GAS',GUARANTOR_ABBR,NULL,VARIATION_VALUE,"
//							+ "NULL,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
//							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
//							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), 'A', NULL, FLAG, INORDER_HIST,NULL, NULL, NULL, NULL,"
//							+ "NULL, NULL,'0', 'K',"
//							+ "NULL, NULL, NULL, NULL, NULL, NULL, 'N.A' ,NULL,'0',NULL,NULL "
//							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
//							+ "AND SEC_TYPE ='ADV' "
//							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//					stmt2 = conn.prepareStatement(queryString2);
//					stmt2.setString(1, ("S"+no+"-"+rev+"-"+cont_no+"-"+cont_rev+"-DLNG"));
//					stmt2.setString(2, rset1.getString(5));
//					stmt2.setString(3, delta_FromDt);
//					stmt2.setString(4, delta_FromDt);
//					stmt2.setString(5, delta_ToDt);
//					stmt2.setString(6, delta_ToDt);
//					rset2 = stmt2.executeQuery();
//					  
//					while(rset2.next()) {
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
//						
//						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
////						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
////						System.out.println(remark);
//						cell = row.createCell(ncell++);
//						if (mpe.counterparty_map.containsKey(abbr)) {
//							abbr =mpe.counterparty_map.get(abbr); 
//						}
//						cell.setCellValue("'"+abbr+"'");
//						
//						cell = row.createCell(ncell++);
//						
//						cd = rset.getString(2) == null ? "null" : rset.getString(2);
//						cell.setCellValue("'"+map+"'");
//						
//						for (int i = 2; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
//							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
//							
//							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
////								if(abbr.equals(prev_abbr))
////								{
////									seq_no = num+"";
////								}
////								else {
////									seq_no="1";
////								}
////								cell.setCellValue("'"+seq_no+"'");	
//								value = value + "(" + adv_seq + ")"; 
//								cell.setCellValue("'"+value+"'");
//							}
//							else if (i == 3) {	// sec_ref_no
////								value = abbr + "-S-" + num ;
//								 value = value + "(" + adv_seq + ")"; 
//								cell.setCellValue("'"+value+"'");
////								num++;
//							}
//							else if(i == 5) {	//SEC_TYPE
//								if(pay_type.equals("AP")) {
//									value = "ADV";
//								}
//								else {
//									value = "DPT";
//								}
//								cell.setCellValue("'"+value+"'");
//							}
//							else if (i == 7) {	// GUARANTOR_CD
//								if (mpe.counterparty_map.containsKey(value)) {
//									value =mpe.counterparty_map.get(value); 
//								}
//								if(value.contains("RIL"))
//								{
//									value="RIL";
//								}
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 8 ) {	// Currency
//								
//								value = currency;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 10) {	//VALUE
//								value = amt;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
//								
//								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
//								stmt3 = conn.prepareStatement(queryString3);
//								stmt3.setString(1, value);
//								rset3 = stmt3.executeQuery();
//								if(rset3.next()) {
//									cell.setCellValue("'"+rset3.getString(1)+"'");
//								}
//								else {
//									cell.setCellValue("'"+value+"'");
//								}
//								rset3.close();
//								stmt3.close();
//							}
//							else if (i == 25) {	// Status
//								if(appr_flag!=null) {
//									if(appr_flag.equals("Y")) {
//										value = "O";
//									}
//									else  {
//										value = "A";
//									}
//								}
//								else {
//									value = "A";
//								}
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 18) {
//								value = pay_dt;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 26) {
////								if(remark.length()>150) {
////									remark = remark.substring(0,150);
////								}
//								value = remark;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 29) {
//								value = ent_by;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 30) {
//								value = ent_dt;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 33) {
//								value = apr_dt;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 34) {
//								value = apr_by;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 44) {
//								if(dr_cr!=null) {
//									if(dr_cr.equals("C")) {
//										dr_cr = "CR";
//									}
//									else if(dr_cr.equals("D")) {
//										dr_cr = "DR";
//									}
//								}
//								else {
//									dr_cr = "CR";
//								}
//								value = dr_cr;
//								cell.setCellValue("'"+value+"'");
//							}
//							else {
//								cell.setCellValue("'"+value+"'");
//							}
//							prev_abbr=abbr;
//						}
//						count++;
//						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"S"+","), conn, "");
//					}
//					rset2.close();
//					stmt2.close();
//				}
//				rset4.close();
//				stmt4.close();
//				}
//				
//				
//				rset1.close();
//				stmt1.close();
				
				
				//FOR SN WITH ADV
				String dr_cr="",appr_flag="",pay_type="";
				queryString1 = "SELECT A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);	
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
			
				while (rset1.next()) {
					no = rset1.getString(1);
					rev = rset1.getString(2);
					cont_no = rset1.getString(3);
					cont_rev = rset1.getString(4);
					cd = rset1.getString(5);
					
					queryString2 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST "
							+ "WHERE "
//							+ "LINK LIKE ? AND "
							+ "((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? )";
					stmt2 = conn.prepareStatement(queryString2);
//					stmt2.setString(1, "%-DLNG");
					stmt2.setString(1, cd);
					stmt2.setString(2, counterparty_cd);
					rset2 = stmt2.executeQuery();
					if(rset2.next())
					{
						seq_no = rset2.getInt(1);
					}
					else {
						seq_no=0;
					}
					rset2.close();
					stmt2.close();
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
					}
					
					if (!seq.containsKey(abbr)) {
						seq.put(abbr,seq_no);
					}
					
				queryString4 = "SELECT  NULL, NULL, NULL, NULL, PAY_TYPE, 'GAS', NULL, CURRENCY, NULL, PAY_AMT, NULL, NULL, NULL, NULL, "
						+ "NULL, NULL, NULL, TO_CHAR(PAY_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, NULL, NULL, NULL, APPROVED_FLAG, "
						+ "REMARK, NULL, 'Y', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
						+ "TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_BY, '0', 'K', NULL, NULL, NULL, NULL, NULL, NULL, SEQ_NO, DR_CR_FLAG, "
						+ "'0', NULL, NULL, NULL, NULL, NULL, NULL "
						+ " FROM DLNG_ADVC_PAY_MST "
						+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'S' ORDER BY SEQ_NO";
				stmt4 = conn.prepareStatement(queryString4);
				stmt4.setString(1,cd);
				stmt4.setString(2,no);
				stmt4.setString(3,rev);
				stmt4.setString(4,cont_no);
				stmt4.setString(5,cont_rev);
				
				rset4 = stmt4.executeQuery();
				while(rset4.next()) {
					pay_type = rset4.getString(5);
					appr_flag = rset4.getString(25);
					dr_cr = rset4.getString(44);
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					abbr = rset.getString(1) == null ? "null" : rset.getString(1);
					cell = row.createCell(ncell++);
					
					cell.setCellValue("'"+abbr+"'");
					
					cell = row.createCell(ncell++);
					cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
					cell.setCellValue("'"+cont_ref+"'");
					
					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset4.getString(i) == null ? "null" : rset4.getString(i);
						
						if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
							if (seq.containsKey(abbr)) {
								seq_no=seq.get(abbr);
								seq_no=seq_no+1;
								seq.put(abbr,seq_no);
							} 
							else {
								seq_no= 1;
								seq.put(abbr,seq_no);
							}
							
							value = seq_no+"";
							cell.setCellValue("'"+value+"'");									
						}
						else if (i == 3) {	// sec_ref_no
							value = abbr + "-S-" + seq_no ;
//							value = abbr + "(" + adv_seq + ")";
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 5) {	//SEC_TYPE
						if(pay_type.equals("AP")) {
							value = "ADV";
						}
						else {
							value = "DPT";
						}
						cell.setCellValue("'"+value+"'");
					}
						else if(i == 25) {	//STATUS
							if(appr_flag!=null && appr_flag.equals("Y")) {
								value = "O";
							}
							else {
								value = "A";
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 44) {
						if(dr_cr!=null) {
							if(dr_cr.equals("C")) {
								dr_cr = "CR";
							}
							else if(dr_cr.equals("D")) {
								dr_cr = "DR";
							}
						}
						else {
							dr_cr = "CR";
						}
						value = dr_cr;
						cell.setCellValue("'"+value+"'");
					}
						else {
							cell.setCellValue("'"+value+"'");
						}
						prev_abbr=abbr;
					}
					count++;
					logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"E"+","), conn, "");
//				}
//				rset3.close();
//				stmt3.close();
			}
			rset4.close();
			stmt4.close();
			}
			rset1.close();
			stmt1.close();
			
			//FOR LOA WITH ADV
			queryString1 = "SELECT A.TENDER_NO,A.LOA_NO , A.LOA_REV_NO, A.CUSTOMER_CD FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B "
					+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
					+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
					+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, rset.getString(2));
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_FromDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_ToDt);
			rset1 = stmt1.executeQuery();
		
			while (rset1.next()) {
				no = rset1.getString(1);
				rev = "0";
				cont_no = rset1.getString(2);
				cont_rev = rset1.getString(3);
				cd = rset1.getString(4);
				
				queryString2 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST "
						+ "WHERE "
//						+ "LINK LIKE ? AND "
						+ "((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? )";
				stmt2 = conn.prepareStatement(queryString2);
//				stmt2.setString(1, "%-DLNG");
				stmt2.setString(1, cd);
				stmt2.setString(2, counterparty_cd);
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					seq_no = rset2.getInt(1);
				}
				else {
					seq_no=0;
				}
				rset2.close();
				stmt2.close();
				if (mpe.counterparty_map.containsKey(abbr)) {
					abbr =mpe.counterparty_map.get(abbr); 
				}
				seq.put(abbr,seq_no);
				
				queryString4 = "SELECT  NULL, NULL, NULL, NULL, PAY_TYPE, 'GAS', NULL, CURRENCY, NULL, PAY_AMT, NULL, NULL, NULL, NULL, "
						+ "NULL, NULL, NULL, TO_CHAR(PAY_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, NULL, NULL, NULL, APPROVED_FLAG, "
						+ "REMARK, NULL, 'Y', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
						+ "TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_BY, '0', 'K', NULL, NULL, NULL, NULL, NULL, NULL, SEQ_NO, DR_CR_FLAG, "
						+ "'0', NULL, NULL, NULL, NULL, NULL, NULL "
						+ " FROM DLNG_ADVC_PAY_MST "
						+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'L' ORDER BY SEQ_NO";
				stmt4 = conn.prepareStatement(queryString4);
				stmt4.setString(1,cd);
				stmt4.setString(2,no);
				stmt4.setString(3,rev);
				stmt4.setString(4,cont_no);
				stmt4.setString(5,cont_rev);
				
				rset4 = stmt4.executeQuery();
				while(rset4.next()) {
					pay_type = rset4.getString(5);
					appr_flag = rset4.getString(25);
					dr_cr = rset4.getString(44);
				
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					abbr = rset.getString(1) == null ? "null" : rset.getString(1);
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					cell = row.createCell(ncell++);
					cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
					cell.setCellValue("'"+cont_ref+"'");
					
					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset4.getString(i) == null ? "null" : rset4.getString(i);
						
						if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
							if (seq.containsKey(abbr)) {
								seq_no=seq.get(abbr);
								seq_no=seq_no+1;
								seq.put(abbr,seq_no);
							} 
							else {
								seq_no= 1;
								seq.put(abbr,seq_no);
							}
							
							value = seq_no+"";
							cell.setCellValue("'"+value+"'");									
						}
						else if (i == 3) {	// sec_ref_no
							value = abbr + "-S-" + seq_no ;
//							value = abbr + "(" + adv_seq + ")";
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 5) {	//SEC_TYPE
						if(pay_type.equals("AP")) {
							value = "ADV";
						}
						else {
							value = "DPT";
						}
						cell.setCellValue("'"+value+"'");
					}
						else if(i == 25) {	//STATUS
							if(appr_flag!=null && appr_flag.equals("Y")) {
								value = "O";
							}
							else {
								value = "A";
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 44) {
						if(dr_cr!=null) {
							if(dr_cr.equals("C")) {
								dr_cr = "CR";
							}
							else if(dr_cr.equals("D")) {
								dr_cr = "DR";
							}
						}
						else {
							dr_cr = "CR";
						}
						value = dr_cr;
						cell.setCellValue("'"+value+"'");
					}
						else {
							cell.setCellValue("'"+value+"'");
						}
						prev_abbr=abbr;
					}
					count++;
					logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"E"+","), conn, "");
//				}
//				rset3.close();
//				stmt3.close();
				}
				rset4.close();
				stmt4.close();
			}
			rset1.close();
			stmt1.close();
					
//				//FOR LOA
//				queryString1 = "SELECT A.TENDER_NO,A.LOA_NO,A.LOA_REV_NO,A.CUSTOMER_CD "
//						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B "
//						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
//						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
//						+ "AND B.COUNTERPARTY_CD = ? "
//						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//				stmt1 = conn.prepareStatement(queryString1);
//				stmt1.setString(1, rset.getString(2));
//				stmt1.setString(2, delta_FromDt);
//				stmt1.setString(3, delta_FromDt);
//				stmt1.setString(4, delta_ToDt);
//				stmt1.setString(5, delta_ToDt);
//				rset1 = stmt1.executeQuery();
//
//				while (rset1.next()) {	
//					no = rset1.getString(1);
//					rev = "0";
//					cont_no = rset1.getString(2);
//					cont_rev = rset1.getString(3);
//					cd = rset1.getString(4);
//					map = "L-"+no+"-"+cont_no+"-"+cont_rev;
					
					//LOA WITHOUT ADV
//					queryString2 = "SELECT SEQ_NO,REF_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
//							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
//							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
//							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
//							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
//							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,NULL "
//							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
////							+ "AND SEC_TYPE NOT IN ('ADV') "
//							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//					stmt2 = conn.prepareStatement(queryString2);
//					stmt2.setString(1, ("L"+no+"-"+"0"+"-"+cont_no+"-"+cont_rev+"-DLNG"));//L1-0-1-0
//					stmt2.setString(2, rset1.getString(4));
//					stmt2.setString(3, counterparty_cd);
//					stmt2.setString(4, delta_FromDt);
//					stmt2.setString(5, delta_FromDt);
//					stmt2.setString(6, delta_ToDt);
//					stmt2.setString(7, delta_ToDt);
//					rset2 = stmt2.executeQuery();
					
//					while(rset2.next()) {
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
//						
//						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
//						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
//						cell = row.createCell(ncell++);
//						if (mpe.counterparty_map.containsKey(abbr)) {
//							abbr =mpe.counterparty_map.get(abbr); 
//					    }
//						cell.setCellValue("'"+abbr+"'");
//						
//						cell = row.createCell(ncell++);
//						
//						cd = rset.getString(2) == null ? "null" : rset.getString(2);
//						cell.setCellValue("'"+map+"'");
//						
//						for (int i = 2; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
//							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
//						
////							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
////								if(abbr.equals(prev_abbr))
////								{
////									seq_no = num+"";
////								}
////								else {
////									seq_no="1";
////								}
////								cell.setCellValue("'"+seq_no+"'");									
////							}
////							else if (i == 3) {	// sec_ref_no
////								value = rset.getString(1) + "-S-" + num ;
////								cell.setCellValue("'"+value+"'");
////								num++;
////							}
//							 if (i == 7) {	// GUARANTOR_CD
//								if (mpe.counterparty_map.containsKey(value)) {
//									value =mpe.counterparty_map.get(value); 
//							    }
//								if(value.contains("RIL"))
//								{
//									value="RIL";
//								}
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 8 && !value.equals("null")) {	// Currency
//								
//								value = value.equals("INR") ? "1" : "2";
//								cell.setCellValue("'"+value+"'");
//							}
//							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
//							
//								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
//								stmt3 = conn.prepareStatement(queryString3);
//								stmt3.setString(1, value);
//								rset3 = stmt3.executeQuery();
//								if(rset3.next()) {
//									cell.setCellValue("'"+rset3.getString(1)+"'");
//								}
//								else {
//									cell.setCellValue("'"+value+"'");
//								}
//								rset3.close();
//								stmt3.close();
//							}
//							else if (i == 25 && !value.equals("null")) {	// Status
//								if(value.equals("Pending")) {
//									value = "P";
//								}
//								else if(value.equals("In order")) {
//									value = "O";
//								}
//								else if(value.equals("Cancelled")) {
//									value = "C";
//								}
//								else if(value.equals("Pending for amendment")) {
//									value = "A";
//								}
//								else if(value.equals("Restated")) {
//									value = "R";
//								}
//								else if(value.equals("Dummy")) {
//									value = "D";
//								}
//								else if(value.equals("Expired")) {
//									value = "E";
//								}
//								cell.setCellValue("'"+value+"'");
//							}
////							else if(i == 26) {
////								if(remark.length()>150) {
////									remark = remark.substring(0,150);
////								}
////								value = remark;
////								cell.setCellValue("'"+value+"'");
////							}
//							else {
//								cell.setCellValue("'"+value+"'");
//							}
//							prev_abbr=abbr;
//						}
//						count++;
//						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"L"+","), conn, "");
//					}
//					rset2.close();
//					stmt2.close();
					
					
					//LOA WITH ADV
//					String amt="",currency="",ent_by="",ent_dt="",apr_by="",apr_dt="",dr_cr="",pay_dt="",appr_flag="",pay_type="",adv_seq="";
//					queryString4 = "SELECT PAY_AMT, CURRENCY, REMARK, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_BY, "
//							+ "TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), DR_CR_FLAG, TO_CHAR(PAY_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_FLAG, PAY_TYPE, SEQ_NO"
//							+ " FROM DLNG_ADVC_PAY_MST "
//							+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'L' ORDER BY SEQ_NO";
//					stmt4 = conn.prepareStatement(queryString4);
//					stmt4.setString(1,cd);
//					stmt4.setString(2,no);
//					stmt4.setString(3,rev);
//					stmt4.setString(4,cont_no);
//					stmt4.setString(5,cont_rev);
//					rset4 = stmt4.executeQuery();
//					while(rset4.next()) {
//						amt = rset4.getString(1); 
//						currency =rset4.getString(2); 
//						remark =rset4.getString(3); 
//						ent_by =rset4.getString(4); 
//						ent_dt =rset4.getString(5); 
//						apr_by =rset4.getString(6); 
//						apr_dt =rset4.getString(7); 
//						dr_cr =rset4.getString(8); 
//						pay_dt =rset4.getString(9);
//						appr_flag =rset4.getString(10); 
//						pay_type =rset4.getString(11); 
//						adv_seq = rset4.getString(12);
//						
//						queryString2 = "SELECT NULL,REF_NO,REF_NO,NVL(ISSUED, 'R'), 'ADV', 'GAS',GUARANTOR_ABBR,NULL,VARIATION_VALUE,"
//								+ "NULL,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
//								+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
//								+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), 'A', NULL, FLAG, INORDER_HIST,NULL, NULL, NULL, NULL,"
//								+ "NULL, NULL,'0', 'K',"
//								+ "NULL, NULL, NULL, NULL, NULL, NULL, 'N.A' ,NULL,'0',NULL,NULL "
//								+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
//								+ "AND SEC_TYPE ='ADV' "
//								+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//								+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//					stmt2 = conn.prepareStatement(queryString2);
//					stmt2.setString(1, ("L"+no+"-"+"0"+"-"+cont_no+"-"+cont_rev+"-DLNG"));//L1-0-1-0
//					stmt2.setString(2, rset1.getString(4));
//					stmt2.setString(3, delta_FromDt);
//					stmt2.setString(4, delta_FromDt);
//					stmt2.setString(5, delta_ToDt);
//					stmt2.setString(6, delta_ToDt);
//					rset2 = stmt2.executeQuery();
//					
//					while(rset2.next()) {
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
//						
//						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
//						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
//						cell = row.createCell(ncell++);
//						if (mpe.counterparty_map.containsKey(abbr)) {
//							abbr =mpe.counterparty_map.get(abbr); 
//						}
//						cell.setCellValue("'"+abbr+"'");
//						
//						cell = row.createCell(ncell++);
//						
//						cd = rset.getString(2) == null ? "null" : rset.getString(2);
//						cell.setCellValue("'"+map+"'");
//						
//						for (int i = 2; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
//							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
//							
//							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
////								if(abbr.equals(prev_abbr))
////								{
////									seq_no = num+"";
////								}
////								else {
////									seq_no="1";
////								}
////								cell.setCellValue("'"+seq_no+"'");	
//								value = value + "(" + adv_seq + ")"; 
//								cell.setCellValue("'"+value+"'");
//							}
//							else if (i == 3) {	// sec_ref_no
////								value = rset.getString(1) + "-S-" + num ;
//								 value = value + "(" + adv_seq + ")";
//								cell.setCellValue("'"+value+"'");
////								num++;
//							}
//							else if(i == 5) {	//SEC_TYPE
//								if(pay_type.equals("AP")) {
//									value = "ADV";
//								}
//								else {
//									value = "DPT";
//								}
//								cell.setCellValue("'"+value+"'");
//							}
//							else if (i == 7) {	// GUARANTOR_CD
//								if (mpe.counterparty_map.containsKey(value)) {
//									value =mpe.counterparty_map.get(value); 
//								}
//								if(value.contains("RIL"))
//								{
//									value="RIL";
//								}
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 8 ) {	// Currency
//								
//								value = currency;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 10) {	//VALUE
//								value = amt;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
//								
//								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
//								stmt3 = conn.prepareStatement(queryString3);
//								stmt3.setString(1, value);
//								rset3 = stmt3.executeQuery();
//								if(rset3.next()) {
//									cell.setCellValue("'"+rset3.getString(1)+"'");
//								}
//								else {
//									cell.setCellValue("'"+value+"'");
//								}
//								rset3.close();
//								stmt3.close();
//							}
//							else if (i == 25) {	// Status
//								if(appr_flag!=null) {
//									if(appr_flag.equals("Y")) {
//										value = "O";
//									}
//									else  {
//										value = "A";
//									}
//								}
//								else {
//									value = "A";
//								}
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 18) {
//								value = pay_dt;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 26) {
////								if(remark.length()>150) {
////									remark = remark.substring(0,150);
////								}
//								value = remark;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 29) {
//								value = ent_by;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 30) {
//								value = ent_dt;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 33) {
//								value = apr_dt;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 34) {
//								value = apr_by;
//								cell.setCellValue("'"+value+"'");
//							}
//							else if(i == 44) {
//								if(dr_cr!=null) {
//									if(dr_cr.equals("C")) {
//										dr_cr = "CR";
//									}
//									else if(dr_cr.equals("D")) {
//										dr_cr = "DR";
//									}
//								}
//								else {
//									dr_cr = "CR";
//								}
//								value = dr_cr;
//								cell.setCellValue("'"+value+"'");
//							}
//							else {
//								cell.setCellValue("'"+value+"'");
//							}
//							prev_abbr=abbr;
//						}
//						count++;
//						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"L"+","), conn, "");
//					}
//					rset2.close();
//					stmt2.close();
//					}
//					rset4.close();
//					stmt4.close();
//				}
//				rset1.close();
//				stmt1.close();
			}
			rset.close();
			stmt.close();
			filename = migration_setup_dir + "EXPORT/FMS_SECURITY_MST(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SECURITY_MST(DLNG)>>,,,,,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}// END OF FMS_SECURITY_MST
	
	
	
	
	public void LOG_FMS_SECURITY_MST() throws SQLException, IOException {
		function_nm = "LOG_FMS_SECURITY_MST(D)()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<LOG_FMS_SECURITY_MST(D)>>,,,,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);

			columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,"
					+ "VARIATION_VALUE,VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,"
					+ "RECEIPT_DT,ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,"
					+ "MODIFY_DT,MODIFY_BY,APRV_DT,APRV_BY,SEQ_REV_NO,GX,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,"
					+ "LOG_SEQ_NO,LOG_ENT_DT,"
					+ "PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD";
	    	
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String seq_no="";
			String prev_abbr="",remark="";
			
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, " COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX ,CONT_TYPE,TIMESTAMP", conn);
			while (rset.next()) {
				remark = "";
				
				//FOR SN
				int num = 1; 
				queryString1 = "SELECT A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					no = rset1.getString(1);
					rev = rset1.getString(2);
					cont_no = rset1.getString(3);
					cont_rev = rset1.getString(4);
					cd = rset1.getString(5);
					map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
					
					//SN WITHOUT ADV
					queryString2 = "SELECT NULL,REF_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL,"
							+ "DTL_SEQ_NO ,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "NULL ,NULL,NULL,NULL "
							+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE LINK = ? AND CUSTOMER_CD = ? AND SEC_TYPE NOT IN ('ADV')"
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("S"+no+"-"+rev+"-"+cont_no+"-"+cont_rev+"-DLNG"));
					stmt2.setString(2, rset1.getString(5));
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
//						System.out.println(remark);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell.setCellValue("'"+abbr+"'");
						
						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
						cell.setCellValue("'"+map+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
						
//							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
//								cell.setCellValue("'"+seq_no+"'");									
//							}
//							else if (i == 3) {	// sec_ref_no
//								value = abbr + "-S-" + num ;
//								cell.setCellValue("'"+value+"'");
//								num++;
//							}
							 if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
								cell.setCellValue("'"+value+"'");
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									cell.setCellValue("'"+rset3.getString(1)+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								rset3.close();
								stmt3.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
								cell.setCellValue("'"+value+"'");
							}
//							else if(i == 26) {
//								if(remark.length()>150) {
//									remark = remark.substring(0,150);
//								}
//								value = remark;
//								cell.setCellValue("'"+value+"'");
//							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							prev_abbr=abbr;
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"S"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
				
					//SN WITH ADV
					String amt="",currency="",ent_by="",ent_dt="",apr_by="",apr_dt="",dr_cr="",pay_dt="",appr_flag="",pay_type="",adv_seq="";
					queryString4 = "SELECT PAY_AMT, CURRENCY, REMARK, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_BY, "
							+ "TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), DR_CR_FLAG, TO_CHAR(PAY_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_FLAG, PAY_TYPE, SEQ_NO "
							+ " FROM DLNG_ADVC_PAY_MST "
							+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'S' ORDER BY SEQ_NO";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1,cd);
					stmt4.setString(2,no);
					stmt4.setString(3,rev);
					stmt4.setString(4,cont_no);
					stmt4.setString(5,cont_rev);
					rset4 = stmt4.executeQuery();
					while(rset4.next()) {
						amt = rset4.getString(1); 
						currency =rset4.getString(2); 
						remark =rset4.getString(3); 
						ent_by =rset4.getString(4); 
						ent_dt =rset4.getString(5); 
						apr_by =rset4.getString(6); 
						apr_dt =rset4.getString(7); 
						dr_cr =rset4.getString(8); 
						pay_dt =rset4.getString(9); 
						appr_flag =rset4.getString(10); 
						pay_type =rset4.getString(11); 
						adv_seq = rset4.getString(12);
						
					queryString2 = "SELECT NULL,REF_NO,REF_NO,NVL(ISSUED, 'R'), 'ADV', UPPER(DEAL_TYPE),GUARANTOR_ABBR,NULL,VARIATION_VALUE,"
							+ "NULL,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), 'A', NULL, FLAG, INORDER_HIST,NULL, NULL, NULL, NULL,"
							+ "NULL, NULL,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, "
							+ "DTL_SEQ_NO ,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "'N.A',NULL,'0',NULL "
							+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE LINK = ? AND CUSTOMER_CD = ? AND SEC_TYPE ='ADV' "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("S"+no+"-"+rev+"-"+cont_no+"-"+cont_rev+"-DLNG"));
					stmt2.setString(2, rset1.getString(5));
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					  
					while(rset2.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
//						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
//						System.out.println(remark);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
						}
						cell.setCellValue("'"+abbr+"'");
						
						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
						cell.setCellValue("'"+map+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							
							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
//								cell.setCellValue("'"+seq_no+"'");	
								value = value + "(" + adv_seq + ")"; 
								cell.setCellValue("'"+value+"'");
							}
							else if (i == 3) {	// sec_ref_no
//								value = abbr + "-S-" + num ;
								 value = value + "(" + adv_seq + ")"; 
								cell.setCellValue("'"+value+"'");
//								num++;
							}
							else if(i == 5) {	//SEC_TYPE
								if(pay_type.equals("AP")) {
									value = "ADV";
								}
								else {
									value = "DPT";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
								}
								if(value.contains("RIL"))
								{
									value="RIL";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 8 ) {	// Currency
								
								value = currency;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 10) {	//VALUE
								value = amt;
								cell.setCellValue("'"+value+"'");
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
								
								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									cell.setCellValue("'"+rset3.getString(1)+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								rset3.close();
								stmt3.close();
							}
							else if (i == 25) {	// Status
								if(appr_flag!=null) {
									if(appr_flag.equals("Y")) {
										value = "O";
									}
									else  {
										value = "A";
									}
								}
								else {
									value = "A";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 18) {
								value = pay_dt;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 26) {
//								if(remark.length()>150) {
//									remark = remark.substring(0,150);
//								}
								value = remark;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 29) {
								value = ent_by;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 30) {
								value = ent_dt;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 33) {
								value = apr_dt;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 34) {
								value = apr_by;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 46) {
								if(dr_cr!=null) {
									if(dr_cr.equals("C")) {
										dr_cr = "CR";
									}
									else if(dr_cr.equals("D")) {
										dr_cr = "DR";
									}
								}
								else {
									dr_cr = "CR";
								}
								value = dr_cr;
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							prev_abbr=abbr;
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"S"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset4.close();
				stmt4.close();
				}
				
				
				rset1.close();
				stmt1.close();
					
//				//FOR LOA
				queryString1 = "SELECT A.TENDER_NO,A.LOA_NO,A.LOA_REV_NO,A.CUSTOMER_CD "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {	
					no = rset1.getString(1);
					rev = "0";
					cont_no = rset1.getString(2);
					cont_rev = rset1.getString(3);
					cd = rset1.getString(4);
					map = "L-"+no+"-"+cont_no+"-"+cont_rev;
					
					//LOA WITHOUT ADV
					queryString2 = "SELECT NULL,REF_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL,"
							+ "DTL_SEQ_NO ,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "NULL ,NULL,NULL,NULL "
							+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE LINK = ? AND CUSTOMER_CD = ? AND SEC_TYPE NOT IN ('ADV') "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("L"+no+"-"+"0"+"-"+cont_no+"-"+cont_rev+"-DLNG"));//L1-0-1-0
					stmt2.setString(2, rset1.getString(4));
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell.setCellValue("'"+abbr+"'");
						
						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
						cell.setCellValue("'"+map+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
						
//							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
//								cell.setCellValue("'"+seq_no+"'");									
//							}
//							else if (i == 3) {	// sec_ref_no
//								value = rset.getString(1) + "-S-" + num ;
//								cell.setCellValue("'"+value+"'");
//								num++;
//							}
							 if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
								cell.setCellValue("'"+value+"'");
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									cell.setCellValue("'"+rset3.getString(1)+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								rset3.close();
								stmt3.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
								cell.setCellValue("'"+value+"'");
							}
//							else if(i == 26) {
//								if(remark.length()>150) {
//									remark = remark.substring(0,150);
//								}
//								value = remark;
//								cell.setCellValue("'"+value+"'");
//							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							prev_abbr=abbr;
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"L"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
					
					
					//LOA WITH ADV
					String amt="",currency="",ent_by="",ent_dt="",apr_by="",apr_dt="",dr_cr="",pay_dt="",appr_flag="",pay_type="",adv_seq="";
					queryString4 = "SELECT PAY_AMT, CURRENCY, REMARK, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_BY, "
							+ "TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), DR_CR_FLAG, TO_CHAR(PAY_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_FLAG, PAY_TYPE, SEQ_NO"
							+ " FROM DLNG_ADVC_PAY_MST "
							+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'L' ORDER BY SEQ_NO";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1,cd);
					stmt4.setString(2,no);
					stmt4.setString(3,rev);
					stmt4.setString(4,cont_no);
					stmt4.setString(5,cont_rev);
					rset4 = stmt4.executeQuery();
					while(rset4.next()) {
						amt = rset4.getString(1); 
						currency =rset4.getString(2); 
						remark =rset4.getString(3); 
						ent_by =rset4.getString(4); 
						ent_dt =rset4.getString(5); 
						apr_by =rset4.getString(6); 
						apr_dt =rset4.getString(7); 
						dr_cr =rset4.getString(8); 
						pay_dt =rset4.getString(9);
						appr_flag =rset4.getString(10); 
						pay_type =rset4.getString(11); 
						adv_seq = rset4.getString(12);
						
						queryString2 = "SELECT NULL,REF_NO,REF_NO,NVL(ISSUED, 'R'), 'ADV', UPPER(DEAL_TYPE),GUARANTOR_ABBR,NULL,VARIATION_VALUE,"
								+ "NULL,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), 'A', NULL, FLAG, INORDER_HIST,NULL, NULL, NULL, NULL,"
								+ "NULL, NULL,'0', 'K',"
								+ "NULL, NULL, NULL, NULL, NULL, NULL, "
								+ "DTL_SEQ_NO ,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "'N.A',NULL,'0',NULL "
								+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE LINK = ? AND CUSTOMER_CD = ? AND SEC_TYPE ='ADV' "
								+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
								+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("L"+no+"-"+"0"+"-"+cont_no+"-"+cont_rev+"-DLNG"));//L1-0-1-0
					stmt2.setString(2, rset1.getString(4));
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
						}
						cell.setCellValue("'"+abbr+"'");
						
						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
						cell.setCellValue("'"+map+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							
							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
//								cell.setCellValue("'"+seq_no+"'");	
								value = value + "(" + adv_seq + ")"; 
								cell.setCellValue("'"+value+"'");
							}
							else if (i == 3) {	// sec_ref_no
//								value = rset.getString(1) + "-S-" + num ;
								 value = value + "(" + adv_seq + ")";
								cell.setCellValue("'"+value+"'");
//								num++;
							}
							else if(i == 5) {	//SEC_TYPE
								if(pay_type.equals("AP")) {
									value = "ADV";
								}
								else {
									value = "DPT";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
								}
								if(value.contains("RIL"))
								{
									value="RIL";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 8 ) {	// Currency
								
								value = currency;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 10) {	//VALUE
								value = amt;
								cell.setCellValue("'"+value+"'");
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
								
								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									cell.setCellValue("'"+rset3.getString(1)+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								rset3.close();
								stmt3.close();
							}
							else if (i == 25) {	// Status
								if(appr_flag!=null) {
									if(appr_flag.equals("Y")) {
										value = "O";
									}
									else  {
										value = "A";
									}
								}
								else {
									value = "A";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 18) {
								value = pay_dt;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 26) {
//								if(remark.length()>150) {
//									remark = remark.substring(0,150);
//								}
								value = remark;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 29) {
								value = ent_by;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 30) {
								value = ent_dt;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 33) {
								value = apr_dt;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 34) {
								value = apr_by;
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 46) {
								if(dr_cr!=null) {
									if(dr_cr.equals("C")) {
										dr_cr = "CR";
									}
									else if(dr_cr.equals("D")) {
										dr_cr = "DR";
									}
								}
								else {
									dr_cr = "CR";
								}
								value = dr_cr;
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							prev_abbr=abbr;
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"L"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
					}
					rset4.close();
					stmt4.close();
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			
			
			
			filename = migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST(D)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<LOG_FMS_SECURITY_MST(D)>>,,,,,,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}// END OF LOG_FMS_SECURITY_MST
	
	
	
	
	
	public void FMS_SUPPLY_CONT_DCQ_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_SUPPLY_CONT_DCQ_DTL(DLNG)()";
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SUPPLY_CONT_DCQ_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,DCQ,REMARK,STATUS,ENT_DT,"
					+ "ENT_BY,MODIFY_DT,MODIFY_BY";
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SEQ_NO,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
//			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
//					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
//					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
				
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO, 'F', A.SEQ_NO, "
						+ "TO_CHAR(A.FROM_DT, 'DD/MM/YYYY hh24:mi:ss'),  TO_CHAR(A.TO_DT, 'DD/MM/YYYY hh24:mi:ss'), A.DCQ,"
						+ " A.REMARK, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL "
						+ "FROM DLNG_SN_DCQ_DTL A, DLNG_SN_MST B "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND A.SN_NO = B.SN_NO AND A.SN_REV_NO = B.SN_REV_NO AND "
						+ "A.FLSA_NO = B.FLSA_NO AND A.FLSA_REV_NO = B.FLSA_REV_NO AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
//				stmt1.setString(1, rset.getString(2));
				stmt1.setString(1, delta_FromDt);
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_ToDt);
				stmt1.setString(4, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
//					abbr = rset1.getString(1);
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					cont_type=rset1.getString(6);
					seq_no = rset1.getString(7);
					
					
//					value = rset1.getString(1) == null ? "null" : rset1.getString(1);
					queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					rset2 = stmt2.executeQuery();
					if(rset2.next()) {
						abbr = rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
//					cell.setCellValue("'"+value+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i == 1) {
							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
						}
						
						cell.setCellValue("'"+value+"'");
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + seq_no + ","), conn, "");
					
				}
				rset1.close();
				stmt1.close();
				
				
				//LOA
				queryString2 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO, 'E', A.SEQ_NO, "
						+ "TO_CHAR(A.FROM_DT, 'DD/MM/YYYY hh24:mi:ss'),  TO_CHAR(A.TO_DT, 'DD/MM/YYYY hh24:mi:ss'), A.DCQ,"
						+ " A.REMARK, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL "
						+ "FROM DLNG_LOA_DCQ_DTL A, DLNG_LOA_MST B "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND A.TENDER_NO = B.TENDER_NO AND A.LOA_NO = B.LOA_NO AND A.LOA_REV_NO = B.LOA_REV_NO AND "
						+ "(? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt2 = conn.prepareStatement(queryString2);
//				stmt2.setString(1, rset.getString(2));
				stmt2.setString(1, delta_FromDt);
				stmt2.setString(2, delta_FromDt);
				stmt2.setString(3, delta_ToDt);
				stmt2.setString(4, delta_ToDt);
				rset2 = stmt2.executeQuery();
				
				while (rset2.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
//					abbr = rset.getString(1);
					cd = rset2.getString(1);
					no = rset2.getString(2);
					rev = rset2.getString(3);
					cont_no = rset2.getString(4);
					cont_rev = rset2.getString(5);
					cont_type=rset2.getString(6);
					seq_no = rset2.getString(7);
					
					
					queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					rset2 = stmt2.executeQuery();
					if(rset2.next()) {
						abbr = rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
//					cell.setCellValue("'"+value+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset2.getString(i) == null ? "null" : rset2.getString(i);
						
						if(i == 1) {
							cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
						}
						cell.setCellValue("'"+value+"'");
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + seq_no + ","), conn, "");
					
				}
				
				rset2.close();
				stmt2.close();
				
//			}
//			stmt.close();
//			rset.close();
			filename = migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_DCQ_DTL(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_CONT_DCQ_DTL(DLNG)>>,,,,", conn);

			logger.checkpoint1(fname1,count+",", conn);

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}


	
	public void FMS_SUPPLY_PURCHASE_MAP_DTL() throws SQLException, IOException {

		function_nm = "FMS_SUPPLY_PURCHASE_MAP_DTL(DLNG)()";
		try {

			System.out.println("<<START>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SUPPLY_PURCHASE_MAP_DTL(DLNG)>>,,,,", conn);

			logger.checkpoint1(fname1, function_nm + "E" + "," + start_end_dt + ",", conn);

			columns = "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PUR_CONT_NO, ALLOC_QTY, "
					+ "QTY_UNIT, SALE_PRICE, RATE_UNIT, COST_PRICE, CP_UNIT, MARGIN, TOTAL_MARGIN, STATUS, ENT_BY, ENT_DT, MODIFY_BY,"
					+ " MODIFY_DT, APRV_BY, APRV_DT, AVG_MARGIN, AUTH_BY, AUTH_DT, CARGO_NO";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			row = spreadsheet.createRow(nrow++);
			//String cargo_seq_no = "", abbr_new="";

			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			logger.checkpoint(fname,
					"COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,TIMESTAMP,",
					conn);

			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();

			while (rset.next()) {
				
				//SN

				queryString1 = "SELECT DISTINCT(B.CUSTOMER_CD), A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO, 'F', A.CARGO_REF_NO, A.ALLOC_QTY, "
						+ "A.QTY_UNIT, A.SALE_PRICE, A.RATE_UNIT, A.COST_PRICE, A.CP_UNIT, A.MARGIN, A.TOTAL_MARGIN, A.STATUS, A.EMP_CD, "
						+ "TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, A.APRV_BY, TO_CHAR(A.APRV_DT, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "A.AVG_MARGIN, A.AUTH_BY, TO_CHAR(A.AUTH_DT, 'DD/MM/YYYY hh24:mi:ss'), 0 "
						+ "FROM DLNG_SN_CARGO_DTL A, FMS7_CUSTOMER_MST B, DLNG_SN_MST C "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND "
						+ "A.FLSA_NO = C.FLSA_NO AND A.FLSA_REV_NO = C.FLSA_REV_NO AND A.SN_NO = C.SN_NO AND A.SN_REV_NO = C.SN_REV_NO "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;

					abbr = rset.getString(1);
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					cont_type = rset1.getString(6);


					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);

						
						if(i==1) {
							cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
						}
						else if(i==7) {
							cargo_ref = rset1.getString(7);
							
							queryString2 = "SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD= ?";
				    		stmt2 = conn.prepareStatement(queryString2);
				    		stmt2.setString(1,cargo_ref);
				    		rset2 = stmt2.executeQuery();
				    		
				    		if (rset2.next()) {
				    			dom_flag = rset2.getString(1);
				    			
				    			if(dom_flag == null || dom_flag.equals("N")) {
				    				value = cargo_ref;
				    			}else if(dom_flag.equals("Y")) {
				    				dom_flag1 = "D";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}else if(dom_flag.equals("K")) {
				    				dom_flag1 = "I";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}
				    			else if(dom_flag.equals("T")) {
				    				dom_flag1 = "T";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}
				    			
				    		}
				    		rset2.close();
				    		stmt2.close();
						}
						
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + no + "," + rev + ","
							+ cont_no + "," + cont_rev + ","), conn, "");

				}
				rset1.close();
				stmt1.close();

				//LOA
				queryString2 = "SELECT DISTINCT(B.CUSTOMER_CD), A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO, 'E', A.CARGO_REF_NO, A.ALLOC_QTY, A.QTY_UNIT, "
						+ "A.SALE_PRICE, A.RATE_UNIT, A.COST_PRICE, A.CP_UNIT, A.MARGIN, A.TOTAL_MARGIN, A.STATUS, A.EMP_CD, "
						+ "TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, A.APRV_BY, TO_CHAR(A.APRV_DT, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "A.AVG_MARGIN, A.AUTH_BY, TO_CHAR(A.AUTH_DT, 'DD/MM/YYYY hh24:mi:ss'), '0' "
						+ "FROM DLNG_LOA_CARGO_DTL A, FMS7_CUSTOMER_MST B, DLNG_LOA_MST C "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND "
						+ "A.TENDER_NO = C.TENDER_NO AND A.LOA_NO = C.LOA_NO AND A.LOA_REV_NO = C.LOA_REV_NO "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset.getString(2));
				stmt2.setString(2, delta_FromDt);
				stmt2.setString(3, delta_FromDt);
				stmt2.setString(4, delta_ToDt);
				stmt2.setString(5, delta_ToDt);
				rset2 = stmt2.executeQuery();

				while (rset2.next()) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;

					abbr = rset.getString(1);
					cd = rset2.getString(1);
					no = rset2.getString(2);
					rev = rset2.getString(3);
					cont_no = rset2.getString(4);
					cont_rev = rset2.getString(5);
					cont_type = rset2.getString(6);

					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset2.getString(i) == null ? "null" : rset2.getString(i);

						if(i == 1) {
							cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
							value = cont_ref;
						}
						else if(i==7) {
							cargo_ref = rset2.getString(7);
							
							queryString3 = "SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD= ?";
				    		stmt3 = conn.prepareStatement(queryString3);
				    		stmt3.setString(1,cargo_ref);
				    		rset3 = stmt3.executeQuery();
				    		
				    		if (rset3.next()) {
				    			dom_flag = rset3.getString(1);
				    			
				    			if(dom_flag == null || dom_flag.equals("N")) {
				    				value = cargo_ref;
				    			}else if(dom_flag.equals("Y")) {
				    				dom_flag1 = "D";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}else if(dom_flag.equals("K")) {
				    				dom_flag1 = "I";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}
				    			else if(dom_flag.equals("T")) {
				    				dom_flag1 = "T";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}
				    			cell.setCellValue("'" + value + "'");
				    		}
							
				    		rset3.close();
				    		stmt3.close();
						}

							cell.setCellValue("'" + value + "'");

					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + no + "," + rev + ","
							+ cont_no + "," + cont_rev + ","), conn, "");

				}
				rset2.close();
				stmt2.close();


			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_SUPPLY_PURCHASE_MAP_DTL(DLNG)_" + start_end_dt + ".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();

			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_PURCHASE_MAP_DTL(DLNG)>>,,,,", conn);

			logger.checkpoint1(fname1, count + ",", conn);

			System.out.println("<<END>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";

			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

	}

//FMS_SUPPLY_ALLOC_REVISED
	public void FMS_SUPPLY_ALLOC_REVISED() throws SQLException, IOException {

		function_nm = "FMS_SUPPLY_ALLOC_REVISED(DLNG)()";
		try {

			System.out.println("<<START>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SUPPLY_ALLOC_REVISED(DLNG)>>,,,,", conn);

			logger.checkpoint1(fname1, function_nm + "E" + "," + start_end_dt + ",", conn);

			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,MODIFICATION_SEQ_NO,PUR_CONT,NEW_PRICE_EFF_DT,"
					+ "ORI_SALE_PRICE,NEW_SALE_PRICE,ORI_MARGIN,NEW_MARGIN,ORI_AVG_MARGIN,NEW_AVG_MARGIN,ORI_TOT_MARGIN,NEW_TOT_MARGIN,"
					+ "ENT_BY,ENT_DT,APPROVE_BY,APPROVE_DT,FLAG,REMARK,ALLOC_QTY,CARGO_NO";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			row = spreadsheet.createRow(nrow++);
			//String cargo_seq_no = "", abbr_new="";

			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			logger.checkpoint(fname,
					"COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,TIMESTAMP,",
					conn);

			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();

			while (rset.next()) {
				
                //SN
				queryString1 = "SELECT DISTINCT(B.CUSTOMER_CD), A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO,A.CONTRACT_TYPE,A.MODIFICATION_SEQ_NO,A.CARGO_REF_NO, "
						+ "TO_CHAR(A.NEW_PRICE_EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.ORI_SALE_PRICE, A.NEW_SALE_PRICE, A.ORI_MARGIN, A.NEW_MARGIN, "
						+ "A.ORI_AVG_MARGIN, A.NEW_AVG_MARGIN, A.ORI_TOT_MARGIN, A.NEW_TOT_MARGIN,"
						+ "A.ENT_BY,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'),A.APPROVE_BY, TO_CHAR(A.APPROVE_DT, 'DD/MM/YYYY hh24:mi:ss'),A.FLAG,A.REMARK, A.ALLOC_QTY,'0' "
						+ "FROM DLNG_CARGO_ALLOC_REVISED_DTL A, FMS7_CUSTOMER_MST B, DLNG_SN_MST C WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND "
						+ "A.FLSA_NO = C.FLSA_NO AND A.FLSA_REV_NO = C.FLSA_REV_NO AND A.SN_NO = C.SN_NO AND A.SN_REV_NO = C.SN_REV_NO AND CONTRACT_TYPE = 'S' "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;

					abbr = rset.getString(1);
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					cont_type = rset1.getString(6);

					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);

						if(i==1) {
							value = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						}
						else if(i==6) {
							if(cont_type.equals("S")) {
								value="F";
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i==8) {
							cargo_ref = rset1.getString(8);
							
							queryString2 = "SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD= ?";
				    		stmt2 = conn.prepareStatement(queryString2);
				    		stmt2.setString(1,cargo_ref);
				    		rset2 = stmt2.executeQuery();
				    		
				    		if (rset2.next()) {
				    			dom_flag = rset2.getString(1);
				    			
				    			if(dom_flag == null || dom_flag.equals("N")) {
				    				value = cargo_ref;
				    			}else if(dom_flag.equals("Y")) {
				    				dom_flag1 = "D";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}else if(dom_flag.equals("K")) {
				    				dom_flag1 = "I";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}
				    			else if(dom_flag.equals("T")) {
				    				dom_flag1 = "T";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}
				    			
				    		}
				    		rset2.close();
				    		stmt2.close();
						}
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + no + "," + rev + ","
							+ cont_no + "," + cont_rev + ","), conn, "");

				}
				rset1.close();
				stmt1.close();

				//LOA
				queryString2 = "SELECT DISTINCT(B.CUSTOMER_CD), A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO,A.CONTRACT_TYPE,A.MODIFICATION_SEQ_NO,A.CARGO_REF_NO, "
						+ "TO_CHAR(A.NEW_PRICE_EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.ORI_SALE_PRICE, A.NEW_SALE_PRICE, A.ORI_MARGIN, A.NEW_MARGIN, "
						+ "A.ORI_AVG_MARGIN, A.NEW_AVG_MARGIN, A.ORI_TOT_MARGIN, A.NEW_TOT_MARGIN,"
						+ "A.ENT_BY,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'),A.APPROVE_BY, TO_CHAR(A.APPROVE_DT, 'DD/MM/YYYY hh24:mi:ss'),A.FLAG,A.REMARK, A.ALLOC_QTY,'0' "
						+ "FROM DLNG_CARGO_ALLOC_REVISED_DTL A, FMS7_CUSTOMER_MST B, DLNG_LOA_MST C "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND "
						+ "A.FLSA_NO = C.TENDER_NO AND A.SN_NO = C.LOA_NO AND A.SN_REV_NO = C.LOA_REV_NO AND A.CONTRACT_TYPE = 'L'"
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset.getString(2));
				stmt2.setString(2, delta_FromDt);
				stmt2.setString(3, delta_FromDt);
				stmt2.setString(4, delta_ToDt);
				stmt2.setString(5, delta_ToDt);
				rset2 = stmt2.executeQuery();

				while (rset2.next()) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;

					abbr = rset.getString(1);
					cd = rset2.getString(1);
					no = rset2.getString(2);
					rev = rset2.getString(3);
					cont_no = rset2.getString(4);
					cont_rev = rset2.getString(5);
					cont_type = rset2.getString(6);


					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset2.getString(i) == null ? "null" : rset2.getString(i);

						if (i == 1) {
							value = "L" + "-" + no + "-" + cont_no + "-" + cont_rev;
						}
						else if(i==7) {
							if(cont_type.equals("L")) {
								value="E";
							}
							cell.setCellValue("'"+value+"'");
						}
						if(i==8) {
							cargo_ref = rset2.getString(8);
							
							queryString3 = "SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD= ?";
				    		stmt3 = conn.prepareStatement(queryString3);
				    		stmt3.setString(1,cargo_ref);
				    		rset3 = stmt3.executeQuery();
				    		
				    		if (rset3.next()) {
				    			dom_flag = rset3.getString(1);
				    			
				    			if(dom_flag == null || dom_flag.equals("N")) {
				    				value = cargo_ref;
				    			}else if(dom_flag.equals("Y")) {
				    				dom_flag1 = "D";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}else if(dom_flag.equals("K")) {
				    				dom_flag1 = "I";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}
				    			else if(dom_flag.equals("T")) {
				    				dom_flag1 = "T";
				    				value = "-"+dom_flag1+"-"+cargo_ref+"-";
				    			}
				    			
				    		}
				    		rset3.close();
				    		stmt3.close();
						}

						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + no + "," + rev + ","
							+ cont_no + "," + cont_rev + ","), conn, "");

				}
				rset2.close();
				stmt2.close();

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_SUPPLY_ALLOC_REVISED(DLNG)_" + start_end_dt + ".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();

			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_ALLOC_REVISED(DLNG)>>,,,,", conn);

			logger.checkpoint1(fname1, count + ",", conn);

			System.out.println("<<END>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";

			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

	}



	//FMS_CONT_PRICE_DTL
	public void FMS_CONT_PRICE_DTL() throws SQLException, IOException {		
			function_nm = "FMS_CONT_PRICE_DTL(DLNG)()";
			
			try {

				System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
				logger.checkpoint(fname, "<<START>>,<<FMS_CONT_PRICE_DTL(DLNG)>>,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
				
				columns = "COMPANY_CD,MAAPPING_ID,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,PRICE_TYPE,CURVE_NM,SLOPE,CONST,QUANTITY_UNIT,"
						+ "RATE,RATE_UNIT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,"
						+ "PHYS_CURVE_NM,CURVE_LOGIC,FORMULA";
				workbook = new XSSFWorkbook();
				spreadsheet = workbook.createSheet("Sheet 1");

				nrow = 0;
				ncell = 0;
				count = 0;
				
				row = spreadsheet.createRow(nrow++);
				
				// Inserting Column Names
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					cell.setCellValue(columns.split(",")[i]);
				}
				
				String seq_no = "";

				logger.checkpoint(fname, "ABBR,CONT_TYPE,SEQ_NO,TIMESTAMP", conn);
				
				// Inserting Rest of the data
				queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
						+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
						+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				//SN
				while (rset.next()) {
					
					queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO "
							+ " FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
							+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
							+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, rset.getString(2));
					stmt1.setString(2, delta_FromDt);
					stmt1.setString(3, delta_FromDt);
					stmt1.setString(4, delta_ToDt);
					stmt1.setString(5, delta_ToDt);
					rset1 = stmt1.executeQuery();
					
					while (rset1.next()) {
						
						queryString2 = " SELECT MAPPING_ID, CONTRACT_TYPE, SEQ_NO, TO_CHAR(FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(TO_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ " NVL(PRICE_TYPE, ''), CURVE_NM, SLOPE, CONST, QUANTITY_UNIT, RATE, RATE_UNIT, NULL, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "NULL, NULL, PRICE_RANGE, TO_CHAR(PRICE_START_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(PRICE_END_DT, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "PHYS_CURVE_NM, NULL, NVL(REMARKS, ' ') FROM FMS9_MRCR_CONT_PRICE_DTL WHERE MAPPING_ID = ? AND CONTRACT_TYPE = 'E' "
								+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)+"-"+rset1.getString(5));
						stmt2.setString(2, delta_FromDt);
						stmt2.setString(3, delta_FromDt);
						stmt2.setString(4, delta_ToDt);
						stmt2.setString(5, delta_ToDt);
						rset2 = stmt2.executeQuery();
						
						while (rset2.next()) {
						
							abbr = rset.getString(1);						
							cont_type = "E";
							seq_no = rset2.getString(3);
							row = spreadsheet.createRow(nrow++);
							ncell = 0;
									
						
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
						    }
							cell = row.createCell(ncell++);
							cell.setCellValue("'"+abbr+"'");
						
							
							for (int i = 1; i < columns.split(",").length; i++) {
								cell = row.createCell(ncell++);
								value = rset2.getString(i) == null ? "null" : rset2.getString(i);
								
								if(i==2) {
									cont_type=rset2.getString(2);
									if(cont_type.equals("E")) {
										value = "F";
									}
									cell.setCellValue("'"+value+"'");
								}
								else if(i == 13 && (rset2.getString(6).equals("F") || !rset2.getString(23).contains("@"))) {	// Remarks
									value = rset2.getString(23);
									cell.setCellValue("'"+value+"'");
								}						
								else if (i == 18 && !value.equals("null")) {	// Price Range
									if (value.equals("A") && rset2.getString(19) != null) {
										value = "D";
									}
									else if(value.length() > 1) {
										value = "O"+value.substring(1);
									}
									else if (rset2.getString(23).contains("MIN@") || rset2.getString(23).contains("MAX@")) {
										value = "null";
									}
									cell.setCellValue("'"+value+"'");
								}
								else if(i == 22 && rset2.getString(6).equals("M")) {	// Curve_Logic
										if (rset2.getString(23) == null || !rset2.getString(23).contains("@")) {
											value = "SINGLE";
										}
										else {
											value = rset2.getString(23).split("@")[0];
										}
									cell.setCellValue("'"+value+"'");
								}
								else if(i == 23 && (rset2.getString(6).equals("F") || !rset2.getString(23).contains("@"))) {	// Formula
									value = "";
									cell.setCellValue("'"+value+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								
							}											
							count++;
							logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
						}
						rset2.close();
						stmt2.close();
					}
					rset1.close();
					stmt1.close();
					
					
					//LOA
					
					queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0',A.LOA_NO,A.LOA_REV_NO "
							+ " FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
							+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
							+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, rset.getString(2));
					stmt1.setString(2, delta_FromDt);
					stmt1.setString(3, delta_FromDt);
					stmt1.setString(4, delta_ToDt);
					stmt1.setString(5, delta_ToDt);
					rset1 = stmt1.executeQuery();
					
					while (rset1.next()) {
						
						queryString2 = " SELECT MAPPING_ID, CONTRACT_TYPE, SEQ_NO, TO_CHAR(FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(TO_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ " NVL(PRICE_TYPE, ''), CURVE_NM, SLOPE, CONST, QUANTITY_UNIT, RATE, RATE_UNIT, NULL, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "NULL, NULL, PRICE_RANGE, TO_CHAR(PRICE_START_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(PRICE_END_DT, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "PHYS_CURVE_NM, NULL, NVL(REMARKS, ' ') FROM FMS9_MRCR_CONT_PRICE_DTL WHERE MAPPING_ID = ? AND CONTRACT_TYPE = 'F' "
								+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, rset1.getString(1)+"-"+rset1.getString(2)+"-"+"0"+"-"+rset1.getString(4)+"-"+rset1.getString(5));
						stmt2.setString(2, delta_FromDt);
						stmt2.setString(3, delta_FromDt);
						stmt2.setString(4, delta_ToDt);
						stmt2.setString(5, delta_ToDt);
						rset2 = stmt2.executeQuery();
						
						while (rset2.next()) {
						
							abbr = rset.getString(1);						
							cont_type = "L";
							seq_no = rset2.getString(3);
							row = spreadsheet.createRow(nrow++);
							ncell = 0;
									
						
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
						    }
							cell = row.createCell(ncell++);
							cell.setCellValue("'"+abbr+"'");
						
							
							for (int i = 1; i < columns.split(",").length; i++) {
								cell = row.createCell(ncell++);
								value = rset2.getString(i) == null ? "null" : rset2.getString(i);
								
								if(i == 1) {
									map_id = cont_type+"-"+rset2.getString(1);
									cell.setCellValue("'"+map_id+"'");
								}
								else if(i==2) {
									cont_type=rset2.getString(2);
									if(cont_type.equals("F")) {
										value = "E";
									}
									cell.setCellValue("'"+value+"'");
								}
								else if(i == 13 && (rset2.getString(6).equals("F") || !rset2.getString(23).contains("@"))) {	// Remarks
									value = rset2.getString(23);
									cell.setCellValue("'"+value+"'");
								}						
								else if (i == 18 && !value.equals("null")) {	// Price Range
									if (value.equals("A") && rset2.getString(19) != null) {
										value = "D";
									}
									else if(value.length() > 1) {
										value = "O"+value.substring(1);
									}
									else if (rset2.getString(23).contains("MIN@") || rset2.getString(23).contains("MAX@")) {
										value = "null";
									}
									cell.setCellValue("'"+value+"'");
								}
								else if(i == 22 && rset2.getString(6).equals("M")) {	// Curve_Logic
										if (rset2.getString(23) == null || !rset2.getString(23).contains("@")) {
											value = "SINGLE";
										}
										else {
											value = rset2.getString(23).split("@")[0];
										}
									cell.setCellValue("'"+value+"'");
								}
								else if(i == 23 && (rset2.getString(6).equals("F") || !rset2.getString(23).contains("@"))) {	// Formula
									value = "";
									cell.setCellValue("'"+value+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
							}											
							count++;
							logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
						}
						rset2.close();
						stmt2.close();
					}
					rset1.close();
					stmt1.close();
				
				}
				stmt.close();
				rset.close();
				
				filename = migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL(DLNG)_"+start_end_dt+".xlsx";
				
				fileOut = new FileOutputStream(filename);  
				
				workbook.write(fileOut);
				fileOut.close(); 

				System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
				System.out.println();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_CONT_PRICE_DTL(DLNG)>>,,,,", conn);

				logger.checkpoint1(fname1,count+",", conn);

				msg = "Data has been Extracted Successfully.";
				msg_type = "S";
				
			}
			catch (Exception e) {

				msg = "One of the Functions faced an Error. Extraction Terminated.";
				msg_type = "E";
				
				//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"  ", e);
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				logger.error(fname, e, function_nm, conn, fname_error);
			}
			
		}	

	//FMS_CONT_PRICE_MIN_DTL
	public void FMS_CONT_PRICE_MIN_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_CONT_PRICE_MIN_DTL(DLNG)()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_CONT_PRICE_MIN_DTL(DLNG)>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,CURVE_LOGIC,CURVE_NM,SLOPE,CONST,QUANTITY_UNIT,"
					+ "RATE,RATE_UNIT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String curve_nm="";
			
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			String seq_no = "";

			logger.checkpoint(fname, "ABBR,CONT_TYPE,SEQ_NO,TIMESTAMP", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO "
						+ " FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					queryString2 = " SELECT MAPPING_ID, CONTRACT_TYPE, SEQ_NO, TO_CHAR(FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(TO_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, CURVE_NM, SLOPE, CONST, '1', NULL, NULL, PRICE_RANGE, TO_CHAR(PRICE_START_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "TO_CHAR(PRICE_END_DT, 'DD/MM/YYYY HH24:MI:SS'), REMARKS, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, "
							+ "NULL FROM FMS9_MRCR_CONT_FIN_PRICE_DTL WHERE MAPPING_ID = ? AND CONTRACT_TYPE = 'E' AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)+"-"+rset1.getString(5));
					stmt2.setString(2, delta_FromDt);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_ToDt);
					stmt2.setString(5, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
					
						abbr = rset.getString(1);						
						cont_type = "";
						seq_no = rset2.getString(3);
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
								
					
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell = row.createCell(ncell++);
						cell.setCellValue("'"+abbr+"'");
					
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							
							
							if(i == 6) {	// Curve_Logic
								value = rset2.getString(16).split("@")[0];
								cell.setCellValue("'"+value+"'");
							}
							else if(i==2) {
								cont_type= rset2.getString(2);
								if(cont_type.equals("E")) {
									value="F";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 7) {	// Curve_Nm
								curve_nm = rset2.getString(7);
								cell.setCellValue("'"+curve_nm+"'");
							}
							else if (i == 13 && !value.equals("null")) {	// Price Range
								if (value.equals("A") && rset2.getString(14) != null) {
									value = "D";
								}
								else if(value.length() > 1 && !value.contains("@")) {
									value = "O"+value.substring(1);
								}
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}											
						count++;
						logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				
				//LOA
					
				queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0',A.LOA_NO,A.LOA_REV_NO "
						+ " FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					queryString2 = " SELECT MAPPING_ID, CONTRACT_TYPE, SEQ_NO, TO_CHAR(FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(TO_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, CURVE_NM, SLOPE, CONST, '1', NULL, NULL, PRICE_RANGE, TO_CHAR(PRICE_START_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "TO_CHAR(PRICE_END_DT, 'DD/MM/YYYY HH24:MI:SS'), REMARKS, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, "
							+ "NULL FROM FMS9_MRCR_CONT_FIN_PRICE_DTL WHERE MAPPING_ID = ? AND CONTRACT_TYPE = 'F' AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset1.getString(1)+"-"+rset1.getString(2)+"-"+"0"+"-"+rset1.getString(4)+"-"+rset1.getString(5));
					stmt2.setString(2, delta_FromDt);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_ToDt);
					stmt2.setString(5, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
					
						abbr = rset.getString(1);						
						cont_type = "L";
						seq_no = rset2.getString(3);
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
								
					
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell = row.createCell(ncell++);
						cell.setCellValue("'"+abbr+"'");
					
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							
							if(i == 1) {
								map_id = cont_type+"-"+rset2.getString(1);
								cell.setCellValue("'"+map_id+"'");
							}
							else if(i==2) {
								cont_type = rset2.getString(2);
								if(cont_type.equals("F")) {
									value="E";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 6) {	// Curve_Logic
								value = rset2.getString(16).split("@")[0];
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 7) {	// Curve_Nm
								curve_nm = rset2.getString(7);
								cell.setCellValue("'"+curve_nm+"'");
							}
							else if (i == 13 && !value.equals("null")) {	// Price Range
								if (value.equals("A") && rset2.getString(14) != null) {
									value = "D";
								}
								else if(value.length() > 1 && !value.contains("@")) {
									value = "O"+value.substring(1);
								}
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}											
						count++;
						logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
			
				
				
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL(DLNG)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_CONT_PRICE_MIN_DTL(DLNG)>>,,,,", conn);

			logger.checkpoint1(fname1,count+",", conn);

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}	
	
	public void FMS_AGMT_SVC_MST() throws SQLException, IOException {
		function_nm = "FMS_AGMT_SVC_MST()";
		
		try {
			
			System.out.println("<<START>><<FMS_AGMT_SVC_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_AGMT_SVC_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,SIGNING_DT,START_DT,END_DT,STATUS,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,"
					+ "DAY_END_TIME,MMCQ_FLAG,MMCQ_CLAUSE,MMCQ_PERCENTAGE,BILLING_FLAG,BILLING_CLAUSE,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String  type = "",counterparty="",abbr="",clause="",mmcq="",name="",ref="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT DISTINCT(A.AGREEMENT_NO), A.CUSTOMER_CD, 'T', A.AGREEMENT_NO, A.AGREEMENT_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, TO_CHAR(A.TRANS_CONT_START_DT, 'DD/MM/YYYY HH24:MI:SS'), "
					+ "TO_CHAR(A.TRANS_CONT_START_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, 'A', 'Y', NULL, '06:00', '06:00', 'Y', NULL, NULL, 'Y', NULL, '1', TO_CHAR(A.TRANS_CONT_START_DT, 'DD/MM/YYYY HH24:MI:SS'), "
					+ "NULL, NULL, NULL  FROM DLNG_SALES_TRANSPORTER_MST A , DLNG_FLSA_MST B WHERE A.AGREEMENT_NO = B.FLSA_NO AND A.AGREEMENT_REV_NO = B.REV_NO AND A.CONTRACT_REV_NO = '0' "
					+ "AND A.TRANS_CONT_START_DT = (SELECT MIN(C.TRANS_CONT_START_DT) FROM DLNG_SALES_TRANSPORTER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD AND  A.CUSTOMER_CD  = C.CUSTOMER_CD ) "
					+ "AND A.CUSTOMER_CD = B.CUSTOMER_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) " ;
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,MMCQ_PERCENTAGE,TIMESTAMP,", conn);
			
			while (rset.next()) {
				no = rset.getString(1);
				cd = rset.getString(2);
				type = rset.getString(3);
				rev = rset.getString(5);
				cont_no = rset.getString(6);
				cont_rev = rset.getString(7);
//				trans_cd = rset.getString(1);
					
					row = spreadsheet.createRow(nrow++);
					value = "";
					name = "";
					ref="";
					mmcq="";
					for (int i = 0; i < columns.split(",").length; i++) {
						
						cell = row.createCell(i);
						value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
						value = value.trim().equals("") ? "null" : value;
						
						if(i == 0) {	//COMPANY_CD
							value = company_cd;
						}
						else if(i == 1) {	//COUNTERPARTY_CD
							queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = rset2.getString(1);
								if(mpe.counterparty_map.containsKey(value)) {
									value = mpe.counterparty_map.get(value);
								}
								value = value.toUpperCase();
								counterparty = value;
							}
							rset2.close();
							stmt2.close();
						}
						 
						else if(i == 5) {	//AGMT_NAME
							 name = "SEIPL-"+counterparty+"-T"+no+"-REV"+rev;
							 value = name;
						}
						
						else if(i == 6) {	//AGMT_REF
							ref = counterparty+"-FL"+no+"-FLREV"+rev;
							value = ref;
						}
						
						else if(i == 9) {	//END_DT
							queryString2 = "SELECT TO_CHAR(TRANS_CONT_END_DT, 'DD/MM/YYYY HH24:MI:SS') FROM DLNG_SALES_TRANSPORTER_MST WHERE CUSTOMER_CD = ? ORDER BY TRANS_CONT_END_DT DESC ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
						}
						
						else if(i == 17) {	//MMCQ_PERCENTAGE
							queryString2 = "SELECT MAX(MDCQ_PERCENTAGE) FROM DLNG_SN_MST WHERE CUSTOMER_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								mmcq = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							value = mmcq;
						}
						
						
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (company_cd + "," + counterparty + "," + type + "," + no + "," + rev + "," + name + "," + ref + "," + mmcq + "," ), conn, "");
				}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_AGMT_SVC_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_SVC_MST>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_AGMT_SVC_MST>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_SVC_CONT_MST() throws SQLException, IOException {
		function_nm = "FMS_SVC_CONT_MST()";
		
		try {
			
			System.out.println("<<START>><<FMS_SVC_CONT_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SVC_CONT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,CONT_REF_NO,DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,"
					+ "DCQ,VARIABLE_DCQ,RENEWAL_DT,CONT_STATUS,IS_ALLOCATED,FILL_STATION_CD,PLANT_SEQ_NO,NEW_START_DT,NEW_END_DT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,LAYOVER_CHARGE_INR,"
					+ "LAYOVER_HRS,DAY_DEF_FLAG,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,MMCQ_FLAG,MMCQ_CLAUSE,MMCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,CHANGE_DATE_REQ,"
					+ "CHANGE_DATE_APPROVE,BILLING_FLAG,BILLING_CLAUSE,TRANSPORT_MGMT_CHARGE,TRANSPORT_MGMT_UNIT,EFF_DT,QTY_OPTION,QTY_OPTION_FIRM,QTY_OPTION_RE,ENT_DT,ENT_BY,"
					+ "MODIFY_DT,MODIFY_BY";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String  type = "",counterparty="",abbr="",clause="",dcq="",name="",ref="",trans_cont_no="",st_dt="",end_dt="",cont_status="";
			String day_def="",day_st="",day_et="",mmcq="",mmcq_per="",fcc_by="",fcc_dt="",truck_firm="",truck_re="",qty_firm="",qty_re="",plant="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			queryString = " SELECT TO_CHAR(A.ENT_DT, 'YY'), A.CUSTOMER_CD, A.AGREEMENT_NO, A.AGREEMENT_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, 'B', A.TRANS_CONT_NO, NULL, TO_CHAR(A.SIGNING_DT, 'DD/MM/YYYY HH24:MI:SS'),'00:00', "
					+ "TO_CHAR(A.SIGNING_DT, 'DD/MM/YYYY HH24:MI:SS'),'00:00',TO_CHAR(A.TRANS_CONT_START_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.TRANS_CONT_END_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
					+ "NULL, 'Y', 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL,  NULL, '1', "
					+ "NULL, NULL, A.TRUCK_FIRM, A.TRUCK_RE, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.USER_CD, TO_CHAR(A.UPDATE_DT, 'DD/MM/YYYY HH24:MI:SS'), UPDATE_BY  "
					+ "FROM DLNG_SALES_TRANSPORTER_MST A , DLNG_FLSA_MST B WHERE A.AGREEMENT_NO = B.FLSA_NO AND A.AGREEMENT_REV_NO = B.REV_NO "
					+ "AND A.CUSTOMER_CD = B.CUSTOMER_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) " ;
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
//			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,MMCQ_PERCENTAGE,TIMESTAMP,", conn);
			
			while (rset.next()) {
				
				
				cd = rset.getString(2);
				no = rset.getString(3);
				rev = rset.getString(4);
				cont_no = rset.getString(5);
				cont_rev = rset.getString(6);
				type = rset.getString(7);
				trans_cont_no = rset.getString(8);
				
//				trans_cd = rset.getString(1);
				
				queryString1 = "SELECT PLANT_SEQ_NO FROM DLNG_SN_PLANT_MST WHERE FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CUSTOMER_CD = ?";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, no);
				stmt1.setString(2, rev);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev);
				stmt1.setString(5, cd);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) {
				
				plant = rset1.getString(1);
				row = spreadsheet.createRow(nrow++);
				value = "";
				name = "";ref="";dcq="";day_def="";day_st="";day_et="";mmcq="";mmcq_per="";fcc_by="";fcc_dt="";truck_firm="";truck_re="";qty_firm="";qty_re="";
				for (int i = 0; i < columns.split(",").length; i++) {
					
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null ) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
//					if(i == 0) {	//COMPANY_CD
//						value = company_cd;
//					}
					 if(i == 1) {	//COUNTERPARTY_CD
						queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
							map = value + "-" +plant;
							p_seq_no = plant;
							if(mpe.customer_map.containsKey(map))
							{
								p_seq_no=mpe.customer_map.get(map);
							}
							if(mpe.counterparty_map.containsKey(value)) {
								value = mpe.counterparty_map.get(value);
							}
							value = value.toUpperCase();
							counterparty = value;
						}
						rset2.close();
						stmt2.close();
						ref = counterparty+"-"+no+"-"+rev+"-"+plant+"-"+cont_no+"-"+cont_rev;
						value = ref;
					}
					
					else if(i == 7) {	//CONT_NAME
						name = counterparty+"-FL"+no+"-FLREV"+rev+"-SN"+cont_no+"-SNREV"+cont_rev+"-"+trans_cont_no;
						value = name;
					}
					
					else if(i == 8) {	//AGMT_REF
//						ref = counterparty+"-FL"+no+"-FLREV"+rev+"-SN"+cont_no+"-SNREV"+cont_rev;
						value = ref;
					}
					 
					else if(i == 13) {	//START_DT
						queryString2 = "SELECT TO_CHAR(START_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(END_DT, 'DD/MM/YYYY HH24:MI:SS'),TCQ,"
								+ "SN_CLOSURE_REQUEST,SN_CLOSURE_CLOSE,TO_CHAR(SN_CLOSURE_DT, 'DD/MM/YYYY hh24:mi:ss'),SN_CLOSURE_QTY "
								+ "FROM DLNG_SN_MST WHERE CUSTOMER_CD = ? AND SN_NO = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? ORDER BY SN_REV_NO DESC";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd); 
						stmt2.setString(2, cont_no); 
//						stmt2.setString(3, cont_rev); 
						stmt2.setString(3, no); 
						stmt2.setString(4, rev); 
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							st_dt = rset2.getString(1);
							end_dt = rset2.getString(2);
							dcq = rset2.getString(3);
							
							sn_req = rset2.getString(4);
							sn_close = rset2.getString(5);
							sn_dt = rset2.getString(6);
							sn_qty = rset2.getString(7);
							
							if(sn_req!=null) {
								if(sn_req.equals("Y") && sn_close.equals("Y")) {								
										sn_req = "A";
										cont_status = "C";
								}
								else if(sn_req.equals("Y") && sn_close.equals("N")) {							
										sn_req = "Y";
										cont_status = "P";
								}
								
								else { 
									cont_status = "Y";
									sn_req = null;
								}
							}
						}
						rset2.close();
						stmt2.close();
						value = st_dt;
					}
					 
					else if(i == 14) {	//END_DT
						value = end_dt;
					}
					
					else if(i == 15) {	//DCQ
						value = dcq;
					}
					
					else if(i == 16) {	//VARIABLE_DCQ
						queryString2 = "SELECT VARIATION_MODE FROM DLNG_SN_MST WHERE CUSTOMER_CD = ? AND SN_NO = ? AND SN_REV_NO = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? ORDER BY SN_REV_NO DESC";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd); 
						stmt2.setString(2, cont_no); 
						stmt2.setString(3, cont_rev); 
						stmt2.setString(4, no); 
						stmt2.setString(5, rev); 
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
						}
						rset2.close();
						stmt2.close();
					}
					 
					else if(i == 18) {	//CONT_STATUS
						value = cont_status;
					}
					
					else if(i == 20) {	//FILL_STATION_CD
						queryString2 = "SELECT TRANSPORTER_CD FROM DLNG_SN_TRANSPORTER_MST WHERE CUSTOMER_CD = ? AND SN_NO = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd); 
						stmt2.setString(2, cont_no); 
//						stmt2.setString(3, cont_rev); 
						stmt2.setString(3, no); 
						stmt2.setString(4, rev); 
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = rset2.getString(1);
						}
						rset2.close();
						stmt2.close();
					}
					
					else if(i == 21) {	//PLANT_SEQ_NO
						value = p_seq_no.split("-")[0];
					}
					
					else if(i == 28) {	//DAY_DEF_FLAG
						queryString2 = "SELECT DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,FCC_BY,TO_CHAR(FCC_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM DLNG_SN_MST WHERE CUSTOMER_CD = ? AND SN_NO = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? ORDER BY SN_REV_NO ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd); 
						stmt2.setString(2, cont_no); 
//						stmt2.setString(3, cont_rev); 
						stmt2.setString(3, no); 
						stmt2.setString(4, rev); 
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							day_def = rset2.getString(1);
							day_st = rset2.getString(2);
							day_et = rset2.getString(3);
							mmcq = rset2.getString(4);
							mmcq_per = rset2.getString(5);
							fcc_by = rset2.getString(6);
							fcc_dt = rset2.getString(7);
						}
						rset2.close();
						stmt2.close();
						
						value = day_def;
					}
					
					else if(i == 30) {	//DAY_START_TIME
						value = day_st;
					}
					
					else if(i == 31) {	//DAY_END_TIME
						value = day_et;
					}
					
					else if(i == 32) {	//MMCQ_FLAG
						value = mmcq;
					}
					
					else if(i == 34) {	//MMCQ_PERCENTAGE
						value = mmcq_per;
					}
					
					else if(i == 36) {	//FCC_BY
						value = fcc_by;
					}
					
					else if(i == 37) {	//FCC_DATE
						value = fcc_dt;
					}
					
					else if(i == 40) {	//BILLING_FLAG
						queryString2 = "SELECT CLAUSE_CD FROM DLNG_FLSA_CLAUSE_MST WHERE BUYER_CD = ? AND FLSA_NO = ? AND REV_NO = ? AND CLAUSE_CD = '6' ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd); 
						stmt2.setString(2, no); 
						stmt2.setString(3, rev); 
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							value = "Y";
						}
						else {
							value = "N";
						}
						rset2.close();
						stmt2.close();
					}
					 
					else if(i == 42) {	//TRANSPORT_MGMT_CHARGE
						queryString2 = "SELECT NEW_INV_SEQ_NO, HLPL_INV_SEQ_NO, FINANCIAL_YEAR, TO_CHAR(INVOICE_DT,'DD/MM/YYYY') FROM DLNG_INVOICE_MST WHERE "
								+ "CUSTOMER_CD = ? AND FGSA_NO = ? AND FGSA_REV_NO = ? AND SN_NO = ? AND PLANT_SEQ_NO = ? AND CONTRACT_TYPE = 'V' ORDER BY PERIOD_START_DT ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						stmt2.setString(2, no);
						stmt2.setString(3, rev);
						stmt2.setString(4, cont_no);
						stmt2.setString(5, plant);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							queryString3 = "SELECT AMOUNT FROM DLNG_SERVICE_INVOICE_ATTACH WHERE SERVICE_INVOICE_NO = ? AND SERVICE_INV_DT = TO_DATE(?,'DD/MM/YYYY') ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, rset2.getString(1));
							stmt3.setString(2, rset2.getString(4));
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								value = rset3.getString(1);
							}
							else {
								value = "0";
							}
							rset3.close();
							stmt3.close();
						}
						else {
							value = null;
						}
						rset2.close();
						stmt2.close();
					}
					
					else if(i == 43) {	//TRANSPORT_MGMT_UNIT
						value = "3";
					}
					
					else if(i == 44) {	//EFF_DT
						value = st_dt;
					}
					
					else if(i == 45) {	//QTY_OPTION
						queryString2 = "SELECT TRUCK_FIRM, TRUCK_RE, QTY_FIRM, QTY_RE FROM DLNG_SALES_TRANSPORTER_MST WHERE CUSTOMER_CD = ? AND CONTRACT_NO = ? AND CONTRACT_REV_NO = ? AND AGREEMENT_NO = ? AND AGREEMENT_REV_NO = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd); 
						stmt2.setString(2, cont_no); 
						stmt2.setString(3, cont_rev); 
						stmt2.setString(4, no); 
						stmt2.setString(5, rev); 
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							truck_firm = rset2.getString(1);
							truck_re = rset2.getString(2);
							qty_firm = rset2.getString(3);
							qty_re = rset2.getString(4);
						}
						rset2.close();
						stmt2.close();
						
						if(truck_firm!=null || truck_re!=null) {
							value = "1";
						}
						else {
							value = "2";
						}
					}
					
					else if(i == 46) {	//QTY_OPTION_FIRM
						if(truck_firm!=null || truck_re!=null) {
							value = truck_firm;
						}
						else {
							value = qty_firm;
						}
					}
					
					else if(i == 47) {	//QTY_OPTION_RE
						if(truck_firm!=null || truck_re!=null) {
							value = truck_re;
						}
						else {
							value = qty_re;
						}
					}
					
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
//				logger.data(fname, (company_cd + "," + counterparty + "," + type + "," + no + "," + rev + "," + name + "," + ref + "," + mmcq + "," ), conn, "");
				
				}
				
				rset1.close();
				stmt1.close();
			}
			
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_SVC_CONT_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SVC_CONT_MST>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_SVC_CONT_MST>>");
			System.out.println();
			
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
			
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_SVC_CONT_MAP() throws SQLException, IOException {
		
		function_nm = "FMS_SVC_CONT_MAP()";
		
		try {

			logger.checkpoint(fname, "\n<<START>>,<<FMS_SVC_CONT_MAP>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CUSTOMER_CD,SELL_CONT_MAP,ENT_BY,ENT_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			count = 0;
			String plant="",counterparty="",ref="",sn_ref="";
			
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			

//			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,TRANSPORTER_MAP,CONT_MAP,AGMT_TYPE,TIMESTAMP,", conn);
			
				queryString = "SELECT '2', A.CUSTOMER_CD, A.AGREEMENT_NO, A.AGREEMENT_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, 'B', NULL, NULL, A.USER_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
						+ "FROM DLNG_SALES_TRANSPORTER_MST A , DLNG_FLSA_MST B WHERE A.AGREEMENT_NO = B.FLSA_NO AND A.AGREEMENT_REV_NO = B.REV_NO "
						+ "AND A.CUSTOMER_CD = B.CUSTOMER_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) " ;
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, delta_FromDt);
				stmt.setString(2, delta_FromDt);
				stmt.setString(3, delta_ToDt);
				stmt.setString(4, delta_ToDt);
				rset = stmt.executeQuery();
				
				while (rset.next()) {
					
//					row = spreadsheet.createRow(nrow++);
					cd = rset.getString(2);
					no = rset.getString(3);
					rev = rset.getString(4);
					cont_no = rset.getString(5);
					cont_rev = rset.getString(6);
					plant="";counterparty="";ref="";sn_ref="";
					
					queryString1 = "SELECT PLANT_SEQ_NO FROM DLNG_SN_PLANT_MST WHERE FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CUSTOMER_CD = ?";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, no);
					stmt1.setString(2, rev);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, cont_rev);
					stmt1.setString(5, cd);
					rset1 = stmt1.executeQuery();
					while(rset1.next()) {
						plant = rset1.getString(1);
						row = spreadsheet.createRow(nrow++);
						value = "";	
						
					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
						
						if(i == 1) {	//COUNTERPARTY_CD
							queryString2 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, cd);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								value = rset2.getString(1);
								if(mpe.counterparty_map.containsKey(value)) {
									value = mpe.counterparty_map.get(value);
								}
								value = value.toUpperCase();
								counterparty = value;
							}
							rset2.close();
							stmt2.close();
							ref = counterparty+"-"+no+"-"+rev+"-"+plant+"-"+cont_no+"-"+cont_rev;
							value = ref;
						}
						
						else if(i == 7) {	//CONTRACT_TYPE
							value = counterparty;
						}
						
						else if(i == 8) {	//CUSTOMER_CD
							sn_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
							value = sn_ref;
						}
						
						
						
						 cell.setCellValue("'"+value+"'");
					}
					count++;
//					logger.data(fname, (abbr + "," + cd + "," + trans_map + "," + cont_map + ","  + agmt_type + ","), conn, "");
					}
					
					rset1.close();
					stmt1.close();
				}
				rset.close();
				stmt.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_SVC_CONT_MAP_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SVC_CONT_MAP>>,,,", conn);
			

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

	public void FMS_DLNG_BUYER_NOM() throws SQLException, IOException {
		int n=0;
		function_nm = "FMS_DLNG_BUYER_NOM(D)()";
		try {

			System.out.println("<<START>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_BUYER_NOM>>,,,,", conn);

			logger.checkpoint1(fname1, function_nm + "E" + "," + start_end_dt + ",", conn);

			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BASE,GCV,NCV,GAS_DT,GEN_DT,GEN_TIME,NOM_REV_NO,PLANT_SEQ,BU_SEQ,QTY_MMBTU,QTY_MT,"
					+ "QTY_SCM,ENT_BY,ENT_DT";

//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			String temp = "";
//			nrow = 0;
//			ncell = 0;
			count = 0;
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			String map = "",nom_id="";
			String qty_gcv="",qty_ncv="",qty_mmbtu="",qty_scm="",gas_dt="",base="";
			int nom_rev=0;
			String qty_mt = "51.5";
			NumberFormat nf = new DecimalFormat("###########0.00");
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_BUYER_NOM_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();

			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);

			logger.checkpoint(fname,"COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,GAS_DT,PLANT_SEQ,TIMESTAMP,", conn);

			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT "
					+ "FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();

			while (rset.next()) {
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;

					queryString2 = "SELECT PARTY_CD, NOM_ID, NULL, NULL, NULL, NULL, CONTRACT_TYPE, NULL, GHV, NHV, TO_CHAR(NOM_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ " TO_CHAR(GEN_DT, 'DD/MM/YYYY HH24:MI:SS'), TIME_ST_DAY, REV_NO, NULL, NULL, DAY_VOL, NULL, TOT_ENE, '1', TO_CHAR(NOM_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_DAILY_NOM "
							+ "WHERE PARTY_CD = ? AND MAPPING_ID = ? AND CONTRACT_TYPE = 'S' AND "
							+ "(? IS NULL OR NOM_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
							+ "(? IS NULL OR NOM_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();

					while (rset2.next()) {
						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						nom_id = rset2.getString(2);
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(9);
						qty_ncv = rset2.getString(10);
						gas_dt = rset2.getString(11);
						nom_rev = rset2.getInt(14);
						qty_mmbtu = rset2.getString(17);
						qty_scm = rset2.getString(19);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double baseVal=0d;
						double deviding_factor=1d;
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);

						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);

						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						

						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);

							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if (i == 7) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								base = value;
								str += value + ",";
							} 
							
							else if (i == 14) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if(i == 16) {	//QTY_MMBTU
								if(nom_rev>1) {
									queryString3 = "SELECT NVL(SUM(EXIT_TOT_ENE),0),NVL(MAX(NOM_REV_NO),0) FROM DLNG_ALLOC_MST "
											+ " WHERE GAS_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND ALLOC_ID = ? "
											+ " AND CONTRACT_TYPE = 'S' ";	
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, gas_dt);
									stmt3.setString(2, nom_id);
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
										qty_mmbtu = rset3.getString(1);
										value = qty_mmbtu;
									}
									else {
										value = qty_mmbtu;
									}
									rset3.close();
									stmt3.close();
								}
								else {
									value = qty_mmbtu;
								}
								str += value + ",";
							}
							
							else if (i == 17) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									mmbtu = Double.parseDouble(qty_mmbtu);
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 18) {	//QTY_SCM
								if(!qty_mmbtu.equals("0")) {
									mmbtu = Double.parseDouble(qty_mmbtu);
									if(base.equals("GCV")) {
										baseVal=gcv;
										deviding_factor=1;
									}
									else {
										baseVal=ncv;
										deviding_factor=1.11d;
									}
//									Double result1= (double) Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
									BigDecimal result1 = BigDecimal.valueOf(Math.round(mmbtu*multiplying_factor/(baseVal*deviding_factor)));
									result1  = result1.setScale(2, RoundingMode.CEILING);
//									Double result2 = result1.doubleValue();
									
									qty_scm = result1.toString();
//									value = value.format("%.2f", value);
									//cell.setCellValue("'" + value + "'");
//									str += value + ",";
								}
								else {
									qty_scm = "0";
								}
								value = qty_scm;
								
								str += value + ",";
							}
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();

		//LOA				

				queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT PARTY_CD, NOM_ID, NULL, NULL, NULL, NULL, CONTRACT_TYPE, NULL, GHV, NHV, TO_CHAR(NOM_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ " TO_CHAR(GEN_DT, 'DD/MM/YYYY HH24:MI:SS'), TIME_ST_DAY, REV_NO, NULL, NULL, DAY_VOL, NULL, TOT_ENE, '1', TO_CHAR(NOM_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_DAILY_NOM "
							+ "WHERE PARTY_CD = ? AND MAPPING_ID = ? AND CONTRACT_TYPE = 'L' AND "
							+ "(? IS NULL OR NOM_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
							+ "(? IS NULL OR NOM_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();

					while (rset2.next()) {
						map = "L-"+no+"-"+cont_no+"-"+cont_rev;
						nom_id = rset2.getString(2);
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(9);
						qty_ncv = rset2.getString(10);
						gas_dt = rset2.getString(11);
						nom_rev = rset2.getInt(14);
						qty_mmbtu = rset2.getString(17);
						qty_scm = rset2.getString(19);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double baseVal=0d;
						double deviding_factor=1d;
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);

						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);

						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";

						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);

							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if (i == 7) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								base = value;
								str += value + ",";
							} 

							else if (i == 14) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}

							else if(i == 16) {	//QTY_MMBTU
								if(nom_rev>1) {
									queryString3 = "SELECT NVL(SUM(EXIT_TOT_ENE),0),NVL(MAX(NOM_REV_NO),0) FROM DLNG_ALLOC_MST "
											+ " WHERE GAS_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND ALLOC_ID = ? "
											+ " AND CONTRACT_TYPE = 'L' ";	
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, gas_dt);
									stmt3.setString(2, nom_id);
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
										qty_mmbtu = rset3.getString(1);
										value = qty_mmbtu;
									}
									else {
										value = qty_mmbtu;
									}
									rset3.close();
									stmt3.close();
								}
								else {
									value = qty_mmbtu;
								}
								str += value + ",";
							}

							else if (i == 17) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									mmbtu = Double.parseDouble(qty_mmbtu);
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 18) {	//QTY_SCM
								if(!qty_mmbtu.equals("0")) {
									mmbtu = Double.parseDouble(qty_mmbtu);
									if(base.equals("GCV")) {
										baseVal=gcv;
										deviding_factor=1;
									}
									else {
										baseVal=ncv;
										deviding_factor=1.11d;
									}
//									Double result1= (double) Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
									BigDecimal result1 = BigDecimal.valueOf(Math.round(mmbtu*multiplying_factor/(baseVal*deviding_factor)));
									result1  = result1.setScale(2, RoundingMode.CEILING);
//									Double result2 = result1.doubleValue();
									
									qty_scm = result1.toString();
//									value = value.format("%.2f", value);
									//cell.setCellValue("'" + value + "'");
//									str += value + ",";
								}
								else {
									qty_scm = "0";
								}
								value = qty_scm;
								
								str += value + ",";
							}
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_" + start_end_dt + ".xlsx";

//			fileOut = new FileOutputStream(filename);

//			workbook.write(fileOut);
//			fileOut.close();

			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_BUYER_NOM>>,,,,", conn);

			logger.checkpoint1(fname1, count + ",", conn);

			System.out.println("<<END>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";

			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

	}

	public void FMS_DLNG_BUYER_NOM_DTL() throws SQLException, IOException {
		int n=0;
		function_nm = "FMS_DLNG_BUYER_NOM_DTL(D)()";
		try {
			
			System.out.println("<<START>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_BUYER_NOM_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1, function_nm + "E" + "," + start_end_dt + ",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,BU_SEQ,BASE,GCV,NCV,GAS_DT,GEN_DT,GEN_TIME,ARRIVAL_DT,ARRIVAL_TIME,"
					+ "NOM_REV_NO,TRUCK_TRANS_CD,TRUCK_CD,FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,QTY_MMBTU,QTY_MT,NEXT_AVAIL_HRS,REMARK,ENT_BY,ENT_DT";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			String temp = "";
//			nrow = 0;
//			ncell = 0;
			count = 0;
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			String map = "",mapping_id="",nom_id="",max_nom_rev="",truck_id="";
			String qty_gcv="",qty_ncv="",qty_mmbtu="",qty_scm="",gas_dt="",gen_dt="",gas_tm="",days="",trans_cd="",slot_st_tm="",slot_ed_tm="",arrival_dt="",arrival_tm="",nom_rev="",truck_nm="",sch_dt="",sch_tm="",sch_ed_tm="",gas_ed_tm="";
			String qty_mt = "51.5";
			Map<String, Integer> bay_cd = new HashMap<String, Integer>();
			int bay=1;
			NumberFormat nf = new DecimalFormat("###########0.00");
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_BUYER_NOM_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname,"COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,GAS_DT,PLANT_SEQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT "
					+ "FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				//SN
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.MAPPING_ID, A.NOM_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GHV, A.NHV, TO_CHAR(A.NOM_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ " TO_CHAR(A.GEN_DT, 'DD/MM/YYYY HH24:MI:SS'), A.TIME_ST_DAY, TO_CHAR(B.ARRIVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), B.ARRIVAL_TIME, A.REV_NO, B.TRANS_CD, "
							+ " B.TRUCK_NM, '1', '1', B.ARRIVAL_TIME, "
							+ " CASE "
								+ " WHEN B.ARRIVAL_TIME IS NOT NULL AND B.ARRIVAL_TIME LIKE '%:%' THEN "
								+ "TO_CHAR(TO_DATE(B.ARRIVAL_TIME, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI') "
								+ "ELSE '03S:00' "
							+ "END "
							+ "AS SLOT_END_TIME, "
							+ "B.TRUCK_VOL, B.TRUCK_ENE, B.NEXT_AVAIL_DAYS, "
							+ "B.REMARKS, '1', TO_CHAR(A.NOM_DT, 'DD/MM/YYYY HH24:MI:SS') , TO_CHAR(TO_DATE(A.TIME_ST_DAY, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI')"
							+ "FROM DLNG_DAILY_NOM A, DLNG_DAILY_TRUCK_NOM_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'S' AND "
							+ "A.MAPPING_ID = B.MAPPING_ID AND A.NOM_ID = B.NOM_ID AND A.REV_NO = B.REV_NO AND A.NOM_DT = B.NOM_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND"
							+ "(? IS NULL OR A.NOM_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
							+ "(? IS NULL OR A.NOM_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						mapping_id = rset2.getString(1);
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(14);
						gas_tm = rset2.getString(15);
						gas_ed_tm = rset2.getString(31);
						arrival_dt = rset2.getString(16);
						arrival_tm = rset2.getString(17);
						nom_rev = rset2.getString(18);
						trans_cd = rset2.getString(19);
						truck_nm = rset2.getString(20);
						slot_st_tm = rset2.getString(23);
						slot_ed_tm = rset2.getString(24);
						qty_mmbtu = rset2.getString(25);
						qty_scm = rset2.getString(26);
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 
							
							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							
							else if(i == 13) {	//GEN_DT
								value = gen_dt;
								
								str += value + ",";
							}
							
							else if(i == 15) {	//ARRIVAL_DT
								if(rset2.getString(16)==null) {
									queryString3 = "SELECT TO_CHAR(SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), SCH_TIME, TO_CHAR(TO_DATE(SCH_TIME, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI') FROM DLNG_DAILY_TRUCK_SCH_DTL WHERE MAPPING_ID = ? AND "
											+ "TRUCK_NM = ? AND TRANS_CD = ? AND TO_DATE(SCH_DT, 'DD/MM/YYYY HH24:MI:SS') = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND SCH_ID = ? AND CONTRACT_TYPE = 'S' ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, mapping_id);
									stmt3.setString(2, truck_nm);
									stmt3.setString(3, trans_cd);
									stmt3.setString(4, gas_dt);
									stmt3.setString(5, rset2.getString(2));
									
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
										sch_dt = rset3.getString(1);
										sch_tm = rset3.getString(2);
										sch_ed_tm = rset3.getString(3);
									}
									rset3.close();
									stmt3.close();
									
									if(sch_dt!=null && !sch_dt.equals("")) {
									value = sch_dt;
									}
									else {
										value = gas_dt;
									}
								}
								else {
									value = rset2.getString(16);
								}
								str += value + ",";
							}
							
							
							else if(i == 16) {	//ARRIVAL_TIME
								if(rset2.getString(17)==null) {
									if(sch_tm!=null && !sch_tm.equals("")) {
									value = sch_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else {
									value = rset2.getString(17);
								}
								str += value + ",";
							}
							
							else if(i == 18) {	//TRUCK_TRANS_CD
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 19) {	//TRUCK_CD
								if(truck_nm.equals("GJ12BX4077")) {
									truck_nm = "GJ12-BX4077";
								}
								
								truck_nm = truck_nm.replaceAll(" ", "");
								
								queryString3 = "SELECT TRUCK_ID FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_NM = ? AND TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, truck_nm);
								stmt3.setString(2, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								else {
									value = "0";
								}
								rset3.close();
								stmt3.close();
//								value = trans_cd ;
								
								str += value + ",";
							}
							
//							else if(i == 21) {	//BAY_CD
//
//								if(arrival_dt == null) {
//									bay = 0;
//								}
//								 else if (bay_cd.containsKey(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev)) {
//									bay = bay_cd.get(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev);
//									bay++ ;
//									bay_cd.put(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev,bay);
//								} 
//								else {
//									bay = 1;
//									bay_cd.put(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev,bay);
//								}
//					    		value = bay+"";
////					    		System.out.println(">>>>>"+gas_dt + "_____" + slot_st_tm +"----"+bay);
//					    		str += value + ",";
//							}
							
							else if(i == 22) {	//SLOT_START_TIME
								if(slot_st_tm==null) {
									if (sch_tm!=null  && !sch_tm.equals("")) {
										value = sch_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else if(!slot_st_tm.contains(":")) {
									value ="00:00";
								}
								else {
									value = slot_st_tm;
								}
//								value = value.replaceAll(";", ":");
								str += value + ",";
							}
							
							
							else if(i == 23) {	//SLOT_END_TIME
								if(slot_st_tm==null) {
									if (sch_ed_tm!=null  && !sch_ed_tm.equals("")) {
										value = sch_ed_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else if(!slot_st_tm.contains(":")) {
									value ="03:00";
								}
								else {
									value = slot_ed_tm;
								}
								str += value + ",";
							}
							
							else if (i == 25) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									mmbtu = Double.parseDouble(qty_mmbtu);
									
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								
								str += value + ","; 
							}
							
							else if(i == 29) {	//ENT_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				//SN ADDITIONAL ENTRY FOR BUYER NOMINATION
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.MAPPING_ID, A.NOM_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GHV, A.NHV, TO_CHAR(A.NOM_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ " TO_CHAR(A.GEN_DT, 'DD/MM/YYYY HH24:MI:SS'), A.TIME_ST_DAY, TO_CHAR(B.ARRIVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), B.ARRIVAL_TIME, A.REV_NO, B.TRANS_CD, "
							+ " B.TRUCK_NM, '1', '1', B.ARRIVAL_TIME, "
							+ " CASE "
							+ " WHEN B.ARRIVAL_TIME IS NOT NULL AND B.ARRIVAL_TIME LIKE '%:%' THEN "
							+ "TO_CHAR(TO_DATE(B.ARRIVAL_TIME, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI') "
							+ "ELSE '03S:00' "
							+ "END "
							+ "AS SLOT_END_TIME, "
							+ "B.TRUCK_VOL, B.TRUCK_ENE, B.NEXT_AVAIL_DAYS, "
							+ "B.REMARKS, '1', TO_CHAR(A.NOM_DT, 'DD/MM/YYYY HH24:MI:SS') , TO_CHAR(TO_DATE(A.TIME_ST_DAY, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI')"
							+ "FROM DLNG_DAILY_NOM A, DLNG_DAILY_TRUCK_NOM_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'S' AND "
							+ "A.MAPPING_ID = B.MAPPING_ID AND A.NOM_ID = B.NOM_ID AND A.REV_NO = B.REV_NO AND A.NOM_DT = B.NOM_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND"
							+ "(? IS NULL OR A.NOM_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
							+ "(? IS NULL OR A.NOM_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						mapping_id = rset2.getString(1);
						nom_id = rset2.getString(2);
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(14);
						gas_tm = rset2.getString(15);
						gas_ed_tm = rset2.getString(31);
						arrival_dt = rset2.getString(16);
						arrival_tm = rset2.getString(17);
						nom_rev = rset2.getString(18);
						trans_cd = rset2.getString(19);
						truck_nm = rset2.getString(20);
						slot_st_tm = rset2.getString(23);
						slot_ed_tm = rset2.getString(24);
						qty_mmbtu = rset2.getString(25);
						qty_scm = rset2.getString(26);
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						//FOR TRUCK_CD
						if(truck_nm.equals("GJ12BX4077")) {
							truck_nm = "GJ12-BX4077";
						}
						
						truck_nm = truck_nm.replaceAll(" ", "");
						
						queryString3 = "SELECT TRUCK_ID FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_NM = ? AND TRANS_CD = ? ";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, truck_nm);
						stmt3.setString(2, trans_cd);
						rset3 = stmt3.executeQuery();
						if(rset3.next()) {
							truck_id = rset3.getString(1);
						}
						rset3.close();
						stmt3.close();
						
						//FOR MAX_REV OF CURRENT DATE
						queryString4 = "SELECT MAX(B.REV_NO)  "
								+ "FROM DLNG_DAILY_NOM A, DLNG_DAILY_TRUCK_NOM_DTL B "
								+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'S' AND B.NOM_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND "
								+ "B.NOM_ID = ? AND A.MAPPING_ID = B.MAPPING_ID AND A.NOM_ID = B.NOM_ID AND A.NOM_DT = B.NOM_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, mapping_id);
						stmt4.setString(3, gas_dt);
						stmt4.setString(4, nom_id);
						rset4 = stmt4.executeQuery();
						if(rset4.next()) {
							max_nom_rev = rset4.getString(1);
						}
						rset4.close();
						stmt4.close();
						
						queryString4 = "SELECT TOTAL_QTY  "
								+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND FGSA_NO = ? AND FGSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO =? AND PLANT_SEQ_NO = ? "
								+ "AND INVOICE_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND TRUCK_ID = ? AND CONTRACT_TYPE = 'S' ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, nom_id.split("-")[1]);
						stmt4.setString(3, nom_id.split("-")[2]);
						stmt4.setString(4, nom_id.split("-")[3]);
						stmt4.setString(5, nom_id.split("-")[4]);
						stmt4.setString(6, nom_id.split("-")[6]);
						stmt4.setString(7, arrival_dt);
						stmt4.setString(8, truck_id);
						rset4 = stmt4.executeQuery();
//						if(rset4.next()) {
//							max_nom_rev = rset4.getString(1);
//						}
						
						if(!nom_rev.equals(max_nom_rev) && rset4.next()) {
						
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 
							
							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							
							else if(i == 13) {	//GEN_DT
								value = gen_dt;
								
								str += value + ",";
							}
							
							else if(i == 15) {	//ARRIVAL_DT
								if(rset2.getString(16)==null) {
									queryString3 = "SELECT TO_CHAR(SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), SCH_TIME, TO_CHAR(TO_DATE(SCH_TIME, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI') FROM DLNG_DAILY_TRUCK_SCH_DTL WHERE MAPPING_ID = ? AND "
											+ "TRUCK_NM = ? AND TRANS_CD = ? AND TO_DATE(SCH_DT, 'DD/MM/YYYY HH24:MI:SS') = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND SCH_ID = ? AND CONTRACT_TYPE = 'S' ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, mapping_id);
									stmt3.setString(2, truck_nm);
									stmt3.setString(3, trans_cd);
									stmt3.setString(4, gas_dt);
									stmt3.setString(5, rset2.getString(2));
									
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
										sch_dt = rset3.getString(1);
										sch_tm = rset3.getString(2);
										sch_ed_tm = rset3.getString(3);
									}
									rset3.close();
									stmt3.close();
									
									if(sch_dt!=null && !sch_dt.equals("")) {
										value = sch_dt;
									}
									else {
										value = gas_dt;
									}
								}
								else {
									value = rset2.getString(16);
								}
								str += value + ",";
							}
							
							
							else if(i == 16) {	//ARRIVAL_TIME
								if(rset2.getString(17)==null) {
									if(sch_tm!=null && !sch_tm.equals("")) {
										value = sch_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else {
									value = rset2.getString(17);
								}
								str += value + ",";
							}
							
							else if(i == 17) {	//NOM_REV_NO
								value = max_nom_rev;
								
								str += value + ",";
							}
							
							else if(i == 18) {	//TRUCK_TRANS_CD
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 19) {	//TRUCK_CD
								
								value = truck_id ;
								
								str += value + ",";
							}
							
//							else if(i == 21) {	//BAY_CD
//
//								if(arrival_dt == null) {
//									bay = 0;
//								}
//								 else if (bay_cd.containsKey(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev)) {
//									bay = bay_cd.get(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev);
//									bay++ ;
//									bay_cd.put(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev,bay);
//								} 
//								else {
//									bay = 1;
//									bay_cd.put(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev,bay);
//								}
//					    		value = bay+"";
////					    		System.out.println(">>>>>"+gas_dt + "_____" + slot_st_tm +"----"+bay);
//					    		str += value + ",";
//							}
							
							else if(i == 22) {	//SLOT_START_TIME
								if(slot_st_tm==null) {
									if (sch_tm!=null  && !sch_tm.equals("")) {
										value = sch_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else if(!slot_st_tm.contains(":")) {
									value ="00:00";
								}
								else {
									value = slot_st_tm;
								}
								value = value.replaceAll(";", ":");
								str += value + ",";
							}
							
							
							else if(i == 23) {	//SLOT_END_TIME
								if(slot_st_tm==null) {
									if (sch_ed_tm!=null  && !sch_ed_tm.equals("")) {
										value = sch_ed_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else if(!slot_st_tm.contains(":")) {
									value ="03:00";
								}
								else {
									value = slot_ed_tm;
								}
								str += value + ",";
							}
							
							else if (i == 25) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									
									
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								
								str += value + ","; 
							}
							
							else if(i == 27) {	//REMARK
								value = "MAX REV RECORD";
								
								str += value + ",";
							}
							
							else if(i == 29) {	//ENT_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						count++;
						logger.insert_data(fname_csv, str, conn);
						
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
						rset4.close();
						stmt4.close();
					
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				//LOA				
				
				queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.MAPPING_ID, A.NOM_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GHV, A.NHV, TO_CHAR(A.NOM_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ " TO_CHAR(A.GEN_DT, 'DD/MM/YYYY HH24:MI:SS'), A.TIME_ST_DAY, TO_CHAR(B.ARRIVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), B.ARRIVAL_TIME, A.REV_NO, B.TRANS_CD, "
							+ " B.TRUCK_NM, '1', '1', B.ARRIVAL_TIME, "
							+ " CASE "
							+ " WHEN B.ARRIVAL_TIME LIKE '%:%' THEN "
							+ "TO_CHAR(TO_DATE(B.ARRIVAL_TIME, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI') "
							+ "ELSE '03S:00' "
							+ "END "
							+ "AS SLOT_END_TIME, "
							+ "B.TRUCK_VOL, B.TRUCK_ENE, "
							+ "B.NEXT_AVAIL_DAYS, B.REMARKS, '1', TO_CHAR(A.NOM_DT, 'DD/MM/YYYY HH24:MI:SS') , TO_CHAR(TO_DATE(A.TIME_ST_DAY, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI') "
							+ "FROM DLNG_DAILY_NOM A, DLNG_DAILY_TRUCK_NOM_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'L' AND "
							+ "A.MAPPING_ID = B.MAPPING_ID AND A.NOM_ID = B.NOM_ID AND A.REV_NO = B.REV_NO AND A.NOM_DT = B.NOM_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND"
							+ "(? IS NULL OR A.NOM_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
							+ "(? IS NULL OR A.NOM_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "L-"+no+"-"+cont_no+"-"+cont_rev;
						mapping_id = rset2.getString(1);
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(14);
						gas_tm = rset2.getString(15);
						gas_ed_tm = rset2.getString(31);
						arrival_dt = rset2.getString(16);
						arrival_tm = rset2.getString(17);
						nom_rev = rset2.getString(18);
						trans_cd = rset2.getString(19);
						truck_nm = rset2.getString(20);
						slot_st_tm = rset2.getString(23);
						slot_ed_tm = rset2.getString(24);
						qty_mmbtu = rset2.getString(25);
						qty_scm = rset2.getString(26);
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 

							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							
							else if(i == 13) {	//GEN_DT
								value = gen_dt;
								
								str += value + ",";
							}
							
							else if(i == 15) {	//ARRIVAL_DT
								if(rset2.getString(16)==null) {
									queryString3 = "SELECT TO_CHAR(SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), SCH_TIME, TO_CHAR(TO_DATE(SCH_TIME, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI') FROM DLNG_DAILY_TRUCK_SCH_DTL WHERE MAPPING_ID = ? AND "
											+ "TRUCK_NM = ? AND TRANS_CD = ? AND TO_DATE(SCH_DT, 'DD/MM/YYYY HH24:MI:SS') = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND SCH_ID = ? AND CONTRACT_TYPE = 'L' ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, mapping_id);
									stmt3.setString(2, truck_nm);
									stmt3.setString(3, trans_cd);
									stmt3.setString(4, gas_dt);
									stmt3.setString(5, rset2.getString(2));
									
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
										sch_dt = rset3.getString(1);
										sch_tm = rset3.getString(2);
										sch_ed_tm = rset3.getString(3);
									}
									rset3.close();
									stmt3.close();
									
									if(sch_dt!=null && !sch_dt.equals("")) {
									value = sch_dt;
									}
									else {
										value = gas_dt;
									}
								}
								else {
									value = rset2.getString(16);
								}
								str += value + ",";
							}
							
							
							else if(i == 16) {	//ARRIVAL_TIME
								if(rset2.getString(17)==null) {
									if(sch_tm!=null && !sch_tm.equals("")) {
									value = sch_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else {
									value = rset2.getString(17);
								}
								str += value + ",";
							}
							
							else if(i == 18) {	//TRUCK_TRANS_CD
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
//								value = trans_cd ;
								
								str += value + ",";
							}

							else if(i == 19) {	//TRUCK_CD
								if(truck_nm.equals("GJ12BX4077")) {
									truck_nm = "GJ12-BX4077";
								}
								
								truck_nm = truck_nm.replaceAll(" ", "");
								
								queryString3 = "SELECT TRUCK_ID FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_NM = ? AND TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, truck_nm);
								stmt3.setString(2, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 22) {	//SLOT_START_TIME
								if(slot_st_tm==null) {
									if (sch_tm!=null  && !sch_tm.equals("")) {
										value = sch_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else if(!slot_st_tm.contains(":")) {
									value ="00:00";
								}
								else {
									value = slot_st_tm;
								}
								value = value.replaceAll(";", ":");
								str += value + ",";
							}
							
							
							else if(i == 23) {	//SLOT_END_TIME
								if(slot_st_tm==null) {
									if (sch_ed_tm!=null  && !sch_ed_tm.equals("")) {
										value = sch_ed_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else if(!slot_st_tm.contains(":")) {
									value ="03:00";
								}
								else {
									value = slot_ed_tm;
								}
								str += value + ",";
							}
							
							else if (i == 25) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								
								str += value + ","; 
							}
							
							else if(i == 29) {	//ENT_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				
				//LOA ADDITIONAL ENTRY FOR BUYER NOMINATION
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.MAPPING_ID, A.NOM_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GHV, A.NHV, TO_CHAR(A.NOM_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ " TO_CHAR(A.GEN_DT, 'DD/MM/YYYY HH24:MI:SS'), A.TIME_ST_DAY, TO_CHAR(B.ARRIVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), B.ARRIVAL_TIME, A.REV_NO, B.TRANS_CD, "
							+ " B.TRUCK_NM, '1', '1', B.ARRIVAL_TIME, "
							+ " CASE "
							+ " WHEN B.ARRIVAL_TIME LIKE '%:%' THEN "
							+ "TO_CHAR(TO_DATE(B.ARRIVAL_TIME, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI') "
							+ "ELSE '03S:00' "
							+ "END "
							+ "AS SLOT_END_TIME, "
							+ "B.TRUCK_VOL, B.TRUCK_ENE, "
							+ "B.NEXT_AVAIL_DAYS, B.REMARKS, '1', TO_CHAR(A.NOM_DT, 'DD/MM/YYYY HH24:MI:SS') , TO_CHAR(TO_DATE(A.TIME_ST_DAY, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI') "
							+ "FROM DLNG_DAILY_NOM A, DLNG_DAILY_TRUCK_NOM_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'L' AND "
							+ "A.MAPPING_ID = B.MAPPING_ID AND A.NOM_ID = B.NOM_ID AND A.REV_NO = B.REV_NO AND A.NOM_DT = B.NOM_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND"
							+ "(? IS NULL OR A.NOM_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
							+ "(? IS NULL OR A.NOM_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "L-"+no+"-"+cont_no+"-"+cont_rev;
						mapping_id = rset2.getString(1);
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						nom_id = rset2.getString(2);
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(14);
						gas_tm = rset2.getString(15);
						gas_ed_tm = rset2.getString(31);
						arrival_dt = rset2.getString(16);
						arrival_tm = rset2.getString(17);
						nom_rev = rset2.getString(18);
						trans_cd = rset2.getString(19);
						truck_nm = rset2.getString(20);
						slot_st_tm = rset2.getString(23);
						slot_ed_tm = rset2.getString(24);
						qty_mmbtu = rset2.getString(25);
						qty_scm = rset2.getString(26);
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						//FOR TRUCK_CD
						if(truck_nm.equals("GJ12BX4077")) {
							truck_nm = "GJ12-BX4077";
						}
						
						truck_nm = truck_nm.replaceAll(" ", "");
						
						queryString3 = "SELECT TRUCK_ID FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_NM = ? AND TRANS_CD = ? ";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, truck_nm);
						stmt3.setString(2, trans_cd);
						rset3 = stmt3.executeQuery();
						if(rset3.next()) {
							truck_id = rset3.getString(1);
						}
						rset3.close();
						stmt3.close();
						
						//FOR MAX_REV OF CURRENT DATE
						queryString4 = "SELECT MAX(B.REV_NO)  "
								+ "FROM DLNG_DAILY_NOM A, DLNG_DAILY_TRUCK_NOM_DTL B "
								+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'L' AND B.NOM_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND "
								+ "B.NOM_ID = ? AND A.MAPPING_ID = B.MAPPING_ID AND A.NOM_ID = B.NOM_ID AND A.NOM_DT = B.NOM_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, mapping_id);
						stmt4.setString(3, gas_dt);
						stmt4.setString(4, nom_id);
						rset4 = stmt4.executeQuery();
						if(rset4.next()) {
							max_nom_rev = rset4.getString(1);
						}
						rset4.close();
						stmt4.close();
						
						queryString4 = "SELECT TOTAL_QTY  "
								+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND FGSA_NO = ? AND FGSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO =? AND PLANT_SEQ_NO = ? "
								+ "AND INVOICE_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND TRUCK_ID = ? AND CONTRACT_TYPE = 'L' ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, nom_id.split("-")[1]);
						stmt4.setString(3, nom_id.split("-")[2]);
						stmt4.setString(4, nom_id.split("-")[3]);
						stmt4.setString(5, nom_id.split("-")[4]);
						stmt4.setString(6, nom_id.split("-")[6]);
						stmt4.setString(7, arrival_dt);
						stmt4.setString(8, truck_id);
						rset4 = stmt4.executeQuery();
//						if(rset4.next()) {
//							max_nom_rev = rset4.getString(1);
//						}
						
						if(!nom_rev.equals(max_nom_rev) && rset4.next()) {
						
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 
							
							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							
							else if(i == 13) {	//GEN_DT
								value = gen_dt;
								
								str += value + ",";
							}
							
							else if(i == 15) {	//ARRIVAL_DT
								if(rset2.getString(16)==null) {
									queryString3 = "SELECT TO_CHAR(SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), SCH_TIME, TO_CHAR(TO_DATE(SCH_TIME, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI') FROM DLNG_DAILY_TRUCK_SCH_DTL WHERE MAPPING_ID = ? AND "
											+ "TRUCK_NM = ? AND TRANS_CD = ? AND TO_DATE(SCH_DT, 'DD/MM/YYYY HH24:MI:SS') = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND SCH_ID = ? AND CONTRACT_TYPE = 'L' ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, mapping_id);
									stmt3.setString(2, truck_nm);
									stmt3.setString(3, trans_cd);
									stmt3.setString(4, gas_dt);
									stmt3.setString(5, rset2.getString(2));
									
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
										sch_dt = rset3.getString(1);
										sch_tm = rset3.getString(2);
										sch_ed_tm = rset3.getString(3);
									}
									rset3.close();
									stmt3.close();
									
									if(sch_dt!=null && !sch_dt.equals("")) {
									value = sch_dt;
									}
									else {
										value = gas_dt;
									}
								}
								else {
									value = rset2.getString(16);
								}
								str += value + ",";
							}
							
							
							else if(i == 16) {	//ARRIVAL_TIME
								if(rset2.getString(17)==null) {
									if(sch_tm!=null && !sch_tm.equals("")) {
										value = sch_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else {
									value = rset2.getString(17);
								}
								str += value + ",";
							}
							
							else if(i == 17) {	//NOM_REV_NO
								value = max_nom_rev;
								
								str += value + ",";
							}
							
							else if(i == 18) {	//TRUCK_TRANS_CD
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 19) {	//TRUCK_CD
								
								value = truck_id ;
								
								str += value + ",";
							}
							
//							else if(i == 21) {	//BAY_CD
//
//								if(arrival_dt == null) {
//									bay = 0;
//								}
//								 else if (bay_cd.containsKey(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev)) {
//									bay = bay_cd.get(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev);
//									bay++ ;
//									bay_cd.put(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev,bay);
//								} 
//								else {
//									bay = 1;
//									bay_cd.put(cd +" "+ arrival_dt + " " + slot_st_tm +" "+ nom_rev,bay);
//								}
//					    		value = bay+"";
////					    		System.out.println(">>>>>"+gas_dt + "_____" + slot_st_tm +"----"+bay);
//					    		str += value + ",";
//							}
							
							else if(i == 22) {	//SLOT_START_TIME
								if(slot_st_tm==null) {
									if (sch_tm!=null  && !sch_tm.equals("")) {
										value = sch_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else if(!slot_st_tm.contains(":")) {
									value ="00:00";
								}
								else {
									value = slot_st_tm;
								}
								value = value.replaceAll(";", ":");
								str += value + ",";
							}
							
							
							else if(i == 23) {	//SLOT_END_TIME
								if(slot_st_tm==null) {
									if (sch_ed_tm!=null  && !sch_ed_tm.equals("")) {
										value = sch_ed_tm;
									}
									else {
										value = gas_tm;
									}
								}
								else if(!slot_st_tm.contains(":")) {
									value ="03:00";
								}
								else {
									value = slot_ed_tm;
								}
								str += value + ",";
							}
							
							else if (i == 25) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									
									
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								
								str += value + ","; 
							}
							
							else if(i == 27) {	//REMARK
								value = "MAX REV RECORD";
								
								str += value + ",";
							}
							
							else if(i == 29) {	//ENT_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						count++;
						logger.insert_data(fname_csv, str, conn);
						
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
						rset4.close();
						stmt4.close();
					
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_" + start_end_dt + ".xlsx";
			
//			fileOut = new FileOutputStream(filename);
			
//			workbook.write(fileOut);
//			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_BUYER_NOM_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1, count + ",", conn);
			
			System.out.println("<<END>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			System.out.println();
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	
	public void FMS_DLNG_SELLER_NOM_DTL() throws SQLException, IOException {
		int n=0;
		function_nm = "FMS_DLNG_SELLER_NOM_DTL(D)()";
		try {
			
			System.out.println("<<START>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_SELLER_NOM_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1, function_nm + "E" + "," + start_end_dt + ",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,BU_SEQ,BASE,GCV,NCV,GAS_DT,GEN_DT,GEN_TIME,ARRIVAL_DT,ARRIVAL_TIME,"
					+ "NOM_REV_NO,TRUCK_TRANS_CD,TRUCK_CD,FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,QTY_MMBTU,QTY_MT,NEXT_AVAIL_HRS,REMARK,ENT_BY,ENT_DT";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			String temp = ""; 
//			nrow = 0;
//			ncell = 0;
			count = 0;
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			String map = "",max_sch_rev="",sch_id="",truck_id="",mapping_id="";
			String qty_gcv="",qty_ncv="",qty_mmbtu="",qty_scm="",gas_dt="",gen_dt="",gas_tm="",days="",trans_cd="",slot_st_tm="",slot_ed_tm="",arrival_dt="",arrival_tm="",sch_rev="",truck_nm="",sch_dt="",sch_tm="",sch_ed_tm="",gas_ed_tm="";
//			String qty_gcv="",qty_ncv="",qty_mmbtu="",qty_scm="",gas_dt="",days="",trans_cd="";
			String qty_mt = "51.5";
			NumberFormat nf = new DecimalFormat("###########0.00");
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_SELLER_NOM_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname,"COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,GAS_DT,PLANT_SEQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT "
					+ "FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				//FOR SN
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					
					queryString2 = "SELECT A.MAPPING_ID, A.SCH_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GHV, A.NHV, TO_CHAR(A.SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ " TO_CHAR(A.GEN_DT, 'DD/MM/YYYY HH24:MI:SS'), A.TIME_ST_DAY, TO_CHAR(B.SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), NVL(B.SCH_TIME,'N.A'), A.REV_NO, B.TRANS_CD, "
							+ "B.TRUCK_NM, '1', '1', "
							+ "NVL(B.SCH_TIME,'N.A'), TO_CHAR(TO_DATE(CASE WHEN B.SCH_TIME = 'N.A' OR B.SCH_TIME IS NULL OR B.SCH_TIME NOT LIKE '%:%' THEN '00:00' ELSE B.SCH_TIME END, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI'), "
							+ "B.TRUCK_VOL, B.TRUCK_ENE, B.NEXT_AVAIL_DAYS, B.REMARKS, '1', TO_CHAR(A.SCH_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_DAILY_SCH A, DLNG_DAILY_TRUCK_SCH_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'S' AND "
							+ "A.MAPPING_ID = B.MAPPING_ID AND A.SCH_ID = B.SCH_ID AND A.REV_NO = B.REV_NO AND A.SCH_DT = B.SCH_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
							+ "AND (? IS NULL OR A.SCH_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.SCH_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						sch_id = rset2.getString(2);
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(14);
						gas_tm = rset2.getString(15);
//						gas_ed_tm = rset2.getString(31);
						arrival_dt = rset2.getString(16);
						arrival_tm = rset2.getString(17);
						sch_rev = rset2.getString(18);
						trans_cd = rset2.getString(19);
						truck_nm = rset2.getString(20);
						slot_st_tm = rset2.getString(23);
						slot_ed_tm = rset2.getString(24);
						qty_mmbtu = rset2.getString(25);
						qty_scm = rset2.getString(26);
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 

							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							
							else if(i == 13) {	//GEN_DT
								value = gen_dt;
								  
								str += value + ",";
							}
							
							else if(i == 15) {	//ARRIVAL_DT
								
								value = arrival_dt;
								str += value + ",";
							}
							
							
							else if(i == 16) {	//ARRIVAL_TIME
								value = arrival_tm;
								str += value + ",";
							}
							
							else if(i == 18) {	//TRUCK_TRANS_CD
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
//								value = trans_cd ;
								
								str += value + ",";
							}

							else if(i == 19) {	//TRUCK_CD
								if(truck_nm.equals("GJ12BX4077")) {
									truck_nm = "GJ12-BX4077";
								}
								
								truck_nm = truck_nm.replaceAll(" ", "");
								
								queryString3 = "SELECT TRUCK_ID FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_NM = ? AND TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, truck_nm);
								stmt3.setString(2, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								else {
									value = "0";
								}
								rset3.close();
								stmt3.close();
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 22) {	//SLOT_START_TIME
								value = slot_st_tm;
								if(!slot_st_tm.contains(":") && !slot_st_tm.equals("N.A")) {
									value = "00:00";
								}
								str += value + ",";
							}
							
							
							else if(i == 23) {	//SLOT_END_TIME
								if(slot_st_tm != null && slot_st_tm.equals("N.A")) {
									value = "N.A";
								}
								else if(!slot_st_tm.contains(":")) {
									value = "03:00";
								}
								else {
								value = slot_ed_tm;
								}
								
								str += value + ",";
							}
							
							else if (i == 25) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								str += value + ","; 
							}

							else if(i == 29) {	//ENT_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				
				//SN ADDITIONAL ENTRY FOR SELLER NOMINATION
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					
					queryString2 = "SELECT A.MAPPING_ID, A.SCH_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GHV, A.NHV, TO_CHAR(A.SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ " TO_CHAR(A.GEN_DT, 'DD/MM/YYYY HH24:MI:SS'), A.TIME_ST_DAY, TO_CHAR(B.SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), NVL(B.SCH_TIME,'N.A'), A.REV_NO, B.TRANS_CD, "
							+ "B.TRUCK_NM, '1', '1', "
							+ "NVL(B.SCH_TIME,'N.A'), TO_CHAR(TO_DATE(CASE WHEN B.SCH_TIME = 'N.A' OR B.SCH_TIME IS NULL OR B.SCH_TIME NOT LIKE '%:%' THEN '00:00' ELSE B.SCH_TIME END, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI'), "
							+ "B.TRUCK_VOL, B.TRUCK_ENE, B.NEXT_AVAIL_DAYS, B.REMARKS, '1', TO_CHAR(A.SCH_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_DAILY_SCH A, DLNG_DAILY_TRUCK_SCH_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'S' AND "
							+ "A.MAPPING_ID = B.MAPPING_ID AND A.SCH_ID = B.SCH_ID AND A.REV_NO = B.REV_NO AND A.SCH_DT = B.SCH_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
							+ "AND (? IS NULL OR A.SCH_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.SCH_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						mapping_id = rset2.getString(1);
						sch_id = rset2.getString(2);
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(14);
						gas_tm = rset2.getString(15);
//						gas_ed_tm = rset2.getString(31);
						arrival_dt = rset2.getString(16);
						arrival_tm = rset2.getString(17);
						sch_rev = rset2.getString(18);
						trans_cd = rset2.getString(19);
						truck_nm = rset2.getString(20);
						slot_st_tm = rset2.getString(23);
						slot_ed_tm = rset2.getString(24);
						qty_mmbtu = rset2.getString(25);
						qty_scm = rset2.getString(26);
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						//FOR TRUCK
						if(truck_nm.equals("GJ12BX4077")) {
							truck_nm = "GJ12-BX4077";
						}
						truck_nm = truck_nm.replaceAll(" ", "");
						
						queryString3 = "SELECT TRUCK_ID FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_NM = ? AND TRANS_CD = ? ";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, truck_nm);
						stmt3.setString(2, trans_cd);
						rset3 = stmt3.executeQuery();
						if(rset3.next()) {
							truck_id = rset3.getString(1);
						}
						rset3.close();
						stmt3.close();
						
						//FOR MAX_REV OF CURRENT DATE
						queryString4 = "SELECT MAX(B.REV_NO)  "
								+ "FROM DLNG_DAILY_SCH A, DLNG_DAILY_TRUCK_SCH_DTL B "
								+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'S' AND B.SCH_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND "
								+ "B.SCH_ID = ? AND A.MAPPING_ID = B.MAPPING_ID AND A.SCH_ID = B.SCH_ID AND A.SCH_DT = B.SCH_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, mapping_id);
						stmt4.setString(3, gas_dt);
						stmt4.setString(4, sch_id);
						rset4 = stmt4.executeQuery();
						if(rset4.next()) {
							max_sch_rev = rset4.getString(1);
						}
						rset4.close();
						stmt4.close();
						
						queryString4 = "SELECT TOTAL_QTY  "
								+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND FGSA_NO = ? AND FGSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO =? AND PLANT_SEQ_NO = ? "
								+ "AND INVOICE_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND TRUCK_ID = ? AND CONTRACT_TYPE = 'S' ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, sch_id.split("-")[1]);
						stmt4.setString(3, sch_id.split("-")[2]);
						stmt4.setString(4, sch_id.split("-")[3]);
						stmt4.setString(5, sch_id.split("-")[4]);
						stmt4.setString(6, sch_id.split("-")[6]);
						stmt4.setString(7, arrival_dt);
						stmt4.setString(8, truck_id);
						rset4 = stmt4.executeQuery();
//						if(rset4.next()) {
//							max_nom_rev = rset4.getString(1);
//						}
						
						if(!sch_rev.equals(max_sch_rev) && rset4.next()) {
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 
							
							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							
							else if(i == 13) {	//GEN_DT
								value = gen_dt;
								
								str += value + ",";
							}
							
							else if(i == 15) {	//ARRIVAL_DT
								
								value = arrival_dt;
								str += value + ",";
							}
							
							
							else if(i == 16) {	//ARRIVAL_TIME
								value = arrival_tm;
								str += value + ",";
							}

							else if(i == 17) {	//NOM_REV_NO
								value = max_sch_rev;
								
								str += value + ",";
							}
							
							else if(i == 18) {	//TRUCK_TRANS_CD
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 19) {	//TRUCK_CD
								
								value = truck_id ;
								str += value + ",";
							}
							
							else if(i == 22) {	//SLOT_START_TIME
								value = slot_st_tm;
								if(!slot_st_tm.contains(":") && !slot_st_tm.equals("N.A")) {
									value = "00:00";
								}
								str += value + ",";
							}
							
							
							else if(i == 23) {	//SLOT_END_TIME
								if(slot_st_tm != null && slot_st_tm.equals("N.A")) {
									value = "N.A";
								}
								else if(!slot_st_tm.contains(":")) {
									value = "00:00";
								}
								else {
									value = slot_ed_tm;
								}
								
								str += value + ",";
							}
							
							else if (i == 25) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								str += value + ","; 
							}

							else if(i == 27) {	//REMARK
								value = "MAX REV RECORD";
								
								str += value + ",";
							}
							
							else if(i == 29) {	//ENT_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
						rset4.close();
						stmt4.close();
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				//LOA				
				
				queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.PARTY_CD, A.SCH_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GHV, A.NHV, TO_CHAR(A.SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ " TO_CHAR(A.GEN_DT, 'DD/MM/YYYY HH24:MI:SS'), A.TIME_ST_DAY, TO_CHAR(B.SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), NVL(B.SCH_TIME,'N.A'), A.REV_NO, B.TRANS_CD, "
							+ " B.TRUCK_NM, '1', '1', "
							+ "NVL(B.SCH_TIME,'N.A') , TO_CHAR(TO_DATE(CASE WHEN B.SCH_TIME = 'N.A' OR B.SCH_TIME IS NULL OR B.SCH_TIME NOT LIKE '%:%'  THEN '00:00' ELSE B.SCH_TIME END, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI'), "
							+ "B.TRUCK_VOL, B.TRUCK_ENE, B.NEXT_AVAIL_DAYS, B.REMARKS, '1', TO_CHAR(A.SCH_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_DAILY_SCH A, DLNG_DAILY_TRUCK_SCH_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'L' AND "
							+ "A.MAPPING_ID = B.MAPPING_ID AND A.SCH_ID = B.SCH_ID AND A.REV_NO = B.REV_NO AND A.SCH_DT = B.SCH_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND"
							+ "(? IS NULL OR A.SCH_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
							+ "(? IS NULL OR A.SCH_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "L-"+no+"-"+cont_no+"-"+cont_rev;
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(14);
						gas_tm = rset2.getString(15);
//						gas_ed_tm = rset2.getString(31);
						arrival_dt = rset2.getString(16);
						arrival_tm = rset2.getString(17);
						sch_rev = rset2.getString(18);
						trans_cd = rset2.getString(19);
						truck_nm = rset2.getString(20);
						slot_st_tm = rset2.getString(23);
						slot_ed_tm = rset2.getString(24);
						qty_mmbtu = rset2.getString(25);
						qty_scm = rset2.getString(26);
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " ");
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 

							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							
							else if(i == 13) {	//GEN_DT
								value = gen_dt;
								  
								str += value + ",";
							}
							
							else if(i == 15) {	//ARRIVAL_DT
								
								value = arrival_dt;
								str += value + ",";
							}
							
							
							else if(i == 16) {	//ARRIVAL_TIME
								value = arrival_tm;
								str += value + ",";
							}
							
							
							else if(i == 18) {	//TRUCK_TRANS_CD
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
//								value = trans_cd ;
								
								str += value + ",";
							}

							else if(i == 19) {	//TRUCK_CD
								if(truck_nm.equals("GJ12BX4077")) {
									truck_nm = "GJ12-BX4077";
								}
								
								truck_nm = truck_nm.replaceAll(" ", "");
								
								queryString3 = "SELECT TRUCK_ID FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_NM = ? AND TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, truck_nm);
								stmt3.setString(2, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 22) {	//SLOT_START_TIME
								value = slot_st_tm;
								if(!slot_st_tm.contains(":") && !slot_st_tm.equals("N.A")) {
									value = "00:00";
								}
								str += value + ",";
							}
							
							
							else if(i == 23) {	//SLOT_END_TIME
								if(slot_st_tm != null && slot_st_tm.equals("N.A")) {
									value = "N.A";
								}
								else if(!slot_st_tm.contains(":")) {
									value = "03:00";
								}
								else {
								value = slot_ed_tm;
								}
								str += value + ",";
							}
							
							else if (i == 25) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								str += value + ","; 
							}

							else if(i == 29) {	//ENT_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				//LOA ADDITIONAL ENTRY FOR SELLER NOMINATION			
				
				queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.PARTY_CD, A.SCH_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GHV, A.NHV, TO_CHAR(A.SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ " TO_CHAR(A.GEN_DT, 'DD/MM/YYYY HH24:MI:SS'), A.TIME_ST_DAY, TO_CHAR(B.SCH_DT, 'DD/MM/YYYY HH24:MI:SS'), NVL(B.SCH_TIME,'N.A'), A.REV_NO, B.TRANS_CD, "
							+ " B.TRUCK_NM, '1', '1', "
							+ "NVL(B.SCH_TIME,'N.A') , TO_CHAR(TO_DATE(CASE WHEN B.SCH_TIME = 'N.A' OR B.SCH_TIME IS NULL OR B.SCH_TIME NOT LIKE '%:%'  THEN '00:00' ELSE B.SCH_TIME END, 'HH24:MI') + INTERVAL '3' HOUR , 'HH24:MI'), "
							+ "B.TRUCK_VOL, B.TRUCK_ENE, B.NEXT_AVAIL_DAYS, B.REMARKS, '1', TO_CHAR(A.SCH_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_DAILY_SCH A, DLNG_DAILY_TRUCK_SCH_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'L' AND "
							+ "A.MAPPING_ID = B.MAPPING_ID AND A.SCH_ID = B.SCH_ID AND A.REV_NO = B.REV_NO AND A.SCH_DT = B.SCH_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND"
							+ "(? IS NULL OR A.SCH_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
							+ "(? IS NULL OR A.SCH_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "L-"+no+"-"+cont_no+"-"+cont_rev;
						mapping_id = cd+"-"+no+"-0-"+cont_no+"-"+cont_rev;
						sch_id = rset2.getString(2);
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(14);
						gas_tm = rset2.getString(15);
//						gas_ed_tm = rset2.getString(31);
						arrival_dt = rset2.getString(16);
						arrival_tm = rset2.getString(17);
						sch_rev = rset2.getString(18);
						trans_cd = rset2.getString(19);
						truck_nm = rset2.getString(20);
						slot_st_tm = rset2.getString(23);
						slot_ed_tm = rset2.getString(24);
						qty_mmbtu = rset2.getString(25);
						qty_scm = rset2.getString(26);
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						//FOR TRUCK
						if(truck_nm.equals("GJ12BX4077")) {
							truck_nm = "GJ12-BX4077";
						}
						truck_nm = truck_nm.replaceAll(" ", "");
						
						queryString3 = "SELECT TRUCK_ID FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_NM = ? AND TRANS_CD = ? ";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, truck_nm);
						stmt3.setString(2, trans_cd);
						rset3 = stmt3.executeQuery();
						if(rset3.next()) {
							truck_id = rset3.getString(1);
						}
						rset3.close();
						stmt3.close();
						
						//FOR MAX_REV OF CURRENT DATE
						queryString4 = "SELECT MAX(B.REV_NO)  "
								+ "FROM DLNG_DAILY_SCH A, DLNG_DAILY_TRUCK_SCH_DTL B "
								+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'L' AND B.SCH_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND "
								+ "B.SCH_ID = ? AND A.MAPPING_ID = B.MAPPING_ID AND A.SCH_ID = B.SCH_ID AND A.SCH_DT = B.SCH_DT AND A.CONTRACT_TYPE = B.CONTRACT_TYPE ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, mapping_id);
						stmt4.setString(3, gas_dt);
						stmt4.setString(4, sch_id);
						rset4 = stmt4.executeQuery();
						if(rset4.next()) {
							max_sch_rev = rset4.getString(1);
						}
						rset4.close();
						stmt4.close();
						
						queryString4 = "SELECT TOTAL_QTY  "
								+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND FGSA_NO = ? AND FGSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO =? AND PLANT_SEQ_NO = ? "
								+ "AND INVOICE_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND TRUCK_ID = ? AND CONTRACT_TYPE = 'L' ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, sch_id.split("-")[1]);
						stmt4.setString(3, sch_id.split("-")[2]);
						stmt4.setString(4, sch_id.split("-")[3]);
						stmt4.setString(5, sch_id.split("-")[4]);
						stmt4.setString(6, sch_id.split("-")[6]);
						stmt4.setString(7, arrival_dt);
						stmt4.setString(8, truck_id);
						rset4 = stmt4.executeQuery();
//						if(rset4.next()) {
//							max_nom_rev = rset4.getString(1);
//						}
						
						if(!sch_rev.equals(max_sch_rev) && rset4.next()) {
							
							
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " ");
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 
							
							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							
							else if(i == 13) {	//GEN_DT
								value = gen_dt;
								
								str += value + ",";
							}
							
							else if(i == 15) {	//ARRIVAL_DT
								
								value = arrival_dt;
								str += value + ",";
							}
							
							
							else if(i == 16) {	//ARRIVAL_TIME
								value = arrival_tm;
								str += value + ",";
							}

							else if(i == 17) {	//NOM_REV_NO
								value = max_sch_rev;
								
								str += value + ",";
							}
							
							
							else if(i == 18) {	//TRUCK_TRANS_CD
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 19) {	//TRUCK_CD
								if(truck_nm.equals("GJ12BX4077")) {
									truck_nm = "GJ12-BX4077";
								}
								
								truck_nm = truck_nm.replaceAll(" ", "");
								
								queryString3 = "SELECT TRUCK_ID FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_NM = ? AND TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, truck_nm);
								stmt3.setString(2, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 22) {	//SLOT_START_TIME
								value = slot_st_tm;
								if(!slot_st_tm.contains(":") && !slot_st_tm.equals("N.A")) {
									value = "00:00";
								}
								str += value + ",";
							}
							
							
							else if(i == 23) {	//SLOT_END_TIME
								if(slot_st_tm != null && slot_st_tm.equals("N.A")) {
									value = "N.A";
								}
								else if(!slot_st_tm.contains(":")) {
									value = "03:00";
								}
								else {
									value = slot_ed_tm;
								}
								str += value + ",";
							}
							
							else if (i == 25) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								str += value + ","; 
							}

							else if(i == 27) {	//REMARK
								value = "MAX REV RECORD";
								
								str += value + ",";
							}
							
							else if(i == 29) {	//ENT_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset4.close();
					stmt4.close();
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_" + start_end_dt + ".xlsx";
			
//			fileOut = new FileOutputStream(filename);
			
//			workbook.write(fileOut);
//			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_SELLER_NOM_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1, count + ",", conn);
			
			System.out.println("<<END>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			System.out.println();
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}

	
	public void FMS_DLNG_ALLOC_MST() throws SQLException, IOException {
		int n=0;
		function_nm = "FMS_DLNG_ALLOC_MST(D)()";
		try {
			
			System.out.println("<<START>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_ALLOC_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1, function_nm + "E" + "," + start_end_dt + ",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,BU_SEQ,BASE,GCV,NCV,GAS_DT,FILL_STATION_CD,BAY_CD,SLOT_START_TIME,"
					+ "SLOT_END_TIME,GEN_DT,GEN_TIME,NOM_REV_NO,TRUCK_TRANS_CD,TRUCK_CD,LOAD_START_DT,LOAD_START_TIME,LOAD_END_DT,LOAD_END_TIME,NEXT_AVAIL_HRS,QTY_MMBTU,"
					+ "QTY_MT,GCV_MMBTU,ENT_BY,ENT_DT";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			String temp = ""; 
//			nrow = 0;
//			ncell = 0;
			count = 0;
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			String map = "",mapping_id="",alloc_id="",truck_id="",max_alloc_rev="";
			String qty_gcv="",qty_ncv="",qty_mmbtu="",qty_scm="",gcv_mmbtu="",gas_dt="",gen_dt="",gen_tm="",gas_tm="",days="",trans_cd="",alloc_rev="",truck_cd="",load_st_dt="",load_ed_dt="",ent_dt="";
//			String qty_gcv="",qty_ncv="",qty_mmbtu="",qty_scm="",gas_dt="",days="",trans_cd="";
			String qty_mt = "51.5";
			NumberFormat nf = new DecimalFormat("###########0.00");
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_ALLOC_MST_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname,"COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,GAS_DT,PLANT_SEQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT "
					+ "FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				//SN
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.MAPPING_ID, A.ALLOC_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GCV, A.NCV, TO_CHAR(A.GAS_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "'1', '1', NULL, NULL, TO_CHAR(A.GAS_DT + 1 , 'DD/MM/YYYY HH24:MI:SS') , '00:00', A.NOM_REV_NO, B.TRANS_CD, "
							+ "B.TRN_CD,  TO_CHAR(B.LOAD_START_TM, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(LOAD_START_TM,'HH24:MI'), "
							+ "TO_CHAR(B.LOAD_END_TM, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(LOAD_END_TM,'HH24:MI'), "
							+ "NVL(A.NEXT_AVAIL_DAYS,'0'), B.LOADED_ENE, NVL(B.LOADED_SCM,'0'), B.GCV_PER_MMBTU, B.ENT_CD,  TO_CHAR(B.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_ALLOC_MST A, DLNG_TANK_VOL_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'S' AND "
							+ "B.SCH_ID LIKE ? AND "
//							+ "A.ALLOC_ID = B.SCH_ID AND "
							+ "A.GAS_DT = B.EFF_DT AND A.SUP_TRN_CD = B.TRN_CD AND A.TRANS_CD = B.TRANS_CD "
							+ "AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-%");
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						map_id = rset2.getString(2);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(18);
						gen_tm = rset2.getString(19);
						alloc_rev = rset2.getString(20);
						trans_cd = rset2.getString(21);
						truck_cd = rset2.getString(22);
						load_st_dt = rset2.getString(23);
						load_ed_dt = rset2.getString(25);
						qty_mmbtu = rset2.getString(28);
						qty_scm = rset2.getString(29);
						gcv_mmbtu = rset2.getString(30);
						ent_dt = rset2.getString(32);
						
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						double gcv_qty = Double.parseDouble(gcv_mmbtu);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if(i == 2) {
								str +=  map_id + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 
							
							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							else if(i == 15) {	//SLOT_START_TIME
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									trans_cd = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								value = trans_cd + "-" + truck_cd;
								str += value + ",";
							}
							
							else if(i == 17) {	//GEN_DT
								value = gen_dt;
								str += value + ",";
							}
							
							else if(i == 20) {	//TRUCK_TRANS_CD
								value = trans_cd ;
								str += value + ",";
							}
							
							else if(i == 21) {	//TRUCK_CD
								value = truck_cd;
								str += value + ",";
							}
							
							else if(i == 22) {	//LOAD_START_DT
								value = load_st_dt;
								str += value + ",";
							}
							
							
							else if(i == 24) {	//LOAD_END_DT
								value = load_ed_dt;
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								str += value + ","; 
							}

							else if (i == 28) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									if(!gcv_mmbtu.equals("0")) {
									mt = mmbtu / gcv_qty;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
									}
									else {
										mt = mmbtu / mt;
										qty_mt = nf.format(mt);
									}
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 31) {	//ENT_DT
								value = ent_dt;
								
								str += value + ",";
							}
							
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				
				//SN ADDITIONAL ENTRY FOR ALLOC
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.MAPPING_ID, A.ALLOC_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GCV, A.NCV, TO_CHAR(A.GAS_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "'1', '1', NULL, NULL, TO_CHAR(A.GAS_DT + 1 , 'DD/MM/YYYY HH24:MI:SS') , '00:00', A.NOM_REV_NO, B.TRANS_CD, "
							+ "B.TRN_CD,  TO_CHAR(B.LOAD_START_TM, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(LOAD_START_TM,'HH24:MI'), "
							+ "TO_CHAR(B.LOAD_END_TM, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(LOAD_END_TM,'HH24:MI'), "
							+ "NVL(A.NEXT_AVAIL_DAYS,'0'), B.LOADED_ENE, NVL(B.LOADED_SCM,'0'), B.GCV_PER_MMBTU, B.ENT_CD,  TO_CHAR(B.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_ALLOC_MST A, DLNG_TANK_VOL_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'S' AND "
							+ "A.ALLOC_ID = B.SCH_ID AND A.GAS_DT = B.EFF_DT AND A.SUP_TRN_CD = B.TRN_CD AND A.TRANS_CD = B.TRANS_CD "
							+ "AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						mapping_id = rset2.getString(1);
						alloc_id = rset2.getString(2);
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						map_id = rset2.getString(2);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(18);
						gen_tm = rset2.getString(19);
						alloc_rev = rset2.getString(20);
						trans_cd = rset2.getString(21);
						truck_cd = rset2.getString(22);
						load_st_dt = rset2.getString(23);
						load_ed_dt = rset2.getString(25);
						qty_mmbtu = rset2.getString(28);
						qty_scm = rset2.getString(29);
						gcv_mmbtu = rset2.getString(30);
						ent_dt = rset2.getString(32);
						
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						double gcv_qty = Double.parseDouble(gcv_mmbtu);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						//FOR MAX_REV OF CURRENT DATE
						queryString4 = "SELECT MAX(A.NOM_REV_NO)  "
								+ "FROM DLNG_ALLOC_MST A, DLNG_TANK_VOL_DTL B "
								+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'S' AND B.EFF_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND "
								+ "B.SCH_ID = ? AND A.ALLOC_ID = B.SCH_ID AND A.ALLOC_DT = B.EFF_DT ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, mapping_id);
						stmt4.setString(3, gas_dt);
						stmt4.setString(4, alloc_id);
						rset4 = stmt4.executeQuery();
						if(rset4.next()) {
							max_alloc_rev = rset4.getString(1);
						}
						rset4.close();
						stmt4.close();
						
						
						queryString4 = "SELECT TOTAL_QTY  "
								+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND FGSA_NO = ? AND FGSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO =? AND PLANT_SEQ_NO = ? "
								+ "AND PERIOD_START_DT <= TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND PERIOD_END_DT >= TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') "
								+ "AND TRUCK_ID = ? AND CONTRACT_TYPE = 'S' ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, alloc_id.split("-")[1]);
						stmt4.setString(3, alloc_id.split("-")[2]);
						stmt4.setString(4, alloc_id.split("-")[3]);
						stmt4.setString(5, alloc_id.split("-")[4]);
						stmt4.setString(6, alloc_id.split("-")[6]);
						stmt4.setString(7, gas_dt);
						stmt4.setString(8, gas_dt);
						stmt4.setString(9, truck_cd);
						rset4 = stmt4.executeQuery();
//						if(rset4.next()) {
//							max_nom_rev = rset4.getString(1);
//						}
						
						
						if(!alloc_rev.equals(max_alloc_rev) && rset4.next()) {
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if(i == 2) {
								str +=  map_id + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 
							
							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							else if(i == 15) {	//SLOT_START_TIME
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									trans_cd = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								value = trans_cd + "-" + truck_cd;
								str += value + ",";
							}
							
							else if(i == 17) {	//GEN_DT
								value = gen_dt;
								str += value + ",";
							}
							
							else if(i == 19) {	//NOM_REV_NO
								value = max_alloc_rev;
								str += value + ",";
							}
							
							else if(i == 20) {	//TRUCK_TRANS_CD
								value = trans_cd ;
								str += value + ",";
							}
							
							else if(i == 21) {	//TRUCK_CD
								value = truck_cd;
								str += value + ",";
							}
							
							else if(i == 22) {	//LOAD_START_DT
								value = load_st_dt;
								str += value + ",";
							}
							
							
							else if(i == 24) {	//LOAD_END_DT
								value = load_ed_dt;
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								str += value + ","; 
							}
							
							else if (i == 28) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									if(!gcv_mmbtu.equals("0")) {
										mt = mmbtu / gcv_qty;
//									qty_mt = mt+"";
										qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
									}
									else {
										mt = mmbtu / mt;
										qty_mt = nf.format(mt);
									}
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 31) {	//ENT_DT
								value = ent_dt;
								
								str += value + ",";
							}
							
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset4.close();
					stmt4.close();
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				
				//LOA				
				
				queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.MAPPING_ID, A.ALLOC_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GCV, A.NCV, TO_CHAR(A.GAS_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "'1', '1', NULL, NULL, TO_CHAR(A.GAS_DT + 1 , 'DD/MM/YYYY HH24:MI:SS') , '00:00', A.NOM_REV_NO, B.TRANS_CD, "
							+ "B.TRN_CD,  TO_CHAR(B.LOAD_START_TM, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(LOAD_START_TM,'HH24:MI'), "
							+ "TO_CHAR(B.LOAD_END_TM, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(LOAD_END_TM,'HH24:MI'), "
							+ "NVL(A.NEXT_AVAIL_DAYS,'0'), B.LOADED_ENE, NVL(B.LOADED_SCM,'0'), B.GCV_PER_MMBTU, B.ENT_CD,  TO_CHAR(B.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_ALLOC_MST A, DLNG_TANK_VOL_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'L' AND "
							+ "A.ALLOC_ID = B.SCH_ID AND A.GAS_DT = B.EFF_DT AND A.SUP_TRN_CD = B.TRN_CD AND A.TRANS_CD = B.TRANS_CD "
							+ "AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "L-"+no+"-"+cont_no+"-"+cont_rev;
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						map_id = rset2.getString(2);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(18);
						gen_tm = rset2.getString(19);
						alloc_rev = rset2.getString(20);
						trans_cd = rset2.getString(21);
						truck_cd = rset2.getString(22);
						load_st_dt = rset2.getString(23);
						load_ed_dt = rset2.getString(25);
						qty_mmbtu = rset2.getString(28);
						qty_scm = rset2.getString(29);
						gcv_mmbtu = rset2.getString(30);
						ent_dt = rset2.getString(32);
						
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if(i == 2) {
								str +=  map_id + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 

							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							else if(i == 15) {	//SLOT_START_TIME
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									trans_cd = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								value = trans_cd + "-" + truck_cd;
								str += value + ",";
							}
							
							else if(i == 17) {	//GEN_DT
								value = gen_dt;
								str += value + ",";
							}
							
							else if(i == 20) {	//TRUCK_TRANS_CD
								value = trans_cd ;
								str += value + ",";
							}
							
							else if(i == 21) {	//TRUCK_CD
								value = truck_cd;
								str += value + ",";
							}
							
							else if(i == 22) {	//LOAD_START_DT
								value = load_st_dt;
								str += value + ",";
							}
							
							
							else if(i == 24) {	//LOAD_END_DT
								value = load_ed_dt;
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								str += value + ","; 
							}

							else if (i == 28) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 31) {	//ENT_DT
								value = ent_dt;
								
								str += value + ",";
							}
							
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				
				//LOA ADDITIONAL ENTRY FOR ALLOC 
				
				queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.MAPPING_ID, A.ALLOC_ID, NULL, NULL, NULL, NULL, A.CONTRACT_TYPE, NULL, NULL, NULL, A.GCV, A.NCV, TO_CHAR(A.GAS_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "'1', '1', NULL, NULL, TO_CHAR(A.GAS_DT + 1 , 'DD/MM/YYYY HH24:MI:SS') , '00:00', A.NOM_REV_NO, B.TRANS_CD, "
							+ "B.TRN_CD,  TO_CHAR(B.LOAD_START_TM, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(LOAD_START_TM,'HH24:MI'), "
							+ "TO_CHAR(B.LOAD_END_TM, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(LOAD_END_TM,'HH24:MI'), "
							+ "NVL(A.NEXT_AVAIL_DAYS,'0'), B.LOADED_ENE, NVL(B.LOADED_SCM,'0'), B.GCV_PER_MMBTU, B.ENT_CD,  TO_CHAR(B.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_ALLOC_MST A, DLNG_TANK_VOL_DTL B "
							+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'L' AND "
							+ "A.ALLOC_ID = B.SCH_ID AND A.GAS_DT = B.EFF_DT AND A.SUP_TRN_CD = B.TRN_CD AND A.TRANS_CD = B.TRANS_CD "
							+ "AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, map);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						map = "L-"+no+"-"+cont_no+"-"+cont_rev;
						mapping_id = rset2.getString(1);
						alloc_id = rset2.getString(2);
						p_seq_no = rset2.getString(2);
						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
//						p_seq_no = rset2.getString(7);
						map_id = rset2.getString(2);
						cont_type = rset2.getString(7);
						qty_gcv = rset2.getString(11);
						qty_ncv = rset2.getString(12);
						gas_dt = rset2.getString(13);
						gen_dt = rset2.getString(18);
						gen_tm = rset2.getString(19);
						alloc_rev = rset2.getString(20);
						trans_cd = rset2.getString(21);
						truck_cd = rset2.getString(22);
						load_st_dt = rset2.getString(23);
						load_ed_dt = rset2.getString(25);
						qty_mmbtu = rset2.getString(28);
						qty_scm = rset2.getString(29);
						gcv_mmbtu = rset2.getString(30);
						ent_dt = rset2.getString(32);
						
						
						days = rset2.getString(27) == null ? "0" : rset2.getString(27);
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						qty_mt = "51.5";
						double multiplying_factor = 0.252 * 1000000;
						double gcv = Double.parseDouble(qty_gcv);
						double ncv = Double.parseDouble(qty_ncv);
						double mmbtu = Double.parseDouble(qty_mmbtu);
						double scm = Double.parseDouble(qty_scm);
						double mt = Double.parseDouble(qty_mt);
						int hr = Integer.parseInt(days);
						
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						//FOR MAX_REV OF CURRENT DATE
						queryString4 = "SELECT MAX(A.NOM_REV_NO)  "
								+ "FROM DLNG_ALLOC_MST A, DLNG_TANK_VOL_DTL B "
								+ "WHERE A.PARTY_CD = ? AND A.MAPPING_ID = ? AND A.CONTRACT_TYPE = 'L' AND B.EFF_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND "
								+ "B.SCH_ID = ? AND A.ALLOC_ID = B.SCH_ID AND A.ALLOC_DT = B.EFF_DT ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, mapping_id);
						stmt4.setString(3, gas_dt);
						stmt4.setString(4, alloc_id);
						rset4 = stmt4.executeQuery();
						if(rset4.next()) {
							max_alloc_rev = rset4.getString(1);
						}
						rset4.close();
						stmt4.close();
						
						
						queryString4 = "SELECT TOTAL_QTY  "
								+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND FGSA_NO = ? AND FGSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO =? AND PLANT_SEQ_NO = ? "
								+ "AND PERIOD_START_DT <= TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND PERIOD_END_DT >= TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') "
								+ "AND TRUCK_ID = ? AND CONTRACT_TYPE = 'L' ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, cd);
						stmt4.setString(2, alloc_id.split("-")[1]);
						stmt4.setString(3, alloc_id.split("-")[2]);
						stmt4.setString(4, alloc_id.split("-")[3]);
						stmt4.setString(5, alloc_id.split("-")[4]);
						stmt4.setString(6, alloc_id.split("-")[6]);
						stmt4.setString(7, gas_dt);
						stmt4.setString(8, gas_dt);
						stmt4.setString(9, truck_cd);
						rset4 = stmt4.executeQuery();
//						if(rset4.next()) {
//							max_nom_rev = rset4.getString(1);
//						}
						
						
						if(!alloc_rev.equals(max_alloc_rev) && rset4.next())	{
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if(i == 2) {
								str +=  map_id + ",";
							}
							
							else if (i == 7) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if (i == 9) { // base
								if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / gcv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / gcv)) == -1) {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								} else if (Math.round(scm) == Math.round((mmbtu * multiplying_factor) / ncv)
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == 1
										|| (Math.round(scm)) - (Math.round((mmbtu * multiplying_factor) / ncv)) == -1) {
									value = "NCV";
//									cell.setCellValue("'" + value + "'");
								} else {
									value = "GCV";
//									cell.setCellValue("'" + value + "'");
								}
								str += value + ",";
							} 
							
							else if(i == 12) {	//GAS_DT
								value = gas_dt;
								
								str += value + ",";
							}
							
							else if(i == 15) {	//SLOT_START_TIME
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									trans_cd = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								value = trans_cd + "-" + truck_cd;
								str += value + ",";
							}
							
							else if(i == 17) {	//GEN_DT
								value = gen_dt;
								str += value + ",";
							}
							
							else if(i == 19) {	//NOM_REV_NO
								value = max_alloc_rev;
								str += value + ",";
							}
							
							else if(i == 20) {	//TRUCK_TRANS_CD
								value = trans_cd ;
								str += value + ",";
							}
							
							else if(i == 21) {	//TRUCK_CD
								value = truck_cd;
								str += value + ",";
							}
							
							else if(i == 22) {	//LOAD_START_DT
								value = load_st_dt;
								str += value + ",";
							}
							
							
							else if(i == 24) {	//LOAD_END_DT
								value = load_ed_dt;
								str += value + ",";
							}
							
							else if(i == 26) {	//NEXT_AVAIL_HRS
								hr = hr * 24;
								value = hr+"";
								
								str += value + ","; 
							}
							
							else if (i == 28) {	//QTY_MT
								if(!qty_mmbtu.equals("0")) {
									mt = mmbtu / mt;
//									qty_mt = mt+"";
									qty_mt = nf.format(mt);
//									mt = Double.parseDouble(qty_mt);
								}
								else {
									qty_mt = "0";
								}
								value = qty_mt+"";
								
								str += value + ",";
							}
							
							else if(i == 31) {	//ENT_DT
								value = ent_dt;
								
								str += value + ",";
							}
							
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset4.close();
					stmt4.close();					
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_" + start_end_dt + ".xlsx";
			
//			fileOut = new FileOutputStream(filename);
			
//			workbook.write(fileOut);
//			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_ALLOC_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1, count + ",", conn);
			
			System.out.println("<<END>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			System.out.println();
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	
	
	public void FMS_DLNG_INVOICE_MST() throws SQLException, IOException {
		int n=0;
		function_nm = "FMS_DLNG_INVOICE_MST()";
		try {
			
			System.out.println("<<START>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_INVOICE_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1, function_nm + "E" + "," + start_end_dt + ",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRUCK_TRANS_CD,TRUCK_CD,MAPPING_ID,BU_UNIT,BU_STATE_TIN,"
					+ "BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INV_FLAG,INVOICE_SEQ,INVOICE_ID_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,"
					+ "DUE_DT,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,EXCHG_RATE_TYPE,INVOICE_RAISED_IN,GROSS_AMT,"
					+ "TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT_1,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,REMARK_1,REMARK_2,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,"
					+ "AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TCS_TDS,TCS_AMT,TCS_FACTOR,PAY_RECV_AMT,"
					+ "PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,"
					+ "PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,"
					+ "TCS_STRUCT_CD,TCS_EFF_DT_1,TDS_STRUCT_CD,TDS_EFF_DT_1,TAX_EFF_DT,TCS_EFF_DT,TDS_EFF_DT,CHKPOST_CD,DRIVER_CD,FIN_SYS,HOLD_AMT";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			String temp = ""; 
//			nrow = 0;
//			ncell = 0;
			count = 0;
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			String map = "";
			String truck_cd="",trans_cd="",rate_unit="",fin_year="",period_st="",period_end="",p_contact="",alloc_qty="",rate="",inv_dt="",tax_dtl="",exch_cd="",tax_cd="",lic="",inv_no="";
			String exch_dt="",due_dt="",chk_dt="",auth_dt="",appr_dt="",ent_dt="",pay_recv_dt="",pay_insert_dt="",pay_update_dt="",print_dt_ori="",print_dt_tri="",print_dt_dup="",sap_appr_dt="";
			int bu_seq=0;
			NumberFormat nf = new DecimalFormat("###########0.00");
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_INVOICE_MST_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname,"COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,GAS_DT,PLANT_SEQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT "
					+ "FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO, A.RATE_UNIT "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					rate_unit = rset1.getString(6);
					map = "S-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.CUSTOMER_CD, A.FINANCIAL_YEAR, A.FGSA_NO, A.FGSA_REV_NO, A.SN_NO, A.SN_REV_NO, "
							+ "'F', NULL, A.TRUCK_ID, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.SUP_PLANT_CD, NULL, NULL, A.PLANT_SEQ_NO, A.CONTACT_PERSON_CD, 'F', "
							+ "A.HLPL_INV_SEQ_NO, A.HLPL_INV_SEQ_NO, A.NEW_INV_SEQ_NO, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, TO_CHAR(A.PERIOD_START_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "TO_CHAR(A.PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.TOTAL_QTY, A.SALE_PRICE, NULL, A.GROSS_AMT_USD, "
							+ "A.EXCHG_RATE_CD, TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EXCHG_RATE_VALUE, A.EXCHG_RATE_TYPE, NULL, A.GROSS_AMT_INR, "
							+ "A.TAX_AMT_INR, A.TAX_STRUCT_CD, NULL, A.NET_AMT_INR, A.NET_AMT_INR, NULL, A.REMARK_1, A.REMARK_2, A.CHECKED_FLAG, A.CHECKED_BY, "
							+ "TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.AUTHORIZED_FLAG, A.AUTHORIZED_BY, TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "A.APPROVED_FLAG, A.APPROVED_BY, TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, NULL, NULL, NULL, NULL, A.PAY_RECV_AMT, TO_CHAR(A.PAY_RECV_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_INSERT_BY, "
							+ "TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_UPDATE_BY, TO_CHAR(A.PAY_UPDATE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "A.PAY_REMARK, A.TDS_PERCENT, A.TDS_TAX_AMT, A.TDS_TAX_PERCENT, A.TDS_TAX_AMT, 'T', A.PRINT_BY_ORI, "
							+ "TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_TRI, TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_DUP, "
							+ "TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, A.SUN_APPROVAL, NULL "
							+ "FROM DLNG_INVOICE_MST A WHERE A.CONTRACT_TYPE ='S' AND A.CUSTOMER_CD = ? AND A.FGSA_NO = ? AND A.FGSA_REV_NO = ? AND A.SN_NO = ? AND "
							+ "A.SN_REV_NO = ? "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, no);
					stmt2.setString(3, rev);
					stmt2.setString(4, cont_no);
					stmt2.setString(5, cont_rev);
					stmt2.setString(6, delta_FromDt);
					stmt2.setString(7, delta_FromDt);
					stmt2.setString(8, delta_ToDt);
					stmt2.setString(9, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						inv_seq="";fin_yr="";
//						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						p_seq_no = rset2.getString(14);
//						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
						cd = rset2.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						truck_cd = rset2.getString(9);
						bu_seq = rset2.getInt(11);
						p_contact = rset2.getString(15);
						inv_no = rset2.getString(19);
						inv_dt = rset2.getString(20);
						period_st = rset2.getString(22);
						period_end = rset2.getString(23);
						due_dt = rset2.getString(24);
						alloc_qty = rset2.getString(25);
						rate = rset2.getString(26);
						exch_cd = rset2.getString(29);
						exch_dt = rset2.getString(30);
						BigDecimal gross_amt = rset2.getBigDecimal(34);
						tax_cd = rset2.getString(36);
						chk_dt = rset2.getString(45);
						auth_dt = rset2.getString(48);
						appr_dt = rset2.getString(51);
						ent_dt = rset2.getString(53);
						pay_recv_dt = rset2.getString(60);
						pay_insert_dt = rset2.getString(62);
						pay_update_dt = rset2.getString(64);
						BigDecimal tds_percent = rset2.getBigDecimal(66);
						print_dt_ori = rset2.getString(72);
						print_dt_tri = rset2.getString(74);
						print_dt_dup = rset2.getString(76);
						sap_appr_dt = rset2.getString(79);
						
						
						fin_yr = rset2.getString(2);
						inv_seq = rset2.getString(17);
						state_code = "0";
						
						double qty = Double.parseDouble(alloc_qty);
						double price = Double.parseDouble(rate);
						double sale_amt = 0;
						
						
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
//							else if(i == 2) {	//FINANCIAL_YEAR
//								queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
//									+" || '-' || "
//									+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
//									+ "FROM DUAL";
//							stmt3 = conn.prepareStatement(queryString3);
//							stmt3.setString(1, period_st);
//							stmt3.setString(2, period_end);
//							rset3 = stmt3.executeQuery();
//							if(rset3.next()) {
//								value = rset3.getString(1) == null ? "" :rset3.getString(1);
//							}
//							fin_year = value;
//							rset3.close();
//							stmt3.close();
//							
////							cell.setCellValue("'"+value+"'");
//							str += value + ",";
//							}
							
							else if(i == 7) {	//CONTRACT_TYPE
								value = cont_type;
								str += value + ",";
							}

							else if(i == 8) {	//TRUCK_TRANS_CD
								queryString3 = "SELECT TRANS_CD FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_ID = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, truck_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									trans_cd = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 9) {	//TRUCK_CD
								value = truck_cd;
								str += value + ",";
							}
							
							else if(i == 10) {	//MAPPING_ID
								value = inv_dt;
								str += value + ",";
							}
							
							else if(i == 11) {	//BU_UNIT
								value = (bu_seq+1)+"";
								str += value + ",";
							}
							
							else if (i == 14) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if(i == 15) {	//CONTACT_PERSON_CD
								queryString3 = "SELECT EMAIL FROM FMS7_CUSTOMER_CONTACT_MST WHERE CUSTOMER_CD = ? AND SEQ_NO = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, cd);
								stmt3.setString(2, p_contact);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								str += value + ",";
							}

							else if(i == 20) {	//INVOICE_DT
								value = inv_dt;
								str += value + ",";
							}

							else if(i == 22) {	//PERIOD_START_DT
								value = period_st;
								str += value + ",";
							}

							else if(i == 23) {	//PERIOD_END_DT
								value = period_end;
								str += value + ",";
							}
							
							
							else if(i == 24) {	//DUE_DT
								value = due_dt;
								str += value + ",";
							}
							
							else if(i == 27) {	//SALE_PRICE_UNIT
								value = rate_unit;
								str += value + ",";
							}
							
							else if(i == 29) {	//EXCHG_RATE_CD
								queryString3 = "SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, exch_cd);
								rset3 = stmt3.executeQuery();
								 if(rset3.next()) {	
									   name = rset3.getString(1);
									   name= name.toUpperCase();
									   if (name.contains("CUSTOMS RATE")) {
											name = "CUSTOM EXCHANGE RATE";
										}
										else if (name.contains("RBI REFERENCE")) {
											name = "RBI REFERENCE RATE";
										}
										else if (name.contains("SBI MUMBAI TT AVERAGE")) {
											name = "SBI MUMBAI TT BUY SELL";
										}
										else if (name.contains("SBI TT BUYING")) {
											name = "SBI RATE BUY";
										}
										else if (name.contains("SBI TT SELLING")) {
											name = "SBI RATE SELL";
										}
										else if (name.contains("SBI TT BUY SELL")) {
											name = "SBI RATE BUY SELL";
										}								
								   }
								rset3.close();
								stmt3.close();
								value = name;
								str += value + ",";
							}
							
							else if(i == 30) {	//EXCHG_RATE_DT
								value = exch_dt;
								str += value + ",";
							}
							
							else if(i == 36) {	//TAX_STRUCT_CD
								queryString4="SELECT REMARK FROM FMS7_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, tax_cd);
								rset4 = stmt4.executeQuery();
								   if(rset4.next()) {	
									   tax_dtl = rset4.getString(1);
									   tax_dtl = tax_dtl.replaceAll(",", "@ ");
									   tax_dtl = tax_dtl.replaceAll("\n", " ");
									   tax_dtl = tax_dtl.replaceAll("\r", " ");
									   tax_dtl = tax_dtl.replaceAll("\"", " ");
									   if (tax_dtl.contains("MH")) {
										   tax_dtl = tax_dtl.split(" MH")[0] + "%";
										}
										else if (tax_dtl.contains("AP")) {
											tax_dtl = tax_dtl.split(" AP")[0] + "%";
										}
										else if (tax_dtl.contains("ADD. VAT")) {
											tax_dtl = tax_dtl.replace("ADD. VAT", "ADVAT");
										}
										else if (tax_dtl.contains("STAX 0%")) {
											tax_dtl = tax_dtl.replace("STAX 0%", "ZSTAX 0%");
										}
								   }else {
									   tax_dtl = null; 
								   }							   								  
								   stmt4.close();
								   rset4.close();
								  
								   str += tax_dtl + ",";
							}
							
							else if(i == 41) {	//REMARK_1
								value = rset2.getString(41);
								if (value!=null) {
									value = value.replaceAll(",", " ");
								}
								str += value + ",";
							}
							
							else if(i == 42) {	//REMARK_2
								value = rset2.getString(42);
								if(value!=null) {
								value = value.replaceAll(",", " ");
								}
								str += value + ",";
							}
							
							else if(i == 45) {	//CHECKED_DT
								value = chk_dt;
								str += value + ",";
							}

							else if(i == 48) {	//AUTHORIZED_DT
								value = auth_dt;
								str += value + ",";
							}

							else if(i == 51) {	//APPROVED_DT
								value = appr_dt;
								str += value + ",";
							}
							
							else if(i == 53) {	//ENT_DT
								value = ent_dt;
								str += value + ",";
							}
							else if(i == 56) {	//TCS_TDS
								tcs_tds="";
								
								if(tds_percent!= null) {
									 tcs_tds = "TDS";	
								}
								else {

								queryString3="SELECT TCS_AMT,TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_INVOICE_TCS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG'";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, cd);
								stmt3.setString(2, inv_seq);
								stmt3.setString(3, fin_yr);
								stmt3.setString(4, "S");
								stmt3.setString(5, state_code);
								
								rset3 = stmt3.executeQuery();
								   if(rset3.next()) {
									   tcs_amt = rset3.getString(1);
									   tcs_date = rset3.getString(2);
									   tcs_tds = "TCS";								
								   }
								   else {
									   tcs_amt = "";
									   tcs_date = "";
									   tcs_tds = "NA";
								   }
//								   else {
//									   queryString4="SELECT TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), TDS_AMT FROM FMS7_INVOICE_TDS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG'";
//										stmt4 = conn.prepareStatement(queryString4);
//										stmt4.setString(1, cd);
//										stmt4.setString(2, inv_seq);
//										stmt4.setString(3, fin_yr);
//										stmt4.setString(4, "S");
//										stmt4.setString(5, state_code);
//										
//										rset4 = stmt4.executeQuery();
//										   if(rset4.next()) {
//											  tds_date = rset4.getString(1); 
//											  tds_amt = rset4.getString(2); 
//											  tcs_tds = "TDS";								
//										   }else {
//											   tcs_tds = "NA";
//											   tds_date = null;
//										   }								   								  
//										stmt4.close();
//										rset4.close();
//										
//										tcs_date = null;
//										tcs_amt = null;
//								   }								   								  
								stmt3.close();
								rset3.close();
								}
	
//								cell.setCellValue("'"+tcs_tds+"'")
								tcs_tds = tcs_tds.replaceAll(",", " ");
								tcs_tds = tcs_tds.replaceAll("\n", " ");
								tcs_tds = tcs_tds.replaceAll("\r", " ");
								tcs_tds = tcs_tds.replaceAll("\"", " ");
								str += tcs_tds + ",";
							
							}
							else if(i == 57) {
								if(tcs_tds.equals("TCS")) {
									str += tcs_amt + ",";
								}
								else {
									str += null + ",";
								}
							}
							else if(i == 58 && !tcs_tds.equals("NA") && !tcs_tds.equals("TDS")) {
								queryString3 = "SELECT TAX_CODE FROM FMS7_TAX_MST  WHERE TAX_ALIAS_CODE = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, "TCS");
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									tax_code=rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								
								queryString3 = "SELECT FACTOR FROM FMS7_TAX_STRUCTURE_DTL  WHERE TAX_CODE = ? AND TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY APP_DATE DESC ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, tax_code);
								stmt3.setString(2, "22");
								stmt3.setString(3, inv_dt);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
									}
								else
								{
									value ="0.075";	
								}
								rset3.close();
								stmt3.close();
								str += value + ",";
							} 
							else if(i == 60) {	//PAY_RECV_DT
								value = pay_recv_dt;
								str += value + ",";
							}
							
							else if(i == 62) {	//PAY_INSERT_DT
								value = pay_insert_dt;
								str += value + ",";
							}
							
							else if(i == 64) {	//PAY_UPDATE_DT
								value = pay_update_dt;
								str += value + ",";
							}
							
							else if(i == 65) {	//PAY_REMARK
								value  = rset2.getString(65);
								if(value!=null) {
									value = value.replaceAll(",", "");
								}
								str += value + ",";
							}

							else if(i == 66 ){ 	//TDS_GROSS_PERCENT
								if(tcs_tds.equals("TDS")) {
									value = tds_percent+"";
								}
								else {
									value = null;
								}
								str += value + ",";
							}
							
							else if(i == 67 ) {	 //TDS_GROSS_AMT
								if(tds_percent!=null && tcs_tds.equals("TDS")) {
									Double result1 = (gross_amt.multiply(tds_percent)).divide(BigDecimal.valueOf(100)).doubleValue();
									value = result1.toString();
								}
								else {
									value = null;
								}
								
								str += value + ",";
							}
							
//							else if(i == 68){ 	//TDS_TAX_PERCENT
//								if(tcs_tds.equals("TDS")) {
//									value = tds_percent+"";
//								}
//								else {
//									value = null;
//								}
//								str += value + ",";
//							}
//
//							else if(i == 69) {	 //TDS_TAX_AMT
//								if(tds_percent!=null && tcs_tds.equals("TDS")) {
//									Double result1 = (gross_amt.multiply(tds_percent)).divide(BigDecimal.valueOf(100)).doubleValue();
//									value = result1.toString();
//								}
//								else {
//									value = null;
//								}
//								str += value + ",";
//							}
							
							else if(i == 72) {	//PRINT_DT_ORI
								value = print_dt_ori;
								str += value + ",";
							}
							
							else if(i == 74) {	//PRINT_DT_TRI
								value = print_dt_tri;
								str += value + ",";
							}
							
							else if(i == 76) {	//PRINT_DT_DUP
								value = print_dt_dup;
								str += value + ",";
							}
							
							else if(i == 79) {	//SAP_APPROVED_DT
								value = sap_appr_dt;
								str += value + ",";
							}
							else if(i == 81) {
								str += tcs_date + ",";	
							}
							else if(i == 83) {
								str += tds_date + ",";	
							}
							else if(i == 85) {
								str += tcs_date + ",";	
							}
							else if(i == 86) {
								str += tds_date + ",";	
							}
							else if(i == 88) {	//DRIVER_CD
								queryString3 = "SELECT A.LICENSE_NO FROM DLNG_TRUCK_DRIVER_LINK_DTL A WHERE A.TRUCK_ID = ? AND A.TRANS_CD = ? AND A.STATUS = 'Y' "
										+ "AND A.ENT_DT < TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY A.ENT_DT DESC ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, truck_cd);
								stmt3.setString(2, trans_cd);
								stmt3.setString(3, inv_dt);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									lic = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								queryString3 = "SELECT A.DRIVER_NAME FROM DLNG_DRIVER_MST A WHERE A.TRANS_CD = ? AND "
										+ "A.LICENSE_NO = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								stmt3.setString(2, lic);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								else {
									value = "0";
								}
								rset3.close();
								stmt3.close();
								
//								lic = lic.replaceAll(" ", "");
//								if(lic.length()>15) {
//								lic = lic.substring(0,14);
//								}
//								value = lic;
										
								str += value + ",";
								}
							
							else if(i == 89) {	//FIN_SYS
								if(sap_appr_dt!=null) {
									value = "S";
								}
								else {
									value = null;
								}
								str += value + ",";
							}
							
							else if(i == 90) {	//HOLD_AMT
								queryString3 = "SELECT A.HOLD_AMOUNT FROM FMS8_PAY_RECV_DTL A WHERE A.COMMODITY_TYPE = 'DLNG' AND A.NEW_INV_SEQ_NO = ? "
										+ "AND A.CONTRACT_TYPE = 'S' AND A.REV_NO = (SELECT MAX(C.REV_NO) FROM FMS8_PAY_RECV_DTL C WHERE A.NEW_INV_SEQ_NO = C.NEW_INV_SEQ_NO "
										+ "AND A.CONTRACT_TYPE = C.CONTRACT_TYPE AND A.COMMODITY_TYPE = C.COMMODITY_TYPE)";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, inv_no);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								str += value + ",";
							}
							
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
//						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				//LOA				
				
				queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO, A.RATE_UNIT "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					rate_unit = rset1.getString(6);
					map = "L-"+ no  +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.CUSTOMER_CD, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.FGSA_NO, A.FGSA_REV_NO, A.SN_NO, A.SN_REV_NO, "
							+ "'E', NULL, A.TRUCK_ID, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.SUP_PLANT_CD, NULL, NULL, A.PLANT_SEQ_NO, A.CONTACT_PERSON_CD, 'F', "
							+ "A.HLPL_INV_SEQ_NO, A.HLPL_INV_SEQ_NO, A.NEW_INV_SEQ_NO, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, TO_CHAR(A.PERIOD_START_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "TO_CHAR(A.PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.TOTAL_QTY, A.SALE_PRICE, NULL, A.GROSS_AMT_USD, "
							+ "A.EXCHG_RATE_CD, TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EXCHG_RATE_VALUE, A.EXCHG_RATE_TYPE, NULL, A.GROSS_AMT_INR, "
							+ "A.TAX_AMT_INR, A.TAX_STRUCT_CD, NULL, A.NET_AMT_INR, A.NET_AMT_INR, NULL, A.REMARK_1, A.REMARK_2, A.CHECKED_FLAG, A.CHECKED_BY, "
							+ "TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.AUTHORIZED_FLAG, A.AUTHORIZED_BY, TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "A.APPROVED_FLAG, A.APPROVED_BY, TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, NULL, 'NA', NULL, NULL, A.PAY_RECV_AMT, TO_CHAR(A.PAY_RECV_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_INSERT_BY, "
							+ "TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_UPDATE_BY, TO_CHAR(A.PAY_UPDATE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "A.PAY_REMARK, A.TDS_PERCENT, A.TDS_TAX_AMT, A.TDS_TAX_PERCENT, A.TDS_TAX_AMT, 'T', A.PRINT_BY_ORI, "
							+ "TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_TRI, TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_DUP, "
							+ "TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, A.SUN_APPROVAL, NULL "
							+ "FROM DLNG_INVOICE_MST A WHERE A.CONTRACT_TYPE ='L' AND A.CUSTOMER_CD = ? AND A.FGSA_NO = ? AND A.FGSA_REV_NO = ? AND A.SN_NO = ? AND "
							+ "A.SN_REV_NO = ? "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, no);
					stmt2.setString(3, rev);
					stmt2.setString(4, cont_no);
					stmt2.setString(5, cont_rev);
					stmt2.setString(6, delta_FromDt);
					stmt2.setString(7, delta_FromDt);
					stmt2.setString(8, delta_ToDt);
					stmt2.setString(9, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
//						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						p_seq_no = rset2.getString(14);
//						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
						cd = rset2.getString(1);
//						p_seq_no = rset2.getString(7);
						cont_type = rset2.getString(7);
						truck_cd = rset2.getString(9);
						bu_seq = rset2.getInt(11);
						p_contact = rset2.getString(15);
						inv_no = rset2.getString(19);
						inv_dt = rset2.getString(20);
						period_st = rset2.getString(22);
						period_end = rset2.getString(23);
						due_dt = rset2.getString(24);
						alloc_qty = rset2.getString(25);
						rate = rset2.getString(26);
						exch_cd = rset2.getString(29);
						exch_dt = rset2.getString(30);
						BigDecimal gross_amt = rset2.getBigDecimal(34);
						tax_cd = rset2.getString(36);
						chk_dt = rset2.getString(45);
						auth_dt = rset2.getString(48);
						appr_dt = rset2.getString(51);
						ent_dt = rset2.getString(53);
						pay_recv_dt = rset2.getString(60);
						pay_insert_dt = rset2.getString(62);
						pay_update_dt = rset2.getString(64);
						BigDecimal tds_percent = rset2.getBigDecimal(66);
						print_dt_ori = rset2.getString(72);
						print_dt_tri = rset2.getString(74);
						print_dt_dup = rset2.getString(76);
						sap_appr_dt = rset2.getString(79);
						
						fin_yr = rset2.getString(2);
						inv_seq = rset2.getString(17);
						state_code = "0";
						
						double qty = Double.parseDouble(alloc_qty);
						double price = Double.parseDouble(rate);
						double sale_amt = 0;
						
						
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
							else if(i == 2) {	//FINANCIAL_YEAR
								queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
									+" || '-' || "
									+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
									+ "FROM DUAL";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, period_st);
							stmt3.setString(2, period_end);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								value = rset3.getString(1) == null ? "" :rset3.getString(1);
							}
							fin_year = value;
							rset3.close();
							stmt3.close();
							
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
							}
							
							else if(i == 7) {	//CONTRACT_TYPE
								value = cont_type;
								str += value + ",";
							}

							else if(i == 8) {	//TRUCK_TRANS_CD
								queryString3 = "SELECT TRANS_CD FROM DLNG_TANK_TRUCK_MST WHERE TRUCK_ID = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, truck_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									trans_cd = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								queryString3 = "SELECT TRANS_ABBR FROM DLNG_TRANS_MST WHERE TRANS_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
//								value = trans_cd ;
								
								str += value + ",";
							}
							
							else if(i == 9) {	//TRUCK_CD
								value = truck_cd;
								str += value + ",";
							}
							
							else if(i == 10) {	//MAPPING_ID
								value = inv_dt;
								str += value + ",";
							}
							
							else if(i == 11) {	//BU_UNIT
								value = (bu_seq+1)+"";
								str += value + ",";
							}
							
							else if (i == 14) { //PLANT_SEQ					
								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if(i == 15) {	//CONTACT_PERSON_CD
								queryString3 = "SELECT EMAIL FROM FMS7_CUSTOMER_CONTACT_MST WHERE CUSTOMER_CD = ? AND SEQ_NO = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, cd);
								stmt3.setString(2, p_contact);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								str += value + ",";
							}

							else if(i == 20) {	//INVOICE_DT
								value = inv_dt;
								str += value + ",";
							}

							else if(i == 22) {	//PERIOD_START_DT
								value = period_st;
								str += value + ",";
							}

							else if(i == 23) {	//PERIOD_END_DT
								value = period_end;
								str += value + ",";
							}
							
							
							else if(i == 24) {	//DUE_DT
								value = due_dt;
								str += value + ",";
							}
							
							else if(i == 27) {	//SALE_PRICE_UNIT
								value = rate_unit;
								str += value + ",";
							}
							
							else if(i == 29) {	//EXCHG_RATE_CD
								queryString3 = "SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, exch_cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								str += value + ",";
							}
							
							else if(i == 30) {	//EXCHG_RATE_DT
								value = exch_dt;
								str += value + ",";
							}
							
							else if(i == 36) {	//TAX_STRUCT_CD
								queryString4="SELECT REMARK FROM FMS7_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, tax_cd);
								rset4 = stmt4.executeQuery();
								   if(rset4.next()) {	
									   tax_dtl = rset4.getString(1);
									   tax_dtl = tax_dtl.replaceAll(",", "@ ");
									   tax_dtl = tax_dtl.replaceAll("\n", " ");
									   tax_dtl = tax_dtl.replaceAll("\r", " ");
									   tax_dtl = tax_dtl.replaceAll("\"", " ");
									   if (tax_dtl.contains("MH")) {
										   tax_dtl = tax_dtl.split(" MH")[0] + "%";
										}
										else if (tax_dtl.contains("AP")) {
											tax_dtl = tax_dtl.split(" AP")[0] + "%";
										}
										else if (tax_dtl.contains("ADD. VAT")) {
											tax_dtl = tax_dtl.replace("ADD. VAT", "ADVAT");
										}
										else if (tax_dtl.contains("STAX 0%")) {
											tax_dtl = tax_dtl.replace("STAX 0%", "ZSTAX 0%");
										}
								   }else {
									   tax_dtl = null; 
								   }							   								  
								   stmt4.close();
								   rset4.close();
								  
								   str += tax_dtl + ",";
							}

							else if(i == 41) {	//REMARK_1
								value = rset2.getString(41);
								if (value!=null) {
									value = value.replaceAll(",", " ");
								}
								str += value + ",";
							}
							
							else if(i == 42) {	//REMARK_2
								value = rset2.getString(42);
								if(value!=null) {
								value = value.replaceAll(",", " ");
								}
								str += value + ",";
							}
							
							else if(i == 45) {	//CHECKED_DT
								value = chk_dt;
								str += value + ",";
							}

							else if(i == 48) {	//AUTHORIZED_DT
								value = auth_dt;
								str += value + ",";
							}

							else if(i == 51) {	//APPROVED_DT
								value = appr_dt;
								str += value + ",";
							}
							
							else if(i == 53) {	//ENT_DT
								value = ent_dt;
								str += value + ",";
							}
							else if(i == 56) {	//TCS_TDS
								tcs_tds="";
								
								if(tds_percent!= null) {
									 tcs_tds = "TDS";	
								}
								else {
									
								queryString3="SELECT TCS_AMT,TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_INVOICE_TCS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG'";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, cd);
								stmt3.setString(2, inv_seq);
								stmt3.setString(3, fin_yr);
								stmt3.setString(4, "L");
								stmt3.setString(5, state_code);
								
								rset3 = stmt3.executeQuery();
								   if(rset3.next()) {
									   tcs_amt = rset3.getString(1);
									   tcs_date = rset3.getString(2);
									   tcs_tds = "TCS";								
								   }
								   else {
									   tcs_amt = "";
									   tcs_date = "";
									   tcs_tds = "NA";	
								   }
//								   else {
//									   queryString4="SELECT TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), TDS_AMT FROM FMS7_INVOICE_TDS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG'";
//										stmt4 = conn.prepareStatement(queryString4);
//										stmt4.setString(1, cd);
//										stmt4.setString(2, inv_seq);
//										stmt4.setString(3, fin_yr);
//										stmt4.setString(4, "L");
//										stmt4.setString(5, state_code);
//										
//										rset4 = stmt4.executeQuery();
//										   if(rset4.next()) {
//											  tds_date = rset4.getString(1); 
//											  tds_amt = rset4.getString(2); 
//											  tcs_tds = "TDS";								
//										   }else {
//											   tcs_tds = "NA";
//											   tds_date = null;
//										   }								   								  
//										stmt4.close();
//										rset4.close();
//										
//										tcs_date = null;
//										tcs_amt = null;
//								   }								   								  
								stmt3.close();
								rset3.close();
								}
	
//								cell.setCellValue("'"+tcs_tds+"'")
								tcs_tds = tcs_tds.replaceAll(",", " ");
								tcs_tds = tcs_tds.replaceAll("\n", " ");
								tcs_tds = tcs_tds.replaceAll("\r", " ");
								tcs_tds = tcs_tds.replaceAll("\"", " ");
								str += tcs_tds + ",";
							
							}
							else if(i == 57) {
								if(tcs_tds.equals("TCS")) {
									str += tcs_amt + ",";
								}
								else {
									str += null + ",";
								}
							}
							else if(i == 58 && !tcs_tds.equals("NA") && !tcs_tds.equals("TDS")) {
								queryString3 = "SELECT TAX_CODE FROM FMS7_TAX_MST  WHERE TAX_ALIAS_CODE = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, "TCS");
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									tax_code=rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								
								queryString3 = "SELECT FACTOR FROM FMS7_TAX_STRUCTURE_DTL  WHERE TAX_CODE = ? AND TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY APP_DATE DESC ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, tax_code);
								stmt3.setString(2, "22");
								stmt3.setString(3, inv_dt);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
									}
								else
								{
									value ="0.075";	
								}
								rset3.close();
								stmt3.close();
								str += value + ",";
							} 
							else if(i == 60) {	//PAY_RECV_DT
								value = pay_recv_dt;
								str += value + ",";
							}
							
							else if(i == 62) {	//PAY_INSERT_DT
								value = pay_insert_dt;
								str += value + ",";
							}
							
							else if(i == 64) {	//PAY_UPDATE_DT
								value = pay_update_dt;
								str += value + ",";
							}

							else if(i == 65) {	//PAY_REMARK
								value  = rset2.getString(65);
								if(rset2.getString(65)!=null) {
								value = value.replaceAll(",", "");
								}
								str += value + ",";
							}
							
							else if(i == 66){ 	//TDS_GROSS_PERCENT
								if(tcs_tds.equals("TDS")) {
									value = tds_percent+"";
								}
								else {
									value = null;
								}
								str += value + ",";
							}
							
							else if(i == 67) {	 //TDS_GROSS_AMT
								if(tds_percent!=null && tcs_tds.equals("TDS")) {
									Double result1 = (gross_amt.multiply(tds_percent)).divide(BigDecimal.valueOf(100)).doubleValue();
									value = result1.toString();
								}
								else {
									value = null;
								}
								
								str += value + ",";
							}
							
//							else if(i == 68){ 	//TDS_TAX_PERCENT
//								if(tcs_tds.equals("TDS")) {
//									value = tds_percent+"";
//								}
//								else {
//									value = null;
//								}
//								str += value + ",";
//							}
//
//							else if(i == 69 ) {	 //TDS_TAX_AMT
//								if(tds_percent!=null && tcs_tds.equals("TDS")) {
//									Double result1 = (gross_amt.multiply(tds_percent)).divide(BigDecimal.valueOf(100)).doubleValue();
//									value = result1.toString();
//								}
//								else {
//									value = null;
//								}
//								str += value + ",";
//							}
							
							else if(i == 72) {	//PRINT_DT_ORI
								value = print_dt_ori;
								str += value + ",";
							}
							
							else if(i == 74) {	//PRINT_DT_TRI
								value = print_dt_tri;
								str += value + ",";
							}
							
							else if(i == 76) {	//PRINT_DT_DUP
								value = print_dt_dup;
								str += value + ",";
							}
							
							else if(i == 79) {	//SAP_APPROVED_DT
								value = sap_appr_dt;
								str += value + ",";
							}
							else if(i == 81) {
								str += tcs_date + ",";	
							}
							else if(i == 83) {
								str += tds_date + ",";	
							}
							else if(i == 85) {
								str += tcs_date + ",";	
							}
							else if(i == 86) {
								str += tds_date + ",";	
							}

							else if(i == 88) {	//DRIVER_CD
								queryString3 = "SELECT A.LICENSE_NO FROM DLNG_TRUCK_DRIVER_LINK_DTL A WHERE A.TRUCK_ID = ? AND A.TRANS_CD = ? AND A.STATUS = 'Y' "
										+ "AND A.ENT_DT < TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY A.ENT_DT DESC ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, truck_cd);
								stmt3.setString(2, trans_cd);
								stmt3.setString(3, inv_dt);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									lic = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								queryString3 = "SELECT A.DRIVER_NAME FROM DLNG_DRIVER_MST A WHERE A.TRANS_CD = ? AND "
										+ "A.LICENSE_NO = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_cd);
								stmt3.setString(2, lic);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								else {
									value = "0";
								}
								rset3.close();
								stmt3.close();
								
//								lic = lic.replaceAll(" ", "");
//								if(lic.length()>15) {
//								lic = lic.substring(0,14);
//								}
//								value = lic;
										
								str += value + ",";
								}

							else if(i == 89) {	//FIN_SYS
								if(sap_appr_dt!=null) {
									value = "S";
								}
								else {
									value = null;
								}
								str += value + ",";
							}

							else if(i == 90) {	//HOLD_AMT
								queryString3 = "SELECT A.HOLD_AMOUNT FROM FMS8_PAY_RECV_DTL A WHERE A.COMMODITY_TYPE = 'DLNG' AND A.NEW_INV_SEQ_NO = ? "
										+ "AND A.CONTRACT_TYPE = 'L' AND A.REV_NO = (SELECT MAX(C.REV_NO) FROM FMS8_PAY_RECV_DTL C WHERE A.NEW_INV_SEQ_NO = C.NEW_INV_SEQ_NO "
										+ "AND A.CONTRACT_TYPE = C.CONTRACT_TYPE AND A.COMMODITY_TYPE = C.COMMODITY_TYPE)";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, inv_no);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								str += value + ",";
							}
							
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
//						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_" + start_end_dt + ".xlsx";
			
//			fileOut = new FileOutputStream(filename);
			
//			workbook.write(fileOut);
//			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INVOICE_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1, count + ",", conn);
			
			System.out.println("<<END>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			System.out.println();
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	

	public void FMS_DLNG_INV_FILE_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_DLNG_INV_FILE_DTL()";
		
		try {
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_INV_FILE_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT";
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			
//			nrow = 0;
//			ncell = 0;
			count = 0;
			String inv_seq= "",inv_dt="",ent_by="",ent_dt="",pdf_type="",file_nm="";
			String o_by= "",o_dt="",d_by="",d_dt="",t_by="",t_dt="";
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "", gas_dt="";
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_INV_FILE_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
			
			queryString = "SELECT DISTINCT(A.CUSTOMER_CD), A.EFF_DT  FROM FMS7_CUSTOMER_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) "
					+ "ORDER BY A.CUSTOMER_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				cd=rset.getString(1);
				
					queryString2 = "SELECT CONTRACT_TYPE, HLPL_INV_SEQ_NO, FINANCIAL_YEAR, TO_CHAR(INVOICE_DT,'DD/MM/YY'), "
							+ "PRINT_BY_ORI, TO_CHAR(PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "PRINT_BY_DUP, TO_CHAR(PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "PRINT_BY_TRI, TO_CHAR(PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND CONTRACT_TYPE IN ('S','L') ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					rset2 = stmt2.executeQuery();
				while(rset2.next()) 
				{
					cont_type = rset2.getString(1);
					inv_seq = rset2.getString(2);
					fin_yr = rset2.getString(3);
					inv_dt = rset2.getString(4);
					o_by = rset2.getString(5);
					o_dt = rset2.getString(6);
					d_by = rset2.getString(7);
					d_dt = rset2.getString(8);
					t_by = rset2.getString(9);
					t_dt = rset2.getString(10);
					
					queryString1 = "SELECT '2', '24', NULL, NULL, INV_TYPE, PDF_INV_NM, NULL, NULL, NULL, NULL, PDF_SIGNED_FLAG, SIGNED_BY, "
							+ "TO_CHAR(SIGNED_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL "
							+ " FROM DLNG_INV_PDF_DTL "
							+ " WHERE PDF_INV_NM LIKE ? AND TO_DATE(CREATED_DT, 'DD/MM/YYYY') = TO_DATE(?, 'DD/MM/YYYY') AND INV_TYPE IN ('O','D','T')";
//							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					
					stmt1 = conn.prepareStatement(queryString1);	
					stmt1.setString(1, "%"+cont_type+"-"+inv_seq);
					stmt1.setString(2, inv_dt);
//					stmt1.setString(2, delta_FromDt);
//					stmt1.setString(3, delta_FromDt);
//					stmt1.setString(4, delta_ToDt);
//					stmt1.setString(5, delta_ToDt);
					rset1 = stmt1.executeQuery();
					
//					System.out.println(inv_dt+"=="+"%-"+cont_type+"-"+inv_seq);
					while(rset1.next())
					{	
//						System.out.println(">>>");
						pdf_type = rset1.getString(5);
						file_nm = rset1.getString(6);
						
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						for (int i = 0; i < columns.split(",").length; i++) {
							
						if(i == 2) {	//INVOICE_SEQ
							value = inv_seq;
//							cell= row.createCell(i);
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
							
						else if (i == 3) {	//FINANCIAL_YEAR
							value = fin_yr;
//							cell= row.createCell(i);
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if (i == 5) {
							value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
							if(!value.contains(".pdf")) {
								value = value + ".pdf";
							}
							str += value + ",";
						 }
						else if(i == 6) {	//ENT_BY
							if(pdf_type.equals("O")) {
								value = o_by;
							}
							
							else if(pdf_type.equals("D")) {
								value = d_by;
							}
							
							else if(pdf_type.equals("T")) {
								value = t_by;
							}
//							cell= row.createCell(i);
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
						else if(i == 7) {	//ENT_DT
							if(pdf_type.equals("O")) {
								value = o_dt;
							}
							
							else if(pdf_type.equals("D")) {
								value = d_dt;
							}
							
							else if(pdf_type.equals("T")) {
								value = t_dt;
							}
//							cell= row.createCell(i);
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
						else if(i == 11) {
							value = rset1.getString(12);
//							cell = row.createCell(i);																					
//							cell.setCellValue("'" + value + "'");
							str += value + ",";
						}
							
							else {
								value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
//								cell = row.createCell(i);																					
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
							
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (company_cd+",24" + ","+inv_seq+ "," +fin_yr+","+pdf_type+","+file_nm+","), conn, "");
						
						
					}					
					stmt1.close();
					rset1.close();
				}
				rset2.close();
				stmt2.close();
				}
			rset.close();
			stmt.close();
			
			
			
			
//			filename = migration_setup_dir + "EXPORT/FMS_DLNG_INV_FILE_DTL_"+start_end_dt+".xlsx";
			
//			fileOut = new FileOutputStream(filename);  
			
//			workbook.write(fileOut);
//			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INV_FILE_DTL>>,,,,", conn);
			
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
		}
		
	}
	
	
	public void FMS_DLNG_INV_PAY_RECV_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_DLNG_INV_PAY_RECV_DTL()";
		
		try {
			
			System.out.println("<<START>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_INV_PAY_RECV_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1, function_nm + "E" + "," + start_end_dt + ",", conn);
			
			columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,PAY_RECV_DT,PAY_RECV_AMT,PAY_REMARK,ENT_BY,ENT_DT,ADV_FLAG,ADV_AMT";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			String temp = ""; 
//			nrow = 0;
//			ncell = 0;
			count = 0;
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			String map = "",pay_recv_dt="",pay_remark="",pay_insert_dt="";
			int bu_seq=0;
			NumberFormat nf = new DecimalFormat("###########0.00");
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_INV_PAY_RECV_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname,"COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,GAS_DT,PLANT_SEQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT "
					+ "FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.FLSA_NO, A.FLSA_REV_NO, A.SN_NO, A.SN_REV_NO "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = "S-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.CUSTOMER_CD, A.NEW_INV_SEQ_NO, A.HLPL_INV_SEQ_NO, A.FINANCIAL_YEAR, '1', TO_CHAR(A.PAY_RECV_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "A.PAY_RECV_AMT, A.PAY_REMARK, A.PAY_INSERT_BY, "
							+ "TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL "
							+ "FROM DLNG_INVOICE_MST A WHERE A.CONTRACT_TYPE ='S' AND A.CUSTOMER_CD = ? AND A.FGSA_NO = ? AND A.FGSA_REV_NO = ? AND A.SN_NO = ? AND "
							+ "A.SN_REV_NO = ? "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, no);
					stmt2.setString(3, rev);
					stmt2.setString(4, cont_no);
					stmt2.setString(5, cont_rev);
					stmt2.setString(6, delta_FromDt);
					stmt2.setString(7, delta_FromDt);
					stmt2.setString(8, delta_ToDt);
					stmt2.setString(9, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						inv_seq="";fin_yr="";
//						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
//						p_seq_no = p_seq_no.split("-")[6];
						
						abbr = rset.getString(1);
						cd = rset2.getString(1);
						
						inv_seq = rset2.getString(3);
						fin_yr = rset2.getString(4);
						pay_recv_dt = rset2.getString(6);
						pay_remark = rset2.getString(8);
						pay_insert_dt = rset2.getString(10);
						
						
						
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							
							
							if(i == 5) {	//PAY_RECV_DT
								value = pay_recv_dt;
								str += value + ",";
							}
							else if(i == 7) {	//PAY_REMARK
								value = pay_remark;
								if(value!=null) {
									value = value.replaceAll(",", " ");
								} 
								
								str += value + ",";
							}
							else if(i == 9) {	//ENT_DT
								value = pay_insert_dt;
								str += value + ",";
							}

							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
//						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				//LOA				
				
				queryString1 = "SELECT A.CUSTOMER_CD, A.TENDER_NO, '0', A.LOA_NO, A.LOA_REV_NO "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					map = "L-"+ no  +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.CUSTOMER_CD, A.NEW_INV_SEQ_NO, A.HLPL_INV_SEQ_NO, A.FINANCIAL_YEAR, '1', TO_CHAR(A.PAY_RECV_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "A.PAY_RECV_AMT, A.PAY_REMARK, A.PAY_INSERT_BY, "
							+ "TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL "
							+ "FROM DLNG_INVOICE_MST A WHERE A.CONTRACT_TYPE ='L' AND A.CUSTOMER_CD = ? AND A.FGSA_NO = ? AND A.FGSA_REV_NO = ? AND A.SN_NO = ? AND "
							+ "A.SN_REV_NO = ? "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, no);
					stmt2.setString(3, rev);
					stmt2.setString(4, cont_no);
					stmt2.setString(5, cont_rev);
					stmt2.setString(6, delta_FromDt);
					stmt2.setString(7, delta_FromDt);
					stmt2.setString(8, delta_ToDt);
					stmt2.setString(9, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						
						abbr = rset.getString(1);
						cd = rset2.getString(1);
						
						inv_seq = rset2.getString(3);
						fin_yr = rset2.getString(4);
						pay_recv_dt = rset2.getString(6);
						pay_remark = rset2.getString(8);
						pay_insert_dt = rset2.getString(10);
						
						
						
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
							
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							
							
							if(i == 5) {	//PAY_RECV_DT
								value = pay_recv_dt;
								str += value + ",";
							}
							else if(i == 7) {	//PAY_REMARK
								value = pay_remark;
								if(value!=null) {
									value = value.replaceAll(",", " ");
								} 
								
								str += value + ",";
							}
							else if(i == 9) {	//ENT_DT
								value = pay_insert_dt;
								str += value + ",";
							}

							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
//						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_" + start_end_dt + ".xlsx";
			
//			fileOut = new FileOutputStream(filename);
			
//			workbook.write(fileOut);
//			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INV_PAY_RECV_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1, count + ",", conn);
			
			System.out.println("<<END>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			System.out.println();
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		}
		catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	
	public void FMS_DLNG_FFLOW_INV_MST() throws SQLException, IOException {
		
		function_nm = "FMS_DLNG_FFLOW_INV_MST()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_FFLOW_INV_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "BU_STATE_TIN,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
					+ "INVOICE_CATEGORY,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,INVOICE_TYPE,LINKED_INVOICE,NUM_LINE,"
					+ "NOTE,GROSS_AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT_INR,"
					+ "TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,"
					+ "CHECKED_FLAG,CHECKED_BY,CHECKED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,"
					+ "MODIFY_DT,OTHER_INV_STR,AMT_WORD,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,"
					+ "PRINT_BY_DUP,PRINT_DT_DUP,INVOICE_ID_SEQ,TDS_STRUCT_CD,TDS_EFF_DT,TCS_STRUCT_CD,TCS_EFF_DT,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,"
					+ "PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,"
					+ "TCS_FACTOR,TCS_TDS,TCS_AMT,ALLOC_QTY,SUB_INV_TYPE,FIN_SYS,HOLD_AMT,CARGO_NO,SAC_CD";

//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");

//			nrow = 0;
//			ncell = 0;
			count = 0;
			String cont_type="",exch_cd="",aprv_flg="",chk_flg="",chk_dt="",chk_by="",exch_dt="",sup_plnt_cd="",agmt_no = "",agmt_rev="",pdf_inv="";
			
			String fname_csv = "", str = "",sac_code="";
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_MST_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);

			logger.checkpoint(fname, "COUNTERPARTY_ABBR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				queryString1 = "SELECT A.CUSTOMER_CD,A.AGREEMENT_NO,A.REV_NO,A.CN_NO,A.CN_REV_NO,A.MAPPING_ID "
						+ " FROM FMS8_LNG_REGAS_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					queryString2 = " SELECT A.FINANCIAL_YEAR,A.CUSTOMER_CD,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CONTRACT_TYPE,A.SUP_PLANT_CD + 1,A.SUP_STATE_CODE,NULL,NULL,A.CONTACT_PERSON_CD,A.HLPL_INV_SEQ_NO,"
							+ "A.NEW_INV_SEQ_NO,TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'),'S',NULL,TO_CHAR(A.PERIOD_START_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS'),'TLU',NULL,NULL,A.REMARK_1,A.GROSS_AMT_USD,A.EXCHG_RATE_CD,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EXCHG_RATE_VALUE,'1',"
							+ "A.GROSS_AMT_INR,A.TAX_AMT_INR,A.TAX_STRUCT_CD,NULL,A.NET_AMT_INR,NULL,NULL,A.NET_AMT_INR,NULL,A.CHECKED_FLAG, A.CHECKED_BY, TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "A.APPROVED_FLAG, A.APPROVED_BY, TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,A.PDF_INV_DTL,"
							+ "A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_TRI,TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_DUP, TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "A.HLPL_INV_SEQ_NO,NULL,NULL,NULL,NULL,A.SUN_APPROVAL,A.SUN_APPROVAL_BY,TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),A.PAY_RECV_AMT, "
							+ "TO_CHAR(A.PAY_RECV_DT, 'DD/MM/YYYY HH24:MI:SS'),A.PAY_INSERT_BY, TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_UPDATE_BY, TO_CHAR(A.PAY_UPDATE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.PAY_REMARK,"
							+ "A.TDS_PERCENT,A.GROSS_TDS_AMT, A.TDS_PERCENT, A.TDS_TAX_AMT,NULL,NULL,NULL,A.TOTAL_QTY,NULL,NULL,NULL,NULL,NULL FROM FMS7_INVOICE_MST A "
							+ "WHERE A.CUSTOMER_CD = ? AND A.SN_NO = '10722' AND A.FGSA_NO = ? AND A.SN_REV_NO = ? AND A.FGSA_REV_NO = ? AND A.CONTRACT_TYPE = 'E' AND A.MAPPING_ID = ? "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset1.getString(1));
//					stmt2.setString(2, rset1.getString(4));
					stmt2.setString(2, rset1.getString(2));
					stmt2.setString(3, rset1.getString(5));
					stmt2.setString(4, rset1.getString(3));
					stmt2.setString(5, rset1.getString(6));
					stmt2.setString(6, delta_FromDt);
					stmt2.setString(7, delta_FromDt);
					stmt2.setString(8, delta_ToDt);
					stmt2.setString(9, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						str = "";
						abbr = rset.getString(1);	
						cd = rset2.getString(2);
						agmt_no = rset2.getString(3);
						agmt_rev = rset2.getString(4);
						cont_no = rset2.getString(5);
						cont_rev = rset2.getString(6);
						cont_type = rset2.getString(7);
						fin_yr = rset2.getString(1);
						inv_seq = rset2.getString(13);
						
						String mail="",sale_unit="",sale_amt="",tax_dtl="";
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					       
					    }
						str += abbr + ",";
						
							
						for (int i = 1; i < columns.split(",").length; i++) {
							value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll("\n", " ");
							value = value.replaceAll(",", "_");
							value = value.replaceAll("\r", "");				
							
							
						  if(i == 2) {
								if(Integer.parseInt(cont_no)<=9999) { 
									cont_ref = "C"+"-"+cd+"-"+agmt_no+"-"+cont_no+"-"+cont_rev;
								}
								else {
									cont_ref = "A"+"-"+cd+"-"+agmt_no+"-"+cont_no+"-"+cont_rev;
								}
								str += cont_ref + ",";	
						   }
							
						  else if (i == 7) { // cont_type							
								if(cont_ref.startsWith("A")) {
									cont_type= "Q";
								}else if(cont_ref.startsWith("C")) {
									cont_type= "O";
								}
								str += cont_type + ",";
							}
							else if(i == 12) {
								queryString3 = "SELECT A.EMAIL FROM FMS7_CUSTOMER_CONTACT_MST A WHERE A.SEQ_NO = ? AND A.CUSTOMER_CD = ? "
										+ "AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_CUSTOMER_CONTACT_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
										+ "AND A.SEQ_NO = B.SEQ_NO)";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, rset2.getString(12));
								stmt3.setString(2, cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									mail = rset3.getString(1);
									if(mail != null) {
										if(!mail.equals("")) {
										mail = mail.replaceAll(",", "");
										}
									}
								}
								stmt3.close();
								rset3.close();
								str += mail + ",";		
							}
							else if (i == 17) { 
								
								String frq = "0";
								str += frq + ",";		
							}
							else if(i == 21) {
								String inv_type="",line_item="";
								queryString3="SELECT ITEM_DESCRIPTION,SAC_CODE FROM FMS8_OTHER_INVOICE_DTL WHERE INV_SEQ_NO= ? AND CONTRACT_TYPE = 'E' AND FINANCIAL_YEAR = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, inv_seq);
								stmt3.setString(2, fin_yr);
								rset3 = stmt3.executeQuery();
								   if(rset3.next()) {	
									  	line_item = rset3.getString(1);	
									  	sac_code = rset3.getString(2);
								   }								   								  
								   stmt3.close();
								   rset3.close();
								   
								   if(line_item.contains("TLU")) {
									   inv_type = "TLU";
								   }
								str += "TLU" + ",";
							}
							else if(i == 26) {
								value = rset2.getString(26);
								name="";
								queryString3="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								   if(rset3.next()) {	
									   name = rset3.getString(1);
									   name= name.toUpperCase();
									   if (name.contains("CUSTOMS RATE")) {
											name = "CUSTOM EXCHANGE RATE";
										}
										else if (name.contains("RBI REFERENCE")) {
											name = "RBI REFERENCE RATE";
										}
										else if (name.contains("SBI MUMBAI TT AVERAGE")) {
											name = "SBI MUMBAI TT BUY SELL";
										}
										else if (name.contains("SBI TT BUYING")) {
											name = "SBI RATE BUY";
										}
										else if (name.contains("SBI TT SELLING")) {
											name = "SBI RATE SELL";
										}
										else if (name.contains("SBI TT BUY SELL")) {
											name = "SBI RATE BUY SELL";
										}								
								   }								   								  
								   stmt3.close();
								   rset3.close();
//								   cell.setCellValue("'"+name.trim()+"'");
									name = name.replaceAll(",", " ");
									name = name.replaceAll("\n", " ");
									name = name.replaceAll("\r", " ");
									name = name.replaceAll("\"", " ");
									str += name.trim() + ",";
							}
							else if(i==32) {
//								System.out.println(rset2.getString(32)+"=="+rset2.getString(15));
								queryString4="SELECT TAX_DETAILS FROM FMS8_OTHER_INVOICE_DTL WHERE INV_SEQ_NO = ? AND CONTRACT_TYPE = 'E' AND FINANCIAL_YEAR = ?";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, rset2.getString(13));
								stmt4.setString(2, rset2.getString(1));
								rset4 = stmt4.executeQuery();
								   if(rset4.next()) {	
									   if (rset4.getString(1).contains("0.0")) {
										   tax_dtl = "CGST 0%@ SGST 0%";
									   }
									   else if (rset4.getString(1).contains("2.5")) {
										   tax_dtl = "CGST 2.5%@ SGST 2.5%";
									   }
									   else if (rset4.getString(1).contains("6.0")) {
										   tax_dtl = "CGST 6%@ SGST 6%";
									   }
									   else if (rset4.getString(1).contains("9.0")) {
										   tax_dtl = "CGST 9%@ SGST 9%";
									   }
									   else if (rset4.getString(1).contains("14.0")) {
										   tax_dtl = "CGST 14%@ SGST 14%";
									   }
									   else {
										   tax_dtl = null;
									   }
								   }else {
									   tax_dtl = null; 
								   }							   								  
								   stmt4.close();
								   rset4.close();
								  
								   str += tax_dtl + ",";
							}
							else if(i == 49) {
								value = "TAX INVOICE";
								str += value + ",";
							}
							else if(i == 51) {
								pdf_inv = rset2.getString(51);
								if(pdf_inv!=null) {
									if(pdf_inv.contains("T")) {
										pdf_inv = "T";
									}else if(pdf_inv.contains("D")) {
										pdf_inv = "D";
									}else if(pdf_inv.contains("O")) {
										pdf_inv ="O";
									}else {
										pdf_inv = null;
									}
								}else {
									pdf_inv = null;
								}
								
								str += pdf_inv + ",";
							}
							else if(i == 59) {
							if(rset2.getString(73)!=null) {
								String tds_per = rset2.getString(73);
								str += "TDS "+tds_per+"%" + ",";
							}else {
								String tds_per = null;
								str += tds_per + ",";
							}
							}
							else if(i == 74) {
								if(rset2.getString(73)!=null){
									double tds_per = rset2.getDouble(73);
									double gross_amt = rset2.getDouble(30);
									
									BigDecimal tdsPerBD = BigDecimal.valueOf(tds_per);
									BigDecimal grossAmtBD = BigDecimal.valueOf(gross_amt);
									
									BigDecimal tdsAmtBD = grossAmtBD.multiply(tdsPerBD).divide(BigDecimal.valueOf(100));
									str += tdsAmtBD+ ",";
								}else {
									BigDecimal tdsAmtBD=null;
									str += tdsAmtBD + ",";
								}
							}
						  
							else if(i == 76) {
								if(rset2.getString(73)!=null){
								double tds_per = rset2.getDouble(73);
								double tax_amt = rset2.getDouble(31);
								
								BigDecimal tdsPerBD = BigDecimal.valueOf(tds_per);
								BigDecimal taxAmtBD = BigDecimal.valueOf(tax_amt);
								
								BigDecimal tdsAmtBD = taxAmtBD.multiply(tdsPerBD).divide(BigDecimal.valueOf(100));
								str += tdsAmtBD + ",";
							}else {
								BigDecimal tdsAmtBD=null;
								str += tdsAmtBD + ",";
							}

							}
							else if(i == 78) {
								if(rset2.getString(73) != null) {
									tcs_tds = "TDS";
								}else {
									tcs_tds = null;
								}
								str += tcs_tds + ",";
							}
							else if(i == 80) {
								String qty_mmbtu = "";
								queryString4 = "SELECT A.QUANTITY FROM FMS8_OTHER_INVOICE_DTL A, FMS7_INVOICE_MST B WHERE B.CUSTOMER_CD = ? AND A.INV_SEQ_NO = ? AND A.FINANCIAL_YEAR = ? AND A.CONTRACT_TYPE = 'E' AND "
										+ "A.INV_SEQ_NO = B.HLPL_INV_SEQ_NO AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.EFF_DT = B.INVOICE_DT AND A.SUPPLIER_CD = B.SUPPLIER_CD "
										+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE ORDER BY A.INV_SEQ_NO ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, rset1.getString(1));
								stmt4.setString(2, rset2.getString(13));
								stmt4.setString(3, rset2.getString(1));
								rset4 = stmt4.executeQuery();
								
								if(rset4.next()) {
									qty_mmbtu = rset4.getString(1);
								}
								else {
									qty_mmbtu = "0";
								}
								stmt4.close();
								rset4.close();
								str += qty_mmbtu + ",";
							}	
							else if(i == 82) {
								if(rset2.getString(63)!=null) {
									str += "S,";
								}else {
									str += null + ",";
								}
							}
							else if(i == 85) {
								str += sac_code + ",";
							}
							
							else {
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				// Late Payment
				String mail="",sale_unit="",sale_amt="",tax_dtl="",inv_dt="",inv_no="";
				queryString2 = " SELECT A.FINANCIAL_YEAR,A.CUSTOMER_CD,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CONTRACT_TYPE,A.SUP_PLANT_CD + 1,A.SUP_STATE_CODE,NULL,"
						+ "NULL,A.CONTACT_PERSON_CD,A.HLPL_INV_SEQ_NO,A.NEW_INV_SEQ_NO,TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'),'P',NULL,TO_CHAR(A.PERIOD_START_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS'),'LP',"
						+ "A.REMARK_SPECIFICATION,NULL,A.REMARK_1,A.GROSS_AMT_USD,A.EXCHG_RATE_CD,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EXCHG_RATE_VALUE,'1',A.GROSS_AMT_INR,A.TAX_AMT_INR,"
						+ "A.TAX_STRUCT_CD,NULL,A.NET_AMT_INR,NULL,NULL,A.NET_AMT_INR,NULL,A.CHECKED_FLAG, A.CHECKED_BY, TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.APPROVED_FLAG,"
						+ "A.APPROVED_BY, TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,A.PDF_INV_DTL,A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_TRI,"
						+ "TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_DUP, TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'),A.HLPL_INV_SEQ_NO,NULL,NULL,NULL,NULL,A.SUN_APPROVAL,A.SUN_APPROVAL_BY,"
						+ "TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),A.PAY_RECV_AMT,TO_CHAR(A.PAY_RECV_DT, 'DD/MM/YYYY HH24:MI:SS'),A.PAY_INSERT_BY, TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_UPDATE_BY, TO_CHAR(A.PAY_UPDATE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.PAY_REMARK,A.TDS_PERCENT,A.TDS_TAX_AMT,"
						+ "A.TDS_PERCENT,A.TDS_TAX_AMT,NULL,NULL,NULL,A.TOTAL_QTY,NULL,NULL,NULL,NULL,NULL "
						+ " FROM DLNG_INVOICE_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND A.CONTRACT_TYPE IN ('I', 'M') AND "
						+ "A.REMARK_SPECIFICATION NOT IN (SELECT C.NEW_INV_SEQ_NO FROM DLNG_INVOICE_MST C WHERE A.REMARK_SPECIFICATION=C.NEW_INV_SEQ_NO AND C.CONTRACT_TYPE = 'V') AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset.getString(2));
				stmt2.setString(2, delta_FromDt);
				stmt2.setString(3, delta_FromDt);
				stmt2.setString(4, delta_ToDt);
				stmt2.setString(5, delta_ToDt);
				rset2 = stmt2.executeQuery();
				
				while (rset2.next()) {
					str = "";
					abbr = rset.getString(1);	
					cd = rset2.getString(2);
					agmt_no = rset2.getString(3);
					agmt_rev = rset2.getString(4);
					cont_no = rset2.getString(5);
					cont_rev = rset2.getString(6);
					cont_type = rset2.getString(7);
					fin_yr = rset2.getString(1);
					inv_seq = rset2.getString(13);
					inv_dt = rset2.getString(15);
					inv_no = rset2.getString(14);
					state_code = rset2.getString(9);
					
					map = "S-"+agmt_no+"-"+agmt_rev+"-"+cont_no+"-"+cont_rev;
					
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				       
				    }

					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
						value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll("\n", " ");
						value = value.replaceAll(",", "_");
						value = value.replaceAll("\r", "");
						
					  if (i == 1) { 		
							str += fin_yr + ",";
					  }
//					  if (i == 2) {
//						queryString1 = "SELECT SN_REF_NO FROM DLNG_SN_MST WHERE A.FLSA_NO=? AND A.FLSA_REV_NO=? AND A.SN_NO=? AND A.SN_REV_NO=? ";
//						stmt1 = conn.prepareStatement(queryString1);
//						stmt1.setString(1, rset2.getString(3));
//						stmt1.setString(2, rset2.getString(4));
//						stmt1.setString(3, rset2.getString(5));
//						stmt1.setString(4, rset2.getString(6));
//						rset1 = stmt1.executeQuery();
//
//						if(rset1.next() && rset1.getString(1)!=null) {
//							value = rset1.getString(1);
//						}else {
//							value = rset2.getString(5)+"-"+rset2.getString(6);
//						}
//						stmt1.close();
//						rset1.close();
//						cont_ref = value;
//						str += value + ",";
//					}
//					
					else if(i == 2) {
						str += map + ",";
					}	
					else if (i == 7) { // cont_type	
						  queryString1 = "SELECT CONTRACT_TYPE FROM DLNG_INVOICE_MST WHERE FINANCIAL_YEAR = ? AND  NEW_INV_SEQ_NO = ?";
						  stmt1 = conn.prepareStatement(queryString1);
						  stmt1.setString(1, "20"+rset2.getString(22).split("/")[1].split("-")[0]+"-20"+rset2.getString(22).split("/")[1].split("-")[1]);
						  stmt1.setString(2, rset2.getString(22));
						  rset1 = stmt1.executeQuery();
						  
//						  System.out.println("20"+rset2.getString(22).split("/")[1].split("-")[0]+"-20"+rset2.getString(22).split("/")[1].split("-")[1]);
						  if (rset1.next()) {
							  cont_type = rset1.getString(1);
						  }
						  stmt1.close();
						  rset1.close();
						  str += cont_type + ",";
						}
						else if(i == 12) {
							queryString3 = "SELECT A.EMAIL FROM FMS7_CUSTOMER_CONTACT_MST A WHERE A.SEQ_NO = ? AND A.CUSTOMER_CD = ? "
									+ "AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_CUSTOMER_CONTACT_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
									+ "AND A.SEQ_NO = B.SEQ_NO)";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, rset2.getString(12));
							stmt3.setString(2, cd);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								mail = rset3.getString(1);
								if(mail != null) {
									if(!mail.equals("")) {
									mail = mail.replaceAll(",", "");
									}
								}
							}
							stmt3.close();
							rset3.close();
							str += mail + ",";		
						}
//						else if (i == 16) {
//							if (cont_ref.startsWith("A") || cont_ref.startsWith("C")) {
//								value = "S";
//							}
//							str += value + ",";
//						}
						else if (i == 17) { 
							
							String frq="0";
							
							str += frq + ",";		
						}
						else if (i == 24 && value.length() > 250) {
							value = value.substring(0, 250);
							str += value + ",";
						}
	 					
						else if(i == 26) {
							value = rset2.getString(26);
							name="";
							queryString3="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, value);
							rset3 = stmt3.executeQuery();
							   if(rset3.next()) {	
								   name = rset3.getString(1);
								   name= name.toUpperCase();
								   if (name.contains("CUSTOMS RATE")) {
										name = "CUSTOM EXCHANGE RATE";
									}
									else if (name.contains("RBI REFERENCE")) {
										name = "RBI REFERENCE RATE";
									}
									else if (name.contains("SBI MUMBAI TT AVERAGE")) {
										name = "SBI MUMBAI TT BUY SELL";
									}
									else if (name.contains("SBI TT BUYING")) {
										name = "SBI RATE BUY";
									}
									else if (name.contains("SBI TT SELLING")) {
										name = "SBI RATE SELL";
									}
									else if (name.contains("SBI TT BUY SELL")) {
										name = "SBI RATE BUY SELL";
									}								
							   }								   								  
							   stmt3.close();
							   rset3.close();
		//							   cell.setCellValue("'"+name.trim()+"'");
								name = name.replaceAll(",", " ");
								name = name.replaceAll("\n", " ");
								name = name.replaceAll("\r", " ");
								name = name.replaceAll("\"", " ");
								str += name.trim() + ",";
						}
		//						}
						else if(i==32 && !value.equals("null")) {
							queryString4="SELECT REMARK FROM FMS7_TAX_STRUCTURE WHERE TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY APP_DATE DESC";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1,value);
							stmt4.setString(2, rset2.getString(18));
							rset4 = stmt4.executeQuery();
							   if(rset4.next()) {	
								   tax_dtl = rset4.getString(1);
								   tax_dtl = tax_dtl.replaceAll(",", "@ ");
								   tax_dtl = tax_dtl.replaceAll("\n", " ");
								   tax_dtl = tax_dtl.replaceAll("\r", " ");
								   tax_dtl = tax_dtl.replaceAll("\"", " ");
								   if (tax_dtl.contains("MH")) {
									   tax_dtl = tax_dtl.split(" MH")[0] + "%";
									}
									else if (tax_dtl.contains("AP")) {
										tax_dtl = tax_dtl.split(" AP")[0] + "%";
									}
									else if (tax_dtl.contains("ADD. VAT")) {
										tax_dtl = tax_dtl.replace("ADD. VAT", "ADVAT");
									}
									else if (tax_dtl.contains("STAX 0%")) {
										tax_dtl = tax_dtl.replace("STAX 0%", "ZSTAX 0%");
									}
							   }else {
								   tax_dtl = null; 
							   }							   								  
							   stmt4.close();
							   rset4.close();
							  
							   str += tax_dtl + ",";
						}
						else if(i == 49) {
							value = "DEBIT NOTE";
							str += value + ",";
						}
						else if(i == 51) {
							pdf_inv = rset2.getString(51);
							if(pdf_inv!=null) {
								if(pdf_inv.contains("T")) {
									pdf_inv = "T";
								}else if(pdf_inv.contains("D")) {
									pdf_inv = "D";
								}else if(pdf_inv.contains("O")) {
									pdf_inv ="O";
								}else {
									pdf_inv = null;
								}
							}else {
								pdf_inv = null;
							}
							
							str += pdf_inv + ",";
						}
						else if(i == 59) {
							if(rset2.getString(73)!=null) {
								String tds_per = rset2.getString(73);
								str += "TDS "+tds_per+"%" + ",";
							}else {
								String tds_per = null;
								str += tds_per + ",";
							}
						}
						else if(i == 74) {
							if(rset2.getString(73)!=null){
								double tds_per = rset2.getDouble(73);
								double gross_amt = rset2.getDouble(30);
								
								BigDecimal tdsPerBD = BigDecimal.valueOf(tds_per);
								BigDecimal grossAmtBD = BigDecimal.valueOf(gross_amt);
								
								BigDecimal tdsAmtBD = grossAmtBD.multiply(tdsPerBD).divide(BigDecimal.valueOf(100));
								str += tdsAmtBD+ ",";
							}else {
								BigDecimal tdsAmtBD=null;
								str += tdsAmtBD + ",";
							}
						}
					  
						else if(i == 76) {
							if(rset2.getString(73)!=null){
								double tds_per = rset2.getDouble(73);
								double tax_amt = rset2.getDouble(31);
								
								BigDecimal tdsPerBD = BigDecimal.valueOf(tds_per);
								BigDecimal taxAmtBD = BigDecimal.valueOf(tax_amt);
								
								BigDecimal tdsAmtBD = taxAmtBD.multiply(tdsPerBD).divide(BigDecimal.valueOf(100));
								str += tdsAmtBD + ",";
						}else {
							BigDecimal tdsAmtBD=null;
							str += tdsAmtBD + ",";
						}
		
						}
						else if(i == 77) {
							queryString3 = "SELECT TAX_CODE FROM FMS7_TAX_MST  WHERE TAX_ALIAS_CODE = ? ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, "TCS");
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								tax_code=rset3.getString(1);
							}
							rset3.close();
							stmt3.close();
							
							
							queryString3 = "SELECT FACTOR FROM FMS7_TAX_STRUCTURE_DTL  WHERE TAX_CODE = ? AND TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY APP_DATE DESC ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, tax_code);
							stmt3.setString(2, "22");
							stmt3.setString(3, inv_dt);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								value = rset3.getString(1);
								}
							else
							{
								value ="0.075";	
							}
							rset3.close();
							stmt3.close();
							str += value + ",";
						} 
						else if(i == 78) {
							tcs_tds="";

							queryString3="SELECT TCS_AMT,TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_INVOICE_TCS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG'";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, cd);
							stmt3.setString(2, inv_seq);
							stmt3.setString(3, fin_yr);
							stmt3.setString(4, cont_type);
							stmt3.setString(5, state_code);
							
							rset3 = stmt3.executeQuery();
							   if(rset3.next()) {
								   tcs_amt = rset3.getString(1);
								   tcs_date = rset3.getString(2);
								   tcs_tds = "TCS";								
							   }else {
								   queryString4="SELECT TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), TDS_AMT FROM FMS7_INVOICE_TDS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG'";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, cd);
									stmt4.setString(2, inv_seq);
									stmt4.setString(3, fin_yr);
									stmt4.setString(4, cont_type);
									stmt4.setString(5, state_code);
									
									rset4 = stmt4.executeQuery();
									   if(rset4.next()) {
										  tds_date = rset4.getString(1); 
										  tds_amt = rset4.getString(2); 
										  tcs_tds = "TDS";								
									   }else {
										   tcs_tds = "NA";
										   tds_date = null;
									   }								   								  
									stmt4.close();
									rset4.close();
									
									tcs_date = null;
									tcs_amt = null;
							   }								   								  
							stmt3.close();
							rset3.close();

//							cell.setCellValue("'"+tcs_tds+"'")
							tcs_tds = tcs_tds.replaceAll(",", " ");
							tcs_tds = tcs_tds.replaceAll("\n", " ");
							tcs_tds = tcs_tds.replaceAll("\r", " ");
							tcs_tds = tcs_tds.replaceAll("\"", " ");
							str += tcs_tds + ",";
						}
						else if(i == 79) {
							str += tcs_amt + ",";
						}
						else if(i == 82) {
							if(rset2.getString(63)!=null) {
								str += "S,";
							}else {
								str += null + ",";
							}
						}
						else if(i == 83) {	//HOLD_AMT
							queryString3 = "SELECT A.HOLD_AMOUNT FROM FMS8_PAY_RECV_DTL A WHERE A.COMMODITY_TYPE = 'DLNG' AND A.NEW_INV_SEQ_NO = ? "
									+ "AND A.CONTRACT_TYPE = 'S' AND A.REV_NO = (SELECT MAX(C.REV_NO) FROM FMS8_PAY_RECV_DTL C WHERE A.NEW_INV_SEQ_NO = C.NEW_INV_SEQ_NO "
									+ "AND A.CONTRACT_TYPE = C.CONTRACT_TYPE AND A.COMMODITY_TYPE = C.COMMODITY_TYPE)";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, inv_no);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								value = rset3.getString(1);
							}
							rset3.close();
							stmt3.close();
							
							str += value + ",";
						}
						
						else {
							str += value + ",";
						}
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
				}
				rset2.close();
				stmt2.close();
				
			}
			stmt.close();
			rset.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_FFLOW_INV_MST>>,,,,", conn);
			
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
		}
		
	}
	
	public void FMS_DLNG_FFLOW_INV_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_DLNG_FFLOW_INV_DTL()";	
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_FFLOW_INV_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_TYPE,INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,UNIT,QTY,RATE,AMOUNT,ENT_BY,ENT_DT,CARGO_NO,SAC_CD";
					
			count = 0;
			
			String fname_csv = "", str = "",sup_cd="";
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();

			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			String seq_no = "";

			logger.checkpoint(fname, "ABBR,CONT_TYPE,SEQ_NO,TIMESTAMP", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), B.CUSTOMER_CD, A.EFF_DT, A.COUNTERPARTY_CD FROM FMS9_COUNTERPTY_MST A, FMS7_CUSTOMER_MST B "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				String inv = "";
				int count_inv = 1;
				String line_dsc="",inv_type="",cur_flg="",agmt_no = "",agmt_rev="";
				
					queryString2 = " SELECT A.FINANCIAL_YEAR,B.CUSTOMER_CD,B.FGSA_NO,B.FGSA_REV_NO,B.SN_NO,B.SN_REV_NO,A.CONTRACT_TYPE,A.SUPPLIER_CD,A.SUPPLIER_STATE_CD,B.NEW_INV_SEQ_NO,NULL,NULL,"
							+ "'TLU',A.INV_SEQ_NO,B.NEW_INV_SEQ_NO,'1',A.ITEM_DESCRIPTION,B.INV_CUR_FLAG,A.QUANTITY,A.RATE,A.CARGO_AMOUNT,A.ENTER_BY,TO_CHAR(A.ENTER_DT , 'DD/MM/YYYY HH24:MI:SS'),NULL, A.SAC_CODE "
							+ "FROM FMS8_OTHER_INVOICE_DTL A, FMS7_INVOICE_MST B "
							+ "WHERE A.INV_SEQ_NO = B.HLPL_INV_SEQ_NO AND A.CONTRACT_TYPE = 'E' AND B.CUSTOMER_CD = ? AND B.SN_NO = '10722' "
							+ "AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.EFF_DT = B.INVOICE_DT AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.CONTRACT_TYPE = B.CONTRACT_TYPE ORDER BY A.INV_SEQ_NO ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset.getString(2));
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						str = "";
						abbr = rset.getString(1);	
						cd = rset2.getString(2);
						agmt_no = rset2.getString(3);
						agmt_rev = rset2.getString(4);
						cont_no = rset2.getString(5);
						cont_rev = rset2.getString(6);
						cont_type = rset2.getString(7);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					       
					    }
						
						str += abbr + ",";
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll("\n", " ");
							value = value.replaceAll(",", "_");
							value = value.replaceAll("\r", "");				
							
							
							if (i == 2) {
								if(Integer.parseInt(cont_no)<=9999) { 
									cont_ref = "C"+"-"+cd+"-"+agmt_no+"-"+cont_no+"-"+cont_rev;
								}
								else {
									cont_ref = "A"+"-"+cd+"-"+agmt_no+"-"+cont_no+"-"+cont_rev;
								}
								str += cont_ref + ",";
							}
							else if (i == 7) { // cont_type							
								if(cont_ref.startsWith("A")) {
									cont_type= "Q";
								}else if(cont_ref.startsWith("C")) {
									cont_type= "O";
								}
								str += cont_type + ",";
							}
							else if(i == 13) {
								line_dsc = rset2.getString(17);
								
								 if(line_dsc.contains("TLU")) {
									   inv_type = "TLU";
								   }
								str += inv_type + ",";
							}
							else if (i == 16) {
								if (inv.equals(rset2.getString(14))) {
									count_inv++;
								}
								else {
									count_inv = 1;
									inv = rset2.getString(14);
								}
								value = count_inv+"";
								str += value + ",";
							}
							else if(i == 18) {
								value=rset2.getString(18);
								value=value.equals("1")?"INR":"USD";
								str += value + ",";
							}
							else {
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
					
					// Late Payment
					inv = "";
					count_inv = 1;
					line_dsc="";inv_type="";
					
					queryString2 = " SELECT A.FINANCIAL_YEAR, A.CUSTOMER_CD, A.FGSA_NO, A.FGSA_REV_NO, A.SN_NO, A.SN_REV_NO, A.CONTRACT_TYPE, A.SUP_PLANT_CD+1, A.SUP_STATE_CODE, A.NEW_INV_SEQ_NO, NULL, NULL, "
							+ "'LP', A.HLPL_INV_SEQ_NO, A.NEW_INV_SEQ_NO, '1', A.REMARK_SPECIFICATION, A.PRICE_UNIT, A.TOTAL_QTY, A.SALE_PRICE, (A.SALE_PRICE * A.TOTAL_QTY), A.EMP_CD, TO_CHAR(A.ENT_DT , 'DD/MM/YYYY HH24:MI:SS'), '0', NULL "
							+ " FROM DLNG_INVOICE_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND "
							+ "A.REMARK_SPECIFICATION NOT IN (SELECT C.NEW_INV_SEQ_NO FROM DLNG_INVOICE_MST C WHERE A.REMARK_SPECIFICATION=C.NEW_INV_SEQ_NO AND C.CONTRACT_TYPE = 'V') AND "
							+ "A.CONTRACT_TYPE IN ('I', 'M') AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset.getString(4));
					stmt2.setString(2, delta_FromDt);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_ToDt);
					stmt2.setString(5, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						str = "";
						abbr = rset.getString(1);	
						cd = rset2.getString(2);
						agmt_no = rset2.getString(3);
						agmt_rev = rset2.getString(4);
						cont_no = rset2.getString(5);
						cont_rev = rset2.getString(6);
						cont_type = rset2.getString(7);
						map = "S-"+agmt_no+"-"+agmt_rev+"-"+cont_no+"-"+cont_rev;
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					       
					    }
						str += abbr + ",";
						
						queryString1 = "SELECT AGREEMENT_NO, REV_NO, CN_NO, CN_REV_NO FROM FMS8_LNG_REGAS_MST WHERE CUSTOMER_CD = ? AND CN_NO = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, cd);
						stmt1.setString(2, agmt_no);
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							agmt_no = rset1.getString(1);
							agmt_rev = rset1.getString(2);
							cont_no = rset1.getString(3);
							cont_rev = rset1.getString(4);
						}
						rset1.close();
						stmt1.close();	
						String inv_cur = "";
						
						for (int i = 1; i < columns.split(",").length; i++) {
//								cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll("\n", " ");
							value = value.replaceAll(",", "_");
							value = value.replaceAll("\r", "");				
							
							
								if (i == 2) {
//								queryString1 = "SELECT SN_REF_NO FROM FMS7_SN_MST WHERE FGSA_NO = ? AND FGSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? ";
//								stmt1 = conn.prepareStatement(queryString1);
//								stmt1.setString(1, rset2.getString(3));
//								stmt1.setString(2, rset2.getString(4));
//								stmt1.setString(3, rset2.getString(5));
//								stmt1.setString(4, rset2.getString(6));
//								rset1 = stmt1.executeQuery();
//		
//								if(rset1.next() && rset1.getString(1)!=null) {
//									value = rset1.getString(1);
//								}else {
//									value = rset2.getString(5)+"-"+rset2.getString(6);
//								}
//								stmt1.close();
//								rset1.close();
//								cont_ref = value;
								str += map + ",";
							}
							else if (i == 7) { // cont_type	
								  queryString1 = "SELECT CONTRACT_TYPE FROM DLNG_INVOICE_MST WHERE FINANCIAL_YEAR = ? AND  NEW_INV_SEQ_NO = ?";
								  stmt1 = conn.prepareStatement(queryString1);
								  stmt1.setString(1, "20"+rset2.getString(17).split("/")[1].split("-")[0]+"-20"+rset2.getString(17).split("/")[1].split("-")[1]);
								  stmt1.setString(2, rset2.getString(17));
								  rset1 = stmt1.executeQuery();
								  
//								  System.out.println("20"+rset2.getString(22).split("/")[1].split("-")[0]+"-20"+rset2.getString(22).split("/")[1].split("-")[1]);
								  if (rset1.next()) {
									  cont_type = rset1.getString(1);
								  }
								  stmt1.close();
								  rset1.close();
								  str += cont_type + ",";
								}
							else if (i == 17) {
								queryString1 = "SELECT TO_CHAR(INVOICE_DT, 'DD-MON-YY'), NVL(INV_CUR_FLAG, '1') FROM DLNG_INVOICE_MST WHERE FINANCIAL_YEAR = ? AND NEW_INV_SEQ_NO = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, "20"+rset2.getString(17).split("/")[1].split("-")[0]+"-20"+rset2.getString(17).split("/")[1].split("-")[1]);
								stmt1.setString(2, rset2.getString(17));
								rset1 = stmt1.executeQuery();
								if (rset1.next()) {
									value = "Delayed Payment Invoice Generated Against Invoice No. " + rset2.getString(17) + " dated " + rset1.getString(1);
									inv_cur = rset1.getString(2).equals("N") ? "1" : rset1.getString(2);
								}
								else {
									value = "Description.";
									inv_cur = "1"; // Rs
								}
								rset1.close();
								stmt1.close();
								
								str += value + ",";
							}
								
							else if(i == 18) {
								value=inv_cur;
//								System.out.println(inv_cur+"==="+rset2.getString(17));
								value=value.equals("1")?"INR":"USD";
								str += value + ",";
							}
							else {
//									cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
			}
			stmt.close();
			rset.close();
			
			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_FFLOW_INV_DTL>>,,,,", conn);

			logger.checkpoint1(fname1,count+",", conn);

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_DLNG_FFLOW_INV_FILE_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_DLNG_FFLOW_INV_FILE_DTL()";
		
		try {
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_FFLOW_INV_FILE_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,INVOICE_TYPE,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT";
			count = 0;
			
			String inv_seq= "",inv_dt="",ent_by="",ent_dt="",pdf_type="",file_nm="";
			String o_by= "",o_dt="",d_by="",d_dt="",t_by="",t_dt="";
			String fname_csv = "", str = "", state_tin="",new_inv="";
			
			
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_FILE_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,INVOICE_TYPE,TIMESTAMP,", conn);
			
			queryString = "SELECT DISTINCT(A.CUSTOMER_CD), A.EFF_DT, A.CUSTOMER_ABBR  FROM FMS7_CUSTOMER_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) "
					+ "ORDER BY A.CUSTOMER_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				cd=rset.getString(1);
				
				queryString2 = "SELECT CONTRACT_TYPE, HLPL_INV_SEQ_NO, FINANCIAL_YEAR, "
						+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), NEW_INV_SEQ_NO, SUP_STATE_CODE+1, "
						+ "PRINT_BY_ORI, TO_CHAR(PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "PRINT_BY_DUP, TO_CHAR(PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "PRINT_BY_TRI, TO_CHAR(PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS') "
						+ "FROM FMS7_INVOICE_MST WHERE CUSTOMER_CD = ? AND CONTRACT_TYPE = 'E' ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					rset2 = stmt2.executeQuery();
					
				while(rset2.next()) 
				{
					if(cd.equals("4")) {
						cont_type = rset2.getString(1);
						inv_seq = rset2.getString(2);
						fin_yr = rset2.getString(3);
						inv_dt = rset2.getString(4);
						inv_dt = inv_dt.replaceAll("/", "");
						new_inv = rset2.getString(5);
						state_tin = rset2.getString(6);
						o_by = rset2.getString(7);
						o_dt = rset2.getString(8);
						d_by = rset2.getString(9);
						d_dt = rset2.getString(10);
						t_by = rset2.getString(11);
						t_dt = rset2.getString(12);
						abbr = rset.getString(3);
					
						
						int index=0;
						queryString1 = "SELECT '2', NULL , NULL, NULL, 'TLU', INV_TYPE, PDF_INV_NM, NULL, NULL, NULL, NULL, PDF_SIGNED_FLAG, SIGNED_BY, "
								+ "TO_CHAR(SIGNED_DT, 'DD/MM/YYYY HH24:MI:SS') "
								+ " FROM FMS8_INV_PDF_DTL "
								+ " WHERE INV_TYPE IN ('O','D','T') AND PDF_INV_NM LIKE ? ";
								
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(++index,"%"+cont_type+"-"+fin_yr+"-"+inv_seq+"%");
						rset1 = stmt1.executeQuery();
						
						while(rset1.next())
						{	
							pdf_type = rset1.getString(6);
							String inv_type = rset1.getString(5);
							
							str = "";
							
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
							}
							
							str += abbr + ",";
							
							for (int i = 1; i < columns.split(",").length; i++) {
								
							 
							 if (i == 1) {	//FINANCIAL_YEAR
								value = fin_yr;
								str += value + ",";
							 }
							 else if(i == 2) {	//INVOICE_NO
								 str += inv_seq + ",";
							 }
							 else if(i == 3) {	//INVOICE_SEQ
									str += new_inv + ",";
							 }
							 else if (i == 6) {
								value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
								if(!value.contains(".pdf")) {
									value = value + ".pdf";
								}
								str += value + ",";
							 }
							 else if(i == 7) {	//ENT_BY
								if(pdf_type.equals("O")) {
									value = o_by;
							 }
								
							 else if(pdf_type.equals("D")) {
									value = d_by;
							 }
								
							 else if(pdf_type.equals("T")) {
									value = t_by;
							 }
								str += value + ",";
							 }
							
							else if(i == 8) {	//ENT_DT
								if(pdf_type.equals("O")) {
									value = o_dt;
								}
								
								else if(pdf_type.equals("D")) {
									value = d_dt;
								}
								
								else if(pdf_type.equals("T")) {
									value = t_dt;
								}
								str += value + ",";
							}
							
							else if(i == 12) {
								value = rset1.getString(13);
								str += value + ",";
							}
								
								else {
									value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
									str += value + ",";
								}
								
							}
							logger.insert_data(fname_csv, str, conn);
							count++;
							logger.data(fname, (company_cd+",24" + ","+inv_seq+ "," +fin_yr+","+pdf_type+","+inv_type+","), conn, "");
							
							
						}					
						stmt1.close();
						rset1.close();
					}
				}
				rset2.close();
				stmt2.close();
				
				//Late Payment
				cd = rset.getString(1);
				queryString2 = "SELECT CONTRACT_TYPE, HLPL_INV_SEQ_NO, FINANCIAL_YEAR, "
						+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), NEW_INV_SEQ_NO, SUP_STATE_CODE+1, "
						+ "PRINT_BY_ORI, TO_CHAR(PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "PRINT_BY_DUP, TO_CHAR(PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "PRINT_BY_TRI, TO_CHAR(PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS') "
						+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND CONTRACT_TYPE = 'I' ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					rset2 = stmt2.executeQuery();
					
				while(rset2.next()) 
				{
					
					cont_type = rset2.getString(1);
					inv_seq = rset2.getString(2);
					fin_yr = rset2.getString(3);
					inv_dt = rset2.getString(4);
					inv_dt = inv_dt.replaceAll("/", "");
					new_inv = rset2.getString(5);
					state_tin = rset2.getString(6);
					o_by = rset2.getString(7);
					o_dt = rset2.getString(8);
					d_by = rset2.getString(9);
					d_dt = rset2.getString(10);
					t_by = rset2.getString(11);
					t_dt = rset2.getString(12);
					abbr = rset.getString(3);
//					System.out.println(cont_type);
					
					int index=0;
					queryString1 = "SELECT '2', NULL , NULL, NULL, 'LP', INV_TYPE, PDF_INV_NM, NULL, NULL, NULL, NULL, PDF_SIGNED_FLAG, SIGNED_BY, "
							+ "TO_CHAR(SIGNED_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL "
							+ " FROM DLNG_INV_PDF_DTL "
							+ " WHERE INV_TYPE IN ('O','D','T') AND (PDF_INV_NM LIKE ? AND PDF_INV_NM LIKE ?)";
							
					stmt1 = conn.prepareStatement(queryString1);
					if(cont_type.equals("I")) {
						stmt1.setString(++index,"%-I-"+inv_seq+"%");
						stmt1.setString(++index,"%-"+inv_dt+"-%");
					}
					rset1 = stmt1.executeQuery();
					
					while(rset1.next())
					{	
						pdf_type = rset1.getString(6);
						String inv_type = rset1.getString(5);
						
						str = "";
						
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
						}
						
						str += abbr + ",";
						
						for (int i = 1; i < columns.split(",").length; i++) {
							
						 
						 if (i == 1) {	//FINANCIAL_YEAR
							value = fin_yr;
							str += value + ",";
						 }
						 else if(i == 2) {	//INVOICE_NO
							 str += inv_seq + ",";
						 }
						 else if(i == 3) {	//INVOICE_SEQ
							 str += new_inv + ",";
						 }
						 else if (i == 6) {
							value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
							if(!value.contains(".pdf")) {
								value = value + ".pdf";
							}
							str += value + ",";
						 }
						 else if(i == 7) {	//ENT_BY
							if(pdf_type.equals("O")) {
								value = o_by;
						 }
							
						 else if(pdf_type.equals("D")) {
								value = d_by;
						 }
							
						 else if(pdf_type.equals("T")) {
								value = t_by;
						 }
							str += value + ",";
						 }
						
						else if(i == 8) {	//ENT_DT
							if(pdf_type.equals("O")) {
								value = o_dt;
							}
							
							else if(pdf_type.equals("D")) {
								value = d_dt;
							}
							
							else if(pdf_type.equals("T")) {
								value = t_dt;
							}
							str += value + ",";
						}
						
						else if(i == 12) {
							value = rset1.getString(13);
							str += value + ",";
						}
							
							else {
								value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
								str += value + ",";
							}
							
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (company_cd+",24" + ","+inv_seq+ "," +fin_yr+","+pdf_type+","+inv_type+","), conn, "");
						
						
					}					
					stmt1.close();
					rset1.close();
				}
				rset2.close();
				stmt2.close();
				}
			rset.close();
			stmt.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_FFLOW_INV_FILE_DTL>>,,,,", conn);
			
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
		}
		
	}
	

	public void FMS_DLNG_SVC_INVOICE_MST() throws SQLException, IOException {
		int n=0;
		function_nm = "FMS_DLNG_SVC_INVOICE_MST()";
		try {
			
			System.out.println("<<START>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_SVC_INVOICE_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1, function_nm + "E" + "," + start_end_dt + ",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SELL_CONT_MAP,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ,"
					+ "CONTACT_PERSON_CD,INV_FLAG,INVOICE_SEQ,INVOICE_ID_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,QTY,QTY_UNIT,DIST_UNIT,SALE_PRICE,"
					+ "SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,EXCHG_RATE_TYPE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,"
					+ "INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,ITEM_DESCRIPTION,REMARK_1,REMARK_2,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,"
					+ "APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TCS_TDS,TCS_AMT,TCS_FACTOR,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,"
					+ "PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,"
					+ "PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,TCS_STRUCT_CD,TDS_STRUCT_CD,"
					+ "FIN_SYS,HOLD_AMT,SAC_CD";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			String temp = ""; 
//			nrow = 0;
//			ncell = 0;
			count = 0;
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			String map = "";
			String fin_year="",period_st="",period_end="",p_contact="",alloc_qty="",rate="",rate_unit="",inv_dt="",tax_dtl="",exch_cd="",tax_cd="",inv_no="",gross_amt_usd="",
					invoice_dt="",sac_cd="",plant_seq="",item_desc="";
			String exch_dt="",due_dt="",chk_dt="",auth_dt="",appr_dt="",ent_dt="",pay_recv_dt="",pay_insert_dt="",pay_update_dt="",print_dt_ori="",print_dt_tri="",print_dt_dup="",sap_appr_dt="";
			int bu_seq=0;
			NumberFormat nf = new DecimalFormat("###########0.00");
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_SVC_INVOICE_MST_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
//			logger.checkpoint(fname,"COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,GAS_DT,PLANT_SEQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT "
					+ "FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) { 
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.AGREEMENT_NO, A.AGREEMENT_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO "
						+ "FROM DLNG_SALES_TRANSPORTER_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND A.TRANS_CONT_START_DT IS NOT NULL AND A.TRANS_CONT_END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
//					map = "S-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.CUSTOMER_CD, A.FINANCIAL_YEAR, A.FGSA_NO, A.FGSA_REV_NO, A.SN_NO, A.SN_REV_NO, "
							+ "'B', TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY'), A.SUP_PLANT_CD, NULL, NULL, A.PLANT_SEQ_NO, A.CONTACT_PERSON_CD, 'F', "
							+ "A.HLPL_INV_SEQ_NO, A.HLPL_INV_SEQ_NO, A.NEW_INV_SEQ_NO, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), BILLING_CYCLE, "
							+ "TO_CHAR(A.PERIOD_START_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, A.INR_BASE, NULL, NULL, NULL, A.GROSS_AMT_USD, "
							+ "A.EXCHG_RATE_CD, TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EXCHG_RATE_VALUE, A.EXCHG_RATE_TYPE, NULL, A.GROSS_AMT_INR, "
							+ "A.TAX_AMT_INR, A.TAX_STRUCT_CD, A.NET_AMT_INR, A.NET_AMT_INR, NULL, NULL, A.REMARK_1, A.REMARK_2, A.CHECKED_FLAG, A.CHECKED_BY, "
							+ "TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.AUTHORIZED_FLAG, A.AUTHORIZED_BY, TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "A.APPROVED_FLAG, A.APPROVED_BY, TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, NULL, "
							+ "NULL, NULL, NULL, "//TCS DETAILS COLUMNS
							+ "A.PAY_RECV_AMT, TO_CHAR(A.PAY_RECV_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_INSERT_BY, "
							+ "TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_UPDATE_BY, TO_CHAR(A.PAY_UPDATE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "A.PAY_REMARK, "
							+ "A.TDS_PERCENT, A.TDS_TAX_AMT, A.TDS_TAX_PERCENT, A.TDS_TAX_AMT, "//TDS DETAILS COLUMNS
							+ "PDF_INV_DTL, A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_TRI, TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "A.PRINT_BY_DUP, TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), A.SUN_APPROVAL, A.SUN_APPROVAL_BY, "
							+ "TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, NULL,  "//TCS_TDS DETAILS
							+ " "//TAX_EFF_DT
							+ " "//TCS_TDS EFF_DT
							+ "A.SUN_APPROVAL, NULL, NULL "
							+ "FROM DLNG_INVOICE_MST A WHERE A.CONTRACT_TYPE ='V' AND A.CUSTOMER_CD = ? AND A.FGSA_NO = ? AND A.FGSA_REV_NO = ? AND A.SN_NO = ? AND "
							+ "A.SN_REV_NO = ? "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, no);
					stmt2.setString(3, rev);
					stmt2.setString(4, cont_no);
					stmt2.setString(5, cont_rev);
					stmt2.setString(6, delta_FromDt);
					stmt2.setString(7, delta_FromDt);
					stmt2.setString(8, delta_ToDt);
					stmt2.setString(9, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						inv_seq="";fin_yr="";
//						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						p_seq_no = rset2.getString(12);
						plant_seq = rset2.getString(12);
						
						abbr = rset.getString(1);
						cd = rset2.getString(1);
						cont_type = rset2.getString(7);
						invoice_dt = rset2.getString(8);
						bu_seq = rset2.getInt(9);
						p_contact = rset2.getString(13);
						inv_no = rset2.getString(17);
						inv_dt = rset2.getString(18);
						period_st = rset2.getString(20);
						period_end = rset2.getString(21);
						due_dt = rset2.getString(22);
						
						gross_amt_usd = rset2.getString(28);
						exch_cd = rset2.getString(29);
						exch_dt = rset2.getString(30);
						BigDecimal gross_amt = rset2.getBigDecimal(34);
						tax_cd = rset2.getString(36);
						chk_dt = rset2.getString(45);
						auth_dt = rset2.getString(48);
						appr_dt = rset2.getString(51);
						ent_dt = rset2.getString(53);
						
						pay_recv_dt = rset2.getString(60);
						pay_insert_dt = rset2.getString(62);
						pay_update_dt = rset2.getString(64);
						
						BigDecimal tds_percent = rset2.getBigDecimal(66);
						
						print_dt_ori = rset2.getString(72);
						print_dt_tri = rset2.getString(74);
						print_dt_dup = rset2.getString(76);
						sap_appr_dt = rset2.getString(79);
						
						
						fin_yr = rset2.getString(2);
						inv_seq = rset2.getString(15);
						state_code = "0";
						
						
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
//						map = abbr + "-" + p_seq_no;
						if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
							p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
						}
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr = mpe.counterparty_map.get(abbr);
						}
//						cell = row.createCell(ncell++);
//						cell.setCellValue("'" + abbr + "'");
						str += abbr + ",";
						p_seq_no = p_seq_no.split("-")[0];
						map = abbr+"-"+no+"-"+rev+"-"+plant_seq+"-"+cont_no+"-"+cont_rev;
//						p_seq_no = abbr + "-" + p_seq_no;
						
						
						for (int i = 1; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							value = value.replaceAll(". ", " "); 
							value = value.replaceAll("\r", " "); 
							value = value.replaceAll("\n", " "); 
							value = value.replaceAll(",", " "); 
							
							if (i == 1) { //COUNTERPARTY_CD		
								
								str += map + ",";
							}
							
//							else if(i == 7) {	//CONTRACT_TYPE
//								value = cont_type;
//								str += value + ",";
//							}
							
							else if(i == 9) {	//BU_UNIT
								value = (bu_seq+1)+"";
								str += value + ",";
							}
							
							else if (i == 12) { //PLANT_SEQ					
//								p_seq_no = p_seq_no.split("-")[0];
//								cell.setCellValue("'" + p_seq_no + "'");
								str += p_seq_no + ",";
							}
							
							else if(i == 13) {	//CONTACT_PERSON_CD
								if(p_contact!=null) 
								{
									queryString3 = "SELECT EMAIL FROM FMS7_CUSTOMER_CONTACT_MST WHERE CUSTOMER_CD = ? AND SEQ_NO = ? ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, cd);
									stmt3.setString(2, p_contact);
								}
								else {
									queryString3 = "SELECT EMAIL FROM FMS7_CUSTOMER_CONTACT_MST WHERE CUSTOMER_CD = ? AND ADDR_FLAG = ? ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, cd);
									stmt3.setString(2, "P"+plant_seq);
								}
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								else {
									value = "X";
								}
								rset3.close();
								stmt3.close();
								
								str += value + ",";
							}

							else if(i == 18) {	//INVOICE_DT
								value = inv_dt;
								str += value + ",";
							}

							else if(i == 20) {	//PERIOD_START_DT
								value = period_st;
								str += value + ",";
							}

							else if(i == 21) {	//PERIOD_END_DT
								value = period_end;
								str += value + ",";
							}
							
							else if(i == 22) {	//DUE_DT
								value = due_dt;
								str += value + ",";
							}
							
							else if(i == 23) {	//QTY
								queryString3 = "SELECT COUNT(*) FROM DLNG_SERVICE_INVOICE_ATTACH WHERE SERVICE_INVOICE_NO = ? AND SERVICE_INV_DT = TO_DATE(?,'DD/MM/YYYY') ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, inv_no);
								stmt3.setString(2, invoice_dt);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								str += value + ",";
							}
							
							else if(i == 24) {	//QTY_UNIT
								if(value.equals("3")) {
									value = "0";
								}
								else {
									value = "X";
								}
								str += value + ",";
							}
							
							else if(i == 26) {	//SALE_PRICE
								queryString3 = "SELECT AMOUNT, RATE FROM DLNG_SERVICE_INVOICE_ATTACH WHERE SERVICE_INVOICE_NO = ? AND SERVICE_INV_DT = TO_DATE(?,'DD/MM/YYYY') ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, inv_no);
								stmt3.setString(2, invoice_dt);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									rate = rset3.getString(1);
									rate_unit = rset3.getString(2);
								}
								rset3.close();
								stmt3.close();
								str += rate + ",";
							}
							
							else if(i == 27) { //SALE_PRICE_UNIT
								str += rate_unit + ",";
							}
							
							else if(i == 28) {	//SALE_AMT
								if(gross_amt_usd.equals("0")) {
									value = gross_amt+"";
								}
								else {
									value = "0.00";
								}
								str += value + ",";
							}
							
							else if(i == 29) {	//EXCHG_RATE_CD
								queryString3 = "SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, exch_cd);
								rset3 = stmt3.executeQuery();
								 if(rset3.next()) {	
									   name = rset3.getString(1);
									   name= name.toUpperCase();
									   if (name.contains("CUSTOMS RATE")) {
											name = "CUSTOM EXCHANGE RATE";
										}
										else if (name.contains("RBI REFERENCE")) {
											name = "RBI REFERENCE RATE";
										}
										else if (name.contains("SBI MUMBAI TT AVERAGE")) {
											name = "SBI MUMBAI TT BUY SELL";
										}
										else if (name.contains("SBI TT BUYING")) {
											name = "SBI RATE BUY";
										}
										else if (name.contains("SBI TT SELLING")) {
											name = "SBI RATE SELL";
										}
										else if (name.contains("SBI TT BUY SELL")) {
											name = "SBI RATE BUY SELL";
										}								
								   }
								 else {
									 name = exch_cd;
								 }
								rset3.close();
								stmt3.close();
								value = name;
								str += value + ",";
							}
							
							else if(i == 30) {	//EXCHG_RATE_DT
								value = exch_dt;
								str += value + ",";
							}
							
							else if(i == 33) { 	//INVOICE_RAISED_IN
								if(gross_amt_usd.equals("0")) {
									value = "1";
								}
								str += value + ",";	
							}
							
							else if(i == 36) {	//TAX_STRUCT_CD
								queryString3 = "SELECT TAX_DETAILS,ITEM_DESCRIPTION,SAC_CODE FROM DLNG_SERVICE_INVOICE_DTL WHERE FINANCIAL_YEAR = ? AND INV_SEQ_NO = ? AND CONTRACT_TYPE = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, fin_yr);
								stmt3.setString(2, inv_seq);
								stmt3.setString(3, "V");
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									tax_dtl = rset3.getString(1);
									item_desc = rset3.getString(2);
									sac_cd = rset3.getString(3);
								}
								else {
									tax_dtl = "CGST-9.0%SGST-9.0%";
									item_desc = "Transport Management Services Charge";
									sac_cd = "998599";
								}
								rset3.close();
								stmt3.close();
								
								if(tax_dtl!=null && tax_dtl.contains("%")) 
								{
									tax_dtl = tax_dtl.replaceAll("-", " ");
									tax_dtl = tax_dtl.replaceAll(".0", "");
									tax_dtl = tax_dtl.split("%")[0] +"%@ " + tax_dtl.split("%")[1] + "%"; 
								}
								
								str += tax_dtl + ",";
								
							}
							
							else if(i == 40) {	//ITEM_DESCRIPTION
								str += item_desc + ",";
							}
							
							else if(i == 41) {	//REMARK_1
								value = rset2.getString(41);
								if (value!=null) {
									value = value.replaceAll(",", " ");
									value = value.replaceAll("\n", " ");
									value = value.replaceAll("\r", " ");
									value = value.replaceAll("\t", " ");
								}
								str += value + ",";
							}
							
							else if(i == 42) {	//REMARK_2
								value = rset2.getString(42);
								if(value!=null) {
								value = value.replaceAll(",", " ");
								value = value.replaceAll("\n", " ");
								value = value.replaceAll("\r", " ");
								value = value.replaceAll("\t", " ");
								}
								str += value + ",";
							}
							
							else if(i == 45) {	//CHECKED_DT
								value = chk_dt;
								str += value + ",";
							}

							else if(i == 48) {	//AUTHORIZED_DT
								value = auth_dt;
								str += value + ",";
							}

							else if(i == 51) {	//APPROVED_DT
								value = appr_dt;
								str += value + ",";
							}
							
							else if(i == 53) {	//ENT_DT
								value = ent_dt;
								str += value + ",";
							}
							else if(i == 56) {	//TCS_TDS
								tcs_tds="";
								
								if(tds_percent!= null) {
									 tcs_tds = "TDS";	
								}
								else {

//								queryString3="SELECT TCS_AMT,TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_INVOICE_TCS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG'";
//								stmt3 = conn.prepareStatement(queryString3);
//								stmt3.setString(1, cd);
//								stmt3.setString(2, inv_seq);
//								stmt3.setString(3, fin_yr);
//								stmt3.setString(4, "S");
//								stmt3.setString(5, state_code);
//								
//								rset3 = stmt3.executeQuery();
//								   if(rset3.next()) {
//									   tcs_amt = rset3.getString(1);
//									   tcs_date = rset3.getString(2);
//									   tcs_tds = "TCS";								
//								   }
//								   else {
//									   tcs_amt = "";
//									   tcs_date = "";
//									   tcs_tds = "NA";
//								   }
//								stmt3.close();
//								rset3.close();
									tcs_tds = "NA";
								}
	
//								cell.setCellValue("'"+tcs_tds+"'")
								tcs_tds = tcs_tds.replaceAll(",", " ");
								tcs_tds = tcs_tds.replaceAll("\n", " ");
								tcs_tds = tcs_tds.replaceAll("\r", " ");
								tcs_tds = tcs_tds.replaceAll("\"", " ");
								str += tcs_tds + ",";
							
							}
							else if(i == 57) {	//TCS_AMT
								if(tcs_tds.equals("TCS")) {
									str += tcs_amt + ",";
								}
								else {
									str += null + ",";
								}
							}
							else if(i == 58 && !tcs_tds.equals("NA") && !tcs_tds.equals("TDS")) {	//TCS_FACTOR
								queryString3 = "SELECT TAX_CODE FROM FMS7_TAX_MST  WHERE TAX_ALIAS_CODE = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, "TCS");
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									tax_code=rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								
								queryString3 = "SELECT FACTOR FROM FMS7_TAX_STRUCTURE_DTL  WHERE TAX_CODE = ? AND TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY APP_DATE DESC ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, tax_code);
								stmt3.setString(2, "22");
								stmt3.setString(3, inv_dt);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
									}
								else
								{
									value ="0.075";	
								}
								rset3.close();
								stmt3.close();
								str += value + ",";
							} 
							else if(i == 60) {	//PAY_RECV_DT
								value = pay_recv_dt;
								str += value + ",";
							}
							
							else if(i == 62) {	//PAY_INSERT_DT
								value = pay_insert_dt;
								str += value + ",";
							}
							
							else if(i == 64) {	//PAY_UPDATE_DT
								value = pay_update_dt;
								str += value + ",";
							}
							
							else if(i == 65) {	//PAY_REMARK
								value  = rset2.getString(66);
								if(value!=null) {
									value = value.replaceAll(",", "");
								}
								str += value + ",";
							}

							else if(i == 66){ 	//TDS_GROSS_PERCENT
								if(tcs_tds.equals("TDS")) {
									value = tds_percent+"";
								}
								else {
									value = null;
								}
								str += value + ",";
							}
							
							else if(i == 67) {	 //TDS_GROSS_AMT
								if(tds_percent!=null && tcs_tds.equals("TDS")) {
									Double result1 = (gross_amt.multiply(tds_percent)).divide(BigDecimal.valueOf(100)).doubleValue();
									value = result1.toString();
								}
								else {
									value = null;
								}
								
								str += value + ",";
							}
							
//							else if(i == 68){ 	//TDS_TAX_PERCENT
//								if(tcs_tds.equals("TDS")) {
//									value = tds_percent+"";
//								}
//								else {
//									value = null;
//								}
//								str += value + ",";
//							}
//
//							else if(i == 69) {	 //TDS_TAX_AMT
//								if(tds_percent!=null && tcs_tds.equals("TDS")) {
//									Double result1 = (gross_amt.multiply(tds_percent)).divide(BigDecimal.valueOf(100)).doubleValue();
//									value = result1.toString();
//								}
//								else {
//									value = null;
//								}
//								str += value + ",";
//							}
							
							else if(i == 72) {	//PRINT_DT_ORI
								value = print_dt_ori;
								str += value + ",";
							}
							
							else if(i == 74) {	//PRINT_DT_TRI
								value = print_dt_tri;
								str += value + ",";
							}
							
							else if(i == 76) {	//PRINT_DT_DUP
								value = print_dt_dup;
								str += value + ",";
							}
							
							else if(i == 79) {	//SAP_APPROVED_DT
								value = sap_appr_dt;
								str += value + ",";
							}
							
							else if(i == 82) {	//FIN_SYS
								if(sap_appr_dt!=null) {
									value = "S";
								}
								else {
									value = null;
								}
								str += value + ",";
							}
							
//							else if(i == 90) {	//HOLD_AMT
//								queryString3 = "SELECT A.HOLD_AMOUNT FROM FMS8_PAY_RECV_DTL A WHERE A.COMMODITY_TYPE = 'DLNG' AND A.NEW_INV_SEQ_NO = ? "
//										+ "AND A.CONTRACT_TYPE = 'S' AND A.REV_NO = (SELECT MAX(C.REV_NO) FROM FMS8_PAY_RECV_DTL C WHERE A.NEW_INV_SEQ_NO = C.NEW_INV_SEQ_NO "
//										+ "AND A.CONTRACT_TYPE = C.CONTRACT_TYPE AND A.COMMODITY_TYPE = C.COMMODITY_TYPE)";
//								stmt3 = conn.prepareStatement(queryString3);
//								stmt3.setString(1, inv_no);
//								rset3 = stmt3.executeQuery();
//								if(rset3.next()) {
//									value = rset3.getString(1);
//								}
//								rset3.close();
//								stmt3.close();
//								
//								str += value + ",";
//							}
							
							else if(i == 84) {	//SAC_CD
								str += sac_cd + ",";
							}
							
							
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
//						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_" + start_end_dt + ".xlsx";
			
//			fileOut = new FileOutputStream(filename);
			
//			workbook.write(fileOut);
//			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_SVC_INVOICE_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1, count + ",", conn);
			
			System.out.println("<<END>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			System.out.println();
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	

	public void FMS_DLNG_SVC_INVOICE_DTL() throws SQLException, IOException {
		int n=0;
		function_nm = "FMS_DLNG_SVC_INVOICE_DTL()";
		try {
			
			System.out.println("<<START>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_SVC_INVOICE_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1, function_nm + "E" + "," + start_end_dt + ",", conn);
			
			columns = "COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,SVC_INVOICE_SEQ,SVC_INVOICE_NO,"
					+ "INVOICE_SEQ,INVOICE_NO,TRUCK_TRANS_CD,TRUCK_CD,MAPPING_ID,PERIOD_START_DT,PERIOD_END_DT,QTY_MMBTU,DISTANCE,ENT_BY,ENT_DT";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
//			nrow = 0;
//			ncell = 0;
			count = 0;
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			String map = "";
			String inv_dt="",inv_no="",ent_by="",ent_dt="",sell_inv_no="",sell_inv_qty="",plant_seq="";
			int bu_seq=0;
			NumberFormat nf = new DecimalFormat("###########0.00");
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_SVC_INVOICE_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
//			logger.checkpoint(fname,"COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,GAS_DT,PLANT_SEQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT "
					+ "FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) { 
				map = "";
				queryString1 = "SELECT A.CUSTOMER_CD, A.AGREEMENT_NO, A.AGREEMENT_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO "
						+ "FROM DLNG_SALES_TRANSPORTER_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND A.TRANS_CONT_START_DT IS NOT NULL AND A.TRANS_CONT_END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					map = "";
					cd = rset1.getString(1);
					no = rset1.getString(2);
					rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
//					map = "S-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
					
					queryString2 = "SELECT A.FINANCIAL_YEAR, A.CUSTOMER_CD, A.FGSA_NO, A.FGSA_REV_NO, A.SN_NO, A.SN_REV_NO, "
							+ "'B', A.SUP_PLANT_CD,  A.PLANT_SEQ_NO,"
							+ "A.HLPL_INV_SEQ_NO, A.NEW_INV_SEQ_NO, NULL, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY'), NULL, NULL, NULL, NULL, "
							+ "NULL, NULL, NULL, A.EMP_CD, TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_INVOICE_MST A WHERE A.CONTRACT_TYPE ='V' AND A.CUSTOMER_CD = ? AND A.FGSA_NO = ? AND A.FGSA_REV_NO = ? AND A.SN_NO = ? AND "
							+ "A.SN_REV_NO = ? "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					stmt2.setString(2, no);
					stmt2.setString(3, rev);
					stmt2.setString(4, cont_no);
					stmt2.setString(5, cont_rev);
					stmt2.setString(6, delta_FromDt);
					stmt2.setString(7, delta_FromDt);
					stmt2.setString(8, delta_ToDt);
					stmt2.setString(9, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						inv_seq="";fin_yr="";
//						map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						p_seq_no = rset2.getString(9);
						plant_seq = rset2.getString(9);
						
						abbr = rset.getString(1);
						cd = rset2.getString(2);
						cont_type = rset2.getString(7);
						bu_seq = rset2.getInt(8);
						inv_no = rset2.getString(11);
						inv_dt = rset2.getString(13);
						ent_by = rset2.getString(21);
						ent_dt = rset2.getString(22);
						
						fin_yr = rset2.getString(1);
						inv_seq = rset2.getString(10);
						state_code = "0";
						
						queryString4 = "SELECT TRUCK_INV_NO, INVOICE_QTY FROM DLNG_SERVICE_INVOICE_ATTACH WHERE SERVICE_INVOICE_NO = ? AND SERVICE_INV_DT = TO_DATE(?,'DD/MM/YYYY') ";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, inv_no);
						stmt4.setString(2, inv_dt);
						rset4 = stmt4.executeQuery();
						while(rset4.next()) {
							
							sell_inv_no = rset4.getString(1);
							sell_inv_qty = nf.format(rset4.getDouble(2));
							
							
							//row = spreadsheet.createRow(nrow++);
	//						ncell = 0;
							str = "";
							
	//						map = abbr + "-" + p_seq_no;
							if (mpe.customer_map.containsKey(abbr + "-" + p_seq_no)) {
								p_seq_no = mpe.customer_map.get(abbr + "-" + p_seq_no);
							}
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr = mpe.counterparty_map.get(abbr);
							}
	//						cell = row.createCell(ncell++);
	//						cell.setCellValue("'" + abbr + "'");
							str += abbr + ",";
							p_seq_no = p_seq_no.split("-")[0];
							map = abbr+"-"+no+"-"+rev+"-"+plant_seq+"-"+cont_no+"-"+cont_rev;
	//						p_seq_no = abbr + "-" + p_seq_no;
							
							
							for (int i = 1; i < columns.split(",").length; i++) {
	//							cell = row.createCell(ncell++);
								value = rset2.getString(i) == null ? "null" : rset2.getString(i);
								value = value.replaceAll(". ", " "); 
								value = value.replaceAll("\r", " "); 
								value = value.replaceAll("\n", " "); 
								value = value.replaceAll(",", " "); 
								
								if (i == 2) { //COUNTERPARTY_CD		
									str += map + ",";
								}
								
	//							else if(i == 7) {	//CONTRACT_TYPE
	//								value = cont_type;
	//								str += value + ",";
	//							}
								
								else if(i == 8) {	//BU_UNIT
									value = (bu_seq+1)+"";
									str += value + ",";
								}
								
								else if(i == 9) {	//BU_STATE_TIN
									value = null; 
									str += value + ",";
								}
								
								else if(i == 12) {	//INVOICE_SEQ
									value = sell_inv_no;
									str += value + ",";
								}
								
								else if(i == 13) {	//INVOICE_NO
									value = sell_inv_no;
									str += value + ",";
								}
	
								else if(i == 19) {	//QTY_MMBTU
									value = sell_inv_qty;
									str += value + ",";
								}
								
								else if(i == 21) {	//ENT_BY
									value = ent_by;
									str += value + ",";
								}
								
								else if(i == 22) {	//ENT_DT
									value = ent_dt;
									str += value + ",";
								}
								
								else {
	//								cell.setCellValue("'" + value + "'");
									str += value + ",";
								}
							}
							logger.insert_data(fname_csv, str, conn);
							count++;
	//						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_type + "," + gas_dt + "," + p_seq_no + ","+ cont_type + ","), conn, "");
					}
					rset4.close();
					stmt4.close();
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_" + start_end_dt + ".xlsx";
			
//			fileOut = new FileOutputStream(filename);
			
//			workbook.write(fileOut);
//			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_SVC_INVOICE_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1, count + ",", conn);
			
			System.out.println("<<END>><<" + function_nm.substring(0, function_nm.length() - 2) + ">>");
			System.out.println();
			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		} catch (Exception e) {
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	

	public void FMS_DLNG_SVC_INV_FILE_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_DLNG_SVC_INV_FILE_DTL()";
		
		try {
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_SVC_INV_FILE_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT";
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			
//			nrow = 0;
//			ncell = 0;
			count = 0;
			String inv_seq= "",inv_dt="",ent_by="",ent_dt="",pdf_type="",file_nm="",sign_flag="",invoice_dt="";
			String o_by= "",o_dt="",d_by="",d_dt="",t_by="",t_dt="";
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "", gas_dt="";
			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_SVC_INV_FILE_DTL_" + start_end_dt + ".csv";
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
			
			queryString = "SELECT DISTINCT(A.CUSTOMER_CD), A.EFF_DT  FROM FMS7_CUSTOMER_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) "
					+ "ORDER BY A.CUSTOMER_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				cd=rset.getString(1);
				
					queryString2 = "SELECT CONTRACT_TYPE, HLPL_INV_SEQ_NO, FINANCIAL_YEAR, TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), "
							+ "PRINT_BY_ORI, TO_CHAR(PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "PRINT_BY_DUP, TO_CHAR(PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "PRINT_BY_TRI, TO_CHAR(PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND CONTRACT_TYPE = 'V' ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					rset2 = stmt2.executeQuery();
				while(rset2.next()) 
				{
					cont_type = rset2.getString(1);
					inv_seq = rset2.getString(2);
					fin_yr = rset2.getString(3);
					inv_dt = rset2.getString(4);
					o_by = rset2.getString(5);
					o_dt = rset2.getString(6);
					d_by = rset2.getString(7);
					d_dt = rset2.getString(8);
					t_by = rset2.getString(9);
					t_dt = rset2.getString(10);
					
					invoice_dt = inv_dt.replaceAll("/", "");
					
					queryString1 = "SELECT '2', '24', NULL, NULL, INV_TYPE, PDF_INV_NM, NULL, NULL, NULL, NULL, PDF_SIGNED_FLAG, SIGNED_BY, "
							+ "TO_CHAR(SIGNED_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL "
							+ " FROM DLNG_INV_PDF_DTL "
							+ " WHERE PDF_INV_NM LIKE ? AND INV_TYPE IN ('O','D','T') ";
//							+ "AND TO_DATE(CREATED_DT, 'DD/MM/YYYY') = TO_DATE(?, 'DD/MM/YYYY') ";
//							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
					
					stmt1 = conn.prepareStatement(queryString1);	
					stmt1.setString(1, "%"+invoice_dt+"%"+cont_type+"-"+inv_seq);
//					stmt1.setString(2, inv_dt);
//					stmt1.setString(2, delta_FromDt);
//					stmt1.setString(3, delta_FromDt);
//					stmt1.setString(4, delta_ToDt);
//					stmt1.setString(5, delta_ToDt);
					rset1 = stmt1.executeQuery();
					
//					System.out.println(inv_dt+"=="+"%-"+cont_type+"-"+inv_seq);
					while(rset1.next())
					{	
//						System.out.println(">>>");
						pdf_type = rset1.getString(5);
						file_nm = rset1.getString(6);
						sign_flag = rset1.getString(11);
						
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						
						for (int i = 0; i < columns.split(",").length; i++) {
							
						if(i == 2) {	//INVOICE_SEQ
							value = inv_seq;
//							cell= row.createCell(i);
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
							
						else if (i == 3) {	//FINANCIAL_YEAR
							value = fin_yr;
//							cell= row.createCell(i);
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if (i == 5) {
							value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
							if(!value.contains(".pdf")) {
								value = value +"-"+pdf_type +".pdf";
							}
							if(sign_flag !=null && sign_flag.equals("Y")) {
								value = "SERVICE_" +value;
							}
							str += value + ",";
						 }
						else if(i == 6) {	//ENT_BY
							if(pdf_type.equals("O")) {
								value = o_by;
							}
							
							else if(pdf_type.equals("D")) {
								value = d_by;
							}
							
							else if(pdf_type.equals("T")) {
								value = t_by;
							}
//							cell= row.createCell(i);
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
						else if(i == 7) {	//ENT_DT
							if(pdf_type.equals("O")) {
								value = o_dt;
							}
							
							else if(pdf_type.equals("D")) {
								value = d_dt;
							}
							
							else if(pdf_type.equals("T")) {
								value = t_dt;
							}
//							cell= row.createCell(i);
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
						else if(i == 11) {
							value = rset1.getString(12);
//							cell = row.createCell(i);																					
//							cell.setCellValue("'" + value + "'");
							str += value + ",";
						}
							
							else {
								value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
//								cell = row.createCell(i);																					
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
							
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (company_cd+",24" + ","+inv_seq+ "," +fin_yr+","+pdf_type+","+file_nm+","), conn, "");
						
						
					}					
					stmt1.close();
					rset1.close();
				}
				rset2.close();
				stmt2.close();
				}
			rset.close();
			stmt.close();
			
			
			
			
//			filename = migration_setup_dir + "EXPORT/FMS_DLNG_SVC_INV_FILE_DTL_"+start_end_dt+".xlsx";
			
//			fileOut = new FileOutputStream(filename);  
			
//			workbook.write(fileOut);
//			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_SVC_INV_FILE_DTL>>,,,,", conn);
			
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
		}
		
	}
	

	public void FMS_DLNG_INVOICE_MST_CR_DR() throws SQLException, IOException {
		function_nm = "FMS_DLNG_INVOICE_MST_CR_DR()";
		
		try {
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_INVOICE_MST_CR_DR>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRUCK_TRANS_CD,TRUCK_CD,MAPPING_ID,BU_UNIT,BU_STATE_TIN,"
					+ "BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INV_FLAG,INVOICE_SEQ,INVOICE_ID_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,"
					+ "DUE_DT,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,EXCHG_RATE_TYPE,INVOICE_RAISED_IN,"
					+ "GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT_1,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,REMARK_1,REMARK_2,CHECKED_FLAG,"
					+ "CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,"
					+ "MODIFY_BY,MODIFY_DT,TCS_TDS,TCS_AMT,TCS_FACTOR,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,"
					+ "PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,"
					+ "PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,TCS_STRUCT_CD,TCS_EFF_DT_1,TDS_STRUCT_CD,TDS_EFF_DT_1,TAX_EFF_DT,"
					+ "TCS_EFF_DT,TDS_EFF_DT,CHKPOST_CD,DRIVER_CD,FIN_SYS,HOLD_AMT,REF_NO,CRITERIA";

			count = 0;
			String fname_csv = "", str = "",cust_cd="",inv_no="",dr_cr_ref="",dr_cr_flg="";
			String invoice_no = "",mail="",dr_cr_tcs_flag="",tds_percent="",item_desc="",ori_tds_percent="";
			String criteria="",pr_strt_dt="",pr_end_dt="",gross_amt_final="",gross_amt_inr="",pdf_inv="",tcs_factor="";
			double  new_qty=0,sale_price=0,net_amt_in=0,prev_qty=0,sale_amt=0,gross_amt=0,tds_gross=0,drcr_price=0;

			fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_INVOICE_MST_CR_DR_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COMPANY_CD,DR_CR_REF_NO,DR_CR_SEQ,FINANCIAL_YEAR,INVOICE_NO,INVOICE_SEQ,TIMESTAMP,", conn);
			
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), B.CUSTOMER_CD, A.EFF_DT, A.COUNTERPARTY_CD FROM FMS9_COUNTERPTY_MST A, FMS7_CUSTOMER_MST B "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				cd=rset.getString(2);
				
				String queryString2 ="SELECT A.DR_CR_FIN_YEAR, A.FGSA_NO, A.FGSA_REV_NO, A.SN_NO, A.SN_REV_NO, A.CONTRACT_TYPE, NULL, B.TRUCK_ID, NULL, B.SUP_PLANT_CD + 1, A.SUP_STATE_CODE,"//11
						+ " NULL, A.PLANT_SEQ_NO, B.CONTACT_PERSON_CD, A.DR_CR_FLAG, A.HLPL_INV_SEQ_NO, A.DR_CR_NO, A.DR_CR_DOC_NO, TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), NULL, TO_CHAR(B.PERIOD_START_DT,'DD/MM/YYYY'), TO_CHAR(B.PERIOD_END_DT,'DD/MM/YYYY'),"//22
						+ " TO_CHAR(A.DUE_DT,'DD/MM/YYYY'), NVL(A.TOTAL_QTY,0), NVL(A.DR_CR_SALE_RATE-A.SALE_PRICE,0), NULL, NVL(A.DR_CR_GROSS_AMT_USD - NVL(A.GROSS_AMT_USD,0), A.GROSS_AMT_USD), B.EXCHG_RATE_CD, TO_CHAR(B.EXCHG_RATE_DT,'DD/MM/YYYY'),"//29
						+ " NVL(A.DR_CR_EXG_RATE-A.EXCHG_RATE_VALUE,0), NULL, NULL, NVL(A.DIFF_AMT,0), NVL(A.TAX_REMARK-A.TAX_AMT_INR,0), A.TAX_STRUCT_CD, NULL, NVL(A.DR_CR_NET_AMT_INR,0), NVL(A.DR_CR_NET_AMT_INR,0), NULL, A.REMARK, A.ITEM_DESCRIPTION,"//41
						+ " B.CHECKED_FLAG, B.CHECKED_BY, TO_CHAR(B.CHECKED_DT,'DD/MM/YYYY'), B.AUTHORIZED_FLAG, B.AUTHORIZED_BY, TO_CHAR(B.AUTHORIZED_DT,'DD/MM/YYYY'), B.APPROVED_FLAG, A.APRV_BY, TO_CHAR(A.APRV_DT,'DD/MM/YYYY'),"//60
						+ " A.EMP_CD, TO_CHAR(A.ENT_DT,'DD/MM/YYYY'), NULL, NULL, A.DR_CR_TCS_FLAG, NULL, NULL,  A.PAY_RECV_AMT, TO_CHAR(A.PAY_RECV_DT,'DD/MM/YYYY'), A.PAY_INSERT_BY, TO_CHAR(A.PAY_INSERT_DT,'DD/MM/YYYY'),"//71
						+ " A.PAY_UPDATE_BY, TO_CHAR(A.PAY_UPDATE_DT,'DD/MM/YYYY'), A.PAY_REMARK, A.TDS_PERCENT, NULL, A.TDS_TAX_PERCENT, A.TDS_TAX_AMT, "//78
						+ " B.PDF_INV_DTL, B.PRINT_BY_ORI, TO_CHAR(B.PRINT_DT_ORI,'DD/MM/YYYY'), B.PRINT_BY_TRI, TO_CHAR(B.PRINT_DT_TRI,'DD/MM/YYYY'), B.PRINT_BY_DUP, TO_CHAR(B.PRINT_DT_DUP,'DD/MM/YYYY'),"
						+ " A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT,'DD/MM/YYYY'), NULL, NULL, NULL, NULL, NULL, "
						+ "NULL, NULL, NULL, NULL, NULL, NULL, B.NEW_INV_SEQ_NO,A.CRITERIA, "//91
						+ "A.SALE_PRICE,A.EXCHG_RATE_VALUE,A.DIFF_QTY,A.GROSS_AMT_INR,A.TAX_REMARK,A.NET_AMT_INR,NVL(A.DR_CR_NET_AMT_INR-A.TAX_AMT_INR,0),"//98
						+ "A.DR_CR_QTY,A.DR_CR_GROSS_AMT_INR,A.DR_CR_SALE_RATE,NVL(A.TAX_REMARK,0),B.TDS_PERCENT " //103
						+ "FROM DLNG_DR_CR_NOTE A, DLNG_INVOICE_MST B " 
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD " 
						+ "AND A.FGSA_NO = B.FGSA_NO " 
						+ "AND A.SN_NO = B.SN_NO " 
						+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE " 
						+ "AND A.HLPL_INV_SEQ_NO = B.HLPL_INV_SEQ_NO " 
						+ "AND A.CUSTOMER_CD = ? "
						+ "AND A.CRITERIA NOT IN ('REV_INV--') "
						+ "AND A.CONTRACT_TYPE IN ('S','L') " 
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) " 
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";

			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, cd);
			stmt2.setString(2, delta_FromDt);
			stmt2.setString(3, delta_FromDt);
			stmt2.setString(4, delta_ToDt);
			stmt2.setString(5, delta_ToDt);
			rset2 = stmt2.executeQuery();
			
			while (rset2.next()) {
				abbr = rset.getString(1);
				cust_cd = rset.getString(2);
				
					fin_yr = rset2.getString(1);
					no = rset2.getString(2);
					cont_no = rset2.getString(4);
					cont_rev = rset2.getString(5);
					cont_type = rset2.getString(6);
					state_code  = rset2.getString(11);
					p_seq_no = rset2.getString(13);
					item_desc = rset2.getString(41);
					dr_cr_tcs_flag = rset2.getString(55);
					tds_percent = rset2.getString(65);
					inv_no  = rset2.getString(90);
					criteria  = rset2.getString(91);
					tds_gross = rset2.getDouble(100);
					drcr_price = rset2.getDouble(101);
					ori_tds_percent = rset2.getString(103);
					
					String tax_cd = rset2.getString(35);
//					System.out.println(inv_no+":>>:"+dr_cr_tcs_flag+">>"+tds_percent);
					
					map=abbr+"-"+p_seq_no;
					
						
					str	="";
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				       
				    }
					str += abbr + ",";
					
					if(mpe.customer_map.containsKey(map))
					{
						p_seq_no=mpe.customer_map.get(map);
					}
					
					str += inv_no + ",";
				
					for (int j = 2; j < columns.split(",").length; j++) {
//						System.out.println(rset2.getString(j)+" :: "+j);
						value = rset2.getString(j-1) == null ? "null" : rset2.getString(j-1).replaceAll("\n", "");
						value = value.replaceAll(",", "_");
						value = value.replaceAll("\r", "");	
						
						if (j == 14) {	// SEQ_NO-map							
							p_seq_no=p_seq_no.split("-")[0];
							str += p_seq_no + ",";							
						}
						else if(j == 15) {
							queryString3 = "SELECT A.EMAIL FROM FMS7_CUSTOMER_CONTACT_MST A WHERE A.SEQ_NO = ? AND A.CUSTOMER_CD = ? "
									+ "AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_CUSTOMER_CONTACT_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
									+ "AND A.SEQ_NO = B.SEQ_NO)";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, rset2.getString(14));
							stmt3.setString(2, cd);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								mail = rset3.getString(1);
								if(mail != null) {
									if(!mail.equals("")) {
									mail = mail.replaceAll(",", "");
									}
								}
							}
							stmt3.close();
							rset3.close();
							str += mail + ",";		
						}

						else if(j==16) {	//inv_flag
							dr_cr_flg = rset2.getString(15);
							if(dr_cr_flg!=null) {
								dr_cr_flg = dr_cr_flg.toUpperCase();
							}
								str += dr_cr_flg + ",";
							}
						else if(j==25)//QTY
						{
//							if(criteria.equals("DIFF-TAX--"))
//							{
//								prev_qty = rset2.getDouble(24);
//								new_qty = rset2.getDouble(96);
//								if(dr_cr_flg.equalsIgnoreCase("dr"))
//								  {
//								    new_qty = new_qty-prev_qty;
//								  }
//								  else
//								  {
//									  new_qty = prev_qty-new_qty;
//								  }
//								
//							}
							if(criteria.equalsIgnoreCase("DIFF-QTY--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--"))
							{
								prev_qty = 0;
								pr_strt_dt = rset2.getString(21);  
								pr_end_dt = rset2.getString(22);
								String mapp_id = cd +"-"+ no +"-"+ rev +"-"+ cont_no +"-"+ cont_rev ;
								
								//NEED TO HANDLE THESE
//								  queryString3 = "SELECT SUM(EXIT_TOT_ENE) FROM DLNG_ALLOC_MST "
//								  		+ "WHERE MAPPING_ID = ? AND PARTY_CD = ? "
//								  		+ "AND CONTRACT_TYPE = ? AND GAS_DT BETWEEN TO_DATE(?,'DD/MM/YYYY') AND "
//								  		+ "TO_DATE(?,'DD/MM/YYYY')  ";
//								  stmt3 = conn.prepareStatement(queryString3);
//								  stmt3.setString(1, mapp_id);
//								  stmt3.setString(2, cd);
//								  stmt3.setString(3, cont_type);
//								  stmt3.setString(4, pr_strt_dt);
//								  stmt3.setString(5, pr_end_dt);
//								  rset3 = stmt3.executeQuery();
//								  if(rset3.next())
//								  {
									  prev_qty = rset2.getDouble(24);
									  new_qty =  rset2.getDouble(99);
									  if(dr_cr_flg.equalsIgnoreCase("dr"))
									  {
										  new_qty = new_qty-prev_qty;
									  }
									  else
									  {
										  new_qty = prev_qty-new_qty;
									  }
//								  }
//								  else
//								  {
//									  new_qty= 0;
//								  }
//								  stmt3.close();
//								  rset3.close();
									  if(dr_cr_flg.equalsIgnoreCase("cr"))
									  {
										  new_qty = new_qty * (-1);
									  }
							  }
							  else
							  {
								new_qty = 0;
							  }
							str += new_qty + ",";
						}
						else if(j==26)	//SALE_PRICE
						{
							if(!criteria.equalsIgnoreCase("DIFF-PRICE--") && !criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--"))
							{
								value = "0";
							}
							str += value + ",";
						}
						else if(j==28)	//SALE_AMT
						{
							sale_price = rset2.getDouble(92);
							if(criteria.equalsIgnoreCase("DIFF-PRIC") || criteria.equalsIgnoreCase("DIFF-PRICE--"))
							{
								gross_amt =(drcr_price-sale_price)*rset2.getDouble(24);//prev_qty
							    gross_amt_final = nf.format(gross_amt);
							}
							else if(criteria.equalsIgnoreCase("DIFF-QTY--"))
							{
								gross_amt =sale_price*new_qty;
							    gross_amt_final = nf.format(gross_amt);
							}
							else if(criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--"))
							{
								gross_amt =(drcr_price-sale_price)*new_qty;//new_qty
							    gross_amt_final = nf.format(gross_amt);
							    if(new_qty<0 && (drcr_price-sale_price)<0)
							    {
							    	gross_amt_final = (Double.parseDouble(gross_amt_final) * (-1))+"";
							    }
							}
//							else if(criteria.equalsIgnoreCase("DIFF-TAX--"))
//							{
//								gross_amt =sale_price*new_qty;
//							    gross_amt_final = nf.format(gross_amt);
//							}
							else
							{
								gross_amt_final = "0";
							}
							
							str += gross_amt_final + ",";
						}
						else if(j == 29) {	//EXCH_CD
							value = rset2.getString(28);
							queryString3="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, value);
							rset3 = stmt3.executeQuery();
							   if(rset3.next()) {	
								   name = rset3.getString(1);
								   name= name.toUpperCase();
								   if (name.contains("CUSTOMS RATE")) {
										name = "CUSTOM EXCHANGE RATE";
									}
									else if (name.contains("RBI REFERENCE")) {
										name = "RBI REFERENCE RATE";
									}
									else if (name.contains("SBI MUMBAI TT AVERAGE")) {
										name = "SBI MUMBAI TT BUY SELL";
									}
									else if (name.contains("SBI TT BUYING")) {
										name = "SBI RATE BUY";
									}
									else if (name.contains("SBI TT SELLING")) {
										name = "SBI RATE SELL";
									}
									else if (name.contains("SBI TT BUY SELL")) {
										name = "SBI RATE BUY SELL";
									}								
							   }								   								  
							   stmt3.close();
							   rset3.close();
								name = name.replaceAll(",", " ");
								name = name.replaceAll("\n", " ");
								name = name.replaceAll("\r", " ");
								name = name.replaceAll("\"", " ");
								str += name.trim() + ",";
						}

						else if(j==33)	//INV_RAISED_IN
						   {
							   if(criteria.contains("DIFF-EXG--"))
							   {
								   value = "EXCHG"; 
							   }
							   else if(criteria.contains("DIFF-PRICE--DIFF-QTY--"))
							   {
								   value = "QTY#PRICE"; 
							   }
							   else if(criteria.contains("DIFF-QTY--"))
							   {
								   value = "QTY";
							   }
							   else if(criteria.contains("DIFF-PRICE--"))
							   {
								   value = "PRICE"; 
							   }
							   else if(criteria.contains("DIFF-TAX--"))
							   {
								   value = "TAXP"; 
							   }
							   else
							   {
								   value = null;  
							   }

							   str += value + ",";
						   }
						else if(j==34)	//GROSS_AMT
						{
							double exch_rate = rset2.getDouble(93);
							String prev_gross = rset2.getString(95);
							String new_gross = rset2.getString(98);
							
							  if(criteria!=null && criteria.equalsIgnoreCase("DIFF-QTY--") && (exch_rate!=0.0))
							  {
									gross_amt_inr = nf.format(rset2.getDouble(100));
							  }
							  else if(criteria!=null && criteria.equalsIgnoreCase("DIFF-TAX--") && (exch_rate!=0.0))
							  {
								  gross_amt_inr =  "0";
							  }
							  else
							  {
								  gross_amt_inr =  rset2.getString(33);
							  }
							  
							  if(Double.parseDouble(gross_amt_final) < 0 || rset2.getDouble(30)<0)
							  {
								  gross_amt_inr = nf.format(Double.parseDouble(gross_amt_inr) * (-1));
							  }
							  str += gross_amt_inr + ",";

						}
						else if(j==35)	//TAX_AMT
						{
							
//							String new_tax = rset2.getString(106);
							
							queryString4="SELECT REMARK FROM FMS7_TAX_STRUCTURE WHERE TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY') ORDER BY APP_DATE DESC ";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1, tax_cd);
							stmt4.setString(2, rset2.getString(19));
							rset4 = stmt4.executeQuery();
							
							   if(rset4.next()) {	
								   tax_cd = rset4.getString(1);
								   tax_cd = tax_cd.replaceAll(",", "-");
								   tax_cd = tax_cd.replaceAll("\n", " ");
								   tax_cd = tax_cd.replaceAll("\r", " ");
								   tax_cd = tax_cd.replaceAll("\"", " ");
								   if (tax_cd.contains("MH")) {
									   tax_cd = tax_cd.split(" MH")[0] + "%";
									}
									else if (tax_cd.contains("AP")) {
										tax_cd = tax_cd.split(" AP")[0] + "%";
									}
									else if (tax_cd.contains("ADD. VAT")) {
										tax_cd = tax_cd.replace("ADD. VAT", "ADVAT");
									}
									else if (tax_cd.contains("STAX 0%")) {
										tax_cd = tax_cd.replace("STAX 0%", "ZSTAX 0%");
									}
							   }else {
								   tax_cd = null; 
							   }
							   
//							   if(criteria.equalsIgnoreCase("DIFF-TAX"))
//							   {
//								   new_tax = new_tax.split("@")[1];
//								   tax_cd = tax_cd.replaceAll("(\\b[A-Za-z]+\\b\\s*)\\d+\\s*%","$1" + new_tax + "%");
//								   System.out.println("tax_cd "+tax_cd);
////								   tax_cd = "";
//							   }
							   stmt4.close();
							   rset4.close();
							   
							   if(criteria.equals("DIFF-TAX--"))
		   						{
		   							tax_cd = item_desc.split(" ")[1].trim();
		   							tax_cd = tax_cd +" "+item_desc.split(" ")[0].split("%")[0].trim()+"%";
		   						}
							   
							   String tax_percent = tax_cd.split(" ")[1];
							   Double tax_per = Double.parseDouble(tax_percent.split("%")[0]);  
							
						   if(criteria!=null && criteria.equalsIgnoreCase("DIFF-PRICE--"))
							  {
								value = nf.format(Double.parseDouble(gross_amt_inr)*tax_per /100);
							  }
							else if(criteria!=null && criteria.equalsIgnoreCase("DIFF-EXG--"))
							{
								value = nf.format(Double.parseDouble(gross_amt_inr)*tax_per /100);
							}
							else if(criteria!=null && criteria.equalsIgnoreCase("DIFF-TAX--"))
							{
								value = nf.format(rset2.getDouble(102));
							}
							else if(criteria!=null && criteria.equalsIgnoreCase("DIFF-QTY--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--"))
							{
								value = nf.format(rset2.getDouble(102));
								 if(dr_cr_flg.equalsIgnoreCase("cr"))
								  {
									 value = (Double.parseDouble(value) * (-1))+"";
								  }
							}
							else 
							{
								value = rset2.getString(34);
							}
						   str += value +  ",";
						}
						else if(j==36)	//TAX_STRUCT_CD
						{
							   str += tax_cd +  ",";
						}
						
						else if(j==38)	//INVOICE_AMT
						{
							String net_pay = "";
							String dr_cr_net = rset2.getString(98);
							String prev_net = rset2.getString(38);
							if(criteria!=null && criteria.equalsIgnoreCase("DIFF-TAX--"))
							{
								net_pay = nf.format(rset2.getDouble(102));
							}
							else if(criteria!=null && (criteria.equalsIgnoreCase("DIFF-QTY--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--")))
							{
								net_pay = nf.format(rset2.getDouble(100)+rset2.getDouble(102));
							}
							else
							{
								net_pay = rset2.getString(37);
							}
							 if(Double.parseDouble(gross_amt_inr) < 0)
							  {
								 net_pay = nf.format(Double.parseDouble(net_pay) * (-1));
							  }
							str += net_pay +",";
							
						}
						else if(j==39)	//NET_PAYABLE_AMT
						{
							String net_pay = "";
							String dr_cr_net = rset2.getString(98);
							String prev_net = rset2.getString(38);
							
							if(criteria!=null && criteria.equalsIgnoreCase("DIFF-TAX--"))
							{
								net_pay = nf.format(rset2.getDouble(102));
							}
							else if(criteria!=null && (criteria.equalsIgnoreCase("DIFF-QTY--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--")))
							{
								net_pay = nf.format(rset2.getDouble(100)+rset2.getDouble(102));
							}
							else
							{
								net_pay = rset2.getString(38);
							}
							
							if(Double.parseDouble(gross_amt_inr) < 0)
							  {
								 net_pay = nf.format(Double.parseDouble(net_pay) * (-1));
							  }
							
							str += net_pay +",";
						}
						
						else if(j== 56) {	//TCS_TDS
							
							 if(tds_percent!=null) 
							   {
//									   System.out.println(rset2.getString(18)+"::"+tds_percent+":::"+ori_tds_percent);
							 	   tds_amt = nf.format(tds_gross * Double.parseDouble(tds_percent) / 100);
								   tcs_tds = "TDS";	
								   
								   queryString4="SELECT TDS_AMT,TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_INVOICE_TDS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? "
								   		+ "AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG' AND INVOICE_TYPE IN ('CREDIT','DEBIT') ";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, cust_cd);
									stmt4.setString(2, rset2.getString(17));
									stmt4.setString(3, fin_yr);
									stmt4.setString(4, cont_type);
									stmt4.setString(5, state_code);
									
									rset4 = stmt4.executeQuery();
								   if(rset4.next()) {
									   tds_date = rset4.getString(2);
								   }								   								  
									stmt4.close();
									rset4.close();
							   }
							   else 
							   {
								   
									queryString3="SELECT TCS_AMT,TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_INVOICE_TCS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? "
											+ "AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG' AND INVOICE_TYPE IN ('CREDIT','DEBIT') ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, cust_cd);
									stmt3.setString(2, rset2.getString(17));
									stmt3.setString(3, fin_yr);
									stmt3.setString(4, cont_type);
									stmt3.setString(5, state_code);
									
									rset3 = stmt3.executeQuery();
									   if(rset3.next()) {
										   tcs_amt = rset3.getString(1);
										   tcs_date = rset3.getString(2);
										   tcs_tds = "TCS";								
									   }
									   else 
									   {
										   tcs_tds = "NA";
										   tds_date = null;
										   tds_amt = null;
										   tcs_date = null;
										   tcs_amt = null;
									   }								   								  
									stmt3.close();
									rset3.close();
									
								   if(!tcs_tds.equals("NA") && ori_tds_percent!=null) 
								   {
									   tds_amt = nf.format(tds_gross * Double.parseDouble(ori_tds_percent) / 100);
									   tcs_tds = "TDS";	
									   tds_date = rset2.getString(19);
									   tcs_date = null;
									   tcs_amt = null;
									   tds_percent = ori_tds_percent;
								   }
							   }
	
								tcs_tds = tcs_tds.replaceAll(",", " ");
								tcs_tds = tcs_tds.replaceAll("\n", " ");
								tcs_tds = tcs_tds.replaceAll("\r", " ");
								tcs_tds = tcs_tds.replaceAll("\"", " ");
								str += tcs_tds + ",";
						}
						
						else if(j==57) {	//TCS_AMT
							if(tcs_amt!=null && Double.parseDouble(gross_amt_inr) < 0)
							  {
								tcs_amt = nf.format(Double.parseDouble(tcs_amt) * (-1));
							  }
							str += tcs_amt + ",";
						}
						
						else if(j==58) {	//TCS_FACTOR
							if(tcs_tds.equals("TCS")) {
								queryString4 = "SELECT B.FACTOR FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.TAX_CODE = ? AND B.APP_DATE < TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ORDER BY B.APP_DATE DESC ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, "127");
								stmt4.setString(2, tcs_date);
								rset4 = stmt4.executeQuery();
								if(rset4.next())
								{
									value = rset4.getString(1);
									tcs_factor = value;
								}
								else 
								{
									value = "0";
								}
							}
							else {
								value = null;
							}
							str += value + ",";
						}
						
						else if(j==66) {	//TDS_GROSS_PERCENT
							str += tds_percent + ",";
						}
						
						else if(j==67) {	//TDS_GROSS_AMT
							str += tds_amt + ",";
						}
						
						else if(j == 70) //PDF_INV_DTL
						{
							pdf_inv = rset2.getString(69);
							if(pdf_inv!=null) {
								if(pdf_inv.contains("2")) 
								{
									pdf_inv = "2";
								}
								else if(pdf_inv.contains("1")) 
								{
									pdf_inv = "1";
								}
								else if(pdf_inv.contains("d")) 
								{
									pdf_inv ="d";
								}
								else if(pdf_inv.contains("C")) 
								{
									pdf_inv ="C";
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
						
						else if(j==80) {
							if(tcs_tds.equals("TCS"))
							{
								value = tcs_tds +" "+ tcs_factor +"%";	
							}
							else 
							{
								value = null ;
							}
							str += value + ",";
						}
						
						else if(j==82) {
							if(tcs_tds.equals("TDS"))
							{
								value = tcs_tds +" "+ tds_percent +"%";	
							}
							else 
							{
								value = null ;
							}
							str += value + ",";
						}
						
						else if(j==85) {
							if(tcs_tds.equals("TCS"))
							{
								value = tcs_date;	
							}
							else 
							{
								value = null ;
							}
							str += value + ",";
						}
						
						else if(j==86) {
							if(tcs_tds.equals("TDS"))
							{
								value = tds_date;
							}
							else 
							{
								value = null ;
							}
							str += value + ",";
						}
						
						else if(j==89)	//FIN_SYS
						{
							if(rset2.getString(76)!=null && rset2.getString(76).equalsIgnoreCase("Y"))
							{
								value = "S";
							}
							else
							{
								value = rset2.getString(88);
							}
							str += value +",";
						}
						
					   else if(j==91)	//REF_NO
						{
							str += inv_no + ",";
						}
						
					   else if(j==92)	//CRITERIA
					   {
						   criteria  = rset2.getString(91);
						   if(criteria.contains("DIFF-EXG--"))
						   {
							   criteria = "EXCHG"; 
						   }
						   else if(criteria.contains("DIFF-PRICE--DIFF-QTY--"))  
						   {
							   criteria = "QTY#PRICE"; 
						   }
						   else if(criteria.contains("DIFF-QTY--"))
						   {
							   criteria = "QTY";
						   }
						   else if(criteria.contains("DIFF-PRICE--"))
						   {
							   criteria = "PRICE"; 
						   }
						   else if(criteria.contains("DIFF-TAX--"))
						   {
							   criteria = "TAXP"; 
						   }
						   else
						   {
							   criteria = null; 
						   }
						   str += criteria + ",";
					   }
						
					   else 
					   {
							str += value +  ",";
					   }
						
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
				
					
				}
				rset2.close();
				stmt2.close();
				
			 }
			 rset.close();
			 stmt.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INVOICE_MST_CR_DR>>,,,,", conn);
			
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
		}
		
	}


	
	 //FMS_DLNG_INV_CRDR_REF
	   public void FMS_DLNG_INV_CRDR_REF() throws SQLException, IOException {
	   	
	   	function_nm = "FMS_DLNG_INV_CRDR_REF()";
	   	
	   	try {
	   		
	   		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
	   		logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_INV_CRDR_REF>>,,,,", conn);
	   		
	   		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
	   		
	   		columns = "COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,"
	   				+ "TAX_STRUCT_CD,TAX_AMT,INVOICE_AMT,NET_PAYABLE_AMT,TCS_TDS,TCS_AMT,"
	   				+ "TCS_FACTOR,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,"
	   				+ "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
	   		
	   		count = 0;
	   		String fname_csv = "", str = "",cust_cd="",inv_no="",invoice_no="",tax_cd="";
	   		String criteria="",pr_strt_dt="",pr_end_dt="",gross_amt="",gross_amt_inr="",agmt_no="",agmt_rev="",tax_amt="",item_desc="";
	   		double  new_qty=0,sale_price=0,net_amt_in=0;
	   		int hlpl=0;
	   		
	   		fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_INV_CRDR_REF_" + start_end_dt + ".csv";
	   		
	   		FileWriter fw = new FileWriter(fname_csv, false); 
	   		fw.close();
	   		
	   		// Inserting Column Names
	   		for (int i = 0; i < columns.split(",").length; i++) {
	   			str += columns.split(",")[i] + ",";
	   		}
	   		logger.insert_data(fname_csv, str, conn);
	   		
	   		logger.checkpoint(fname, "COMPANY_CD,FINANCIAL_YEAR,INVOICE_SEQ,TIMESTAMP,", conn);
	   		
	   		queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), B.CUSTOMER_CD, A.EFF_DT, A.COUNTERPARTY_CD FROM FMS9_COUNTERPTY_MST A, FMS7_CUSTOMER_MST B "
	   				+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND "
	   				+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
	   		stmt = conn.prepareStatement(queryString);
	   		rset = stmt.executeQuery();
	   		
	   		while (rset.next()) {
	   			cd=rset.getString(2);
	   			
	   			queryString2 = "SELECT A.DR_CR_FIN_YEAR,NULL,A.HLPL_INV_SEQ_NO,A.TOTAL_QTY,A.SALE_PRICE,NULL,A.GROSS_AMT_USD,B.EXCHG_RATE_CD,TO_CHAR(B.EXCHG_RATE_DT,'DD/MM/YYYY'),"//9
	   					+ "NVL(A.DR_CR_EXG_RATE,A.EXCHG_RATE_VALUE),NULL,NVL(A.GROSS_AMT_INR+A.DIFF_AMT,0),A.TAX_STRUCT_CD,NVL(A.TAX_REMARK,A.DR_CR_NET_AMT_INR - A.DR_CR_GROSS_AMT_INR),"//14
	   					+ "NVL(A.NET_AMT_INR+A.DR_CR_NET_AMT_INR,0),NVL(A.NET_AMT_INR+A.DR_CR_NET_AMT_INR,0),NULL,NULL,NULL,A.TDS_PERCENT,NULL,A.TDS_TAX_PERCENT,"//22
	   					+ "A.TDS_TAX_AMT,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY'),NULL,NULL, "//27
	   					+ "A.CUSTOMER_CD,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CONTRACT_TYPE,A.DR_CR_DOC_NO,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "//35
	   					+ "B.NEW_INV_SEQ_NO,B.FINANCIAL_YEAR,A.CRITERIA,A.DR_CR_SALE_RATE,TO_CHAR(B.PERIOD_START_DT,'DD/MM/YYYY'),"
	   					+ "TO_CHAR(B.PERIOD_END_DT,'DD/MM/YYYY'),A.PLANT_SEQ_NO,A.DIFF_QTY,A.DR_CR_GROSS_AMT_INR,A.DR_CR_NET_AMT_INR,A.DR_CR_QTY, "//46
	   					+ "A.GROSS_AMT_INR,A.ITEM_DESCRIPTION,NVL(A.TAX_AMT_INR+A.TAX_REMARK,0) "//49
	   					+ "FROM DLNG_DR_CR_NOTE A, DLNG_INVOICE_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND A.FGSA_NO = B.FGSA_NO "
	   					+ "AND A.SN_NO = B.SN_NO AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.HLPL_INV_SEQ_NO = B.HLPL_INV_SEQ_NO "
	   					+ "AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.PLANT_SEQ_NO = B.PLANT_SEQ_NO "
	   					+ "AND A.CUSTOMER_CD = ? AND A.CRITERIA NOT IN ('OTHERS-1','OTHERS-2','TCS','REV_INV--','MULTI-cr','MULTI-dr2') "
	   					+ "AND A.CONTRACT_TYPE IN ('S','L') "
	   					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
	   					+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";	
	   			stmt2 = conn.prepareStatement(queryString2);
	   				stmt2.setString(1, cd);
	   				stmt2.setString(2, delta_FromDt);
	   				stmt2.setString(3, delta_FromDt);
	   				stmt2.setString(4, delta_ToDt);
	   				stmt2.setString(5, delta_ToDt);
	   				rset2 = stmt2.executeQuery();
	   			while(rset2.next()) 
	   			{
	   				cust_cd = rset2.getString(28);
	   				agmt_no = rset2.getString(29);
	   				agmt_rev = rset2.getString(30);
	   				cont_no = rset2.getString(31);
	   				cont_rev = rset2.getString(32);
	   				cont_type = rset2.getString(33);
	   				tax_cd = rset2.getString(13);
	   				criteria = rset2.getString(38);
	   				item_desc = rset2.getString(48);
	   				str	="";
	   				
	   				inv_no = rset2.getString(34);
	   				abbr = rset.getString(1);	
	   				
	   				if (mpe.counterparty_map.containsKey(abbr)) {
	   					abbr =mpe.counterparty_map.get(abbr); 
	   			    }
	   				str += abbr+"&"+ inv_no+ ",";
	   								
	   				for (int j = 1; j < columns.split(",").length; j++) {
	   					value = rset2.getString(j) == null ? "null" : rset2.getString(j).replaceAll("\n", " ");
	   					value = value.replaceAll(",", "_");
	   					value = value.replaceAll("\r", "");	
	   					
	   					
	   					if(j==4)
	   					{
	   						  if(criteria.equalsIgnoreCase("DIFF-QTY--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--"))
							  {
								new_qty =  rset2.getDouble(46);
							  }
							  else
							  {
								  new_qty =  rset2.getDouble(4);
							  }
							  str += new_qty + ",";
	   					}
	   					else if(j==5)	//SALE_PRICE
	   					  {
	   						  if(criteria.equalsIgnoreCase("DIFF-PRICE--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--"))
	   						  {
	   						     sale_price = rset2.getDouble(39);
	   						  }
	   						  else
	   						  {
	   							  sale_price = rset2.getDouble(5); 
	   						  }
	   						  str += sale_price + ",";
	   					  }
	   					  else if(j==7)	//SALE_AMT
	   					  {
	   						  if(criteria.equalsIgnoreCase("DIFF-QTY--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--"))
	   						  {
	   							  gross_amt =nf.format(sale_price*new_qty);
	   						  }
	   						  else if(criteria.equalsIgnoreCase("DIFF-PRICE--"))
	   						  {
	   							  gross_amt =nf.format(sale_price*new_qty);
	   						  }
	   						  else
	   						  {
	   							  gross_amt =  rset2.getString(7);
	   						  }
	   						  str += gross_amt + ",";
	   					  }
	   					else if(j == 8) {
	   						value = rset2.getString(8);
	   						queryString3="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
	   						stmt3 = conn.prepareStatement(queryString3);
	   						stmt3.setString(1, value);
	   						rset3 = stmt3.executeQuery();
	   						   if(rset3.next()) {	
	   							   name = rset3.getString(1);
	   							   name= name.toUpperCase();
	   							   if (name.contains("CUSTOMS RATE")) {
	   									name = "CUSTOM EXCHANGE RATE";
	   								}
	   								else if (name.contains("RBI REFERENCE")) {
	   									name = "RBI REFERENCE RATE";
	   								}
	   								else if (name.contains("SBI MUMBAI TT AVERAGE")) {
	   									name = "SBI MUMBAI TT BUY SELL";
	   								}
	   								else if (name.contains("SBI TT BUYING")) {
	   									name = "SBI RATE BUY";
	   								}
	   								else if (name.contains("SBI TT SELLING")) {
	   									name = "SBI RATE SELL";
	   								}
	   								else if (name.contains("SBI TT BUY SELL")) {
	   									name = "SBI RATE BUY SELL";
	   								}								
	   						   }								   								  
	   						   stmt3.close();
	   						   rset3.close();
	   							name = name.replaceAll(",", " ");
	   							name = name.replaceAll("\n", " ");
	   							name = name.replaceAll("\r", " ");
	   							name = name.replaceAll("\"", " ");
	   							str += name.trim() + ",";
	   					}
	   					else if(j==12)	//GROSS_AMT
	   					{
	   						double exch_rate = rset2.getDouble(10);
//	   						 criteria = rset2.getString(44);
	   						  if((criteria.equalsIgnoreCase("DIFF-QTY--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--")) && (exch_rate!=0.0))
	   						  {
	   							  gross_amt_inr =nf.format(Double.parseDouble(gross_amt)*(exch_rate));
	   						  }
		   						else if(criteria.equalsIgnoreCase("DIFF-PRICE--"))
								{
		   							gross_amt_inr = nf.format(Double.parseDouble(gross_amt)*exch_rate);
								}
		   						else if(criteria.equalsIgnoreCase("DIFF-EXG--"))
		   						{
		   							gross_amt_inr = nf.format(Double.parseDouble(gross_amt)*exch_rate);
		   						}
		   						else if(criteria.equalsIgnoreCase("DIFF-TAX--"))
		   						{
		   							gross_amt_inr = rset2.getString(47);
		   						}
	   						  else
	   						  {
	   							  gross_amt_inr =  rset2.getString(12);
	   						  }
	   						  str += gross_amt_inr + ",";
	   					}
	   					else if(j==13)
	   					{
	   						queryString4="SELECT REMARK FROM FMS7_TAX_STRUCTURE WHERE TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY') ORDER BY APP_DATE DESC ";
	   						stmt4 = conn.prepareStatement(queryString4);
	   						stmt4.setString(1, tax_cd);
//	   						System.out.println("rset2.getString(41) "+rset2.getString(41)+tax_cd);
	   						stmt4.setString(2, rset2.getString(35));
	   						rset4 = stmt4.executeQuery();
	   						
	   						   if(rset4.next()) {	
	   							   tax_cd = rset4.getString(1);
	   							   tax_cd = tax_cd.replaceAll(",", "-");
	   							   tax_cd = tax_cd.replaceAll("\n", " ");
	   							   tax_cd = tax_cd.replaceAll("\r", " ");
	   							   tax_cd = tax_cd.replaceAll("\"", " ");
	   							   if (tax_cd.contains("MH")) {
	   								   tax_cd = tax_cd.split(" MH")[0] + "%";
	   								}
	   								else if (tax_cd.contains("AP")) {
	   									tax_cd = tax_cd.split(" AP")[0] + "%";
	   								}
	   								else if (tax_cd.contains("ADD. VAT")) {
	   									tax_cd = tax_cd.replace("ADD. VAT", "ADVAT");
	   								}
	   								else if (tax_cd.contains("STAX 0%")) {
	   									tax_cd = tax_cd.replace("STAX 0%", "ZSTAX 0%");
	   								}
	   						   }else {
	   							   tax_cd = null; 
	   						   }							   								  
	   						   stmt4.close();
	   						   rset4.close();
	   						   
		   						if(criteria.equals("DIFF-TAX--"))
		   						{
		   							tax_cd = item_desc.split(" ")[1].trim();
		   							tax_cd = tax_cd +" "+item_desc.split(" ")[0].split("%")[0].trim()+"%";
		   						}
	   						   str += tax_cd +  ",";
	   					}
	   					else if(j==14)	//TAX_AMT
	   					{
	   						String tax_percent = tax_cd.split(" ")[1];
							Double tax_per = Double.parseDouble(tax_percent.split("%")[0]);
	   					   if(criteria.equalsIgnoreCase("DIFF-TAX--"))
	   					   {
	   						   value = rset2.getString(49);
	   					   }
							else if(criteria.equalsIgnoreCase("DIFF-PRICE--"))
							{
								value = nf.format(Double.parseDouble(gross_amt_inr)*tax_per /100);
							}
							else if(criteria.equalsIgnoreCase("DIFF-EXG--"))
							{
								value = nf.format(Double.parseDouble(gross_amt_inr)*tax_per /100);
							}
							else if(criteria.equalsIgnoreCase("DIFF-QTY--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--"))
							{
								value = nf.format(Double.parseDouble(gross_amt_inr)*tax_per /100);
							}
	   					   else
	   					   {
	   						   value = rset2.getString(15);
	   					   }
	   					   tax_amt = value;
	   					   str += value +  ",";
	   					}
	   					else if(j==15)	//INV_AMT
	   					{
	   						if(criteria.equalsIgnoreCase("DIFF-TAX--"))
   						   {
	   							value = nf.format(Double.parseDouble(gross_amt_inr)+Double.parseDouble(tax_amt));
   						   }
	   						else if(criteria.equalsIgnoreCase("DIFF-PRICE--"))
		   						{
		   							value = nf.format(rset2.getDouble(45)+rset2.getDouble(16));
		   						}
	   						else if(criteria.equalsIgnoreCase("DIFF-EXG--"))
	   						{
	   							value = nf.format(Double.parseDouble(gross_amt_inr)+Double.parseDouble(tax_amt));
	   						}
	   						else if(criteria.equalsIgnoreCase("DIFF-QTY--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--"))
	   						{
	   							value = nf.format(Double.parseDouble(gross_amt_inr)+Double.parseDouble(tax_amt));
	   						}
   						   else
	   						   {
	   							   value = rset2.getString(16);
	   						   }
	   						   str += value +  ",";
	   					}
	   					else if(j==16)	//NET_AMT
	   					{
	   						if(criteria.equalsIgnoreCase("DIFF-TAX--"))
	   						{
	   							value = nf.format(Double.parseDouble(gross_amt_inr)+Double.parseDouble(tax_amt));
	   						}
	   						else if(criteria.equalsIgnoreCase("DIFF-PRICE--"))
	   						{
	   							value = nf.format(rset2.getDouble(45)+rset2.getDouble(16));
	   						}
	   						else if(criteria.equalsIgnoreCase("DIFF-EXG--"))
	   						{
	   							value = nf.format(Double.parseDouble(gross_amt_inr)+Double.parseDouble(tax_amt));
	   						}
	   						else if(criteria.equalsIgnoreCase("DIFF-QTY--") || criteria.equalsIgnoreCase("DIFF-PRICE--DIFF-QTY--"))
	   						{
	   							value = nf.format(Double.parseDouble(gross_amt_inr)+Double.parseDouble(tax_amt));
	   						}
	   						else
	   						{
	   							value = rset2.getString(16);
	   						}
	   						str += value +  ",";
	   					}
	   					else {
	   						str += value +  ",";
	   					}
	   					
	   				}
	   				logger.insert_data(fname_csv, str, conn);
	   				count++;
	   				logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
	   				
	   				}
	   			rset2.close();
	   			stmt2.close();
	   			
	   			}
	   		rset.close();
	   		stmt.close();
	   		
	   		logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
	   		logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INV_CRDR_REF>>,,,,", conn);
	   		
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
	   	}
	   	
	   }
	   

		public void FMS_DLNG_INV_CRDR_FILE_DTL() throws SQLException, IOException { 
			
			function_nm = "FMS_DLNG_INV_CRDR_FILE_DTL()";
			
			try {
				
				System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
				logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_INV_CRDR_FILE_DTL>>,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
				
				columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,"
						+ "PDF_CONTENT,SF_GEN_DT,EMAIL_SENT,EMAIL_SENT_BY,EMAIL_SENT_DT";
//				workbook = new XSSFWorkbook();
//				spreadsheet = workbook.createSheet("Sheet 1");
				
//				nrow = 0;
//				ncell = 0;
				count = 0;
				String inv_seq= "",inv_no="",inv_dt="",ent_by="",ent_dt="",pdf_type="",file_nm="";
				String o_by= "",o_dt="",d_by="",d_dt="",t_by="",t_dt="",pdf_inv_dtl="";
//				row = spreadsheet.createRow(nrow++);
				String fname_csv = "", str = "", gas_dt="";
				fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_INV_CRDR_FILE_DTL_" + start_end_dt + ".csv";
				
				FileWriter fw = new FileWriter(fname_csv, false); 
				fw.close();
				
				// Inserting Column Names
				for (int i = 0; i < columns.split(",").length; i++) {
//					cell = row.createCell(i);
//					cell.setCellValue(columns.split(",")[i]);
					str += columns.split(",")[i] + ",";
				}
				logger.insert_data(fname_csv, str, conn);
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				queryString = "SELECT DISTINCT(A.CUSTOMER_CD), A.EFF_DT  FROM FMS7_CUSTOMER_MST A "
						+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "ORDER BY A.CUSTOMER_CD, A.EFF_DT DESC  ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					cd=rset.getString(1);
					
						queryString2 = "SELECT B.CONTRACT_TYPE, B.HLPL_INV_SEQ_NO, B.DR_CR_FIN_YEAR, "
								+ "TO_CHAR(B.INVOICE_DT,'DD/MM/YYYY'), A.NEW_INV_SEQ_NO, A.SUP_STATE_CODE+1, "
								+ "A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "A.PRINT_BY_DUP, TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "A.PRINT_BY_TRI, TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "A.PDF_INV_DTL,B.DR_CR_DOC_NO "
								+ "FROM DLNG_INVOICE_MST A, DLNG_DR_CR_NOTE B  WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND A.FGSA_NO = B.FGSA_NO "
								+ "AND A.SN_NO = B.SN_NO AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.HLPL_INV_SEQ_NO = B.HLPL_INV_SEQ_NO "
								+ "AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.PLANT_SEQ_NO = B.PLANT_SEQ_NO AND "
								+ "B.CUSTOMER_CD = ? AND B.CONTRACT_TYPE IN ('S','L') ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
					while(rset2.next()) 
					{
						cont_type = rset2.getString(1);
						inv_seq = rset2.getString(2);
						fin_yr = rset2.getString(3);
						inv_dt = rset2.getString(4);
						o_by = rset2.getString(7);
						o_dt = rset2.getString(8);
						d_by = rset2.getString(9);
						d_dt = rset2.getString(10);
						t_by = rset2.getString(11);
						t_dt = rset2.getString(12);
						pdf_inv_dtl = rset2.getString(13);
						inv_no = rset2.getString(14);
						
						queryString1 = "SELECT '2', '24', NULL, NULL, INV_TYPE, PDF_INV_NM, NULL, NULL, NULL, NULL, PDF_SIGNED_FLAG, SIGNED_BY, "
								+ "TO_CHAR(SIGNED_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, MAIL_SENT_FLAG, NULL, TO_CHAR(MAIL_SENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
								+ " FROM DLNG_INV_PDF_DTL "
								+ " WHERE PDF_INV_NM LIKE ? "
//								+ "AND TO_DATE(CREATED_DT, 'DD/MM/YYYY') = TO_DATE(?, 'DD/MM/YYYY') "
								+ "AND INV_TYPE IN ('2','1','C','d')";
//								+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//								+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
						
						stmt1 = conn.prepareStatement(queryString1);	
						stmt1.setString(1, "%"+cont_type+"-"+inv_seq);
//						stmt1.setString(2, inv_dt);
//						stmt1.setString(2, delta_FromDt);
//						stmt1.setString(3, delta_FromDt);
//						stmt1.setString(4, delta_ToDt);
//						stmt1.setString(5, delta_ToDt);
						rset1 = stmt1.executeQuery();
						
//						System.out.println(inv_dt+"=="+"%-"+cont_type+"-"+inv_seq);
						while(rset1.next())
						{	
//							System.out.println(">>>");
							pdf_type = rset1.getString(5);
							file_nm = rset1.getString(6);
							
//							row = spreadsheet.createRow(nrow++);
//							ncell = 0;
							str = "";
							
							for (int i = 0; i < columns.split(",").length; i++) {
								
							if(i == 2) {	//INVOICE_SEQ
								value = inv_no;
//								cell= row.createCell(i);
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
								
							else if (i == 3) {	//FINANCIAL_YEAR
								value = fin_yr;
//								cell= row.createCell(i);
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if(i == 4) {	//PDF_TYPE
								if(pdf_type.equals("C") || pdf_type.equals("d")) {
									value = "O";
							 }
							 
							 else if(pdf_type.equals("1")) {
									value = "D";
							 }
								
							 else if(pdf_type.equals("2")) {
									value = "T";
							 }
//								cell= row.createCell(i);
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if (i == 5) {
								value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
								if(pdf_inv_dtl.contains("C"))
								{
									value = "CREDIT-"+value+"-"+pdf_type;
								}
								else if(pdf_inv_dtl.contains("d"))
								{
									value = "DEBIT-"+value+"-"+pdf_type;
								}
								if(!value.contains(".pdf")) {
									value = value + ".pdf";
								}
								str += value + ",";
							 }
							else if(i == 6) {	//ENT_BY
								if(pdf_type.equals("C") || pdf_type.equals("d")) {
									value = o_by;
								}
								
								else if(pdf_type.equals("1")) {
									value = d_by;
								}
								
								else if(pdf_type.equals("2")) {
									value = t_by;
								}
//								cell= row.createCell(i);
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							
							else if(i == 7) {	//ENT_DT
							if(pdf_type.equals("C") || pdf_type.equals("d")) {
									value = o_dt;
							 }
							 
							 else if(pdf_type.equals("1")) {
									value = d_dt;
							 }
								
							 else if(pdf_type.equals("2")) {
									value = t_dt;
							 }
//								cell= row.createCell(i);
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							
							else if(i == 11) {
								value = rset1.getString(12);
//								cell = row.createCell(i);																					
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
							}
								
								else {
									value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
//									cell = row.createCell(i);																					
//									cell.setCellValue("'" + value + "'");
									str += value + ",";
								}
								
							}
							logger.insert_data(fname_csv, str, conn);
							count++;
							logger.data(fname, (company_cd+",24" + ","+inv_seq+ "," +fin_yr+","+pdf_type+","+file_nm+","), conn, "");
							
							
						}					
						stmt1.close();
						rset1.close();
					}
					rset2.close();
					stmt2.close();
					}
				rset.close();
				stmt.close();
				
				
				
				
//				filename = migration_setup_dir + "EXPORT/FMS_DLNG_INV_FILE_DTL_"+start_end_dt+".xlsx";
				
//				fileOut = new FileOutputStream(filename);  
				
//				workbook.write(fileOut);
//				fileOut.close(); 
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INV_CRDR_FILE_DTL>>,,,,", conn);
				
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
			}
			
		}


	    public void FMS_DLNG_INVOICE_MST_LP() throws SQLException, IOException {
			
			function_nm = "FMS_DLNG_INVOICE_MST_LP()";
			
			try {
				
				System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
				logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_INVOICE_MST_LP>>,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
				
				columns = "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRUCK_TRANS_CD,TRUCK_CD,MAPPING_ID,BU_UNIT,BU_STATE_TIN,"
						+ "BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INV_FLAG,INVOICE_SEQ,INVOICE_ID_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,"
						+ "DUE_DT,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,EXCHG_RATE_TYPE,INVOICE_RAISED_IN,GROSS_AMT,"
						+ "TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT_1,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,REMARK_1,REMARK_2,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,"
						+ "AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TCS_TDS,TCS_AMT,TCS_FACTOR,PAY_RECV_AMT,"
						+ "PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,"
						+ "PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,"
						+ "TCS_STRUCT_CD,TCS_EFF_DT_1,TDS_STRUCT_CD,TDS_EFF_DT_1,TAX_EFF_DT,TCS_EFF_DT,TDS_EFF_DT,CHKPOST_CD,DRIVER_CD,FIN_SYS,HOLD_AMT,REF_NO,CRITERIA,"
						+ "DISCOUNT_DAYS,INT_RATE";
				
				workbook = new XSSFWorkbook();
				count = 0;
				String agmt_no,agmt_rev,pdf_inv,inv_no;
				NumberFormat nf2 = new DecimalFormat("###########0.0000");

				String fname_csv = "", str = "";
				fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_INVOICE_MST_LP_" + start_end_dt + ".csv";
				
				FileWriter fw = new FileWriter(fname_csv, false); 
				fw.close();
				
				// Inserting Column Names
						for (int i = 0; i < columns.split(",").length; i++) {
							str += columns.split(",")[i] + ",";
						}
						logger.insert_data(fname_csv, str, conn);

						logger.checkpoint(fname, "COUNTERPARTY_ABBR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO,TIMESTAMP,", conn);
						
						// Inserting Rest of the data
						queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
								+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
								+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
						stmt = conn.prepareStatement(queryString);
						rset = stmt.executeQuery();
						
						while (rset.next()) {
							
							queryString2 = "SELECT A.CUSTOMER_CD, A.FINANCIAL_YEAR, A.FGSA_NO, A.FGSA_REV_NO, A.SN_NO, A.SN_REV_NO, A.CONTRACT_TYPE, NULL, A.TRUCK_ID, "
									+ "NULL, A.SUP_PLANT_CD + 1, A.SUP_STATE_CODE, NULL, A.PLANT_SEQ_NO, A.CONTACT_PERSON_CD, 'LP', A.HLPL_INV_SEQ_NO, A.HLPL_INV_SEQ_NO, "
									+ "A.NEW_INV_SEQ_NO, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, TO_CHAR(A.PERIOD_START_DT, 'DD/MM/YYYY HH24:MI:SS'), "
									+ "TO_CHAR(A.PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.TOTAL_QTY, A.SALE_PRICE, NULL, A.GROSS_AMT_USD, "
									+ "A.EXCHG_RATE_CD, TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EXCHG_RATE_VALUE, A.EXCHG_RATE_TYPE, NULL, A.GROSS_AMT_INR, "
									+ "A.TAX_AMT_INR, A.TAX_STRUCT_CD, NULL, A.NET_AMT_INR, A.NET_AMT_INR, NULL, A.REMARK_1, A.REMARK_2, A.CHECKED_FLAG, A.CHECKED_BY, "
									+ "TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.AUTHORIZED_FLAG, A.AUTHORIZED_BY, TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'), "
									+ "A.APPROVED_FLAG, A.APPROVED_BY, TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
									+ "NULL, NULL, NULL, NULL, NULL, A.PAY_RECV_AMT, TO_CHAR(A.PAY_RECV_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_INSERT_BY, "
									+ "TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_UPDATE_BY, TO_CHAR(A.PAY_UPDATE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
									+ "A.PAY_REMARK, A.TDS_PERCENT, A.TDS_TAX_AMT, A.TDS_TAX_PERCENT, A.TDS_TAX_AMT, A.PDF_INV_DTL, A.PRINT_BY_ORI, "
									+ "TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_TRI, TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_DUP, "
									+ "TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), "
									+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, A.SUN_APPROVAL, NULL, A.REMARK_SPECIFICATION, NULL, A.OFFSPEC_QTY, A.OFFSPEC_RATE "
									+ "FROM DLNG_INVOICE_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND A.CONTRACT_TYPE = 'I' AND "
									+ "(? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
							
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset.getString(2));
							stmt2.setString(2, delta_FromDt);
							stmt2.setString(3, delta_FromDt);
							stmt2.setString(4, delta_ToDt);
							stmt2.setString(5, delta_ToDt);
							rset2 = stmt2.executeQuery();
							
							while (rset2.next()) {
								str = "";
								abbr = rset.getString(1);	
								fin_yr = rset2.getString(2);
								cd = rset2.getString(1);
								agmt_no = rset2.getString(3);
								agmt_rev = rset2.getString(4);
								cont_no = rset2.getString(5);
								cont_rev = rset2.getString(6);
								cont_type = rset2.getString(7);
								inv_seq = rset2.getString(17);
								inv_no = rset2.getString(19);
								BigDecimal tds_percent = rset2.getBigDecimal(66);
								BigDecimal gross_amt = rset2.getBigDecimal(34);
								
								String mail="",sale_unit="",sale_amt="",tax_dtl="";
								
								if (mpe.counterparty_map.containsKey(abbr)) {
									abbr =mpe.counterparty_map.get(abbr); 
							       
							    }
								str += abbr + ",";
								
								for (int i = 1; i < columns.split(",").length; i++) {
									
									value = rset2.getString(i) == null ? "null" : rset2.getString(i);
									value = value.replaceAll(",", " ");
									value = value.replaceAll("\n", " ");
									value = value.replaceAll("\r", " ");
									value = value.replaceAll("\"", " ");
									
									if(i == 1) {
										str += rset2.getString(91)+ ",";
									}
									else if(i == 15) {
										queryString3 = "SELECT A.EMAIL FROM FMS7_CUSTOMER_CONTACT_MST A WHERE A.SEQ_NO = ? AND A.CUSTOMER_CD = ? "
												+ "AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_CUSTOMER_CONTACT_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
												+ "AND A.SEQ_NO = B.SEQ_NO)";
										stmt3 = conn.prepareStatement(queryString3);
										stmt3.setString(1, rset2.getString(15));
										stmt3.setString(2, cd);
										rset3 = stmt3.executeQuery();
										if(rset3.next()) {
											mail = rset3.getString(1);
											if(mail != null) {
												if(!mail.equals("")) {
												mail = mail.replaceAll(",", "");
												}
											}
										}
										stmt3.close();
										rset3.close();
										str += mail + ",";		
									}
									else if(i==36 && !value.equals("null")) {
										queryString4="SELECT REMARK FROM FMS7_TAX_STRUCTURE WHERE TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY APP_DATE DESC";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1,value);
										stmt4.setString(2, rset2.getString(22));
										rset4 = stmt4.executeQuery();
										   if(rset4.next()) {	
											   tax_dtl = rset4.getString(1);
											   tax_dtl = tax_dtl.replaceAll(",", "@ ");
											   tax_dtl = tax_dtl.replaceAll("\n", " ");
											   tax_dtl = tax_dtl.replaceAll("\r", " ");
											   tax_dtl = tax_dtl.replaceAll("\"", " ");
											   if (tax_dtl.contains("MH")) {
												   tax_dtl = tax_dtl.split(" MH")[0] + "%";
												}
												else if (tax_dtl.contains("AP")) {
													tax_dtl = tax_dtl.split(" AP")[0] + "%";
												}
												else if (tax_dtl.contains("ADD. VAT")) {
													tax_dtl = tax_dtl.replace("ADD. VAT", "ADVAT");
												}
												else if (tax_dtl.contains("STAX 0%")) {
													tax_dtl = tax_dtl.replace("STAX 0%", "ZSTAX 0%");
												}
										   }else {
											   tax_dtl = null; 
										   }							   								  
										   stmt4.close();
										   rset4.close();
										  
										   str += tax_dtl + ",";
									}
									else if(i == 56) {
										if(tds_percent!=null) {
											tcs_tds = "TDS";
										}
										else {
											tcs_tds = rset2.getString(56);
										}
										str += tcs_tds + ",";
									}
									else if(i == 66 ){ 	//TDS_GROSS_PERCENT
										if(tcs_tds!= null && tcs_tds.equals("TDS")) {
											value = tds_percent+"";
										}
										else {
											value = null;
										}
										str += value + ",";
									}
									
									else if(i == 67 ) {	 //TDS_GROSS_AMT
										if(tds_percent!=null && tcs_tds.equals("TDS")) {
											Double result1 = (gross_amt.multiply(tds_percent)).divide(BigDecimal.valueOf(100)).doubleValue();
											value = result1.toString();
										}
										else {
											value = null;
										}
										
										str += value + ",";
									}
									else if(i == 70) {
										pdf_inv = rset2.getString(70);
										if(pdf_inv!=null) {
											if(pdf_inv.contains("T")) {
												pdf_inv = "T";
											}else if(pdf_inv.contains("D")) {
												pdf_inv = "D";
											}else if(pdf_inv.contains("O")) {
												pdf_inv ="O";
											}else {
												pdf_inv = null;
											}
										}else {
											pdf_inv = null;
										}
										
										str += pdf_inv + ",";
									}
									else if(i == 89) {
										if(rset2.getString(77)!=null) {
											str += "S,";
										}else {
											str += null + ",";
										}
									}
									else if(i == 90) {	//HOLD_AMT
										queryString3 = "SELECT A.HOLD_AMOUNT FROM FMS8_PAY_RECV_DTL A WHERE A.COMMODITY_TYPE = 'DLNG' AND A.NEW_INV_SEQ_NO = ? "
												+ "AND A.CONTRACT_TYPE = 'I' AND A.REV_NO = (SELECT MAX(C.REV_NO) FROM FMS8_PAY_RECV_DTL C WHERE A.NEW_INV_SEQ_NO = C.NEW_INV_SEQ_NO "
												+ "AND A.CONTRACT_TYPE = C.CONTRACT_TYPE AND A.COMMODITY_TYPE = C.COMMODITY_TYPE)";
										stmt3 = conn.prepareStatement(queryString3);
										stmt3.setString(1, inv_no);
										rset3 = stmt3.executeQuery();
										if(rset3.next()) {
											value = rset3.getString(1);
										}
										rset3.close();
										stmt3.close();
										
										str += value + ",";
									}
									else {
										str += value + ",";
									}
								}
								logger.insert_data(fname_csv, str, conn);
								count++;
								logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
							}
							rset2.close();
							stmt2.close();
						}
						stmt.close();
						rset.close();
						logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
						logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INVOICE_MST_LP>>,,,,", conn);
						
						logger.checkpoint1(fname1,count+",", conn);
						
						System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
						System.out.println();

						msg = "Data has been Extracted Successfully.";
						msg_type = "S";
				
				}catch(Exception e) {
						msg = "One of the Functions faced an Error. Extraction Terminated.";
						msg_type = "E";
								
						//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				}
		}
	   
	   
	   public void FMS_DLNG_INV_FILE_DTL_LP() throws SQLException, IOException {
			
			function_nm = "FMS_DLNG_INV_FILE_DTL_LP()";
			
			try {
				
				System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
				logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_INV_FILE_DTL_LP>>,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
				
				columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,"
						+ "MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT,"
						+ "EMAIL_SENT,EMAIL_SENT_BY,EMAIL_SENT_DT";
				count = 0;
				
				String inv_seq= "",inv_dt="",ent_by="",ent_dt="",pdf_type="",file_nm="";
				String o_by= "",o_dt="",d_by="",d_dt="",t_by="",t_dt="";
				String fname_csv = "", str = "", state_tin="",new_inv="";
				
				
				fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_INV_FILE_DTL_LP_" + start_end_dt + ".csv";
				
				FileWriter fw = new FileWriter(fname_csv, false); 
				fw.close();
				
				// Inserting Column Names
				for (int i = 0; i < columns.split(",").length; i++) {
					str += columns.split(",")[i] + ",";
				}
				logger.insert_data(fname_csv, str, conn);
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,INVOICE_TYPE,TIMESTAMP,", conn);
				
				queryString = "SELECT DISTINCT(A.CUSTOMER_CD), A.EFF_DT, A.CUSTOMER_ABBR  FROM FMS7_CUSTOMER_MST A "
						+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "ORDER BY A.CUSTOMER_CD, A.EFF_DT DESC  ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					
					cd = rset.getString(1);
					queryString2 = "SELECT CONTRACT_TYPE, HLPL_INV_SEQ_NO, FINANCIAL_YEAR, "
							+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), NEW_INV_SEQ_NO, SUP_STATE_CODE+1, "
							+ "PRINT_BY_ORI, TO_CHAR(PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "PRINT_BY_DUP, TO_CHAR(PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "PRINT_BY_TRI, TO_CHAR(PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND CONTRACT_TYPE = 'I' ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						
					while(rset2.next()) 
					{
						
						cont_type = rset2.getString(1);
						inv_seq = rset2.getString(2);
						fin_yr = rset2.getString(3);
						inv_dt = rset2.getString(4);
						inv_dt = inv_dt.replaceAll("/", "");
						new_inv = rset2.getString(5);
						state_tin = rset2.getString(6);
						o_by = rset2.getString(7);
						o_dt = rset2.getString(8);
						d_by = rset2.getString(9);
						d_dt = rset2.getString(10);
						t_by = rset2.getString(11);
						t_dt = rset2.getString(12);
						abbr = rset.getString(3);
//						System.out.println(cont_type);
						
						int index=0;
						queryString1 = "SELECT '2', NULL , NULL, NULL, INV_TYPE, PDF_INV_NM, NULL, NULL, NULL, NULL, PDF_SIGNED_FLAG, SIGNED_BY, "
								+ "TO_CHAR(SIGNED_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, MAIL_SENT_FLAG, NULL, TO_CHAR(MAIL_SENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
								+ "FROM DLNG_INV_PDF_DTL "
								+ " WHERE INV_TYPE IN ('O','D','T') AND (PDF_INV_NM LIKE ? AND PDF_INV_NM LIKE ?)";
								
						stmt1 = conn.prepareStatement(queryString1);
						if(cont_type.equals("I")) {
							stmt1.setString(++index,"%-I-"+inv_seq+"%");
							stmt1.setString(++index,"%-"+inv_dt+"-%");
						}
						rset1 = stmt1.executeQuery();
						
						while(rset1.next())
						{	
							pdf_type = rset1.getString(5);
							
							str = "";
							
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
							}
							
							str += abbr + ",";
							
							for (int i = 1; i < columns.split(",").length; i++) {
								
							 
							 if (i == 1) {	//FINANCIAL_YEAR
								value = fin_yr;
								str += value + ",";
							 }
							 else if(i == 2) {	//INVOICE_NO
								 str += inv_seq + ",";
							 }
							 else if(i == 3) {	//INVOICE_SEQ
								 str += new_inv + ",";
							 }
							 else if (i == 5) {
								if(rset1.getString(11)!=null && rset1.getString(11).equals("Y")) {
									value = "LATEPAY_"+rset1.getString(6)+"-"+pdf_type+".pdf";
								}else 
								{
									value = rset1.getString(6)+"-"+pdf_type+".pdf";
								}
									str += value + ",";
								}
							 else if(i == 6) {	//ENT_BY
								if(pdf_type.equals("O")) {
									value = o_by;
							 }
								
							 else if(pdf_type.equals("D")) {
									value = d_by;
							 }
								
							 else if(pdf_type.equals("T")) {
									value = t_by;
							 }
								str += value + ",";
							 }
							
							else if(i == 7) {	//ENT_DT
								if(pdf_type.equals("O")) {
									value = o_dt;
								}
								
								else if(pdf_type.equals("D")) {
									value = d_dt;
								}
								
								else if(pdf_type.equals("T")) {
									value = t_dt;
								}
								str += value + ",";
							}
							
//							else if(i == 12) {
//								value = rset1.getString(13);
//								str += value + ",";
//							}
								
								else {
									value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
									str += value + ",";
								}
								
							}
							logger.insert_data(fname_csv, str, conn);
							count++;
							logger.data(fname, (company_cd+",24" + ","+inv_seq+ "," +fin_yr+","+pdf_type+","), conn, "");
							
							
						}					
						stmt1.close();
						rset1.close();
					}
					rset2.close();
					stmt2.close();
					}
				rset.close();
				stmt.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INV_FILE_DTL_LP>>,,,,", conn);
				
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
			}
			
		}
	   
	   public void FMS_DLNG_FFLOW_INV_MST_SERV() throws SQLException, IOException {
			
			function_nm = "FMS_DLNG_FFLOW_INV_MST_SERV()";
			
			try {

				System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
				logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_FFLOW_INV_MST_SERV>>,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
				
				columns = "COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
						+ "BU_STATE_TIN,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,"
						+ "INVOICE_CATEGORY,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,INVOICE_TYPE,LINKED_INVOICE,NUM_LINE,"
						+ "NOTE,GROSS_AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT_INR,"
						+ "TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,"
						+ "CHECKED_FLAG,CHECKED_BY,CHECKED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,"
						+ "MODIFY_DT,OTHER_INV_STR,AMT_WORD,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,"
						+ "PRINT_BY_DUP,PRINT_DT_DUP,INVOICE_ID_SEQ,TDS_STRUCT_CD,TDS_EFF_DT,TCS_STRUCT_CD,TCS_EFF_DT,"
						+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,"
						+ "PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,"
						+ "TCS_FACTOR,TCS_TDS,TCS_AMT,ALLOC_QTY,SUB_INV_TYPE,FIN_SYS,HOLD_AMT,CARGO_NO,SAC_CD";

//				workbook = new XSSFWorkbook();
//				spreadsheet = workbook.createSheet("Sheet 1");

//				nrow = 0;
//				ncell = 0;
				count = 0;
				String cont_type="",exch_cd="",aprv_flg="",chk_flg="",chk_dt="",chk_by="",exch_dt="",sup_plnt_cd="",agmt_no = "",agmt_rev="",pdf_inv="";
				
				String fname_csv = "", str = "",sac_code="";
				fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_MST_SERV_" + start_end_dt + ".csv";
				
				FileWriter fw = new FileWriter(fname_csv, false); 
				fw.close();
				
				// Inserting Column Names
				for (int i = 0; i < columns.split(",").length; i++) {
					str += columns.split(",")[i] + ",";
				}
				logger.insert_data(fname_csv, str, conn);

				logger.checkpoint(fname, "COUNTERPARTY_ABBR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO,TIMESTAMP,", conn);
				
				// Inserting Rest of the data
				queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
						+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
						+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				
				while (rset.next()) {
					
					String mail="",sale_unit="",sale_amt="",tax_dtl="",inv_dt="",inv_no="";
					queryString2 = " SELECT A.FINANCIAL_YEAR,A.CUSTOMER_CD,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CONTRACT_TYPE,A.SUP_PLANT_CD + 1,A.SUP_STATE_CODE,NULL,"
							+ "NULL,A.CONTACT_PERSON_CD,A.HLPL_INV_SEQ_NO,A.NEW_INV_SEQ_NO,TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'),'S',NULL,TO_CHAR(A.PERIOD_START_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS'),'LP',"
							+ "A.REMARK_SPECIFICATION,NULL,A.REMARK_1,A.GROSS_AMT_USD,A.EXCHG_RATE_CD,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EXCHG_RATE_VALUE,'1',A.GROSS_AMT_INR,A.TAX_AMT_INR,"
							+ "A.TAX_STRUCT_CD,NULL,A.NET_AMT_INR,NULL,NULL,A.NET_AMT_INR,NULL,A.CHECKED_FLAG, A.CHECKED_BY, TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.APPROVED_FLAG,"
							+ "A.APPROVED_BY, TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,A.PDF_INV_DTL,A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_TRI,"
							+ "TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_DUP, TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'),A.HLPL_INV_SEQ_NO,NULL,NULL,NULL,NULL,A.SUN_APPROVAL,A.SUN_APPROVAL_BY,"
							+ "TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),A.PAY_RECV_AMT,TO_CHAR(A.PAY_RECV_DT, 'DD/MM/YYYY HH24:MI:SS'),A.PAY_INSERT_BY, TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_UPDATE_BY, TO_CHAR(A.PAY_UPDATE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.PAY_REMARK,A.TDS_PERCENT,A.TDS_TAX_AMT,"
							+ "A.TDS_PERCENT,A.TDS_TAX_AMT,NULL,NULL,NULL,A.TOTAL_QTY,NULL,NULL,NULL,NULL,NULL "
							+ "FROM DLNG_INVOICE_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND A.CONTRACT_TYPE = 'M' "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset.getString(2));
					stmt2.setString(2, delta_FromDt);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_ToDt);
					stmt2.setString(5, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						str = "";
						abbr = rset.getString(1);	
						cd = rset2.getString(2);
						agmt_no = rset2.getString(3);
						agmt_rev = rset2.getString(4);
						cont_no = rset2.getString(5);
						cont_rev = rset2.getString(6);
						cont_type = rset2.getString(7);
						fin_yr = rset2.getString(1);
						inv_seq = rset2.getString(13);
						inv_dt = rset2.getString(15);
						inv_no = rset2.getString(14);
						state_code = rset2.getString(9);
						
//						map = "S-"+agmt_no+"-"+agmt_rev+"-"+cont_no+"-"+cont_rev;
						
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }

						str += abbr + ",";
						
						for (int i = 1; i < columns.split(",").length; i++) {
							value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll("\n", " ");
							value = value.replaceAll(",", "_");
							value = value.replaceAll("\r", "");
							
						if (i == 1) { 		
							str += fin_yr + ",";
						}
						else if(i == 2) {
							str += rset2.getString(22) + ",";
						}	
						else if(i == 12) {
								queryString3 = "SELECT A.EMAIL FROM FMS7_CUSTOMER_CONTACT_MST A WHERE A.SEQ_NO = ? AND A.CUSTOMER_CD = ? "
										+ "AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS7_CUSTOMER_CONTACT_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
										+ "AND A.SEQ_NO = B.SEQ_NO)";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, rset2.getString(12));
								stmt3.setString(2, cd);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									mail = rset3.getString(1);
									if(mail != null) {
										if(!mail.equals("")) {
										mail = mail.replaceAll(",", "");
										}
									}
								}
								stmt3.close();
								rset3.close();
								str += mail + ",";		
						}
						else if (i == 17) { 
								
								String frq="0";
								
								str += frq + ",";		
						}
						else if (i == 24 && value.length() > 250) {
								value = value.substring(0, 250);
								str += value + ",";
						}
		 					
						else if(i == 26) {
								value = rset2.getString(26);
								name="";
								queryString3="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD=?";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								   if(rset3.next()) {	
									   name = rset3.getString(1);
									   name= name.toUpperCase();
									   if (name.contains("CUSTOMS RATE")) {
											name = "CUSTOM EXCHANGE RATE";
										}
										else if (name.contains("RBI REFERENCE")) {
											name = "RBI REFERENCE RATE";
										}
										else if (name.contains("SBI MUMBAI TT AVERAGE")) {
											name = "SBI MUMBAI TT BUY SELL";
										}
										else if (name.contains("SBI TT BUYING")) {
											name = "SBI RATE BUY";
										}
										else if (name.contains("SBI TT SELLING")) {
											name = "SBI RATE SELL";
										}
										else if (name.contains("SBI TT BUY SELL")) {
											name = "SBI RATE BUY SELL";
										}								
								   }								   								  
								   stmt3.close();
								   rset3.close();
			//							   cell.setCellValue("'"+name.trim()+"'");
									name = name.replaceAll(",", " ");
									name = name.replaceAll("\n", " ");
									name = name.replaceAll("\r", " ");
									name = name.replaceAll("\"", " ");
									str += name.trim() + ",";
							}
			//						}
							else if(i==32 && !value.equals("null")) {
								queryString4="SELECT REMARK FROM FMS7_TAX_STRUCTURE WHERE TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY APP_DATE DESC";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1,value);
								stmt4.setString(2, rset2.getString(18));
								rset4 = stmt4.executeQuery();
								   if(rset4.next()) {	
									   tax_dtl = rset4.getString(1);
									   tax_dtl = tax_dtl.replaceAll(",", "@ ");
									   tax_dtl = tax_dtl.replaceAll("\n", " ");
									   tax_dtl = tax_dtl.replaceAll("\r", " ");
									   tax_dtl = tax_dtl.replaceAll("\"", " ");
									   if (tax_dtl.contains("MH")) {
										   tax_dtl = tax_dtl.split(" MH")[0] + "%";
										}
										else if (tax_dtl.contains("AP")) {
											tax_dtl = tax_dtl.split(" AP")[0] + "%";
										}
										else if (tax_dtl.contains("ADD. VAT")) {
											tax_dtl = tax_dtl.replace("ADD. VAT", "ADVAT");
										}
										else if (tax_dtl.contains("STAX 0%")) {
											tax_dtl = tax_dtl.replace("STAX 0%", "ZSTAX 0%");
										}
								   }else {
									   tax_dtl = null; 
								   }							   								  
								   stmt4.close();
								   rset4.close();
								  
								   str += tax_dtl + ",";
							}
							else if(i == 49) {
								value = "DEBIT NOTE";
								str += value + ",";
							}
							else if(i == 51) {
								pdf_inv = rset2.getString(51);
								if(pdf_inv!=null) {
									if(pdf_inv.contains("T")) {
										pdf_inv = "T";
									}else if(pdf_inv.contains("D")) {
										pdf_inv = "D";
									}else if(pdf_inv.contains("O")) {
										pdf_inv ="O";
									}else {
										pdf_inv = null;
									}
								}else {
									pdf_inv = null;
								}
								
								str += pdf_inv + ",";
							}
							else if(i == 59) {
								if(rset2.getString(73)!=null) {
									String tds_per = rset2.getString(73);
									str += "TDS "+tds_per+"%" + ",";
								}else {
									String tds_per = null;
									str += tds_per + ",";
								}
							}
							else if(i == 74) {
								if(rset2.getString(73)!=null){
									double tds_per = rset2.getDouble(73);
									double gross_amt = rset2.getDouble(30);
									
									BigDecimal tdsPerBD = BigDecimal.valueOf(tds_per);
									BigDecimal grossAmtBD = BigDecimal.valueOf(gross_amt);
									
									BigDecimal tdsAmtBD = grossAmtBD.multiply(tdsPerBD).divide(BigDecimal.valueOf(100));
									str += tdsAmtBD+ ",";
								}else {
									BigDecimal tdsAmtBD=null;
									str += tdsAmtBD + ",";
								}
							}
						  
							else if(i == 76) {
								if(rset2.getString(73)!=null){
									double tds_per = rset2.getDouble(73);
									double tax_amt = rset2.getDouble(31);
									
									BigDecimal tdsPerBD = BigDecimal.valueOf(tds_per);
									BigDecimal taxAmtBD = BigDecimal.valueOf(tax_amt);
									
									BigDecimal tdsAmtBD = taxAmtBD.multiply(tdsPerBD).divide(BigDecimal.valueOf(100));
									str += tdsAmtBD + ",";
							}else {
								BigDecimal tdsAmtBD=null;
								str += tdsAmtBD + ",";
							}
			
							}
//							else if(i == 77) {
//								queryString3 = "SELECT TAX_CODE FROM FMS7_TAX_MST  WHERE TAX_ALIAS_CODE = ? ";
//								stmt3 = conn.prepareStatement(queryString3);
//								stmt3.setString(1, "TCS");
//								rset3 = stmt3.executeQuery();
//								if(rset3.next()) {
//									tax_code=rset3.getString(1);
//								}
//								rset3.close();
//								stmt3.close();
//								
//								
//								queryString3 = "SELECT FACTOR FROM FMS7_TAX_STRUCTURE_DTL  WHERE TAX_CODE = ? AND TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY APP_DATE DESC ";
//								stmt3 = conn.prepareStatement(queryString3);
//								stmt3.setString(1, tax_code);
//								stmt3.setString(2, "22");
//								stmt3.setString(3, inv_dt);
//								rset3 = stmt3.executeQuery();
//								if(rset3.next()) {
//									value = rset3.getString(1);
//									}
//								else
//								{
//									value ="0.075";	
//								}
//								rset3.close();
//								stmt3.close();
//								str += value + ",";
//							} 
							else if(i == 78) {
								tcs_tds="";

								queryString3="SELECT TCS_AMT,TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_INVOICE_TCS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG'";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, cd);
								stmt3.setString(2, inv_seq);
								stmt3.setString(3, fin_yr);
								stmt3.setString(4, cont_type);
								stmt3.setString(5, state_code);
								
								rset3 = stmt3.executeQuery();
								   if(rset3.next()) {
									   tcs_amt = rset3.getString(1);
									   tcs_date = rset3.getString(2);
									   tcs_tds = "TCS";								
								   }else {
									   queryString4="SELECT TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), TDS_AMT FROM FMS7_INVOICE_TDS_DTL WHERE CUSTOMER_CD = ? AND HLPL_INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND SUP_STATE_CODE = ? AND COMMODITY_TYPE = 'DLNG'";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1, cd);
										stmt4.setString(2, inv_seq);
										stmt4.setString(3, fin_yr);
										stmt4.setString(4, cont_type);
										stmt4.setString(5, state_code);
										
										rset4 = stmt4.executeQuery();
										   if(rset4.next()) {
											  tds_date = rset4.getString(1); 
											  tds_amt = rset4.getString(2); 
											  tcs_tds = "TDS";								
										   }else {
											   tcs_tds = "NA";
											   tds_date = null;
										   }								   								  
										stmt4.close();
										rset4.close();
										
										tcs_date = null;
										tcs_amt = null;
								   }								   								  
								stmt3.close();
								rset3.close();

//								cell.setCellValue("'"+tcs_tds+"'")
								tcs_tds = tcs_tds.replaceAll(",", " ");
								tcs_tds = tcs_tds.replaceAll("\n", " ");
								tcs_tds = tcs_tds.replaceAll("\r", " ");
								tcs_tds = tcs_tds.replaceAll("\"", " ");
								str += tcs_tds + ",";
							}
							else if(i == 79) {
								str += tcs_amt + ",";
							}
							else if(i == 82) {
								if(rset2.getString(63)!=null) {
									str += "S,";
								}else {
									str += null + ",";
								}
							}
							else if(i == 83) {	//HOLD_AMT
								queryString3 = "SELECT A.HOLD_AMOUNT FROM FMS8_PAY_RECV_DTL A WHERE A.COMMODITY_TYPE = 'DLNG' AND A.NEW_INV_SEQ_NO = ? "
										+ "AND A.CONTRACT_TYPE = 'V' AND A.REV_NO = (SELECT MAX(C.REV_NO) FROM FMS8_PAY_RECV_DTL C WHERE A.NEW_INV_SEQ_NO = C.NEW_INV_SEQ_NO "
										+ "AND A.CONTRACT_TYPE = C.CONTRACT_TYPE AND A.COMMODITY_TYPE = C.COMMODITY_TYPE)";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, inv_no);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								str += value + ",";
							}
							
							else {
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
					
				}
				stmt.close();
				rset.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_FFLOW_INV_MST_SERV>>,,,,", conn);
				
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
			}
			
		}
	   
	   public void FMS_DLNG_FFLOW_INV_FILE_DTL_SERV() throws SQLException, IOException {
			
			function_nm = "FMS_DLNG_FFLOW_INV_FILE_DTL_SERV()";
			
			try {
				
				System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
				logger.checkpoint(fname, "<<START>>,<<FMS_DLNG_FFLOW_INV_FILE_DTL_SERV>>,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
				
				columns = "COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,INVOICE_TYPE,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT,EMAIL_SENT,EMAIL_SENT_BY,EMAIL_SENT_DT";
				count = 0;
				
				String inv_seq= "",inv_dt="",ent_by="",ent_dt="",pdf_type="",file_nm="";
				String o_by= "",o_dt="",d_by="",d_dt="",t_by="",t_dt="";
				String fname_csv = "", str = "", state_tin="",new_inv="";
				
				
				fname_csv = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_FILE_DTL_SERV_" + start_end_dt + ".csv";
				
				FileWriter fw = new FileWriter(fname_csv, false); 
				fw.close();
				
				// Inserting Column Names
				for (int i = 0; i < columns.split(",").length; i++) {
					str += columns.split(",")[i] + ",";
				}
				logger.insert_data(fname_csv, str, conn);
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,INVOICE_TYPE,TIMESTAMP,", conn);
				
				queryString = "SELECT DISTINCT(A.CUSTOMER_CD), A.EFF_DT, A.CUSTOMER_ABBR  FROM FMS7_CUSTOMER_MST A "
						+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "ORDER BY A.CUSTOMER_CD, A.EFF_DT DESC  ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					cd=rset.getString(1);
					
					queryString2 = "SELECT CONTRACT_TYPE, HLPL_INV_SEQ_NO, FINANCIAL_YEAR, "
							+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), NEW_INV_SEQ_NO, SUP_STATE_CODE+1, "
							+ "PRINT_BY_ORI, TO_CHAR(PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "PRINT_BY_DUP, TO_CHAR(PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "PRINT_BY_TRI, TO_CHAR(PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM DLNG_INVOICE_MST WHERE CUSTOMER_CD = ? AND CONTRACT_TYPE = 'M' ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						rset2 = stmt2.executeQuery();
						
					while(rset2.next()) 
					{
						
						cont_type = rset2.getString(1);
						inv_seq = rset2.getString(2);
						fin_yr = rset2.getString(3);
						inv_dt = rset2.getString(4);
						inv_dt = inv_dt.replaceAll("/", "");
						new_inv = rset2.getString(5);
						state_tin = rset2.getString(6);
						o_by = rset2.getString(7);
						o_dt = rset2.getString(8);
						d_by = rset2.getString(9);
						d_dt = rset2.getString(10);
						t_by = rset2.getString(11);
						t_dt = rset2.getString(12);
						abbr = rset.getString(3);
//						System.out.println(cont_type);
						
						int index=0;
						queryString1 = "SELECT '2', NULL , NULL, NULL, 'LP', INV_TYPE, PDF_INV_NM, NULL, NULL, NULL, NULL, PDF_SIGNED_FLAG, SIGNED_BY, "
								+ "TO_CHAR(SIGNED_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, MAIL_SENT_FLAG, NULL, TO_CHAR(MAIL_SENT_DT, 'DD/MM/YYYY HH24:MI:SS') "
								+ " FROM DLNG_INV_PDF_DTL "
								+ " WHERE INV_TYPE IN ('O','D','T') AND (PDF_INV_NM LIKE ? AND PDF_INV_NM LIKE ?)";
								
						stmt1 = conn.prepareStatement(queryString1);
						if(cont_type.equals("M")) {
							stmt1.setString(++index,"%-M-"+inv_seq+"%");
							stmt1.setString(++index,"%-"+inv_dt+"-%");
						}
						rset1 = stmt1.executeQuery();
						
						while(rset1.next())
						{	
							pdf_type = rset1.getString(6);
							String inv_type = rset1.getString(5);
							
							str = "";
							
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
							}
							
							str += abbr + ",";
							
							for (int i = 1; i < columns.split(",").length; i++) {
								
							 
							 if (i == 1) {	//FINANCIAL_YEAR
								value = fin_yr;
								str += value + ",";
							 }
							 else if(i == 2) {	//INVOICE_NO
								 str += inv_seq + ",";
							 }
							 else if(i == 3) {	//INVOICE_SEQ
								 str += new_inv + ",";
							 }
							 else if (i == 6) {
								if(rset1.getString(12)!=null && rset1.getString(12).equals("Y")) {
										value = "LATEPAY_"+rset1.getString(7)+"-"+pdf_type+".pdf";
								}else 
								{
										value = rset1.getString(7)+"-"+pdf_type+".pdf";
								}
								str += value + ",";
							}
							 else if(i == 7) {	//ENT_BY
								if(pdf_type.equals("O")) {
									value = o_by;
							 }
								
							 else if(pdf_type.equals("D")) {
									value = d_by;
							 }
								
							 else if(pdf_type.equals("T")) {
									value = t_by;
							 }
								str += value + ",";
							 }
							
							else if(i == 8) {	//ENT_DT
								if(pdf_type.equals("O")) {
									value = o_dt;
								}
								
								else if(pdf_type.equals("D")) {
									value = d_dt;
								}
								
								else if(pdf_type.equals("T")) {
									value = t_dt;
								}
								str += value + ",";
							}
							
							else if(i == 12) {
								value = rset1.getString(13);
								str += value + ",";
							}
								
								else {
									value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
									str += value + ",";
								}
								
							}
							logger.insert_data(fname_csv, str, conn);
							count++;
							logger.data(fname, (company_cd+",24" + ","+inv_seq+ "," +fin_yr+","+pdf_type+","+inv_type+","), conn, "");
							
							
						}					
						stmt1.close();
						rset1.close();
					}
					rset2.close();
					stmt2.close();
					}
				rset.close();
				stmt.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_FFLOW_INV_FILE_DTL_SERV>>,,,,", conn);
				
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
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}	
	
	public void makeDirectory() {
		function_nm = "makeDirectory()";
		try {
			String fName[] = {(migration_setup_dir+"EXPORT"), (migration_setup_dir+"DataLogs"), (migration_setup_dir+"DataLogs/RollBack"), (migration_setup_dir+"DataLogs/Extractor"), (migration_setup_dir+"DataLogs/Reader")};
			
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
