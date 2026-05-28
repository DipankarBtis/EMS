
package com.etrm.fms.migration;

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.logging.Level;

//import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.io.FileWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.SystemErrorLogger;
public class Transport_SEIPL_Data_Extractor {

	String db_src_file_name = "Transport_SEIPL_Data_Extractor.java";
	String function_nm = "";
	
	String migration_setup_dir = "";
	
	String sysDateTime = "";
	
	String fname = "";
	String fname_error = "";
	
	String fname1 = "";

	DataMigration_Logger logger = new DataMigration_Logger();
	Migration_Plants_Exceptions mpe=new Migration_Plants_Exceptions();
    DateUtil date = new DateUtil();
	String queryString = "", queryString1 = "", queryString2 = "", queryString3 = "",queryString5 = "",queryString4 = "",queryString6 = "";
	Connection conn;
	ResultSet rset, rset1,rset2,rset3,rset4,rset5,rset6;
	PreparedStatement stmt, stmt1,stmt2,stmt3,stmt4,stmt5,stmt6;

	String dbline = "", username = "", encrypted = "", password = "";
	String columns = "", filename = "", value = "";
	
	String checked_values = "", msg = "", msg_type = "", abbr="",ent_pt="",abbr1="",map="",p_seq_no="",map_id="",trans_rate="",pos_imb_rate="",neg_imb_rate="";
	
	String unauth_overrun="",ship_or_pay="",trans_map="",cont_map="";
	String agmt_no="",agmt_rev="",cont_no="",cont_rev="",cont_type="",p_seq="";
	final String company_cd = "2";
	String cd = "", eff_dt = "", agmt_type = "",map1="";
	
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
			
			fname = "DataLogs/Extractor/Transport_SEIPL_Data_Extractor(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Extractor/Transport_SEIPL_Data_Extractor_Error(log)"+sysDateTime+".csv";
			
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
				
				if (checked_values.contains("FMS_GTA_AGMT_MST")) {
					FMS_GTA_AGMT_MST();
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
				}
				if (checked_values.contains("FMS_GTA_PG_INV_MST")) {
					FMS_GTA_PG_INV_MST();
				}
				if (checked_values.contains("FMS_GTA_FFLOW_INV_MST")) {
					FMS_GTA_FFLOW_INV_MST();
				}
				if (checked_values.contains("FMS_GTA_FFLOW_INV_DTL")) {
					FMS_GTA_FFLOW_INV_DTL();
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
	
	public void FMS_GTA_AGMT_MST() throws SQLException, IOException {
		function_nm = "FMS_GTA_AGMT_MST()";
		
		try {

			System.out.println("<<START>><<FMS_GTA_AGMT_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_GTA_AGMT_MST>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,START_DT,END_DT,STATUS,TOT_TRANS_QTY,UNIT_CD,CALC_BASE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,AGMT_NAME,AGMT_TYPE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			ncell = 0;
			count = 0;
			String qty = "",cal_base = "",agmt_name="";
			//String arr[] = {"98,4","106,124"};
			int agmt_rev=0;
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			logger.checkpoint(fname, "COMPANY_CD,ABBR,AGMT_NAME,AGMT_TYPE,TIMESTAMP,", conn);
//			for(int j = 0;j<arr.length;j++) {
//				nrow++;
			//COUNTERPARTY
			queryString2 = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' AND A.COUNTERPARTY_CD IN ( 4, 43, 97, 124, 263, 298 ) ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
//			for(int i = 0;i<arr[j].split(",").length;i++) {
//				queryString2 += "?,";
//			}
//			queryString2 = queryString2.substring(0, queryString2.length()-1);
			stmt2 = conn.prepareStatement(queryString2);
//			for (int i = 0; i < arr[j].split(",").length;i++) {
//				stmt2.setString(index++, arr[j].split(",")[i]);
//			}
			rset2 = stmt2.executeQuery();
			
			while (rset2.next()) {
				
			queryString = " SELECT '2', PARTY_CD, AGREEMENT_NO, '0', TO_CHAR(ST_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(END_DT, 'DD/MM/YYYY HH24:MI:SS'),"
					+ " STATUS, TOT_TRANS_QTY, UNIT_CD, QTY_BASE_FLAG, '1', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, NULL, AGMT_TYPE FROM FMS_AGREEMENT_MST A, FMS7_TRANSPORTER_MST B "
					+  "WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, rset2.getString(2));
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			
			
			
			while (rset.next()) {
				row = spreadsheet.createRow(nrow++);
				ncell = 0;
				abbr = rset2.getString(1);
				
				cd = rset.getString(2);
				agmt_rev = rset.getInt(4);
				qty = rset.getString(9);
				cal_base = rset.getString(10);
				agmt_type = rset.getString(16);
//				row = spreadsheet.createRow(nrow++);
				value = "";

				if (mpe.transporter_map.containsKey(abbr)) {
			    	 abbr =mpe.transporter_map.get(abbr); 
			    }
					
				cell = row.createCell(ncell++);
				cell.setCellValue("'"+abbr+"'");
				
				for (int i = 1; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
					value = value.trim().equals("") ? "null" : value;
					
					
//					if(i == 1) {	//COUNTERPARTY_CD
//						queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
//						stmt1 = conn.prepareStatement(queryString1);
//						stmt1.setString(1, cd);
//						rset1 = stmt1.executeQuery();
//						if(rset1.next()) {
//							cd = rset1.getString(1);
//						}
//						rset1.close();
//						stmt1.close();
//						value = cd;
//					}
					if(i == 1) {
						value = abbr;
					}
					else if(i == 3) {	//AGMT_REV
						if(agmt_rev == 0) {
							value = agmt_rev+"";
						}
					}
					else if(i == 6) {	//STATUS
						if(value.equals("1")) {
							value = "Y";
						}
						else {
							value = "N";
						}
					}
//					else if (i == 7) {	// TOT_TRANS_QTY
//						tot_trans_qty += rset.getInt(8);
//						value = tot_trans_qty + "";
//					}
					else if(i == 8) {	//UNIT_CD
						queryString1 = "SELECT UNIT_ABR FROM FMS_UNIT_MST WHERE UNIT_CD = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, qty);
						rset1 = stmt1.executeQuery();
						if(rset1.next()) {
							qty = rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						value = qty;
					}
					else if(i == 9) {	//CALC_BASE
						if (cal_base!=null) {
							if (cal_base.equals("GHV")) {
								cal_base = "GCV";
							} else if (cal_base.equals("NHV")) {
								cal_base = "NCV";
							} 
						}else {
							cal_base = "GCV";
						}
						value = cal_base;
					}
					else if(i == 14) {	//AGMT_NAME
						if(cd != null) {
							agmt_name = "SEIPL-"+abbr+"-GTA"+rset.getString(3)+"-REV0";
						}
						value = agmt_name;
					}
					else if(i == 15) {
						if(agmt_type.startsWith("G")) {
							value = "G";
						}else if(agmt_type.startsWith("P")) {
							value = "P";
						}
					} 
					
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (company_cd + "," + abbr + "," + agmt_name + "," + agmt_type + "," ), conn, "");

			}

			stmt.close();
			rset.close();
			}
			rset2.close();
			stmt2.close();
//			}
			filename = migration_setup_dir + "EXPORT/FMS_GTA_AGMT_MST_"+start_end_dt+".xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_AGMT_MST>>,,", conn);
			

			logger.checkpoint1(fname1,count+",", conn);

			System.out.println("<<END>><<FMS_GTA_AGMT_MST>>");
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

	
	public void FMS_GTA_ENTRY_POINT() throws SQLException, IOException {
		function_nm = "FMS_GTA_ENTRY_POINT()";
		
		try {
			
			System.out.println("<<START>><<FMS_GTA_ENTRY_POINT>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_GTA_ENTRY_POINT>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,TRANSPORTER_CD,PLANT_SEQ,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,AGMT_TYPE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			ncell = 0;
			count = 0;
			String ent_pt = "",org_abbr= "";
			int agmt_rev=0;
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			logger.checkpoint(fname, "COMPANY_CD,ABBR,TRANSPORTER_CD,ENT_PT,AGMT_TYPE,TIMESTAMP,", conn);
			//COUNTERPARTY
			queryString2 = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt2 = conn.prepareStatement(queryString2);
			rset2 = stmt2.executeQuery();
			
			while (rset2.next()) {
				
				
				queryString = " SELECT '2', PARTY_CD, AGREEMENT_NO, '0', ENTRY_PT_CD, NULL, '1',TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, 'G' "
						+ "FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND CONT_AGR_TYPE IN('FGSA','Tender','Trans')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, rset2.getString(2));
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
				rset = stmt.executeQuery();
				
			
				
				while (rset.next()) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					abbr = rset2.getString(1);
					
					cd = rset.getString(2);
					agmt_rev = rset.getInt(4);
					
					agmt_type = rset.getString(11);
//				row = spreadsheet.createRow(nrow++);
					value = "";
					
					ent_pt = rset.getString(5);
					if(ent_pt.length()>3) {
						ent_pt = rset.getString(5).substring(rset.getString(5).length()-2);
					}
					else {
						ent_pt = rset.getString(5).substring(rset.getString(5).length()-1);
					}
					
					if(ent_pt.startsWith("0")) {
						ent_pt = ent_pt.substring(ent_pt.length()-1);
					}
					
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
				    	 abbr =mpe.transporter_map.get(abbr); 
				    }
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
						value = value.trim().equals("") ? "null" : value;
						
						
//					if(i == 1) {	//COUNTERPARTY_CD
//						queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
//						stmt1 = conn.prepareStatement(queryString1);
//						stmt1.setString(1, cd);
//						rset1 = stmt1.executeQuery();
//						if(rset1.next()) {
//							cd = rset1.getString(1);
//						}
//						rset1.close();
//						stmt1.close();
//						value = cd;
//					}
						if(i == 1) {	//
							value = abbr;
						}
						else if(i == 3) {	//AGMT_REV
							if(agmt_rev == 0) {
								value = agmt_rev+"";
							}
						}
						else if(i == 4) {	//TRANSPORTER_CD
							value = abbr;
						}
						else if(i == 5) {	//PLANT_SEQ_NO
							queryString1 = "SELECT PLANT_SHORT_ABBR FROM FMS7_TRANSPORTER_PLANT_DTL WHERE TRANSPORTER_CD = ? AND SEQ_NO = ? ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, rset.getString(2));
							stmt1.setString(2, ent_pt);
							rset1 = stmt1.executeQuery();
							if(rset1.next()) {
								ent_pt = rset1.getString(1);
								if (mpe.meter_map.containsKey(ent_pt)) 
								{
									ent_pt = mpe.meter_map.get(ent_pt);
								}
							}
							
							else  {
						    	 ent_pt = org_abbr; 
						    }
							value = ent_pt;
							rset1.close();
							stmt1.close();
						}
						
						cell.setCellValue("'" + value + "'");
					}
					count++;
				logger.data(fname, (company_cd + "," + abbr + "," + cd + "," + ent_pt + "," + agmt_type + "," ), conn, "");
					
				}
				
				stmt.close();
				rset.close();
				
				
				
				//Parking
				queryString = " SELECT '2', PARTY_CD, AGREEMENT_NO, '0', ENTRY_PT_CD, NULL, '1',TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, 'P' "
						+ "FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND CONT_AGR_TYPE IN('Parking')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, rset2.getString(2));
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
				rset = stmt.executeQuery();
				
			
				
				while (rset.next()) {
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					abbr = rset2.getString(1);
					
					cd = rset.getString(2);
					agmt_rev = rset.getInt(4);
					agmt_type = rset.getString(11);
//				row = spreadsheet.createRow(nrow++);
					value = "";
					
					
					ent_pt = rset.getString(5);
					if(ent_pt.length()>3) {
						ent_pt = rset.getString(5).substring(rset.getString(5).length()-2);
					}
					else {
						ent_pt = rset.getString(5).substring(rset.getString(5).length()-1);
					}
					
					if(ent_pt.startsWith("0")) {
						ent_pt = ent_pt.substring(ent_pt.length()-1);
					}
					
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
				    	 abbr =mpe.transporter_map.get(abbr); 
				    }
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
						value = value.trim().equals("") ? "null" : value;
						
						
//					if(i == 1) {	//COUNTERPARTY_CD
//						queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
//						stmt1 = conn.prepareStatement(queryString1);
//						stmt1.setString(1, cd);
//						rset1 = stmt1.executeQuery();
//						if(rset1.next()) {
//							cd = rset1.getString(1);
//						}
//						rset1.close();
//						stmt1.close();
//						value = cd;
//					}
						if(i == 1) {	//
							value = abbr;
						}
						else if(i == 3) {	//AGMT_REV
							if(agmt_rev == 0) {
								value = agmt_rev+"";
							}
						}
						else if(i == 4) {	//TRANSPORTER_CD
							value = abbr;
						}
						else if(i == 5) {	//PLANT_SEQ_NO
							queryString1 = "SELECT PLANT_SHORT_ABBR FROM FMS7_TRANSPORTER_PLANT_DTL WHERE TRANSPORTER_CD = ? AND SEQ_NO = ? ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, rset.getString(2));
							stmt1.setString(2, ent_pt);
							rset1 = stmt1.executeQuery();
							if(rset1.next()) {
								ent_pt = rset1.getString(1);
								if (mpe.meter_map.containsKey(ent_pt)) 
								{
									ent_pt = mpe.meter_map.get(ent_pt);
								}
							}
							
							else  {
						    	 ent_pt = org_abbr; 
						    }
							value = ent_pt;
							rset1.close();
							stmt1.close();
						}
						
						cell.setCellValue("'" + value + "'");
					}
					count++;
				logger.data(fname, (company_cd + "," + abbr + "," + cd + "," + ent_pt + "," + agmt_type + "," ), conn, "");
					
				}
				
				stmt.close();
				rset.close();
			
			}
			rset2.close();
			stmt2.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_GTA_ENTRY_POINT_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_ENTRY_POINT>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_GTA_ENTRY_POINT>>");
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
	
	//FMS_GTA_EXIT_POINT
	public void FMS_GTA_EXIT_POINT() throws SQLException, IOException {
			function_nm = "FMS_GTA_EXIT_POINT()";
			
			try {
				
				System.out.println("<<START>><<FMS_GTA_EXIT_POINT>>");
				logger.checkpoint(fname, "<<START>>,<<FMS_GTA_EXIT_POINT>>,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
				
				columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,ENTITY,ENTITY_CD,PLANT_SEQ,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,AGMT_TYPE";
				
				workbook = new XSSFWorkbook();
				spreadsheet = workbook.createSheet("Sheet 1");
				
				nrow = 0;
				ncell = 0;
				count = 0;
				String ext_pt = "",org_abbr= "",abbr_c="",org_abbr_c="",cont_cust_cd="",p_abbr ="";
				int agmt_rev=0;
				// Below block of code is for inserting columns
				row = spreadsheet.createRow(nrow++);
				
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					cell.setCellValue(columns.split(",")[i]);
				}
				logger.checkpoint(fname, "COMPANY_CD,ABBR,CUSTOMER_CD,CUSTOMER_ABBR,EXIT_PT,TIMESTAMP,", conn);
				//COUNTERPARTY
				queryString2 = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
						+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
						+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
				stmt2 = conn.prepareStatement(queryString2);
				rset2 = stmt2.executeQuery();

				while (rset2.next()) {
					
					
					queryString = " SELECT '2', PARTY_CD, AGREEMENT_NO, '0', EXIT_PT_CD, CONT_CUST_CD, NULL, '1',TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, 'G' "
							+ "FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND CONT_AGR_TYPE IN('FGSA','Tender','Trans')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, rset2.getString(2));
//				stmt.setString(1, delta_FromDt);
//				stmt.setString(2, delta_FromDt);
//				stmt.setString(3, delta_ToDt);
//				stmt.setString(4, delta_ToDt);
					rset = stmt.executeQuery();
					
				
					
					while (rset.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						abbr = rset2.getString(1);
						
						cd = rset.getString(2);
						agmt_rev = rset.getInt(4);
						ext_pt = rset.getString(5);
						cont_cust_cd = rset.getString(6);
						
						agmt_type = rset.getString(12);
//					row = spreadsheet.createRow(nrow++);
						value = "";
						
						if (mpe.transporter_map.containsKey(abbr)) {
							org_abbr= abbr;
							abbr =mpe.transporter_map.get(abbr); 
						}
						if (mpe.meter_map.containsKey(org_abbr)) 
						{
							org_abbr = mpe.meter_map.get(org_abbr);
						}
//						if(mpe.counterparty_map.containsKey(abbr)) {
//							abbr_c = mpe.counterparty_map.get(abbr);
//						}
						cell = row.createCell(ncell++);
						cell.setCellValue("'"+abbr+"'");
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
							value = value.trim().equals("") ? "null" : value;
							
							
							if(i == 1) {	//
								value = abbr;
							}
							else if(i == 3) {	//AGMT_REV
								if(agmt_rev == 0) {
									value = agmt_rev+"";
								}
							}
							else if(i == 4) {	//ENTITY
								if(rset.getInt(5) < 10000){
								queryString1 = "SELECT CUSTOMER_CD FROM FMS_DELV_MST WHERE DELV_PT_CD = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, ext_pt);
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									cd = rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								
								queryString1 = "SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, cd);
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									abbr_c = rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								value = "C";
								}
								else {
									queryString1 = "SELECT CUSTOMER_CD FROM FMS_DELV_MST WHERE DELV_PT_CD = ? ";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1, ext_pt);
									rset1 = stmt1.executeQuery();
									if(rset1.next()) {
										cd = rset1.getString(1);
									}
									rset1.close();
									stmt1.close();
									
									queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ?  ";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1, cd);
									rset1 = stmt1.executeQuery();
									if(rset1.next()) {
										abbr_c = rset1.getString(1);
									}
									rset1.close();
									stmt1.close();
									value = "R";
									
								}
							}
							else if(i == 5) {	//ENTITY_CD
								if(mpe.counterparty_map.containsKey(abbr_c)) {
									value = mpe.counterparty_map.get(abbr_c);
								}
								else {
									value = abbr_c;
								}
							}
							
	                        else if(i == 6) {
							if(rset.getInt(5) < 10000){
								if(mpe.customer_map.containsKey(abbr_c+"-"+ext_pt.substring(ext_pt.length()-1))) {
									org_abbr_c = mpe.customer_map.get(abbr_c+"-"+ext_pt.substring(ext_pt.length()-1)).split("-")[0];
								}
								else {
									org_abbr_c = ext_pt.substring(ext_pt.length()-1);
								}
							queryString1 = "SELECT PLANT_SHORT_ABBR FROM FMS7_CUSTOMER_PLANT_DTL WHERE CUSTOMER_CD = ? AND SEQ_NO = ? ORDER BY EFF_DT DESC";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, cd);
							stmt1.setString(2, org_abbr_c);
							rset1 = stmt1.executeQuery();
							if(rset1.next()) {
								ext_pt = rset1.getString(1);
							}
							
							else  {
								ext_pt = " "; 
							}
							value = ext_pt;
							rset1.close();
							stmt1.close();
							}
							
							else{
//								queryString1 = "SELECT A.PLANT_SHORT_ABBR FROM FMS7_TRANSPORTER_PLANT_DTL A, FMS_DELV_MST B WHERE A.TRANSPORTER_CD = B.CUSTOMER_CD AND B.DELV_PT_CD = ? ";
								queryString1 = "SELECT PLANT_SHORT_ABBR FROM FMS7_TRANSPORTER_PLANT_DTL  WHERE TRANSPORTER_CD = ? AND SEQ_NO = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, cd);
								stmt1.setString(2, ext_pt.substring(ext_pt.length()-1));
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									ext_pt = rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								value = ext_pt;
							}
							
						}

							cell.setCellValue("'" + value + "'");
						}
						count++;
					logger.data(fname, (company_cd + "," + abbr + "," + cd + "," + abbr_c + "," + ext_pt + ","  + agmt_type + "," ), conn, "");
						
					}
					
					stmt.close();
					rset.close();
					
					
					//Parking
					
					queryString = " SELECT '2', PARTY_CD, AGREEMENT_NO, '0', EXIT_PT_CD, NULL, NULL, '1',TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, 'P' "
							+ "FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND CONT_AGR_TYPE IN('Parking')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, rset2.getString(2));
					rset = stmt.executeQuery();
					
				
					
					while (rset.next()) {
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						abbr = rset2.getString(1);
						
						cd = rset.getString(2);
						agmt_rev = rset.getInt(4);
						ext_pt = rset.getString(5);
						agmt_type = rset.getString(12);
//					row = spreadsheet.createRow(nrow++);
						value = "";
						
						if (mpe.transporter_map.containsKey(abbr)) {
							org_abbr= abbr;
							abbr =mpe.transporter_map.get(abbr); 
						}
						if (mpe.meter_map.containsKey(org_abbr)) 
						{
							org_abbr = mpe.meter_map.get(org_abbr);
						}
//						if(mpe.counterparty_map.containsKey(abbr)) {
//							abbr_c = mpe.counterparty_map.get(abbr);
//						}
						cell = row.createCell(ncell++);
						cell.setCellValue("'"+abbr+"'");
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = (rset.getString(i + 1) == null || rset.getString(i + 1).equals("0")) ? "null" : rset.getString(i + 1).trim().replaceAll("'", "");
							value = value.trim().equals("") ? "null" : value;
							
							if(i == 1) {	//
								value = abbr;
							}
							else if(i == 3) {	//AGMT_REV
								if(agmt_rev == 0) {
									value = agmt_rev+"";
								}
							}
							else if(i == 4) {	//ENTITY
								
								queryString3 = "SELECT A.TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST A, FMS_DELV_MST B WHERE A.TRANSPORTER_CD = B.CUSTOMER_CD AND B.DELV_PT_CD = ? ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, ext_pt);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									abbr_c = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
								
								value = "R";
							}
							else if(i == 5) {	//ENTITY_CD
								if(mpe.counterparty_map.containsKey(abbr_c)) {
									value = mpe.counterparty_map.get(abbr_c);
								}
								else {
									value = abbr_c;
								}
							}
							else if(i == 6) {	//PLANT_SEQ
								
								
								
								if(mpe.customer_map.containsKey(abbr_c+"-"+ext_pt.substring(ext_pt.length()-1))) {
									org_abbr_c = mpe.customer_map.get(abbr_c+"-"+ext_pt.substring(ext_pt.length()-1)).split("-")[0];
								}
								else {
									org_abbr_c = ext_pt.substring(ext_pt.length()-1);
								}
								queryString1 = "SELECT PLANT_SHORT_ABBR FROM FMS7_TRANSPORTER_PLANT_DTL WHERE TRANSPORTER_CD = ? AND SEQ_NO = ? ORDER BY EFF_DT DESC";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, cd);
								stmt1.setString(2, org_abbr_c);
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									ext_pt = rset1.getString(1);
								}
								
								else  {
									ext_pt = " "; 
								}
								value = ext_pt;
								rset1.close();
								stmt1.close();
							}
							
							cell.setCellValue("'" + value + "'");
						}
						count++;
					logger.data(fname, (company_cd + "," + abbr + "," + cd + "," + abbr_c + "," + ext_pt + ","  + agmt_type + "," ), conn, "");
						
					}
					
					stmt.close();
					rset.close();
				
				}
				rset2.close();
				stmt2.close();
				
				filename = migration_setup_dir + "EXPORT/FMS_GTA_EXIT_POINT_"+start_end_dt+".xlsx";
				
				fileOut = new FileOutputStream(filename);
				
				workbook.write(fileOut);
				fileOut.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_GTA_EXIT_POINT>>,,", conn);
				
				
				logger.checkpoint1(fname1,count+",", conn);
				
				System.out.println("<<END>><<FMS_GTA_EXIT_POINT>>");
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
	
	public void FMS_GTA_AGMT_BILLING_DTL() throws SQLException, IOException {
		function_nm = "FMS_GTA_AGMT_BILLING_DTL()";
		
		try {
			
			System.out.println("<<START>><<FMS_GTA_AGMT_BILLING_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_GTA_AGMT_BILLING_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,"
					+ "INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,"
					+ "DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE";
			
			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			ncell = 0;
			count = 0;
			String ent_pt = "",org_abbr= "";
			//String arr[] = {"98,4","106,124"};
			int agmt_rev=0;
			String map_id="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
			logger.checkpoint(fname, "COMPANY_CD,ABBR,CD,ENT_PT,AGMT_TYPE,TIMESTAMP,", conn);
//			for(int j = 0;j<arr.length;j++) {
//				nrow++;
			//COUNTERPARTY
//			queryString2 = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
//					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
//					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
//			stmt2 = conn.prepareStatement(queryString2);
//			rset2 = stmt2.executeQuery();
//			
//			while (rset2.next()) {
				
				
				queryString = " SELECT DISTINCT(A.ENTRY_PT_CD), A.PARTY_CD, A.CONT_AGR_TYPE "
						+ "FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD  AND A.CONT_AGR_TYPE IN('FGSA','Tender','Trans')";
				stmt = conn.prepareStatement(queryString);
//				stmt.setString(1, rset2.getString(2));
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
				rset = stmt.executeQuery();
				
			
				
				while (rset.next()) {
//					System.out.println("in rset loop>>>");
					
					
					cd = rset.getString(2);
					agmt_no = "1";
					agmt_type = rset.getString(3);
					
					map = cd+"-"+agmt_no; 
					
					queryString3 = "SELECT '2', MAPPING_ID, '1', '0', 'G', MODE_CD, 'B', PAY_DUE_PERIOD, '1', INV_RAISED_IN, INV_PAY_MODE, INT_RATE_CD, INT_PAY_MODE, "
							+ "INT_PAY_RATE, NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, 'C', 'N', NULL, NULL, NULL, NULL, NULL "
							+ "FROM FMS_CONT_SECU_PAY_INV WHERE MAPPING_ID LIKE ?  ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, map+"%");
					rset3 = stmt3.executeQuery();
					
					if(rset3.next()){
						
//						System.out.println("in rset3 loop>>>");	
						
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
//					abbr = rset2.getString(1);
					
//					cd = rset.getString(2);
					map = rset3.getString(2);
					agmt_rev = rset3.getInt(4);
					ent_pt = rset.getString(1);
//					agmt_type = rset3.getString(5);
//				row = spreadsheet.createRow(nrow++);
					value = "";
					
					ent_pt = rset.getString(1);
					if(ent_pt.length()>3) {
						ent_pt = rset.getString(1).substring(rset.getString(1).length()-2);
					}
					else {
						ent_pt = rset.getString(1).substring(rset.getString(1).length()-1);
					}
					
					if(ent_pt.startsWith("0")) {
						ent_pt = ent_pt.substring(ent_pt.length()-1);
					}
					
					queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, cd);
					rset1 = stmt1.executeQuery();
					if(rset1.next()) {
						abbr = rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
				    	 abbr =mpe.transporter_map.get(abbr); 
				    }
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = (rset3.getString(i + 1) == null || rset3.getString(i + 1).equals("0")) ? "null" : rset3.getString(i + 1).trim().replaceAll("'", "");
						value = value.trim().equals("") ? "null" : value;
						
						
//					if(i == 1) {	//COUNTERPARTY_CD
//						queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
//						stmt1 = conn.prepareStatement(queryString1);
//						stmt1.setString(1, cd);
//						rset1 = stmt1.executeQuery();
//						if(rset1.next()) {
//							cd = rset1.getString(1);
//						}
//						rset1.close();
//						stmt1.close();
//						value = cd;
//					}
						if(i == 2) {	//AGMT_NO
							value = agmt_no;
						}
						
						else if(i == 3) {	//AGMT_REV
							if(agmt_rev == 0) {
								value = agmt_rev+"";
							}
						}
						
						else if(i == 5) {	//BILLING_FREQ
							queryString1 = "SELECT MODE_NM FROM FMS_MODE_MST WHERE MODE_CD = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, value);
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									value = rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								
								if(value.equals("Fortnightly")) {
									value = "F";
								}
								
						}
						
						else if(i == 9) {	//INVOICE_CUR_CD
							if(value.equals("INR"))
							{
								value = "1";
							}
							else {
								value = "2";
							}
						}
						
						else if(i == 10) {	//PAYMENT_CUR_CD
							if(value.equals("INR"))
							{
								value = "1";
							}
							else {
								value = "2";
							}
						}
						
						else if(i == 11) { 	// INT_CAL_RATE_CD
							queryString1 = "SELECT INT_RATE_NM FROM FMS_CONT_INT_RATE_MST WHERE INT_RATE_CD = ? ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, value);
							rset1 = stmt1.executeQuery();
							if(rset1.next()) {
								value = rset1.getString(1);
							}
							rset1.close();
							stmt1.close();
						}
						
						
						else if(i == 28) {	//PLANT_SEQ_NO
							queryString1 = "SELECT PLANT_SHORT_ABBR FROM FMS7_TRANSPORTER_PLANT_DTL WHERE TRANSPORTER_CD = ? AND SEQ_NO = ? ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, rset.getString(2));
							stmt1.setString(2, ent_pt);
							rset1 = stmt1.executeQuery();
							if(rset1.next()) {
								ent_pt = rset1.getString(1);
								if (mpe.meter_map.containsKey(ent_pt)) 
								{
									ent_pt = mpe.meter_map.get(ent_pt);
								}
							}
							
							else  {
						    	 ent_pt = org_abbr; 
						    }
							value = ent_pt;
							map_id = map_id + ent_pt + ",";
							rset1.close();
							stmt1.close();
						}
						
						cell.setCellValue("'" + value + "'");
					}
					count++;
				logger.data(fname, (company_cd + "," + abbr + "," + cd + "," + ent_pt + "," + agmt_type + "," ), conn, "");
					
				}
				rset3.close();
				stmt3.close();
				
				}
				
				stmt.close();
				rset.close();
				
				
				//Parking
				queryString = " SELECT DISTINCT(A.ENTRY_PT_CD), A.PARTY_CD, A.CONT_AGR_TYPE "
						+ "FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD  AND A.CONT_AGR_TYPE IN('Parking')";
				stmt = conn.prepareStatement(queryString);
//				stmt.setString(1, rset2.getString(2));
//			stmt.setString(1, delta_FromDt);
//			stmt.setString(2, delta_FromDt);
//			stmt.setString(3, delta_ToDt);
//			stmt.setString(4, delta_ToDt);
				rset = stmt.executeQuery();
				
				
				
				while (rset.next()) {
//					System.out.println("in rset loop>>>");
					
					
					cd = rset.getString(2);
					agmt_no = "1";
					agmt_type = rset.getString(3);
					
					
					map = cd+"-"+agmt_no; 
					
					queryString3 = "SELECT '2', MAPPING_ID, '1', '0', 'P', MODE_CD, 'B', PAY_DUE_PERIOD, '1', INV_RAISED_IN, INV_PAY_MODE, INT_RATE_CD, INT_PAY_MODE, "
							+ "INT_PAY_RATE, NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, 'C', 'N', NULL, NULL, NULL, NULL, NULL "
							+ "FROM FMS_CONT_SECU_PAY_INV WHERE MAPPING_ID LIKE ? AND CONT_AGR_TYPE = ? ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, map+"%");
					stmt3.setString(2, agmt_type);
					rset3 = stmt3.executeQuery();
					
					if(rset3.next()){
						
//						System.out.println("in rset3 loop>>>");	
						
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
//					abbr = rset2.getString(1);
						
//					cd = rset.getString(2);
						map = rset3.getString(2);
						agmt_rev = rset3.getInt(4);
						ent_pt = rset.getString(1);
//						agmt_type = rset3.getString(5);
//				row = spreadsheet.createRow(nrow++);
						value = "";
						
						ent_pt = rset.getString(1);
						if(ent_pt.length()>3) {
							ent_pt = rset.getString(1).substring(rset.getString(1).length()-2);
						}
						else {
							ent_pt = rset.getString(1).substring(rset.getString(1).length()-1);
						}
						
						if(ent_pt.startsWith("0")) {
							ent_pt = ent_pt.substring(ent_pt.length()-1);
						}
						
						queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, cd);
						rset1 = stmt1.executeQuery();
						if(rset1.next()) {
							abbr = rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						if (mpe.transporter_map.containsKey(abbr)) {
							org_abbr= abbr;
							abbr =mpe.transporter_map.get(abbr); 
						}
						if (mpe.meter_map.containsKey(org_abbr)) 
						{
							org_abbr = mpe.meter_map.get(org_abbr);
						}
						cell = row.createCell(ncell++);
						cell.setCellValue("'"+abbr+"'");
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = (rset3.getString(i + 1) == null || rset3.getString(i + 1).equals("0")) ? "null" : rset3.getString(i + 1).trim().replaceAll("'", "");
							value = value.trim().equals("") ? "null" : value;
							
							
//					if(i == 1) {	//COUNTERPARTY_CD
//						queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
//						stmt1 = conn.prepareStatement(queryString1);
//						stmt1.setString(1, cd);
//						rset1 = stmt1.executeQuery();
//						if(rset1.next()) {
//							cd = rset1.getString(1);
//						}
//						rset1.close();
//						stmt1.close();
//						value = cd;
//					}
							if(i == 2) {	//AGMT_NO
								value = agmt_no;
							}
							
							else if(i == 3) {	//AGMT_REV
								if(agmt_rev == 0) {
									value = agmt_rev+"";
								}
							}
							
							else if(i == 5) {	//BILLING_FREQ
								queryString1 = "SELECT MODE_NM FROM FMS_MODE_MST WHERE MODE_CD = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, value);
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									value = rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								
								if(value.equals("Fortnightly")) {
									value = "F";
								}
								
							}
							
							else if(i == 9) {	//INVOICE_CUR_CD
								if(value.equals("INR"))
								{
									value = "1";
								}
								else {
									value = "2";
								}
							}
							
							else if(i == 10) {	//PAYMENT_CUR_CD
								if(value.equals("INR"))
								{
									value = "1";
								}
								else {
									value = "2";
								}
							}
							
							else if(i == 11) { 	// INT_CAL_RATE_CD
								queryString1 = "SELECT INT_RATE_NM FROM FMS_CONT_INT_RATE_MST WHERE INT_RATE_CD = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, value);
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									value = rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
							}
							
							
							else if(i == 28) {	//PLANT_SEQ_NO
								queryString1 = "SELECT PLANT_SHORT_ABBR FROM FMS7_TRANSPORTER_PLANT_DTL WHERE TRANSPORTER_CD = ? AND SEQ_NO = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, rset.getString(2));
								stmt1.setString(2, ent_pt);
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									ent_pt = rset1.getString(1);
									if (mpe.meter_map.containsKey(ent_pt)) 
									{
										ent_pt = mpe.meter_map.get(ent_pt);
									}
								}
								
								else  {
									ent_pt = org_abbr; 
								}
								value = ent_pt;
								map_id = map_id + ent_pt + ",";
								rset1.close();
								stmt1.close();
							}
							
							cell.setCellValue("'" + value + "'");
						}
						count++;
						logger.data(fname, (company_cd + "," + abbr + "," + cd + "," + ent_pt + "," + agmt_type + "," ), conn, "");
						
					}
					rset3.close();
					stmt3.close();
					
				}
				
				stmt.close();
				rset.close();
				
				
				
				
				
			
