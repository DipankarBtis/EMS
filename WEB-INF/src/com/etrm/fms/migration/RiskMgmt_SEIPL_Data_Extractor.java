
package com.etrm.fms.migration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.SystemErrorLogger;

public class RiskMgmt_SEIPL_Data_Extractor {

	String db_src_file_name = "RiskMgmt_SEIPL_Data_Extractor.java";
	String function_nm = "";
	
	String migration_setup_dir = "";
	
	String pdf_path = "";
	
	String sysDateTime = "";
	
	String fname = "";
	String fname_error = "";
	
	String fname1 = "";

	DataMigration_Logger logger = new DataMigration_Logger();
	Migration_Plants_Exceptions mpe =new Migration_Plants_Exceptions();

	String queryString="",queryString1="",queryString2="",queryString3="",queryString4="";
	Connection conn;
	ResultSet rset,rset1,rset2,rset3,rset4;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4;

	String cust_abbr = "", gx = "", bank_nm = "", cd="", agmt_no = "", cont_no = "", cont_ref = "",cont_desc="";
	
	File file1 = null;
	
	String dbline = "", username = "", encrypted = "", password = "";
	String columns = "", filename = "", value = "";
	
	String checked_values = "", msg = "", msg_type = "";
	
	final String company_cd = "2";
	String abbr="",cont_type="",deal_id="";
	
	String delta_FromDt = null;
	String delta_ToDt = null;
	String start_end_dt = null;
	
	String dir_flag = "N";

	int nrow = 0;
	int count = 0;
	int ncell = 0, query_ind = 0;

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
			
			fname = "DataLogs/Extractor/RiskMgmt_SEIPL_Data_Extractor(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Extractor/RiskMgmt_SEIPL_Data_Extractor_Error(log)"+sysDateTime+".csv";
			
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
				
				if (checked_values.contains("FMS_LIMIT_MST")) {
					FMS_LIMIT_MST();
				}
				if (checked_values.contains("FMS_LIMIT_DTL")) {
					FMS_LIMIT_DTL();
				}
				if (checked_values.contains("FMS_MR_CONT_TAQ_DTL")) {
					FMS_MR_CONT_TAQ_DTL();
				}
				if (checked_values.contains("FMS_MR_EXPO_EOD_MST")) {
					FMS_MR_EXPO_EOD_MST();
				}
				if (checked_values.contains("FMS_MR_EXPO_EOD_DTL")) {
					FMS_MR_EXPO_EOD_DTL();
				}
				if (checked_values.contains("FMS_SECURITY_DEAL_MAP,")) {
					FMS_SECURITY_DEAL_MAP();
				}
				if (checked_values.contains("FMS_SECURITY_MST,")) {
					FMS_SECURITY_MST();
				}
				if (checked_values.contains("LOG_FMS_SECURITY_MST,")) {
					LOG_FMS_SECURITY_MST();
				}
				if (checked_values.contains("FMS_SECURITY_FILE_DTL,")) {
					FMS_SECURITY_FILE_DTL();
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

	public void FMS_LIMIT_MST() throws SQLException, IOException {
		function_nm = "FMS_LIMIT_MST()";
		
		try {

			System.out.println("<<START>><<FMS_LIMIT_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_LIMIT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_CD,BANK_CD,GX,LIMIT_ID,CREDIT_RATING,RATING_EFF_DT,PARENT_OWNSHIP_CD,PARENT_OWNSHIP,PARENT_ENT_DT,PARENT_EXIT_DT,REF_NO,STATUS,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			cust_abbr = ""; gx = ""; bank_nm = "";
			
			nrow = 0;
			count = 0;
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = " SELECT A.CUST_ABBR, A.BANK_CD, 'K', A.LIMIT_ID, A.CREDIT_RATING, TO_CHAR(A.RATING_EFF_DATE, 'DD/MM/YYYY HH24:MI:SS'), A.PARENT_OWNSHIP_ABBR, A.PARENT_OWNSHIP, TO_CHAR(A.PARENT_ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.PARENT_EXIT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.REF_NO, CASE WHEN A.STATUS = 'Authorized' THEN 'Y' ELSE 'N' END, A.REMARKS, A.ENT_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), '2', CASE WHEN A.MODIFY_BY IS NULL THEN NULL ELSE '2' END FROM FMS9_LIMIT_MST A WHERE (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "CUST_ABBR,LIMIT_ID,BANK_CD,GX,TIMESTAMP,", conn);
			
			while (rset.next()) {
				
				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim();
					value = value.trim().equals("") ? "null" : value;
					
					bank_nm = "0";
					gx = "K";
					
					if (i == 0) {	// Cust_Abbr
						cust_abbr = value;
						if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        cust_abbr = value; 
					    }
						else if (cust_abbr.contains("RIL")) {
					        value = "RIL"; 
					        cust_abbr = value; 
						}
						else if (value.contains("BANK")) {
							value = "0";
						}
					}
					else if (i == 1 && !value.equals("0")) {	// Bank_Name
						queryString1 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ?";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, value);
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							value = rset1.getString(1);
						}
						
						bank_nm = value;
						
						rset1.close();
						stmt1.close();
					}
					else if (i == 2 && cust_abbr.contains("IGX")) {	// GX
						value = "I";
						gx = "I";
					}
					else if (i == 6) {
						if (mpe.counterparty_map.containsKey(value)) {	// Parent_Ownship_Abbr
					        value = mpe.counterparty_map.get(value); 
					    }
					}
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cust_abbr + "," + rset.getString(3) + "," + bank_nm + "," + gx + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_LIMIT_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_LIMIT_MST>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_LIMIT_MST>>");
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

	public void FMS_LIMIT_DTL() throws SQLException, IOException {
		function_nm = "FMS_LIMIT_DTL()";
		
		try {

			System.out.println("<<START>><<FMS_LIMIT_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_LIMIT_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COUNTERPARTY_CD,BANK_CD,LIMIT_ID,SEQ_NO,GX,REF_NO,LIMIT_TYPE,ACTION_TYPE,CATEGORY,AMT,AMT_UNIT,EFF_DT,EXP_DT,REVIEW_DT,REMARKS,IS_ACTIVE,INACTIVATION_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			cust_abbr = ""; gx = ""; bank_nm = "";
			
			nrow = 0;
			count = 0;
			
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data
			queryString = " SELECT A.CUST_ABBR, A.REF_NO, A.LIMIT_ID, A.SEQ_NO, 'K', A.REF_NO, A.LIMIT_TYPE, A.ACTION_TYPE, A.CAT_USER_TYPE, A.AMT, '1',TO_CHAR(A.EFF_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), A.REMARKS, A.FLAG, TO_CHAR(A.INACTIVATION_DT, 'DD/MM/YYYY HH24:MI:SS'), A.ENT_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.MODIFY_BY, TO_CHAR(A.MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), '2',  CASE WHEN A.MODIFY_BY IS NULL THEN NULL ELSE '2' END  FROM FMS9_LIMIT_DTL A WHERE (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "CUST_ABBR,LIMIT_ID,BANK_CD,GX,SEQ_NO,TIMESTAMP,", conn);
			
			while (rset.next()) {
				
				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).trim();
					value = value.trim().equals("") ? "null" : value;
					
					bank_nm = "0";
					gx = "K";
					
					if (i == 0) {	// Cust_Abbr
						cust_abbr = value;
						if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        cust_abbr = value; 
					    }
						else if (cust_abbr.contains("RIL")) {
					        value = "RIL"; 
					        cust_abbr = value; 
						}
						else if (value.contains("BANK")) {
							value = "0";
						}
					}
					else if (i == 1 && !value.equals("null")) {	// Bank_Name
						queryString1 = "SELECT A.BANK_NAME FROM FMS7_BANK_MST A, FMS9_LIMIT_MST B WHERE A.BANK_CD = B.BANK_CD AND B.REF_NO = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, value.split("-L")[0]);
						rset1 = stmt1.executeQuery();
						
						if (rset1.next()) {
							value = rset1.getString(1);
						}
						else {
							value = "0";
						}
						
						bank_nm = value;
						
						rset1.close();
						stmt1.close();
					}
					else if (i == 4 && cust_abbr.contains("IGX")) {	// GX
						value = "I";
						gx = "I";
					}
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (cust_abbr + "," + rset.getString(3) + "," + bank_nm + "," + gx + "," + rset.getString(4) + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = migration_setup_dir + "EXPORT/FMS_LIMIT_DTL_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_LIMIT_DTL>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_LIMIT_DTL>>");
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
	
	
	
public void FMS_MR_CONT_TAQ_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_MR_CONT_TAQ_DTL()";
		try {

			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			logger.checkpoint(fname, "<<START>>,<<FMS_MR_CONT_TAQ_DTL>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,ASED_TCQ,ASED_DCQ,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String remark="", cargo_ref="", dom_flag="", dom_flag1="";
			
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				
				queryString1 = "SELECT NULL,CP_CD, AGMT_NO, CONT_NO,CONT_TYPE,SEQ_NO, "
						+ "TO_CHAR(FROM_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(TO_DT, 'DD/MM/YYYY HH24:MI:SS'),TAQ,DCQ, "
						+ "REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),ENT_BY,NULL, NULL "
						+ "FROM FMS9_CONT_TAQ_DTL WHERE CP_CD = ? "
						+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
					cd = rset1.getString(2);
					remark = rset1.getString(11);
					cont_type = rset1.getString(5);
					agmt_no = rset1.getString(3);
					cont_no = rset1.getString(4);
					
					 
					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1);
						
						if (i == 0) { 
							if(cont_type.equals("B")) {
								queryString2="SELECT TRADER_ABBR FROM FMS7_TRADER_MST WHERE TRADER_CD=?";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, cd);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									abbr = rset2.getString(1);
									if (mpe.counterparty_map.containsKey(abbr)) {
										abbr =mpe.counterparty_map.get(abbr); 
								    }
									if (abbr.contains("RIL-D6")) {
									    abbr = "RIL";
									}
								}
								
								stmt2.close();
								rset2.close();
							}
							else {
								queryString2="SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD=?";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, cd);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									abbr = rset2.getString(1);
									if (mpe.counterparty_map.containsKey(abbr)) {
										abbr =mpe.counterparty_map.get(abbr); 
								    }
									if (abbr.contains("RIL-D6")) {
									    abbr = "RIL";
									}
								}
								stmt2.close();
								rset2.close();
							}
							cell.setCellValue("'"+abbr+"'");
						}
						else if(i == 1) {
							if(cont_type.equals("B")) {
								queryString2 = "SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD= ?";
					    		stmt2 = conn.prepareStatement(queryString2);
					    		stmt2.setString(1,agmt_no);
					    		rset2 = stmt2.executeQuery();
					    		
					    		if (rset2.next()) {
					    			dom_flag = rset2.getString(1);
					    			
					    			if(dom_flag == null || dom_flag.equals("N")) {
					    				value = agmt_no;
					    			}
					    			else if(dom_flag.equals("Y")) {
					    				dom_flag1 = "D";
					    				value = "-"+dom_flag1+"-"+agmt_no+"-";
					    			}
					    		}
							}
							else if(cont_type.equals("L")) {
								cont_ref = "L-"+agmt_no+"-"+cont_no;
								value = cont_ref;
							}
							else if(cont_type.equals("K")) {
								cont_ref = "X-"+cd+"-"+cont_no;
								value = cont_ref;
							}
							else {
								value = agmt_no;
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 4) {
							if(cont_type.equals("B") && dom_flag.equals("N")) {
								cont_type =  "N";
								value = cont_type;
							}
							else {
								value = cont_type;
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 10) {
							if (remark!=null && remark.length() > 150) {
					            remark = remark.substring(0, 150);
					        }
							cell.setCellValue("'"+remark+"'");
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + agmt_no + ","  + cont_no + ","  + cont_type + ","), conn, "");
					
				}
				rset1.close();
				stmt1.close();
			}
			stmt.close();
			rset.close();
			filename = migration_setup_dir + "EXPORT/FMS_MR_CONT_TAQ_DTL_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 

			System.out.println("<<END>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_MR_CONT_TAQ_DTL>>,,,,", conn);

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

public void FMS_MR_EXPO_EOD_MST() throws SQLException, IOException {
	
	function_nm = "FMS_MR_EXPO_EOD_MST()";
	
	try {

		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_MR_EXPO_EOD_MST>>,,,,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);

		columns = "COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,COUNTERPARTY_NM,CONTRACT_TYPE,MAPPING_ID,CONT_REF,CONT_SIGN_DT,CONT_START_DT,CONT_END_DT,CONT_ENT_BY,CONT_ENT_DT,"
				+ "CONT_APRV_BY,CONT_APRV_DT,CONT_STATUS,PRICE_TYPE,CONT_PRICE,RATE_UNIT,PHYS_CURVE,FIN_CURVE,TOT_DCQ,TOT_ALLOC_QTY,TOT_ORI_EXPO_PHY,TOT_ORI_EXPO_FIN,"
				+ "TOT_UNR_EXPO_PHY,TOT_UNR_EXPO_FIN,TOT_R_EXPO_PHY,TOT_R_EXPO_FIN,TOT_UNR_PHY_LEG,TOT_UNR_FIN_LEG,TOT_R_FIN_LEG,TOT_MTM_TOTAL,PHYS_FWD_PRICE_DT,FIN_FWD_PRICE_DT,"
				+ "ENT_BY,ENT_DT,DEAL_NUM";
		count = 0;
		
		String fname_csv = "", str = "", deal_ref = "", contract_type = "", name = "", sn_no = "", sn_rev = "";
		fname_csv = migration_setup_dir + "EXPORT/FMS_MR_EXPO_EOD_MST_" + start_end_dt + ".csv";
		
		FileWriter fw = new FileWriter(fname_csv, false); 
		fw.close();
		
		// Inserting Column Names
		for (int i = 0; i < columns.split(",").length; i++) {
			str += columns.split(",")[i] + ",";
		}
		logger.insert_data(fname_csv, str, conn);

		
		// Inserting Rest of the data
		queryString = "SELECT '2', TO_CHAR(A.REPORT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.BUY_SELL, A.CUSTOMER_CD, A.CUSTOMER_NM, NULL, A.DEAL_ID, A.DEAL_REF_NO,"
				+ " TO_CHAR(A.DEAL_SIGN_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.DEAL_START_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.DEAL_END_DT, 'DD/MM/YYYY HH24:MI:SS'), "
				+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, A.PHYS_CURVE_NM, NULL, A.TOT_DCQ, A.TOT_ALLOC_QTY,"
				+ " A.TOT_ORI_EXPO_PHY, A.TOT_ORI_EXPO_FIN, A.TOT_UNR_EXPO_PHY, A.TOT_UNR_EXPO_FIN, A.TOT_R_EXPO_PHY, A.TOT_R_EXPO_FIN, A.TOT_UNR_PHY_LEG,"
				+ " A.TOT_UNR_FIN_LEG, A.TOT_R_FIN_LEG, A.TOT_MTM_TOTAL, NULL, NULL, A.ENT_BY, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL"
				+ " FROM FMS9_EOD_EXPOSURE_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
				+ " B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
				+ " AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
		stmt = conn.prepareStatement(queryString);	
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		
		logger.checkpoint(fname, "COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,CONTRACT_TYPE,MAPPING_ID,TIMESTAMP,", conn);
		while(rset.next())
		{	
			
			str = "";
			abbr = rset.getString(4);
			name = rset.getString(5);
			deal_id = rset.getString(7);
			deal_ref = rset.getString(8);
//			 String[] parts = deal_id.split("-");
		    cont_type = deal_id.split("-")[0];
		    cont_type = cont_type.substring(0,1);
		        
			for (int i = 0; i < columns.split(",").length; i++) {	
				value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
			
				
			    if(i==3) {
			    	if(cont_type.equals("B")) {
			    		queryString1 = "SELECT TRADER_ABBR, TRADER_NAME FROM FMS7_TRADER_MST WHERE TRADER_CD = ? ";
			    		stmt1 = conn.prepareStatement(queryString1);
			    		stmt1.setString(1, abbr);
			    		rset1 = stmt1.executeQuery();
			    		if(rset1.next()) {
			    			abbr = rset1.getString(1);
			    			name = rset1.getString(2);
			    		}
			    		rset1.close();
			    		stmt1.close();
			    	}
			    	else {
			    		queryString1 = "SELECT CUSTOMER_ABBR, CUSTOMER_NAME FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
			    		stmt1 = conn.prepareStatement(queryString1);
			    		stmt1.setString(1, abbr);
			    		rset1 = stmt1.executeQuery();
			    		if(rset1.next()) {
			    			abbr = rset1.getString(1);
			    			name = rset1.getString(2);
			    		}
			    		rset1.close();
			    		stmt1.close();
			    	}

					if(abbr.contains("RIL")) {
						abbr = "RIL";
					}
			    	if (mpe.counterparty_map.containsKey(abbr)) {
				    	 abbr =mpe.counterparty_map.get(abbr); 
				    }
			    	value=abbr;
			    	
					str += value + ",";
				}
			    else if (i == 4) {	//COUNTERPARTY_NM
			    	value = name;
			    	
			    	str += value + ",";
			    }
			    else if(i==5) {		//CONTRACT_TYPE
				    if(cont_type.equals("B")) {
				    	queryString1 = "SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE CARGO_REF_CD = ? ";
				    	stmt1 = conn.prepareStatement(queryString1);
				    	stmt1.setString(1, deal_ref);
				    	rset1 = stmt1.executeQuery();
				    	if(rset1.next()) {
				    		contract_type = rset1.getString(1);
				    	}
				    	rset1.close();
				    	stmt1.close();
				    	
				    	if(contract_type == null || contract_type.equals("N")) {
				    		value = "N";
				    	}
				    	else if(contract_type.equals("Y")) {
				    		value = "D";
				    	}
				    	else if(contract_type.equals("K")) {
				    		value = "I";
				    	}
				    	else {
				    		value = contract_type;
				    	}
				    }
				    else if(cont_type.equals("K")) {
				    	value = "X";
				    }
				    else if(cont_type.equals("P")) {
				    		value = null;
				    }
				    else if(cont_type.equals("E")) {
				    	value = "F";
				    }
				    else if(cont_type.equals("F")) {
				    	value = "E";
				    }
				    else if(cont_type.equals("O")) {
				    	value = "Z";
				    }
				    else {
				    	value = cont_type;
				    }
				    cont_type = value ;
			    	str += value + ",";
			    }
			    
			    else if(i == 7 && cont_type!=null) {	//CONT_REF
			    	if(cont_type.equals("N")) {
			    		value = deal_ref;
			    	}
			    	else if(cont_type.equals("S")) {
			    		value = "S-"+deal_id.split("-")[1]+"-"+deal_id.split("-")[2]+"-"+deal_id.split("-")[3]+"-"+deal_id.split("-")[4];
			    	}
			    	else if(cont_type.equals("D") || cont_type.equals("I") || cont_type.equals("T")) {
			    		queryString1 = "SELECT SN_NO, SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE DOM_BUY_FLAG = ? AND CARGO_SEL LIKE ? "; 
			    		stmt1 = conn.prepareStatement(queryString1);
			    		stmt1.setString(1, contract_type);
			    		stmt1.setString(2, "%"+deal_ref+"%");
			    		rset1 = stmt1.executeQuery();
			    		if(rset1.next()) {
			    			sn_no = rset1.getString(1);
			    			sn_rev = rset1.getString(2);
			    		}
			    		rset1.close();
			    		stmt1.close();
			    		
			    		value = abbr +"-"+sn_no+"-"+deal_id.split("-")[4]+"-"+cont_type+"-"+deal_ref+"-"+sn_rev;
			    	}
			    	else if(cont_type.equals("F")) {
			    		value = "S-"+deal_id.split("-")[1]+"-"+deal_id.split("-")[2]+"-"+deal_id.split("-")[3]+"-"+deal_id.split("-")[4];
			    	}
			    	else if(cont_type.equals("E")) {
			    		value = "L-"+deal_id.split("-")[1]+"-"+deal_id.split("-")[3]+"-"+deal_id.split("-")[4];
			    	}
			    	else {
			    		value = cont_type+"-"+deal_id.split("-")[1]+"-"+deal_id.split("-")[3]+"-"+deal_id.split("-")[4];
			    	}
			    	str += value + ",";	
			    }
				else
				{																			
					str += value + ",";	
				}
			}
			
			logger.insert_data(fname_csv, str, conn);
			count++;
			logger.data(fname, ( ","), conn, "");
				
		}				
		stmt.close();
		rset.close();

		
		logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
		logger.checkpoint(fname, "<<END>>,<<FMS_MR_EXPO_EOD_MST>>,,,,,", conn);
		
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

public void FMS_MR_EXPO_EOD_DTL() throws SQLException, IOException {
	
	function_nm = "FMS_MR_EXPO_EOD_DTL()";
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_MR_EXPO_EOD_DTL>>,,,,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns = "COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,CONTRACT_TYPE,MAPPING_ID,GAS_DT,CONT_MTH,DCQ,ALLOC_QTY,SEQ_NO,PRICE_TYPE,FIN_CURVE_NM,CONT_PRICE,RATE_UNIT,SPOT_MTH,SPOT_START_DT,"
				+ "SPOT_END_DT,SETTLE_PRICE,RU_PHY_FLAG,RU_FIN_FLAG,FWD_PRICE_PHY,FWD_PRICE_FIN,SLOPE,CONST,EFF_RATE_USD,ORI_EXPO_PHY,ORI_EXPO_FIN,UNR_EXPO_PHY,UNR_EXPO_FIN,R_EXPO_PHY,R_EXPO_FIN,"
				+ "UNR_PHY_LEG,UNR_FIN_LEG,R_FIN_LEG,MTM_TOTAL,ENT_BY,ENT_DT,PHY_CURVE_NM,WA_RATE,QTY_UNIT";
		count = 0;
		
		String fname_csv = "", str = "", man_conf = "", man_cd="", cargo_seq="" ,contract_type = "", name = "", sn_no = "", sn_rev = "";
		fname_csv = migration_setup_dir + "EXPORT/FMS_MR_EXPO_EOD_DTL_" + start_end_dt + ".csv";
		
		FileWriter fw = new FileWriter(fname_csv, false); 
		fw.close();
		
		// Inserting Column Names
		for (int i = 0; i < columns.split(",").length; i++) {
			str += columns.split(",")[i] + ",";
		}
		logger.insert_data(fname_csv, str, conn);
		
		
		// Inserting Rest of the data
		queryString = "SELECT '2', TO_CHAR(A.REPORT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, A.CUSTOMER_CD, NULL, A.DEAL_ID, TO_CHAR(A.DELV_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.CONT_MTH, 'DD/MM/YYYY HH24:MI:SS'),"
				+ " A.DCQ, A.ALLOC_QTY, A.SEQ_NO, A.PRICE_TYPE, A.CURVE_NM, A.CONT_PRICE, '2', TO_CHAR(A.INDEX_SETTLE_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.INDEX_START_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.INDEX_END_DT, 'DD/MM/YYYY HH24:MI:SS'), A.SETTLE_PRICE, A.RU_PHY_FLAG, A.RU_FIN_FLAG, A.FWD_PRICE_PHY, A.FWD_PRICE_FIN,"
				+ " A.SLOPE, A.CONST, A.CONT_PRICE, A.ORI_EXPO_PHY, A.ORI_EXPO_FIN, A.UNR_EXPO_PHY, A.UNR_EXPO_FIN, A.R_EXPO_PHY, A.R_EXPO_FIN, A.UNR_PHY_LEG, A.UNR_FIN_LEG, A.R_FIN_LEG,"
				+ " A.MTM_TOTAL, A.ENT_BY, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), A.PHYS_CURVE_NM,"
				+ " NULL, '1' "
				+ " FROM FMS9_EOD_EXPOSURE_DTL A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
				+ " B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) AND (? IS NULL OR A.REPORT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))"
				+ " AND (? IS NULL OR A.REPORT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
		stmt = conn.prepareStatement(queryString);	
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		
		logger.checkpoint(fname, "COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,CONTRACT_TYPE,MAPPING_ID,TIMESTAMP,", conn);
		while(rset.next())
		{	
			str = "";
			abbr = rset.getString(4);
//			name = rset.getString(5);
			deal_id = rset.getString(6);
			
//			 String[] parts = deal_id.split("-");
			cont_type = deal_id.split("-")[0];
			cont_type = cont_type.substring(0,1);
			if(cont_type.equals("B")) {
				man_conf = deal_id.split("-")[3];
				man_cd = deal_id.split("-")[1];
				cargo_seq = deal_id.split("-")[4];
			}
			
			for (int i = 0; i < columns.split(",").length; i++) {	
				value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
				
				if(i == 2) {	//BUY_SELL
					if(cont_type.equals("B")) {
						value = "Buy";
					}
					else {
						value = "Sell";
					}
					str += value + ",";
				}
				else if(i == 3) {
					if(cont_type.equals("B")) {
						queryString1 = "SELECT TRADER_ABBR, TRADER_NAME FROM FMS7_TRADER_MST WHERE TRADER_CD = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, abbr);
						rset1 = stmt1.executeQuery();
						if(rset1.next()) {
							abbr = rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
					}
					else {
						queryString1 = "SELECT CUSTOMER_ABBR, CUSTOMER_NAME FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, abbr);
						rset1 = stmt1.executeQuery();
						if(rset1.next()) {
							abbr = rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
					}
					
					if(abbr.contains("RIL")) {
						abbr = "RIL";
					}
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
					}
					value=abbr;
					
					str += value + ",";
				}
				else if(i == 4) {		//CONTRACT_TYPE
					if(cont_type.equals("B")) {
						queryString1 = "SELECT DOM_BUY_FLAG FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CONF_CD = ? AND MAN_CD = ? AND CARGO_SEQ_NO = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, man_conf);
						stmt1.setString(2, man_cd);
						stmt1.setString(3, cargo_seq);
						rset1 = stmt1.executeQuery();
						if(rset1.next()) {
							contract_type = rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						if(contract_type == null || contract_type.equals("N")) {
							value = "N";
						}
						else if(contract_type.equals("Y")) {
							value = "D";
						}
						else if(contract_type.equals("K")) {
							value = "I";
						}
						else {
							value = contract_type;
						}
					}
					else if(cont_type.equals("K")) {
						value = "X";
					}
					else if(cont_type.equals("P")) {
						value = null;
					}
					else if(cont_type.equals("E")) {
						value = "F";
					}
					else if(cont_type.equals("F")) {
						value = "E";
					}
					else if(cont_type.equals("O")) {
						value = "Z";
					}
					else {
						value = cont_type;
					}
					cont_type = value ;
					str += value + ",";
				}
				
				else if(i == 25) {	//EFF_RATE_USD
					if(value.equals("0")) {
						value = null;
					}
					str += value + ",";
				}
				else
				{																			
					str += value + ",";	
				}
			}
			
			logger.insert_data(fname_csv, str, conn);
			count++;
			logger.data(fname, ( ","), conn, "");
			
		}				
		stmt.close();
		rset.close();
		
		
		logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
		logger.checkpoint(fname, "<<END>>,<<FMS_MR_EXPO_EOD_DTL>>,,,,,", conn);
		
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

public void FMS_SECURITY_DEAL_MAP() throws SQLException, IOException {
	
	function_nm = "FMS_SECURITY_DEAL_MAP()";
	String base="",sec_ref_no="";
	String link="",cargo_ref_cd="",cont_type="",man_cnf_cd="",trd_abbr="",sn_rev="", trd_cd="",sn_no="",sn_rev_no="",customer_cd="",bu_seq_no="",cargo_seq_no="",dom_buy_flag="";

	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_SECURITY_DEAL_MAP>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
				+ "CONTRACT_TYPE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT";	
		
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


//		PURCHASE:
		int seq_no1=0;
		int map_seq_no1=0;
		Map<String, Integer> map_seq_no = new HashMap<String, Integer>();
		
		queryString= "SELECT LINK,CUSTOMER_CD, SEQ_NO,CUSTOMER_ABBR,"
				+ "REF_NO, NULL, NULL, NULL, NULL, NULL, ENT_CD, TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
				+ " TO_CHAR(MODIFY_DT,'DD/MM/YYYY HH24:MI:SS'), APRV_BY, TO_CHAR(APRV_DT,'DD/MM/YYYY HH24:MI:SS'), "
				+ "NULL, NULL, 'K', NULL FROM FMS9_SECURITY_POST_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
				+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_ABBR DESC";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		
		logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
		
		while(rset.next())
		{	
		
//			
			if(rset.getString(1)!=null  && (rset.getString(4)!=null && !rset.getString(4).contains("AM/NS-T")  && !rset.getString(4).contains("RIL-D6") && !rset.getString(4).contains("RIL-CBM") )  ) 
			{
				
			if(rset.getString(1).startsWith("B")) {
				
			link=rset.getString(1);

			
			customer_cd=rset.getString(2);
			trd_abbr=rset.getString(4);
			
			sec_ref_no=rset.getString(5);
			
			sn_no="";
			sn_rev="";
			
			
			man_cnf_cd=link.split("-")[0];
			man_cnf_cd=man_cnf_cd.substring(1);
			cargo_seq_no=link.split("-")[1];
			
			
			 queryString1="SELECT CARGO_REF_CD,NVL(DOM_BUY_FLAG,'N') FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CONF_CD= ? AND CARGO_SEQ_NO = ? ";
			 stmt1=conn.prepareStatement(queryString1);
			 stmt1.setString(1, man_cnf_cd);
			 stmt1.setString(2,cargo_seq_no );
			 rset1=stmt1.executeQuery();
			 while(rset1.next()) {
				 cargo_ref_cd=rset1.getString(1);
				 dom_buy_flag=rset1.getString(2);
				 
				
				 
				 
				 queryString2="SELECT SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG= ?  "
				 		+ "ORDER BY CUSTOMER_CD,DOM_BUY_FLAG,SN_NO,SN_REV_NO DESC";
				 stmt2=conn.prepareStatement(queryString2);
				 stmt2.setString(1,"%"+cargo_ref_cd+"%");
				 stmt2.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);

				 rset2=stmt2.executeQuery();
				 if(rset2.next()) {
					do {
						 sn_no=rset2.getString(1);
						 sn_rev=rset2.getString(2);
						 row = spreadsheet.createRow(nrow++);
							 trd_abbr=trd_abbr.trim();
							 value=trd_abbr;
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value); 
							        trd_abbr = value; 
							    }
							 
							 for (int i = 0; i < columns.split(",").length; i++) {
									
									value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
									cell = row.createCell(i);
									
									if(i==0) {
										 if (dom_buy_flag == null) {
											 cont_type = "N";  // Handle null first
											}else if (dom_buy_flag.equals("Y")) {
												cont_type = "D";
											} else if (dom_buy_flag.equals("K")) {
												cont_type = "I";
											} else if (dom_buy_flag.equals("T")) {
//												dom_buy_flag="Y";
												cont_type = "T";
											}else if (dom_buy_flag.equals("N")) {
												cont_type = "N";
											}	 
										 
										 value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";
										 cell.setCellValue("'"+value+"'");
										
									}
									 if(i==1) {
										man_cnf_cd=link.split("-")[0];
										
										man_cnf_cd=man_cnf_cd.substring(1,man_cnf_cd.length());
										value=man_cnf_cd;
									}
									 if(i==2)//SEQ_NO FROM FMS_DIRECT
									{
										value=rset.getString(3);
										seq_no1=Integer.parseInt(value);
									}
									 if(i==3) {
										 
										 if(map_seq_no.containsKey(trd_abbr+seq_no1)) {
											map_seq_no1 = (map_seq_no.get(trd_abbr+seq_no1)+1);
											map_seq_no.put(trd_abbr+seq_no1, map_seq_no1);
										}
										else {
											map_seq_no1=0;
											map_seq_no.put(trd_abbr+seq_no1, map_seq_no1);
										}
										
										 value=map_seq_no1+"";
										cell.setCellValue("'"+value+"'");
									}
									 if(i==4)//SEC_REF_NO _FROM FMS_DIRECT
									{
										
										 value=rset.getString(5);
										 cell.setCellValue("'"+rset.getString(5)+"'");
									
									}
									 if(i==16) {
										if(sec_ref_no.contains("-V")) {
										sec_ref_no=sec_ref_no.split("-V")[1];
										}
										value="0";
									}
									 if(i==18)//GX
									{
										if(trd_abbr.contains("IGX"))
										{
											value="I";
										}
										else
										{
											value="K";
										}
									}
//									else
//									{
										cell.setCellValue("'" + value + "'");
//									}
									
									
								}count++;
					 } while(rset2.next());
					 stmt2.close();
					 rset2.close();
				 }
				 else if(!dom_buy_flag.equals("N"))
				 {
//					 System.out.println("=>"+cargo_ref_cd);
					 row = spreadsheet.createRow(nrow++);
					 trd_abbr=trd_abbr.trim();
					 value=trd_abbr;
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        trd_abbr = value; 
					    }
//					 if(trd_abbr.contains("RIL")) {
//						 trd_abbr="RIL";
//					 }
//					 if(trd_abbr.contains("AMNS-T") || trd_abbr.contains("AM/NS-T") ) {
//						 trd_abbr="AMNS";
//					 }
					
					 for (int i = 0; i < columns.split(",").length; i++) {
							
							value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
							cell = row.createCell(i);
							
							if(i==0) {
								 if (dom_buy_flag == null) {
									 cont_type = "N";  // Handle null first
									}else if (dom_buy_flag.equals("Y")) {
										cont_type = "D";
									} else if (dom_buy_flag.equals("K")) {
										cont_type = "I";
									} else if (dom_buy_flag.equals("T")) {
//										dom_buy_flag="Y";
										cont_type = "T";
									}else if (dom_buy_flag.equals("N")) {
										cont_type = "N";
									}	 
								 
								 value = trd_abbr+"-"+0+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+0+"=PUR";
								 cell.setCellValue("'"+value+"'");
								
							}
							 if(i==1) {
								man_cnf_cd=link.split("-")[0];
								man_cnf_cd=man_cnf_cd.substring(1,man_cnf_cd.length());
								value=man_cnf_cd;
							}
							 if(i==2)//SEQ_NO FROM FMS_DIRECT
								{
									value=rset.getString(3);
									seq_no1=Integer.parseInt(value);
								}
							 if(i==3) {
								 
								 if(map_seq_no.containsKey(trd_abbr+seq_no1)) {
									map_seq_no1 = (map_seq_no.get(trd_abbr+seq_no1)+1);
									map_seq_no.put(trd_abbr+seq_no1, map_seq_no1);
								}
								else {
									map_seq_no1=0;
									map_seq_no.put(trd_abbr+seq_no1, map_seq_no1);
								}
								
								 value=map_seq_no1+"";
								cell.setCellValue("'"+value+"'");
							}
							else if(i==4)//SEC_REF_NO _FROM FMS_DIRECT
							{
								value=rset.getString(5);
								cell.setCellValue("'"+rset.getString(5)+"'");
							
							}
							 if(i==16) {
								if(sec_ref_no.contains("-V")) {
								sec_ref_no=sec_ref_no.split("-V")[1];
								}
								value="0";
							}
							 if(i==18)//GX
							{
								 if(trd_abbr.contains("IGX"))
									{
										value="I";
									}
									else
									{
										value="K";
									}
							}
//							else
//							{
								cell.setCellValue("'" + value + "'");
//							}
							
						}count++;
				 }
				 
				 if(dom_buy_flag.equals("N"))
				 {
					
					 row = spreadsheet.createRow(nrow++);
					 trd_abbr=trd_abbr.trim();
					 value=trd_abbr;
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        trd_abbr = value; 
					    }
//					 if(trd_abbr.contains("RIL")) {
//						 trd_abbr="RIL";
//					 }
//					 if(trd_abbr.contains("AMNS-T") || trd_abbr.contains("AM/NS-T") ) {
//						 trd_abbr="AMNS";
//					 }
					
					 for (int i = 0; i < columns.split(",").length; i++) {
							
							value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
							cell = row.createCell(i);
							
							if(i==0) {
								 if (dom_buy_flag == null) {
									 cont_type = "N";  // Handle null first
									}else if (dom_buy_flag.equals("Y")) {
										cont_type = "D";
									} else if (dom_buy_flag.equals("K")) {
										cont_type = "I";
									} else if (dom_buy_flag.equals("T")) {
//										dom_buy_flag="Y";
										cont_type = "T";
									}else if (dom_buy_flag.equals("N")) {
										cont_type = "N";
									}	 
								 
								 value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";
								 cell.setCellValue("'"+value+"'");
								
							}
							 if(i==1) {
								man_cnf_cd=link.split("-")[0];
								
								man_cnf_cd=man_cnf_cd.substring(1,man_cnf_cd.length());
								value=man_cnf_cd;
							}
							 if(i==2)//SEQ_NO FROM FMS_DIRECT
								{
									value=rset.getString(3);
									seq_no1=Integer.parseInt(value);
								}
							 if(i==3) {
								 
								 if(map_seq_no.containsKey(trd_abbr+seq_no1)) {
									map_seq_no1 = (map_seq_no.get(trd_abbr+seq_no1)+1);
									map_seq_no.put(trd_abbr+seq_no1, map_seq_no1);
								}
								else {
									map_seq_no1=0;
									map_seq_no.put(trd_abbr+seq_no1, map_seq_no1);
								}
								
								 value=map_seq_no1+"";
								cell.setCellValue("'"+value+"'");
							}
							if(i==4)//SEC_REF_NO _FROM FMS_DIRECT
							{
								value=rset.getString(5);
								cell.setCellValue("'"+rset.getString(5)+"'");
							
							}
							if(i==16) {
								if(sec_ref_no.contains("-V")) {
								sec_ref_no=sec_ref_no.split("-V")[1];
								}
								value="0";
								 cell.setCellValue("'"+value+"'");
							}
							if(i==18)//GX
							{
								if(trd_abbr.contains("IGX"))
								{
									value="I";
								}
								else
								{
									value="K";
								}
							}
//							else
//							{
								cell.setCellValue("'" + value + "'");
//							}
						}
					 count++;
				 }
			 }

			 stmt1.close();
			 rset1.close();
			 
			}
			// link 0 
//			else if(rset.getString(1).equals("0"))
//			{
//				row = spreadsheet.createRow(nrow++);
//				
//				link=rset.getString(1);
//				customer_cd=rset.getString(2);
//				trd_abbr=rset.getString(4);
//				
//				 trd_abbr=trd_abbr.trim();
//				 value=trd_abbr;
//				 if (mpe.counterparty_map.containsKey(value)) {
//				        value =mpe.counterparty_map.get(value); 
//				        trd_abbr = value; 
//				    }
//
//				 // link is 0 so there is no mandate conform no 
//				 for (int i = 0; i < columns.split(",").length; i++) {
//					 
//					 value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
//					 cell = row.createCell(i);
//					 
//					if(i==0) {
//						
//						 value = trd_abbr+":"+link+"=PUR";
//						 cell.setCellValue("'"+value+"'");
//						
//					 }
////					if(i==1)
////					{
////						value="0=PUR";
////					}
//					 if(i==2)//SEQ_NO FROM FMS_DIRECT
//						{
//							value=rset.getString(3);
//							seq_no1=Integer.parseInt(value);
//						}
//					 if(i==3) {
//						 
//						 if(map_seq_no.containsKey(trd_abbr+seq_no1)) {
//							map_seq_no1 = (map_seq_no.get(trd_abbr+seq_no1)+1);
//							map_seq_no.put(trd_abbr+seq_no1, map_seq_no1);
//						}
//						else {
//							map_seq_no1=0;
//							map_seq_no.put(trd_abbr+seq_no1, map_seq_no1);
//						}
//						
//						 value=map_seq_no1+"";
//						cell.setCellValue("'"+value+"'");
//					}
//					if(i==4)//SEC_REF_NO _FROM FMS_DIRECT
//					{
//						value=rset.getString(5);
//					 cell.setCellValue("'"+rset.getString(5)+"'");
//					
//					}
//					if(i==16) {
//							
//					value="0";
//					 cell.setCellValue("'"+value+"'");
//					}
//					if(i==18)//GX
//					{
//						if(trd_abbr.contains("IGX"))
//						{
//							value="I";
//						}
//						else
//						{
//							value="K";
//						}
//					}
////					else
////					 {
//						 cell.setCellValue("'" + value + "'");
////					 }
//				 }	
//				 count++;
//			}
			}
//			else // for null
//			{
//			else if(rset.getString(1)==null 
//					&& (!rset.getString(4).contains("AM/NS-T")  || !rset.getString(4).contains("RIL-D6") || !rset.getString(4).contains("RIL-CBM"))) // for null
//			{
//				row = spreadsheet.createRow(nrow++);
//				
//				link=rset.getString(1);
//				customer_cd=rset.getString(2);
//				trd_abbr=rset.getString(4);
//				
//				 trd_abbr=trd_abbr.trim();
//				 value=trd_abbr;
//				 if (mpe.counterparty_map.containsKey(value)) {
//				        value =mpe.counterparty_map.get(value); 
//				        trd_abbr = value; 
//				    }
//
//				 
//				 for (int i = 0; i < columns.split(",").length; i++) {
//					 
//					 value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
//					 cell = row.createCell(i);
//					 
//						 if(i==0) {
//							 value = trd_abbr+":"+link+"=PUR";
//							 cell.setCellValue("'"+value+"'");
//						  }
////						 if(i==1)
////							{
////								value="null=PUR";
////							}
//						 if(i==2)//SEQ_NO FROM FMS_DIRECT
//							{
//								value=rset.getString(3);
//								seq_no1=Integer.parseInt(value);
//							}
//						 if(i==3) {
//							 
//							 if(map_seq_no.containsKey(trd_abbr+seq_no1)) {
//								map_seq_no1 = (map_seq_no.get(trd_abbr+seq_no1)+1);
//								map_seq_no.put(trd_abbr+seq_no1, map_seq_no1);
//							}
//							else {
//								map_seq_no1=0;
//								map_seq_no.put(trd_abbr+seq_no1, map_seq_no1);
//							}
//							
//							 value=map_seq_no1+"";
//							cell.setCellValue("'"+value+"'");
//						}
//						if(i==4)//SEC_REF_NO _FROM FMS_DIRECT
//						{
//							value=rset.getString(5);
//							cell.setCellValue("'"+rset.getString(5)+"'");
//
//						}
//						if(i==16) {
//								value="0";
//								cell.setCellValue("'"+value+"'");
//						}
//						if(i==18)//GX
//						{
//							if(trd_abbr.contains("IGX"))
//							{
//								value="I";
//							}
//							else
//							{
//								value="K";
//							}
//						}
////						else
////						{
//							 cell.setCellValue("'" + value + "'");
////					    }
//				 }	
//				 count++;
//			}
		}
		stmt.close();
		rset.close();
			
		//SALES :
		
		String seq_no_S="",cont_rev="",org_abbr="",agmt_rev="",counterparty_cd="";
		int map_seq_no_S=0;
		Map<String, Integer> map_seq_no_S1 = new HashMap<String, Integer>();
		
		queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
				+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
				+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
		stmt = conn.prepareStatement(queryString);
		rset = stmt.executeQuery();
		
		logger.checkpoint(fname, " COUNTERPARTY_CD, SEQ_NO, MAP_SEQ_NO, SEQ_REV_NO, GX ,  CONT_TYPE,TIMESTAMP", conn);
		
		while (rset.next()) {
			counterparty_cd = rset.getString(2);
			int num = 1; 
			//FOR SN
			queryString1 = "SELECT A.FGSA_NO, A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD FROM FMS7_SN_MST A, FMS7_CUSTOMER_MST B "
					+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
					+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
					+ "AND B.COUNTERPARTY_CD = ? "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, rset.getString(2));
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_FromDt);
			stmt1.setString(4, delta_ToDt);	
			stmt1.setString(5, delta_ToDt);
			rset1 = stmt1.executeQuery();
		
