package com.etrm.fms.migration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.prefs.Preferences;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;

		

public class RiskMgmt_SEIPL_Data_Reader {

	

	String db_src_file_name="RiskMgmt_SEIPL_Data_Reader.java";

	String migration_setup_dir = "";
	
	String queryString="", queryString1="", queryString2="";
	Connection conn, conn_fms8;
	ResultSet rset,rset1,rset2;
	PreparedStatement stmt,stmt1,stmt2;

	String dbline = "", username = "", encrypted = "", password = "";
	String function_nm = "", columns = "", data = "", table_name = "";
	
	String sysDateTime = "";

	String delta_FromDt = null;
	String delta_ToDt = null;
	String start_end_dt = null;
	
	String fname = "";
	String fname_error = "";
	
	String fname1 = "";

	int index = 0;
	int logger_count = 0;
	int skipped_count=0;  
	int total_count=0;  
	
	final String company_cd = "2";
	String cd = "", eff_dt = "", abbr = "", cont_ref = "", agmt_no = "", cont_no = "", cont_type = "", seq_no = "";
	
	String bank_cd = "0", limit = "0", gx = "0";

	String checked_values = "", msg = "", msg_type = "";
	String transporter_map = "", meter_map = "";

	File file1 = null;
	FileInputStream file = null;
	XSSFWorkbook workbook = null;
	XSSFSheet sheet = null;
	Iterator<Row> rowIterator = null;
	Iterator<Cell> cellIterator = null;
	Row row;
	Cell cell;
	BufferedWriter out;
	
	DataMigration_Logger logger = new DataMigration_Logger();
	
	public void init() {

		function_nm="init()";
		try {

			getmail_list();
			
			fname = "DataLogs/Reader/RiskMgmt_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/RiskMgmt_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
			fname = migration_setup_dir + fname;
			fname_error = migration_setup_dir + fname_error;
			
			fname1 = "DataLogs/Script_Status(log).csv";	
			fname1 = migration_setup_dir + fname1;

			Preferences preferences =  Preferences.userRoot().node("/processFlag");

			Context initContext = new InitialContext();
	    	if(initContext == null)
	    	{
	    		throw new Exception("Boom - No Context");
	    	}

	    	Context envContext  = (Context)initContext.lookup("java:/comp/env");
	    	DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
	    	if(ds != null)
	    	{
	    		preferences.put("Flag", "0");
	    		conn = ds.getConnection();
	    		
	    		// Connection for FMS8 database
	    		Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("FMS8 DBLINE:"+dbline);
				conn_fms8 = DriverManager.getConnection(dbline, username, password);
				
				
				if(conn != null && !checked_values.equals(""))  
				{
					conn.setAutoCommit(false);
					
					if (checked_values.contains("FMS_LIMIT_MST")) {
						FMS_LIMIT_MST();
					}
					if (checked_values.contains("FMS_LIMIT_DTL")) {
						FMS_LIMIT_DTL();
					}
					if (checked_values.contains("FMS_FORWARD_PRICE_DTL")) {
						FMS_FORWARD_PRICE_DTL();
					}	
					if (checked_values.contains("FMS_SPOT_PRICE_DTL")) {
						FMS_SPOT_PRICE_DTL();
					}
					if (checked_values.contains("FMS_CURVE_HOLIDAY_CALND")) {
						FMS_CURVE_HOLIDAY_CALND();
					}
					if (checked_values.contains("FMS_CURVE_SETTLE_CALND")) {
						FMS_CURVE_SETTLE_CALND();
					}
					if (checked_values.contains("FMS_MR_CONT_TAQ_DTL")) {
						FMS_MR_CONT_TAQ_DTL();
					}
					if (checked_values.contains("FMS_MR_EXPO_EOD_MST")) {
						FMS_MR_EXPO_EOD_MST();
					}
					if (checked_values.contains("FMS_MR_EXPO_EOD_DTL")) {
						FMS_MR_EXPO_EOD_DTL();
					}
					if (checked_values.contains("FMS_SECURITY_DEAL_MAP,")) {
						FMS_SECURITY_DEAL_MAP();
					}
					if (checked_values.contains("FMS_SECURITY_MST,")) {
						FMS_SECURITY_MST();
					}
					if (checked_values.contains("LOG_FMS_SECURITY_MST,")) {
						LOG_FMS_SECURITY_MST();
					}
					if (checked_values.contains("FMS_SECURITY_FILE_DTL,")) {
						FMS_SECURITY_FILE_DTL();
					}

		    		preferences.put("Flag", "1");
					
					conn.close();
					conn_fms8.close();
					
					conn = null;
					conn_fms8 = null;
				
				}
				else {
					msg = "No Checkbox was selected. Data Not Inserted.";
					msg_type = "E";
				}
	    	}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			msg = "One of the Functions faced an Error. Data Not Inserted.";
			msg_type = "E";
		}
		finally
		{
			try
			{
				if(rset != null){try{rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				
				if (file != null) {try{file.close();}catch(Exception e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				
				msg = "One of the Functions faced an Error. Data Not Inserted.";
				msg_type = "E";
			}
		}
		
	}


	public void FMS_LIMIT_MST() throws IOException, SQLException {

		function_nm="FMS_LIMIT_MST()";
		try {
			
			System.out.println("<<START>><<FMS_LIMIT_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_LIMIT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_LIMIT_MST(COUNTERPARTY_CD,BANK_CD,GX,LIMIT_ID,CREDIT_RATING,RATING_EFF_DT,PARENT_OWNSHIP_CD,PARENT_OWNSHIP,PARENT_ENT_DT,PARENT_EXIT_DT,REF_NO,STATUS,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE) VALUES(?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_LIMIT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LIMIT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CUST_CD,LIMIT_ID,BANK_CD,GX,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = "0"; 
						bank_cd = "0"; limit = "0"; gx = "0";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							if (cell.getColumnIndex() == 0) {
								cd = cell.getStringCellValue().toUpperCase();
								cd = cd.substring(1, cd.length()-1);
								
								if (!cd.equals("0") && cd.contains("IGX")) {	// IGX_Cd
									queryString = "SELECT COUNTERPARTY_CD FROM FMS_COMPANY_EXCHG_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									rset = stmt.executeQuery();
									if (rset.next()) {
										cd = rset.getString(1);
									}
									else {
										cd = "";
									}
									
									rset.close();
									stmt.close();
									
								}
								else if (!cd.equals("0")) {	// Counterparty_Cd
									queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									rset = stmt.executeQuery();
									if (rset.next()) {
										cd = rset.getString(1);
									}
									else {
										cd = "";
									}
									
									rset.close();
									stmt.close();
									
								}
								data = cd;
							}
							else if (cell.getColumnIndex() == 1) {	// Bank_Cd
								bank_cd = cell.getStringCellValue();
								bank_cd = bank_cd.substring(1, bank_cd.length()-1);
								
								if (!bank_cd.equals("0") && !bank_cd.equals("null")) {
									queryString = "SELECT BANK_CD FROM FMS_BANK_MST WHERE BANK_NAME = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, bank_cd);
									rset = stmt.executeQuery();
									if (rset.next()) {
										bank_cd = rset.getString(1);
									}
									else {
										bank_cd = "0";
									}
									
									rset.close();
									stmt.close();
									
								}
								data = bank_cd;
							}
							else if (cell.getColumnIndex() == 3) {	// Limit_Id
								limit = cell.getStringCellValue().toUpperCase();
								limit = limit.substring(1, limit.length()-1);
								
								if (!limit.equals("0")) {
									queryString = "SELECT LIMIT_ID+1 FROM FMS_LIMIT_MST WHERE COUNTERPARTY_CD = ? AND BANK_CD = ? AND GX = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									stmt.setString(2, bank_cd);
									stmt.setString(3, gx);
									rset = stmt.executeQuery();
									if (rset.next()) {
										limit = rset.getString(1);
									}
									
									rset.close();
									stmt.close();
									
								}
								data = limit;
							}
							else if (cell.getColumnIndex() == 6) {	// Parent_Cd
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue().toUpperCase();
								if(data != null) {
							    	data = data.substring(1, data.length()-1);
							    	
									queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, data);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										data = rset.getString(1);
									}
									else {
										data = "";
									}
									
									rset.close();
									stmt.close();
								}
							}
							else {
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
							    	data = data.substring(1, data.length()-1);
							    }
								
								if (cell.getColumnIndex() == 2) {	// Gx
									gx = data;
								}
								
							}
							
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT BANK_CD FROM FMS_LIMIT_MST WHERE COUNTERPARTY_CD = ? AND BANK_CD = ?  AND GX = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, bank_cd);
						//stmt.setString(3, limit);
						stmt.setString(3, gx);
						
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (cd + "," + limit + "," + bank_cd + "," + gx + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (cd + "," + limit + "," + bank_cd + "," + gx + ","), conn, "E");
						}
						rset.close();
						stmt.close();
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_LIMIT_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_LIMIT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch(Exception e)
		{

			msg = "One of the Functions faced an Error. Data Not Inserted.";
			msg_type = "E";
			
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		finally {
			conn.commit();
			if (file != null) {
				file.close();
			}
		}
		
	}
	
	public void FMS_LIMIT_DTL() throws IOException, SQLException {

		function_nm="FMS_LIMIT_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_LIMIT_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_LIMIT_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_LIMIT_DTL(COUNTERPARTY_CD,BANK_CD,LIMIT_ID,SEQ_NO,GX,REF_NO,LIMIT_TYPE,ACTION_TYPE,CATEGORY,AMT,AMT_UNIT,EFF_DT,EXP_DT,REVIEW_DT,REMARKS,IS_ACTIVE,INACTIVATION_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE) VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_LIMIT_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LIMIT_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CUST_CD,LIMIT_ID,BANK_CD,GX,SEQ_NO,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = "0"; 
						bank_cd = "0"; limit = "0"; gx = "0";
						String seq = "0";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							if (cell.getColumnIndex() == 0) {
								cd = cell.getStringCellValue().toUpperCase();
								cd = cd.substring(1, cd.length()-1);
								
								if (!cd.equals("0") && cd.contains("IGX")) {	// IGX_Cd
									queryString = "SELECT COUNTERPARTY_CD FROM FMS_COMPANY_EXCHG_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									rset = stmt.executeQuery();
									if (rset.next()) {
										cd = rset.getString(1);
									}
									else {
										cd = "";
									}
									
									rset.close();
									stmt.close();
									
								}
								else if (!cd.equals("0")) {	// Counterparty_Cd
									queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									rset = stmt.executeQuery();
									if (rset.next()) {
										cd = rset.getString(1);
									}
									else {
										cd = "";
									}
									
									rset.close();
									stmt.close();
									
								}
								data = cd;
							}
							else if (cell.getColumnIndex() == 1) {	// Bank_Cd
								bank_cd = cell.getStringCellValue();
								bank_cd = bank_cd.substring(1, bank_cd.length()-1);
								
								if (!bank_cd.equals("0") && !bank_cd.equals("null")) {
									queryString = "SELECT BANK_CD FROM FMS_BANK_MST WHERE BANK_NAME = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, bank_cd);
									rset = stmt.executeQuery();
									if (rset.next()) {
										bank_cd = rset.getString(1);
									}
									else {
										bank_cd = "0";
									}
									
									rset.close();
									stmt.close();
									
								}
								data = bank_cd;
							}
							else if (cell.getColumnIndex() == 2) {	// Limit_Id
								limit = cell.getStringCellValue().toUpperCase();
								limit = limit.substring(1, limit.length()-1);
								
								if (!limit.equals("0")) {
									queryString = "SELECT LIMIT_ID+1 FROM FMS_LIMIT_DTL WHERE COUNTERPARTY_CD = ? AND BANK_CD = ? AND GX = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									stmt.setString(2, bank_cd);
									stmt.setString(3, gx);
									rset = stmt.executeQuery();
									if (rset.next()) {
										limit = rset.getString(1);
									}
									
									rset.close();
									stmt.close();
									
								}
								data = limit;
							}
							else {
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
							    	data = data.substring(1, data.length()-1);
							    }
								
								if (cell.getColumnIndex() == 3) {	// Seq No
									seq = data;
								}
								else if (cell.getColumnIndex() == 4) {	// Gx
									gx = data;
								}
								
							}
							
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT BANK_CD FROM FMS_LIMIT_DTL WHERE COUNTERPARTY_CD = ? AND BANK_CD = ? AND LIMIT_ID = ? AND GX = ? AND SEQ_NO = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, bank_cd);
						stmt.setString(3, limit);
						stmt.setString(4, gx);
						stmt.setString(5, seq);
						
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (cd + "," + limit + "," + bank_cd + "," + gx + "," + seq + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (cd + "," + limit + "," + bank_cd + "," + gx + "," + seq + ","), conn, "E");
						}
						rset.close();
						stmt.close();
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_LIMIT_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_LIMIT_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch(Exception e)
		{

			msg = "One of the Functions faced an Error. Data Not Inserted.";
			msg_type = "E";
			
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		finally {
			conn.commit();
			if (file != null) {
				file.close();
			}
		}
		
	}
	