//			}
//			rset2.close();
//			stmt2.close();
//			}
			filename = migration_setup_dir + "EXPORT/FMS_GTA_AGMT_BILLING_DTL_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);
			
			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_AGMT_BILLING_DTL>>,,", conn);
			
			
			logger.checkpoint1(fname1,count+",", conn);
			
			System.out.println("<<END>><<FMS_GTA_AGMT_BILLING_DTL>>");
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

	public void FMS_GTA_CONT_MST() throws SQLException, IOException {
		
		function_nm = "FMS_GTA_CONT_MST()";
		
		try {

			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_CONT_MST>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,CONT_REF_NO,CT_REF_NO,START_DT,END_DT,ENTRY_PT_MAPPING_ID,"
					+ "EXIT_PT_MAPPING_ID,MDQ,MDQ_UNIT,VARIABLE_MDQ,RATE_UNIT,TRANSPORT_RATE,POSITIVE_IMB_RATE,NEGETIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,SIP_PAY_RATE,"
					+ "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,CALC_BASE,GCV,NCV,SIP_PAY_FREQ,CT_SEQ_NO,SIP_PAY_PERCENT,AGMT_TYPE,PARKING_RATE,MAX_PARK_QTY,MAX_PARK_UNIT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");

			nrow = 0;
			ncell = 0;
			count = 0;
			String org_abbr= "",pay_freq,pay_rate,party_cd="";
			String mdq="",cus_cd="",cont_agr_no="",cont_agr_rev="",contract_no="",contract_rev="",cont_cust_cd="";
			int cont_no=1;
//			double i1=0;
//			double i2=0;
			BigDecimal i1 = BigDecimal.ZERO;
			BigDecimal i2 = BigDecimal.ZERO;
			
			row = spreadsheet.createRow(nrow++);
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}
//			cont_no = 1;

			logger.checkpoint(fname, "COUNTERPARTY_ABBR,PARTY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,AGMT_TYPE,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				//CUSTOMER
				
				queryString1 = "SELECT A.PARTY_CD, A.AGREEMENT_NO, '0', A.CONT_NO, '0', 'C', A.CONT_CUST_CD,A.MAPPING_ID,A.CONT_MAPPING_ID,TO_CHAR(A.CONT_ST_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_CHAR(A.CONT_END_DT, 'DD/MM/YYYY HH24:MI:SS'),A.ENTRY_PT_CD,A.EXIT_PT_CD,A.TOT_ENE,'1',NULL,'1',A.MAPPING_ID,NULL,NULL,NULL,NULL,'1',"
						+ "TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.ENE_BASE,A.GHV,A.NHV,'M',NULL,'90', 'G', NULL, NULL, NULL FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B "
						+ "WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND A.CONT_AGR_TYPE IN('FGSA','Tender') ORDER BY A.CONT_END_DT DESC";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					//ent_pt = rset1.getString(12);
					map_id = rset1.getString(18);
					abbr1 = abbr;
					pay_freq = rset1.getString(30);
					pay_rate = rset1.getString(32);
					party_cd = rset1.getString(1);
					agmt_no = rset1.getString(2);
					agmt_rev = rset1.getString(3);
//					cont_no = rset1.getString(2);
					cont_rev = rset1.getString(5);
					cont_cust_cd = rset1.getString(7);
					
					ent_pt = rset1.getString(12);
					
					if(ent_pt.length()>3) {
						ent_pt = rset1.getString(12).substring(rset1.getString(12).length()-2);
					}
					else {
						ent_pt = rset1.getString(12).substring(rset1.getString(12).length()-1);
					}
					
					if(ent_pt.startsWith("0")) {
						ent_pt = ent_pt.substring(ent_pt.length()-1);
					}
					
//					System.out.println(":cust_cd: "+cont_cust_cd);
//					System.out.println("length:: "+cont_cust_cd.length());
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
				    	 abbr =mpe.transporter_map.get(abbr); 
				    }
					
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					
					
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i==4) {
							cell.setCellValue("'"+cont_no+"'");
							cont_no++;
						}
						else if(i==8) {
							
							trans_map = rset1.getString(8);
							cont_map = rset1.getString(9);
							if(cont_map!=null) {
								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
							}
							else {
								value = rset1.getString(2)+"-"+rset1.getString(4)+"-"+rset1.getString(12)+"-"+rset1.getString(13);
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i==9) {
							value = null;
							cell.setCellValue("'"+value+"'");
						} 
						else if(i == 12) {
							queryString2 = "SELECT PLANT_SHORT_ABBR FROM FMS7_TRANSPORTER_PLANT_DTL WHERE TRANSPORTER_CD = ? AND SEQ_NO = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(1));
							stmt2.setString(2, ent_pt);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								ent_pt = rset2.getString(1);
							}
							
							else  {
						    	 ent_pt = org_abbr; 
						    }
							
							rset2.close();
							stmt2.close();
							
							value = abbr+"-"+ent_pt;
							
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 13) {
							
							abbr1 = rset1.getString(13);
							if(rset1.getInt(13) < 10000){
								queryString2 = "SELECT A.CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST A, FMS_DELV_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.DELV_PT_CD = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, abbr1);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									abbr1 = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								if (mpe.counterparty_map.containsKey(abbr1)) {
									abbr1 =mpe.counterparty_map.get(abbr1); 
							    }
	
//								p_seq_no = rset1.getString(13).substring(rset1.getString(13).length()-1);
								p_seq_no = rset1.getString(13);
								p_seq_no = p_seq_no.substring(cont_cust_cd.length());

								map=abbr1+"-"+p_seq_no;
								if(mpe.customer_map.containsKey(map))
								{
									p_seq_no=mpe.customer_map.get(map);
								}
	
								p_seq_no=p_seq_no.split("-")[0];
								value  = abbr1+"-"+p_seq_no;
								cell.setCellValue("'"+value+"'");
							}
							
							else{
								queryString2 = "SELECT A.TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST A, FMS_DELV_MST B WHERE A.TRANSPORTER_CD = B.CUSTOMER_CD AND B.DELV_PT_CD = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, abbr1);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									abbr1 = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								p_seq_no = rset1.getString(13).substring(rset1.getString(13).length()-1);
								value=abbr1+"-"+p_seq_no;
								cell.setCellValue("'"+value+"'");
							}
							
							
						}
						
						else if(i == 14) {	//MDQ
							if(cont_map!=null && cont_map.contains("-")) {
								cus_cd = cont_map.split("-")[1];
								cont_agr_no = cont_map.split("-")[2];
								cont_agr_rev = cont_map.split("-")[3];
								contract_no = cont_map.split("-")[4];
								contract_rev = cont_map.split("-")[5];
								
								queryString2 = "SELECT A.ENE FROM FMS_MDQ_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_REV_NO = ? "
										+ "AND A.CONTRACT_NO = ? AND A.CONTRACT_REV_NO = ? AND A.CONT_AGR_TYPE IN('FGSA','Tender') "
										+ "AND A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS_MDQ_DTL C WHERE A.MAPPING_ID = C.MAPPING_ID AND A.CONT_AGR_NO = C.CONT_AGR_NO AND "
										+ "A.CONT_AGR_REV_NO = C.CONT_AGR_REV_NO AND A.CONT_CUST_CD = C.CONT_CUST_CD AND A.CONTRACT_NO = C.CONTRACT_NO "
										+ "AND A.CONTRACT_REV_NO = C.CONTRACT_REV_NO AND A.CONT_AGR_TYPE IN('FGSA','Tender'))";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, trans_map);
								stmt2.setString(2, cont_agr_no);
								stmt2.setString(3, cus_cd);
								stmt2.setString(4, cont_agr_rev);
								stmt2.setString(5, contract_no);
								stmt2.setString(6, contract_rev);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									mdq = rset2.getString(1);
								}
								else {
									mdq = rset1.getString(14);
								}
								rset2.close();
								stmt2.close();
							}
							else {
								mdq = rset1.getString(14);
							}
							value = mdq;
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 18) { //Transmission rate 
							if(cont_map!=null && cont_map.contains("-")) {
								cus_cd = cont_map.split("-")[1];
								cont_agr_no = cont_map.split("-")[2];
								cont_agr_rev = cont_map.split("-")[3];
								contract_no = cont_map.split("-")[4];
								contract_rev = cont_map.split("-")[5];
							
							queryString2 = "SELECT A.PRICE_RATE FROM FMS_CONT_PRICE_DTL A WHERE A.MAPPING_ID = ? AND A.PRICE_CD = '16' AND A.CONT_AGR_TYPE IN('FGSA','Tender') "
							+ "AND A.EFF_DATE = (SELECT MAX(B.EFF_DATE) FROM FMS_CONT_PRICE_DTL B WHERE A.MAPPING_ID = B.MAPPING_ID AND A.CONT_CUST_CD = B.CONT_CUST_CD AND A.CONT_CUST_CD = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? AND CONTRACT_REV_NO = ?)";
//							queryString2 = "SELECT A.PRICE_RATE FROM FMS_CONT_PRICE_DTL A WHERE A.MAPPING_ID = ? AND A.PRICE_CD = '16' AND A.CONT_CUST_CD = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? AND CONTRACT_REV_NO = ?"
//									+ "AND A.CONT_AGR_TYPE IN('FGSA','Tender') AND A.EFF_DATE IN (SELECT B.CONT_ST_DT FROM FMS_CONT_MST B WHERE B.MAPPING_ID = A.MAPPING_ID AND B.CONT_CUST_CD = A.CONT_CUST_CD )";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, map_id);
							stmt2.setString(2, cus_cd);
							stmt2.setString(3, cont_agr_no);
							stmt2.setString(4, cont_agr_rev);
							stmt2.setString(5, contract_no);
							stmt2.setString(6, contract_rev);
//							
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								i1 = rset2.getBigDecimal(1);
								
							}
							else {
								i1 = new BigDecimal(0);
							}
							rset2.close();
							stmt2.close();
							}else {
								queryString2 = "SELECT A.PRICE_RATE FROM FMS_CONT_PRICE_DTL A WHERE A.MAPPING_ID = ? AND A.PRICE_CD = '16' AND A.CONT_AGR_TYPE IN('FGSA','Tender') "
								+ "AND A.EFF_DATE = (SELECT MAX(B.EFF_DATE) FROM FMS_CONT_PRICE_DTL B WHERE A.MAPPING_ID = B.MAPPING_ID AND A.CONT_CUST_CD = B.CONT_CUST_CD )";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, map_id);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									i1 =  rset2.getBigDecimal(1);
								}
								else {
									i1 = BigDecimal.ZERO;
								}
								rset2.close();
								stmt2.close();
							}
//								if(i1!=0) {
							if (i1 != null && i1.compareTo(BigDecimal.ZERO) != 0) {
								trans_rate = i1+"";
								}
								else {
								trans_rate = null;
								}
							cell.setCellValue("'"+trans_rate+"'");
							
						}
						else if(i == 19) { //Positive_imb_rate
							
							if (i1 != null && i1.compareTo(BigDecimal.ZERO) != 0) {
//								i2 = i1 / 2;
								i2 = i1.divide(BigDecimal.valueOf(2));

							}
							else {
								i2 = BigDecimal.ZERO;
							}
								if(i2 != null &&i2.compareTo(BigDecimal.ZERO) != 0) {
								pos_imb_rate = i2+"";
								}
								else {
								pos_imb_rate = null;
								}
							cell.setCellValue("'"+pos_imb_rate+"'");
							
						}
						else if(i == 20) { //Negative_imb_rate
//								if(i2!=0.0) {
							if(i2 != null && i2.compareTo(BigDecimal.ZERO) != 0) {
								neg_imb_rate = i2+"";
								}
								else {
								neg_imb_rate = null;
								}
							cell.setCellValue("'"+neg_imb_rate+"'");
							
						}
						else if(i == 21) { //Unauth_overrun_rate

							if(i2 != null && i2.compareTo(BigDecimal.ZERO) != 0) {
								unauth_overrun = i2+"";
								}
								else {
								unauth_overrun = null;
								}
							cell.setCellValue("'"+unauth_overrun+"'");
							
						}
						else if(i == 22) { //Ship_or_pay
							if(i2 != null && i2.compareTo(BigDecimal.ZERO) != 0) {
								ship_or_pay = i1+"";
								}
								else {
								ship_or_pay = null;
								}
							cell.setCellValue("'"+ship_or_pay+"'");
							
						}
						else if(i==27) {
							value = rset1.getString(27);
							if(value.equals("GHV")) {
								value = "GCV";
							}else if(value.equals("NHV")) {
								value = "NCV";
							}
							cell.setCellValue("'"+value+"'");

						}
						else if(i == 30) {	//SIP_PAY_FREQ
							if(abbr.toUpperCase().equals("GSPL")) {
								pay_freq = "D";
							}
							else if(abbr.toUpperCase().equals("GSINLIM")) {
								pay_freq = "D";
							}
							else if(abbr.toUpperCase().equals("GSIGLIM")) {
								pay_freq = "D";
							}
							else {
								pay_freq = "M";
							}
							value = pay_freq;
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 32) {	//SIP_PAY_PERCENT
							if(abbr.toUpperCase().equals("GSPL")) {
								pay_rate = "100";
							}
							else if(abbr.toUpperCase().equals("GSINLIM")) {
								pay_rate = "100";
							}
							else if(abbr.toUpperCase().equals("GSIGLIM")) {
								pay_rate = "100";
							}
							else {
								pay_rate = "90";
							}
							value = pay_rate;
							cell.setCellValue("'"+value+"'");
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
						
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + party_cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + ent_pt + "," + agmt_type + ","), conn, "");

				}
				rset1.close();
				stmt1.close();
				
				
				//TRANSPORTER
//				cont_no = 1;
				queryString1 = "SELECT A.PARTY_CD, A.AGREEMENT_NO, '0', A.CONT_NO, '0', NULL, NULL,A.MAPPING_ID,NULL,TO_CHAR(A.CONT_ST_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_CHAR(A.CONT_END_DT, 'DD/MM/YYYY HH24:MI:SS'),A.ENTRY_PT_CD,A.EXIT_PT_CD,A.TOT_ENE,'1',NULL,'1',A.CONT_CUST_CD,A.CONT_AGR_NO,"
						+ "A.CONT_AGR_REV_NO,A.CONTRACT_NO,A.CONTRACT_REV_NO,'1',"
						+ "TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.ENE_BASE,A.GHV,A.NHV,'M',NULL,'90', 'G', NULL, NULL, NULL FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B "
						+ "WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND A.CONT_AGR_TYPE = 'Trans' ORDER BY A.CONT_END_DT DESC";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					map_id = rset1.getString(8);
					cus_cd = rset1.getString(18);
					cont_agr_no = rset1.getString(19);
					cont_agr_rev = rset1.getString(20);
					contract_no = rset1.getString(21);
					contract_rev = rset1.getString(22);
					

					ent_pt = rset1.getString(12);
					if(ent_pt.length()>3) {
						ent_pt = rset1.getString(12).substring(rset1.getString(12).length()-2);
					}
					else {
						ent_pt = rset1.getString(12).substring(rset1.getString(12).length()-1);
					}
					
					if(ent_pt.startsWith("0")) {
						ent_pt = ent_pt.substring(ent_pt.length()-1);
					}
					
					
					
					abbr1 = abbr;
					pay_freq = rset1.getString(30);
					pay_rate = rset1.getString(32);
					party_cd = rset1.getString(1);
					agmt_no = rset1.getString(2);
					agmt_rev = rset1.getString(3);
//					cont_no = rset1.getString(2);
					cont_rev = rset1.getString(5);
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
				    	 abbr =mpe.transporter_map.get(abbr); 
				    }
					
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					
					
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i==4) {
							cell.setCellValue("'"+cont_no+"'");
							cont_no++;
						}
						
                         else if(i == 6) {
							abbr1 = rset1.getString(13);
							if(rset1.getInt(13) < 10000){
								queryString2 = "SELECT A.CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST A, FMS_DELV_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.DELV_PT_CD = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, abbr1);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									abbr1 = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								if (mpe.counterparty_map.containsKey(abbr1)) {
									abbr1 =mpe.counterparty_map.get(abbr1); 
							    }
	
								p_seq_no = rset1.getString(13);
								p_seq_no = p_seq_no.substring(cus_cd.length());
								map=abbr1+"-"+p_seq_no;
								if(mpe.customer_map.containsKey(map))
								{
									p_seq_no=mpe.customer_map.get(map);
								}
	
								p_seq_no=p_seq_no.split("-")[0];
								p_seq  = abbr1+"-"+p_seq_no;
								value = "C";
								cell.setCellValue("'"+value+"'");
							}
							
							else{
								queryString2 = "SELECT A.TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST A, FMS_DELV_MST B WHERE A.TRANSPORTER_CD = B.CUSTOMER_CD AND B.DELV_PT_CD = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, abbr1);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									abbr1 = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								
								p_seq_no = rset1.getString(13).substring(rset1.getString(13).length()-1);
								
								p_seq=abbr1+"-"+p_seq_no;
								value = "R";
								cell.setCellValue("'"+value+"'");
							}
							
							
						}
						
						else if(i==8) {
							trans_map = rset1.getString(8);
//							cont_map = rset1.getString(9);
//							if(cont_map!=null) {
//								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
//							}
//							else {
//								value = rset1.getString(2)+"-"+rset1.getString(4)+"-"+rset1.getString(12)+"-"+rset1.getString(13);
//							}
							cell.setCellValue("'"+trans_map+"'");
						}
						else if(i == 12) {
							int cnt = 0;
							queryString2 = "SELECT PLANT_SHORT_ABBR FROM FMS7_TRANSPORTER_PLANT_DTL WHERE TRANSPORTER_CD = ? AND SEQ_NO = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(++cnt, rset1.getString(1));
							
							
							
							stmt2.setString(++cnt, ent_pt);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								ent_pt = rset2.getString(1);
							}
							
							else  {
						    	 ent_pt = org_abbr; 
						    }
							
							rset2.close();
							stmt2.close();
							
							value = abbr+"-"+ent_pt;
							
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i==13) {
							value  = p_seq;
							cell.setCellValue("'"+value+"'");
						}
//						else if(i == 13) {
//							
//							abbr1 = rset1.getString(13);
//							if(rset1.getInt(13) < 10000){
//								queryString2 = "SELECT A.CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST A, FMS_DELV_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.DELV_PT_CD = ? ";
//								stmt2 = conn.prepareStatement(queryString2);
//								stmt2.setString(1, abbr1);
//								rset2 = stmt2.executeQuery();
//								if(rset2.next()) {
//									abbr1 = rset2.getString(1);
//								}
//								rset2.close();
//								stmt2.close();
//								if (mpe.counterparty_map.containsKey(abbr1)) {
//									abbr1 =mpe.counterparty_map.get(abbr1); 
//							    }
//	
//								p_seq_no = rset1.getString(13);
//								p_seq_no = p_seq_no.substring(cus_cd.length());
//								map=abbr1+"-"+p_seq_no;
//								if(mpe.customer_map.containsKey(map))
//								{
//									p_seq_no=mpe.customer_map.get(map);
//								}
//	
//								p_seq_no=p_seq_no.split("-")[0];
//								value  = abbr1+"-"+p_seq_no;
//								cell.setCellValue("'"+value+"'");
//							}
//							
//							else{
//								queryString2 = "SELECT A.TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST A, FMS_DELV_MST B WHERE A.TRANSPORTER_CD = B.CUSTOMER_CD AND B.DELV_PT_CD = ? ";
//								stmt2 = conn.prepareStatement(queryString2);
//								stmt2.setString(1, abbr1);
//								rset2 = stmt2.executeQuery();
//								if(rset2.next()) {
//									abbr1 = rset2.getString(1);
//								}
//								rset2.close();
//								stmt2.close();
//								p_seq_no = rset1.getString(13).substring(rset1.getString(13).length()-1);
//								value=abbr1+"-"+p_seq_no;
//								cell.setCellValue("'"+value+"'");
//							}
//							
//							
//						}

						else if(i == 14) {	//MDQ
							if(cus_cd!=null) {
								
								queryString2 = "SELECT A.ENE FROM FMS_MDQ_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_REV_NO = ? "
										+ "AND A.CONTRACT_NO = ? AND A.CONTRACT_REV_NO = ? AND A.CONT_AGR_TYPE IN('Trans') "
										+ "AND A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS_MDQ_DTL C WHERE A.MAPPING_ID = C.MAPPING_ID AND A.CONT_AGR_NO = C.CONT_AGR_NO AND "
										+ "A.CONT_AGR_REV_NO = C.CONT_AGR_REV_NO AND A.CONT_CUST_CD = C.CONT_CUST_CD AND A.CONTRACT_NO = C.CONTRACT_NO "
										+ "AND A.CONTRACT_REV_NO = C.CONTRACT_REV_NO AND A.CONT_AGR_TYPE IN('Trans'))";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, trans_map);
								stmt2.setString(2, cont_agr_no);
								stmt2.setString(3, cus_cd);
								stmt2.setString(4, cont_agr_rev);
								stmt2.setString(5, contract_no);
								stmt2.setString(6, contract_rev);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									mdq = rset2.getString(1);
								}
								else {
									mdq = rset1.getString(14);
								}
								rset2.close();
								stmt2.close();
							}
							else {
								mdq = rset1.getString(14);
							}
							value = mdq;
							cell.setCellValue("'"+value+"'");
						}
						
						else if(i == 18) { //Transmission rate 
//							queryString2 = "SELECT A.PRICE_RATE FROM FMS_CONT_PRICE_DTL A WHERE A.MAPPING_ID = ? AND A.PRICE_CD = '16' AND A.CONT_AGR_TYPE IN('Trans') "
//									+ "AND A.EFF_DATE = (SELECT MAX(B.EFF_DATE) FROM FMS_CONT_PRICE_DTL B WHERE A.MAPPING_ID = B.MAPPING_ID AND A.CONT_CUST_CD = B.CONT_CUST_CD )";
							
							queryString2 = "SELECT A.PRICE_RATE FROM FMS_CONT_PRICE_DTL A WHERE A.MAPPING_ID = ? AND A.PRICE_CD = '16' AND A.CONT_AGR_TYPE IN('Trans') "
									+ "AND A.EFF_DATE = (SELECT MAX(B.EFF_DATE) FROM FMS_CONT_PRICE_DTL B WHERE A.MAPPING_ID = B.MAPPING_ID AND A.CONT_CUST_CD = B.CONT_CUST_CD AND A.CONT_CUST_CD = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? AND CONTRACT_REV_NO = ?)";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, map_id);
							stmt2.setString(2, cus_cd);
							stmt2.setString(3, cont_agr_no);
							stmt2.setString(4, cont_agr_rev);
							stmt2.setString(5, contract_no);
							stmt2.setString(6, contract_rev);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								i1 = rset2.getBigDecimal(1);
								
							}
							else {
								i1 = BigDecimal.ZERO;
							}
							rset2.close();
							stmt2.close();
							if(i1 != null && i1.compareTo(BigDecimal.ZERO) != 0) {
								trans_rate = i1+"";
								}
								else {
								trans_rate = null;
								}
							cell.setCellValue("'"+trans_rate+"'");
							
						}
						else if(i == 19) { //Positive_imb_rate
							
							if(i1 != null && i1.compareTo(BigDecimal.ZERO) != 0) {
								i2 = i1.divide(BigDecimal.valueOf(2));
							}
							else {
								i2 = BigDecimal.ZERO;
							}
							if(i1 != null && i1.compareTo(BigDecimal.ZERO) != 0) {
								pos_imb_rate = i2+"";
								}
								else {
								pos_imb_rate = null;
								}
							cell.setCellValue("'"+pos_imb_rate+"'");
							
						}
						else if(i == 20) { //Negative_imb_rate
							if(i2 != null && i2.compareTo(BigDecimal.ZERO) != 0) {
								neg_imb_rate = i2+"";
								}
								else {
								neg_imb_rate = null;
								}
							cell.setCellValue("'"+neg_imb_rate+"'");
							
						}
						else if(i == 21) { //Unauth_overrun_rate
							if(i2 != null && i2.compareTo(BigDecimal.ZERO) != 0) {
								unauth_overrun = i2+"";
								}
								else {
								unauth_overrun = null;
								}
							cell.setCellValue("'"+unauth_overrun+"'");
							
						}
						else if(i == 22) { //Ship_or_pay
							if(i2 != null && i2.compareTo(BigDecimal.ZERO) != 0) {
								ship_or_pay = i1+"";
								}
								else {
								ship_or_pay = null;
								}
							cell.setCellValue("'"+ship_or_pay+"'");
							
						}
						else if(i==27) {
							value = rset1.getString(27);
							if(value.equals("GHV")) {
								value = "GCV";
							}else if(value.equals("NHV")) {
								value = "NCV";
							}
							cell.setCellValue("'"+value+"'");

						}
						else if(i == 30) {	//SIP_PAY_FREQ
							if(abbr.toUpperCase().equals("GSPL")) {
								pay_freq = "D";
							}
							else if(abbr.toUpperCase().equals("GSINLIM")) {
								pay_freq = "D";
							}
							else if(abbr.toUpperCase().equals("GSIGLIM")) {
								pay_freq = "D";
							}
							else {
								pay_freq = "M";
							}
							value = pay_freq;
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 32) {	//SIP_PAY_PERCENT
							if(abbr.toUpperCase().equals("GSPL")) {
								pay_rate = "100";
							}
							else if(abbr.toUpperCase().equals("GSINLIM")) {
								pay_rate = "100";
							}
							else if(abbr.toUpperCase().equals("GSIGLIM")) {
								pay_rate = "100";
							}
							else {
								pay_rate = "90";
							}
							value = pay_rate;
							cell.setCellValue("'"+value+"'");
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
						
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + party_cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + ent_pt + "," + agmt_type + ","), conn, "");

				}
				rset1.close();
				stmt1.close();
				
				
				//PARKING
//				cont_no = 1;
				queryString1 = "SELECT A.PARTY_CD, A.AGREEMENT_NO, '0', A.CONT_NO, '0', 'K', NULL,A.MAPPING_ID,NULL,TO_CHAR(A.CONT_ST_DT, 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_CHAR(A.CONT_END_DT, 'DD/MM/YYYY HH24:MI:SS'),A.ENTRY_PT_CD,A.EXIT_PT_CD,A.TOT_ENE,'1',NULL,'1',NULL,NULL,NULL,NULL,NULL,'1',"
						+ "TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.ENE_BASE,A.GHV,A.NHV,'M',NULL,'90', 'P', NULL, NULL, '1',"
						+ "A.CONT_CUST_CD,A.CONT_AGR_NO,A.CONT_AGR_REV_NO,A.CONTRACT_NO,A.CONTRACT_REV_NO FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B "
						+ "WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND A.CONT_AGR_TYPE = 'Parking' ORDER BY A.CONT_END_DT DESC";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					
					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					map_id = rset1.getString(8);
					abbr1 = abbr;
					pay_freq = rset1.getString(30);
					pay_rate = rset1.getString(32);
					party_cd = rset1.getString(1);
					agmt_no = rset1.getString(2);
					agmt_rev = rset1.getString(3);
//					cont_no = rset1.getString(2);
					cont_rev = rset1.getString(5);
					
					
					ent_pt = rset1.getString(12);
					if(ent_pt.length()>3) {
						ent_pt = rset1.getString(12).substring(rset1.getString(12).length()-2);
					}
					else {
						ent_pt = rset1.getString(12).substring(rset1.getString(12).length()-1);
					}
					
					if(ent_pt.startsWith("0")) {
						ent_pt = ent_pt.substring(ent_pt.length()-1);
					}
					
					cus_cd = rset1.getString(37);
					cont_agr_no = rset1.getString(38);
					cont_agr_rev = rset1.getString(39);
					contract_no = rset1.getString(40);
					contract_rev = rset1.getString(41);
					
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
				    	 abbr =mpe.transporter_map.get(abbr); 
				    }
					
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					
					
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i==4) {
							cell.setCellValue("'"+cont_no+"'");
							cont_no++;
						}
						else if(i==8) {
							trans_map = rset1.getString(8);
//							cont_map = rset1.getString(9);
//							if(cont_map!=null) {
//								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
//							}
//							else {
//								value = rset1.getString(2)+"-"+rset1.getString(4)+"-"+rset1.getString(12)+"-"+rset1.getString(13);
//							}
							cell.setCellValue("'"+trans_map+"'");
						}
						else if(i == 12) {
							queryString2 = "SELECT PLANT_SHORT_ABBR FROM FMS7_TRANSPORTER_PLANT_DTL WHERE TRANSPORTER_CD = ? AND SEQ_NO = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, rset1.getString(1));
							stmt2.setString(2, ent_pt);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								ent_pt = rset2.getString(1);
							}
							
							else  {
						    	 ent_pt = org_abbr; 
						    }
							
							rset2.close();
							stmt2.close();
							
							value = abbr+"-"+ent_pt;
							
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 13) {
							
							abbr1 = rset1.getString(13);
							if(rset1.getInt(13) < 10000){
								queryString2 = "SELECT A.CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST A, FMS_DELV_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.DELV_PT_CD = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, abbr1);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									abbr1 = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								if (mpe.counterparty_map.containsKey(abbr1)) {
									abbr1 =mpe.counterparty_map.get(abbr1); 
							    }
	
								p_seq_no = rset1.getString(13);
								p_seq_no = p_seq_no.substring(cus_cd.length());
								map=abbr1+"-"+p_seq_no;
								if(mpe.customer_map.containsKey(map))
								{
									p_seq_no=mpe.customer_map.get(map);
								}
	
								p_seq_no=p_seq_no.split("-")[0];
								value  = abbr1+"-"+p_seq_no;
								cell.setCellValue("'"+value+"'");
							}
							
							else{
								queryString2 = "SELECT A.TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST A, FMS_DELV_MST B WHERE A.TRANSPORTER_CD = B.CUSTOMER_CD AND B.DELV_PT_CD = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, abbr1);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									abbr1 = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								p_seq_no = rset1.getString(13).substring(rset1.getString(13).length()-1);
								value=abbr1+"-"+p_seq_no;
								cell.setCellValue("'"+value+"'");
							}
							
							
						}

						else if(i == 14) {	//MDQ
							if(cus_cd!=null) {
								
								queryString2 = "SELECT A.ENE FROM FMS_MDQ_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_REV_NO = ? "
										+ "AND A.CONTRACT_NO = ? AND A.CONTRACT_REV_NO = ? AND A.CONT_AGR_TYPE IN('Parking') "
										+ "AND A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS_MDQ_DTL C WHERE A.MAPPING_ID = C.MAPPING_ID AND A.CONT_AGR_NO = C.CONT_AGR_NO AND "
										+ "A.CONT_AGR_REV_NO = C.CONT_AGR_REV_NO AND A.CONT_CUST_CD = C.CONT_CUST_CD AND A.CONTRACT_NO = C.CONTRACT_NO "
										+ "AND A.CONTRACT_REV_NO = C.CONTRACT_REV_NO AND A.CONT_AGR_TYPE IN('Parking'))";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, map_id);
								stmt2.setString(2, cont_agr_no);
								stmt2.setString(3, cus_cd);
								stmt2.setString(4, cont_agr_rev);
								stmt2.setString(5, contract_no);
								stmt2.setString(6, contract_rev);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									mdq = rset2.getString(1);
								}
								else {
									mdq = rset1.getString(14);
								}
								rset2.close();
								stmt2.close();
							}
							else {
								mdq = rset1.getString(14);
							}
							value = mdq;
							cell.setCellValue("'"+value+"'");
						}
						
						
