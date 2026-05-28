
package com.etrm.fms.migration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.SystemErrorLogger;

public class Master_SEIPL_Data_Extractor {

	/*	public static void main(String[] args) {
			Master_Excel_Extractor ex = new Master_Excel_Extractor();
			ex.getmail_list();
			ex.getCustomerTraderList();
			ex.makeDirectory();
			ex.init();
		}
	
	}
	
	class Master_Excel_Extractor {*/

	String db_src_file_name = "Master_SEIPL_Data_Extractor.java";
	String function_nm = "";
	
	String migration_setup_dir = "";
	
	String sysDateTime = "";
	
	//private static final Logger LOGGER = Logger.getLogger(Master_Excel_Extractor.class.getName());
	String fname = "";
	String fname_error = "";
	
	//bellow fname1  is for csv file function start & function end only 
   //logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn); (-E : EXTRACTOR) 
	String fname1 = "";

	DataMigration_Logger logger = new DataMigration_Logger();
	Migration_Plants_Exceptions mpe =new Migration_Plants_Exceptions();

	String query_abbr = "", queryString = "", queryString1 = "";
	Connection conn;
	ResultSet rset_abbr, rset, rset1;
	PreparedStatement stmt_abbr, stmt, stmt1;

	//String customer_delete = "", trader_delete = "", customer_map = "", trader_map = "", transporter_map = "", counterparty_map = "", meter_map= "";
	String dbline = "", username = "", encrypted = "", password = "";
	String columns = "", filename = "", value = "";
	
	String checked_values = "", msg = "", msg_type = "";
	
	String delta_FromDt = null;
	String delta_ToDt = null;
	String start_end_dt = null;
	
	String abbr = "", cd = "", seq_no = "", entity = "", eff_dt = "", address_type = "", tax_code = "", tax_nm = "";
	
	String dir_flag = "N";

	int nrow = 0;
	int count = 0;

	XSSFWorkbook workbook = null;
	XSSFSheet spreadsheet = null;
	XSSFRow row;
	Cell cell;
	
	FileOutputStream fileOut = null;

	public void init() {

        function_nm="init()";
		try {
			
			getmail_list();
//			getCustomerTraderList();
			makeDirectory();
			
			fname = "DataLogs/Extractor/Master_SEIPL_Data_Extractor(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Extractor/Master_SEIPL_Data_Extractor_Error(log)"+sysDateTime+".csv";
			
			fname = migration_setup_dir + fname;
			fname_error = migration_setup_dir + fname_error;
			
			fname1 = "DataLogs/Script_Status(log).csv";	
			fname1 = migration_setup_dir + fname1;

			Preferences preferences =  Preferences.userRoot().node("/processFlag");

			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("FMS8 DBLINE:"+dbline);
			conn = DriverManager.getConnection(dbline, username, password);
			
			
			if (conn != null && dir_flag.equals("Y") && !checked_values.equals("")) {

	    		preferences.put("Flag", "0");
				
				if (checked_values.contains("FMS_COUNTERPARTY_MST")) {
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
				
//				if (checked_values.contains("FMS_PRODUCT_MST")) {
//	    			FMS_PRODUCT_MST();
//				}
				
				if (checked_values.contains("FMS_PRODUCT_MOLECULE_MST")) {
					FMS_PRODUCT_MOLECULE_MST();
				}
				
				if (checked_values.contains("FMS_SAC_MST")) {
	    			FMS_SAC_MST();
				}
				
	    		preferences.put("Flag", "1");
				conn.close();
				conn = null;
			
			}
			else {
				msg = "No Checkbox was selected. Extraction Terminated.";
				msg_type = "E";
			}

		} catch (Exception e) {
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
		} 
		finally {
			try {

				if (rset_abbr != null) {
					try {
						rset_abbr.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (rset != null) {
					try {
						rset.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (rset1 != null) {
					try {
						rset1.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt_abbr != null) {
					try {
						stmt_abbr.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt1 != null) {
					try {
						stmt1.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				
			} catch (Exception e) {
				//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				
				msg = "One of the Functions faced an Error. Extraction Terminated.";
				msg_type = "E";
			}
		}

	}

	public void FMS_COUNTERPARTY_MST() throws SQLException, IOException {
        function_nm="FMS_COUNTERPARTY_MST()";
        
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_MST>>");
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COUNTERPARTY_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_CD,EFF_DT,COUNTERPARTY_NM,COUNTERPARTY_ABBR,PAN_NO,PAN_ISSUE_DT,CATEGORY,WEB_ADDR,NOTES,STATUS,KYC,IGX,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,SAP_CODE,NCF_CATEGORY,ENT_PROFILE,MOD_PROFILE";
			//columns = "COUNTERPARTY_CD,EFF_DT,CATEGORY,WEB_ADDR";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count=0;
			
			String wb="",cg="";
			
			row = spreadsheet.createRow(nrow++);

			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Inserting Rest of the data (COUNTERPARTY)
			queryString = "SELECT COUNTERPARTY_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY'), COUNTERPARTY_NM, COUNTERPARTY_ABBR, PAN_NO, TO_CHAR(PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), KYC_NOTES, DORMANT_FLAG, KYC, IGX, ENT_BY, TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,'2','2'  FROM FMS9_COUNTERPTY_MST WHERE COUNTERPARTY_ABBR != 'SEIPL' AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY COUNTERPARTY_CD, EFF_DT DESC ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_CD,EFF_DT,CATEGORY,WEB_ADDR,TIMESTAMP", conn);
			
			while (rset.next()) {
				cd=rset.getString(1);
				eff_dt=rset.getString(2);
				
				row = spreadsheet.createRow(nrow++);
				String web_addr = null, categ = null;

				// for customer
				queryString1 = "SELECT WEB_ADDR, CATEGORY FROM FMS7_CUSTOMER_MST WHERE COUNTERPARTY_CD = ? AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(1));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				if (rset1.next()) {
					web_addr = rset1.getString(1);
					categ = rset1.getString(2);					
					
				}
				rset1.close();
				stmt1.close();

				// for trader
				queryString1 = "SELECT WEB_ADDR, CATEGORY FROM FMS7_TRADER_MST WHERE COUNTERPARTY_CD = ?  AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(1));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				if (rset1.next()) {
					web_addr = rset1.getString(1);
					categ = rset1.getString(2);
					
				}
				rset1.close();
				stmt1.close();

				// for transporter
				queryString1 = "SELECT WEB_ADDR FROM FMS7_TRANSPORTER_MST WHERE COUNTERPARTY_CD = ?  AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(1));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				if (rset1.next()) {
					web_addr = rset1.getString(1);
					categ = null;
				
				}
				rset1.close();
				stmt1.close();

				cell = row.createCell(0);
				abbr = rset.getString(4).trim();
				value=abbr;
				
//				if(i == 0) {

			    if (mpe.counterparty_map.containsKey(value)) {
			        value =mpe.counterparty_map.get(value); 
			        abbr = value; 
			    }
			    
//				}
				cell.setCellValue("'" + abbr + "&@&" + rset.getString(1) + "'");
				
				cd="'" + abbr + "&@&" + rset.getString(1) + "'";

				cell = row.createCell(1);
				cell.setCellValue("'" + rset.getString(2) + "'");

				cell = row.createCell(2);
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(3);
				cell.setCellValue("'" + abbr + "'");

				cell = row.createCell(4);
				cell.setCellValue("'" + rset.getString(5) + "'");

				cell = row.createCell(5);
				cell.setCellValue("'" + rset.getString(6) + "'");

				cell = row.createCell(6);
				cell.setCellValue("'" + categ + "'");
				cg=categ;
				
				cell = row.createCell(7);
				cell.setCellValue("'" + web_addr + "'");
				wb="'" + web_addr + "'";
				

				cell = row.createCell(8);
				cell.setCellValue("'" + rset.getString(7) + "'");

				cell = row.createCell(9);
				cell.setCellValue("'"+ ((rset.getString(8) != null && rset.getString(8).equalsIgnoreCase("Y")) ? "N" : "Y") + "'");

				cell = row.createCell(10);
				cell.setCellValue("'" + rset.getString(9) + "'");

				cell = row.createCell(11);
				cell.setCellValue("'" + rset.getString(10) + "'");
				
				cell = row.createCell(12);
				// value = getEmpAbbr(rset.getString(11));
				cell.setCellValue("'" + rset.getString(11) + "'");

				cell = row.createCell(13);
				cell.setCellValue("'" + rset.getString(12) + "'");

				cell = row.createCell(14);
				// value = getEmpAbbr(rset.getString(13));
				cell.setCellValue("'" + rset.getString(13) + "'");

				cell = row.createCell(15);
				cell.setCellValue("'" + rset.getString(14) + "'");
				
				cell = row.createCell(16);
				cell.setCellValue("'" + rset.getString(15) + "'");
				
				cell = row.createCell(17);
				cell.setCellValue("'" + rset.getString(16) + "'");
				
				cell = row.createCell(18);
				cell.setCellValue("'" + rset.getString(17) + "'");
				
				cell = row.createCell(19);
				cell.setCellValue("'" + rset.getString(18) + "'");
				

				if (rset.getString(8) != null && rset.getString(8).equalsIgnoreCase("Y")) 
				{ // Inserting two entrieswith different Eff_Dtin-case of Inactive Counterparty
					queryString1 = "SELECT TO_CHAR(DORMANT_EFF_DT, 'DD/MM/YYYY') FROM FMS9_COUNTERPTY_DORMANT_DTL WHERE ENTITY_CD = ? AND ENTITY_FLAG = 'M' AND DORMANT_FLAG = 'Y' ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, rset.getString(1));
					rset1 = stmt1.executeQuery();
					
					if (rset1.next()) {

						row = spreadsheet.createRow(nrow++);
						

						cell = row.createCell(0);
						abbr = rset.getString(4).trim();
						value=abbr;
						
//						if(i == 0) {

					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        abbr = value; 
					    }
					    
//						}
						cell.setCellValue("'" + abbr + "&@&" + rset.getString(1) + "'");
						
						cd="'" + abbr + "&@&" + rset.getString(1) + "'";

//						cell = row.createCell(0);
//						cell.setCellValue("'" + rset.getString(4) + "&@&" + rset.getString(1) + "'");
//						cd="'" + rset.getString(4) + "&@&" + rset.getString(1) + "'";

						cell = row.createCell(1);
						cell.setCellValue("'" + rset1.getString(1) + "'");
						eff_dt=rset1.getString(1);

						cell = row.createCell(2);
						cell.setCellValue("'" + rset.getString(3) + "'");

						cell = row.createCell(3);
						cell.setCellValue("'" + abbr + "'");

						cell = row.createCell(4);
						cell.setCellValue("'" + rset.getString(5) + "'");

						cell = row.createCell(5);
						cell.setCellValue("'" + rset.getString(6) + "'");

						cell = row.createCell(6);
						cell.setCellValue("'" + categ + "'");
						cg=categ;

						cell = row.createCell(7);
						cell.setCellValue("'" + web_addr + "'");
						wb="'" + web_addr + "'";

						cell = row.createCell(8);
						cell.setCellValue("'" + rset.getString(7) + "'");

						cell = row.createCell(9);
						cell.setCellValue("'"+ ((rset.getString(8) != null && rset.getString(8).equalsIgnoreCase("Y")) ? "N" : "Y")+ "'");

						cell = row.createCell(10);
						cell.setCellValue("'" + rset.getString(9) + "'");

						cell = row.createCell(11);
						cell.setCellValue("'" + rset.getString(10) + "'");
						
						cell = row.createCell(12);
						// value = getEmpAbbr(rset.getString(11));
						cell.setCellValue("'" + rset.getString(11) + "'");

						cell = row.createCell(13);
						cell.setCellValue("'" + rset.getString(12) + "'");

						cell = row.createCell(14);
						// value = getEmpAbbr(rset.getString(13));
						cell.setCellValue("'" + rset.getString(13) + "'");

						cell = row.createCell(15);
						cell.setCellValue("'" + rset.getString(14) + "'");
						
						cell = row.createCell(16);
						cell.setCellValue("'" + rset.getString(15) + "'");
						
						cell = row.createCell(17);
						cell.setCellValue("'" + rset.getString(16) + "'");
						
						cell = row.createCell(18);
						cell.setCellValue("'" + rset.getString(17) + "'");
						
						cell = row.createCell(19);
						cell.setCellValue("'" + rset.getString(18) + "'");
						
					

						count++;
						if( wb.contains(","))
						{
							wb=wb.replaceAll(","," ");
						}
						logger.data(fname, (cd +","+eff_dt+ " , " + cg + " , " + wb + " , "), conn, "");
		
					}
					rset1.close();
					stmt1.close();
					
				}
				
				count++;
				if( wb.contains(","))
				{
					wb=wb.replaceAll(","," ");
				}
				logger.data(fname, (cd +","+eff_dt+ " , " + cg + " , " + wb + " , "), conn, "");	

			}
			
			stmt.close();
			rset.close();

			// Inserting Rest of the data (CHA)
			queryString = "SELECT CHA_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY'), CHA_NAME, CHA_ABBR, PAN_NO, TO_CHAR(PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'),WEB_ADDR, NOTES, 'N', NULL, NULL, EMP_CD, TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, NULL, '2', '2' FROM FMS7_CUS_HOUSE_AGENT_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CHA_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {

				cd=rset.getString(1);
				eff_dt=rset.getString(2);
				
				row = spreadsheet.createRow(nrow++);

				abbr = rset.getString(4).trim();
				value=abbr;
				
//				if(i == 0) {

				 if (mpe.counterparty_map.containsKey(value)) {
				        value =mpe.counterparty_map.get(value); 
				        abbr = value; 
				    }
				
//				}
				
				cell = row.createCell(0);
				cell.setCellValue("'" + abbr + "&@&" + rset.getString(1) + "'");
				cd="'" + abbr + "&@&" + rset.getString(1) + "'";
				
				cell = row.createCell(1);
				cell.setCellValue("'" + rset.getString(2) + "'");
				eff_dt=rset.getString(2);

				cell = row.createCell(2);
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(3);
				cell.setCellValue("'" + abbr + "'");

				cell = row.createCell(4);
				cell.setCellValue("'" + rset.getString(5) + "'");

				cell = row.createCell(5);
				cell.setCellValue("'" + rset.getString(6) + "'");

				cell = row.createCell(6);
				cell.setCellValue("'null'");
				cg="null";

				cell = row.createCell(7);
				cell.setCellValue("'" + rset.getString(7) + "'");
				wb="'" + rset.getString(7) + "'";

				cell = row.createCell(8);
				cell.setCellValue("'" + rset.getString(8) + "'");

				cell = row.createCell(9);
				cell.setCellValue("'" + (rset.getString(9).equals("Y") ? "N" : "Y") + "'");

				cell = row.createCell(10);
				cell.setCellValue("'" + rset.getString(10) + "'");

				cell = row.createCell(11);
				cell.setCellValue("'" + rset.getString(11) + "'");			
				
				cell = row.createCell(12);
				// value = getEmpAbbr(rset.getString(11));
				cell.setCellValue("'" + rset.getString(12) + "'");

				cell = row.createCell(13);
				cell.setCellValue("'" + rset.getString(13) + "'");

				cell = row.createCell(14);
				cell.setCellValue("'" + rset.getString(14) + "'");

				cell = row.createCell(15);
				cell.setCellValue("'" + rset.getString(15) + "'");
					
				cell = row.createCell(16);
				cell.setCellValue("'" + rset.getString(16) + "'");
				
				cell = row.createCell(17);
				cell.setCellValue("'" + rset.getString(17) + "'");
				
				cell = row.createCell(18);
				cell.setCellValue("'" + rset.getString(18) + "'");
				
				cell = row.createCell(19);
				cell.setCellValue("'" + rset.getString(19) + "'");
				

				count++;
				if( wb.contains(","))
				{
					wb=wb.replaceAll(","," ");
				}
				
				logger.data(fname, (cd +","+eff_dt+ " , " + cg + " , " + wb + " , "), conn, "");
			}
			
			stmt.close();
			rset.close();

			// Inserting Rest of the data (VESSEL AGENT)
			queryString = "SELECT VESSEL_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY'), VESSEL_NAME, VESSEL_ABBR, PAN_NO, TO_CHAR(PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'),WEB_ADDR, NOTES, 'N', NULL, NULL, EMP_CD, TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, NULL, '2', '2' FROM FMS7_VESSEL_AGENT_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY VESSEL_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {

				row = spreadsheet.createRow(nrow++);

				abbr = rset.getString(4).trim();
				value=abbr;
//				if(i == 0) {
				 if (mpe.counterparty_map.containsKey(value)) {
				        value =mpe.counterparty_map.get(value); 
				        abbr = value; 
				    }
//				}
				
					cell = row.createCell(0);
					cell.setCellValue("'" + abbr + "&@&" + rset.getString(1) + "'");
					cd="'" + abbr + "&@&" + rset.getString(1) + "'";
					
					cell = row.createCell(1);
					cell.setCellValue("'" + rset.getString(2) + "'");
					eff_dt=rset.getString(2);

					cell = row.createCell(2);
					cell.setCellValue("'" + rset.getString(3) + "'");

					cell = row.createCell(3);
					cell.setCellValue("'" + abbr + "'");

					cell = row.createCell(4);
					cell.setCellValue("'" + rset.getString(5) + "'");

					cell = row.createCell(5);
					cell.setCellValue("'" + rset.getString(6) + "'");

					cell = row.createCell(6);
					cell.setCellValue("'null'");
					cg="null";

					cell = row.createCell(7);
					cell.setCellValue("'" + rset.getString(7) + "'");
					wb="'" + rset.getString(7) + "'";

					cell = row.createCell(8);
					cell.setCellValue("'" + rset.getString(8) + "'");

					cell = row.createCell(9);
					cell.setCellValue("'" + (rset.getString(9).equals("Y") ? "N" : "Y") + "'");

					cell = row.createCell(10);
					cell.setCellValue("'" + rset.getString(10) + "'");

					cell = row.createCell(11);
					cell.setCellValue("'" + rset.getString(11) + "'");			
					
					cell = row.createCell(12);
					// value = getEmpAbbr(rset.getString(11));
					cell.setCellValue("'" + rset.getString(12) + "'");

					cell = row.createCell(13);
					cell.setCellValue("'" + rset.getString(13) + "'");

					cell = row.createCell(14);
					cell.setCellValue("'" + rset.getString(14) + "'");

					cell = row.createCell(15);
					cell.setCellValue("'" + rset.getString(15) + "'");
						
					cell = row.createCell(16);
					cell.setCellValue("'" + rset.getString(16) + "'");
					
					cell = row.createCell(17);
					cell.setCellValue("'" + rset.getString(17) + "'");
					
					cell = row.createCell(18);
					cell.setCellValue("'" + rset.getString(18) + "'");
					
					cell = row.createCell(19);
					cell.setCellValue("'" + rset.getString(19) + "'");
				
				
				count++;
				if( wb.contains(","))
				{
					wb=wb.replaceAll(","," ");
				}
				
				logger.data(fname, (cd +","+eff_dt+ " , " + cg + " , " + wb + " , "), conn, "");
			}
			
			stmt.close();
			rset.close();

			// Inserting Rest of the data (SURVEYOR)
			queryString = "SELECT SURVEYOR_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY'), SURVEYOR_NAME, SURVEYOR_ABBR, PAN_NO, TO_CHAR(PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'),WEB_ADDR, NOTES, 'N', NULL, NULL, EMP_CD, TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, NULL, '2', '2' FROM FMS7_SURVEYOR_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY SURVEYOR_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {

				row = spreadsheet.createRow(nrow++);


				abbr = rset.getString(4).trim();
				value=abbr;
//				if(i == 0) {
				 if (mpe.counterparty_map.containsKey(value)) {
				        value =mpe.counterparty_map.get(value); 
				        abbr = value; 
				    }
//				}
				
					cell = row.createCell(0);
					cell.setCellValue("'" + abbr + "&@&" + rset.getString(1) + "'");
					cd="'" + abbr + "&@&" + rset.getString(1) + "'";
					
					cell = row.createCell(1);
					cell.setCellValue("'" + rset.getString(2) + "'");
					eff_dt=rset.getString(2);

					cell = row.createCell(2);
					cell.setCellValue("'" + rset.getString(3) + "'");

					cell = row.createCell(3);
					cell.setCellValue("'" + abbr + "'");

					cell = row.createCell(4);
					cell.setCellValue("'" + rset.getString(5) + "'");

					cell = row.createCell(5);
					cell.setCellValue("'" + rset.getString(6) + "'");

					cell = row.createCell(6);
					cell.setCellValue("'null'");
					cg="null";

					cell = row.createCell(7);
					cell.setCellValue("'" + rset.getString(7) + "'");
					wb="'" + rset.getString(7) + "'";

					cell = row.createCell(8);
					cell.setCellValue("'" + rset.getString(8) + "'");

					cell = row.createCell(9);
					cell.setCellValue("'" + (rset.getString(9).equals("Y") ? "N" : "Y") + "'");

					cell = row.createCell(10);
					cell.setCellValue("'" + rset.getString(10) + "'");

					cell = row.createCell(11);
					cell.setCellValue("'" + rset.getString(11) + "'");			
					
					cell = row.createCell(12);
					// value = getEmpAbbr(rset.getString(11));
					cell.setCellValue("'" + rset.getString(12) + "'");

					cell = row.createCell(13);
					cell.setCellValue("'" + rset.getString(13) + "'");

					cell = row.createCell(14);
					cell.setCellValue("'" + rset.getString(14) + "'");

					cell = row.createCell(15);
					cell.setCellValue("'" + rset.getString(15) + "'");
						
					cell = row.createCell(16);
					cell.setCellValue("'" + rset.getString(16) + "'");
					
					cell = row.createCell(17);
					cell.setCellValue("'" + rset.getString(17) + "'");
					
					cell = row.createCell(18);
					cell.setCellValue("'" + rset.getString(18) + "'");
					
					cell = row.createCell(19);
					cell.setCellValue("'" + rset.getString(19) + "'");
				


				count++;
				if( wb.contains(","))
				{
					wb=wb.replaceAll( "," ," ");
				}
				logger.data(fname, (cd +","+eff_dt+ " , " + cg + " , " + wb + " , "), conn, "");
			}

			
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_MST>>,,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

	}

	public void FMS_COUNTERPARTY_ADDR_MST() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_ADDR_MST()";
		
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_ADDR_MST>>");
			logger.checkpoint(fname, "\n<<START>>,<<FMS_COUNTERPARTY_ADDR_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ADDRESS_TYPE,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count=0;
			String addr_type="";

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data (COUNTERPARTY)
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), B.COUNTERPARTY_CD, B.ADDRESS_TYPE, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), B.MODIFY_BY, TO_CHAR(B.MODIFY_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EFF_DT FROM FMS9_COUNTERPTY_MST A, FMS9_COUNTERPTY_ADD_MST B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND A.COUNTERPARTY_ABBR != 'SEIPL' AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_ADD_MST C WHERE B.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ADDRESS_TYPE,EFF_DT,TIMESTAMP", conn);
			
			while (rset.next()) 
			{

				abbr=rset.getString(1).trim();

			    if (mpe.counterparty_map.containsKey(abbr)) {
			        abbr =mpe.counterparty_map.get(abbr); 
			    }
			    
				cd=rset.getString(2);
				addr_type=rset.getString(3);
				eff_dt=rset.getString(4);
				
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					/*
					 * if ((i == 16 || i == 18) && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					if(i == 0) {

						 value = abbr;
					}
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + " , " + cd + " , " + addr_type + " , " + eff_dt + " , "), conn, "");

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (CHA)
			queryString = "SELECT DISTINCT(A.CHA_ABBR), B.CHA_CD, B.ADDRESS_TYPE, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_CUS_HOUSE_AGENT_MST A, FMS7_CUS_HOUSE_AGENT_ADDR_MST B WHERE A.CHA_CD = B.CHA_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUS_HOUSE_AGENT_ADDR_MST C WHERE B.CHA_CD = C.CHA_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.CHA_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {
				

				abbr=rset.getString(1).trim();

			    if (mpe.counterparty_map.containsKey(abbr)) {
			        abbr =mpe.counterparty_map.get(abbr); 
			    }
			    
				cd=rset.getString(2);
				addr_type=rset.getString(3);
				eff_dt=rset.getString(4);
				
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					/*
					 * if (i == 16 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					if(i == 0) {
						value = abbr;
					}
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + " , " + cd + " , " + addr_type + " , " + eff_dt + " , "), conn, "");

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (VESSEL AGENT)
			queryString = "SELECT DISTINCT(A.VESSEL_ABBR), B.VESSEL_CD, B.ADDRESS_TYPE, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_VESSEL_AGENT_MST A, FMS7_VESSEL_AGENT_ADDR_MST B WHERE A.VESSEL_CD = B.VESSEL_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_VESSEL_AGENT_ADDR_MST C WHERE B.VESSEL_CD = C.VESSEL_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.VESSEL_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {


				abbr=rset.getString(1).trim();

			    if (mpe.counterparty_map.containsKey(abbr)) {
			        abbr =mpe.counterparty_map.get(abbr); 
			    }
			    
				cd=rset.getString(2);
				addr_type=rset.getString(3);
				eff_dt=rset.getString(4);
				
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					
					/*
					 * if (i == 16 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					if(i == 0) {

						value = abbr;
					}
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + " , " + cd + " , " + addr_type + " , " + eff_dt + " , "), conn, "");
			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (SURVEYOR)
			queryString = "SELECT DISTINCT(A.SURVEYOR_ABBR), B.SURVEYOR_CD, B.ADDRESS_TYPE, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_SURVEYOR_MST A, FMS7_SURVEYOR_ADDRESS_MST B WHERE A.SURVEYOR_CD = B.SURVEYOR_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_SURVEYOR_ADDRESS_MST C WHERE B.SURVEYOR_CD = C.SURVEYOR_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.SURVEYOR_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {


				abbr=rset.getString(1).trim();

			    if (mpe.counterparty_map.containsKey(abbr)) {
			        abbr =mpe.counterparty_map.get(abbr); 
			    }
			    
				cd=rset.getString(2);
				addr_type=rset.getString(3);
				eff_dt=rset.getString(4);
				
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					
					/*
					 * if (i == 16 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					if(i == 0) {
						value = abbr;
					}
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + " , " + cd + " , " + addr_type + " , " + eff_dt + " , "), conn, "");

			}
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_ADDR_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_ADDR_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_ADDR_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_ADDR_MST()  ",e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}

	}


	public void FMS_ENTITY_REQ_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_REQ_DTL()";
		
		try {

			System.out.println("<<START>><<FMS_ENTITY_REQ_DTL>>");
			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_REQ_DTL>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,SEQ_NO,ENTITY,REMARK,APRV_NOTE,STATUS,REQ_BY,REQ_DT,APRV_BY,APRV_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			

			String RIL_cd = "1";
			int seq = 1;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data (COUNTERPARTY)
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND A.COUNTERPARTY_ABBR != 'SEIPL' AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			int seq_n = 0;
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,SEQ_NO,ENTITY,TIMESTAMP", conn);
			
			while (rset.next()) {

				seq = 1;
				seq_n = seq;
				abbr = rset.getString(1).trim();
				cd = rset.getString(2);
				
				// CUSTOMER
				queryString1 = "SELECT CUSTOMER_CD, EMP_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_CUSTOMER_MST WHERE COUNTERPARTY_CD = ? AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY EFF_DT ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				if (rset1.next()) {

					row = spreadsheet.createRow(nrow++);

					cell = row.createCell(0);
					value = abbr;
//					if(i == 0) {
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value);
					        abbr = value; 
					    }
//					}
					cell.setCellValue("'" + value + "'");

					cell = row.createCell(1);
					cell.setCellValue("'" + rset.getString(2) + "'");

					seq_n = seq;
					cell = row.createCell(2);
					cell.setCellValue("'" + (seq++) + "'");

					cell = row.createCell(3);
					cell.setCellValue("'C'");
					entity = "C";

					cell = row.createCell(4);
					cell.setCellValue("'Requested (SEIPL)'");

					cell = row.createCell(5);
					cell.setCellValue("'Approved (SEIPL)'");

					cell = row.createCell(6);
					cell.setCellValue("'A'");

					cell = row.createCell(7);
					// value = getEmpAbbr(rset1.getString(2));
					cell.setCellValue("'" + rset1.getString(2) + "'");

					cell = row.createCell(8);
					cell.setCellValue("'" + rset1.getString(3) + "'");

					cell = row.createCell(9);
					// value = getEmpAbbr(rset1.getString(2));
					cell.setCellValue("'" + rset1.getString(2) + "'");

					cell = row.createCell(10);
					cell.setCellValue("'" + rset1.getString(3) + "'");
					count++;
					logger.data(fname, (abbr + " , " + cd + " , " + seq_n + " , " + entity + " , "), conn, "");

				}
				rset1.close();
				stmt1.close();

				// TRADER
				queryString1 = "SELECT TRADER_CD, EMP_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_TRADER_MST WHERE COUNTERPARTY_CD = ? AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ORDER BY EFF_DT";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				if (rset1.next()) {
					
					if(abbr.equals("RIL")) {
						RIL_cd = rset.getString(2);
					}
					
					if (!abbr.equals("RIL")) {
						
						row = spreadsheet.createRow(nrow++);
	
						//cell = row.createCell(0);
						//cell.setCellValue("'" + rset.getString(1) + "'");
						cell = row.createCell(0);
						if (abbr.contains("RIL")) {
							cell.setCellValue("'RIL'");
						}
						else {
//							if(i == 0) {
							value = abbr;
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value);
							        abbr = value; 
							    }
//							}
							cell.setCellValue("'" + value + "'");
						}
	
						//cell = row.createCell(1);
						//cell.setCellValue("'" + rset.getString(2) + "'");
						cell = row.createCell(1);
						if (abbr.contains("RIL")) {
							cell.setCellValue("'" + RIL_cd + "'");
						}
						else {
							cell.setCellValue("'" + rset.getString(2) + "'");
						}
	
						if (abbr.contains("RIL")) {
							seq = 2;
						}
						seq_n = seq;
						cell = row.createCell(2);
						cell.setCellValue("'" + (seq++) + "'");
	
						cell = row.createCell(3);
						cell.setCellValue("'T'");
						entity = "T";
	
						cell = row.createCell(4);
						cell.setCellValue("'Requested (SEIPL)'");
	
						cell = row.createCell(5);
						cell.setCellValue("'Approved (SEIPL)'");
	
						cell = row.createCell(6);
						cell.setCellValue("'A'");
	
						cell = row.createCell(7);
						// value = getEmpAbbr(rset1.getString(2));
						cell.setCellValue("'" + rset1.getString(2) + "'");
	
						cell = row.createCell(8);
						cell.setCellValue("'" + rset1.getString(3) + "'");
	
						cell = row.createCell(9);
						// value = getEmpAbbr(rset1.getString(2));
						cell.setCellValue("'" + rset1.getString(2) + "'");
	
						cell = row.createCell(10);
						cell.setCellValue("'" + rset1.getString(3) + "'");
						count++;
						logger.data(fname, (abbr + " , " + cd + " , " + seq_n + " , " + entity + " , "), conn, "");
						
					}

				}
				rset1.close();
				stmt1.close();

				// TRANSPORTER
				queryString1 = "SELECT TRANSPORTER_CD, EMP_CD, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_TRANSPORTER_MST WHERE COUNTERPARTY_CD = ? AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ORDER BY EFF_DT";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
//				if (rset1.next() && !transporter_map.contains(abbr+";") && !abbr.toUpperCase().contains("PARK")) {
				if (rset1.next() && !mpe.transporter_map.containsKey(abbr) && !abbr.toUpperCase().contains("PARK")) {
					row = spreadsheet.createRow(nrow++);

					cell = row.createCell(0);
//					if(i == 0) {
					value = abbr;
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value);
					        abbr = value; 
					    }
//					}
					cell.setCellValue("'" + value + "'");

					cell = row.createCell(1);
					cell.setCellValue("'" + rset.getString(2) + "'");
					
					seq_n = seq;
					cell = row.createCell(2);
					cell.setCellValue("'" + (seq++) + "'");

					cell = row.createCell(3);
					cell.setCellValue("'R'");
					entity = "R";

					cell = row.createCell(4);
					cell.setCellValue("'Requested (SEIPL)'");

					cell = row.createCell(5);
					cell.setCellValue("'Approved (SEIPL)'");

					cell = row.createCell(6);
					cell.setCellValue("'A'");

					cell = row.createCell(7);
					// value = getEmpAbbr(rset1.getString(2));
					cell.setCellValue("'" + rset1.getString(2) + "'");

					cell = row.createCell(8);
					cell.setCellValue("'" + rset1.getString(3) + "'");

					cell = row.createCell(9);
					// value = getEmpAbbr(rset1.getString(2));
					cell.setCellValue("'" + rset1.getString(2) + "'");

					cell = row.createCell(10);
					cell.setCellValue("'" + rset1.getString(3) + "'");
					count++;
					logger.data(fname, (abbr + " , " + cd + " , " + seq_n + " , " + entity + " , "), conn, "");

				}
				rset1.close();
				stmt1.close();

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (CHA)
			queryString = "SELECT DISTINCT(A.CHA_ABBR), A.CHA_CD, A.EMP_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_CUS_HOUSE_AGENT_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUS_HOUSE_AGENT_MST C WHERE A.CHA_CD = C.CHA_CD)  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.CHA_CD";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {
				row = spreadsheet.createRow(nrow++);

				cell = row.createCell(0);
//				if(i == 0) {
				value = rset.getString(1).trim();
//					for (int c = 0; c < counterparty_map.split(",").length; c++){
//						if (counterparty_map.split(",")[c].contains(value+";")){
//							value = counterparty_map.split(",")[c].split(";")[1];
//							abbr = value;
//						}
//					}
				 if (mpe.counterparty_map.containsKey(value)) {
				        value =mpe.counterparty_map.get(value);
				        abbr = value; 
				    }
//				}
				cell.setCellValue("'" + value + "'");

				cell = row.createCell(1);
				cell.setCellValue("'" + rset.getString(2) + "'");

				cell = row.createCell(2);
				cell.setCellValue("'1'");
				seq_n = 1;

				cell = row.createCell(3);
				cell.setCellValue("'H'");
				entity = "H";

				cell = row.createCell(4);
				cell.setCellValue("'Requested (SEIPL)'");

				cell = row.createCell(5);
				cell.setCellValue("'Approved (SEIPL)'");

				cell = row.createCell(6);
				cell.setCellValue("'A'");

				cell = row.createCell(7);
				// value = getEmpAbbr(rset.getString(3));
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(8);
				cell.setCellValue("'" + rset.getString(4) + "'");

				cell = row.createCell(9);
				// value = getEmpAbbr(rset.getString(3));
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(10);
				cell.setCellValue("'" + rset.getString(4) + "'");

				count++;
				logger.data(fname, (abbr + " , " + cd + " , " + seq_n + " , " + entity + " , "), conn, "");
			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (VESSEL AGENT)
			queryString = "SELECT DISTINCT(A.VESSEL_ABBR), A.VESSEL_CD, A.EMP_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_VESSEL_AGENT_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_VESSEL_AGENT_MST C WHERE A.VESSEL_CD = C.VESSEL_CD)  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.VESSEL_CD";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {
				row = spreadsheet.createRow(nrow++);

				cell = row.createCell(0);
//				if(i == 0) {
				value = rset.getString(1).trim();
//					for (int c = 0; c < counterparty_map.split(",").length; c++){
//						if (counterparty_map.split(",")[c].contains(value+";")){
//							value = counterparty_map.split(",")[c].split(";")[1];
//							abbr = value;
//						}
//					}
				 if (mpe.counterparty_map.containsKey(value)) {
				        value =mpe.counterparty_map.get(value); 
				        abbr = value; 
				    }
//				}
				cell.setCellValue("'" + value + "'");

				cell = row.createCell(1);
				cell.setCellValue("'" + rset.getString(2) + "'");
				seq_n = 1;
				cell = row.createCell(2);
				cell.setCellValue("'1'");

				cell = row.createCell(3);
				cell.setCellValue("'V'");
				entity = "V";
				cell = row.createCell(4);
				cell.setCellValue("'Requested (SEIPL)'");

				cell = row.createCell(5);
				cell.setCellValue("'Approved (SEIPL)'");

				cell = row.createCell(6);
				cell.setCellValue("'A'");

				cell = row.createCell(7);
				// value = getEmpAbbr(rset.getString(3));
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(8);
				cell.setCellValue("'" + rset.getString(4) + "'");

				cell = row.createCell(9);
				// value = getEmpAbbr(rset.getString(3));
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(10);
				cell.setCellValue("'" + rset.getString(4) + "'");
				count++;
				
				logger.data(fname, (abbr + " , " + cd + " , " + seq_n + " , " + entity + " , "), conn, "");
			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (SURVEYOR)
			queryString = "SELECT DISTINCT(A.SURVEYOR_ABBR), A.SURVEYOR_CD, A.EMP_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_SURVEYOR_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_SURVEYOR_MST C WHERE A.SURVEYOR_CD = C.SURVEYOR_CD)  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.SURVEYOR_CD";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {
				row = spreadsheet.createRow(nrow++);

				cell = row.createCell(0);
//				if(i == 0) {
				value = rset.getString(1).trim();
//					for (int c = 0; c < counterparty_map.split(",").length; c++){
//						if (counterparty_map.split(",")[c].contains(value+";")){
//							value = counterparty_map.split(",")[c].split(";")[1];
//							abbr = value;
//						}
//					}
				 if (mpe.counterparty_map.containsKey(value)) {
				        value =mpe.counterparty_map.get(value); 
				        abbr = value; 
				    }
//				}
				cell.setCellValue("'" + value + "'");

				cell = row.createCell(1);
				cell.setCellValue("'" + rset.getString(2) + "'");

				cell = row.createCell(2);
				if (value.equalsIgnoreCase("JBB")) {
					cell.setCellValue("'2'");
					seq_n = 2;
				}
				else {
					cell.setCellValue("'1'");
					seq_n = 1;
//					System.out.println(value);
				}
				
				cell = row.createCell(3);
				cell.setCellValue("'S'");
				
				entity = "S";
				cell = row.createCell(4);
				cell.setCellValue("'Requested (SEIPL)'");

				cell = row.createCell(5);
				cell.setCellValue("'Approved (SEIPL)'");

				cell = row.createCell(6);
				cell.setCellValue("'A'");

				cell = row.createCell(7);
				// value = getEmpAbbr(rset.getString(3));
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(8);
				cell.setCellValue("'" + rset.getString(4) + "'");

				cell = row.createCell(9);
				// value = getEmpAbbr(rset.getString(3));
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(10);
				cell.setCellValue("'" + rset.getString(4) + "'");
				count++;
				
				logger.data(fname, (abbr + " , " + cd + " , " + seq_n + " , " + entity + " , "), conn, "");
			}
			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_ENTITY_REQ_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " , , ,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_REQ_DTL>>,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_ENTITY_REQ_DTL>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_REQ_DTL()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_ADDR_MST() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_ADDR_MST()";
		
		try {

			System.out.println("<<START>><<FMS_ENTITY_ADDR_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_ADDR_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ADDRESS_TYPE,ENTITY,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;

			String RIL_cd = "1";
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data (COUNTERPARTY)
			queryString = "SELECT DISTINCT(B.COUNTERPARTY_ABBR), B.COUNTERPARTY_CD, B.EFF_DT  FROM FMS9_COUNTERPTY_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE B.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND B.COUNTERPARTY_ABBR != 'SEIPL' AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.COUNTERPARTY_CD, B.EFF_DT DESC   ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ADRESS_TYPE,ENTITY,EFF_DATE,TIMESTAMP", conn);
			while (rset.next()) {
				
				String entity_cd = "";
				abbr = rset.getString(1).trim();
				cd = rset.getString(2);
				
				// CUSTOMER
				queryString1 = "SELECT B.CUSTOMER_CD, B.ADDRESS_TYPE, 'C', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_CUSTOMER_ADDRESS_MST B WHERE  B.CUSTOMER_CD = (SELECT DISTINCT(D.CUSTOMER_CD) FROM FMS7_CUSTOMER_MST D WHERE B.CUSTOMER_CD = D.CUSTOMER_CD AND D.COUNTERPARTY_CD = ? )  AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.CUSTOMER_CD, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY')  ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					row = spreadsheet.createRow(nrow++);
					eff_dt = rset1.getString(4);
					entity = rset1.getString(3);
					address_type = rset1.getString(2);

					value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
					cell = row.createCell(0);
//					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        abbr = value; 
					    }
//					}
					cell.setCellValue("'" + value + "'");

					value = rset.getString(2) == null ? "null" : rset.getString(2).replace("'", "");
					cell = row.createCell(1);
					cell.setCellValue("'" + value + "'");

					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
						/*
						 * if (i == 17 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); }
						 */
						cell.setCellValue("'" + value + "'");
					}
					count++;
					
					logger.data(fname,(abbr + " , " + cd + " , " + address_type + " , " + entity + " , " + eff_dt + " , "), conn, "");
					entity_cd = rset1.getString(1);

				}
				stmt1.close();
				rset1.close();