			while (rset1.next()) {
				
				queryString3 = "SELECT CUSTOMER_CD, SEQ_NO, NULL, REF_NO, NULL, NULL, NULL, NULL, 'S', ENT_CD, "
						+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, "
						+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL,CUSTOMER_ABBR  "
						+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
						+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, ("S"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)));
				stmt3.setString(2, rset1.getString(5));
				stmt3.setString(3, counterparty_cd);
				stmt3.setString(4, delta_FromDt);
				stmt3.setString(5, delta_FromDt);
				stmt3.setString(6, delta_ToDt);
				stmt3.setString(7, delta_ToDt);
				rset3 = stmt3.executeQuery();
				
				
				while (rset3.next() && !rset3.getString(20).contains("ESSARRM")) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					abbr = rset.getString(1) == null ? "null" : rset.getString(1);
					cell = row.createCell(ncell++);
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					cell.setCellValue("'"+abbr+"=SALES'");
				
					cell = row.createCell(ncell++);
					
//					cont_ref = rset3.getString(1);
					cd = rset.getString(2) == null ? "null" : rset.getString(2);
					cell.setCellValue("'"+cd+"'");
//					System.out.println(cd);
					
					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset3.getString(i) == null ? "null" : rset3.getString(i);
						
						if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//							if(abbr.equals(prev_abbr))
//							{
//								seq_no = num+"";
//							}
//							else {
//								seq_no="1";
//							}
							seq_no_S = value;
							cell.setCellValue("'"+seq_no_S+"'");									
						}
//						else if (i == 3) {	// map_seq_no
//							cell.setCellValue("'"+1+"'");									
//						}
						else if(i==3) {
							
							 if(map_seq_no_S1.containsKey(abbr+seq_no_S)) {
								 map_seq_no_S = (map_seq_no_S1.get(abbr+seq_no_S)+1);
								 map_seq_no_S1.put(abbr+seq_no_S, map_seq_no_S);
							}
							else {
								map_seq_no_S=0;
								map_seq_no_S1.put(abbr+seq_no_S, map_seq_no_S);
							}
							
							 value=map_seq_no_S+"";
							cell.setCellValue("'"+value+"'");
						}
