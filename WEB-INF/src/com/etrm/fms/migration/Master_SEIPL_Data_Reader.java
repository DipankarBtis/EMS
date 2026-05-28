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

//import automation.Auto_DB_Connection;

		

public class Master_SEIPL_Data_Reader {

	/*public static void main(String[] args) 
	{
		Master_Excel_Reader ex = new Master_Excel_Reader();
		ex.init();
	}
	
	}
	
	class Master_Excel_Reader {*/

	String db_src_file_name="Master_SEIPL_Data_Reader.java";

	String migration_setup_dir = "";
	
	String queryString="", queryString1="", query_logger="",query_String_upd="",query="";
	Connection conn;
	ResultSet rset,rset1,rset2,rs;
	PreparedStatement stmt,stmt1,stmt2,st;
	
	String function_nm = "", columns = "", data = "";
	
	String sysDateTime = "";
	String start_end_dt = null;
	
	String fname = "";
	String fname_error = "";
	
	//bellow fname1 & fname_error1 is for csv file function start & function end only 
    //logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);  (-R : READER )
	String fname1 = "";

	int index = 0;
	int logger_count = 0;
	int skipped_count=0;  
	int total_count=0;  
	
	String  abbr = "", cd = "", eff_dt = "",seq = "",name="",entity = "", addr_type = "";
	final String company_cd = "2";

	String checked_values = "", msg = "", msg_type = "";
//	String transporter_map = "", meter_map = "";

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
	Migration_Plants_Exceptions mpe =new Migration_Plants_Exceptions();
	
