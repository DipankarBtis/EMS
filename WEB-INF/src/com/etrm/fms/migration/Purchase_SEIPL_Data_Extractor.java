

package com.etrm.fms.migration;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.SystemErrorLogger;


public class Purchase_SEIPL_Data_Extractor {

//	public static void main(String[] args) 
//	{
//		Purchase_Excel_Extractor ex = new Purchase_Excel_Extractor();
//		ex.getmail_list();
//		ex.makeDirectory();
//		ex.init();
//	}
//	
//}
//
//class Purchase_Excel_Extractor {
//	public void setDelta_FromDt(String from_dt) {
//		delta_FromDt = from_dt + " 00:00:00";
//	}
//	
//	public void setDelta_ToDt(String to_dt) {
//		delta_ToDt = to_dt + " 23:59:59";
//	}
	String db_src_file_name = "Purchase_SEIPL_Data_Extractor.java";
	String function_nm = "";

	//private static final Logger LOGGER = Logger.getLogger(Purchase_Excel_Extractor.class.getName());
	String fname = "";
	String fname_error = "";
	
	
	String query_abbr="",queryString="",queryString1="",queryString2="",queryString3="",queryString4="";
	Connection conn;
	
	ResultSet rset_abbr,rset,rset1,rset2,rset3,rset4;
	PreparedStatement stmt_abbr,stmt,stmt1,stmt2,stmt3,stmt4;
	
	DataMigration_Logger logger = new DataMigration_Logger();
	Migration_Plants_Exceptions mpe =new Migration_Plants_Exceptions();
	String fname1 = "";
	
	String dbline = "", username = "", encrypted = "", password = "";
	int plant_no1,split_plant_count1;
	String columns = "", filename = "", value = "",company_cd="2";
	String pipeline_cd = "", lng_cd = "", igx_cd = "",tank_cd="",man_cd="",abbr="";
	
	String dir_flag = "N";
	
	int nrow = 0, ncell = 0, query_ind = 0,count= 0;
	
	String checked_values = "", migration_setup_dir = "", msg_type = "", msg = "", sysDateTime = "", start_end_dt = "",org_abbr="";
	
	String delta_FromDt = null;
	String delta_ToDt = null;
	DateUtil utilDate = new DateUtil();	
	FileOutputStream fileOut;
	
	XSSFWorkbook workbook;
	XSSFSheet spreadsheet;
	XSSFRow row;
	Cell cell;
	
	public void init() {

		try {
			
			getmail_list();
			makeDirectory();
			
			fname = "DataLogs/Extractor/Purchase_SEIPL_Data_Extractor(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Extractor/Purchase_SEIPL_Data_Extractor_Error(log)"+sysDateTime+".csv";
			
			fname = migration_setup_dir + fname;
			fname_error = migration_setup_dir + fname_error;
			
			Preferences preferences =  Preferences.userRoot().node("/processFlag");	
			
			fname1 = "DataLogs/Script_Status(log).csv";	
			fname1 = migration_setup_dir + fname1;
//			preferences.put("Flag", "1");
            Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("FMS8 DBLINE:"+dbline);
            
            conn=DriverManager.getConnection(dbline,username,password);       
	    	if(conn != null && dir_flag.equals("Y"))  
	    	{
	    		preferences.put("Flag", "0");
	    			    		
	    		MANCD_FOR_CARGO();		// For getting Man_cd for lng, igx, pipeline gas 
				//FMS_TRADER_AGMT_MST();
				//FMS_TRADER_AGMT_BU();
				//FMS_TRADER_AGMT_PLANT();		Same Excel file is used as FMS_TRADER_AGMT_BU(). So function not created.
	    		if (checked_values.contains("FMS_TRADER_CN_MST,")) {
					FMS_TRADER_CN_MST();
				}
				if (checked_values.contains("FMS_TRADER_CARGO_MST,")) {
					FMS_TRADER_CARGO_MST();
				}
				if (checked_values.contains("FMS_PSEUDO_CARGO_DTL,")) {
					FMS_PSEUDO_CARGO_DTL();
				}
				if (checked_values.contains("FMS_CONT_PRICE_DTL(P),")) {
					FMS_CONT_PRICE_DTL();
				}
				if (checked_values.contains("FMS_CONT_PRICE_MIN_DTL(P),")) {
					FMS_CONT_PRICE_MIN_DTL();
				}
				if (checked_values.contains("FMS_CARGO_SVC_CONT_MST,")) {
					FMS_CARGO_SVC_CONT_MST();
				}
				if (checked_values.contains("FMS_CARGO_SVC_CONT_SVC_BU,")) {
					FMS_CARGO_SVC_CONT_SVC_BU();
					// FMS_CARGO_SVC_CONT_BU();		Same Excel file is used as FMS_CARGO_SVC_CONT_SVC_BU(). So function not created.
				}
	    		if (checked_values.contains("FMS_BUY_CARGO_NOM,")) {
					FMS_BUY_CARGO_NOM();
				}
				if (checked_values.contains("FMS_BUY_CARGO_NOM_BL,")) {
					FMS_BUY_CARGO_NOM_BL();
				}
				if (checked_values.contains("FMS_BUY_CARGO_NOM_BOE,")) {
					FMS_BUY_CARGO_NOM_BOE();
				}
				if (checked_values.contains("FMS_BUY_CARGO_ALLOC,")) {
					FMS_BUY_CARGO_ALLOC();
				}
				if (checked_values.contains("FMS_BUY_CARGO_ALLOC_BL,")) {
					FMS_BUY_CARGO_ALLOC_BL();
				}
				if (checked_values.contains("FMS_BUY_CARGO_ALLOC_BOE,")) {
					FMS_BUY_CARGO_ALLOC_BOE();
				}
				if (checked_values.contains("FMS_TRADER_CONT_MST,")) {
	    			FMS_TRADER_CONT_MST();
	    		}
				if (checked_values.contains("FMS_TRADER_CONT_BU,")) {
	    			FMS_TRADER_CONT_BU();
	    		}
				if (checked_values.contains("FMS_TRADER_CONT_SPLIT_PLANT,")) {
					FMS_TRADER_CONT_SPLIT_PLANT();
	    		}
				if (checked_values.contains("FMS_TRADER_CONT_PLANT,")) {
	    			FMS_TRADER_CONT_PLANT();
	    		}
				if (checked_values.contains("FMS_TRADER_BILLING_DTL,")) {
					FMS_TRADER_BILLING_DTL();
	    		}
				if (checked_values.contains("FMS_TRADER_CONT_PLANT_CHRG,")) {
					FMS_TRADER_CONT_PLANT_CHRG();
	    		}
				if (checked_values.contains("FMS_BUY_DAILY_BUYER_NOM,")) {
					FMS_BUY_DAILY_BUYER_NOM();
	    		}
				if (checked_values.contains("FMS_BUY_DAILY_SELLER_NOM,")) {
					FMS_BUY_DAILY_SELLER_NOM();
	    		}
				if (checked_values.contains("FMS_BUY_DAILY_ALLOCATION,")) {
					FMS_BUY_DAILY_ALLOCATION();
	    		}
				if (checked_values.contains("FMS_BUY_DAILY_ALLOCATION_MM,")) {
					FMS_BUY_DAILY_ALLOCATION_MM();
	    		}
				if (checked_values.contains("FMS_PUR_SG_INV_MST,")) {
					FMS_PUR_SG_INV_MST();
					FMS_PUR_SG_INV_MST_PURCHASE();
	    		}
				if(checked_values.contains("FMS_CUSTOM_PD_BOND_DTL,"))
				{
					FMS_CUSTOM_PD_BOND_DTL();
				}
				if (checked_values.contains("FMS_PUR_PG_INV_MST,")) {
					FMS_PUR_PG_INV_MST();
	    		}
				
				
				preferences.put("Flag", "1");
			
	    		conn.close();
	    		conn = null;
	    	} 
	    	else {
				msg = "No Checkbox was selected. Extraction Terminated.";
				msg_type = "E";
			}
			
		}
		catch(Exception e)
		{
			//LOGGER.log(Level.WARNING, "Error in Purchase_SEIPL_Data_Extractor.java -> Purchase_Excel_Extractor -> init()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
		}
		finally
		{
			try
			{
				if(rset != null){try{rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_abbr != null){try{rset_abbr.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset4 != null){try{rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_abbr != null){try{stmt_abbr.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			catch(Exception e)
			{
				//LOGGER.log(Level.WARNING, "Error in Purchase_SEIPL_Data_Extractor.java -> Purchase_Excel_Extractor -> init()  ", e);
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				
				msg = "One of the Functions faced an Error. Extraction Terminated.";
				msg_type = "E";
			}
		}
		
	}

	public void MANCD_FOR_CARGO() throws SQLException, IOException {
		function_nm = "MANCD_FOR_CARGO()";
		
		try {
			
			queryString = "SELECT MAN_CD, NVL(DOM_BUY_FLAG, 'N') FROM FMS7_MAN_CONFIRM_CARGO_DTL ORDER BY MAN_CD";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while (rset.next()) {
				
				if (rset.getString(2).equals("N") && !lng_cd.contains(rset.getString(1))) {
					lng_cd += (rset.getString(1) + ",");
				}
				else if (rset.getString(2).equals("K") && !igx_cd.contains(rset.getString(1))) {
					igx_cd += (rset.getString(1) + ",");
				}
				else if (rset.getString(2).equals("Y") && !pipeline_cd.contains(rset.getString(1))) {
					pipeline_cd += (rset.getString(1) + ",");
				}
				else if (rset.getString(2).equals("T") && !tank_cd.contains(rset.getString(1))) {
					tank_cd += (rset.getString(1) + ",");
				}
				/*else {
					System.out.println(">>"+rset.getString(1));
				}*/
//				System.out.println(lng_cd);
			}
			rset.close();
			stmt.close();
			
			lng_cd = lng_cd.substring(0, lng_cd.length()-1);
			igx_cd = igx_cd.substring(0, igx_cd.length()-1);
			pipeline_cd = pipeline_cd.substring(0, pipeline_cd.length()-1);
			tank_cd = tank_cd.substring(0, tank_cd.length()-1);

			
			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}

	public void FMS_TRADER_AGMT_MST() throws SQLException, IOException {
		function_nm = "FMS_TRADER_AGMT_MST()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");			
			
			
	    	columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,AGMT_BASE,AGMT_TYP,SIGNING_DT,START_DT,END_DT,STATUS,FLAG,REMARK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,DEMURRAGE,DEMURRAGE_CLAUSE,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,REOPEN_REQUEST_FLAG,REOPEN_REQUEST_BY,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVE_BY,REOPEN_APPROVAL_DT,REOPEN_REMARK";
	    	
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			row = spreadsheet.createRow(nrow++);
			
			
			
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				int num = 1;
				query_ind = 1;
				
				queryString1 = "SELECT A.TRD_CD, 'M', '1', '0', B.TRADER_NAME, A.MAN_CD, NULL, A.MAN_TYPE, TO_CHAR(A.MAN_DT, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "TO_CHAR(A.MAN_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.MAN_BY_DT, 'DD/MM/YYYY HH24:MI:SS'), 'A', NULL, "
						+ "A.GIVEN_REASON, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, 'Y', NULL,"
						+ " '06:00', '06:00', 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, "
						+ "NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,"
						+ " NULL FROM FMS7_MAN_REQ_MST A, FMS7_TRADER_MST B WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? AND A.MAN_CD IN (";
						if (lng_cd.split(",").length > 0) {
							queryString1 += "?";
							for (int i = 1; i < lng_cd.split(",").length; i++) {
								queryString1 += ",?";
							}
						}
				queryString1 += ") AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY MAN_CD";
				
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(query_ind++, rset.getString(2));
				for (int i = 0; i < lng_cd.split(",").length; i++) {
					stmt1.setString(query_ind++, lng_cd.split(",")[i]);
				}
				stmt1.setString(query_ind++, delta_FromDt);
				stmt1.setString(query_ind++, delta_FromDt);
				stmt1.setString(query_ind++, delta_ToDt);
				stmt1.setString(query_ind++, delta_ToDt);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					value = rset.getString(1) == null ? "null" : rset.getString(1);
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+value+"'");
					
					value = rset.getString(2) == null ? "null" : rset.getString(2);
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+value+"'");
					
					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						
						if (i == 3) {	// agmt_no
							cell.setCellValue("'"+num+"'");
						}
						else if (i == 5) {	// agmt_name
							value = "SEIPL-" + rset.getString(1) + "-M"+num+"-REV0";  
							cell.setCellValue("'"+value+"'");
						}
						else if (i == 7) {	// agreement base
							queryString2 = "SELECT DELV_TYPE FROM FMS7_MAN_REQ_CARGO_DTL WHERE MAN_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(6));
							rset2 = stmt2.executeQuery();
							if (rset2.next() && rset2.getString(1).equals("FOB")) {
								value = "X";
							}
							else {
								value = "D";
							}
							rset2.close();
							stmt2.close();

							cell.setCellValue("'"+value+"'");
						}
						else if (i == 8) {	// agreement type
							value = value.equals("F") ? "0" : "1";
							cell.setCellValue("'"+value+"'");
						}
						else if (i == 14 && value.length() > 100) {	// remark or given reason (in case of length exceeds from 100 characters )
							value = value.substring(0, 100);
							cell.setCellValue("'"+value+"'");
						}
						
						else if (i == 24) {	// DEMURRAGE_RATE
							queryString2 = "SELECT DEMURRAGE_RATE, ALW_LAYTIME_HRS, ALW_LAYTIME_MNS FROM FMS7_CARGO_NOMINATION WHERE MAN_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(6));
							rset2 = stmt2.executeQuery();
							
							if (rset2.next() && rset2.getString(1) != null) {
								cell.setCellValue("'Y'");
								
								i++;
								cell = row.createCell(ncell++);
								cell.setCellValue("'null'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(1) == null ? "null" : rset2.getString(1);
								cell.setCellValue("'"+value+"'");

								i++;
								cell = row.createCell(ncell++);
								cell.setCellValue("'2'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(2) == null ? "null" : rset2.getString(2);
								cell.setCellValue("'"+value+"'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(3) == null ? "null" : rset2.getString(3);
								cell.setCellValue("'"+value+"'");
								
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							rset2.close();
							stmt2.close();
							
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
						
						
					}
					num++;
				}
				
				
				rset1.close();
				stmt1.close();
			}
			stmt.close();
			rset.close();

			
			filename = migration_setup_dir + "EXPORT/FMS_TRADER_AGMT_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}

	public void FMS_TRADER_AGMT_BU() throws SQLException, IOException {
		
		function_nm = "FMS_TRADER_AGMT_BU()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");

			
			
	    	columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
	    	
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;

			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				int num = 1;
				query_ind = 1;
				
				queryString1 = "SELECT A.TRD_CD, 'M', '1', '0', '1', A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_MAN_REQ_MST A, FMS7_TRADER_MST B WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? AND A.MAN_CD IN (";
						if (lng_cd.split(",").length > 0) {
							queryString1 += "?";
							for (int i = 1; i < lng_cd.split(",").length; i++) {
								queryString1 += ",?";
							}
						}
				queryString1 += ") AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY MAN_CD";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(query_ind++, rset.getString(2));
				for (int i = 0; i < lng_cd.split(",").length; i++) {
					stmt1.setString(query_ind++, lng_cd.split(",")[i]);
				}
				stmt1.setString(query_ind++, delta_FromDt);
				stmt1.setString(query_ind++, delta_FromDt);
				stmt1.setString(query_ind++, delta_ToDt);
				stmt1.setString(query_ind++, delta_ToDt);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					value = rset.getString(1) == null ? "null" : rset.getString(1);
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+value+"'");
					
					value = rset.getString(2) == null ? "null" : rset.getString(2);
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+value+"'");
					
					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						
						if (i == 3) {	// agmt_no
							cell.setCellValue("'"+num+"'");
						}
						
						else {
							cell.setCellValue("'"+value+"'");
						}
						
						
					}
					num++;
					count++;
					logger.data(fname, (" M " + "," + num + "," +" 0 "+ "," +" N "+"," +" 0 "+","), conn, "");
				}
				rset1.close();
				stmt1.close();
			}
			stmt.close();
			rset.close();

			
			filename = migration_setup_dir + "EXPORT/FMS_TRADER_AGMT_BU_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
	

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_TRADER_CN_MST() throws SQLException, IOException {
		
		function_nm = "FMS_TRADER_CN_MST()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TRADER_CN_MST>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			
	    	columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_BASE,AGMT_TYP,CONTRACT_TYPE,"
	    			+ "CONT_NO,CONT_REV,CONT_NAME,CONT_REF_NO,CONT_STATUS,DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,"
	    			+ "START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,REV_DT,NUM_CARGO,DAY_DEF_FLAG,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,DEMURRAGE,DEMURRAGE_CLAUSE,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,FCC_FLAG,FCC_BY,FCC_DATE";
	    	
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0; 
			String cd = "";
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// Getting the start of cont_no
			int no = 1000;
			queryString = "SELECT TO_CHAR(SYSDATE, 'YYYY') FROM DUAL";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if(rset.next()) {
				no = no * Integer.parseInt("7" + rset.getString(1).substring(2));
			}
			rset.close();
			stmt.close();
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "CD,AGMT_TYPE,AGMT_NO,AGMR_REV,CONT_TYPE,CONT_NO,CONT_REV,TIMESTAMP", conn);
			
			while (rset.next()) {
				
				int num = 1;
				query_ind = 1;
				
				queryString1 = "SELECT A.TRD_CD, 'M', '1', '0', NULL, A.MAN_TYPE, 'N', TO_CHAR(A.ENT_DT, 'YYYY'), '0', B.TRADER_NAME, A.MAN_CD, 'Y',"// CHANGING FORMAT ONLY YEAR //DG20250506
						+ " TO_CHAR(D.MAN_CONF_DT, 'DD/MM/YYYY HH24:MI:SS'), D.MAN_CONF_TIME, TO_CHAR(D.MAN_CONF_DT, 'DD/MM/YYYY HH24:MI:SS'), D.MAN_CONF_TIME,"
						+ " TO_CHAR(C.DELV_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(C.DELV_TO_DT, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "TO_CHAR(D.ENT_DT, 'DD/MMYYYY HH24:MI:SS'), A.EMP_CD, NULL, NULL, NULL, A.DEAL_TYPE, 'Y', NULL, "
						+ "'06:00', '06:00', 'N', NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL, NULL, NULL, NULL, 'N', NULL, NULL, NULL, NULL,"
						+ " 'N', NULL, 'Y', NULL, 'N', NULL, NULL, NULL, 'Y', A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS')  "
						+ "FROM FMS7_MAN_REQ_MST A, FMS7_TRADER_MST B , FMS7_MAN_CONFIRM_CARGO_DTL C , FMS7_MAN_CONFIRM_MST D "
						+ "WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ?  AND  C.MAN_CONF_CD=D.MAN_CONF_CD AND C.CARGO_REF_CD = (SELECT MAX(Z.CARGO_REF_CD) FROM FMS7_MAN_CONFIRM_CARGO_DTL Z WHERE Z.MAN_CONF_CD = C.MAN_CONF_CD )  AND C.MAN_CD=A.MAN_CD AND A.MAN_CD IN (";	
				if (lng_cd.split(",").length > 0) {
					queryString1 += "?";
					for (int i = 1; i < lng_cd.split(",").length; i++) {
						queryString1 += ",?";
					}
				}
				queryString1 += ")  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ORDER BY MAN_CD";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(query_ind++, rset.getString(2));
				for (int i = 0; i < lng_cd.split(",").length; i++) {
					stmt1.setString(query_ind++, lng_cd.split(",")[i]);
				}
				stmt1.setString(query_ind++, delta_FromDt);
				stmt1.setString(query_ind++, delta_FromDt);
				stmt1.setString(query_ind++, delta_ToDt);
				stmt1.setString(query_ind++, delta_ToDt);
				rset1 = stmt1.executeQuery();			
				
				while(rset1.next()) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					no++;
					

					value = rset.getString(1) == null ? "null" : rset.getString(1).trim();
					cell = row.createCell(ncell++);
					
					 if (mpe.counterparty_map.containsKey(value)) {
//						 	org_abbr=value;
					        value =mpe.counterparty_map.get(value); 
					    }
					 if (value.contains("RIL")) {
						    value = "RIL";
						}
					 cell.setCellValue("'"+value+"'");
					 
					cd = rset.getString(2) == null ? "null" : rset.getString(2);
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+cd+"'");
					
					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						
						if (i == 3) {	// agmt_no
							cell.setCellValue("'"+num+"'");
						}  
						else if(i == 8) {	// Cont_No
							cell.setCellValue("'"+ rset1.getString(8)+"'");
//							cell.setCellValue("'"+no+"'");
						}
						else if (i == 10) {	// agmt_name
							value = "SEIPL-" + rset.getString(1) + "-MSPA"+num+"-REV0 N" + no + "-REV0";  
							cell.setCellValue("'"+value+"'");
						}
						else if (i == 5) {	// agreement base
							queryString2 = "SELECT DELV_TYPE FROM FMS7_MAN_REQ_CARGO_DTL WHERE MAN_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(11));
							rset2 = stmt2.executeQuery();
							if (rset2.next() && rset2.getString(1).equals("FOB")) {
								value = "X";
							}
							else {
								value = "D";
							}
							rset2.close();
							stmt2.close();
							
							cell.setCellValue("'"+value+"'");
						}
						else if (i == 6) {	// agreement type
							value = value.equals("F") ? "0" : "1";
							cell.setCellValue("'"+value+"'");
						}
						else if (i == 11) {	// MAN_CONFIRM_CD(CONT_REF_NO)
							queryString2 = "SELECT MAN_CONF_CD FROM FMS7_MAN_CONFIRM_MST WHERE MAN_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, value);
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								cell.setCellValue("'"+rset2.getString(1)+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							rset2.close();
							stmt2.close();
						}
						else if (i == 17) {
							queryString2 = "SELECT TO_CHAR(MIN(DELV_FROM_DT), 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(11));
							rset2 = stmt2.executeQuery();
							
							if(rset2.next()) {
								value = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							
							cell.setCellValue("'"+value+"'");
						}
						else if (i == 18) {
							queryString2 = "SELECT TO_CHAR(MAX(DELV_TO_DT), 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(11));
							rset2 = stmt2.executeQuery();
							
							if(rset2.next()) {
								value = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							
							cell.setCellValue("'"+value+"'");
						}
						else if (i == 24) {	// No. Of Cargo
							queryString2 = "SELECT COUNT(CARGO_SEQ_NO) FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(11));
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								cell.setCellValue("'"+rset2.getInt(1)+"'");
							}
							rset2.close();
							stmt2.close();
						}
						else if (i == 29) {	// DEMURRAGE_RATE
							queryString2 = "SELECT DEMURRAGE_RATE, ALW_LAYTIME_HRS, ALW_LAYTIME_MNS FROM FMS7_CARGO_NOMINATION WHERE MAN_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(11));
							rset2 = stmt2.executeQuery();
							
							if (rset2.next() && rset2.getString(1) != null) {
								cell.setCellValue("'Y'");
								
								i++;
								cell = row.createCell(ncell++);
								cell.setCellValue("'null'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(1) == null ? "null" : rset2.getString(1);
								cell.setCellValue("'"+value+"'");

								i++;
								cell = row.createCell(ncell++);
								cell.setCellValue("'2'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(2) == null ? "null" : rset2.getString(2);
								cell.setCellValue("'"+value+"'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(3) == null ? "null" : rset2.getString(3);
								cell.setCellValue("'"+value+"'");
								
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							rset2.close();
							stmt2.close();
							
						}
						else if (i == 54) {	// For FCC details
							queryString2 = "SELECT 'Y', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS7_MAN_CONFIRM_MST WHERE MAN_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(11));
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								cell.setCellValue("'Y'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(2) == null ? "null" : rset2.getString(2);
								
								cell.setCellValue("'"+value+"'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(3) == null ? "null" : rset2.getString(3);
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							rset2.close();
							stmt2.close();
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
						
						
					}
					num++;
					count++;
					logger.data(fname, (cd + "," + " M " + "," + num + "," +" 0 "+ "," +" N "+"," + no +","+" 0 "+","), conn, "");
				}
				
				
				rset1.close();
				stmt1.close();
			}
			stmt.close();
			rset.close();

			
			filename = migration_setup_dir + "EXPORT/FMS_TRADER_CN_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_CN_MST>>,", conn);
						
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_TRADER_CARGO_MST() throws SQLException, IOException {
		
		function_nm = "FMS_TRADER_CARGO_MST()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TRADER_CARGO_MST>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			
	    	columns = "COMPANY_CD,CONT_REF_NO,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,CARGO_REF,CARGO_STATUS,CARGO_QTY,RATE,RATE_UNIT,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,TOLERANCE";
	    	
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String cargo_no="",man_conf_cd="";
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// Getting the start of cont_no
			int no = 1000;
			String unit="";
			queryString = "SELECT TO_CHAR(SYSDATE, 'YYYY') FROM DUAL";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if(rset.next()) {
				no = no * Integer.parseInt("7" + rset.getString(1).substring(2));
			}
			rset.close();
			stmt.close();
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ABBR,MAN_CD,AGMT_TYPE,AGMT_NO,AGMR_REV,CONT_TYPE,CONT_NO,CONT_REV,CARGO_NO,TIMESTAMP", conn);

			
			while (rset.next()) {
				
				int num = 1;
				query_ind = 1;
				
				queryString1 = "SELECT A.TRD_CD, 'M', '1', '0', 'N', TO_CHAR(SYSDATE, 'YYYY'), '0', '1', '00001', 'Y', '1', NULL, NULL, "
						+ "TO_CHAR(A.MAN_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.MAN_BY_DT, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EMP_CD, NULL, NULL, NULL, A.MAN_CD "
						+ "FROM FMS7_MAN_REQ_MST A, FMS7_TRADER_MST B WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ?  AND A.MAN_CD IN (";
				if (lng_cd.split(",").length > 0) {
					queryString1 += "?";
					for (int i = 1; i < lng_cd.split(",").length; i++) {
						queryString1 += ",?";
					}
				}
				queryString1 += ")  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY MAN_CD";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(query_ind++, rset.getString(2));
				for (int i = 0; i < lng_cd.split(",").length; i++) {
					stmt1.setString(query_ind++, lng_cd.split(",")[i]);
				}
				stmt1.setString(query_ind++, delta_FromDt);
				stmt1.setString(query_ind++, delta_FromDt);
				stmt1.setString(query_ind++, delta_ToDt);
				stmt1.setString(query_ind++, delta_ToDt);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) {
					no++;
					
					man_cd = rset1.getString(21);			
					
					queryString2 = "SELECT CARGO_SEQ_NO, CARGO_REF_CD, CARGO_STATUS, CONFIRM_VOL, PRICE, PRICE_UNIT, "
							+ "TO_CHAR(DELV_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(DELV_TO_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), EMP_CD FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CD = ?  AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset1.getString(21));
					stmt2.setString(2, delta_FromDt);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_ToDt);
					stmt2.setString(5, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
						while (rset2.next()) {
						
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim();
						abbr=value;
						cell = row.createCell(ncell++);
						
						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        abbr=value;
						    }
						 if (abbr.contains("RIL-D6") || abbr.contains("RIL-CBM")) {
							    abbr = "RIL";
							}
						 cell.setCellValue("'"+abbr+"'");
						
						cell = row.createCell(ncell++);
						queryString3 = "SELECT MAN_CONF_CD FROM FMS7_MAN_CONFIRM_MST WHERE MAN_CD = ? ";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, man_cd);
						rset3 = stmt3.executeQuery();
						if (rset3.next()) {
							man_conf_cd = rset3.getString(1);
							cell.setCellValue("'"+rset3.getString(1)+"'");
						}
						else {
							cell.setCellValue("'"+man_cd+"'");
						}
						rset3.close();
						stmt3.close();
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i);
							
							if (i == 3) {	// agmt_no
								cell.setCellValue("'"+num+"'");
							}
							else if(i == 6) {	// Cont_No
								cell.setCellValue("'"+no+"'");
							}
							else if(i == 8) {
								cargo_no = rset2.getString(1) == null ? "null" : rset2.getString(1);
								cell.setCellValue("'"+cargo_no+"'");
								
								i++;
								cell = row.createCell(ncell++);
								cell.setCellValue("'"+rset2.getString(2)+"'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(3) .equals("T") ? "Y" : "N";
								cell.setCellValue("'"+value+"'");
								
								i++;	//CARGO_QTY
								cell = row.createCell(ncell++);
								queryString4 = "SELECT CONFIRM_VOL,UNIT_CD FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CD = ? AND CARGO_SEQ_NO = ? ";//232025
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, man_cd);
								stmt4.setString(2, rset2.getString(1));
								rset4 = stmt4.executeQuery();
								if(rset4.next()) {
									//unit   1=mmbtu  2=tbtu
										unit=rset4.getString(2);
										if(unit.equals("2"))
										{
											 double cargo_vol = rset4.getDouble(1);
											 BigDecimal conv_mmbtu = BigDecimal.valueOf(Math.round(cargo_vol*1000000));
											 conv_mmbtu  = conv_mmbtu.setScale(0, RoundingMode.CEILING);
											 cell.setCellValue("'"+conv_mmbtu+"'");
											 
										}else
										{
											cell.setCellValue("'"+rset4.getString(1)+"'");
										}
							
								}
								rset4.close();
								stmt4.close();
								
								i++;	//PRICE(RATE)
								cell = row.createCell(ncell++);
								if(rset2.getString(5) == null || rset2.getString(5).equals("") || rset2.getString(5).equals("0")) {
									queryString4 = "SELECT NVL(PRICE,'0') FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CD = ? AND MAN_CONF_CD = ? ";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, man_cd);
									stmt4.setString(2, man_conf_cd);
									rset4 = stmt4.executeQuery();
									if(rset4.next()) {
										cell.setCellValue("'"+rset4.getString(1)+"'");
									}
									rset4.close();
									stmt4.close();
								}
								
								else {
								cell.setCellValue("'"+rset2.getString(5)+"'");
								}
								
								
								i++;
								cell = row.createCell(ncell++);
								cell.setCellValue("'"+rset2.getString(6)+"'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(7) == null ? "null" : rset2.getString(7);
								cell.setCellValue("'"+value+"'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(8) == null ? "null" : rset2.getString(8);
								cell.setCellValue("'"+value+"'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(9) == null ? "null" : rset2.getString(9);
								cell.setCellValue("'"+value+"'");
								
								i++;
								cell = row.createCell(ncell++);
								value = rset2.getString(10) == null ? "null" : rset2.getString(10);
							
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							
							
						}
						count++;
						logger.data(fname, (abbr + ","+man_cd + "," + " M " + "," + num + "," +" 0 "+ "," +" N "+"," + no +","+" 0 "+","+cargo_no+","), conn, "");
					}
						
					rset2.close();
					stmt2.close();
					
					num++;
					
				}
				rset1.close();
				stmt1.close();
			}
			stmt.close();
			rset.close();

			
			filename = migration_setup_dir + "EXPORT/FMS_TRADER_CARGO_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_CARGO_MST>>,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
public void FMS_PSEUDO_CARGO_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_PSEUDO_CARGO_DTL()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_PSEUDO_CARGO_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,CONTRACT_TYPE,CARGO_NO,SEQ_NO,QTY,ENT_DT,ENT_BY";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			logger.checkpoint(fname, "CONTRACT_TYPE,CARGO_NO,SEQ_NO,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DOM_BUY_FLAG, CARGO_REF_CD, CARGO_SEQ_NO, CONFIRM_VOL, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD FROM FMS9_PSEUDO_CARGO_DTL";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				row = spreadsheet.createRow(nrow++);
				ncell = 0;
				
				cell = row.createCell(ncell++);
				cell.setCellValue("'2'");
				
				for (int i = 1; i < columns.split(",").length; i++) {
					cell = row.createCell(ncell++);
					value = rset.getString(i) == null ? "null" : rset.getString(i);

					if(i==1) {
						value = rset.getString(1);
						if(value.equals("Y")) {
							value = "D";
						}
						cell.setCellValue("'" + value + "'");
					}else {
						cell.setCellValue("'" + value + "'");
					}
					
					
				}
				count++;
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_PSEUDO_CARGO_DTL_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 

			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_PSEUDO_CARGO_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	


public void FMS_CONT_PRICE_DTL() throws SQLException, IOException {
	
	function_nm = "FMS_CONT_PRICE_DTL(P)()";
	
	try {

		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_CONT_PRICE_DTL(P)>>,,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);

    	columns = "COMPANY_CD,CARGO_REF_CD,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,PRICE_TYPE,CURVE_NM,SLOPE,CONST,QUANTITY_UNIT,RATE,RATE_UNIT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,PHYS_CURVE_NM,CURVE_LOGIC,FORMULA";
    	
		workbook = new XSSFWorkbook();
		spreadsheet = workbook.createSheet("Sheet 1");

		nrow = 0;
		ncell = 0;
		count = 0;
		String seq_no="",cargo_ref_no="",dom_buy_flag="",sn_no="",sn_rev_no="",cont_type="";
		row = spreadsheet.createRow(nrow++);
		
		// Inserting Column Names
		for (int i = 0; i < columns.split(",").length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columns.split(",")[i]);
		}
		// Inserting Rest of the data
		queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
		stmt = conn.prepareStatement(queryString);
		rset = stmt.executeQuery();
		
		logger.checkpoint(fname, "CARGO_REF_CD,CONT_TYPE,SEQ_NO,TIMESTAMP", conn);
		
		while (rset.next()) {
			
			
			query_ind = 1;
			
			queryString1 = "SELECT A.MAN_CD, B.TRADER_CD FROM FMS7_MAN_REQ_MST A, FMS7_TRADER_MST B WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? ";
			
			queryString1 += "  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))   ORDER BY MAN_CD";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(query_ind++, rset.getString(2));
			stmt1.setString(query_ind++, delta_FromDt);
			stmt1.setString(query_ind++, delta_FromDt);
			stmt1.setString(query_ind++, delta_ToDt);
			stmt1.setString(query_ind++, delta_ToDt);
			rset1 = stmt1.executeQuery();
			while(rset1.next()) {
				
				queryString3 = "SELECT MAN_CONF_CD,CARGO_SEQ_NO,CARGO_REF_CD,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CD = ? AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, rset1.getString(1));
				stmt3.setString(2, delta_FromDt);
				stmt3.setString(3, delta_FromDt);
				stmt3.setString(4, delta_ToDt);
				stmt3.setString(5, delta_ToDt);
				rset3 = stmt3.executeQuery();
				
				while (rset3.next()) {
					cont_type=rset3.getString(4);
					if(cont_type==null) {
						cont_type="N";
					}
					else if(cont_type.equals("T")) {
						cont_type="Y";
					}
			    	
					queryString2 = " SELECT MAPPING_ID, NULL, SEQ_NO, TO_CHAR(FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(TO_DT, 'DD/MM/YYYY HH24:MI:SS'), NVL(PRICE_TYPE, ''), "
							+ "CURVE_NM, SLOPE, CONST, QUANTITY_UNIT, RATE, RATE_UNIT, NULL, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL,"
							+ " PRICE_RANGE, TO_CHAR(PRICE_START_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(PRICE_END_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "PHYS_CURVE_NM, NULL, NVL(REMARKS, ' ') FROM FMS9_MRCR_CONT_PRICE_DTL WHERE MAPPING_ID = ? AND "
							+ "(? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, (rset1.getString(2)+"-"+rset3.getString(1)+"-"+rset3.getString(2)+"-"+rset3.getString(3)+"-0"));
					stmt2.setString(2, delta_FromDt);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_ToDt);
					stmt2.setString(5, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
//						System.out.println("=>cargoref_cd"+rset3.getString(3));
						
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim();
						cell = row.createCell(ncell++);
						
						
						
						queryString4= " SELECT SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG= ? ORDER BY CUSTOMER_CD,DOM_BUY_FLAG,SN_NO,SN_REV_NO DESC";
						stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1,"%"+rset3.getString(3)+"%");
						stmt4.setString(2,cont_type);
						rset4 = stmt4.executeQuery();
						if (rset4.next()) {
							do {
								sn_no=rset4.getString(1);
								sn_rev_no=rset4.getString(2);
							}while(rset4.next());
						}else {
							sn_no="0";
							sn_rev_no="0";
						}
						stmt4.close();
						rset4.close();

//						stmt3.setString(1,"%"+cargo_ref_cd+"%");

						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value);  
						 }
						 if (value.contains("RIL")) {
							 value = "RIL";
						 }
						 if (value.contains("AMNS-T") || value.contains("AM/NS-T")) {
							 value = "AMNS";
						 }
						 dom_buy_flag = rset3.getString(4);
							if (dom_buy_flag == null) {
								dom_buy_flag = "N";  // Handle null first
							}else if (dom_buy_flag.equals("Y")) {
								dom_buy_flag = "D";
							} else if (dom_buy_flag.equals("K")) {
								dom_buy_flag = "I";
							} else if (dom_buy_flag.equals("T")) {
								dom_buy_flag = "T";
							}else if (dom_buy_flag.equals("N")) {
								dom_buy_flag = "N";
							}
							
						 cell.setCellValue("'"+value+"-"+sn_no+"-"+rset3.getString(2)+"-"+dom_buy_flag+"-"+rset3.getString(3)+"-"+sn_rev_no+"'");
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							
							if (i == 1) {
								cell.setCellValue("'"+rset3.getString(3)+"'");
								cargo_ref_no=rset3.getString(3);
							}
							else if(i==2) {
								dom_buy_flag = rset3.getString(4);
								if (dom_buy_flag == null) {
									dom_buy_flag = "N";  // Handle null first
								}else if (dom_buy_flag.equals("Y")) {
									dom_buy_flag = "D";
								} else if (dom_buy_flag.equals("K")) {
									dom_buy_flag = "I";
								} else if (dom_buy_flag.equals("T")) {
									dom_buy_flag = "T";
								}else if (dom_buy_flag.equals("N")) {
									dom_buy_flag = "N";
								}
								

								cell.setCellValue("'"+dom_buy_flag+"'");
							}
							else if(i == 3) {	// seq_no
								seq_no = rset2.getString(3);
								cell.setCellValue("'"+seq_no+"'");
							}
							else if(i == 13 && (rset2.getString(6).equals("F") || !rset2.getString(23).contains("@"))) {	// Remarks
								value = rset2.getString(23);
								cell.setCellValue("'"+value+"'");
							}
							/*else if ((i == 14) && !value.equals("null")) {	// emp_abbr
								value = getEmpAbbr(value);
								cell.setCellValue("'"+value+"'");
							}*/
							else if (i == 18 && !value.equals("null")) {	// Price Range
								if (value.equals("A") && rset2.getString(19) != null) {
									value = "D";
								}
								else if(value.length() > 1) {
									value = "O"+value.substring(1);
								}
								else if (rset2.getString(23).contains("MIN@") || rset2.getString(23).contains("MAX@")) {
									value = "null";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 22 && rset2.getString(6).equals("M")) {	// Curve_Logic
									if (rset2.getString(23) == null || !rset2.getString(23).contains("@")) {
										value = "SINGLE";
									}
									else {
										value = rset2.getString(23).split("@")[0];
									}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 23 && (rset2.getString(6).equals("F") || !rset2.getString(23).contains("@"))) {	// Formula
								value = "null";
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							
						}
						count++;
						logger.data(fname, (cargo_ref_no+ "," + " N " + "," + seq_no +","), conn, "");

					}
					rset2.close();
					stmt2.close();
				
				}
				rset3.close();
				stmt3.close();
				
			}
			rset1.close();
			stmt1.close();
		}
		stmt.close();
		rset.close();
		
		filename = migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL(P)_"+start_end_dt+".xlsx";
		
		fileOut = new FileOutputStream(filename);  
		
		workbook.write(fileOut);
		fileOut.close(); 
		
		logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
		logger.checkpoint(fname, "<<END>>,<<FMS_CONT_PRICE_DTL(P)>>,,", conn);
								
		logger.checkpoint1(fname1,count+",", conn);

		System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		System.out.println();

		msg = "Data has been Extracted Successfully.";
		msg_type = "S";
		
	}
	catch (Exception e) {

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
	
}

public void FMS_CONT_PRICE_MIN_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_CONT_PRICE_MIN_DTL(P)()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_CONT_PRICE_MIN_DTL(P)>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
	    	columns = "COMPANY_CD,CARGO_REF_NO,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,CURVE_LOGIC,CURVE_NM,SLOPE,CONST,QUANTITY_UNIT,RATE,RATE_UNIT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
	    	
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String curve_nm="",seq_no="",cargo_ref_no="",dom_buy_flag="",sn_no="",sn_rev_no="",cont_type="";
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
//			// Getting the start of cont_no
//			int no = 1000;
//			queryString = "SELECT TO_CHAR(SYSDATE, 'YYYY') FROM DUAL";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			if(rset.next()) {
//				no = no * Integer.parseInt("7" + rset.getString(1).substring(2));
//			}
//			rset.close();
//			stmt.close();
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL'  ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "CARGO_REF_CD,CONT_TYPE,SEQ_NO,CURVE_NM,TIMESTAMP", conn);
			
			while (rset.next()) {
				
				query_ind = 1;
				
				queryString1 = "SELECT A.MAN_CD, B.TRADER_CD FROM FMS7_MAN_REQ_MST A, FMS7_TRADER_MST B WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ?";
				
				queryString1 += "  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))   ORDER BY MAN_CD";

				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(query_ind++, rset.getString(2));
				stmt1.setString(query_ind++, delta_FromDt);
				stmt1.setString(query_ind++, delta_FromDt);
				stmt1.setString(query_ind++, delta_ToDt);
				stmt1.setString(query_ind++, delta_ToDt);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) {
				
					
					queryString3 = "SELECT MAN_CONF_CD, CARGO_SEQ_NO, CARGO_REF_CD,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CD = ? AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, rset1.getString(1));
					stmt3.setString(2, delta_FromDt);
					stmt3.setString(3, delta_FromDt);
					stmt3.setString(4, delta_ToDt);
					stmt3.setString(5, delta_ToDt);
					rset3 = stmt3.executeQuery();
					
					while (rset3.next()) {
				    	
						cont_type=rset3.getString(4);
						
						if(cont_type==null) {
							cont_type="N";
						}else if(cont_type.equals("T")) {
							cont_type="Y";
						}
						
						queryString2 = " SELECT MAPPING_ID, NULL, SEQ_NO, TO_CHAR(FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(TO_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, CURVE_NM, SLOPE, CONST, '1', NULL, NULL, PRICE_RANGE, TO_CHAR(PRICE_START_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(PRICE_END_DT, 'DD/MM/YYYY HH24:MI:SS'), REMARKS, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL FROM FMS9_MRCR_CONT_FIN_PRICE_DTL WHERE MAPPING_ID = ? AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, (rset1.getString(2)+"-"+rset3.getString(1)+"-"+rset3.getString(2)+"-"+rset3.getString(3)+"-0"));
						stmt2.setString(2, delta_FromDt);
						stmt2.setString(3, delta_FromDt);
						stmt2.setString(4, delta_ToDt);
						stmt2.setString(5, delta_ToDt);
						rset2 = stmt2.executeQuery();
						
						while (rset2.next()) {
							row = spreadsheet.createRow(nrow++);
							ncell = 0;
							
							value = rset.getString(1) == null ? "null" : rset.getString(1).trim();
							cell = row.createCell(ncell++);
							
							
							
							queryString4= " SELECT SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG= ? ORDER BY CUSTOMER_CD,DOM_BUY_FLAG,SN_NO,SN_REV_NO DESC";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1,"%"+rset3.getString(3)+"%");
							stmt4.setString(2,cont_type);
							rset4 = stmt4.executeQuery();
//							while (rset4.next()) {
//								sn_no=rset4.getString(1);
//								sn_rev_no=rset4.getString(2);
//							}
//							
							if (rset4.next()) {
								do {
									sn_no=rset4.getString(1);
									sn_rev_no=rset4.getString(2);
								}while(rset4.next());
							}else {
								sn_no="0";
								sn_rev_no="0";
							}
							stmt4.close();
							rset4.close();

//							stmt3.setString(1,"%"+cargo_ref_cd+"%");

							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value);  
							 }
							 if (value.contains("RIL")) {
								 value = "RIL";
							 }
							 if (value.contains("AMNS-T") || value.contains("AM/NS-T")) {
								 value = "AMNS";
							 }
							 dom_buy_flag = rset3.getString(4);
								if (dom_buy_flag == null) {
									dom_buy_flag = "N";  // Handle null first
								}else if (dom_buy_flag.equals("Y")) {
									dom_buy_flag = "D";
								} else if (dom_buy_flag.equals("K")) {
									dom_buy_flag = "I";
								} else if (dom_buy_flag.equals("T")) {
									dom_buy_flag = "T";
								}else if (dom_buy_flag.equals("N")) {
									dom_buy_flag = "N";
								}
								
							 cell.setCellValue("'"+value+"-"+sn_no+"-"+rset3.getString(2)+"-"+dom_buy_flag+"-"+rset3.getString(3)+"-"+sn_rev_no+"'");
							
														
							for (int i = 1; i < columns.split(",").length; i++) {
								cell = row.createCell(ncell++);
								value = rset2.getString(i) == null ? "null" : rset2.getString(i);
								
								if (i == 1) {	// Mapping_Id
									cell.setCellValue("'"+rset3.getString(3)+"'");
									cargo_ref_no=rset3.getString(3);
								}
								
								else if(i==2) {
									dom_buy_flag = rset3.getString(4);
									if (dom_buy_flag == null) {
										dom_buy_flag = "N";  // Handle null first
									}else if (dom_buy_flag.equals("Y")) {
										dom_buy_flag = "D";
									} else if (dom_buy_flag.equals("K")) {
										dom_buy_flag = "I";
									} else if (dom_buy_flag.equals("T")) {
										dom_buy_flag = "T";
									}else if (dom_buy_flag.equals("N")) {
										dom_buy_flag = "N";
									}
									

									cell.setCellValue("'"+dom_buy_flag+"'");
								}
								else if(i == 3) {	// seq_no
									seq_no = rset2.getString(3);
									cell.setCellValue("'"+seq_no+"'");
								}
								else if(i == 6 && rset2.getString(16)!=null) {	// Curve_Logic
									value = rset2.getString(16).split("@")[0];
									cell.setCellValue("'"+value+"'");
								}
								else if(i == 7) {	// Curve_Nm
									curve_nm = rset2.getString(7);
									cell.setCellValue("'"+curve_nm+"'");
								}
								else if (i == 13 && !value.equals("null")) {	// Price Range
									if (value.equals("A") && rset2.getString(14) != null) {
										value = "D";
									}
									else if(value.length() > 1 && !value.contains("@")) {
										value = "O"+value.substring(1);
									}
									cell.setCellValue("'"+value+"'");
								}
								/*else if ((i == 17) && !value.equals("null")) {	// emp_abbr
									value = getEmpAbbr(value);
									cell.setCellValue("'"+value+"'");
								}*/
								else {
									cell.setCellValue("'"+value+"'");
								}
								
							}
							count++;
							logger.data(fname, (cargo_ref_no + "," + " N " + "," + seq_no +","+ curve_nm +","), conn, "");
						}
						rset2.close();
						stmt2.close();
					
					}
					rset3.close();
					stmt3.close();
					
				}
				rset1.close();
				stmt1.close();
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL(P)_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_CONT_PRICE_MIN_DTL(P)>>,,,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_CARGO_SVC_CONT_MST() throws SQLException, IOException {
		
		function_nm = "FMS_CARGO_SVC_CONT_MST()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_CARGO_SVC_CONT_MST>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
	    	columns = "COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,CONT_NO,CONTRACT_TYPE,CONT_REF_NO,CONT_NAME,CONT_STATUS,START_DT,END_DT,PROV_SVC_RATE,PROV_SVC_RATE_UNIT1,PROV_SVC_RATE_UNIT2,FINAL_SVC_RATE,FINAL_SVC_RATE_UNIT1,FINAL_SVC_RATE_UNIT2,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,SIGNING_DT,SIGNING_TIME";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;
			String no="";

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// Below block of code is for inserting data 
			
			// SURVEYOR
			queryString1 = "SELECT DISTINCT(A.SURVEYOR_ABBR), '2', 'S', TO_CHAR(SYSDATE, 'YYYY'), 'Y', 'CONT-SURV-0', 'SEIPL', 'Y', '01/01/2007 00:00:00', '31/12/2026 00:00:00', B.SERVICE_CHARGE, '1', '0', B.SERVICE_CHARGE, '1', '0', TO_CHAR(B.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), B.EMP_CD, NULL, NULL, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), '00:00'  FROM FMS7_SURVEYOR_MST A, FMS7_CARGO_ENTITY_SERV_CHARGES B WHERE A.SURVEYOR_CD = B.ENTITY_CD AND B.ENTITY_TYPE = 'S' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))   ORDER BY A.SURVEYOR_ABBR ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_FromDt);
			stmt1.setString(6, delta_FromDt);
			stmt1.setString(7, delta_ToDt);
			stmt1.setString(8, delta_ToDt);
			rset1 = stmt1.executeQuery();
						
			int seq_no = 0;	
			
			logger.checkpoint(fname, "CD,ENTITY_TYPE,CONT_NO,CONT_TYPE,TIMESTAMP", conn);
			
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				seq_no++;
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replace("'", "");
					
					if(i == 0) {	// Entity Type with Abbr
						value = rset1.getString(1).trim();
						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value);  
						 }
						value = rset1.getString(3) + value;
						
					}
					else if (i == 3) {	// cont_no
						no = (1000 * Integer.parseInt("3" + value.substring(2)) + seq_no) + "";
					}
					else if (i == 5) {	// cont_ref_no
						value = value + seq_no;
					}
					else if (i == 6) {	// cont_name
						value = rset1.getString(1) + "-" + value + "-" + rset1.getString(5) + (1000 * Integer.parseInt("3" + rset1.getString(4).substring(2)) + seq_no);
					}
					/*else if (i == 17 && !value.equals("null")) {	// emp_abbr
						value = getEmpAbbr(value);
					}*/
					cell.setCellValue("'"+value+"'");
				}
				
				count++;
				logger.data(fname, ("2" + "," + " S " + "," + no + "," +" Y "+","), conn, "");
				
			}
			stmt1.close();
			rset1.close();
			
			// VESSEL
			queryString1 = "SELECT DISTINCT(A.VESSEL_ABBR), '2', 'V', TO_CHAR(SYSDATE, 'YYYY'), 'A', 'CONT-VA-0', 'SEIPL', 'Y', '01/01/2007 00:00:00', '31/12/2026 00:00:00', B.SERVICE_CHARGE, '1', '5', B.SERVICE_CHARGE, '1', '5', TO_CHAR(B.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), B.EMP_CD, NULL, NULL, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), '00:00'  FROM FMS7_VESSEL_AGENT_MST A, FMS7_CARGO_ENTITY_SERV_CHARGES B WHERE A.VESSEL_CD = B.ENTITY_CD AND B.ENTITY_TYPE = 'V' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))   ORDER BY A.VESSEL_ABBR ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_FromDt);
			stmt1.setString(6, delta_FromDt);
			stmt1.setString(7, delta_ToDt);
			stmt1.setString(8, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			seq_no = 0;	
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				seq_no++;
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replace("'", "");
					
					if(i == 0) {	// Entity Type with Abbr
						value = rset1.getString(1).trim();
						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value);  
						 }
						value = rset1.getString(3) + value;
						
					}
					else if (i == 3) {	// cont_no
						no = (1000 * Integer.parseInt("2" + value.substring(2)) + seq_no) + "";
					}
					else if (i == 5) {	// cont_ref_no
						value = value + seq_no;
					}
					else if (i == 6) {	// cont_name
						value = rset1.getString(1) + "-" + value + "-" + rset1.getString(5) + (1000 * Integer.parseInt("2" + rset1.getString(4).substring(2)) + seq_no);
					}
					/*else if (i == 17 && !value.equals("null")) {	// emp_abbr
						value = getEmpAbbr(value);
					}*/
					cell.setCellValue("'"+value+"'");
				}
				
				count++;
				logger.data(fname, ("2" + "," + " V " + "," + no + "," +" A "+","), conn, "");				
			}
			stmt1.close();
			rset1.close();
			
			// CHA
			queryString1 = "SELECT DISTINCT(A.CHA_ABBR), '2', 'H', TO_CHAR(SYSDATE, 'YYYY'), 'H', 'CONT-CHA-0', 'SEIPL', 'Y', '01/01/2007 00:00:00', '31/12/2026 00:00:00', B.SERVICE_CHARGE, '1', '5', B.SERVICE_CHARGE, '1', '5',TO_CHAR(B.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), B.EMP_CD, NULL, NULL, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), '00:00'  FROM FMS7_CUS_HOUSE_AGENT_MST A, FMS7_CARGO_ENTITY_SERV_CHARGES B WHERE A.CHA_CD = B.ENTITY_CD AND B.ENTITY_TYPE = 'C' AND B.SERVICE_CHARGE_DT = (SELECT MAX(C.SERVICE_CHARGE_DT) FROM FMS7_CARGO_ENTITY_SERV_CHARGES C WHERE C.ENTITY_CD = B.ENTITY_CD AND C.ENTITY_TYPE = 'C') AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))   ORDER BY A.CHA_ABBR ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_FromDt);
			stmt1.setString(6, delta_FromDt);
			stmt1.setString(7, delta_ToDt);
			stmt1.setString(8, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			seq_no = 0;	
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				seq_no++;
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replace("'", "");
					
					if(i == 0) {	// Entity Type with Abbr
						value = rset1.getString(1).trim();
						if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value);  
						}	
						value = rset1.getString(3) + value;
									
					}
					else if (i == 3) {	// cont_no
						no = (1000 * Integer.parseInt("1" + value.substring(2)) + seq_no) + "";
					}
					else if (i == 5) {	// cont_ref_no
						value = value + seq_no;
					}
					else if (i == 6) {	// cont_name
						value = rset1.getString(1) + "-" + value + "-" + rset1.getString(5) + (1000 * Integer.parseInt("1" + rset1.getString(4).substring(2)) + seq_no);
					}
					/*else if (i == 17 && !value.equals("null")) {	// emp_abbr
						value = getEmpAbbr(value);
					}*/
					cell.setCellValue("'"+value+"'");
				}
				
				count++;
				logger.data(fname, ("2" + "," + " H " + "," + no + "," +" H "+","), conn, "");				
			}
			stmt1.close();
			rset1.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_CARGO_SVC_CONT_MST>>,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_CARGO_SVC_CONT_SVC_BU() throws SQLException, IOException {
		
		function_nm = "FMS_CARGO_SVC_CONT_SVC_BU()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_CARGO_SVC_CONT_SVC_BU>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
	    	columns = "COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,CONT_NO,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;
			String no="";

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// Below block of code is for inserting data 
			
			// SURVEYOR
			queryString1 = "SELECT DISTINCT(A.SURVEYOR_ABBR), '2', 'S', TO_CHAR(SYSDATE, 'YYYY'), 'Y', '1', B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), B.SERVICE_CHARGE  FROM FMS7_SURVEYOR_MST A, FMS7_CARGO_ENTITY_SERV_CHARGES B WHERE A.SURVEYOR_CD = B.ENTITY_CD AND B.ENTITY_TYPE = 'S' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))   ORDER BY A.SURVEYOR_ABBR ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_FromDt);
			stmt1.setString(6, delta_FromDt);
			stmt1.setString(7, delta_ToDt);
			stmt1.setString(8, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			int seq_no = 0;	
			
			logger.checkpoint(fname, "CD,CONT_NO,CONT_TYPE,PLANT_SEQ_NO,TIMESTAMP", conn);
			
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				seq_no++;
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replace("'", "");
					
					if(i == 0) {	// Entity Type with Abbr
						value = rset1.getString(1).trim();
						if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value);  
						}
						value = rset1.getString(3) + value;
						
					}
					else if (i == 3) {	// cont_no
						no = (1000 * Integer.parseInt("3" + value.substring(2)) + seq_no) + "";
					}
					/*else if (i == 6 && !value.equals("null")) {	// emp_abbr
						value = getEmpAbbr(value);
					}*/
					cell.setCellValue("'"+value+"'");
				}
				count++;
				logger.data(fname, ("2" + "," + no + "," + "Y" + "," +" 1 "+","), conn, "");
			}
			stmt1.close();
			rset1.close();
			
			// VESSEL
			queryString1 = "SELECT DISTINCT(A.VESSEL_ABBR), '2', 'V', TO_CHAR(SYSDATE, 'YYYY'), 'A', '1', B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), B.SERVICE_CHARGE  FROM FMS7_VESSEL_AGENT_MST A, FMS7_CARGO_ENTITY_SERV_CHARGES B WHERE A.VESSEL_CD = B.ENTITY_CD AND B.ENTITY_TYPE = 'V' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))   ORDER BY A.VESSEL_ABBR ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_FromDt);
			stmt1.setString(6, delta_FromDt);
			stmt1.setString(7, delta_ToDt);
			stmt1.setString(8, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			seq_no = 0;	
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				seq_no++;
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replace("'", "");
					
					if(i == 0) {	// Entity Type with Abbr
						value = rset1.getString(1).trim();
						if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value);  
					    }
						value = rset1.getString(3) + value;
						
					}
					else if (i == 3) {	// cont_no
						value = (1000 * Integer.parseInt("2" + value.substring(2)) + seq_no) + "";
					}
					/*else if (i == 6 && !value.equals("null")) {	// emp_abbr
						value = getEmpAbbr(value);
					}*/
					cell.setCellValue("'"+value+"'");
				}
				count++;
				logger.data(fname, ("2" + "," + no + "," + "A" + "," +" 1 "+","), conn, "");
			}
			stmt1.close();
			rset1.close();
			
			// CHA
			queryString1 = "SELECT DISTINCT(A.CHA_ABBR), '2', 'H', TO_CHAR(SYSDATE, 'YYYY'), 'H', '1', B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), B.SERVICE_CHARGE  FROM FMS7_CUS_HOUSE_AGENT_MST A, FMS7_CARGO_ENTITY_SERV_CHARGES B WHERE A.CHA_CD = B.ENTITY_CD AND B.ENTITY_TYPE = 'C' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))   ORDER BY A.CHA_ABBR ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_FromDt);
			stmt1.setString(6, delta_FromDt);
			stmt1.setString(7, delta_ToDt);
			stmt1.setString(8, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			seq_no = 0;	
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				seq_no++;
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replace("'", "");
					
					if(i == 0) {	// Entity Type with Abbr
						value = rset1.getString(1).trim();
						if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value);  
					     }
						value = rset1.getString(3) + value;
						
					}
					else if (i == 3) {	// cont_no
						value = (1000 * Integer.parseInt("1" + value.substring(2)) + seq_no) + "";
					}
					/*else if (i == 6 && !value.equals("null")) {	// emp_abbr
						value = getEmpAbbr(value);
					}*/
					cell.setCellValue("'"+value+"'");
				}
				count++;
				logger.data(fname, ("2" + "," + no + "," + "H" + "," +" 1 "+","), conn, "");
			}
			stmt1.close();
			rset1.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_SVC_BU_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_CARGO_SVC_CONT_SVC_BU>>,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_BUY_CARGO_NOM() throws SQLException, IOException {
		
		function_nm = "FMS_BUY_CARGO_NOM()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_BUY_CARGO_NOM>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);

	    	columns = "COMPANY_CD,CONT_REF_N0,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,SHIP_CD,EXP_DELV_QTY,EXP_FROM_DT,EXP_TO_DT,COUNTRY_ORIGIN,LOAD_PORT,REMARK,SPLIT_BOL,NUM_BL,NUM_BOE,LIQUEFAC_PLANT,LIQUEFAC_COUNTRY,LIQUEFAC_PROMOTOR,LIQUEFAC_REMARK,ENT_BY,ENT_DT,FLAG,LINKED_SURVEYOR_CONT,LINKED_CHAGENT_CONT,LINKED_VAGENT_CONT";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String agmt_no="",cont_no="",cargo_no="",man_cd="";
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			

			logger.checkpoint(fname, "ABBR,MAN_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,TIMESTAMP", conn);

			
			while (rset.next()) {
			
				queryString1 = "SELECT A.TRD_CD, 'M', A.MAN_CD, '0', 'N', A.MAN_CONF_CD, '0', A.CARGO_REF_CD, '0', A.SHIP_CD, A.EXP_DELV_QTY,"
						+ " TO_CHAR(A.EXP_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.EXP_TO_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ " A.COUNTRY_ORIGIN, A.LOAD_PORT, A.REMARK, NULL, '1', '1', A.LIQUEFAC_PLANT, A.LIQUEFAC_COUNTRY,"
						+ " A.LIQUEFAC_PROMOTOR, A.LIQUEFAC_REMARK, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, A.SURV_CD, A.CHA_CD, A.VA_CD "
						+ "FROM FMS7_CARGO_NOMINATION A, FMS7_TRADER_MST B WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ?  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))    ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					value = rset.getString(1) == null ? "null" : rset.getString(1).trim();
					abbr=value;
					cell = row.createCell(ncell++);
					
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        abbr=value;
					    }
					 if (abbr.contains("RIL")) {
						 abbr = "RIL";
					 }
					 cell.setCellValue("'"+abbr+"'");
					
					cell = row.createCell(ncell++);
					
					man_cd = rset1.getString(8);
					cell.setCellValue("'"+man_cd+"'");
					
					for(int i = 2; i < columns.split(",").length; i++) {

						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						cell = row.createCell(i);
						
						 if(i == 3) {
							agmt_no = rset1.getString(6) == null ? "null" : rset1.getString(6);
							cell.setCellValue("'"+agmt_no+"'");
						}
//						else if(i == 6){
//							cont_no = rset1.getString(6) == null ? "null" : rset1.getString(6);
//							cell.setCellValue("'"+cont_no+"'");
//						}
						else if(i == 8){
							
							cargo_no = rset1.getString(8) == null ? "null" : rset1.getString(8);
							cell.setCellValue("'"+cargo_no+"'");
						}
																	
						if(i == 10) {	// Ship_nm
							queryString2 = "SELECT SHIP_NAME FROM FMS7_SHIP_MST WHERE SHIP_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, value);
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								value = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();

							cell.setCellValue("'"+value+"'");
						}
						else if(i==15)
						{
							if(value.equals(" "))
							{
//								value=value.trim();
								value=null;
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 17) {	// Split_BOL
							queryString2 = "SELECT COUNT(*) FROM FMS7_CARGO_BL_DTL WHERE CARGO_REF_NO = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(8));
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								if (rset2.getInt(1) <= 1) {
									cell.setCellValue("'"+value+"'");
									
									i++;
									cell = row.createCell(i);
									value = rset2.getInt(1)+"";
									cell.setCellValue("'"+value+"'");
								}
								else {
									cell.setCellValue("'Y'");
									
									i++;
									cell = row.createCell(i);
									if (rset2.getInt(1) > 2) {
										value = "2";
									}
									else {
										value = rset2.getInt(1)+"";
									}
									cell.setCellValue("'"+value+"'");
								}
							}
							else {
								cell.setCellValue("'"+value+"'");
								
								i++;
								cell = row.createCell(i);
								value = rset2.getInt(1)+"";
								cell.setCellValue("'0'");
							}
							stmt2.close();
							rset2.close();
						}
						else if(i == 19) {	// BOE Count
							queryString2 = "SELECT COUNT(*) FROM FMS7_CARGO_ARRIVAL_DTL WHERE CARGO_REF_NO = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(8));
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								if (rset2.getInt(1) > 2) {
									value = "2";
								}
								else {
									value = rset2.getInt(1)+"";
								}
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							stmt2.close();
							rset2.close();
						}
						else if(i==22)
						{

							if(value.equals(" "))
							{
//								value=value.trim();
								value=null;
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 27) {	// Surveyor Abbr
							queryString2 = "SELECT SURVEYOR_ABBR FROM FMS7_SURVEYOR_MST WHERE SURVEYOR_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, value);
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								value = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value);  
							 }

							cell.setCellValue("'"+value+"'");
						}
						
						/*else if ((i == 24) && !value.equals("null")) {	// emp_abbr
							value = getEmpAbbr(value);
							cell.setCellValue("'"+value+"'");
						}*/
						else if(i == 28) {	// CHA Abbr
							queryString2 = "SELECT CHA_ABBR FROM FMS7_CUS_HOUSE_AGENT_MST WHERE CHA_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, value);
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								value = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value);  
							 }
							 
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 29) {	// VA Abbr
							queryString2 = "SELECT VESSEL_ABBR FROM FMS7_VESSEL_AGENT_MST WHERE VESSEL_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, value);
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								value = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value);  
							 }

							cell.setCellValue("'"+value+"'");
						}
						else {
							
								cell.setCellValue("'"+value+"'");
							
						}
						
					}
					count++;
					logger.data(fname, (abbr + "," +man_cd + "," + " M " + "," + agmt_no + "," +" 0 "+ "," +" N "+"," + cont_no +","+" 0 "+"," + cargo_no + "," + " 0 " + ","), conn, "");					
				}
				rset1.close();
				stmt1.close();
			
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BUY_CARGO_NOM>>,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_BUY_CARGO_NOM_BL() throws SQLException, IOException {
		
		function_nm = "FMS_BUY_CARGO_NOM_BL()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_BUY_CARGO_NOM_BL>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
						
			columns = "COMPANY_CD,CONT_REF_NO,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BL_NO,BL_QTY,BL_QTY_UNIT,BL_PRICE,BL_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,BL_QTY_MT,BL_QTY_SCM";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String agmt_no="",cont_no="",cargo_no="";
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL'  ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ABBR,MAN_CD,AGMT_TYPE,AGMT_NO,AGMR_REV,CONT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BL_NO,TIMESTAMP", conn);
			
			while (rset.next()) {
				
				queryString1 = "SELECT A.TRD_CD, 'M', A.MAN_CD, '0', 'N', A.MAN_CONF_CD, '0', A.CARGO_REF_CD, '0', A.SHIP_CD, A.EXP_DELV_QTY,"
						+ " TO_CHAR(A.EXP_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.EXP_TO_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ " A.COUNTRY_ORIGIN, A.LOAD_PORT, A.REMARK, NULL, '1', '1', A.LIQUEFAC_PLANT, A.LIQUEFAC_COUNTRY, "
						+ "A.LIQUEFAC_PROMOTOR, A.LIQUEFAC_REMARK, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ " NULL, A.SURV_CD, A.CHA_CD, A.VA_CD "
						+ "FROM FMS7_CARGO_NOMINATION A, FMS7_TRADER_MST B WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ?  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					queryString2 = "SELECT NULL, 'M', NULL, '0', 'N', NULL, '0', CARGO_REF_NO, '0', SPLIT_SEQ, QTY_MMBTU, NULL, NULL, '2', "
							+ "EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), FLAG, QTY_MT, QTY_SCM"
							+ " FROM FMS7_CARGO_BL_DTL A "
							+ "WHERE CARGO_REF_NO = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ORDER BY A.SPLIT_SEQ  DESC";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset1.getString(8));
					stmt2.setString(2, delta_FromDt);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_ToDt);
					stmt2.setString(5, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
//						if(rset2.getString(10).equals("1")) {	// For taking split_seq 1&2 data in case of custom duty non-applicable. 
//							nrow--;
//							count--;
//						}
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim();
						abbr=value;
						cell = row.createCell(ncell++);
						
						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        abbr=value;
						    }
						 if (abbr.contains("RIL")) {
							 abbr = "RIL";
						 }
						 cell.setCellValue("'"+abbr+"'");
						
						 cell = row.createCell(ncell++);
						
						 man_cd = rset1.getString(8);	
						cell.setCellValue("'"+man_cd+"'");
						
						for(int i = 2; i <= 9; i++) {

							value = rset1.getString(i) == null ? "null" : rset1.getString(i);
							cell = row.createCell(i);
							
							cell.setCellValue("'"+value+"'");
							
							if(i == 3) {
								agmt_no = rset1.getString(3) == null ? "null" : rset1.getString(3);
								cell.setCellValue("'"+agmt_no+"'");
							}
							else if(i == 6){
								cont_no = rset1.getString(6) == null ? "null" : rset1.getString(6);
								cell.setCellValue("'"+cont_no+"'");
							}
							else if(i == 8){
								cargo_no = rset1.getString(8) == null ? "null" : rset1.getString(8);
								cell.setCellValue("'"+cargo_no+"'");
							}
							
						}
						
						for(int i = 10; i < columns.split(",").length; i++) {

							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							cell = row.createCell(i);
							
							if((i == 10 && value.equals("0") )|| (i == 10 && value.equals("2") ) )  {	// BL No
								cell.setCellValue("'1'");
							}
							/*else if ((i == 15) && !value.equals("null")) {	// emp_abbr
								value = getEmpAbbr(value);
								cell.setCellValue("'"+value+"'");
							}*/
							else {
								cell.setCellValue("'"+value+"'");
							}
							
						}
						if(rset2.getString(10).equals("1") ) {	// For taking split_seq 1&2 data in case of custom duty non-applicable. 
							nrow--;
							count--;
						}
						
						count++;
						logger.data(fname, (abbr + "," + man_cd + "," + " M " + "," + agmt_no + "," +" 0 "+ "," +" N "+"," + cont_no +","+" 0 "+"," + cargo_no + "," + " 0 " + ","+ " 1 " + ","), conn, "");					
						
					}
					rset2.close();
					stmt2.close();
					
				}
				rset1.close();
				stmt1.close();
			
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_BL_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BUY_CARGO_NOM_BL>>,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_BUY_CARGO_NOM_BOE() throws SQLException, IOException {
		
		function_nm = "FMS_BUY_CARGO_NOM_BOE()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_BUY_CARGO_NOM_BOE>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,CONT_REF_NO,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BOE_NO,BU_SEQ,PLANT_SEQ,BOE_QTY,BOE_QTY_UNIT,BOE_PRICE,BOE_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,CUSTOM_DUTY,LOAD_PORT,LINKED_BL,BOE_QTY_MT,BOE_QTY_SCM";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String agmt_no="",cont_no="",cargo_no="",boe_qty="",arrv_dt="",unit_cd="";
			double conf_vol = 0;
			row = spreadsheet.createRow(nrow++);
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL'  ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ABBR,MAN_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BOE_NO,TIMESTAMP", conn);

			while (rset.next()) {
				
				queryString1 = "SELECT A.TRD_CD, 'M', A.MAN_CD, '0', 'N', A.MAN_CONF_CD, '0', A.CARGO_REF_CD, '0', A.SHIP_CD,"
						+ " A.EXP_DELV_QTY, TO_CHAR(A.EXP_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "TO_CHAR(A.EXP_TO_DT, 'DD/MM/YYYY HH24:MI:SS'), A.COUNTRY_ORIGIN, "
						+ "A.LOAD_PORT, A.REMARK, NULL, '1', '1', A.LIQUEFAC_PLANT, "
						+ "A.LIQUEFAC_COUNTRY, A.LIQUEFAC_PROMOTOR, A.LIQUEFAC_REMARK, "
						+ "A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, A.SURV_CD, A.CHA_CD, A.VA_CD "
						+ "FROM FMS7_CARGO_NOMINATION A, FMS7_TRADER_MST B "
						+ "WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					queryString2 = "SELECT DISTINCT(A.BE_NO), 'M', NULL, '0', 'N', NULL, '0', A.CARGO_REF_NO, '0', A.SPLIT_SEQ, '1', '1',"
							+ " A.SPLIT_SEQ, NULL, B.CONFIRM_PRICE  , '2', A.EMP_CD, "
							+ "TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.FLAG, 'Y', A.PORT_CODE, A.SPLIT_SEQ, A.EXP_QTY_MT, A.EXP_QTY_SCM "
							+ "FROM FMS7_CARGO_ARRIVAL_DTL A ,FMS7_CARGO_NOMINATION B WHERE CARGO_REF_NO = ?   "
							+ "AND  A.CARGO_REF_NO=B.CARGO_REF_CD  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ORDER BY A.SPLIT_SEQ DESC";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset1.getString(8));
					stmt2.setString(2, delta_FromDt);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_ToDt);
					stmt2.setString(5, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
						
						
						
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim();
						abbr=value;
						cell = row.createCell(ncell++);
						
						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        abbr=value;
						    }
						 if (abbr.contains("RIL")) {
							 abbr = "RIL";
						 }
						 cell.setCellValue("'"+abbr+"'");
						
						 cell = row.createCell(ncell++);
						
						 man_cd = rset1.getString(8);	
						cell.setCellValue("'"+man_cd+"'");
						
						for(int i = 2; i <= 9; i++) {

							value = rset1.getString(i) == null ? "null" : rset1.getString(i);
							cell = row.createCell(i);
							
							cell.setCellValue("'"+value+"'");
							
							if(i == 3) {
								agmt_no = rset1.getString(3) == null ? "null" : rset1.getString(3);
								cell.setCellValue("'"+agmt_no+"'");
							}
							else if(i == 6){
								cont_no = rset1.getString(6) == null ? "null" : rset1.getString(6);
								cell.setCellValue("'"+cont_no+"'");
							}
							else if(i == 8){
								cargo_no = rset1.getString(8) == null ? "null" : rset1.getString(8);
								cell.setCellValue("'"+cargo_no+"'");
							}
							
						}
						
						for(int i = 10; i < columns.split(",").length; i++) {

							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							cell = row.createCell(i);
							
							if((i == 10 ||  i == 22) && value.equals("0") || (i == 10 ||  i == 22) && value.equals("2") ) {	// BE No
								cell.setCellValue("'1'");
							}
							else if(i ==13) //mmbtu
							{
								//boe_qty 
//								taking arrv_dt
								
								queryString3 = "SELECT TO_CHAR(ACT_ARRV_DT,'DD/MM/YYYY')   "
										+ " FROM FMS7_CARGO_ARRIVAL_DTL "
										+ "WHERE CARGO_REF_NO = ? AND "
										+ "(? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
										+ " AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
										+ "ORDER BY SPLIT_SEQ DESC";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, rset2.getString(8));
								stmt3.setString(2, delta_FromDt);
								stmt3.setString(3, delta_FromDt);
								stmt3.setString(4, delta_ToDt);
								stmt3.setString(5, delta_ToDt);
								rset3 = stmt3.executeQuery();
								if (rset3.next()) {
									arrv_dt=rset3.getString(1);
									
								}
								
								rset3.close();
								stmt3.close();
								
								
								
								if(arrv_dt!=null )
								{
									Date arrv_dt1=new Date(arrv_dt);
									Date d1=new Date("01/12/2014");
									if(arrv_dt1.after(d1))
									{
										queryString3 = "SELECT QTY_MMBTU, QTY_MT, QTY_SCM  "
												+ " FROM FMS7_CARGO_BL_DTL "
												+ "WHERE CARGO_REF_NO = ? AND "
												+ "(? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
												+ " AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
												+ "ORDER BY SPLIT_SEQ DESC";
										stmt3 = conn.prepareStatement(queryString3);
										stmt3.setString(1, rset1.getString(8));
										stmt3.setString(2, delta_FromDt);
										stmt3.setString(3, delta_FromDt);
										stmt3.setString(4, delta_ToDt);
										stmt3.setString(5, delta_ToDt);
										rset3 = stmt3.executeQuery();
										if (rset3.next()) {
											boe_qty=rset3.getString(1);
											
										}
										rset3.close();
										stmt3.close();
										cell.setCellValue("'"+boe_qty+"'");
									}
									else
									{
											queryString3 = "SELECT A.CONFIRM_VOL,B.UNIT_CD FROM FMS7_CARGO_NOMINATION A ,FMS7_MAN_CONFIRM_CARGO_DTL B "
													+ " WHERE  A.CARGO_REF_CD=B.CARGO_REF_CD  AND A.CARGO_REF_CD= ? ";
											stmt3 = conn.prepareStatement(queryString3);
											stmt3.setString(1, rset1.getString(8));
											rset3 = stmt3.executeQuery();
											if(rset3.next())
											{
												conf_vol= rset3.getDouble(1);	
												unit_cd= rset3.getString(2);	
											}
											
											//converting tbtuto mmbtu
											if(unit_cd.equals("2"))
											{
												 BigDecimal conf_vol1 = BigDecimal.valueOf(Math.round(conf_vol*1000000));
												 conf_vol1  = conf_vol1.setScale(0, RoundingMode.CEILING);
												 cell.setCellValue("'"+conf_vol1+"'");
												 
											}else
											{
												cell.setCellValue("'"+conf_vol+"'");
											}
											rset3.close();
											stmt3.close();
										
									}
									
									
									
								}
								else
								{
									cell.setCellValue("'"+null+"'");
								}
							}
						
							else if(i == 20 && rset2.getString(10).equals("2")) {	// Custom Duty
								value = "N";
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						if(rset2.getString(10).equals("1") ) {	// For taking split_seq 1&2 data in case of custom duty non-applicable. 
							nrow--;
							count--;
						}
						
						count++;
						logger.data(fname, (abbr + "," + man_cd + "," + " M " + "," + agmt_no + "," +" 0 "+ "," +" N "+"," + cont_no +","+" 0 "+"," + cargo_no + "," + " 0 " + ","+ " 1 " + ","), conn, "");					
					}
					rset2.close();
					stmt2.close();
					
				}
				rset1.close();
				stmt1.close();
			
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_BOE_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BUY_CARGO_NOM_BOE>>,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);
			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_BUY_CARGO_ALLOC() throws SQLException, IOException {
		
		function_nm = "FMS_BUY_CARGO_ALLOC()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_BUY_CARGO_ALLOC>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
	    	columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,SHIP_CD,ACT_ARRV_DT,BOOKED_DT,BOOKED_TIME,FLOAT_DT,FLOAT_TIME,PILOT_ON_BOARD_DT,PILOT_ON_BOARD_TIME,UNLOAD_ARM_CON_DT,UNLOAD_ARM_CON_TIME,UNLOAD_ARM_DIS_CON_DT,UNLOAD_ARM_DIS_CON_TIME,DISCHARGE_DT,DISCHARGE_TIME,REMARK,QQ_NO,QQ_DT,QQ_QTY_MMBTU,QQ_QQ_QTY_MT,QQ_QQ_QTY_SCM,QQ_DENSITY,QQ_GHV,QQ_GCV,QQ_REMARK,ACT_QTY_MMBTU,ENT_BY,ENT_DT,ALL_FAST_DT,ALL_FAST_TIME,CUSTOME_CLEARANCE_START_DT,CUSTOME_CLEARANCE_START_TIME,CUSTOME_CLEARANCE_END_DT,CUSTOME_CLEARANCE_END_TIME";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String cd="",cont_no="",act_arr_dt="";
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// Inserting Rest of the data(TO GET ABBR)
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
					
			logger.checkpoint(fname, "ABBR,MAN_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,TIMESTAMP", conn);
			while (rset.next()) {
			// Inserting Rest of the data
			
			queryString1 = "SELECT A.CARGO_REF_NO, A.CARGO_REF_NO, 'M', A.CARGO_REF_NO, '0', 'N', A.CARGO_REF_NO, '0', "
					+ "A.CARGO_REF_NO, '0', A.VESSEL_NM, TO_CHAR(A.ACT_ARRV_DT, 'DD/MM/YYYY HH24:MI:SS'), "
					+ "TO_CHAR(A.BOOKED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.BOOKED_TIME, TO_CHAR(A.FLOAT_DT, 'DD/MM/YYYY HH24:MI:SS'),"
					+ " A.FLOAT_TIME, TO_CHAR(A.PILOT_ON_BOARD_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PILOT_ON_BOARD_TIME,"
					+ " TO_CHAR(A.UNLOAD_ARM_CON_DT, 'DD/MM/YYYY HH24:MI:SS'), A.UNLOAD_ARM_CON_TIME, "
					+ "TO_CHAR(A.UNLOAD_ARM_DIS_CON_DT, 'DD/MM/YYYY HH24:MI:SS'),"
					+ " A.UNLOAD_ARM_DIS_CON_TIME, TO_CHAR(A.DISCHARGE_DT, 'DD/MM/YYYY HH24:MI:SS'),"
					+ " A.DISCHARGE_TIME, A.REMARK, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, "
					+ "A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, NULL, NULL, NULL"
					+ " FROM FMS7_CARGO_ARRIVAL_DTL A, FMS7_CARGO_NOMINATION B, FMS7_TRADER_MST C"
					+ " WHERE A.CARGO_REF_NO = B.CARGO_REF_CD AND B.TRD_CD = C.TRADER_CD AND C.COUNTERPARTY_CD = ? "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, rset.getString(2));
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_FromDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			logger.checkpoint(fname, "CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,TIMESTAMP", conn);

			while (rset1.next()) {
//				row = spreadsheet.createRow(nrow++);
//				
//				comp_cd = "2";
//				cell = row.createCell(0);
//				cell.setCellValue("'"+comp_cd+"'");
				
				row = spreadsheet.createRow(nrow++);
				ncell = 0;
				value = rset.getString(1) == null ? "null" : rset.getString(1).trim();
				abbr=value;
				cell = row.createCell(ncell++);
				
				 if (mpe.counterparty_map.containsKey(value)) {
				        value =mpe.counterparty_map.get(value); 
				        abbr=value;
				    }
				 if (abbr.contains("RIL")) {
					 abbr = "RIL";
				 }
				 cell.setCellValue("'"+abbr+"'");
				
				for(int i = 1; i < columns.split(",").length; i++) {
					value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
					cell = row.createCell(i);

					if(i == 1) {
						cd = rset1.getString(1) == null ? "null" : rset1.getString(1);
						cell.setCellValue("'"+cd+"'");
					}					
					else if(i == 6){
						cont_no = rset1.getString(7) == null ? "null" : rset1.getString(7);
						cell.setCellValue("'"+cont_no+"'");
					}					
					
					if(i == 25) {	// QQ details QQ_NO,QQ_DT,QQ_QTY_MMBTU,QQ_QQ_QTY_MT,QQ_QQ_QTY_SCM,QQ_DENSITY,QQ_GHV,QQ_GCV,QQ_REMARK,ACT_QTY_MMBTU
						queryString2 = "SELECT QQ_NO, TO_CHAR(QQ_DT, 'DD/MM/YYYY HH24:MI:SS'), QTY_MMBTU, QTY_MT, QTY_SCM, DENSITY, GHV, GCV, REMARK, QTY_MMBTU "
								+ "FROM FMS7_CARGO_QQ_DTL WHERE CARGO_REF_NO = ? AND "
								+ "(? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
								+ " AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY SPLIT_SEQ DESC";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, rset1.getString(1));
						stmt2.setString(2, delta_FromDt);
						stmt2.setString(3, delta_FromDt);
						stmt2.setString(4, delta_ToDt);
						stmt2.setString(5, delta_ToDt);
						rset2 = stmt2.executeQuery();
						if (rset2.next()) {
							
							for (int j = 1; j < 10; j++) {
								if(j != 1) {
									cell = row.createCell(++i);
								}
								value = rset2.getString(j);
								cell.setCellValue("'"+value+"'");
							}
							
						}
						rset2.close();
						stmt2.close();
						
						

						
					}
					else if(i==34) //Total Actual Unloaded Quantity of LNG:
					{
						queryString2 = "SELECT  QTY_MMBTU "
								+ "FROM FMS7_CARGO_QQ_DTL WHERE CARGO_REF_NO = ? AND "
								+ "(? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
								+ " AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY SPLIT_SEQ ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, rset1.getString(1));
						stmt2.setString(2, delta_FromDt);
						stmt2.setString(3, delta_FromDt);
						stmt2.setString(4, delta_ToDt);
						stmt2.setString(5, delta_ToDt);
						rset2 = stmt2.executeQuery();
						if (rset2.next()) {
							
						value = rset2.getString(1);
						cell.setCellValue("'"+value+"'");
							
						}
						rset2.close();
						stmt2.close();
					}
					else if(i==11)//act_aarv_dt
					{
						act_arr_dt=rset1.getString(12);
//						System.out.println(""+act_arr_dt+":"+rset1.getString(1));
						cell.setCellValue("'"+act_arr_dt+"'");
					}
					else {
						cell.setCellValue("'"+value+"'");
					}
					
				}
				
				if(act_arr_dt==null)
				{
					nrow--;
					count--;
				}
				count++;
				logger.data(fname, (cd + "," + " M " + "," + cont_no + "," +" 0 "+ "," +" N "+"," + cont_no +","+" 0 "+"," + cont_no + "," + " 0 " + ","), conn, "");					
			}
			rset1.close();
			stmt1.close();
			
			}
			rset.close();
			stmt.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BUY_CARGO_ALLOC>>,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_BUY_CARGO_ALLOC_BL() throws SQLException, IOException {
		
		function_nm = "FMS_BUY_CARGO_ALLOC_BL()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_BUY_CARGO_ALLOC_BL>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,CONT_REF_NO,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BL_NO,BL_REF,BL_DT,IMPORT_DEPT_SNO,IMPORT_CD,ENDORSE_DT,REMARK,ENT_BY,ENT_DT,FLAG";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;

			count = 0;
			String cont_no="";
			
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL'  ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ABBR,MAN_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BL_NO,TIMESTAMP", conn);
			
			while (rset.next()) {
				
				queryString1 = "SELECT A.TRD_CD, 'M', A.MAN_CD, '0', 'N', A.MAN_CONF_CD, '0', A.CARGO_REF_CD, '0',"
						+ " A.SHIP_CD, A.EXP_DELV_QTY, TO_CHAR(A.EXP_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "TO_CHAR(A.EXP_TO_DT, 'DD/MM/YYYY HH24:MI:SS'), A.COUNTRY_ORIGIN, A.LOAD_PORT, "
						+ "A.REMARK, NULL, '1', '1', A.LIQUEFAC_PLANT, A.LIQUEFAC_COUNTRY, A.LIQUEFAC_PROMOTOR,"
						+ " A.LIQUEFAC_REMARK, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "NULL, A.SURV_CD, A.CHA_CD, A.VA_CD "
						+ "FROM FMS7_CARGO_NOMINATION A, FMS7_TRADER_MST B  WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					queryString2 = "SELECT DISTINCT(A.BILL_LAD_NO), 'M', NULL, '0', 'N', NULL, '0', A.CARGO_REF_NO, '0', "
							+ "A.SPLIT_SEQ, A.BILL_LAD_NO, TO_CHAR(A.BILL_LAD_DT, 'DD/MM/YYYY HH24:MI:SS'), A.IMPORT_DEPT_SNO,"
							+ " '108003075', TO_CHAR(A.HLPL_ENDORSE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.REMARK, A.EMP_CD,"
							+ " TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.FLAG ,TO_CHAR(B.ACT_ARRV_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM FMS7_CARGO_BL_DTL A ,FMS7_CARGO_ARRIVAL_DTL B WHERE A.CARGO_REF_NO = ? AND A.CARGO_REF_NO=B.CARGO_REF_NO "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND"
							+ " (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.SPLIT_SEQ DESC";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset1.getString(8));
					stmt2.setString(2, delta_FromDt);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_ToDt);
					stmt2.setString(5, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
//						if(rset2.getString(10).equals("1") || rset2.getString(10).equals("2")  ) {	// For taking split_seq 1&2 data in case of custom duty non-applicable. 
//							nrow--;
//							count--;
//						}
//						String act_arrv_dt="";
						
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						man_cd = rset1.getString(8);					
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim();
						abbr=value;
						cell = row.createCell(ncell++);
						
//						act_arrv_dt=rset2.getString(20);
						
						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        abbr=value;
						    }
						 if (abbr.contains("RIL")) {
							 abbr = "RIL";
						 }
						 cell.setCellValue("'"+abbr+"'");
						
						
						cell = row.createCell(1);	
						cell.setCellValue("'"+man_cd+"'");
						
						
						for(int i = 2; i <= 9; i++) {

							value = rset1.getString(i) == null ? "null" : rset1.getString(i);
							cell = row.createCell(i);
							
							cell.setCellValue("'"+value+"'");
							 if(i == 6){
								cont_no = rset1.getString(8) == null ? "null" : rset1.getString(8);
								cell.setCellValue("'"+cont_no+"'");
							}	
							
						}
						
						for(int i = 10; i < columns.split(",").length; i++) {

							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							cell = row.createCell(i);
							
							if((i == 10 && value.equals("0") )|| (i == 10 && value.equals("2") ) ) {	// BL No
								cell.setCellValue("'1'");
							}
							/*else if ((i == 17) && !value.equals("null")) {	// emp_abbr
								value = getEmpAbbr(value);
								cell.setCellValue("'"+value+"'");
							}*/
							else {
								cell.setCellValue("'"+value+"'");
							}
							
						}
						
						if(rset2.getString(10).equals("1") ) {	// For taking split_seq 1&2 data in case of custom duty non-applicable. 
							nrow--;
							count--;
						}
						
						count++;					
						logger.data(fname, (abbr + "," + man_cd + "," + " M " + "," + cont_no + "," +" 0 "+ "," +" N "+"," + cont_no +","+" 0 "+"," + cont_no + "," + " 0 " + ","+ " 1 " + ","), conn, "");					

					}
					rset2.close();
					stmt2.close();
					
				}
				rset1.close();
				stmt1.close();
			
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_BL_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BUY_CARGO_ALLOC_BL>>,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void FMS_BUY_CARGO_ALLOC_BOE() throws SQLException, IOException {
		
		function_nm = "FMS_BUY_CARGO_ALLOC_BOE()";
		
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_BUY_CARGO_ALLOC_BOE>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,CONT_REF_NO,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BOE_NO,BU_SEQ,PLANT_SEQ,BOE_REF,BOE_DT,"
					+ "ACT_BOE_QTY,ACT_BOE_QTY_UNIT,ACT_QTY_MT,ACT_QTY_SCM,CUSTOM_DUTY,LOAD_PORT,ENT_BY,ENT_DT,FLAG,BOE_PROVISIONAL_PRICE,BOE_PROVISIONAL_PRICE_UNIT,BOE_FINAL_PRICE,BOE_FINAL_PRICE_UNIT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String cont_no="",boe_qty="",boe_act_mt="",boe_act_scm="";
		

			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ABBR,MAN_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BOE_NO,TIMESTAMP", conn);
			
			while (rset.next()) {
				
				queryString1 = "SELECT A.TRD_CD, 'M', A.MAN_CD, '0', 'N', A.MAN_CONF_CD, '0', A.CARGO_REF_CD, '0', A.SHIP_CD, A.CONFIRM_VOL, TO_CHAR(A.EXP_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ " TO_CHAR(A.EXP_TO_DT, 'DD/MM/YYYY HH24:MI:SS'), A.COUNTRY_ORIGIN, A.LOAD_PORT, A.REMARK, NULL, '1', '1',"
						+ " A.LIQUEFAC_PLANT, A.LIQUEFAC_COUNTRY, A.LIQUEFAC_PROMOTOR, A.LIQUEFAC_REMARK, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL,"
						+ " A.SURV_CD, A.CHA_CD, A.VA_CD, A.CONFIRM_PRICE "
						+ "FROM FMS7_CARGO_NOMINATION A, FMS7_TRADER_MST B WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? "
						+ " AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					queryString2 = "SELECT DISTINCT(BE_NO), 'M', NULL, '0', 'N', NULL, '0', CARGO_REF_NO, '0', SPLIT_SEQ, '1', '1',"
							+ " BE_NO, TO_CHAR(BE_DT, 'DD/MM/YYYY HH24:MI:SS'), SPLIT_SEQ, NULL, SPLIT_SEQ, SPLIT_SEQ, 'Y', "
							+ "PORT_CODE, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), FLAG, '0', '2', NULL, '2' ,TO_CHAR(ACT_ARRV_DT, 'DD/MM/YYYY HH24:MI:SS')"
							+ "FROM FMS7_CARGO_ARRIVAL_DTL A WHERE CARGO_REF_NO = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.SPLIT_SEQ DESC";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset1.getString(8));
					stmt2.setString(2, delta_FromDt);
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_ToDt);
					stmt2.setString(5, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
//						if(rset2.getString(10).equals("1") || rset2.getString(10).equals("2")  ) {	// For taking split_seq 1&2 data in case of custom duty non-applicable. && NOT TAKING NON CUSTOM DUTY 
//							nrow--;
//							count--;
//						}
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						man_cd = rset1.getString(8);					
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim();
						abbr=value;
						cell = row.createCell(ncell++);
						
//						act_arrv_dt=rset2.getString(28);
						
						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        abbr=value;
						    }
						 if (abbr.contains("RIL")) {
							 abbr = "RIL";
						 }
						 cell.setCellValue("'"+abbr+"'");
						
						
						cell = row.createCell(1);	
						cell.setCellValue("'"+man_cd+"'");
						
						for(int i = 2; i <= 9; i++) {

							value = rset1.getString(i) == null ? "null" : rset1.getString(i);
							cell = row.createCell(i);
							
							cell.setCellValue("'"+value+"'");
							if(i == 6){
								cont_no = rset1.getString(8) == null ? "null" : rset1.getString(8);
								cell.setCellValue("'"+cont_no+"'");
							}	
						}
						
						for(int i = 10; i < columns.split(",").length; i++) {

							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							cell = row.createCell(i);
							
							if((i == 10 && value.equals("0") )|| (i == 10 && value.equals("2") ) )  {	// BE No
								cell.setCellValue("'1'");
							}
						 
							else if(i ==15) {  //ACT BOE MMMBTU ,MT,SCM //DG 20250509
								
								queryString3 = "SELECT QTY_MMBTU, QTY_MT, QTY_SCM  "
											+ " FROM FMS7_CARGO_QQ_DTL WHERE CARGO_REF_NO = ? AND "
											+ "(? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
											+ " AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY SPLIT_SEQ DESC";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, rset1.getString(8));
									stmt3.setString(2, delta_FromDt);
									stmt3.setString(3, delta_FromDt);
									stmt3.setString(4, delta_ToDt);
									stmt3.setString(5, delta_ToDt);
									rset3 = stmt3.executeQuery();
									if (rset3.next()) {
										boe_qty=rset3.getString(1);
										boe_act_mt=rset3.getString(2);
										boe_act_scm=rset3.getString(3);
										
									}
							
								cell.setCellValue("'"+boe_qty+"'");
								rset3.close();
								stmt3.close();
							}
							else if(i==17)
							{
								cell.setCellValue("'"+boe_act_mt+"'");
							}
							else if(i==18)
							{
								cell.setCellValue("'"+boe_act_scm+"'");
							}
							else if(i == 19 && rset2.getString(10).equals("2")) {	// Custom Duty
								value = "N";
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 26) {	// Custom Duty
								value =rset1.getString(30) ;
								cell.setCellValue("'"+value+"'");
							}
							/*else if ((i == 21) && !value.equals("null")) {	// emp_abbr
								value = getEmpAbbr(value);
								cell.setCellValue("'"+value+"'");
							}*/
							else {
								cell.setCellValue("'"+value+"'");
							}
							
						}
						if(rset2.getString(10).equals("1")  ) {	// For taking split_seq 1&2 data in case of custom duty non-applicable. 
							nrow--;
							count--;
						}
						count++;
						logger.data(fname, (abbr + "," + man_cd + "," + " M " + "," + cont_no + "," +" 0 "+ "," +" N "+"," + cont_no +","+" 0 "+","+cont_no+","+" 0 "+","+" 1 "+","), conn, "");
					}
					rset2.close();
					stmt2.close();
					
				}
				rset1.close();
				stmt1.close();
			
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_BOE_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 

			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BUY_CARGO_ALLOC_BOE>>,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	

public void FMS_TRADER_CONT_MST() throws SQLException, IOException {
		
		function_nm = "FMS_TRADER_CONT_MST()";
		String sn_no="",sn_rev_no="",fgsa_no="",fgsa_rev_no="";
		String customer_cd="";
		String clause_cd="";
		String  trd_abbr="",cargo_sel="",cargo_seq="",sn_ref_no="",trade_ref_no="",rate="",cont_type="";
		double tcq=0;
		String st_dt="",end_dt="",rate_unit="";
		String org_abbr="";
		


		try {
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TRADER_CONT_MST>>,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,"
					+ "END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,"
					+ "BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,"
					+ "DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,"
					+ "MODIFY_BY,CONT_STATUS,TXN_CHARGE,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,DDA_DT,DDA_TIME,TXN_UNIT,DAY_DEF_CLAUSE,"
					+ "MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,"
					+ "OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,"
					+ "CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String dom_buy_flag="",cargo_list="",unit="",cargo_status="",fcc_flag="",cancel_dt="",status="";
	
//			Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
				
				queryString = "SELECT B.TRADER_ABBR,A.CUSTOMER_CD,A.FGSA_NO,A.FGSA_REV_NO,TO_CHAR(A.ENT_DT, 'YYYY'),A.SN_REV_NO,A.DOM_BUY_FLAG,A.SN_NAME,NULL,"
						+ "NULL,TO_CHAR(A.SIGNING_DT,'DD/MM/YYYY HH24:MI:SS'),A.SIGNING_TIME,TO_CHAR(A.START_DT,'DD/MM/YYYY  HH24:MI:SS'),"
						+ "TO_CHAR(A.END_DT,'DD/MM/YYYY HH24:MI:SS'),'X',0,A.TCQ,A.DCQ,NULL,A.QUANTITY_UNIT,NULL,A.RATE_UNIT,NULL,"
						+ "A.POST_MARGIN,A.TRANSPORTATION_CHARGE,A.SPLIT_FLAG,A.SPLIT_TYPE,"
						+ "A.BUYER_NOM,A.BUYER_MONTH_NOM,A.BUYER_WEEK_NOM,A.BUYER_DAILY_NOM,"
						+ "A.SELLER_NOM,A.SELLER_MONTH_NOM,A.SELLER_WEEK_NOM,A.SELLER_DAILY_NOM,"
						+ "A.DAY_DEF,A.DAY_START_TIME,"
						+ "A.DAY_END_TIME,A.MDCQ,A.MDCQ_PERCENTAGE,A.FCC_FLAG,A.FCC_BY,TO_CHAR(A.FCC_DATE,'DD/MM/YYYY HH24:MI:SS'),A.REMARK,"
						+ "TO_CHAR(A.RENEWAL_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.DEAL_ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EMP_CD,NULL,NULL,"
						+ "A.FCC_FLAG,"// FLAG 
						+ "4,'N','N',TO_CHAR(A.SIGNING_DT, 'DD/MM/YYYY HH24:MI:SS'),A.SIGNING_TIME,1,NULL,A.MEASUREMENT,"
						+ "NULL,A.MEAS_STANDARD,A.MEAS_TEMPERATURE,A.PRESSURE_MIN_BAR,A.PRESSURE_MAX_BAR,A.OFF_SPEC_GAS,NULL,"
						+ "A.SPEC_GAS_ENERGY_BASE,A.SPEC_GAS_MIN_ENERGY,A.SPEC_GAS_MAX_ENERGY,NULL,A.CARGO_SEL,NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "A.SN_CLOSURE_FLAG,A.SN_CLOSURE_QTY,TO_CHAR(A.SN_CLOSURE_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,A.SN_NO "
						+ "FROM FMS7_TRADER_CONT_MST A, FMS7_TRADER_MST B  "
						+ "WHERE A.CUSTOMER_CD = B.TRADER_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY  A.CUSTOMER_CD,A.DOM_BUY_FLAG,A.SN_NO , A.SN_REV_NO DESC";
				
				
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, delta_FromDt);
				stmt.setString(2, delta_FromDt);
				stmt.setString(3, delta_ToDt);
				stmt.setString(4, delta_ToDt);
				rset = stmt.executeQuery();
				

				logger.checkpoint(fname, "CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_TYPE,TIMESTAMP", conn);
				String seq="";
				
				while(rset.next())
				{
//					seq="";
					
					trd_abbr=rset.getString(1);
					customer_cd=rset.getString(2);
					fgsa_no=rset.getString(3);
					fgsa_rev_no=rset.getString(4);
					sn_no=rset.getString(81);
					sn_rev_no=rset.getString(6);
					dom_buy_flag =rset.getString(7);
					cargo_sel=rset.getString(70);
					
					org_abbr=trd_abbr;
					if(cargo_sel!=null) {
						seq=cargo_sel;
					}
					String dom1 = "";
//					System.out.println("--->"+cargo_sel);
//					if(cargo_sel!=null)
//					{
						for (int j= 0; j <seq.split("@").length; j++) {
							
							row = spreadsheet.createRow(nrow++);
						
							cargo_list += seq.split("@")[j] + ","; // append string with a comma
						
							cargo_status="";
							for (int i = 0; i < columns.split(",").length; i++) {
								
								value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
								cell = row.createCell(i);
								
								
								if(i==0) { // FOR SELECTING CARGO_SEQ_NO
									queryString1="SELECT CARGO_SEQ_NO,PRICE,DOM_BUY_FLAG,"
											+ "TO_CHAR(DELV_FROM_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(DELV_TO_DT,'DD/MM/YYYY HH24:MI:SS'),"
											+ "PRICE_UNIT,CARGO_STATUS  FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ?";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1,seq.split("@")[j]);
									rset1 = stmt1.executeQuery();
									while(rset1.next()) {
										cargo_seq=rset1.getString(1);
										rate=rset1.getString(2);
										dom_buy_flag=rset1.getString(3);
										st_dt=rset1.getString(4);
										end_dt=rset1.getString(5);
										rate_unit=rset1.getString(6);
										cargo_status=rset1.getString(7);
						
									}
									stmt1.close();
									rset1.close();
									trd_abbr=trd_abbr.trim();
									
									if(trd_abbr.contains("RIL")) {
										trd_abbr="RIL";
									}
									value=trd_abbr;
									
									if (mpe.counterparty_map.containsKey(value)) {
										value =mpe.counterparty_map.get(value); 
										trd_abbr = value; 
										value=trd_abbr;
									}
									dom1 = dom_buy_flag;
									if (dom_buy_flag == null) {
										dom_buy_flag = "N";  // Handle null first
									}else if (dom_buy_flag.equals("Y")) {
										dom_buy_flag = "D";
									}else if (dom_buy_flag.equals("K")) {
										dom_buy_flag = "I";
									}else if (dom_buy_flag.equals("T")) {
										dom_buy_flag = "T";
									}else if (dom_buy_flag.equals("N")) {
										dom_buy_flag = "N";
									}
									
									value = trd_abbr +"-"+sn_no+"-"+cargo_seq+"-"+dom_buy_flag+"-"+seq.split("@")[j];
								}
								
								if(i==6) {// VALUE OF PIPELINE GAS CHANGING IN EMS
									queryString1="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD=?";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1,seq.split("@")[j]);
									rset1 = stmt1.executeQuery();
									while(rset1.next()) {
										value=rset1.getString(1);
										
										if (value == null) {
										    value = "N";  // Handle null first
										}else if (value.equals("Y")) {
										    value = "D";
										} else if (value.equals("K")) {
										    value = "I";
										} else if (value.equals("T")) {
										    value = "T";
										}else if (value.equals("N")) {
										    value = "N";
										}
									}
									stmt1.close();
									rset1.close();
								}					
								if(i==8) {//20250507--cont_ref_no
									
									dom_buy_flag =rset.getString(7);
//									if(trd_abbr.equals("AMNS"))
//									{
//										System.out.println(dom_buy_flag);
//									}
									
									queryString1= "SELECT SN_REF_NO FROM FMS7_TRADER_CONT_MST WHERE SN_NO = ? AND SN_REV_NO = ? AND FGSA_NO = ? AND FGSA_REV_NO = ? AND CUSTOMER_CD = ? AND DOM_BUY_FLAG = ? ";
									stmt1=conn.prepareStatement(queryString1);
									stmt1.setString(1, sn_no);
									stmt1.setString(2, sn_rev_no);
									stmt1.setString(3, fgsa_no);
									stmt1.setString(4, fgsa_rev_no);
									stmt1.setString(5, customer_cd);
									if ("T".equals(dom1)) {   
									    dom1 = "Y";
									}
									stmt1.setString(6, dom1);
//									stmt1.setString(6, dom1 = dom1.equals("T") ? "Y" : dom1);
									rset1=stmt1.executeQuery();
//									System.out.println("=="+sn_no+"=="+sn_rev_no+"=="+fgsa_no+"=="+fgsa_rev_no+"=="+customer_cd+"=="+dom1);
									while(rset1.next()) {
										sn_ref_no= rset1.getString(1);
									
										if(dom_buy_flag.equals("K")) {
											dom_buy_flag= "I";
											value = trd_abbr +"-"+sn_no+"-"+cargo_seq+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no;
											
										}
										else
										{
											queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ?";
											stmt2 = conn.prepareStatement(queryString2);
											stmt2.setString(1,seq.split("@")[j]);
											rset2 = stmt2.executeQuery();
											while(rset2.next()) {
												dom_buy_flag=rset2.getString(1);
												cont_type=dom_buy_flag;
											}
											stmt2.close();
											rset2.close();
											
											if(cont_type.equals("T")) {
												cont_type="Y";
											}
								
											queryString2= "SELECT TRADE_REF_NO FROM FMS7_TRADER_CONT_MST WHERE SN_NO = ? AND SN_REV_NO = ? "
													+ "AND FGSA_NO = ? AND FGSA_REV_NO = ? AND CUSTOMER_CD = ? AND DOM_BUY_FLAG = ? ";
											stmt2=conn.prepareStatement(queryString2);
											stmt2.setString(1, sn_no);
											stmt2.setString(2, sn_rev_no);
											stmt2.setString(3, fgsa_no);
											stmt2.setString(4, fgsa_rev_no);
											stmt2.setString(5, customer_cd);
											stmt2.setString(6, cont_type);
											rset2=stmt2.executeQuery();
											while(rset2.next()) {
												trade_ref_no= rset2.getString(1);
												
												if(dom_buy_flag.equals("Y")) {
													dom_buy_flag= "D";
													value = trd_abbr +"-"+sn_no+"-"+cargo_seq+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no;
//													System.out.println(value);
												}
												else if(dom_buy_flag.equals("T")) {
													dom_buy_flag= "T";
													value = trd_abbr +"-"+sn_no+"-"+cargo_seq+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no;
//													System.out.println(value);
												}
												
											}
											stmt2.close();
											rset2.close();
//											if(trd_abbr.equals("AMNS")  )
//							    			{
//							    				System.out.println("---->"+value);
//							    			
//												System.out.println("===>"+sn_no+":"+sn_rev_no+":"+dom_buy_flag+":"+fgsa_rev_no+":"+customer_cd+":"+cont_type);
//							    			}
										}
									}
									stmt1.close();
									rset1.close();

									cell.setCellValue("'" + value + "'");
									
								}
								
								if(i==9) {//20250507--trade_ref_no
									dom_buy_flag =rset.getString(7);
									queryString1="SELECT DOM_BUY_FLAG,CONFIRM_VOL,UNIT_CD FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ?";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1,seq.split("@")[j]);
									rset1 = stmt1.executeQuery();
									while(rset1.next()) {
										dom_buy_flag=rset1.getString(1);
										cont_type=dom_buy_flag;
										tcq=rset1.getDouble(2);	
										unit=rset1.getString(3);
									}
									stmt1.close();
									rset1.close();
									
									if(("T").equals(cont_type)) {
										cont_type="Y";
									}
						
									queryString2= "SELECT TRADE_REF_NO, SN_REF_NO FROM FMS7_TRADER_CONT_MST "
											+ "WHERE SN_NO = ? AND SN_REV_NO = ? AND FGSA_NO = ? AND FGSA_REV_NO = ? "
											+ "AND CUSTOMER_CD = ? AND DOM_BUY_FLAG = ? ";
									stmt2=conn.prepareStatement(queryString2);
									
									
									
									stmt2.setString(1, sn_no);
									stmt2.setString(2, sn_rev_no);
									stmt2.setString(3, fgsa_no);
									stmt2.setString(4, fgsa_rev_no);
									stmt2.setString(5, customer_cd);
									stmt2.setString(6, cont_type);
									rset2=stmt2.executeQuery();
									while(rset2.next()) {
										trade_ref_no= rset2.getString(1);
										
										if(dom_buy_flag.equals("Y")) {
											dom_buy_flag= "D";
//											value = trd_abbr +"-"+sn_no+"-"+cargo_seq+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no;
											value=rset2.getString(2);
										}
										else if(dom_buy_flag.equals("T")) {
//											dom_buy_flag= "T";
//											value = trd_abbr +"-"+sn_no+"-"+cargo_seq+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no;
											value=rset2.getString(2);
										}
										else if(dom_buy_flag.equals("K")) {
											value = trade_ref_no;
										}
									}
									stmt2.close();
									rset2.close();
									cell.setCellValue("'" + value + "'");
								}	
								if(i==12)//start_dt
								{	
									value=st_dt;
								
								}
								if(i==13)//end_dt
								{
									value=end_dt;
									
								}
								if(i==16)
								{
									
									if(unit.equals("2"))
									{
										
										 BigDecimal tcq_conv = BigDecimal.valueOf(Math.round(tcq*1000000));
										 tcq_conv  = tcq_conv.setScale(0, RoundingMode.CEILING);
										 value=tcq_conv+"";
									}
									else {
										value=tcq+"";
									}
								}
								if(i==20) {
									value=rate;
								}
								if(i==21)
								{
									value=rate_unit;
								}
								
								 if(i==21 && value.length() > 1) 
								 {
									  value = value.substring(0,1);
								 }
								 if(i==25)//split flag
								 {
									 if(org_abbr.equals("RIL-CBM")){
										 value="N";
									 }
									 
								 }

								 if(i==49) {// CONT STATUS (IN FMS: CARGO STATUS )
									
									 fcc_flag=rset.getString(50);
								  	 if(fcc_flag!=null)
									 {
										 if(fcc_flag.equals("Y"))
										 {
											 value="Y";
										 }
										 else if(fcc_flag.equals("N"))
										 {
											 value="N";
										 }
										 else 
										 {
											 value="F";
										 }
									 }
									 else
									 {
										 value="F";
									 }
								  	//CONDITION FOR F-MAKING INTIALLY F TO X 
								  	 // IF F WILL LIE IN CONTRACT CLOSE THEN GO TO CLOSE IN NEXT QUERY
								  	queryString1 = "SELECT CARGO_STATUS " +
								               "FROM FMS7_MAN_CONFIRM_CARGO_DTL " +
								               "WHERE CARGO_REF_CD = ? " +
								               "AND CARGO_STATUS='F'";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1,seq.split("@")[j]);
									rset1 = stmt1.executeQuery();
									if(rset1.next()) {
									    
									    value="X";
									}
									rset1.close();
									stmt1.close(); 
									
								  	 
								  	 
									// condition for contract close 
									// value='C'
									queryString1 = "SELECT CARGO_STATUS " +
								               "FROM FMS7_MAN_CONFIRM_CARGO_DTL " +
								               "WHERE CARGO_REF_CD = ? " +
								               "AND DELV_TO_DT < SYSDATE";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1,seq.split("@")[j]);
									rset1 = stmt1.executeQuery();
									if(rset1.next()) {
									    value = rset1.getString(1);
									    value="C";
									}
									rset1.close();
									stmt1.close(); 

								 	// for checking flag cancel: X 
								 	queryString1="SELECT CARGO_STATUS,TO_CHAR(CANCEL_DT,'DD/MM/YYYY HH24:MI:SS') FROM FMS9_MAN_CANCEL_CARGO_DTL WHERE CARGO_REF_CD = ?";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1,seq.split("@")[j]);
									rset1 = stmt1.executeQuery();
									if(rset1.next()) {
										value=rset1.getString(1);
										cancel_dt=rset1.getString(2);
										
									}
									stmt1.close();
									rset1.close();
									status=value;
									cell.setCellValue("'" + value + "'");
									 
								 }
								
								if(i>=68 && i<=75) {
									queryString1="SELECT CLAUSE_CD FROM FMS7_TRADER_CLAUSE_MST WHERE SN_NO=? AND SN_REV_NO=? AND FGSA_NO=? AND FGSA_REV_NO=? AND BUYER_CD=?";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1,sn_no);
									stmt1.setString(2,sn_rev_no);
									stmt1.setString(3,fgsa_no);
									stmt1.setString(4,fgsa_rev_no);
									stmt1.setString(5,customer_cd);
									rset1 = stmt1.executeQuery();
									
									if(i==68) {
										if(rset1.next()) {
											clause_cd=rset1.getString(1);
											if(clause_cd.equals("1")) {
												value="Y";
									    		cell.setCellValue("'" + value + "'");
											}
											else {
									    		value=null;
									    		cell.setCellValue("'" + value + "'");
									    	}
										}
									}
									
									if(i==69) {
										value=null;
										cell.setCellValue("'" + value + "'");
										
									}
									else if(i==70) {
										if(rset1.next()) {
											clause_cd=rset1.getString(1);
											if(clause_cd.equals("6")) {
												value="Y";
									    		cell.setCellValue("'" + value + "'");
											}
											else {
									    		value=null;
									    		cell.setCellValue("'" + value + "'");
									    	}
										}
									}
									else if(i==72) {
										if(rset1.next()) {
											clause_cd=rset1.getString(1);
											if(clause_cd.equals("9")) {
												value="Y";
									    		cell.setCellValue("'" + value + "'");
											}
											else {
									    		value=null;
									    		cell.setCellValue("'" + value + "'");
									    	}
										}
									}
									else {
										cell.setCellValue("'" + value + "'");
									}
									
									stmt1.close();
									rset1.close();
								}
								if(i==76)//closure_flag
								{
									if(status.equals("C"))
									{
										value="A";
									}
								}
								if(i==78)//closure_efft_dt
								{
									if(status.equals("X"))
									{
										value=cancel_dt;
									}
									else if(status.equals("C"))
									{
										value=end_dt;
									}
								}
								
							cell.setCellValue("'" + value + "'");
								
								
							}
							count++;
							logger.data(fname, (customer_cd + "," + fgsa_no + "," +fgsa_rev_no+ "," +sn_no+"," + sn_rev_no +","+dom_buy_flag+","), conn, "");
		
							}	
					
				}
				
					stmt.close();
					rset.close();
					cargo_list=cargo_list.substring(0,cargo_list.length()-1);
					
//					FOR CARGO LIST NOT IN TRADER
					int query_ind=1;
					
					queryString = "SELECT A.CARGO_REF_CD,C.TRADER_ABBR,'0','0',TO_CHAR(A.ENT_DT, 'YYYY'),'0',A.DOM_BUY_FLAG,NULL,"
							+ "NULL,NULL,TO_CHAR(A.DELV_FROM_DT, 'DD/MM/YYYY'),TO_CHAR(A.DELV_FROM_DT, 'HH24:MI'),TO_CHAR(A.DELV_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(A.DELV_TO_DT, 'DD/MM/YYYY HH24:MI:SS'),'X','0',A.CONFIRM_VOL,"//TCQ IS CONFIRM_VOL
							+ "'0',NULL,UNIT_CD,A.PRICE,A.PRICE_UNIT,NULL,NULL,NULL,'N',"
							+ "NULL,'Y','N','N','N','Y','N','N','N',"
							+ "'Y','06:00','06:00','Y','100','Y','1',NULL,NULL,NULL,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EMP_CD,NULL,NULL,"
							+ "'N','4',NULL,NULL,TO_CHAR(A.DELV_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'),'00:00','1',NULL,"
							+ "'N',NULL,NULL,NULL,NULL,NULL,'N',"
							+ "NULL,'0',NULL,NULL,NULL,NULL,"
							+ "'N',NULL,NULL,NULL,NULL,NULL ,"
							+ "NULL,NULL,NULL,NULL  "
							+ "FROM  FMS7_MAN_CONFIRM_CARGO_DTL A,FMS7_MAN_REQ_MST B ,FMS7_TRADER_MST C  "
							+ "WHERE A.MAN_CD=B.MAN_CD  AND B.TRD_CD = C.TRADER_CD AND A.DOM_BUY_FLAG IS NOT NULL AND A.DOM_BUY_FLAG !='N' "
							+ "AND A.CARGO_REF_CD  NOT IN (";
					if (cargo_list.split(",").length > 0) {
						queryString += "?";
						for (int i = 1; i < cargo_list.split(",").length; i++) {
							queryString += ",?";
						}
					}
					queryString+=") AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					
					stmt = conn.prepareStatement(queryString);
					for (int i = 0; i < cargo_list.split(",").length; i++) {
						stmt.setString(query_ind++, cargo_list.split(",")[i]);
					}
					stmt.setString(query_ind++, delta_FromDt);
					stmt.setString(query_ind++, delta_FromDt);
					stmt.setString(query_ind++, delta_ToDt);
					stmt.setString(query_ind++, delta_ToDt);
					rset = stmt.executeQuery();
					
					while(rset.next())
					{
						String strt_dt="",ed_dt="";
						String tcq_calc="";
						double no_of_days=0;
						row = spreadsheet.createRow(nrow++);
						trd_abbr=rset.getString(2);
						
						unit=rset.getString(20);
						tcq=rset.getDouble(17);
						
						trd_abbr=trd_abbr.trim();
						if(trd_abbr.contains("RIL")) {
							trd_abbr="RIL";
						}
						value=trd_abbr;
						
						if (mpe.counterparty_map.containsKey(value)) {
							value =mpe.counterparty_map.get(value); 
							trd_abbr = value; 
							value=trd_abbr;
						}
						
						for (int i = 0; i < columns.split(",").length; i++) {
							value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
//							System.out.println("==>"+(i+1)+":"+value);
							cell = row.createCell(i);
							
							
							if(i==0) { // FOR SELECTING CARGO_SEQ_NO
							
								queryString1="SELECT CARGO_SEQ_NO,PRICE,DOM_BUY_FLAG,TO_CHAR(DELV_FROM_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(DELV_TO_DT,'DD/MM/YYYY HH24:MI:SS'),"
										+ "PRICE_UNIT  FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ?";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1,rset.getString(1));
								rset1 = stmt1.executeQuery();
								while(rset1.next()) {
									cargo_seq=rset1.getString(1);
									rate=rset1.getString(2);
									dom_buy_flag=rset1.getString(3);
									st_dt=rset1.getString(4);
									end_dt=rset1.getString(5);
									rate_unit=rset1.getString(6);
					
								}
								stmt1.close();
								rset1.close();
							
								
								if (dom_buy_flag == null) {
									dom_buy_flag = "N";  // Handle null first
								}else if (dom_buy_flag.equals("Y")) {
									dom_buy_flag = "D";
								}else if (dom_buy_flag.equals("K")) {
									dom_buy_flag = "I";
								}else if (dom_buy_flag.equals("T")) {
									dom_buy_flag = "T";
								}else if (dom_buy_flag.equals("N")) {
									dom_buy_flag = "N";
								}
								
								value = trd_abbr +"-"+0+"-"+cargo_seq+"-"+dom_buy_flag+"-"+rset.getString(1);
								cell.setCellValue("'" + value + "'");
							}
							else if(i==6) {// VALUE OF PIPELINE GAS CHANGING IN EMS
								queryString1="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD=?";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1,rset.getString(1));
								rset1 = stmt1.executeQuery();
								while(rset1.next()) {
									value=rset1.getString(1);
									
									if (value == null) {
									    value = "N";  // Handle null first
									}else if (value.equals("Y")) {
									    value = "D";
									} else if (value.equals("K")) {
									    value = "I";
									} else if (value.equals("T")) {
									    value = "T";
									}else if (value.equals("N")) {
									    value = "N";
									}
								}
								stmt1.close();
								rset1.close();
								cell.setCellValue("'" + value + "'");
							}
							else if(i==8)//cont-refno
							{
								value = trd_abbr +"-"+0+"-"+cargo_seq+"-"+dom_buy_flag+"-"+rset.getString(1)+"-"+0;
								cell.setCellValue("'" + value + "'");
							}
							else if(i==9)//trader contract ref no 
							{
								value = trd_abbr +"-"+0+"-"+cargo_seq+"-"+dom_buy_flag+"-"+rset.getString(1)+"-"+0;
								cell.setCellValue("'" + value + "'");
							}
							
							else if(i==16)
							{
								
								if(unit.equals("2"))
								{
									
									 BigDecimal tcq_conv = BigDecimal.valueOf(Math.round(tcq*1000000));
									 tcq_conv  = tcq_conv.setScale(0, RoundingMode.CEILING);
									 value=tcq_conv+"";
									 tcq_calc=tcq_conv+"";
									
								}
								else {
									value=tcq+"";
									tcq_calc=tcq+"";
								}
								 cell.setCellValue("'" + value + "'");
							}
							else if(i==17)
							{
								strt_dt=rset.getString(13);
								ed_dt=rset.getString(14);
								
								strt_dt=strt_dt.substring(0,strt_dt.length()-9);
								ed_dt=ed_dt.substring(0,ed_dt.length()-9);
								

								no_of_days=utilDate.getDays(strt_dt,ed_dt);
								no_of_days = Math.abs(no_of_days);
								no_of_days=no_of_days+2;// added 2 because of start date and end date 
								
								BigDecimal tcq1 = new BigDecimal(tcq_calc);// convert tcq_calc string into bigdec
								String no_of_day_1=Double.toString(no_of_days);
								BigDecimal days = new BigDecimal(no_of_day_1);// convert non_of_day_1 string  into bigdec
							

								BigDecimal dcq1 = tcq1.divide(days, 0, RoundingMode.HALF_UP);
							
								cell.setCellValue("'" + dcq1 + "'");
//								System.out.println("=="+days+":"+tcq1+":"+dcq1 +" st:"+strt_dt+" end:"+ed_dt);
								
							}
							else if(i==49) {
								// CONT STATUS (IN FMS: CARGO STATUS )
								
								 fcc_flag=rset.getString(50);
							  	 if(fcc_flag!=null)
								 {
									 if(fcc_flag.equals("Y"))
									 {
										 value="Y";
									 }
									 else if(fcc_flag.equals("N"))
									 {
										 value="N";
									 }
									 else 
									 {
										 value="F";
									 }
								 }
								 else
								 {
									 value="F";
								 }
							  	 
							  	 //CONDITION FOR F-MAKING INTIALLY F TO X 
							  	 // IF F WILL LIE IN CONTRACT CLOSE THEN GO TO CLOSE IN NEXT QUERY
							  	queryString1 = "SELECT CARGO_STATUS " +
							               "FROM FMS7_MAN_CONFIRM_CARGO_DTL " +
							               "WHERE CARGO_REF_CD = ? " +
							               "AND CARGO_STATUS='F'";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1,rset.getString(1));
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
								    
								    value="X";
								}
								rset1.close();
								stmt1.close(); 
								
								// condition for contract close 
								// value='C'
								queryString1 = "SELECT CARGO_STATUS " +
							               "FROM FMS7_MAN_CONFIRM_CARGO_DTL " +
							               "WHERE CARGO_REF_CD = ? " +
							               "AND DELV_TO_DT < SYSDATE";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1,rset.getString(1));
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
								    
								    value="C";
								}
								rset1.close();
								stmt1.close(); 

							 	// for checking flag cancel: X 
							 	queryString1="SELECT CARGO_STATUS,TO_CHAR(CANCEL_DT,'DD/MM/YYYY HH24:MI:SS') FROM FMS9_MAN_CANCEL_CARGO_DTL WHERE CARGO_REF_CD = ?";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1,rset.getString(1));
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									value=rset1.getString(1);
									cancel_dt=rset1.getString(2);
								}
								stmt1.close();
								rset1.close();
								status=value;
								cell.setCellValue("'" + value + "'");
								 
							 }
							else if(i==76)//closure_flag
							{
								if(status.equals("C"))
								{
									value="A";
								}
								cell.setCellValue("'" + value + "'");
								
							}
							else if(i==78)//closure_efft_dt
							{
								if(status.equals("X"))
								{
									value=cancel_dt;
								}
								else if(status.equals("C"))
								{
									value=end_dt;
								}
								cell.setCellValue("'" + value + "'");
							}
							else
							{
								cell.setCellValue("'" + value + "'");
							}
						}
						count++;
						logger.data(fname, (customer_cd + "," + fgsa_no + "," +fgsa_rev_no+ "," +0+"," + 0 +","+dom_buy_flag+","+"Cargo without contract in FMS ,"), conn, "");
						
					}
					stmt.close();
					rset.close();

				filename = migration_setup_dir + "EXPORT/FMS_TRADER_CONT_MST_"+start_end_dt+".xlsx";

				fileOut = new FileOutputStream(filename);

				workbook.write(fileOut);
				fileOut.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_CONT_MST>>,", conn);
										
				logger.checkpoint1(fname1,count+",", conn);
				
				System.out.println("<<END>><<FMS_TRADER_CONT_MST>>");
				System.out.println();

				msg = "Data has been Extracted Successfully.";
				msg_type = "S";
				
		}
		catch(Exception e){

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
public void FMS_TRADER_BILLING_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_TRADER_BILLING_DTL()";
		String sn_no,sn_rev_no,fgsa_no,fgsa_rev_no,cargo_sel,dom_buy_flag="",cargo_seq="",trd_abbr="",int_cal_rate_cd="",exchng_rate_cd="",int_rate_nm="",exchng_rate_nm="",org_abbr="";		
		String customer_cd="",plant_seq_no="";
		String cargo_list="";
		try {
			
			System.out.println("<<START>><<FMS_TRADER_BILLING_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TRADER_BILLING_DTL>>,,,,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,SPLIT_FLAG,PLANT_SEQ_NO,HOLIDAY_STATE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
//			Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			
			
			queryString = "SELECT B.CARGO_SEL, A.CUSTOMER_CD,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO, A.SN_REV_NO,A.CONT_TYPE,A.BILLING_FREQ,"
					+ "A.FLAG,A.DUE_DATE,A.SECOND_DUE_DT,A.INVOICE_CUR_CD,A.PAYMENT_CUR_CD,A.INT_CAL_RATE_CD,A.INT_CAL_SIGN,"
					+ "A.INT_CAL_PERCENTAGE,A.EXCHNG_RATE_CD,A.EXCHNG_RATE_CAL,A.INV_CRITERIA,A.EXCHG_RATE_NOTE,A.TAX_STRUCT_CD,"
					+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY hh24:mi:ss'),A.EMP_CD,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,C.PLANT_SEQ_NO,NULL "
					+ "FROM FMS7_TRADER_BILLING_DTL A , FMS7_TRADER_CONT_MST B , FMS7_TRADER_PLANT_MST C "
					+ "WHERE A.SN_NO=B.SN_NO AND A.SN_REV_NO=B.SN_REV_NO AND  A.CONT_TYPE=B.DOM_BUY_FLAG AND  "
					+ "A.FGSA_NO=B.FGSA_NO AND A.FGSA_REV_NO=B.FGSA_REV_NO AND A.CUSTOMER_CD=B.CUSTOMER_CD"
					+ " AND A.FGSA_NO=C.FGSA_NO AND A.FGSA_REV_NO=C.FGSA_REV_NO AND A.CUSTOMER_CD=C.CUSTOMER_CD AND  A.SN_NO=C.SN_NO AND A.SN_REV_NO=C.SN_REV_NO"
					+ " AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
					+ " AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.CUSTOMER_CD,A.CONT_TYPE,A.SN_NO,A.SN_REV_NO DESC";
			stmt = conn.prepareStatement(queryString);	
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP", conn);
			
			String seq="";
			
			while(rset.next())
			{
				if(rset.getString(1)!=null) // THIS IS BECAUSE : dont want data of rev 
				{
					
					
					cargo_sel=rset.getString(1);
					customer_cd=rset.getString(2);
					fgsa_no=rset.getString(3);
					fgsa_rev_no=rset.getString(4);
					sn_no=rset.getString(5);
					sn_rev_no=rset.getString(6);
//					dom_buy_flag=rset.getString(7);
					int_cal_rate_cd=rset.getString(14);
					exchng_rate_cd=rset.getString(17);
					plant_seq_no=rset.getString(32);
					
//					if(cargo_sel!=null) {
						seq=cargo_sel;
//				}
					
					for (int j= 0; j <seq.split("@").length; j++) {
							
							row = spreadsheet.createRow(nrow++);
							cargo_list += seq.split("@")[j] + ","; // append string with a comma
						
							plant_seq_no=rset.getString(32);
							
							for (int i = 0; i < columns.split(",").length; i++) {
								
								value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
								cell = row.createCell(i);
								
								
								
								if(i ==0) {
									queryString2="SELECT CARGO_SEQ_NO,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
									stmt2=conn.prepareStatement(queryString2);
									stmt2.setString(1,seq.split("@")[j]);
									rset2=stmt2.executeQuery();
									while(rset2.next()) {
										cargo_seq=rset2.getString(1);
										dom_buy_flag=rset2.getString(2);

									}
									stmt2.close();
									rset2.close();
									
									queryString3="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD = ? ";
									stmt3=conn.prepareStatement(queryString3);
									stmt3.setString(1,rset.getString(2));
									rset3=stmt3.executeQuery();
									while(rset3.next()) {
										trd_abbr=rset3.getString(1);
									}
									stmt3.close();
									rset3.close();
									
									trd_abbr=trd_abbr.trim();
									
									org_abbr =trd_abbr;
									
									if(trd_abbr.contains("RIL")) {
										trd_abbr="RIL";
									}
									value=trd_abbr;
									if (mpe.counterparty_map.containsKey(value)) {
										value =mpe.counterparty_map.get(value); 
										trd_abbr = value; 
										value=trd_abbr;
									}
									
									int plant_seq=0;
									if(org_abbr.contains("CBM"))
									{
//										System.out.println("--->>"+plant_seq_no);
										 plant_seq=Integer.parseInt(plant_seq_no);
										 plant_seq=plant_seq+3;
										 plant_seq_no=plant_seq+"";
//										 System.out.println("->>"+plant_seq_no);
										
									} 
									//for plant :
									if(mpe.trader_map.containsKey(trd_abbr+"-"+plant_seq_no)   && !org_abbr.contains("CBM") )
									{
										plant_seq_no=mpe.trader_map.get(trd_abbr+"-"+plant_seq_no);
										plant_seq_no=plant_seq_no.split("-")[0];
									}
									
									
									if (dom_buy_flag == null) {
										dom_buy_flag = "N";  // Handle null first
									}else if (dom_buy_flag.equals("Y")) {
										dom_buy_flag = "D";
									} else if (dom_buy_flag.equals("K")) {
										dom_buy_flag = "I";
									} else if (dom_buy_flag.equals("T")) {
										dom_buy_flag = "T";
									}else if (dom_buy_flag.equals("N")) {
										dom_buy_flag = "N";
									}
									
									value=trd_abbr+"-"+sn_no+"-"+cargo_seq+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no;
									cell.setCellValue("'" + value + "'");
								}

								else if (i == 1) {
									queryString3="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD = ? ";
									stmt3=conn.prepareStatement(queryString3);
									stmt3.setString(1,rset.getString(2));
									rset3=stmt3.executeQuery();
									while(rset3.next()) {
										value=rset3.getString(1);
										
										value=value.trim();
										
										if(value.contains("RIL")) {
											value="RIL";
										}
										
										if (mpe.counterparty_map.containsKey(value)) {
											value =mpe.counterparty_map.get(value); 
										}
										
									}
									stmt3.close();
									rset3.close();
									cell.setCellValue("'" + value + "'");
								}
								else if(i==6) {// VALUE OF PIPELINE GAS CHANGING IN EMS
									
									queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD=?";
									stmt2 = conn.prepareStatement(queryString2);
									
									stmt2.setString(1,seq.split("@")[j]);
									rset2 = stmt2.executeQuery();
									while(rset2.next()) {
										value=rset2.getString(1);
//													System.out.println(value);
										
										if (value == null) {
										    value = "N";  // Handle null first
										}else if (value.equals("Y")) {
										    value = "D";
										} else if (value.equals("K")) {
										    value = "I";
										} else if (value.equals("T")) {
										    value = "T";
										}else if (value.equals("N")) {
										    value = "";
										}
										cell.setCellValue("'" + value + "'");
									}
									stmt2.close();
									rset2.close();
								}
								
								else if(i==13) {
									queryString2="SELECT INT_RATE_NM FROM FMS7_CONT_INT_RATE_MST WHERE INT_RATE_CD = ?";
									stmt2=conn.prepareStatement(queryString2);
									stmt2.setString(1,int_cal_rate_cd);
									rset2=stmt2.executeQuery();
									while(rset2.next()) {
										int_rate_nm=rset2.getString(1);

									}
									stmt2.close();
									rset2.close();
									value=int_rate_nm;
									cell.setCellValue("'" + value + "'");
								}
								else if(i==16) {
									queryString2="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD = ?";
									stmt2=conn.prepareStatement(queryString2);
									stmt2.setString(1,exchng_rate_cd);
									rset2=stmt2.executeQuery();
									while(rset2.next()) {
										exchng_rate_nm=rset2.getString(1);

									}
									stmt2.close();
									rset2.close();
									 value=exchng_rate_nm;
										cell.setCellValue("'" + value + "'");
								}
								else if(i==25)//BILLING BUSINESS(B) as said by vijay sir 20250612
								{
									cell.setCellValue("'B'");
								}
								else if(i==31)//plant_seq_no
								{
									cell.setCellValue("'" + plant_seq_no + "'");
								}
								
							else {
								
								cell.setCellValue("'" + value + "'");
							}									
						}	
							count++;
							logger.data(fname, (customer_cd + "," + sn_no + "," + sn_rev_no + "," +fgsa_no+ "," +fgsa_rev_no+","), conn, "");
						}
					
					
				}
				
									
			}					
				stmt.close();
				rset.close();
				
				queryString = "SELECT B.CARGO_SEL, A.CUSTOMER_CD,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO, A.SN_REV_NO,A.CONT_TYPE,A.BILLING_FREQ,"
						+ "A.FLAG,A.DUE_DATE,A.SECOND_DUE_DT,A.INVOICE_CUR_CD,A.PAYMENT_CUR_CD,A.INT_CAL_RATE_CD,A.INT_CAL_SIGN,"
						+ "A.INT_CAL_PERCENTAGE,A.EXCHNG_RATE_CD,A.EXCHNG_RATE_CAL,A.INV_CRITERIA,A.EXCHG_RATE_NOTE,A.TAX_STRUCT_CD,"
						+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY hh24:mi:ss'),A.EMP_CD,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Y',C.PLANT_SEQ_NO,NULL, C.CUSTOMER_CD "
						+ "FROM FMS7_TRADER_BILLING_DTL A , FMS7_TRADER_CONT_MST B , FMS7_TRADER_OTHER_PLANT_DTL C WHERE A.SN_NO=B.SN_NO AND A.SN_REV_NO=B.SN_REV_NO AND "
						+ "A.FGSA_NO=B.FGSA_NO AND A.FGSA_REV_NO=B.FGSA_REV_NO AND A.CUSTOMER_CD=B.CUSTOMER_CD"
						+ " AND A.FGSA_NO=C.FGSA_NO AND A.FGSA_REV_NO=C.FGSA_REV_NO AND  A.SN_NO=C.SN_NO AND A.SN_REV_NO=C.SN_REV_NO"
						+ " AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
						+ " AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.CUSTOMER_CD,A.CONT_TYPE,A.SN_NO,A.SN_REV_NO DESC";
				stmt = conn.prepareStatement(queryString);	
				stmt.setString(1, delta_FromDt);
				stmt.setString(2, delta_FromDt);
				stmt.setString(3, delta_ToDt);
				stmt.setString(4, delta_ToDt);
				rset = stmt.executeQuery();

				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP", conn);
				
				seq="";
				
				while(rset.next())
				{
					if(rset.getString(1)!=null) // THIS IS BECAUSE : dont want data of rev 
					{
						
						
						cargo_sel=rset.getString(1);
						customer_cd=rset.getString(2);
						fgsa_no=rset.getString(3);
						fgsa_rev_no=rset.getString(4);
						sn_no=rset.getString(5);
						sn_rev_no=rset.getString(6);
//						dom_buy_flag=rset.getString(7);
						int_cal_rate_cd=rset.getString(14);
						exchng_rate_cd=rset.getString(17);
						plant_seq_no=rset.getString(32);
						
//						if(cargo_sel!=null) {
							seq=cargo_sel;
//					}
						
						for (int j= 0; j <seq.split("@").length; j++) {
								
								row = spreadsheet.createRow(nrow++);
								cargo_list += seq.split("@")[j] + ","; // append string with a comma
								
								for (int i = 0; i < columns.split(",").length; i++) {
									
									value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
									cell = row.createCell(i);
									
									
									
									if(i ==0) {
										queryString2="SELECT CARGO_SEQ_NO,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
										stmt2=conn.prepareStatement(queryString2);
										stmt2.setString(1,seq.split("@")[j]);
										rset2=stmt2.executeQuery();
										while(rset2.next()) {
											cargo_seq=rset2.getString(1);
											dom_buy_flag=rset2.getString(2);

										}
										stmt2.close();
										rset2.close();
										
										queryString3="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD = ? ";
										stmt3=conn.prepareStatement(queryString3);
										stmt3.setString(1,rset.getString(2));
										rset3=stmt3.executeQuery();
										while(rset3.next()) {
											trd_abbr=rset3.getString(1);
											abbr = trd_abbr;
										}
										stmt3.close();
										rset3.close();
										
										trd_abbr=trd_abbr.trim();
										
										if(trd_abbr.contains("RIL")) {
											trd_abbr="RIL";
										}
										value=trd_abbr;
										if (mpe.counterparty_map.containsKey(value)) {
											value =mpe.counterparty_map.get(value); 
											trd_abbr = value; 
											value=trd_abbr;
										}
										
										
										//for plant :
										if (abbr.contains("CBM")) {
											plant_seq_no = (Integer.parseInt(plant_seq_no) + 3) + "";
										}
										if(mpe.trader_map.containsKey(trd_abbr+"-"+plant_seq_no))
										{
											plant_seq_no=mpe.trader_map.get(trd_abbr+"-"+plant_seq_no);
											plant_seq_no=plant_seq_no.split("-")[0];
										}
										
										
										if (dom_buy_flag == null) {
											dom_buy_flag = "N";  // Handle null first
										}else if (dom_buy_flag.equals("Y")) {
											dom_buy_flag = "D";
										} else if (dom_buy_flag.equals("K")) {
											dom_buy_flag = "I";
										} else if (dom_buy_flag.equals("T")) {
											dom_buy_flag = "T";
										}else if (dom_buy_flag.equals("N")) {
											dom_buy_flag = "N";
										}
										
										value=trd_abbr+"-"+sn_no+"-"+cargo_seq+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no;
										cell.setCellValue("'" + value + "'");
									}
									else if (i == 1) {
										queryString3="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD = ? ";
										stmt3=conn.prepareStatement(queryString3);
										stmt3.setString(1,rset.getString(34));
										rset3=stmt3.executeQuery();
										while(rset3.next()) {
											value=rset3.getString(1);
											
											value=value.trim();
											
											if(value.contains("RIL")) {
												value="RIL";
											}
											
											if (mpe.counterparty_map.containsKey(value)) {
												value =mpe.counterparty_map.get(value); 
											}
											
										}
										stmt3.close();
										rset3.close();
										cell.setCellValue("'" + value + "'");
									}
									
									else if(i==6) {// VALUE OF PIPELINE GAS CHANGING IN EMS
										
										queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD=?";
										stmt2 = conn.prepareStatement(queryString2);
										
										stmt2.setString(1,seq.split("@")[j]);
										rset2 = stmt2.executeQuery();
										while(rset2.next()) {
											value=rset2.getString(1);
//														System.out.println(value);
											
											if (value == null) {
											    value = "N";  // Handle null first
											}else if (value.equals("Y")) {
											    value = "D";
											} else if (value.equals("K")) {
											    value = "I";
											} else if (value.equals("T")) {
											    value = "T";
											}else if (value.equals("N")) {
											    value = "";
											}
											cell.setCellValue("'" + value + "'");
										}
										stmt2.close();
										rset2.close();
									}
									
									else if(i==13) {
										queryString2="SELECT INT_RATE_NM FROM FMS7_CONT_INT_RATE_MST WHERE INT_RATE_CD = ?";
										stmt2=conn.prepareStatement(queryString2);
										stmt2.setString(1,int_cal_rate_cd);
										rset2=stmt2.executeQuery();
										while(rset2.next()) {
											int_rate_nm=rset2.getString(1);

										}
										stmt2.close();
										rset2.close();
										value=int_rate_nm;
										cell.setCellValue("'" + value + "'");
									}
									else if(i==16) {
										queryString2="SELECT EXC_RATE_NM FROM FMS7_CONT_EXCHG_RATE_MST WHERE EXC_RATE_CD = ?";
										stmt2=conn.prepareStatement(queryString2);
										stmt2.setString(1,exchng_rate_cd);
										rset2=stmt2.executeQuery();
										while(rset2.next()) {
											exchng_rate_nm=rset2.getString(1);

										}
										stmt2.close();
										rset2.close();
										 value=exchng_rate_nm;
											cell.setCellValue("'" + value + "'");
									}
									else if(i==25)//BILLING BUSINESS(B) as said by vijay sir 20250612
									{
										cell.setCellValue("'B'");
									}
									else if(i==31)//plant_seq_no
									{
										cell.setCellValue("'" + plant_seq_no + "'");
									}
									
								else {
									
									cell.setCellValue("'" + value + "'");
								}									
							}	
								count++;
								logger.data(fname, (customer_cd + "," + sn_no + "," + sn_rev_no + "," +fgsa_no+ "," +fgsa_rev_no+","), conn, "");
							}
						
						
					}
					
					
					
								
								
//							}	
//							stmt1.close();
//							rset1.close();
//							
										
				}					
					stmt.close();
					rset.close();
				

				filename = migration_setup_dir + "EXPORT/FMS_TRADER_BILLING_DTL_"+start_end_dt+".xlsx";

				fileOut = new FileOutputStream(filename);

				workbook.write(fileOut);
				fileOut.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,,,"), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_BILLING_DTL>>,,,,,,", conn);
				logger.checkpoint1(fname1,count+",", conn);

				
				System.out.println("<<END>><<FMS_TRADER_BILLING_DTL>>");
				System.out.println();

				msg = "Data has been Extracted Successfully.";
				msg_type = "S";
				
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}

public void FMS_BUY_DAILY_BUYER_NOM() throws SQLException, IOException {
	
	function_nm = "FMS_BUY_DAILY_BUYER_NOM()";
	String base="",trd_cd="";
	String cont_map_id="",cargo_ref_cd="",cont_type="",alloc_dt="",trd_abbr="",fgsa_no="",fgsa_rev_no="",sn_no="",sn_rev_no="",bu_seq_no="",cargo_seq_no="",dom_buy_flag="";
	
	
	BigDecimal baseVal=new BigDecimal("0");
	BigDecimal deviding_factor=new BigDecimal("0");
	double cal=0.252*1000000;
	BigDecimal multiplying_factor=  BigDecimal.valueOf(cal);
	BigDecimal gcv=new BigDecimal("9802.8");
	BigDecimal ncv=new BigDecimal("8831.35");
	BigDecimal qty_mmbtu=new BigDecimal("0");
	int supp_num=0;
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_BUY_DAILY_BUYER_NOM>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO";

		count = 0;
		String ins_map_id="";
//		Below block of code is for inserting columns
		String fname_csv = "", str = "", gas_dt="",plant_seq_no="",qty="";
		fname_csv = migration_setup_dir + "EXPORT/FMS_BUY_DAILY_BUYER_NOM_" + start_end_dt + ".csv";
		
		FileWriter fw = new FileWriter(fname_csv, false); 
		fw.close();

		for (int i = 0; i < columns.split(",").length; i++) {
			str += columns.split(",")[i] + ",";
		}

		queryString="SELECT A.CONT_MAPPING_ID, A.PARTY_CD, 0, 0, NULL, 0, 0, A.PLANT_SEQ_NO, 0, 0, (A.SUPPLIER_SEQ+1), TO_CHAR(A.ALLOC_DT,'DD/MM/YYYY HH24:MI:SS'), A.CONT_MAPPING_ID, "
				+ "TO_CHAR(A.ALLOC_DT-1,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.ALLOC_DT,'HH24:MI'),NULL,NULL,NULL,A.ENTRY_TOT_ENE,NULL,A.ENT_BY, "
				+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),0 FROM FMS9_PO_ALLOC_DTL A "
				+ "WHERE (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))) AND (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')))";
		
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		
		logger.insert_data(fname_csv, str, conn);
		
		logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
		
//		String seq="";
		
		while(rset.next())
		{	
			cont_map_id=rset.getString(1);
			alloc_dt=rset.getString(12);
//			base=rset.getString(16);
			qty_mmbtu= BigDecimal.valueOf(rset.getDouble(19));
			String qty_mmbtu1=rset.getString(19);
//			row = spreadsheet.createRow(nrow++);
			str = "";
//			String flag1="";
			if (!ins_map_id.contains(rset.getString(1)+"-"+rset.getString(12)+";")) {
				ins_map_id += rset.getString(1)+"-"+rset.getString(12)+";";
			} 
			if(!qty_mmbtu1.equals("0"))
			{
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
//					cell = row.createCell(i);
					if(i==0) {
						cont_map_id = cont_map_id.split("-")[2];
						cargo_seq_no=rset.getString(1).split("-")[5];
						queryString1="SELECT CARGO_REF_CD,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, cont_map_id);
						stmt1.setString(2, cargo_seq_no);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							cargo_ref_cd=rset1.getString(1);
							dom_buy_flag=rset1.getString(2);

							
							queryString3="SELECT CUSTOMER_CD,FGSA_NO,FGSA_REV_NO ,SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG = ?  ORDER BY  CUSTOMER_CD,DOM_BUY_FLAG,SN_NO ,SN_REV_NO DESC ";
							stmt3=conn.prepareStatement(queryString3);
							stmt3.setString(1,"%"+cargo_ref_cd+"%");
							stmt3.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);
							rset3=stmt3.executeQuery();
							if(rset3.next()) {
								trd_cd=rset3.getString(1);
//								if(cargo_ref_cd.equals("23031")) {
//								System.out.println(cargo_ref_cd+"=="+trd_cd);
//								}
								fgsa_no=rset3.getString(2);
								fgsa_rev_no=rset3.getString(3);
								sn_no=rset3.getString(4);
								sn_rev_no=rset3.getString(5);
//								dom_buy_flag=rset3.getString(6);
								 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
								 stmt4=conn.prepareStatement(queryString4);
								 stmt4.setString(1,trd_cd);
								 rset4=stmt4.executeQuery();
								 while(rset4.next()) {
									 trd_abbr=rset4.getString(1);
								 }
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 org_abbr=trd_abbr;
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									    }
									 if(trd_abbr.contains("RIL")) {
										 trd_abbr="RIL";
									 }
								 
								 stmt4.close();
								 rset4.close();
									
									if (dom_buy_flag == null) {
										dom_buy_flag = "N";  // Handle null first
									}else if (dom_buy_flag.equals("Y")) {
										dom_buy_flag = "D";
									} else if (dom_buy_flag.equals("K")) {
										dom_buy_flag = "I";
									} else if (dom_buy_flag.equals("T")) {
										dom_buy_flag = "T";
									}else if (dom_buy_flag.equals("N")) {
										dom_buy_flag = "N";
									}
									
									value = trd_abbr+"-"+ sn_no+"-"+cargo_seq_no+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-"+sn_rev_no;
							}
							else { // FOR SELECTING CARGO_SEQ_NO
							
								String cargo_seq = "";
								queryString4="SELECT A.CARGO_SEQ_NO, A.DOM_BUY_FLAG, B.TRD_CD  FROM FMS7_MAN_CONFIRM_CARGO_DTL A, FMS7_MAN_REQ_MST B WHERE A.MAN_CD = B.MAN_CD AND A.CARGO_REF_CD = ?";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1,cargo_ref_cd);
								rset4 = stmt4.executeQuery();
								while(rset4.next()) {
									cargo_seq=rset4.getString(1);
									dom_buy_flag=rset4.getString(2);
									trd_abbr = rset4.getString(3);
					
								}
								stmt4.close();
								rset4.close();
								
								 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
								 stmt4=conn.prepareStatement(queryString4);
								 stmt4.setString(1,trd_abbr);
								 rset4=stmt4.executeQuery();
								 while(rset4.next()) {
									 trd_abbr=rset4.getString(1);
								 }
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 org_abbr=trd_abbr;
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									    }
									 if(trd_abbr.contains("RIL")) {
										 trd_abbr="RIL";
									 }
								 
								 stmt4.close();
								 rset4.close();
							
								
								if (dom_buy_flag == null) {
									dom_buy_flag = "N";  // Handle null first
								}else if (dom_buy_flag.equals("Y")) {
									dom_buy_flag = "D";
								}else if (dom_buy_flag.equals("K")) {
									dom_buy_flag = "I";
								}else if (dom_buy_flag.equals("T")) {
									dom_buy_flag = "T";
								}else if (dom_buy_flag.equals("N")) {
									dom_buy_flag = "N";
								}
								
								value = trd_abbr +"-"+0+"-"+cargo_seq+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-0";
//								cell.setCellValue("'" + value + "'");
							}
							stmt3.close();
							rset3.close();
							
						}
						stmt1.close();
						rset1.close();
						str += value + ",";	
					}
					else if(i==7)//plant _seq_no mapping
					{
						plant_seq_no=rset.getString(8);
						
						
						int plant_seq=0;
						if(org_abbr.contains("CBM"))
						{
							 plant_seq=Integer.parseInt(plant_seq_no);
							 plant_seq=plant_seq+3;
							 plant_seq_no=plant_seq+"";
							
						} 
						//for plant :
						if(mpe.trader_map.containsKey(trd_abbr+"-"+plant_seq_no)   && !org_abbr.contains("CBM") )
						{
							plant_seq_no=mpe.trader_map.get(trd_abbr+"-"+plant_seq_no);
							plant_seq_no=plant_seq_no.split("-")[0];
							value=plant_seq_no;
						}
						else
						{
							value=plant_seq_no;
						}
						
						str += value + ",";	
						
						
					}
					else if(i==12) {
						cont_map_id= rset.getString(1).split("-")[2];
						cargo_seq_no=rset.getString(1).split("-")[5];
						queryString1="SELECT CARGO_REF_CD FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, cont_map_id);
						stmt1.setString(2, cargo_seq_no);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							cargo_ref_cd=rset1.getString(1);
							
							queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, cargo_ref_cd);
							rset2=stmt2.executeQuery();
							while(rset2.next()) {
								value=rset2.getString(1);
								if (value == null) {
								    value = "N";  // Handle null first
								}else if (value.equals("Y")) {
								    value = "D";
								} else if (value.equals("K")) {
								    value = "I";
								} else if (value.equals("T")) {
								    value = "T";
								}else if (value.equals("N")) {
									value = "N";
								}
							}
							stmt2.close();
							rset2.close();

						}
						stmt1.close();
						rset1.close();
						
						str += value + ",";	
					}
					else if(i==15) {
						
						queryString1 = "SELECT ALLOC_BASE_FLAG, ENTRY_HV_GHV, ENTRY_HV_NHV FROM FMS9_PO_ALLOC_MST WHERE CONT_MAPPING_ID = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(1));
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							value = rset1.getString(1);
							base = value;
							
							if(value.equals("GHV")){
								value="GCV";
							}
							else if(value.equals("NHV")) {
								value="NCV";
							}
//							cell.setCellValue("'" + value + "'");
							str += value + ",";	
							
							i++;
//							cell = row.createCell(i);
//							cell.setCellValue("'" + rset1.getString(2) + "'");
							str += rset1.getString(2) + ",";	
							
							i++;
//							cell = row.createCell(i);
//							cell.setCellValue("'" + rset1.getString(3) + "'");
							str += rset1.getString(3) + ",";	
						}
						
						rset1.close();
						stmt1.close();
						

					}
					
					
					else if(i==19) {
						if(base.equals("GCV")) {
							baseVal=gcv;
							deviding_factor=new BigDecimal("1");
						}
						else {
							baseVal=ncv;
							deviding_factor=new BigDecimal("1.11");
						}
						
//						Double result1=Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
//						Double result1 = ((qty_mmbtu.multiply(multiplying_factor)).divide((baseVal.multiply(deviding_factor)))).setScale(2, RoundingMode.FLOOR).doubleValue();
						Double result1 = (qty_mmbtu.multiply(multiplying_factor).divide(baseVal.multiply(deviding_factor), 10, RoundingMode.HALF_UP)).setScale(2, RoundingMode.FLOOR).doubleValue();

						value = Double.toString(result1);
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
				

						
					}
					else {
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
					
				}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
				
				
			}//if condition qty_mmbtu!=0
						
			
			
		}
			stmt.close();
			rset.close();
			
			
			// For remaining data which is in FMS9_PO_ALLOC_MST but not in FMS9_PO_ALLOC_DTL
			queryString="SELECT A.CONT_MAPPING_ID,A.PARTY_CD,0,0,NULL,0,0,'1',0,0,'1',TO_CHAR(A.ALLOC_DT,'DD/MM/YYYY HH24:MI:SS'),A.CONT_MAPPING_ID, "
					+ "TO_CHAR(A.ALLOC_DT-1,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.ALLOC_DT,'HH24:MI'),A.ALLOC_BASE_FLAG,A.ENTRY_HV_GHV,A.ENTRY_HV_NHV,A.ENTRY_TOT_ENE,NULL,A.ENT_BY, "
					+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),0 FROM FMS9_PO_ALLOC_MST A "
					+ "WHERE (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))) "
					+ "AND (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')))  ORDER BY ALLOC_ID";
			
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			

			logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
			
			while(rset.next())
			{	
				if (!ins_map_id.contains(rset.getString(1)+"-"+rset.getString(12)+";")) {
					cont_map_id=rset.getString(1);
					alloc_dt=rset.getString(12);
					base=rset.getString(16);
					qty_mmbtu= BigDecimal.valueOf(rset.getDouble(19));
					str = "";	

						for (int i = 0; i < columns.split(",").length; i++) {
							
							value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
							if(i==0) {
								cont_map_id = cont_map_id.split("-")[2];
								cargo_seq_no=rset.getString(1).split("-")[5];
								queryString1="SELECT CARGO_REF_CD,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, cont_map_id);
								stmt1.setString(2, cargo_seq_no);
								rset1=stmt1.executeQuery();
								while(rset1.next()) {
									cargo_ref_cd=rset1.getString(1);
									dom_buy_flag=rset1.getString(2);

									
									queryString3="SELECT CUSTOMER_CD,FGSA_NO,FGSA_REV_NO ,SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG = ?  ORDER BY  CUSTOMER_CD,DOM_BUY_FLAG,SN_NO ,SN_REV_NO DESC ";
									stmt3=conn.prepareStatement(queryString3);
									stmt3.setString(1,"%"+cargo_ref_cd+"%");
									stmt3.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);
									rset3=stmt3.executeQuery();
									if(rset3.next()) {
										trd_cd=rset3.getString(1);
//										if(cargo_ref_cd.equals("23031")) {
//										System.out.println(cargo_ref_cd+"=="+trd_cd);
//										}
										fgsa_no=rset3.getString(2);
										fgsa_rev_no=rset3.getString(3);
										sn_no=rset3.getString(4);
										sn_rev_no=rset3.getString(5);
//										dom_buy_flag=rset3.getString(6);
										 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
										 stmt4=conn.prepareStatement(queryString4);
										 stmt4.setString(1,trd_cd);
										 rset4=stmt4.executeQuery();
										 while(rset4.next()) {
											 trd_abbr=rset4.getString(1);
										 }
											 trd_abbr=trd_abbr.trim();
											 value=trd_abbr;
											 if (mpe.counterparty_map.containsKey(value)) {
											        value =mpe.counterparty_map.get(value); 
											        trd_abbr = value; 
											    }
											 if(trd_abbr.contains("RIL")) {
												 trd_abbr="RIL";
											 }
										 
										 stmt4.close();
										 rset4.close();
											
											if (dom_buy_flag == null) {
												dom_buy_flag = "N";  // Handle null first
											}else if (dom_buy_flag.equals("Y")) {
												dom_buy_flag = "D";
											} else if (dom_buy_flag.equals("K")) {
												dom_buy_flag = "I";
											} else if (dom_buy_flag.equals("T")) {
												dom_buy_flag = "T";
											}else if (dom_buy_flag.equals("N")) {
												dom_buy_flag = "N";
											}
											
											value = trd_abbr+"-"+ sn_no+"-"+cargo_seq_no+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-"+sn_rev_no;
									}
									else { // FOR SELECTING CARGO_SEQ_NO
									
										String cargo_seq = "";
										queryString4="SELECT A.CARGO_SEQ_NO, A.DOM_BUY_FLAG, B.TRD_CD  FROM FMS7_MAN_CONFIRM_CARGO_DTL A, FMS7_MAN_REQ_MST B WHERE A.MAN_CD = B.MAN_CD AND A.CARGO_REF_CD = ?";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1,cargo_ref_cd);
										rset4 = stmt4.executeQuery();
										while(rset4.next()) {
											cargo_seq=rset4.getString(1);
											dom_buy_flag=rset4.getString(2);
											trd_abbr = rset4.getString(3);
							
										}
										stmt4.close();
										rset4.close();
										
										 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
										 stmt4=conn.prepareStatement(queryString4);
										 stmt4.setString(1,trd_abbr);
										 rset4=stmt4.executeQuery();
										 while(rset4.next()) {
											 trd_abbr=rset4.getString(1);
										 }
											 trd_abbr=trd_abbr.trim();
											 value=trd_abbr;
											 if (mpe.counterparty_map.containsKey(value)) {
											        value =mpe.counterparty_map.get(value); 
											        trd_abbr = value; 
											    }
											 if(trd_abbr.contains("RIL")) {
												 trd_abbr="RIL";
											 }
										 
										 stmt4.close();
										 rset4.close();
									
										
										if (dom_buy_flag == null) {
											dom_buy_flag = "N";  // Handle null first
										}else if (dom_buy_flag.equals("Y")) {
											dom_buy_flag = "D";
										}else if (dom_buy_flag.equals("K")) {
											dom_buy_flag = "I";
										}else if (dom_buy_flag.equals("T")) {
											dom_buy_flag = "T";
										}else if (dom_buy_flag.equals("N")) {
											dom_buy_flag = "N";
										}
										
										value = trd_abbr +"-"+0+"-"+cargo_seq+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-0";
//										cell.setCellValue("'" + value + "'");
									}
									stmt3.close();
									rset3.close();
									
								}
								stmt1.close();
								rset1.close();
//							cell.setCellValue("'" + value + "'");
								str += value + ",";	
							}
							
							else if(i==10)//BU Unit
							{
												
//								queryString4="SELECT B.TRADER_ABBR,C.CARGO_SEL,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,PLANT_SEQ_NO,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL"
//										+ " FROM FMS7_TRADER_SUPPLIER_PLANT_MST A , FMS7_TRADER_MST B , FMS7_TRADER_CONT_MST C WHERE A.CUSTOMER_CD=B.TRADER_CD"
//										+ "  AND A.FGSA_NO=C.FGSA_NO AND A.FGSA_REV_NO=C.FGSA_REV_NO AND A.SN_NO=C.SN_NO AND A.CUSTOMER_CD=C.CUSTOMER_CD AND A.SN_REV_NO=C.SN_REV_NO AND "
//										+ " (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
//										+ "ORDER BY  A.CUSTOMER_CD,A.FLAG,A.SN_NO , A.SN_REV_NO DESC";
								queryString4="SELECT A.PLANT_SEQ_NO "
										+ "FROM FMS7_TRADER_SUPPLIER_PLANT_MST A ,FMS7_TRADER_MST B ,FMS7_TRADER_CONT_MST C "
										+ "WHERE C.CUSTOMER_CD =? AND C.FGSA_NO=? AND "
										+ "C.FGSA_REV_NO=?   AND C.SN_NO = ?  AND C.SN_REV_NO = ? AND  A.CUSTOMER_CD=B.TRADER_CD AND A.FGSA_NO=C.FGSA_NO AND A.FGSA_REV_NO=C.FGSA_REV_NO "
										+ "AND A.SN_NO=C.SN_NO AND A.CUSTOMER_CD=C.CUSTOMER_CD AND A.SN_REV_NO=C.SN_REV_NO " ;
								stmt4=conn.prepareStatement(queryString4);
								stmt4.setString(1,trd_cd);
								stmt4.setString(2,fgsa_no);
								stmt4.setString(3,fgsa_rev_no);
								stmt4.setString(4,sn_no);
								stmt4.setString(5,sn_rev_no);
								rset4=stmt4.executeQuery();
								if(rset4.next()) {
									int no=rset4.getInt(1);
									no=no+1;
									value=no+"";
									
								}
								else {
									value="1";
								}
								stmt4.close();
								rset4.close();
								str += value + ",";	
							
								
//								if(cargo_ref_cd.equals("22034"))
//								{
//									System.out.println(""+value);
//									System.out.println("====>"+trd_cd+":"+fgsa_no+":"+sn_no);
//								}
												
								}
							else if(i==12) {
								cont_map_id= rset.getString(1).split("-")[2];
								cargo_seq_no=rset.getString(1).split("-")[5];
								queryString1="SELECT CARGO_REF_CD FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, cont_map_id);
								stmt1.setString(2, cargo_seq_no);
								rset1=stmt1.executeQuery();
								while(rset1.next()) {
									cargo_ref_cd=rset1.getString(1);
									
									queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
									stmt2=conn.prepareStatement(queryString2);
									stmt2.setString(1, cargo_ref_cd);
									rset2=stmt2.executeQuery();
									while(rset2.next()) {
										value=rset2.getString(1);
										if (value == null) {
										    value = "N";  // Handle null first
										}else if (value.equals("Y")) {
										    value = "D";
										} else if (value.equals("K")) {
										    value = "I";
										} else if (value.equals("T")) {
										    value = "T";
										}else if (value.equals("N")) {
											value = "N";
										}
									}
									stmt2.close();
									rset2.close();

								}
								stmt1.close();
								rset1.close();
								
//								cell.setCellValue("'" + value + "'");
								str += value + ",";	

							}
							else if(i==15) {
								if(value.equals("GHV")){
									value="GCV";
								}
								else if(value.equals("NHV")) {
									value="NCV";
								}
//								cell.setCellValue("'" + value + "'");
								str += value + ",";	

							}
							
							
							else if(i==19) {
								if(base.equals("GCV")) {
									baseVal=gcv;
									deviding_factor=new BigDecimal("1");
								}
								else {
									baseVal=ncv;
									deviding_factor=new BigDecimal("1.11");
								}
//								Double result1=Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
//								Double result1 = ((qty_mmbtu.multiply(multiplying_factor)).divide((baseVal.multiply(deviding_factor)))).setScale(2, RoundingMode.FLOOR).doubleValue();
								Double result1 = (qty_mmbtu.multiply(multiplying_factor).divide(baseVal.multiply(deviding_factor), 10, RoundingMode.HALF_UP)).setScale(2, RoundingMode.FLOOR).doubleValue();

								value = Double.toString(result1);
//								cell.setCellValue("'" + value + "'");
								str += value + ",";	

								
							}
							else {
								str += value + ",";	
							}
							
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
//					}

				}
				
			}
				stmt.close();
				rset.close();

			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BUY_DAILY_BUYER_NOM>>,", conn);
		
						
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_BUY_DAILY_BUYER_NOM>>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
	}
	catch(Exception e){

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);

	}
}

public void FMS_TRADER_CONT_BU() throws SQLException, IOException {
	
	function_nm = "FMS_TRADER_CONT_BU()";
	String base="",plant_seq_no="",plant_abbr="";
	String cont_map_id="",alloc_dt="",trd_abbr="",fgsa_no="",fgsa_rev_no="",sn_no="",sn_rev_no="",cargo_sel="",cargo_seq="",dom_buy_flag="";
	int m=1;
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_TRADER_CONT_BU>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT,CONTRACT_TYPE";
		
		workbook = new XSSFWorkbook();
		spreadsheet = workbook.createSheet("Sheet 1");
		
		nrow = 0;
		count = 0;
		String cd="";
//		Below block of code is for inserting columns
		row = spreadsheet.createRow(nrow++);

		for (int i = 0; i < columns.split(",").length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columns.split(",")[i]);
		}

		
//	
		queryString = "SELECT B.TRADER_ABBR,C.CARGO_SEL,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,PLANT_SEQ_NO,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL"
				+ " FROM FMS7_TRADER_SUPPLIER_PLANT_MST A , FMS7_TRADER_MST B , FMS7_TRADER_CONT_MST C WHERE A.CUSTOMER_CD=B.TRADER_CD"
				+ "  AND A.FGSA_NO=C.FGSA_NO AND A.FGSA_REV_NO=C.FGSA_REV_NO AND A.SN_NO=C.SN_NO AND A.CUSTOMER_CD=C.CUSTOMER_CD AND A.SN_REV_NO=C.SN_REV_NO AND "
				+ " (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
				+ "ORDER BY  A.CUSTOMER_CD,A.FLAG,A.SN_NO , A.SN_REV_NO DESC";
		
		
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		
		String seq="";

		logger.checkpoint(fname, "TRADER_ABBR,FGSA_NO,FGSA_REV_NO,SN_NO,SN_REV_NO,TIMESTAMP", conn);
		
		while(rset.next())
		{		
			trd_abbr=rset.getString(1);
			cargo_sel=rset.getString(2);
			fgsa_no=rset.getString(3);
			fgsa_rev_no=rset.getString(4);
			sn_no=rset.getString(5);
			sn_rev_no=rset.getString(6);
			plant_seq_no=rset.getString(7);
//			dom_buy_flag=rset.getString(10);
			
			if(cargo_sel!=null) {
				seq=cargo_sel;
			}
			
			
			
			for (int j= 0; j <seq.split("@").length; j++) {
				
				row = spreadsheet.createRow(nrow++);
			
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
//					
					if(i==0) {
						queryString1="SELECT CARGO_SEQ_NO, DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, seq.split("@")[j]);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							cargo_seq=rset1.getString(1);
							dom_buy_flag=rset1.getString(2);
						}
						stmt1.close();
						rset1.close();
						trd_abbr=trd_abbr.trim();
						value=trd_abbr;
						if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        trd_abbr = value; 
						    }
						
						if(trd_abbr.contains("RIL")) {
							trd_abbr="RIL";
						}
						if (dom_buy_flag == null) {
							dom_buy_flag = "N";  // Handle null first
						}else if (dom_buy_flag.equals("Y")) {
							dom_buy_flag = "D";
						} else if (dom_buy_flag.equals("K")) {
							dom_buy_flag = "I";
						} else if (dom_buy_flag.equals("T")) {
							dom_buy_flag = "T";
						}else if (dom_buy_flag.equals("N")) {
							dom_buy_flag = "N";
						}
						
						
						value = trd_abbr+"-"+sn_no+"-"+cargo_seq+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no;
					}
					
					if(i==1) {
						value=seq.split("@")[j];
					}
					
					if(i==6) {
						if(plant_seq_no.equals("0")) {
							plant_abbr="SEIPL-GJ";
						}
						else {
							queryString1="SELECT PLANT_SHORT_ABBR FROM FMS7_SUPPLIER_PLANT_DTL WHERE SEQ_NO = ?";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1,plant_seq_no);
							rset1=stmt1.executeQuery();
							while(rset1.next()) {
								plant_abbr=rset1.getString(1);
							}
							stmt1.close();
							rset1.close();
							
						}
						value=plant_abbr;
						cell.setCellValue("'" + value + "'");

					}
					
					if(i==9) {// VALUE OF PIPELINE GAS CHANGING IN EMS
						queryString1="SELECT  DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, seq.split("@")[j]);
						rset1=stmt1.executeQuery();
						if(rset1.next()) {
							value=rset1.getString(1);


							if (value == null) {
							    value = "N";  // Handle null first
							}else if (value.equals("Y")) {
							    value = "D";
							} else if (value.equals("K")) {
							    value = "I";
							} else if (value.equals("T")) {
							    value = "T";
							}else if (value.equals("N")) {
							    value = "N";
							}
						}
						stmt1.close();
						rset1.close();
					}
					
					
				cell.setCellValue("'" + value + "'");
					
					
				}
		
				count++;
				logger.data(fname, (trd_abbr+","+fgsa_no+","+fgsa_rev_no+","+sn_no+","+sn_rev_no+","), conn, "");
			}
		}
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_TRADER_CONT_BU_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_CONT_BU>>,", conn);

						
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TRADER_CONT_BU>>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
	}
	catch(Exception e){

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);

	}
}

public void FMS_TRADER_CONT_PLANT() throws SQLException, IOException {
	
	function_nm = "FMS_TRADER_CONT_PLANT()";
	String sn_no,sn_rev_no,fgsa_no,fgsa_rev_no,supp_split_value="",ent_by="";
	String plant_seq_no="", dom_buy_flag="",cargo_sel="",cargo_seq="",trd_abbr="",customer_cd="",ent_dt="",split_value_str="",org_abbr="";
//	double plant_no=1d;
	String cd="";
	BigDecimal split_plant_count = new BigDecimal("1");
	double split_value = 0d,split = 0d;
	BigDecimal plant_no = new BigDecimal("1");
	BigDecimal one = new BigDecimal("1");
			

	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_TRADER_CONT_PLANT>>,,,,,,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE";
		
		workbook = new XSSFWorkbook();
		spreadsheet = workbook.createSheet("Sheet 1");
		
		nrow = 0;
		count = 0;
		
//		Below block of code is for inserting columns
		row = spreadsheet.createRow(nrow++);

		for (int i = 0; i < columns.split(",").length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columns.split(",")[i]);
		}
	
		
//	    queryString = "SELECT B.CUSTOMER_CD,C.CARGO_SEL,B.FGSA_NO,B.FGSA_REV_NO,B.SN_NO,B.SN_REV_NO,B.PLANT_SEQ_NO,B.SPLIT_VALUE,B.EMP_CD,"
//	    		+ "TO_CHAR(B.ENT_DT,'DD/MM/YYYY hh24:mi:ss'),B.FLAG FROM  FMS7_TRADER_PLANT_MST B , FMS7_TRADER_CONT_MST C"
//	    		+ "  WHERE  B.SN_NO=C.SN_NO AND B.SN_REV_NO=C.SN_REV_NO AND B.FGSA_NO=C.FGSA_NO AND B.FGSA_REV_NO=C.FGSA_REV_NO"
//	    		+ " AND B.CUSTOMER_CD=C.CUSTOMER_CD AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//	    		+ " AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
//	    		+ "ORDER BY  C.CUSTOMER_CD,C.DOM_BUY_FLAG,C.SN_NO , C.SN_REV_NO DESC"; 
		
		queryString="SELECT A.CUSTOMER_CD,B.CARGO_SEL,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.PLANT_SEQ_NO,A.SPLIT_VALUE,A.EMP_CD,"//7=PLANT
				+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL FROM FMS7_TRADER_PLANT_MST A , FMS7_TRADER_CONT_MST B WHERE "
				+ "A.SN_NO=B.SN_NO AND A.SN_REV_NO=B.SN_REV_NO AND A.FGSA_NO=B.FGSA_NO AND A.FGSA_REV_NO=B.FGSA_REV_NO AND"
				+ " A.FLAG=B.DOM_BUY_FLAG AND A.CUSTOMER_CD=B.CUSTOMER_CD AND  (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
				+ " AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))ORDER BY  B.CUSTOMER_CD,B.DOM_BUY_FLAG,B.SN_NO,B.SN_REV_NO DESC, A.PLANT_SEQ_NO desc ";

		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3,delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();

		logger.checkpoint(fname, "CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,CONTRACT_TYPE,TIMESTAMP", conn);
		
		String seq="";
		while(rset.next())
		{
			
			customer_cd=rset.getString(1);
			cargo_sel=rset.getString(2);
			fgsa_no=rset.getString(3);
			fgsa_rev_no=rset.getString(4);
			sn_no=rset.getString(5);
			sn_rev_no=rset.getString(6);
			
			plant_seq_no=rset.getString(7);
			
			split_value_str=rset.getString(8);
			ent_dt=rset.getString(10);
			ent_by=rset.getString(9);
			
			
			if(cargo_sel!=null) {
				seq=cargo_sel;
			}
			
			if(split_value_str!=null) {
				
				split_value=Double.parseDouble(split_value_str);
				
			}
			else {
				split_value=0;
				value=null;
			}
			
			
			String flag="1";		
			for (int j= 0; j <seq.split("@").length; j++) {
				plant_seq_no=rset.getString(7);
				do {
					row = spreadsheet.createRow(nrow++);
					
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
						if(i ==0) {
						queryString1="SELECT CARGO_SEQ_NO,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1,seq.split("@")[j]);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							cargo_seq=rset1.getString(1);
							dom_buy_flag=rset1.getString(2);
						}
						stmt1.close();
						rset1.close();
						
						queryString2="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1,customer_cd);
						rset2=stmt2.executeQuery();
						while(rset2.next()) {
							trd_abbr=rset2.getString(1);
							abbr=trd_abbr;
						}
						stmt2.close();
						rset2.close();
						trd_abbr=trd_abbr.trim();
						value=trd_abbr;
						
						org_abbr=trd_abbr;
						
						if (mpe.counterparty_map.containsKey(value)) {
							value =mpe.counterparty_map.get(value); 
							trd_abbr = value;
						}
						if(trd_abbr.contains("RIL")) {
							trd_abbr="RIL";
						}
						
						int plant_seq=0;
						if(org_abbr.contains("CBM"))
						{
//							System.out.println("--->>"+plant_seq_no);
							 plant_seq=Integer.parseInt(plant_seq_no);
							 plant_seq=plant_seq+3;
							 plant_seq_no=plant_seq+"";
//							 System.out.println("->>"+plant_seq_no);
							
						}
						
						if(mpe.trader_map.containsKey(trd_abbr+"-"+plant_seq_no)  && !org_abbr.contains("CBM"))
						{
							flag="1";

							if( ((trd_abbr+"-"+plant_seq_no).equals("RIL-2") || (trd_abbr+"-"+plant_seq_no).equals("BP-2") ) && !abbr.contains("CBM") )
							{
								flag="0";
								
							}
							plant_seq_no=mpe.trader_map.get(trd_abbr+"-"+plant_seq_no);
							plant_seq_no=plant_seq_no.split("-")[0];

						}
						
						if (dom_buy_flag == null) {
							dom_buy_flag = "N";  // Handle null first
						}else if (dom_buy_flag.equals("Y")) {
							dom_buy_flag = "D";
						} else if (dom_buy_flag.equals("K")) {
							dom_buy_flag = "I";
						} else if (dom_buy_flag.equals("T")) {
							dom_buy_flag = "T";
						}else if (dom_buy_flag.equals("N")) {
							dom_buy_flag = "N";
						}
							
						value = trd_abbr+"-"+sn_no+"-"+cargo_seq+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no;
						
						cell.setCellValue("'" + value + "'");
						
					}
						else if(i==6)//plant_seq_no
						{
							value=plant_seq_no;
							cell.setCellValue("'" + value + "'");	
						}
						else if(i==7) {
							
						queryString1="SELECT COUNT(PLANT_SEQ_NO) FROM FMS7_TRADER_PLANT_MST WHERE "
								+ "SN_NO=? AND SN_REV_NO=? AND FGSA_NO=? AND FGSA_REV_NO=? AND CUSTOMER_CD=? AND ENT_DT=TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
						
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1,sn_no);
						stmt1.setString(2,sn_rev_no);
						stmt1.setString(3,fgsa_no);
						stmt1.setString(4,fgsa_rev_no);
						stmt1.setString(5,customer_cd);
						stmt1.setString(6,ent_dt);
						rset1 = stmt1.executeQuery();
						while(rset1.next()) {
							
							value = rset1.getString(1);
							plant_no=new BigDecimal(value);
						}
						stmt1.close();
						rset1.close();
						 
					
						
						if(flag.equals("0"))
						{
							plant_no=new BigDecimal("0");
						}
						
						String temp_var="",temp_var2="";
						temp_var=plant_no+"";
						plant_no1=Integer.parseInt(temp_var);
						
						temp_var2=split_plant_count+"";
						split_plant_count1=Integer.parseInt(temp_var2);
						
						if(supp_split_value.equals(sn_no+"-"+sn_rev_no+"-"+customer_cd+"-"+ent_by) && split_plant_count1==plant_no1  && plant_no1>1) {
//						if(supp_split_value.equals(sn_no+"-"+sn_rev_no+"-"+customer_cd+"-"+ent_by) && split_plant_count.compareTo(plant_no) == 0 && plant_no.compareTo(one) > 1) {
							
							split = BigDecimal.valueOf(split_value)
							        .subtract(
							            plant_no.subtract(one)
							                    .multiply(BigDecimal.valueOf(Math.floor(split * 100) / 100))
							        ).doubleValue();
//							split=split+Double.parseDouble(value);
							split_plant_count=one;
				
						}
						else {
							supp_split_value=sn_no+"-"+sn_rev_no+"-"+customer_cd+"-"+ent_by;
							split_plant_count = split_plant_count.add(one);
						
							
							
							if(plant_no1 >=1)
							{
									split_plant_count=new BigDecimal(value);
							
							}
							
							temp_var2=split_plant_count+"";
							split_plant_count1=Integer.parseInt(temp_var2);
//							if(plant_no.compareTo(one) == 1) {
//								split_plant_count=new BigDecimal(value);
//							}
//							if(plant_no.compareTo(new BigDecimal("0")) == 1) {
//								
//							split =  BigDecimal.valueOf(split_value).divide(plant_no).doubleValue();
//							}
							if(plant_no1 > 0)
							{
								split =  BigDecimal.valueOf(split_value).divide(plant_no).doubleValue();
//							
							}
							else {
								split = split_value;
				
							}
						}
						value =  split+"";
						cell.setCellValue("'" + value + "'");
					}
						else if(i==10) {
						queryString1="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD=?";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1,seq.split("@")[j]);
						rset1 = stmt1.executeQuery();
						while(rset1.next()) {
							value=rset1.getString(1);
							
							if (value == null) {
							    value = "N";  // Handle null first
							}else if (value.equals("Y")) {
							    value = "D";
							} else if (value.equals("K")) {
							    value = "I";
							} else if (value.equals("T")) {
							    value = "T";
							}else if (value.equals("N")) {
							    value = "N";
							}
						}
						stmt1.close();
						rset1.close();
						cell.setCellValue("'" + value + "'");
					}
					else {
						
						cell.setCellValue("'" + value + "'");
						}

		}
				count++;
				logger.data(fname, (cd + "," + fgsa_no + "," +fgsa_rev_no+ "," +sn_no+"," + sn_rev_no +","+plant_seq_no+","+dom_buy_flag+","), conn, "");
				
				

			}
				while(split_plant_count1<plant_no1);
//				split_plant_count.compareTo(plant_no) == -1
			}
			
		}
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_TRADER_CONT_PLANT_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_CONT_PLANT>>,,,,,,", conn);
									
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TRADER_CONT_PLANT>>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
	}
	catch(Exception e){

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);

	}
}

public void FMS_BUY_DAILY_SELLER_NOM() throws SQLException, IOException {
	
	function_nm = "FMS_BUY_DAILY_SELLER_NOM()";
	String base="",trd_cd="";
	String cont_map_id="",cargo_ref_cd="",cont_type="",alloc_dt="",trd_abbr="",fgsa_no="",fgsa_rev_no="",sn_no="",sn_rev_no="",plant_seq_no="",bu_seq_no="",cargo_seq_no="",dom_buy_flag="";

	BigDecimal baseVal=new BigDecimal("0");
	BigDecimal deviding_factor=new BigDecimal("0");
	double cal=0.252*1000000;
	BigDecimal multiplying_factor=  BigDecimal.valueOf(cal);
	BigDecimal gcv=new BigDecimal("9802.8");
	BigDecimal ncv=new BigDecimal("8831.35");
	BigDecimal qty_mmbtu=new BigDecimal("0");
	int supp_num=0;
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_BUY_DAILY_SELLER_NOM>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO";
		
		count = 0;
		String cd="";
		
		String ins_map_id="";
		String fname_csv = "", str = "", gas_dt="";
		plant_seq_no="";
		fname_csv = migration_setup_dir + "EXPORT/FMS_BUY_DAILY_SELLER_NOM_" + start_end_dt + ".csv";
		
		FileWriter fw = new FileWriter(fname_csv, false); 
		fw.close();

		for (int i = 0; i < columns.split(",").length; i++) {
			str += columns.split(",")[i] + ",";
		}

		queryString="SELECT A.CONT_MAPPING_ID, A.PARTY_CD, 0, 0, NULL, 0, 0, A.PLANT_SEQ_NO, 0, 0, (A.SUPPLIER_SEQ+1), TO_CHAR(A.ALLOC_DT,'DD/MM/YYYY HH24:MI:SS'), A.CONT_MAPPING_ID, "
				+ "TO_CHAR(A.ALLOC_DT-1,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.ALLOC_DT,'HH24:MI'),NULL,NULL,NULL,A.ENTRY_TOT_ENE,NULL,A.ENT_BY, "
				+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),0 FROM FMS9_PO_ALLOC_DTL A "
				+ "WHERE (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))) AND (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')))";
		
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		
		logger.insert_data(fname_csv, str, conn);
		
		logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
		
		while(rset.next())
		{	
			cont_map_id=rset.getString(1);
			alloc_dt=rset.getString(12);
			qty_mmbtu= BigDecimal.valueOf(rset.getDouble(19));
			String qty_mmbtu1="";
			qty_mmbtu1=rset.getString(19);
			
			
			str = "";
			if (!ins_map_id.contains(rset.getString(1)+"-"+rset.getString(12)+";")) {
				ins_map_id += rset.getString(1)+"-"+rset.getString(12)+";";
			} 
					
			if(!qty_mmbtu1.equals("0"))
			{
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
//					cell = row.createCell(i);
					if(i==0) {
						cont_map_id = cont_map_id.split("-")[2];
						cargo_seq_no=rset.getString(1).split("-")[5];
						queryString1="SELECT CARGO_REF_CD,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, cont_map_id);
						stmt1.setString(2, cargo_seq_no);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							cargo_ref_cd=rset1.getString(1);
							dom_buy_flag=rset1.getString(2);

							
							queryString3="SELECT CUSTOMER_CD,FGSA_NO,FGSA_REV_NO ,SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG = ?  ORDER BY  CUSTOMER_CD,DOM_BUY_FLAG,SN_NO ,SN_REV_NO DESC ";
							stmt3=conn.prepareStatement(queryString3);
							stmt3.setString(1,"%"+cargo_ref_cd+"%");
							stmt3.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);
							rset3=stmt3.executeQuery();
							if(rset3.next()) {
								trd_cd=rset3.getString(1);
								fgsa_no=rset3.getString(2);
								fgsa_rev_no=rset3.getString(3);
								sn_no=rset3.getString(4);
								sn_rev_no=rset3.getString(5);
//								dom_buy_flag=rset3.getString(6);
								 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
								 stmt4=conn.prepareStatement(queryString4);
								 stmt4.setString(1,trd_cd);
								 rset4=stmt4.executeQuery();
								 while(rset4.next()) {
									 trd_abbr=rset4.getString(1);
								 }
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 org_abbr=trd_abbr;
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									    }
									 if(trd_abbr.contains("RIL")) {
										 trd_abbr="RIL";
									 }
								 
								 stmt4.close();
								 rset4.close();
									
									if (dom_buy_flag == null) {
										dom_buy_flag = "N";  // Handle null first
									}else if (dom_buy_flag.equals("Y")) {
										dom_buy_flag = "D";
									} else if (dom_buy_flag.equals("K")) {
										dom_buy_flag = "I";
									} else if (dom_buy_flag.equals("T")) {
										dom_buy_flag = "T";
									}else if (dom_buy_flag.equals("N")) {
										dom_buy_flag = "N";
									}
									
									value = trd_abbr+"-"+ sn_no+"-"+cargo_seq_no+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-"+sn_rev_no;
							}
							else { // FOR SELECTING CARGO_SEQ_NO
							
								String cargo_seq = "";
								queryString4="SELECT A.CARGO_SEQ_NO, A.DOM_BUY_FLAG, B.TRD_CD  FROM FMS7_MAN_CONFIRM_CARGO_DTL A, FMS7_MAN_REQ_MST B WHERE A.MAN_CD = B.MAN_CD AND A.CARGO_REF_CD = ?";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1,cargo_ref_cd);
								rset4 = stmt4.executeQuery();
								while(rset4.next()) {
									cargo_seq=rset4.getString(1);
									dom_buy_flag=rset4.getString(2);
									trd_abbr = rset4.getString(3);
					
								}
								stmt4.close();
								rset4.close();
								
								 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
								 stmt4=conn.prepareStatement(queryString4);
								 stmt4.setString(1,trd_abbr);
								 rset4=stmt4.executeQuery();
								 while(rset4.next()) {
									 trd_abbr=rset4.getString(1);
								 }
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 org_abbr=trd_abbr;
									 
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									    }
									 if(trd_abbr.contains("RIL")) {
										 trd_abbr="RIL";
									 }
								 
								 stmt4.close();
								 rset4.close();
							
								
								if (dom_buy_flag == null) {
									dom_buy_flag = "N";  // Handle null first
								}else if (dom_buy_flag.equals("Y")) {
									dom_buy_flag = "D";
								}else if (dom_buy_flag.equals("K")) {
									dom_buy_flag = "I";
								}else if (dom_buy_flag.equals("T")) {
									dom_buy_flag = "T";
								}else if (dom_buy_flag.equals("N")) {
									dom_buy_flag = "N";
								}
								
								value = trd_abbr +"-"+0+"-"+cargo_seq+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-0";
//								cell.setCellValue("'" + value + "'");
							}
							stmt3.close();
							rset3.close();
							
						}
						stmt1.close();
						rset1.close();
//					cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
					else if(i==7)//plant _seq_no mapping
					{
						plant_seq_no=rset.getString(8);
						
						int plant_seq=0;
						if(org_abbr.contains("CBM"))
						{
							 plant_seq=Integer.parseInt(plant_seq_no);
							 plant_seq=plant_seq+3;
							 plant_seq_no=plant_seq+"";
							
						} 
						//for plant :
						if(mpe.trader_map.containsKey(trd_abbr+"-"+plant_seq_no)   && !org_abbr.contains("CBM") )
						{
							plant_seq_no=mpe.trader_map.get(trd_abbr+"-"+plant_seq_no);
							plant_seq_no=plant_seq_no.split("-")[0];
							value=plant_seq_no;
						}
						
						else
						{
							value=plant_seq_no;
						}
						
						str += value + ",";	
						
						
					}
					else if(i==12) {
						cont_map_id= rset.getString(1).split("-")[2];
						cargo_seq_no=rset.getString(1).split("-")[5];
						queryString1="SELECT CARGO_REF_CD FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, cont_map_id);
						stmt1.setString(2, cargo_seq_no);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							cargo_ref_cd=rset1.getString(1);
							
							queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, cargo_ref_cd);
							rset2=stmt2.executeQuery();
							while(rset2.next()) {
								value=rset2.getString(1);
								if (value == null) {
								    value = "N";  // Handle null first
								}else if (value.equals("Y")) {
								    value = "D";
								} else if (value.equals("K")) {
								    value = "I";
								} else if (value.equals("T")) {
								    value = "T";
								}else if (value.equals("N")) {
									value = "N";
								}
							}
							stmt2.close();
							rset2.close();

						}
						stmt1.close();
						rset1.close();
						
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
					
					else if(i==15) {
						
						queryString1 = "SELECT ALLOC_BASE_FLAG, ENTRY_HV_GHV, ENTRY_HV_NHV FROM FMS9_PO_ALLOC_MST WHERE CONT_MAPPING_ID = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(1));
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							value = rset1.getString(1);
							base = value;
							
							if(value.equals("GHV")){
								value="GCV";
							}
							else if(value.equals("NHV")) {
								value="NCV";
							}
//							cell.setCellValue("'" + value + "'");
							str += value + ",";	
							
							i++;
//							cell = row.createCell(i);
//							cell.setCellValue("'" + rset1.getString(2) + "'");
							str += rset1.getString(2) + ",";	
							
							i++;
//							cell = row.createCell(i);
//							cell.setCellValue("'" + rset1.getString(3) + "'");
							str += rset1.getString(3) + ",";	
						}
						
						rset1.close();
						stmt1.close();
						

					}
					
					
					else if(i==19) {
						if(base.equals("GCV")) {
							baseVal=gcv;
							deviding_factor=new BigDecimal("1");
						}
						else {
							baseVal=ncv;
							deviding_factor=new BigDecimal("1.11");
						}
//						Double result1=Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
//						Double result1 = ((qty_mmbtu.multiply(multiplying_factor)).divide((baseVal.multiply(deviding_factor)))).setScale(2, RoundingMode.FLOOR).doubleValue();
						Double result1 = (qty_mmbtu.multiply(multiplying_factor).divide(baseVal.multiply(deviding_factor), 10, RoundingMode.HALF_UP)).setScale(2, RoundingMode.FLOOR).doubleValue();

						value = Double.toString(result1);
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
						
					}
					else {
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
					
					
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
				
			}//qty_mmbtu1 not equals(0)

		}
			stmt.close();
			rset.close();
			
			
			// For remaining data which is in FMS9_PO_ALLOC_MST but not in FMS9_PO_ALLOC_DTL
			queryString="SELECT A.CONT_MAPPING_ID,A.PARTY_CD,0,0,NULL,0,0,'1',0,0,'1',TO_CHAR(A.ALLOC_DT,'DD/MM/YYYY HH24:MI:SS'),A.CONT_MAPPING_ID, "
					+ "TO_CHAR(A.ALLOC_DT-1,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.ALLOC_DT,'HH24:MI'),A.ALLOC_BASE_FLAG,A.ENTRY_HV_GHV,A.ENTRY_HV_NHV,A.ENTRY_TOT_ENE,NULL,A.ENT_BY, "
					+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),0 FROM FMS9_PO_ALLOC_MST A "
					+ "WHERE (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))) AND (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))) ORDER BY ALLOC_ID";
			
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			

			logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
			
			while(rset.next())
			{	
				if (!ins_map_id.contains(rset.getString(1)+"-"+rset.getString(12)+";")) {
					cont_map_id=rset.getString(1);
					alloc_dt=rset.getString(12);
					base=rset.getString(16);
					
					qty_mmbtu= BigDecimal.valueOf(rset.getDouble(19));
					str = "";
					
							for (int i = 0; i < columns.split(",").length; i++) {
								
								value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
//								cell = row.createCell(i);
								if(i==0) {
									cont_map_id = cont_map_id.split("-")[2];
									cargo_seq_no=rset.getString(1).split("-")[5];
									queryString1="SELECT CARGO_REF_CD,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
									stmt1=conn.prepareStatement(queryString1);
									stmt1.setString(1, cont_map_id);
									stmt1.setString(2, cargo_seq_no);
									rset1=stmt1.executeQuery();
									while(rset1.next()) {
										cargo_ref_cd=rset1.getString(1);
										dom_buy_flag=rset1.getString(2);

										
										queryString3="SELECT CUSTOMER_CD,FGSA_NO,FGSA_REV_NO ,SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG = ?  ORDER BY  CUSTOMER_CD,DOM_BUY_FLAG,SN_NO ,SN_REV_NO DESC ";
										stmt3=conn.prepareStatement(queryString3);
										stmt3.setString(1,"%"+cargo_ref_cd+"%");
										stmt3.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);
										rset3=stmt3.executeQuery();
										if(rset3.next()) {
											trd_cd=rset3.getString(1);
//											if(cargo_ref_cd.equals("23031")) {
//											System.out.println(cargo_ref_cd+"=="+trd_cd);
//											}
											fgsa_no=rset3.getString(2);
											fgsa_rev_no=rset3.getString(3);
											sn_no=rset3.getString(4);
											sn_rev_no=rset3.getString(5);
//											dom_buy_flag=rset3.getString(6);
											 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
											 stmt4=conn.prepareStatement(queryString4);
											 stmt4.setString(1,trd_cd);
											 rset4=stmt4.executeQuery();
											 while(rset4.next()) {
												 trd_abbr=rset4.getString(1);
											 }
												 trd_abbr=trd_abbr.trim();
												 value=trd_abbr;
												 if (mpe.counterparty_map.containsKey(value)) {
												        value =mpe.counterparty_map.get(value); 
												        trd_abbr = value; 
												    }
												 if(trd_abbr.contains("RIL")) {
													 trd_abbr="RIL";
												 }
											 
											 stmt4.close();
											 rset4.close();
												
												if (dom_buy_flag == null) {
													dom_buy_flag = "N";  // Handle null first
												}else if (dom_buy_flag.equals("Y")) {
													dom_buy_flag = "D";
												} else if (dom_buy_flag.equals("K")) {
													dom_buy_flag = "I";
												} else if (dom_buy_flag.equals("T")) {
													dom_buy_flag = "T";
												}else if (dom_buy_flag.equals("N")) {
													dom_buy_flag = "N";
												}
												
												value = trd_abbr+"-"+ sn_no+"-"+cargo_seq_no+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-"+sn_rev_no;
										}
										else { // FOR SELECTING CARGO_SEQ_NO
										
											String cargo_seq = "";
											queryString4="SELECT A.CARGO_SEQ_NO, A.DOM_BUY_FLAG, B.TRD_CD  FROM FMS7_MAN_CONFIRM_CARGO_DTL A, FMS7_MAN_REQ_MST B WHERE A.MAN_CD = B.MAN_CD AND A.CARGO_REF_CD = ?";
											stmt4 = conn.prepareStatement(queryString4);
											stmt4.setString(1,cargo_ref_cd);
											rset4 = stmt4.executeQuery();
											while(rset4.next()) {
												cargo_seq=rset4.getString(1);
												dom_buy_flag=rset4.getString(2);
												trd_abbr = rset4.getString(3);
								
											}
											stmt4.close();
											rset4.close();
											
											 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
											 stmt4=conn.prepareStatement(queryString4);
											 stmt4.setString(1,trd_abbr);
											 rset4=stmt4.executeQuery();
											 while(rset4.next()) {
												 trd_abbr=rset4.getString(1);
											 }
												 trd_abbr=trd_abbr.trim();
												 value=trd_abbr;
												 if (mpe.counterparty_map.containsKey(value)) {
												        value =mpe.counterparty_map.get(value); 
												        trd_abbr = value; 
												    }
												 if(trd_abbr.contains("RIL")) {
													 trd_abbr="RIL";
												 }
											 
											 stmt4.close();
											 rset4.close();
										
											
											if (dom_buy_flag == null) {
												dom_buy_flag = "N";  // Handle null first
											}else if (dom_buy_flag.equals("Y")) {
												dom_buy_flag = "D";
											}else if (dom_buy_flag.equals("K")) {
												dom_buy_flag = "I";
											}else if (dom_buy_flag.equals("T")) {
												dom_buy_flag = "T";
											}else if (dom_buy_flag.equals("N")) {
												dom_buy_flag = "N";
											}
											
											value = trd_abbr +"-"+0+"-"+cargo_seq+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-0";
//											cell.setCellValue("'" + value + "'");
										}
										stmt3.close();
										rset3.close();
										
									}
									stmt1.close();
									rset1.close();
//								cell.setCellValue("'" + value + "'");
									str += value + ",";	
								}
							else if(i==10)//BU Unit
							{
									
//									queryString4="SELECT B.TRADER_ABBR,C.CARGO_SEL,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,PLANT_SEQ_NO,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL"
//											+ " FROM FMS7_TRADER_SUPPLIER_PLANT_MST A , FMS7_TRADER_MST B , FMS7_TRADER_CONT_MST C WHERE A.CUSTOMER_CD=B.TRADER_CD"
//											+ "  AND A.FGSA_NO=C.FGSA_NO AND A.FGSA_REV_NO=C.FGSA_REV_NO AND A.SN_NO=C.SN_NO AND A.CUSTOMER_CD=C.CUSTOMER_CD AND A.SN_REV_NO=C.SN_REV_NO AND "
//											+ " (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
//											+ "ORDER BY  A.CUSTOMER_CD,A.FLAG,A.SN_NO , A.SN_REV_NO DESC";
									queryString4="SELECT A.PLANT_SEQ_NO "
											+ "FROM FMS7_TRADER_SUPPLIER_PLANT_MST A ,FMS7_TRADER_MST B ,FMS7_TRADER_CONT_MST C "
											+ "WHERE C.CUSTOMER_CD =? AND C.FGSA_NO=? AND "
											+ "C.FGSA_REV_NO=?   AND C.SN_NO = ?  AND C.SN_REV_NO = ?"
											+ " AND  A.CUSTOMER_CD=B.TRADER_CD AND A.FGSA_NO=C.FGSA_NO AND A.FGSA_REV_NO=C.FGSA_REV_NO "
											+ "AND A.SN_NO=C.SN_NO AND A.CUSTOMER_CD=C.CUSTOMER_CD AND A.SN_REV_NO=C.SN_REV_NO " ;
									stmt4=conn.prepareStatement(queryString4);
									stmt4.setString(1,trd_cd);
									stmt4.setString(2,fgsa_no);
									stmt4.setString(3,fgsa_rev_no);
									stmt4.setString(4,sn_no);
									stmt4.setString(5,sn_rev_no);
									rset4=stmt4.executeQuery();
									if(rset4.next()) {
										int no=rset4.getInt(1);
										no=no+1;
										value=no+"";
										
									}
									else {
										value="1";
									}
									stmt4.close();
									rset4.close();
									str += value + ",";	
								
									
//									if(cargo_ref_cd.equals("22034"))
//									{
//										System.out.println(""+value);
//										System.out.println("====>"+trd_cd+":"+fgsa_no+":"+sn_no);
//									}
									
								}
								else if(i==12) {
									cont_map_id= rset.getString(1).split("-")[2];
									cargo_seq_no=rset.getString(1).split("-")[5];
									queryString1="SELECT CARGO_REF_CD FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
									stmt1=conn.prepareStatement(queryString1);
									stmt1.setString(1, cont_map_id);
									stmt1.setString(2, cargo_seq_no);
									rset1=stmt1.executeQuery();
									while(rset1.next()) {
										cargo_ref_cd=rset1.getString(1);
										
										queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
										stmt2=conn.prepareStatement(queryString2);
										stmt2.setString(1, cargo_ref_cd);
										rset2=stmt2.executeQuery();
										while(rset2.next()) {
											value=rset2.getString(1);
											if (value == null) {
											    value = "N";  // Handle null first
											}else if (value.equals("Y")) {
											    value = "D";
											} else if (value.equals("K")) {
											    value = "I";
											} else if (value.equals("T")) {
											    value = "T";
											}else if (value.equals("N")) {
												value = "N";
											}
										}
										stmt2.close();
										rset2.close();

									}
									stmt1.close();
									rset1.close();
									
//									cell.setCellValue("'" + value + "'");
									str += value + ",";	

								}
								else if(i==15) {
									if(value.equals("GHV")){
										value="GCV";
									}
									else if(value.equals("NHV")) {
										value="NCV";
									}
//									cell.setCellValue("'" + value + "'");
									str += value + ",";	
								}
								
								
								else if(i==19) {
									if(base.equals("GCV")) {
										baseVal=gcv;
										deviding_factor=new BigDecimal("1");
									}
									else {
										baseVal=ncv;
										deviding_factor=new BigDecimal("1.11");
									}
//									Double result1=Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
//									Double result1 = ((qty_mmbtu.multiply(multiplying_factor)).divide((baseVal.multiply(deviding_factor)))).setScale(2, RoundingMode.FLOOR).doubleValue();
									Double result1 = (qty_mmbtu.multiply(multiplying_factor).divide(baseVal.multiply(deviding_factor), 10, RoundingMode.HALF_UP)).setScale(2, RoundingMode.FLOOR).doubleValue();

									value = Double.toString(result1);
//									cell.setCellValue("'" + value + "'");
									str += value + ",";	
									
								}
								else {
//									cell.setCellValue("'" + value + "'");
									str += value + ",";	
								}
								
							}
							logger.insert_data(fname_csv, str, conn);
							count++;
							logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
							
				}
				
			}
				stmt.close();
				rset.close();

			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BUY_DAILY_SELLER_NOM>>,", conn);
		
						
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_BUY_DAILY_SELLER_NOM>>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
	}
	catch(Exception e){

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
}

public void FMS_TRADER_CONT_SPLIT_PLANT() throws SQLException, IOException {
	
	function_nm = "FMS_TRADER_CONT_SPLIT_PLANT()";
	String base="",customer_cd="",cargo_sel="",cargo_seq_no="",trd_cd="",sp_trd_abbr="",split_value_str="",ent_dt="",supp_split_value="",ent_by="",abbr="";
	BigDecimal plant_no = new BigDecimal("0");
	String cont_map_id="",cargo_ref_cd="",cont_type="",alloc_dt="",trd_abbr="",fgsa_no="",fgsa_rev_no="",sn_no="",sn_rev_no="",plant_seq_no="",bu_seq_no="",dom_buy_flag="",org_abbr="";
	BigDecimal split_plant_count=new BigDecimal("1");
	BigDecimal split_value = new BigDecimal("0"),split = new BigDecimal("0");
	int m = 0;
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_TRADER_CONT_SPLIT_PLANT>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT";
		
		workbook = new XSSFWorkbook();
		spreadsheet = workbook.createSheet("Sheet 1");
		
		nrow = 0;
		count = 0;
		String cd="";
//		Below block of code is for inserting columns
		row = spreadsheet.createRow(nrow++);

		for (int i = 0; i < columns.split(",").length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columns.split(",")[i]);
		}

		
//
//		queryString="SELECT C.CUSTOMER_CD , C.CARGO_SEL, A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,C.DOM_BUY_FLAG,A.CUSTOMER_CD,B.PLANT_SEQ_NO,"
//				+ "A.SPLIT_VALUE,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS') FROM FMS7_TRADER_OTHER_PLANT_MST A , "
//				+ "FMS7_TRADER_OTHER_PLANT_DTL B , FMS7_TRADER_CONT_MST C WHERE A.SN_NO=B.SN_NO AND A.SN_REV_NO=B.SN_REV_NO AND A.FGSA_NO=B.FGSA_NO AND "
//				+ "A.FGSA_REV_NO=B.FGSA_REV_NO AND A.SN_NO=C.SN_NO AND A.SN_REV_NO=C.SN_REV_NO AND A.FGSA_NO=C.FGSA_NO "
//				+ "ORDER BY  C.CUSTOMER_CD,C.DOM_BUY_FLAG,C.SN_NO , C.SN_REV_NO DESC";
		
		queryString="SELECT C.CUSTOMER_CD,C.CARGO_SEL,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,NULL,A.CUSTOMER_CD,B.PLANT_SEQ_NO,"//9=PLANT
				+ "A.SPLIT_VALUE,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS') "
				+ "FROM FMS7_TRADER_OTHER_PLANT_MST A ,FMS7_TRADER_OTHER_PLANT_DTL B , FMS7_TRADER_CONT_MST C "
				+ "WHERE  A.SN_NO=B.SN_NO AND A.SN_REV_NO=B.SN_REV_NO AND "
				+ "A.FGSA_NO=B.FGSA_NO AND A.FGSA_REV_NO=B.FGSA_REV_NO AND "
				+ "A.SN_NO=C.SN_NO AND A.SN_REV_NO=C.SN_REV_NO AND A.FGSA_NO=C.FGSA_NO AND A.FGSA_REV_NO=C.FGSA_REV_NO  AND"
				+ " (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
				+ "ORDER BY C.CUSTOMER_CD,C.DOM_BUY_FLAG,C.SN_NO,C.SN_REV_NO DESC ,B.PLANT_SEQ_NO DESC";
		
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		

		logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
		
		String seq = "";
		
		while(rset.next())
		{	
			
			
			
			for (int j= 0; j <seq.split("@").length; j++) {
				
				do {
				row = spreadsheet.createRow(nrow++);
				String flag="1";
				
				customer_cd=rset.getString(1);
				cargo_sel=rset.getString(2);
				fgsa_no=rset.getString(3);
				fgsa_rev_no=rset.getString(4);
				sn_no=rset.getString(5);
				sn_rev_no=rset.getString(6);
//				dom_buy_flag=rset.getString(7);
				trd_cd=rset.getString(8);
				split_value_str=rset.getString(10);
				ent_by=rset.getString(11);
				ent_dt=rset.getString(12);
				plant_seq_no=rset.getString(9);
//				base=rset.getString(16);
//				qty_mmbtu=rset.getDouble(19);
				if(cargo_sel!=null) {
					seq=cargo_sel;
				}
				
				if(split_value_str!=null) {
					split_value=new BigDecimal(split_value_str);
				}
				else {
					split_value=new BigDecimal("0");
					value=null;
				}
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					
					
					if(i==0) {
						queryString1="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, customer_cd);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							trd_abbr = rset1.getString(1);
							abbr=trd_abbr;
							
						}
						stmt1.close();
						rset1.close();
						trd_abbr=trd_abbr.trim();
						value=trd_abbr;
						org_abbr=trd_abbr;
						//plant_seq_mapping
					
						if (mpe.counterparty_map.containsKey(value)) {
						       value =mpe.counterparty_map.get(value); 
						       trd_abbr = value; 
						 }
						 if(trd_abbr.contains("RIL")) {
							 trd_abbr="RIL";
						 }
							int plant_seq=0;
							if(org_abbr.contains("CBM"))
							{
//								System.out.println("--->>"+plant_seq_no);
								 plant_seq=Integer.parseInt(plant_seq_no);
								 plant_seq=plant_seq+3;
								 plant_seq_no=plant_seq+"";
//								 System.out.println("->>"+plant_seq_no);
								
							} 
						if(mpe.trader_map.containsKey(trd_abbr+"-"+plant_seq_no)  && !org_abbr.contains("CBM"))
						{
							flag="1";
							if(( (trd_abbr+"-"+plant_seq_no).equals("RIL-2") || (trd_abbr+"-"+plant_seq_no).equals("BP-2") ) && !abbr.contains("CBM") )
							{
								flag="0";
								
							}
							plant_seq_no=mpe.trader_map.get(trd_abbr+"-"+plant_seq_no);
							plant_seq_no=plant_seq_no.split("-")[0];
						}
						
						
						 
						 queryString2= "SELECT CARGO_SEQ_NO,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
						 stmt2=conn.prepareStatement(queryString2);
						 stmt2.setString(1, seq.split("@")[j]);
						 rset2=stmt2.executeQuery();
						 while(rset2.next()) {
							 cargo_seq_no=rset2.getString(1);
							 dom_buy_flag=rset2.getString(2);
						 }
						 stmt2.close();
						 rset2.close();
						 
						 if (dom_buy_flag == null) {
							 dom_buy_flag = "";  // Handle null first
							}else if (dom_buy_flag.equals("null")) {
								dom_buy_flag = "N";
							}else if (dom_buy_flag.equals("Y")) {
								dom_buy_flag = "D";
							} else if (dom_buy_flag.equals("K")) {
								dom_buy_flag = "I";
							} else if (dom_buy_flag.equals("T")) {
								dom_buy_flag = "T";
							}else if (dom_buy_flag.equals("N")) {
								dom_buy_flag = "N";
							}
						 
						 value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no; 
							cell.setCellValue("'" + value + "'");
					}
					
					else if(i==7) {
						queryString1= "SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD = ?  ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1,trd_cd);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							sp_trd_abbr=rset1.getString(1);
						}
						stmt1.close();
						rset1.close();
						sp_trd_abbr=sp_trd_abbr.trim();
						value=sp_trd_abbr;
						if (mpe.counterparty_map.containsKey(value)) {
						       value =mpe.counterparty_map.get(value); 
						       sp_trd_abbr = value; 
						 }
						 if(sp_trd_abbr.contains("RIL")) {
							 sp_trd_abbr="RIL";
						 }
						 value=sp_trd_abbr;
							cell.setCellValue("'" + value + "'");
					}
					
					else if(i==8) //plant_seq_no
					{
						value=plant_seq_no;
						cell.setCellValue("'" + value + "'");
					}
					
					else if(i==9) {
						queryString1="SELECT COUNT(PLANT_SEQ_NO) FROM FMS7_TRADER_OTHER_PLANT_DTL WHERE "
								+ "SN_NO=? AND SN_REV_NO=? AND FGSA_NO=? AND FGSA_REV_NO=? AND CUSTOMER_CD=? AND ENT_DT=TO_DATE(?,'DD/MM/YYYY HH24:MI:SS')"
								+ " AND  EMP_CD=? ";
						
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1,sn_no);
						stmt1.setString(2,sn_rev_no);
						stmt1.setString(3,fgsa_no);
						stmt1.setString(4,fgsa_rev_no);
						stmt1.setString(5,trd_cd);
						stmt1.setString(6,ent_dt);
						stmt1.setString(7,ent_by);
						rset1 = stmt1.executeQuery();
						while(rset1.next()) {
							value = rset1.getString(1);
							plant_no=new BigDecimal(value);
						}
						stmt1.close();
						rset1.close();
						
						if(flag.equals("0"))
						{
							plant_no=new BigDecimal("0");
						}

						String temp_var="",temp_var2="";
						temp_var=plant_no+"";
						plant_no1=Integer.parseInt(temp_var);
						
						temp_var2=split_plant_count+"";
						split_plant_count1=Integer.parseInt(temp_var2);
					
						if(supp_split_value.equals(sn_no+"-"+sn_rev_no+"-"+trd_cd+"-"+ent_by) && split_plant_count1>plant_no1  &&  plant_no1>1) {
//						if(supp_split_value.equals(sn_no+"-"+sn_rev_no+"-"+trd_cd+"-"+ent_by) && split_plant_count.compareTo(plant_no) == 1 && plant_no.compareTo(new BigDecimal("1.0")) == 1) {
//							split=split_value-((Math.floor(split*100)/100)*(plant_no-1.0));
							split = split_value.subtract(split.multiply(new BigDecimal("100")).setScale(2, RoundingMode.FLOOR).divide(new BigDecimal("100")).multiply(plant_no.subtract(new BigDecimal("1"))));
//							split=split+Double.parseDouble(value);
							split_plant_count=new BigDecimal("1");
							split=split.setScale(2, RoundingMode.CEILING);
				
						}
						else {
							supp_split_value=sn_no+"-"+sn_rev_no+"-"+trd_cd+"-"+ent_by;
							split_plant_count = split_plant_count.add(new BigDecimal("1"));
							
							
//					
							if(plant_no1==1)
							{
								split_plant_count=new BigDecimal("1");
							}
							
//							if(plant_no.compareTo(new BigDecimal("1")) == 0) {
//								split_plant_count=new BigDecimal("1");
//						
//							}
							if(plant_no1>0) {
//							if(plant_no.compareTo(new BigDecimal("0")) == 1) {
//								System.out.println(plant_no);
//								System.out.println(""+split_value);
							split = split_value.divide(plant_no);
							}
							else {
								split = split_value;
							}
							
//							System.out.println(split+"before");
//							split=Math.floor(split*100)/100;
							split=split.setScale(2, RoundingMode.CEILING);
//							System.out.println(split+"after");
						}
						value =  split+"";
//						System.out.println(supp_split_value+"==="+split);
						cell.setCellValue("'" + value + "'");
					}
			
					else if(i==6) {
							
							queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1,seq.split("@")[j]);
							rset2=stmt2.executeQuery();
							while(rset2.next()) {
								value=rset2.getString(1);
								if (value == null) {
								    value = "";  // Handle null first
								}else if (value.equals("null")) {
								    value = "N";
								}else if (value.equals("Y")) {
								    value = "D";
								} else if (value.equals("K")) {
								    value = "I";
								} else if (value.equals("T")) {
								    value = "T";
								}else if (value.equals("N")) {
									value = "N";
								}
							}
							stmt2.close();
							rset2.close();
							cell.setCellValue("'" + value + "'");

					}
					else 
					{
						cell.setCellValue("'" + value + "'");
					}

				}
		
				count++;
				logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
			}
			
				while(split_plant_count1<plant_no1);
//				split_plant_count.compareTo(plant_no) == -1
			}
		}
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_TRADER_CONT_SPLIT_PLANT_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_CONT_SPLIT_PLANT>>,", conn);
						
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TRADER_CONT_SPLIT_PLANT>>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
	}
	catch(Exception e){

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
}


public void FMS_TRADER_CONT_PLANT_CHRG() throws SQLException, IOException {
	
	function_nm = "FMS_TRADER_CONT_PLANT_CHRG()";
	String base="",customer_cd="",cargo_sel="",cargo_seq_no="",trd_cd="",sp_trd_abbr="",chrg_cd="";
	String cont_map_id="",cargo_ref_cd="",cont_type="",alloc_dt="",trd_abbr="",fgsa_no="",fgsa_rev_no="",sn_no="",sn_rev_no="",plant_seq_no="",bu_seq_no="",dom_buy_flag="";
	int m=1;
	
	float baseVal=0f;
	float deviding_factor=1f;
	double multiplying_factor= 0.252*1000000;
	float gcv=9802.8f;
	float ncv=8831.35f;
	double qty_mmbtu=0;
	int supp_num=0;
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_TRADER_CONT_PLANT_CHRG>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,EFF_DT,CHARGE_ABBR,CHARGE_RATE,ENT_BY,ENT_DT,"
				+ "MODIFY_BY,MODIFY_DT";
		
		workbook = new XSSFWorkbook();
		spreadsheet = workbook.createSheet("Sheet 1");
		
		nrow = 0;
		count = 0;
		String cd="";
//		Below block of code is for inserting columns
		row = spreadsheet.createRow(nrow++);

		for (int i = 0; i < columns.split(",").length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columns.split(",")[i]);
		}

		

//		queryString="SELECT C.CUSTOMER_CD , C.CARGO_SEL, A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,C.DOM_BUY_FLAG,A.CUSTOMER_CD,B.PLANT_SEQ_NO,"
//				+ "A.SPLIT_VALUE,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS') FROM FMS7_TRADER_OTHER_PLANT_MST A , "
//				+ "FMS7_TRADER_OTHER_PLANT_DTL B , FMS7_TRADER_CONT_MST C WHERE A.SN_NO=B.SN_NO AND A.SN_REV_NO=B.SN_REV_NO AND A.FGSA_NO=B.FGSA_NO AND "
//				+ "A.FGSA_REV_NO=B.FGSA_REV_NO AND A.SN_NO=C.SN_NO AND ";
//		
		
		queryString="SELECT A.TRADER_CD,B.CARGO_SEL,A.AGR_NO,A.AGR_REV_NO,A.CONTRACT_NO,A.CONTRACT_REV_NO,NULL,A.PLANT_SEQ_NO,"
				+ "TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.LINE_ITEM_CD,A.TARIFF,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "NULL,NULL FROM FMS7_TRD_OTH_TARIFF_DTL A  , FMS7_TRADER_CONT_MST B , FMS7_OTHER_CHARGES_MST C "
				+ "WHERE A.CONTRACT_NO=B.SN_NO AND A.CONTRACT_REV_NO=B.SN_REV_NO AND A.AGR_NO=B.FGSA_NO AND A.AGR_REV_NO=B.FGSA_REV_NO AND A.TRADER_CD=B.CUSTOMER_CD"
				+ "   AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		

		logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
		
		String seq = "";
		
		while(rset.next())
		{	
			
			customer_cd=rset.getString(1);
			cargo_sel=rset.getString(2);
			fgsa_no=rset.getString(3);
			fgsa_rev_no=rset.getString(4);
			sn_no=rset.getString(5);
			sn_rev_no=rset.getString(6);
			chrg_cd=rset.getString(10);

			if(cargo_sel!=null) {
				seq=cargo_sel;
			}
			
			
			for (int j= 0; j <seq.split("@").length; j++) {
				
				row = spreadsheet.createRow(nrow++);
			
			
					
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if(i==0) {
						queryString1="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, customer_cd);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							trd_abbr = rset1.getString(1);
						}
						stmt1.close();
						rset1.close();
						trd_abbr=trd_abbr.trim();
						value=trd_abbr;
						if (mpe.counterparty_map.containsKey(value)) {
						       value =mpe.counterparty_map.get(value); 
						       trd_abbr = value; 
						 }
						 if(trd_abbr.contains("RIL")) {
							 trd_abbr="RIL";
						 }
						 
						 queryString2= "SELECT CARGO_SEQ_NO,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
						 stmt2=conn.prepareStatement(queryString2);
						 stmt2.setString(1, seq.split("@")[j]);
						 rset2=stmt2.executeQuery();
						 while(rset2.next()) {
							 cargo_seq_no=rset2.getString(1);
							 dom_buy_flag=rset2.getString(2);
						 }
						 stmt2.close();
						 rset2.close();
						 
						
							if (dom_buy_flag == null) {
							    dom_buy_flag = "";  // Handle null first
							}else if (dom_buy_flag.equals("null")) {
							    dom_buy_flag = "";
							}else if (dom_buy_flag.equals("Y")) {
							    dom_buy_flag = "D";
							} else if (dom_buy_flag.equals("K")) {
							    dom_buy_flag = "I";
							} else if (dom_buy_flag.equals("T")) {
							    dom_buy_flag = "T";
							}else if (dom_buy_flag.equals("N")) {
								dom_buy_flag = "";
							}
					
						 
						 value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+dom_buy_flag+"-"+seq.split("@")[j]+"-"+sn_rev_no; 
						 
						cell.setCellValue("'" + value + "'");
					}
					
	
					if(i==6) {
							queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1,seq.split("@")[j]);
							rset2=stmt2.executeQuery();
							while(rset2.next()) {
								value=rset2.getString(1);
								if (value == null) {
								    value = "";  // Handle null first
								}else if (value.equals("null")) {
								    value = "";
								}else if (value.equals("Y")) {
								    value = "D";
								} else if (value.equals("K")) {
								    value = "I";
								} else if (value.equals("T")) {
								    value = "T";
								}else if (value.equals("N")) {
									value = "";
								}
							}
							stmt2.close();
							rset2.close();
							
						cell.setCellValue("'" + value + "'");
					}
					
					if(i==9) {
						queryString2=" SELECT CHARGES_NM FROM FMS7_OTHER_CHARGES_MST WHERE CHARGES_CD= ? ";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1,chrg_cd);
						rset2=stmt2.executeQuery();
						if(rset2.next()) {
							value=rset2.getString(1);

						}
						stmt2.close();
						rset2.close();
						cell.setCellValue("'" + value + "'");

					}

					
				cell.setCellValue("'" + value + "'");
					
					
				}
		
				count++;
				logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
			}
		}
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_TRADER_CONT_PLANT_CHRG_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_CONT_PLANT_CHRG>>,", conn);
						
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TRADER_CONT_PLANT_CHRG>>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
	}
	catch(Exception e){

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);

	}
}

public void FMS_BUY_DAILY_ALLOCATION() throws SQLException, IOException {
	
	function_nm = "FMS_BUY_DAILY_ALLOCATION()";
	String base="",trd_cd="";
	String cont_map_id="",cargo_ref_cd="",cont_type="",alloc_dt="",trd_abbr="",fgsa_no="",fgsa_rev_no="",sn_no="",sn_rev_no="",plant_seq_no="",bu_seq_no="",cargo_seq_no="",dom_buy_flag="";
	int m=1;
	
	BigDecimal baseVal=new BigDecimal("0");
	BigDecimal deviding_factor=new BigDecimal("1");
	double cal= 0.252*1000000;
	BigDecimal multiplying_factor= BigDecimal.valueOf(cal);
	BigDecimal gcv= new BigDecimal("9802.8");
	BigDecimal ncv=new BigDecimal("8831.35");
	BigDecimal qty_mmbtu=new BigDecimal("0");
	int supp_num=0;
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_BUY_DAILY_ALLOCATION>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO";
		
		count = 0;
		String cd="";
		String ins_map_id="";
		String fname_csv = "", str = "", gas_dt="";
		plant_seq_no="";
		fname_csv = migration_setup_dir + "EXPORT/FMS_BUY_DAILY_ALLOCATION_" + start_end_dt + ".csv";
		
		FileWriter fw = new FileWriter(fname_csv, false); 
		fw.close();

		for (int i = 0; i < columns.split(",").length; i++) {
			str += columns.split(",")[i] + ",";
		}

		queryString="SELECT A.CONT_MAPPING_ID, A.PARTY_CD, 0, 0, NULL, 0, 0, A.PLANT_SEQ_NO, 0, 0, (A.SUPPLIER_SEQ+1), TO_CHAR(A.ALLOC_DT,'DD/MM/YYYY HH24:MI:SS'), A.CONT_MAPPING_ID, "
				+ "TO_CHAR(A.ALLOC_DT-1,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.ALLOC_DT,'HH24:MI'),NULL,NULL,NULL,A.ENTRY_TOT_ENE,NULL,A.ENT_BY, "
				+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),0 FROM FMS9_PO_ALLOC_DTL A "
				+ "WHERE (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))) AND (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')))";
		
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		
		
		logger.insert_data(fname_csv, str, conn);
		
		logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
		while(rset.next())
		{	
			cont_map_id=rset.getString(1);
			alloc_dt=rset.getString(12);
			qty_mmbtu= BigDecimal.valueOf(rset.getDouble(19));
			
			str = "";
			if (!ins_map_id.contains(rset.getString(1)+"-"+rset.getString(12)+";")) {
				ins_map_id += rset.getString(1)+"-"+rset.getString(12)+";";
			} 
			String qty_mmbtu1="";
			qty_mmbtu1=rset.getString(19);
			
			if(!qty_mmbtu1.equals("0"))
			{
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
//					cell = row.createCell(i);
					if(i==0) {
						cont_map_id = cont_map_id.split("-")[2];
						cargo_seq_no=rset.getString(1).split("-")[5];
						queryString1="SELECT CARGO_REF_CD,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, cont_map_id);
						stmt1.setString(2, cargo_seq_no);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							cargo_ref_cd=rset1.getString(1);
							dom_buy_flag=rset1.getString(2);

							queryString3="SELECT CUSTOMER_CD,FGSA_NO,FGSA_REV_NO ,SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG = ?  ORDER BY  CUSTOMER_CD,DOM_BUY_FLAG,SN_NO ,SN_REV_NO DESC ";
							stmt3=conn.prepareStatement(queryString3);
							stmt3.setString(1,"%"+cargo_ref_cd+"%");
							stmt3.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);
							rset3=stmt3.executeQuery();
							if(rset3.next()) {
								trd_cd=rset3.getString(1);

								
//								if(cargo_ref_cd.equals("23031")) {
//								System.out.println(cargo_ref_cd+"=="+trd_cd);
//								}
								fgsa_no=rset3.getString(2);
								fgsa_rev_no=rset3.getString(3);
								sn_no=rset3.getString(4);
								sn_rev_no=rset3.getString(5);
//								dom_buy_flag=rset3.getString(6);
								 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
								 stmt4=conn.prepareStatement(queryString4);
								 stmt4.setString(1,trd_cd);
								 rset4=stmt4.executeQuery();
								 while(rset4.next()) {
									 trd_abbr=rset4.getString(1);
								 }
								 
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 org_abbr=trd_abbr;
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									    }
									 if(trd_abbr.contains("RIL")) {
										 trd_abbr="RIL";
									 }
								 
								 stmt4.close();
								 rset4.close();
									
									if (dom_buy_flag == null) {
										dom_buy_flag = "N";  // Handle null first
									}else if (dom_buy_flag.equals("Y")) {
										dom_buy_flag = "D";
									} else if (dom_buy_flag.equals("K")) {
										dom_buy_flag = "I";
									} else if (dom_buy_flag.equals("T")) {
										dom_buy_flag = "T";
									}else if (dom_buy_flag.equals("N")) {
										dom_buy_flag = "N";
									}
									
									value = trd_abbr+"-"+ sn_no+"-"+cargo_seq_no+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-"+sn_rev_no;
							}
							else { // FOR SELECTING CARGO_SEQ_NO
							
								String cargo_seq = "";
								queryString4="SELECT A.CARGO_SEQ_NO, A.DOM_BUY_FLAG, B.TRD_CD  FROM FMS7_MAN_CONFIRM_CARGO_DTL A, FMS7_MAN_REQ_MST B WHERE A.MAN_CD = B.MAN_CD AND A.CARGO_REF_CD = ?";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1,cargo_ref_cd);
								rset4 = stmt4.executeQuery();
								while(rset4.next()) {
									cargo_seq=rset4.getString(1);
									dom_buy_flag=rset4.getString(2);
									trd_abbr = rset4.getString(3);
					
								}
								stmt4.close();
								rset4.close();
								
								 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
								 stmt4=conn.prepareStatement(queryString4);
								 stmt4.setString(1,trd_abbr);
								 rset4=stmt4.executeQuery();
								 while(rset4.next()) {
									 trd_abbr=rset4.getString(1);
								 }
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 org_abbr=trd_abbr;
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									    }
									 if(trd_abbr.contains("RIL")) {
										 trd_abbr="RIL";
									 }
								 
								 stmt4.close();
								 rset4.close();
							
								
								if (dom_buy_flag == null) {
									dom_buy_flag = "N";  // Handle null first
								}else if (dom_buy_flag.equals("Y")) {
									dom_buy_flag = "D";
								}else if (dom_buy_flag.equals("K")) {
									dom_buy_flag = "I";
								}else if (dom_buy_flag.equals("T")) {
									dom_buy_flag = "T";
								}else if (dom_buy_flag.equals("N")) {
									dom_buy_flag = "N";
								}
								
								value = trd_abbr +"-"+0+"-"+cargo_seq+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-0";
//								cell.setCellValue("'" + value + "'");
							}
							stmt3.close();
							rset3.close();
							
						}
						stmt1.close();
						rset1.close();
//					cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
					else if(i==7)//plant _seq_no mapping
					{
						plant_seq_no=rset.getString(8);
						
						int plant_seq=0;
						if(org_abbr.contains("CBM"))
						{
							 plant_seq=Integer.parseInt(plant_seq_no);
							 plant_seq=plant_seq+3;
							 plant_seq_no=plant_seq+"";
							
						} 
						//for plant :
						if(mpe.trader_map.containsKey(trd_abbr+"-"+plant_seq_no)   && !org_abbr.contains("CBM") )
						{
							plant_seq_no=mpe.trader_map.get(trd_abbr+"-"+plant_seq_no);
							plant_seq_no=plant_seq_no.split("-")[0];
							value=plant_seq_no;
						}
						
						else
						{
							value=plant_seq_no;
						}
						
						str += value + ",";	
						
						
					}
					else if(i==12) {
						cont_map_id= rset.getString(1).split("-")[2];
						cargo_seq_no=rset.getString(1).split("-")[5];
						queryString1="SELECT CARGO_REF_CD FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, cont_map_id);
						stmt1.setString(2, cargo_seq_no);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							cargo_ref_cd=rset1.getString(1);
							
							queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, cargo_ref_cd);
							rset2=stmt2.executeQuery();
							while(rset2.next()) {
								value=rset2.getString(1);
								if (value == null) {
								    value = "";  // Handle null first
								}else if (value.equals("null")) {
								    value = "N";
								}else if (value.equals("Y")) {
								    value = "D";
								} else if (value.equals("K")) {
								    value = "I";
								} else if (value.equals("T")) {
								    value = "T";
								}else if (value.equals("N")) {
									value = "N";
								}
							}
							stmt2.close();
							rset2.close();

						}
						stmt1.close();
						rset1.close();
						
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
					
					else if(i==15) {
						
						queryString1 = "SELECT ALLOC_BASE_FLAG, ENTRY_HV_GHV, ENTRY_HV_NHV FROM FMS9_PO_ALLOC_MST WHERE CONT_MAPPING_ID = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(1));
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							value = rset1.getString(1);
							base = value;
							
							if(value.equals("GHV")){
								value="GCV";
							}
							else if(value.equals("NHV")) {
								value="NCV";
							}
//							cell.setCellValue("'" + value + "'");
							str += value + ",";	
							
							i++;
//							cell = row.createCell(i);
//							cell.setCellValue("'" + rset1.getString(2) + "'");
							str += rset1.getString(2) + ",";	
							
							i++;
//							cell = row.createCell(i);
//							cell.setCellValue("'" + rset1.getString(3) + "'");
							str += rset1.getString(3) + ",";	
							
						}
						
						rset1.close();
						stmt1.close();
						

					}
					
					
					else if(i==19) {
						if(base.equals("GCV")) {
							baseVal=gcv;
							deviding_factor=new BigDecimal("1");
						}
						else {
							baseVal=ncv;
							deviding_factor=new BigDecimal("1.11");
						}

//						Double result1 = ((qty_mmbtu.multiply(multiplying_factor)).divide((baseVal.multiply(deviding_factor)))).setScale(2, RoundingMode.FLOOR).doubleValue();
						Double result1 = (qty_mmbtu.multiply(multiplying_factor).divide(baseVal.multiply(deviding_factor), 10, RoundingMode.HALF_UP)).setScale(2, RoundingMode.FLOOR).doubleValue();
						value = Double.toString(result1);
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
						
					}
					else {
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
					
					
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
			}// qty_mmbtu not equals("0")

			
		}
			stmt.close();
			rset.close();
			
			// For remaining data which is in FMS9_PO_ALLOC_MST but not in FMS9_PO_ALLOC_DTL
			queryString="SELECT A.CONT_MAPPING_ID,A.PARTY_CD,0,0,NULL,0,0,'1',0,0,'1',TO_CHAR(A.ALLOC_DT,'DD/MM/YYYY HH24:MI:SS'),A.CONT_MAPPING_ID, "
					+ "TO_CHAR(A.ALLOC_DT-1,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.ALLOC_DT,'HH24:MI'),A.ALLOC_BASE_FLAG,A.ENTRY_HV_GHV,A.ENTRY_HV_NHV,A.ENTRY_TOT_ENE,NULL,A.ENT_BY, "
					+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),0 FROM FMS9_PO_ALLOC_MST A "
					+ "WHERE (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))) AND (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))) ORDER BY ALLOC_ID";
			
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			

			logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
			
//						String seq="";
			
			while(rset.next())
			{	
				if (!ins_map_id.contains(rset.getString(1)+"-"+rset.getString(12)+";")) {
					cont_map_id=rset.getString(1);
					alloc_dt=rset.getString(12);
					base=rset.getString(16);
					
					qty_mmbtu= BigDecimal.valueOf(rset.getDouble(19));
					str = "";
						for (int i = 0; i < columns.split(",").length; i++) {
							
							value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
							if(i==0) {
								cont_map_id = cont_map_id.split("-")[2];
								cargo_seq_no=rset.getString(1).split("-")[5];
								queryString1="SELECT CARGO_REF_CD,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, cont_map_id);
								stmt1.setString(2, cargo_seq_no);
								rset1=stmt1.executeQuery();
								while(rset1.next()) {
									cargo_ref_cd=rset1.getString(1);
									dom_buy_flag=rset1.getString(2);

									
									queryString3="SELECT CUSTOMER_CD,FGSA_NO,FGSA_REV_NO ,SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG = ?  ORDER BY  CUSTOMER_CD,DOM_BUY_FLAG,SN_NO ,SN_REV_NO DESC ";
									stmt3=conn.prepareStatement(queryString3);
									stmt3.setString(1,"%"+cargo_ref_cd+"%");
									stmt3.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);
									rset3=stmt3.executeQuery();
									if(rset3.next()) {
										trd_cd=rset3.getString(1);
//										if(cargo_ref_cd.equals("23031")) {
//										System.out.println(cargo_ref_cd+"=="+trd_cd);
//										}
										fgsa_no=rset3.getString(2);
										fgsa_rev_no=rset3.getString(3);
										sn_no=rset3.getString(4);
										sn_rev_no=rset3.getString(5);
//										dom_buy_flag=rset3.getString(6);
										 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
										 stmt4=conn.prepareStatement(queryString4);
										 stmt4.setString(1,trd_cd);
										 rset4=stmt4.executeQuery();
										 while(rset4.next()) {
											 trd_abbr=rset4.getString(1);
										 }
											 trd_abbr=trd_abbr.trim();
											 value=trd_abbr;
											 if (mpe.counterparty_map.containsKey(value)) {
											        value =mpe.counterparty_map.get(value); 
											        trd_abbr = value; 
											    }
											 if(trd_abbr.contains("RIL")) {
												 trd_abbr="RIL";
											 }
										 
										 stmt4.close();
										 rset4.close();
											
											if (dom_buy_flag == null) {
												dom_buy_flag = "N";  // Handle null first
											}else if (dom_buy_flag.equals("Y")) {
												dom_buy_flag = "D";
											} else if (dom_buy_flag.equals("K")) {
												dom_buy_flag = "I";
											} else if (dom_buy_flag.equals("T")) {
												dom_buy_flag = "T";
											}else if (dom_buy_flag.equals("N")) {
												dom_buy_flag = "N";
											}
											
											value = trd_abbr+"-"+ sn_no+"-"+cargo_seq_no+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-"+sn_rev_no;
									}
									else { // FOR SELECTING CARGO_SEQ_NO
									
										String cargo_seq = "";
										queryString4="SELECT A.CARGO_SEQ_NO, A.DOM_BUY_FLAG, B.TRD_CD  FROM FMS7_MAN_CONFIRM_CARGO_DTL A, FMS7_MAN_REQ_MST B WHERE A.MAN_CD = B.MAN_CD AND A.CARGO_REF_CD = ?";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1,cargo_ref_cd);
										rset4 = stmt4.executeQuery();
										while(rset4.next()) {
											cargo_seq=rset4.getString(1);
											dom_buy_flag=rset4.getString(2);
											trd_abbr = rset4.getString(3);
							
										}
										stmt4.close();
										rset4.close();
										
										 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
										 stmt4=conn.prepareStatement(queryString4);
										 stmt4.setString(1,trd_abbr);
										 rset4=stmt4.executeQuery();
										 while(rset4.next()) {
											 trd_abbr=rset4.getString(1);
										 }
											 trd_abbr=trd_abbr.trim();
											 value=trd_abbr;
											 if (mpe.counterparty_map.containsKey(value)) {
											        value =mpe.counterparty_map.get(value); 
											        trd_abbr = value; 
											    }
											 if(trd_abbr.contains("RIL")) {
												 trd_abbr="RIL";
											 }
										 
										 stmt4.close();
										 rset4.close();
									
										
										if (dom_buy_flag == null) {
											dom_buy_flag = "N";  // Handle null first
										}else if (dom_buy_flag.equals("Y")) {
											dom_buy_flag = "D";
										}else if (dom_buy_flag.equals("K")) {
											dom_buy_flag = "I";
										}else if (dom_buy_flag.equals("T")) {
											dom_buy_flag = "T";
										}else if (dom_buy_flag.equals("N")) {
											dom_buy_flag = "N";
										}
										
										value = trd_abbr +"-"+0+"-"+cargo_seq+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-0";
//										cell.setCellValue("'" + value + "'");
									}
									stmt3.close();
									rset3.close();
									
								}
								stmt1.close();
								rset1.close();
//							cell.setCellValue("'" + value + "'");
								str += value + ",";	
							}

							else if(i==10)//BU Unit
							{
								
//								queryString4="SELECT B.TRADER_ABBR,C.CARGO_SEL,A.FGSA_NO,A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,PLANT_SEQ_NO,A.EMP_CD,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),NULL"
//										+ " FROM FMS7_TRADER_SUPPLIER_PLANT_MST A , FMS7_TRADER_MST B , FMS7_TRADER_CONT_MST C WHERE A.CUSTOMER_CD=B.TRADER_CD"
//										+ "  AND A.FGSA_NO=C.FGSA_NO AND A.FGSA_REV_NO=C.FGSA_REV_NO AND A.SN_NO=C.SN_NO AND A.CUSTOMER_CD=C.CUSTOMER_CD AND A.SN_REV_NO=C.SN_REV_NO AND "
//										+ " (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
//										+ "ORDER BY  A.CUSTOMER_CD,A.FLAG,A.SN_NO , A.SN_REV_NO DESC";
								queryString4="SELECT A.PLANT_SEQ_NO "
										+ "FROM FMS7_TRADER_SUPPLIER_PLANT_MST A ,FMS7_TRADER_MST B ,FMS7_TRADER_CONT_MST C "
										+ "WHERE C.CUSTOMER_CD =? AND C.FGSA_NO=? AND "
										+ "C.FGSA_REV_NO=?   AND C.SN_NO = ?  AND C.SN_REV_NO = ? AND  A.CUSTOMER_CD=B.TRADER_CD AND A.FGSA_NO=C.FGSA_NO AND A.FGSA_REV_NO=C.FGSA_REV_NO "
										+ "AND A.SN_NO=C.SN_NO AND A.CUSTOMER_CD=C.CUSTOMER_CD AND A.SN_REV_NO=C.SN_REV_NO " ;
								stmt4=conn.prepareStatement(queryString4);
								stmt4.setString(1,trd_cd);
								stmt4.setString(2,fgsa_no);
								stmt4.setString(3,fgsa_rev_no);
								stmt4.setString(4,sn_no);
								stmt4.setString(5,sn_rev_no);
								rset4=stmt4.executeQuery();
								if(rset4.next()) {
									int no=rset4.getInt(1);
									no=no+1;
									value=no+"";
									
								}
								else {
									value="1";
								}
								stmt4.close();
								rset4.close();
								str += value + ",";	
							
								
//								if(cargo_ref_cd.equals("22034"))
//								{
//									System.out.println(""+value);
//									System.out.println("====>"+trd_cd+":"+fgsa_no+":"+sn_no);
//								}
								
							}
							
							else if(i==12) {
								cont_map_id= rset.getString(1).split("-")[2];
								cargo_seq_no=rset.getString(1).split("-")[5];
								queryString1="SELECT CARGO_REF_CD FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, cont_map_id);
								stmt1.setString(2, cargo_seq_no);
								rset1=stmt1.executeQuery();
								while(rset1.next()) {
									cargo_ref_cd=rset1.getString(1);
									
									queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
									stmt2=conn.prepareStatement(queryString2);
									stmt2.setString(1, cargo_ref_cd);
									rset2=stmt2.executeQuery();
									while(rset2.next()) {
										value=rset2.getString(1);
										if (value == null) {
										    value = "N";  // Handle null first
										}else if (value.equals("Y")) {
										    value = "D";
										} else if (value.equals("K")) {
										    value = "I";
										} else if (value.equals("T")) {
										    value = "T";
										}else if (value.equals("N")) {
											value = "N";
										}
									}
									stmt2.close();
									rset2.close();

								}
								stmt1.close();
								rset1.close();
								
//								cell.setCellValue("'" + value + "'");
								str += value + ",";	
							}
							else if(i==15) {
								if(value.equals("GHV")){
									value="GCV";
								}
								else if(value.equals("NHV")) {
									value="NCV";
								}
//								cell.setCellValue("'" + value + "'");
								str += value + ",";	
							}
							
							
							else if(i==19) {
								if(base.equals("GCV")) {
									baseVal=gcv;
									deviding_factor=new BigDecimal("1");
								}
								else {
									baseVal=ncv;
									deviding_factor=new BigDecimal("1.11");
								}
//								Double result1=Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
//								Double result1 = ((qty_mmbtu.multiply(multiplying_factor)).divide((baseVal.multiply(deviding_factor)))).setScale(2, RoundingMode.FLOOR).doubleValue();
								Double result1 = (qty_mmbtu.multiply(multiplying_factor).divide(baseVal.multiply(deviding_factor), 10, RoundingMode.HALF_UP)).setScale(2, RoundingMode.FLOOR).doubleValue();

								value = Double.toString(result1);
//								cell.setCellValue("'" + value + "'");
								str += value + ",";	
								
							}
							else {
//								cell.setCellValue("'" + value + "'");
								str += value + ",";	
							}
							
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
						
//					}//if condition qty_mmbtu !0
						
				}
				
			}
				stmt.close();
				rset.close();

//			filename = migration_setup_dir + "EXPORT/FMS_BUY_DAILY_ALLOCATION_"+start_end_dt+".xlsx";

//			fileOut = new FileOutputStream(filename);

//			workbook.write(fileOut);
//			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BUY_DAILY_ALLOCATION>>,", conn);
		
						
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_BUY_DAILY_ALLOCATION>>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
	}
	catch(Exception e){

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);

	}
}


public void FMS_BUY_DAILY_ALLOCATION_MM() throws SQLException, IOException {
	
	function_nm = "FMS_BUY_DAILY_ALLOCATION_MM()";
	String base="",trd_cd="";
	String cont_map_id="",cargo_ref_cd="",alloc_dt="",trd_abbr="",fgsa_no="",fgsa_rev_no="",sn_no="",sn_rev_no="",plant_seq_no="",bu_seq_no="",cargo_seq_no="",dom_buy_flag="";
	int m=1;
	
	BigDecimal baseVal=new BigDecimal("0");
	BigDecimal deviding_factor=new BigDecimal("1");
	double cal= 0.252*1000000;
	BigDecimal multiplying_factor= BigDecimal.valueOf(cal);
	BigDecimal gcv= new BigDecimal("9802.8");
	BigDecimal ncv=new BigDecimal("8831.35");
	BigDecimal qty_mmbtu=new BigDecimal("0");
	int supp_num=0;
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_BUY_DAILY_ALLOCATION_MM>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,SEQ_NO,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,EMAIL_SENT,CARGO_NO,DTL_CATEGORY,MOLECULE_MAP";
		
		count = 0;
		String cd="";
		String fname_csv = "", str = "", gas_dt="";
		plant_seq_no="";
		fname_csv = migration_setup_dir + "EXPORT/FMS_BUY_DAILY_ALLOCATION_MM_" + start_end_dt + ".csv";
		
		FileWriter fw = new FileWriter(fname_csv, false); 
		fw.close();

		for (int i = 0; i < columns.split(",").length; i++) {
			str += columns.split(",")[i] + ",";
		}

		queryString="SELECT A.CONT_MAPPING_ID, A.PARTY_CD, 0, 0, NULL, 0, 0, A.PLANT_SEQ_NO, 0, 0, (A.SUPPLIER_SEQ+1), TO_CHAR(A.ALLOC_DT,'DD/MM/YYYY HH24:MI:SS'), A.CONT_MAPPING_ID, 0, "
				+ " TO_CHAR(A.ALLOC_DT-1,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.ALLOC_DT,'HH24:MI'), NULL, NULL, NULL, A.ENTRY_TOT_ENE, NULL, A.ENT_BY, "
				+ " TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'), NULL, 0, 'MOL', A.PROD_CD FROM FMS9_PO_ALLOC_DTL A "
				+ " WHERE (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))) AND (A.ENT_DT IS NULL OR (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))) ORDER BY A.CONT_MAPPING_ID, A.ALLOC_DT ";
		
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		
		logger.insert_data(fname_csv, str, conn);
		
		logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
		
		int seq = 0;
		String map_id = "";
		
		while(rset.next())
		{	
			cont_map_id=rset.getString(1);
			alloc_dt=rset.getString(12);
			qty_mmbtu= BigDecimal.valueOf(rset.getDouble(20));
			
			String qty_mmbtu1="";
			qty_mmbtu1=rset.getString(20);
			str = "";
					
			if(!qty_mmbtu1.equals("0"))
			{
				
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
//					cell = row.createCell(i);
					if(i==0) {
						cont_map_id = cont_map_id.split("-")[2];
						cargo_seq_no=rset.getString(1).split("-")[5];
						queryString1="SELECT CARGO_REF_CD,DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, cont_map_id);
						stmt1.setString(2, cargo_seq_no);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							cargo_ref_cd=rset1.getString(1);
							dom_buy_flag=rset1.getString(2);
							
							queryString3="SELECT CUSTOMER_CD,FGSA_NO,FGSA_REV_NO ,SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG = ?  ORDER BY  CUSTOMER_CD,DOM_BUY_FLAG,SN_NO ,SN_REV_NO DESC ";
							stmt3=conn.prepareStatement(queryString3);
							stmt3.setString(1,"%"+cargo_ref_cd+"%");
							stmt3.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);
							rset3=stmt3.executeQuery();
							if(rset3.next()) {
								trd_cd=rset3.getString(1);
//								if(cargo_ref_cd.equals("23031")) {
//								System.out.println(cargo_ref_cd+"=="+trd_cd);
//								}
								fgsa_no=rset3.getString(2);
								fgsa_rev_no=rset3.getString(3);
								sn_no=rset3.getString(4);
								sn_rev_no=rset3.getString(5);
//								dom_buy_flag=rset3.getString(6);
								 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
								 stmt4=conn.prepareStatement(queryString4);
								 stmt4.setString(1,trd_cd);
								 rset4=stmt4.executeQuery();
								 while(rset4.next()) {
									 trd_abbr=rset4.getString(1);
								 }
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 org_abbr=trd_abbr;
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									    }
									 if(trd_abbr.contains("RIL")) {
										 trd_abbr="RIL";
									 }
								 
								 stmt4.close();
								 rset4.close();
									
									if (dom_buy_flag == null) {
										dom_buy_flag = "N";  // Handle null first
									}else if (dom_buy_flag.equals("Y")) {
										dom_buy_flag = "D";
									} else if (dom_buy_flag.equals("K")) {
										dom_buy_flag = "I";
									} else if (dom_buy_flag.equals("T")) {
										dom_buy_flag = "T";
									}else if (dom_buy_flag.equals("N")) {
										dom_buy_flag = "N";
									}
									
									value = trd_abbr+"-"+ sn_no+"-"+cargo_seq_no+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-"+sn_rev_no;
							}
							else { // FOR SELECTING CARGO_SEQ_NO
							
								String cargo_seq = "";
								queryString4="SELECT A.CARGO_SEQ_NO, A.DOM_BUY_FLAG, B.TRD_CD  FROM FMS7_MAN_CONFIRM_CARGO_DTL A, FMS7_MAN_REQ_MST B WHERE A.MAN_CD = B.MAN_CD AND A.CARGO_REF_CD = ?";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1,cargo_ref_cd);
								rset4 = stmt4.executeQuery();
								while(rset4.next()) {
									cargo_seq=rset4.getString(1);
									dom_buy_flag=rset4.getString(2);
									trd_abbr = rset4.getString(3);
					
								}
								stmt4.close();
								rset4.close();
								
								 queryString4="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
								 stmt4=conn.prepareStatement(queryString4);
								 stmt4.setString(1,trd_abbr);
								 rset4=stmt4.executeQuery();
								 while(rset4.next()) {
									 trd_abbr=rset4.getString(1);
								 }
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 org_abbr=trd_abbr;
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									    }
									 if(trd_abbr.contains("RIL")) {
										 trd_abbr="RIL";
									 }
								 
								 stmt4.close();
								 rset4.close();
							
								
								if (dom_buy_flag == null) {
									dom_buy_flag = "N";  // Handle null first
								}else if (dom_buy_flag.equals("Y")) {
									dom_buy_flag = "D";
								}else if (dom_buy_flag.equals("K")) {
									dom_buy_flag = "I";
								}else if (dom_buy_flag.equals("T")) {
									dom_buy_flag = "T";
								}else if (dom_buy_flag.equals("N")) {
									dom_buy_flag = "N";
								}
								
								value = trd_abbr +"-"+0+"-"+cargo_seq+"-"+dom_buy_flag+"-"+cargo_ref_cd+"-0";
//								cell.setCellValue("'" + value + "'");
							}
							stmt3.close();
							rset3.close();
							
						}
						stmt1.close();
						rset1.close();
//					cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
	
						else if(i==7)//plant _seq_no mapping
						{
							plant_seq_no=rset.getString(8);
							
							int plant_seq=0;
							if(org_abbr.contains("CBM"))
							{
								 plant_seq=Integer.parseInt(plant_seq_no);
								 plant_seq=plant_seq+3;
								 plant_seq_no=plant_seq+"";
							} 
							//for plant :
							if(mpe.trader_map.containsKey(trd_abbr+"-"+plant_seq_no)   && !org_abbr.contains("CBM") )
							{
								plant_seq_no=mpe.trader_map.get(trd_abbr+"-"+plant_seq_no);
								plant_seq_no=plant_seq_no.split("-")[0];
								value=plant_seq_no;
							}
							
							else
							{
								value=plant_seq_no;
							}
							
							str += value + ",";	
							
							
						}
					else if(i==12) {
						cont_map_id= rset.getString(1).split("-")[2];
						cargo_seq_no=rset.getString(1).split("-")[5];
						queryString1="SELECT CARGO_REF_CD FROM FMS7_MAN_CONFIRM_CARGO_DTL  WHERE MAN_CONF_CD=? AND CARGO_SEQ_NO=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, cont_map_id);
						stmt1.setString(2, cargo_seq_no);
						rset1=stmt1.executeQuery();
						while(rset1.next()) {
							cargo_ref_cd=rset1.getString(1);
							
							queryString2="SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, cargo_ref_cd);
							rset2=stmt2.executeQuery();
							while(rset2.next()) {
								value=rset2.getString(1);
								if (value == null) {
								    value = "";  // Handle null first
								}else if (value.equals("null")) {
								    value = "N";
								}else if (value.equals("Y")) {
								    value = "D";
								} else if (value.equals("K")) {
								    value = "I";
								} else if (value.equals("T")) {
								    value = "T";
								}else if (value.equals("N")) {
									value = "N";
								}
							}
							stmt2.close();
							rset2.close();

						}
						stmt1.close();
						rset1.close();
						
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
					
					else if (i == 13) {
						if (map_id.equals(rset.getString(1)+rset.getString(12))) {
							seq++;
						}
						else {
							seq = 1;
							map_id = rset.getString(1)+rset.getString(12);
						}

//						cell.setCellValue("'" + seq + "'");
						str += value + ",";	
					}
					
					else if(i==16) {
						
						queryString1 = "SELECT ALLOC_BASE_FLAG, ENTRY_HV_GHV, ENTRY_HV_NHV FROM FMS9_PO_ALLOC_MST WHERE CONT_MAPPING_ID = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(1));
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							value = rset1.getString(1);
							base = value;
							
							if(value.equals("GHV")){
								value="GCV";
							}
							else if(value.equals("NHV")) {
								value="NCV";
							}
//							cell.setCellValue("'" + value + "'");
							str += value + ",";	
							
							i++;
//							cell = row.createCell(i);
//							cell.setCellValue("'" + rset1.getString(2) + "'");
							str += rset1.getString(2) + ",";	
							
							i++;
//							cell = row.createCell(i);
//							cell.setCellValue("'" + rset1.getString(3) + "'");
							str += rset1.getString(3) + ",";	
						}
						
						rset1.close();
						stmt1.close();
						
					}
					else if(i==20) {
						if(base.equals("GCV")) {
							baseVal=gcv;
							deviding_factor=new BigDecimal("1");
						}
						else {
							baseVal=ncv;
							deviding_factor=new BigDecimal("1.11");
						}

//						Double result1 = ((qty_mmbtu.multiply(multiplying_factor)).divide((baseVal.multiply(deviding_factor)))).setScale(2, RoundingMode.FLOOR).doubleValue();
						Double result1 = (qty_mmbtu.multiply(multiplying_factor).divide(baseVal.multiply(deviding_factor), 10, RoundingMode.HALF_UP)).setScale(2, RoundingMode.FLOOR).doubleValue();
						value = Double.toString(result1);
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	

						
					}
					else if (i == 26) {
						queryString1 = "SELECT PROD_ABRV FROM FMS7_PRODUCT_MST WHERE PROD_CD = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(27));
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							value = rset1.getString(1);
						}
						
						stmt1.close();
						rset1.close();

//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
					else {
//						cell.setCellValue("'" + value + "'");
						str += value + ",";	
					}
					
					
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
				
			}//if condition qty_mmbtu 0

			
		}
			stmt.close();
			rset.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BUY_DAILY_ALLOCATION_MM>>,", conn);
		
						
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_BUY_DAILY_ALLOCATION_MM>>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
	}
	catch(Exception e){

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);

	}
}

public void FMS_PUR_SG_INV_MST() throws SQLException, IOException {
	
	function_nm = "FMS_PUR_SG_INV_MST()";
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_PUR_SG_INV_MST>>,,,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		
		columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PERIOD_START_DT,PERIOD_END_DT,FINANCIAL_YEAR, "
				+ "		BU_UNIT,BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD, "
				+ "		INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,DUE_DT,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT, "
				+ "		EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT, "
				+ "		TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,CHECKED_FLAG,CHECKED_BY, "
				+ "		CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT, "
				+ "		TXN_CHARGE,TXN_AMOUNT,TAX_TXN_AMT,TAX_TXN_CD,TAX_TXN_EFF_DT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI, "
				+ "		TCS_CERT_FLAG,TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TCS_EFF_DT,SAP_EXCHNG_RATE,SAP_APPROVAL, "
				+ "		SAP_APPROVED_BY,SAP_APPROVED_DT,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,TCS_TDS,SYS_INV_NO, "
				+ "		PAY_INSERT_BY,PAY_INSERT_DT,PAY_RECV_AMT,PAY_RECV_DT,PAY_REMARK,PAY_UPDATE_BY,PAY_UPDATE_DT, "
				+ "		CARGO_NO,BOE_NO,INV_FLAG,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,SUG_QTY,SUG_PERCENT,QTY_UNIT, "
				+ "		TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,OTHER_CHARGES,MARKET_MARGIN_AMT,OTHER_CHARGES_AMT,FIN_SYS";

    	
		workbook = new XSSFWorkbook();
		spreadsheet = workbook.createSheet("Sheet 1");

		nrow = 0;
		ncell = 0;
		count = 0;
		String financial_year="",exchange_rate_cd="";
		row = spreadsheet.createRow(nrow++);
		
		// Inserting Column Names
		for (int i = 0; i < columns.split(",").length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columns.split(",")[i]);
		}
		
		// Inserting Rest of the data
		queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
		stmt = conn.prepareStatement(queryString);
		rset = stmt.executeQuery();
//		rset2.getString(1)+","+rset2.getString(13)+","+rset2.getString(14)+","+rset2.getString(19)+","+rset2.getString(83)+","
		logger.checkpoint(fname, "CARGO_REF_NO, INVOICE_NO, INVOICE_DT, REMARK,FINANCIAL_YEAR,CONTRACT_TYPE,TIMESTAMP", conn);
		
		String inv_flag="",cargo_ref="";
		String price1="",price2="",price3="",tax_cd="",tax_eff_dt="",inv_eff_dt="",sap_aprv="";
		double diff=0d;
		while (rset.next()) {
			diff=0.0;
			query_ind = 1;
			financial_year="";
			queryString1 = "SELECT  DISTINCT(A.MAN_CD),TO_CHAR(A.EXP_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.EXP_TO_DT, 'DD/MM/YYYY HH24:MI:SS'),"
					+ " EXTRACT(YEAR FROM ADD_MONTHS(A.EXP_FROM_DT, -3)) || '-' ||  EXTRACT(YEAR FROM ADD_MONTHS(A.EXP_TO_DT, 9)) "
					+ " FROM FMS7_CARGO_NOMINATION A, FMS7_TRADER_MST B "
					+ "WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? AND A.MAN_CD IN (";
					if (lng_cd.split(",").length > 0) {
						queryString1 += "?";
						for (int i = 1; i < lng_cd.split(",").length; i++) {
							queryString1 += ",?";
						}
					}
			queryString1 += ") AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "ORDER BY  EXTRACT(YEAR FROM ADD_MONTHS(A.EXP_FROM_DT, -3)) || '-' ||  EXTRACT(YEAR FROM ADD_MONTHS(A.EXP_TO_DT, 9))  ASC ";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(query_ind++, rset.getString(2));
			for (int i = 0; i < lng_cd.split(",").length; i++) {
				stmt1.setString(query_ind++, lng_cd.split(",")[i]);
			}
			stmt1.setString(query_ind++, delta_FromDt);
			stmt1.setString(query_ind++, delta_FromDt);
			stmt1.setString(query_ind++, delta_ToDt);
			stmt1.setString(query_ind++, delta_ToDt);
			rset1 = stmt1.executeQuery();
		
			while(rset1.next()) {
				financial_year="";
				// PF, P
					queryString2="SELECT B.CARGO_REF_CD ,NULL,NULL ,NULL,NULL,NULL,NULL,NULL ,NULL,NULL ,"//10
							+ "A.SPLIT_SEQ, NULL,NULL,"		// split_seq = boe_no
							+ "'0',NULL,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'),'10',TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EXP_DELV_QTY,A.CONFIRM_PRICE,"
							+ "B.PRICE_UNIT,NULL,"//23
							+ "NULL,NULL,NULL,'1',NULL,NULL ,NULL,NULL,A.INVOICE_AMT,NULL,NULL,"//34		
							+ "NULL,NULL,A.CHECKED_FLAG,A.CHECKED_BY,TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS')"//39
							+ ",A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'),"//42
							+ "A.APPROVED_FLAG,A.APPROVED_BY,TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,"	//52
							+ "'O',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),'N',NULL,NULL,NULL,NULL,NULL,A.SUN_APPROVAL,"
							+ "A.SUN_APPROVAL_BY,TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"	
							+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,A.REMARK,NULL,"
							+ "NULL,NULL,NULL,'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL "
							+ "FROM FMS7_PROV_SELLER_PAY A,FMS7_MAN_CONFIRM_CARGO_DTL B "
							+ "WHERE B.MAN_CD=? AND A.CARGO_REF_NO=B.CARGO_REF_CD AND  (B.DOM_BUY_FLAG='N' OR B.DOM_BUY_FLAG IS NULL) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset1.getString(1));
					
					rset2 = stmt2.executeQuery();
					while(rset2.next())
					{
						queryString3 = "SELECT COUNT(CARGO_REF_NO) FROM FMS7_PROV_SELLER_PAY WHERE CARGO_REF_NO = ? ";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, rset2.getString(1));
						rset3 = stmt3.executeQuery();
						
						if (rset3.next() && ((rset3.getInt(1) > 1 && !rset2.getString(11).equals("0") && !rset2.getString(11).equals("1")) || (rset3.getInt(1) == 1 && rset2.getString(11).equals("0") ))) {
							
							for (int k = 0; k < 2; k++) {
								row = spreadsheet.createRow(nrow++);
								ncell = 0;
								
								for (int i = 1; i <= columns.split(",").length; i++) {
									cell = row.createCell(ncell++);
									value = rset2.getString(i) == null ? "null" : rset2.getString(i);
									if(i==1)
									{
										value = value +  (k == 0 ? "-P" : "-PF");
										inv_flag="-"+value.split("-")[1];
										inv_flag=inv_flag.substring(1,inv_flag.length());
										cell.setCellValue("'"+value+"'");
									}
									else if(i==8)
									{
										cell.setCellValue("'"+rset1.getString(2)+"'");
									}
									else if(i==9)
									{
										cell.setCellValue("'"+rset1.getString(3)+"'");
									}
									else if(i==10)
									{
										
										queryString4 = "SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
												+ " || '-' || "
												+ "EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), 9)) "
												+ "FROM DUAL";
							    		stmt4=conn.prepareStatement(queryString4);
							    		stmt4.setString(1, rset1.getString(2));
							    		stmt4.setString(2, rset1.getString(3));
							    		rset4=stmt4.executeQuery();
										if(rset4.next())
										{
											financial_year=rset4.getString(1)==null?"":rset4.getString(1);
										}
										rset4.close();
										stmt4.close();
										
										cell.setCellValue("'"+financial_year+"'");
									
										
									}
									else if ((i == 11 && rset2.getString(11).equals("0")) || (i == 11 && rset2.getString(11).equals("2")) ) {	// boe no
										cell.setCellValue("'1'");
									}
									else if(i==17)//inv_dt
									{
										cell.setCellValue("'"+rset2.getString(17)+"'"); //20250513
									}
									else if(i==40 )
									{
										if(inv_flag.equals("PF"))
										{
											
											cell.setCellValue("'"+null+"'");
										}
										else {
											cell.setCellValue("'"+rset2.getString(40)+"'");
										}
									}
									else if(i==41 )
									{
										if(inv_flag.equals("PF"))
										{
											
											cell.setCellValue("'"+null+"'");
										}
										else {
											cell.setCellValue("'"+rset2.getString(41)+"'");
										}
									}else if( i==42)
									{
										if(inv_flag.equals("PF"))
										{
											
											cell.setCellValue("'"+null+"'");
										}
										else {
											cell.setCellValue("'"+rset2.getString(42)+"'");
										}
									}
									else if (i == 43) {
										
										if(inv_flag.equals("PF"))
										{
											cell.setCellValue("'"+null+"'");
										}
										else {
										value = rset2.getString(43) == null ? "null" : rset2.getString(43);
										value = value.equals("Y") ? "A" : "R";
										cell.setCellValue("'"+value+"'");
										}
									}else if( i==44)
									{
										if(inv_flag.equals("PF"))
										{
											
											cell.setCellValue("'"+null+"'");
										}
										else {
											cell.setCellValue("'"+rset2.getString(44)+"'");
										}
									}
									else if( i==45)
									{
										if(inv_flag.equals("PF"))
										{
											
											cell.setCellValue("'"+null+"'");
										}
										else {
											cell.setCellValue("'"+rset2.getString(45)+"'");
										}
									}
									else if( i==53)
									{
										if(inv_flag.equals("PF"))
										{
											
											cell.setCellValue("'"+null+"'");
										}
										else {
											cell.setCellValue("'"+rset2.getString(53)+"'");
										}
									}
									else if( i==54)
									{
										if(inv_flag.equals("PF"))
										{
											
											cell.setCellValue("'"+null+"'");
										}
										else {
											cell.setCellValue("'"+rset2.getString(54)+"'");
										}
									}
									else if( i==55)
									{
										if(inv_flag.equals("PF"))
										{
											
											cell.setCellValue("'"+null+"'");
										}
										else {
											cell.setCellValue("'"+rset2.getString(55)+"'");
										}
									}	
									else if(i==62)// SAP_APPROVAL 
									{
										sap_aprv=value;
										cell.setCellValue("'"+sap_aprv+"'");
									}
									else if(i==95) // FIN_SYS
									{
										if(sap_aprv.equals("Y"))
										{
											cell.setCellValue("'S'");
										}
										else
										{
											cell.setCellValue("'"+value+"'");
										}
									}
									
									else {
										cell.setCellValue("'"+value+"'");
									}
								}
								count++;
								logger.data(fname, (rset2.getString(1)+","+rset2.getString(16)+","+rset2.getString(17)+","+rset2.getString(83)+","+financial_year+","+"N"+","), conn, "");
							}
						}

						rset3.close();
						stmt3.close();
					}
					rset2.close();
					stmt2.close();
				
			
				//F: 
				financial_year="";
				queryString2="SELECT A.CARGO_REF_NO ,NULL,NULL ,NULL,NULL,NULL,NULL,NULL ,NULL,NULL ,A.SPLIT_SEQ, NULL,NULL,"
						+ "'0',NULL,A.INVOICE_NO,TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'),'10',TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.ACTUAL_UNLOADED_QTY,A.CONFIRM_PRICE,B.PRICE_UNIT,NULL,"
						+ "NULL,NULL,NULL,'1',NULL,NULL ,NULL,NULL,A.INVOICE_AMT,NULL,NULL,"	
						+ "NULL,NULL,A.CHECKED_FLAG,A.CHECKED_BY,TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "A.APPROVED_FLAG,A.APPROVED_BY,TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,"	
						+ "'O',NULL,NULL,'N',NULL,NULL,NULL,NULL,NULL,"
						+ "A.SUN_APPROVAL,A.SUN_APPROVAL_BY,TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,A.PAY_INSERT_BY,TO_CHAR(A.PAYMENT_DT, 'DD/MM/YYYY HH24:MI:SS'),"/////PAYEMT_DT & PAYINSER DT ALSO WIHCIH?
						+ "A.PAY_RECV_AMT,NULL,A.PAY_REMARK,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "NULL,NULL,NULL,'1' ,NULL,NULL,NULL,NULL,NULL,NULL,NULL  "
						+ "FROM FMS7_FINAL_SELLER_PAY A,FMS7_MAN_CONFIRM_CARGO_DTL B "
						+ "WHERE B.MAN_CD=? AND A.CARGO_REF_NO=B.CARGO_REF_CD  AND (B.DOM_BUY_FLAG='N' OR B.DOM_BUY_FLAG IS NULL) ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset1.getString(1));
				
				rset2 = stmt2.executeQuery();
				while(rset2.next())
				{
					queryString3 = "SELECT COUNT(CARGO_REF_NO) FROM FMS7_FINAL_SELLER_PAY WHERE CARGO_REF_NO = ? ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, rset2.getString(1));
					rset3 = stmt3.executeQuery();
					
					if (rset3.next() && ((rset3.getInt(1) > 1 && !rset2.getString(11).equals("0") && !rset2.getString(11).equals("1") ) || (rset3.getInt(1) == 1 && rset2.getString(11).equals("0")))) {
						
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						for (int i = 1; i <= columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							if(i==1)
							{
								cargo_ref=value;
								cell.setCellValue("'"+value+"-F'");
								inv_flag="-F";
							}else if(i==8)
							{
								cell.setCellValue("'"+rset1.getString(2)+"'");
							}
							else if(i==9)
							{
								cell.setCellValue("'"+rset1.getString(3)+"'");
							}
							else if(i==10)
							{
								
								queryString4 = "SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
										+ " || '-' || "
										+ "EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), 9)) "
										+ "FROM DUAL";
					    		stmt4=conn.prepareStatement(queryString4);
					    		stmt4.setString(1, rset1.getString(2));
					    		stmt4.setString(2, rset1.getString(3));
					    		rset4=stmt4.executeQuery();
								if(rset4.next())
								{
									financial_year=rset4.getString(1)==null?"":rset4.getString(1);
								}
								rset4.close();
								stmt4.close();
								
								cell.setCellValue("'"+financial_year+"'");
								
							}
							else if ((i == 11 && rset2.getString(11).equals("0")) || (i == 11 && rset2.getString(11).equals("2")) ) {	// boe no
								cell.setCellValue("'1'");
							}
							else if(i==17)
							{
//								cell.setCellValue("'"+rset1.getString(2)+"'");
								cell.setCellValue("'"+rset2.getString(17)+"'"); //20250513 
								
							}
							else if(i==21)// sale price
							{
								cell.setCellValue("'"+rset2.getString(21)+"'");
								
							}
							else if (i == 43) {
								value = value.equals("Y") ? "A" : "R";
								cell.setCellValue("'"+value+"'");
							}
							else if(i==62)// SAP_APPROVAL 
							{
								sap_aprv=value;
								cell.setCellValue("'"+sap_aprv+"'");
							}
							
							else if(i==84)// diff in final price
							{
								cell.setCellValue("'"+rset2.getString(21)+"'");
							}
							else if(i==95) // FIN_SYS
							{
								if(sap_aprv.equals("Y"))
								{
									cell.setCellValue("'S'");
								}
								else
								{
									cell.setCellValue("'"+value+"'");
								}
							}
							else {
								cell.setCellValue("'"+value+"'");}
							}
						count++;
						logger.data(fname, (rset2.getString(1)+","+rset2.getString(16)+","+rset2.getString(17)+","+rset2.getString(83)+","+financial_year+","+"N"+","), conn, "");
						
					}

					rset3.close();
					stmt3.close();
					

				}
				rset2.close();
				stmt2.close();

				//CP:
				financial_year="";
				queryString2="SELECT   A.CARGO_REF_NO,NULL,NULL ,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "'1',NULL,NULL,'0',"//SELLER_DUE_DT
						+ "NULL,A.CHALLAN_NO,TO_CHAR(A.CUSTOM_DUTY_DT, 'DD/MM/YYYY HH24:MI:SS'),'10',TO_CHAR(A.CUSTOM_DUTY_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "A.EXP_DELV_QTY,A.CONFIRM_PRICE,B.PRICE_UNIT,A.SELLER_INV_AMT,"
						+ "TO_CHAR(A.CUSTOM_DUTY_DT, 'DD/MM/YYYY'),TO_CHAR(A.CUSTOM_DUTY_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EXCHG_RATE,'1',A.INVOICE_AMT,A.TOTAL_CD_AMT ,"//by use of custom_duty_dt  can get :EXCHG_RATE_CD FROM FMS7_EXCHG_RATE_ENTRY
						+ "A.TAX_STR_CD,NULL,A.INVOICE_AMT,NULL,NULL,A.CUSTOM_DUTY_PAY,NULL,A.CHECKED_FLAG,A.CHECKED_BY,"
						+ "TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,"
						+ "TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.APPROVED_FLAG,A.APPROVED_BY,"
						+ "TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "NULL,NULL,NULL,NULL,NULL,'O',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "'N',NULL,NULL,NULL,NULL,NULL,"
						+ "A.SUN_APPROVAL,A.SUN_APPROVAL_BY,TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "NULL,NULL,'CP',A.CIF_VALUE,A.ASSESSABLE_VALUE,A.REMARK_DIFF_CD,NULL,NULL,NULL,NULL,'1', "
						+ "NULL,NULL,NULL,NULL ,NULL,NULL,NULL  "
						+ "FROM FMS7_CUSTOM_DUTY A,FMS7_MAN_CONFIRM_CARGO_DTL B "
						+ "WHERE B.MAN_CD=? AND A.CARGO_REF_NO=B.CARGO_REF_CD AND (B.DOM_BUY_FLAG IS NULL OR B.DOM_BUY_FLAG='N')";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset1.getString(1));
				
				rset2 = stmt2.executeQuery();
				while(rset2.next())
				{
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					String surv_chrg="";inv_eff_dt="";
					String inv_amt_cp="";
					BigDecimal cif_value = null;
					BigDecimal ass_val;
					BigDecimal lad_chrg;
					
					for (int i = 1; i <= columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset2.getString(i) == null ? "null" : rset2.getString(i);
						if(i==1)
						{
							cargo_ref=rset1.getString(1);
//							if(cargo_ref.equals("24010"))
//							{
//								System.out.println("->"+cargo_ref);
//							}
							cell.setCellValue("'"+value+"-CP'");
							inv_flag="-CP";
						}else if(i==8)
						{
							cell.setCellValue("'"+rset1.getString(2)+"'");
						}
						else if(i==9)
						{
							cell.setCellValue("'"+rset1.getString(3)+"'");
						}
						else if(i==10)
						{
							
							queryString = "SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
									+ " || '-' || "
									+ "EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), 9)) "
									+ "FROM DUAL";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1, rset1.getString(2));
				    		stmt4.setString(2, rset1.getString(3));
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								financial_year=rset4.getString(1)==null?"":rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							
							cell.setCellValue("'"+financial_year+"'");
		
						}
						else if(i==16)// INV_NO
						{
							if(value.equals("null"))
							{
								value="NA";
								cell.setCellValue("'"+value+"'");
							}
//							else {
								cell.setCellValue("'"+value+"'");
//							}
							
						}
						else if(i==24)
						{
							
							queryString = "SELECT  EXCHG_RATE_CD FROM FMS7_EXCHG_RATE_ENTRY WHERE EFF_DT=TO_DATE(?, 'DD/MM/YYYY')";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1, rset2.getString(24));
				    		
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								exchange_rate_cd=rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							
							
							cell.setCellValue("'"+exchange_rate_cd+"'");
						}
						else if(i==29)// for tax_acd_amt=tax_amt in ems
						{
//							if(cargo_ref.equals("24010"))
//							{
//								System.out.println("--->"+value);
//							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i==30)
						{
							tax_cd=rset2.getString(30);
							
							queryString = "SELECT TO_CHAR(APP_DATE,'DD/MM/YYYY') FROM FMS7_CARGO_TAX_MST "
									+ " WHERE TAX_STR_CD = ?";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1,tax_cd);
				    		
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								tax_eff_dt=rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							cell.setCellValue("'"+tax_eff_dt+"'");
						}
						else if(i==31)
						{
							cell.setCellValue("'"+tax_eff_dt+"'");
						}
						else if(i==32)//inv_amt
						{
							cell.setCellValue("'"+value+"'");
							inv_amt_cp=value;
						}
						else if (i == 43) {
							value = value.equals("Y") ? "A" : "R";
							cell.setCellValue("'"+value+"'");
						}
						else if(i==62)// SAP_APPROVAL 
						{
							sap_aprv=value;
							cell.setCellValue("'"+sap_aprv+"'");
						}
						
						else if(i==81)//CIFVALUE
						{
							if(value.equals("0")){
							queryString = "SELECT NVL(SURV_ACTUAL_SERVICE_CHARGE,'0') FROM FMS7_CARGO_NOMINATION "
									+ " WHERE  CARGO_REF_CD= ?";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1,cargo_ref);
				    		
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								surv_chrg=rset4.getString(1);
								BigDecimal surv_chrg1 = new BigDecimal(surv_chrg);
								BigDecimal inv_amt_cp1 = new BigDecimal(inv_amt_cp);
							
							    cif_value = inv_amt_cp1.add(surv_chrg1).setScale(2, RoundingMode.CEILING);
						
								cell.setCellValue("'"+cif_value+"'");
//								System.out.println("---"+cif_value);
							}
							rset4.close();
							stmt4.close();
						}
							else
							{
								cif_value = rset2.getBigDecimal(81);
								cell.setCellValue("'"+value+"'");
							}
							
						}
						else if(i==82)
						{
							//if 01-04-04 -- 26-09-17
							// if lading charge then : add to cif
							//ass= cif+ (cif*1)
							//else
							// ass=cif 
							//for inv_eff_dt:
							
							queryString = "SELECT TO_CHAR(SELLER_INV_DT,'DD/MM/YYYY')  FROM FMS7_CUSTOM_DUTY "
									+ " WHERE CARGO_REF_NO = ? ";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1,cargo_ref);
				    		
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								inv_eff_dt=rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							  
							if (inv_eff_dt != null) {
								
							    queryString = "SELECT CIF_PERCENT FROM FMS7_LADING_CHARGE_MST WHERE EFF_DT <= TO_DATE(?, 'DD/MM/YYYY') "
							                + "ORDER BY EFF_DT DESC";
							    stmt4 = conn.prepareStatement(queryString);
							    stmt4.setString(1, inv_eff_dt); 

							    rset4 = stmt4.executeQuery();
							    if (rset4.next()) {
							        String cif_percent = rset4.getString(1);
							        if(!cif_percent.equals("0"))
							        {
							        	 BigDecimal cif_per = new BigDecimal(cif_percent);
									        
									        lad_chrg= (cif_per.multiply(cif_value)).divide(new BigDecimal("100"));
									        lad_chrg  = lad_chrg.setScale(2, RoundingMode.CEILING);
									        
									        ass_val=lad_chrg.add(cif_value);
									        ass_val  = ass_val.setScale(2, RoundingMode.CEILING);
									        cell.setCellValue("'"+ass_val+"'");
							        }
							        else
							        {
							        	cell.setCellValue("'"+value+"'");
							        }
							        
							       
							    }
							    else
						        {
						        	cell.setCellValue("'"+value+"'");
						        }
							    rset4.close();
							    stmt4.close();
							}
						
						}
						else if(i==95) // FIN_SYS
						{
							if(sap_aprv.equals("Y"))
							{
								cell.setCellValue("'S'");
							}
							else
							{
								cell.setCellValue("'"+value+"'");
							}
						}
						else {
							cell.setCellValue("'"+value+"'");
							}
						}
					count++;
					logger.data(fname, (rset2.getString(1)+","+rset2.getString(16)+","+rset2.getString(17)+","+rset2.getString(83)+","+financial_year+","+"N"+","), conn, "");
				}//END OF custom_duty_prov:
				rset2.close();
				stmt2.close();
				
				//CF:
				financial_year="";
				queryString2="SELECT   A.CARGO_REF_NO,NULL,NULL ,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "'1',NULL,NULL,'0',"//TO_CHAR(A.SELLER_DUE_DT_FINAL
						+ "NULL,A.CHALLAN_NO,TO_CHAR(A.CUSTOM_DUTY_DT, 'DD/MM/YYYY HH24:MI:SS'),'10',TO_CHAR(A.CUSTOM_DUTY_DT, 'DD/MM/YYYY HH24:MI:SS'),A.ACTUAL_UNLOADED_QTY,"
						+ "A.CONFIRM_PRICE,B.PRICE_UNIT,A.INVOICE_AMT,"
						+ "TO_CHAR(A.CUSTOM_DUTY_DT, 'DD/MM/YYYY'),TO_CHAR(A.CUSTOM_DUTY_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EXCHG_RATE,'1',A.CIF_VALUE,A.TOTAL_CD_AMT,"//by use of custom_duty_dt  can get :EXCHG_RATE_CD FROM FMS7_EXCHG_RATE_ENTRY
						+ "A.TAX_STR_CD,NULL,A.TOTAL_CD_AMT,NULL,NULL,A.CUSTOM_DUTY_PAY_REFUND,NULL,A.CHECKED_FLAG,A.CHECKED_BY,"
						+ "TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.AUTHORIZED_FLAG,A.AUTHORIZED_BY,TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.APPROVED_FLAG,A.APPROVED_BY,TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "NULL,NULL,NULL,NULL,NULL,'O',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "'N',NULL,NULL,NULL,NULL,NULL,"
						+ "A.SUN_APPROVAL,A.SUN_APPROVAL_BY,TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "NULL,NULL,'CF',A.CIF_VALUE,A.ASSESSABLE_VALUE,A.REMARK_DIFF_CD,NULL,NULL,NULL,NULL,'1',"
						+ "NULL,NULL,NULL,NULL ,NULL,NULL,NULL  "
						+ "FROM FMS7_FINAL_CUSTOM_DUTY A,FMS7_MAN_CONFIRM_CARGO_DTL B "
						+ "WHERE B.MAN_CD=? AND A.CARGO_REF_NO=B.CARGO_REF_CD AND (B.DOM_BUY_FLAG='N' OR B.DOM_BUY_FLAG IS NULL)";
				
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset1.getString(1));
				
				rset2 = stmt2.executeQuery();
				while(rset2.next())
				{
					String ex_rate_val="";
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					for (int i = 1; i <= columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset2.getString(i) == null ? "null" : rset2.getString(i);
						if(i==1)
						{	cargo_ref=value;
							cell.setCellValue("'"+value+"-CF'");
//							if(cargo_ref.equals("24010"))
//							{
//								System.out.println("->"+cargo_ref);
//							}
							inv_flag="-CF";
						}else if(i==8)
						{
							cell.setCellValue("'"+rset1.getString(2)+"'");
						}
						else if(i==9)
						{
							cell.setCellValue("'"+rset1.getString(3)+"'");
						}
						else if(i==10)
						{
							
							queryString = "SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
									+ " || '-' || "
									+ "EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), 9)) "
									+ "FROM DUAL";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1, rset1.getString(2));
				    		stmt4.setString(2, rset1.getString(3));
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								financial_year=rset4.getString(1)==null?"":rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							
							cell.setCellValue("'"+financial_year+"'");
							

						}
						else if(i==16)// INV_NO
						{
							if(value.equals("null"))
							{
								value="NA";
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							
						}
						else if(i==24)
						{
							
							queryString = "SELECT  EXCHG_RATE_CD FROM FMS7_EXCHG_RATE_ENTRY WHERE EFF_DT=TO_DATE(?, 'DD/MM/YYYY')";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1, rset2.getString(24));
				    		
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								exchange_rate_cd=rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							
							
							cell.setCellValue("'"+exchange_rate_cd+"'");
							
						}
						else if(i==29)// for tax_acd_amt=tax_amt in ems
						{
//							if(cargo_ref.equals("24010"))
//							{
//								System.out.println("--->"+value);
//							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i==30)
						{
							tax_cd=rset2.getString(30);
							
							queryString = "SELECT TO_CHAR(APP_DATE,'DD/MM/YYYY') FROM FMS7_CARGO_TAX_MST "
									+ " WHERE TAX_STR_CD = ?";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1,tax_cd);
				    		
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								tax_eff_dt=rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							cell.setCellValue("'"+tax_eff_dt+"'");
						}
						else if(i==31)
						{
							cell.setCellValue("'"+tax_eff_dt+"'");
						}
						else if(i==32)
						{

							cell.setCellValue("'"+value+"'");

							
						}
						else if (i == 43) {
							value = value.equals("Y") ? "A" : "R";
							cell.setCellValue("'"+value+"'");
						}
						else if(i==62)// SAP_APPROVAL 
						{
							sap_aprv=value;
							cell.setCellValue("'"+sap_aprv+"'");
							
						}
						else if(i==84)
						{			
							diff=0.0;
							//for prov_cust  :price:
							queryString = "SELECT CONFIRM_PRICE "
									+ "FROM FMS7_CUSTOM_DUTY "
									+ "WHERE CARGO_REF_NO =?";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1, cargo_ref);
				    		
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								price3=rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							//for prov :price:
							queryString = "SELECT CONFIRM_PRICE "
									+ "FROM FMS7_PROV_SELLER_PAY "
									+ "WHERE CARGO_REF_NO =?";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1, cargo_ref);
				    		
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								price2=rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							//for final :price:
							queryString = "SELECT CONFIRM_PRICE "
									+ "FROM FMS7_FINAL_SELLER_PAY "
									+ "WHERE CARGO_REF_NO =?";
				    		stmt4=conn.prepareStatement(queryString);
				    		stmt4.setString(1, cargo_ref);
				    		
				    		rset4=stmt4.executeQuery();
							if(rset4.next())
							{
								price1=rset4.getString(1);
							}
							rset4.close();
							stmt4.close();
							
							if(!price1.equals(""))
							{
								diff=(new BigDecimal(price1).subtract(new BigDecimal(price3))).abs().doubleValue();
							}
							else
							{
								
								diff=(new BigDecimal(price2).subtract(new BigDecimal(price3))).abs().doubleValue();
							}

							cell.setCellValue("'"+diff+"'");
						}
						else if(i==95) // FIN_SYS
						{
							if(sap_aprv.equals("Y"))
							{
								cell.setCellValue("'S'");
							}
							else
							{
								cell.setCellValue("'"+value+"'");
							}
						}
						else {
							cell.setCellValue("'"+value+"'");}
						}
					count++;

					logger.data(fname, (rset2.getString(1)+","+rset2.getString(16)+","+rset2.getString(17)+","+rset2.getString(83)+","+financial_year+","+"N"+","), conn, "");
				}//END OF custom_duty_FINAL
				
				rset2.close();
				stmt2.close();
			}
			rset1.close();
			stmt1.close();
					
		}
		stmt.close();
		rset.close();
		
		filename = migration_setup_dir + "EXPORT/FMS_PUR_SG_INV_MST_"+start_end_dt+".xlsx";
		
		fileOut = new FileOutputStream(filename);  
		
		workbook.write(fileOut);
		fileOut.close(); 
		
		logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,, "), conn);
		logger.checkpoint(fname, "<<END>>,<<FMS_PUR_SG_INV_MST>>,,,", conn);
								
		System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		System.out.println();

		msg = "Data has been Extracted Successfully.";
		msg_type = "S";
		
	}
	catch (Exception e) {

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
	
}

public void FMS_CUSTOM_PD_BOND_DTL() throws SQLException, IOException {
	
	function_nm = "FMS_CUSTOM_PD_BOND_DTL()";
	try {

		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_CUSTOM_PD_BOND_DTL>>,,,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		
		columns = "COMPANY_CD,SEQ_NO,CAL_YEAR,PD_BOND,PD_BOND_UNIT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

    	
		workbook = new XSSFWorkbook();
		spreadsheet = workbook.createSheet("Sheet 1");

		nrow = 0;
		ncell = 0;
		count = 0;
		int seq=0;
		String prv_cal_yr="",custm_duty_dt="";
		row = spreadsheet.createRow(nrow++);
		
		// Inserting Column Names
		for (int i = 0; i < columns.split(",").length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columns.split(",")[i]);
		}
		
		// Inserting Rest of the data
		queryString = "SELECT  CD_YEAR,NULL,NULL,PD_BOND_AMT,'1',EMP_CD,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS')"
				+ ",NULL,NULL,TO_CHAR(CUSTOM_DUTY_DT,'YYYY')  "
				+ "FROM FMS7_CUSTOM_DUTY  "
				+ "WHERE  (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
				+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY  TO_CHAR(CUSTOM_DUTY_DT,'YYYY')";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		logger.checkpoint(fname, "COMPANY_CD,SEQ_NO ,CAL_YEAR,TIMESTAMP", conn);
	
		while (rset.next()) {
		
		row = spreadsheet.createRow(nrow++);
		ncell = 0;
					
		for (int i = 1; i <= columns.split(",").length; i++) {
			cell = row.createCell(ncell++);
			value = rset.getString(i) == null ? "null" : rset.getString(i);
			if(i==1)
			{
				custm_duty_dt=rset.getString(10);
				value=custm_duty_dt;
				cell.setCellValue("'"+value+"'");
			}
			else if(i==2)
			{
				
				
				if(custm_duty_dt.equals(prv_cal_yr) ) {
	    			seq++;
	    			value=seq+"";
	    		}
	    		else
	    		{	seq=1;
	    			value=seq+"";
	    		}
				prv_cal_yr=custm_duty_dt;
				
				cell.setCellValue("'"+value+"'");
			}
			else
			{
				cell.setCellValue("'"+value+"'");
			}
			
		}
					
		count++;
		logger.data(fname, ('2'+","+""), conn, "");
		}
		stmt.close();
		rset.close();
		
		filename = migration_setup_dir + "EXPORT/FMS_CUSTOM_PD_BOND_DTL_"+start_end_dt+".xlsx";
		
		fileOut = new FileOutputStream(filename);  
		
		workbook.write(fileOut);
		fileOut.close(); 
		
		logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,, "), conn);
		logger.checkpoint(fname, "<<END>>,<<FMS_CUSTOM_PD_BOND_DTL>>,,,", conn);
								
		logger.checkpoint1(fname1,count+",", conn);

		System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		System.out.println();

		msg = "Data has been Extracted Successfully.";
		msg_type = "S";
		
	}
	catch (Exception e) {

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	}
	
}

public void FMS_PUR_SG_INV_MST_PURCHASE() throws SQLException, IOException{
	
	function_nm = "FMS_PUR_SG_INV_MST_PURCHASE()";
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_PUR_SG_INV_MST_PURCHASE>>,", conn);
		
		columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,PLANT_SEQ,"
				+ "CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,ALLOC_QTY,SALE_PRICE,"
				+ "SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,"
				+ "TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,"
				+ "AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,FINANCIAL_YEAR,TXN_CHARGE,TXN_AMOUNT,TAX_TXN_AMT,"
				+ "TAX_TXN_CD,TAX_TXN_EFF_DT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,TCS_CERT_FLAG,TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TCS_EFF_DT,"
				+ "SAP_EXCHNG_RATE,"
				+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,TCS_TDS,SYS_INV_NO,"
				+ "PAY_INSERT_BY,PAY_INSERT_DT,PAY_RECV_AMT,PAY_RECV_DT,PAY_REMARK,PAY_UPDATE_BY,PAY_UPDATE_DT,CARGO_NO,BOE_NO,INV_FLAG,CIF_AMT,"
				+ "ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,SUG_QTY,SUG_PERCENT,QTY_UNIT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,"
				+ "MARKET_MARGIN,OTHER_CHARGES,MARKET_MARGIN_AMT,OTHER_CHARGES_AMT,FIN_SYS";
		
		workbook = new XSSFWorkbook();
		spreadsheet = workbook.createSheet("Sheet 1");

		nrow = 0;
		ncell = 0;
		
		int count2=0;
		String cd="",contract_type="",tax="",fin_year="",plant_seq_no="",tcs_flag="",td_cd="",tax_code="",gross_amt="",sn_rev_no="",sn_no="",org_plant_seq_no="",othr_chrg="",trans_chrg="",org_td_cd="",sap_aprv="";
		
		row = spreadsheet.createRow(nrow++);
		
		// Inserting Column Names
		for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						cell.setCellValue(columns.split(",")[i]);
		}
		
		// Inserting Rest of the data
		queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
				+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) "
				+ "FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
		stmt = conn.prepareStatement(queryString);
		rset = stmt.executeQuery();
		
		logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD, PLANT_SEQ, FINANCIAL_YEAR,CONTRACT_TYPE,INVOICE_NO,TIMESTAMP", conn);

		while (rset.next()) {
			
			query_ind = 1;	
			
			queryString1 = "SELECT A.MAN_CD, B.TRADER_CD "
					+ "FROM FMS7_MAN_REQ_MST A, FMS7_TRADER_MST B WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? AND A.MAN_CD IN (";
			if (pipeline_cd.split(",").length > 0) {
				queryString1 += "?";
				for (int i = 1; i < pipeline_cd.split(",").length; i++) {
					queryString1 += ",?";
				}
			}
		   if (igx_cd.split(",").length > 0) {
				queryString1 += ",?";
				for (int i = 1; i < igx_cd.split(",").length; i++) {
					queryString1 += ",?";
				}
			}
		   if (tank_cd.split(",").length > 0) {
				queryString1 += ",?";
				for (int i = 1; i < tank_cd.split(",").length; i++) {
					queryString1 += ",?";
				}
			}
			queryString1 += ") AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY MAN_CD";
	
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(query_ind++, rset.getString(2));
			for (int i = 0; i < pipeline_cd.split(",").length; i++) {
				stmt1.setString(query_ind++, pipeline_cd.split(",")[i]);
			}
			for (int i = 0; i < igx_cd.split(",").length; i++) {
				stmt1.setString(query_ind++, igx_cd.split(",")[i]);
			}
			for (int i = 0; i < tank_cd.split(",").length; i++) {
				stmt1.setString(query_ind++, tank_cd.split(",")[i]);
			}
			stmt1.setString(query_ind++, delta_FromDt);
			stmt1.setString(query_ind++, delta_FromDt);
			stmt1.setString(query_ind++, delta_ToDt);
			stmt1.setString(query_ind++, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			while(rset1.next()) {
				
				queryString2 = "SELECT A.PARTY_CD ,A.CARGO_REF_NO, NULL, NULL, NULL, NULL, B.DOM_BUY_FLAG, A.BUYER_PLANT_CD, NULL,"
						+ " A.PLANT_CD, NULL, NULL, A.INVOICE_NO, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.FORTNIGHT, TO_CHAR(A.PERIOD_ST_DT, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "TO_CHAR(A.PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS') ,"
						+ " A.ALLOC_QTY, A.CONF_PRICE, B.PRICE_UNIT, A.INVOICE_AMT_USD, A.EXCHG_RT_CD, TO_CHAR(A.EXCHG_RT_DT, 'DD/MM/YYYY HH24:MI:SS'),"//24
						+ "A.EXCHG_RT_VAL, '1', A.INVOICE_AMT, A.INVOICE_TAX_AMT, NULL, NULL, NULL, NULL, NULL, NULL, NUL"//35
						+ "L, A.CHECKED_FLAG, A.CHECKED_BY, TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.AUTHORIZED_FLAG, A.AUTHORIZED_BY, "//40
						+ "TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.APPROVED_FLAG, A.APPROVED_BY,"
						+ "TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL,"///49
						+ "NULL, NULL, NULL, 'O', A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), 'N'"
						+ ", NULL, NULL, NULL, NULL, NULL,A.SUN_APPROVAL,A.SUN_APPROVAL_BY,TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, NULL, 'TDS', NULL, A.PAY_INSERT_BY, TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "A.PAY_RECV_AMT, TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_REMARK, NULL, NULL, NULL, NULL, 'F', NULL, NULL, A.REMARK, NULL, NULL, NULL, NULL, '1', A.TC_PRICE, A.TC_AMT,"//90
						+ " A.MM_PRICE, A.OC_PRICE, A.MM_AMT, A.OC_AMT,NULL "//94
						+ "FROM FMS7_DOM_PUR_INV_DTL A, FMS7_MAN_CONFIRM_CARGO_DTL B WHERE B.MAN_CD = ? AND A.PLANT_CD != '0' "
						+ "AND A.CARGO_REF_NO = B.CARGO_REF_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";	
				
				stmt2 = conn.prepareStatement(queryString2);					
				stmt2.setString(1, (rset1.getString(1)));
				stmt2.setString(2, delta_FromDt);
				stmt2.setString(3, delta_FromDt);
				stmt2.setString(4, delta_ToDt);
				stmt2.setString(5, delta_ToDt);
				rset2 = stmt2.executeQuery();
				
				while (rset2.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					plant_seq_no=rset2.getString(10);
					org_plant_seq_no=plant_seq_no;
					othr_chrg="";trans_chrg="";
					org_td_cd="";sn_no="";sn_rev_no="";
					
//					BigDecimal alloc_qty_chrg = BigDecimal.valueOf(rset2.getDouble(19));
//					BigDecimal trans_chrg_cal = BigDecimal.ZERO;
//					BigDecimal othr_chrg_cal = BigDecimal.ZERO;
					
					tcs_flag="";fin_year="";tax_code="";sap_aprv="";
					
				 for(int i = 0; i < columns.split(",").length; i++) {
				 
						cell = row.createCell(ncell++);
						value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
						if(i==0)
						{
							value=rset2.getString(1);
							
							queryString3 = "SELECT TRADER_ABBR,TRADER_CD FROM FMS7_TRADER_MST WHERE TRADER_CD  = ? ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, value);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								abbr=rset3.getString(1);
								td_cd=rset3.getString(2);
								}
							else {
								abbr=null;
								td_cd=null;
							}
							org_td_cd=td_cd;
							org_abbr=abbr;
							rset3.close();
							stmt3.close();
							
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
//						        abbr=value;
						    }
							if(abbr!=null)
							{
								if (abbr.contains("RIL")) {
									 abbr = "RIL";
								}
							}
							
							int plant_seq=0;
							if(org_abbr != null && org_abbr.contains("CBM"))
							{
								 plant_seq=Integer.parseInt(plant_seq_no);
								 plant_seq=plant_seq+3;
								 plant_seq_no=plant_seq+"";
								
							} 
							//for plant :
							if(org_abbr != null && mpe.trader_map.containsKey(abbr+"-"+plant_seq_no)   && !org_abbr.contains("CBM") )
							{
								plant_seq_no=mpe.trader_map.get(abbr+"-"+plant_seq_no);
								plant_seq_no=plant_seq_no.split("-")[0];
							}
							else if(org_abbr != null && !org_abbr.contains("CBM"))
							{
								plant_seq_no=rset2.getString(10);
							}
							
							cell.setCellValue("'"+abbr+"'");
						
						}
						
						else if(i == 1) {
							
							queryString3 = "SELECT SN_REV_NO,SN_NO FROM FMS7_TRADER_CONT_MST "
									+ "WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG = ? ORDER BY SN_REV_NO DESC ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, "%"+value+"%");
							stmt3.setString(2,rset2.getString(7).equals("T") ? "Y" : rset2.getString(7));
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
							if(rset2.getString(7).equals("Y")) {
								sn_rev_no=rset3.getString(1);
								sn_no=rset3.getString(2);
								value = "-D-"+value +"-"+rset3.getString(1);
								
								}
							else if(rset2.getString(7).equals("K")) {
								sn_rev_no=rset3.getString(1);
								sn_no=rset3.getString(2);
								value = "-I-"+value +"-"+rset3.getString(1);
								}
							else if(rset2.getString(7).equals("T")) {
								sn_rev_no=rset3.getString(1);
								sn_no=rset3.getString(2);
								value = "-T-"+value +"-"+rset3.getString(1);
								}
							else {
								sn_rev_no=rset3.getString(1);
								sn_no=rset3.getString(2);
								value = "-"+rset2.getString(7)+"-"+value +"-"+rset3.getString(1);
								}
							}
							else
							{
								sn_rev_no=null;
								sn_no=null;	
							}
							rset3.close();
							stmt3.close();
							
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 6) {	//CONTRACT_TYPE
							if(value.equals("Y")) {
								value = "D";
							}
							else if(value.equals("K")) {
								value = "I";
							}
							else {
								value = rset2.getString(7);
							}
							contract_type=value;
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 7) {	//BU_UNIT
							value = (rset2.getInt(8) + 1) +"";
							cell.setCellValue("'"+value+"'");
						}
						else if(i==9)
						{
							cell.setCellValue("'"+plant_seq_no+"'");
						}
						else if(i == 11) {	//INVOICE_SEQ
							queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
									+" || '-' || "
									+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
									+ "FROM DUAL";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, rset2.getString(17));
							stmt3.setString(2, rset2.getString(18));
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								value = rset3.getString(1) == null ? "" :rset3.getString(1);
							}
							fin_year = value;
							rset3.close();
							stmt3.close();
							
							cell.setCellValue("'"+value+"'");
							}
						
						
						else if(i==26)//gross_amt
						{
							cell.setCellValue("'"+value+":"+rset2.getString(22)+"'");
						}
						else if(i == 27) {	//TAX_AMT
							queryString3 = "SELECT TAX_STRUCT_DTL FROM FMS7_TRADER_TAX_STRUCT_DTL WHERE TRADER_CD = ? AND PLANT_SEQ_NO = ? ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, rset1.getString(2));
							stmt3.setString(2, rset2.getString(10));
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								tax = rset3.getString(1);
								
								if(tax.equals("VAT 5 AP 5%")) {
									tax = "VAT 5%";
								}
								if(tax.equals("CST 2 AP 2%"))
								{
									tax="CST 2%";
								}
								if(tax.contains("STAX 0%"))
								{
									tax="ZSTAX 0%";
								}
								if(tax.contains("CST 2 MH 2%"))
								{
									tax="CST 2%";
								}
//								else if(tax.contains("ADD. VAT"))
//								{
//									tax="ADVAT";
//								}

								
							}
							else {
								tax = null;
							}
							rset3.close();
							stmt3.close();
							
							if(tax!=null) {
							value = tax.split(" ")[1];
							}
							else {
								value = "0%";
							}
							
							
							value=rset2.getString(28);
							
							cell.setCellValue("'"+value+"'");
						}	
						else if(i == 28) {	//TAX_STRUCT_CD
//								queryString3 = "SELECT TAX_STRUCT_DTL FROM FMS7_TRADER_TAX_STRUCT_DTL WHERE TRADER_CD = ? AND PLANT_SEQ_NO = ? ";
//							stmt3 = conn.prepareStatement(queryString3);
//							stmt3.setString(1, rset1.getString(2));
//							stmt3.setString(2, rset2.getString(9));
//							rset3 = stmt3.executeQuery();
//							if(rset3.next()) {
//								value = rset3.getString(1);
//								
//								if(value.equals("VAT 5 AP 5%")) {
//									value = "VAT 5%";
//								}
//							}
//							rset3.close();
//							stmt3.close();
						
							value = tax;
							cell.setCellValue("'"+value+"'");
						}
						else if(i==30)
						{
//							// Other Charges
//							queryString3 = "SELECT  A.TARIFF "
//									+ "FROM FMS7_TRD_OTH_TARIFF_DTL A , FMS7_TRADER_CONT_MST B "
//									+ "WHERE A.CONTRACT_NO=B.SN_NO AND A.CONTRACT_REV_NO=B.SN_REV_NO AND A.AGR_NO=B.FGSA_NO "
//									+ "AND A.AGR_REV_NO=B.FGSA_REV_NO AND A.TRADER_CD=B.CUSTOMER_CD AND "
//									+ " A.TRADER_CD = ? AND A.CONTRACT_REV_NO = ? AND  A.CONTRACT_NO=? AND A.PLANT_SEQ_NO=?   "
//									+ " AND A.LINE_ITEM_CD = '102' "
//									+ "AND A.EFF_DT = ( SELECT MAX(EFF_DT)  "
//									+ "FROM FMS7_TRD_OTH_TARIFF_DTL C WHERE C.CONTRACT_NO = A.CONTRACT_NO  "
//									+ "AND C.CONTRACT_REV_NO = A.CONTRACT_REV_NO AND C.AGR_NO = A.AGR_NO  "
//									+ "AND C.AGR_REV_NO = A.AGR_REV_NO AND C.PLANT_SEQ_NO = A.PLANT_SEQ_NO "
//									+ " AND C.TRADER_CD = A.TRADER_CD AND C.LINE_ITEM_CD = '102' )  ";
//							stmt3 = conn.prepareStatement(queryString3);
//							stmt3.setString(1,org_td_cd);
//							stmt3.setString(2,sn_rev_no);
//							stmt3.setString(3,sn_no);
//							stmt3.setString(4,org_plant_seq_no);
//							rset3 = stmt3.executeQuery();
//							while(rset3.next()) {
//								othr_chrg=rset3.getString(1);
//							}
//							
//							rset3.close();
//							stmt3.close();
							
							
//							//Transportation Charges 
//							queryString3 = "SELECT  A.TARIFF "
//									+ "FROM FMS7_TRD_OTH_TARIFF_DTL A , FMS7_TRADER_CONT_MST B  "
//									+ "WHERE A.CONTRACT_NO=B.SN_NO AND A.CONTRACT_REV_NO=B.SN_REV_NO AND A.AGR_NO=B.FGSA_NO "
//									+ "AND A.AGR_REV_NO=B.FGSA_REV_NO AND A.TRADER_CD=B.CUSTOMER_CD AND "
//									+ " A.TRADER_CD = ? AND A.CONTRACT_REV_NO = ? AND  A.CONTRACT_NO=? AND A.PLANT_SEQ_NO=? AND A.LINE_ITEM_CD = '103' "
//									+ " AND A.EFF_DT = ( SELECT MAX(C.EFF_DT) "
//									+ "FROM FMS7_TRD_OTH_TARIFF_DTL C WHERE C.CONTRACT_NO = A.CONTRACT_NO "
//									+ "AND C.CONTRACT_REV_NO = A.CONTRACT_REV_NO AND C.AGR_NO = A.AGR_NO "
//									+ "AND C.AGR_REV_NO = A.AGR_REV_NO AND C.PLANT_SEQ_NO = A.PLANT_SEQ_NO"
//									+ " AND C.TRADER_CD = A.TRADER_CD AND C.LINE_ITEM_CD = '103') ";
//							stmt3 = conn.prepareStatement(queryString3);
//							stmt3.setString(1, org_td_cd);
//							stmt3.setString(2,sn_rev_no);
//							stmt3.setString(3,sn_no);
//							stmt3.setString(4,org_plant_seq_no);
//
//							
//							rset3 = stmt3.executeQuery();
//							while(rset3.next()) {
//								trans_chrg=rset3.getString(1);
//								}
//							
//							rset3.close();
//							stmt3.close();
////							if(sn_no.equals("14") && sn_rev_no.equals("2") && org_plant_seq_no.equals("1"))
//							{
//								System.out.println(trans_chrg+":"+othr_chrg+":"+sn_rev_no+":"+sn_no+":"+org_plant_seq_no+":"+org_td_cd+":"+rset2.getString(13));
//							}
							

							// Calculate trans_chrg_cal
//							if (trans_chrg != null && !trans_chrg.equalsIgnoreCase("null") && !trans_chrg.trim().isEmpty()) {
//							    BigDecimal trans_chrg_bd = new BigDecimal(trans_chrg.trim());
//							    trans_chrg_cal = alloc_qty_chrg.multiply(trans_chrg_bd);
//							}
//
//							// Calculate othr_chrg_cal
//							if (othr_chrg != null && !othr_chrg.equalsIgnoreCase("null") && !othr_chrg.trim().isEmpty()) {
//							    BigDecimal othr_chrg_bd = new BigDecimal(othr_chrg.trim());
//							    othr_chrg_cal = alloc_qty_chrg.multiply(othr_chrg_bd);
//							}
							 
//							 cell.setCellValue("'" +  othr_chrg_cal + ":" + trans_chrg_cal + "'");
							 cell.setCellValue("'" +  rset2.getString(90) + ":" + rset2.getString(94) + "'");
							 
						}
						else if(i==33)//netpayable adding -flag(tcs)
						{

							queryString3 = "SELECT TURNOVER_FLAG FROM FMS7_TRADER_TURNOVER_DTL WHERE TRADER_CD = ? AND FINANCIAL_YEAR = ? ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, td_cd);
							stmt3.setString(2, fin_year);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								tcs_flag=rset3.getString(1);
							}
							else
							{
								tcs_flag="N";	
							}
							rset3.close();
							stmt3.close();
							if(tcs_flag.equals("N"))
							{
								if(contract_type.equals("I"))
								{
									tcs_flag=null;
								}
							}
							cell.setCellValue("'" + tcs_flag + "'");
							
						}
						 
						else if(i == 41) {	//APPROVE_FLAG
							if (rset2.getString(42)!=null) {
								if (rset2.getString(42).equals("Y")) {
									value = "A";
								} else if (rset2.getString(42).equals("N")) {
									value = "R";
								} 
							}
							else {
								value = rset2.getString(42);
							}
							cell.setCellValue("'"+value+"'");
						}
						
						
						else if(i == 46) {	//FINANCIAL_YEAR

						value = fin_year;
						cell.setCellValue("'"+value+"'");
						
						}
						else if(i==55)//flag
						{
//							queryString3 = "SELECT TURNOVER_FLAG FROM FMS7_TRADER_TURNOVER_DTL WHERE TRADER_CD = ? AND FINANCIAL_YEAR = ? ";
//							stmt3 = conn.prepareStatement(queryString3);
//							stmt3.setString(1, rset1.getString(2));
//							stmt3.setString(2, fin_year);
//							rset3 = stmt3.executeQuery();
//							if(rset3.next()) {
//								tcs_flag=rset3.getString(1);
//								}
//							else
//							{
//								tcs_flag="N";	
//							}
//							rset3.close();
//							stmt3.close();
//							
							cell.setCellValue("'"+tcs_flag+"'");
							
						}
						
						else if(i == 57) {	//TCS_FACTOR
							
							if( tcs_flag!=null && tcs_flag.equals("Y")) {
								queryString3 = "SELECT TAX_CODE FROM FMS7_TAX_MST  WHERE TAX_ALIAS_CODE = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, "TCS");
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									tax_code=rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								
								queryString3 = "SELECT FACTOR FROM FMS7_TAX_STRUCTURE_DTL  WHERE TAX_CODE = ? AND TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY APP_DATE DESC ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, tax_code);
								stmt3.setString(2, "22");
								stmt3.setString(3, rset2.getString(14));
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
									}
								else
								{
									value ="0.075";	
								}
								rset3.close();
								stmt3.close();
								
								cell.setCellValue("'"+value+"'");
							}
							else 
							{
								/* System.out.println("==sg=>"+value); */
								cell.setCellValue("'"+value+"'");
							}
							
						}
						else if(i==61)// SAP_APPROVAL 
						{
							sap_aprv=value;
							cell.setCellValue("'"+sap_aprv+"'");
						}
						else if(i==68)
						{
							if(tcs_flag!=null)
							{
								if(tcs_flag.equals("Y"))
								{
									cell.setCellValue("'TCS'");
								}
								else if(tcs_flag.equals("N"))
								{
									cell.setCellValue("'TDS'");
								}
							}
							else
							{
								cell.setCellValue("'null'");
							}
						}
						else if(i==94) // FIN_SYS
						{
							if(sap_aprv.equals("Y"))
							{
								cell.setCellValue("'S'");
							}
							else
							{
								cell.setCellValue("'"+value+"'");
							}
						}

						else
						{
							cell.setCellValue("'"+value+"'");
						}
												
						
					 }
					 count++;
					 count2++;
					 logger.data(fname, (cd+","+abbr+","+plant_seq_no+","+fin_year+","+contract_type+","+rset2.getString(13)+","), conn, "");
							
				}
				rset2.close();
				stmt2.close();				
			}			
			rset1.close();
			stmt1.close();			
		}
		rset.close();
		stmt.close();	
		
		filename = migration_setup_dir + "EXPORT/FMS_PUR_SG_INV_MST_PURCHASE_"+start_end_dt+".xlsx";
		
		fileOut = new FileOutputStream(filename);  
		
		workbook.write(fileOut);
		fileOut.close(); 
		
		logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count2 + ", "), conn);
		logger.checkpoint(fname, "<<END>>,<<FMS_PUR_SG_INV_MST_PURCHASE>>,", conn);
								
		logger.checkpoint1(fname1,count+",", conn);

		System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		System.out.println();

		msg = "Data has been Extracted Successfully.";
		msg_type = "S";
		
	}catch(Exception e) {
		
		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		
	}
}

public void FMS_PUR_PG_INV_MST() throws SQLException, IOException{
	
	function_nm = "FMS_PUR_PG_INV_MST()";
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_PUR_PG_INV_MST>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns="COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,PLANT_SEQ,"
				+ "CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,ALLOC_QTY,SALE_PRICE,"
				+ "SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,"
				+ "TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,"
				+ "AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,FINANCIAL_YEAR,TXN_CHARGE,TXN_AMOUNT,TAX_TXN_AMT,"
				+ "TAX_TXN_CD,TAX_TXN_EFF_DT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,TCS_CERT_FLAG,TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TCS_EFF_DT,"
				+ "SAP_EXCHNG_RATE,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,TCS_TDS,SYS_INV_NO,"
				+ "PAY_INSERT_BY,PAY_INSERT_DT,PAY_RECV_AMT,PAY_RECV_DT,PAY_REMARK,PAY_UPDATE_BY,PAY_UPDATE_DT,CARGO_NO,BOE_NO,INV_FLAG,CIF_AMT,"
				+ "ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,SUG_QTY,SUG_PERCENT,QTY_UNIT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,"
				+ "MARKET_MARGIN,OTHER_CHARGES,MARKET_MARGIN_AMT,OTHER_CHARGES_AMT,FIN_SYS";
		
		workbook = new XSSFWorkbook();
		spreadsheet = workbook.createSheet("Sheet 1");

		nrow = 0;
		ncell = 0;
		count = 0;
		String cd="",contract_type="",fin_year="",tax="",plant_seq_no="",tcs_flag="",td_cd="",tax_code="",sap_aprv="";
		
		row = spreadsheet.createRow(nrow++);
		
		// Inserting Column Names
		for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						cell.setCellValue(columns.split(",")[i]);
		}
		
		// Inserting Rest of the data
		queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
				+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
				+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
		stmt = conn.prepareStatement(queryString);
		rset = stmt.executeQuery();
		
		logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD, PLANT_SEQ, FINANCIAL_YEAR,CONTRACT_TYPE,INVOICE_NO,TIMESTAMP", conn);

		while (rset.next()) {
//			System.out.println("=");
			query_ind = 1;	
			
			queryString1 = "SELECT A.MAN_CD, B.TRADER_CD FROM FMS7_MAN_REQ_MST A, FMS7_TRADER_MST B WHERE A.TRD_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? AND A.MAN_CD IN (";
			if (pipeline_cd.split(",").length > 0) {
				queryString1 += "?";
				for (int i = 1; i < pipeline_cd.split(",").length; i++) {
					queryString1 += ",?";
				}
			}
		   if (igx_cd.split(",").length > 0) {
				queryString1 += ",?";
				for (int i = 1; i < igx_cd.split(",").length; i++) {
					queryString1 += ",?";
				}
			}
		   if (tank_cd.split(",").length > 0) {
				queryString1 += ",?";
				for (int i = 1; i < tank_cd.split(",").length; i++) {
					queryString1 += ",?";
				}
			}
			queryString1 += ") AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY MAN_CD";
	
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(query_ind++, rset.getString(2));
			for (int i = 0; i < pipeline_cd.split(",").length; i++) {
				stmt1.setString(query_ind++, pipeline_cd.split(",")[i]);
			}
			for (int i = 0; i < igx_cd.split(",").length; i++) {
				stmt1.setString(query_ind++, igx_cd.split(",")[i]);
			}
			for (int i = 0; i < tank_cd.split(",").length; i++) {
				stmt1.setString(query_ind++, tank_cd.split(",")[i]);
			}
			stmt1.setString(query_ind++, delta_FromDt);
			stmt1.setString(query_ind++, delta_FromDt);
			stmt1.setString(query_ind++, delta_ToDt);
			stmt1.setString(query_ind++, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			while(rset1.next()) {
//				System.out.println(":"+rset1.getString(1));
				queryString2 = "SELECT A.PARTY_CD ,A.CARGO_REF_NO, NULL, NULL, NULL, NULL, B.DOM_BUY_FLAG, A.BUYER_PLANT_CD, NULL,"//9
						+ " A.PLANT_CD, NULL, NULL, A.INVOICE_NO, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.FORTNIGHT, TO_CHAR(A.PERIOD_ST_DT, 'DD/MM/YYYY HH24:MI:SS'), "//16
						+ "TO_CHAR(A.PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS') ,"
						+ " A.ALLOC_QTY, A.CONF_PRICE, B.PRICE_UNIT, A.INVOICE_AMT_USD, A.EXCHG_RT_CD, TO_CHAR(A.EXCHG_RT_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "A.EXCHG_RT_VAL, '1', A.INVOICE_AMT, A.INVOICE_TAX_AMT, NULL, NULL, A.INVOICE_AMT, NULL, NULL, NULL, NUL"//35
						+ "L, A.CHECKED_FLAG, A.CHECKED_BY, TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.AUTHORIZED_FLAG, A.AUTHORIZED_BY, "//40
						+ "TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.APPROVED_FLAG, A.APPROVED_BY,"//43
						+ "TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL,"//49
						+ "NULL, NULL, NULL, 'O', A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), 'N'"
						+ ", NULL, NULL, NULL, NULL, NULL,A.SUN_APPROVAL,A.SUN_APPROVAL_BY,TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, NULL, 'TDS', NULL, A.PAY_INSERT_BY, TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), "//
						+ "A.PAY_RECV_AMT, TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_REMARK, NULL, NULL, NULL, NULL, 'F', NULL, NULL, A.REMARK, NULL, NULL, NULL, NULL, '1', "
						+ "A.TC_PRICE, A.TC_AMT,"//90
						+ " A.MM_PRICE, A.OC_PRICE, A.MM_AMT, A.OC_AMT,NULL "//95
						+ "FROM FMS7_DOM_PUR_INV_DTL_PG A, FMS7_MAN_CONFIRM_CARGO_DTL B WHERE B.MAN_CD = ? AND A.PLANT_CD != '0' "
						+ "AND A.CARGO_REF_NO = B.CARGO_REF_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND "
						+ "(? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";	
				
				stmt2 = conn.prepareStatement(queryString2);					
				stmt2.setString(1, (rset1.getString(1)));
				stmt2.setString(2, delta_FromDt);
				stmt2.setString(3, delta_FromDt);
				stmt2.setString(4, delta_ToDt);
				stmt2.setString(5, delta_ToDt);
				rset2 = stmt2.executeQuery();
				
				while (rset2.next()) {
					//					dom_buy_flag = rset2.getString(6);
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
//					System.out.println("-");
					plant_seq_no=rset2.getString(10);
					
					tcs_flag="";fin_year="";tax_code="";
					
					for(int i = 0; i < columns.split(",").length; i++) {
				 
						cell = row.createCell(ncell++);
						value = rset2.getString(i+1) == null ? "null" : rset2.getString(i+1);
						if(i==0)
						{
							value=rset2.getString(1);
							
							queryString3 = "SELECT TRADER_ABBR,TRADER_CD FROM FMS7_TRADER_MST WHERE TRADER_CD  = ? ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, value);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								abbr=rset3.getString(1);
								td_cd=rset3.getString(2);
								}
							else {
								abbr=null;
								td_cd=null;
							}
							org_abbr=abbr;
							rset3.close();
							stmt3.close();
							
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
//						        abbr=value;
						    }
							if(abbr!=null)
							{
								if (abbr.contains("RIL")) {
									 abbr = "RIL";
								}
							}
							
							int plant_seq=0;
							if(org_abbr != null && org_abbr.contains("CBM"))
							{
								 plant_seq=Integer.parseInt(plant_seq_no);
								 plant_seq=plant_seq+3;
								 plant_seq_no=plant_seq+"";
								
							} 
							//for plant :
							if(org_abbr != null && mpe.trader_map.containsKey(abbr+"-"+plant_seq_no)   && !org_abbr.contains("CBM") )
							{
								plant_seq_no=mpe.trader_map.get(abbr+"-"+plant_seq_no);
								plant_seq_no=plant_seq_no.split("-")[0];
							}
							else if(org_abbr != null && !org_abbr.contains("CBM"))
							{
								plant_seq_no=rset2.getString(10);
							}
							cell.setCellValue("'"+abbr+"'");
						
						}
						
						else if(i == 1) {
							
							queryString3 = "SELECT SN_REV_NO FROM FMS7_TRADER_CONT_MST "
									+ "WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG = ? ORDER BY SN_REV_NO DESC ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, "%"+value+"%");
							stmt3.setString(2,rset2.getString(7).equals("T") ? "Y" : rset2.getString(7));
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
							if(rset2.getString(7).equals("Y")) {
								value = "-D-"+value +"-"+rset3.getString(1);
								}
							else if(rset2.getString(7).equals("K")) {
								value = "-I-"+value +"-"+rset3.getString(1);
								}
							else if(rset2.getString(7).equals("T")) {
								value = "-T-"+value +"-"+rset3.getString(1);
								}
							else {
								value = "-"+rset2.getString(7)+"-"+value +"-"+rset3.getString(1);
								}
							}
							rset3.close();
							stmt3.close();
							
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 6) {	//CONTRACT_TYPE
							if(value.equals("Y")) {
								value = "D";
							}
							else if(value.equals("K")) {
								value = "I";
							}
							else {
								value = rset2.getString(7);
							}
							contract_type=value;
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 7) {	//BU_UNIT
							value = (rset2.getInt(8) + 1) +"";
							cell.setCellValue("'"+value+"'");
						}
						else if(i==9)
						{
							cell.setCellValue("'"+plant_seq_no+"'");
						}
						
						else if(i == 11) {	//INVOICE_SEQ
							queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
									+" || '-' || "
									+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
									+ "FROM DUAL";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, rset2.getString(17));
							stmt3.setString(2, rset2.getString(18));
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								value = rset3.getString(1) == null ? "" :rset3.getString(1);
							}
							fin_year = value;
							rset3.close();
							stmt3.close();
							
							
							cell.setCellValue("'"+value+"@"+rset2.getString(15)+"@"+rset2.getString(16)+"@"+rset2.getString(17)+"'");
						}
						else if(i==26)// GROSS AMT 
						{
							cell.setCellValue("'"+value+","+rset2.getString(22)+"'");
							//  inv_amt     ,    inv_amt_usd
							
						}
						else if(i == 28) {	//TAX_AMT
							
							
							queryString3 = "SELECT TAX_STRUCT_DTL FROM FMS7_TRADER_TAX_STRUCT_DTL WHERE TRADER_CD = ? AND PLANT_SEQ_NO = ? ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, rset1.getString(2));
							stmt3.setString(2, rset2.getString(10));
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								tax = rset3.getString(1);
								
								if(tax.equals("VAT 5 AP 5%")) {
									tax = "VAT 5%";
								}
								if(tax.equals("CST 2 AP 2%"))
								{
									tax="CST 2%";
								}
								if(tax.contains("STAX 0%"))
								{
									tax="ZSTAX 0%";
								}
								if(tax.contains("CST 2 MH 2%"))
								{
									tax="CST 2%";
								}
							}
							else {
								tax = null;
							}
							rset3.close();
							stmt3.close();
							
							
							
//							if(tax!=null) {
//							value = tax.split(" ")[1];
//							}
//							else {
//								value = "0%";
//							}
							value=rset2.getString(28);
							cell.setCellValue("'"+tax+"'");
						}	
//						else if(i == 28) {	//TAX_STRUCT_CD
////								queryString3 = "SELECT TAX_STRUCT_DTL FROM FMS7_TRADER_TAX_STRUCT_DTL WHERE TRADER_CD = ? AND PLANT_SEQ_NO = ? ";
////							stmt3 = conn.prepareStatement(queryString3);
////							stmt3.setString(1, rset1.getString(2));
////							stmt3.setString(2, rset2.getString(9));
////							rset3 = stmt3.executeQuery();
////							if(rset3.next()) {
////								value = rset3.getString(1);
////								
////								if(value.equals("VAT 5 AP 5%")) {
////									value = "VAT 5%";
////								}
////							}
////							rset3.close();
////							stmt3.close();
//							value = tax;
//							cell.setCellValue("'"+value+"'");
//						}
						else if(i==30) //INV_AMT
						{
							cell.setCellValue("'"+value+","+rset2.getString(22)+":"+rset2.getString(90)+"="+rset2.getString(94)+"'");
							//  inv_amt     ,    inv_amt_usd   - tran_chrgamt       otherchrg_amt
							
						}
						else if(i==33)//netpayable adding -flag(tcs)
						{
//							queryString3 = "SELECT  FROM FMS7_TRADER_TURNOVER_DTL WHERE TRADER_CD = ? AND FINANCIAL_YEAR = ? ";
//							stmt3 = conn.prepareStatement(queryString3);
//							stmt3.setString(1, rset1.getString(2));
//							stmt3.setString(2, fin_year);
//							rset3 = stmt3.executeQuery();
//							if(rset3.next()) {
//								tcs_flag=rset3.getString(1);
//								}
//							else
//							{
//								tcs_flag="N";	
//							}
//							rset3.close();
//							stmt3.close();
							
							queryString3 = "SELECT TURNOVER_FLAG FROM FMS7_TRADER_TURNOVER_DTL WHERE TRADER_CD = ? AND FINANCIAL_YEAR = ? ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, td_cd);
							stmt3.setString(2, fin_year);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								tcs_flag=rset3.getString(1);
								}
							else
							{
								tcs_flag="N";	
							}
							rset3.close();
							stmt3.close();
							if(tcs_flag.equals("N"))
							{
								if(contract_type.equals("I"))
								{
									tcs_flag=null;
								}
							}
							
							cell.setCellValue("'"+tcs_flag+"'");
						}
						 
						else if(i == 41) {	//APPROVE_FLAG
							if (rset2.getString(42)!=null) {
								if (rset2.getString(42).equals("Y")) {
									value = "A";
								} else if (rset2.getString(42).equals("N")) {
									value = "R";
								} 
							}
							else {
								value = rset2.getString(42);
							}
							cell.setCellValue("'"+value+"'");
						}
						
						
						else if(i == 46) {	//FINANCIAL_YEAR

						value = fin_year;
						cell.setCellValue("'"+value+"'");
						
						}
						else if(i==55)//flag
						{
//							queryString3 = "SELECT TURNOVER_FLAG FROM FMS7_TRADER_TURNOVER_DTL WHERE TRADER_CD = ? AND FINANCIAL_YEAR = ? ";
//							stmt3 = conn.prepareStatement(queryString3);
//							stmt3.setString(1, rset1.getString(2));
//							stmt3.setString(2, fin_year);
//							rset3 = stmt3.executeQuery();
//							if(rset3.next()) {
//								tcs_flag=rset3.getString(1);
//								}
//							else
//							{
//								tcs_flag="N";	
//							}
//							rset3.close();
//							stmt3.close();
							
							cell.setCellValue("'"+tcs_flag+"'");
							
						}
						else if(i == 57) {	//TCS_FACTOR
							if( tcs_flag!=null && tcs_flag.equals("Y") ) {
								queryString3 = "SELECT TAX_CODE FROM FMS7_TAX_MST  WHERE TAX_ALIAS_CODE = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, "TCS");
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									tax_code=rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								
								queryString3 = "SELECT FACTOR FROM FMS7_TAX_STRUCTURE_DTL  WHERE TAX_CODE = ? AND TAX_STR_CD = ? AND APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ORDER BY APP_DATE DESC ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, tax_code);
								stmt3.setString(2, "22");
								stmt3.setString(3, rset2.getString(14));
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1);
									}
								else
								{
									value ="0.075";	
								}
								rset3.close();
								stmt3.close();
								
								cell.setCellValue("'"+value+"'");
							}
							else
							{
								cell.setCellValue("'"+value+"'");	
							}
							
							
						}
//						else if(i==64)
//						{
//							if (tcs_flag.equals("N")) {
//
//									gross_amt = rset2.getString(27);
//									double tds_Amt = (Double.parseDouble(gross_amt) * Double.parseDouble("0.1")) / 100;
//									value = tds_Amt + "";
//									cell.setCellValue("'" + value + "'");
//
//							}
//							else
//							{
//								cell.setCellValue("'" + null + "'");
//							}
////							
////							System.out.println(""+tds_flag+":"+fin_year+":"+rset2.getString(13));
//						}
						else if(i==61)// SAP_APPROVAL 
						{
							sap_aprv=value;
							cell.setCellValue("'"+sap_aprv+"'");
						}
						else if(i==68)
						{
							if(tcs_flag!=null)
							{
								if(tcs_flag.equals("Y"))
								{
									cell.setCellValue("'TCS'");
								}
								else if(tcs_flag.equals("N"))
								{
									cell.setCellValue("'TDS'");
								}
							}
							else
							{
								cell.setCellValue("'null'");
							}
						}
						else if(i==95)
						{
							
							
						}
//						else if(i==56)//
//						{
//							queryString3 = "SELECT TURNOVER_FLAG FROM FMS7_TRADER_TURNOVER_DTL WHERE TRADER_CD = ? AND FINANCIAL_YEAR = ? ";
//							stmt3 = conn.prepareStatement(queryString3);
//							stmt3.setString(1, rset1.getString(2));
//							stmt3.setString(2, fin_year);
//							rset3 = stmt3.executeQuery();
//							if(rset3.next()) {
//								tcs_flag=rset3.getString(1);
//								}
//							else
//							{
//								tcs_flag="N";	
//							}
//							rset3.close();
//							stmt3.close();
//							
//							cell.setCellValue("'"+tcs_flag+"'");
//							
//						}
						else if(i==94)
						{
							if(sap_aprv.equals("Y"))
							{
								cell.setCellValue("'S'");
							}
							else
							{
								cell.setCellValue("'"+value+"'");
							}
						}
						
						else
						{
							cell.setCellValue("'"+value+"'");
						}
												
						
					 }
					 count++;
//					 count2++;
					 logger.data(fname, (cd+","+abbr+","+plant_seq_no+","+fin_year+","+contract_type+","+rset2.getString(13)+","), conn, "");
							
				}
				rset2.close();
				stmt2.close();				
			}			
			rset1.close();
			stmt1.close();			
		}
		rset.close();
		stmt.close();	
		
		filename = migration_setup_dir + "EXPORT/FMS_PUR_PG_INV_MST_"+start_end_dt+".xlsx";
		
		fileOut = new FileOutputStream(filename);  
		
		workbook.write(fileOut);
		fileOut.close(); 
		
		logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
		logger.checkpoint(fname, "<<END>>,<<FMS_PUR_PG_INV_MST>>,", conn);
								
		logger.checkpoint1(fname1,count+",", conn);

		System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		System.out.println();

		msg = "Data has been Extracted Successfully.";
		msg_type = "S";
		
	}catch(Exception e) {
		
		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		
	}
}

	
	public void getmail_list() {
		function_nm = "getmail_list";
		
		try
		{
			String strline = "";
			
			File fsetup=new File(migration_setup_dir+"Migration_Setup.txt");
			String mail_list_path=fsetup.getAbsolutePath();
			try (FileInputStream f1 = new FileInputStream(mail_list_path)) {
				try (DataInputStream in = new DataInputStream(f1)) {
					try(BufferedReader br = new BufferedReader(new InputStreamReader(in)))
					{
					while((strline = br.readLine())!=null)
					{
						if(strline.startsWith("dbline"))
						{
							String  tmp[]=strline.split("dbline:");
							dbline = tmp[1].toString();
							
						}
						if(strline.startsWith("username"))
						{
							String  tmp[]=strline.split("username:");
							username = tmp[1].toString();
						}
						if(strline.startsWith("password"))
						{
							String  tmp[]=strline.split("password:");
							 encrypted = tmp[1].toString();
					         password = encrypted;
						}
					}
					}
				}
			}
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void makeDirectory() {
		function_nm = "makeDirectory()";
		
		try {
			String fName[] = {(migration_setup_dir+"EXPORT"), (migration_setup_dir+"DataLogs"), (migration_setup_dir+"DataLogs/RollBack"), (migration_setup_dir+"DataLogs/Extractor"), (migration_setup_dir+"DataLogs/Reader")};
			//String fName_Import = "IMPORT";
			
			for (int i = 0; i < fName.length; i++) {
				File directory = new File(fName[i]);
				
				if(!directory.exists()) {
	
					if(directory.mkdirs()) {
						System.out.println(fName[i]+" Directory Created Successfully.");
						dir_flag = "Y";
					}
					else {
						System.out.println("Failed to create Directory: " + fName[i]);
						dir_flag = "N";
					}
					
				}
				else {
					dir_flag = "Y";
					System.out.println(fName[i]+" Directory Already Exists.");
				}
			}
			
		} 
		catch(Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e); 
		}
	}

	
	public void setChecked_Values(String checked_val) {
		checked_values = checked_val;
	}
	
	public void setDelta_FromDt(String from_dt) {
		delta_FromDt = from_dt;
	}
	
	public void setDelta_ToDt(String to_dt) {
		delta_ToDt = to_dt;
	}
	

	
	public void setStart_End_Dt(String dt) {
		start_end_dt = dt;
	}
	
	public void setSysDateTime(String dt) {
		sysDateTime = dt;
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
	
}