	public void FMS_FORWARD_PRICE_DTL() throws IOException, SQLException {

		function_nm="FMS_FORWARD_PRICE_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_FORWARD_PRICE_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_FORWARD_PRICE_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "REPORT_DT,CURVE_DT,CURVE_NM,COMMODITY_TYPE,CURVE_TYPE,CURVE_UNIT,PHYS_FIN,SETTLE_PRICE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			

			logger.checkpoint(fname, "REPORT_DT,CURVE_DT,CURVE_NM,", conn);

			queryString1 = "INSERT INTO FMS_FORWARD_PRICE_DTL(REPORT_DT,CURVE_DT,CURVE_NM,COMMODITY_TYPE,CURVE_TYPE,CURVE_UNIT,PHYS_FIN,SETTLE_PRICE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			
			queryString = "SELECT TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CURVE_DD_MM_YR, 'DD/MM/YYYY HH24:MI:SS'), CURVE_NM, COMMODITY_TYPE, CURVE_TYPE, CURVE_UNIT, PHYS_FIN, SETTLE_PRICE, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS9_FORWARD2_PRICE_DTL  ORDER BY ENT_DT DESC ";
			stmt = conn_fms8.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				stmt1 = conn.prepareStatement(queryString1);
				
				for (int i = 1; i <= columns.split(",").length; i++) {
					stmt1.setString(i, rset.getString(i));
				}
				
				queryString2 = "SELECT REPORT_DT FROM FMS_FORWARD_PRICE_DTL WHERE REPORT_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND CURVE_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND CURVE_NM = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset.getString(1));
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(3));
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next()) {
					
					logger.data(fname, (rset.getString(1) + "," + rset.getString(2) + "," + rset.getString(3) + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();

					logger_count++;
				}
				else {
					
					logger.data(fname, (rset.getString(1) + "," + rset.getString(2) + "," + rset.getString(3) + ","), conn, "E");
					
					stmt1.close();

					skipped_count++;
				}
				
				rset2.close();
				stmt2.close();
				
			}
			rset.close();
			stmt.close();
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_FORWARD_PRICE_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_FORWARD_PRICE_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch(Exception e)
		{

			msg = "One of the Functions faced an Error. Data Not Inserted.";
			msg_type = "E";
			
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		finally {
			conn.commit();
			if (file != null) {
				file.close();
			}
		}
		
	}
	
	public void FMS_SPOT_PRICE_DTL() throws IOException, SQLException {

		function_nm="FMS_SPOT_PRICE_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_SPOT_PRICE_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SPOT_PRICE_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "REPORT_DT,CURVE_DT,CURVE_NM,COMMODITY_TYPE,CURVE_TYPE,CURVE_UNIT,PHYS_FIN,SETTLE_PRICE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ACTUAL_CURVE";
			

			logger.checkpoint(fname, "REPORT_DT,CURVE_DT,CURVE_NM,", conn);

			queryString1 = "INSERT INTO FMS_SPOT_PRICE_DTL(REPORT_DT,CURVE_DT,CURVE_NM,COMMODITY_TYPE,CURVE_TYPE,CURVE_UNIT,PHYS_FIN,SETTLE_PRICE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ACTUAL_CURVE) VALUES(TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?) ";
			
			queryString = "SELECT TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CURVE_DD_MM_YR, 'DD/MM/YYYY HH24:MI:SS'), CURVE_NM, COMMODITY_TYPE, CURVE_TYPE, CURVE_UNIT, PHYS_FIN, SETTLE_PRICE, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL FROM FMS9_CURVE2_PRICE_DTL  ORDER BY ENT_DT DESC ";
			stmt = conn_fms8.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				stmt1 = conn.prepareStatement(queryString1);
				
				for (int i = 1; i <= columns.split(",").length; i++) {
					stmt1.setString(i, rset.getString(i));
					if(i == 13) {
						queryString2 = "SELECT CURVE_TYPE FROM FMS_PROD_CURVE_MAP WHERE PROD_TYPE = ? ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, rset.getString(3));
						rset2 = stmt2.executeQuery();
						
						if (rset2.next()) {
							stmt1.setString(i, rset2.getString(1));
						}
						rset2.close();
						stmt2.close();
					}
					
				}
				
				queryString2 = "SELECT REPORT_DT FROM FMS_SPOT_PRICE_DTL WHERE REPORT_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND CURVE_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND CURVE_NM = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset.getString(1));
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(3));
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next()) {
					
					logger.data(fname, (rset.getString(1) + "," + rset.getString(2) + "," + rset.getString(3) + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();

					logger_count++;
				}
				else {
					
					logger.data(fname, (rset.getString(1) + "," + rset.getString(2) + "," + rset.getString(3) + ","), conn, "E");
					
					stmt1.close();

					skipped_count++;
				}
				
				rset2.close();
				stmt2.close();
				
			}
			rset.close();
			stmt.close();
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_SPOT_PRICE_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SPOT_PRICE_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch(Exception e)
		{

			msg = "One of the Functions faced an Error. Data Not Inserted.";
			msg_type = "E";
			
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		finally {
			conn.commit();
			if (file != null) {
				file.close();
			}
		}
		
	}
	
	public void FMS_CURVE_HOLIDAY_CALND() throws IOException, SQLException {

		function_nm="FMS_CURVE_HOLIDAY_CALND()";
		try {
			
			System.out.println("<<START>><<FMS_CURVE_HOLIDAY_CALND>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_CURVE_HOLIDAY_CALND>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "CURVE_NM,HOLIDAY_DT,HOLIDAY_NM,STATUS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,HOLIDAY_LAST_UP";
			

			logger.checkpoint(fname, "HOLIDAY_DT,CURVE_NM,", conn);

			queryString1 = "INSERT INTO FMS_CURVE_HOLIDAY_CALND(CURVE_NM,HOLIDAY_DT,HOLIDAY_NM,STATUS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,HOLIDAY_LAST_UP) VALUES(?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?) ";
			
			queryString = "SELECT CURVE_NM, TO_CHAR(HOLIDAY_DT, 'DD/MM/YYYY HH24:MI:SS'), HOLIDAY_NM, FLAG, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL FROM FMS9_CURVE_HOLIDAY_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY ENT_DT DESC ";
			stmt = conn_fms8.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				stmt1 = conn.prepareStatement(queryString1);
				
				for (int i = 1; i <= columns.split(",").length; i++) {
					stmt1.setString(i, rset.getString(i));
				}
				
				queryString2 = "SELECT HOLIDAY_DT FROM FMS_CURVE_HOLIDAY_CALND WHERE HOLIDAY_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND CURVE_NM = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset.getString(2));
				stmt2.setString(2, rset.getString(1));
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next()) {
					
					logger.data(fname, (rset.getString(2) + "," + rset.getString(1) + ","), conn, "");	
					
					stmt1.executeUpdate();
					stmt1.close();

					logger_count++;
				}
				else {
					
					logger.data(fname, (rset.getString(2) + "," + rset.getString(1) + ","), conn, "E");
					
					stmt1.close();

					skipped_count++;
				}
				
				rset2.close();
				stmt2.close();
				
			}
			rset.close();
			stmt.close();
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_CURVE_HOLIDAY_CALND>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_CURVE_HOLIDAY_CALND>>,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch(Exception e)
		{

			msg = "One of the Functions faced an Error. Data Not Inserted.";
			msg_type = "E";
			
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		finally {
			conn.commit();
			if (file != null) {
				file.close();
			}
		}
		
	}
	
	public void FMS_CURVE_SETTLE_CALND() throws IOException, SQLException {

		function_nm="FMS_CURVE_SETTLE_CALND()";
		try {
			
			System.out.println("<<START>><<FMS_CURVE_SETTLE_CALND>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_CURVE_SETTLE_CALND>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "CURVE_NM,CONT_MONTH,SETTLE_START_DT,SETTLE_END_DT,SETTLE_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			

			logger.checkpoint(fname, "CURVE_NM,CONT_MONTH,", conn);

			queryString1 = "INSERT INTO FMS_CURVE_SETTLE_CALND(CURVE_NM,CONT_MONTH,SETTLE_START_DT,SETTLE_END_DT,SETTLE_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			
			queryString = "SELECT CURVE_NM, TO_CHAR(CONT_MM_YYYY, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(SETTLE_START_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(SETTLE_END_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(SETTLE_DT, 'DD/MM/YYYY HH24:MI:SS'), ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS9_SETTLE_CALND_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY ENT_DT DESC ";
			stmt = conn_fms8.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				stmt1 = conn.prepareStatement(queryString1);
				
				for (int i = 1; i <= columns.split(",").length; i++) {
					stmt1.setString(i, rset.getString(i));
				}
				
				queryString2 = "SELECT CONT_MONTH FROM FMS_CURVE_SETTLE_CALND WHERE CONT_MONTH = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND CURVE_NM = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset.getString(2));
				stmt2.setString(2, rset.getString(1));
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next()) {
					
					logger.data(fname, (rset.getString(2) + "," + rset.getString(1) + ","), conn, "");	// CHECK
					
					stmt1.executeUpdate();
					stmt1.close();

					logger_count++;
				}
				else {
					
					logger.data(fname, (rset.getString(2) + "," + rset.getString(1) + ","), conn, "E");
					
					stmt1.close();

					skipped_count++;
				}
				
				rset2.close();
				stmt2.close();
				
			}
			rset.close();
			stmt.close();
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_CURVE_SETTLE_CALND>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_CURVE_SETTLE_CALND>>,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch(Exception e)
		{

			msg = "One of the Functions faced an Error. Data Not Inserted.";
			msg_type = "E";
			
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		finally {
			conn.commit();
			if (file != null) {
				file.close();
			}
		}
		
	}
	
public void FMS_MR_CONT_TAQ_DTL() throws IOException, SQLException {
		
		function_nm="FMS_MR_CONT_TAQ_DTL()";
		try {
			
			table_name = "FMS_MR_CONT_TAQ_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			
			queryString1 = "INSERT INTO FMS_MR_CONT_TAQ_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,ASED_TCQ,ASED_DCQ,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_MR_CONT_TAQ_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_MR_CONT_TAQ_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,CONT_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					agmt_no = "";
					abbr = "";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
				    cellIterator = row.cellIterator();
				    while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						data = null;
						
				    	if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 
				    		abbr = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!abbr.equals("NULL")) {
								abbr = abbr.substring(1, abbr.length() - 1);
							}	
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		} else {
				    			cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	
				    	else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			queryString = "SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO LIKE ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref+"%");
					    		stmt.setString(3, cont_ref.split("-")[0]);
					    		rset = stmt.executeQuery();
					    		
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			cont_no = rset.getString(2);
					    			cont_type = rset.getString(3);
					    		} else {
					    			agmt_no = "";
					    			cont_no = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		} 
				    		else if(cont_ref.contains("D")){
				    			queryString = "SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ? AND COMPANY_CD = '2'";
				    			stmt = conn.prepareStatement(queryString);
		    					stmt.setString(1, "%"+cont_ref+"%");
					    		rset = stmt.executeQuery();
					    		if(rset.next()) {
					    			agmt_no = rset.getString(1);
					    			cont_no = rset.getString(2);
					    			cont_type = rset.getString(3);
					    		}else {
					    			agmt_no = "";
					    			cont_no = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		else if(cont_ref.length()>2 && !cont_ref.contains("D") && !cont_ref.contains("L") && !cont_ref.contains("X")) {
				    			queryString = "SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ?";
		    					stmt = conn.prepareStatement(queryString);
		    					stmt.setString(1, cont_ref);
					    		rset = stmt.executeQuery();
					    		
					    		if(rset.next()) {
					    			agmt_no = rset.getString(1);
					    			cont_no = rset.getString(2);
					    			cont_type = rset.getString(3);
					    		}else {
					    			agmt_no = "";
					    			cont_no = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.contains("D")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 4) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.contains("D")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}
				    		}
				    		data = cont_type;
				    	}
				    		
				    	else {
				    		
				    		if (cell.getColumnIndex() == 5) {	// SEQ_NO
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    	
				    }
				     
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_MR_CONT_TAQ_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
							+ "AND AGMT_NO = ? AND CONT_NO=? AND CONTRACT_TYPE = ? AND SEQ_NO =? ";
					
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, cont_no);
					stmt.setString(5, cont_type);
					stmt.setString(6, seq_no);

					rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
						
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + ","  + cont_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + cont_no + ","), conn, "E");
					}
				    
				    rset.close();
				    stmt.close();
				}
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch(Exception e)
		{

			msg = "One of the Functions faced an Error. Data Not Inserted.";
			msg_type = "E";
			
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		finally {
			conn.commit();
			if (file != null) {
				file.close();
			}
		}
	}