//						else if (i == 4) {	// sec_ref_no
//							value = abbr + "-S-" + num ;
//							cell.setCellValue("'"+value+"'");
//						}
						else if (i == 5) {	// fgsa_no
							cell.setCellValue("'"+rset1.getString(1)+"'");
							num++;
						}
						else if (i == 6) {	// fgsa_no_rev
							cell.setCellValue("'"+rset1.getString(2)+"'");
						}
						else if (i == 7) {	// sn_no
							cell.setCellValue("'"+rset1.getString(3)+"'");
						}
						else if (i == 8) {	// sn_no_rev
							cell.setCellValue("'"+rset1.getString(4)+"'");
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
					}
					count++;
					logger.data(fname, (cd+","+seq_no_S+","+"1"+","+"K"+","+"S"+","), conn, "");
				}
				rset3.close();
				stmt3.close();
			}
			rset1.close();
			stmt1.close();


			//FOR LOA
			queryString1 = "SELECT A.TENDER_NO,A.LOA_NO , A.LOA_REV_NO, A.CUSTOMER_CD FROM FMS7_LOA_MST A, FMS7_CUSTOMER_MST B "
					+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
					+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
					+ "AND B.COUNTERPARTY_CD = ? "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, rset.getString(2));
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_FromDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_ToDt);
			rset1 = stmt1.executeQuery();
		
			while (rset1.next()) {
				
				queryString3 =  "SELECT CUSTOMER_CD, SEQ_NO, NULL, REF_NO, NULL, NULL, NULL, NULL, 'L', ENT_CD, "
						+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, "
						+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL,CUSTOMER_ABBR "
						+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
						+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, ("L"+rset1.getString(1)+"-0-"+rset1.getString(2)+"-"+rset1.getString(3)));
				stmt3.setString(2, rset1.getString(4));
				stmt3.setString(3, counterparty_cd);
				stmt3.setString(4, delta_FromDt);
				stmt3.setString(5, delta_FromDt);
				stmt3.setString(6, delta_ToDt);
				stmt3.setString(7, delta_ToDt);
				rset3 = stmt3.executeQuery();
				
				while (rset3.next() && !rset3.getString(20).contains("ESSARRM")) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					
					agmt_no = rset1.getString(1);
					agmt_rev = "0";
					cont_no = rset1.getString(2);
					cont_rev = rset1.getString(3);
					
					abbr = rset.getString(1) == null ? "null" : rset.getString(1);
					cell = row.createCell(ncell++);
					if (mpe.counterparty_map.containsKey(abbr)) {
						org_abbr = abbr;
						abbr =mpe.counterparty_map.get(abbr); 
				    }else {
				    	org_abbr = null;
				    }
					cell.setCellValue("'"+abbr+"=SALES'");
				
					cell = row.createCell(ncell++);
					
					if(org_abbr != null && org_abbr.equals("KSFL")) {
						cont_ref = "L-KSFL-"+agmt_no+"-"+cont_no+"-"+cont_rev;
						value = cont_ref;
						cell.setCellValue("'"+value+"'");
					}else {
						cont_ref = "L-"+agmt_no+"-"+cont_no+"-"+cont_rev;
						value = cont_ref;
						cell.setCellValue("'"+value+"'");
					}
					
					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset3.getString(i) == null ? "null" : rset3.getString(i);
						 if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
							 seq_no_S = value;
								cell.setCellValue("'"+seq_no_S+"'");									
						}
						else if (i == 3) {
							
							 if(map_seq_no_S1.containsKey(abbr+seq_no_S)) {
								 map_seq_no_S = (map_seq_no_S1.get(abbr+seq_no_S)+1);
								 map_seq_no_S1.put(abbr+seq_no_S, map_seq_no_S);
							}
							else {
								map_seq_no_S=0;
								map_seq_no_S1.put(abbr+seq_no_S, map_seq_no_S);
							}
							
							 value=map_seq_no_S+"";
							cell.setCellValue("'"+value+"'");
						}
//						else if (i == 4) {	// sec_ref_no
//							value = abbr + "-S-" + num ;
//							cell.setCellValue("'"+value+"'");
//							num++;
//						}
						else {
							cell.setCellValue("'"+value+"'");
						}
					}
					count++;
					logger.data(fname, (cd+","+seq_no_S+","+"1"+","+"K"+","+"L"+","), conn, "");
				}
				rset3.close();
				stmt3.close();
			}
			rset1.close();
			stmt1.close();
			
			
			
			//LTCORA
			queryString1 = "SELECT A.CUSTOMER_CD,A.AGREEMENT_NO, A.REV_NO , A.CN_NO , A.CN_REV_NO, A.CN_TERM FROM FMS8_LNG_REGAS_MST A, FMS7_CUSTOMER_MST B "
					+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
					+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
					+ "AND B.COUNTERPARTY_CD = ? "
					+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, rset.getString(2));
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_FromDt);
			stmt1.setString(4, delta_ToDt);
			stmt1.setString(5, delta_ToDt);
			rset1 = stmt1.executeQuery();
		
			while (rset1.next()) {
				
				queryString3 = "SELECT CUSTOMER_CD, SEQ_NO, NULL, REF_NO, NULL, NULL, NULL, NULL, NULL, ENT_CD, "
						+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, "
						+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL "
						+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
						+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, ("C"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)+"-"+rset1.getString(5)));
				stmt3.setString(2, rset1.getString(1));
				stmt3.setString(3, counterparty_cd);
				stmt3.setString(4, delta_FromDt);
				stmt3.setString(5, delta_FromDt);
				stmt3.setString(6, delta_ToDt);
				stmt3.setString(7, delta_ToDt);
				rset3 = stmt3.executeQuery();
				
				while (rset3.next()) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					cd = rset1.getString(1);
					agmt_no = rset1.getString(2);
					agmt_rev = rset1.getString(3);
					cont_no = rset1.getString(4);
					cont_rev = rset1.getString(5);
					cont_type = rset1.getString(6);
					
					abbr = rset.getString(1) == null ? "null" : rset.getString(1);
					cell = row.createCell(ncell++);
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
				    }
					cell.setCellValue("'"+abbr+"=SALES'");
				
					cell = row.createCell(ncell++);
					
					if(cont_type.equals("C")) { 
						cont_ref = "C"+"-"+cd+"-"+agmt_no+"-"+cont_no+"-"+cont_rev;
					}
					else if(cont_type.equals("A")) {
						cont_ref = "A"+"-"+cd+"-"+agmt_no+"-"+cont_no+"-"+cont_rev;
					}
					cell.setCellValue("'"+cont_ref+"'");	
					
					for (int i = 2; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset3.getString(i) == null ? "null" : rset3.getString(i);
						
						if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//							if(abbr.equals(prev_abbr))
//							{
//								seq_no = num+"";
//							}
//							else {
//								seq_no="1";
//							}
							seq_no_S = value;
							cell.setCellValue("'"+seq_no_S+"'");									
						}
						else if (i == 3) {
							
							 if(map_seq_no.containsKey(abbr+seq_no_S)) {
								 map_seq_no_S = (map_seq_no_S1.get(abbr+seq_no_S)+1);
								 map_seq_no_S1.put(abbr+seq_no_S, map_seq_no_S);
							}
							else {
								map_seq_no_S=0;
								map_seq_no.put(abbr+seq_no_S, map_seq_no_S);
							}
							
							 value=map_seq_no_S+"";
							cell.setCellValue("'"+value+"'");
						}
//						else if (i == 4) {	// sec_ref_no
//							value = rset.getString(1) + "-S-" + num ;
//							cell.setCellValue("'"+value+"'");
//						}
						else if (i == 5) {	// agmt_no
							cell.setCellValue("'"+agmt_no+"'");
							num++;
						}
						else if (i == 6) {	// agmt_rev
							cell.setCellValue("'"+agmt_rev+"'");
						}
						else if (i == 7) {	// cont_no
							cell.setCellValue("'"+cont_no+"'");
						}
						else if (i == 8) {	// cont_rev
							cell.setCellValue("'"+cont_rev+"'");
						}
						else if (i == 9) {	// cont_type
							if (value.equals("A")) {
								cont_type = "Q";
							}
							else if(value.equals("C")) {
								cont_type = "O";
							}
							cell.setCellValue("'"+cont_type+"'");
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
					}
					count++;
					logger.data(fname, (cd+","+seq_no_S+","+"1"+","+rset1.getString(5)+","+"K"+","+"L"+","), conn, "");
				}
				rset3.close();
				stmt3.close();
			}
			rset1.close();
			stmt1.close();
		}
		rset.close();
		stmt.close();
		
		
		
		//DLNG:
		
		String adv_seq="",no="",rev="",cont_no="",cont_ref="";
		cont_rev="";
		int seq_no=0;
		counterparty_cd="";
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, " COUNTERPARTY_CD, SEQ_NO, MAP_SEQ_NO, SEQ_REV_NO, GX ,  CONT_TYPE,TIMESTAMP", conn);
			
			while (rset.next()) {
				abbr = rset.getString(1) == null ? "null" : rset.getString(1);
				counterparty_cd = rset.getString(2) == null ? "null" : rset.getString(2);
				if(abbr!=null) {
					abbr= abbr.toUpperCase();
				}
				int num = 1; 
				//FOR SN WITHOUT ADV(CUSTOMER)
				queryString1 = "SELECT A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);	
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
			
				while (rset1.next()) {
					no = rset1.getString(1);
					rev = rset1.getString(2);
					cont_no = rset1.getString(3);
					cont_rev = rset1.getString(4);
					cd = rset1.getString(5);
					
					queryString3 = "SELECT CUSTOMER_CD, SEQ_NO, NULL, REF_NO, NULL, NULL, NULL, NULL, 'F', ENT_CD, "
							+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, "
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL "
							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
//							+ "AND SEC_TYPE NOT IN ('ADV') "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, ("S"+no+"-"+rev+"-"+cont_no+"-"+cont_rev+"-DLNG"));
					stmt3.setString(2, cd);
					stmt3.setString(3, counterparty_cd);
					stmt3.setString(4, delta_FromDt);
					stmt3.setString(5, delta_FromDt);
					stmt3.setString(6, delta_ToDt);
					stmt3.setString(7, delta_ToDt);
					rset3 = stmt3.executeQuery();
					
					
					while (rset3.next()) {
						
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						seq_no=rset3.getInt(2);
						
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell.setCellValue("'"+abbr+"=DLNG"+"'");
					
						cell = row.createCell(ncell++);
						cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						cell.setCellValue("'"+cont_ref+"'");

						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset3.getString(i) == null ? "null" : rset3.getString(i);
						
							 if (i == 3) {	// map_seq_no
								cell.setCellValue("'"+0+"'");									
							}
							else if (i == 5) {	// fgsa_no
								cell.setCellValue("'"+rset1.getString(1)+"'");
								num++;
							}
							else if (i == 6) {	// fgsa_no_rev
								cell.setCellValue("'"+rset1.getString(2)+"'");
							}
							else if (i == 7) {	// sn_no
								cell.setCellValue("'"+rset1.getString(3)+"'");
							}
							else if (i == 8) {	// sn_no_rev
								cell.setCellValue("'"+rset1.getString(4)+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"E"+","), conn, "");
					}
					rset3.close();
					stmt3.close();
				
				}
				rset1.close();
				stmt1.close();
				

				//FOR LOA(CUSTOMER)
				queryString1 = "SELECT A.TENDER_NO,A.LOA_NO , A.LOA_REV_NO, A.CUSTOMER_CD FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
			
				while (rset1.next()) {
					no = rset1.getString(1);
					rev = "0";
					cont_no = rset1.getString(2);
					cont_rev = rset1.getString(3);
					cd = rset1.getString(4);
					//FOR LOA WITHOUT ADV
					queryString3 =  "SELECT CUSTOMER_CD, SEQ_NO, NULL, REF_NO, NULL, NULL, NULL, NULL, 'E', ENT_CD, "
							+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, "
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL "
							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
//							+ "AND SEC_TYPE NOT IN ('ADV') "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, ("L"+no+"-0-"+cont_no+"-"+cont_rev+"-DLNG"));
					stmt3.setString(2, cd);
					stmt3.setString(3, counterparty_cd);
					stmt3.setString(4, delta_FromDt);
					stmt3.setString(5, delta_FromDt);
					stmt3.setString(6, delta_ToDt);
					stmt3.setString(7, delta_ToDt);
					rset3 = stmt3.executeQuery();
					
					while (rset3.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell.setCellValue("'"+abbr+"=DLNG"+"'");
					
						cell = row.createCell(ncell++);
						
						cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
						cell.setCellValue("'"+cont_ref+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset3.getString(i) == null ? "null" : rset3.getString(i);
							if (i == 3) {	// map_seq_no
								cell.setCellValue("'"+0+"'");									
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"F"+","), conn, "");
					}
					rset3.close();
					stmt3.close();

				}
				rset1.close();
				stmt1.close();
				
			}
			rset.close();
			stmt.close();
			//COMPLETE ----------DLNG---------
			
			//MERGE COUNTERPARTY PURCHASE  :
			// RIL=RIL-D6,RIL-CBM;
			// AMNS=AMNS-T,AM/NS-T TOTAL:22 
			int seq_no_merge=0;
			org_abbr="";
			
			Map<String, Integer> merge_seq_no = new HashMap<String, Integer>();
			queryString= "SELECT LINK,COUNTERPARTY_CD, SEQ_NO,CUSTOMER_ABBR,"
					+ "REF_NO, NULL, NULL, NULL, NULL, NULL, ENT_CD, TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
					+ " TO_CHAR(MODIFY_DT,'DD/MM/YYYY HH24:MI:SS'), APRV_BY, TO_CHAR(APRV_DT,'DD/MM/YYYY HH24:MI:SS'), "
					+ "NULL, NULL, 'K', NULL FROM FMS9_SECURITY_POST_MST "
					+ "WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
					+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_ABBR DESC";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
			while(rset.next())
			{	
				if(rset.getString(1)!=null  
						&& (rset.getString(4).contains("AM/NS-T")  || rset.getString(4).contains("RIL-D6") 
								|| rset.getString(4).contains("RIL-CBM") )  ) 
				{
					
				if(rset.getString(1).startsWith("B")) {
					
				link=rset.getString(1);

				
				customer_cd=rset.getString(2);
				trd_abbr=rset.getString(4);
			
				sec_ref_no=rset.getString(5);
				sn_no="";
				sn_rev="";
				man_cnf_cd=link.split("-")[0];
				man_cnf_cd=man_cnf_cd.substring(1);
				cargo_seq_no=link.split("-")[1];
				
						
				 queryString1="SELECT CARGO_REF_CD,NVL(DOM_BUY_FLAG,'N') FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CONF_CD= ? AND CARGO_SEQ_NO = ? ";
				 stmt1=conn.prepareStatement(queryString1);
				 stmt1.setString(1, man_cnf_cd);
				 stmt1.setString(2,cargo_seq_no );
				 rset1=stmt1.executeQuery();
				 while(rset1.next()) {
					 
					 cargo_ref_cd=rset1.getString(1);
					 dom_buy_flag=rset1.getString(2);
						
					
					 
					 queryString2="SELECT SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG= ?  "
					 		+ "ORDER BY CUSTOMER_CD,DOM_BUY_FLAG,SN_NO,SN_REV_NO DESC";
					 stmt2=conn.prepareStatement(queryString2);
					 stmt2.setString(1,"%"+cargo_ref_cd+"%");
					 stmt2.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);
					

					 rset2=stmt2.executeQuery();
					 if(rset2.next()) {
						 
						do {
							 sn_no=rset2.getString(1);
							 sn_rev=rset2.getString(2);
							 row = spreadsheet.createRow(nrow++);
								 trd_abbr=trd_abbr.trim();
								 value=trd_abbr;
								 org_abbr=trd_abbr;
								 if (mpe.counterparty_map.containsKey(value)) {
								        value =mpe.counterparty_map.get(value); 
								        trd_abbr = value; 
								    }
								 
								 if(trd_abbr.contains("RIL")) {
									 trd_abbr="RIL";
									 customer_cd="1";
								 }
								 
								 if(trd_abbr.contains("AMNS-T") || trd_abbr.contains("AM/NS-T") ) {
									 trd_abbr="AMNS";
									 customer_cd="33";
								 }
								 
								 if(trd_abbr.contains("ESSARRM")  ) {
									 trd_abbr="NEL";
									 customer_cd="209";
								 }
								 
//								 System.out.println(trd_abbr+":"+org_abbr);
								 
								 
								 for (int i = 0; i < columns.split(",").length; i++) {
										
										value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
										cell = row.createCell(i);
										
										if(i==0) {
											 if (dom_buy_flag == null) {
												 cont_type = "N";  // Handle null first
												}else if (dom_buy_flag.equals("Y")) {
													cont_type = "D";
												} else if (dom_buy_flag.equals("K")) {
													cont_type = "I";
												} else if (dom_buy_flag.equals("T")) {
//													dom_buy_flag="Y";
													cont_type = "T";
												}else if (dom_buy_flag.equals("N")) {
													cont_type = "N";
												}	 
											 
											 value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";
											 cell.setCellValue("'"+value+"'");
											
										}
										if(i==1) {
											man_cnf_cd=link.split("-")[0];
											
											man_cnf_cd=man_cnf_cd.substring(1,man_cnf_cd.length());
											value=man_cnf_cd;
											cell.setCellValue("'"+value+"'");
										}
										 if(i==2)//SEQ_NO FROM FMS_DIRECT
										{
											queryString4 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST  WHERE COUNTERPARTY_CD =?   ";
											stmt4 = conn.prepareStatement(queryString4);
											stmt4.setString(1,customer_cd);
											rset4 = stmt4.executeQuery();
											if(rset4.next()) {
												seq_no_merge=rset4.getInt(1)+1;
												value=seq_no_merge+"";
											}
											else {
												value="1";
											}
											rset4.close();
											stmt4.close();
											
										
											if(merge_seq_no.containsKey(trd_abbr)) {
												value = (merge_seq_no.get(trd_abbr)+1)+"";
												seq_no_merge=Integer.parseInt(value);
												merge_seq_no.put(trd_abbr, Integer.parseInt(value));
											}
											else {
												merge_seq_no.put(trd_abbr, Integer.parseInt(value));
											}
											
											cell.setCellValue("'"+value+"'");
												
										}
										 if(i==3) {
											
											value="0";
											cell.setCellValue("'"+value+"'");
										}
										 if(i==4)//SEC_REF_NO _FROM FMS_DIRECT
										{
											 cell.setCellValue("'"+org_abbr+"-S-"+seq_no_merge+"'");
														
											 value=org_abbr+"-S-"+seq_no_merge;
										
										}
										 if(i==16) {
											if(sec_ref_no.contains("-V")) {
											sec_ref_no=sec_ref_no.split("-V")[1];
											}
											value="0";
										}
										 if(i==18)//GX
										{
											if(trd_abbr.contains("IGX"))
											{
												value="I";
											}
											else
											{
												value="K";
											}
										}
//										else
//										{
											cell.setCellValue("'" + value + "'");
//										}
										
										
									}
								 count++;
						 } while(rset2.next());
						 stmt2.close();
						 rset2.close();
					 }
					 else
					 {
						 
							 sn_no="0";
							 sn_rev="0";
							 row = spreadsheet.createRow(nrow++);
								 trd_abbr=trd_abbr.trim();
								 value=trd_abbr;
								 org_abbr=trd_abbr;
								 if (mpe.counterparty_map.containsKey(value)) {
								        value =mpe.counterparty_map.get(value); 
								        trd_abbr = value; 
								    }
								 
								 if(trd_abbr.contains("RIL")) {
									 trd_abbr="RIL";
									 customer_cd="1";
								 }
								 if(trd_abbr.contains("AMNS-T") || trd_abbr.contains("AM/NS-T") ) {
									 trd_abbr="AMNS";
									 customer_cd="33";
									
								 }
								 
								 if(trd_abbr.contains("ESSARRM")  ) {
									 trd_abbr="NEL";
									 customer_cd="209";
								 }
//								 System.out.println(trd_abbr+":"+org_abbr);
								 for (int i = 0; i < columns.split(",").length; i++) {
										
										value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
										cell = row.createCell(i);
										
										
										if(i==0) {
											 if (dom_buy_flag == null) {
												 cont_type = "N";  // Handle null first
												}else if (dom_buy_flag.equals("Y")) {
													cont_type = "D";
												} else if (dom_buy_flag.equals("K")) {
													cont_type = "I";
												} else if (dom_buy_flag.equals("T")) {
//													dom_buy_flag="Y";
													cont_type = "T";
												}else if (dom_buy_flag.equals("N")) {
													cont_type = "N";
												}	 
											 
											 value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";
											 cell.setCellValue("'"+value+"'");
											
										}
										 if(i==1) {
											man_cnf_cd=link.split("-")[0];
											
											man_cnf_cd=man_cnf_cd.substring(1,man_cnf_cd.length());
											value=man_cnf_cd;
											cell.setCellValue("'"+value+"'");
										}
										 if(i==2)//SEQ_NO FROM FMS_DIRECT
										{
//											 value=rset.getString(3);
											 
											 	queryString4 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST  WHERE COUNTERPARTY_CD =?   ";
												stmt4 = conn.prepareStatement(queryString4);
												stmt4.setString(1,customer_cd);
												rset4 = stmt4.executeQuery();
												if(rset4.next()) {
													seq_no_merge=rset4.getInt(1)+1;
													value=seq_no_merge+"";
												}
												else {
													value="1";
												}
												rset4.close();
												stmt4.close();
												
											
												if(merge_seq_no.containsKey(trd_abbr)) {
													value = (merge_seq_no.get(trd_abbr)+1)+"";
													seq_no_merge=Integer.parseInt(value);
													merge_seq_no.put(trd_abbr, Integer.parseInt(value));
												}
												else {
													merge_seq_no.put(trd_abbr, Integer.parseInt(value));
												}
												
												cell.setCellValue("'"+value+"'");
												
										}
										 if(i==3) {
											
											value="0";
											 cell.setCellValue("'"+value+"'");
										}
										 if(i==4)//SEC_REF_NO _FROM FMS_DIRECT
											{
												 cell.setCellValue("'"+org_abbr+"-S-"+seq_no_merge+"'");
												
												 value=org_abbr+"-S-"+seq_no_merge;
											
											}
										 if(i==16) {
											if(sec_ref_no.contains("-V")) {
											sec_ref_no=sec_ref_no.split("-V")[1];
											}
											value="0";
										}
										 if(i==18)//GX
										{
											if(trd_abbr.contains("IGX"))
											{
												value="I";
											}
											else
											{
												value="K";
											}
										}
											cell.setCellValue("'" + value + "'");
										
									}
								 count++;
						 stmt2.close();
						 rset2.close();
						 
						 
					 }
					 
				 }

				 stmt1.close();
				 rset1.close();
				 
				}
				}

			}
			stmt.close();
			rset.close();
			
			//MERGE SALES :
			String sales_abbr="";
			map_seq_no_S=0;
			map_seq_no_S1 = new HashMap<String, Integer>();
			merge_seq_no = new HashMap<String, Integer>();
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, " COUNTERPARTY_CD, SEQ_NO, MAP_SEQ_NO, SEQ_REV_NO, GX ,  CONT_TYPE,TIMESTAMP", conn);
			
			while (rset.next()) {
				
				int num = 1; 
				//FOR SN
				queryString1 = "SELECT A.FGSA_NO, A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD FROM FMS7_SN_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);	
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
			
				while (rset1.next()) {
					
					queryString3 = "SELECT CUSTOMER_CD, SEQ_NO, NULL, REF_NO, NULL, NULL, NULL, NULL, 'S', ENT_CD, "
							+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY, "
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL,CUSTOMER_ABBR  "
							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND CUSTOMER_CD = ? "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, ("S"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)));
					stmt3.setString(2, rset1.getString(5));
					stmt3.setString(3, delta_FromDt);
					stmt3.setString(4, delta_FromDt);
					stmt3.setString(5, delta_ToDt);
					stmt3.setString(6, delta_ToDt);
					rset3 = stmt3.executeQuery();
					
					
					while (rset3.next() && rset3.getString(20).contains("ESSARRM")) {
						
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						cell = row.createCell(ncell++);
						
						sales_abbr=rset3.getString(20);
						org_abbr=abbr;
						
						cell.setCellValue("'"+abbr+"=SALES'");
					
						cell = row.createCell(ncell++);
						
						if(sales_abbr.contains("ESSARRM")  ) {
							abbr="NEL";
							customer_cd="209";
						 }
						
//						cont_ref = rset3.getString(1);
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
						cell.setCellValue("'"+cd+"'");
//						System.out.println(cd);
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset3.getString(i) == null ? "null" : rset3.getString(i);
							
							 if(i==2)//SEQ_NO FROM FMS_DIRECT
							 {
								 	queryString4 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST  WHERE COUNTERPARTY_CD =?   ";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1,customer_cd);
									rset4 = stmt4.executeQuery();
									if(rset4.next()) {
										seq_no_merge=rset4.getInt(1)+1;
										value=seq_no_merge+"";
									}
									else {
										value="1";
									}
									rset4.close();
									stmt4.close();
									
								
									if(merge_seq_no.containsKey(abbr)) {
										value = (merge_seq_no.get(abbr)+1)+"";
										seq_no_merge=Integer.parseInt(value);
										merge_seq_no.put(abbr, Integer.parseInt(value));
									}
									else {
										merge_seq_no.put(abbr, Integer.parseInt(value));
									}
									
									cell.setCellValue("'"+value+"'");
										
								}
							else if(i==3) {
								
								 if(map_seq_no_S1.containsKey(abbr+seq_no_S)) {
									 map_seq_no_S = (map_seq_no_S1.get(abbr+seq_no_S)+1);
									 map_seq_no_S1.put(abbr+seq_no_S, map_seq_no_S);
								}
								else {
									map_seq_no_S=0;
									map_seq_no_S1.put(abbr+seq_no_S, map_seq_no_S);
								}
								
								 value=map_seq_no_S+"";
								cell.setCellValue("'"+value+"'");
							}
							else if(i==4)
							{
								 cell.setCellValue("'"+org_abbr+"-S-"+seq_no_merge+"'");
								 value=org_abbr+"-S-"+seq_no_merge;
							}
							else if (i == 5) {	// fgsa_no
								cell.setCellValue("'"+rset1.getString(1)+"'");
								num++;
							}
							else if (i == 6) {	// fgsa_no_rev
								cell.setCellValue("'"+rset1.getString(2)+"'");
							}
							else if (i == 7) {	// sn_no
								cell.setCellValue("'"+rset1.getString(3)+"'");
							}
							else if (i == 8) {	// sn_no_rev
								cell.setCellValue("'"+rset1.getString(4)+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no_S+","+"1"+","+"K"+","+"S"+","), conn, "");
					}
					rset3.close();
					stmt3.close();
				}
				rset1.close();
				stmt1.close();

			}
			rset.close();
			stmt.close();
			
			
			
			
			
			
			// ADV_DPT_DLNG
			seq_no=1;
			Map<String, Integer> seq = new HashMap<String, Integer>();
			
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, " COUNTERPARTY_CD, SEQ_NO, MAP_SEQ_NO, SEQ_REV_NO, GX ,  CONT_TYPE,TIMESTAMP", conn);
			
			while (rset.next()) {
				abbr = rset.getString(1) == null ? "null" : rset.getString(1);
				counterparty_cd = rset.getString(2) == null ? "null" : rset.getString(2);
				if(abbr!=null) {
					abbr= abbr.toUpperCase();
				}
				int num = 1; 

					//FOR SN WITH ADV
					queryString1 = "SELECT A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B "
							+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
							+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
							+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, rset.getString(2));
					stmt1.setString(2, delta_FromDt);
					stmt1.setString(3, delta_FromDt);
					stmt1.setString(4, delta_ToDt);	
					stmt1.setString(5, delta_ToDt);
					rset1 = stmt1.executeQuery();
				
					while (rset1.next()) {
						no = rset1.getString(1);
						rev = rset1.getString(2);
						cont_no = rset1.getString(3);
						cont_rev = rset1.getString(4);
						cd = rset1.getString(5);
						
						queryString2 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST "
								+ "WHERE "
								+ "((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? )";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cd);
						stmt2.setString(2, counterparty_cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next())
						{
							seq_no = rset2.getInt(1);
						}
						else {
							seq_no=0;
						}
						rset2.close();
						stmt2.close();
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
						}
						
						if (!seq.containsKey(abbr)) {
							seq.put(abbr,seq_no);
						}
						
					queryString4 = "SELECT  NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'F', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, NULL, APPROVED_BY, TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL "
							+ " FROM DLNG_ADVC_PAY_MST "
							+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'S' ORDER BY SEQ_NO";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1,cd);
					stmt4.setString(2,no);
					stmt4.setString(3,rev);
					stmt4.setString(4,cont_no);
					stmt4.setString(5,cont_rev);
					
					rset4 = stmt4.executeQuery();
					while(rset4.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
//						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						cell = row.createCell(ncell++);
						
						cell.setCellValue("'"+abbr+"=DLNG"+"'");
						
						cell = row.createCell(ncell++);
						cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						cell.setCellValue("'"+cont_ref+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset4.getString(i) == null ? "null" : rset4.getString(i);
							
							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
								if (seq.containsKey(abbr)) {
									seq_no=seq.get(abbr);
									seq_no=seq_no+1;
									seq.put(abbr,seq_no);
								} 
								else {
									seq_no= 1;
									seq.put(abbr,seq_no);
								}
								
								value = seq_no+"";
								cell.setCellValue("'"+value+"'");									
							}
							else if (i == 3) {	// map_seq_no
								cell.setCellValue("'"+0+"'");									
							}
							else if (i == 4) {	// sec_ref_no
								value = abbr + "-S-" + seq_no ;
								cell.setCellValue("'"+value+"'");
							}
							else if (i == 5) {	// fgsa_no
								cell.setCellValue("'"+no+"'");
								num++;
							}
							else if (i == 6) {	// fgsa_no_rev
								cell.setCellValue("'"+rev+"'");
							}
							else if (i == 7) {	// sn_no
								cell.setCellValue("'"+cont_no+"'");
							}
							else if (i == 8) {	// sn_no_rev
								cell.setCellValue("'"+cont_rev+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"F"+","), conn, "");

				}
				rset4.close();
				stmt4.close();
				}
				rset1.close();
				stmt1.close();
				
				//FOR LOA WITH ADV
				queryString1 = "SELECT A.TENDER_NO,A.LOA_NO , A.LOA_REV_NO, A.CUSTOMER_CD FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
			
				while (rset1.next()) {
					no = rset1.getString(1);
					rev = "0";
					cont_no = rset1.getString(2);
					cont_rev = rset1.getString(3);
					cd = rset1.getString(4);
					
					queryString2 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST "
							+ "WHERE "
//							+ "LINK LIKE ? AND "
							+ "((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? )";
					stmt2 = conn.prepareStatement(queryString2);
//					stmt2.setString(1, "%-DLNG");
					stmt2.setString(1, cd);
					stmt2.setString(2, counterparty_cd);
					rset2 = stmt2.executeQuery();
					if(rset2.next())
					{
						seq_no = rset2.getInt(1);
					}
					else {
						seq_no=0;
					}
					rset2.close();
					stmt2.close();
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
					}
					if (!seq.containsKey(abbr)) {
						seq.put(abbr,seq_no);
					}
					
					queryString4 = "SELECT  NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'E', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "NULL, NULL, APPROVED_BY, TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), '0', NULL, 'K', NULL "
							+ " FROM DLNG_ADVC_PAY_MST "
							+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'L' ORDER BY SEQ_NO";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1,cd);
					stmt4.setString(2,no);
					stmt4.setString(3,rev);
					stmt4.setString(4,cont_no);
					stmt4.setString(5,cont_rev);
					
					rset4 = stmt4.executeQuery();
					while(rset4.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
//						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						cell = row.createCell(ncell++);
						cell.setCellValue("'"+abbr+"=DLNG"+"'");
						
						cell = row.createCell(ncell++);
						cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
						cell.setCellValue("'"+cont_ref+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset4.getString(i) == null ? "null" : rset4.getString(i);
							
							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
								if (seq.containsKey(abbr)) {
									seq_no=seq.get(abbr);
									seq_no=seq_no+1;
									seq.put(abbr,seq_no);
								} 
								else {
									seq_no= 1;
									seq.put(abbr,seq_no);
								}
								
								value = seq_no+"";
								cell.setCellValue("'"+value+"'");									
							}
							else if (i == 3) {	// map_seq_no
								cell.setCellValue("'"+0+"'");									
							}
							else if (i == 4) {	// sec_ref_no
								value = abbr + "-S-" + seq_no ;
//								value = abbr + "(" + adv_seq + ")";
								cell.setCellValue("'"+value+"'");
							}
							else if (i == 5) {	// fgsa_no
								cell.setCellValue("'"+no+"'");
								num++;
							}
							else if (i == 6) {	// fgsa_no_rev
								cell.setCellValue("'"+rev+"'");
							}
							else if (i == 7) {	// sn_no
								cell.setCellValue("'"+cont_no+"'");
							}
							else if (i == 8) {	// sn_no_rev
								cell.setCellValue("'"+cont_rev+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"E"+","), conn, "");

					}
					rset4.close();
					stmt4.close();
				}
				rset1.close();
				stmt1.close();
				
				
			}
			rset.close();
			stmt.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SECURITY_DEAL_MAP>>,", conn);
						
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_SECURITY_DEAL_MAP>>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
	}
	catch(Exception e){

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		
		//LOGGER.log(Level.WARNING,"Error in Purchase_SEIPL_Data_Extractor.java -> Purchase_Excel_Extractor -> FMS_TRADER_CONT_MST()  ", e);
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//		logger.error(fname, e, function_nm, conn, fname_error);

	}
}




