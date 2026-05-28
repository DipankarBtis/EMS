
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

public class Derivative_SEIPL_Data_Extractor {

	String db_src_file_name = "Derivative_SEIPL_Data_Extractor.java";
	String function_nm = "";
	
	String migration_setup_dir = "";
	
	String sysDateTime = "";
	
	String fname = "";
	String fname_error = "";
	
	String fname1 = "";

	DataMigration_Logger logger = new DataMigration_Logger();
	Migration_Plants_Exceptions mpe=new Migration_Plants_Exceptions();

	String queryString = "", queryString1 = "", queryString2 = "", queryString3 = "";
	Connection conn;
	ResultSet rset, rset1,rset2,rset3;
	PreparedStatement stmt, stmt1,stmt2,stmt3;

	String dbline = "", username = "", encrypted = "", password = "";
	String columns = "", filename = "", value = "";
	
	String abbr="",cont_no="",cont_ref="",agmt_no="",agmt_type="",agmt_rev="",cont_rev="",cont_type="",deal_ref="",fin_yr="";
	
	String checked_values = "", msg = "", msg_type = "",cd="";
	final String company_cd = "2";
	
	String delta_FromDt = null;
	String delta_ToDt = null;
	String start_end_dt = null;
	
	String dir_flag = "N";

	
	int nrow = 0, ncell = 0, query_ind = 0, count = 0;

	XSSFWorkbook workbook = null;
	XSSFSheet spreadsheet = null;
	XSSFRow row;
	Cell cell;
	
	FileOutputStream fileOut = null;