public void FMS_MR_EXPO_EOD_MST() throws IOException, SQLException {
	
	function_nm="FMS_MR_EXPO_EOD_MST()";
	try {
		
		System.out.println("<<START>><<FMS_MR_EXPO_EOD_MST>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_MR_EXPO_EOD_MST>>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		String cont_type = "", ref = "",deal_num ="",no="",rev="",cont_no="",cont_rev="",cargo_no="",type="",base="",entity_type="",rpt_dt="",map="";
		String cont_ent_by = "",cont_ent_dt = "",cont_appr_by = "",cont_appr_dt = "",cont_status ="",price_type = "",cont_price="",rate_unit="";
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		
		columns = "COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,COUNTERPARTY_NM,CONTRACT_TYPE,MAPPING_ID,CONT_REF,CONT_SIGN_DT,CONT_START_DT,CONT_END_DT,CONT_ENT_BY,CONT_ENT_DT,"
				+ "CONT_APRV_BY,CONT_APRV_DT,CONT_STATUS,PRICE_TYPE,CONT_PRICE,RATE_UNIT,PHYS_CURVE,FIN_CURVE,TOT_DCQ,TOT_ALLOC_QTY,TOT_ORI_EXPO_PHY,TOT_ORI_EXPO_FIN,"
				+ "TOT_UNR_EXPO_PHY,TOT_UNR_EXPO_FIN,TOT_R_EXPO_PHY,TOT_R_EXPO_FIN,TOT_UNR_PHY_LEG,TOT_UNR_FIN_LEG,TOT_R_FIN_LEG,TOT_MTM_TOTAL,PHYS_FWD_PRICE_DT,FIN_FWD_PRICE_DT,"
				+ "ENT_BY,ENT_DT,DEAL_NUM";
		
		
//		logger.checkpoint(fname, "CURVE_NM,CONT_MONTH,", conn);
		
		queryString1 = "INSERT INTO FMS_MR_EXPO_EOD_MST(COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,COUNTERPARTY_NM,CONTRACT_TYPE,MAPPING_ID,CONT_REF,CONT_SIGN_DT,CONT_START_DT,"
				+ "CONT_END_DT,CONT_ENT_BY,CONT_ENT_DT,CONT_APRV_BY,CONT_APRV_DT,CONT_STATUS,PRICE_TYPE,CONT_PRICE,RATE_UNIT,PHYS_CURVE,FIN_CURVE,TOT_DCQ,TOT_ALLOC_QTY,"
				+ "TOT_ORI_EXPO_PHY,TOT_ORI_EXPO_FIN,TOT_UNR_EXPO_PHY,TOT_UNR_EXPO_FIN,TOT_R_EXPO_PHY,TOT_R_EXPO_FIN,TOT_UNR_PHY_LEG,TOT_UNR_FIN_LEG,TOT_R_FIN_LEG,TOT_MTM_TOTAL,"
				+ "PHYS_FWD_PRICE_DT,FIN_FWD_PRICE_DT,ENT_BY,ENT_DT,DEAL_NUM) "
				+ "VALUES(?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), "
				+ "?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?) ";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_MR_EXPO_EOD_MST_"+start_end_dt+".csv");
		if(file1.exists()) {
			
//			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_MR_EXPO_EOD_MST_"+start_end_dt+".csv"));
			String filename = migration_setup_dir + "EXPORT/FMS_MR_EXPO_EOD_MST_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
				String line = br.readLine();
//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for inserting SEIPL data
//			rowIterator = sheet.iterator();
//			rowIterator.next();
			

			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,REPORT_DT,CONTRACT_TYPE,MAPPING_ID,DEAL_NUM,TIMESTAMP,", conn);

//			while (rowIterator.hasNext()) {
			while((line=br.readLine()) != null) {
				total_count++;  
				
					data = "";
					cd = "0"; 
					cont_type = ""; ref = "";deal_num ="";no="";rev="";cont_no="";cont_rev="";cargo_no="";type="";base="";entity_type="";rpt_dt="";map="";
					cont_ent_by = "";cont_ent_dt = "";cont_appr_by = "";cont_appr_dt = "";cont_status ="";price_type = "";cont_price="";rate_unit="";
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
//					row = rowIterator.next();
//					cellIterator = row.cellIterator(); 
					for(int i = 0; i < line.split(",").length;i++) {
//					while (cellIterator.hasNext()) {
//						cell = cellIterator.next();
						data = null;
						
						if (i == 3) {
							cd = line.split(",")[i].toUpperCase();
//							cd = cd.substring(1, cd.length()-1);
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							rset = stmt.executeQuery();
							if (rset.next()) {
								cd = rset.getString(1);
							}
							else {
								cd = "";
							}
								
							rset.close();
							stmt.close();
								
							data = cd;
						}
						else if (i == 11) {	//CONT_ENT_BY
							if(cont_type!=null) {
								if(cont_type.equals("N")) {		//LNG
									queryString = "SELECT ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,CARGO_STATUS,NULL,RATE,RATE_UNIT,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND CARGO_REF = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, company_cd);
									stmt.setString(2, cd);
									stmt.setString(3, cont_type);
									stmt.setString(4, ref);
									rset = stmt.executeQuery();
									if(rset.next()) {
										cont_ent_by = rset.getString(1);
										cont_ent_dt = rset.getString(2);
										cont_appr_by = rset.getString(3);
										cont_appr_dt = rset.getString(4);
										cont_status = rset.getString(5);
										price_type = rset.getString(6);
										cont_price = rset.getString(7);
										rate_unit = rset.getString(8);
										cargo_no = rset.getString(9);
									}
									rset.close();
									stmt.close();
								}
								
								else if(cont_type.equals("I") || cont_type.equals("D") || cont_type.equals("T")) { 	//TRADER_CONT
									queryString = "SELECT ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,CONT_STATUS,NULL,RATE,RATE_UNIT FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND CONT_REF_NO = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, company_cd);
									stmt.setString(2, cd);
									stmt.setString(3, cont_type);
									stmt.setString(4, ref);
									rset = stmt.executeQuery();
									if(rset.next()) {
										cont_ent_by = rset.getString(1);
										cont_ent_dt = rset.getString(2);
										cont_appr_by = rset.getString(3);
										cont_appr_dt = rset.getString(4);
										cont_status = rset.getString(5);
										price_type = rset.getString(6);
										cont_price = rset.getString(7);
										rate_unit = rset.getString(8);
									}
									rset.close();
									stmt.close();
								}
								
								else if(cont_type.equals("S")) {	//SALES(SN)
									queryString = "SELECT ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,CONT_STATUS,NULL,RATE,RATE_UNIT	FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, company_cd);
									stmt.setString(2, cd);
									stmt.setString(3, ref.split("-")[1]);
									stmt.setString(4, ref.split("-")[2]);
									stmt.setString(5, ref.split("-")[3]);
									stmt.setString(6, ref.split("-")[4]);
									stmt.setString(7, cont_type);
									rset = stmt.executeQuery();
									if(rset.next()) {
										cont_ent_by = rset.getString(1);
										cont_ent_dt = rset.getString(2);
										cont_appr_by = rset.getString(3);
										cont_appr_dt = rset.getString(4);
										cont_status = rset.getString(5);
										price_type = rset.getString(6);
										cont_price = rset.getString(7);
										rate_unit = rset.getString(8);
									}
									rset.close();
									stmt.close();
								}
								
								else if (cont_type.equals("L") || cont_type.equals("X")) { 	 //SALES(LOA & IGX)
									queryString = "SELECT ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,CONT_STATUS,NULL,RATE,RATE_UNIT	FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND CONT_REF_NO = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, company_cd);
									stmt.setString(2, cd);
									stmt.setString(3, cont_type);
									stmt.setString(4, ref);
									rset = stmt.executeQuery();
									if(rset.next()) {
										cont_ent_by = rset.getString(1);
										cont_ent_dt = rset.getString(2);
										cont_appr_by = rset.getString(3);
										cont_appr_dt = rset.getString(4);
										cont_status = rset.getString(5);
										price_type = rset.getString(6);
										cont_price = rset.getString(7);
										rate_unit = rset.getString(8);
									}
									rset.close();
									stmt.close();
								}
								
								else if(cont_type.equals("F") || cont_type.equals("E")) {	  //DLNG(SN & LOA)
									queryString = "SELECT ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,CONT_STATUS,NULL,RATE,RATE_UNIT	FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND CONT_REF_NO = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, company_cd);
									stmt.setString(2, cd);
									stmt.setString(3, cont_type);
									stmt.setString(4, ref);
									rset = stmt.executeQuery();
									if(rset.next()) {
										cont_ent_by = rset.getString(1);
										cont_ent_dt = rset.getString(2);
										cont_appr_by = rset.getString(3);
										cont_appr_dt = rset.getString(4);
										cont_status = rset.getString(5);
										price_type = rset.getString(6);
										cont_price = rset.getString(7);
										rate_unit = rset.getString(8);
									}
									rset.close();
									stmt.close();
								}
								
								else {
									cont_ent_by = null;
									cont_ent_dt = null;
									cont_appr_by = null;
									cont_appr_dt = null;
									cont_status = null;
									price_type = null;
									cont_price = null;
									rate_unit = null;
								}
							
							data = cont_ent_by;
							}
						}
						
						else if(i == 12) {	//CONT_ENT_DT
							data = cont_ent_dt;
						}
						
						else if(i == 13) {	//CONT_APPR_BY
							data = cont_appr_by;
						}
						
						else if(i == 14) {	//CONT_APPR_DT
							data = cont_appr_dt;
						}
						
						else if(i == 15) {	//CONT_STATUS
							data = cont_status;
						}
						
						else if(i == 16) {	//PRICE_TYPE
							data = price_type;
						}
						
						else if(i == 17) {	//CONT_PRICE
							data = cont_price;
						}
						
						else if(i == 18) {	//RATE_UNIT
							data = rate_unit;
						}
						
						else if (i == 37) {	//DEAL_NUM
							if(cont_type!=null) {
							if(cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X") || cont_type.equals("Q") || cont_type.equals("O") || cont_type.equals("F") || cont_type.equals("E") || cont_type.equals("W") || cont_type.equals("B") || cont_type.equals("M"))
							{
								entity_type = "C";
							}
							else if(cont_type.equals("D") || cont_type.equals("T") || cont_type.equals("N") || cont_type.equals("I") || cont_type.equals("G") || cont_type.equals("P"))
							{
								entity_type = "T";
							}
							else if(cont_type.equals("C") || cont_type.equals("R"))
							{
								entity_type = "R";
							}	
							else if(cont_type.equals("H"))
							{
								entity_type = "H";
							}	
							else if(cont_type.equals("Y"))
							{
								entity_type = "S";
							}	
							else if(cont_type.equals("A"))
							{
								entity_type = "V";
							}
							else if(cont_type.equals("Z"))
							{
								entity_type = "Z";
							}
							
							
							if(cont_type.equals("S") || (cont_type.equals("N")  && (cargo_no.equals("") || cargo_no.equals("0")))) {
								deal_num = company_cd+entity_type+cd+""+cont_type+""+no+"-"+cont_no;
							}
							else if(cont_type.equals("N")) {
								deal_num = cont_type+""+no+"-"+cont_no+"-"+cargo_no;
							}
							else {
								deal_num=company_cd+entity_type+cd+""+cont_type+""+cont_no;
							}
							
							}
							data = deal_num;
						}
						else {
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							if(data != null) {
//						    	data = data.substring(1, data.length()-1);
//						    }
							if(i == 1) {	//REPORT_DT
								rpt_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if (i == 5) {	//CONTRACT_TYPE
								cont_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if (i == 6) {	//MAPPING_ID
								map = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if (i == 7) {	//CONT_REF
								ref = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								if(map.startsWith("B")) {
									if(cont_type.equals("N")) {		//LNG
										queryString = "SELECT AGMT_NO, CONT_NO FROM FMS_TRADER_CARGO_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND CARGO_REF = ? ";
										stmt = conn.prepareStatement(queryString);
										stmt.setString(1, company_cd);
										stmt.setString(2, cd);
										stmt.setString(3, cont_type);
										stmt.setString(4, ref);
										rset = stmt.executeQuery();
										if(rset.next()) {
											no = rset.getString(1);
											cont_no = rset.getString(2);
										}
										rset.close();
										stmt.close();
									}
									
									else if(cont_type.equals("I") || cont_type.equals("D") || cont_type.equals("T")) { 	//TRADER_CONT
										queryString = "SELECT AGMT_NO, CONT_NO FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND CONT_REF_NO = ? ";
										stmt = conn.prepareStatement(queryString);
										stmt.setString(1, company_cd);
										stmt.setString(2, cd);
										stmt.setString(3, cont_type);
										stmt.setString(4, ref);
										rset = stmt.executeQuery();
										if(rset.next()) {
											no = rset.getString(1);
											cont_no = rset.getString(2);
										}
										rset.close();
										stmt.close();
									}
								}
								else {
									no = map.split("-")[1];
									cont_no = map.split("-")[3];
								}
							}
							
						}
//						System.out.println(index+"::"+data);
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT REPORT_DT FROM FMS_MR_EXPO_EOD_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND REPORT_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND CONTRACT_TYPE = ? AND MAPPING_ID = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, rpt_dt);
					stmt.setString(4, cont_type);
					stmt.setString(5, map);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("") && cont_type!=null) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd + "," + cd + "," + rpt_dt + "," + cont_type + "," + map + "," + deal_num + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						
						skipped_count++;     
						logger.data(fname, (company_cd + "," + cd + "," + rpt_dt + "," + cont_type + "," + map + "," + deal_num + ","), conn, "E");
					}
					rset.close();
					stmt.close();
			   
			}
			br.close();
			}

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		//conn.commit();
		
		System.out.println("<<END>><<FMS_MR_EXPO_EOD_MST>>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_MR_EXPO_EOD_MST>>,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	}
	catch(Exception e)
	{

		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		logger.error(fname, e, function_nm, conn, fname_error);
	}
	finally {
		conn.commit();
		if (file != null) {
			file.close();
		}
	}
	
}

public void FMS_MR_EXPO_EOD_DTL() throws IOException, SQLException {
	
	function_nm="FMS_MR_EXPO_EOD_DTL()";
	try {
		
		System.out.println("<<START>><<FMS_MR_EXPO_EOD_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_MR_EXPO_EOD_DTL>>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		String rpt_dt="",map="",cont_type="",seq="",gas_dt="",buy_sell="";
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		
		columns = "COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,CONTRACT_TYPE,MAPPING_ID,GAS_DT,CONT_MTH,DCQ,ALLOC_QTY,SEQ_NO,PRICE_TYPE,FIN_CURVE_NM,CONT_PRICE,RATE_UNIT,SPOT_MTH,SPOT_START_DT,SPOT_END_DT,SETTLE_PRICE,RU_PHY_FLAG,RU_FIN_FLAG,FWD_PRICE_PHY,FWD_PRICE_FIN,SLOPE,CONST,EFF_RATE_USD,ORI_EXPO_PHY,ORI_EXPO_FIN,UNR_EXPO_PHY,UNR_EXPO_FIN,R_EXPO_PHY,R_EXPO_FIN,UNR_PHY_LEG,UNR_FIN_LEG,R_FIN_LEG,MTM_TOTAL,ENT_BY,ENT_DT,PHY_CURVE_NM";
		
		
//		logger.checkpoint(fname, "CURVE_NM,CONT_MONTH,", conn);
		
		queryString1 = "INSERT INTO FMS_MR_EXPO_EOD_DTL(COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,CONTRACT_TYPE,MAPPING_ID,GAS_DT,CONT_MTH,DCQ,ALLOC_QTY,SEQ_NO,PRICE_TYPE,FIN_CURVE_NM,CONT_PRICE,"
				+ "RATE_UNIT,SPOT_MTH,SPOT_START_DT,SPOT_END_DT,SETTLE_PRICE,RU_PHY_FLAG,RU_FIN_FLAG,FWD_PRICE_PHY,FWD_PRICE_FIN,SLOPE,CONST,EFF_RATE_USD,ORI_EXPO_PHY,ORI_EXPO_FIN,UNR_EXPO_PHY,"
				+ "UNR_EXPO_FIN,R_EXPO_PHY,R_EXPO_FIN,UNR_PHY_LEG,UNR_FIN_LEG,R_FIN_LEG,MTM_TOTAL,ENT_BY,ENT_DT,PHY_CURVE_NM,WA_RATE,QTY_UNIT) "
				+ "VALUES(?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,?, "
				+ "?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?),?,? ";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_MR_EXPO_EOD_DTL_"+start_end_dt+".csv");
		if(file1.exists()) {
			
//			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_MR_EXPO_EOD_MST_"+start_end_dt+".csv"));
			String filename = migration_setup_dir + "EXPORT/FMS_MR_EXPO_EOD_DTL_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
				String line = br.readLine();
//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
//			rowIterator = sheet.iterator();
//			rowIterator.next();
				
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,REPORT_DT,BUY_SELL,CONTRACT_TYPE,MAPPING_ID,GAS_DT,SEQ_NO,TIMESTAMP,", conn);
				
//			while (rowIterator.hasNext()) {
				while((line=br.readLine()) != null) {
					total_count++;  
					
					data = "";
					cd = "0"; 
					rpt_dt="";map="";cont_type="";seq="";gas_dt="";buy_sell="";
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
//					row = rowIterator.next();
//					cellIterator = row.cellIterator(); 
					for(int i = 0; i < line.split(",").length;i++) {
//					while (cellIterator.hasNext()) {
//						cell = cellIterator.next();
						data = null;
						
						if (i == 3) {
							cd = line.split(",")[i].toUpperCase();
//							cd = cd.substring(1, cd.length()-1);
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							rset = stmt.executeQuery();
							if (rset.next()) {
								cd = rset.getString(1);
							}
							else {
								cd = "";
							}
							
							rset.close();
							stmt.close();
							
							data = cd;
						}
						
						else {
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							if(data != null) {
//						    	data = data.substring(1, data.length()-1);
//						    }
							if(i == 1) {	//REPORT_DT
								rpt_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 2) {	//BUY_SELL
								buy_sell = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if (i == 4) {	//CONTRACT_TYPE
								cont_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if (i == 5) {	//MAPPING_ID
								map = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if (i == 6) {	//GAS_DT
								gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if (i == 10) {	//SEQ_NO
								seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							
						}
//						System.out.println(index+"::"+data);
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT REPORT_DT FROM FMS_MR_EXPO_EOD_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND REPORT_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND CONTRACT_TYPE = ? AND MAPPING_ID = ? AND BUY_SELL = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND SEQ_NO = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, rpt_dt);
					stmt.setString(4, cont_type);
					stmt.setString(5, map);
					stmt.setString(6, buy_sell);
					stmt.setString(7, gas_dt);
					stmt.setString(8, seq);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("") && cont_type!=null && !cont_type.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd + "," + cd + "," + rpt_dt + "," + buy_sell + "," + cont_type + "," + map + "," + gas_dt + "," + seq + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						
						skipped_count++;     
						logger.data(fname, (company_cd + "," + cd + "," + rpt_dt + "," + buy_sell + "," + cont_type + "," + map + "," + gas_dt + "," + seq + "," ), conn, "E");
					}
					rset.close();
					stmt.close();
					
				}
				br.close();
			}
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		//conn.commit();
		
		System.out.println("<<END>><<FMS_MR_EXPO_EOD_DTL>>");
		System.out.println();
		
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_MR_EXPO_EOD_DTL>>,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	}
	catch(Exception e)
	{
		
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		logger.error(fname, e, function_nm, conn, fname_error);
	}
	finally {
		conn.commit();
		if (file != null) {
			file.close();
		}
	}
	
}
	
public void FMS_SECURITY_DEAL_MAP() throws IOException, SQLException {

	function_nm="FMS_SECURITY_DEAL_MAP()";
	try {
		table_name = "FMS_SECURITY_DEAL_MAP";
		System.out.println("<<START>><<"+table_name+">>");
		logger.checkpoint(fname,"\n<<START>>,<<FMS_SECURITY_DEAL_MAP>>,,,," , conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		
		logger_count = 0;
		skipped_count = 0;   
		total_count = 0;
		int no_P = 0;
		String seq_no = "", agmt_no = "",map_seq_no="",seq_rev_no="",gx="",cont_ref_no="",agmt_rev="",cont_rev="",cont_type="",man_cnf_cd="",sec_ref_no="",link="";
		String identifier="",no="",rev="";
		
		queryString1="INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO,AGMT_NO,AGMT_REV,"
				+ "CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}
			
			logger.checkpoint(fname, "COMPANY_CD,CD,SEQ_NO,MAP_SEQ_NO,SEQ_REV_NO,GX,CONTRACT_TYPE,TIMESTAMP", conn); 
			
			while (rowIterator.hasNext()) {
				total_count++;
			
				abbr = "";link="";map_seq_no="";seq_no="";gx="";
				cd = "0";
				data = null;
				
				index = 1;
				stmt1 = conn.prepareStatement(queryString1);
			    
				row = rowIterator.next();
			    cellIterator = row.cellIterator();
			    while (cellIterator.hasNext()) {
					cell = cellIterator.next();
					data = null;
					
			    	if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 
			    		
			    		String identifier1=(cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
			    		
			    		if (identifier1!=null) {
			    			identifier1 = identifier1.substring(1, identifier1.length() - 1);
			    			identifier=identifier1.split("=")[1];// it can be PUR DLNG
			    			
			    			if(identifier.contains("PUR"))
			    			{
//			    				'VEDALTD:0=PUR'
			    				cont_ref_no=identifier1.split("=")[0];
					    		if (cont_ref_no!=null) {
									if(!cont_ref_no.contains(":")) {
										abbr=cont_ref_no.split("-")[0];
						    			cont_type=cont_ref_no.split("-")[3];
									}
									else
									{
										abbr=cont_ref_no.split(":")[0];
						    			link=cont_ref_no.split(":")[1];
									}
								}
					    		else {
					    			cont_ref_no=null;
					    			abbr=null;
					    			cont_type=null;
					    		}
			    				
			    			}
			    			else if(identifier.contains("DLNG"))
			    			{
			    				abbr=identifier1.split("=")[0];
			    			}
			    			else if(identifier.contains("SALES"))
			    			{
			    				abbr=identifier1.split("=")[0];
			    			}
			    			
						}
			    		data = company_cd;
			    	}else if (cell.getColumnIndex() == 1 ) {	// Counterparty_Cd
			    		
			    		if(identifier.contains("PUR"))
			    		{
			    			if(cont_ref_no!=null  && !cont_ref_no.contains(":") )
				    		{
				    			man_cnf_cd = cell.getStringCellValue();
				    			man_cnf_cd = man_cnf_cd.substring(1, man_cnf_cd.length()-1);	
					    		
					    		
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		}
					    		else 
					    		{
					    			cd=null;
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    		 
					    		if(cont_type!=null && cont_type.equals("N")) {
					    		queryString = "SELECT AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM "
					    				+ "FMS_TRADER_CN_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, man_cnf_cd);
					    		stmt.setString(2, cd);
					    		stmt.setString(3, company_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_type = rset.getString(3);
					    			no_P = rset.getInt(4);
					    			cont_rev =  rset.getString(5);
					    			
					    		}
					    		rset.close();
					    		stmt.close();
					    		}
					    		else  if(cont_type!=null && ( cont_type.equals("D") || cont_type.equals("T"))) {
							    		queryString = "SELECT AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV "
							    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ?  AND COMPANY_CD = ?";
							    		stmt = conn.prepareStatement(queryString);
							    		stmt.setString(1, cont_ref_no);
							    		stmt.setString(2, cd);
							    		stmt.setString(3, company_cd);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			
							    			agmt_no = rset.getString(1);
							    			agmt_rev = rset.getString(2);
							    			cont_type = rset.getString(3);
							    			no_P = rset.getInt(4);
							    			cont_rev =  rset.getString(5);
							    			
							    		}
							    		rset.close();
							    		stmt.close();
							    		}
					    		else  if(cont_type!=null && cont_type.equals("I")) {
						    		queryString = "SELECT AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV "
						    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cont_ref_no);
						    		stmt.setString(2, cd);
						    		stmt.setString(3, company_cd);
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_type = rset.getString(3);
						    			no_P = rset.getInt(4);
						    			cont_rev =  rset.getString(5);
						    			
						    		}
						    		rset.close();
						    		stmt.close();
						    		}
					    		
					    		queryString = "SELECT COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_CD = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			abbr = rset.getString(1);
					    		}
					    		else {
					    			abbr=null;
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		else {
				    			
					    		if(abbr!=null && abbr.equals("IGX"))
					    		{
					    			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COMPANY_EXCHG_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, abbr);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			cd = rset.getString(1);
						    		}
						    		else 
						    		{
						    			cd=null;
						    		}
						    		rset.close();
						    		stmt.close();
						    		gx="I";
					    		}
					    		else
					    		{
					    			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, abbr);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			cd = rset.getString(1);
						    		}
						    		else 
						    		{
						    			cd=null;
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		data = cd;
					    		
					    		
					    		agmt_no="0";
					    		agmt_rev="0";
					    		cont_rev="0";
					    		cont_type="0";
					    		no_P=0;
					    		
				    		}
			    		}
			    		else if (identifier.contains("DLNG"))
			    		{
			    			cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
			    		}
			    		else if(identifier.contains("SALES"))
		    			{
			    			cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data=cd;
		    			}
				    }	

			    	else if(cell.getColumnIndex() == 5) {	//AGMT_NO	
			    		
			    		if(identifier.contains("PUR"))
			    		{
			    			data = agmt_no;		
			    		}
			    		else if(identifier.contains("DLNG"))
			    		{
			    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND  COUNTERPARTY_CD = ? AND REMARK LIKE ?  ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, company_cd);
				    		stmt.setString(2, cd);
				    		stmt.setString(3, "%@"+cont_ref);
				    		
				    		
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			no = rset.getString(1);
				    			rev = rset.getString(2);
				    			cont_no = rset.getString(3);
				    			cont_rev = rset.getString(4);
				    			cont_type = rset.getString(5);
				    		} else {
				    			no = "";
				    			rev = "";
				    			cont_no = "";
				    			cont_rev = "";
				    			cont_type = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		data = no;
			    		}
			    		else if(identifier.contains("SALES"))
			    		{
			    			agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}else if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
//					    		System.out.println(agmt_no+"="+agmt_rev+"="+cont_no+"="+cont_rev+"="+cont_type);
				    		}
				    		
							data = agmt_no;
			    		}
			    	}
			    	else if(cell.getColumnIndex() == 6) {	//AGMT_REV
			    		if(identifier.contains("PUR"))
			    		{
			    			data = agmt_rev;	
			    		}
			    		else if(identifier.contains("DLNG"))
			    		{
			    			data = rev;
			    		}	
			    		else if(identifier.contains("SALES"))
			    		{
			    			if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
							data = agmt_rev;
			    			
			    		}
			    	}
			    	else if(cell.getColumnIndex() == 7) { //CONT_NO
			    		if(identifier.contains("PUR"))
			    		{
			    			data = no_P+"";	
			    		}
			    		else if(identifier.contains("DLNG"))
			    		{
			    			data = cont_no;
			    		}
			    		else if(identifier.contains("SALES"))
			    		{
			    			if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
			    		}		    						    			
			    	}
			    	else if(cell.getColumnIndex() == 8) {	//CONTRACT_REV 	
			    		if(identifier.contains("PUR"))
			    		{
			    			data = cont_rev;
			    		}
			    		else if(identifier.contains("DLNG"))
			    		{
			    			data = cont_rev;
			    		}
			    		else if(identifier.contains("SALES"))
			    		{
			    			if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
			    		}
		    			
			    	}
			    	else if(cell.getColumnIndex() == 9) {	//CONTRACT_TYPE			 
			    		
			    		if(identifier.contains("PUR"))
			    		{
			    			data = cont_type;
			    		}
			    		else if(identifier.contains("DLNG"))
			    		{
			    			data = cont_type;
			    		}
			    		else if(identifier.contains("SALES"))
			    		{
			    			if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
				    		}
				    		data = cont_type;
			    			
			    		}
			    		
		    						    						    			
			    	}
			    	else {
			    		
			    		if (cell.getColumnIndex() == 2) {	//SEQ_NO	    			
			    			seq_no = cell.getStringCellValue();
			    			seq_no = seq_no.substring(1, seq_no.length()-1);
			    		}
			    		
			    		if (cell.getColumnIndex() == 3) {	//MAP_SEQ_NO    			
			    			map_seq_no = cell.getStringCellValue();
			    			map_seq_no = map_seq_no.substring(1, map_seq_no.length()-1);
			    		}
			    		
			    		
			    		if (cell.getColumnIndex() == 16) {	// seq_rev_no
			    			seq_rev_no = cell.getStringCellValue();
			    			seq_rev_no = seq_rev_no.substring(1, seq_rev_no.length()-1);
			    		}
			    		if (cell.getColumnIndex() == 18) {	// gx //24_06_2025
			    			gx= cell.getStringCellValue();
			    			gx = gx.substring(1, gx.length()-1);
			    		}
			    		

				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}
//			    	System.out.println(index+"=="+data);
			    	stmt1.setString(index++, data);
			    }
//			  ("COMPANY_CD", "COUNTERPARTY_CD", "SEQ_NO", "MAP_SEQ_NO", "SEQ_REV_NO", "GX")
		    	queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? "
		    			+ "AND COUNTERPARTY_CD = ?   AND SEQ_NO = ? AND MAP_SEQ_NO = ? AND SEQ_REV_NO = ?  AND GX = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, seq_no);
		    	stmt.setString(4, map_seq_no);
		    	stmt.setString(5, seq_rev_no);
		    	stmt.setString(6, gx);

		    	rset = stmt.executeQuery();
		    	
			    if (row.getRowNum() != 0 && !rset.next() && !agmt_no.equals("") && cd!=null && !cd.equals("")) {
//			    	security_list=cont_ref_no+",";
			    	if(identifier.contains("PUR"))
			    	{
			    		logger.data(fname,((company_cd+"=PURCHASE")+", "+(cd+":"+abbr)+", "+seq_no+", "+map_seq_no+", "+seq_rev_no+" ,"+gx+","+cont_type+","), conn,"");
			    	}
			    	else if(identifier.contains("DLNG"))
			    	{
			    		logger.data(fname, ((company_cd+"=DLNG")+","+abbr + "," + seq_no + "," + map_seq_no + "," + seq_rev_no + "," + gx + "," + cont_type + ","), conn, "");
			    	}
			    	else if(identifier.contains("SALES"))
		    		{
			    		logger.data(fname, ((company_cd+"=SALES")+","+(cd+":"+abbr) + "," + seq_no + "," + map_seq_no + "," + seq_rev_no + "," + gx + ","  + cont_type + ","), conn, "");
		    		}
			    	
			    	stmt1.executeUpdate();
			    	stmt1.close();
			    	logger_count++;
			    }else {
			    	stmt1.close();
			    	skipped_count++;
			    	
			    	if(identifier.contains("PUR"))
			    	{
			    		logger.data(fname,((company_cd+"=PURCHASE")+", "+(cd+":"+abbr)+", "+seq_no+", "+map_seq_no+", "+seq_rev_no+", "+gx+","+cont_ref_no+","+man_cnf_cd+","+cont_type+","), conn, "E");
			    	}
			    	else if(identifier.contains("DLNG"))
			    	{
			    		logger.data(fname, ((company_cd+"=DLNG")+","+abbr + "," + seq_no + "," + map_seq_no + "," + seq_rev_no + "," + gx + "," + no + ","), conn, "E");
			    	}
			    	else if(identifier.contains("SALES"))
		    		{
			    		logger.data(fname, ((company_cd+"=SALES")+","+(cd+":"+abbr) + "," + seq_no + "," + map_seq_no + "," + seq_rev_no + "," + gx + ","  + cont_type + ","), conn, "E");
		    		}
			    	
			    }
			    
			    rset.close();
			    stmt.close();
			}
		
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
//		conn.commit();
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();
		
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
					
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
		
		logger.checkpoint1(fname1,logger_count+",", conn);
					
		logger.checkpoint(fname, "<<END>><<FMS_SECURITY_DEAL_MAP>>", conn);
	}
	catch(Exception e)
	{
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		logger.error(fname, e, function_nm, conn, fname_error);
	}
	finally {
		conn.commit();
		if (file != null) {
			file.close();
		}
	}
	
}




public void FMS_SECURITY_MST() throws IOException, SQLException {

	function_nm="FMS_SECURITY_MST()";
	try {
		table_name = "FMS_SECURITY_MST";
		System.out.println("<<START>><<"+table_name+">>");
		logger.checkpoint(fname,"\n<<START>>,<<FMS_SECURITY_MST>>,,,," , conn);
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

		data = "";
		logger_count = 0;
		skipped_count = 0;   
		total_count = 0;
		String sec_ref_list="", cont_no_list = "";

		queryString1 = "INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,"
				+ "GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,"
				+ "CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,"
				+ "ENT_DT,MODIFY_DT,MODIFY_BY,APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,"
				+ "SAP_REVERSAL_BY,SAP_REVERSAL_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ " ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?,"
				+ " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, "
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?,?,?,?)";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_MST_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECURITY_MST_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}
			
			logger.checkpoint(fname, "COMPANY_CD,CD,SEQ_NO,SEQ_REV_NO,GX,SEC_REF_NO,TIMESTAMP", conn);

	
			while (rowIterator.hasNext()) {
				
				total_count++;					
				String seq_rev_no="",gx="",cont_ref_no="",sec_ref_no="",seq_no="",bank_cd="",cont_type="";
				String sn_no="",sn_rev="",cont_rev="",cont_no="",identifier="";
				String g_cd="",map="";
				abbr = "";
				cd = "0";
				data = null;
				
				index = 1;
				
				stmt1 = conn.prepareStatement(queryString1);
			    
				row = rowIterator.next();
			    cellIterator = row.cellIterator();
			    while (cellIterator.hasNext()) {
					cell = cellIterator.next();
					data = null;
					
			    	if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 
			    		
			    		
			    		String identifier1=(cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
			    		
			    		if (identifier1!=null) {
			    			identifier1 = identifier1.substring(1, identifier1.length() - 1);
			    			identifier=identifier1.split("=")[1];// it can be PUR DLNG
			    			
			    			if(identifier.contains("PUR"))
			    			{
			    				cont_ref_no=identifier1.split("=")[0];
					    		if (cont_ref_no!=null) {
									if(!cont_ref_no.contains(":")) {
										abbr=cont_ref_no.split("-")[0];
						    			cont_type=cont_ref_no.split("-")[3];
									}
									else
									{
										abbr=cont_ref_no.split(":")[0];
										cont_type="NA";
										cont_ref_no="0";
									}
//									System.out.println(":"+abbr);
								}
					    		else 
					    		{
					    			cont_ref_no=null;
					    			abbr=null;
					    			cont_type=null;
					    			
					    		}
			    				
			    			}
			    			else if(identifier.contains("DLNG"))
			    			{
			    				abbr=identifier1.split("=")[0];
			    			}
			    			else if(identifier.contains("SALES"))
			    			{
			    				abbr=identifier1.split("=")[0];
			    			}
			    			
						}
			    		data = company_cd;
			    		
			    		
			    	}
			    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
			    		
			    		if(identifier.contains("PUR"))
		    			{
			    			   if(abbr!=null &&  abbr.equals("IGX"))
					    		{
					    			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COMPANY_EXCHG_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, abbr);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			cd = rset.getString(1);
						    		}
						    		else 
						    		{
						    			cd=null;
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		else
					    		{
					    			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, abbr);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			cd = rset.getString(1);
						    		}
						    		else 
						    		{
						    			cd=null;
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		
					    		
					    
				    		data = cd;
				    		
		    			}
			    		else if(identifier.contains("DLNG"))
			    		{
			    			map = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!map.equals("NULL")) {
								map = map.substring(1, map.length() - 1);
							}
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		} else {
				    			cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data=cd;
			    		}
			    		else if(identifier.contains("SALES"))
		    			{
			    			map = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if(map!=null) {
				    			map = map.substring(1,map.length()-1);
				    		}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		} else {
				    			cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
		    			}
			    		
			    		
			    		
			    	}	
			    	else if (cell.getColumnIndex() == 2) {	// seq_no
			    		
		    			data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
				    	seq_no=data;
			    		
		    		}
			    	
			    	else if(cell.getColumnIndex() == 3) {
			    		data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
				    	sec_ref_no=data;
				    	
			    	}
//			    	else if (cell.getColumnIndex() == 7) {//GUARANTOR_CD
//			    		g_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//			    		if(g_cd!=null)
//			    		{
//			    			g_cd = g_cd.substring(1, g_cd.length() - 1);
//				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
//				    		stmt = conn.prepareStatement(queryString);
//				    		stmt.setString(1, g_cd);
//				    		rset = stmt.executeQuery();
//				    		if (rset.next()) {
//				    			g_cd = rset.getString(1);
//				    		} else {
//				    			g_cd ="";
//				    		}
//				    		rset.close();
//				    		stmt.close();
//			    		}
//			    		data=g_cd;
//			    		
//			    	}
					else if(cell.getColumnIndex() == 12 || cell.getColumnIndex() == 14 || cell.getColumnIndex() == 16 ) {
			    		bank_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if(bank_cd!=null) {
			    				bank_cd = bank_cd.substring(1,bank_cd.length() - 1);
			    				queryString = "SELECT BANK_CD FROM FMS_BANK_MST WHERE BANK_NAME = ? ";
			    				stmt = conn.prepareStatement(queryString);
			    				stmt.setString(1, bank_cd);
			    				rset = stmt.executeQuery();
			    					if(rset.next()) {
			    						bank_cd=rset.getString(1);
			    					}
			    					else {
			    						bank_cd=null;
			    					}
			    					rset.close();
			    					stmt.close();
			    		}
			    		 data=bank_cd ;
			    	}
//					else if(cell.getColumnIndex() == 26)//remarks
//					{
//						data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    		if(data.length()>=250)
//					    	{
//				    			data=data.substring(0,250);
//					    	}
//				    	}
//				    	
//				    	
//				    }
					else if (cell.getColumnIndex() == 35) {	// seq_rev_no

		    			data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    		seq_rev_no=data;
		    		}
					else if (cell.getColumnIndex() == 36) {	// GX

		    			data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
				    	gx=data;
		    		}
			    	else 
			    	{	
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}
//		    		System.out.println(index+"=="+data);
			    	stmt1.setString(index++, data);
			    		
			    }
//			   ("COMPANY_CD", "COUNTERPARTY_CD", "SEQ_NO", "SEQ_REV_NO", "GX")
		    	queryString = "SELECT SEQ_NO FROM FMS_SECURITY_MST "
		    			+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = ?";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, seq_no);
		    	stmt.setString(4, seq_rev_no);
		    	stmt.setString(5, gx);
		    	
		    	
		    	
		    	rset = stmt.executeQuery();
		    	
			    if (row.getRowNum() != 0 && !rset.next()  && cd!=null && !cd.equals("")   ) {
			    	
			    	if(identifier.contains("PUR"))
			    	{
			    		logger.data(fname,((company_cd+"=PURCHASE")+","+(cd+":"+abbr)+" ,"+seq_no+" ,"+seq_rev_no+", "+gx+","+sec_ref_no+","), conn, "");
			    	}
			    	else if(identifier.contains("DLNG"))
			    	{
			    		logger.data(fname, ((company_cd+"=DLNG")+","+(cd+":"+abbr) +","+ seq_no + "," + seq_rev_no + "," + gx + ","+sec_ref_no+","), conn, "");
			    	}
			    	else if(identifier.contains("SALES"))
			    	{
			    		logger.data(fname, ((company_cd+"=SALES")+","+(cd+":"+abbr) + "," + seq_no + "," + seq_rev_no + "," + gx + ","+sec_ref_no+","), conn, "");
			    	}
			    	
			    	stmt1.executeUpdate();
			    	stmt1.close();
			    	logger_count++;
			    } else {
			    	stmt1.close();
			    	skipped_count++;
			    	
			    	if(identifier.contains("PUR"))
			    	{
			    		logger.data(fname,((company_cd+"=PURCHASE")+","+(cd+":"+abbr)+" ,"+seq_no+" ,"+seq_rev_no+", "+gx+","+sec_ref_no+","), conn, "E");
			    	}
			    	else if(identifier.contains("DLNG"))
			    	{
			    		logger.data(fname, ((company_cd+"=DLNG")+","+(cd+":"+abbr) +","+ seq_no + "," + seq_rev_no + ","+ gx + ","+sec_ref_no+","), conn, "E");
			    		
			    	}
			    	else if(identifier.contains("SALES"))
			    	{
			    		logger.data(fname, ((company_cd+"=SALES")+","+(cd+":"+abbr) + "," + seq_no + "," + seq_rev_no + "," + gx + ","+sec_ref_no+","), conn, "E");
			    	}
			    	
			    }
			    
			    rset.close();
			    stmt.close();
//			    System.out.println(":"+sec_ref_list);
			}

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
//		conn.commit();
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();
		
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
					
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
		
		logger.checkpoint1(fname1,logger_count+",", conn);
					
		logger.checkpoint(fname, "<<END>><<FMS_SECURITY_MST(P)>>", conn);
	}
	catch(Exception e)
	{
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		logger.error(fname, e, function_nm, conn, fname_error);
	}
	finally {
		conn.commit();
		if (file != null) {
			file.close();
		}
	}
	
}

public void LOG_FMS_SECURITY_MST() throws IOException, SQLException {

	function_nm="LOG_FMS_SECURITY_MST()";
	try {
		table_name = "LOG_FMS_SECURITY_MST";
		System.out.println("<<START>><<"+table_name+">>");
		logger.checkpoint(fname,"\n<<START>>,<<LOG_FMS_SECURITY_MST>>,,,," , conn);
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

		data = "";
		logger_count = 0;
		skipped_count = 0;   
		total_count = 0;
		String sec_ref_list="", cont_no_list = "";

		queryString1 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,"
				+ "GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,"
				+ "CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,"
				+ "ENT_DT,MODIFY_DT,MODIFY_BY,APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,"
				+ "SAP_REVERSAL_BY,SAP_REVERSAL_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD,LOG_SEQ_NO,LOG_ENT_DT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ " ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?,"
				+ " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, "
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST_"+start_end_dt+".csv");
		if(file1.exists()) {

			String fileName = migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST_"+start_end_dt+".csv";
//			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST_"+start_end_dt+".xlsx"));

//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
//			if (rowIterator.hasNext()) {	// For skipping the first row
//				rowIterator.next();
//			}
//			

	
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

				String line = br.readLine();
				logger.checkpoint(fname, "COMPANY_CD,CD,SEQ_NO,SEQ_REV_NO,GX,SEC_REF_NO,LOG_SEQ_NO,TIMESTAMP", conn);
			
//				while (rowIterator.hasNext()) {

			    while ((line = br.readLine()) != null) {
					
					total_count++;					
					String seq_rev_no="",gx="",cont_ref_no="",sec_ref_no="",seq_no="",bank_cd="",cont_type="",log_seq_no="";
					String sn_no="",sn_rev="",cont_rev="",cont_no="",identifier="";
					String g_cd="",map="";
					abbr = "";
					cd = "0";
					data = null;
					
					index = 1;
					
					stmt1 = conn.prepareStatement(queryString1);
				    
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
				    
//				    while (cellIterator.hasNext()) {
					for (int i = 0; i < line.split(",").length; i++)
				    {
//						cell = cellIterator.next();
						data = null;
						
				    	if (i == 0) {	// Counterparty_Abbr, Company Code 
				    		
				    		
				    		String identifier1=(line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		identifier1 = identifier1.substring(1, identifier1.length() - 1);
				    		if (identifier1!=null) {
				    			
				    			identifier=identifier1.split("=")[1];// it can be PUR DLNG
				    			
				    			if(identifier.contains("PUR"))
				    			{
				    				cont_ref_no=identifier1.split("=")[0];
						    		if (cont_ref_no!=null) {
										if(!cont_ref_no.contains(":")) {
											abbr=cont_ref_no.split("-")[0];
							    			cont_type=cont_ref_no.split("-")[3];
										}
										else
										{
											abbr=cont_ref_no.split(":")[0];
											cont_type="NA";
											cont_ref_no="0";
										}
	//									System.out.println(":"+abbr);
									}
						    		else 
						    		{
						    			cont_ref_no=null;
						    			abbr=null;
						    			cont_type=null;
						    			
						    		}
				    				
				    			}
				    			else if(identifier.contains("DLNG"))
				    			{
				    				abbr=identifier1.split("=")[0];
				    			}
				    			else if(identifier.contains("SALES"))
				    			{
				    				abbr=identifier1.split("=")[0];
				    			}
				    			
							}
				    		data = company_cd;
				    		
				    		
				    	}
				    	else if (i == 1) {	// Counterparty_Cd
				    		
				    		if(identifier.contains("PUR"))
			    			{
				    			   if(abbr!=null && abbr.equals("IGX"))
						    		{
						    			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COMPANY_EXCHG_MST WHERE COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(queryString);
							    		stmt.setString(1, abbr);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		else 
							    		{
							    			cd=null;
							    		}
							    		rset.close();
							    		stmt.close();
						    		}
						    		else
						    		{
						    			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(queryString);
							    		stmt.setString(1, abbr);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		else 
							    		{
							    			cd=null;
							    		}
							    		rset.close();
							    		stmt.close();
						    		}
						    		
						    		
						    
					    		data = cd;
					    		
			    			}
				    		else if(identifier.contains("DLNG"))
				    		{
				    			map = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//					    		if (!map.equals("NULL")) {
//									map = map.substring(1, map.length() - 1);
//								}
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd ="";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data=cd;
				    		}
				    		else if(identifier.contains("SALES"))
			    			{
				    			map = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//					    		if(map!=null) {
//					    			map = map.substring(1,map.length()-1);
//					    		}
					    		
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd ="";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
			    			}
				    		
				    		
				    		
				    	}	
				    	else if (i == 2) {	// seq_no
				    		
			    			data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
					    	seq_no=data;
				    		
			    		}
				    	
				    	else if(i == 3) {
				    		data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
					    	sec_ref_no=data;
					    	
				    	}
	//			    	else if (i == 7) {//GUARANTOR_CD
	//			    		g_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
	//			    		if(g_cd!=null)
	//			    		{
	//			    			g_cd = g_cd.substring(1, g_cd.length() - 1);
	//				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
	//				    		stmt = conn.prepareStatement(queryString);
	//				    		stmt.setString(1, g_cd);
	//				    		rset = stmt.executeQuery();
	//				    		if (rset.next()) {
	//				    			g_cd = rset.getString(1);
	//				    		} else {
	//				    			g_cd ="";
	//				    		}
	//				    		rset.close();
	//				    		stmt.close();
	//			    		}
	//			    		data=g_cd;
	//			    		
	//			    	}
						else if(i == 12 || i == 14 || i == 16 ) {
				    		bank_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    		if(bank_cd != null) {
				    			bank_cd = bank_cd.replaceAll("@@", ",");
//				    				bank_cd = bank_cd.substring(1,bank_cd.length() - 1);
				    				queryString = "SELECT BANK_CD FROM FMS_BANK_MST WHERE BANK_NAME = ? ";
				    				stmt = conn.prepareStatement(queryString);
				    				stmt.setString(1, bank_cd);
				    				rset = stmt.executeQuery();
				    					if(rset.next()) {
				    						bank_cd=rset.getString(1);
				    					}
				    					else {
				    						bank_cd=null;
				    					}
				    					rset.close();
				    					stmt.close();
				    		}
				    		 data=bank_cd ;
				    	}
//						else if(cell.getColumnIndex() == 26)//remarks
//						{
//							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    		if(data.length()>=250)
//						    	{
//					    			data=data.substring(0,250);
//						    	}
//					    	}
//					    	
//					    	
//					    }
						else if (i == 35) {	// seq_rev_no
	
			    			data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
				    		seq_rev_no=data;
			    		}
						else if (i == 36) {	// GX
	
			    			data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
					    	gx=data;
			    		}
				    	else 
				    	{	
				    		if (i == 47) {	// LOG_SEQ_NO
				    			log_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//						    	if(log_seq_no != null) {
//						    		log_seq_no = log_seq_no.substring(1, log_seq_no.length()-1);
//						    	}
				    		} 
					    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
				    	}
//			    		System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    		
				    }
	//			   ("COMPANY_CD", "COUNTERPARTY_CD", "SEQ_NO", "SEQ_REV_NO", "GX")
			    	queryString = "SELECT SEQ_NO FROM LOG_FMS_SECURITY_MST "
			    			+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = ? AND LOG_SEQ_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, seq_no);
			    	stmt.setString(4, seq_rev_no);
			    	stmt.setString(5, gx);
			    	stmt.setString(6, log_seq_no);
			    	
			    	
			    	
			    	rset = stmt.executeQuery();
			    	
				    if (!rset.next()  && cd != null &&  !cd.equals("")   ) {
				    	
				    	if(identifier.contains("PUR"))
				    	{
				    		logger.data(fname,((company_cd+"=PURCHASE")+","+(cd+":"+abbr)+" ,"+seq_no+" ,"+seq_rev_no+", "+gx+","+sec_ref_no+","+log_seq_no+","), conn, "");
				    	}
				    	else if(identifier.contains("DLNG"))
				    	{
				    		logger.data(fname, ((company_cd+"=DLNG")+","+(cd+":"+abbr) +","+ seq_no + "," + seq_rev_no + "," + gx + ","+sec_ref_no+","+log_seq_no+","), conn, "");
				    	}
				    	else if(identifier.contains("SALES"))
				    	{
				    		logger.data(fname, ((company_cd+"=SALES")+","+(cd+":"+abbr) + "," + seq_no + "," + seq_rev_no + "," + gx + ","+sec_ref_no+","+log_seq_no+","), conn, "");
				    	}
				    	
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    } else {
				    	stmt1.close();
				    	skipped_count++;
				    	
				    	if(identifier.contains("PUR"))
				    	{
				    		logger.data(fname,((company_cd+"=PURCHASE")+","+(cd+":"+abbr)+" ,"+seq_no+" ,"+seq_rev_no+", "+gx+","+sec_ref_no+","+log_seq_no+","), conn, "E");
				    	}
				    	else if(identifier.contains("DLNG"))
				    	{
				    		logger.data(fname, ((company_cd+"=DLNG")+","+(cd+":"+abbr) +","+ seq_no + "," + seq_rev_no + ","+ gx + ","+sec_ref_no+","+log_seq_no+","), conn, "E");
				    		
				    	}
				    	else if(identifier.contains("SALES"))
				    	{
				    		logger.data(fname, ((company_cd+"=SALES")+","+(cd+":"+abbr) + "," + seq_no + "," + seq_rev_no + "," + gx + ","+sec_ref_no+","+log_seq_no+","), conn, "E");
				    	}
				    	
				    }
				    
				    rset.close();
				    stmt.close();
	//			    System.out.println(":"+sec_ref_list);
				}
			}

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
//		conn.commit();
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();
		
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
					
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
		
		logger.checkpoint1(fname1,logger_count+",", conn);
					
		logger.checkpoint(fname, "<<END>><<LOG_FMS_SECURITY_MST(P)>>", conn);
	}
	catch(Exception e)
	{
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		logger.error(fname, e, function_nm, conn, fname_error);
	}
	finally {
		conn.commit();
		if (file != null) {
			file.close();
		}
	}
	
}

public void FMS_SECURITY_FILE_DTL() throws IOException, SQLException {
	function_nm="FMS_SECURITY_FILE_DTL()";
	try {
		
		table_name = "FMS_SECURITY_FILE_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
		
		queryString1 = "INSERT INTO FMS_SECURITY_FILE_DTL(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEQ_REV_NO,GX,FILE_TYPE,SEC_INT_REF,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_FILE_DTL_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_SECURITY_FILE_DTL_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
				// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
//			if (rowIterator.hasNext()) {	// For skipping the first row
//				rowIterator.next();
//			}
				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,SEC_INT_REF,SEQ_NO,SEQ_REV_NO,GX,FILE_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String new_inv="",agmt_rev="",cont_rev="",seq_rev="", sec_int_ref = "";
					abbr = "";
					bu_seq="";inv_seq="";fin_yr="";pdf_type="";file_nm="";sign_by="";sign_by_cd="";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++)
					{
//					cell = cellIterator.next();
						data = null;
						if(i == 0) {
							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							
							String[] parts = abbr.split("@");
							abbr=parts[0];
							cont_type=parts[1];
							
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		} else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
							data = company_cd;
						}
						else if(i == 1) {
							fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							data = cd;
						}
						else if(i == 2) {
//							inv_seq = line.split(",")[i];
//							
//							new_inv = inv_seq.split("@")[1];
//							inv_seq = inv_seq.split("@")[0];
							cont_ref = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = '2' ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cont_ref);
							stmt.setString(2, cd);
							stmt.setString(3, cont_type);
							
							rset = stmt.executeQuery();
							if (rset.next()) {
								agmt_no = rset.getString(1);
								agmt_rev = rset.getString(2);
								cont_no = rset.getString(3);
								cont_rev = rset.getString(4);
							} else {
								agmt_no = "0";
								agmt_rev = "0";
								cont_no = "0";
								cont_rev = "0";
							}
							rset.close();
							stmt.close();
							
							queryString = "SELECT A.SEQ_NO,A.SEQ_REV_NO FROM FMS_SECURITY_DEAL_MAP A WHERE A.COUNTERPARTY_CD = ? AND A.AGMT_NO = ? AND A.AGMT_REV = ? AND A.CONT_NO = ? AND A.CONT_REV = ? AND A.CONTRACT_TYPE = ? AND A.COMPANY_CD = '2' AND A.SEQ_NO NOT IN (SELECT B.SEQ_NO FROM FMS_SECURITY_FILE_DTL B, FMS_SECURITY_DEAL_MAP C WHERE A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND A.SEQ_NO = B.SEQ_NO AND C.COMPANY_CD = B.COMPANY_CD AND C.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND C.SEQ_NO = B.SEQ_NO AND A.AGMT_NO = C.AGMT_NO AND A.CONT_NO = C.CONT_NO AND A.AGMT_REV = C.AGMT_REV AND A.CONT_REV = C.CONT_REV AND A.CONTRACT_TYPE = C.CONTRACT_TYPE)";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							stmt.setString(2, agmt_no);
							stmt.setString(3, agmt_rev);
							stmt.setString(4, cont_no);
							stmt.setString(5, cont_rev);
							stmt.setString(6, cont_type);
							
							
							
							rset = stmt.executeQuery();
							if (rset.next()) {
								seq_no = rset.getString(1);
								seq_rev = rset.getString(2);
								
//								System.out.println(cd+":"+agmt_no+":"+agmt_rev+":"+cont_no+":"+cont_rev+":"+cont_type+"==="+seq_no);
								
							} else {
								seq_no ="0";
								seq_rev ="0";
							}
							rset.close();
							stmt.close();
							
							data = seq_no;
						}
						
						else if(i == 3) {
							sec_int_ref = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							data = seq_rev;
						}
						else if(i == 13) {	//SIGNED_BY
							sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							data = sign_by;
						}
						else if (i == 15) {	// SIGNED_ENT_BY
							if(sign_by!=null) {
								if(sign_by.contains("@")) {
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMAIL_ID) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sign_by.toUpperCase());
									rset = stmt.executeQuery();
									if (rset.next()) {
										sign_by_cd = rset.getString(1);
									} else {
										sign_by_cd ="0";
									}
									rset.close();
									stmt.close();
								}
								else {
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) LIKE ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sign_by.toUpperCase()+"%");
									rset = stmt.executeQuery();
									if (rset.next()) {
										sign_by_cd = rset.getString(1);
									} else {
										sign_by_cd ="0";
									}
									rset.close();
									stmt.close();
								}
							}
							else {
								sign_by_cd = "0";
							}
							data = sign_by_cd;
						}
						
						
						else {
							
							 if (i == 4) {	// PDF_TYPE
								gx = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
						    else if (i == 5) {	// PDF_TYPE
								pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							 
						    else if(i == 7) {	//FILE_NAME
								file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							} 
							 
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_SECURITY_FILE_DTL WHERE "
							+ "COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = ? AND FILE_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, seq_no);
					stmt.setString(4, seq_rev);
					stmt.setString(5, gx);
					stmt.setString(6, pdf_type);
					rset = stmt.executeQuery();
					
					if (!rset.next() && !seq_no.equals("0")){
						
						logger.data(fname, (company_cd + "," + cd + "," + sec_int_ref + "," + seq_no + ","  + seq_rev  + "," + gx + "," + pdf_type + "," + file_nm  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + cd + "," + sec_int_ref + "," + seq_no + "," + seq_rev  + "," + gx + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
					}
					
					rset.close();
					stmt.close();
				}
				br.close();
			}
			
//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";				
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		//conn.commit();
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();
		
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	}
	catch(Exception e)
	{
		
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		logger.error(fname, e, function_nm, conn, fname_error);
	}
	finally {
		conn.commit();
		if (file != null) {
			file.close();
		}
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
	
	// Setter-Getter methods
	public void setChecked_Values(String checked_val) {
		checked_values = checked_val;
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
	
	public void setSysDateTime(String dt) {
		sysDateTime = dt;
	}
	
	public void setStart_End_Dt(String dt) {
		start_end_dt = dt;
	}
	
	public void setDelta_FromDt(String from_dt) {
		delta_FromDt = from_dt + " 00:00:00";
	}
	
	public void setDelta_ToDt(String to_dt) {
		delta_ToDt = to_dt + " 23:59:59";
	}
}