				// TRADER
				queryString1 = "SELECT B.TRADER_CD, B.ADDRESS_TYPE, 'T', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_TRADER_ADDRESS_MST B WHERE  B.TRADER_CD = (SELECT DISTINCT(D.TRADER_CD) FROM FMS7_TRADER_MST D WHERE B.TRADER_CD = D.TRADER_CD AND D.COUNTERPARTY_CD = ? )  AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.TRADER_CD, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					if(abbr.equals("RIL")) {
						RIL_cd = rset.getString(2);
					}
					
					if (!abbr.equals("RIL")) {
						
						row = spreadsheet.createRow(nrow++);
						// fetch value for logger:
						eff_dt = rset1.getString(4);
						entity = rset1.getString(3);
						
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
						
//						cell = row.createCell(0);
//						cell.setCellValue("'" + value + "'");
						cell = row.createCell(0);
						if (abbr.contains("RIL")) {
							cell.setCellValue("'RIL'");
						}
						else {
//						if(i == 0) {
//							for (int c = 0; c < counterparty_map.split(",").length; c++){
//								if (counterparty_map.split(",")[c].contains(value+";")){
//									value = counterparty_map.split(",")[c].split(";")[1];
//									abbr = value;
//								}
//							}
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value);
							        abbr = value; 
							    }
//						}
							cell.setCellValue("'" + value + "'");
						}
	
						value = rset.getString(2) == null ? "null" : rset.getString(2).replace("'", "");
						cell = row.createCell(1);
//						cell.setCellValue("'" + value + "'");
						if (abbr.contains("RIL")) {
							cell.setCellValue("'" + RIL_cd + "'");
						}
						else {
							cell.setCellValue("'" + value + "'");
						}
	
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
							/*
							 * if (i == 17 && !value.equals("null")) { // emp_abbr value =
							 * getEmpAbbr(value); }
							 */
							cell.setCellValue("'" + value + "'");
						}
						count++;
						
						logger.data(fname, (abbr + " , " + cd + " , " + address_type + " , " + entity + " , " + eff_dt + " , "), conn, "");
						entity_cd = rset1.getString(1);
					}
				
				}
				stmt1.close();
				rset1.close();

				// TRANSPORTER
				queryString1 = "SELECT B.TRANSPORTER_CD, B.ADDRESS_TYPE, 'R', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_TRANSPORTER_ADDRESS_MST B WHERE B.TRANSPORTER_CD = (SELECT DISTINCT(D.TRANSPORTER_CD) FROM FMS7_TRANSPORTER_MST D WHERE B.TRANSPORTER_CD = D.TRANSPORTER_CD AND D.COUNTERPARTY_CD = ? )  AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.TRANSPORTER_CD, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY')  ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					row = spreadsheet.createRow(nrow++);
					eff_dt = rset1.getString(4);
					entity = rset1.getString(3);

					value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
					cell = row.createCell(0);
//					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        abbr = value; 
					    }
