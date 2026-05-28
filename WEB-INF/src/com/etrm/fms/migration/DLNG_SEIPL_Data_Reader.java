package com.etrm.fms.migration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.sales_invoice.DataBean_Sales_Invoice;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;


public class DLNG_SEIPL_Data_Reader {

	

	String db_src_file_name="DLNG_SEIPL_Data_Reader.java";

	String migration_setup_dir = "";
	String pdf_path = "";
	
	String queryString="", queryString1="",queryString2="",queryString3="",queryString5="";
	Connection conn;
	ResultSet rset,rset1,rset2,rset3,rset5;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt5;
	
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
	String cd = "", eff_dt = "",no="",rev="",cont_no="",cont_rev="",type="";

	String checked_values = "", msg = "", msg_type = "",abbr="",table_name="";
	String transporter_map = "", meter_map = "",cont_ref="",cont_type="",a_typ="",cargo_no="",seq_no="",mod_seq="";

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
			
			fname = "DataLogs/Reader/DLNG_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/DLNG_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
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
					if (checked_values.contains("FMS_BAY_SLOT_MST,")) {
						FMS_BAY_SLOT_MST();
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
						FMS_AGMT_BU();
						FMS_AGMT_PLANT();
						FMS_AGMT_TRUCK_TRANS();
                        FMS_AGMT_FILLING_STN();
					}
					if (checked_values.contains("FMS_AGMT_FILLING_STN,")) {
//						FMS_AGMT_FILLING_STN();
					}
					if (checked_values.contains("FMS_AGMT_PLANT(DLNG),")) {
//						FMS_AGMT_PLANT();
					}
					if (checked_values.contains("FMS_AGMT_BILLING_DTL(DLNG),")) {
						FMS_AGMT_BILLING_DTL();
					}
					if (checked_values.contains("FMS_AGMT_TRUCK_TRANS,")) {
//						FMS_AGMT_TRUCK_TRANS();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_MST(DLNG),")) {
						FMS_SUPPLY_CONT_MST();
						FMS_SUPPLY_CONT_BU();
						
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_FILLING_STN,")) {
						FMS_SUPPLY_CONT_FILLING_STN();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_PLANT(DLNG),")) {
						FMS_SUPPLY_CONT_PLANT();
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
						FMS_AGMT_SVC_PLANT();
						FMS_AGMT_SVC_BU();
					}
					if(checked_values.contains("FMS_SVC_CONT_MST,")) {
						FMS_SVC_CONT_MST();
						FMS_SVC_CONT_BU();
					}
					if(checked_values.contains("FMS_SVC_CONT_MST_UPDATE,")) {
						FMS_SVC_CONT_MST_UPDATE();
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
					if (checked_values.contains("FMS_DLNG_SELLER_NOM_DTL(D),")) {
						FMS_DLNG_SELLER_NOM_DTL();
					}
					if (checked_values.contains("FMS_DLNG_ALLOC_MST(D),")) {
						FMS_DLNG_ALLOC_MST();
					}
					if (checked_values.contains("FMS_DLNG_INVOICE_MST,")) {
						FMS_DLNG_INVOICE_MST();
						FMS_DLNG_INV_TAX_DTL();
					}
					if (checked_values.contains("FMS_DLNG_INV_FILE_DTL,")) {
						FMS_DLNG_INV_FILE_DTL();
					}
					if (checked_values.contains("FMS_DLNG_INV_FILE_DTL_UPDATE,")) {
						FMS_DLNG_INV_FILE_DTL_UPDATE();
					}
					if (checked_values.contains("FMS_DLNG_INV_PAY_RECV_DTL,")) {
						FMS_DLNG_INV_PAY_RECV_DTL();
					}
					if (checked_values.contains("FMS_DLNG_FFLOW_INV_MST,")) {
						FMS_DLNG_FFLOW_INV_MST();
						FMS_DLNG_FFLOW_INV_TAX_DTL();
					}
					if (checked_values.contains("FMS_DLNG_FFLOW_INV_DTL,")) {
						FMS_DLNG_FFLOW_INV_DTL();
					}
					if (checked_values.contains("FMS_DLNG_FFLOW_INV_FILE_DTL,")) {
						FMS_DLNG_FFLOW_INV_FILE_DTL();
					}
					if (checked_values.contains("FMS_DLNG_FFLOW_INV_FILE_DTL_UPDATE,")) {
						FMS_DLNG_FFLOW_INV_FILE_DTL_UPDATE();
					}
					if (checked_values.contains("FMS_SVC_CONT_BILLING_DTL,")) {
						FMS_SVC_CONT_BILLING_DTL();
					}
					if (checked_values.contains("FMS_DLNG_SVC_INVOICE_MST,")) {
						FMS_DLNG_SVC_INVOICE_MST();
						FMS_DLNG_SVC_INV_TAX_DTL();
					}
					if (checked_values.contains("FMS_DLNG_SVC_INVOICE_DTL,")) {
						FMS_DLNG_SVC_INVOICE_DTL();
					}
					if (checked_values.contains("FMS_DLNG_SVC_INV_FILE_DTL,")) {
						FMS_DLNG_SVC_INV_FILE_DTL();
					}
					if (checked_values.contains("FMS_DLNG_INVOICE_MST_CR_DR,")) { 
	                	FMS_DLNG_INVOICE_MST_CR_DR();
	                	FMS_DLNG_INV_TAX_DTL_CR_DR();
					}
					if (checked_values.contains("FMS_DLNG_INV_CRDR_REF,")) { 
						FMS_DLNG_INV_CRDR_REF();
						FMS_DLNG_INV_CRDR_TAX_DTL();
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
						FMS_DLNG_FFLOW_INV_DTL_SERV();
						FMS_DLNG_FFLOW_INV_TAX_DTL_SERV();
					}
					if (checked_values.contains("FMS_DLNG_FFLOW_INV_FILE_DTL_SERV,")) {
						FMS_DLNG_FFLOW_INV_FILE_DTL_SERV();
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
				if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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


	public void FMS_TRUCK_TRANSPORTER_MST() throws IOException, SQLException {

		function_nm="FMS_TRUCK_TRANSPORTER_MST()";
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_TRANSPORTER_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRUCK_TRANSPORTER_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_TRUCK_TRANSPORTER_MST(TRUCK_TRANS_CD,EFF_DT,TRUCK_TRANS_NAME,TRUCK_TRANS_ABBR,ADDR,STATE,CITY,PIN,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE) VALUES(?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANSPORTER_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANSPORTER_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "ENT_PROFILE,TRUCK_TRANS_CD,EFF_DT,TIMESTAMP,", conn);

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

							if (cell.getColumnIndex() == 0) {
								cd = cell.getStringCellValue();
								cd = cd.substring(1, cd.length()-1);
							}
							if (cell.getColumnIndex() == 1) {
								eff_dt = cell.getStringCellValue();
								eff_dt = eff_dt.substring(1, eff_dt.length()-1);
							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE ENT_PROFILE = ? AND TRUCK_TRANS_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ";
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
			
			System.out.println("<<END>><<FMS_TRUCK_TRANSPORTER_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_TRANSPORTER_MST>>,,", conn);
			
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


	public void FMS_TRUCK_TRANS_CONTACT_MST() throws IOException, SQLException {

		function_nm="FMS_TRUCK_TRANS_CONTACT_MST()";
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_TRANS_CONTACT_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRUCK_TRANS_CONTACT_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String flag,seq_no;
			
			queryString1 = "INSERT INTO FMS_TRUCK_TRANS_CONTACT_MST(COMPANY_CD,TRUCK_TRANS_CD,SEQ_NO,EFF_DT,CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,NOM_FLAG,JT_FLAG,"
					+ "INV_FLAG,RM_FLAG,FM_FLAG,PM_FLAG,OTHER_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ADDR_IS_ACTIVE) VALUES(?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANS_CONTACT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANS_CONTACT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,TRUCK_TRANS_CD,SEQ_NO,EFF_DT,ADDR_FLAG,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = ""; eff_dt = "";flag="";seq_no="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							if (cell.getColumnIndex() == 1) {
								cd = cell.getStringCellValue();
								cd = cd.substring(1, cd.length()-1);
							}
							else if (cell.getColumnIndex() == 2) {
								seq_no = cell.getStringCellValue();
								seq_no = seq_no.substring(1, seq_no.length()-1);
							}
							else if (cell.getColumnIndex() == 3) {
								eff_dt = cell.getStringCellValue();
								eff_dt = eff_dt.substring(1, eff_dt.length()-1);
							}
							else if (cell.getColumnIndex() == 11) {
								flag = cell.getStringCellValue();
								flag = flag.substring(1, flag.length()-1);
							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANS_CONTACT_MST WHERE COMPANY_CD = ? AND TRUCK_TRANS_CD = ? AND SEQ_NO = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND ADDR_FLAG = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, seq_no);
						stmt.setString(4, eff_dt);
						stmt.setString(5, flag);
						
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + cd + "," + seq_no + "," + eff_dt + "," + flag + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + seq_no + "," + eff_dt + "," + flag + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_TRUCK_TRANS_CONTACT_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_TRANS_CONTACT_MST>>,,,,", conn);
			
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


	public void FMS_TRUCK_MST() throws IOException, SQLException {

		function_nm="FMS_TRUCK_MST()";
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRUCK_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
			String max_cd = "";
			String num ,flag;

			queryString1 = "INSERT INTO FMS_TRUCK_MST(TRUCK_CD,EFF_DT,TRUCK_REG_NUM,TRUCK_TYPE,TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP,ACTIVE_FLAG,"
					+ "ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE) VALUES(?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
//			queryString = "SELECT MAX(TRUCK_CD) FROM FMS_TRUCK_MST ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				max_cd = rset.getInt(1)+1;
//			}
//			rset.close();
//			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "ENT_PROFILE,TRUCK_CD,TRUCK_REG_NUM,EFF_DT,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = ""; eff_dt = "";num="";flag="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							
							if (cell.getColumnIndex() == 0) {
								max_cd = cell.getStringCellValue();
								max_cd = max_cd.substring(1, max_cd.length()-1);
								}
							else if (cell.getColumnIndex() == 1) {
								eff_dt = cell.getStringCellValue();
								eff_dt = eff_dt.substring(1, eff_dt.length()-1);
							}
							else if (cell.getColumnIndex() == 2) {
								num = cell.getStringCellValue();
								num = num.substring(1, num.length()-1);
							}
							else if (cell.getColumnIndex() == 7) {
								flag = cell.getStringCellValue();
								flag = flag.substring(1, flag.length()-1);
							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							
//							if (cell.getColumnIndex() == 0) {
//								data = "'"+(max_cd++)+"'";
//								}
							
							
							if(data != null && data.contains("null")) {
								data = null;
							}
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT TRUCK_CD FROM FMS_TRUCK_MST WHERE ENT_PROFILE = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND TRUCK_REG_NUM = ? AND ACTIVE_FLAG = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, eff_dt);
						stmt.setString(3, num);
						stmt.setString(4, flag);
						
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + max_cd + "," + num + "," + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
//							max_cd--;
							skipped_count++;     
							logger.data(fname, (company_cd + "," + max_cd + "," + num + "," + eff_dt + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_TRUCK_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_MST>>,,,", conn);
			
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
	
	public void FMS_TRUCK_DRIVER_MST() throws IOException, SQLException {
		
		function_nm="FMS_TRUCK_DRIVER_MST()";
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_DRIVER_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRUCK_DRIVER_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String no = "",addr = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
			int max_cd = 1;
			
			queryString1 = "INSERT INTO FMS_TRUCK_DRIVER_MST(DRIVER_CD,DRIVER_NAME,DRIVER_ADDR,DRIVER_DOB,DRIVER_STATUS,DRIVER_MOBILE,LICENCE_NO,LICENCE_TYPE,LICENCE_FROM_DT,"
					+ "LICENCE_TO_DT,LICENCE_ISSUE_STATE,LICENCE_FILE_NAME,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE,EFF_DT) VALUES("
					+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(DRIVER_CD) FROM FMS_TRUCK_DRIVER_MST ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1)+1;
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_MST_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "ENT_PROFILE,LICENCE_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					data = "";
					cd = ""; eff_dt = "";
					addr = "";
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						
//						if(cell.getColumnIndex() == 6) {
//							no = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
//							no = no.substring(1, no.length()-1);
//						}
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						
						if (cell.getColumnIndex() == 0) {
							data = "'"+(max_cd++)+"'";
						}
						else if(cell.getColumnIndex() == 2 && addr.length() > 100) {
							addr = addr.substring(100);
							data = addr;
						}
//						if(cell.getColumnIndex() == 6 && no.length() > 15) {
//							no = no.substring(15);
//							data = no;
//						}
						else if(cell.getColumnIndex() == 6) {
							no = data;
							if (no!=null) {
								no = no.substring(1, no.length() - 1);
							}
						}
						
						
						if(data != null && data.contains("null")) {
							data = null;
						}
						if(data != null) {
							data = data.substring(1, data.length()-1);
						}
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT LICENCE_NO FROM FMS_TRUCK_DRIVER_MST WHERE ENT_PROFILE = ? AND LICENCE_NO = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, no);
					
					rset = stmt.executeQuery();
					
					if (!rset.next()) {
						logger.data(fname, (company_cd + "," + no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						max_cd--;
						skipped_count++;     
						logger.data(fname, (company_cd + "," + no + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_TRUCK_DRIVER_MST>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_DRIVER_MST>>,", conn);
			
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
	
	public void FMS_FILLING_STATION_MST() throws IOException, SQLException {
		
		function_nm="FMS_FILLING_STATION_MST()";
		try {
			
			System.out.println("<<START>><<FMS_FILLING_STATION_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_FILLING_STATION_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String no = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
			int max_cd = 1;
			String entity = "";
			
			queryString1 = "INSERT INTO FMS_FILLING_STATION_MST(COUNTERPARTY_CD,FILL_STATION_CD,EFF_DT,FILL_STATION_NAME,FILL_STATION_ABBR,ACTIVE_FLAG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
					+ "VALUES(?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(FILL_STATION_CD) FROM FMS_FILLING_STATION_MST ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1)+1;
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_FILLING_STATION_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_FILLING_STATION_MST_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "COUNTERPARTY_CD,FILL_STATION_CD,EFF_DT,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					data = "";
//					cd = ""; 
					eff_dt = "";
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						
//						if(cell.getColumnIndex() == 1) {
//							cd = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
//							if (cd!=null) {
//								cd = cd.substring(1, cd.length() - 1);
//							}
//						}
//						if(cell.getColumnIndex() == 2) {
//							entity = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
//							if (entity!=null) {
//								entity = entity.substring(1, entity.length() - 1);
//							}
//						}
						if(cell.getColumnIndex() == 2) {
							eff_dt = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (eff_dt!=null) {
								eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
							}
						}
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						
						
						if (cell.getColumnIndex() == 0) {
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SEIPL' ";
							stmt = conn.prepareStatement(queryString);
							rset = stmt.executeQuery();
							
							if (rset.next()) {
								cd = rset.getString(1);
							}
							rset.close();
							stmt.close();
							
							data = "'"+cd+"'";
						}
						else if (cell.getColumnIndex() == 1) {
							data = "'"+(max_cd++)+"'";
						}
						
						
						if(data != null && data.contains("null")) {
							data = null;
						}
						if(data != null) {
							data = data.substring(1, data.length()-1);
						}
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_FILLING_STATION_MST WHERE COUNTERPARTY_CD = ?  AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')";
					stmt = conn.prepareStatement(queryString);
//					stmt.setString(1, company_cd);
					stmt.setString(1, cd);
//					stmt.setString(3, entity);
					stmt.setString(2, eff_dt);
					
					rset = stmt.executeQuery();
					
					if (!rset.next()) {
						//System.out.println(queryString1);
						
						logger.data(fname, (cd + ","+max_cd+"," + eff_dt + ","), conn, "");
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						max_cd--;
						skipped_count++;     
						logger.data(fname, (cd + ","+max_cd+"," + eff_dt + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_FILLING_STATION_MST>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_FILLING_STATION_MST>>,,,,", conn);
			
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

	public void FMS_BAY_SLOT_MST() throws IOException, SQLException {

		function_nm="FMS_BAY_SLOT_MST()";
		try {
			
			
			System.out.println("<<START>><<FMS_BAY_SLOT_MST>>");
			
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BAY_SLOT_MST>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "FILL_STATION_CD,BAY_CD,EFF_DT,BAY_NAME,ACTIVE_FLAG,SLOT_CALD_TYPE,SLOT_START_TIME,SLOT_INTERVAL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			
			queryString = "SELECT MAX(BAY_CD+1) FROM FMS_BAY_SLOT_MST ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next() && rset.getString(1)!=null) {
				cd = rset.getString(1);
			}
			else {
				cd = "1";
			}
			rset.close();
			stmt.close();
			
			queryString1 = "INSERT INTO FMS_BAY_SLOT_MST (FILL_STATION_CD,BAY_CD,EFF_DT,BAY_NAME,ACTIVE_FLAG,SLOT_CALD_TYPE,SLOT_START_TIME,SLOT_INTERVAL,ENT_BY,ENT_DT,MODIFY_BY,"
					+ "MODIFY_DT) values (1,?,TO_DATE('01/01/2020 00:00:00','DD/MM/YYYY HH24:MI:SS'),'BAY 1','Y','G','06:00','03:00',1,"
					+ "TO_DATE('01/01/2020 00:00:00','DD/MM/YYYY HH24:MI:SS'),null,null)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, cd);
				
			//for data already exists..
				queryString5 = "SELECT BAY_CD FROM FMS_BAY_SLOT_MST WHERE FILL_STATION_CD = '1' AND BAY_NAME = 'BAY 1'  "
						+ "AND EFF_DT = TO_DATE('01/01/2020 00:00:00','DD/MM/YYYY HH24:MI:SS')";
				stmt5 = conn.prepareStatement(queryString5);
			
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next() ) {
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++; 
				}
				stmt5.close();
				rset5.close();
				
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_BAY_SLOT_MST>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_BAY_SLOT_MST>>,", conn);
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
		}
	}


	public void FMS_CHECKPOST_MST() throws IOException, SQLException {
		
		function_nm="FMS_CHECKPOST_MST()";
		try {
			
			System.out.println("<<START>><<FMS_CHECKPOST_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_CHECKPOST_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String no = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
			int max_cd = 1;
			
			queryString1 = "INSERT INTO FMS_CHECKPOST_MST(CHKPOST_CD,CHKPOST_NAME,EFF_DT,STATE_CODE,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE) "
					+ "VALUES(?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			queryString = "SELECT MAX(CHKPOST_CD) FROM FMS_CHECKPOST_MST ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				max_cd = rset.getInt(1)+1;
			}
			rset.close();
			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CHECKPOST_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CHECKPOST_MST_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "ENT_PROFILE,CHKPOST_CD,EFF_DT,TIMESTAMP,", conn);
				
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
						
						if(cell.getColumnIndex() == 1) {
							no = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (no!=null) {
								no = no.substring(1, no.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 2) {
							eff_dt = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (eff_dt!=null) {
								eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
							}
						}
						
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						
						if (cell.getColumnIndex() == 0) {
							data = "'"+(max_cd++)+"'";
						}
						
						
						if(data != null && data.contains("null")) {
							data = null;
						}
						if(data != null) {
							data = data.substring(1, data.length()-1);
						}
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT CHKPOST_CD FROM FMS_CHECKPOST_MST WHERE ENT_PROFILE = ? AND CHKPOST_NAME = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, no);
					stmt.setString(3, eff_dt);
					
					rset = stmt.executeQuery();
					
					if (!rset.next()) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd + ","+max_cd+"," + eff_dt + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						max_cd--;
						skipped_count++;     
						logger.data(fname, (company_cd + ","+max_cd+"," + eff_dt + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_CHECKPOST_MST>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_CHECKPOST_MST>>,,", conn);
			
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
	
	public void FMS_TRUCK_TRANSPORTER_LINK() throws IOException, SQLException {
		
		function_nm="FMS_TRUCK_TRANSPORTER_LINK()";
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_TRANSPORTER_LINK>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRUCK_TRANSPORTER_LINK>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String no = "",rel_dt="";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
//			int max_cd = 1;
			
			queryString1 = "INSERT INTO FMS_TRUCK_TRANSPORTER_LINK(TRUCK_TRANS_CD,TRUCK_CD,LINK_SEQ,EFF_DT,RELEASE_DT,REMARKS,R_REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE) "
					+ "VALUES(?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
//			queryString = "SELECT MAX(TRUCK_TRANS_CD) FROM FMS_TRUCK_TRANSPORTER_LINK ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				max_cd = rset.getInt(1)+1;
//			}
//			rset.close();
//			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANSPORTER_LINK_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_TRANSPORTER_LINK_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "ENT_PROFILE,TRUCK_TRANS_CD,TRUCK_CD,EFF_DT,LINK_SEQ,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					data = "";
					cd = ""; eff_dt = "";
					no = "";
					String seq="";
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						
						if(cell.getColumnIndex() == 1) {
							no = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (no!=null) {
								no = no.substring(1, no.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 2) {
							seq = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (seq!=null) {
								seq = seq.substring(1, seq.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 3) {
							eff_dt = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (eff_dt!=null) {
								eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 4) {
							rel_dt = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if(rel_dt!=null) {
							rel_dt = rel_dt.substring(1, rel_dt.length()-1);
							}
						}
						
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						if (data != null) {
							data = data.substring(1, data.length() - 1);
						}
						if (cell.getColumnIndex() == 0) {
//							data = data.substring(1, data.length()-1);
							
							queryString = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE TRUCK_TRANS_ABBR = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = cd;
						}
						else if(cell.getColumnIndex() == 1) {
//							data = data.substring(1, data.length()-1);
							
//							queryString = "SELECT TRUCK_CD FROM FMS_TRUCK_MST WHERE TRUCK_REG_NUM = ? ";
//							stmt = conn.prepareStatement(queryString);
//							stmt.setString(1, data);
//							rset = stmt.executeQuery();
//							if(rset.next()) {
//								no = rset.getString(1);
//							}
//							
//							rset.close();
//							stmt.close();
							data = no;
						}
						
						else if(cell.getColumnIndex() == 2) {
//							queryString = "SELECT LINK_SEQ FROM FMS_TRUCK_TRANSPORTER_LINK WHERE TRUCK_TRANS_CD = ? AND TRUCK_CD = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
//							stmt = conn.prepareStatement(queryString);
//							stmt.setString(1, cd);
//							stmt.setString(2, no);
//							stmt.setString(3, eff_dt);
//							rset = stmt.executeQuery();
//							if(rset.next() && rset.getInt(1) > 0) {
//								seq = rset.getInt(1);
//							}
//							else {
//								queryString2 = "SELECT MAX(LINK_SEQ) FROM FMS_TRUCK_TRANSPORTER_LINK WHERE TRUCK_CD = ?  ";
//								stmt2 = conn.prepareStatement(queryString2);
//								stmt2.setString(1, no);
//								rset2 = stmt2.executeQuery();
//								if(rset2.next() && rset2.getInt(1) > 0) {
//									seq = rset2.getInt(1);
//								}
//								else {
//									seq = 1;
//								}
//								rset2.close();
//								stmt2.close();
//							}
//							rset.close();
//							stmt.close();
							
							data = seq;
						}
						
						else if(cell.getColumnIndex() == 3) {
							data = eff_dt;
						}
						
						else if(cell.getColumnIndex() == 4) {
							data = rel_dt;
						}
						
//						if(data != null) {
//							data = data.substring(1, data.length()-1);
//						}
//						System.out.println(index+":::>>"+data);
						stmt1.setString(index++, data);
					}
//					System.out.println(no);
					queryString = "SELECT ENT_PROFILE FROM FMS_TRUCK_TRANSPORTER_LINK WHERE ENT_PROFILE = ? AND TRUCK_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND TRUCK_TRANS_CD = ? AND LINK_SEQ = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, no);
					stmt.setString(3, eff_dt);
					stmt.setString(4, cd);
					stmt.setString(5, seq);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !no.equals("")) {
						
						
						logger.data(fname, (company_cd + "," + cd + "," + no + "," + eff_dt + "," + seq + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + cd + "," + no + "," + eff_dt + "," + seq + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_TRUCK_TRANSPORTER_LINK>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_TRANSPORTER_LINK>>,,,", conn);
			
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
	
	public void FMS_TRUCK_DRIVER_TRANS_LINK() throws IOException, SQLException {
		
		function_nm="FMS_TRUCK_DRIVER_TRANS_LINK()";
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_DRIVER_TRANS_LINK>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRUCK_DRIVER_TRANS_LINK>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String no = "",rel_dt="";
			String seq="";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
//			int max_cd = 1;
			
			queryString1 = "INSERT INTO FMS_TRUCK_DRIVER_TRANS_LINK(TRUCK_TRANS_CD,DRIVER_CD,LINK_SEQ,EFF_DT,RELEASE_DT,REMARKS,R_REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE) "
					+ "VALUES(?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
//			queryString = "SELECT MAX(TRUCK_TRANS_CD) FROM FMS_TRUCK_DRIVER_TRANS_LINK ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				max_cd = rset.getInt(1)+1;
//			}
//			rset.close();
//			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_TRANS_LINK_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_TRANS_LINK_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "ENT_PROFILE,DRIVER_CD,EFF_DT,TRUCK_TRANS_CD,LINK_SEQ,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					data = "";
					cd = ""; eff_dt = "";
					no = "";rel_dt="";
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						if(cell.getColumnIndex() == 2) {
							seq = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (seq!=null) {
								seq = seq.substring(1, seq.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 3) {
							eff_dt = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (eff_dt!=null) {
								eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 4) {
							rel_dt = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if(rel_dt!=null) {
							rel_dt = rel_dt.substring(1, rel_dt.length()-1);
							}
						}

						
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						if (data != null) {
							data = data.substring(1, data.length() - 1);
						}

						
						if (cell.getColumnIndex() == 0) {
//							data = data.substring(1, data.length()-1);
							
							queryString = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE TRUCK_TRANS_ABBR = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = cd;
						}
						else if(cell.getColumnIndex() == 1) {
//							data = data.substring(1, data.length()-1);
							
							queryString = "SELECT DRIVER_CD FROM FMS_TRUCK_DRIVER_MST WHERE LICENCE_NO = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, data);  
							rset = stmt.executeQuery();
							if(rset.next()) {
								no = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = no;
						}
						else if(cell.getColumnIndex() == 2) {
//							queryString = "SELECT LINK_SEQ FROM FMS_TRUCK_DRIVER_TRANS_LINK WHERE TRUCK_TRANS_CD = ? AND DRIVER_CD = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
//							stmt = conn.prepareStatement(queryString);
//							stmt.setString(1, cd);
//							stmt.setString(2, no);
//							stmt.setString(3, eff_dt);
//							rset = stmt.executeQuery();
//							if(rset.next() && rset.getInt(1) > 0) {
//								seq = rset.getInt(1);
//							}
//							else {
//							queryString2 = "SELECT MAX(LINK_SEQ) FROM FMS_TRUCK_DRIVER_TRANS_LINK WHERE DRIVER_CD = ?  ";
//							stmt2 = conn.prepareStatement(queryString2);
//							stmt2.setString(1, no);
//							rset2 = stmt2.executeQuery();
//							if(rset2.next() && rset2.getInt(1) > 0) {
//								seq = rset2.getInt(1);
//							}
//							else {
//								seq = 0;
//							}
//							rset2.close();
//							stmt2.close();
//							}
//							rset.close();
//							stmt.close();
							
							data = seq;
						}
						
						else if(cell.getColumnIndex() == 3) {
							data = eff_dt;
						}
						
						else if(cell.getColumnIndex() == 4) {
							data = rel_dt;
						}
//						if(data != null) {
//							data = data.substring(1, data.length()-1);
//						}
//						System.out.println(index+":::>>"+data);
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT DRIVER_CD FROM FMS_TRUCK_DRIVER_TRANS_LINK WHERE ENT_PROFILE = ? AND DRIVER_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND TRUCK_TRANS_CD = ? AND LINK_SEQ = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, no);
					stmt.setString(3, eff_dt);
					stmt.setString(4, cd);
					stmt.setString(5, seq);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("") && !no.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd + "," + no + "," + eff_dt + "," + cd + "," + seq + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + no + "," + eff_dt + "," + cd + "," + seq + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_TRUCK_DRIVER_TRANS_LINK>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_DRIVER_TRANS_LINK>>,,,", conn);
			
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
	
	//BELOW FUNCTION FOR EXCEL
	public void FMS_TRUCK_DRIVER_LINK() throws IOException, SQLException {
		
		function_nm="FMS_TRUCK_DRIVER_LINK()";
		try {
			
			System.out.println("<<START>><<FMS_TRUCK_DRIVER_LINK>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRUCK_DRIVER_LINK>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String no = "",rel_dt="";
			String seq="";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
//			int max_cd = 1;
			
			queryString1 = "INSERT INTO FMS_TRUCK_DRIVER_LINK(TRUCK_CD,DRIVER_CD,LINK_SEQ,EFF_DT,RELEASE_DT,REMARKS,R_REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE) "
					+ "VALUES(?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
//			queryString = "SELECT MAX(TRUCK_TRANS_CD) FROM FMS_TRUCK_DRIVER_TRANS_LINK ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				max_cd = rset.getInt(1)+1;
//			}
//			rset.close();
//			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_LINK_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRUCK_DRIVER_LINK_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "ENT_PROFILE,TRUCK_CD,DRIVER_CD,EFF_DT,LINK_SEQ,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					data = "";
					cd = ""; eff_dt = "";
					no = "";
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						if(cell.getColumnIndex() == 0) {
							cd = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (cd!=null) {
								cd = cd.substring(1, cd.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 2) {
							seq = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (seq!=null) {
								seq = seq.substring(1, seq.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 3) {
							eff_dt = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (eff_dt!=null) {
								eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 4) {
							rel_dt = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if(rel_dt!=null) {
							rel_dt = rel_dt.substring(1, rel_dt.length()-1);
							}
						}
						
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						if (data != null) {
							data = data.substring(1, data.length() - 1);
						}
						
						if (cell.getColumnIndex() == 0) {
//							data = data.substring(1, data.length()-1);
							
//							queryString = "SELECT TRUCK_CD FROM FMS_TRUCK_MST WHERE TRUCK_REG_NUM = ? ";
//							stmt = conn.prepareStatement(queryString);
//							stmt.setString(1, data);
//							rset = stmt.executeQuery();
//							if(rset.next()) {
//								cd = rset.getString(1);
//							}
//							
//							rset.close();
//							stmt.close();
							data = cd;
						}
						else if(cell.getColumnIndex() == 1) {
//							data = data.substring(1, data.length()-1);
							
							queryString = "SELECT DRIVER_CD FROM FMS_TRUCK_DRIVER_MST WHERE LICENCE_NO = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, data);  
							rset = stmt.executeQuery();
							if(rset.next()) {
								no = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = no;
						}
						
						else if(cell.getColumnIndex() == 2) {
//							queryString = "SELECT LINK_SEQ FROM FMS_TRUCK_DRIVER_LINK WHERE TRUCK_CD = ? AND DRIVER_CD = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
//							stmt = conn.prepareStatement(queryString);
//							stmt.setString(1, cd);
//							stmt.setString(2, no);
//							stmt.setString(3, eff_dt);
//							rset = stmt.executeQuery();
//							if(rset.next() && rset.getInt(1) > 0) {
//								seq = rset.getInt(1);
//							}
//							else {
//								queryString2 = "SELECT MAX(LINK_SEQ) FROM FMS_TRUCK_DRIVER_LINK WHERE DRIVER_CD = ?  ";
//								stmt2 = conn.prepareStatement(queryString2);
//								stmt2.setString(1, no);
//								rset2 = stmt2.executeQuery();
//								if(rset2.next() && rset2.getInt(1) > 0) {
//									seq = rset2.getInt(1);
//								}
//								else {
//									seq = 1;
//								}
//								rset2.close();
//								stmt2.close();
//							}
//							rset.close();
//							stmt.close();
							
							data = seq;
						}
						
						else if(cell.getColumnIndex() == 3) {
							data = eff_dt;
						}
						
						else if(cell.getColumnIndex() == 4) {
							data = rel_dt;
						}


//						if(data != null) {
//							data = data.substring(1, data.length()-1);
//						}
//						System.out.println(index+":::>>"+data);
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT DRIVER_CD FROM FMS_TRUCK_DRIVER_LINK WHERE ENT_PROFILE = ? AND TRUCK_CD = ? AND DRIVER_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND LINK_SEQ = ?  ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, no);
					stmt.setString(4, eff_dt);
					stmt.setString(5, seq);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("") && !no.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd + "," + cd + "," + no + "," + eff_dt + "," + seq + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + cd + "," + no + "," + eff_dt + "," + seq + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_TRUCK_DRIVER_LINK>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_DRIVER_LINK>>,,,", conn);
			
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
	
	//BELOW FUNCTION ONLY READER WHICH TAKES DATA FROM EMS TABLES FOR LINK DETAILS
//	public void FMS_TRUCK_DRIVER_LINK() throws IOException, SQLException {
//		
//		function_nm="FMS_TRUCK_DRIVER_LINK()";
//		try {
//			
//			System.out.println("<<START>><<FMS_TRUCK_DRIVER_LINK>>");
//			
//			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRUCK_DRIVER_LINK>>,,", conn);
//			
//			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
//			
//			columns = "TRUCK_CD,DRIVER_CD,EFF_DT,RELEASE_DT,REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
//			
//			data = "";
//			String no = "";
//			logger_count = 0;   
//			skipped_count = 0;   
//			total_count = 0;
//			
////			int count =0;
//			
//			queryString1 = "INSERT INTO FMS_TRUCK_DRIVER_LINK(TRUCK_CD,DRIVER_CD,EFF_DT,RELEASE_DT,REMARKS,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE) "
//					+ "VALUES(?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
//			
//			stmt1 = conn.prepareStatement(queryString1);
//			
//				
//				// Below block of code is for inserting SEIPL data
//				queryString2 = " SELECT A.TRUCK_CD, B.DRIVER_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.RELEASE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.REMARKS, "
//						+ "'2', A.ENT_BY, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL FROM FMS_TRUCK_TRANSPORTER_LINK A, FMS_TRUCK_DRIVER_TRANS_LINK B "
//						+ "WHERE A.TRUCK_TRANS_CD = B.TRUCK_TRANS_CD AND A.ENT_PROFILE = ? AND B.ENT_PROFILE = ? ";
//						
//				stmt2 = conn.prepareStatement(queryString2);
//				stmt2.setString(1, company_cd);	
//				stmt2.setString(2, company_cd);	
//				rset2 = stmt2.executeQuery();
//				
//				logger.checkpoint(fname, "ENT_PROFILE,TRUCK_CD,DRIVER_CD,EFF_DT,TIMESTAMP,", conn);
//				
//				while (rset2.next()) {
//					
//					data = "";no = "";
//					cd = ""; eff_dt = "";
//					total_count++;  
//					
//					index = 1;
//					stmt1 = conn.prepareStatement(queryString1);
//					no = rset2.getString(1);
//					cd = rset2.getString(2);
//					eff_dt = rset2.getString(3);
//					
//					for (int i = 0; i < columns.split(",").length; i++) {
//						
//						data = (rset2.getString(i + 1) == null || rset2.getString(i + 1).equals("0")) ? null : rset2.getString(i + 1).trim().replaceAll("'", "");
//					
//						logger.data(fname, (company_cd + "," + cd + "," + no + "," + eff_dt + " , " ), conn, "");
////						System.out.println(index+":::>>"+data);
//						stmt1.setString(index++, data);
//						}
//					
//					
//					queryString = "SELECT TRUCK_CD FROM FMS_TRUCK_DRIVER_LINK WHERE ENT_PROFILE = ? AND TRUCK_CD = ? AND DRIVER_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ";
//					stmt = conn.prepareStatement(queryString);
//					stmt.setString(1, company_cd);
//					stmt.setString(2, no);
//					stmt.setString(3, cd);
//					stmt.setString(4, eff_dt);
//					
//					rset = stmt.executeQuery();
//					
//					if (!rset.next()) {
//						
//						logger.data(fname, (company_cd + "," + no + "," + cd + "," + eff_dt + " , " ), conn, "");
//						
//						stmt1.executeUpdate();
//						
//						stmt1.close();
//						
//						logger_count++;
//					}
//					else {
//						stmt1.close();
//						skipped_count++;     
//						logger.data(fname, (company_cd + "," + no + "," + cd + "," + eff_dt + " , " ), conn, "E");
//					}
//					rset.close();
//					stmt.close();
//					
//				}
//				rset2.close();
//				stmt2.close();
//				
//				msg = "Data has been Inserted Successfully in Database.";
//				msg_type = "S";
//				
//			//conn.commit();
//			
//			System.out.println("<<END>><<FMS_TRUCK_DRIVER_LINK>>");
//			System.out.println();
//			
//			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 
//			
//			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
//			
//			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
//			
//			logger.checkpoint(fname, "<<END>>,<<FMS_TRUCK_DRIVER_LINK>>,,", conn);
//			
//			logger.checkpoint1(fname1,logger_count+",", conn);
//			
//		}
//		catch(Exception e)
//		{
//			
//			msg = "One of the Functions faced an Error. Data Not Inserted.";
//			msg_type = "E";
//			
//			conn.rollback();
//			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//			logger.error(fname, e, function_nm, conn, fname_error);
//		}finally {
//			conn.commit();
//		}
//		
//	}	
	
	public void FMS_LINK_CHECKPOST_PLANT() throws IOException, SQLException {
		
		function_nm="FMS_LINK_CHECKPOST_PLANT()";
		try {
			
			System.out.println("<<START>><<FMS_LINK_CHECKPOST_PLANT>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_LINK_CHECKPOST_PLANT>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String no = "",type = "",ch_no = "",rev_seq="";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
//			int max_cd = 1;
			
			queryString1 = "INSERT INTO FMS_LINK_CHECKPOST_PLANT(COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,PLANT_SEQ_NO,CHKPOST_CD,REV_SEQ,EFF_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,RELEASE_DT) "
					+ "VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
//			queryString = "SELECT MAX(TRUCK_TRANS_CD) FROM FMS_TRUCK_DRIVER_TRANS_LINK ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				max_cd = rset.getInt(1)+1;
//			}
//			rset.close();
//			stmt.close();
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_LINK_CHECKPOST_PLANT_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LINK_CHECKPOST_PLANT_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,PLANT_SEQ_NO,CHKPOST_CD,EFF_DT,REV_SEQ,TIMESTAMP,", conn);
				
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
						
						if(cell.getColumnIndex() == 2) {
							type = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (type!=null) {
								type = type.substring(1, type.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 3) {
							no = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (no!=null) {
								no = no.substring(1, no.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 5) {
							rev_seq = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (rev_seq!=null) {
								rev_seq = rev_seq.substring(1, rev_seq.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 6) {
							eff_dt = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (eff_dt!=null) {
								eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
							}
						}
						
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						
						if (cell.getColumnIndex() == 1) {
							if (data!=null) {
								data = data.substring(1, data.length() - 1);
							}
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = "'"+cd+"'";
						}
						if(cell.getColumnIndex() == 4) {
							if (data!=null) {
								data = data.substring(1, data.length() - 1);
							}
							queryString = "SELECT CHKPOST_CD FROM FMS_CHECKPOST_MST WHERE CHKPOST_NAME = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, data);  
							rset = stmt.executeQuery();
							if(rset.next()) {
								ch_no = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = "'"+ch_no+"'";
						}
						
						if(data != null && data.contains("null")) {
							data = null;
						}
						if(data != null) {
							data = data.substring(1, data.length()-1);
						}
//						System.out.println(index+":::>>"+data);
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_LINK_CHECKPOST_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND ENTITY_TYPE = ? AND CHKPOST_CD = ? AND REV_SEQ = ? AND PLANT_SEQ_NO = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, eff_dt);
					stmt.setString(4, type);
					stmt.setString(5, ch_no);
					stmt.setString(6, rev_seq);
					stmt.setString(7, no);
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + " , " + ch_no + "," + eff_dt + " , " + rev_seq + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + " , " + ch_no + "," + eff_dt + " , " + rev_seq + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_LINK_CHECKPOST_PLANT>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_LINK_CHECKPOST_PLANT>>,,,,,", conn);
			
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
	
	public void FMS_AGMT_MST() throws IOException, SQLException {
		
		function_nm="FMS_AGMT_MST(DLNG)()";
		try {
			
			System.out.println("<<START>><<FMS_AGMT_MST(DLNG)>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_AGMT_MST(DLNG)>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
//			String no = "",type = "",rev = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
//			int max_cd = 1;
			
			queryString1 = "INSERT INTO FMS_AGMT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,SIGNING_DT,START_DT,END_DT,RENEWAL_DT,"
					+ "AGMT_BASE,AGMT_TYP,STATUS,BUYER_NOM_CLAUSE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_CLAUSE,"
					+ "SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,"
					+ "MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,"
					+ "SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,FLAG,REV_DT,REMARKS,LIABILITY_CLAUSE,"
					+ "BILLING_CLAUSE,LC_CLAUSE,RENEWAL_FLAG,PRE_APPROVAL_DATE,PRE_APPROVAL,PRE_APPROVAL_BY,REOPEN_REQUEST_FLAG,REOPEN_REQUEST_DT,"
					+ "REOPEN_APPROVAL_FLAG,REOPEN_APPROVAL_DT,REOPEN_REQUEST_BY,REOPEN_APPROVE_BY,REMARK,CONT_NAME,AGMT_REF_NO,BILLING_FLAG,"
					+ "BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,MEAS_CLAUSE,SPEC_CLAUSE,LIABILITY,TERMINATE_FLAG,TERMINATE_CLAUSE,"
					+ "TERMINATE_PLANED,TERMINATE_FORCE) "
					+ "VALUES(?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,"
					+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,"
					+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),"
					+ "?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,"
					+ "?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_MST(DLNG)_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_MST(DLNG)_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					data = "";
					cd = ""; eff_dt = "";
					no = "";type = "";rev = "";
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						
						if(cell.getColumnIndex() == 2) {
							type = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (type!=null) {
								type = type.substring(1, type.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 3) {
							no = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (no!=null) {
								no = no.substring(1, no.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 4) {
							rev = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (rev!=null) {
								rev = rev.substring(1, rev.length() - 1);
							}
						}
						
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						
						if (cell.getColumnIndex() == 1) {	//COUNTERPARTY_CD
							if (data!=null) {
								data = data.substring(1, data.length() - 1);
							}
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = "'"+cd+"'";
						}
						else if(cell.getColumnIndex() == 10) {	//AGMT_TYP
							if (data!=null) {
									data = data.substring(1, data.length() - 1);
								if(data.equals("T")){
									data = "'"+ "0" +"'";
								}
								else if(data.equals("S")) {
									data = "'"+ "1" +"'";
								}
							}
						}
						else if(cell.getColumnIndex() == 11) {	//STATUS CHANGES TO A & D
							if (data!=null) {
									data = data.substring(1, data.length() - 1);
								if(data.equals("Y")) {
									data = "'"+ "A" +"'";
								}
								else {
									data = "'"+ "D" +"'";
								}
							}
						}
						
						
						if(data != null && data.contains("null")) {
							data = null;
						}
						if(data != null) {
							data = data.substring(1, data.length()-1);
						}
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, type);
					stmt.setString(4, no);
					stmt.setString(5, rev);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + "," + rev + ","  ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + "," + rev + ","  ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_AGMT_MST(DLNG)>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_MST(DLNG)>>,,,,", conn);
			
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
	
	public void FMS_AGMT_BU() throws IOException, SQLException {

		function_nm="FMS_AGMT_BU(DLNG)()";
		try {
			
			
			System.out.println("<<START>><<FMS_AGMT_BU(DLNG)>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_AGMT_BU(DLNG)>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			

			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString5 = "SELECT DISTINCT(SEQ_NO) FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY ='B' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			rset5 = stmt5.executeQuery();
			
			while(rset5.next()) {
			
			
			
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, NULL, ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_AGMT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_AGMT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
//			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
			
			while (rset.next()) {
				
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						
						if(i == 5) {	//PLANT_SEQ_NO
							data = rset5.getString(1);
						}
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = ?  ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(3));
				stmt2.setString(4, rset.getString(4));
				stmt2.setString(5, rset.getString(5));
				stmt2.setString(6, rset5.getString(1));
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
//					skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				}
	
			
			rset.close();
			stmt.close();
		}
		rset5.close();
		stmt5.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_AGMT_BU(DLNG)>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_BU(DLNG)>>,", conn);
			
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
		}
		
	}

	
	
	public void FMS_AGMT_PLANT() throws IOException, SQLException {

		function_nm="FMS_AGMT_PLANT(DLNG)()";
		try {
			
			
			System.out.println("<<START>><<FMS_AGMT_PLANT(DLNG)>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_AGMT_PLANT(DLNG)>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			

			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT";
			
			
			queryString5 = "SELECT SEQ_NO,COUNTERPARTY_CD FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY ='C' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
//			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			while(rset5.next()) {
				cd = rset5.getString(2);
				
			
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, NULL, NULL, ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			stmt.setString(2,cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_AGMT_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
//			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
			
			while (rset.next()) {
				
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						
						if(i == 5) {	//PLANT_SEQ_NO
							data = rset5.getString(1);
						}
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = ?  ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(3));
				stmt2.setString(4, rset.getString(4));
				stmt2.setString(5, rset.getString(5));
				stmt2.setString(6, rset5.getString(1));
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
//					skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				
				
				}
	
			rset.close();
			stmt.close();
			}
			rset5.close();
			stmt5.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_AGMT_PLANT(DLNG)>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_PLANT(DLNG)>>,", conn);
			
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
		}
		
	}
	
	public void FMS_AGMT_BILLING_DTL() throws IOException, SQLException {
		
		function_nm="FMS_AGMT_BILLING_DTL(DLNG)()";
		try {
			
			System.out.println("<<START>><<FMS_AGMT_BILLING_DTL(DLNG)>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_AGMT_BILLING_DTL(DLNG)>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String plant="",name="",int_cd="",exchg_cd="";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
//			int max_cd = 1;
			
			queryString1 = "INSERT INTO FMS_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,"
					+ "PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,"
					+ "MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_BILLING_DTL(DLNG)_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_BILLING_DTL(DLNG)_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,INT_RATE_CD,EXCHNG_RATE_CD,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					data = "";
					cd = ""; eff_dt = "";
					no = "";type = "";rev = "";plant="";name="";int_cd="";exchg_cd="";
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						
						if(cell.getColumnIndex() == 2) {
							type = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (type!=null) {
								type = type.substring(1, type.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 3) {
							no = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (no!=null) {
								no = no.substring(1, no.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 4) {
							rev = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (rev!=null) {
								rev = rev.substring(1, rev.length() - 1);
							}
						}
						if(cell.getColumnIndex() == 27) {
							plant = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (plant!=null) {
								plant = plant.substring(1, plant.length() - 1);
							}
						}
						
						
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						
						if (cell.getColumnIndex() == 1) {	//COUNTERPARTY_CD
							if (data!=null) {
								data = data.substring(1, data.length() - 1);
							}
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							
							rset.close();
							stmt.close();
							data = "'"+cd+"'";
						}
						else if(cell.getColumnIndex() == 6) {	//BILLING_FLAG
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name != null) {
								name = name.substring(1, name.length() - 1);
							
				    		
				    		if(name.equals("T")) {
				    			name = "T";
				    		}else if(name.equals("B")) {
				    			name = "B";
				    		}else if(name.equals("Y")) {
				    			name = null;
				    		}
				    		}
				    		data = "'"+name+"'";
				    	}
						
						else if(cell.getColumnIndex() == 11) {	//INT_CAL_RATE_CD
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name != null) {
								name = name.substring(1, name.length() - 1);
							}
							queryString = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) LIKE ? ";
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, "%"+name+"%");
					    	rset = stmt.executeQuery();
					    	if (rset.next()) {
					    		int_cd = rset.getString(1);
					    	}else {
					    		int_cd =  null;
					    	}
					    	rset.close();
					    	stmt.close();
					    	data = "'"+int_cd+"'";
				    	}
						
						else if(cell.getColumnIndex() == 14) {	//EXCHNG_RATE_CD
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name!=null) {
								name = name.substring(1, name.length() - 1);
							}
							queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) LIKE ? ";
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, "%"+name+"%");
					    	rset = stmt.executeQuery();
					    	if (rset.next()) {
					    		exchg_cd = rset.getString(1);
					    	}
					    	rset.close();
					    	stmt.close();
					    	data = "'"+exchg_cd+"'";
				    	}
						
						else if (cell.getColumnIndex() == 27) {	//PLANT_SEQ_NO
							plant = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (plant!=null) {
				    			plant = plant.substring(1, plant.length() - 1);
							}
							
//							queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY = 'C' AND UPPER(PLANT_ABBR) = ? ";
//							stmt = conn.prepareStatement(queryString);
//							stmt.setString(1, company_cd);
//							stmt.setString(2, plant);
//							rset = stmt.executeQuery();
//							if(rset.next()) {
//								plant = rset.getString(1);
//							}
//							
//							rset.close();
//							stmt.close();
							data = "'"+plant+"'";
						}
						
						
						if(data != null && data.contains("null")) {
							data = null;
						}
						if(data != null) {
							data = data.substring(1, data.length()-1);
						}
//						System.out.println(index+">>>"+data);
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, type);
					stmt.setString(4, no);
					stmt.setString(5, rev);
					stmt.setString(6, plant);
					
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + "," + rev + "," + plant + "," + int_cd + "," + exchg_cd + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + "," + rev + "," + plant + "," + int_cd + "," + exchg_cd + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_AGMT_BILLING_DTL(DLNG)>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_BILLING_DTL(DLNG)>>,,", conn);
			
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
	
	public void FMS_AGMT_TRUCK_TRANS() throws IOException, SQLException {

		function_nm="FMS_AGMT_TRUCK_TRANS()";
		try {
			
			
			System.out.println("<<START>><<FMS_AGMT_TRUCK_TRANS>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_AGMT_TRUCK_TRANS>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			

			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,ENT_BY,ENT_DT";
			
			
			queryString5 = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE ENT_PROFILE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
//			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			while(rset5.next()) {
				
			
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, NULL, ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_AGMT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_AGMT_TRUCK_TRANS(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
//			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
			
			while (rset.next()) {
				
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						
						if(i == 5) {	//TRANSPORTER_CD
							data = rset5.getString(1);
						}
//						System.out.println(columns.split(",")[i]+""+data);
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_TRUCK_TRANS WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND TRANSPORTER_CD = ?  ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(3));
				stmt2.setString(4, rset.getString(4));
				stmt2.setString(5, rset.getString(5));
				stmt2.setString(6, rset5.getString(1));
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
//					skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				
				
				}
	
			rset.close();
			stmt.close();
			}
			rset5.close();
			stmt5.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_AGMT_TRUCK_TRANS>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_TRUCK_TRANS>>,", conn);
			
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
		}
		
	}
	
       public void FMS_AGMT_FILLING_STN() throws IOException, SQLException {

		function_nm="FMS_AGMT_FILLING_STN()";
		try {
			
			
			System.out.println("<<START>><<FMS_AGMT_FILLING_STN>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_AGMT_FILLING_STN>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			

			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,FILL_STATION_CD,ENT_BY,ENT_DT";
			
			queryString5 = "SELECT DISTINCT(FILL_STATION_CD) FROM FMS_FILLING_STATION_MST ";
			stmt5 = conn.prepareStatement(queryString5);
//			stmt5.setString(1, company_cd);
			rset5 = stmt5.executeQuery();
			
			while(rset5.next()) {
			
			
			
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, NULL, ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_AGMT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_AGMT_FILLING_STN(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,FILL_STATION_CD,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
//			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
			
			while (rset.next()) {
				
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						
						if(i == 5) {	//fill_station_cd
							data = rset5.getString(1);
						}
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_FILLING_STN WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND FILL_STATION_CD = ?  ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(3));
				stmt2.setString(4, rset.getString(4));
				stmt2.setString(5, rset.getString(5));
				stmt2.setString(6, rset5.getString(1));
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
//					skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				}
	
			
			rset.close();
			stmt.close();
		}
		rset5.close();
		stmt5.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_AGMT_FILLING_STN>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_FILLING_STN>>,", conn);
			
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
		}
		
	}

	public void FMS_AGMT_SVC_MST() throws IOException, SQLException {

		function_nm="FMS_AGMT_SVC_MST()";
		try {
			
			System.out.println("<<START>><<FMS_AGMT_SVC_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_AGMT_SVC_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			String  type = "",counterparty="",abbr="",clause="",mmcq="",name="",ref="";
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_AGMT_SVC_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,"
					+ "SIGNING_DT,START_DT,END_DT,"
					+ "STATUS,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,MMCQ_FLAG,MMCQ_CLAUSE,MMCQ_PERCENTAGE,BILLING_FLAG,BILLING_CLAUSE,ENT_BY,"
					+ "ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT) "
					+ "VALUES(?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_SVC_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_SVC_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,MMCQ_PERCENTAGE,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = ""; type="";
						name = "";ref="";mmcq="";abbr="";
						no="";rev="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							if (cell.getColumnIndex() == 1) {
								cd = cell.getStringCellValue();
								if (cd!=null) {
									cd = cd.substring(1, cd.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 2) {
								type = cell.getStringCellValue();
								if (type!=null) {
									type = type.substring(1, type.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 3) {
								no = cell.getStringCellValue();
								if (no!=null) {
									no = no.substring(1, no.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 4) {
								rev = cell.getStringCellValue();
								if (rev!=null) {
									rev = rev.substring(1, rev.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 5) {
								name = cell.getStringCellValue();
								if (name!=null) {
									name = name.substring(1, name.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 6) {
								ref = cell.getStringCellValue();
								if (ref!=null) {
									ref = ref.substring(1, ref.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 17) {
								mmcq = cell.getStringCellValue();
								if (mmcq!=null) {
									mmcq = mmcq.substring(1, mmcq.length() - 1);
								}
							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							
							if (cell.getColumnIndex() == 1) {	//COUNTERPARTY_CD
								if (data!=null) {
									data = data.substring(1, data.length() - 1);
								
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, data.toUpperCase());
								rset = stmt.executeQuery();
								if(rset.next()) {
									cd = rset.getString(1);
								}
								
								rset.close();
								stmt.close();
								}
								data = "'"+cd+"'";
							}
							
							
							if(data!=null && data.contains("null")) {
								data = null;
							}
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_SVC_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, type);
						stmt.setString(4, no);
						stmt.setString(5, rev);
						
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + "," + rev + "," + name + "," + ref + "," + mmcq + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + "," + rev + "," + name + "," + ref + "," + mmcq + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_AGMT_SVC_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_SVC_MST>>,,", conn);
			
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

	public void FMS_AGMT_SVC_PLANT() throws IOException, SQLException {

		function_nm="FMS_AGMT_SVC_PLANT()";
		try {
			
			
			System.out.println("<<START>><<FMS_AGMT_SVC_PLANT>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_AGMT_SVC_PLANT>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String plant="";
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, NULL, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_AGMT_SVC_MST WHERE COMPANY_CD = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1,company_cd);
			rset5 = stmt5.executeQuery();
			
			queryString1 = "INSERT INTO FMS_AGMT_SVC_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
//			stmt1 = conn.prepareStatement(queryString1);
			
//			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
			
			while (rset5.next()) {
					
					cd = rset5.getString(2);
				
					queryString = "SELECT DISTINCT(SEQ_NO) FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY = 'C' AND COUNTERPARTY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1,company_cd);
					stmt.setString(2,cd);
					rset = stmt.executeQuery();
					
					
					while(rset.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset5.getString(i+1) == null ? "" : rset5.getString(i+1);
						
						if(i == 5) {	//PLANT_SEQ_NO
							plant = rset.getString(1);
							data = plant;
						}
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_SVC_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = ?  ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset5.getString(2));
				stmt2.setString(3, rset5.getString(3));
				stmt2.setString(4, rset5.getString(4));
				stmt2.setString(5, rset5.getString(5));
				stmt2.setString(6, plant);
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
					}
					stmt2.close();
					rset2.close();
				
				}
				stmt.close();
				rset.close();
			}
			rset5.close();
			stmt5.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_AGMT_SVC_PLANT>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_SVC_PLANT>>,", conn);
			
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
		}
		
	}

	public void FMS_AGMT_SVC_BU() throws IOException, SQLException {
		
		function_nm="FMS_AGMT_SVC_BU()";
		try {
			
			
			System.out.println("<<START>><<FMS_AGMT_SVC_BU>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_AGMT_SVC_BU>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String plant="";
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString = "SELECT DISTINCT(SEQ_NO) FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY = 'B' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_AGMT_SVC_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
//			stmt1 = conn.prepareStatement(queryString1);
			
//			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
			
			while (rset.next()) {
				
				queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, NULL, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_AGMT_SVC_MST WHERE COMPANY_CD = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1,company_cd);
				rset5 = stmt5.executeQuery();
				
				while(rset5.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset5.getString(i+1) == null ? "" : rset5.getString(i+1);
						
						if(i == 5) {	//PLANT_SEQ_NO
							plant = rset.getString(1);
							data = plant;
						}
						stmt1.setString(i+1,data);
					}
					
					//for data already exists..
					queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_SVC_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = ?  ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, company_cd);
					stmt2.setString(2, rset5.getString(2));
					stmt2.setString(3, rset5.getString(3));
					stmt2.setString(4, rset5.getString(4));
					stmt2.setString(5, rset5.getString(5));
					stmt2.setString(6, plant);
					
					rset2 = stmt2.executeQuery();
					
					if (!rset2.next() ) {
						
						//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					stmt2.close();
					rset2.close();
					
				}
				stmt5.close();
				rset5.close();
			}
			rset.close();
			stmt.close();
			
			
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_AGMT_SVC_BU>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_AGMT_SVC_BU>>,", conn);
			
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
		}
		
	}
	
	public void FMS_SVC_CONT_MST() throws IOException, SQLException {

		function_nm="FMS_SVC_CONT_MST()";
		try {
			
			System.out.println("<<START>><<FMS_SVC_CONT_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SVC_CONT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			String  type = "",counterparty="",abbr="",clause="",mmcq="",name="",ref="";
			int max=0;
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_SVC_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,CONT_REF_NO,"
					+ "DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,DCQ,VARIABLE_DCQ,"
					+ "RENEWAL_DT,CONT_STATUS,IS_ALLOCATED,FILL_STATION_CD,PLANT_SEQ_NO,NEW_START_DT,NEW_END_DT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,LAYOVER_CHARGE_INR,LAYOVER_HRS,"
					+ "DAY_DEF_FLAG,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,MMCQ_FLAG,MMCQ_CLAUSE,MMCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,CHANGE_DATE_REQ,CHANGE_DATE_APPROVE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TRANSPORT_MGMT_CHARGE,TRANSPORT_MGMT_UNIT,EFF_DT,"
					+ "QTY_OPTION,QTY_OPTION_FIRM,QTY_OPTION_RE,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
					+ "?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SVC_CONT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SVC_CONT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NAME,CONT_REF_NO,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = ""; type="";
						name = "";ref="";mmcq="";abbr="";
						no="";rev="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							
							if (cell.getColumnIndex() == 1) {
								ref = cell.getStringCellValue();
								if (ref!=null) {
									ref = ref.substring(1, ref.length() - 1);
								}
							}
							else if (cell.getColumnIndex() == 2) {
								no = cell.getStringCellValue();
								if (no!=null) {
									no = no.substring(1, no.length() - 1);
								}
							}
							else if (cell.getColumnIndex() == 3) {
								rev = cell.getStringCellValue();
								if (rev!=null) {
									rev = rev.substring(1, rev.length() - 1);
								}
							}
							else if (cell.getColumnIndex() == 5) {
								cont_rev = cell.getStringCellValue();
								if (cont_rev!=null) {
									cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}
							}
							else if (cell.getColumnIndex() == 6) {
								type = cell.getStringCellValue();
								if (type!=null) {
									type = type.substring(1, type.length() - 1);
								}
							}
							else if (cell.getColumnIndex() == 7) {
								name = cell.getStringCellValue();
								if (name!=null) {
									name = name.substring(1, name.length() - 1);
								}
							}
							
//							if (cell.getColumnIndex() == 17) {
//								mmcq = cell.getStringCellValue();
//								mmcq = mmcq.substring(1, mmcq.length()-1);
//							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							
							if(cell.getColumnIndex() == 0) {
								if (data!=null) {
									data = data.substring(1, data.length() - 1);
								}
								cont_no = data;
								
								data = "'"+company_cd+"'";
							}
							
							else if (cell.getColumnIndex() == 1) {	//COUNTERPARTY_CD
								if (data!=null) {
									data = data.substring(1, data.length() - 1);
									data = data.split("-")[0];
								}
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, data);
								rset = stmt.executeQuery();
								if(rset.next()) {
									cd = rset.getString(1);
								}
								
								rset.close();
								stmt.close();
								data = "'"+cd+"'";
							}
							
							else if(cell.getColumnIndex() == 4) {	//CONT_NO
								max = Integer.parseInt(cont_no);
								
								queryString2 = "SELECT CONT_NO FROM FMS_SVC_CONT_MST WHERE CONT_REF_NO LIKE ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, (ref.split("-")[0] + "-" + ref.split("-")[1] + "-" + ref.split("-")[2] + "-" + ref.split("-")[3] + "-" + ref.split("-")[4] + "-") + "%");
								stmt2.setString(2, cd);
								rset2 = stmt2.executeQuery();
								if (rset2.next()) {
									cont_no = rset2.getString(1);
								}
								else {
						    		
					    			queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_SVC_CONT_MST WHERE COMPANY_CD = '2' AND CONT_NO LIKE ? ";
						    		stmt = conn.prepareStatement(queryString);	
						    		stmt.setString(1, max+"%");
						    		rset = stmt.executeQuery();
						    		if (rset.next() && rset.getInt(1) > 0) {
						    			max = rset.getInt(1);
//							    		System.out.println(ref+"==="+max);
						    				
						    		}else{				    		
						    			max = max * 10000;
						    			max++;
//							    		System.out.println(cont_no+"__"+ref+"---"+max);
						    		}
						    		rset.close();
						    		stmt.close();
						    		cont_no = max + "";
								}
								data = "'"+cont_no+"'";
								
								rset2.close();
								stmt2.close();
							}
							
							
							if(data!=null && data.contains("null")) {
								data = null;
							}
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
//							System.out.println(index+">>>"+data);
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_SVC_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REF_NO = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, no);
						stmt.setString(4, rev);
						stmt.setString(5, cont_rev);
						stmt.setString(6, type);
						stmt.setString(7, ref);
						
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
//							System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + "," + rev + "," + name + "," + ref + "," ), conn, "");
							
							stmt1.executeUpdate();
//							conn.commit();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + "," + rev + "," + name + "," + ref + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_SVC_CONT_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SVC_CONT_MST>>,,", conn);
			
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
	
	public void FMS_SVC_CONT_BU() throws IOException, SQLException {
		
		function_nm="FMS_SVC_CONT_BU()";
		try {
			
			
			System.out.println("<<START>><<FMS_SVC_CONT_BU>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SVC_CONT_BU>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String plant="1";
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString1 = "INSERT INTO FMS_SVC_CONT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
//			stmt1 = conn.prepareStatement(queryString1);
			
//			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
			
				
				queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, NULL, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_SVC_CONT_MST WHERE COMPANY_CD = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1,company_cd);
				rset5 = stmt5.executeQuery();
				
				while(rset5.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset5.getString(i+1) == null ? "" : rset5.getString(i+1);
						
						if(i == 7) {	//PLANT_SEQ_NO
							data = plant;
						}
						stmt1.setString(i+1,data);
					}
					
					//for data already exists..
					queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_SVC_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ?  ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, company_cd);
					stmt2.setString(2, rset5.getString(2));
					stmt2.setString(3, rset5.getString(3));
					stmt2.setString(4, rset5.getString(4));
					stmt2.setString(5, rset5.getString(5));
					stmt2.setString(6, rset5.getString(6));
					stmt2.setString(7, rset5.getString(7));
					stmt2.setString(8, plant);
					
					rset2 = stmt2.executeQuery();
					
					if (!rset2.next() ) {
						
						//logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						//logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					stmt2.close();
					rset2.close();
					
				}
				stmt5.close();
				rset5.close();
			
			
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_SVC_CONT_BU>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SVC_CONT_BU>>,", conn);
			
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
		}
	}
	

	public void FMS_SVC_CONT_MST_UPDATE() throws IOException, SQLException {
		
		function_nm="FMS_SVC_CONT_MST_UPDATE()";
		try {
			
			
			System.out.println("<<START>><<FMS_SVC_CONT_MST_UPDATE>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SVC_CONT_MST_UPDATE>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String plant="1";
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString1 = "UPDATE FMS_SVC_CONT_MST SET CONT_STATUS = ? WHERE "
					+ "COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
//			stmt1 = conn.prepareStatement(queryString1);
			
//			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
			
				
				queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, PLANT_SEQ_NO FROM FMS_SVC_CONT_MST WHERE COMPANY_CD = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1,company_cd);
				rset5 = stmt5.executeQuery();
				
				while(rset5.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					cd = rset5.getString(2);
					no = rset5.getString(3);
					rev = rset5.getString(4);
					cont_no = rset5.getString(5);
					cont_rev = rset5.getString(6);
					cont_type = rset5.getString(7);
					plant = rset5.getString(8);
					
					//for data already exists..
					queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_SVC_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ?  ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, company_cd);
					stmt2.setString(2, cd);
					stmt2.setString(3, no);
					stmt2.setString(4, rev);
					stmt2.setString(5, cont_no);
					stmt2.setString(6, cont_rev);
					stmt2.setString(7, cont_type);
					stmt2.setString(8, plant);
					
					rset2 = stmt2.executeQuery();
					
					if (rset2.next() ) {
						
						//logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
						stmt1.setString(1, "Y");
						stmt1.setString(2, company_cd);
						stmt1.setString(3, cd);
						stmt1.setString(4, no);
						stmt1.setString(5, rev);
						stmt1.setString(6, cont_no);
						stmt1.setString(7, cont_rev);
						stmt1.setString(8, cont_type);
						stmt1.setString(9, plant);
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						//logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					stmt2.close();
					rset2.close();
					
				}
				stmt5.close();
				rset5.close();
			
			
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_SVC_CONT_MST_UPDATE>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SVC_CONT_MST_UPDATE>>,", conn);
			
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
		}
	}
	
	
	//FMS_SUPPLY_CONT_MST(DLNG)
	public void FMS_SUPPLY_CONT_MST() throws IOException, SQLException {

		function_nm="FMS_SUPPLY_CONT_MST(DLNG)()";
		try {
			
			table_name = "FMS_SUPPLY_CONT_MST(DLNG)";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  
			String abbr1 = "";

			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
				    + "CONTRACT_TYPE,CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,"
				    + "TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,"
				    + "BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,"
				    + "DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,"
				    + "MODIFY_DT,MODIFY_BY,CONT_STATUS,IS_ALLOCATED,DDA_DT,DDA_TIME,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,TXN_CHARGE,BUYER_NOM_CUTOFF,"
				    + "TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_CLOSE,TCQ_REQUEST_QTY,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,CHANGE_DATE_REQ,"
				    + "MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,"
				    + "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,"
				    + "TERMINATE_PLANED,TERMINATE_FORCE,SF_GEN_DT,CLOSURE_REQUEST_FLAG,CLOSE_EFF_DT,CLOSURE_ALLOC_QTY,CLOSURE_REMARK,ADV_ADJUST) VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				    + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				    + "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,"
				    + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
				    + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_MST(DLNG)_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_MST(DLNG)_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					no = ""; rev = "";
					abbr = "";
					cd = "0";
					int num=0;
					int max=0;

					data = null;
					String cont_name="",a_base="",a_type="",spec_gas="";
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
				    		abbr1 = abbr.split("/")[0];
//				    		System.out.println(""+abbr1);
				    		cont_no = abbr.split("/")[1];
//				    		System.out.println(""+cont_no);
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
//				    		System.out.println(":: "+cont_ref);
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		
				    		stmt.setString(1, abbr1);
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
				    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (no != null && (cont_ref.startsWith("L"))) {
								no = "0";
							} 
							if (no != null && !no.equals("0")) {
								no = no.substring(1, no.length() - 1);
							}				    						    		
							data = no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (rev != null && (cont_ref.startsWith("L"))) {
				    			rev = "0";
							}
				    		else if (rev != null) {
								rev = rev.substring(1, rev.length() - 1);
							}				    		
							data = rev;
				    	}
			    		
				    	else if(cell.getColumnIndex() == 4) {//CONT_NO
							max = Integer.parseInt(cont_no);
//							System.out.println("max:: "+max);
							
							queryString2 = "SELECT CONT_NO FROM FMS_SUPPLY_CONT_MST WHERE REMARK LIKE ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							if(cont_ref.startsWith("L")) {
							stmt2.setString(1, ("%@"+cont_ref.split("-")[0] + "-" + cont_ref.split("-")[1] + "-" + cont_ref.split("-")[2] + "-" ) + "%");
				    	   }else {
							stmt2.setString(1, ("%@"+cont_ref.split("-")[0] + "-" + cont_ref.split("-")[1] + "-" + cont_ref.split("-")[2] + "-" + cont_ref.split("-")[3] + "-") + "%");
				    		
				    	}
							stmt2.setString(2, cd);
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								cont_no = rset2.getString(1);

//					    		System.out.println(cont_ref+"***"+cont_no);
							}
							else {
					    		
				    			queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND CONT_NO LIKE ?  AND CONTRACT_TYPE IN ('E','F')";
					    		stmt = conn.prepareStatement(queryString);	
					    		stmt.setString(1, max+"%");
					    		rset = stmt.executeQuery();
					    		if (rset.next() && rset.getInt(1) > 0) {
					    			max = rset.getInt(1);
//						    		System.out.println(cont_ref+"==="+max);
					    				
					    		}else{				    		
					    			max = max * 10000;
					    			max++;
//						    		System.out.println(cont_no+"__"+cont_ref+"---"+max);
					    		}
					    		rset.close();
					    		stmt.close();
					    		cont_no = max + "";
							}
							num = Integer.parseInt(cont_no);
							data = cont_no;
//							System.out.println("cont-no:: "+data);
							
							rset2.close();
							stmt2.close();
						}
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (cont_rev != null) {
				    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
							}	
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //Cont_type
				    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (cont_type != null) {
				    			cont_type = cont_type.substring(1, cont_type.length() - 1);
							}	
				    		data = cont_type;
				    	}
				    	
				    	
				    	else if(cell.getColumnIndex() == 14) {
//				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			a_base = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    			if(a_base!=null) {
				    				a_base = a_base.substring(1, a_base.length() - 1);
				    			}
//				    		}else 
				    			if(cont_type.equals("F") && a_base == null){
					    		queryString = "SELECT AGMT_BASE,AGMT_TYP FROM FMS_AGMT_MST WHERE COUNTERPARTY_CD = ? AND AGMT_TYPE = 'D' AND AGMT_NO = ? AND AGMT_REV = ?";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, no);
					    		stmt.setString(3, rev);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			a_base= rset.getString(1);
					    			a_typ= rset.getString(2);
					    		} else {
					    			a_base= null;
					    			a_typ= null;
					    		}
					    		rset.close();
					    		stmt.close();
					    		
				    		}
				    		data = a_base;
				    	}
				    	else if(cell.getColumnIndex() == 15) {
				    		if(cont_ref.startsWith("L")) {
				    			a_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    			if (a_type!=null) {
									a_type = a_type.substring(1, a_type.length() - 1);
								}
				    		}
				    		else if(cont_type.equals("F")){
				    			a_type = a_typ;
				    		}
				    		data = a_type;
				    	}
				    	else if(cell.getColumnIndex() == 71) {
				    		spec_gas = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (spec_gas!=null) {
								spec_gas = spec_gas.substring(1, spec_gas.length() - 1);
							}
							spec_gas = ("0".equals(spec_gas)) ? "0" : (("1".equals(spec_gas)) ? "GCV" : (("2".equals(spec_gas)) ? "NCV" : null));
				    		data = spec_gas;
				    	}
				    					    					  				    	
				    	else {				    	
				    						    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);					    	
					    	}
				    	}				
//				    	System.out.println(index+"==="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?  ";
			    			if(cont_type.equals("F")) {
			    				queryString += "AND CONT_NO = ? ";
			    			}else {
			    				queryString += "AND REMARK LIKE ? ";
			    			}
			    			
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, no);
			    	stmt.setString(4, rev);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, cont_type);			   

			    	if(cont_type.equals("F")) {
				    	stmt.setInt(8, num);
	    			}else {
	    				stmt.setString(8, "%@"+cont_ref);
	    			}
			    	rset = stmt.executeQuery();
			    	
				    if (!rset.next() && !cd.equals("") ) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + "," + no + "," + rev + "," + no + ","+ cont_rev + ","+ cont_type + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + "," + no + "," + rev + "," + no + ","+ cont_rev + ","+ cont_type + ","), conn, "E");
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
			conn.commit();
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
	
//FMS_SUPPLY_CONT_BU()	
	public void FMS_SUPPLY_CONT_BU() throws IOException, SQLException {

		function_nm="FMS_SUPPLY_CONT_BU(DLNG)()";
		try {
			
			
			System.out.println("<<START>><<FMS_SUPPLY_CONT_BU(DLNG)>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SUPPLY_CONT_BU(DLNG)>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, '1', ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ?  AND CONTRACT_TYPE IN('E','F')";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
//			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
			
			while (rset.next()) {
				
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ?  ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(3));
				stmt2.setString(4, rset.getString(4));
				stmt2.setString(5, rset.getString(5));
				stmt2.setString(6, rset.getString(6));
				stmt2.setString(7, rset.getString(7));
				stmt2.setString(8, rset.getString(8));
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
//					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
//					skipped_count++; 
					
//					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				}
	
			
			rset.close();
			stmt.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_SUPPLY_CONT_BU(DLNG)>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_CONT_BU(DLNG)>>,", conn);
			
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
		}
		
	}
	
	
//FMS_SUPPLY_CONT_FILLING_STN
public void FMS_SUPPLY_CONT_FILLING_STN() throws IOException, SQLException {
		
		function_nm="FMS_SUPPLY_CONT_FILLING_STN()";
		try {
			
			System.out.println("<<START>><<FMS_SUPPLY_CONT_FILLING_STN>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SUPPLY_CONT_FILLING_STN>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String no = "",type = "",rev = "",fill_cd="";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
//			int max_cd = 1;
			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_FILLING_STN(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,FILL_STATION_CD,ENT_BY,ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_FILLING_STN_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_FILLING_STN_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,FILL_STATION_CD,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					data = "";
					cd = ""; eff_dt = "";
					no = "";type = "";rev = "";fill_cd="";
					
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
				    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (no != null) {
								no = no.substring(1, no.length() - 1);
							}
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE IN ('F','E') ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, "%@"+cont_ref);
					    		
					    		
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
//				    		}
				    		
							data = no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		if(!cont_ref.startsWith("L")) {
					    		rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (rev != null) {
									rev = rev.substring(1, rev.length() - 1);
								}
				    		}
							data = rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		if(!cont_ref.startsWith("L")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		if(!cont_ref.startsWith("L")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
				    		}
				    		data = cont_type;
				    	}

				    	else {
				    		if(cell.getColumnIndex() == 7) {
							fill_cd = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
							if (fill_cd != null) {
								fill_cd = fill_cd.substring(1, fill_cd.length() - 1);
							}	
				    	}
						
						data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						if(data != null) {
							data = data.substring(1, data.length()-1);
						}
					}
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_FILLING_STN WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND FILL_STATION_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, no);
					stmt.setString(4, rev);
					stmt.setString(5, cont_no);
					stmt.setString(6, cont_rev);
					stmt.setString(7, cont_type);
					stmt.setString(8, fill_cd);
					
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("") && !no.equals("") &&!rev.equals("") && !cont_no.equals("") && !cont_rev.equals("") && !cont_type.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd + "," + cd +  "," + no + "," + rev + "," +cont_no+","+cont_rev+","+cont_type+","+ fill_cd + "," ), conn, " ");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + cd +  "," + no + "," + rev + "," +cont_no+","+cont_rev+","+cont_type+","+ fill_cd + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_SUPPLY_CONT_FILLING_STN>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_CONT_FILLING_STN>>,,", conn);
			
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
	
//FMS_SUPPLY_CONT_PLANT(DLNG)
	public void FMS_SUPPLY_CONT_PLANT() throws IOException, SQLException {

		function_nm="FMS_SUPPLY_CONT_PLANT(DLNG)()";
		try {
			
			table_name = "FMS_SUPPLY_CONT_PLANT(DLNG)";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "PLANT_SEQ_NO,ENT_BY,ENT_DT,TRANSPORTATION_CHARGE,MARKET_MARGIN,OTHER_CHARGES) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT(DLNG)_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT(DLNG)_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,COUNTERPARTY_ABBR,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					no = ""; rev = ""; 
					String seq_no = "";
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
				    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (no != null) {
								no = no.substring(1, no.length() - 1);
							}
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE IN ('F','E') ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, "%@"+cont_ref);
					    		
					    		
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
//				    		}
				    		
							data = no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		if(!cont_ref.startsWith("L")) {
					    		rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (rev != null) {
									rev = rev.substring(1, rev.length() - 1);
								}
				    		}
							data = rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		if(!cont_ref.startsWith("L")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		if(!cont_ref.startsWith("L")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
				    		}
				    		data = cont_type;
				    	}
						
				    	else {
				    	
				    		if (cell.getColumnIndex() == 7) {	// PLANT_SEQ_NO
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
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
			    			+ "AND AGMT_NO = ? AND AGMT_REV = ? AND  PLANT_SEQ_NO = ?  AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, no);
			    	stmt.setString(4, rev);
			    	stmt.setString(5, seq_no);
			    	stmt.setString(6, cont_no);
			    	stmt.setString(7, cont_rev);
			    	stmt.setString(8, cont_type);
			    	rset = stmt.executeQuery();
			    	

					if (!rset.next() && !cd.equals("") && !no.equals("") &&!rev.equals("") && !cont_no.equals("") && !cont_rev.equals("") && !cont_type.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + " , " + abbr + "," + cont_type + "," + no + "," + rev + "," + seq_no + "," + cont_no + ","+ cont_rev + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + " , " + abbr + "," + cont_type + "," + no + "," + rev + "," + seq_no + "," + cont_no + ","+ cont_rev + ","), conn, "E");
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
	
//FMS_SUPPLY_CONT_LIABILITY
public void FMS_SUPPLY_CONT_LIABILITY() throws IOException, SQLException {
		
		function_nm="FMS_SUPPLY_CONT_LIABILITY(DLNG)()";
		try {
			
			table_name = "FMS_SUPPLY_CONT_LIABILITY(DLNG)";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String ld_promise = "",top_promise="";
			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,"
					+ "CONT_REV,LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_FROM,LQ_DAMG_TO,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "	LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_FROM,TAKE_PAY_TO,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,"
					+ "	TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,"
					+ "	MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT) VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_LIABILITY(DLNG)_"+start_end_dt+".xlsx");
			
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_LIABILITY(DLNG)_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++; 
					no = ""; rev = ""; seq_no = ""; type = "D";
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
						
				    	if (cell.getColumnIndex() == 0) {
				    		
				    		abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (abbr != null) {
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
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	} else if(cell.getColumnIndex() == 2) {
				    		data = type;
				    		
				    	}
				    	else if(cell.getColumnIndex() == 3) {
				    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (no != null) {
								no = no.substring(1, no.length() - 1);
							}
//				    		if(cont_ref.startsWith("L")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE IN('F','E') ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, "%@"+cont_ref);
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
//				    		}
				    		
							data = no;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Agmt_rev
							data = rev;
				    	}
				    	else if (cell.getColumnIndex() == 5) { //contract_type
				    		data = cont_type;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //Cont_no
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 7) { //Cont_rev
				    		data = cont_rev;
				    	}
				    					    	
				    	else if(cell.getColumnIndex() == 10) {
				    		ld_promise = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!ld_promise.equals("NULL")) {
								ld_promise = ld_promise.substring(1, ld_promise.length() - 1);
							}
				    		
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
				    			ld_promise = null;
				    		}
				    		
				    		data = ld_promise;
				    	}
				    	else if(cell.getColumnIndex() == 18) {
				    		top_promise = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!top_promise.equals("NULL")) {
								top_promise = top_promise.substring(1, top_promise.length() - 1);
							}
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
				    			top_promise = null;
				    		}
				    		
				    		data = top_promise;
				    	}
				    	
				    	else {
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}	
				    	stmt1.setString(index++, data);		    	
				    }
					
				    queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_LIABILITY WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, no);
			    	stmt.setString(4, rev);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, cont_type);
			    	stmt.setString(8, type);
		    	
			    	rset = stmt.executeQuery();
			    	
			    	 if (!rset.next() && !cd.equals("") && !no.equals("") &&!rev.equals("") &&!cont_no.equals("") &&!cont_rev.equals("") &&!cont_type.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + cd + " , " + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
					}
			    	 else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+"," + cd + " , " + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + ","), conn, "E");
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
			
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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

//FMS_SUPPLY_BILLING_DTL_(DLNG)
		public void FMS_SUPPLY_BILLING_DTL() throws IOException, SQLException {

			function_nm="FMS_SUPPLY_BILLING_DTL(DLNG)()";
			try {
				
				table_name = "FMS_SUPPLY_BILLING_DTL(DLNG)";
				System.out.println("<<START>><<"+table_name+">>");
				
				data = "";
				
				logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				
				data = "";
				logger_count = 0;
				skipped_count = 0;   
				total_count = 0;  
				String state_code,exchg_cd,name,seq_no;

				
				queryString1 = "INSERT INTO FMS_SUPPLY_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
						+ "BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,"
						+ "EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,"
						+ "BILLING_DAYS,EFF_DT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
						+ "TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?)";
						
				stmt1 = conn.prepareStatement(queryString1);
				
				
				file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_BILLING_DTL(DLNG)_"+start_end_dt+".xlsx");
				if(file1.exists()) {

					
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_BILLING_DTL(DLNG)_"+start_end_dt+".xlsx"));

					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					
					// Below block of code is for unique SEIPL data
					rowIterator = sheet.iterator();
					if (rowIterator.hasNext()) {	// For skipping the first row
						rowIterator.next();
					}

					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_TYPE,EFF_DT,PLANT_SEQ_NO,TIMESTAMP,", conn);
					
					while (rowIterator.hasNext()) {
						total_count++;  
						
						no = ""; rev = "";cont_type="";state_code="";exchg_cd="";
						seq_no="";
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
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
					    	else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (no != null) {
									no = no.substring(1, no.length() - 1);
								}
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE IN ('F','E') ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, "%@"+cont_ref);
						    		
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			no = rset.getString(1);
						    			rev = "0";
						    			cont_no = rset.getString(3);
						    			cont_rev = "0";
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
//					    		}
					    		
								data = no;
					    	}
					    	
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
//					    		if(!cont_ref.startsWith("L")) {
//						    		rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (rev != null) {
//										rev = rev.substring(1, rev.length() - 1);
//									}
//					    		}
								data = rev;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
//					    		if(!cont_ref.startsWith("L")) {
//						    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (cont_no != null) {
//						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
//									}
//					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
//					    		if(!cont_ref.startsWith("L") ) {
//						    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (cont_rev != null) {
//						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//									}	
//					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 6) { //contract_type
//					    		if(!cont_ref.startsWith("L")) {
//						    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (cont_type != null) {
//						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
//									}	
//					    		}
					    		data = cont_type;
					    	}
					    	else if(cell.getColumnIndex() == 8) {
					    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    		if (name != null) {
									name = name.substring(1, name.length() - 1);
								}
					    		
					    		if(name.equals("T")) {
					    			name = "B";
					    		}else if(name.equals("B")) {
					    			name = "B";
					    		}else if(name.equals("Y")) {
					    			name = null;
					    		}
					    		data = name;
					    	}
					    	else if(cell.getColumnIndex() == 13) {
					    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    		if (name != null) {
									name = name.substring(1, name.length() - 1);
								}
								queryString = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) LIKE ? ";
						    	stmt = conn.prepareStatement(queryString);
						    	stmt.setString(1, "%"+name+"%");
						    	rset = stmt.executeQuery();
						    	if (rset.next()) {
						    		exchg_cd = rset.getString(1);
						    	}else {
						    		exchg_cd =  null;
						    	}
						    	rset.close();
						    	stmt.close();
						    	data = exchg_cd;
					    	}
					    	else if(cell.getColumnIndex() == 16) {
					    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    		if (name!=null) {
									name = name.substring(1, name.length() - 1);
								}
								queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) LIKE ? ";
						    	stmt = conn.prepareStatement(queryString);
						    	stmt.setString(1, "%"+name+"%");
						    	rset = stmt.executeQuery();
						    	if (rset.next()) {
						    		exchg_cd = rset.getString(1);
						    	}
						    	rset.close();
						    	stmt.close();
						    	data = exchg_cd;
					    	}
					    	
					    	else if (cell.getColumnIndex() == 31) { //PLANT_SEQ_NO
					    		
//					    		queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_PLANT WHERE COUNTERPARTY_CD = ? "
//					    				+ "AND CONT_NO=? AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
//					    		stmt = conn.prepareStatement(queryString);
//					    		stmt.setString(1, cd);
//					    		stmt.setString(2, cont_no);
//					    		stmt.setString(3, cont_rev);
//					    		stmt.setString(4, no);
//					    		stmt.setString(5, rev);
//					    		stmt.setString(6, cont_type);
//					    		
//					    		rset = stmt.executeQuery();
//					    		if (rset.next()) {
//					    			seq_no= rset.getString(1);
//					    		} else {
//					    			seq_no = "";
//					    		}
//					    		rset.close();
//					    		stmt.close();
					    		seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    		if (seq_no!=null) {
					    			seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
					    		data = seq_no;
					    		
					    	}
					    	else if (cell.getColumnIndex() == 32) { //holiday_state
						    		queryString = "SELECT B.TIN FROM  FMS_COUNTERPARTY_PLANT_DTL A,FMS_STATE_MST B WHERE A.COUNTERPARTY_CD = ? AND SEQ_NO = ? AND B.STATE_NM = A.PLANT_STATE ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, seq_no);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			state_code=rset.getString(1);
						    		}
						    		else
						    		{
						    			state_code=null;
						    		}
						    		rset.close();
						    		stmt.close();
						    		data = state_code;
					    	}
					    	
				    
					    	else {	
					    		if(cell.getColumnIndex() == 28) {
					    			eff_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    			if (eff_dt != null) {
					    				eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
									}
					    		}
					    		
						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
					    	}
//					    	System.out.println(":"+index+":"+data);
					    	stmt1.setString(index++, data);
					    
					    }
					     
				    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? "
//				    			+ "AND AGMT_REV = ? "
				    			+ "AND CONT_NO = ? "
//				    			+ "AND CONT_REV = ? "
				    			+ "AND CONTRACT_TYPE = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND PLANT_SEQ_NO = ? ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, company_cd);
				    	stmt.setString(2, cd);
				    	stmt.setString(3, no);
//				    	stmt.setString(4, rev); 
				    	stmt.setString(4, cont_no);
//				    	stmt.setString(6, cont_rev);
				    	stmt.setString(5,cont_type );
				    	stmt.setString(6,eff_dt);
				    	stmt.setString(7,seq_no);
				    	rset = stmt.executeQuery();
				    	
							if (!rset.next() && !cd.equals("") && !no.equals("") &&!rev.equals("") && !cont_no.equals("") && !cont_rev.equals("") && !cont_type.equals("") && eff_dt!=null) {

							logger.data(fname, (company_cd+"," + cd + "," +no+","+ rev + "," + cont_no + ","+cont_rev+","+cont_type+","+eff_dt+","+seq_no ), conn, "");
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+"," + cd + "," +no+","+ rev + "," + cont_no + ","+cont_rev+","+cont_type+","+eff_dt+","+seq_no ), conn, "E");
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
				conn.commit();
				System.out.println("<<END>><<"+table_name+">>");
				System.out.println();

				logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,"), conn); 

				logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,"), conn); 
				
				logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,"), conn); 
				
				logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,", conn);
				
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
		
		
//FMS_SUPPLY_CFORM_DTL
	public void FMS_SUPPLY_CFORM_DTL() throws IOException, SQLException {

		function_nm="FMS_SUPPLY_CFORM_DTL(DLNG)()";
		try {
			
			table_name = "FMS_SUPPLY_CFORM_DTL(DLNG)";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  
			String state_code,exchg_cd,name,seq_no,bu_seq,comd_type;

			
			queryString1 = "INSERT INTO FMS_SUPPLY_CFORM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "PLANT_SEQ,BU_SEQ,COMMODITY_TYPE,CFORM_FLAG,EFF_DT,ENT_DT,ENT_BY,MOD_DT,MOD_BY)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
					
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CFORM_DTL(DLNG)_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CFORM_DTL(DLNG)_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_TYPE,PLANT_SEQ,EFF_DT,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					no = ""; rev = "";cont_type="";state_code="";exchg_cd="";bu_seq="";comd_type="";
					seq_no="";
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
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (no != null) {
								no = no.substring(1, no.length() - 1);
							}
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE IN ('F','E') ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, "%@"+cont_ref);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			no = rset.getString(1);
					    			rev = "0";
					    			cont_no = rset.getString(3);
					    			cont_rev = "0";
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
				    	
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
							data = rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		data = cont_type;
				    	}
//				    	else if(cell.getColumnIndex() == 7) {
//				    		queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_PLANT WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ?"
//				    				+ "AND CONT_REV = ? AND CONTRACT_TYPE = ?";
//				    		stmt = conn.prepareStatement(queryString);
//				    		stmt.setString(1, cd);
//				    		stmt.setString(2, no);
//				    		stmt.setString(3, rev);
//				    		stmt.setString(4, cont_no);
//				    		stmt.setString(5, cont_rev);
//				    		stmt.setString(6, cont_type);
//				    		rset = stmt.executeQuery();
//				    		if(rset.next()) {
//				    			seq_no = rset.getString(1);
//				    		}
//				    		rset.close();
//				    		stmt.close();
//				    		data = seq_no;
//				    		
//				    	}
				    	else if(cell.getColumnIndex() == 7) {
				    		seq_no =  (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!seq_no.equals("NULL")) {
				    			seq_no = seq_no.substring(1, seq_no.length() - 1);
							}
				    		data = seq_no;
				    	}
                        else if(cell.getColumnIndex() == 8) {
                        	queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ?"
				    				+ "AND CONT_REV = ? AND CONTRACT_TYPE = ?";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cd);
				    		stmt.setString(2, no);
				    		stmt.setString(3, rev);
				    		stmt.setString(4, cont_no);
				    		stmt.setString(5, cont_rev);
				    		stmt.setString(6, cont_type);
				    		rset = stmt.executeQuery();
				    		if(rset.next()) {
				    			bu_seq = rset.getString(1);
				    		}
				    		else {
				    			bu_seq= "1";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = bu_seq;
				    	}
                        else if(cell.getColumnIndex() == 9) {
                        	comd_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (comd_type != null) {
				    			comd_type = comd_type.substring(1, comd_type.length() - 1);
							}
				    		data = comd_type;
				    	}
			    
                        else if(cell.getColumnIndex() == 11) {
                        	eff_dt = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
                        	if (eff_dt != null) {
                        		eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
                        	}
                        	data = eff_dt;
                        }
				    	
				    	else {	
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
//					    	System.out.println(":"+index+":"+data);
				    	stmt1.setString(index++, data);
				    
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CFORM_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? "
//			    			+ "AND AGMT_REV = ? "
			    			+ "AND CONT_NO = ? "
//			    			+ "AND CONT_REV = ? "
			    			+ "AND PLANT_SEQ = ? AND BU_SEQ = ? AND COMMODITY_TYPE = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND CONTRACT_TYPE = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, no);
//			    	stmt.setString(4, rev); 
			    	stmt.setString(4, cont_no);
//			    	stmt.setString(6, cont_rev);
			    	stmt.setString(5,seq_no);
			    	stmt.setString(6,bu_seq);
			    	stmt.setString(7,comd_type);
			    	stmt.setString(8, eff_dt);
			    	stmt.setString(9, cont_type);

			    	rset = stmt.executeQuery();
			    	
						if (!rset.next() && !cd.equals("") && !no.equals("") &&!rev.equals("") && !cont_no.equals("") && !cont_rev.equals("") && !cont_type.equals("") && bu_seq!=null && eff_dt!=null) {

						logger.data(fname, (company_cd+"," + cd + "," +no+","+ rev + "," + cont_no + ","+cont_rev+","+cont_type+","+seq_no+","+eff_dt+"," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + "," +no+","+ rev + "," + cont_no + ","+cont_rev+","+cont_type+","+seq_no+","+eff_dt+"," ), conn, "E");
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
			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,", conn);
			
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
		
		
	public void FMS_CFORM_MST() throws IOException, SQLException {
		
		function_nm="FMS_CFORM_MST()";
		try {
			
			table_name = "FMS_CFORM_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  
			String state_code,cform_no,cform_cd,cform_amt,ent_dt;
			
			
			queryString1 = "INSERT INTO FMS_CFORM_MST(COMPANY_CD,CFORM_CD,CFORM_NO,CFORM_DT,COUNTERPARTY_CD,FINANCIAL_YEAR,ISSUING_STATE,PERIOD_FROM,PERIOD_TO,CFORM_AMOUNT,CFORM_FILE,"
					+ "NO_OF_INVOICES,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT)"
					+ " VALUES (?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CFORM_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CFORM_MST_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,CFORM_CD,CFORM_NO,ISSUING_STATE,CFORM_AMOUNT,ENT_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext()) {
					total_count++;  
					
					state_code="";cform_no="";cform_amt="";ent_dt="";cform_cd="";
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
						
						if (cell.getColumnIndex() == 0) {	// Company_cd
							data = company_cd;
						}
						
						else if(cell.getColumnIndex() == 1) {	//CFORM_CD
							cform_cd = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
							if (!cform_cd.equals("NULL")) {
								cform_cd = cform_cd.substring(1, cform_cd.length() - 1);
							}
							data = cform_cd;
						}
						
						else if(cell.getColumnIndex() == 2) {	//CFORM_NO
							cform_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
							if (!cform_no.equals("NULL")) {
								cform_no = cform_no.substring(1, cform_no.length() - 1);
							}
							data = cform_no;
						}
						else if (cell.getColumnIndex() == 4) {	// Counterparty_Cd
							
							abbr = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
							if (!abbr.equals("NULL")) {
								abbr = abbr.substring(1, abbr.length() - 1);
							}
							
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
							data = cd;
						}
						
						else if (cell.getColumnIndex() == 6) {	// ISSUING_STATE
							
							state_code = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
							if (!state_code.equals("NULL")) {
								state_code = state_code.substring(1, state_code.length() - 1);
							}
							
							queryString = "SELECT TIN FROM FMS_STATE_MST WHERE UPPER(STATE_NM) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, state_code);
							rset = stmt.executeQuery();
							if (rset.next()) {
								state_code = rset.getString(1);
							} else {
								state_code = "";
							}
							rset.close();
							stmt.close();
							data = state_code;
						}
						
						else {	
							
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
						}
//					    	System.out.println(":"+index+":"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_CFORM_MST WHERE COMPANY_CD = ? AND CFORM_NO = ? AND CFORM_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cform_no);
					stmt.setString(3, cform_cd);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("") && !cform_no.equals("")) {
						
						logger.data(fname, (company_cd+"," + cd + ","+cform_cd+"," +cform_no+","+ state_code + "," + cform_amt + ","+ent_dt+"," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + ","+cform_cd+"," +cform_no+","+ state_code + "," + cform_amt + ","+ent_dt+"," ), conn, "E");
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
			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,", conn);
			
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
	
	
	public void FMS_CFORM_DTL() throws IOException, SQLException {
		
		function_nm="FMS_CFORM_DTL()";
		try {
			
			table_name = "FMS_CFORM_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  
			String state_code,cform_no,cform_cd,inv_no,inv_dt,ent_dt;
			
			
			queryString1 = "INSERT INTO FMS_CFORM_DTL(COMPANY_CD,CFORM_CD,INVOICE_NO,INVOICE_DT,INVOICE_AMOUNT,COMMODITY_TYPE,ENT_BY,ENT_DT)"
					+ " VALUES (?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CFORM_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CFORM_DTL_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,CFORM_NO,CFORM_CD,INVOICE_NO,INVOICE_DT,TIMESTAMP,", conn);
				while (rowIterator.hasNext()) {
					total_count++;  
					
					state_code="";cform_no="";cform_cd="";inv_no="";inv_dt="";ent_dt="";
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
						
						if (cell.getColumnIndex() == 0) {	// Company_cd
							data = company_cd;
						}
						
						else if(cell.getColumnIndex() == 1) {	//CFORM_CD
							cform_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
							if (!cform_no.equals("NULL")) {
								cform_no = cform_no.substring(1, cform_no.length() - 1);
							}
							queryString = "SELECT CFORM_CD FROM FMS_CFORM_MST WHERE CFORM_NO = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cform_no);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cform_cd = rset.getString(1);
							}
							else {
								cform_cd = "0";
							}
							rset.close();
							stmt.close();
							data = cform_cd;
						}
						
						else if(cell.getColumnIndex() == 2) {	//INVOICE_NO
							inv_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
							if (!inv_no.equals("NULL")) {
								inv_no = inv_no.substring(1, inv_no.length() - 1);
							}
							data = inv_no;
						}
						
						else if(cell.getColumnIndex() == 3) {	//INVOICE_DT
							inv_dt = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
							if (!inv_dt.equals("NULL")) {
								inv_dt = inv_dt.substring(1, inv_dt.length() - 1);
							}
							data = inv_dt;
						}
						
						else {	
							
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
						}
//					    	System.out.println(":"+index+":"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_CFORM_DTL WHERE COMPANY_CD = ? AND CFORM_CD = ? AND INVOICE_NO = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cform_cd);
					stmt.setString(3, inv_no);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("") && !cform_cd.equals("") && !inv_no.equals("")) {
						
						logger.data(fname, (company_cd+ ","+cform_no +","+cform_cd + "," + inv_no + ","+inv_dt+"," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+ ","+cform_no +","+cform_cd + "," + inv_no + ","+inv_dt+"," ), conn, "E");
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
			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,", conn);
			
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
	
	
//FMS_SUPPLY_CONT_TRUCK_TRANS
		public void FMS_SUPPLY_CONT_TRUCK_TRANS() throws IOException, SQLException {
			
			function_nm="FMS_SUPPLY_CONT_TRUCK_TRANS()";
			try {
				
				System.out.println("<<START>><<FMS_SUPPLY_CONT_TRUCK_TRANS>>");
				
				logger.checkpoint(fname, "\n<<START>>,<<FMS_SUPPLY_CONT_TRUCK_TRANS>>,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				data = "";
				String trans_cd="";
				logger_count = 0;   
				skipped_count = 0;   
				total_count = 0;  
//				int max_cd = 1;
				
				queryString1 = "INSERT INTO FMS_SUPPLY_CONT_TRUCK_TRANS(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRANSPORTER_CD,ENT_BY,ENT_DT) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				
				stmt1 = conn.prepareStatement(queryString1);
				
				
				file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_TRUCK_TRANS_"+start_end_dt+".xlsx");
				if(file1.exists()) {
					
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_TRUCK_TRANS_"+start_end_dt+".xlsx"));
					
					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					
					// Below block of code is for inserting SEIPL data
					rowIterator = sheet.iterator();
					rowIterator.next();
					
					
					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,TIMESTAMP,", conn);
					
					while (rowIterator.hasNext()) {
						total_count++;  
						
						data = "";
						cd = ""; eff_dt = "";
						no = "";type = "";rev = "";trans_cd="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							data = null;
							
							if(cell.getColumnIndex() == 0) {	//COMPANY_CD
								abbr = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
					    		if (!abbr.equals("NULL")) {
									abbr = abbr.substring(1, abbr.length() - 1);

								}
								data =company_cd;
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
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
					    	else if (cell.getColumnIndex() == 2) { //Agmt_no
					    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (no != null) {
									no = no.substring(1, no.length() - 1);
								}
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE IN ('F','E') ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, "%@"+cont_ref);
						    		
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
//					    		}
					    		
								data = no;
					    	}
							else if(cell.getColumnIndex() == 3) {
								data = rev;
							}
							else if(cell.getColumnIndex() == 4) {
								data=cont_no;
							}
							else if(cell.getColumnIndex() == 5) {
								data = cont_rev;
							}
							else if(cell.getColumnIndex() == 6) {
								data = cont_type;
							}
							else if (cell.getColumnIndex() == 7) {	//TRANSPORTER_CD
								trans_cd = cell.getStringCellValue().contains("null") ? null :cell.getStringCellValue();
								if(trans_cd!=null) {
								trans_cd = trans_cd.substring(1, trans_cd.length()-1);
								trans_cd = trans_cd.toUpperCase();
								}
								
								queryString = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE UPPER(TRUCK_TRANS_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, trans_cd);
								rset = stmt.executeQuery();
								if(rset.next()) {
									trans_cd = rset.getString(1);
								}
								else {
									trans_cd = "0";
								}
								
								rset.close();
								stmt.close();
								data = trans_cd;

							}
							else {
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if(data != null) {
								data = data.substring(1, data.length()-1);
							   }
							}
//							System.out.println(index+">>>"+data);
							stmt1.setString(index++, data);
							
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_TRUCK_TRANS WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND TRANSPORTER_CD = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, no);
						stmt.setString(4, rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, cont_type);
						stmt.setString(8, trans_cd);
						
						
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !no.equals("") &&!rev.equals("") && !cont_no.equals("") && !cont_rev.equals("") && !cont_type.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + cd + ","+ no + "," + rev + ","+cont_no +","+cont_rev+","+cont_type+","+ trans_cd + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + ","+ no + "," + rev + ","+cont_no +","+cont_rev+","+cont_type+","+ trans_cd + "," ), conn, "E");
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
				
				System.out.println("<<END>><<FMS_SUPPLY_CONT_TRUCK_TRANS>>");
				System.out.println();
				
				logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 
				
				logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
				
				logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
				
				logger.checkpoint(fname, "<<END>>,<<FMS_SUPPLY_CONT_TRUCK_TRANS>>,,", conn);
				
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

			function_nm="FMS_SECURITY_DEAL_MAP(DLNG)()";
			try {
				
				table_name = "FMS_SECURITY_DEAL_MAP(DLNG)";
				System.out.println("<<START>><<"+table_name+">>");
				
				logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				data = "";
				logger_count = 0;   
				skipped_count = 0;   
				total_count = 0; 

				
				queryString1 = "INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO, "
						+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?) ";
				stmt1 = conn.prepareStatement(queryString1);
				
				file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP(DLNG)_"+start_end_dt+".xlsx");
				if(file1.exists()) {
					
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP(DLNG)_"+start_end_dt+".xlsx"));

					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					
					// Below block of code is for unique SEIPL data
					rowIterator = sheet.iterator();
					if (rowIterator.hasNext()) {	// For skipping the first row
						rowIterator.next();
					}

					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_ABBR,COUNTERPARTY_CD, SEQ_NO, SEC_REF_NO, MAP_SEQ_NO, SEQ_REV_NO, GX, AGMT_NO,TIMESTAMP,", conn);
					
					while (rowIterator.hasNext()) {
						String map_seq_no="",seq_rev_no="",sec_ref_no="";
						abbr = "";seq_no="";
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
					    		}else {
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
					    	else if(cell.getColumnIndex() == 2) {	//SEQ_NO
					    		queryString = "SELECT MAX(SEQ_NO)+1 FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, company_cd);
					    		stmt.setString(2, cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next() && rset.getString(1)!=null && rset.getInt(1) > 0) {
					    			seq_no = rset.getString(1);
					    		}else {
					    			seq_no = "1";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = seq_no;
					    	}
					    	else if(cell.getColumnIndex() == 3) {	//MAP_SEQ_NO
					    		queryString = "SELECT MAX(MAP_SEQ_NO)+1 FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, company_cd);
					    		stmt.setString(2, cd);
					    		stmt.setString(3, seq_no);
					    		rset = stmt.executeQuery();
					    		if (rset.next() && rset.getString(1)!=null) {
					    			map_seq_no = rset.getString(1);
					    		}else {
					    			map_seq_no = "1";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = map_seq_no;
					    	}
							else if (cell.getColumnIndex() == 5) { //Agmt_no
//					    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//					    		if (no != null) {
//									no = no.substring(1, no.length() - 1);
//								}
//					    		if(cont_ref.startsWith("L")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?  ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, company_cd);
						    		stmt.setString(2, cd);
						    		stmt.setString(3, cont_ref);
						    		
						    		
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
//					    		}
					    		
								data = no;
					    	}
							else if (cell.getColumnIndex() == 6) { //Agmt_rev
//					    		if(!cont_ref.startsWith("L")) {
//						    		rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (rev != null) {
//										rev = rev.substring(1, rev.length() - 1);
//									}
//					    		}
								data = rev;
					    	}
					    	else if (cell.getColumnIndex() == 7) { //Cont_no
//					    		if(!cont_ref.startsWith("L")) {
//						    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (cont_no != null) {
//						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
//									}
//					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 8) { //Cont_rev
//					    		if(!cont_ref.startsWith("L")) {
//						    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (cont_rev != null) {
//						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//									}	
//					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 9) { //contract_type
//					    		if(!cont_ref.startsWith("L")) {
//						    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (cont_type != null) {
//						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
//									}	
//					    		}
					    		data = cont_type;
					    	}
					    	else {
//					    		if (cell.getColumnIndex() == 2) {	// seq_no
//					    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//					    			if (seq_no != null) {
//					    				seq_no = seq_no.substring(1, seq_no.length() - 1);
//									}
//						    	}
//					    		if (cell.getColumnIndex() == 3) {	// map_seq_no
//					    			map_seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//					    			if (map_seq_no != null) {
//					    				map_seq_no = map_seq_no.substring(1, map_seq_no.length() - 1);
//									}
//					    		}
					    		if (cell.getColumnIndex() == 4) {	// sec_ref_no
					    			sec_ref_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    			if (sec_ref_no != null) {
					    				sec_ref_no = sec_ref_no.substring(1, sec_ref_no.length() - 1);
									}
					    		}
					    		if (cell.getColumnIndex() == 16) {	// seq_rev_no
					    			seq_rev_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    			if (seq_rev_no != null) {
					    				seq_rev_no = seq_rev_no.substring(1, seq_rev_no.length() - 1);
									}
					    		}
					    		
						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
					    	}
//					    	System.out.println(index+"-"+data);
					    	stmt1.setString(index++, data);
					    
					    }
//					    COMPANY_CD", "COUNTERPARTY_CD", "SEQ_NO", "MAP_SEQ_NO", "SEQ_REV_NO", "GX
					    queryString = "SELECT COUNTERPARTY_CD FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
//								+ "AND SEQ_NO =? "
								+ " AND  SEQ_REV_NO = ? AND GX = ? AND SEC_REF_NO = ? AND CONTRACT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? ";
						
					    stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
//						stmt.setString(3, seq_no);
//						stmt.setString(4, map_seq_no);
						stmt.setString(3, seq_rev_no);
						stmt.setString(4, "K");
						stmt.setString(5, sec_ref_no);
						stmt.setString(6, cont_type);
						stmt.setString(7, no);
						stmt.setString(8, rev);
						stmt.setString(9, cont_no);
						stmt.setString(10, cont_rev);
						
						rset = stmt.executeQuery();
				    	
					    if (!rset.next() && !cd.equals("") && !no.equals("")) {
							
							logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + sec_ref_no + "," + map_seq_no + "," + seq_rev_no + "," + "K" + "," + no + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + sec_ref_no + "," + map_seq_no + "," + seq_rev_no + "," + "K" + "," + no + ","), conn, "E");
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
			
		}//end of FMS_SECURITY_DEAL_MAP
		
		
		public void FMS_SECURITY_MST() throws IOException, SQLException {

			function_nm="FMS_SECURITY_MST(DLNG)()";
			try {
				
				table_name = "FMS_SECURITY_MST(DLNG)";
				System.out.println("<<START>><<"+table_name+">>");
				
				logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				data = "";
				logger_count = 0;   
				skipped_count = 0;   
				total_count = 0; 

				
				queryString1 = "INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,"
						+ "VARIATION_VALUE,VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,"
						+ "EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,APRV_DT,APRV_BY,SEQ_REV_NO,GX,"
						+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD)"
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,? ) ";
				stmt1 = conn.prepareStatement(queryString1);
			
				
				file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_MST(DLNG)_"+start_end_dt+".xlsx");
				if(file1.exists()) {
					
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECURITY_MST(DLNG)_"+start_end_dt+".xlsx"));

					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					
					// Below block of code is for unique SEIPL data
					rowIterator = sheet.iterator();
					if (rowIterator.hasNext()) {	// For skipping the first row
						rowIterator.next();
					}

					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_ABBR,COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX,TIMESTAMP,", conn);
					
					while (rowIterator.hasNext()) {
					String seq_rev_no="",bank_cd="",g_cd="",map="",sec_ref_no="";
					abbr = "";seq_no="";
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
				    		data = cd;
				    	}
				    	else if(cell.getColumnIndex() == 2) {	//SEQ_NO
				    		sec_ref_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if(sec_ref_no!=null)
				    		{
				    			sec_ref_no = sec_ref_no.substring(1, sec_ref_no.length() - 1);
				    			
				    			if(map.startsWith("S")) {
				    				queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
				    						+ "AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
				    				stmt = conn.prepareStatement(queryString);
				    				stmt.setString(1, company_cd);
				    				stmt.setString(2, cd);
				    				stmt.setString(3, map);
				    				stmt.setString(4, "F");
				    				rset = stmt.executeQuery();
						    		if (rset.next() && rset.getString(1)!=null) {
						    			map = rset.getString(1)+"-"+rset.getString(2)+"-"+rset.getString(3)+"-"+rset.getString(4)+"-"+rset.getString(5);
						    		}
						    		rset.close();
						    		stmt.close();
						    		
				    			queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? AND CONTRACT_TYPE = ? "
				    					+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? "
				    					+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";	
				    			stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, company_cd);
					    		stmt.setString(2, cd);
					    		stmt.setString(3, sec_ref_no);
					    		stmt.setString(4, map.split("-")[0]);
					    		stmt.setString(5, map.split("-")[1]);
					    		stmt.setString(6, map.split("-")[2]);
					    		stmt.setString(7, map.split("-")[3]);
					    		stmt.setString(8, map.split("-")[4]);
					    		stmt.setString(9, company_cd);
					    		stmt.setString(10, cd);
					    		stmt.setString(11, sec_ref_no);
					    		rset = stmt.executeQuery();
					    		if (rset.next() && rset.getString(1)!=null) {
					    			seq_no = rset.getString(1);
					    		}else {
					    			seq_no = "0";
					    		}
					    		rset.close();
					    		stmt.close();
				    			}
				    			else if(map.startsWith("L")) {
				    				queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
				    						+ "AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
				    				stmt = conn.prepareStatement(queryString);
				    				stmt.setString(1, company_cd);
				    				stmt.setString(2, cd);
				    				stmt.setString(3, map);
				    				stmt.setString(4, "E");
				    				rset = stmt.executeQuery();
						    		if (rset.next() && rset.getString(1)!=null) {
						    			map = rset.getString(1)+"-"+rset.getString(2)+"-"+rset.getString(3)+"-"+rset.getString(4)+"-"+rset.getString(5);
						    		}
						    		rset.close();
						    		stmt.close();
				    				
					    			queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? AND CONTRACT_TYPE = ? "
					    					+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? "
					    					+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";	
					    			stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, company_cd);
						    		stmt.setString(2, cd);
						    		stmt.setString(3, sec_ref_no);
						    		stmt.setString(4, map.split("-")[0]);
						    		stmt.setString(5, map.split("-")[1]);
						    		stmt.setString(6, map.split("-")[2]);
						    		stmt.setString(7, map.split("-")[3]);
						    		stmt.setString(8, map.split("-")[4]);
						    		stmt.setString(9, company_cd);
						    		stmt.setString(10, cd);
						    		stmt.setString(11, sec_ref_no);
						    		rset = stmt.executeQuery();
						    		if (rset.next() && rset.getString(1)!=null) {
						    			seq_no = rset.getString(1);
						    		}else {
						    			seq_no = "0";
						    		}
						    		rset.close();
						    		stmt.close();
					    				
				    			}
				    			else {
					    		queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? "
					    				+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, company_cd);
					    		stmt.setString(2, cd);
					    		stmt.setString(3, sec_ref_no);
					    		stmt.setString(4, company_cd);
					    		stmt.setString(5, cd);
					    		stmt.setString(6, sec_ref_no);
					    		rset = stmt.executeQuery();
					    		if (rset.next() && rset.getString(1)!=null) {
					    			seq_no = rset.getString(1);
					    		}else {
					    			seq_no = "0";
					    		}
					    		rset.close();
					    		stmt.close();
				    			}
				    		}
				    		data = seq_no;
				    	}
				    	else if (cell.getColumnIndex() == 7) {//GUARANTOR_CD
				    		g_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if(g_cd!=null)
				    		{
				    			g_cd = g_cd.substring(1, g_cd.length() - 1);
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
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
				    		bank_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if(bank_cd!=null) {
					    		bank_cd = bank_cd.substring(1, bank_cd.length() - 1);
								queryString2 = "SELECT BANK_CD FROM FMS_BANK_MST WHERE  BANK_NAME = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1,bank_cd );
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									bank_cd=rset2.getString(1);
								}
								else {
									bank_cd=null;
								}
								rset2.close();
								stmt2.close();
							}
				    		data=bank_cd;
				    		
						}
				    	
				    	else {
//				    		if (cell.getColumnIndex() == 2) {	// seq_no
//				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//				    			if (seq_no != null) {
//				    				seq_no = seq_no.substring(1, seq_no.length() - 1);
//								}
//					    	}
				    		
				    		if (cell.getColumnIndex() == 35) {	// seq_rev_no
				    			seq_rev_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_rev_no != null) {
				    				seq_rev_no = seq_rev_no.substring(1, seq_rev_no.length() - 1);
								}
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    
				    	stmt1.setString(index++, data);
				    
				    }
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
							+ "AND SEQ_NO =?  AND  SEQ_REV_NO = ? AND GX = ? ";
					
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, seq_no);
					stmt.setString(4, seq_rev_no);
					stmt.setString(5, "K");
					rset = stmt.executeQuery();
			    	
				    if (!rset.next() && !cd.equals("") && !seq_no.equals("0")) {
						
						logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + seq_rev_no + "," + "K" + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + seq_rev_no + "," + "K" + ","), conn, "E");
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
			
				System.out.println("<<END>><<"+table_name+">>");
				System.out.println();

				logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,"), conn); 

				logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,"), conn); 
				
				logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,"), conn); 
				
				logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,", conn);
				
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
			
		}//end of FMS_SECURITY_MST
		
		
		
		
		
	public void LOG_FMS_SECURITY_MST() throws IOException, SQLException {

			function_nm="LOG_FMS_SECURITY_MST(D)()";
			try {
				
				table_name = "LOG_FMS_SECURITY_MST(D)";
				System.out.println("<<START>><<"+table_name+">>");
				
				logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				String prev_seq_no="",log_seq_no="";
				int no=0;
				data = "";
				logger_count = 0;   
				skipped_count = 0;   
				total_count = 0; 
				String map="";
				
				queryString1 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,"
						+ "SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,"
						+ "VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,"
						+ "CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,"
						+ "REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,APRV_DT,APRV_BY,SEQ_REV_NO,GX,"
						+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,"
						+ "LOG_SEQ_NO,LOG_ENT_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD)"
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,? "
						+ ") ";
				stmt1 = conn.prepareStatement(queryString1);
			
				
				file1 = new File(migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST(D)_"+start_end_dt+".xlsx");
				if(file1.exists()) {
					
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST(D)_"+start_end_dt+".xlsx"));

					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					
					// Below block of code is for unique SEIPL data
					rowIterator = sheet.iterator();
					if (rowIterator.hasNext()) {	// For skipping the first row
						rowIterator.next();
					}

					logger.checkpoint(fname, "COMPANY_CD,ABBR , COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX, LOG_SEQ_NO,TIMESTAMP", conn);
					
					while (rowIterator.hasNext()) {
						String seq_rev_no="",bank_cd="",g_cd="",sec_ref_no="";
						abbr = "";seq_no="";
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
					    		data = cd;
					    	}
					    	else if(cell.getColumnIndex() == 2) {	//SEQ_NO
					    		sec_ref_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    		if(sec_ref_no!=null)
					    		{
					    			sec_ref_no = sec_ref_no.substring(1, sec_ref_no.length() - 1);
					    			
					    			if(map.startsWith("S")) {
					    				queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV FROM FMS_SUPPLY_CONT_MST "
					    						+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					    						+ "AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    				stmt = conn.prepareStatement(queryString);
					    				stmt.setString(1, company_cd);
					    				stmt.setString(2, cd);
					    				stmt.setString(3, map);
					    				stmt.setString(4, "F");
					    				rset = stmt.executeQuery();
							    		if (rset.next() && rset.getString(1)!=null) {
							    			map = rset.getString(1)+"-"+rset.getString(2)+"-"+rset.getString(3)+"-"+rset.getString(4)+"-"+rset.getString(5);
							    		}
							    		rset.close();
							    		stmt.close();
							    		
					    			queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?"
					    					+ " AND CONTRACT_TYPE = ? "
					    					+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? ";
//					    					+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";	
					    			stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, company_cd);
						    		stmt.setString(2, cd);
						    		stmt.setString(3, sec_ref_no);
						    		stmt.setString(4, map.split("-")[0]);
						    		stmt.setString(5, map.split("-")[1]);
						    		stmt.setString(6, map.split("-")[2]);
						    		stmt.setString(7, map.split("-")[3]);
						    		stmt.setString(8, map.split("-")[4]);
//						    		stmt.setString(9, company_cd);
//						    		stmt.setString(10, cd);
//						    		stmt.setString(11, sec_ref_no);
						    		rset = stmt.executeQuery();
						    		if (rset.next() && rset.getString(1)!=null) {
						    			seq_no = rset.getString(1);
						    		}else {
						    			seq_no = "0";
						    		}
						    		rset.close();
						    		stmt.close();
					    			}
					    			else if(map.startsWith("L")) {
					    				queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? "
					    						+ "AND COUNTERPARTY_CD = ? "
					    						+ "AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    				stmt = conn.prepareStatement(queryString);
					    				stmt.setString(1, company_cd);
					    				stmt.setString(2, cd);
					    				stmt.setString(3, map);
					    				stmt.setString(4, "E");
					    				rset = stmt.executeQuery();
							    		if (rset.next() && rset.getString(1)!=null) {
							    			map = rset.getString(1)+"-"+rset.getString(2)+"-"+rset.getString(3)+"-"+rset.getString(4)+"-"+rset.getString(5);
							    		}
							    		rset.close();
							    		stmt.close();
					    				
						    			queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? AND CONTRACT_TYPE = ? "
						    					+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? ";
//						    					+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";	
						    			stmt = conn.prepareStatement(queryString);
							    		stmt.setString(1, company_cd);
							    		stmt.setString(2, cd);
							    		stmt.setString(3, sec_ref_no);
							    		stmt.setString(4, map.split("-")[0]);
							    		stmt.setString(5, map.split("-")[1]);
							    		stmt.setString(6, map.split("-")[2]);
							    		stmt.setString(7, map.split("-")[3]);
							    		stmt.setString(8, map.split("-")[4]);
//							    		stmt.setString(9, company_cd);
//							    		stmt.setString(10, cd);
//							    		stmt.setString(11, sec_ref_no);
							    		rset = stmt.executeQuery();
							    		if (rset.next() && rset.getString(1)!=null) {
							    			seq_no = rset.getString(1);
							    		}else {
							    			seq_no = "0";
							    		}
							    		rset.close();
							    		stmt.close();
						    				
					    			}
					    			else {
						    		queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? ";
//						    				+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, company_cd);
						    		stmt.setString(2, cd);
						    		stmt.setString(3, sec_ref_no);
//						    		stmt.setString(4, company_cd);
//						    		stmt.setString(5, cd);
//						    		stmt.setString(6, sec_ref_no);
						    		rset = stmt.executeQuery();
						    		if (rset.next() && rset.getString(1)!=null) {
						    			seq_no = rset.getString(1);
						    		}else {
						    			seq_no = "0";
						    		}
						    		rset.close();
						    		stmt.close();
					    			}
					    		}
					    		data = seq_no;
					    	}
					    	else if (cell.getColumnIndex() == 7) {//GUARANTOR_CD
					    		g_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    		if(g_cd!=null)
					    		{
					    			g_cd = g_cd.substring(1, g_cd.length() - 1);
						    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						    		stmt = conn.prepareStatement(queryString);
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
					    		bank_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    		if(bank_cd!=null) {
						    		bank_cd = bank_cd.substring(1, bank_cd.length() - 1);
									queryString2 = "SELECT BANK_CD FROM FMS_BANK_MST WHERE  BANK_NAME = ? ";
									stmt2 = conn.prepareStatement(queryString2);
									stmt2.setString(1,bank_cd );
									rset2 = stmt2.executeQuery();
									if(rset2.next()) {
										bank_cd=rset2.getString(1);
									}
									else {
										bank_cd=null;
									}
									rset2.close();
									stmt2.close();
								}
					    		data=bank_cd;
					    		
							}	else if(cell.getColumnIndex() == 43)//LOG_SEQ_NO
							{
								log_seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(log_seq_no != null) {
						    		log_seq_no = log_seq_no.substring(1, log_seq_no.length()-1);
						    	}
						    	data=log_seq_no;
							}
					    	
					    	else {
//					    		if (cell.getColumnIndex() == 2) {	// seq_no
//					    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//					    			if (seq_no != null) {
//					    				seq_no = seq_no.substring(1, seq_no.length() - 1);
//									}
//						    	}
					    		
					    		if (cell.getColumnIndex() == 35) {	// seq_rev_no
					    			seq_rev_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    			if (seq_rev_no != null) {
					    				seq_rev_no = seq_rev_no.substring(1, seq_rev_no.length() - 1);
									}
					    		}
					    		
						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
					    	}
//					    System.out.println(":"+data);
					    	stmt1.setString(index++, data);
					    
					    }
//					    System.out.println("--------------------");
					    queryString = "SELECT SEQ_NO FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = ? "
					    		+ "AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = ? AND LOG_SEQ_NO =?  ";
//					    AND SEC_REF_NO = ? ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, company_cd);
				    	stmt.setString(2, cd);
				    	stmt.setString(3, seq_no);
				    	stmt.setString(4, seq_rev_no);
				    	stmt.setString(5, "K");
				    	stmt.setString(6, log_seq_no);
//				    	stmt.setString(7,sec_ref_no );
				    	
						
						rset = stmt.executeQuery();
				    	
					    if (!rset.next() && !cd.equals("") && !seq_no.equals("")) {
							
							logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + seq_rev_no + "," + "K" + ","+log_seq_no+","+sec_ref_no+","), conn, "");
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + seq_rev_no + "," + "K" + ","+log_seq_no+","+sec_ref_no+","), conn, "E");
						}
					    
					    rset.close();
					    stmt.close();
		        }
					
					msg = "Data has been Inserted Successfully in Database.";
					msg_type = "S";	
//					System.out.println(":"+logger_count);
				}
				else {
					msg = "Excel File not found while Execution. Program Terminated.";
					msg_type = "E";
				}
			
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
			
		}//end of LOG_FMS_SECURITY_MST
		
		
		
		
		
		
		
		
		public void FMS_SUPPLY_CONT_DCQ_DTL() throws IOException, SQLException {

			function_nm="FMS_SUPPLY_CONT_DCQ_DTL(DLNG)()";
			try {
				
				table_name = "FMS_SUPPLY_CONT_DCQ_DTL";
				System.out.println("<<START>><<"+table_name+">>");
				
				logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				data = "";
				logger_count = 0;   
				skipped_count = 0;   
				total_count = 0; 

				
				queryString1 = "INSERT INTO FMS_SUPPLY_CONT_DCQ_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SEQ_NO,"
						+ "FROM_DT,TO_DT,DCQ,REMARK,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
						+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
				stmt1 = conn.prepareStatement(queryString1);
				
				file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_DCQ_DTL(DLNG)_"+start_end_dt+".xlsx");
				if(file1.exists()) {

					
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_DCQ_DTL(DLNG)_"+start_end_dt+".xlsx"));

					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					
					// Below block of code is for unique SEIPL data
					rowIterator = sheet.iterator();
					if (rowIterator.hasNext()) {	// For skipping the first row
						rowIterator.next();
					}

					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,TIMESTAMP,", conn);
					
					while (rowIterator.hasNext()) {
						no = ""; rev = ""; seq_no = "" ; cont_type = "";
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
					    			cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							else if (cell.getColumnIndex() == 2) { //Agmt_no
//					    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//					    		if (no != null) {
//									no = no.substring(1, no.length() - 1);
//								}
//					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, "%@"+cont_ref);
//						    		stmt.setString(3, cont_ref.split("-")[0]);
						    		
						    		
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
//					    		}
					    		
								data = no;
					    	}
					    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
//					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
//						    		rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (rev != null) {
//										rev = rev.substring(1, rev.length() - 1);
//									}
//					    		}
								data = rev;
					    	}
					    	else if (cell.getColumnIndex() == 4) { //Cont_no
//					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
//						    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (cont_no != null) {
//						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
//									}
//					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (cell.getColumnIndex() == 5) { //Cont_rev
//					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
//						    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (cont_rev != null) {
//						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//									}	
//					    		}
					    		data = cont_rev;
					    	}
					    	else if (cell.getColumnIndex() == 6) { //contract_type
//					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
//						    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//						    		if (cont_type != null) {
//						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
//									}	
//					    		}
					    		data = cont_type;
					    	}
							
					    	else {
					    		
					    		if (cell.getColumnIndex() == 7) {	// seq_no
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
					     
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_DCQ_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND SEQ_NO = ?";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, no);
						stmt.setString(4, rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, cont_type);
						stmt.setString(8, seq_no);
						rset = stmt.executeQuery();
				    	

					    if (!rset.next() && !cd.equals("") && !no.equals("")) {
							
							logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + no + "," + rev + "," + cont_no + "," + cont_rev + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + no + "," + rev + "," + cont_no + "," + cont_rev + ","), conn, "E");
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
		
		
		public void FMS_SUPPLY_PURCHASE_MAP_DTL() throws IOException, SQLException {
			
			function_nm="FMS_SUPPLY_PURCHASE_MAP_DTL(DLNG)()";
			try {
				
				table_name = "FMS_SUPPLY_PURCHASE_MAP_DTL(DLNG)";
				System.out.println("<<START>><<"+table_name+">>");
				
				logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				data = "";
				String rate_unit="", pur_cont_no="",new_pur_cont_no="";;
				logger_count = 0;   
				skipped_count = 0;   
				total_count = 0; 

				
				queryString1 = "INSERT INTO FMS_SUPPLY_PURCHASE_MAP_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
						+ "PUR_CONT_NO,ALLOC_QTY,QTY_UNIT,SALE_PRICE,RATE_UNIT,COST_PRICE,CP_UNIT,MARGIN,TOTAL_MARGIN,STATUS,ENT_BY,ENT_DT,MODIFY_BY,"
						+ "MODIFY_DT,APRV_BY,APRV_DT,AVG_MARGIN,AUTH_BY,AUTH_DT,CARGO_NO) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
				stmt1 = conn.prepareStatement(queryString1);
				
				file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_PURCHASE_MAP_DTL(DLNG)_"+start_end_dt+".xlsx");
				if(file1.exists()) {

					
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_PURCHASE_MAP_DTL(DLNG)_"+start_end_dt+".xlsx"));

					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					
					// Below block of code is for unique SEIPL data
					rowIterator = sheet.iterator();
					if (rowIterator.hasNext()) {	// For skipping the first row
						rowIterator.next();
					}

					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PUR_CONT_NO,TIMESTAMP,", conn);
					
					while (rowIterator.hasNext()) {
						no = ""; rev = ""; seq_no = "";
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
					    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (no != null) {
									no = no.substring(1, no.length() - 1);
								}
					    		queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE IN ('F','E') ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, "%@"+cont_ref);
					    		
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
					    	else if(cell.getColumnIndex() == 3) {
								data = rev;
							}
							else if(cell.getColumnIndex() == 4) {
								data=cont_no;
							}
							else if(cell.getColumnIndex() == 5) {
								data = cont_rev;
							}
							else if(cell.getColumnIndex() == 6) {
								data = cont_type;
							}
					    	
					    	else if (cell.getColumnIndex() == 7) {	// pur_cont_no
					    		pur_cont_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (pur_cont_no != null) {
				    				pur_cont_no = pur_cont_no.substring(1, pur_cont_no.length() - 1);
								}
				    			
				    			if((pur_cont_no.contains("D")) || (pur_cont_no.contains("I")) || (pur_cont_no.contains("T"))) {
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
			    				}
				    			else if(pur_cont_no.length()==1) {
			    					new_pur_cont_no = "0";
			    				}
//			    				else if (pur_cont_no.contains("-") && pur_cont_no.contains("I")){
//			    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ?";
//			    					stmt = conn.prepareStatement(queryString);
//			    					stmt.setString(1, "%"+pur_cont_no+"%");
//						    		rset = stmt.executeQuery();
//						    		if(rset.next()) {
//						    			new_pur_cont_no = rset.getString(1);
//						    		}else {
//						    			new_pur_cont_no = "";
//						    		}
//						    		rset.close();
//						    		stmt.close();
//			    				}
			    				else {
			    					queryString = "SELECT CONT_NO, CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ?";
			    					stmt = conn.prepareStatement(queryString);
			    					stmt.setString(1, pur_cont_no);
						    		rset = stmt.executeQuery();
						    		if(rset.next()) {
						    			new_pur_cont_no = rset.getString(1);
						    			cargo_no = rset.getString(2);
						    		}else {
						    			new_pur_cont_no = "";
						    		}
						    		rset.close();
						    		stmt.close();
			    				}
					    		data = 	new_pur_cont_no;		    			
							}
					    	
					    	else if (cell.getColumnIndex() == 11) {	//rate_unit
					    		rate_unit = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (rate_unit != null) {
				    				rate_unit = rate_unit.substring(1, rate_unit.length() - 1);
				    				data=rate_unit.trim();
								}
					    	}
					    	
					    	else if (cell.getColumnIndex() == 26) {// cargo_no
					    		if ((!pur_cont_no.contains("D")) || (!pur_cont_no.contains("I")) || (!pur_cont_no.contains("T"))) {	
				    			
					    			data = cargo_no;
					    		}else {
						    		cargo_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    			if (cargo_no != null) {
					    				cargo_no = cargo_no.substring(1, cargo_no.length() - 1);
									}
					    			data = cargo_no;
					    		}
				    		
				    		}
//					    	
//					    	else if (cell.getColumnIndex() == 26 && !pur_cont_no.contains("-")) {	// seq_no
//				    			data = cargo_no;
//				    		}

					    	else {
//					    		if (cell.getColumnIndex() == 26) {	// seq_no
//					    			cargo_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//					    			if (cargo_no != null) {
//					    				cargo_no = cargo_no.substring(1, cargo_no.length() - 1);
//									}
//					    		}
					    		
						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
					    	}
					    	stmt1.setString(index++, data);
					    
					    }
					     
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PUR_CONT_NO = ? AND CARGO_NO = ?";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, no);
						stmt.setString(4, rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, cont_type);
						stmt.setString(8, new_pur_cont_no);
						stmt.setString(9, cargo_no);
						rset = stmt.executeQuery();
				    	

					    if (!rset.next() && !cd.equals("") && !no.equals("") && !new_pur_cont_no.equals("")) {
							
							logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + no + "," + rev + "," + cont_no + "," + cont_rev + ","+ new_pur_cont_no + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + no + "," + rev + "," + cont_no + "," + cont_rev + ","+ new_pur_cont_no + ","), conn, "E");
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
		

		//READER
		//FMS_SUPPLY_ALLOC_REVISED
			public void FMS_SUPPLY_ALLOC_REVISED() throws IOException, SQLException {
				
				function_nm="FMS_SUPPLY_ALLOC_REVISED(DLNG)()";
				try {
					
					table_name = "FMS_SUPPLY_ALLOC_REVISED(DLNG)";
					System.out.println("<<START>><<"+table_name+">>");
					
					logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
					
					data = "";
					String pur_cont_no="",new_pur_cont_no="";
					logger_count = 0;   
					skipped_count = 0;   
					total_count = 0; 

					
					queryString1 = "INSERT INTO FMS_SUPPLY_ALLOC_REVISED(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "MODIFICATION_SEQ_NO,PUR_CONT,NEW_PRICE_EFF_DT,ORI_SALE_PRICE,NEW_SALE_PRICE,ORI_MARGIN,NEW_MARGIN,ORI_AVG_MARGIN,NEW_AVG_MARGIN,"
							+ "ORI_TOT_MARGIN,NEW_TOT_MARGIN,ENT_BY,ENT_DT,APPROVE_BY,APPROVE_DT,FLAG,REMARK,ALLOC_QTY,CARGO_NO) VALUES(?,?,?,?,?,?,?,?,?,"
							+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
					stmt1 = conn.prepareStatement(queryString1);
					
					file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_ALLOC_REVISED(DLNG)_"+start_end_dt+".xlsx");
					if(file1.exists()) {

						
						file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_ALLOC_REVISED(DLNG)_"+start_end_dt+".xlsx"));

						workbook = new XSSFWorkbook(file);
						sheet = workbook.getSheetAt(0);
						
						// Below block of code is for unique SEIPL data
						rowIterator = sheet.iterator();
						if (rowIterator.hasNext()) {	// For skipping the first row
							rowIterator.next();
						}

						logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PUR_CONT,MODIFICATION_SEQ,TIMESTAMP,", conn);
						
						while (rowIterator.hasNext()) {
							no = ""; rev = ""; seq_no = "";
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
						    		no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
						    		if (no != null) {
										no = no.substring(1, no.length() - 1);
									}
						    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE IN ('F','E') ";
							    		stmt = conn.prepareStatement(queryString);
							    		stmt.setString(1, cd);
							    		stmt.setString(2, "%@"+cont_ref);
							    		
							    		
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
//						    		}
						    		
									data = no;
						    	}
						    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
									data = rev;
						    	}
						    	else if (cell.getColumnIndex() == 4) { //Cont_no
						    		data = cont_no;
						    	}
					    		
						    	else if (cell.getColumnIndex() == 5) { //Cont_rev
						    		data = cont_rev;
						    	}
						    	else if (cell.getColumnIndex() == 6) { //contract_type
						    		data = cont_type;
						    	}
						    	
						    	else if (cell.getColumnIndex() == 8) {	// pur_cont_no
						    		pur_cont_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    			if (pur_cont_no != null) {
					    				pur_cont_no = pur_cont_no.substring(1, pur_cont_no.length() - 1);
									}
					    			
					    			if((pur_cont_no.contains("D")) || (pur_cont_no.contains("I")) || (pur_cont_no.contains("T"))) {
				    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ?";
				    					stmt = conn.prepareStatement(queryString);
				    					stmt.setString(1, "%"+pur_cont_no+"%");
							    		rset = stmt.executeQuery();
							    		if(rset.next()) {
							    			new_pur_cont_no = rset.getString(1);
							    		}else {
							    			new_pur_cont_no = "0";
							    		}
							    		rset.close();
							    		stmt.close();
				    				}

				    				else {
				    					queryString = "SELECT CONT_NO, CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ?";
				    					stmt = conn.prepareStatement(queryString);
				    					stmt.setString(1, pur_cont_no);
							    		rset = stmt.executeQuery();
							    		if(rset.next()) {
							    			new_pur_cont_no = rset.getString(1);
							    			cargo_no = rset.getString(2);
							    		}else {
							    			new_pur_cont_no = "0";
							    		}
							    		rset.close();
							    		stmt.close();
				    				}
						    		data = 	new_pur_cont_no;	    			
								}
						    		
						    	else if (cell.getColumnIndex() == 25) {// cargo_no
						    		if ((!pur_cont_no.contains("D")) || (!pur_cont_no.contains("I")) || (!pur_cont_no.contains("T"))) {	
					    			
						    			data = cargo_no;
						    		}else {
							    		cargo_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    			if (cargo_no != null) {
						    				cargo_no = cargo_no.substring(1, cargo_no.length() - 1);
										}
						    			data = cargo_no;
						    		}
					    		
					    		}
						    	
						    	else {
						    		if (cell.getColumnIndex() == 7) {	// mod_seq
						    			mod_seq = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    			if (mod_seq != null) {
						    				mod_seq = mod_seq.substring(1, mod_seq.length() - 1);
										}
						    		}
							    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							    	if(data != null) {
							    		data = data.substring(1, data.length()-1);
							    	}
						    	}
						    	//System.out.println(index+"-"+data);
						    	stmt1.setString(index++, data);
						    
						    }
						     
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_ALLOC_REVISED WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
									+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND MODIFICATION_SEQ_NO = ? "
									+ "AND PUR_CONT = ? AND CARGO_NO = ?";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, no);
							stmt.setString(4, rev);
							stmt.setString(5, cont_no);
							stmt.setString(6, cont_rev);
							stmt.setString(7, cont_type);
							stmt.setString(8, mod_seq);
							stmt.setString(9, new_pur_cont_no);
							stmt.setString(10, cargo_no);
							rset = stmt.executeQuery();
					    	

						    if (!rset.next() && !cd.equals("") && !new_pur_cont_no.equals("") && !no.equals("") && !rev.equals("")&& !cont_no.equals("") && !cont_rev.equals("")&& !cont_type.equals("")) {
								
								logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + new_pur_cont_no +"," + mod_seq + ","), conn, "");
								
								stmt1.executeUpdate();
								stmt1.close();
								
								logger_count++;
							}
							else {
								stmt1.close();
								skipped_count++;     
								logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + new_pur_cont_no +"," + mod_seq + ","), conn, "E");
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

					logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,,,"), conn); 

					logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,,,"), conn); 
					
					logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,,,"), conn); 
					
					logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,,,", conn);
					
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
		
		//FMS_CONT_PRICE_DTL
public void FMS_CONT_PRICE_DTL() throws IOException, SQLException {

			function_nm="FMS_CONT_PRICE_DTL(DLNG)()";
			try {
				table_name = "FMS_CONT_PRICE_DTL(DLNG)";
				System.out.println("<<START>><<"+table_name+">>");
				logger.checkpoint(fname,"\n<<START>>,<<FMS_CONT_PRICE_DTL(DLNG)>>,,,," , conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				data = "";
				logger_count = 0;
				skipped_count = 0;   
				total_count = 0;

				queryString1="INSERT INTO FMS_CONT_PRICE_DTL(COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO, FROM_DT, TO_DT, PRICE_TYPE,"
						+ " CURVE_NM, SLOPE, CONST, QUANTITY_UNIT, RATE, RATE_UNIT, REMARKS, ENT_BY, ENT_DT, MODIFY_BY, MODIFY_DT, PRICE_RANGE,"
						+ " PRICE_START_DT, PRICE_END_DT, PHYS_CURVE_NM, CURVE_LOGIC, FORMULA)"
						+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,"
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
				stmt1 = conn.prepareStatement(queryString1);
				
				file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL(DLNG)_"+start_end_dt+".xlsx");
				if(file1.exists()) {

					
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL(DLNG)_"+start_end_dt+".xlsx"));

					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					
					// Below block of code is for unique SEIPL data
					rowIterator = sheet.iterator();
					if (rowIterator.hasNext()) {	// For skipping the first row
						rowIterator.next();
					}
					
					logger.checkpoint(fname, "COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,TIMESTAMP", conn);
					
					while (rowIterator.hasNext()) {
						total_count++;
						String seq_no = "",map_id="",cont_type="";
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
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {				    			
					    			cd=rset.getString(1);
					    		}else  {
					    			cd = "";
					    		}	
					    		
					    		rset.close();
					    		stmt.close();
						    					    						    						    		
					    		map_id  = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());		
					    		if (!map_id.equals("NULL")) {
									map_id = map_id.substring(1, map_id.length() - 1);
								}
								if(map_id.startsWith("L")) {
					    			String[] parts = map_id.split("-");	
					    			no = parts[2];
						            cont_no = parts[4];
						            cont_rev = parts[5];
						            
						            cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
						            
						            queryString2 = "SELECT CONT_NO FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = 'E' ";
						    		stmt2 = conn.prepareStatement(queryString2);
						    		stmt2.setString(1, cd);
						    		stmt2.setString(2, "%@"+cont_ref);
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
					    			no = parts[1];
					    			rev = parts[2];
						            cont_no = parts[3];
						            cont_rev = parts[4];
						            
						            cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						            
					    			 queryString2 = "SELECT CONT_NO FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = 'F' ";
							    		stmt2 = conn.prepareStatement(queryString2);
							    		stmt2.setString(1, cd);
							    		stmt2.setString(2, "%@"+cont_ref);
							    		rset2 = stmt2.executeQuery();
							    		if (rset2.next()) {					    		
							    			cont_no = rset2.getString(1);					    			
							    		} else {
							    			cont_no = "";

							    		}
							    		rset2.close();
							    		stmt2.close();
//					    			String[] parts = map_id.split("-");	
//						            no = parts[1];
//						            cont_no = parts[3];
					    		}
					            
					    		map_id = cd+"-"+no+"-"+cont_no;	
					    		
					    		data = map_id;
					    		}
					    	else if (cell.getColumnIndex() == 23) {	// MULTI_LEG
					    		data = cell.getStringCellValue();
					    		
					    		if (data!=null || !data.equals("")) {
									data = data.substring(1, data.length()-1);
					    		if (data.contains("MULTI_LEG@")) {
					    			if (data.split("@")[1].contains("-") && data.split("@")[2].contains("-")) {
					    				data = data.split("@")[0] + "@Settled@" + data.split("@")[1].substring(1) + "@" + data.split("@")[2].substring(1);
					    			}
					    			else if (!data.split("@")[1].contains("-") && !data.split("@")[2].contains("-")) {
					    				data = data.split("@")[0] + "@Forward@" + data.split("@")[1] + "@" + data.split("@")[2];
					    			}
					    		}
					    	}
					    		else {
					    			data = null;
					    		}
					    	}

					    	else {
					    		if (cell.getColumnIndex() == 3) {	// seq_no
					    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();;
					    			if (seq_no!=null) {
										seq_no = seq_no.substring(1, seq_no.length() - 1);
									}
					    		}				    	
					    		if (cell.getColumnIndex() == 2) {	// cont_type
					    			cont_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();;
					    			if (cont_type!=null) {
										cont_type = cont_type.substring(1, cont_type.length() - 1);
									}
					    		}				    		
					    		

						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
					    	}
					    	stmt1.setString(index++, data);
					    }
					     
				    	queryString = "SELECT SEQ_NO FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = ? AND MAPPING_ID = ? AND SEQ_NO = ? AND CONTRACT_TYPE = ?";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, company_cd);   	
				    	stmt.setString(2, map_id);
				    	stmt.setString(3, seq_no);
				    	stmt.setString(4, cont_type);
				    	rset = stmt.executeQuery();
				    	
					    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("") && !cd.equals("")) {				
					    	//System.out.println(queryString1);
					    	logger.data(fname,(company_cd+","+map_id+" ,"+cont_type+" ,"+seq_no+","), conn,"");
					    	stmt1.executeUpdate();
					    	stmt1.close();
					    	logger_count++;				    	
					    }else {
					    	skipped_count++;
					    	logger.data(fname,(company_cd+","+map_id+", "+cont_type+" ,"+seq_no+","), conn, "E");
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
//				conn.commit();
				System.out.println("<<END>><<"+table_name+">>");
				System.out.println();
				
				logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

				logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
							
				logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
				
				logger.checkpoint1(fname1,logger_count+",", conn);
							
				logger.checkpoint(fname, "<<END>><<FMS_CONT_PRICE_DTL(DLNG)>>", conn);
				
			}
			catch(Exception e)
			{
				msg = "One of the Functions faced an Error. Data Not Inserted.";
				msg_type = "E";
				
				conn.rollback();
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
			finally {
				conn.commit();
				if (file != null) {
					file.close();
				}
			}
			
		}

//FMS_CONT_PRICE_MIN_DTL
public void FMS_CONT_PRICE_MIN_DTL() throws IOException, SQLException {

			function_nm="FMS_CONT_PRICE_MIN_DTL(DLNG)()";
			try {
				table_name = "FMS_CONT_PRICE_MIN_DTL(DLNG)";
				System.out.println("<<START>><<"+table_name+">>");
				logger.checkpoint(fname,"\n<<START>>,<<FMS_CONT_PRICE_MIN_DTL(DLNG)>>,,,," , conn);
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				data = "";
				logger_count = 0;
				skipped_count = 0;   
				total_count = 0;

		    	
				queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, table_name);
				rset = stmt.executeQuery();
				
				rset.close();
				stmt.close();
				
				
				queryString1="INSERT INTO FMS_CONT_PRICE_MIN_DTL(COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,CURVE_LOGIC,CURVE_NM,SLOPE,"
						+ "CONST,QUANTITY_UNIT,RATE,RATE_UNIT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT)"
						+ " VALUES(?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, ?, "
						+ "?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
						+ " ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss')"
						+ ")";
				stmt1 = conn.prepareStatement(queryString1);
				
				file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL(DLNG)_"+start_end_dt+".xlsx");
				if(file1.exists()) {

					
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL(DLNG)_"+start_end_dt+".xlsx"));

					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					
					// Below block of code is for unique SEIPL data
					rowIterator = sheet.iterator();
					if (rowIterator.hasNext()) {	// For skipping the first row
						rowIterator.next();
					}
					
					logger.checkpoint(fname, "COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,CURVE_NM,TIMESTAMP", conn);
					
					while (rowIterator.hasNext()) {
						total_count++;
						String seq_no = "", curve_nm = "",map_id = "",cont_type = "";
						String no="";
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
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {				    			
					    			cd=rset.getString(1);
					    		}else  {
					    			cd = "";
					    		}	
					    		
					    		rset.close();
					    		stmt.close();
						    					    						    						    		
					    		map_id  = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());		
					    		if (!map_id.equals("NULL")) {
									map_id = map_id.substring(1, map_id.length() - 1);
								}
								if(map_id.startsWith("L")) {
					    			String[] parts = map_id.split("-");	
					    			no = parts[2];
						            cont_no = parts[4];
						            cont_rev = parts[5];
						            
						            cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
						            
						            queryString2 = "SELECT CONT_NO FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = 'E' ";
						    		stmt2 = conn.prepareStatement(queryString2);
						    		stmt2.setString(1, cd);
						    		stmt2.setString(2, "%@"+cont_ref);
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
					    			no = parts[1];
					    			rev = parts[2];
						            cont_no = parts[3];
						            cont_rev = parts[4];
						            
						            cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						            
					    			 queryString2 = "SELECT CONT_NO FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = 'F' ";
							    		stmt2 = conn.prepareStatement(queryString2);
							    		stmt2.setString(1, cd);
							    		stmt2.setString(2, "%@"+cont_ref);
							    		rset2 = stmt2.executeQuery();
							    		if (rset2.next()) {					    		
							    			cont_no = rset2.getString(1);					    			
							    		} else {
							    			cont_no = "";

							    		}
							    		rset2.close();
							    		stmt2.close();
//					    			String[] parts = map_id.split("-");	
//						            no = parts[1];
//						            cont_no = parts[3];
					    		}
					            
					    		map_id = cd+"-"+no+"-"+cont_no;	
					    		
					    		data = map_id;
					    		}
					    	else if (cell.getColumnIndex() == 13) {	// MULTI_LEG
					    		data = cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
					    		if (data.contains("M@")) {
					    			if (data.split("@")[1].contains("-") && data.split("@")[2].contains("-")) {
					    				data = data.split("@")[0] + "@Settled@" + data.split("@")[1].substring(1) + "@" + data.split("@")[2].substring(1);
					    			}
					    			else if (!data.split("@")[1].contains("-") && !data.split("@")[2].contains("-")) {
					    				data = data.split("@")[0] + "@Forward@" + data.split("@")[1] + "@" + data.split("@")[2];
					    			}
					    		}
					    	}
					    	else {
					    		
					    		if (cell.getColumnIndex() == 2) {	// cont_type
					    			cont_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    			if (cont_type!=null) {
										cont_type = cont_type.substring(1, cont_type.length() - 1);
									}
					    		}
					    		if (cell.getColumnIndex() == 3) {	// seq_no
					    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    			if (seq_no!=null) {
										seq_no = seq_no.substring(1, seq_no.length() - 1);
									}
					    		}
					    		if (cell.getColumnIndex() == 7) {	// curve_nm
					    			curve_nm = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    			if (curve_nm!=null) {
										curve_nm = curve_nm.substring(1, curve_nm.length() - 1);
									}
					    		}
					    		
						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
					    	}
					    	
					    	stmt1.setString(index++, data);
					    }
					     
				    	queryString = "SELECT SEQ_NO FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = ? AND MAPPING_ID = ? AND CONTRACT_TYPE = ? AND SEQ_NO = ? AND CURVE_NM = ? ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, company_cd);
				    	stmt.setString(2, map_id);
				    	stmt.setString(3, cont_type);
				    	stmt.setString(4, seq_no);
				    	stmt.setString(5, curve_nm);
				    	rset = stmt.executeQuery();
				    	
					    if (row.getRowNum() != 0 && !rset.next() && !no.equals("")&& !cont_no.equals("") && !cd.equals("")) {
					    	//System.out.println(queryString1);		    	
					    	logger.data(fname,(company_cd+","+map_id+", "+cont_type+", "+seq_no+", "+curve_nm+","), conn,"");
					    	stmt1.executeUpdate();
					    	stmt1.close();
					    	logger_count++;
					    } else {
					    	skipped_count++;
					    	logger.data(fname,(company_cd+" ,"+map_id+", "+cont_type+" ,"+seq_no+", "+curve_nm+","), conn, "E");
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
//				conn.commit();
				System.out.println("<<END>><<"+table_name+">>");
				System.out.println();
				
				logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

				logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
							
				logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
				
				logger.checkpoint1(fname1,logger_count+",", conn);
							
				logger.checkpoint(fname, "<<END>><<FMS_CONT_PRICE_MIN_DTL(DLNG)>>", conn);
				
			}
			catch(Exception e)
			{
				msg = "One of the Functions faced an Error. Data Not Inserted.";
				msg_type = "E";
				
				conn.rollback();
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
			finally {
				conn.commit();
				if (file != null) {
					file.close();
				}
			}
			
		}		

	
		
	public void FMS_SVC_CONT_MAP() throws IOException, SQLException {

		function_nm="FMS_SVC_CONT_MAP()";
		try {
			
			System.out.println("<<START>><<FMS_SVC_CONT_MAP>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SVC_CONT_MAP>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			String  type = "",counterparty="",abbr="",clause="",sn_ref="",name="",ref="";
			String sn_type="",sn_no="",sn_rev="",sn_cont_no="",sn_cont_rev="";
			int temp_cont_rev = 0;
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_SVC_CONT_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CUSTOMER_CD,SELL_CONT_MAP,ENT_BY,ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SVC_CONT_MAP_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SVC_CONT_MAP_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SELL_CONT_MAP,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = ""; type="";
						ref="";sn_ref="";abbr="";
						no="";rev="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							if (cell.getColumnIndex() == 1) {
								cd = cell.getStringCellValue();
								if (cd!=null) {
									cd = cd.substring(1, cd.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 2) {
								type = cell.getStringCellValue();
								if (type!=null) {
									type = type.substring(1, type.length() - 1);
								}
							}
//							if (cell.getColumnIndex() == 3) {
//								no = cell.getStringCellValue();
//								if (no!=null) {
//									no = no.substring(1, no.length() - 1);
//								}
//							}
//							if (cell.getColumnIndex() == 4) {
//								rev = cell.getStringCellValue();
//								if (rev!=null) {
//									rev = rev.substring(1, rev.length() - 1);
//								}
//							}
							if (cell.getColumnIndex() == 5) {
								name = cell.getStringCellValue();
								if (name!=null) {
									name = name.substring(1, name.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 6) {
								ref = cell.getStringCellValue();
								if (ref!=null) {
									ref = ref.substring(1, ref.length() - 1);
								}
							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							
							if (cell.getColumnIndex() == 1) {	//COUNTERPARTY_CD
								if (data!=null) {
									data = data.substring(1, data.length() - 1);
									ref = data;
									data = data.split("-")[0];
								
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, data.toUpperCase());
								rset = stmt.executeQuery();
								if(rset.next()) {
									cd = rset.getString(1);
								}
								
								rset.close();
								stmt.close();
								}
								data = "'"+cd+"'";
							}
							
							else if(cell.getColumnIndex() == 2) {	//AGMT_NO
								queryString = "SELECT AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE FROM FMS_SVC_CONT_MST WHERE CONT_REF_NO = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, ref);
//								System.out.println(ref);
								rset = stmt.executeQuery();
								if(rset.next()) {
									no = rset.getString(1);
									rev = rset.getString(2);
									cont_no = rset.getString(3);
									cont_rev = rset.getString(4);
									type = rset.getString(5);
								}
								rset.close();
								stmt.close();
								data = "'"+no+"'";
							}
							
							else if(cell.getColumnIndex() == 3) {	//AGMT_REV
								data = "'"+rev+"'";
							}
							
							else if(cell.getColumnIndex() == 4) {	//CONT_NO
								data = "'"+cont_no+"'";
							}
							
							else if(cell.getColumnIndex() == 5) {	//CONT_REV
								data = "'"+cont_rev+"'";
							}
							
							else if(cell.getColumnIndex() == 6) {	//CONTRACT_TYPE
								data = "'"+type+"'";
							}
							
							else if(cell.getColumnIndex() == 7) {	//CUSTOMER_CD
								data = "'"+cd+"'";
							}
							
							else if(cell.getColumnIndex() == 8) {	//SELL_CONT_MAP
								if (data!=null) {
									data = data.substring(1, data.length() - 1);
								}
								sn_ref = data;
								temp_cont_rev = Integer.parseInt(sn_ref.split("-")[4]);
								
								queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = 'F' ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd); 
								stmt.setString(2, cd); 
								stmt.setString(3, "%@"+sn_ref);
								rset = stmt.executeQuery();
								if(rset.next()) {
									sn_type = rset.getString(1);
									sn_no = rset.getString(2);
									sn_rev = rset.getString(3);
									sn_cont_no = rset.getString(4);
									sn_cont_rev = rset.getString(5);
								}
								else {
									sn_ref = sn_ref.substring(0,sn_ref.length()-1);
									
									while(temp_cont_rev > 0) {
										temp_cont_rev = temp_cont_rev - 1;
									
										sn_ref = sn_ref + temp_cont_rev;
										
										queryString2 = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = 'F' ";
										stmt2 = conn.prepareStatement(queryString2);
										stmt2.setString(1, company_cd);
										stmt2.setString(2, cd);
										stmt2.setString(3, "%@"+sn_ref);
										rset2 = stmt2.executeQuery();
										if(rset2.next()) {
											sn_type = rset2.getString(1);
											sn_no = rset2.getString(2);
											sn_rev = rset2.getString(3);
											sn_cont_no = rset2.getString(4);
											sn_cont_rev = rset2.getString(5);
											
											break;
										}
										else {
											sn_type = "";
											sn_no = "";
											sn_rev = "";
											sn_cont_no = "";
											sn_cont_rev = "";
										}
										rset2.close();
										stmt2.close();
									}
								}
								rset.close();
								stmt.close();
								
								if(!sn_type.equals("")) {
									sn_ref = sn_type +"-"+ sn_no +"-"+ sn_rev +"-"+ sn_cont_no  +"-"+ sn_cont_rev;
								}
								else{
									sn_ref = "";
								}
								data = "'"+sn_ref+"'";
							}
							
							
							if(data!=null && data.contains("null")) {
								data = null;
							}
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_SVC_CONT_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ?  AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND CUSTOMER_CD = ? AND SELL_CONT_MAP = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, no);
						stmt.setString(4, rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, type);
						stmt.setString(8, cd);
						stmt.setString(9, sn_ref);
						
						
						rset = stmt.executeQuery();
						
						if (!rset.next() && !sn_ref.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + sn_ref + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + type + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + sn_ref + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_SVC_CONT_MAP>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SVC_CONT_MAP>>,,", conn);
			
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

	public void FMS_DLNG_BUYER_NOM() throws IOException, SQLException {
		function_nm="FMS_DLNG_BUYER_NOM(D)()";
		try {
			
			table_name = "FMS_DLNG_BUYER_NOM";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			
			queryString1 = "INSERT INTO FMS_DLNG_BUYER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BASE,GCV,NCV,GAS_DT,GEN_DT,GEN_TIME,NOM_REV_NO,"
					+ "PLANT_SEQ,BU_SEQ,QTY_MMBTU,QTY_MT,QTY_SCM,ENT_BY,ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_BUYER_NOM_"+start_end_dt+".csv");
			if(file1.exists()) {

				String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_BUYER_NOM_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();

					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,GAS_DT,NOM_REV_NO,PLANT_SEQ_NO,BU_SEQ,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						String gen_time="",gas_dt="";
						no = ""; rev = ""; seq_no = "" ; 
						String nom_rev_no = "",plant_seq="",bu_seq=""; 
						abbr = "";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) 
						for (int i = 0; i < line.split(",").length; i++)
					    {
//						cell = cellIterator.next();
							data = null;
//						System.out.println(line.split(",")[i]);
					    	if (i == 0) {	// Counterparty_Abbr, Company Code 
					    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!abbr.equals("NULL")) {
//								abbr = abbr.substring(1, abbr.length() - 1);
//							}
					    		data = company_cd;
					    	}
					    	else if (i == 1) {	// Counterparty_Cd
					    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!cont_ref.equals("NULL")) {
//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
//							}
					    		
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
							else if (i == 2) { //Agmt_no
					    		no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (no != null) {
//								no = no.substring(1, no.length() - 1);
//							}
					    		cont_type = cont_ref.split("-")[0];
					    		if(cont_type.equals("S")) {
					    			cont_type = "F";
					    		}
					    		else {
					    			cont_type = "E";
					    		}
					    		queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = ? ";
						    	stmt = conn.prepareStatement(queryString);
						    	stmt.setString(1, cd);
						    	stmt.setString(2, "%@"+cont_ref);
						    	stmt.setString(3, cont_type);
						    		
						    		
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
					    	else if (i == 3) { //Agmt_rev
								data = rev;
					    	}
					    	else if (i == 4) { //Cont_no
					    		data = cont_no;
					    	}
					    	else if (i == 5) { //Cont_rev
					    		data = cont_rev;
					    	}

					    	else if (i == 6) { //contract_type
					    		data = cont_type;
					    	}
					    	
					    	else if(i == 15) {	//bu_seq
					    		queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND"
					    				+ " CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? "; 
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, company_cd);
					    		stmt.setString(2, cd);
					    		stmt.setString(3, no);
					    		stmt.setString(4, rev);
					    		stmt.setString(5, cont_no);
					    		stmt.setString(6, cont_rev);
					    		stmt.setString(7, cont_type);
					    		rset = stmt.executeQuery();
					    		if(rset.next()) {
					    			bu_seq = rset.getString(1);
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = bu_seq;
					    	}
					    	
					    	
					    	else {				    		
					    		if (i == 10) {	// gas_dt
					    			gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gas_dt != null) {
//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//								}
					    		}
					    		if (i == 12) {	//gen_time
					    			gen_time = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gen_time != null) {
//				    				gen_time = gen_time.substring(1, gen_time.length() - 1);
//								}
					    		}	
					    		if (i == 13) {	// nom_rev_no
					    			nom_rev_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
					    		}
					    		if (i == 14) {	// plant_seq
					    			plant_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (plant_seq_no != null) {
//				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
//								}
					    		}
					    								    		

						    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
					    	}
					    	
//					    	System.out.println(index+"-"+data);
					    	stmt1.setString(index++, data);
					    
					    }
					     
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_BUYER_NOM WHERE "
								+ "COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? "
								+ "AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND NOM_REV_NO = ? AND PLANT_SEQ = ? AND BU_SEQ = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, no);
						stmt.setString(4, rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, cont_type);
						stmt.setString(8, gas_dt);
						stmt.setString(9, nom_rev_no);
						stmt.setString(10, plant_seq);
						stmt.setString(11, bu_seq);
						rset = stmt.executeQuery();

					    if (!rset.next() && !cd.equals("") && !bu_seq.equals("") && !no.equals("")){
							
							logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + gas_dt + "," + nom_rev_no + "," + plant_seq + "," + bu_seq + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + gas_dt + "," + nom_rev_no + "," + plant_seq + "," + bu_seq + "," ), conn, "E");
						}
					    
					    rset.close();
					    stmt.close();
					}
					br.close();
				}

//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
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
	
	
	public void FMS_DLNG_BUYER_NOM_DTL() throws IOException, SQLException {
		function_nm="FMS_DLNG_BUYER_NOM_DTL(D)()";
		try {
			
			table_name = "FMS_DLNG_BUYER_NOM_DTL(D)";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			
			queryString1 = "INSERT INTO FMS_DLNG_BUYER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,BU_SEQ,BASE,GCV,NCV,GAS_DT,GEN_DT,"
					+ "GEN_TIME,ARRIVAL_DT,ARRIVAL_TIME,NOM_REV_NO,TRUCK_TRANS_CD,TRUCK_CD,FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,QTY_MMBTU,QTY_MT,NEXT_AVAIL_HRS,"
					+ "REMARK,ENT_BY,ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_BUYER_NOM_DTL_"+start_end_dt+".csv");
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_BUYER_NOM_DTL_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();
					
					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,GAS_DT,NOM_REV_NO,PLANT_SEQ_NO,BU_SEQ,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						String gen_time="",gas_dt="";
						no = ""; rev = ""; seq_no = "" ; 
						String nom_rev_no = "",plant_seq="",bu_seq="";
						String trans_cd="",truck_id="",fill_cd="",bay_cd="",slot_st="",slot_ed="",slot_interval="",arrival_tm="",slot_st_hr="",arrival_tm_hr="";
						abbr = "";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);

//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) 
						for (int i = 0; i < line.split(",").length; i++)
						{
//						cell = cellIterator.next();
							data = null;
//						System.out.println(line.split(",")[i]);
							if (i == 0) {	// Counterparty_Abbr, Company Code 
								abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!abbr.equals("NULL")) {
//								abbr = abbr.substring(1, abbr.length() - 1);
//							}
								data = company_cd;
							}
							else if (i == 1) {	// Counterparty_Cd
								cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!cont_ref.equals("NULL")) {
//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
//							}
								
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
							else if (i == 2) { //Agmt_no
								no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (no != null) {
//								no = no.substring(1, no.length() - 1);
//							}
								cont_type = cont_ref.split("-")[0];
								if(cont_type.equals("S")) {
									cont_type = "F";
								}
								else {
									cont_type = "E";
								}
								queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cd);
								stmt.setString(2, "%@"+cont_ref);
								stmt.setString(3, cont_type);
								
								
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
							else if (i == 3) { //Agmt_rev
								data = rev;
							}
							else if (i == 4) { //Cont_no
								data = cont_no;
							}
							else if (i == 5) { //Cont_rev
								data = cont_rev;
							}
							
							else if (i == 6) { //contract_type
								data = cont_type;
							}
							
							else if(i == 8) {	//bu_seq
								queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND"
										+ " CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? "; 
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, cd);
								stmt.setString(3, no);
								stmt.setString(4, rev);
								stmt.setString(5, cont_no);
								stmt.setString(6, cont_rev);
								stmt.setString(7, cont_type);
								rset = stmt.executeQuery();
								if(rset.next()) {
									bu_seq = rset.getString(1);
								}
								rset.close();
								stmt.close();
								
								data = bu_seq;
							}
							
							else if(i == 18) {	//TRUCK_TRANS_CD
								
								data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								if(data!=null) {
								queryString = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE ENT_PROFILE = ? AND UPPER(TRUCK_TRANS_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, data.toUpperCase());
								rset = stmt.executeQuery();
								if(rset.next()) {
									trans_cd = rset.getString(1);
								}
								else {
									trans_cd = "0";
								}
								rset.close();
								stmt.close();
								}
								data = trans_cd;
							}
							
							else if(i == 22) {	//SLOT_START_TIME
								arrival_tm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								if(arrival_tm!=null) {
								queryString = "SELECT SLOT_START_TIME, SLOT_INTERVAL FROM FMS_BAY_SLOT_MST WHERE FILL_STATION_CD = '1' AND BAY_CD = '1' ";
								stmt = conn.prepareStatement(queryString);
								rset = stmt.executeQuery();
								if(rset.next()) {
									slot_st = rset.getString(1);
									slot_interval = rset.getString(2);
								}
								else {
									slot_st = "00:00";
									slot_interval = "03:00";
								}
								rset.close();
								stmt.close();
								
								arrival_tm_hr = arrival_tm.split(":")[0];
								slot_st_hr = slot_st.split(":")[0];
								
								if((Integer.parseInt(arrival_tm_hr) >= Integer.parseInt(slot_st_hr)) && ((Integer.parseInt(arrival_tm_hr) - Integer.parseInt(slot_st_hr)) < 3) && ((Integer.parseInt(arrival_tm_hr) - Integer.parseInt(slot_st_hr)) >= 0)) {
									data = slot_st;
								}
								else {
									if((Integer.parseInt(slot_st.split(":")[0]) + Integer.parseInt(slot_interval.split(":")[0])+"").length() > 1) {
										slot_st = Integer.parseInt(slot_st.split(":")[0]) + Integer.parseInt(slot_interval.split(":")[0]) + ":00";
									}
									else {
										slot_st = "0"+(Integer.parseInt(slot_st.split(":")[0]) + Integer.parseInt(slot_interval.split(":")[0])) + ":00";
									}
//									slot_st = Integer.parseInt(slot_st.split(":")[0]) + Integer.parseInt(slot_interval.split(":")[0])+":00";
									while(!slot_st.split(":")[0].equals(slot_st_hr)){
										if((Integer.parseInt(arrival_tm_hr) >= Integer.parseInt(slot_st.split(":")[0])) && ((Integer.parseInt(arrival_tm_hr) - Integer.parseInt(slot_st.split(":")[0])) < 3) && ((Integer.parseInt(arrival_tm_hr) - Integer.parseInt(slot_st.split(":")[0])) >= 0)){
											data = slot_st;
											break;
										}
										else {
										
										queryString = "SELECT TO_CHAR(TO_DATE(?, 'HH24:MI') +   INTERVAL '3' HOUR  , 'HH24') FROM DUAL ";
										stmt = conn.prepareStatement(queryString);
										stmt.setString(1, slot_st);
//										stmt.setString(2, slot_interval.split(":")[0]);
										rset = stmt.executeQuery();
										if(rset.next()) {
											if((rset.getInt(1) % 24 + "").length() > 1) {
												slot_st = (rset.getInt(1) % 24)+":00";
											}
											else {
												slot_st = "0"+(rset.getInt(1) % 24)+":00";
											}
										}
										rset.close();
										stmt.close();
										data = slot_st;
										}
	//									slot_st_hr = slot_st.split(":")[0];
	//									if(slot_st_hr.equals("24")) {
	//										slot_st_hr = "00";
	//									}
	//									if(arrival_tm_hr.equals(slot_st_hr) || Integer.parseInt(arrival_tm_hr) < Integer.parseInt(slot_st_hr)) {
	//										data = slot_st;
	//									}
	//									else {
	//										slot_st_hr = Integer.parseInt(slot_st.split(":")[0]) + Integer.parseInt(slot_interval.split(":")[0])+"";
	//									}
									}
									
	//								data = slot_st;
									}
								}
//								System.out.println(slot_st);
							}
							else if(i == 23) {	//SLOT_END_TIME
								if((Integer.parseInt(slot_st.split(":")[0]) + Integer.parseInt(slot_interval.split(":")[0])+"").length() > 1) {
									slot_ed = Integer.parseInt(slot_st.split(":")[0]) + Integer.parseInt(slot_interval.split(":")[0]) + ":00";
								}
								else {
									slot_ed = "0"+(Integer.parseInt(slot_st.split(":")[0]) + Integer.parseInt(slot_interval.split(":")[0])) + ":00";
								}
								data = slot_ed;
							}
							
							else {
								if (i == 7) {	// plant_seq
									plant_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (plant_seq_no != null) {
//				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
//								}
								}
								if (i == 12) {	// gas_dt
									gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gas_dt != null) {
//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//								}
								}
								if (i == 14) {	//gen_time
									gen_time = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gen_time != null) {
//				    				gen_time = gen_time.substring(1, gen_time.length() - 1);
//								}
								}	
								if (i == 17) {	// nom_rev_no
									nom_rev_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
								if (i == 19) {	// truck_cd
									truck_id = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
								if (i == 20) {	//fill_cd
									fill_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
								if (i == 21) {	// bay_cd
									bay_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
//								if (i == 22) {	//slot_start_time
//									slot_st = line.split(",")[i].contains("null") ? null : line.split(",")[i];
////				    			if (nom_rev_no != null) {
////				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
////								}
//								}
//								if (i == 23) {	// slot_end_time
//									slot_ed = line.split(",")[i].contains("null") ? null : line.split(",")[i];
////				    			if (nom_rev_no != null) {
////				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
////								}
//								}
								
								
								
								data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
							}
							
//					    	System.out.println(index+"-"+data);
							stmt1.setString(index++, data);
							
						}

						queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_BUYER_NOM_DTL WHERE "
								+ "COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? "
								+ "AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND NOM_REV_NO = ? AND PLANT_SEQ = ? AND BU_SEQ = ? AND TRUCK_TRANS_CD = ? "
								+ "AND TRUCK_CD = ?  AND FILL_STATION_CD = ? AND BAY_CD = ? AND SLOT_START_TIME = ? AND SLOT_END_TIME = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, no);
						stmt.setString(4, rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, cont_type);
						stmt.setString(8, gas_dt);
						stmt.setString(9, nom_rev_no);
						stmt.setString(10, plant_seq);
						stmt.setString(11, bu_seq);
						stmt.setString(12, trans_cd);
						stmt.setString(13, truck_id);
						stmt.setString(14, fill_cd);
						stmt.setString(15, bay_cd);
						stmt.setString(16, slot_st);
						stmt.setString(17, slot_ed);
						
//						System.out.println(company_cd +"."+cd+"."+no +"."+rev+"."+cont_no+"."+cont_rev+"."+cont_type+"."+gas_dt+"."+nom_rev_no+"."+plant_seq+"."+
//						bu_seq +"."+trans_cd+"."+truck_id+"."+fill_cd+"."+bay_cd +"."+slot_st+"."+slot_ed);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !bu_seq.equals("") && !no.equals("")){
							
							logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + gas_dt + "," + nom_rev_no + "," + plant_seq + "," + bu_seq + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + gas_dt + "," + nom_rev_no + "," + plant_seq + "," + bu_seq + "," ), conn, "E");
						}
						
						rset.close();
						stmt.close();
					}
					br.close();
				}
				
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
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
	
	
	public void FMS_DLNG_SELLER_NOM_DTL() throws IOException, SQLException {
		function_nm="FMS_DLNG_SELLER_NOM_DTL(D)()";
		try {
			
			table_name = "FMS_DLNG_SELLER_NOM_DTL(D)";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			
			queryString1 = "INSERT INTO FMS_DLNG_SELLER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,BU_SEQ,BASE,GCV,NCV,GAS_DT,GEN_DT,"
					+ "GEN_TIME,ARRIVAL_DT,ARRIVAL_TIME,NOM_REV_NO,TRUCK_TRANS_CD,TRUCK_CD,FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,QTY_MMBTU,QTY_MT,NEXT_AVAIL_HRS,"
					+ "REMARK,ENT_BY,ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_SELLER_NOM_DTL_"+start_end_dt+".csv");
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_SELLER_NOM_DTL_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();
					
					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,GAS_DT,NOM_REV_NO,PLANT_SEQ_NO,BU_SEQ,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						String gen_time="",gas_dt="";
						no = ""; rev = ""; seq_no = "" ; 
						String nom_rev_no = "",plant_seq="",bu_seq="";
						String trans_cd="",truck_id="",fill_cd="",bay_cd="",slot_st="",slot_ed="";
						abbr = "";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) 
						for (int i = 0; i < line.split(",").length; i++)
						{
//						cell = cellIterator.next();
							data = null;
//						System.out.println(line.split(",")[i]);
							if (i == 0) {	// Counterparty_Abbr, Company Code 
								abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!abbr.equals("NULL")) {
//								abbr = abbr.substring(1, abbr.length() - 1);
//							}
								data = company_cd;
							}
							else if (i == 1) {	// Counterparty_Cd
								cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!cont_ref.equals("NULL")) {
//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
//							}
								
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
							else if (i == 2) { //Agmt_no
								no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (no != null) {
//								no = no.substring(1, no.length() - 1);
//							}
								cont_type = cont_ref.split("-")[0];
								if(cont_type.equals("S")) {
									cont_type = "F";
								}
								else {
									cont_type = "E";
								}
								queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cd);
								stmt.setString(2, "%@"+cont_ref);
								stmt.setString(3, cont_type);
								
								
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
							else if (i == 3) { //Agmt_rev
								data = rev;
							}
							else if (i == 4) { //Cont_no
								data = cont_no;
							}
							else if (i == 5) { //Cont_rev
								data = cont_rev;
							}
							
							else if (i == 6) { //contract_type
								data = cont_type;
							}
							
							else if(i == 8) {	//bu_seq
								queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND"
										+ " CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? "; 
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, cd);
								stmt.setString(3, no);
								stmt.setString(4, rev);
								stmt.setString(5, cont_no);
								stmt.setString(6, cont_rev);
								stmt.setString(7, cont_type);
								rset = stmt.executeQuery();
								if(rset.next()) {
									bu_seq = rset.getString(1);
								}
								rset.close();
								stmt.close();
								
								data = bu_seq;
							}
							
							else if(i == 18) {	//TRUCK_TRANS_CD
								
								data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								if(data!=null) {
								queryString = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE ENT_PROFILE = ? AND UPPER(TRUCK_TRANS_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, data.toUpperCase());
								rset = stmt.executeQuery();
								if(rset.next()) {
									trans_cd = rset.getString(1);
								}
								else {
									trans_cd = "0";
								}
								rset.close();
								stmt.close();
								}
								data = trans_cd;
							}
							
							else if(i == 22){	//SLOT_START_TIME
								queryString = "SELECT A.SLOT_START_TIME, A.SLOT_END_TIME FROM FMS_DLNG_BUYER_NOM_DTL A WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? AND "
										+ "A.AGMT_NO = ? AND A.AGMT_REV = ? AND A.CONT_NO = ? AND A.CONT_REV = ? AND A.CONTRACT_TYPE = ? AND A.PLANT_SEQ = ? AND A.BU_SEQ = ? AND "
										+ "A.GAS_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND A.TRUCK_TRANS_CD = ? AND A.TRUCK_CD = ? AND "
										+ "A.FILL_STATION_CD = ? AND A.BAY_CD = ? AND A.NOM_REV_NO = (SELECT MAX(C.NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM_DTL C WHERE "
										+ "C.COMPANY_CD = A.COMPANY_CD AND C.COUNTERPARTY_CD = A.COUNTERPARTY_CD AND C.AGMT_NO = A.AGMT_NO AND C.AGMT_REV = A.AGMT_REV AND "
										+ "C.CONT_NO = A.CONT_NO AND C.CONT_REV = A.CONT_REV AND C.CONTRACT_TYPE = A.CONTRACT_TYPE AND C.PLANT_SEQ = A.PLANT_SEQ AND "
										+ "C.BU_SEQ = A.BU_SEQ AND C.GAS_DT = A.GAS_DT AND C.TRUCK_TRANS_CD = A.TRUCK_TRANS_CD AND C.TRUCK_CD = A.TRUCK_CD )";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, cd);
								stmt.setString(3, no);
								stmt.setString(4, rev);
								stmt.setString(5, cont_no);
								stmt.setString(6, cont_rev);
								stmt.setString(7, cont_type);
								stmt.setString(8, plant_seq);
								stmt.setString(9, bu_seq);
								stmt.setString(10, gas_dt);
								stmt.setString(11, trans_cd);
								stmt.setString(12, truck_id);
								stmt.setString(13, fill_cd);
								stmt.setString(14, bay_cd);
								rset = stmt.executeQuery();
								if(rset.next()) {
									slot_st = rset.getString(1);
									slot_ed = rset.getString(2);
								}
								else {
									slot_st = "00:00";
									slot_ed = "03:00";
								}
								rset.close();
								stmt.close();
								
								data = slot_st;
							}
							
							else if(i == 23){	//SLOT_END_TIME
								data = slot_ed;
							}
							
							else {
								if (i == 7) {	// plant_seq
									plant_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (plant_seq_no != null) {
//				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
//								}
								}
								if (i == 12) {	// gas_dt
									gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gas_dt != null) {
//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//								}
								}
								if (i == 14) {	//gen_time
									gen_time = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gen_time != null) {
//				    				gen_time = gen_time.substring(1, gen_time.length() - 1);
//								}
								}	
								if (i == 17) {	// nom_rev_no
									nom_rev_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
								if (i == 19) {	// truck_cd
									truck_id = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
								if (i == 20) {	//fill_cd
									fill_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
								if (i == 21) {	// bay_cd
									bay_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
//								if (i == 22) {	//slot_start_time
//									slot_st = line.split(",")[i].contains("null") ? null : line.split(",")[i];
////				    			if (nom_rev_no != null) {
////				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
////								}
//								}
//								if (i == 23) {	// slot_end_time
//									slot_ed = line.split(",")[i].contains("null") ? null : line.split(",")[i];
////				    			if (nom_rev_no != null) {
////				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
////								}
//								}
								
								
								
								data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
							}
							
//					    	System.out.println(index+"-"+data);
							stmt1.setString(index++, data);
							
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_SELLER_NOM_DTL WHERE "
								+ "COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? "
								+ "AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND NOM_REV_NO = ? AND PLANT_SEQ = ? AND BU_SEQ = ? AND TRUCK_TRANS_CD = ? "
								+ "AND TRUCK_CD = ?  AND FILL_STATION_CD = ? AND BAY_CD = ? AND SLOT_START_TIME = ? AND SLOT_END_TIME = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, no);
						stmt.setString(4, rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, cont_type);
						stmt.setString(8, gas_dt);
						stmt.setString(9, nom_rev_no);
						stmt.setString(10, plant_seq);
						stmt.setString(11, bu_seq);
						stmt.setString(12, trans_cd);
						stmt.setString(13, truck_id);
						stmt.setString(14, fill_cd);
						stmt.setString(15, bay_cd);
						stmt.setString(16, slot_st);
						stmt.setString(17, slot_ed);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !bu_seq.equals("") && !no.equals("")){
							
							logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + gas_dt + "," + nom_rev_no + "," + plant_seq + "," + bu_seq + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + gas_dt + "," + nom_rev_no + "," + plant_seq + "," + bu_seq + "," ), conn, "E");
						}
						
						rset.close();
						stmt.close();
					}
					br.close();
				}
				
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
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
	
	
	public void FMS_DLNG_ALLOC_MST() throws IOException, SQLException {
		function_nm="FMS_DLNG_ALLOC_MST(D)()";
		try {
			
			table_name = "FMS_DLNG_ALLOC_MST(D)";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			NumberFormat nf = new DecimalFormat("###########0.00");
			
			queryString1 = "INSERT INTO FMS_DLNG_ALLOC_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,BU_SEQ,BASE,GCV,NCV,GAS_DT,FILL_STATION_CD,BAY_CD,SLOT_START_TIME,"
					+ "SLOT_END_TIME,GEN_DT,GEN_TIME,NOM_REV_NO,TRUCK_TRANS_CD,TRUCK_CD,LOAD_START_DT,LOAD_START_TIME,LOAD_END_DT,LOAD_END_TIME,NEXT_AVAIL_HRS,QTY_MMBTU,"
					+ "QTY_MT,GCV_MMBTU,ENT_BY,ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_ALLOC_MST_"+start_end_dt+".csv");
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_ALLOC_MST_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();
					
					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,BU_SEQ,GAS_DT,FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,NOM_REV_NO,TRUCK_TRANS_CD,TRUCK_CD,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						String gen_time="",gas_dt="";
						no = ""; rev = ""; seq_no = "" ; 
						String nom_rev_no = "",plant_seq="",bu_seq="";
						String trans_cd="",truck_id="",fill_cd="",bay_cd="",slot_st="",slot_ed="";
						abbr = "";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) 
						for (int i = 0; i < line.split(",").length; i++)
						{
//						cell = cellIterator.next();
							data = null;
//						System.out.println(line.split(",")[i]);
							if (i == 0) {	// Counterparty_Abbr, Company Code 
								abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!abbr.equals("NULL")) {
//								abbr = abbr.substring(1, abbr.length() - 1);
//							}
								data = company_cd;
							}
							else if (i == 1) {	// Counterparty_Cd
								cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!cont_ref.equals("NULL")) {
//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
//							}
								
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
							else if (i == 2) { //Agmt_no
//								no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (no != null) {
//								no = no.substring(1, no.length() - 1);
//							}
								cont_type = cont_ref.split("-")[0];
								if(cont_type.equals("S")) {
									cont_type = "F";
								}
								else {
									cont_type = "E";
								}
								queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cd);
								stmt.setString(2, "%@"+cont_ref);
								stmt.setString(3, cont_type);
								
								
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
							else if (i == 3) { //Agmt_rev
								data = rev;
							}
							else if (i == 4) { //Cont_no
								data = cont_no;
							}
							else if (i == 5) { //Cont_rev
								data = cont_rev;
							}
							
							else if (i == 6) { //contract_type
								data = cont_type;
							}
							
							else if(i == 8) {	//bu_seq
								queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND"
										+ " CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? "; 
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, cd);
								stmt.setString(3, no);
								stmt.setString(4, rev);
								stmt.setString(5, cont_no);
								stmt.setString(6, cont_rev);
								stmt.setString(7, cont_type);
								rset = stmt.executeQuery();
								if(rset.next()) {
									bu_seq = rset.getString(1);
								}
								rset.close();
								stmt.close();
								
								data = bu_seq;
							}
							
							else if(i == 15) {	//SLOT_START_TIME
								
								data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								
								if(data!=null) {
								trans_cd = data.split("-")[0];
								truck_id = data.split("-")[1];
								}
								
								
								queryString = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE ENT_PROFILE = ? AND UPPER(TRUCK_TRANS_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, trans_cd.toUpperCase());
								rset = stmt.executeQuery();
								if(rset.next()) {
									trans_cd = rset.getString(1);
								}
								rset.close();
								stmt.close();
								
								queryString = "SELECT A.SLOT_START_TIME, A.SLOT_END_TIME FROM FMS_DLNG_BUYER_NOM_DTL A WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? AND "
										+ "A.AGMT_NO = ? AND A.AGMT_REV = ? AND A.CONT_NO = ? AND A.CONT_REV = ? AND A.CONTRACT_TYPE = ? AND A.PLANT_SEQ = ? AND A.BU_SEQ = ? AND "
										+ "A.GAS_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND A.TRUCK_TRANS_CD = ? AND A.TRUCK_CD = ? AND "
										+ "A.FILL_STATION_CD = ? AND A.BAY_CD = ? AND A.NOM_REV_NO = (SELECT MAX(C.NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM_DTL C WHERE "
										+ "C.COMPANY_CD = A.COMPANY_CD AND C.COUNTERPARTY_CD = A.COUNTERPARTY_CD AND C.AGMT_NO = A.AGMT_NO AND C.AGMT_REV = A.AGMT_REV AND "
										+ "C.CONT_NO = A.CONT_NO AND C.CONT_REV = A.CONT_REV AND C.CONTRACT_TYPE = A.CONTRACT_TYPE AND C.PLANT_SEQ = A.PLANT_SEQ AND "
										+ "C.BU_SEQ = A.BU_SEQ AND C.GAS_DT = A.GAS_DT AND C.TRUCK_TRANS_CD = A.TRUCK_TRANS_CD AND C.TRUCK_CD = A.TRUCK_CD )";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, cd);
								stmt.setString(3, no);
								stmt.setString(4, rev);
								stmt.setString(5, cont_no);
								stmt.setString(6, cont_rev);
								stmt.setString(7, cont_type);
								stmt.setString(8, plant_seq);
								stmt.setString(9, bu_seq);
								stmt.setString(10, gas_dt);
								stmt.setString(11, trans_cd);
								stmt.setString(12, truck_id);
								stmt.setString(13, fill_cd);
								stmt.setString(14, bay_cd);
								rset = stmt.executeQuery();
								if(rset.next()) {
									slot_st = rset.getString(1);
									slot_ed = rset.getString(2);
								}
								else {
									if(!cont_rev.equals("") &&  Integer.parseInt(cont_rev) > 0) {
										queryString2 = "SELECT A.SLOT_START_TIME, A.SLOT_END_TIME FROM FMS_DLNG_BUYER_NOM_DTL A WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? AND "
												+ "A.AGMT_NO = ? AND A.AGMT_REV = ? AND A.CONT_NO = ? AND A.CONT_REV = ? AND A.CONTRACT_TYPE = ? AND A.PLANT_SEQ = ? AND A.BU_SEQ = ? AND "
												+ "A.GAS_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND A.TRUCK_TRANS_CD = ? AND A.TRUCK_CD = ? AND "
												+ "A.FILL_STATION_CD = ? AND A.BAY_CD = ? AND A.NOM_REV_NO = (SELECT MAX(C.NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM_DTL C WHERE "
												+ "C.COMPANY_CD = A.COMPANY_CD AND C.COUNTERPARTY_CD = A.COUNTERPARTY_CD AND C.AGMT_NO = A.AGMT_NO AND C.AGMT_REV = A.AGMT_REV AND "
												+ "C.CONT_NO = A.CONT_NO AND C.CONT_REV = A.CONT_REV AND C.CONTRACT_TYPE = A.CONTRACT_TYPE AND C.PLANT_SEQ = A.PLANT_SEQ AND "
												+ "C.BU_SEQ = A.BU_SEQ AND C.GAS_DT = A.GAS_DT AND C.TRUCK_TRANS_CD = A.TRUCK_TRANS_CD AND C.TRUCK_CD = A.TRUCK_CD )";
										stmt2 = conn.prepareStatement(queryString2);
										stmt2.setString(1, company_cd);
										stmt2.setString(2, cd);
										stmt2.setString(3, no);
										stmt2.setString(4, rev);
										stmt2.setString(5, cont_no);
										stmt2.setString(6, Integer.parseInt(cont_rev)-1+"");
										stmt2.setString(7, cont_type);
										stmt2.setString(8, plant_seq);
										stmt2.setString(9, bu_seq);
										stmt2.setString(10, gas_dt);
										stmt2.setString(11, trans_cd);
										stmt2.setString(12, truck_id);
										stmt2.setString(13, fill_cd);
										stmt2.setString(14, bay_cd);
										rset2 = stmt2.executeQuery();
										if(rset2.next()) {
											slot_st = rset2.getString(1);
											slot_ed = rset2.getString(2);
										}
										else {
											slot_st = "00:00";
											slot_ed = "03:00";
										}
									}
									else {
										slot_st = "00:00";
										slot_ed = "03:00";
									}
								}
								rset.close();
								stmt.close();
								
								data = slot_st;
							}
							
							else if(i == 16) {	//SLOT_END_TIME
								data = slot_ed;
							}
							
							else if(i == 20) {	//TRUCK_TRANS_CD
								data = trans_cd;
							}
							
							else if(i == 21) {	//TRUCK_CD
								data = truck_id;
							}
							
//							else if(i == 23) {	//LOAD_START_TIME
//								data = slot_st;
//							}
							
							
//							else if(i == 25) {	//LOAD_END_TIME
//								data = slot_ed;
//							}
							
//							else if(i == 27) {
//								data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//								 
//								data = nf.format(Double.parseDouble(data));
//								 
//							}
							
							else {
								if (i == 7) {	// plant_seq
									plant_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (plant_seq_no != null) {
//				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
//								}
								}
								if (i == 12) {	// gas_dt
									gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gas_dt != null) {
//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//								}
								}
								if (i == 13) {	//fill_cd
									fill_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
								if (i == 14) {	// bay_cd
									bay_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
								if (i == 19) {	// nom_rev_no
									nom_rev_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
								if (i == 21) {	// truck_cd
									truck_id = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
								}
								
								
								
								data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
							}
							
//					    	System.out.println(index+"-"+data);
							stmt1.setString(index++, data);
							
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_ALLOC_MST WHERE "
								+ "COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? "
								+ "AND PLANT_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND FILL_STATION_CD = ? AND BAY_CD = ? AND SLOT_START_TIME = ? AND SLOT_END_TIME = ? "
								+ "AND NOM_REV_NO = ?  AND TRUCK_TRANS_CD = ? AND TRUCK_CD = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, no);
						stmt.setString(4, rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, cont_type);
						stmt.setString(8, plant_seq);
						stmt.setString(9, bu_seq);
						stmt.setString(10, gas_dt);
						stmt.setString(11, fill_cd);
						stmt.setString(12, bay_cd);
						stmt.setString(13, slot_st);
						stmt.setString(14, slot_ed);
						stmt.setString(15, nom_rev_no);
						stmt.setString(16, trans_cd);
						stmt.setString(17, truck_id);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !bu_seq.equals("") && !no.equals("")){
							
							logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + plant_seq + "," + bu_seq + "," + gas_dt + "," + fill_cd + "," + bay_cd + "," + slot_st + "," + slot_ed + "," + nom_rev_no + "," + trans_cd + "," + truck_id + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + plant_seq + "," + bu_seq + "," + gas_dt + "," + fill_cd + "," + bay_cd + "," + slot_st + "," + slot_ed + "," + nom_rev_no + "," + trans_cd + "," + truck_id + "," ), conn, "E");
						}
						
						rset.close();
						stmt.close();
					}
					br.close();
				}
				
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
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
	
	
	
	public void FMS_DLNG_INVOICE_MST() throws IOException, SQLException {
		function_nm="FMS_DLNG_INVOICE_MST()";
		try {
			
			table_name = "FMS_DLNG_INVOICE_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			
			queryString1 = "INSERT INTO FMS_DLNG_INVOICE_MST(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRUCK_TRANS_CD,TRUCK_CD,"
					+ "MAPPING_ID,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INV_FLAG,INVOICE_SEQ,INVOICE_ID_SEQ,INVOICE_NO,INVOICE_DT,FREQ,"
					+ "PERIOD_START_DT,PERIOD_END_DT,DUE_DT,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,EXCHG_RATE_TYPE,"
					+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT_1,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,REMARK_1,REMARK_2,CHECKED_FLAG,CHECKED_BY,"
					+ "CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TCS_TDS,TCS_AMT,"
					+ "TCS_FACTOR,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,"
					+ "TDS_TAX_PERCENT,TDS_TAX_AMT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,"
					+ "SAP_APPROVED_DT,TCS_STRUCT_CD,TCS_EFF_DT_1,TDS_STRUCT_CD,TDS_EFF_DT_1,TAX_EFF_DT,TCS_EFF_DT,TDS_EFF_DT,CHKPOST_CD,DRIVER_CD,FIN_SYS,HOLD_AMT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,"
					+ "?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_INVOICE_MST_"+start_end_dt+".csv");
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_INVOICE_MST_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();
					
					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,BU_SEQ,INVOICE_DT,FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,INVOICE_NO,TRUCK_TRANS_CD,TRUCK_CD,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						String trans_cd="",bu_seq="",truck_cd="",inv_dt="",fill_cd="",bay_cd="",slot_st="",slot_end="",bu_state="",state_code="",contact_cd="";
						String bu_cont="",exch_rate="",plant_seq="",inv_raised="",name="",tax_dt="",chk_cd="",driver_cd="",fin_year="",inv_seq="",inv_no="",mail="",lic="";
						no = ""; rev = ""; seq_no = "" ; 
						abbr = "";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						for (int i = 0; i < line.split(",").length; i++)
						{
//						cell = cellIterator.next();
							data = null;
//						System.out.println(line.split(",")[i]);
							if (i == 0) {	// Counterparty_Abbr, Company Code 
								abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!abbr.equals("NULL")) {
//								abbr = abbr.substring(1, abbr.length() - 1);
//							}
								data = company_cd;
							}
							else if (i == 1) {	// Counterparty_Cd
								cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
								
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
							else if (i == 3) { //Agmt_no
//								no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (no != null) {
//								no = no.substring(1, no.length() - 1);
//							}
								cont_type = cont_ref.split("-")[0];
								if(cont_type.equals("S")) {
									cont_type = "F";
								}
								else {
									cont_type = "E";
								}
								queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cd);
								stmt.setString(2, "%@"+cont_ref);
								stmt.setString(3, cont_type);
								
								
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
							else if (i == 4) { //Agmt_rev
								data = rev;
							}
							else if (i == 5) { //Cont_no
								data = cont_no;
							}
							else if (i == 6) { //Cont_rev
								data = cont_rev;
							}
							
							else if (i == 7) { //contract_type
								data = cont_type;
							}
							
							else if(i == 8) {	//TRUCK_TRANS_CD
								trans_cd = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
								trans_cd = trans_cd.toUpperCase();
								queryString = "SELECT TRUCK_TRANS_CD FROM FMS_TRUCK_TRANSPORTER_MST WHERE UPPER(TRUCK_TRANS_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, trans_cd);
								rset = stmt.executeQuery();
								if(rset.next()) {
									trans_cd = rset.getString(1);
								}
								stmt.close();
								rset.close();
								
								data = trans_cd;
							}
							
							else if(i == 10) {	//MAPPING_ID
								inv_dt = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
								queryString = "SELECT A.FILL_STATION_CD, A.BAY_CD, A.SLOT_START_TIME, A.SLOT_END_TIME FROM FMS_DLNG_ALLOC_MST A WHERE A.COMPANY_CD = ? AND "
										+ "A.COUNTERPARTY_CD = ? AND A.AGMT_NO = ? AND A.AGMT_REV = ? AND A.CONT_NO = ? AND A.CONT_REV = ? AND A.CONTRACT_TYPE = ? AND "
										+ "TO_DATE(A.GAS_DT, 'DD/MM/YY HH24:MI:SS') = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND A.TRUCK_CD = ? AND A.TRUCK_TRANS_CD = ? AND A.BU_SEQ = '1' "
										+ "AND A.NOM_REV_NO = (SELECT MAX(C.NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST C WHERE A.COMPANY_CD = C.COMPANY_CD "
										+ "AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND A.AGMT_NO = C.AGMT_NO AND A.AGMT_REV = C.AGMT_REV AND A.CONT_NO = C.CONT_NO "
										+ "AND A.CONT_REV = C.CONT_REV AND A.CONTRACT_TYPE = C.CONTRACT_TYPE AND A.GAS_DT = C.GAS_DT AND A.TRUCK_CD = C.TRUCK_CD AND A.TRUCK_TRANS_CD = C.TRUCK_TRANS_CD )";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, cd);
								stmt.setString(3, no);
								stmt.setString(4, rev);
								stmt.setString(5, cont_no);
								stmt.setString(6, cont_rev);
								stmt.setString(7, cont_type);
								stmt.setString(8, inv_dt);
								stmt.setString(9, truck_cd);
								stmt.setString(10, trans_cd);
								rset = stmt.executeQuery();
								if(rset.next()) {
									fill_cd = rset.getString(1);
									bay_cd = rset.getString(2);
									slot_st = rset.getString(3);
									slot_end = rset.getString(4);
								}
								stmt.close();
								rset.close();
								
								data = fill_cd+"-"+bay_cd+"-"+slot_st+"-"+slot_end;
							}
							
							else if(i == 12) {	//BU_STATE_TIN
								queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
					    				+ " AND COUNTERPARTY_CD= '2' AND ENTITY='B' AND SEQ_NO = ?";
								stmt=conn.prepareStatement(queryString);
								stmt.setString(1, bu_seq);
								rset = stmt.executeQuery();
					    		if (rset.next()) {				    			
					    			bu_state = rset.getString(1);
					    		}else {
					    			bu_state  ="0";
					    		}	
					    		rset.close();
					    		stmt.close();
					    		
					    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
								stmt=conn.prepareStatement(queryString);
								stmt.setString(1, bu_state);
								rset = stmt.executeQuery();
					    		if (rset.next()) {				    			
					    			state_code = rset.getString(1);
					    		}else {
					    			state_code  ="0";
					    		}	
					    		rset.close();
					    		stmt.close();
					    		
					    		data = state_code;
							}
							
							else if(i == 13) {	//BU_CONTACT_PERSON
								queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
					    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y'";
								stmt=conn.prepareStatement(queryString);
								
								String addr_flag = "";
								if(bu_seq.equals("1")) {								
									addr_flag = "P1";
								}else if(bu_seq.equals("2")){
									addr_flag = "P2";
								}		
	
								stmt.setString(1, addr_flag);
								rset = stmt.executeQuery();
								
					    		if (rset.next()) {				    			
					    			bu_cont=rset.getString(1);
					    		}else {
					    			bu_cont ="0";
					    		}	
					    		
					    		rset.close();
					    		stmt.close();
					    		data=bu_cont;
							}
							
							else if(i == 15) {	//CONTACT_PERSON_CD

					    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					    		
					    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
										
						    	stmt = conn.prepareStatement(queryString);
						    	stmt.setString(1, mail);
						    	stmt.setString(2, cd);
						    	rset = stmt.executeQuery();
						    	
						    	if (rset.next()) {
						    		contact_cd = rset.getString(1);
						    	}
						    	else {
						    		contact_cd = "1";
						    	}
						    	rset.close();
						    	stmt.close();
					    		data=contact_cd;
							}
							
							else if(i == 29) {	//EXCHG_RATE_CD
								exch_rate = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
								queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, exch_rate.toUpperCase());
								rset = stmt.executeQuery();
								if(rset.next()) {
									exch_rate = rset.getString(1);
								}
								else {
									exch_rate = "0";
								}
								rset.close();
								stmt.close();
								
								data = exch_rate;
							}
							
							else if(i == 33) {	//INVOICE RAISED IN
								queryString = "SELECT INVOICE_CUR_CD FROM FMS_SUPPLY_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? "
										+ "AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, cd);
								stmt.setString(3, no);
								stmt.setString(4, rev);
								stmt.setString(5, cont_no);
								stmt.setString(6, cont_rev);
								stmt.setString(7, cont_type);
								stmt.setString(8, plant_seq);
								rset = stmt.executeQuery();
								if(rset.next()) {
									inv_raised = rset.getString(1);
								}
								else {
									inv_raised = "1";
								}
								rset.close();
								stmt.close();
								
								data = inv_raised;
							}
							
							else if(i == 36) {	//TAX_STRUCT_CD

					    		
					    		String temp_struct = "";
					    		tax_dt = "";
								name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@ ", ", ");;
								if(name!=null) {
					    		if (!name.contains(", ")) {
									queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, name);
									rset = stmt.executeQuery();
									if (rset.next()) {
										temp_struct = rset.getString(1);
										tax_dt = rset.getString(2);
									}
									rset.close();
									stmt.close();
								}
					    		else {

									int flag = 0;
									queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, "%"+name.split(", ")[0]+"%");
									rset = stmt.executeQuery();
									
									while (rset.next()) {
										
										for (int j = 0; j < name.split(", ").length; j++) {
											if (rset.getString(2).contains(name.split(", ")[j])) {
												flag = 1;
											}
											else {
												flag = 0;
												break;
											}
										}
										
										if (flag == 1) {
											temp_struct = rset.getString(1);
											name=rset.getString(2);
											tax_dt = rset.getString(3);
											break;
										}
									}
									rset.close();
									stmt.close();
								}
							}
							    	data = temp_struct;
					    	
							}
							
							else if(i == 80) {	//TCS_STRUCT_CD
								queryString = "SELECT TAX_STRUCT_CD  "
										+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TCS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cd);
								stmt.setString(2, "C");
								stmt.setString(3,company_cd);
								rset = stmt.executeQuery();
								if(rset.next()) {
									data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}
							
							else if(i == 82) {	//TDS_STRUCT_CD
								queryString = "SELECT TAX_STRUCT_CD  "
										+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TDS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cd);
								stmt.setString(2, "C");
								stmt.setString(3,company_cd);
								rset = stmt.executeQuery();
								if(rset.next()) {
									data = rset.getString(1);
								}
								else {
									data = null;
								}
								rset.close();
								stmt.close();
							}
							
							else if(i == 84) {	//TAX_EFF_DT
								data = tax_dt;
							}
							
							else if(i == 87) {	//CHKPOST_CD
								queryString = "SELECT A.CHKPOST_CD FROM FMS_LINK_CHECKPOST_PLANT A WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? AND A.ENTITY_TYPE = 'C' AND A.PLANT_SEQ_NO = ? "
										+ "AND A.REV_SEQ = (SELECT MAX(C.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT C WHERE C.COMPANY_CD = A.COMPANY_CD AND C.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
										+ "AND C.ENTITY_TYPE = A.ENTITY_TYPE AND C.PLANT_SEQ_NO = A.PLANT_SEQ_NO AND C.EFF_DT = A.EFF_DT )";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, cd);
								stmt.setString(3, plant_seq);
								rset = stmt.executeQuery();
								if(rset.next()) {
									chk_cd = rset.getString(1);
								}
								rset.close();
								stmt.close();
								
								data = chk_cd;
							}
							
							else if(i == 88) {	//DRIVER_CD
								lic = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								lic = lic.toUpperCase();
								
								if(!lic.equals("0")) {
									queryString="SELECT DRIVER_CD FROM FMS_TRUCK_DRIVER_MST WHERE ENT_PROFILE = ? "
						    				+ " AND UPPER(DRIVER_NAME) = ? ";
									stmt=conn.prepareStatement(queryString);
									stmt.setString(1, company_cd);
									stmt.setString(2, lic);
									rset = stmt.executeQuery();
						    		if (rset.next()) {				    			
						    			driver_cd = rset.getString(1);
						    		}else {
						    			driver_cd  ="0";
						    		}	 
						    		rset.close();
						    		stmt.close();
								}
								else {
									driver_cd = "0";
								}
								
								data = driver_cd;
							}
							
							else {
								if(i == 2) {	//FINANCIAL_YEAR
									fin_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								else if (i == 9) {	// truck_cd
									truck_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								else if(i == 11) {	//BU_UNIT
									bu_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								else if (i == 14) {	// PLANT_SEQ_NO
									plant_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								else if(i == 17) {	//INVOICE_SEQ
									inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								else if(i == 19) {	//INVOICE_NO
									inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								
								
								
								data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
							}
							
//					    	System.out.println(index+" : >>>"+data);
							stmt1.setString(index++, data);
							
						}
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_INVOICE_MST WHERE "
								+ "COMPANY_CD = ? AND FINANCIAL_YEAR = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, fin_year);
						stmt.setString(3, state_code);
						stmt.setString(4, inv_seq);
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !no.equals("")){
							
							logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + plant_seq + "," + bu_seq + "," + inv_dt + "," + fill_cd + "," + bay_cd + "," + slot_st + "," + slot_end + "," + inv_no + "," + trans_cd + "," + truck_cd + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + plant_seq + "," + bu_seq + "," + inv_dt + "," + fill_cd + "," + bay_cd + "," + slot_st + "," + slot_end + "," + inv_no + "," + trans_cd + "," + truck_cd + "," ), conn, "E");
						}
						
						rset.close();
						stmt.close();
					}
					br.close();
				}
				
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
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
	
	
	public void FMS_DLNG_INV_TAX_DTL() throws IOException, SQLException {

		function_nm="FMS_DLNG_INV_TAX_DTL()";
		try {
			
			
			System.out.println("<<START>><<FMS_DLNG_INV_TAX_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DLNG_INV_TAX_DTL>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="",tax_eff_dt_1="";
			
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			
			columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT_1,TAX_DESCR,"
					+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TAX_EFF_DT";
			
			queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,TO_CHAR(TAX_EFF_DT_1, 'dd/mm/yyyy hh24:mi:ss'),"
					+ "NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
					+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss') "
					+ "	FROM FMS_DLNG_INVOICE_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			queryString1 = "INSERT INTO FMS_DLNG_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT_1,TAX_DESCR,TAX_AMT,"
					+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TAX_EFF_DT) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			
			logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
			
			while (rset.next()) {
				tax_struct_cd = rset.getString(5);
				tax_eff_dt_1 =  rset.getString(7);
				String desc1="";
				String count_value="",adv_amt="",gross_amt="";
				
				int count_desc=1;
				adv_amt = "";
				
				for(int j=0;j<count_desc;j++) {
					
					queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, tax_struct_cd);
					rset2 = stmt2.executeQuery();
					
					if(rset2.next()) {
						
						stmt1 = conn.prepareStatement(queryString1);
						
						for(int i = 0;i < columns.split(",").length;i++) {
							data = "";
							data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
							
							if(i == 4) {	//TAX_STRUCT_CD
								if(tax_struct_cd != null) {
									tax_struct_cd = rset.getString(5);
								}
								else {
									tax_struct_cd = "0";
								}
								data = tax_struct_cd;
							}
							else if(i == 5) {
								
								//TAX_CODE
								if (tax_struct_cd != null) {
									
									desc = rset2.getString(1);
									eff_dt = rset2.getString(2); 
									if(desc.contains(", "))
									{
										count_desc=desc.split(", ").length;
										String[] parts = desc.split(", ");
										desc1 = parts[j];
										desc=desc1;
										
									}
//									else {
//										desc = null;
//									}
								
									if (!tax_struct_cd.equals("0")) {
										if(desc!=null)
										{
											desc_nm=desc.split(" ")[0];
										}
										
										queryString3 = "SELECT TAX_CODE FROM FMS_TAX_MST WHERE TAX_ALIAS_CODE = ? ";
										stmt3 = conn.prepareStatement(queryString3);
										stmt3.setString(1, desc_nm);
										
										rset3 = stmt3.executeQuery();
										if (rset3.next()) {
											data = rset3.getString(1);
										}
										else {
											data = "0";
										}
										rset3.close();
										stmt3.close();
									} 
									else {
										data = "0";
									}
								}
								else {
										data = "0";
									}
								tax_code = data;
								
							}
							
							else if(i == 6) {	//TAX_EFF_DT_1
								data = tax_eff_dt_1;
							}
							else if(i == 7) {	//TAX_DESCR
							   if(tax_struct_cd != null) {
									if(!tax_struct_cd.equals("0")) {
										data=desc;
									}
							   }
							    else {
										data = null;
								}
							}
							else if(i==8)
							{
								
								double tax_amt = 0.0;
								if (desc != null) {
								    if ( !desc.contains("on") ) {
								    	
								        count_value = desc.split("%")[0];
								        count_value = count_value.split(" ")[1]; 
//								        else {
								            gross_amt = rset.getString(10);
								            tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100;
								            adv_amt = tax_amt + "";
								            adv=adv_amt;
//								        }
								    }
								    else if (desc.contains("on")) {

								        count_value = desc.split("%")[0];
								        count_value = count_value.split(" ")[1]; 
								        tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100;
								           
								    }
								}
								data = tax_amt + "";

							}
							
							else if(i == 9 && desc != null && desc.contains("on")) {

								data = adv;
							}
							
							else if(i == 14) {
								data = eff_dt;
							}
							
							stmt1.setString(i+1,data);
								
							}
					
					
				//for data already exists..
				queryString5 = "SELECT TAX_AMT FROM FMS_DLNG_INV_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
						+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, rset.getString(2));
				stmt5.setString(3, rset.getString(3));
				stmt5.setString(4, rset.getString(4));
				stmt5.setString(5, rset.getString(5));
				stmt5.setString(6, tax_code);
				rset5 = stmt5.executeQuery();
				
					if (!rset5.next() ) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
					}
					stmt5.close();
					rset5.close();
				}
				rset2.close();
				stmt2.close();
				}
			}
			rset.close();
			stmt.close();

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
		
			System.out.println("<<END>><<FMS_DLNG_INV_TAX_DTL>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INV_TAX_DTL>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

			
		
		}
		catch(Exception e)
		{
			msg = "One of the Functions faced an Error. Data Not Inserted.";
			msg_type = "E";
			
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
		}
	
	}
	
	
	public void FMS_DLNG_INV_FILE_DTL() throws IOException, SQLException {
		function_nm="FMS_DLNG_INV_FILE_DTL()";
		try {
			
			table_name = "FMS_DLNG_INV_FILE_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
			
			queryString1 = "INSERT INTO FMS_DLNG_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,"
					+ "PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT) "
					+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_INV_FILE_DTL_"+start_end_dt+".csv");
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_INV_FILE_DTL_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();
					
					logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						no = ""; rev = ""; seq_no = "" ; 
						abbr = "";
						bu_seq="";inv_seq="";fin_yr="";pdf_type="";file_nm="";sign_by="";sign_by_cd="";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) 
						for (int i = 0; i < line.split(",").length; i++)
						{
//						cell = cellIterator.next();
							data = null;
//						System.out.println(line.split(",")[i]);
							if (i == 14) {	// SIGNED_ENT_BY
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
										queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) = ? ";
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
								}
								else {
									sign_by_cd = "0";
								}
								data = sign_by_cd;
							}
							
							
							else {
								if(i == 1) {	//BU_UNIT
									bu_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								else if(i == 2) {	//INVOICE_SEQ
									inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								else if(i == 3) {	//FINANCIAL_YEAR
									fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								else if (i == 4) {	// PDF_TYPE
									pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								
								else if(i == 5) {	//FILE_NAME
									file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								
								else if(i == 11) {	//SIGNED_BY
									sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								
								data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
							}
							
//					    	System.out.println(index+" : >>>"+data);
							stmt1.setString(index++, data);
							
						}
						
						queryString = "SELECT COMPANY_CD FROM FMS_DLNG_INV_FILE_DTL WHERE "
								+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND PDF_TYPE = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, bu_seq);
						stmt.setString(3, inv_seq);
						stmt.setString(4, fin_yr);
						stmt.setString(5, pdf_type);
						rset = stmt.executeQuery();
						
						if (!rset.next()){
							
							logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
						}
						
						rset.close();
						stmt.close();
					}
					br.close();
				}
				
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
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
	
	
	public void FMS_DLNG_INV_FILE_DTL_UPDATE() throws IOException, SQLException {
		function_nm="FMS_DLNG_INV_FILE_DTL_UPDATE()";
		try {
			
			String file_name ="",file_nm="";
			System.out.println("<<START>><<FMS_DLNG_INV_FILE_DTL_UPDATE>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DLNG_INV_FILE_DTL_UPDATE>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME";
			
			queryString = "SELECT COMPANY_CD, BU_STATE_TIN, INVOICE_SEQ, FINANCIAL_YEAR, PDF_TYPE, FILE_NAME, PDF_SIGNED FROM FMS_DLNG_INV_FILE_DTL WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "UPDATE FMS_DLNG_INV_FILE_DTL SET FILE_NAME = ? "
					+ "WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND PDF_TYPE = ? ";
			
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
			String bu_state_tin="",invoice_seq="",fin_yr="",pdf_type="",cont_desc="";
			while (rset.next()) {
				total_count++;
				
				
				bu_state_tin = rset.getString(2);
				invoice_seq = rset.getString(3);
				fin_yr = rset.getString(4);
				pdf_type = rset.getString(5);
				file_name = rset.getString(6);
				
				if(file_name!=null) {
					if(file_name.contains("-S-")) {
						cont_desc = "SALES_";
					}
					else if(file_name.contains("-L-")) {
						cont_desc = "LOA_";
					}
				}
				
				if (file_name!=null) {
					file_name = file_name.split(".pdf")[0];
				}
				if(!file_name.contains("-"+pdf_type))
				{
					file_name = file_name+"-"+pdf_type;
				}
				
				file_name += ".pdf";
				
				if (file_name!=null) {
					data = file_name;
//					file1 = new File(pdf_path + "dlng_sales_invoice/" + data);
					
					if(rset.getString(7)!=null && rset.getString(7).equals("Y") && !rset.getString(6).contains(cont_desc)) {
						data = cont_desc+file_name;
					}
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, data);
					stmt1.setString(2, company_cd);
					stmt1.setString(3, bu_state_tin);
					stmt1.setString(4, invoice_seq);
					stmt1.setString(5, fin_yr);
					stmt1.setString(6, pdf_type);
					stmt1.executeUpdate();
					stmt1.close();
					
					logger.data(fname, (company_cd + "," + bu_state_tin + "," + invoice_seq + "," + fin_yr + "," + pdf_type + "," + data  + "," ), conn, "");	
					logger_count++;
				}
	
			}
			rset.close();
			stmt.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_DLNG_INV_FILE_DTL_UPDATE>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INV_FILE_DTL_UPDATE>>,", conn);
			
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
	
	
	public void FMS_DLNG_INV_PAY_RECV_DTL() throws IOException, SQLException {
		function_nm="FMS_DLNG_INV_PAY_RECV_DTL()";
		try {
			
			table_name = "FMS_DLNG_INV_PAY_RECV_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			String inv_no = "",bu_state_tin="",inv_seq="",fin_yr="",seq="",pay_recv_dt="";
			
			queryString1 = "INSERT INTO FMS_DLNG_INV_PAY_RECV_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,PAY_RECV_DT,PAY_RECV_AMT,PAY_REMARK,ENT_BY,"
					+ "ENT_DT,ADV_FLAG,ADV_AMT) "
					+ "VALUES(?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_INV_PAY_RECV_DTL_"+start_end_dt+".csv");
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_INV_PAY_RECV_DTL_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();
					
					logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,INVOICE_NO,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						no = ""; rev = ""; seq_no = "" ; 
						abbr = "";
						inv_no="";bu_state_tin="";inv_seq="";fin_yr= "";seq="";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) 
						for (int i = 0; i < line.split(",").length; i++)
						{
//						cell = cellIterator.next();
							data = null;
//						System.out.println(line.split(",")[i]);
							if (i == 0) {	// COMPANY_CD
								abbr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr.toUpperCase());
								rset = stmt.executeQuery();
								if (rset.next()) {
									cd = rset.getString(1);
								} else {
									cd ="0";
								}
								rset.close();
								stmt.close();
								data = company_cd;
							}
							
							else if(i == 1) {	//BU_STATE_TIN
								inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								
								queryString = "SELECT BU_STATE_TIN, INVOICE_SEQ, FINANCIAL_YEAR FROM FMS_DLNG_INVOICE_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND INVOICE_NO = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, company_cd);
								stmt.setString(2, cd);
								stmt.setString(3, inv_no);
								rset = stmt.executeQuery();
								if (rset.next()) {
									bu_state_tin = rset.getString(1);
									inv_seq = rset.getString(2);
									fin_yr = rset.getString(3);
								} else {
									bu_state_tin ="24";
									inv_seq = "0";
									fin_yr = "0";
								}
								rset.close();
								stmt.close();
								data = bu_state_tin;
							}
							
							else if(i == 2) {
								data = inv_seq;
							}
							
							else if(i == 3) {
								data = fin_yr;
							}
							
							
							else {
								if(i == 4) {	//SEQ_NO
									seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								
								if(i == 5) {	//PAY_RECV_DT
									pay_recv_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								
								
								
								data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
							}
							
//					    	System.out.println(index+" : >>>"+data);
							stmt1.setString(index++, data);
							
						}
						
						queryString = "SELECT COMPANY_CD FROM FMS_DLNG_INV_PAY_RECV_DTL WHERE "
								+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND SEQ_NO = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, bu_state_tin);
						stmt.setString(3, inv_seq);
						stmt.setString(4, fin_yr);
						stmt.setString(5, seq);
						rset = stmt.executeQuery();
						
						if (!rset.next() && pay_recv_dt!=null){
							
							logger.data(fname, (company_cd + "," + bu_state_tin + "," + inv_seq + "," + fin_yr + "," + seq + "," + inv_no  + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd + "," + bu_state_tin + "," + inv_seq + "," + fin_yr + "," + seq + "," + inv_no  + "," ), conn, "E");
						}
						
						rset.close();
						stmt.close();
					}
					br.close();
				}
				
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
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
	
public void FMS_DLNG_FFLOW_INV_MST() throws IOException, SQLException {
		
		function_nm="FMS_DLNG_FFLOW_INV_MST()";
		try {
			

			DateUtil utilDate = new DateUtil();
			DataBean_Sales_Invoice bill_freq = new DataBean_Sales_Invoice();
			
			table_name = "FMS_DLNG_FFLOW_INV_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			queryString1 = "INSERT INTO FMS_DLNG_FFLOW_INV_MST(COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "BU_STATE_TIN,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,INVOICE_CATEGORY,FREQ,PERIOD_START_DT,"
					+ "PERIOD_END_DT,DUE_DT,INVOICE_TYPE,LINKED_INVOICE,NUM_LINE,NOTE,GROSS_AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,"
					+ "GROSS_AMT_INR,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,CHECKED_FLAG,CHECKED_BY,"
					+ "CHECKED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,OTHER_INV_STR,AMT_WORD,PDF_INV_DTL,PRINT_BY_ORI,"
					+ "PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,INVOICE_ID_SEQ,TDS_STRUCT_CD,TDS_EFF_DT,TCS_STRUCT_CD,TCS_EFF_DT,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,"
					+ "TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,TCS_FACTOR,TCS_TDS,TCS_AMT,ALLOC_QTY,SUB_INV_TYPE,FIN_SYS,HOLD_AMT,CARGO_NO,SAC_CD)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			Map<String, Integer> invseq = new HashMap<String, Integer>();
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_MST_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_MST_"+start_end_dt+".csv";
				
				try (
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
									String line = br.readLine();
									
									logger.checkpoint(fname, "COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,INVOICE_TYPE,TIMESTAMP,", conn);
									int inv_seq_no=1;
									
									while ((line = br.readLine()) != null) {
										total_count++; 
										String dt="",financial_year="",contact_person_cd="",mail="",billing_eff_dt = "", freq = "", start_dt = "", end_dt = "",tds_per="";
										String addr_flag = "", inv_type="";
										String bu_seq_no ="",bu_plant_state="",state_code="",bu_cont_person_cd="",name="",exchg_cd="";
										String agmt_no = "",agmt_rev = "",seq_no = "";
										abbr = "";
										cd = "0";
										data = null;
										
										index = 1;
										stmt1 = conn.prepareStatement(queryString1);
										
										for (int i = 0; i < line.split(",").length; i++)
									    {	
											data = null;
											
									    	if (i == 0) {
									    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
									    		data = company_cd;
									    	}
									    	else if(i == 1) {
									    		financial_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		data = financial_year;
									    	}
									    	else if (i == 2) {	// Counterparty_Cd
									    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);

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
									    		data = cd;
									    	}
											else if (i == 3) { //Agmt_no
									    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		
									    		 if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
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
										    			billing_eff_dt = rset.getString(6);
										    		} else {
										    			agmt_no = "";
										    			agmt_rev = "";
										    			cont_no = "";
										    			cont_rev = "";
										    			cont_type = "";
										    			billing_eff_dt = "";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		 else if(cont_ref.startsWith("S")) {
									    			 cont_type = cont_ref.split("-")[0];
									    			 if(cont_type.equals("S")) {
															cont_type = "F";
														}
														else {
															cont_type = "E";
														}
									    			 
									    			 queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = ? ";
														stmt = conn.prepareStatement(queryString);
														stmt.setString(1, cd);
														stmt.setString(2, "%@"+cont_ref);
														stmt.setString(3, cont_type);
														
														
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
									    	else if (i == 4) { //Agmt_rev
//									    		if(!cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//									    			agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//									    		}
												data = agmt_rev;
									    	}
									    	else if (i == 5) { //Cont_no
//									    		if(!cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//									    			cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//									    		}
									    		data = cont_no;
									    	}
								    		
									    	else if (i == 6) { //Cont_rev
//									    		if(!cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//									    			cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//									    		}
									    		data = cont_rev;
									    	}
									    	else if (i == 7) { //contract_type
//									    		if(!cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//									    			cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//									    		}
									    		data = cont_type;
									    	}
									    					    	
									    	else if(i == 8) { // BU_SEQ
									    		 bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    		if (bu_seq_no!=null) {
					//								bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
					//							}
												data  = bu_seq_no;
									    	}
									    	else if(i ==9) {
									    		queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
									    				+ " AND COUNTERPARTY_CD='2' AND ENTITY='B' AND SEQ_NO = ?";
												stmt=conn.prepareStatement(queryString);
												stmt.setString(1, bu_seq_no);
												rset = stmt.executeQuery();
									    		if (rset.next()) {				    			
									    			bu_plant_state = rset.getString(1);
									    		}else {
									    			bu_plant_state  ="0";
									    		}	
									    		rset.close();
									    		stmt.close();
									    		
									    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
												stmt=conn.prepareStatement(queryString);
												stmt.setString(1, bu_plant_state);
												rset = stmt.executeQuery();
									    		if (rset.next()) {				    			
									    			state_code = rset.getString(1);
									    		}else {
									    			state_code  ="0";
									    		}	
									    		rset.close();
									    		stmt.close();
									    		
									    		data = state_code;
									    		
									    	}
									    	else if(i == 10) { //BU_CONTACT_PERSON

									    		queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
									    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y' ORDER BY CONTACT_PERSON ";
												stmt=conn.prepareStatement(queryString);
												
												
												if(bu_seq_no.equals("1")) {								
													addr_flag = "P1";
												}else if(bu_seq_no.equals("2")){
													addr_flag = "P2";
												}else if(bu_seq_no.equals("3")){
													addr_flag = "P3";
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
									    	else if (i == 11) {	//addr_flag
//								    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				//				    			if (inv_seq != null) {
				//				    				inv_seq = inv_seq.substring(1, inv_seq.length() - 1);
				//								}
								    			data = addr_flag; 
								    		}
									    	else if(i == 12)
									    	{
									    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		
									    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
														
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, mail);
										    	stmt.setString(2, cd);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		contact_person_cd = rset.getString(1);
										    	}
										    	else {
										    		contact_person_cd = "0";
										    	}
										    	rset.close();
										    	stmt.close();
									    		data=contact_person_cd;
									    	}
									    	else if(i == 13) {
									    		if (invseq.containsKey(financial_year)) {
									    			
									    			inv_seq_no=invseq.get(financial_year);
									    			inv_seq_no=inv_seq_no+1;
													invseq.put(financial_year,inv_seq_no);
													
												} else {
													inv_seq_no=1;
													invseq.put(financial_year,inv_seq_no);
													
												}
									    		data=inv_seq_no+"";
									    	}
									    	else if (i == 17) {

										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										    	freq = data;
										    	
									    	}
									    	else if(i == 26) {
									    		name = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		exchg_cd="";
												queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, name);
										    	rset = stmt.executeQuery();
										    	if (rset.next()) {
										    		exchg_cd = rset.getString(1);
										    	}
										    	else {
										    		exchg_cd = "0";
										    	}
										    	rset.close();
										    	stmt.close();
										    	data = exchg_cd;
									    	}
									    	else if(i == 32) {
									    		
									    		String temp_struct = "";
									    		dt = "";
												name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@ ", ", ");
									    
												if(name!= null) {
													if (!name.contains(", ")) {
														queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
														stmt = conn.prepareStatement(queryString);
														stmt.setString(1, name);
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
														queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
														stmt = conn.prepareStatement(queryString);
														stmt.setString(1, "%"+name.split(", ")[0]+"%");
														rset = stmt.executeQuery();
														
														while (rset.next()) {
															
															for (int j = 0; j < name.split(", ").length; j++) {
																if (rset.getString(2).contains(name.split(", ")[j])) {
																	flag = 1;
																}
																else {
																	flag = 0;
																	break;
																}
															}
															
															if (flag == 1) {
																temp_struct = rset.getString(1);
																name=rset.getString(2);
																dt = rset.getString(3);
																break;
															}
														}
														rset.close();
														stmt.close();
													}
												}
									    		
											   	data = temp_struct;
									    	}
									    	else if(i == 33) {
									    		data = dt;
									    	}
									    	else if(i == 59) {
									    		String tds_desc = line.split(",")[i].contains("null") ? null : line.split(",")[i];
												if(tds_desc!=null) {
													queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1,tds_desc);
													rset = stmt.executeQuery();
													if(rset.next()) {
														data = rset.getString(1);
													}
													else {
														data = null;
													}
													rset.close();
													stmt.close();
												}
									    	}
									    	else if(i == 78) {
									    		if(tds_per!=null) {
									    			data = "TDS";
									    		}else {
									    			data = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
									    		}
									    	}
									    	else if(i == 84) {
									    		if(cont_type.equals("O") || cont_type.equals("Q")) {
//									    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
													
													queryString = "SELECT CARGO_NO FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND INVOICE_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND INVOICE_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND CONTRACT_TYPE = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, cd);
													stmt.setString(2, start_dt);
													stmt.setString(3, end_dt);
													stmt.setString(4, cont_type);
													rset = stmt.executeQuery();
//													System.out.println(cd + "=" + start_dt+"="+end_dt+"="+cont_type);
													if (rset.next()) {
														cargo_no = rset.getString(1);
													}
													else{
														cargo_no="0";
													}
													rset.close();
													stmt.close();
													
									    		}else {
									    			cargo_no = "0";	
									    		}
									    		data  = cargo_no;
									    	}
									    	else if(i == 85) {
									    		String sac_cd="";
									    		
									    		queryString = "SELECT SAC_CD FROM FMS_SAC_MST WHERE ENT_PROFILE = '2' ";
												stmt = conn.prepareStatement(queryString);
												rset = stmt.executeQuery();
												if (rset.next()) {
													sac_cd = rset.getString(1);
												}
												else{
													sac_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
												}
												rset.close();
												stmt.close();
												data = sac_cd;
									    	}
									    	
									    	else {			    	
									    		
									    	    if (i == 21) {	//invoice_type
									    			inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}
									    		if(i == 18) {
									    			start_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}
									    		if(i == 19) {
									    			end_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}
									    		if(i == 75) {
									    			tds_per = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}
									    		
										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    	}	
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);		    	
									    }
										
									    queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_FFLOW_INV_MST WHERE COMPANY_CD = ? "
									    		+ "AND FINANCIAL_YEAR = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ?";
								    	stmt = conn.prepareStatement(queryString);
								    	stmt.setString(1, company_cd);
								    	stmt.setString(2, financial_year);
								    	stmt.setString(3, state_code);
								    	stmt.setInt(4, inv_seq_no);
								    	stmt.setString(5, inv_type);
								    	
								    	rset = stmt.executeQuery();
								    	
								    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") ) {
												//System.out.println(queryString1);
												
												logger.data(fname, (company_cd+ "," + financial_year + "," + state_code + "," + inv_seq_no + "," + inv_type + ","), conn, "");
												stmt1.executeUpdate();
												stmt1.close();
												
												logger_count++;
										}
								    	 else {
												stmt1.close();
												skipped_count++;     
												logger.data(fname, (company_cd+ "," + financial_year + "," + state_code + "," + inv_seq_no + "," + inv_type + ","), conn, "E");
												
												if (invseq.containsKey(financial_year)) {
													inv_seq_no = invseq.get(financial_year);
													inv_seq_no=inv_seq_no-1;
													invseq.put(financial_year, inv_seq_no);
												}
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
			
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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


public void FMS_DLNG_FFLOW_INV_TAX_DTL() throws IOException, SQLException {

	function_nm="FMS_DLNG_FFLOW_INV_TAX_DTL()";
	try {
		
		
		System.out.println("<<START>><<FMS_DLNG_FFLOW_INV_TAX_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_DLNG_FFLOW_INV_TAX_DTL>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,NULL,TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss'),"
				+ "NULL,TAX_AMT,GROSS_AMT_INR,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
				+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),GROSS_AMT_INR"
				+ "	FROM FMS_DLNG_FFLOW_INV_MST WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_DLNG_FFLOW_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,"
				+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,TAX_CODE,", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(6);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			
			int count_desc=1;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						if(i == 5) {	//TAX_STRUCT_CD
							if(tax_struct_cd != null) {
								tax_struct_cd = rset.getString(6);
							}
							else {
								tax_struct_cd = "0";
							}
							data = tax_struct_cd;
						}
						else if(i == 6) {
							//TAX_CODE
							if (tax_struct_cd != null) {
								
								desc = rset2.getString(1);
								eff_dt = rset2.getString(2); 
								if(desc.contains(", "))
								{
									count_desc=desc.split(", ").length;
									String[] parts = desc.split(", ");
									desc1 = parts[j];
									desc=desc1;
									
								}
//								else {
//									desc = null;
//								}
								if (!tax_struct_cd.equals("0")) {
									if(desc!=null)
									{
										desc_nm=desc.split(" ")[0];
									}
									
									queryString3 = "SELECT TAX_CODE FROM FMS_TAX_MST WHERE TAX_ALIAS_CODE = ? ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, desc_nm);
									
									rset3 = stmt3.executeQuery();
									if (rset3.next()) {
										data = rset3.getString(1);
									}
									else {
										data = "0";
									}
									rset3.close();
									stmt3.close();
								} 
								else {
									data = "0";
								}
							}
							else {
									data = "0";
								}
							tax_code = data;
							
						}
						
						else if(i == 7) {	//TAX_EFF_DT
							data = eff_dt;
						}
						else if(i == 8) {	//TAX_DESCR
						   if(tax_struct_cd != null) {
								if(!tax_struct_cd.equals("0")) {
									data=desc;
								}
						   }
						    else {
									data = null;
							}
						}
						else if(i==9)
						{
							
							double tax_amt = 0.0;
							if (desc != null) {
							    if ( !desc.contains("on") ) {
							    	
							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
//							        else {
							            gross_amt = rset.getString(16);
							            tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100);
							            adv_amt = tax_amt + "";
							            adv=adv_amt;
//							        }
							    }
							    else if (desc.contains("on")) {

							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
							        tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100);
							           
							    }
							}
							data = tax_amt + "";

						}
						
						else if(i == 10 && desc != null && desc.contains("on")) {

							data = adv;
						}
						
//						System.out.println(i+1 +"=="+ data );
						stmt1.setString(i+1,data);
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_DLNG_FFLOW_INV_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? AND INVOICE_TYPE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(6));
			stmt5.setString(6, tax_code);
			stmt5.setString(7, rset.getString(5));
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4)+ "," + rset.getString(5) + " , " + rset.getString(6) + " , " +tax_code+ " , "), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(5) + " , " + rset.getString(6) + " , " +tax_code+ " , "), conn, "E");
					
				}
				stmt5.close();
				rset5.close();
			}
			rset2.close();
			stmt2.close();
			}
		}
		rset.close();
		stmt.close();

		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
	
		System.out.println("<<END>><<FMS_DLNG_FFLOW_INV_TAX_DTL>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_FFLOW_INV_TAX_DTL>>,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);

		
	
	}
	catch(Exception e)
	{
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
	finally {
		conn.commit();
	}
	
}

public void FMS_DLNG_FFLOW_INV_DTL() throws IOException, SQLException {
	
	function_nm="FMS_DLNG_FFLOW_INV_DTL()";
	try {
		
		table_name = "FMS_DLNG_FFLOW_INV_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 

		String line_no="",inv_type="",bu_seq_no="",agmt_no="",fin_yr="",state_code="",inv_seq="",cont_cd="",bu_cont_person_cd="",agmt_rev="";
		String addr_flag="", new_inv="", bu_plant_state="",sac_cd="";
		
		queryString1 = "INSERT INTO FMS_DLNG_FFLOW_INV_DTL (COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
				+ "BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_TYPE,INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,UNIT,"
				+ "QTY,RATE,AMOUNT,ENT_BY,ENT_DT,CARGO_NO,SAC_CD) "
		        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
		        + "?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?)";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_DTL_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_DTL_"+start_end_dt+".csv";
			try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_DTL_"+start_end_dt+".xlsx"));
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				//				if (rowIterator.hasNext()) {	// For skipping the first row
				//					rowIterator.next();
				//				}
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_TYPE,INVOICE_SEQ,LINE_NO,TIMESTAMP,", conn);
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									agmt_no = ""; agmt_rev = ""; seq_no = "";
									abbr = "";
									cd = "0";
									data = null;
									
									index = 1;
									stmt1 = conn.prepareStatement(queryString1);
									for (int i = 0; i < line.split(",").length; i++)
								    {	
				//				    	cell = cellIterator.next();
										data = null;
										
								    	if (i == 0) {
								    		
								    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
											data = company_cd;
								    	}
								    	else if (i == 1) {	// Counterparty_Cd
								    		
								    		fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
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
								    		data = fin_yr;
								    	}
								    	else if (i == 2) {	//cd
								    		cont_ref = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		
											data = cd;
								    	}
										else if (i == 3) { //Agmt_no
											
								    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//				    		
								    		if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
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
								    		}
								    		else if(cont_ref.startsWith("S")) {
								    			
								    			cont_type = cont_ref.split("-")[0];
												if(cont_type.equals("S")) {
													cont_type = "F";
												}
												else {
													cont_type = "E";
												}
												queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = ? ";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, cd);
												stmt.setString(2, "%@"+cont_ref);
												stmt.setString(3, cont_type);
												
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
								    	else if (i == 4) { //Agmt_rev
//								    		if(!cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//									    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//								    		}
											data = agmt_rev;
								    	}
								    	else if (i == 5) { //Cont_no
//								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//								    			cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//								    		}
								    		data = cont_no;
								    	}
								    	else if (i == 6) { //Cont_rev
//								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//								    			cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//								    		}
								    		data = cont_rev;
								    	}
								    	else if (i == 7) { //contract_type
//								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//								    			cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//								    		}
								    		data = cont_type;
								    	}
								    					    	
								    	else if(i == 8) { // BU_SEQ
								    		
								    		queryString = "SELECT BU_UNIT FROM FMS_FFLOW_INV_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
								    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
								    		stmt = conn.prepareStatement(queryString);
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
								    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
				
								    		rset.close();
								    		stmt.close();		    		
								    		data  = bu_seq_no;
								    	}
								    	else if(i ==9) {
								    		
								    		queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
								    				+ " AND COUNTERPARTY_CD='2' AND ENTITY='B' AND SEQ_NO = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_seq_no);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			bu_plant_state = rset.getString(1);
								    		}else {
								    			bu_plant_state  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_plant_state);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			state_code = rset.getString(1);
								    		}else {
								    			state_code  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		data = state_code;
								    		
								    	}
								    	else if(i == 10) {
								    		new_inv = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
								    		queryString = "SELECT BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,CARGO_NO,SAC_CD FROM FMS_DLNG_FFLOW_INV_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
								    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND INVOICE_NO = ?";
								    		stmt = conn.prepareStatement(queryString);
								    		stmt.setString(1, cd);
								    		stmt.setString(2, agmt_no);
								    		stmt.setString(3, agmt_rev);
								    		stmt.setString(4, cont_no);
								    		stmt.setString(5, cont_rev);
								    		stmt.setString(6, cont_type);
								    		stmt.setString(7, new_inv);
								    		rset = stmt.executeQuery();
								    		
								    		if (rset.next()) {
								    			bu_cont_person_cd =rset.getString(1);
								    			addr_flag = rset.getString(2);
								    			cont_cd = rset.getString(3);
								    			inv_seq = rset.getString(4);
								    			cargo_no = rset.getString(5);
								    			sac_cd = rset.getString(6);
								    			
								    		}else {
								    			bu_cont_person_cd ="0";
								    			addr_flag = null;
								    			cont_cd = null;
								    			inv_seq = null;
								    			cargo_no ="0";
								    		}
								    		rset.close();
								    		stmt.close();	
								    		data  = bu_cont_person_cd;
								    		
								    	}
								    	else if(i == 11) { // INVOICE_SEQ
								    		data  = addr_flag;
								    		
								    	}
								    	else if(i == 12) { // SALE_PRICE
								    		data  = cont_cd;
								    	}
								    	else if(i == 14) { // SALE_PRICE_UNIT
								    		data  = inv_seq;
								    	}
								    	else if(i == 24) { // SALE_PRICE_UNIT
								    		data  = cargo_no;
								    	}
								    	else if(i == 25) {
								    		data  = sac_cd;
								    	}
								    	else {			    	
								    		if (i == 13) {	//alloc_dt
								    			inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		
								    		if (i == 16) {	//alloc_dt
								    			line_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		
								    		
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    	}	
//								    	System.out.println(index+"-"+data);
								    	stmt1.setString(index++, data);			    	
								    }
									
								    queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_FFLOW_INV_DTL WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
								    		+ "AND BU_STATE_TIN = ?  AND INVOICE_TYPE = ? AND INVOICE_SEQ = ? AND LINE_NO = ? ";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, fin_yr);
							    	stmt.setString(3, state_code);
							    	stmt.setString(4, inv_type);
							    	stmt.setString(5, inv_seq);
							    	stmt.setString(6, line_no);
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !bu_seq_no.equals("")) {
										//System.out.println(queryString1);
											
										logger.data(fname, (company_cd+"," + cd + " , " + fin_yr + " , " + state_code + "," + inv_type + "," + inv_seq + "," + line_no + ","), conn, "");
										
										stmt1.executeUpdate();
										stmt1.close();
										
										logger_count++;
									}
							    	 else {
										stmt1.close();
										skipped_count++;     
										logger.data(fname, (company_cd+"," + cd + " , " + fin_yr + " , " + state_code + "," + inv_type + "," + inv_seq + "," + line_no + ","), conn, "E");
									}
							    	 
							    	 rset.close();
									 stmt.close();
								}
			}
			
			// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";		
	
		}
		
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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


public void FMS_DLNG_FFLOW_INV_FILE_DTL() throws IOException, SQLException {
	function_nm="FMS_DLNG_FFLOW_INV_FILE_DTL()";
	try {
		
		table_name = "FMS_DLNG_FFLOW_INV_FILE_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
		
		queryString1 = "INSERT INTO FMS_DLNG_FFLOW_INV_FILE_DTL(COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,INVOICE_TYPE,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,"
				+ "PDF_SIGNED,SIGNED_BY,SIGNED_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_FILE_DTL_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_FILE_DTL_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	

				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String new_inv="", inv_type="";
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
							data = fin_yr;
						}
						else if(i == 2) {
							inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN,INVOICE_TYPE FROM FMS_DLNG_FFLOW_INV_MST WHERE INVOICE_ID_SEQ = ? AND FINANCIAL_YEAR = ? AND COUNTERPARTY_CD = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, inv_seq);
							stmt.setString(2, fin_yr);
							stmt.setString(3, cd);
							rset = stmt.executeQuery();
							if (rset.next()) {
								inv_seq = rset.getString(1);
								bu_seq = rset.getString(2);
								inv_type = rset.getString(3);
							} else {
								inv_seq ="0";
								bu_seq ="0";
								inv_type ="DI";
							}
							rset.close();
							stmt.close();
							
							data = bu_seq;
						}
						
						else if(i == 3) {
							data = inv_seq;
						}
						else if(i == 4) {
							data = inv_type;
						}
//						else if (i == 15) {	// SIGNED_ENT_BY
//							if(sign_by!=null) {
//								if(sign_by.contains("@")) {
//									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMAIL_ID) = ? ";
//									stmt = conn.prepareStatement(queryString);
//									stmt.setString(1, sign_by.toUpperCase());
//									rset = stmt.executeQuery();
//									if (rset.next()) {
//										sign_by_cd = rset.getString(1);
//									} else {
//										sign_by_cd ="0";
//									}
//									rset.close();
//									stmt.close();
//								}
//								else {
//									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) = ? ";
//									stmt = conn.prepareStatement(queryString);
//									stmt.setString(1, sign_by.toUpperCase());
//									rset = stmt.executeQuery();
//									if (rset.next()) {
//										sign_by_cd = rset.getString(1);
//									} else {
//										sign_by_cd ="0";
//									}
//									rset.close();
//									stmt.close();
//								}
//							}
//							else {
//								sign_by_cd = "0";
//							}
//							data = sign_by_cd;
//						}
						
						
						else {
							 if (i == 5) {	// PDF_TYPE
								pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 6) {	//FILE_NAME
								file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 12) {	//SIGNED_BY
								sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_DLNG_FFLOW_INV_FILE_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND PDF_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, inv_seq);
					stmt.setString(4, fin_yr);
					stmt.setString(5, inv_type);
					stmt.setString(6, pdf_type);
					rset = stmt.executeQuery();
					
					if (!rset.next()){
						
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
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


public void FMS_DLNG_FFLOW_INV_FILE_DTL_UPDATE() throws IOException, SQLException {
	function_nm="FMS_DLNG_FFLOW_INV_FILE_DTL_UPDATE()";
	try {
		
		String file_name ="",file_nm="";
		System.out.println("<<START>><<FMS_DLNG_FFLOW_INV_FILE_DTL_UPDATE>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_DLNG_FFLOW_INV_FILE_DTL_UPDATE>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,INVOICE_TYPE";
		
		queryString = "SELECT COMPANY_CD, BU_STATE_TIN, INVOICE_SEQ, FINANCIAL_YEAR, PDF_TYPE, FILE_NAME, INVOICE_TYPE, PDF_SIGNED FROM FMS_DLNG_FFLOW_INV_FILE_DTL WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		
		queryString1 = "UPDATE FMS_DLNG_FFLOW_INV_FILE_DTL SET FILE_NAME = ? "
				+ "WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND PDF_TYPE = ? AND INVOICE_TYPE = ? ";
		
		
		logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,", conn);
		String bu_state_tin="",invoice_seq="",fin_yr="",pdf_type="",cont_desc="",invoice_type="";
		while (rset.next()) {
			total_count++;
			
			
			bu_state_tin = rset.getString(2);
			invoice_seq = rset.getString(3);
			fin_yr = rset.getString(4);
			pdf_type = rset.getString(5);
			file_name = rset.getString(6);
			invoice_type = rset.getString(7);
			
			if(file_name.contains("-I-") || file_name.contains("-M-")) {
				cont_desc = "LATEPAY_";
			}
			else if(file_name.contains("-E-")) {
				cont_desc = "DEFICIENCY_";
			}
			
			if (file_name!=null) {
				file_name = file_name.split(".pdf")[0];
			}
			if(!file_name.contains("-"+pdf_type))
			{
				file_name = file_name+"-"+pdf_type;
			}
			
			file_name += ".pdf";
			
			if (file_name!=null) {
				data = file_name;
//				file1 = new File(pdf_path + "dlng_freeflow_invoice/" + data);
				
				if(rset.getString(8)!=null && rset.getString(8).equals("Y") && !rset.getString(6).contains(cont_desc)) {
					data = cont_desc+file_name;
				}
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, data);
				stmt1.setString(2, company_cd);
				stmt1.setString(3, bu_state_tin);
				stmt1.setString(4, invoice_seq);
				stmt1.setString(5, fin_yr);
				stmt1.setString(6, pdf_type);
				stmt1.setString(7, invoice_type);
				stmt1.executeUpdate();
				stmt1.close();
				
				logger.data(fname, (company_cd + "," + bu_state_tin + "," + invoice_seq + "," + fin_yr + "," + pdf_type + "," + data  + "," + invoice_type + ","), conn, "");	
				logger_count++;
			}

		}
		rset.close();
		stmt.close();
		
		

		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
		System.out.println("<<END>><<FMS_DLNG_FFLOW_INV_FILE_DTL_UPDATE>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_FFLOW_INV_FILE_DTL_UPDATE>>,", conn);
		
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



public void FMS_SVC_CONT_BILLING_DTL() throws IOException, SQLException {

	function_nm="FMS_SVC_CONT_BILLING_DTL()";
	try {
		
		
		System.out.println("<<START>><<FMS_SVC_CONT_BILLING_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_SVC_CONT_BILLING_DTL>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String sell_cont_map="",plant_seq="";
		
		columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,"
				+ "INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,"
				+ "DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE,EFF_DT";
		
		queryString = "SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,'F','B',NULL,NULL,'1','1',NULL,NULL,NULL,"
				+ "NULL,NULL,NULL,NULL,NULL,TO_CHAR(A.START_DT,'DD/MM/YYYY'),A.ENT_BY,"
				+ "NULL,NULL, NULL,NULL,NULL, NULL,NULL,A.PLANT_SEQ_NO,NULL,TO_CHAR(A.START_DT,'DD/MM/YYYY')  "
				+ "	FROM FMS_SVC_CONT_MST A WHERE A.COMPANY_CD = ? AND A.CONT_REV = (SELECT MAX(C.CONT_REV) FROM FMS_SVC_CONT_MST C WHERE "
				+ "A.COMPANY_CD = C.COMPANY_CD AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND A.AGMT_NO = C.AGMT_NO AND A.AGMT_REV = C.AGMT_REV "
				+ "AND A.CONT_NO = C.CONT_NO AND A.PLANT_SEQ_NO = C.PLANT_SEQ_NO)";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_SVC_CONT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
				+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,"
				+ "TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,"
				+ "BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE,EFF_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,"
				+ "?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'))";
		
		logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,EFF_DT,", conn);
		
		while (rset.next()) {
			stmt1 = conn.prepareStatement(queryString1);
			String due_dt="",second_due_dt="",int_cd="",int_sign="",int_per="",due_dt_in="",state="",eff_dt="";
			cd = rset.getString(2);
			no = rset.getString(3);
			rev = rset.getString(4);
			cont_no = rset.getString(5);
			cont_rev = rset.getString(6);
			type = rset.getString(7);
			plant_seq = rset.getString(31);
			eff_dt = rset.getString(33);
			
			queryString2 = "SELECT SELL_CONT_MAP "
					+ "FROM FMS_SVC_CONT_MAP "
					+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, company_cd);
			stmt2.setString(2, cd);
			stmt2.setString(3, no);
			stmt2.setString(4, rev);
			stmt2.setString(5, cont_no);
			stmt2.setString(6, cont_rev);
			stmt2.setString(7, type);
			rset2 = stmt2.executeQuery();
			if(rset2.next()) {
				sell_cont_map = rset2.getString(1);
			}
			rset2.close();
			stmt2.close();  
			
			
			queryString3 = "SELECT A.DUE_DATE,A.SECOND_DUE_DT,A.INT_CAL_RATE_CD,A.INT_CAL_SIGN,A.INT_CAL_PERCENTAGE,A.DUE_DT_IN,A.HOLIDAY_STATE "
					+ "FROM FMS_SUPPLY_BILLING_DTL A "
					+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? AND A.CONTRACT_TYPE = ? AND A.AGMT_NO = ? AND A.AGMT_REV = ? AND A.CONT_NO = ? "
//					+ "AND A.CONT_REV = ? "
					+ "AND A.PLANT_SEQ_NO = ? "
					+ "AND A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL C WHERE A.COMPANY_CD = C.COMPANY_CD AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND A.AGMT_NO = C.AGMT_NO "
					+ "AND A.AGMT_REV = C.AGMT_REV AND A.CONT_NO = C.CONT_NO AND A.CONT_REV = C.CONT_REV AND A.PLANT_SEQ_NO = C.PLANT_SEQ_NO)";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, company_cd);
			stmt3.setString(2, cd);
			stmt3.setString(3, sell_cont_map.split("-")[0]);
			stmt3.setString(4, sell_cont_map.split("-")[1]);
			stmt3.setString(5, sell_cont_map.split("-")[2]);
			stmt3.setString(6, sell_cont_map.split("-")[3]);
//			stmt3.setString(7, sell_cont_map.split("-")[4]);
			stmt3.setString(7, plant_seq);
			rset3 = stmt3.executeQuery();
			
			if(rset3.next()) {
				due_dt = rset3.getString(1);
				second_due_dt = rset3.getString(2);
				int_cd = rset3.getString(3);
				int_sign = rset3.getString(4);
				int_per = rset3.getString(5);
				due_dt_in = rset3.getString(6);
				state = rset3.getString(7);
			}
			rset3.close();
			stmt3.close();
			
			
			for(int i = 0;i < columns.split(",").length;i++) {
				data = "";
				data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
				
				if(i == 9) {	//DUE_DATE
					data = due_dt;
				}
				
				else if(i == 10) {	//SECOND_DUE_DT
					data = second_due_dt;
				}
				
				else if(i == 13) {	//INT_CAL_RATE_CD
					data = int_cd;
				}
				
				else if(i == 14) {	//INT_CAL_SIGN
					data = int_sign;
				}
				
				else if(i == 15) {	//INT_CAL_PERCENTAGE
					data = int_per;
				}
				
				else if(i == 25) {	//DUE_DT_IN
					data = due_dt_in;
				}
				
				else if(i == 31) {	//DUE_DT_IN
					data = state;
				}
				
				stmt1.setString(i+1,data);
			}				
				
			//for data already exists..
			queryString5 = "SELECT COMPANY_CD "
					+ "FROM FMS_SVC_CONT_BILLING_DTL "
					+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? "
					+ "AND CONT_NO = ? AND CONT_REV = ? AND PLANT_SEQ_NO = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY')";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			stmt5.setString(3, type);
			stmt5.setString(4, no);
			stmt5.setString(5, rev);
			stmt5.setString(6, cont_no);
			stmt5.setString(7, cont_rev);
			stmt5.setString(8, plant_seq);
			stmt5.setString(9, eff_dt);
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + cd + "," + no + "," + rev+ "," + cont_no + "," + cont_rev + "," + type + "," + plant_seq + "," + eff_dt + "," ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + cd + "," + no + "," + rev+ "," + cont_no + "," + cont_rev + "," + type + "," + plant_seq + "," + eff_dt + "," ), conn, "E");
					
				}
				stmt5.close();
				rset5.close();
		}
		rset.close();
		stmt.close();

		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
	
		System.out.println("<<END>><<FMS_SVC_CONT_BILLING_DTL>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_SVC_CONT_BILLING_DTL>>,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);

		
	
	}
	catch(Exception e)
	{
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
	finally {
		conn.commit();
	}

}


public void FMS_DLNG_SVC_INVOICE_MST() throws IOException, SQLException { 
	function_nm="FMS_DLNG_SVC_INVOICE_MST()";
	try {
		
		table_name = "FMS_DLNG_SVC_INVOICE_MST";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		
		queryString1 = "INSERT INTO FMS_DLNG_SVC_INVOICE_MST(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SELL_CONT_MAP,BU_UNIT,"
				+ "BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INV_FLAG,INVOICE_SEQ,INVOICE_ID_SEQ,INVOICE_NO,INVOICE_DT,FREQ,"
				+ "PERIOD_START_DT,PERIOD_END_DT,DUE_DT,QTY,QTY_UNIT,DIST_UNIT,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,"
				+ "EXCHG_RATE_VALUE,EXCHG_RATE_TYPE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,"
				+ "INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,ITEM_DESCRIPTION,REMARK_1,REMARK_2,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,"
				+ "APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TCS_TDS,TCS_AMT,TCS_FACTOR,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,"
				+ "PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,"
				+ "PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,TCS_STRUCT_CD,TDS_STRUCT_CD,"
				+ "FIN_SYS,HOLD_AMT,SAC_CD) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
				
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
				+ "?,?,?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_SVC_INVOICE_MST_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_SVC_INVOICE_MST_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	
				//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
				// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
//			if (rowIterator.hasNext()) {	// For skipping the first row
//				rowIterator.next();
//			}
				String line = br.readLine();
				
				logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,BU_SEQ,INVOICE_DT,INVOICE_NO,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String bu_seq="",inv_dt="",bu_state="",state_code="",contact_cd="",sell_cont_map="",tcs_tds="",tds_per="";
					String bu_cont="",exch_rate="",plant_seq="",inv_raised="",name="",fin_year="",inv_seq="",inv_no="",mail="";
					no = ""; rev = ""; seq_no = "" ; 
					abbr = "";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++)
					{
//					cell = cellIterator.next();
						data = null;
//					System.out.println(line.split(",")[i]);
						if (i == 0) {	// Counterparty_Abbr, Company Code 
							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//			    		if (!abbr.equals("NULL")) {
//							abbr = abbr.substring(1, abbr.length() - 1);
//						}
							data = company_cd;
						}
						else if (i == 1) {	// Counterparty_Cd
							cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, abbr.toUpperCase());
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
						else if (i == 3) { //Agmt_no
//							no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//			    		if (no != null) {
//							no = no.substring(1, no.length() - 1);
//						}
							queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SVC_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONT_REF_NO = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, cont_ref);
							
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
						else if (i == 4) { //Agmt_rev
							data = rev;
						}
						else if (i == 5) { //Cont_no
							data = cont_no;
						}
						else if (i == 6) { //Cont_rev
							data = cont_rev;
						}
						
						else if (i == 7) { //contract_type
							data = cont_type;
						}
						
						else if(i == 8) {	//SELL_CONT_MAP
							queryString = "SELECT SELL_CONT_MAP FROM FMS_SVC_CONT_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? "
									+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, no);
							stmt.setString(4, rev);
							stmt.setString(5, cont_no);
							stmt.setString(6, cont_rev);
							stmt.setString(7, cont_type);
							rset = stmt.executeQuery();
							if(rset.next()) {
								sell_cont_map = rset.getString(1);
							}
							stmt.close();
							rset.close();
							
							data = sell_cont_map;
						}
						
						else if(i == 10) {	//BU_STATE_TIN
							queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
				    				+ " AND COUNTERPARTY_CD= '2' AND ENTITY='B' AND SEQ_NO = ?";
							stmt=conn.prepareStatement(queryString);
							stmt.setString(1, bu_seq);
							rset = stmt.executeQuery();
				    		if (rset.next()) {				    			
				    			bu_state = rset.getString(1);
				    		}else {
				    			bu_state  ="0";
				    		}	
				    		rset.close();
				    		stmt.close();
				    		
				    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
							stmt=conn.prepareStatement(queryString);
							stmt.setString(1, bu_state);
							rset = stmt.executeQuery();
				    		if (rset.next()) {				    			
				    			state_code = rset.getString(1);
				    		}else {
				    			state_code  ="0";
				    		}	
				    		rset.close();
				    		stmt.close();
				    		
				    		data = state_code;
						}
						
						else if(i == 11) {	//BU_CONTACT_PERSON
							queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
				    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y'";
							stmt=conn.prepareStatement(queryString);
							
							String addr_flag = "";
							if(bu_seq.equals("1")) {								
								addr_flag = "P1";
							}else if(bu_seq.equals("2")){
								addr_flag = "P2";
							}		

							stmt.setString(1, addr_flag);
							rset = stmt.executeQuery();
							
				    		if (rset.next()) {				    			
				    			bu_cont=rset.getString(1);
				    		}else {
				    			bu_cont ="0";
				    		}	
				    		
				    		rset.close();
				    		stmt.close();
				    		data=bu_cont;
						}
						
						else if(i == 13) {	//CONTACT_PERSON_CD

				    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    		
				    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
									
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, mail);
					    	stmt.setString(2, cd);
					    	rset = stmt.executeQuery();
					    	
					    	if (rset.next()) {
					    		contact_cd = rset.getString(1);
					    	}
					    	else {
					    		contact_cd = "1";
					    	}
					    	rset.close();
					    	stmt.close();
				    		data=contact_cd;
						}
						
						else if(i == 29) {	//EXCHG_RATE_CD
							exch_rate = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, exch_rate.toUpperCase());
							rset = stmt.executeQuery();
							if(rset.next()) {
								exch_rate = rset.getString(1);
							}
							else {
								exch_rate = null;
							}
							rset.close();
							stmt.close();
							
							data = exch_rate;
						}
						
						else if(i == 33) {	//INVOICE RAISED IN
							queryString = "SELECT INVOICE_CUR_CD FROM FMS_SVC_CONT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? "
									+ "AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, no);
							stmt.setString(4, rev);
							stmt.setString(5, cont_no);
							stmt.setString(6, cont_rev);
							stmt.setString(7, cont_type);
							stmt.setString(8, plant_seq);
							rset = stmt.executeQuery();
							if(rset.next()) {
								inv_raised = rset.getString(1);
							}
							else {
								inv_raised = "1";
							}
							rset.close();
							stmt.close();
							
							data = inv_raised;
						}
						
						else if(i == 36) {	//TAX_STRUCT_CD

				    		
				    		String temp_struct = "";
							name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@ ", ", ");;
							if(name!=null) {
				    		if (!name.contains(", ")) {
								queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, name);
								rset = stmt.executeQuery();
								if (rset.next()) {
									temp_struct = rset.getString(1);
								}
								rset.close();
								stmt.close();
							}
				    		else {

								int flag = 0;
								queryString = "SELECT TAX_STR_CD, DESCR FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, "%"+name.split(", ")[0]+"%");
								rset = stmt.executeQuery();
								
								while (rset.next()) {
									
									for (int j = 0; j < name.split(", ").length; j++) {
										if (rset.getString(2).contains(name.split(", ")[j])) {
											flag = 1;
										}
										else {
											flag = 0;
											break;
										}
									}
									
									if (flag == 1) {
										temp_struct = rset.getString(1);
										name=rset.getString(2);
										break;
									}
								}
								rset.close();
								stmt.close();
							}
						}
						    	data = temp_struct;
				    	
						}
						
						else if(i == 70) {	//PDF_INV_DTL
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							if(data!=null) {
								data = data.substring(data.length()-1,data.length());
							}
						}
						
						else if(i == 80) {	//TCS_STRUCT_CD
							queryString = "SELECT TAX_STRUCT_CD "
									+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TCS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							stmt.setString(2, "C");
							stmt.setString(3,company_cd);
							rset = stmt.executeQuery();
							if(rset.next() && tcs_tds.equals("TCS")) {
								data = rset.getString(1);
							}
							else {
								data = null;
							}
							rset.close();
							stmt.close();
						}
						
						else if(i == 81) {	//TDS_STRUCT_CD
//							if(tds_per!=null && tds_per.equals("0.1")) {
//								tds_per = ".1"; 
//							}
							queryString = "SELECT TAX_STRUCT_CD FROM FMS_ENTITY_TCS_TDS_MST "
									+ "WHERE TAX_APP = 'TDS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y' ";
//									+ "AND TAX_STRUCT_DTL LIKE ?";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							stmt.setString(2, "C");
							stmt.setString(3,company_cd);
//							stmt.setString(4,"%"+tds_per+"%");
							rset = stmt.executeQuery();
							if(rset.next() && tcs_tds.equals("TDS")) {
								data = rset.getString(1);
							}
							else {
								data = null;
							}
							rset.close();
							stmt.close();
						}
						
						else {
							if(i == 2) {	//FINANCIAL_YEAR
								fin_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 9) {	//BU_UNIT
								bu_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if (i == 12) {	// PLANT_SEQ_NO
								plant_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 15) {	//INVOICE_SEQ
								inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 17) {	//INVOICE_NO
								inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 56) {	//TCS_TDS
								tcs_tds = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 66) {	//TDS_GROSS_PERCENT
								tds_per = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) { 
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_SVC_INVOICE_MST WHERE "
							+ "COMPANY_CD = ? AND FINANCIAL_YEAR = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, fin_year);
					stmt.setString(3, state_code);
					stmt.setString(4, inv_seq);
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("") && !no.equals("")){
						logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,BU_SEQ,INVOICE_DT,INVOICE_NO,TIMESTAMP,", conn);
						
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + plant_seq + "," + bu_seq + "," + inv_dt + "," + inv_no + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + plant_seq + "," + bu_seq + "," + inv_dt + "," + inv_no + "," ), conn, "E");
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


public void FMS_DLNG_SVC_INV_TAX_DTL() throws IOException, SQLException {

	function_nm="FMS_DLNG_SVC_INV_TAX_DTL()";
	try {
		
		
		System.out.println("<<START>><<FMS_DLNG_SVC_INV_TAX_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_DLNG_SVC_INV_TAX_DTL>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,"
				+ "NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
				+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss') "
				+ "	FROM FMS_DLNG_SVC_INVOICE_MST WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_DLNG_SVC_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_DESCR,TAX_AMT,"
				+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(5);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			
			int count_desc=1;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						if(i == 4) {	//TAX_STRUCT_CD
							if(tax_struct_cd != null) {
								tax_struct_cd = rset.getString(5);
							}
							else {
								tax_struct_cd = "0";
							}
							data = tax_struct_cd;
						}
						else if(i == 5) {
							
							//TAX_CODE
							if (tax_struct_cd != null) {
								
								desc = rset2.getString(1);
								if(desc.contains(", "))
								{
									count_desc=desc.split(", ").length;
									String[] parts = desc.split(", ");
									desc1 = parts[j];
									desc=desc1;
									
								}
//								else {
//									desc = null;
//								}
							
								if (!tax_struct_cd.equals("0")) {
									if(desc!=null)
									{
										desc_nm=desc.split(" ")[0];
									}
									
									queryString3 = "SELECT TAX_CODE FROM FMS_TAX_MST WHERE TAX_ALIAS_CODE = ? ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, desc_nm);
									
									rset3 = stmt3.executeQuery();
									if (rset3.next()) {
										data = rset3.getString(1);
									}
									else {
										data = "0";
									}
									rset3.close();
									stmt3.close();
								} 
								else {
									data = "0";
								}
							}
							else {
									data = "0";
								}
							tax_code = data;
							
						}
						
						else if(i == 6) {	//TAX_DESCR
						   if(tax_struct_cd != null) {
								if(!tax_struct_cd.equals("0")) {
									data=desc;
								}
						   }
						    else {
									data = null;
							}
						}
						else if(i==7)
						{
							
							double tax_amt = 0.0;
							if (desc != null) {
							    if ( !desc.contains("on") ) {
							    	
							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
//							        else {
							            gross_amt = rset.getString(9);
							            tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100;
							            adv_amt = tax_amt + "";
							            adv=adv_amt;
//							        }
							    }
							    else if (desc.contains("on")) {

							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
							        tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100;
							           
							    }
							}
							data = tax_amt + "";

						}
						
						else if(i == 8 && desc != null && desc.contains("on")) {
							data = adv;
						}
						
						stmt1.setString(i+1,data);
							
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_DLNG_SVC_INV_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(5));
			stmt5.setString(6, tax_code);
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt5.close();
				rset5.close();
			}
			rset2.close();
			stmt2.close();
			}
		}
		rset.close();
		stmt.close();

		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
	
		System.out.println("<<END>><<FMS_DLNG_SVC_INV_TAX_DTL>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_SVC_INV_TAX_DTL>>,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);

		
	
	}
	catch(Exception e)
	{
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
	finally {
		conn.commit();
	}

}


public void FMS_DLNG_SVC_INVOICE_DTL() throws IOException, SQLException {
	function_nm="FMS_DLNG_SVC_INVOICE_DTL()";
	try {
		
		table_name = "FMS_DLNG_SVC_INVOICE_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		
		queryString1 = "INSERT INTO FMS_DLNG_SVC_INVOICE_DTL(COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,"
				+ "SVC_INVOICE_SEQ,SVC_INVOICE_NO,INVOICE_SEQ,INVOICE_NO,TRUCK_TRANS_CD,TRUCK_CD,MAPPING_ID,"
				+ "PERIOD_START_DT,PERIOD_END_DT,QTY_MMBTU,DISTANCE,ENT_BY,ENT_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_SVC_INVOICE_DTL_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_SVC_INVOICE_DTL_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	
				//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
				// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
//			if (rowIterator.hasNext()) {	// For skipping the first row
//				rowIterator.next();
//			}
				String line = br.readLine();
				
				logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,FINANCIAL_YEAR,BU_STATE_TIN,SVC_INVOICE_SEQ,SVC_INVOICE_NO,INVOICE_NO,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String bu_seq="",bu_state="",state_code="",truck_trans_cd="",truck_cd="",mapping_id="",period_start_dt="",period_end_dt="",alloc_qty="";
					String plant_seq="",fin_year="",inv_seq="",inv_no="",svc_inv_seq="",svc_inv_no="";
					no = ""; rev = ""; seq_no = "" ; 
					abbr = "";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++)
					{
//					cell = cellIterator.next();
						data = null;
//					System.out.println(line.split(",")[i]);
						if (i == 0) {	// Counterparty_Abbr, Company Code 
							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//			    		if (!abbr.equals("NULL")) {
//							abbr = abbr.substring(1, abbr.length() - 1);
//						}
							data = company_cd;
						}
						else if (i == 2) {	// Counterparty_Cd
							cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, abbr.toUpperCase());
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
						else if (i == 3) { //Agmt_no
//							no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//			    		if (no != null) {
//							no = no.substring(1, no.length() - 1);
//						}
							queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SVC_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONT_REF_NO = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, cont_ref);
							
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
						else if (i == 4) { //Agmt_rev
							data = rev;
						}
						else if (i == 5) { //Cont_no
							data = cont_no;
						}
						else if (i == 6) { //Cont_rev
							data = cont_rev;
						}
						
						else if (i == 7) { //contract_type
							data = cont_type;
						}
						
						else if(i == 9) {	//BU_STATE_TIN
							queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
				    				+ " AND COUNTERPARTY_CD= '2' AND ENTITY='B' AND SEQ_NO = ?";
							stmt=conn.prepareStatement(queryString);
							stmt.setString(1, bu_seq);
							rset = stmt.executeQuery();
				    		if (rset.next()) {				    			
				    			bu_state = rset.getString(1);
				    		}else {
				    			bu_state  ="0";
				    		}	
				    		rset.close();
				    		stmt.close();
				    		
				    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
							stmt=conn.prepareStatement(queryString);
							stmt.setString(1, bu_state);
							rset = stmt.executeQuery();
				    		if (rset.next()) {				    			
				    			state_code = rset.getString(1);
				    		}else {
				    			state_code  ="0";
				    		}	
				    		rset.close();
				    		stmt.close();
				    		
				    		data = state_code;
						}
						
						else if(i == 12) {	//INVOICE_SEQ
							
							inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT INVOICE_SEQ, TRUCK_TRANS_CD, TRUCK_CD, MAPPING_ID, "
									+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'), TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'), ALLOC_QTY "
									+ "FROM FMS_DLNG_INVOICE_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND INVOICE_NO = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, inv_no);
							rset = stmt.executeQuery();
							if(rset.next()) {
								inv_seq = rset.getString(1);
								truck_trans_cd = rset.getString(2);
								truck_cd = rset.getString(3);
								mapping_id = rset.getString(4);
								period_start_dt = rset.getString(5);
								period_end_dt = rset.getString(6);
								alloc_qty = rset.getString(7);
							}
							rset.close();
							stmt.close();
							
							data = inv_seq;
						}
						
						else if(i == 13) {	//INVOICE_NO
							data = inv_no;
						}
						
						else if(i == 14) {	//TRUCK_TRANS_CD
							data = truck_trans_cd;
						}
						
						else if(i == 15) {	//TRUCK_CD
							data = truck_cd;
						}
						
						else if(i == 16) {	//MAPPING_ID
							data = mapping_id;
						}
						
						else if(i == 17) {	//PERIOD_START_DT
							data = period_start_dt;
						}
						
						else if(i == 18) {	//PERIOD_END_DT
							data = period_end_dt;
						}
						
						else if(i == 19) {	//QTY_MMBTU
							data = alloc_qty;
						}
						
						else {
							if(i == 1) {	//FINANCIAL_YEAR
								fin_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 8) {	//BU_UNIT
								bu_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 10) {	//SVC_INVOICE_SEQ
								svc_inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 11) {	//SVC_INVOICE_NO
								svc_inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) { 
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_SVC_INVOICE_DTL WHERE "
							+ "COMPANY_CD = ? AND FINANCIAL_YEAR = ? AND BU_STATE_TIN = ? AND SVC_INVOICE_SEQ = ? AND INVOICE_SEQ = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, fin_year);
					stmt.setString(3, state_code);
					stmt.setString(4, svc_inv_seq);
					stmt.setString(5, inv_seq);
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("") && !no.equals("")){
						logger.data(fname, (abbr + "," + cd + "," + fin_year + "," + state_code + "," + svc_inv_seq + "," + svc_inv_no + "," + inv_no + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (abbr + "," + cd + "," + fin_year + "," + state_code + "," + svc_inv_seq + "," + svc_inv_no + "," + inv_no + "," ), conn, "E");
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


public void FMS_DLNG_SVC_INV_FILE_DTL() throws IOException, SQLException {
	function_nm="FMS_DLNG_SVC_INV_FILE_DTL()";
	try {
		
		table_name = "FMS_DLNG_SVC_INV_FILE_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
		
		queryString1 = "INSERT INTO FMS_DLNG_SVC_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,"
				+ "PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT) "
				+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_SVC_INV_FILE_DTL_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_SVC_INV_FILE_DTL_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
				// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
//			if (rowIterator.hasNext()) {	// For skipping the first row
//				rowIterator.next();
//			}
				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					no = ""; rev = ""; seq_no = "" ; 
					abbr = "";
					bu_seq="";inv_seq="";fin_yr="";pdf_type="";file_nm="";sign_by="";sign_by_cd="";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
//				row = rowIterator.next();
//			    cellIterator = row.cellIterator();
//			    while (cellIterator.hasNext()) 
					for (int i = 0; i < line.split(",").length; i++)
					{
//					cell = cellIterator.next();
						data = null;
//					System.out.println(line.split(",")[i]);
						if (i == 14) {	// SIGNED_ENT_BY
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
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) = ? ";
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
							}
							else {
								sign_by_cd = "0";
							}
							data = sign_by_cd;
						}
						
						
						else {
							if(i == 1) {	//BU_UNIT
								bu_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 2) {	//INVOICE_SEQ
								inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 3) {	//FINANCIAL_YEAR
								fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if (i == 4) {	// PDF_TYPE
								pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 5) {	//FILE_NAME
								file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 11) {	//SIGNED_BY
								sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_DLNG_SVC_INV_FILE_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND PDF_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, inv_seq);
					stmt.setString(4, fin_yr);
					stmt.setString(5, pdf_type);
					rset = stmt.executeQuery();
					
					if (!rset.next()){
						
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
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


public void FMS_DLNG_INVOICE_MST_CR_DR() throws IOException, SQLException {
	function_nm="FMS_DLNG_INVOICE_MST_CR_DR()";
	try {
		
		table_name = "FMS_DLNG_INVOICE_MST_CR_DR";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		
		queryString1 = "INSERT INTO FMS_DLNG_INVOICE_MST(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRUCK_TRANS_CD,TRUCK_CD,"//10
				+ "MAPPING_ID,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INV_FLAG,INVOICE_SEQ,INVOICE_ID_SEQ,INVOICE_NO,INVOICE_DT,FREQ,"//22
				+ "PERIOD_START_DT,PERIOD_END_DT,DUE_DT,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,EXCHG_RATE_TYPE,"
				+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT_1,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,REMARK_1,REMARK_2,CHECKED_FLAG,CHECKED_BY,"
				+ "CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TCS_TDS,TCS_AMT,"
				+ "TCS_FACTOR,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,"
				+ "TDS_TAX_PERCENT,TDS_TAX_AMT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,"
				+ "SAP_APPROVED_DT,TCS_STRUCT_CD,TCS_EFF_DT_1,TDS_STRUCT_CD,TDS_EFF_DT_1,TAX_EFF_DT,TCS_EFF_DT,TDS_EFF_DT,CHKPOST_CD,DRIVER_CD,FIN_SYS,HOLD_AMT,"
				+ "REF_NO,CRITERIA) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,"
				+ "?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "?,?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_INVOICE_MST_CR_DR_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_INVOICE_MST_CR_DR_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	
				String line = br.readLine();
				
				logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,BU_SEQ,INVOICE_DT,FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,INVOICE_NO,TRUCK_TRANS_CD,TRUCK_CD,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String trans_cd="",bu_seq="",truck_cd="",inv_dt="",fill_cd="",bay_cd="",slot_st="",slot_end="",bu_state="",state_code="",contact_cd="";
					String bu_cont="",exch_rate="",plant_seq="",inv_raised="",name="",tax_dt="",chk_cd="",driver_cd="",inv_seq="",inv_no="";
					String fin_yr="",map_id="",freq="",sale_unit="",due_dt="";
					no = ""; rev = ""; seq_no = "" ; 
					abbr = "";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++)
					{
						data = null;
						if (i == 0) {	// Counterparty_Abbr, Company Code 
							abbr = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
							data = company_cd;
						}
						else if (i == 1) {	// Counterparty_Cd
							cont_ref = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
							
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
						else if(i==2)
						{
							fin_yr = line.split(",")[i].contains("'null'") ? null : line.split(",")[i];
							data = fin_yr;
						}
						else if (i == 3) { //Agmt_no
							queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRUCK_TRANS_CD,DRIVER_CD,BU_CONTACT_PERSON_CD,CHKPOST_CD,"
									+ " INVOICE_RAISED_IN,FREQ,MAPPING_ID,TRUCK_CD,CONTACT_PERSON_CD,BU_STATE_TIN,SALE_PRICE_UNIT"
									+ " FROM FMS_DLNG_INVOICE_MST WHERE COMPANY_CD = ? AND  COUNTERPARTY_CD = ? AND INVOICE_NO = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, cont_ref);
							
							
							rset = stmt.executeQuery();
							if (rset.next()) 
							{
								no = rset.getString(1);
								rev = rset.getString(2);
								cont_no = rset.getString(3);
								cont_rev = rset.getString(4);
								cont_type = rset.getString(5);
								trans_cd = rset.getString(6);
								driver_cd = rset.getString(7);
								bu_cont = rset.getString(8);
								chk_cd = rset.getString(9);
								inv_raised = rset.getString(10);
								freq = rset.getString(11);
								map_id = rset.getString(12);
								truck_cd = rset.getString(13);
								contact_cd = rset.getString(14);
								state_code = rset.getString(15);
								sale_unit = rset.getString(16);
							} 
							else {
								no = "";
								rev = "";
								cont_no = "";
								cont_rev = "";
								cont_type = "";
								trans_cd = "";
								driver_cd = "";
								bu_cont = "";
								chk_cd = "";
								inv_raised = "";
								freq = "";
								map_id = "";
								truck_cd = "";
								contact_cd = "";
								state_code = "";
								sale_unit = "";
							}
							rset.close();
							stmt.close();
							
							data = no;
						}
						else if (i == 4) { //Agmt_rev
							data = rev;
						}
						else if (i == 5) { //Cont_no
							data = cont_no;
						}
						else if (i == 6) { //Cont_rev
							data = cont_rev;
						}
						
						else if (i == 7) { //contract_type
							data = cont_type;
						}
						else if (i == 8) { //trans_cd
							data = trans_cd;
						}
						else if (i == 9) { //truck_cd
							data = truck_cd;
						}
						else if (i == 10) { //map_id
							data = map_id;
						}
						
						else if(i == 12) {	//BU_STATE_TIN
				    		data = state_code;
						}
//						
						else if(i == 13) {	//BU_CONTACT_PERSON
				    		data=bu_cont;
						}
//						
						else if(i == 15) {	//CONTACT_PERSON_CD

				    		data=contact_cd;
						}
						else if(i == 17) 
				    	{
				    		
				    		int count=0,inv_seq_no=0;
				    		queryString = "SELECT MAX(INVOICE_SEQ) "
				    				+ "FROM FMS_DLNG_INVOICE_MST "
				    				+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? AND BU_STATE_TIN =? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, company_cd);
				    		stmt.setString(2, fin_yr);
				    		stmt.setString(3, state_code);
				    		rset = stmt.executeQuery();
				    		if(rset.next())
				    		{
				    			count = rset.getInt(1);
				    		}
				    		else
				    		{
				    			count=0;
				    		}
				    		stmt.close();
					    	rset.close();
					    	if(count>0)
					    	{
					    		inv_seq_no = count+1;
					    	}
					    	else
					    	{
					    		inv_seq_no = 1;
					    	}
				    		data=inv_seq_no+"";
				    	}
						else if (i == 21) { //freq
							data = freq;
						}
						else if (i == 24) { //due_dt
							due_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							data = due_dt;
						}
						else if(i==27)
						{
							data = sale_unit;
						}
						else if(i == 29) {	//EXCHG_RATE_CD
							exch_rate = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
							queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, exch_rate.toUpperCase());
							rset = stmt.executeQuery();
							if(rset.next()) {
								exch_rate = rset.getString(1);
							}
							else {
								exch_rate = "0";
							}
							rset.close();
							stmt.close();
							
							data = exch_rate;
						}
						
						else if(i == 33) {	//INVOICE RAISED IN
							data = inv_raised;
						}
						
						else if(i == 36) {	//TAX_STRUCT_CD

				    		String temp_struct = "";
				    		tax_dt = "";
							name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("- ", ", ");
							if(name!=null) {
				    		if (!name.contains(", ")) {
								queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, name);
								rset = stmt.executeQuery();
								if (rset.next()) {
									temp_struct = rset.getString(1);
									tax_dt = rset.getString(2);
								}
								rset.close();
								stmt.close();
							}
				    		else {

								int flag = 0;
								queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, "%"+name.split(", ")[0]+"%");
								rset = stmt.executeQuery();
								
								while (rset.next()) {
									
									for (int j = 0; j < name.split(", ").length; j++) {
										if (rset.getString(2).contains(name.split(", ")[j])) {
											flag = 1;
										}
										else {
											flag = 0;
											break;
										}
									}
									
									if (flag == 1) {
										temp_struct = rset.getString(1);
										name=rset.getString(2);
										tax_dt = rset.getString(3);
										break;
									}
								}
								rset.close();
								stmt.close();
							}
						}
						    	data = temp_struct;
				    	
						}
						
						else if(i == 80) {	//TCS_STRUCT_CD
							queryString = "SELECT TAX_STRUCT_CD  "
									+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TCS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							stmt.setString(2, "C");
							stmt.setString(3,company_cd);
							rset = stmt.executeQuery();
							if(rset.next()) {
								data = rset.getString(1);
							}
							else {
								data = null;
							}
							rset.close();
							stmt.close();
						}
						
						else if(i == 82) {	//TDS_STRUCT_CD
							queryString = "SELECT TAX_STRUCT_CD  "
									+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TDS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							stmt.setString(2, "C");
							stmt.setString(3,company_cd);
							rset = stmt.executeQuery();
							if(rset.next()) {
								data = rset.getString(1);
							}
							else {
								data = null;
							}
							rset.close();
							stmt.close();
						}
						
						else if(i == 84) {	//TAX_EFF_DT
							data = tax_dt;
						}
						
						else if(i == 87) {	//CHKPOST_CD
							data = chk_cd;
						}
						
						else if(i == 88) {	//DRIVER_CD
							data = driver_cd;
						}
						
						else {
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						}
						
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_INVOICE_MST WHERE "
							+ "COMPANY_CD = ? AND FINANCIAL_YEAR = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, fin_yr);
					stmt.setString(3, state_code);
					stmt.setString(4, inv_seq);
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("") && !no.equals("")){
						
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + plant_seq + "," + bu_seq + "," + inv_dt + "," + fill_cd + "," + bay_cd + "," + slot_st + "," + slot_end + "," + inv_no + "," + trans_cd + "," + truck_cd + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (abbr + "," + cd + "," + no + "," + rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + plant_seq + "," + bu_seq + "," + inv_dt + "," + fill_cd + "," + bay_cd + "," + slot_st + "," + slot_end + "," + inv_no + "," + trans_cd + "," + truck_cd + "," ), conn, "E");
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

public void FMS_DLNG_INV_TAX_DTL_CR_DR() throws IOException, SQLException {

	function_nm="FMS_DLNG_INV_TAX_DTL_CR_DR()";
	try {
		
		
		System.out.println("<<START>><<FMS_DLNG_INV_TAX_DTL_CR_DR>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_DLNG_INV_TAX_DTL_CR_DR>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="",tax_eff_dt_1="";
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT_1,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TAX_EFF_DT";
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,TO_CHAR(TAX_EFF_DT_1, 'dd/mm/yyyy hh24:mi:ss'),"
				+ "NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
				+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss') "
				+ "	FROM FMS_DLNG_INVOICE_MST WHERE COMPANY_CD = ? AND INV_FLAG IN ('CR','DR')";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_DLNG_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT_1,TAX_DESCR,TAX_AMT,"
				+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TAX_EFF_DT) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(5);
			tax_eff_dt_1 =  rset.getString(7);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			
			int count_desc=1;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						if(i == 4) {	//TAX_STRUCT_CD
							if(tax_struct_cd != null) {
								tax_struct_cd = rset.getString(5);
							}
							else {
								tax_struct_cd = "0";
							}
							data = tax_struct_cd;
						}
						else if(i == 5) {
							
							//TAX_CODE
							if (tax_struct_cd != null) {
								
								desc = rset2.getString(1);
								eff_dt = rset2.getString(2); 
								if(desc.contains(", "))
								{
									count_desc=desc.split(", ").length;
									String[] parts = desc.split(", ");
									desc1 = parts[j];
									desc=desc1;
									
								}
//								else {
//									desc = null;
//								}
							
								if (!tax_struct_cd.equals("0")) {
									if(desc!=null)
									{
										desc_nm=desc.split(" ")[0];
									}
									
									queryString3 = "SELECT TAX_CODE FROM FMS_TAX_MST WHERE TAX_ALIAS_CODE = ? ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, desc_nm);
									
									rset3 = stmt3.executeQuery();
									if (rset3.next()) {
										data = rset3.getString(1);
									}
									else {
										data = "0";
									}
									rset3.close();
									stmt3.close();
								} 
								else {
									data = "0";
								}
							}
							else {
									data = "0";
								}
							tax_code = data;
							
						}
						
						else if(i == 6) {	//TAX_EFF_DT_1
							data = tax_eff_dt_1;
						}
						else if(i == 7) {	//TAX_DESCR
						   if(tax_struct_cd != null) {
								if(!tax_struct_cd.equals("0")) {
									data=desc;
								}
						   }
						    else {
									data = null;
							}
						}
						else if(i==8)
						{
							double tax_amt = rset.getDouble(9);
							if (desc != null) {
							    if ( !desc.contains("on") ) {
							    	
//							        count_value = desc.split("%")[0];
//							        count_value = count_value.split(" ")[1]; 
////							        else {
//							            gross_amt = rset.getString(10);
//							            tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100;
							            adv_amt = tax_amt + "";
							            adv=adv_amt;
//							        }
							    }
							    else if (desc.contains("on")) {

							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
							        tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100;
							           
							    }
							}
							data = tax_amt + "";

						}
						
						else if(i == 9 && desc != null && desc.contains("on")) {

							data = adv;
						}
						
						else if(i == 14) {
							data = eff_dt;
						}
						
						stmt1.setString(i+1,data);
						
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_DLNG_INV_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(5));
			stmt5.setString(6, tax_code);
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt5.close();
				rset5.close();
			}
			rset2.close();
			stmt2.close();
			}
		}
		rset.close();
		stmt.close();

		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
	
		System.out.println("<<END>><<FMS_DLNG_INV_TAX_DTL_CR_DR>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INV_TAX_DTL_CR_DR>>,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);

		
	
	}
	catch(Exception e)
	{
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
	finally {
		conn.commit();
		if (file != null) {
			file.close();
		}
	}

}
	


//FMS_DLNG_INV_CRDR_REF
public void FMS_DLNG_INV_CRDR_REF() throws IOException, SQLException {
	
	function_nm="FMS_DLNG_INV_CRDR_REF()";
	try {
		

		DateUtil utilDate = new DateUtil();
		DataBean_Sales_Invoice bill_freq = new DataBean_Sales_Invoice();
		
		table_name = "FMS_DLNG_INV_CRDR_REF";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		String financial_year="",inv_amt="",inv_raise="",inv_no="",bu="";
		String gross_amt="",temp_struct="",tc="",ta="",mm="",ma="",oc="",oa="";
		String mst_inv_no="",criteria="",sale_amt ="",tax_des="";
		String temp_tc="",temp_oc="",temp_mm="",tax="",tcs_factor="",tcs_amt="",exchg_cd="",name="",sale_unit="";
		double tax_amt=0,final_sale_amt=0;
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		
		queryString1 =  "INSERT INTO FMS_DLNG_INV_CRDR_REF (COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT, "
			  + "EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT, "
			  + "TAX_STRUCT_CD,TAX_AMT,INVOICE_AMT,NET_PAYABLE_AMT,TCS_TDS,TCS_AMT,TCS_FACTOR,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT, "
			  + "TDS_TAX_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
			  + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, "
			  + "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?, "
			  + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			  + "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";

		
		stmt1 = conn.prepareStatement(queryString1);
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_INV_CRDR_REF_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_INV_CRDR_REF_"+start_end_dt+".csv";
			try (
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,INVOICE_NO,TIMESTAMP,", conn);
								int inv_seq_no=1;
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									abbr = "";
									cd = "0";
									data = null;
									
									index = 1;
									stmt1 = conn.prepareStatement(queryString1);
									
									for (int i = 0; i < line.split(",").length; i++)
								    {	
										data = null;
										
								    	if (i == 0) {
								    		
								    		inv_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		inv_no = inv_no.split("&")[1];
								    		data = company_cd;
								    	}
								    	
								    	else if(i==1) {
								    		financial_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		data = financial_year;
								    	}
								    	
								    	else if (i == 2) 
								    	{	
								    		cont_ref = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		queryString = "SELECT INVOICE_RAISED_IN,BU_STATE_TIN,INVOICE_SEQ,GROSS_AMT, "
								    				+ "EXCHG_RATE_CD,TAX_STRUCT_CD,INVOICE_AMT,CRITERIA,SALE_PRICE_UNIT FROM FMS_DLNG_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND INVOICE_NO  = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		inv_raise = rset.getString(1);
									    		bu = rset.getString(2);
									    		inv_seq_no = rset.getInt(3);
//									    		gross_amt = rset.getString(4);
									    		exchg_cd = rset.getString(5);
									    		temp_struct = rset.getString(6);
//									    		inv_amt = rset.getString(7);
									    		criteria = rset.getString(8);
									    		sale_unit = rset.getString(9);
									    	}
									    	else
									    	{
									    		inv_raise="1";
									    		inv_amt=null;
									    	}
									    	stmt.close();
									    	rset.close();
									    	data = bu;
								    	}
								    	else if(i==3)
								    	{
								    		data = inv_seq_no+"";
								    	}
								    	else if(i==4)
								    	{
								    		sale_amt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		data = sale_amt;
								    	}
								    	else if(i==6)
								    	{
								    		data = sale_unit;
								    	}
								    	else if(i == 8) {
								    		
									    	data = exchg_cd;
								    	}
								    	else if(i==11)
								    	{
									    	data = inv_raise;
								    	}

								    	else if(i==12)
								    	{
								    		gross_amt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    	data = gross_amt;
								    	}
								    	
								    	else if(i==13)
								    	{
								    		
								    		tax_des = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		if (tax_des == null) {
								    		    continue; 
								    		}
								    		if (tax_des.contains("-")) {
								    		    name = tax_des.replaceAll("-", ", ");
								    		} 
								    		else 
								    		{
								    		    name = tax_des;
								    		}
//								    		System.out.println("name >>"+name+"::criteria::"+criteria);
								    		//tax_amt
								    		
								    		if ("QTY".equalsIgnoreCase(criteria))
								    		{
								    			final_sale_amt = Double.parseDouble(gross_amt);
									    		double taxPercent = 0.0;

									    		if (name != null && !name.isEmpty()) 
									    		{
									    		    String[] parts = name.split(",");

									    		    for (String part : parts) 
									    		    {
									    		        part = part.trim();
									    		        int percentIndex = part.indexOf('%');

									    		        if (percentIndex > 0) 
									    		        {
									    		            String number = part.substring(0, percentIndex).replaceAll("[^0-9.]", "");
									    		            taxPercent += Double.parseDouble(number);
									    		        }
									    		    }
									    		     tax_amt = final_sale_amt * taxPercent / 100;
									    		}
									    		else
									    		{
									    			 tax_amt = 0;
									    		}
								    		}
								    		if ("TAXP".equalsIgnoreCase(criteria))
								    		{
								    			if(name!=null) {
										    		if (!name.contains(", ")) {
														queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
														stmt = conn.prepareStatement(queryString);
														stmt.setString(1, name);
														rset = stmt.executeQuery();
														if (rset.next()) {
															temp_struct = rset.getString(1);
														}
														rset.close();
														stmt.close();
													}
										    		else {

														int flag = 0;
														queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
														stmt = conn.prepareStatement(queryString);
														stmt.setString(1, "%"+name.split(", ")[0]+"%");
														rset = stmt.executeQuery();
														
														while (rset.next()) {
															
															for (int j = 0; j < name.split(", ").length; j++) {
																if (rset.getString(2).contains(name.split(", ")[j])) {
																	flag = 1;
																}
																else {
																	flag = 0;
																	break;
																}
															}
															
															if (flag == 1) {
																temp_struct = rset.getString(1);
																name=rset.getString(2);
																break;
															}
														}
														rset.close();
														stmt.close(); 
													}
												}
								    		}

								    		
								    		data = temp_struct;
								    	}
								    	else if(i==14)
								    	{
								    		tax = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
								    		if ("QTY".equalsIgnoreCase(criteria))
								    		{
								    		 data = tax_amt+"";
								    		}
								    		else
								    		{
								    		  data = tax+"";
								    		}
								    	}
								    	else if(i==15)
								    	{
								    		String net_amt1 = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		double net_amt = 0;
								    		if ("QTY".equalsIgnoreCase(criteria))
								    		{
								    			net_amt = tax_amt+final_sale_amt;
								    			data = net_amt+"";
								    		}
								    		else
								    		{
//								    			net_amt = tax+final_sale_amt;
								    			data = net_amt1;
								    		}

								    	}
								    	else if(i==16)
								    	{
								    		String net_amt1 = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		double net_amt = 0;
								    		if ("QTY".equalsIgnoreCase(criteria))
								    		{
								    			net_amt = tax_amt+final_sale_amt;
								    			data = net_amt+"";
								    		}
								    		else
								    		{
//								    			net_amt = tax+final_sale_amt;
								    			data = net_amt1;
								    		}

								    	}
								    	else if(i==17)
								    	{
								    		String tcs_tds="";
								    		queryString = "SELECT TCS_TDS,TCS_AMT,TCS_FACTOR "
								    				+ "FROM FMS_DLNG_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND INVOICE_NO  = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		tcs_tds = rset.getString(1);
									    		tcs_amt = rset.getString(2);
									    		tcs_factor = rset.getString(3);
									    	}
									    	else
									    	{
									    		tcs_tds = null;
									    		tcs_amt = null;
									    		tcs_factor = null;
									    	}
									    	stmt.close();
									    	rset.close();
								    		data = tcs_tds;

								    	}
								    	else if(i==18)
								    	{
								    		data = tcs_amt;
								    	}
								    	else if(i==19)
								    	{
								    		data = tcs_factor;
								    	}
								    	else 
								    	{			    	
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    	}	
//								    	System.out.println(index+"->>>"+data);
								    	stmt1.setString(index++, data);		    	
								    }
									
								    queryString = "SELECT INVOICE_SEQ FROM FMS_DLNG_INV_CRDR_REF WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? "
								    		+ "AND FINANCIAL_YEAR = ? AND INVOICE_SEQ = ? ";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, bu);
							    	stmt.setString(3, financial_year);
							    	stmt.setString(4, inv_seq_no+"");
							    	
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !bu.equals("") ) 
							    	 {
							    		 logger.data(fname, (company_cd+"," + financial_year+"," +bu+","+ inv_seq_no + ","+inv_no+","), conn, "");
											
											stmt1.executeUpdate();
											stmt1.close();
											
											logger_count++;
									}
							    	 else {
											stmt1.close();
											skipped_count++;     
											logger.data(fname, (company_cd+"," + financial_year+"," +bu+","+ inv_seq_no + ","+inv_no+","), conn, "E");
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
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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

//FMS_DLNG_INV_CRDR_TAX_DTL
public void FMS_DLNG_INV_CRDR_TAX_DTL() throws IOException, SQLException {

	function_nm="FMS_DLNG_INV_CRDR_TAX_DTL()";
	try {
		
		
		System.out.println("<<START>><<FMS_DLNG_INV_CRDR_TAX_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_DLNG_INV_CRDR_TAX_DTL>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,NULL,"
				+ "NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
				+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),GROSS_AMT"
				+ "	FROM FMS_DLNG_INV_CRDR_REF WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_DLNG_INV_CRDR_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,"
				+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(5);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			double tax_amt = 0,final_tax=0;
			
			int count_desc=1;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						if(i == 4) {	//TAX_STRUCT_CD
							if(tax_struct_cd != null) {
								tax_struct_cd = rset.getString(5);
							}
							else {
								tax_struct_cd = "0";
							}
							data = tax_struct_cd;
						}
						else if(i == 5) {
							
							//TAX_CODE
							if (tax_struct_cd != null) {
								
								desc = rset2.getString(1);
								eff_dt = rset2.getString(2); 
								if(desc.contains(", "))
								{
									count_desc=desc.split(", ").length;
									String[] parts = desc.split(", ");
									desc1 = parts[j];
									desc=desc1;
									tax_amt = rset.getDouble(9);
									final_tax = tax_amt/2;
								}
								else {
									tax_amt = rset.getDouble(9);
									final_tax = tax_amt;
								}
							
								if (!tax_struct_cd.equals("0")) {
									if(desc!=null)
									{
										desc_nm=desc.split(" ")[0];
									}
									
									queryString3 = "SELECT TAX_CODE FROM FMS_TAX_MST WHERE TAX_ALIAS_CODE = ? ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, desc_nm);
									
									rset3 = stmt3.executeQuery();
									if (rset3.next()) {
										data = rset3.getString(1);
									}
									else {
										data = "0";
									}
									rset3.close();
									stmt3.close();
								} 
								else {
									data = "0";
								}
							}
							else {
									data = "0";
								}
							tax_code = data;
							
						}
						
						else if(i == 6) {	//TAX_EFF_DT
							data = eff_dt;
						}
						else if(i == 7) {	//TAX_DESCR
						   if(tax_struct_cd != null) {
								if(!tax_struct_cd.equals("0")) {
									data=desc;
								}
						   }
						    else {
									data = null;
							}
						}
						else if(i==8)
						{
							
//							double tax_amt = 0.0;
//							if (desc != null) {
//							    if ( !desc.contains("on") ) {
//							    	
//							        count_value = desc.split("%")[0];
//							        count_value = count_value.split(" ")[1]; 
////							        else {
//							            gross_amt = rset.getString(15);
//							            tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100);
//							            adv_amt = tax_amt + "";
//							            adv=adv_amt;
////							        }
//							    }
//							    else if (desc.contains("on")) {
//
//							        count_value = desc.split("%")[0];
//							        count_value = count_value.split(" ")[1]; 
//							        tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100);
//							           
//							    }
//							}
							data = final_tax + "";

						}
						
						else if(i == 9 && desc != null && desc.contains("on")) {

							data = adv;
						}
						
						stmt1.setString(i+1,data);
							
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_DLNG_INV_CRDR_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(5));
			stmt5.setString(6, tax_code);
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt5.close();
				rset5.close();
			}
			rset2.close();
			stmt2.close();
			}
		}
		rset.close();
		stmt.close();

		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
	
		System.out.println("<<END>><<FMS_DLNG_INV_CRDR_TAX_DTL>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_INV_CRDR_TAX_DTL>>,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);

		
	
	}
	catch(Exception e)
	{
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
	finally {
		conn.commit();
		if (file != null) {
			file.close();
		}
	}
	
}


public void FMS_DLNG_INV_CRDR_FILE_DTL() throws IOException, SQLException {
	function_nm="FMS_DLNG_INV_CRDR_FILE_DTL()";
	try {
		
		table_name = "FMS_DLNG_INV_CRDR_FILE_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,inv_no,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
		
		queryString1 = "INSERT INTO FMS_DLNG_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,"
				+ "PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT,EMAIL_SENT,EMAIL_SENT_BY,EMAIL_SENT_DT) "
				+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_INV_CRDR_FILE_DTL_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_INV_CRDR_FILE_DTL_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
				// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
//			if (rowIterator.hasNext()) {	// For skipping the first row
//				rowIterator.next();
//			}
				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					no = ""; rev = ""; seq_no = "" ; 
					abbr = "";
					bu_seq="";inv_seq="";inv_no="";fin_yr="";pdf_type="";file_nm="";sign_by="";sign_by_cd="";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
//				row = rowIterator.next();
//			    cellIterator = row.cellIterator();
//			    while (cellIterator.hasNext()) 
					for (int i = 0; i < line.split(",").length; i++)
					{
//					cell = cellIterator.next();
						data = null;
//					System.out.println(line.split(",")[i]);
						
						if(i == 2) {	//INVOICE_SEQ
							inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT INVOICE_SEQ FROM FMS_DLNG_INVOICE_MST WHERE COMPANY_CD = ? AND INVOICE_NO = ? AND BU_STATE_TIN = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, inv_no);
							stmt.setString(3, bu_seq);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								inv_seq = rset.getString(1);
							}
							else 
							{
								inv_seq=null;
							}
							rset.close();
							stmt.close();
							
							data = inv_seq;
						}
						else if (i == 14) {	// SIGNED_ENT_BY
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
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) = ? ";
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
							}
							else {
								sign_by_cd = "0";
							}
							data = sign_by_cd;
						}
						
						
						else {
							if(i == 1) {	//BU_STATE_TIN
								bu_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 3) {	//FINANCIAL_YEAR
								fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if (i == 4) {	// PDF_TYPE
								pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 5) {	//FILE_NAME
								file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 11) {	//SIGNED_BY
								sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_DLNG_INV_FILE_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND PDF_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, inv_seq);
					stmt.setString(4, fin_yr);
					stmt.setString(5, pdf_type);
					rset = stmt.executeQuery();
					
					if (!rset.next() && inv_seq!=null){
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else { 
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
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



public void FMS_DLNG_INVOICE_MST_LP() throws IOException, SQLException {
	function_nm="FMS_DLNG_INVOICE_MST_LP()";
	try {
		
		DateUtil utilDate = new DateUtil();
		table_name = "FMS_DLNG_INVOICE_MST_LP";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		
		queryString1 = "INSERT INTO FMS_DLNG_INVOICE_MST(COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRUCK_TRANS_CD,TRUCK_CD,"
				+ "MAPPING_ID,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INV_FLAG,INVOICE_SEQ,INVOICE_ID_SEQ,INVOICE_NO,INVOICE_DT,FREQ,"
				+ "PERIOD_START_DT,PERIOD_END_DT,DUE_DT,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,EXCHG_RATE_TYPE,"
				+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT_1,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,REMARK_1,REMARK_2,CHECKED_FLAG,CHECKED_BY,"
				+ "CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TCS_TDS,TCS_AMT,"
				+ "TCS_FACTOR,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,"
				+ "TDS_TAX_PERCENT,TDS_TAX_AMT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,"
				+ "SAP_APPROVED_DT,TCS_STRUCT_CD,TCS_EFF_DT_1,TDS_STRUCT_CD,TDS_EFF_DT_1,TAX_EFF_DT,TCS_EFF_DT,TDS_EFF_DT,CHKPOST_CD,DRIVER_CD,FIN_SYS,HOLD_AMT,"
				+ "REF_NO,CRITERIA,DISCOUNT_DAYS,INT_RATE) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,"
				+ "?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_INVOICE_MST_LP_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_INVOICE_MST_LP_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
				// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
//			if (rowIterator.hasNext()) {	// For skipping the first row
//				rowIterator.next();
//			}
				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ_NO,CONTACT_PERSON_CD,INV_SEQ,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					total_count++; 
					String trans_cd="",bu_seq="",truck_cd="",inv_dt="",sale_pr_unit="",bu_state="",state_code="",contact_cd="",plant_seq_no="",bu_seq_no="",agmt_rev="";
					String bu_cont="",exch_rate="",plant_seq="",inv_raised="",name="",tax_dt="",chk_cd="",driver_cd="",fin_year="",inv_seq="",inv_no="",mail="",lic="",agmt_no="",map_id="",due_dt="",pay_recv_dt="",disc_days="";
					String period_st_dt = "", period_end_dt = "";
					no = ""; rev = ""; seq_no = "" ; 
					abbr = "";
					cd = "0";
					int inv_seq_no = 0;
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++)
					{
						data = null;
						if (i == 0) { // Company Code 
							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							data = company_cd;
						}
						else if (i == 1) {	// Counterparty_Cd
							cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							
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
						else if (i == 3) { //Agmt_no
							queryString = "SELECT AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRUCK_TRANS_CD, "
									+ "TRUCK_CD, MAPPING_ID, BU_UNIT, BU_STATE_TIN, BU_CONTACT_PERSON_CD, PLANT_SEQ, SALE_PRICE_UNIT, "
									+ "TO_CHAR(PAY_RECV_DT, 'DD/MM/YYYY'), TO_CHAR(DUE_DT, 'DD/MM/YYYY') "
									+ "FROM FMS_DLNG_INVOICE_MST WHERE INVOICE_NO = ? AND COMPANY_CD = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cont_ref);
				    		stmt.setString(2, company_cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			agmt_no = rset.getString(1);
				    			agmt_rev = rset.getString(2);
				    			cont_no = rset.getString(3);
				    			cont_rev = rset.getString(4);
				    			cont_type = rset.getString(5);
				    			trans_cd = rset.getString(6);
				    			truck_cd = rset.getString(7);
				    			map_id = rset.getString(8);
				    			bu_seq_no = rset.getString(9);
				    			state_code = rset.getString(10);
				    			bu_cont = rset.getString(11);
				    			plant_seq_no = rset.getString(12);
				    			sale_pr_unit = rset.getString(13);
				    			pay_recv_dt = rset.getString(14) == null ? "" : rset.getString(14);
				    			due_dt = rset.getString(15) == null ? "" : rset.getString(15);
				    		} else {
				    			agmt_no =  "";
				    			agmt_rev =  "";
				    			cont_no =  "";
				    			cont_rev =  "";
				    			cont_type =  "";
				    			trans_cd =  "";
				    			truck_cd =  "";
				    			map_id =  "";
				    			bu_seq_no =  "";
				    			state_code =  "";
				    			bu_cont =  "";
				    			plant_seq_no =  "";
				    			sale_pr_unit = "";
				    			pay_recv_dt = "";
				    			due_dt = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
							data = agmt_no;
						}
						else if (i == 4) { //Agmt_rev
							data = agmt_rev;
						}
						else if (i == 5) { //Cont_no
							data = cont_no;
						}
						else if (i == 6) { //Cont_rev
							data = cont_rev;
						}
						
						else if (i == 7) { //contract_type
							data = cont_type;
						}
						
						else if(i == 8) {	//TRUCK_TRANS_CD
							data = trans_cd;
						}
						
						else if(i == 9) {	//TRUCK_CD
							data = truck_cd;
						}
						
						else if(i == 10) {	//MAPPING_ID
							data = map_id;
						}
						
						else if(i == 11) {	//BU SEQ
							data = bu_seq_no;
						}
						
						else if(i == 12) {	//BU_STATE_TIN
				    		data = state_code;
						}
						
						else if(i == 13) {	//BU_CONTACT_PERSON
				    		data=bu_cont;
						}
						
						else if(i == 13) {	
				    		data=plant_seq_no;
						}
						
						else if(i == 15) {	//CONTACT_PERSON_CD

				    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    		
				    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
									
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, mail);
					    	stmt.setString(2, cd);
					    	rset = stmt.executeQuery();
					    	
					    	if (rset.next()) {
					    		contact_cd = rset.getString(1);
					    	}
					    	else {
					    		contact_cd = "1";
					    	}
					    	rset.close();
					    	stmt.close();
				    		data=contact_cd;
						}
						else if(i == 17) 
				    	{
				    		
				    		int count=0;
				    		queryString = "SELECT MAX(INVOICE_SEQ) "
				    				+ "FROM FMS_DLNG_INVOICE_MST "
				    				+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? AND BU_STATE_TIN =? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, company_cd);
				    		stmt.setString(2, fin_year);
				    		stmt.setString(3, state_code);
				    		rset = stmt.executeQuery();
				    		if(rset.next())
				    		{
				    			count = rset.getInt(1);
				    		}
				    		else
				    		{
				    			count=0;
				    		}
				    		stmt.close();
					    	rset.close();
					    	if(count>0)
					    	{
					    		inv_seq_no = count+1;
					    	}
					    	else
					    	{
					    		inv_seq_no = 1;
					    	}
				    		data=inv_seq_no+"";
				    	}
						
						else if (i == 22) {	// Period Start Date
							period_st_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT TO_CHAR(START_DT, 'DD/MM/YYYY'), TO_CHAR(END_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST "
									+ " WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_rEV = ? AND CONT_NO = ? AND CONT_REV = ? "
									+ " AND CONTRACT_TYPE = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, agmt_no);
							stmt.setString(4, agmt_rev);
							stmt.setString(5, cont_no);
							stmt.setString(6, cont_rev);
							stmt.setString(7, cont_type);
							rset = stmt.executeQuery();
							
							if (rset.next()) {
								period_st_dt = rset.getString(1);
								period_end_dt = rset.getString(2);
							}
							rset.close();
							stmt.close();
							
							data = period_st_dt;
							
							
						}
						
						else if (i == 23) {
							data = period_end_dt;
						}
						
						else if(i == 27) {
							data=sale_pr_unit+"";
						}
						
						else if(i == 33) {	//INVOICE RAISED IN
							queryString = "SELECT INVOICE_CUR_CD FROM FMS_SUPPLY_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? "
									+ "AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, agmt_no);
							stmt.setString(4, agmt_rev);
							stmt.setString(5, cont_no);
							stmt.setString(6, cont_rev);
							stmt.setString(7, cont_type);
							stmt.setString(8, plant_seq_no);
							rset = stmt.executeQuery();
							if(rset.next()) {
								inv_raised = rset.getString(1);
							}
							else {
								inv_raised = "1";
							}
							rset.close();
							stmt.close();
							
							data = inv_raised;
						}
						
						else if(i == 36) {	//TAX_STRUCT_CD

				    		
				    		String temp_struct = "";
				    		tax_dt = "";
							name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@ ", ", ");;
							if(name!=null) {
				    		if (!name.contains(", ")) {
								queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, name);
								rset = stmt.executeQuery();
								if (rset.next()) {
									temp_struct = rset.getString(1);
									tax_dt = rset.getString(2);
								}
								rset.close();
								stmt.close();
							}
				    		else {

								int flag = 0;
								queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, "%"+name.split(", ")[0]+"%");
								rset = stmt.executeQuery();
								
								while (rset.next()) {
									
									for (int j = 0; j < name.split(", ").length; j++) {
										if (rset.getString(2).contains(name.split(", ")[j])) {
											flag = 1;
										}
										else {
											flag = 0;
											break;
										}
									}
									
									if (flag == 1) {
										temp_struct = rset.getString(1);
										name=rset.getString(2);
										tax_dt = rset.getString(3);
										break;
									}
								}
								rset.close();
								stmt.close();
							}
						}
						    	data = temp_struct;
				    	
						}
						else if( i == 93) {
				    		disc_days = (line.split(",")[i].contains("null") ? "0" : line.split(",")[i]);
				    		float int_days = utilDate.getDays(pay_recv_dt, due_dt)-1-Float.parseFloat(disc_days);
				    		data = int_days + "";
						}
						else {
							if(i == 2) {	//FINANCIAL_YEAR
								fin_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 18) {	//INVOICE_SEQ
								inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 19) {	//INVOICE_NO
								inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_INVOICE_MST WHERE "
							+ "COMPANY_CD = ? AND FINANCIAL_YEAR = ? AND BU_STATE_TIN = ? AND INVOICE_ID_SEQ = ? AND INV_FLAG='LP' ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, fin_year);
					stmt.setString(3, state_code);
					stmt.setString(4, inv_seq);
					rset = stmt.executeQuery();
					
					if (!rset.next() && !cd.equals("")){
						
						logger.data(fname, (company_cd+"," + cd + " , " + fin_year + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont + "," + plant_seq_no + "," + contact_cd+ "," + inv_seq + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + " , " + fin_year + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont + "," + plant_seq_no + "," + contact_cd+ "," + inv_seq + "," ), conn, "E");
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

public void FMS_DLNG_INV_FILE_DTL_LP() throws IOException, SQLException {
	function_nm="FMS_DLNG_INV_FILE_DTL_LP()";
	try {
		
		table_name = "FMS_DLNG_INV_FILE_DTL_LP";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
		
		queryString1 = "INSERT INTO FMS_DLNG_INV_FILE_DTL(COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,"
				+ "PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT,EMAIL_SENT,EMAIL_SENT_BY,EMAIL_SENT_DT) "
				+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_INV_FILE_DTL_LP_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_INV_FILE_DTL_LP_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	

				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					total_count++;
					String new_inv="", inv_type="";
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
							data = fin_yr;
						}
						else if(i == 2) {
							inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN FROM FMS_DLNG_INVOICE_MST WHERE INVOICE_ID_SEQ = ? AND FINANCIAL_YEAR = ? AND COUNTERPARTY_CD = ? AND INV_FLAG = 'LP' AND COMPANY_CD = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, inv_seq);
							stmt.setString(2, fin_yr);
							stmt.setString(3, cd);
							stmt.setString(4, company_cd);
							
							rset = stmt.executeQuery();
							if (rset.next()) {
								seq_no = rset.getString(1);
								bu_seq = rset.getString(2);
							} else {
								seq_no ="0";
								bu_seq ="0";
							}
							rset.close();
							stmt.close();
							
							data = bu_seq;
						}
						
						else if(i == 3) {
							data = seq_no;
						}
//						else if (i == 15) {	// SIGNED_ENT_BY
//							if(sign_by!=null) {
//								if(sign_by.contains("@")) {
//									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMAIL_ID) = ? ";
//									stmt = conn.prepareStatement(queryString);
//									stmt.setString(1, sign_by.toUpperCase());
//									rset = stmt.executeQuery();
//									if (rset.next()) {
//										sign_by_cd = rset.getString(1);
//									} else {
//										sign_by_cd ="0";
//									}
//									rset.close();
//									stmt.close();
//								}
//								else {
//									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) = ? ";
//									stmt = conn.prepareStatement(queryString);
//									stmt.setString(1, sign_by.toUpperCase());
//									rset = stmt.executeQuery();
//									if (rset.next()) {
//										sign_by_cd = rset.getString(1);
//									} else {
//										sign_by_cd ="0";
//									}
//									rset.close();
//									stmt.close();
//								}
//							}
//							else {
//								sign_by_cd = "0";
//							}
//							data = sign_by_cd;
//						}
						
						
						else {
							 if (i == 4) {	// PDF_TYPE
									pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								
								else if(i == 5) {	//FILE_NAME
									file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								
								else if(i == 11) {	//SIGNED_BY
									sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_DLNG_INV_FILE_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND PDF_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, seq_no);
					stmt.setString(4, fin_yr);
					stmt.setString(5, pdf_type);
					rset = stmt.executeQuery();
					
					if (!rset.next()){
						
						logger.data(fname, (company_cd + "," + bu_seq + "," + seq_no + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + seq_no + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
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

public void FMS_DLNG_FFLOW_INV_MST_SERV() throws IOException, SQLException {
	
	function_nm="FMS_DLNG_FFLOW_INV_MST_SERV()";
	try {
		

		DateUtil utilDate = new DateUtil();
		DataBean_Sales_Invoice bill_freq = new DataBean_Sales_Invoice();
		
		table_name = "FMS_DLNG_FFLOW_INV_MST";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		
		queryString1 = "INSERT INTO FMS_DLNG_FFLOW_INV_MST(COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
				+ "BU_STATE_TIN,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,INVOICE_CATEGORY,FREQ,PERIOD_START_DT,"
				+ "PERIOD_END_DT,DUE_DT,INVOICE_TYPE,LINKED_INVOICE,NUM_LINE,NOTE,GROSS_AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,"
				+ "GROSS_AMT_INR,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,CHECKED_FLAG,CHECKED_BY,"
				+ "CHECKED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,OTHER_INV_STR,AMT_WORD,PDF_INV_DTL,PRINT_BY_ORI,"
				+ "PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,INVOICE_ID_SEQ,TDS_STRUCT_CD,TDS_EFF_DT,TCS_STRUCT_CD,TCS_EFF_DT,"
				+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,"
				+ "TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,TCS_FACTOR,TCS_TDS,TCS_AMT,ALLOC_QTY,SUB_INV_TYPE,FIN_SYS,HOLD_AMT,CARGO_NO,SAC_CD)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
				+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		stmt1 = conn.prepareStatement(queryString1);
		Map<String, Integer> invseq = new HashMap<String, Integer>();
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_MST_SERV_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_MST_SERV_"+start_end_dt+".csv";
			
			try (
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,INVOICE_TYPE,TIMESTAMP,", conn);
								
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									String dt="",financial_year="",contact_person_cd="",mail="",billing_eff_dt = "", freq = "", start_dt = "", end_dt = "",tds_per="",inv_id_seq="";
									String addr_flag = "", inv_type="";
									String bu_seq_no ="",bu_plant_state="",state_code="",bu_cont_person_cd="",name="",exchg_cd="";
									String agmt_no = "",agmt_rev = "",seq_no = "";
									abbr = "";
									cd = "0";
									data = null;
									int inv_seq_no = 0;
									
									index = 1;
									stmt1 = conn.prepareStatement(queryString1);
									
									for (int i = 0; i < line.split(",").length; i++)
								    {	
										data = null;
										
								    	if (i == 0) {
								    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
								    		data = company_cd;
								    	}
								    	else if(i == 1) {
								    		financial_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		data = financial_year;
								    	}
								    	else if (i == 2) {	// Counterparty_Cd
								    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);

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
								    		data = cd;
								    	}
										else if (i == 3) { //Agmt_no
											queryString = "SELECT AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, "
													+ "BU_UNIT, BU_STATE_TIN, BU_CONTACT_PERSON_CD "
													+ "FROM FMS_DLNG_SVC_INVOICE_MST WHERE INVOICE_NO = ? AND COMPANY_CD = ? ";
								    		stmt = conn.prepareStatement(queryString);
								    		stmt.setString(1, cont_ref);
								    		stmt.setString(2, company_cd);
								    		rset = stmt.executeQuery();
								    		if (rset.next()) {
								    			agmt_no = rset.getString(1);
								    			agmt_rev = rset.getString(2);
								    			cont_no = rset.getString(3);
								    			cont_rev = rset.getString(4);
								    			cont_type = rset.getString(5);
								    			bu_seq_no = rset.getString(6);
								    			state_code = rset.getString(7);
								    			bu_cont_person_cd = rset.getString(8);
								    		} else {
								    			agmt_no =  "";
								    			agmt_rev =  "";
								    			cont_no =  "";
								    			cont_rev =  "";
								    			cont_type =  "";
								    			bu_seq_no =  "";
								    			state_code =  "";
								    			bu_cont_person_cd =  "";
								    		}
								    		rset.close();
								    		stmt.close();
								    		
											data = agmt_no;
								    	}
								    	else if (i == 4) { //Agmt_rev
											data = agmt_rev;
								    	}
								    	else if (i == 5) { //Cont_no
								    		data = cont_no;
								    	}
							    		
								    	else if (i == 6) { //Cont_rev
								    		data = cont_rev;
								    	}
								    	else if (i == 7) { //contract_type
								    		data = cont_type;
								    	}
								    					    	
								    	else if(i == 8) { // BU_SEQ
											data  = bu_seq_no;
								    	}
								    	else if(i ==9) {
								    		data = state_code;
								    		
								    	}
								    	else if(i == 10) { //BU_CONTACT_PERSON

								    		queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
								    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y' ORDER BY CONTACT_PERSON ";
											stmt=conn.prepareStatement(queryString);
											
											
											if(bu_seq_no.equals("1")) {								
												addr_flag = "P1";
											}else if(bu_seq_no.equals("2")){
												addr_flag = "P2";
											}else if(bu_seq_no.equals("3")){
												addr_flag = "P3";
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
								    	else if (i == 11) {	//addr_flag
//							    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
			//				    			if (inv_seq != null) {
			//				    				inv_seq = inv_seq.substring(1, inv_seq.length() - 1);
			//								}
							    			data = addr_flag; 
							    		}
								    	else if(i == 12)
								    	{
								    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
								    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
													
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, mail);
									    	stmt.setString(2, cd);
									    	rset = stmt.executeQuery();
									    	
									    	if (rset.next()) {
									    		contact_person_cd = rset.getString(1);
									    	}
									    	else {
									    		contact_person_cd = "0";
									    	}
									    	rset.close();
									    	stmt.close();
								    		data=contact_person_cd;
								    	}
								    	else if(i == 13) {
								    		int count=0;
								    		queryString = "SELECT MAX(INVOICE_SEQ) "
								    				+ "FROM FMS_DLNG_FFLOW_INV_MST "
								    				+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? AND BU_STATE_TIN =? ";
								    		stmt = conn.prepareStatement(queryString);
								    		stmt.setString(1, company_cd);
								    		stmt.setString(2, financial_year);
								    		stmt.setString(3, state_code);
								    		rset = stmt.executeQuery();
								    		if(rset.next())
								    		{
								    			count = rset.getInt(1);
								    		}
								    		else
								    		{
								    			count=0;
								    		}
								    		stmt.close();
									    	rset.close();
									    	if(count>0)
									    	{
									    		inv_seq_no = count+1;
									    	}
									    	else
									    	{
									    		inv_seq_no = 1;
									    	}
								    		data=inv_seq_no+"";
								    	}
								    	else if (i == 17) {

									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    	freq = data;
									    	
								    	}
								    	else if(i == 26) {
								    		name = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		exchg_cd="";
											queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, name);
									    	rset = stmt.executeQuery();
									    	if (rset.next()) {
									    		exchg_cd = rset.getString(1);
									    	}
									    	else {
									    		exchg_cd = "0";
									    	}
									    	rset.close();
									    	stmt.close();
									    	data = exchg_cd;
								    	}
								    	else if(i == 32) {
								    		
								    		String temp_struct = "";
								    		dt = "";
											name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@ ", ", ");
								    
											if(name!= null) {
												if (!name.contains(", ")) {
													queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, name);
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
													queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, "%"+name.split(", ")[0]+"%");
													rset = stmt.executeQuery();
													
													while (rset.next()) {
														
														for (int j = 0; j < name.split(", ").length; j++) {
															if (rset.getString(2).contains(name.split(", ")[j])) {
																flag = 1;
															}
															else {
																flag = 0;
																break;
															}
														}
														
														if (flag == 1) {
															temp_struct = rset.getString(1);
															name=rset.getString(2);
															dt = rset.getString(3);
															break;
														}
													}
													rset.close();
													stmt.close();
												}
											}
								    		
										   	data = temp_struct;
								    	}
								    	else if(i == 33) {
								    		data = dt;
								    	}
								    	else if(i == 59) {
								    		String tds_desc = line.split(",")[i].contains("null") ? null : line.split(",")[i];
											if(tds_desc!=null) {
												queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1,tds_desc);
												rset = stmt.executeQuery();
												if(rset.next()) {
													data = rset.getString(1);
												}
												else {
													data = null;
												}
												rset.close();
												stmt.close();
											}
								    	}
								    	else if(i == 78) {
								    		if(tds_per!=null) {
								    			data = "TDS";
								    		}else {
								    			data = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
								    		}
								    	}
								    	else if(i == 85) {
								    		String sac_cd="";
								    		
								    		queryString = "SELECT SAC_CD FROM FMS_SAC_MST WHERE ENT_PROFILE = '2' ";
											stmt = conn.prepareStatement(queryString);
											rset = stmt.executeQuery();
											if (rset.next()) {
												sac_cd = rset.getString(1);
											}
											else{
												sac_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
											}
											rset.close();
											stmt.close();
											data = sac_cd;
								    	}
								    	
								    	else {			    	
								    		
								    	    if (i == 21) {	//invoice_type
								    			inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		if(i == 18) {
								    			start_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		if(i == 19) {
								    			end_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		if(i == 58) {
								    			inv_id_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		if(i == 75) {
								    			tds_per = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    	}	
//								    	System.out.println(index+"-"+data);
								    	stmt1.setString(index++, data);		    	
								    }
									
								    queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_FFLOW_INV_MST WHERE COMPANY_CD = ? "
								    		+ "AND FINANCIAL_YEAR = ? AND BU_STATE_TIN = ? AND INVOICE_ID_SEQ = ? AND INVOICE_TYPE = 'LP' ";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, financial_year);
							    	stmt.setString(3, state_code);
							    	stmt.setString(4, inv_id_seq);
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") ) {
//											System.out.println(queryString1);
											
											logger.data(fname, (company_cd+ "," + financial_year + "," + state_code + "," + inv_id_seq + "," + inv_type + "," + inv_seq_no + ","), conn, "");
											stmt1.executeUpdate();
											stmt1.close();
											conn.commit();
											logger_count++;
									}
							    	 else {
											stmt1.close();
											skipped_count++;     
											logger.data(fname, (company_cd+ "," + financial_year + "," + state_code + "," + inv_id_seq + "," + inv_type + "," + inv_seq_no + ","), conn, "E");
											
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
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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


public void FMS_DLNG_FFLOW_INV_DTL_SERV() throws IOException, SQLException {
	
	function_nm="FMS_DLNG_FFLOW_INV_DTL_SERV()";
	try {
		
		table_name = "FMS_DLNG_FFLOW_INV_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 

		String line_no="",inv_type="",bu_seq_no="",agmt_no="",fin_yr="",state_code="",inv_seq="",cont_cd="",bu_cont_person_cd="",agmt_rev="";
		String addr_flag="", new_inv="", bu_plant_state="",sac_cd="";
		
		queryString1 = "INSERT INTO FMS_DLNG_FFLOW_INV_DTL (COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
				+ "BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_TYPE,INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,UNIT,"
				+ "QTY,RATE,AMOUNT,ENT_BY,ENT_DT,CARGO_NO,SAC_CD) "
		        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
		        + "?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?)";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_MST_SERV_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_MST_SERV_"+start_end_dt+".csv";
			try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_DTL_"+start_end_dt+".xlsx"));
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				//				if (rowIterator.hasNext()) {	// For skipping the first row
				//					rowIterator.next();
				//				}
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_TYPE,INVOICE_SEQ,LINE_NO,TIMESTAMP,", conn);
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									agmt_no = ""; agmt_rev = ""; seq_no = "";
									abbr = "";
									cd = "0";
									data = null;
									
									index = 1;
									stmt1 = conn.prepareStatement(queryString1);
									System.out.println(line);
									for (int i = 0; i < line.split(",").length; i++)
								    {	
				//				    	cell = cellIterator.next();
										data = null;
										
								    	if (i == 0) {
								    		
								    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
											data = company_cd;
								    	}
								    	else if (i == 1) {	// Counterparty_Cd
								    		
								    		fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
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
								    		data = fin_yr;
								    	}
								    	else if (i == 2) {	//cd
								    		cont_ref = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		
											data = cd;
								    	}
										else if (i == 3) { //Agmt_no
											
								    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//				    		
								    		if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
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
								    		}
								    		else if(cont_ref.startsWith("S")) {
								    			
								    			cont_type = cont_ref.split("-")[0];
												if(cont_type.equals("S")) {
													cont_type = "F";
												}
												else {
													cont_type = "E";
												}
												queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND REMARK LIKE ? AND CONTRACT_TYPE = ? ";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, cd);
												stmt.setString(2, "%@"+cont_ref);
												stmt.setString(3, cont_type);
												
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
								    	else if (i == 4) { //Agmt_rev
//								    		if(!cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//									    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//								    		}
											data = agmt_rev;
								    	}
								    	else if (i == 5) { //Cont_no
//								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//								    			cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//								    		}
								    		data = cont_no;
								    	}
								    	else if (i == 6) { //Cont_rev
//								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//								    			cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//								    		}
								    		data = cont_rev;
								    	}
								    	else if (i == 7) { //contract_type
//								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
//								    			cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//								    		}
								    		data = cont_type;
								    	}
								    					    	
								    	else if(i == 8) { // BU_SEQ
								    		
								    		queryString = "SELECT BU_UNIT FROM FMS_FFLOW_INV_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
								    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
								    		stmt = conn.prepareStatement(queryString);
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
								    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
				
								    		rset.close();
								    		stmt.close();		    		
								    		data  = bu_seq_no;
								    	}
								    	else if(i ==9) {
								    		
								    		queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
								    				+ " AND COUNTERPARTY_CD='2' AND ENTITY='B' AND SEQ_NO = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_seq_no);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			bu_plant_state = rset.getString(1);
								    		}else {
								    			bu_plant_state  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_plant_state);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			state_code = rset.getString(1);
								    		}else {
								    			state_code  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		data = state_code;
								    		
								    	}
								    	else if(i == 10) {
								    		new_inv = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
								    		queryString = "SELECT BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,CARGO_NO,SAC_CD FROM FMS_DLNG_FFLOW_INV_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
								    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND INVOICE_NO = ?";
								    		stmt = conn.prepareStatement(queryString);
								    		stmt.setString(1, cd);
								    		stmt.setString(2, agmt_no);
								    		stmt.setString(3, agmt_rev);
								    		stmt.setString(4, cont_no);
								    		stmt.setString(5, cont_rev);
								    		stmt.setString(6, cont_type);
								    		stmt.setString(7, new_inv);
								    		rset = stmt.executeQuery();
								    		
								    		if (rset.next()) {
								    			bu_cont_person_cd =rset.getString(1);
								    			addr_flag = rset.getString(2);
								    			cont_cd = rset.getString(3);
								    			inv_seq = rset.getString(4);
								    			cargo_no = rset.getString(5);
								    			sac_cd = rset.getString(6);
								    			
								    		}else {
								    			bu_cont_person_cd ="0";
								    			addr_flag = null;
								    			cont_cd = null;
								    			inv_seq = null;
								    			cargo_no ="0";
								    		}
								    		rset.close();
								    		stmt.close();	
								    		data  = bu_cont_person_cd;
								    		
								    	}
								    	else if(i == 11) { // INVOICE_SEQ
								    		data  = addr_flag;
								    		
								    	}
								    	else if(i == 12) { // SALE_PRICE
								    		data  = cont_cd;
								    	}
								    	else if(i == 14) { // SALE_PRICE_UNIT
								    		data  = inv_seq;
								    	}
								    	else if(i == 24) { // SALE_PRICE_UNIT
								    		data  = cargo_no;
								    	}
								    	else if(i == 25) {
								    		data  = sac_cd;
								    	}
								    	else {			    	
								    		if (i == 13) {	//alloc_dt
								    			inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		
								    		if (i == 16) {	//alloc_dt
								    			line_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		
								    		
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    	}	
								    	System.out.println(index+"-"+data);
								    	stmt1.setString(index++, data);			    	
								    }
									
								    queryString = "SELECT COUNTERPARTY_CD FROM FMS_DLNG_FFLOW_INV_DTL WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
								    		+ "AND BU_STATE_TIN = ?  AND INVOICE_TYPE = ? AND INVOICE_SEQ = ? AND LINE_NO = ? ";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, fin_yr);
							    	stmt.setString(3, state_code);
							    	stmt.setString(4, inv_type);
							    	stmt.setString(5, inv_seq);
							    	stmt.setString(6, line_no);
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !bu_seq_no.equals("")) {
										//System.out.println(queryString1);
											
										logger.data(fname, (company_cd+"," + cd + " , " + fin_yr + " , " + state_code + "," + inv_type + "," + inv_seq + "," + line_no + ","), conn, "");
										
										stmt1.executeUpdate();
										stmt1.close();
										
										logger_count++;
									}
							    	 else {
										stmt1.close();
										skipped_count++;     
										logger.data(fname, (company_cd+"," + cd + " , " + fin_yr + " , " + state_code + "," + inv_type + "," + inv_seq + "," + line_no + ","), conn, "E");
									}
							    	 
							    	 rset.close();
									 stmt.close();
								}
			}
			
			// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";		
	
		}
		
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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



public void FMS_DLNG_FFLOW_INV_TAX_DTL_SERV() throws IOException, SQLException {

	function_nm="FMS_DLNG_FFLOW_INV_TAX_DTL_SERV()";
	try {
		
		
		System.out.println("<<START>><<FMS_DLNG_FFLOW_INV_TAX_DTL_SERV>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_DLNG_FFLOW_INV_TAX_DTL_SERV>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,NULL,TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss'),"
				+ "NULL,TAX_AMT,GROSS_AMT_INR,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
				+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),GROSS_AMT_INR"
				+ "	FROM FMS_DLNG_FFLOW_INV_MST WHERE COMPANY_CD = ? AND INVOICE_TYPE = 'LP' ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_DLNG_FFLOW_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,"
				+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,TAX_CODE,", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(6);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			
			int count_desc=1;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						if(i == 5) {	//TAX_STRUCT_CD
							if(tax_struct_cd != null) {
								tax_struct_cd = rset.getString(6);
							}
							else {
								tax_struct_cd = "0";
							}
							data = tax_struct_cd;
						}
						else if(i == 6) {
							//TAX_CODE
							if (tax_struct_cd != null) {
								
								desc = rset2.getString(1);
								eff_dt = rset2.getString(2); 
								if(desc.contains(", "))
								{
									count_desc=desc.split(", ").length;
									String[] parts = desc.split(", ");
									desc1 = parts[j];
									desc=desc1;
									
								}
//								else {
//									desc = null;
//								}
								if (!tax_struct_cd.equals("0")) {
									if(desc!=null)
									{
										desc_nm=desc.split(" ")[0];
									}
									
									queryString3 = "SELECT TAX_CODE FROM FMS_TAX_MST WHERE TAX_ALIAS_CODE = ? ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, desc_nm);
									
									rset3 = stmt3.executeQuery();
									if (rset3.next()) {
										data = rset3.getString(1);
									}
									else {
										data = "0";
									}
									rset3.close();
									stmt3.close();
								} 
								else {
									data = "0";
								}
							}
							else {
									data = "0";
								}
							tax_code = data;
							
						}
						
						else if(i == 7) {	//TAX_EFF_DT
							data = eff_dt;
						}
						else if(i == 8) {	//TAX_DESCR
						   if(tax_struct_cd != null) {
								if(!tax_struct_cd.equals("0")) {
									data=desc;
								}
						   }
						    else {
									data = null;
							}
						}
						else if(i==9)
						{
							
							double tax_amt = 0.0;
							if (desc != null) {
							    if ( !desc.contains("on") ) {
							    	
							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
//							        else {
							            gross_amt = rset.getString(16);
							            tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100);
							            adv_amt = tax_amt + "";
							            adv=adv_amt;
//							        }
							    }
							    else if (desc.contains("on")) {

							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
							        tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100);
							           
							    }
							}
							data = tax_amt + "";

						}
						
						else if(i == 10 && desc != null && desc.contains("on")) {

							data = adv;
						}
						
//						System.out.println(i+1 +"=="+ data );
						stmt1.setString(i+1,data);
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_DLNG_FFLOW_INV_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? AND INVOICE_TYPE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(6));
			stmt5.setString(6, tax_code);
			stmt5.setString(7, rset.getString(5));
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt5.close();
				rset5.close();
			}
			rset2.close();
			stmt2.close();
			}
		}
		rset.close();
		stmt.close();

		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
	
		System.out.println("<<END>><<FMS_DLNG_FFLOW_INV_TAX_DTL_SERV>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_DLNG_FFLOW_INV_TAX_DTL_SERV>>,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);

		
	
	}
	catch(Exception e)
	{
		msg = "One of the Functions faced an Error. Data Not Inserted.";
		msg_type = "E";
		
		conn.rollback();
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
	finally {
		conn.commit();
	}

}


public void FMS_DLNG_FFLOW_INV_FILE_DTL_SERV() throws IOException, SQLException {
	function_nm="FMS_DLNG_FFLOW_INV_FILE_DTL_SERV()";
	try {
		
		table_name = "FMS_DLNG_FFLOW_INV_FILE_DTL_SERV";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
		
		queryString1 = "INSERT INTO FMS_DLNG_FFLOW_INV_FILE_DTL(COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,INVOICE_TYPE,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,"
				+ "PDF_SIGNED,SIGNED_BY,SIGNED_DT,EMAIL_SENT,EMAIL_SENT_BY,EMAIL_SENT_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_FILE_DTL_SERV_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_DLNG_FFLOW_INV_FILE_DTL_SERV_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	

				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String inv_type="LP";
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
							data = fin_yr;
						}
						else if(i == 2) {
							inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN FROM FMS_DLNG_FFLOW_INV_MST WHERE INVOICE_ID_SEQ = ? AND FINANCIAL_YEAR = ? "
									+ "AND COUNTERPARTY_CD = ? AND INVOICE_TYPE = 'LP' AND COMPANY_CD = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, inv_seq);
							stmt.setString(2, fin_yr);
							stmt.setString(3, cd);
							stmt.setString(4, company_cd);
							
							rset = stmt.executeQuery();
							if (rset.next()) {
								seq_no = rset.getString(1);
								bu_seq = rset.getString(2);
							} else {
								seq_no ="0";
								bu_seq ="0";
							}
							rset.close();
							stmt.close();
							
							data = bu_seq;
						}
						
						else if(i == 3) {
							data = seq_no;
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
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) = ? ";
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
							}
							else {
								sign_by_cd = "0";
							}
							data = sign_by_cd;
						}
						
						
						else {
							 if (i == 5) {	// PDF_TYPE
								pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 6) {	//FILE_NAME
								file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 12) {	//SIGNED_BY
								sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_DLNG_FFLOW_INV_FILE_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND PDF_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, seq_no);
					stmt.setString(4, fin_yr);
					stmt.setString(5, inv_type);
					stmt.setString(6, pdf_type);
					System.out.println(company_cd+"=="+bu_seq+"=="+inv_seq+"=="+fin_yr+"=="+inv_type+"=="+pdf_type);
					rset = stmt.executeQuery();
					
					if (!rset.next()){
						
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
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
	
	public void setPdf_path(String path) {
		pdf_path = path;
	}
	
	public void setSysDateTime(String dt) {
		sysDateTime = dt;
	}
	
	public void setStart_End_Dt(String dt) {
		start_end_dt = dt;
	}
}
