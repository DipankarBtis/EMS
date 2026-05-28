package com.etrm.fms.migration;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileReader;
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

public class Sales_SEIPL_Data_RollBack {
	
	String db_src_file_name = "Sales_SEIPL_Data_RollBack.java";
	String migration_setup_dir = "";
	String sysDateTime = "";
	String checked_values = "", msg = "", msg_type = "";
	
	String function_nm = "";
	String start_end_dt = null;
	
	PreparedStatement stmt,st_del,stmt1,stmt2;
	Connection conn;
	ResultSet rset,rset1,rset2;
	
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
	
	String data = "", column = "",value = "", cont_ref="",name="",exchg_cd="";
	String cd ="",abbr="",company_cd="2",agmt_no="",cp_abbr="";
	String query_select = "", query_delete = "",query_fetch_columnname = "",query_data_left = "",query_String="",query_String1="",query_String2="";
	
	int logger_count = 0,no=0;
	String[] data1 = null;
	
	DataMigration_Logger logger = new DataMigration_Logger();
	
	
	
	public void init() {
		function_nm = "init()";
		try {

			fname= "DataLogs/RollBack/Sales_SEIPL_Data_RollBack(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/RollBack/Sales_SEIPL_Data_RollBack(log)"+sysDateTime+".csv";
			
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

					
					if (checked_values.contains("FMS_INVOICE_DTL,")) { 
						FMS_INVOICE_DTL();
					}
					if (checked_values.contains("FMS_INVOICE_MST,")) {
						FMS_INVOICE_MST();
					}
					if (checked_values.contains("FMS_SUPPLY_ALLOC_REVISED,")) {
						FMS_SUPPLY_ALLOC_REVISED();
					}
					if (checked_values.contains("FMS_SUPPLY_PURCHASE_MAP_DTL,")) {
						FMS_SUPPLY_PURCHASE_MAP_DTL();
					}
//					if (checked_values.contains("LOG_SUPPLY_CONT_MST,")) {
//						LOG_SUPPLY_CONT_MST();
//					}FMS_SUPPLY_CONT_PLANT_CHRG
					if (checked_values.contains("FMS_SUPPLY_CONT_PLANT_CHRG,")) {
						FMS_SUPPLY_CONT_PLANT_CHRG();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_DCQ_DTL,")) {
						FMS_SUPPLY_CONT_DCQ_DTL();
					}
//					if (checked_values.contains("LOG_FMS_SECURITY_MST,")) {
//						LOG_FMS_SECURITY_MST();
//					}
					if (checked_values.contains("FMS_SECURITY_MST(S),")) {
						FMS_SECURITY_MST();
					}
					if (checked_values.contains("FMS_SECURITY_DEAL_MAP(S),")) {
						FMS_SECURITY_DEAL_MAP();
					}
					if (checked_values.contains("FMS_DAILY_ALLOCATION_DTL_CT,")) { 
						FMS_DAILY_ALLOCATION_DTL_CT();
					}
					if (checked_values.contains("FMS_DAILY_ALLOCATION_DTL,")) { 
						FMS_DAILY_ALLOCATION_DTL();
					}
					if (checked_values.contains("FMS_METER_TICKET_READING,")) { 
						FMS_METER_TICKET_READING();
					}
					if (checked_values.contains("FMS_CONT_PRICE_MIN_DTL(S),")) { 
						FMS_CONT_PRICE_MIN_DTL();
					}
					if (checked_values.contains("FMS_CONT_PRICE_DTL(S),")) { 
						FMS_CONT_PRICE_DTL();
					}
					if (checked_values.contains("FMS_SUPPLY_BILLING_DTL,")) { 
						FMS_SUPPLY_BILLING_DTL();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_LIABILITY,")) { 
						FMS_SUPPLY_CONT_LIABILITY();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_TRANSPTR,")) { 
						FMS_SUPPLY_CONT_TRANSPTR();	
					}
					if (checked_values.contains("FMS_DAILY_SELLER_NOM_DTL,")) {
						FMS_DAILY_SELLER_NOM_DTL();
					}
					if (checked_values.contains("FMS_DAILY_SELLER_NOM,")) {
						FMS_DAILY_SELLER_NOM();
					}
					if (checked_values.contains("FMS_DAILY_BUYER_NOM_DTL,")) { 
						FMS_DAILY_BUYER_NOM_DTL();
					}
					if (checked_values.contains("FMS_DAILY_BUYER_NOM,")) { 
						FMS_DAILY_BUYER_NOM();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_BU,")) { 
						FMS_SUPPLY_CONT_BU();
					}
					if (checked_values.contains("FMS_SUPPLY_AGMT_LIABILITY,")) { 
						FMS_SUPPLY_AGMT_LIABILITY();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_PLANT,")) { 
						FMS_SUPPLY_CONT_PLANT();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_MST,")) {
						FMS_SUPPLY_CONT_MST();
					}
					if (checked_values.contains("FMS_AGMT_DEACTIVATION_DTL,")) {
						FMS_AGMT_DEACTIVATION_DTL();
					}
					if (checked_values.contains("FMS_AGMT_BILLING_DTL,")) {
						FMS_AGMT_BILLING_DTL();
					}
					if (checked_values.contains("FMS_AGMT_BU,")) {
						FMS_AGMT_BU();
					}
					if (checked_values.contains("FMS_AGMT_PLANT,")) {
						FMS_AGMT_PLANT();
					}
					if (checked_values.contains("FMS_AGMT_TRANSPTR,")) {
						FMS_AGMT_TRANSPTR();
					}
					if (checked_values.contains("FMS_AGMT_MST,")) {
						FMS_AGMT_MST();
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
				if(rset != null){try{rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null) {try {stmt.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(st_del != null) {try {st_del.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null) {try {stmt1.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null) {try {stmt2.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(conn != null) {try {conn.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(file != null) {try {file.close();} catch (Exception e ) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			
			catch (Exception e){
			    	new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					msg = "One of the Functions faced an Error. RollBack Terminated.";
					msg_type = "E";
			    }
		     }
	}
	
	//FMS_AGMT_MST
	public void FMS_AGMT_MST() throws SQLException, IOException {
		function_nm = "FMS_AGMT_MST()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_AGMT_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_AGMT_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_AGMT_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ?  ";

			String  agmt_type, agmt_no, agmt_rev;
			
		
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,SIGNING_DT,START_DT,END_DT,RENEWAL_DT,AGMT_BASE,AGMT_TYP,STATUS,BUYER_NOM_CLAUSE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_CLAUSE,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,"
		    +"SPEC_GAS_MAX_ENERGY,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,FLAG,REV_DT,REMARKS,LIABILITY_CLAUSE,BILLING_CLAUSE,LC_CLAUSE,RENEWAL_FLAG,PRE_APPROVAL_DATE,PRE_APPROVAL,PRE_APPROVAL_BY,REOPEN_REQUEST_FLAG,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVAL_DT,REOPEN_REQUEST_BY,REOPEN_APPROVE_BY,REMARK,CONT_NAME,BILLING_FLAG,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,MEAS_CLAUSE,SPEC_CLAUSE,LIABILITY,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE ";
				
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_AGMT_MST WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						agmt_type=""; agmt_no=""; agmt_rev=""; 
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
//					    		abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
//					    		abbr = abbr.substring(1, abbr.length()-1);
								abbr = data;
					    		data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		
					    		
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = cd;
					    	}
							
					    	else if (cell.getColumnIndex() == 58) {	// AGMT_REF_NO
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
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);

					   
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_type=rset.getString(3);
						        agmt_no=rset.getString(4);
						        agmt_rev=rset.getString(5);
						       
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_type);
							    st_del.setString(3, agmt_no);
							    st_del.setString(4, agmt_rev);
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_AGMT_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_type=rset.getString(3);
			        agmt_no=rset.getString(4);
			        agmt_rev=rset.getString(5);
			        
			        
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_AGMT_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_AGMT_MST()>>,,,,", conn);
			
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

	//FMS_AGMT_TRANSPTR
	public void FMS_AGMT_TRANSPTR() throws SQLException, IOException {
		function_nm = "FMS_AGMT_TRANSPTR()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_AGMT_TRANSPTR()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_AGMT_TRANSPTR>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_AGMT_TRANSPTR WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND TRANSPORTER_CD = ? AND PLANT_SEQ_NO = ? ";

			String  agmt_type, agmt_no, agmt_rev, transporter_cd, plant_seq_no;
			
		
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT";
				
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_TRANSPTR_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_TRANSPTR_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,TRANSPORTER_CD,PLANT_SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,TRANSPORTER_CD,PLANT_SEQ_NO FROM FMS_AGMT_TRANSPTR WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						agmt_type=""; agmt_no=""; agmt_rev=""; 
						transporter_cd=""; plant_seq_no="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
//					    		abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
//					    		abbr = abbr.substring(1, abbr.length()-1);
								abbr = data;
					    		data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		
					    		
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = cd;
					    	}
							
					    	else if (cell.getColumnIndex() == 5) {	// TRANSPORTER Counterparty_Cd
				    		abbr = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!abbr.equals("NULL")) {
								abbr = abbr.substring(1, abbr.length() - 1);
							}
							query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(query_String);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			data = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		transporter_cd = data;
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
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
					   
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_type=rset.getString(3);
						        agmt_no=rset.getString(4);
						        agmt_rev=rset.getString(5);
						        transporter_cd=rset.getString(6);
						        plant_seq_no=rset.getString(7);
						       
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_type);
							    st_del.setString(3, agmt_no);
							    st_del.setString(4, agmt_rev);
							    st_del.setString(5, transporter_cd);
							    st_del.setString(6, plant_seq_no);
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+transporter_cd+","+plant_seq_no+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,TRANSPORTER_CD,PLANT_SEQ_NO FROM FMS_AGMT_TRANSPTR WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_type=rset.getString(3);
			        agmt_no=rset.getString(4);
			        agmt_rev=rset.getString(5);
			        transporter_cd=rset.getString(6);
			        plant_seq_no=rset.getString(7);
			        
			        
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+transporter_cd+","+plant_seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_AGMT_TRANSPTR()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_AGMT_TRANSPTR()>>,,,,,,", conn);
			
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
	
	//FMS_AGMT_PLANT
	public void FMS_AGMT_PLANT() throws SQLException, IOException {
		function_nm = "FMS_AGMT_PLANT()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_AGMT_PLANT()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_AGMT_PLANT>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_AGMT_PLANT WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = ? ";

			String  agmt_type, agmt_no, agmt_rev, plant_seq_no;
			
		
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT";
				
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_PLANT_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_PLANT_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,PLANT_SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,PLANT_SEQ_NO FROM FMS_AGMT_PLANT WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						agmt_type=""; agmt_no=""; agmt_rev=""; 
						 plant_seq_no="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
//					    		abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
//					    		abbr = abbr.substring(1, abbr.length()-1);
								abbr = data;
					    		data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		
					    		
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = cd;
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
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
					   
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_type=rset.getString(3);
						        agmt_no=rset.getString(4);
						        agmt_rev=rset.getString(5);
						        plant_seq_no=rset.getString(6);
						       
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_type);
							    st_del.setString(3, agmt_no);
							    st_del.setString(4, agmt_rev);
							    st_del.setString(5, plant_seq_no);
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+plant_seq_no+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,PLANT_SEQ_NO FROM FMS_AGMT_PLANT WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_type=rset.getString(3);
			        agmt_no=rset.getString(4);
			        agmt_rev=rset.getString(5);
			        plant_seq_no=rset.getString(6);
			        
			        
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+plant_seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_AGMT_PLANT()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_AGMT_PLANT()>>,,,,,", conn);
			
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
	
	//FMS_AGMT_BU
	public void FMS_AGMT_BU() throws SQLException, IOException {
		function_nm = "FMS_AGMT_BU()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_AGMT_BU()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_AGMT_BU>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_AGMT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = ? ";

			String  agmt_type, agmt_no, agmt_rev, plant_seq_no;
			
		
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
				
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_BU_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_BU_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,PLANT_SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,PLANT_SEQ_NO FROM FMS_AGMT_BU WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						agmt_type=""; agmt_no=""; agmt_rev=""; 
						 plant_seq_no="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
//					    		abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
//					    		abbr = abbr.substring(1, abbr.length()-1);
								abbr = data;
					    		data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		
					    		
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = cd;
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
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
					   
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_type=rset.getString(3);
						        agmt_no=rset.getString(4);
						        agmt_rev=rset.getString(5);
						        plant_seq_no=rset.getString(6);
						       
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_type);
							    st_del.setString(3, agmt_no);
							    st_del.setString(4, agmt_rev);
							    st_del.setString(5, plant_seq_no);
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+plant_seq_no+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,PLANT_SEQ_NO FROM FMS_AGMT_BU WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_type=rset.getString(3);
			        agmt_no=rset.getString(4);
			        agmt_rev=rset.getString(5);
			        plant_seq_no=rset.getString(6);
			        
			        
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+plant_seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_AGMT_BU()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_AGMT_BU()>>,,,,,", conn);
			
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
	
	//FMS_AGMT_BILLING_DTL
	public void FMS_AGMT_BILLING_DTL() throws SQLException, IOException {
		function_nm = "FMS_AGMT_BILLING_DTL()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_AGMT_BILLING_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_AGMT_BILLING_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_AGMT_BILLING_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? ";

			String  agmt_type, agmt_no, agmt_rev,seq_no,state_code,int_cd;
			
		
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,"
					+ "INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,"
					+ "MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE";
				
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_BILLING_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_BILLING_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_AGMT_BILLING_DTL WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						agmt_type=""; agmt_no=""; agmt_rev=""; seq_no="";state_code="";int_cd="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    		data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		
					    		
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = cd;
					    	}
							
					    	else if (cell.getColumnIndex() == 2) {	// agmt_type
				    			agmt_type = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
				    			if (!agmt_type.equals("null")) {
				    				agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
								}
				    			data = agmt_type;
				    		}
					    	else if (cell.getColumnIndex() == 3) {	// agmt_no
				    			agmt_no = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
				    			if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
				    			data = agmt_no;
				    		}
					    	else if (cell.getColumnIndex() == 4) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
				    			if (!agmt_rev.equals("null")) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    			data = agmt_rev;
				    		}	
							
					    	else if(cell.getColumnIndex() == 11) {
					    		name = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
//					    		if (name != null) {
					    		if (!name.equals("null")) {
									name = name.substring(1, name.length() - 1);
								}
								query_String = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) = ? ";
						    	stmt = conn.prepareStatement(query_String);
						    	stmt.setString(1, name);
						    	rset = stmt.executeQuery();
						    	if (rset.next()) {
						    		int_cd = rset.getString(1);
						    	}else {
						    		int_cd = "null";
						    	}
						    	rset.close();
						    	stmt.close();
						    	data = int_cd;
					    	}
					    	else if(cell.getColumnIndex() == 12) {
					    		name = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
//					    		if (name != null) {
					    		if(!name.equals("null")) {
									name = name.substring(1, name.length() - 1);
								}
					    		if(name.equals("0")) {
					    			name = "null";
					    		}
					    		data = name;
					    	}
							
					    	else if(cell.getColumnIndex() == 14) {
					    		name = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
//					    		if (name != null) {
					    		if (!name.equals("null")) {	
					    		name = name.substring(1, name.length() - 1);
								}
								String queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
						    	stmt = conn.prepareStatement(queryString);
						    	stmt.setString(1, name);
						    	rset = stmt.executeQuery();
						    	if (rset.next()) {
						    		exchg_cd = rset.getString(1);
						    	}
						    	rset.close();
						    	stmt.close();
						    	data = exchg_cd;
					    	}
							
					    	else if (cell.getColumnIndex() == 27) { //PLANT_SEQ_NO
					    		
					    		query_String = "SELECT PLANT_SEQ_NO FROM FMS_AGMT_PLANT WHERE COUNTERPARTY_CD = ? "
					    				+ " AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, agmt_no);
					    		stmt.setString(3, agmt_rev);
					    		stmt.setString(4, agmt_type);
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			seq_no= rset.getString(1);
					    		} else {
					    			seq_no = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = seq_no;
					    		
					    	}
					    	else if (cell.getColumnIndex() == 28) { //holiday_state
						    		query_String = "SELECT B.STATE_CODE FROM  FMS_COUNTERPARTY_PLANT_DTL A,FMS_STATE_MST B WHERE A.COUNTERPARTY_CD = ? AND B.STATE_NM = A.PLANT_STATE ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, cd);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			state_code=rset.getString(1);
						    		}
						    		else
						    		{
						    			state_code="null";
						    		}
						    		rset.close();
						    		stmt.close();
						    		data = state_code;
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
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
					   
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_type=rset.getString(3);
						        agmt_no=rset.getString(4);
						        agmt_rev=rset.getString(5);
						       
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_type);
							    st_del.setString(3, agmt_no);
							    st_del.setString(4, agmt_rev);
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_AGMT_BILLING_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_type=rset.getString(3);
			        agmt_no=rset.getString(4);
			        agmt_rev=rset.getString(5);
			        
			        
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_AGMT_BILLING_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_AGMT_BILLING_DTL()>>,,,,", conn);
			
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
	
	//FMS_AGMT_DEACTIVATION_DTL
	public void FMS_AGMT_DEACTIVATION_DTL() throws SQLException, IOException {
		function_nm = "FMS_AGMT_DEACTIVATION_DTL()";
		try {
			column=" ";
			logger_count=0;
			int bn=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_AGMT_DEACTIVATION_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_AGMT_DEACTIVATION_DTL>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_AGMT_DEACTIVATION_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND SEQ_NO = ? ";

			String  agmt_type, agmt_no, agmt_rev, seq_no;
			
		
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,SEQ_NO,FROM_DT,TO_DT,STATUS,ENT_BY,ENT_DT,FLAG,REMARK ";
				
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_DEACTIVATION_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_DEACTIVATION_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, SEQ_NO FROM FMS_AGMT_DEACTIVATION_DTL WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						agmt_type=""; agmt_no=""; agmt_rev=""; seq_no="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
//					    		abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
//					    		abbr = abbr.substring(1, abbr.length()-1);
								abbr = data;
					    		   data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		
					    		
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = cd;
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
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
					   
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_type=rset.getString(3);
						        agmt_no=rset.getString(4);
						        agmt_rev=rset.getString(5);
						        seq_no=rset.getString(6);
						       
						       
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_type);
							    st_del.setString(3, agmt_no);
							    st_del.setString(4, agmt_rev);
							    st_del.setString(5, seq_no);
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+seq_no+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, SEQ_NO FROM FMS_AGMT_DEACTIVATION_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_type=rset.getString(3);
			        agmt_no=rset.getString(4);
			        agmt_rev=rset.getString(5);
			        seq_no=rset.getString(5);
			        
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_AGMT_DEACTIVATION_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_AGMT_DEACTIVATION_DTL()>>,,,,,", conn);
			
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
	
	//FMS_SUPPLY_CONT_MST
	public void FMS_SUPPLY_CONT_MST () throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_CONT_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_CONT_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_CONT_MST>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO =? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";

			String  agmt_no, agmt_rev, cont_no, cont_rev, cont_type;
			String cont_ref,a_base,a_typ,a_type,spec_gas;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_REV,CONTRACT_TYPE,CONT_REF_NO,"
					+ "TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,"
					+ "RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,"
					+ "BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,"
					+ "DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,"
					+ "MODIFY_BY,CONT_STATUS,IS_ALLOCATED,DDA_DT,DDA_TIME,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,TXN_CHARGE,BUYER_NOM_CUTOFF,"
					+ "TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_CLOSE,TCQ_REQUEST_QTY,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,"
					+ "CHANGE_DATE_REQ,MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
					+ "OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,"
					+ "LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,SF_GEN_DT,"
					+ "CLOSURE_REQUEST_FLAG,CLOSE_EFF_DT,CLOSURE_ALLOC_QTY";
			
			
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
					    agmt_no="0"; agmt_rev="0"; cont_no="";cont_rev="";cont_type="";
					    cont_ref="";a_base="";a_typ="";a_type="";spec_gas="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		cont_ref = data;					    		
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							
					    	else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null") && (cont_ref.startsWith("L") || cont_ref.startsWith("X"))) {
									agmt_no = "0";
								}else if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}				    						    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_rev.equals("null") && (cont_ref.startsWith("L") || cont_ref.startsWith("X"))) {
					    			agmt_rev = "0";
								}
					    		else if (!agmt_rev.equals("null")) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}				    		
								data = agmt_rev;
					    	}
							
				    	  else if(cell.getColumnIndex() == 4) {
				    		  data = null;
				    	  }
					    	
				    	  else if (cell.getColumnIndex() == 6) { //Cont_type
					    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!cont_type.equals("null")) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
					    		data = cont_type;
					    	}
					    	else if(cell.getColumnIndex() == 7 ){   //	CONT_NAME
					    		data = null;
					    	}
					    	else if(cell.getColumnIndex() == 14) {
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			a_base = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    			if (!a_base.equals("null")) {
										a_base = a_base.substring(1, a_base.length() - 1);
									}
					    		}else if(cont_type.equals("S")){
						    		query_String = "SELECT AGMT_BASE,AGMT_TYP FROM FMS_AGMT_MST WHERE COUNTERPARTY_CD = ? AND AGMT_TYPE = 'F' AND AGMT_NO = ? AND AGMT_REV = ?";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			a_base= rset.getString(1);
						    			a_typ= rset.getString(2);
						    		} else {
						    			a_base= "null";
						    			a_typ= "null";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		data = a_base;
					    	}
					    	else if(cell.getColumnIndex() == 15) {
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			a_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    			if (!a_type.equals("null")) {
										a_type = a_type.substring(1, a_type.length() - 1);
									}
					    		}else if(cont_type.equals("S")){
					    			a_type = a_typ;
					    		}
					    		data = a_type;
					    	}
					    	
