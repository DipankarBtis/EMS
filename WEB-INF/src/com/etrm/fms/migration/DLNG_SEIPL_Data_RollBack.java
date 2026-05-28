package com.etrm.fms.migration;

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

public class DLNG_SEIPL_Data_RollBack {


	

	String db_src_file_name="DLNG_SEIPL_Data_RollBack.java";

	String migration_setup_dir = "";
	String sysDateTime = "";
	String checked_values = "", msg = "", msg_type = "";
	
	String function_nm = "";
	String start_end_dt = null;
	
	PreparedStatement stmt,st_del,stmt1;
	Connection conn;
	ResultSet rset,rset1;
	
	//bellow fname1  is for csv file function start & function end only 
	String fname1 = "";
	String fname ="";
	String fname_error = "";
	
	
	File file1 = null;
	FileInputStream file = null;
	XSSFWorkbook workbook = null;
	XSSFSheet sheet = null;
	Iterator<Row> rowIterator = null;
	Iterator<Cell> cellIterator = null;
	Row row;
	Cell cell;
	
	String data = "", column = "",value = "" ;
	String cd ="",abbr="",company_cd="2",agmt_no="",cp_abbr="";
	String query_select = "", query_delete = "",query_fetch_columnname = "",query_data_left = "",query_String="",query_String1="";
	
	int logger_count = 0;
	String[] data1 = null;
	