//					}
					cell.setCellValue("'" + value + "'");

					value = rset.getString(2) == null ? "null" : rset.getString(2).replace("'", "");
					cell = row.createCell(1);
					cell.setCellValue("'" + value + "'");

					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
						/*
						 * if (i == 17 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); }
						 */
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (abbr + " , " + cd + " , " + address_type + " , " + entity + " , " + eff_dt + " , "), conn, "");
					entity_cd = rset1.getString(1);

				}
				stmt1.close();
				rset1.close();
			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_ENTITY_ADDR_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " , , , ,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_ADDR_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_ENTITY_ADDR_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_ADDR_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COMPANY_OWNER_MST() throws SQLException, IOException {
		function_nm = "FMS_COMPANY_OWNER_MST()";
		
		try {

			System.out.println("<<START>><<FMS_COMPANY_OWNER_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_COMPANY_OWNER_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,EFF_DT,COMPANY_NM,COMPANY_ABBR,PAN_NO,PAN_ISSUE_DT,NOTES,STATUS,CATEGORY,WEB_ADDR,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,INVOICE_PREFIX";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = "SELECT '2', TO_CHAR(EFF_DT, 'DD/MM/YYYY'), SUPPLIER_NAME, SUPPLIER_ABBR, PAN_NO, TO_CHAR(PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), NOTES, DORMANT_FLAG, NULL, WEB_ADDR, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, '2' FROM FMS7_SUPPLIER_MST WHERE SUPPLIER_ABBR = 'SEIPL'  AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COMPANY_CD,EFF_DT,TIMESTAMP", conn);
			
			while (rset.next()) {
				cd = rset.getString(1);
				eff_dt = rset.getString(2);

				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					value = rset.getString(i + 1);
					cell = row.createCell(i);
					if (i == 7) {
						value = value.equalsIgnoreCase("N") ? "Y" : "N";
					}
					/*
					 * else if (i == 10) { // emp_abbr value = getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cd + "," + eff_dt + ","), conn, "");
			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_COMPANY_OWNER_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " , "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COMPANY_OWNER_MST>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_COMPANY_OWNER_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COMPANY_OWNER_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COMPANY_OWNER_ADDR_MST() throws SQLException, IOException {
		function_nm = "FMS_COMPANY_OWNER_ADDR_MST()";
		
		try {

			System.out.println("<<START>><<FMS_COMPANY_OWNER_ADDR_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_COMPANY_OWNER_ADDR_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ADDRESS_TYPE,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data (COUNTERPARTY)
			queryString = "SELECT DISTINCT(B.COUNTERPARTY_ABBR), B.COUNTERPARTY_CD  FROM FMS9_COUNTERPTY_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE B.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND UPPER(B.COUNTERPARTY_ABBR) = 'SEIPL' AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.COUNTERPARTY_CD  ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ADDRESS_TYPE,EFF_DT,TIMESTAMP", conn);
			
			while (rset.next()) {
				// fetch value from resultset for logger:
				abbr = rset.getString(1);
				cd = rset.getString(2);
				
				// SUPPLIER
				queryString1 = "SELECT B.COUNTERPARTY_CD, B.ADDRESS_TYPE, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_SUPPLIER_ADDRESS_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_SUPPLIER_ADDRESS_MST C WHERE B.SUPPLIER_CD = C.SUPPLIER_CD) AND B.SUPPLIER_CD = (SELECT DISTINCT(D.SUPPLIER_CD) FROM FMS7_SUPPLIER_MST D WHERE B.SUPPLIER_CD = D.SUPPLIER_CD AND D.COUNTERPARTY_CD = ? AND UPPER(SUPPLIER_ABBR) = 'SEIPL' ) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.SUPPLIER_CD ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					row = spreadsheet.createRow(nrow++);
					// fetch value from resultset for logger:
					address_type = rset1.getString(2);
					eff_dt = rset1.getString(3);

					value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
					cell = row.createCell(0);
					cell.setCellValue("'" + value + "'");

					value = rset.getString(2) == null ? "null" : rset.getString(2).replace("'", "");
					cell = row.createCell(1);
					cell.setCellValue("'" + value + "'");

					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
						/*
						 * if (i == 16 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); }
						 */
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + address_type + "," + eff_dt + ","), conn, "");

				}
				stmt1.close();
				rset1.close();
			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_COMPANY_OWNER_ADDR_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " ,,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COMPANY_OWNER_ADDR_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_COMPANY_OWNER_ADDR_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COMPANY_OWNER_ADDR_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COUNTERPARTY_PLANT_DTL() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_PLANT_DTL()";
		
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_PLANT_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_COUNTERPARTY_PLANT_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			
			int gail = 1, gspl = 1, pil = 1, rgpl = 1, gsiglim = 1;
			String RIL_cd = "1";
			int seq = 1;
			
			String plant_map = "";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data (COUNTERPARTY)
			queryString = "SELECT DISTINCT(B.COUNTERPARTY_ABBR), B.COUNTERPARTY_CD, B.EFF_DT  FROM FMS9_COUNTERPTY_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE B.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND B.COUNTERPARTY_ABBR != 'SEIPL' AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.COUNTERPARTY_CD, B.EFF_DT DESC   ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,TIMESTAMP", conn);
			
			while (rset.next()) {
				
				abbr = rset.getString(1).trim();
				cd = rset.getString(2);

				// CUSTOMER
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'C', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.PLANT_NAME, A.PLANT_SHORT_ABBR, A.PLANT_ADDR, A.PLANT_STATE, A.PLANT_ZONE, A.PLANT_CITY, A.PLANT_PIN, A.PLANT_SECTOR, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_CUSTOMER_PLANT_DTL A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				stmt1.setString(6, delta_FromDt);
				stmt1.setString(7, delta_FromDt);
				stmt1.setString(8, delta_ToDt);
				stmt1.setString(9, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					plant_map = abbr + "-" + rset1.getString(3);
					
					if (!Arrays.asList(mpe.customer_delete).contains(plant_map)) {
						
						row = spreadsheet.createRow(nrow++);
						// fetch data from resultset for logger:
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						eff_dt = rset1.getString(4);
						
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
						cell = row.createCell(0);
//						if(i == 0) {
//							for (int c = 0; c < counterparty_map.split(",").length; c++){
//								if (counterparty_map.split(",")[c].contains(value+";")){
//									value = counterparty_map.split(",")[c].split(";")[1];
//									abbr = value;
//								}
//							}
						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value);
						        abbr = value; 
						    }
//						}
						cell.setCellValue("'" + value + "'");
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
							
							if (i == 6) {
								if (value.length() > 20) {
									value = value.substring(0, 20);
								}
								if (abbr.equals("BGL")) {		// SagarB20250828	Entered Pant abbr for BGL since user was unable to enter it in FMSLIVE
									value = "BGL HYD";
								}
								cell.setCellValue("'" + value + "'");

							} 
							else if (i == 5 && value.length() > 50) {
								value = value.substring(0, 50);
								cell.setCellValue("'" + value + "'");

							} 
							else if (i == 12 && !value.equals("null")) { // plant_sector
								
								query_abbr = "SELECT SECTOR_ABBR FROM FMS7_SECTOR_MST WHERE SECTOR_CD = ? ";
								stmt_abbr = conn.prepareStatement(query_abbr);
								stmt_abbr.setString(1, value);
								rset_abbr = stmt_abbr.executeQuery();
								
								if (rset_abbr.next()) {
									value = rset_abbr.getString(1);
								}
								
								rset_abbr.close();
								stmt_abbr.close();

								cell.setCellValue("'" + value + "'");
							}
							/*
							 * else if (i == 15 && !value.equals("null")) { // emp_abbr value =
							 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
							 */
							else {
								cell.setCellValue("'" + value + "'");
							}
						}
						count++;
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn,"");
					}

				}
				stmt1.close();
				rset1.close();

				// TRADER
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'T', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.PLANT_NAME, A.PLANT_SHORT_ABBR, A.PLANT_ADDR, A.PLANT_STATE, A.PLANT_ZONE, A.PLANT_CITY, A.PLANT_PIN, A.PLANT_SECTOR, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_TRADER_PLANT_DTL A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ?  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				stmt1.setString(6, delta_FromDt);
				stmt1.setString(7, delta_FromDt);
				stmt1.setString(8, delta_ToDt);
				stmt1.setString(9, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {

					plant_map = abbr + "-" + rset1.getString(3);
					
					if(abbr.equals("RIL")) {
						RIL_cd = rset1.getString(1);
					}
					
					if (!Arrays.asList(mpe.trader_delete).contains(plant_map) && !abbr.equals("RIL"))
					{
						row = spreadsheet.createRow(nrow++);
						// fetch data from resultset for logger:
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						eff_dt = rset1.getString(4);
						
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
						
						cell = row.createCell(0);
						if (abbr.contains("RIL")) {
							cell.setCellValue("'RIL'");
						}
						else {
//							if(i == 0) {
//								for (int c = 0; c < counterparty_map.split(",").length; c++){
//									if (counterparty_map.split(",")[c].contains(value+";")){
//										value = counterparty_map.split(",")[c].split(";")[1];
//										abbr = value;
//									}
//								}
							
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value);
							        abbr = value; 
							    }
//							}
							cell.setCellValue("'" + value + "'");
						}
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
							
							if (i == 1 && abbr.contains("RIL")) {
								cell.setCellValue("'" + RIL_cd + "'");
							}
							else if(i == 3) {
								if (rset.getString(1).contains("RIL-D6")) {
									value = seq_no;
									seq = Integer.parseInt(value);
								}
								else if (rset.getString(1).contains("RIL-CBM")) {
									value = (seq + Integer.parseInt(rset1.getString(3))) + "";
									seq_no = value;
								}
								else {
									value = seq_no;
								}
								cell.setCellValue("'" + value + "'");
							}
							else if (i == 6 && value.length() > 20) {
								value = value.substring(0, 20);
								cell.setCellValue("'" + value + "'");

							} else if (i == 5 && value.length() > 50) {
								value = value.substring(0, 50);
								cell.setCellValue("'" + value + "'");

							} else if (i == 12 && !value.equals("null")) { // plant_sector
								
								query_abbr = "SELECT SECTOR_ABBR FROM FMS7_SECTOR_MST WHERE SECTOR_CD = ? ";
								stmt_abbr = conn.prepareStatement(query_abbr);
								stmt_abbr.setString(1, value);
								rset_abbr = stmt_abbr.executeQuery();
								
								if (rset_abbr.next()) {
									value = rset_abbr.getString(1);
								}
								
								rset_abbr.close();
								stmt_abbr.close();

								cell.setCellValue("'" + value + "'");
							}
							/*
							 * else if (i == 15 && !value.equals("null")) { // emp_abbr value =
							 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
							 */
							else {
								cell.setCellValue("'" + value + "'");
							}
						}
						count++;
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn,"");
					}

				}
				stmt1.close();
				rset1.close();

				// TRANSPORTER
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'R', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.PLANT_NAME, A.PLANT_SHORT_ABBR, A.PLANT_ADDR, A.PLANT_STATE, A.PLANT_ZONE, A.PLANT_CITY, A.PLANT_PIN, A.PLANT_SECTOR, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_TRANSPORTER_PLANT_DTL A, FMS7_TRANSPORTER_MST B WHERE A.TRANSPORTER_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				stmt1.setString(6, delta_FromDt);
				stmt1.setString(7, delta_FromDt);
				stmt1.setString(8, delta_ToDt);
				stmt1.setString(9, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {

					row = spreadsheet.createRow(nrow++);

					value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
					cell = row.createCell(0);
					
//					for (int x = 0; x < transporter_map.split(",").length; x++) {
//						if (transporter_map.split(",")[x].split(";")[0].contains(abbr)) {
//							value = transporter_map.split(",")[x].split(";")[1];
//							abbr = value;
//						}
//					}
					
					
						if (mpe.transporter_map.containsKey(abbr)) {
//							System.out.println("==>"+abbr);
							value = mpe.transporter_map.get(abbr);
							abbr = value;
//							System.out.println("value abbr:"+value);
						}

//					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        abbr = value; 
					    }
//					}
					cell.setCellValue("'" + value + "'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
					
						if (i == 3) {
							if (abbr.contains("Gail")) {
								value = gail+"";
								gail++;
							}
							else if (abbr.contains("RGPL")) {
								value = rgpl+"";
								rgpl++;
							}
							else if (abbr.contains("PipeInfra")) {
								value = pil+"";
								pil++;
							}
							else if (abbr.contains("GSPL")) {
								value = gspl+"";
								gspl++;
							}
							else if (abbr.contains("GSIGLIM")) {
								value = gsiglim+"";
								gsiglim++;
							}

							cell.setCellValue("'" + value + "'");
						}
						else if (i == 6 && value.length() > 20) {
							value = value.substring(0, 20);
							cell.setCellValue("'" + value + "'");

						} else if (i == 5 && value.length() > 50) {
							value = value.substring(0, 50);
							cell.setCellValue("'" + value + "'");

						} else if (i == 12 && !value.equals("null")) { // plant_sector
							
							query_abbr = "SELECT SECTOR_ABBR FROM FMS7_SECTOR_MST WHERE SECTOR_CD = ? ";
							stmt_abbr = conn.prepareStatement(query_abbr);
							stmt_abbr.setString(1, value);
							rset_abbr = stmt_abbr.executeQuery();
							
							if (rset_abbr.next()) {
								value = rset_abbr.getString(1);
							}
							rset_abbr.close();
							stmt_abbr.close();

							cell.setCellValue("'" + value + "'");
						}
						/*
						 * else if (i == 15 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						 */
						else {
							cell.setCellValue("'" + value + "'");
						}
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

				}
				stmt1.close();
				rset1.close();
				
				// TRANSPORTER: mapping disabled meter as plants
				queryString1 = "SELECT B.TRANSPORTER_CD, 'R', NULL, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.TRANSPORTER_NAME, B.TRANSPORTER_ABBR, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), B.EMP_CD, NULL, NULL FROM  FMS7_TRANSPORTER_MST B WHERE  B.COUNTERPARTY_CD = ? AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
//				while (rset1.next() && !transporter_map.contains(";"+rset1.getString(6)) && !rset1.getString(6).toUpperCase().contains("PARK") && transporter_map.contains(abbr+";") && !meter_map.contains(abbr+";") && !rset1.getString(6).toUpperCase().contains("PMSKP") ) {
				while (rset1.next() && !mpe.transporter_map.containsKey(rset1.getString(6))  && !rset1.getString(6).toUpperCase().contains("PARK") && mpe.transporter_map.containsKey(abbr) && !mpe.meter_map.containsKey(abbr) && !rset1.getString(6).toUpperCase().contains("PMSKP") ) {
						
					row = spreadsheet.createRow(nrow++);

					value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
					cell = row.createCell(0);
					
//					for (int x = 0; x < transporter_map.split(",").length; x++) {
//						if (transporter_map.split(",")[x].split(";")[0].contains(abbr)) {
//							value = transporter_map.split(",")[x].split(";")[1];
//							abbr = value;
//						}
//					}
//					System.out.println("==>"+abbr);
					if (mpe.transporter_map.containsKey(abbr)) {
						value = mpe.transporter_map.get(abbr);
						abbr = value;
//						System.out.println("value abbr:"+value);
					}
					
//					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        abbr = value; 
					    }
//					}
					cell.setCellValue("'" + value + "'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
					
						if (i == 3) {
							if (abbr.contains("Gail")) {
								value = gail+"";
								gail++;
							}
							else if (abbr.contains("RGPL")) {
								value = rgpl+"";
								rgpl++;
							}
							else if (abbr.contains("PipeInfra")) {
								value = pil+"";
								pil++;
							}
							else if (abbr.contains("GSPL")) {
								value = gspl+"";
								gspl++;
							}
							else if (abbr.contains("GSIGLIM")) {
								value = gsiglim+"";
								gsiglim++;
							}

							cell.setCellValue("'" + value + "'");
						}
						else if (i == 6 && value.length() > 20) {
							value = value.substring(0, 20);
							cell.setCellValue("'" + value + "'");

						} else if (i == 5 && value.length() > 50) {
							value = value.substring(0, 50);
							cell.setCellValue("'" + value + "'");

						} else if (i == 7) { // Address
							
							query_abbr = "SELECT A.ADDR, A.STATE, A.ZONE, A.CITY, A.PIN FROM FMS7_TRANSPORTER_ADDRESS_MST A WHERE A.TRANSPORTER_CD = ? ";
							stmt_abbr = conn.prepareStatement(query_abbr);
							stmt_abbr.setString(1, rset1.getString(1));
							rset_abbr = stmt_abbr.executeQuery();
							
							if (rset_abbr.next()) {
								
								do {
									cell = row.createCell(i++);
									cell.setCellValue("'" + value + "'");
									
								} while (i < 12);
								i--;
							}
							else {
								cell.setCellValue("'null'");
							}
							
							rset_abbr.close();
							stmt_abbr.close();
						}
						/*
						 * else if (i == 15 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						 */
						else {
							cell.setCellValue("'" + value + "'");
						}
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

				}
				stmt1.close();
				rset1.close();

			}

			stmt.close();
			rset.close();

			// SUPPLIER
			queryString1 = "SELECT B.SUPPLIER_ABBR, '2', 'B', '1', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), 'SEIPL-GJ', 'SEIPL-GJ', A.ADDR, A.STATE, A.ZONE, A.CITY, A.PIN, NULL, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_SUPPLIER_ADDRESS_MST A, FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD AND B.SUPPLIER_ABBR = 'SEIPL' AND A.ADDRESS_TYPE = 'R' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.EFF_DT DESC ";
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

			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				// fetch data from resultset for logger:
				entity = rset1.getString(3);
				seq_no = rset1.getString(4);
				eff_dt = rset1.getString(5);

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					/*
					 * if (i == 15 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt1.close();
			rset1.close();

			queryString1 = "SELECT B.SUPPLIER_ABBR, '2', 'B', (A.SEQ_NO+1), TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.PLANT_NAME, A.PLANT_SHORT_ABBR, A.PLANT_ADDR, A.PLANT_STATE, A.PLANT_ZONE, A.PLANT_CITY, A.PLANT_PIN, A.PLANT_SECTOR, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_SUPPLIER_PLANT_DTL A, FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD  AND B.SUPPLIER_ABBR = 'SEIPL' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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

			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				// fetch data from resultset for logger:
				entity = rset1.getString(3);
				seq_no = rset1.getString(4);
				eff_dt = rset1.getString(5);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");

					if (i == 12 && !value.equals("null")) { // plant_sector
						query_abbr = "SELECT SECTOR_ABBR FROM FMS7_SECTOR_MST WHERE SECTOR_CD = ? ";
						stmt_abbr = conn.prepareStatement(query_abbr);
						stmt_abbr.setString(1, value);
						rset_abbr = stmt_abbr.executeQuery();
						
						if (rset_abbr.next()) {
							value = rset_abbr.getString(1);
						}
						
						rset_abbr.close();
						stmt_abbr.close();

						cell.setCellValue("'" + value + "'");
					}
					/*
					 * else if (i == 15 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt1.close();
			rset1.close();

			filename = migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_PLANT_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " ,,,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_PLANT_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_PLANT_DTL>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_PLANT_DTL()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COUNTERPARTY_BU_DTL() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_BU_DTL()";
		
		try {
			logger.checkpoint(fname, "<<START>>,<<FMS_COUNTERPARTY_BU_DTL>>,,,,", conn);
			System.out.println("<<START>><<FMS_COUNTERPARTY_BU_DTL>>");
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);

			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data

			// SURVEYOR
			queryString1 = "SELECT B.SURVEYOR_ABBR, '2', 'S', '1', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.SURVEYOR_NAME, B.SURVEYOR_ABBR, A.ADDR, A.STATE, A.ZONE, A.CITY, A.PIN, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_SURVEYOR_ADDRESS_MST A, FMS7_SURVEYOR_MST B WHERE A.SURVEYOR_CD = B.SURVEYOR_CD AND A.ADDRESS_TYPE = 'R' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.EFF_DT DESC ";
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
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,TIMESTAMP", conn);
			
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				
				abbr = rset1.getString(1).trim();
				cd = rset1.getString(2);
				entity = rset1.getString(3);
				seq_no = rset1.getString(4);
				eff_dt = rset1.getString(5);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					/*
					 * if (i == 15 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
	 				if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//	 				}

	 				    if (mpe.counterparty_map.containsKey(value)) {
	 				        value =mpe.counterparty_map.get(value); 
	 				        abbr = value; 
	 				   
						}
	 				}
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt1.close();
			rset1.close();

			// VESSEL
			queryString1 = "SELECT B.VESSEL_ABBR, '2', 'V', '1', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.VESSEL_NAME, B.VESSEL_ABBR, A.ADDR, A.STATE, A.ZONE, A.CITY, A.PIN, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_VESSEL_AGENT_ADDR_MST A, FMS7_VESSEL_AGENT_MST B WHERE A.VESSEL_CD = B.VESSEL_CD AND A.ADDRESS_TYPE = 'R' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.EFF_DT DESC ";
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

			while (rset1.next()) {
				for (int k = 0; k < 2; k++) {
					row = spreadsheet.createRow(nrow++);
					
					abbr = rset1.getString(1).trim();
					cd = rset1.getString(2);
					entity = rset1.getString(3);
					seq_no = rset1.getString(4);
					eff_dt = rset1.getString(5);
	
					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
						/*
						 * if (i == 15 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); }
						 */
						if(i == 0) {
	//						for (int c = 0; c < counterparty_map.split(",").length; c++){
	//							if (counterparty_map.split(",")[c].contains(value+";")){
	//								value = counterparty_map.split(",")[c].split(";")[1];
	//								abbr = value;
	//							}
	//						}
	
						    if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						      
						        abbr = value; 
						    }
						}
						else if (i == 2 && k == 0) {
							value = "S";
						}
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");
				}

			}
			stmt1.close();
			rset1.close();

			// CHA
			queryString1 = "SELECT B.CHA_ABBR, '2', 'H', '1', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY'), B.CHA_NAME, B.CHA_ABBR, A.ADDR, A.STATE, A.ZONE, A.CITY, A.PIN, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_CUS_HOUSE_AGENT_ADDR_MST A, FMS7_CUS_HOUSE_AGENT_MST B WHERE A.CHA_CD = B.CHA_CD AND A.ADDRESS_TYPE = 'R' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.EFF_DT DESC ";
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

			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				
				abbr = rset1.getString(1).trim();
				cd = rset1.getString(2);
				entity = rset1.getString(3);
				seq_no = rset1.getString(4);
				eff_dt = rset1.getString(5);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					/*
					 * if (i == 15 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}

					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value);
					        abbr = value; 
					    }
					}
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt1.close();
			rset1.close();

			filename = migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_BU_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " ,,,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_BU_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_BU_DTL>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_BU_DTL()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_SECTOR_MST() throws SQLException, IOException {
		function_nm = "FMS_SECTOR_MST()";
		
		try {

			System.out.println("<<START>><<FMS_SECTOR_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SECTOR_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "SECTOR_CD,SECTOR_NAME,SECTOR_ABBR,SECTOR_TYPE,STATUS_FLAG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = "SELECT SECTOR_CD, SECTOR_NAME, SECTOR_ABBR, FLAG, 'Y', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, '2', NULL FROM FMS7_SECTOR_MST  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "SECTOR_CD,SECTOR_ABBR,TIMESTAMP", conn);
			
			while (rset.next()) {
				cd = rset.getString(1);
				abbr = rset.getString(3);
				
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					if (i == 3) {
						value = (value.equals("N") ? "null" : value);
						cell.setCellValue("'" + value + "'");
					} 
					/*
					 * else if (i == 5 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
//					else {
						cell.setCellValue("'" + value + "'");
//					}
				}
				count++;
				logger.data(fname, (cd + "," + abbr + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_SECTOR_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SECTOR_MST>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_SECTOR_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_SECTOR_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_INT_RATE_MST() throws SQLException, IOException {
		function_nm = "FMS_INT_RATE_MST()";
		
		try {

			System.out.println("<<START>><<FMS_INT_RATE_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_INT_RATE_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "INT_RATE_CD,INT_RATE_NM,BANK_ABBR,FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = "SELECT INT_RATE_CD, INT_RATE_NM, BANK_ABBR, FLAG, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, '2', NULL FROM FMS7_CONT_INT_RATE_MST  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "INT_RATE_CD,BANK_ABBR,TIMESTAMP", conn);
			
			while (rset.next()) {
				cd = rset.getString(1);
				abbr = rset.getString(3).trim();
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					/*
					 * if (i == 5 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
					 * }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cd + "," + abbr + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_INT_RATE_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_INT_RATE_MST>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_INT_RATE_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_INT_RATE_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_EXCHG_RATE_MST() throws SQLException, IOException {
		function_nm = "FMS_EXCHG_RATE_MST()";
		
		try {

			System.out.println("<<START>><<FMS_EXCHG_RATE_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_EXCHG_RATE_MST>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "EXC_RATE_CD,EXC_RATE_NM,BANK_ABBR,FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,COMPONENT_FLAG,COMPONENT1,COMPONENT2,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = "SELECT EXC_RATE_CD, EXC_RATE_NM, BANK_ABBR, FLAG, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, NULL, NULL, NULL, '2', NULL FROM FMS7_CONT_EXCHG_RATE_MST  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "EXC_RATE_CD,BANK_ABBR,TIMESTAMP", conn);
			
			while (rset.next()) {
				cd = rset.getString(1);
				abbr = rset.getString(3).trim();
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					/*
					 * if (i == 5 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
					 * }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cd + "," + abbr + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_EXCHG_RATE_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_EXCHG_RATE_MST>>,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_EXCHG_RATE_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_EXCHG_RATE_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_INT_PAY_RATE_ENTRY() throws SQLException, IOException {
		function_nm = "FMS_INT_PAY_RATE_ENTRY()";
		
		try {

			System.out.println("<<START>><<FMS_INT_PAY_RATE_ENTRY>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_INT_PAY_RATE_ENTRY>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			// "EXPORT/FMS_INT_PAY_RATE_ENTRY.xlsx"
			columns = "INT_RATE_NM,INT_RATE_CD,EFF_DT,INT_VAL,CURRENCY_CD,REMARK,FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = "SELECT B.INT_RATE_NM, A.INT_RATE_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.INT_VAL, A.CURRENCY_CD, A.REMARK, A.FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, '2', NULL FROM FMS7_INT_PAY_RATE_ENTRY A, FMS7_CONT_INT_RATE_MST B WHERE A.INT_RATE_CD = B.INT_RATE_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "EXC_RATE_CD,BANK_ABBR,TIMESTAMP", conn);
			
			while (rset.next()) {
				cd = rset.getString(2);
				abbr = rset.getString(1);
				eff_dt = rset.getString(3);
				
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					/*
					 * if (i == 8 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
					 * }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cd + "," + abbr + "," + eff_dt + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_INT_PAY_RATE_ENTRY_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_INT_PAY_RATE_ENTRY>>,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_INT_PAY_RATE_ENTRY>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_INT_PAY_RATE_ENTRY()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_EXCHG_RATE_ENTRY() throws SQLException, IOException {
		function_nm = "FMS_EXCHG_RATE_ENTRY()";
		
		try {

			System.out.println("<<START>><<FMS_EXCHG_RATE_ENTRY>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_EXCHG_RATE_ENTRY>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "EXCHG_RATE_NM,EXCHG_RATE_CD,EFF_DT,EXCHG_VAL,CURRENCY_CD,CURRENCY_CD_FROM,REMARK,FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = "SELECT B.EXC_RATE_NM, A.EXCHG_RATE_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.EXCHG_VAL, A.CURRENCY_CD, A.CURRENCY_CD_FROM, A.REMARK, A.FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.ENT_BY, NULL, NULL, '2', NULL FROM FMS7_EXCHG_RATE_ENTRY A, FMS7_CONT_EXCHG_RATE_MST B WHERE A.EXCHG_RATE_CD = B.EXC_RATE_CD  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "EXCHG_RATE_NM,EXCHG_RATE_CD,EFF_DT,TIMESTAMP", conn);
			
			while (rset.next()) {
				abbr = rset.getString(1);
				cd = rset.getString(2);
				eff_dt = rset.getString(3);
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					/*
					 * if (i == 9 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
					 * }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cd + "," + abbr + "," + eff_dt + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_EXCHG_RATE_ENTRY_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_EXCHG_RATE_ENTRY>>,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_EXCHG_RATE_ENTRY>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_EXCHG_RATE_ENTRY()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_BANK_MST() throws SQLException, IOException {
		function_nm = "FMS_BANK_MST()";
		
		try {

			System.out.println("<<START>><<FMS_BANK_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_BANK_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "BANK_CD,BANK_NAME,BANK_ABBR,EFF_DT,BRANCH_NAME,ADDR,CITY,PIN,STATE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,REMARK,BRANCH_IFSC_CD,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String bank_name = "";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = "SELECT BANK_CD, BANK_NAME, BANK_ABBR, TO_CHAR(EFF_DT, 'DD/MM/YYYY'), BRANCH_NAME, ADDR, CITY, PIN, STATE, COUNTRY, PHONE, MOBILE, ALT_PHONE, FAX_1, FAX_2, EMAIL, REMARK, BRANCH_IFSC_CD, NVL(FLAG, 'Y'), '2', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL FROM FMS7_BANK_MST ";
//					+ " WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY')) ";
			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "BANK_CD,BANK_NAME,BANK_ABBR,EFF_DT,TIMESTAMP", conn);
			
			while (rset.next()) {
				// fetch
				cd = rset.getString(1);
				bank_name = rset.getString(2);
				abbr = rset.getString(3);
				eff_dt = rset.getString(4);

				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					/*
					 * if (i == 20 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cd + "," + bank_name + "," + abbr + "," + eff_dt + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_BANK_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BANK_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_BANK_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_BANK_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_TURNOVER_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_TURNOVER_DTL()";
		
		try {

			System.out.println("<<START>><<FMS_ENTITY_TURNOVER_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_TURNOVER_DTL>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,FINANCIAL_YEAR,TURNOVER_CD,TURNOVER_FLAG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String financial_yr = "";
			
			String RIL_cd = "1";
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data (COUNTERPARTY)
			queryString = "SELECT DISTINCT(B.COUNTERPARTY_ABBR), B.COUNTERPARTY_CD  FROM FMS9_COUNTERPTY_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE B.COUNTERPARTY_CD = C.COUNTERPARTY_CD)  AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.COUNTERPARTY_CD  ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,FINANCIAL_YEAR,TIMESTAMP", conn);
			while (rset.next()) {
				cd = rset.getString(2);
				abbr = rset.getString(1).trim();
				
				// CUSTOMER
				queryString1 = "SELECT B.CUSTOMER_CD, 'C', B.FINANCIAL_YEAR, B.TURNOVER_CD, B.TURNOVER_FLAG, B.ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_CUSTOMER_TURNOVER_DTL B WHERE B.CUSTOMER_CD = (SELECT DISTINCT(D.CUSTOMER_CD) FROM FMS7_CUSTOMER_MST D WHERE B.CUSTOMER_CD = D.CUSTOMER_CD AND D.COUNTERPARTY_CD = ? )  AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.CUSTOMER_CD, B.TURNOVER_FLAG DESC ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					row = spreadsheet.createRow(nrow++);

					entity = rset1.getString(2);
					financial_yr = rset1.getString(3);

					value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
					cell = row.createCell(0);
//					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}
					

				    if (mpe.counterparty_map.containsKey(value)) {
				        value =mpe.counterparty_map.get(value); 
				        abbr = value; 
				    }
//					}
					cell.setCellValue("'" + value + "'");

					value = rset.getString(2) == null ? "null" : rset.getString(2).replace("'", "");
					cell = row.createCell(1);
					cell.setCellValue("'" + value + "'");

					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
						/*
						 * if (i == 6 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
						 * }
						 */
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + financial_yr + ","), conn, "");

				}
				stmt1.close();
				rset1.close();

				// TRADER
				queryString1 = "SELECT B.TRADER_CD, 'T', B.FINANCIAL_YEAR, B.TURNOVER_CD, B.TURNOVER_FLAG, B.ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_TRADER_TURNOVER_DTL B WHERE B.TRADER_CD = (SELECT DISTINCT(D.TRADER_CD) FROM FMS7_TRADER_MST D WHERE B.TRADER_CD = D.TRADER_CD AND D.COUNTERPARTY_CD = ? ) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.TRADER_CD ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					
					if(abbr.equals("RIL")) {
						RIL_cd = rset.getString(2);
					}
					
					if (!abbr.equals("RIL")) {
						
						row = spreadsheet.createRow(nrow++);
						
						entity = rset1.getString(2);
						financial_yr = rset1.getString(3);
						
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
						
						//cell = row.createCell(0);
						//cell.setCellValue("'" + value + "'");
						cell = row.createCell(0);
						if (abbr.contains("RIL")) {
							cell.setCellValue("'RIL'");
						}
						else {
//							if(i == 0) {
//							for (int c = 0; c < counterparty_map.split(",").length; c++){
//								if (counterparty_map.split(",")[c].contains(value+";")){
//									value = counterparty_map.split(",")[c].split(";")[1];
//									abbr = value;
//								}
//							}
							

						    if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						       
						        abbr = value; 
						    }
//						}
							cell.setCellValue("'" + value + "'");
						}
	
						value = rset.getString(2) == null ? "null" : rset.getString(2).replace("'", "");
						cell = row.createCell(1);
						//cell.setCellValue("'" + value + "'");
						if ( abbr.contains("RIL")) {
							cell.setCellValue("'" + RIL_cd + "'");
						}
						else {
							cell.setCellValue("'" + value + "'");
						}
	
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
							/*
							 * if (i == 6 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
							 * }
							 */
							cell.setCellValue("'" + value + "'");
						}
						count++;
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + financial_yr + ","), conn, "");
						
					}
				}
				stmt1.close();
				rset1.close();

				// BUSINESS OWNER
				queryString1 = "SELECT B.SUPPLIER_CD, 'B', B.FINANCIAL_YEAR, B.TURNOVER_CD, B.TURNOVER_FLAG, B.ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_SUPPLIER_TURNOVER_DTL B WHERE B.SUPPLIER_CD = (SELECT DISTINCT(D.SUPPLIER_CD) FROM FMS7_SUPPLIER_MST D WHERE B.SUPPLIER_CD = D.SUPPLIER_CD AND D.COUNTERPARTY_CD = ? )  AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.SUPPLIER_CD ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					row = spreadsheet.createRow(nrow++);
					entity = rset1.getString(2);
					financial_yr = rset1.getString(3);
					
					value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
					cell = row.createCell(0);
					cell.setCellValue("'" + value + "'");

					value = rset.getString(2) == null ? "null" : rset.getString(2).replace("'", "");
					cell = row.createCell(1);
					cell.setCellValue("'" + value + "'");

					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
						/*
						 * if (i == 6 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
						 * }
						 */
						cell.setCellValue("'" + value + "'");
					}

				}
				stmt1.close();
				rset1.close();

			}

			stmt.close();
			rset.close();
			
			// Adding By-Default TurnOver Entry 'YES' for all Financial Year
			queryString = "SELECT 'SEIPL', '1', 'B', NULL, '1', 'Y', A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL FROM FMS7_SUPPLIER_MST A WHERE A.SUPPLIER_CD = '1' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				
				for (int i = 0; i < 6; i++) {
					
					row = spreadsheet.createRow(nrow++);
					abbr = rset.getString(1);
					cd = "1";
					entity = rset.getString(3);
					financial_yr = "20" + (19+i) + "-20" + (20+i);
					
					for (int j = 0; j < columns.split(",").length; j++) {

						cell = row.createCell(j);
						value = rset.getString(j+1) == null ? "null" : rset.getString(j+1).replace("'", "");
						
						if (j == 3) {
							value = financial_yr;
						}

						cell.setCellValue("'" + value + "'");
						
						
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + financial_yr + ","), conn, "");
					
				}
				
			}
			
			rset.close();
			stmt.close();

			filename = migration_setup_dir + "EXPORT/FMS_ENTITY_TURNOVER_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_TURNOVER_DTL>>,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_ENTITY_TURNOVER_DTL>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_TURNOVER_DTL()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_SHIP_MST() throws SQLException, IOException {
		function_nm = "FMS_SHIP_MST()";
		
		try {

			System.out.println("<<START>><<FMS_SHIP_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SHIP_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "EFF_DT,SHIP_CD,SHIP_NAME,SHIP_CALL_SIGN,SHIP_FLAG,SHIP_IMO_NO,SHIP_CLASS_SOC,INMARSAT_NO,SHIP_OWNER_NAME,SHIP_OPERATOR_NAME,SHIP_FAX_NO,SHIP_TELEX_NO,SHIP_EMAIL,GROSS_TONNAGE,CARGO_CAPACITY,VOLUME_UNIT,PERCENTAGE_CAPACITY,SHIP_ITEM,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String ship_nm = "";
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = "SELECT '01/01/2007', SHIP_CD, SHIP_NAME, SHIP_CALL_SIGN, SHIP_FLAG, SHIP_IMO_NO, SHIP_CLASS_SOC, INMARSAT_NO, SHIP_OWNER_NAME, SHIP_OPERATOR_NAME, SHIP_FAX_NO, SHIP_TELEX_NO, SHIP_EMAIL, GROSS_TONNAGE, CARGO_CAPACITY, VOLUME_UNIT, PERCENTAGE_CAPACITY, SHIP_ITEM, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, '2', NULL FROM FMS7_SHIP_MST  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "EFF_DT,SHIP_CD,SHIP_NAME,TIMESTAMP", conn);
			
			while (rset.next()) {
				cd = rset.getString(2);
				ship_nm = rset.getString(3);
				eff_dt = rset.getString(1);
				
				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					if (i == 15 && !value.equalsIgnoreCase("null")) {
						queryString1 = "SELECT UPPER(UNIT_ABR) FROM FMS7_UNIT_MST WHERE UNIT_CD = ?";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, value);
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							if (rset1.getString(1).equalsIgnoreCase("SCM")) {
								value = "1";
							} else if (rset1.getString(1).equalsIgnoreCase("MMSCM")) {
								value = "2";
							} else if (rset1.getString(1).equalsIgnoreCase("MT")) {
								value = "3";
							}
							cell.setCellValue("'" + value + "'");
						} else {
							cell.setCellValue("'" + value + "'");
						}
						
						rset1.close();
						stmt1.close();
					}
					/*
					 * else if (i == 18 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (eff_dt + "," + cd + "," + ship_nm + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_SHIP_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SHIP_MST>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_SHIP_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_SHIP_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_CONTACT_MST() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_CONTACT_MST()";
		
		try {

			System.out.println("<<START>><<FMS_ENTITY_CONTACT_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_CONTACT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			// columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ADDR_IS_ACTIVE,TO_NOM,TO_INV,TO_FM,TO_PM,TO_JT,TO_OTHER,RM_FLAG,TO_RM,TYPE";
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ADDR_IS_ACTIVE,TO_NOM,TO_INV,TO_FM,TO_PM,TO_JT,TO_OTHER,RM_FLAG,TO_RM,TYPE,F402_FLAG,TO_F402";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			
//			String[] rlng = {"N", "N", "N", "N", "N", "N"};
//			String[] dlng = {"N", "N", "N", "N", "N", "N"};
			
			String type = "RLNG";
			String map="", temp="", cp_plant = "";
			
			int seq_num = 1;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			Map<String, String> cp_seq = new HashMap<String, String>();
			// Below block of code is for inserting data (COUNTERPARTY)
			queryString = "SELECT DISTINCT(B.COUNTERPARTY_ABBR), B.COUNTERPARTY_CD, B.EFF_DT  FROM FMS9_COUNTERPTY_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE B.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND B.COUNTERPARTY_ABBR != 'SEIPL' AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.COUNTERPARTY_CD, B.EFF_DT DESC   ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "ABBR,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,TYPE,TIMESTAMP", conn);

			while (rset.next()) {

				abbr = rset.getString(1).trim();
				cd = rset.getString(2);
				
				cp_seq = new HashMap<String, String>();
				cp_plant = "";
				String[] dlng = {"null", "null", "null", "null", "null", "null"};
				String[] rlng = {"null", "null", "null", "null", "null", "null"};
				
				seq_num = 1;
				// CUSTOMER: RLNG
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'C', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.NOM_FLAG, A.INV_FLAG, A.FM_FLAG, A.PM_FLAG, A.JT_FLAG, A.OTHER_FLAG, A.ACTIVE_FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, NULL, A.INV_TO_FLAG, NULL, NULL, NULL, NULL, NULL, NULL, 'RLNG', NULL, NULL  FROM FMS7_CUSTOMER_CONTACT_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND A.ACTIVE_FLAG = 'Y' ORDER BY A.EMAIL, A.EFF_DT  ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				stmt1.setString(6, delta_FromDt);
				stmt1.setString(7, delta_FromDt);
				stmt1.setString(8, delta_ToDt);
				stmt1.setString(9, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					
					if ( (rset1.getString(11) != null && !rset1.getString(11).toLowerCase().contains("shell.com"))) {
						if (!cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
							for (int s = 0; s < rlng.length; s++) {
								rlng[s] = "null";
							}
						}
//						String[] rlng = {"null", "null", "null", "null", "null", "null"};
	
						temp="";
						if (rset1.getString(12).contains("P")) {
							map = rset.getString(1).trim()+"-"+rset1.getString(12).substring(1);
							
	//						for (int c = 0; c < customer_map.split(",").length; c++){
	//							if (customer_map.split(",")[c].contains(map)){
	//								temp = customer_map.split(",")[c];
	//							}
	//						}
							 if (mpe.customer_map.containsKey(map)) {
								 temp= mpe.customer_map.get(map); 
							    }
						}
										
						if (!cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
							row = spreadsheet.createRow(nrow++);
							count++;
							cp_plant = "";
						}
						
						
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						eff_dt = rset1.getString(4);
						
						if (mpe.customer_map.containsKey(map) && !temp.isEmpty()){
							seq_no = temp.split("-")[1];
						}
	
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
						cell = row.createCell(0);
	//					if(i == 0) {
	//						for (int c = 0; c < counterparty_map.split(",").length; c++){
	//							if (counterparty_map.split(",")[c].contains(value+";")){
	//								value = counterparty_map.split(",")[c].split(";")[1];
	//								abbr = value;
	//							}
	//						}
						
	
					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					    }
					    
				        abbr = value; 
				        
//				        if (rset.getString(1).contains("IFFCO")) {
//				        	System.out.println(">>"+abbr);
//				        }
	//					}
						cell.setCellValue("'" + value + "'");
	
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).trim().replace("'", "");
							if (i == 3) {
								if (cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
									cell.setCellValue("'" + cp_seq.get(rset1.getString(1) + rset1.getString(11)) + "'");
									
									for (int s = 0; s < rlng.length; s++) {
										if (rset1.getString(s+14).equals("Y")) {
											rlng[s] = "Y";
										}
									}
									
								} else {
									cell.setCellValue("'" + (seq_num) + "'");
									cp_seq.put(rset1.getString(1) + rset1.getString(11), ((seq_num++)+""));
	
									for (int s = 0; s < rlng.length; s++) {
										rlng[s] = rset1.getString(s+14);
									}
								}
							}
							else if (i == 6 || i == 13) {
								value = value.replaceAll("//?", "");
								value = value.replaceAll("&", "and");
	
								cell.setCellValue("'" + value + "'");
							}
							/*
							 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
							 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
							 */
							else if(i == 12) {
	
								if (!temp.isEmpty()) {
									value = "P"+temp.split("-")[0];
								}
								cp_plant = cp_plant + value + ",";
								cell.setCellValue("'" + cp_plant + "'");
							}
							else if (i == 14) {
								for (int s = 0; s < rlng.length; s++) {
									cell = row.createCell(i++);
									cell.setCellValue("'" + rlng[s] + "'");
								}
								i--;
							}
							else if (i == 34) {
								type = value;
								cell.setCellValue("'" + value + "'");
							}
							else {
								cell.setCellValue("'" + value + "'");
							}
						}
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");
					}
						
				}
				stmt1.close();
				rset1.close();

				cp_seq = new HashMap<String, String>();
				cp_plant = "";
				seq_num = 1;
				// CUSTOMER: DLNG
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'C', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.DEF_NOM_FLAG, A.DEF_INV_FLAG, A.DEF_FM_FLAG, A.DEF_PM_FLAG, A.DEF_JT_FLAG, A.DEF_OTHER_FLAG, A.ACTIVE_FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, NULL, A.DEF_INV_TO_FLAG, NULL, NULL, NULL, NULL, NULL, NULL, 'DLNG', NULL, NULL  FROM FMS7_CUSTOMER_CONTACT_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND A.ACTIVE_FLAG = 'Y' ORDER BY  A.EMAIL, A.EFF_DT ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				stmt1.setString(6, delta_FromDt);
				stmt1.setString(7, delta_FromDt);
				stmt1.setString(8, delta_ToDt);
				stmt1.setString(9, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					
					if ((rset1.getString(11) != null && !rset1.getString(11).toLowerCase().contains("shell.com"))) {
						if (!cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
							for (int s = 0; s < dlng.length; s++) {
								dlng[s] = "null";
							}
						}
//						String[] dlng = {"null", "null", "null", "null", "null", "null"};
						
						temp="";
						if (rset1.getString(12).contains("P")) {
							map = rset.getString(1).trim()+"-"+rset1.getString(12).substring(1);
							
	//						for (int c = 0; c < customer_map.split(",").length; c++){
	//							if (customer_map.split(",")[c].contains(map)){
	//								temp = customer_map.split(",")[c];
	//							}
	//						}
							 if (mpe.customer_map.containsKey(map)) {
								 temp= mpe.customer_map.get(map); 
							       
							    }
	
						}
						
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						eff_dt = rset1.getString(4);
	
						if (!cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
							row = spreadsheet.createRow(nrow++);
							count++;
							cp_plant = "";
						}
						
						if (mpe.customer_map.containsKey(map) && !temp.isEmpty()){
							seq_no = temp.split("-")[0];
						}
	
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
						cell = row.createCell(0);
	//					if(i == 0) {
	//						for (int c = 0; c < counterparty_map.split(",").length; c++){
	//							if (counterparty_map.split(",")[c].contains(value+";")){
	//								value = counterparty_map.split(",")[c].split(";")[1];
	//								abbr = value;
	//							}
	//						}
						
	
					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					       
					        abbr = value; 
					    }
					    
	//					}
						cell.setCellValue("'" + value + "'");
	
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).trim().replace("'", "");
							if (i == 3) {
								if (cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
									cell.setCellValue("'" + cp_seq.get(rset1.getString(1) + rset1.getString(11)) + "'");
									
									for (int s = 0; s < dlng.length; s++) {
										if (rset1.getString(s+14).equals("Y")) {
											dlng[s] = "Y";
										}
										if (s == 4 && dlng[0].contains("Y")) {
											dlng[s] = "Y";
										}
									}
									
								} else {
									cell.setCellValue("'" + (seq_num) + "'");
									cp_seq.put(rset1.getString(1) + rset1.getString(11), ((seq_num++)+""));
	
									for (int s = 0; s < dlng.length; s++) {
										dlng[s] = rset1.getString(s+14);
										if (s == 4 && dlng[0].contains("Y")) {
											dlng[s] = "Y";
										}
									}
								}
							}
							/*
							 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
							 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
							 */	
							else if (i == 6 || i == 13) {
								value = value.replaceAll("//?", "");
								value = value.replaceAll("&", "and");
	
								cell.setCellValue("'" + value + "'");
							}
							else if(i == 12) {
	
								if (!temp.isEmpty()) {
									value = "P"+temp.split("-")[0];
								}
								cp_plant = cp_plant + value + ",";
								cell.setCellValue("'" + cp_plant + "'");
							}
							else if (i == 14) {
								for (int s = 0; s < dlng.length; s++) {
									cell = row.createCell(i++);
									cell.setCellValue("'" + dlng[s] + "'");
								}
								i--;
							}
							else if (i == 34) {
								type = value;
								cell.setCellValue("'" + value + "'");
							}
							else {
								cell.setCellValue("'" + value + "'");
							}
						}
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");
					}

				}
				stmt1.close();
				rset1.close();

				cp_seq = new HashMap<String, String>();
				cp_plant = "";
				seq_num = 1;
				// TRADER: RLNG
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'T', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.NOM_FLAG, A.INV_FLAG, A.FM_FLAG, A.PM_FLAG, A.JT_FLAG, A.OTHER_FLAG,A.ACTIVE_FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'RLNG', NULL, NULL  FROM FMS7_TRADER_CONTACT_MST A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND A.ACTIVE_FLAG = 'Y' ORDER BY A.EMAIL, A.EFF_DT  ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				stmt1.setString(6, delta_FromDt);
				stmt1.setString(7, delta_FromDt);
				stmt1.setString(8, delta_ToDt);
				stmt1.setString(9, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
						
					if ((rset1.getString(11) != null && !rset1.getString(11).toLowerCase().contains("shell.com"))) {
						temp="";

						if (!cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
							for (int s = 0; s < rlng.length; s++) {
								rlng[s] = "null";
							}
						}
//						String[] rlng = {"null", "null", "null", "null", "null", "null"};
						
						if (rset1.getString(12).contains("P")) {
							map = rset.getString(1).trim()+"-"+rset1.getString(12).substring(1);
							
	//						for (int c = 0; c <  ̣.split(",").length; c++){
	//							if (trader_map.split(",")[c].contains(map)){
	//								temp = trader_map.split(",")[c];
	//							}
	//						}
							
							 if (mpe.trader_map.containsKey(map)) 
							 {
								 temp= mpe.trader_map.get(map);     
							 }
						
						}
	
						if (!cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
							row = spreadsheet.createRow(nrow++);
							count++;
							cp_plant = "";
						}
						
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						eff_dt = rset1.getString(4);
						
						if (mpe.trader_map.containsKey(map) && !temp.isEmpty()) {
							seq_no = temp.split("-")[0];
						
						}
						
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
						
						cell = row.createCell(0);
	//					if(i == 0) {
	//						for (int c = 0; c < counterparty_map.split(",").length; c++){
	//							if (counterparty_map.split(",")[c].contains(value+";")){
	//								value = counterparty_map.split(",")[c].split(";")[1];
	//								abbr = value;
	//							}
	//						}
						
	
					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					      
					        abbr = value; 
					    }
	//					}
						cell.setCellValue("'" + value + "'");
	
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).trim().replace("'", "");
							if (i == 3) {
								if (cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
									cell.setCellValue("'" + cp_seq.get(rset1.getString(1) + rset1.getString(11)) + "'");
									
									for (int s = 0; s < rlng.length; s++) {
										if (rset1.getString(s+14).equals("Y")) {
											rlng[s] = "Y";
										}
									}
									
								} else {
									cell.setCellValue("'" + (seq_num) + "'");
									cp_seq.put(rset1.getString(1) + rset1.getString(11), ((seq_num++)+""));
	
									for (int s = 0; s < rlng.length; s++) {
										rlng[s] = rset1.getString(s+14);
									}
								}
							}
							/*
							 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
							 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
							 */
							else if (i == 6 || i == 13) {
								value = value.replaceAll("//?", "");
								value = value.replaceAll("&", "and");
	
								cell.setCellValue("'" + value + "'");
							}
							else if(i == 12) {
	
								if (!temp.isEmpty()) {
									value = "P"+temp.split(";")[1].split("-")[0];
								}
								cp_plant = cp_plant + value + ",";
								cell.setCellValue("'" + cp_plant + "'");
							}
							else if (i == 14) {
								for (int s = 0; s < rlng.length; s++) {
									cell = row.createCell(i++);
									cell.setCellValue("'" + rlng[s] + "'");
								}
								i--;
							}
							else if (i == 34) {
								type = value;
								cell.setCellValue("'" + value + "'");
							}
							else {
								cell.setCellValue("'" + value + "'");
							}
						}
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");
					}

				}
				stmt1.close();
				rset1.close();
				
				// TRADER: DLNG