public void FMS_SECURITY_MST() throws SQLException, IOException {
	
	function_nm = "FMS_SECURITY_MST()";
	String base="",link="",customer_cd="",sec_ref_no="",man_cnf_cd="";
	String cont_map_id="",cargo_ref_cd="",cont_type="",alloc_dt="",trd_abbr="",fgsa_no="",fgsa_rev_no="",sn_no="",sn_rev="",plant_seq_no="",bu_seq_no="",cargo_seq_no="",dom_buy_flag="";
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_SECURITY_MST>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		

    	columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,"
    			+ "VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,"
    			+ "EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,"
    			+ "APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,"
    			+ "PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD";			
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
		
		
		//PURCHASE:
		queryString="SELECT LINK,CUSTOMER_ABBR,SEQ_NO,REF_NO,NVL(ISSUED,'R'),SEC_TYPE,DEAL_TYPE,GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
				+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,"
				+ "TO_CHAR(REC_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(ISSU_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(CANCEL_DT,'DD/MM/YYYY HH24:MI:SS'),TENOR,TO_CHAR(REVIEW_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "STATUS,REMARKS,FLAG,INORDER_HIST,ENT_CD,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "MODIFY_BY,TO_CHAR(APRV_DT,'DD/MM/YYYY HH24:MI:SS'),APRV_BY,0,'K',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,RECEIVED "
				+ "FROM FMS9_SECURITY_POST_MST WHERE"
				+ " (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_ABBR DESC ";
		
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		

		logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
		
		while(rset.next())
		{	
			if(rset.getString(1)!=null && (rset.getString(2)!=null && !rset.getString(2).contains("AM/NS-T")  && !rset.getString(2).contains("RIL-D6") && !rset.getString(4).contains("RIL-CBM") ))
			{
				if(rset.getString(1).startsWith("B")) { 
				link=rset.getString(1);
				trd_abbr=rset.getString(2);
			
				man_cnf_cd=link.split("-")[0];
				man_cnf_cd=man_cnf_cd.substring(1,man_cnf_cd.length());
				cargo_seq_no=link.split("-")[1];
				
				 queryString1="SELECT CARGO_REF_CD,NVL(DOM_BUY_FLAG, 'N') FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CONF_CD= ? AND CARGO_SEQ_NO = ? ";
				 stmt1=conn.prepareStatement(queryString1);
				 stmt1.setString(1, man_cnf_cd);
				 stmt1.setString(2,cargo_seq_no );
				 rset1=stmt1.executeQuery();
				 
				 
				 while(rset1.next()) {
					 cargo_ref_cd=rset1.getString(1);
					 dom_buy_flag=rset1.getString(2);

					 queryString2="SELECT SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG= ?  "
						 		+ "ORDER BY CUSTOMER_CD,DOM_BUY_FLAG,SN_NO,SN_REV_NO DESC";
						 stmt2=conn.prepareStatement(queryString2);
						 stmt2.setString(1,"%"+cargo_ref_cd+"%");
						 stmt2.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);

						 rset2=stmt2.executeQuery();
						 
						 
						 if(rset2.next())
						 {
							 do {
								 sn_no=rset2.getString(1);
								 sn_rev=rset2.getString(2);

								 row = spreadsheet.createRow(nrow++);
								 
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									 }
									
									 for (int i = 0; i < columns.split(",").length; i++) {
											
											value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
											cell = row.createCell(i);
											
											if(i==0) {
												if (dom_buy_flag.equals("Y"))
												{
													cont_type = "D";
												}
												else if (dom_buy_flag.equals("K"))
												{
													cont_type = "I";
												}
												else if (dom_buy_flag.equals("T"))
												{
													cont_type = "T";
												}
												else if (dom_buy_flag.equals("N"))
												{
													cont_type = "N";
												}	 
												value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";
												cell.setCellValue("'"+value+"'");
											}
											else if(i==1) //ABBR
											{
												cell.setCellValue("'"+trd_abbr+"'");
											}
											else if(i==2)//seq_no
											{
												cell.setCellValue("'"+rset.getString(3)+"'");
											}
											else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
												
												 if (mpe.counterparty_map.containsKey(value)) {
												        value =mpe.counterparty_map.get(value);  
												 }
												 if (value.contains("RIL")) {
													 value = "RIL";
												 }
												 cell.setCellValue("'"+value+"'");
											}
											
											else if(i==8) {
												if(value.equals("INR")) {
													value="1";
												}
												else if(value.equals("USD")) {
													value="2";
												} 
												 cell.setCellValue("'"+value+"'");
											}
											else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
												queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
												stmt4 = conn.prepareStatement(queryString4);
												stmt4.setString(1, value);
												rset4 = stmt4.executeQuery();
												if(rset4.next()) {
													cell.setCellValue("'"+rset4.getString(1)+"'");
												}
												else {
													cell.setCellValue("'"+value+"'");
												}
												rset4.close();
												stmt4.close();
											}
											else if(i == 25 && !value.equals("null")) {	// Status
													if(value.equals("Pending")) {
														value = "P";
													}
													else if(value.equals("In order")) {
														value = "O";
													}
													else if(value.equals("Cancelled")) {
														value = "C";
													}
													else if(value.equals("Pending for amendment")) {
														value = "A";
													}
													else if(value.equals("Restated")) {
														value = "R";
													}
													else if(value.equals("Dummy")) {
														value = "D";
													}
													else if(value.equals("Expired")) {
														value = "E";
													}
													cell.setCellValue("'"+value+"'");
											}
											else if(i==36)//GX
											{
												if(trd_abbr.contains("IGX"))
												{
													value="I";
												}
												else
												{
													value="K";
												}
												cell.setCellValue("'" + value + "'");
													
											}
											else {
										cell.setCellValue("'" + value + "'");
										}
											
										}
										count++;
							 }while(rset2.next());
							
							 stmt2.close();
							 rset2.close();
							
							 
						 }
						 else if(!dom_buy_flag.equals("N"))
						 {
							 
							 row = spreadsheet.createRow(nrow++);
							 
							 trd_abbr=trd_abbr.trim();
							 value=trd_abbr;
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value); 
							        trd_abbr = value; 
							    }
							
							 
							 for (int i = 0; i < columns.split(",").length; i++) {
									
									value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
									cell = row.createCell(i);
									
									if(i==0) {
										
										 if (dom_buy_flag == null) {
											 cont_type = "N";  // Handle null first
											}else if (dom_buy_flag.equals("Y")) {
												cont_type = "D";
											} else if (dom_buy_flag.equals("K")) {
												cont_type = "I";
											} else if (dom_buy_flag.equals("T")) {
//													dom_buy_flag="Y";
												cont_type = "T";
											}else if (dom_buy_flag.equals("N")) {
												cont_type = "N";
											}	 
										 value = trd_abbr+"-"+0+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+0+"=PUR";
										 cell.setCellValue("'"+value+"'");
										}
									else if(i==1)
									{
										cell.setCellValue("'"+trd_abbr+"'");
									}
									else if(i==2)//seq_no
									{
										cell.setCellValue("'"+rset.getString(3)+"'");
									}
									else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
										 if (mpe.counterparty_map.containsKey(value)) {
										        value =mpe.counterparty_map.get(value);  
										 }
										 if (value.contains("RIL")) {
											 value = "RIL";
										 }
										 cell.setCellValue("'"+value+"'");
									}
									
									else if(i==8) {
										if(value.equals("INR")) {
											value="1";
										}
										else if(value.equals("USD")) {
											value="2";
										} 
										 cell.setCellValue("'"+value+"'");
									}
									else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
										queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1, value);
										rset4 = stmt4.executeQuery();
										if(rset4.next()) {
											cell.setCellValue("'"+rset4.getString(1)+"'");
										}
										else {
											cell.setCellValue("'"+value+"'");
										}
										rset4.close();
										stmt4.close();
									}
									else if(i == 25 && !value.equals("null")) {	// Status
												if(value.equals("Pending")) {
													value = "P";
												}
												else if(value.equals("In order")) {
													value = "O";
												}
												else if(value.equals("Cancelled")) {
													value = "C";
												}
												else if(value.equals("Pending for amendment")) {
													value = "A";
												}
												else if(value.equals("Restated")) {
													value = "R";
												}
												else if(value.equals("Dummy")) {
													value = "D";
												}
												else if(value.equals("Expired")) {
													value = "E";
												}
												cell.setCellValue("'"+value+"'");
									}
									else if(i==36)//GX
									{
										if(trd_abbr.contains("IGX"))
										{
											value="I";
										}
										else
										{
											value="K";
										}
										cell.setCellValue("'" + value + "'");
											
									}
									else {
								cell.setCellValue("'" + value + "'");}
									
								}
								count++;
						 }
						
						 stmt2.close();
						 rset2.close();
//						 for lng 
						 if(dom_buy_flag.equals("N") )
						 {
							 row = spreadsheet.createRow(nrow++);
							 link=link.substring(1,link.length());
							 link=link.split("-")[0];
							 
							 trd_abbr=trd_abbr.trim();
							 value=trd_abbr;
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value); 
							        trd_abbr = value; 
							    }
							 
							 
							 for (int i = 0; i < columns.split(",").length; i++) {
									
									value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
									cell = row.createCell(i);
									
									if(i==0) {
										
											 if (dom_buy_flag == null) {
												 cont_type = "N";  // Handle null first
												}else if (dom_buy_flag.equals("Y")) {
													cont_type = "D";
												} else if (dom_buy_flag.equals("K")) {
													cont_type = "I";
												} else if (dom_buy_flag.equals("T")) {
													dom_buy_flag="Y";
													cont_type = "T";
												}else if (dom_buy_flag.equals("N")) {
													cont_type = "N";
												}	 
											 value = trd_abbr+"-"+sn_no+"-"+link+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";// link is used as mandate_conform
											 cell.setCellValue("'"+value+"'");
											
										}
									else if(i==1)
									{
										cell.setCellValue("'"+trd_abbr+"'");
									}
									else if(i==2)//seq_no
									{
										cell.setCellValue("'"+rset.getString(3)+"'");
									}
									else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
										
										 if (mpe.counterparty_map.containsKey(value)) {
										        value =mpe.counterparty_map.get(value);  
										 }
										 if (value.contains("RIL")) {
											 value = "RIL";
										 }
										 cell.setCellValue("'"+value+"'");
									}
									
									else if(i==8) {
										if(value.equals("INR")) {
											value="1";
										}
										else if(value.equals("USD")) {
											value="2";
										} 
										 cell.setCellValue("'"+value+"'");
									}
									else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
										queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1, value);
										rset4 = stmt4.executeQuery();
										if(rset4.next()) {
											cell.setCellValue("'"+rset4.getString(1)+"'");
										}
										else {
											cell.setCellValue("'"+value+"'");
										}
										rset4.close();
										stmt4.close();
									}
									else if(i == 25 && !value.equals("null")) {	// Status
												if(value.equals("Pending")) {
													value = "P";
												}
												else if(value.equals("In order")) {
													value = "O";
												}
												else if(value.equals("Cancelled")) {
													value = "C";
												}
												else if(value.equals("Pending for amendment")) {
													value = "A";
												}
												else if(value.equals("Restated")) {
													value = "R";
												}
												else if(value.equals("Dummy")) {
													value = "D";
												}
												else if(value.equals("Expired")) {
													value = "E";
												}
												cell.setCellValue("'"+value+"'");
									}
									else if(i==36)//GX
									{
										if(trd_abbr.contains("IGX"))
										{
											value="I";
										}
										else
										{
											value="K";
										}
										cell.setCellValue("'" + value + "'");
											
									}
									else {
								cell.setCellValue("'" + value + "'");
									}
								}
								count++;
						 }
					 }
					 stmt1.close();
					 rset1.close();
				
				logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
			}
			else if(rset.getString(1).equals("0"))
			{
					link=rset.getString(1);
					
					trd_abbr=rset.getString(2);

					 row = spreadsheet.createRow(nrow++);
					 
						 trd_abbr=trd_abbr.trim();
						 value=trd_abbr;
						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        trd_abbr = value; 
						    }
						
						 for (int i = 0; i < columns.split(",").length; i++) {
								
								value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
								cell = row.createCell(i);
								
								if(i==0) {
									
									 value = trd_abbr+":"+0+":"+"NA=PUR";
									 cell.setCellValue("'"+value+"'");
								}
								else if(i==1)//COUNTERPARTY_ABBR
								{
									cell.setCellValue("'"+trd_abbr+"'");
								}
								else if(i==2)//seq_no
								{
									cell.setCellValue("'"+rset.getString(3)+"'");
								}
								else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
									
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value);  
									 }
									 if (value.contains("RIL")) {
										 value = "RIL";
									 }
									 cell.setCellValue("'"+value+"'");
								}	
								else if(i==8) {
									if(value.equals("INR")) {
										value="1";
									}
									else if(value.equals("USD")) {
										value="2";
									} 
									 cell.setCellValue("'"+value+"'");
								}
								else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
									queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, value);
									rset4 = stmt4.executeQuery();
									if(rset4.next()) {
										cell.setCellValue("'"+rset4.getString(1)+"'");
									}
									else {
										cell.setCellValue("'"+value+"'");
									}
									rset4.close();
									stmt4.close();
								}
								else if(i == 25 && !value.equals("null")) {	// Status
											if(value.equals("Pending")) {
												value = "P";
											}
											else if(value.equals("In order")) {
												value = "O";
											}
											else if(value.equals("Cancelled")) {
												value = "C";
											}
											else if(value.equals("Pending for amendment")) {
												value = "A";
											}
											else if(value.equals("Restated")) {
												value = "R";
											}
											else if(value.equals("Dummy")) {
												value = "D";
											}
											else if(value.equals("Expired")) {
												value = "E";
											}
											cell.setCellValue("'"+value+"'");
								}
								else if(i==36)//GX
								{
									if(trd_abbr.contains("IGX"))
									{
										value="I";
									}
									else
									{
										value="K";
									}
									cell.setCellValue("'" + value + "'");
										
								}
							else {
							cell.setCellValue("'" + value + "'");
							}
							
						 }count++;
				}
            }
			else if(rset.getString(1)==null && (!rset.getString(4).contains("AM/NS-T")  || !rset.getString(4).contains("RIL-D6") || !rset.getString(4).contains("RIL-CBM")) )
			{

				link=rset.getString(1);
				
				trd_abbr=rset.getString(2);

				 row = spreadsheet.createRow(nrow++);
				 
					 trd_abbr=trd_abbr.trim();
					 value=trd_abbr;
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        trd_abbr = value; 
					    }
					
					 
					 for (int i = 0; i < columns.split(",").length; i++) {
							
							value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
							cell = row.createCell(i);
							
							if(i==0) {
								
								 value = trd_abbr+":"+0+":"+"NA=PUR";
								 cell.setCellValue("'"+value+"'");
							}
							else if(i==1)// ABBR
							{
								cell.setCellValue("'"+trd_abbr+"'");
							}
							else if(i==2)//seq_no
							{
								cell.setCellValue("'"+rset.getString(3)+"'");
							}
							else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
								
								 if (mpe.counterparty_map.containsKey(value)) {
								        value =mpe.counterparty_map.get(value);  
								 }
								 if (value.contains("RIL")) {
									 value = "RIL";
								 }
								 cell.setCellValue("'"+value+"'");
							}	
							else if(i==8) {
								if(value.equals("INR")) {
									value="1";
								}
								else if(value.equals("USD")) {
									value="2";
								} 
								 cell.setCellValue("'"+value+"'");
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
								queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, value);
								rset4 = stmt4.executeQuery();
								if(rset4.next()) {
									cell.setCellValue("'"+rset4.getString(1)+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								rset4.close();
								stmt4.close();
							}
							else if(i == 25 && !value.equals("null")) {	// Status
										if(value.equals("Pending")) {
											value = "P";
										}
										else if(value.equals("In order")) {
											value = "O";
										}
										else if(value.equals("Cancelled")) {
											value = "C";
										}
										else if(value.equals("Pending for amendment")) {
											value = "A";
										}
										else if(value.equals("Restated")) {
											value = "R";
										}
										else if(value.equals("Dummy")) {
											value = "D";
										}
										else if(value.equals("Expired")) {
											value = "E";
										}
										cell.setCellValue("'"+value+"'");
							}
							else if(i==36)//GX
							{
								if(trd_abbr.contains("IGX"))
								{
									value="I";
								}
								else
								{
									value="K";
								}
								cell.setCellValue("'" + value + "'");
									
							}
						else {
						cell.setCellValue("'" + value + "'");
						}
						
					 }count++;
			    }
            }
			stmt.close();
			rset.close();
			
			//SALES :----------------------------------------------------------------------------------------------------------------------
			
			String seq_no="",map="",cont_rev="",agmt_rev="",counterparty_cd="";
			
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, " COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX ,CONT_TYPE,TIMESTAMP", conn);
			while (rset.next() ) {
				counterparty_cd = rset.getString(2);
				//FOR SN
				int num = 1; 
				queryString1 = "SELECT A.FGSA_NO, A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD "
						+ "FROM FMS7_SN_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next() ) {
					map = "S-"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4);
					
					queryString2 = "SELECT CUSTOMER_CD,SEQ_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,CUSTOMER_ABBR "
							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("S"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)));
					stmt2.setString(2, rset1.getString(5));
					stmt2.setString(3, counterparty_cd);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next() && !rset2.getString(47).contains("ESSARRM")) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell.setCellValue("'"+abbr+"=SALES'");
						
						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
						cell.setCellValue("'"+map+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
						
//							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
//								cell.setCellValue("'"+seq_no+"'");									
//							}
//							else if (i == 3) {	// sec_ref_no
//								value = rset.getString(1) + "-S-" + num ;
//								cell.setCellValue("'"+value+"'");
//								num++;
//							}
							 if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
								cell.setCellValue("'"+value+"'");
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, value);
								rset4 = stmt4.executeQuery();
								if(rset4.next()) {
									cell.setCellValue("'"+rset4.getString(1)+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								rset4.close();
								stmt4.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"S"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				
				
				rset1.close();
				stmt1.close();
					
//				//FOR LOA
				queryString1 = "SELECT A.TENDER_NO,A.LOA_NO,A.LOA_REV_NO,A.CUSTOMER_CD "
						+ "FROM FMS7_LOA_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {	
					map = "L-"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3);
					queryString2 = "SELECT NULL,SEQ_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ " TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD,"
							+ " TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,NULL,CUSTOMER_ABBR  "
							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("L"+rset1.getString(1)+"-"+"0"+"-"+rset1.getString(2)+"-"+rset1.getString(3)));//L1-0-1-0
					stmt2.setString(2, rset1.getString(4));
					stmt2.setString(3, counterparty_cd);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next() &&  !rset2.getString(48).contains("ESSARRM")) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell.setCellValue("'"+abbr+"=SALES'");
						
						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
						cell.setCellValue("'"+map+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
						
//							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
//								cell.setCellValue("'"+seq_no+"'");									
//							}
//							else if (i == 3) {	// sec_ref_no
//								value = rset.getString(1) + "-S-" + num ;
//								cell.setCellValue("'"+value+"'");
//								num++;
//							}
							 if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
								cell.setCellValue("'"+value+"'");
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, value);
								rset4 = stmt4.executeQuery();
								if(rset4.next()) {
									cell.setCellValue("'"+rset4.getString(1)+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								rset4.close();
								stmt4.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"L"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				
				
				//FOR LTCORA
				queryString1 = "SELECT A.CUSTOMER_CD,A.AGREEMENT_NO,A.REV_NO,A.CN_NO,A.CN_REV_NO,A.CN_TERM "
						+ "FROM FMS8_LNG_REGAS_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {	
//					System.out.println(rset1.getString(1));
					queryString2 = "SELECT NULL,SEQ_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,NULL "
							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, "C"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)+"-"+rset1.getString(5));//C1-1-0-1-0
					stmt2.setString(2, rset1.getString(1));
					stmt2.setString(3, counterparty_cd);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					while(rset2.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						customer_cd = rset1.getString(1);
						agmt_no = rset1.getString(2);
						agmt_rev = rset1.getString(3);
						cont_no = rset1.getString(4);
						cont_rev = rset1.getString(5);
						cont_type = rset1.getString(6);
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell.setCellValue("'"+abbr+"=SALES'");
						
						cell = row.createCell(ncell++);
						
						if(cont_type.equals("C")) { 
							cont_ref = "C"+"-"+customer_cd+"-"+agmt_no+"-"+cont_no+"-"+cont_rev;
						}
						else if(cont_type.equals("A")) {
							cont_ref = "A"+"-"+customer_cd+"-"+agmt_no+"-"+cont_no+"-"+cont_rev;
						}
						cell.setCellValue("'"+cont_ref+"'");		
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
						
//							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
//								cell.setCellValue("'"+seq_no+"'");									
//							}
//							else if (i == 3) {	// sec_ref_no
//								value = rset.getString(1) + "-S-" + num ;
//								cell.setCellValue("'"+value+"'");
//								num++;
//							}
							 if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
								cell.setCellValue("'"+value+"'");
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, value);
								rset4 = stmt4.executeQuery();
								if(rset4.next()) {
									cell.setCellValue("'"+rset4.getString(1)+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								rset4.close();
								stmt4.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"L"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
					
			}
			rset.close();
			stmt.close();
			
			
			
			
			
			//DLNG :
			String no="",remark="",rev="";
			cont_rev="";
			counterparty_cd="";
			map="";
			seq_no="";
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, " COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX ,CONT_TYPE,TIMESTAMP", conn);
			while (rset.next()) {
				abbr = rset.getString(1) == null ? "null" : rset.getString(1);
				counterparty_cd = rset.getString(2) == null ? "null" : rset.getString(2);
				if(abbr!=null) {
					abbr= abbr.toUpperCase();
				}
				remark = "";
				
				//FOR SN
				int num = 1; 
				queryString1 = "SELECT A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					no = rset1.getString(1);
					rev = rset1.getString(2);
					cont_no = rset1.getString(3);
					cont_rev = rset1.getString(4);
					cd = rset1.getString(5);
					map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
					
					//SN WITHOUT ADV
					queryString2 = "SELECT SEQ_NO,REF_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL "
							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
//							+ "AND SEC_TYPE NOT IN ('ADV')"
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("S"+no+"-"+rev+"-"+cont_no+"-"+cont_rev+"-DLNG"));
					stmt2.setString(2, rset1.getString(5));
					stmt2.setString(3, counterparty_cd);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
