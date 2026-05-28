package com.etrm.fms.migration;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;

		

public class Derivative_SEIPL_Data_Reader {

	

	String db_src_file_name="Derivative_SEIPL_Data_Reader.java";

	String migration_setup_dir = "";
	
	String queryString="", queryString1="",queryString2="",queryString3="";
	Connection conn;
	ResultSet rset,rset1,rset2,rset3;
	PreparedStatement stmt,stmt1,stmt2,stmt3;
	
	String function_nm = "", columns = "", data = "",table_name="";
	
	String sysDateTime = "";
	String start_end_dt = null;
	
	String fname = "";
	String fname_error = "";
	
	String fname1 = "";
	String pdf_path = "";

	int index = 0;
	int logger_count = 0;
	int skipped_count=0;  
	int total_count=0;  
	
	final String company_cd = "2";
	String cd = "", eff_dt = "";

	String checked_values = "", msg = "", msg_type = "",abbr="",cont_no="",cont_ref="",agmt_no="",agmt_type="",agmt_rev="",cont_rev="",cont_type="",state_code="";
	String transporter_map = "", meter_map = "",prev_cd="",map_seq_no="",sec_ref_no="",plant_seq_no="",bu_seq_no="",bu_plant_state="",bu_cont_person_cd="";

	File file1 = null;
	FileInputStream file = null;
	XSSFWorkbook workbook = null;
	XSSFSheet sheet = null;
	Iterator<Row> rowIterator = null;
	Iterator<Cell> cellIterator = null;
	Row row;
	Cell cell;
	BufferedWriter out;
	File file2 = null;
	
	DataMigration_Logger logger = new DataMigration_Logger();
	
