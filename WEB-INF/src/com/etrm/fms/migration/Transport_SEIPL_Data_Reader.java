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

import com.etrm.fms.gta.DataBean_GTA_Remittance;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;

		

public class Transport_SEIPL_Data_Reader {

	

	String db_src_file_name="Transport_SEIPL_Data_Reader.java";

	String migration_setup_dir = "";
	
	String queryString="", queryString1="",queryString2 = "",queryString5="",queryString3="";
	Connection conn;
	ResultSet rset,rset1,rset2,rset5,rset3;
	PreparedStatement stmt,stmt1,stmt2,stmt5,stmt3;
	
	String function_nm = "", columns = "", data = "",table_name="";
	
	String sysDateTime = "";
	String start_end_dt = null;
	
	String fname = "",agmt_no="",agmt_rev="",cont_no="",cont_rev="",cont_type="",ent_pt="",exit_pt="",cus_cd="",cont_ref="",name="",sell_cont_map="",cont_name="",plant_seq="";
	String fname_error = "",state_code="";
	String int_cd="";
	String fname1 = "";

	int index = 0;
	int logger_count = 0;
	int skipped_count=0;  
	int total_count=0;  
	
	final String company_cd = "2";
	String cd = "", eff_dt = "", agmt_type = "";

	String checked_values = "", msg = "", msg_type = "",abbr="";
	String transporter_map = "", meter_map = "";

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
			
			fname = "DataLogs/Reader/Transport_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/Transport_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
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
					
