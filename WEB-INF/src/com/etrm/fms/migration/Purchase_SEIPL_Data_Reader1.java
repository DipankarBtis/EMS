package com.etrm.fms.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.RuntimeConf;

import automation.Auto_DB_Connection;
import automation.Auto_SystemErrorLogger;

public class Purchase_SEIPL_Data_Reader1 {

	public static void main(String[] args) 
	{
		Purchase_Excel_Reader ex = new Purchase_Excel_Reader();
		ex.init();
	}
	
}

class Purchase_Excel_Reader {

	String db_src_file_name="Purchase_SEIPL_Data_Reader.java";
	
	String queryString="", queryString1="";
	Connection conn;
	ResultSet rset,rset1;
	PreparedStatement stmt,stmt1;
	
	String data = "", table_name = "";
	int num = 1, index = 0;
	
	String abbr = "", cd = "1";
	
	File file1 = null;
	FileInputStream file = null;
	XSSFWorkbook workbook = null; 
	XSSFSheet sheet = null;
	Iterator<Row> rowIterator = null;
	Iterator<Cell> cellIterator = null;
	
	Cell cell;
	Row row;
	
	public void init() {

		String function_nm="init()";
		try {
			
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
				if(conn != null)  
				{
					FMS_TRADER_AGMT_MST();
					FMS_TRADER_AGMT_BU();
					FMS_TRADER_AGMT_PLANT();
					FMS_TRADER_CN_MST();
					FMS_TRADER_CARGO_MST();
					FMS_SECURITY_DEAL_MAP();
					FMS_SECURITY_MST();
					LOG_FMS_SECURITY_MST();
					FMS_CONT_PRICE_DTL();
					FMS_CONT_PRICE_MIN_DTL();
					FMS_CARGO_SVC_CONT_MST();
					FMS_CARGO_SVC_CONT_SVC_BU();
					FMS_CARGO_SVC_CONT_BU();
					FMS_BUY_CARGO_NOM();
					FMS_BUY_CARGO_NOM_BL();
					FMS_BUY_CARGO_NOM_BOE();
					FMS_BUY_CARGO_ALLOC();
					FMS_BUY_CARGO_ALLOC_BL();
					FMS_BUY_CARGO_ALLOC_BOE();
				}
	    	}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally
		{
			try
			{
				if(rset != null){try{rset.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try{rset1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(conn != null){try{conn.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			catch(Exception e)
			{
				new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
		}
		
	}

	public void FMS_TRADER_AGMT_MST() throws IOException, SQLException {

		String function_nm="FMS_TRADER_AGMT_MST()";
		try {
			table_name = "FMS_TRADER_AGMT_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_TRADER_AGMT_MST VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			
			file1 = new File("migration/EXPORT/FMS_TRADER_AGMT_MST.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_TRADER_AGMT_MST.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_no = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 15) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 3) {	// agmt_no
				    			agmt_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			agmt_no = agmt_no.substring(1, agmt_no.length()-1);
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT AGMT_NAME FROM FMS_TRADER_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() ) {
				    	// System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_TRADER_AGMT_BU() throws IOException, SQLException {

		String function_nm="FMS_TRADER_AGMT_BU()";
		try {
			table_name = "FMS_TRADER_AGMT_BU";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_TRADER_AGMT_BU VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_TRADER_AGMT_BU.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_TRADER_AGMT_BU.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_type = "", agmt_no = "", plant_seq_no = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT A.COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST A WHERE A.COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		else {
				    			cd = null;
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 6) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// Agmt_Type
				    			agmt_type = cell.getStringCellValue();
				    			agmt_type = agmt_type.substring(1, agmt_type.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 3) {	// Agmt_no
				    			agmt_no = cell.getStringCellValue();
				    			agmt_no = agmt_no.substring(1, agmt_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 5) {	// Plant_Seq_No
				    			plant_seq_no = cell.getStringCellValue();
				    			plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length()-1);
				    		}
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_TRADER_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND PLANT_SEQ_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_type);
			    	stmt.setString(4, agmt_no);
			    	stmt.setString(5, plant_seq_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && cd != null ) {
				    	// System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_TRADER_AGMT_PLANT() throws IOException, SQLException {

		String function_nm="FMS_TRADER_AGMT_PLANT()";
		try {
			table_name = "FMS_TRADER_AGMT_PLANT";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_TRADER_AGMT_PLANT VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_TRADER_AGMT_BU.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_TRADER_AGMT_BU.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_type = "", agmt_no = "", plant_seq_no = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT A.COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST A WHERE A.COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		else {
				    			cd = null;
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 6) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// Agmt_Type
				    			agmt_type = cell.getStringCellValue();
				    			agmt_type = agmt_type.substring(1, agmt_type.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 3) {	// Agmt_no
				    			agmt_no = cell.getStringCellValue();
				    			agmt_no = agmt_no.substring(1, agmt_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 5) {	// Plant_Seq_No
				    			plant_seq_no = cell.getStringCellValue();
				    			plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length()-1);
				    		}
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_TRADER_AGMT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND PLANT_SEQ_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_type);
			    	stmt.setString(4, agmt_no);
			    	stmt.setString(5, plant_seq_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && cd != null ) {
				    	// System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_TRADER_CN_MST() throws IOException, SQLException {

		String function_nm="FMS_TRADER_CN_MST()";
		try {
			table_name = "FMS_TRADER_CN_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
	    	queryString1 = "INSERT INTO FMS_TRADER_CN_MST VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_TRADER_CN_MST.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_TRADER_CN_MST.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_no = "", cont_no = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 20 || cell.getColumnIndex() == 55) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 3) {	// agmt_no
				    			agmt_no = cell.getStringCellValue();
				    			agmt_no = agmt_no.substring(1, agmt_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 8) {	// cont_no
				    			cont_no = cell.getStringCellValue();
				    			cont_no = cont_no.substring(1, cont_no.length()-1);
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NAME FROM FMS_TRADER_CN_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, cont_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() ) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_TRADER_CARGO_MST() throws IOException, SQLException {

		String function_nm="FMS_TRADER_CARGO_MST()";
		try {
			table_name = "FMS_TRADER_CARGO_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_TRADER_CARGO_MST VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_TRADER_CARGO_MST.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_TRADER_CARGO_MST.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_no = "", cont_no = "", cargo_no = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 17) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 3) {	// agmt_no
				    			agmt_no = cell.getStringCellValue();
				    			agmt_no = agmt_no.substring(1, agmt_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 6) {	// cont_no
				    			cont_no = cell.getStringCellValue();
				    			cont_no = cont_no.substring(1, cont_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 8) {	// cargo_no
				    			cargo_no = cell.getStringCellValue();
				    			cargo_no = cargo_no.substring(1, cargo_no.length()-1);
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? AND CARGO_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, cont_no);
			    	stmt.setString(5, cargo_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() ) {
				    	// System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_SECURITY_DEAL_MAP() throws IOException, SQLException {

		String function_nm="FMS_SECURITY_DEAL_MAP()";
		try {
			table_name = "FMS_SECURITY_DEAL_MAP";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_SECURITY_DEAL_MAP VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_SECURITY_DEAL_MAP.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_SECURITY_DEAL_MAP.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 10) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// seq_no
				    			seq_no = cell.getStringCellValue();
				    			seq_no = seq_no.substring(1, seq_no.length()-1);
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, seq_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() ) {
				    	// System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_SECURITY_MST() throws IOException, SQLException {

		String function_nm="FMS_SECURITY_MST()";
		try {
			table_name = "FMS_SECURITY_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_SECURITY_MST VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_SECURITY_MST.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_SECURITY_MST.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 7 || cell.getColumnIndex() == 29 || cell.getColumnIndex() == 32 || cell.getColumnIndex() == 34) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// seq_no
				    			seq_no = cell.getStringCellValue();
				    			seq_no = seq_no.substring(1, seq_no.length()-1);
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, seq_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() ) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void LOG_FMS_SECURITY_MST() throws IOException, SQLException {

		String function_nm="LOG_FMS_SECURITY_MST()";
		try {
			table_name = "LOG_FMS_SECURITY_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO LOG_FMS_SECURITY_MST VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/LOG_FMS_SECURITY_MST.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/LOG_FMS_SECURITY_MST.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 9 || cell.getColumnIndex() == 31 || cell.getColumnIndex() == 34 || cell.getColumnIndex() == 36) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// seq_no
				    			seq_no = cell.getStringCellValue();
				    			seq_no = seq_no.substring(1, seq_no.length()-1);
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT SEQ_NO FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, seq_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() ) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{ 
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_CONT_PRICE_DTL() throws IOException, SQLException {

		String function_nm="FMS_CONT_PRICE_DTL()";
		try {
			table_name = "FMS_CONT_PRICE_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_CONT_PRICE_DTL VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_CONT_PRICE_DTL.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_CONT_PRICE_DTL.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1)+"-"+cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 14) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
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
				    	}
				    	else {
				    		if (cell.getColumnIndex() == 3) {	// seq_no
				    			seq_no = cell.getStringCellValue();
				    			seq_no = seq_no.substring(1, seq_no.length()-1);
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT SEQ_NO FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = ? AND MAPPING_ID = ? AND SEQ_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, seq_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() ) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_CONT_PRICE_MIN_DTL() throws IOException, SQLException {

		String function_nm="FMS_CONT_PRICE_MIN_DTL()";
		try {
			table_name = "FMS_CONT_PRICE_MIN_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_CONT_PRICE_MIN_DTL VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_CONT_PRICE_MIN_DTL.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_CONT_PRICE_MIN_DTL.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String seq_no = "", curve_nm = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1)+"-"+cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
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
						/*else if (cell.getColumnIndex() == 17) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 3) {	// seq_no
				    			seq_no = cell.getStringCellValue();
				    			seq_no = seq_no.substring(1, seq_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 7) {	// curve_nm
				    			curve_nm = cell.getStringCellValue();
				    			curve_nm = curve_nm.substring(1, curve_nm.length()-1);
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT SEQ_NO FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = ? AND MAPPING_ID = ? AND SEQ_NO = ? AND CURVE_NM = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, seq_no);
			    	stmt.setString(4, curve_nm);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() ) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_CARGO_SVC_CONT_MST() throws IOException, SQLException {

		String function_nm="FMS_CARGO_SVC_CONT_MST()";
		try {
			table_name = "FMS_CARGO_SVC_CONT_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_CARGO_SVC_CONT_MST VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_CARGO_SVC_CONT_MST.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_CARGO_SVC_CONT_MST.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String entity_type = "", cont_no = "", cont_type = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT A.COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.COMPANY_CD = '2' AND B.ENTITY = ? AND A.COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr.substring(0, 1));
				    		stmt.setString(2, abbr.substring(1));
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		else {
				    			cd = null;
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 17) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// Entity_Type
				    			entity_type = cell.getStringCellValue();
				    			entity_type = entity_type.substring(1, entity_type.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 3) {	// Cont_no
				    			cont_no = cell.getStringCellValue();
				    			cont_no = cont_no.substring(1, cont_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 4) {	// Cont_Type
				    			cont_type = cell.getStringCellValue();
				    			cont_type = cont_type.substring(1, cont_type.length()-1);
				    		}
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_CARGO_SVC_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY_TYPE = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, entity_type);
			    	stmt.setString(4, cont_no);
			    	stmt.setString(5, cont_type);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && cd != null ) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_CARGO_SVC_CONT_SVC_BU() throws IOException, SQLException {

		String function_nm="FMS_CARGO_SVC_CONT_SVC_BU()";
		try {
			table_name = "FMS_CARGO_SVC_CONT_SVC_BU";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_CARGO_SVC_CONT_SVC_BU VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_CARGO_SVC_CONT_SVC_BU.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_CARGO_SVC_CONT_SVC_BU.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String entity_type = "", cont_no = "", cont_type = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT A.COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.COMPANY_CD = '2' AND B.ENTITY = ? AND A.COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr.substring(0, 1));
				    		stmt.setString(2, abbr.substring(1));
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		else {
				    			cd = null;
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 6) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// Entity_Type
				    			entity_type = cell.getStringCellValue();
				    			entity_type = entity_type.substring(1, entity_type.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 3) {	// Cont_no
				    			cont_no = cell.getStringCellValue();
				    			cont_no = cont_no.substring(1, cont_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 4) {	// Cont_Type
				    			cont_type = cell.getStringCellValue();
				    			cont_type = cont_type.substring(1, cont_type.length()-1);
				    		}
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_CARGO_SVC_CONT_SVC_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY_TYPE = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, entity_type);
			    	stmt.setString(4, cont_no);
			    	stmt.setString(5, cont_type);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && cd != null ) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_CARGO_SVC_CONT_BU() throws IOException, SQLException {

		String function_nm="FMS_CARGO_SVC_CONT_BU()";
		try {
			table_name = "FMS_CARGO_SVC_CONT_BU";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_CARGO_SVC_CONT_BU VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_CARGO_SVC_CONT_SVC_BU.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_CARGO_SVC_CONT_SVC_BU.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String entity_type = "", cont_no = "", cont_type = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT A.COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.COMPANY_CD = '2' AND B.ENTITY = ? AND A.COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr.substring(0, 1));
				    		stmt.setString(2, abbr.substring(1));
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		else {
				    			cd = null;
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 6) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// Entity_Type
				    			entity_type = cell.getStringCellValue();
				    			entity_type = entity_type.substring(1, entity_type.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 3) {	// Cont_no
				    			cont_no = cell.getStringCellValue();
				    			cont_no = cont_no.substring(1, cont_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 4) {	// Cont_Type
				    			cont_type = cell.getStringCellValue();
				    			cont_type = cont_type.substring(1, cont_type.length()-1);
				    		}
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_CARGO_SVC_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ENTITY_TYPE = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, entity_type);
			    	stmt.setString(4, cont_no);
			    	stmt.setString(5, cont_type);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && cd != null ) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_BUY_CARGO_NOM() throws IOException, SQLException {

		String function_nm="FMS_BUY_CARGO_NOM()";
		try {
			table_name = "FMS_BUY_CARGO_NOM";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_BUY_CARGO_NOM VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_BUY_CARGO_NOM.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_BUY_CARGO_NOM.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_no = "", cont_no = "", cargo_no = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else if (cell.getColumnIndex() == 3) {	// Agmt_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT AGMT_NO FROM FMS_TRADER_AGMT_MST WHERE AGMT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		agmt_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 6) {	// Cont_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CONT_NO FROM FMS_TRADER_CN_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cont_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 8) {	// Cargo_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cargo_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 10) {	// Ship_cd
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT SHIP_CD FROM FMS_SHIP_MST WHERE SHIP_NAME = ?  ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
						else if (cell.getColumnIndex() == 14 || cell.getColumnIndex() == 21) {	// Country_nm
				    		data = ((cell.getStringCellValue().contains("'null'") || cell.getStringCellValue().equals("''")) ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
							queryString = "SELECT COUNTRY_NM FROM FMS_COUNTRY_MST WHERE UPPER(COUNTRY_NM) LIKE ? ";
							stmt = conn.prepareStatement(queryString);
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
						/*else if (cell.getColumnIndex() == 24) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else if (cell.getColumnIndex() == 27 || cell.getColumnIndex() == 28 || cell.getColumnIndex() == 29) {	// CHA, VA, SURV
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT A.COMPANY_CD, A.ENTITY_TYPE, A.COUNTERPARTY_CD, A.CONTRACT_TYPE, A.CONT_NO FROM FMS_CARGO_SVC_CONT_MST A, FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
								data = rset.getString(1)+rset.getString(2)+rset.getString(3)+rset.getString(4)+rset.getString(5);
				    		}
				    		else {
				    			data = null;
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
				    	else {
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_NOM WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? and CONTRACT_TYPE = 'N' AND CARGO_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, cont_no);
			    	stmt.setString(5, cargo_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("0")) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_BUY_CARGO_NOM_BL() throws IOException, SQLException {

		String function_nm="FMS_BUY_CARGO_NOM_BL()";
		try {
			table_name = "FMS_BUY_CARGO_NOM_BL";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_BUY_CARGO_NOM_BL VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_BUY_CARGO_NOM_BL.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_BUY_CARGO_NOM_BL.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_no = "", cont_no = "", cargo_no = "", bl_no = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else if (cell.getColumnIndex() == 3) {	// Agmt_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT AGMT_NO FROM FMS_TRADER_AGMT_MST WHERE AGMT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		agmt_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 6) {	// Cont_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CONT_NO FROM FMS_TRADER_CN_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cont_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 8) {	// Cargo_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cargo_no = data;
				    	}
						/*else if (cell.getColumnIndex() == 15) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 10) {	// Bl_No
				    			bl_no = cell.getStringCellValue();
				    			bl_no = bl_no.substring(1, bl_no.length()-1);
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_NOM_BL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? and CONTRACT_TYPE = 'N' AND CARGO_NO = ? AND BL_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, cont_no);
			    	stmt.setString(5, cargo_no);
			    	stmt.setString(6, bl_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("0")) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_BUY_CARGO_NOM_BOE() throws IOException, SQLException {

		String function_nm="FMS_BUY_CARGO_NOM_BOE()";
		try {
			table_name = "FMS_BUY_CARGO_NOM_BOE";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_BUY_CARGO_NOM_BOE VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_BUY_CARGO_NOM_BOE.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_BUY_CARGO_NOM_BOE.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_no = "", cont_no = "", cargo_no = "", boe_no = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else if (cell.getColumnIndex() == 3) {	// Agmt_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT AGMT_NO FROM FMS_TRADER_AGMT_MST WHERE AGMT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		agmt_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 6) {	// Cont_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CONT_NO FROM FMS_TRADER_CN_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cont_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 8) {	// Cargo_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cargo_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 13) {	// BOE_QTY
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT BL_QTY FROM FMS_BUY_CARGO_NOM_BL WHERE CONT_NO = ? AND COMPANY_CD = '2' AND BL_NO = ?  ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cont_no);
				    		stmt.setString(2, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
				    	/*else if (cell.getColumnIndex() == 17) {	// Emp_Cd
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
				    	}*/
				    	else {
				    		if (cell.getColumnIndex() == 10) {	// Boe_No
				    			boe_no = cell.getStringCellValue();
				    			boe_no = boe_no.substring(1, boe_no.length()-1);
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_NOM_BOE WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = 'N' AND CARGO_NO = ? AND BOE_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, cont_no);			
			    	stmt.setString(5, cargo_no);			
			    	stmt.setString(6, boe_no);			    	
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("0")) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_BUY_CARGO_ALLOC() throws IOException, SQLException {

		String function_nm="FMS_BUY_CARGO_ALLOC()";
		try {
			table_name = "FMS_BUY_CARGO_ALLOC";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_BUY_CARGO_ALLOC VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_BUY_CARGO_ALLOC.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_BUY_CARGO_ALLOC.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_no = "", cont_no = "", cargo_no = "";
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
				    		cargo_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		cargo_no = cargo_no.substring(1, cargo_no.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cargo_no);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		data = cd;
				    	}
				    	else if (cell.getColumnIndex() == 3) {	// Agmt_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT AGMT_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		agmt_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 6) {	// Cont_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CONT_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cont_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 8) {	// Cargo_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cargo_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 10) {	// Ship_cd
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT SHIP_CD FROM FMS_BUY_CARGO_NOM WHERE CONT_NO = ? AND CARGO_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cont_no);
				    		stmt.setString(2, cargo_no);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
						/*else if (cell.getColumnIndex() == 35) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_ALLOC WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? and CONTRACT_TYPE = 'N' AND CARGO_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, cont_no);
			    	stmt.setString(5, cargo_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("0")) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_BUY_CARGO_ALLOC_BL() throws IOException, SQLException {

		String function_nm="FMS_BUY_CARGO_ALLOC_BL()";
		try {
			table_name = "FMS_BUY_CARGO_ALLOC_BL";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_BUY_CARGO_ALLOC_BL VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_BUY_CARGO_ALLOC_BL.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_BUY_CARGO_ALLOC_BL.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_no = "", cont_no = "", cargo_no = "", bl_no = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else if (cell.getColumnIndex() == 3) {	// Agmt_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT AGMT_NO FROM FMS_TRADER_AGMT_MST WHERE AGMT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		agmt_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 6) {	// Cont_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CONT_NO FROM FMS_TRADER_CN_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cont_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 8) {	// Cargo_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cargo_no = data;
				    	}
						/*else if (cell.getColumnIndex() == 17) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 10) {	// Bl_No
				    			bl_no = cell.getStringCellValue();
				    			bl_no = bl_no.substring(1, bl_no.length()-1);
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_ALLOC_BL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? and CONTRACT_TYPE = 'N' AND CARGO_NO = ? AND BL_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, cont_no);
			    	stmt.setString(5, cargo_no);
			    	stmt.setString(6, bl_no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("0")) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
		}
		
	}

	public void FMS_BUY_CARGO_ALLOC_BOE() throws IOException, SQLException {

		String function_nm="FMS_BUY_CARGO_ALLOC_BOE()";
		try {
			table_name = "FMS_BUY_CARGO_ALLOC_BOE";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";

	    	queryString1 = "INSERT INTO FMS_BUY_CARGO_ALLOC_BOE VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
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
			
			file1 = new File("migration/EXPORT/FMS_BUY_CARGO_ALLOC_BOE.xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File("migration/EXPORT/FMS_BUY_CARGO_ALLOC_BOE.xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					String agmt_no = "", cont_no = "", cargo_no = "", boe_no = "";
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
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = "2";
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else if (cell.getColumnIndex() == 3) {	// Agmt_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT AGMT_NO FROM FMS_TRADER_AGMT_MST WHERE AGMT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		agmt_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 6) {	// Cont_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CONT_NO FROM FMS_TRADER_CN_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cont_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 8) {	// Cargo_no
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		cargo_no = data;
				    	}
				    	else if (cell.getColumnIndex() == 15) {	// ACT_BOE_QTY
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT BL_QTY FROM FMS_BUY_CARGO_NOM_BL WHERE CONT_NO = ? AND COMPANY_CD = '2' AND BL_NO = ?  ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cont_no);
				    		stmt.setString(2, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
				    	else if (cell.getColumnIndex() == 17) {	// ACT_QTY_MT
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT BL_QTY_MT FROM FMS_BUY_CARGO_NOM_BL WHERE CONT_NO = ? AND COMPANY_CD = '2' AND BL_NO = ?  ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cont_no);
				    		stmt.setString(2, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
				    	else if (cell.getColumnIndex() == 18) {	// ACT_QTY_SCM
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT BL_QTY_SCM FROM FMS_BUY_CARGO_NOM_BL WHERE CONT_NO = ? AND COMPANY_CD = '2' AND BL_NO = ?  ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cont_no);
				    		stmt.setString(2, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		data = rset.getString(1);
				    		}
				    		else  {
				    			data = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
						/*else if (cell.getColumnIndex() == 21) {	// Emp_Cd
							data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE EMP_UID = ? ";
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
						}*/
				    	else {
				    		if (cell.getColumnIndex() == 10) {	// Boe_No
				    			boe_no = cell.getStringCellValue();
				    			boe_no = boe_no.substring(1, boe_no.length()-1);
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_ALLOC_BOE WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = 'N' AND CARGO_NO = ? AND BOE_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, "2");
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, cont_no);			
			    	stmt.setString(5, cargo_no);			
			    	stmt.setString(6, boe_no);			    	
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("0")) {
				    	//System.out.println(queryString1);
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    }
				    
				    rset.close();
				    stmt.close();
				}
				
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
		}
		catch(Exception e)
		{
			conn.rollback();
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			conn.commit();
			file.close();
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

}

