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

public class Purchase_SEIPL_Data_RollBack 
{
	String db_src_file_name = "Purchase_SEIPL_Data_RollBack.java";
	String migration_setup_dir = "";
	String sysDateTime = "";
	String checked_values = "", msg = "", msg_type = "";
	
	String function_nm = "";
	String start_end_dt = null;
	
	PreparedStatement stmt,st_del,stmt1,stmt2;
	ResultSet rset,rset1,rset2;
	
	Connection conn;
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
	String query_select = "", query_delete = "",query_fetch_columnname = "",query_data_left = "",query_String="",query_String1="",query_String2="";
	
	int logger_count = 0;
	String[] data1 = null;
	
	DataMigration_Logger logger = new DataMigration_Logger();
	
	
	
	public void init() {
		function_nm = "init()";
		try {

			fname= "DataLogs/RollBack/Purchase_SEIPL_Data_RollBack(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/RollBack/Purchase_SEIPL_Data_RollBack_Error(log)"+sysDateTime+".csv";
			
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
	    		
				if (conn != null && !checked_values.equals(""))
				{
					conn.setAutoCommit(false);
//					if (checked_values.contains("FMS_TRADER_CONT_BU")) {
//						FMS_TRADER_CONT_BU();
//					}
					
					if (checked_values.contains("FMS_TRADER_CONT_MST")) {
						FMS_TRADER_CONT_MST();
					}
					
					if (checked_values.contains("FMS_BUY_CARGO_ALLOC_BOE,")) {
						FMS_BUY_CARGO_ALLOC_BOE();
					}
					
					if (checked_values.contains("FMS_BUY_CARGO_ALLOC_BL,")) {
						FMS_BUY_CARGO_ALLOC_BL();
					}
					
					if (checked_values.contains("FMS_BUY_CARGO_ALLOC,")) {
						FMS_BUY_CARGO_ALLOC();
					}
					
					if (checked_values.contains("FMS_BUY_CARGO_NOM_BOE,")) {
						FMS_BUY_CARGO_NOM_BOE();
					}
					
					if (checked_values.contains("FMS_BUY_CARGO_NOM_BL,")) {
						FMS_BUY_CARGO_NOM_BL();
					}
					
					if (checked_values.contains("FMS_BUY_CARGO_NOM,")) {
						FMS_BUY_CARGO_NOM();
					}
					
					if (checked_values.contains("FMS_CARGO_SVC_CONT_SVC_BU,")) {
						FMS_CARGO_SVC_CONT_SVC_BU();
						FMS_CARGO_SVC_CONT_BU();
					}
					
					if (checked_values.contains("FMS_CARGO_SVC_CONT_MST,")) {
						FMS_CARGO_SVC_CONT_MST();
					}
					
					if (checked_values.contains("FMS_CONT_PRICE_MIN_DTL,")) {
						FMS_CONT_PRICE_MIN_DTL();
					}
					
					if (checked_values.contains("FMS_CONT_PRICE_DTL,")) {
						FMS_CONT_PRICE_DTL();
					}
					
					if (checked_values.contains("LOG_FMS_SECURITY_MST,")) {
						LOG_FMS_SECURITY_MST();
					}
					
					if (checked_values.contains("FMS_SECURITY_MST1")) {
						FMS_SECURITY_MST1();
					}
					
					if (checked_values.contains("FMS_SECURITY_DEAL_MAP")) {
						FMS_SECURITY_DEAL_MAP();
					}
					
					if (checked_values.contains("FMS_TRADER_CARGO_MST")) {
						FMS_TRADER_BILLING_DTL1();
						FMS_TRADER_CARGO_MST();
					}
					
					if (checked_values.contains("FMS_TRADER_CN_MST")) {
						FMS_TRADER_CONT_PLANT1();
						FMS_TRADER_CONT_BU1();
						FMS_TRADER_CN_MST();
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
				if (st_del != null) {try {st_del.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if (stmt1 != null) {try {stmt1.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset != null){try{rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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
	
	public void FMS_TRADER_CN_MST() throws SQLException, IOException {
		function_nm = "FMS_TRADER_CN_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRADER_CN_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRADER_CN_MST>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_TRADER_CN_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ?  AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CONT_REF_NO = ? ";
			
			String  agmt_type,agmt_no="",agmt_rev="", cont_rev="", contract_type="",cont_ref_no = "";
		
			
			column = "COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_BASE,AGMT_TYP,CONTRACT_TYPE,CONT_REV,CONT_REF_NO,CONT_STATUS,DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,REV_DT,NUM_CARGO,DAY_DEF_FLAG,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,DEMURRAGE,DEMURRAGE_CLAUSE,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,FCC_FLAG,FCC_BY,FCC_DATE";
			data1 = new String[column.split(",").length];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_CN_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_CN_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_REV,CONTRACT_TYPE,CONT_REF_NO FROM FMS_TRADER_CN_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						agmt_type="";agmt_no="";agmt_rev=""; cont_rev=""; contract_type="";cont_ref_no = "";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if(cell.getColumnIndex() == 0) 
							{
								abbr=data;
								 
								 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
								 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,abbr);
						 		 rset = stmt.executeQuery(); 
						 		 if(rset.next())
						 		 {
						 		 cd=rset.getString(1);
						 		 }
						 		 else
						 		 { cd="";
						 		 }
						 		 stmt.close();
						 		 rset.close();
						 		 data=cd;
						 		 
							}
							else if(cell.getColumnIndex() == 1 || cell.getColumnIndex() == 8  || cell.getColumnIndex() == 10) {
								data=null;
							}
							else if(cell.getColumnIndex() == 3) {
								
								query_String = "SELECT AGMT_NO FROM FMS_TRADER_AGMT_MST WHERE COUNTERPARTY_CD = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			
					    		}else {
					    			agmt_no = "null";
					    		}
					    		
					    		rset.close();
					    		stmt.close();				    		
					    		data = agmt_no;	
					    		
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
						        cont_rev=rset.getString(6);
						        contract_type=rset.getString(7);
						        cont_ref_no=rset.getString(8);

							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_type);
							    st_del.setString(3, agmt_no);
							    st_del.setString(4, agmt_rev);
							    st_del.setString(5, contract_type);
							    st_del.setString(6, cont_rev);
							    st_del.setString(7, cont_ref_no);
							  
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+contract_type+","+cont_rev+","+cont_ref_no+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
			
				// Data left
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_REV,CONTRACT_TYPE,CONT_REF_NO FROM FMS_TRADER_CN_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_type=rset.getString(3);
			        agmt_no=rset.getString(4);
			        agmt_rev=rset.getString(5);
			        cont_rev=rset.getString(6);
			        contract_type=rset.getString(7);
			        cont_ref_no=rset.getString(8);
					
					logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+contract_type+","+cont_rev+","+cont_ref_no+","), conn, "N");
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
			
			
			System.out.println("<<ROLLBACK_END>><<FMS_TRADER_CN_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRADER_CN_MST()>>,,,,,,,", conn);
			
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
	public void FMS_TRADER_CONT_PLANT1() throws SQLException, IOException {
		function_nm = "FMS_TRADER_CONT_PLANT1()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRADER_CONT_PLANT1()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRADER_CONT_PLANT1>>,", conn);
		
//			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_TRADER_CONT_PLANT WHERE COMPANY_CD = ? AND CONTRACT_TYPE = 'N' ";
			
			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE";
	
		    st_del = conn.prepareStatement(query_delete);
		    st_del.setString(1, company_cd);

		    st_del.executeUpdate();
		    logger_count++;

		    st_del.close();

			System.out.println("<<ROLLBACK_END>><<FMS_TRADER_CONT_PLANT1()>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRADER_CONT_PLANT1()>>,", conn);
			
//			logger.checkpoint1(fname1,logger_count+",", conn);

		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}
	
	public void FMS_TRADER_CONT_BU1() throws SQLException, IOException {
		function_nm = "FMS_TRADER_CONT_BU()";
		try {
//			column=" ";
			logger_count=0;
			System.out.println("<<ROLLBACK_START>><<FMS_TRADER_CONT_BU()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRADER_CONT_BU>>,,,,,,,", conn);
			
//			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_TRADER_CONT_BU WHERE COMPANY_CD = ? AND CONTRACT_TYPE = 'N' ";
			st_del = conn.prepareStatement(query_delete);
			st_del.setString(1, company_cd);
			st_del.executeUpdate();
							  
		   	st_del.close();
			  
			msg = "Data has been Rollbacked Successfully from Database.";
			msg_type = "S";
				
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_TRADER_CONT_BU()>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRADER_CONT_BU()>>,,,,,,,", conn);
			
//			logger.checkpoint1(fname1,logger_count+",", conn);

		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}

	public void FMS_TRADER_CARGO_MST() throws SQLException, IOException {
		function_nm = "FMS_TRADER_CARGO_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRADER_CARGO_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRADER_CARGO_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_TRADER_CARGO_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CARGO_NO = ? AND CARGO_REF = ?";

			String  agmt_type="",agmt_no="",agmt_rev="", cont_rev="", contract_type="",cargo_no = "",cargo_ref="";
		

			column = "COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, CONTRACT_TYPE, CONT_REV, CARGO_NO, CARGO_REF, CARGO_STATUS,CARGO_QTY,RATE,RATE_UNIT, START_DT, END_DT, ENT_DT, ENT_BY, MODIFY_DT, MODIFY_BY, TOLERANCE";
			data1 = new String[column.split(",").length];
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_CARGO_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_CARGO_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,CONTRACT_TYPE, CONT_REV, CARGO_NO,CARGO_REF,TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,CONTRACT_TYPE, CONT_REV, CARGO_NO,CARGO_REF FROM FMS_TRADER_CARGO_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
				
						agmt_type="";agmt_no="";agmt_rev=""; cont_rev=""; contract_type="";cargo_no = "";cargo_ref="";
						
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if(cell.getColumnIndex() == 0) 
							{
								abbr=data;
								 
								 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
								 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,abbr);
						 		 rset = stmt.executeQuery(); 
						 		 if(rset.next())
						 		 {
						 		 cd=rset.getString(1);
						 		 }
						 		 else
						 		 { cd="";
						 		 }
						 		 stmt.close();
						 		 rset.close();
						 		 data=cd;
						 		 
							}
							else if(cell.getColumnIndex() == 1 || cell.getColumnIndex() == 6  ) {
								data=null;
							}
							else if(cell.getColumnIndex() == 11)
							{
								data = String.format("%.2f", Double.parseDouble(data));
							}
							else if(cell.getColumnIndex() == 12)
							{
								data = String.format("%.4f", Double.parseDouble(data));
							}
							
							else if(cell.getColumnIndex() == 3) {
								
								query_String = "SELECT AGMT_NO FROM FMS_TRADER_AGMT_MST WHERE COUNTERPARTY_CD = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    		
					    		}else {
					    			agmt_no = "null";
					    		}
					    		
					    		rset.close();
					    		stmt.close();				    		
					    		data = agmt_no;	
					    		
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
						        contract_type=rset.getString(6);
						        cont_rev=rset.getString(7);
						        cargo_no=rset.getString(8);
						        cargo_ref=rset.getString(9);

							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, agmt_type);
							    st_del.setString(3, agmt_no);
							    st_del.setString(4, agmt_rev);
							    st_del.setString(5, contract_type);
							    st_del.setString(6, cont_rev);
							    st_del.setString(7, cargo_no);
							    st_del.setString(8, cargo_ref);
							    
							  
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+contract_type+","+cont_rev+","+cargo_no+","+cargo_ref+","), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV,CONTRACT_TYPE, CONT_REV, CARGO_NO,CARGO_REF FROM FMS_TRADER_CARGO_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
			        cd=rset.getString(2);
			        agmt_type=rset.getString(3);
			        agmt_no=rset.getString(4);
			        agmt_rev=rset.getString(5);
			        contract_type=rset.getString(6);
			        cont_rev=rset.getString(7);
			        cargo_no=rset.getString(8);
			        cargo_ref=rset.getString(9);
			      
					
					logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+contract_type+","+cont_rev+","+cargo_no+","+cargo_ref+","), conn, "N");
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
			
			
			System.out.println("<<ROLLBACK_END>><<FMS_TRADER_CARGO_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRADER_CARGO_MST()>>,,,,,,,", conn);
			
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
	
	public void FMS_TRADER_BILLING_DTL1() throws SQLException, IOException {
		function_nm = "FMS_TRADER_BILLING_DTL1()";
		try {
//			column=" ";
//			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_TRADER_BILLING_DTL1()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRADER_BILLING_DTL1>>,", conn);
		
//			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			query_delete = "DELETE FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE = 'N' ";
			
//			column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE";
	
		    st_del = conn.prepareStatement(query_delete);
		    st_del.setString(1, company_cd);

		    st_del.executeUpdate();
//		    logger_count++;

		    st_del.close();

			System.out.println("<<ROLLBACK_END>><<FMS_TRADER_BILLING_DTL1()>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRADER_BILLING_DTL1()>>,", conn);
			
//			logger.checkpoint1(fname1,logger_count+",", conn);

		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}
							   
	public void FMS_SECURITY_DEAL_MAP() throws SQLException, IOException {

		function_nm = "FMS_SECURITY_DEAL_MAP()";
		try {
			column=" ";
			logger_count=0;
			
			
			System.out.println("<<ROLLBACK_START>><<FMS_SECURITY_DEAL_MAP()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SECURITY_DEAL_MAP>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			query_delete = "DELETE FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND MAP_SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = ?";

			String  seq_no="",mapseq_no="",seqrev_no="", gx="";
			String cont_ref_no="",agmt_rev="",cont_type="",cont_rev="";
			int no=0;
			column = "COUNTERPARTY_CD, SEQ_NO, MAP_SEQ_NO, SEC_REF_NO, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, ENT_BY, ENT_DT, MODIFY_BY, MODIFY_DT, APRV_BY, APRV_DT, SEQ_REV_NO, GX, ENTITY_CD, SHARE_PERCENT";
			data1 = new String[column.split(",").length];
//			System.out.println("=>"+data1.length);
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, MAP_SEQ_NO, SEQ_REV_NO, GX,TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, MAP_SEQ_NO, SEQ_REV_NO, GX FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = '2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						seq_no="";mapseq_no="";seqrev_no=""; gx="";
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if(cell.getColumnIndex() == 0) 
							{
								abbr=data;
								 
								 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
								 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,abbr);
						 		 rset = stmt.executeQuery(); 
						 		 if(rset.next())
						 		 {
						 		 cd=rset.getString(1);
						 		 }
						 		 else
						 		 { cd="";
						 		 }
						 		 stmt.close();
						 		 rset.close();
						 		 data=cd;
						 	
							}
							else if (cell.getColumnIndex() == 1) {	// cont_ref
					    		
				    			cont_ref_no = cell.getStringCellValue();
				    			cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length()-1);	
					    		
					    		query_String = "SELECT AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_TRADER_CN_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? ";
					    		stmt = conn.prepareStatement(query_String);
					    		stmt.setString(1, cont_ref_no);
					    		stmt.setString(2, cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_type = rset.getString(3);
					    			no = rset.getInt(4);
					    			cont_rev =  rset.getString(5);
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = null;
					    	}
							else if(cell.getColumnIndex() == 5) {
								data = agmt_no;
							}
							else if(cell.getColumnIndex() == 6) {
								data = agmt_rev;
							}
							else if(cell.getColumnIndex() == 7) {
								data = no+"";
							}
							else if(cell.getColumnIndex() == 8) {
								data = cont_rev;
							}
							else if(cell.getColumnIndex() == 9) {
								data = cont_type;
							}
								
							
							if (data!= null) 
							{  
//								System.out.println(i);
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
						        mapseq_no=rset.getString(4);
						        seqrev_no=rset.getString(5);
						        gx=rset.getString(6);

							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, seq_no);
							    st_del.setString(3, mapseq_no);
							    st_del.setString(4, seqrev_no);
							    st_del.setString(5, gx);
							  
							    st_del.executeUpdate();
							  
							    logger.data(fname, (company_cd+","+cd+","+seq_no+","+mapseq_no+","+seqrev_no+","+gx+","), conn, "");
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
			        mapseq_no=rset.getString(4);
			        seqrev_no=rset.getString(5);
			        gx=rset.getString(6);
			      
					
					logger.data(fname, (company_cd+","+cd+","+seq_no+","+mapseq_no+","+seqrev_no+","+gx+","), conn, "N");
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
			
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SECURITY_DEAL_MAP()>>,,,,,,,", conn);
			
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
	
			public void FMS_SECURITY_MST1() throws SQLException, IOException {
					function_nm = "FMS_SECURITY_MST1()";
					try {
						column=" ";
						logger_count=0;

						System.out.println("<<ROLLBACK_START>><<FMS_SECURITY_MST1()>>");
						
						logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SECURITY_MST1>>,,", conn);
						
						logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

						query_delete = "DELETE FROM FMS_SECURITY_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = ?";

						String  seq_no, seq_rev_no, gx;
						int no=0;
						String cont_ref_no="",sec_ref_no="";
						
					
						column = "COUNTERPARTY_CD, SEQ_NO,SEC_REF_NO, SEC_CATEGORY,SEC_TYPE, DEAL_TYPE,GUARANTOR_CD, CURRENCY,VARIATION_VALUE,VALUE, VALUE_FLUC,ISS_BANK_CD, ISS_BANK_REF, ADV_BANK_CD, ADV_BANK_REF, CONFIRM_BANK_CD, CONFIRM_BANK_REF, RECEIPT_DT, ISSUE_DT, EXPIRE_DT, RENEW_DT, CANCEL_DT,"
								+ "TENOR, REVIEW_DT, STATUS, REMARKS, FLAG, INORDER_HIST, ENT_BY, ENT_DT, MODIFY_DT, MODIFY_BY, APRV_DT, APRV_BY, SEQ_REV_NO, GX, SAP_APPROVAL, SAP_APPROVED_BY, SAP_APPROVED_DT, SAP_REVERSAL, SAP_REVERSAL_BY, SAP_REVERSAL_DT, PG_REF ";
						data1 = new String[column.split(",").length]; 
//						System.out.println("=>"+data1.length);
						file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_MST1_"+start_end_dt+".xlsx");
						if (file1.exists()) 
						{
							file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECURITY_MST1_"+start_end_dt+".xlsx"));
							workbook = new XSSFWorkbook(file);
							sheet = workbook.getSheetAt(0);
							rowIterator = sheet.iterator();
							rowIterator.next();

							logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO ,GX, TIMESTAMP", conn);
							while (rowIterator.hasNext())
							{
									query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX FROM FMS_SECURITY_MST WHERE COMPANY_CD='2' AND ";
									row = rowIterator.next();
									cellIterator = row.cellIterator();
									
									data = "";cd="";
									int i = 0;
									seq_no=""; seq_rev_no=""; gx="";
									while (cellIterator.hasNext()) 
									{
										cell = cellIterator.next();
										data = cell.getStringCellValue();
										data = data.substring(1, data.length() - 1);			

										if(cell.getColumnIndex() == 0) 
										{
											abbr=data;
											 
											 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
											 stmt=conn.prepareStatement(query_String);
									 		 stmt.setString(1,abbr);
									 		 rset = stmt.executeQuery(); 
									 		 if(rset.next())
									 		 {
									 		 cd=rset.getString(1);
									 		 }
									 		 else
									 		 { cd="";
									 		 }
									 		 stmt.close();
									 		 rset.close();
									 		 data=cd;
									 		 
										}
										else if(cell.getColumnIndex() == 1  ) {
											cont_ref_no = data;
								    		
								    		query_String = "SELECT CONT_NO FROM FMS_TRADER_CN_MST WHERE CONT_REF_NO = ?  AND COUNTERPARTY_CD = ? ";
								    		stmt = conn.prepareStatement(query_String);
								    		stmt.setString(1, cont_ref_no);
								    		stmt.setString(2, cd);
								    		rset = stmt.executeQuery();
								    		if (rset.next()) {				    							    								    		
								    			no = rset.getInt(1);				    			
								    		}
								    		rset.close();
								    		stmt.close();
								    		
								    		query_String = "SELECT SEC_REF_NO FROM FMS_SECURITY_DEAL_MAP WHERE CONT_NO = ? AND COUNTERPARTY_CD = ? ";
								    		stmt = conn.prepareStatement(query_String);
								    		stmt.setInt(1,no);
								    		stmt.setString(2, cd);
								    		rset = stmt.executeQuery();
								    		if (rset.next()) {	
								    			
								    			sec_ref_no = rset.getString(1);				    			
								    		}
								    		rset.close();
								    		stmt.close();
								    		
											data=null;
										}
										
										else if(cell.getColumnIndex() == 3) {
								    		data = sec_ref_no;	
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
									        seq_rev_no=rset.getString(4);
									        gx=rset.getString(5);
									       
										    st_del = conn.prepareStatement(query_delete);
										    st_del.setString(1, cd);
										    st_del.setString(2, seq_no);
										    st_del.setString(3, seq_rev_no);
										    st_del.setString(4, gx);
										   
										    
										  
										    st_del.executeUpdate();
										  
										    logger.data(fname, (company_cd+","+cd+","+seq_no+","+seq_rev_no+","+gx+","), conn, "");
										    logger_count++;   
										  
										    st_del.close();
									      }

									     rset.close();
									     stmt.close();
						            }
							}//while(row) completed
							
							// Data left
							query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX FROM FMS_SECURITY_MST WHERE COMPANY_CD = '2' ";
							stmt = conn.prepareStatement(query_data_left);
							
							rset = stmt.executeQuery();
							while (rset.next()) {
								company_cd=rset.getString(1);
						        cd=rset.getString(2);
						        seq_no=rset.getString(3);
						        seq_rev_no=rset.getString(4);
						        gx=rset.getString(5);
						        
						      
								
								logger.data(fname, (company_cd+","+cd+","+seq_no+","+seq_rev_no+","+gx+","), conn, "N");
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
						System.out.println("<<ROLLBACK_END>><<FMS_SECURITY_MST1()>>");
						
						
						logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
						logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SECURITY_MST1()>>,,,,,,,", conn);
						
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
				
			public void LOG_FMS_SECURITY_MST() throws SQLException, IOException {
					function_nm = "LOG_FMS_SECURITY_MST()";
					try {
						column="";
						logger_count=0;

						
						System.out.println("<<ROLLBACK_START>><<LOG_FMS_SECURITY_MST()>>");
						
						logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<LOG_FMS_SECURITY_MST>>,,", conn);
						
						logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

						query_delete = "DELETE FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND LOG_SEQ_NO = ? AND GX = ? AND SEQ_REV_NO = ?";

						String  seq_no, seq_rev_no, gx,log_seq_no,g_cd,bank_cd,prev_seq_no;
						int no=0;
						int num=0;
						String prev_abbr="";
						String cont_ref_no="",sec_ref_no="";g_cd="";bank_cd="";prev_seq_no="";
						
					
						column = "COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,"
								+ "ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,"
								+ "MODIFY_BY,APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,LOG_SEQ_NO,LOG_ENT_DT,PG_REF,CR_DR,"
								+ "TDS_AMT,TDS_STRUCT_CD,TDS_EFF_DT";
						data1 = new String[column.split(",").length]; 
//						System.out.println("=>"+data1.length);
						file1 = new File(migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST_"+start_end_dt+".xlsx");
						if (file1.exists()) 
						{
							file = new FileInputStream(new File(migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST_"+start_end_dt+".xlsx"));
							workbook = new XSSFWorkbook(file);
							sheet = workbook.getSheetAt(0);
							rowIterator = sheet.iterator();
							rowIterator.next();

							logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, GX, SEQ_REV_NO, TIMESTAMP", conn);
							while (rowIterator.hasNext())
							{
									query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, GX, SEQ_REV_NO FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD='2' AND ";
									row = rowIterator.next();
									cellIterator = row.cellIterator();
									
									data = "";cd="";
									int i = 0;
									seq_no=""; seq_rev_no=""; gx="";log_seq_no="";
									while (cellIterator.hasNext()) 
									{
										cell = cellIterator.next();
										data = cell.getStringCellValue();
										data = data.substring(1, data.length() - 1);	
//										System.out.println("data:"+data);

										if(cell.getColumnIndex() == 0) 
										{
											abbr=data;
											if(!abbr.equals(prev_abbr))
								    		{
								    			num=1;
								    		}
											 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
											 stmt=conn.prepareStatement(query_String);
									 		 stmt.setString(1,abbr);
									 		 rset = stmt.executeQuery(); 
									 		 if(rset.next())
									 		 {
									 		 cd=rset.getString(1);
									 		 }
									 		 else
									 		 { cd="";
									 		 }
									 		 stmt.close();
									 		 rset.close();
									 		 data=cd;
									 		prev_abbr=abbr;
									 		 
										}
										else if(cell.getColumnIndex() == 1  ) {
											cont_ref_no = data;
								    		
								    		query_String = "SELECT CONT_NO FROM FMS_TRADER_CN_MST WHERE CONT_REF_NO = ?  AND COUNTERPARTY_CD = ? ";
								    		stmt = conn.prepareStatement(query_String);
								    		stmt.setString(1, cont_ref_no);
								    		stmt.setString(2, cd);
								    		rset = stmt.executeQuery();
								    		if (rset.next()) {				    							    								    		
								    			no = rset.getInt(1);				    			
								    		}
								    		rset.close();
								    		stmt.close();
								    		
								    		query_String = "SELECT SEC_REF_NO FROM FMS_SECURITY_DEAL_MAP WHERE CONT_NO = ? AND COUNTERPARTY_CD = ? ";
								    		stmt = conn.prepareStatement(query_String);
								    		stmt.setInt(1,no);
								    		stmt.setString(2, cd);
								    		rset = stmt.executeQuery();
								    		if (rset.next()) {	
								    			
								    			sec_ref_no = rset.getString(1);				    			
								    		}
								    		rset.close();
								    		stmt.close();
								    		
											data=null;
										}
										else if (cell.getColumnIndex() == 2) {	// Seq_no
							    			seq_no=data;
							    			
								    	}
										
										else if(cell.getColumnIndex() == 3) {
								    		data = sec_ref_no;	
										}
										else if (cell.getColumnIndex() == 7) {//GUARANTOR_CD
								    		g_cd = data;
								    		
							    			g_cd = g_cd.substring(1, g_cd.length() - 1);
								    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
								    		stmt = conn.prepareStatement(query_String);
								    		stmt.setString(1, g_cd);
								    		rset = stmt.executeQuery();
								    		if (rset.next()) {
								    			g_cd = rset.getString(1);
								    		} else {
								    			g_cd ="null";
								    		}
								    		rset.close();
								    		stmt.close();
								    		
								    		data=g_cd;
								    		
								    	}
								    	else if (cell.getColumnIndex() == 12  || cell.getColumnIndex() == 14 || cell.getColumnIndex() ==  16) {
								    		bank_cd = data;

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
											
								    		data=bank_cd;
								    		
										}else if(cell.getColumnIndex() ==26)
								    	{
//								    		System.out.println(":"+data+":");
								    	}
								    	else if (cell.getColumnIndex() ==43) {
//											System.out.println("-->"+seq_no+":"+prev_seq_no);
								    		if(abbr.equals(prev_abbr)) {
								    		if(prev_seq_no.equals(seq_no))
								    		{
								    			num++;
								    			prev_seq_no=seq_no;
								    		}
								    		}
								    		else
								    		{
								    			num=1;
								    			prev_seq_no=seq_no;
								    		}
								    		
								    		data=num+"";
								    
										}
								    	
										
										
										if (data!= null) 
										{  
											data1[i] = data;
//											System.out.println("-------->"+i+":"+data);
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
//									System.out.println("->"+query_select);

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
									        log_seq_no=rset.getString(4);
									        gx=rset.getString(5);
									        seq_rev_no=rset.getString(6);
										    st_del = conn.prepareStatement(query_delete);
										    st_del.setString(1, cd);
										    st_del.setString(2, seq_no);
										    st_del.setString(3, log_seq_no);
										    st_del.setString(4, gx);
										    st_del.setString(5, seq_rev_no);
										  
										    st_del.executeUpdate();
										  
										    logger.data(fname, (company_cd+","+cd+","+seq_no+","+log_seq_no+","+gx+","+seq_rev_no+","), conn, "");
										    logger_count++;   
										  
										    st_del.close();
									      }

									     rset.close();
									     stmt.close();
						            }
							}//while(row) completed
							
							// Data left
							query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, LOG_SEQ_NO, GX, SEQ_REV_NO FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = '2' ";
							stmt = conn.prepareStatement(query_data_left);
							
							rset = stmt.executeQuery();
							while (rset.next()) {
								company_cd=rset.getString(1);
						        cd=rset.getString(2);
						        seq_no=rset.getString(3);
						        log_seq_no=rset.getString(4);
						        gx=rset.getString(5);
						        seq_rev_no=rset.getString(6);
						        
								logger.data(fname, (company_cd+","+cd+","+seq_no+","+log_seq_no+","+gx+","+seq_rev_no+","), conn, "N");
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
						
						
						logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
						logger.checkpoint(fname, "<<ROLLBACK_END>>,<<LOG_FMS_SECURITY_MST()>>,,,,,,,", conn);
						
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
//			public void FMS_CONT_PRICE_DTL() throws SQLException, IOException {
//					function_nm = "FMS_CONT_PRICE_DTL()";
//					try {
//						column=" ";
//						logger_count=0;
//						
//						System.out.println("<<ROLLBACK_START>><<FMS_CONT_PRICE_DTL()>>");
//						
//						logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_CONT_PRICE_DTL>>,,,,,,,", conn);
//						
//						logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
//						
//						query_delete = "DELETE FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = '2' AND MAPPING_ID = ? AND CONTRACT_TYPE = ? AND SEQ_NO = ? ";
//						
//						String  map_id,cont_type,seq_no,cont_no,cargo_no,cargo_ref;
//						company_cd="2";
//					
//						column = "MAPPING_ID,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,PRICE_TYPE,CURVE_NM,SLOPE,CONST,QUANTITY_UNIT,RATE,RATE_UNIT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PRICE_RANGE,PRICE_START_DT,"
//								+ "PRICE_END_DT,PHYS_CURVE_NM,CURVE_LOGIC,FORMULA";
//						
//						data1 = new String[column.split(",").length];
//						file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL_"+start_end_dt+".xlsx");
//						if (file1.exists()) 
//						{
//							file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL_"+start_end_dt+".xlsx"));
//							workbook = new XSSFWorkbook(file);
//							sheet = workbook.getSheetAt(0);
//							rowIterator = sheet.iterator();
//							rowIterator.next();
//
//							logger.checkpoint(fname, ",TIMESTAMP", conn);
//							while (rowIterator.hasNext())
//							{
//									query_select = "SELECT COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO FROM FMS_CONT_PRICE_DTL WHERE ";
//									row = rowIterator.next();
//									cellIterator = row.cellIterator();
//									
//									data = "";cd="";
//									int i = 0;
//									map_id="";cont_type="";seq_no="";cont_no="";cargo_no="";cargo_ref="";
//									while (cellIterator.hasNext()) 
//									{
//										cell = cellIterator.next();
//										data = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
//										data = data.substring(1, data.length() - 1);			
//
//										if(cell.getColumnIndex() == 0) 
//										{
//											 abbr=data;
//									 		 data=company_cd;
//										}
//										else if(cell.getColumnIndex() == 1 ) {	// Counterparty_Cd
//								    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
//								    		stmt = conn.prepareStatement(query_String);
//								    		stmt.setString(1, abbr);
//								    		rset = stmt.executeQuery();
//								    		if (rset.next()) {
//								    			map_id = rset.getString(1)+"-"+cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1);
//								    			cd=rset.getString(1);
//								    		}				    		
//								    		rset.close();
//								    		stmt.close();
//								    	
//								    		data=cell.getStringCellValue();
//								    		data = data.substring(1, data.length()-1);
//								    		
//								    		query_String="SELECT CONT_NO,AGMT_NO,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COUNTERPARTY_CD = ? ";
//								    		stmt=conn.prepareStatement(query_String);
//								    		stmt.setString(1, data);
//											stmt.setString(2, cd);
//								    		rset = stmt.executeQuery();
//								    		if (rset.next()) {
//								    			cont_no=rset.getString(1);
//								    			agmt_no=rset.getString(2);
//								    			cargo_no=rset.getString(3);
//								    		}
//								    		rset.close();
//								    		stmt.close();
//								    		
//								    		data=cd+"-"+agmt_no+"-"+cont_no+"-"+cargo_no;
//								    		map_id=data;
//								    		}
//								    	
//										
//																			
//										
//										else if (cell.getColumnIndex() == 23) {	// MULTI_LEG
//											
//								    		if (data.contains("MULTI_LEG@")) {
//								    			if (data.split("@")[1].contains("-") && data.split("@")[2].contains("-")) {
//								    				data = data.split("@")[0] + "@Settled@" + data.split("@")[1].substring(1) + "@" + data.split("@")[2].substring(1);
//								    			}
//								    			else if (!data.split("@")[1].contains("-") && !data.split("@")[2].contains("-")) {
//								    				data = data.split("@")[0] + "@Forward@" + data.split("@")[1] + "@" + data.split("@")[2];
//								    			}
//								    		}
//								    		else {
//							    				data = "null";
//							    			}
//								    	}
//										
//										
//										if (data!= null) 
//										{  
//											data1[i] = data;
//											if (data.equals("null")) {
//										        	 query_select = query_select + column.split(",")[i] + " IS NULL AND ";
//										     } else if (data.split("/").length == 3 && data != null && data.contains(":") && data.length() == 19) {
//										        	 query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
//										     } else {
//										        	 query_select = query_select + column.split(",")[i] + " =? AND ";
//										      }
//											i++;
//										} 
//										
//									}//while(column) completed
//									
//									query_select = query_select.substring(0, query_select.length() - 4);
//								    
//						            if(!cd.equals("")) 
//						            {
//						            	 stmt = conn.prepareStatement(query_select);
//									     int k = 1;
//									     for (int w = 0; w < (column.split(",").length); w++) 
//									     {
//										     if (data1[w] != null && !data1[w].equals("null")) 
//										     {
//											 stmt.setString(k, data1[w]);
//											 k++;
//										     }
//									     }
//									     
//									     rset = stmt.executeQuery();
//									          
//									     if (rset.next())       
//									     {
//									    	 //		"COMPANY_CD", "MAPPING_ID", "CONTRACT_TYPE", "SEQ_NO")
//									    	 company_cd=rset.getString(1);
//										     map_id = rset.getString(2);
//										     cont_type = rset.getString(3);
//										     seq_no = rset.getString(4);
//
//										    st_del = conn.prepareStatement(query_delete);
//										    st_del.setString(1, map_id);
//										    st_del.setString(2, cont_type);
//										    st_del.setString(3, seq_no);
//										  
//										    st_del.executeUpdate();
//										  
//										    logger.data(fname, (company_cd+","+map_id+","+cont_type+","+seq_no+","), conn, "");
//										    logger_count++;   
//										  
//										    st_del.close();
//									      }
//
//									     rset.close();
//									     stmt.close();
//						            }
//							}//while(row) completed
//						
//							// Data left
//							query_data_left = "SELECT COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = '2' ";
//							stmt = conn.prepareStatement(query_data_left);
//							
//							rset = stmt.executeQuery();
//							while (rset.next()) {
//								company_cd=rset.getString(1);
//						        map_id = rset.getString(2);
//						        cont_type = rset.getString(3);
//						        seq_no = rset.getString(4);
//								
//								logger.data(fname, (company_cd+","+map_id+","+cont_type+","+seq_no+","), conn, "N");
//							}
//							rset.close();
//							stmt.close();
//
//
//							msg = "Data has been Rollbacked Successfully from Database.";
//							msg_type = "S";
//						}
//						else {
//							msg = "Excel File not found while Execution. Program Terminated.";
//							msg_type = "E";
//						}
//						conn.commit();
//						
//						
//						System.out.println("<<ROLLBACK_END>><<FMS_CONT_PRICE_DTL()>>");
//						System.out.println();
//						
//						logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
//						logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_CONT_PRICE_DTL()>>,,,,,,,", conn);
//						
//						logger.checkpoint1(fname1,logger_count+",", conn);
//
//					} catch (Exception e) {
//						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//						logger.error(fname, e, function_nm, conn, fname_error);
//						
//						msg = "One of the Functions faced an Error. RollBack Terminated.";
//						msg_type = "E";
//					} finally {
//						if(file !=null)
//						{file.close();}
//					}
//				}
			
			
			public void FMS_CONT_PRICE_DTL() throws SQLException, IOException {
				function_nm = "FMS_CONT_PRICE_DTL()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_CONT_PRICE_DTL()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_CONT_PRICE_DTL>>,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

					query_delete = "DELETE FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = '2' "
							+ "AND MAPPING_ID = ? AND CONTRACT_TYPE = ? AND SEQ_NO = ? ";
					
					String  cont_no="",cargo_no="",map_id="",contract_type="",seq_no="";
					company_cd="2";
				
					column = "COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,PRICE_TYPE,CURVE_NM,SLOPE,CONST,QUANTITY_UNIT,RATE,RATE_UNIT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,PHYS_CURVE_NM,CURVE_LOGIC,FORMULA";
					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();
				
						logger.checkpoint(fname, "COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO,TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
								query_select = "SELECT COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO FROM FMS_CONT_PRICE_DTL WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";cd="";
								int i = 0;
								cont_no="";cargo_no="";map_id="";contract_type="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if(cell.getColumnIndex() == 0) 
									{
										 abbr=data;
										 
										 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
										 stmt=conn.prepareStatement(query_String);
								 		 stmt.setString(1,abbr);
								 		 rset = stmt.executeQuery(); 
								 		 if(rset.next())
								 		 {
								 		 cd=rset.getString(1);
								 		 }
								 		 else
								 		 { cd="";
								 		 }
								 		 stmt.close();
								 		 rset.close();
								 		 data=company_cd;
								 		 
									}
									else if(cell.getColumnIndex() == 1 ) {
//										query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
//							    		stmt = conn.prepareStatement(query_String);
//							    		stmt.setString(1, abbr);
//							    		rset = stmt.executeQuery();
//							    		if (rset.next()) {							    			
//							    			cd=rset.getString(1);
//							    		}
//							    		rset.close();
//							    		stmt.close();
//							    	
//							    		data=cell.getStringCellValue();
//							    		data = data.substring(1, data.length()-1);
//							    		
//							    		query_String="SELECT CONT_NO,AGMT_NO,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ?";
//							    		stmt=conn.prepareStatement(query_String);
//							    		stmt.setString(1, data);
//							    		rset = stmt.executeQuery();
//							    		if (rset.next()) {
//							    			cont_no=rset.getString(1);
//							    			agmt_no=rset.getString(2);
//							    			cargo_no=rset.getString(3);
//							    		}
//							    		rset.close();
//							    		stmt.close();
//							    		
//							    		data=cd+"-"+agmt_no+"-"+cont_no+"-"+cargo_no;
//							    		map_id=data;
										query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, abbr);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
//							    			map_id = rset.getString(1)+"-"+cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1);
							    			cd=rset.getString(1);
							    		}				    		
							    		rset.close();
							    		stmt.close();
							    	
							    		data=cell.getStringCellValue();
							    		data = data.substring(1, data.length()-1);
							    		
							    		query_String="SELECT CONT_NO,AGMT_NO,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COUNTERPARTY_CD = ? ";
							    		stmt=conn.prepareStatement(query_String);
							    		stmt.setString(1, data);
										stmt.setString(2, cd);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cont_no=rset.getString(1);
							    			agmt_no=rset.getString(2);
							    			cargo_no=rset.getString(3);
							    		}
							    		rset.close();
							    		stmt.close();
							    		
							    		data=cd+"-"+agmt_no+"-"+cont_no+"-"+cargo_no;
							    		map_id=data;				
							    		}
									else if (cell.getColumnIndex() == 23) {	// MULTI_LEG
							    		data = cell.getStringCellValue();
								    	if(data != null) {
								    		data = data.substring(1, data.length()-1);
								    	}
							    		if (data.contains("MULTI_LEG@")) {
							    			if (data.split("@")[1].contains("-") && data.split("@")[2].contains("-")) {
							    				data = data.split("@")[0] + "@Settled@" + data.split("@")[1].substring(1) + "@" + data.split("@")[2].substring(1);
							    			}
							    			else if (!data.split("@")[1].contains("-") && !data.split("@")[2].contains("-")) {
							    				data = data.split("@")[0] + "@Forward@" + data.split("@")[1] + "@" + data.split("@")[2];
							    			}
							    		}
										else {
							    			data = null;
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
								        contract_type=rset.getString(3);
								        seq_no=rset.getString(4);

									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1,map_id );
									    st_del.setString(2,contract_type );
									    st_del.setString(3, seq_no);
									    st_del.executeUpdate();
									  
									    logger.data(fname, (company_cd+","+map_id+","+contract_type+","+seq_no+","), conn, "");
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
					        map_id = rset.getString(2);
					        contract_type = rset.getString(3);
					        seq_no = rset.getString(4);
					        logger.data(fname, (company_cd+","+map_id+","+contract_type+","+seq_no+","), conn, "N");
						
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_CONT_PRICE_DTL()>>");
					System.out.println();
					
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
				public void FMS_CONT_PRICE_MIN_DTL() throws SQLException, IOException {
					function_nm = "FMS_CONT_PRICE_MIN_DTL()";
					try {
						column=" ";
						logger_count=0;
						
						System.out.println("<<ROLLBACK_START>><<FMS_CONT_PRICE_MIN_DTL()>>");
						
						logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_CONT_PRICE_MIN_DTL>>,,,,", conn);
						
						logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

						query_delete = "DELETE FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = '2' AND MAPPING_ID =? AND CONTRACT_TYPE=? AND  SEQ_NO= ? AND CURVE_NM =? ";
						
//						("COMPANY_CD", "MAPPING_ID", "CONTRACT_TYPE", "SEQ_NO", "CURVE_NM")
						String  map_id="",contract_type="",seq_no="",curve_nm="",cont_no="",cargo_no="";
						company_cd="2";
					
						column = "COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,CURVE_LOGIC,CURVE_NM,SLOPE,CONST,QUANTITY_UNIT,RATE,RATE_UNIT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
						data1 = new String[column.split(",").length];
						file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL_"+start_end_dt+".xlsx");
						if (file1.exists()) 
						{
							file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL_"+start_end_dt+".xlsx"));
							workbook = new XSSFWorkbook(file);
							sheet = workbook.getSheetAt(0);
							rowIterator = sheet.iterator();
							rowIterator.next();
					
							logger.checkpoint(fname, "COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO, CURVE_NM,TIMESTAMP", conn);
							while (rowIterator.hasNext())
							{
									query_select = "SELECT COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO , CURVE_NM FROM FMS_CONT_PRICE_MIN_DTL WHERE ";
									row = rowIterator.next();
									cellIterator = row.cellIterator();
									
									data = "";cd="";
									int i = 0;
									map_id="";contract_type="";seq_no="";curve_nm="";cont_no="";cargo_no="";
									while (cellIterator.hasNext()) 
									{
										cell = cellIterator.next();
										data = cell.getStringCellValue();
										data = data.substring(1, data.length() - 1);			

										if(cell.getColumnIndex() == 0) 
										{
											 abbr=data;
											 
											 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
											 stmt=conn.prepareStatement(query_String);
									 		 stmt.setString(1,abbr);
									 		 rset = stmt.executeQuery(); 
									 		 if(rset.next())
									 		 {
									 		 cd=rset.getString(1);
									 		 }
									 		 else
									 		 { cd="";
									 		 }
									 		 stmt.close();
									 		 rset.close();
									 		 data=company_cd;
									 		 
										}
										else if(cell.getColumnIndex() == 1 ) {
											
											query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
								    		stmt = conn.prepareStatement(query_String);
								    		stmt.setString(1, abbr);
								    		rset = stmt.executeQuery();
								    		if (rset.next()) {							    			
								    			cd=rset.getString(1);
								    		}
								    		rset.close();
								    		stmt.close();
								    	
								    		data=cell.getStringCellValue();
								    		data = data.substring(1, data.length()-1);
								    		
								    		query_String="SELECT CONT_NO,AGMT_NO,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ?";
								    		stmt=conn.prepareStatement(query_String);
								    		stmt.setString(1, data);
								    		rset = stmt.executeQuery();
								    		if (rset.next()) {
								    			cont_no=rset.getString(1);
								    			agmt_no=rset.getString(2);
								    			cargo_no=rset.getString(3);
								    		}
								    		rset.close();
								    		stmt.close();
								    		
								    		data=cd+"-"+agmt_no+"-"+cont_no+"-"+cargo_no;
								    		map_id=data;
										}
										else if (cell.getColumnIndex() == 16) {	// MULTI_LEG
								    		data = cell.getStringCellValue();
									    	if(data != null) {
									    		data = data.substring(1, data.length()-1);
									    	}
								    		if (data.contains("MULTI_LEG@")) {
								    			if (data.split("@")[1].contains("-") && data.split("@")[2].contains("-")) {
								    				data = data.split("@")[0] + "@Settled@" + data.split("@")[1].substring(1) + "@" + data.split("@")[2].substring(1);
								    			}
								    			else if (!data.split("@")[1].contains("-") && !data.split("@")[2].contains("-")) {
								    				data = data.split("@")[0] + "@Forward@" + data.split("@")[1] + "@" + data.split("@")[2];
								    			}
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
									        contract_type=rset.getString(3);
									        seq_no=rset.getString(4);
									        curve_nm=rset.getString(5);
									        
										    st_del = conn.prepareStatement(query_delete);
										    st_del.setString(1,map_id );
										    st_del.setString(2,contract_type );
										    st_del.setString(3,seq_no);
										    st_del.setString(4, curve_nm);
										    st_del.executeUpdate();
										  
										    logger.data(fname, (company_cd+","+map_id+","+contract_type+","+seq_no+","+curve_nm+","), conn, "");
										    logger_count++;   
										  
										    st_del.close();
									      }

									     rset.close();
									     stmt.close();
						            }
							}//while(row) completed
						
							// Data left
							query_data_left = "SELECT COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = '2' ";
							stmt = conn.prepareStatement(query_data_left);
							
							rset = stmt.executeQuery();
							while (rset.next()) {
								st_del.setString(1,map_id );
							    st_del.setString(2,contract_type );
							    st_del.setString(3,seq_no);
							    st_del.setString(4, curve_nm);
						        
						        logger.data(fname, (company_cd+","+map_id+","+contract_type+","+seq_no+","+curve_nm+","), conn, "N");
							
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
						
						
						System.out.println("<<ROLLBACK_END>><<FMS_CONT_PRICE_MIN_DTL()>>");
						System.out.println();
						
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

			public void FMS_CARGO_SVC_CONT_MST() throws SQLException, IOException {
				function_nm = "FMS_CARGO_SVC_CONT_MST()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_CARGO_SVC_CONT_MST()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_CARGO_SVC_CONT_MST>>,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
					
					query_delete = "DELETE FROM FMS_CARGO_SVC_CONT_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ENTITY_TYPE = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? ";
					
					String  ent_type,cont_no,cont_type;
					company_cd="2";
				
					column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,CONTRACT_TYPE,CONT_STATUS,START_DT,END_DT,PROV_SVC_RATE,PROV_SVC_RATE_UNIT1,"
							+ "PROV_SVC_RATE_UNIT2,FINAL_SVC_RATE,FINAL_SVC_RATE_UNIT1,FINAL_SVC_RATE_UNIT2,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,SIGNING_DT,SIGNING_TIME";
					
					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_MST_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_MST_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, ",TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
							
								query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY_TYPE, CONT_NO, CONTRACT_TYPE FROM FMS_CARGO_SVC_CONT_MST WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";
								int i = 0;
								ent_type="";cont_no="";cont_type="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if(cell.getColumnIndex() == 0) 
									{
										 abbr=data;
								 		 data=company_cd;
									}
									else if(cell.getColumnIndex() == 1 ) {
										query_String = "SELECT A.COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.COMPANY_CD = '2' AND B.ENTITY = ? AND A.COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, abbr.substring(0, 1));
							    		stmt.setString(2, abbr.substring(1));
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		
							    		rset.close();
							    		stmt.close();
							    		data = cd;
							    	}

									else if(cell.getColumnIndex() == 3 || cell.getColumnIndex() == 5 || cell.getColumnIndex() == 6) {
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
								    	 //		"COMPANY_CD", "COUNTERPARTY_CD" , "ENTITY_TYPE" , "CONT_NO" , "CONTRACT_TYPE"
								    	 company_cd=rset.getString(1);
									     cd = rset.getString(2);
									     ent_type = rset.getString(3);
									     cont_no = rset.getString(4);
									     cont_type = rset.getString(5);

									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1, cd);
									    st_del.setString(2, ent_type);
									    st_del.setString(3, cont_no);
									    st_del.setString(4, cont_type);
									  
									    st_del.executeUpdate();
									  
									    logger.data(fname, (company_cd+","+cd+","+ent_type+","+cont_no+","+cont_type+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
					
						// Data left
						query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,CONT_NO,CONTRACT_TYPE FROM FMS_CARGO_SVC_CONT_MST WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
					        cd = rset.getString(2);
					        ent_type = rset.getString(3);
					        cont_no = rset.getString(4);
					        cont_type = rset.getString(5);
							
							logger.data(fname, (company_cd+","+cd+","+ent_type+","+cont_no+","+cont_type+","), conn, "N");
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_CARGO_SVC_CONT_MST()>>");
					System.out.println();
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_CARGO_SVC_CONT_MST()>>,,,,,,,", conn);
					
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

			public void FMS_CARGO_SVC_CONT_SVC_BU() throws SQLException, IOException {
				function_nm = "FMS_CARGO_SVC_CONT_SVC_BU()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_CARGO_SVC_CONT_SVC_BU()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_CARGO_SVC_CONT_SVC_BU>>,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
					
					query_delete = "DELETE FROM FMS_CARGO_SVC_CONT_SVC_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
					
					String  plant_seq_no,cont_no,cont_type;
					company_cd="2";
				
					column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,CONT_NO,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT";
					
					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_SVC_BU_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_SVC_BU_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, ",TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
							
								query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, CONT_NO, CONTRACT_TYPE, PLANT_SEQ_NO FROM FMS_CARGO_SVC_CONT_SVC_BU WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";
								int i = 0;
								plant_seq_no="";cont_no="";cont_type="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if(cell.getColumnIndex() == 0) 
									{
										 abbr=data;
								 		 data=company_cd;
									}
									else if(cell.getColumnIndex() == 1 ) {     		//COUNTERPARTY_CD
										query_String = "SELECT A.COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.COMPANY_CD = '2' AND B.ENTITY = ? AND A.COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, abbr.substring(0, 1));
							    		stmt.setString(2, abbr.substring(1));
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		
							    		rset.close();
							    		stmt.close();
							    		data = cd;
							    	}
									else if(cell.getColumnIndex() == 3) {	//CONT_NO
										query_String = "SELECT CONT_NO,CONTRACT_TYPE FROM FMS_CARGO_SVC_CONT_MST WHERE COUNTERPARTY_CD = ?";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cd);
							    		rset = stmt.executeQuery();
							    		if (rset.next() && rset.getInt(1) > 0) {
							    			cont_no = rset.getString(1);
							    			cont_type = rset.getString(2);
							    		}				    						    		
							    		rset.close();
							    		stmt.close();
							    			
						    			data = cont_no+"";
									}
									else if(cell.getColumnIndex() == 4) {	//CONT_TYPE
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
									
								}//while(column) completed
								
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
								    	 //		"COMPANY_CD", "COUNTERPARTY_CD" , "CONT_NO" , "CONTRACT_TYPE" , "PLANT_SEQ_NO"
								    	 company_cd=rset.getString(1);
									     cd = rset.getString(2);
									     cont_no = rset.getString(3);
									     cont_type = rset.getString(4);
									     plant_seq_no = rset.getString(5);
									     
									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1, cd);
									    st_del.setString(2, cont_no);
									    st_del.setString(3, cont_type);
									    st_del.setString(4, plant_seq_no);
									    
									    st_del.executeUpdate();
									  
									    logger.data(fname, (company_cd+","+cd+","+cont_no+","+cont_type+","+plant_seq_no+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
					
						// Data left
						query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,PLANT_SEQ_NO FROM FMS_CARGO_SVC_CONT_SVC_BU WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
					        cd = rset.getString(2);
					        cont_no = rset.getString(3);
					        cont_type = rset.getString(4);
					        plant_seq_no = rset.getString(5);
							
							logger.data(fname, (company_cd+","+cd+","+cont_no+","+cont_type+","+plant_seq_no+","), conn, "N");
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_CARGO_SVC_CONT_SVC_BU()>>");
					System.out.println();
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_CARGO_SVC_CONT_SVC_BU()>>,,,,,,,", conn);
					
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

			public void FMS_CARGO_SVC_CONT_BU() throws SQLException, IOException {
				function_nm = "FMS_CARGO_SVC_CONT_BU()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_CARGO_SVC_CONT_BU()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_CARGO_SVC_CONT_BU>>,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
					
					query_delete = "DELETE FROM FMS_CARGO_SVC_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
					
					String  plant_seq_no,cont_no,cont_type;
					company_cd="2";
				
					column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,CONT_NO,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT";
					
					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_SVC_BU_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_SVC_BU_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, ",TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
							
								query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, CONT_NO, CONTRACT_TYPE, PLANT_SEQ_NO FROM FMS_CARGO_SVC_CONT_BU WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";
								int i = 0;
								plant_seq_no="";cont_no="";cont_type="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if(cell.getColumnIndex() == 0) 
									{
										 abbr=data;
								 		 data=company_cd;
									}
									else if(cell.getColumnIndex() == 1 ) {     		//COUNTERPARTY_CD
										query_String = "SELECT A.COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.COMPANY_CD = '2' AND B.ENTITY = ? AND A.COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, abbr.substring(0, 1));
							    		stmt.setString(2, abbr.substring(1));
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		
							    		rset.close();
							    		stmt.close();
							    		data = cd;
							    	}
									else if(cell.getColumnIndex() == 3) {		//CONT_NO
										query_String = "SELECT CONT_NO,CONTRACT_TYPE FROM FMS_CARGO_SVC_CONT_MST WHERE COUNTERPARTY_CD = ?";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cd);
							    		rset = stmt.executeQuery();
							    		if (rset.next() && rset.getInt(1) > 0) {
							    			cont_no = rset.getString(1);
							    			cont_type = rset.getString(2);
							    		}				    						    		
							    		rset.close();
							    		stmt.close();
							    			
						    			data = cont_no+"";
									}
									else if(cell.getColumnIndex() == 4) {		//CONT_TYPE
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
									
								}//while(column) completed
								
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
								    	 //		"COMPANY_CD", "COUNTERPARTY_CD" , "CONT_NO" , "CONTRACT_TYPE" , "PLANT_SEQ_NO"
								    	 company_cd=rset.getString(1);
									     cd = rset.getString(2);
									     cont_no = rset.getString(3);
									     cont_type = rset.getString(4);
									     plant_seq_no = rset.getString(5);
									     
									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1, cd);
									    st_del.setString(2, cont_no);
									    st_del.setString(3, cont_type);
									    st_del.setString(4, plant_seq_no);
									    
									    st_del.executeUpdate();
									  
									    logger.data(fname, (company_cd+","+cd+","+cont_no+","+cont_type+","+plant_seq_no+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
					
						// Data left
						query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,CONT_NO,CONTRACT_TYPE,PLANT_SEQ_NO FROM FMS_CARGO_SVC_CONT_BU WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
					        cd = rset.getString(2);
					        cont_no = rset.getString(3);
					        cont_type = rset.getString(4);
					        plant_seq_no = rset.getString(5);
							
							logger.data(fname, (company_cd+","+cd+","+cont_no+","+cont_type+","+plant_seq_no+","), conn, "N");
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_CARGO_SVC_CONT_BU()>>");
					System.out.println();
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_CARGO_SVC_CONT_BU()>>,,,,,,,", conn);
					
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

			public void FMS_BUY_CARGO_NOM() throws SQLException, IOException {
				
				function_nm = "FMS_BUY_CARGO_NOM()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_BUY_CARGO_NOM()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_BUY_CARGO_NOM>>,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
					
					query_delete = "DELETE FROM FMS_BUY_CARGO_NOM WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV =? "
							+ "AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_NO = ? AND NOM_REV = ? ";
					
					String  agmt_type,agmt_no,agmt_rev,cont_type,cont_no,cont_rev,cargo_no,nom_rev,cont_ref_no,ship_cd,cargo_ref_no;
					company_cd="2";
				
					column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,SHIP_CD,EXP_DELV_QTY,EXP_FROM_DT,"
							+ "EXP_TO_DT,COUNTRY_ORIGIN,LOAD_PORT,REMARK,SPLIT_BOL,NUM_BL,NUM_BOE,LIQUEFAC_PLANT,LIQUEFAC_COUNTRY,LIQUEFAC_PROMOTOR,LIQUEFAC_REMARK,"
							+ "ENT_BY,ENT_DT,FLAG,LINKED_SURVEYOR_CONT,LINKED_CHAGENT_CONT,LINKED_VAGENT_CONT";
					
					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, ",TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
							
								query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV FROM FMS_BUY_CARGO_NOM WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";
								int i = 0;
								agmt_type="";agmt_no="";agmt_rev="";cont_type="";cont_no="";cont_rev="";cargo_no="";nom_rev="";cargo_ref_no="";ship_cd="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if(cell.getColumnIndex() == 0) 
									{
										 abbr=data;
								 		 data=company_cd;
									}
									else if(cell.getColumnIndex() == 1 ) {     		//COUNTERPARTY_CD
										
										cargo_ref_no = data;
										query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, abbr);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		
							    		rset.close();
							    		stmt.close();
							    		
							    		data = cd;
							    		
							    		query_String = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ?  AND COUNTERPARTY_CD = ?";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cargo_ref_no);
							    		stmt.setString(2, cd);

							    		rset = stmt.executeQuery();
							    		if (rset.next()) {				    			
							    			agmt_type = rset.getString(1);
							    			agmt_no = rset.getString(2);
							    			agmt_rev = rset.getString(3);
							    			cont_type = rset.getString(4);
							    			cont_no = rset.getString(5);
							    			cont_rev =  rset.getString(6);	
							    			cargo_no =  rset.getString(7);
							    		}
							    		rset.close();
							    		stmt.close();
							    	}
									else if(cell.getColumnIndex() == 2) {	//AGMT_TYPE		    						    						    						    						   			    		
							    		data = agmt_type;
							    	}
							    	else if(cell.getColumnIndex() == 3) {	//AGMT_NO
							    		data = agmt_no;
							    	}
							    	else if(cell.getColumnIndex() == 4) {	//AGMT_REV
							    		data = agmt_rev;
							    	}
							    	else if(cell.getColumnIndex() == 5) {	//CONT_TYPE
							    		data = cont_type;
							    	}				    	
							    	else if (cell.getColumnIndex() == 6) {	//CONT_NO			    		
						    			data = cont_no;	
							    	}
							    	else if (cell.getColumnIndex() == 7) {	//CONT_REV			    		
						    			data = cont_rev;	
							    	}
									else if(cell.getColumnIndex() == 8) {
							    		data = cargo_no;							    		
							    	}
									else if (cell.getColumnIndex() == 10) {		// SHIP_CD
							    		
										
							    		query_String = "SELECT SHIP_CD FROM FMS_SHIP_MST WHERE SHIP_NAME = ?  ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, data);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
								    		ship_cd = rset.getString(1);
							    		}
							    		else  {
							    			ship_cd = "0";
							    		}
							    		rset.close();
							    		stmt.close();
							    		
							    		data  = ship_cd;
							    	}
									
									else if (cell.getColumnIndex() == 14 || cell.getColumnIndex() == 21) {		// COUNTRY_NM

										query_String = "SELECT COUNTRY_NM FROM FMS_COUNTRY_MST WHERE UPPER(COUNTRY_NM) LIKE ? ";
										stmt = conn.prepareStatement(query_String);
										stmt.setString(1, "%"+data+"%");
										rset = stmt.executeQuery();
										if(rset.next()) {
											data = rset.getString(1);
										}
										else if (data != null) {
											data = Camel_Case_Converter(data);
										}
										rset.close();
										stmt.close();
									}
									else if (cell.getColumnIndex() == 27 || cell.getColumnIndex() == 28 || cell.getColumnIndex() == 29) {	// CHA, VA, SURV
							    		
							    		query_String = "SELECT A.COMPANY_CD, A.ENTITY_TYPE, A.COUNTERPARTY_CD, A.CONTRACT_TYPE, A.CONT_NO FROM FMS_CARGO_SVC_CONT_MST A, FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, data);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			data = rset.getString(3)+"-"+rset.getString(4)+'-'+rset.getString(5);
							    		}
							    		else {
							    			data = "null";
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
								    	 //		"COMPANY_CD", "COUNTERPARTY_CD" , "AGMT_TYPE" , "AGMT_NO" , "AGMT_REV" , "CONTRACT_TYPE" , "CONT_NO" , "CONT_REV" , "CARGO_NO" , "NOM_REV"
								    	 company_cd=rset.getString(1);
									     cd = rset.getString(2);
									     agmt_type = rset.getString(3);
									     agmt_no = rset.getString(4);
									     agmt_rev = rset.getString(5);
									     cont_type = rset.getString(6);
									     cont_no = rset.getString(7);
									     cont_rev = rset.getString(8);
									     cargo_no = rset.getString(9);
									     nom_rev = rset.getString(10);
									     
									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1, cd);
									    st_del.setString(2, agmt_type);
									    st_del.setString(3, agmt_no);
									    st_del.setString(4, agmt_rev);
									    st_del.setString(5, cont_type);
									    st_del.setString(6, cont_no);
									    st_del.setString(7, cont_rev);
									    st_del.setString(8, cargo_no);
									    st_del.setString(9, nom_rev);
									    
									    st_del.executeUpdate();
									  
									    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+nom_rev+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
					
						// Data left
						query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV FROM FMS_BUY_CARGO_NOM WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
						     cd = rset.getString(2);
						     agmt_type = rset.getString(3);
						     agmt_no = rset.getString(4);
						     agmt_rev = rset.getString(5);
						     cont_type = rset.getString(6);
						     cont_no = rset.getString(7);
						     cont_rev = rset.getString(8);
						     cargo_no = rset.getString(9);
						     nom_rev = rset.getString(10);
						     
							logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+nom_rev+","), conn, "N");
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_BUY_CARGO_NOM()>>");
					System.out.println();
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_BUY_CARGO_NOM()>>,,,,,,,", conn);
					
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
	
			public void FMS_BUY_CARGO_NOM_BL() throws SQLException, IOException {
				
				function_nm = "FMS_BUY_CARGO_NOM_BL()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_BUY_CARGO_NOM_BL()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_BUY_CARGO_NOM_BL>>,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
					
					query_delete = "DELETE FROM FMS_BUY_CARGO_NOM_BL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV =? "
							+ "AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_NO = ? AND NOM_REV = ? AND BL_NO = ?";
					
					String  agmt_type,agmt_no,agmt_rev,cont_type,cont_no,cont_rev,cargo_no,nom_rev,cargo_ref_no,ship_cd,bl_no;
					company_cd="2";
				
					column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BL_NO,"
							+ "BL_QTY,BL_QTY_UNIT,BL_PRICE,BL_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,BL_QTY_MT,BL_QTY_SCM";
					
					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_BL_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_BL_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, ",TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
							
								query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BL_NO FROM FMS_BUY_CARGO_NOM_BL WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";
								int i = 0;
								agmt_type="";agmt_no="";agmt_rev="";cont_type="";cont_no="";cont_rev="";cargo_no="";nom_rev="";cargo_ref_no="";ship_cd="";bl_no="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if(cell.getColumnIndex() == 0) 
									{
										 abbr=data;
								 		 data=company_cd;
									}
									else if(cell.getColumnIndex() == 1 ) {     		//COUNTERPARTY_CD
										
										cargo_ref_no = data;
										query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, abbr);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		
							    		rset.close();
							    		stmt.close();
							    		
							    		data = cd;
							    		
							    		query_String = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COUNTERPARTY_CD = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cargo_ref_no);
							    		stmt.setString(2, cd);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {				    			
							    			agmt_type = rset.getString(1);
							    			agmt_no = rset.getString(2);
							    			agmt_rev = rset.getString(3);
							    			cont_type = rset.getString(4);
							    			cont_no = rset.getString(5);
							    			cont_rev =  rset.getString(6);
							    			cargo_no =  rset.getString(7);
							    		}
							    		rset.close();
							    		stmt.close();
							    	}
									else if(cell.getColumnIndex() == 2) {	//AGMT_TYPE		    						    						    						    						   			    		
							    		data = agmt_type;
							    	}
							    	else if(cell.getColumnIndex() == 3) {	//AGMT_NO
							    		data = agmt_no;
							    	}
							    	else if(cell.getColumnIndex() == 4) {	//AGMT_REV
							    		data = agmt_rev;
							    	}
							    	else if(cell.getColumnIndex() == 5) {	//CONT_TYPE
							    		data = cont_type;
							    	}				    	
							    	else if (cell.getColumnIndex() == 6) {	//CONT_NO			    		
						    			data = cont_no;	
							    	}
							    	else if (cell.getColumnIndex() == 7) {	//CONT_REV			    		
						    			data = cont_rev;	
							    	}
									else if(cell.getColumnIndex() == 8) {	//CARGO_NO
							    		data = cargo_no;
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
								    	 //		"COMPANY_CD", "COUNTERPARTY_CD" , "AGMT_TYPE" , "AGMT_NO" , "AGMT_REV" , "CONTRACT_TYPE" , "CONT_NO" , "CONT_REV" , "CARGO_NO" , "NOM_REV" , "BL_NO"
								    	 company_cd=rset.getString(1);
									     cd = rset.getString(2);
									     agmt_type = rset.getString(3);
									     agmt_no = rset.getString(4);
									     agmt_rev = rset.getString(5);
									     cont_type = rset.getString(6);
									     cont_no = rset.getString(7);
									     cont_rev = rset.getString(8);
									     cargo_no = rset.getString(9);
									     nom_rev = rset.getString(10);
									     bl_no = rset.getString(11);
									     
									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1, cd);
									    st_del.setString(2, agmt_type);
									    st_del.setString(3, agmt_no);
									    st_del.setString(4, agmt_rev);
									    st_del.setString(5, cont_type);
									    st_del.setString(6, cont_no);
									    st_del.setString(7, cont_rev);
									    st_del.setString(8, cargo_no);
									    st_del.setString(9, nom_rev);
									    st_del.setString(10, bl_no);
									    
									    st_del.executeUpdate();
									  
									    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+nom_rev+","+bl_no+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
					
						// Data left
						query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BL_NO FROM FMS_BUY_CARGO_NOM_BL WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
						     cd = rset.getString(2);
						     agmt_type = rset.getString(3);
						     agmt_no = rset.getString(4);
						     agmt_rev = rset.getString(5);
						     cont_type = rset.getString(6);
						     cont_no = rset.getString(7);
						     cont_rev = rset.getString(8);
						     cargo_no = rset.getString(9);
						     nom_rev = rset.getString(10);
						     bl_no = rset.getString(11);
						     
							logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+nom_rev+","+bl_no+","), conn, "N");
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_BUY_CARGO_NOM_BL()>>");
					System.out.println();
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_BUY_CARGO_NOM_BL()>>,,,,,,,", conn);
					
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
	
			public void FMS_BUY_CARGO_NOM_BOE() throws SQLException, IOException {
				
				function_nm = "FMS_BUY_CARGO_NOM_BOE()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_BUY_CARGO_NOM_BOE()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_BUY_CARGO_NOM_BOE>>,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
					
					query_delete = "DELETE FROM FMS_BUY_CARGO_NOM_BOE WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV =? "
							+ "AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_NO = ? AND NOM_REV = ? AND BOE_NO = ?";
					
					String  agmt_type,agmt_no,agmt_rev,cont_type,cont_no,cont_rev,cargo_no,nom_rev,cargo_ref_no,ship_cd,boe_no,boe_qty;
					company_cd="2";
				
					column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BOE_NO,"
							+ "BU_SEQ,PLANT_SEQ,BOE_QTY,BOE_QTY_UNIT,BOE_PRICE,BOE_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,CUSTOM_DUTY,LOAD_PORT,LINKED_BL,BOE_QTY_MT,BOE_QTY_SCM";
					
					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_BOE_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_BOE_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, ",TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
							
								query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BOE_NO FROM FMS_BUY_CARGO_NOM_BOE WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";
								int i = 0;
								agmt_type="";agmt_no="";agmt_rev="";cont_type="";cont_no="";cont_rev="";cargo_no="";nom_rev="";cargo_ref_no="";ship_cd="";boe_qty="";boe_no="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if(cell.getColumnIndex() == 0) 
									{
										 abbr=data;
								 		 data=company_cd;
									}
									else if(cell.getColumnIndex() == 1 ) {     		//COUNTERPARTY_CD
										
										cargo_ref_no = data;
										query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, abbr);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		
							    		rset.close();
							    		stmt.close();
							    		
							    		data = cd;
							    		
							    		query_String = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cargo_ref_no);
							    		stmt.setString(2, cd);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {				    			
							    			agmt_type = rset.getString(1);
							    			agmt_no = rset.getString(2);
							    			agmt_rev = rset.getString(3);
							    			cont_type = rset.getString(4);
							    			cont_no = rset.getString(5);
							    			cont_rev =  rset.getString(6);	
							    			cargo_no =  rset.getString(7);
							    		}
							    		rset.close();
							    		stmt.close();
							    	}
									else if(cell.getColumnIndex() == 2) {	//AGMT_TYPE		    						    						    						    						   			    		
							    		data = agmt_type;
							    	}
							    	else if(cell.getColumnIndex() == 3) {	//AGMT_NO
							    		data = agmt_no;
							    	}
							    	else if(cell.getColumnIndex() == 4) {	//AGMT_REV
							    		data = agmt_rev;
							    	}
							    	else if(cell.getColumnIndex() == 5) {	//CONT_TYPE
							    		data = cont_type;
							    	}				    	
							    	else if (cell.getColumnIndex() == 6) {	//CONT_NO			    		
						    			data = cont_no;	
							    	}
							    	else if (cell.getColumnIndex() == 7) {	//CONT_REV			    		
						    			data = cont_rev;	
							    	}
									else if(cell.getColumnIndex() == 8) {	//CARGO_NO
							    		data = cargo_no;
							    	}
//									else if (cell.getColumnIndex() == 10) {	// Boe_No
//						    			boe_no = data;
//						    		}
//									else if (cell.getColumnIndex() == 13) {	// BOE_QTY
//										
//							    		query_String = "SELECT BL_QTY FROM FMS_BUY_CARGO_NOM_BL WHERE CONT_NO = ? AND COMPANY_CD = '2'  AND COUNTERPARTY_CD = ? AND BL_NO = ? AND CARGO_NO = ?";
//							    		stmt = conn.prepareStatement(query_String);
//							    		stmt.setString(1, cont_no);
//							    		stmt.setString(2, cd);
//							    		stmt.setString(3, boe_no);
//							    		stmt.setString(4, cargo_no);
//							    		rset = stmt.executeQuery();
//							    		if (rset.next()) {
//							    		
//								    		boe_qty = rset.getString(1);
//								    		if(boe_qty==null)
//								    		{
//								    			boe_qty="null";
//								    		}
//							    		}
//							    		else  {
//							    			boe_qty = "0";
//							    		}
//							    		rset.close();
//							    		stmt.close();
//							    		data = boe_qty;
//							    	}
//									System.out.println(boe_qty);
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
//										System.out.println(i+":::data for i==="+data);
										i++;
									} 
								}//while(column) completed
								
								query_select = query_select.substring(0, query_select.length() - 4);
								 
					            if(!cd.equals("")) 
					            {
					            	
					            	 stmt = conn.prepareStatement(query_select);
								     int k = 1;
								     for (int w = 0; w < (column.split(",").length); w++) 
								     {	
//								    	 System.out.println(w+"<<data1 for w:::"+data1[w]);
								    	 if (data1[w] != null && !data1[w].equals("null")) 
									     {
									    
										 stmt.setString(k, data1[w]);
										 k++;
									     }
								    	 
								     }
								     rset = stmt.executeQuery();
								     
								     
								     
								     if (rset.next())       
								     {
								    	
								    	 //		"COMPANY_CD", "COUNTERPARTY_CD" , "AGMT_TYPE" , "AGMT_NO" , "AGMT_REV" , "CONTRACT_TYPE" , "CONT_NO" , "CONT_REV" , "CARGO_NO" , "NOM_REV" , "BOE_NO"
								    	 company_cd=rset.getString(1);
									     cd = rset.getString(2);
									     agmt_type = rset.getString(3);
									     agmt_no = rset.getString(4);
									     agmt_rev = rset.getString(5);
									     cont_type = rset.getString(6);
									     cont_no = rset.getString(7);
									     cont_rev = rset.getString(8);
									     cargo_no = rset.getString(9);
									     nom_rev = rset.getString(10);
									     boe_no = rset.getString(11);
									     
									     
									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1, cd);
									    st_del.setString(2, agmt_type);
									    st_del.setString(3, agmt_no);
									    st_del.setString(4, agmt_rev);
									    st_del.setString(5, cont_type);
									    st_del.setString(6, cont_no);
									    st_del.setString(7, cont_rev);
									    st_del.setString(8, cargo_no);
									    st_del.setString(9, nom_rev);
									    st_del.setString(10, boe_no);
									   
									    st_del.executeUpdate();
									   
									    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+nom_rev+","+boe_no+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
					
						// Data left
						query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BOE_NO FROM FMS_BUY_CARGO_NOM_BOE WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
						     cd = rset.getString(2);
						     agmt_type = rset.getString(3);
						     agmt_no = rset.getString(4);
						     agmt_rev = rset.getString(5);
						     cont_type = rset.getString(6);
						     cont_no = rset.getString(7);
						     cont_rev = rset.getString(8);
						     cargo_no = rset.getString(9);
						     nom_rev = rset.getString(10);
						     boe_no = rset.getString(11);
						     
							logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+nom_rev+","+boe_no+","), conn, "N");
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_BUY_CARGO_NOM_BOE()>>");
					System.out.println();
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_BUY_CARGO_NOM_BOE()>>,,,,,,,", conn);
					
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
			
			public void FMS_BUY_CARGO_ALLOC() throws SQLException, IOException {
				
				function_nm = "FMS_BUY_CARGO_ALLOC()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_BUY_CARGO_ALLOC()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_BUY_CARGO_ALLOC>>,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
					
					query_delete = "DELETE FROM FMS_BUY_CARGO_ALLOC WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV =? "
							+ "AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_NO = ? AND ALLOC_REV = ? ";
					
					String  agmt_type,agmt_no,agmt_rev,cont_type,cont_no,cont_rev,cargo_no,alloc_rev,ship_cd,cargo_ref_no="";
				
					column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,SHIP_CD,"
							+ "ACT_ARRV_DT,BOOKED_DT,BOOKED_TIME,FLOAT_DT,FLOAT_TIME,PILOT_ON_BOARD_DT,PILOT_ON_BOARD_TIME,UNLOAD_ARM_CON_DT,"
							+ "UNLOAD_ARM_CON_TIME,UNLOAD_ARM_DIS_CON_DT,UNLOAD_ARM_DIS_CON_TIME,DISCHARGE_DT,DISCHARGE_TIME,REMARK,QQ_NO,"
							+ "QQ_DT,QQ_QTY_MMBTU,QQ_QQ_QTY_MT,QQ_QQ_QTY_SCM,QQ_DENSITY,QQ_GHV,QQ_GCV,QQ_REMARK,ACT_QTY_MMBTU,ENT_BY,ENT_DT,"
							+ "ALL_FAST_DT,ALL_FAST_TIME,CUSTOME_CLEARANCE_START_DT,CUSTOME_CLEARANCE_START_TIME,CUSTOME_CLEARANCE_END_DT,CUSTOME_CLEARANCE_END_TIME";
					
					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, ",TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
							
								query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV FROM FMS_BUY_CARGO_ALLOC WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";
								int i = 0;
								agmt_type="";agmt_no="";agmt_rev="";cont_type="";cont_no="";cont_rev="";cargo_no="";alloc_rev="";ship_cd="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if(cell.getColumnIndex() == 0) 
									{
										 abbr = data;
								 		 data=company_cd;
									}
									else if(cell.getColumnIndex() == 1 ) {     		//COUNTERPARTY_CD
										
										cargo_ref_no = data;
										query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, abbr);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		
							    		rset.close();
							    		stmt.close();
							    		
							    		data = cd;
							    		
							    		query_String = "SELECT COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ?";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cargo_ref_no);
							    		stmt.setString(2, cd);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    			agmt_type = rset.getString(2);
							    			agmt_no = rset.getString(3);
							    			agmt_rev = rset.getString(4);
							    			cont_type = rset.getString(5);
							    			cont_no = rset.getString(6);
							    			cont_rev = rset.getString(7);
							    			cargo_no = rset.getString(8);
							    		}
							    		rset.close();
							    		stmt.close();
							    	}
									else if(cell.getColumnIndex() == 2) {	//AGMT_TYPE		    						    						    						    						   			    		
							    		data = agmt_type;
							    	}
							    	else if(cell.getColumnIndex() == 3) {	//AGMT_NO
							    		data = agmt_no;
							    	}
							    	else if(cell.getColumnIndex() == 4) {	//AGMT_REV
							    		data = agmt_rev;
							    	}
							    	else if(cell.getColumnIndex() == 5) {	//CONT_TYPE
							    		data = cont_type;
							    	}				    	
							    	else if (cell.getColumnIndex() == 6) {	//CONT_NO			    		
						    			data = cont_no;	
							    	}
							    	else if (cell.getColumnIndex() == 7) {	//CONT_REV			    		
						    			data = cont_rev;	
							    	}
									else if(cell.getColumnIndex() == 8) {	//CARGO_NO
							    		data = cargo_no;
							    	}
									else if (cell.getColumnIndex() == 10) {	 // SHIP_CD
							    		query_String = "SELECT SHIP_CD FROM FMS_BUY_CARGO_NOM WHERE CONT_NO = ? AND CARGO_NO = ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cont_no);
							    		stmt.setString(2, cargo_no);
							    		stmt.setString(3, cd);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
								    		ship_cd = rset.getString(1);
							    		}
							    		else  {
							    			ship_cd = "0";
							    		}
							    		rset.close();
							    		stmt.close();
							    		data = ship_cd;
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
								     //	"COMPANY_CD", "COUNTERPARTY_CD" , "AGMT_TYPE" , "AGMT_NO" , "AGMT_REV" , "CONTRACT_TYPE" , "CONT_NO" , "CONT_REV" , "CARGO_NO" , "ALLOC_REV" 
								    	 company_cd=rset.getString(1);
									     cd = rset.getString(2);
									     agmt_type = rset.getString(3);
									     agmt_no = rset.getString(4);
									     agmt_rev = rset.getString(5);
									     cont_type = rset.getString(6);
									     cont_no = rset.getString(7);
									     cont_rev = rset.getString(8);
									     cargo_no = rset.getString(9);
									     alloc_rev = rset.getString(10);
									     
									    st_del = conn.prepareStatement(query_delete);
									    
									    st_del.setString(1, cd);
									    st_del.setString(2, agmt_type);
									    st_del.setString(3, agmt_no);
									    st_del.setString(4, agmt_rev);
									    st_del.setString(5, cont_type);
									    st_del.setString(6, cont_no);
									    st_del.setString(7, cont_rev);
									    st_del.setString(8, cargo_no);
									    st_del.setString(9,alloc_rev);
									    st_del.executeUpdate();
									    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+alloc_rev+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
					
						// Data left
						query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV FROM FMS_BUY_CARGO_ALLOC WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
						     cd = rset.getString(2);
						     agmt_type = rset.getString(3);
						     agmt_no = rset.getString(4);
						     agmt_rev = rset.getString(5);
						     cont_type = rset.getString(6);
						     cont_no = rset.getString(7);
						     cont_rev = rset.getString(8);
						     cargo_no = rset.getString(9);
						     alloc_rev = rset.getString(10);
						    
						     
							logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+alloc_rev+","), conn, "N");
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_BUY_CARGO_ALLOC()>>");
					System.out.println();
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_BUY_CARGO_ALLOC()>>,,,,,,,", conn);
					
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
			
			public void FMS_BUY_CARGO_ALLOC_BL() throws SQLException, IOException {
				
				function_nm = "FMS_BUY_CARGO_ALLOC_BL()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_BUY_CARGO_ALLOC_BL()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_BUY_CARGO_ALLOC_BL>>,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
					
					query_delete = "DELETE FROM FMS_BUY_CARGO_ALLOC_BL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV =? "
							+ "AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_NO = ? AND ALLOC_REV = ? AND BL_NO = ?";
					
					String  agmt_type,agmt_no,agmt_rev,cont_type,cont_no,cont_rev,cargo_no,alloc_rev,ship_cd,bl_no,cargo_ref_no;
					;
				
					column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BL_NO,"
							+ "BL_REF,BL_DT,IMPORT_DEPT_SNO,IMPORT_CD,ENDORSE_DT,REMARK,ENT_BY,ENT_DT,FLAG";
					
					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_BL_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_BL_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, ",TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
							
								query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BL_NO FROM FMS_BUY_CARGO_ALLOC_BL WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";
								int i = 0;
								agmt_type="";agmt_no="";agmt_rev="";cont_type="";cont_no="";cont_rev="";cargo_no="";alloc_rev="";ship_cd="";bl_no="";cargo_ref_no="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if(cell.getColumnIndex() == 0) 
									{
										 abbr = data;
								 		 data=company_cd;
									}
									else if(cell.getColumnIndex() == 1 ) {     		//COUNTERPARTY_CD
										
										cargo_ref_no = data;
							    		
							    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, abbr);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		rset.close();
							    		stmt.close();
							    		data = cd;
							    		
							    		query_String = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2' ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cargo_ref_no);
							    		stmt.setString(2, cd);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {				    			
							    			agmt_type = rset.getString(1);
							    			agmt_no = rset.getString(2);
							    			agmt_rev = rset.getString(3);
							    			cont_type = rset.getString(4);
							    			cont_no = rset.getString(5);
							    			cont_rev =  rset.getString(6);	
							    			cargo_no =  rset.getString(7);	
							    		}
							    		rset.close();
							    		stmt.close();
							    	}
									else if(cell.getColumnIndex() == 2) {	//AGMT_TYPE		    						    						    						    						   			    		
							    		data = agmt_type;
							    	}
							    	else if(cell.getColumnIndex() == 3) {	//AGMT_NO
							    		data = agmt_no;
							    	}
							    	else if(cell.getColumnIndex() == 4) {	//AGMT_REV
							    		data = agmt_rev;
							    	}
							    	else if(cell.getColumnIndex() == 5) {	//CONT_TYPE
							    		data = cont_type;
							    	}				    	
							    	else if (cell.getColumnIndex() == 6) {	//CONT_NO			    		
						    			data = cont_no;	
							    	}
							    	else if (cell.getColumnIndex() == 7) {	//CONT_REV			    		
						    			data = cont_rev;	
							    	}
									else if(cell.getColumnIndex() == 8) {	//CARGO_NO
							    		data = cargo_no;
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
								     //	"COMPANY_CD", "COUNTERPARTY_CD" , "AGMT_TYPE" , "AGMT_NO" , "AGMT_REV" , "CONTRACT_TYPE" , "CONT_NO" , "CONT_REV" , "CARGO_NO" , "ALLOC_REV" , "BL_NO"
								    	 company_cd=rset.getString(1);
									     cd = rset.getString(2);
									     agmt_type = rset.getString(3);
									     agmt_no = rset.getString(4);
									     agmt_rev = rset.getString(5);
									     cont_type = rset.getString(6);
									     cont_no = rset.getString(7);
									     cont_rev = rset.getString(8);
									     cargo_no = rset.getString(9);
									     alloc_rev = rset.getString(10);
									     bl_no = rset.getString(11);
									     
									    
									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1, cd);
									    st_del.setString(2, agmt_type);
									    st_del.setString(3, agmt_no);
									    st_del.setString(4, agmt_rev);
									    st_del.setString(5, cont_type);
									    st_del.setString(6, cont_no);
									    st_del.setString(7, cont_rev);
									    st_del.setString(8, cargo_no);
									    st_del.setString(9,alloc_rev);
									    st_del.setString(10, bl_no);
									    st_del.executeUpdate();
									    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+alloc_rev+","+bl_no+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
					
						// Data left
						query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BL_NO FROM FMS_BUY_CARGO_ALLOC_BL WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
						     cd = rset.getString(2);
						     agmt_type = rset.getString(3);
						     agmt_no = rset.getString(4);
						     agmt_rev = rset.getString(5);
						     cont_type = rset.getString(6);
						     cont_no = rset.getString(7);
						     cont_rev = rset.getString(8);
						     cargo_no = rset.getString(9);
						     alloc_rev = rset.getString(10);
						     bl_no = rset.getString(11);
						     
							logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+alloc_rev+","+bl_no+","), conn, "N");
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_BUY_CARGO_ALLOC_BL()>>");
					System.out.println();
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_BUY_CARGO_ALLOC_BL()>>,,,,,,,", conn);
					
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
			
			public void FMS_BUY_CARGO_ALLOC_BOE() throws SQLException, IOException {
				
				function_nm = "FMS_BUY_CARGO_ALLOC_BOE()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_BUY_CARGO_ALLOC_BOE()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_BUY_CARGO_ALLOC_BOE>>,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
					
					query_delete = "DELETE FROM FMS_BUY_CARGO_ALLOC_BOE WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV =? "
							+ "AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_NO = ? AND ALLOC_REV = ? AND BOE_NO = ?";
					
					String  agmt_type,agmt_no,agmt_rev,cont_type,cont_no,cont_rev,cargo_no,alloc_rev,ship_cd,boe_no,cont_ref_no;
					;
				
					column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BOE_NO,"
							+ "BU_SEQ,PLANT_SEQ,BOE_REF,BOE_DT,ACT_BOE_QTY,ACT_BOE_QTY_UNIT,ACT_QTY_MT,ACT_QTY_SCM,CUSTOM_DUTY,LOAD_PORT,ENT_BY,ENT_DT,FLAG,BOE_PROVISIONAL_PRICE,"
							+ "BOE_PROVISIONAL_PRICE_UNIT,BOE_FINAL_PRICE,BOE_FINAL_PRICE_UNIT";
					
					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_BOE_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_BOE_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, ",TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
							
								query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BOE_NO FROM FMS_BUY_CARGO_ALLOC_BOE WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";
								int i = 0;
								agmt_type="";agmt_no="";agmt_rev="";cont_type="";cont_no="";cont_rev="";cargo_no="";alloc_rev="";ship_cd="";boe_no="";cont_ref_no="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			
									data1[0]=company_cd;
									if(cell.getColumnIndex() == 0) 
									{
										 abbr = data;
								 		 data=company_cd;
								 		
									}
									else if(cell.getColumnIndex() == 1 ) {     		//COUNTERPARTY_CD
										
										cont_ref_no = data;
							    		
							    		query_String = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, abbr);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
							    			cd = rset.getString(1);
							    		}
							    		rset.close();
							    		stmt.close();
							    		data = cd;
							    		
							    		query_String = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE COMPANY_CD = '2' AND CARGO_REF = ? AND COUNTERPARTY_CD = ?";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cont_ref_no);
							    		stmt.setString(2, cd);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {				    			
							    			agmt_type = rset.getString(1);
							    			agmt_no = rset.getString(2);
							    			agmt_rev = rset.getString(3);
							    			cont_type = rset.getString(4);
							    			cont_no = rset.getString(5);
							    			cont_rev =  rset.getString(6);
							    			cargo_no =  rset.getString(7);
							    		}
							    		rset.close();
							    		stmt.close();
							    	}
									else if(cell.getColumnIndex() == 2) {	//AGMT_TYPE		    						    						    						    						   			    		
							    		data = agmt_type;
							    	}
							    	else if(cell.getColumnIndex() == 3) {	//AGMT_NO
							    		data = agmt_no;
							    	}
							    	else if(cell.getColumnIndex() == 4) {	//AGMT_REV
							    		data = agmt_rev;
							    	}
							    	else if(cell.getColumnIndex() == 5) {	//CONT_TYPE
							    		data = cont_type;
							    	}				    	
							    	else if (cell.getColumnIndex() == 6) {	//CONT_NO			    		
						    			data = cont_no;	
							    	}
							    	else if (cell.getColumnIndex() == 7) {	//CONT_REV			    		
						    			data = cont_rev;	
							    	}
									else if(cell.getColumnIndex() == 8) {	//CARGO_NO
							    		data = cargo_no;
							    	}
//									else if (cell.getColumnIndex() == 15) {	// ACT_BOE_QTY
//										
//							    		query_String = "SELECT BL_QTY FROM FMS_BUY_CARGO_NOM_BL WHERE CONT_NO = ? AND COMPANY_CD = '2' AND BL_NO = ?  ";
//							    		stmt = conn.prepareStatement(query_String);
//							    		stmt.setString(1, cont_no);
//							    		stmt.setString(2, data);
//							    		rset = stmt.executeQuery();
//							    		if (rset.next()) {
//								    		data = rset.getString(1);
//								    		if(rset.getString(1) == null) {
//								    			data = "null";
//								    		}
//							    		}
//							    		else  {
//							    			data = "0";
//							    		}
//							    		rset.close();
//							    		stmt.close();
//							    	}
							    	else if (cell.getColumnIndex() == 17) {	// ACT_QTY_MT
							    		query_String = "SELECT BL_QTY_MT FROM FMS_BUY_CARGO_NOM_BL WHERE CONT_NO = ? AND COMPANY_CD = '2' AND BL_NO = ?  ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cont_no);
							    		stmt.setString(2, data);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
								    		data = rset.getString(1);
								    		if(rset.getString(1) == null) {
								    			data = "null";
								    		}
							    		}
							    		else  {
							    			data = "0";
							    		}
							    		rset.close();
							    		stmt.close();
							    	}
							    	else if (cell.getColumnIndex() == 18) {	// ACT_QTY_SCM

							    		query_String = "SELECT BL_QTY_SCM FROM FMS_BUY_CARGO_NOM_BL WHERE CONT_NO = ? AND COMPANY_CD = '2' AND BL_NO = ?  ";
							    		stmt = conn.prepareStatement(query_String);
							    		stmt.setString(1, cont_no);
							    		stmt.setString(2, data);
							    		rset = stmt.executeQuery();
							    		if (rset.next()) {
								    		data = rset.getString(1);
								    		if(rset.getString(1) == null) {
								    			data = "null";
								    		}
							    		}
							    		else  {
							    			data = "0";
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
								     //	"COMPANY_CD", "COUNTERPARTY_CD" , "AGMT_TYPE" , "AGMT_NO" , "AGMT_REV" , "CONTRACT_TYPE" , "CONT_NO" , "CONT_REV" , "CARGO_NO" , "ALLOC_REV" ,"BOE_NO"
								    	 company_cd=rset.getString(1);
									     cd = rset.getString(2);
									     agmt_type = rset.getString(3);
									     agmt_no = rset.getString(4);
									     agmt_rev = rset.getString(5);
									     cont_type = rset.getString(6);
									     cont_no = rset.getString(7);
									     cont_rev = rset.getString(8);
									     cargo_no = rset.getString(9);
									     alloc_rev = rset.getString(10);
									     boe_no = rset.getString(11);
									     
									    
									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1, cd);
									    st_del.setString(2, agmt_type);
									    st_del.setString(3, agmt_no);
									    st_del.setString(4, agmt_rev);
									    st_del.setString(5, cont_type);
									    st_del.setString(6, cont_no);
									    st_del.setString(7, cont_rev);
									    st_del.setString(8, cargo_no);
									    st_del.setString(9,alloc_rev);
									    st_del.setString(10, boe_no);
									    
									    st_del.executeUpdate();
									    
									    logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+alloc_rev+","+boe_no+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
					
						// Data left
						query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BOE_NO FROM FMS_BUY_CARGO_ALLOC_BOE WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
						     cd = rset.getString(2);
						     agmt_type = rset.getString(3);
						     agmt_no = rset.getString(4);
						     agmt_rev = rset.getString(5);
						     cont_type = rset.getString(6);
						     cont_no = rset.getString(7);
						     cont_rev = rset.getString(8);
						     cargo_no = rset.getString(9);
						     alloc_rev = rset.getString(10);
						     boe_no = rset.getString(11);
						     
							logger.data(fname, (company_cd+","+cd+","+agmt_type+","+agmt_no+","+agmt_rev+","+cont_type+","+cont_no+","+cont_rev+","+cargo_no+","+alloc_rev+","+boe_no+","), conn, "N");
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_BUY_CARGO_ALLOC_BOE()>>");
					System.out.println();
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_BUY_CARGO_ALLOC_BOE()>>,,,,,,,", conn);
					
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
			
			public void FMS_TRADER_CONT_MST() throws SQLException, IOException {
				function_nm = "FMS_TRADER_CONT_MST()";
				try {
					column=" ";
					logger_count=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_TRADER_CONT_MST()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRADER_CONT_MST>>,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
					
					query_delete = "DELETE FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ?  AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
					
					String  agmt_no="",agmt_rev="",cont_no="", cont_rev="", contract_type="";
				
					
					column="COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_REV,CONTRACT_TYPE,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,CONT_STATUS,TXN_CHARGE,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,DDA_DT,DDA_TIME,TXN_UNIT,DAY_DEF_CLAUSE,MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE";

					data1 = new String[column.split(",").length];
					file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_MST_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_MST_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
								query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_TRADER_CONT_MST WHERE ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";cd="";
								int i = 0;
								agmt_no="";agmt_rev="";cont_no=""; cont_rev=""; contract_type="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if(cell.getColumnIndex() == 0) 
									{
										abbr=data;
										 
										 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
										 stmt=conn.prepareStatement(query_String);
								 		 stmt.setString(1,abbr);
								 		 rset = stmt.executeQuery(); 
								 		 if(rset.next())
								 		 {
								 		 cd=rset.getString(1);
								 		 }
								 		 else
								 		 { cd="";
								 		 }
								 		 stmt.close();
								 		 rset.close();
								 		 data=cd;
								 		 
									}
									else if(cell.getColumnIndex() == 1  || cell.getColumnIndex() == 4 || cell.getColumnIndex() == 7) {
										data=null;
									}
//									else if(cell.getColumnIndex() == 3) {
//										
//										query_String = "SELECT AGMT_NO FROM FMS_TRADER_AGMT_MST WHERE COUNTERPARTY_CD = ? ";
//							    		stmt = conn.prepareStatement(query_String);
//							    		stmt.setString(1, cd);
//							    		rset = stmt.executeQuery();
//							    		if (rset.next()) {
//							    			agmt_no = rset.getString(1);
//							    			
//							    		}else {
//							    			agmt_no = "null";
//							    		}
//							    		
//							    		rset.close();
//							    		stmt.close();				    		
//							    		data = agmt_no;	
//							    		
//									}
									
									
									if (data!= null) 
									{  
										data1[i] = data;
										
										if (data.equals("null")) {
									        	 query_select = query_select + column.split(",")[i] + " IS NULL AND ";
									     } else if (data.split("/").length == 3 && data != null && data.contains(":") && (data.length() == 19 || data.length() == 20)) {
									        	 query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
									     } else {
									        	 query_select = query_select + column.split(",")[i] + " =? AND ";
									      }
										i++;
									} 
									
								}//while(column) completed
								
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
//								        agmt_type=rset.getString(3);
								        agmt_no=rset.getString(3);
								        agmt_rev=rset.getString(4);
								        
								        cont_rev=rset.getString(6);
								        contract_type=rset.getString(7);
//								        cont_ref_no=rset.getString(8);

									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1, cd);
//									    st_del.setString(2, agmt_type);
									    st_del.setString(2, agmt_no);
									    st_del.setString(3, agmt_rev);
									    
									    st_del.setString(4, cont_rev);
									    st_del.setString(5, contract_type);
//									    st_del.setString(7, cont_ref_no);
									  
									    st_del.executeUpdate();
									  
									    logger.data(fname, (company_cd+","+cd+","+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+contract_type+","+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
					
						// Data left
						query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_REV,CONTRACT_TYPE FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
					        cd=rset.getString(2);
//					        agmt_type=rset.getString(3);
					        agmt_no=rset.getString(3);
					        agmt_rev=rset.getString(4);
					        cont_rev=rset.getString(5);
					        contract_type=rset.getString(6);
//					        cont_ref_no=rset.getString(8);
							
							logger.data(fname, (company_cd+","+cd+","+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+contract_type+","+","+","), conn, "N");
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
					
					
					System.out.println("<<ROLLBACK_END>><<FMS_TRADER_CN_MST()>>");
					System.out.println();
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRADER_CN_MST()>>,,,,,,,", conn);
					
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
			
			public void FMS_TRADER_BILLING_DTL() throws SQLException, IOException {
				function_nm = "FMS_TRADER_BILLING_DTL()";
				try {
					column=" ";
					logger_count=0;
					int bn=0;
					
					System.out.println("<<ROLLBACK_START>><<FMS_TRADER_BILLING_DTL()>>");
					
					logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_TRADER_BILLING_DTL>>,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

					query_delete = "DELETE FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE=? ";

					String  agmt_no, agmt_rev, cont_no, cont_rev, contract_type;
					String sn_name;
				
					column = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL";
							
					data1 = new String[column.split(",").length]; 
					file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_BILLING_DTL_"+start_end_dt+".xlsx");
					if (file1.exists()) 
					{
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_BILLING_DTL_"+start_end_dt+".xlsx"));
						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
						rowIterator.next();

						logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TIMESTAMP", conn);
						while (rowIterator.hasNext())
						{
								query_select = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD='2' AND ";
								row = rowIterator.next();
								cellIterator = row.cellIterator();
								
								data = "";cd="";
								int i = 0;
								agmt_no=""; agmt_rev=""; cont_no=""; cont_rev=""; contract_type="";sn_name="";
								while (cellIterator.hasNext()) 
								{
									cell = cellIterator.next();
									data = cell.getStringCellValue();
									data = data.substring(1, data.length() - 1);			

									if (cell.getColumnIndex() == 0) {	// SN_NAME,CONT_REF_NO 
										sn_name = data;
										data = company_cd;
							    	}
							    	else if (cell.getColumnIndex() == 1) {	// COUNTERPARTY_CD
							    		query_String = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? ";
										stmt = conn.prepareStatement(query_String);
										stmt.setString(1, sn_name);
										rset = stmt.executeQuery();
										if (rset.next()) {
							    			cd = rset.getString(1);
							    			agmt_no = rset.getString(2);
							    			agmt_rev = rset.getString(3);
							    			cont_no = rset.getString(4);
							    			cont_rev = rset.getString(5);
							    			contract_type = rset.getString(6);
							    		}
										rset.close();
							    		stmt.close();
										
										
							    		data = cd;
							    		
							    	}
							    	else if(cell.getColumnIndex() == 2) { //AGMT_NO
							    		data = agmt_no+"";	
							    	
							    	}
							    	else if(cell.getColumnIndex() == 3) { //AGMT_REV_NO
							    		data = agmt_rev+"";	
							    	
							    	}
							    	else if(cell.getColumnIndex() == 4) {  //CONT_NO
						    			data = cont_no+"";			    			
						    			
							    	}
									
									else if (cell.getColumnIndex() == 5) {	// CONT_REV
						    		data = cont_rev+"";
					    		    }

					    		
					    		    else if (cell.getColumnIndex() == 6) {	// DOM_FLAG(CONT_TYPE)
						    		data = contract_type;
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
								        contract_type=rset.getString(7);
								       
									    st_del = conn.prepareStatement(query_delete);
									    st_del.setString(1, cd);
									    st_del.setString(2, agmt_no);
									    st_del.setString(3, agmt_rev);
									    st_del.setString(4, cont_no);
									    st_del.setString(5, cont_rev);
									    st_del.setString(6, contract_type);
									  
									    
									    st_del.executeUpdate();
									    
									    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+contract_type+","), conn, "");
									    logger_count++;   
									  
									    st_del.close();
								      }

								     rset.close();
								     stmt.close();
					            }
						}//while(row) completed
						
						// Data left
						query_data_left = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD = '2' ";
						stmt = conn.prepareStatement(query_data_left);
						
						rset = stmt.executeQuery();
						while (rset.next()) {
							company_cd=rset.getString(1);
					        cd=rset.getString(2);
					        agmt_no=rset.getString(3);
					        agmt_rev=rset.getString(4);
					        cont_no=rset.getString(5);
					        cont_rev=rset.getString(6);
					        contract_type=rset.getString(7);
					        
					      
							
						    logger.data(fname, (company_cd+","+cd+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+contract_type+","), conn, "N");
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
					System.out.println("<<ROLLBACK_END>><<FMS_TRADER_BILLING_DTL()>>");
					
					
					logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,,,", conn);
					logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_TRADER_BILLING_DTL()>>,,,,,", conn);
					
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
				
			
	public String Camel_Case_Converter(String value) {

				//value = value.substring(1, value.length()-1);
			String converted_string = value; 
				
			if (value.length() > 3 && !value.contains("null")) {
				if (value.contains(" ")) {
					converted_string = "";
					for (int i = 0; i < value.split(" ").length; i++) {
						if (!value.split(" ")[i].substring(0, 1).equals("(") && !value.split(" ")[i].contains(")")) {
							converted_string += value.split(" ")[i].substring(0, 1).toUpperCase();
							converted_string = converted_string + value.split(" ")[i].substring(1).toLowerCase() + " ";
						}
						else {
							converted_string += value.split(" ")[i] + " ";
						}
					}
				}
					else {
					converted_string = value.substring(0, 1).toUpperCase();
					converted_string = converted_string + value.substring(1).toLowerCase() + " ";
				}
			}
			else {
				converted_string = null;
			}
				
			return converted_string;
				
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