					if (checked_values.contains("FMS_GTA_AGMT_MST")) {
						FMS_GTA_AGMT_MST();
						FMS_GTA_AGMT_BU();
						FMS_GTA_AGMT_TRANS_BU();
					}
					if (checked_values.contains("FMS_GTA_ENTRY_POINT")) {
						FMS_GTA_ENTRY_POINT();
					}
					if (checked_values.contains("FMS_GTA_EXIT_POINT")) {
						FMS_GTA_EXIT_POINT();
					}
					if (checked_values.contains("FMS_GTA_AGMT_BILLING_DTL")) {
						FMS_GTA_AGMT_BILLING_DTL();
					}
					if (checked_values.contains("FMS_GTA_CONT_MST")) {
						FMS_GTA_CONT_MST();
						FMS_GTA_CONT_TRANS_BU();
						FMS_GTA_CONT_BU();
					}
					if (checked_values.contains("FMS_GTA_CONT_MAP")) {
						FMS_GTA_CONT_MAP();
					}
					if (checked_values.contains("FMS_GTA_BILLING_DTL")) {
						FMS_GTA_BILLING_DTL();
					}
					if (checked_values.contains("FMS_DAILY_TRANSPORTER_NOM")) {
						FMS_DAILY_TRANSPORTER_NOM();
					}
					if (checked_values.contains("FMS_DAILY_TRANSPORTER_SCH")) {
						FMS_DAILY_TRANSPORTER_SCH();
					}
					if (checked_values.contains("FMS_DAILY_TRANSPORTER_ALLOC")) {
						FMS_DAILY_TRANSPORTER_ALLOC();
					} 
					if (checked_values.contains("FMS_GTA_SG_INV_MST")) {
						FMS_GTA_SG_INV_MST();
						FMS_GTA_SG_INV_TAX_DTL();
					} 
					if (checked_values.contains("FMS_GTA_PG_INV_MST")) {
						FMS_GTA_PG_INV_MST();
						FMS_GTA_PG_INV_TAX_DTL();
					}
					if (checked_values.contains("FMS_GTA_FFLOW_INV_MST")) {
						FMS_GTA_FFLOW_INV_MST();
						FMS_GTA_FFLOW_INV_TAX_DTL();
					}
					if (checked_values.contains("FMS_GTA_FFLOW_INV_DTL")) {
						FMS_GTA_FFLOW_INV_DTL();
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
				if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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


	public void FMS_GTA_AGMT_MST() throws IOException, SQLException {

		function_nm="FMS_GTA_AGMT_MST()";
		try {
			
			System.out.println("<<START>><<FMS_GTA_AGMT_MST>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_AGMT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String agmt_no="",agmt_rev="";
			
			queryString1 = "INSERT INTO FMS_GTA_AGMT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,START_DT,END_DT,STATUS,TOT_TRANS_QTY,UNIT_CD,CALC_BASE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,AGMT_NAME,AGMT_TYPE) "
					+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_AGMT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_GTA_AGMT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						cd = ""; eff_dt = "";
						String unit="";
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
							else if(cell.getColumnIndex() == 8) {	//UNIT_CD
								unit = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (unit!=null) {
									unit = unit.substring(1, unit.length() - 1);
								}
								queryString = "SELECT ENERGY_UNIT_CD FROM FMS_ENERGY_UNIT WHERE ENERGY_UNIT_ABR = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, unit);
								rset = stmt.executeQuery();
								if(rset.next()) {
									unit = rset.getString(1);
								}
								else {
									unit = "0";
								}
								rset.close();
								stmt.close();
								
								data = unit;
							}
							else {
								if (cell.getColumnIndex() == 2) {
									agmt_no = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if (agmt_no!=null) {
										agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
									}
								}
								if (cell.getColumnIndex() == 3) {
									agmt_rev = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if (agmt_rev!=null) {
										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
									}
								}
								if (cell.getColumnIndex() == 15) {
									agmt_type = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if (agmt_type!=null) {
										agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
									}
								}
								
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();

								if(data != null) {
							    	data = data.substring(1, data.length()-1);
							    }
							}
//							System.out.println(index+"::"+data);
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT AGMT_NAME FROM FMS_GTA_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND AGMT_TYPE = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_rev);
						stmt.setString(5, agmt_type);
						
						rset = stmt.executeQuery();
						
						if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + agmt_type + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + agmt_type + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_GTA_AGMT_MST>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_AGMT_MST>>,,", conn);
			
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
	
	public void FMS_GTA_AGMT_BU() throws IOException, SQLException {

		function_nm="FMS_GTA_AGMT_BU()";
		try {
			
			
			System.out.println("<<START>><<FMS_GTA_AGMT_BU>>");
			
			
			data = "";
			String plant;
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			
			queryString5 = "SELECT DISTINCT(SEQ_NO) FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY ='B' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			rset5 = stmt5.executeQuery();
			
			while(rset5.next()) {
				plant = rset5.getString(1);
			
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, AGMT_TYPE, NULL,'1',"
					+ "TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_GTA_AGMT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			

			while (rset.next()) {
				
				queryString1 = "INSERT INTO FMS_GTA_AGMT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
						+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				
				cd = rset.getString(2);
				agmt_no = rset.getString(3);
				agmt_rev = rset.getString(4);
				agmt_type = rset.getString(5);
				
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						
						if(i == 5) {		//PLANT_SEQ_NO
							data = plant;
						}
						
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
					queryString2 = "SELECT COUNTERPARTY_CD FROM FMS_GTA_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD =? AND AGMT_NO = ? "
							+ "AND AGMT_REV = ? AND PLANT_SEQ_NO = ? AND AGMT_TYPE = ? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, company_cd);
				  	stmt2.setString(2, cd);
			    	stmt2.setString(3, agmt_no);
			    	stmt2.setString(4, agmt_rev);
			    	stmt2.setString(5, plant);
			    	stmt2.setString(6, agmt_type);
				
					rset2 = stmt2.executeQuery();
					
					if (!rset2.next()) {
						stmt1.executeUpdate();
						stmt1.close();
					}
					else {

						stmt1.close();
						
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
			
		
			System.out.println("<<END>><<FMS_GTA_AGMT_BU>>");
			System.out.println();

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
	
	public void FMS_GTA_AGMT_TRANS_BU() throws IOException, SQLException {
		
		function_nm="FMS_GTA_AGMT_TRANS_BU()";
		try {
			
			
			System.out.println("<<START>><<FMS_GTA_AGMT_TRANS_BU>>");
			
			
			data = "";
			String ent_by,ent_dt,plant;
			columns = "COMPANY_CD,COUNTRERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, NULL, NULL, NULL, SEQ_NO, NULL, NULL FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND ENTITY = 'R' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			while (rset.next()) {
				cd = rset.getString(2);
				agmt_no="";agmt_rev="";agmt_type="";ent_by="";ent_dt="";
				plant = rset.getString(6);
				queryString2 = "SELECT NULL, NULL, AGMT_NO, AGMT_REV, AGMT_TYPE, NULL, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_GTA_AGMT_MST WHERE COMPANY_CD = ? AND "
						+ "COUNTERPARTY_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1,company_cd);
				stmt2.setString(2, cd);
				rset2 = stmt2.executeQuery();
				
				while(rset2.next())	{
					agmt_no = rset2.getString(3);
					agmt_rev = rset2.getString(4);
					agmt_type = rset2.getString(5);
					ent_by = rset2.getString(7);
					ent_dt = rset2.getString(8); 
					
				queryString1 = "INSERT INTO FMS_GTA_AGMT_TRANS_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,PLANT_SEQ_NO,"
						+ "ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				
				for(int i = 0;i < columns.split(",").length;i++) {
					data = "";
					data = rset2.getString(i+1) == null ? "" : rset2.getString(i+1);
					
					if(i == 0) {	//COMPANY_CD
						data = company_cd;
					}
					else if(i == 1) {	//COUNTERPARTY_CD
						data = cd;
					}
					else if(i == 5) {	//PLANT_SEQ_NO
						data = plant;
					}
					stmt1.setString(i+1,data);
				}
				
				//for data already exists..
				queryString5 = "SELECT COUNTERPARTY_CD FROM FMS_GTA_AGMT_TRANS_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? "
						+ "AND AGMT_REV = ? AND AGMT_TYPE = ? AND PLANT_SEQ_NO = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, cd);
				stmt5.setString(3, agmt_no);
				stmt5.setString(4, agmt_rev);
				stmt5.setString(5, agmt_type);
				stmt5.setString(6, plant);
				
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next()) {
					stmt1.executeUpdate();
					stmt1.close();
				}
				else {
					stmt1.close();
				}
				stmt5.close();
				rset5.close();
			}
			rset2.close();
			stmt2.close();
			
			}
			
			rset.close();
			stmt.close();
			
			
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			
			System.out.println("<<END>><<FMS_GTA_AGMT_TRANS_BU>>");
			System.out.println();
			
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
	
	
	public void FMS_GTA_ENTRY_POINT() throws IOException, SQLException {
		
		function_nm="FMS_GTA_ENTRY_POINT()";
		try {
			
			System.out.println("<<START>><<FMS_GTA_ENTRY_POINT>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_ENTRY_POINT>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String agmt_no="",agmt_rev="";
			
			queryString1 = "INSERT INTO FMS_GTA_ENTRY_POINT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,TRANSPORTER_CD,PLANT_SEQ,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,AGMT_TYPE) "
					+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_ENTRY_POINT_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_GTA_ENTRY_POINT_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,PLANT_SEQ,AGMT_TYPE,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					data = "";
					cd = ""; eff_dt = "";
					String plant = "",plant_seq="";
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						
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
						else if(cell.getColumnIndex() == 4) {	//TRANSPORTER_CD
							data = cd;
						}
						else if(cell.getColumnIndex() == 5) {	//PLANT_SEQ
							plant = cell.getStringCellValue().contains("'null'") ? "Null" : cell.getStringCellValue();
							if (!plant.equals("Null")) {
								plant = plant.substring(1,plant.length()-1).toUpperCase();
							}
							
							queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE ENTITY = 'R' AND COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND UPPER(PLANT_ABBR) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, plant);
							rset = stmt.executeQuery();
							if(rset.next()) {
								plant_seq = rset.getString(1);
							}else {
								plant_seq="";
							}
							rset.close();
							stmt.close();
							data = plant_seq;
						}
						else {
							if (cell.getColumnIndex() == 2) {
								agmt_no = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (agmt_no!=null) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 3) {
								agmt_rev = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (agmt_rev!=null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 10) {
								agmt_type = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (agmt_type!=null) {
									agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
								}
							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();

							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
						}
//							System.out.println(index+"::"+data);
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT AGMT_NO FROM FMS_GTA_ENTRY_POINT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND TRANSPORTER_CD = ? AND PLANT_SEQ = ? AND AGMT_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev);
					stmt.setString(5, cd);
					stmt.setString(6, plant_seq);
					stmt.setString(7, agmt_type);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !plant_seq.equals("") && !cd.equals("")) {
						//System.out.println(queryString1);
						
							logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + plant_seq + "," + agmt_type + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						
						skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + plant_seq + "," + agmt_type + ","), conn, "E");
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
			
			System.out.println("<<END>><<FMS_GTA_ENTRY_POINT>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_ENTRY_POINT>>,,", conn);
			
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
	
	public void FMS_GTA_EXIT_POINT() throws IOException, SQLException {
		
		function_nm="FMS_GTA_EXIT_POINT()";
		try {
			
			System.out.println("<<START>><<FMS_GTA_EXIT_POINT>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_EXIT_POINT>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String agmt_no="",agmt_rev="",cus_cd="",entity="";
			
			queryString1 = "INSERT INTO FMS_GTA_EXIT_POINT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,ENTITY,ENTITY_CD,PLANT_SEQ,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,AGMT_TYPE) "
					+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_EXIT_POINT_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_GTA_EXIT_POINT_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,ENTITY_CD,PLANT_SEQ,AGMT_TYPE,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					data = "";
					cd = ""; eff_dt = "";abbr = "";agmt_type="";
					String plant = "",plant_seq="";
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						
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
						else if(cell.getColumnIndex() == 5) {	//ENTITY_CD
							abbr = cell.getStringCellValue().contains("'null'") ? "Null" : cell.getStringCellValue();
							if (!abbr.equals("Null")) {
								abbr = abbr.substring(1,abbr.length()-1).toUpperCase();
							}
							
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, abbr);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cus_cd = rset.getString(1);
							}
							else {
								cus_cd = "";
							}
							rset.close();
							stmt.close();
							
							data = cus_cd;
						}
						else if(cell.getColumnIndex() == 6) {	//PLANT_SEQ
							plant = cell.getStringCellValue().contains("'null'") ? "Null" : cell.getStringCellValue();
							if (!plant.equals("Null")) {
								plant = plant.substring(1,plant.length()-1).toUpperCase();
							}
							if(cd.equals(cus_cd)) {
							queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE ENTITY = 'R' AND COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND UPPER(PLANT_ABBR) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cus_cd);
							stmt.setString(3, plant);
							rset = stmt.executeQuery();
							if(rset.next()) {
								plant_seq = rset.getString(1);
							} else {
								plant_seq = "";
							}
							rset.close();
							stmt.close();
							}
							else {
							queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE ENTITY = 'C' AND COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND UPPER(PLANT_ABBR) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cus_cd);
							stmt.setString(3, plant);
							rset = stmt.executeQuery();
							if(rset.next()) {
								plant_seq = rset.getString(1);
							} else {
								plant_seq = "";
							}
							rset.close();
							stmt.close();
							}
							data = plant_seq;
						}
						else {
							if (cell.getColumnIndex() == 2) {
								agmt_no = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (agmt_no!=null) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 3) {
								agmt_rev = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (agmt_rev!=null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 4) {
								entity = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (entity!=null) {
									entity = entity.substring(1, entity.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 11) {
								agmt_type = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (agmt_type!=null) {
									agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
								}
							}
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();

							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
						}
//							System.out.println(index+"::"+data);
						stmt1.setString(index++, data);
					}
					
					queryString = "SELECT AGMT_NO FROM FMS_GTA_EXIT_POINT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND ENTITY = ? AND ENTITY_CD = ? AND PLANT_SEQ = ? AND AGMT_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev);
					stmt.setString(5, entity);
					stmt.setString(6, cus_cd);
					stmt.setString(7, plant_seq);
					stmt.setString(8, agmt_type);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !plant_seq.equals("") && !cd.equals("")) {
						//System.out.println(queryString1);
						
							logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + cus_cd + "," + plant_seq + ","  + agmt_type + "," ), conn, "");
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						
						skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + cus_cd + "," + plant_seq + ","  + agmt_type + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_GTA_EXIT_POINT>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_EXIT_POINT>>,,", conn);
			
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
	
	public void FMS_GTA_AGMT_BILLING_DTL() throws IOException, SQLException {
		
		function_nm="FMS_GTA_AGMT_BILLING_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_GTA_AGMT_BILLING_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_AGMT_BILLING_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			queryString1 = "INSERT INTO FMS_GTA_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,BILLING_FREQ,"
					+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,"
					+ "EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,"
					+ "EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) VALUES(?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_AGMT_BILLING_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_GTA_AGMT_BILLING_DTL_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,PLANT_SEQ,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					index = 1;
					data = "";
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						
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
						else if(cell.getColumnIndex() == 11) {
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name != null) {
								name = name.substring(1, name.length() - 1);
							}
							queryString = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) = ? ";
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, name);
					    	rset = stmt.executeQuery();
					    	if (rset.next()) {
					    		int_cd = rset.getString(1);
					    	}else {
					    		int_cd =  null;
					    	}
					    	rset.close();
					    	stmt.close();
					    	data = int_cd;
				    	}
						else if(cell.getColumnIndex() == 28) {	//PLANT_SEQ
							plant_seq = cell.getStringCellValue().contains("'null'") ? "Null" : cell.getStringCellValue();
							if (!plant_seq.equals("Null")) {
								plant_seq = plant_seq.substring(1,plant_seq.length()-1).toUpperCase();
							}
							
							queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE ENTITY = 'R' AND COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND UPPER(PLANT_ABBR) = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, plant_seq);
							rset = stmt.executeQuery();
							if(rset.next()) {
								plant_seq = rset.getString(1);
							} else {
								plant_seq = "";
							}
							rset.close();
							stmt.close();
							
						
							data = plant_seq;
						}
						else if (cell.getColumnIndex() == 29) { //holiday_state
				    		queryString = "SELECT B.TIN FROM  FMS_COUNTERPARTY_PLANT_DTL A,FMS_STATE_MST B WHERE A.COUNTERPARTY_CD = ? AND B.STATE_NM = A.PLANT_STATE ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cd);
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
							if (cell.getColumnIndex() == 2) {
								agmt_no = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (agmt_no!=null) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 3) {
								agmt_rev = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (agmt_rev!=null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
							}
							if (cell.getColumnIndex() == 4) {
								agmt_type = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (agmt_type!=null) {
									agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
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
					
					queryString = "SELECT AGMT_NO FROM FMS_GTA_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ?  AND PLANT_SEQ_NO = ? AND AGMT_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev);
					stmt.setString(5, plant_seq);
					stmt.setString(6, agmt_type);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !plant_seq.equals("") && !cd.equals("")) {
						//System.out.println(queryString1);
						
							logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + agmt_type + "," + plant_seq + "," ), conn, "");
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						
						skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + agmt_no + "," + agmt_rev + "," + agmt_type + "," + plant_seq + "," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_GTA_AGMT_BILLING_DTL>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_AGMT_BILLING_DTL>>,,", conn);
			
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
	
	public void FMS_GTA_CONT_MST() throws IOException, SQLException {
		
		function_nm="FMS_GTA_CONT_MST()";
		try {
			
			table_name = "FMS_GTA_CONT_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			int no;
			String abbr1;
			
			queryString1 = "INSERT INTO FMS_GTA_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,CONT_REF_NO,"
						+ "CT_REF_NO,START_DT,END_DT,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,MDQ,MDQ_UNIT,VARIABLE_MDQ,RATE_UNIT,TRANSPORT_RATE,POSITIVE_IMB_RATE,"
						+ "NEGETIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,SIP_PAY_RATE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,CALC_BASE,GCV,NCV,SIP_PAY_FREQ,CT_SEQ_NO,SIP_PAY_PERCENT,"
						+ "AGMT_TYPE,PARKING_RATE,MAX_PARK_QTY,MAX_PARK_UNIT) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_CONT_MST_"+start_end_dt+".xlsx");
			
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_GTA_CONT_MST_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,CONT_REF,CUSTOMER_CD,ENT_PT,EXIT_PT,AGMT_TYPE,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					abbr = "";
					abbr1 = "";
					cd = "0";
					data = null;
					no = 0;
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
				    cellIterator = row.cellIterator();
				    while (cellIterator.hasNext()) {
				    	
				    	cell = cellIterator.next();
						data = null;
						
				    	if (cell.getColumnIndex() == 0) {
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
				    			cd = rset.getString(1);
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	} 
				    	else if (cell.getColumnIndex() == 2) { //Agmt_no
					    	agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    	if (agmt_no != null) {
					    		agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
					    	agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    	if (agmt_rev != null) {
								agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
							}
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
//				    		queryString = "SELECT MAX(CONT_NO+1) FROM FMS_GTA_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? ";
//				    		stmt = conn.prepareStatement(queryString);
//				    		stmt.setString(1, company_cd);
//				    		stmt.setString(2, cd);
//				    		rset = stmt.executeQuery();
//				    		if(rset.next() && rset.getInt(1) > 0) {
//				    			no = rset.getInt(1);
//				    		}
//				    		else {
//				    			no = 1;
//				    		}
//				    		
//				    		rset.close();
//				    		stmt.close();
				    		data = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    	if (data != null) {
								data = data.substring(1, data.length() - 1);
							}
					    	no = Integer.parseInt(data);
							data = no+"";
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
				    	else if(cell.getColumnIndex() == 7) {	//CONT_NAME
				    		if (cont_type.equals("C") || cont_type.equals("R")) {
								cont_name = abbr + "-SEIPL-GTA" + agmt_no + "-REV" + agmt_rev + " " + cont_type + no+ "-REV" + cont_rev;
							}else if(cont_type.equals("K")) {
								cont_name = abbr + "-SEIPL-MPSA" + agmt_no + "-REV" + agmt_rev + " " + cont_type + no+ "-REV" + cont_rev;
							}
							data = cont_name;
				    	}
				    	else if(cell.getColumnIndex() == 8) {	//CONT_REF_NO
				    		cont_ref = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if(cont_ref!=null) {
				    			cont_ref = cont_ref.substring(1,cont_ref.length()-1);
				    		}
				    		data = cont_ref;
				    	}
				    	else if(cell.getColumnIndex() == 12) {
				    		ent_pt = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    	if (ent_pt != null) {
					    		ent_pt = ent_pt.substring(1, ent_pt.length() - 1);
					    		abbr1 = ent_pt.split("-")[1];
							}	
					    	//System.out.println(abbr1);
					    	queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE UPPER(PLANT_ABBR) = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr1.toUpperCase());
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			ent_pt = rset.getString(1);
				    		}else {
				    			ent_pt = "";
				    		}
				    		rset.close();
				    		stmt.close();
					    	
				    		data = cd+"-"+ent_pt;
				    	}
				    	else if(cell.getColumnIndex() == 13) {
				    		exit_pt = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    	if (exit_pt != null) {
					    		exit_pt = exit_pt.substring(1, exit_pt.length() - 1);
							}	
					    	
					    	abbr = exit_pt.split("-")[0];
					    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) LIKE ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr.toUpperCase()+"%");
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cus_cd = rset.getString(1);
				    		}else {
				    			cus_cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		
				    		exit_pt = exit_pt.substring(exit_pt.length()-1);
//				    		queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ? ";
//				    		stmt = conn.prepareStatement(queryString);
//				    		stmt.setString(1, exit_pt);
//				    		rset = stmt.executeQuery();
//				    		if (rset.next()) {
//				    			exit_pt = rset.getString(1);
//				    		}else {
//				    			exit_pt = "";
//				    		}
//				    		rset.close();
//				    		stmt.close();
//					    	System.out.println(exit_pt);
				    		if(cont_type.equals("K")) {
				    			data = "R-"+cus_cd+"-"+exit_pt;
				    		}
				    		else {
				    			data = cont_type+"-"+cus_cd+"-"+exit_pt;
				    		}
				    	}
				    	else if (cell.getColumnIndex() == 33) {
							agmt_type = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if (agmt_type!=null) {
								agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
							}
							data = agmt_type;
						}
				    	else {

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}	
				    	
//				    	System.out.println(index+"-"+data);
				    	stmt1.setString(index++, data);		    	
				    }
					
				    queryString = "SELECT COUNTERPARTY_CD FROM FMS_GTA_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_REF_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?  AND AGMT_TYPE = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, cont_ref);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, cont_type);
			    	stmt.setString(8, agmt_type);
			    	
			    	rset = stmt.executeQuery();
			    	
			    	 if (!rset.next() && !cd.equals("") && !cus_cd.equals("") && !ent_pt.equals("")) {
//							System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + no + "," + cont_rev + "," + cont_type + "," + cont_name + "," + cont_ref + "," + cus_cd + "," + ent_pt + "," + exit_pt + ","  + agmt_type + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
					}
			    	 else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + no + "," + cont_rev + "," + cont_type + "," + cont_name + "," + cont_ref + "," + cus_cd + "," + ent_pt + "," + exit_pt + ","  + agmt_type + ","), conn, "E");
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
	
	public void FMS_GTA_CONT_BU() throws IOException, SQLException {

		function_nm="FMS_GTA_CONT_BU()";
		try {
			
			
			System.out.println("<<START>><<FMS_GTA_CONT_BU>>");
			
			
			data = "";
			String plant="1",exit_pt="",ent_pt="";
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT,AGMT_TYPE";
			
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, EXIT_PT_MAPPING_ID,'1',"
					+ "TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), AGMT_TYPE, ENTRY_PT_MAPPING_ID FROM FMS_GTA_CONT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			

			while (rset.next()) {
				
				queryString1 = "INSERT INTO FMS_GTA_CONT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,"
						+ "ENT_BY,ENT_DT,AGMT_TYPE) VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?)";
				stmt1 = conn.prepareStatement(queryString1);
				
				cd = rset.getString(2);
				agmt_no = rset.getString(3);
				agmt_rev = rset.getString(4);
				cont_no = rset.getString(5);
				cont_rev = rset.getString(6);
				cont_type = rset.getString(7);
				exit_pt = rset.getString(8);
				agmt_type = rset.getString(11);
				ent_pt = rset.getString(12);
				
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						if(i == 7) {	//PLANT_SEQ_NO
							if(agmt_type.equals("P")) {	
								queryString2 = "SELECT PLANT_ABBR FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, company_cd);
								stmt2.setString(2, exit_pt.split("-")[0]);
								stmt2.setString(3, exit_pt.split("-")[1]);
								stmt2.setString(4, exit_pt.split("-")[2]);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									exit_pt = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								
								exit_pt = exit_pt.toUpperCase();
								
								if(exit_pt.equals("PIL MSK")) {
									plant = "2";
								}
								else {
									plant = "1";
								}
								data = plant;
							}
							else if(agmt_type.equals("G") && cont_type.equals("C")) {
								queryString2 = "SELECT PLANT_ABBR FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY = 'R' AND COUNTERPARTY_CD = ? AND SEQ_NO = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, company_cd);
								stmt2.setString(2, ent_pt.split("-")[0]);
								stmt2.setString(3, ent_pt.split("-")[1]);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									ent_pt = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								
								ent_pt = ent_pt.toUpperCase();
								
								if(ent_pt.equals("GAIL MSK")) {
									plant = "2";
								}
								else {
									plant = "1";
								}
								data = plant;
							}
							else if(agmt_type.equals("G") && cont_type.equals("R")) {
								queryString2 = "SELECT PLANT_ABBR FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY = 'R' AND COUNTERPARTY_CD = ? AND SEQ_NO = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, company_cd);
								stmt2.setString(2, ent_pt.split("-")[0]);
								stmt2.setString(3, ent_pt.split("-")[1]);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									ent_pt = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								
								ent_pt = ent_pt.toUpperCase();
								
								if(ent_pt.equals("GSPL DAH")) {
									plant = "3";
								}
								else {
									plant = "1";
								}
								data = plant;
							}
							else{
								data = plant;
						}
						}
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
					queryString5 = "SELECT COUNTERPARTY_CD FROM FMS_GTA_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD =? AND AGMT_NO = ? "
							+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? AND AGMT_TYPE = ? ";
					stmt5 = conn.prepareStatement(queryString5);
					stmt5.setString(1, company_cd);
				  	stmt5.setString(2, cd);
			    	stmt5.setString(3, agmt_no);
			    	stmt5.setString(4, agmt_rev);
			    	stmt5.setString(5, cont_no);
			    	stmt5.setString(6, cont_rev);
			    	stmt5.setString(7, cont_type);
			    	stmt5.setString(8, plant);
			    	stmt5.setString(9, agmt_type);
				
					rset5 = stmt5.executeQuery();
					
					if (!rset5.next()) {
						stmt1.executeUpdate();
						stmt1.close();
					}
					else {

						stmt1.close();
						
					}
				stmt5.close();
				rset5.close();
				}

			rset.close();
			stmt.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
		
			System.out.println("<<END>><<FMS_GTA_CONT_BU>>");
			System.out.println();

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
	
	public void FMS_GTA_CONT_TRANS_BU() throws IOException, SQLException {
		
		function_nm="FMS_GTA_CONT_TRANS_BU()";
		try {
			
			
			System.out.println("<<START>><<FMS_GTA_CONT_TRANS_BU>>");
			
			
			data = "";
			String plant="1",ent_pt="";
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT,AGMT_TYPE";
			
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, NULL,ENT_BY, "
					+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), AGMT_TYPE FROM FMS_GTA_CONT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			while (rset.next()) {
				plant = "";
				queryString1 = "INSERT INTO FMS_GTA_CONT_TRANS_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,"
						+ "ENT_BY,ENT_DT,AGMT_TYPE) VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?)";
				stmt1 = conn.prepareStatement(queryString1);
				
				cd = rset.getString(2);
				agmt_no = rset.getString(3);
				agmt_rev = rset.getString(4);
				cont_no = rset.getString(5);
				cont_rev = rset.getString(6);
				cont_type = rset.getString(7);
				plant = "1";
				agmt_type = rset.getString(11);
				
				for(int i = 0;i < columns.split(",").length;i++) {
					data = "";
					data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
					
					if(i == 7) {	//PLANT_SEQ_NO
						queryString5 = "SELECT ENTRY_PT_MAPPING_ID FROM FMS_GTA_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND"
								+ " AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
						stmt5 = conn.prepareStatement(queryString5);
						stmt5.setString(1, company_cd);
						stmt5.setString(2, cd);
						stmt5.setString(3, rset.getString(3));
						stmt5.setString(4, rset.getString(4));
						stmt5.setString(5, rset.getString(5));
						stmt5.setString(6, rset.getString(6));
						stmt5.setString(7, rset.getString(7));
						stmt5.setString(8, rset.getString(11));
						rset5 = stmt5.executeQuery();
						if(rset5.next()) {
							ent_pt = rset5.getString(1);
						}
						rset5.close();
						stmt5.close();
//						System.out.println(ent_pt);
						if(cd.equals("29")) {
							if(ent_pt.equals("29-4")) {
								queryString5 = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = '29' AND ENTITY = 'R' AND UPPER(PLANT_ABBR) = 'PIL AP' ";
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, company_cd);
								rset5 = stmt5.executeQuery();
								if(rset5.next()) {
									plant = rset5.getString(1);
								}
								rset5.close();
								stmt5.close();
							}
							else if(ent_pt.equals("29-1")) {
								queryString5 = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = '29' AND ENTITY = 'R' AND UPPER(PLANT_ABBR) = 'PIL GJ' ";
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, company_cd);
								rset5 = stmt5.executeQuery();
								if(rset5.next()) {
									plant = rset5.getString(1);
								}
								rset5.close();
								stmt5.close();
							}
							else if(ent_pt.equals("29-5")) {
								queryString5 = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = '29' AND ENTITY = 'R' AND UPPER(PLANT_ABBR) = 'PIL MH' ";
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, company_cd);
								rset5 = stmt5.executeQuery();
								if(rset5.next()) {
									plant = rset5.getString(1);
								}
								rset5.close();
								stmt5.close();	
							}
							else {
								queryString5 = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = '29' AND ENTITY = 'R' AND UPPER(PLANT_ABBR) = 'PIL GJ' ";
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, company_cd);
								rset5 = stmt5.executeQuery();
								if(rset5.next()) {
									plant = rset5.getString(1);
								}
								rset5.close();
								stmt5.close();
							}
//							System.out.println(plant);
							data = plant;
						}
						else if(cd.equals("4")) {
							if(ent_pt.equals("4-4")) {
								queryString5 = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = '4' AND ENTITY = 'R' AND PLANT_ABBR = 'GAIL GJ' ";
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, company_cd);
								rset5 = stmt5.executeQuery();
								if(rset5.next()) {
									plant = rset5.getString(1);
								}
								rset5.close();
								stmt5.close();
							}
							data = plant;
						}
//						else if(cd.equals("45")) {
//							if(ent_pt.equals("45-4")) {
//								queryString5 = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = '45' AND ENTITY = 'R' AND PLANT_ABBR = 'GSPL Gujarat' ";
//								stmt5 = conn.prepareStatement(queryString5);
//								stmt5.setString(1, company_cd);
//								rset5 = stmt5.executeQuery();
//								if(rset5.next()) {
//									plant = rset5.getString(1);
//								}
//								rset5.close();
//								stmt5.close();
//							}
//							data = plant;
//						}
						else {
							data = plant;
						}
					}
					stmt1.setString(i+1,data);
				}
				
				//for data already exists..
				queryString5 = "SELECT COUNTERPARTY_CD FROM FMS_GTA_CONT_TRANS_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD =? AND AGMT_NO = ? "
						+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? AND AGMT_TYPE = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, cd);
				stmt5.setString(3, agmt_no);
				stmt5.setString(4, agmt_rev);
				stmt5.setString(5, cont_no);
				stmt5.setString(6, cont_rev);
				stmt5.setString(7, cont_type);
				stmt5.setString(8, plant);
				stmt5.setString(9, agmt_type);
				
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next()) {
					stmt1.executeUpdate();
					stmt1.close();
				}
				else {
					stmt1.close();
				}
				stmt5.close();
				rset5.close();
			}
			
