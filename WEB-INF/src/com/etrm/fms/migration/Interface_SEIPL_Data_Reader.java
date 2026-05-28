package com.etrm.fms.migration;

//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.HashMap;
import java.util.Iterator;
//import java.util.Map;
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


public class Interface_SEIPL_Data_Reader {

	String db_src_file_name="Interface_SEIPL_Data_Reader.java";

	String dbline = "", username = "", encrypted = "", password = "";
	String migration_setup_dir = "";
	
	String queryString="", queryString1="";
	Connection conn;
	ResultSet rset,rset1;
	PreparedStatement stmt,stmt1;
	
	String data = "", table_name = "", function_nm = "", columns = "", company_cd = "2",name="";
	int num = 1, index = 0;

	String abbr = "", cd = "1",cont_type="",plant_seq_no="",entity="",type="";
	
	String sysDateTime = "";
	String start_end_dt = null;
	
	String fname = "";
	String fname_error = "";
	String fname1 = "";

	int logger_count = 0;
	int skipped_count=0;  
	int total_count=0, no = 0; 
	
	Migration_Plants_Exceptions mpe=new Migration_Plants_Exceptions();

	String checked_values = "", msg = "", msg_type = "";
	
	File file1 = null;
	FileInputStream file = null;
	XSSFWorkbook workbook = null; 
	XSSFSheet sheet = null;
	Iterator<Row> rowIterator = null;
	Iterator<Cell> cellIterator = null;

	DataMigration_Logger logger = new DataMigration_Logger();
	
	Cell cell;
	Row row;
	
