
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Master_SEIPL_Data_Extractor1 {

	public static void main(String[] args) {
		Master_Excel_Extractor1 ex = new Master_Excel_Extractor1();
		ex.getmail_list();
		ex.getCustomerTraderList();
		ex.init();
	}

}

class Master_Excel_Extractor1 {

	private static final Logger LOGGER = Logger.getLogger(Master_Excel_Extractor.class.getName());
	String fname = "xls_reports/Master_SEIPL_Data_Extractor(datalogger).csv";
	String fname_error = "xls_reports/Master_SEIPL_Data_Extractor_Error(datalogger).csv";

	Data_Logger logger = new Data_Logger();

	String query_abbr = "", queryString = "", queryString1 = "";
	Connection conn;
	ResultSet rset_abbr, rset, rset1;
	PreparedStatement stmt_abbr, stmt, stmt1;

	String customer_delete = "", trader_delete = "", customer_map = "", trader_map = "";
	String dbline = "", username = "", encrypted = "", password = "";
	String columns = "", filename = "", value = "";
	String delta_FromDt = null;
	String delta_ToDt = null;
	String function_nm = "";
	String abbr = "", cd = "", seq_no = "", entity = "", eff_dt = "", address_type = "", tax_code = "", tax_nm = "";

	int nrow = 0;
	int count = 0;

	XSSFWorkbook workbook = null;
	XSSFSheet spreadsheet = null;
	XSSFRow row;
	Cell cell;
	FileOutputStream fileOut = null;