//				queryString1 = "SELECT B.COUNTERPARTY_CD, 'T', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.DEF_NOM_FLAG, A.DEF_INV_FLAG, A.DEF_FM_FLAG, A.DEF_PM_FLAG, A.DEF_JT_FLAG, A.DEF_OTHER_FLAG, A.ACTIVE_FLAG , TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'DLNG'  FROM FMS7_TRADER_CONTACT_MST A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND A.ACTIVE_FLAG = 'Y' ORDER BY A.TRADER_CD, UPPER(A.CONTACT_PERSON), A.SEQ_NO ";
//				stmt1 = conn.prepareStatement(queryString1);
//				stmt1.setString(1, rset.getString(2));
//				stmt1.setString(2, delta_FromDt);
//				stmt1.setString(3, delta_FromDt);
//				stmt1.setString(4, delta_ToDt);
//				stmt1.setString(5, delta_ToDt);
//				stmt1.setString(6, delta_FromDt);
//				stmt1.setString(7, delta_FromDt);
//				stmt1.setString(8, delta_ToDt);
//				stmt1.setString(9, delta_ToDt);
//				rset1 = stmt1.executeQuery();
//
//				while (rset1.next()) {
//					
//					temp="";
//					
//					if (rset1.getString(12).contains("P")) {
//						map = rset.getString(1).trim()+"-"+rset1.getString(12).substring(1);
//						
//						for (int c = 0; c < trader_map.split(",").length; c++){
//							if (trader_map.split(",")[c].contains(map)){
//								temp = trader_map.split(",")[c];
//							}
//						}
//					}
//
//					row = spreadsheet.createRow(nrow++);
//					entity = rset1.getString(2);
//					seq_no = rset1.getString(3);
//					eff_dt = rset1.getString(4);
//					
//					if (trader_map.contains(map) && !temp.isEmpty()) {
//						seq_no = temp.split(";")[1].split("-")[0];
//					}
//					
//					value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
//					
//					cell = row.createCell(0);
////					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}
////					}
//					cell.setCellValue("'" + value + "'");
//
//					for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(i);
//						value = rset1.getString(i) == null ? "null" : rset1.getString(i).trim().replace("'", "");
//						if (i == 3) {
//							if (cp_seq.containsKey(rset1.getString(1) + rset1.getString(5).toUpperCase())) {
//								cell.setCellValue("'" + cp_seq.get(rset1.getString(1) + rset1.getString(5).toUpperCase()) + "'");
//							} else {
//								cell.setCellValue("'" + value + "'");
//								cp_seq.put(rset1.getString(1) + rset1.getString(5).toUpperCase(), value);
//							}
//						}
//						/*
//						 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
//						 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
//						 */
//						else if(i == 12 && !temp.isEmpty()) {
//							value = "P"+temp.split(";")[1].split("-")[0];
//							cell.setCellValue("'" + value + "'");
//						}
//						else if (i == 34) {
//							type = value;
//							cell.setCellValue("'" + value + "'");
//						}
//						else {
//							cell.setCellValue("'" + value + "'");
//						}
//					}
//					count++;
//					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");
//
//				}
//				stmt1.close();
//				rset1.close();

				cp_seq = new HashMap<String, String>();
				cp_plant = "";
				seq_num = 1;
				// TRANSPORTER: RLNG
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'R', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.NOM_FLAG, A.INV_FLAG, A.FM_FLAG, A.PM_FLAG, A.JT_FLAG, A.OTHER_FLAG,A.ACTIVE_FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'RLNG', NULL, NULL  FROM FMS7_TRANSPORTER_CONTACT_MST A, FMS7_TRANSPORTER_MST B WHERE A.TRANSPORTER_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND A.ACTIVE_FLAG = 'Y' ORDER BY   A.EMAIL, A.EFF_DT  ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				stmt1.setString(6, delta_FromDt);
				stmt1.setString(7, delta_FromDt);
				stmt1.setString(8, delta_ToDt);
				stmt1.setString(9, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					
					if ((rset1.getString(11) != null && !rset1.getString(11).toLowerCase().contains("shell.com"))) {
//						String[] rlng = {"null", "null", "null", "null", "null", "null"};

						if (!cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
							for (int s = 0; s < rlng.length; s++) {
								rlng[s] = "null";
							}
						}
						
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						eff_dt = rset1.getString(4);
						
						map=map.split("-")[0];
						if (!cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
							row = spreadsheet.createRow(nrow++);
							count++;
							cp_plant = "";
						}
						temp="";
						
	//					for (int c = 0; c < transporter_map.split(",").length; c++){
	//						if (transporter_map.split(",")[c].contains(map)){
	//							temp = transporter_map.split(",")[c];
	//						}
	//					}
						if (mpe.transporter_map.containsKey(map)) {
							temp = mpe.transporter_map.get(map);
						}
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
						
						cell = row.createCell(0);
	//					if(i == 0) {
	//						for (int c = 0; c < counterparty_map.split(",").length; c++){
	//							if (counterparty_map.split(",")[c].contains(value+";")){
	//								value = counterparty_map.split(",")[c].split(";")[1];
	//								abbr = value;
	//							}
	//						}
						
	
					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					       
					        abbr = value; 
					    }
	//					}
						cell.setCellValue("'" + value + "'");
	
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).trim().replace("'", "");
							if (i == 3) {
								if (cp_seq.containsKey(rset1.getString(1) + rset1.getString(11))) {
									cell.setCellValue("'" + cp_seq.get(rset1.getString(1) + rset1.getString(11)) + "'");
									
									for (int s = 0; s < rlng.length; s++) {
										if (rset1.getString(s+14).equals("Y")) {
											rlng[s] = "Y";
										}
									}
									
								} else {
									cell.setCellValue("'" + (seq_num) + "'");
									cp_seq.put(rset1.getString(1) + rset1.getString(11), ((seq_num++)+""));
	
									for (int s = 0; s < rlng.length; s++) {
										rlng[s] = rset1.getString(s+14);
									}
								}
							}
							else if (i == 14) {
								for (int s = 0; s < rlng.length; s++) {
									cell = row.createCell(i++);
									cell.setCellValue("'" + rlng[s] + "'");
								}
								i--;
							}
							else if(i == 12) {
	
								if (!temp.isEmpty()) {
									value = "P"+temp.split(";")[1].split("-")[0];
								}
								cp_plant = cp_plant + value + ",";
								cell.setCellValue("'" + cp_plant + "'");
							}
							/*
							 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
							 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
							 */
							else if (i == 6 || i == 13) {
								value = value.replaceAll("//?", "");
								value = value.replaceAll("&", "and");
	
								cell.setCellValue("'" + value + "'");
							}
							else if (i == 34) {
								type = value;
								cell.setCellValue("'" + value + "'");
							}
							else {
								cell.setCellValue("'" + value + "'");
							}
						}
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");
					}

				}
				stmt1.close();
				rset1.close();
				
				// TRANSPORTER: DLNG