					    	else if(cell.getColumnIndex() == 71) {
					    		
					    		data = data.substring(1,data.length()-1);
					    		if(data != null) {
					    		spec_gas = data;
//					    		spec_gas = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
//					    		spec_gas = spec_gas.substring(1, spec_gas.length()-1);
					    		spec_gas = ("0".equals(spec_gas)) ? "0" : (("1".equals(spec_gas)) ? "GCV" : (("2".equals(spec_gas)) ? "NCV" : "null"));
					    		data = spec_gas;
					    		}
					    		else {
					    			data = "null";
					    		}
					    	}
//							System.out.println(i+" ::Data:: "+data);
							
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
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_no=rset.getString(3);
						        agmt_rev=rset.getString(4);
						        cont_no = rset.getString(5);
						        cont_rev = rset.getString(6);
						        cont_type = rset.getString(7);
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_no);
							    st_del.setString(3, agmt_rev);
							    st_del.setString(4, cont_no);
							    st_del.setString(5, cont_rev);
							    st_del.setString(6, cont_type);
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_no=rset.getString(3);
			        agmt_rev=rset.getString(4);
			        cont_no = rset.getString(5);
			        cont_rev = rset.getString(6);
			        cont_type = rset.getString(7);
			        
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_CONT_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_CONT_MST()>>,,,,,,", conn);
			
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
	
	//FMS_SUPPLY_CONT_PLANT
	public void FMS_SUPPLY_CONT_PLANT () throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_CONT_PLANT()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_CONT_PLANT()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_CONT_PLANT>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SUPPLY_CONT_PLANT WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO =? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ?";

			String  agmt_no, agmt_rev, cont_no, cont_rev, cont_type,plant_seq_no,cont_ref;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "PLANT_SEQ_NO,ENT_BY,ENT_DT,TRANSPORTATION_CHARGE,MARKET_MARGIN,OTHER_CHARGES";
				
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_PLANT WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
					    agmt_no=""; agmt_rev=""; cont_no="";cont_rev="";cont_type="";plant_seq_no="";cont_ref="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							
					    	else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
							
					    	else if (cell.getColumnIndex() == 6) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
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
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_no=rset.getString(3);
						        agmt_rev=rset.getString(4);
						        cont_no = rset.getString(5);
						        cont_rev = rset.getString(6);
						        cont_type = rset.getString(7);
						        plant_seq_no = rset.getString(8);
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_no);
							    st_del.setString(3, agmt_rev);
							    st_del.setString(4, cont_no);
							    st_del.setString(5, cont_rev);
							    st_del.setString(6, cont_type);
							    st_del.setString(7, plant_seq_no);
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+plant_seq_no+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_PLANT WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_no=rset.getString(3);
			        agmt_rev=rset.getString(4);
			        cont_no = rset.getString(5);
			        cont_rev = rset.getString(6);
			        cont_type = rset.getString(7);
			        plant_seq_no = rset.getString(8);
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+plant_seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_CONT_PLANT()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_CONT_PLANT()>>,,,,,,,", conn);
			
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
	
	//FMS_SUPPLY_AGMT_LIABILITY
	public void FMS_SUPPLY_AGMT_LIABILITY () throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_AGMT_LIABILITY()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_AGMT_LIABILITY()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_AGMT_LIABILITY>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SUPPLY_AGMT_LIABILITY WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ?";

			String  agmt_type, agmt_no, agmt_rev;
			String ld_promise, top_promise;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,"
					+ "LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,"
					+ "TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,"
					+ "MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT";
			//System.out.println("::column_length::"+column.split(",").length);
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_AGMT_LIABILITY_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_AGMT_LIABILITY_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD,AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_SUPPLY_AGMT_LIABILITY WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
					    agmt_no=""; agmt_rev=""; agmt_type="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							
//					    	
					    	else if(cell.getColumnIndex() == 7) {
					    		ld_promise = data;
					    		
					    		if("Daily".equals(ld_promise)) {
					    			ld_promise = "D";
					    		}
					    		else if("Weekly".equals(ld_promise)) {
					    			ld_promise = "W";
					    		}
					    		else if("Fortnightly".equals(ld_promise)) {
					    			ld_promise = "F";
					    		}
					    		else if("Monthly".equals(ld_promise)) {
					    			ld_promise = "M";
					    		}
					    		else if("Quarterly".equals(ld_promise)) {
					    			ld_promise = "Q";
					    		}
					    		else if("Invoice Cycle".equals(ld_promise)) {
					    			ld_promise = "IC";
					    		}
					    		else if("TCQ".equals(ld_promise)) {
					    			ld_promise = "T";
					    		}
					    		else if("Defined Period".equals(ld_promise)) {
					    			ld_promise = "DP";
					    		}
					    		else if("Supply Period".equals(ld_promise)) {
					    			ld_promise = "SP";
					    		}else {
					    			ld_promise = "null";
					    		}
					    		
					    		data = ld_promise;
					    	}
							
					    	else if(cell.getColumnIndex() == 13) {
					    		top_promise = data;
					    		
					    		if("Daily".equals(top_promise)) {
					    			top_promise = "D";
					    		}
					    		else if("Weekly".equals(top_promise)) {
					    			top_promise = "W";
					    		}
					    		else if("Fortnightly".equals(top_promise)) {
					    			top_promise = "F";
					    		}
					    		else if("Monthly".equals(top_promise)) {
					    			top_promise = "M";
					    		}
					    		else if("Quarterly".equals(top_promise)) {
					    			top_promise = "Q";
					    		}
					    		else if("Invoice Cycle".equals(top_promise)) {
					    			top_promise = "IC";
					    		}
					    		else if("TCQ".equals(top_promise)) {
					    			top_promise = "T";
					    		}
					    		else if("Defined Period".equals(top_promise)) {
					    			top_promise = "DP";
					    		}
					    		else if("Supply Period".equals(top_promise)) {
					    			top_promise = "SP";
					    		}
					    		else {
					    			top_promise = "null";
					    		}
					    		
					    		data = top_promise;
					    	}
					         
					    	
//							 System.out.println(i+"::>>data>>:: "+data);
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
//								   System.out.println(i+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_type=rset.getString(3);
						        agmt_no=rset.getString(4);
						        agmt_rev=rset.getString(5);
						       
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_type);
							    st_del.setString(3, agmt_no);
							    st_del.setString(4, agmt_rev);
							   
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD,AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_SUPPLY_AGMT_LIABILITY WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_type=rset.getString(3);
			        agmt_no=rset.getString(4);
			        agmt_rev=rset.getString(5);
			       
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_AGMT_LIABILITY()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_AGMT_LIABILITY()>>,,,,", conn);
			
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
	
	
	//FMS_SUPPLY_CONT_BU
		public void FMS_SUPPLY_CONT_BU () throws SQLException, IOException {
			function_nm = "FMS_SUPPLY_CONT_BU()";
			try {
				column=" ";
				logger_count=0;
				
				System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_CONT_BU()>>");
				
				logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_CONT_BU>>,,,,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

				query_delete = "DELETE FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO =? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ?";

				String  agmt_no, agmt_rev, cont_no, cont_rev, cont_type,plant_seq_no,cont_ref;
				
				column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
						+ "PLANT_SEQ_NO,ENT_BY,ENT_DT";
					
				data1 = new String[column.split(",").length]; 
				file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_BU_"+start_end_dt+".xlsx");
				if (file1.exists()) 
				{
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_BU_"+start_end_dt+".xlsx"));
					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					rowIterator = sheet.iterator();
					rowIterator.next();

					logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO, TIMESTAMP", conn);
					while (rowIterator.hasNext())
					{
							query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD='2' AND ";
							row = rowIterator.next();
							cellIterator = row.cellIterator();
							
							data = "";cd="";
							int i = 0;
						    agmt_no=""; agmt_rev=""; cont_no="";cont_rev="";cont_type="";plant_seq_no="";cont_ref="";
							while (cellIterator.hasNext()) 
							{
								cell = cellIterator.next();
								data = cell.getStringCellValue();
								data = data.substring(1, data.length() - 1);			

								if (cell.getColumnIndex() == 0) {	// Company_cd 
									abbr = data;
						    	    data = company_cd;
						    	}
						    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
						    		cont_ref = data;
						    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, abbr);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			cd = rset.getString(1);
						    		} else {
						    			cd = "";
						    		}
						    		rset.close();
						    		stmt.close();
						    		data = cd;
						    	}
								
						    	else if (cell.getColumnIndex() == 2) { //Agmt_no
						    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_no.equals("null")) {
										agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
									}
						    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
						    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
							    		stmt = conn.prepareStatement(query_String);
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
						    		}
						    		
									data = agmt_no;
						    	}
								
						    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
							    		if (!agmt_rev.equals("null")) {
											agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
										}
						    		}
									data = agmt_rev;
						    	}
								
						    	else if (cell.getColumnIndex() == 4) { //Cont_no
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
							    		if (!cont_no.equals("null")) {
							    			cont_no = cont_no.substring(1, cont_no.length() - 1);
										}
						    		}
						    		data = cont_no;
						    	}
					    		
						    	else if (cell.getColumnIndex() == 5) { //Cont_rev
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
							    		if (!cont_rev.equals("null")) {
							    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
										}	
						    		}
						    		data = cont_rev;
						    	}
								
						    	else if (cell.getColumnIndex() == 6) { //contract_type
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
							    		if (!cont_type.equals("null")){
							    			cont_type = cont_type.substring(1, cont_type.length() - 1);
										}	
						    		}
						    		data = cont_type;
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
							}
							//while(column) completed
							
							query_select = query_select.substring(0, query_select.length() - 4);
				            if(!cd.equals("")) 
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
							        cd=rset.getString(2);
							        agmt_no=rset.getString(3);
							        agmt_rev=rset.getString(4);
							        cont_no = rset.getString(5);
							        cont_rev = rset.getString(6);
							        cont_type = rset.getString(7);
							        plant_seq_no = rset.getString(8);
							        
								    st_del = conn.prepareStatement(query_delete);
								    st_del.setString(1, cd);
								    st_del.setString(2, agmt_no);
								    st_del.setString(3, agmt_rev);
								    st_del.setString(4, cont_no);
								    st_del.setString(5, cont_rev);
								    st_del.setString(6, cont_type);
								    st_del.setString(7, plant_seq_no);
								   
								    st_del.executeUpdate();
								  
								    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+plant_seq_no+","), conn, "");
								    logger_count++;   
								  
								    st_del.close();
							      }

							     rset.close();
							     stmt.close();
				            }
					}//while(row) completed
					
					// Data left
					query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' ";
					stmt = conn.prepareStatement(query_data_left);
					
					rset = stmt.executeQuery();
					while (rset.next()) {
						company_cd=rset.getString(1);
				        cd=rset.getString(2);
				        agmt_no=rset.getString(3);
				        agmt_rev=rset.getString(4);
				        cont_no = rset.getString(5);
				        cont_rev = rset.getString(6);
				        cont_type = rset.getString(7);
				        plant_seq_no = rset.getString(8);
				      
						
					    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+plant_seq_no+","), conn, "N");
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
				System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_CONT_BU()>>");
				
				
				logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
				logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_CONT_BU()>>,,,,,,,", conn);
				
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
	
