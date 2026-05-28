
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
import java.util.prefs.Preferences;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.SystemErrorLogger;

public class Interface_SEIPL_Data_Extractor {

	String db_src_file_name = "Sales_SEIPL_Data_Extractor.java";
	String function_nm = "";
	
	String migration_setup_dir = "";
	
	String sysDateTime = "";
	
	//private static final Logger LOGGER = Logger.getLogger(Sales_Excel_Extractor.class.getName());
	
	String fname = "";
	String fname_error = "";
	String fname1 = "";

	DataMigration_Logger logger = new DataMigration_Logger();
	Migration_Plants_Exceptions mpe=new Migration_Plants_Exceptions();
	
	String queryString="",queryString1="";
	Connection conn;
	ResultSet rset,rset1;
	PreparedStatement stmt,stmt1;
	
	String dbline = "", username = "", encrypted = "", password = "";
	String columns = "", filename = "", value = "";
	int nrow = 0, ncell = 0, query_ind = 0, count = 0;

	String checked_values = "", msg = "", msg_type = "",trans_abbr="",seq_no = "",qty_scm = "",qty_mmbtu="", qty_gcv="",qty_ncv="",map_id="",sn_dt="",sn_qty="",pdf_inv="";
	
	String abbr = "", agmt_no = "", agmt_rev = "", agmt_type = "", p_seq_no = "",cont_no="",cont_rev="",cont_type="",transpoter_cd="",dom_flag1="",
			cd="",cabbr="",map="",sn_req="",sn_close="",dom_flag="",buy_sale="",plant_abbr="",trans_cd="",agmt_ref="",cont_base = "",pro_cd = "",pro_abbr="",gross_trans="";
	
	String delta_FromDt = null;
	String delta_ToDt = null;
	String start_end_dt = null;
	String cont_ref = "";
	
	String dir_flag = "N";
	
	FileOutputStream fileOut;
	
	XSSFWorkbook workbook;
	XSSFSheet spreadsheet;
	XSSFRow row;
	Cell cell;