//						System.out.println(remark);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell.setCellValue("'"+abbr+"=DLNG'");
						
						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
						cell.setCellValue("'"+rset2.getString(1)+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							
							if(i==2)
							{
								cell.setCellValue("'"+rset2.getString(1)+"'");
							}
							else if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
								cell.setCellValue("'"+value+"'");
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									cell.setCellValue("'"+rset3.getString(1)+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								rset3.close();
								stmt3.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"S"+","), conn, "");
					}
					rset2.close();
					stmt2.close();

				}
				
				
				rset1.close();
				stmt1.close();
					
//				//FOR LOA
				queryString1 = "SELECT A.TENDER_NO,A.LOA_NO,A.LOA_REV_NO,A.CUSTOMER_CD "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {	
					no = rset1.getString(1);
					rev = "0";
					cont_no = rset1.getString(2);
					cont_rev = rset1.getString(3);
					cd = rset1.getString(4);
					map = "L-"+no+"-"+cont_no+"-"+cont_rev;
					
					//LOA WITHOUT ADV
					queryString2 = "SELECT SEQ_NO,REF_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,NULL "
							+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
							+ "AND SEC_TYPE NOT IN ('ADV') "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("L"+no+"-"+"0"+"-"+cont_no+"-"+cont_rev+"-DLNG"));//L1-0-1-0
					stmt2.setString(2, rset1.getString(4));
					stmt2.setString(3, counterparty_cd);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
						cell.setCellValue("'"+abbr+"=DLNG'");
						
						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
						cell.setCellValue("'"+rset2.getString(1)+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i);
						
							if(i==2)
							{
								cell.setCellValue("'"+rset2.getString(1)+"'");
							}
							else if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
								cell.setCellValue("'"+value+"'");
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									cell.setCellValue("'"+rset3.getString(1)+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
								rset3.close();
								stmt3.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
								cell.setCellValue("'"+value+"'");
							}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"L"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
					

				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			
			
			//MERGE COUNTERPARTY :PURCHASE 
			int seq_no_merge=0;
			String org_abbr="";

			Map<String, Integer> merge_seq_no = new HashMap<String, Integer>();
			queryString="SELECT LINK,CUSTOMER_ABBR,SEQ_NO,REF_NO,NVL(ISSUED,'R'),SEC_TYPE,DEAL_TYPE,GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
					+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,"
					+ "TO_CHAR(REC_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(ISSU_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(CANCEL_DT,'DD/MM/YYYY HH24:MI:SS'),TENOR,TO_CHAR(REVIEW_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "STATUS,REMARKS,FLAG,INORDER_HIST,ENT_CD,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "MODIFY_BY,TO_CHAR(APRV_DT,'DD/MM/YYYY HH24:MI:SS'),APRV_BY,0,'K',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL "
					+ "FROM FMS9_SECURITY_POST_MST WHERE"
					+ " (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_ABBR DESC ";
			
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			

			logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
			
			while(rset.next())
			{	
				if(rset.getString(1)!=null 
						&& (rset.getString(2).contains("AM/NS-T")  || rset.getString(2).contains("RIL-D6") || 
								rset.getString(4).contains("RIL-CBM")  ))
				{
					if(rset.getString(1).startsWith("B")) { 
					link=rset.getString(1);
					trd_abbr=rset.getString(2);
				
					man_cnf_cd=link.split("-")[0];
					man_cnf_cd=man_cnf_cd.substring(1,man_cnf_cd.length());
					cargo_seq_no=link.split("-")[1];
					
					 queryString1="SELECT CARGO_REF_CD,NVL(DOM_BUY_FLAG, 'N') FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CONF_CD= ? AND CARGO_SEQ_NO = ? ";
					 stmt1=conn.prepareStatement(queryString1);
					 stmt1.setString(1, man_cnf_cd);
					 stmt1.setString(2,cargo_seq_no );
					 rset1=stmt1.executeQuery();
					 
					 
					 while(rset1.next()) {
						 cargo_ref_cd=rset1.getString(1);
						 dom_buy_flag=rset1.getString(2);

						 queryString2="SELECT SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG= ?  "
							 		+ "ORDER BY CUSTOMER_CD,DOM_BUY_FLAG,SN_NO,SN_REV_NO DESC";
							 stmt2=conn.prepareStatement(queryString2);
							 stmt2.setString(1,"%"+cargo_ref_cd+"%");
							 stmt2.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);

							 rset2=stmt2.executeQuery();
							 
							 
							 if(rset2.next())
							 {
								 do {
									 sn_no=rset2.getString(1);
									 sn_rev=rset2.getString(2);

									 row = spreadsheet.createRow(nrow++);
									 
										 trd_abbr=trd_abbr.trim();
										 value=trd_abbr;
										 org_abbr=trd_abbr;
										 
										 if (mpe.counterparty_map.containsKey(value)) {
										        value =mpe.counterparty_map.get(value); 
										        trd_abbr = value; 
										 }
										 
										 if(trd_abbr.contains("RIL")) {
											 trd_abbr="RIL";
											 customer_cd="1";
											 
										 }
										 if(trd_abbr.contains("AMNS-T") || trd_abbr.contains("AM/NS-T") ) {
											 trd_abbr="AMNS";
											 customer_cd="33";
										 }
										 
										 if(trd_abbr.contains("ESSARRM")  ) {
											 trd_abbr="NEL";
											 customer_cd="209";
										 }
										 
										 for (int i = 0; i < columns.split(",").length; i++) {
												
												value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
												cell = row.createCell(i);
												
												if(i==0) {
													if (dom_buy_flag.equals("Y")) {
														cont_type = "D";
													} else if (dom_buy_flag.equals("K")) {
														cont_type = "I";
													} else if (dom_buy_flag.equals("T")) {
														cont_type = "T";
													}else if (dom_buy_flag.equals("N")) {
														cont_type = "N";
													}	 
													 value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";
													 cell.setCellValue("'"+value+"'");
														
												}
												else if(i==1) {//
													cell.setCellValue("'"+trd_abbr+"'");
												}
												else if(i==2)//seq_no
												{
													queryString4 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST  WHERE COUNTERPARTY_CD =?   ";
													stmt4 = conn.prepareStatement(queryString4);
													stmt4.setString(1,customer_cd);
													rset4 = stmt4.executeQuery();
													if(rset4.next()) {
														seq_no_merge=rset4.getInt(1)+1;
														value=seq_no_merge+"";
													}
													else {
														value="1";
													}
													rset4.close();
													stmt4.close();
													
												
													if(merge_seq_no.containsKey(trd_abbr)) {
														value = (merge_seq_no.get(trd_abbr)+1)+"";
														seq_no_merge=Integer.parseInt(value);
														merge_seq_no.put(trd_abbr, Integer.parseInt(value));
													}
													else {
														merge_seq_no.put(trd_abbr, Integer.parseInt(value));
													}
													
													cell.setCellValue("'"+value+"'");
												}
												else if(i==3)//SEC_REF_NO
												{
													cell.setCellValue("'"+org_abbr+"-S-"+seq_no_merge+"'");
//													System.out.println("org=="+org_abbr+"-S-"+seq_no_merge+":trd_abbr=="+trd_abbr);										
												}
												
												else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
													
													 if (mpe.counterparty_map.containsKey(value)) {
													        value =mpe.counterparty_map.get(value);  
													 }
													 if (value.contains("RIL")) {
														 value = "RIL";
													 }
													 cell.setCellValue("'"+value+"'");
												}
												else if(i==8) {
													if(value.equals("INR")) {
														value="1";
													}
													else if(value.equals("USD")) {
														value="2";
													} 
													 cell.setCellValue("'"+value+"'");
												}
												else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
													queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
													stmt4 = conn.prepareStatement(queryString4);
													stmt4.setString(1, value);
													rset4 = stmt4.executeQuery();
													if(rset4.next()) {
														cell.setCellValue("'"+rset4.getString(1)+"'");
													}
													else {
														cell.setCellValue("'"+value+"'");
													}
													rset4.close();
													stmt4.close();
												}
												else if(i == 25 && !value.equals("null")) {	// Status
															if(value.equals("Pending")) {
																value = "P";
															}
															else if(value.equals("In order")) {
																value = "O";
															}
															else if(value.equals("Cancelled")) {
																value = "C";
															}
															else if(value.equals("Pending for amendment")) {
																value = "A";
															}
															else if(value.equals("Restated")) {
																value = "R";
															}
															else if(value.equals("Dummy")) {
																value = "D";
															}
															else if(value.equals("Expired")) {
																value = "E";
															}
															cell.setCellValue("'"+value+"'");
												}
												else if(i==36)//GX
												{
													if(trd_abbr.contains("IGX"))
													{
														value="I";
													}
													else
													{
														value="K";
													}
													cell.setCellValue("'" + value + "'");
														
												}
												else {
											cell.setCellValue("'" + value + "'");}
												
											}
											count++;
								 }while(rset2.next());
								
								 stmt2.close();
								 rset2.close();
								
								 
							 }
							 else
							 {
								 sn_no="0";
								 sn_rev="0";

								 row = spreadsheet.createRow(nrow++);
								 
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 org_abbr=trd_abbr;
									 
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									 }
									 
									 if(trd_abbr.contains("RIL")) {
										 trd_abbr="RIL";
										 customer_cd="1";
										 
									 }
									 
									 if(trd_abbr.contains("AMNS-T") || trd_abbr.contains("AM/NS-T") ) {
										 trd_abbr="AMNS";
										 customer_cd="33";
									 }
									 
									 if(trd_abbr.contains("ESSARRM")) {
										 trd_abbr="NEL";
										 customer_cd="209";
									 }
									 
									 for (int i = 0; i < columns.split(",").length; i++) {
											
											value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
											cell = row.createCell(i);
											
											if(i==0) {
												if (dom_buy_flag.equals("Y")) {
													cont_type = "D";
												} else if (dom_buy_flag.equals("K")) {
													cont_type = "I";
												} else if (dom_buy_flag.equals("T")) {
													cont_type = "T";
												}else if (dom_buy_flag.equals("N")) {
													cont_type = "N";
												}	 
												 value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";
												 cell.setCellValue("'"+value+"'");
													
											}
											else if(i==1) {//
												cell.setCellValue("'"+trd_abbr+"'");
											}
											else if(i==2)//seq_no
											{
												queryString4 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST  WHERE COUNTERPARTY_CD =?   ";
												stmt4 = conn.prepareStatement(queryString4);
												stmt4.setString(1,customer_cd);
												rset4 = stmt4.executeQuery();
												if(rset4.next()) {
													seq_no_merge=rset4.getInt(1)+1;
													value=seq_no_merge+"";
												}
												else {
													value="1";
												}
												rset4.close();
												stmt4.close();
												
											
												if(merge_seq_no.containsKey(trd_abbr)) {
													value = (merge_seq_no.get(trd_abbr)+1)+"";
													seq_no_merge=Integer.parseInt(value);
													merge_seq_no.put(trd_abbr, Integer.parseInt(value));
												}
												else {
													merge_seq_no.put(trd_abbr, Integer.parseInt(value));
												}
												
												cell.setCellValue("'"+value+"'");
											}
											else if(i==3)//SEC_REF_NO
											{
												cell.setCellValue("'"+org_abbr+"-S-"+seq_no_merge+"'");
//												System.out.println("org=="+org_abbr+"-S-"+seq_no_merge+":trd_abbr=="+trd_abbr);										
											}
											
											else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
												
												 if (mpe.counterparty_map.containsKey(value)) {
												        value =mpe.counterparty_map.get(value);  
												 }
												 if (value.contains("RIL")) {
													 value = "RIL";
												 }
												 cell.setCellValue("'"+value+"'");
											}
											else if(i==8) {
												if(value.equals("INR")) {
													value="1";
												}
												else if(value.equals("USD")) {
													value="2";
												} 
												 cell.setCellValue("'"+value+"'");
											}
											else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
												queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
												stmt4 = conn.prepareStatement(queryString4);
												stmt4.setString(1, value);
												rset4 = stmt4.executeQuery();
												if(rset4.next()) {
													cell.setCellValue("'"+rset4.getString(1)+"'");
												}
												else {
													cell.setCellValue("'"+value+"'");
												}
												rset4.close();
												stmt4.close();
											}
											else if(i == 25 && !value.equals("null")) {	// Status
														if(value.equals("Pending")) {
															value = "P";
														}
														else if(value.equals("In order")) {
															value = "O";
														}
														else if(value.equals("Cancelled")) {
															value = "C";
														}
														else if(value.equals("Pending for amendment")) {
															value = "A";
														}
														else if(value.equals("Restated")) {
															value = "R";
														}
														else if(value.equals("Dummy")) {
															value = "D";
														}
														else if(value.equals("Expired")) {
															value = "E";
														}
														cell.setCellValue("'"+value+"'");
											}
											else if(i==36)//GX
											{
												if(trd_abbr.contains("IGX"))
												{
													value="I";
												}
												else
												{
													value="K";
												}
												cell.setCellValue("'" + value + "'");
													
											}
											else {
										cell.setCellValue("'" + value + "'");}
											
										}
										count++;
								 
							 }
							
							 stmt2.close();
							 rset2.close();
						 }
						 stmt1.close();
						 rset1.close();
					
					logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
				  }
	             }
	            }
				stmt.close();
				rset.close();
			
				
				//MERGE SECURITY SALES 
				
				String sales_abbr="";
				seq_no_merge=0;
				queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
						+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
						+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				
				logger.checkpoint(fname, " COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX ,CONT_TYPE,TIMESTAMP", conn);
				while (rset.next() ) {
					
					//FOR SN
					int num = 1; 
					queryString1 = "SELECT A.FGSA_NO, A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD "
							+ "FROM FMS7_SN_MST A, FMS7_CUSTOMER_MST B "
							+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
							+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
							+ "AND B.COUNTERPARTY_CD = ? "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, rset.getString(2));
					stmt1.setString(2, delta_FromDt);
					stmt1.setString(3, delta_FromDt);
					stmt1.setString(4, delta_ToDt);
					stmt1.setString(5, delta_ToDt);
					rset1 = stmt1.executeQuery();

					while (rset1.next() ) {
						map = "S-"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4);
						
						queryString2 = "SELECT CUSTOMER_CD,SEQ_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
								+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
								+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
								+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,CUSTOMER_ABBR "
								+ "FROM FMS9_SECURITY_POST_MST WHERE LINK = ? AND CUSTOMER_CD = ? "
								+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
								+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, ("S"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)));
						stmt2.setString(2, rset1.getString(5));
						stmt2.setString(3, delta_FromDt);
						stmt2.setString(4, delta_FromDt);
						stmt2.setString(5, delta_ToDt);
						stmt2.setString(6, delta_ToDt);
						rset2 = stmt2.executeQuery();
						
						while(rset2.next() && rset2.getString(47).contains("ESSARRM")) {
							row = spreadsheet.createRow(nrow++);
							ncell = 0;
							
							abbr = rset.getString(1) == null ? "null" : rset.getString(1);
							cell = row.createCell(ncell++);
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
						    }
							sales_abbr=rset2.getString(47);
							cell.setCellValue("'"+abbr+"=SALES'");
							
							cell = row.createCell(ncell++);
							
							cd = rset.getString(2) == null ? "null" : rset.getString(2);
							cell.setCellValue("'"+map+"'");
							
							if(sales_abbr.contains("ESSARRM")  ) {
								abbr="NEL";
								customer_cd="209";
							 }
							
							for (int i = 2; i < columns.split(",").length; i++) {
								cell = row.createCell(ncell++);
								value = rset2.getString(i) == null ? "null" : rset2.getString(i);
							

								if(i==2)//SEQ_NO FROM FMS_DIRECT
								 {
									 	queryString4 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST  WHERE COUNTERPARTY_CD =?   ";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1,customer_cd);
										rset4 = stmt4.executeQuery();
										if(rset4.next()) {
											seq_no_merge=rset4.getInt(1)+1;
											value=seq_no_merge+"";
										}
										else {
											value="1";
										}
										rset4.close();
										stmt4.close();
										
									
										if(merge_seq_no.containsKey(abbr)) {
											value = (merge_seq_no.get(abbr)+1)+"";
											seq_no_merge=Integer.parseInt(value);
											merge_seq_no.put(abbr, Integer.parseInt(value));
										}
										else {
											merge_seq_no.put(abbr, Integer.parseInt(value));
										}
										
										cell.setCellValue("'"+value+"'");
											
									}
								else if(i==3)
								{
									cell.setCellValue("'"+abbr+"-S-"+seq_no_merge+"'");
								}
								
								else  if (i == 7) {	// GUARANTOR_CD
									if (mpe.counterparty_map.containsKey(value)) {
										value =mpe.counterparty_map.get(value); 
								    }
									if(value.contains("RIL"))
									{
										value="RIL";
									}
									cell.setCellValue("'"+value+"'");
								}
								else if(i == 8 && !value.equals("null")) {	// Currency
									
									value = value.equals("INR") ? "1" : "2";
									cell.setCellValue("'"+value+"'");
								}
								else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
								
									queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, value);
									rset4 = stmt4.executeQuery();
									if(rset4.next()) {
										cell.setCellValue("'"+rset4.getString(1)+"'");
									}
									else {
										cell.setCellValue("'"+value+"'");
									}
									rset4.close();
									stmt4.close();
								}
								else if (i == 25 && !value.equals("null")) {	// Status
									if(value.equals("Pending")) {
										value = "P";
									}
									else if(value.equals("In order")) {
										value = "O";
									}
									else if(value.equals("Cancelled")) {
										value = "C";
									}
									else if(value.equals("Pending for amendment")) {
										value = "A";
									}
									else if(value.equals("Restated")) {
										value = "R";
									}
									else if(value.equals("Dummy")) {
										value = "D";
									}
									else if(value.equals("Expired")) {
										value = "E";
									}
									cell.setCellValue("'"+value+"'");
								}
								else {
									cell.setCellValue("'"+value+"'");
								}
							}
							count++;
							logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"S"+","), conn, "");
						}
						rset2.close();
						stmt2.close();
					}
					
					
					rset1.close();
					stmt1.close();
				
					
					
						
				}
				rset.close();
				stmt.close();
				
				
				
			
			
			//DLNG:ADV DPT :
				int seq_no_D=1;
				Map<String, Integer> seq = new HashMap<String, Integer>();
				queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
						+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
						+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				
				logger.checkpoint(fname, " COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX ,CONT_TYPE,TIMESTAMP", conn);
				while (rset.next()) {
					abbr = rset.getString(1) == null ? "null" : rset.getString(1);
					counterparty_cd = rset.getString(2) == null ? "null" : rset.getString(2);
					if(abbr!=null) {
						abbr= abbr.toUpperCase();
					}
					remark = "";
				
					
					//FOR SN WITH ADV
					String dr_cr="",appr_flag="",pay_type="";
					queryString1 = "SELECT A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B "
							+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
							+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
							+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, rset.getString(2));
					stmt1.setString(2, delta_FromDt);
					stmt1.setString(3, delta_FromDt);
					stmt1.setString(4, delta_ToDt);	
					stmt1.setString(5, delta_ToDt);
					rset1 = stmt1.executeQuery();
				
					while (rset1.next()) {
						no = rset1.getString(1);
						rev = rset1.getString(2);
						cont_no = rset1.getString(3);
						cont_rev = rset1.getString(4);
						cd = rset1.getString(5);
						
						queryString2 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST "
								+ "WHERE "
//								+ "LINK LIKE ? AND "
								+ "((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? )";
						stmt2 = conn.prepareStatement(queryString2);
//						stmt2.setString(1, "%-DLNG");
						stmt2.setString(1, cd);
						stmt2.setString(2, counterparty_cd);
						rset2 = stmt2.executeQuery();
						if(rset2.next())
						{
							seq_no_D = rset2.getInt(1);
						}
						else {
							seq_no_D=0;
						}
						rset2.close();
						stmt2.close();
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
						}
						
						if (!seq.containsKey(abbr)) {
							seq.put(abbr,seq_no_D);
						}
						
					queryString4 = "SELECT  NULL, NULL, NULL, NULL, PAY_TYPE, 'GAS', NULL, CURRENCY, NULL, PAY_AMT, NULL, NULL, NULL, NULL, "
							+ "NULL, NULL, NULL, TO_CHAR(PAY_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, NULL, NULL, NULL, APPROVED_FLAG, "
							+ "REMARK, NULL, 'Y', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
							+ "TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_BY, '0', 'K', NULL, NULL, NULL, NULL, NULL, NULL, SEQ_NO, DR_CR_FLAG, "
							+ "'0', NULL, NULL, NULL, NULL, NULL, NULL "
							+ " FROM DLNG_ADVC_PAY_MST "
							+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'S' ORDER BY SEQ_NO";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1,cd);
					stmt4.setString(2,no);
					stmt4.setString(3,rev);
					stmt4.setString(4,cont_no);
					stmt4.setString(5,cont_rev);
					
					rset4 = stmt4.executeQuery();
					while(rset4.next()) {
						pay_type = rset4.getString(5);
						appr_flag = rset4.getString(25);
						dr_cr = rset4.getString(44);
						
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
//						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						cell = row.createCell(ncell++);
						
						cell.setCellValue("'"+abbr+"=DLNG'");
						
						cell = row.createCell(ncell++);
						cont_ref = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
						cell.setCellValue("'"+cont_ref+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset4.getString(i) == null ? "null" : rset4.getString(i);
							
							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
								if (seq.containsKey(abbr)) {
									seq_no_D=seq.get(abbr);
									seq_no_D=seq_no_D+1;
									seq.put(abbr,seq_no_D);
								} 
								else {
									seq_no_D= 1;
									seq.put(abbr,seq_no_D);
								}
								
								value = seq_no_D+"";
								cell.setCellValue("'"+value+"'");									
							}
							else if (i == 3) {	// sec_ref_no
								value = abbr + "-S-" + seq_no_D ;
//								value = abbr + "(" + adv_seq + ")";
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 5) {	//SEC_TYPE
							if(pay_type.equals("AP") || pay_type.equals("LP")) {
								value = "ADV";
							}
							else {
								value = "DPT";
							}
							cell.setCellValue("'"+value+"'");
						}
							else if(i == 25) {	//STATUS
								if(appr_flag!=null && appr_flag.equals("Y")) {
									value = "O";
								}
								else {
									value = "A";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 44) {
							if(dr_cr!=null) {
								if(dr_cr.equals("C")) {
									dr_cr = "CR";
								}
								else if(dr_cr.equals("D")) {
									dr_cr = "DR";
								}
							}
							else {
								dr_cr = "CR";
							}
							value = dr_cr;
							cell.setCellValue("'"+value+"'");
						}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"E"+","), conn, "");

				}
				rset4.close();
				stmt4.close();
				}
				rset1.close();
				stmt1.close();
				
				//FOR LOA WITH ADV
				queryString1 = "SELECT A.TENDER_NO,A.LOA_NO , A.LOA_REV_NO, A.CUSTOMER_CD FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? AND A.START_DT IS NOT NULL AND A.END_DT IS NOT NULL "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();
			
				while (rset1.next()) {
					no = rset1.getString(1);
					rev = "0";
					cont_no = rset1.getString(2);
					cont_rev = rset1.getString(3);
					cd = rset1.getString(4);
					
					queryString2 = "SELECT MAX(SEQ_NO) FROM FMS9_SECURITY_POST_MST "
							+ "WHERE "
//							+ "LINK LIKE ? AND "
							+ "((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? )";
					stmt2 = conn.prepareStatement(queryString2);
//					stmt2.setString(1, "%-DLNG");
					stmt2.setString(1, cd);
					stmt2.setString(2, counterparty_cd);
					rset2 = stmt2.executeQuery();
					if(rset2.next())
					{
						seq_no_D = rset2.getInt(1);
					}
					else {
						seq_no_D=0;
					}
					rset2.close();
					stmt2.close();
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
					}
					if (!seq.containsKey(abbr)) {
						seq.put(abbr,seq_no_D);
					}
					
					queryString4 = "SELECT  NULL, NULL, NULL, NULL, PAY_TYPE, 'GAS', NULL, CURRENCY, NULL, PAY_AMT, NULL, NULL, NULL, NULL, "
							+ "NULL, NULL, NULL, TO_CHAR(PAY_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, NULL, NULL, NULL, APPROVED_FLAG, "
							+ "REMARK, NULL, 'Y', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
							+ "TO_CHAR(APPROVED_DT, 'DD/MM/YYYY HH24:MI:SS'), APPROVED_BY, '0', 'K', NULL, NULL, NULL, NULL, NULL, NULL, SEQ_NO, DR_CR_FLAG, "
							+ "'0', NULL, NULL, NULL, NULL, NULL, NULL "
							+ " FROM DLNG_ADVC_PAY_MST "
							+ "WHERE CUSTOMER_CD = ? AND FLSA_NO = ? AND FLSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? AND CONTRACT_TYPE = 'L' ORDER BY SEQ_NO";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1,cd);
					stmt4.setString(2,no);
					stmt4.setString(3,rev);
					stmt4.setString(4,cont_no);
					stmt4.setString(5,cont_rev);
					
					rset4 = stmt4.executeQuery();
					while(rset4.next()) {
						pay_type = rset4.getString(5);
						appr_flag = rset4.getString(25);
						dr_cr = rset4.getString(44);
					
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						
//						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						cell = row.createCell(ncell++);
						cell.setCellValue("'"+abbr+"=DLNG'");
						
						cell = row.createCell(ncell++);
						cont_ref = "L-"+no+"-"+cont_no+"-"+cont_rev;
						cell.setCellValue("'"+cont_ref+"'");
						
						for (int i = 2; i < columns.split(",").length; i++) {
							cell = row.createCell(ncell++);
							value = rset4.getString(i) == null ? "null" : rset4.getString(i);
							
							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
								if (seq.containsKey(abbr)) {
									seq_no_D=seq.get(abbr);
									seq_no_D=seq_no_D+1;
									seq.put(abbr,seq_no_D);
								} 
								else {
									seq_no_D= 1;
									seq.put(abbr,seq_no_D);
								}
								
								value = seq_no_D+"";
								cell.setCellValue("'"+value+"'");									
							}
							else if (i == 3) {	// sec_ref_no
								value = abbr + "-S-" + seq_no_D ;
//								value = abbr + "(" + adv_seq + ")";
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 5) {	//SEC_TYPE
							if(pay_type.equals("AP") || pay_type.equals("LP")) {
								value = "ADV";
							}
							else {
								value = "DPT";
							}
							cell.setCellValue("'"+value+"'");
						   }
							else if(i == 25) {	//STATUS
								if(appr_flag!=null && appr_flag.equals("Y")) {
									value = "O";
								}
								else {
									value = "A";
								}
								cell.setCellValue("'"+value+"'");
							}
							else if(i == 44) {
							if(dr_cr!=null) {
								if(dr_cr.equals("C")) {
									dr_cr = "CR";
								}
								else if(dr_cr.equals("D")) {
									dr_cr = "DR";
								}
							}
							else {
								dr_cr = "CR";
							}
							value = dr_cr;
							cell.setCellValue("'"+value+"'");
						}
							else {
								cell.setCellValue("'"+value+"'");
							}
						}
						count++;
						logger.data(fname, (cd+","+seq_no+","+"1"+","+"K"+","+"E"+","), conn, "");