	public void init() {

		function_nm="init()";
		try {
			
			fname = "DataLogs/Reader/Derivative_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/Derivative_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
			fname = migration_setup_dir + fname;
			fname_error = migration_setup_dir + fname_error;
			
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
				if(conn != null && !checked_values.equals(""))  
				{
					conn.setAutoCommit(false);
					
					if (checked_values.contains("FMS_DERV_AGMT_MST,")) {
						FMS_DERV_AGMT_MST();
						FMS_DERV_AGMT_BU();
						FMS_DERV_AGMT_PLANT();
						FMS_DERV_AGMT_BILLING_DTL();
					}
					if (checked_values.contains("FMS_DERV_CONT_MST,")) {
						FMS_DERV_CONT_MST();
						FMS_DERV_CONT_BU();
						FMS_DERV_CONT_PLANT();
						FMS_SECURITY_DEAL_MAP();
						FMS_SECURITY_MST();
					}
					if (checked_values.contains("FMS_DERV_INSTRUMENT_MST,")) {
						FMS_DERV_INSTRUMENT_MST();
					}
					if (checked_values.contains("FMS_DERV_CONT_BILLING_DTL,")) {
						FMS_DERV_CONT_BILLING_DTL();
					}
					if (checked_values.contains("FMS_DERV_INVOICE_MST,")) {
						FMS_DERV_INVOICE_MST();
					}
					if (checked_values.contains("FMS_DERV_INV_FILE_DTL,")) {
						FMS_DERV_INV_FILE_DTL();
					}
					if (checked_values.contains("FMS_DERV_HEDGE_EXPOSURE_DTL,")) {
						FMS_DERV_HEDGE_EXPOSURE_DTL();
					}
					if (checked_values.contains("FMS_DERV_INV_FILE_DTL_UPDATE,")) {
						FMS_DERV_INV_FILE_DTL_UPDATE();
					}
					
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


	public void FMS_DERV_AGMT_MST() throws IOException, SQLException {

		function_nm="FMS_DERV_AGMT_MST()";
		try {
			
			System.out.println("<<START>><<FMS_DERV_AGMT_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DERV_AGMT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String agmt_no="",agmt_rev="";
			
			queryString1 = "INSERT INTO FMS_DERV_AGMT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,SIGNING_DT,START_DT,END_DT,"
					+ "STATUS,BILLING_FLAG,BILLING_CLAUSE,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DERV_AGMT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DERV_AGMT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

//				logger.checkpoint(fname, "COMPANY_CD,TANK_CD,EFF_DT,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = "";
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							
							if(cell.getColumnIndex() == 0) {	//COMPANY_CD
								abbr = cell.getStringCellValue().contains("'null'") ? "Null" : cell.getStringCellValue();
								if (!abbr.equals("Null")) {
									abbr = abbr.substring(1,abbr.length()-1).toUpperCase();
								}
								data = company_cd;
							}
							else if(cell.getColumnIndex() == 1) {	//COUNTERPARTY_CD
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if(rset.next()) {
									cd = rset.getString(1);
								}
								else {
									cd = "";
								}
								rset.close();
								stmt.close();
								
								data = cd;
							}
							else if(cell.getColumnIndex() == 5) {
								data = "SEIPL-"+abbr+"-U1-REV0";
							}
							else {
								if (cell.getColumnIndex() == 3) {
									agmt_no = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if (agmt_no!=null) {
										agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
									}
								}
								if (cell.getColumnIndex() == 4) {
									agmt_rev = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if (agmt_rev!=null) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
								}
								
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();

								if(data != null) {
							    	data = data.substring(1, data.length()-1);
							    }
							}
							//System.out.println(index+"::"+data);
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT AGMT_NAME FROM FMS_DERV_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_rev);
						
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + cd + "," + cont_ref + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + cont_ref  + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_DERV_AGMT_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_AGMT_MST>>,,", conn);
			
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
	
	public void FMS_DERV_AGMT_BU() throws IOException, SQLException {

		function_nm="FMS_DERV_AGMT_BU()";
		try {
			
			System.out.println("<<START>><<FMS_DERV_AGMT_BU>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DERV_AGMT_BU>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
//			logger_count = 0;   
//			skipped_count = 0;   
//			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,'1',ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_DERV_AGMT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_DERV_AGMT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			//logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
			
			while (rset.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_DERV_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = 'U' AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = '1' ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(4));
				stmt2.setString(4, rset.getString(5));
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
					//skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				}
			rset.close();
			stmt.close();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_DERV_AGMT_BU>>");
			System.out.println();
		
//			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_AGMT_BU>>,", conn);
			
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
	
	public void FMS_DERV_AGMT_PLANT() throws IOException, SQLException {

		function_nm="FMS_DERV_AGMT_PLANT()";
		try {
			
			System.out.println("<<START>><<FMS_DERV_AGMT_PLANT>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DERV_AGMT_PLANT>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
//			logger_count = 0;   
//			skipped_count = 0;   
//			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,'1',ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_DERV_AGMT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_DERV_AGMT_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			//logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
			
			while (rset.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_DERV_AGMT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = 'U' AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = '1' ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(4));
				stmt2.setString(4, rset.getString(5));
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
					//skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				}
			rset.close();
			stmt.close();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_DERV_AGMT_PLANT>>");
			System.out.println();
		
//			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_AGMT_PLANT>>,", conn);
			
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
	
	
	public void FMS_DERV_AGMT_BILLING_DTL() throws IOException, SQLException {

		function_nm="FMS_DERV_AGMT_BILLING_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_DERV_AGMT_BILLING_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DERV_AGMT_BILLING_DTL>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String due_days="",billing_days="";
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,"
					+ "INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,"
					+ "MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE";
			
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,'S','S',NULL,NULL,'2','2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'),ENT_BY,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL FROM FMS_DERV_AGMT_MST "
					+ "WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_DERV_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
					+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,"
					+ "EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,"
					+ "EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?)";
			
			//logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
			
			while (rset.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					cd = rset.getString(2);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? null : rset.getString(i+1);
						
						if(i == 1) {
							queryString3 = "SELECT COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_CD = ? ";
				    		stmt3 = conn.prepareStatement(queryString3);
				    		stmt3.setString(1, cd);
				    		rset3 = stmt3.executeQuery();
				    		if (rset3.next()) {
				    			abbr=rset3.getString(1);
				    		}
				    		rset3.close();
				    		stmt3.close();
						}
						else if(i == 7 && abbr!=null) 
						{
							due_days = "";billing_days="";
							if(abbr.equals("SPSSETL1")) 
							{
								due_days = "14";
								billing_days = "C";
							}
							else if(abbr.equals("SIETCO")) 
							{
								due_days = "5";
								billing_days = "B";
								
							}
							else if(abbr.equals("SILS")) 
							{
								due_days = "5";
								billing_days = "B";
							}
							data = due_days;
						}
						else if(i == 23) 
						{
							data = billing_days;
						}
						else if(i == 29) {
							queryString3 = "SELECT B.TIN FROM FMS_COUNTERPARTY_PLANT_DTL A,FMS_STATE_MST B WHERE A.COUNTERPARTY_CD = ? AND A.SEQ_NO = ? AND B.STATE_NM = A.PLANT_STATE ";
				    		stmt3 = conn.prepareStatement(queryString3);
				    		stmt3.setString(1, cd);
				    		stmt3.setString(2, "1");
				    		rset3 = stmt3.executeQuery();
				    		if (rset3.next()) {
				    			state_code=rset3.getString(1);
				    		}
				    		else
				    		{
				    			state_code=null;
				    		}
				    		rset3.close();
				    		stmt3.close();
				    		data = state_code;
						}
						
						//System.out.println(i+1+"-"+data);
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_DERV_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = 'U' AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = '1' ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(4));
				stmt2.setString(4, rset.getString(5));
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
					//skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				}
			rset.close();
			stmt.close();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_DERV_AGMT_BILLING_DTL>>");
			System.out.println();
		
//			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_AGMT_PLANT>>,", conn);
			
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
	
	public void FMS_DERV_CONT_MST() throws IOException, SQLException {

		function_nm="FMS_DERV_CONT_MST()";
		try {
			
			System.out.println("<<START>><<FMS_DERV_CONT_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DERV_CONT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
			int no = 0;
			
			queryString1 = "INSERT INTO FMS_DERV_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CONT_NAME,"
					+ "CONT_REF_NO,CONT_STATUS,DDA_DT,DDA_TIME,DDA_NOTE,SIGNING_DT,SIGNING_TIME,TRADE_DT,TRADE_TIME,NO_OF_INSTRUMENT,REMARKS,BILLING_FLAG,"
					+ "BILLING_CLAUSE,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT) VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DERV_CONT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DERV_CONT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

//				logger.checkpoint(fname, "COMPANY_CD,TANK_CD,EFF_DT,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = "";
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							
							if(cell.getColumnIndex() == 0) {	//COMPANY_CD
								abbr = cell.getStringCellValue().contains("'null'") ? "Null" : cell.getStringCellValue();
								if (!abbr.equals("Null")) {
									abbr = abbr.substring(1,abbr.length()-1).toUpperCase();
								}
								data = company_cd;
							}
							else if(cell.getColumnIndex() == 1) {	//COUNTERPARTY_CD
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if(rset.next()) {
									cd = rset.getString(1);
								}
								else {
									cd = "";
								}
								rset.close();
								stmt.close();
								
								data = cd;
							}
							else if(cell.getColumnIndex() == 6) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
									cont_no = cont_no.substring(1, cont_no.length() - 1);
									no = Integer.parseInt(cont_no.substring(2));
								
									queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_DERV_CONT_MST WHERE CONT_NO LIKE ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cont_no.substring(2)+"%");
						    		rset = stmt.executeQuery();
						    		if (rset.next() && rset.getInt(1) > 0) {
						    			no = rset.getInt(1);					    				
						    		}else{					    		
						    			no = no * 10000;
		//				    			queryString2 = "SELECT TO_CHAR(SYSDATE, 'YYYY') FROM DUAL";
		//				    			stmt2 = conn.prepareStatement(queryString2);
		//				    			rset2 = stmt2.executeQuery();				    			
		//				    			
		//				    			if(rset2.next()) {				    				
						    				//no = no * Integer.parseInt(cont_no.substring(2));
						    				no++;
		//				    			}
		//				    			rset2.close();
		//				    			stmt2.close();	
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		data = no+"";
							}
							else if(cell.getColumnIndex() == 8) {
								data = "SEIPL-"+abbr+"-HEDGE1-REV0 V"+no+"-REV0";
							}
							else {
								if (cell.getColumnIndex() == 3) {
									agmt_no = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if (agmt_no!=null) {
										agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
									}
								}
								if (cell.getColumnIndex() == 4) {
									agmt_rev = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if (agmt_rev!=null) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
								}
								if (cell.getColumnIndex() == 9) { //Cont_ref
									cont_ref = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if (cont_ref!=null) {
										cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
									}
								}
								
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();

								if(data != null) {
							    	data = data.substring(1, data.length()-1);
							    }
							}
							//System.out.println(index+"::"+data);
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT CONT_NO FROM FMS_DERV_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? "
								+ "AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CONT_REF_NO = ?";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, "U");
						stmt.setString(4, agmt_no);
						stmt.setString(5, agmt_rev);
						stmt.setString(6, "V");
						stmt.setString(7, "0");
						stmt.setString(8, cont_ref);
						
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
//							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
//							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_DERV_CONT_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_CONT_MST>>,,", conn);
			
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
	
	
	public void FMS_DERV_CONT_BU() throws IOException, SQLException {

		function_nm="FMS_DERV_CONT_BU()";
		try {
			
			System.out.println("<<START>><<FMS_DERV_CONT_BU>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DERV_CONT_BU>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
//			logger_count = 0;   
//			skipped_count = 0;   
//			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,'1',ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_DERV_CONT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_DERV_CONT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			//logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
			
			while (rset.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_DERV_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = 'U' AND AGMT_NO = ? "
						+ "AND AGMT_REV = ? AND CONTRACT_TYPE = 'V' AND CONT_NO = ? AND CONT_REV = '0' AND PLANT_SEQ_NO = '1' ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(4));
				stmt2.setString(4, rset.getString(5));
				stmt2.setString(5, rset.getString(7));
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
					//skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				}
			rset.close();
			stmt.close();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_DERV_CONT_BU>>");
			System.out.println();
		
//			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_CONT_BU>>,", conn);
			
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
	
	
	public void FMS_DERV_CONT_PLANT() throws IOException, SQLException {

		function_nm="FMS_DERV_CONT_PLANT()";
		try {
			
			System.out.println("<<START>><<FMS_DERV_CONT_PLANT>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DERV_CONT_PLANT>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
//			logger_count = 0;   
//			skipped_count = 0;   
//			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,'1',ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_DERV_CONT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_DERV_CONT_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			//logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
			
			while (rset.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_DERV_CONT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = 'U' AND AGMT_NO = ? "
						+ "AND AGMT_REV = ? AND CONTRACT_TYPE = 'V' AND CONT_NO = ? AND CONT_REV = '0' AND PLANT_SEQ_NO = '1' ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(4));
				stmt2.setString(4, rset.getString(5));
				stmt2.setString(5, rset.getString(7));
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
					//skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				}
			rset.close();
			stmt.close();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_DERV_CONT_PLANT>>");
			System.out.println();
		
//			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_CONT_PLANT>>,", conn);
			
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
 	
	public void FMS_SECURITY_DEAL_MAP() throws IOException, SQLException {

		function_nm="FMS_SECURITY_DEAL_MAP()";
		try {
			
			System.out.println("<<START>><<FMS_SECURITY_DEAL_MAP>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SECURITY_DEAL_MAP>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
//			logger_count = 0;   
//			skipped_count = 0;   
//			total_count = 0;   
			String seq_no="";
			int num = 1;
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,"
	    			+ "ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT";	    	
			
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,NULL,'1',NULL,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,NULL,NULL,'0',NULL,'K',NULL FROM FMS_DERV_CONT_MST WHERE COMPANY_CD = ? "
					+ "ORDER BY COUNTERPARTY_CD";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO, "
					+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?) ";
			
			//logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
			
			while (rset.next()) {
				
					stmt1 = conn.prepareStatement(queryString1);
					
					cd  = rset.getString(2) == null ? "null" : rset.getString(2);
					
					queryString3 = "SELECT COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST A WHERE COUNTERPARTY_CD = ? ";
		    		stmt3 = conn.prepareStatement(queryString3);
		    		stmt3.setString(1, cd);
		    		rset3 = stmt3.executeQuery();
		    		if (rset3.next()) {
		    			abbr=rset3.getString(1);
		    		}
		    		
		    		rset3.close();
		    		stmt3.close();
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? null : rset.getString(i+1);
						
						if(i==2) {
							queryString3 = "SELECT NVL(MAX(SEQ_NO),0)+1 FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? ";
				    		stmt3 = conn.prepareStatement(queryString3);
				    		stmt3.setString(1, cd);
				    		rset3 = stmt3.executeQuery();
				    		if (rset3.next() ) {
				    			seq_no=rset3.getString(1);
				    		}else {
				    			seq_no = "1";
				    		}
				    		//System.out.println(":"+seq_no);
				    		rset3.close();
				    		stmt3.close();
							
							data = seq_no;
						}

						else if (i == 4) {	// sec_ref_no
							sec_ref_no = abbr + "-S-" + seq_no;
							data = sec_ref_no;
						}

						//System.out.println(i+1+"-"+data);
						stmt1.setString(i+1,data);

					}
					
				//for data already exists..
					
					
					queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND MAP_SEQ_NO = '1' "
							+ " AND  SEQ_REV_NO = ? AND GX = ? AND AGMT_NO = '1' AND AGMT_REV = '0' AND CONT_NO = ? AND CONT_REV = '0' AND CONTRACT_TYPE = ?";
					
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, company_cd);
					stmt2.setString(2, cd);
					stmt2.setString(3, rset.getString(17));
					stmt2.setString(4, "K");
					stmt2.setString(5, rset.getString(8));
					stmt2.setString(6, "V");
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
					//skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				}
			rset.close();
			stmt.close();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_SECURITY_DEAL_MAP>>");
			System.out.println();
		
//			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SECURITY_DEAL_MAP>>,", conn);
			
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
	
	public void FMS_SECURITY_MST() throws IOException, SQLException {

		function_nm="FMS_SECURITY_MST()";
		try {
			
			System.out.println("<<START>><<FMS_SECURITY_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_SECURITY_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
//			logger_count = 0;   
//			skipped_count = 0;   
//			total_count = 0;   
			String seq_no="";
			int num = 1;
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,"
					+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,RECEIPT_DT,"
					+ "ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,"
					+ "APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,"
					+ "PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD"; 	
			
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_DERV_CONT_MST WHERE COMPANY_CD = ? "
					+ "ORDER BY COUNTERPARTY_CD";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			while(rset.next()) {
			
			queryString3 = "SELECT COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,'R','OA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
					+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'O',NULL,NULL,NULL,ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "NULL,NULL,NULL,NULL,'0','K',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? "
					+ "AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1,company_cd);
			stmt3.setString(2,rset.getString(2));
			stmt3.setString(3,rset.getString(3));
			stmt3.setString(4,rset.getString(4));
			stmt3.setString(5,rset.getString(5));
			stmt3.setString(6,rset.getString(6));
			stmt3.setString(7,rset.getString(7));
			rset3 = stmt3.executeQuery();
				
			
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
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,? "
					+ ") ";
			
			
			//logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
			
			while (rset3.next()) {
				
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset3.getString(i+1) == null ? null : rset3.getString(i+1);
						
						//System.out.println(i+1+"-"+data);
						stmt1.setString(i+1,data);

					}
					
				//for data already exists..
					
					queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND SEQ_NO = ? AND SEC_REF_NO = ?"
							+ " AND  SEQ_REV_NO = ? AND GX = ? ";
					
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, company_cd);
					stmt2.setString(2, rset3.getString(2));
					stmt2.setString(3, rset3.getString(3));
					stmt2.setString(4, rset3.getString(4));
					stmt2.setString(5, "0");
					stmt2.setString(6, "K");
			
				rset2 = stmt2.executeQuery();
				
				if (!rset2.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
//					logger_count++;
				}
				else {
					
					stmt1.close();
					//skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt2.close();
				rset2.close();
				}
		
			rset3.close();
			stmt3.close();
		
			}
			rset.close();
			stmt.close();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_SECURITY_MST>>");
			System.out.println();
		
//			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
//			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_SECURITY_MST>>,", conn);
			
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
	
	public void FMS_DERV_INSTRUMENT_MST() throws IOException, SQLException {

		function_nm="FMS_DERV_INSTRUMENT_MST()";
		try {
			
			System.out.println("<<START>><<FMS_DERV_INSTRUMENT_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DERV_INSTRUMENT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
			
			queryString1 = "INSERT INTO FMS_DERV_INSTRUMENT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_TYPE,CONTRACT_TYPE,CONT_NO,AGMT_REV,CONT_REV,"
					+ "INSTRUMENT_NO,INSTRUMENT_TYPE,BUY_SELL,STATUS,QTY,QTY_UNIT,RATE,RATE_UNIT,PRODUCT_NM,CURVE_NM,PROJ_METHOD,CONT_DD_MM_YR,PRICE_START_DT,"
					+ "PRICE_END_DT,CONV_FACTOR,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DERV_INSTRUMENT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DERV_INSTRUMENT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

//				logger.checkpoint(fname, "COMPANY_CD,TANK_CD,EFF_DT,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = "";
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							
							if(cell.getColumnIndex() == 0) {	//COMPANY_CD
								abbr = cell.getStringCellValue().contains("'null'") ? "Null" : cell.getStringCellValue();
								if (!abbr.equals("Null")) {
									abbr = abbr.substring(1,abbr.length()-1).toUpperCase();
								}
								data = company_cd;
							}
							else if(cell.getColumnIndex() == 1) {	//COUNTERPARTY_CD
								cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
					    		if (!cont_ref.equals("NULL")) {
									cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
								}
								
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if(rset.next()) {
									cd = rset.getString(1);
								}
								else {
									cd = "";
								}
								rset.close();
								stmt.close();
								
								data = cd;
							}
							else if(cell.getColumnIndex() == 2) {
								queryString = "SELECT AGMT_NO,AGMT_TYPE,CONTRACT_TYPE,CONT_NO,AGMT_REV,CONT_REV FROM FMS_DERV_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_type = rset.getString(2);
					    			cont_type = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			agmt_rev = rset.getString(5);
					    			cont_rev = rset.getString(6);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = agmt_no;
					    		
							}
							else if(cell.getColumnIndex() == 3) {
								data = agmt_type;
							}
							else if(cell.getColumnIndex() == 4) {
								data = cont_type;
							}
							else if(cell.getColumnIndex() == 5) {
								data = cont_no;
							}
							else if(cell.getColumnIndex() == 6) {
								data = agmt_rev;
							}
							else if(cell.getColumnIndex() == 7) {
								data = cont_rev;
							}
							
							else {

								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();

								if(data != null) {
							    	data = data.substring(1, data.length()-1);
							    }
							}
							//System.out.println(index+"::"+data);
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT CONT_NO FROM FMS_DERV_INSTRUMENT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_TYPE = ?"
								+ "AND CONTRACT_TYPE = ? AND CONT_NO = ?  AND AGMT_REV = ? AND CONT_REV = ? AND INSTRUMENT_NO = ?";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_type);
						stmt.setString(5, cont_type);
						stmt.setString(6, cont_no);
						stmt.setString(7, agmt_rev);
						stmt.setString(8, cont_rev);
						stmt.setString(9, "1");

						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
							//System.out.println(queryString1);
							
//							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
//							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_DERV_INSTRUMENT_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_INSTRUMENT_MST>>,,", conn);
			
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
	
	
	public void FMS_DERV_CONT_BILLING_DTL() throws IOException, SQLException {

		function_nm="FMS_DERV_CONT_BILLING_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_DERV_CONT_BILLING_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DERV_CONT_BILLING_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;  
			
			queryString1 = "INSERT INTO FMS_DERV_CONT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
					+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
					+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,"
					+ "EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DERV_CONT_BILLING_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DERV_CONT_BILLING_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

//				logger.checkpoint(fname, "COMPANY_CD,TANK_CD,EFF_DT,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = "";
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							
							if(cell.getColumnIndex() == 0) {	//COMPANY_CD
								abbr = cell.getStringCellValue().contains("'null'") ? "Null" : cell.getStringCellValue();
								if (!abbr.equals("Null")) {
									abbr = abbr.substring(1,abbr.length()-1).toUpperCase();
								}
								data = company_cd;
							}
							else if(cell.getColumnIndex() == 1) {	//COUNTERPARTY_CD
								cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
					    		if (!cont_ref.equals("NULL")) {
									cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
								}
								
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, abbr);
								rset = stmt.executeQuery();
								if(rset.next()) {
									cd = rset.getString(1);
								}
								else {
									cd = "";
								}
								rset.close();
								stmt.close();
								
								data = cd;
							}
							else if(cell.getColumnIndex() == 2) {
								queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_DERV_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_type = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			cont_type = rset.getString(6);
					    		} else {
					    			agmt_type = "";
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = agmt_type;
					    		
							}
							else if(cell.getColumnIndex() == 3) {
								data = agmt_no;
							}
							else if(cell.getColumnIndex() == 4) {
								data = agmt_rev;
							}
							else if(cell.getColumnIndex() == 5) {
								data = cont_no;
							}
							else if(cell.getColumnIndex() == 6) {
								data = cont_rev;
							}
							else if(cell.getColumnIndex() == 7) {
								data = cont_type;
							}
							
							else if (cell.getColumnIndex() == 32) { //holiday_state
					    		queryString = "SELECT B.TIN FROM  FMS_COUNTERPARTY_PLANT_DTL A,FMS_STATE_MST B WHERE A.COUNTERPARTY_CD = ? AND A.SEQ_NO = ? AND B.STATE_NM = A.PLANT_STATE ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, "1");
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

								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();

								if(data != null) {
							    	data = data.substring(1, data.length()-1);
							    }
							}
							
							//System.out.println(index+"::"+data);
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT CONT_NO FROM FMS_DERV_CONT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_TYPE = ?"
								+ "AND CONTRACT_TYPE = ? AND CONT_NO = ?  AND AGMT_REV = ? AND CONT_REV = ? AND PLANT_SEQ_NO = ?";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_type);
						stmt.setString(5, cont_type);
						stmt.setString(6, cont_no);
						stmt.setString(7, agmt_rev);
						stmt.setString(8, cont_rev);
						stmt.setString(9, "1");

						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
							//System.out.println(queryString1);
							
//							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
//							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_DERV_CONT_BILLING_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_CONT_BILLING_DTL>>,,", conn);
			
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
	
	
	public void FMS_DERV_INVOICE_MST() throws IOException, SQLException {
		
		function_nm="FMS_DERV_INVOICE_MST()";
		try {
			
			table_name = "FMS_DERV_INVOICE_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			queryString1 = "INSERT INTO FMS_DERV_INVOICE_MST (COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
						+ "BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,INVOICE_REF_NO,INV_TYPE,FREQ,PERIOD_START_DT,"
						+ "PERIOD_END_DT,DUE_DT,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,"
						+ "INV_STATUS,REMARK_1,REMARK_2,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,"
						+ "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,INVOICE_ID_SEQ,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,"
						+ "PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,"
						+ "INSTRUMENT_NO,FIN_SYS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
			            + " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			            + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
			            + " ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, ? , TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
			            + "?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),  ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
			            + "?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DERV_INVOICE_MST_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DERV_INVOICE_MST_"+start_end_dt+".csv";
				try (
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {

									String line = br.readLine();
									
									logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ_NO,CONTACT_PERSON_CD,TIMESTAMP,", conn);
									
									while ((line = br.readLine()) != null) {
										total_count++; 
										String financial_year="",contact_person_cd="",inv_seq_no="";
										agmt_no = ""; agmt_rev = "";
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
									    	else if (i == 1) {	// Counterparty_Cd
									    		
									    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);

									    		if(cont_ref.endsWith("(")) {
									    			cont_ref = cont_ref.substring(0, cont_ref.length()-1);
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
											else if (i == 3) { //Agmt_no

									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_DERV_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? ";
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
									    		bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];

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
												
												String addr_flag = "";
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
//									    	
									    	else if (i == 11) {	//plant_seq
								    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];

								    			data = plant_seq_no; 
								    		}
									    	else if(i == 12)
									    	{
//									    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//									    		
//									    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
//														
//										    	stmt = conn.prepareStatement(queryString);
//										    	stmt.setString(1, mail);
//										    	stmt.setString(2, cd);
//										    	rset = stmt.executeQuery();
//										    	
//										    	if (rset.next()) {
//										    		contact_person_cd = rset.getString(1);
//										    	}
//										    	else {
//										    		contact_person_cd = "1";
//										    	}
//										    	rset.close();
//										    	stmt.close();
									    		data = "1";
									    	}
//	
									    	else {			    									    		
									    		
									    		if(i == 2) {
										    		financial_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										    	}
									    		
									    		else if(i == 13) {
									    			inv_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										    	}
									    		
										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];

									    	}	
									    	//System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);		    	
									    }
										
									    queryString = "SELECT COUNTERPARTY_CD FROM FMS_DERV_INVOICE_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
									    		+ "AND FINANCIAL_YEAR = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT =? "
									    		+ "AND BU_STATE_TIN = ?  AND PLANT_SEQ = ? AND INVOICE_SEQ = ?";
								    	stmt = conn.prepareStatement(queryString);
								    	stmt.setString(1, company_cd);
								    	stmt.setString(2, cd);
								    	stmt.setString(3, financial_year);
								    	stmt.setString(4, agmt_no);
								    	stmt.setString(5, agmt_rev);
								    	stmt.setString(6, cont_no);
								    	stmt.setString(7, cont_rev);
								    	stmt.setString(8, cont_type);
								    	stmt.setString(9, bu_seq_no);
								    	stmt.setString(10, state_code);
								    	stmt.setString(11, plant_seq_no);
								    	stmt.setString(12, inv_seq_no);
								    	
								    	rset = stmt.executeQuery();
								    	
								    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") ) {
												//System.out.println(queryString1);
												
												logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," ), conn, "");
												
												stmt1.executeUpdate();
												stmt1.close();
												
												logger_count++;
										}
								    	 else {
												stmt1.close();
												skipped_count++;     
												logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," ), conn, "E");
											
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
	
	
	public void FMS_DERV_INV_FILE_DTL() throws IOException, SQLException {
		function_nm="FMS_DERV_INV_FILE_DTL()";
		try {
			
			table_name = "FMS_DERV_INV_FILE_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,inv_no,inv_type;
			
			queryString1 = "INSERT INTO FMS_DERV_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,INV_TYPE,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,"
					+ "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT,PDF_CONTENT,SF_GEN_DT,SIGNED_ENT_BY) "
					+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DERV_INV_FILE_DTL_"+start_end_dt+".csv");
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DERV_INV_FILE_DTL_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	

					String line = br.readLine();
					
					logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) { ; 
						abbr = "";
						bu_seq="";inv_seq="";fin_yr="";pdf_type="";file_nm="";inv_no="";inv_type="";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						

						for (int i = 0; i < line.split(",").length; i++)
						{
							data = null;
							if (i == 0) {
								state_code = "";inv_type="";fin_yr="";inv_seq="";
								inv_no  = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								queryString="SELECT BU_STATE_TIN,INV_TYPE,FINANCIAL_YEAR,INVOICE_SEQ FROM FMS_DERV_INVOICE_MST WHERE INVOICE_NO = ?";
								stmt=conn.prepareStatement(queryString);
								stmt.setString(1, inv_no);
								rset = stmt.executeQuery();
					    		if (rset.next()) {				    			
					    			state_code = rset.getString(1);
					    			inv_type = rset.getString(2);
					    			fin_yr =  rset.getString(3);
					    			inv_seq =  rset.getString(4);
					    		}	
					    		rset.close();
					    		stmt.close();
					    		
					    		data = company_cd;
								
							}
							else if(i == 1) {
								data = state_code;
							}
							else if (i == 2) {
								data = inv_seq;
							}
							else if(i == 3) {
								data = inv_type;
							}
							else if(i == 4) {
								data = fin_yr;
							}
							
							else {
								if(i == 5) {
									pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
									data = line.split(",")[i].contains("null") ? null : line.split(",")[i];

							}
							
							stmt1.setString(index++, data);
							
						}
						
						queryString = "SELECT COMPANY_CD FROM FMS_DERV_INV_FILE_DTL WHERE "
								+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND PDF_TYPE = ? ";	//AND INV_TYPE = ?";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, state_code);
						stmt.setString(3, inv_seq);
						stmt.setString(4, fin_yr);
						stmt.setString(5, pdf_type);
//						stmt.setString(6, inv_type);
						rset = stmt.executeQuery();
						
						if (!rset.next()){
							
							logger.data(fname, (company_cd + "," + state_code + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + inv_no  + "," + inv_type  + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd + "," + state_code + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + inv_no  + "," + inv_type  + ","), conn, "E");
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
		
	
	public void FMS_DERV_HEDGE_EXPOSURE_DTL() throws IOException, SQLException {
		function_nm="FMS_DERV_HEDGE_EXPOSURE_DTL()";
		try {
			
			table_name = "FMS_DERV_HEDGE_EXPOSURE_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			String hedge_dt="";
			
			
			queryString1 = "INSERT INTO FMS_DERV_HEDGE_EXPOSURE_DTL (COMPANY_CD,HEDGE_DT,BALANCE_QTY,ENT_BY,ENT_DT,FLAG) "
					+ "VALUES(?,TO_DATE('01/04/2023', 'DD/MM/YYYY'),'47424048',1,TO_DATE('01/04/2023', 'DD/MM/YYYY'),'L')";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
				
			//for data already exists..
			queryString = "SELECT COMPANY_CD FROM FMS_DERV_HEDGE_EXPOSURE_DTL WHERE "
					+ "COMPANY_CD = ? AND HEDGE_DT = TO_DATE('01/04/2023', 'DD/MM/YYYY')";
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
			
		
			queryString1 = "INSERT INTO FMS_DERV_HEDGE_EXPOSURE_DTL(COMPANY_CD,HEDGE_DT,BALANCE_QTY,ENT_BY,ENT_DT,FLAG) "
					+ "VALUES(?,TO_DATE(?, 'DD/MM/YYYY'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DERV_HEDGE_EXPOSURE_DTL_"+start_end_dt+".csv");
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DERV_HEDGE_EXPOSURE_DTL_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	

					String line = br.readLine();
					
					logger.checkpoint(fname, "COMPANY_CD,HEDGE_DT,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						

						for (int i = 0; i < line.split(",").length; i++)
						{
							data = null;
							
								if(i == 1) {
									hedge_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}
								
									data = line.split(",")[i].contains("null") ? null : line.split(",")[i];

						
							
							stmt1.setString(index++, data);
						}
						
						
						queryString = "SELECT COMPANY_CD FROM FMS_DERV_HEDGE_EXPOSURE_DTL WHERE "
								+ "COMPANY_CD = ? AND HEDGE_DT = TO_DATE(?, 'DD/MM/YYYY')";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, hedge_dt);
						rset = stmt.executeQuery();
						
						if (!rset.next()){
							
							logger.data(fname, (company_cd + "," + hedge_dt + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd + "," + hedge_dt + ","), conn, "");
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
	
	public void FMS_DERV_INV_FILE_DTL_UPDATE() throws IOException, SQLException {

		function_nm="FMS_DERV_INV_FILE_DTL_UPDATE()";
		try {
			
			System.out.println("<<START>><<FMS_DERV_INV_FILE_DTL_UPDATE>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DERV_INV_FILE_DTL_UPDATE>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String file_nm = "",file_new="";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,INV_TYPE,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME FROM FMS_DERV_INV_FILE_DTL WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,INV_TYPE,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP", conn);
		
			while (rset.next()) {
				total_count++;
					file_nm = rset.getString(7).split(".pdf")[0];
					if (!rset.getString(7).contains("-O")) {
						file_nm += "-" + rset.getString(6) + ".pdf";
					}
					else {
						file_nm += ".pdf";
					}
					
					if (file_nm!=null) {
						file1 = new File(pdf_path+"derivatives_invoice/" + "DERIVATIVE_" + file_nm); 
						if(file1.exists()) {
							file_new = "DERIVATIVE_"+file_nm;
							
							queryString1 = "UPDATE FMS_DERV_INV_FILE_DTL SET FILE_NAME = ? WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND INV_TYPE = ? "
									+ "AND FINANCIAL_YEAR = ? AND PDF_TYPE = ?";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1,file_new);
							stmt1.setString(2,rset.getString(1));
							stmt1.setString(3,rset.getString(2));
							stmt1.setString(4,rset.getString(3));
							stmt1.setString(5,rset.getString(4));
							stmt1.setString(6,rset.getString(5));
							stmt1.setString(7,rset.getString(6));
							stmt1.executeUpdate();
							stmt1.close();
							
							logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(5) + "," + rset.getString(6)+ "," + file_new + " , " ), conn, "");
							logger_count++;
						}
						else if(!file1.exists()){
							queryString1 = "UPDATE FMS_DERV_INV_FILE_DTL SET FILE_NAME = ? WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND INV_TYPE = ? "
									+ "AND FINANCIAL_YEAR = ? AND PDF_TYPE = ?";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1,file_nm);
							stmt1.setString(2,rset.getString(1));
							stmt1.setString(3,rset.getString(2));
							stmt1.setString(4,rset.getString(3));
							stmt1.setString(5,rset.getString(4));
							stmt1.setString(6,rset.getString(5));
							stmt1.setString(7,rset.getString(6));
							stmt1.executeUpdate();
							stmt1.close();    
						}
						else {
							logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(5) + "," + rset.getString(6)+ "," + file_nm + " , " ), conn, "E");
							skipped_count++; 
						}
					}
				}
			rset.close();
			stmt.close();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_DERV_INV_FILE_DTL_UPDATE>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_INV_FILE_DTL_UPDATE>>,", conn);
			
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
	
	public void setPdf_path(String path) {
		pdf_path = path;
	}
	
}