//FMS_DAILY_BUYER_NOM
	public void FMS_DAILY_BUYER_NOM () throws SQLException, IOException {
		int n=0;
		function_nm = "FMS_DAILY_BUYER_NOM()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_DAILY_BUYER_NOM()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_DAILY_BUYER_NOM>>,,,,,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_DAILY_BUYER_NOM WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ?  AND CONT_NO =? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CONTRACT_TYPE = ? AND CARGO_NO = ?";

			String  agmt_no, agmt_rev, cont_no, cont_rev, cont_type, nom_rev_no,plant_seq;
			String trans_cd, gas_dt, trans_seq, bu_seq,cargo_no,cont_ref;
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,"
					+ "GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO";
				
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".csv");
			if (file1.exists()) 
			{
//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
//				rowIterator = sheet.iterator();
//				rowIterator.next();

				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					String line = br.readLine();
					

					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,PLANT_SEQ,CONTRACT_TYPE,TIMESTAMP,", conn);

					while ((line = br.readLine()) != null)
					{
							query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ, TO_CHAR(GAS_DT,'DD/MM/YYYY hh24:mi:ss'),CONTRACT_TYPE,CARGO_NO FROM FMS_DAILY_BUYER_NOM WHERE COMPANY_CD='2' AND ";
//						row = rowIterator.next();
//						cellIterator = row.cellIterator();
							
							data = "";cd="";
							int i = 0;
						    agmt_no=""; agmt_rev=""; cont_no="";cont_rev="";cont_type="";nom_rev_no="";plant_seq="";
						    trans_cd=""; gas_dt=""; trans_seq=""; bu_seq="";cargo_no="";cont_ref="";
						    
//						while (cellIterator.hasNext()) 
						    for (int x = 0; x < line.split(",").length; x++)
							{
//							cell = cellIterator.next();
								data = line.split(",")[x];			
//							System.out.println(x+"==="+data);
//							data = data.substring(1, data.length() - 1);
								if (x == 0) {	// Company_cd 
									abbr = data;
						    	    data = company_cd;
						    	}
						    	else if (x == 1) {	// Counterparty_Cd
						    		cont_ref = data;
						    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, abbr);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			cd = rset.getString(1);
						    		} else {
						    			cd = "";
						    		}
						    		rset.close();
						    		stmt.close();
						    		data = cd;
						    	}
									
						    	else if (x == 2) { //Agmt_no
						    		agmt_no = (line.split(",")[x].contains("null") ? "null" : line.split(",")[x]);
//					    		if (!agmt_no.equals("null")) {
//									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
//								}
						    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
						    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
							    		stmt = conn.prepareStatement(query_String);
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
						    		}
						    		
									data = agmt_no;
						    	}
						    	else if (x == 3) { //Agmt_rev
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		agmt_rev = (line.split(",")[x].contains("null") ? "null" : line.split(",")[x]);
//						    		if (!agmt_rev.equals("null")) {
//										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
//									}
						    		}
									data = agmt_rev;
						    	}
						    	else if (x == 4) { //Cont_no
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_no = (line.split(",")[x].contains("null") ? "null" : line.split(",")[x]);
//						    		if (!cont_no.equals("null")) {
//						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
//									}
						    		}
						    		data = cont_no;
						    	}
					    		
						    	else if (x == 5) { //Cont_rev
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_rev = (line.split(",")[x].contains("null") ? "null" : line.split(",")[x]);
//						    		if (!cont_rev.equals("null")) {
//						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//									}	
						    		}
						    		data = cont_rev;
						    	}
						    	else if (x == 12) { //contract_type
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_type = (line.split(",")[x].contains("null") ? "null" : line.split(",")[x]);
//						    		if (!cont_type.equals("null")) {
//						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
//									}	
						    		}
						    		data = cont_type;
						    	}
								
						    	
						    	else if (x == 8) {	// trans_cd
						    		trans_cd = (line.split(",")[x].contains("'null'") ? "null" : line.split(",")[x]);
//					    		if (!trans_cd.equals("null")) {
//									trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
//								}
									query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, trans_cd);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			trans_cd = rset.getString(1);
						    		}else {
						    			trans_cd = "";
						    		}
						    		rset.close();
						    		stmt.close();
						    		data = trans_cd;
					    		}
						    	
						    	else if (x == 9) {	// trans_seq
									trans_seq = (line.split(",")[x].contains("'null'") ? "null" : line.split(",")[x]);
									if(!trans_seq.equals("null")) {
//									trans_seq = trans_seq.substring(1, trans_seq.length()-1);
										
										query_String = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ?";
										stmt = conn.prepareStatement(query_String);
										stmt.setString(1, trans_seq);
										rset = stmt.executeQuery();
										
										if (rset.next()) {
											trans_seq = rset.getString(1);
										}
										else{
											trans_seq="";
											n++;
//										System.out.println(n + " " + trans_cd);
										}
										rset.close();
										stmt.close();
							    	}
									data=trans_seq;
								}
						    	
						    	else if(x == 10) { // BU_SEQ
						    		query_String = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq = rset.getString(1);
						    		}
						    		else if(!rset.next()){
						    			bu_seq = line.split(",")[x].contains("null") ? "null" : line.split(",")[x];
//					    			if (!bu_seq.equals("null")) {
//										bu_seq = bu_seq.substring(1, bu_seq.length() - 1);
//									}
						    		}
						    		rset.close();
						    		stmt.close();	
						    		data  = bu_seq;
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
							}
							//while(column) completed
							
							query_select = query_select.substring(0, query_select.length() - 4);
					        if(!cd.equals("")) 
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
							        cd=rset.getString(2);
							        agmt_no=rset.getString(3);
							        agmt_rev=rset.getString(4);
							        cont_no = rset.getString(5);
							        cont_rev = rset.getString(6);
							        nom_rev_no = rset.getString(7);
							        plant_seq =rset.getString(8);
							        trans_cd = rset.getString(9);
							        trans_seq = rset.getString(10);
							        bu_seq = rset.getString(11);
							        gas_dt = rset.getString(12);
							        cont_type = rset.getString(13);
							        cargo_no = rset.getString(14);
							        
								    st_del = conn.prepareStatement(query_delete);
								    st_del.setString(1, cd);
								    st_del.setString(2, agmt_no);
								    st_del.setString(3, agmt_rev);
								    st_del.setString(4, cont_no);
								    st_del.setString(5, cont_rev);
								    st_del.setString(6, nom_rev_no);
								    st_del.setString(7, plant_seq);
								    st_del.setString(8, trans_cd);
								    st_del.setString(9, trans_seq);
								    st_del.setString(10, bu_seq);
								    st_del.setString(11, gas_dt);
								    st_del.setString(12, cont_type);
								    st_del.setString(13, cargo_no);


								    st_del.executeUpdate();
								  
									logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + plant_seq+ "," + nom_rev_no + ","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+cont_type+","+cargo_no+","), conn, "");

								    logger_count++;   
								  
								    st_del.close();
							      }

							     rset.close();
							     stmt.close();
					        }
					}//while(row) completed
					br.close();
				}
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,TO_CHAR(GAS_DT,'DD/MM/YYYY hh24:mi:ss'),CONTRACT_TYPE,CARGO_NO FROM FMS_DAILY_BUYER_NOM WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_no=rset.getString(3);
			        agmt_rev=rset.getString(4);
			        cont_no = rset.getString(5);
			        cont_rev = rset.getString(6);
			        nom_rev_no = rset.getString(7);
			        plant_seq =rset.getString(8);
			        trans_cd = rset.getString(9);
			        trans_seq = rset.getString(10);
			        bu_seq = rset.getString(11);
			        gas_dt = rset.getString(12);
			        cont_type = rset.getString(13);
			        cargo_no = rset.getString(14);
			      
					
				logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + plant_seq+ "," + nom_rev_no + ","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+cont_type+","+cargo_no+","), conn, "N");

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
			System.out.println("<<ROLLBACK_END>><<FMS_DAILY_BUYER_NOM()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_DAILY_BUYER_NOM()>>,,,,,,,,,,,", conn);
			
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

//FMS_DAILY_BUYER_NOM_DTL	
	public void FMS_DAILY_BUYER_NOM_DTL () throws SQLException, IOException {
		function_nm = "FMS_DAILY_BUYER_NOM_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_DAILY_BUYER_NOM_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_DAILY_BUYER_NOM_DTL>>,,,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_DAILY_BUYER_NOM_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ?  AND CONT_NO =? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CONTRACT_TYPE = ? AND SEQ_NO = ? AND CARGO_NO = ?";

			String  agmt_no, agmt_rev, cont_no, cont_rev, cont_type, nom_rev_no,plant_seq;
			String seq_no,trans_cd, gas_dt, trans_seq, bu_seq,cargo_no,cont_ref;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,"
					+ "GAS_DT,CONTRACT_TYPE,SEQ_NO,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,CT_REF,UTR_NO,ENT_BY,ENT_DT,CARGO_NO";

			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_DTL_"+start_end_dt+".csv");
			if (file1.exists()) 
			{
//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_DTL_"+start_end_dt+".xlsx"));
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
//				rowIterator = sheet.iterator();
//				rowIterator.next();


				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					String line = br.readLine();

					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,SEQ_NO, CARGO_NO, TIMESTAMP,", conn);

					while ((line = br.readLine()) != null)
					{
							query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ, TO_CHAR(GAS_DT,'DD/MM/YYYY hh24:mi:ss'),CONTRACT_TYPE,SEQ_NO,CARGO_NO FROM FMS_DAILY_BUYER_NOM_DTL WHERE COMPANY_CD='2' AND ";
//						row = rowIterator.next();
//						cellIterator = row.cellIterator();
							
							data = "";cd="";
							int i = 0;
						    agmt_no=""; agmt_rev=""; cont_no="";cont_rev="";cont_type="";nom_rev_no="";plant_seq="";
						    seq_no="";trans_cd=""; gas_dt=""; trans_seq=""; bu_seq="";cargo_no="";cont_ref="";
						    
//						while (cellIterator.hasNext()) 
						    for (int x = 0; x < line.split(",").length; x++)
							{
//							cell = cellIterator.next();
								data = line.split(",")[x];
//							data = data.substring(1, data.length() - 1);			

								if (x == 0) {	// Company_cd 
									abbr = data;
						    	    data = company_cd;
						    	}
						    	else if (x == 1) {	// Counterparty_Cd
						    		cont_ref = data;
						    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, abbr);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			cd = rset.getString(1);
						    		} else {
						    			cd = "";
						    		}
						    		rset.close();
						    		stmt.close();
						    		data = cd;
						    	}
								
						    	else if (x == 2) { //Agmt_no
						    		agmt_no = (line.split(",")[x].contains("null") ? "null" : line.split(",")[x]);
//					    		if (!agmt_no.equals("null")) {
//									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
//								}
						    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
						    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
							    		stmt = conn.prepareStatement(query_String);
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
						    		}
						    		
									data = agmt_no;
						    	}
						    	else if (x == 3) { //Agmt_rev
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		agmt_rev = (line.split(",")[x].contains("null") ? "null" : line.split(",")[x]);
//						    		if (!agmt_rev.equals("null")) {
//										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
//									}
						    		}
									data = agmt_rev;
						    	}
						    	else if (x == 4) { //Cont_no
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_no = (line.split(",")[x].contains("null") ? "null" : line.split(",")[x]);
//						    		if (!cont_no.equals("null")) {
//						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
//									}
						    		}
						    		data = cont_no;
						    	}
					    		
						    	else if (x == 5) { //Cont_rev
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_rev = (line.split(",")[x].contains("null") ? "null" : line.split(",")[x]);
//						    		if (!cont_rev.equals("null")) {
//						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//									}	
						    		}
						    		data = cont_rev;
						    	}
						    	else if (x == 12) { //contract_type
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_type = (line.split(",")[x].contains("null") ? "null" : line.split(",")[x]);
//						    		if (!cont_type.equals("null")) {
//						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
//									}	
						    		}
						    		data = cont_type;
						    	}
						    	
						    	else if (x == 8) {	// trans_cd
						    		trans_cd = (line.split(",")[x].contains("'null'") ? "null" : line.split(",")[x]);
//					    		if (!trans_cd.equals("null")) {
//									trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
//								}
									query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, trans_cd);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			trans_cd = rset.getString(1);
						    		}else {
						    			trans_cd = "";
						    		}
						    		rset.close();
						    		stmt.close();
						    		data = trans_cd;
					    		}
						    	
						    	else if (x == 9) {	// trans_seq
									trans_seq = (line.split(",")[x].contains("'null'") ? "null" : line.split(",")[x]);
									if(!trans_seq.equals("null")) {
										trans_seq = trans_seq.substring(1, trans_seq.length()-1);
										
										query_String = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ?";
										stmt = conn.prepareStatement(query_String);
										stmt.setString(1, trans_seq);
										rset = stmt.executeQuery();
										
										if (rset.next()) {
											trans_seq = rset.getString(1);
										}
										else{
											trans_seq = "";
										}
										rset.close();
										stmt.close();
							    	}
									data=trans_seq;
								}
						    	
						    	else if(x == 10) { // BU_SEQ
						    		query_String = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq = rset.getString(1);
						    		}
						    		else if(!rset.next()){
						    			bu_seq= line.split(",")[x].contains("null") ? "null" : line.split(",")[x];
//					    			if (!bu_seq.equals("null")) {
//										bu_seq = bu_seq.substring(1, bu_seq.length() - 1);
//									}
						    		}
						    		rset.close();
						    		stmt.close();	
						    		data  = bu_seq;
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
							}
							//while(column) completed
							
							query_select = query_select.substring(0, query_select.length() - 4);
					        if(!cd.equals("")) 
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
							        cd=rset.getString(2);
							        agmt_no=rset.getString(3);
							        agmt_rev=rset.getString(4);
							        cont_no = rset.getString(5);
							        cont_rev = rset.getString(6);
							        nom_rev_no = rset.getString(7);
							        plant_seq =rset.getString(8);
							        trans_cd = rset.getString(9);
							        trans_seq = rset.getString(10);
							        bu_seq = rset.getString(11);
							        gas_dt = rset.getString(12);
							        cont_type = rset.getString(13);
							        seq_no = rset.getString(14);
							        cargo_no = rset.getString(15);
							        
								    st_del = conn.prepareStatement(query_delete);
								    st_del.setString(1, cd);
								    st_del.setString(2, agmt_no);
								    st_del.setString(3, agmt_rev);
								    st_del.setString(4, cont_no);
								    st_del.setString(5, cont_rev);
								    st_del.setString(6, nom_rev_no);
								    st_del.setString(7, plant_seq);
								    st_del.setString(8, trans_cd);
								    st_del.setString(9, trans_seq);
								    st_del.setString(10, bu_seq);
								    st_del.setString(11, gas_dt);
								    st_del.setString(12, cont_type);
								    st_del.setString(13, seq_no);
								    st_del.setString(14, cargo_no);


								    st_del.executeUpdate();
								  
									logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + plant_seq+ "," + nom_rev_no + ","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+cont_type+","+seq_no+","+cargo_no+","), conn, "");

								    logger_count++;   
								  
								    st_del.close();
							      }

							     rset.close();
							     stmt.close();
					        }
					}//while(row) completed
					br.close();
				}
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ, TO_CHAR(GAS_DT,'DD/MM/YYYY hh24:mi:ss'),CONTRACT_TYPE,SEQ_NO,CARGO_NO FROM FMS_DAILY_BUYER_NOM_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_no=rset.getString(3);
			        agmt_rev=rset.getString(4);
			        cont_no = rset.getString(5);
			        cont_rev = rset.getString(6);
			        nom_rev_no = rset.getString(7);
			        plant_seq =rset.getString(8);
			        trans_cd = rset.getString(9);
			        trans_seq = rset.getString(10);
			        bu_seq = rset.getString(11);
			        gas_dt = rset.getString(12);
			        cont_type = rset.getString(13);
			        seq_no = rset.getString(14);
			        cargo_no = rset.getString(15);
			      
					
					logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + plant_seq+ "," + nom_rev_no + ","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+cont_type+","+seq_no+","+cargo_no+","), conn, "N");

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
			System.out.println("<<ROLLBACK_END>><<FMS_DAILY_BUYER_NOM_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_DAILY_BUYER_NOM_DTL()>>,,,,,,,,,", conn);
			
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
	