//						else if(i == 19) { //Positive_imb_rate
//							
//							if(i1!=0) {
//								i2 = i1 / 2;
//							}
//							else {
//								i2 = 0;
//							}
//								if(i2!=0) {
//								pos_imb_rate = i2+"";}
//								else {
//								pos_imb_rate = null;
//								}
//							cell.setCellValue("'"+pos_imb_rate+"'");
//							
//						}
//						else if(i == 20) { //Negative_imb_rate
//								if(i2!=0) {
//								neg_imb_rate = i2+"";}
//								else {
//								neg_imb_rate = null;
//								}
//							cell.setCellValue("'"+neg_imb_rate+"'");
//							
//						}
//						else if(i == 21) { //Unauth_overrun_rate
//								if(i2!=0) {
//								unauth_overrun = i2+"";}
//								else {
//								unauth_overrun = null;
//								}
//							cell.setCellValue("'"+unauth_overrun+"'");
//							
//						}
//						else if(i == 22) { //Ship_or_pay
//								if(i2!=0) {
//								ship_or_pay = i1+"";}
//								else {
//								ship_or_pay = null;
//								}
//							cell.setCellValue("'"+ship_or_pay+"'");
//							
//						}
						else if(i==27) {
							value = rset1.getString(27);
							if(value.equals("GHV")) {
								value = "GCV";
							}else if(value.equals("NHV")) {
								value = "NCV";
							}
							cell.setCellValue("'"+value+"'");

						}
						else if(i == 30) {	//SIP_PAY_FREQ
							if(abbr.toUpperCase().equals("GSPL")) {
								pay_freq = "D";
							}
							else if(abbr.toUpperCase().equals("GSINLIM")) {
								pay_freq = "D";
							}
							else if(abbr.toUpperCase().equals("GSIGLIM")) {
								pay_freq = "D";
							}
							else {
								pay_freq = "M";
							}
							value = pay_freq;
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 32) {	//SIP_PAY_PERCENT
							if(abbr.toUpperCase().equals("GSPL")) {
								pay_rate = "100";
							}
							else if(abbr.toUpperCase().equals("GSINLIM")) {
								pay_rate = "100";
							}
							else if(abbr.toUpperCase().equals("GSIGLIM")) {
								pay_rate = "100";
							}
							else {
								pay_rate = "90";
							}
							value = pay_rate;
							cell.setCellValue("'"+value+"'");
						}
						else if(i == 34) { //parking_CHARGES
							queryString2 = "SELECT A.PRICE_RATE FROM FMS_CONT_PRICE_DTL A WHERE A.MAPPING_ID = ? AND A.PRICE_CD = '44' "
									+ "AND A.CONT_AGR_TYPE IN('Parking') AND A.EFF_DATE = (SELECT MAX(B.EFF_DATE) FROM FMS_CONT_PRICE_DTL B WHERE A.MAPPING_ID = B.MAPPING_ID AND A.CONT_CUST_CD = B.CONT_CUST_CD)";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, map_id);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								i1 = rset2.getBigDecimal(1);
							}
							else {
								i1 = BigDecimal.ZERO;
							}
							rset2.close();
							stmt2.close();
							if(i1 != null && i1.compareTo(BigDecimal.ZERO) != 0) {
									trans_rate = i1+"";
								}
								else {
									trans_rate = null;
								}
							cell.setCellValue("'"+trans_rate+"'");
							
						}
						else if(i == 35) {	//MDQ
							if(cus_cd!=null) {
								
								queryString2 = "SELECT A.ENE FROM FMS_MDQ_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_REV_NO = ? "
										+ "AND A.CONTRACT_NO = ? AND A.CONTRACT_REV_NO = ? AND A.CONT_AGR_TYPE IN('Parking') "
										+ "AND A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS_MDQ_DTL C WHERE A.MAPPING_ID = C.MAPPING_ID AND A.CONT_AGR_NO = C.CONT_AGR_NO AND "
										+ "A.CONT_AGR_REV_NO = C.CONT_AGR_REV_NO AND A.CONT_CUST_CD = C.CONT_CUST_CD AND A.CONTRACT_NO = C.CONTRACT_NO "
										+ "AND A.CONTRACT_REV_NO = C.CONTRACT_REV_NO AND A.CONT_AGR_TYPE IN('Parking'))";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, map_id);
								stmt2.setString(2, cont_agr_no);
								stmt2.setString(3, cus_cd);
								stmt2.setString(4, cont_agr_rev);
								stmt2.setString(5, contract_no);
								stmt2.setString(6, contract_rev);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									mdq = rset2.getString(1);
								}
								else {
									mdq = rset1.getString(14);
								}
								rset2.close();
								stmt2.close();
							}
							else {
								mdq = rset1.getString(14);
							}
							value = mdq;
							cell.setCellValue("'"+value+"'");
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
						
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + party_cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + ent_pt + "," + agmt_type + ","), conn, "");

				}
				rset1.close();
				stmt1.close();
			
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_GTA_CONT_MST_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_CONT_MST>>,,,", conn);
			

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
			logger.error(fname, e, function_nm, conn, fname_error);
		}
		
	}
	
	public void FMS_GTA_CONT_MAP() throws SQLException, IOException {
		
		function_nm = "FMS_GTA_CONT_MAP()";
		
		try {

			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_CONT_MAP>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CUSTOMER_CD,SELL_CONT_MAP,ENT_BY,ENT_DT,AGMT_TYPE,MAPED_ENTITY_TYPE";

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
			

			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,TRANSPORTER_MAP,CONT_MAP,AGMT_TYPE,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				queryString1 = "SELECT A.MAPPING_ID,A.CONT_MAPPING_ID,A.ENTRY_PT_CD,NULL,NULL,NULL,CONT_CUST_CD,NULL,'1',TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), 'G', 'C',"
						+ "A.CONT_AGR_TYPE "
						+ " FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND A.CONT_AGR_TYPE IN('FGSA','Tender') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {

					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					agmt_type = rset1.getString(11);


					if (mpe.transporter_map.containsKey(abbr)) {

				    	 abbr =mpe.transporter_map.get(abbr); 
				    }
					
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						
						if(i==1) {
							trans_map = rset1.getString(1);
							cont_map = rset1.getString(2);
							if(cont_map!=null) {
							value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
							}
							else {
								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5];
							}
							cell.setCellValue("'"+value+"'");
						}
						else if(i==2) {
							cont_map = rset1.getString(2);
							if(cont_map!=null) {
								if(cont_map.startsWith("S")) {
									value = cont_map.split("-")[0]+"-"+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5];
								}else if(cont_map.startsWith("L")) {
									value = "L-"+cont_map.split("-")[2]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5];
								}
								
							}else {
								if(rset1.getString(13).equals("FGSA")) {
								value= "S-0-0-0-0";
								}else {
									value = "L-0-0-0-0";
								}
							}
							cell.setCellValue("'"+value+"'");
							
						}
						else if(i==3) {
							value =null;
							cell.setCellValue("'"+value+"'");
						}
						else if(i==7) {
							queryString3="SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD=?";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, rset1.getString(7));
							rset3 = stmt3.executeQuery();
							   if(rset3.next()) {
								   abbr = rset3.getString(1);
								   if (mpe.counterparty_map.containsKey(abbr)) {
										abbr =mpe.counterparty_map.get(abbr); 
								   }
							   }else {
								   abbr = null;
							   }
							   stmt3.close();
							   rset3.close();
							    cell.setCellValue("'"+abbr+"'");
						}
						else if(i==8) {
							if(cont_map!=null) {
								 if(cont_map.startsWith("S")) {
									value = cont_map.split("-")[0]+"-"+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5];
								}
							}
								
						    cell.setCellValue("'"+value+"'");
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
						
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + trans_map + "," + cont_map + ","  + agmt_type + ","), conn, "");

				}
				rset1.close();
				stmt1.close();
				
				
				
				queryString1 = "SELECT A.MAPPING_ID,A.CONT_MAPPING_ID,A.ENTRY_PT_CD,NULL,NULL,NULL,CONT_CUST_CD,NULL,'1',TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), 'G', 'C',"
						+ "A.CONT_AGR_TYPE "
						+ " FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND A.CONT_AGR_TYPE IN('Trans') AND A.EXIT_PT_CD < 10000";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {

					row = spreadsheet.createRow(nrow++);
					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					agmt_type = rset1.getString(11);


					if (mpe.transporter_map.containsKey(abbr)) {

				    	 abbr =mpe.transporter_map.get(abbr); 
				    }
					
					cell = row.createCell(ncell++);
					cell.setCellValue("'"+abbr+"'");
					
					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						
						if(i==1) {
							trans_map = rset1.getString(1);
							
							cell.setCellValue("'"+trans_map+"'");
						}
						else if(i==2) {
							value = "S-0-0-0-0";
							cell.setCellValue("'"+value+"'");
							
						}
						else if(i==3) {
							value =null;
							cell.setCellValue("'"+value+"'");
						}
						else if(i==7) {
							queryString3="SELECT CUSTOMER_ABBR FROM FMS7_CUSTOMER_MST WHERE CUSTOMER_CD=?";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, rset1.getString(7));
							rset3 = stmt3.executeQuery();
							   if(rset3.next()) {
								   abbr = rset3.getString(1);
								   if (mpe.counterparty_map.containsKey(abbr)) {
										abbr =mpe.counterparty_map.get(abbr); 
								   }
							   }else {
								   abbr = null;
							   }
							   stmt3.close();
							   rset3.close();
							    cell.setCellValue("'"+abbr+"'");
						}
						else if(i==8) {
							value =null;
							cell.setCellValue("'"+value+"'");
						    //cell.setCellValue("'"+value+"'");
						}
						else {
							cell.setCellValue("'"+value+"'");
						}
						
					}
					count++;
					logger.data(fname, (abbr + "," + cd + "," + trans_map + "," + cont_map + ","  + agmt_type + ","), conn, "");

				}
				rset1.close();
				stmt1.close();
				
			}
			stmt.close();
			rset.close();
			
			filename = migration_setup_dir + "EXPORT/FMS_GTA_CONT_MAP_"+start_end_dt+".xlsx";
			
			fileOut = new FileOutputStream(filename);  
			
			workbook.write(fileOut);
			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_CONT_MAP>>,,,", conn);
			

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

	//FMS_GTA_BILLING_DTL
		public void FMS_GTA_BILLING_DTL() throws SQLException, IOException {
				function_nm = "FMS_GTA_BILLING_DTL()";
				
				try {
					
					System.out.println("<<START>><<FMS_GTA_BILLING_DTL>>");
					logger.checkpoint(fname, "<<START>>,<<FMS_GTA_BILLING_DTL>>,,,,,,", conn);
					
					logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
					
					columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,"
							+ "INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,"
							+ "DUE_DT_IN,EXCLUDE_SAT,EFF_DT,BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE,AGMT_TYPE";
					
					workbook = new XSSFWorkbook();
					spreadsheet = workbook.createSheet("Sheet 1");
					
					nrow = 0;
					ncell = 0;
					count = 0;
					String ent_pt = "",org_abbr= "",ext_pt="",rev="",flag="";
					String qty = "",cal_base = "",agmt_name="",trans_cd ="",cust_cd="",contract_no="";
					//String arr[] = {"98,4","106,124"};
					int agmt_rev=0;
					String map_id="",mod_cd="",int_rate="",int_per="",pay_per="",type="",agr_no="",name="";
					// Below block of code is for inserting columns
					row = spreadsheet.createRow(nrow++);
					
					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						cell.setCellValue(columns.split(",")[i]);
					}
					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,AGMT_TYPE,TIMESTAMP,", conn);
						
					
					queryString4 = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
							+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
							+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
					stmt4 = conn.prepareStatement(queryString4);
					rset4 = stmt4.executeQuery();
					
					while (rset4.next()) {
						
						queryString6 = "SELECT DISTINCT(A.ENTRY_PT_CD), A.PARTY_CD,A.CONT_NO,A.EXIT_PT_CD,A.MAPPING_ID, A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,"
								+ " A.CONT_CUST_CD,A.CONT_MAPPING_ID FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND A.CONT_AGR_TYPE IN ('FGSA','Tender') ";
						stmt6 = conn.prepareStatement(queryString6);
						stmt6.setString(1, rset4.getString(2));
						rset6 = stmt6.executeQuery();
						
						while(rset6.next()) {
						
							ent_pt=rset6.getString(1);
							cd = rset6.getString(2);
							cont_no = rset6.getString(3);
							ext_pt=rset6.getString(4);
							map_id = rset6.getString(5);
							agr_no = rset6.getString(6);
							rev = rset6.getString(7);
							contract_no = rset6.getString(8);
							cont_rev = rset6.getString(9);
							cont_type = rset6.getString(10);
							cust_cd = rset6.getString(11);
							cont_map = rset6.getString(12);
							agmt_no = "1";
							
							map = cd+"-"+agmt_no+"-"+cont_no+"-"+cd+"-"+ent_pt+"-"+ext_pt; 
//							System.out.println(map+","+cust_cd+","+agr_no+","+rev+","+contract_no+","+cont_rev+",");

//	                      data that exist in fms_cont_secu_pay_inv						
							queryString3 = "SELECT '2', MAPPING_ID, '1', MODE_CD, NULL, CONTRACT_REV_NO, CONT_AGR_TYPE,NULL, 'B', PAY_DUE_PERIOD, '1', INV_RAISED_IN, INV_PAY_MODE, INT_RATE_CD, INT_PAY_MODE, "
									+ "INT_PAY_RATE, NULL, NULL, NULL, NULL, NULL, TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), '1', NULL, NULL,'B', 'N', NULL, '15', NULL, NULL, NULL,NULL,NULL "
									+ "FROM FMS_CONT_SECU_PAY_INV WHERE MAPPING_ID = ? AND CONT_AGR_TYPE IN ('FGSA','Tender') ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, map);
							rset3 = stmt3.executeQuery();
							
							while(rset3.next()){
							row = spreadsheet.createRow(nrow++);
							ncell = 0;
							//id = rset3.getString(2);
							value = "";
							
							
							queryString = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							rset = stmt.executeQuery();
							if(rset.next()) {
								abbr = rset.getString(1);
							}
							rset.close();
							stmt.close();
							
							if (mpe.transporter_map.containsKey(abbr)) {
								org_abbr= abbr;
						    	 abbr =mpe.transporter_map.get(abbr); 
						    }
							if (mpe.meter_map.containsKey(org_abbr)) 
							{
								org_abbr = mpe.meter_map.get(org_abbr);
							}
							cell = row.createCell(ncell++);
							cell.setCellValue("'"+abbr+"'");
							
							for (int i = 1; i < columns.split(",").length; i++) {
								cell = row.createCell(i);
								value = (rset3.getString(i + 1) == null || rset3.getString(i + 1).equals("0")) ? "null" : rset3.getString(i + 1).trim().replaceAll("'", "");
								value = value.trim().equals("") ? "null" : value;
								
								if(i==1) {
//									cont_map = rset.getString(6);
									if(cont_map!=null) {
										value = map.split("-")[1]+"-"+map.split("-")[2]+"-"+map.split("-")[4]+"-"+map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
									}
									else {
										value = agmt_no+"-"+rset6.getString(3)+"-"+rset6.getString(1)+"-"+rset6.getString(4);								
									}
									cell.setCellValue("'"+value+"'");
								}
								if(i == 2) {	//AGMT_NO
									value = agmt_no;
								}
								
								else if(i == 3) {	//AGMT_REV
									value="0";
//									if(agmt_rev == 0) {
//										value = agmt_rev+"";
//									}
								}
								
								else if(i==5) {
									value=rset3.getString(6);
									if(value == null) {
										value = "0";
									}else {
										value = rset3.getString(6);
									}
								}
								
								else if(i==6) {
									value = rset3.getString(7);
									if(value.equals("FGSA") || value.equals("Tender")) {
										value="C";
									}
								}
								
								else if(i==7) {
									queryString1 = "SELECT PAY_DUE_PERIOD,MODE_CD,INT_RATE_CD FROM FMS_CONT_SECU_PAY_INV WHERE MAPPING_ID = ? AND CONT_CUST_CD = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? AND CONTRACT_REV_NO = ? "
											+ "AND CONT_AGR_TYPE IN ('FGSA','Tender')";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1, map_id);
									stmt1.setString(2, cust_cd);
									stmt1.setString(3, agr_no);
									stmt1.setString(4, rev);
									stmt1.setString(5, contract_no);
									stmt1.setString(6, cont_rev);
//									System.out.println(id+","+cust_cd+","+agr_no+","+rev+","+contract_no+","+cont_rev);
									rset1 = stmt1.executeQuery();

									if(rset1.next()) {
										pay_per=rset1.getString(1);
										mod_cd = rset1.getString(2);
										int_rate=rset1.getString(3);
									}
									rset1.close();
									stmt1.close();
									
									queryString2 = "SELECT MODE_NM FROM FMS_MODE_MST WHERE MODE_CD = ? ";
									stmt2 = conn.prepareStatement(queryString2);
									stmt2.setString(1, mod_cd);
									rset2 = stmt2.executeQuery();
									if(rset2.next()) {
										value = rset2.getString(1);
									}
									rset2.close();
									stmt2.close();
									
									if(value.equals("Fortnightly")) {
										value = "F";
									}else if(value.equals("Weekly")) {
										value="W";
									}else if(value.equals("Monthly")) {
										value="M";
									}else {
										value="O";
									}
									
								}
								
								else if(i==9) {
									value = pay_per;
									cell.setCellValue("'"+value+"'");
								}
								
								else if(i == 11) {	//INVOICE_CUR_CD
									if(value.equals("INR"))
									{
										value = "1";
									}
									else {
										value = "2";
									}
								}
								
								else if(i == 12) {	//PAYMENT_CUR_CD
									if(value.equals("INR"))
									{
										value = "1";
									}
									else {
										value = "2";
									}
								}
								
								else if(i == 13) { 	// INT_CAL_RATE_CD
									queryString1 = "SELECT INT_RATE_NM FROM FMS_CONT_INT_RATE_MST WHERE INT_RATE_CD = ? ";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1, int_rate);
									rset1 = stmt1.executeQuery();
									if(rset1.next()) {
										value = rset1.getString(1);
									}
									rset1.close();
									stmt1.close();
								}
								
								else if(i==15) {
									queryString1 = "SELECT INT_PAY_RATE FROM FMS_CONT_SECU_PAY_INV WHERE MAPPING_ID = ? AND CONT_CUST_CD = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? AND CONTRACT_REV_NO = ? ";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1, map_id);
									stmt1.setString(2, cust_cd);
									stmt1.setString(3, agr_no);
									stmt1.setString(4, rev);
									stmt1.setString(5, contract_no);
									stmt1.setString(6, cont_rev);
									rset1 = stmt1.executeQuery();
									if(rset1.next()) {
										value = rset1.getString(1);
									}
									rset1.close();
									stmt1.close();
								}
								else if(i==27) {
									queryString1 = "SELECT TO_CHAR(CONT_ST_DT, 'DD/MM/YYYY') FROM FMS_CONT_MST WHERE MAPPING_ID = ? ";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1, map_id);
									rset1 =stmt1.executeQuery();
									String eff_dt="";
									if(rset1.next()) {
										eff_dt = rset1.getString(1);
									}
									rset1.close();
									stmt1.close();
									value=eff_dt;
									
								}
								else if(i==33) {
									if(rset3.getString(7).equals("FGSA") || rset3.getString(7).equals("Tender")) {
										value="G";
									}
								}
								
								cell.setCellValue("'" + value + "'");
							}
							count++;
						logger.data(fname, (cd+","+abbr+","+agmt_no+","+"0"+","+cont_no+","+rset3.getString(6)+","+"C"+","+"1"+","+"G"+","), conn, "");
							
						}
						rset3.close();
						stmt3.close();
						
						
//						Billing details that do not exist in fms_cont_secu_pay_inv 
						if(cont_type.equals("FGSA")) {
							type = "S";
						}else if(cont_type.equals("Tender")){
							type = "L";
						}
						queryString5 = "SELECT '2',NULL,'1',NULL,A.SN_NO,A.SN_REV_NO,A.CONT_TYPE,A.BILLING_FREQ,A.FLAG,A.DUE_DATE,'1',A.INVOICE_CUR_CD,A.PAYMENT_CUR_CD,A.INT_CAL_RATE_CD,A.INT_CAL_SIGN,"
								+ "A.INT_CAL_PERCENTAGE,NULL,NULL,NULL,NULL,NULL,TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'),A.EMP_CD,NULL,NULL,'B','N',NULL,'15',NULL,NULL,NULL,NULL,NULL "
								+"FROM FMS7_SN_BILLING_DTL A WHERE A.FGSA_NO NOT IN (SELECT DISTINCT(B.CONT_AGR_NO) FROM FMS_CONT_SECU_PAY_INV B WHERE B.CONT_CUST_CD = A.CUSTOMER_CD AND "
								+ "B.CONT_AGR_NO = A.FGSA_NO AND B.CONT_AGR_REV_NO = A.FGSA_REV_NO AND B.CONTRACT_NO = A.SN_NO AND B.CONTRACT_REV_NO = A.SN_REV_NO AND A.CONT_TYPE = '"+cont_type+"') "
								+ "AND A.CUSTOMER_CD = ? AND A.FGSA_NO = ? AND A.FGSA_REV_NO = ? AND A.SN_NO = ? AND A.SN_REV_NO = ? AND A.CONT_TYPE = ? ";
						
						stmt5 = conn.prepareStatement(queryString5);
						stmt5.setString(1, cust_cd);
						stmt5.setString(2, agr_no);
						stmt5.setString(3, rev);
						stmt5.setString(4, contract_no);
						stmt5.setString(5, cont_rev);
						stmt5.setString(6, type);
//						System.out.println(cust_cd+","+no+","+rev+","+cont_no+","+cont_rev+","+type+",");
						rset5 = stmt5.executeQuery();
						while(rset5.next()) {
							row = spreadsheet.createRow(nrow++);
							ncell = 0;
							queryString = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							rset = stmt.executeQuery();
							if(rset.next()) {
								abbr = rset.getString(1);
							}
							rset.close();
							stmt.close();
							
							if (mpe.transporter_map.containsKey(abbr)) {
								org_abbr= abbr;
						    	 abbr =mpe.transporter_map.get(abbr); 
						    }
							if (mpe.meter_map.containsKey(org_abbr)) 
							{
								org_abbr = mpe.meter_map.get(org_abbr);
							}
							cell = row.createCell(ncell++);
							cell.setCellValue("'"+abbr+"'");
							
							for (int j = 1; j < columns.split(",").length; j++) {
								cell = row.createCell(j);
								value = (rset5.getString(j + 1) == null || rset5.getString(j + 1).equals("0")) ? "null" : rset5.getString(j + 1).trim().replaceAll("'", "");
								value = value.trim().equals("") ? "null" : value;
								
								if(j==1) {
									cont_map = rset6.getString(12);
									if(cont_map!=null) {
										value = map.split("-")[1]+"-"+map.split("-")[2]+"-"+map.split("-")[4]+"-"+map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
									}
									else {
										value = agmt_no+"-"+rset6.getString(3)+"-"+rset6.getString(1)+"-"+rset6.getString(4);								
									}
									cell.setCellValue("'"+value+"'");
								}
								else if(j == 3) {	//AGMT_REV
									value="0";
//									if(agmt_rev == 0) {
//										value = agmt_rev+"";
//									}
								}
								else if(j==6) {
									if(cont_type.equals("FGSA") || cont_type.equals("Tender")) {
										value = "C";
									}
									cell.setCellValue("'"+value+"'");
									
								}
								
								else if(j==7) {
									String freq ="";
									freq = rset5.getString(8);
									if(freq.equals("F")) {
										value = freq;
									}else {
										value = "O";
									}
									cell.setCellValue("'"+value+"'");
								}
								else if(j == 8) {
									flag = rset5.getString(9);
									if(flag.equals("T")) {
										flag = "B";
									}
									value = flag;
									cell.setCellValue("'"+value+"'");
								}
								
								
								else if(j==13) {
									String rate_cd="";
									rate_cd = rset5.getString(14);

										queryString2="SELECT INT_RATE_NM FROM FMS7_CONT_INT_RATE_MST WHERE INT_RATE_CD=?";
										stmt2 = conn.prepareStatement(queryString2);
										stmt2.setString(1, rate_cd);
										rset2 = stmt2.executeQuery();
										   if(rset2.next()) {	
											   name = rset2.getString(1);
											   name= name.toUpperCase(); 
											   if(name.equals("SB BR")) {
													name = "SBI BR";
												}
											   value=name;
										   }							   								  
										   stmt2.close();
										   rset2.close();
										    cell.setCellValue("'"+value+"'");
								}
								else if(j==27) {
									if(type.equals("S")) {
									queryString2 = "SELECT TO_CHAR(B.START_DT, 'DD/MM/YYYY') FROM FMS7_SN_BILLING_DTL A, FMS7_SN_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
											+ "AND A.FGSA_NO = B.FGSA_NO AND A.FGSA_REV_NO = B.FGSA_REV_NO AND A.SN_NO = B.SN_NO AND A.SN_REV_NO = B.SN_REV_NO AND B.CUSTOMER_CD = ? "
											+ "AND A.FGSA_NO = ? AND A.FGSA_REV_NO = ? AND A.SN_NO = ? AND A.SN_REV_NO = ?";
									} else {
										queryString2 = "SELECT TO_CHAR(B.START_DT, 'DD/MM/YYYY') FROM FMS7_SN_BILLING_DTL A, FMS7_LOA_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD "
												+ "AND A.FGSA_NO = B.TENDER_NO AND A.SN_NO = B.LOA_NO AND A.SN_REV_NO = B.LOA_REV_NO AND A.CUSTOMER_CD = ? "
												+ "AND A.FGSA_NO = ? AND A.FGSA_REV_NO = ? AND A.SN_NO = ? AND A.SN_REV_NO = ?";
									}
									stmt2 = conn.prepareStatement(queryString2);
									stmt2.setString(1, cust_cd);
									stmt2.setString(2, agr_no);
									stmt2.setString(3, rev);
									stmt2.setString(4, contract_no);
									stmt2.setString(5, cont_rev);
									rset2 = stmt2.executeQuery();
									if(rset2.next()) {
										eff_dt = rset2.getString(1);
									}
									stmt2.close();
									rset2.close();
									value = eff_dt;
								}
								
								else if(j==33) {
									if(rset5.getString(7).equals("S") || rset5.getString(7).equals("L")) {
										value="G";
									}
								}
								
								cell.setCellValue("'" + value + "'");
							}
							count++;
							logger.data(fname, (cd+","+abbr+","+agmt_no+","+"0"+","+cont_no+","+rset5.getString(6)+","+"C"+","+"1"+","+"G"+","), conn, "");
						}
						stmt5.close();
						rset5.close();
						
						
//					BILLING OF CUSTOMER WHICH DO NOT EXIST EITHER IN FMS7_SN_BILLING_DTL OR FMS_CONT_SECU_PAY_INV	
						map = cd+"-"+agmt_no+"-"+cont_no+"-"+cd+"-"+ent_pt+"-"+ext_pt; 
						queryString = "SELECT DISTINCT(MAPPING_ID) FROM FMS_CONT_SECU_PAY_INV  WHERE MAPPING_ID = ? AND CONT_AGR_TYPE IN ('Tender','FGSA')";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, map);
						
						rset = stmt.executeQuery();
						if(!rset.next()) {
						
					    queryString2 = "SELECT DISTINCT(CUSTOMER_CD) FROM FMS7_SN_BILLING_DTL  WHERE CUSTOMER_CD = ? AND FGSA_NO = ? AND FGSA_REV_NO = ? AND SN_NO = ? AND SN_REV_NO = ? "
					    		+ "AND CONT_TYPE IN ('S','L')";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, cust_cd);
						stmt2.setString(2, agr_no);
						stmt2.setString(3, rev);
						stmt2.setString(4, contract_no);
						stmt2.setString(5, cont_rev);
//						stmt5.setString(6, type);
						rset2 = stmt.executeQuery();
						if(!rset2.next()) {
						
						
						//transporter billing that do not exist in fms_cont_secu_pay_inv
						queryString5 =	"SELECT '2', A.MAPPING_ID, '1', A.MODE_CD, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,NULL, 'B', A.PAY_DUE_PERIOD, '1', A.INV_RAISED_IN, A.INV_PAY_MODE, A.INT_RATE_CD, A.INT_PAY_MODE, "
						+ "A.INT_PAY_RATE, NULL, NULL, NULL, NULL, NULL, TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), '1', NULL, NULL,'B', 'N', NULL, '15', NULL, NULL, NULL,NULL,NULL "
						+ "FROM FMS_CONT_SECU_PAY_INV A WHERE A.MAPPING_ID LIKE ? AND A.CONT_AGR_TYPE IN ('Tender','FGSA') ";
						
						stmt5 = conn.prepareStatement(queryString5);
						stmt5.setString(1, cd+"-"+agmt_no+"-%-"+cd+"-%");
						rset5 = stmt5.executeQuery();
						if(rset5.next()) {
							
							row = spreadsheet.createRow(nrow++);
							ncell = 0;
							queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, cd);
							rset1 = stmt.executeQuery();
							if(rset1.next()) {
								abbr = rset1.getString(1);
							}
							rset1.close();
							stmt1.close();
							
							if (mpe.transporter_map.containsKey(abbr)) {
								org_abbr= abbr;
						    	 abbr =mpe.transporter_map.get(abbr); 
						    }
							if (mpe.meter_map.containsKey(org_abbr)) 
							{
								org_abbr = mpe.meter_map.get(org_abbr);
							}
							cell = row.createCell(ncell++);
							cell.setCellValue("'"+abbr+"'");
							
							for (int j = 1; j < columns.split(",").length; j++) {
								cell = row.createCell(j);
								value = (rset5.getString(j + 1) == null || rset5.getString(j + 1).equals("0")) ? "null" : rset5.getString(j + 1).trim().replaceAll("'", "");
								value = value.trim().equals("") ? "null" : value;
								
								if(j==1) {
								cont_map = rset6.getString(12);
									if(cont_map!=null) {
										value = map.split("-")[1]+"-"+map.split("-")[2]+"-"+map.split("-")[4]+"-"+map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
									}
									else {
										value = agmt_no+"-"+rset6.getString(3)+"-"+rset6.getString(1)+"-"+rset6.getString(4);								
									}
									cell.setCellValue("'"+value+"'");
//									value = map;
								}
								else if(j == 3) {	//AGMT_REV
									value="0";
								}
								else if(j==6) {
									if(cont_type.equals("Tender") ||cont_type.equals("FGSA") ) {
										value = "C";
									}
//									cell.setCellValue("'"+value+"'");
									
								}
								
								else if(j == 7) {	//BILLING_FREQ
								queryString1 = "SELECT MODE_NM FROM FMS_MODE_MST WHERE MODE_CD = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, rset5.getString(4));
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									value = rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								
								if(value.equals("Fortnightly")) {
									value = "F";
								}else if(value.equals("Weekly")) {
									value="W";
								}else if(value.equals("Monthly")) {
									value="M";
								}else {
									value="O";
								}
								
						}
						
						else if(j == 11) {	//INVOICE_CUR_CD
							if(value.equals("INR"))
							{
								value = "1";
							}
							else {
								value = "2";
							}
						}
						
						else if(j == 12) {	//PAYMENT_CUR_CD
							if(value.equals("INR"))
							{
								value = "1";
							}
							else {
								value = "2";
							}
						}
						
						else if(j == 13) { 	// INT_CAL_RATE_CD
							queryString1 = "SELECT INT_RATE_NM FROM FMS_CONT_INT_RATE_MST WHERE INT_RATE_CD = ? ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, rset5.getString(14));
							rset1 = stmt1.executeQuery();
							if(rset1.next()) {
								value = rset1.getString(1);
							}
							rset1.close();
							stmt1.close();
						}
						else if(j==33) {
						     	value="G";
					         }
								cell.setCellValue("'" + value + "'");
					    	}
//							System.out.println(" :: "+value);
							count++;
							logger.data(fname, (cd+","+abbr+","+agmt_no+","+"0"+","+cont_no+","+rset5.getString(6)+","+"C"+","+"1"+","+"G"+","), conn, "");
						}
						stmt5.close();
						rset5.close();
					  }
						rset2.close();
						stmt2.close();
					}
					rset.close();
					stmt.close();
						
				}
					stmt6.close();
					rset6.close();

	//Transporter
				queryString6 = "SELECT DISTINCT(A.ENTRY_PT_CD), A.PARTY_CD,A.CONT_NO,A.EXIT_PT_CD,A.MAPPING_ID, A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,"
						+ " A.CONT_CUST_CD,A.CONT_MAPPING_ID FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND A.CONT_AGR_TYPE = 'Trans' ";
				stmt6 = conn.prepareStatement(queryString6);
				stmt6.setString(1, rset4.getString(2));
				rset6 = stmt6.executeQuery();
				
				while(rset6.next()) {
						ent_pt=rset6.getString(1);
						cd = rset6.getString(2);
						cont_no = rset6.getString(3);
						ext_pt=rset6.getString(4);
						map_id = rset6.getString(5);
						agr_no = rset6.getString(6);
						rev = rset6.getString(7);
						contract_no = rset6.getString(8);
						cont_rev = rset6.getString(9);
						cont_type = rset6.getString(10);
						cust_cd = rset6.getString(11);
						cont_map = rset6.getString(12);
						agmt_no = "1";
										
						//1
						map = cd+"-"+agmt_no+"-"+cont_no+"-"+cd+"-"+ent_pt+"-"+ext_pt; 
	//										System.out.println("map"+map);Transporter
						queryString3 = "SELECT '2', MAPPING_ID, '1', MODE_CD, CONTRACT_NO, CONTRACT_REV_NO, CONT_AGR_TYPE,NULL, 'B', PAY_DUE_PERIOD, '1', INV_RAISED_IN, INV_PAY_MODE, INT_RATE_CD, INT_PAY_MODE, "
								+ "INT_PAY_RATE, NULL, NULL, NULL, NULL, NULL, TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), '1', NULL, NULL,'B', 'N', NULL, '15', NULL, NULL, NULL,NULL,NULL "
								+ "FROM FMS_CONT_SECU_PAY_INV WHERE MAPPING_ID = ? AND CONT_AGR_TYPE = 'Trans' ";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, map);
						rset3 = stmt3.executeQuery();
						
						while(rset3.next()){
//											System.out.println("in rset3 loop>>>");	
						row = spreadsheet.createRow(nrow++);
						ncell = 0;
						map = rset3.getString(2);
//										ent_pt = rset.getString(1);
						value = "";
						
						
						queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, cd);
						rset1 = stmt1.executeQuery();
						if(rset1.next()) {
							abbr = rset1.getString(1);
						}
						rset1.close();
							stmt1.close();
							
							if (mpe.transporter_map.containsKey(abbr)) {
								org_abbr= abbr;
						    	 abbr =mpe.transporter_map.get(abbr); 
						    }
							if (mpe.meter_map.containsKey(org_abbr)) 
							{
								org_abbr = mpe.meter_map.get(org_abbr);
							}
							cell = row.createCell(ncell++);
							cell.setCellValue("'"+abbr+"'");
							
							for (int i = 1; i < columns.split(",").length; i++) {
								cell = row.createCell(i);
								value = (rset3.getString(i + 1) == null || rset3.getString(i + 1).equals("0")) ? "null" : rset3.getString(i + 1).trim().replaceAll("'", "");
								value = value.trim().equals("") ? "null" : value;
								
//											
								if(i==1) {
									cont_map = rset3.getString(2);
									value  = cont_map;
//										cell.setCellValue("'"+value+"'");
									}
									else if(i == 2) {	//AGMT_NO
										value = agmt_no;
									}
									
									else if(i == 3) {	//AGMT_REV
									value="0";
//												if(agmt_rev == 0) {
//													value = agmt_rev+"";
//												}
										}
										else if(i==6) {
											value = rset3.getString(7);
											if(value.equals("Trans")) {
												value="R";
											}
										}
											
									else if(i == 7) {	//BILLING_FREQ
										queryString1 = "SELECT MODE_NM FROM FMS_MODE_MST WHERE MODE_CD = ? ";
											stmt1 = conn.prepareStatement(queryString1);
											stmt1.setString(1, rset3.getString(4));
											rset1 = stmt1.executeQuery();
											if(rset1.next()) {
												value = rset1.getString(1);
											}
											rset1.close();
											stmt1.close();
											
											if(value.equals("Fortnightly")) {
												value = "F";
											}else if(value.equals("Weekly")) {
												value="W";
											}else if(value.equals("Monthly")) {
												value="M";
											}else {
												value="O";
											}
											
									}
									
									else if(i == 11) {	//INVOICE_CUR_CD
										if(value.equals("INR"))
										{
											value = "1";
										}
										else {
											value = "2";
										}
									}
											
									else if(i == 12) {	//PAYMENT_CUR_CD
										if(value.equals("INR"))
										{
											value = "1";
										}
										else {
											value = "2";
										}
									}
									
									else if(i == 13) { 	// INT_CAL_RATE_CD
										queryString1 = "SELECT INT_RATE_NM FROM FMS_CONT_INT_RATE_MST WHERE INT_RATE_CD = ? ";
										stmt1 = conn.prepareStatement(queryString1);
										stmt1.setString(1, rset3.getString(14));
										rset1 = stmt1.executeQuery();
										if(rset1.next()) {
											value = rset1.getString(1);
										}
										rset1.close();
										stmt1.close();
									}
							
									else if(i==27) {
										queryString1 = "SELECT TO_CHAR(CONT_ST_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_CONT_MST WHERE MAPPING_ID = ? ";
										stmt1 = conn.prepareStatement(queryString1);
										stmt1.setString(1, map);
										rset1 =stmt1.executeQuery();
										String eff_dt="";
										if(rset1.next()) {
											eff_dt = rset1.getString(1);
										}
										value=eff_dt;
									}
									else if(i==33) {
										if(rset3.getString(7).equals("Trans")) {
											value="G";
										}
									}
									
									cell.setCellValue("'" + value + "'");
								}
								count++;
							logger.data(fname, (cd+","+abbr+","+agmt_no+","+"0"+","+cont_no+","+rset3.getString(6)+","+"R"+","+eff_dt+","+"1"+","+"G"+","), conn, "");
								
							}
							rset3.close();
							stmt3.close();
							
							//2Transporter
							map = cd+"-"+agmt_no+"-"+cont_no+"-"+cd+"-"+ent_pt+"-"+ext_pt; 
							queryString = "SELECT DISTINCT(C.MAPPING_ID) FROM FMS_CONT_SECU_PAY_INV C WHERE C.MAPPING_ID = ? AND C.CONT_AGR_TYPE = 'Trans'";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, map);
									
							rset = stmt.executeQuery();
							if(!rset.next()) {
							
							//transporter billing that do not exist in fms_cont_secu_pay_inv
							queryString5 =	"SELECT '2', A.MAPPING_ID, '1', A.MODE_CD, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,NULL, 'B', A.PAY_DUE_PERIOD, '1', A.INV_RAISED_IN, A.INV_PAY_MODE, A.INT_RATE_CD, A.INT_PAY_MODE, "
							+ "A.INT_PAY_RATE, NULL, NULL, NULL, NULL, NULL, TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), '1', NULL, NULL,'B', 'N', NULL, '15', NULL, NULL, NULL,NULL,NULL "
							+ "FROM FMS_CONT_SECU_PAY_INV A WHERE A.MAPPING_ID LIKE ? AND A.CONT_AGR_TYPE = 'Trans' ";
							
							stmt5 = conn.prepareStatement(queryString5);
							stmt5.setString(1, cd+"-"+agmt_no+"-%-"+cd+"-%");
							rset5 = stmt5.executeQuery();
							if(rset5.next()) {
								
								row = spreadsheet.createRow(nrow++);
								ncell = 0;
								queryString = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cd);
								rset = stmt.executeQuery();
								if(rset.next()) {
									abbr = rset.getString(1);
								}
								rset.close();
								stmt.close();
								
								if (mpe.transporter_map.containsKey(abbr)) {
									org_abbr= abbr;
							    	 abbr =mpe.transporter_map.get(abbr); 
							    }
								if (mpe.meter_map.containsKey(org_abbr)) 
								{
									org_abbr = mpe.meter_map.get(org_abbr);
								}
								cell = row.createCell(ncell++);
								cell.setCellValue("'"+abbr+"'");
								
								for (int j = 1; j < columns.split(",").length; j++) {
									cell = row.createCell(j);
									value = (rset5.getString(j + 1) == null || rset5.getString(j + 1).equals("0")) ? "null" : rset5.getString(j + 1).trim().replaceAll("'", "");
									value = value.trim().equals("") ? "null" : value;
									
									if(j==1) {
//												cont_map = rset6.getString(5);
//												cont_map = cd+"-"+agmt_no+"-"+cont_no+"-"+cd+"-"+ent_pt+"-"+ext_pt;
										value = map;
//												cell.setCellValue("'"+map+"'");
									}
									else if(j == 3) {	//AGMT_REV
										value="0";
									}
									else if(j==6) {
										if(cont_type.equals("Trans")) {
											value = "R";
										}
//												cell.setCellValue("'"+value+"'");
										
									}
									
									else if(j == 7) {	//BILLING_FREQ
									queryString1 = "SELECT MODE_NM FROM FMS_MODE_MST WHERE MODE_CD = ? ";
									stmt1 = conn.prepareStatement(queryString1);
									stmt1.setString(1, rset5.getString(4));
									rset1 = stmt1.executeQuery();
									if(rset1.next()) {
										value = rset1.getString(1);
									}
									rset1.close();
									stmt1.close();
									
									if(value.equals("Fortnightly")) {
										value = "F";
									}else if(value.equals("Weekly")) {
										value="W";
									}else if(value.equals("Monthly")) {
										value="M";
									}else {
										value="O";
									}
									
							}
							
							else if(j == 11) {	//INVOICE_CUR_CD
								if(value.equals("INR"))
								{
									value = "1";
								}
								else {
									value = "2";
							}
						}
						
						else if(j == 12) {	//PAYMENT_CUR_CD
							if(value.equals("INR"))
							{
								value = "1";
							}
							else {
								value = "2";
							}
						}
						
						else if(j == 13) { 	// INT_CAL_RATE_CD
							queryString1 = "SELECT INT_RATE_NM FROM FMS_CONT_INT_RATE_MST WHERE INT_RATE_CD = ? ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, rset5.getString(14));
							rset1 = stmt1.executeQuery();
							if(rset1.next()) {
								value = rset1.getString(1);
							}
							rset1.close();
							stmt1.close();
						}
						else if(j==33) {
						     	value="G";
					         }
								cell.setCellValue("'" + value + "'");
					    	}
//										System.out.println(" :: "+value);
							count++;
							logger.data(fname, (cd+","+abbr+","+agmt_no+","+"0"+","+cont_no+","+rset5.getString(6)+","+"R"+","+"1"+","+"G"+","), conn, "");
						}
						stmt5.close();
						rset5.close();
						}
						rset.close();
						stmt.close();
					}
						stmt6.close();
						rset6.close();
						
						
//		Parking
						queryString6 = "SELECT DISTINCT(A.ENTRY_PT_CD), A.PARTY_CD,A.CONT_NO,A.EXIT_PT_CD,A.MAPPING_ID, A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,"
								+ " A.CONT_CUST_CD,A.CONT_MAPPING_ID FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND A.CONT_AGR_TYPE = 'Parking' ";
						stmt6 = conn.prepareStatement(queryString6);
						stmt6.setString(1, rset4.getString(2));
						rset6 = stmt6.executeQuery();
						
						while(rset6.next()) {
							
							ent_pt=rset6.getString(1);
							cd = rset6.getString(2);
							cont_no = rset6.getString(3);
							ext_pt=rset6.getString(4);
							map_id = rset6.getString(5);
							agr_no = rset6.getString(6);
							rev = rset6.getString(7);
							contract_no = rset6.getString(8);
							cont_rev = rset6.getString(9);
							cont_type = rset6.getString(10);
							cust_cd = rset6.getString(11);
							cont_map = rset6.getString(12);
							agmt_no = "1";
							map = cd+"-"+agmt_no+"-"+cont_no+"-"+cd+"-"+ent_pt+"-"+ext_pt; 
							
							queryString3 = "SELECT '2', MAPPING_ID, '1', MODE_CD, CONTRACT_NO, CONTRACT_REV_NO, CONT_AGR_TYPE,NULL, 'B', PAY_DUE_PERIOD, '1', INV_RAISED_IN, INV_PAY_MODE, INT_RATE_CD, INT_PAY_MODE, "
									+ "INT_PAY_RATE, NULL, NULL, NULL, NULL, NULL, TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), '1', NULL, NULL,'B', 'N', NULL, PAY_WAR_DAY, NULL, NULL, NULL,NULL,NULL "
									+ "FROM FMS_CONT_SECU_PAY_INV WHERE MAPPING_ID = ? AND CONT_AGR_TYPE = 'Parking' ";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, map);
