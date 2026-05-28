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

//import automation.Auto_DB_Connection;

		

public class OthInv_SEIPL_Data_Reader {

	/*public static void main(String[] args) 
	{
		OthInv_Excel_Reader ex = new OthInv_Excel_Reader();
		ex.init();
	}
	
	}
	
	class Vendor_Excel_Reader {*/

	String db_src_file_name="OthInv_SEIPL_Data_Reader.java";

	String migration_setup_dir = "";
	
	String queryString="", queryString1="", queryString2="";
	Connection conn;
	ResultSet rset,rset1,rset2;
	PreparedStatement stmt,stmt1,stmt2;
	
	String function_nm = "", columns = "", data = "";
	String table_name = "";
	
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
	
	String  abbr = "", cd = "", eff_dt = "",seq = "",name="",entity = "", addr_type = "",exchg_cd="";
	final String company_cd = "2";

	String checked_values = "", msg = "", msg_type = "";

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
			
			fname = "DataLogs/Reader/OthInv_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/OthInv_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
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
					
					if (checked_values.contains("FMS_OTH_ENTITY_MST")) 
					{
						FMS_OTH_ENTITY_MST();
					}
					if (checked_values.contains("FMS_OTH_ENTITY_ADDR_MST")) 
					{
						FMS_OTH_ENTITY_ADDR_MST();
					}
					if(checked_values.contains("FMS_OTH_INVOICE_MST")) 
					{
						FMS_OTH_INVOICE_MST();
					}
					if(checked_values.contains("FMS_OTH_INVOICE_DTL")) 
					{
						FMS_OTH_INVOICE_DTL();
					}
					if(checked_values.contains("FMS_OTH_INV_CRDR_REF")) 
					{
						FMS_OTH_INV_CRDR_REF();
					}
					if(checked_values.contains("FMS_OTH_INV_FILE_DTL"))
					{
						FMS_OTH_INV_FILE_DTL();
					}
					if(checked_values.contains("FMS_OTH_INV_IRN_DTL"))
					{
						FMS_OTH_INV_IRN_DTL();
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
	
	public void FMS_OTH_ENTITY_MST() throws IOException, SQLException {

		function_nm="FMS_OTH_ENTITY_MST()";
		try {
			System.out.println("<<START>><<FMS_OTH_ENTITY_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_OTH_ENTITY_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			String flag = "";

			
			queryString1 = "INSERT INTO FMS_OTH_ENTITY_MST (ENTITY_CD,ENTITY_TYPE,EFF_DT,ENTITY_NAME,ENTITY_ABBR,GST_TIN_NO,GST_TIN_DT,CST_TIN_NO,CST_TIN_DT,"
					+ "PAN_NO,PAN_ISSUE_DT,TAN_NO,TAN_ISSUE_DT,ADDL_NO,ADDL_ISSUE_DT,GSTIN_NO,GSTIN_DT,WEB_ADDR,NOTES,ACTIVE_FLAG,BUSINESS_FLAG,PAYEE_ACCOUNT_NO,"
					+ "IFSC,PAYEE_NM,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE) VALUES('3','S',TO_DATE('01/01/2003', 'DD/MM/YYYY'),"
					+ "'Hazira LNG Private Limited','HLPL','','','','','AAACH9143C','','','','','','24AAACH9143C1ZZ','','','','N','','',"
					+ "'','','6',TO_DATE('15/07/2021', 'DD/MM/YYYY'),'','',?,'')";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
				
			//for data already exists..
			queryString = "SELECT ENTITY_CD "
					+ "FROM FMS_OTH_ENTITY_MST "
					+ "WHERE ENTITY_CD = '3' AND ENTITY_TYPE = 'S' AND EFF_DT = TO_DATE('01/01/2003', 'DD/MM/YYYY') AND ENT_PROFILE = ?";
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
			
			
			queryString1 = "INSERT INTO FMS_OTH_ENTITY_MST(ENTITY_CD,ENTITY_TYPE,EFF_DT,ENTITY_NAME,ENTITY_ABBR,GST_TIN_NO,GST_TIN_DT,CST_TIN_NO,CST_TIN_DT,"
					+ "PAN_NO,PAN_ISSUE_DT,TAN_NO,TAN_ISSUE_DT,ADDL_NO,ADDL_ISSUE_DT,GSTIN_NO,GSTIN_DT,WEB_ADDR,NOTES,ACTIVE_FLAG,BUSINESS_FLAG,PAYEE_ACCOUNT_NO,"
					+ "IFSC,PAYEE_NM,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE) "
					+ "VALUES(?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_OTH_ENTITY_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_OTH_ENTITY_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
			
				//String up_cg="",up_wb="";
				logger.checkpoint(fname, "ENTITY_CD,ENTITY_TYPE,ENTITY_ABBR,EFF_DT,TIMESTAMP", conn);
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
							if (cd != null) 
							{
								cd = cd.substring(1, cd.length() - 1);
							}
							data=cd;
				    	}
						if (cell.getColumnIndex() == 1) 
						{	
							flag = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (flag != null) 
							{
								flag = flag.substring(1, flag.length() - 1);
							}
							data=flag;
				    	}
						else if (cell.getColumnIndex() == 4) 
						{	
							abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if (abbr != null) {
								abbr = abbr.substring(1, abbr.length() - 1);
							}
							data=abbr;
				    	}
						else {
							
							if (cell.getColumnIndex() == 2) 
							{
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

					queryString = "SELECT ENTITY_CD "
							+ "FROM FMS_OTH_ENTITY_MST "
							+ "WHERE ENTITY_CD = ? AND ENTITY_TYPE = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND ENT_PROFILE = ?";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, cd);
					stmt.setString(2, flag);
					stmt.setString(3, eff_dt);
					stmt.setString(4, company_cd);

					rset = stmt.executeQuery();
					
					if (row.getRowNum() != 0 && !rset.next() && cd != null && !cd.equals("")) 
					{
						logger.data(fname, (cd + ","+ flag + "," + abbr + "," + eff_dt +","), conn, "");
						stmt1.executeUpdate();
						stmt1.close();							
						logger_count++;
					}
					else {
						stmt1.close();					
						skipped_count++;  
						logger.data(fname, (cd + ","+ flag + "," + abbr + "," + eff_dt +","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_OTH_ENTITY_MST>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL :,"+total_count+",,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 		
			logger.checkpoint(fname, "<<END>>,<<FMS_OTH_ENTITY_MST>>,,", conn);
			
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
	
	public void FMS_OTH_ENTITY_ADDR_MST() throws IOException, SQLException 
	{

		function_nm="FMS_OTH_ENTITY_ADDR_MST()";
		try 
		{

			System.out.println("<<START>><<FMS_OTH_ENTITY_ADDR_MST>>");
			logger.checkpoint(fname, "\n<<START>>,<<FMS_OTH_ENTITY_ADDR_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0; 
			skipped_count = 0;   
			total_count = 0;   
			String flag = "";
			
			queryString1 = "INSERT INTO FMS_OTH_ENTITY_ADDR_MST (ENTITY_CD,ENTITY_TYPE,EFF_DT,ADDRESS_TYPE,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,"
					+ "ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE) VALUES ('3','S',TO_DATE('01-07-17', 'DD/MM/YYYY'),"
					+ "'R','101-103 Abhijit-II, Mithakali Circle','Ahmedabad','380006','Gujarat','W','India','07930011100',NULL,NULL,'07930011101',"
					+ "'07930011200',NULL,'4',TO_DATE('31-07-17', 'DD/MM/YYYY'),NULL,NULL,?,NULL)";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
				
			//for data already exists..
			queryString = "SELECT ENTITY_CD FROM FMS_OTH_ENTITY_ADDR_MST WHERE ENTITY_CD = '3' AND ENTITY_TYPE = 'S' AND ADDRESS_TYPE = 'R' "
					+ "AND EFF_DT = TO_DATE('01-07-17', 'DD/MM/YYYY') AND ENT_PROFILE = ?";
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

			queryString1 = "INSERT INTO FMS_OTH_ENTITY_ADDR_MST(ENTITY_CD,ENTITY_TYPE,EFF_DT,ADDRESS_TYPE,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,"
					+ "ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE) "
					+ "VALUES(?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_OTH_ENTITY_ADDR_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) 
			{
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_OTH_ENTITY_ADDR_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();

				String  addr_type = "";
				
				logger.checkpoint(fname, "ENTITY_CD,ENTITY_TYPE,EFF_DT,ADDRESS_TYPE,TIMESTAMP", conn);
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
								if (cd != null) 
								{
									cd = cd.substring(1, cd.length() - 1);
								}
								if(cd.equals("HPPL") || cd.equals("SEIPL")) 
								{
									queryString2 = "SELECT ENTITY_CD FROM FMS_OTH_ENTITY_MST WHERE ENTITY_ABBR = ? AND ENTITY_TYPE = 'S' AND ENT_PROFILE = ?";
									stmt2 = conn.prepareStatement(queryString2);
									stmt2.setString(1, cd);
									stmt2.setString(2, company_cd);
									rset2 = stmt2.executeQuery();
									if (rset2.next()) 
									{
										cd = rset2.getString(1) == null ? "null" : rset2.getString(1);
									}
									rset2.close();
									stmt2.close();
								}
								
								data=cd;
					    	}
							else if(cell.getColumnIndex() == 7 && cell.getStringCellValue().length() > 2 && !cell.getStringCellValue().equals("'null'"))
							{
								queryString2 = "SELECT STATE_NM FROM FMS_STATE_MST WHERE UPPER(STATE_NM) LIKE ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, cell.getStringCellValue().toUpperCase());
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									data = rset2.getString(1) == null ? "null" : rset2.getString(1);
									rset2.close();
									stmt2.close();
								}
								else {
									data = Camel_Case_Converter(cell.getStringCellValue());
								}
								rset2.close();
								stmt2.close();
							}
							else if(cell.getColumnIndex() == 9 && cell.getStringCellValue().length() > 2 && !cell.getStringCellValue().equals("'null'")) 
							{
								queryString2 = "SELECT COUNTRY_NM FROM FMS_COUNTRY_MST WHERE UPPER(COUNTRY_NM) LIKE ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, cell.getStringCellValue().toUpperCase());
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									data = rset2.getString(1) == null ? "null" : rset2.getString(1);
									rset2.close();
									stmt2.close();
								}
								else {
									data = Camel_Case_Converter(cell.getStringCellValue());
								}
								rset2.close();
								stmt2.close();
							}
							else 
							{
								if (cell.getColumnIndex() == 1) 
								{	
									flag = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
									if (flag != null) 
									{
										flag = flag.substring(1, flag.length() - 1);
									}
						    	}
								if (cell.getColumnIndex() == 2) 
								{
									eff_dt = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
									if (eff_dt != null) {
										eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
									}
								}
								if (cell.getColumnIndex() == 3) 
								{
									addr_type = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
									if (addr_type != null) 
									{
										addr_type = addr_type.substring(1, addr_type.length() - 1);
									}
								}
								
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						    	if(data != null) 
						    	{
						    		data = data.substring(1, data.length()-1);
						    	}
							}
								stmt1.setString(index++, data);
						}
						
						queryString = "SELECT ENTITY_CD FROM FMS_OTH_ENTITY_ADDR_MST WHERE ENTITY_CD = ? AND ENTITY_TYPE = ? AND ADDRESS_TYPE = ? "
								+ "AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND ENT_PROFILE = ?";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cd);
						stmt.setString(2, flag);
						stmt.setString(3, addr_type);
						stmt.setString(4, eff_dt);
						stmt.setString(5, company_cd);
						rset = stmt.executeQuery();
						
						if (row.getRowNum() != 0 && !rset.next() && !cd.equals("") ) 
						{
						
							logger.data(fname, (cd + ","+ flag + "," + eff_dt + "," + addr_type + ","), conn, "");
							stmt1.executeUpdate();
							stmt1.close();
							conn.commit();
							
							logger_count++;
						}
						else 
						{
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (cd + ","+ flag + "," + eff_dt + "," + addr_type + ","), conn, "E");
						}
						rset.close();
						stmt.close();
				}


				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else 
			{
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			
			System.out.println("<<END>><<FMS_OTH_ENTITY_ADDR_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,"), conn); 
			logger.checkpoint(fname, "<<END>>,<<FMS_OTH_ENTITY_ADDR_MST>>,,,", conn);
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
		finally 
		{
			conn.commit();

			if (file != null) 
			{
				file.close();
			}
		}
		
	}
	

	public void FMS_OTH_INVOICE_MST() throws IOException, SQLException 
	{
		
		function_nm="FMS_OTH_INVOICE_MST()";
		try 
		{
			table_name = "FMS_OTH_INVOICE_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String fin_yr = "",vend_cd="",inv_type="",inv_seq="",inv_no="",p_end="",p_st="";
			
			queryString1 = "INSERT INTO FMS_OTH_INVOICE_MST (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,DUE_DT,"
					+ "PERIOD_START_DT,PERIOD_END_DT,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT_INR,"
					+ "INVOICE_RAISED_IN,REMARK,REMARK1,REMARK2,REMARK3,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,"
					+ "APPROVED_FLAG,APPROVED_BY,APPROVED_DT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,FIN_SYS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,NET_PAYABLE,INVOICE_ID_SEQ,INVOICE_CATEGORY,"
					+ "VENDOR_TYPE,INV_FLAG,REF_NO,CRITERIA) "
					+ "VALUES (?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,"
					+ "?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,"
					+ "?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			Map<String, Integer> invseq = new HashMap<String, Integer>();
			file1 = new File(migration_setup_dir + "EXPORT/FMS_OTH_INVOICE_MST_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_OTH_INVOICE_MST_"+start_end_dt+".csv";
				
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
				{
					String line = br.readLine();
					
					logger.checkpoint(fname, "COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,INVOICE_SEQ,INVOICE_NO,TIMESTAMP,", conn);
					int inv_seq_no=1;
					while ((line = br.readLine()) != null) 
					{
						total_count++; 
						abbr = "";
						cd = "0";
						data = null;
						fin_yr="";vend_cd="";inv_type="";inv_seq="";inv_no="";p_end="";p_st="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						for (int i = 0; i < line.split(",").length; i++)
					    {	
							data = null;
							
					    	if (i == 1) // Supplier_cd
					    	{	
					    		abbr = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
					    		
					    		queryString = "SELECT ENTITY_CD FROM FMS_OTH_ENTITY_MST WHERE ENTITY_ABBR = ? AND ENTITY_TYPE = 'S'";
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
					    	else if(i == 3)
				    		{
				    			inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			data = inv_type;
				    		}
					    	else if (i == 4) 
				    		{	
				    			fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			data = fin_yr;
			    			}
					    	else if (i == 5) 
				    		{	
					    		if (invseq.containsKey(fin_yr+inv_type+cd)) 
					    		{
					    			
					    			inv_seq_no=invseq.get(fin_yr+inv_type+cd);
					    			inv_seq_no=inv_seq_no+1;
									invseq.put(fin_yr+inv_type+cd,inv_seq_no);
									
								} 
					    		else 
					    		{
									inv_seq_no=1;
									invseq.put(fin_yr+inv_type+cd,inv_seq_no);
									
								}
					    		data=inv_seq_no+"";
			    			}
					    	else if(i == 14 && inv_type.equals("HS"))
					    	{
					    		name = line.split(",")[i].contains("null") ? null : line.split(",")[i];

					    		queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
						    	stmt = conn.prepareStatement(queryString);
						    	stmt.setString(1, name);
						    	rset = stmt.executeQuery();
						    	if (rset.next()) 
						    	{
						    		exchg_cd = rset.getString(1);
						    	}
						    	rset.close();
						    	stmt.close();
						    	
						    	data = exchg_cd;
					    	}
					    	else 
					    	{		
					    		if(i == 2)
					    		{
					    			vend_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					    		}
					    		if (i == 4) 
					    		{	
					    			fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		if (i == 6) 
					    		{	
					    			inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		if (i == 9) 
					    		{	
					    			p_st = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		if (i == 10) 
					    		{	
					    			p_end = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		
						    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@", "/");
					    	}
					    	
					    	stmt1.setString(index++, data);		    	
					    }
						
					    queryString = "SELECT SUPPLIER_CD "
					    		+ "FROM FMS_OTH_INVOICE_MST "
					    		+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? "
					    		+ "AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND INVOICE_SEQ = ? AND PERIOD_START_DT = TO_DATE(?, 'DD/MM/YYYY') "
					    		+ "AND PERIOD_END_DT = TO_DATE(?, 'DD/MM/YYYY')";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, company_cd);
				    	stmt.setString(2, cd);
				    	stmt.setString(3, vend_cd);
				    	stmt.setString(4, inv_type);
				    	stmt.setString(5, fin_yr);
				    	stmt.setString(6, inv_seq_no+"");
				    	stmt.setString(7, p_st);
				    	stmt.setString(8, p_end);
				    	
				    	rset = stmt.executeQuery();
				    	
				    	 if (!rset.next() && !cd.equals("")) 
				    	 {
								stmt1.executeUpdate();
								stmt1.close();
								logger_count++;
								logger.data(fname, (company_cd+"," + cd + " , " + vend_cd + "," + inv_type + "," + fin_yr + "," + inv_seq + "," + inv_no + ","), conn, "");
				    	 }
				    	 else 
				    	 {
								stmt1.close();
								skipped_count++; 
								logger.data(fname, (company_cd+"," + cd + " , " + vend_cd + "," + inv_type + "," + fin_yr + "," + inv_seq + "," + inv_no + ","), conn, "E");
				    	
								if (invseq.containsKey(fin_yr+inv_type+cd)) {
									inv_seq_no = invseq.get(fin_yr+inv_type+cd);
									inv_seq_no=inv_seq_no-1;
									invseq.put(fin_yr+inv_type+cd, inv_seq_no);
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
	
	public void FMS_OTH_INVOICE_DTL() throws IOException, SQLException 
	{
		
		function_nm="FMS_OTH_INVOICE_DTL()";
		try 
		{
			table_name = "FMS_OTH_INVOICE_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String fin_yr = "",vend_cd="",inv_type="",inv_seq="",tax_desc="",sac_cd="",tax_dt="",tax="",ship_nm="",ship_cd="",seq_no="",inv_no="",tax_cat="",sign="";
			
			queryString1 = "INSERT INTO FMS_OTH_INVOICE_DTL (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,EFF_DT,SEQ_NO,INVOICE_NO,INVOICE_SEQ,ITEM_DESCRIPTION,SALE_PRICE,"
					+ "SALE_PRICE_UNIT,TAX_STRUCT_CD,FINANCIAL_YEAR,HSN_CODE,SAC_CODE,PURCHASE_NO,REFERENCE_NO,VESSEL_CD,VESSEL_AGENT,VESSEL_FLAG,GRT,IMPORTER,QUANTITY,"
					+ "CARGO_DT,CARGO_AMOUNT,TAX_CD,GATE_PASS_NO,HRS_BERTHING,TIME_SLOTS_BERTHING,FLAG_SAC,SALE_NO,PACER_NO,VENDOR_SUPP_INV_REF,UAM_NO,"
					+ "CARGO_TYPE,CESS_RATE,CESS_AMOUNT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,TAX_EFF_DT,DISCHARGE_PORT,SIGN,AMT_DESCRIPTION,ITEM_AMT,ITEM_FLAG,INV_FLAG,"
					+ "REF_NO,CRITERIA,ITEM_TAX_AMT,TOTAL_CESS_AMOUNT) "
					+ "VALUES (?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,"
					+ "?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			file1 = new File(migration_setup_dir + "EXPORT/FMS_OTH_INVOICE_DTL_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_OTH_INVOICE_DTL_"+start_end_dt+".csv";
				
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
				{
					String line = br.readLine();
					
					//logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ_NO,CONTACT_PERSON_CD,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) 
					{
						total_count++; 
						abbr = "";
						cd = "0";
						data = null;
						fin_yr="";vend_cd="";inv_type="";inv_seq="";tax_desc="";sac_cd="";tax_dt="";tax="";seq_no="";inv_no="";tax_cat="";sign="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						for (int i = 0; i < line.split(",").length; i++)
					    {	
							data = null;
							
					    	if (i == 1) // Supplier_cd
					    	{	
					    		abbr = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
					    		
					    		queryString = "SELECT ENTITY_CD FROM FMS_OTH_ENTITY_MST WHERE ENTITY_ABBR = ? AND ENTITY_TYPE = 'S'";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) 
					    		{
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
					    	else if(i == 3)
				    		{
				    			inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			data = inv_type;
				    		}
					    	else if(i == 6)
					    	{
					    		inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					    		data = inv_no;
					    	}
					    	else if(i == 7)
					    	{
					    		queryString = "SELECT INVOICE_SEQ FROM FMS_OTH_INVOICE_MST WHERE COMPANY_CD = ? AND INVOICE_NO = ? AND INVOICE_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, company_cd);
					    		stmt.setString(2, inv_no);
					    		stmt.setString(3, inv_type);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) 
					    		{
					    			inv_seq = rset.getString(1);
					    		} 
					    		rset.close();
					    		stmt.close();
					    		
					    		data = inv_seq;
					    	}
					    	else if(i == 11)
					    	{
					    		queryString = "SELECT INVOICE_CATEGORY FROM FMS_OTH_INVOICE_MST WHERE COMPANY_CD = ? AND INVOICE_NO = ? AND INVOICE_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, company_cd);
					    		stmt.setString(2, inv_no);
					    		stmt.setString(3, inv_type);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) 
					    		{
					    			tax_cat = rset.getString(1);
					    		} 
					    		rset.close();
					    		stmt.close();
					    		
					    		
					    		tax = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
					    		
					    		if(tax!=null)
					    		{
					    			tax = tax.replaceAll("@", ", ");
					    		}
					    		queryString = "SELECT A.TAX_STR_CD,TO_CHAR(A.APP_DATE,'DD/MM/YYYY') "
					    				+ "FROM FMS_TAX_STRUCTURE A "
					    				+ "WHERE A.TAX_CATEGORY = ? AND A.DESCR = ? AND A.PAY_RECV= ? "
					    				+ "AND A.APP_DATE=(SELECT MAX(B.APP_DATE) FROM FMS_TAX_STRUCTURE B "
					    				+ "WHERE A.TAX_STR_CD=B.TAX_STR_CD AND B.APP_DATE<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, tax_cat);
					    		stmt.setString(2, tax);
					    		stmt.setString(3, "R");
					    		rset = stmt.executeQuery();
					    		if (rset.next()) 
					    		{
					    			tax_desc = rset.getString(1) == null ? "" : rset.getString(1);
					    			tax_dt = rset.getString(2) == null ? "" : rset.getString(2);
					    		} 
					    		else 
					    		{
					    			tax_desc = "";
					    			tax_dt = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = tax_desc;
					    	}
					    	else if(i == 14)
					    	{
					    		sac_cd = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);

					    		queryString = "SELECT SAC_CD "
					    				+ "FROM FMS_SAC_MST "
					    				+ "WHERE SAC_CODE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1 ,sac_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) 
					    		{
					    			sac_cd = rset.getString(1) == null ? "" : rset.getString(1);
					    		} 
					    		else 
					    		{
					    			sac_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = sac_cd;
					    	}
					    	else if(i == 17 && (inv_type.equals("HSA") || inv_type.equals("HS")))
					    	{
					    		ship_nm = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					    		
					    		queryString = "SELECT SHIP_CD "
					    				+ "FROM FMS_SHIP_MST "
					    				+ "WHERE SHIP_NAME = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1 ,ship_nm);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) 
					    		{
					    			ship_cd = rset.getString(1);
					    		} 
					    		else 
					    		{
					    			ship_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = ship_cd;
					    	}
					    	else if(i == 41)
					    	{
					    		data = tax_dt;
					    	}
					    	else 
					    	{		
					    		if(i == 2)
					    		{
					    			vend_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					    		}
					    		if (i == 4) 
					    		{	
					    			eff_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		if (i == 5) 
					    		{	
					    			seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		if (i == 12) 
					    		{	
					    			fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		if (i == 43) 
					    		{	
					    			sign = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		
						    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@", "/");
					    	}
					    	
					    	stmt1.setString(index++, data);		    	
					    }
						
						int cnt = 0;
					    queryString = "SELECT SUPPLIER_CD "
					    		+ "FROM FMS_OTH_INVOICE_DTL "
					    		+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD = ? AND VENDOR_CD = ? "
					    		+ "AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND INVOICE_SEQ = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND SEQ_NO = ? ";
					    if(inv_type.equals("AHPL"))
					    {
					    	queryString+= "AND SIGN = ?";
					    }
					    
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(++cnt, company_cd);
				    	stmt.setString(++cnt, cd);
				    	stmt.setString(++cnt, vend_cd);
				    	stmt.setString(++cnt, inv_type);
				    	stmt.setString(++cnt, fin_yr);
				    	stmt.setString(++cnt, inv_seq);
				    	stmt.setString(++cnt, eff_dt);
				    	stmt.setString(++cnt, seq_no);
				    	
				    	if(inv_type.equals("AHPL"))
					    {
				    		stmt.setString(++cnt, sign);
					    }
				    	
				    	rset = stmt.executeQuery();
				    	
				    	 if (!rset.next() && !cd.equals("")) 
				    	 {
								
								//logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," ), conn, "");
								
								stmt1.executeUpdate();
								stmt1.close();
								
								logger_count++;
				    	 }
				    	 else 
				    	 {
								stmt1.close();
								skipped_count++;     
								//logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," ), conn, "E");
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
	
	
	public void FMS_OTH_INV_CRDR_REF() throws IOException, SQLException 
	{
		
		function_nm="FMS_OTH_INV_CRDR_REF()";
		try 
		{
			table_name = "FMS_OTH_INV_CRDR_REF";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String fin_yr = "",supp_cd="",inv_type="",inv_seq="",vend_cd="",inv_no="";
			
			queryString1 = "INSERT INTO FMS_OTH_INV_CRDR_REF (COMPANY_CD,SUPPLIER_CD,VENDOR_CD,INVOICE_TYPE,FINANCIAL_YEAR,SEQ_NO,INVOICE_SEQ,ALLOC_QTY,SALE_PRICE,"
					+ "SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,"
					+ "GRT,QUANTITY,CARGO_DT,CARGO_AMOUNT,HRS_BERTHING,TIME_SLOTS_BERTHING,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,"
					+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			file1 = new File(migration_setup_dir + "EXPORT/FMS_OTH_INV_CRDR_REF_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_OTH_INV_CRDR_REF_"+start_end_dt+".csv";
				
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
				{
					String line = br.readLine();
					
					logger.checkpoint(fname, "COMPANY_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,SUPPLIER_CD,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) 
					{
						total_count++; 
						abbr = "";
						cd = "0";
						data = null;
						fin_yr="";supp_cd="";inv_type="";inv_seq="";inv_no="";
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						for (int i = 0; i < line.split(",").length; i++)
					    {	
							data = null;
							
					    		if(i == 1)
					    		{
					    			abbr = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
						    		
						    		queryString = "SELECT ENTITY_CD FROM FMS_OTH_ENTITY_MST WHERE ENTITY_ABBR = ? AND ENTITY_TYPE = 'S'";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, abbr);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) 
						    		{
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
					    		else if(i == 3)
					    		{
						    		inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    		data = 	inv_type;
					    		}
					    		else if(i == 6)
					    		{
					    			inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					    			
					    			if (inv_no!=null) 
					    			{
										queryString = "SELECT INVOICE_SEQ FROM FMS_OTH_INVOICE_MST WHERE COMPANY_CD = ? AND INVOICE_NO = ? AND INVOICE_TYPE = ? ";
										stmt = conn.prepareStatement(queryString);
										stmt.setString(1, company_cd);
										stmt.setString(2, inv_no);
										stmt.setString(3, inv_type);
										rset = stmt.executeQuery();
										if (rset.next()) 
										{
											inv_seq = rset.getString(1);
										}
										rset.close();
										stmt.close();
									}
					    			
									data = inv_seq;
					    		}
					    		else 
					    		{
					    			if(i == 2)
						    		{
						    			vend_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    		}
						    		if (i == 3) 
						    		{	
						    			inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					    			}
						    		if (i == 4) 
						    		{	
						    			fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					    			}
						    		
							    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@", "/");
					    		}
						    	
					    	stmt1.setString(index++, data);		    	
					    }
						
						int cnt = 0;
					    queryString = "SELECT SUPPLIER_CD "
					    		+ "FROM FMS_OTH_INV_CRDR_REF "
					    		+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND INVOICE_SEQ = ? AND VENDOR_CD = ?";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(++cnt, company_cd);
				    	stmt.setString(++cnt, cd);
				    	stmt.setString(++cnt, inv_type);
				    	stmt.setString(++cnt, fin_yr);
				    	stmt.setString(++cnt, inv_seq);
				    	stmt.setString(++cnt, vend_cd);
				    	
				    	rset = stmt.executeQuery();
				    	
				    	 if (!rset.next() && !cd.equals("")) 
				    	 {
								
								logger.data(fname, (company_cd+"," + inv_seq + " , " + inv_type + "," + fin_yr + "," + vend_cd + "," + supp_cd + "," ), conn, "");
								
								stmt1.executeUpdate();
								stmt1.close();
								
								logger_count++;
				    	 }
				    	 else 
				    	 {
								stmt1.close();
								skipped_count++;     
								logger.data(fname, (company_cd+"," + inv_seq + " , " + inv_type + "," + fin_yr + "," + vend_cd + "," + supp_cd + "," ), conn, "E");
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
	
	
	
	
	public void FMS_OTH_INV_FILE_DTL() throws IOException, SQLException 
	{
		
		function_nm="FMS_OTH_INV_FILE_DTL()";
		try 
		{
			table_name = "FMS_OTH_INV_FILE_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String fin_yr = "",supp_cd="",inv_type="",inv_seq="",pdf_type="",file_nm="";
			
			queryString1 = "INSERT INTO FMS_OTH_INV_FILE_DTL (COMPANY_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,"
					+ "MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT,PDF_CONTENT,SF_GEN_DT,SIGNED_ENT_BY,SUPPLIER_CD) "
					+ "VALUES (?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			file1 = new File(migration_setup_dir + "EXPORT/FMS_OTH_INV_FILE_DTL_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_OTH_INV_FILE_DTL_"+start_end_dt+".csv";
				
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
				{
					String line = br.readLine();
					
					logger.checkpoint(fname, "COMPANY_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,SUPPLIER_CD,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) 
					{
						total_count++; 
						abbr = "";
						cd = "0";
						data = null;
						fin_yr="";supp_cd="";inv_type="";inv_seq="";pdf_type="";file_nm="";
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						for (int i = 0; i < line.split(",").length; i++)
					    {	
							data = null;
					    		
				    		if (i == 1) 
				    		{	
				    			inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			
				    			if (inv_seq!=null) 
				    			{
									queryString = "SELECT INVOICE_SEQ FROM FMS_OTH_INVOICE_MST WHERE COMPANY_CD = ? AND INVOICE_NO = ?";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, company_cd);
									stmt.setString(2, inv_seq);
									rset = stmt.executeQuery();
									if (rset.next()) 
									{
										inv_seq = rset.getString(1);
									}
									rset.close();
									stmt.close();
								}
				    			
								data = inv_seq;
			    			}
				    		else 
				    		{
				    			if (i == 2) 
					    		{	
					    			inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		if (i == 3) 
					    		{	
					    			fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		if (i == 4) 
					    		{	
					    			pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		if (i == 5) 
					    		{	
					    			file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			}
					    		if(i == 16)
					    		{
					    			supp_cd = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					    		}
					    		
						    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					    	
				    		}
					    		
				    		//System.out.println(index+"--"+data);
					    	stmt1.setString(index++, data);		    	
					    }
						
						int cnt = 0;
					    queryString = "SELECT SUPPLIER_CD "
					    		+ "FROM FMS_OTH_INV_FILE_DTL "
					    		+ "WHERE COMPANY_CD = ? AND SUPPLIER_CD = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND INVOICE_SEQ = ? AND PDF_TYPE = ?";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(++cnt, company_cd);
				    	stmt.setString(++cnt, supp_cd);
				    	stmt.setString(++cnt, inv_type);
				    	stmt.setString(++cnt, fin_yr);
				    	stmt.setString(++cnt, inv_seq);
				    	stmt.setString(++cnt, pdf_type);
				    	
				    	rset = stmt.executeQuery();
				    	
				    	 if (!rset.next() && !cd.equals("")) 
				    	 {
								
								logger.data(fname, (company_cd+"," + inv_seq + " , " + inv_type + "," + fin_yr + "," + pdf_type + "," + file_nm + "," + supp_cd + "," ), conn, "");
								
								stmt1.executeUpdate();
								stmt1.close();
								
								logger_count++;
				    	 }
				    	 else 
				    	 {
								stmt1.close();
								skipped_count++;     
								logger.data(fname, (company_cd+"," + inv_seq + " , " + inv_type + "," + fin_yr + "," + pdf_type + "," + file_nm + "," + supp_cd + "," ), conn, "E");
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

	public void FMS_OTH_INV_IRN_DTL() throws IOException, SQLException {

		function_nm="FMS_OTH_INV_IRN_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_OTH_INV_IRN_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_OTH_INV_IRN_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String seq_no="";

			queryString1 = "INSERT INTO FMS_OTH_INV_IRN_DTL(COMPANY_CD,INVOICE_NO,IRN_NO,XLS_FILE_NM,SIGN_QR_CODE,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_OTH_INV_IRN_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_OTH_INV_IRN_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,INVOICE_NO,IRN_NO,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						String irn_no="";
						seq_no="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							if (cell.getColumnIndex() == 1) {
								seq_no = cell.getStringCellValue();
								seq_no = seq_no.substring(1, seq_no.length()-1);
							}
							if (cell.getColumnIndex() == 2) {
								irn_no = cell.getStringCellValue();
								irn_no = irn_no.substring(1, irn_no.length()-1);
							}
							
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT INVOICE_NO FROM FMS_OTH_INV_IRN_DTL WHERE COMPANY_CD = ? AND INVOICE_NO = ? AND IRN_NO = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, seq_no);
						stmt.setString(3, irn_no);
						
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + seq_no + "," + irn_no + ","), conn, "");
							
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
			
			System.out.println("<<END>><<FMS_OTH_INV_IRN_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_OTH_INV_IRN_DTL>>,,", conn);
			
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