//				queryString1 = "SELECT B.COUNTERPARTY_CD, 'R', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.DEF_NOM_FLAG, A.DEF_INV_FLAG, A.DEF_FM_FLAG, A.DEF_PM_FLAG, A.DEF_JT_FLAG, A.DEF_OTHER_FLAG,A.ACTIVE_FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'DLNG'  FROM FMS7_TRANSPORTER_CONTACT_MST A, FMS7_TRANSPORTER_MST B WHERE A.TRANSPORTER_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND A.ACTIVE_FLAG = 'Y' ORDER BY A.TRANSPORTER_CD, UPPER(A.CONTACT_PERSON), A.SEQ_NO ";
//				stmt1 = conn.prepareStatement(queryString1);
//				stmt1.setString(1, rset.getString(2));
//				stmt1.setString(2, delta_FromDt);
//				stmt1.setString(3, delta_FromDt);
//				stmt1.setString(4, delta_ToDt);
//				stmt1.setString(5, delta_ToDt);
//				stmt1.setString(6, delta_FromDt);
//				stmt1.setString(7, delta_FromDt);
//				stmt1.setString(8, delta_ToDt);
//				stmt1.setString(9, delta_ToDt);
//				rset1 = stmt1.executeQuery();
//
//				while (rset1.next()) {
//					entity = rset1.getString(2);
//					seq_no = rset1.getString(3);
//					eff_dt = rset1.getString(4);
//					row = spreadsheet.createRow(nrow++);
//					
//					if (transporter_map.contains(map)) {
//						seq_no = temp.split(";")[1].split("-")[0];
//					}
//
//					value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
//					
//					cell = row.createCell(0);
////					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}
////					}
//					cell.setCellValue("'" + value + "'");
//
//					for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(i);
//						value = rset1.getString(i) == null ? "null" : rset1.getString(i).trim().replace("'", "");
//						if (i == 3) {
//  					if (cp_seq.containsKey(rset1.getString(1) + rset1.getString(5).toUpperCase() + rset1.getString(3))) {
//								cell.setCellValue("'" + cp_seq.get(rset1.getString(1) + rset1.getString(5).toUpperCase() + rset1.getString(3)) + "'");
//							} else {
//								cell.setCellValue("'" + value + "'");
//							cp_seq.put(rset1.getString(1) + rset1.getString(5).toUpperCase() + rset1.getString(3), value);
//							}
//						}
//						/*
//						 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
//						 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
//						 */
//						else if (i == 34) {
//							type = value;
//							cell.setCellValue("'" + value + "'");
//						}
//						else {
//							cell.setCellValue("'" + value + "'");
//						}
//					}
//					count++;
//					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");
//
//				}
//				stmt1.close();
//				rset1.close();
//
			}
			stmt.close();
			rset.close();
			
			// Below block of code is for inserting data (CHA)
			queryString = "SELECT DISTINCT(B.CHA_ABBR), B.CHA_CD, 'H', A.CONTACT_TYPE, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL_1, 'R', NULL, 'N', 'N', 'N', 'N', 'N', 'N', 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'RLNG', NULL, NULL FROM FMS7_CUS_HUS_AGT_CONTACT_MST A, FMS7_CUS_HOUSE_AGENT_MST B WHERE A.CHA_CD = B.CHA_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUS_HOUSE_AGENT_MST C WHERE B.CHA_CD = C.CHA_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.CHA_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {
				abbr = rset.getString(1).trim();
				cd = rset.getString(2);
				entity = rset.getString(3);
				eff_dt = rset.getString(5);
				row = spreadsheet.createRow(nrow++);

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
					
					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}

					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					       
					        abbr = value; 
					    }
						cell.setCellValue("'" + value + "'");
					}
					else if (i == 3) {
						value = (value.equalsIgnoreCase("B") ? "2" : "1");
						cell.setCellValue("'" + value + "'");
						seq_no = value;
					}
					/*
					 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else if (i == 34) {
						type = value;
						cell.setCellValue("'" + value + "'");
					}
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (VESSEL AGENT)
			queryString = "SELECT DISTINCT(B.VESSEL_ABBR), B.VESSEL_CD, 'V', A.CONTACT_TYPE, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL_1, 'R', NULL, 'N', 'N', 'N', 'N', 'N', 'N', 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'RLNG', NULL, NULL FROM FMS7_VESSEL_AGENT_CONTACT_MST A, FMS7_VESSEL_AGENT_MST B WHERE A.VESSEL_CD = B.VESSEL_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_VESSEL_AGENT_MST C WHERE B.VESSEL_CD = C.VESSEL_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.VESSEL_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {
				abbr = rset.getString(1).trim();
				cd = rset.getString(2);
				entity = rset.getString(3);
				eff_dt = rset.getString(5);
				
				row = spreadsheet.createRow(nrow++);

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
					
					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}

					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					       
					        abbr = value; 
					    }
						cell.setCellValue("'" + value + "'");
						
					}
					else if (i == 3) {
						value = (value.equalsIgnoreCase("B") ? "2" : "1");
						cell.setCellValue("'" + value + "'");
						seq_no = value;
					}
					/*
					 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else if (i == 34) {
						type = value;
						cell.setCellValue("'" + value + "'");
					}
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (SURVEYOR)
			queryString = "SELECT DISTINCT(B.SURVEYOR_ABBR), B.SURVEYOR_CD, 'S', A.CONTACT_TYPE, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL_1, 'R', NULL, 'N', 'N', 'N', 'N', 'N', 'N', 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'RLNG', NULL, NULL FROM FMS7_SURVEYOR_CONTACT_MST A, FMS7_SURVEYOR_MST B WHERE A.SURVEYOR_CD = B.SURVEYOR_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_SURVEYOR_MST C WHERE B.SURVEYOR_CD = C.SURVEYOR_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.SURVEYOR_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {

				row = spreadsheet.createRow(nrow++);
				abbr = rset.getString(1).trim();
				cd = rset.getString(2);
				entity = rset.getString(3);
				eff_dt = rset.getString(5);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
					
					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}

					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					       
					        abbr = value; 
					    }
						cell.setCellValue("'" + value + "'");
					}
					else if (i == 3) {
						value = (value.equalsIgnoreCase("B") ? "2" : "1");
						cell.setCellValue("'" + value + "'");
						seq_no = value;
					}
					/*
					 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else if (i == 34) {
						type = value;
						cell.setCellValue("'" + value + "'");
					}
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");

			}
			stmt.close();
			rset.close();

			seq_num = 1;
			// Below block of code is for inserting data (BUSINESS OWNER): RLNG
			cp_seq = new HashMap<String, String>();
			cp_plant = "";
			
			String[] rlng = {"null", "null", "null", "null", "null", "null"};
			
			queryString = "SELECT B.SUPPLIER_ABBR, B.COUNTERPARTY_CD, 'B', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.NOM_FLAG, A.INV_FLAG, A.FM_FLAG, A.PM_FLAG, A.JT_FLAG, A.OTHER_FLAG,A.ACTIVE_FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, NULL, NULL, NULL, NULL, NULL, NULL, A.REMITTANCE_FLAG, NULL, 'RLNG', 'N', 'N'  FROM FMS7_SUPPLIER_CONTACT_MST A, FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD AND UPPER(B.SUPPLIER_ABBR) = 'SEIPL' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND A.ACTIVE_FLAG = 'Y' ORDER BY A.EMAIL, A.EFF_DT  ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next() && rset.getString(12) != null) {
				
//				String[] rlng = {"null", "null", "null", "null", "null", "null"};

				if (!cp_seq.containsKey(rset.getString(12))) {
					for (int s = 0; s < rlng.length; s++) {
						rlng[s] = "null";
					}
				}
				
				temp="";
				map = rset.getString(1).trim()+"-"+rset.getString(4);

				String oInvCC= "",oInvBCC="",dInvCC="",dInvBCC="",tInvCC="",tInvBCC="",dInvTO="",tInvTO="", F402_TO = "", F402_CC = "", F402_BCC = "";
				
//				for (int c = 0; c < supplier_map.split(",").length; c++){
//					if (supplier_map.split(",")[c].contains(map)){
//						temp = supplier_map.split(",")[c];
//						//System.out.println(temp);
//					}
//				}
				
				abbr = rset.getString(1).trim();
				cd = rset.getString(2);
				entity = rset.getString(3);
				seq_no = rset.getString(4);
				eff_dt = rset.getString(5);
				
				if (!cp_seq.containsKey(rset.getString(12))) {
					row = spreadsheet.createRow(nrow++);
					count++;
					cp_plant = "";
				}
				
//				if (supplier_map.contains(map)) {
//					seq_no = temp.split(";")[1].split("-")[0];
//				}	

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
					if (i == 3) {
						if (cp_seq.containsKey(rset.getString(12))) {
							cell.setCellValue("'" + cp_seq.get(rset.getString(12)) + "'");
							
							for (int s = 0; s < rlng.length; s++) {
								if (rset.getString(s+15).equals("Y")) {
									rlng[s] = "Y";
								}
							}
						} else {
							cell.setCellValue("'" + (seq_num) + "'");
							cp_seq.put(rset.getString(12), ((seq_num++)+""));

							for (int s = 0; s < rlng.length; s++) {
								rlng[s] = rset.getString(s+15);
							}
						}
					}

					else if (i == 6 || i == 13) {
						value = value.replaceAll("//?", "");
						value = value.replaceAll("&", "and");

						cell.setCellValue("'" + value + "'");
					}
					/*
					 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else if(i == 12) {

						if (value.contains("P")) {
							value = "P"+(Integer.parseInt(value.substring(1))+1);
//							value = "";
						}
						
						if (rset.getString(12).contains("GX-HAZIRA-Commercial@shell.com") || rset.getString(12).contains("GXHAZIRAFN-SEIINV@shell.com")) {
							value += ",P1,P2,P3";
						}
						
						if (!value.equals("")) {
							cp_plant = cp_plant + value + ",";
						}
						cell.setCellValue("'" + cp_plant + "'");
					}
					else if (i == 14) {
						for (int s = 0; s < rlng.length; s++) {
							cell = row.createCell(i++);
							cell.setCellValue("'" + rlng[s] + "'");
						}
						i--;
					}
					else if (i == 27) {
						//queryString1 = "SELECT NVL(T_INV_TO) FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'RLNG' ";
						queryString1 = "SELECT O_INV_CC,O_INV_BCC,D_INV_CC,D_INV_BCC,T_INV_CC,T_INV_BCC,D_INV_TO,T_INV_TO, FORM402_TO, FORM402_CC, FORM402_BCC FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'RLNG' ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(4));
						rset1 = stmt1.executeQuery();
						
						while (rset1.next() ) {							
						    oInvCC = rset1.getString(1);
						    oInvBCC = rset1.getString(2);
						    dInvCC = rset1.getString(3);
						    dInvBCC = rset1.getString(4);
						    tInvCC = rset1.getString(5);
						    tInvBCC = rset1.getString(6);
						    dInvTO = rset1.getString(7);
						    tInvTO = rset1.getString(8);	
						    F402_TO = rset1.getString(9);
						    F402_CC = rset1.getString(10);
						    F402_BCC = rset1.getString(11);
						}
						if ("Y".equals(dInvTO) || "Y".equals(tInvTO)) {
							cell.setCellValue("'Y'");
						}
						else if ("Y".equals(oInvCC) || "Y".equals(dInvCC) || "Y".equals(tInvCC)) {
							cell.setCellValue("'N'");
						}
						else if ("Y".equals(oInvBCC) || "Y".equals(dInvBCC)|| "Y".equals(tInvBCC)) {
							cell.setCellValue("'B'");
						} 
						else {
							cell.setCellValue("'null'");
						}
						rset1.close();
						stmt1.close();
					}
					else if (i == 34) {
						type = value;
						cell.setCellValue("'" + value + "'");
					} 
					else if (i == 35 && (F402_TO.equals("Y") || F402_CC.equals("Y") || F402_BCC.equals("Y"))) {
						cell.setCellValue("'Y'");
					}
					else if (i == 36) {
						if ("Y".equals(F402_TO)) {
							cell.setCellValue("'Y'");
						}
						else if ("Y".equals(F402_CC)) {
							cell.setCellValue("'N'");
						}
						else if ("Y".equals(F402_BCC)) {
							cell.setCellValue("'B'");
						} 
						else {
							cell.setCellValue("'null'");
						}
					}
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");

			}
			stmt.close();
			rset.close();
			

			seq_num = 1;
			// Below block of code is for inserting data (BUSINESS OWNER): DLNG
			cp_seq = new HashMap<String, String>();
			cp_plant = "";
			String[] dlng = {"null", "null", "null", "null", "null", "null"};
			
			queryString = "SELECT B.SUPPLIER_ABBR, B.COUNTERPARTY_CD, 'B', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.DEF_NOM_FLAG, A.DEF_INV_FLAG, A.DEF_FM_FLAG, A.DEF_PM_FLAG, A.DEF_JT_FLAG, A.DEF_OTHER_FLAG, A.ACTIVE_FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, NULL, NULL, NULL, NULL, NULL, NULL, A.DLNG_REMITTANCE_FLAG, NULL, 'DLNG', 'N', 'N'  FROM FMS7_SUPPLIER_CONTACT_MST A, FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD AND UPPER(B.SUPPLIER_ABBR) = 'SEIPL' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND A.ACTIVE_FLAG = 'Y' ORDER BY   A.EMAIL, A.EFF_DT  ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next() && rset.getString(12) != null) {
				
//				String[] dlng = {"null", "null", "null", "null", "null", "null"};

				if (!cp_seq.containsKey(rset.getString(12))) {
					for (int s = 0; s < dlng.length; s++) {
						dlng[s] = "null";
					}
				}
				
				temp="";
				map = rset.getString(1).trim()+"-"+rset.getString(4);
				
//				for (int c = 0; c < supplier_map.split(",").length; c++){
//					if (supplier_map.split(",")[c].contains(map)){
//						temp = supplier_map.split(",")[c];
//						//System.out.println(temp);
//					}
//				}

				String oInvCC= "",oInvBCC="",dInvCC="",dInvBCC="",tInvCC="",tInvBCC="",dInvTO="",tInvTO="", F402_TO = "", F402_CC = "", F402_BCC = "";
				
				abbr = rset.getString(1).trim();
				cd = rset.getString(2);
				entity = rset.getString(3);
				seq_no = rset.getString(4);
				eff_dt = rset.getString(5);
				
				if (!cp_seq.containsKey(rset.getString(12))) {
					row = spreadsheet.createRow(nrow++);
					count++;
					cp_plant = "";
				}
				
//				if (supplier_map.contains(map)) {
//					seq_no = temp.split(";")[1].split("-")[0];
//				}	

	
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
					if (i == 3) {
						if (cp_seq.containsKey(rset.getString(12))) {
							cell.setCellValue("'" + cp_seq.get(rset.getString(12)) + "'");
							
							for (int s = 0; s < dlng.length; s++) {
								if (rset.getString(s+15).equals("Y")) {
									dlng[s] = "Y";
								}
								if (s == 4 && dlng[0].contains("Y")) {
									dlng[s] = "Y";
								}
							}
						} else {
							cell.setCellValue("'" + (seq_num) + "'");
							cp_seq.put(rset.getString(12), ((seq_num++)+""));

							for (int s = 0; s < dlng.length; s++) {
								dlng[s] = rset.getString(s+15);
								if (s == 4 && dlng[0].contains("Y")) {
									dlng[s] = "Y";
								}
							}
						}
					}
					
					else if (i == 6 || i == 13) {
						value = value.replaceAll("//?", "");
						value = value.replaceAll("&", "and");

						cell.setCellValue("'" + value + "'");
					}
					/*
					* else if (i == 22 && !value.equals("null")) { // emp_abbr value =
					* getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					*/
					else if(i == 12) {

						if (value.contains("P")) {
							value = "P"+(Integer.parseInt(value.substring(1))+1);
//							value = "";
						}
						
						if (rset.getString(12).contains("GX-HAZIRA-Commercial@shell.com") || rset.getString(12).contains("GXHAZIRAFN-SEIINV@shell.com")) {
							value += ",P1,P2,P3";
						}
						
						if (!value.equals("")) {
							cp_plant = cp_plant + value + ",";
						}
						cell.setCellValue("'" + cp_plant + "'");
					}
					else if (i == 14) {
						for (int s = 0; s < dlng.length; s++) {
							cell = row.createCell(i++);
							if ((i == 15 || i == 16 || i == 19) && rset.getString(12).contains("GX-HAZIRA-Commercial@shell.com")) {
								dlng[s] = "Y";
							}
							cell.setCellValue("'" + dlng[s] + "'");
						}
						i--;
					}
					else if (i == 27) {
						//queryString1 = "SELECT NVL(T_INV_TO) FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'RLNG' ";
						queryString1 = "SELECT O_INV_CC,O_INV_BCC,D_INV_CC,D_INV_BCC,T_INV_CC,T_INV_BCC,D_INV_TO,T_INV_TO, FORM402_TO, FORM402_CC, FORM402_BCC FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'DLNG' ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(4));
						rset1 = stmt1.executeQuery();
						
						while (rset1.next() ) {							
						    oInvCC = rset1.getString(1);
						    oInvBCC = rset1.getString(2);
						    dInvCC = rset1.getString(3);
						    dInvBCC = rset1.getString(4);
						    tInvCC = rset1.getString(5);
						    tInvBCC = rset1.getString(6);
						    dInvTO = rset1.getString(7);
						    tInvTO = rset1.getString(8);	
						    F402_TO = rset1.getString(9);
						    F402_CC = rset1.getString(10);
						    F402_BCC = rset1.getString(11);
						}
						
						if ("Y".equals(dInvTO) || "Y".equals(tInvTO)) {
							cell.setCellValue("'Y'");
						}
						else if ("Y".equals(oInvCC) || "Y".equals(dInvCC) || "Y".equals(tInvCC)) {
							cell.setCellValue("'N'");
						}
						else if ("Y".equals(oInvBCC) || "Y".equals(dInvBCC)|| "Y".equals(tInvBCC)) {
							cell.setCellValue("'B'");
						} 
						else {
							cell.setCellValue("'null'");
						}
						rset1.close();
						stmt1.close();
					} 
					else if (i == 34) {
						type = value;
						cell.setCellValue("'" + value + "'");
					} 
					else if (i == 35 && (F402_TO.equals("Y") || F402_CC.equals("Y") || F402_BCC.equals("Y"))) {
						cell.setCellValue("'Y'");
					}
					else if (i == 36) {
						if ("Y".equals(F402_TO)) {
							cell.setCellValue("'Y'");
						}
						else if ("Y".equals(F402_CC)) {
							cell.setCellValue("'N'");
						}
						else if ("Y".equals(F402_BCC)) {
							cell.setCellValue("'B'");
						} 
						else {
							cell.setCellValue("'null'");
						}
					}
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");

			}
			stmt.close();
			rset.close();
			

			
			
			// SagarB20250610 Added Static contact persons for created trader plants named 'GX HAZIRA COMMERCIAL' from SEIPL Business Owner as per discussed by Vijay on 20250610 : RLNG
			String[] traders = {"GUNSPL", "TGPL", "SELNG", "SITME", "SPSSETL1", "SIETCO", "SILS", "SEMTEXP", "SITMEFZ", "AMNS", "BP", "BPCL", "CAIRENH", "DEEPENER", "DIL", "EFSPL", "ESSAREX", "HOECLTD", "HPCL", "IGSPL", "MGRPL", "ONGC", "RIL", "SANENER", "SEMTIPL", "VEDALTD"};
			String[] trader_plant = {"P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,", "P1,P2,", "P1,", "P1,", "P1,P3,P4,P5,", "P1,", "P1,P2,P3,P4,P5", "P1,"};
			
			queryString = "SELECT B.SUPPLIER_ABBR, 0, 'T', 1, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, 'P1', A.ADDL_ADDR_LINE, A.NOM_FLAG, A.INV_FLAG, A.FM_FLAG, A.PM_FLAG, A.JT_FLAG, A.OTHER_FLAG, A.ACTIVE_FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, NULL, NULL, NULL, NULL, NULL, NULL, A.REMITTANCE_FLAG, NULL, 'RLNG', 'N', 'N'  FROM FMS7_SUPPLIER_CONTACT_MST A, FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD AND UPPER(B.SUPPLIER_ABBR) = 'SEIPL' AND  A.CONTACT_PERSON = 'GX HAZIRA COMMERCIAL'  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				
				for (int k = 0; k < traders.length; k++) {
					
//					String[] dlng = {"null", "null", "null", "null", "null", "null"};
						for (int s = 0; s < dlng.length; s++) {
							dlng[s] = "null";
						}
					
					temp="";
					map = rset.getString(1).trim()+"-"+rset.getString(4);
					
//								for (int c = 0; c < supplier_map.split(",").length; c++){
//									if (supplier_map.split(",")[c].contains(map)){
//										temp = supplier_map.split(",")[c];
//										//System.out.println(temp);
//									}
//								}

					String oInvCC= "",oInvBCC="",dInvCC="",dInvBCC="",tInvCC="",tInvBCC="",dInvTO="",tInvTO="", F402_TO = "", F402_CC = "", F402_BCC = "";
					
					abbr = traders[k];
					cd = rset.getString(2);
					entity = rset.getString(3);
					seq_no = rset.getString(4);
					eff_dt = rset.getString(5);
					
						row = spreadsheet.createRow(nrow++);
						cp_plant = "";
					
//								if (supplier_map.contains(map)) {
//									seq_no = temp.split(";")[1].split("-")[0];
//								}	

		
					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
						
						
						if (i == 0) {
							cell.setCellValue("'" +abbr+ "'");
						}
						else if (i == 3) {
							if (cp_seq.containsKey(rset.getString(12))) {
								cell.setCellValue("'" + value + "'");
								
								for (int s = 0; s < dlng.length; s++) {
									if (rset.getString(s+15).equals("Y")) {
										dlng[s] = "Y";
									}
								}
							} else {
								cell.setCellValue("'" + value + "'");
								cp_seq.put(rset.getString(12), ((seq_num++)+""));

								for (int s = 0; s < dlng.length; s++) {
									dlng[s] = rset.getString(s+15);
								}
							}
						}
						
						else if (i == 6 || i == 13) {
							value = value.replaceAll("//?", "");
							value = value.replaceAll("&", "and");

							cell.setCellValue("'" + value + "'");
						}
						/*
						* else if (i == 22 && !value.equals("null")) { // emp_abbr value =
						* getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
//						*/
						else if(i == 12) {

							cell.setCellValue("'" + trader_plant[k] + "'");
						}
						else if (i == 14) {
							for (int s = 0; s < dlng.length; s++) {
								cell = row.createCell(i++);
								cell.setCellValue("'" + dlng[s] + "'");
							}
							i--;
						}
						else if (i == 27) {
							//queryString1 = "SELECT NVL(T_INV_TO) FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'RLNG' ";
							queryString1 = "SELECT O_INV_CC,O_INV_BCC,D_INV_CC,D_INV_BCC,T_INV_CC,T_INV_BCC,D_INV_TO,T_INV_TO, FORM402_TO, FORM402_CC, FORM402_BCC FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'DLNG' ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, rset.getString(4));
							rset1 = stmt1.executeQuery();
							
							while (rset1.next() ) {							
							    oInvCC = rset1.getString(1);
							    oInvBCC = rset1.getString(2);
							    dInvCC = rset1.getString(3);
							    dInvBCC = rset1.getString(4);
							    tInvCC = rset1.getString(5);
							    tInvBCC = rset1.getString(6);
							    dInvTO = rset1.getString(7);
							    tInvTO = rset1.getString(8);	
							    F402_TO = rset1.getString(9);
							    F402_CC = rset1.getString(10);
							    F402_BCC = rset1.getString(11);
							}
							if ("Y".equals(dInvTO) || "Y".equals(tInvTO)) {
								cell.setCellValue("'Y'");
							}
							else if ("Y".equals(oInvCC) || "Y".equals(dInvCC) || "Y".equals(tInvCC)) {
								cell.setCellValue("'N'");
							}
							else if ("Y".equals(oInvBCC) || "Y".equals(dInvBCC)|| "Y".equals(tInvBCC)) {
								cell.setCellValue("'B'");
							} 
							else {
								cell.setCellValue("'null'");
							}
							rset1.close();
							stmt1.close();
						} 
						else if (i == 34) {
							type = value;
							cell.setCellValue("'" + value + "'");
						} 
						else if (i == 35 && (F402_TO.equals("Y") || F402_CC.equals("Y") || F402_BCC.equals("Y"))) {
							cell.setCellValue("'Y'");
						}
						else if (i == 36) {
							if ("Y".equals(F402_TO)) {
								cell.setCellValue("'Y'");
							}
							else if ("Y".equals(F402_CC)) {
								cell.setCellValue("'N'");
							}
							else if ("Y".equals(F402_BCC)) {
								cell.setCellValue("'B'");
							} 
							else {
								cell.setCellValue("'null'");
							}
						}
						else {
							cell.setCellValue("'" + value + "'");
						}
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");

					
				}
				
			}
			rset.close();
			stmt.close();
			
			
			// SagarB20250610 Added Static contact persons for created trader plants named 'GX HAZIRA COMMERCIAL' from SEIPL Business Owner as per discussed by Vijay on 20250610 : DLNG
			queryString = "SELECT B.SUPPLIER_ABBR, 0, 'T', 1, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, 'P1', A.ADDL_ADDR_LINE, A.DEF_NOM_FLAG, A.DEF_INV_FLAG, A.DEF_FM_FLAG, A.DEF_PM_FLAG, A.DEF_JT_FLAG, A.DEF_OTHER_FLAG, A.ACTIVE_FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, NULL, NULL, NULL, NULL, NULL, NULL, A.DLNG_REMITTANCE_FLAG, NULL, 'DLNG', 'N', 'N'  FROM FMS7_SUPPLIER_CONTACT_MST A, FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD AND UPPER(B.SUPPLIER_ABBR) = 'SEIPL' AND  A.CONTACT_PERSON = 'GX HAZIRA COMMERCIAL'  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				
				for (int k = 0; k < traders.length; k++) {
					
//					String[] dlng = {"null", "null", "null", "null", "null", "null"};

						for (int s = 0; s < dlng.length; s++) {
							dlng[s] = "null";
						}
					
					temp="";
					map = rset.getString(1).trim()+"-"+rset.getString(4);
					
//					for (int c = 0; c < supplier_map.split(",").length; c++){
//						if (supplier_map.split(",")[c].contains(map)){
//							temp = supplier_map.split(",")[c];
//							//System.out.println(temp);
//						}
//					}

					String oInvCC= "",oInvBCC="",dInvCC="",dInvBCC="",tInvCC="",tInvBCC="",dInvTO="",tInvTO="", F402_TO = "", F402_CC = "", F402_BCC = "";
					
					abbr = traders[k];
					cd = rset.getString(2);
					entity = rset.getString(3);
					seq_no = rset.getString(4);
					eff_dt = rset.getString(5);
					
						row = spreadsheet.createRow(nrow++);
						cp_plant = "";
					
//					if (supplier_map.contains(map)) {
//						seq_no = temp.split(";")[1].split("-")[0];
//					}	

		
					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
						
						
						if (i == 0) {
							cell.setCellValue("'" +abbr+ "'");
						}
						else if (i == 3) {
							if (cp_seq.containsKey(rset.getString(12))) {
								cell.setCellValue("'" + value + "'");
								
								for (int s = 0; s < dlng.length; s++) {
									if (rset.getString(s+15).equals("Y")) {
										dlng[s] = "Y";
									}
								}
							} else {
								cell.setCellValue("'" + value + "'");
								cp_seq.put(rset.getString(12), ((seq_num++)+""));

								for (int s = 0; s < dlng.length; s++) {
									dlng[s] = rset.getString(s+15);
								}
							}
						}
						
						else if (i == 6 || i == 13) {
							value = value.replaceAll("//?", "");
							value = value.replaceAll("&", "and");

							cell.setCellValue("'" + value + "'");
						}
						/*
						* else if (i == 22 && !value.equals("null")) { // emp_abbr value =
						* getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						*/
						else if(i == 12) {

							cell.setCellValue("'" + trader_plant[k] + "'");
						}
						else if (i == 14) {
							for (int s = 0; s < dlng.length; s++) {
								cell = row.createCell(i++);
								cell.setCellValue("'" + dlng[s] + "'");
							}
							i--;
						}
						else if (i == 27) {
							//queryString1 = "SELECT NVL(T_INV_TO) FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'RLNG' ";
							queryString1 = "SELECT O_INV_CC,O_INV_BCC,D_INV_CC,D_INV_BCC,T_INV_CC,T_INV_BCC,D_INV_TO,T_INV_TO, FORM402_TO, FORM402_CC, FORM402_BCC FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'DLNG' ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, rset.getString(4));
							rset1 = stmt1.executeQuery();
							
							while (rset1.next() ) {							
							    oInvCC = rset1.getString(1);
							    oInvBCC = rset1.getString(2);
							    dInvCC = rset1.getString(3);
							    dInvBCC = rset1.getString(4);
							    tInvCC = rset1.getString(5);
							    tInvBCC = rset1.getString(6);
							    dInvTO = rset1.getString(7);
							    tInvTO = rset1.getString(8);	
							    F402_TO = rset1.getString(9);
							    F402_CC = rset1.getString(10);
							    F402_BCC = rset1.getString(11);
							}
							if ("Y".equals(dInvTO) || "Y".equals(tInvTO)) {
								cell.setCellValue("'Y'");
							}
							else if ("Y".equals(oInvCC) || "Y".equals(dInvCC) || "Y".equals(tInvCC)) {
								cell.setCellValue("'N'");
							}
							else if ("Y".equals(oInvBCC) || "Y".equals(dInvBCC)|| "Y".equals(tInvBCC)) {
								cell.setCellValue("'B'");
							} 
							else {
								cell.setCellValue("'null'");
							}
							rset1.close();
							stmt1.close();
						} 
						else if (i == 34) {
							type = value;
							cell.setCellValue("'" + value + "'");
						} 
						else if (i == 35 && (F402_TO.equals("Y") || F402_CC.equals("Y") || F402_BCC.equals("Y"))) {
							cell.setCellValue("'Y'");
						}
						else if (i == 36) {
							if ("Y".equals(F402_TO)) {
								cell.setCellValue("'Y'");
							}
							else if ("Y".equals(F402_CC)) {
								cell.setCellValue("'N'");
							}
							else if ("Y".equals(F402_BCC)) {
								cell.setCellValue("'B'");
							} 
							else {
								cell.setCellValue("'null'");
							}
						}
						else {
							cell.setCellValue("'" + value + "'");
						}
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");

					
				}
				
			}
			rset.close();
			stmt.close();

			
			// SagarB20250610 Added Static contact persons for created trader plants named 'GX HAZIRA COMMERCIAL' from SEIPL Business Owner as per discussed by Vijay on 20250610 : RLNG
			String[] customers = {"SEMTIPL"};
			String[] customer_plant = {"P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,P11,P12"};
			
			queryString = "SELECT B.SUPPLIER_ABBR, 0, 'C', 1, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, 'P1', A.ADDL_ADDR_LINE, A.NOM_FLAG, A.INV_FLAG, A.FM_FLAG, A.PM_FLAG, A.JT_FLAG, A.OTHER_FLAG, A.ACTIVE_FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, NULL, NULL, NULL, NULL, NULL, NULL, A.REMITTANCE_FLAG, NULL, 'RLNG', 'N', 'N'  FROM FMS7_SUPPLIER_CONTACT_MST A, FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD AND UPPER(B.SUPPLIER_ABBR) = 'SEIPL' AND  A.CONTACT_PERSON = 'GX HAZIRA COMMERCIAL'  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				
				for (int k = 0; k < customers.length; k++) {
					
//					String[] dlng = {"null", "null", "null", "null", "null", "null"};
						for (int s = 0; s < dlng.length; s++) {
							dlng[s] = "null";
						}
					
					temp="";
					map = rset.getString(1).trim()+"-"+rset.getString(4);
					
//								for (int c = 0; c < supplier_map.split(",").length; c++){
//									if (supplier_map.split(",")[c].contains(map)){
//										temp = supplier_map.split(",")[c];
//										//System.out.println(temp);
//									}
//								}

					String oInvCC= "",oInvBCC="",dInvCC="",dInvBCC="",tInvCC="",tInvBCC="",dInvTO="",tInvTO="", F402_TO = "", F402_CC = "", F402_BCC = "";
					
					abbr = customers[k];
					cd = rset.getString(2);
					entity = rset.getString(3);
					seq_no = rset.getString(4);
					eff_dt = rset.getString(5);
					
						row = spreadsheet.createRow(nrow++);
						cp_plant = "";
					
//								if (supplier_map.contains(map)) {
//									seq_no = temp.split(";")[1].split("-")[0];
//								}	

		
					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
						
						
						if (i == 0) {
							cell.setCellValue("'" +abbr+ "'");
						}
						else if (i == 3) {
							if (cp_seq.containsKey(rset.getString(12))) {
								cell.setCellValue("'" + value + "'");
								
								for (int s = 0; s < dlng.length; s++) {
									if (rset.getString(s+15).equals("Y")) {
										dlng[s] = "Y";
									}
								}
							} else {
								cell.setCellValue("'" + value + "'");
								cp_seq.put(rset.getString(12), ((seq_num++)+""));

								for (int s = 0; s < dlng.length; s++) {
									dlng[s] = rset.getString(s+15);
								}
							}
						}
						
						else if (i == 6 || i == 13) {
							value = value.replaceAll("//?", "");
							value = value.replaceAll("&", "and");

							cell.setCellValue("'" + value + "'");
						}
						/*
						* else if (i == 22 && !value.equals("null")) { // emp_abbr value =
						* getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
//						*/
						else if(i == 12) {

							cell.setCellValue("'" + customer_plant[k] + "'");
						}
						else if (i == 14) {
							for (int s = 0; s < dlng.length; s++) {
								cell = row.createCell(i++);
								cell.setCellValue("'" + dlng[s] + "'");
							}
							i--;
						}
						else if (i == 27) {
							//queryString1 = "SELECT NVL(T_INV_TO) FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'RLNG' ";
							queryString1 = "SELECT O_INV_CC,O_INV_BCC,D_INV_CC,D_INV_BCC,T_INV_CC,T_INV_BCC,D_INV_TO,T_INV_TO, FORM402_TO, FORM402_CC, FORM402_BCC FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'DLNG' ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, rset.getString(4));
							rset1 = stmt1.executeQuery();
							
							while (rset1.next() ) {							
							    oInvCC = rset1.getString(1);
							    oInvBCC = rset1.getString(2);
							    dInvCC = rset1.getString(3);
							    dInvBCC = rset1.getString(4);
							    tInvCC = rset1.getString(5);
							    tInvBCC = rset1.getString(6);
							    dInvTO = rset1.getString(7);
							    tInvTO = rset1.getString(8);	
							    F402_TO = rset1.getString(9);
							    F402_CC = rset1.getString(10);
							    F402_BCC = rset1.getString(11);
							}
							if ("Y".equals(dInvTO) || "Y".equals(tInvTO)) {
								cell.setCellValue("'Y'");
							}
							else if ("Y".equals(oInvCC) || "Y".equals(dInvCC) || "Y".equals(tInvCC)) {
								cell.setCellValue("'N'");
							}
							else if ("Y".equals(oInvBCC) || "Y".equals(dInvBCC)|| "Y".equals(tInvBCC)) {
								cell.setCellValue("'B'");
							} 
							else {
								cell.setCellValue("'null'");
							}
							rset1.close();
							stmt1.close();
						} 
						else if (i == 34) {
							type = value;
							cell.setCellValue("'" + value + "'");
						} 
						else if (i == 35 && (F402_TO.equals("Y") || F402_CC.equals("Y") || F402_BCC.equals("Y"))) {
							cell.setCellValue("'Y'");
						}
						else if (i == 36) {
							if ("Y".equals(F402_TO)) {
								cell.setCellValue("'Y'");
							}
							else if ("Y".equals(F402_CC)) {
								cell.setCellValue("'N'");
							}
							else if ("Y".equals(F402_BCC)) {
								cell.setCellValue("'B'");
							} 
							else {
								cell.setCellValue("'null'");
							}
						}
						else {
							cell.setCellValue("'" + value + "'");
						}
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + "," + type + ","), conn, "");

					
				}
				
			}
			rset.close();
			stmt.close();
			
			
			filename = migration_setup_dir + "EXPORT/FMS_ENTITY_CONTACT_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_CONTACT_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_ENTITY_CONTACT_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_CONTACT_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_METER_MST() throws SQLException, IOException {
		function_nm = "FMS_METER_MST()";
		
		try {

			System.out.println("<<START>><<FMS_METER_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_METER_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,METER_SEQ,METER_ID,METER_REF,SPECIFICATION,NOTE,STATUS,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			
			String plant_sq = "", meter_sq = "", id = "";

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
//			FMS7_METER_MST A, FMS7_TRANSPORTER_MST B
			queryString = "SELECT B.TRANSPORTER_ABBR, B.TRANSPORTER_CD, A.METER_SEQ_NO, 'R', '1', NULL, A.METER_ID, A.SPECIFICATION, A.NOTE, 'Y', A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, A.TRANS_CUST_CD FROM FMS7_METER_MST A, FMS7_TRANSPORTER_MST B WHERE A.TRANS_CUST_CD = B.TRANSPORTER_CD AND A.METER_TYPE = 'T' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			stmt.setString(5, delta_FromDt);
			stmt.setString(6, delta_FromDt);
			stmt.setString(7, delta_ToDt);
			stmt.setString(8, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,PLANT_SEQ,METER_SEQ,METER_ID,TIMESTAMP", conn);
			
			while (rset.next()) {
//				abbr = rset.getString(1).trim();
//				cd = rset.getString(2);
//				plant_sq = rset.getString(3);
//				meter_sq = rset.getString(5);
//				id = rset.getString(6);
				int m_loc_no = (Integer.parseInt(rset.getString(15)) * 100) + Integer.parseInt(rset.getString(3));
//				
//
//				row = spreadsheet.createRow(nrow++);
//				value = "";
//
//				for (int i = 0; i < columns.split(",").length; i++) {
//					cell = row.createCell(i);
//					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
//					value = value.trim().equals("") ? "null" : value;
//					
//					if(i == 0) {
////						for (int c = 0; c < counterparty_map.split(",").length; c++){
////							if (counterparty_map.split(",")[c].contains(value+";")){
////								value = counterparty_map.split(",")[c].split(";")[1];
////								abbr = value;
////							}
////						}
//
//					    if (mpe.counterparty_map.containsKey(value)) {
//					        value =mpe.counterparty_map.get(value); 
//					       
//					        abbr = value; 
//					    }
//					}
//					else if (i == 5) {
//						value = abbr + "-P" + plant_sq + "-M" + meter_sq;
//						id = value;
//					}
//					else if (i == 6) {
//						value = value.equals("null") ? "" : value;
//						value = value + "-" + rset.getInt(3);
//						id = value;
//						if (value.length() > 20) {
//							value = value.substring(0, 20);
//						}
//					}
//					/*
//					 * else if (i == 10 && !value.equals("null")) { // emp_abbr value =
//					 * getEmpAbbr(value); }
//					 */
//					cell.setCellValue("'" + value + "'");
//
//				}
//				count++;
//				logger.data(fname, (abbr + "," + cd + "," + plant_sq + "," + meter_sq + "," + id + ","), conn, "");

//				FMS7_METER_DTL
				queryString1 = " SELECT NULL, NULL, NULL, NULL, METER_SEQ_NO, NULL, METER_ID, SPECIFICATION, NOTE, FLAG, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_METER_DTL WHERE METER_LOC_NO = ?  AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setInt(1, m_loc_no);
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {

					abbr = rset.getString(1).trim();
					cd = rset.getString(2);
					meter_sq = rset1.getString(5);
					plant_sq = rset.getString(3);
					id = rset.getString(6);
					
					row = spreadsheet.createRow(nrow++);
					value = "";

					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						if (i == 0 || i == 1 || i == 2 || i == 3) {
							value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
							if(i == 0) {
//								for (int c = 0; c < counterparty_map.split(",").length; c++){
//									if (counterparty_map.split(",")[c].contains(value+";")){
//										value = counterparty_map.split(",")[c].split(";")[1];
//										abbr = value;
//									}
//								}

							    if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value); 
							       
							        abbr = value; 
							    }
							}
						} 
						else if (i == 5) {
							value = abbr + "-P" + plant_sq + "-M" + meter_sq;
							id = value;
						}
						else if (i == 6) {
							value = value.equals("null") ? "" : value;
							value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
							value = value + "(M" + rset1.getString(5) + ")";
							if (value.length() > 20) {
								value = value.substring(0, 20);
							}
						}
						/*
						 * else if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replace("'",
						 * ""); value = getEmpAbbr(value); }
						 */
						else {
							value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replaceAll("'", "");
							value = value.replaceAll("\n", " ");
						}
						value = value.trim().equals("") ? "null" : value;
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + plant_sq + "," + meter_sq + "," + id + ","), conn, "");

				}
				stmt1.close();
				rset1.close();

			}

			stmt.close();
			rset.close();

