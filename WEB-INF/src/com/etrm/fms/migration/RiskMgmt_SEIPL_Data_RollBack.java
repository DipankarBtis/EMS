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

public class RiskMgmt_SEIPL_Data_RollBack {


	

	String db_src_file_name="RiskMgmt_SEIPL_Data_Rollback.java";

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

			fname= "DataLogs/RollBack/RiskMgmt_SEIPL_Data_RollBack(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/RollBack/RiskMgmt_SEIPL_Data_RollBack_Error(log)"+sysDateTime+".csv";
			
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
	    		
				if (conn != null && !checked_values.equals(""))
				{
		    		preferences.put("Flag", "0");
					conn.setAutoCommit(false);
					
					if (checked_values.contains("FMS_LIMIT_DTL,")) {
						FMS_LIMIT_DTL();
					}
					
					if (checked_values.contains("FMS_LIMIT_MST,")) {
						FMS_LIMIT_MST();
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
	public void FMS_LIMIT_MST() throws SQLException, IOException {
		function_nm = "FMS_LIMIT_MST()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_LIMIT_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_LIMIT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_LIMIT_MST WHERE COUNTERPARTY_CD = ? AND BANK_CD = ? AND GX = ? AND LIMIT_ID = ?  ";

			String bank_cd,limit,gx;
			
			column = "COUNTERPARTY_CD,BANK_CD,GX,LIMIT_ID,CREDIT_RATING,RATING_EFF_DT,PARENT_OWNSHIP_CD,PARENT_OWNSHIP,"
					+ "PARENT_ENT_DT,PARENT_EXIT_DT,REF_NO,STATUS,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE";
			data1 = new String[column.split(",").length]; 
 
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_LIMIT_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LIMIT_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COUNTERPARTY_CD, BANK_CD, LIMIT_ID, GX, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COUNTERPARTY_CD, BANK_CD, GX, LIMIT_ID FROM FMS_LIMIT_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";
						int i = 0;
						bank_cd="";limit="";gx="";
						
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);	
							
							
							if(cell.getColumnIndex() == 0) {
								cd = data.toUpperCase();
								
								if (!cd.equals("0") && cd.contains("IGX")) {	// IGX_Cd
									query_String = "SELECT COUNTERPARTY_CD FROM FMS_COMPANY_EXCHG_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									stmt = conn.prepareStatement(query_String);
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
									query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									stmt = conn.prepareStatement(query_String);
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
							else if(cell.getColumnIndex() == 1) {	// Bank_Cd
								bank_cd = data.toUpperCase();
								
								if (!bank_cd.equals("0") && !bank_cd.equals("null")) {
									query_String = "SELECT BANK_CD FROM FMS_BANK_MST WHERE UPPER(BANK_NAME) = ? ";
									stmt = conn.prepareStatement(query_String);
									stmt.setString(1, bank_cd.toUpperCase());
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
							else if(cell.getColumnIndex() == 2) {
								gx =data;
							}
							else if (cell.getColumnIndex() == 3) {	// Limit_Id
								limit = data;
								
								if (!limit.equals("0")) {
									query_String = "SELECT LIMIT_ID FROM FMS_LIMIT_MST WHERE COUNTERPARTY_CD = ? AND BANK_CD = ? AND GX = ? ";
									stmt = conn.prepareStatement(query_String);
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
							    	abbr = data.toUpperCase();
								
									query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									stmt = conn.prepareStatement(query_String);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										data = rset.getString(1);
									}
									else {
										data = "null" ;
									}
									
									rset.close();
									stmt.close();
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
						    	cd=rset.getString(1);
						    	bank_cd=rset.getString(2);
						    	gx=rset.getString(3);
						    	limit=rset.getString(4);
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, bank_cd);
							    st_del.setString(3, gx);
							    st_del.setString(4, limit);
							 
							    st_del.executeUpdate();
							  
							    logger.data(fname, (cd+","+bank_cd+","+limit+","+gx+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COUNTERPARTY_CD, BANK_CD, LIMIT_ID, GX FROM FMS_LIMIT_MST WHERE ENT_PROFILE = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					cd=rset.getString(1);
			        bank_cd=rset.getString(2);
			        limit=rset.getString(3);
			        gx=rset.getString(4);
			        
			        
					logger.data(fname, (cd+","+bank_cd+","+limit+","+gx+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_LIMIT_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_LIMIT_MST()>>,,,,,,,", conn);
			
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
	
	public void FMS_LIMIT_DTL() throws SQLException, IOException {
		function_nm = "FMS_LIMIT_DTL()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_LIMIT_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_LIMIT_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_LIMIT_DTL WHERE COUNTERPARTY_CD = ? AND BANK_CD = ? AND GX = ? AND LIMIT_ID = ?  AND SEQ_NO = ? ";

			String bank_cd,limit,gx,seq_no;
			
			column = "COUNTERPARTY_CD,BANK_CD,LIMIT_ID,SEQ_NO,GX,REF_NO,LIMIT_TYPE,ACTION_TYPE,CATEGORY,AMT,AMT_UNIT,EFF_DT,EXP_DT,"
					+ "REVIEW_DT,REMARKS,IS_ACTIVE,INACTIVATION_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE";
			data1 = new String[column.split(",").length]; 
 
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_LIMIT_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LIMIT_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COUNTERPARTY_CD, BANK_CD, LIMIT_ID, GX, SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COUNTERPARTY_CD, BANK_CD, GX, LIMIT_ID, SEQ_NO FROM FMS_LIMIT_DTL WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";
						int i = 0;
						bank_cd="";limit="";gx="";seq_no="";
						
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);	
							
							
							if(cell.getColumnIndex() == 0) {
								cd = data.toUpperCase();
								
								if (!cd.equals("0")) {	// Counterparty_Cd
									query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									stmt = conn.prepareStatement(query_String);
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
							else if(cell.getColumnIndex() == 1) {	// Bank_Cd
								bank_cd = data.toUpperCase();
								
								if (!bank_cd.equals("0") && !bank_cd.equals("null")) {
									query_String = "SELECT BANK_CD FROM FMS_BANK_MST WHERE UPPER(BANK_NAME) = ? ";
									stmt = conn.prepareStatement(query_String);
									stmt.setString(1, bank_cd.toUpperCase());
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
								limit = data;
								
								if (!limit.equals("0")) {
									query_String = "SELECT LIMIT_ID FROM FMS_LIMIT_MST WHERE COUNTERPARTY_CD = ? AND BANK_CD = ? AND GX = ? ";
									stmt = conn.prepareStatement(query_String);
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
						    	cd=rset.getString(1);
						    	bank_cd=rset.getString(2);
						    	gx=rset.getString(3);
						    	limit=rset.getString(4);
						    	seq_no=rset.getString(5);
						    	
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, bank_cd);
							    st_del.setString(3, gx);
							    st_del.setString(4, limit);
							    st_del.setString(5, seq_no);
							 
							    st_del.executeUpdate();
							  
							    logger.data(fname, (cd+","+bank_cd+","+limit+","+seq_no+","+gx+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COUNTERPARTY_CD, BANK_CD, LIMIT_ID, SEQ_NO, GX FROM FMS_LIMIT_DTL WHERE ENT_PROFILE = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					cd=rset.getString(1);
			        bank_cd=rset.getString(2);
			        limit=rset.getString(3);
			        seq_no=rset.getString(4);
			        gx=rset.getString(5);
			        
			        
					logger.data(fname, (cd+","+bank_cd+","+limit+","+seq_no+","+gx+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_LIMIT_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_LIMIT_DTL()>>,,,,,,,", conn);
			
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