	DataMigration_Logger logger = new DataMigration_Logger();
	
	
	public void init() {
		function_nm = "init()";
		try {

			fname= "DataLogs/RollBack/DLNG_SEIPL_Data_RollBack(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/RollBack/DLNG_SEIPL_Data_RollBack_Error(log)"+sysDateTime+".csv";
			
			fname = migration_setup_dir + fname;
			fname_error = migration_setup_dir + fname_error;

			Preferences preferences =  Preferences.userRoot().node("/processFlag");

			fname1 = "DataLogs/Script_Status(log).csv";	
			fname1 = migration_setup_dir + fname1;

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
				if (conn != null && !checked_values.equals(""))
				{
		    		preferences.put("Flag", "0");
					conn.setAutoCommit(false);
					
					if (checked_values.contains("FMS_TRUCK_TRANSPORTER_MST")) {
						FMS_TRUCK_TRANSPORTER_MST();
					}
					if (checked_values.contains("FMS_TRUCK_TRANS_CONTACT_MST")) {
						FMS_TRUCK_TRANS_CONTACT_MST();
					}
					if (checked_values.contains("FMS_TRUCK_MST")) {
						FMS_TRUCK_MST();
					}
					if (checked_values.contains("FMS_TRUCK_DRIVER_MST")) {
						FMS_TRUCK_DRIVER_MST();
					}
					if (checked_values.contains("FMS_FILLING_STATION_MST")) {
						FMS_FILLING_STATION_MST();
					}
					if (checked_values.contains("FMS_CHECKPOST_MST")) {
						FMS_CHECKPOST_MST();
					}
					if (checked_values.contains("FMS_TRUCK_TRANSPORTER_LINK")) {
						FMS_TRUCK_TRANSPORTER_LINK();
					}
					if (checked_values.contains("FMS_TRUCK_DRIVER_TRANS_LINK")) {
						FMS_TRUCK_DRIVER_TRANS_LINK();
					}
					if (checked_values.contains("FMS_TRUCK_DRIVER_LINK")) {
						FMS_TRUCK_DRIVER_LINK();
					}
					if (checked_values.contains("FMS_LINK_CHECKPOST_PLANT")) {
						FMS_LINK_CHECKPOST_PLANT();
					}
//					if (checked_values.contains("FMS_AGMT_MST(DLNG)")) {
//						FMS_AGMT_MST();
//					}
		    		preferences.put("Flag", "1");

				}
				else {
					msg = "No Checkbox was selected. RollBack Terminated.";
					msg_type = "E";
				}
	    	}
	    	
		}
		catch (Exception e) {
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
			
		}
		finally{
			
			try{
				if (stmt != null) {try {stmt.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if (conn != null) {try {conn.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if (file != null) {try {file.close();} catch (Exception e ) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			
			catch (Exception e){
			    	new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					msg = "One of the Functions faced an Error. RollBack Terminated.";
					msg_type = "E";
			    }
		     }
	}
	public void FMS_TRUCK_TRANSPORTER_MST() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_TRANSPORTER_MST()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRUCK_TRANSPORTER_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRUCK_TRANSPORTER_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_TRUCK_TRANSPORTER_MST WHERE ENT_PROFILE = ? AND TRUCK_TRANS_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') ";

			String trans_cd,eff_dt;
			
			column = "TRUCK_TRANS_CD,EFF_DT,TRUCK_TRANS_NAME,TRUCK_TRANS_ABBR,STATE,CITY,PIN,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			data1 = new String[column.split(",").length]; 
 
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANSPORTER_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANSPORTER_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "ENT_PROFILE, TRUCK_TRANS_CD, EFF_DT, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT ENT_PROFILE, TRUCK_TRANS_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_TRUCK_TRANSPORTER_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";
						int i = 0;
						trans_cd="";eff_dt="";
						
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);	
							
							
							 if(cell.getColumnIndex() == 0) {
								 trans_cd = data;
							}
							 else if (cell.getColumnIndex() == 1) {
								 eff_dt = data;
							 }
							 else if (cell.getColumnIndex() == 4) {
								 data = null;
							 }
							if (data!= null) 
							{  
								data1[i] = data;
								if (data.equals("null")) {
							        	 query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							     } else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
							        	 query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							     } else {
							        	 query_select = query_select + column.split(",")[i] + " =? AND ";
							      }
                                  i++;
							} 
							
						}//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!company_cd.equals("")) 
			            {	
			            	 stmt = conn.prepareStatement(query_select);
						     int k = 1;
						     for (int w = 0; w < (column.split(",").length); w++) 
						     {
							     if (data1[w] != null && !data1[w].equals("null")) 
							     {
								 stmt.setString(k, data1[w]);
								 k++;
							     }
						     }
						     
						     rset = stmt.executeQuery();
						          
						     if (rset.next())       
						     {
						    	trans_cd=rset.getString(2);
						    	eff_dt=rset.getString(3);
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, company_cd);
							    st_del.setString(2, trans_cd);
							    st_del.setString(3, eff_dt);
							 
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+trans_cd+","+eff_dt+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT ENT_PROFILE, TRUCK_TRANS_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_TRUCK_TRANSPORTER_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        trans_cd=rset.getString(2);
			        eff_dt=rset.getString(3);
			        
			        
					logger.data(fname, (company_cd+","+trans_cd+","+eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();


				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_TRUCK_TRANSPORTER_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRUCK_TRANSPORTER_MST()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void FMS_TRUCK_TRANS_CONTACT_MST() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_TRANS_CONTACT_MST()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRUCK_TRANS_CONTACT_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRUCK_TRANS_CONTACT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_TRUCK_TRANS_CONTACT_MST WHERE COMPANY_CD = ? AND TRUCK_TRANS_CD = ? AND SEQ_NO = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND ADDR_FLAG = ? ";
			
			String trans_cd,eff_dt,addr,seq;
			
			column = "COMPANY_CD,TRUCK_TRANS_CD,SEQ_NO,EFF_DT,CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,NOM_FLAG,JT_FLAG,"
					+ "INV_FLAG,RM_FLAG,FM_FLAG,PM_FLAG,OTHER_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ADDR_IS_ACTIVE";
			data1 = new String[column.split(",").length]; 
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANS_CONTACT_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANS_CONTACT_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "COMPANY_CD,TRUCK_TRANS_CD,SEQ_NO,EFF_DT,ADDR_FLAG,TIMESTAMP,", conn);
				while (rowIterator.hasNext())
				{
					query_select = "SELECT COMPANY_CD, TRUCK_TRANS_CD, SEQ_NO, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'), ADDR_FLAG FROM FMS_TRUCK_TRANS_CONTACT_MST WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					data = "";
					int i = 0;
					trans_cd="";eff_dt="";addr="";seq="";
					
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = cell.getStringCellValue();
						data = data.substring(1, data.length() - 1);	
						
						
						if(cell.getColumnIndex() == 1) {
							trans_cd = data;
						}
						else if (cell.getColumnIndex() == 2) {
							seq = data;
						}
						else if (cell.getColumnIndex() == 3) {
							eff_dt = data;
						}
						else if (cell.getColumnIndex() == 11) {
							addr = data;
						}
						
						if (data!= null) 
						{  
							data1[i] = data;
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
								query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						} 
						
					}//while(column) completed
					
					query_select = query_select.substring(0, query_select.length() - 4);
					if(!company_cd.equals("")) 
					{	
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (data1[w] != null && !data1[w].equals("null")) 
							{
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						
						rset = stmt.executeQuery();
						
						if (rset.next())       
						{
							trans_cd=rset.getString(2);
							seq=rset.getString(3);
							eff_dt=rset.getString(4);
							addr=rset.getString(5);
							
							st_del = conn.prepareStatement(query_delete);
							st_del.setString(1, company_cd);
							st_del.setString(2, trans_cd);
							st_del.setString(3, seq);
							st_del.setString(4, eff_dt);
							st_del.setString(5, addr);
							
							st_del.executeUpdate();
							
							logger.data(fname, (company_cd+","+trans_cd+","+seq+","+eff_dt+","+addr+","), conn, "");
							logger_count++;   
							
							st_del.close();
						}
						
						rset.close();
						stmt.close();
					}
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, TRUCK_TRANS_CD, SEQ_NO, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'), ADDR_FLAG FROM FMS_TRUCK_TRANS_CONTACT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					trans_cd=rset.getString(2);
					seq=rset.getString(3);
					eff_dt=rset.getString(4);
					addr=rset.getString(5);
					
					
					logger.data(fname, (company_cd+","+trans_cd+","+seq+","+eff_dt+","+addr+","), conn, "N");
				}
				rset.close();
				stmt.close();
				
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_TRUCK_TRANS_CONTACT_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRUCK_TRANS_CONTACT_MST()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void FMS_TRUCK_MST() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_MST()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRUCK_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRUCK_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_TRUCK_MST WHERE ENT_PROFILE = ? AND TRUCK_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') ";
			
			String truck_cd,eff_dt;
			
			column = "EFF_DT,TRUCK_REG_NUM,TRUCK_TYPE,TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			data1 = new String[column.split(",").length]; 
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "ENT_PROFILE,EFF_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext())
				{
					query_select = "SELECT ENT_PROFILE, TRUCK_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_TRUCK_MST WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					data = "";
					int i = 0;
					truck_cd="";eff_dt="";
					
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = cell.getStringCellValue();
						data = data.substring(1, data.length() - 1);	
						
						
						if(cell.getColumnIndex() == 0) {
							 data = null;
						}
						else if (cell.getColumnIndex() == 1) {
							eff_dt = data;
						}
						
						if (data!= null) 
						{  
							data1[i] = data;
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
								query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						} 
						
					}//while(column) completed
					
					query_select = query_select.substring(0, query_select.length() - 4);
					if(!company_cd.equals("")) 
					{	
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (data1[w] != null && !data1[w].equals("null")) 
							{
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						
						rset = stmt.executeQuery();
						
						if (rset.next())       
						{
							
							truck_cd = rset.getString(2);
							eff_dt=rset.getString(3);
							
							st_del = conn.prepareStatement(query_delete);
							st_del.setString(1, company_cd);
							st_del.setString(2, truck_cd);
							st_del.setString(3, eff_dt);
							
							st_del.executeUpdate();
							
							logger.data(fname, (company_cd+","+truck_cd+","+eff_dt+","), conn, "");
							logger_count++;   
							
							st_del.close();
						}
						
						rset.close();
						stmt.close();
					}
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT ENT_PROFILE, TRUCK_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_TRUCK_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					truck_cd=rset.getString(2);
					eff_dt=rset.getString(3);
					
					
					logger.data(fname, (company_cd+","+truck_cd+","+eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();
				
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_TRUCK_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRUCK_MST()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void FMS_TRUCK_DRIVER_MST() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_DRIVER_MST()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRUCK_DRIVER_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRUCK_DRIVER_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_TRUCK_DRIVER_MST WHERE ENT_PROFILE = ? AND DRIVER_CD = ? AND LICENCE_NO = ? ";
			
			String driver_cd,no;
			
			column = "DRIVER_NAME,DRIVER_ADDR,DRIVER_DOB,DRIVER_STATUS,DRIVER_MOBILE,LICENCE_NO,LICENCE_TYPE,LICENCE_FROM_DT,LICENCE_TO_DT,"
					+ "LICENCE_ISSUE_STATE,LICENCE_FILE_NAME,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE,EFF_DT";
			data1 = new String[column.split(",").length]; 
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "ENT_PROFILE,EFF_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext())
				{
					query_select = "SELECT ENT_PROFILE, DRIVER_CD, LICENCE_NO FROM FMS_TRUCK_DRIVER_MST WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					data = "";
					int i = 0;
					driver_cd="";no="";
					
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = cell.getStringCellValue();
						data = data.substring(1, data.length() - 1);	
						
						
						if(cell.getColumnIndex() == 0) {
							data = null;
						}
						
						
						if (data!= null) 
						{  
							data1[i] = data;
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
								query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						} 
						
					}//while(column) completed
					
					query_select = query_select.substring(0, query_select.length() - 4);
					
					if(!company_cd.equals("")) 
					{	
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (data1[w] != null && !data1[w].equals("null")) 
							{
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						
						rset = stmt.executeQuery();
						
						if (rset.next())       
						{
							driver_cd = rset.getString(2);
							no=rset.getString(3);
							
							st_del = conn.prepareStatement(query_delete);
							st_del.setString(1, company_cd);
							st_del.setString(2, driver_cd);
							st_del.setString(3, no);
							
							st_del.executeUpdate();
							
							logger.data(fname, (company_cd+","+driver_cd+","+no+","), conn, "");
							logger_count++;   
							
							st_del.close();
						}
						
						rset.close();
						stmt.close();
					}
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT ENT_PROFILE, DRIVER_CD, LICENCE_NO FROM FMS_TRUCK_DRIVER_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					driver_cd=rset.getString(2);
					no=rset.getString(3);
					
					
					logger.data(fname, (company_cd+","+driver_cd+","+no+","), conn, "N");
				}
				rset.close();
				stmt.close();
				
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_TRUCK_DRIVER_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRUCK_DRIVER_MST()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void FMS_FILLING_STATION_MST() throws SQLException, IOException {
		function_nm = "FMS_FILLING_STATION_MST()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_FILLING_STATION_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_FILLING_STATION_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_FILLING_STATION_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY_TYPE = ? AND FILL_STATION_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') ";
			
			String cd,type,no,eff_dt;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,EFF_DT,FILL_STATION_NAME,FILL_STATION_ABBR,ACTIVE_FLAG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			data1 = new String[column.split(",").length]; 
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_FILLING_STATION_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_FILLING_STATION_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "ENT_PROFILE,EFF_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext())
				{
					query_select = "SELECT COUNTERPARTY_CD, ENTITY_TYPE, FILL_STATION_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_FILLING_STATION_MST WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					data = "";
					int i = 0;
					cd="";type="";no="";eff_dt="";
					
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = cell.getStringCellValue();
						data = data.substring(1, data.length() - 1);	
						
						if(cell.getColumnIndex() == 3) {
							data = null;
						}
						
						if (data!= null) 
						{  
							data1[i] = data;
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
								query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						} 
						
					}//while(column) completed
					
					query_select = query_select.substring(0, query_select.length() - 4);
					
					if(!company_cd.equals("")) 
					{	
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (data1[w] != null && !data1[w].equals("null")) 
							{
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						
						rset = stmt.executeQuery();
						
						if (rset.next())       
						{
							cd = rset.getString(1);
							type = rset.getString(2);
							no=rset.getString(3);
							eff_dt=rset.getString(4);
							
							st_del = conn.prepareStatement(query_delete);
							st_del.setString(1, company_cd);
							st_del.setString(2, cd);
							st_del.setString(3, type);
							st_del.setString(4, no);
							st_del.setString(5, eff_dt);
							
							st_del.executeUpdate();
							
							logger.data(fname, (company_cd+","+cd+","+type+","+no+","+eff_dt+","), conn, "");
							logger_count++;   
							
							st_del.close();
						}
						
						rset.close();
						stmt.close();
					}
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY_TYPE, FILL_STATION_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_FILLING_STATION_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd = rset.getString(2);
					type = rset.getString(3);
					no=rset.getString(4);
					eff_dt=rset.getString(5);
					
					logger.data(fname, (company_cd+","+cd+","+type+","+no+","+eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();
				
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_FILLING_STATION_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_FILLING_STATION_MST()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void FMS_CHECKPOST_MST() throws SQLException, IOException {
		function_nm = "FMS_CHECKPOST_MST()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_CHECKPOST_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_CHECKPOST_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_CHECKPOST_MST WHERE ENT_PROFILE = ? AND CHKPOST_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') ";
			
			String cd,eff_dt;
			
			column = "CHKPOST_NAME,EFF_DT,STATE_CODE,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			data1 = new String[column.split(",").length]; 
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CHECKPOST_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CHECKPOST_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "ENT_PROFILE,EFF_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext())
				{
					query_select = "SELECT CHKPOST_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_CHECKPOST_MST WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					data = "";
					int i = 0;
					cd="";eff_dt="";
					
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = cell.getStringCellValue();
						data = data.substring(1, data.length() - 1);	
						
						if(cell.getColumnIndex() == 0) {
							data = null;
						}
						
						if (data!= null) 
						{  
							data1[i] = data;
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
								query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						} 
						
					}//while(column) completed
					
					query_select = query_select.substring(0, query_select.length() - 4);
					if(!company_cd.equals("")) 
					{	
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (data1[w] != null && !data1[w].equals("null")) 
							{
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						
						rset = stmt.executeQuery();
						
						if (rset.next())       
						{
							cd = rset.getString(1);
							eff_dt=rset.getString(2);
							
							st_del = conn.prepareStatement(query_delete);
							st_del.setString(1, company_cd);
							st_del.setString(2, cd);
							st_del.setString(3, eff_dt);
							
							st_del.executeUpdate();
							
							logger.data(fname, (company_cd+","+cd+","+eff_dt+","), conn, "");
							logger_count++;   
							
							st_del.close();
						}
						
						rset.close();
						stmt.close();
					}
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT ENT_PROFILE, CHKPOST_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_CHECKPOST_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd = rset.getString(2);
					eff_dt=rset.getString(3);
					
					logger.data(fname, (company_cd+","+cd+","+eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();
				
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_CHECKPOST_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_CHECKPOST_MST()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void FMS_TRUCK_TRANSPORTER_LINK() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_TRANSPORTER_LINK()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRUCK_TRANSPORTER_LINK()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRUCK_TRANSPORTER_LINK>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_TRUCK_TRANSPORTER_LINK WHERE ENT_PROFILE = ? AND TRUCK_TRANS_CD = ? AND TRUCK_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') ";
			
			String cd,truck_cd,eff_dt;
			
			column = "TRUCK_TRANS_CD,TRUCK_CD,EFF_DT,RELEASE_DT,REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			data1 = new String[column.split(",").length]; 
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANSPORTER_LINK_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANSPORTER_LINK_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "ENT_PROFILE,EFF_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext())
				{
					query_select = "SELECT TRUCK_TRANS_CD, TRUCK_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_TRUCK_TRANSPORTER_LINK WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					data = "";
					int i = 0;
					cd="";truck_cd="";eff_dt="";
					
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = cell.getStringCellValue();
						data = data.substring(1, data.length() - 1);	
						
						if(cell.getColumnIndex() == 0) {
							query_String = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE TRUCK_TRANS_ABBR = ? ";
							stmt = conn.prepareStatement(query_String);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = cd;
						}
						else if (cell.getColumnIndex() == 1) {
							query_String = "SELECT TRUCK_CD FROM FMS_TRUCK_MST WHERE TRUCK_REG_NUM = ? ";
							stmt = conn.prepareStatement(query_String);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								truck_cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = truck_cd;
						}
						
						if (data!= null) 
						{  
							data1[i] = data;
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
								query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						} 
						
					}//while(column) completed
					
					query_select = query_select.substring(0, query_select.length() - 4);
					if(!company_cd.equals("")) 
					{	
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (data1[w] != null && !data1[w].equals("null")) 
							{
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						
						rset = stmt.executeQuery();
						
						if (rset.next())       
						{
							cd = rset.getString(1);
							truck_cd = rset.getString(2);
							eff_dt=rset.getString(3);
							
							st_del = conn.prepareStatement(query_delete);
							st_del.setString(1, company_cd);
							st_del.setString(2, cd);
							st_del.setString(3, truck_cd);
							st_del.setString(4, eff_dt);
							
							st_del.executeUpdate();
							
							logger.data(fname, (company_cd+","+cd+","+truck_cd+","+eff_dt+","), conn, "");
							logger_count++;   
							
							st_del.close();
						}
						
						rset.close();
						stmt.close();
					}
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT ENT_PROFILE, TRUCK_TRANS_CD, TRUCK_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_TRUCK_TRANSPORTER_LINK WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd = rset.getString(2);
					truck_cd = rset.getString(3);
					eff_dt=rset.getString(4);
					
					logger.data(fname, (company_cd+","+cd+","+truck_cd+","+eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();
				
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_TRUCK_TRANSPORTER_LINK()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRUCK_TRANSPORTER_LINK()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void FMS_TRUCK_DRIVER_TRANS_LINK() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_DRIVER_TRANS_LINK()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRUCK_DRIVER_TRANS_LINK()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRUCK_DRIVER_TRANS_LINK>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_TRUCK_DRIVER_TRANS_LINK WHERE ENT_PROFILE = ? AND TRUCK_TRANS_CD = ? AND DRIVER_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') ";
			
			String cd,driver_cd,eff_dt;
			
			column = "TRUCK_TRANS_CD,DRIVER_CD,EFF_DT,RELEASE_DT,REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			data1 = new String[column.split(",").length]; 
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_TRANS_LINK_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_TRANS_LINK_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "ENT_PROFILE,EFF_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext())
				{
					query_select = "SELECT TRUCK_TRANS_CD, DRIVER_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_TRUCK_DRIVER_TRANS_LINK WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					data = "";
					int i = 0;
					cd="";driver_cd="";eff_dt="";
					
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = cell.getStringCellValue();
						data = data.substring(1, data.length() - 1);	
						
						if(cell.getColumnIndex() == 0) {
							query_String = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE TRUCK_TRANS_ABBR = ? ";
							stmt = conn.prepareStatement(query_String);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = cd;
						}
						else if (cell.getColumnIndex() == 1) {
							query_String = "SELECT DRIVER_CD FROM FMS_TRUCK_DRIVER_MST WHERE LICENCE_NO = ? ";
							stmt = conn.prepareStatement(query_String);
							stmt.setString(1, data);  
							rset = stmt.executeQuery();
							if(rset.next()) {
								driver_cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = driver_cd;
						}
						
						if (data!= null) 
						{  
							data1[i] = data;
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
								query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						} 
						
					}//while(column) completed
					
					query_select = query_select.substring(0, query_select.length() - 4);
					if(!company_cd.equals("")) 
					{	
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (data1[w] != null && !data1[w].equals("null")) 
							{
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						
						rset = stmt.executeQuery();
						
						if (rset.next())       
						{
							cd = rset.getString(1);
							driver_cd = rset.getString(2);
							eff_dt=rset.getString(3);
							
							st_del = conn.prepareStatement(query_delete);
							st_del.setString(1, company_cd);
							st_del.setString(2, cd);
							st_del.setString(3, driver_cd);
							st_del.setString(4, eff_dt);
							
							st_del.executeUpdate();
							
							logger.data(fname, (company_cd+","+cd+","+driver_cd+","+eff_dt+","), conn, "");
							logger_count++;   
							
							st_del.close();
						}
						
						rset.close();
						stmt.close();
					}
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT ENT_PROFILE, TRUCK_TRANS_CD, DRIVER_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_TRUCK_DRIVER_TRANS_LINK WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd = rset.getString(2);
					driver_cd = rset.getString(3);
					eff_dt=rset.getString(4);
					
					logger.data(fname, (company_cd+","+cd+","+driver_cd+","+eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();
				
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_TRUCK_DRIVER_TRANS_LINK()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRUCK_DRIVER_TRANS_LINK()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void FMS_TRUCK_DRIVER_LINK() throws SQLException, IOException {
		function_nm = "FMS_TRUCK_DRIVER_LINK()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRUCK_DRIVER_LINK()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRUCK_DRIVER_LINK>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_TRUCK_DRIVER_LINK WHERE ENT_PROFILE = ? AND TRUCK_CD = ? AND DRIVER_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') ";
			
			String cd,driver_cd,eff_dt;
			
			column = "TRUCK_CD,DRIVER_CD,EFF_DT,RELEASE_DT,REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			data1 = new String[column.split(",").length]; 
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_LINK_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_LINK_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "ENT_PROFILE,EFF_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext())
				{
					query_select = "SELECT TRUCK_CD, DRIVER_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_TRUCK_DRIVER_LINK WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					data = "";
					int i = 0;
					cd="";driver_cd="";eff_dt="";
					
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = cell.getStringCellValue();
						data = data.substring(1, data.length() - 1);	
						
						if(cell.getColumnIndex() == 0) {
							query_String = "SELECT TRUCK_CD FROM FMS_TRUCK_MST WHERE TRUCK_REG_NUM = ? ";
							stmt = conn.prepareStatement(query_String);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = cd;
						}
						else if (cell.getColumnIndex() == 1) {
							query_String = "SELECT DRIVER_CD FROM FMS_TRUCK_DRIVER_MST WHERE LICENCE_NO = ? ";
							stmt = conn.prepareStatement(query_String);
							stmt.setString(1, data);  
							rset = stmt.executeQuery();
							if(rset.next()) {
								driver_cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = driver_cd;
						}
						
						if (data!= null) 
						{  
							data1[i] = data;
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
								query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						} 
						
					}//while(column) completed
					
					query_select = query_select.substring(0, query_select.length() - 4);
					if(!company_cd.equals("")) 
					{	
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (data1[w] != null && !data1[w].equals("null")) 
							{
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						
						rset = stmt.executeQuery();
						
						if (rset.next())       
						{
							cd = rset.getString(1);
							driver_cd = rset.getString(2);
							eff_dt=rset.getString(3);
							
							st_del = conn.prepareStatement(query_delete);
							st_del.setString(1, company_cd);
							st_del.setString(2, cd);
							st_del.setString(3, driver_cd);
							st_del.setString(4, eff_dt);
							
							st_del.executeUpdate();
							
							logger.data(fname, (company_cd+","+cd+","+driver_cd+","+eff_dt+","), conn, "");
							logger_count++;   
							
							st_del.close();
						}
						
						rset.close();
						stmt.close();
					}
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT ENT_PROFILE, TRUCK_CD, DRIVER_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_TRUCK_DRIVER_LINK WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd = rset.getString(2);
					driver_cd = rset.getString(3);
					eff_dt=rset.getString(4);
					
					logger.data(fname, (company_cd+","+cd+","+driver_cd+","+eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();
				
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_TRUCK_DRIVER_LINK()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRUCK_DRIVER_LINK()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void FMS_LINK_CHECKPOST_PLANT() throws SQLException, IOException {
		function_nm = "FMS_LINK_CHECKPOST_PLANT()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_LINK_CHECKPOST_PLANT()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_LINK_CHECKPOST_PLANT>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_LINK_CHECKPOST_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY_TYPE = ? AND PLANT_SEQ_NO = ? AND CHKPOST_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') ";
			
			String cd,type,plant,chk_cd,eff_dt;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,PLANT_SEQ_NO,CHKPOST_CD,EFF_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,RELEASE_DT";
			data1 = new String[column.split(",").length]; 
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_LINK_CHECKPOST_PLANT_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LINK_CHECKPOST_PLANT_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "ENT_PROFILE,EFF_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext())
				{
					query_select = "SELECT COUNTERPARTY_CD, ENTITY_TYPE, PLANT_SEQ_NO, CHKPOST_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_LINK_CHECKPOST_PLANT WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					data = "";
					int i = 0;
					cd="";type="";plant="";chk_cd="";eff_dt="";
					
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = cell.getStringCellValue();
						data = data.substring(1, data.length() - 1);	
						
						if(cell.getColumnIndex() == 1) {
							query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							stmt = conn.prepareStatement(query_String);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = cd;
						}
						else if (cell.getColumnIndex() == 4) {
							query_String = "SELECT CHKPOST_CD FROM FMS_CHECKPOST_MST WHERE CHKPOST_NAME = ? ";
							stmt = conn.prepareStatement(query_String);
							stmt.setString(1, data);  
							rset = stmt.executeQuery();
							if(rset.next()) {
								chk_cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = chk_cd;
						}
						
						if (data!= null) 
						{  
							data1[i] = data;
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
								query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						} 
						
					}//while(column) completed
					
					query_select = query_select.substring(0, query_select.length() - 4);
					if(!company_cd.equals("")) 
					{	
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (data1[w] != null && !data1[w].equals("null")) 
							{
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						
						rset = stmt.executeQuery();
						
						if (rset.next())       
						{
							cd = rset.getString(1);
							type = rset.getString(2);
							plant = rset.getString(3);
							chk_cd = rset.getString(4);
							eff_dt=rset.getString(5);
							
							st_del = conn.prepareStatement(query_delete);
							st_del.setString(1, company_cd);
							st_del.setString(2, cd);
							st_del.setString(3, type);
							st_del.setString(4, plant);
							st_del.setString(5, chk_cd);
							st_del.setString(6, eff_dt);
							
							st_del.executeUpdate();
							
							logger.data(fname, (company_cd+","+cd+","+type+","+plant+","+chk_cd+","+eff_dt+","), conn, "");
							logger_count++;   
							
							st_del.close();
						}
						
						rset.close();
						stmt.close();
					}
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY_TYPE, PLANT_SEQ_NO, CHKPOST_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_LINK_CHECKPOST_PLANT WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd = rset.getString(2);
					type = rset.getString(3);
					plant = rset.getString(4);
					chk_cd = rset.getString(5);
					eff_dt=rset.getString(6);
					
					logger.data(fname, (company_cd+","+cd+","+type+","+plant+","+chk_cd+","+eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();
				
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_LINK_CHECKPOST_PLANT()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_LINK_CHECKPOST_PLANT()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void FMS_AGMT_MST() throws SQLException, IOException {
		function_nm = "FMS_AGMT_MST(DLNG)()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_AGMT_MST(DLNG)()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_AGMT_MST(DLNG)>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY_TYPE = ? AND PLANT_SEQ_NO = ? AND CHKPOST_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') ";
			
			String cd,type,no,rev;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,SIGNING_DT,START_DT,END_DT,RENEWAL_DT,AGMT_BASE,AGMT_TYP,STATUS,"
					+ "BUYER_NOM_CLAUSE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_CLAUSE,SELLER_NOM,SELLER_MONTH_NOM,"
					+ "SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,MEASUREMENT,MEAS_STANDARD,"
					+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
					+ "ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,FLAG,REV_DT,REMARKS,LIABILITY_CLAUSE,BILLING_CLAUSE,LC_CLAUSE,RENEWAL_FLAG,PRE_APPROVAL_DATE,"
					+ "PRE_APPROVAL,PRE_APPROVAL_BY,REOPEN_REQUEST_FLAG,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVAL_DT,REOPEN_REQUEST_BY,"
					+ "REOPEN_APPROVE_BY,REMARK,CONT_NAME,AGMT_REF_NO,BILLING_FLAG,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,MEAS_CLAUSE,"
					+ "SPEC_CLAUSE,LIABILITY,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE";
			
			data1 = new String[column.split(",").length]; 
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_MST(DLNG)_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_MST(DLNG)_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "ENT_PROFILE,EFF_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext())
				{
					query_select = "SELECT COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_AGMT_MST WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					data = "";
					int i = 0;
					cd="";type="";no="";rev="";
					
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = cell.getStringCellValue();
						data = data.substring(1, data.length() - 1);	
						
						if(cell.getColumnIndex() == 1) {	//COUNTERPARTY_CD
							query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							stmt = conn.prepareStatement(query_String);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = cd;
						}
						else if(cell.getColumnIndex() == 10) {	//AGMT_TYP
							data = data.substring(1, data.length()-1);
							if(data.equals("T")){
								data = "'"+ "0" +"'";
							}
							else if(data.equals("S")) {
								data = "'"+ "1" +"'";
							}
						}
						else if(cell.getColumnIndex() == 11) {	//STATUS CHANGES TO A & D
							
							if(data.equals("Y")) {
								data =  "A" ;
							}
							else {
								data =  "D" ;
							}
						}
						
						if (data!= null) 
						{  
							data1[i] = data;
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
								query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						} 
						
					}//while(column) completed
					
					query_select = query_select.substring(0, query_select.length() - 4);
					if(!company_cd.equals("")) 
					{	
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (data1[w] != null && !data1[w].equals("null")) 
							{
//								System.out.println(w+">>????"+data1[w]);
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						
						rset = stmt.executeQuery();
						
						if (rset.next())       
						{
							cd = rset.getString(1);
							type = rset.getString(2);
							no = rset.getString(3);
							rev = rset.getString(4);
							
							st_del = conn.prepareStatement(query_delete);
							st_del.setString(1, company_cd);
							st_del.setString(2, cd);
							st_del.setString(3, type);
							st_del.setString(4, no);
							st_del.setString(5, rev);
							
							System.out.println("not del");
							st_del.executeUpdate();
							System.out.println("del");
							
							logger.data(fname, (company_cd+","+cd+","+type+","+no+","+rev+","), conn, "");
							logger_count++;   
							
							st_del.close();
						}
						
						rset.close();
						stmt.close();
					}
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_AGMT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(query_data_left);
				stmt.setString(1, company_cd);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd = rset.getString(2);
					type = rset.getString(3);
					no = rset.getString(4);
					rev = rset.getString(5);
					
					logger.data(fname, (company_cd+","+cd+","+type+","+no+","+rev+","), conn, "N");
				}
				rset.close();
				stmt.close();
				
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println();
			System.out.println("<<ROLLBACK_END>><<FMS_AGMT_MST(DLNG)()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_AGMT_MST(DLNG)()>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}
	
	public void setChecked_Values(String checked_val) {
		checked_values = checked_val;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setSysDateTime(String dt) {
		sysDateTime = dt;
	}
	
	public String getMsg_Type() {
		return msg_type;
	}
	
	public void setMigration_Setup_Dir(String dir) {
		migration_setup_dir = dir;
	}
	
	public void setStart_End_Dt(String dt) {
		start_end_dt = dt;
	}
}