	public void init() {

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println(dbline);
			conn = DriverManager.getConnection(dbline, username, password);
			if (conn != null) {
//	    		FMS_COUNTERPARTY_MST();
//	    		FMS_COUNTERPARTY_ADDR_MST();

//	    		FMS_ENTITY_REQ_DTL();
//	    		FMS_ENTITY_ADDR_MST();
//	    		FMS_COMPANY_OWNER_MST();
//	    		FMS_COMPANY_OWNER_ADDR_MST();
	    		FMS_COUNTERPARTY_PLANT_DTL();
//	    		FMS_COUNTERPARTY_BU_DTL();
//	    		FMS_SECTOR_MST();
//	    		FMS_INT_RATE_MST();
//	    		FMS_EXCHG_RATE_MST();
//	    		FMS_INT_PAY_RATE_ENTRY();

//	    		FMS_EXCHG_RATE_ENTRY();
//	    		FMS_BANK_MST();
//	    		FMS_ENTITY_TURNOVER_DTL();
//	    		FMS_SHIP_MST();
//	    		FMS_ENTITY_CONTACT_MST();
//	    		FMS_METER_MST();
//	    		FMS_GOVT_STAT_TAX();

	    		FMS_COUNTERPARTY_PLANT_TAX();
//				FMS_COUNTERPARTY_BU_TAX();
//				FMS_TAX_MST();
//				FMS_TAX_STRUCTURE();
//	    		FMS_TAX_STRUCTURE_DTL();
//	    		FMS_ENTITY_TAX_STRUCT_DTL();
//	    		FMS_ENTITY_SERVICE_TAX_DTL();
//	    		FMS_ENTITY_BU_SVC_TAX_DTL();
//	    		FMS_CUSTOM_TAX_STRUCT_DTL();
	    		FMS_HOLIDAY_DTL();

				conn.close();
				conn = null;
			}

		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ",
					e);
		} finally {
			try {
				if (rset != null) {
					try {
						rset.close();
					} catch (SQLException e) {
						LOGGER.log(Level.WARNING,
								"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
					}
				}
				if (rset1 != null) {
					try {
						rset1.close();
					} catch (SQLException e) {
						LOGGER.log(Level.WARNING,
								"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						LOGGER.log(Level.WARNING,
								"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
					}
				}
				if (stmt1 != null) {
					try {
						stmt1.close();
					} catch (SQLException e) {
						LOGGER.log(Level.WARNING,
								"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						LOGGER.log(Level.WARNING,
								"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
					}
				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING,
						"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> init()  ", e);
			}
		}

	}

	public void FMS_COUNTERPARTY_MST() throws SQLException, IOException {

		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_MST>>");

			columns = "COUNTERPARTY_CD,EFF_DT,COUNTERPARTY_NM,COUNTERPARTY_ABBR,PAN_NO,PAN_ISSUE_DT,CATEGORY,WEB_ADDR,NOTES,STATUS,KYC,IGX,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,SAP_CODE,NCF_CATEGORY,ENT_PROFILE,MOD_PROFILE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;

			row = spreadsheet.createRow(nrow++);

			// Inserting Column Names
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Inserting Rest of the data (COUNTERPARTY)
			queryString = "SELECT COUNTERPARTY_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'), COUNTERPARTY_NM, COUNTERPARTY_ABBR, PAN_NO, TO_CHAR(PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), KYC_NOTES, DORMANT_FLAG, KYC, IGX, ENT_BY, TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'), MODIFY_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS9_COUNTERPTY_MST WHERE COUNTERPARTY_ABBR != 'SEIPL' AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY COUNTERPARTY_CD, EFF_DT DESC ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {

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
				cell.setCellValue("'" + rset.getString(4) + "&@&" + rset.getString(1) + "'");

				cell = row.createCell(1);
				cell.setCellValue("'" + rset.getString(2) + "'");

				cell = row.createCell(2);
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(3);
				cell.setCellValue("'" + rset.getString(4) + "'");

				cell = row.createCell(4);
				cell.setCellValue("'" + rset.getString(5) + "'");

				cell = row.createCell(5);
				cell.setCellValue("'" + rset.getString(6) + "'");

				cell = row.createCell(6);
				cell.setCellValue("'" + categ + "'");

				cell = row.createCell(7);
				cell.setCellValue("'" + web_addr + "'");

				cell = row.createCell(8);
				cell.setCellValue("'" + rset.getString(7) + "'");

				cell = row.createCell(9);
				cell.setCellValue("'"
						+ ((rset.getString(8) != null && rset.getString(8).equalsIgnoreCase("Y")) ? "N" : "Y") + "'");

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
				cell.setCellValue("'null'");

				cell = row.createCell(17);
				cell.setCellValue("'null'");

				cell = row.createCell(18);
				cell.setCellValue("'2'");

				cell = row.createCell(19);
				cell.setCellValue("'null'");

				if (rset.getString(8) != null && rset.getString(8).equalsIgnoreCase("Y")) { // Inserting two entrieswith different Eff_Dtin-case of Inactive Counterparty
					queryString1 = "SELECT TO_CHAR(DORMANT_EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS9_COUNTERPTY_DORMANT_DTL WHERE ENTITY_CD = ? AND ENTITY_FLAG = 'M' AND DORMANT_FLAG = 'Y' ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, rset.getString(1));
					rset1 = stmt1.executeQuery();
					if (rset1.next()) {

						row = spreadsheet.createRow(nrow++);

						cell = row.createCell(0);
						cell.setCellValue("'" + rset.getString(4) + "&@&" + rset.getString(1) + "'");

						cell = row.createCell(1);
						cell.setCellValue("'" + rset1.getString(1) + "'");

						cell = row.createCell(2);
						cell.setCellValue("'" + rset.getString(3) + "'");

						cell = row.createCell(3);
						cell.setCellValue("'" + rset.getString(4) + "'");

						cell = row.createCell(4);
						cell.setCellValue("'" + rset.getString(5) + "'");

						cell = row.createCell(5);
						cell.setCellValue("'" + rset.getString(6) + "'");

						cell = row.createCell(6);
						cell.setCellValue("'" + categ + "'");

						cell = row.createCell(7);
						cell.setCellValue("'" + web_addr + "'");

						cell = row.createCell(8);
						cell.setCellValue("'" + rset.getString(7) + "'");

						cell = row.createCell(9);
						cell.setCellValue("'"
								+ ((rset.getString(8) != null && rset.getString(8).equalsIgnoreCase("Y")) ? "N" : "Y")
								+ "'");

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
						cell.setCellValue("'null'");

						cell = row.createCell(17);
						cell.setCellValue("'null'");

						cell = row.createCell(18);
						cell.setCellValue("'2'");

						cell = row.createCell(19);
						cell.setCellValue("'null'");

					}
					rset1.close();
					stmt1.close();
				}

			}
			stmt.close();
			rset.close();

			// Inserting Rest of the data (CHA)
			queryString = "SELECT CHA_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'), CHA_NAME, CHA_ABBR, PAN_NO, TO_CHAR(PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), NOTES, 'N', NULL, NULL, EMP_CD, TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, WEB_ADDR FROM FMS7_CUS_HOUSE_AGENT_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CHA_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {

				row = spreadsheet.createRow(nrow++);

				cell = row.createCell(0);
				cell.setCellValue("'" + rset.getString(4) + "&@&C" + rset.getString(1) + "'");

				cell = row.createCell(1);
				cell.setCellValue("'" + rset.getString(2) + "'");

				cell = row.createCell(2);
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(3);
				cell.setCellValue("'" + rset.getString(4) + "'");

				cell = row.createCell(4);
				cell.setCellValue("'" + rset.getString(5) + "'");

				cell = row.createCell(5);
				cell.setCellValue("'" + rset.getString(6) + "'");

				cell = row.createCell(6);
				cell.setCellValue("'null'");

				cell = row.createCell(7);
				cell.setCellValue("'" + rset.getString(15) + "'");

				cell = row.createCell(8);
				cell.setCellValue("'" + rset.getString(7) + "'");

				cell = row.createCell(9);
				cell.setCellValue("'" + (rset.getString(8) == "Y" ? "N" : "Y") + "'");

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
				cell.setCellValue("'" + rset.getString(13) + "'");

				cell = row.createCell(15);
				cell.setCellValue("'" + rset.getString(14) + "'");

				cell = row.createCell(16);
				cell.setCellValue("'null'");

				cell = row.createCell(17);
				cell.setCellValue("'null'");

				cell = row.createCell(18);
				cell.setCellValue("'2'");

				cell = row.createCell(19);
				cell.setCellValue("'null'");

			}
			stmt.close();
			rset.close();

			// Inserting Rest of the data (VESSEL AGENT)
			queryString = "SELECT VESSEL_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'), VESSEL_NAME, VESSEL_ABBR, PAN_NO, TO_CHAR(PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), NOTES, 'N', NULL, NULL, EMP_CD, TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, WEB_ADDR FROM FMS7_VESSEL_AGENT_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY VESSEL_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {

				row = spreadsheet.createRow(nrow++);

				cell = row.createCell(0);
				cell.setCellValue("'" + rset.getString(4) + "&@&V" + rset.getString(1) + "'");

				cell = row.createCell(1);
				cell.setCellValue("'" + rset.getString(2) + "'");

				cell = row.createCell(2);
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(3);
				cell.setCellValue("'" + rset.getString(4) + "'");

				cell = row.createCell(4);
				cell.setCellValue("'" + rset.getString(5) + "'");

				cell = row.createCell(5);
				cell.setCellValue("'" + rset.getString(6) + "'");

				cell = row.createCell(6);
				cell.setCellValue("'null'");

				cell = row.createCell(7);
				cell.setCellValue("'" + rset.getString(15) + "'");

				cell = row.createCell(8);
				cell.setCellValue("'" + rset.getString(7) + "'");

				cell = row.createCell(9);
				cell.setCellValue("'" + (rset.getString(8) == "Y" ? "N" : "Y") + "'");

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
				cell.setCellValue("'" + rset.getString(13) + "'");

				cell = row.createCell(15);
				cell.setCellValue("'" + rset.getString(14) + "'");

				cell = row.createCell(16);
				cell.setCellValue("'null'");

				cell = row.createCell(17);
				cell.setCellValue("'null'");

				cell = row.createCell(18);
				cell.setCellValue("'2'");

				cell = row.createCell(19);
				cell.setCellValue("'null'");

			}
			stmt.close();
			rset.close();

			// Inserting Rest of the data (SURVEYOR)
			queryString = "SELECT SURVEYOR_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'), SURVEYOR_NAME, SURVEYOR_ABBR, PAN_NO, TO_CHAR(PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), NOTES, 'N', NULL, NULL, EMP_CD, TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, WEB_ADDR FROM FMS7_SURVEYOR_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY SURVEYOR_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {

				row = spreadsheet.createRow(nrow++);

				cell = row.createCell(0);
				cell.setCellValue("'" + rset.getString(4) + "&@&S" + rset.getString(1) + "'");

				cell = row.createCell(1);
				cell.setCellValue("'" + rset.getString(2) + "'");

				cell = row.createCell(2);
				cell.setCellValue("'" + rset.getString(3) + "'");

				cell = row.createCell(3);
				cell.setCellValue("'" + rset.getString(4) + "'");

				cell = row.createCell(4);
				cell.setCellValue("'" + rset.getString(5) + "'");

				cell = row.createCell(5);
				cell.setCellValue("'" + rset.getString(6) + "'");

				cell = row.createCell(6);
				cell.setCellValue("'null'");

				cell = row.createCell(7);
				cell.setCellValue("'" + rset.getString(15) + "'");

				cell = row.createCell(8);
				cell.setCellValue("'" + rset.getString(7) + "'");

				cell = row.createCell(9);
				cell.setCellValue("'" + (rset.getString(8) == "Y" ? "N" : "Y") + "'");

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
				cell.setCellValue("'" + rset.getString(13) + "'");

				cell = row.createCell(15);
				cell.setCellValue("'" + rset.getString(14) + "'");

				cell = row.createCell(16);
				cell.setCellValue("'null'");

				cell = row.createCell(17);
				cell.setCellValue("'null'");

				cell = row.createCell(18);
				cell.setCellValue("'2'");

				cell = row.createCell(19);
				cell.setCellValue("'null'");

			}
			stmt.close();
			rset.close();

			filename = "xls_reports/FMS_COUNTERPARTY_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();

			System.out.println("<<END>><<FMS_COUNTERPARTY_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_MST()  ",
					e);
		}

	}

	public void FMS_COUNTERPARTY_ADDR_MST() throws SQLException, IOException {

		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_ADDR_MST>>");

			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ADDRESS_TYPE,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data (COUNTERPARTY)
			queryString = "SELECT DISTINCT(A.COUNTERPARTY_ABBR), B.COUNTERPARTY_CD, B.ADDRESS_TYPE, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), B.MODIFY_BY, TO_CHAR(B.MODIFY_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EFF_DT FROM FMS9_COUNTERPTY_MST A, FMS9_COUNTERPTY_ADD_MST B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND A.COUNTERPARTY_ABBR != 'SEIPL' AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_ADD_MST C WHERE B.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.COUNTERPARTY_CD, A.EFF_DT DESC  ";
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
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					/*
					 * if ((i == 16 || i == 18) && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (CHA)
			queryString = "SELECT DISTINCT(A.CHA_ABBR), B.CHA_CD, B.ADDRESS_TYPE, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_CUS_HOUSE_AGENT_MST A, FMS7_CUS_HOUSE_AGENT_ADDR_MST B WHERE A.CHA_CD = B.CHA_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUS_HOUSE_AGENT_ADDR_MST C WHERE B.CHA_CD = C.CHA_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.CHA_CD ";
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
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					/*
					 * if (i == 16 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (VESSEL AGENT)
			queryString = "SELECT DISTINCT(A.VESSEL_ABBR), B.VESSEL_CD, B.ADDRESS_TYPE, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_VESSEL_AGENT_MST A, FMS7_VESSEL_AGENT_ADDR_MST B WHERE A.VESSEL_CD = B.VESSEL_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_VESSEL_AGENT_ADDR_MST C WHERE B.VESSEL_CD = C.VESSEL_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.VESSEL_CD ";
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
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					/*
					 * if (i == 16 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (SURVEYOR)
			queryString = "SELECT DISTINCT(A.SURVEYOR_ABBR), B.SURVEYOR_CD, B.ADDRESS_TYPE, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_SURVEYOR_MST A, FMS7_SURVEYOR_ADDRESS_MST B WHERE A.SURVEYOR_CD = B.SURVEYOR_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_SURVEYOR_ADDRESS_MST C WHERE B.SURVEYOR_CD = C.SURVEYOR_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.SURVEYOR_CD ";
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
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					/*
					 * if (i == 16 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}

			}
			stmt.close();
			rset.close();

			filename = "xls_reports/FMS_COUNTERPARTY_ADDR_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();

			System.out.println("<<END>><<FMS_COUNTERPARTY_ADDR_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_ADDR_MST()  ",
					e);
		}

	}

	public void FMS_ENTITY_REQ_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_REQ_DTL()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_REQ_DTL>>");
			logger.checkpoint(fname, "\n<<START>>,<<FMS_ENTITY_REQ_DTL>>,,,", conn);
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,SEQ_NO,ENTITY,REMARK,APRV_NOTE,STATUS,REQ_BY,REQ_DT,APRV_BY,APRV_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
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
				abbr = rset.getString(1);
				cd = rset.getString(2);
				// CUSTOMER
				queryString1 = "SELECT CUSTOMER_CD, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_CUSTOMER_MST WHERE COUNTERPARTY_CD = ? AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
					cell.setCellValue("'" + rset.getString(1) + "'");

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
				queryString1 = "SELECT TRADER_CD, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_TRADER_MST WHERE COUNTERPARTY_CD = ? AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
					cell.setCellValue("'" + rset.getString(1) + "'");

					cell = row.createCell(1);
					cell.setCellValue("'" + rset.getString(2) + "'");

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
				rset1.close();
				stmt1.close();

				// TRANSPORTER
				queryString1 = "SELECT TRANSPORTER_CD, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_TRANSPORTER_MST WHERE COUNTERPARTY_CD = ? AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
					cell.setCellValue("'" + rset.getString(1) + "'");

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
			queryString = "SELECT DISTINCT(A.CHA_ABBR), A.CHA_CD, A.EMP_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_CUS_HOUSE_AGENT_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUS_HOUSE_AGENT_MST C WHERE A.CHA_CD = C.CHA_CD)  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.CHA_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {
				row = spreadsheet.createRow(nrow++);

				cell = row.createCell(0);
				cell.setCellValue("'" + rset.getString(1) + "'");

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
			queryString = "SELECT DISTINCT(A.VESSEL_ABBR), A.VESSEL_CD, A.EMP_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_VESSEL_AGENT_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_VESSEL_AGENT_MST C WHERE A.VESSEL_CD = C.VESSEL_CD)  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.VESSEL_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {
				row = spreadsheet.createRow(nrow++);

				cell = row.createCell(0);
				cell.setCellValue("'" + rset.getString(1) + "'");

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
			queryString = "SELECT DISTINCT(A.SURVEYOR_ABBR), A.SURVEYOR_CD, A.EMP_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss') FROM FMS7_SURVEYOR_MST A WHERE A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_SURVEYOR_MST C WHERE A.SURVEYOR_CD = C.SURVEYOR_CD)  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.SURVEYOR_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {
				row = spreadsheet.createRow(nrow++);

				cell = row.createCell(0);
				cell.setCellValue("'" + rset.getString(1) + "'");

				cell = row.createCell(1);
				cell.setCellValue("'" + rset.getString(2) + "'");

				cell = row.createCell(2);
				cell.setCellValue("'1'");
				seq_n = 1;
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

			filename = "xls_reports/FMS_ENTITY_REQ_DTL.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " , , ,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_REQ_DTL>>,,,", conn);
			System.out.println("<<END>><<FMS_ENTITY_REQ_DTL>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_REQ_DTL()  ", e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_ADDR_MST() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_ADDR_MST()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_ADDR_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_ADDR_MST>>,,,,", conn);
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ADDRESS_TYPE,ENTITY,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

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
				abbr = rset.getString(1);
				cd = rset.getString(2);
				// CUSTOMER
				queryString1 = "SELECT B.CUSTOMER_CD, B.ADDRESS_TYPE, 'C', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_CUSTOMER_ADDRESS_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUSTOMER_ADDRESS_MST C WHERE B.CUSTOMER_CD = C.CUSTOMER_CD) AND B.CUSTOMER_CD = (SELECT DISTINCT(D.CUSTOMER_CD) FROM FMS7_CUSTOMER_MST D WHERE B.CUSTOMER_CD = D.CUSTOMER_CD AND D.COUNTERPARTY_CD = ? )  AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.CUSTOMER_CD ";
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
						 * if (i == 17 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); }
						 */
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname,
							(abbr + " , " + cd + " , " + address_type + " , " + entity + " , " + eff_dt + " , "), conn,
							"");
					entity_cd = rset1.getString(1);

				}
				stmt1.close();
				rset1.close();

				// TRADER
				queryString1 = "SELECT B.TRADER_CD, B.ADDRESS_TYPE, 'T', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_TRADER_ADDRESS_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_TRADER_ADDRESS_MST C WHERE B.TRADER_CD = C.TRADER_CD) AND B.TRADER_CD = (SELECT DISTINCT(D.TRADER_CD) FROM FMS7_TRADER_MST D WHERE B.TRADER_CD = D.TRADER_CD AND D.COUNTERPARTY_CD = ? )  AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.TRADER_CD ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(2));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {
					row = spreadsheet.createRow(nrow++);
					// fetch value for logger:
					eff_dt = rset1.getString(4);
					entity = rset1.getString(3);
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
						 * if (i == 17 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); }
						 */
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname,
							(abbr + " , " + cd + " , " + address_type + " , " + entity + " , " + eff_dt + " , "), conn,
							"");
					entity_cd = rset1.getString(1);

				}
				stmt1.close();
				rset1.close();

				// TRANSPORTER
				queryString1 = "SELECT B.TRANSPORTER_CD, B.ADDRESS_TYPE, 'R', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_TRANSPORTER_ADDRESS_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_TRANSPORTER_ADDRESS_MST C WHERE B.TRANSPORTER_CD = C.TRANSPORTER_CD) AND B.TRANSPORTER_CD = (SELECT DISTINCT(D.TRANSPORTER_CD) FROM FMS7_TRANSPORTER_MST D WHERE B.TRANSPORTER_CD = D.TRANSPORTER_CD AND D.COUNTERPARTY_CD = ? )  AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.TRANSPORTER_CD ";
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
						 * if (i == 17 && !value.equals("null")) { // emp_abbr value =
						 * getEmpAbbr(value); }
						 */
						cell.setCellValue("'" + value + "'");
					}
					count++;
					logger.data(fname,
							(abbr + " , " + cd + " , " + address_type + " , " + entity + " , " + eff_dt + " , "), conn,
							"");
					entity_cd = rset1.getString(1);

				}
				stmt1.close();
				rset1.close();
			}

			stmt.close();
			rset.close();

			filename = "xls_reports/FMS_ENTITY_ADDR_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " , , , ,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_ADDR_MST>>,,,,", conn);
			System.out.println("<<END>><<FMS_ENTITY_ADDR_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_ADDR_MST()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COMPANY_OWNER_MST() throws SQLException, IOException {
		function_nm = "FMS_COMPANY_OWNER_MST()";
		try {

			System.out.println("<<START>><<FMS_COMPANY_OWNER_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_COMPANY_OWNER_MST>>,", conn);
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
			queryString = "SELECT '2', TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), SUPPLIER_NAME, SUPPLIER_ABBR, PAN_NO, TO_CHAR(PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), NOTES, DORMANT_FLAG, NULL, WEB_ADDR, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, '2' FROM FMS7_SUPPLIER_MST WHERE SUPPLIER_ABBR = 'SEIPL'  AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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

			filename = "xls_reports/FMS_COMPANY_OWNER_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " , "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COMPANY_OWNER_MST>>,", conn);
			System.out.println("<<END>><<FMS_COMPANY_OWNER_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COMPANY_OWNER_MST()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COMPANY_OWNER_ADDR_MST() throws SQLException, IOException {
		function_nm = "FMS_COMPANY_OWNER_ADDR_MST()";
		try {

			System.out.println("<<START>><<FMS_COMPANY_OWNER_ADDR_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_COMPANY_OWNER_ADDR_MST>>,,,", conn);
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
				queryString1 = "SELECT B.COUNTERPARTY_CD, B.ADDRESS_TYPE, TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.ADDR, B.CITY, B.PIN, B.STATE, B.ZONE, B.COUNTRY, B.PHONE, B.MOBILE, B.ALT_PHONE, B.FAX_1, B.FAX_2, B.EMAIL, B.EMP_CD, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_SUPPLIER_ADDRESS_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_SUPPLIER_ADDRESS_MST C WHERE B.SUPPLIER_CD = C.SUPPLIER_CD) AND B.SUPPLIER_CD = (SELECT DISTINCT(D.SUPPLIER_CD) FROM FMS7_SUPPLIER_MST D WHERE B.SUPPLIER_CD = D.SUPPLIER_CD AND D.COUNTERPARTY_CD = ? AND UPPER(SUPPLIER_ABBR) = 'SEIPL' ) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.SUPPLIER_CD ";
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

			filename = "xls_reports/FMS_COMPANY_OWNER_ADDR_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " ,,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COMPANY_OWNER_ADDR_MST>>,,,", conn);
			System.out.println("<<END>><<FMS_COMPANY_OWNER_ADDR_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COMPANY_OWNER_ADDR_MST()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COUNTERPARTY_PLANT_DTL() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_PLANT_DTL()";
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_PLANT_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_COUNTERPARTY_PLANT_DTL>>,,,,", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			
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
				
				abbr = rset.getString(1);
				cd = rset.getString(2);

				// CUSTOMER
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'C', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.PLANT_NAME, A.PLANT_SHORT_ABBR, A.PLANT_ADDR, A.PLANT_STATE, A.PLANT_ZONE, A.PLANT_CITY, A.PLANT_PIN, A.PLANT_SECTOR, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_CUSTOMER_PLANT_DTL A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
					
					if (!customer_delete.contains(plant_map)) {
						
						row = spreadsheet.createRow(nrow++);
						// fetch data from resultset for logger:
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						eff_dt = rset1.getString(4);
						
						value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
						cell = row.createCell(0);
						cell.setCellValue("'" + value + "'");
						
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
							
							if (i == 6 && value.length() > 20) {
								value = value.substring(0, 20);
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
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'T', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.PLANT_NAME, A.PLANT_SHORT_ABBR, A.PLANT_ADDR, A.PLANT_STATE, A.PLANT_ZONE, A.PLANT_CITY, A.PLANT_PIN, A.PLANT_SECTOR, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_TRADER_PLANT_DTL A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ?  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
					
					if (!trader_delete.contains(plant_map))
					{
						row = spreadsheet.createRow(nrow++);
						// fetch data from resultset for logger:
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						eff_dt = rset1.getString(4);
						value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
						cell = row.createCell(0);
						cell.setCellValue("'" + value + "'");
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
							if (i == 6 && value.length() > 20) {
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
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn,
								"");
					}

				}
				stmt1.close();
				rset1.close();

				// TRANSPORTER
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'R', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.PLANT_NAME, A.PLANT_SHORT_ABBR, A.PLANT_ADDR, A.PLANT_STATE, A.PLANT_ZONE, A.PLANT_CITY, A.PLANT_PIN, A.PLANT_SECTOR, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_TRANSPORTER_PLANT_DTL A, FMS7_TRANSPORTER_MST B WHERE A.TRANSPORTER_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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

					value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
					cell = row.createCell(0);
					cell.setCellValue("'" + value + "'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
						if (i == 6 && value.length() > 20) {
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

			}

			stmt.close();
			rset.close();

			// SUPPLIER
			queryString1 = "SELECT B.SUPPLIER_ABBR, '2', 'B', '1', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), 'SEIPL-GJ', 'SEIPL-GJ', A.ADDR, A.STATE, A.ZONE, A.CITY, A.PIN, NULL, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_SUPPLIER_ADDRESS_MST A, FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD AND B.SUPPLIER_ABBR = 'SEIPL' AND A.ADDRESS_TYPE = 'R' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))  AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.EFF_DT DESC ";
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

			queryString1 = "SELECT B.SUPPLIER_ABBR, '2', 'B', (A.SEQ_NO+1), TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.PLANT_NAME, A.PLANT_SHORT_ABBR, A.PLANT_ADDR, A.PLANT_STATE, A.PLANT_ZONE, A.PLANT_CITY, A.PLANT_PIN, A.PLANT_SECTOR, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_SUPPLIER_PLANT_DTL A, FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD  AND B.SUPPLIER_ABBR = 'SEIPL' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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

			filename = "xls_reports/FMS_COUNTERPARTY_PLANT_DTL.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " ,,,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_PLANT_DTL>>,,,,", conn);
			System.out.println("<<END>><<FMS_COUNTERPARTY_PLANT_DTL>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_PLANT_DTL()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COUNTERPARTY_BU_DTL() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_BU_DTL()";
		try {
			logger.checkpoint(fname, "<<START>>,<<FMS_COUNTERPARTY_BU_DTL>>,,,,", conn);
			System.out.println("<<START>><<FMS_COUNTERPARTY_BU_DTL>>");

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
			queryString1 = "SELECT B.SURVEYOR_ABBR, '2', 'S', '1', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.SURVEYOR_NAME, B.SURVEYOR_ABBR, A.ADDR, A.STATE, A.ZONE, A.CITY, A.PIN, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_SURVEYOR_ADDRESS_MST A, FMS7_SURVEYOR_MST B WHERE A.SURVEYOR_CD = B.SURVEYOR_CD AND A.ADDRESS_TYPE = 'R' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.EFF_DT DESC ";
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
				abbr = rset1.getString(1);
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
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt1.close();
			rset1.close();

			// VESSEL
			queryString1 = "SELECT B.VESSEL_ABBR, '2', 'V', '1', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.VESSEL_NAME, B.VESSEL_ABBR, A.ADDR, A.STATE, A.ZONE, A.CITY, A.PIN, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_VESSEL_AGENT_ADDR_MST A, FMS7_VESSEL_AGENT_MST B WHERE A.VESSEL_CD = B.VESSEL_CD AND A.ADDRESS_TYPE = 'R' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.EFF_DT DESC ";
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
				abbr = rset1.getString(1);
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
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt1.close();
			rset1.close();

			// CHA
			queryString1 = "SELECT B.CHA_ABBR, '2', 'H', '1', TO_CHAR(B.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), B.CHA_NAME, B.CHA_ABBR, A.ADDR, A.STATE, A.ZONE, A.CITY, A.PIN, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL FROM FMS7_CUS_HOUSE_AGENT_ADDR_MST A, FMS7_CUS_HOUSE_AGENT_MST B WHERE A.CHA_CD = B.CHA_CD AND A.ADDRESS_TYPE = 'R' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.EFF_DT DESC ";
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
				abbr = rset1.getString(1);
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
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt1.close();
			rset1.close();

			filename = "xls_reports/FMS_COUNTERPARTY_BU_DTL.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + " ,,,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_BU_DTL>>,,,,", conn);
			System.out.println("<<END>><<FMS_COUNTERPARTY_BU_DTL>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_BU_DTL()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_SECTOR_MST() throws SQLException, IOException {
		function_nm = "FMS_SECTOR_MST()";
		try {

			System.out.println("<<START>><<FMS_SECTOR_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SECTOR_MST>>,", conn);
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
			queryString = "SELECT SECTOR_CD, SECTOR_NAME, SECTOR_ABBR, FLAG, FLAG, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, '2', NULL FROM FMS7_SECTOR_MST  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
						value = (value.equals("N") ? "O" : value);
						cell.setCellValue("'" + value + "'");
					} else if (i == 4) {
						value = (value.equals("N") ? "N" : "Y");
						cell.setCellValue("'" + value + "'");
					}
					/*
					 * else if (i == 5 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (cd + "," + abbr + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = "xls_reports/FMS_SECTOR_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SECTOR_MST>>,", conn);
			System.out.println("<<END>><<FMS_SECTOR_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_SECTOR_MST()  ", e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_INT_RATE_MST() throws SQLException, IOException {
		function_nm = "FMS_INT_RATE_MST()";
		try {

			System.out.println("<<START>><<FMS_INT_RATE_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_INT_RATE_MST>>,", conn);
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
				abbr = rset.getString(3);
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

			filename = "xls_reports/FMS_INT_RATE_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_INT_RATE_MST>>,", conn);
			System.out.println("<<END>><<FMS_INT_RATE_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_INT_RATE_MST()  ", e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_EXCHG_RATE_MST() throws SQLException, IOException {
		function_nm = "FMS_EXCHG_RATE_MST()";
		try {

			System.out.println("<<START>><<FMS_EXCHG_RATE_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_EXCHG_RATE_MST>>,", conn);
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
				abbr = rset.getString(3);
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

			filename = "xls_reports/FMS_EXCHG_RATE_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_EXCHG_RATE_MST>>,", conn);
			System.out.println("<<END>><<FMS_EXCHG_RATE_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_EXCHG_RATE_MST()  ", e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_INT_PAY_RATE_ENTRY() throws SQLException, IOException {
		function_nm = "FMS_INT_PAY_RATE_ENTRY()";
		try {

			System.out.println("<<START>><<FMS_INT_PAY_RATE_ENTRY>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_INT_PAY_RATE_ENTRY>>,,", conn);
			// "xls_reports/FMS_INT_PAY_RATE_ENTRY.xlsx"
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
			queryString = "SELECT B.INT_RATE_NM, A.INT_RATE_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.INT_VAL, A.CURRENCY_CD, A.REMARK, A.FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, '2', NULL FROM FMS7_INT_PAY_RATE_ENTRY A, FMS7_CONT_INT_RATE_MST B WHERE A.INT_RATE_CD = B.INT_RATE_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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

			filename = "xls_reports/FMS_INT_PAY_RATE_ENTRY.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_INT_PAY_RATE_ENTRY>>,,", conn);
			System.out.println("<<END>><<FMS_INT_PAY_RATE_ENTRY>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_INT_PAY_RATE_ENTRY()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_EXCHG_RATE_ENTRY() throws SQLException, IOException {
		function_nm = "FMS_EXCHG_RATE_ENTRY()";
		try {

			System.out.println("<<START>><<FMS_EXCHG_RATE_ENTRY>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_EXCHG_RATE_ENTRY>>,,", conn);
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
			queryString = "SELECT B.EXC_RATE_NM, A.EXCHG_RATE_CD, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EXCHG_VAL, A.CURRENCY_CD, A.CURRENCY_CD_FROM, A.REMARK, A.FLAG, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.ENT_BY, NULL, NULL, '2', NULL FROM FMS7_EXCHG_RATE_ENTRY A, FMS7_CONT_EXCHG_RATE_MST B WHERE A.EXCHG_RATE_CD = B.EXC_RATE_CD  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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

			filename = "xls_reports/FMS_EXCHG_RATE_ENTRY.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_EXCHG_RATE_ENTRY>>,,", conn);
			System.out.println("<<END>><<FMS_EXCHG_RATE_ENTRY>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_EXCHG_RATE_ENTRY()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_BANK_MST() throws SQLException, IOException {
		function_nm = "FMS_BANK_MST()";
		try {

			System.out.println("<<START>><<FMS_BANK_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_BANK_MST>>,,,", conn);
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
			queryString = "SELECT BANK_CD, BANK_NAME, BANK_ABBR, TO_CHAR(EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), BRANCH_NAME, ADDR, CITY, PIN, STATE, COUNTRY, PHONE, MOBILE, ALT_PHONE, FAX_1, FAX_2, EMAIL, REMARK, BRANCH_IFSC_CD, NVL(FLAG, 'Y'), '2', EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, NULL FROM FMS7_BANK_MST WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();
			logger.checkpoint(fname, "BANK_CD,BANK_NAME, BANK_ABBR,EFF_DT,TIMESTAMP", conn);
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

			filename = "xls_reports/FMS_BANK_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_BANK_MST>>,,,", conn);
			System.out.println("<<END>><<FMS_BANK_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_BANK_MST()  ", e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_TURNOVER_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_TURNOVER_DTL()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_TURNOVER_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_TURNOVER_DTL>>,,,", conn);
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,FINANCIAL_YEAR,TURNOVER_CD,TURNOVER_FLAG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count = 0;
			String financial_yr = "";
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
				abbr = rset.getString(1);
				// CUSTOMER
				queryString1 = "SELECT B.CUSTOMER_CD, 'C', B.FINANCIAL_YEAR, B.TURNOVER_CD, B.TURNOVER_FLAG, B.ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_CUSTOMER_TURNOVER_DTL B WHERE B.CUSTOMER_CD = (SELECT DISTINCT(D.CUSTOMER_CD) FROM FMS7_CUSTOMER_MST D WHERE B.CUSTOMER_CD = D.CUSTOMER_CD AND D.COUNTERPARTY_CD = ? )  AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.CUSTOMER_CD ";
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
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + financial_yr + ","), conn, "");

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
					count++;
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + financial_yr + ","), conn, "");

				}
				stmt1.close();
				rset1.close();

			}

			stmt.close();
			rset.close();

			filename = "xls_reports/FMS_ENTITY_TURNOVER_DTL.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_TURNOVER_DTL>>,,,", conn);
			System.out.println("<<END>><<FMS_ENTITY_TURNOVER_DTL>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_TURNOVER_DTL()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_SHIP_MST() throws SQLException, IOException {
		function_nm = "FMS_SHIP_MST()";
		try {

			System.out.println("<<START>><<FMS_SHIP_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_SHIP_MST>>,,", conn);
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
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
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

			filename = "xls_reports/FMS_SHIP_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",, "), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_SHIP_MST>>,,", conn);
			System.out.println("<<END>><<FMS_SHIP_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_SHIP_MST()  ", e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_CONTACT_MST() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_CONTACT_MST()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_CONTACT_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_CONTACT_MST>>,,", conn);
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ADDR_IS_ACTIVE,TO_NOM,TO_INV,TO_FM,TO_PM,TO_JT,TO_OTHER,RM_FLAG,TO_RM";

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

			Map<String, String> cp_seq = new HashMap<String, String>();
			// Below block of code is for inserting data (COUNTERPARTY)
			queryString = "SELECT DISTINCT(B.COUNTERPARTY_ABBR), B.COUNTERPARTY_CD, B.EFF_DT  FROM FMS9_COUNTERPTY_MST B WHERE B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS9_COUNTERPTY_MST C WHERE B.COUNTERPARTY_CD = C.COUNTERPARTY_CD) AND B.COUNTERPARTY_ABBR != 'SEIPL' AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.COUNTERPARTY_CD, B.EFF_DT DESC   ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, delta_FromDt);
			stmt.setString(2, delta_FromDt);
			stmt.setString(3, delta_ToDt);
			stmt.setString(4, delta_ToDt);
			rset = stmt.executeQuery();

			while (rset.next()) {

				abbr = rset.getString(1);
				cd = rset.getString(2);

				// CUSTOMER
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'C', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.NOM_FLAG, A.INV_FLAG, A.FM_FLAG, A.PM_FLAG, A.JT_FLAG, A.OTHER_FLAG, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, 'N', NVL(A.INV_TO_FLAG, 'N'), 'N', 'N', 'N', 'N', 'N', 'N'  FROM FMS7_CUSTOMER_CONTACT_MST A, FMS7_CUSTOMER_MST B WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.CUSTOMER_CD, UPPER(A.CONTACT_PERSON), A.SEQ_NO ";
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
					entity = rset1.getString(2);
					seq_no = rset1.getString(3);
					eff_dt = rset1.getString(4);

					row = spreadsheet.createRow(nrow++);

					value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
					cell = row.createCell(0);
					cell.setCellValue("'" + value + "'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
						if (i == 3) {
							if (cp_seq.containsKey(rset1.getString(1) + rset1.getString(5).toUpperCase())) {
								cell.setCellValue(
										"'" + cp_seq.get(rset1.getString(1) + rset1.getString(5).toUpperCase()) + "'");
							} else {
								cell.setCellValue("'" + value + "'");
								cp_seq.put(rset1.getString(1) + rset1.getString(5).toUpperCase(), value);
							}
						}
						/*
						 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
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

				// TRADER
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'T', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.NOM_FLAG, A.INV_FLAG, A.FM_FLAG, A.PM_FLAG, A.JT_FLAG, A.OTHER_FLAG, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N'  FROM FMS7_TRADER_CONTACT_MST A, FMS7_TRADER_MST B WHERE A.TRADER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.TRADER_CD, UPPER(A.CONTACT_PERSON), A.SEQ_NO ";
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
					entity = rset1.getString(2);
					seq_no = rset1.getString(3);
					eff_dt = rset1.getString(4);
					value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
					cell = row.createCell(0);
					cell.setCellValue("'" + value + "'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
						if (i == 3) {
							if (cp_seq.containsKey(rset1.getString(1) + rset1.getString(5).toUpperCase())) {
								cell.setCellValue(
										"'" + cp_seq.get(rset1.getString(1) + rset1.getString(5).toUpperCase()) + "'");
							} else {
								cell.setCellValue("'" + value + "'");
								cp_seq.put(rset1.getString(1) + rset1.getString(5).toUpperCase(), value);
							}
						}
						/*
						 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
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

				// TRANSPORTER
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'R', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.NOM_FLAG, A.INV_FLAG, A.FM_FLAG, A.PM_FLAG, A.JT_FLAG, A.OTHER_FLAG, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N'  FROM FMS7_TRANSPORTER_CONTACT_MST A, FMS7_TRANSPORTER_MST B WHERE A.TRANSPORTER_CD = B.TRANSPORTER_CD AND B.COUNTERPARTY_CD = ? AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.TRANSPORTER_CD, UPPER(A.CONTACT_PERSON), A.SEQ_NO ";
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
					entity = rset1.getString(2);
					seq_no = rset1.getString(3);
					eff_dt = rset1.getString(4);
					row = spreadsheet.createRow(nrow++);

					value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
					cell = row.createCell(0);
					cell.setCellValue("'" + value + "'");

					for (int i = 1; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
						if (i == 3) {
							if (cp_seq.containsKey(rset1.getString(1) + rset1.getString(5).toUpperCase())) {
								cell.setCellValue(
										"'" + cp_seq.get(rset1.getString(1) + rset1.getString(5).toUpperCase()) + "'");
							} else {
								cell.setCellValue("'" + value + "'");
								cp_seq.put(rset1.getString(1) + rset1.getString(5).toUpperCase(), value);
							}
						}
						/*
						 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
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

			// Below block of code is for inserting data (CHA)
			queryString = "SELECT DISTINCT(B.CHA_ABBR), B.CHA_CD, 'H', A.CONTACT_TYPE, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL_1, 'R', NULL, 'N', 'N', 'N', 'N', 'N', 'N', 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, 'Y', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N' FROM FMS7_CUS_HUS_AGT_CONTACT_MST A, FMS7_CUS_HOUSE_AGENT_MST B WHERE A.CHA_CD = B.CHA_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_CUS_HOUSE_AGENT_MST C WHERE B.CHA_CD = C.CHA_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.CHA_CD ";
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
				abbr = rset.getString(1);
				cd = rset.getString(2);
				entity = rset.getString(3);
				eff_dt = rset.getString(5);
				row = spreadsheet.createRow(nrow++);

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					if (i == 3) {
						value = (value.equalsIgnoreCase("B") ? "2" : "1");
						cell.setCellValue("'" + value + "'");
						seq_no = value;
					}
					/*
					 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (VESSEL AGENT)
			queryString = "SELECT DISTINCT(B.VESSEL_ABBR), B.VESSEL_CD, 'V', A.CONTACT_TYPE, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL_1, 'R', NULL, 'N', 'N', 'N', 'N', 'N', 'N', 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, 'Y', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N' FROM FMS7_VESSEL_AGENT_CONTACT_MST A, FMS7_VESSEL_AGENT_MST B WHERE A.VESSEL_CD = B.VESSEL_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_VESSEL_AGENT_MST C WHERE B.VESSEL_CD = C.VESSEL_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.VESSEL_CD ";
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
				abbr = rset.getString(1);
				cd = rset.getString(2);
				entity = rset.getString(3);
				eff_dt = rset.getString(5);
				row = spreadsheet.createRow(nrow++);

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					if (i == 3) {
						value = (value.equalsIgnoreCase("B") ? "2" : "1");
						cell.setCellValue("'" + value + "'");
						seq_no = value;
					}
					/*
					 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (SURVEYOR)
			queryString = "SELECT DISTINCT(B.SURVEYOR_ABBR), B.SURVEYOR_CD, 'S', A.CONTACT_TYPE, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL_1, 'R', NULL, 'N', 'N', 'N', 'N', 'N', 'N', 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, 'Y', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N' FROM FMS7_SURVEYOR_CONTACT_MST A, FMS7_SURVEYOR_MST B WHERE A.SURVEYOR_CD = B.SURVEYOR_CD AND B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS7_SURVEYOR_MST C WHERE B.SURVEYOR_CD = C.SURVEYOR_CD) AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY B.SURVEYOR_CD ";
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
				abbr = rset.getString(1);
				cd = rset.getString(2);
				entity = rset.getString(3);
				eff_dt = rset.getString(5);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					if (i == 3) {
						value = (value.equalsIgnoreCase("B") ? "2" : "1");
						cell.setCellValue("'" + value + "'");
						seq_no = value;
					}
					/*
					 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt.close();
			rset.close();

			// Below block of code is for inserting data (BUSINESS OWNER)
			cp_seq = new HashMap<String, String>();
			queryString = "SELECT B.SUPPLIER_ABBR, B.COUNTERPARTY_CD, 'B', A.SEQ_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.CONTACT_PERSON, A.CONTACT_DESIG, A.PHONE, A.MOBILE, A.FAX_1, A.FAX_2, A.EMAIL, A.ADDR_FLAG, A.ADDL_ADDR_LINE, A.NOM_FLAG, A.INV_FLAG, A.FM_FLAG, A.PM_FLAG, A.JT_FLAG, A.OTHER_FLAG, 'Y', TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.ACTIVE_FLAG, 'N', 'N', 'N', 'N', 'N', 'N', A.REMITTANCE_FLAG, 'N'  FROM FMS7_SUPPLIER_CONTACT_MST A, FMS7_SUPPLIER_MST B WHERE A.SUPPLIER_CD = B.SUPPLIER_CD AND UPPER(B.SUPPLIER_ABBR) = 'SEIPL' AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY A.SEQ_NO, UPPER(A.CONTACT_PERSON), A.SEQ_NO ";
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
				abbr = rset.getString(1);
				cd = rset.getString(2);
				entity = rset.getString(3);
				seq_no = rset.getString(4);
				eff_dt = rset.getString(5);
				row = spreadsheet.createRow(nrow++);

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					if (i == 3) {
						if (cp_seq.containsKey(rset.getString(6).toUpperCase())) {
							cell.setCellValue("'" + cp_seq.get(rset.getString(6).toUpperCase()) + "'");
						} else {
							cell.setCellValue("'" + value + "'");
							cp_seq.put(rset.getString(6).toUpperCase(), value);
						}
					}
					/*
					 * else if (i == 22 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); cell.setCellValue("'"+value+"'"); }
					 */
					else if (i == 27) {
						queryString1 = "SELECT NVL(T_INV_TO, 'N') FROM DLNG_MAIL_SETUP_MST WHERE SEQ_NO = ? AND COMMODITY_TYPE = 'RLNG' ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, rset.getString(4));
						rset1 = stmt1.executeQuery();
						if (rset1.next()) {
							cell.setCellValue("'" + rset1.getString(1) + "'");
						} else {
							cell.setCellValue("'N'");
						}
						rset1.close();
						stmt1.close();
					} else if (i == 12 && value.contains("P")) {
						value = value.split("P")[1];
						value = "P" + (Integer.parseInt(value) + 1);
						cell.setCellValue("'" + value + "'");
					} else {
						cell.setCellValue("'" + value + "'");
					}
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + eff_dt + ","), conn, "");

			}
			stmt.close();
			rset.close();

			filename = "xls_reports/FMS_ENTITY_CONTACT_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_CONTACT_MST>>,,,,", conn);
			System.out.println("<<END>><<FMS_ENTITY_CONTACT_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_CONTACT_MST()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_METER_MST() throws SQLException, IOException {
		function_nm = "FMS_METER_MST()";
		try {

			System.out.println("<<START>><<FMS_METER_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_METER_MST>>,,,,", conn);
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
			queryString = "SELECT B.TRANSPORTER_ABBR, B.TRANSPORTER_CD, A.METER_SEQ_NO, 'R', '1', A.METER_ID, NULL, A.SPECIFICATION, A.NOTE, 'Y', A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, A.TRANS_CUST_CD FROM FMS7_METER_MST A, FMS7_TRANSPORTER_MST B WHERE A.TRANS_CUST_CD = B.TRANSPORTER_CD  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
				abbr = rset.getString(1);
				cd = rset.getString(2);
				plant_sq = rset.getString(3);
				meter_sq = rset.getString(5);
				id = rset.getString(6);

				row = spreadsheet.createRow(nrow++);
				value = "";

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					value = value.trim().equals("") ? "null" : value;
					if (i == 5) {
						value = value.equals("null") ? "" : value;
						value = value + "-" + rset.getInt(3);
						id = value;
						if (value.length() > 20) {
							value = value.substring(0, 20);
						}
					}
					/*
					 * else if (i == 10 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");

				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + plant_sq + "," + meter_sq + "," + id + ","), conn, "");

//				FMS7_METER_DTL
				queryString1 = " SELECT NULL, NULL, NULL, NULL, METER_SEQ_NO, NULL, NULL, SPECIFICATION, NOTE, FLAG, EMP_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL FROM FMS7_METER_DTL WHERE METER_ID = ?  AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, rset.getString(6));
				stmt1.setString(2, delta_FromDt);
				stmt1.setString(3, delta_FromDt);
				stmt1.setString(4, delta_ToDt);
				stmt1.setString(5, delta_ToDt);
				rset1 = stmt1.executeQuery();

				while (rset1.next()) {

					abbr = rset.getString(1);
					cd = rset.getString(2);
					meter_sq = rset1.getString(5);
					plant_sq = rset.getString(3);
					id = rset.getString(6);
					row = spreadsheet.createRow(nrow++);
					value = "";

					for (int i = 0; i < columns.split(",").length; i++) {
						cell = row.createCell(i);
						if (i == 0 || i == 1 || i == 2 || i == 3 || i == 6) {
							value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
						} else if (i == 5) {
							value = value.equals("null") ? "" : value;
							value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
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
							value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
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
			queryString = "SELECT B.TRANSPORTER_ABBR, B.TRANSPORTER_CD, '0', 'R', '0', A.METER_ID, NULL, A.SPECIFICATION, A.NOTE, 'Y', A.EMP_CD, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, NULL, A.TRANS_CUST_CD FROM FMS7_METER_MST A, FMS7_TRANSPORTER_MST B WHERE A.TRANS_CUST_CD = B.TRANSPORTER_CD AND A.TRANS_CUST_CD = (SELECT DISTINCT(C.TRANS_CUST_CD) FROM FMS7_METER_MST C WHERE A.TRANS_CUST_CD = C.TRANS_CUST_CD) AND A.METER_SEQ_NO = '1'  AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
				value = "";
				abbr = rset.getString(1);
				cd = rset.getString(2);
				plant_sq = rset.getString(3);
				meter_sq = rset.getString(5);
				id = rset.getString(6);

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
					value = value.trim().equals("") ? "null" : value;
					if (i == 5) {
						value = value.equals("null") ? "" : value;
						if (value.length() > 20) {
							value = value.substring(0, 20);
						}
					}
					/*
					 * else if (i == 10 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + plant_sq + "," + meter_sq + "," + id + ","), conn, "");

			}

			stmt.close();
			rset.close();

			filename = "xls_reports/FMS_METER_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_METER_MST>>,,,,", conn);
			System.out.println("<<END>><<FMS_METER_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_METER_MST()  ", e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_GOVT_STAT_TAX() throws SQLException, IOException {
		function_nm = "FMS_GOVT_STAT_TAX()";
		try {
			System.out.println("<<START>><<FMS_GOVT_STAT_TAX>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_GOVT_STAT_TAX>>,,", conn);
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
					value = rset.getString(i + 1) == null ? "null" : rset.getString(i + 1).replace("'", "");
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

			filename = "xls_reports/FMS_GOVT_STAT_TAX.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_GOVT_STAT_TAX>>,,", conn);
			System.out.println("<<END>><<FMS_GOVT_STAT_TAX>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_GOVT_STAT_TAX()  ", e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COUNTERPARTY_PLANT_TAX() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_PLANT_TAX()";
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_PLANT_TAX>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_COUNTERPARTY_PLANT_TAX>>,,,,", conn);
			
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_NM,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			
			nrow = 0;
			count = 0;
			String st_nm = "", plant_map = "";
			
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
				abbr = rset.getString(1);
				cd = rset.getString(2);

				// CUSTOMER
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'C', A.PLANT_SEQ_NO, C.STAT_NM, A.STAT_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.FLAG, A.REMARK, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, C.STAT_TYPE FROM FMS7_CUSTOMER_PLANT_TAX_CDS A, FMS7_CUSTOMER_MST B, FMS7_GOVT_STAT_NO C WHERE A.CUSTOMER_CD = B.CUSTOMER_CD AND B.COUNTERPARTY_CD = ? AND C.STAT_CD = A.STAT_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
					plant_map = abbr + "-" + rset1.getString(3);
					
					if (!customer_delete.contains(plant_map))  {
						row = spreadsheet.createRow(nrow++);
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						st_nm = rset1.getString(4);
						value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
						cell = row.createCell(0);
						cell.setCellValue("'" + value + "'");
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
							if (i == 4) {
								value = value + "-" + rset1.getString(13);
							}
							/*
							 * else if (i == 10 && !value.equals("null")) { // emp_abbr value =
							 * getEmpAbbr(value); }
							 */
							cell.setCellValue("'" + value + "'");
						}
						count++;
						logger.data(fname, (abbr + "," + cd + "," + entity + "," + seq_no + "," + st_nm + ","), conn,
								"");
					}

				}
				stmt1.close();
				rset1.close();

				// TRADER
				queryString1 = "SELECT B.COUNTERPARTY_CD, 'T', A.PLANT_SEQ_NO, C.STAT_NM, A.STAT_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.FLAG, A.REMARK, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, C.STAT_TYPE FROM FMS7_TRADER_PLANT_TAX_CDS A, FMS7_TRADER_MST B, FMS7_GOVT_STAT_NO C WHERE A.TRADER_CD = B.TRADER_CD AND B.COUNTERPARTY_CD = ?  AND C.STAT_CD = A.STAT_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
					plant_map = abbr + "-" + rset1.getString(3);
					
					if (!trader_delete.contains(plant_map))  {
						row = spreadsheet.createRow(nrow++);
						entity = rset1.getString(2);
						seq_no = rset1.getString(3);
						st_nm = rset1.getString(4);
						value = rset.getString(1) == null ? "null" : rset.getString(1).replace("'", "");
						cell = row.createCell(0);
						cell.setCellValue("'" + value + "'");
						for (int i = 1; i < columns.split(",").length; i++) {
							cell = row.createCell(i);
							value = rset1.getString(i) == null ? "null" : rset1.getString(i).replace("'", "");
							if (i == 4) {
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

				}
				stmt1.close();
				rset1.close();

			}

			stmt.close();
			rset.close();

			// SUPPLIER
			queryString1 = "SELECT B.SUPPLIER_ABBR, '2', 'B', '1', GST_TIN_NO, TO_CHAR(B.GST_TIN_DT, 'DD/MM/YYYY hh24:mi:ss'), CST_TIN_NO, TO_CHAR(B.CST_TIN_DT, 'DD/MM/YYYY hh24:mi:ss'), PAN_NO, TO_CHAR(B.PAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), TAN_NO, TO_CHAR(B.TAN_ISSUE_DT, 'DD/MM/YYYY hh24:mi:ss'), GSTIN_NO, TO_CHAR(B.GSTIN_DT, 'DD/MM/YYYY hh24:mi:ss'), 'Y', NULL, TO_CHAR(B.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), B.EMP_CD, NULL, NULL FROM FMS7_SUPPLIER_MST B WHERE B.SUPPLIER_ABBR = 'SEIPL' AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();

			if (rset1.next()) {
				abbr = rset1.getString(1);
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
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
						cell = row.createCell(i);
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(5) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(6) == null ? "'null'" : rset1.getString(6).replace("'", "");
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
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
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
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
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
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
						cell = row.createCell(i);
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(11) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(12) == null ? "'null'" : rset1.getString(12).replace("'", "");
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
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
						cell = row.createCell(i);
						cell.setCellValue("'" + value + "'");
					}

					cell = row.createCell(4); // For stat_nm
					cell.setCellValue("'" + rset.getString(2) + "-" + rset.getString(3) + "'");

					cell = row.createCell(5); // For stat_no
					cell.setCellValue("'" + rset1.getString(13) + "'");

					cell = row.createCell(6); // For eff_dt
					value = rset1.getString(14) == null ? "'null'" : rset1.getString(14).replace("'", "");
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

			queryString1 = "SELECT B.SUPPLIER_ABBR, '2', 'B', (A.PLANT_SEQ_NO+1), C.STAT_NM, A.STAT_NO, TO_CHAR(A.EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), A.FLAG, A.REMARK, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, C.STAT_TYPE FROM FMS7_SUPPLIER_PLANT_TAX_CDS A, FMS7_SUPPLIER_MST B, FMS7_GOVT_STAT_NO C WHERE A.SUPPLIER_CD = B.SUPPLIER_CD  AND A.STAT_CD = C.STAT_CD AND (? IS NULL OR A.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR A.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR B.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR C.ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
				abbr = rset1.getString(1);
				cd = rset1.getString(2);
				entity = rset1.getString(3);
				seq_no = rset1.getString(4);
				st_nm = rset1.getString(5);

				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
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

			filename = "xls_reports/FMS_COUNTERPARTY_PLANT_TAX.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_PLANT_TAX>>,,,,", conn);
			System.out.println("<<END>><<FMS_COUNTERPARTY_PLANT_TAX>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_PLANT_TAX()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_COUNTERPARTY_BU_TAX() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_BU_TAX()";
		try {

			System.out.println("<<START>><<FMS_COUNTERPARTY_BU_TAX>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_COUNTERPARTY_BU_TAX>>,,,", conn);
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
				abbr = rset1.getString(1);
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
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
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
				abbr = rset1.getString(1);
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
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
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
				abbr = rset1.getString(1);
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
						value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
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
					logger.data(fname, (abbr + "," + cd + "," + entity + "," + st_nm + ","), conn, "");

				}
				rset.close();
				stmt.close();

			}
			stmt1.close();
			rset1.close();

			filename = "xls_reports/FMS_COUNTERPARTY_BU_TAX.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_COUNTERPARTY_BU_TAX>>,,,", conn);
			System.out.println("<<END>><<FMS_COUNTERPARTY_BU_TAX>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_COUNTERPARTY_BU_TAX()  ",
					e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_TAX_MST() throws SQLException, IOException {
		function_nm = "FMS_TAX_MST()";
		try {

			System.out.println("<<START>><<FMS_TAX_MST>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TAX_MST>>,,", conn);
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

			filename = "xls_reports/FMS_TAX_MST.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TAX_MST>>,,", conn);
			System.out.println("<<END>><<FMS_TAX_MST>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_TAX_MST()  ", e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_TAX_STRUCTURE() throws SQLException, IOException {
		function_nm = "FMS_TAX_STRUCTURE()";
		try {

			System.out.println("<<START>><<FMS_TAX_STRUCTURE>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TAX_STRUCTURE>>,,", conn);
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

			filename = "xls_reports/FMS_TAX_STRUCTURE.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TAX_STRUCTURE>>,,", conn);
			System.out.println("<<END>><<FMS_TAX_STRUCTURE>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_TAX_STRUCTURE()  ", e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_TAX_STRUCTURE_DTL() throws SQLException, IOException {
		function_nm = "FMS_TAX_STRUCTURE_DTL()";
		try {

			System.out.println("<<START>><<FMS_TAX_STRUCTURE_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_TAX_STRUCTURE_DTL>>,,", conn);
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

			filename = "xls_reports/FMS_TAX_STRUCTURE_DTL.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_TAX_STRUCTURE_DTL>>,,", conn);
			System.out.println("<<END>><<FMS_TAX_STRUCTURE_DTL>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_TAX_STRUCTURE_DTL()  ",e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_TAX_STRUCT_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_TAX_STRUCT_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_ENTITY_TAX_STRUCT_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_TAX_STRUCT_DTL>>,,,", conn);
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TAX_STRUCT_DT,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT,SAP_TAX_CODE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;

			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data

			// CUSTOMER
			queryString1 = "SELECT CUSTOMER_ABBR, CUSTOMER_CD, 'C', PLANT_SEQ_NO, TAX_STRUCT_CD, TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss'), TAX_STRUCT_DTL, TAX_STRUCT_REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(CUSTOMER_EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL FROM FMS7_CUSTOMER_TAX_STRUCT_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_CD ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TIMESTAMP", conn);
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				
				abbr=rset1.getString(1);
				cd=rset1.getString(2);
				entity=rset1.getString(3);
				seq_no=rset1.getString(4);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					/*
					 * if (i == 9 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
					 * }
					 */
					cell.setCellValue("'" + value + "'");
				}count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + ","+ seq_no + ","), conn, "");

			}

			stmt1.close();
			rset1.close();

			// TRADER
			queryString1 = "SELECT TRADER_ABBR, TRADER_CD, 'T', PLANT_SEQ_NO, TAX_STRUCT_CD, TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss'), TAX_STRUCT_DTL, TAX_STRUCT_REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(TRADER_EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL FROM FMS7_TRADER_TAX_STRUCT_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY TRADER_CD ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();

			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				abbr=rset1.getString(1);
				cd=rset1.getString(2);
				entity=rset1.getString(3);
				seq_no=rset1.getString(4);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					/*
					 * if (i == 9 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
					 * }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + ","+ seq_no + ","), conn, "");

			}

			stmt1.close();
			rset1.close();

			filename = "xls_reports/FMS_ENTITY_TAX_STRUCT_DTL.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_TAX_STRUCT_DTL>>,,,", conn);
			System.out.println("<<END>><<FMS_ENTITY_TAX_STRUCT_DTL>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_TAX_STRUCT_DTL()  ",e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_SERVICE_TAX_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_SERVICE_TAX_DTL()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_SERVICE_TAX_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_SERVICE_TAX_DTL>>,,,,", conn);
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TAX_STRUCT_DT,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT,SAP_TAX_CODE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count=0;
			String tax_cd="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);
			
			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data

			// CUSTOMER
			queryString1 = "SELECT CUSTOMER_ABBR, CUSTOMER_CD, 'C', PLANT_SEQ_NO, TAX_STRUCT_CD, TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, TAX_STRUCT_DTL, TAX_STRUCT_REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(CUSTOMER_EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL FROM FMS7_CUSTOMER_SERVICE_TAX_DTL  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_CD ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();
			logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TIMESTAMP", conn);
			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				abbr=rset1.getString(1);
				cd=rset1.getString(2);
				entity=rset1.getString(3);
				seq_no=rset1.getString(4);
				tax_cd=rset1.getString(5);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					/*
					 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + ","+ seq_no + ","+ tax_cd + ","), conn, "");

			}

			stmt1.close();
			rset1.close();

			// CUSTOMER TAX DTL INVOICE-WISE
			queryString1 = "SELECT CUSTOMER_ABBR, CUSTOMER_CD, 'C', PLANT_SEQ_NO, TAX_STRUCT_CD, TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss'), INVFLAG, TAX_STRUCT_DTL, TAX_STRUCT_REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(CUSTOMER_EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL FROM FMS7_TAX_DTL_INVOICEWISE  WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY CUSTOMER_CD ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();

			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				abbr=rset1.getString(1);
				cd=rset1.getString(2);
				entity=rset1.getString(3);
				seq_no=rset1.getString(4);
				tax_cd=rset1.getString(5);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					if (i == 6) {
						if (value.equals("U")) {
							value = "UG";
						} else if (value.equals("M")) {
							value = "LP";
						} else if (value.equals("B")) {
							value = "ST";
						} else if (value.equals("A")) {
							value = "RV";
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
			queryString1 = "SELECT TRADER_ABBR, TRADER_CD, 'T', PLANT_SEQ_NO, TAX_STRUCT_CD, TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, TAX_STRUCT_DTL, TAX_STRUCT_REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(TRADER_EFF_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL FROM FMS7_TRADER_SERVICE_TAX_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY TRADER_CD ";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, delta_FromDt);
			stmt1.setString(2, delta_FromDt);
			stmt1.setString(3, delta_ToDt);
			stmt1.setString(4, delta_ToDt);
			rset1 = stmt1.executeQuery();

			while (rset1.next()) {
				row = spreadsheet.createRow(nrow++);
				abbr=rset1.getString(1);
				cd=rset1.getString(2);
				entity=rset1.getString(3);
				seq_no=rset1.getString(4);
				tax_cd=rset1.getString(5);
				for (int i = 0; i < columns.split(",").length; i++) {
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					/*
					 * if (i == 10 && !value.equals("null")) { // emp_abbr value =
					 * getEmpAbbr(value); }
					 */
					cell.setCellValue("'" + value + "'");
				}	count++;
				logger.data(fname, (abbr + "," + cd + "," + entity + ","+ seq_no + ","+ tax_cd + ","), conn, "");

			}

			stmt1.close();
			rset1.close();

			filename = "xls_reports/FMS_ENTITY_SERVICE_TAX_DTL.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_SERVICE_TAX_DTL>>,,,,", conn);
			System.out.println("<<END>><<FMS_ENTITY_SERVICE_TAX_DTL>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_SERVICE_TAX_DTL()  ",e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_ENTITY_BU_SVC_TAX_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_BU_SVC_TAX_DTL()";
		try {

			System.out.println("<<START>><<FMS_ENTITY_BU_SVC_TAX_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_ENTITY_BU_SVC_TAX_DTL>>,,,,", conn);
			columns = "COUNTERPARTY_ABBR,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TAX_STRUCT_DT,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT,SAP_TAX_CODE";

			workbook = new XSSFWorkbook();
			spreadsheet = workbook.createSheet("Sheet 1");
			nrow = 0;
			count=0;
			String tax_cd="";
			// Below block of code is for inserting columns
			row = spreadsheet.createRow(nrow++);

			for (int i = 0; i < columns.split(",").length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns.split(",")[i]);
			}

			// Below block of code is for inserting data

			// CHA, VA, SURVEYOR
			queryString1 = "SELECT NULL, ENTITY_CD, ENTITY_TYPE, '1', TAX_STRUCT_CD, TO_CHAR(TAX_STRUCT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL, TAX_STRUCT_DTL, TAX_STRUCT_REMARK, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), EMP_CD, NULL, NULL, 'Y', '1', TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), NULL FROM FMS7_CARGO_ENTITY_TAX_DTL WHERE (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ORDER BY ENTITY_CD ";
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
				for (int i = 0; i < columns.split(",").length; i++) 
				{
					cell = row.createCell(i);
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					if (i == 0) { // counterparty_abbr

						if (rset1.getString(3).equals("C")) {
							queryString = "SELECT CHA_ABBR FROM FMS7_CUS_HOUSE_AGENT_MST WHERE CHA_CD = ? ";
						
						}

						else if (rset1.getString(3).equals("S")) {
							queryString = "SELECT SURVEYOR_ABBR FROM FMS7_SURVEYOR_MST WHERE SURVEYOR_CD = ? ";
						}

						else if (rset1.getString(3).equals("V")) {
							queryString = "SELECT VESSEL_ABBR FROM FMS7_VESSEL_AGENT_MST WHERE VESSEL_CD = ? ";
						}

						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, rset1.getString(2));
						rset = stmt.executeQuery();
						if (rset.next()) {
							value = rset.getString(1);
							abbr=value;
						}
						rset.close();
						stmt.close();
					} else if (i == 2 && rset1.getString(3).equals("C")) { // Entity_type
						value = "H";
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

			filename = "xls_reports/FMS_ENTITY_BU_SVC_TAX_DTL.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,,,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_ENTITY_BU_SVC_TAX_DTL>>,,,,", conn);
			System.out.println("<<END>><<FMS_ENTITY_BU_SVC_TAX_DTL>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_ENTITY_BU_SVC_TAX_DTL()  ",e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}

	public void FMS_CUSTOM_TAX_STRUCT_DTL() throws SQLException, IOException {
		function_nm = "FMS_CUSTOM_TAX_STRUCT_DTL()";
		try {

			System.out.println("<<START>><<FMS_CUSTOM_TAX_STRUCT_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_CUSTOM_TAX_STRUCT_DTL>>,,", conn);
			columns = "EFF_DT,TAX_STRUCT_CD,TAX_STRUCT_DT,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,SAP_TAX_CODE,COMPANY_CD";

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
			queryString1 = "SELECT TO_CHAR(A.APP_DATE, 'DD/MM/YYYY hh24:mi:ss'), A.TAX_STR_CD, TO_CHAR(B.APP_DATE, 'DD/MM/YYYY hh24:mi:ss'), A.TAX_DESC, A.REMARK, TO_CHAR(A.ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), A.EMP_CD, NULL, NULL, A.FLAG, NULL, '2' FROM FMS7_CARGO_TAX_MST A, FMS7_TAX_STRUCTURE B WHERE A.TAX_STR_CD = B.TAX_STR_CD AND (? IS NULL OR ENT_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) AND (? IS NULL OR ENT_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')) ";
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
					value = rset1.getString(i + 1) == null ? "null" : rset1.getString(i + 1).replace("'", "");
					/*
					 * if (i == 6 && !value.equals("null")) { // emp_abbr value = getEmpAbbr(value);
					 * }
					 */
					cell.setCellValue("'" + value + "'");
				}
				count++;
				logger.data(fname, (eff_dt + "," + cd + "," +tax_str_dt+","), conn, "");

			}

			stmt1.close();
			rset1.close();

			filename = "xls_reports/FMS_CUSTOM_TAX_STRUCT_DTL.xlsx";

			fileOut = new FileOutputStream(filename);

			workbook.write(fileOut);
			fileOut.close();
			
			logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ",,"), conn);
			logger.checkpoint(fname, "<<END>>,<<FMS_CUSTOM_TAX_STRUCT_DTL>>,,", conn);
			System.out.println("<<END>><<FMS_CUSTOM_TAX_STRUCT_DTL>>");
			System.out.println();

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_CUSTOM_TAX_STRUCT_DTL()  ",e);
			logger.error(fname, e, function_nm, conn, fname_error);
		}

	}
	
	public void FMS_HOLIDAY_DTL() throws SQLException, IOException {
		
		function_nm = "FMS_HOLIDAY_DTL()";

		try {
			
			System.out.println("<<START>><<FMS_HOLIDAY_DTL>>");
			logger.checkpoint(fname, "<<START>>,<<FMS_HOLIDAY_DTL>>,", conn);
			
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
				
				
				stmt.close();
				rset.close();

				filename = "xls_reports/FMS_HOLIDAY_DTL.xlsx";

				fileOut = new FileOutputStream(filename);

				workbook.write(fileOut);
				fileOut.close();
				
				logger.checkpoint(fname, ("\nTOTAL DATA EXTRACTED :," + count + ", "), conn);
				logger.checkpoint(fname, "<<END>>,<<FMS_HOLIDAY_DTL>>,", conn);
				
				System.out.println("<<END>><<FMS_HOLIDAY_DTL>>");
				System.out.println();


			}
		catch(Exception e){
			
			LOGGER.log(Level.WARNING,"Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> FMS_HOLIDAY_DTL()  ", e);
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
		try {
			String strline = "";

			File fsetup = new File("market_risk/Setup.txt");
			String mail_list_path = fsetup.getAbsolutePath();
			FileInputStream f1 = new FileInputStream(mail_list_path);
			DataInputStream in = new DataInputStream(f1);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

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
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> getmail_list() ", e);
		}
	}

	
	public void getCustomerTraderList() {
		try
		{
			String strline = "";
			
			File fsetup=new File("WEB-INF/src/com/hlpl/hazira/fms7/market_risk/Migration_Plants_Exceptions.txt");
			String file_path=fsetup.getAbsolutePath();
			FileInputStream f1 = new FileInputStream(file_path);
			DataInputStream in = new DataInputStream(f1);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			while((strline = br.readLine())!=null)
			{
				if(strline.startsWith("CUSTOMER-PLANTS-DELETE"))
				{
					String  tmp[]=strline.split("CUSTOMER-PLANTS-DELETE: ");
					customer_delete = tmp[1].toString();
					
				}
				if(strline.startsWith("TRADER-PLANTS-DELETE"))
				{
					String  tmp[]=strline.split("TRADER-PLANTS-DELETE: ");
					trader_delete = tmp[1].toString();
				}
				if(strline.startsWith("CUSTOMER-PLANTS-MAPPING"))
				{
					String  tmp[]=strline.split("CUSTOMER-PLANTS-MAPPING: ");
					 customer_map = tmp[1].toString();
				}
				if(strline.startsWith("TRADER-PLANTS-MAPPING"))
				{
					String  tmp[]=strline.split("TRADER-PLANTS-MAPPING: ");
					trader_map = tmp[1].toString();
				}
			}
			
//			System.out.println(customer_delete);
//			System.out.println(trader_delete);
//			System.out.println(customer_map);
//			System.out.println(trader_map);
		}
		catch (Exception e) 
		{
			 LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> getCustomerTraderList() ", e); 
		}
	}


}