//FMS_DAILY_SELLER_NOM
	public void FMS_DAILY_SELLER_NOM () throws SQLException, IOException {
		function_nm = "FMS_DAILY_SELLER_NOM()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_DAILY_SELLER_NOM()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_DAILY_SELLER_NOM>>,,,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_DAILY_SELLER_NOM WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ?  AND CONT_NO =? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CONTRACT_TYPE = ? AND CARGO_NO = ?";

			String  agmt_no, agmt_rev, cont_no, cont_rev, cont_type, nom_rev_no,plant_seq;
			String trans_cd, gas_dt, trans_seq, bu_seq,cargo_no,cont_ref;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,"
					+ "GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO";
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_SELLER_NOM_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_SELLER_NOM_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,CARGO_NO, TIMESTAMP,", conn);

				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ, TO_CHAR(GAS_DT,'DD/MM/YYYY hh24:mi:ss'),CONTRACT_TYPE,CARGO_NO FROM FMS_DAILY_SELLER_NOM WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
					    agmt_no=""; agmt_rev=""; cont_no="";cont_rev="";cont_type="";nom_rev_no="";plant_seq="";
					    trans_cd=""; gas_dt=""; trans_seq=""; bu_seq="";cargo_no="";cont_ref="";
					    
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
	
					    	else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
							

					    	else if (cell.getColumnIndex() == 12) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
					    	}
					    	
					    	else if (cell.getColumnIndex() == 8) {	// trans_cd
					    		trans_cd = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!trans_cd.equals("null")) {
									trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
								}
								query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, trans_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		}else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = trans_cd;
				    		}
					    	
					    	else if (cell.getColumnIndex() == 9) {	// trans_seq
								trans_seq = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
								if(!trans_seq.equals("null")) {
									trans_seq = trans_seq.substring(1, trans_seq.length()-1);
									
									query_String = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ?";
									stmt = conn.prepareStatement(query_String);
									stmt.setString(1, trans_seq);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										trans_seq = rset.getString(1);
									}
									else{
										trans_seq = "";
									}
									rset.close();
									stmt.close();
						    	}
								data=trans_seq;
							}
					    	
					    	else if(cell.getColumnIndex() == 10) { // BU_SEQ
					    		query_String = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
					    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, agmt_no);
					    		stmt.setString(3, agmt_rev);
					    		stmt.setString(4, cont_no);
					    		stmt.setString(5, cont_rev);
					    		stmt.setString(6, cont_type);
					    		rset = stmt.executeQuery();
					    		
					    		if (rset.next()) {
					    			bu_seq = rset.getString(1);
					    		}
					    		else if(!rset.next()){
					    			bu_seq = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
					    			if (!bu_seq.equals("null")) {
										bu_seq = bu_seq.substring(1, bu_seq.length() - 1);
									}
					    		}
					    		rset.close();
					    		stmt.close();	
					    		data  = bu_seq;
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
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_no=rset.getString(3);
						        agmt_rev=rset.getString(4);
						        cont_no = rset.getString(5);
						        cont_rev = rset.getString(6);
						        nom_rev_no = rset.getString(7);
						        plant_seq =rset.getString(8);
						        trans_cd = rset.getString(9);
						        trans_seq = rset.getString(10);
						        bu_seq = rset.getString(11);
						        gas_dt = rset.getString(12);
						        cont_type = rset.getString(13);
						        cargo_no = rset.getString(14);
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_no);
							    st_del.setString(3, agmt_rev);
							    st_del.setString(4, cont_no);
							    st_del.setString(5, cont_rev);
							    st_del.setString(6, nom_rev_no);
							    st_del.setString(7, plant_seq);
							    st_del.setString(8, trans_cd);
							    st_del.setString(9, trans_seq);
							    st_del.setString(10, bu_seq);
							    st_del.setString(11, gas_dt);
							    st_del.setString(12, cont_type);
							    st_del.setString(13, cargo_no);


							    st_del.executeUpdate();
							  
								logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + plant_seq+ "," + nom_rev_no + ","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+cont_type+","+cargo_no+","), conn, "");

							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ, TO_CHAR(GAS_DT,'DD/MM/YYYY hh24:mi:ss'),CONTRACT_TYPE,CARGO_NO FROM FMS_DAILY_SELLER_NOM WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_no=rset.getString(3);
			        agmt_rev=rset.getString(4);
			        cont_no = rset.getString(5);
			        cont_rev = rset.getString(6);
			        nom_rev_no = rset.getString(7);
			        plant_seq =rset.getString(8);
			        trans_cd = rset.getString(9);
			        trans_seq = rset.getString(10);
			        bu_seq = rset.getString(11);
			        gas_dt = rset.getString(12);
			        cont_type = rset.getString(13);
			        cargo_no = rset.getString(14);
			      
					
					logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + plant_seq+ "," + nom_rev_no + ","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+cont_type+","+cargo_no+","), conn, "N");

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
			System.out.println("<<ROLLBACK_END>><<FMS_DAILY_SELLER_NOM()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_DAILY_SELLER_NOM()>>,,,,,,,,,", conn);
			
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
	

	//FMS_DAILY_SELLER_NOM
		public void FMS_DAILY_SELLER_NOM_DTL () throws SQLException, IOException {
			function_nm = "FMS_DAILY_SELLER_NOM_DTL()";
			try {
				column=" ";
				logger_count=0;
				
				System.out.println("<<ROLLBACK_START>><<FMS_DAILY_SELLER_NOM_DTL()>>");
				
				logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_DAILY_SELLER_NOM_DTL>>,,,,,,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

				query_delete = "DELETE FROM FMS_DAILY_SELLER_NOM_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ?  AND CONT_NO =? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CONTRACT_TYPE = ?  AND SEQ_NO = ? AND CARGO_NO = ?";

				String  agmt_no, agmt_rev, cont_no, cont_rev, cont_type, nom_rev_no,plant_seq;
				String seq_no, trans_cd, gas_dt, trans_seq, bu_seq,cargo_no;
				
				column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,"
						+ "GAS_DT,CONTRACT_TYPE,SEQ_NO,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,CT_REF,UTR_NO,ENT_BY,ENT_DT,CARGO_NO";

				data1 = new String[column.split(",").length]; 
				file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_SELLER_NOM_DTL_"+start_end_dt+".xlsx");
				if (file1.exists()) 
				{
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_SELLER_NOM_DTL_"+start_end_dt+".xlsx"));
					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					rowIterator = sheet.iterator();
					rowIterator.next();

					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,SEQ_NO,CARGO_NO, TIMESTAMP,", conn);

					while (rowIterator.hasNext())
					{
							query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ, TO_CHAR(GAS_DT,'DD/MM/YYYY hh24:mi:ss'),CONTRACT_TYPE,SEQ_NO,CARGO_NO FROM FMS_DAILY_SELLER_NOM_DTL WHERE COMPANY_CD='2' AND ";
							row = rowIterator.next();
							cellIterator = row.cellIterator();
							
							data = "";cd="";
							int i = 0;
						    agmt_no=""; agmt_rev=""; cont_no="";cont_rev="";cont_type="";nom_rev_no="";plant_seq="";
						    seq_no=""; trans_cd=""; gas_dt=""; trans_seq=""; bu_seq="";cargo_no="";
						    
							while (cellIterator.hasNext()) 
							{
								cell = cellIterator.next();
								data = cell.getStringCellValue();
								data = data.substring(1, data.length() - 1);			

								if (cell.getColumnIndex() == 0) {	// Company_cd 
									abbr = data;
						    	    data = company_cd;
						    	}
						    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
						    		cont_ref = data;
						    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, abbr);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			cd = rset.getString(1);
						    		} else {
						    			cd = "";
						    		}
						    		rset.close();
						    		stmt.close();
						    		data = cd;
						    	}
								
						    	else if (cell.getColumnIndex() == 2) { //Agmt_no
						    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_no.equals("null")){
										agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
									}
						    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
						    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
							    		stmt = conn.prepareStatement(query_String);
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
						    		}
						    		
									data = agmt_no;
						    	}
						    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
							    		if (!agmt_rev.equals("null")) {
											agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
										}
						    		}
									data = agmt_rev;
						    	}
						    	else if (cell.getColumnIndex() == 4) { //Cont_no
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
							    		if (!cont_no.equals("null")) {
							    			cont_no = cont_no.substring(1, cont_no.length() - 1);
										}
						    		}
						    		data = cont_no;
						    	}
					    		
						    	else if (cell.getColumnIndex() == 5) { //Cont_rev
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
							    		if (!cont_rev.equals("null")) {
							    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
										}	
						    		}
						    		data = cont_rev;
						    	}
						    	else if (cell.getColumnIndex() == 12) { //contract_type
						    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
							    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
							    		if (!cont_type.equals("null")) {
							    			cont_type = cont_type.substring(1, cont_type.length() - 1);
										}	
						    		}
						    		data = cont_type;
						    	
						    	}
						    	else if (cell.getColumnIndex() == 8) {	// trans_cd
						    		trans_cd = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!trans_cd.equals("null")) {
										trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
									}
									query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, trans_cd);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			trans_cd = rset.getString(1);
						    		}else {
						    			trans_cd = "";
						    		}
						    		rset.close();
						    		stmt.close();
						    		data = trans_cd;
					    		}
						    	
						    	else if (cell.getColumnIndex() == 9) {	// trans_seq
									trans_seq = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
									if(!trans_seq.equals("null")) {
										trans_seq = trans_seq.substring(1, trans_seq.length()-1);
										
										query_String = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ?";
										stmt = conn.prepareStatement(query_String);
										stmt.setString(1, trans_seq);
										rset = stmt.executeQuery();
										
										if (rset.next()) {
											trans_seq = rset.getString(1);
										}
										else{
											trans_seq = "";
										}
										rset.close();
										stmt.close();
							    	}
									data=trans_seq;
								}
						    	
						    	else if(cell.getColumnIndex() == 10) { // BU_SEQ
						    		query_String = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq = rset.getString(1);
						    		}
						    		else if(!rset.next()){
						    			bu_seq = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
						    			if (!bu_seq.equals("null")) {
											bu_seq = bu_seq.substring(1, bu_seq.length() - 1);
										}
						    		}
						    		rset.close();
						    		stmt.close();	
						    		data  = bu_seq;
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
							}
							//while(column) completed
							
							query_select = query_select.substring(0, query_select.length() - 4);
				            if(!cd.equals("")) 
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
							        cd=rset.getString(2);
							        agmt_no=rset.getString(3);
							        agmt_rev=rset.getString(4);
							        cont_no = rset.getString(5);
							        cont_rev = rset.getString(6);
							        nom_rev_no = rset.getString(7);
							        plant_seq =rset.getString(8);
							        trans_cd = rset.getString(9);
							        trans_seq = rset.getString(10);
							        bu_seq = rset.getString(11);
							        gas_dt = rset.getString(12);
							        cont_type = rset.getString(13);
							        seq_no = rset.getString(14);
							        cargo_no = rset.getString(15);
							        
								    st_del = conn.prepareStatement(query_delete);
								    st_del.setString(1, cd);
								    st_del.setString(2, agmt_no);
								    st_del.setString(3, agmt_rev);
								    st_del.setString(4, cont_no);
								    st_del.setString(5, cont_rev);
								    st_del.setString(6, nom_rev_no);
								    st_del.setString(7, plant_seq);
								    st_del.setString(8, trans_cd);
								    st_del.setString(9, trans_seq);
								    st_del.setString(10, bu_seq);
								    st_del.setString(11, gas_dt);
								    st_del.setString(12, cont_type);
								    st_del.setString(13, seq_no);
								    st_del.setString(14, cargo_no);


								    st_del.executeUpdate();
								  
									logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + plant_seq+ "," + nom_rev_no + ","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+cont_type+","+seq_no+","+cargo_no+","), conn, "");

								    logger_count++;   
								  
								    st_del.close();
							      }

							     rset.close();
							     stmt.close();
				            }
					}//while(row) completed
					
					// Data left
					query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, CONT_REV,NOM_REV_NO, PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ, TO_CHAR(GAS_DT,'DD/MM/YYYY hh24:mi:ss'),CONTRACT_TYPE,SEQ_NO,CARGO_NO FROM FMS_DAILY_SELLER_NOM_DTL WHERE COMPANY_CD = '2' ";
					stmt = conn.prepareStatement(query_data_left);
					
					rset = stmt.executeQuery();
					while (rset.next()) {
						company_cd=rset.getString(1);
				        cd=rset.getString(2);
				        agmt_no=rset.getString(3);
				        agmt_rev=rset.getString(4);
				        cont_no = rset.getString(5);
				        cont_rev = rset.getString(6);
				        nom_rev_no = rset.getString(7);
				        plant_seq =rset.getString(8);
				        trans_cd = rset.getString(9);
				        trans_seq = rset.getString(10);
				        bu_seq = rset.getString(11);
				        gas_dt = rset.getString(12);
				        cont_type = rset.getString(13);
				        seq_no = rset.getString(14);
				        cargo_no = rset.getString(15);
				      
						
						logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + plant_seq+ "," + nom_rev_no + ","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+cont_type+","+seq_no+","+cargo_no+","), conn, "N");

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
				System.out.println("<<ROLLBACK_END>><<FMS_DAILY_SELLER_NOM_DTL()>>");
				
				
				logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,,,", conn);
				logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_DAILY_SELLER_NOM_DTL()>>,,,,,,,,,", conn);
				
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
		
//FMS_SUPPLY_CONT_TRANSPTR
	public void FMS_SUPPLY_CONT_TRANSPTR () throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_CONT_TRANSPTR()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_CONT_TRANSPTR()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_CONT_TRANSPTR>>,,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SUPPLY_CONT_TRANSPTR WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ?  AND CONT_NO =? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND TRANSPORTER_CD = ? AND PLANT_SEQ_NO =  ?";

			String  agmt_no, agmt_rev, cont_no, cont_rev, cont_type,plant_seq_no,trans_cd;
			String cont_ref,trans_abbr;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRANSPORTER_CD,PLANT_SEQ_NO,ENT_BY,ENT_DT";

			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_TRANSPTR_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_TRANSPTR_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO, CONT_REV,CONTRACT_TYPE,TRANSPORTER_CD,PLANT_SEQ_NO, TIMESTAMP,", conn);

				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, CONT_REV,CONTRACT_TYPE,TRANSPORTER_CD,PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_TRANSPTR WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
					    agmt_no=""; agmt_rev=""; cont_no="";cont_rev="";cont_type="";plant_seq_no="";
					    trans_cd=""; cont_ref="";trans_abbr="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		cont_ref = data;
//					    		System.out.println("::cont_ref::"+cont_ref);
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							
					    	else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")){
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 6) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
					    	}
					    	else if (cell.getColumnIndex() == 7) { //trans_abbr
					    		trans_abbr = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!trans_abbr.equals("null")) {
					    			trans_abbr = trans_abbr.substring(1, trans_abbr.length() - 1);
								}
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1,trans_abbr );
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		} else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = trans_cd;
					    	}
					    	
							
//										System.out.println(i+"::data::"+data);
							
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
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_no=rset.getString(3);
						        agmt_rev=rset.getString(4);
						        cont_no = rset.getString(5);
						        cont_rev = rset.getString(6);
						        cont_type = rset.getString(7);
						        trans_cd = rset.getString(8);	
						        plant_seq_no =rset.getString(9);
						        
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_no);
							    st_del.setString(3, agmt_rev);
							    st_del.setString(4, cont_no);
							    st_del.setString(5, cont_rev);
							    st_del.setString(6, cont_type);
							    st_del.setString(7, trans_cd);
							    st_del.setString(8, plant_seq_no);


							    st_del.executeUpdate();
							  
								logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + ","+cont_no+","+cont_rev+","+cont_type+","+trans_cd+","+plant_seq_no+","), conn, "");

							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO, CONT_REV,CONTRACT_TYPE, TRANSPORTER_CD, PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_TRANSPTR WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_no=rset.getString(3);
			        agmt_rev=rset.getString(4);
			        cont_no = rset.getString(5);
			        cont_rev = rset.getString(6);
			        cont_type = rset.getString(7);
			        trans_cd = rset.getString(8);	
			        plant_seq_no =rset.getString(9);
					
					logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + ","+cont_no+","+cont_rev+","+cont_type+","+trans_cd+","+plant_seq_no+","), conn, "N");

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
			System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_CONT_TRANSPTR()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_CONT_TRANSPTR()>>,,,,,,,,", conn);
			
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

