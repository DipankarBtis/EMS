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

public class TermOps_SEIPL_Data_Rollback {


	

	String db_src_file_name="TermOps_SEIPL_Data_Rollback.java";

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
	String cd ="",abbr="",company_cd="",agmt_no="",cp_abbr="";
	String query_select = "", query_delete = "",query_fetch_columnname = "",query_data_left = "",query_String="",query_String1="";
	
	int logger_count = 0;
	String[] data1 = null;
	
	DataMigration_Logger logger = new DataMigration_Logger();
	
	
	public void init() {
		function_nm = "init()";
		try {

			fname= "DataLogs/RollBack/TermOps_SEIPL_Data_RollBack(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/RollBack/TermOps_SEIPL_Data_RollBack_Error(log)"+sysDateTime+".csv";
			
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
					
					if (checked_values.contains("FMS_TANK_INVENTORY_DTL")) {
						FMS_TANK_INVENTORY_DTL();
					}
					
					if (checked_values.contains("FMS_TANK_CONSUMPTION_MST")) {
						FMS_TANK_CONSUMPTION_MST();
					}
					
					if (checked_values.contains("FMS_TANK_MST")) {
						FMS_TANK_MST();
					}
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
	public void FMS_TANK_MST() throws SQLException, IOException {
		function_nm = "FMS_TANK_MST()";
		try {
			column=" ";
			logger_count=0;		
			
			System.out.println("<<ROLLBACK_START>><<FMS_TANK_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TANK_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_TANK_MST WHERE COMPANY_CD = ? AND TANK_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') ";

			String tank_cd,eff_dt;
			
			column = "COMPANY_CD,TANK_CD,TANK_NAME,EFF_DT,STATUS,TANK_T1_VOLUME,TANK_T1_HEIGHT,TANK_T2_VOLUME,TANK_T2_HEIGHT,TANK_D1_VOLUME,TANK_D1_HEIGHT,TANK_D2_VOLUME,TANK_D2_HEIGHT,TANK_DIAMETER,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TANK_PI_TAG";
			data1 = new String[column.split(",").length]; 
 
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TANK_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TANK_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, TANK_CD, EFF_DT, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, TANK_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_TANK_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";
						int i = 0;
						tank_cd="";eff_dt="";
						
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);	
							if(cell.getColumnIndex() == 0) {
								company_cd = data;
							}
							else if(cell.getColumnIndex() == 1) {
								
								tank_cd = data;
							}
							else if(cell.getColumnIndex() == 3) {
								
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
			            if(!tank_cd.equals("")) 
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
						    	company_cd=rset.getString(1);
						    	tank_cd=rset.getString(2);
						    	eff_dt=rset.getString(3);
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, company_cd);
							    st_del.setString(2, tank_cd);
							    st_del.setString(3, eff_dt);
							 
							  
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+tank_cd+","+eff_dt+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, TANK_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_TANK_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        tank_cd=rset.getString(2);
			        eff_dt=rset.getString(3);
			        
			        
					logger.data(fname, (company_cd+","+tank_cd+","+eff_dt+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_TANK_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TANK_MST()>>,,,,,,,", conn);
			
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
	public void FMS_TANK_CONSUMPTION_MST() throws SQLException, IOException {
		function_nm = "FMS_TANK_CONSUMPTION_MST()";
		try {
			column=" ";
			logger_count=0;
			
			
			System.out.println("<<ROLLBACK_START>><<FMS_TANK_CONSUMPTION_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TANK_CONSUMPTION_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_TANK_CONSUMPTION_MST WHERE COMPANY_CD = ?  AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ";

			String eff_dt;
			
			column = "COMPANY_CD,EFF_DT,PERCENTAGE,REMARK,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,FLAG";
			data1 = new String[column.split(",").length]; 
 
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TANK_CONSUMPTION_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TANK_CONSUMPTION_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, EFF_DT, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_TANK_CONSUMPTION_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";
						int i = 0;
						eff_dt="";
						
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);	
							
							if(cell.getColumnIndex() == 0) {
								company_cd = data;
							}
							
							else if(cell.getColumnIndex() == 1) {
								
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
						    	company_cd=rset.getString(1);
						    	eff_dt=rset.getString(2);
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, company_cd);
							    st_del.setString(2, eff_dt);
							 
							  
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+eff_dt+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_TANK_CONSUMPTION_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        eff_dt=rset.getString(2);
			        
			        
					logger.data(fname, (company_cd+","+eff_dt+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_TANK_CONSUMPTION_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TANK_CONSUMPTION_MST()>>,,,,,,,", conn);
			
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
	public void FMS_TANK_INVENTORY_DTL() throws SQLException, IOException {
		function_nm = "FMS_TANK_INVENTORY_DTL()";
		try {
			column=" ";
			logger_count=0;			
			
			System.out.println("<<ROLLBACK_START>><<FMS_TANK_INVENTORY_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TANK_INVENTORY_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_TANK_INVENTORY_DTL WHERE COMPANY_CD = ?  AND INV_LEVEL_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND TANK_CD = ?";

			String inv_dt,tank_cd;
			
			column = "COMPANY_CD,INV_LEVEL_DT,TANK_CD,TANK_VOLUME,TANK_HEIGHT,TANK_MMSCM,TANK_CONV_FACTOR_1,TANK_CONV_FACTOR_2,TANK_MMBTU,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			data1 = new String[column.split(",").length]; 
 
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TANK_INVENTORY_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TANK_INVENTORY_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, EFF_DT, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, TO_CHAR(INV_LEVEL_DT,'DD/MM/YYYY hh24:mi:ss'), TANK_CD FROM FMS_TANK_INVENTORY_DTL WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";
						int i = 0;
						inv_dt="";tank_cd="";
						
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);	
							
							if(cell.getColumnIndex() == 0) {
								company_cd = data;
							}
							
							else if(cell.getColumnIndex() == 1) {
								
								inv_dt = data;
							}
							else if(cell.getColumnIndex() == 2) {
								
								tank_cd = data;
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
						    	company_cd=rset.getString(1);
						    	inv_dt=rset.getString(2);
						    	tank_cd=rset.getString(3);
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, company_cd);
							    st_del.setString(2, inv_dt);
							    st_del.setString(3, tank_cd);
							 
							  
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+inv_dt+","+tank_cd+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, TO_CHAR(INV_LEVEL_DT,'DD/MM/YYYY hh24:mi:ss'), TANK_CD FROM FMS_TANK_INVENTORY_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        inv_dt=rset.getString(2);
			        tank_cd=rset.getString(3);
			        
			        
					logger.data(fname, (company_cd+","+inv_dt+","+tank_cd+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_TANK_INVENTORY_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TANK_INVENTORY_DTL()>>,,,,,,,", conn);
			
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
