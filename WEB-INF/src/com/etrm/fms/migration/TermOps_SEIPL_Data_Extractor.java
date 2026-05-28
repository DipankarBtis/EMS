
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
import java.util.prefs.Preferences;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.SystemErrorLogger;

public class TermOps_SEIPL_Data_Extractor {

	String db_src_file_name = "TermOps_SEIPL_Data_Extractor.java";
	String function_nm = "";
	
	String migration_setup_dir = "";
	
	String sysDateTime = "";
	
	String fname = "";
	String fname_error = "";
	
	String fname1 = "";

	DataMigration_Logger logger = new DataMigration_Logger();

	String queryString = "", queryString1 = "";
	Connection conn;
	ResultSet rset, rset1;
	PreparedStatement stmt, stmt1;

	String dbline = "", username = "", encrypted = "", password = "";
	String columns = "", filename = "", value = "";
	
	String checked_values = "", msg = "", msg_type = "";
	
	final String company_cd = "2";
	String cd = "", eff_dt = "";
	
	String delta_FromDt = null;
	String delta_ToDt = null;
	String start_end_dt = null;
	
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
			makeDirectory();
			
			fname = "DataLogs/Extractor/TermOps_SEIPL_Data_Extractor(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Extractor/TermOps_SEIPL_Data_Extractor_Error(log)"+sysDateTime+".csv";
			
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
				
				if (checked_values.contains("FMS_TANK_MST")) {
					FMS_TANK_MST();
				}
				if (checked_values.contains("FMS_TANK_CONSUMPTION_MST")) {
					FMS_TANK_CONSUMPTION_MST();
				}
				if (checked_values.contains("FMS_TANK_INVENTORY_DTL")) {
					FMS_TANK_INVENTORY_DTL();
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

	public void FMS_TANK_MST() throws SQLException, IOException {
		function_nm = "FMS_TANK_MST()";
		
		try {

			System.out.println("<<START>><<FMS_TANK_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TANK_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,TANK_CD,TANK_NAME,EFF_DT,STATUS,TANK_T1_VOLUME,TANK_T1_HEIGHT,TANK_T2_VOLUME,TANK_T2_HEIGHT,TANK_D1_VOLUME,TANK_D1_HEIGHT,TANK_D2_VOLUME,TANK_D2_HEIGHT,TANK_DIAMETER,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TANK_PI_TAG";

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

			// Below block of code is for inserting data for Tank-1
			queryString = " SELECT '2', '1', 'TANK-1', TO_CHAR(TANK_DTL_DT, 'DD/MM/YYYY HH24:MI:SS'), FLAG, TANK1_T1_VOLUME, TANK1_T1_HEIGHT, TANK1_T2_VOLUME, TANK1_T2_HEIGHT, TANK1_D1_VOLUME, TANK1_D1_HEIGHT, TANK1_D2_VOLUME, TANK1_D2_HEIGHT, TANK1_DIAMETER, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS7_TANK_MASTER_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,TANK_CD,EFF_DT,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(2);
				eff_dt = rset.getString(4);
				
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

			// Below block of code is for inserting data for Tank-2
			queryString = " SELECT '2', '2', 'TANK-2', TO_CHAR(TANK_DTL_DT, 'DD/MM/YYYY HH24:MI:SS'), FLAG, TANK2_T1_VOLUME, TANK2_T1_HEIGHT, TANK2_T2_VOLUME, TANK2_T2_HEIGHT, TANK2_D1_VOLUME, TANK2_D1_HEIGHT, TANK2_D2_VOLUME, TANK2_D2_HEIGHT, TANK2_DIAMETER, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS7_TANK_MASTER_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				cd = rset.getString(2);
				eff_dt = rset.getString(4);
				
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

			// Below block of code is for inserting data for Tank-3
			queryString = " SELECT '2', '3', 'TANK-3', TO_CHAR(TANK_DTL_DT, 'DD/MM/YYYY HH24:MI:SS'), FLAG, TANK3_T1_VOLUME, TANK3_T1_HEIGHT, TANK3_T2_VOLUME, TANK3_T2_HEIGHT, TANK3_D1_VOLUME, TANK3_D1_HEIGHT, TANK3_D2_VOLUME, TANK3_D2_HEIGHT, TANK3_DIAMETER, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS7_TANK_MASTER_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				cd = rset.getString(2);
				eff_dt = rset.getString(4);
				
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

			filename = migration_setup_dir + "EXPORT/FMS_TANK_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TANK_MST>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_TANK_MST>>");
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

	public void FMS_TANK_CONSUMPTION_MST() throws SQLException, IOException {
		function_nm = "FMS_TANK_CONSUMPTION_MST()";
		
		try {

			System.out.println("<<START>><<FMS_TANK_CONSUMPTION_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TANK_CONSUMPTION_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,EFF_DT,PERCENTAGE,REMARK,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,FLAG";

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

			// Below block of code is for inserting data
			queryString = " SELECT '2', TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), PERCENTAGE, REMARK, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, FLAG FROM FMS7_CONSUMPTION_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,EFF_DT,TIMESTAMP,", conn);
			
			while (rset.next()) {
				eff_dt = rset.getString(2);
				
				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + eff_dt + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_TANK_CONSUMPTION_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TANK_CONSUMPTION_MST>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_TANK_CONSUMPTION_MST>>");
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

	public void FMS_TANK_INVENTORY_DTL() throws SQLException, IOException {
		function_nm = "FMS_TANK_INVENTORY_DTL()";
		
		try {

			System.out.println("<<START>><<FMS_TANK_INVENTORY_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TANK_INVENTORY_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,INV_LEVEL_DT,TANK_CD,TANK_VOLUME,TANK_HEIGHT,TANK_MMSCM,TANK_CONV_FACTOR_1,TANK_CONV_FACTOR_2,TANK_MMBTU,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

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

			// Below block of code is for inserting data for Tank-1
			queryString = " SELECT '2', TO_CHAR(INV_LEVEL_DT, 'DD/MM/YYYY HH24:MI:SS'), '1', T1_VOLUME, T1_HEIGHT, T1_MMSCM, T1_CONV_FACTOR_1, T1_CONV_FACTOR_2, T1_MMBTU, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL FROM FMS7_INVENTORY_LEVEL_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,TANK_CD,INV_LEVEL_DT,TIMESTAMP,", conn);
			
			while (rset.next()) {
				cd = rset.getString(3);
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

			// Below block of code is for inserting data for Tank-2
			queryString = " SELECT '2', TO_CHAR(INV_LEVEL_DT, 'DD/MM/YYYY HH24:MI:SS'), '2', T2_VOLUME, T2_HEIGHT, T2_MMSCM, T2_CONV_FACTOR_1, T2_CONV_FACTOR_2, T2_MMBTU, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL FROM FMS7_INVENTORY_LEVEL_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			
			while (rset.next()) {
				cd = rset.getString(3);
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

			/*// Below block of code is for inserting data for Tank-3
			queryString = " SELECT '2', TO_CHAR(INV_LEVEL_DT, 'DD/MM/YYYY HH24:MI:SS'), '3', T3_VOLUME, T3_HEIGHT, T3_MMSCM, T3_CONV_FACTOR_1, T3_CONV_FACTOR_2, T3_MMBTU, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL FROM FMS7_INVENTORY_LEVEL_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			
			while (rset.next()) {
				cd = rset.getString(3);
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
			rset.close();*/
			
			filename = migration_setup_dir + "EXPORT/FMS_TANK_INVENTORY_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TANK_INVENTORY_DTL>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_TANK_INVENTORY_DTL>>");
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