			rset.close();
			stmt.close();
			
			
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			
			System.out.println("<<END>><<FMS_GTA_CONT_TRANS_BU>>");
			System.out.println();
			
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
	
	public void FMS_GTA_CONT_MAP() throws IOException, SQLException {
		
		function_nm="FMS_GTA_CONT_MAP()";
		try {
			
			table_name = "FMS_GTA_CONT_MAP";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			String cont_ref1="",agmt_no1="",cont_no1="",cont_rev1="",map_entity;
			
			queryString1 = "INSERT INTO FMS_GTA_CONT_MAP(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CUSTOMER_CD,SELL_CONT_MAP,ENT_BY,ENT_DT,AGMT_TYPE,MAPED_ENTITY_TYPE) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_CONT_MAP_"+start_end_dt+".xlsx");
			
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_GTA_CONT_MAP_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_REF_NO,CUST_CD,SELL_CONT_MAP,AGMT_TYPE,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					abbr = "";
					cd = "0";
					data = null;
					map_entity="";
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
				    cellIterator = row.cellIterator();
				    while (cellIterator.hasNext()) {
				    	
				    	cell = cellIterator.next();
						data = null;
						
				    	if (cell.getColumnIndex() == 0) {
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
				    	else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		cont_ref1 = (cell.getStringCellValue().contains("'null'") ? "null" : cell.getStringCellValue());
				    		if (!cont_ref1.equals("null")) {
				    			cont_ref1 = cont_ref1.substring(1, cont_ref1.length() - 1);
							}
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_GTA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
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
					    			
//					    			System.out.println("matched_cont_ref::"+cont_ref);
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
				    		data = agmt_rev;
				    	}
				    	else if(cell.getColumnIndex() == 4) {
				    		data = cont_no;
				    	}
				    	else if(cell.getColumnIndex() == 5) {
				    		data = cont_rev;
				    	}
				    	else if(cell.getColumnIndex() == 6) {
				    		data = cont_type;
				    	}
				    	else if(cell.getColumnIndex() == 7) {
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name != null) {
								name = name.substring(1, name.length() - 1);
							}
				    		
					    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, name);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cus_cd = rset.getString(1);
				    		}else {
				    			cus_cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cus_cd;
				    	}
				    	else if(cell.getColumnIndex() == 8) {
				    		
				    		if(cont_ref1.startsWith("L")) {
				    			queryString = "SELECT AGMT_NO,CONT_NO,CONT_REV FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cus_cd);
					    		stmt.setString(2, cont_ref1);
					    		stmt.setString(3, cont_ref1.split("-")[0]);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no1 = rset.getString(1);
					    			cont_no1 = rset.getString(2);
					    			cont_rev1 = rset.getString(3);
					    			sell_cont_map = "L-"+agmt_no1+"-0-"+cont_no1+"-"+cont_rev1;
					    		} else {
					    			agmt_no1 = "";
					    			cont_no1 = "";
					    			cont_rev1 = "";
					    			sell_cont_map = "L-0-0-0-0";
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data = sell_cont_map;
					    		
				    		}else if(!cont_ref1.startsWith("L")){
				    			sell_cont_map = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    		if (sell_cont_map != null) {
					    			sell_cont_map = sell_cont_map.substring(1, sell_cont_map.length() - 1);
								}else {
									sell_cont_map = cont_ref1;
								}
					    		data = sell_cont_map;
				    		}
				    	}
				    	else if (cell.getColumnIndex() == 11) {
							agmt_type = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if (agmt_type!=null) {
								agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
							}
							data = agmt_type;
						}
				    	else if (cell.getColumnIndex() == 12) {
				    		map_entity = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
				    		if (map_entity!=null) {
				    			map_entity = map_entity.substring(1, map_entity.length() - 1);
				    		}
				    		data = map_entity;
				    	}
				    	else {

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}	
				    	
//				    	System.out.println(index+"-"+data);
				    	stmt1.setString(index++, data);		    	
				    }
					
				    queryString = "SELECT COUNTERPARTY_CD FROM FMS_GTA_CONT_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND SELL_CONT_MAP = ? AND CUSTOMER_CD = ? AND AGMT_TYPE = ? AND MAPED_ENTITY_TYPE = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, cont_type);
			    	stmt.setString(8, sell_cont_map);
			    	stmt.setString(9, cus_cd);
			    	stmt.setString(10, agmt_type);
			    	stmt.setString(11, map_entity);
			    	
			    	
			    	rset = stmt.executeQuery();
			    	
			    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !sell_cont_map.equals("") && !cus_cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + cont_ref + "," + cus_cd + "," + sell_cont_map + ","  + agmt_type + "," + map_entity + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
					}
			    	 else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + cont_ref + "," + cus_cd + "," + sell_cont_map + ","  + agmt_type + "," + map_entity + ","), conn, "E");
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
	