//					}
//					rset3.close();
//					stmt3.close();
					}
					rset4.close();
					stmt4.close();
				}
				rset1.close();
				stmt1.close();
						

				}
				rset.close();
				stmt.close();
			

			filename = migration_setup_dir + "EXPORT/FMS_SECURITY_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SECURITY_MST>>,", conn);
						
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_SECURITY_MST>>");
			System.out.println();

			msg = "Data has been Extracted Successfully.";
			msg_type = "S";
			
	}
	catch(Exception e){

		msg = "One of the Functions faced an Error. Extraction Terminated.";
		msg_type = "E";
		
		
		
		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//		logger.error(fname, e, function_nm, conn, fname_error);

	}
}	

public void LOG_FMS_SECURITY_MST() throws SQLException, IOException {
	
	function_nm = "LOG_FMS_SECURITY_MST()";
	String base="",link="",customer_cd="",sec_ref_no="",man_cnf_cd="";
	String cont_map_id="",cargo_ref_cd="",cont_type="",alloc_dt="",trd_abbr="",fgsa_no="",fgsa_rev_no="",sn_no="",sn_rev="",plant_seq_no="",bu_seq_no="",cargo_seq_no="",dom_buy_flag="";
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<LOG_FMS_SECURITY_MST>>,", conn);
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		

    	columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,"
    			+ "VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,"
    			+ "EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,"
    			+ "APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,"
    			+ "PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD,LOG_SEQ_NO,LOG_ENT_DT";			
//		workbook = new XSSFWorkbook();
//		spreadsheet = workbook.createSheet("Sheet 1");
//		
//		nrow = 0;
		count = 0;
		String cd="";
		
		String fname_csv = "", str = "";
		fname_csv = migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST_" + start_end_dt + ".csv";
		
		FileWriter fw = new FileWriter(fname_csv, false); 
		fw.close();

//		Below block of code is for inserting columns
//		row = spreadsheet.createRow(nrow++);

//		for (int i = 0; i < columns.split(",").length; i++) {
//			cell = row.createCell(i);
//			cell.setCellValue(columns.split(",")[i]);
//		}
		for (int i = 0; i < columns.split(",").length; i++) {
			str += columns.split(",")[i] + ",";
		}
		logger.insert_data(fname_csv, str, conn);
		
		//PURCHASE:
		queryString="SELECT LINK,CUSTOMER_ABBR,SEQ_NO,REF_NO,NVL(ISSUED,'R'),SEC_TYPE,DEAL_TYPE,GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
				+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,"
				+ "TO_CHAR(REC_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(ISSU_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(CANCEL_DT,'DD/MM/YYYY HH24:MI:SS'),TENOR,TO_CHAR(REVIEW_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "STATUS,REMARKS,FLAG,INORDER_HIST,ENT_CD,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "MODIFY_BY,TO_CHAR(APRV_DT,'DD/MM/YYYY HH24:MI:SS'),APRV_BY,0,'K',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,DTL_SEQ_NO,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS')  "
				+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE"
				+ " (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_ABBR DESC ";
		
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1, delta_FromDt);
		stmt.setString(2, delta_FromDt);
		stmt.setString(3, delta_ToDt);
		stmt.setString(4, delta_ToDt);
		rset = stmt.executeQuery();
		

		logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
		
		while(rset.next())
		{	
			if(rset.getString(1)!=null && (rset.getString(2)!=null && !rset.getString(2).contains("AM/NS-T")  && !rset.getString(2).contains("RIL-D6") && !rset.getString(4).contains("RIL-CBM") ))
			{
				if(rset.getString(1).startsWith("B")) { 
				link=rset.getString(1);
				trd_abbr=rset.getString(2);
			
				man_cnf_cd=link.split("-")[0];
				man_cnf_cd=man_cnf_cd.substring(1,man_cnf_cd.length());
				cargo_seq_no=link.split("-")[1];
				
				 queryString1="SELECT CARGO_REF_CD,NVL(DOM_BUY_FLAG, 'N') FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CONF_CD= ? AND CARGO_SEQ_NO = ? ";
				 stmt1=conn.prepareStatement(queryString1);
				 stmt1.setString(1, man_cnf_cd);
				 stmt1.setString(2,cargo_seq_no );
				 rset1=stmt1.executeQuery();
				 
				 
				 while(rset1.next()) {
					 cargo_ref_cd=rset1.getString(1);
					 dom_buy_flag=rset1.getString(2);

					 queryString2="SELECT SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG= ?  "
						 		+ "ORDER BY CUSTOMER_CD,DOM_BUY_FLAG,SN_NO,SN_REV_NO DESC";
						 stmt2=conn.prepareStatement(queryString2);
						 stmt2.setString(1,"%"+cargo_ref_cd+"%");
						 stmt2.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);

						 rset2=stmt2.executeQuery();
						 
						 
						 if(rset2.next())
						 {
							 do {
								 str = "";
								 sn_no=rset2.getString(1);
								 sn_rev=rset2.getString(2);

//								 row = spreadsheet.createRow(nrow++);
								 
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									 }
									
									 for (int i = 0; i < columns.split(",").length; i++) {
											
//											value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1);
//											cell = row.createCell(i);
										 	value = rset.getString(i+1) == null ? "null" : rset.getString(i+1).replaceAll(",", " ");
											
											if(i==0) {
												if (dom_buy_flag.equals("Y"))
												{
													cont_type = "D";
												}
												else if (dom_buy_flag.equals("K"))
												{
													cont_type = "I";
												}
												else if (dom_buy_flag.equals("T"))
												{
													cont_type = "T";
												}
												else if (dom_buy_flag.equals("N"))
												{
													cont_type = "N";
												}	 
												value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";
//												cell.setCellValue("'"+value+"'");
												str += value + ",";
											}
											else if(i==1) //ABBR
											{
//												cell.setCellValue("'"+trd_abbr+"'");
												str += trd_abbr + ",";
											}
											else if(i==2)//seq_no
											{
//												cell.setCellValue("'"+rset.getString(3)+"'");
												str += rset.getString(3) + ",";
											}
											else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
												
												 if (mpe.counterparty_map.containsKey(value)) {
												        value =mpe.counterparty_map.get(value);  
												 }
												 if (value.contains("RIL")) {
													 value = "RIL";
												 }
//												 cell.setCellValue("'"+value+"'");
												 str += value + ",";
											}
											
											else if(i==8) {
												if(value.equals("INR")) {
													value="1";
												}
												else if(value.equals("USD")) {
													value="2";
												} 
//												 cell.setCellValue("'"+value+"'");
												 str += value + ",";
											}
											else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
												queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
												stmt4 = conn.prepareStatement(queryString4);
												stmt4.setString(1, value);
												rset4 = stmt4.executeQuery();
												if(rset4.next()) {
//													cell.setCellValue("'"+rset4.getString(1)+"'");
													str += rset4.getString(1).replaceAll(",", "@@") + ",";
												}
												else {
//													cell.setCellValue("'"+value+"'");
													str += value + ",";
												}
												rset4.close();
												stmt4.close();
											}
											else if(i == 25 && !value.equals("null")) {	// Status
													if(value.equals("Pending")) {
														value = "P";
													}
													else if(value.equals("In order")) {
														value = "O";
													}
													else if(value.equals("Cancelled")) {
														value = "C";
													}
													else if(value.equals("Pending for amendment")) {
														value = "A";
													}
													else if(value.equals("Restated")) {
														value = "R";
													}
													else if(value.equals("Dummy")) {
														value = "D";
													}
													else if(value.equals("Expired")) {
														value = "E";
													}
//													cell.setCellValue("'"+value+"'");
													str += value + ",";
											}
											else if(i==36)//GX
											{
												if(trd_abbr.contains("IGX"))
												{
													value="I";
												}
												else
												{
													value="K";
												}
//												cell.setCellValue("'" + value + "'");
												str += value + ",";	
											}
											else {
//												cell.setCellValue("'" + value + "'");
												str += value.replaceAll(",", " ") + ",";
										}
											
										}
										count++;
										logger.insert_data(fname_csv, str, conn);
										logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
							 }while(rset2.next());
							
							 stmt2.close();
							 rset2.close();
							
							 
						 }
						 else if(!dom_buy_flag.equals("N"))
						 {
							 str = "";
//							 row = spreadsheet.createRow(nrow++);
							 
							 trd_abbr=trd_abbr.trim();
							 value=trd_abbr;
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value); 
							        trd_abbr = value; 
							    }
							
							 
							 for (int i = 0; i < columns.split(",").length; i++) {
									
									value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replaceAll(",", " ");
//									cell = row.createCell(i);
									
									if(i==0) {
										
										 if (dom_buy_flag == null) {
											 cont_type = "N";  // Handle null first
											}else if (dom_buy_flag.equals("Y")) {
												cont_type = "D";
											} else if (dom_buy_flag.equals("K")) {
												cont_type = "I";
											} else if (dom_buy_flag.equals("T")) {
//													dom_buy_flag="Y";
												cont_type = "T";
											}else if (dom_buy_flag.equals("N")) {
												cont_type = "N";
											}	 
										 value = trd_abbr+"-"+0+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+0+"=PUR";
//										 cell.setCellValue("'"+value+"'");
										 str += value + ",";
										}
									else if(i==1)
									{
//										cell.setCellValue("'"+trd_abbr+"'");
										str += trd_abbr + ",";
									}
									else if(i==2)//seq_no
									{
//										cell.setCellValue("'"+rset.getString(3)+"'");
										str += rset.getString(3) + ",";
									}
									else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
										 if (mpe.counterparty_map.containsKey(value)) {
										        value =mpe.counterparty_map.get(value);  
										 }
										 if (value.contains("RIL")) {
											 value = "RIL";
										 }
//										 cell.setCellValue("'"+value+"'");
										 str += value + ",";
									}
									
									else if(i==8) {
										if(value.equals("INR")) {
											value="1";
										}
										else if(value.equals("USD")) {
											value="2";
										} 
//										 cell.setCellValue("'"+value+"'");
										str += value + ",";
									}
									else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
										queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1, value);
										rset4 = stmt4.executeQuery();
										if(rset4.next()) {
//											cell.setCellValue("'"+rset4.getString(1)+"'");
											str += rset4.getString(1).replaceAll(",", "@@") + ",";
										}
										else {
//											cell.setCellValue("'"+value+"'");
											str += value + ",";
										}
										rset4.close();
										stmt4.close();
									}
									else if(i == 25 && !value.equals("null")) {	// Status
												if(value.equals("Pending")) {
													value = "P";
												}
												else if(value.equals("In order")) {
													value = "O";
												}
												else if(value.equals("Cancelled")) {
													value = "C";
												}
												else if(value.equals("Pending for amendment")) {
													value = "A";
												}
												else if(value.equals("Restated")) {
													value = "R";
												}
												else if(value.equals("Dummy")) {
													value = "D";
												}
												else if(value.equals("Expired")) {
													value = "E";
												}
//												cell.setCellValue("'"+value+"'");
												str += value + ",";
									}
									else if(i==36)//GX
									{
										if(trd_abbr.contains("IGX"))
										{
											value="I";
										}
										else
										{
											value="K";
										}
//										cell.setCellValue("'" + value + "'");
										str += value + ",";
									}
									else {
//										cell.setCellValue("'" + value + "'");
										str += value.replaceAll(",", " ") + ",";}
									
								}
								count++;
								logger.insert_data(fname_csv, str, conn);
								logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
						 }
						
						 stmt2.close();
						 rset2.close();
//						 for lng 
						 if(dom_buy_flag.equals("N") )
						 {
							 str = "";
//							 row = spreadsheet.createRow(nrow++);
							 link=link.substring(1,link.length());
							 link=link.split("-")[0];
							 
							 trd_abbr=trd_abbr.trim();
							 value=trd_abbr;
							 if (mpe.counterparty_map.containsKey(value)) {
							        value =mpe.counterparty_map.get(value); 
							        trd_abbr = value; 
							    }
							 
							 
							 for (int i = 0; i < columns.split(",").length; i++) {
									
									value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replaceAll(",", " ");;
//									cell = row.createCell(i);
									
									if(i==0) {
										
											 if (dom_buy_flag == null) {
												 cont_type = "N";  // Handle null first
												}else if (dom_buy_flag.equals("Y")) {
													cont_type = "D";
												} else if (dom_buy_flag.equals("K")) {
													cont_type = "I";
												} else if (dom_buy_flag.equals("T")) {
													dom_buy_flag="Y";
													cont_type = "T";
												}else if (dom_buy_flag.equals("N")) {
													cont_type = "N";
												}	 
											 value = trd_abbr+"-"+sn_no+"-"+link+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";// link is used as mandate_conform
//											 cell.setCellValue("'"+value+"'");
											 str += value + ",";
											
										}
									else if(i==1)
									{
//										cell.setCellValue("'"+trd_abbr+"'");
										str += trd_abbr + ",";
									}
									else if(i==2)//seq_no
									{
//										cell.setCellValue("'"+rset.getString(3)+"'");
										str += rset.getString(3) + ",";
									}
									else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
										
										 if (mpe.counterparty_map.containsKey(value)) {
										        value =mpe.counterparty_map.get(value);  
										 }
										 if (value.contains("RIL")) {
											 value = "RIL";
										 }
//										 cell.setCellValue("'"+value+"'");
										 str += value + ",";
									}
									
									else if(i==8) {
										if(value.equals("INR")) {
											value="1";
										}
										else if(value.equals("USD")) {
											value="2";
										} 
//										 cell.setCellValue("'"+value+"'");
										str += value + ",";
									}
									else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
										queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1, value);
										rset4 = stmt4.executeQuery();
										if(rset4.next()) {
//											cell.setCellValue("'"+rset4.getString(1)+"'");
											str += rset4.getString(1).replaceAll(",", "@@") + ",";
										}
										else {
//											cell.setCellValue("'"+value+"'");
											str += value + ",";
										}
										rset4.close();
										stmt4.close();
									}
									else if(i == 25 && !value.equals("null")) {	// Status
												if(value.equals("Pending")) {
													value = "P";
												}
												else if(value.equals("In order")) {
													value = "O";
												}
												else if(value.equals("Cancelled")) {
													value = "C";
												}
												else if(value.equals("Pending for amendment")) {
													value = "A";
												}
												else if(value.equals("Restated")) {
													value = "R";
												}
												else if(value.equals("Dummy")) {
													value = "D";
												}
												else if(value.equals("Expired")) {
													value = "E";
												}
//												cell.setCellValue("'"+value+"'");
												str += value + ",";
									}
									else if(i==36)//GX
									{
										if(trd_abbr.contains("IGX"))
										{
											value="I";
										}
										else
										{
											value="K";
										}
//										cell.setCellValue("'" + value + "'");
										str += value + ",";
											
									}
									else {
//								cell.setCellValue("'" + value + "'");
										str += value.replaceAll(",", " ") + ",";
									}
								}
								count++;
								logger.insert_data(fname_csv, str, conn);
								logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
						 }
					 }
					 stmt1.close();
					 rset1.close();
					 
					 
			}
			else if(rset.getString(1).equals("0"))
			{
					link=rset.getString(1);
					
					trd_abbr=rset.getString(2);

//					 row = spreadsheet.createRow(nrow++);
						 str = "";
						 trd_abbr=trd_abbr.trim();
						 value=trd_abbr;
						 if (mpe.counterparty_map.containsKey(value)) {
						        value =mpe.counterparty_map.get(value); 
						        trd_abbr = value; 
						    }
						
						 for (int i = 0; i < columns.split(",").length; i++) {
								
								value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replaceAll(",", " ");;
//								cell = row.createCell(i);
								
								if(i==0) {
									
									 value = trd_abbr+":"+0+":"+"NA=PUR";
//									 cell.setCellValue("'"+value+"'");
									 str += value + ",";
								}
								else if(i==1)//COUNTERPARTY_ABBR
								{
//									cell.setCellValue("'"+trd_abbr+"'");
									str += trd_abbr + ",";
								}
								else if(i==2)//seq_no
								{
//									cell.setCellValue("'"+rset.getString(3)+"'");
									str += rset.getString(3) + ",";
								}
								else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
									
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value);  
									 }
									 if (value.contains("RIL")) {
										 value = "RIL";
									 }