//										stmt3.setString(2, agmt_type);
							rset3 = stmt3.executeQuery();
							
							while(rset3.next()){
								
								row = spreadsheet.createRow(nrow++);
								ncell = 0;
								map = rset3.getString(2);
								value = "";
								
								
								queryString1 = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, cd);
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									abbr = rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								
								if (mpe.transporter_map.containsKey(abbr)) {
									org_abbr= abbr;
									abbr =mpe.transporter_map.get(abbr); 
								}
								if (mpe.meter_map.containsKey(org_abbr)) 
								{
									org_abbr = mpe.meter_map.get(org_abbr);
								}
								cell = row.createCell(ncell++);
								cell.setCellValue("'"+abbr+"'");
								
								for (int i = 1; i < columns.split(",").length; i++) {
									cell = row.createCell(i);
									value = (rset3.getString(i + 1) == null || rset3.getString(i + 1).equals("0")) ? "null" : rset3.getString(i + 1).trim().replaceAll("'", "");
									value = value.trim().equals("") ? "null" : value;
									
									
									if(i==1) {
										value=rset3.getString(2);
									}
									else if(i == 2) {	//AGMT_NO
										value = agmt_no;
									}
									
									else if(i == 3) {	//AGMT_REV
//													if(agmt_rev == 0) {
//														value = agmt_rev+"";
//													}
										value="0";
									}
									else if(i==6) {
//										value = rset3.getString(7);
//										if(value.equals("Parking")) {
											value="K";
//										}
									}
									else if(i == 7) {	//BILLING_FREQ
										queryString1 = "SELECT MODE_NM FROM FMS_MODE_MST WHERE MODE_CD = ? ";
											stmt1 = conn.prepareStatement(queryString1);
											stmt1.setString(1, rset3.getString(4));
											rset1 = stmt1.executeQuery();
											if(rset1.next()) {
												value = rset1.getString(1);
											}
											rset1.close();
											stmt1.close();
											
											if(value.equals("Fortnightly")) {
												value = "F";
											}else if(value.equals("Weekly")) {
												value="W";
											}else if(value.equals("Monthly")) {
												value="M";
											}else {
												value="O";
											}
											
									}
									
									else if(i == 11) {	//INVOICE_CUR_CD
										if(value.equals("INR"))
										{
											value = "1";
										}
										else {
											value = "2";
										}
									}
									
									else if(i == 12) {	//PAYMENT_CUR_CD
										if(value.equals("INR"))
										{
											value = "1";
										}
										else {
											value = "2";
										}
									}
									
									else if(i == 13) { 	// INT_CAL_RATE_CD
										queryString1 = "SELECT INT_RATE_NM FROM FMS_CONT_INT_RATE_MST WHERE INT_RATE_CD = ? ";
										stmt1 = conn.prepareStatement(queryString1);
										stmt1.setString(1, rset3.getString(14));
										rset1 = stmt1.executeQuery();
										if(rset1.next()) {
											value = rset1.getString(1);
										}
										rset1.close();
										stmt1.close();
									}
									
									else if(i==27) {
										queryString1 = "SELECT TO_CHAR(CONT_ST_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_CONT_MST WHERE MAPPING_ID = ? ";
										stmt1 = conn.prepareStatement(queryString1);
										stmt1.setString(1, map);
										rset1 =stmt1.executeQuery();
										String eff_dt="";
										if(rset1.next()) {
											eff_dt = rset1.getString(1);
										}
										value=eff_dt;
									}
									
									else if(i==33) {
										if(rset3.getString(7).equals("Parking")) {
											value="P";
										}
									}
									
									cell.setCellValue("'" + value + "'");
								}
								count++;
						logger.data(fname, (cd+","+abbr+","+agmt_no+","+"0"+","+cont_no+","+rset3.getString(6)+","+"K"+","+eff_dt+","+"1"+","+"P"+","), conn, "");
								
						}
							rset3.close();
							stmt3.close();
							
//Parking billing that do not exist in fms_cont_secu_pay_inv
						map = cd+"-"+agmt_no+"-"+cont_no+"-"+cd+"-"+ent_pt+"-"+ext_pt; 
						queryString = "SELECT DISTINCT(MAPPING_ID) FROM FMS_CONT_SECU_PAY_INV WHERE MAPPING_ID = ? AND CONT_AGR_TYPE = 'Parking'";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, map);
						
						rset = stmt.executeQuery();
						if(!rset.next()) {
						
						queryString5 =	"SELECT '2', A.MAPPING_ID, '1', A.MODE_CD, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,NULL, 'B', A.PAY_DUE_PERIOD, '1', A.INV_RAISED_IN, A.INV_PAY_MODE, A.INT_RATE_CD, A.INT_PAY_MODE, "
						+ "A.INT_PAY_RATE, NULL, NULL, NULL, NULL, NULL, TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), '1', NULL, NULL,'B', 'N', NULL, '15', NULL, NULL, NULL,NULL,NULL "
						+ "FROM FMS_CONT_SECU_PAY_INV A WHERE A.MAPPING_ID LIKE ? AND A.CONT_AGR_TYPE = 'Parking' ";
						stmt5 = conn.prepareStatement(queryString5);
						stmt5.setString(1, cd+"-"+agmt_no+"-%-"+cd+"-%");
						rset5 = stmt5.executeQuery();
						if(rset5.next()) {
							
							row = spreadsheet.createRow(nrow++);
							ncell = 0;
							queryString = "SELECT TRANSPORTER_ABBR FROM FMS7_TRANSPORTER_MST WHERE TRANSPORTER_CD = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							rset = stmt.executeQuery();
							if(rset.next()) {
								abbr = rset.getString(1);
							}
							rset.close();
							stmt.close();
							
							if (mpe.transporter_map.containsKey(abbr)) {
								org_abbr= abbr;
						    	 abbr =mpe.transporter_map.get(abbr); 
						    }
							if (mpe.meter_map.containsKey(org_abbr)) 
							{
								org_abbr = mpe.meter_map.get(org_abbr);
							}
							cell = row.createCell(ncell++);
							cell.setCellValue("'"+abbr+"'");
							
							for (int j = 1; j < columns.split(",").length; j++) {
								cell = row.createCell(j);
								value = (rset5.getString(j + 1) == null || rset5.getString(j + 1).equals("0")) ? "null" : rset5.getString(j + 1).trim().replaceAll("'", "");
								value = value.trim().equals("") ? "null" : value;
								
								if(j==1) {
//									cont_map = rset6.getString(5);
//									cont_map = cd+"-"+agmt_no+"-"+cont_no+"-"+cd+"-"+ent_pt+"-"+ext_pt;
									value = map;
//									cell.setCellValue("'"+map+"'");
								}
								else if(j == 3) {	//AGMT_REV
									value="0";
								}
								else if(j==6) {
//									if(cont_type.equals("Parking")) {
										value = "K";
//									}
//									cell.setCellValue("'"+value+"'");
									
								}
								
								else if(j == 7) {	//BILLING_FREQ
								queryString1 = "SELECT MODE_NM FROM FMS_MODE_MST WHERE MODE_CD = ? ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, rset5.getString(4));
								rset1 = stmt1.executeQuery();
								if(rset1.next()) {
									value = rset1.getString(1);
								}
								rset1.close();
								stmt1.close();
								
								if(value.equals("Fortnightly")) {
									value = "F";
								}else if(value.equals("Weekly")) {
									value="W";
								}else if(value.equals("Monthly")) {
									value="M";
								}else {
									value="O";
								}
								
						}
						
						else if(j == 11) {	//INVOICE_CUR_CD
							if(value.equals("INR"))
							{
								value = "1";
							}
							else {
								value = "2";
							}
						}
						
						else if(j == 12) {	//PAYMENT_CUR_CD
							if(value.equals("INR"))
							{
								value = "1";
							}
							else {
								value = "2";
							}
						}
						
						else if(j == 13) { 	// INT_CAL_RATE_CD
							queryString1 = "SELECT INT_RATE_NM FROM FMS_CONT_INT_RATE_MST WHERE INT_RATE_CD = ? ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, rset5.getString(14));
							rset1 = stmt1.executeQuery();
							if(rset1.next()) {
								value = rset1.getString(1);
							}
							rset1.close();
							stmt1.close();
						}
						else if(j==33) {
						     	value="P";
					         }
								cell.setCellValue("'" + value + "'");
					    	}
//							System.out.println(" :: "+value);
							count++;
							logger.data(fname, (cd+","+abbr+","+agmt_no+","+"0"+","+cont_no+","+rset5.getString(6)+","+"K"+","+"1"+","+"P"+","), conn, "");
						}
						stmt5.close();
						rset5.close();
						}
						rset.close();
						stmt.close();
					}
						rset6.close();
						stmt6.close();
					}
					rset4.close();
					stmt4.close();
					
//					}
//					rset2.close();
//					stmt2.close();
//					}
					filename = migration_setup_dir + "EXPORT/FMS_GTA_BILLING_DTL_"+start_end_dt+".xlsx";
					
					fileOut = new FileOutputStream(filename);
					
					workbook.write(fileOut);
					fileOut.close();
					
					logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,,, "), conn);
					logger.checkpoint(fname, "<<END>>,<<FMS_GTA_BILLING_DTL>>,,,,,,", conn);
					
					
					logger.checkpoint1(fname1,count+",", conn);
					
					System.out.println("<<END>><<FMS_GTA_BILLING_DTL>>");
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


	public void FMS_DAILY_TRANSPORTER_NOM() throws SQLException, IOException {
		
		function_nm = "FMS_DAILY_TRANSPORTER_NOM()";
		
		try {
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DAILY_TRANSPORTER_NOM>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SELL_CONT_MAP,GAS_DT,GEN_DT,GEN_TIME,NOM_REV_NO,MDQ,MDQ_UNIT,BASE,GCV,NCV,"
					+ "QTY_MMBTU,QTY_SCM,EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			
//			nrow = 0;
//			ncell = 0;
			count = 0;
			String org_abbr= "",base="",gas_dt="",nom_rev="",mdq="",cus_cd="",cont_agr_no="",cont_agr_rev="",contract_no="",contract_rev="";
			
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			fname_csv = migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,GAS_DT,NOM_REV_NO,TRANSPORTER_MAP,CONT_MAP,MDQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				
				//CUSTOMER
				queryString1 = "SELECT A.TRANSPORTER_MAPPING_ID, A.MAPPING_ID, NULL, NULL, NULL, NULL, NULL, TO_CHAR(A.GAS_DT,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.GEN_DT,'DD/MM/YYYY HH24:MI:SS'), A.GEN_TIME, A.NOM_REV_NO, A.ENTRY_MDQ, '1', A.ENE_BASE, A.GCV_ENTRY, A.NCV_ENTRY, "
						+ " A.QTY_MMBTU_ENTRY, A.QTY_SCM_ENTRY, A.ENE_BASE, A.GCV_EXIT, A.NCV_EXIT, QTY_MMBTU_EXIT, QTY_SCM_EXIT, A.EMP_CD, TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
						+ " '1', NULL , NULL "
						+ "FROM FMS7_DAILY_TRANSPORTER_NOM_DTL A, FMS7_TRANSPORTER_MST B, FMS_CONT_MST C WHERE A.TRANSPORTER_CD = B.TRANSPORTER_CD AND C.PARTY_CD = B.TRANSPORTER_CD "
						+ "AND A.TRANSPORTER_MAPPING_ID = C.MAPPING_ID AND A.MAPPING_ID = C.CONT_MAPPING_ID AND B.COUNTERPARTY_CD = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					str = "";
					
//					row = spreadsheet.createRow(nrow++);
//					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					trans_map = rset1.getString(1);
					cont_map = rset1.getString(2);
					gas_dt =  rset1.getString(8);
					nom_rev =  rset1.getString(11);
					mdq = rset1.getString(12);
					base = rset1.getString(14);
					abbr1 = abbr;
					
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
						abbr =mpe.transporter_map.get(abbr); 
					}
					
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					
					
//					cell = row.createCell(ncell++);
//					cell.setCellValue("'"+abbr+"'");
					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i == 1) {	//MAPPING_ID
							if(cont_map!=null) {
								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
								}
							else {
								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5];
								}
							
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if(i == 2) {	//CONTRACT_TYPE
//							if(cont_map!=null) {
//								value = cont_map.substring(0,1);
//							}
							value = "C";
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if(i == 12) {	//MDQ
							if(mdq==null) {
								if(cont_map!=null) {
									cus_cd = cont_map.split("-")[1];
									cont_agr_no = cont_map.split("-")[2];
									cont_agr_rev = cont_map.split("-")[3];
									contract_no = cont_map.split("-")[4];
									contract_rev = cont_map.split("-")[5];
									}
								queryString2 = "SELECT A.ENE FROM FMS_MDQ_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_REV_NO = ? "
										+ "AND A.CONTRACT_NO = ? AND A.CONTRACT_REV_NO = ? AND A.CONT_AGR_TYPE IN('FGSA','Tender') "
										+ "AND A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS_MDQ_DTL C WHERE A.MAPPING_ID = C.MAPPING_ID AND A.CONT_AGR_NO = C.CONT_AGR_NO AND "
										+ "A.CONT_AGR_REV_NO = C.CONT_AGR_REV_NO AND A.CONT_CUST_CD = C.CONT_CUST_CD AND A.CONTRACT_NO = C.CONTRACT_NO "
										+ "AND A.CONTRACT_REV_NO = C.CONTRACT_REV_NO AND A.CONT_AGR_TYPE IN('FGSA','Tender'))";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, trans_map);
								stmt2.setString(2, cont_agr_no);
								stmt2.setString(3, cus_cd);
								stmt2.setString(4, cont_agr_rev);
								stmt2.setString(5, contract_no);
								stmt2.setString(6, contract_rev);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									mdq = rset2.getString(1);
								}
								else {
									queryString3 = "SELECT TOT_ENE FROM FMS_CONT_MST WHERE MAPPING_ID = ? AND CONT_MAPPING_ID = ? AND CONT_AGR_TYPE IN ('FGSA','Tender') ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, trans_map);
									stmt3.setString(2, cont_map);
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
									mdq = rset3.getString(1);
									}
									rset3.close();
									stmt3.close();
								}
								rset2.close();
								stmt2.close();
								
								
							}
							else {
								mdq =  rset1.getString(12);
							}
							str += mdq + ",";
						}
						else if(i == 14) {	//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i == 19) {//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}

						else {
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
					
				}
				rset1.close();
				stmt1.close();
				
				
				//TRANSPORTER

				queryString1 = "SELECT A.MAPPING_ID, 'R', NULL, NULL, NULL, NULL, NULL, TO_CHAR(A.NOM_DT,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.GEN_DT,'DD/MM/YYYY HH24:MI:SS'), NVL(A.TIME_ST_DAY,'00:00'), A.REV_NO, A.ENTRY_MDQ, '1', A.NOM_BASE_FLAG, A.GHV, A.NHV, "
						+ " A.TOT_ENE, A.DAY_TOT, A.NOM_BASE_FLAG, A.GHV, A.NHV, A.EXIT_TOT_ENE, A.EXIT_DAY_TOT, NULL, NULL, NULL, NULL, "
						+ " '1', NULL , NULL "
						+ "FROM FMS_DAILY_NOM A, FMS7_TRANSPORTER_MST B, FMS_CONT_MST C WHERE A.PARTY_CD = B.TRANSPORTER_CD AND C.PARTY_CD = B.TRANSPORTER_CD "
						+ "AND A.MAPPING_ID = C.MAPPING_ID AND A.CONT_MAPPING_ID = '0' AND C.CONT_AGR_TYPE IN('Trans') AND B.COUNTERPARTY_CD = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					str = "";
					
//					row = spreadsheet.createRow(nrow++);
//					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					trans_map = rset1.getString(1);
					//cont_map = rset1.getString(2);
					gas_dt =  rset1.getString(8);
					nom_rev =  rset1.getString(11);
					mdq = rset1.getString(12);
					base = rset1.getString(14);
					abbr1 = abbr;
					
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
						abbr =mpe.transporter_map.get(abbr); 
					}
					
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					
					
//					cell = row.createCell(ncell++);
//					cell.setCellValue("'"+abbr+"'");
					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i == 1) {	//MAPPING_ID
//							if(cont_map!=null) {
//								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
//								}
//							else {
//								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5];
//								}
							
//							cell.setCellValue("'"+value+"'");
							value = trans_map;
							str += value + ",";
						}
						else if(i == 12) {	//MDQ
							if(mdq==null) {
								
									queryString3 = "SELECT TOT_ENE FROM FMS_CONT_MST WHERE MAPPING_ID = ? AND CONT_AGR_TYPE = 'Trans' ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, trans_map);
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
									mdq = rset3.getString(1);
									}
									rset3.close();
									stmt3.close();
							}
							else {
								mdq =  rset1.getString(12);
							}
							str += mdq + ",";
						}
						else if(i == 14) {	//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i == 19) {//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}

						else {
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + mdq + "," ), conn, "");
					
				}
				rset1.close();
				stmt1.close();
				
				
				
				//PARKING
				
				queryString1 = "SELECT A.MAPPING_ID, 'K', NULL, NULL, NULL, NULL, NULL, TO_CHAR(A.NOM_DT,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.GEN_DT,'DD/MM/YYYY HH24:MI:SS'), NVL(A.TIME_ST_DAY,'00:00'), A.REV_NO, A.ENTRY_MDQ, '1', A.NOM_BASE_FLAG, A.GHV, A.NHV, "
						+ " A.TOT_ENE, A.DAY_TOT, A.NOM_BASE_FLAG, A.GHV, A.NHV, A.EXIT_TOT_ENE, A.EXIT_DAY_TOT, NULL, NULL, NULL, NULL, "
						+ " '1', NULL , NULL "
						+ "FROM FMS_DAILY_NOM A, FMS7_TRANSPORTER_MST B, FMS_CONT_MST C WHERE A.PARTY_CD = B.TRANSPORTER_CD AND C.PARTY_CD = B.TRANSPORTER_CD "
						+ "AND A.MAPPING_ID = C.MAPPING_ID AND A.CONT_MAPPING_ID = '1' AND C.CONT_AGR_TYPE IN('Parking') AND B.COUNTERPARTY_CD = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();
				
				while (rset1.next()) {
					str = "";
					
//					row = spreadsheet.createRow(nrow++);
//					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					trans_map = rset1.getString(1);
					//cont_map = rset1.getString(2);
					gas_dt =  rset1.getString(8);
					nom_rev =  rset1.getString(11);
					mdq = rset1.getString(12);
					base = rset1.getString(14);
					abbr1 = abbr;
					
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
						abbr =mpe.transporter_map.get(abbr); 
					}
					
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					
					
//					cell = row.createCell(ncell++);
//					cell.setCellValue("'"+abbr+"'");
					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i == 1) {	//MAPPING_ID
//							if(cont_map!=null) {
//								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
//								}
//							else {
//								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5];
//								}
							
//							cell.setCellValue("'"+value+"'");
							value = trans_map;
							str += value + ",";
						}
						else if(i == 12) {	//MDQ
							if(mdq==null) {
								
								queryString3 = "SELECT TOT_ENE FROM FMS_CONT_MST WHERE MAPPING_ID = ? AND CONT_AGR_TYPE = 'Parking' ";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_map);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									mdq = rset3.getString(1);
								}
								rset3.close();
								stmt3.close();
							}
							else {
								mdq =  rset1.getString(12);
							}
							str += mdq + ",";
						}
						else if(i == 14) {	//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i == 19) {//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						
						else {
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + mdq + "," ), conn, "");
					
				}
				rset1.close();
				stmt1.close();
			
			}
			stmt.close();
			rset.close();
			
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_"+start_end_dt+".xlsx";
			
//			fileOut = new FileOutputStream(filename);  
			
//			workbook.write(fileOut);
//			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DAILY_TRANSPORTER_NOM>>,,,", conn);
			
			
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
	
	public void FMS_DAILY_TRANSPORTER_SCH() throws SQLException, IOException {
		
		function_nm = "FMS_DAILY_TRANSPORTER_SCH()";
		
		try {
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DAILY_TRANSPORTER_SCH>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SELL_CONT_MAP,GAS_DT,GEN_DT,GEN_TIME,SCH_REV_NO,MDQ,MDQ_UNIT,BASE,GCV,NCV,"
					+ "QTY_MMBTU,QTY_SCM,EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			
//			nrow = 0;
//			ncell = 0;
			count = 0;
			String base="",gas_dt="",nom_rev="",mdq="";
//			int cont_no=0;
			double baseVal=0d;
			double deviding_factor=1d;
			double multiplying_factor= 0.252*1000000;
			double gcv=9802.8d;
			double ncv=8831.35d;
			double qty_mmbtu=0;
			double qty_mmbtu1=0;
			
			
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			fname_csv = migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_SCH_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,GAS_DT,NOM_REV_NO,TRANSPORTER_MAP,CONT_MAP,MDQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				//CUSTOMER
				queryString1 = "SELECT A.MAPPING_ID, A.CONT_MAPPING_ID, NULL, NULL, NULL, NULL, NULL, TO_CHAR(A.SCHEDULE_DT,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.GEN_DT,'DD/MM/YYYY HH24:MI:SS'),"
						+ " A.TIME_MADE, A.REV_NO, NULL, '1', A.SCH_BASE_FLAG, A.GHV_E, A.NHV, "
						+ " A.ENTRY_TOT_ENE, NULL, A.SCH_BASE_FLAG, A.GHV_EXT, A.NHV, EXIT_TOT_ENE, NULL, '1', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
						+ " '1', NULL , NULL "
						+ "FROM FMS_DAILY_SCHEDULE A, FMS7_TRANSPORTER_MST B, FMS_CONT_MST C WHERE A.PARTY_CD = B.TRANSPORTER_CD AND C.PARTY_CD = B.TRANSPORTER_CD "
						+ "AND A.MAPPING_ID = C.MAPPING_ID AND A.CONT_MAPPING_ID = C.CONT_MAPPING_ID AND C.CONT_AGR_TYPE IN ('FGSA','Tender') AND B.COUNTERPARTY_CD = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();	
				
				while (rset1.next()) {
					str = "";
					base = rset1.getString(14);
//					row = spreadsheet.createRow(nrow++);
//					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					trans_map = rset1.getString(1);
					cont_map = rset1.getString(2);
					gas_dt =  rset1.getString(8);
					nom_rev =  rset1.getString(11);
					qty_mmbtu = rset1.getDouble(17);
					qty_mmbtu1 = rset1.getDouble(22);
					mdq=null;

					if (mpe.transporter_map.containsKey(abbr)) {
						abbr =mpe.transporter_map.get(abbr); 
					}

//					cell = row.createCell(ncell++);
//					cell.setCellValue("'"+abbr+"'");
					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i == 1) {	//MAPPING_ID
							if(cont_map!=null && cont_map.length()>2) {
								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
							}
							else {
								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5];
							}
							
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if(i==2) {
							value = "C";
							str += value + ",";
						}
						else if(i == 12) {	//MDQ
							queryString2 = "SELECT ENTRY_MDQ FROM FMS7_DAILY_TRANSPORTER_NOM_DTL WHERE TRANSPORTER_MAPPING_ID = ? AND MAPPING_ID = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, trans_map);
							stmt2.setString(2, cont_map);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								mdq = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							if(mdq!=null) {
								value = mdq;
							}
							else {
								queryString2 = "SELECT TOT_ENE FROM FMS_CONT_MST WHERE MAPPING_ID = ? AND CONT_MAPPING_ID = ? AND CONT_AGR_TYPE IN ('FGSA','Tender') ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, trans_map);
								stmt2.setString(2, cont_map);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									mdq = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								
								value = mdq;
							}
							
							str += value + ",";
						}
						else if(i == 14) {	//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i==18) {
							if(base.equals("GCV")) {
								baseVal=gcv;
								deviding_factor=1;
							}
							else {
								baseVal=ncv;
								deviding_factor=1.11d;
							}
//							Double result1= (double) Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
							BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor)));
							result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
							
							value = result1.toString();
//							value = value.format("%.2f", value);
							//cell.setCellValue("'" + value + "'");
							str += value + ",";
						}
						
						else if(i == 19	) {//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i==23) {
							if(base.equals("GCV")) {
								baseVal=gcv;
								deviding_factor=1;
							}
							else {
								baseVal=ncv;
								deviding_factor=1.11d;
							}
							BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu1*multiplying_factor/(baseVal*deviding_factor)));
							result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
//							value = Double.toString(result2);
							
							value = result1.toString();
//							value = value.format("%.2f", value);
							//cell.setCellValue("'" + value + "'");
							str += value + ",";						
							}
						else {
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
					
				}
				rset1.close();
				stmt1.close();
				
				
				
				
				//TRANSPORTER
				queryString1 = "SELECT A.MAPPING_ID, A.CONT_MAPPING_ID, NULL, NULL, NULL, NULL, NULL, TO_CHAR(A.SCHEDULE_DT,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.GEN_DT,'DD/MM/YYYY HH24:MI:SS'),"
						+ " A.TIME_MADE, A.REV_NO, NULL, '1', A.SCH_BASE_FLAG, A.GHV_E, A.NHV, "
						+ " A.ENTRY_TOT_ENE, NULL, A.SCH_BASE_FLAG, A.GHV_EXT, A.NHV, EXIT_TOT_ENE, NULL, '1', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
						+ " '1', NULL , NULL "
						+ "FROM FMS_DAILY_SCHEDULE A, FMS7_TRANSPORTER_MST B, FMS_CONT_MST C WHERE A.PARTY_CD = B.TRANSPORTER_CD AND C.PARTY_CD = B.TRANSPORTER_CD "
						+ "AND A.MAPPING_ID = C.MAPPING_ID AND A.CONT_MAPPING_ID = '0' AND C.CONT_AGR_TYPE = 'Trans' AND B.COUNTERPARTY_CD = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();	
				
				while (rset1.next()) {
					str = "";
					base = rset1.getString(14);
//					row = spreadsheet.createRow(nrow++);
//					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					trans_map = rset1.getString(1);
					cont_map = rset1.getString(2);
					gas_dt =  rset1.getString(8);
					nom_rev =  rset1.getString(11);
					qty_mmbtu = rset1.getDouble(17);
					qty_mmbtu1 = rset1.getDouble(22);
					mdq=null;

					if (mpe.transporter_map.containsKey(abbr)) {
						abbr =mpe.transporter_map.get(abbr); 
					}

//					cell = row.createCell(ncell++);
//					cell.setCellValue("'"+abbr+"'");
					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i == 1) {	//MAPPING_ID
//							if(cont_map!=null && cont_map.length()>2) {
//								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
//							}
//							else {
//								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5];
//							}
							value = trans_map;
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if(i==2) {
							value = "R";
							str += value + ",";
						}
						else if(i == 12) {	//MDQ
							
							queryString2 = "SELECT ENTRY_MDQ FROM FMS_DAILY_NOM WHERE MAPPING_ID = ? AND CONT_MAPPING_ID = '0' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, trans_map);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								mdq = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							if(mdq!=null) {
								value = mdq;
							}
							else {
								queryString2 = "SELECT TOT_ENE FROM FMS_CONT_MST WHERE MAPPING_ID = ? AND CONT_AGR_TYPE = 'Trans' ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, trans_map);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									mdq = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								
								value = mdq;
							}
							
							str += value + ",";
						}
						else if(i == 14) {	//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i==18) {
							if(base.equals("GCV")) {
								baseVal=gcv;
								deviding_factor=1;
							}
							else {
								baseVal=ncv;
								deviding_factor=1.11d;
							}
//							Double result1= (double) Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
							BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor)));
							result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
							
							value = result1.toString();
//							value = value.format("%.2f", value);
							//cell.setCellValue("'" + value + "'");
							str += value + ",";
						}
						
						else if(i == 19	) {//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i==23) {
							if(base.equals("GCV")) {
								baseVal=gcv;
								deviding_factor=1;
							}
							else {
								baseVal=ncv;
								deviding_factor=1.11d;
							}
							BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu1*multiplying_factor/(baseVal*deviding_factor)));
							result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
//							value = Double.toString(result2);
							
							value = result1.toString();
//							value = value.format("%.2f", value);
							//cell.setCellValue("'" + value + "'");
							str += value + ",";						
							}
						else {
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
					
				}
				rset1.close();
				stmt1.close();
			
				//PARKING
				queryString1 = "SELECT A.MAPPING_ID, A.CONT_MAPPING_ID, NULL, NULL, NULL, NULL, NULL, TO_CHAR(A.SCHEDULE_DT,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.GEN_DT,'DD/MM/YYYY HH24:MI:SS'),"
						+ " A.TIME_MADE, A.REV_NO, NULL, '1', A.SCH_BASE_FLAG, A.GHV_E, A.NHV, "
						+ " A.ENTRY_TOT_ENE, NULL, A.SCH_BASE_FLAG, A.GHV_EXT, A.NHV, EXIT_TOT_ENE, NULL, '1', TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
						+ " '1', NULL , NULL "
						+ "FROM FMS_DAILY_SCHEDULE A, FMS7_TRANSPORTER_MST B, FMS_CONT_MST C WHERE A.PARTY_CD = B.TRANSPORTER_CD AND C.PARTY_CD = B.TRANSPORTER_CD "
						+ "AND A.MAPPING_ID = C.MAPPING_ID AND A.CONT_MAPPING_ID = '1' AND C.CONT_AGR_TYPE = 'Parking' AND B.COUNTERPARTY_CD = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();	
				
				while (rset1.next()) {
					str = "";
					base = rset1.getString(14);
//					row = spreadsheet.createRow(nrow++);
//					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					trans_map = rset1.getString(1);
					cont_map = rset1.getString(2);
					gas_dt =  rset1.getString(8);
					nom_rev =  rset1.getString(11);
					qty_mmbtu = rset1.getDouble(17);
					qty_mmbtu1 = rset1.getDouble(22);
					mdq=null;
					
					if (mpe.transporter_map.containsKey(abbr)) {
						abbr =mpe.transporter_map.get(abbr); 
					}
					
//					cell = row.createCell(ncell++);
//					cell.setCellValue("'"+abbr+"'");
					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i == 1) {	//MAPPING_ID
//							if(cont_map!=null && cont_map.length()>2) {
//								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
//							}
//							else {
//								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5];
//							}
							value = trans_map;
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if(i==2) {
							value = "K";
							str += value + ",";
						}
						else if(i == 12) {	//MDQ
							
							queryString2 = "SELECT ENTRY_MDQ FROM FMS_DAILY_NOM WHERE MAPPING_ID = ? AND CONT_MAPPING_ID = '1' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, trans_map);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								mdq = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							if(mdq!=null) {
								value = mdq;
							}
							else {
								queryString2 = "SELECT TOT_ENE FROM FMS_CONT_MST WHERE MAPPING_ID = ? AND CONT_AGR_TYPE = 'Parking' ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, trans_map);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									mdq = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								
								value = mdq;
							}
							
							str += value + ",";
						}
						else if(i == 14) {	//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i==18) {
							if(base.equals("GCV")) {
								baseVal=gcv;
								deviding_factor=1;
							}
							else {
								baseVal=ncv;
								deviding_factor=1.11d;
							}
//							Double result1= (double) Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
							BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor)));
							result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
							
							value = result1.toString();
//							value = value.format("%.2f", value);
							//cell.setCellValue("'" + value + "'");
							str += value + ",";
						}
						
						else if(i == 19	) {//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i==23) {
							if(base.equals("GCV")) {
								baseVal=gcv;
								deviding_factor=1;
							}
							else {
								baseVal=ncv;
								deviding_factor=1.11d;
							}
							BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu1*multiplying_factor/(baseVal*deviding_factor)));
							result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
//							value = Double.toString(result2);
							
							value = result1.toString();
//							value = value.format("%.2f", value);
							//cell.setCellValue("'" + value + "'");
							str += value + ",";						
						}
						else {
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
					
				}
				rset1.close();
				stmt1.close();
				
			}
			stmt.close();
			rset.close();
			
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_NOM_"+start_end_dt+".xlsx";
			
//			fileOut = new FileOutputStream(filename);  
			
//			workbook.write(fileOut);
//			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DAILY_TRANSPORTER_SCH>>,,,", conn);
			
			
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

	public void FMS_DAILY_TRANSPORTER_ALLOC() throws SQLException, IOException {
		
		function_nm = "FMS_DAILY_TRANSPORTER_ALLOC()";
		
		try {
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_DAILY_TRANSPORTER_ALLOC>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,SELL_CONT_MAP,GAS_DT,GEN_DT,GEN_TIME,ALLOC_REV_NO,MDQ,MDQ_UNIT,BASE,GCV,NCV,"
					+ "QTY_MMBTU,QTY_SCM,EXIT_BASE,EXIT_GCV,EXIT_NCV,EXIT_QTY_MMBTU,EXIT_QTY_SCM,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,BU_SEQ,ENTRY_PT_MAPPING_ID,EXIT_PT_MAPPING_ID,"
					+ "ADJ_IMBALANCE";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			
//			nrow = 0;
//			ncell = 0;
			count = 0;
			String org_abbr= "",base="",gas_dt="",nom_rev="",mdq="";
			double baseVal=0d;
			double deviding_factor=1d;
			double multiplying_factor= 0.252*1000000;
			double gcv=9802.8d;
			double ncv=8831.35d;
			double qty_mmbtu=0;
			double qty_mmbtu1=0;
			
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			fname_csv = migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_ALLOC_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,GAS_DT,NOM_REV_NO,TRANSPORTER_MAP,CONT_MAP,MDQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				//CUSTOMER
				queryString1 = "SELECT A.MAPPING_ID, A.CONT_MAPPING_ID, NULL, NULL, NULL, NULL, NULL, TO_CHAR(A.ALLOC_DT,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.ALLOC_DT - INTERVAL '1' DAY,'DD/MM/YYYY HH24:MI:SS'),"
						+ " '12:00', '0', NULL, '1', A.ALLOC_BASE_FLAG, A.ENTRY_HV_GHV, A.ENTRY_HV_NHV, "
						+ " A.ENTRY_TOT_ENE, '0', A.ALLOC_BASE_FLAG, A.EXIT_HV_GHV, A.EXIT_HV_NHV, EXIT_TOT_ENE, '0', '1', TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
						+ " '1', NULL , NULL,A.IMBALANCE "
						+ "FROM FMS_ALLOC_MST A, FMS7_TRANSPORTER_MST B, FMS_CONT_MST C WHERE A.PARTY_CD = B.TRANSPORTER_CD AND C.PARTY_CD = B.TRANSPORTER_CD "
						+ "AND A.MAPPING_ID = C.MAPPING_ID AND A.CONT_MAPPING_ID = C.CONT_MAPPING_ID AND B.COUNTERPARTY_CD = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();	
				
				while (rset1.next()) {
					str = "";
					base = rset1.getString(14);
//					row = spreadsheet.createRow(nrow++);
//					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					trans_map = rset1.getString(1);
					cont_map = rset1.getString(2);
					gas_dt =  rset1.getString(8);
					nom_rev =  rset1.getString(11);
					abbr1 = abbr;
					qty_mmbtu = rset1.getDouble(17);
					qty_mmbtu1 = rset1.getDouble(22);
					mdq=null;
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
						abbr =mpe.transporter_map.get(abbr); 
					}
					
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					
					
//					cell = row.createCell(ncell++);
//					cell.setCellValue("'"+abbr+"'");
					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i == 1) {	//MAPPING_ID
							if(cont_map!=null && cont_map.length()>1) {
								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
							}
							else {
								value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5];
							}
							
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if(i == 2) {	//CONTRACT_TYPE
							value = "C";
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if(i == 12) {	//MDQ
							queryString2 = "SELECT ENTRY_MDQ FROM FMS7_DAILY_TRANSPORTER_NOM_DTL WHERE TRANSPORTER_MAPPING_ID = ? AND MAPPING_ID = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, trans_map);
							stmt2.setString(2, cont_map);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								mdq = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							if(mdq!=null) {
								value = mdq;
							}
							else {
								queryString2 = "SELECT TOT_ENE FROM FMS_CONT_MST WHERE MAPPING_ID = ? AND CONT_MAPPING_ID = ? AND CONT_AGR_TYPE IN ('FGSA','Tender') ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, trans_map);
								stmt2.setString(2, cont_map);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									mdq = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								
								value = mdq;
							}
							
							str += value + ",";
						}
						
						else if(i == 14) {	//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i==18) {
							if(base.equals("GCV")) {
								baseVal=gcv;
								deviding_factor=1;
							}
							else {
								baseVal=ncv;
								deviding_factor=1.11d;
							}
//							Double result1= (double) Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
							BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor)));
							result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
							
							value = result1.toString();