//FMS_SUPPLY_CONT_LIABILITY
	public void FMS_SUPPLY_CONT_LIABILITY () throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_CONT_LIABILITY()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_CONT_LIABILITY()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_CONT_LIABILITY>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SUPPLY_CONT_LIABILITY WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ?";

			String  agmt_no, agmt_rev,cont_no,cont_rev,cont_type;
			String ld_promise, top_promise,cont_ref;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,LIAB_LQ_DAMG,"
					+ "LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_FROM,LQ_DAMG_TO,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_FROM,TAKE_PAY_TO,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,"
					+ "TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,"
					+ "MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT";

			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_LIABILITY_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_LIABILITY_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_SUPPLY_CONT_LIABILITY WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
					    agmt_no=""; agmt_rev=""; cont_no="";cont_rev="";cont_type="";
					    cont_ref="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							
					    	else if(cell.getColumnIndex() == 3) {
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		if(cont_ref.startsWith("L")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = 'L' ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_type = rset.getString(3);
						    			cont_no = rset.getString(4);
						    			cont_rev = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_type = "";
						    			cont_no = "";
						    			cont_rev = "";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Agmt_rev
					    		if(!cont_ref.startsWith("L")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 5) { //contract_type
					    		if(!cont_ref.startsWith("L")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
					    	}
					    	else if (cell.getColumnIndex() == 6) { //Cont_no
					    		if(!cont_ref.startsWith("L")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 7) { //Cont_rev
					    		if(!cont_ref.startsWith("L")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
							
					    	else if(cell.getColumnIndex() == 10) {
					    		ld_promise = data;
					    		
					    		if("Daily".equals(ld_promise)) {
					    			ld_promise = "D";
					    		}
					    		else if("Weekly".equals(ld_promise)) {
					    			ld_promise = "W";
					    		}
					    		else if("Fortnightly".equals(ld_promise)) {
					    			ld_promise = "F";
					    		}
					    		else if("Monthly".equals(ld_promise)) {
					    			ld_promise = "M";
					    		}
					    		else if("Quarterly".equals(ld_promise)) {
					    			ld_promise = "Q";
					    		}
					    		else if("Invoice Cycle".equals(ld_promise)) {
					    			ld_promise = "IC";
					    		}
					    		else if("TCQ".equals(ld_promise)) {
					    			ld_promise = "T";
					    		}
					    		else if("Defined Period".equals(ld_promise)) {
					    			ld_promise = "DP";
					    		}
					    		else if("Supply Period".equals(ld_promise)) {
					    			ld_promise = "SP";
					    		}else {
					    			ld_promise = "null";
					    		}
					    		
					    		data = ld_promise;
					    	}
							
					    	else if(cell.getColumnIndex() == 18) {
					    		top_promise = data;
					    		
					    		if("Daily".equals(top_promise)) {
					    			top_promise = "D";
					    		}
					    		else if("Weekly".equals(top_promise)) {
					    			top_promise = "W";
					    		}
					    		else if("Fortnightly".equals(top_promise)) {
					    			top_promise = "F";
					    		}
					    		else if("Monthly".equals(top_promise)) {
					    			top_promise = "M";
					    		}
					    		else if("Quarterly".equals(top_promise)) {
					    			top_promise = "Q";
					    		}
					    		else if("Invoice Cycle".equals(top_promise)) {
					    			top_promise = "IC";
					    		}
					    		else if("TCQ".equals(top_promise)) {
					    			top_promise = "T";
					    		}
					    		else if("Defined Period".equals(top_promise)) {
					    			top_promise = "DP";
					    		}
					    		else if("Supply Period".equals(top_promise)) {
					    			top_promise = "SP";
					    		}
					    		else {
					    			top_promise = "null";
					    		}
					    		
					    		data = top_promise;
					    	}
					         
					    	
//								 System.out.println(i+"::>>data>>:: "+data);
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
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_no=rset.getString(3);
						        agmt_rev=rset.getString(4);
						        cont_type=rset.getString(5);
						        cont_no=rset.getString(6);
						        cont_rev=rset.getString(7);
						       
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_no);
							    st_del.setString(3, agmt_rev);
							    st_del.setString(4, cont_type);
							    st_del.setString(5, cont_no);
							    st_del.setString(6, cont_rev);
							   
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD,AGMT_NO, AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_SUPPLY_CONT_LIABILITY WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_no=rset.getString(3);
			        agmt_rev=rset.getString(4);
			        cont_type=rset.getString(5);
			        cont_no=rset.getString(6);
			        cont_rev=rset.getString(7);
			       
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_CONT_LIABILITY()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_CONT_LIABILITY()>>,,,,,,", conn);
			
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
		

//FMS_SUPPLY_BILLING_DTL
	public void FMS_SUPPLY_BILLING_DTL () throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_BILLING_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_BILLING_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_BILLING_DTL>>,,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SUPPLY_BILLING_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ?  AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND PLANT_SEQ_NO = ?";

			String  agmt_no, agmt_rev,cont_no,cont_rev,cont_type;
			String cont_ref,eff_dt,plant_seq_no,state_code,int_cd;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,"
					 +"EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EFF_DT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE";

			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_BILLING_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_BILLING_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,EFF_DT, PLANT_SEQ_NO,TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss'),PLANT_SEQ_NO FROM FMS_SUPPLY_BILLING_DTL WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
					    agmt_no=""; agmt_rev=""; cont_no="";cont_rev="";cont_type="";
					    cont_ref="";eff_dt="";plant_seq_no="";state_code="";int_cd="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							
					    	else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
					    		}
					    						    		
					    		data  = agmt_no;
					    	}
							
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 6) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
					    	}
							
					    	else if(cell.getColumnIndex() == 13) {
					    		name = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
//					    		if (name != null) {
					    		if(!name.equals("null")) {
									name = name.substring(1, name.length() - 1);
								}
								query_String = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) = ? ";
						    	stmt = conn.prepareStatement(query_String);
						    	stmt.setString(1, name);
						    	rset = stmt.executeQuery();
						    	if (rset.next()) {
						    		int_cd = rset.getString(1);
						    	}else {
						    		int_cd = "null";
						    	}
						    	rset.close();
						    	stmt.close();
						    	data = int_cd;
					    	}
							
					    	else if(cell.getColumnIndex() == 16) {
					    		name = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
//					    		if (name!=null) {
					    		if (!name.equals("null")) {
									name = name.substring(1, name.length() - 1);
								}
								String queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
						    	stmt = conn.prepareStatement(queryString);
						    	stmt.setString(1, name);
						    	rset = stmt.executeQuery();
						    	if (rset.next()) {
						    		exchg_cd = rset.getString(1);
						    	}
						    	rset.close();
						    	stmt.close();
						    	data = exchg_cd;
					    	}
					    	else if (cell.getColumnIndex() == 31) { //PLANT_SEQ_NO
					    		
					    		query_String = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_PLANT WHERE COUNTERPARTY_CD = ? "
					    				+ "AND CONT_NO=? AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_no);
					    		stmt.setString(3, cont_rev);
					    		stmt.setString(4, agmt_no);
					    		stmt.setString(5, agmt_rev);
					    		stmt.setString(6, cont_type);
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			plant_seq_no= rset.getString(1);
					    		} else {
					    			plant_seq_no = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = plant_seq_no;
					    		
					    	}
					    	else if (cell.getColumnIndex() == 32) { //holiday_state
						    		query_String = "SELECT B.STATE_CODE FROM  FMS_COUNTERPARTY_PLANT_DTL A,FMS_STATE_MST B WHERE A.COUNTERPARTY_CD = ? AND B.STATE_NM = A.PLANT_STATE ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, cd);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			state_code=rset.getString(1);
						    		}
						    		else
						    		{
						    			state_code="null";
						    		}
						    		rset.close();
						    		stmt.close();
						    		data = state_code;
					    	}
							
//								 System.out.println(i+"::>>data>>:: "+data);
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
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
						        cd=rset.getString(2);
						        agmt_no=rset.getString(3);
						        agmt_rev=rset.getString(4);
						        cont_no=rset.getString(5);
						        cont_rev=rset.getString(6);
						        cont_type=rset.getString(7);
						        eff_dt=rset.getString(8);
						        plant_seq_no=rset.getString(9);
						       
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_no);
							    st_del.setString(3, agmt_rev);
							    st_del.setString(4, cont_no);
							    st_del.setString(5, cont_rev);
							    st_del.setString(6, cont_type);
							    st_del.setString(7, eff_dt);
							    st_del.setString(8, plant_seq_no);
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+eff_dt+","+plant_seq_no+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD,AGMT_NO, AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss'),PLANT_SEQ_NO FROM FMS_SUPPLY_BILLING_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_no=rset.getString(3);
			        agmt_rev=rset.getString(4);
			        cont_no=rset.getString(5);
			        cont_rev=rset.getString(6);
			        cont_type=rset.getString(7);
			        eff_dt=rset.getString(8);
			        plant_seq_no=rset.getString(9);
			      
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+eff_dt+","+plant_seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_BILLING_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_BILLING_DTL()>>,,,,,,,,", conn);
			
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
		

//FMS_CONT_PRICE_DTL	
	public void FMS_CONT_PRICE_DTL() throws SQLException, IOException {
		function_nm = "FMS_CONT_PRICE_DTL(S)()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_CONT_PRICE_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_CONT_PRICE_DTL>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = '2' AND MAPPING_ID = ? AND CONTRACT_TYPE = ? AND SEQ_NO = ?";

			String  map_id,seq_no,cont_type,cont_ref,cont_rev,cont_no;
			
			
			column = "COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,PRICE_TYPE,CURVE_NM,SLOPE,CONST,QUANTITY_UNIT,RATE,RATE_UNIT,REMARKS,ENT_BY,"
					+"ENT_DT,MODIFY_BY,MODIFY_DT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,PHYS_CURVE_NM,CURVE_LOGIC";
			
			
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						map_id="";seq_no="";cont_type="";cont_ref="";cont_rev="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {				    			
					    			cd=rset.getString(1);
					    		}else  {
					    			cd = "";
					    		}	
					    		
					    		rset.close();
					    		stmt.close();
						    					    						    						    		
					    		map_id  = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());		
					    		if (!map_id.equals("null")) {
									map_id = map_id.substring(1, map_id.length() - 1);
								}
								if(map_id.startsWith("L")) {
					    			String[] parts = map_id.split("-");	
					    			agmt_no = parts[2];
						            cont_no = parts[4];
						            cont_rev = parts[5];
						            
						            cont_ref = "L-"+agmt_no+"-"+cont_no+"-"+cont_rev;
						            
						            query_String2 = "SELECT CONT_NO FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = 'L' ";
						    		stmt2 = conn.prepareStatement(query_String2);
						    		stmt2.setString(1, cd);
						    		stmt2.setString(2, cont_ref);
						    		rset2 = stmt2.executeQuery();
						    		if (rset2.next()) {					    		
						    			cont_no = rset2.getString(1);					    			
						    		} else {
						    			cont_no = "";

						    		}
						    		rset2.close();
						    		stmt2.close();
					    		}else {
					    			String[] parts = map_id.split("-");	
						            agmt_no = parts[1];
						            cont_no = parts[3];
					    		}
					            
					    		map_id = cd+"-"+agmt_no+"-"+cont_no;	
					    		
					    		data = map_id;
					    		}
								
							
							else if(cell.getColumnIndex() == 23) {
								data = null;
							}
							
//							System.out.println(i+"::>>data>>:: "+data);
							
								 
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
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								map_id=rset.getString(2);
						        cont_type=rset.getString(3);
						        seq_no=rset.getString(4);
						       
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, map_id);
							    st_del.setString(2, cont_type);
							    st_del.setString(3, seq_no);
							    
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+map_id+","+cont_type+","+seq_no+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					map_id=rset.getString(2);
			        cont_type=rset.getString(3);
			        seq_no=rset.getString(4);
			      
					
				    logger.data(fname, (company_cd+","+map_id+","+cont_type+","+seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_CONT_PRICE_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_CONT_PRICE_DTL()>>,,,", conn);
			
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
	
//FMS_CONT_PRICE_MIN_DTL
	public void FMS_CONT_PRICE_MIN_DTL() throws SQLException, IOException {
		function_nm = "FMS_CONT_PRICE_MIN_DTL(S)()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_CONT_PRICE_MIN_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_CONT_PRICE_MIN_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = '2' AND MAPPING_ID = ? AND CONTRACT_TYPE = ? AND SEQ_NO = ? AND CURVE_NM = ?";

			String  map_id,seq_no,cont_type,cont_ref,cont_rev,cont_no,curve_nm;
			
			
			column = "COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,CURVE_LOGIC,CURVE_NM,SLOPE,CONST,QUANTITY_UNIT,"
					+"RATE,RATE_UNIT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO, CURVE_NM, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,CURVE_NM FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						map_id="";seq_no="";cont_type="";cont_ref="";cont_rev="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {				    			
					    			cd=rset.getString(1);
					    		}else  {
					    			cd = "";
					    		}	
					    		
					    		rset.close();
					    		stmt.close();
						    					    						    						    		
					    		map_id  = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());		
					    		if (!map_id.equals("null")) {
									map_id = map_id.substring(1, map_id.length() - 1);
								}
								if(map_id.startsWith("L")) {
					    			String[] parts = map_id.split("-");	
					    			agmt_no = parts[2];
						            cont_no = parts[4];
						            cont_rev = parts[5];
						            
						            cont_ref = "L-"+agmt_no+"-"+cont_no+"-"+cont_rev;
						            
						            query_String2 = "SELECT CONT_NO FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = 'L' ";
						    		stmt2 = conn.prepareStatement(query_String2);
						    		stmt2.setString(1, cd);
						    		stmt2.setString(2, cont_ref);
						    		rset2 = stmt2.executeQuery();
						    		if (rset2.next()) {					    		
						    			cont_no = rset2.getString(1);					    			
						    		} else {
						    			cont_no = "";

						    		}
						    		rset2.close();
						    		stmt2.close();
					    		}else {
					    			String[] parts = map_id.split("-");	
						            agmt_no = parts[1];
						            cont_no = parts[3];
					    		}
					            
					    		map_id = cd+"-"+agmt_no+"-"+cont_no;	
					    		
					    		data = map_id;
					    		}
	
//							System.out.println(i+"::>>data>>:: "+data);
							
								 
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
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								map_id=rset.getString(2);
						        cont_type=rset.getString(3);
						        seq_no=rset.getString(4);
						        curve_nm=rset.getString(5);
						       
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, map_id);
							    st_del.setString(2, cont_type);
							    st_del.setString(3, seq_no);
							    st_del.setString(4, curve_nm);
							    
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+map_id+","+cont_type+","+seq_no+","+curve_nm+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO, CURVE_NM FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					map_id=rset.getString(2);
			        cont_type=rset.getString(3);
			        seq_no=rset.getString(4);
			        curve_nm=rset.getString(5);

					
				    logger.data(fname, (company_cd+","+map_id+","+cont_type+","+seq_no+","+curve_nm+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_CONT_PRICE_MIN_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_CONT_PRICE_MIN_DTL()>>,,,,", conn);
			
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
	

	
//FMS_METER_TICKET_READING
	public void FMS_METER_TICKET_READING () throws SQLException, IOException {
		function_nm = "FMS_METER_TICKET_READING()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_METER_TICKET_READING()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_METER_TICKET_READING>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_METER_TICKET_READING WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND PLANT_SEQ = ? AND METER_TYPE = ? AND METER_SEQ = ? AND GAS_DT=TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss')";

			String  plant_seq, mt_type, mt_seq, gas_dt;
			
			
			column = "COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,METER_SEQ,GAS_DT,GEN_DT,GEN_TIME,QTY_MMBTU,QTY_SCM,QTY_BTU,RECONCIL_QTY_MMBTU,"
					+"RECONCIL_QTY_SCM,RECONCIL_QTY_BTU,TOTAL_QTY_MMBTU,CALC_GCV,CALC_NCV,DEFINE_GCV,DEFINE_NCV,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,TOTAL_QTY_SCM";
			
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_METER_TICKET_READING_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_METER_TICKET_READING_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, PLANT_SEQ, METER_TYPE, METER_SEQ, GAS_DT, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD, PLANT_SEQ, METER_TYPE, METER_SEQ, TO_CHAR(GAS_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_METER_TICKET_READING WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						plant_seq=""; mt_type=""; mt_seq=""; gas_dt="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
					    	else if(cell.getColumnIndex() == 2 )//plant_seq_no
					    	{	
					    		 data= cell.getStringCellValue();
					    		 data= data.substring(1, data.length()-1);

					    		if (data.length() > 20) {
					    			  data = data.substring(0, data.length() - 1);
					    		}
					    		query_String = "SELECT METER_SEQ,PLANT_SEQ,METER_TYPE FROM FMS_METER_MST WHERE  METER_REF LIKE ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1,data+"%");
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			mt_seq = rset.getString(1);
					    			plant_seq = rset.getString(2);
					    			mt_type=rset.getString(3);
					    		}
					    		else {
					    			mt_seq = "null";
					    			plant_seq = "null";
					    			mt_type="null";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = plant_seq;
					    	}
					    	else if(cell.getColumnIndex() == 3 )//meter seq_no
					    	{	
					    		data = mt_type;
					    	}
					    	else if(cell.getColumnIndex() == 4 )//meter seq_no
					    	{	
					    		data = mt_seq;
					    	}
								
						
							
//							System.out.println(i+"::>>data>>:: "+data);
							
								 
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
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
									cd=rset.getString(2);
									plant_seq=rset.getString(3);
									mt_type=rset.getString(4);
									mt_seq=rset.getString(5);
									gas_dt=rset.getString(6);
						       
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, plant_seq);
							    st_del.setString(3, mt_type);
							    st_del.setString(4, mt_seq);
							    st_del.setString(5, gas_dt);
							    
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+plant_seq+","+mt_type+","+mt_seq+","+gas_dt+","), conn, "");
							    logger_count++;   
//							  System.out.println("::logger_count::"+logger_count);
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, PLANT_SEQ, METER_TYPE, METER_SEQ, TO_CHAR(GAS_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS_METER_TICKET_READING WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					plant_seq=rset.getString(3);
					mt_type=rset.getString(4);
					mt_seq=rset.getString(5);
					gas_dt=rset.getString(6);

					
				    logger.data(fname, (company_cd+","+cd+","+plant_seq+","+mt_type+","+mt_seq+","+gas_dt+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_METER_TICKET_READING()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_METER_TICKET_READING()>>,,,,,", conn);
			
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
	

//FMS_DAILY_ALLOCATION_DTL
	public void FMS_DAILY_ALLOCATION_DTL () throws SQLException, IOException {
		function_nm = "FMS_DAILY_ALLOCATION_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_DAILY_ALLOCATION_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_DAILY_ALLOCATION_DTL>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_DAILY_ALLOCATION_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT=TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CONTRACT_TYPE = ? AND CARGO_NO = ?";

			String  agmt_no,agmt_rev,cont_no,cont_rev,nom_rev_no,cont_ref;
			String plant_seq,trans_cd,trans_seq,bu_seq, gas_dt,cont_type,cargo_no;
			
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,"
					+"GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO";
			
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_ALLOCATION_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_ALLOCATION_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, NOM_REV_NO,  GAS_DT, CONTRACT_TYPE, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, NOM_REV_NO, PLANT_SEQ, TRANSPORTER_CD, TRANS_SEQ, BU_SEQ, TO_CHAR(GAS_DT, 'DD/MM/YYYY hh24:mi:ss'), CONTRACT_TYPE,CARGO_NO FROM FMS_DAILY_ALLOCATION_DTL WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						agmt_no="";agmt_rev="";cont_no="";cont_rev="";nom_rev_no="";cont_ref="";
						plant_seq="";trans_cd="";trans_seq="";bu_seq="";gas_dt="";cont_type="";cargo_no="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
					    		}
					    						    		
					    		data  = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 12) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")){
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
					    	}
					    	
					    	else if (cell.getColumnIndex() == 8) {	// trans_cd
					    		trans_cd = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!trans_cd.equals("null")) {
									trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
								}
								query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, trans_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		}else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = trans_cd;
				    		}
					    	
	
					    	else if (cell.getColumnIndex() == 10) {
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
//								System.out.println(i+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
									cd=rset.getString(2);
									agmt_no=rset.getString(3);
									agmt_rev=rset.getString(4);
									cont_no=rset.getString(5);
									cont_rev=rset.getString(6);
									nom_rev_no=rset.getString(7);
									plant_seq=rset.getString(8);
									trans_cd=rset.getString(9);
									trans_seq=rset.getString(10);
									bu_seq=rset.getString(11);
									gas_dt=rset.getString(12);
									cont_type=rset.getString(13);
									cargo_no=rset.getString(14);
						       
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2,agmt_no );
							    st_del.setString(3,agmt_rev);
							    st_del.setString(4,cont_no);
							    st_del.setString(5,cont_rev);
							    st_del.setString(6,nom_rev_no);
							    st_del.setString(7,plant_seq);
							    st_del.setString(8,trans_cd);
							    st_del.setString(9,trans_seq);
							    st_del.setString(10,bu_seq);
							    st_del.setString(11,gas_dt);
							    st_del.setString(12,cont_type);
							    st_del.setString(13,cargo_no);
							    
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+plant_seq+","+gas_dt+","+cont_type+","), conn, "");
							    logger_count++;   