	public void init() {

        function_nm="init()";
		try {
			
			getmail_list();
			makeDirectory();
			
			fname = "DataLogs/Extractor/Derivative_SEIPL_Data_Extractor(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Extractor/Derivative_SEIPL_Data_Extractor_Error(log)"+sysDateTime+".csv";
			
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
				
				if (checked_values.contains("FMS_DERV_AGMT_MST,")) {
					FMS_DERV_AGMT_MST();
				}
				if (checked_values.contains("FMS_DERV_CONT_MST,")) {
					FMS_DERV_CONT_MST();
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
				if (rset2 != null) {
					try {
						rset2.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (rset3 != null) {
					try {
						rset3.close();
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
				if (stmt2 != null) {
					try {
						stmt2.close();
					} catch (SQLException e) {
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
				if (stmt3 != null) {
					try {
						stmt3.close();
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
	
	public void FMS_DERV_AGMT_MST() throws SQLException, IOException {
		function_nm = "FMS_DERV_AGMT_MST()";
		
		try {

			System.out.println("<<START>><<FMS_DERV_AGMT_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DERV_AGMT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,SIGNING_DT,START_DT,END_DT,STATUS,BILLING_FLAG,BILLING_CLAUSE,ENT_BY,ENT_DT,"
					+ "MODIFY_DT,MODIFY_BY,REV_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			ncell = 0;
			count = 0;
			String start_dt="",end_dt="";
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			logger.checkpoint(fname, "ABBR,CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TIMESTAMP,", conn);

			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				queryString1 = " SELECT A.CUSTOMER_CD, 'U', '1', '0', NULL, A.DEAL_ID, NULL, NULL, NULL, 'A', 'Y', NULL, A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "NULL, NULL, NULL FROM FMS9_MR_DERI_DEAL_DTL A, FMS7_TRADER_MST B WHERE A.CUSTOMER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
	
				if (rset1.next()) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					agmt_type =  rset1.getString(2);
				    agmt_no = rset1.getString(3);
				    agmt_rev = rset1.getString(4);
					cd = rset1.getString(1);
					deal_ref = rset1.getString(9)==null?"":rset1.getString(9);
					abbr = rset.getString(1);
					
				    if (mpe.counterparty_map.containsKey(abbr)) {
				    	 abbr =mpe.counterparty_map.get(abbr); 
				    }
						
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						
						if(i==6) {
							value = cd+"-1";
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 7) {	// SIGNING_DT
							queryString2 = "SELECT TO_CHAR(MIN(A.PRICE_START_DT), 'DD/MM/YYYY hh24:mi:ss') , '31/12/2999 00:00:00' "
									+ "FROM FMS9_MR_DERI_DEAL_DTL A, FMS7_TRADER_MST B WHERE B.COUNTERPARTY_CD = ? AND A.CUSTOMER_CD = B.TRADER_CD";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset.getString(2));
							rset2 = stmt2.executeQuery();
							if (rset2.next()) {
								start_dt = rset2.getString(1);
								end_dt = rset2.getString(2);
						    }
							rset2.close();
							stmt2.close();
							
							cell.setCellValue("'"+start_dt+"'");
						}
						else if(i == 8) { 
							cell.setCellValue("'"+start_dt+"'");
						}
						else if(i == 9) {
							cell.setCellValue("'"+end_dt+"'");
						}
						else { 
							cell.setCellValue("'" + value + "'");
						}
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + agmt_type + "," + agmt_no + "," + agmt_rev + "," ), conn, "");
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();

			filename = migration_setup_dir + "EXPORT/FMS_DERV_AGMT_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_AGMT_MST>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_DERV_AGMT_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_DERV_CONT_MST() throws SQLException, IOException {
		function_nm = "FMS_DERV_CONT_MST()";
		
		try {

			System.out.println("<<START>><<FMS_DERV_CONT_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DERV_CONT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CONT_NAME,CONT_REF_NO,CONT_STATUS,"
					+ "DDA_DT,DDA_TIME,DDA_NOTE,SIGNING_DT,SIGNING_TIME,TRADE_DT,TRADE_TIME,NO_OF_INSTRUMENT,REMARKS,BILLING_FLAG,BILLING_CLAUSE,"
					+ "ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			ncell = 0;
			count = 0;
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			logger.checkpoint(fname, "COMPANY_CD,ABBR,AGMT_NAME,AGMT_TYPE,TIMESTAMP,", conn);

			//COUNTERPARTY

			
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				queryString1 = " SELECT A.CUSTOMER_CD, 'U', '1', '0', 'V', TO_CHAR(A.ENT_DT, 'YYYY'), '0', NULL, A.DEAL_ID, 'A', "
						+ "TO_CHAR(A.DDA_DT, 'DD/MM/YYYY HH24:MI:SS'), A.DDA_TIME, NULL, TO_CHAR(A.DDA_DT, 'DD/MM/YYYY HH24:MI:SS'), A.DDA_TIME, "
						+ "TO_CHAR(A.TRADE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.TRADE_TIME, '1', A.REMARKS, 'N', NULL, A.EMP_CD, "
						+ "TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, A.DEAL_REF_NO FROM FMS9_MR_DERI_DEAL_DTL A, FMS7_TRADER_MST B WHERE A.CUSTOMER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
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
					deal_ref = rset1.getString(9)==null?"":rset1.getString(9);
					cont_ref = rset1.getString(27)==null?"":rset1.getString(27);
					cont_ref=cont_ref.trim();
					abbr = rset.getString(1);
					
				    if (mpe.counterparty_map.containsKey(abbr)) {
				    	 abbr =mpe.counterparty_map.get(abbr); 
				    }
						
//					value = rset.getString(1) == null ? "null" : rset.getString(1);
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
							if(i==9) {
								if(!cont_ref.equals("")) {
									value = deal_ref+" ("+cont_ref+")";
								}else {
									value = deal_ref;
								}
							}	
							cell.setCellValue("'" + value + "'");
					}
					count++;
					//logger.data(fname, (company_cd + "," + abbr + "," + agmt_name + "," + agmt_type + "," ), conn, "");
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();

			filename = migration_setup_dir + "EXPORT/FMS_DERV_CONT_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_CONT_MST>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_DERV_CONT_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_DERV_INSTRUMENT_MST() throws SQLException, IOException {
		function_nm = "FMS_DERV_INSTRUMENT_MST()";
		
		try {

			System.out.println("<<START>><<FMS_DERV_INSTRUMENT_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DERV_INSTRUMENT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_TYPE,CONTRACT_TYPE,CONT_NO,AGMT_REV,CONT_REV,INSTRUMENT_NO,INSTRUMENT_TYPE,BUY_SELL,STATUS,QTY,"
					+ "QTY_UNIT,RATE,RATE_UNIT,PRODUCT_NM,CURVE_NM,PROJ_METHOD,CONT_DD_MM_YR,PRICE_START_DT,PRICE_END_DT,CONV_FACTOR,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			ncell = 0;
			count = 0;
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			logger.checkpoint(fname, "COMPANY_CD,ABBR,AGMT_NAME,AGMT_TYPE,TIMESTAMP,", conn);

			//COUNTERPARTY

			
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				queryString1 = " SELECT A.DEAL_ID,NULL,NULL,NULL,NULL,NULL,NULL,'1',A.INSTRUMENT_TYPE,UPPER(A.BUY_SELL),'Y',A.QTY,UPPER(A.QTY_UNIT),"
						+ "A.RATE,A.RATE_UNIT,A.PRODUCT_NM,A.CURVE_NM,A.PROJ_METHOD,TO_CHAR(A.CONT_DD_MM_YR, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.PRICE_START_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_CHAR(A.PRICE_END_DT, 'DD/MM/YYYY HH24:MI:SS'),NVL(A.CONV_FACTOR,'1'),TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EMP_CD,NULL,NULL,A.DEAL_REF_NO "
						+ "FROM FMS9_MR_DERI_DEAL_DTL A, FMS7_TRADER_MST B WHERE A.CUSTOMER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
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
					
					abbr = rset.getString(1);
					
				    if (mpe.counterparty_map.containsKey(abbr)) {
				    	 abbr =mpe.counterparty_map.get(abbr); 
				    }
						
				    deal_ref = rset1.getString(9)==null?"":rset1.getString(1);
					cont_ref = rset1.getString(27)==null?"":rset1.getString(27);
					cont_ref=cont_ref.trim();
//					value = rset.getString(1) == null ? "null" : rset.getString(1);
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i==1) {
							if(!cont_ref.equals("")) {
								value = deal_ref+" ("+cont_ref+")";
							}
							cell.setCellValue("'" + value + "'");
							
						}
						else if(i == 13) {
							value = rset1.getString(13)==null?"":rset1.getString(13);
							if(value.equals("MMBTU")) {
								value = "1";
							}
							else if(value.equals("TBTU")) {
								value = "2";
							}
							else if(value.equals("SCM")) {
								value = "3";
							}
							else if(value.equals("MMSCM")) {
								value = "4";
							}
							else if(value.equals("MT")) {
								value = "5";
							}
							else if(value.equals("BBL")) {
								value = "6";
							}
							cell.setCellValue("'" + value + "'");
							
						}
							cell.setCellValue("'" + value + "'");
					}
					count++;
					//logger.data(fname, (company_cd + "," + abbr + "," + agmt_name + "," + agmt_type + "," ), conn, "");
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();

			filename = migration_setup_dir + "EXPORT/FMS_DERV_INSTRUMENT_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_INSTRUMENT_MST>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_DERV_INSTRUMENT_MST>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}
	
	
	public void FMS_DERV_CONT_BILLING_DTL() throws SQLException, IOException {
		function_nm = "FMS_DERV_CONT_BILLING_DTL()";
		
		try {

			System.out.println("<<START>><<FMS_DERV_CONT_BILLING_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DERV_CONT_BILLING_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
					+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,"
					+ "EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,"
					+ "EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			ncell = 0;
			count = 0;
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			logger.checkpoint(fname, "COMPANY_CD,ABBR,AGMT_NAME,AGMT_TYPE,TIMESTAMP,", conn);
			
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				queryString1 = " SELECT A.CUSTOMER_CD, NULL, NULL, NULL, NULL, NULL, NULL, 'S', 'S', A.INV_DUE_DAY, NULL, A.INVOICE_CUR_CD, A.PAYMENT_CUR_CD, "
						+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.EMP_CD, NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
						+ " '1',NULL,A.DEAL_ID, A.DEAL_REF_NO FROM FMS9_MR_DERI_DEAL_DTL A, FMS7_TRADER_MST B WHERE A.CUSTOMER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
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
					deal_ref = rset1.getString(33)==null?"":rset1.getString(33);
					cont_ref = rset1.getString(34)==null?"":rset1.getString(34);
					cont_ref=cont_ref.trim();
					abbr = rset.getString(1);
					
				    if (mpe.counterparty_map.containsKey(abbr)) {
				    	 abbr =mpe.counterparty_map.get(abbr); 
				    }
						
//					value = rset.getString(1) == null ? "null" : rset.getString(1);
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
							if(i==1) {
								if(!cont_ref.equals("")) {
									value = deal_ref+" ("+cont_ref+")";
								}
								else {
									value = deal_ref;
								}
							} 
							
							cell.setCellValue("'" + value + "'"); 
							
					}
					count++;
					//logger.data(fname, (company_cd + "," + abbr + "," + agmt_name + "," + agmt_type + "," ), conn, "");
				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();

			filename = migration_setup_dir + "EXPORT/FMS_DERV_CONT_BILLING_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_CONT_BILLING_DTL>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_DERV_CONT_BILLING_DTL>>");
			System.out.println();
			

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			

		} catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}
	

	public void FMS_DERV_INVOICE_MST() throws SQLException, IOException {
		
		function_nm = "FMS_DERV_INVOICE_MST()";	
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DERV_INVOICE_MST>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,"
					+ "PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,INVOICE_REF_NO,INV_TYPE,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,"
					+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,"
					+ "REMARK_1,REMARK_2,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,"
					+ "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,INVOICE_ID_SEQ,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,"
					+ "PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,INSTRUMENT_NO,FIN_SYS";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");

//			nrow = 0;
//			ncell = 0;
			count = 0;
			
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			int inv_seq = 0;
			
			fname_csv = migration_setup_dir + "EXPORT/FMS_DERV_INVOICE_MST_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			String seq_no = "";
			Map<String, Integer> inv_seq_no_1 = new HashMap<String, Integer>();
			logger.checkpoint(fname, "ABBR,CONT_TYPE,SEQ_NO,TIMESTAMP", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				queryString1 = " SELECT A.CUSTOMER_CD,A.DEAL_ID, A.DEAL_REF_NO,A.SEQ_NO FROM FMS9_MR_DERI_DEAL_DTL A, FMS7_TRADER_MST B "
						+ "WHERE A.CUSTOMER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					queryString2 = " SELECT B.MAPPING_ID, B.FINANCIAL_YEAR, A.FGSA_NO, A.FGSA_REV_NO, A.SN_NO, A.SN_REV_NO, 'V', A.PLANT_SEQ_NO, NULL, NULL, A.PLANT_SEQ_NO, "
				            + "NULL,B.INV_SEQ_NO, A.NEW_INV_SEQ_NO, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY HH24:MI:SS'), A.DUMMY_CARGO_NO,A.FLAG,'12', TO_CHAR(A.PERIOD_START_DT, 'DD/MM/YYYY HH24:MI:SS'), "
				            + "TO_CHAR(A.PERIOD_END_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.DUE_DT, 'DD/MM/YYYY HH24:MI:SS'),B.QUANTITY, B.RATE_SGST, A.SALES_PRICE_FLAG, "
				            + "B.SGST_AMT, B.RATE_CGST, A.SALES_PRICE_FLAG, B.CGST_AMT, A.SALES_PRICE_FLAG, A.GROSS_AMT_USD, A.NET_AMT_INR,NULL,A.REMARK_1, A.REMARK_2, "
				            + "A.CHECKED_FLAG, A.CHECKED_BY, TO_CHAR(A.CHECKED_DT, 'DD/MM/YYYY HH24:MI:SS'),A.AUTHORIZED_FLAG, A.AUTHORIZED_BY, TO_CHAR(A.AUTHORIZED_DT, 'DD/MM/YYYY HH24:MI:SS'),"
				            + " A.APPROVED_FLAG, A.APPROVED_BY, TO_CHAR(A.APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), "
				            + "A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, B.INV_SEQ_NO, A.PAY_RECV_AMT, TO_CHAR(A.PAY_RECV_DT, 'DD/MM/YYYY HH24:MI:SS'), "
				            + "A.PAY_INSERT_BY, TO_CHAR(A.PAY_INSERT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PAY_UPDATE_BY, TO_CHAR(A.PAY_UPDATE_DT, 'DD/MM/YYYY HH24:MI:SS'),A.PAY_REMARK, "
				            + "A.PDF_INV_DTL, A.PRINT_BY_ORI, TO_CHAR(A.PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_TRI, "
				            + "TO_CHAR(A.PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'), A.PRINT_BY_DUP, TO_CHAR(A.PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), A.SUN_APPROVAL, A.SUN_APPROVAL_BY, "
				            + "TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),'1',NULL FROM AC_INVOICE_MST A,AC_INVOICE_DTL B "
				            + "WHERE A.CUSTOMER_CD = ? AND A.FGSA_NO = '1' AND A.SN_NO = ? AND A.CONTRACT_TYPE = 'H' AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.FLAG = B.FLAG "
				            + "AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.HLPL_INV_SEQ_NO = B.INV_SEQ_NO "
				            + "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
				            + "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.NEW_INV_SEQ_NO";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, rset1.getString(1));
					stmt2.setString(2, rset1.getString(4));
					//stmt2.setString(3, rset1.getString(1)+"-"+rset1.getString(4));
					stmt2.setString(3, delta_FromDt);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_ToDt);
					stmt2.setString(6, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while (rset2.next()) {
					
						str = "";
						
						abbr = rset.getString(1);
						cd = rset2.getString(1);
						fin_yr = rset2.getString(2);
						
//						if (!inv_no.contains(rset2.getString(14))) {
//							inv_no += rset2.getString(14) + ",";
//							inv_seq++;
//						}
						
						
						
//						inv_seq = rset2.getString(13);

						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					       
					    }
						str += abbr + ",";
						
								
						for (int i = 1; i < columns.split(",").length; i++) {
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							value = value.replaceAll(",", " ");
							value = value.replaceAll("\n", " ");
							value = value.replaceAll("\r", " ");
							value = value.replaceAll("\"", " ");

							if(i==1) {								
								
								queryString3 = "SELECT DEAL_ID,DEAL_REF_NO FROM FMS9_MR_DERI_DEAL_DTL WHERE DEAL_ID = ?";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, rset2.getString(1));
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									deal_ref = rset3.getString(1)==null?"":rset3.getString(1);
									cont_ref = rset3.getString(2)==null?"":rset3.getString(2);
									cont_ref=cont_ref.trim();
								}
								rset3.close();
								stmt3.close();
								
								
								if(!cont_ref.equals("")) {
									value = deal_ref+" ("+cont_ref+")";
								}
								else {
									value = deal_ref+"(";
								}
								str += value + ",";
							} 
							else if (i == 13) {
								String inv_no=rset2.getString(14);
								if (inv_seq_no_1.containsKey(inv_no)) {
									inv_seq=inv_seq_no_1.get(inv_no);
								} else {
									inv_seq=inv_seq+1;
									inv_seq_no_1.put(inv_no,inv_seq);
								}
								str += inv_seq + ",";
							}
							else if(i == 17) {
								value = rset2.getString(17);
								if(value.equals("Y")){
									value = "I";
								}else if(value.equals("R")) {
									value = "R";
								}
								str += value + ",";
							}
							else if(i == 67) {
								if(rset2.getString(63)!=null) {
									str += "S,";
								}else {
									str += null + ",";
								}
							}
							else {
								value = value.replaceAll(",", " ");
								value = value.replaceAll("\n", " ");
								value = value.replaceAll("\r", " ");
								value = value.replaceAll("\"", " ");
								str += value + ",";
							}
							
						}			
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (abbr + "," + cont_type + "," + seq_no + ","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
			}
			stmt.close();
			rset.close();
			

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_INVOICE_MST>>,,,,", conn);

			logger.checkpoint1(fname1,count+",", conn);

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
		}
		catch (Exception e) {

			msg = "One of the Functions faced an Error. Extraction Terminated.";
			msg_type = "E";
			
			//LOGGER.log(Level.WARNING, "Error in Sales_SEIPL_Data_Extractor.java -> Sales_Excel_Extractor -> "+function_nm.substring(0, function_nm.length()-2)+"  ", e);
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_DERV_INV_FILE_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_DERV_INV_FILE_DTL()";
		
		try {
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DERV_INV_FILE_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,INV_TYPE,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT,PDF_CONTENT,SF_GEN_DT,SIGNED_ENT_BY";
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			
//			nrow = 0;
//			ncell = 0;
			count = 0;
			String inv_seq= "",pdf_type="",file_nm="",inv_no="";
			String o_by= "",o_dt="";
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			fname_csv = migration_setup_dir + "EXPORT/FMS_DERV_INV_FILE_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
			
			queryString = "SELECT DISTINCT(A.TRADER_CD), A.EFF_DT FROM FMS7_TRADER_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_TRADER_MST C WHERE A.TRADER_CD = C.TRADER_CD) "
					+ "ORDER BY A.TRADER_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				cd=rset.getString(1);
				
					queryString2 = "SELECT NEW_INV_SEQ_NO, HLPL_INV_SEQ_NO, PRINT_BY_ORI, TO_CHAR(PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS') FROM AC_INVOICE_MST "
							+ "WHERE CUSTOMER_CD = ? AND CONTRACT_TYPE = 'H' ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, cd);
					rset2 = stmt2.executeQuery();
				while(rset2.next()) 
				{
					
					inv_no = rset2.getString(1)== null ? null :rset2.getString(1);
					inv_seq = rset2.getString(2)== null ? null :rset2.getString(2);
					o_by = rset2.getString(3)== null ? null :rset2.getString(3);
					o_dt = rset2.getString(4)== null ? null :rset2.getString(4);
					
					queryString1 = "SELECT NULL, NULL, NULL, NULL, INV_TYPE, PDF_INV_NM, NULL, NULL, NULL, NULL, PDF_SIGNED_FLAG, SIGNED_BY, "
							+ "TO_CHAR(SIGNED_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL "
							+ " FROM FMS8_INV_PDF_DTL "
							+ " WHERE NEW_INV_SEQ_NO = ? ";

					
					stmt1 = conn.prepareStatement(queryString1);	
					stmt1.setString(1, inv_no);

					rset1 = stmt1.executeQuery();
					
					while(rset1.next())
					{	
						pdf_type = rset1.getString(5);
						file_nm = rset1.getString(6);
						
						str = "";
						
						str += inv_no + ",";
						
						for (int i = 1; i < columns.split(",").length; i++) {
							
						if(i == 2) {	//INVOICE_SEQ
							value = inv_seq;

							str += value + ",";
						}	

						 else if (i == 6) {
							
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).replaceAll(",", " ");
							if(!value.contains(".pdf")) {
								value = value + ".pdf";
							}
							
							str += value + ",";
						 }

						else if(i == 7) {	//ENT_BY
//							if(pdf_type.equals("O")) {
								value = o_by;
//							}
						
							str += value + ",";
						}
						
						else if(i == 8) {	//ENT_DT
								value = o_dt;
							
							str += value + ",";
						}
						
						else if(i == 11) {
							value = rset1.getString(11);

							str += value + ",";
						}
							
							else {
								value = rset1.getString(i) == null ? "null" : rset1.getString(i);
								str += value + ",";
							}
							
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (company_cd+",24" + ","+inv_seq+ "," +fin_yr+","+pdf_type+","+file_nm+","), conn, "");
						
						
					}					
					stmt1.close();
					rset1.close();
				}
				rset2.close();
				stmt2.close();
				}
			rset.close();
			stmt.close();
			
			
			
			
//			filename = migration_setup_dir + "EXPORT/FMS_DLNG_INV_FILE_DTL_"+start_end_dt+".xlsx";
			
//			fileOut = new FileOutputStream(filename);  
			
//			workbook.write(fileOut);
//			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_INV_FILE_DTL>>,,,,", conn);
			
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
		}
		
	}
	
	public void FMS_DERV_HEDGE_EXPOSURE_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_DERV_HEDGE_EXPOSURE_DTL()";
		
		try {
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_DERV_HEDGE_EXPOSURE_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,HEDGE_DT,BALANCE_QTY,ENT_BY,ENT_DT,FLAG";
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			
//			nrow = 0;
//			ncell = 0;
			count = 0;
			String hedge_dt="";
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			fname_csv = migration_setup_dir + "EXPORT/FMS_DERV_HEDGE_EXPOSURE_DTL_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
				
			}
			
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COMPANY_CD,HEDGE_DT,TIMESTAMP,", conn);

					queryString1 = "SELECT '2', TO_CHAR(HEDGE_DT, 'DD/MM/YYYY'), BALANCE_QTY, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), FLAG "
							+ " FROM FMS9_HEDGE_QTY_DTL ";
					stmt1 = conn.prepareStatement(queryString1);
					rset1 = stmt1.executeQuery();
					while(rset1.next())
					{		
						str = "";
						hedge_dt = rset1.getString(1) == null ? "null" : rset1.getString(1);
						
						for (int i = 1; i <= columns.split(",").length; i++) {
								value = rset1.getString(i) == null ? "null" : rset1.getString(i);
								str += value + ",";	
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						logger.data(fname, (company_cd+","+hedge_dt+","), conn, "");
						
						
					}					
					stmt1.close();
					rset1.close();

//			filename = migration_setup_dir + "EXPORT/FMS_DLNG_INV_FILE_DTL_"+start_end_dt+".xlsx";
			
//			fileOut = new FileOutputStream(filename);  
			
//			workbook.write(fileOut);
//			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DERV_HEDGE_EXPOSURE_DTL>>,,,,", conn);
			
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