//							value = value.format("%.2f", value);
							//cell.setCellValue("'" + value + "'");
							str += value + ",";
						}
						
						else if(i == 19) {//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
}
						else if(i == 23) {
							if(base.equals("GCV")) {
								baseVal=gcv;
								deviding_factor=1;
							}
							else {
								baseVal=ncv;
								deviding_factor=1.11d;
							}
							BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu1*multiplying_factor/(baseVal*deviding_factor)));
							result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
//							value = Double.toString(result2);
							
							value = result1.toString();
//							value = value.format("%.2f", value);
							//cell.setCellValue("'" + value + "'");
							str += value + ",";						
							}
						
						else {
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
					
				}
				rset1.close();
				stmt1.close();
				
				
				//TRANSPORTER
				queryString1 = "SELECT A.MAPPING_ID, NULL, NULL, NULL, NULL, NULL, NULL, TO_CHAR(A.ALLOC_DT,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.ALLOC_DT - INTERVAL '1' DAY,'DD/MM/YYYY HH24:MI:SS'),"
						+ " '12:00', '0', NULL, '1', A.ALLOC_BASE_FLAG, A.ENTRY_HV_GHV, A.ENTRY_HV_NHV, "
						+ " A.ENTRY_TOT_ENE, '0', A.ALLOC_BASE_FLAG, A.EXIT_HV_GHV, A.EXIT_HV_NHV, EXIT_TOT_ENE, '0', '1', TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
						+ " '1', NULL , NULL,A.IMBALANCE "
						+ "FROM FMS_ALLOC_MST A, FMS7_TRANSPORTER_MST B, FMS_CONT_MST C WHERE A.PARTY_CD = B.TRANSPORTER_CD AND C.PARTY_CD = B.TRANSPORTER_CD "
						+ "AND A.MAPPING_ID = C.MAPPING_ID AND A.CONT_MAPPING_ID = '0' AND B.COUNTERPARTY_CD = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				rset1 = stmt1.executeQuery();	
				
				while (rset1.next()) {
					str = "";
					base = rset1.getString(14);
//					row = spreadsheet.createRow(nrow++);
//					ncell = 0;
					abbr = rset.getString(1);
					cd = rset1.getString(1);
					trans_map = rset1.getString(1);
					gas_dt =  rset1.getString(8);
					nom_rev =  rset1.getString(11);
					abbr1 = abbr;
					qty_mmbtu = rset1.getDouble(17);
					qty_mmbtu1 = rset1.getDouble(22);
					mdq=null;
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
						abbr =mpe.transporter_map.get(abbr); 
					}
					
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					
					
//					cell = row.createCell(ncell++);
//					cell.setCellValue("'"+abbr+"'");
					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						if(i == 1) {	//MAPPING_ID
							value = trans_map;
							
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if(i == 2) {	//CONTRACT_TYPE
							value = "R";
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						else if(i == 12) {	//MDQ
							
							queryString2 = "SELECT ENTRY_MDQ FROM FMS_DAILY_NOM WHERE MAPPING_ID = ? AND CONT_MAPPING_ID = '0' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, trans_map);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								mdq = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							if(mdq!=null) {
								value = mdq;
							}
							else {
								queryString2 = "SELECT TOT_ENE FROM FMS_CONT_MST WHERE MAPPING_ID = ? AND CONT_AGR_TYPE = 'Trans' ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, trans_map);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									mdq = rset2.getString(1);
								}
								rset2.close();
								stmt2.close();
								
								value = mdq;
							}
							
							str += value + ",";
						}
						else if(i == 14) {	//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
						}
						else if(i==18) {
							if(base.equals("GCV")) {
								baseVal=gcv;
								deviding_factor=1;
							}
							else {
								baseVal=ncv;
								deviding_factor=1.11d;
							}
//							Double result1= (double) Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
							BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor)));
							result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
							
							value = result1.toString();
//							value = value.format("%.2f", value);
							//cell.setCellValue("'" + value + "'");
							str += value + ",";
						}
						
						else if(i == 19) {//BASE
							if(base.equals("GHV")) {
								base = "GCV";
							}
							else if(base.equals("NHV")) {
								base = "NCV";
							}
//							cell.setCellValue("'"+base+"'");
							str += base + ",";
}
						else if(i == 23) {
							if(base.equals("GCV")) {
								baseVal=gcv;
								deviding_factor=1;
							}
							else {
								baseVal=ncv;
								deviding_factor=1.11d;
							}
							BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu1*multiplying_factor/(baseVal*deviding_factor)));
							result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
//							value = Double.toString(result2);
							
							value = result1.toString();
//							value = value.format("%.2f", value);
							//cell.setCellValue("'" + value + "'");
							str += value + ",";						
							}
						
						else {
//							cell.setCellValue("'"+value+"'");
							str += value + ",";
						}
						
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
					
				}
				rset1.close();
				stmt1.close();
			
			
			//PARKING
			queryString1 = "SELECT A.MAPPING_ID, NULL, NULL, NULL, NULL, NULL, NULL, TO_CHAR(A.ALLOC_DT,'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.ALLOC_DT - INTERVAL '1' DAY,'DD/MM/YYYY HH24:MI:SS'),"
					+ " '12:00', '0', NULL, '1', A.ALLOC_BASE_FLAG, A.ENTRY_HV_GHV, A.ENTRY_HV_NHV, "
					+ " A.ENTRY_TOT_ENE, '0', A.ALLOC_BASE_FLAG, A.EXIT_HV_GHV, A.EXIT_HV_NHV, EXIT_TOT_ENE, '0', '1', TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, "
					+ " '1', NULL , NULL,A.IMBALANCE "
					+ "FROM FMS_ALLOC_MST A, FMS7_TRANSPORTER_MST B, FMS_CONT_MST C WHERE A.PARTY_CD = B.TRANSPORTER_CD AND C.PARTY_CD = B.TRANSPORTER_CD "
					+ "AND A.MAPPING_ID = C.MAPPING_ID AND A.CONT_MAPPING_ID = '1' AND B.COUNTERPARTY_CD = ? ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, rset.getString(2));
			rset1 = stmt1.executeQuery();	
			
			while (rset1.next()) {
				str = "";
				base = rset1.getString(14);
//					row = spreadsheet.createRow(nrow++);
//					ncell = 0;
				abbr = rset.getString(1);
				cd = rset1.getString(1);
				trans_map = rset1.getString(1);
				gas_dt =  rset1.getString(8);
				nom_rev =  rset1.getString(11);
				abbr1 = abbr;
				qty_mmbtu = rset1.getDouble(17);
				qty_mmbtu1 = rset1.getDouble(22);
				mdq=null;
				if (mpe.transporter_map.containsKey(abbr)) {
					org_abbr= abbr;
					abbr =mpe.transporter_map.get(abbr); 
				}
				
				if (mpe.meter_map.containsKey(org_abbr)) 
				{
					org_abbr = mpe.meter_map.get(org_abbr);
				}
				
				
//					cell = row.createCell(ncell++);
//					cell.setCellValue("'"+abbr+"'");
				str += abbr + ",";
				
				for (int i = 1; i < columns.split(",").length; i++) {
//						cell = row.createCell(ncell++);
					value = rset1.getString(i) == null ? "null" : rset1.getString(i);
					if(i == 1) {	//MAPPING_ID
						value = trans_map;
						
//							cell.setCellValue("'"+value+"'");
						str += value + ",";
					}
					else if(i == 2) {	//CONTRACT_TYPE
						value = "K";
//							cell.setCellValue("'"+value+"'");
						str += value + ",";
					}
					else if(i == 12) {	//MDQ
						
						queryString2 = "SELECT ENTRY_MDQ FROM FMS_DAILY_NOM WHERE MAPPING_ID = ? AND CONT_MAPPING_ID = '1' ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, trans_map);
						rset2 = stmt2.executeQuery();
						if(rset2.next()) {
							mdq = rset2.getString(1);
						}
						rset2.close();
						stmt2.close();
						if(mdq!=null) {
							value = mdq;
						}
						else {
							queryString2 = "SELECT TOT_ENE FROM FMS_CONT_MST WHERE MAPPING_ID = ? AND CONT_AGR_TYPE = 'Parking' ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, trans_map);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								mdq = rset2.getString(1);
							}
							rset2.close();
							stmt2.close();
							
							value = mdq;
						}
						
						str += value + ",";
					}
					else if(i == 14) {	//BASE
						if(base.equals("GHV")) {
							base = "GCV";
						}
						else if(base.equals("NHV")) {
							base = "NCV";
						}
//							cell.setCellValue("'"+base+"'");
						str += base + ",";
					}
					else if(i==18) {
						if(base.equals("GCV")) {
							baseVal=gcv;
							deviding_factor=1;
						}
						else {
							baseVal=ncv;
							deviding_factor=1.11d;
						}
//							Double result1= (double) Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor));
						BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu*multiplying_factor/(baseVal*deviding_factor)));
						result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
						
						value = result1.toString();
//							value = value.format("%.2f", value);
						//cell.setCellValue("'" + value + "'");
						str += value + ",";
					}
					
					else if(i == 19) {//BASE
						if(base.equals("GHV")) {
							base = "GCV";
						}
						else if(base.equals("NHV")) {
							base = "NCV";
						}
//							cell.setCellValue("'"+base+"'");
						str += base + ",";
					}
					else if(i == 23) {
						if(base.equals("GCV")) {
							baseVal=gcv;
							deviding_factor=1;
						}
						else {
							baseVal=ncv;
							deviding_factor=1.11d;
						}
						BigDecimal result1 = BigDecimal.valueOf(Math.round(qty_mmbtu1*multiplying_factor/(baseVal*deviding_factor)));
						result1  = result1.setScale(2, RoundingMode.CEILING);
//							Double result2 = result1.doubleValue();
//							value = Double.toString(result2);
						
						value = result1.toString();
//							value = value.format("%.2f", value);
						//cell.setCellValue("'" + value + "'");
						str += value + ",";						
					}
					
					else {
//							cell.setCellValue("'"+value+"'");
						str += value + ",";
					}
					
				}
				logger.insert_data(fname_csv, str, conn);
				count++;
				logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
				
			}
			rset1.close();
			stmt1.close();
			
		}
		stmt.close();
		rset.close();
			
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_ALLOC_"+start_end_dt+".xlsx";
			
//			fileOut = new FileOutputStream(filename);  
			
//			workbook.write(fileOut);
//			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_DAILY_TRANSPORTER_ALLOC>>,,,", conn);
			
			
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
	
	
	public void FMS_GTA_SG_INV_MST() throws SQLException, IOException {
		
		function_nm = "FMS_GTA_SG_INV_MST()";
		
		try {
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_SG_INV_MST>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,TRANS_BU_UNIT,INVOICE_TYPE,"
					+ "ALLOC_QTY,DEFICIENCY_QTY,NEG_IMB_QTY,POS_IMB_QTY,UNAUTH_OVERRUN_QTY,PARKING_QTY,SIP_PAY_RATE,TRANSPORT_RATE,NEGETIVE_IMB_RATE,POSITIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,"
					+ "PARKING_RATE,TRANSMISSION_AMT,NEG_IMB_AMT,POS_IMB_AMT,UNAUTH_OVERRUN_AMT,PARKING_AMT,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,RATE_UNIT,SALE_AMT,EXCHG_RATE_CD,"
					+ "EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "INV_STATUS,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,"
					+ "FINANCIAL_YEAR,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,DEFICIENCY_AMT,SIP_PAY_FREQ,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,TCS_TDS,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SYS_INV_NO,INV_COMPONENT,FIN_SYS";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			
//			nrow = 0;
//			ncell = 0;
			count = 0;
			String org_abbr= "",fin_year="",inv_dt="",st_dt="",end_dt="",freq="",cust_cd="",inv_type="",inv_no="",dt="",month="",year="";
			double tax_amt = 0,sale_amt = 0,tax=0;
			String qty="",rate="",cmp_cd="",bill_rate="",sip_or_pay="",transport_charges="",positive_imbal="",nagative_imbal="",un_overrun="";
			String pos_qty = null, neg_qty = null, un_qty = null, park_charge="", park_qty = null,sip_qty=null,bill_no="";
			double pos_sale = 0, neg_sale = 0, un_sale = 0, park_sale = 0;
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "",cus_map="",trans_map1="",park_map="",cont_dt="";
			fname_csv = migration_setup_dir + "EXPORT/FMS_GTA_SG_INV_MST_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,GAS_DT,NOM_REV_NO,TRANSPORTER_MAP,CONT_MAP,MDQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				queryString2 = "SELECT A.MAPPING_ID, A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,"
						+ " A.CONT_CUST_CD,A.CONT_MAPPING_ID,A.PARTY_CD,TO_CHAR(A.CONT_ST_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? "
						+ "ORDER BY A.MAPPING_ID, A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,A.CONT_CUST_CD ";

				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset.getString(2));
				rset2 = stmt2.executeQuery();
				
				while(rset2.next()) {
				
					map_id = rset2.getString(1);
					agmt_no = rset2.getString(2);
					agmt_rev = rset2.getString(3);
					cont_no = rset2.getString(4);
					cont_rev = rset2.getString(5);
					cont_type = rset2.getString(6);
					cust_cd = rset2.getString(7);
					cont_map = rset2.getString(8);
					
					//CUSTOMER
					queryString1 = "SELECT A.MAPPING_ID, 'C', NULL, NULL, NULL, 'C', NULL, NULL, '1', NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
							+ "NULL,NULL,NULL,NULL,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),A.BILL_NO,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, TO_CHAR(A.BILL_PERIOD_FROM, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(A.BILL_PERIOD_TO, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DUE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "'1',NULL, NULL, NULL, NULL,'1',NULL,NULL,NULL,NULL,A.APRV_AMT,NULL,NULL,A.APRV_AMT,NULL,A.VERIFY_BY, A.VERIFY_BY, TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL,"
							+ "NULL,  NULL, NULL, NULL,"
							+ "	A.ENTER_BY, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), 'O', A.ENTER_BY, "
							+ "	TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.GROSS_TDS_AMT, '2', NULL,'01/04/2023 00:00:00' , 'TDS', A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "NULL, NULL, NULL,A.REV_NO, A.SPLIT_ID,"
							+ " A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_CUST_CD , A.CONT_AGR_TYPE, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY'), A.INV_NO "
							+ "FROM FMS_INV_BILL_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO =  ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
							+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? AND "
							+ "A.CONT_AGR_TYPE IN ('FGSA','Tender') ORDER BY A.BILL_NO,A.APRV_BY";

					    stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, map_id);
						stmt1.setString(2, agmt_no);
						stmt1.setString(3, agmt_rev);
						stmt1.setString(4, cont_no);
						stmt1.setString(5, cont_rev);  
						stmt1.setString(6, cust_cd);
						stmt1.setString(7, cont_type);
						rset1 = stmt1.executeQuery();	
						
						while (rset1.next()) {
							
							str = "";
		//					row = spreadsheet.createRow(nrow++);
		//					ncell = 0;
							abbr = rset.getString(1);
		//					cd = rset1.getString(1);
							trans_map = rset1.getString(1);
							inv_dt = rset1.getString(30);
							st_dt = rset1.getString(32);
							end_dt = rset1.getString(33);
							dt = end_dt.split(" ")[0];
							month = dt.split("/")[1];
							year = dt.split("/")[2];
							String last_date = "";
							last_date = date.getLastDateOfMonth( month, year);
							inv_no = rset1.getString(87);
							inv_no = inv_no.split("-")[2];
							cont_dt = rset2.getString(10);
							
							//System.out.println(inv_no);
							freq = "";
							
							if(rset1.getString(29).contains(" ") && !rset1.getString(29).contains(",")) {
                                bill_no = rset1.getString(29).split(" ")[0];
							}else {
								bill_no = rset1.getString(29);
							}
							map =  rset2.getString(9)+bill_no+rset1.getString(30);
//							System.out.println(map+" :: "+map1);
							if(!cus_map.contains(map) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
							(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) {									
							abbr1 = abbr;
							if (mpe.transporter_map.containsKey(abbr)) {
								org_abbr= abbr;
								abbr =mpe.transporter_map.get(abbr); 
							}
							
							if (mpe.meter_map.containsKey(org_abbr)) 
							{
								org_abbr = mpe.meter_map.get(org_abbr);
							}
							
		//					cell = row.createCell(ncell++);
		//					cell.setCellValue("'"+abbr+"'");
							str += abbr + ",";
							
							for (int i = 1; i < columns.split(",").length; i++) {
		//						cell = row.createCell(ncell++);
								value = rset1.getString(i) == null ? "null" : rset1.getString(i);
								value = value.replaceAll(" ", "");
								value = value.replaceAll(",", "-");
		                            if(i==1) {
									cont_map = rset2.getString(8);
									if(cont_map!=null) {
										value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
									}
									else {
										value = trans_map;
									}
									str += value + ",";
								}
		//						
		                            else if(i==10) { 
		                            	
		                            	String comp [] = {"43","16","12","30","13","31","32","34","44"};
		                            	sip_qty = null;
		                            	qty = null;bill_rate=null;sale_amt=0;tax=0;
		                        		un_overrun = null;
										positive_imbal= null;
										sip_or_pay= null;
										nagative_imbal= null;
										transport_charges= null;
										park_charge = null;
		
										pos_qty = null; neg_qty = null; un_qty = null;park_qty = null;
										pos_sale = 0; neg_sale = 0; un_sale = 0;park_sale = 0;
										
		                        		inv_type = "";
		                            	
		                            	for(int j = 0; j<comp.length;j++) {
		
		                            		//System.out.println(comp[j]+"Index"+j);
		                            		queryString3 = "SELECT QTY,COMPO_CD,RATE,AMT,TAX_AMT FROM FMS_COMPO_INV_DTL WHERE "
		                                			+ " MAPPING_ID = ? AND REV_NO = ? AND SPLIT_ID = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
		                                			+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? "
//		                                			+ "AND REV_DT =  TO_DATE(TO_DATE(?,'DD/MM/YYYY'),'DD-MON-YY') "
		                                			+ "AND COMPO_CD = ? AND INV_NO LIKE ? ";

//		                            		if(!rset1.getString(86).contains("[")) {
//		                            			queryString3 + "AND REV_DT =  TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
//		                        			}else {
//		                        				queryString3 += "AND INV_NO = ? ";
//		                        			}
//		                            			queryString3 += 
		                            			
		            							stmt3 = conn.prepareStatement(queryString3);
		            							stmt3.setString(1, trans_map);
		            							stmt3.setString(2, rset1.getString(78));
		            							stmt3.setString(3, rset1.getString(79));
		            							stmt3.setString(4, rset1.getString(80));
		            							stmt3.setString(5, rset1.getString(81));
		            							stmt3.setString(6, rset1.getString(82));
		            							stmt3.setString(7, rset1.getString(83));
		            							stmt3.setString(8, rset1.getString(84));
		            							stmt3.setString(9, rset1.getString(85));
//		            							stmt3.setString(10, rset1.getString(85));
		            							stmt3.setString(10, comp[j]);
		            							stmt3.setString(11, "%"+inv_no+"%");		            							
//		            							if (!rset1.getString(86).contains("[")) {
//		            								stmt3.setString(10, rset1.getString(85));   								
//												}else {
//													stmt3.setString(10, rset1.getString(86));
//												}

		            							rset3 = stmt3.executeQuery();
		            							while(rset3.next()) {
		//            								qty = rset3.getString(1);
		            								cmp_cd = rset3.getString(2);
		            								bill_rate = rset3.getString(3);
		//            								sale_amt = rset3.getDouble(4);
		            								tax = rset3.getDouble(5);		
		    
		            							if (cmp_cd!=null) {
													if (comp[j].equals("43") || comp[j].equals("16")) {
														inv_type = "TC";
														if (comp[j].equals("43")) {
															sip_or_pay = bill_rate;
															sip_qty = rset3.getString(1);
		
														} else if (comp[j].equals("16")) {
															transport_charges = bill_rate;
															qty = rset3.getString(1);
														}
														
														sale_amt += rset3.getDouble(4);
		
		
													} else if (comp[j].equals("12") || comp[j].equals("30")
															|| comp[j].equals("13") || comp[j].equals("31")
															|| comp[j].equals("32") || comp[j].equals("34")) {
														inv_type = "IC";
														if (comp[j].equals("13") || comp[j].equals("31")
																|| comp[j].equals("32")) {
															nagative_imbal = bill_rate;
															neg_qty = rset3.getString(1);
															neg_sale = rset3.getDouble(4);
		
														} else if (comp[j].equals("12") || comp[j].equals("30")) {
															positive_imbal = bill_rate;
															pos_qty = rset3.getString(1);
															pos_sale = rset3.getDouble(4);
		
														} else if (comp[j].equals("34")) {
															un_overrun = bill_rate;
															un_qty = rset3.getString(1);
															un_sale = rset3.getDouble(4);
		
														}
		
													}
													else if(comp[j].equals("44")){
														inv_type = "PC";
														park_charge = bill_rate;
														park_qty = rset3.getString(1);
														park_sale = rset3.getDouble(4);
													}
													
												}
			
		            							
		            						}
		            							stmt3.close();
		            							rset3.close();
		                            	}
		                            	
										str += inv_type + ",";
										str += qty + "," + sip_qty + "," + neg_qty + "," + pos_qty + "," + un_qty + ","+park_qty+",";
										str += sip_or_pay + "," + transport_charges + "," + nagative_imbal + ","
												+ positive_imbal + "," + un_overrun + "," + park_charge + ",";
										str += sale_amt + "," + neg_sale + "," + pos_sale + "," + un_sale
												+ "," + park_sale + ",";
		
										i += 17;
				
		                            }
		                            
								else if(i == 28) {	//INVOICE_SEQ
									queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
											+" || '-' || "
											+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
											+ "FROM DUAL";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, st_dt);
									stmt3.setString(2, end_dt);
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
										value = rset3.getString(1) == null ? "" :rset3.getString(1);
									}
									fin_year = value;
									rset3.close();
									stmt3.close();
									
		//							cell.setCellValue("'"+value+"'");
									str += value + ",";
									}
								
								else if(i == 31) {	//FREQ
									if(Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15) {									
									freq = "1";
									}
									else if(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])) {
										freq = "2";
									}
									else {
										freq = "8";
									}
								
		
									str += freq + ",";
								}
		                            
								else if(i == 32) {
									if((st_dt.split("/")[1].equals(cont_dt.split("/")[1]))  && (Integer.parseInt(st_dt.split("/")[0])<Integer.parseInt(cont_dt.split("/")[0]))) {
										st_dt = cont_dt;
									}
									else {
										st_dt=rset1.getString(32);
									}
									str  += st_dt + ",";
								}
		                            
								else if(i == 33) {
									str  += end_dt + ",";
								}
		                            
								else if(i==36) {
									double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;
		
									str += sale_amt1 + ",";
								}
		                            
								else if(i==41) {
									double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;
		
									str += sale_amt1 + ",";
								}
		                            
								else if(i==42) {
									String tax_cd = "";
									int count = 0;
									queryString3 = "SELECT B.DESCRIPTION, A.TAX_RATE FROM FMS_INV_TAX_DTL A , FMS_PRICE_MST B WHERE "
		                        			+ " A.MAPPING_ID = ? AND A.REV_NO = ? AND A.SPLIT_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
		                        			+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? "
		                        			+ "AND A.INV_NO = ? AND A.TAX_CD = B.PRICE_CD AND B.TYPE_CODE = 'T' AND A.TAX_RATE != 0 AND A.BILL_TAX_AMT != 0";
									stmt3  =conn.prepareStatement(queryString3);
									stmt3.setString(1, trans_map);
        							stmt3.setString(2, rset1.getString(78));
        							stmt3.setString(3, rset1.getString(79));
        							stmt3.setString(4, rset1.getString(80));
        							stmt3.setString(5, rset1.getString(81));
        							stmt3.setString(6, rset1.getString(82));
        							stmt3.setString(7, rset1.getString(83));
        							stmt3.setString(8, rset1.getString(84));
        							stmt3.setString(9, rset1.getString(85));
//        							stmt3.setString(10, rset1.getString(85));
        							//stmt3.setString(11, "%"+st_dt.split("/")[0]+end_dt.split("/")[0]+end_dt.split("/")[1]+end_dt.split(" ")[0].split("/")[2]+"%");
        							stmt3.setString(10, rset1.getString(87));
        							rset3 = stmt3.executeQuery();					
        							while(rset3.next()) {
        								count++;
        								if(count>1) 
        									tax_cd +="@ ";
//        								tax_cd += rset3.getString(1);
        								tax_cd += (rset3.getString(1)+" "+rset3.getString(2)+"%");
        							}
        							
//        							tax_cd = tax_cd.substring(0, tax_cd.length()-2);
        							rset3.close();
        							stmt3.close();
        							str += tax_cd + ",";
        							
								}    
		                            
								else if(i == 50) {	//CHECKED_FLAG
									if(!value.equals("null")) {
										value = "Y";
									}
									else {
										value = "null";
									}
									str += value + ",";
								}
								
//								else if(i == 53) {	//AUTHORIZED_FLAG
//									if(!value.equals("null")) {
//										value = "Y";
//									}
//									else {
//										value = "null";
//									}
//									str += value + ",";
//								}
//								
//								else if(i == 56) {	//APPROVED_FLAG
//									if(!value.equals("null")) {
//										value = "A";
//									}
//									else {
//										value = "null";
//									}
//									str += value + ",";
//								}
								
								else if(i == 61) {	//FINANCIAL_YEAR
									value = fin_year;
									
									str += value + ",";
								}
		                           
								else if(i==69) {    
									str += "TDS 2%" + ",";
								}
								else if(i == 76) {
									if(sip_or_pay!=null && transport_charges!=null) {
										value = "TP-SP";
									}
									else if(sip_or_pay!=null && transport_charges == null) {
										value  = "SP";
									}
									else if(transport_charges!=null && sip_or_pay == null) {
										value  = "TP";
									}
									else if(nagative_imbal != null && positive_imbal != null && un_overrun != null) {
										value  = "NI-PI-UR";
									}
									else if(nagative_imbal == null && positive_imbal != null && un_overrun != null) {
										value  = "PI-UR";
									}
									else if(nagative_imbal != null && positive_imbal == null && un_overrun != null) {
										value  = "NI-UR";
									}
									else if(nagative_imbal != null && positive_imbal != null && un_overrun == null) {
										value  = "NI-PI";
									}
									else if(nagative_imbal == null && positive_imbal == null && un_overrun != null) {
										value  = "UR";
									}
									else if(nagative_imbal != null && positive_imbal == null && un_overrun == null) {
										value  = "NI";
									}
									else if(nagative_imbal == null && positive_imbal != null && un_overrun == null) {
										value  = "PI";
									}
									else if(park_charge != null){
										value = "PC";
									}
									str += value + ",";
								}    
		                            
								else if(i==77) {//FIN_SYS
									value = rset1.getString(72);
									if(value!=null) {
										value = "S";
									}
									str += value + ",";
								}
		  
								else {
									str += value + ",";
								}
								
		                            
		                            
							}
							logger.insert_data(fname_csv, str, conn);
							count++;
							cus_map += (map+",");
		//					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
							}
//							else {
//								cus_map += (map);
//							}
						}
						rset1.close();
						stmt1.close();
							
							
				//TRANSPORTER
						queryString1 = "SELECT A.MAPPING_ID, 'R', NULL, NULL, NULL, 'R', NULL, NULL, '1', NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
								+ "NULL,NULL,NULL,NULL,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),A.BILL_NO,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, TO_CHAR(A.BILL_PERIOD_FROM, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "TO_CHAR(A.BILL_PERIOD_TO, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DUE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "'1',NULL, NULL, NULL, NULL,'1',NULL,NULL,NULL,NULL,A.APRV_AMT,NULL,NULL,A.APRV_AMT,NULL,A.VERIFY_BY, A.VERIFY_BY, TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL,"
								+ "NULL,  NULL, NULL, NULL,"
								+ "	A.ENTER_BY, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), 'O', A.ENTER_BY, "
								+ "	TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.GROSS_TDS_AMT, '2', NULL,'01/04/2023 00:00:00' , 'TDS', A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "NULL, NULL,NULL, A.REV_NO, A.SPLIT_ID,"
								+ " A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_CUST_CD , A.CONT_AGR_TYPE, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY'), A.INV_NO "
								+ "FROM FMS_INV_BILL_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO =  ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
								+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? AND "
//								+ "A.CONT_AGR_TYPE  = 'Trans' AND A.APRV_BY IS NOT NULL ORDER BY A.BILL_NO";
								+ "A.CONT_AGR_TYPE  = 'Trans' ORDER BY A.BILL_NO,A.APRV_BY";

					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, map_id);
					stmt1.setString(2, agmt_no);
					stmt1.setString(3, agmt_rev);
					stmt1.setString(4, cont_no);
					stmt1.setString(5, cont_rev);  
					stmt1.setString(6, cust_cd);
					stmt1.setString(7, cont_type);
					rset1 = stmt1.executeQuery();	
					
					while (rset1.next()) {
						str = "";
		//				row = spreadsheet.createRow(nrow++);
		//				ncell = 0;
						abbr = rset.getString(1);
		//				cd = rset1.getString(1);
						trans_map = rset1.getString(1);
						inv_dt = rset1.getString(30);
						st_dt = rset1.getString(32);
						end_dt = rset1.getString(33);
						dt = end_dt.split(" ")[0];
						month = dt.split("/")[1];
						year = dt.split("/")[2];
						String last_date = "";
						last_date = date.getLastDateOfMonth( month, year);
						inv_no = rset1.getString(87);
						inv_no = inv_no.split("-")[2];
						cont_dt=rset2.getString(10);
						freq = "";
						if(rset1.getString(29).contains(" ") && !rset1.getString(29).contains(",")) {
                            bill_no = rset1.getString(29).split(" ")[0];
						}else {
							bill_no = rset1.getString(29);
						}
						map =  rset2.getString(9)+bill_no+rset1.getString(30);						
						if(!trans_map1.contains(map) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
								(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) {	
						
						abbr1 = abbr;
						if (mpe.transporter_map.containsKey(abbr)) {
							org_abbr= abbr;
							abbr =mpe.transporter_map.get(abbr); 
						}
						
						if (mpe.meter_map.containsKey(org_abbr)) 
						{
							org_abbr = mpe.meter_map.get(org_abbr);
						}
						
						
		//				cell = row.createCell(ncell++);
		//				cell.setCellValue("'"+abbr+"'");
						str += abbr + ",";
						
						for (int i = 1; i < columns.split(",").length; i++) {
		//					cell = row.createCell(ncell++);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i);
							value = value.replaceAll(" ", "");
							value = value.replaceAll(",", "-");
		                        if(i==1) {
								value = trans_map;
								str += value + ",";
							    }
		//					
		                        else if(i==10) { 
	                            	
		                        	String comp [] = {"43","16","12","30","13","31","32","34","44"};
	                            	sip_qty = null;
	                            	qty = null;bill_rate=null;sale_amt=0;tax=0;
	                        		un_overrun = null;
									positive_imbal= null;
									sip_or_pay= null;
									nagative_imbal= null;
									transport_charges= null;
									park_charge = null;
	
									pos_qty = null; neg_qty = null; un_qty = null;park_qty = null;
									pos_sale = 0; neg_sale = 0; un_sale = 0;park_sale = 0;
									
	                        		inv_type = "";
	                            	
	                            	for(int j = 0; j<comp.length;j++) {
	
	                            		//System.out.println(comp[j]+"Index"+j);
	                            		queryString3 = "SELECT QTY,COMPO_CD,RATE,AMT,TAX_AMT FROM FMS_COMPO_INV_DTL WHERE "
	                                			+ " MAPPING_ID = ? AND REV_NO = ? AND SPLIT_ID = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
	                                			+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? "
//	                                			+ "AND REV_DT =  TO_DATE(TO_DATE(?,'DD/MM/YYYY'),'DD-MON-YY') "
	                                			+ "AND COMPO_CD = ? AND INV_NO LIKE ? ";

//	                            		if(!rset1.getString(86).contains("[")) {
//	                            			queryString3 + "AND REV_DT =  TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
//	                        			}else {
//	                        				queryString3 += "AND INV_NO = ? ";
//	                        			}
//	                            			queryString3 += 
	                            			
	            							stmt3 = conn.prepareStatement(queryString3);
	            							stmt3.setString(1, trans_map);
	            							stmt3.setString(2, rset1.getString(78));
	            							stmt3.setString(3, rset1.getString(79));
	            							stmt3.setString(4, rset1.getString(80));
	            							stmt3.setString(5, rset1.getString(81));
	            							stmt3.setString(6, rset1.getString(82));
	            							stmt3.setString(7, rset1.getString(83));
	            							stmt3.setString(8, rset1.getString(84));
	            							stmt3.setString(9, rset1.getString(85));
//	            							stmt3.setString(10, rset1.getString(85));
	            							stmt3.setString(10, comp[j]);
	            							stmt3.setString(11, "%"+inv_no+"%");		            							
//	            							if (!rset1.getString(86).contains("[")) {
//	            										            								
//											}else {
//												stmt3.setString(10, rset1.getString(86));
//											}

	            							rset3 = stmt3.executeQuery();
	            							while(rset3.next()) {
	//            								qty = rset3.getString(1);
	            								cmp_cd = rset3.getString(2);
	            								bill_rate = rset3.getString(3);
	//            								sale_amt = rset3.getDouble(4);
	            								tax = rset3.getDouble(5);		
	    
	            							if (cmp_cd!=null) {
												if (comp[j].equals("43") || comp[j].equals("16")) {
													inv_type = "TC";
													if (comp[j].equals("43")) {
														sip_or_pay = bill_rate;
														sip_qty = rset3.getString(1);
	
													} else if (comp[j].equals("16")) {
														transport_charges = bill_rate;
														qty = rset3.getString(1);
													}
													
													sale_amt += rset3.getDouble(4);
	
	
												} else if (comp[j].equals("12") || comp[j].equals("30")
														|| comp[j].equals("13") || comp[j].equals("31")
														|| comp[j].equals("32") || comp[j].equals("34")) {
													inv_type = "IC";
													if (comp[j].equals("13") || comp[j].equals("31")
															|| comp[j].equals("32")) {
														nagative_imbal = bill_rate;
														neg_qty = rset3.getString(1);
														neg_sale = rset3.getDouble(4);
	
													} else if (comp[j].equals("12") || comp[j].equals("30")) {
														positive_imbal = bill_rate;
														pos_qty = rset3.getString(1);
														pos_sale = rset3.getDouble(4);
	
													} else if (comp[j].equals("34")) {
														un_overrun = bill_rate;
														un_qty = rset3.getString(1);
														un_sale = rset3.getDouble(4);
	
													}
	
												}
												else if(comp[j].equals("44")){
													inv_type = "PC";
													park_charge = bill_rate;
													park_qty = rset3.getString(1);
													park_sale = rset3.getDouble(4);
												}
												
											}
		
	            							
	            						}
	            							stmt3.close();
	            							rset3.close();
	                            	}
	                            	
									str += inv_type + ",";
									str += qty + "," + sip_qty + "," + neg_qty + "," + pos_qty + "," + un_qty + ","+park_qty+",";
									str += sip_or_pay + "," + transport_charges + "," + nagative_imbal + ","
											+ positive_imbal + "," + un_overrun + "," + park_charge + ",";
									str += sale_amt + "," + neg_sale + "," + pos_sale + "," + un_sale
											+ "," + park_sale + ",";
	
									i += 17;
			
	                            }
		                        
							else if(i == 28) {	//INVOICE_SEQ
								queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
										+" || '-' || "
										+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
										+ "FROM DUAL";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, st_dt);
								stmt3.setString(2, end_dt);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1) == null ? "" :rset3.getString(1);
								}
								fin_year = value;
								rset3.close();
								stmt3.close();
								
		//						cell.setCellValue("'"+value+"'");
								str += value + ",";
								}
							
							else if(i == 31) {	//FREQ
								if(Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15) {									
								freq = "1";
								}
								else if(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])) {
									freq = "2";
								}
								else {
									freq = "8";
								}
							
	
								str += freq + ",";
							}
		                        
							else if(i == 32) {
								if((st_dt.split("/")[1].equals(cont_dt.split("/")[1]))  && (Integer.parseInt(st_dt.split("/")[0])<Integer.parseInt(cont_dt.split("/")[0]))) {
									st_dt = cont_dt;
								}
								else {
									st_dt=rset1.getString(32);
								}
								str  += st_dt + ",";
							}
							else if(i == 33) {
								str  += end_dt + ",";
							}
		                        
							else if(i==36) {
								double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;
	
								str += sale_amt1 + ",";
							}
	                            
							else if(i==41) {
								double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;
	
								str += sale_amt1 + ",";
							}
							else if(i==42) {
								String tax_cd = "";
								int count = 0;
								queryString3 = "SELECT B.DESCRIPTION, A.TAX_RATE FROM FMS_INV_TAX_DTL A , FMS_PRICE_MST B WHERE "
	                        			+ " A.MAPPING_ID = ? AND A.REV_NO = ? AND A.SPLIT_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
	                        			+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? "
	                        			+ "AND A.INV_NO = ? AND A.TAX_CD = B.PRICE_CD AND B.TYPE_CODE = 'T' AND A.TAX_RATE != 0 AND A.BILL_TAX_AMT != 0";
								stmt3  =conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_map);
    							stmt3.setString(2, rset1.getString(78));
    							stmt3.setString(3, rset1.getString(79));
    							stmt3.setString(4, rset1.getString(80));
    							stmt3.setString(5, rset1.getString(81));
    							stmt3.setString(6, rset1.getString(82));
    							stmt3.setString(7, rset1.getString(83));
    							stmt3.setString(8, rset1.getString(84));
    							stmt3.setString(9, rset1.getString(85));