//			FMS7_METER_MST A, FMS7_TRANSPORTER_MST B
//			queryString = "SELECT B.TRANSPORTER_ABBR, B.TRANSPORTER_CD, '0', 'R', '0', NULL, A.METER_ID, A.SPECIFICATION, A.NOTE, 'Y', A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, A.TRANS_CUST_CD FROM FMS7_METER_MST A, FMS7_TRANSPORTER_MST B WHERE A.TRANS_CUST_CD = B.TRANSPORTER_CD AND A.TRANS_CUST_CD = (SELECT DISTINCT(C.TRANS_CUST_CD) FROM FMS7_METER_MST C WHERE A.TRANS_CUST_CD = C.TRANS_CUST_CD) AND A.METER_SEQ_NO = '1'  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
//			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
//			stmt.setString(5, delta_FromDt);
//			stmt.setString(6, delta_FromDt);
//			stmt.setString(7, delta_ToDt);
//			stmt.setString(8, delta_ToDt);
//			rset = stmt.executeQuery();
//
//			while (rset.next()) {
//
//				row = spreadsheet.createRow(nrow++);
//				value = "";
//				abbr = rset.getString(1).trim();
//				cd = rset.getString(2);
//				plant_sq = rset.getString(3);
//				meter_sq = rset.getString(5);
//				id = rset.getString(6);
//
//				for (int i = 0; i < columns.split(",").length; i++) {
//					cell = row.createCell(i);
//					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
//					value = value.trim().equals("") ? "null" : value;
//					
//					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}
//					}
//
//					else if (i == 5) {
//						value = abbr + "-P" + plant_sq + "-M" + meter_sq;
//						id = value;
//					}
//					else if (i == 6) {
//						value = value.equals("null") ? "" : value;
//						if (value.length() > 20) {
//							value = value.substring(0, 20);
//						}
//					}
//					/*
//					 * else if (i == 10 && !value.equals("null")) { // emp_abbr value =
//					 * getEmpAbbr(value); }
//					 */
//					cell.setCellValue("'" + value + "'");
//				}
//				count++;
//				logger.data(fname, (abbr + "," + cd + "," + plant_sq + "," + meter_sq + "," + id + ","), conn, "");
//
//			}
//
//			stmt.close();
//			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_METER_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_METER_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_METER_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_METER_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_GOVT_STAT_TAX() throws SQLException, IOException {
		function_nm = "FMS_GOVT_STAT_TAX()";
		
		try {
			System.out.println("<<START>><<FMS_GOVT_STAT_TAX>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_GOVT_STAT_TAX>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "STAT_CD,STAT_NM,STAT_TYPE,STATUS,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			
			String st_nm = "", st_type = "";

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = " SELECT STAT_CD, STAT_NM, STAT_TYPE, FLAG, REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, '2', NULL FROM FMS7_GOVT_STAT_NO  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "STAT_CD,STAT_NM,STAT_TYPE,TIMESTAMP", conn);
			
			while (rset.next()) {
				cd = rset.getString(1);
				st_nm = rset.getString(2);
				st_type = rset.getString(3);

				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim().replace("'", "");
					value = value.trim().equals("") ? "null" : value;
					/*
					 * if (i == 6 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
					 * }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cd + "," + st_nm + "," + st_type + ","), conn, "");
			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_GOVT_STAT_TAX_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_GOVT_STAT_TAX>>,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_GOVT_STAT_TAX>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_GOVT_STAT_TAX()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COUNTERPARTY_PLANT_TAX() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_PLANT_TAX()";
		
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_PLANT_TAX>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_COUNTERPARTY_PLANT_TAX>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_NM,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String st_nm = "", plant_map = "", RIL_cd = "1";
			
			int seq = 1;
			
			queryString = "SELECT MAX(A.SEQ_NO) FROM FMS7_TRADER_PLANT_DTL A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD AND b.TRADER_ABBR = 'RIL-D6'";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if (rset.next()) {
				seq = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data (COUNTERPARTY)
			queryString = "SELECT DISTINCT(B.COUNTERPARTY_ABBR), B.COUNTERPARTY_CD, B.EFF_DT  FROM FMS9_COUNTERPTY_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE B.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND B.COUNTERPARTY_ABBR != 'SEIPL' AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.COUNTERPARTY_CD, B.EFF_DT DESC   ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_NM,TIMESTAMP", conn);
			
			while (rset.next()) {
				abbr = rset.getString(1).trim();
				cd = rset.getString(2);

				// CUSTOMER
				queryString1 = "SELECT DISTINCT(B.COUNTERPARTY_CD), 'C', A.PLANT_SEQ_NO, C.STAT_NM, A.STAT_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.FLAG, A.REMARK, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, C.STAT_TYPE FROM FMS7_CUSTOMER_PLANT_TAX_CDS A, FMS7_CUSTOMER_MST B, FMS7_GOVT_STAT_NO C WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND C.STAT_CD = A.STAT_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.PLANT_SEQ_NO, A.STAT_NO DESC ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				stmt1.setString(6, delta_FromDt);
				stmt1.setString(7, delta_FromDt);
				stmt1.setString(8, delta_ToDt);
				stmt1.setString(9, delta_ToDt);
				stmt1.setString(10, delta_FromDt);
				stmt1.setString(11, delta_FromDt);
				stmt1.setString(12, delta_ToDt);
				stmt1.setString(13, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					plant_map = abbr + "-" + rset1.getString(3) + ",";
					
					if (!Arrays.asList(mpe.customer_delete).contains(plant_map))  {
						row = spreadsheet.createRow(nrow++);
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						st_nm = rset1.getString(4);
						value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
						cell = row.createCell(0);
//					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}

					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					       
					        abbr = value; 
					    }
//					}
						cell.setCellValue("'" + value + "'");
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).trim().replace("'", "");
							
							if (i == 4) {
//								value = value + "-" + rset1.getString(13);
								
								// SagarB20250819 UPDATED CHANGES FOR GSTIN SINCE GSTIN-S IS REMOVED FROM MIGRATE2EMS
								if (value.contains("GSTIN")) {
									value = value + "-" + "R";
								}
								else {
									value = value + "-" + rset1.getString(13);
								}
							}
							/*
							 * else if (i == 10 && !value.equals("null")) { // emp_abbr value =
							 * getEmpAbbr(value); }
							 */
							cell.setCellValue("'" + value + "'");
						}
						
						count++;
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + st_nm + ","), conn,"");
					}

				}
				stmt1.close();
				rset1.close();

				// TRADER
				queryString1 = "SELECT DISTINCT(B.COUNTERPARTY_CD), 'T', A.PLANT_SEQ_NO, C.STAT_NM, A.STAT_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.FLAG, A.REMARK, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, C.STAT_TYPE FROM FMS7_TRADER_PLANT_TAX_CDS A, FMS7_TRADER_MST B, FMS7_GOVT_STAT_NO C WHERE A.TRADER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ?  AND C.STAT_CD = A.STAT_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.PLANT_SEQ_NO";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				stmt1.setString(6, delta_FromDt);
				stmt1.setString(7, delta_FromDt);
				stmt1.setString(8, delta_ToDt);
				stmt1.setString(9, delta_ToDt);
				stmt1.setString(10, delta_FromDt);
				stmt1.setString(11, delta_FromDt);
				stmt1.setString(12, delta_ToDt);
				stmt1.setString(13, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					plant_map = abbr.trim() + "-" + rset1.getString(3) + "," ;
					
					if (!Arrays.asList(mpe.trader_delete).contains(plant_map) && !abbr.equals("RIL"))  {
						row = spreadsheet.createRow(nrow++);
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						st_nm = rset1.getString(4);
						
						value = rset.getString(1) == null ? "null" : rset.getString(1).trim().replace("'", "");
//						if(i == 0) {
//							for (int c = 0; c < counterparty_map.split(",").length; c++){
//								if (counterparty_map.split(",")[c].contains(value+";")){
//									value = counterparty_map.split(",")[c].split(";")[1];
//									abbr = value;
//								}
//							}

					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					       
					        abbr = value; 
					    }
//						}
						cell = row.createCell(0);
						if (abbr.contains("RIL")) {
							abbr = "RIL";
							cell.setCellValue("'" + abbr + "'");
						}
						else {
							cell.setCellValue("'" + value + "'");
						}
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).trim().replace("'", "");
							
							if (i == 1 && abbr.contains("RIL")) {
								value = RIL_cd;
							}
							else if (i == 3) {
								if (rset.getString(1).contains("RIL-CBM")) {
									value = (seq + Integer.parseInt(rset1.getString(3))) + "";
									seq_no = value;
								}
								else {
									value = seq_no;
								}
							}
							else if (i == 4) {
								value = value + "-" + rset1.getString(13);
							}
							
							/*
							 * else if (i == 10 && !value.equals("null")) { // emp_abbr value =
							 * getEmpAbbr(value); }
							 */
							
							cell.setCellValue("'" + value + "'");
						}
						count++;
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + st_nm + ","), conn,"");
					}
					else if(abbr.equals("abbr")) {
						RIL_cd = cd;
					}

				}
				stmt1.close();
				rset1.close();

			}

			stmt.close();
			rset.close();

			// SUPPLIER
			queryString1 = "SELECT B.SUPPLIER_ABBR, '2', 'B', '1', GST_TIN_NO, TO_CHAR(B.GST_TIN_DT, 'DD/MM/YYYY'), CST_TIN_NO, TO_CHAR(B.CST_TIN_DT, 'DD/MM/YYYY'), PAN_NO, TO_CHAR(B.PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), TAN_NO, TO_CHAR(B.TAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), GSTIN_NO, TO_CHAR(B.GSTIN_DT, 'DD/MM/YYYY hh24:mi:ss'), 'Y', NULL, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), B.EMP_CD, NULL, NULL FROM FMS7_SUPPLIER_MST B WHERE B.SUPPLIER_ABBR = 'SEIPL' AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();

			if (rset1.next()) {
				abbr = rset1.getString(1).trim();
				cd = rset1.getString(2);
				entity = rset1.getString(3);
				seq_no = rset1.getString(4);

				// For GST_TIN_NO
				queryString = "SELECT STAT_CD, STAT_NM, STAT_TYPE FROM FMS7_GOVT_STAT_NO WHERE UPPER(STAT_NM) LIKE '%GST TIN%' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();

				if (rset.next()) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					st_nm = rset.getString(2);
					for (int i = 0; i < 4; i++) {
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
						cell = row.createCell(i);
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(5) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(6) == null ? "null" : rset1.getString(6).replace("'", "");
					cell.setCellValue("'" + value + "'");

					for (int i = 7; i < 13; i++) {
						cell = row.createCell(i);
						/*
						 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * rset1.getString(i+8) == null ? "null" : rset1.getString(i+8).replace("'",
						 * ""); value = getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						 */
						// else {
						cell.setCellValue("'" + rset1.getString(i + 8) + "'");
						// }
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + st_nm + ","), conn, "");

				}
				rset.close();
				stmt.close();

				// For CST_TIN_NO
				queryString = "SELECT STAT_CD, STAT_NM, STAT_TYPE FROM FMS7_GOVT_STAT_NO WHERE UPPER(STAT_NM) LIKE '%CST TIN%' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();

				if (rset.next()) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					st_nm = rset.getString(2);
					for (int i = 0; i < 4; i++) {
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
						cell = row.createCell(i);
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(7) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(8) == null ? "null" : rset1.getString(8).replace("'", "");
					cell.setCellValue("'" + value + "'");

					for (int i = 7; i < 13; i++) {
						cell = row.createCell(i);
						/*
						 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * rset1.getString(i+8) == null ? "null" : rset1.getString(i+8).replace("'",
						 * ""); value = getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						 */
						// else {
						cell.setCellValue("'" + rset1.getString(i + 8) + "'");
						// }
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + st_nm + ","), conn, "");
				}
				rset.close();
				stmt.close();

				// For PAN_NO
				queryString = "SELECT STAT_CD, STAT_NM, STAT_TYPE FROM FMS7_GOVT_STAT_NO WHERE UPPER(STAT_NM) LIKE '%PAN%' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();

				if (rset.next()) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					st_nm = rset.getString(2);
					for (int i = 0; i < 4; i++) {
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
						cell = row.createCell(i);
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(9) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(10) == null ? "null" : rset1.getString(10).replace("'", "");
					cell.setCellValue("'" + value + "'");

					for (int i = 7; i < 13; i++) {
						cell = row.createCell(i);
						/*
						 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * rset1.getString(i+8) == null ? "null" : rset1.getString(i+8).replace("'",
						 * ""); value = getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						 */
						// else {
						cell.setCellValue("'" + rset1.getString(i + 8) + "'");
						// }
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + st_nm + ","), conn, "");
				}
				rset.close();
				stmt.close();

				// For TAN_NO
				queryString = "SELECT STAT_CD, STAT_NM, STAT_TYPE FROM FMS7_GOVT_STAT_NO WHERE UPPER(STAT_NM) LIKE '%TAN%' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();

				if (rset.next()) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					st_nm = rset.getString(2);
					
					for (int i = 0; i < 4; i++) {
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
						cell = row.createCell(i);
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(11) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(12) == null ? "null" : rset1.getString(12).replace("'", "");
					cell.setCellValue("'" + value + "'");

					for (int i = 7; i < 13; i++) {
						cell = row.createCell(i);
						/*
						 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * rset1.getString(i+8) == null ? "null" : rset1.getString(i+8).replace("'",
						 * ""); value = getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						 */
						// else {
						cell.setCellValue("'" + rset1.getString(i + 8) + "'");
						// }
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + st_nm + ","), conn, "");
				}
				rset.close();
				stmt.close();

				// For GSTIN_NO
				queryString = "SELECT STAT_CD, STAT_NM, STAT_TYPE FROM FMS7_GOVT_STAT_NO WHERE UPPER(STAT_NM) LIKE '%GSTIN%' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();

				if (rset.next()) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					st_nm = rset.getString(2);
					
					for (int i = 0; i < 4; i++) {
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
						cell = row.createCell(i);
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(13) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(14) == null ? "null" : rset1.getString(14).replace("'", "");
					cell.setCellValue("'" + value + "'");

					for (int i = 7; i < 13; i++) {
						cell = row.createCell(i);
						/*
						 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * rset1.getString(i+8) == null ? "null" : rset1.getString(i+8).replace("'",
						 * ""); value = getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						 */
						// else {
						cell.setCellValue("'" + rset1.getString(i + 8) + "'");
						// }
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + st_nm + ","), conn, "");
				}
				rset.close();
				stmt.close();

			}
			stmt1.close();
			rset1.close();

			queryString1 = "SELECT B.SUPPLIER_ABBR, '2', 'B', (A.PLANT_SEQ_NO+1), C.STAT_NM, A.STAT_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY'), A.FLAG, A.REMARK, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, C.STAT_TYPE FROM FMS7_SUPPLIER_PLANT_TAX_CDS A, FMS7_SUPPLIER_MST B, FMS7_GOVT_STAT_NO C WHERE A.SUPPLIER_CD = B.SUPPLIER_CD  AND A.STAT_CD = C.STAT_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.PLANT_SEQ_NO";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_FromDt);
			stmt1.setString(6, delta_FromDt);
			stmt1.setString(7, delta_ToDt);
			stmt1.setString(8, delta_ToDt);
			stmt1.setString(9, delta_FromDt);
			stmt1.setString(10, delta_FromDt);
			stmt1.setString(11, delta_ToDt);
			stmt1.setString(12, delta_ToDt);
			rset1 = stmt1.executeQuery();

			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				abbr = rset1.getString(1).trim();
				cd = rset1.getString(2);
				entity = rset1.getString(3);
				seq_no = rset1.getString(4);
				st_nm = rset1.getString(5);

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
					if (i == 4) {
						value = value + "-" + rset1.getString(14);
					}
					/*
					 * else if (i == 10 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + st_nm + ","), conn, "");

			}
			stmt1.close();
			rset1.close();

			filename = migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_PLANT_TAX_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_PLANT_TAX>>,,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_PLANT_TAX>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_PLANT_TAX()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COUNTERPARTY_BU_TAX() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_BU_TAX()";
		
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_BU_TAX>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_COUNTERPARTY_BU_TAX>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_NM,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			
			String st_nm = "";

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data

			// SURVEYOR
			queryString1 = "SELECT B.SURVEYOR_ABBR, '2', 'S', '1', NULL, NULL, NULL, NULL, PAN_NO, TO_CHAR(B.PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, NULL, 'Y', NULL, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), B.EMP_CD, NULL, NULL FROM FMS7_SURVEYOR_MST B  WHERE (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,STAT_NM,TIMESTAMP", conn);
			
			while (rset1.next()) {
				abbr = rset1.getString(1).trim();
				cd = rset1.getString(2);
				entity = rset1.getString(3);

				// For PAN_NO
				queryString = "SELECT STAT_CD, STAT_NM, STAT_TYPE  FROM FMS7_GOVT_STAT_NO WHERE UPPER(STAT_NM) LIKE '%PAN%' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();

				if (rset.next()) {
					row = spreadsheet.createRow(nrow++);
					value = "";
//					st_nm = rset.getString(2);
					st_nm = rset.getString(2) + "-" + rset.getString(3);

					for (int i = 0; i < 4; i++) {
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
						cell = row.createCell(i);
						if(i == 0) {
//							for (int c = 0; c < counterparty_map.split(",").length; c++){
//								if (counterparty_map.split(",")[c].contains(value+";")){
//									value = counterparty_map.split(",")[c].split(";")[1];
//									abbr = value;
//								}
//							}

						    if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						       
						        abbr = value; 
						    }
						}
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(9) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(10) == null ? "null" : rset1.getString(10).replace("'", "");
					cell.setCellValue("'" + value + "'");

					for (int i = 7; i < 13; i++) {
						cell = row.createCell(i);
						/*
						 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * rset1.getString(i+8) == null ? "null" : rset1.getString(i+8).replace("'",
						 * ""); value = getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						 */
						// else {
						cell.setCellValue("'" + rset1.getString(i + 8) + "'");
						// }
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + st_nm + ","), conn, "");

				}
				rset.close();
				stmt.close();

			}
			stmt1.close();
			rset1.close();

			// VESSEL
			queryString1 = "SELECT B.VESSEL_ABBR, '2', 'V', '1', NULL, NULL, NULL, NULL, PAN_NO, TO_CHAR(B.PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, NULL, 'Y', NULL, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), B.EMP_CD, NULL, NULL FROM FMS7_VESSEL_AGENT_MST B WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();

			while (rset1.next()) {
				abbr = rset1.getString(1).trim();
				cd = rset1.getString(2);
				entity = rset1.getString(3);
				
				// For PAN_NO
				queryString = "SELECT STAT_CD, STAT_NM, STAT_TYPE  FROM FMS7_GOVT_STAT_NO WHERE UPPER(STAT_NM) LIKE '%PAN%' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();

				if (rset.next()) {
					st_nm = rset.getString(2) + "-" + rset.getString(3);
					row = spreadsheet.createRow(nrow++);
					value = "";

					for (int i = 0; i < 4; i++) {
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
						cell = row.createCell(i);
						if(i == 0) {
//							for (int c = 0; c < counterparty_map.split(",").length; c++){
//								if (counterparty_map.split(",")[c].contains(value+";")){
//									value = counterparty_map.split(",")[c].split(";")[1];
//									abbr = value;
//								}
//							}

						    if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						       
						        abbr = value; 
						    }
						}
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(9) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(10) == null ? "null" : rset1.getString(10).replace("'", "");
					cell.setCellValue("'" + value + "'");

					for (int i = 7; i < 13; i++) {
						cell = row.createCell(i);
						/*
						 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * rset1.getString(i+8) == null ? "null" : rset1.getString(i+8).replace("'",
						 * ""); value = getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						 */
						// else {
						cell.setCellValue("'" + rset1.getString(i + 8) + "'");
						// }
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + st_nm + ","), conn, "");

				}
				rset.close();
				stmt.close();

			}
			stmt1.close();
			rset1.close();

			// CHA
			queryString1 = "SELECT B.CHA_ABBR, '2', 'H', '1', NULL, NULL, NULL, NULL, PAN_NO, TO_CHAR(B.PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, NULL, 'Y', NULL, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), B.EMP_CD, NULL, NULL FROM FMS7_CUS_HOUSE_AGENT_MST B  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();

			while (rset1.next()) {
				abbr = rset1.getString(1).trim();
				cd = rset1.getString(2);
				entity = rset1.getString(3);
				
				// For PAN_NO
				queryString = "SELECT STAT_CD, STAT_NM, STAT_TYPE  FROM FMS7_GOVT_STAT_NO WHERE UPPER(STAT_NM) LIKE '%PAN%' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();

				if (rset.next()) {
					row = spreadsheet.createRow(nrow++);
					value = "";
					st_nm = rset.getString(2) + "-" + rset.getString(3);
					for (int i = 0; i < 4; i++) {
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
						cell = row.createCell(i);
						if(i == 0) {
//							for (int c = 0; c < counterparty_map.split(",").length; c++){
//								if (counterparty_map.split(",")[c].contains(value+";")){
//									value = counterparty_map.split(",")[c].split(";")[1];
//									abbr = value;
//								}
//							}

						    if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        abbr = value; 
						    }
						}
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(9) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(10) == null ? "null" : rset1.getString(10).replace("'", "");
					cell.setCellValue("'" + value + "'");

					for (int i = 7; i < 13; i++) {
						cell = row.createCell(i);
						/*
						 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * rset1.getString(i+8) == null ? "null" : rset1.getString(i+8).replace("'",
						 * ""); value = getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
						 */
						// else {
						cell.setCellValue("'" + rset1.getString(i + 8) + "'");
						// }
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + st_nm + ","), conn, "");

				}
				rset.close();
				stmt.close();

			}
			stmt1.close();
			rset1.close();

			filename = migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_BU_TAX_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_BU_TAX>>,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_COUNTERPARTY_BU_TAX>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_BU_TAX()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_TAX_MST() throws SQLException, IOException {
		function_nm = "FMS_TAX_MST()";
		
		try {

			System.out.println("<<START>><<FMS_TAX_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TAX_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "TAX_CODE,TAX_NAME,TAX_ALIAS_CODE,SHT_NM,APP_DATE,STATUS,TAX_PRIORITY,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = " SELECT TAX_CODE, TAX_NAME, TAX_ALIAS_CODE, SHT_NM, TO_CHAR(APP_DATE, 'DD/MM/YYYY hh24:mi:ss'), STATUS, TAX_PRIORITY, TO_CHAR(SYSDATE, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, '2', NULL FROM FMS7_TAX_MST  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "TAX_CODE,TAX_NAME,APP_DATE,TIMESTAMP", conn);
			
			while (rset.next()) {
				tax_code = rset.getString(1);
				tax_nm = rset.getString(2);
				// taking app_date:
				eff_dt = rset.getString(5);
				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					value = value.trim().equals("") ? "null" : value;
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (tax_code + "," + tax_nm + "," + eff_dt + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_TAX_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TAX_MST>>,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TAX_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_TAX_MST()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_TAX_STRUCTURE() throws SQLException, IOException {
		function_nm = "FMS_TAX_STRUCTURE()";
		
		try {

			System.out.println("<<START>><<FMS_TAX_STRUCTURE>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TAX_STRUCTURE>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "TAX_STR_CD,DESCR,STATUS,TAX_ALIAS_CODES,TAX_STR,TAX_TOTAL,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,REMARK,TAX_CATEGORY,APP_DATE,SAP_TAX_CODE,SAP_GL,PAY_RECV,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			
			String descr = "";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = " SELECT B.TAX_STR_CD, B.REMARK, B.STATUS, B.TAX_ALIAS_CODES, B.TAX_STR, B.TAX_TOTAL, TO_CHAR(SYSDATE, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, NULL, 'P', TO_CHAR(B.APP_DATE, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL, '2', NULL FROM FMS7_TAX_STRUCTURE B ORDER BY B.TAX_STR_CD ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "TAX_STR_CD,DESCR,APP_DATE,TIMESTAMP", conn);
			
			while (rset.next()) {

				row = spreadsheet.createRow(nrow++);
				value = "";
				cd = rset.getString(1);
				
//				descr = rset.getString(2);
                //app_date=-eff_dt
				
				eff_dt = rset.getString(13);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					if (i == 1) { // For Remark
						if (value.contains(",")) {
							String text = "";
							for (int j = 0; j < value.split(",").length; j++) {
								text += (value.split(",")[j] + ", ");
							}
							value = text.substring(0, text.length() - 2);
						}
						descr=value;
						descr=descr.replace(',',' ');
					}
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cd + "," + descr + "," + eff_dt + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_TAX_STRUCTURE_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TAX_STRUCTURE>>,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TAX_STRUCTURE>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_TAX_STRUCTURE()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_TAX_STRUCTURE_DTL() throws SQLException, IOException {
		function_nm = "FMS_TAX_STRUCTURE_DTL()";
		
		try {

			System.out.println("<<START>><<FMS_TAX_STRUCTURE_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TAX_STRUCTURE_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "DESCR,TAX_NAME,TAX_ALIAS_CODE,SHT_NM,FACTOR,TAX_ON,TAX_NAME1,TAX_ALIAS_CODE1,SHT_NM1,FLAG,APP_DATE,SAP_TAX_CODE,SAP_GL";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
            count=0;
            
            String descr="";
            
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = " SELECT A.TAX_STR_CD, B.TAX_NAME, B.TAX_ALIAS_CODE, B.SHT_NM, A.FACTOR, A.TAX_ON, A.TAX_ON_CD, NULL, NULL, A.FLAG, TO_CHAR(A.APP_DATE, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_TAX_STRUCTURE_DTL A, FMS7_TAX_MST B WHERE A.TAX_CODE = B.TAX_CODE ORDER BY A.TAX_STR_CD ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "DESCR,TAX_NAME,APP_DATE,TIMESTAMP", conn);
			
			while (rset.next()) 
			{
                tax_nm=rset.getString(2);
                //app_dt =efftdate
                eff_dt=rset.getString(11);
                
				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					if (i == 0) {
						queryString1 = "SELECT REMARK FROM FMS7_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, value);
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							// For Remark
							value = rset1.getString(1) == null ? "null" : rset1.getString(1);
							if (value.contains(",")) {
								String text = "";
								for (int j = 0; j < value.split(",").length; j++) {
									text += (value.split(",")[j] + ", ");
								}
								value = text.substring(0, text.length() - 2);
							}
							descr=value;
							descr=descr.replace(',',' ');
							
							cell.setCellValue("'" + value + "'");
						}
						rset1.close();
						stmt1.close();
					} else if (i == 6) {
						if (!value.equals("null")) {
							queryString1 = "SELECT TAX_NAME, TAX_ALIAS_CODE, SHT_NM FROM FMS7_TAX_MST WHERE TAX_CODE = ? ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, value);
							rset1 = stmt1.executeQuery();
							if (rset1.next()) {
								cell.setCellValue("'" + rset1.getString(1) + "'");

								i++;
								cell = row.createCell(i);
								cell.setCellValue("'" + rset1.getString(2) + "'");

								i++;
								cell = row.createCell(i);
								cell.setCellValue("'" + rset1.getString(3) + "'");
							}
							stmt1.close();
							rset1.close();
						} else {
							cell.setCellValue("'null'");

							i++;
							cell = row.createCell(i);
							cell.setCellValue("'null'");

							i++;
							cell = row.createCell(i);
							cell.setCellValue("'null'");
						}
					} else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (descr + "," + tax_nm + "," + eff_dt + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_TAX_STRUCTURE_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TAX_STRUCTURE_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_TAX_STRUCTURE_DTL>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_TAX_STRUCTURE_DTL()  ",e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_TAX_STRUCT_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_TAX_STRUCT_DTL()";
		
		try {
			
			System.out.println("<<START>><<FMS_ENTITY_TAX_STRUCT_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_TAX_STRUCT_DTL>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT,INVOICE_TYPE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;
			
			String map = "", RIL_cd = "1";
			String temp = "";
			
			int seq = 1;
			
			queryString = "SELECT MAX(A.SEQ_NO) FROM FMS7_TRADER_PLANT_DTL A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD AND B.TRADER_ABBR = 'RIL-D6'";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if (rset.next()) {
				seq = rset.getInt(1);
			}
			rset.close();
			stmt.close();

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data

			// CUSTOMER
			queryString1 = "SELECT B.CUSTOMER_ABBR, A.CUSTOMER_CD, 'C', A.PLANT_SEQ_NO, A.TAX_STRUCT_CD, A.TAX_STRUCT_DTL, A.TAX_STRUCT_REMARK, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(A.TAX_STRUCT_DT, 'DD/MM/YYYY'), 'S' FROM FMS7_CUSTOMER_TAX_STRUCT_DTL A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND  (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.CUSTOMER_CD, TO_CHAR(A.TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss') DESC ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TIMESTAMP", conn);
			
			while (rset1.next()) {

				map = rset1.getString(1).trim()+"-"+rset1.getString(4);
				
//				for (int c = 0; c < customer_map.split(",").length; c++) {
//					if (customer_map.split(",")[c].contains(map+";")) {
//						temp = customer_map.split(",")[c];
//					}
//				}
				
				if (mpe.customer_map.containsKey(map)) {
					 temp= mpe.customer_map.get(map); 
				}
				
				row = spreadsheet.createRow(nrow++);
					
				abbr=rset1.getString(1).trim();
				cd=rset1.getString(2);
				entity=rset1.getString(3);
				seq_no=rset1.getString(4);
				
				String bu_unit = "1";
				
//				if (customer_map.contains(map+";")) {
//					seq_no = temp.split(";")[1].split("-")[0];
//					bu_unit = temp.split(";")[1].split("-")[1];
//				}	
				if (mpe.customer_map.containsKey(map) && !temp.isEmpty()){
					seq_no = temp.split("-")[0];
					bu_unit = temp.split("-")[1];
				}
				else if (rset1.getString(6).contains("CST 3")) {
					bu_unit = "2";
				}	
				else if (rset1.getString(6).contains(" MH ")) {
					bu_unit = "2";
				}	
				else if (rset1.getString(6).contains(" AP ")) {
					bu_unit = "3";
				}

				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					/*
					 * if (i == 9 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
					 * }
					 */
					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}

					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        abbr = value; 
					    }
					}
					else if (i == 3) {
						value = seq_no;
					}
					else if (i == 5) {
						value = value.replaceAll(",", ", ");
						
						if (value.contains("MH")) {
							value = value.split(" MH")[0] + "%";
						}
						else if (value.contains("AP")) {
							value = value.split(" AP")[0] + "%";
						}
						else if (value.contains("ADD. VAT")) {
							value = value.replace("ADD. VAT", "ADVAT");
						}
					}
					else if(i == 12) {
						value = bu_unit;
					}
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + ","+ seq_no + ","), conn, "");
				
			}

			stmt1.close();
			rset1.close();

			// TRADER
			queryString1 = "SELECT B.TRADER_ABBR, A.TRADER_CD, 'T', A.PLANT_SEQ_NO, A.TAX_STRUCT_CD, A.TAX_STRUCT_DTL, A.TAX_STRUCT_REMARK, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(A.TAX_STRUCT_DT, 'DD/MM/YYYY'), 'S' FROM FMS7_TRADER_TAX_STRUCT_DTL A, FMS7_TRADER_MST B WHERE A.TRADER_cD = B.TRADER_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.TRADER_CD, TO_CHAR(A.TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss') DESC ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();

			while (rset1.next()) {
				
				if (!rset1.getString(1).equals("RIL")) {
					
					map = rset1.getString(1).trim() + "-" + rset1.getString(4);
					
//					for (int c = 0; c < trader_map.split(",").length; c++) {
//						if (trader_map.split(",")[c].contains(map+";")) {
//							temp = trader_map.split(",")[c];
//						}
//					}
					if (mpe.trader_map.containsKey(map)) 
					 {
						 temp= mpe.trader_map.get(map); 
					}
					
					row = spreadsheet.createRow(nrow++);
					
					abbr = rset1.getString(1).trim();
					cd = rset1.getString(2);
					entity = rset1.getString(3);
					seq_no = rset1.getString(4);
					
					String bu_unit = "1";
					
//					if (trader_map.contains(map+";")) {
//						seq_no = temp.split("-")[0];
//						bu_unit = temp.split("-")[1];
//					}
					if (mpe.trader_map.containsKey(map) && !temp.isEmpty()){
						seq_no = temp.split("-")[0];
						bu_unit = temp.split("-")[1];
					}
					else if (abbr.equals("AMNS") && rset1.getString(6).contains("CST 2")) {
						bu_unit = "2";
					}	
					else if (rset1.getString(6).contains(" MH ")) {
						bu_unit = "2";
					}	
					else if (rset1.getString(6).contains(" AP ")) {
						bu_unit = "3";
					}
					
					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
						/*
						 * if (i == 9 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
						 * }
						 */
						
						if (i == 0 && rset1.getString(1).contains("RIL")) {
							value = "RIL";
						}
						else if(i == 0) {
//							for (int c = 0; c < counterparty_map.split(",").length; c++){
//								if (counterparty_map.split(",")[c].contains(value+";")){
//									value = counterparty_map.split(",")[c].split(";")[1];
//									abbr = value;
//								}
//							}
	
						    if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        abbr = value; 
						    }
						}
						else if(i == 1 && abbr.contains("RIL")) {
							value = RIL_cd;
						}
						else if (i == 3) {
							
							if (rset1.getString(1).contains("RIL-CBM")) {
								value = (seq + Integer.parseInt(rset1.getString(4))) + "";
								seq_no = value;
							}
							else {
								value = seq_no;
							}
							
						} 
						else if (i == 5) {
							value = value.replaceAll(",", ", ");
							
							if (value.contains("MH")) {
								value = value.split(" MH")[0] + "%";
							}
							else if (value.contains("AP")) {
								value = value.split(" AP")[0] + "%";
							}
							else if (value.contains("ADD. VAT")) {
								value = value.replace("ADD. VAT", "ADVAT");
							}
						}
						else if (i == 12) {
							value = bu_unit;
						}
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + ","), conn, "");
				}
				else if(rset1.getString(1).equals("RIL"))  {
					RIL_cd = rset1.getString(2);
				}

			}

			stmt1.close();
			rset1.close();

			filename = migration_setup_dir + "EXPORT/FMS_ENTITY_TAX_STRUCT_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_TAX_STRUCT_DTL>>,,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_ENTITY_TAX_STRUCT_DTL>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_TAX_STRUCT_DTL()  ",e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_SERVICE_TAX_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_SERVICE_TAX_DTL()";
		
		try {

			System.out.println("<<START>><<FMS_ENTITY_SERVICE_TAX_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_SERVICE_TAX_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count=0;
			String tax_cd="";
			
			String RIL_cd = "1";
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data

			// CUSTOMER
			queryString1 = "SELECT CUSTOMER_ABBR, CUSTOMER_CD, 'C', PLANT_SEQ_NO, TAX_STRUCT_CD, 'SI', TAX_STRUCT_DTL, TAX_STRUCT_REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY') FROM FMS7_CUSTOMER_SERVICE_TAX_DTL  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_CD, TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss') DESC ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TIMESTAMP", conn);
			
			while (rset1.next()) {
				
				abbr=rset1.getString(1).trim();
				cd=rset1.getString(2);
				entity=rset1.getString(3);
				seq_no=rset1.getString(4);
				tax_cd=rset1.getString(5);
				
				for (int j = 0; j < 2; j++) {
				row = spreadsheet.createRow(nrow++);
					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
						/*
						 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); }
						 */
						
						if(i == 0) {
	//						for (int c = 0; c < counterparty_map.split(",").length; c++){
	//							if (counterparty_map.split(",")[c].contains(value+";")){
	//								value = counterparty_map.split(",")[c].split(";")[1];
	//								abbr = value;
	//							}
	//						}
	
						    if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        abbr = value; 
						    }
							
						}
						else if(i == 5 && j == 1) {
							value = "RV";
						}
						else if (i == 6) {
							value = value.replaceAll(",", ", ");
							
							if (value.contains("MH")) {
								value = value.split(" MH")[0] + "%";
							}
							else if (value.contains("AP")) {
								value = value.split(" AP")[0] + "%";
							}
							else if (value.contains("ADD. VAT")) {
								value = value.replace("ADD. VAT", "ADVAT");
							}
						}
						cell.setCellValue("'" + value + "'");
					}

					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + ","+ seq_no + ","+ tax_cd + ","), conn, "");
				}

			}

			stmt1.close();
			rset1.close();

			// CUSTOMER TAX DTL INVOICE-WISE
			queryString1 = "SELECT CUSTOMER_ABBR, CUSTOMER_CD, 'C', PLANT_SEQ_NO, TAX_STRUCT_CD, INVFLAG, TAX_STRUCT_DTL, TAX_STRUCT_REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY') FROM FMS7_TAX_DTL_INVOICEWISE  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_CD, TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss') DESC ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();

			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				
				abbr=rset1.getString(1).trim();
				cd=rset1.getString(2);
				entity=rset1.getString(3);
				seq_no=rset1.getString(4);
				tax_cd=rset1.getString(5);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
					
					if(i == 0) {
//						for (int c = 0; c < counterparty_map.split(",").length; c++){
//							if (counterparty_map.split(",")[c].contains(value+";")){
//								value = counterparty_map.split(",")[c].split(";")[1];
//								abbr = value;
//							}
//						}

					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        abbr = value; 
					    }
						cell.setCellValue("'" + value + "'");
					}
					else if (i == 5) {
						if (value.equals("U")) {
							value = "UG";
						} else if (value.equals("M")) {
							value = "LP";
						} else if (value.equals("B")) {
							value = "ST";
						} else if (value.equals("A")) {
							value = "RV";
						} else if (value.equals("C")) {
							value = "SI";
						}
						cell.setCellValue("'" + value + "'");

					}
					else if (i == 6) {
						value = value.replaceAll(",", ", ");
						
						if (value.contains("MH")) {
							value = value.split(" MH")[0] + "%";
						}
						else if (value.contains("AP")) {
							value = value.split(" AP")[0] + "%";
						}
						else if (value.contains("ADD. VAT")) {
							value = value.replace("ADD. VAT", "ADVAT");
						}
						cell.setCellValue("'" + value + "'");
					}
					/*
					 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + ","+ seq_no + ","+ tax_cd + ","), conn, "");
			}

			stmt1.close();
			rset1.close();

			// TRADER
			queryString1 = "SELECT TRADER_ABBR, TRADER_CD, 'T', PLANT_SEQ_NO, TAX_STRUCT_CD, 'SI', TAX_STRUCT_DTL, TAX_STRUCT_REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY') FROM FMS7_TRADER_SERVICE_TAX_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY TRADER_CD, TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss') DESC ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();

			while (rset1.next()) {
				
				if(abbr.equals("RIL")) {
					RIL_cd = rset1.getString(2);
				}
				
				if (!abbr.equals("RIL")) {
					
					row = spreadsheet.createRow(nrow++);
					
					abbr=rset1.getString(1).trim();
					cd=rset1.getString(2);
					
					entity=rset1.getString(3);
					seq_no=rset1.getString(4);
					tax_cd=rset1.getString(5);
					
					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
						/*
						 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); }
						 */
						if (i == 0 && abbr.contains("RIL")) {
							value = "RIL";
						}
						else if(i == 0) {
//							for (int c = 0; c < counterparty_map.split(",").length; c++){
//								if (counterparty_map.split(",")[c].contains(value+";")){
//									value = counterparty_map.split(",")[c].split(";")[1];
//									abbr = value;
//								}
//							}

						    if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        abbr = value; 
						    }
							
						}
						else if (i == 6) {
							value = value.replaceAll(",", ", ");
							
							if (value.contains("MH")) {
								value = value.split(" MH")[0] + "%";
							}
							else if (value.contains("AP")) {
								value = value.split(" AP")[0] + "%";
							}
							else if (value.contains("ADD. VAT")) {
								value = value.replace("ADD. VAT", "ADVAT");
							}
						}
						else if (i == 1 && abbr.contains("RIL")) {
							value = RIL_cd;
						}
						cell.setCellValue("'" + value + "'");
					}	
					
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + ","+ seq_no + ","+ tax_cd + ","), conn, "");
				}

			}

			stmt1.close();
			rset1.close();

			filename = migration_setup_dir + "EXPORT/FMS_ENTITY_SERVICE_TAX_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_SERVICE_TAX_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_ENTITY_SERVICE_TAX_DTL>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_SERVICE_TAX_DTL()  ",e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_BU_SVC_TAX_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_BU_SVC_TAX_DTL()";
		
		try {

			System.out.println("<<START>><<FMS_ENTITY_BU_SVC_TAX_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_BU_SVC_TAX_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count=0;
			
			String tax_cd="", invoice_type = "";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data

			// CHA, VA, SURVEYOR
			queryString1 = "SELECT NULL, ENTITY_CD, ENTITY_TYPE, '1', TAX_STRUCT_CD, NULL, TAX_STRUCT_DTL, TAX_STRUCT_REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY') FROM FMS7_CARGO_ENTITY_TAX_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY ENTITY_CD ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TIMESTAMP", conn);
			
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				cd=rset1.getString(2);
				entity=rset1.getString(3);
				seq_no=rset1.getString(4);
				tax_cd=rset1.getString(5);
				
				invoice_type = "";
				
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
					if (i == 0) { // counterparty_abbr

						if (rset1.getString(3).equals("C")) {
							queryString = "SELECT CHA_ABBR FROM FMS7_CUS_HOUSE_AGENT_MST WHERE CHA_CD = ? ";
							invoice_type = "CH";
						
						}

						else if (rset1.getString(3).equals("S")) {
							queryString = "SELECT SURVEYOR_ABBR FROM FMS7_SURVEYOR_MST WHERE SURVEYOR_CD = ? ";
							invoice_type = "SF";
						}

						else if (rset1.getString(3).equals("V")) {
							queryString = "SELECT VESSEL_ABBR FROM FMS7_VESSEL_AGENT_MST WHERE VESSEL_CD = ? ";
							invoice_type = "VA";
						}

						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, rset1.getString(2));
						rset = stmt.executeQuery();
						if (rset.next()) {
							value = rset.getString(1).trim();
							abbr=value;
						}
						rset.close();
						stmt.close();
						
//						if(i == 0) {
//							for (int c = 0; c < counterparty_map.split(",").length; c++) {
//								if (counterparty_map.split(",")[c].contains(value+";")){
//									value = counterparty_map.split(",")[c].split(";")[1];
//									abbr = value;
//								}
//							}

					    if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        abbr = value; 
					    }