//							  System.out.println("::logger_count::"+logger_count);
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, NOM_REV_NO, PLANT_SEQ, TRANSPORTER_CD, TRANS_SEQ, BU_SEQ, TO_CHAR(GAS_DT, 'DD/MM/YYYY hh24:mi:ss'), CONTRACT_TYPE, CARGO_NO FROM FMS_DAILY_ALLOCATION_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					agmt_no=rset.getString(3);
					agmt_rev=rset.getString(4);
					cont_no=rset.getString(5);
					cont_rev=rset.getString(6);
					nom_rev_no=rset.getString(7);
					plant_seq=rset.getString(8);
					trans_cd=rset.getString(9);
					trans_seq=rset.getString(10);
					bu_seq=rset.getString(11);
					gas_dt=rset.getString(12);
					cont_type=rset.getString(13);
					cargo_no=rset.getString(14);

					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+plant_seq+","+gas_dt+","+cont_type+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_DAILY_ALLOCATION_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_DAILY_ALLOCATION_DTL()>>,,,,,", conn);
			
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
	

//FMS_DAILY_ALLOCATION_DTL_CT	
	public void FMS_DAILY_ALLOCATION_DTL_CT () throws SQLException, IOException {
		function_nm = "FMS_DAILY_ALLOCATION_DTL_CT()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_DAILY_ALLOCATION_DTL_CT()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_DAILY_ALLOCATION_DTL_CT>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_DAILY_ALLOCATION_DTL_CT WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT=TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CONTRACT_TYPE = ? AND SEQ_NO = ? AND CARGO_NO = ? AND DTL_CATEGORY = ?";

			String  agmt_no,agmt_rev,cont_no,cont_rev,nom_rev_no,cont_ref,seq_no,dtl_catg;
			String plant_seq,trans_cd,trans_seq,bu_seq, gas_dt,cont_type,cargo_no;
			
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,GAS_DT,CONTRACT_TYPE,"
					+"SEQ_NO,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,CT_REF,UTR_NO,ENT_BY,ENT_DT,CARGO_NO,DTL_CATEGORY,MOLECULE_MAP";
			
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_ALLOCATION_DTL_CT_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_ALLOCATION_DTL_CT_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, NOM_REV_NO, PLANT_SEQ, TRANSPORTER_CD, TRANS_SEQ, BU_SEQ, GAS_DT, CONTRACT_TYPE, SEQ_NO, CARGO_NO, DTL_CATEGORY, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, NOM_REV_NO, PLANT_SEQ, TRANSPORTER_CD, TRANS_SEQ, BU_SEQ, TO_CHAR(GAS_DT, 'DD/MM/YYYY hh24:mi:ss'), CONTRACT_TYPE, SEQ_NO, CARGO_NO, DTL_CATEGORY FROM FMS_DAILY_ALLOCATION_DTL_CT WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						agmt_no="";agmt_rev="";cont_no="";cont_rev="";nom_rev_no="";cont_ref="";seq_no="";dtl_catg="";
						plant_seq="";trans_cd="";trans_seq="";bu_seq="";gas_dt="";cont_type="";cargo_no="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
					    		}
					    						    		
					    		data  = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 12) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
					    	}
					    	
					    	else if (cell.getColumnIndex() == 8) {	// trans_cd
					    		trans_cd = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!trans_cd.equals("null")) {
									trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
								}
								query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, trans_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		}else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = trans_cd;
				    		}
					    	
	
					    	else if (cell.getColumnIndex() == 10) {
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
//								System.out.println(i+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
									cd=rset.getString(2);
									agmt_no=rset.getString(3);
									agmt_rev=rset.getString(4);
									cont_no=rset.getString(5);
									cont_rev=rset.getString(6);
									nom_rev_no=rset.getString(7);
									plant_seq=rset.getString(8);
									trans_cd=rset.getString(9);
									trans_seq=rset.getString(10);
									bu_seq=rset.getString(11);
									gas_dt=rset.getString(12);
									cont_type=rset.getString(13);
									seq_no=rset.getString(14);
									cargo_no=rset.getString(15);
									dtl_catg=rset.getString(16);
						       
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2,agmt_no );
							    st_del.setString(3,agmt_rev);
							    st_del.setString(4,cont_no);
							    st_del.setString(5,cont_rev);
							    st_del.setString(6,nom_rev_no);
							    st_del.setString(7,plant_seq);
							    st_del.setString(8,trans_cd);
							    st_del.setString(9,trans_seq);
							    st_del.setString(10,bu_seq);
							    st_del.setString(11,gas_dt);
							    st_del.setString(12,cont_type);
							    st_del.setString(13,seq_no);
							    st_del.setString(14,cargo_no);
							    st_del.setString(15, dtl_catg);
							    
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+plant_seq+","+gas_dt+","+cont_type+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, NOM_REV_NO, PLANT_SEQ, TRANSPORTER_CD, TRANS_SEQ, BU_SEQ, TO_CHAR(GAS_DT, 'DD/MM/YYYY hh24:mi:ss'), CONTRACT_TYPE, SEQ_NO, CARGO_NO, DTL_CATEGORY FROM FMS_DAILY_ALLOCATION_DTL_CT WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					agmt_no=rset.getString(3);
					agmt_rev=rset.getString(4);
					cont_no=rset.getString(5);
					cont_rev=rset.getString(6);
					nom_rev_no=rset.getString(7);
					plant_seq=rset.getString(8);
					trans_cd=rset.getString(9);
					trans_seq=rset.getString(10);
					bu_seq=rset.getString(11);
					gas_dt=rset.getString(12);
					cont_type=rset.getString(13);
					seq_no=rset.getString(14);
					cargo_no=rset.getString(15);
					dtl_catg=rset.getString(16);

					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+plant_seq+","+gas_dt+","+cont_type+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_DAILY_ALLOCATION_DTL_CT()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_DAILY_ALLOCATION_DTL_CT()>>,,,,,", conn);
			
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
	