//    							stmt3.setString(10, rset1.getString(85));
    							//stmt3.setString(11, "%"+st_dt.split("/")[0]+end_dt.split("/")[0]+end_dt.split("/")[1]+end_dt.split(" ")[0].split("/")[2]+"%");
    							stmt3.setString(10, rset1.getString(87));
    							rset3 = stmt3.executeQuery();					
    							while(rset3.next()) {
    								count++;
    								if(count>1) 
    									tax_cd +="@ ";
//    								tax_cd += rset3.getString(1);
    								tax_cd += (rset3.getString(1)+" "+rset3.getString(2)+"%");
    							}
    							
//    							tax_cd = tax_cd.substring(0, tax_cd.length()-2);
    							rset3.close();
    							stmt3.close();
    							str += tax_cd + ",";
    							
							}    
							else if(i == 50) {	//CHECKED_FLAG
								if(!value.equals("null")) {
									value = "Y";
								}
								else {
									value = "null";
								}
								str += value + ",";
							}
							
//							else if(i == 53) {	//AUTHORIZED_FLAG
//								if(!value.equals("null")) {
//									value = "Y";
//								}
//								else {
//									value = "null";
//								}
//								str += value + ",";
//							}
//							
//							else if(i == 56) {	//APPROVED_FLAG
//								if(!value.equals("null")) {
//									value = "A";
//								}
//								else {
//									value = "null";
//								}
//								str += value + ",";
//							}
							
							else if(i == 61) {	//FINANCIAL_YEAR
								value = fin_year;
								
								str += value + ",";
							}
		                        
							else if(i==69) {    
								str += "TDS 2%" + ",";
							}
		                        
							else if(i == 76) {
								
								if(sip_or_pay!=null && transport_charges!=null) {
									value = "TP-SP";
								}
								else if(sip_or_pay!=null && transport_charges == null) {
									value  = "SP";
								}
								else if(transport_charges!=null && sip_or_pay == null) {
									value  = "TP";
								}
								else if(nagative_imbal != null && positive_imbal != null && un_overrun != null) {
									value  = "NI-PI-UR";
								}
								else if(nagative_imbal == null && positive_imbal != null && un_overrun != null) {
									value  = "PI-UR";
								}
								else if(nagative_imbal != null && positive_imbal == null && un_overrun != null) {
									value  = "NI-UR";
								}
								else if(nagative_imbal != null && positive_imbal != null && un_overrun == null) {
									value  = "NI-PI";
								}
								else if(nagative_imbal == null && positive_imbal == null && un_overrun != null) {
									value  = "UR";
								}
								else if(nagative_imbal != null && positive_imbal == null && un_overrun == null) {
									value  = "NI";
								}
								else if(nagative_imbal == null && positive_imbal != null && un_overrun == null) {
									value  = "PI";
								}
								else if(park_charge != null){
									value = "PC";
								}
								str += value + ",";
							}    
		
							else if(i==77) {//FIN_SYS
								value = rset1.getString(72);
								if(value!=null) {
									value = "S";
								}
								str += value + ",";
							}    
		                        
							else {
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						trans_map1 +=(map+",");
		//				logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
						}
					}
					rset1.close();
					stmt1.close();
						
					//Parking
					queryString1 = "SELECT A.MAPPING_ID, 'K', NULL, NULL, NULL, 'K', NULL, NULL, '1', NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
							+ "NULL,NULL,NULL,NULL,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),A.BILL_NO,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, TO_CHAR(A.BILL_PERIOD_FROM, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(A.BILL_PERIOD_TO, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DUE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "'1',NULL, NULL, NULL, NULL,'1',NULL,NULL,NULL,NULL,A.APRV_AMT,NULL,NULL,A.APRV_AMT,NULL,A.VERIFY_BY, A.VERIFY_BY, TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, NULL,"
							+ "NULL,  NULL, NULL, NULL,"
							+ "	A.ENTER_BY, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), 'O', A.ENTER_BY, "
							+ "	TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.GROSS_TDS_AMT, '2', NULL,'01/04/2023 00:00:00', 'TDS', A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "NULL, NULL, NULL,A.REV_NO, A.SPLIT_ID,"
							+ " A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_CUST_CD , A.CONT_AGR_TYPE, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY'), A.INV_NO "
							+ "FROM FMS_INV_BILL_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO =  ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
							+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? AND "
							+ "A.CONT_AGR_TYPE  = 'Parking' ORDER BY A.BILL_NO,A.APRV_BY";

				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, map_id);
				stmt1.setString(2, agmt_no);
				stmt1.setString(3, agmt_rev);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, cont_rev);  
				stmt1.setString(6, cust_cd);
				stmt1.setString(7, cont_type);
				rset1 = stmt1.executeQuery();	
				
				while (rset1.next()) {
					str = "";
		//			row = spreadsheet.createRow(nrow++);
		//			ncell = 0;
					abbr = rset.getString(1);
		//			cd = rset1.getString(1);
					trans_map = rset1.getString(1);
					inv_dt = rset1.getString(30);
					st_dt = rset1.getString(32);
					end_dt = rset1.getString(33);
					dt = end_dt.split(" ")[0];
					month = dt.split("/")[1];
					year = dt.split("/")[2];
					String last_date = "";
					last_date = date.getLastDateOfMonth( month, year);
					inv_no = rset1.getString(87);
					inv_no = inv_no.split("-")[2];
					
					freq = "";
					
					cont_dt=rset2.getString(10);
					if(rset1.getString(29).contains(" ") && !rset1.getString(29).contains(",")) {
                        bill_no = rset1.getString(29).split(" ")[0];
					}else {
						bill_no = rset1.getString(29);
					}
					map =  rset2.getString(9)+bill_no+rset1.getString(30);
					if(!park_map.contains(map) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
							(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) {
					
					abbr1 = abbr;
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
						abbr =mpe.transporter_map.get(abbr); 
					}
					
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					
					
		//			cell = row.createCell(ncell++);
		//			cell.setCellValue("'"+abbr+"'");
					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
		//				cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						value = value.replaceAll(" ", "");
						value = value.replaceAll(",", "-");
		                    if(i==1) {
							value = trans_map;
							str += value + ",";
						    }
		//				
		                    else if(i==10) { 
                            	
		                    	String comp [] = {"43","16","12","30","13","31","32","34","44"};
                            	sip_qty = null;
                            	qty = null;bill_rate=null;sale_amt=0;tax=0;
                        		un_overrun = null;
								positive_imbal= null;
								sip_or_pay= null;
								nagative_imbal= null;
								transport_charges= null;
								park_charge = null;

								pos_qty = null; neg_qty = null; un_qty = null;park_qty = null;
								pos_sale = 0; neg_sale = 0; un_sale = 0;park_sale = 0;
								
                        		inv_type = "";
                            	
                            	for(int j = 0; j<comp.length;j++) {

                            		//System.out.println(comp[j]+"Index"+j);
                            		queryString3 = "SELECT QTY,COMPO_CD,RATE,AMT,TAX_AMT FROM FMS_COMPO_INV_DTL WHERE "
                                			+ " MAPPING_ID = ? AND REV_NO = ? AND SPLIT_ID = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
                                			+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? "
//                                			+ "AND REV_DT =  TO_DATE(TO_DATE(?,'DD/MM/YYYY'),'DD-MON-YY') "
                                			+ "AND COMPO_CD = ? AND INV_NO LIKE ? ";

//                            		if(!rset1.getString(86).contains("[")) {
//                            			queryString3 + "AND REV_DT =  TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
//                        			}else {
//                        				queryString3 += "AND INV_NO = ? ";
//                        			}
//                            			queryString3 += 
                            			
            							stmt3 = conn.prepareStatement(queryString3);
            							stmt3.setString(1, trans_map);
            							stmt3.setString(2, rset1.getString(78));
            							stmt3.setString(3, rset1.getString(79));
            							stmt3.setString(4, rset1.getString(80));
            							stmt3.setString(5, rset1.getString(81));
            							stmt3.setString(6, rset1.getString(82));
            							stmt3.setString(7, rset1.getString(83));
            							stmt3.setString(8, rset1.getString(84));
            							stmt3.setString(9, rset1.getString(85));
//            							stmt3.setString(10, rset1.getString(85));
            							stmt3.setString(10, comp[j]);
            							stmt3.setString(11, "%"+inv_no+"%");		            							
//            							if (!rset1.getString(86).contains("[")) {
//            										            								
//										}else {
//											stmt3.setString(10, rset1.getString(86));
//										}

            							rset3 = stmt3.executeQuery();
            							while(rset3.next()) {
//            								qty = rset3.getString(1);
            								cmp_cd = rset3.getString(2);
            								bill_rate = rset3.getString(3);
//            								sale_amt = rset3.getDouble(4);
            								tax = rset3.getDouble(5);		
    
            							if (cmp_cd!=null) {
											if (comp[j].equals("43") || comp[j].equals("16")) {
												inv_type = "TC";
												if (comp[j].equals("43")) {
													sip_or_pay = bill_rate;
													sip_qty = rset3.getString(1);

												} else if (comp[j].equals("16")) {
													transport_charges = bill_rate;
													qty = rset3.getString(1);
												}
												
												sale_amt += rset3.getDouble(4);


											} else if (comp[j].equals("12") || comp[j].equals("30")
													|| comp[j].equals("13") || comp[j].equals("31")
													|| comp[j].equals("32") || comp[j].equals("34")) {
												inv_type = "IC";
												if (comp[j].equals("13") || comp[j].equals("31")
														|| comp[j].equals("32")) {
													nagative_imbal = bill_rate;
													neg_qty = rset3.getString(1);
													neg_sale = rset3.getDouble(4);

												} else if (comp[j].equals("12") || comp[j].equals("30")) {
													positive_imbal = bill_rate;
													pos_qty = rset3.getString(1);
													pos_sale = rset3.getDouble(4);

												} else if (comp[j].equals("34")) {
													un_overrun = bill_rate;
													un_qty = rset3.getString(1);
													un_sale = rset3.getDouble(4);

												}

											}
											else if(comp[j].equals("44")){
												inv_type = "PC";
												park_charge = bill_rate;
												park_qty = rset3.getString(1);
												park_sale = rset3.getDouble(4);
											}
											
										}
	
            							
            						}
            							stmt3.close();
            							rset3.close();
                            	}
                            	
								str += inv_type + ",";
								str += qty + "," + sip_qty + "," + neg_qty + "," + pos_qty + "," + un_qty + ","+park_qty+",";
								str += sip_or_pay + "," + transport_charges + "," + nagative_imbal + ","
										+ positive_imbal + "," + un_overrun + "," + park_charge + ",";
								str += sale_amt + "," + neg_sale + "," + pos_sale + "," + un_sale
										+ "," + park_sale + ",";

								i += 17;
		
                            }
		                    
						else if(i == 28) {	//INVOICE_SEQ
							queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
									+" || '-' || "
									+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
									+ "FROM DUAL";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, st_dt);
							stmt3.setString(2, end_dt);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								value = rset3.getString(1) == null ? "" :rset3.getString(1);
							}
							fin_year = value;
							rset3.close();
							stmt3.close();
							
		//					cell.setCellValue("'"+value+"'");
							str += value + ",";
							}
						
						else if(i == 31) {	//FREQ
							if(Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15) {									
							freq = "1";
							}
							else if(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0])  == Integer.parseInt(last_date.split("/")[0])) {
								freq = "2";
							}
							else {
								freq = "8";
							}
							str += freq + ",";
						}
		                    
						else if(i == 32) {
							if((st_dt.split("/")[1].equals(cont_dt.split("/")[1]))  && (Integer.parseInt(st_dt.split("/")[0])<Integer.parseInt(cont_dt.split("/")[0]))) {
								st_dt = cont_dt;
							}
							else {
								st_dt=rset1.getString(32);
							}
							str  += st_dt + ",";
						}
						else if(i == 33) {
							str  += end_dt + ",";
						}
						else if(i==36) {
							double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;

							str += sale_amt1 + ",";
						}
                            
						else if(i==41) {
							double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;

							str += sale_amt1 + ",";
						}
						else if(i==42) {
							String tax_cd = "";
							int count = 0;
							queryString3 = "SELECT B.DESCRIPTION, A.TAX_RATE FROM FMS_INV_TAX_DTL A , FMS_PRICE_MST B WHERE "
                        			+ " A.MAPPING_ID = ? AND A.REV_NO = ? AND A.SPLIT_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
                        			+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? "
                        			+ "AND A.INV_NO = ? AND A.TAX_CD = B.PRICE_CD AND B.TYPE_CODE = 'T' AND A.TAX_RATE != 0 AND A.BILL_TAX_AMT != 0";
							stmt3  =conn.prepareStatement(queryString3);
							stmt3.setString(1, trans_map);
							stmt3.setString(2, rset1.getString(78));
							stmt3.setString(3, rset1.getString(79));
							stmt3.setString(4, rset1.getString(80));
							stmt3.setString(5, rset1.getString(81));
							stmt3.setString(6, rset1.getString(82));
							stmt3.setString(7, rset1.getString(83));
							stmt3.setString(8, rset1.getString(84));
							stmt3.setString(9, rset1.getString(85));
//							stmt3.setString(10, rset1.getString(85));
							//stmt3.setString(11, "%"+st_dt.split("/")[0]+end_dt.split("/")[0]+end_dt.split("/")[1]+end_dt.split(" ")[0].split("/")[2]+"%");
							stmt3.setString(10, rset1.getString(87));
							rset3 = stmt3.executeQuery();					
							while(rset3.next()) {
								count++;
								if(count>1) 
									tax_cd +="@ ";
//								tax_cd += rset3.getString(1);
								tax_cd += (rset3.getString(1)+" "+rset3.getString(2)+"%");
							}
							
//							tax_cd = tax_cd.substring(0, tax_cd.length()-2);
							rset3.close();
							stmt3.close();
							str += tax_cd + ",";
							
						}    
						else if(i == 50) {	//CHECKED_FLAG
							if(!value.equals("null")) {
								value = "Y";
							}
							else {
								value = "null";
							}
							str += value + ",";
						}
						
//						else if(i == 53) {	//AUTHORIZED_FLAG
//							if(!value.equals("null")) {
//								value = "Y";
//							}
//							else {
//								value = "null";
//							}
//							str += value + ",";
//						}
//						
//						else if(i == 56) {	//APPROVED_FLAG
//							if(!value.equals("null")) {
//								value = "A";
//							}
//							else {
//								value = "null";
//							}
//							str += value + ",";
//						}
						
						else if(i == 61) {	//FINANCIAL_YEAR
							value = fin_year;
							
							str += value + ",";
						}
		                    
						else if(i==69) {    
							str += "TDS 2%" + ",";
						}    
		                    
						else if(i == 76) {
							
							if(sip_or_pay!=null && transport_charges!=null) {
								value = "TP-SP";
							}
							else if(sip_or_pay!=null && transport_charges == null) {
								value  = "SP";
							}
							else if(transport_charges!=null && sip_or_pay == null) {
								value  = "TP";
							}
							else if(nagative_imbal != null && positive_imbal != null && un_overrun != null) {
								value  = "NI-PI-UR";
							}
							else if(nagative_imbal == null && positive_imbal != null && un_overrun != null) {
								value  = "PI-UR";
							}
							else if(nagative_imbal != null && positive_imbal == null && un_overrun != null) {
								value  = "NI-UR";
							}
							else if(nagative_imbal != null && positive_imbal != null && un_overrun == null) {
								value  = "NI-PI";
							}
							else if(nagative_imbal == null && positive_imbal == null && un_overrun != null) {
								value  = "UR";
							}
							else if(nagative_imbal != null && positive_imbal == null && un_overrun == null) {
								value  = "NI";
							}
							else if(nagative_imbal == null && positive_imbal != null && un_overrun == null) {
								value  = "PI";
							}
							else if(park_charge != null){
								value = "PC";
							}
							str += value + ",";
						}    
		
						else if(i==77) {//FIN_SYS
							value = rset1.getString(72);
							if(value!=null) {
								value = "S";
							}
							str += value + ",";
						}
						
						else {
							str += value + ",";
						}
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					park_map +=(map+",");
		//			logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
					}
				}
				rset1.close();
				stmt1.close();
			
				}
				stmt2.close();
				rset2.close();
			}
			stmt.close();
			rset.close();
			
//			filename = migration_setup_dir + "EXPORT/FMS_DAILY_TRANSPORTER_ALLOC_"+start_end_dt+".xlsx";
			
//			fileOut = new FileOutputStream(filename);  
			
//			workbook.write(fileOut);
//			fileOut.close(); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_SG_INV_MST>>,,,", conn);
			
			
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
	
//FMS_GTA_PG_INV_MST
	public void FMS_GTA_PG_INV_MST() throws SQLException, IOException {
		
		function_nm = "FMS_GTA_PG_INV_MST()";
		
		try {
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_PG_INV_MST>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,TRANS_BU_UNIT,INVOICE_TYPE,"
					+ "ALLOC_QTY,DEFICIENCY_QTY,NEG_IMB_QTY,POS_IMB_QTY,UNAUTH_OVERRUN_QTY,PARKING_QTY,SIP_PAY_RATE,TRANSPORT_RATE,NEGETIVE_IMB_RATE,POSITIVE_IMB_RATE,UNAUTH_OVERRUN_RATE,"
					+ "PARKING_RATE,TRANSMISSION_AMT,NEG_IMB_AMT,POS_IMB_AMT,UNAUTH_OVERRUN_AMT,PARKING_AMT,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,RATE_UNIT,SALE_AMT,EXCHG_RATE_CD,"
					+ "EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "INV_STATUS,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,"
					+ "FINANCIAL_YEAR,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,DEFICIENCY_AMT,SIP_PAY_FREQ,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,TCS_TDS,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SYS_INV_NO,INV_COMPONENT,FIN_SYS";
			
//			workbook = new XSSFWorkbook();
//			spreadsheet = workbook.createSheet("Sheet 1");
			
//			nrow = 0;
//			ncell = 0;
			count = 0;
			String org_abbr= "",fin_year="",inv_dt="",st_dt="",end_dt="",freq="",cust_cd="",inv_type="",inv_no="",month="",year="",dt="";
			double tax_amt = 0,sale_amt = 0,tax=0;
			String qty="",rate="",cmp_cd="",bill_rate="",sip_or_pay="",transport_charges="",positive_imbal="",nagative_imbal="",un_overrun="";
			String pos_qty = null, neg_qty = null, un_qty = null, park_charge="", park_qty = null,sip_qty=null;
			double pos_sale = 0, neg_sale = 0, un_sale = 0, park_sale = 0;
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "",cus_map="",trans_map1="",bill_no="",park_map="",cont_dt="";
			fname_csv = migration_setup_dir + "EXPORT/FMS_GTA_PG_INV_MST_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,GAS_DT,NOM_REV_NO,TRANSPORTER_MAP,CONT_MAP,MDQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				queryString2 = "SELECT A.MAPPING_ID, A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,"
						+ " A.CONT_CUST_CD,A.CONT_MAPPING_ID,A.PARTY_CD,TO_CHAR(A.CONT_ST_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? "
						+ "ORDER BY A.MAPPING_ID, A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,A.CONT_CUST_CD ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset.getString(2));
				rset2 = stmt2.executeQuery();
				
				while(rset2.next()) {
				
					map_id = rset2.getString(1);
					agmt_no = rset2.getString(2);
					agmt_rev = rset2.getString(3);
					cont_no = rset2.getString(4);
					cont_rev = rset2.getString(5);
					cont_type = rset2.getString(6);
					cust_cd = rset2.getString(7);
					cont_map = rset2.getString(8);
					
					//CUSTOMER
					
						queryString1 = "SELECT A.MAPPING_ID, 'C', NULL, NULL, NULL, 'C', NULL, NULL, '1', NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
									+ "NULL,NULL,NULL,NULL,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),A.BILL_NO,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, TO_CHAR(A.BILL_PERIOD_FROM, 'DD/MM/YYYY HH24:MI:SS'),"
									+ "TO_CHAR(A.BILL_PERIOD_TO, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DUE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
									+ "'1',NULL, NULL, NULL, NULL,'1',NULL,NULL,NULL,NULL,A.BILL_AMT,NULL,NULL,A.BILL_AMT,NULL,A.VERIFY_BY, A.VERIFY_BY, TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), A.VERIFY_BY, A.VERIFY_BY,"
									+ "TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'),  A.APRV_BY, A.APRV_BY, TO_CHAR(A.APRV_DT, 'DD/MM/YYYY HH24:MI:SS'),"
									+ "	A.ENTER_BY, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), 'O', A.ENTER_BY, "
									+ "	TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.GROSS_TDS_AMT, '2', NULL,'01/04/2023 00:00:00', 'TDS', A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
									+ "NULL, NULL, NULL,A.REV_NO, A.SPLIT_ID,"
									+ " A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_CUST_CD , A.CONT_AGR_TYPE, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY'), A.INV_NO "
									+ "FROM FMS_INV_BILL_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO =  ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
									+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? AND "
//									+ "A.CONT_AGR_TYPE IN ('FGSA','Tender') AND A.APRV_BY IS NOT NULL ORDER BY A.BILL_NO ";
									+ "A.CONT_AGR_TYPE IN ('FGSA','Tender') ORDER BY A.BILL_NO,A.APRV_BY ";

						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, map_id);
						stmt1.setString(2, agmt_no);
						stmt1.setString(3, agmt_rev);
						stmt1.setString(4, cont_no);
						stmt1.setString(5, cont_rev);  
						stmt1.setString(6, cust_cd);
						stmt1.setString(7, cont_type);
						rset1 = stmt1.executeQuery();	
						
						while (rset1.next()) {
							str = "";
		//					row = spreadsheet.createRow(nrow++);
		//					ncell = 0;
							abbr = rset.getString(1);
		//					cd = rset1.getString(1);
							trans_map = rset1.getString(1);
							inv_dt = rset1.getString(30);
							st_dt = rset1.getString(32);
							end_dt = rset1.getString(33);
							dt = end_dt.split(" ")[0];
							month = dt.split("/")[1];
							year = dt.split("/")[2];
							String last_date = "";
							last_date = date.getLastDateOfMonth( month, year);
							inv_no = rset1.getString(87);
							inv_no = inv_no.split("-")[2];
							freq = "";
							cont_dt = rset2.getString(10);
							if(rset1.getString(29).contains(" ") && !rset1.getString(29).contains(",")) {
                                bill_no = rset1.getString(29).split(" ")[0];
							}else {
								bill_no = rset1.getString(29);
							}
							map =  rset2.getString(9)+bill_no+rset1.getString(30);							
							if(!cus_map.contains(map) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
									(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) {	
							
							abbr1 = abbr;
							if (mpe.transporter_map.containsKey(abbr)) {
								org_abbr= abbr;
								abbr =mpe.transporter_map.get(abbr); 
							}
							
							if (mpe.meter_map.containsKey(org_abbr)) 
							{
								org_abbr = mpe.meter_map.get(org_abbr);
							}
							
							
		//					cell = row.createCell(ncell++);
		//					cell.setCellValue("'"+abbr+"'");
							str += abbr + ",";
							
							for (int i = 1; i < columns.split(",").length; i++) {
		//						cell = row.createCell(ncell++);
								value = rset1.getString(i) == null ? "null" : rset1.getString(i);
								value = value.replaceAll(" ", "");
								value = value.replaceAll(",", "-");
		                            if(i==1) {
									cont_map = rset2.getString(8);
									if(cont_map!=null) {
										value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
									}
									else {
										value = trans_map;
									}
									str += value + ",";
								}
		//						
                                  else if(i==10) { 
		                            	
                                	  String comp [] = {"43","16","12","30","13","31","32","34","44"};
		                            	sip_qty = null;
		                            	qty = null;bill_rate=null;sale_amt=0;tax=0;
		                        		un_overrun = null;
										positive_imbal= null;
										sip_or_pay= null;
										nagative_imbal= null;
										transport_charges= null;
										park_charge = null;
		
										pos_qty = null; neg_qty = null; un_qty = null;park_qty = null;
										pos_sale = 0; neg_sale = 0; un_sale = 0;park_sale = 0;
										
										inv_type = "";
		                            	
		                            	for(int j = 0; j<comp.length;j++) {
		
		                            		//System.out.println(comp[j]+"Index"+j);
		                            		queryString3 = "SELECT BILL_QTY,COMPO_CD,BILL_RATE,BILL_AMT,BILL_TAX_AMT FROM FMS_COMPO_INV_DTL WHERE "
		                                			+ " MAPPING_ID = ? AND REV_NO = ? AND SPLIT_ID = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
		                                			+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? "
//		                                			+ "AND REV_DT =  TO_DATE(TO_DATE(?,'DD/MM/YYYY'),'DD-MON-YY') "
		                                			+ "AND COMPO_CD = ? AND INV_NO LIKE ? ";

//		                            		if(!rset1.getString(86).contains("[")) {
//		                            			queryString3 + "AND REV_DT =  TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
//		                        			}else {
//		                        				queryString3 += "AND INV_NO = ? ";
//		                        			}
//		                            			queryString3 += 
		                            			
		            							stmt3 = conn.prepareStatement(queryString3);
		            							stmt3.setString(1, trans_map);
		            							stmt3.setString(2, rset1.getString(78));
		            							stmt3.setString(3, rset1.getString(79));
		            							stmt3.setString(4, rset1.getString(80));
		            							stmt3.setString(5, rset1.getString(81));
		            							stmt3.setString(6, rset1.getString(82));
		            							stmt3.setString(7, rset1.getString(83));
		            							stmt3.setString(8, rset1.getString(84));
		            							stmt3.setString(9, rset1.getString(85));
//		            							stmt3.setString(10, rset1.getString(85));
		            							stmt3.setString(10, comp[j]);
		            							stmt3.setString(11, "%"+inv_no+"%");
		            							
//		            							if (!rset1.getString(86).contains("[")) {
//		            										            								
//												}else {
//													stmt3.setString(10, rset1.getString(86));
//												}

		            							rset3 = stmt3.executeQuery();
		            							while(rset3.next()) {
		//            								qty = rset3.getString(1);
		            								cmp_cd = rset3.getString(2);
		            								bill_rate = rset3.getString(3);
		//            								sale_amt = rset3.getDouble(4);
		            								tax = rset3.getDouble(5);		
		    
		            							if (cmp_cd!=null) {
													if (comp[j].equals("43") || comp[j].equals("16")) {
														inv_type = "TC";
														if (comp[j].equals("43")) {
															sip_or_pay = bill_rate;
															sip_qty = rset3.getString(1);

														} else if (comp[j].equals("16")) {
															transport_charges = bill_rate;
															qty = rset3.getString(1);
														}
														sale_amt += rset3.getDouble(4);
		
		
													} else if (comp[j].equals("12") || comp[j].equals("30")
															|| comp[j].equals("13") || comp[j].equals("31")
															|| comp[j].equals("32") || comp[j].equals("34")) {
														inv_type = "IC";
														if (comp[j].equals("13") || comp[j].equals("31")
																|| comp[j].equals("32")) {
															nagative_imbal = bill_rate;
															neg_qty = rset3.getString(1);
															neg_sale = rset3.getDouble(4);
		
														} else if (comp[j].equals("12") || comp[j].equals("30")) {
															positive_imbal = bill_rate;
															pos_qty = rset3.getString(1);
															pos_sale = rset3.getDouble(4);
		
														} else if (comp[j].equals("34")) {
															un_overrun = bill_rate;
															un_qty = rset3.getString(1);
															un_sale = rset3.getDouble(4);
		
														}
		
													}
													else if(comp[j].equals("44")){
														inv_type = "PC";
														park_charge = bill_rate;
														park_qty = rset3.getString(1);
														park_sale = rset3.getDouble(4);
													}
													
												}
			
		            							
		            						}
		            							stmt3.close();
		            							rset3.close();
		                            	}
		                            	
										str += inv_type + ",";
										str += qty + ","+ sip_qty + "," + neg_qty + "," + pos_qty + "," + un_qty + ","+park_qty+",";
										str += sip_or_pay + "," + transport_charges + "," + nagative_imbal + ","
												+ positive_imbal + "," + un_overrun + "," + park_charge + ",";
										str += sale_amt + "," + neg_sale + "," + pos_sale + "," + un_sale
												+ "," + park_sale + ",";
		
										i += 17;
				
		                            }
		                            
								else if(i == 28) {	//INVOICE_SEQ
									queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
											+" || '-' || "
											+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
											+ "FROM DUAL";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, st_dt);
									stmt3.setString(2, end_dt);
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
										value = rset3.getString(1) == null ? "" :rset3.getString(1);
									}
									fin_year = value;
									rset3.close();
									stmt3.close();
									
		//							cell.setCellValue("'"+value+"'");
									str += value + ",";
									}
								
								else if(i == 31) {	//FREQ
									if(Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15) {									
									freq = "1";
									}
									else if(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])) {
										freq = "2";
									}
									else {
										freq = "8";
									}
								
		
									str += freq + ",";
								}
								else if(i == 32) {
									if((st_dt.split("/")[1].equals(cont_dt.split("/")[1]))  && (Integer.parseInt(st_dt.split("/")[0])<Integer.parseInt(cont_dt.split("/")[0]))) {
										st_dt = cont_dt;
									}
									else {
										st_dt=rset1.getString(32);
									}
									str  += st_dt + ",";
								}
								else if(i == 33) {
									str  += end_dt + ",";
								}
								else if(i==36) {
									double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;
		
									str += sale_amt1 + ",";
								}
		                            
								else if(i==41) {
									double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;
		
									str += sale_amt1 + ",";
								}
		                        
								else if(i==42) {
									String tax_cd = "";
									int count = 0;
									queryString3 = "SELECT B.DESCRIPTION, A.TAX_RATE FROM FMS_INV_TAX_DTL A , FMS_PRICE_MST B WHERE "
		                        			+ " A.MAPPING_ID = ? AND A.REV_NO = ? AND A.SPLIT_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
		                        			+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? "
		                        			+ "AND A.INV_NO = ? AND A.TAX_CD = B.PRICE_CD AND B.TYPE_CODE = 'T' AND A.TAX_RATE != 0 AND A.BILL_TAX_AMT != 0";
									stmt3  =conn.prepareStatement(queryString3);
									stmt3.setString(1, trans_map);
									stmt3.setString(2, rset1.getString(78));
        							stmt3.setString(3, rset1.getString(79));
        							stmt3.setString(4, rset1.getString(80));
        							stmt3.setString(5, rset1.getString(81));
        							stmt3.setString(6, rset1.getString(82));
        							stmt3.setString(7, rset1.getString(83));
        							stmt3.setString(8, rset1.getString(84));
        							stmt3.setString(9, rset1.getString(85));
//        							stmt3.setString(10, rset1.getString(85));
        							//stmt3.setString(11, "%"+st_dt.split("/")[0]+end_dt.split("/")[0]+end_dt.split("/")[1]+end_dt.split(" ")[0].split("/")[2]+"%");
        							stmt3.setString(10, rset1.getString(87));
        							rset3 = stmt3.executeQuery();					
        							while(rset3.next()) {
        								count++;
        								if(count>1) 
        									tax_cd +="@ ";
//        								tax_cd += rset3.getString(1);
        								tax_cd += (rset3.getString(1)+" "+rset3.getString(2)+"%");
        							}
        							
//        							tax_cd = tax_cd.substring(0, tax_cd.length()-2);
        							rset3.close();
        							stmt3.close();
        							str += tax_cd + ",";
        							
								}    
		                        
								else if(i == 50) {	//CHECKED_FLAG
									if(!value.equals("null")) {
										value = "Y";
									}
									else {
										value = "null";
									}
									str += value + ",";
								}
								
								else if(i == 53) {	//AUTHORIZED_FLAG
									if(!value.equals("null")) {
										value = "Y";
									}
									else {
										value = "null";
									}
									str += value + ",";
								}
								
								else if(i == 56) {	//APPROVED_FLAG
									if(!value.equals("null")) {
										value = "A";
									}
									else {
										value = "null";
									}
									str += value + ",";
								}
								
								else if(i == 61) {	//FINANCIAL_YEAR
									value = fin_year;
									
									str += value + ",";
								}
		                            
								else if(i==69) {    
									str += "TDS 2%" + ",";
								}    
								else if(i == 76) {
									
									if(sip_or_pay!=null && transport_charges!=null) {
										value = "TP-SP";
									}
									else if(sip_or_pay!=null && transport_charges == null) {
										value  = "SP";
									}
									else if(transport_charges!=null && sip_or_pay == null) {
										value  = "TP";
									}
									else if(nagative_imbal != null && positive_imbal != null && un_overrun != null) {
										value  = "NI-PI-UR";
									}
									else if(nagative_imbal == null && positive_imbal != null && un_overrun != null) {
										value  = "PI-UR";
									}
									else if(nagative_imbal != null && positive_imbal == null && un_overrun != null) {
										value  = "NI-UR";
									}
									else if(nagative_imbal != null && positive_imbal != null && un_overrun == null) {
										value  = "NI-PI";
									}
									else if(nagative_imbal == null && positive_imbal == null && un_overrun != null) {
										value  = "UR";
									}
									else if(nagative_imbal != null && positive_imbal == null && un_overrun == null) {
										value  = "NI";
									}
									else if(nagative_imbal == null && positive_imbal != null && un_overrun == null) {
										value  = "PI";
									}
									else if(park_charge != null){
										value = "PC";
									}
									str += value + ",";
								}    
		  
								else if(i==77) {//FIN_SYS
									value = rset1.getString(72);
									if(value!=null) {
										value = "S";
									}
									str += value + ",";
								}    
		                            
								else {
									str += value + ",";
								}
								
		                            
		                            
							}
							logger.insert_data(fname_csv, str, conn);
							count++;
							cus_map += (map+",");
		//					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
							}
						}
						rset1.close();
						stmt1.close();
							
							
				//TRANSPORTER
						queryString1 = "SELECT A.MAPPING_ID, 'R', NULL, NULL, NULL, 'R', NULL, NULL, '1', NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
								+ "NULL,NULL,NULL,NULL,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),A.BILL_NO,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, TO_CHAR(A.BILL_PERIOD_FROM, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "TO_CHAR(A.BILL_PERIOD_TO, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DUE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "'1',NULL, NULL, NULL, NULL,'1',NULL,NULL,NULL,NULL,A.BILL_AMT,NULL,NULL,A.BILL_AMT,NULL,A.VERIFY_BY, A.VERIFY_BY, TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), A.VERIFY_BY, A.VERIFY_BY,"
								+ "TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'),  A.APRV_BY, A.APRV_BY, TO_CHAR(A.APRV_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "	A.ENTER_BY, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), 'O', A.ENTER_BY, "
								+ "	TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.GROSS_TDS_AMT, '2', NULL,'01/04/2023 00:00:00', 'TDS', A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "NULL, NULL,NULL, A.REV_NO, A.SPLIT_ID,"
								+ " A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_CUST_CD , A.CONT_AGR_TYPE, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY'), A.INV_NO "
								+ "FROM FMS_INV_BILL_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO =  ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
								+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? AND "
