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

		

public class Vendor_SEIPL_Data_Reader {

	/*public static void main(String[] args) 
	{
		Vendor_Excel_Reader ex = new Vendor_Excel_Reader();
		ex.init();
	}
	
	}
	
	class Vendor_Excel_Reader {*/

	String db_src_file_name="Vendor_SEIPL_Data_Reader.java";

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
			
			fname = "DataLogs/Reader/Vendor_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/Vendor_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
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
					
					if (checked_values.contains("FMS_VENDOR_MST")) {
						FMS_VENDOR_MST();
					}
					
					if (checked_values.contains("FMS_VENDOR_ADDR_MST")) {
						FMS_VENDOR_ADDR_MST();
					}
					
					if (checked_values.contains("FMS_OTHER_INVOICE_MST")) {
						FMS_OTHER_INVOICE_MST();
					}
					
					if (checked_values.contains("FMS_OTHER_INVOICE_DTL")) {
						FMS_OTHER_INVOICE_DTL();
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
	

	public void FMS_VENDOR_MST() throws IOException, SQLException {

		function_nm="FMS_VENDOR_MST()";
		try {
			System.out.println("<<START>><<FMS_VENDOR_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_VENDOR_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;   
			

			queryString1 = "INSERT INTO FMS_VENDOR_MST(VENDOR_CD,EFF_DT,VENDOR_NM,VENDOR_ABBR,GST_TIN_NO,GST_TIN_DT,CST_TIN_NO,CST_TIN_DT,"
					+ "PAN_NO,PAN_ISSUE_DT,TAN_NO,TAN_ISSUE_DT,GSTIN_NO,GSTIN_DT,WEB_ADDR,NOTES,PAYEE_ACCOUNT_NO,IFSC,PAYEE_NM,"
					+ "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE) "
					+ "VALUES(?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_VENDOR_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_VENDOR_MST_"+start_end_dt+".xlsx"));

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
						{	
							cd = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (cd != null) {
								cd = cd.substring(1, cd.length() - 1);
							}
							data=cd;
				    	}
						else if (cell.getColumnIndex() == 3) 
						{	
							abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (abbr != null) {
								abbr = abbr.substring(1, abbr.length() - 1);
							}
							data=abbr;
				    	}
						else {
							
							if (cell.getColumnIndex() == 1) {
								eff_dt = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
								if (eff_dt != null) {
									eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
								}
							}
							
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
						}
							stmt1.setString(index++, data);
					}

					queryString = "SELECT VENDOR_CD   "
							+ "FROM FMS_VENDOR_MST  "
							+ "WHERE VENDOR_CD = ?  AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') "
							+ "ORDER BY VENDOR_CD ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, cd);
					stmt.setString(2, eff_dt);

					rset = stmt.executeQuery();
					
					if (row.getRowNum() != 0 && !rset.next() && cd != null && !cd.equals("") ) 
					{
						logger.data(fname, (cd +","+abbr+","+eff_dt+","), conn, "");
						stmt1.executeUpdate();
						stmt1.close();							
						logger_count++;
					}
					else {
						stmt1.close();					
						skipped_count++;  
						logger.data(fname, (cd +","+abbr+","+eff_dt+","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_VENDOR_MST>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL :,"+total_count+",,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 		
			logger.checkpoint(fname, "<<END>>,<<FMS_VENDOR_MST>>,,", conn);
			
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
	
	public void FMS_VENDOR_ADDR_MST() throws IOException, SQLException {

		function_nm="FMS_VENDOR_ADDR_MST()";
		try {

			System.out.println("<<START>><<FMS_VENDOR_ADDR_MST>>");
			logger.checkpoint(fname, "\n<<START>>,<<FMS_VENDOR_ADDR_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0; 
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_VENDOR_ADDR_MST(VENDOR_CD,EFF_DT,ADDRESS_TYPE,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
					+ "VALUES(?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_VENDOR_ADDR_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) 
			{
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_VENDOR_ADDR_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();

				String  addr_type = "";
				
				logger.checkpoint(fname, "VENDOR_CD,EFF_DT,ADDRESS_TYPE,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					total_count++;  
					cd="";
					index = 1;
					stmt1=conn.prepareStatement(queryString1);	
						
					row = rowIterator.next();
					cellIterator = row.cellIterator();
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							data = null;
									
							if (cell.getColumnIndex() == 0) 
							{	
								cd = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
								if (cd != null) {
									cd = cd.substring(1, cd.length() - 1);
								}
								data=cd;
					    	}
							
							else {
								
								if (cell.getColumnIndex() == 1) {
									eff_dt = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
									if (eff_dt != null) {
										eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
									}
								}
								if (cell.getColumnIndex() == 2) {
									addr_type = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
									if (addr_type != null) {
										addr_type = addr_type.substring(1, addr_type.length() - 1);
									}
								}
								
								data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
							}
								stmt1.setString(index++, data);
						}
						
						queryString = "SELECT VENDOR_CD FROM FMS_VENDOR_ADDR_MST WHERE VENDOR_CD = ? AND ADDRESS_TYPE = ? AND  EFF_DT = TO_DATE(?, 'DD/MM/YYYY')";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, addr_type);
						stmt.setString(3, eff_dt);
						rset = stmt.executeQuery();
						
						if (row.getRowNum() != 0 && !rset.next() && !cd.equals("") ) {
						
							logger.data(fname, (cd + " , " + eff_dt + " , " + addr_type + ","), conn, "");
							stmt1.executeUpdate();
							stmt1.close();
							conn.commit();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (cd + " , " + eff_dt + " , " + addr_type + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_VENDOR_ADDR_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			logger.checkpoint(fname, "<<END>>,<<FMS_VENDOR_ADDR_MST>>,,,", conn);
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
	
	public void FMS_OTHER_INVOICE_MST() throws IOException, SQLException {

		function_nm="FMS_OTHER_INVOICE_MST()";
		try {
			System.out.println("<<START>><<FMS_OTHER_INVOICE_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_OTHER_INVOICE_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;   
			

			queryString1 = "INSERT INTO FMS_OTHER_INVOICE_MST(COMPANY_CD,VENDOR_CD,CONTRACT_TYPE,PERIOD_START_DT,PERIOD_END_DT,INV_SEQ_NO,FINANCIAL_YEAR,INVOICE_DT,EXCHG_RATE_DT,EXCHG_RATE_VALUE,GROSS_AMT_INR,"
					+ "GROSS_AMT_USD,NET_AMT_INR,TAX_AMT_INR,FLAG,INV_CUR_FLAG,SALE_PRICE_FLAG,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,PDF_INV_DTL,"
					+ "NEW_INV_SEQ_NO,SUPPLIER_CD,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
					+ "VALUES(?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY'),?,?,"
					+ "?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_OTHER_INVOICE_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_OTHER_INVOICE_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
			
				//String up_cg="",up_wb="";
				logger.checkpoint(fname, "COMPANY_CD,VENDOR_CD,CONTRACT_TYPE,PERIOD_START_DT,PERIOD_END_DT,INV_SEQ_NO,FINANCIAL_YEAR,SUPPLIER_CD,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					total_count++;  
					cd="";
					index = 1;
					String cont_type="", start_dt="", end_dt="", inv_seq_no="", fin_yr="", supp_cd="";
					stmt1=conn.prepareStatement(queryString1);	
						
					row = rowIterator.next();
					cellIterator = row.cellIterator();
						
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = null;
								
						if (cell.getColumnIndex() == 1) 
						{	
							cd = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (cd != null) {
								cd = cd.substring(1, cd.length() - 1);
							}
							data=cd;
				    	}
						else if (cell.getColumnIndex() == 2) 
						{	
							cont_type = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (cont_type != null) {
								cont_type = cont_type.substring(1, cont_type.length() - 1);
							}
							data=cont_type;
				    	}
						else if (cell.getColumnIndex() == 3) 
						{	
							start_dt = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (start_dt != null) {
								start_dt = start_dt.substring(1, start_dt.length() - 1);
							}
							data=start_dt;
				    	}
						else if (cell.getColumnIndex() == 4) 
						{	
							end_dt = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (end_dt != null) {
								end_dt = end_dt.substring(1, end_dt.length() - 1);
							}
							data=end_dt;
				    	}
						else if (cell.getColumnIndex() == 5) 
						{	
							inv_seq_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (inv_seq_no != null) {
								inv_seq_no = inv_seq_no.substring(1, inv_seq_no.length() - 1);
							}
							data=inv_seq_no;
				    	}
						else if (cell.getColumnIndex() == 6) 
						{	
							fin_yr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (fin_yr != null) {
								fin_yr = fin_yr.substring(1, fin_yr.length() - 1);
							}
							data=fin_yr;
				    	}
						else if (cell.getColumnIndex() == 25) 
						{	
							supp_cd = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (supp_cd != null) {
								supp_cd = supp_cd.substring(1, supp_cd.length() - 1);
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, supp_cd);
								rset = stmt.executeQuery();
								if (rset.next()) {
									supp_cd = rset.getString(1);
								}
								else {
									supp_cd = "0";
								}
								rset.close();
								stmt.close();
							}
							
							data=supp_cd;
				    	}
						else {
							
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
						}
//						System.out.println(index+"=="+data);
							stmt1.setString(index++, data);
					}

					queryString = "SELECT VENDOR_CD   "
							+ "FROM FMS_OTHER_INVOICE_MST  "
							+ "WHERE COMPANY_CD = ? AND VENDOR_CD = ? AND CONTRACT_TYPE = ? AND PERIOD_START_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND PERIOD_END_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
							+ "ORDER BY VENDOR_CD ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, cont_type);
					stmt.setString(4, start_dt);
					stmt.setString(5, end_dt);
					stmt.setString(6, inv_seq_no);
					stmt.setString(7, fin_yr);
					stmt.setString(8, supp_cd);

					rset = stmt.executeQuery();
					
					if (row.getRowNum() != 0 && !rset.next() && cd != null && !cd.equals("") ) 
					{
						logger.data(fname, (company_cd+","+cd +","+cont_type+","+start_dt+","+end_dt+","+inv_seq_no+","+fin_yr+","+supp_cd+","), conn, "");
						stmt1.executeUpdate();
						stmt1.close();							
						logger_count++;
					}
					else {
						stmt1.close();					
						skipped_count++;  
						logger.data(fname, (company_cd+","+cd +","+cont_type+","+start_dt+","+end_dt+","+inv_seq_no+","+fin_yr+","+supp_cd+","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_OTHER_INVOICE_MST>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL :,"+total_count+",,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 		
			logger.checkpoint(fname, "<<END>>,<<FMS_OTHER_INVOICE_MST>>,,", conn);
			
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

	
	public void FMS_OTHER_INVOICE_DTL() throws IOException, SQLException {

		function_nm="FMS_OTHER_INVOICE_DTL()";
		try {
			System.out.println("<<START>><<FMS_OTHER_INVOICE_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_OTHER_INVOICE_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_OTHER_INVOICE_DTL(COMPANY_CD,INV_SEQ_NO,SEQ_NO,ITEM_DESCRIPTION,RATE,FLAG,TAX_DESCR,FINANCIAL_YEAR,CONTRACT_TYPE,EFF_DT,HSN_CODE,"
					+ "SAC_CODE,REMARK,REMARK1,REMARK2,REMARK3,PURCHASE_NO,REFERENCE_NO,VESSEL_CD,GRT,VENDOR_CD,VESSEL_AGENT,"
					+ "IMPORTER,QUANTITY,CARGO_DT,CARGO_AMOUNT,RATE_CGST,RATE_SGST,RATE_IGST,TAX_CD,GATE_PASS_NO,HRS_BERTHING,"
					+ "TIME_SLOTS_BERTHING,FLAG_SAC,SALE_NO,PACER_NO,VENDOR_SUPP_INV_REF,UAM_NO,CARGO_TYPE,SUPPLIER_CD,CESS_RATE,"
					+ "CESS_AMOUNT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,"
					+ "?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,"
					+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_OTHER_INVOICE_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_OTHER_INVOICE_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
			
				//String up_cg="",up_wb="";
				logger.checkpoint(fname, "COMPANY_CD,INV_SEQ_NO,FINANCIAL_YEAR,CONTRACT_TYPE,EFF_DT,SUPPLIER_CD,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					total_count++;  
					cd="";
					index = 1;
					String cont_type="", eff_dt="", inv_seq_no="", fin_yr="", supp_cd="", seq_no="";
					stmt1=conn.prepareStatement(queryString1);	
						
					row = rowIterator.next();
					cellIterator = row.cellIterator();
						
					while (cellIterator.hasNext()) 
					{
						cell = cellIterator.next();
						data = null;
								
						if (cell.getColumnIndex() == 1) 
						{	
							inv_seq_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (inv_seq_no != null) {
								inv_seq_no = inv_seq_no.substring(1, inv_seq_no.length() - 1);
							}
							data=inv_seq_no;
				    	}
						else if (cell.getColumnIndex() == 2) 
						{	
							seq_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (seq_no != null) {
								seq_no = seq_no.substring(1, seq_no.length() - 1);
							}
							data=seq_no;
				    	}
						else if (cell.getColumnIndex() == 6) 
						{	
							fin_yr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (fin_yr != null) {
								fin_yr = fin_yr.substring(1, fin_yr.length() - 1);
							}
							data=fin_yr;
				    	}
						else if (cell.getColumnIndex() == 7) 
						{	
							cont_type = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (cont_type != null) {
								cont_type = cont_type.substring(1, cont_type.length() - 1);
							}
							data=cont_type;
				    	}
						else if (cell.getColumnIndex() == 8) 
						{	
							eff_dt = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (eff_dt != null) {
								eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
							}
							data=eff_dt;
				    	}
						else if (cell.getColumnIndex() == 39) 
						{	
							supp_cd = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							
							if (supp_cd != null) {
								supp_cd = supp_cd.substring(1, supp_cd.length() - 1);
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, supp_cd);
								rset = stmt.executeQuery();
								if (rset.next()) {
									supp_cd = rset.getString(1);
								}
								else {
									supp_cd = "0";
								}
								rset.close();
								stmt.close();
							}
							data=supp_cd;
				    	}
						else {
							
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
						}
//						System.out.println(index+"=="+data);
							stmt1.setString(index++, data);
					}

					queryString = "SELECT VENDOR_CD   "
							+ "FROM FMS_OTHER_INVOICE_DTL  "
							+ "WHERE COMPANY_CD = ? AND CONTRACT_TYPE = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND INV_SEQ_NO = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? AND SEQ_NO = ? "
							+ "ORDER BY VENDOR_CD ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cont_type);
					stmt.setString(3, eff_dt);
					stmt.setString(4, inv_seq_no);
					stmt.setString(5, fin_yr);
					stmt.setString(6, supp_cd);
					stmt.setString(7, seq_no);

					rset = stmt.executeQuery();
					
					if (row.getRowNum() != 0 && !rset.next()) 
					{
						logger.data(fname, (company_cd+","+inv_seq_no +","+fin_yr+","+cont_type+","+eff_dt+","+supp_cd+","+seq_no +","), conn, "");
						stmt1.executeUpdate();
						stmt1.close();							
						logger_count++;
					}
					else {
						stmt1.close();					
						skipped_count++;  
						logger.data(fname, (company_cd+","+inv_seq_no +","+fin_yr+","+cont_type+","+eff_dt+","+supp_cd+","+seq_no +","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_OTHER_INVOICE_DTL>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL :,"+total_count+",,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 		
			logger.checkpoint(fname, "<<END>>,<<FMS_OTHER_INVOICE_DTL>>,,", conn);
			
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