//									 cell.setCellValue("'"+value+"'");
									 str += value + ",";
								}	
								else if(i==8) {
									if(value.equals("INR")) {
										value="1";
									}
									else if(value.equals("USD")) {
										value="2";
									} 
//									 cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
								else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
									queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, value);
									rset4 = stmt4.executeQuery();
									if(rset4.next()) {
//										cell.setCellValue("'"+rset4.getString(1)+"'");
										str += rset4.getString(1).replaceAll(",", "@@") + ",";
									}
									else {
//										cell.setCellValue("'"+value+"'");
										str += value + ",";
									}
									rset4.close();
									stmt4.close();
								}
								else if(i == 25 && !value.equals("null")) {	// Status
											if(value.equals("Pending")) {
												value = "P";
											}
											else if(value.equals("In order")) {
												value = "O";
											}
											else if(value.equals("Cancelled")) {
												value = "C";
											}
											else if(value.equals("Pending for amendment")) {
												value = "A";
											}
											else if(value.equals("Restated")) {
												value = "R";
											}
											else if(value.equals("Dummy")) {
												value = "D";
											}
											else if(value.equals("Expired")) {
												value = "E";
											}
//											cell.setCellValue("'"+value+"'");
											str += value + ",";
								}
								else if(i==36)//GX
								{
									if(trd_abbr.contains("IGX"))
									{
										value="I";
									}
									else
									{
										value="K";
									}
//									cell.setCellValue("'" + value + "'");
									str += value + ",";
										
								}
							else {
//							cell.setCellValue("'" + value + "'");
								str += value.replaceAll(",", " ") + ",";
							}
							
						 }count++;
							logger.insert_data(fname_csv, str, conn);
							logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
				}
            }
			else if(rset.getString(1)==null && (!rset.getString(4).contains("AM/NS-T")  || !rset.getString(4).contains("RIL-D6") || !rset.getString(4).contains("RIL-CBM")) )
			{

				link=rset.getString(1);
				
				trd_abbr=rset.getString(2);

//				 row = spreadsheet.createRow(nrow++);
				str = "";
				 
					 trd_abbr=trd_abbr.trim();
					 value=trd_abbr;
					 if (mpe.counterparty_map.containsKey(value)) {
					        value =mpe.counterparty_map.get(value); 
					        trd_abbr = value; 
					    }
					
					 
					 for (int i = 0; i < columns.split(",").length; i++) {
							
							value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replaceAll(",", " ");
//							cell = row.createCell(i);
							
							if(i==0) {
								
								 value = trd_abbr+":"+0+":"+"NA=PUR";
//								 cell.setCellValue("'"+value+"'");
								 str += value + ",";
							}
							else if(i==1)// ABBR
							{
//								cell.setCellValue("'"+trd_abbr+"'");
								str += trd_abbr + ",";
							}
							else if(i==2)//seq_no
							{
//								cell.setCellValue("'"+rset.getString(3)+"'");
								str += rset.getString(3) + ",";
							}
							else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
								
								 if (mpe.counterparty_map.containsKey(value)) {
								        value =mpe.counterparty_map.get(value);  
								 }
								 if (value.contains("RIL")) {
									 value = "RIL";
								 }
//								 cell.setCellValue("'"+value+"'");
								 str += value + ",";
							}	
							else if(i==8) {
								if(value.equals("INR")) {
									value="1";
								}
								else if(value.equals("USD")) {
									value="2";
								} 
//								 cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
								queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, value);
								rset4 = stmt4.executeQuery();
								if(rset4.next()) {
//									cell.setCellValue("'"+rset4.getString(1)+"'");
									str += rset4.getString(1).replaceAll(",", "@@") + ",";
								}
								else {
//									cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
								rset4.close();
								stmt4.close();
							}
							else if(i == 25 && !value.equals("null")) {	// Status
										if(value.equals("Pending")) {
											value = "P";
										}
										else if(value.equals("In order")) {
											value = "O";
										}
										else if(value.equals("Cancelled")) {
											value = "C";
										}
										else if(value.equals("Pending for amendment")) {
											value = "A";
										}
										else if(value.equals("Restated")) {
											value = "R";
										}
										else if(value.equals("Dummy")) {
											value = "D";
										}
										else if(value.equals("Expired")) {
											value = "E";
										}
//										cell.setCellValue("'"+value+"'");
										str += value + ",";
							}
							else if(i==36)//GX
							{
								if(trd_abbr.contains("IGX"))
								{
									value="I";
								}
								else
								{
									value="K";
								}
//								cell.setCellValue("'" + value + "'");
								str += value + ",";
									
							}
						else {
//						cell.setCellValue("'" + value + "'");
							str += value.replaceAll(",", " ") + ",";
						}
						
					 }count++;
						logger.insert_data(fname_csv, str, conn);
						logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
			    }
            }
			stmt.close();
			rset.close();
			
			//SALES :----------------------------------------------------------------------------------------------------------------------
			
			String seq_no="",map="",cont_rev="",agmt_rev="",counterparty_cd="";
			
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, " COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX ,CONT_TYPE,TIMESTAMP", conn);
			while (rset.next() ) {
				counterparty_cd=rset.getString(2);
				//FOR SN
				int num = 1; 
				queryString1 = "SELECT A.FGSA_NO, A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD "
						+ "FROM FMS7_SN_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next() ) {
					map = "S-"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4);
					
					queryString2 = "SELECT CUSTOMER_CD,SEQ_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,DTL_SEQ_NO,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS'),CUSTOMER_ABBR  "
							+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("S"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)));
					stmt2.setString(2, rset1.getString(5));
					stmt2.setString(3, counterparty_cd);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next() && !rset2.getString(49).contains("ESSARRM")) {
//						row = spreadsheet.createRow(nrow++);
						str = "";
//						ncell = 0;
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
//						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
//						cell.setCellValue("'"+abbr+"=SALES'");
						str += abbr+"=SALES'" + ",";
//						
//						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
//						cell.setCellValue("'"+map+"'");
						str += map + ",";
						
						for (int i = 2; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll(",", " ");;
						
//							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
//								cell.setCellValue("'"+seq_no+"'");									
//							}
//							else if (i == 3) {	// sec_ref_no
//								value = rset.getString(1) + "-S-" + num ;
//								cell.setCellValue("'"+value+"'");
//								num++;
//							}
							 if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, value);
								rset4 = stmt4.executeQuery();
								if(rset4.next()) {
//									cell.setCellValue("'"+rset4.getString(1)+"'");
									str += rset4.getString(1).replaceAll(",", "@@") + ",";
								}
								else {
//									cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
								rset4.close();
								stmt4.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else {
//								cell.setCellValue("'"+value+"'");
								str += value.replaceAll(",", " ") + ",";
							}
						}
						count++;
						logger.insert_data(fname_csv, str, conn);
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"S"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				
				
				rset1.close();
				stmt1.close();
					
//				//FOR LOA
				queryString1 = "SELECT A.TENDER_NO,A.LOA_NO,A.LOA_REV_NO,A.CUSTOMER_CD "
						+ "FROM FMS7_LOA_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {	
					map = "L-"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3);
					queryString2 = "SELECT NULL,SEQ_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"//9
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"//18
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ " TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD,"
							+ " TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,DTL_SEQ_NO,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS'),CUSTOMER_ABBR   "
							+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("L"+rset1.getString(1)+"-"+"0"+"-"+rset1.getString(2)+"-"+rset1.getString(3)));//L1-0-1-0
					stmt2.setString(2, rset1.getString(4));
					stmt2.setString(3, counterparty_cd);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next() &&  !rset2.getString(49).contains("ESSARRM")) {
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
//						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
//						cell.setCellValue("'"+abbr+"=SALES'");
						str += abbr+"=SALES'" + ",";
						
//						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
//						cell.setCellValue("'"+map+"'");
						str += map + ",";
						
						for (int i = 2; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll(",", " ");
						
//							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
//								cell.setCellValue("'"+seq_no+"'");									
//							}
//							else if (i == 3) {	// sec_ref_no
//								value = rset.getString(1) + "-S-" + num ;
//								cell.setCellValue("'"+value+"'");
//								num++;
//							}
							 if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, value);
								rset4 = stmt4.executeQuery();
								if(rset4.next()) {
//									cell.setCellValue("'"+rset4.getString(1)+"'");
									str += rset4.getString(1).replaceAll(",", "@@") + ",";
								}
								else {
//									cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
								rset4.close();
								stmt4.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else {
//								cell.setCellValue("'"+value+"'");
								str += value.replaceAll(",", " ") + ",";
							}
						}
						count++;
						logger.insert_data(fname_csv, str, conn);
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"L"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
				
				
				
				//FOR LTCORA
				queryString1 = "SELECT A.CUSTOMER_CD,A.AGREEMENT_NO,A.REV_NO,A.CN_NO,A.CN_REV_NO,A.CN_TERM "
						+ "FROM FMS8_LNG_REGAS_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {	
//					System.out.println(rset1.getString(1));
					queryString2 = "SELECT NULL,SEQ_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,DTL_SEQ_NO,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS')  "
							+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, "C"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)+"-"+rset1.getString(5));//C1-1-0-1-0
					stmt2.setString(2, rset1.getString(1));
					stmt2.setString(3, counterparty_cd);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					while(rset2.next()) {
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						
						customer_cd = rset1.getString(1);
						agmt_no = rset1.getString(2);
						agmt_rev = rset1.getString(3);
						cont_no = rset1.getString(4);
						cont_rev = rset1.getString(5);
						cont_type = rset1.getString(6);
						str = "";
						
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
//						cell.setCellValue("'"+abbr+"=SALES'");
						str += abbr+"=SALES'" + ",";
						
//						cell = row.createCell(ncell++);
						
						if(cont_type.equals("C")) { 
							cont_ref = "C"+"-"+customer_cd+"-"+agmt_no+"-"+cont_no+"-"+cont_rev;
						}
						else if(cont_type.equals("A")) {
							cont_ref = "A"+"-"+customer_cd+"-"+agmt_no+"-"+cont_no+"-"+cont_rev;
						}
//						cell.setCellValue("'"+cont_ref+"'");
						str += cont_ref + ",";
						
						for (int i = 2; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll(",", " ");;
						
//							if (i == 2) {	// seq_no// if abbr change then only seq_no will be 1 otherwise +1
//								if(abbr.equals(prev_abbr))
//								{
//									seq_no = num+"";
//								}
//								else {
//									seq_no="1";
//								}
//								cell.setCellValue("'"+seq_no+"'");									
//							}
//							else if (i == 3) {	// sec_ref_no
//								value = rset.getString(1) + "-S-" + num ;
//								cell.setCellValue("'"+value+"'");
//								num++;
//							}
							 if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt4 = conn.prepareStatement(queryString4);
								stmt4.setString(1, value);
								rset4 = stmt4.executeQuery();
								if(rset4.next()) {
//									cell.setCellValue("'"+rset4.getString(1)+"'");
									str += rset4.getString(1).replaceAll(",", "@@") + ",";
								}
								else {
//									cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
								rset4.close();
								stmt4.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else {
//								cell.setCellValue("'"+value+"'");
								str += value.replaceAll(",", " ") + ",";
							}
						}
						count++;
						logger.insert_data(fname_csv, str, conn);
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"L"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
				}
				rset1.close();
				stmt1.close();
					
			}
			rset.close();
			stmt.close();
			
			
			
			
			
			//DLNG :
			String no="",remark="",rev="";
			counterparty_cd="";
			cont_rev="";
			map="";
			seq_no="";
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
					+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			logger.checkpoint(fname, " COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX ,CONT_TYPE,TIMESTAMP", conn);
			while (rset.next()) {
				abbr = rset.getString(1) == null ? "null" : rset.getString(1);
				counterparty_cd = rset.getString(2) == null ? "null" : rset.getString(2);
				if(abbr!=null) {
					abbr= abbr.toUpperCase();
				}
				remark = "";
				
				//FOR SN
				int num = 1; 
				queryString1 = "SELECT A.FLSA_NO, A.FLSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD "
						+ "FROM DLNG_SN_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					no = rset1.getString(1);
					rev = rset1.getString(2);
					cont_no = rset1.getString(3);
					cont_rev = rset1.getString(4);
					cd = rset1.getString(5);
					map = "S-"+no+"-"+rev+"-"+cont_no+"-"+cont_rev;
					
					//SN WITHOUT ADV
					queryString2 = "SELECT SEQ_NO,REF_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,DTL_SEQ_NO,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS')  "
							+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
//							+ "AND SEC_TYPE NOT IN ('ADV')"
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("S"+no+"-"+rev+"-"+cont_no+"-"+cont_rev+"-DLNG"));
					stmt2.setString(2, rset1.getString(5));
					stmt2.setString(3, counterparty_cd);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next()) {
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
//						System.out.println(remark);
//						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
//						cell.setCellValue("'"+abbr+"=DLNG'");
						str += abbr+"=DLNG'" + ",";
						
//						cell = row.createCell(ncell++);
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
//						cell.setCellValue("'"+rset2.getString(1)+"'");
						str += rset2.getString(1) + ",";
						
						for (int i = 2; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll(",", " ");;
							
							if(i==2)
							{
//								cell.setCellValue("'"+rset2.getString(1)+"'");
								str += rset2.getString(1) + ",";
							}
							else if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
//									cell.setCellValue("'"+rset3.getString(1)+"'");
									str += rset3.getString(1).replaceAll(",", "@@") + ",";
								}
								else {
//									cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
								rset3.close();
								stmt3.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else {
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
						}
						count++;
						logger.insert_data(fname_csv, str, conn);
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"S"+","), conn, "");
					}
					rset2.close();
					stmt2.close();

				}
				
				
				rset1.close();
				stmt1.close();
					