//								+ "A.CONT_AGR_TYPE  = 'Trans' AND A.APRV_BY IS NOT NULL ORDER BY A.BILL_NO";
								+ "A.CONT_AGR_TYPE  = 'Trans' ORDER BY A.BILL_NO,A.APRV_BY";

					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, map_id);
					stmt1.setString(2, agmt_no);
					stmt1.setString(3, agmt_rev);
					stmt1.setString(4, cont_no);
					stmt1.setString(5, cont_rev);  
					stmt1.setString(6, cust_cd);
					stmt1.setString(7, cont_type);
					rset1 = stmt1.executeQuery();	
					
					while (rset1.next()) {
						str = "";
		//				row = spreadsheet.createRow(nrow++);
		//				ncell = 0;
						abbr = rset.getString(1);
		//				cd = rset1.getString(1);
						trans_map = rset1.getString(1);
						inv_dt = rset1.getString(30);
						st_dt = rset1.getString(32);
						end_dt = rset1.getString(33);
						dt = end_dt.split(" ")[0];
						month = dt.split("/")[1];
						year = dt.split("/")[2];
						String last_date = "";
						last_date = date.getLastDateOfMonth( month, year);
						inv_no = rset1.getString(87);
						inv_no = inv_no.split("-")[2];
						cont_dt=rset2.getString(10);
						freq = "";
						
						
						if(rset1.getString(29).contains(" ") && !rset1.getString(29).contains(",")) {
                            bill_no = rset1.getString(29).split(" ")[0];
						}else {
							bill_no = rset1.getString(29);
						}
						map =  rset2.getString(9)+bill_no+rset1.getString(30);						
						if(!trans_map1.contains(map) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
								(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) {	
						
						abbr1 = abbr;
						if (mpe.transporter_map.containsKey(abbr)) {
							org_abbr= abbr;
							abbr =mpe.transporter_map.get(abbr); 
						}
						
						if (mpe.meter_map.containsKey(org_abbr)) 
						{
							org_abbr = mpe.meter_map.get(org_abbr);
						}
						
						
		//				cell = row.createCell(ncell++);
		//				cell.setCellValue("'"+abbr+"'");
						str += abbr + ",";
						
						for (int i = 1; i < columns.split(",").length; i++) {
		//					cell = row.createCell(ncell++);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i);
							value = value.replaceAll(" ", "");
							value = value.replaceAll(",", "-");
		                        if(i==1) {
								value = trans_map;
								str += value + ",";
							    }
		//					
		                        else if(i==10) { 
	                            	
		                        	String comp [] = {"43","16","12","30","13","31","32","34","44"};
	                            	sip_qty = null;
	                            	qty = null;bill_rate=null;sale_amt=0;tax=0;
	                        		un_overrun = null;
									positive_imbal= null;
									sip_or_pay= null;
									nagative_imbal= null;
									transport_charges= null;
									park_charge = null;
	
									pos_qty = null; neg_qty = null; un_qty = null;park_qty = null;
									pos_sale = 0; neg_sale = 0; un_sale = 0;park_sale = 0;
									
									inv_type = "";
	                            	
	                            	for(int j = 0; j<comp.length;j++) {
	
	                            		//System.out.println(comp[j]+"Index"+j);
	                            		queryString3 = "SELECT BILL_QTY,COMPO_CD,BILL_RATE,BILL_AMT,BILL_TAX_AMT FROM FMS_COMPO_INV_DTL WHERE "
	                                			+ " MAPPING_ID = ? AND REV_NO = ? AND SPLIT_ID = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
	                                			+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? "
//	                                			+ "AND REV_DT =  TO_DATE(TO_DATE(?,'DD/MM/YYYY'),'DD-MON-YY') "
	                                			+ "AND COMPO_CD = ? AND INV_NO LIKE ? ";

//	                            		if(!rset1.getString(86).contains("[")) {
//	                            			queryString3 + "AND REV_DT =  TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
//	                        			}else {
//	                        				queryString3 += "AND INV_NO = ? ";
//	                        			}
//	                            			queryString3 += 
	                            			
	            							stmt3 = conn.prepareStatement(queryString3);
	            							stmt3.setString(1, trans_map);
	            							stmt3.setString(2, rset1.getString(78));
	            							stmt3.setString(3, rset1.getString(79));
	            							stmt3.setString(4, rset1.getString(80));
	            							stmt3.setString(5, rset1.getString(81));
	            							stmt3.setString(6, rset1.getString(82));
	            							stmt3.setString(7, rset1.getString(83));
	            							stmt3.setString(8, rset1.getString(84));
	            							stmt3.setString(9, rset1.getString(85));
//	            							stmt3.setString(10, rset1.getString(85));
	            							stmt3.setString(10, comp[j]);
	            							stmt3.setString(11, "%"+inv_no+"%");
	            							
//	            							if (!rset1.getString(86).contains("[")) {
//	            										            								
//											}else {
//												stmt3.setString(10, rset1.getString(86));
//											}

	            							rset3 = stmt3.executeQuery();
	            							while(rset3.next()) {
	//            								qty = rset3.getString(1);
	            								cmp_cd = rset3.getString(2);
	            								bill_rate = rset3.getString(3);
	//            								sale_amt = rset3.getDouble(4);
	            								tax = rset3.getDouble(5);		
	    
	            							if (cmp_cd!=null) {
												if (comp[j].equals("43") || comp[j].equals("16")) {
													inv_type = "TC";
													if (comp[j].equals("43")) {
														sip_or_pay = bill_rate;
														sip_qty = rset3.getString(1);

													} else if (comp[j].equals("16")) {
														transport_charges = bill_rate;
														qty = rset3.getString(1);
													}
													sale_amt += rset3.getDouble(4);
	
	
												} else if (comp[j].equals("12") || comp[j].equals("30")
														|| comp[j].equals("13") || comp[j].equals("31")
														|| comp[j].equals("32") || comp[j].equals("34")) {
													inv_type = "IC";
													if (comp[j].equals("13") || comp[j].equals("31")
															|| comp[j].equals("32")) {
														nagative_imbal = bill_rate;
														neg_qty = rset3.getString(1);
														neg_sale = rset3.getDouble(4);
	
													} else if (comp[j].equals("12") || comp[j].equals("30")) {
														positive_imbal = bill_rate;
														pos_qty = rset3.getString(1);
														pos_sale = rset3.getDouble(4);
	
													} else if (comp[j].equals("34")) {
														un_overrun = bill_rate;
														un_qty = rset3.getString(1);
														un_sale = rset3.getDouble(4);
	
													}
	
												}
												else if(comp[j].equals("44")){
													inv_type = "PC";
													park_charge = bill_rate;
													park_qty = rset3.getString(1);
													park_sale = rset3.getDouble(4);
												}
												
											}
		
	            							
	            						}
	            							stmt3.close();
	            							rset3.close();
	                            	}
	                            	
									str += inv_type + ",";
									str += qty + ","+ sip_qty + "," + neg_qty + "," + pos_qty + "," + un_qty + ","+park_qty+",";
									str += sip_or_pay + "," + transport_charges + "," + nagative_imbal + ","
											+ positive_imbal + "," + un_overrun + "," + park_charge + ",";
									str += sale_amt + "," + neg_sale + "," + pos_sale + "," + un_sale
											+ "," + park_sale + ",";
	
									i += 17;
			
	                            }
		                        
							else if(i == 28) {	//INVOICE_SEQ
								queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
										+" || '-' || "
										+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
										+ "FROM DUAL";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, st_dt);
								stmt3.setString(2, end_dt);
								rset3 = stmt3.executeQuery();
								if(rset3.next()) {
									value = rset3.getString(1) == null ? "" :rset3.getString(1);
								}
								fin_year = value;
								rset3.close();
								stmt3.close();
								
		//						cell.setCellValue("'"+value+"'");
								str += value + ",";
								}
							
							else if(i == 31) {	//FREQ
								if(Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15) {									
								freq = "1";
								}
								else if(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])) {
									freq = "2";
								}
								else {
									freq = "8";
								}
							
	
								str += freq + ",";
							}
							else if(i == 32) {
								if((st_dt.split("/")[1].equals(cont_dt.split("/")[1]))  && (Integer.parseInt(st_dt.split("/")[0])<Integer.parseInt(cont_dt.split("/")[0]))) {
									st_dt = cont_dt;
								}
								else {
									st_dt=rset1.getString(32);
								}
								str  += st_dt + ",";
							}
							else if(i == 33) {
								str  += end_dt + ",";
							}
							else if(i==36) {
								double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;
	
								str += sale_amt1 + ",";
							}
	                            
							else if(i==41) {
								double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;
	
								str += sale_amt1 + ",";
							}
		                     
							else if(i==42) {
								String tax_cd = "";
								int count = 0;
								queryString3 = "SELECT B.DESCRIPTION, A.TAX_RATE FROM FMS_INV_TAX_DTL A , FMS_PRICE_MST B WHERE "
	                        			+ " A.MAPPING_ID = ? AND A.REV_NO = ? AND A.SPLIT_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
	                        			+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? "
	                        			+ "AND A.INV_NO = ? AND A.TAX_CD = B.PRICE_CD AND B.TYPE_CODE = 'T' AND A.TAX_RATE != 0 AND A.BILL_TAX_AMT != 0";
								stmt3  =conn.prepareStatement(queryString3);
								stmt3.setString(1, trans_map);
								stmt3.setString(2, rset1.getString(78));
								stmt3.setString(3, rset1.getString(79));
								stmt3.setString(4, rset1.getString(80));
								stmt3.setString(5, rset1.getString(81));
								stmt3.setString(6, rset1.getString(82));
								stmt3.setString(7, rset1.getString(83));
								stmt3.setString(8, rset1.getString(84));
								stmt3.setString(9, rset1.getString(85));
//    							stmt3.setString(10, rset1.getString(85));
    							//stmt3.setString(11, "%"+st_dt.split("/")[0]+end_dt.split("/")[0]+end_dt.split("/")[1]+end_dt.split(" ")[0].split("/")[2]+"%");
    							stmt3.setString(10, rset1.getString(87));
    							rset3 = stmt3.executeQuery();					
    							while(rset3.next()) {
    								count++;
    								if(count>1) 
    									tax_cd +="@ ";
//    								tax_cd += rset3.getString(1);
    								tax_cd += (rset3.getString(1)+" "+rset3.getString(2)+"%");
    							}
    							
//    							tax_cd = tax_cd.substring(0, tax_cd.length()-2);
    							rset3.close();
    							stmt3.close();
    							str += tax_cd + ",";
    							
							}        
		                        
							else if(i == 50) {	//CHECKED_FLAG
								if(!value.equals("null")) {
									value = "Y";
								}
								else {
									value = "null";
								}
								str += value + ",";
							}
							
							else if(i == 53) {	//AUTHORIZED_FLAG
								if(!value.equals("null")) {
									value = "Y";
								}
								else {
									value = "null";
								}
								str += value + ",";
							}
							
							else if(i == 56) {	//APPROVED_FLAG
								if(!value.equals("null")) {
									value = "A";
								}
								else {
									value = "null";
								}
								str += value + ",";
							}
							
							else if(i == 61) {	//FINANCIAL_YEAR
								value = fin_year;
								
								str += value + ",";
							}
							else if(i==69) {    
								str += "TDS 2%" + ",";
							}    
		                        
							else if(i == 76) {
								
								if(sip_or_pay!=null && transport_charges!=null) {
									value = "TP-SP";
								}
								else if(sip_or_pay!=null && transport_charges == null) {
									value  = "SP";
								}
								else if(transport_charges!=null && sip_or_pay == null) {
									value  = "TP";
								}
								else if(nagative_imbal != null && positive_imbal != null && un_overrun != null) {
									value  = "NI-PI-UR";
								}
								else if(nagative_imbal == null && positive_imbal != null && un_overrun != null) {
									value  = "PI-UR";
								}
								else if(nagative_imbal != null && positive_imbal == null && un_overrun != null) {
									value  = "NI-UR";
								}
								else if(nagative_imbal != null && positive_imbal != null && un_overrun == null) {
									value  = "NI-PI";
								}
								else if(nagative_imbal == null && positive_imbal == null && un_overrun != null) {
									value  = "UR";
								}
								else if(nagative_imbal != null && positive_imbal == null && un_overrun == null) {
									value  = "NI";
								}
								else if(nagative_imbal == null && positive_imbal != null && un_overrun == null) {
									value  = "PI";
								}
								else if(park_charge != null){
									value = "PC";
								}
								str += value + ",";
							}    
		
							else if(i==77) {//FIN_SYS
								value = rset1.getString(72);
								if(value!=null) {
									value = "S";
								}
								str += value + ",";
							}
							
							else {
								str += value + ",";
							}
						}
						logger.insert_data(fname_csv, str, conn);
						count++;
						trans_map1 +=(map+",");
		//				logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
						}
					}
					rset1.close();
					stmt1.close();
						
					//Parking
					queryString1 = "SELECT A.MAPPING_ID, 'K', NULL, NULL, NULL, 'K', NULL, NULL, '1', NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"
							+ "NULL,NULL,NULL,NULL,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),A.BILL_NO,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), NULL, TO_CHAR(A.BILL_PERIOD_FROM, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "TO_CHAR(A.BILL_PERIOD_TO, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DUE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "'1',NULL, NULL, NULL, NULL,'1',NULL,NULL,NULL,NULL,A.BILL_AMT,NULL,NULL,A.BILL_AMT,NULL,A.VERIFY_BY, A.VERIFY_BY, TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), A.VERIFY_BY, A.VERIFY_BY,"
							+ "TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'),  A.APRV_BY, A.APRV_BY, TO_CHAR(A.APRV_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "	A.ENTER_BY, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'), 'O', A.ENTER_BY, "
							+ "	TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,A.GROSS_TDS_AMT, '2', NULL,'01/04/2023 00:00:00', 'TDS', A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "NULL, NULL,NULL, A.REV_NO, A.SPLIT_ID,"
							+ " A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_CUST_CD , A.CONT_AGR_TYPE, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY'), A.INV_NO "
							+ "FROM FMS_INV_BILL_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO =  ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
							+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? AND "
//							+ "A.CONT_AGR_TYPE  = 'Parking' AND A.APRV_BY IS NOT NULL ORDER BY A.BILL_NO";
							+ "A.CONT_AGR_TYPE  = 'Parking' ORDER BY A.BILL_NO,A.APRV_BY";

				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, map_id);
				stmt1.setString(2, agmt_no);
				stmt1.setString(3, agmt_rev);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, cont_rev);  
				stmt1.setString(6, cust_cd);
				stmt1.setString(7, cont_type);
				rset1 = stmt1.executeQuery();	
				
				while (rset1.next()) {
					str = "";
		//			row = spreadsheet.createRow(nrow++);
		//			ncell = 0;
					abbr = rset.getString(1);
		//			cd = rset1.getString(1);
					trans_map = rset1.getString(1);
					inv_dt = rset1.getString(30);
					st_dt = rset1.getString(32);
					end_dt = rset1.getString(33);
					dt = end_dt.split(" ")[0];
					month = dt.split("/")[1];
					year = dt.split("/")[2];
					String last_date = "";
					last_date = date.getLastDateOfMonth( month, year);
					inv_no = rset1.getString(87);
					inv_no = inv_no.split("-")[2];
					freq = "";
					cont_dt=rset2.getString(10);
					if(rset1.getString(29).contains(" ") && !rset1.getString(29).contains(",")) {
                        bill_no = rset1.getString(29).split(" ")[0];
					}else {
						bill_no = rset1.getString(29);
					}
					map =  rset2.getString(9)+bill_no+rset1.getString(30);
					
					if(!park_map.contains(map) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
							(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) {
					
					abbr1 = abbr;
					if (mpe.transporter_map.containsKey(abbr)) {
						org_abbr= abbr;
						abbr =mpe.transporter_map.get(abbr); 
					}
					
					if (mpe.meter_map.containsKey(org_abbr)) 
					{
						org_abbr = mpe.meter_map.get(org_abbr);
					}
					
					
		//			cell = row.createCell(ncell++);
		//			cell.setCellValue("'"+abbr+"'");
					str += abbr + ",";
					
					for (int i = 1; i < columns.split(",").length; i++) {
		//				cell = row.createCell(ncell++);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i);
						value = value.replaceAll(" ", "");
						value = value.replaceAll(",", "-");
		                    if(i==1) {
							value = trans_map;
							str += value + ",";
						    }
		//				
		                    else if(i==10) { 
                            	
		                    	String comp [] = {"43","16","12","30","13","31","32","34","44"};
                            	sip_qty = null;
                            	qty = null;bill_rate=null;sale_amt=0;tax=0;
                        		un_overrun = null;
								positive_imbal= null;
								sip_or_pay= null;
								nagative_imbal= null;
								transport_charges= null;
								park_charge = null;

								pos_qty = null; neg_qty = null; un_qty = null;park_qty = null;
								pos_sale = 0; neg_sale = 0; un_sale = 0;park_sale = 0;
								
								inv_type = "";
                            	
                            	for(int j = 0; j<comp.length;j++) {

                            		//System.out.println(comp[j]+"Index"+j);
                            		queryString3 = "SELECT BILL_QTY,COMPO_CD,BILL_RATE,BILL_AMT,BILL_TAX_AMT FROM FMS_COMPO_INV_DTL WHERE "
                                			+ " MAPPING_ID = ? AND REV_NO = ? AND SPLIT_ID = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
                                			+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? "
//                                			+ "AND REV_DT =  TO_DATE(TO_DATE(?,'DD/MM/YYYY'),'DD-MON-YY') "
                                			+ "AND COMPO_CD = ? AND INV_NO LIKE ? ";

//                            		if(!rset1.getString(86).contains("[")) {
//                            			queryString3 + "AND REV_DT =  TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
//                        			}else {
//                        				queryString3 += "AND INV_NO = ? ";
//                        			}
//                            			queryString3 += 
                            			
            							stmt3 = conn.prepareStatement(queryString3);
            							stmt3.setString(1, trans_map);
            							stmt3.setString(2, rset1.getString(78));
            							stmt3.setString(3, rset1.getString(79));
            							stmt3.setString(4, rset1.getString(80));
            							stmt3.setString(5, rset1.getString(81));
            							stmt3.setString(6, rset1.getString(82));
            							stmt3.setString(7, rset1.getString(83));
            							stmt3.setString(8, rset1.getString(84));
            							stmt3.setString(9, rset1.getString(85));
//            							stmt3.setString(10, rset1.getString(85));
            							stmt3.setString(10, comp[j]);
            							stmt3.setString(11, "%"+inv_no+"%");
            							
//            							if (!rset1.getString(86).contains("[")) {
//            										            								
//										}else {
//											stmt3.setString(10, rset1.getString(86));
//										}

            							rset3 = stmt3.executeQuery();
            							while(rset3.next()) {
//            								qty = rset3.getString(1);
            								cmp_cd = rset3.getString(2);
            								bill_rate = rset3.getString(3);
//            								sale_amt = rset3.getDouble(4);
            								tax = rset3.getDouble(5);		
    
            							if (cmp_cd!=null) {
											if (comp[j].equals("43") || comp[j].equals("16")) {
												inv_type = "TC";
												if (comp[j].equals("43")) {
													sip_or_pay = bill_rate;
													sip_qty = rset3.getString(1);

												} else if (comp[j].equals("16")) {
													transport_charges = bill_rate;
													qty = rset3.getString(1);
												}
												sale_amt += rset3.getDouble(4);


											} else if (comp[j].equals("12") || comp[j].equals("30")
													|| comp[j].equals("13") || comp[j].equals("31")
													|| comp[j].equals("32") || comp[j].equals("34")) {
												inv_type = "IC";
												if (comp[j].equals("13") || comp[j].equals("31")
														|| comp[j].equals("32")) {
													nagative_imbal = bill_rate;
													neg_qty = rset3.getString(1);
													neg_sale = rset3.getDouble(4);

												} else if (comp[j].equals("12") || comp[j].equals("30")) {
													positive_imbal = bill_rate;
													pos_qty = rset3.getString(1);
													pos_sale = rset3.getDouble(4);

												} else if (comp[j].equals("34")) {
													un_overrun = bill_rate;
													un_qty = rset3.getString(1);
													un_sale = rset3.getDouble(4);

												}

											}
											else if(comp[j].equals("44")){
												inv_type = "PC";
												park_charge = bill_rate;
												park_qty = rset3.getString(1);
												park_sale = rset3.getDouble(4);
											}
											
										}
	
            							
            						}
            							stmt3.close();
            							rset3.close();
                            	}
                            	
								str += inv_type + ",";
								str += qty + ","+ sip_qty + "," + neg_qty + "," + pos_qty + "," + un_qty + ","+park_qty+",";
								str += sip_or_pay + "," + transport_charges + "," + nagative_imbal + ","
										+ positive_imbal + "," + un_overrun + "," + park_charge + ",";
								str += sale_amt + "," + neg_sale + "," + pos_sale + "," + un_sale
										+ "," + park_sale + ",";

								i += 17;
		
                            }
		                    
						else if(i == 28) {	//INVOICE_SEQ
							queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
									+" || '-' || "
									+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
									+ "FROM DUAL";
							stmt3 = conn.prepareStatement(queryString3);
							stmt3.setString(1, st_dt);
							stmt3.setString(2, end_dt);
							rset3 = stmt3.executeQuery();
							if(rset3.next()) {
								value = rset3.getString(1) == null ? "" :rset3.getString(1);
							}
							fin_year = value;
							rset3.close();
							stmt3.close();
							
		//					cell.setCellValue("'"+value+"'");
							str += value + ",";
							}
						
						else if(i == 31) {	//FREQ
							if(Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15) {									
							freq = "1";
							}
							else if(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) ==  Integer.parseInt(last_date.split("/")[0])) {
								freq = "2";
							}
							else {
								freq = "8";
							}
						

							str += freq + ",";
						}
						else if(i == 32) {
							if((st_dt.split("/")[1].equals(cont_dt.split("/")[1]))  && (Integer.parseInt(st_dt.split("/")[0])<Integer.parseInt(cont_dt.split("/")[0]))) {
								st_dt = cont_dt;
							}
							else {
								st_dt=rset1.getString(32);
							}
							str  += st_dt + ",";
						}
						else if(i == 33) {
							str  += end_dt + ",";
						}
						else if(i==36) {
							double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;

							str += sale_amt1 + ",";
						}
                            
						else if(i==41) {
							double sale_amt1 = sale_amt + neg_sale + pos_sale + un_sale + park_sale;

							str += sale_amt1 + ",";
						}
		                    
						else if(i==42) {
							String tax_cd = "";
							int count = 0;
							queryString3 = "SELECT B.DESCRIPTION, A.TAX_RATE FROM FMS_INV_TAX_DTL A , FMS_PRICE_MST B WHERE "
                        			+ " A.MAPPING_ID = ? AND A.REV_NO = ? AND A.SPLIT_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
                        			+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? "
                        			+ "AND A.INV_NO = ? AND A.TAX_CD = B.PRICE_CD AND B.TYPE_CODE = 'T' AND A.TAX_RATE != 0 AND A.BILL_TAX_AMT != 0";
							stmt3  =conn.prepareStatement(queryString3);
							stmt3.setString(1, trans_map);
							stmt3.setString(2, rset1.getString(78));
							stmt3.setString(3, rset1.getString(79));
							stmt3.setString(4, rset1.getString(80));
							stmt3.setString(5, rset1.getString(81));
							stmt3.setString(6, rset1.getString(82));
							stmt3.setString(7, rset1.getString(83));
							stmt3.setString(8, rset1.getString(84));
							stmt3.setString(9, rset1.getString(85));
//							stmt3.setString(10, rset1.getString(85));
							//stmt3.setString(11, "%"+st_dt.split("/")[0]+end_dt.split("/")[0]+end_dt.split("/")[1]+end_dt.split(" ")[0].split("/")[2]+"%");
							stmt3.setString(10, rset1.getString(87));
							rset3 = stmt3.executeQuery();					
							while(rset3.next()) {
								count++;
								if(count>1) 
									tax_cd +="@ ";
//								tax_cd += rset3.getString(1);
								tax_cd += (rset3.getString(1)+" "+rset3.getString(2)+"%");
							}
							
//							tax_cd = tax_cd.substring(0, tax_cd.length()-2);
							rset3.close();
							stmt3.close();
							str += tax_cd + ",";
							
						}        
		                   
						else if(i == 50) {	//CHECKED_FLAG
							if(!value.equals("null")) {
								value = "Y";
							}
							else {
								value = "null";
							}
							str += value + ",";
						}
						
						else if(i == 53) {	//AUTHORIZED_FLAG
							if(!value.equals("null")) {
								value = "Y";
							}
							else {
								value = "null";
							}
							str += value + ",";
						}
						
						else if(i == 56) {	//APPROVED_FLAG
							if(!value.equals("null")) {
								value = "A";
							}
							else {
								value = "null";
							}
							str += value + ",";
						}
						
						else if(i == 61) {	//FINANCIAL_YEAR
							value = fin_year;
							
							str += value + ",";
						}
						else if(i==69) {    
							str += "TDS 2%" + ",";
						}    
		                    
						else if(i == 76) {
							
							if(sip_or_pay!=null && transport_charges!=null) {
								value = "TP-SP";
							}
							else if(sip_or_pay!=null && transport_charges == null) {
								value  = "SP";
							}
							else if(transport_charges!=null && sip_or_pay == null) {
								value  = "TP";
							}
							else if(nagative_imbal != null && positive_imbal != null && un_overrun != null) {
								value  = "NI-PI-UR";
							}
							else if(nagative_imbal == null && positive_imbal != null && un_overrun != null) {
								value  = "PI-UR";
							}
							else if(nagative_imbal != null && positive_imbal == null && un_overrun != null) {
								value  = "NI-UR";
							}
							else if(nagative_imbal != null && positive_imbal != null && un_overrun == null) {
								value  = "NI-PI";
							}
							else if(nagative_imbal == null && positive_imbal == null && un_overrun != null) {
								value  = "UR";
							}
							else if(nagative_imbal != null && positive_imbal == null && un_overrun == null) {
								value  = "NI";
							}
							else if(nagative_imbal == null && positive_imbal != null && un_overrun == null) {
								value  = "PI";
							}
							else if(park_charge != null){
								value = "PC";
							}
							str += value + ",";
						}    
		
						else if(i==77) {//FIN_SYS
							value = rset1.getString(72);
							if(value!=null) {
								value = "S";
							}
							str += value + ",";
						}
						
						else {
							str += value + ",";
						}
					}
					logger.insert_data(fname_csv, str, conn);
					count++;
					park_map +=(map+",");
		//			logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
					}
				}
				rset1.close();
				stmt1.close();
			
				}
				stmt2.close();
				rset2.close();
			}
			stmt.close();
			rset.close();
			
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_PG_INV_MST>>,,,", conn);
			
			
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
	
	//FFLOW
	public void FMS_GTA_FFLOW_INV_MST() throws SQLException, IOException {
		
		function_nm = "FMS_GTA_FFLOW_INV_MST()";
		
		try {
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_FFLOW_INV_MST>>,,,", conn);
			logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
			
			System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");

			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,ADDR_FLAG,INVOICE_SEQ,INVOICE_NO,INVOICE_REF,"
					+ "INVOICE_DT,INVOICE_CATEGORY,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,INVOICE_TYPE,LINKED_INVOICE,NUM_LINE,NOTE,GROSS_AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_DT,"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT_INR,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,CHECKED_FLAG,"
					+ "CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,FINANCIAL_YEAR,MODIFY_BY,MODIFY_DT,"
					+ "OTHER_INV_STR,AMT_WORD,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,TDS_AMT,TCS_TDS,TDS_STRUCT_CD,TDS_EFF_DT,TCS_AMT,TCS_STRUCT_CD,TCS_EFF_DT,SAP_APPROVAL,"
					+ "SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_EXCHNG_RATE,TCS_CERT_FLAG,ALLOC_QTY,SUB_INV_TYPE,FIN_SYS";
			count = 0;
			String org_abbr= "",fin_year="",inv_dt="",st_dt="",end_dt="",freq="",cust_cd="",inv_type="",inv_no="",cmp_cd = "",due_dt="";
			double tax = 0,qty=0,bill_amt=0;
			
			String cus_map = "", trans_map1 = "",month="",year="",dt="",cus_map1="",trans_map2="",bill_no="",cont_dt="",cont_st_dt="";
//			row = spreadsheet.createRow(nrow++);
			String fname_csv = "", str = "";
			fname_csv = migration_setup_dir + "EXPORT/FMS_GTA_FFLOW_INV_MST_" + start_end_dt + ".csv";
			
			FileWriter fw = new FileWriter(fname_csv, false); 
			fw.close();
			
			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
//				cell = row.createCell(i);
//				cell.setCellValue(columns.split(",")[i]);
				str += columns.split(",")[i] + ",";
			}
			logger.insert_data(fname_csv, str, conn);
			
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,GAS_DT,NOM_REV_NO,TRANSPORTER_MAP,CONT_MAP,MDQ,TIMESTAMP,", conn);
			
			// Inserting Rest of the data
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
					+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
					+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				
				queryString2 = "SELECT A.MAPPING_ID, A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,"
						+ " A.CONT_CUST_CD,A.CONT_MAPPING_ID,A.PARTY_CD FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? "
						+ "ORDER BY A.MAPPING_ID, A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,A.CONT_CUST_CD ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, rset.getString(2));
				rset2 = stmt2.executeQuery();
				
				while(rset2.next()) {
				
					map_id = rset2.getString(1);
					agmt_no = rset2.getString(2);
					agmt_rev = rset2.getString(3);
					cont_no = rset2.getString(4);
					cont_rev = rset2.getString(5);
					cont_type = rset2.getString(6);
					cust_cd = rset2.getString(7);
					cont_map = rset2.getString(8);
					
					//CUSTOMER
					queryString1 = "SELECT A.MAPPING_ID, 'C', NULL, NULL, NULL, 'C','1', NULL, NULL, NULL,A.BILL_NO,A.BILL_NO,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),'S',"
							+ "	NULL,TO_CHAR(A.BILL_PERIOD_FROM, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.BILL_PERIOD_TO, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DUE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
							+ "	NULL,NULL,NULL,A.REMARK,NULL,NULL,NULL,NULL,'1',NULL,NULL,NULL,NULL,A.BILL_AMT,NULL,NULL,A.BILL_AMT,NULL,A.VERIFY_BY, A.VERIFY_BY, TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), A.VERIFY_BY, A.VERIFY_BY,"
							+ "	TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'),A.APRV_BY, A.APRV_BY, TO_CHAR(A.APRV_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "	A.ENTER_BY, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,'O',A.ENTER_BY, "
							+ "	TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'),A.GROSS_TDS_AMT,'TDS','TDS 2%','01/04/2023 00:00:00',NULL,NULL,NULL, A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
							+ "	NULL,NULL,NULL,NULL, NULL,A.REV_NO, A.SPLIT_ID,"
							+ " A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_CUST_CD , A.CONT_AGR_TYPE, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY'), A.INV_NO ,TO_CHAR(A.CONTRACT_DT, 'DD/MM/YYYY HH24:MI:SS') "
							+ "FROM FMS_INV_BILL_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO =  ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
							+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? AND "
//							+ "A.CONT_AGR_TYPE IN ('FGSA','Tender') AND A.APRV_BY IS NOT NULL ORDER BY A.BILL_NO,ORDER BY A.APRV_BY ";
							+ "A.CONT_AGR_TYPE IN ('FGSA','Tender') ORDER BY A.BILL_NO,A.APRV_BY ";

					    stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, map_id);
						stmt1.setString(2, agmt_no);
						stmt1.setString(3, agmt_rev);
						stmt1.setString(4, cont_no);
						stmt1.setString(5, cont_rev);  
						stmt1.setString(6, cust_cd);
						stmt1.setString(7, cont_type);
						rset1 = stmt1.executeQuery();	
						
						while (rset1.next()) {
//							map = rset1.getString(1)+rset1.getString(13)+rset1.getString(72)+rset1.getString(73)+rset1.getString(74)+rset1.getString(75)+rset1.getString(76)+rset1.getString(77);
								str = "";
								int count1 = 0;
								abbr = rset.getString(1);
								trans_map = rset1.getString(1);
								inv_dt = rset1.getString(13);
								st_dt = rset1.getString(16);
								end_dt = rset1.getString(17);
								dt = end_dt.split(" ")[0];
								month = dt.split("/")[1];
								year = dt.split("/")[2];
								String last_date = "";
								last_date = date.getLastDateOfMonth( month, year);
								due_dt = rset1.getString(18);
								cont_dt = rset1.getString(81);
//							if((st_dt.split("/")[1].equals(cont_dt.split("/")[1]))  && (Integer.parseInt(st_dt.split("/")[0])<Integer.parseInt(cont_dt.split("/")[0])) ) {
//								System.out.println(" :: "+cont_dt);
//							}
								inv_no = rset1.getString(80);
								inv_no = inv_no.split("-")[2];
								freq = "";
//								 System.out.println(cont_st_dt);
								
								abbr1 = abbr;
								if (mpe.transporter_map.containsKey(abbr)) {
									org_abbr= abbr;
									abbr =mpe.transporter_map.get(abbr); 
								}
								
								if (mpe.meter_map.containsKey(org_abbr)) 
								{
									org_abbr = mpe.meter_map.get(org_abbr);
								}
								
								str += abbr + ",";
								
								for (int i = 1; i < columns.split(",").length; i++) {
									value = rset1.getString(i) == null ? "null" : rset1.getString(i);
									//value = value.replaceAll(" ", "");
									value = value.replaceAll(",", "-");								
			                            if(i==1) {
											cont_map = rset2.getString(8);
											if(cont_map!=null) {
												value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
											}
											else {
												value = trans_map;
											}
											str += value + ",";
			                            }
			                            else if(i == 10) {
											queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
											+" || '-' || "
											+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
											+ "FROM DUAL";
											stmt3 = conn.prepareStatement(queryString3);
											stmt3.setString(1, st_dt);
											stmt3.setString(2, end_dt);
											rset3 = stmt3.executeQuery();
											if(rset3.next()) {
												value = rset3.getString(1) == null ? "" :rset3.getString(1);
											}
											fin_year = value;
											rset3.close();
											stmt3.close();
											
											str += value + ",";
			                           }
			                           else if(i == 13) {
			                        	   
			                        	   str += inv_dt + ",";
			                           }
			                            
			                           else if(i == 15) {
											if(Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15) {									
											freq = "1";
											}
											else if(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])) {
												freq = "2";
											}
											else {
												freq = "8";
											}
											str += freq + ",";
			                           }
		                                  
			                           else if( i == 16) {
			                        	   str += st_dt + ",";
			                           }
			                           else if(i == 17) {
			                        	   str += end_dt + ",";
			                           }
			                           else if(i == 18) {
			                        	   str += due_dt + ",";
			                           }
			                           else if(i==19) { 
				                            	
//			                            	String comp [] = {"12","30","13","31","32","34","44","43","16"};
			                            	String comp [] = {"43","16","12","30","13","31","32","34","44"};

												
												inv_type = "";
												qty = 0;bill_amt=0;
				                            	
				                            	for(int j = 0; j<comp.length;j++) {
				
				                            		queryString3 = "SELECT BILL_QTY,COMPO_CD,BILL_AMT FROM FMS_COMPO_INV_DTL WHERE "
				                                			+ " MAPPING_ID = ? AND REV_NO = ? AND SPLIT_ID = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
				                                			+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? "
				                                			+ "AND COMPO_CD = ? AND INV_NO LIKE ? ";

		
				            							stmt3 = conn.prepareStatement(queryString3);
				            							stmt3.setString(1, trans_map);
				            							stmt3.setString(2, rset1.getString(71));
				            							stmt3.setString(3, rset1.getString(72));
				            							stmt3.setString(4, rset1.getString(73));
				            							stmt3.setString(5, rset1.getString(74));
				            							stmt3.setString(6, rset1.getString(75));
				            							stmt3.setString(7, rset1.getString(76));
				            							stmt3.setString(8, rset1.getString(77));
				            							stmt3.setString(9, rset1.getString(78));
				            							stmt3.setString(10, comp[j]);
				            							stmt3.setString(11, "%"+inv_no+"%");
		
				            							rset3 = stmt3.executeQuery();
				            							while(rset3.next()) {
				            								qty += rset3.getDouble(1);
				            								cmp_cd = rset3.getString(2);
				            								bill_amt += rset3.getDouble(3);
				            								count1++;
				    
				            							if (cmp_cd!=null) {
															if (comp[j].equals("43") || comp[j].equals("16") ) {
																inv_type = "TC";														
															} else if (comp[j].equals("12") || comp[j].equals("30")
																	|| comp[j].equals("13") || comp[j].equals("31")
																	|| comp[j].equals("32") || comp[j].equals("34")) {
																inv_type= "IC";
															}
															else if(comp[j].equals("44")){
																inv_type = "PC";
															}
														}
				            						}
				            							stmt3.close();
				            							rset3.close();
				                            	}
				                            	str += inv_type + ",";
			                           }
			                            
	                                  else if(i == 21) {
	                                	  str += count1 + ",";
	                                  }
	                                  else if(i ==28) {
	                                	  str += bill_amt + ",";
	                                  }
	                                  else if(i == 29) {
										String tax_cd = "";
										int count = 0;
										queryString3 = "SELECT B.DESCRIPTION, A.TAX_RATE FROM FMS_INV_TAX_DTL A , FMS_PRICE_MST B WHERE "
			                        			+ " A.MAPPING_ID = ? AND A.REV_NO = ? AND A.SPLIT_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
			                        			+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? "
			                        			+ "AND A.INV_NO = ? AND A.TAX_CD = B.PRICE_CD AND B.TYPE_CODE = 'T' AND A.TAX_RATE != 0 AND A.BILL_TAX_AMT != 0";
										stmt3  =conn.prepareStatement(queryString3);
										stmt3.setString(1, trans_map);
										stmt3.setString(2, rset1.getString(71));
            							stmt3.setString(3, rset1.getString(72));
            							stmt3.setString(4, rset1.getString(73));
            							stmt3.setString(5, rset1.getString(74));
            							stmt3.setString(6, rset1.getString(75));
            							stmt3.setString(7, rset1.getString(76));
            							stmt3.setString(8, rset1.getString(77));
            							stmt3.setString(9, rset1.getString(78));
		      							stmt3.setString(10, rset1.getString(80));
	
		      							rset3 = stmt3.executeQuery();					
		      							while(rset3.next()) {
		      								count++;
		      								if(count>1) 
		      									tax_cd +="@ ";
		      								tax_cd += (rset3.getString(1)+" "+rset3.getString(2)+"%");
		      							}
		      							rset3.close();
		      							stmt3.close();
		      							str += tax_cd + ",";
	      							
	                                  } 
	                                  else if(i == 37) { //CHECKED_FLAG
	                                	  	if(!value.equals("null")) {
												value = "Y";
											}
											else {
												value = "null";
											}
											str += value + ","; 
	                                  } 
	                                  else if(i == 40) { //AUTHORIZED_FLAG
	                                	  	if(!value.equals("null")) {
												value = "Y";
											}
											else {
												value = "null";
											}
											str += value + ","; 
	                                  }
	                                  else if(i == 43) { //APPROVED_FLAG
	                                	  	if(!value.equals("null")) {
												value = "A";
											}
											else {
												value = "null";
											}
											str += value + ",";
	                                  } 
	                                  else if(i == 48) {
	                                	  str += fin_year + ",";
	                                  }
	                                  else if(i == 68) {
	                                	  str += qty + ",";
	                                  }
	
	                                  else if(i==70) {//FIN_SYS
	  									value = rset1.getString(63);
	  									if(value!=null) {
	  										value = "S";
	  									}
	  									str += value + ",";
	  								}
	                                  else {
										str += value + ",";
									}
			                            
								}
								
								map = rset1.getString(1)+rset1.getString(13)+rset1.getString(73)+rset1.getString(74)+rset1.getString(75)+rset1.getString(76)+rset1.getString(77)+rset1.getString(78)+inv_type;
								if(rset1.getString(11).contains(" ") && !rset1.getString(11).contains(",")) {
									bill_no = rset1.getString(11).split(" ")[0];
								}
								else {
									bill_no  = rset1.getString(11);
								}
								map1=rset2.getString(9)+bill_no;
								
								if (((!cus_map1.contains(map1) && cus_map.contains(map)) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
									(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) || 
									(!cus_map1.contains(map1) && !((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
									(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0]))))) {
							
//								if (((!cus_map1.contains(map1) && cus_map.contains(map)) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
//										(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) || 
//										(!cus_map1.contains(map1) && !((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
//										(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0]))))
//										|| ((st_dt.split("/")[1].equals(cont_dt.split("/")[1]))  && (Integer.parseInt(st_dt.split("/")[0])<Integer.parseInt(cont_dt.split("/")[0])))) {
								logger.insert_data(fname_csv, str, conn);
								count++;
								cus_map1 +=(map1+",");
			//					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
							}
							else {
								cus_map += (map+",");
								cus_map1 +=(map1+",");
							}
								

						}
						rset1.close();
						stmt1.close();
							
							
				//TRANSPORTER
						queryString1 = "SELECT A.MAPPING_ID, 'R', NULL, NULL, NULL, 'R ','1', NULL, NULL, NULL,A.BILL_NO,A.BILL_NO,TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),'S',"
								+ "	NULL,TO_CHAR(A.BILL_PERIOD_FROM, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.BILL_PERIOD_TO, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DUE_DT, 'DD/MM/YYYY HH24:MI:SS'), "
								+ "	NULL,NULL,NULL,A.REMARK,NULL,NULL,NULL,NULL,'1',NULL,NULL,NULL,NULL,A.BILL_AMT,NULL,NULL,A.BILL_AMT,NULL,A.VERIFY_BY, A.VERIFY_BY, TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), A.VERIFY_BY, A.VERIFY_BY,"
								+ "	TO_CHAR(A.VERIFY_DT, 'DD/MM/YYYY HH24:MI:SS'),A.APRV_BY, A.APRV_BY, TO_CHAR(A.APRV_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "	A.ENTER_BY, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(A.BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL,NULL,NULL,NULL,'O',A.ENTER_BY, "
								+ "	TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'),A.GROSS_TDS_AMT,'TDS','TDS 2%','01/04/2023 00:00:00',NULL,NULL,NULL, A.SUN_APPROVAL, A.SUN_APPROVAL_BY, TO_CHAR(A.SUN_APPROVAL_DT, 'DD/MM/YYYY HH24:MI:SS'),"
								+ "	NULL,NULL,NULL,NULL, NULL,A.REV_NO, A.SPLIT_ID,"
								+ " A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_CUST_CD , A.CONT_AGR_TYPE, TO_CHAR(A.ENTER_DT, 'DD/MM/YYYY'), A.INV_NO,TO_CHAR(A.CONTRACT_DT, 'DD/MM/YYYY HH24:MI:SS') "
								+ "FROM FMS_INV_BILL_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO =  ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
								+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? AND "
								+ "A.CONT_AGR_TYPE = 'Trans' ORDER BY A.BILL_NO,A.APRV_BY ";

					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, map_id);
					stmt1.setString(2, agmt_no);
					stmt1.setString(3, agmt_rev);
					stmt1.setString(4, cont_no);
					stmt1.setString(5, cont_rev);  
					stmt1.setString(6, cust_cd);
					stmt1.setString(7, cont_type);
					rset1 = stmt1.executeQuery();	
					
					while (rset1.next()) {
//						map = rset1.getString(1)+rset1.getString(13)+rset1.getString(72)+rset1.getString(73)+rset1.getString(74)+rset1.getString(75)+rset1.getString(76)+rset1.getString(77);
//						if (trans_map1.contains(map)) {
						
							str = "";
							int count1 = 0;
							abbr = rset.getString(1);
							trans_map = rset1.getString(1);
							inv_dt = rset1.getString(13);
							st_dt = rset1.getString(16);
							end_dt = rset1.getString(17);
							dt = end_dt.split(" ")[0];
							month = dt.split("/")[1];
							year = dt.split("/")[2];
							String last_date = "";
							last_date = date.getLastDateOfMonth( month, year);
							inv_no = rset1.getString(80);
							inv_no = inv_no.split("-")[2];
							cont_dt = rset1.getString(81);
//							if((st_dt.split("/")[1].equals(cont_dt.split("/")[1]))  && (Integer.parseInt(st_dt.split("/")[0])<Integer.parseInt(cont_dt.split("/")[0])) ) {
//								System.out.println(" :Trans: "+cont_dt);
//							}
							freq = "";
							
//							if(rset1.getString(11).contains(" ")) {
//								bill_no = rset1.getString(11).split(" ")[0];
//							}
//							else {
//								bill_no  = rset1.getString(11);
//							}
							if(rset1.getString(11).contains(" ") && !rset1.getString(11).contains(",")) {
								bill_no = rset1.getString(11).split(" ")[0];
							}
							else {
								bill_no  = rset1.getString(11);
							}
							
							abbr1 = abbr;
							if (mpe.transporter_map.containsKey(abbr)) {
								org_abbr= abbr;
								abbr =mpe.transporter_map.get(abbr); 
							}
							
							if (mpe.meter_map.containsKey(org_abbr)) 
							{
								org_abbr = mpe.meter_map.get(org_abbr);
							}
							
							str += abbr + ",";
							
							for (int i = 1; i < columns.split(",").length; i++) {

								value = rset1.getString(i) == null ? "null" : rset1.getString(i);
								//value = value.replaceAll(" ", "");
								value = value.replaceAll(",", "-");								
		                            if(i==1) {
										cont_map = rset2.getString(8);
										if(cont_map!=null) {
											value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
										}
										else {
											value = trans_map;
										}
										str += value + ",";
		                            }
		                            else if(i == 10) {
										queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
										+" || '-' || "
										+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
										+ "FROM DUAL";
										stmt3 = conn.prepareStatement(queryString3);
										stmt3.setString(1, st_dt);
										stmt3.setString(2, end_dt);
										rset3 = stmt3.executeQuery();
										if(rset3.next()) {
											value = rset3.getString(1) == null ? "" :rset3.getString(1);
										}
										fin_year = value;
										rset3.close();
										stmt3.close();
										
										str += value + ",";
		                           }
		                           else if(i == 13) {
		                        	   str += inv_dt + ",";
		                           }
		                           else if(i == 15) {
										if(Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15) {									
										freq = "1";
										}
										else if(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])) {
											freq = "2";
										}
										else {
											freq = "8";
										}
										str += freq + ",";
		                           }
	                                  
		                           else if( i == 16) {
		                        	   str += st_dt + ",";
		                           }
		                           else if(i == 17) {
		                        	   str += end_dt + ",";
		                           }