//						}
					} else if (i == 2 && rset1.getString(3).equals("C")) { // Entity_type
						value = "H";
					}
					else if (i == 5) {
						value = invoice_type;
					}
					else if (i == 6) {
						value = value.replaceAll(",", ", ");
						
						if (value.contains("MH")) {
							value = value.split(" MH")[0] + "%";
						}
						else if (value.contains("AP")) {
							value = value.split(" AP")[0] + "%";
						}
						else if (value.contains("ADD. VAT")) {
							value = value.replace("ADD. VAT", "ADVAT");
						}
					}
					/*
					 * else if (i == 10 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + ","+ seq_no + ","+ tax_cd + ","), conn, "");
				
				//

			}
			stmt1.close();
			rset1.close();

			filename = migration_setup_dir + "EXPORT/FMS_ENTITY_BU_SVC_TAX_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_BU_SVC_TAX_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_ENTITY_BU_SVC_TAX_DTL>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_BU_SVC_TAX_DTL()  ",e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_CUSTOM_TAX_STRUCT_DTL() throws SQLException, IOException {
		function_nm = "FMS_CUSTOM_TAX_STRUCT_DTL()";
		
		try {

			System.out.println("<<START>><<FMS_CUSTOM_TAX_STRUCT_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_CUSTOM_TAX_STRUCT_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "EFF_DT,TAX_STRUCT_CD,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,COMPANY_CD";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count=0;
			
			String tax_str_dt="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data

			// CUSTOM TAX
			queryString1 = "SELECT TO_CHAR(A.APP_DATE, 'DD/MM/YYYY'), A.TAX_STR_CD, A.TAX_DESC, A.REMARK, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.FLAG, '2' FROM FMS7_CARGO_TAX_MST A, FMS7_TAX_STRUCTURE B WHERE A.TAX_STR_CD = B.TAX_STR_CD AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();
			
			logger.checkpoint(fname, "EFF_DT,TAX_STRUCT_CD,TAX_STRUCT_DTL,TIMESTAMP", conn);
			
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);

				eff_dt=rset1.getString(1);
				cd=rset1.getString(2);
				tax_str_dt=rset1.getString(3);
				
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).trim().replace("'", "");
					/*
					 * if (i == 6 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
					 * }
					 */
					if (i == 2) {
						value = value.replaceAll(",", ", ");
						
						if (value.contains("MH")) {
							value = value.split(" MH")[0] + "%";
						}
						else if (value.contains("AP")) {
							value = value.split(" AP")[0] + "%";
						}
					}
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (eff_dt + "," + cd + "," +tax_str_dt+","), conn, "");

			}

			stmt1.close();
			rset1.close();

			filename = migration_setup_dir + "EXPORT/FMS_CUSTOM_TAX_STRUCT_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_CUSTOM_TAX_STRUCT_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_CUSTOM_TAX_STRUCT_DTL>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_CUSTOM_TAX_STRUCT_DTL()  ",e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}
	
	public void FMS_HOLIDAY_DTL() throws SQLException, IOException {
		function_nm = "FMS_HOLIDAY_DTL()";

		try {
			
			System.out.println("<<START>><<FMS_HOLIDAY_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_HOLIDAY_DTL>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="HOLIDAY_DT,HOLIDAY_NM,HOLIDAY_DAY,STATE_TIN,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,ENT_PROFILE,MOD_PROFILE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			queryString = "SELECT TO_CHAR(HOLIDAY_DT, 'DD/MM/YYYY hh24:mi:ss'),HOLIDAY_NM,HOLIDAY_DAY,STATE_CODE,TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'),ENT_BY,TO_CHAR(MODIFY_DT, 'DD/MM/YYYY hh24:mi:ss'),MODIFY_BY,FLAG,'2',NUll FROM FMS9_HOLIDAY_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			logger.checkpoint(fname, "HOLIDAY_DT,HOLIDAY_NM,TIMESTAMP", conn);
			
			while(rset.next())
			{
				String holi_dt = rset.getString(1);
				String holi_nm = rset.getString(2);
				
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if (i == 8) {
						value = (value.equals("N") ? "N" : "Y");
						cell.setCellValue("'" + value + "'");
					}
						else {
						cell.setCellValue("'" + value + "'");
					}
				}
				
				count++;
				logger.data(fname, (holi_dt + "," + holi_nm + ","), conn, "");
				
			}
			
			// FOR DLNG_HOLIDAY_DTL
			queryString = "SELECT TO_CHAR(HOLIDAY_DT, 'DD/MM/YYYY hh24:mi:ss'), HOLIDAY_NM, TRIM(TO_CHAR(HOLIDAY_DT, 'Day')), '27', TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), ENT_BY, NUll, NUll, FLAG,'2', NUll FROM DLNG_HOLIDAY_DTL  WHERE HOLIDAY_DT NOT IN (SELECT HOLIDAY_DT FROM FMS9_HOLIDAY_DTL) AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			logger.checkpoint(fname, "HOLIDAY_DT,HOLIDAY_NM,TIMESTAMP", conn);
				
			while(rset.next())
			{
				String holi_dt = rset.getString(1);
				String holi_nm = rset.getString(2);
					
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
						
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
						
					if (i == 8) {
						value = (value.equals("N") ? "N" : "Y");
						cell.setCellValue("'" + value + "'");
					}
						else {
						cell.setCellValue("'" + value + "'");
					}
				}
					
				count++;
				logger.data(fname, (holi_dt + "," + holi_nm + ","), conn, "");
					
			}
				
				
				stmt.close();
				rset.close();

				filename = migration_setup_dir + "EXPORT/FMS_HOLIDAY_DTL_"+start_end_dt+".xlsx";

				fileOut = new FileOutputStream(filename);

				workbook.write(fileOut);
				fileOut.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_HOLIDAY_DTL>>,", conn);
				
				logger.checkpoint1(fname1,count+",", conn);
				
				System.out.println("<<END>><<FMS_HOLIDAY_DTL>>");
				System.out.println();
				

				msg = "Data has been Extracted Successfully.";
				msg_type = "S";
				

		} catch(Exception e){

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_HOLIDAY_DTL()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
	}

	public void FMS_PRODUCT_MST() throws SQLException, IOException {
		function_nm = "FMS_PRODUCT_MST()";//DIYA 20250226:PRODUCT_MASTER TO PRODUCT_MST()

		try {
			
			System.out.println("<<START>><<FMS_PRODUCT_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_PRODUCT_MST>>,", conn);

			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="PROD_CD,PROD_NM,PROD_ABBR,PROD_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,PROD_FLAG,ENT_PROFILE,MOD_PROFILE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			queryString = "SELECT PROD_CD,PROD_NM,PROD_ABRV,PROD_DESC,EMP_CD,TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,DEL_FLAG,2,NULL FROM "
					+ "FMS7_PRODUCT_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			logger.checkpoint(fname, "PROD_CD,PROD_ABBR,TIMESTAMP", conn);
			
			while(rset.next())
			{
				String prod_cd = rset.getString(1);
				String prod_abbr = rset.getString(3);
				
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if (i == 8) {
						value = (( value.equals("null") || value.equals("N")) ? "Y" : "N");
//						System.out.println(value);
					}
//						else {
//						
//					}
					cell.setCellValue("'" + value + "'");
				}
				
				count++;
				logger.data(fname, (prod_cd + "," + prod_abbr + ","), conn, "");
				
			}
				
				stmt.close();
				rset.close();
				
				
				filename = migration_setup_dir + "EXPORT/FMS_PRODUCT_MST_"+start_end_dt+".xlsx";

				fileOut = new FileOutputStream(filename);

				workbook.write(fileOut);
				fileOut.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_PRODUCT_MST>>,", conn);
				
				logger.checkpoint1(fname1,count+",", conn);
				
				System.out.println("<<END>><<FMS_PRODUCT_MST>>");
				System.out.println();
				

				msg = "Data has been Extracted Successfully.";
				msg_type = "S";
				

		} catch(Exception e){

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_HOLIDAY_DTL()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
	}
	
	public void FMS_PRODUCT_MOLECULE_MST() throws SQLException, IOException {
		function_nm = "FMS_PRODUCT_MOLECULE_MST()";

		try {
			
			System.out.println("<<START>><<FMS_PRODUCT_MOLECULE_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_PRODUCT_MOLECULE_MST>>,", conn);

			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="PROD_CD,MOLE_CD,MOLE_NM,MOLE_ABBR,MOLE_DESC,ENT_BY,ENT_DT,MOD_BY,MOD_DT,MOLE_FLAG,ENT_PROFILE,MOD_PROFILE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			int num=0;
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			columns="PROD_CD ,MOLE_CD , MOLE_NM, MOLE_ABBR, MOLE_DESC, ENT_BY, ENT_DT, MOD_BY, MOD_DT, MOLE_FLAG, ENT_PROFILE, MOD_PROFILE";
			queryString = "SELECT PROD_NM,PROD_NM,PROD_NM,PROD_ABRV,PROD_DESC,EMP_CD,TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,'Y','2',NULL "
					+ "FROM FMS7_PRODUCT_MST "
					+ "WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			
			rset = stmt.executeQuery();
			logger.checkpoint(fname, "PROD_CD,PROD_ABBR,TIMESTAMP", conn);
			
			while(rset.next())
			{
				String prod_cd = rset.getString(1);
				String prod_abbr = rset.getString(3);
				
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if(i==0)
					{
						if(value.contains("CAP"))
						{
							value="1";
						}
						else if(value.contains("Regassified LNG") || value.contains("RLNG")
								|| value.contains("ReGasified Liquified Natural Gas") || value.contains("Liquified Natural Gas") ||  value.contains("LNG") )
						{
							value="2";
						}
						else if(value.contains("Downstream LNG"))
						{
							value="3";
						}
						else if(value.contains("Domestic Gas"))
						{
							value="4";
						}
						
						cell.setCellValue("'" + value + "'");
					}
					else if(i==1)
					{
						num++;
						cell.setCellValue("'" + num + "'");
					}
					else
					{
						cell.setCellValue("'" + value + "'");
					}
					
					
				}
				
				count++;
				logger.data(fname, (prod_cd + "," + prod_abbr + ","), conn, "");
				
			}

			stmt.close();
			rset.close();
				
			// SagarB20250828 added new product MOLECULE 
//			queryString = "SELECT MAX(PROD_CD)+1 FROM FMS7_PRODUCT_MST ";
//			stmt = conn.prepareStatement(queryString);
//			rset = stmt.executeQuery();
//			
//			if (rset.next()) {

				row = spreadsheet.createRow(nrow++);
				
				cell = row.createCell(0);
				cell.setCellValue("'5'");
				
				cell = row.createCell(1);
				cell.setCellValue("'" + (++num) + "'");

				cell = row.createCell(2);
				cell.setCellValue("'Third Party LNG'");

				cell = row.createCell(3);
				cell.setCellValue("'TPLNG'");

				cell = row.createCell(4);
				cell.setCellValue("'"+null+"'");

				cell = row.createCell(5);
				cell.setCellValue("'1'");

				cell = row.createCell(6);
				cell.setCellValue("'28/08/2025 00:00:00'");

				cell = row.createCell(7);
				cell.setCellValue("'"+null+"'");

				cell = row.createCell(8);
				cell.setCellValue("'"+null+"'");

				cell = row.createCell(9);
				cell.setCellValue("'Y'");

				cell = row.createCell(10);
				cell.setCellValue("'2'");

				cell = row.createCell(11);
				cell.setCellValue("'"+null+"'");

				count++;
				logger.data(fname, ("1,TPLNG,"), conn, "");
				
//			}
//			rset.close();
//			stmt.close();

				filename = migration_setup_dir + "EXPORT/FMS_PRODUCT_MOLECULE_MST_"+start_end_dt+".xlsx";

				fileOut = new FileOutputStream(filename);

				workbook.write(fileOut);
				fileOut.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_PRODUCT_MOLECULE_MST>>,", conn);
				
				logger.checkpoint1(fname1,count+",", conn);
				
				System.out.println("<<END>><<FMS_PRODUCT_MOLECULE_MST>>");
				System.out.println();
				

				msg = "Data has been Extracted Successfully.";
				msg_type = "S";
				

		} catch(Exception e){

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_HOLIDAY_DTL()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
	}
	
	
	public void FMS_SAC_MST() throws SQLException, IOException {
		function_nm = "FMS_SAC_MST()";
		try {
			
			System.out.println("<<START>><<FMS_SAC_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SAC_MST>>,", conn);

			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns="SAC_CD,SAC_CODE,SAC_DESC,REMARKS,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,SAC_FLAG,ENT_PROFILE,MOD_RPOFILE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			queryString = "SELECT 1, SAC_CODE,DESCRIPTION,REMARK,NULL,TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,'Y',2,NULL "
					+ "FROM FMS8_SERVICE_MST WHERE SUPPLIER_CD= '1' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			logger.checkpoint(fname, "SAC_CODE,TIMESTAMP", conn);
			
			while(rset.next())
			{
				String sac_code = rset.getString(1);
				
				row = spreadsheet.createRow(nrow++);
				for (int i = 0; i < columns.split(",").length; i++) {
					
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
					cell = row.createCell(i);
					
					if ((i == 2) && value.length() > 100) {
							value = value.replaceAll("¿","");
							value = value.replaceAll("","");
							value = value.replaceAll("","");
							value = value.substring(0,100);
//							cell.setCellValue("'" + value + "'");
						
					}
					else if (i == 4 && value.length() > 150) {
							value = value.replaceAll("¿","");
							value = value.replaceAll("","");
							value = value.replaceAll("","");
							value = value.substring(0,150);
//							cell.setCellValue("'" + value + "'");
						
					}
//						else {
//						
//					}
					cell.setCellValue("'" + value + "'");
				}
				
				count++;
				logger.data(fname, (sac_code + ","), conn, "");
				
			}
				
				
				stmt.close();
				rset.close();

				filename = migration_setup_dir + "EXPORT/FMS_SAC_MST_"+start_end_dt+".xlsx";

				fileOut = new FileOutputStream(filename);

				workbook.write(fileOut);
				fileOut.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_SAC_MST>>,", conn);
				
				logger.checkpoint1(fname1,count+",", conn);
				
				System.out.println("<<END>><<FMS_SAC_MST>>");
				System.out.println();
				

				msg = "Data has been Extracted Successfully.";
				msg_type = "S";
				

		} catch(Exception e){

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_HOLIDAY_DTL()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
	}
	


	/*
	 * public String getEmpAbbr(String emp_cd) {
	 * 
	 * String abbr = ""; try {
	 * 
	 * query_abbr = "SELECT EMP_ABR FROM HR_EMP_MST WHERE EMP_CD = ? "; stmt_abbr =
	 * conn.prepareStatement(query_abbr); stmt_abbr.setString(1, emp_cd); rset_abbr
	 * = stmt_abbr.executeQuery();
	 * 
	 * if (rset_abbr.next()) { abbr = rset_abbr.getString(1); } else { abbr =
	 * emp_cd; }
	 * 
	 * rset_abbr.close(); stmt_abbr.close();
	 * 
	 * } catch(Exception e) { LOGGER.log(Level.WARNING,
	 * "Error in Purchase_SEIPL_Data_Extractor.java -> Purchase_Excel_Extractor -> getEmpAbbr() "
	 * , e); }
	 * 
	 * return abbr; }
	 */

	public void getmail_list() {
		function_nm = "getmail_list()";
		
		try {
			String strline = "";

			File fsetup = new File(migration_setup_dir+"Migration_Setup.txt");
			String mail_list_path = fsetup.getAbsolutePath();
			
			try (FileInputStream f1 = new FileInputStream(mail_list_path)) {
			
				try (DataInputStream in = new DataInputStream(f1)) {
				
					try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) { 
		
						while ((strline = br.readLine()) != null) {
							if (strline.startsWith("dbline")) {
								String tmp[] = strline.split("dbline:");
								dbline = tmp[1].toString();
			
							}
							if (strline.startsWith("username")) {
								String tmp[] = strline.split("username:");
								username = tmp[1].toString();
							}
							if (strline.startsWith("password")) {
								String tmp[] = strline.split("password:");
								encrypted = tmp[1].toString();
								password = encrypted;
							}
						}
						
					}
				}
			}
			
		} catch (Exception e) {
			// LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> getmail_list() ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
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
//			
//			try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
//			
//				while((strline = br.readLine())!=null)
//				{
//					if(strline.startsWith("CUSTOMER-PLANTS-DELETE"))
//					{
//						String  tmp[]=strline.split("CUSTOMER-PLANTS-DELETE: ");
//						customer_delete = tmp[1].toString();
//						
//					}
//					if(strline.startsWith("TRADER-PLANTS-DELETE"))
//					{
//						String  tmp[]=strline.split("TRADER-PLANTS-DELETE: ");
//						trader_delete = tmp[1].toString();
//					}
//					if(strline.startsWith("CUSTOMER-PLANTS-MAPPING"))
//					{
//						String  tmp[]=strline.split("CUSTOMER-PLANTS-MAPPING: ");
//						 customer_map = tmp[1].toString();
//					}
//					if(strline.startsWith("TRADER-PLANTS-MAPPING"))
//					{
//						String  tmp[]=strline.split("TRADER-PLANTS-MAPPING: ");
//						trader_map = tmp[1].toString();
//					}
//					if(strline.startsWith("TRANSPORTER-MAPPING"))
//					{
//						String  tmp[]=strline.split("TRANSPORTER-MAPPING: ");
//						transporter_map = tmp[1].toString();
//					}
//					if(strline.startsWith("COUNTERPARTY-MAPPING"))
//					{
//						String  tmp[]=strline.split("COUNTERPARTY-MAPPING: ");
//						counterparty_map = tmp[1].toString();
//					}
//					if(strline.startsWith("METER-MAPPING"))
//					{
//						String  tmp[]=strline.split("METER-MAPPING: ");
//						meter_map = tmp[1].toString();
//					}
//				}
//			}
//			catch (Exception e) 
//			{
//				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e); 
//			}
//			
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

	// Setter-Getter methods
	public void setChecked_Values(String checked_val) {
		checked_values = checked_val;
	}
	
	public void setDelta_FromDt(String from_dt) {
		delta_FromDt = from_dt + " 00:00:00";
	}
	
	public void setDelta_ToDt(String to_dt) {
		delta_ToDt = to_dt + " 23:59:59";
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