//				//FOR LOA
				queryString1 = "SELECT A.TENDER_NO,A.LOA_NO,A.LOA_REV_NO,A.CUSTOMER_CD "
						+ "FROM DLNG_LOA_MST A, FMS7_CUSTOMER_MST B "
						+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
						+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
						+ "AND B.COUNTERPARTY_CD = ? "
						+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
						+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {	
					no = rset1.getString(1);
					rev = "0";
					cont_no = rset1.getString(2);
					cont_rev = rset1.getString(3);
					cd = rset1.getString(4);
					map = "L-"+no+"-"+cont_no+"-"+cont_rev;
					
					//LOA WITHOUT ADV
					queryString2 = "SELECT SEQ_NO,REF_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
							+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
							+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
							+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,NULL,DTL_SEQ_NO,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS')  "
							+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE LINK = ? AND ((COUNTERPARTY_CD IS NULL AND CUSTOMER_CD = ?) OR COUNTERPARTY_CD = ? ) "
							+ "AND SEC_TYPE NOT IN ('ADV') "
							+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, ("L"+no+"-"+"0"+"-"+cont_no+"-"+cont_rev+"-DLNG"));//L1-0-1-0
					stmt2.setString(2, rset1.getString(4));
					stmt2.setString(3, counterparty_cd);
					stmt2.setString(4, delta_FromDt);
					stmt2.setString(5, delta_FromDt);
					stmt2.setString(6, delta_ToDt);
					stmt2.setString(7, delta_ToDt);
					rset2 = stmt2.executeQuery();
					
					while(rset2.next()) {
//						row = spreadsheet.createRow(nrow++);
//						ncell = 0;
						str = "";
						abbr = rset.getString(1) == null ? "null" : rset.getString(1);
						remark = rset2.getString(26) == null ? "null" : rset2.getString(26);
//						cell = row.createCell(ncell++);
						if (mpe.counterparty_map.containsKey(abbr)) {
							abbr =mpe.counterparty_map.get(abbr); 
					    }
//						cell.setCellValue("'"+abbr+"=DLNG'");
						str += abbr+"=DLNG'" + ",";
						
//						cell = row.createCell(ncell++);
						
						
						cd = rset.getString(2) == null ? "null" : rset.getString(2);
//						cell.setCellValue("'"+rset2.getString(1)+"'");
						str += rset2.getString(1) + ",";
						
						for (int i = 2; i < columns.split(",").length; i++) {
//							cell = row.createCell(ncell++);
							value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll(",", " ");;
						
							if(i==2)
							{
//								cell.setCellValue("'"+rset2.getString(1)+"'");
								str += rset2.getString(1) + ",";
							}
							else if (i == 7) {	// GUARANTOR_CD
								if (mpe.counterparty_map.containsKey(value)) {
									value =mpe.counterparty_map.get(value); 
							    }
								if(value.contains("RIL"))
								{
									value="RIL";
								}
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if(i == 8 && !value.equals("null")) {	// Currency
								
								value = value.equals("INR") ? "1" : "2";
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
							
								queryString3 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, value);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
//									cell.setCellValue("'"+rset3.getString(1)+"'");
									str += rset3.getString(1).replaceAll(",", "@@") + ",";
								}
								else {
//									cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
								rset3.close();
								stmt3.close();
							}
							else if (i == 25 && !value.equals("null")) {	// Status
								if(value.equals("Pending")) {
									value = "P";
								}
								else if(value.equals("In order")) {
									value = "O";
								}
								else if(value.equals("Cancelled")) {
									value = "C";
								}
								else if(value.equals("Pending for amendment")) {
									value = "A";
								}
								else if(value.equals("Restated")) {
									value = "R";
								}
								else if(value.equals("Dummy")) {
									value = "D";
								}
								else if(value.equals("Expired")) {
									value = "E";
								}
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
							else {
//								cell.setCellValue("'"+value+"'");
								str += value + ",";
							}
						}
						count++;
						logger.insert_data(fname_csv, str, conn);
						logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"L"+","), conn, "");
					}
					rset2.close();
					stmt2.close();
					

				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
			
			
			//MERGE COUNTERPARTY :PURCHASE 
			int seq_no_merge=0;
			String org_abbr="";

			Map<String, Integer> merge_seq_no = new HashMap<String, Integer>();
			queryString="SELECT LINK,CUSTOMER_ABBR,SEQ_NO,REF_NO,NVL(ISSUED,'R'),SEC_TYPE,DEAL_TYPE,GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
					+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,"
					+ "TO_CHAR(REC_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(ISSU_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "TO_CHAR(RENEW_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(CANCEL_DT,'DD/MM/YYYY HH24:MI:SS'),TENOR,TO_CHAR(REVIEW_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "STATUS,REMARKS,FLAG,INORDER_HIST,ENT_CD,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "MODIFY_BY,TO_CHAR(APRV_DT,'DD/MM/YYYY HH24:MI:SS'),APRV_BY,0,'K',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,DTL_SEQ_NO,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS')  "
					+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE"
					+ " (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_ABBR DESC ";
			
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			

			logger.checkpoint(fname, "CONT_MAPPING_ID,ALLOC_DT,TIMESTAMP", conn);
			
			while(rset.next())
			{	
				if(rset.getString(1)!=null 
						&& (rset.getString(2).contains("AM/NS-T")  || rset.getString(2).contains("RIL-D6") || 
								rset.getString(4).contains("RIL-CBM")  ))
				{
					if(rset.getString(1).startsWith("B")) { 
					link=rset.getString(1);
					trd_abbr=rset.getString(2);
				
					man_cnf_cd=link.split("-")[0];
					man_cnf_cd=man_cnf_cd.substring(1,man_cnf_cd.length());
					cargo_seq_no=link.split("-")[1];
					
					 queryString1="SELECT CARGO_REF_CD,NVL(DOM_BUY_FLAG, 'N') FROM FMS7_MAN_CONFIRM_CARGO_DTL WHERE MAN_CONF_CD= ? AND CARGO_SEQ_NO = ? ";
					 stmt1=conn.prepareStatement(queryString1);
					 stmt1.setString(1, man_cnf_cd);
					 stmt1.setString(2,cargo_seq_no );
					 rset1=stmt1.executeQuery();
					 
					 
					 while(rset1.next()) {
						 
						 cargo_ref_cd=rset1.getString(1);
						 dom_buy_flag=rset1.getString(2);

						 queryString2="SELECT SN_NO,SN_REV_NO FROM FMS7_TRADER_CONT_MST WHERE CARGO_SEL LIKE ? AND DOM_BUY_FLAG= ?  "
							 		+ "ORDER BY CUSTOMER_CD,DOM_BUY_FLAG,SN_NO,SN_REV_NO DESC";
							 stmt2=conn.prepareStatement(queryString2);
							 stmt2.setString(1,"%"+cargo_ref_cd+"%");
							 stmt2.setString(2,dom_buy_flag.equals("T") ? "Y" : dom_buy_flag);

							 rset2=stmt2.executeQuery();
							 
							 
							 if(rset2.next())
							 {
								 do {
									 str = "";
									 sn_no=rset2.getString(1);
									 sn_rev=rset2.getString(2);

//									 row = spreadsheet.createRow(nrow++);
									 
										 trd_abbr=trd_abbr.trim();
										 value=trd_abbr;
										 org_abbr=trd_abbr;
										 
										 if (mpe.counterparty_map.containsKey(value)) {
										        value =mpe.counterparty_map.get(value); 
										        trd_abbr = value; 
										 }
										 
										 if(trd_abbr.contains("RIL")) {
											 trd_abbr="RIL";
											 customer_cd="1";
											 
										 }
										 if(trd_abbr.contains("AMNS-T") || trd_abbr.contains("AM/NS-T") ) {
											 trd_abbr="AMNS";
											 customer_cd="33";
										 }
										 
										 if(trd_abbr.contains("ESSARRM")  ) {
											 trd_abbr="NEL";
											 customer_cd="209";
										 }
										 
										 for (int i = 0; i < columns.split(",").length; i++) {
												
												value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replaceAll(",", " ");
//												cell = row.createCell(i);
												
												if(i==0) {
													if (dom_buy_flag.equals("Y")) {
														cont_type = "D";
													} else if (dom_buy_flag.equals("K")) {
														cont_type = "I";
													} else if (dom_buy_flag.equals("T")) {
														cont_type = "T";
													}else if (dom_buy_flag.equals("N")) {
														cont_type = "N";
													}	 
													 value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";
//													 cell.setCellValue("'"+value+"'");
													 str += value + ",";
														
												}
												else if(i==1) {//
//													cell.setCellValue("'"+trd_abbr+"'");
													str += trd_abbr + ",";
												}
												else if(i==2)//seq_no
												{
													queryString4 = "SELECT MAX(SEQ_NO) FROM LOG_FMS9_SECURITY_POST_MST  WHERE COUNTERPARTY_CD =?   ";
													stmt4 = conn.prepareStatement(queryString4);
													stmt4.setString(1,customer_cd);
													rset4 = stmt4.executeQuery();
													if(rset4.next()) {
														seq_no_merge=rset4.getInt(1)+1;
														value=seq_no_merge+"";
													}
													else {
														value="1";
													}
													rset4.close();
													stmt4.close();
													
												
													if(merge_seq_no.containsKey(trd_abbr)) {
														value = (merge_seq_no.get(trd_abbr)+1)+"";
														seq_no_merge=Integer.parseInt(value);
														merge_seq_no.put(trd_abbr, Integer.parseInt(value));
													}
													else {
														merge_seq_no.put(trd_abbr, Integer.parseInt(value));
													}
													
//													cell.setCellValue("'"+value+"'");
													str += value + ",";
												}
												else if(i==3)//SEC_REF_NO
												{
//													cell.setCellValue("'"+org_abbr+"-S-"+seq_no_merge+"'");
													str += org_abbr+"-S-"+seq_no_merge + ",";
//													System.out.println("org=="+org_abbr+"-S-"+seq_no_merge+":trd_abbr=="+trd_abbr);										
												}
												
												else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
													
													 if (mpe.counterparty_map.containsKey(value)) {
													        value =mpe.counterparty_map.get(value);  
													 }
													 if (value.contains("RIL")) {
														 value = "RIL";
													 }
//													 cell.setCellValue("'"+value+"'");
													 str += value + ",";
												}
												else if(i==8) {
													if(value.equals("INR")) {
														value="1";
													}
													else if(value.equals("USD")) {
														value="2";
													} 
//													 cell.setCellValue("'"+value+"'");
													str += value + ",";
												}
												else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
													queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
													stmt4 = conn.prepareStatement(queryString4);
													stmt4.setString(1, value);
													rset4 = stmt4.executeQuery();
													if(rset4.next()) {
//														cell.setCellValue("'"+rset4.getString(1)+"'");
														str += rset4.getString(1).replaceAll(",", "@@") + ",";
													}
													else {
//														cell.setCellValue("'"+value+"'");
														str += value + ",";
													}
													rset4.close();
													stmt4.close();
												}
												else if(i == 25 && !value.equals("null")) {	// Status
															if(value.equals("Pending")) {
																value = "P";
															}
															else if(value.equals("In order")) {
																value = "O";
															}
															else if(value.equals("Cancelled")) {
																value = "C";
															}
															else if(value.equals("Pending for amendment")) {
																value = "A";
															}
															else if(value.equals("Restated")) {
																value = "R";
															}
															else if(value.equals("Dummy")) {
																value = "D";
															}
															else if(value.equals("Expired")) {
																value = "E";
															}
//															cell.setCellValue("'"+value+"'");
															str += value + ",";
												}
												else if(i==36)//GX
												{
													if(trd_abbr.contains("IGX"))
													{
														value="I";
													}
													else
													{
														value="K";
													}
//													cell.setCellValue("'" + value + "'");
													str += value + ",";
														
												}
												else {
//											cell.setCellValue("'" + value + "'");
													str += value + ",";}
												
											}
											count++;
											logger.insert_data(fname_csv, str, conn);
											logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
								 }while(rset2.next());
								
								 stmt2.close();
								 rset2.close();
								
								 
							 }
							 else
							 {
								 sn_no="0";
								 sn_rev="0";
								 str = "";
//								 row = spreadsheet.createRow(nrow++);
								 
									 trd_abbr=trd_abbr.trim();
									 value=trd_abbr;
									 org_abbr=trd_abbr;
									 
									 if (mpe.counterparty_map.containsKey(value)) {
									        value =mpe.counterparty_map.get(value); 
									        trd_abbr = value; 
									 }
									 
									 if(trd_abbr.contains("RIL")) {
										 trd_abbr="RIL";
										 customer_cd="1";
										 
									 }
									 
									 if(trd_abbr.contains("AMNS-T") || trd_abbr.contains("AM/NS-T") ) {
										 trd_abbr="AMNS";
										 customer_cd="33";
									 }
									 
									 if(trd_abbr.contains("ESSARRM")) {
										 trd_abbr="NEL";
										 customer_cd="209";
									 }
									 
									 for (int i = 0; i < columns.split(",").length; i++) {
											
											value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replaceAll(",", " ");
//											cell = row.createCell(i);
											
											if(i==0) {
												if (dom_buy_flag.equals("Y")) {
													cont_type = "D";
												} else if (dom_buy_flag.equals("K")) {
													cont_type = "I";
												} else if (dom_buy_flag.equals("T")) {
													cont_type = "T";
												}else if (dom_buy_flag.equals("N")) {
													cont_type = "N";
												}	 
												 value = trd_abbr+"-"+sn_no+"-"+cargo_seq_no+"-"+cont_type+"-"+cargo_ref_cd+"-"+sn_rev+"=PUR";
//												 cell.setCellValue("'"+value+"'");
												 str += value + ",";
													
											}
											else if(i==1) {//
//												cell.setCellValue("'"+trd_abbr+"'");
												str += trd_abbr + ",";
											}
											else if(i==2)//seq_no
											{
												queryString4 = "SELECT MAX(SEQ_NO) FROM LOG_FMS9_SECURITY_POST_MST  WHERE COUNTERPARTY_CD =?   ";
												stmt4 = conn.prepareStatement(queryString4);
												stmt4.setString(1,customer_cd);
												rset4 = stmt4.executeQuery();
												if(rset4.next()) {
													seq_no_merge=rset4.getInt(1)+1;
													value=seq_no_merge+"";
												}
												else {
													value="1";
												}
												rset4.close();
												stmt4.close();
												
											
												if(merge_seq_no.containsKey(trd_abbr)) {
													value = (merge_seq_no.get(trd_abbr)+1)+"";
													seq_no_merge=Integer.parseInt(value);
													merge_seq_no.put(trd_abbr, Integer.parseInt(value));
												}
												else {
													merge_seq_no.put(trd_abbr, Integer.parseInt(value));
												}
												
//												cell.setCellValue("'"+value+"'");
												str += value + ",";
											}
											else if(i==3)//SEC_REF_NO
											{
//												cell.setCellValue("'"+org_abbr+"-S-"+seq_no_merge+"'");
												str += org_abbr+"-S-"+seq_no_merge + ",";
//												System.out.println("org=="+org_abbr+"-S-"+seq_no_merge+":trd_abbr=="+trd_abbr);										
											}
											
											else if (i == 7 && !value.equals("null")) {	// GUARANTOR_ABBR
												
												 if (mpe.counterparty_map.containsKey(value)) {
												        value =mpe.counterparty_map.get(value);  
												 }
												 if (value.contains("RIL")) {
													 value = "RIL";
												 }
//												 cell.setCellValue("'"+value+"'");
												 str += value + ",";
											}
											else if(i==8) {
												if(value.equals("INR")) {
													value="1";
												}
												else if(value.equals("USD")) {
													value="2";
												} 
//												 cell.setCellValue("'"+value+"'");
												str += value + ",";
											}
											else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {	// Bank_Name
												queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
												stmt4 = conn.prepareStatement(queryString4);
												stmt4.setString(1, value);
												rset4 = stmt4.executeQuery();
												if(rset4.next()) {
//													cell.setCellValue("'"+rset4.getString(1)+"'");
													str += rset4.getString(1).replaceAll(",", "@@") + ",";
												}
												else {
//													cell.setCellValue("'"+value+"'");
													str += value + ",";
												}
												rset4.close();
												stmt4.close();
											}
											else if(i == 25 && !value.equals("null")) {	// Status
														if(value.equals("Pending")) {
															value = "P";
														}
														else if(value.equals("In order")) {
															value = "O";
														}
														else if(value.equals("Cancelled")) {
															value = "C";
														}
														else if(value.equals("Pending for amendment")) {
															value = "A";
														}
														else if(value.equals("Restated")) {
															value = "R";
														}
														else if(value.equals("Dummy")) {
															value = "D";
														}
														else if(value.equals("Expired")) {
															value = "E";
														}
//														cell.setCellValue("'"+value+"'");
														str += value + ",";
											}
											else if(i==36)//GX
											{
												if(trd_abbr.contains("IGX"))
												{
													value="I";
												}
												else
												{
													value="K";
												}
//												cell.setCellValue("'" + value + "'");
												str += value + ",";
													
											}
											else {
//										cell.setCellValue("'" + value + "'");
												str += value + ",";}
											
										}
										count++;
										logger.insert_data(fname_csv, str, conn);
										logger.data(fname, (cont_map_id+","+alloc_dt+","), conn, "");
								 
							 }
							
							 stmt2.close();
							 rset2.close();
						 }
						 stmt1.close();
						 rset1.close();
				  }
	             }
	            }
				stmt.close();
				rset.close();
			
				
				//MERGE SECURITY SALES 
				
				String sales_abbr="";
				seq_no_merge=0;
				queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
						+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND "
						+ "A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				
				logger.checkpoint(fname, " COMPANY_CD, COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX ,CONT_TYPE,TIMESTAMP", conn);
				while (rset.next() ) {
					
					//FOR SN
					int num = 1; 
					queryString1 = "SELECT A.FGSA_NO, A.FGSA_REV_NO,A.SN_NO,A.SN_REV_NO,A.CUSTOMER_CD "
							+ "FROM FMS7_SN_MST A, FMS7_CUSTOMER_MST B "
							+ "WHERE B.CUSTOMER_CD = A.CUSTOMER_CD AND "
							+ "B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) "
							+ "AND B.COUNTERPARTY_CD = ? "
							+ "AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
							+ "AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, rset.getString(2));
					stmt1.setString(2, delta_FromDt);
					stmt1.setString(3, delta_FromDt);
					stmt1.setString(4, delta_ToDt);
					stmt1.setString(5, delta_ToDt);
					rset1 = stmt1.executeQuery();

					while (rset1.next() ) {
						map = "S-"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4);
						
						queryString2 = "SELECT CUSTOMER_CD,SEQ_NO,REF_NO,NVL(ISSUED, 'R'),SEC_TYPE, UPPER(DEAL_TYPE),GUARANTOR_ABBR,CURRENCY,VARIATION_VALUE,"
								+ "VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD ,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF, TO_CHAR(REC_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "TO_CHAR(ISSU_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EXP_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(RENEW_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(CANCEL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "TENOR,TO_CHAR(REVIEW_DT, 'DD/MM/YYYY HH24:MI:SS'), STATUS, REMARKS, FLAG, INORDER_HIST,ENT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY,"
								+ "TO_CHAR(APRV_DT, 'DD/MM/YYYY HH24:MI:SS'), APRV_BY,'0', 'K',"
								+ "NULL, NULL, NULL, NULL, NULL, NULL, NULL ,NULL,NULL,NULL,DTL_SEQ_NO,TO_CHAR(DTL_ENT_DATE, 'DD/MM/YYYY HH24:MI:SS'),CUSTOMER_ABBR  "
								+ "FROM LOG_FMS9_SECURITY_POST_MST WHERE LINK = ? AND CUSTOMER_CD = ? "
								+ "AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) "
								+ "AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, ("S"+rset1.getString(1)+"-"+rset1.getString(2)+"-"+rset1.getString(3)+"-"+rset1.getString(4)));
						stmt2.setString(2, rset1.getString(5));
						stmt2.setString(3, delta_FromDt);
						stmt2.setString(4, delta_FromDt);
						stmt2.setString(5, delta_ToDt);
						stmt2.setString(6, delta_ToDt);
						rset2 = stmt2.executeQuery();
						
						while(rset2.next() && rset2.getString(49).contains("ESSARRM")) {
//							row = spreadsheet.createRow(nrow++);
//							ncell = 0;
							str = "";
							abbr = rset.getString(1) == null ? "null" : rset.getString(1);
//							cell = row.createCell(ncell++);
							if (mpe.counterparty_map.containsKey(abbr)) {
								abbr =mpe.counterparty_map.get(abbr); 
						    }
							sales_abbr=rset2.getString(47);
//							cell.setCellValue("'"+abbr+"=SALES'");
							str += abbr+"=SALES'" + ",";
							
//							cell = row.createCell(ncell++);
							
							cd = rset.getString(2) == null ? "null" : rset.getString(2);
//							cell.setCellValue("'"+map+"'");
							str += map + ",";
							
							if(sales_abbr.contains("ESSARRM")  ) {
								abbr="NEL";
								customer_cd="209";
							 }
							
							
							
							
							
							for (int i = 2; i < columns.split(",").length; i++) {
//								cell = row.createCell(ncell++);
								value = rset2.getString(i) == null ? "null" : rset2.getString(i).replaceAll(",", " ");
							

								if(i==2)//SEQ_NO FROM FMS_DIRECT
								 {
									 	queryString4 = "SELECT MAX(SEQ_NO) FROM LOG_FMS9_SECURITY_POST_MST  WHERE COUNTERPARTY_CD =?   ";
										stmt4 = conn.prepareStatement(queryString4);
										stmt4.setString(1,customer_cd);
										rset4 = stmt4.executeQuery();
										if(rset4.next()) {
											seq_no_merge=rset4.getInt(1)+1;
											value=seq_no_merge+"";
										}
										else {
											value="1";
										}
										rset4.close();
										stmt4.close();
										
									
										if(merge_seq_no.containsKey(abbr)) {
											value = (merge_seq_no.get(abbr)+1)+"";
											seq_no_merge=Integer.parseInt(value);
											merge_seq_no.put(abbr, Integer.parseInt(value));
										}
										else {
											merge_seq_no.put(abbr, Integer.parseInt(value));
										}
										
//										cell.setCellValue("'"+value+"'");
										str += value + ",";
											
									}
								else if(i==3)
								{
//									cell.setCellValue("'"+abbr+"-S-"+seq_no_merge+"'");
									str += abbr+"-S-"+seq_no_merge + ",";
								}
								
								else  if (i == 7) {	// GUARANTOR_CD
									if (mpe.counterparty_map.containsKey(value)) {
										value =mpe.counterparty_map.get(value); 
								    }
									if(value.contains("RIL"))
									{
										value="RIL";
									}
//									cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
								else if(i == 8 && !value.equals("null")) {	// Currency
									
									value = value.equals("INR") ? "1" : "2";
//									cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
								else if (!value.equals("null") && (i == 12 || i == 14 || i == 16)) {
								
									queryString4 = "SELECT BANK_NAME FROM FMS7_BANK_MST WHERE BANK_CD = ? ";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, value);
									rset4 = stmt4.executeQuery();
									if(rset4.next()) {
//										cell.setCellValue("'"+rset4.getString(1)+"'");
										str += rset4.getString(1).replaceAll(",", "@@") + ",";
									}
									else {
//										cell.setCellValue("'"+value+"'");
										str += value + ",";
									}
									rset4.close();
									stmt4.close();
								}
								else if (i == 25 && !value.equals("null")) {	// Status
									if(value.equals("Pending")) {
										value = "P";
									}
									else if(value.equals("In order")) {
										value = "O";
									}
									else if(value.equals("Cancelled")) {
										value = "C";
									}
									else if(value.equals("Pending for amendment")) {
										value = "A";
									}
									else if(value.equals("Restated")) {
										value = "R";
									}
									else if(value.equals("Dummy")) {
										value = "D";
									}
									else if(value.equals("Expired")) {
										value = "E";
									}
//									cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
								else {
//									cell.setCellValue("'"+value+"'");
									str += value + ",";
								}
							}
							count++;
							logger.insert_data(fname_csv, str, conn);
							logger.data(fname, (cd+","+seq_no+","+"1"+","+rset1.getString(3)+","+"K"+","+"S"+","), conn, "");
						}
						rset2.close();
						stmt2.close();
					}
					rset1.close();
					stmt1.close();
				}
				rset.close();
				stmt.close();
				
				
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<LOG_FMS_SECURITY_MST>>,", conn);
						
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<LOG_FMS_SECURITY_MST>>");
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


public void FMS_SECURITY_FILE_DTL() throws SQLException, IOException {
	
	function_nm = "FMS_SECURITY_FILE_DTL()";
	
	try {
		
		System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
		logger.checkpoint(fname, "<<START>>,<<FMS_SECURITY_FILE_DTL>>,,,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
		
		columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEQ_REV_NO,GX,FILE_TYPE,SEC_INT_REF,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT";
		count = 0;
		
		String invoice_no= "",inv_dt="",ent_by="",ent_dt="",pdf_type="",file_nm="",adv_cont_type="";
		String o_by= "",o_dt="",d_by="",d_dt="",t_by="",t_dt="",cont_rev="",adv_cont_no="",agmt_rev="";
		String fname_csv = "", str = "", state_tin="",new_inv="",sug_inv_dt="",end_dt="",flag="",customer_cd="",fin_yr="";
		int inv_seq;
		
		fname_csv = migration_setup_dir + "EXPORT/FMS_SECURITY_FILE_DTL_" + start_end_dt + ".csv";
		
		FileWriter fw = new FileWriter(fname_csv, false); 
		fw.close();
		
		// Inserting Column Names
		for (int i = 0; i < columns.split(",").length; i++) {
			str += columns.split(",")[i] + ",";
		}
		logger.insert_data(fname_csv, str, conn);
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
		
		queryString = "SELECT DISTINCT(A.CUSTOMER_CD), A.EFF_DT, A.CUSTOMER_ABBR  FROM FMS7_CUSTOMER_MST A "
				+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_MST C WHERE A.CUSTOMER_CD = C.CUSTOMER_CD) "
				+ "ORDER BY A.CUSTOMER_CD, A.EFF_DT DESC  ";
		stmt = conn.prepareStatement(queryString);
		rset = stmt.executeQuery();
		while(rset.next())
		{
			cd=rset.getString(1);
			
				queryString2 = "SELECT CONTRACT_TYPE, HLPL_INV_SEQ_NO, FINANCIAL_YEAR,"
						+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), NEW_INV_SEQ_NO, SUP_STATE_CODE+1, "
						+ "PRINT_BY_ORI, TO_CHAR(PRINT_DT_ORI, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "PRINT_BY_DUP, TO_CHAR(PRINT_DT_DUP, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "PRINT_BY_TRI, TO_CHAR(PRINT_DT_TRI, 'DD/MM/YYYY HH24:MI:SS'), "
						+ "FGSA_NO, CUSTOMER_CD, FLAG, TO_CHAR(INVOICE_DT,'dd-Mon-yy'), "
						+ "TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),FGSA_NO,FGSA_REV_NO,SN_NO,SN_REV_NO "
						+ "FROM FMS7_INVOICE_MST WHERE CUSTOMER_CD = ? AND CONTRACT_TYPE = 'C' AND FLAG = 'A' ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, cd);
				rset2 = stmt2.executeQuery();
				
			while(rset2.next()) 
			{
				cont_type = rset2.getString(1);
				inv_seq = rset2.getInt(2);
				fin_yr = rset2.getString(3);
				inv_dt = rset2.getString(4);
				inv_dt = inv_dt.replaceAll("/", "");
				new_inv = rset2.getString(5);
				state_tin = rset2.getString(6);
				o_by = rset2.getString(7);
				o_dt = rset2.getString(8);
				d_by = rset2.getString(9);
				d_dt = rset2.getString(10);
				t_by = rset2.getString(11);
				t_dt = rset2.getString(12);
				abbr = rset.getString(3);
				cont_no = rset2.getString(13);
				customer_cd = rset2.getString(14);
				flag = rset2.getString(15);
				sug_inv_dt=rset2.getString(16);
				end_dt = rset2.getString(17);
				end_dt = end_dt.replaceAll("/", "");
				agmt_no = rset2.getString(18);			
				agmt_rev = rset2.getString(19);			
				adv_cont_no = rset2.getString(20);			
				cont_rev = rset2.getString(21);			
				
				int index=0;
				queryString1 = "SELECT '2', NULL , NULL, NULL, 'K', 'PDF', NULL, PDF_INV_NM, INV_TYPE, NULL,NULL,NULL, PDF_SIGNED_FLAG, SIGNED_BY, "
						+ "TO_CHAR(SIGNED_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL "
						+ " FROM FMS8_INV_PDF_DTL "
						+ " WHERE PDF_INV_NM LIKE ? AND PDF_INV_NM LIKE ? AND PDF_INV_NM LIKE ? AND INV_TYPE = 'O'"; 
				
				stmt1 = conn.prepareStatement(queryString1);
				
				stmt1.setString(++index,"ADV%-"+cont_type+"-"+inv_seq);
				stmt1.setString(++index,"%-"+inv_dt+"-%");
				stmt1.setString(++index,"%-"+abbr+"-%");
					
				rset1 = stmt1.executeQuery();
				
				while(rset1.next())
				{	
					pdf_type = rset1.getString(9);
					file_nm = rset1.getString(6);
					
					str = "";
					
					if (mpe.counterparty_map.containsKey(abbr)) {
						abbr =mpe.counterparty_map.get(abbr); 
					}
					
//					if(cont_type.equals("C")) {
						queryString3 = "SELECT CN_TERM FROM FMS8_LNG_REGAS_MST WHERE CUSTOMER_CD = ? AND CN_NO = ? ";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, customer_cd);
						stmt3.setString(2, adv_cont_no);
						rset3 = stmt3.executeQuery();
						if(rset3.next()) {
							cont_type = rset3.getString(1);
						}
						stmt3.close();
						rset3.close();
//					}
					if(cont_type.equals("C")) {
						adv_cont_type = "O";
					}else {
						adv_cont_type = "Q";
					}
					str += abbr+"@"+adv_cont_type + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
						
					 
					 if (i == 1) {	//FINANCIAL_YEAR
						value = fin_yr;
						str += value + ",";
					 }
					 else if(i == 2) {	//INVOICE_NO
						 
						 if(cont_type.equals("A")) {
								cont_ref = "A"+"-"+customer_cd+"-"+agmt_no+"-"+adv_cont_no+"-"+cont_rev;
								value = cont_ref;
							}
							else if(cont_type.equals("C")) {
								cont_ref = "C"+"-"+customer_cd+"-"+agmt_no+"-"+adv_cont_no+"-"+cont_rev;
								value = cont_ref;
							}
							str += value + ",";
						 
//						 str += inv_seq+"@"+new_inv + ",";
					 }
					 else if(i == 3) {	//INVOICE_SEQ
							str += new_inv + ",";
					 }
					 else if(i == 6) {	//SEC_INT_REF
							str += new_inv + ",";
					 }
					 else if (i == 7) {
							value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
							
								if(value.startsWith("ADV")) {
									cont_desc = "Advance_";
								}
//								file1 = new File(pdf_path+"security/adv_invoice/" + cont_desc + value);
								
								if(rset1.getString(13)!=null && rset1.getString(13).equals("Y")) {
									file_nm = cont_desc+value+"-O.pdf";
								}else {
									file_nm = rset1.getString(8)+"-O.pdf";
								}
							str += file_nm + ",";
						 }
					 else if(i == 8) {	//ENT_BY
						if(pdf_type.equals("O")) {
							value = o_by;
					 }
						
					 else if(pdf_type.equals("D")) {
							value = d_by;
					 }
						
					 else if(pdf_type.equals("T")) {
							value = t_by;
					 }
						str += value + ",";
					 }
					
					else if(i == 9) {	//ENT_DT
						if(pdf_type.equals("O")) {
							value = o_dt;
						}
						
						else if(pdf_type.equals("D")) {
							value = d_dt;
						}
						
						else if(pdf_type.equals("T")) {
							value = t_dt;
						}
						str += value + ",";
					}
					
					else if(i == 13) {
						value = rset1.getString(14);
						str += value + ",";
					}
						
						else {
							value = rset1.getString(i+1) == null ? "null" : rset1.getString(i+1).replaceAll(",", " ");
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
		
		logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
		logger.checkpoint(fname, "<<END>>,<<FMS_SECURITY_FILE_DTL>>,,,,", conn);
		
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
	
	public void setPdf_path(String path) {
		pdf_path = path;
	}

}