//FMS_SECURITY_DEAL_MAP
	public void FMS_SECURITY_DEAL_MAP () throws SQLException, IOException {
		function_nm = "FMS_SECURITY_DEAL_MAP(S)()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SECURITY_DEAL_MAP()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SECURITY_DEAL_MAP>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND MAP_SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = ?";

			String seq_no, mp_sq_no, sq_rev_no, gx;
			
			
			column = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+"ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT";
			
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, MAP_SEQ_NO, SEQ_REV_NO, GX, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEQ_REV_NO,GX FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						seq_no=""; mp_sq_no=""; sq_rev_no=""; gx="";						
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
//								System.out.println(i+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								cd=rset.getString(2);
								seq_no=rset.getString(3);
								mp_sq_no=rset.getString(4);
								sq_rev_no=rset.getString(5);
								gx=rset.getString(6);
								
						       
						        
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2,seq_no );
							    st_del.setString(3,mp_sq_no);
							    st_del.setString(4,sq_rev_no);
							    st_del.setString(5,gx);
							 
							    
							   
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+seq_no+","+mp_sq_no+","+sq_rev_no+","+gx+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, MAP_SEQ_NO, SEQ_REV_NO, GX FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					seq_no=rset.getString(3);
					mp_sq_no=rset.getString(4);
					sq_rev_no=rset.getString(5);
					gx=rset.getString(6);
					
					
				    logger.data(fname, (company_cd+","+cd+","+seq_no+","+mp_sq_no+","+sq_rev_no+","+gx+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SECURITY_DEAL_MAP()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SECURITY_DEAL_MAP()>>,,,,,", conn);
			
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
	

//FMS_SECURITY_MST
	public void FMS_SECURITY_MST () throws SQLException, IOException {
		function_nm = "FMS_SECURITY_MST(S)()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SECURITY_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SECURITY_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SECURITY_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = ?";

			String seq_no, sq_rev_no, gx,bank_cd,g_cd;
			
			
			column = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,"
					+"CONFIRM_BANK_CD,CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,"
					+"MODIFY_BY,APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD,TDS_EFF_DT";
			
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECURITY_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEQ_REV_NO,GX FROM FMS_SECURITY_MST WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						seq_no="";  sq_rev_no=""; gx="";bank_cd="";g_cd="";					
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							
							else if (cell.getColumnIndex() == 7) {//GUARANTOR_CD
					    		g_cd = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
					    		if(!g_cd.equals("null"))
					    		{
					    			g_cd = g_cd.substring(1, g_cd.length() - 1);
						    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, g_cd);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			g_cd = rset.getString(1);
						    		} else {
						    			g_cd ="";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		data=g_cd;
					    		
					    	}
					    	else if (cell.getColumnIndex() == 12  || cell.getColumnIndex() == 14 || cell.getColumnIndex() ==  16) {
					    		bank_cd = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
					    		if(!bank_cd.equals("null")) {
						    		bank_cd = bank_cd.substring(1, bank_cd.length() - 1);
									query_String2 = "SELECT BANK_CD FROM FMS_BANK_MST WHERE  BANK_NAME = ? ";
									stmt2 = conn.prepareStatement(query_String2);
									stmt2.setString(1,bank_cd );
									rset2 = stmt2.executeQuery();
									if(rset2.next()) {
										bank_cd=rset2.getString(1);
									}
									else {
										bank_cd="null";
									}
									rset2.close();
									stmt2.close();
								}
					    		data=bank_cd;
					    		
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
//								System.out.println(i+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								cd=rset.getString(2);
								seq_no=rset.getString(3);
								sq_rev_no=rset.getString(4);
								gx=rset.getString(5);
								
						
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2,seq_no );
							    st_del.setString(3,sq_rev_no);
							    st_del.setString(4,gx);
							
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+seq_no+","+sq_rev_no+","+gx+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX FROM FMS_SECURITY_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					seq_no=rset.getString(3);
					sq_rev_no=rset.getString(4);
					gx=rset.getString(5);
					
					
				    logger.data(fname, (company_cd+","+cd+","+seq_no+","+sq_rev_no+","+gx+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SECURITY_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SECURITY_MST()>>,,,,", conn);
			
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
	
//LOG_FMS_SECURITY_MST
	public void LOG_FMS_SECURITY_MST () throws SQLException, IOException {
		function_nm = "LOG_FMS_SECURITY_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<LOG_FMS_SECURITY_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<LOG_FMS_SECURITY_MST>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = ? AND LOG_SEQ_NO = ?";

			String seq_no, sq_rev_no, gx,bank_cd,g_cd,lg_sq_no;
			
			
			column = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,"
					+"CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,"
					+"SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,LOG_ENT_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD,TDS_EFF_DT";
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX, LOG_SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEQ_REV_NO,GX,LOG_SEQ_NO FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						seq_no="";  sq_rev_no=""; gx="";bank_cd="";g_cd="";lg_sq_no="";			
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							
							else if (cell.getColumnIndex() == 7) {//GUARANTOR_CD
					    		g_cd = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
					    		if(g_cd.equals("null"))
					    		{
				    			g_cd = g_cd.substring(1, g_cd.length() - 1);
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, g_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			g_cd = rset.getString(1);
					    		} else {
					    			g_cd ="";
					    		}
					    		rset.close();
					    		stmt.close();
					    		}
					    		data=g_cd;
					    		
					    	}
					    	else if (cell.getColumnIndex() == 12  || cell.getColumnIndex() == 14 || cell.getColumnIndex() ==  16) {
					    		bank_cd = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
					    		if(bank_cd.equals("null")) {
					    		bank_cd = bank_cd.substring(1, bank_cd.length() - 1);
								query_String2 = "SELECT BANK_CD FROM FMS_BANK_MST WHERE  BANK_NAME = ? ";
								stmt2 = conn.prepareStatement(query_String2);
								stmt2.setString(1,bank_cd );
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									bank_cd=rset2.getString(1);
								}
								else {
									bank_cd="null";
								}
								rset2.close();
								stmt2.close();
								}
					    		data=bank_cd;
					    		
							}
					    	else if(cell.getColumnIndex() == 43) {
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
//								System.out.println(i+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								cd=rset.getString(2);
								seq_no=rset.getString(3);
								sq_rev_no=rset.getString(4);
								gx=rset.getString(5);
								lg_sq_no=rset.getString(6);
						
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2,seq_no );
							    st_del.setString(3,sq_rev_no);
							    st_del.setString(4,gx);
							    st_del.setString(5, lg_sq_no);
							
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+seq_no+","+sq_rev_no+","+gx+","+lg_sq_no+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX, LOG_SEQ_NO FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					seq_no=rset.getString(3);
					sq_rev_no=rset.getString(4);
					gx=rset.getString(5);
					lg_sq_no=rset.getString(6);
					
					
				    logger.data(fname, (company_cd+","+cd+","+seq_no+","+sq_rev_no+","+gx+","+lg_sq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<LOG_FMS_SECURITY_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<LOG_FMS_SECURITY_MST()>>,,,,,", conn);
			
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

//	FMS_SUPPLY_CONT_DCQ_DTL
	public void FMS_SUPPLY_CONT_DCQ_DTL () throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_CONT_DCQ_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_CONT_DCQ_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_CONT_DCQ_DTL>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SUPPLY_CONT_DCQ_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE =? AND SEQ_NO = ?";
			String agmt_no, agmt_rev, cont_no, cont_rev, cont_type, seq_no,cont_ref;
						
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,DCQ,REMARK,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";
			
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_DCQ_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_DCQ_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SEQ_NO FROM FMS_SUPPLY_CONT_DCQ_DTL WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						seq_no="";  agmt_no=""; agmt_rev=""; cont_no=""; cont_rev=""; cont_type=""; cont_ref="";				
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
								cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							
							else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 6) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
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
//								System.out.println(i+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								cd=rset.getString(2);
								agmt_no=rset.getString(3);
								agmt_rev=rset.getString(4);
								cont_no=rset.getString(5);
								cont_rev=rset.getString(6);
								cont_type=rset.getString(7);
								seq_no=rset.getString(8);
						
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2,agmt_no);
							    st_del.setString(3,agmt_rev);
							    st_del.setString(4,cont_no);
							    st_del.setString(5, cont_rev);
							    st_del.setString(6, cont_type);
							    st_del.setString(7, seq_no);
							
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+seq_no+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SEQ_NO FROM FMS_SUPPLY_CONT_DCQ_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					agmt_no=rset.getString(3);
					agmt_rev=rset.getString(4);
					cont_no=rset.getString(5);
					cont_rev=rset.getString(6);
					cont_type=rset.getString(7);
					seq_no=rset.getString(8);
					
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_CONT_DCQ_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_CONT_DCQ_DTL()>>,,,,,,,", conn);
			
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

//FMS_SUPPLY_CONT_PLANT_CHRG	
	public void FMS_SUPPLY_CONT_PLANT_CHRG () throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_CONT_PLANT_CHRG()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_CONT_PLANT_CHRG()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_CONT_PLANT_CHRG>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SUPPLY_CONT_PLANT_CHRG WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE =? AND PLANT_SEQ_NO = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CHARGE_ABBR =?";
			String agmt_no, agmt_rev, cont_no, cont_rev, cont_type, plant_seq_no,cont_ref,chrg_abbr,eff_dt;
						
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,EFF_DT,CHARGE_ABBR,CHARGE_RATE,"
					+"ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY";
			
			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT_CHRG_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT_CHRG_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,EFF_DT,CHARGE_ABBR, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'),CHARGE_ABBR FROM FMS_SUPPLY_CONT_PLANT_CHRG WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						plant_seq_no="";  agmt_no=""; agmt_rev=""; cont_no=""; cont_rev=""; cont_type=""; cont_ref="";chrg_abbr="";eff_dt="";				
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
								cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							
							else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 6) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
					    	}
							
					    	else if(cell.getColumnIndex() == 9) {
					    		chrg_abbr=(cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!chrg_abbr.equals("null")) {
					    			chrg_abbr = chrg_abbr.substring(1, chrg_abbr.length() - 1);
								}
								query_String="SELECT CHARGE_ABBR FROM FMS_CHARGE_MST WHERE CHARGE_NAME = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1,chrg_abbr );
					    		rset = stmt.executeQuery();
					    		while(rset.next()) {
					    			chrg_abbr=rset.getString(1);
					    		}
					    		rset.close();
					    		stmt.close();				    		
					    		data = chrg_abbr;
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
//								System.out.println(i+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								cd=rset.getString(2);
								agmt_no=rset.getString(3);
								agmt_rev=rset.getString(4);
								cont_no=rset.getString(5);
								cont_rev=rset.getString(6);
								cont_type=rset.getString(7);
								plant_seq_no=rset.getString(8);
								eff_dt=rset.getString(9);
								chrg_abbr=rset.getString(10);
						
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2,agmt_no);
							    st_del.setString(3,agmt_rev);
							    st_del.setString(4,cont_no);
							    st_del.setString(5, cont_rev);
							    st_del.setString(6, cont_type);
							    st_del.setString(7, plant_seq_no);
							    st_del.setString(8, eff_dt);
							    st_del.setString(9, chrg_abbr);
							
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+plant_seq_no+","+eff_dt+","+chrg_abbr+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'),CHARGE_ABBR FROM FMS_SUPPLY_CONT_PLANT_CHRG WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					 company_cd=rset.getString(1);
						cd=rset.getString(2);
						agmt_no=rset.getString(3);
						agmt_rev=rset.getString(4);
						cont_no=rset.getString(5);
						cont_rev=rset.getString(6);
						cont_type=rset.getString(7);
						plant_seq_no=rset.getString(8);
						eff_dt=rset.getString(9);
						chrg_abbr=rset.getString(10);
					
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+plant_seq_no+","+eff_dt+","+chrg_abbr+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_CONT_PLANT_CHRG()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_CONT_PLANT_CHRG()>>,,,,,,,", conn);
			
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
	

//	LOG_SUPPLY_CONT_MST
	public void LOG_SUPPLY_CONT_MST () throws SQLException, IOException {
		function_nm = "LOG_SUPPLY_CONT_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<LOG_SUPPLY_CONT_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<LOG_SUPPLY_CONT_MST>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM LOG_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE =? AND LOG_SEQ_NO = ?";
			String agmt_no, agmt_rev, cont_no, cont_rev, cont_type, lg_seq_no,cont_ref;
			String cont_name,agmt_base,agmt_type,price_appr,price_req;
						
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO,LOG_BY,LOG_DT,CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,"
					+"AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,"
					+"SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,CONT_STATUS,"
					+"IS_ALLOCATED,DDA_DT,DDA_TIME,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,TXN_CHARGE,BUYER_NOM_CUTOFF,TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_CLOSE,TCQ_REQUEST_QTY,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,CHANGE_DATE_REQ";

			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/LOG_SUPPLY_CONT_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/LOG_SUPPLY_CONT_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO FROM LOG_SUPPLY_CONT_MST WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						lg_seq_no="";  agmt_no=""; agmt_rev=""; cont_no=""; cont_rev=""; cont_type=""; cont_ref="";
						cont_name=""; agmt_base=""; agmt_type=""; price_appr=""; price_req="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
								cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							
							else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,AGMT_BASE,"
					    					+ "AGMT_TYPE,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG FROM FMS_SUPPLY_CONT_MST WHERE "
					    					+ "COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
						    			cont_name = rset.getString(6);
						    			agmt_base = rset.getString(7);
						    			agmt_type = rset.getString(8);
						    			price_req = rset.getString(9);
						    			price_appr = rset.getString(10);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    			cont_name = "";
						    			agmt_base = "";
						    			agmt_type = "";
						    			price_req = "null";
						    			price_appr = "null";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    						    		
					    		data  = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 6) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
					    	}
					    	
					    	else if(cell.getColumnIndex() == 10) {
					    		if (!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    			query_String = "SELECT CONT_NAME,AGMT_BASE,AGMT_TYPE,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = 'S'";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			cont_name = rset.getString(1);
						    			agmt_base = rset.getString(2);
						    			agmt_type = rset.getString(3);
						    			price_req = rset.getString(4);
						    			price_appr = rset.getString(5);
						    		}else {
						    			cont_name = "";
						    			agmt_base = "";
						    			agmt_type = "";
						    			price_req = "null";
						    			price_appr = "null";
						    			
						    		}
					    		}
					    		
					    		data = cont_name;				    	
					    	}
					    	else if(cell.getColumnIndex() == 17) {
					    		data = agmt_base+"";
					    	}
					    	else if(cell.getColumnIndex() == 18) {	
					    		data = agmt_type+"";
					    	}
					    	else if(cell.getColumnIndex() == 63) {
					    		data = price_req+"";
					    	}
					    	else if(cell.getColumnIndex() == 64) {
					    		data = price_appr+"";
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
//								System.out.println(column.split(",")[i]+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								cd=rset.getString(2);
								agmt_no=rset.getString(3);
								agmt_rev=rset.getString(4);
								cont_no=rset.getString(5);
								cont_rev=rset.getString(6);
								cont_type=rset.getString(7);
								lg_seq_no=rset.getString(8);
						
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2,agmt_no);
							    st_del.setString(3,agmt_rev);
							    st_del.setString(4,cont_no);
							    st_del.setString(5, cont_rev);
							    st_del.setString(6, cont_type);
							    st_del.setString(7, lg_seq_no);
							
							    st_del.executeUpdate();
//							    logger_count++;
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+lg_seq_no+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO FROM LOG_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					agmt_no=rset.getString(3);
					agmt_rev=rset.getString(4);
					cont_no=rset.getString(5);
					cont_rev=rset.getString(6);
					cont_type=rset.getString(7);
					lg_seq_no=rset.getString(8);
					
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+lg_seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<LOG_SUPPLY_CONT_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<LOG_SUPPLY_CONT_MST()>>,,,,,,,", conn);
			
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
	
//FMS_SUPPLY_PURCHASE_MAP_DTL
	public void FMS_SUPPLY_PURCHASE_MAP_DTL () throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_PURCHASE_MAP_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_PURCHASE_MAP_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_PURCHASE_MAP_DTL>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE =? AND PUR_CONT_NO = ?";
			String agmt_no, agmt_rev, cont_no, cont_rev, cont_type,cont_ref,rate_unit,pur_cont_no,new_pur_cont_no;
						
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PUR_CONT_NO,ALLOC_QTY,"
					+ "QTY_UNIT,SALE_PRICE,RATE_UNIT,COST_PRICE,CP_UNIT,MARGIN,TOTAL_MARGIN,STATUS,ENT_BY,ENT_DT,MODIFY_BY,"
					+ "MODIFY_DT,APRV_BY,APRV_DT,AVG_MARGIN,AUTH_BY,AUTH_DT,CARGO_NO";

			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_PURCHASE_MAP_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_PURCHASE_MAP_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PUR_CONT_NO FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
				        agmt_no=""; agmt_rev=""; cont_no=""; cont_rev=""; cont_type=""; cont_ref="";rate_unit="";
				        pur_cont_no="";new_pur_cont_no="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
								cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							
							else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		stmt.setString(3, cont_ref.split("-")[0]);//.equals("X") ? "I" : cont_ref.split("-")[0]
						    		
						    		
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
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	
					    	else if(cell.getColumnIndex()==4) {
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
								data = cont_no;
					    	}
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 6) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}
					    		}
					    		data = cont_type;
					    	}

			    				else if(cell.getColumnIndex()==7) { //pur_cont_no
			    					pur_cont_no = cell.getStringCellValue().contains("null") ? "" : cell.getStringCellValue();
					    			if (!pur_cont_no.equals("null")) {
					    				pur_cont_no = pur_cont_no.substring(1, pur_cont_no.length() - 1);
									}
					    			
				    				String queryString="";
									if(pur_cont_no.contains("-") && pur_cont_no.contains("D")) {
				    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE TRADE_REF_NO LIKE ?";
				    					stmt = conn.prepareStatement(queryString);
				    					stmt.setString(1, "%"+pur_cont_no+"%");
							    		rset = stmt.executeQuery();
							    		if(rset.next()) {
							    			new_pur_cont_no = rset.getString(1);
							    		}else {
							    			new_pur_cont_no = "";
							    		}
							    		rset.close();
							    		stmt.close();
				    				}
				    				else if (pur_cont_no.contains("-") && pur_cont_no.contains("I")){
				    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ?";
				    					stmt = conn.prepareStatement(queryString);
				    					stmt.setString(1, "%"+pur_cont_no+"%");
							    		rset = stmt.executeQuery();
							    		if(rset.next()) {
							    			new_pur_cont_no = rset.getString(1);
							    		}else {
							    			new_pur_cont_no = "";
							    		}
							    		rset.close();
							    		stmt.close();
				    				}else {
				    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ?";
				    					stmt = conn.prepareStatement(queryString);
				    					stmt.setString(1, pur_cont_no);
							    		rset = stmt.executeQuery();
							    		if(rset.next()) {
							    			new_pur_cont_no = rset.getString(1);
							    		}else {
							    			new_pur_cont_no = "";
							    		}
							    		rset.close();
							    		stmt.close();
				    				}
						    		data = 	new_pur_cont_no;	
			    				}
					    	
			    				else if (cell.getColumnIndex() == 11) {	//rate_unit
						    		rate_unit = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
					    			if (!rate_unit.equals("null")) {
					    				rate_unit = rate_unit.substring(1, rate_unit.length() - 1);
					    				data=rate_unit.trim();
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
								//System.out.println(column.split(",")[i]+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								cd=rset.getString(2);
								agmt_no=rset.getString(3);
								agmt_rev=rset.getString(4);
								cont_no=rset.getString(5);
								cont_rev=rset.getString(6);
								cont_type=rset.getString(7);
								new_pur_cont_no=rset.getString(8);
						
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2,agmt_no);
							    st_del.setString(3,agmt_rev);
							    st_del.setString(4,cont_no);
							    st_del.setString(5, cont_rev);
							    st_del.setString(6, cont_type);
							    st_del.setString(7, new_pur_cont_no);
							
							    st_del.executeUpdate();
//							    logger_count++;
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PUR_CONT_NO FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					agmt_no=rset.getString(3);
					agmt_rev=rset.getString(4);
					cont_no=rset.getString(5);
					cont_rev=rset.getString(6);
					cont_type=rset.getString(7);
					new_pur_cont_no=rset.getString(8);
					
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_PURCHASE_MAP_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_PURCHASE_MAP_DTL()>>,,,,,,", conn);
			
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
	
//FMS_SUPPLY_ALLOC_REVISED
	public void FMS_SUPPLY_ALLOC_REVISED () throws SQLException, IOException {
		function_nm = "FMS_SUPPLY_ALLOC_REVISED()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SUPPLY_ALLOC_REVISED()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SUPPLY_ALLOC_REVISED>>,,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SUPPLY_ALLOC_REVISED WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE =?  AND MODIFICATION_SEQ_NO = ? AND PUR_CONT = ?";
			String agmt_no, agmt_rev, cont_no, cont_rev, cont_type,cont_ref,mod_sq_no,pur_cont_no,new_pur_cont_no;
						
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,MODIFICATION_SEQ_NO,PUR_CONT,NEW_PRICE_EFF_DT,"
					+ "ORI_SALE_PRICE,NEW_SALE_PRICE,ORI_MARGIN,NEW_MARGIN,ORI_AVG_MARGIN,NEW_AVG_MARGIN,ORI_TOT_MARGIN,NEW_TOT_MARGIN,"
					+ "ENT_BY,ENT_DT,APPROVE_BY,APPROVE_DT,FLAG,REMARK,ALLOC_QTY,CARGO_NO";

			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_ALLOC_REVISED_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_ALLOC_REVISED_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,MODIFICATION_SEQ_NO,PUR_CONT, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,MODIFICATION_SEQ_NO,PUR_CONT FROM FMS_SUPPLY_ALLOC_REVISED WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
				        agmt_no=""; agmt_rev=""; cont_no=""; cont_rev=""; cont_type=""; cont_ref="";mod_sq_no="";
				        pur_cont_no="";new_pur_cont_no="";

						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
								cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							
							else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		stmt.setString(3, cont_ref.split("-")[0]);//.equals("X") ? "I" : cont_ref.split("-")[0]
						    		
						    		
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
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
						    	
					    	else if(cell.getColumnIndex()==4) {
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
								data = cont_no;
					    	}
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 6) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}
					    		}
					    		data = cont_type;
					    	}
					    	
					    	
			    				else if(cell.getColumnIndex()==8) { //pur_cont_no
			    					pur_cont_no = cell.getStringCellValue().contains("null") ? "" : cell.getStringCellValue();
					    			if (!pur_cont_no.equals("null")) {
					    				pur_cont_no = pur_cont_no.substring(1, pur_cont_no.length() - 1);
									}
					    			
				    				String queryString="";
									if(pur_cont_no.contains("-") && pur_cont_no.contains("D")) {
				    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE TRADE_REF_NO LIKE ?";
				    					stmt = conn.prepareStatement(queryString);
				    					stmt.setString(1, "%"+pur_cont_no+"%");
							    		rset = stmt.executeQuery();
							    		if(rset.next()) {
							    			new_pur_cont_no = rset.getString(1);
							    		}else {
							    			new_pur_cont_no = "";
							    		}
							    		rset.close();
							    		stmt.close();
				    				}
				    				else if (pur_cont_no.contains("-") && pur_cont_no.contains("I")){
				    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ?";
				    					stmt = conn.prepareStatement(queryString);
				    					stmt.setString(1, "%"+pur_cont_no+"%");
							    		rset = stmt.executeQuery();
							    		if(rset.next()) {
							    			new_pur_cont_no = rset.getString(1);
							    		}else {
							    			new_pur_cont_no = "";
							    		}
							    		rset.close();
							    		stmt.close();
				    				}else {
				    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ?";
				    					stmt = conn.prepareStatement(queryString);
				    					stmt.setString(1, pur_cont_no);
							    		rset = stmt.executeQuery();
							    		if(rset.next()) {
							    			new_pur_cont_no = rset.getString(1);
							    		}else {
							    			new_pur_cont_no = "";
							    		}
							    		rset.close();
							    		stmt.close();
				    				}
						    		data = 	new_pur_cont_no;	
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
//								System.out.println(column.split(",")[i]+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								cd=rset.getString(2);
								agmt_no=rset.getString(3);
								agmt_rev=rset.getString(4);
								cont_no=rset.getString(5);
								cont_rev=rset.getString(6);
								cont_type=rset.getString(7);
								mod_sq_no=rset.getString(8);
								new_pur_cont_no=rset.getString(9);
						
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2,agmt_no);
							    st_del.setString(3,agmt_rev);
							    st_del.setString(4,cont_no);
							    st_del.setString(5, cont_rev);
							    st_del.setString(6, cont_type);
							    st_del.setString(7, mod_sq_no);
							    st_del.setString(8, new_pur_cont_no);
							
							    st_del.executeUpdate();