	public void init() {

		function_nm="init()";
		try {
			
			fname = "DataLogs/Reader/Interface_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/Interface_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
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
					
					if (checked_values.contains("FMS_ENTITY_ACCOUNT_CODE_SUN,")) {	
						FMS_ENTITY_ACCOUNT_CODE_SUN();
					}
					if (checked_values.contains("FMS_TAX_STRUCTURE_SUN,")) {	
						FMS_TAX_STRUCTURE_SUN();
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

	public void FMS_ENTITY_ACCOUNT_CODE_SUN() throws IOException, SQLException {

		function_nm="FMS_ENTITY_ACCOUNT_CODE_SUN()";
		try {
			
			table_name = "FMS_ENTITY_ACCOUNT_CODE_SUN";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  

			queryString1 = "INSERT INTO FMS_ENTITY_ACCOUNT_CODE_SUN(COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ_NO,ENTITY,ACCOUNT_TYPE,ACCOUNT_CODE,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE) "
					+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_ACCOUNT_CODE_SUN_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_ACCOUNT_CODE_SUN_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ_NO,ENTITY,ACCOUNT_TYPE,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
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
				    			cd = rset.getString(1);
				    		} else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// agmt_type
				    			plant_seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (plant_seq_no != null) {
				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 3) {	// agmt_no
				    			entity = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (entity != null) {
				    				entity = entity.substring(1, entity.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 4) {	// agmt_rev
				    			type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (type != null) {
				    				type = type.substring(1, type.length() - 1);
								}
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_ENTITY_ACCOUNT_CODE_SUN WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND PLANT_SEQ_NO = ? AND ENTITY = ? AND ACCOUNT_TYPE = ?  ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, plant_seq_no);
			    	stmt.setString(4, entity);
			    	stmt.setString(5, type);
			    	rset = stmt.executeQuery();
			    	
				    if (!rset.next() && !cd.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + "," + plant_seq_no + "," + entity + "," + type + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + "," + plant_seq_no + "," + entity + "," + type + ","), conn, "E");
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


	public void FMS_TAX_STRUCTURE_SUN() throws IOException, SQLException {

		function_nm="FMS_TAX_STRUCTURE_SUN()";
		try {
			
			table_name = "FMS_TAX_STRUCTURE_SUN";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  
			int count = 0;

			queryString1 = "INSERT INTO FMS_TAX_STRUCTURE_SUN(COMPANY_CD,TAX_STR_CD,TAX_CODE,BU_UNIT,BU_STATE_TIN,SUN_CODE,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE,ACCOUNT_TYPE) "
					+ "VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TAX_STRUCTURE_SUN_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TAX_STRUCTURE_SUN_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,TAX_STR_CD,TAX_CODE,BU_UNIT,BU_STATE_TIN,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					
					row = rowIterator.next();
					count = 1;
					String ins_taxes = "";
					for (int i = 0; i < count; i++) {
						total_count++;  
						
						cd = "0";
						data = null;
						
						int tax_cd = 0, tax_str_cd = 0;
						String bu = "", bu_tin = "";
						String pay_recv = "";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
					    cellIterator = row.cellIterator();
					    while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							data = null;
							
					    	if (cell.getColumnIndex() == 0) {	// Company Code 
						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    		
						    		if (data.contains("S")) {
						    			pay_recv = "P";
						    		}
						    		else {
						    			pay_recv = data;
						    		}
						    	}
						    	
								data = company_cd;
					    	}
					    	else if (cell.getColumnIndex() == 1) {	// Tax Structure
					    		String factor = "";
						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    		
						    		if (data.contains(" ")) {
						    			factor = data.split(" ")[1];
						    			data = data.split(" ")[0];
						    		}
						    		
						    	}
					    		queryString = "SELECT B.TAX_STR_CD, B.TAX_CODE FROM FMS_TAX_STRUCTURE A, FMS_TAX_STRUCTURE_DTL B, FMS_TAX_MST C WHERE A.TAX_STR_CD = B.TAX_STR_CD AND B.TAX_CODE = C.TAX_CODE AND C.TAX_ALIAS_CODE LIKE ? ";
					    				
					    		if (!factor.equals("")) {
					    			queryString += " AND B.FACTOR = ? ";
					    		}
					    		if (!pay_recv.equals("")) {
					    			queryString += " AND A.PAY_RECV = ? ";
					    		}
					    				
					    		int index = 1;
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(index++, "%"+data+"%");		
					    		if (!factor.equals("")) {
					    			stmt.setString(index++, factor);
					    		}		
					    		if (!pay_recv.equals("")) {
					    			stmt.setString(index++, pay_recv);
					    		}
					    		rset = stmt.executeQuery();
					    		while (rset.next()) {
					    			if (!ins_taxes.contains(rset.getString(1)+rset.getString(2))) {
						    			tax_str_cd = rset.getInt(1);
						    			tax_cd = rset.getInt(2);
						    			ins_taxes += (rset.getString(1)+rset.getString(2)+",");
					    			}
					    		} 
					    		rset.close();
					    		stmt.close();
					    		data = tax_str_cd+"";
					    		
					    		
					    		// For getting count
					    		queryString = "SELECT COUNT(B.TAX_STR_CD) FROM FMS_TAX_STRUCTURE A, FMS_TAX_STRUCTURE_DTL B, FMS_TAX_MST C WHERE A.TAX_STR_CD = B.TAX_STR_CD AND B.TAX_CODE = C.TAX_CODE AND C.TAX_ALIAS_CODE LIKE ? ";
			    				
					    		if (!factor.equals("")) {
					    			queryString += " AND B.FACTOR = ? ";
					    		}
					    		if (!pay_recv.equals("")) {
					    			queryString += " AND A.PAY_RECV = ? ";
					    		}
					    				
					    		index = 1;
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(index++, "%"+data+"%");		
					    		if (!factor.equals("")) {
					    			stmt.setString(index++, factor);
					    		}		
					    		if (!pay_recv.equals("")) {
					    			stmt.setString(index++, pay_recv);
					    		}
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			count = rset.getInt(1);
					    		} 
					    		rset.close();
					    		stmt.close();
					    		
					    		
					    	}
					    	else if (cell.getColumnIndex() == 2) { // TaxCode
					    		data = tax_cd+"";
					    	}
					    	else if (cell.getColumnIndex() == 4) { // BU State Tin
					    		
						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
						    	
						    	queryString = "SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM LIKE ? ";	
						    	stmt = conn.prepareStatement(queryString);
						    	stmt.setString(1, "%"+data+"%");
						    	rset = stmt.executeQuery();
						    	
						    	if (rset.next()) {
						    		data = rset.getString(1);
						    	}
						    	else {
						    		data = "0";
						    	}
						    	
						    	rset.close();
						    	stmt.close();
						    	
						    	bu_tin = data;
					    		
					    	}
					    	else {
					    		
					    		if (cell.getColumnIndex() == 3) { // BU Unit
					    			bu = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							    	if(bu != null) {
							    		bu = bu.substring(1, bu.length()-1);
							    	}
					    		}
					    		
						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
					    	}
					    	stmt1.setString(index++, data);
					    }
					     
				    	queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE_SUN WHERE COMPANY_CD = ? AND TAX_STR_CD = ? AND TAX_CODE = ? AND BU_UNIT = ? AND BU_STATE_TIN = ?  ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, company_cd);
				    	stmt.setInt(2, tax_str_cd);
				    	stmt.setInt(3, tax_cd);
				    	stmt.setString(4, bu);
				    	stmt.setString(5, bu_tin);
				    	rset = stmt.executeQuery();
				    	
					    if (!rset.next() && tax_str_cd != 0) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + tax_str_cd + "," + tax_cd + "," + bu + "," + bu_tin + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+"," + tax_str_cd + "," + tax_cd + "," + bu + "," + bu_tin + ","), conn, "E");
						}
					    
					    rset.close();
					    stmt.close();
					}
					
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