//FMS_GTA_BILLING_DTL
	public void FMS_GTA_BILLING_DTL() throws IOException, SQLException {
		
		function_nm="FMS_GTA_BILLING_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_GTA_BILLING_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_BILLING_DTL>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			queryString1 = "INSERT INTO FMS_GTA_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
					+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,"
					+ "EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,"
					+ "EXCLUDE_SAT,EFF_DT,BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE,AGMT_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_BILLING_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_GTA_BILLING_DTL_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				String agmt_type="",date="";
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,AGMT_TYPE,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					index = 1;
					data = "";
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
					cellIterator = row.cellIterator(); 
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						
						if(cell.getColumnIndex() == 0) {	//COMPANY_CD
							abbr = cell.getStringCellValue().contains("'null'") ? "Null" : cell.getStringCellValue();
							if (!abbr.equals("NULL")) {
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
						else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
				    			agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,AGMT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY') FROM FMS_GTA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE IN ('C','R','K') ";
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
					    			agmt_type=rset.getString(6);
					    			date = rset.getString(7);
					    			
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    			agmt_type = "";
					    			date="";
					    		}
					    		rset.close();
					    		stmt.close();
//				    		}
				    		
							data = agmt_no;
				    	}
						else if(cell.getColumnIndex() == 3) {
							data = agmt_rev;
						}
						else if(cell.getColumnIndex() == 4) {
							data = cont_no;				
						}
						else if(cell.getColumnIndex() == 5) {
							data = cont_rev;
						}
						else if(cell.getColumnIndex() == 6) {
							data = cont_type;
						}
						
						else if(cell.getColumnIndex() == 13) {
							String int_rate ="";
							int_rate = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (int_rate != null) {
				    			int_rate = int_rate.substring(1, int_rate.length() - 1);
							}
							queryString = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE INT_RATE_NM LIKE ? ";
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, int_rate+"%");
					    	rset = stmt.executeQuery();
					    	if (rset.next()) {
					    		int_cd = rset.getString(1);
					    	}else {
					    		int_cd =  null;
					    	}
					    	rset.close();
					    	stmt.close();
					    	data = int_cd;
				    	}
						else if(cell.getColumnIndex() == 27) {
							data = date;
						}
						else if(cell.getColumnIndex() == 31) {
							queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_TRANS_BU WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							stmt.setString(2, agmt_no);
							stmt.setString(3, agmt_rev);
							stmt.setString(4, cont_no);
							stmt.setString(5, cont_rev);
							stmt.setString(6, cont_type);
							stmt.setString(7, agmt_type);
							rset = stmt.executeQuery();
							if(rset.next()) {
								plant_seq = rset.getString(1);
							}
							else {
								plant_seq = "";
							}
							rset.close();
							stmt.close();
							data = plant_seq;
							
						}
						else if (cell.getColumnIndex() == 32) { //holiday_state
				    		queryString = "SELECT B.TIN FROM  FMS_COUNTERPARTY_BU_DTL A,FMS_STATE_MST B WHERE A.COUNTERPARTY_CD = ? AND A.SEQ_NO = ? AND B.STATE_NM = A.PLANT_STATE ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cd);
				    		stmt.setString(2, plant_seq);
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
						else if (cell.getColumnIndex() == 33) {
							data = agmt_type;
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
					
					queryString = "SELECT AGMT_NO FROM FMS_GTA_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? AND AGMT_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev);
					stmt.setString(5, cont_no);
					stmt.setString(6, cont_rev);
					stmt.setString(7, cont_type);
					stmt.setString(8, plant_seq);
					stmt.setString(9, agmt_type);
					
					rset = stmt.executeQuery();
					
					if (!rset.next() && !plant_seq.equals("") && !cd.equals("") && !agmt_no.equals("")&& !agmt_rev.equals("") && !cont_no.equals("")&& !cont_rev.equals("") && !cont_type.equals("")) {
						//System.out.println(queryString1);
						
							logger.data(fname, (cont_ref + "," + cd + "," + agmt_no + "," + agmt_rev + ","  +cont_no+","+cont_rev+","+cont_type+","+ plant_seq + ","+agmt_type+"," ), conn, "");
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						
						skipped_count++;     
						logger.data(fname, (cont_ref + "," + cd + "," + agmt_no + "," + agmt_rev + ","  +cont_no+","+cont_rev+","+cont_type+","+ plant_seq + ","+agmt_type+"," ), conn, "E");
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
			
			System.out.println("<<END>><<FMS_GTA_BILLING_DTL>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_BILLING_DTL>>,,,,,,", conn);
			
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
	
 public void FMS_DAILY_TRANSPORTER_NOM() throws IOException, SQLException {
		
		function_nm="FMS_DAILY_TRANSPORTER_NOM()";
		try {
			
			table_name = "FMS_DAILY_TRANSPORTER_NOM";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String map_id = "",bu_seq="",gas_dt="",nom_rev="",sell_cont="",mdq_unit="",mdq,cust_cd;
			queryString1 = "INSERT INTO FMS_DAILY_TRANSPORTER_NOM(COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SELL_CONT_MAP,"
					+ "GAS_DT,GEN_DT,GEN_TIME,NOM_REV_NO,MDQ,MDQ_UNIT,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,ENT_BY,"
					+ "ENT_DT,MODIFY_BY,MODIFY_DT,BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID) "
						+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_"+start_end_dt+".csv";
				
				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
									String line = br.readLine();
									
									logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SELL_CONT_MAP,GAS_DT,NOM_REV_NO,BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,TIMESTAMP,", conn);
									
									while ((line = br.readLine()) != null) {
										total_count++;
										abbr = "";
										cd = "0";
										data = null;
										map_id="";bu_seq="";sell_cont="";gas_dt="";nom_rev="";mdq_unit="";mdq="";cust_cd="";
										index = 1;
										stmt1 = conn.prepareStatement(queryString1);
										agmt_no = "";
										agmt_rev = "";
										cont_no = "";
										cont_rev = "";
										cont_type = "";
										ent_pt="";
										exit_pt="";
					//					row = rowIterator.next();
					//				    cellIterator = row.cellIterator();
					//				    while (cellIterator.hasNext()) {
										for (int i = 0; i < line.split(",").length; i++)
									    {	
					//				    	cell = cellIterator.next();
											data = null;
											
									    	if (i == 0) {	//COMPANY_CD
									    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!abbr.equals("NULL")) {
					//								abbr = abbr.substring(1, abbr.length() - 1);
					//							}
									    		
									    		data = company_cd;
									    	}
									    	else if (i == 1) {	//COUNTERPARTY_CD
									    		map_id = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//				    		if(map_id!= null) {
					//				    		map_id = map_id.substring(1,map_id.length()-1);
					//				    		}
									    		
									    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, abbr.toUpperCase());
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
									    	else if (i == 2) {	//CONTRACT_TYPE
					// 							cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							queryString = "SELECT CONTRACT_TYPE, AGMT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, ENTRY_PT_MAPPING_ID, EXIT_PT_MAPPING_ID, MDQ_UNIT FROM FMS_GTA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE IN ('C','K','R') ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, map_id);
					 							stmt.setString(2, cd);
					// 							stmt.setString(3, cont_type);
					 							rset = stmt.executeQuery();
					 							if (rset.next()) {
					 								cont_type = rset.getString(1);
					 								agmt_type = rset.getString(2);
					 								agmt_no = rset.getString(3);
					 								agmt_rev = rset.getString(4);
					 								cont_no = rset.getString(5);
					 								cont_rev = rset.getString(6);
					 								ent_pt = rset.getString(7);
					 								exit_pt = rset.getString(8);
					 								mdq_unit= rset.getString(9);
					 							}
					 							rset.close();
					 							stmt.close();
					 							
					 							data = cont_type;
					 						}
									    	else if (i == 3) { //AGMT_NO
												data = agmt_no;
									    	}
									    	else if (i == 4) { //AGMT_REV
												data = agmt_rev;
									    	}
									    	else if (i == 5) { //CONT_NO
									    		data = cont_no;
									    	}
									    	else if (i == 6) { //CONT_REV
									    		data = cont_rev;
									    	}
									    	else if(i == 7) {	//SELL_CONT_MAP
									    		if(cont_type.equals("C")) {
										    		queryString = "SELECT SELL_CONT_MAP, CUSTOMER_CD FROM FMS_GTA_CONT_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
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
										    			sell_cont = rset.getString(1);
										    			cust_cd = rset.getString(2);
										    		}else {
										    			sell_cont = "S-0-0-0-0";
										    			cust_cd = "0";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		if(cont_type.equals("C")) {
										    	if(sell_cont!=null && !sell_cont.equals("")) {
										    	sell_cont = cust_cd +"-"+sell_cont;
										    	}
									    		}
									    		else {
										    		sell_cont="0";
										    	}
										    		
									    		data = sell_cont;
									    		
									    	}
									    	else if(i == 8) {	//GAS_DT
									    		gas_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		data = gas_dt;
									    	}
									    	else if(i == 11) {	//NOM_REV_NO
									    		nom_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		data = nom_rev;
									    	}
									    	else if(i == 12) {	//MDQ
									    		mdq = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		if(mdq!=null) {
									    		data = mdq;
									    		}
					//				    		if(cd.equals("4"))
					//				    		{
					//				    			System.out.println(gas_dt+":::::"+mdq);
					//				    		}
									    	}
									    	else if(i == 13) {	//MDQ_UNIT
									    		mdq_unit = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		data = mdq_unit;
									    	}
									    	else if(i == 28) {	//BU_UNIT
												queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, company_cd);
												stmt.setString(2, cd);
												stmt.setString(3, agmt_no);
												stmt.setString(4, agmt_rev);
												stmt.setString(5, cont_no);
												stmt.setString(6, cont_rev);
												stmt.setString(7, cont_type);
												stmt.setString(8, agmt_type);
												rset = stmt.executeQuery();
												if (rset.next()) {
													bu_seq = rset.getString(1);
												}
												rset.close();
												stmt.close();
											data = bu_seq;
										}
									    	else if(i == 29) {	//ENTRY_PT_MAPPING_ID
									    		data = ent_pt;
									    	}
									    	else if(i == 30) {	//EXIT_PT_MAPPING_ID
									    		data = exit_pt;
									    	}
									    	else {
					
										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
									    	}	
									    	
					//				    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);		    	
									    }
										
									    queryString = "SELECT COUNTERPARTY_CD FROM FMS_DAILY_TRANSPORTER_NOM WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND SELL_CONT_MAP = ? AND GAS_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND NOM_REV_NO = ? AND ENTRY_PT_MAPPING_ID = ? AND EXIT_PT_MAPPING_ID = ?  ";
								    	stmt = conn.prepareStatement(queryString);
								    	stmt.setString(1, company_cd);
								    	stmt.setString(2, cd);
								    	stmt.setString(3, cont_type);
								    	stmt.setString(4, agmt_no);
								    	stmt.setString(5, agmt_rev);
								    	stmt.setString(6, cont_no);
								    	stmt.setString(7, cont_rev);
								    	stmt.setString(8, sell_cont);
								    	stmt.setString(9, gas_dt);
								    	stmt.setString(10, nom_rev);
								    	stmt.setString(11, ent_pt);
								    	stmt.setString(12, exit_pt);
								    	
								    	rset = stmt.executeQuery();
								    	
								    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !sell_cont.equals("") && !gas_dt.equals("") && !mdq_unit.equals("")) {
												//System.out.println(queryString1);
												
												logger.data(fname, (company_cd +"," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + sell_cont + "," + gas_dt+"," + nom_rev + "," + mdq_unit + "," + ent_pt + "," + exit_pt + "," ), conn, "");
												
												stmt1.executeUpdate();
												stmt1.close();
												
												logger_count++;
										}
								    	 else {
												stmt1.close();
												skipped_count++;     
												logger.data(fname, (company_cd +"," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + sell_cont + "," + gas_dt+"," + nom_rev + "," + mdq_unit + "," + ent_pt + "," + exit_pt + "," ), conn, "E");
										}
								    	 
								    	 rset.close();
										 stmt.close();
									}
									br.close();
				}
				
				// Below block of code is for unique SEIPL data
				//rowIterator = sheet.iterator();
								
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
	
 	public void FMS_DAILY_TRANSPORTER_SCH() throws IOException, SQLException {
		
		function_nm="FMS_DAILY_TRANSPORTER_SCH()";
		try {
			
			table_name = "FMS_DAILY_TRANSPORTER_SCH";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String map_id = "",bu_seq="",gas_dt="",sch_rev="",sell_cont="",mdq_unit="",mdq,cust_cd;
			queryString1 = "INSERT INTO FMS_DAILY_TRANSPORTER_SCH(COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SELL_CONT_MAP,"
					+ "GAS_DT,GEN_DT,GEN_TIME,SCH_REV_NO,MDQ,MDQ_UNIT,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,ENT_BY,"
					+ "ENT_DT,MODIFY_BY,MODIFY_DT,BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID) "
						+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_SCH_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_SCH_"+start_end_dt+".csv";
				
				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
									String line = br.readLine();
									
									logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SELL_CONT_MAP,GAS_DT,SCH_REV_NO,BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,TIMESTAMP,", conn);
									
									while ((line = br.readLine()) != null) {
										total_count++;
										abbr = "";
										cd = "0";
										data = null;
										map_id="";bu_seq="";sell_cont="";gas_dt="";sch_rev="";mdq="";cust_cd="";
										index = 1;
										stmt1 = conn.prepareStatement(queryString1);
										agmt_no = "";
										agmt_rev = "";
										cont_no = "";
										cont_rev = "";
										cont_type = "";
										ent_pt="";
										exit_pt="";
					//					row = rowIterator.next();
					//				    cellIterator = row.cellIterator();
					//				    while (cellIterator.hasNext()) {
										for (int i = 0; i < line.split(",").length; i++)
									    {	
					//				    	cell = cellIterator.next();
											data = null;
											
									    	if (i == 0) {	//COMPANY_CD
									    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!abbr.equals("NULL")) {
					//								abbr = abbr.substring(1, abbr.length() - 1);
					//							}
								
									    		data = company_cd;
									    	}
									    	else if (i == 1) {	//COUNTERPARTY_CD
									    		map_id = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//				    		if(map_id!= null) {
					//				    		map_id = map_id.substring(1,map_id.length()-1);
					//				    		}
									    		
									    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, abbr.toUpperCase());
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
									    	else if (i == 2) {	//CONTRACT_TYPE
					// 							cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							queryString = "SELECT CONTRACT_TYPE, AGMT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, ENTRY_PT_MAPPING_ID, EXIT_PT_MAPPING_ID, MDQ_UNIT FROM FMS_GTA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE IN ('C','K','R') ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, map_id);
					 							stmt.setString(2, cd);
					// 							stmt.setString(3, cont_type);
					 							rset = stmt.executeQuery();
					 							if (rset.next()) {
					 								cont_type = rset.getString(1);
					 								agmt_type = rset.getString(2);
					 								agmt_no = rset.getString(3);
					 								agmt_rev = rset.getString(4);
					 								cont_no = rset.getString(5);
					 								cont_rev = rset.getString(6);
					 								ent_pt = rset.getString(7);
					 								exit_pt = rset.getString(8);
					 								mdq_unit= rset.getString(9);
					 							}
					 							rset.close();
					 							stmt.close();
					 							
					 							data = cont_type;
					 						}
									    	else if (i == 3) { //AGMT_NO
												data = agmt_no;
									    	}
									    	else if (i == 4) { //AGMT_REV
												data = agmt_rev;
									    	}
									    	else if (i == 5) { //CONT_NO
									    		data = cont_no;
									    	}
									    	else if (i == 6) { //CONT_REV
									    		data = cont_rev;
									    	}
									    	else if(i == 7) {	//SELL_CONT_MAP
									    		if(cont_type.equals("C")) {
										    		queryString = "SELECT SELL_CONT_MAP, CUSTOMER_CD FROM FMS_GTA_CONT_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
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
										    			sell_cont = rset.getString(1);
										    			cust_cd = rset.getString(2);
										    		}else {
										    			sell_cont = "S-0-0-0-0";
										    			cust_cd = "0";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		if(cont_type.equals("C")) {
										    	if(sell_cont!=null && !sell_cont.equals("")) {
										    	sell_cont = cust_cd +"-"+sell_cont;
										    	}
									    		}
									    		else {
										    		sell_cont="0";
										    	}
										    		
									    		data = sell_cont;
									    		
									    	}
									    	else if(i == 8) {	//GAS_DT
									    		gas_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		data = gas_dt;
									    	}
									    	else if(i == 11) {	//SCH_REV_NO
									    		sch_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		data = sch_rev;
									    	}
									    	else if(i == 12) {	//MDQ
					//				    		queryString = "SELECT MDQ FROM FMS_DAILY_TRANSPORTER_NOM WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND AGMT_NO = ? "
					//				    				+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND SELL_CONT_MAP = ?  AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY NOM_REV_NO DESC "; 
					//				    		stmt = conn.prepareStatement(queryString);
					//				    		stmt.setString(1, company_cd);
					//				    		stmt.setString(2, cd);
					//				    		stmt.setString(3, cont_type);
					//				    		stmt.setString(4, agmt_no);
					//				    		stmt.setString(5, agmt_rev);
					//				    		stmt.setString(6, cont_no);
					//				    		stmt.setString(7, cont_rev);
					//				    		stmt.setString(8, sell_cont);
					//				    		stmt.setString(9, gas_dt);
					//				    		rset = stmt.executeQuery();
					//				    		if(rset.next()) {
					//				    			mdq = rset.getString(1);
					//				    		}
					//				    		rset.close();
					//				    		stmt.close();
									    		mdq = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		data = mdq;
									    		
					//				    		System.out.println("MDQ::"+mdq);
									    	}
									    	else if(i == 13) {	//MDQ_UNIT
									    		mdq_unit = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		data = mdq_unit;
									    	}
									    	
									    	else if(i == 28) {	//BU_UNIT
												queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, company_cd);
												stmt.setString(2, cd);
												stmt.setString(3, agmt_no);
												stmt.setString(4, agmt_rev);
												stmt.setString(5, cont_no);
												stmt.setString(6, cont_rev);
												stmt.setString(7, cont_type);
												stmt.setString(8, agmt_type);
												rset = stmt.executeQuery();
												if (rset.next()) {
													bu_seq = rset.getString(1);
												}
												rset.close();
												stmt.close();
											data = bu_seq;
										}
									    	else if(i == 29) {	//ENTRY_PT_MAPPING_ID
									    		data = ent_pt;
									    	}
									    	else if(i == 30) {	//EXIT_PT_MAPPING_ID
									    		data = exit_pt;
									    	}
									    	else {
					
										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
									    	}	
									    	
					//				    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);		    	
									    }
										
									    queryString = "SELECT COUNTERPARTY_CD FROM FMS_DAILY_TRANSPORTER_SCH WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND SELL_CONT_MAP = ? AND GAS_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND SCH_REV_NO = ? AND ENTRY_PT_MAPPING_ID = ? AND EXIT_PT_MAPPING_ID = ?  ";
								    	stmt = conn.prepareStatement(queryString);
								    	stmt.setString(1, company_cd);
								    	stmt.setString(2, cd);
								    	stmt.setString(3, cont_type);
								    	stmt.setString(4, agmt_no);
								    	stmt.setString(5, agmt_rev);
								    	stmt.setString(6, cont_no);
								    	stmt.setString(7, cont_rev);
								    	stmt.setString(8, sell_cont);
								    	stmt.setString(9, gas_dt);
								    	stmt.setString(10, sch_rev);
								    	stmt.setString(11, ent_pt);
								    	stmt.setString(12, exit_pt);
								    	
								    	rset = stmt.executeQuery();
								    	
								    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !sell_cont.equals("") && !mdq.equals("")) {
												//System.out.println(queryString1);
												
												logger.data(fname, (company_cd +"," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + sell_cont + "," + gas_dt+"," + sch_rev + "," + bu_seq + "," + ent_pt + "," + exit_pt + "," ), conn, "");
												
												stmt1.executeUpdate();
												stmt1.close();
												
												logger_count++;
										}
								    	 else {
												stmt1.close();
												skipped_count++;     
												logger.data(fname, (company_cd +"," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + sell_cont + "," + gas_dt+"," + sch_rev + "," + bu_seq + "," + ent_pt + "," + exit_pt + "," ), conn, "E");
										}
								    	 
								    	 rset.close();
										 stmt.close();
									}
									br.close();
				}
				
				// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
				
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
	
 	public void FMS_DAILY_TRANSPORTER_ALLOC() throws IOException, SQLException {
 		
 		function_nm="FMS_DAILY_TRANSPORTER_ALLOC()";
 		try {
 			
 			table_name = "FMS_DAILY_TRANSPORTER_ALLOC";
 			System.out.println("<<START>><<"+table_name+">>");
 			
 			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
 			
 			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
 			
 			data = "";
 			logger_count = 0;   
 			skipped_count = 0;   
 			total_count = 0; 
 			String map_id = "",bu_seq="",gas_dt="",alloc_rev="",sell_cont="",mdq_unit="",mdq,gen_dt,qtymmbtu,cust_cd;
 			queryString1 = "INSERT INTO FMS_DAILY_TRANSPORTER_ALLOC(COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SELL_CONT_MAP,"
 					+ "GAS_DT,GEN_DT,GEN_TIME,ALLOC_REV_NO,MDQ,MDQ_UNIT,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,ENT_BY,"
 					+ "ENT_DT,MODIFY_BY,MODIFY_DT,BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,ADJ_IMBALANCE) "
 					+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
 					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
 			
 			stmt1 = conn.prepareStatement(queryString1);
 			
 			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_ALLOC_"+start_end_dt+".csv");
 			
 			if(file1.exists()) {
 				
 				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_ALLOC_"+start_end_dt+".csv";
 				
 				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
					 				String line = br.readLine();
					 				
					 				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SELL_CONT_MAP,GAS_DT,ALLOC_REV_NO,BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,TIMESTAMP,", conn);
					 				
					 				while ((line = br.readLine()) != null) {
					 					total_count++;
					 					abbr = "";
					 					cd = "0";
					 					data = null;
					 					map_id="";bu_seq="";sell_cont="";gas_dt="";alloc_rev="";gen_dt="";mdq="";qtymmbtu="";cust_cd="";
					 					index = 1;
					 					stmt1 = conn.prepareStatement(queryString1);
					 					agmt_no = "";
										agmt_rev = "";
										cont_no = "";
										cont_rev = "";
										cont_type = "";
										ent_pt="";
										exit_pt="";
					//					row = rowIterator.next();
					//				    cellIterator = row.cellIterator();
					//				    while (cellIterator.hasNext()) {
					 					for (int i = 0; i < line.split(",").length; i++)
					 					{	
					//				    	cell = cellIterator.next();
					 						data = null;
					 						
					 						if (i == 0) {	//COMPANY_CD
					 							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!abbr.equals("NULL")) {
					//								abbr = abbr.substring(1, abbr.length() - 1);
					//							}
					 							
					 							data = company_cd;
					 						}
					 						else if (i == 1) {	//COUNTERPARTY_CD
					 							map_id = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//				    		if(map_id!= null) {
					//				    		map_id = map_id.substring(1,map_id.length()-1);
					//				    		}
					 							
					 							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, abbr.toUpperCase());
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
					 						else if (i == 2) {	//CONTRACT_TYPE
					// 							cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							queryString = "SELECT CONTRACT_TYPE, AGMT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, ENTRY_PT_MAPPING_ID, EXIT_PT_MAPPING_ID, MDQ_UNIT FROM FMS_GTA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE IN ('C','K','R') ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, map_id);
					 							stmt.setString(2, cd);
					// 							stmt.setString(3, cont_type);
					 							rset = stmt.executeQuery();
					 							if (rset.next()) {
					 								cont_type = rset.getString(1);
					 								agmt_type = rset.getString(2);
					 								agmt_no = rset.getString(3);
					 								agmt_rev = rset.getString(4);
					 								cont_no = rset.getString(5);
					 								cont_rev = rset.getString(6);
					 								ent_pt = rset.getString(7);
					 								exit_pt = rset.getString(8);
					 								mdq_unit= rset.getString(9);
					 							}
					 							rset.close();
					 							stmt.close();
					 							
					 							data = cont_type;
					 						}
					 						else if (i == 3) { //AGMT_NO
					 							data = agmt_no;
					 						}
					 						else if (i == 4) { //AGMT_REV
					 							data = agmt_rev;
					 						}
					 						else if (i == 5) { //CONT_NO
					 							data = cont_no;
					 						}
					 						else if (i == 6) { //CONT_REV
					 							data = cont_rev;
					 						}
					 						else if(i == 7) {	//SELL_CONT_MAP
									    		if(cont_type.equals("C")) {
										    		queryString = "SELECT SELL_CONT_MAP, CUSTOMER_CD FROM FMS_GTA_CONT_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
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
										    			sell_cont = rset.getString(1);
										    			cust_cd = rset.getString(2);
										    		}else {
										    			sell_cont = "S-0-0-0-0";
										    			cust_cd = "0";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
										    		if(cont_type.equals("C")) {
											    	if(sell_cont!=null && !sell_cont.equals("")) {
											    	sell_cont = cust_cd +"-"+sell_cont;
											    	}
										    		}
									    		else {
										    		sell_cont="0";
										    	}
										    		
									    		data = sell_cont;
									    		
									    	}
					 						else if(i == 8) {	//GAS_DT
					 							gas_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = gas_dt;
					 						}
					 						else if(i == 9) {	//GEN_DT
					 							gen_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = gen_dt;
					 						}
					 						else if(i == 11) {	//NOM_REV_NO
					 							alloc_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = alloc_rev;
					 						}
					 						else if(i == 12) {	//MDQ
					//				    		queryString = "SELECT MDQ FROM FMS_DAILY_TRANSPORTER_NOM WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND AGMT_NO = ? "
					//				    				+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND SELL_CONT_MAP = ?  AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY NOM_REV_NO DESC "; 
					//				    		stmt = conn.prepareStatement(queryString);
					//				    		stmt.setString(1, company_cd);
					//				    		stmt.setString(2, cd);
					//				    		stmt.setString(3, cont_type);
					//				    		stmt.setString(4, agmt_no);
					//				    		stmt.setString(5, agmt_rev);
					//				    		stmt.setString(6, cont_no);
					//				    		stmt.setString(7, cont_rev);
					//				    		stmt.setString(8, sell_cont);
					//				    		stmt.setString(9, gas_dt);
					//				    		rset = stmt.executeQuery();
					//				    		if(rset.next()) {
					//				    			mdq = rset.getString(1);
					//				    		}
					//				    		else {
					//				    			mdq = "";
					//				    		}
					//				    		rset.close();
					//				    		stmt.close();
					 							mdq = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		data = mdq;
									    		
					//				    		System.out.println("MDQ::"+mdq);
									    	}
					 						else if(i == 13) {	//MDQ_UNIT
					 							mdq_unit = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		data = mdq_unit;
									    	}
					 						else if(i == 17) {	//MMBTU
					 							qtymmbtu = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							if(qtymmbtu!=null && qtymmbtu.length() > 8) {
					 								qtymmbtu =  qtymmbtu.substring(1,qtymmbtu.length()-1);
					 							}
					 							data = qtymmbtu;
					 						}
					 						else if(i == 31) {
					 							data = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							if(data!=null && data.length() > 8) {
					 								data =  data.substring(1,data.length()-1);
					 							}
					 						}
					// 						else if(i == 28) {	//BU_SEQ
					// 							bu_seq = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					// 							data = bu_seq;
					// 						}
					 						else if(i == 28) {	//BU_UNIT
													queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, company_cd);
													stmt.setString(2, cd);
													stmt.setString(3, agmt_no);
													stmt.setString(4, agmt_rev);
													stmt.setString(5, cont_no);
													stmt.setString(6, cont_rev);
													stmt.setString(7, cont_type);
													stmt.setString(8, agmt_type);
													rset = stmt.executeQuery();
													if (rset.next()) {
														bu_seq = rset.getString(1);
													}
													rset.close();
													stmt.close();
												data = bu_seq;
											}
					 						else if(i == 29) {	//ENTRY_PT_MAPPING_ID
					 							data = ent_pt;
					 						}
					 						else if(i == 30) {	//EXIT_PT_MAPPING_ID
					 							data = exit_pt;
					 						}
					 						else {
					 							
					 							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
					 						}	
					 						
					//				    	System.out.println(index+"-"+data);
					 						stmt1.setString(index++, data);		    	
					 					}
					 					
					 					queryString = "SELECT COUNTERPARTY_CD FROM FMS_DAILY_TRANSPORTER_ALLOC WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND AGMT_NO = ? "
					 							+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND SELL_CONT_MAP = ? AND GAS_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND ALLOC_REV_NO = ? "
					 							+ "AND ENTRY_PT_MAPPING_ID = ? AND EXIT_PT_MAPPING_ID = ? AND BU_SEQ = ? ";
					 					stmt = conn.prepareStatement(queryString);
					 					stmt.setString(1, company_cd);
					 					stmt.setString(2, cd);
					 					stmt.setString(3, cont_type);
					 					stmt.setString(4, agmt_no);
					 					stmt.setString(5, agmt_rev);
					 					stmt.setString(6, cont_no);
					 					stmt.setString(7, cont_rev);
					 					stmt.setString(8, sell_cont);
					 					stmt.setString(9, gas_dt);
					 					stmt.setString(10, alloc_rev);
					 					stmt.setString(11, ent_pt);
					 					stmt.setString(12, exit_pt);
					 					stmt.setString(13, bu_seq);
					 					
					 					rset = stmt.executeQuery();
					 					
					 					if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !sell_cont.equals("") && !mdq.equals("")) {
					 						//System.out.println(queryString1);
					 						
					 						logger.data(fname, (company_cd +"," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + sell_cont + "," + gas_dt+"," + alloc_rev + "," + bu_seq + "," + ent_pt + "," + exit_pt + "," ), conn, "");
					 						
					 						stmt1.executeUpdate();
					 						stmt1.close();
					 						
					 						logger_count++;
					 					}
					 					else {
					 						stmt1.close();
					 						skipped_count++;     
					 						logger.data(fname, (company_cd +"," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + sell_cont + "," + gas_dt+"," + alloc_rev + "," + bu_seq + "," + ent_pt + "," + exit_pt + "," ), conn, "E");
					 					}
					 					
					 					rset.close();
					 					stmt.close();
					 				}
					 				br.close();
				}
 				
 				// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
 				
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
 	
 
 	public void FMS_GTA_SG_INV_MST() throws IOException, SQLException {
 		
 		function_nm="FMS_GTA_SG_INV_MST()";
 		try {
 			
 			table_name = "FMS_GTA_SG_INV_MST";
 			System.out.println("<<START>><<"+table_name+">>");
 			
 			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
 			
 			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
 			
 			DateUtil utilDate = new DateUtil();
			DataBean_GTA_Remittance bill_freq = new DataBean_GTA_Remittance();
 			
 			data = "";
 			logger_count = 0;   
 			skipped_count = 0;   
 			total_count = 0; 
 			String map_id = "",bu_seq="",bu_cont="",trans_bu_seq="",fin_year="",st_dt="",end_dt="",alloc_qty="",transport_rate="",tax="",tax_struct_cd="",tax_eff_dt= "",
 					inv_type="",plant="",bil_freq="",tax_tds="",tds_struct_cd="",tds_eff_dt= "",freq="",inv_no="",gross_amt="",billing_eff_dt="";
 	
 			String tds="",tds_date="",tds_cd="",tds_amt="",tds_ftr="",tds_ftr1="",tcd_tds="";
 			queryString1 = "INSERT INTO FMS_GTA_SG_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,TRANS_BU_UNIT,INVOICE_TYPE,"
 					+ "ALLOC_QTY,DEFICIENCY_QTY,NEG_IMB_QTY,POS_IMB_QTY,UNAUTH_OVERRUN_QTY,PARKING_QTY,SIP_PAY_RATE,TRANSPORT_RATE,NEGETIVE_IMB_RATE,POSITIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,PARKING_RATE,"
					+ "TRANSMISSION_AMT,NEG_IMB_AMT,POS_IMB_AMT,UNAUTH_OVERRUN_AMT,PARKING_AMT,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,RATE_UNIT,SALE_AMT,EXCHG_RATE_CD,"
					+ "EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "INV_STATUS,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,"
					+ "FINANCIAL_YEAR,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,DEFICIENCY_AMT,SIP_PAY_FREQ,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,TCS_TDS,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SYS_INV_NO,INV_COMPONENT,FIN_SYS)"
					+"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
 			
 			stmt1 = conn.prepareStatement(queryString1);
			Map<String, Integer> invseq = new HashMap<String, Integer>();
 			file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_SG_INV_MST_"+start_end_dt+".csv");
 			
 			if(file1.exists()) {
 				
 				String fileName = migration_setup_dir + "EXPORT/FMS_GTA_SG_INV_MST_"+start_end_dt+".csv";
 				
 				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
					 				String line = br.readLine();
					 				
					 				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANS_BU_UNIT, BU_UNIT, FREQ, PERIOD_START_DT, PERIOD_END_DT, INVOICE_TYPE, INVOICE_SEQ, INV_NO ,TIMESTAMP,", conn);
					 				int inv_seq_no = 1;
					 				while ((line = br.readLine()) != null) {
					 					total_count++;
					 					abbr = "";
					 					cd = "0";
					 					data = null;
					 					map_id="";bu_seq="";bu_cont="";trans_bu_seq="";fin_year="";st_dt="";end_dt="";alloc_qty="";transport_rate="";tax="";tax_struct_cd="";
					 					tax_eff_dt= "";inv_type ="";plant="";tax_tds="";tds_struct_cd="";tds_eff_dt= "";inv_no="";
					 					index = 1;
					 					stmt1 = conn.prepareStatement(queryString1);
					 					agmt_no = "";
					 					agmt_rev = "";
					 					cont_no = "";
					 					cont_rev = "";
					 					cont_type = "";
					 					ent_pt="";
					 					exit_pt="";
					 					
					 					double qty=0;
					 					double rate=0;
					 					double sale_amt=0; 
					 					double tax_amt=0; 
					 					String invoice_amt=""; 
//					 					double tds_amt=0; 
					//					row = rowIterator.next();
					//				    cellIterator = row.cellIterator();
					//				    while (cellIterator.hasNext()) {
					 					for (int i = 0; i < line.split(",").length; i++)
					 					{	
					//				    	cell = cellIterator.next();
					 						data = null;
					 						
					 						if (i == 0) {	//COMPANY_CD
					 							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!abbr.equals("NULL")) {
					//								abbr = abbr.substring(1, abbr.length() - 1);
					//							}
					 							
					 							data = company_cd;
					 						}
					 						
					 						else if (i == 1) {	//COUNTERPARTY_CD
					 							map_id = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//				    		if(map_id!= null) {
					//				    		map_id = map_id.substring(1,map_id.length()-1);
					//				    		}
					 							
					 							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, abbr.toUpperCase());
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
					 						
					 						else if (i == 2) {	//AGMT_NO
					 							
					 							queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, ENTRY_PT_MAPPING_ID, AGMT_TYPE FROM FMS_GTA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE IN('C','K','R') ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, map_id);
					 							stmt.setString(2, cd);
					// 							stmt.setString(3, cont_type);
					 							rset = stmt.executeQuery();
					 							if (rset.next()) {
					 								cont_type = rset.getString(1);
					 								agmt_no = rset.getString(2);
					 								agmt_rev = rset.getString(3);
					 								cont_no = rset.getString(4);
					 								cont_rev = rset.getString(5);
					 								ent_pt = rset.getString(6);
					 								agmt_type = rset.getString(7);
					 							}
					 							rset.close();
					 							stmt.close();
					 							data = agmt_no;
					 						}
					 						
					 						else if (i == 3) { //AGMT_REV
					 							data = agmt_rev;
					 						}
					 						
					 						else if (i == 4) { //CONT_NO
					 							data = cont_no;
					 						}
					 						
					 						else if (i == 5) { //CONT_REV
					 							data = cont_rev;
					 						}
					 						
					 						else if (i == 6) { //CONTRACT_TYPE
					 							data = cont_type;
					 						}
					 						
					 						else if(i == 7) {	//BU_UNIT
					 								queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
					 								stmt = conn.prepareStatement(queryString);
					 								stmt.setString(1, company_cd);
					 								stmt.setString(2, cd);
					 								stmt.setString(3, agmt_no);
					 								stmt.setString(4, agmt_rev);
					 								stmt.setString(5, cont_no);
					 								stmt.setString(6, cont_rev);
					 								stmt.setString(7, cont_type);
					 								stmt.setString(8, agmt_type);
					 								rset = stmt.executeQuery();
					 								if (rset.next()) {
					 									bu_seq = rset.getString(1);
					 								}
					 								rset.close();
					 								stmt.close();
					 							data = bu_seq;
					 						}
					 						
					 						else if(i == 8) {	//BU_CONTACT_PERSON_CD 
					 							queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
									    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y'";
												stmt=conn.prepareStatement(queryString);
												
												String addr_flag = "";
												if(bu_seq.equals("1")) {								
													addr_flag = "P1";
												}else if(bu_seq.equals("2")){
													addr_flag = "P2";
												}else if(bu_seq.equals("3")){
													addr_flag = "P3";
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
					 						
					 						else if(i == 9) {	//TRANS_BU_UNIT
					 							queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_TRANS_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, company_cd);
													stmt.setString(2, cd);
													stmt.setString(3, agmt_no);
													stmt.setString(4, agmt_rev);
													stmt.setString(5, cont_no);
													stmt.setString(6, cont_rev);
													stmt.setString(7, cont_type);
													stmt.setString(8, agmt_type);
													rset = stmt.executeQuery();
													if (rset.next()) {
														trans_bu_seq = rset.getString(1);
													}
													rset.close();
													stmt.close();
												data = trans_bu_seq;
					 						}
					 						
					 						else if(i == 10) {	//INVOICE_TYPE
					 							inv_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = inv_type;
					 						}
					 						
					 						else if(i == 28) {
					 							fin_year = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		if (invseq.containsKey(fin_year)) {
									    			
									    			inv_seq_no=invseq.get(fin_year);
									    			inv_seq_no=inv_seq_no+1;
													invseq.put(fin_year,inv_seq_no);
													
												} else {
													inv_seq_no=1;
													invseq.put(fin_year,inv_seq_no);
													
												}
									    		data=inv_seq_no+"";
									    	}
					 						
					 						else if (i == 31) {
					 							freq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										    	
										    	queryString = "SELECT BILLING_FREQ FROM FMS_GTA_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
										    			+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
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
													bil_freq = rset.getString(1);
													if(bil_freq.equals("O")) {
														freq ="8";
													}
												}
												rset.close();
												stmt.close();
										    	
												data = freq ;
									    	}
					 						else if(i==41) {
					 							gross_amt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							sale_amt = Double.parseDouble(gross_amt);
					 							data = gross_amt;
					 						}
					 						else if(i == 42) {	//TAX_AMT
					 							tax = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							
					 							if(tax!=null && tax.contains("@")){
					 								tax = tax.replace("@", ",");
					 							}
					 							
					 							
					 							queryString = "SELECT TAX_STRUCT_DTL, TAX_STRUCT_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE "
					 									+ "COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = 'R' AND BU_UNIT = ? AND TAX_STRUCT_DTL = ?";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, company_cd);
												stmt.setString(2, cd);
												stmt.setString(3, bu_seq);
												stmt.setString(4, tax);
												rset = stmt.executeQuery();
												
												
												if (rset.next()) {
													tax = rset.getString(1);
													tax_struct_cd = rset.getString(2);
													tax_eff_dt = rset.getString(3);
													
													if (tax!=null) {
														if (tax.contains(",")) {
															tax = tax.split(" ")[1];
															tax = tax.substring(0, 1);
															tax = Integer.parseInt(tax) * 2 + "";
					//										System.out.println("tax:: "+tax);
														} else {
															tax = tax.split(" ")[1];
															tax = tax.substring(0, 2);
															tax = Integer.parseInt(tax) + "";
														}
														tax_amt = Double.parseDouble(tax);
														tax_amt = sale_amt * tax_amt / 100; 
													}else {
														tax_amt = 0;
													}
												}
												rset.close();
												stmt.close();
												
												data = tax_amt+"";
					 						}
					 						else if(i == 43) {	//TAX_STRUCT_CD
					 							data = tax_struct_cd;
					 						}
					 						
					 						else if(i == 44) {	//TAX_EFF_DT
					 							data = tax_eff_dt;
					 						}
					 						
					 						else if(i == 45) {	//INVOICE_AMT
//					 							invoice_amt = sale_amt + tax_amt;
					 							invoice_amt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = invoice_amt+"";
					 						}
					 						
					 						else if (i == 48) {	//NET_PAYABLE_AMT
					 							data = invoice_amt+"";
					 						}
					 						
					 						else if(i==67) {
					 							tds_amt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = tds_amt;
					 						}
					 						else if(i==68) {
					 							tds_ftr = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							if(tds_amt!=null) {
					 								tds_ftr1 = tds_ftr;
					 							}else {
					 								tds_ftr1=null;
					 							}
					 							data = tds_ftr1;
					 						}
					 						
					 						else if (i==69) {
						 						tds = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
						 						if(tds_amt!=null) {
					 							queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE "
					 									+ "DESCR = ? AND TAX_CATEGORY = 'S' AND PAY_RECV = 'P'";
					 							stmt = conn.prepareStatement(queryString);
												stmt.setString(1, tds);
												rset = stmt.executeQuery();
												
												if(rset.next()) {
													tds_cd = rset.getString(1);
											     	}
						 						}
						 						else {
					 								tds_cd = null;
					 							}
												rset.close();
												stmt.close();
												data = tds_cd;
					 						}
					 						else if(i==71) {
					 							String  tcd_tds1="";
					 							tcd_tds = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							if(tds_amt!=null) {
					 								tcd_tds1 =tcd_tds;
					 							}
					 							else {
					 								tcd_tds1=null;
					 							}
					 							data = tcd_tds1;
					 						}
					 						else {
					 							if(i == 32) {	//PERIOD_START_DT
					 	 							st_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data = st_dt;
					 	 						}
					 	 						
					 	 						else if(i == 33) {	//PERIOD_END_DT
					 	 							end_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data = end_dt;
					 	 						}
					 	 						else if(i==29)// INV_NO
					 	 						{
					 	 							inv_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data=inv_no;
					 	 						}
//					 	 						else if(i == 31) {
//					 	 							freq = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//					 	 							data = freq;
//					 	 						}
					
					 							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
					 							
					 						}	
					 						
					//				    	System.out.println(index+"-"+data);
					 						stmt1.setString(index++, data);		    	
					 					}
					 					
					 					queryString = "SELECT COUNTERPARTY_CD FROM FMS_GTA_SG_INV_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? "
					 							+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT = ? AND TRANS_BU_UNIT = ? AND INVOICE_TYPE = ? "
					 							+ "AND INVOICE_SEQ = ? AND FREQ = ? AND PERIOD_START_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') "
					 							+ "AND PERIOD_END_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND FINANCIAL_YEAR = ? ";
					 					stmt = conn.prepareStatement(queryString);
					 					stmt.setString(1, company_cd);
					 					stmt.setString(2, cd);
					 					stmt.setString(3, agmt_no);
					 					stmt.setString(4, agmt_rev);
					 					stmt.setString(5, cont_no);
					 					stmt.setString(6, cont_rev);
					 					stmt.setString(7, cont_type);
					 					stmt.setString(8, bu_seq);
					 					stmt.setString(9, trans_bu_seq);
					 					stmt.setString(10, inv_type);
					//                    stmt.setString(11, inv_no);
					 					stmt.setString(11, inv_seq_no+"");
					 					stmt.setString(12, freq);
					 					stmt.setString(13, st_dt);
					 					stmt.setString(14, end_dt);
					 					stmt.setString(15, fin_year);
					 					
					 					
					 					rset = stmt.executeQuery();
					 					
					 					if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !inv_type.equals("")) {
					 						//System.out.println(queryString1);
					// 							logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANS_BU_UNIT, BU_UNIT, FREQ, PERIOD_START_DT, PERIOD_END_DT, INVOICE_TYPE, INVOICE_SEQ,TIMESTAMP,", conn);
					 						logger.data(fname, (company_cd +"," + cd  + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + trans_bu_seq+"," + bu_seq + "," + freq + "," + st_dt + "," + end_dt + "," +inv_type +","+ inv_seq_no +","+inv_no+","), conn, "");
					 						
					 						stmt1.executeUpdate();
					 						stmt1.close();
					 						
					 						logger_count++;
					 					}
					 					else {
					 						stmt1.close();
					 						skipped_count++;     
					 						logger.data(fname, (company_cd +"," + cd  + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + trans_bu_seq+"," + bu_seq + "," + freq + "," + st_dt + "," + end_dt + "," +inv_type +","+ inv_seq_no +","+inv_no+","), conn, "E");
					 					
					 						if (invseq.containsKey(fin_year)) {
												inv_seq_no = invseq.get(fin_year);
												inv_seq_no=inv_seq_no-1;
												invseq.put(fin_year, inv_seq_no);
											}
					 					}
					 					
					 					rset.close();
					 					stmt.close();
					 				}
					 				br.close();
				}
 				
 				// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
 				
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
 	
 	
 	public void FMS_GTA_SG_INV_TAX_DTL() throws IOException, SQLException {

		function_nm="FMS_GTA_SG_INV_TAX_DTL()";
		try {
			
			
			System.out.println("<<START>><<FMS_GTA_SG_INV_TAX_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_SG_INV_TAX_DTL>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
			
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			
			columns = "COMPANY_CD,CONTRACT_TYPE,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
					+ "TAX_EFF_DT,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			
			queryString = "SELECT COMPANY_CD,CONTRACT_TYPE,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,"
					+ "TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss'),NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), NULL, "
					+ "NULL,GROSS_AMT"
					+ "	FROM FMS_GTA_SG_INV_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			queryString1 = "INSERT INTO FMS_GTA_SG_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
					+ "TAX_EFF_DT,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			
			logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
			
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
//								        else {
								            gross_amt = rset.getString(16);
//								            tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100);
								            tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100;
								            adv_amt = tax_amt + "";
								            adv=adv_amt;
//								        }
								    }
								    else if (desc.contains("on")) {

								        count_value = desc.split("%")[0];
								        count_value = count_value.split(" ")[1]; 
							            tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100;
								           
								    }
								}
								data = tax_amt + "";

							}
							
							else if(i == 10 && desc != null && desc.contains("on")) {

								data = adv;
							}
							
							//System.out.println("index-"+i+1+"-data-"+data);
							stmt1.setString(i+1,data);
								
							}
					
					
				//for data already exists..
				queryString5 = "SELECT TAX_AMT FROM FMS_GTA_SG_INV_TAX_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE = ?  AND INVOICE_TYPE = ? AND "
						+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, rset.getString(2));
				stmt5.setString(3, rset.getString(3));
				stmt5.setString(4, rset.getString(4));
				stmt5.setString(5, rset.getString(5));
				stmt5.setString(6, rset.getString(6));
				stmt5.setString(7, tax_code);
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
			
		
			System.out.println("<<END>><<FMS_GTA_SG_INV_TAX_DTL>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_SG_INV_TAX_DTL>>,", conn);
			
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
 	
 	
 	
 	
 //FMS_GTA_PG_INV_MST
public void FMS_GTA_PG_INV_MST() throws IOException, SQLException {
 		
 		function_nm="FMS_GTA_PG_INV_MST()";
 		try {
 			
 			table_name = "FMS_GTA_PG_INV_MST";
 			System.out.println("<<START>><<"+table_name+">>");
 			
 			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
 			
 			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
 			
 			DateUtil utilDate = new DateUtil();
			DataBean_GTA_Remittance bill_freq = new DataBean_GTA_Remittance();
 			
 			data = "";
 			logger_count = 0;   
 			skipped_count = 0;   
 			total_count = 0; 
 			String map_id = "",bu_seq="",bu_cont="",trans_bu_seq="",fin_year="",st_dt="",end_dt="",tax="",tax_struct_cd="",tax_eff_dt= "",
 					inv_type="",freq="",inv_no="",gross_amt="",bil_freq="";
 			String tds="",tds_date="",tds_cd="",tds_amt="",tds_ftr="",tds_ftr1="",tcs_td="";
 			queryString1 = "INSERT INTO FMS_GTA_PG_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,TRANS_BU_UNIT,INVOICE_TYPE,"
 					+ "ALLOC_QTY,DEFICIENCY_QTY,NEG_IMB_QTY,POS_IMB_QTY,UNAUTH_OVERRUN_QTY,PARKING_QTY,SIP_PAY_RATE,TRANSPORT_RATE,NEGETIVE_IMB_RATE,POSITIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,PARKING_RATE,"
					+ "TRANSMISSION_AMT,NEG_IMB_AMT,POS_IMB_AMT,UNAUTH_OVERRUN_AMT,PARKING_AMT,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,RATE_UNIT,SALE_AMT,EXCHG_RATE_CD,"
					+ "EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "INV_STATUS,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,"
					+ "FINANCIAL_YEAR,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,DEFICIENCY_AMT,SIP_PAY_FREQ,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,TCS_TDS,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SYS_INV_NO,INV_COMPONENT,FIN_SYS)"
					+"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
 			
 			stmt1 = conn.prepareStatement(queryString1);
			Map<String, Integer> invseq = new HashMap<String, Integer>();
 			file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_PG_INV_MST_"+start_end_dt+".csv");
 			
 			if(file1.exists()) {
 				
 				String fileName = migration_setup_dir + "EXPORT/FMS_GTA_PG_INV_MST_"+start_end_dt+".csv";
 				
 				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
					 				String line = br.readLine();
					 				
					 				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANS_BU_UNIT, BU_UNIT, FREQ, PERIOD_START_DT, PERIOD_END_DT, INVOICE_TYPE, INVOICE_SEQ, INV_NO ,TIMESTAMP,", conn);
					 				int inv_seq_no = 1;
					 				while ((line = br.readLine()) != null) {
					 					total_count++;
					 					abbr = "";
					 					cd = "0";
					 					data = null;
					 					map_id="";bu_seq="";bu_cont="";trans_bu_seq="";fin_year="";st_dt="";end_dt="";tax="";tax_struct_cd="";
					 					tax_eff_dt= "";inv_type ="";inv_no="";
					 					index = 1;
					 					stmt1 = conn.prepareStatement(queryString1);
					 					agmt_no = "";
					 					agmt_rev = "";
					 					cont_no = "";
					 					cont_rev = "";
					 					cont_type = "";
					 					ent_pt="";
					 					exit_pt="";
					 					
					 					
					 					double sale_amt=0; 
					 					double tax_amt=0; 
					 					String invoice_amt=""; 
					 					
					//					row = rowIterator.next();
					//				    cellIterator = row.cellIterator();
					//				    while (cellIterator.hasNext()) {
					 					for (int i = 0; i < line.split(",").length; i++)
					 					{	
					//				    	cell = cellIterator.next();
					 						data = null;
					 						
					 						if (i == 0) {	//COMPANY_CD
					 							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!abbr.equals("NULL")) {
					//								abbr = abbr.substring(1, abbr.length() - 1);
					//							}
					 							
					 							data = company_cd;
					 						}
					 						
					 						else if (i == 1) {	//COUNTERPARTY_CD
					 							map_id = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//				    		if(map_id!= null) {
					//				    		map_id = map_id.substring(1,map_id.length()-1);
					//				    		}
					 							
					 							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, abbr.toUpperCase());
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
					 						
					 						else if (i == 2) {	//AGMT_NO
					 							
					 							queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, ENTRY_PT_MAPPING_ID, AGMT_TYPE FROM FMS_GTA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE IN('C','K','R') ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, map_id);
					 							stmt.setString(2, cd);
					// 							stmt.setString(3, cont_type);
					 							rset = stmt.executeQuery();
					 							if (rset.next()) {
					 								cont_type = rset.getString(1);
					 								agmt_no = rset.getString(2);
					 								agmt_rev = rset.getString(3);
					 								cont_no = rset.getString(4);
					 								cont_rev = rset.getString(5);
					 								ent_pt = rset.getString(6);
					 								agmt_type = rset.getString(7);
					 							}
					 							rset.close();
					 							stmt.close();
					 							data = agmt_no;
					 						}
					 						
					 						else if (i == 3) { //AGMT_REV
					 							data = agmt_rev;
					 						}
					 						
					 						else if (i == 4) { //CONT_NO
					 							data = cont_no;
					 						}
					 						
					 						else if (i == 5) { //CONT_REV
					 							data = cont_rev;
					 						}
					 						
					 						else if (i == 6) { //CONTRACT_TYPE
					 							data = cont_type;
					 						}
					 						
					 						else if(i == 7) {	//BU_UNIT
					 								queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
					 								stmt = conn.prepareStatement(queryString);
					 								stmt.setString(1, company_cd);
					 								stmt.setString(2, cd);
					 								stmt.setString(3, agmt_no);
					 								stmt.setString(4, agmt_rev);
					 								stmt.setString(5, cont_no);
					 								stmt.setString(6, cont_rev);
					 								stmt.setString(7, cont_type);
					 								stmt.setString(8, agmt_type);
					 								rset = stmt.executeQuery();
					 								if (rset.next()) {
					 									bu_seq = rset.getString(1);
					 								}
					 								rset.close();
					 								stmt.close();
					 							data = bu_seq;
					 						}
					 						
					 						else if(i == 8) {	//BU_CONTACT_PERSON_CD 
					 							queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
									    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y'";
												stmt=conn.prepareStatement(queryString);
												
												String addr_flag = "";
												if(bu_seq.equals("1")) {								
													addr_flag = "P1";
												}else if(bu_seq.equals("2")){
													addr_flag = "P2";
												}else if(bu_seq.equals("3")){
													addr_flag = "P3";
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
					 						
					 						else if(i == 9) {	//TRANS_BU_UNIT
					 							queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_TRANS_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, company_cd);
													stmt.setString(2, cd);
													stmt.setString(3, agmt_no);
													stmt.setString(4, agmt_rev);
													stmt.setString(5, cont_no);
													stmt.setString(6, cont_rev);
													stmt.setString(7, cont_type);
													stmt.setString(8, agmt_type);
													rset = stmt.executeQuery();
													if (rset.next()) {
														trans_bu_seq = rset.getString(1);
													}
													rset.close();
													stmt.close();
												data = trans_bu_seq;
					 						}
					 						
					 						else if(i == 10) {	//INVOICE_TYPE
					 							inv_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = inv_type;
					 						}
					 						
					 						else if(i == 28) {
					 							fin_year = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		if (invseq.containsKey(fin_year)) {
									    			
									    			inv_seq_no=invseq.get(fin_year);
									    			inv_seq_no=inv_seq_no+1;
													invseq.put(fin_year,inv_seq_no);
													
												} else {
													inv_seq_no=1;
													invseq.put(fin_year,inv_seq_no);
													
												}
									    		data=inv_seq_no+"";
									    	}
					 						else if (i == 31) {

					 							freq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										    	
										    	queryString = "SELECT BILLING_FREQ FROM FMS_GTA_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
										    			+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
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
													bil_freq = rset.getString(1);
													if(bil_freq.equals("O")) {
														freq ="8";
													}
												}
												rset.close();
												stmt.close();
										    	
												data = freq ;
									    	}
					 						
//					 						
					 						else if(i==41) {
					 							gross_amt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							sale_amt = Double.parseDouble(gross_amt);
					 							data = gross_amt;
					 						}
					 						else if(i == 42) {	//TAX_AMT
					 							tax = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							
					 							if(tax!=null && tax.contains("@")){
					 								tax = tax.replace("@", ",");
					 							}
					 							
					 							
					 							queryString = "SELECT TAX_STRUCT_DTL, TAX_STRUCT_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE "
					 									+ "COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = 'R' AND BU_UNIT = ? AND TAX_STRUCT_DTL = ?";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, company_cd);
												stmt.setString(2, cd);
												stmt.setString(3, bu_seq);
												stmt.setString(4, tax);
												rset = stmt.executeQuery();
												
					
												if (rset.next()) {
													tax = rset.getString(1);
													tax_struct_cd = rset.getString(2);
													tax_eff_dt = rset.getString(3);
													
													if (tax!=null) {
														if (tax.contains(",")) {
															tax = tax.split(" ")[1];
															tax = tax.substring(0, 1);
															tax = Integer.parseInt(tax) * 2 + "";
					//										System.out.println("tax:: "+tax);
														} else {
															tax = tax.split(" ")[1];
															tax = tax.substring(0, 2);
															tax = Integer.parseInt(tax) + "";
														}
														tax_amt = Double.parseDouble(tax);
														tax_amt = sale_amt * tax_amt / 100; 
													}else {
														tax_amt = 0;
													}
												}
												rset.close();
												stmt.close();
												
												data = tax_amt+"";
					 						}
					 						else if(i == 43) {	//TAX_STRUCT_CD
					 							data = tax_struct_cd;
					 						}
					 						
					 						else if(i == 44) {	//TAX_EFF_DT
					 							data = tax_eff_dt;
					 						}
					 						
					 						else if(i == 45) {	//INVOICE_AMT
//					 							invoice_amt = sale_amt + tax_amt;
					 							invoice_amt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = invoice_amt+"";
					 						}
					 						
					 						else if (i == 48) {	//NET_PAYABLE_AMT
					 							data = invoice_amt+"";
					 						}
					 						
//					 						else if (i==69) {
//						 						tds = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//					 							queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE "
//					 									+ "DESCR = ? AND TAX_CATEGORY = 'S' AND PAY_RECV = 'P'";
//					 							stmt = conn.prepareStatement(queryString);
//												stmt.setString(1, tds);
//												rset = stmt.executeQuery();
//												
//												if(rset.next()) {
//													tds_cd = rset.getString(1);
//												}
//												rset.close();
//												stmt.close();
//												data = tds_cd;
//					 						}
					 						

					 						else if(i==67) {
					 							tds_amt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = tds_amt;
					 						}
					 						else if(i==68) {
					 							tds_ftr = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							if(tds_amt!=null) {
					 								tds_ftr1 = tds_ftr;
					 							}else {
					 								tds_ftr1=null;
					 							}
					 							data = tds_ftr1;
					 						}
					 						
					 						else if (i==69) {
						 						tds = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
						 						if(tds_amt!=null) {
					 							queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE "
					 									+ "DESCR = ? AND TAX_CATEGORY = 'S' AND PAY_RECV = 'P'";
					 							stmt = conn.prepareStatement(queryString);
												stmt.setString(1, tds);
												rset = stmt.executeQuery();
												
												if(rset.next()) {
													tds_cd = rset.getString(1);
											     	}
						 						}
						 						else {
					 								tds_cd = null;
					 							}
												rset.close();
												stmt.close();
												data = tds_cd;
					 						}
					 						else if(i==71) {
					 							String  tcd_tds1="";
					 							tcs_td = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							if(tds_amt!=null) {
					 								tcd_tds1 =tcs_td;
					 							}
					 							else {
					 								tcd_tds1=null;
					 							}
					 							data = tcd_tds1;
					 						}
					 						
					 						else {
					 							if(i == 32) {	//PERIOD_START_DT
					 	 							st_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data = st_dt;
					 	 						}
					 	 						
					 	 						else if(i == 33) {	//PERIOD_END_DT
					 	 							end_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data = end_dt;
					 	 						}
					 	 						else if(i==29)// INV_NO
					 	 						{
					 	 							inv_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data=inv_no;
					 	 						}
//					 	 						else if(i == 31) {
//					 	 							freq = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//					 	 							data = freq;
//					 	 						}
					
					 							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
					 							
					 						}	
					 						
					//				    	System.out.println(index+"-"+data);
					 						stmt1.setString(index++, data);		    	
					 					}
					 					
					 					queryString = "SELECT COUNTERPARTY_CD FROM FMS_GTA_PG_INV_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? "
					 							+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT = ? AND TRANS_BU_UNIT = ? AND INVOICE_TYPE = ? "
					 							+ "AND INVOICE_SEQ = ? AND FREQ = ? AND PERIOD_START_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') "
					 							+ "AND PERIOD_END_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND FINANCIAL_YEAR = ? ";
					 					stmt = conn.prepareStatement(queryString);
					 					stmt.setString(1, company_cd);
					 					stmt.setString(2, cd);
					 					stmt.setString(3, agmt_no);
					 					stmt.setString(4, agmt_rev);
					 					stmt.setString(5, cont_no);
					 					stmt.setString(6, cont_rev);
					 					stmt.setString(7, cont_type);
					 					stmt.setString(8, bu_seq);
					 					stmt.setString(9, trans_bu_seq);
					 					stmt.setString(10, inv_type);
					//                    stmt.setString(11, inv_no);
					 					stmt.setString(11, inv_seq_no+"");
					 					stmt.setString(12, freq);
					 					stmt.setString(13, st_dt);
					 					stmt.setString(14, end_dt);
					 					stmt.setString(15, fin_year);
					 					
					 					
					 					rset = stmt.executeQuery();
					 					
					 					if (!rset.next() && !cd.equals("") && !agmt_no.equals("")  && !inv_type.equals("")) {
					 						//System.out.println(queryString1);
					// 							logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANS_BU_UNIT, BU_UNIT, FREQ, PERIOD_START_DT, PERIOD_END_DT, INVOICE_TYPE, INVOICE_SEQ,TIMESTAMP,", conn);
					 						logger.data(fname, (company_cd +"," + cd  + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + trans_bu_seq+"," + bu_seq + "," + freq + "," + st_dt + "," + end_dt + "," +inv_type +","+ inv_seq_no +","+inv_no+","), conn, "");
					 						
					 						stmt1.executeUpdate();
					 						stmt1.close();
					 						
					 						logger_count++;
					 					}
					 					else {
					 						stmt1.close();
					 						skipped_count++;     
					 						logger.data(fname, (company_cd +"," + cd  + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + trans_bu_seq+"," + bu_seq + "," + freq + "," + st_dt + "," + end_dt + "," +inv_type +","+ inv_seq_no +","+inv_no+","), conn, "E");
					 					
					 						if (invseq.containsKey(fin_year)) {
												inv_seq_no = invseq.get(fin_year);
												inv_seq_no=inv_seq_no-1;
												invseq.put(fin_year, inv_seq_no);
											}
					 					}
					 					
					 					rset.close();
					 					stmt.close();
					 				}
					 				br.close();
				}
 				
 				// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
 				
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
 	
public void FMS_GTA_PG_INV_TAX_DTL() throws IOException, SQLException {

	function_nm="FMS_GTA_PG_INV_TAX_DTL()";
	try {
		
		
		System.out.println("<<START>><<FMS_GTA_PG_INV_TAX_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_PG_INV_TAX_DTL>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,CONTRACT_TYPE,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
				+ "TAX_EFF_DT,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		queryString = "SELECT COMPANY_CD,CONTRACT_TYPE,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,"
				+ "TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss'),NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), NULL, "
				+ "NULL,GROSS_AMT"
				+ "	FROM FMS_GTA_PG_INV_MST WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_GTA_PG_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
				+ "TAX_EFF_DT,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
		
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
							            tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100;
							            adv_amt = tax_amt + "";
							            adv=adv_amt;
//							        }
							    }
							    else if (desc.contains("on")) {

							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
//							        tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100);
							        tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100;

							           
							    }
							}
							data = tax_amt + "";

						}
						
						else if(i == 10 && desc != null && desc.contains("on")) {

							data = adv;
						}
						
						//System.out.println("index-"+i+1+"-data-"+data);
						stmt1.setString(i+1,data);
							
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_GTA_PG_INV_TAX_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE = ?  AND INVOICE_TYPE = ? AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(5));
			stmt5.setString(6, rset.getString(6));
			stmt5.setString(7, tax_code);
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
		
	
		System.out.println("<<END>><<FMS_GTA_PG_INV_TAX_DTL>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_GTA_PG_INV_TAX_DTL>>,", conn);
		
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
 	

	//FMS_GTA_FFLOW_INV_MST
	public void FMS_GTA_FFLOW_INV_MST() throws IOException, SQLException {
		
		function_nm="FMS_GTA_FFLOW_INV_MST()";
		try {
			
			table_name = "FMS_GTA_FFLOW_INV_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String map_id = "",bu_seq="",bu_cont="",trans_bu_seq="",fin_year="",st_dt="",end_dt="",tax="",tax_struct_cd="",tax_eff_dt= "";
					
			String tds="",inv_type="",freq="",inv_no="",gross_amt="",tds_cd="",tds_date="",tds_amt="",tcs_tds="",tcs_tds1="",trans_bu="";
			queryString1 = "INSERT INTO FMS_GTA_FFLOW_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,ADDR_FLAG,INVOICE_SEQ,INVOICE_NO,INVOICE_REF,"
						 + "INVOICE_DT,INVOICE_CATEGORY,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,INVOICE_TYPE,LINKED_INVOICE,NUM_LINE,NOTE,GROSS_AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,"
						 + "GROSS_AMT_INR,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,"
						 + "APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,FINANCIAL_YEAR,MODIFY_BY,MODIFY_DT,OTHER_INV_STR,AMT_WORD,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,TDS_AMT,TCS_TDS,TDS_STRUCT_CD,TDS_EFF_DT,"
						 + "TCS_AMT,TCS_STRUCT_CD,TCS_EFF_DT,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_EXCHNG_RATE,TCS_CERT_FLAG,ALLOC_QTY,SUB_INV_TYPE,FIN_SYS)"
						 + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
						 + "?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
						 + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
						 + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?) ";
				
			
				stmt1 = conn.prepareStatement(queryString1);
				Map<String, Integer> invseq = new HashMap<String, Integer>();
				file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_FFLOW_INV_MST_"+start_end_dt+".csv");
			
				if(file1.exists()) {
				
					String fileName = migration_setup_dir + "EXPORT/FMS_GTA_FFLOW_INV_MST_"+start_end_dt+".csv";
				
					try (
							BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					 				String line = br.readLine();
					 				
					 				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANS_BU_UNIT, BU_UNIT, FREQ, PERIOD_START_DT, PERIOD_END_DT, INVOICE_TYPE, INVOICE_SEQ, INV_NO ,TIMESTAMP,", conn);
					 				int inv_seq_no = 1;
					 				while ((line = br.readLine()) != null) {
					 					total_count++;
					 					abbr = "";
					 					cd = "0";
					 					data = null;
					 					map_id="";bu_seq="";bu_cont="";trans_bu_seq="";fin_year="";st_dt="";end_dt="";tax="";tax_struct_cd="";
					 					tax_eff_dt= "";inv_type ="";inv_no="";
					 					index = 1;
					 					stmt1 = conn.prepareStatement(queryString1);
					 					agmt_no = "";
					 					agmt_rev = "";
					 					cont_no = "";
					 					cont_rev = "";
					 					cont_type = "";
					 					ent_pt="";
					 					exit_pt="";
					 					
					 					
					 					double sale_amt=0; 
					 					double tax_amt=0; 
					 					String invoice_amt=""; 
					 					

					 					for (int i = 0; i < line.split(",").length; i++)
					 					{	
					 						data = null;
					 						
					 						if (i == 0) {	//COMPANY_CD
					 							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					 							data = company_cd;
					 						}
					 						
					 						else if (i == 1) {	//COUNTERPARTY_CD
					 							map_id = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
	
					 							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, abbr.toUpperCase());
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
					 						
					 						else if (i == 2) {	//AGMT_NO
					 							
					 							queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, ENTRY_PT_MAPPING_ID, AGMT_TYPE FROM FMS_GTA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE IN('C','K','R') ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, map_id);
					 							stmt.setString(2, cd);
					// 							stmt.setString(3, cont_type);
					 							rset = stmt.executeQuery();
					 							if (rset.next()) {
					 								cont_type = rset.getString(1);
					 								agmt_no = rset.getString(2);
					 								agmt_rev = rset.getString(3);
					 								cont_no = rset.getString(4);
					 								cont_rev = rset.getString(5);
					 								ent_pt = rset.getString(6);
					 								agmt_type = rset.getString(7);
					 							}
					 							rset.close();
					 							stmt.close();
					 							data = agmt_no;
					 						}
					 						
					 						else if (i == 3) { //AGMT_REV
					 							data = agmt_rev;
					 						}
					 						
					 						else if (i == 4) { //CONT_NO
					 							data = cont_no;
					 						}
					 						
					 						else if (i == 5) { //CONT_REV
					 							data = cont_rev;
					 						}
					 						
					 						else if (i == 6) { //CONTRACT_TYPE
					 							data = cont_type;
					 						}
					 						
					 						else if(i == 7) {	//BU_UNIT
					 								queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
					 								stmt = conn.prepareStatement(queryString);
					 								stmt.setString(1, company_cd);
					 								stmt.setString(2, cd);
					 								stmt.setString(3, agmt_no);
					 								stmt.setString(4, agmt_rev);
					 								stmt.setString(5, cont_no);
					 								stmt.setString(6, cont_rev);
					 								stmt.setString(7, cont_type);
					 								stmt.setString(8, agmt_type);
					 								rset = stmt.executeQuery();
					 								if (rset.next()) {
					 									bu_seq = rset.getString(1);
					 								}
					 								rset.close();
					 								stmt.close();
					 							data = bu_seq;
					 						}
					 						
					 						else if(i == 8) {	//BU_CONTACT_PERSON_CD 
					 							queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
									    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y'";
												stmt=conn.prepareStatement(queryString);
												
												String addr_flag = "";
												if(bu_seq.equals("1")) {								
													addr_flag = "P1";
												}else if(bu_seq.equals("2")){
													addr_flag = "P2";
												}else if(bu_seq.equals("3")){
													addr_flag = "P3";
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
					 						
					 						else if(i == 9) {	//ADDR_FLAG
					 							
					 							queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_TRANS_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
				 								stmt = conn.prepareStatement(queryString);
				 								stmt.setString(1, company_cd);
				 								stmt.setString(2, cd);
				 								stmt.setString(3, agmt_no);
				 								stmt.setString(4, agmt_rev);
				 								stmt.setString(5, cont_no);
				 								stmt.setString(6, cont_rev);
				 								stmt.setString(7, cont_type);
				 								stmt.setString(8, agmt_type);
				 								rset = stmt.executeQuery();
				 								if (rset.next()) {
				 									trans_bu = rset.getString(1);
				 								}
				 								rset.close();
				 								stmt.close();
					 							
					 							if(trans_bu.equals("1")) {
					 								data = "B1";
					 							}else if(trans_bu.equals("2")) {
					 								data = "B2";
					 							}
					 							else if(trans_bu.equals("3")) {
					 								data = "B3";
					 							}
											
					 						}
					 						
					 						
					 						else if(i == 10) {
					 							fin_year = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		if (invseq.containsKey(fin_year)) {
									    			
									    			inv_seq_no=invseq.get(fin_year);
									    			inv_seq_no=inv_seq_no+1;
													invseq.put(fin_year,inv_seq_no);
													
												} else {
													inv_seq_no=1;
													invseq.put(fin_year,inv_seq_no);
													
												}
									    		data=inv_seq_no+"";
									    	}
					 						else if(i == 19) {	//INVOICE_TYPE
					 							inv_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = inv_type;
					 						}
//					 						else if (i == 31) {
//
//										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//										    	freq = data;
//										    	
//									    	}
					 						else if(i==28) {
					 							gross_amt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							sale_amt = Double.parseDouble(gross_amt);
					 							data = gross_amt;
					 						}
					 						else if(i == 29) {	//TAX_AMT
					 							tax = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							
					 							if(tax!=null && tax.contains("@")){
					 								tax = tax.replace("@", ",");
					 							}
					 							
					 							
					 							queryString = "SELECT TAX_STRUCT_DTL, TAX_STRUCT_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE "
					 									+ "COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY = 'R' AND BU_UNIT = ? AND TAX_STRUCT_DTL = ?";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, company_cd);
												stmt.setString(2, cd);
												stmt.setString(3, bu_seq);
												stmt.setString(4, tax);
												rset = stmt.executeQuery();
												
					
												if (rset.next()) {
													tax = rset.getString(1);
													tax_struct_cd = rset.getString(2);
													tax_eff_dt = rset.getString(3);
													
													if (tax!=null) {
														if (tax.contains(",")) {
															tax = tax.split(" ")[1];
															tax = tax.substring(0, 1);
															tax = Integer.parseInt(tax) * 2 + "";
					//										System.out.println("tax:: "+tax);
														} else {
															tax = tax.split(" ")[1];
															tax = tax.substring(0, 2);
															tax = Integer.parseInt(tax) + "";
														}
														tax_amt = Double.parseDouble(tax);
														tax_amt = sale_amt * tax_amt / 100; 
													}else {
														tax_amt = 0;
													}
												}
												rset.close();
												stmt.close();
												
												data = tax_amt+"";
					 						}
					 						else if(i == 30) {	//TAX_STRUCT_CD
					 							data = tax_struct_cd;
					 						}
					 						
					 						else if(i == 31) {	//TAX_EFF_DT
					 							data = tax_eff_dt;
					 						}
					 						
					 						else if(i == 32) {	//INVOICE_AMT
					 							invoice_amt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = invoice_amt+"";
					 						}
					 						
					 						else if (i == 35) {	//NET_PAYABLE_AMT
					 							data = invoice_amt+"";
					 						}
					 						
					 						else if(i==56) {
					 							tds_amt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = tds_amt;
					 						}
					 						else if(i==57) {
					 							tcs_tds = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							if(tds_amt!=null) {
					 								tcs_tds1 = tcs_tds;
					 							}else {
					 								tcs_tds1 = null;
					 							}
					 								data = tcs_tds1;
					 						}
					 						else if (i==58) {
					 							tds = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							if(tds_amt!=null) {
					 							queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE "
					 									+ "DESCR = ? AND TAX_CATEGORY = 'S' AND PAY_RECV = 'P'";
					 							stmt = conn.prepareStatement(queryString);
												stmt.setString(1, tds);
												rset = stmt.executeQuery();
												
												if(rset.next()) {
													tds_cd = rset.getString(1);
												   }
					 							}
					 							else {
					 								tds_cd = null;
					 							}
					 							rset.close();
												stmt.close();
												data = tds_cd;
					 						}
					 						
					 						else {
					 							if(i == 16) {	//PERIOD_START_DT
					 	 							st_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data = st_dt;
					 	 						}
					 	 						
					 	 						else if(i == 17) {	//PERIOD_END_DT
					 	 							end_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data = end_dt;
					 	 						}
					 	 						else if(i == 11)// INV_NO
					 	 						{
					 	 							inv_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data = inv_no;
					 	 						}
					 	 						else if(i == 15) {
					 	 							freq = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data = freq;
					 	 						}
					
					 							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
					 							
					 						}	
					 						
//									    	System.out.println(index+"-"+data);
					 						stmt1.setString(index++, data);		    	
					 					}
					 					
					 					queryString = "SELECT COUNTERPARTY_CD FROM FMS_GTA_FFLOW_INV_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? "
					 							+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT = ? AND INVOICE_TYPE = ? "
					 							+ "AND INVOICE_SEQ = ? AND FREQ = ? AND PERIOD_START_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') "
					 							+ "AND PERIOD_END_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND FINANCIAL_YEAR = ? ";
					 					stmt = conn.prepareStatement(queryString);
					 					stmt.setString(1, company_cd);
					 					stmt.setString(2, cd);
					 					stmt.setString(3, agmt_no);
					 					stmt.setString(4, agmt_rev);
					 					stmt.setString(5, cont_no);
					 					stmt.setString(6, cont_rev);
					 					stmt.setString(7, cont_type);
					 					stmt.setString(8, bu_seq);
					 					stmt.setString(9, inv_type);
					//                    stmt.setString(11, inv_no);
					 					stmt.setString(10, inv_seq_no+"");
					 					stmt.setString(11, freq);
					 					stmt.setString(12, st_dt);
					 					stmt.setString(13, end_dt);
					 					stmt.setString(14, fin_year);
					 					
					 					
					 					rset = stmt.executeQuery();
					 					
					 					if (!rset.next() && !cd.equals("") && !agmt_no.equals("")  && !inv_type.equals("")) {
					 						//System.out.println(queryString1);
					// 							logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANS_BU_UNIT, BU_UNIT, FREQ, PERIOD_START_DT, PERIOD_END_DT, INVOICE_TYPE, INVOICE_SEQ,TIMESTAMP,", conn);
					 						logger.data(fname, (company_cd +"," + cd  + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + trans_bu_seq+"," + bu_seq + "," + freq + "," + st_dt + "," + end_dt + "," +inv_type +","+ inv_seq_no +","+inv_no+","), conn, "");
					 						
					 						stmt1.executeUpdate();
					 						stmt1.close();
					 						
					 						logger_count++;
					 					}
					 					else {
					 						stmt1.close();
					 						skipped_count++;     
					 						logger.data(fname, (company_cd +"," + cd  + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + trans_bu_seq+"," + bu_seq + "," + freq + "," + st_dt + "," + end_dt + "," +inv_type +","+ inv_seq_no +","+inv_no+","), conn, "E");
					 					
					 						if (invseq.containsKey(fin_year)) {
												inv_seq_no = invseq.get(fin_year);
												inv_seq_no=inv_seq_no-1;
												invseq.put(fin_year, inv_seq_no);
											}
					 					}
					 					
					 					rset.close();
					 					stmt.close();
					 				}
					 				br.close();
				}
				
				// Below block of code is for unique SEIPL data
				//				rowIterator = sheet.iterator();
				
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

//FMS_GTA_FFLOW_INV_TAX_DTL
	public void FMS_GTA_FFLOW_INV_TAX_DTL() throws IOException, SQLException {

		function_nm="FMS_GTA_FFLOW_INV_TAX_DTL()";
		try {
			
			
			System.out.println("<<START>><<FMS_GTA_FFLOW_INV_TAX_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_FFLOW_INV_TAX_DTL>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
			
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			
			columns = "COMPANY_CD,CONTRACT_TYPE,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
					+ "TAX_EFF_DT,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			
			queryString = "SELECT COMPANY_CD,CONTRACT_TYPE,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,"
					+ "TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss'),NULL,TAX_AMT,GROSS_AMT_INR,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), NULL, "
					+ "NULL,GROSS_AMT_INR"
					+ "	FROM FMS_GTA_FFLOW_INV_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			queryString1 = "INSERT INTO FMS_GTA_FFLOW_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,"
					+ "TAX_EFF_DT,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			
			logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
			
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
//								        else {
								            gross_amt = rset.getString(16);
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
							
							else if(i == 10 && desc != null && desc.contains("on")) {

								data = adv;
							}
							
							//System.out.println("index-"+i+1+"-data-"+data);
							stmt1.setString(i+1,data);
								
							}
					
					
				//for data already exists..
				queryString5 = "SELECT TAX_AMT FROM FMS_GTA_FFLOW_INV_TAX_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE = ?  AND INVOICE_TYPE = ? AND "
						+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, rset.getString(2));
				stmt5.setString(3, rset.getString(3));
				stmt5.setString(4, rset.getString(4));
				stmt5.setString(5, rset.getString(5));
				stmt5.setString(6, rset.getString(6));
				stmt5.setString(7, tax_code);
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
			
		
			System.out.println("<<END>><<FMS_GTA_FFLOW_INV_TAX_DTL>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_FFLOW_INV_TAX_DTL>>,", conn);
			
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
	
	
	//FMS_GTA_FFLOW_INV_DTL
	public void FMS_GTA_FFLOW_INV_DTL() throws IOException, SQLException {
			
			function_nm="FMS_GTA_FFLOW_INV_DTL()";
			try {
				
				table_name = "FMS_GTA_FFLOW_INV_DTL";
				System.out.println("<<START>><<"+table_name+">>");
				
				logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				DateUtil utilDate = new DateUtil();
			DataBean_GTA_Remittance bill_freq = new DataBean_GTA_Remittance();
				
				data = "";
				logger_count = 0;   
				skipped_count = 0;   
				total_count = 0; 
				String map_id = "",bu_seq="",bu_cont="",fin_year="",tax="",desc="";
				String		inv_type="",freq="",inv_no="",cnt="",inv_seq="",adr_flg="",bill_no="";
				queryString1 = "INSERT INTO FMS_GTA_FFLOW_INV_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,ADDR_FLAG,"
					+ "INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,UNIT,QTY,RATE,AMOUNT,ENT_BY,ENT_DT,FINANCIAL_YEAR,INVOICE_TYPE)"
					+"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?) ";
				
				stmt1 = conn.prepareStatement(queryString1);
			Map<String, Integer> invseq = new HashMap<String, Integer>();
				file1 = new File(migration_setup_dir + "EXPORT/FMS_GTA_FFLOW_INV_DTL_"+start_end_dt+".csv");
				
				if(file1.exists()) {
					
					String fileName = migration_setup_dir + "EXPORT/FMS_GTA_FFLOW_INV_DTL_"+start_end_dt+".csv";
					
					try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
					 				String line = br.readLine();
					 				
					 				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,INVOICE_TYPE, INVOICE_SEQ, INV_NO ,LINE_NO,LINE_DESC,TIMESTAMP,", conn);
					 				int inv_seq_no = 1;
					 				inv_seq="";
					 				while ((line = br.readLine()) != null) {
					 					total_count++;
					 					abbr = "";
					 					cd = "0";
					 					data = null;
					 					map_id="";bu_seq="";bu_cont="";fin_year="";tax="";
					 					inv_type ="";inv_no="";
					 					index = 1;
					 					stmt1 = conn.prepareStatement(queryString1);
					 					agmt_no = "";
					 					agmt_rev = "";
					 					cont_no = "";
					 					cont_rev = "";
					 					cont_type = "";
					 					ent_pt="";
					 					exit_pt="";
					 					
					 					for (int i = 0; i < line.split(",").length; i++)
					 					{	
					//				    	cell = cellIterator.next();
					 						data = null;
					 						
					 						if (i == 0) {	//COMPANY_CD
					 							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					 							data = company_cd;
					 						}
					 						
					 						else if (i == 1) {	//COUNTERPARTY_CD
					 							map_id = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE UPPER(COUNTERPARTY_ABBR) = ? ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, abbr.toUpperCase());
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
					 						
                                               else if (i == 2) {	//AGMT_NO
					 							bill_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV,ADDR_FLAG,INVOICE_SEQ,INVOICE_TYPE FROM FMS_GTA_FFLOW_INV_MST WHERE INVOICE_NO = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE IN('C','K','R') AND COMPANY_CD = '2' ";
					 							stmt = conn.prepareStatement(queryString);
					 							stmt.setString(1, bill_no);
					 							stmt.setString(2, cd);
					// 							stmt.setString(3, cont_type);
					 							rset = stmt.executeQuery();
					 							if (rset.next()) {
					 								cont_type = rset.getString(1);
					 								agmt_no = rset.getString(2);
					 								agmt_rev = rset.getString(3);
					 								cont_no = rset.getString(4);
					 								cont_rev = rset.getString(5);
					 								adr_flg = rset.getString(6);
					 								inv_seq = rset.getString(7);
					 								inv_type=rset.getString(8);
					 							}
					 							rset.close();
					 							stmt.close();
					 							data = agmt_no;
					 						}
					 						
					 						else if (i == 3) { //AGMT_REV
					 							data = agmt_rev;
					 						}
					 						
					 						else if (i == 4) { //CONT_NO
					 							data = cont_no;
					 						}
					 						
					 						else if (i == 5) { //CONT_REV
					 							data = cont_rev;
					 						}
					 						
					 						else if (i == 6) { //CONTRACT_TYPE
					 							data = cont_type;
					 						}
					 						
					 						else if(i == 7) {	//BU_UNIT
					 							if(cont_type.equals("C") || cont_type.equals("R")) {
					 								agmt_type = "G";
					 							}
					 							else {
					 								agmt_type = "P";
					 							}
					 							queryString = "SELECT PLANT_SEQ_NO FROM FMS_GTA_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ? ";
					 								stmt = conn.prepareStatement(queryString);
					 								stmt.setString(1, company_cd);
					 								stmt.setString(2, cd);
					 								stmt.setString(3, agmt_no);
					 								stmt.setString(4, agmt_rev);
					 								stmt.setString(5, cont_no);
					 								stmt.setString(6, cont_rev);
					 								stmt.setString(7, cont_type);
					 								stmt.setString(8, agmt_type);
					 								rset = stmt.executeQuery();
					 								if (rset.next()) {
					 									bu_seq = rset.getString(1);
					 								}
					 								rset.close();
					 								stmt.close();
					 							data = bu_seq;
					 						}
					 						
					 						else if(i == 8) {	//BU_CONTACT_PERSON_CD 
					 							queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
									    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y'";
												stmt=conn.prepareStatement(queryString);
												
												String addr_flag = "";
												if(bu_seq.equals("1")) {								
													addr_flag = "P1";
												}else if(bu_seq.equals("2")){
													addr_flag = "P2";
												}else if(bu_seq.equals("3")){
													addr_flag = "P3";
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
					 						
					 						else if(i==9) {
					 							data  = adr_flg;
					 						}
					 						else if(i==10) {
					 							data = inv_seq;
					 						}
					 						else if(i==12) {
					 							cnt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = cnt;
					 						}
					 						else if(i==13) {
					 							desc = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = desc;
					 						}
					 						else if(i==20) {
					 							fin_year = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = fin_year;
					 						}
					 						else if(i == 21) {	//INVOICE_TYPE
//					 							inv_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 							data = inv_type;
					 						}
					 						
					 						else {
					 	 						 if(i==11)// INV_NO
					 	 						{
					 	 							inv_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					 	 							data=inv_no;
					 	 						}
					
					 							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					 							
					 						}	
					 						
//									    	System.out.println(index+"-"+data);
					 						stmt1.setString(index++, data);		    	
					 					}
					 					
					 					queryString = "SELECT COUNTERPARTY_CD FROM FMS_GTA_FFLOW_INV_DTL WHERE COMPANY_CD = ? AND INVOICE_NO = ? AND LINE_NO =? "
					 							+ "AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND INVOICE_SEQ = ? ";
					 					stmt = conn.prepareStatement(queryString);
					 					stmt.setString(1, company_cd);
					 					stmt.setString(2, inv_no);
					 					stmt.setString(3, cnt+"");
					 					stmt.setString(4, fin_year);
					 					stmt.setString(5, inv_type);
					 					stmt.setString(6, inv_seq);
					 					
					 					
					 					rset = stmt.executeQuery();
					 					
					 					if (!rset.next() && !cd.equals("") && !agmt_no.equals("")  && !inv_type.equals("")) {
					 						//System.out.println(queryString1);
					// 							COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,INVOICE_TYPE, INVOICE_SEQ, INV_NO ,TIMESTAMP,
					 						logger.data(fname, (company_cd +"," + cd  + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," +inv_type +","+ inv_seq +","+inv_no+","+cnt+","+desc+","), conn, "");
					 						
					 						stmt1.executeUpdate();
					 						stmt1.close();
					 						
					 						logger_count++;
					 					}
					 					else {
					 						stmt1.close();
					 						skipped_count++;     
					 						logger.data(fname, (company_cd +"," + cd  + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," +inv_type +","+ inv_seq +","+inv_no+","+cnt+","+desc+","), conn, "E");
					 					
//					 						if (invseq.containsKey(fin_year)) {
//												inv_seq_no = invseq.get(fin_year);
//												inv_seq_no=inv_seq_no-1;
//												invseq.put(fin_year, inv_seq_no);
//											}
					 					}
					 					
					 					rset.close();
					 					stmt.close();
					 				}
					 				br.close();
				}
					
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
					
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