	public void init() {

		function_nm="init()";
		try {
//			getCustomerTraderList();
			
			fname = "DataLogs/Reader/Master_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/Master_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
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
					
					
					if (checked_values.contains("FMS_COUNTERPARTY_MST")) {
						FMS_COUNTERPARTY_MST_Update_IGX();
						FMS_COUNTERPARTY_MST();
					}
					
					if (checked_values.contains("FMS_COUNTERPARTY_ADDR_MST")) {
						FMS_COUNTERPARTY_ADDR_MST();
					}
					
					if (checked_values.contains("FMS_ENTITY_REQ_DTL")) {
						FMS_ENTITY_REQ_DTL();////
					}
					
					if (checked_values.contains("FMS_SECTOR_MST")) {
		    			FMS_SECTOR_MST();
		    			FMS_SECTOR_DTL();
					}
					
					if (checked_values.contains("FMS_GOVT_STAT_TAX")) {
						//FMS_GOVT_STAT_TAX();
					}
					
					if (checked_values.contains("FMS_ENTITY_ADDR_MST")) {
		    			FMS_ENTITY_ADDR_MST();////
					}
					
					if (checked_values.contains("FMS_COMPANY_OWNER_MST")) {
		    			FMS_COMPANY_OWNER_MST();
					}
					
					if (checked_values.contains("FMS_COMPANY_OWNER_ADDR_MST")) {
		    			FMS_COMPANY_OWNER_ADDR_MST();
					}
					
					if (checked_values.contains("FMS_COUNTERPARTY_PLANT_DTL")) {
		    			FMS_COUNTERPARTY_PLANT_DTL();////
					}
					
					if (checked_values.contains("FMS_COUNTERPARTY_BU_DTL")) {
		    			FMS_COUNTERPARTY_BU_DTL();
					}
					
					if (checked_values.contains("FMS_INT_RATE_MST")) {
						//FMS_INT_RATE_MST();
					}
					
					if (checked_values.contains("FMS_EXCHG_RATE_MST")) {
						//FMS_EXCHG_RATE_MST();
					}
					
					if (checked_values.contains("FMS_INT_PAY_RATE_ENTRY")) {
		    			FMS_INT_PAY_RATE_ENTRY();
					}

					if (checked_values.contains("FMS_EXCHG_RATE_ENTRY")) {
		    			FMS_EXCHG_RATE_ENTRY();
					}
					
					if (checked_values.contains("FMS_BANK_MST")) {
		    			FMS_BANK_MST();
					}
					
					if (checked_values.contains("FMS_ENTITY_TURNOVER_DTL")) {
		    			FMS_ENTITY_TURNOVER_DTL();////
					}
					
					if (checked_values.contains("FMS_SHIP_MST")) {
		    			FMS_SHIP_MST();
					}
					
					if (checked_values.contains("FMS_ENTITY_CONTACT_MST")) {
		    			FMS_ENTITY_CONTACT_MST();//
					}
		    		
					if (checked_values.contains("FMS_METER_MST")) {
		    			FMS_METER_MST();
					}
					
					if (checked_values.contains("FMS_COUNTERPARTY_PLANT_TAX")) {
		    			FMS_COUNTERPARTY_PLANT_TAX();////
					}
					
					if (checked_values.contains("FMS_COUNTERPARTY_BU_TAX")) {
						FMS_COUNTERPARTY_BU_TAX();
					}
					
					if (checked_values.contains("FMS_TAX_MST")) {
						//FMS_TAX_MST();
					}
					
					if (checked_values.contains("FMS_TAX_STRUCTURE")) {
						//FMS_TAX_STRUCTURE();
					}
					
					if (checked_values.contains("FMS_TAX_STRUCTURE_DTL")) {
						//FMS_TAX_STRUCTURE_DTL();
					}
					
					if (checked_values.contains("FMS_ENTITY_TAX_STRUCT_DTL")) {
		    			FMS_ENTITY_TAX_STRUCT_DTL();////
					}
					
					if (checked_values.contains("FMS_ENTITY_SERVICE_TAX_DTL")) {
		    			FMS_ENTITY_SERVICE_TAX_DTL();////
					}
					
					if (checked_values.contains("FMS_ENTITY_BU_SVC_TAX_DTL")) {
		    			FMS_ENTITY_BU_SVC_TAX_DTL();
					}
					
					if (checked_values.contains("FMS_CUSTOM_TAX_STRUCT_DTL")) {
		    			FMS_CUSTOM_TAX_STRUCT_DTL();
					}
					
					if (checked_values.contains("FMS_HOLIDAY_DTL")) {
		    			FMS_HOLIDAY_DTL();
					}
					
					if (checked_values.contains("FMS_PRODUCT_MST")) {
		    			FMS_PRODUCT_MST();
					}
					
					if (checked_values.contains("FMS_PRODUCT_MOLECULE_MST")) {
		    			FMS_PRODUCT_MOLECULE_MST();
					}
					
					if (checked_values.contains("FMS_SAC_MST")) {
		    			FMS_SAC_MST();
					}
					
					if (checked_values.contains("FMS_ENTITY_TCS_TDS_MST")) {
						FMS_ENTITY_TCS_TDS_MST();
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
				if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rs != null){try{rs.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(st != null){try{st.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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
	
	public void FMS_COUNTERPARTY_MST_Update_IGX() throws IOException, SQLException {

		function_nm="FMS_COUNTERPARTY_MST_Update_IGX()";
		try {
			System.out.println("<<START>><<FMS_COUNTERPARTY_MST_Update_IGX>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COUNTERPARTY_MST_Update_IGX>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;   
			

			queryString1 = "UPDATE FMS_COUNTERPARTY_MST SET IGX = ? WHERE COUNTERPARTY_CD = ? AND ENT_PROFILE = '1' ";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
			
				//String up_cg="",up_wb="";
				logger.checkpoint(fname, "CD,ABBR,IGX,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					total_count++;  
					cd="";
					
					String igx = "";
					data = "";
					index = 1;

					stmt1=conn.prepareStatement(queryString1);	
						
					row = rowIterator.next();
					cellIterator = row.cellIterator();
						
						while (cellIterator.hasNext()) 
						{
							
							cell = cellIterator.next();
							
							if (cell.getColumnIndex() == 0) 
							{	// Counterparty_cd																	
										
									abbr = cell.getStringCellValue().split("&@&")[0].substring(1, cell.getStringCellValue().split("&@&")[0].length());
//									cd = cell.getStringCellValue().split("&@&")[1].substring(0, cell.getStringCellValue().split("&@&")[1].length()-1);										
										//same_cd_flag = 1;
										
									queryString = "SELECT COUNTERPARTY_CD, COUNTERPARTY_ABBR, NVL(IGX, 'N') FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND ENT_PROFILE = '1' ORDER BY COUNTERPARTY_CD DESC ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next() ) {								
											cd = rset.getString(1);	
											data = rset.getString(3);		// IGX Flag
									}
									else 
									{ 
										cd = "";
										data = "N";							// IGX Flag
									}
									rset.close();
									stmt.close();
							
					    	}
							
							else if (cell.getColumnIndex() == 11) {
								igx = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(igx != null) {
						    		igx = igx.substring(1, igx.length()-1);
						    	}
							}
							
						}
						
						
						
						if (!cd.equals("") && igx != null && igx.contains("Y") && data.contains("N")) 
						{
							//System.out.println(queryString);
							
							logger.data(fname, (cd + " , " + abbr + "," + igx + ","), conn, "");
							stmt1.setString(1, igx);
							stmt1.setString(2, cd);
							stmt1.executeUpdate();
							stmt1.close();							
							logger_count++;
						}
						else {
							stmt1.close();					
							skipped_count++;  
							logger.data(fname, (cd + " , " + abbr + "," + igx + ","), conn, "E");
						}
						
					/*} 
					catch (Exception e) {
						conn.rollback();		
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'"+abbr + " - " + eff_dt+"'"), conn, fname_error, function_nm);			
					}*/
				}
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_MST_Update_IGX>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL :,"+total_count+",,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 		
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_MST_Update_IGX>>,,", conn);
			
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
			if (file != null) {
				file.close();
			}
			conn.commit();
		}
		
	}

	public void FMS_COUNTERPARTY_MST() throws IOException, SQLException {

		function_nm="FMS_COUNTERPARTY_MST()";
		try {
			System.out.println("<<START>><<FMS_COUNTERPARTY_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COUNTERPARTY_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;   
			
//			queryString = "SELECT (MAX(COUNTERPARTY_CD)+1) FROM FMS_COUNTERPARTY_MST  ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			if (rset.next() && rset.getInt(1) > 0) {
//				num = rset.getInt(1);
//			}else {
//				num = 1;
//			}
			//System.out.println(num);
//			rset.close();
//			stmt.close();

			
//			queryString1="UPDATE FMS_COUNTERPARTY_MST SET CATEGORY=?,WEB_ADDR=? WHERE COUNTERPARTY_ABBR=?";
//	    	queryString1 = "INSERT INTO FMS_COUNTERPARTY_MST VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) 
//			{
//				if(rset.getString(2).equals("DATE")) 
//				{
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else 
//				{
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_COUNTERPARTY_MST(COUNTERPARTY_CD,EFF_DT,COUNTERPARTY_NM,COUNTERPARTY_ABBR,PAN_NO,PAN_ISSUE_DT,CATEGORY,WEB_ADDR,NOTES,STATUS,KYC,IGX,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,SAP_CODE,NCF_CATEGORY,ENT_PROFILE,MOD_PROFILE) VALUES(?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
			
				//String up_cg="",up_wb="";
				logger.checkpoint(fname, "CD,ABBR,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					total_count++;  
					String sts = "",abbr1 = "";
					int same_cd_flag = 0;
					cd="";
					index = 1;

					stmt1=conn.prepareStatement(queryString1);	
						
					row = rowIterator.next();
					cellIterator = row.cellIterator();
						
						while (cellIterator.hasNext()) 
						{
							
							cell = cellIterator.next();
							data = null;
							
							if (cell.getColumnIndex() == 0) 
							{	// Counterparty_cd																	
										
									abbr = cell.getStringCellValue().split("&@&")[0].substring(1, cell.getStringCellValue().split("&@&")[0].length());
									cd = cell.getStringCellValue().split("&@&")[1].substring(0, cell.getStringCellValue().split("&@&")[1].length()-1);										
										//same_cd_flag = 1;
										
									queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ORDER BY COUNTERPARTY_CD DESC ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next() ) {								
											cd = rset.getString(1);	
									}
									else 
									{ 
										cd = "";
									}
									rset.close();
									stmt.close();
							
									data = cd;
					    	}
																				 
							/*if (cell.getColumnIndex() == 12 || cell.getColumnIndex() == 14) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
//								if (rset.next()) {
//						    		data = rset.getString(1) == null ? "null" : rset.getString(1);
//								}
//								else {
//									data = "null";
//								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 1) {
									eff_dt = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
									if (eff_dt != null) {
										eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
									}
								}
								if (cell.getColumnIndex() == 3) {
									abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
									if (abbr != null) {
										abbr = abbr.substring(1, abbr.length() - 1);
									}
								}
								if (cell.getColumnIndex() == 9) {
									sts = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
									if (sts != null) {
										sts = sts.substring(1, sts.length() - 1);
									}
								}
								data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
//						System.out.println("out while");
						queryString = "SELECT DISTINCT(COUNTERPARTY_CD), COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_CD = ? AND COUNTERPARTY_ABBR = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY')  ORDER BY COUNTERPARTY_CD";
//						queryString = "SELECT DISTINCT(A.COUNTERPARTY_CD), A.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST A WHERE A.COUNTERPARTY_ABBR = ? ORDER BY COUNTERPARTY_CD";
						stmt = conn.prepareStatement(queryString);
						
						
						stmt.setString(1, cd);
						stmt.setString(2, abbr);
						stmt.setString(3, eff_dt);

						rset = stmt.executeQuery();
						
						if (row.getRowNum() != 0 && !rset.next() && !cd.equals("") ) 
						{
							//System.out.println(queryString);
							
							logger.data(fname, (cd + " , " + abbr+","+eff_dt+","), conn, "");
							stmt1.executeUpdate();
							stmt1.close();							
							logger_count++;
						}
						else {
							stmt1.close();					
							skipped_count++;  
							logger.data(fname, (cd + " , " + abbr+","+eff_dt+","), conn, "E");
						}
						
						rset.close();
						stmt.close();
					/*} 
					catch (Exception e) {
						conn.rollback();		
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'"+abbr + " - " + eff_dt+"'"), conn, fname_error, function_nm);			
					}*/
				}
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_MST>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL :,"+total_count+",,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 		
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_MST>>,,", conn);
			
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
			if (file != null) {
				file.close();
			}
			conn.commit();
		}
		
	}
	
	public void FMS_COUNTERPARTY_ADDR_MST() throws IOException, SQLException {

		function_nm="FMS_COUNTERPARTY_ADDR_MST()";
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_ADDR_MST>>");
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COUNTERPARTY_ADDR_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0; 
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_COUNTERPARTY_ADDR_MST VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_ADDR_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_COUNTERPARTY_ADDR_MST(COUNTERPARTY_CD,ADDRESS_TYPE,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_ADDR_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) 
			{
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_ADDR_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();

				String  addr_type = "";
				
				logger.checkpoint(fname, "CD,ABBR,ADDRESS_TYPE,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					total_count++;  
					//try {
						data = "";
						cd="";
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						if (cellIterator.hasNext()) { 
							abbr = cellIterator.next().getStringCellValue();
							abbr = abbr.substring(1, abbr.length()-1);
						}
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 1) 
							{
								queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								
								if (rset.next()) {
									data = rset.getString(1);
									cd = rset.getString(1);
									rset.close();
									stmt.close();
								} 
								else {
									rset.close();
									stmt.close();
									
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ?  GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
									}
									rset.close();
									stmt.close();
								}
								
							
							}
							else if (cell.getColumnIndex() == 9 && cell.getStringCellValue().length() > 2) {
								queryString = "SELECT COUNTRY_NM FROM FMS_COUNTRY_MST WHERE UPPER(COUNTRY_NM) LIKE ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue().toUpperCase());
								rset = stmt.executeQuery();
								if(rset.next()) {
									data = rset.getString(1);
								}
								else {
									data = Camel_Case_Converter(cell.getStringCellValue());
								}
								rset.close();
								stmt.close();
							}
							/*else if (cell.getColumnIndex() == 16 || cell.getColumnIndex() == 18) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 2) {
									addr_type = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
									if (addr_type != null) {
										addr_type = addr_type.substring(1, addr_type.length() - 1);
									}
								}
								if (cell.getColumnIndex() == 3) {
									eff_dt = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
									if (eff_dt != null) {
										eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
									}
								}
								
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}

							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_ADDR_MST WHERE COUNTERPARTY_CD = ? AND ADDRESS_TYPE = ? AND  EFF_DT = TO_DATE(?, 'DD/MM/YYYY')";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, addr_type);
						stmt.setString(3, eff_dt);
						rset = stmt.executeQuery();
						
						if (row.getRowNum() != 0 && !rset.next() && !cd.equals("") ) {
							//System.out.println(queryString);
						
							logger.data(fname, (cd + " , " + abbr + " , " + addr_type + " , " + eff_dt+","), conn, "");
							stmt1.executeUpdate();
							stmt1.close();
							conn.commit();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (cd + " , " + abbr + " , " + addr_type + " , " + eff_dt+","), conn, "E");
						}
						
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'"+abbr + " - " + addr_type + " - " + eff_dt+"'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_ADDR_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_ADDR_MST>>,,,", conn);
			
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
	
	public void FMS_ENTITY_REQ_DTL() throws IOException, SQLException 
	{
		function_nm="FMS_ENTITY_REQ_DTL()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_REQ_DTL>>");
			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_REQ_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_ENTITY_REQ_DTL VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_REQ_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_ENTITY_REQ_DTL(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,ENTITY,REMARK,APRV_NOTE,STATUS,REQ_BY,REQ_DT,APRV_BY,APRV_DT) VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_REQ_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_REQ_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();


				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,SEQ,ENTITY,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						abbr = ""; cd = ""; seq = "";entity="";
						data = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						if (cellIterator.hasNext()) { 
							abbr = cellIterator.next().getStringCellValue();
							abbr = abbr.substring(1, abbr.length()-1);
							data = company_cd;
							stmt1.setString(index++, data);
						}
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 1) {
								queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y'  GROUP BY EFF_DT, COUNTERPARTY_CD ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if (rset.next()) {
									data = rset.getString(1);
									cd = rset.getString(1);
									rset.close();
									stmt.close();
								}
								else {
									rset.close();
									stmt.close();
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ?  GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
									}
									rset.close();
									stmt.close();
								}
							}
							/*else if (cell.getColumnIndex() == 7 || cell.getColumnIndex() == 9) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 2) {
									seq = cell.getStringCellValue();
									seq = seq.substring(1,seq.length()-1);
								}
								if (cell.getColumnIndex() == 3) {
									entity = cell.getStringCellValue();
									entity = entity.substring(1,entity.length()-1);
								}
								
								data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_REQ_DTL WHERE COUNTERPARTY_CD = ? AND COMPANY_CD = ? AND SEQ_NO = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, company_cd);
						stmt.setString(3, seq);
						rset = stmt.executeQuery();
						
						if (row.getRowNum() != 0 && !rset.next() && !cd.equals("")) {
							//System.out.println(queryString);
							
							logger.data(fname, (company_cd+" ," + cd + " , " + abbr + " , " + seq+","+entity+","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							conn.commit();
							
							logger_count++;
						}
						else {
							skipped_count++;     
							logger.data(fname, (company_cd+", " + cd + " , " + abbr + " , " + seq+","+entity+","), conn, "E");
						}
						
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'"+abbr + " - " + seq+"'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_ENTITY_REQ_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_REQ_DTL>>,,,,", conn);
			
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
	
	public void FMS_SECTOR_MST() throws IOException, SQLException {

		function_nm="FMS_SECTOR_MST()";
		try {

			System.out.println("<<START>><<FMS_SECTOR_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SECTOR_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			int max_cd = 1;

//	    	queryString1 = "INSERT INTO FMS_SECTOR_MST VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_SECTOR_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_SECTOR_MST(SECTOR_CD,SECTOR_NAME,SECTOR_ABBR,SECTOR_TYPE,STATUS_FLAG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(SECTOR_CD) FROM FMS_SECTOR_MST";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1)+1;
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SECTOR_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECTOR_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "ABBR,,TIMESTAMP", conn);
				
				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						abbr = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 2) {
								abbr = cell.getStringCellValue().toUpperCase();
								abbr = abbr.substring(1, abbr.length()-1);
							}
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							/*if (cell.getColumnIndex() == 5) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							if(cell.getColumnIndex() == 0) {
								data = "'"+(max_cd++)+"'";
							}
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT SECTOR_CD FROM FMS_SECTOR_MST WHERE UPPER(SECTOR_ABBR) = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, abbr);
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (abbr+",,"), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
						   max_cd--;
						   skipped_count++;     
						   logger.data(fname, (abbr+",,"), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, abbr, conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_SECTOR_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SECTOR_MST>>,", conn);
			
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

	public void FMS_SECTOR_DTL() throws IOException, SQLException {

		function_nm="FMS_SECTOR_DTL()";
		try {

			System.out.println("<<START>><<FMS_SECTOR_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SECTOR_DTL>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_SECTOR_DTL VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_SECTOR_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_SECTOR_DTL(SECTOR_CD,EFF_DT,DEMAND_SECT_CD,SUPPLY_SECT_CD,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE) VALUES(?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SECTOR_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECTOR_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "ABBR,,TIMESTAMP", conn);
				
				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						abbr = "";
						
						index = 1;
						
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						String[] rowData = new String[11];
						
						for(int i=0;i<rowData.length;i++) {
							
							if(i == 0){
							continue;
							}					
							
							if(i == 2 ){
								abbr = row.getCell(2).getStringCellValue().toUpperCase();
								
								abbr = abbr.substring(1, abbr.length()-1);
								
							}
							else {
								rowData[i] = row.getCell(i).getStringCellValue();
								
								rowData[i] = rowData[i].substring(1, rowData[i].length()-1);
							}
						}
//						stmt1.setInt(1, Integer.parseInt(rowData[0]));  //SECTOR_CD

						queryString = "SELECT SECTOR_CD FROM FMS_SECTOR_MST WHERE UPPER(SECTOR_ABBR) = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, abbr);
						rset = stmt.executeQuery();
						
						if (rset.next()) {
							String txt3 = rset.getString(1);
							//System.out.println(queryString1);
								String queryString2 = "SELECT SECTOR_CD FROM FMS_SECTOR_DTL WHERE SECTOR_CD = ?";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, txt3);
								rset2 = stmt2.executeQuery();
								
								if(!rset2.next()) {

									stmt1 = conn.prepareStatement(queryString1);
									
									stmt1.setInt(1, Integer.parseInt(txt3));
									
									stmt1.setString(2, rowData[6]);	//ENT_DT

									stmt1.setString(3, null);	//DEMAND_SECT_CD

									stmt1.setString(4, null);	//SUPPLY_SECT_CD

									stmt1.setInt(5, Integer.parseInt(rowData[5]));	//ENT_BY

									stmt1.setString(6, rowData[6]);	//ENT_DT

									stmt1.setString(7, null);	//MODIFY_BY

									stmt1.setString(8, null);	//MODIFY_DT

									stmt1.setInt(9, Integer.parseInt(rowData[9]));	//ENT_PROFILE

									stmt1.setString(10, null);	//MOD_PROFILE
									
									logger.data(fname, (abbr+",,"), conn, "");
									
									stmt1.executeUpdate();
									stmt1.close();
									
									logger_count++;
								}
//								else {
//									stmt1.close();
//									continue;
//								}
								rset2.close();
								stmt2.close();
						}
						else {
//							stmt1.close();
						   
						   skipped_count++;     
						   logger.data(fname, (abbr+",,"), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, abbr, conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_SECTOR_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SECTOR_DTL>>,", conn);
			
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
	
	public void FMS_GOVT_STAT_TAX() throws IOException, SQLException {

		function_nm="FMS_GOVT_STAT_TAX()";
		try {

			System.out.println("<<START>><<FMS_GOVT_STAT_TAX>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GOVT_STAT_TAX>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			int max_cd = 1000;
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

	    	queryString1 = "INSERT INTO FMS_GOVT_STAT_TAX VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_GOVT_STAT_TAX' ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(STAT_CD) FROM FMS_GOVT_STAT_TAX";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_GOVT_STAT_TAX_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_GOVT_STAT_TAX_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CD,STAT_NM,TIMESTAMP", conn);

				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						String type = "";
						String flag = "N";
						data = "";
						
						name = ""; cd = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								max_cd++;
								data = max_cd+"";
								cd = data;
							}
							else if (cell.getColumnIndex() == 1) {
								name = cell.getStringCellValue();
								name = name.substring(1,name.length()-1);
								data = name;
								name = name.toUpperCase();
							}
							else if (cell.getColumnIndex() == 2) {
								type = cell.getStringCellValue().toUpperCase();
								type = type.substring(1,type.length()-1);
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}

								queryString = "	SELECT STAT_CD, UPPER(STAT_NM), STAT_TYPE FROM FMS_GOVT_STAT_TAX WHERE STAT_TYPE = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, type);
								rset = stmt.executeQuery();
								
								while (rset.next()) {
									if (name.contains(rset.getString(2)) && flag.equals("N")) {
										flag = "Y";
									}
								}
								rset.close();
								stmt.close();
							}
							/*else if (cell.getColumnIndex() == 6) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}

							stmt1.setString(index++, data);
						}
						
						if (flag.equals("N")) {
							//System.out.println(queryString1);
							logger.data(fname, (cd + " , " + name+","), conn, "");
								
							stmt1.executeUpdate();
							stmt1.close();
								
							logger_count++;
						}
						else {
							stmt1.close();
						   max_cd--;
						   skipped_count++;     
						   logger.data(fname, (cd + " , " + name+","), conn, "E");
						}
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'"+cd + " - " + name+"'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_GOVT_STAT_TAX>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_GOVT_STAT_TAX>>,", conn);
			
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
	
	public void FMS_ENTITY_ADDR_MST() throws IOException, SQLException {

		function_nm="FMS_ENTITY_ADDR_MST()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_ADDR_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_ADDR_MST>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_ENTITY_ADDR_MST VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_ADDR_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			
			queryString1 = "INSERT INTO FMS_ENTITY_ADDR_MST(COMPANY_CD,COUNTERPARTY_CD,ADDRESS_TYPE,ENTITY,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
//			PreparedStatement stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_ADDR_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_ADDR_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,ADDR_TYPE,ENTITY,EFF_DT,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						abbr = ""; cd = ""; entity = ""; addr_type = ""; eff_dt = "";
						
						index = 1;
					 stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						if (cellIterator.hasNext()) { 
							abbr = cellIterator.next().getStringCellValue();
							abbr = abbr.substring(1, abbr.length()-1);
							data = company_cd;
							stmt1.setString(index++, data);
						}
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 1) 
							{
								queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if (rset.next()) {
									data = rset.getString(1);
									cd = rset.getString(1);
									rset.close();
									stmt.close();
								}
								else {
									rset.close();
									stmt.close();
									
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
									}
									rset.close();
									stmt.close();
								}
							}
							else if (cell.getColumnIndex() == 10 && cell.getStringCellValue().length() > 2)
							{
								queryString = "SELECT COUNTRY_NM FROM FMS_COUNTRY_MST WHERE UPPER(COUNTRY_NM) LIKE ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue().toUpperCase());
								rset = stmt.executeQuery();
								if(rset.next()) {
									data = rset.getString(1);
									rset.close();
									stmt.close();
								}
								else {
									data = Camel_Case_Converter(cell.getStringCellValue());
								}
								rset.close();
								stmt.close();
							}
							/*else if (cell.getColumnIndex() == 17) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 2) {
									addr_type = cell.getStringCellValue();
									addr_type = addr_type.substring(1,addr_type.length()-1);
								}
								if (cell.getColumnIndex() == 3) {
									entity = cell.getStringCellValue();
									entity = entity.substring(1,entity.length()-1);
								}
								if (cell.getColumnIndex() == 4) {
									eff_dt = cell.getStringCellValue();
									eff_dt = eff_dt.substring(1,eff_dt.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_ADDR_MST WHERE COUNTERPARTY_CD = ? AND COMPANY_CD = ? AND ENTITY = ? AND ADDRESS_TYPE = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, company_cd);
						stmt.setString(3, entity);
						stmt.setString(4, addr_type);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+", "+ cd + " , " + abbr + " , " + addr_type + " , " + entity + " , " + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
//							if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
							skipped_count++;     
						   logger.data(fname, (company_cd+", "+ cd + " , " + abbr + " , " + addr_type + " , " + entity + " , " + eff_dt + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'2 - "+ cd + " - " + abbr + " - " + addr_type + " - " + entity + " - " + eff_dt + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_ENTITY_ADDR_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_ADDR_MST>>,,,,,", conn);	
			
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
			//resultset close.

		}
		
	}
	
	public void FMS_COMPANY_OWNER_MST() throws IOException, SQLException {

		function_nm="FMS_COMPANY_OWNER_MST()";
		try {

			System.out.println("<<START>><<FMS_COMPANY_OWNER_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COMPANY_OWNER_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			columns = "";
			String[] data;
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			queryString = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COMPANY_OWNER_MST' ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				columns = columns + rset.getString(1) + ",";
			}
			rset.close();
			stmt.close();
			
			columns = columns.substring(0, columns.length()-1);
			data = new String[columns.split(",").length-3];
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COMPANY_OWNER_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COMPANY_OWNER_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,EFF_DT,TIMESTAMP", conn);
				
				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						abbr = ""; eff_dt = "";
						
						index = 0;
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							/*if (cell.getColumnIndex() == 10) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data[index++] = rset.getString(1) == null ? "null" : rset.getString(1);
								}
								else {
									data[index++] = "null";
								}
								rset.close();
								stmt.close();
							}*/
							/*else {*/
								if (cell.getColumnIndex() == 1) {
									eff_dt = cell.getStringCellValue();
									eff_dt = eff_dt.substring(1, eff_dt.length()-1);
								}
								if (cell.getColumnIndex() == 3) {
									abbr = cell.getStringCellValue();
									abbr = abbr.substring(1, abbr.length()-1);
								}
								data[index++] = cell.getStringCellValue();
								/*}*/
						}
						
						queryString = " UPDATE FMS_COMPANY_OWNER_MST SET ";
						queryString += "EFF_DT = TO_DATE(?, 'DD/MM/YYYY'), ";	
						queryString += "COMPANY_NM = ?, COMPANY_ABBR = ?, PAN_NO = ?, ";
						queryString += "PAN_ISSUE_DT =  TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ";	
						queryString += "NOTES = ?, STATUS = ?, CATEGORY = ?, WEB_ADDR = ?, ENT_BY = ?, ";
						queryString += "ENT_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ";	
						queryString += "MODIFY_BY = ?, MODIFY_DT = ?, INVOICE_PREFIX = ? WHERE COMPANY_CD = '2' ";

						stmt = conn.prepareStatement(queryString);
						
						index = 1;
						
						for (int i = 1; i < data.length; i++) {
								this.data = data[i].contains("null") ? null : data[i];
								if(this.data != null) {
						    		this.data = this.data.substring(1, this.data.length()-1);
						    	}
								stmt.setString(index++, this.data); 
						}
						logger.data(fname, (company_cd+","+company_cd+"," + abbr + " , " + eff_dt + ","), conn, "");

						stmt.executeUpdate();
						stmt.close();
						
						logger_count++;
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'2 - " + abbr + " - " + eff_dt + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_COMPANY_OWNER_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED :, "+logger_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_COMPANY_OWNER_MST>>,,,", conn);
			
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
	
	public void FMS_COMPANY_OWNER_ADDR_MST() throws IOException, SQLException {

		function_nm="FMS_COMPANY_OWNER_ADDR_MST()";
		try {

			System.out.println("<<START>><<FMS_COMPANY_OWNER_ADDR_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COMPANY_OWNER_ADDR_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_COMPANY_OWNER_ADDR_MST VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COMPANY_OWNER_ADDR_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_COMPANY_OWNER_ADDR_MST(COMPANY_CD,ADDRESS_TYPE,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
//			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COMPANY_OWNER_ADDR_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COMPANY_OWNER_ADDR_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,ABBR,ADDR_TYPE,EFF_DT,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						abbr = ""; addr_type = ""; eff_dt = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						if (cellIterator.hasNext()) { 
							abbr = cellIterator.next().getStringCellValue();
							abbr = abbr.substring(1, abbr.length()-1);
							data = company_cd;
							stmt1.setString(index++, data);
							cell = cellIterator.next();
						}
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 9 && cell.getStringCellValue().length() > 2) 
							{
								queryString = "SELECT COUNTRY_NM FROM FMS_COUNTRY_MST WHERE UPPER(COUNTRY_NM) LIKE ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue().toUpperCase());
								rset = stmt.executeQuery();
								if(rset.next()) {
									data = rset.getString(1);
								}
								else {
									data = Camel_Case_Converter(cell.getStringCellValue());
								}
								rset.close();
								stmt.close();
							}
							/*else if (cell.getColumnIndex() == 16 || cell.getColumnIndex() == 18) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 2) {
									addr_type = cell.getStringCellValue();
									addr_type = addr_type.substring(1,addr_type.length()-1);
								}
								if (cell.getColumnIndex() == 3) {
									eff_dt = cell.getStringCellValue();
									eff_dt = eff_dt.substring(1,eff_dt.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_ADDR_MST WHERE COMPANY_CD = ?  AND ADDRESS_TYPE = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, addr_type);
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + abbr + " , " + addr_type + " , " + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							skipped_count++;     
						   logger.data(fname, (company_cd+"," + abbr + " , " + addr_type + " , " + eff_dt + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, abbr, conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_COMPANY_OWNER_ADDR_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_COMPANY_OWNER_ADDR_MST>>,,,", conn);
			
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
	
	public void FMS_COUNTERPARTY_PLANT_DTL() throws IOException, SQLException {

		function_nm="FMS_COUNTERPARTY_PLANT_DTL()";
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_PLANT_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COUNTERPARTY_PLANT_DTL>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_COUNTERPARTY_PLANT_DTL VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_PLANT_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_COUNTERPARTY_PLANT_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
//			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_PLANT_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_PLANT_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,ENTITY,SEQ_NO,EFF_DT,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						abbr = ""; cd = ""; entity = ""; eff_dt = ""; seq = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						if (cellIterator.hasNext()) { 
							abbr = cellIterator.next().getStringCellValue();
							abbr = abbr.substring(1, abbr.length()-1);
							data = company_cd;
							stmt1.setString(index++, data);
						}
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 1 && !abbr.equalsIgnoreCase("SEIPL")) 
							{
								queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if (rset.next()) {
									data = rset.getString(1);
									cd = rset.getString(1);
									rset.close();
									stmt.close();
								}
								else 
								{
									rset.close();
									stmt.close();
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) 
									{
										data = rset.getString(1);
										cd = rset.getString(1);
									}
								rset.close();
								stmt.close();
								}
							}
							else if (cell.getColumnIndex() == 1 && abbr.equalsIgnoreCase("SEIPL")) {
								data = company_cd;
								cd = company_cd;
							}
							else if (cell.getColumnIndex() == 12 && !cell.getStringCellValue().contains("null")) {	// Sector_CD

								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
								
								queryString = "SELECT SECTOR_CD FROM FMS_SECTOR_MST WHERE SECTOR_ABBR = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, data);
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}
							/*else if (cell.getColumnIndex() == 15) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 4) {
									eff_dt = cell.getStringCellValue();
									eff_dt = eff_dt.split("'")[1];						
								}
								if (cell.getColumnIndex() == 3) {
									seq = cell.getStringCellValue();
									seq = seq.substring(1,seq.length()-1);
								}
								if (cell.getColumnIndex() == 2) {
									entity = cell.getStringCellValue();
									entity = entity.substring(1,entity.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COUNTERPARTY_CD = ? AND COMPANY_CD = ? AND ENTITY = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND SEQ_NO = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, company_cd);
						stmt.setString(3, entity);
						stmt.setString(4, eff_dt);
						stmt.setString(5, seq);
						
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+", " + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
						   logger.data(fname, (company_cd+", " + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + eff_dt + ","), conn, "E");
						}
						rset.close();
						stmt.close();
				/*	} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, abbr, conn, fname_error, function_nm);
					}*/
				   
				}
				
				queryString1 = "SELECT PLANT_ABBR FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = '2' AND PLANT_NAME = 'SPSSETL1-1' AND ENTITY = 'T' ";
				stmt1 = conn.prepareStatement(queryString1);
				rset1 = stmt1.executeQuery();
				
				if (!rset1.next()) {
					// Insert queries to be inserted as discussed with Vijay on 06/05/2025
					queryString = "Insert into FMS_COUNTERPARTY_PLANT_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (2,278,'T',1,to_date('01/04/2023','DD/MM/YYYY'),'SEMTEXP-1','SEMTEXP','C/O IKEVA VENTURE AND KNOWLEDGE ADVISORY SERVICES PVT. LTD. LEVEL 1, MB TOWERS, ROAD NO. 10, BANJARA HILLS','TELANGANA','S','HYDERABAD','500034',null,'Y',to_date('06/06/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
					stmt = conn.prepareStatement(queryString);
					stmt.executeUpdate();
					stmt.close();
					
					queryString = "Insert into FMS_COUNTERPARTY_PLANT_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (2,115,'T',1,to_date('21/02/2024','DD/MM/YYYY'),'SILS-1','SILS-1','80 STRAND','NA','0','LONDON',null,null,'Y',to_date('06/06/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
					stmt = conn.prepareStatement(queryString);
					stmt.executeUpdate();
					stmt.close();
					
					queryString = "Insert into FMS_COUNTERPARTY_PLANT_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (2,113,'T',1,to_date('11/10/2023','DD/MM/YYYY'),'SIETCO-1','SIETCO','THE METROPOLIS TOWER 1 9 NORTH BUONA VISTA DRIVE 07 TO 01','SINGAPORE','0','SINGAPORE','138588',null,'Y',to_date('06/06/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
					stmt = conn.prepareStatement(queryString);
					stmt.executeUpdate();
					stmt.close();
					
					queryString = "Insert into FMS_COUNTERPARTY_PLANT_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (2,112,'T',1,to_date('02/10/2023','DD/MM/YYYY'),'SPSSETL1-1','SPSSETL1','THE METROPOLIS TOWER 1,NO.9 NORTH BUONA VISTA DRIVE','SINGAPORE','0','SINGAPORE','138588',null,'Y',to_date('06/06/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
					stmt = conn.prepareStatement(queryString);
					stmt.executeUpdate();
					stmt.close();
					
					queryString = "Insert into FMS_COUNTERPARTY_PLANT_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (2,110,'T',1,to_date('08/03/2023','DD/MM/YYYY'),'SITMEFZ','SITMEFZ','GROUND FLOOR JAFZA 10 BUILDING','DUBAI','0','JEBEL ALI FREE ZONE','16968',null,'Y',to_date('06/06/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
					stmt = conn.prepareStatement(queryString);
					stmt.executeUpdate();
					stmt.close();
					
					queryString = "Insert into FMS_COUNTERPARTY_PLANT_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (2,256,'T',1,to_date('20/10/2008','DD/MM/YYYY'),'SITME','SITME-1','Level 3, The Offices 4, One Central, Dubai World Trade Center, PO Box 11677, Dubai, UAE','Dubai','E','Vadodara','363642',null,'Y',to_date('06/06/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,to_date('06/06/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1)";
					stmt = conn.prepareStatement(queryString);
					stmt.executeUpdate();
					stmt.close();
					
					queryString = "Insert into FMS_COUNTERPARTY_PLANT_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (2,116,'T',1,to_date('29/04/2005','DD/MM/YYYY'),'SELNG-1','SELNG','P.O. Box 16968, Jebel Ali','TX 47449','0','Dubai','0',null,'Y',to_date('06/06/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
					stmt = conn.prepareStatement(queryString);
					stmt.executeUpdate();
					stmt.close();
					
					queryString = "Insert into FMS_COUNTERPARTY_PLANT_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (2,270,'T',1,to_date('27/04/2005','DD/MM/YYYY'),'TGPL-1','TGPL','33, Cavendish Square',null,'0','London, W1G0PW',null,null,'Y',to_date('06/06/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
					stmt = conn.prepareStatement(queryString);
					stmt.executeUpdate();
					stmt.close();
					
					queryString = "Insert into FMS_COUNTERPARTY_PLANT_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (2,152,'T',1,to_date('10/02/2021','DD/MM/YYYY'),'GUNSPL-1','GUNSPL','12 MARINA BLVD 35 03 MARINA BAY FINANCIAL TOWER 3','NA','0','SINGAPORE','18982',null,'Y',to_date('06/06/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
					stmt = conn.prepareStatement(queryString);
					stmt.executeUpdate();
					stmt.close();
					/////
				}
				rset1.close();
				stmt1.close();
				


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_PLANT_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_PLANT_DTL>>,,,,,", conn);
			
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
	
	public void FMS_COUNTERPARTY_BU_DTL() throws IOException, SQLException {

		function_nm="FMS_COUNTERPARTY_BU_DTL()";
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_BU_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COUNTERPARTY_BU_DTL>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			//DATA FOR TRANSPORTER BU 
			//FOR GAIL(GAIL UP)
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'GAIL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,"
					+ "PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'R',1,to_date('08/03/2007','DD/MM/YYYY'),'GAIL UP','GAIL UP',"
					+ " 'Sector - 1, Noida, Gautam Budh Nagar,','Uttar Pradesh','N','Noida','201301','Y',to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
				  	+ "AND ENTITY = 'R' AND SEQ_NO = '1' AND EFF_DT = TO_DATE('08/03/2007','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//FOR GAIL(GAIL GJ)	20250821
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,"
					+ "PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'R',2,to_date('08/03/2007','DD/MM/YYYY'),'GAIL GJ','GAIL GJ',"
					+ "'Hazira Compressor Station, P.O.:ONGCL, Ichchapor Magdalla Road','Gujarat','W','Surat','394518','Y',to_date('21/08/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
				  	+ "AND ENTITY = 'R' AND SEQ_NO = '2' AND EFF_DT = TO_DATE('08/03/2007','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//FOR GSPL(GSPL Gujarat)
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'GSPL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,"
					+ "PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'R',1,to_date('01/10/2024','DD/MM/YYYY'),'GSPL Gujarat','GSPL Gujarat',"
					+ " 'GSPL Bhavan, E-18, GIDC Electronics Estate, Nr. K-7 Circle,Sector-26','Gujarat','W','Gandhinagar','382028','Y',to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),"
					+ "1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
				  	+ "AND ENTITY = 'R' AND SEQ_NO = '1' AND EFF_DT = TO_DATE('01/10/2024','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//FOR PIL(PIL AP)
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'PIPEINFRA' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,"
					+ "PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'R',3,to_date('01/01/2020','DD/MM/YYYY'),'PIL AP','PIL AP',"
					+ "'Compressor Station 2, Mandal - Pedaregi, Koppaka','Andhra Pradesh','S','West Godavari','534003','Y',to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),"
					+ "1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND SEQ_NO = '3' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//FOR PIL(PIL GJ)
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'PIPEINFRA' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,"
					+ "PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'R',1,to_date('01/01/2020','DD/MM/YYYY'),'PIL GJ','PIL GJ',"
					+ "'Bhadbhut- Kaswas Road, Bhadbhut,Compressor Station 10','Gujarat','W','Bharuch','392012','Y',to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),"
					+ "1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND SEQ_NO = '1' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//FOR PIL(PIL MH)
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'PIPEINFRA' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,"
					+ "PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'R',2,to_date('01/01/2020','DD/MM/YYYY'),'PIL MH','PIL MH',"
					+ "'Tower-1, 3rd Level,C Wing - 301 to 304 Plot R1, Sector 40, Seawoods Railway Stn Nerul Node','Maharashtra','C','Navi Mumbai','400706',"
					+ "'Y',to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND SEQ_NO = '2' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			

			
			//FOR RGPL
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'RGPL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'R',1,TO_DATE('01/12/2022','DD/MM/YYYY'),'RGPL SHAHDOL','RGPL SHAHD','SHAHDOL','Madhya Pradesh',NULL,NULL,NULL,'Y',TO_DATE('24/06/2025','DD/MM/YYYY'),1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND SEQ_NO = '1' AND EFF_DT = TO_DATE('01/12/2022','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			

			//FOR GSINLIM
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'GSINLIM' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'R',1,TO_DATE('13/01/2025','DD/MM/YYYY'),'GITL KUC','GITL KUC',NULL,'Telangana',NULL,'KUC',NULL,'Y',TO_DATE('24/06/2025','DD/MM/YYYY'),1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND SEQ_NO = '1' AND EFF_DT = TO_DATE('13/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			

			//FOR GSIGLIM
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'GSIGLIM' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			//PLANT-2
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'R',2,TO_DATE('01/06/2023','DD/MM/YYYY'),'GIGL Barmar','GIGL Barma',NULL,'Rajasthan',NULL,NULL,NULL,'Y',TO_DATE('24/06/2025','DD/MM/YYYY'),1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND SEQ_NO = '2' AND EFF_DT = TO_DATE('01/06/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//PLANT-1
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'R',1,TO_DATE('01/06/2023','DD/MM/YYYY'),'GIGL Palanpur','GIGL PAL',NULL,'Gujarat',NULL,NULL,NULL,'Y',TO_DATE('24/06/2025','DD/MM/YYYY'),1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND SEQ_NO = '1' AND EFF_DT = TO_DATE('01/06/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			
			// GX PLANT-1	20250904
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,1,'G',1,to_date('01/04/2024','DD/MM/YYYY'),'IGX UP','IGX UP','Plot No. C-001/A/1, 6th Floor, Part D, Max Towers, Sec - 16B, Noida,','Uttar Pradesh','N','Gautam Buddha Nagar','201301','Y',to_date('04/09/2025','DD/MM/YYYY'),1,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = '1'  "
					+ "AND ENTITY = 'G' AND SEQ_NO = '1' AND EFF_DT = TO_DATE('01/04/2024','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			
//	    	queryString1 = "INSERT INTO FMS_COUNTERPARTY_BU_DTL VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_BU_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_COUNTERPARTY_BU_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_BU_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_BU_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,ENTITY,SEQ_NO,EFF_DT,TIMESTAMP", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						abbr = ""; cd = ""; entity = ""; eff_dt = ""; seq = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						if (cellIterator.hasNext()) { 
							abbr = cellIterator.next().getStringCellValue();
							abbr = abbr.substring(1, abbr.length()-1);
							data = company_cd;
							stmt1.setString(index++, data);
						}
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 1 && !abbr.equalsIgnoreCase("SEIPL")) 
							{
								queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if (rset.next()) {
									data = rset.getString(1);
									cd = rset.getString(1);
									rset.close();
									stmt.close();
								}
								else {
									rset.close();
									stmt.close();
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
									}
									rset.close();
									stmt.close();
								}
							}
							else if (cell.getColumnIndex() == 1 && abbr.equalsIgnoreCase("SEIPL")) {
								data = company_cd;
								cd = company_cd;
							}
							/*else if (cell.getColumnIndex() == 15) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 4) {
									eff_dt = cell.getStringCellValue();
									eff_dt = eff_dt.split("'")[1];						
								}
								if (cell.getColumnIndex() == 3) {
									seq = cell.getStringCellValue();
									seq = seq.substring(1,seq.length()-1);
								}
								if (cell.getColumnIndex() == 2) {
									entity = cell.getStringCellValue();
									entity = entity.substring(1,entity.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_BU_DTL WHERE COUNTERPARTY_CD = ? AND COMPANY_CD = ? AND ENTITY = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND SEQ_NO = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, "2");
						stmt.setString(3, entity);
						stmt.setString(4, eff_dt);
						stmt.setString(5, seq);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") ) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+", " + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
						   logger.data(fname, (company_cd+" , " + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + eff_dt + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("2 - '" + cd + " - " + abbr + " - " + entity + " - " + seq + " - " + eff_dt + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_BU_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_BU_DTL>>,,,,,", conn);
			
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
	
	public void FMS_INT_RATE_MST() throws IOException, SQLException {

		function_nm="FMS_INT_RATE_MST()";
		try {

			System.out.println("<<START>><<FMS_INT_RATE_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_INT_RATE_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			int max_cd = 0;
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

	    	queryString1 = "INSERT INTO FMS_INT_RATE_MST VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_INT_RATE_MST' ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(INT_RATE_CD) FROM FMS_INT_RATE_MST";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_INT_RATE_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INT_RATE_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CD,ABBR,TIMESTAMP,", conn);
	
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						cd = ""; abbr = "";
						data = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								max_cd++;
								data = (max_cd)+"";
							}
							/*else if (cell.getColumnIndex() == 5) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 0) {
									cd = cell.getStringCellValue().toUpperCase();
									cd = cd.substring(1, cd.length()-1);
								}
								if (cell.getColumnIndex() == 1) {
									name = cell.getStringCellValue().toUpperCase();
									name = name.substring(1, name.length()-1);
									name = name.trim();
								}
								if (cell.getColumnIndex() == 2) {
									abbr = cell.getStringCellValue().toUpperCase();
									abbr = abbr.substring(1, abbr.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT INT_RATE_NM FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) LIKE ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, "%"+name+"%");
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, ( cd + " , " + abbr + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {	
							stmt1.close();
							max_cd--;
							skipped_count++;     
							logger.data(fname, ( cd + " , " + abbr + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, abbr, conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_INT_RATE_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_INT_RATE_MST>>,", conn);
			
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
	
	public void FMS_EXCHG_RATE_MST() throws IOException, SQLException {

		function_nm="FMS_EXCHG_RATE_MST()";
		try {

			System.out.println("<<START>><<FMS_EXCHG_RATE_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_EXCHG_RATE_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			int max_cd = 0;
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

	    	queryString1 = "INSERT INTO FMS_EXCHG_RATE_MST VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_EXCHG_RATE_MST' ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(EXC_RATE_CD) FROM FMS_EXCHG_RATE_MST";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_EXCHG_RATE_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_EXCHG_RATE_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CD,ABBR,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						cd = ""; abbr = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								max_cd++;
								data = max_cd+"";
								cd = data;
							}
							/*else if (cell.getColumnIndex() == 5) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 1) {
									name = cell.getStringCellValue().toUpperCase();
									name = name.substring(1, name.length()-1);
									name = name.trim();
								}
								if (cell.getColumnIndex() == 2) {
									abbr = cell.getStringCellValue().toUpperCase();
									abbr = abbr.substring(1, abbr.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT EXC_RATE_NM FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) LIKE ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, "%"+name+"%");
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, ( cd + " ," + abbr +  ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							max_cd--;
							skipped_count++;     
							logger.data(fname, (cd + " ," + abbr +  ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'" + cd + " - " + abbr +  "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_EXCHG_RATE_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_EXCHG_RATE_MST>>,", conn);
			
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
	
	public void FMS_INT_PAY_RATE_ENTRY() throws IOException, SQLException {

		function_nm="FMS_INT_PAY_RATE_ENTRY()";
		try {

			System.out.println("<<START>><<FMS_INT_PAY_RATE_ENTRY>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_INT_PAY_RATE_ENTRY>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_INT_PAY_RATE_ENTRY VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_INT_PAY_RATE_ENTRY' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_INT_PAY_RATE_ENTRY(INT_RATE_CD,EFF_DT,INT_VAL,CURRENCY_CD,REMARK,FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE) VALUES(?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
//			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_INT_PAY_RATE_ENTRY_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INT_PAY_RATE_ENTRY_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CD,NAME,EFF_DT,TIMESTAMP", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						cd = ""; eff_dt = ""; name = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								name = cell.getStringCellValue().toUpperCase();
								name = name.substring(1, name.length()-1).trim();
								
								if(name.equals("SB BR")) {
									name = "SBI BR";
								}								
								queryString = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) LIKE ? ";
						    	stmt = conn.prepareStatement(queryString);
						    	stmt.setString(1, "%"+name+"%");
						    	rset = stmt.executeQuery();
						    	if (rset.next()) {
						    		cd = rset.getString(1);
						    	}
						    	rset.close();
						    	stmt.close();
						    	
								cell = cellIterator.next();
						    	data = cd;
						    	stmt1.setString(index++, data);
								cell = cellIterator.next();
							}
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							/*if (cell.getColumnIndex() == 8) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							if(cell.getColumnIndex() == 2) {
								eff_dt = data;
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT INT_RATE_CD FROM FMS_INT_PAY_RATE_ENTRY WHERE INT_RATE_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, eff_dt);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") ) {
							//System.out.println(queryString1);
							
							logger.data(fname, ( cd + " , " + name + " , " + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
						   logger.data(fname, (cd + " , " + name + " , " + eff_dt + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'" + cd + " - " + name + " - " + eff_dt + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_INT_PAY_RATE_ENTRY>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_INT_PAY_RATE_ENTRY>>,,", conn);
			
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
	
	public void FMS_EXCHG_RATE_ENTRY() throws IOException, SQLException {

		function_nm="FMS_EXCHG_RATE_ENTRY()";
		try {

			System.out.println("<<START>><<FMS_EXCHG_RATE_ENTRY>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_EXCHG_RATE_ENTRY>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_EXCHG_RATE_ENTRY VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_EXCHG_RATE_ENTRY' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_EXCHG_RATE_ENTRY(EXCHG_RATE_CD,EFF_DT,EXCHG_VAL,CURRENCY_CD,CURRENCY_CD_FROM,REMARK,FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE) VALUES(?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_EXCHG_RATE_ENTRY_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_EXCHG_RATE_ENTRY_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CD,NAME,EFF_DT,TIMESTAMP", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						cd = ""; eff_dt = ""; name = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								name = cell.getStringCellValue().toUpperCase();
								name = name.substring(1, name.length()-1).trim();
								
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
								
								
								queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
						    	stmt = conn.prepareStatement(queryString);
						    	stmt.setString(1, name);
						    	rset = stmt.executeQuery();
						    	if (rset.next()) {
						    		cd = rset.getString(1);
						    	}
						    	rset.close();
						    	stmt.close();
						    	
								cell = cellIterator.next();
						    	data = cd;
						    	stmt1.setString(index++, data);
							}
							else if (cell.getColumnIndex() == 6 && cell.getStringCellValue().length() > 2 && !cell.getStringCellValue().contains("'null'")) {
								data = cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1).replace("'", " ");
						    	stmt1.setString(index++, data);
							}
							/*else if (cell.getColumnIndex() == 9) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
						    	stmt1.setString(index++, data);
							}*/
							else {
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
								if(cell.getColumnIndex() == 2) {
									eff_dt = data;
								}
						    	stmt1.setString(index++, data);
							}
						}
						
						queryString = "SELECT EXCHG_RATE_CD FROM FMS_EXCHG_RATE_ENTRY WHERE EXCHG_RATE_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, eff_dt);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") ) {
							//System.out.println(queryString1);
							
							logger.data(fname, ( cd + " ," + name + " , " + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
						   logger.data(fname, (cd + " ," + name + " , " + eff_dt + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'" + cd + " - " + name + " - " + eff_dt + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_EXCHG_RATE_ENTRY>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_EXCHG_RATE_ENTRY>>,,", conn);
			
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
	
	public void FMS_BANK_MST() throws IOException, SQLException {

		function_nm="FMS_BANK_MST()";
		try {

			System.out.println("<<START>><<FMS_BANK_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_BANK_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			int max_cd = 1000;
			String bank_name="";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_BANK_MST VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_BANK_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_BANK_MST(BANK_CD,BANK_NAME,BANK_ABBR,EFF_DT,BRANCH_NAME,ADDR,CITY,PIN,STATE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,REMARK,BRANCH_IFSC_CD,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE) VALUES(?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(BANK_CD) FROM FMS_BANK_MST";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next() && rset.getInt(1) != 0) {
				max_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BANK_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BANK_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CD,BANK_NAME,BANK_ABBR,EFF_DT,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						
						cd = ""; abbr = ""; eff_dt = "";
						data = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if(cell.getColumnIndex() == 1) {
								bank_name=cell.getStringCellValue();
								bank_name = bank_name.substring(1, bank_name.length()-1);
							}
							
							if (cell.getColumnIndex() == 0) {
								max_cd++;
								data = max_cd+"";
								cd = data;
							}
							else if (cell.getColumnIndex() == 9 && cell.getStringCellValue().length() > 2) {
								queryString = "SELECT COUNTRY_NM FROM FMS_COUNTRY_MST WHERE UPPER(COUNTRY_NM) LIKE ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1).toUpperCase());
								rset = stmt.executeQuery();
								if(rset.next()) {
									data = rset.getString(1);
								}
								else {
									data = Camel_Case_Converter(cell.getStringCellValue());
								}
								rset.close();
								stmt.close();
								if(data != null && data.contains("null")) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							/*else if (cell.getColumnIndex() == 20) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 1) {
									name = cell.getStringCellValue();
									name = name.substring(1, name.length()-1);
									//name = name.trim();
								}
								if (cell.getColumnIndex() == 2) {
									abbr = cell.getStringCellValue().toUpperCase();
									abbr = abbr.substring(1, abbr.length()-1);
								}
								if (cell.getColumnIndex() == 3) {
									eff_dt = cell.getStringCellValue().toUpperCase();
									eff_dt = eff_dt.substring(1, eff_dt.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT BANK_ABBR FROM FMS_BANK_MST WHERE BANK_NAME = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, name);
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, ( cd + " , " + bank_name + " , " + abbr + " , " + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							max_cd--;
							skipped_count++;     
							logger.data(fname, ( cd + " , " + bank_name + " , " + abbr + " , " + eff_dt + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'" + cd + " - " + abbr + " - " + eff_dt + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_BANK_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_BANK_MST>>,,,", conn);
			
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
	
	public void FMS_ENTITY_TURNOVER_DTL() throws IOException, SQLException {

		function_nm="FMS_ENTITY_TURNOVER_DTL()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_TURNOVER_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_TURNOVER_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_ENTITY_TURNOVER_DTL VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_TURNOVER_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_ENTITY_TURNOVER_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,FINANCIAL_YEAR,TURNOVER_CD,TURNOVER_FLAG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_TURNOVER_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_TURNOVER_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,ENTITY,FINANCIAL_YEAR,TIMESTAMP", conn);

				String fin_yr="";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						abbr = ""; cd = ""; fin_yr = ""; entity = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								abbr = cell.getStringCellValue();
								abbr = abbr.substring(1, abbr.length()-1);
								abbr = abbr.trim();
								data = company_cd;
							}
							else if (cell.getColumnIndex() == 1) {
								if (!abbr.toUpperCase().contains("SEIPL")) 
								{
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
										rset.close();
										stmt.close();
									}
									else {
										rset.close();
										stmt.close();
										queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
										stmt = conn.prepareStatement(queryString);
										stmt.setString(1, abbr);
										rset = stmt.executeQuery();
										if (rset.next()) {
											data = rset.getString(1);
											cd = rset.getString(1);
										}
										rset.close();
										stmt.close();
									}
									
								}
								else   if (cell.getColumnIndex() == 1 && abbr.equalsIgnoreCase("SEIPL")) {
									data = company_cd;
									cd = company_cd;
								}
							}
							/*else if (cell.getColumnIndex() == 6) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 2) {
									entity = cell.getStringCellValue();
									entity = entity.substring(1, entity.length()-1);
								}
								if (cell.getColumnIndex() == 3) {
									fin_yr = cell.getStringCellValue();
									fin_yr = fin_yr.substring(1, fin_yr.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}

							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TURNOVER_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND FINANCIAL_YEAR = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, "2");
						stmt.setString(2, cd);
						stmt.setString(3, entity);
						stmt.setString(4, fin_yr);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+" ," + cd + " , " + abbr + " , " + entity + " , " + fin_yr + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
						   logger.data(fname, (company_cd+" ," + cd + " , " + abbr + " , " + entity + " , " + fin_yr + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("2 - '" + cd + " - " + abbr + " - " + entity + " - " + fin_yr + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_ENTITY_TURNOVER_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_TURNOVER_DTL>>,,,,", conn);
			
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
	
	public void FMS_SHIP_MST() throws IOException, SQLException {

		function_nm="FMS_SHIP_MST()";
		try {

			System.out.println("<<START>><<FMS_SHIP_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SHIP_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			int max_cd = 0;
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_SHIP_MST VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_SHIP_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_SHIP_MST(EFF_DT,SHIP_CD,SHIP_NAME,SHIP_CALL_SIGN,SHIP_FLAG,SHIP_IMO_NO,SHIP_CLASS_SOC,INMARSAT_NO,SHIP_OWNER_NAME,SHIP_OPERATOR_NAME,SHIP_FAX_NO,SHIP_TELEX_NO,SHIP_EMAIL,GROSS_TONNAGE,CARGO_CAPACITY,VOLUME_UNIT,PERCENTAGE_CAPACITY,SHIP_ITEM,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE) VALUES(TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(SHIP_CD) FROM FMS_SHIP_MST";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SHIP_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SHIP_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CD,SHIP_NAME,EFF_DT,TIMESTAMP", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						name = ""; cd = ""; eff_dt = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 1) {
								max_cd++;
								data = max_cd+"";
								cd = data;
							}
							/*else if ( cell.getColumnIndex() == 18) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 0) {
									eff_dt = cell.getStringCellValue().toUpperCase();
									eff_dt = eff_dt.substring(1, eff_dt.length()-1);
								}
								if (cell.getColumnIndex() == 2) {
									name = cell.getStringCellValue().toUpperCase();
									name = name.substring(1,name.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT SHIP_CD FROM FMS_SHIP_MST WHERE UPPER(SHIP_NAME) = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, name);
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (cd + " , " + name + " , " + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							max_cd--;
							skipped_count++;     
							logger.data(fname, (cd + " , " + name + " , " + eff_dt + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'" + cd + " - " + name + " - " + eff_dt + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_SHIP_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SHIP_MST>>,,", conn);
			
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
	
	public void FMS_ENTITY_CONTACT_MST() throws IOException, SQLException {

		function_nm="FMS_ENTITY_CONTACT_MST()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_CONTACT_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_CONTACT_MST>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			

			
			//GX CONTACT PERSON-RLNG
			cd = "1";
			queryString1 = "Insert into FMS_ENTITY_CONTACT_MST (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ADDR_IS_ACTIVE,TO_NOM,TO_INV,TO_FM,TO_PM,TO_JT,TO_OTHER,RM_FLAG,TO_RM,TYPE,F402_FLAG,TO_F402) values (?,?,'G',1,to_date('01/05/2025','DD/MM/YYYY'),'Vijayakumar',null,null,null,null,null,'vijayakumar.s@shell.com','B1',null,'Y','Y','N','N','Y','N','Y',to_date('04/09/2025','DD/MM/YYYY'),1,null,null,'Y','Y','Y',null,null,'Y',null,'Y','Y','RLNG',null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
				  	+ "AND ENTITY = 'G' AND SEQ_NO = '1' AND EFF_DT = TO_DATE('01/05/2025', 'DD/MM/YYYY') AND ADDR_FLAG = 'B1' AND TYPE = 'RLNG' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();

			

			//GX CONTACT PERSON-DLNG
			queryString1 = "Insert into FMS_ENTITY_CONTACT_MST (COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ADDR_IS_ACTIVE,TO_NOM,TO_INV,TO_FM,TO_PM,TO_JT,TO_OTHER,RM_FLAG,TO_RM,TYPE,F402_FLAG,TO_F402) values (?,?,'G',1,to_date('01/05/2025','DD/MM/YYYY'),'Vijayakumar',null,null,null,null,null,'vijayakumar.s@shell.com','B1',null,'Y','Y','N','N','Y','N','Y',to_date('04/09/2025','DD/MM/YYYY'),1,null,null,'Y','Y','Y',null,null,'Y',null,'Y','Y','DLNG','N',null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
				  	+ "AND ENTITY = 'G' AND SEQ_NO = '1' AND EFF_DT = TO_DATE('01/05/2025', 'DD/MM/YYYY') AND ADDR_FLAG = 'B1' AND TYPE = 'DLNG' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();

//	    	queryString1 = "INSERT INTO FMS_ENTITY_CONTACT_MST VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_CONTACT_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_ENTITY_CONTACT_MST(COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ADDR_IS_ACTIVE,TO_NOM,TO_INV,TO_FM,TO_PM,TO_JT,TO_OTHER,RM_FLAG,TO_RM,TYPE,F402_FLAG,TO_F402 )VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?)";//DIYA 20250226 ADD BRACKET)AFTER TYPE
			
//			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_CONTACT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_CONTACT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,ENTITY,SEQ_NO,EFF_DT,ADDR_FLAG,TIMESTAMP", conn);

				String addr_flag = "", type = "";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						
						row = rowIterator.next();
						int plant_num = 1;
						
						for (int i = 0; i < plant_num; i++) {
							data = "";
							abbr = ""; cd = ""; seq = ""; entity = ""; addr_flag = ""; eff_dt = ""; type = "";
							
							index = 1;
							
							cellIterator = row.cellIterator(); 
							stmt1 = conn.prepareStatement(queryString1);
							
							while (cellIterator.hasNext()) {
								cell = cellIterator.next();
								if (cell.getColumnIndex() == 0) {
									abbr = cell.getStringCellValue();
									abbr = abbr.substring(1, abbr.length()-1);
									abbr = abbr.trim();
									data = company_cd;
								}
								else if (cell.getColumnIndex() == 1) 
								{
									if (!abbr.toUpperCase().contains("SEIPL")) 
									{
										queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
										stmt = conn.prepareStatement(queryString);
										stmt.setString(1, abbr);
										rset = stmt.executeQuery();
										if (rset.next()) {
											data = rset.getString(1);
											cd = rset.getString(1);
											rset.close();
											stmt.close();
										}
										else {
											rset.close();
											stmt.close();
											
											queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
											stmt = conn.prepareStatement(queryString);
											stmt.setString(1, abbr);
											rset = stmt.executeQuery();
											if (rset.next()) {
												data = rset.getString(1);
												cd = rset.getString(1);
											}
											rset.close();
											stmt.close();
										}
									}
									else  if (cell.getColumnIndex() == 1 && abbr.equalsIgnoreCase("SEIPL")) {
										data = "2";
										cd = "2";
									}
								}
								/*else if (cell.getColumnIndex() == 22) {	// Emp_Cd
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cell.getStringCellValue());
									rset = stmt.executeQuery();
									if (rset.next()) {
							    		data = rset.getString(1);
									}
									else {
										data = null;
									}
									rset.close();
									stmt.close();
								}*/
								else if (cell.getColumnIndex() == 12) {
									addr_flag = cell.getStringCellValue();
									addr_flag = addr_flag.substring(1, addr_flag.length()-1);
									
									if(addr_flag.contains("B") &&  !entity.contains("B")) {
										
										queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY = ? AND COUNTERPARTY_CD = ? ";
										stmt = conn.prepareStatement(queryString);
										stmt.setString(1, company_cd);
										stmt.setString(2, entity);
										stmt.setString(3, cd);
										rset = stmt.executeQuery();
										
										while (rset.next()) {
											
											if (!addr_flag.contains("P"+rset.getString(1))) {
												addr_flag += ("P"+rset.getString(1)+",");
											}
											
										}
										rset.close();
										stmt.close();
										
									}
									
									plant_num = addr_flag.split(",").length;
									addr_flag = addr_flag.split(",")[i];
									
									data = addr_flag;
									
								}
								else {
									if (cell.getColumnIndex() == 2) {
										entity = cell.getStringCellValue();
										entity = entity.substring(1, entity.length()-1);
									}
									if (cell.getColumnIndex() == 3) {
										seq = cell.getStringCellValue();
										seq = seq.substring(1, seq.length()-1);
									}
									if (cell.getColumnIndex() == 34) {
										type = cell.getStringCellValue();
										type = type.substring(1, type.length()-1);
									}
									if (cell.getColumnIndex() == 4) {
										eff_dt = cell.getStringCellValue().split("'")[1];
									}
									data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if(data != null) {
							    		data = data.substring(1, data.length()-1);
							    	}
								}
								stmt1.setString(index++, data);
							}
							
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND SEQ_NO = ? AND ADDR_FLAG = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND TYPE = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, entity);
							stmt.setString(4, seq);
							stmt.setString(5, addr_flag);
							stmt.setString(6, eff_dt);
							stmt.setString(7, type);
							rset = stmt.executeQuery();
							
							if (!rset.next() && !cd.equals("")) {
								//System.out.println(queryString1);
								
								logger.data(fname, (company_cd+"," + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + eff_dt + " , " + addr_flag + ","), conn, "");
								
								stmt1.executeUpdate();
								stmt1.close();
								
								logger_count++;
							}
							else {
								stmt1.close();
								skipped_count++;     
							   logger.data(fname, (company_cd+"," + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + eff_dt + " , " + addr_flag + ","), conn, "E");
							}
							rset.close();
							stmt.close();
						/*} catch (Exception e) {
							new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
							logger.data_error(fname, e, ("2 - '" + cd + " - " + abbr + " - " + entity + " - " + seq_no + " - " + eff_dt + " - " + addr_flag + "'"), conn, fname_error, function_nm);
						}*/
					}
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_ENTITY_CONTACT_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_CONTACT_MST>>,,,,,,", conn);
			
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
	
	public void FMS_METER_MST() throws IOException, SQLException {

		function_nm="FMS_METER_MST()";
		try {

			System.out.println("<<START>><<FMS_METER_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_METER_MST>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			int gail_dahej = 1, gailhazsei = 1, gail_haz = 1, pil_haz = 1, pil_bhad = 1, pil_ank = 1, pil_msk = 1;

//	    	queryString1 = "INSERT INTO FMS_METER_MST VALUES(";
	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_METER_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
	    	queryString1 = "INSERT INTO FMS_METER_MST(COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,METER_SEQ,METER_ID,METER_REF,SPECIFICATION,NOTE,STATUS,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY) VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
//			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_METER_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_METER_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,PLANT_SEQ,METER_TYPE,METER_SEQ,TIMESTAMP", conn);

				String  p_seq_no = "", meter_type = "", meter_seq_no = "", org_abbr = "";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						abbr = ""; cd = ""; p_seq_no = ""; meter_type = ""; meter_seq_no = ""; org_abbr = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								abbr = cell.getStringCellValue();
								abbr = abbr.substring(1, abbr.length()-1);
								abbr = abbr.trim();
								org_abbr = abbr;
//								if (mpe.transporter_map.contains(abbr+";")) 
//								{
//									for (int c = 0; c < transporter_map.split(",").length; c++) {
//										if (transporter_map.split(",")[c].contains(abbr+";")) {
//											org_abbr = abbr;
//											abbr = transporter_map.split(",")[c].split(";")[1];
//										}
//									}
//								}
								if (mpe.transporter_map.containsKey(abbr)) {
									org_abbr = abbr;
									abbr = mpe.transporter_map.get(abbr);
								}
//								if (meter_map.contains(org_abbr+";")) {
//									for (int c = 0; c < meter_map.split(",").length; c++) {
//										if (meter_map.split(",")[c].contains(org_abbr+";")) {
//											org_abbr = meter_map.split(",")[c].split(";")[1];
//										}
//									}
//								}
								if(org_abbr.equals("PIL-Haz")) {
									org_abbr = "PIL HAZ";
								}
								else if (mpe.meter_map.containsKey(org_abbr)) 
								{
									org_abbr = mpe.meter_map.get(org_abbr);
								}
								
								
								data = company_cd;
							}
							else if (cell.getColumnIndex() == 1) 
							{
								if (!abbr.toUpperCase().contains("SEIPL")) 
								{
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
										rset.close();
										stmt.close();
									}
									else {
										rset.close();
										stmt.close();
										queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
										stmt = conn.prepareStatement(queryString);
										stmt.setString(1, abbr);
										rset = stmt.executeQuery();
										if (rset.next()) {
											data = rset.getString(1);
											cd = rset.getString(1);
										}
										rset.close();
										stmt.close();
									}
								
								}
							}
							else if (cell.getColumnIndex() == 2) {
								p_seq_no = cell.getStringCellValue();
								p_seq_no = p_seq_no.substring(1, p_seq_no.length()-1);
								
								queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ? AND ENTITY = 'R' AND COMPANY_CD = '2' ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, org_abbr);
								rset = stmt.executeQuery();
								
								if (rset.next()) {
									p_seq_no = rset.getString(1);
								}
								rset.close();
								stmt.close();
								data = p_seq_no;
							}
							else if (cell.getColumnIndex() == 4) {
									meter_seq_no = cell.getStringCellValue();
									meter_seq_no = meter_seq_no.substring(1, meter_seq_no.length()-1);
									
									queryString = "SELECT C.PLANT_ABBR FROM FMS_COUNTERPARTY_PLANT_DTL C, FMS_COUNTERPARTY_MST A WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND A.COUNTERPARTY_ABBR = ? AND C.SEQ_NO = ? AND C.ENTITY = 'R' ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									stmt.setString(2, p_seq_no);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										if (rset.getString(1).equals("GAILHAZSEI")) {
											meter_seq_no = gailhazsei+"";
											gailhazsei++;
										}
										else if (rset.getString(1).equals("GAIL DAHEJ")) {
											meter_seq_no = gail_dahej+"";
											gail_dahej++;
										}
										else if (rset.getString(1).equals("GAIL HAZ")) {
											meter_seq_no = gail_haz+"";
											gail_haz++;
										}
										else if (rset.getString(1).equals("PIL MSK")) {
											meter_seq_no = pil_msk+"";
											pil_msk++;
										}
										else if (rset.getString(1).equals("PIL ANK")) {
											meter_seq_no = pil_ank+"";
											pil_ank++;
										}
										else if (rset.getString(1).equals("PIL BHAD")) {
											meter_seq_no = pil_bhad+"";
											pil_bhad++;
										}
										else if (rset.getString(1).equals("PIL HAZ")) {
											meter_seq_no = pil_haz+"";
											pil_haz++;
										}
									}
									
									rset.close();
									stmt.close();
									
									data = meter_seq_no;
									
							}
							else if (cell.getColumnIndex() == 5) {
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
								data = abbr + "-P" + p_seq_no + "-M" + meter_seq_no; 
							}
							else if(cell.getColumnIndex() == 9 && cell.getStringCellValue().contains("*")) {	// Flag(*)
								data = "N";
							}
							/*else if (cell.getColumnIndex() == 10) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 3) {
									meter_type = cell.getStringCellValue();
									meter_type = meter_type.substring(1, meter_type.length()-1);
								}
							
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
								
							}
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_METER_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND PLANT_SEQ = ? AND METER_TYPE = ? AND METER_SEQ = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, p_seq_no);
						stmt.setString(4, meter_type);
						stmt.setString(5, meter_seq_no);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+" ," + cd + " , " + abbr + " , " + p_seq_no + " , " + meter_type + " , " + meter_seq_no + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
						   logger.data(fname, (company_cd+"," + cd + " , " + abbr + " , " + p_seq_no + " , " + meter_type + " , " + meter_seq_no + ","), conn, "E");
						}
						rset.close();
   						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("2 - '" + cd + " - " + abbr + " - " + p_seq_no + " - " + meter_type + " - " + meter_seq_no + "'"), conn, fname_error, function_nm);
					}*/
				   
				}
				
				queryString = "SELECT COUNTERPARTY_CD, SEQ_NO, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY') FROM FMS_COUNTERPARTY_PLANT_DTL WHERE ENTITY = 'R' AND PLANT_ABBR = 'PIL ANK' AND COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				
				if (rset.next()) {
					queryString1 = "INSERT INTO FMS_METER_MST(COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,METER_SEQ,METER_ID,METER_REF,SPECIFICATION,NOTE,STATUS,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY) VALUES('2',?,?,'R','1',?,'PIL ANK',NULL,'Added Through Insert Query After Discussion With Vijay on 03-06-2025','Y',?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, rset.getString(1));
					stmt1.setString(2, rset.getString(2));
					stmt1.setString(3, "PipeInfra-P"+rset.getString(2)+"-M1");
					stmt1.setString(4, rset.getString(3));
					stmt1.setString(5, rset.getString(4));
					stmt1.setString(6, rset.getString(6));
					stmt1.setString(7, rset.getString(5));
					
					stmt1.executeUpdate();
					
					stmt1.close();
				}
				
				rset.close();
				stmt.close();


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_METER_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_METER_MST>>,,,,,", conn);
			
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
	
	public void FMS_COUNTERPARTY_PLANT_TAX() throws IOException, SQLException {

		function_nm="FMS_COUNTERPARTY_PLANT_TAX()";
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_PLANT_TAX>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COUNTERPARTY_PLANT_TAX>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_COUNTERPARTY_PLANT_TAX VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_PLANT_TAX' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_COUNTERPARTY_PLANT_TAX(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
//			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_PLANT_TAX_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_PLANT_TAX_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,ENTITY,PLANT_SEQ_NO,STAT_CD,TIMESTAMP", conn);
				
				String stat = "";
				String prv_abbr="",prv_plant_seq_no="",prv_stat_nm="",prv_stat_no="",stat_no="",stat_nm="";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						String flag = "N";
						String flag2 ="N";
					
						data = "";
						abbr = ""; cd = ""; entity = ""; stat = ""; seq = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						if (cellIterator.hasNext()) { 
							abbr = cellIterator.next().getStringCellValue();
							abbr = abbr.substring(1, abbr.length()-1);
							data = company_cd;
							stmt1.setString(index++, data);
						}
						
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 1 && !abbr.equalsIgnoreCase("SEIPL")) 
							{
								queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if (rset.next()) {
									data = rset.getString(1);
									cd = rset.getString(1);
									rset.close();
									stmt.close();
								}
								else {
									rset.close();
									stmt.close();
									
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
									}
									rset.close();
									stmt.close();
								}
							}
							/*else if (cell.getColumnIndex() == 10) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 3) {
									seq = cell.getStringCellValue();
									seq = seq.substring(1,seq.length()-1);
								}
								if (cell.getColumnIndex() == 2) {
									entity = cell.getStringCellValue();
									entity = entity.substring(1,entity.length()-1);
								}
								if (cell.getColumnIndex() == 4) 
								{
									stat = cell.getStringCellValue();
									stat = stat.substring(1,stat.length()-1);	
									stat_nm=stat;
								
									if(stat.toUpperCase().contains("GVAT TIN") || stat.toUpperCase().contains("GST TIN")) {
										stat = "VAT TIN No.-S";
									}
									
									queryString = "	SELECT STAT_CD, UPPER(STAT_NM), STAT_TYPE FROM FMS_GOVT_STAT_TAX ";
									stmt = conn.prepareStatement(queryString);
									rset = stmt.executeQuery();
									
									while (rset.next()) {
										if (stat.contains("-") && stat.split("-")[0].toUpperCase().contains(rset.getString(2)) && flag.equals("N") && stat.split("-")[1].toUpperCase().contains(rset.getString(3))) {
											flag = "Y";
											stat = rset.getString(1);
										}
									}
									rset.close();
									stmt.close();

									if (flag.equals("N")) {
										stat = "";									
									}
									data = stat;
								}
								else
								{
									
									if(cell.getColumnIndex() == 5)
									{
										 stat_no = cell.getStringCellValue();
										 stat_no = stat_no.substring(1,stat_no.length()-1);
										
										if(prv_abbr.equals(abbr) && prv_plant_seq_no.equals(seq))
										{ 
											
											if((stat_nm.toUpperCase().contains("GVAT TIN") || stat_nm.toUpperCase().contains("GST TIN") || stat_nm.toUpperCase().contains("VAT TIN")))
											{
												if(!prv_stat_no.equals(stat_no))
												{
												flag2="Y";
												}
											}
										}
										
									}
									data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if(data != null) {
							    		data = data.substring(1, data.length()-1);
							    	}
									if (cell.getColumnIndex() == 1 && abbr.equalsIgnoreCase("SEIPL")) {
										cd = company_cd;
									}
								}
							}
						    	stmt1.setString(index++, data);
						   	
						}
						
						if(!prv_plant_seq_no.equals(seq) || !prv_abbr.equals(abbr))
						{
				    		
				    		if(stat_nm.toUpperCase().contains("GVAT TIN") || stat_nm.toUpperCase().contains("GST TIN") || stat_nm.toUpperCase().contains("VAT TIN"))
							{
				    			prv_abbr=abbr;
				    			prv_plant_seq_no=seq;
				    			prv_stat_nm=stat_nm;
				    			prv_stat_no=stat_no;
				    	    }
						}
						if(flag2.equals("Y"))
						{
							logger.data(fname, (abbr + "," + seq +"," + stat_nm+"," + stat_no+","+ stat_nm+" is different from "+prv_stat_nm+","), conn, "");
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_PLANT_TAX WHERE COUNTERPARTY_CD = ? AND COMPANY_CD = ? AND ENTITY = ? AND STAT_CD = ? AND PLANT_SEQ_NO = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, company_cd);
						stmt.setString(3, entity);
						stmt.setString(4, stat);
						stmt.setString(5, seq);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !stat.equals("") && !cd.equals("")) {
							
							logger.data(fname, (company_cd+" , " + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + stat + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+" , " + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + stat + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("2 - '" + cd + " - " + abbr + " - " + entity + " - " + seq + " - " + stat + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_PLANT_TAX>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_PLANT_TAX>>,,,,,", conn);
			
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
	
	public void FMS_COUNTERPARTY_BU_TAX() throws IOException, SQLException {

		function_nm="FMS_COUNTERPARTY_BU_TAX()";
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_BU_TAX>>");
		
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COUNTERPARTY_BU_TAX>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			//DATA FOR TRANSPORTER BU 
			//FOR GAIL(GAIL UP)
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'GAIL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			//PAN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',1,1001,'AAACG1209J',null,'Y',null,to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, STAT_CD FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
				  	+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND STAT_CD = '1001' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			//GSTIN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',1,1003,'09AAACG1209J1ZU',null,'Y',null,to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND STAT_CD = '1003' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//FOR GAIL(GAIL GJ)	20250821
			//PAN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',2,1001,'AAACG1209J',null,'Y',null,to_date('21/08/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, STAT_CD FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
				  	+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND STAT_CD = '1001' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//GSTIN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',2,1003,'24AAACG1209J2Z1',null,'Y',null,to_date('21/08/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND STAT_CD = '1003' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();

			//CST TIN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',2,1004,'24690101732',null,'Y',null,to_date('21/08/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND STAT_CD = '1004' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();

			//VAT TIN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',2,1005,'24190101732',null,'Y',null,to_date('21/08/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND STAT_CD = '1005' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			
			//FOR GSPL(GSPL Gujarat)
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'GSPL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			//PAN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',1,1001,'AABCG1812E',null,'Y',null,to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
				  	+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND STAT_CD = '1001' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			//GSTIN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',1,1003,'24AABCG1812E1ZB',null,'Y',null,to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND STAT_CD = '1003' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//FOR PIL(PIL AP)
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'PIPEINFRA' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			//PAN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',3,1001,'AAJCP8265A',null,'Y',null,to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND STAT_CD = '1001' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			//GSTIN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',3,1003,'37AAJCP8265A1ZA',null,'Y',null,to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND STAT_CD = '1003' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//FOR PIL(PIL GJ)
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'PIPEINFRA' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			//PAN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',1,1001,'AAJCP8265A',null,'Y',null,to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND STAT_CD = '1001' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			//GSTIN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',1,1003,'24AAJCP8265A1ZH',null,'Y',null,to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND STAT_CD = '1003' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//FOR PIL(PIL MH)
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = 'PIPEINFRA' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			stmt.close();
			rset.close();
			
			//PAN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',2,1001,'AAJCP8265A',null,'Y',null,to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND STAT_CD = '1001' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			//GSTIN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "values (?,?,'R',2,1003,'27AAJCP8265A1ZB',null,'Y',null,to_date('12/05/2025 00:00:00','DD/MM/YYYY HH24:MI:SS'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
					+ "AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND STAT_CD = '1003' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			

////////
			//FOR GX(IGX UP)
			cd = "1";		
			
			//GSTIN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?, ?,'G',1,1003,'09AAFCI4600J1ZM',null,'Y',null,to_date('04/09/2025','DD/MM/YYYY'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
				  	+ "AND ENTITY = 'G' AND PLANT_SEQ_NO = '1' AND STAT_CD = '1003' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
			//PAN
			queryString1 = "Insert into FMS_COUNTERPARTY_BU_TAX (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) values (?,?,'G',1,1001,'AAFCI4600J',null,'Y',null,to_date('04/09/2025','DD/MM/YYYY'),1,null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			
			//for data already exists..
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
				  	+ "AND ENTITY = 'G' AND PLANT_SEQ_NO = '1' AND STAT_CD = '1001' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			stmt.setString(2, cd);
			
			rset = stmt.executeQuery();
			
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
//				logger_count++;
			}
			else {
				stmt1.close();
//				skipped_count++; 
			}
			stmt.close();
			rset.close();
			
//	    	queryString1 = "INSERT INTO FMS_COUNTERPARTY_BU_TAX VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_BU_TAX' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_COUNTERPARTY_BU_TAX(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
//			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_BU_TAX_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_BU_TAX_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,ENTITY,PLANT_SEQ_NO,STAT_CD,TIMESTAMP", conn);
				
				String  stat = "";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						String flag = "N";
						data = "";
						abbr = ""; cd = ""; entity = ""; stat = ""; seq = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						if (cellIterator.hasNext()) { 
							abbr = cellIterator.next().getStringCellValue();
							abbr = abbr.substring(1, abbr.length()-1);
							data = company_cd;
							stmt1.setString(index++, data);
						}
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 1 && !abbr.equalsIgnoreCase("SEIPL")) {
								queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if (rset.next()) {
									data = rset.getString(1);
									cd = rset.getString(1);
									rset.close();
									stmt.close();
								}
								else {
									rset.close();
									stmt.close();
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
									}
									rset.close();
									stmt.close();
								}
							}
							else if (cell.getColumnIndex() == 1 && abbr.equalsIgnoreCase("SEIPL")) {
								data = "";
								cd = "";
							}
							/*else if (cell.getColumnIndex() == 10) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 3) {
									seq = cell.getStringCellValue();
									seq = seq.substring(1,seq.length()-1);
								}
								if (cell.getColumnIndex() == 2) {
									entity = cell.getStringCellValue();
									entity = entity.substring(1,entity.length()-1);
								}
								if (cell.getColumnIndex() == 4) {
									stat = cell.getStringCellValue();
									stat = stat.substring(1,stat.length()-1);	
									
									queryString = "	SELECT STAT_CD, UPPER(STAT_NM), STAT_TYPE FROM FMS_GOVT_STAT_TAX ";
									stmt = conn.prepareStatement(queryString);
									rset = stmt.executeQuery();
									
									while (rset.next()) {
										if (stat.contains("-") && stat.split("-")[0].toUpperCase().contains(rset.getString(2)) && flag.equals("N") && stat.split("-")[1].toUpperCase().contains(rset.getString(3))) {
											flag = "Y";
											stat = rset.getString(1);
										}
									}
									rset.close();
									stmt.close();

									if (flag.equals("N")) {
										stat = "";									
									}
									data = stat;
								}
								else {
									data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if(data != null) {
							    		data = data.substring(1, data.length()-1);
							    	}
								}
							}
						    	stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_BU_TAX WHERE COUNTERPARTY_CD = ? AND COMPANY_CD = ? AND ENTITY = ? AND STAT_CD = ? AND PLANT_SEQ_NO = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, company_cd);
						stmt.setString(3, entity);
						stmt.setString(4, stat);
						stmt.setString(5, seq);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !stat.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+" ," + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + stat + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
						   logger.data(fname, (company_cd+" ," + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + stat + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("2 - '" + cd + " - " + abbr + " - " + entity + " - " + seq + " - " + stat + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_BU_TAX>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_BU_TAX>>,,,,,", conn);
			
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
	
	public void FMS_TAX_MST() throws IOException, SQLException {

		function_nm="FMS_TAX_MST()";
		try {

			System.out.println("<<START>><<FMS_TAX_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TAX_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			int max_cd = 100;
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

	    	queryString1 = "INSERT INTO FMS_TAX_MST VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_TAX_MST' ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(TAX_CODE) FROM FMS_TAX_MST";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TAX_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TAX_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CD,SHT_NM,TIMESTAMP", conn);
				
				String  sht_nm = "";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						String  alias_code = "";
						String flag = "N";
						data = "";
						cd = ""; sht_nm = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								max_cd++;
								data = max_cd+"";
								cd = data;
							}
							else if (cell.getColumnIndex() == 1) {
								name = cell.getStringCellValue();
								name = name.substring(1,name.length()-1);
								data = name;
								name = name.toUpperCase();

								queryString = "	SELECT TAX_CODE FROM FMS_TAX_MST WHERE UPPER(TAX_NAME) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, name);
								rset = stmt.executeQuery();
								
								if (rset.next()) {
									flag = "Y";
								}
								rset.close();
								stmt.close();
							}
							else if (cell.getColumnIndex() == 2) {
								alias_code = cell.getStringCellValue();
								alias_code = alias_code.substring(1,alias_code.length()-1);
								data = alias_code;
								alias_code = alias_code.toUpperCase();

								queryString = "	SELECT TAX_CODE FROM FMS_TAX_MST WHERE UPPER(TAX_ALIAS_CODE) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, alias_code);
								rset = stmt.executeQuery();
								
								if (rset.next()) {
									flag = "Y";
								}
								else {
									flag = "N";
								}
								rset.close();
								stmt.close();
							}
							else if (cell.getColumnIndex() == 3) {
								sht_nm = cell.getStringCellValue();
								sht_nm = sht_nm.substring(1,sht_nm.length()-1);
								data = sht_nm;
								sht_nm = sht_nm.toUpperCase();

								queryString = "	SELECT TAX_CODE FROM FMS_TAX_MST WHERE UPPER(SHT_NM) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, sht_nm);
								rset = stmt.executeQuery();
								
								if (rset.next()) {
									flag = "Y";
								}
								rset.close();
								stmt.close();
							}
							/*else if (cell.getColumnIndex() == 8|| cell.getColumnIndex() == 10) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
						}
						
						if (flag.equals("N")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (cd + " , " + sht_nm + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							max_cd--;
							skipped_count++;     
							logger.data(fname, (cd + " , " + sht_nm + ","), conn, "E");
						}
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'" + cd + " - " + sht_nm + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_TAX_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TAX_MST>>,", conn);
			
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
	
	public void FMS_TAX_STRUCTURE() throws IOException, SQLException {

		function_nm="FMS_TAX_STRUCTURE()";
		try {

			System.out.println("<<START>><<FMS_TAX_STRUCTURE>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TAX_STRUCTURE>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			int max_cd = 0;
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

	    	queryString1 = "INSERT INTO FMS_TAX_STRUCTURE VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_TAX_STRUCTURE' ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(TAX_STR_CD) FROM FMS_TAX_STRUCTURE";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TAX_STRUCTURE_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TAX_STRUCTURE_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "CD,DESCR,TIMESTAMP", conn);
				
				String descr="";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						String sts = "";
						String flag = "N";
						data = "";
						cd = ""; name = "";descr="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								max_cd++;
								data = max_cd+"";
								cd = data;
							}
							else if (cell.getColumnIndex() == 1) {
								name = cell.getStringCellValue();
								name = name.substring(1, name.length()-1);
								data = name;
								name = name.toUpperCase();
								descr=name;
								descr=descr.replace(',',' ');
							}
							else if (cell.getColumnIndex() == 2) {
								sts = cell.getStringCellValue();
								sts = sts.substring(1, sts.length()-1);
								data = sts;

								queryString = "	SELECT DESCR FROM FMS_TAX_STRUCTURE WHERE UPPER(DESCR) = ? AND STATUS = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, name);
								stmt.setString(2, sts);
								rset = stmt.executeQuery();
								
								if (rset.next()) {
									flag = "Y";
								}
								rset.close();
								stmt.close();
								
								// Three columns removed in latest update 04/01/2025
								cell = cellIterator.next();
								cell = cellIterator.next();
								cell = cellIterator.next();
							}
							/*else if (cell.getColumnIndex() == 7 || cell.getColumnIndex() == 9) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							stmt1.setString(index++, data);
							
						}
						
						if (flag.equals("N")) {
							
							logger.data(fname, ( cd + " , " +descr + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							max_cd--;
							skipped_count++;     
							logger.data(fname, ( cd + " , " + descr + ","), conn, "E");
						}
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'" + cd + " - " + name + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_TAX_STRUCTURE>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TAX_STRUCTURE>>,", conn);
			

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
	
	public void FMS_TAX_STRUCTURE_DTL() throws IOException, SQLException {

		function_nm="FMS_TAX_STRUCTURE_DTL()";
		try {

			System.out.println("<<START>><<FMS_TAX_STRUCTURE_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TAX_STRUCTURE_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

	    	queryString1 = "INSERT INTO FMS_TAX_STRUCTURE_DTL VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_TAX_STRUCTURE_DTL' ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TAX_STRUCTURE_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TAX_STRUCTURE_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "DESCR,TAX_STR_CD,TAX_CODE,TIMESTAMP", conn);
				
				String  code = "100"; 
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						String alias_code = "", sht_nm = "";
						String flag = "N";
						data = "";
						name = ""; cd = "0"; code = "100"; 
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								name = cell.getStringCellValue();
								name = name.substring(1,name.length()-1);
								
								queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, name);
								rset = stmt.executeQuery();
								if(rset.next()) {
									cd = rset.getString(1);
								}
								rset.close();
								stmt.close();
								
								data = cd;
						    	stmt1.setString(index++, data);
							}
							else if (cell.getColumnIndex() == 1 || cell.getColumnIndex() == 6) {
								name = cell.getStringCellValue();
								name = name.toUpperCase();
								name = name.substring(1,name.length()-1);

								queryString = "	SELECT TAX_CODE FROM FMS_TAX_MST WHERE UPPER(TAX_NAME) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, name);
								rset = stmt.executeQuery();
								
								if (rset.next()) {
									flag = "Y";
									data = rset.getString(1);
							    	stmt1.setString(index++, data);
									if (cell.getColumnIndex() == 1) {
										code = rset.getString(1);
									}
									cell = cellIterator.next();
									cell = cellIterator.next();
								}
								rset.close();
								stmt.close();
							}
							else if (cell.getColumnIndex() == 2 || cell.getColumnIndex() == 7) {
								if (flag.equals("N")) {
									alias_code = cell.getStringCellValue();
									alias_code = alias_code.toUpperCase();
									alias_code = alias_code.substring(1,alias_code.length()-1);

									queryString = "	SELECT TAX_CODE FROM FMS_TAX_MST WHERE UPPER(TAX_ALIAS_CODE) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, alias_code);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										flag = "Y";
										data = rset.getString(1);
								    	stmt1.setString(index++, data);
										if (cell.getColumnIndex() == 2) {
											code = rset.getString(1);
										}
										cell = cellIterator.next();
									}
									rset.close();
									stmt.close();
								}
							}
							else if (cell.getColumnIndex() == 3 || cell.getColumnIndex() == 8) {
								if (flag.equals("N")) {
									sht_nm = cell.getStringCellValue();
									sht_nm = sht_nm.toUpperCase();
									sht_nm = sht_nm.substring(1,sht_nm.length()-1);

									queryString = "	SELECT TAX_CODE FROM FMS_TAX_MST WHERE UPPER(SHT_NM) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sht_nm);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										flag = "Y";
										data = rset.getString(1);
								    	stmt1.setString(index++, data);
										if (cell.getColumnIndex() == 3) {
											code = rset.getString(1);
										}
									}
									else {
										data = null;
								    	stmt1.setString(index++, data);
									}
									rset.close();
									stmt.close();
								}
							}
							else {
								if (cell.getColumnIndex() == 4) {
									flag = "N";
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
						    	stmt1.setString(index++, data);
							}
						}
						
						queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE_DTL WHERE TAX_STR_CD = ? AND TAX_CODE = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, code);
						rset = stmt.executeQuery();
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (name + " , " + cd + " , " + code + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
						   logger.data(fname, ( name + " , " + cd + " , " + code + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("'" + name + " - " + cd + " - " + code + "'"), conn, fname_error, function_nm);
					}*/
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_TAX_STRUCTURE_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL :, "+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED :, "+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TAX_STRUCTURE_DTL>>,,", conn);	
			
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
	
	public void FMS_ENTITY_TAX_STRUCT_DTL() throws IOException, SQLException {

		function_nm="FMS_ENTITY_TAX_STRUCT_DTL()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_TAX_STRUCT_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_TAX_STRUCT_DTL>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			

			String i4 = "";
			// I4
			queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE LIKE '%I4%' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				i4 = rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			// CST 2%(I4) tax for BP -> BPAP2CST under SEIPL-MH
			//
			queryString1 = "Insert into FMS_ENTITY_TAX_STRUCT_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT,INVOICE_TYPE) values (2,43,'T',1,?,'CST 2%',NULL,TO_DATE('24/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/01/2021','DD/MM/YYYY'),'S')";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, i4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD FROM FMS_ENTITY_TAX_STRUCT_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '43'  "
				  	+ " AND ENTITY = 'T' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'S' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2021','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

//	    	queryString1 = "INSERT INTO FMS_ENTITY_TAX_STRUCT_DTL VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_TAX_STRUCT_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_ENTITY_TAX_STRUCT_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT,INVOICE_TYPE) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?)";
//			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_TAX_STRUCT_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_TAX_STRUCT_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,ENTITY,PLANT_SEQ_NO,BU_UNIT,EFF_DT,INVIOCE_TYPE,TIMESTAMP", conn);

				String bu_unit = "", temp_struct = "", tax_struct_dtl = "";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						abbr = ""; cd = ""; seq = ""; entity = ""; bu_unit = ""; eff_dt = "";
						String inv_type = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								abbr = cell.getStringCellValue();
								abbr = abbr.substring(1, abbr.length()-1);
								abbr = abbr.trim();
								data = company_cd;
						    	stmt1.setString(index++, data);
							}
							else if (cell.getColumnIndex() == 1) {
								queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if (rset.next()) {
									data = rset.getString(1);
									cd = rset.getString(1);
									rset.close();
									stmt.close();
								}
								else {
									rset.close();
									stmt.close();
									
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
									}
									rset.close();
									stmt.close();
								}
								rset.close();
								stmt.close();
						    	stmt1.setString(index++, data);
							}
							else if (cell.getColumnIndex() == 4) {
								temp_struct = "";
								String dt = "";
								
//								cell = cellIterator.next();
//								dt = cell.getStringCellValue();
//								dt = dt.substring(1, dt.length()-1);
								dt = "";
								
								cell = cellIterator.next();
								tax_struct_dtl = cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1).replaceAll(",", ", ");
								
								if (!tax_struct_dtl.contains(", ")) {
									queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, tax_struct_dtl);
									rset = stmt.executeQuery();
									if (rset.next()) {
										temp_struct = rset.getString(1);
										dt = rset.getString(2);
									}
									rset.close();
									stmt.close();
								}
								else {

									int flag = 0;
									
									queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, "%"+tax_struct_dtl.split(", ")[0]+"%");
									rset = stmt.executeQuery();
									
									while (rset.next()) {
										
										for (int i = 0; i < tax_struct_dtl.split(", ").length; i++) {
											if (rset.getString(2).contains(tax_struct_dtl.split(", ")[i])) {
												flag = 1;
											}
											else {
												flag = 0;
												break;
											}
										}
										
										if (flag == 1) {
											temp_struct = rset.getString(1);
											tax_struct_dtl = rset.getString(2);
											dt = rset.getString(3);
											break;
										}
									}
									
									rset.close();
									stmt.close();
								}
								
								
								
								data = temp_struct;
						    	stmt1.setString(index++, data);
//								data = dt;
//						    	stmt1.setString(index++, data);
								data = tax_struct_dtl;
						    	stmt1.setString(index++, data);
							}
							/*else if (cell.getColumnIndex() == 9) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
						    	stmt1.setString(index++, data);
							}*/
							else {
								if (cell.getColumnIndex() == 2) {
									entity = cell.getStringCellValue();
									entity = entity.substring(1, entity.length()-1);
								}
								if (cell.getColumnIndex() == 3) {
									seq = cell.getStringCellValue();
									seq = seq.substring(1, seq.length()-1);
								}
								if (cell.getColumnIndex() == 12) {
									bu_unit = cell.getStringCellValue();
									bu_unit = bu_unit.substring(1, bu_unit.length()-1);
								}
								if (cell.getColumnIndex() == 13) {
									eff_dt = cell.getStringCellValue();
									eff_dt = eff_dt.substring(1, eff_dt.length()-1);
								}
								if (cell.getColumnIndex() == 14) {
									inv_type = cell.getStringCellValue();
									inv_type = inv_type.substring(1, inv_type.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
//								System.out.println(index+"===="+data);
						    	stmt1.setString(index++, data);
							}
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TAX_STRUCT_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND PLANT_SEQ_NO = ? AND BU_UNIT = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND INVOICE_TYPE = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, entity);
						stmt.setString(4, seq);
						stmt.setString(5, bu_unit);
						stmt.setString(6, eff_dt);
						stmt.setString(7, inv_type);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !temp_struct.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + bu_unit + " , " + eff_dt + " , " + inv_type + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
						   logger.data(fname, (company_cd+"," + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + bu_unit + " , " + eff_dt + " , " + inv_type + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("2 - '" + cd + " - " + abbr + " - " + entity + " - " + seq + " - " + bu_unit + " - " + eff_dt + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_ENTITY_TAX_STRUCT_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_TAX_STRUCT_DTL>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
			
		}	catch(Exception e) {

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
	
	public void FMS_ENTITY_SERVICE_TAX_DTL() throws IOException, SQLException {

		function_nm="FMS_ENTITY_SERVICE_TAX_DTL()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_SERVICE_TAX_DTL>>");
			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_SERVICE_TAX_DTL>>,,,,,,,", conn);
			
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_ENTITY_SERVICE_TAX_DTL VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_SERVICE_TAX_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_ENTITY_SERVICE_TAX_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY'))";
			
//			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_SERVICE_TAX_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_SERVICE_TAX_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,ENTITY,PLANT_SEQ_NO,INVOICE_TYPE,EFF_DT,BU_UNIT,TIMESTAMP", conn);

				String  type = "", dt = "", bu_unit = "", temp_struct = "", tax_struct_dtl = "";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						temp_struct = "";
						data = "";
						abbr = ""; cd = ""; seq = ""; entity = ""; type = ""; dt = ""; bu_unit = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								abbr = cell.getStringCellValue();
								abbr = abbr.substring(1, abbr.length()-1);
								abbr = abbr.trim();
								data = company_cd;
							}
							else if (cell.getColumnIndex() == 1) {
								queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if (rset.next()) {
									data = rset.getString(1);
									cd = rset.getString(1);
									rset.close();
									stmt.close();
								}
								else {
									rset.close();
									stmt.close();
									
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
									}
									rset.close();
									stmt.close();
								}
								
							}
							else if (cell.getColumnIndex() == 4) {
								
//								cell = cellIterator.next();
//								dt = cell.getStringCellValue();
//								dt = dt.substring(1, dt.length()-1);
								dt = "";
								
								cell = cellIterator.next();
								type = cell.getStringCellValue().equals("'null'") ? "'0'" : cell.getStringCellValue();
								type = type.substring(1, type.length()-1);
								
								cell = cellIterator.next();
								
								tax_struct_dtl = cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1).replaceAll(",", ", ");
								
								if (!tax_struct_dtl.contains(", ")) {
									queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, tax_struct_dtl);
									rset = stmt.executeQuery();
									if (rset.next()) {
										temp_struct = rset.getString(1);
										dt = rset.getString(2);
									}
									rset.close();
									stmt.close();
								}
								else {

									int flag = 0;
									
									queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, "%"+tax_struct_dtl.split(", ")[0]+"%");
									rset = stmt.executeQuery();
									
									while (rset.next()) {
										
										for (int i = 0; i < tax_struct_dtl.split(", ").length; i++) {
											if (rset.getString(2).contains(tax_struct_dtl.split(", ")[i])) {
												flag = 1;
											}
											else {
												flag = 0;
												break;
											}
										}
										
										if (flag == 1) {
											temp_struct = rset.getString(1);
											tax_struct_dtl = rset.getString(2);
											dt = rset.getString(3);
											break;
										}
									}
									
									rset.close();
									stmt.close();
								}
								
								data = temp_struct;
						    	stmt1.setString(index++, data);
//								data = dt;
//						    	stmt1.setString(index++, data);
								data = type;
						    	stmt1.setString(index++, data);
								data = tax_struct_dtl;

							}
							/*else if (cell.getColumnIndex() == 10) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 2) {
									entity = cell.getStringCellValue();
									entity = entity.substring(1, entity.length()-1);
								}
								if (cell.getColumnIndex() == 3) {
									seq = cell.getStringCellValue();
									seq = seq.substring(1, seq.length()-1);
								}
								if (cell.getColumnIndex() == 13) {
									bu_unit = cell.getStringCellValue();
									bu_unit = bu_unit.substring(1, bu_unit.length()-1);
								}
								if (cell.getColumnIndex() == 14) {
									dt = cell.getStringCellValue();
									dt = dt.substring(1, dt.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}

							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_SERVICE_TAX_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND PLANT_SEQ_NO = ? AND INVOICE_TYPE = ? AND BU_UNIT = ?  AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, entity);
						stmt.setString(4, seq);
						stmt.setString(5, type);
						stmt.setString(6, "1");
						stmt.setString(7, dt);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !temp_struct.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+" ," + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + type + " , " + dt + " , " + bu_unit + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
						   logger.data(fname, (company_cd+" ," + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + type + " , " + dt + " , " + bu_unit + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("2 - '" + cd + " - " + abbr + " - " + entity + " - " + seq + " - " + type + " - " + dt + " - " + bu_unit + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_ENTITY_SERVICE_TAX_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_SERVICE_TAX_DTL>>,,,,,,,", conn);
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
	
	public void FMS_ENTITY_BU_SVC_TAX_DTL() throws IOException, SQLException {

		function_nm="FMS_ENTITY_BU_SVC_TAX_DTL()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_BU_SVC_TAX_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_BU_SVC_TAX_DTL>>,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			String ga = "", g4 = "", gbgc = "", g5g6 = "";
			
			// IGST 12%
			queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE LIKE '%GA%' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				ga = rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			// IGST 18%
			queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE LIKE '%G4%' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				g4 = rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			// CGST 6%, SGST 6%
			queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE LIKE '%GB, GC%' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				gbgc = rset.getString(1);
			}
			rset.close();
			stmt.close();

			// CGST 9%, SGST 9%
			queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE LIKE '%G5, G6%' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				g5g6 = rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			
			//DATA FOR TRANSPORTER BU TAX 
			//FOR GAIL
			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('19/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('19/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('19/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('19/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('19/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('19/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('19/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('19/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('19/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			// For Plant GAIL GJ
			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',2,?,'IC','CGST 6%, SGST 6%',null,to_date('21/08/2025','DD/MM/YYYY'),1,null,null,'Y',1,to_date('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',2,?,'IC','IGST 12%',null,to_date('21/08/2025','DD/MM/YYYY'),1,null,null,'Y',2,to_date('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',2,?,'IC','IGST 12%',null,to_date('21/08/2025','DD/MM/YYYY'),1,null,null,'Y',3,to_date('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',2,?,'PC','CGST 9%, SGST 9%',null,to_date('21/08/2025','DD/MM/YYYY'),1,null,null,'Y',1,to_date('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g5g6);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',2,?,'PC','IGST 18%',null,to_date('21/08/2025','DD/MM/YYYY'),1,null,null,'Y',2,to_date('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',2,?,'PC','IGST 18%',null,to_date('21/08/2025','DD/MM/YYYY'),1,null,null,'Y',3,to_date('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',2,?,'TC','CGST 6%, SGST 6%',null,to_date('21/08/2025','DD/MM/YYYY'),1,null,null,'Y',1,to_date('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',2,?,'TC','IGST 12%',null,to_date('21/08/2025','DD/MM/YYYY'),1,null,null,'Y',2,to_date('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,4,'R',2,?,'TC','IGST 12%',null,to_date('21/08/2025','DD/MM/YYYY'),1,null,null,'Y',3,to_date('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '4'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();
			
			
			//FOR PipeInfra
			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',2,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',3,?,'TC','CGST 6%, SGST 6%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',2,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',3,?,'IC','CGST 6%, SGST 6%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',2,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',3,?,'PC','CGST 9%, SGST 9%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g5g6);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',2,?,'TC','CGST 6%, SGST 6%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',3,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',2,?,'IC','CGST 6%, SGST 6%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',3,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',2,?,'PC','CGST 9%, SGST 9%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g5g6);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',2,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',3,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',1,?,'TC','CGST 6%, SGST 6%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',3,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',1,?,'IC','CGST 6%, SGST 6%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',2,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',3,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',1,?,'PC','CGST 9%, SGST 9%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g5g6);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',2,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,29,'R',3,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/01/2020','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '29'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '3' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/01/2020','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();
			
			

			//FOR GSPL
			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,45,'R',1,?,'TC','CGST 6%, SGST 6%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '45'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,45,'R',1,?,'IC','CGST 6%, SGST 6%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '45'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,45,'R',1,?,'PC','CGST 9%, SGST 9%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g5g6);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '45'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,45,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '45'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,45,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '45'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,45,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '45'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,45,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '45'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,45,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '45'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,45,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('01/07/2017','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '45'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/07/2017','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();
			
			

			//FOR RGPL
			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,53,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('29/06/2022','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '53'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('29/06/2022','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,53,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('29/06/2022','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '53'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('29/06/2022','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,53,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('29/06/2022','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '53'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('29/06/2022','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,53,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('29/06/2022','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '53'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('29/06/2022','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,53,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('29/06/2022','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '53'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('29/06/2022','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,53,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('29/06/2022','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '53'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('29/06/2022','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,53,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('29/06/2022','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '53'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('29/06/2022','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,53,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('29/06/2022','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '53'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('29/06/2022','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,53,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('29/06/2022','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '53'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('29/06/2022','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();
			
			

			//FOR GSINLIM
			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,134,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('13/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '134'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('13/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,134,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('13/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '134'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('13/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,134,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('13/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '134'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('13/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,134,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('13/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '134'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('13/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,134,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('13/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '134'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('13/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,134,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('13/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '134'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('13/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,134,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('13/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '134'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('13/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,134,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('13/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '134'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('13/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,134,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('13/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '134'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('13/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();
			
			

			//FOR GSIGLIM
			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',2,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',2,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',2,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',2,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',2,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',2,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',2,?,'IC','CGST 6%, SGST 6%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',1,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',1,?,'TC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',2,?,'PC','CGST 9%, SGST 9%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g5g6);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',1,?,'PC','IGST 18%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'PC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',2,?,'TC','CGST 6%, SGST 6%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',1,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, gbgc);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'TC' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,168,'R',2,?,'IC','IGST 12%',NULL,TO_DATE('20/06/2025','DD/MM/YYYY'),1,NULL,NULL,'Y',3,TO_DATE('18/05/2023','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, ga);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '168'  "
				  	+ " AND ENTITY = 'R' AND PLANT_SEQ_NO = '2' AND INVOICE_TYPE = 'IC' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('18/05/2023','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();
			

			// GX 
			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,1,'G',1,?,'TX','IGST 18%',null,to_date('04/09/2025','DD/MM/YYYY'),1,null,null,'Y',1,to_date('01/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '1'  "
				  	+ " AND ENTITY = 'G' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TX' AND BU_UNIT = '1' AND EFF_DT = TO_DATE('01/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,1,'G',1,?,'TX','IGST 18%',null,to_date('04/09/2025','DD/MM/YYYY'),1,null,null,'Y',2,to_date('01/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '1'  "
				  	+ " AND ENTITY = 'G' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TX' AND BU_UNIT = '2' AND EFF_DT = TO_DATE('01/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();

			//
			queryString1 = "Insert into FMS_ENTITY_BU_SVC_TAX_DTL (COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) values (2,1,'G',1,?,'TX','IGST 18%',null,to_date('04/09/2025','DD/MM/YYYY'),1,null,null,'Y',3,to_date('01/01/2025','DD/MM/YYYY'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, g4);
				
			//for data already exists..
		    queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, ENTITY, PLANT_SEQ_NO, EFF_DT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = '1'  "
				  	+ " AND ENTITY = 'G' AND PLANT_SEQ_NO = '1' AND INVOICE_TYPE = 'TX' AND BU_UNIT = '3' AND EFF_DT = TO_DATE('01/01/2025','DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
				
			if (!rset.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
			}
			else {
				stmt1.close();
			}
			stmt.close();
			rset.close();
			

//	    	queryString1 = "INSERT INTO FMS_ENTITY_BU_SVC_TAX_DTL VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_BU_SVC_TAX_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_ENTITY_BU_SVC_TAX_DTL(COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT) VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY'))";
			
//			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_BU_SVC_TAX_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_BU_SVC_TAX_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,ENTITY,PLANT_SEQ_NO,INVOICE_TYPE,BU_UNIT,EFF_DT,TIMESTAMP", conn);

				String type = "", dt = "", bu_unit = "", temp_struct = "", tax_struct_dtl = "";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						temp_struct = "";
						data = "";
						abbr = ""; cd = ""; seq = ""; entity = ""; type = ""; dt = ""; bu_unit = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							if (cell.getColumnIndex() == 0) {
								abbr = cell.getStringCellValue();
								abbr = abbr.substring(1, abbr.length()-1);
								abbr = abbr.trim();
								data = company_cd;
							}
							else if (cell.getColumnIndex() == 1) {
								queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? AND STATUS = 'Y' GROUP BY EFF_DT, COUNTERPARTY_CD ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if (rset.next()) {
									data = rset.getString(1);
									cd = rset.getString(1);
									rset.close();
									stmt.close();
								}
								else {
									rset.close();
									stmt.close();
									
									queryString = "SELECT COUNTERPARTY_CD, MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? GROUP BY EFF_DT, COUNTERPARTY_CD ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, abbr);
									rset = stmt.executeQuery();
									if (rset.next()) {
										data = rset.getString(1);
										cd = rset.getString(1);
									}
									rset.close();
									stmt.close();
								}
								
							}
							else if (cell.getColumnIndex() == 4) {
								
//								cell = cellIterator.next();
//								dt = cell.getStringCellValue();
//								dt = dt.substring(1, dt.length()-1);
								dt = "";
								
								cell = cellIterator.next();
								type = cell.getStringCellValue().equals("'null'") ? "'0'" : cell.getStringCellValue();
								type = type.substring(1, type.length()-1);
								
								cell = cellIterator.next();
								
								tax_struct_dtl = cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1).replaceAll(",", ", ");
								
								if (!tax_struct_dtl.contains(", ")) {
									queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, tax_struct_dtl);
									rset = stmt.executeQuery();
									if (rset.next()) {
										temp_struct = rset.getString(1);
										dt = rset.getString(2);
									}
									rset.close();
									stmt.close();
								}
								else {

									int flag = 0;
									
									queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, "%"+tax_struct_dtl.split(", ")[0]+"%");
									rset = stmt.executeQuery();
									
									while (rset.next()) {
										
										for (int i = 0; i < tax_struct_dtl.split(", ").length; i++) {
											if (rset.getString(2).contains(tax_struct_dtl.split(", ")[i])) {
												flag = 1;
											}
											else {
												flag = 0;
												break;
											}
										}
										
										if (flag == 1) {
											temp_struct = rset.getString(1);
											tax_struct_dtl = rset.getString(2);
											dt = rset.getString(3);
											break;
										}
									}
									
									rset.close();
									stmt.close();
								}
								
								data = temp_struct;
						    	stmt1.setString(index++, data);
//								data = dt;
//						    	stmt1.setString(index++, data);
								data = type;
						    	stmt1.setString(index++, data);
								data = tax_struct_dtl;

							}
							/*else if (cell.getColumnIndex() == 10) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 2) {
									entity = cell.getStringCellValue();
									entity = entity.substring(1, entity.length()-1);
								}
								if (cell.getColumnIndex() == 3) {
									seq = cell.getStringCellValue();
									seq = seq.substring(1, seq.length()-1);
								}
								if (cell.getColumnIndex() == 13) {
									bu_unit = cell.getStringCellValue();
									bu_unit = bu_unit.substring(1, bu_unit.length()-1);
								}
								if (cell.getColumnIndex() == 14) {
									dt = cell.getStringCellValue();
									dt = dt.substring(1, dt.length()-1);
								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
							
							stmt1.setString(index++, data);
							
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND PLANT_SEQ_NO = ? AND INVOICE_TYPE = ? AND BU_UNIT = ?  AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, entity);
						stmt.setString(4, seq);
						stmt.setString(5, type);
						stmt.setString(6, "1");
						stmt.setString(7, dt);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !temp_struct.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+" ," + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + type + " , " + bu_unit + " , " + dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+"," + cd + " , " + abbr + " , " + entity + " , " + seq + " , " + type + " , " + bu_unit + " , " + dt + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("2 - '" + cd + " - " + abbr + " - " + entity + " - " + seq + " - " + type + " - " + bu_unit + " - " + dt + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_ENTITY_BU_SVC_TAX_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_BU_SVC_TAX_DTL>>,,,,,,,", conn);
			
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
	
	public void FMS_CUSTOM_TAX_STRUCT_DTL() throws IOException, SQLException 
	{

		function_nm="FMS_CUSTOM_TAX_STRUCT_DTL()";
		try {

			System.out.println("<<START>><<FMS_CUSTOM_TAX_STRUCT_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_CUSTOM_TAX_STRUCT_DTL>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_CUSTOM_TAX_STRUCT_DTL VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_CUSTOM_TAX_STRUCT_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_CUSTOM_TAX_STRUCT_DTL(EFF_DT,TAX_STRUCT_CD,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,COMPANY_CD) VALUES(TO_DATE(?, 'DD/MM/YYYY'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CUSTOM_TAX_STRUCT_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CUSTOM_TAX_STRUCT_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,TAX_STRUCT_CD,TAX_STRUCT_DTL,EFF_DT,TIMESTAMP", conn);

				String dt = "", tax_struct_cd = "", tax_struct_dtl = "", temp_struct = "";
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						data = "";
						dt = ""; tax_struct_cd = ""; tax_struct_dtl = "" ;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							
							if (cell.getColumnIndex() == 1) {
								temp_struct = "";
								
//								cell = cellIterator.next();
//								dt = cell.getStringCellValue();
//								dt = dt.substring(1, dt.length()-1);
								dt = "";
								
								cell = cellIterator.next();
								
								tax_struct_dtl = cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1);
								
								if (!tax_struct_dtl.contains(", ")) {
									queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, tax_struct_dtl);
									rset = stmt.executeQuery();
									if (rset.next()) {
										temp_struct = rset.getString(1);
										dt = rset.getString(2);
									}
									rset.close();
									stmt.close();
								}
								else {

									int flag = 0;
									
									queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, "%"+tax_struct_dtl.split(", ")[0]+"%");
									rset = stmt.executeQuery();
									
									while (rset.next()) {
										
										for (int i = 0; i < tax_struct_dtl.split(", ").length; i++) {
											if (rset.getString(2).contains(tax_struct_dtl.split(", ")[i])) {
												flag = 1;
											}
											else {
												flag = 0;
												break;
											}
										}
										
										if (flag == 1) {
											temp_struct = rset.getString(1);
											tax_struct_dtl = rset.getString(2);
											dt = rset.getString(3);
											break;
										}
									}
									
									rset.close();
									stmt.close();
								}
								
								data = temp_struct;
						    	stmt1.setString(index++, data);
//								data = dt;
//						    	stmt1.setString(index++, data);
								data = tax_struct_dtl;
						    	
							}
							/*else if (cell.getColumnIndex() == 6) {	// Emp_Cd
								queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cell.getStringCellValue());
								rset = stmt.executeQuery();
								if (rset.next()) {
						    		data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}*/
							else {
								if (cell.getColumnIndex() == 0) {
									eff_dt = cell.getStringCellValue();
									eff_dt = eff_dt.substring(1, eff_dt.length()-1);
								}
//								if (cell.getColumnIndex() == 3) {
//									tax_struct_dtl = cell.getStringCellValue();
//									tax_struct_dtl = tax_struct_dtl.substring(1, tax_struct_dtl.length()-1);
//								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
//							System.out.println(index+"===="+data);
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT TAX_STRUCT_CD FROM FMS_CUSTOM_TAX_STRUCT_DTL WHERE COMPANY_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, eff_dt);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !temp_struct.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + tax_struct_cd + " , " + tax_struct_dtl + " , " + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							skipped_count++;     
						   logger.data(fname, (company_cd+"," + tax_struct_cd + " , " + tax_struct_dtl + " , " + dt + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, ("2 - '" + tax_struct_cd + " - " + tax_struct_dtl + " - " + dt + "'"), conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
			
			System.out.println("<<END>><<FMS_CUSTOM_TAX_STRUCT_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_CUSTOM_TAX_STRUCT_DTL>>,,,", conn);
			
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
	
	public void FMS_HOLIDAY_DTL() throws SQLException, IOException 
	{
		
		function_nm="FMS_HOLIDAY_DTL()";
		try {
			
			
			System.out.println("<<START>><<FMS_HOLIDAY_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_HOLIDAY_DTL>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

//	    	queryString1 = "INSERT INTO FMS_HOLIDAY_DTL VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_HOLIDAY_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_HOLIDAY_DTL(HOLIDAY_DT,HOLIDAY_NM,HOLIDAY_DAY,STATE_TIN,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,ENT_PROFILE,MOD_PROFILE) VALUES(TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
		    //DIYA 20250226 :REMOVE RSET.CLOSE
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_HOLIDAY_DTL_"+start_end_dt+".xlsx");

			if(file1.exists()) {
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_HOLIDAY_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "HOLIDAY_DT,HOLIDAY_NM,TIMESTAMP,", conn);
				
				String holi_dt = "", holi_nm = "",state_tin="";
				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						holi_dt = ""; holi_nm = ""; state_tin="";
						data = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							
								if (cell.getColumnIndex() == 0) {
									holi_dt = cell.getStringCellValue().toUpperCase();
									holi_dt = holi_dt.substring(1, holi_dt.length()-1);
								}
								if (cell.getColumnIndex() == 1) {
									holi_nm = cell.getStringCellValue().toUpperCase();
									holi_nm = holi_nm.substring(1, holi_nm.length()-1);
								}
								if (cell.getColumnIndex() == 3) {
									state_tin = cell.getStringCellValue().toUpperCase();
									state_tin = state_tin.substring(1, state_tin.length()-1);
								}
							
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							//System.out.println(index+"=="+data);
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT HOLIDAY_DT FROM FMS_HOLIDAY_DTL WHERE HOLIDAY_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND STATE_TIN = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, holi_dt);
						stmt.setString(2, state_tin);
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, ( holi_dt + " , " + holi_nm + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {	
							stmt1.close();
							skipped_count++;     
							logger.data(fname, ( holi_dt + " , " + holi_nm + ","), conn, "E");
						}
						rset.close();
						stmt.close();
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, abbr, conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			
			System.out.println("<<END>><<FMS_HOLIDAY_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_HOLIDAY_DTL>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

			
		}
		
		catch(Exception e) {

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
	
	public void FMS_PRODUCT_MST() throws SQLException, IOException 
	{
		
		function_nm="FMS_PRODUCT_MST()";
		try {
			
			
			System.out.println("<<START>><<FMS_PRODUCT_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_PRODUCT_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			int prod_cd = 1;


			queryString = "SELECT NVL(MAX(PROD_CD), 0)+1 FROM FMS_PRODUCT_MST ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if(rset.next()) {
				prod_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();

			logger.checkpoint(fname, "PROD_CD,PROD_ABBR,TIMESTAMP,", conn);

			
			// DOM GAS (CAPPED)
			queryString1 = "INSERT INTO FMS_PRODUCT_MST(PROD_CD,PROD_NM,PROD_ABBR,PROD_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,PROD_FLAG,ENT_PROFILE,MOD_PROFILE) VALUES(?,'Domestic Gas (Capped)','DOM GAS (CAPPED)','Used to identify molecules supplied from DOM gas capped','1',TO_DATE(SYSDATE, 'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,'Y',?,NULL)";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setInt(1, prod_cd);
			stmt1.setString(2, company_cd);
			queryString = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_NM = 'Domestic Gas (Capped)' AND PROD_ABBR = 'DOM GAS (CAPPED)' AND PROD_DESC = 'Used to identify molecules supplied from DOM gas capped' AND PROD_FLAG = 'Y' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (!rset.next()) {
				stmt1.executeUpdate();
				stmt1.close();
				
				logger.data(fname, ( (prod_cd++) + " , " + "DOM GAS (CAPPED)" + ","), conn, "");
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++;     
				logger.data(fname, ( (prod_cd) + " , " + "DOM GAS (CAPPED)" + ","), conn, "E");
			}
			rset.close();
			stmt.close();
			
			// RLNG
			queryString1 = "INSERT INTO FMS_PRODUCT_MST(PROD_CD,PROD_NM,PROD_ABBR,PROD_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,PROD_FLAG,ENT_PROFILE,MOD_PROFILE) VALUES(?,'Regassified LNG','RLNG',NULL,'1',TO_DATE(SYSDATE, 'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,'Y',?,NULL)";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setInt(1, prod_cd);
			stmt1.setString(2, company_cd);
			
			queryString = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_NM = 'Regassified LNG' AND PROD_ABBR = 'RLNG' AND PROD_DESC IS NULL AND PROD_FLAG = 'Y' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (!rset.next()) {
				stmt1.executeUpdate();
				stmt1.close();
				
				logger.data(fname, ( (prod_cd++) + " , " + "RLNG" + ","), conn, "");
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++;     
				logger.data(fname, ( (prod_cd) + " , " + "RLNG" + ","), conn, "E");
			}
			rset.close();
			stmt.close();

			
			// DLNG
			queryString1 = "INSERT INTO FMS_PRODUCT_MST(PROD_CD,PROD_NM,PROD_ABBR,PROD_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,PROD_FLAG,ENT_PROFILE,MOD_PROFILE) VALUES(?,'Downstream LNG','DLNG',NULL,'1',TO_DATE(SYSDATE, 'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,'Y',?,NULL)";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setInt(1, prod_cd);
			stmt1.setString(2, company_cd);
			
			queryString = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_NM = 'Downstream LNG' AND PROD_ABBR = 'DLNG' AND PROD_DESC IS NULL AND PROD_FLAG = 'Y' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (!rset.next()) {
				stmt1.executeUpdate();
				stmt1.close();
				
				logger.data(fname, ( (prod_cd++) + " , " + "DLNG" + ","), conn, "");
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++;     
				logger.data(fname, ( (prod_cd) + " , " + "DLNG" + ","), conn, "E");
			}
			rset.close();
			stmt.close();
			
			
			// DOMGAS
			queryString1 = "INSERT INTO FMS_PRODUCT_MST(PROD_CD,PROD_NM,PROD_ABBR,PROD_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,PROD_FLAG,ENT_PROFILE,MOD_PROFILE) VALUES(?,'Domestic Gas','DOMGAS',NULL,'1',TO_DATE(SYSDATE, 'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,'Y',?,NULL)";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setInt(1, prod_cd);
			stmt1.setString(2, company_cd);
			
			queryString = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_NM = 'Domestic Gas' AND PROD_ABBR = 'DOMGAS' AND PROD_DESC IS NULL AND PROD_FLAG = 'Y' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (!rset.next()) {
				stmt1.executeUpdate();
				stmt1.close();
				
				logger.data(fname, ( (prod_cd++) + " , " + "DOMGAS" + ","), conn, "");
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++;     
				logger.data(fname, ( (prod_cd) + " , " + "DOMGAS" + ","), conn, "E");
			}
			rset.close();
			stmt.close();
			
			
			// TPLNG SagarB20250828 added new product
			queryString1 = "INSERT INTO FMS_PRODUCT_MST(PROD_CD,PROD_NM,PROD_ABBR,PROD_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,PROD_FLAG,ENT_PROFILE,MOD_PROFILE) VALUES(?,'Third Party LNG','TPLNG',NULL,'1',TO_DATE('28/08/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,'Y',?,NULL)";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setInt(1, prod_cd);
			stmt1.setString(2, company_cd);
			
			queryString = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_NM = 'Third Party LNG' AND PROD_ABBR = 'TPLNG' AND PROD_DESC IS NULL AND PROD_FLAG = 'Y' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (!rset.next()) {
				stmt1.executeUpdate();
				stmt1.close();
				
				logger.data(fname, ( (prod_cd++) + " , " + "DOMGAS" + ","), conn, "");
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++;     
				logger.data(fname, ( (prod_cd) + " , " + "DOMGAS" + ","), conn, "E");
			}
			rset.close();
			stmt.close();
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_PRODUCT_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_PRODUCT_MST>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

			
		}
		
		catch(Exception e) {

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
	
//	public void FMS_PRODUCT_MOLECULE_MST() throws SQLException, IOException 
//	{
//		
//		function_nm="FMS_PRODUCT_MOLECULE_MST()";
//		try {
//			
//			
//			System.out.println("<<START>><<FMS_PRODUCT_MOLECULE_MST>>");
//			
//			logger.checkpoint(fname, "\n<<START>>,<<FMS_PRODUCT_MOLECULE_MST>>,", conn);
//			
//			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
//			
//			data = "";
//			logger_count = 0;   
//			skipped_count = 0;   
//			total_count = 0; 
//			
//			int mole_cd = 1, prod_cd = 0;
//
//
//			queryString = "SELECT NVL(MAX(MOLE_CD), 0)+1 FROM FMS_PRODUCT_MOLECULE_MST ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			if(rset.next()) {
//				mole_cd = rset.getInt(1);
//			}
//			rset.close();
//			stmt.close();
//
//			logger.checkpoint(fname, "MOLE_CD,MOLE_ABBR,TIMESTAMP,", conn);
//
//			
//			// D6 - DOM GAS (CAPPED)
//			queryString = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_ABBR = 'DOM GAS (CAPPED)' ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			if(rset.next()) {
//				prod_cd = rset.getInt(1);
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = "INSERT INTO FMS_PRODUCT_MOLECULE_MST(PROD_CD,MOLE_CD,MOLE_NM,MOLE_ABBR,MOLE_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,MOLE_FLAG,ENT_PROFILE,MOD_PROFILE) VALUES(?, ?, 'D6', 'D6', NULL, '1', TO_DATE(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, 'Y', ?, NULL)";
//			stmt1 = conn.prepareStatement(queryString1);
//			stmt1.setInt(1, prod_cd);
//			stmt1.setInt(2, mole_cd);
//			stmt1.setString(3, company_cd);
//			
//			queryString = "SELECT MOLE_CD FROM FMS_PRODUCT_MOLECULE_MST WHERE MOLE_NM = 'D6' AND MOLE_ABBR = 'D6' AND MOLE_DESC IS NULL AND MOLE_FLAG = 'Y' AND PROD_CD = ? ";
//			stmt = conn.prepareStatement(queryString);
//			stmt.setInt(1, prod_cd);
//			rset = stmt.executeQuery();
//			
//			if (!rset.next() && prod_cd != 0) {
//				stmt1.executeUpdate();
//				stmt1.close();
//				
//				logger.data(fname, ( (mole_cd++) + " , " + "D6" + ","), conn, "");
//				logger_count++;
//			}
//			else {
//				skipped_count++;     
//				logger.data(fname, ( (mole_cd) + " , " + "D6" + ","), conn, "E");
//			}
//			rset.close();
//			stmt.close();
//			
//
//			// D6 - DOMGAS
//			prod_cd = 0;
//			queryString = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_ABBR = 'DOMGAS' ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			if(rset.next()) {
//				prod_cd = rset.getInt(1);
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = "INSERT INTO FMS_PRODUCT_MOLECULE_MST(PROD_CD,MOLE_CD,MOLE_NM,MOLE_ABBR,MOLE_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,MOLE_FLAG,ENT_PROFILE,MOD_PROFILE) VALUES(?, ?, 'D6', 'D6', NULL, '1', TO_DATE(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, 'Y', ?, NULL)";
//			stmt1 = conn.prepareStatement(queryString1);
//			stmt1.setInt(1, prod_cd);
//			stmt1.setInt(2, mole_cd);
//			stmt1.setString(3, company_cd);
//			
//			queryString = "SELECT MOLE_CD FROM FMS_PRODUCT_MOLECULE_MST WHERE MOLE_NM = 'D6' AND MOLE_ABBR = 'D6' AND MOLE_DESC IS NULL AND MOLE_FLAG = 'Y' AND PROD_CD = ? ";
//			stmt = conn.prepareStatement(queryString);
//			stmt.setInt(1, prod_cd);
//			rset = stmt.executeQuery();
//			
//			if (!rset.next() && prod_cd != 0) {
//				stmt1.executeUpdate();
//				stmt1.close();
//				
//				logger.data(fname, ( (mole_cd++) + " , " + "D6" + ","), conn, "");
//				logger_count++;
//			}
//			else {
//				skipped_count++;     
//				logger.data(fname, ( (mole_cd) + " , " + "D6" + ","), conn, "E");
//			}
//			rset.close();
//			stmt.close();
//			
//
//			// Imported - RLNG
//			prod_cd = 0;
//			queryString = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_ABBR = 'RLNG' ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			if(rset.next()) {
//				prod_cd = rset.getInt(1);
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = "INSERT INTO FMS_PRODUCT_MOLECULE_MST(PROD_CD,MOLE_CD,MOLE_NM,MOLE_ABBR,MOLE_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,MOLE_FLAG,ENT_PROFILE,MOD_PROFILE) VALUES(?, ?, 'Imported', 'Imported', NULL, '1', TO_DATE(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, 'Y', ?, NULL)";
//			stmt1 = conn.prepareStatement(queryString1);
//			stmt1.setInt(1, prod_cd);
//			stmt1.setInt(2, mole_cd);
//			stmt1.setString(3, company_cd);
//			
//			queryString = "SELECT MOLE_CD FROM FMS_PRODUCT_MOLECULE_MST WHERE MOLE_NM = 'Imported' AND MOLE_ABBR = 'Imported' AND MOLE_DESC IS NULL AND MOLE_FLAG = 'Y' AND PROD_CD = ? ";
//			stmt = conn.prepareStatement(queryString);
//			stmt.setInt(1, prod_cd);
//			rset = stmt.executeQuery();
//			
//			if (!rset.next() && prod_cd != 0) {
//				stmt1.executeUpdate();
//				stmt1.close();
//				
//				logger.data(fname, ( (mole_cd++) + " , " + "Imported" + ","), conn, "");
//				logger_count++;
//			}
//			else {
//				skipped_count++;     
//				logger.data(fname, ( (mole_cd) + " , " + "Imported" + ","), conn, "E");
//			}
//			rset.close();
//			stmt.close();
//			
//			
//			msg = "Data has been Inserted Successfully in Database.";
//			msg_type = "S";
//			
//			System.out.println("<<END>><<FMS_PRODUCT_MOLECULE_MST>>");
//			System.out.println();
//
//			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
//
//			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
//			
//			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
//			
//			logger.checkpoint(fname, "<<END>>,<<FMS_PRODUCT_MOLECULE_MST>>,", conn);
//			
//			logger.checkpoint1(fname1,logger_count+",", conn);
//
//			
//		}
//		
//		catch(Exception e) {
//
//			msg = "One of the Functions faced an Error. Data Not Inserted.";
//			msg_type = "E";
//			
//			conn.rollback();
//			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//			logger.error(fname, e, function_nm, conn, fname_error);
//		}
//		finally {
//			conn.commit();
//			if (file != null) {
//				file.close();
//			}
//		}		
//	}
	
	
	public void FMS_PRODUCT_MOLECULE_MST() throws SQLException, IOException 
	{
		
		function_nm="FMS_PRODUCT_MOLECULE_MST()";
		try {
			
			
			System.out.println("<<START>><<FMS_PRODUCT_MOLECULE_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_PRODUCT_MOLECULE_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			logger.checkpoint(fname, "MOLE_CD,MOLE_ABBR,TIMESTAMP,", conn);

			queryString1 = "INSERT INTO FMS_PRODUCT_MOLECULE_MST"
					+ "(PROD_CD,MOLE_CD,MOLE_NM,MOLE_ABBR,MOLE_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,MOLE_FLAG,ENT_PROFILE,MOD_PROFILE) "
					+ "VALUES(?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?, ?, ?, ?, ?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_PRODUCT_MOLECULE_MST_"+start_end_dt+".xlsx");
			
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_PRODUCT_MOLECULE_MST_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "MOLE_CD,MOLE_ABBR,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {  

					row = rowIterator.next();
					data = null;

					index = 1;
					String mol_cd="",prod_cd="";
					stmt1 = conn.prepareStatement(queryString1);
					
					cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						data = null;
						
						if (cell.getColumnIndex() == 0) {	//pro_cd
							prod_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(prod_cd != null) {
								prod_cd = prod_cd.substring(1, prod_cd.length()-1);
							}
							data=prod_cd;
						}
						else if (cell.getColumnIndex() == 1) {	// mol_cd
							mol_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(mol_cd != null) {
								mol_cd = mol_cd.substring(1, mol_cd.length()-1);
							}
							data=mol_cd;
						}
						else {
							
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
						}
						
//						System.out.println(index+"===>"+data);
						stmt1.setString(index++, data);
					}
				
					queryString = "SELECT MOLE_CD FROM FMS_PRODUCT_MOLECULE_MST WHERE PROD_CD = ? AND  MOLE_CD = ?";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, prod_cd);
					stmt.setString(2, mol_cd);
					rset = stmt.executeQuery();
					
					if (!rset.next() && prod_cd != null && !prod_cd.equals("0")) {
						stmt1.executeUpdate();
						stmt1.close();
						
						logger.data(fname, ( mol_cd + " , " + prod_cd + ","), conn, "");
						logger_count++;
					}
					else {
						skipped_count++;     
						logger.data(fname, (mol_cd + " , " +prod_cd + ","), conn, "E");
					}
					rset.close();
					stmt.close();			
				
				}
			
			}
			
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_PRODUCT_MOLECULE_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_PRODUCT_MOLECULE_MST>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

			
		}
		
		catch(Exception e) {

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
	
	
	public void FMS_SAC_MST() throws SQLException, IOException 
	{
		
		function_nm="FMS_SAC_MST()";
		
		try {
			
			
			System.out.println("<<START>><<FMS_SAC_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SAC_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			int max_cd=1;
			
			

//	    	queryString1 = "INSERT INTO FMS_SAC_MST VALUES(";
//	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_SAC_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1 = "INSERT INTO FMS_SAC_MST(SAC_CD,SAC_CODE,SAC_DESC,REMARKS,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,SAC_FLAG,ENT_PROFILE,MOD_PROFILE) VALUES(?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";//DIYA 20250226:SAC_CD ADDED
			
			//stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(SAC_CD) FROM FMS_SAC_MST";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1)+1;
			}
			rset.close();
			stmt.close();
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SAC_MST_"+start_end_dt+".xlsx");

			if(file1.exists()) {
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SAC_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "SAC_NAME,TIMESTAMP,", conn);
				
				String sac_name="";
				
				while (rowIterator.hasNext()) {
					total_count++;  
					//try {
						sac_name = "";
						data = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							
								if (cell.getColumnIndex() == 2) {
									sac_name = cell.getStringCellValue();
									sac_name = sac_name.substring(1, sac_name.length()-1);
//									System.out.println(prod_cd);
								}
//								
//								if (cell.getColumnIndex() == 1) {
//									sac_code = cell.getStringCellValue();
//									sac_code = sac_code.substring(1, sac_code.length()-1);
//								}
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if(cell.getColumnIndex() == 0) {
									data = "'"+(max_cd++)+"'";
//									System.out.println(data);
								}
							
								
								if(data != null) {
						    		data = data.substring(1, data.length()-1);
//						    		System.out.println(index+"=="+data.length());
						    	}
//							System.out.println(index+"=="+data);
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT SAC_CD FROM FMS_SAC_MST WHERE SAC_DESC=?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, sac_name);
//						stmt.setString(2, prod_abbr);
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							sac_name=sac_name.replace(","," ");//DIYA 20250226:REPLACE
							logger.data(fname, ( sac_name + " , " ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {	
							max_cd--;
							stmt1.close();
							skipped_count++; 
							sac_name=sac_name.replace(","," ");//DIYA 20250226:REPLACE
							logger.data(fname, ( sac_name + " , " ), conn, "E");
						}
						rset.close();
						stmt.close();
						
					/*} catch (Exception e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						logger.data_error(fname, e, abbr, conn, fname_error, function_nm);
					}*/
				   
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			
			System.out.println("<<END>><<FMS_SAC_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SAC_MST>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

			
		}
		
		catch(Exception e) {

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
	
	public void FMS_ENTITY_TCS_TDS_MST() throws SQLException, IOException 
	{
		
		function_nm="FMS_ENTITY_TCS_TDS_MST()";
		try {
			
			
			System.out.println("<<START>><<FMS_ENTITY_TCS_TDS_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_TCS_TDS_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			String inv_type = "";
			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
			String[] tax_dtls = new String[5];
			String[] tax_dtls1 = new String[5];		// SagarB20250823 new array for TDS 0 and TDS 10
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,ENTITY,INVOICE_TYPE,TAX_APP,TAX_CATEGORY,TAX_STRUCT_CD,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,EFF_DT,FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";
			
			queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = 'BIPSUP' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				ent_by = rset.getString(1);
			}
			
			rset.close();
			stmt.close();
			
			queryString1 = "INSERT INTO FMS_ENTITY_TCS_TDS_MST(COMPANY_CD,COUNTERPARTY_CD,ENTITY,INVOICE_TYPE,TAX_APP,TAX_CATEGORY,TAX_STRUCT_CD,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,EFF_DT,FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ENTITY,INVOICE_TYPE,TAX_CATEGORY,TAX_APP,EFF_DT,", conn);
			
			// For TCS.(Customer).
			// Below block of code is for inserting SEIPL data (Customer). For TCS.
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = 'T3' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			
			queryString = "SELECT  '2',A.COUNTERPARTY_CD, B.ENTITY, 'S', 'TCS', NULL, NULL, NULL, 'Migrated Data(SEIPL)', TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), 'Y', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE  A.EFF_DT = (SELECT MIN(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE A.COUNTERPARTY_CD=C.COUNTERPARTY_CD )  AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.ENTITY = 'C' AND B.COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				for (int i = 0; i < 5; i++) {
				stmt1 = conn.prepareStatement(queryString1);
				
					for (int j = 0; j < columns.split(",").length; j++) {
						data = "";
						data = rset.getString(j+1) == null ? "" : rset.getString(j+1);
						
						if (j == 0) {
							data = company_cd;
						}
						else if (j == 3) {
							if (i == 0) {
								data = "S";
							}
							else if (i == 1) {
								data = "CR";
							}
							else if (i == 2) {
								data = "DR";
							}
							else if (i == 3) {
								data = "CCR";
							}
							else if (i == 4) {
								data = "CDR";
							}
							
							inv_type = data;
						}
						else if (j == 5) {
							
							stmt1.setString(j+1, tax_dtls[0]);j++;
							stmt1.setString(j+1, tax_dtls[1]);j++;
//							stmt1.setString(j+1, tax_dtls[2]);j++;
//							stmt1.setString(j+1, tax_dtls[3]);j++;
							data = tax_dtls[3];
							
						}
						else if (j == 12) {
							data = ent_by;
						}
						stmt1.setString(j+1, data);
						
					}

					// query to check if the data already exists or not
					query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
					st = conn.prepareStatement(query);
					st.setString(1, company_cd);
					st.setString(2, rset.getString(2));
					st.setString(3, rset.getString(3));
					st.setString(4, inv_type);
					st.setString(5, tax_dtls[0]);
					st.setString(6, rset.getString(5));
					st.setString(7, rset.getString(10));
				
					rs = st.executeQuery();
					
					if (!rs.next() && tax_dtls[0] != null) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					
					st.close();
					rs.close();
				}
			
			}
			rset.close();
			stmt.close();
			
			// For TDS.(Customer).	SagarB20250819
			// Below block of code is for inserting SEIPL data (Customer). For service.
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = '7U' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			
			queryString = "SELECT  '2',A.COUNTERPARTY_CD, B.ENTITY, 'S', 'TDS', NULL, NULL, NULL, 'Migrated Data(SEIPL)', TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), 'Y', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE  A.EFF_DT = (SELECT MIN(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE A.COUNTERPARTY_CD=C.COUNTERPARTY_CD )  AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.ENTITY = 'C' AND B.COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				for (int i = 0; i < 4; i++) {
				stmt1 = conn.prepareStatement(queryString1);
				
					for (int j = 0; j < columns.split(",").length; j++) {
						data = "";
						data = rset.getString(j+1) == null ? "" : rset.getString(j+1);
						
						if (j == 0) {
							data = company_cd;
						}
						else if (j == 3) {
							if (i == 0) {
								data = "SI";
							}
							else if (i == 1) {
								data = "CR";
							}
							else if (i == 2) {
								data = "DR";
							}
							else if (i == 3) {
								data = "ST";
							}
							
							inv_type = data;
						}
						else if (j == 5) {
							
							stmt1.setString(j+1, tax_dtls[0]);j++;
							stmt1.setString(j+1, tax_dtls[1]);j++;
//										stmt1.setString(j+1, tax_dtls[2]);j++;
//										stmt1.setString(j+1, tax_dtls[3]);j++;
							data = tax_dtls[3];
							
						}
						else if (j == 12) {
							data = ent_by;
						}
						stmt1.setString(j+1, data);
						
					}

					// query to check if the data already exists or not
					query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
					st = conn.prepareStatement(query);
					st.setString(1, company_cd);
					st.setString(2, rset.getString(2));
					st.setString(3, rset.getString(3));
					st.setString(4, inv_type);
					st.setString(5, tax_dtls[0]);
					st.setString(6, rset.getString(5));
					st.setString(7, rset.getString(10));
				
					rs = st.executeQuery();
					
					if (!rs.next() && tax_dtls[0] != null) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					
					st.close();
					rs.close();
				}
			
			}
			rset.close();
			stmt.close();
			
			//For TCS.
			// Below block of code is for inserting SEIPL data (Trader). 
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = 'T4' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = 'T4' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();

			queryString = "SELECT  '2', A.COUNTERPARTY_CD, B.ENTITY, 'S', 'TCS', NULL, NULL, NULL, 'Migrated Data(SEIPL)', TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), 'Y', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE  A.COUNTERPARTY_ABBR = 'BP' AND  A.EFF_DT = (SELECT MIN(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE  A.COUNTERPARTY_CD=C.COUNTERPARTY_CD )  AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.ENTITY = 'T' AND B.COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				for (int i = 0; i < 3; i++) {
				stmt1 = conn.prepareStatement(queryString1);
				
					for (int j = 0; j < columns.split(",").length; j++) {
						data = "";
						data = rset.getString(j+1) == null ? "" : rset.getString(j+1);
						
						if (j == 0) {
							data = company_cd;
						}
						else if (j == 3) {
							
							if (i == 0) {
								data = "S";
							}
							else if (i == 1) {
								data = "CR";
							}
							else if (i == 2) {
								data = "DR";
							}
							inv_type = data;
						}
						else if (j == 5) {
							
							stmt1.setString(j+1, tax_dtls[0]);j++;
							stmt1.setString(j+1, tax_dtls[1]);j++;
//							stmt1.setString(j+1, tax_dtls[2]);j++;
//							stmt1.setString(j+1, tax_dtls[3]);j++;
							data = tax_dtls[3];
							
						}
						else if (j == 12) {
							data = ent_by;
						}
						stmt1.setString(j+1, data);
						
					}
					
					
					// query to check if the data already exists or not
					query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
					st = conn.prepareStatement(query);
					st.setString(1, company_cd);
					st.setString(2, rset.getString(2));
					st.setString(3, rset.getString(3));
					st.setString(4, inv_type);
					st.setString(5, tax_dtls[0]);
					st.setString(6,rset.getString(5));
					st.setString(7, rset.getString(10));
					rs = st.executeQuery();

					if (!rs.next() && tax_dtls[0] != null) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					
					st.close();
					rs.close();
				}
			
			}
			rset.close();
			stmt.close();
			
			
			
			//For TDS 
			// Below block of code is for inserting SEIPL data (Customer).
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = '8B' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			
			queryString = "SELECT  '2',A.COUNTERPARTY_CD, B.ENTITY, 'S', 'TDS', NULL, NULL, NULL, 'Migrated Data(SEIPL)', TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), 'Y', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.EFF_DT = (SELECT MIN(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD ) AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.ENTITY = 'C' AND B.COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				for (int i = 0; i < 5; i++) {
				stmt1 = conn.prepareStatement(queryString1);
				
					for (int j = 0; j < columns.split(",").length; j++) {
						data = "";
						data = rset.getString(j+1) == null ? "" : rset.getString(j+1);
						
						if (j == 0) {
							data = company_cd;
						}
						else if (j == 3) {
							if (i == 0) {
								data = "S";
							}
							else if (i == 1) {
								data = "CR";
							}
							else if (i == 2) {
								data = "DR";
							}
							else if (i == 3) {
								data = "CCR";
							}
							else if (i == 4) {
								data = "CDR";
							}
							inv_type = data;
						}
						else if (j == 5) {
							
							stmt1.setString(j+1, tax_dtls[0]);j++;
							stmt1.setString(j+1, tax_dtls[1]);j++;
//							stmt1.setString(j+1, tax_dtls[2]);j++;
//							stmt1.setString(j+1, tax_dtls[3]);j++;
							data = tax_dtls[3];
							
						}
						else if (j == 12) {
							data = ent_by;
						}
						
						stmt1.setString(j+1, data);
						
					}
					
					// query to check if the data already exists or not
					query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
					st = conn.prepareStatement(query);
					st.setString(1, company_cd);
					st.setString(2, rset.getString(2));
					st.setString(3, rset.getString(3));
					st.setString(4, inv_type);
					st.setString(5, tax_dtls[0]);
					st.setString(6, rset.getString(5));
					st.setString(7, rset.getString(10));
					rs = st.executeQuery();
					
					if (!rs.next() && tax_dtls[0] != null) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					
					st.close();
					rs.close();
				}
				
			}
			
			rset.close();
			stmt.close();
			
			//TDS 
			// Below block of code is for inserting SEIPL data (Trader).
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = '8A' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = '0T' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls1.length; i++) {
					tax_dtls1[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls1.length; i++) {
					tax_dtls1[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			
			queryString = "SELECT  '2',A.COUNTERPARTY_CD, B.ENTITY, 'S', 'TDS', NULL, NULL, NULL, 'Migrated Data(SEIPL)', TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), 'Y', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.EFF_DT = (SELECT MIN(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD ) AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.ENTITY = 'T' AND B.COMPANY_CD = ? AND COUNTERPARTY_ABBR NOT IN ('SPSSTEL1', 'SIETCO', 'STASCO', 'SITMEFZ', 'SILS', 'SELNG', 'SITME', 'TGPL') ";	// SagarB20250823 NOT IN criteria added
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				for (int i = 0; i < 3; i++) {
				stmt1 = conn.prepareStatement(queryString1);
				
					for (int j = 0; j < columns.split(",").length; j++) {
						data = "";
						data = rset.getString(j+1) == null ? "" : rset.getString(j+1);
						
						if (j == 0) {
							data = company_cd;
						}
						else if (j == 3) {
							
							if (i == 0) {
								data = "S";
							}
							else if (i == 1) {
								data = "CR";
							}
							else if (i == 2) {
								data = "DR";
							}
							
							inv_type = data;
						}
						else if (j == 5) {
							
							if (rset.getString(16).equals("BP")) {
								stmt1.setString(j+1, tax_dtls1[0]);j++;
								stmt1.setString(j+1, tax_dtls1[1]);j++;
//								stmt1.setString(j+1, tax_dtls1[2]);j++;
//								stmt1.setString(j+1, tax_dtls1[3]);j++;
								data = tax_dtls1[3];
							
								
							}
							else {
								stmt1.setString(j+1, tax_dtls[0]);j++;
								stmt1.setString(j+1, tax_dtls[1]);j++;
//								stmt1.setString(j+1, tax_dtls[2]);j++;
//								stmt1.setString(j+1, tax_dtls[3]);j++;
								data = tax_dtls[3];
							}
							
						}
						else if (j == 12) {
							data = ent_by;
						}
						
						stmt1.setString(j+1, data);
						
					}
					
					// query to check if the data already exists or not
					query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
					st = conn.prepareStatement(query);
					st.setString(1, company_cd);
					st.setString(2, rset.getString(2));
					st.setString(3, rset.getString(3));
					st.setString(4, inv_type);
					st.setString(5, tax_dtls[0]);
					st.setString(6, rset.getString(5));
					st.setString(7, rset.getString(10));
					rs = st.executeQuery();
					
					if (!rs.next() && tax_dtls[0] != null) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					
					st.close();
					rs.close();
				}
				
			}
			
			rset.close();
			stmt.close();
			
			//TDS
			// Below block of code is for inserting SEIPL data (Transporter).
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = '7C' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			
			queryString = "SELECT  '2',A.COUNTERPARTY_CD, B.ENTITY, 'TC', 'TDS', NULL, NULL, NULL, 'Migrated Data(SEIPL)', TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), 'Y', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.EFF_DT = (SELECT MIN(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD ) AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.ENTITY = 'R' AND B.COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				for (int i = 0; i < 4; i++) {
				stmt1 = conn.prepareStatement(queryString1);
				
					for (int j = 0; j < columns.split(",").length; j++) {
						data = "";
						data = rset.getString(j+1) == null ? "" : rset.getString(j+1);
						
						if (j == 0) {
							data = company_cd;
						}
						else if (j == 3) {
							
							if (i == 0) {
								data = "TC";
							}
							else if (i == 1) {
								data = "IC";
							}
							else if (i == 2) {
								data = "DR";
							}
							else if (i == 3) {
								data = "PC";
							}
							
							inv_type = data;
						}
						else if (j == 5) {
							
							stmt1.setString(j+1, tax_dtls[0]);j++;
							stmt1.setString(j+1, tax_dtls[1]);j++;
//							stmt1.setString(j+1, tax_dtls[2]);j++;
//							stmt1.setString(j+1, tax_dtls[3]);j++;
							data = tax_dtls[3];
							
						}
						else if (j == 12) {
							data = ent_by;
						}
						
						stmt1.setString(j+1, data);
						
					}
					
					// query to check if the data already exists or not
					query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
					st = conn.prepareStatement(query);
					st.setString(1, company_cd);
					st.setString(2, rset.getString(2));
					st.setString(3, rset.getString(3));
					st.setString(4, inv_type);
					st.setString(5, tax_dtls[0]);
					st.setString(6, rset.getString(5));
					st.setString(7, rset.getString(10));
					rs = st.executeQuery();
					
					if (!rs.next() && tax_dtls[0] != null) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					
					st.close();
					rs.close();
				}
				
			}
			
			rset.close();
			stmt.close();
			
			//TDS
			// Below block of code is for inserting SEIPL data (VESSEL).
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = '7C' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			
			queryString = "SELECT  '2',A.COUNTERPARTY_CD, B.ENTITY, 'VA', 'TDS', NULL, NULL, NULL, 'Migrated Data(SEIPL)', TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), 'Y', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.EFF_DT = (SELECT MIN(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD ) AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.ENTITY = 'V' AND B.COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				stmt1 = conn.prepareStatement(queryString1);
				
					for (int j = 0; j < columns.split(",").length; j++) {
						data = "";
						data = rset.getString(j+1) == null ? "" : rset.getString(j+1);
						
						if (j == 0) {
							data = company_cd;
						}
						else if (j == 3) {
								
							data = "VA";
							inv_type = data;
						}
						else if (j == 5) {
							
							stmt1.setString(j+1, tax_dtls[0]);j++;
							stmt1.setString(j+1, tax_dtls[1]);j++;
//							stmt1.setString(j+1, tax_dtls[2]);j++;
//							stmt1.setString(j+1, tax_dtls[3]);j++;
							data = tax_dtls[3];
							
						}
						else if (j == 12) {
							data = ent_by;
						}
						
						stmt1.setString(j+1, data);
						
					}
					
					// query to check if the data already exists or not
					query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
					st = conn.prepareStatement(query);
					st.setString(1, company_cd);
					st.setString(2, rset.getString(2));
					st.setString(3, rset.getString(3));
					st.setString(4, inv_type);
					st.setString(5, tax_dtls[0]);
					st.setString(6, rset.getString(5));
					st.setString(7, rset.getString(10));
					rs = st.executeQuery();
					
					if (!rs.next() && tax_dtls[0] != null) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					
					st.close();
					rs.close();
				
				
			}
			
			rset.close();
			stmt.close();
							
			
			//TDS
			// Below block of code is for inserting SEIPL data (CHA).
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = '7C' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			
			queryString = "SELECT  '2',A.COUNTERPARTY_CD, B.ENTITY, 'CH', 'TDS', NULL, NULL, NULL, 'Migrated Data(SEIPL)', TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), 'Y', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.EFF_DT = (SELECT MIN(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD ) AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.ENTITY = 'H' AND B.COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				stmt1 = conn.prepareStatement(queryString1);
				
					for (int j = 0; j < columns.split(",").length; j++) {
						data = "";
						data = rset.getString(j+1) == null ? "" : rset.getString(j+1);
						
						if (j == 0) {
							data = company_cd;
						}
						else if (j == 3) {
								
							data = "CH";
							inv_type = data;
						}
						else if (j == 5) {
							
							stmt1.setString(j+1, tax_dtls[0]);j++;
							stmt1.setString(j+1, tax_dtls[1]);j++;
//							stmt1.setString(j+1, tax_dtls[2]);j++;
//							stmt1.setString(j+1, tax_dtls[3]);j++;
							data = tax_dtls[3];
							
						}
						else if (j == 12) {
							data = ent_by;
						}
						
						stmt1.setString(j+1, data);
						
					}
					
					// query to check if the data already exists or not
					query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
					st = conn.prepareStatement(query);
					st.setString(1, company_cd);
					st.setString(2, rset.getString(2));
					st.setString(3, rset.getString(3));
					st.setString(4, inv_type);
					st.setString(5, tax_dtls[0]);
					st.setString(6, rset.getString(5));
					st.setString(7, rset.getString(10));
					rs = st.executeQuery();
					
					if (!rs.next() && tax_dtls[0] != null) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					
					st.close();
					rs.close();
				
				
			}
			
			rset.close();
			stmt.close();
			
			
			//TDS
			// Below block of code is for inserting SEIPL data (SURVEYOR).
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = '7C' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			
			queryString = "SELECT  '2',A.COUNTERPARTY_CD, B.ENTITY, 'SF', 'TDS', NULL, NULL, NULL, 'Migrated Data(SEIPL)', TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), 'Y', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.EFF_DT = (SELECT MIN(C.EFF_DT) FROM FMS_COUNTERPARTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD ) AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.ENTITY = 'S' AND B.COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				stmt1 = conn.prepareStatement(queryString1);
				
					for (int j = 0; j < columns.split(",").length; j++) {
						data = "";
						data = rset.getString(j+1) == null ? "" : rset.getString(j+1);
						
						if (j == 0) {
							data = company_cd;
						}
						else if (j == 3) {
								
							data = "SF";
							inv_type = data;
						}
						else if (j == 5) {
							
							stmt1.setString(j+1, tax_dtls[0]);j++;
							stmt1.setString(j+1, tax_dtls[1]);j++;
//							stmt1.setString(j+1, tax_dtls[2]);j++;
//							stmt1.setString(j+1, tax_dtls[3]);j++;
							data = tax_dtls[3];
							
						}
						else if (j == 12) {
							data = ent_by;
						}
						
						stmt1.setString(j+1, data);
						
					}
					
					// query to check if the data already exists or not
					query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
					st = conn.prepareStatement(query);
					st.setString(1, company_cd);
					st.setString(2, rset.getString(2));
					st.setString(3, rset.getString(3));
					st.setString(4, inv_type);
					st.setString(5, tax_dtls[0]);
					st.setString(6, rset.getString(5));
					st.setString(7, rset.getString(10));
					rs = st.executeQuery();
					
					if (!rs.next() && tax_dtls[0] != null) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					
					st.close();
					rs.close();
				
				
			}
			
			rset.close();
			stmt.close();
							
			

			
			
			//TDS
			// Below block of code is for inserting SEIPL data (IGX).
			queryString = "SELECT TAX_CATEGORY, TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), DESCR, SAP_TAX_CODE FROM FMS_TAX_STRUCTURE WHERE SAP_TAX_CODE = '7J' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = rset.getString(i+1);
				}
			}
			else {
				for (int i = 0; i < tax_dtls.length; i++) {
					tax_dtls[i] = null;
				}
			}
			rset.close();
			stmt.close();
			
			
			queryString = "SELECT  '2',A.COUNTERPARTY_CD, 'G', NULL, 'TDS', NULL, NULL, NULL, 'Migrated Data(SEIPL)', TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), 'Y', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS_COMPANY_EXCHG_MST A WHERE A.EFF_DT = (SELECT MIN(C.EFF_DT) FROM FMS_COMPANY_EXCHG_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD ) ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				stmt1 = conn.prepareStatement(queryString1);
				
						for (int i = 0; i < 2; i++) {
						stmt1 = conn.prepareStatement(queryString1);
						
							for (int j = 0; j < columns.split(",").length; j++) {
								data = "";
								data = rset.getString(j+1) == null ? "" : rset.getString(j+1);
								
								if (j == 0) {
									data = company_cd;
								}
								else if (j == 3) {
									if (i == 0) {
										data = "TX";
									}
									else if (i == 1) {
										data = "DR";
									}
									
									inv_type = data;
								}
								else if (j == 5) {
									
									stmt1.setString(j+1, tax_dtls[0]);j++;
									stmt1.setString(j+1, tax_dtls[1]);j++;
//									stmt1.setString(j+1, tax_dtls[2]);j++;
//									stmt1.setString(j+1, tax_dtls[3]);j++;
									data = tax_dtls[3];
									
								}
								else if (j == 12) {
									data = ent_by;
								}
								stmt1.setString(j+1, data);
								
							}

							// query to check if the data already exists or not
							query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
							st = conn.prepareStatement(query);
							st.setString(1, company_cd);
							st.setString(2, rset.getString(2));
							st.setString(3, rset.getString(3));
							st.setString(4, inv_type);
							st.setString(5, tax_dtls[0]);
							st.setString(6, rset.getString(5));
							st.setString(7, rset.getString(10));
						
							rs = st.executeQuery();
							
							if (!rs.next() && tax_dtls[0] != null) {
								
								logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
								
								stmt1.executeUpdate();
								stmt1.close();

								logger_count++;
							}
							else {
								
								stmt1.close();
								skipped_count++; 
								
								logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
								
							}
							
							st.close();
							rs.close();
						}
					
					// query to check if the data already exists or not
					query = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = ? AND INVOICE_TYPE = ? AND TAX_CATEGORY = ? AND TAX_APP = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
					st = conn.prepareStatement(query);
					st.setString(1, company_cd);
					st.setString(2, rset.getString(2));
					st.setString(3, rset.getString(3));
					st.setString(4, inv_type);
					st.setString(5, tax_dtls[0]);
					st.setString(6, rset.getString(5));
					st.setString(7, rset.getString(10));
					rs = st.executeQuery();
					
					if (!rs.next() && tax_dtls[0] != null) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					
					st.close();
					rs.close();
				
				
			}
			
			rset.close();
			stmt.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
		
			System.out.println("<<END>><<FMS_ENTITY_TCS_TDS_MST>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_TCS_TDS_MST>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

			
		}
		
		catch(Exception e) {

			msg = "One of the Functions faced an Error. Data Not Inserted.";
			msg_type = "E";
			
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
		finally {
			conn.commit();
		}		
	}
	
	
//	public void getCustomerTraderList() {
//		function_nm = "getCustomerTraderList()";
//		
//		try
//		{
//			String strline = "";
//			
//			File fsetup=new File(migration_setup_dir+"Migration_Plants_Exceptions.txt");
//			String file_path=fsetup.getAbsolutePath();
//			FileInputStream f1 = new FileInputStream(file_path);
//			DataInputStream in = new DataInputStream(f1);
//			try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
//			
//				while((strline = br.readLine())!=null)
//				{
//					if(strline.startsWith("TRANSPORTER-MAPPING"))
//					{
//						String  tmp[]=strline.split("TRANSPORTER-MAPPING: ");
//						transporter_map = tmp[1].toString();
//					}
//					if(strline.startsWith("METER-MAPPING"))
//					{
//						String  tmp[]=strline.split("METER-MAPPING: ");
//						meter_map = tmp[1].toString();
//					}
//				}
//			}
//			
////			System.out.println(customer_delete);
////			System.out.println(trader_delete);
////			System.out.println(customer_map);
////			System.out.println(trader_map);
//		}
//		catch (Exception e) 
//		{
//			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> getCustomerTraderList() ", e);
//			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e); 
//		}
//	}
	
	
	public String Camel_Case_Converter(String value) {

		value = value.substring(1, value.length()-1);
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
				converted_string = converted_string.substring(0, converted_string.length()-1);
			}
			else {
				converted_string = value.substring(0, 1).toUpperCase();
				converted_string = converted_string + value.substring(1).toLowerCase();
			}
		}
		else {
			converted_string = null;
		}
		
		return converted_string;
		
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

