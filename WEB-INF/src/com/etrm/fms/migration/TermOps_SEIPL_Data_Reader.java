package com.etrm.fms.migration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
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

		

public class TermOps_SEIPL_Data_Reader {

	

	String db_src_file_name="TermOps_SEIPL_Data_Reader.java";

	String migration_setup_dir = "";
	
	String queryString="", queryString1="";
	Connection conn;
	ResultSet rset,rset1;
	PreparedStatement stmt,stmt1;
	
	String function_nm = "", columns = "", data = "";
	
	String sysDateTime = "";
	String start_end_dt = null;
	
	String fname = "";
	String fname_error = "";
	
	String fname1 = "";

	int index = 0;
	int logger_count = 0;
	int skipped_count=0;  
	int total_count=0;  
	
	final String company_cd = "2";
	String cd = "", eff_dt = "";

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
			
			fname = "DataLogs/Reader/TermOps_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/TermOps_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
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
	    		conn = ds.getConnection();
				if(conn != null && !checked_values.equals(""))  
				{
		    		preferences.put("Flag", "0");
					conn.setAutoCommit(false);
					
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
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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


	public void FMS_TANK_MST() throws IOException, SQLException {

		function_nm="FMS_TANK_MST()";
		try {
			
			System.out.println("<<START>><<FMS_TANK_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TANK_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_TANK_MST(COMPANY_CD,TANK_CD,TANK_NAME,EFF_DT,STATUS,TANK_T1_VOLUME,TANK_T1_HEIGHT,TANK_T2_VOLUME,TANK_T2_HEIGHT,TANK_D1_VOLUME,TANK_D1_HEIGHT,TANK_D2_VOLUME,TANK_D2_HEIGHT,TANK_DIAMETER,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TANK_PI_TAG) VALUES(?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TANK_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TANK_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,TANK_CD,EFF_DT,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = ""; eff_dt = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							if (cell.getColumnIndex() == 1) {
								cd = cell.getStringCellValue().toUpperCase();
								cd = cd.substring(1, cd.length()-1);
							}
							if (cell.getColumnIndex() == 3) {
								eff_dt = cell.getStringCellValue().toUpperCase();
								eff_dt = eff_dt.substring(1, eff_dt.length()-1);
							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT TANK_CD FROM FMS_TANK_MST WHERE COMPANY_CD = ? AND TANK_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, eff_dt);
						
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_TANK_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TANK_MST>>,,", conn);
			
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


	public void FMS_TANK_CONSUMPTION_MST() throws IOException, SQLException {

		function_nm="FMS_TANK_CONSUMPTION_MST()";
		try {

			System.out.println("<<START>><<FMS_TANK_CONSUMPTION_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TANK_CONSUMPTION_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_TANK_CONSUMPTION_MST(COMPANY_CD,EFF_DT,PERCENTAGE,REMARK,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,FLAG) VALUES(?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TANK_CONSUMPTION_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TANK_CONSUMPTION_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,EFF_DT,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = ""; eff_dt = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							
							if (cell.getColumnIndex() == 1) {
								eff_dt = cell.getStringCellValue().toUpperCase();
								eff_dt = eff_dt.substring(1, eff_dt.length()-1);
							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COMPANY_CD FROM FMS_TANK_CONSUMPTION_MST WHERE COMPANY_CD = ?  AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, eff_dt);
						
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (company_cd + "," + eff_dt + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_TANK_CONSUMPTION_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TANK_CONSUMPTION_MST>>,", conn);
			
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


	public void FMS_TANK_INVENTORY_DTL() throws IOException, SQLException {

		function_nm="FMS_TANK_INVENTORY_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_TANK_INVENTORY_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TANK_INVENTORY_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_TANK_INVENTORY_DTL(COMPANY_CD,INV_LEVEL_DT,TANK_CD,TANK_VOLUME,TANK_HEIGHT,TANK_MMSCM,TANK_CONV_FACTOR_1,TANK_CONV_FACTOR_2,TANK_MMBTU,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TANK_INVENTORY_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TANK_INVENTORY_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,TANK_CD,INV_LEVEL_DT,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = ""; eff_dt = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							if (cell.getColumnIndex() == 2) {
								cd = cell.getStringCellValue().toUpperCase();
								cd = cd.substring(1, cd.length()-1);
							}
							if (cell.getColumnIndex() == 1) {
								eff_dt = cell.getStringCellValue().toUpperCase();
								eff_dt = eff_dt.substring(1, eff_dt.length()-1);
							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT TANK_CD FROM FMS_TANK_INVENTORY_DTL WHERE COMPANY_CD = ? AND TANK_CD = ? AND INV_LEVEL_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, eff_dt);
						
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_TANK_INVENTORY_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TANK_INVENTORY_DTL>>,,", conn);
			
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
}