//		                           else if(i==19) { 
//			                            	
////		                            	String comp [] = {"12","30","13","31","32","34","44","43","16"};
//		                            	String comp [] = {"43","16","12","30","13","31","32","34","44"};
//
//											
//											inv_type = "";
//											qty = 0;bill_amt=0;
//			                            	
//			                            	for(int j = 0; j<comp.length;j++) {
//			
//			                            		queryString3 = "SELECT BILL_QTY,COMPO_CD,BILL_AMT FROM FMS_COMPO_INV_DTL WHERE "
//			                                			+ " MAPPING_ID = ? AND REV_NO = ? AND SPLIT_ID = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
//			                                			+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? "
//			                                			+ "AND COMPO_CD = ? AND INV_NO LIKE ? ";
//
//	
//			            							stmt3 = conn.prepareStatement(queryString3);
//			            							stmt3.setString(1, trans_map);
//			            							stmt3.setString(2, rset1.getString(71));
//			            							stmt3.setString(3, rset1.getString(72));
//			            							stmt3.setString(4, rset1.getString(73));
//			            							stmt3.setString(5, rset1.getString(74));
//			            							stmt3.setString(6, rset1.getString(75));
//			            							stmt3.setString(7, rset1.getString(76));
//			            							stmt3.setString(8, rset1.getString(77));
//			            							stmt3.setString(9, rset1.getString(78));
//			            							stmt3.setString(10, comp[j]);
//			            							stmt3.setString(11, "%"+inv_no+"%");
//	
//			            							rset3 = stmt3.executeQuery();
//			            							while(rset3.next()) {
//			            								qty += rset3.getDouble(1);
//			            								cmp_cd = rset3.getString(2);
//			            								bill_amt += rset3.getDouble(3);
//			            								count1++;
//			    
//			            							if (cmp_cd!=null) {
//														if (comp[j].equals("43") || comp[j].equals("16")) {
//															inv_type = "TC";														
//														} else if (comp[j].equals("12") || comp[j].equals("30")
//																|| comp[j].equals("13") || comp[j].equals("31")
//																|| comp[j].equals("32") || comp[j].equals("34")) {
//															inv_type= "IC";
//														}
//														else if(comp[j].equals("44")){
//															inv_type = "PC";
//														}
//													}
//			            						}
//			            							stmt3.close();
//			            							rset3.close();
//			                            	}
//			                            	str += inv_type + ",";
//		                           }
		                          
		                           else if(i==19) { 
		                            	
//		                            	String comp [] = {"12","30","13","31","32","34","44","43","16"};
		                            	String comp [] = {"43","16","12","30","13","31","32","34","44"};

											
											inv_type = "";
											qty = 0;bill_amt=0;
			                            	
			                            	for(int j = 0; j<comp.length;j++) {
			
			                            		queryString3 = "SELECT BILL_QTY,COMPO_CD,BILL_AMT FROM FMS_COMPO_INV_DTL WHERE "
			                                			+ " MAPPING_ID = ? AND REV_NO = ? AND SPLIT_ID = ? AND CONT_AGR_NO = ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
			                                			+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? "
			                                			+ "AND COMPO_CD = ? AND INV_NO LIKE ? ";

	
			            							stmt3 = conn.prepareStatement(queryString3);
			            							stmt3.setString(1, trans_map);
			            							stmt3.setString(2, rset1.getString(71));
			            							stmt3.setString(3, rset1.getString(72));
			            							stmt3.setString(4, rset1.getString(73));
			            							stmt3.setString(5, rset1.getString(74));
			            							stmt3.setString(6, rset1.getString(75));
			            							stmt3.setString(7, rset1.getString(76));
			            							stmt3.setString(8, rset1.getString(77));
			            							stmt3.setString(9, rset1.getString(78));
			            							stmt3.setString(10, comp[j]);
			            							stmt3.setString(11, "%"+inv_no+"%");
	
			            							rset3 = stmt3.executeQuery();
			            							while(rset3.next()) {
			            								qty += rset3.getDouble(1);
			            								cmp_cd = rset3.getString(2);
			            								bill_amt += rset3.getDouble(3);
			            								count1++;
			    
			            							if (cmp_cd!=null) {
														if (comp[j].equals("43") || comp[j].equals("16")) {
															inv_type = "TC";														
														} else if (comp[j].equals("12") || comp[j].equals("30")
																|| comp[j].equals("13") || comp[j].equals("31")
																|| comp[j].equals("32") || comp[j].equals("34")) {
															inv_type= "IC";
														}
														else if(comp[j].equals("44")){
															inv_type = "PC";
														}
													}
			            						}
			            							stmt3.close();
			            							rset3.close();
			                            	}
			                            	str += inv_type + ",";
		                           }
		                            
                                  else if(i == 21) {
                                	  str += count1 + ",";
                                  }
                                  else if(i ==28) {
                                	  str += bill_amt + ",";
                                  }
                                  else if(i == 29) {
									String tax_cd = "";
									int count = 0;
									queryString3 = "SELECT B.DESCRIPTION, A.TAX_RATE FROM FMS_INV_TAX_DTL A , FMS_PRICE_MST B WHERE "
		                        			+ " A.MAPPING_ID = ? AND A.REV_NO = ? AND A.SPLIT_ID = ? AND A.CONT_AGR_NO = ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
		                        			+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? "
		                        			+ "AND A.INV_NO = ? AND A.TAX_CD = B.PRICE_CD AND B.TYPE_CODE = 'T' AND A.TAX_RATE != 0 AND A.BILL_TAX_AMT != 0";
									stmt3  =conn.prepareStatement(queryString3);
									stmt3.setString(1, trans_map);
									stmt3.setString(2, rset1.getString(71));
        							stmt3.setString(3, rset1.getString(72));
        							stmt3.setString(4, rset1.getString(73));
        							stmt3.setString(5, rset1.getString(74));
        							stmt3.setString(6, rset1.getString(75));
        							stmt3.setString(7, rset1.getString(76));
        							stmt3.setString(8, rset1.getString(77));
        							stmt3.setString(9, rset1.getString(78));
	      							stmt3.setString(10, rset1.getString(80));

	      							rset3 = stmt3.executeQuery();					
	      							while(rset3.next()) {
	      								count++;
	      								if(count>1) 
	      									tax_cd +="@ ";
	      								tax_cd += (rset3.getString(1)+" "+rset3.getString(2)+"%");
	      							}
	      							rset3.close();
	      							stmt3.close();
	      							str += tax_cd + ",";
      							
                                  } 
                                  else if(i == 37) { //CHECKED_FLAG
                                	  	if(!value.equals("null")) {
											value = "Y";
										}
										else {
											value = "null";
										}
										str += value + ","; 
                                  } 
                                  else if(i == 40) { //AUTHORIZED_FLAG
                                	  	if(!value.equals("null")) {
											value = "Y";
										}
										else {
											value = "null";
										}
										str += value + ","; 
                                  }
                                  else if(i == 43) { //APPROVED_FLAG
                                	  	if(!value.equals("null")) {
											value = "A";
										}
										else {
											value = "null";
										}
										str += value + ",";
                                  } 
                                  else if(i == 48) {
                                	  str += fin_year + ",";
                                  }
                                  else if(i == 68) {
                                	  str += qty + ",";
                                  }

                                  else if(i==70) {//FIN_SYS
	  									value = rset1.getString(63);
	  									if(value!=null) {
	  										value = "S";
	  									}
	  									str += value + ",";
	  								}
                                  
                                  else {
									str += value + ",";
								}
								
		                            
		                            
							}
							map = rset1.getString(1)+rset1.getString(13)+rset1.getString(73)+rset1.getString(74)+rset1.getString(75)+rset1.getString(76)+rset1.getString(77)+rset1.getString(78)+inv_type;
							map1 = rset2.getString(9)+bill_no;
							
							if (((!trans_map2.contains(map1) && trans_map1.contains(map)) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
								(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) ||
								(!trans_map2.contains(map1) &&!((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
								(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0]))))) {
							
							
//							if (((!trans_map2.contains(map1) && trans_map1.contains(map)) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
//									(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) ||
//									(!trans_map2.contains(map1) &&!((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
//									(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0]))))
//									|| ((st_dt.split("/")[1].equals(cont_dt.split("/")[1]))  && (Integer.parseInt(st_dt.split("/")[0])<Integer.parseInt(cont_dt.split("/")[0])))) {

							logger.insert_data(fname_csv, str, conn);
							count++;
							trans_map2 += (map1+",");
		//					logger.data(fname, (abbr + "," + cd + "," + gas_dt + "," + nom_rev + "," + trans_map + "," + cont_map + "," + mdq + "," ), conn, "");
						}
							
						else {
							trans_map1 += (map+",");
							trans_map2 += (map1+",");
						}
							
						
					}
					rset1.close();
					stmt1.close();
					
				}
				stmt2.close();
				rset2.close();
			}
			stmt.close();
			rset.close();
			
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_GTA_FFLOW_INV_MST>>,,,", conn);
			
			
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
	
	
	
	//FMS_GTA_FFLOW_INV_DTL
			public void FMS_GTA_FFLOW_INV_DTL() throws SQLException, IOException {
				
				function_nm = "FMS_GTA_FFLOW_INV_DTL()";
				
				try {
					
					logger.checkpoint(fname, "\n<<START>>,<<FMS_GTA_FFLOW_INV_DTL>>,,,", conn);
					logger.checkpoint1(fname1,function_nm+"E"+","+start_end_dt+",", conn);
					
					System.out.println("<<START>><<"+function_nm.substring(0, function_nm.length()-2)+">>");
					
					columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,"
							+ "ADDR_FLAG,INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,UNIT,QTY,RATE,AMOUNT,ENT_BY,ENT_DT,FINANCIAL_YEAR,INVOICE_TYPE";
							
					
//					workbook = new XSSFWorkbook();
//					spreadsheet = workbook.createSheet("Sheet 1");
					
//					nrow = 0;
//					ncell = 0;
					count = 0;
					String org_abbr= "",st_dt="",end_dt="",cust_cd="",inv_type="",inv_no="",bill="";
					String qty="",rate="",cmp_cd="",amt="",desc="",bill_no="",dt="",month="",year="",cont_dt="";
					String fname_csv = "", str = "",cus_map="",trans_map1="",cus_map1="",trans_map2="";
					fname_csv = migration_setup_dir + "EXPORT/FMS_GTA_FFLOW_INV_DTL_" + start_end_dt + ".csv";
					
					FileWriter fw = new FileWriter(fname_csv, false); 
					fw.close();
					
					// Inserting Column Names
					for (int i = 0; i < columns.split(",").length; i++) {
						str += columns.split(",")[i] + ",";
					}
					logger.insert_data(fname_csv, str, conn);
					
					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,INVOICE_NO,INVOICE_TYPE,TIMESTAMP,", conn);
					
					// Inserting Rest of the data
					queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), A.COUNTERPARTY_CD, A.EFF_DT  FROM FMS9_COUNTERPTY_MST A "
							+ "WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
							+ "AND A.COUNTERPARTY_ABBR != 'SEIPL' ORDER BY A.COUNTERPARTY_CD, A.EFF_DT DESC  ";
					stmt = conn.prepareStatement(queryString);
					rset = stmt.executeQuery();
					
					while (rset.next()) {
						
						queryString2 = "SELECT A.MAPPING_ID, A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,"
								+ " A.CONT_CUST_CD,A.CONT_MAPPING_ID,A.PARTY_CD FROM FMS_CONT_MST A, FMS7_TRANSPORTER_MST B WHERE A.PARTY_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? "
								+ "ORDER BY A.CONT_AGR_NO, A.CONT_AGR_REV_NO, A.CONTRACT_NO, A.CONTRACT_REV_NO, A.CONT_AGR_TYPE,A.CONT_CUST_CD";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, rset.getString(2));
						rset2 = stmt2.executeQuery();
						
						while(rset2.next()) {
						
							map_id = rset2.getString(1);
							agmt_no = rset2.getString(2);
							agmt_rev = rset2.getString(3);
							cont_no = rset2.getString(4);
							cont_rev = rset2.getString(5);
							cont_type = rset2.getString(6);
							cust_cd = rset2.getString(7);
							cont_map = rset2.getString(8);
							
							
							//CUSTOMER
								queryString1 = "SELECT MAPPING_ID,TO_CHAR(BILL_PERIOD_FROM, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(BILL_PERIOD_TO, 'DD/MM/YYYY HH24:MI:SS'),"
											+ "BILL_NO,ENTER_BY, TO_CHAR(ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'),REV_NO, SPLIT_ID,CONT_AGR_NO,CONT_AGR_REV_NO,CONTRACT_NO,"
											+ "CONTRACT_REV_NO, CONT_CUST_CD , CONT_AGR_TYPE, TO_CHAR(ENTER_DT, 'DD/MM/YYYY'), INV_NO,TO_CHAR(BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(CONTRACT_DT, 'DD/MM/YYYY HH24:MI:SS') "
											+ "FROM FMS_INV_BILL_DTL WHERE MAPPING_ID = ? AND CONT_AGR_NO =  ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
											+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? AND "
//											+ "CONT_AGR_TYPE IN ('FGSA','Tender') AND APRV_BY IS NOT NULL ORDER BY BILL_NO";
											+ "CONT_AGR_TYPE IN ('FGSA','Tender') ORDER BY BILL_NO,APRV_BY";

								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, map_id);
								stmt1.setString(2, agmt_no);
								stmt1.setString(3, agmt_rev);
								stmt1.setString(4, cont_no);
								stmt1.setString(5, cont_rev);  
								stmt1.setString(6, cust_cd);
								stmt1.setString(7, cont_type);
								rset1 = stmt1.executeQuery();	
								
								while (rset1.next()) {
									int cnt = 0;
									abbr = rset.getString(1);
									trans_map = rset1.getString(1);
									st_dt = rset1.getString(2);
									end_dt = rset1.getString(3);
									dt = end_dt.split(" ")[0];
									month = dt.split("/")[1];
									year = dt.split("/")[2];
									String last_date = "";
									last_date = date.getLastDateOfMonth( month, year);
									bill_no = rset1.getString(4);
									inv_no = rset1.getString(16);
									inv_no = inv_no.split("-")[2];
									cont_dt = rset1.getString(18);
									
									queryString4 = "SELECT A.MAPPING_ID, NULL, A.COMPO_CD,NULL, NULL,'C',NULL,NULL, NULL, NULL,NULL,NULL,NULL,'INR',"
											+ "A.BILL_QTY, A.BILL_RATE, A.BILL_AMT, NULL,NULL, NULL,NULL,NULL "
											+ "FROM FMS_COMPO_INV_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO =  ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
											+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? AND A.REV_NO = ? AND A.SPLIT_ID = ? AND A.INV_NO LIKE ? "
											+ "AND A.CONT_AGR_TYPE IN ('FGSA','Tender') AND A.BILL_QTY IS NOT NULL AND A.BILL_QTY != '0'";
									stmt4 = conn.prepareStatement(queryString4);
									stmt4.setString(1, rset1.getString(1));
									stmt4.setString(2, rset1.getString(9));
									stmt4.setString(3, rset1.getString(10));
									stmt4.setString(4, rset1.getString(11));
									stmt4.setString(5, rset1.getString(12));  
									stmt4.setString(6, rset1.getString(13));
									stmt4.setString(7, rset1.getString(14));
									stmt4.setString(8, rset1.getString(7));
									stmt4.setString(9, rset1.getString(8));
									stmt4.setString(10, "%"+inv_no+"%");
									rset4 = stmt4.executeQuery();	
									while(rset4.next()) {
									
								
									str = "";	
									cmp_cd = rset4.getString(3);
//									int cnt = 0;
									abbr1 = abbr;
									if (mpe.transporter_map.containsKey(abbr)) {
										org_abbr= abbr;
										abbr =mpe.transporter_map.get(abbr); 
									}
									
									if (mpe.meter_map.containsKey(org_abbr)) 
									{
										org_abbr = mpe.meter_map.get(org_abbr);
									}
									str += abbr + ",";
									
									for (int i = 1; i < columns.split(",").length; i++) {
										value = rset4.getString(i) == null ? "null" : rset4.getString(i);
										value = value.replaceAll(" ", "");
										value = value.replaceAll(",", "-");
										
				                            if(i==1) {
											cont_map = rset2.getString(8);
											if(cont_map!=null) {
												value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
											}
											else {
												value = trans_map;
											}
											str += value + ",";
										}
										
				                            else if(i==2) {
												
												if(bill_no.contains(", ")) {
													bill_no = bill_no.replaceAll(", ", "- ");
												}
												str += bill_no+",";
											}
				                            else if(i==11) {
				                            	str += bill_no +",";
				                            }
				                            
										  else if(i==12) {//LINE_NO
											cnt++;
											str += cnt +",";
										  }

										 else if(i==13) {//LINE_DESC
											queryString3 = "SELECT DESCRIPTION FROM FMS_PRICE_MST WHERE TYPE_CODE = 'R' AND PRICE_CD = ? ";
											stmt3 = conn.prepareStatement(queryString3);
											stmt3.setString(1,cmp_cd);
											rset3 = stmt3.executeQuery();
											if(rset3.next()) {
												desc = rset3.getString(1);
											}
											stmt3.close();
											rset3.close();
											str += desc +",";
										}
				                            
										else if(i==18) { //ENT_BY
											str += rset1.getString(5)+",";
										}
	                                    else if(i==19) {//ENT_DT
	                                    	str += rset1.getString(6)+",";
										}
	                                    else if(i ==20) {	//FINANCIAL_YEAR
											queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
													+" || '-' || "
													+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
													+ "FROM DUAL";
											stmt3 = conn.prepareStatement(queryString3);
											stmt3.setString(1, st_dt);
											stmt3.setString(2, end_dt);
											rset3 = stmt3.executeQuery();
											if(rset3.next()) {
												value = rset3.getString(1) == null ? "" :rset3.getString(1);
											}
//											fin_year = value;
											rset3.close();
											stmt3.close();
											str += value + ",";
											}
				                            
										else if(i==21) { //INV_TYPE
											
											if (cmp_cd.equals("12") || cmp_cd.equals("30")
													|| cmp_cd.equals("13") || cmp_cd.equals("31")
													|| cmp_cd.equals("32") || cmp_cd.equals("34")) 
											{
												inv_type = "IC";
											}
											else if (cmp_cd.equals("43") || cmp_cd.equals("16")) {
												inv_type = "TC";
											}
											
											else if(cmp_cd.equals("44")){
												inv_type = "PC";
											}
											
	                                  	  str += inv_type + ",";
	                                    }
				  
										else {
											str += value + ",";
										}
				                            
									}
									map = rset1.getString(1)+rset1.getString(17)+rset1.getString(9)+rset1.getString(10)+rset1.getString(11)+rset1.getString(12)+rset1.getString(13)+rset1.getString(14);
									  if(rset1.getString(4).contains(" ")) {
										  bill = rset1.getString(4).split(" ")[0];
										}
										else {
											bill  = rset1.getString(4);
										}
									map1 = rset2.getString(9)+bill;
    								
									  if ((cus_map.contains(map) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
		    									(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) || 
		                                    	(!cus_map1.contains(map1) &&!((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
		    									(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0]))))
		                                    	) {
									logger.insert_data(fname_csv, str, conn);
									count++;
									cus_map1 +=(map1+",");
									logger.data(fname, (abbr + "," + trans_map + "," + cont_type + "," + bill_no + "," +inv_type + "," ), conn, "");
								    }
									else {
										cus_map += (map+",");
										cus_map1 +=(map1+",");
									}

								}
								stmt4.close();
								rset4.close();
							}
							rset1.close();
							stmt1.close();
									
									
						//TRANSPORTER
							queryString1 = "SELECT MAPPING_ID,TO_CHAR(BILL_PERIOD_FROM, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(BILL_PERIOD_TO, 'DD/MM/YYYY HH24:MI:SS'),"
									+ "BILL_NO,ENTER_BY, TO_CHAR(ENTER_DT, 'DD/MM/YYYY HH24:MI:SS'),REV_NO, SPLIT_ID,"
									+ "CONT_AGR_NO, CONT_AGR_REV_NO, CONTRACT_NO, CONTRACT_REV_NO, CONT_CUST_CD , CONT_AGR_TYPE, TO_CHAR(ENTER_DT, 'DD/MM/YYYY'), INV_NO,TO_CHAR(BILL_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(CONTRACT_DT, 'DD/MM/YYYY HH24:MI:SS') "
									+ "FROM FMS_INV_BILL_DTL WHERE MAPPING_ID = ? AND CONT_AGR_NO =  ? AND CONT_AGR_REV_NO = ? AND CONTRACT_NO = ? "
									+ "AND CONTRACT_REV_NO = ? AND CONT_CUST_CD = ? AND CONT_AGR_TYPE = ? AND "
//									+ "CONT_AGR_TYPE = 'Trans' AND APRV_BY IS NOT NULL ORDER BY BILL_NO ";
									+ "CONT_AGR_TYPE = 'Trans' ORDER BY BILL_NO,APRV_BY ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, map_id);
						stmt1.setString(2, agmt_no);
						stmt1.setString(3, agmt_rev);
						stmt1.setString(4, cont_no);
						stmt1.setString(5, cont_rev);  
						stmt1.setString(6, cust_cd);
						stmt1.setString(7, cont_type);
						rset1 = stmt1.executeQuery();	
						
						while (rset1.next()) {
							int cnt = 0;
							abbr = rset.getString(1);
							trans_map = rset1.getString(1);
							
							st_dt = rset1.getString(2);
							end_dt = rset1.getString(3);
							dt = end_dt.split(" ")[0];
							month = dt.split("/")[1];
							year = dt.split("/")[2];
							String last_date = "";
							last_date = date.getLastDateOfMonth( month, year);
							bill_no = rset1.getString(4);
							cont_dt = rset1.getString(18);
							inv_no = rset1.getString(16);
							inv_no = inv_no.split("-")[2];
							queryString4 = "SELECT A.MAPPING_ID, NULL, A.COMPO_CD,NULL, NULL,'R',NULL,NULL, NULL, NULL,NULL,NULL,NULL,'INR',"
									+ "A.BILL_QTY, A.BILL_RATE, A.BILL_AMT, NULL,NULL, NULL,NULL,NULL "
									+ "FROM FMS_COMPO_INV_DTL A WHERE A.MAPPING_ID = ? AND A.CONT_AGR_NO =  ? AND A.CONT_AGR_REV_NO = ? AND A.CONTRACT_NO = ? "
									+ "AND A.CONTRACT_REV_NO = ? AND A.CONT_CUST_CD = ? AND A.CONT_AGR_TYPE = ? AND A.REV_NO = ? AND A.SPLIT_ID = ? AND A.INV_NO LIKE ? "
									+ "AND A.CONT_AGR_TYPE  = 'Trans' AND A.BILL_QTY IS NOT NULL AND A.BILL_QTY != '0'";
							
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1, rset1.getString(1));
							stmt4.setString(2, rset1.getString(9));
							stmt4.setString(3, rset1.getString(10));
							stmt4.setString(4, rset1.getString(11));
							stmt4.setString(5, rset1.getString(12));  
							stmt4.setString(6, rset1.getString(13));
							stmt4.setString(7, rset1.getString(14));
							stmt4.setString(8, rset1.getString(7));
							stmt4.setString(9, rset1.getString(8));
							stmt4.setString(10, "%"+inv_no+"%");
							rset4 = stmt4.executeQuery();	
							while(rset4.next()) {
							
						
							str = "";	
							cmp_cd = rset4.getString(3);
//							int cnt = 0;
							abbr1 = abbr;
							if (mpe.transporter_map.containsKey(abbr)) {
								org_abbr= abbr;
								abbr =mpe.transporter_map.get(abbr); 
							}
							
							if (mpe.meter_map.containsKey(org_abbr)) 
							{
								org_abbr = mpe.meter_map.get(org_abbr);
							}
							str += abbr + ",";
							
							for (int i = 1; i < columns.split(",").length; i++) {
								value = rset4.getString(i) == null ? "null" : rset4.getString(i);
								value = value.replaceAll(" ", "");
								value = value.replaceAll(",", "-");
								
		                            if(i==1) {
									cont_map = rset2.getString(8);
									if(cont_map!=null) {
										value = trans_map.split("-")[1]+"-"+trans_map.split("-")[2]+"-"+trans_map.split("-")[4]+"-"+trans_map.split("-")[5]+cont_map.split("-")[0]+cont_map.split("-")[2]+"-"+cont_map.split("-")[3]+"-"+cont_map.split("-")[4]+"-"+cont_map.split("-")[5]+"-"+cont_map.split("-")[6];
									}
									else {
										value = trans_map;
									}
									str += value + ",";
								}
								
		                            else if(i==2) {
										bill_no = rset1.getString(4);
										if(bill_no.contains(", ")) {
											bill_no = bill_no.replaceAll(", ", "- ");
										}
										str += bill_no+",";
									}
		                            else if(i==11) {
		                            	str += bill_no +",";
		                            }
		                            
								  else if(i==12) {//LINE_NO
									cnt++;
									str += cnt +",";
								  }

								  else if(i==13) {//LINE_DESC
									queryString3 = "SELECT DESCRIPTION FROM FMS_PRICE_MST WHERE TYPE_CODE = 'R' AND PRICE_CD = ? ";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1,cmp_cd);
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
										desc = rset3.getString(1);
									}
									stmt3.close();
									rset3.close();
									str += desc +",";
								  }
		                            
								 else if(i==18) { //ENT_BY
									str += rset1.getString(5)+",";
								 }
	                             else if(i==19) {//ENT_DT
	                            	str += rset1.getString(6)+",";
								 }
	                             else if(i ==20) {	//FINANCIAL_YEAR
									queryString3 = " SELECT EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), -3)) "
											+" || '-' || "
											+"EXTRACT (YEAR FROM ADD_MONTHS (TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), 9))"
											+ "FROM DUAL";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, st_dt);
									stmt3.setString(2, end_dt);
									rset3 = stmt3.executeQuery();
									if(rset3.next()) {
										value = rset3.getString(1) == null ? "" :rset3.getString(1);
									}
//									fin_year = value;
									rset3.close();
									stmt3.close();
									str += value + ",";
									}
		                            
								else if(i==21) { //INV_TYPE
									if (cmp_cd.equals("12") || cmp_cd.equals("30")
											|| cmp_cd.equals("13") || cmp_cd.equals("31")
											|| cmp_cd.equals("32") || cmp_cd.equals("34")) 
									{
										inv_type = "IC";
									}
									else if (cmp_cd.equals("43") || cmp_cd.equals("16")) {
										inv_type = "TC";
									}
									else if(cmp_cd.equals("44")){
										inv_type = "PC";
									}
									
	                          	  str += inv_type + ",";
	                            }
		  
								else {
									str += value + ",";
								}
		                            
							}
							map = rset1.getString(1)+rset1.getString(17)+rset1.getString(9)+rset1.getString(10)+rset1.getString(11)+rset1.getString(12)+rset1.getString(13)+rset1.getString(14);
							 if(rset1.getString(4).contains(" ")) {
								 bill = rset1.getString(4).split(" ")[0];
								}
								else {
									bill  = rset1.getString(4);
								}
							 map1 = rset2.getString(9)+bill;
							
//							  
							 if ((trans_map1.contains(map) && ((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
										(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0])))) || 
		                            	(!trans_map2.contains(map1) &&!((Integer.parseInt(st_dt.split("/")[0]) == 1 && Integer.parseInt(end_dt.split("/")[0]) == 15 ) || 
										(Integer.parseInt(st_dt.split("/")[0]) == 16 && Integer.parseInt(end_dt.split("/")[0]) == Integer.parseInt(last_date.split("/")[0]))))) {
							 
							logger.insert_data(fname_csv, str, conn);
							count++;
							trans_map2 += (map1+",");
							logger.data(fname, (abbr + "," + trans_map + "," + cont_type + "," + bill_no + "," +inv_type + "," ), conn, "");
							}
							else {
								trans_map1 += (map+",");
								trans_map2 += (map1+",");
							}
						}
						stmt4.close();
						rset4.close();
					}
					rset1.close();
					stmt1.close();		
					
						}
						stmt2.close();
						rset2.close();
					}
					stmt.close();
					rset.close();
					
					
					logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + (count) + " ,,,"), conn);
					logger.checkpoint(fname, "<<END>>,<<FMS_GTA_FFLOW_INV_DTL>>,,,", conn);
					
					
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