	public void init() {

        function_nm="init()";
		try {
			
			getmail_list();
			makeDirectory();
			
			fname = "DataLogs/Extractor/Interface_SEIPL_Data_Extractor(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Extractor/Interface_SEIPL_Data_Extractor_Error(log)"+sysDateTime+".csv";
			
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
				
				if (checked_values.contains("FMS_ENTITY_ACCOUNT_CODE_SUN")) {
					FMS_ENTITY_ACCOUNT_CODE_SUN();
				}
				if (checked_values.contains("FMS_TAX_STRUCTURE_SUN")) {
					FMS_TAX_STRUCTURE_SUN();
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
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
		} 
		finally {
			try {
				if (rset != null) {
					try {
						rset.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (rset1 != null) {
					try {
						rset1.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt1 != null) {
					try {
						stmt1.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				
			} catch (Exception e) {
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				
				msg = "One of the Functions faced an Error. Extraction Terminated.";
				msg_type = "E";
			}
		}

	}

	public void FMS_ENTITY_ACCOUNT_CODE_SUN() throws SQLException, IOException {
		
		function_nm = "FMS_ENTITY_ACCOUNT_CODE_SUN()";
		
		try {

			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_ACCOUNT_CODE_SUN>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ_NO,ENTITY,ACCOUNT_TYPE,ACCOUNT_CODE,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
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
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,PLANT_SEQ_NO,ENTITY,ACCOUNT_TYPE,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {	
				String plant_seq="";
				queryString1 = "SELECT A.CUSTOMER_CD,'0','C',A.ACCOUNT_TYPE,A.ACCOUNT_CD,'2',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,A.SUG_ACCOUNT_CD,A.SER_ACCOUNT_CD,A.SALES_ACCOUNT_CD FROM "
						+ "FMS7_CUSTOMER_MAPPING A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);                               
				stmt1.setString(5, delta_ToDt);

				rset1 = stmt1.executeQuery();
				while (rset1.next()) {
					
					abbr = rset.getString(1).trim();
					cd = rset1.getString(1);
					plant_seq = rset1.getString(2);
					
					String[] accountTypes = {"DFT", "UG", "SI", "S"};
					String[] accountValues = {
					    rset1.getString(5),  
					    rset1.getString(12),  
					    rset1.getString(13), 
					    rset1.getString(14) 
					};
					
						for(int c = 0; c < accountTypes.length; c++) {
							if (accountValues[c] != null) {
								row = spreadsheet.createRow(nrow++);
						        ncell = 0;
						        
						        if (mpe.counterparty_map.containsKey(abbr)) {
							    	 abbr =mpe.counterparty_map.get(abbr); 
							    }
								cell = row.createCell(ncell++);
								cell.setCellValue("'"+abbr+"'");
								
						    	for (int i = 1; i < columns.split(",").length; i++) {
						    		
						    		cell = row.createCell(ncell++);
									value = rset1.getString(i) == null ? "null" : rset1.getString(i);
									
						    		 if (i == 4) {
							             cell.setCellValue("'"+accountTypes[c]+"'");
									}
							        else if (i == 5) {
							             cell.setCellValue("'"+accountValues[c]+"'");
									}
									
									else {
										cell.setCellValue("'"+value+"'");
									}
						    	}
						    	count++;
						    	logger.data(fname, (abbr + "," + plant_seq + ","+ 'C' + "," + accountTypes[c] + ","), conn, "");
							}
						}
				}
				rset1.close();
				stmt1.close();
				
				//TRANSPORTER
				String type = "";
				queryString1 = "SELECT A.TRANSPORTER_CD,'0','R','DFT',A.ACCOUNT_CD,'2',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL FROM "
						+ "FMS7_TRANSPORTER_MAPPING A, FMS7_TRANSPORTER_MST B WHERE A.TRANSPORTER_CD = B.TRANSPORTER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);                               
				stmt1.setString(5, delta_ToDt);

				rset1 = stmt1.executeQuery();
				while (rset1.next()) {
					
					abbr = rset.getString(1).trim();
					cd = rset1.getString(1);
					plant_seq = rset1.getString(2);
					type = rset1.getString(4);
					
								row = spreadsheet.createRow(nrow++);
						        ncell = 0;
						        
						        if (mpe.counterparty_map.containsKey(abbr)) {
							    	 abbr =mpe.counterparty_map.get(abbr); 
							    }
								cell = row.createCell(ncell++);
								cell.setCellValue("'"+abbr+"'");
								
						    	for (int i = 1; i < columns.split(",").length; i++) {
						    		
						    		cell = row.createCell(ncell++);
									value = rset1.getString(i) == null ? "null" : rset1.getString(i);
									
										cell.setCellValue("'"+value+"'");
						    	}
						    	count++;
						    	logger.data(fname, (abbr + "," + plant_seq + ","+ 'R' + "," + type + ","), conn, "");
							
						
				}
				rset1.close();
				stmt1.close();
				
				//PUR_TRANSPORTER
				queryString1 = "SELECT A.TRANSPORTER_CD,'0','T',A.ACCOUNT_TYPE,A.ACCOUNT_CD,'2',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,A.PARK_ACCOUNT_CD FROM "
						+ "FMS7_PUR_TRANSPORTER_MAPPING A, FMS7_TRANSPORTER_MST B WHERE A.TRANSPORTER_CD = B.TRANSPORTER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);                               
				stmt1.setString(5, delta_ToDt);

				rset1 = stmt1.executeQuery();
				while (rset1.next()) {
					
					abbr = rset.getString(1).trim();
					cd = rset1.getString(1);
					plant_seq = rset1.getString(2);
					
					String[] accountTypes = {"R", "K"};
					String[] accountValues = {
					    rset1.getString(5),  
					    rset1.getString(12)  
					};
					
						for(int c = 0; c < accountTypes.length; c++) {
							if (accountValues[c] != null) {
								row = spreadsheet.createRow(nrow++);
						        ncell = 0;
						        
						        if (mpe.counterparty_map.containsKey(abbr)) {
							    	 abbr =mpe.counterparty_map.get(abbr); 
							    }
								cell = row.createCell(ncell++);
								cell.setCellValue("'"+abbr+"'");
								
						    	for (int i = 1; i < columns.split(",").length; i++) {
						    		
						    		cell = row.createCell(ncell++);
									value = rset1.getString(i) == null ? "null" : rset1.getString(i);
									
						    		 if (i == 4) {
							             cell.setCellValue("'"+accountTypes[c]+"'");
									}
							        else if (i == 5) {
							             cell.setCellValue("'"+accountValues[c]+"'");
									}
									
									else {
										cell.setCellValue("'"+value+"'");
									}
						    	}
						    	count++;
						    	logger.data(fname, (abbr + "," + plant_seq + ","+ 'T' + "," + accountTypes[c] + ","), conn, "");
							}
						}
				}
				rset1.close();
				stmt1.close();
				
				//TRADER MAP
				queryString1 = "SELECT A.TRADER_CD,'0','T',A.ACCOUNT_TYPE,A.ACCOUNT_CD,'2',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,A.DERI_ACCOUNT_CD FROM "
						+ "FMS7_TRADER_MAPPING A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);                               
				stmt1.setString(5, delta_ToDt);

				rset1 = stmt1.executeQuery();
				while (rset1.next()) {
					
					abbr = rset.getString(1).trim();
					cd = rset1.getString(1);
					plant_seq = rset1.getString(2);
					
					String[] accountTypes = {"DFT", "V"};
					String[] accountValues = {
					    rset1.getString(5),  
					    rset1.getString(12)  
					};
					
						for(int c = 0; c < accountTypes.length; c++) {
							if (accountValues[c] != null) {
								row = spreadsheet.createRow(nrow++);
						        ncell = 0;
						        
						        if (mpe.counterparty_map.containsKey(abbr)) {
							    	 abbr =mpe.counterparty_map.get(abbr); 
							    }
						        if (abbr.contains("RIL-D6") || abbr.contains("RIL-CBM")) {
								    abbr = "RIL";
								}
								cell = row.createCell(ncell++);
								cell.setCellValue("'"+abbr+"'");
								
						    	for (int i = 1; i < columns.split(",").length; i++) {
						    		
						    		cell = row.createCell(ncell++);
									value = rset1.getString(i) == null ? "null" : rset1.getString(i);
									
						    		 if (i == 4) {
							             cell.setCellValue("'"+accountTypes[c]+"'");
									}
							        else if (i == 5) {
							             cell.setCellValue("'"+accountValues[c]+"'");
									}
									
									else {
										cell.setCellValue("'"+value+"'");
									}
						    	}
						    	count++;
						    	logger.data(fname, (abbr + "," + plant_seq + ","+ 'T' + "," + accountTypes[c] + ","), conn, "");
							}
						}
				}
				rset1.close();
				stmt1.close();
				
				//LNG_PUR_MAPPING
				queryString1 = "SELECT A.TRADER_CD,'0','T',A.ACCOUNT_TYPE,A.ACCOUNT_CD,'2',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,A.IGX_ACCOUNT_CD FROM "
						+ "FMS7_LNG_PURCHASE_MAPPING A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);                               
				stmt1.setString(5, delta_ToDt);

				rset1 = stmt1.executeQuery();
				while (rset1.next()) {
					
					abbr = rset.getString(1).trim();
					cd = rset1.getString(1);
					plant_seq = rset1.getString(2);
					
					String[] accountTypes = {"S", "I"};
					String[] accountValues = {
					    rset1.getString(5),  
					    rset1.getString(12)  
					};
					
						for(int c = 0; c < accountTypes.length; c++) {
							if (accountValues[c] != null) {
								row = spreadsheet.createRow(nrow++);
						        ncell = 0;
						        
						        if (mpe.counterparty_map.containsKey(abbr)) {
							    	 abbr =mpe.counterparty_map.get(abbr); 
							    }
						        if (abbr.contains("RIL-D6") || abbr.contains("RIL-CBM")) {
								    abbr = "RIL";
								}
								cell = row.createCell(ncell++);
								cell.setCellValue("'"+abbr+"'");
								
						    	for (int i = 1; i < columns.split(",").length; i++) {
						    		
						    		cell = row.createCell(ncell++);
									value = rset1.getString(i) == null ? "null" : rset1.getString(i);
									
						    		 if (i == 4) {
							             cell.setCellValue("'"+accountTypes[c]+"'");
									}
							        else if (i == 5) {
							             cell.setCellValue("'"+accountValues[c]+"'");
									}
									
									else {
										cell.setCellValue("'"+value+"'");
									}
						    	}
						    	count++;
						    	logger.data(fname, (abbr + "," + plant_seq + ","+ 'R' + "," + accountTypes[c] + ","), conn, "");
							}
						}
				}
				rset1.close();
				stmt1.close();
				
				//CUSTOM_DUTY_MAPPING
				queryString1 = "SELECT A.TRADER_CD,'0','T','CD',A.ACCOUNT_CD,'2',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL FROM "
						+ "FMS7_CUSTOM_DUTY_MAPPING A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
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
					
					abbr = rset.getString(1).trim();
					cd = rset1.getString(1);
					plant_seq = rset1.getString(2);
					type = rset1.getString(4);
					  
				    if (mpe.counterparty_map.containsKey(abbr)) {
				    	 abbr =mpe.counterparty_map.get(abbr); 
					}
				    if (abbr.contains("RIL-D6") || abbr.contains("RIL-CBM")) {
					    abbr = "RIL";
					}
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
								
					for (int i = 1; i < columns.split(",").length; i++) {
						    		
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						
						cell.setCellValue("'"+value+"'");
						
					}
					 count++;
				     logger.data(fname, (abbr + "," + plant_seq + ","+ 'R' + "," + type + ","), conn, "");
				}
				rset1.close();
				stmt1.close();
				
				//TRADER_PLANT_MAP
				queryString1 = "SELECT A.TRADER_CD,A.PLANT_CD,'T','DFT',A.ACCOUNT_CD,'2',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL FROM "
						+ "FMS7_TRADER_PLANT_MAPPING A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
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
					
					abbr = rset.getString(1).trim();
					cd = rset1.getString(1);
					plant_seq = rset1.getString(2);
					type = rset1.getString(4);
					  
				    if (mpe.counterparty_map.containsKey(abbr)) {
				    	 abbr =mpe.counterparty_map.get(abbr); 
					}
				    if (abbr.contains("RIL-D6") || abbr.contains("RIL-CBM")) {
					    abbr = "RIL";
					}
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					if(mpe.trader_map.containsKey(abbr+"-"+plant_seq)) {
						plant_seq = mpe.trader_map.get(abbr+"-"+plant_seq);
						plant_seq=plant_seq.split("-")[0];
					}
								
					for (int i = 1; i < columns.split(",").length; i++) {
						    		
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						
						if (i == 2) {
				             cell.setCellValue("'"+plant_seq+"'");
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
					}
					 count++;
				     logger.data(fname, (abbr + "," + plant_seq + ","+ 'R' + "," + type + ","), conn, "");
							
				}
				rset1.close();
				stmt1.close();
				
				//LNG_PUR_PLANT_MAPPING
				queryString1 = "SELECT A.TRADER_CD,A.PLANT_CD,'T',A.ACCOUNT_TYPE,A.ACCOUNT_CD,'2',A.EMP_CD,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,A.IGX_ACCOUNT_CD FROM "
						+ "FMS7_LNG_PUR_PLANT_MAPPING A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD "
						+ "AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);                               
				stmt1.setString(5, delta_ToDt);

				rset1 = stmt1.executeQuery();
				while (rset1.next()) {
					
					
					abbr = rset.getString(1).trim();
					cd = rset1.getString(1);
					plant_seq = rset1.getString(2);
					
					String[] accountTypes = {"S", "I"};
					String[] accountValues = {
					    rset1.getString(5),  
					    rset1.getString(12)  
					};
					
						for(int c = 0; c < accountTypes.length; c++) {
							if (accountValues[c] != null) {
								row = spreadsheet.createRow(nrow++);
						        ncell = 0;
						        
						        if (mpe.counterparty_map.containsKey(abbr)) {
							    	 abbr =mpe.counterparty_map.get(abbr); 
							    }
						        if (abbr.contains("RIL-D6") || abbr.contains("RIL-CBM")) {
								    abbr = "RIL";
								}
								cell = row.createCell(ncell++);
								cell.setCellValue("'"+abbr+"'");
								
								if(mpe.trader_map.containsKey(abbr+"-"+plant_seq)) {
									plant_seq = mpe.trader_map.get(abbr+"-"+plant_seq);
									plant_seq=plant_seq.split("-")[0];
								}
								
						    	for (int i = 1; i < columns.split(",").length; i++) {
						    		
						    		cell = row.createCell(ncell++);
									value = rset1.getString(i) == null ? "null" : rset1.getString(i);
									
								    if(i == 2) {
							        	 cell.setCellValue("'"+plant_seq+"'");
							        }
								    else if (i == 4) {
							             cell.setCellValue("'"+accountTypes[c]+"'");
									}
							        else if (i == 5) {
							             cell.setCellValue("'"+accountValues[c]+"'");
									}
									
									else {
										cell.setCellValue("'"+value+"'");
									}
						    	}
						    	count++;
						    	logger.data(fname, (abbr + "," + plant_seq + ","+ 'R' + "," + accountTypes[c] + ","), conn, "");
							}
						}
				}
				rset1.close();
				stmt1.close();
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_ENTITY_ACCOUNT_CODE_SUN_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_ACCOUNT_CODE_SUN>>,,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	


	public void FMS_TAX_STRUCTURE_SUN() throws SQLException, IOException {
		
		function_nm = "FMS_TAX_STRUCTURE_SUN()";
		
		try {

			logger.checkpoint(fname, "\n<<START>>,<<FMS_TAX_STRUCTURE_SUN>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,TAX_STR_CD,TAX_CODE,BU_UNIT,BU_STATE_TIN,SUN_CODE,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE,ACCOUNT_TYPE";
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
			
			logger.checkpoint(fname, "COMPANY_CD,TAX_STR_CD,TAX_CODE,BU_UNIT,BU_STATE_TIN,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT 'P', SHT_NM, 'P', '1', 'Gujarat', SUN_ACCOUNT_CODE, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS'), '1', NULL, NULL, '2', NULL, 'S', SUN_ACC_CODE_SUG, TDS_ACCOUNT_CD, PURCHASE_ACCOUNT_CD FROM FMS7_TAX_MST WHERE SHT_NM != 'ST'";
//					+ " AND (? IS NULL OR APP_DATE >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR APP_DATE <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  ";
			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);                               
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				String tax_str = "",  pay_recv = "", bu = "", bu_state = "";
		        
				for (int j = 0; j < 4; j++) {
					if ((j == 0 && rset.getString(6) != null) || (j != 0 && rset.getString(5+j+8) != null)) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						for (int i = 0; i < columns.split(",").length; i++) {

							value = rset.getString(i+1) == null ? "null" : rset.getString(i+1);
				    		cell = row.createCell(ncell++);
							
							if (i == 1) {
								if (value.contains("Zero ST")) {
									value = "ZeroST";
								}
								else if (value.contains("ADD. VAT")) {
									value = "ADVAT";
								}
								else if (value.contains("ZERO VAT")) {
									value = "ZVAT";
								}
								else if (value.contains(" MH")) {
									value = value.replace(" MH", "");
								}
								else if (value.contains(" AP")) {
									value = value.replace(" AP", "");
								}
								tax_str = value;
								cell.setCellValue("'"+value+"'");
								
							}
							else if(i == 2 || i == 0) {
								if (j == 0) {
									value = "R";
								}
								else if (j == 1) {
									value = "S";
								}
								pay_recv = value;
								cell.setCellValue("'"+value+"'");
							}
							else if (i == 3) {
								if (rset.getString(2).contains("CST 3") || rset.getString(2).contains("MH")) {
									value = "2";
								}
								else if (rset.getString(2).contains("AP")) {
									value = "3";
								}
								bu = value;
								cell.setCellValue("'"+value+"'");
							}
							else if (i == 4) {
								if (rset.getString(2).contains("CST 3") || rset.getString(2).contains("MH")) {
									value = "Maharashtra";
								}
								else if (rset.getString(2).contains("AP")) {
									value = "Andhra Pradesh";
								}
								bu_state = value;
								cell.setCellValue("'"+value+"'");
							}
							else if (i == 5 && j != 0) {
								value = rset.getString(i+j+8);
								cell.setCellValue("'"+value+"'");
							}
							else if (i == 12 && j == 1) {
								value = "UG";
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
							
						}
				    	count++;
				    	logger.data(fname, (rset.getString(1) + "," + tax_str + ","+ pay_recv + "," + bu + "," + bu_state + ","), conn, "");
					}
					
				}

			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_TAX_STRUCTURE_SUN_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TAX_STRUCTURE_SUN>>,,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"()  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}

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
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}	
	
	public void makeDirectory() {
		function_nm = "makeDirectory()";
		try {
			String fName[] = {(migration_setup_dir+"EXPORT"), (migration_setup_dir+"DataLogs"), (migration_setup_dir+"DataLogs/RollBack"), (migration_setup_dir+"DataLogs/Extractor"), (migration_setup_dir+"DataLogs/Reader")};
			
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