//								    logger_count++;
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+mod_sq_no+","+new_pur_cont_no+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,MODIFICATION_SEQ_NO,PUR_CONT FROM FMS_SUPPLY_ALLOC_REVISED WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					agmt_no=rset.getString(3);
					agmt_rev=rset.getString(4);
					cont_no=rset.getString(5);
					cont_rev=rset.getString(6);
					cont_type=rset.getString(7);
					mod_sq_no=rset.getString(8);
					new_pur_cont_no=rset.getString(9);
					
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+mod_sq_no+","+new_pur_cont_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_SUPPLY_ALLOC_REVISED()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SUPPLY_ALLOC_REVISED()>>,,,,,,,,", conn);
			
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

//FMS_INVOICE_MST
	public void FMS_INVOICE_MST () throws SQLException, IOException {
		function_nm = "FMS_INVOICE_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_INVOICE_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_INVOICE_MST>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND FINANCIAL_YEAR = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE =?  AND BU_UNIT = ? AND BU_STATE_TIN = ? AND PLANT_SEQ = ? AND INVOICE_SEQ = ?";
			String agmt_no, agmt_rev, cont_no, cont_rev, cont_type,cont_ref,bu_seq_no;
			String fin_yr,state_code,plant_seq_no, inv_seq,bu_cont_person_cd,name,exchg_cd;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,"
					+ "PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,ALLOC_QTY,SALE_PRICE,"
					+ "SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,"
					+ "INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,REMARK_1,REMARK_2,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,"
					+ "AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,EXCHG_RATE_TYPE,MODIFY_BY,MODIFY_DT,TCS_TDS,TCS_AMT,TCS_FACTOR,"
					+ "INVOICE_ID_SEQ,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,TDS_GROSS_PERCENT,"
					+ "TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,"
					+ "TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,SAP_APPROVAL,SAP_APPROVED_BY,"
					+ "SAP_APPROVED_DT,TCS_STRUCT_CD,TCS_EFF_DT,TDS_STRUCT_CD,TDS_EFF_DT,MARKET_MARGIN,OTHER_CHARGES,MARKET_MARGIN_AMT,OTHER_CHARGES_AMT";

			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD,FINANCIAL_YEAR, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN, PLANT_SEQ,INVOICE_SEQ FROM FMS_INVOICE_MST WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
				        agmt_no=""; agmt_rev=""; cont_no=""; cont_rev=""; cont_type=""; cont_ref="";bu_seq_no="";plant_seq_no="";inv_seq="";
				        fin_yr="";state_code="";bu_cont_person_cd="";name="";exchg_cd="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
								cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							
							else if (cell.getColumnIndex() == 3) { //Agmt_no
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 5) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 6) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 7) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
					    	}
					    					    	
					    	else if(cell.getColumnIndex() == 8) { // BU_SEQ
					    		bu_seq_no = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
					    		if (!bu_seq_no.equals("null")) {
									bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
								}
								data  = bu_seq_no;
					    	}
					    	else if(cell.getColumnIndex() == 10) { //BU_CONTACT_PERSON
					    		query_String="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
					    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y'";
								stmt=conn.prepareStatement(query_String);
								
								String addr_flag = "";
								if(bu_seq_no.equals("1")) {								
									addr_flag = "P1";
								}else if(bu_seq_no.equals("2")){
									addr_flag = "P2";
								}		
								
								stmt.setString(1, addr_flag);
								rset = stmt.executeQuery();
								
					    		if (rset.next()) {				    			
					    			bu_cont_person_cd=rset.getString(1);
					    		}else {
					    			bu_cont_person_cd ="0";
					    		}	
					    		
					    		rset.close();
					    		stmt.close();
					    		data=bu_cont_person_cd;
					    	}
					    	else if(cell.getColumnIndex() == 11) { // PLANT_SEQ
					    		query_String = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_PLANT WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
					    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, agmt_no);
					    		stmt.setString(3, agmt_rev);
					    		stmt.setString(4, cont_no);
					    		stmt.setString(5, cont_rev);
					    		stmt.setString(6, cont_type);
					    		rset = stmt.executeQuery();
					    		
					    		if (rset.next()) {
					    			plant_seq_no = rset.getString(1);
					    		}else if(!rset.next()){
					    			plant_seq_no = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
					    			if (!plant_seq_no.equals("null")) {
										plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
									}
					    		}
					    		rset.close();
					    		stmt.close();	
					    		data  = plant_seq_no;
					    	}
					    	else if(cell.getColumnIndex() == 24) {
					    		name = cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue();
					    		if (!name.equals("null")) {
									name = name.substring(1, name.length() - 1);
								}
								query_String = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
						    	stmt = conn.prepareStatement(query_String);
						    	stmt.setString(1, name);
						    	rset = stmt.executeQuery();
						    	if (rset.next()) {
						    		exchg_cd = rset.getString(1);
						    	}
						    	rset.close();
						    	stmt.close();
						    	data = exchg_cd;
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
//								System.out.println(i+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								cd=rset.getString(2);
								fin_yr=rset.getString(3);
								agmt_no=rset.getString(4);
								agmt_rev=rset.getString(5);
								cont_no=rset.getString(6);
								cont_rev=rset.getString(7);
								cont_type=rset.getString(8);
								bu_seq_no=rset.getString(9);
								state_code=rset.getString(10);
								plant_seq_no=rset.getString(11);
								inv_seq=rset.getString(12);
						
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, fin_yr);
							    st_del.setString(3,agmt_no);
							    st_del.setString(4,agmt_rev);
							    st_del.setString(5,cont_no);
							    st_del.setString(6, cont_rev);
							    st_del.setString(7, cont_type);
							    st_del.setString(8, bu_seq_no);
							    st_del.setString(9, state_code);
							    st_del.setString(10, plant_seq_no);
							    st_del.setString(11, inv_seq);
							    st_del.executeUpdate();
//								    logger_count++;
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+plant_seq_no+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD,FINANCIAL_YEAR, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN, PLANT_SEQ,INVOICE_SEQ FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					fin_yr=rset.getString(3);
					agmt_no=rset.getString(4);
					agmt_rev=rset.getString(5);
					cont_no=rset.getString(6);
					cont_rev=rset.getString(7);
					cont_type=rset.getString(8);
					bu_seq_no=rset.getString(9);
					state_code=rset.getString(10);
					plant_seq_no=rset.getString(11);
					inv_seq=rset.getString(12);
					
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+plant_seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_INVOICE_MST()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_INVOICE_MST()>>,,,,,", conn);
			
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

//FMS_INVOICE_DTL
	public void FMS_INVOICE_DTL () throws SQLException, IOException {
		function_nm = "FMS_INVOICE_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_INVOICE_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_INVOICE_DTL>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_INVOICE_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND FINANCIAL_YEAR = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE =?  AND BU_UNIT = ? AND BU_STATE_TIN = ? AND PLANT_SEQ = ? AND INVOICE_SEQ = ? AND ALLOCATION_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss')";
			String agmt_no, agmt_rev, cont_no, cont_rev, cont_type,cont_ref,bu_seq_no;
			String fin_yr,state_code,plant_seq_no, inv_seq,sale_price_unit,sale_price,alloc_dt;
			
			column = "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,PLANT_SEQ,INVOICE_SEQ,ALLOCATION_DT,DAILY_QTY,"
					+"SALE_PRICE,SALE_PRICE_UNIT,AMT_INR,AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_VALUE,ENT_BY,ENT_DT,FLAG,MODIFY_BY,MODIFY_DT";

			data1 = new String[column.split(",").length]; 
			file1 = new File(migration_setup_dir + "EXPORT/FMS_INVOICE_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD,FINANCIAL_YEAR, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO, TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN, PLANT_SEQ,INVOICE_SEQ, TO_CHAR(ALLOCATION_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_INVOICE_DTL WHERE COMPANY_CD='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
				        agmt_no=""; agmt_rev=""; cont_no=""; cont_rev=""; cont_type=""; cont_ref="";bu_seq_no="";plant_seq_no="";inv_seq="";
				        fin_yr="";state_code="";sale_price_unit="";sale_price_unit="";alloc_dt="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if (cell.getColumnIndex() == 0) {	// Company_cd 
								abbr = data;
					    	    data = company_cd;
					    	}

							else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
								cont_ref = data;
					    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(query_String);
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
							
							else if (cell.getColumnIndex() == 3) { //Agmt_no
					    		agmt_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
					    		if (!agmt_no.equals("null")) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			query_String = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(query_String);
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
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		agmt_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!agmt_rev.equals("null")){
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (cell.getColumnIndex() == 5) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_no = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_no.equals("null")) {
						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 6) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_rev = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_rev.equals("null")) {
						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 7) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
						    		cont_type = (cell.getStringCellValue().contains("null") ? "null" : cell.getStringCellValue());
						    		if (!cont_type.equals("null")) {
						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
									}	
					    		}
					    		data = cont_type;
					    	}
					    					    	
					    	else if(cell.getColumnIndex() == 8) { // BU_SEQ
					    		query_String = "SELECT BU_UNIT FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
					    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, agmt_no);
					    		stmt.setString(3, agmt_rev);
					    		stmt.setString(4, cont_no);
					    		stmt.setString(5, cont_rev);
					    		stmt.setString(6, cont_type);
					    		rset = stmt.executeQuery();
					    		
					    		if (rset.next()) {
					    			bu_seq_no = rset.getString(1);
					    		}else {
					    			bu_seq_no = "";
					    		}

					    		rset.close();
					    		stmt.close();		    		
					    		data  = bu_seq_no;
					    	}
					    	
					    	else if(cell.getColumnIndex() == 10) { // PLANT_SEQ
					    		query_String = "SELECT PLANT_SEQ FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
					    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, agmt_no);
					    		stmt.setString(3, agmt_rev);
					    		stmt.setString(4, cont_no);
					    		stmt.setString(5, cont_rev);
					    		stmt.setString(6, cont_type);
					    		rset = stmt.executeQuery();
					    		
					    		if (rset.next()) {
					    			plant_seq_no = rset.getString(1);
					    		}else {
					    			plant_seq_no = "";
					    		}
					    		rset.close();
					    		stmt.close();	
					    		data  = plant_seq_no;
					    	}
					    	else if(cell.getColumnIndex() == 14) { // SALE_PRICE
					    		query_String = "SELECT SALE_PRICE FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
					    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, agmt_no);
					    		stmt.setString(3, agmt_rev);
					    		stmt.setString(4, cont_no);
					    		stmt.setString(5, cont_rev);
					    		stmt.setString(6, cont_type);
					    		rset = stmt.executeQuery();
					    		
					    		if (rset.next()) {
					    			sale_price = rset.getString(1);
					    		}else {
					    			sale_price = "";
					    		}
					    		rset.close();
					    		stmt.close();	
					    		data  = sale_price;
					    	}
					    	else if(cell.getColumnIndex() == 15) { // SALE_PRICE_UNIT
					    		query_String = "SELECT SALE_PRICE_UNIT FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
					    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, agmt_no);
					    		stmt.setString(3, agmt_rev);
					    		stmt.setString(4, cont_no);
					    		stmt.setString(5, cont_rev);
					    		stmt.setString(6, cont_type);
					    		rset = stmt.executeQuery();
					    		
					    		if (rset.next()) {
					    			sale_price_unit = rset.getString(1);
					    		}else {
					    			sale_price_unit = "";
					    		}
					    		rset.close();
					    		stmt.close();	
					    		data  = sale_price_unit;
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
//								System.out.println(i+"::>>data>>:: "+data);
								i++;
					            
							} 
							
						}
						//while(column) completed
						
						query_select = query_select.substring(0, query_select.length() - 4);
			            if(!cd.equals("")) 
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
								cd=rset.getString(2);
								fin_yr=rset.getString(3);
								agmt_no=rset.getString(4);
								agmt_rev=rset.getString(5);
								cont_no=rset.getString(6);
								cont_rev=rset.getString(7);
								cont_type=rset.getString(8);
								bu_seq_no=rset.getString(9);
								state_code=rset.getString(10);
								plant_seq_no=rset.getString(11);
								inv_seq=rset.getString(12);
								alloc_dt=rset.getString(13);
						
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, fin_yr);
							    st_del.setString(3,agmt_no);
							    st_del.setString(4,agmt_rev);
							    st_del.setString(5,cont_no);
							    st_del.setString(6, cont_rev);
							    st_del.setString(7, cont_type);
							    st_del.setString(8, bu_seq_no);
							    st_del.setString(9, state_code);
							    st_del.setString(10, plant_seq_no);
							    st_del.setString(11, inv_seq);
							    st_del.setString(12, alloc_dt);
							    st_del.executeUpdate();
//								    logger_count++;
							    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+plant_seq_no+","), conn, "");
							    logger_count++;   
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD,FINANCIAL_YEAR, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN, PLANT_SEQ,INVOICE_SEQ, TO_CHAR(ALLOCATION_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_INVOICE_DTL WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					fin_yr=rset.getString(3);
					agmt_no=rset.getString(4);
					agmt_rev=rset.getString(5);
					cont_no=rset.getString(6);
					cont_rev=rset.getString(7);
					cont_type=rset.getString(8);
					bu_seq_no=rset.getString(9);
					state_code=rset.getString(10);
					plant_seq_no=rset.getString(11);
					inv_seq=rset.getString(12);
					alloc_dt=rset.getString(13);
					
					
				    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type+","+plant_seq_no+","), conn, "N");
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
			System.out.println("<<ROLLBACK_END>><<FMS_INVOICE_DTL()>>");
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_INVOICE_DTL()>>,,,,,", conn);
			
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
