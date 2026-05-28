package com.etrm.fms.migration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import java.util.prefs.Preferences;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;

public class Purchase_SEIPL_Data_Reader {

//	public static void main(String[] args) 
//	{
//		Purchase_Excel_Reader ex = new Purchase_Excel_Reader();
//		ex.init();
//	}
//	
//}
//
//class Purchase_Excel_Reader {

	String db_src_file_name="Purchase_SEIPL_Data_Reader.java";

	String migration_setup_dir = "";
	
	String queryString="", queryString1="", queryString2="",queryString5="",queryString3="";
	Connection conn;
	ResultSet rset,rset1,rset2,rset3,rset5;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt5;
	
	String data = "", table_name = "" , value = "" , value1 = "";	
	final String company_cd = "2";
	int num = 1, index = 0;
	
	String sysDateTime = "";
	String start_end_dt = null;
	
	String fname = "", function_nm = "";
	String fname_error = "", fname1 = "";
	
	int logger_count = 0;
	int skipped_count=0;  
	int total_count=0;
	
	String abbr = "", cd = "1",cargo_ref_no="",plant_seq_no="",cont_no="";
	
	String checked_values = "", msg = "", msg_type = "",columns="";
	
	File file1 = null;
	FileInputStream file = null;
	XSSFWorkbook workbook = null; 
	XSSFSheet sheet = null;
	Iterator<Row> rowIterator = null;
	Iterator<Cell> cellIterator = null;
	
	Cell cell;
	Row row;
	
	NumberFormat nf = new DecimalFormat("#########0.00");
	TaxCalculator TaxCalc = new TaxCalculator();

	DataMigration_Logger logger = new DataMigration_Logger();
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();	
	Migration_Plants_Exceptions mpe =new Migration_Plants_Exceptions();
	
	public void init() {

		function_nm="init()";
		try {
			
			fname = "DataLogs/Reader/Purchase_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/Purchase_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
			fname = migration_setup_dir + fname;
			fname_error = migration_setup_dir + fname_error;
			
			Preferences preferences =  Preferences.userRoot().node("/processFlag");	
		
			fname1 = "DataLogs/Script_Status(log).csv";	
			fname1 = migration_setup_dir + fname1;
		
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
					conn.setAutoCommit(false);
					preferences.put("Flag", "0");
					
					//FMS_TRADER_AGMT_MST();
					//FMS_TRADER_AGMT_BU();
					//FMS_TRADER_AGMT_PLANT();
					
					if (checked_values.contains("FMS_TRADER_AGMT_MST,")) {
						FMS_TRADER_AGMT_MST();
					}
					if (checked_values.contains("FMS_TRADER_AGMT_PLANT,")) {
						FMS_TRADER_AGMT_PLANT();
					}
					if (checked_values.contains("FMS_TRADER_AGMT_BU,")) {
						FMS_TRADER_AGMT_BU();
					}
					if (checked_values.contains("FMS_TRADER_AGMT_BILLING_DTL,")) {
						FMS_TRADER_AGMT_BILLING_DTL();
					}
					if (checked_values.contains("FMS_TRADER_CN_MST,")) {
						FMS_TRADER_CN_MST();
						FMS_TRADER_CONT_PLANT1();
						FMS_TRADER_CONT_BU1();
					}
					if (checked_values.contains("FMS_TRADER_CARGO_MST,")) {
						FMS_TRADER_CARGO_MST();
						FMS_TRADER_BILLING_DTL1();
					}
					if (checked_values.contains("FMS_PSEUDO_CARGO_DTL,")) {
						FMS_PSEUDO_CARGO_DTL();
					}
//					if (checked_values.contains("FMS_SECURITY_DEAL_MAP(P),")) {
//						FMS_SECURITY_DEAL_MAP();
//					}
//					if (checked_values.contains("FMS_SECURITY_MST(P),")) {
//						FMS_SECURITY_MST();
//					}
					
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
						FMS_CARGO_SVC_CONT_BU();
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
					if (checked_values.contains("FMS_TRADER_CONT_BU")) {
	    				FMS_TRADER_CONT_BU();
					}
					if (checked_values.contains("FMS_TRADER_CONT_SPLIT_PLANT")) {
						FMS_TRADER_CONT_SPLIT_PLANT();
					}
					if (checked_values.contains("FMS_TRADER_CONT_PLANT,")) {
	    				FMS_TRADER_CONT_PLANT();
					}
					if (checked_values.contains("FMS_TRADER_CONT_PLANT_CHRG,")) {
						FMS_TRADER_CONT_PLANT_CHRG();
		    		}
					if (checked_values.contains("FMS_TRADER_BILLING_DTL,")) {
						FMS_TRADER_BILLING_DTL();
					}
					if (checked_values.contains("FMS_BUY_DAILY_BUYER_NOM")) {
		    			FMS_BUY_DAILY_BUYER_NOM();
		    		}

					if (checked_values.contains("FMS_BUY_DAILY_SELLER_NOM")) {
		    				FMS_BUY_DAILY_SELLER_NOM();
		    		}
					if (checked_values.contains("FMS_BUY_DAILY_ALLOCATION,")) {
		    			FMS_BUY_DAILY_ALLOCATION();
		    		}
					if (checked_values.contains("FMS_BUY_DAILY_ALLOCATION_MM")) {
	    				FMS_BUY_DAILY_ALLOCATION_MM();
					}
					
					if (checked_values.contains("FMS_PUR_SG_INV_MST,")) { 
						FMS_PUR_SG_INV_MST();
						FMS_PUR_SG_INV_MST_PURCHASE();
						FMS_PUR_SG_INV_TAX_DTL();
					}
					if(checked_values.contains("FMS_CUSTOM_PD_BOND_DTL,"))
					{
						FMS_CUSTOM_PD_BOND_DTL();
					}
					if(checked_values.contains("FMS_PUR_PG_INV_MST,"))
					{
						FMS_PUR_PG_INV_MST();
						FMS_PUR_PG_INV_TAX_DTL();
					}
					
					
					if(checked_values.contains("LOG_TRADER_CONT_MST,"))
					{
						LOG_TRADER_CONT_MST();
					}
					
					if(checked_values.contains("LOG_TRADER_CN_MST,"))
					{
						LOG_TRADER_CN_MST();
					}
					if(checked_values.contains("LOG_TRADER_CARGO_MST,"))
					{
						LOG_TRADER_CARGO_MST();
					}
//					if (checked_values.contains("LOG_FMS_SECURITY_MST(P),")) {
//						LOG_FMS_SECURITY_MST();
//					}
					
					preferences.put("Flag", "1");
				}
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally
		{
			try
			{
				if(rset != null){try{rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
		}
		
	}

	public void FMS_TRADER_AGMT_MST() throws IOException, SQLException {

		function_nm="FMS_TRADER_AGMT_MST()";
		try {
			
			
			System.out.println("<<START>><<FMS_TRADER_AGMT_MST>>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_TRADER_AGMT_MST>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE";
			
			//FOR SITMEFZ
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SITMEFZ' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}

			queryString1 = "INSERT INTO FMS_TRADER_AGMT_MST (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,AGMT_BASE,AGMT_TYP,"
					+ "SIGNING_DT,START_DT,END_DT,STATUS,FLAG,REMARK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,"
					+ "DEMURRAGE,DEMURRAGE_CLAUSE,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,"
					+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
					+ "LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,REOPEN_REQUEST_FLAG,"
					+ "REOPEN_REQUEST_BY,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVE_BY,REOPEN_APPROVAL_DT,REOPEN_REMARK) "
					+ "VALUES (?,?,'M',1,0,'SEIPL-SITMEFZ-M1-REV0','MSPA-1','D','0',"
					+ "TO_DATE('29/04/2005 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),TO_DATE('29/04/2005 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
					+ "TO_DATE('29/04/2099 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),'A',NULL,NULL,113,TO_DATE('26/03/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
					+ "TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),113,NULL,'Y',"
					+ "NULL,'06:00','06:00','Y',NULL,50000,'2',36,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Y',NULL,NULL,NULL,NULL,"
					+ "NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
				
			//for data already exists..
				queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_TRADER_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
						+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, cd);
			
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next() ) {
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++; 
				}
				stmt5.close();
				rset5.close();
				
				//FOR SELNG
				queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SELNG' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				if(rset.next()) {
					cd  =  rset.getString(1);
				}
				queryString1 = "INSERT INTO FMS_TRADER_AGMT_MST (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,AGMT_BASE,AGMT_TYP,"
						+ "SIGNING_DT,START_DT,END_DT,STATUS,FLAG,REMARK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,"
						+ "DEMURRAGE,DEMURRAGE_CLAUSE,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,"
						+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
						+ "LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,REOPEN_REQUEST_FLAG,"
						+ "REOPEN_REQUEST_BY,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVE_BY,REOPEN_APPROVAL_DT,REOPEN_REMARK) "
						+ "values (?,?,'M',1,0,'SEIPL-SELNG-M1-REV0','MSPA-1','D','1',"
						+ "TO_DATE('29/04/2005 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),TO_DATE('29/04/2005 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_DATE('29/04/2020 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),'A',NULL,NULL,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),113,NULL,'Y',NULL,"
						+ "'06:00','06:00','Y',null,50000,'2',36,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Y',NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "NULL,NULL,NULL,NULL,NULL,NULL)";
				
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, cd);
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,,", conn);
				
				//for data already exists..
				queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_TRADER_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
						+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, cd);
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next() ) {
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++; 
				}
				stmt5.close();
				rset5.close();
				
				//FOR SITME
				queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SITME' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				if(rset.next()) {
					cd  =  rset.getString(1);
				}
				queryString1 = "INSERT INTO FMS_TRADER_AGMT_MST (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,AGMT_BASE,AGMT_TYP,"
						+ "SIGNING_DT,START_DT,END_DT,STATUS,FLAG,REMARK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,"
						+ "DEMURRAGE,DEMURRAGE_CLAUSE,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,"
						+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
						+ "LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,REOPEN_REQUEST_FLAG,"
						+ "REOPEN_REQUEST_BY,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVE_BY,REOPEN_APPROVAL_DT,REOPEN_REMARK) "
						+ "values (?,?,'M',1,0,'SEIPL-SITME-M1-REV0','MSPA-1','D','1',TO_DATE('29/04/2005 00:00:00', 'DD/MM/YYYY HH24:MI:SS')"
						+ ",TO_DATE('29/04/2005 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_DATE('29/04/2020 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),'A',NULL,NULL,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),113,NULL,'Y',NULL,"
						+ "'06:00','06:00','Y',null,50000,'2',36,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Y',NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "NULL,NULL,NULL,NULL,NULL,NULL)";
				
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, cd);
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,,", conn);
				
				//for data already exists..
				queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_TRADER_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
						+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, cd);
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next() ) {
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++; 
				}
				stmt5.close();
				rset5.close();
			
				//FOR TGPL
				queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'TGPL' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				if(rset.next()) {
					cd  =  rset.getString(1);
				}
				queryString1 = "INSERT INTO FMS_TRADER_AGMT_MST (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,AGMT_BASE,AGMT_TYP,"
						+ "SIGNING_DT,START_DT,END_DT,STATUS,FLAG,REMARK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,"
						+ "DEMURRAGE,DEMURRAGE_CLAUSE,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,"
						+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
						+ "LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,REOPEN_REQUEST_FLAG,"
						+ "REOPEN_REQUEST_BY,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVE_BY,REOPEN_APPROVAL_DT,REOPEN_REMARK) "
						+ "values (?,?,'M',1,0,'SEIPL-TGPL-M1-REV0','MSPA-1','D','1',TO_DATE('29/04/2005 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_DATE('29/04/2005 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_DATE('29/04/2020 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),'A',NULL,NULL,113,"
						+ "TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),113,NULL,'Y',NULL,"
						+ "'06:00','06:00','Y',null,50000,'2',36,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Y',NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "NULL,NULL,NULL,NULL,NULL,NULL)";
				
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, cd);
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,,", conn);
				
				//for data already exists..
				queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_TRADER_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
						+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, cd);
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next() ) {
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++; 
				}
				stmt5.close();
				rset5.close();
				
				//FOR GUNSPL
				queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'GUNSPL' ";
				stmt = conn.prepareStatement(queryString);
				rset = stmt.executeQuery();
				if(rset.next()) {
					cd  =  rset.getString(1);
				}
				queryString1 = "INSERT INTO FMS_TRADER_AGMT_MST (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,AGMT_BASE,AGMT_TYP,"
						+ "SIGNING_DT,START_DT,END_DT,STATUS,FLAG,REMARK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,"
						+ "DEMURRAGE,DEMURRAGE_CLAUSE,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,"
						+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
						+ "LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,REOPEN_REQUEST_FLAG,"
						+ "REOPEN_REQUEST_BY,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVE_BY,REOPEN_APPROVAL_DT,REOPEN_REMARK) "
						+ "values (?,?,'M',1,0,'SEIPL-GUNSPL-M1-REV0','MSPA-1','D','1',TO_DATE('29/04/2005 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_DATE('29/04/2005 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_DATE('29/04/2020 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),'A',NULL,NULL,113,"
						+ "TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),"
						+ "TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),113,NULL,'Y',NULL,"
						+ "'06:00','06:00','Y',null,50000,'2',36,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Y',NULL,NULL,NULL,NULL,NULL,NULL,"
						+ "NULL,NULL,NULL,NULL,NULL,NULL)";
				
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, company_cd);
				stmt1.setString(2, cd);
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,,", conn);
				
				//for data already exists..
				queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV FROM FMS_TRADER_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
						+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, cd);
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next() ) {
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++; 
				}
				stmt5.close();
				rset5.close();
				
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_TRADER_AGMT_MST>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_AGMT_MST>>,", conn);
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
		}
	}

	public void FMS_TRADER_AGMT_BU() throws IOException, SQLException {

		function_nm="FMS_TRADER_AGMT_BU()";
		try {
			
			
			System.out.println("<<START>><<FMS_TRADER_AGMT_BU>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRADER_AGMT_BU>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE";
			//1-SITMEFZ
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SITMEFZ' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_BU (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT)"
					+ " VALUES (?,?,'M',1,0,1,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
				
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//2-SELNG
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SELNG' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_BU (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES (?,?,'M',1,0,1,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			//3-SITME
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SITME' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_BU (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT)"
					+ " VALUES (?,?,'M',1,0,1,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//4-TGPL
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'TGPL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_BU (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES (?,?,'M',1,0,1,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//5-GUNSPL
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'GUNSPL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_BU (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES (?,?,'M',1,0,1,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
				
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_TRADER_AGMT_BU>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_AGMT_BU>>,", conn);
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
		}
	}

	public void FMS_TRADER_AGMT_PLANT() throws IOException, SQLException {

		function_nm="FMS_TRADER_AGMT_PLANT()";
		try {
			
			
			System.out.println("<<START>><<FMS_TRADER_AGMT_PLANT>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRADER_AGMT_PLANT>>,", conn);
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE";
			//1-SITMEFZ
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SITMEFZ' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_PLANT (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES (?,?,'M',1,0,1,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
				
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//2-SELNG
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SELNG' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_PLANT (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES (?,?,'M',1,0,1,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//3-SITME
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SITME' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_PLANT (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT)"
					+ " VALUES (?,?,'M',1,0,1,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//4-TGPL
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'TGPL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_PLANT (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES (?,?,'M',1,0,1,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//5-GUNSPL
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'GUNSPL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_PLANT (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
					+ "VALUES (?,?,'M',1,0,1,113,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
				
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_TRADER_AGMT_PLANT>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_AGMT_PLANT>>,", conn);
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
		}
	}
	
	public void FMS_TRADER_AGMT_BILLING_DTL() throws IOException, SQLException {

		function_nm="FMS_TRADER_AGMT_BILLING_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_TRADER_AGMT_BILLING_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRADER_AGMT_BILLING_DTL>>,", conn);
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE";
			//1-SITMEFZ
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SITMEFZ' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_BILLING_DTL (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
					+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
					+ "EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
					+ "VALUES (?,?,'M',1,0,'D','D',12,NULL,2,2,4,'+',5,NULL,NULL,NULL,NULL,NULL,TO_DATE('26/03/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),113,NULL,NULL,'C','N',NULL,NULL,"
					+ "1,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
				
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//2-SELNG
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SELNG' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_BILLING_DTL (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
					+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
					+ "EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
					+ "VALUES (?,?,'M',1,0,'D','D',12,NULL,2,2,4,'+',5,NULL,NULL,NULL,NULL,NULL,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),113,NULL,NULL,'B','N',NULL,NULL,"
					+ "1,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//3-SITME
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'SITME' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_BILLING_DTL (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
					+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
					+ "EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
					+ "VALUES (?,?,'M',1,0,'D','D',12,NULL,2,2,4,'+',5,NULL,NULL,NULL,NULL,NULL,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),113,NULL,NULL,'B','N',NULL,NULL,"
					+ "1,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//4-TGPL
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'TGPL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_BILLING_DTL (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
					+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
					+ "EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
					+ "VALUES (?,?,'M',1,0,'D','D',12,NULL,2,2,4,'+',5,NULL,NULL,NULL,NULL,NULL,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),113,NULL,NULL,'B','N',NULL,NULL,"
					+ "1,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
			
			//5-GUNSPL
			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = 'GUNSPL' ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			if(rset.next()) {
				cd  =  rset.getString(1);
			}
			queryString1 = "INSERT INTO FMS_TRADER_AGMT_BILLING_DTL (COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
					+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
					+ "EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
					+ "VALUES (?,?,'M',1,0,'D','D',12,NULL,2,2,4,'+',5,NULL,NULL,NULL,NULL,NULL,TO_DATE('15/04/2025 00:00:00', 'DD/MM/YYYY HH24:MI:SS'),113,NULL,NULL,'B','N',NULL,NULL,"
					+ "1,NULL)";
			
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, company_cd);
			stmt1.setString(2, cd);
			//TO CHECK DATA ALREADY EXISTS..
			queryString5 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO FROM FMS_TRADER_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_TYPE = 'M' AND AGMT_NO = '1' AND AGMT_REV = '0' AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, cd);
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				stmt1.executeUpdate();
				stmt1.close();
				logger_count++;
			}
			else {
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
				
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_TRADER_AGMT_BILLING_DTL>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_AGMT_BILLING_DTL>>,", conn);
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
		}
	}
	public void FMS_TRADER_CN_MST() throws IOException, SQLException {

		function_nm="FMS_TRADER_CN_MST()";
		try {
			table_name = "FMS_TRADER_CN_MST";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_TRADER_CN_MST>>,,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
		

			
			queryString1= "INSERT INTO FMS_TRADER_CN_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_BASE,AGMT_TYP,"
					+ "CONTRACT_TYPE,CONT_NO,CONT_REV,CONT_NAME,CONT_REF_NO,CONT_STATUS,DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,"
					+ "START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,REV_DT,NUM_CARGO,DAY_DEF_FLAG,DAY_DEF_CLAUSE,DAY_START_TIME,"
					+ "DAY_END_TIME,DEMURRAGE,DEMURRAGE_CLAUSE,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,"
					+ "MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,"
					+ "OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,FCC_FLAG,FCC_BY,FCC_DATE)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_CN_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_CN_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_TYPE,CONT_NO,CONT_REV,CARGO_REF(Mandate Conform no),TIMESTAMP", conn);
				//cargo ref in ems and mandate conform no in fms 
				
				while (rowIterator.hasNext()) {
					total_count++;
					String agmt_no = "", cont_type = "", cont_rev = "",cont_ref_no = "" ,cp_abbr="";
					int no = 0;
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
				    		data = company_cd;
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
				    	else if(cell.getColumnIndex() == 3) { //agmt_no
				    		queryString = "SELECT AGMT_NO FROM FMS_TRADER_AGMT_MST WHERE COUNTERPARTY_CD = ? AND COMPANY_CD = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cd);
				    		stmt.setString(2, company_cd);
				    		
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			agmt_no = rset.getString(1);
				    		}else {
				    			agmt_no = null;
				    		}
				    		rset.close();
				    		stmt.close();				    		
				    		data = agmt_no;	
				    	
				    	}
				    	else if(cell.getColumnIndex() == 8) { //cont_no
				    		
				    		cont_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if(cont_no!=null)
				    		{
				    		cont_no = cont_no.substring(3, cont_no.length()-1);
				    	
				    		}
				    		
				    		queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_TRADER_CN_MST WHERE CONT_NO LIKE ?   AND COMPANY_CD = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, "7" + cont_no + "%" );
				    		stmt.setString(2, company_cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next() && rset.getInt(1) > 0) {
				    			no = rset.getInt(1);
				    			
				    		}
				    		else{
				    			no = 1000;
			    				no = no * Integer.parseInt("7" + cont_no);
			    				no++;
				    		}
				    		rset.close();
				    		stmt.close();
				    			
			    			data = no+"";	
			    		
				    	}
				    	else if(cell.getColumnIndex() == 10) { //cont_name
				    		queryString = "SELECT COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_CD = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cp_abbr = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		value = "SEIPL-" + cp_abbr + "-MSPA1-REV0 N" + no + "-REV0";
				    		data = value;
				    		
				    	}
				    	else {

				    		if (cell.getColumnIndex() == 7) {	// cont_type
				    			cont_type = cell.getStringCellValue();
				    			cont_type = cont_type.substring(1, cont_type.length()-1);
				    		}
				    		
				    		if (cell.getColumnIndex() == 9) {	// cont_rev
				    			cont_rev = cell.getStringCellValue();
				    			cont_rev = cont_rev.substring(1, cont_rev.length()-1);
				    			
				    		}
				    		
				    		if (cell.getColumnIndex() ==11) {	// cont_ref_no
				    			cont_ref_no = cell.getStringCellValue();
				    			cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length()-1);				    			
				    		}


					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}

//					    System.out.println(index+"="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NAME FROM FMS_TRADER_CN_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CONT_REF_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, "M");
			    	stmt.setString(4, agmt_no);
			    	stmt.setString(5, "0");
			    	//stmt.setInt(6, no);
			    	stmt.setString(6, cont_type);
			    	stmt.setString(7, cont_rev);
			    	stmt.setString(8, cont_ref_no);
			    	
			    	rset = stmt.executeQuery();
			    				    	
				    if (row.getRowNum() != 0 && !rset.next() && agmt_no!=null && !cd.equals("0")) {
				    	//System.out.println(queryString1);
				    	
				    	logger.data(fname,("2"+", "+(cd+":"+abbr)+", "+" M "+", "+agmt_no+", "+" 0 "+", "+cont_type+", "+no+", "+cont_rev+","+cont_ref_no+","), conn,"");
				    	
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	agmt_no = "";
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,("2"+", "+(cd+":"+abbr)+", "+" M "+", "+agmt_no+", "+" 0 "+", "+cont_type+", "+no+", "+cont_rev+","+cont_ref_no+","), conn,"E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_TRADER_CN_MST>>", conn);
								
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
	public void FMS_TRADER_CONT_PLANT1() throws IOException, SQLException {

		function_nm="FMS_TRADER_CONT_PLANT1()";
		try {
			
			
			System.out.println("<<START>><<FMS_TRADER_CONT_PLANT1>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRADER_CONT_PLANT1>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE";
			
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY,ENT_DT FROM FMS_TRADER_AGMT_PLANT WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_TRADER_CONT_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE) VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,,", conn);
			
			while (rset.next()) {
				queryString2 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, NULL, NULL, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), CONTRACT_TYPE FROM FMS_TRADER_CN_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(3));
				stmt2.setString(4, rset.getString(4));
				stmt2.setString(5, rset.getString(5));
				rset2 = stmt2.executeQuery();
				
				while(rset2.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset2.getString(i+1) == null ? "" : rset2.getString(i+1);
						
						
						if(i == 6) {//PLANT_SEQ_NO
							data = rset.getString(6);
						}
						
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString5 = "SELECT COUNTERPARTY_CD FROM FMS_TRADER_CONT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND PLANT_SEQ_NO = ? AND CONTRACT_TYPE = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, rset2.getString(2));
				stmt5.setString(3, rset2.getString(3));
				stmt5.setString(4, rset2.getString(4));
				stmt5.setString(5, rset2.getString(5));
				stmt5.setString(6, rset.getString(6));
				stmt5.setString(7, rset2.getString(11));
			
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next() ) {
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
				}
				stmt5.close();
				rset5.close();
				}
	
			}
			rset2.close();
			stmt2.close();
			
			rset.close();
			stmt.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
		
			System.out.println("<<END>><<FMS_TRADER_CONT_PLANT1>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_CONT_PLANT1>>,,,,", conn);
			
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
		}
		
	}
	public void FMS_TRADER_CONT_BU1() throws IOException, SQLException {

		function_nm="FMS_TRADER_CONT_BU1()";
		try {
			
			
			System.out.println("<<START>><<FMS_TRADER_CONT_BU1>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_TRADER_CONT_BU1>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT,CONTRACT_TYPE";
			
			queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY,ENT_DT FROM FMS_TRADER_AGMT_BU WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_TRADER_CONT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT,CONTRACT_TYPE) VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,,", conn);
			
			while (rset.next()) {
				queryString2 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, NULL, ENT_BY, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), CONTRACT_TYPE FROM FMS_TRADER_CN_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, company_cd);
				stmt2.setString(2, rset.getString(2));
				stmt2.setString(3, rset.getString(3));
				stmt2.setString(4, rset.getString(4));
				stmt2.setString(5, rset.getString(5));
				rset2 = stmt2.executeQuery();
				
				while(rset2.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset2.getString(i+1) == null ? "" : rset2.getString(i+1);
						
						
						if(i == 6) {//PLANT_SEQ_NO
							data = rset.getString(6);
						}
						
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString5 = "SELECT COUNTERPARTY_CD FROM FMS_TRADER_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND PLANT_SEQ_NO = ? AND CONTRACT_TYPE = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, rset2.getString(2));
				stmt5.setString(3, rset2.getString(3));
				stmt5.setString(4, rset2.getString(4));
				stmt5.setString(5, rset2.getString(5));
				stmt5.setString(6, rset.getString(6));
				stmt5.setString(7, rset2.getString(10));
			
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next() ) {
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
				}
				stmt5.close();
				rset5.close();
				}
	
			}
			rset2.close();
			stmt2.close();
			
			rset.close();
			stmt.close();
			
			

			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_TRADER_CONT_BU1>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_CONT_BU1>>,", conn);
			
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
		}
		
	}
	

		public void FMS_TRADER_CARGO_MST() throws IOException, SQLException {

		function_nm="FMS_TRADER_CARGO_MST()";
		try {
			table_name = "FMS_TRADER_CARGO_MST";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_TRADER_CARGO_MST>>" , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			int no = 0;
				
			
			queryString1="INSERT INTO FMS_TRADER_CARGO_MST(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, CONTRACT_TYPE,"
					+ " CONT_NO, CONT_REV, CARGO_NO, CARGO_REF, CARGO_STATUS, CARGO_QTY, RATE, RATE_UNIT, START_DT, END_DT,"
					+ " ENT_DT, ENT_BY, MODIFY_DT, MODIFY_BY, TOLERANCE)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_CARGO_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_CARGO_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_TYPE,CONT_NO,CONT_REV,CARGO_NO,CARGO_REF_NO,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String agmt_no = "" , cargo_no = "",cont_type = "", cont_rev = "", cont_ref_no = "",agmt_type="",agmt_rev="",cargo_ref_no="";
					abbr = "";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					cd="";
					row = rowIterator.next();
				    cellIterator = row.cellIterator();
				    while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						data = null;
						
						if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 
				    		abbr = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		abbr = abbr.substring(1, abbr.length()-1);
				    		data = company_cd;
				    	}
			    	
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		
				    		cont_ref_no = cell.getStringCellValue();
			    			cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length()-1);	
				    						    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1,abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    		
				    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV "
				    				+ "FROM FMS_TRADER_CN_MST "
				    				+ "WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = ?";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cont_ref_no);
				    		stmt.setString(2, cd);
				    		stmt.setString(3, company_cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			agmt_type = rset.getString(1);
				    			agmt_no = rset.getString(2);
				    			agmt_rev = rset.getString(3);
				    			cont_type = rset.getString(4);
				    			no = rset.getInt(5);
				    			cont_rev = rset.getString(6);
				    		}				    		
				    		rset.close();
				    		stmt.close();
				    	}	
				    					 				    			    					    		
				    	else if(cell.getColumnIndex() == 2) {				    					    						    		
				    		data = agmt_type;					    	
				    	}					    		
				    	else if(cell.getColumnIndex() == 3) {				    					    		
				    		data = agmt_no;					    		
				    	}	
				    	else if(cell.getColumnIndex() == 4) {				    					    		
				    		data = agmt_rev;					    		
				    	}	
				    	else if(cell.getColumnIndex() == 5) {				    					    		
				    		data  = cont_type;					    	
				    	}	
				    	else if(cell.getColumnIndex() == 6) {				    						    					    						    		
			    			data = no+"";			    						    			
				    	}
				    	else if(cell.getColumnIndex() == 7) {				    					    		
				    		data  = cont_rev;					    		
				    	}
				    	else if(cell.getColumnIndex()==12)
				    	{
				    		data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
//					    	System.out.println(cargo_ref_no+":"+data+":");
				    	}
				    	else {				    		
				    		
				    		if (cell.getColumnIndex() == 8) {	// cargo_no
				    			cargo_no = cell.getStringCellValue();
				    			cargo_no = cargo_no.substring(1, cargo_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 9) {	// cargo_ref_no
				    			cargo_ref_no = cell.getStringCellValue();
				    			cargo_ref_no = cargo_ref_no.substring(1, cargo_ref_no.length()-1);
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
						//System.out.println(index+"="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CARGO_NO = ? AND CONT_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_type);
			    	stmt.setString(4, agmt_no);
			    	stmt.setString(5, agmt_rev);			    
			    	stmt.setString(6, cont_type);
			    	stmt.setString(7,cont_rev);
			    	stmt.setString(8,cargo_no);
			    	stmt.setInt(9, no);
			    	
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !agmt_no.equals("")) {
				    	
				    	logger.data(fname,(company_cd+", "+(cd+":"+abbr)+", "+agmt_type+", "+agmt_no+", "+agmt_rev+", "+cont_type+", "+no+", "+cont_rev+","+cargo_no+","+cargo_ref_no+","), conn,"");				    	
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    }else {
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+", "+(cd+":"+abbr)+", "+agmt_type+", "+agmt_no+", "+agmt_rev+", "+cont_type+", "+no+", "+cont_rev+","+cargo_no+","+cargo_ref_no+","), conn,"E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_TRADER_CARGO_MST>>", conn);
			
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
		
		public void FMS_TRADER_BILLING_DTL1() throws IOException, SQLException {

			function_nm="FMS_TRADER_BILLING_DTL1()";
			try {
				
				
				System.out.println("<<START>><<FMS_TRADER_BILLING_DTL1>>");
				
				logger.checkpoint(fname, "\n<<START>>,<<FMS_TRADER_BILLING_DTL1>>,", conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				data = "";
				logger_count = 0;   
				skipped_count = 0;   
				total_count = 0;   
				
//				String inv_type = "";
//				String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//				String[] tax_dtls = new String[5];
				
				columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,"
						+ "INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
						+ "EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,"
						+ "SPLIT_FLAG,PLANT_SEQ_NO,HOLIDAY_STATE";
				
				queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD,AGMT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,BILLING_FLAG,"
						+ "ENT_DT, ENT_BY, MODIFY_DT, MODIFY_BY"
						+ "	FROM FMS_TRADER_CN_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1,company_cd);
				rset = stmt.executeQuery();
				
				
				queryString1 = "INSERT INTO FMS_TRADER_BILLING_DTL (COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
						+ "BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,"
						+ "EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,"
						+ "BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,SPLIT_FLAG,PLANT_SEQ_NO,HOLIDAY_STATE) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?)";
//				stmt1 = conn.prepareStatement(queryString1);
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,", conn);
				
				while (rset.next()) {
					queryString2 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, NULL, NULL, NULL, BILLING_FREQ, BILLING_FLAG, DUE_DATE, SECOND_DUE_DT,"
							+ "	 INVOICE_CUR_CD, PAYMENT_CUR_CD, INT_CAL_RATE_CD, INT_CAL_SIGN, INT_CAL_PERCENTAGE, EXCHNG_RATE_CD, EXCHNG_RATE_CAL,"
							+ "	 EXCHNG_CRITERIA, EXCHG_RATE_NOTE, TAX_STRUCT_CD, TO_CHAR(ENT_DT, 'DD/MM/YYYY hh24:mi:ss'), ENT_BY, TO_CHAR(MODIFY_DT, 'DD/MM/YYYY hh24:mi:ss'), MODIFY_BY, DUE_DT_IN, EXCLUDE_SAT, NULL, EXCHG_VAL,"
							+ "	 EXCL_SAT_MAP, NULL, PLANT_SEQ_NO, HOLIDAY_STATE FROM FMS_TRADER_AGMT_BILLING_DTL"
							+ " WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, company_cd);
					stmt2.setString(2, rset.getString(2));
					stmt2.setString(3, rset.getString(3));
					stmt2.setString(4, rset.getString(4));
					stmt2.setString(5, rset.getString(5));
					rset2 = stmt2.executeQuery();
					
					while(rset2.next()) {
						stmt1 = conn.prepareStatement(queryString1);
						
						for(int i = 0;i < columns.split(",").length;i++) {
							data = "";
							data = rset2.getString(i+1) == null ? "" : rset2.getString(i+1);
							
							if(i == 4) {	//CONT_NO
								data = rset.getString(6);
							}
							else if(i == 5) {	//CONT_REV
								data = rset.getString(7);
							}
							else if(i == 6) {	//CONTRACT_TYPE
								data = rset.getString(8);
							}
//							System.out.println(i+"::>>>>"+data);
							stmt1.setString(i+1,data);
						}
						
					//for data already exists..
					queryString5 = "SELECT COUNTERPARTY_CD "
							+ "FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  "
							+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
					stmt5 = conn.prepareStatement(queryString5);
					stmt5.setString(1, company_cd);
					stmt5.setString(2, rset2.getString(2));
					stmt5.setString(3, rset2.getString(3));
					stmt5.setString(4, rset2.getString(4));
					stmt5.setString(5, rset.getString(6));
					stmt5.setString(6, rset.getString(7));
					stmt5.setString(7, rset.getString(8));
					stmt5.setString(8, rset2.getString(32));
				
					rset5 = stmt5.executeQuery();
//					("COMPANY_CD", "COUNTERPARTY_CD", "AGMT_NO", "AGMT_REV", "CONT_NO", "CONT_REV", "CONTRACT_TYPE", "PLANT_SEQ_NO")
					if (!rset5.next() ) {
						
//						logger.data(fname, ( company_cd + "," + rset2.getString(2) + "," + rset2.getString(3) + "," + rset2.getString(4) + "," + rset.getString(6) + "," + rset.getString(7)+ "," + rset.getString(8) + " , " +rset2.getString(32)+","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
//						logger.data(fname, ( company_cd + "," + rset2.getString(2) + "," + rset2.getString(3) + "," + rset2.getString(4) + "," + rset.getString(6) + "," + rset.getString(7)+ "," + rset.getString(8) + " , " +rset2.getString(32)+","), conn, "E");
						
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
				
			
				System.out.println("<<END>><<FMS_TRADER_BILLING_DTL1>>");
				System.out.println();
			
				logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
				logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
				logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
				
				logger.checkpoint(fname, "<<END>>,<<FMS_TRADER_BILLING_DTL1>>,", conn);
				
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
			}
			
		}
		
		
		public void FMS_PSEUDO_CARGO_DTL() throws IOException, SQLException {

			function_nm="FMS_PSEUDO_CARGO_DTL()";
			try {
				table_name = "FMS_PSEUDO_CARGO_DTL";
				System.out.println("<<START>><<"+table_name+">>");
				logger.checkpoint(fname,"\n<<START>>,<<FMS_PSEUDO_CARGO_DTL>>,,,,," , conn);
				
				logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
				
				data = "";
				logger_count = 0;
				skipped_count = 0;   
				total_count = 0;
				

				queryString1= "INSERT INTO FMS_PSEUDO_CARGO_DTL(COMPANY_CD,CONTRACT_TYPE,CARGO_NO,SEQ_NO,QTY,ENT_DT,ENT_BY) VALUES(?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
				
				stmt1 = conn.prepareStatement(queryString1);
				
				file1 = new File(migration_setup_dir + "EXPORT/FMS_PSEUDO_CARGO_DTL_"+start_end_dt+".xlsx");
				if(file1.exists()) {

					
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_PSEUDO_CARGO_DTL_"+start_end_dt+".xlsx"));

					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					
					// Below block of code is for unique SEIPL data
					rowIterator = sheet.iterator();
					if (rowIterator.hasNext()) {	// For skipping the first row
						rowIterator.next();
					}
					
					logger.checkpoint(fname, "COMPANY_CD,CONTRACT_TYPE,CARGO_NO,SEQ_NO,QTY,ENT_DT,ENT_BY,TIMESTAMP", conn);
					
					while (rowIterator.hasNext()) {
						total_count++;
						String cargo_no = "", cont_type = "" , seq_no="";
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

					    		if (cell.getColumnIndex() == 1) {	// cont_type
					    			cont_type = cell.getStringCellValue();
					    			cont_type = cont_type.substring(1, cont_type.length()-1);
					    		}
					    		
					    		if (cell.getColumnIndex() == 2) {	// cargo_no
					    			cargo_no = cell.getStringCellValue();
					    			cargo_no = cargo_no.substring(1, cargo_no.length()-1);
					    			
					    		}
					    		if (cell.getColumnIndex() == 3) {	// seq_no
					    			seq_no = cell.getStringCellValue();
					    			seq_no = seq_no.substring(1, seq_no.length()-1);
					    			
					    		}
					    		

						    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(data != null) {
						    		data = data.substring(1, data.length()-1);
						    	}
//						    System.out.println(index+"="+data);
					    	stmt1.setString(index++, data);
					    }
					     
				    	queryString = "SELECT CARGO_NO FROM FMS_PSEUDO_CARGO_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE = ? AND CARGO_NO = ? AND SEQ_NO = ?";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, company_cd);
				    	stmt.setString(2, cont_type);
				    	stmt.setString(3, cargo_no);
				    	stmt.setString(4, seq_no);
				    	
				    	rset = stmt.executeQuery();
				    				    	
					    if (row.getRowNum() != 0 && !rset.next()) {
					    	//System.out.println(queryString1);
					    	
					    	logger.data(fname,("2"+", "+cont_type+", "+cargo_no+", "+seq_no+", "), conn,"");
					    	
					    	stmt1.executeUpdate();
					    	stmt1.close();
					    	
					    	logger_count++;
					    }else {
					    	stmt1.close();
					    	skipped_count++;
					    	logger.data(fname,("2"+", "+cont_type+", "+cargo_no+", "+seq_no+", "), conn,"E");
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
				logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

				logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
							
				logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
				
				logger.checkpoint1(fname1,logger_count+",", conn);
							
				logger.checkpoint(fname, "<<END>><<FMS_PSEUDO_CARGO_DTL>>", conn);
									
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
		
	public void FMS_CONT_PRICE_DTL() throws IOException, SQLException {

		function_nm="FMS_CONT_PRICE_DTL(P)()";
		try {
			table_name = "FMS_CONT_PRICE_DTL(P)";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_CONT_PRICE_DTL(P)>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;

			queryString1="INSERT INTO FMS_CONT_PRICE_DTL(COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO, FROM_DT, TO_DT, PRICE_TYPE,"
					+ " CURVE_NM, SLOPE, CONST, QUANTITY_UNIT, RATE, RATE_UNIT, REMARKS, ENT_BY, ENT_DT, MODIFY_BY, MODIFY_DT, PRICE_RANGE,"
					+ " PRICE_START_DT, PRICE_END_DT, PHYS_CURVE_NM, CURVE_LOGIC, FORMULA)"
					+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL(P)_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL(P)_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String seq_no = "",map_id="",cont_type="",cont_no="",agmt_no="",cargo_no="",cont_ref_no="",dom_buy_flag="";
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
				    		cont_ref_no=  (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (cont_ref_no!=null) {
								cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
								dom_buy_flag=cont_ref_no.split("-")[3];
					    		abbr = cont_ref_no.split("-")[0];
							}
				    		else
				    		{
				    			dom_buy_flag=null;
				    			abbr=null;
				    		}
				    		data = company_cd;
				    		
				    	}
				    	else if (cell.getColumnIndex() == 1 && dom_buy_flag!=null ) {	// Counterparty_Cd
				    		
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			map_id = rset.getString(1)+"-"+cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1);
				    			cd=rset.getString(1);
				    		}	
				    		else {
				    			cd=null;
				    		}
				    		rset.close();
				    		stmt.close();
				    	
				    		data=cell.getStringCellValue();
				    		data = data.substring(1, data.length()-1);
				    		
				    		if(dom_buy_flag.equals("N") && !dom_buy_flag.equals("")) {
				    		queryString="SELECT CONT_NO,AGMT_NO,CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = ? ";
				    		stmt=conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
							stmt.setString(2, cd);
				    		stmt.setString(3, company_cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cont_no=rset.getString(1);
				    			agmt_no=rset.getString(2);
				    			cargo_no=rset.getString(3);
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		data=cd+"-"+agmt_no+"-"+cont_no+"-"+cargo_no;
				    		map_id=data;
				    		}
				    		
				    		else if((dom_buy_flag.equals("D") || dom_buy_flag.equals("T")) && !dom_buy_flag.equals("")){
					    		queryString="SELECT CONT_NO,AGMT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ? ";
					    		stmt=conn.prepareStatement(queryString);
					    		stmt.setString(1, cont_ref_no);
					    		stmt.setString(2, company_cd);
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cont_no=rset.getString(1);
					    			agmt_no=rset.getString(2);
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data=cd+"-"+agmt_no+"-"+cont_no;
					    		map_id=data;
				    		}
				    		else if(dom_buy_flag.equals("I") && !dom_buy_flag.equals("")){
					    		queryString="SELECT CONT_NO,AGMT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ?  AND COMPANY_CD = ?";
					    		stmt=conn.prepareStatement(queryString);
					    		stmt.setString(1, cont_ref_no);
					    		stmt.setString(2, company_cd);
//					    		stmt.setString(2, cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cont_no=rset.getString(1);
					    			agmt_no=rset.getString(2);
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data=cd+"-"+agmt_no+"-"+cont_no;
					    		map_id=data;
				    		}
				    		
				    		}
						
				    	else if (cell.getColumnIndex() == 23) {	// MULTI_LEG
				    		data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    		if (data.contains("MULTI_LEG@")) {
					    			if (data.split("@")[1].contains("-") && data.split("@")[2].contains("-")) {
					    				data = data.split("@")[0] + "@Forward@" + data.split("@")[1].substring(1) + "@" + data.split("@")[2].substring(1);
					    			}
					    			else if (!data.split("@")[1].contains("-") && !data.split("@")[2].contains("-")) {
					    				data = data.split("@")[0] + "@Settled@" + data.split("@")[1] + "@" + data.split("@")[2];
					    			}
					    		}
					    	}
				    	}
				    	else {
				    		if (cell.getColumnIndex() == 3) {	// seq_no
				    			seq_no = cell.getStringCellValue();
				    			seq_no = seq_no.substring(1, seq_no.length()-1);
				    		}				    	
				    		if (cell.getColumnIndex() == 2) {	// cont_type
				    			cont_type = cell.getStringCellValue();
				    			cont_type = cont_type.substring(1, cont_type.length()-1);
				    		}				    		
				    		

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT SEQ_NO FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = ? AND MAPPING_ID = ? AND SEQ_NO = ? AND CONTRACT_TYPE = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);   	
			    	stmt.setString(2, map_id);
			    	stmt.setString(3, seq_no);
			    	stmt.setString(4, cont_type);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !agmt_no.equals("") && cd!=null) {				
				    	//System.out.println(queryString1);
				    	logger.data(fname,(company_cd+","+map_id+" ,"+cont_type+" ,"+seq_no+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;				    	
				    }else {
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+map_id+", "+cont_type+" ,"+seq_no+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_CONT_PRICE_DTL(P)>>", conn);
			
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

	public void FMS_CONT_PRICE_MIN_DTL() throws IOException, SQLException {

		function_nm="FMS_CONT_PRICE_MIN_DTL(P)()";
		try {
			table_name = "FMS_CONT_PRICE_MIN_DTL(P)";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_CONT_PRICE_MIN_DTL(P)>>,,,," , conn);
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			rset.close();
			stmt.close();
			
			queryString1="INSERT INTO FMS_CONT_PRICE_MIN_DTL(COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,CURVE_LOGIC,CURVE_NM,SLOPE,"
					+ "CONST,QUANTITY_UNIT,RATE,RATE_UNIT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT)"
					+ " VALUES(?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, ?, "
					+ "?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ " ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss')"
					+ ")";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL(P)_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL(P)_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,CURVE_NM,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String seq_no = "", curve_nm = "",map_id = "",cont_type = "";
					String cont_no="",agmt_no="",cargo_no="",cont_ref_no="",dom_buy_flag="";
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
							cont_ref_no=  (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref_no.equals("NULL")) {
								cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
								dom_buy_flag=cont_ref_no.split("-")[3];
					    		abbr = cont_ref_no.split("-")[0];
							}else
				    		{
				    			dom_buy_flag=null;
				    			abbr=null;
				    		}
							
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1 && dom_buy_flag!=null ) {	// Counterparty_Cd
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			map_id = rset.getString(1)+"-"+cell.getStringCellValue().substring(1, cell.getStringCellValue().length()-1);
				    			cd=rset.getString(1);
				    		}
				    		else 
				    		{
				    			cd=null;
				    		}
				    		rset.close();
				    		stmt.close();
				    	
				    		data=cell.getStringCellValue();
				    		data = data.substring(1, data.length()-1);
				    		
				    		if(dom_buy_flag.equals("N") && !dom_buy_flag.equals("")) {
				    		queryString="SELECT CONT_NO,AGMT_NO,CARGO_NO "
				    				+ "FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = ? ";
				    		stmt=conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
							stmt.setString(2, cd);
							stmt.setString(3, company_cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cont_no=rset.getString(1);
				    			agmt_no=rset.getString(2);
				    			cargo_no=rset.getString(3);
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		data=cd+"-"+agmt_no+"-"+cont_no+"-"+cargo_no;
				    		map_id=data;
				    		}
				    		
				    		else if((dom_buy_flag.equals("D") || dom_buy_flag.equals("T")) && !dom_buy_flag.equals("")){
					    		queryString="SELECT CONT_NO,AGMT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ?  AND COMPANY_CD = ?";
					    		stmt=conn.prepareStatement(queryString);
					    		stmt.setString(1, cont_ref_no);
					    		stmt.setString(2, company_cd);
//					    		stmt.setString(2, cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cont_no=rset.getString(1);
					    			agmt_no=rset.getString(2);
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data=cd+"-"+agmt_no+"-"+cont_no;
					    		map_id=data;
				    		}
				    		else if(dom_buy_flag.equals("I") && !dom_buy_flag.equals("")){
					    		queryString="SELECT CONT_NO,AGMT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ?   AND COMPANY_CD = ? ";
					    		stmt=conn.prepareStatement(queryString);
					    		stmt.setString(1, cont_ref_no);
					    		stmt.setString(2, company_cd);
//					    		stmt.setString(2, cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cont_no=rset.getString(1);
					    			agmt_no=rset.getString(2);
					    		}
					    		rset.close();
					    		stmt.close();
					    		
					    		data=cd+"-"+agmt_no+"-"+cont_no;
					    		map_id=data;
				    		}
				    		
				    	}
				    	else if (cell.getColumnIndex() == 13) {	// MULTI_LEG
				    		data = cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
//				    		if (data.contains("M@")) {
//				    			if (data.split("@")[1].contains("-") && data.split("@")[2].contains("-")) {
//				    				data = data.split("@")[0] + "@Forward@" + data.split("@")[1] + "@" + data.split("@")[2];
//				    			}
//				    			else if (!data.split("@")[1].contains("-") && !data.split("@")[2].contains("-")) {
//				    				data = data.split("@")[0] + "@Settled@" + data.split("@")[1].substring(1) + "@" + data.split("@")[2].substring(1);
//				    			}
//				    		}
				    		if (data.contains("M@")) {
				    			if (data.split("@")[1].contains("-") && data.split("@")[2].contains("-")) {
				    				data = data.split("@")[0] + "@Settled@" + data.split("@")[1].substring(1) + "@" + data.split("@")[2].substring(1);
				    			}
				    			else if (!data.split("@")[1].contains("-") && !data.split("@")[2].contains("-")) {
				    				data = data.split("@")[0] + "@Forward@" + data.split("@")[1] + "@" + data.split("@")[2];
				    			}
				    		}
				    	}
				    	else {
				    		
				    		if (cell.getColumnIndex() == 2) {	// cont_type
				    			cont_type = cell.getStringCellValue();
				    			cont_type = cont_type.substring(1, cont_type.length()-1);
				    		}
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
				     
			    	queryString = "SELECT SEQ_NO FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = ? AND MAPPING_ID = ? "
			    			+ "AND CONTRACT_TYPE = ? AND SEQ_NO = ? AND CURVE_NM = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, map_id);
			    	stmt.setString(3, cont_type);
			    	stmt.setString(4, seq_no);
			    	stmt.setString(5, curve_nm);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !agmt_no.equals("") && cd!=null ) {
				    	//System.out.println(queryString1);		    	
				    	logger.data(fname,(company_cd+","+map_id+", "+cont_type+", "+seq_no+", "+curve_nm+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    } else {
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+" ,"+map_id+", "+cont_type+" ,"+seq_no+", "+curve_nm+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_CONT_PRICE_MIN_DTL(P)>>", conn);
			
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

	public void FMS_CARGO_SVC_CONT_MST() throws IOException, SQLException {

		function_nm="FMS_CARGO_SVC_CONT_MST()";
		try {
			table_name = "FMS_CARGO_SVC_CONT_MST";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_CARGO_SVC_CONT_MST>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
		
			// Getting the start of cont_no
			int no = 1000;
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			rset.close();
			stmt.close();
			queryString1 = "INSERT INTO FMS_CARGO_SVC_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,CONT_NO,CONTRACT_TYPE,CONT_REF_NO,CONT_NAME,"
					+ "CONT_STATUS,START_DT,END_DT,PROV_SVC_RATE,PROV_SVC_RATE_UNIT1,PROV_SVC_RATE_UNIT2,FINAL_SVC_RATE,FINAL_SVC_RATE_UNIT1,"
					+ "FINAL_SVC_RATE_UNIT2,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,SIGNING_DT,SIGNING_TIME) VALUES(?, ?, ?, ?, ?, ?, ?, ?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ " ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,CD,ENTITY_TYPE,CONT_NO,CONTRACT_TYPE,TIMESTAMP", conn);
				
				int seq_no = 0,seq_no1 = 0,seq_no2 = 0;
				while (rowIterator.hasNext()) {
					total_count++;
					seq_no++;
					seq_no1++;
					seq_no2++;
					String entity_type = "", cont_type = "", cp_abbr = "",prov_svc_rate = "";
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
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		
				    		
				    		queryString = "SELECT A.COUNTERPARTY_CD FROM "
				    				+ "FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD "
				    				+ "AND B.COMPANY_CD = '2' AND B.ENTITY = ? AND A.COUNTERPARTY_ABBR = ? ";
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
				    		
				    		
				    		
				    		
//				    		System.out.println(":"+abbr+":"+cd);
				    	}
				    	
				    	else if (cell.getColumnIndex() == 2) {	// Entity_Type
			    			entity_type = cell.getStringCellValue();
			    			entity_type = entity_type.substring(1, entity_type.length()-1);
			    			data  = entity_type;
			    		}
				    	
				    	else if(cell.getColumnIndex() == 3) {   //Cont_no				    		
				    		queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_CARGO_SVC_CONT_MST WHERE ENTITY_TYPE = ?  AND COMPANY_CD = '2'";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, entity_type);
				    		rset = stmt.executeQuery();
				    		if (rset.next() && rset.getInt(1) > 0) {
				    			no = rset.getInt(1);
				    			//System.out.println(no);	
				    		}else{
				    			// Getting the start of cont_no
				    			no = 1000;
				    			queryString2 = "SELECT TO_CHAR(SYSDATE, 'YYYY') FROM DUAL";
				    			stmt2 = conn.prepareStatement(queryString2);
				    			rset2 = stmt2.executeQuery();
				    		
				    			if(rset2.next()){
				    				if("S".equals(entity_type)) {			    				
				    					no = no * Integer.parseInt("3" + rset2.getString(1).substring(2));	
				    				}
				    				if("V".equals(entity_type)) {			    				
				    					no = no * Integer.parseInt("2" + rset2.getString(1).substring(2));
				    				}
				    				if("H".equals(entity_type)) {			    				
				    					no = no * Integer.parseInt("1" + rset2.getString(1).substring(2));		
					    			}
				    			}				    		
				    			no++;
				    			rset2.close();
				    			stmt2.close();					    			
				    		}				    						    		
				    		rset.close();
				    		stmt.close();
				    			
			    			data = no+"";
				    	}

				    	else if(cell.getColumnIndex() == 5) { //cont_ref_no
				    			if("S".equals(entity_type)) {				    				
					    			value = "CONT-SURV-"+seq_no;
					    		}				    			
				    			if("V".equals(entity_type)) {
				    				
					    			value = "CONT-VA-"+seq_no1;
					    		}
				    			if("H".equals(entity_type)) {				    				
					    			value = "CONT-CHA-"+seq_no2;
					    		}
				    			
				    			data  =  value;	
				    	}
				    	else if(cell.getColumnIndex() == 6) { //cont_name
				    		queryString = "SELECT COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_CD = ?";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cp_abbr = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		value1 = cp_abbr + "-SEIPL-" + cont_type + no;
				    		data = value1;				    	
				    		
				    	}
				    	
						
				    	else {
				    	
				    		
				    		if (cell.getColumnIndex() == 4) {	// Cont_Type
				    			cont_type = cell.getStringCellValue();
				    			cont_type = cont_type.substring(1, cont_type.length()-1);
				    		}
				    		
				    		if (cell.getColumnIndex() == 10) {	// //prov_svc_rate
				    			prov_svc_rate = cell.getStringCellValue();
				    			prov_svc_rate = prov_svc_rate.substring(1, prov_svc_rate.length()-1);
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	//System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_CARGO_SVC_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND ENTITY_TYPE = ? AND PROV_SVC_RATE = ? AND CONTRACT_TYPE = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, entity_type);
			    	stmt.setString(4, prov_svc_rate);
			    	stmt.setString(5, cont_type);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && cd != null ) {
				    	
				    	logger.data(fname,(company_cd+","+(cd+":"+abbr)+", "+entity_type+", "+no+", "+cont_type+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    } else {
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+(cd+":"+abbr)+", "+entity_type+", "+no+", "+cont_type+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_CARGO_SVC_MST>>", conn);
			
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

	public void FMS_CARGO_SVC_CONT_SVC_BU() throws IOException, SQLException {

		function_nm="FMS_CARGO_SVC_CONT_SVC_BU()";
		try {
			table_name = "FMS_CARGO_SVC_CONT_SVC_BU";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_CARGO_SVC_CONT_SVC_BU>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			int no = 1000;
	
			queryString1 = "INSERT INTO FMS_CARGO_SVC_CONT_SVC_BU(COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,CONT_NO,CONTRACT_TYPE,PLANT_SEQ_NO,"
					+ "ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_SVC_BU_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_SVC_BU_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,CD,CONT_NAME,CONTRACT_TYPE,PLANT_SEQ_NO,TIMESTAMP", conn); 
				
				while (rowIterator.hasNext()) {
					total_count++;
					String entity_type = "", cont_type = "",plant_seq_no = "";
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
				    		data = company_cd;
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
//				    		System.out.println(":"+abbr+":"+cd);
				    	}
				    	else if (cell.getColumnIndex() == 2) {	// Entity_Type
			    			entity_type = cell.getStringCellValue();
			    			entity_type = entity_type.substring(1, entity_type.length()-1);
			    			data  = entity_type;
			    		}
				    	else if(cell.getColumnIndex() == 3) {   //Cont_no				    		
				    		queryString = "SELECT CONT_NO,CONTRACT_TYPE FROM FMS_CARGO_SVC_CONT_MST WHERE COUNTERPARTY_CD = ?  AND COMPANY_CD = '2'";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next() && rset.getInt(1) > 0) {
				    			no = rset.getInt(1);	
				    			cont_type = rset.getString(2);
				    		}				    						    		
				    		rset.close();
				    		stmt.close();
				    			
			    			data = no+"";
				    	}else if(cell.getColumnIndex() == 4) {
				    		data = cont_type;
				    	}
				    	
				    	else {				    		
				    		if (cell.getColumnIndex() == 5) {	// plant_seq_no
				    			plant_seq_no= cell.getStringCellValue();
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
				     
			    	queryString = "SELECT CONT_NO FROM FMS_CARGO_SVC_CONT_SVC_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? AND CONT_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);		    	
			    	stmt.setString(3, cont_type);
			    	stmt.setString(4, plant_seq_no);
			    	stmt.setInt(5, no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_type.equals("")) {
				    	//System.out.println(queryString1);
				    	logger.data(fname,(company_cd+","+(cd+":"+abbr)+", "+no+", "+cont_type+", "+plant_seq_no+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;    	
				    } else {
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+(cd+":"+abbr)+", "+no+", "+cont_type+", "+plant_seq_no+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_CARGO_SVC_CONT_SVC_BU>>", conn);
			
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

	public void FMS_CARGO_SVC_CONT_BU() throws IOException, SQLException {

		function_nm="FMS_CARGO_SVC_CONT_BU()";
		try {
			table_name = "FMS_CARGO_SVC_CONT_BU";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_CARGO_SVC_CONT_BU>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			int no = 1000;
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();

			rset.close();
			stmt.close();
			
			queryString1="INSERT INTO FMS_CARGO_SVC_CONT_BU(COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,CONT_NO,CONTRACT_TYPE,PLANT_SEQ_NO,"
					+ "ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_SVC_BU_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CARGO_SVC_CONT_SVC_BU_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,CD,CONT_NO,CONTRACT_TYPE,PLANT_SEQ_NO,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String entity_type = "", cont_type = "",plant_seq_no = "";
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
				    		data = company_cd;
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
				    	else if (cell.getColumnIndex() == 2) {	// Entity_Type
			    			entity_type = cell.getStringCellValue();
			    			entity_type = entity_type.substring(1, entity_type.length()-1);
			    			data  = entity_type;
			    		}
				    	else if(cell.getColumnIndex() == 3) {   //Cont_no				    		
				    		queryString = "SELECT CONT_NO,CONTRACT_TYPE FROM FMS_CARGO_SVC_CONT_MST WHERE COUNTERPARTY_CD = ?  AND COMPANY_CD=?";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cd);
				    		stmt.setString(2, company_cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next() && rset.getInt(1) > 0) {
				    			no = rset.getInt(1);	
				    			cont_type = rset.getString(2);
				    		}				    						    		
				    		rset.close();
				    		stmt.close();
				    			
			    			data = no+"";
				    	}else if(cell.getColumnIndex() == 4) {
				    		data = cont_type;
				    	}
			    	
				    	else {
				    		if (cell.getColumnIndex() == 5) {	// plant_seq_no
				    			plant_seq_no= cell.getStringCellValue();
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
				     
			    	queryString = "SELECT CONT_NO FROM FMS_CARGO_SVC_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? AND CONT_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);			    
			    	stmt.setString(3, cont_type);
			    	stmt.setString(4, plant_seq_no);
			    	stmt.setInt(5, no);

			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_type.equals("") ) {
				    	//System.out.println(queryString1);
				    	
				    	logger.data(fname,(company_cd+","+cd+" ,"+no+" ,"+cont_type+", "+plant_seq_no+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    }else {
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+cd+", "+no+" ,"+cont_type+" ,"+plant_seq_no+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);

			logger.checkpoint(fname, "<<END>><<FMS_CARGO_SVC_CONT_BU>>", conn);
			
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

	public void FMS_BUY_CARGO_NOM() throws IOException, SQLException {

		function_nm="FMS_BUY_CARGO_NOM()";
		try {
			table_name = "FMS_BUY_CARGO_NOM";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BUY_CARGO_NOM>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";	
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			int no = 0;		
			

			queryString1="INSERT INTO FMS_BUY_CARGO_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,"
					+ "CONT_REV,CARGO_NO,NOM_REV,SHIP_CD,EXP_DELV_QTY,EXP_FROM_DT,EXP_TO_DT,COUNTRY_ORIGIN,LOAD_PORT,REMARK,SPLIT_BOL,"
					+ "NUM_BL,NUM_BOE,LIQUEFAC_PLANT,LIQUEFAC_COUNTRY,LIQUEFAC_PROMOTOR,LIQUEFAC_REMARK,ENT_BY,ENT_DT,FLAG,"
					+ "LINKED_SURVEYOR_CONT,LINKED_CHAGENT_CONT,LINKED_VAGENT_CONT) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,CARGO_REF_NO,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String agmt_no = "", cargo_no = "", ship_cd = "",agmt_type = "",agmt_rev ="", cont_type = "",cont_rev = "",nom_rev="",cargo_ref_no="",exp_from_dt="",exp_to_dt="";
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
				    		
				    		data = company_cd;
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		
				    		cargo_ref_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		cargo_ref_no = cargo_ref_no.substring(1, cargo_ref_no.length()-1);
				    		
				    		data = cd;
				    		
				    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO "
				    				+ "FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ?";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cargo_ref_no);
				    		stmt.setString(2, cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			agmt_type = rset.getString(1);
				    			agmt_no = rset.getString(2);
				    			agmt_rev = rset.getString(3);
				    			cont_type = rset.getString(4);
				    			no = rset.getInt(5);
				    			cont_rev = rset.getString(6);
				    			cargo_no = rset.getString(7);
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
				    	else if (cell.getColumnIndex() == 2) {					    		
				    		data = agmt_type;	
				    	}
				    	else if (cell.getColumnIndex() == 3) {					    		
				    		data = agmt_no;	
				    	}
				    	else if (cell.getColumnIndex() == 4) {					    		
				    		data = agmt_rev;	
				    	}
				    	else if (cell.getColumnIndex() == 5) {					    		
				    		data = cont_type;	
				    	}
				    	else if (cell.getColumnIndex() == 6) {					    		
			    			data = no+"";	
				    	}
				    	else if (cell.getColumnIndex() == 7) {					    		
				    		data = cont_rev;	
				    	}
				    	else if (cell.getColumnIndex() == 8) {	
				    		data = cargo_no;
				    		
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
					    		ship_cd = rset.getString(1);
				    		}
				    		else  {
				    			ship_cd = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		data  = ship_cd;
				    	}
				    	
				    	else if(cell.getColumnIndex() == 12)//start date
				    	{
				    		
				    		exp_from_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		
					    	if(exp_from_dt != null) {
					    		exp_from_dt = exp_from_dt.substring(1, exp_from_dt.length()-1);
					    	}
				    	
					    	data=exp_from_dt;
//					    	if((no+"").equals("725129"))
//					    	{
//					    		System.out.println("->start:"+exp_from_dt);
//					    	}
					    	
				    	}
				    	else if(cell.getColumnIndex() == 13)//end date
				    	{
				    		exp_to_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		
					    	if(exp_from_dt != null) {
					    		exp_to_dt = exp_to_dt.substring(1, exp_to_dt.length()-1);
					    	}
				    	
					    	data=exp_to_dt;

				    	}
						else if (cell.getColumnIndex() == 14 || cell.getColumnIndex() == 21) {	
							
				    		data = ((cell.getStringCellValue().contains("'null'") || cell.getStringCellValue().equals("' '") || cell.getStringCellValue().equals("''"))  ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
					    	
					    	//condition: for merging country name 
					    	if(data!=null)
					    	{
					    		if(data.contains("AUSTRALIA"))
						    	{
						    		data="AUSTRALIA";
						    	}
					    	}
					    	
//					    	else if()
					
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
							
						
				    	else if (cell.getColumnIndex() == 27 || cell.getColumnIndex() == 28 || cell.getColumnIndex() == 29) {	// CHA, VA, SURV
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT A.COMPANY_CD, A.ENTITY_TYPE, A.COUNTERPARTY_CD, A.CONTRACT_TYPE, A.CONT_NO "
				    				+ "FROM FMS_CARGO_SVC_CONT_MST A, FMS_COUNTERPARTY_MST B "
				    				+ "WHERE A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.COUNTERPARTY_ABBR = ? AND A.COMPANY_CD='2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, data);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
								data = rset.getString(3)+"-"+rset.getString(4)+'-'+rset.getString(5);
				    		}
				    		else {
				    			data = null;
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
				    	else {				    		
				    		if (cell.getColumnIndex() == 9) {	// Nom_rev
				    			nom_rev= cell.getStringCellValue();
				    			nom_rev = nom_rev.substring(1, nom_rev.length()-1);
				    		}
				    	
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
					    	
				    	}

				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_NOM WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CARGO_NO = ? AND NOM_REV = ? AND CONT_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_type);
			    	stmt.setString(4, agmt_no);
			    	stmt.setString(5, agmt_rev);
			    	stmt.setString(6, cont_type);
			    	stmt.setString(7, cont_rev);
			    	stmt.setString(8, cargo_no);
			    	stmt.setString(9, nom_rev);		
			    	stmt.setInt(10, no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !agmt_type.equals("") && !cd.equals("0")) {
				    	//System.out.println(queryString1);
//				    	 "COMPANY_CD,CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,CARGO_REF_NO,TIMESTAMP"/
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+", "+agmt_type+", "+agmt_no+" ,"+agmt_rev+" ,"+cont_type+", "+no+" ,"+cont_rev+" ,"+cargo_no+","+nom_rev+","+cargo_ref_no+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    } else {	
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+" ,"+agmt_type+" ,"+agmt_no+", "+agmt_rev+", "+cont_type+" ,"+no+" ,"+cont_rev+" ,"+cargo_no+","+nom_rev+","+cargo_ref_no+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_BUY_CARGO_NOM>>", conn);
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

	public void FMS_BUY_CARGO_NOM_BL() throws IOException, SQLException {

		function_nm="FMS_BUY_CARGO_NOM_BL()";
		try {
			table_name = "FMS_BUY_CARGO_NOM_BL";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BUY_CARGO_NOM_BL>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;

			// Getting the start of cont_no

			
			queryString1 = "INSERT INTO FMS_BUY_CARGO_NOM_BL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,"
					+ "CONT_REV,CARGO_NO,NOM_REV,BL_NO,BL_QTY,BL_QTY_UNIT,BL_PRICE,BL_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,BL_QTY_MT,BL_QTY_SCM) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_BL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_BL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BL_NO,CARGO_REF_NO,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String agmt_no = "", cont_type = "", cargo_no = "", bl_no = "", bl_qty = "",agmt_type = "",agmt_rev = "",nom_rev = "",cont_rev = "",cargo_ref_no="";
					abbr = "";
					cd = "0";
					int no=0;
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
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		cargo_ref_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		cargo_ref_no = cargo_ref_no.substring(1, cargo_ref_no.length()-1);	
				    		
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
				    		
				    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO "
				    				+ "FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cargo_ref_no);
				    		stmt.setString(2, cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {				    			
				    			agmt_type = rset.getString(1);
				    			agmt_no = rset.getString(2);
				    			agmt_rev = rset.getString(3);
				    			cont_type = rset.getString(4);
				    			no = rset.getInt(5);
				    			cont_rev =  rset.getString(6);
				    			cargo_no =  rset.getString(7);
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    	}else if (cell.getColumnIndex() == 2) {				    		
				    		data = agmt_type;
				    	}
				    	else if (cell.getColumnIndex() == 3) {				    		
				    		data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 4) {				    		
				    		data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 5) {				    		
				    		data = cont_type;
				    	}
				    	else if (cell.getColumnIndex() == 6) {					    			
			    			data = no+"";	
				    	}
				    	else if (cell.getColumnIndex() == 7) {					    			
			    			data = cont_rev;	
				    	}
				    	else if (cell.getColumnIndex() == 8) {	// Cargo_no				    					    		
				    		data = cargo_no;
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
				    		
				    		if (cell.getColumnIndex() == 9) {	// nom_rev
				    			nom_rev = cell.getStringCellValue();
				    			nom_rev = nom_rev.substring(1, nom_rev.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 10) {	// Bl_No
				    			bl_no = cell.getStringCellValue();
				    			bl_no = bl_no.substring(1, bl_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 11) {	// Bl_Qty
				    			bl_qty = cell.getStringCellValue();
				    			bl_qty = bl_qty.substring(1, bl_qty.length()-1);
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_NOM_BL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CARGO_NO = ? AND NOM_REV = ? AND BL_NO = ? AND CONT_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_type);
			    	stmt.setString(4, agmt_no);
			    	stmt.setString(5, agmt_rev);
			    	stmt.setString(6, cont_type);
			    	stmt.setString(7, cont_rev);
			    	stmt.setString(8, cargo_no);
			    	stmt.setString(9, nom_rev);
			    	stmt.setString(10, bl_no);			    	
			    	stmt.setInt(11, no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !agmt_type.equals("")) {
				    	//System.out.println(queryString1);
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+" ,"+agmt_type+", "+agmt_no+" ,"+agmt_rev+", "+cont_type+", "+no+", "+cont_rev+", "+cargo_no+", "+nom_rev+", "+bl_no+","+cargo_ref_no+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    } else {
				    	agmt_no = "";
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+" ,"+agmt_type+" ,"+agmt_no+", "+agmt_rev+" ,"+cont_type+", "+no+", "+cont_rev+" ,"+cargo_no+" ,"+nom_rev+" ,"+bl_no+","+cargo_ref_no+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
						
			logger.checkpoint1(fname1,logger_count+",", conn);
			
			logger.checkpoint(fname, "<<END>><<FMS_BUY_CARGO_NOM_BL>>", conn);
			
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

	public void FMS_BUY_CARGO_NOM_BOE() throws IOException, SQLException {

		function_nm="FMS_BUY_CARGO_NOM_BOE()";
		try {
			table_name = "FMS_BUY_CARGO_NOM_BOE";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BUY_CARGO_NOM_BOE>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;	
			int no=0;
		
			queryString1 = "INSERT INTO FMS_BUY_CARGO_NOM_BOE(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,"
					+ "CONT_REV,CARGO_NO,NOM_REV,BOE_NO,BU_SEQ,PLANT_SEQ,BOE_QTY,BOE_QTY_UNIT,BOE_PRICE,BOE_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,"
					+ "CUSTOM_DUTY,LOAD_PORT,LINKED_BL,BOE_QTY_MT,BOE_QTY_SCM) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_BOE_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_NOM_BOE_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BOE_NO,CARGO_REF,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String agmt_type = "", agmt_no = "", agmt_rev = "", cont_type = "", cont_rev = "", cargo_no = "", nom_rev = "", boe_no = "", boe_qty="",cargo_ref_no="";
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
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		cargo_ref_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		cargo_ref_no = cargo_ref_no.substring(1, cargo_ref_no.length()-1);	
				    		
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
				    		
				    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO "
				    				+ "FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cargo_ref_no);
				    		stmt.setString(2, cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {				    			
				    			agmt_type = rset.getString(1);
				    			agmt_no = rset.getString(2);
				    			agmt_rev = rset.getString(3);
				    			cont_type = rset.getString(4);
				    			no = rset.getInt(5);
				    			cont_rev =  rset.getString(6);
				    			cargo_no =  rset.getString(7);	
				    		}
				    	
				    		rset.close();
				    		stmt.close();
				    	}
				    	else if (cell.getColumnIndex() == 2) {					    		
				    		data = agmt_type;
				    	}
				    	else if (cell.getColumnIndex() == 3) {					    		
				    		data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 4) {				    		
				    		data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 5) {					    		
				    		data = cont_type;
				    	}
				    	else if (cell.getColumnIndex() == 6) {					    			
			    			data = no+"";	
				    	}
				    	else if (cell.getColumnIndex() == 7) {					    			
			    			data = cont_rev;	
				    	}
				    	else if (cell.getColumnIndex() == 8) {	// Cargo_no
				    		data = cargo_no;
				    		
				    	}
				    	
				    	else {
				    		if (cell.getColumnIndex() == 9) {	// nom_rev
				    			nom_rev = cell.getStringCellValue();
				    			nom_rev = nom_rev.substring(1, nom_rev.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 10) {	// Boe_No
				    			boe_no = cell.getStringCellValue();
				    			boe_no = boe_no.substring(1, boe_no.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 13) {	// boe_qty
				    			boe_qty = cell.getStringCellValue();
				    			boe_qty = boe_qty.substring(1, boe_qty.length()-1);
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
//				    	System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_NOM_BOE WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CARGO_NO = ? AND NOM_REV = ? AND BOE_NO = ? AND CONT_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_type);
			    	stmt.setString(4, agmt_no);			
			    	stmt.setString(5, agmt_rev);			
			    	stmt.setString(6,cont_type);
			    	stmt.setString(7,cont_rev);
			    	stmt.setString(8, cargo_no);
			    	stmt.setString(9, nom_rev);
			    	stmt.setString(10, boe_no);			   
			    	stmt.setInt(11, no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !agmt_type.equals("")) {
				    	//System.out.println(queryString1);
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+" ,"+agmt_type+", "+agmt_no+", "+agmt_rev+", "+cont_type+", "+no+", "+cont_rev+", "+cargo_no+", "+nom_rev+", "+boe_no+","+cargo_ref_no+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    } else {
				    	agmt_no = "";
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+", "+agmt_type+", "+agmt_no+", "+agmt_rev+" ,"+cont_type+" ,"+no+" ,"+cont_rev+" ,"+cargo_no+", "+nom_rev+" ,"+boe_no+","+cargo_ref_no+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_BUY_CARGO_NOM_BOE>>", conn);
			
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

	public void FMS_BUY_CARGO_ALLOC() throws IOException, SQLException {

		function_nm="FMS_BUY_CARGO_ALLOC()";
		try {
			table_name = "FMS_BUY_CARGO_ALLOC";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BUY_CARGO_ALLOC>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;

			// Getting the start of cont_no
			int no = 1000;

			queryString1 = "INSERT INTO FMS_BUY_CARGO_ALLOC(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,"
					+ "CONT_REV,CARGO_NO,ALLOC_REV,SHIP_CD,ACT_ARRV_DT,BOOKED_DT,BOOKED_TIME,FLOAT_DT,FLOAT_TIME,PILOT_ON_BOARD_DT,"
					+ "PILOT_ON_BOARD_TIME,UNLOAD_ARM_CON_DT,UNLOAD_ARM_CON_TIME,UNLOAD_ARM_DIS_CON_DT,UNLOAD_ARM_DIS_CON_TIME,"
					+ "DISCHARGE_DT,DISCHARGE_TIME,REMARK,QQ_NO,QQ_DT,QQ_QTY_MMBTU,QQ_QQ_QTY_MT,QQ_QQ_QTY_SCM,QQ_DENSITY,QQ_GHV,QQ_GCV,"
					+ "QQ_REMARK,ACT_QTY_MMBTU,ENT_BY,ENT_DT,ALL_FAST_DT,ALL_FAST_TIME,CUSTOME_CLEARANCE_START_DT,CUSTOME_CLEARANCE_START_TIME,"
					+ "CUSTOME_CLEARANCE_END_DT,CUSTOME_CLEARANCE_END_TIME) VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,CARGO_REF_NO,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String agmt_no = "", cargo_no = "",ship_cd = "",agmt_type = "",agmt_rev = "",cont_type = "",cont_rev = "",alloc_rev = "",cargo_ref_no="";
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
				    		
				    		data = company_cd;
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
						
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		
				    		cargo_ref_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		cargo_ref_no = cargo_ref_no.substring(1, cargo_ref_no.length()-1);
				    		
				    		data = cd;
				    		
				    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO "
				    				+ "FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ?";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cargo_ref_no);
				    		stmt.setString(2, cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			agmt_type = rset.getString(1);
				    			agmt_no = rset.getString(2);
				    			agmt_rev = rset.getString(3);
				    			cont_type = rset.getString(4);
				    			no = rset.getInt(5);
				    			cont_rev = rset.getString(6);
				    			cargo_no = rset.getString(7);
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
				    	else if (cell.getColumnIndex() == 2) {					    		
				    		data = agmt_type;	
				    	}
				    	else if (cell.getColumnIndex() == 3) {					    		
				    		data = agmt_no;	
				    	}
				    	else if (cell.getColumnIndex() == 4) {					    		
				    		data = agmt_rev;	
				    	}
				    	else if (cell.getColumnIndex() == 5) {					    		
				    		data = cont_type;	
				    	}
				    	else if (cell.getColumnIndex() == 6) {					    		
			    			data = no+"";	
				    	}
				    	else if (cell.getColumnIndex() == 7) {					    		
				    		data = cont_rev;	
				    	}
				    	else if (cell.getColumnIndex() == 8) {	
				    		data = cargo_no;
				    		
				    	}
				    	else if (cell.getColumnIndex() == 10) {	// Ship_cd
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    		queryString = "SELECT SHIP_CD FROM FMS_BUY_CARGO_NOM WHERE CONT_NO = ? AND CARGO_NO = ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setInt(1, no);
				    		stmt.setString(2, cargo_no);
				    		stmt.setString(3,cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
					    		ship_cd = rset.getString(1);
				    		}
				    		else  {
				    			ship_cd = "0";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = ship_cd;
				    	}
				    	
						
				    	else {				    		
				    		if (cell.getColumnIndex() == 9) {	// alloc_rev
				    			alloc_rev = cell.getStringCellValue();
				    			alloc_rev = alloc_rev.substring(1, alloc_rev.length()-1);
				    		}
				    						    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_ALLOC WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CARGO_NO = ? AND ALLOC_REV = ? AND SHIP_CD = ? AND CONT_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_type);
			    	stmt.setString(4, agmt_no);
			    	stmt.setString(5, agmt_rev);
			    	stmt.setString(6, cont_type);
			    	stmt.setString(7, cont_rev);
			    	stmt.setString(8, cargo_no);
			    	stmt.setString(9, alloc_rev);
			    	stmt.setString(10, ship_cd);
			    	stmt.setInt(11, no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !agmt_type.equals("") && !cd.equals("0")) {
				    	//System.out.println(queryString1);
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+", "+agmt_type+", "+agmt_no+" ,"+agmt_rev+", "+cont_type+", "+no+" ,"+cont_rev+", "+cargo_no+", "+alloc_rev+","+cargo_ref_no+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;			    	
				    } else {
				    	agmt_no = "";
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+", "+agmt_type+" ,"+agmt_no+", "+agmt_rev+", "+cont_type+" ,"+no+" ,"+cont_rev+" ,"+cargo_no+", "+alloc_rev+","+cargo_ref_no+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_BUY_CARGO_ALLOC>>", conn);
			
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

	public void FMS_BUY_CARGO_ALLOC_BL() throws IOException, SQLException {

		function_nm="FMS_BUY_CARGO_ALLOC_BL()";
		try {
			table_name = "FMS_BUY_CARGO_ALLOC_BL";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BUY_CARGO_ALLOC_BL>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			// Getting the start of cont_no
			int no = 0;
			

			queryString1 = "INSERT INTO FMS_BUY_CARGO_ALLOC_BL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,"
					+ "CONT_REV,CARGO_NO,ALLOC_REV,BL_NO,BL_REF,BL_DT,IMPORT_DEPT_SNO,IMPORT_CD,ENDORSE_DT,REMARK,ENT_BY,ENT_DT,FLAG) VALUES(?,"
					+ "?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?,"
					+ " 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_BL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_BL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				logger.checkpoint(fname, "COMPANY_CD,CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BL_NO,CONT_REF_NO,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String agmt_no = "", cargo_no = "", bl_no = "", bl_ref ="",agmt_type = "",agmt_rev = "",cont_type = "",cont_rev = "",alloc_rev = "",cargo_ref_no="";
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
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		cargo_ref_no = cell.getStringCellValue();
				    		cargo_ref_no = cargo_ref_no.substring(1, cargo_ref_no.length()-1);	
				    		
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
				    		
				    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO "
				    				+ "FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD='2'";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cargo_ref_no);
				    		stmt.setString(2, cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {				    			
				    			agmt_type = rset.getString(1);
				    			agmt_no = rset.getString(2);
				    			agmt_rev = rset.getString(3);
				    			cont_type = rset.getString(4);
				    			no = rset.getInt(5);
				    			cont_rev =  rset.getString(6);		
				    			cargo_no =  rset.getString(7);	
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
				    	else if (cell.getColumnIndex() == 2) {			    		
				    		data = agmt_type;	
				    	}
				    	else if (cell.getColumnIndex() == 3) {					    		
				    		data = agmt_no;	
				    	}
				    	else if (cell.getColumnIndex() == 4) {					    		
				    		data = agmt_rev;	
				    	}
				    	else if (cell.getColumnIndex() == 5) {					    		
				    		data = cont_type;	
				    	}
				    	else if (cell.getColumnIndex() == 6) {	// Cont_no		
			    			data = no+"";	
				    	}
				    	else if (cell.getColumnIndex() == 7) {					    		
				    		data = cont_rev;	
				    	}
				    	else if (cell.getColumnIndex() == 8) {	// Cargo_no
				    		data = cargo_no;
				    	}
						
				    	else {				    		
				    		if (cell.getColumnIndex() == 9) {	// alloc_rev
				    			alloc_rev = cell.getStringCellValue();
				    			alloc_rev = alloc_rev.substring(1, alloc_rev.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 10) {	// Bl_No
				    			bl_no = cell.getStringCellValue();
				    			bl_no = bl_no.substring(1, bl_no.length()-1);
				    		}
				    		
				    		if (cell.getColumnIndex() == 11) {	// Bl_ref
				    			bl_ref = cell.getStringCellValue();
				    			bl_ref = bl_ref.substring(1, bl_ref.length()-1);
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	// System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_ALLOC_BL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CARGO_NO = ? AND ALLOC_REV = ? AND BL_NO = ? AND CONT_NO = ?"; 
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_type);
			    	stmt.setString(4, agmt_no);
			    	stmt.setString(5, agmt_rev);
			    	stmt.setString(6, cont_type);
			    	stmt.setString(7, cont_rev);
			    	stmt.setString(8, cargo_no);
			    	stmt.setString(9, alloc_rev);
			    	stmt.setString(10, bl_no);			    
			    	stmt.setInt(11, no);
			    	
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !agmt_type.equals("")) {
//				    	"CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BL_NO,CONT_REF_NO,TIMESTAMP"
				    	//System.out.println(queryString1);
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+", "+agmt_type+", "+agmt_no+", "+agmt_rev+", "+cont_type+", "+no+", "+cont_rev+", "+cargo_no+", "+alloc_rev+", "+bl_no+", "+cargo_ref_no+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    } else {
				    	agmt_no = "";
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+", "+agmt_type+" ,"+agmt_no+" ,"+agmt_rev+", "+cont_type+", "+no+", "+cont_rev+", "+cargo_no+" ,"+alloc_rev+" ,"+bl_no+" ,"+cargo_ref_no+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_BUY_CARGO_ALLOC_BL>>", conn);
			
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

	public void FMS_BUY_CARGO_ALLOC_BOE() throws IOException, SQLException {

		function_nm="FMS_BUY_CARGO_ALLOC_BOE()";
		try {
			table_name = "FMS_BUY_CARGO_ALLOC_BOE";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BUY_CARGO_ALLOC_BOE>>,,,," , conn);
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
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


			queryString1 = "INSERT INTO FMS_BUY_CARGO_ALLOC_BOE(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,"
					+ "CONT_REV,CARGO_NO,ALLOC_REV,BOE_NO,BU_SEQ,PLANT_SEQ,BOE_REF,BOE_DT,ACT_BOE_QTY,ACT_BOE_QTY_UNIT,ACT_QTY_MT,ACT_QTY_SCM,"
					+ "CUSTOM_DUTY,LOAD_PORT,ENT_BY,ENT_DT,FLAG,BOE_PROVISIONAL_PRICE,BOE_PROVISIONAL_PRICE_UNIT,BOE_FINAL_PRICE,"
					+ "BOE_FINAL_PRICE_UNIT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_BOE_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BUY_CARGO_ALLOC_BOE_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,BOE_NO,CARGO_REF_NO,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String agmt_no = "", cargo_no = "", boe_no = "",boe_ref="",agmt_type = "",agmt_rev = "",cont_type = "",cont_rev = "",alloc_rev = "",cargo_ref_no="";
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
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		cargo_ref_no = cell.getStringCellValue();
				    		cargo_ref_no = cargo_ref_no.substring(1, cargo_ref_no.length()-1);	
				    		
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
				    		
				    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO "
				    				+ "FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD= ?";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cargo_ref_no);
				    		stmt.setString(2, cd);
				    		stmt.setString(3, company_cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {				    			
				    			agmt_type = rset.getString(1);
				    			agmt_no = rset.getString(2);
				    			agmt_rev = rset.getString(3);
				    			cont_type = rset.getString(4);
				    			no = rset.getInt(5);
				    			cont_rev =  rset.getString(6);
				    			cargo_no =  rset.getString(7);
				    		}
				    		rset.close();
				    		stmt.close();
				    	}
				    	else if (cell.getColumnIndex() == 2) {			    		
				    		data = agmt_type;	
				    	}
				    	else if (cell.getColumnIndex() == 3) {			    		
				    		data = agmt_no;	
				    	}
				    	else if (cell.getColumnIndex() == 4) {			    		
				    		data = agmt_rev;	
				    	}
				    	else if (cell.getColumnIndex() == 5) {			    		
				    		data = cont_type;	
				    	}
				    	else if (cell.getColumnIndex() == 6) {					    			
			    			data = no+"";
				    	}
				    	else if (cell.getColumnIndex() == 7) {			    		
				    		data = cont_rev;	
				    	}
				    	else if (cell.getColumnIndex() == 8) {	// Cargo_no
				    		data = cargo_no;
				    	}
				    	else if (cell.getColumnIndex() == 17) {	// ACT_QTY_MT
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	else if (cell.getColumnIndex() == 18) {	// ACT_QTY_SCM
				    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
					
				    	else {
				    		if (cell.getColumnIndex() == 9) {	// alloc_rev
				    			alloc_rev = cell.getStringCellValue();
				    			alloc_rev = alloc_rev.substring(1, alloc_rev.length()-1);
				    		}
				    		if (cell.getColumnIndex() == 10) {	// Boe_No
				    			boe_no = cell.getStringCellValue();
				    			boe_no = boe_no.substring(1, boe_no.length()-1);
				    		}
				    		
				    		if (cell.getColumnIndex() == 13) {	// Boe_ref
				    			boe_ref = cell.getStringCellValue();
				    			boe_ref = boe_ref.substring(1, boe_ref.length()-1);
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	//System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT CONT_NO FROM FMS_BUY_CARGO_ALLOC_BOE WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CARGO_NO = ? AND ALLOC_REV = ? AND BOE_NO = ? AND CONT_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);			    	
			    	stmt.setString(3, agmt_type);
			    	stmt.setString(4, agmt_no);
			    	stmt.setString(5, agmt_rev);
			    	stmt.setString(6, cont_type);			
			    	stmt.setString(7, cont_rev);			
			    	stmt.setString(8, cargo_no);
			    	stmt.setString(9, alloc_rev);
			    	stmt.setString(10, boe_no);			    	
			    	stmt.setInt(11, no);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !agmt_type.equals("")) {
				    	//System.out.println(queryString1);
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+", "+agmt_type+", "+agmt_no+", "+agmt_rev+", "+cont_type+", "+no+", "+cont_rev+", "+cargo_no+", "+alloc_rev+", "+boe_no+", "+cargo_ref_no+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    } else {
				    	agmt_no = "";
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+(cd+"-"+abbr)+", "+agmt_type+", "+agmt_no+", "+agmt_rev+", "+cont_type+", "+no+", "+cont_rev+", "+cargo_no+", "+alloc_rev+", "+boe_no+", "+cargo_ref_no+","), conn,"E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_BUY_CARGO_ALLOC_BOE>>", conn);
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

	
	public void FMS_TRADER_CONT_MST() throws IOException, SQLException {

		function_nm="FMS_TRADER_CONT_MST()";
		try {
			table_name = "FMS_TRADER_CONT_MST";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_TRADER_CONT_MST>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			// Getting the start of cont_no
			int no = 1000;

			
			queryString1= "INSERT INTO FMS_TRADER_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,CONT_REF_NO,TRADE_REF_NO,"
					+ "SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,"
					+ "VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,"
					+ "BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,"
					+ "DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,"
					+ "CONT_STATUS,TXN_CHARGE,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,DDA_DT,DDA_TIME,TXN_UNIT,DAY_DEF_CLAUSE,MEASUREMENT,"
					+ "MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,"
					+ "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE"
					+ ",TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,"
					+ "CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,"
					+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String agmt_no = "",trade_ref_no="",sn_no="", cont_type = "", cont_rev = "",cont_ref_no = "" ,cp_abbr="",dom_buy_flag="",agmt_rev_no="",cont_no="",cargo_no="";
					abbr = "";
					cd = "0";
					data = null;
//					int m=1;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
				    
					row = rowIterator.next();
				    cellIterator = row.cellIterator();
				    while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						data = null;
						
				    	if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 

				    		sn_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (sn_no!=null) {
								sn_no = sn_no.substring(1, sn_no.length() - 1);
								cont_type=sn_no.split("-")[3];
								cargo_no=sn_no.split("-")[4];
					    		abbr=sn_no.split("-")[0];
					    		
							}
				    		else
				    		{
				    			cont_type=null;
				    			cargo_no=null;
				    			abbr=null;
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
				    		}
				    		rset.close();
				    		stmt.close();				    		
				    		if(!cd.equals("0")) {
				    			data=cd;
				    		}
				    		else {
				    			cd=null;
				    		}
				    		
				    	}
				    	
				    	
				    	else if(cell.getColumnIndex() == 4  && cont_type!=null ) {
				    		 //cont_no
				    		cont_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if(cont_no!=null)
				    		{
				    			cont_no = cont_no.substring(3, cont_no.length()-1);
				    		}
				    		if(cont_type.equals("I")) {
					    		queryString3="SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ? ";
					    		stmt3 = conn.prepareStatement(queryString3);
	//				    		System.out.println(sn_no+"-%");
					    		stmt3.setString(1,sn_no+"-%");
					    		rset3 = stmt3.executeQuery();
					    		if(rset3.next()) {
					    			no= rset3.getInt(1);
					    			data=no+"";
					    			
					    		}
					    		else {
					    		
					    		queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_TRADER_CONT_MST  WHERE CONT_NO LIKE ? " ;
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1,"8"+cont_no+"%");
					    		rset = stmt.executeQuery();
					    		if (rset.next() && rset.getInt(1) > 0) {
					    			no = rset.getInt(1);
					    			data=no+"";
					    				
					    		}else{
					    			// Getting the start of cont_no	
					    			no = 1000;			    				
				    				no = no * Integer.parseInt("8" +cont_no);
				    				no++;
					    		}
					    		rset.close();
					    		stmt.close();
					    		}	
					    		rset3.close();
					    		stmt3.close();
				    			data = no+"";	
//				    			System.out.println(no);
				    		}
				    		else if(cont_type.equals("D") || cont_type.equals("T") ) {
//				    			System.out.println("==>"+cont_no+","+sn_no);
				    			queryString3="SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ? AND COMPANY_CD= ? ";
					    		stmt3 = conn.prepareStatement(queryString3);
					    		stmt3.setString(1,sn_no+"-%");
					    		stmt3.setString(2,company_cd);
					    		rset3 = stmt3.executeQuery();
					    		if(rset3.next()) {
					    			no= rset3.getInt(1);
					    		}
					    		else {
					    		
					    		queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_TRADER_CONT_MST WHERE CONT_NO LIKE ?  AND COMPANY_CD= ?" ;
					    		
					    		stmt = conn.prepareStatement(queryString);		
					    		stmt.setString(1,"8"+cont_no+"%");
					    		stmt.setString(2,company_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next() && rset.getInt(1) > 0) {
					    			no = rset.getInt(1);
//					    			System.out.println("I=>"+no+"-DT");
					    				
					    		}else{
					    			// Getting the start of cont_no	
					    			no = 1000;			    				
				    				no = no * Integer.parseInt("8" +cont_no);
				    				no++;
					    		}
					    		rset.close();
					    		stmt.close();
					    		}	
					    		rset3.close();
					    		stmt3.close();
				    			data = no+"";	   
				    		}
				    		
				    	}
				    	
				    	else if(cell.getColumnIndex() == 7) { //cont_name
				    		queryString = "SELECT COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_CD = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cp_abbr = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		value = cp_abbr + "-SEIPL-" + no+  "-REV0";
				    		data = value;
				    		
				    	}
				    	else if(cell.getColumnIndex() == 45)
				    	{
				    		data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	
				    	else   if(cell.getColumnIndex() ==69) {
				    		 data=null;
				    	 }
				    	
				    	else {
					    	if (cell.getColumnIndex() == 2) {	//agmt_no
						    	agmt_no = cell.getStringCellValue();
						    	agmt_no = agmt_no.substring(1, agmt_no.length()-1);
//						    	System.out.println(agmt_no);
						    }
					    	 if (cell.getColumnIndex() == 3) {	// agmt_rev_no
					    		agmt_rev_no = cell.getStringCellValue();
					    		agmt_rev_no = agmt_rev_no.substring(1, agmt_rev_no.length()-1);
//					    		System.out.println(agmt_rev_no);
					    	}
					    	 if (cell.getColumnIndex() == 5) {	// cont_rev
				    			cont_rev = cell.getStringCellValue();
				    			cont_rev = cont_rev.substring(1, cont_rev.length()-1);
//					    		System.out.println(cont_rev);

				    			
				    		}
					    	 if (cell.getColumnIndex() ==6) {	// dom_buy_flag
					    		
				    			dom_buy_flag = cell.getStringCellValue();
				    			dom_buy_flag= dom_buy_flag.substring(1, dom_buy_flag.length()-1);
				    			if(dom_buy_flag.equals("null")) {
				    				dom_buy_flag=null;
//				    			    data=dom_buy_flag;
				    			}
				    			else if(dom_buy_flag.equals("")) {
				    				dom_buy_flag=null;
//				    				data=dom_buy_flag;
				    			}
				    			else { 
				    				dom_buy_flag = cell.getStringCellValue();
					    			dom_buy_flag= dom_buy_flag.substring(1, dom_buy_flag.length()-1);
				    			}
//					    		System.out.println(dom_buy_flag);

					    	 }	
					    	 if (cell.getColumnIndex() ==8) {	// cont_ref_no
				    			cont_ref_no = cell.getStringCellValue();
				    			cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length()-1);	
					    	}
					    	 if (cell.getColumnIndex() == 9) {	// trade_ref_no
					    			
					    		trade_ref_no = cell.getStringCellValue();
					    		trade_ref_no = trade_ref_no.substring(1, trade_ref_no.length()-1);	
					    		
					    	}
					    	
				    		data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
//				    	System.out.println(index+"===>"+data);
				    	stmt1.setString(index++, data);
				    }
				     
//				    "COMPANY_CD", "COUNTERPARTY_CD", "AGMT_NO", "AGMT_REV", "CONT_NO", "CONT_REV", "CONTRACT_TYPE")
				    queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ?"
			    			+ "  AND AGMT_REV = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? "
			    			+ "AND CONT_REF_NO = ? ";
//			    			+ "AND TRADE_REF_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev_no);
			    	stmt.setString(5, cont_rev);
			    	stmt.setString(6,dom_buy_flag );
			    	stmt.setString(7,cont_ref_no );
//			    	stmt.setString(8,trade_ref_no );
			    	
			    	rset = stmt.executeQuery();
			    				    	
				    if (row.getRowNum() != 0 && !rset.next() && cd!=null && dom_buy_flag!=null && cargo_no!=null ) {
				    	
				    	logger.data(fname,(company_cd+","+(cd+":"+abbr)+", "+agmt_no+", "+agmt_rev_no+","+no+", "+cont_rev+","+dom_buy_flag+","), conn,"");
				    	
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	
				    	skipped_count++;
//				    	no--;
				    	stmt1.close();
				    	logger.data(fname,(company_cd+","+(cd+":"+abbr)+", "+agmt_no+", "+agmt_rev_no+","+no+", "+cont_rev+","+dom_buy_flag+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count), conn); 
						
			logger.checkpoint(fname, "<<END>><<FMS_TRADER_CONT_MST>>", conn);
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
	
	public void FMS_TRADER_BILLING_DTL() throws IOException, SQLException {

		function_nm="FMS_TRADER_BILLING_DTL()";
		try {
			table_name = "FMS_TRADER_BILLING_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_TRADER_BILLING_DTL>>,,,,,," , conn);
				
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			String cont_no ="",cont_ref_no = "",sn_no = "",sn_rev="",cont_type = "" ;
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			queryString1= "INSERT INTO FMS_TRADER_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,"
					+ "SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
					+ "EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,"
					+ "BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,SPLIT_FLAG,PLANT_SEQ_NO,HOLIDAY_STATE)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "?,?,?,?,?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_BILLING_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_BILLING_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SN_NO,PLANT_SEQ_NO,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {  
					total_count++;

					String dom_buy_flag="";
					String int_cal_rate_nm="",exchng_rate_nm="";
					String  cont_rev="" ,agmt_rev_no="", agmt_no = "", plant_seq_no="",int_cal_rate_cd="",exchng_rate_cd="";
					cd = "";abbr="";
					cont_no="";cont_ref_no = "";sn_no = "";sn_rev="";cont_type = "" ;
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
				    
					row = rowIterator.next();
				    cellIterator = row.cellIterator();
				    while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						data = null;
						
				    	if (cell.getColumnIndex() == 0) {	// SN_NAME,CONT_REF_NO,TRADE_REF_NO 
				    		sn_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (sn_no!=null) {
								sn_no = sn_no.substring(1, sn_no.length() - 1);
								cont_type=sn_no.split("-")[3];
							}
				    		else
				    		{
				    			cont_type=null;
				    		}
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1 && cont_type!=null) {	// COUNTERPARTY_CD
				    		
				    		data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
							abbr=data;
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ?";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, data);
							rset = stmt.executeQuery();
							
							if(rset.next()) {
								cd = rset.getString(1);
							}
							rset.close();
							stmt.close();
							
				    		if(cont_type.equals("I")) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, sn_no);
								stmt.setString(2, company_cd);
								rset = stmt.executeQuery();
								while (rset.next()) {
//					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								
								
					    		data = cd;
				    		}
				    		else if(cont_type.equals("D") || cont_type.equals("T")) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ?";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, sn_no);
								stmt.setString(2, company_cd);
								rset = stmt.executeQuery();
								while (rset.next()) {
//					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								
								
					    		data = cd;
				    		}
				    	}
				    	else if(cell.getColumnIndex() == 2) { //AGMT_NO
				    		data = agmt_no+"";	
				    	
				    	}
				    	else if(cell.getColumnIndex() == 3) { //AGMT_REV_NO
				    		data = agmt_rev_no+"";	
				    	
				    	}
				    	else if(cell.getColumnIndex() == 4) {  //CONT_NO
			    			data = cont_no+"";			    			
			    			
				    	}
				    	
						
				    	else if (cell.getColumnIndex() == 5) {	// CONT_REV
					    		data = cont_rev+"";
				    	}

				    		
				    	else if (cell.getColumnIndex() == 6) {	// DOM_FLAG(CONT_TYPE)
					    		data = dom_buy_flag;
				    	}
				    	else if (cell.getColumnIndex() == 13) { // int_cal_rate_cd
				    		int_cal_rate_nm = cell.getStringCellValue().toUpperCase();
				    		int_cal_rate_nm = int_cal_rate_nm.substring(1, int_cal_rate_nm.length() - 1);
				

							if(int_cal_rate_nm.equals("SB BR")) {
								int_cal_rate_nm = "SBI BR";
							}
							queryString = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) = ? ";
							stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1,int_cal_rate_nm );
					    	rset = stmt.executeQuery();
					    	if (rset.next()) {
					    		int_cal_rate_cd = rset.getString(1);
					    	}
					    	rset.close();
					    	stmt.close();
					    	
					    	data =int_cal_rate_cd ;
			    	}
				    	
				    	else if (cell.getColumnIndex() == 16) { // exc_cal_rate_cd
				    		exchng_rate_nm = cell.getStringCellValue();
				    		exchng_rate_nm = exchng_rate_nm.substring(1, exchng_rate_nm.length() - 1);
				    		
//				    		if(exchng_rate_nm.contains("FBIPL"))
//				    		{
//				    			exchng_rate_nm=exchng_rate_nm;
//				    		}
//				    		else
//				    		{
//				    			exchng_rate_nm=exchng_rate_nm.toUpperCase();
//				    		}
				    		if(!exchng_rate_nm.contains("FBIPL"))
				    		{
				    			exchng_rate_nm=exchng_rate_nm.toUpperCase();
				    		}
				    		
//				    		if(exchng_rate_nm.contains("FBIPL"))
//				    		{
//				    			System.out.println(":"+exchng_rate_nm+":");
//				    		}
				    		
				    		if (exchng_rate_nm.contains("CUSTOMS RATE")) {
				    			exchng_rate_nm = "CUSTOM EXCHANGE RATE";
							}
							else if (exchng_rate_nm.contains("RBI REFERENCE")) {
								exchng_rate_nm = "RBI REFERENCE RATE";
							}
							else if (exchng_rate_nm.contains("SBI MUMBAI TT AVERAGE")) {
								exchng_rate_nm = "SBI MUMBAI TT BUY SELL";
							}
							else if (exchng_rate_nm.contains("SBI TT BUYING")) {
								exchng_rate_nm = "SBI RATE BUY";
							}
							else if (exchng_rate_nm.contains("SBI TT SELLING")) {
								exchng_rate_nm = "SBI RATE SELL";
							}
							else if (exchng_rate_nm.contains("SBI TT BUY SELL")) {
								exchng_rate_nm = "SBI RATE BUY SELL";
							}
				    		
				    		
				    		
				    		queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE EXC_RATE_NM = ? ";
							stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1,exchng_rate_nm );
					    	rset = stmt.executeQuery();
					    	if (rset.next()) {
					    		exchng_rate_cd = rset.getString(1);
					    	}
					    	rset.close();
					    	stmt.close();
					    	
					    	data =exchng_rate_cd ;
					    	
			    	}
				    	
				    	else if(cell.getColumnIndex()==31) {
								plant_seq_no = cell.getStringCellValue();
								plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
								data = plant_seq_no;
						}
				    	
				    	
				    	

				    		else {
				    			
				    			
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT BILLING_FREQ FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev_no);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, dom_buy_flag );
			    	stmt.setString(8, plant_seq_no );
			    	
			    	rset = stmt.executeQuery();
			    	

			    				    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("") && cd!=null) {
				    	
				    	logger.data(fname,(company_cd+","+(cd+":"+abbr)+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+sn_no+","+plant_seq_no+","), conn,"");
				    	
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	
				    	skipped_count++;
				    	stmt1.close();
				    	logger.data(fname,(company_cd+","+(cd+":"+abbr)+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+sn_no+","+plant_seq_no+","), conn, "E");
				    }				    
				    rset.close();
				    stmt.close();			
				}
				
				
				//for cargo which dont have contract:adding billing
				
				String ent_dt="",ent_by="";
			
				queryString = "SELECT CONT_REF_NO,CONT_NO,COUNTERPARTY_CD,CONTRACT_TYPE,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),ENT_BY "
						+ "FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD = '2' ";
		    	stmt = conn.prepareStatement(queryString);
		    	rset = stmt.executeQuery();
		    	
		    	while(rset.next())
		    	{
//		    		AMNS-0-1-D-21059-0
		    		cont_ref_no=rset.getString(1);
		    	    cont_no=rset.getString(2);
		    	    cont_type = rset.getString(4);
		    	    cd=rset.getString(3);
		    	    ent_dt=rset.getString(5);
		    	    ent_by=rset.getString(6);
		    		
		    	    
		    	    sn_no=cont_ref_no.split("-")[1];
		    	    sn_rev=cont_ref_no.split("-")[5];
		    	    
		    		if(sn_no.equals("0") && sn_rev.equals("0"))
		    		{
		    			
		    			queryString1 = "INSERT INTO FMS_TRADER_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
		    					+ "CONTRACT_TYPE,BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,"
		    					+ "INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
		    					
		    					+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,"
		    					+ "ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,"
		    					+ "BILLING_DAYS,EXCHG_VAL,EXCL_SAT_MAP,SPLIT_FLAG,PLANT_SEQ_NO,HOLIDAY_STATE)"
		    					+ "VALUES(?,?,?,?,?,'0',"
		    					+ "?,'F','B','3',NULL,'1','1',"
		    					+ "'7','+','2','10'"
		    					+ ",'D','LST',NULL,NULL,"
		    					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,NULL,NULL,NULL,NULL,"
		    					+ "NULL,NULL,NULL,NULL,'1',NULL) ";
				    	stmt1 = conn.prepareStatement(queryString1);
				    	stmt1.setString(1, company_cd);
				    	stmt1.setString(2, cd );
				    	stmt1.setString(3, sn_no);
				    	stmt1.setString(4, sn_rev);
				    	stmt1.setString(5, cont_no);
				    	stmt1.setString(6, cont_type);
				    	stmt1.setString(7, ent_dt);
				    	stmt1.setString(8, ent_by);
				    	
				    	stmt1.executeUpdate();
				    	stmt1.close();
//				    	logger_count++;
				    	logger.data(fname,(company_cd+","+cd+", "+sn_no+", "+sn_rev+","+cont_no+", "+cont_type+","+ent_dt+","+ent_by+","+"Cargo without contract in FMS ,"), conn, "");
		    		}
		    	}
		    	rset.close();
		    	stmt.close();
				
		
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count+",,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, "<<END>><<FMS_TRADER_BILLING_DTL>>", conn);
			
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
	
	public void FMS_BUY_DAILY_BUYER_NOM() throws IOException, SQLException {

		function_nm="FMS_BUY_DAILY_BUYER_NOM()";
		try {
			table_name = "FMS_BUY_DAILY_BUYER_NOM";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BUY_DAILY_BUYER_NOM>>,,,,,," , conn);
				
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			queryString1= "INSERT INTO FMS_BUY_DAILY_BUYER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD"
					+ ",TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO ) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,"
					+ "?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_DAILY_BUYER_NOM_"+start_end_dt+".csv");
			if(file1.exists()) {

				String fileName = migration_setup_dir + "EXPORT/FMS_BUY_DAILY_BUYER_NOM_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

				String line = br.readLine();
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,TRANS_CD,TRANS_SEQ,BU_SEQ,GAS_DT,NOM_REV_NO,CARGO_NO,CONT_REF_NO,TIMESTAMP", conn);
				
				while ((line = br.readLine()) != null) {  
					total_count++;
					String  cont_type = "",dom_buy_flag="",cont_ref_no="",cargo_no="",trade_ref_no="";
					String cont_no ="", cont_rev="" ,agmt_rev_no="", agmt_no = "", plant_seq="" , trans_cd="", trans_seq="", bu_seq="",nom_rev_no="",gas_dt="",qty_mmbtu="";
					
					cd = "";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
				    

				    for (int i = 0; i < line.split(",").length; i++) {
//						cell = cellIterator.next();
						data = null;
				    	
						if (i == 0) {	// Counterparty_Abbr, Company Code 
							cont_ref_no = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
				    		if (cont_ref_no!=null) {
//								cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
								cont_type = cont_ref_no.split("-")[3];
					    		abbr = cont_ref_no.split("-")[0];
							}
				    		else
				    		{
				    			cont_type = null;
					    		abbr =null;
				    		}
							trade_ref_no = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);

							
				    		data = company_cd;
				    	}

						else if (i == 1 && cont_type != null) {	// COUNTERPARTY_CD
				    		if(cont_type.equals("I")) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cont_ref_no);
								stmt.setString(2, company_cd);
								rset = stmt.executeQuery();
								while (rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								
								
					    		data = cd;
				    		}
				    		else if(cont_type.equals("D") || cont_type.equals("T") ) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, trade_ref_no);
								stmt.setString(2, company_cd);
								rset = stmt.executeQuery();
								while (rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								
								
					    		data = cd;
				    		}
				    	}
				    	else if(i == 2) { //AGMT_NO
				    		data=agmt_no+"";
				    	
				    	}
				    		
				    	else if(i == 3) { //AGMT_REV_NO
				    					    		
				    		data=agmt_rev_no+"";
		
				    	}	    		
				    	else if(i == 4) { //CONT_NO
				    	
				    		data=cont_no+"";
				    	}
				    	else if(i == 5 ) { //CONT_REV
				    		
				    		data=cont_rev+"";
				    	}
				    	else if(i == 12) { //DOM_BUY_FLAG , CONTRACT_TYPE
				    		
				    		data=dom_buy_flag+"";

				    	}


		
				    	else {
//				    			
				    		if (i ==11) {	
					    			
					    		gas_dt=line.split(",")[i];
//					    		gas_dt = gas_dt.substring(1, gas_dt.length()-1);		    			
					    	}

				    		if (i ==8) {	
						    			
						    	trans_cd=line.split(",")[i];
//						    	trans_cd = trans_cd.substring(1, trans_cd.length()-1);
						    }
				    			 
				    		if (i ==9) {	
						    			
						    	trans_seq=line.split(",")[i];
//						    	trans_seq = trans_seq.substring(1, trans_seq.length()-1);
						    			
						    }
				    		if (i ==6) {	
						    			
						    	nom_rev_no=line.split(",")[i];
//						    	nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length()-1);
						    			
						    }
				    		
				    		if(i == 7) { // PLANT_SEQ_NO
				    			
				    			plant_seq=line.split(",")[i];
//				    			plant_seq = plant_seq.substring(1, plant_seq.length()-1);
						    
				    		}
				    		
				    		if(i == 10) { // BU_SEQ_NO
				    			
				    			bu_seq=line.split(",")[i];
//				    			bu_seq = bu_seq.substring(1, bu_seq.length()-1);
						    
				    		}
				    		if(i == 18) { // QTY_MMBTU
				    			
				    			qty_mmbtu=line.split(",")[i];
//				    			bu_seq = bu_seq.substring(1, bu_seq.length()-1);
						    
				    		}
				    			 
				    		if (i ==22) {	
						    			
						    	cargo_no=line.split(",")[i];
//						    	cargo_no = cargo_no.substring(1, cargo_no.length()-1);

						    }
					    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
				    	}

//				    	if(cont_ref_no.equals("AMNS-0-1-N-21052-0"))
//				    	{
//				    		System.out.println(":"+company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+",");
//				    	}
//						System.out.println(data);
				    	stmt1.setString(index++, data);
				    }

			    	queryString = "SELECT QTY_MMBTU FROM FMS_BUY_DAILY_BUYER_NOM WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
			    			+ " AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND TRANSPORTER_CD=? AND "
			    			+ "TRANS_SEQ=? AND BU_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND NOM_REV_NO = ?    AND CARGO_NO=?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev_no);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, dom_buy_flag );
			    	stmt.setString(8, plant_seq );
			    	stmt.setString(9, trans_cd );
			    	stmt.setString(10, trans_seq);
			    	stmt.setString(11, bu_seq );
			    	stmt.setString(12, gas_dt );
			    	stmt.setString(13, nom_rev_no );
			    	stmt.setString(14, cargo_no );


			    	
			    	rset = stmt.executeQuery();
			    				    	
				    if (!rset.next() && !cont_no.equals("") && cd!=null &&  !plant_seq.equals("") && !bu_seq.equals("")) {
				    	
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+nom_rev_no+","+cargo_no+","+cont_ref_no+","+qty_mmbtu+","), conn,"");
				    	stmt1.executeUpdate();

				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	
				    	skipped_count++;
				    	stmt1.close();
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+nom_rev_no+","+cargo_no+","+cont_ref_no+","+qty_mmbtu+","), conn, "E");
				    }				    
				    rset.close();
				    stmt.close();			
				}	
				br.close();
				}
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count+",,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, "<<END>><<FMS_BUY_DAILY_BUYER_NOM>>", conn);
			
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
	
	public void FMS_TRADER_CONT_BU() throws IOException, SQLException {

		function_nm="FMS_TRADER_CONT_BU()";
		try {
			table_name = "FMS_TRADER_CONT_BU";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_TRADER_CONT_BU>>,,,,,," , conn);
				
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			queryString1= "INSERT INTO FMS_TRADER_CONT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT,CONTRACT_TYPE)"
					+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_BU_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_BU_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {  
					total_count++;
					String  cont_type = "",dom_buy_flag="",cont_ref_no="",trade_ref_no="",plant_abbr="";
					String cont_no ="", cont_rev="" ,agmt_rev_no="", agmt_no = "", plant_seq="";
					cd = "0";
					data = null;
//					int m=1;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
				    
					row = rowIterator.next();
				    cellIterator = row.cellIterator();
				    while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						data = null;
				    	
						if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 
							cont_ref_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (cont_ref_no!=null) {
								cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
								abbr = cont_ref_no.split("-")[0];
					    		cont_type= cont_ref_no.split("-")[3];
							}
				    		else
				    		{
				    			abbr = null;
					    		cont_type= null;
				    		}
							trade_ref_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (trade_ref_no!=null) {
								trade_ref_no = trade_ref_no.substring(1, trade_ref_no.length() - 1);
							}
						
				    		data = company_cd;
				    	}
						else if (cell.getColumnIndex() == 1 && cont_type!=null) {	// Counterparty_Cd
								
								if(cont_type.equals("I")) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ?";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cont_ref_no);
					    		stmt.setString(2, company_cd);
					    		rset = stmt.executeQuery();
					    		while(rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag= rset.getString(6);
					    			
					    		}
					    		rset.close();
					    		stmt.close();				    		
//					    		
//					    			data=cd;

								}
								else if(cont_type.equals("D") || cont_type.equals("T")) {
						    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE "
						    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ?  AND COMPANY_CD = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, trade_ref_no);
						    		stmt.setString(2, company_cd);
						    		
						    		rset = stmt.executeQuery();
						    		while(rset.next()) {
						    			cd = rset.getString(1);
						    			agmt_no = rset.getString(2);
						    			agmt_rev_no = rset.getString(3);
						    			cont_no = rset.getString(4);
						    			cont_rev = rset.getString(5);
						    			dom_buy_flag= rset.getString(6);
						    			
						    		}
						    		rset.close();
						    		stmt.close();	
								}
								data=cd;
					    	}
				    	else if(cell.getColumnIndex() == 2) { //AGMT_NO
				    		data=agmt_no+"";
				    	}
				    	
				    	else if(cell.getColumnIndex() == 3) { //AGMT_REV_NO
				    		data=agmt_rev_no+"";
				    	}
				    	else if(cell.getColumnIndex() == 4) {  //CONT_NO
				    		
				    		data=cont_no+"";
			    		}
				    	else if(cell.getColumnIndex() == 5) {  //CONT_REV
				    		
				    		data=cont_rev+"";
				    	}
				    	else if(cell.getColumnIndex()==6) {
				    		plant_abbr=(cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if(plant_abbr!=null)
				    		{
				    			plant_abbr = plant_abbr.substring(1, plant_abbr.length()-1);
				    		}
				    		
				    		queryString="SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ? AND COMPANY_CD = ?";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1,plant_abbr);
				    		stmt.setString(2, company_cd);
				    		
				    		rset = stmt.executeQuery();
				    		while(rset.next()) {
				    			plant_seq=rset.getString(1);
				    			
				    		}
				    		data=plant_seq+"";
				    		rset.close();
				    		stmt.close();				    		
				    		
			    		}
				    	else if(cell.getColumnIndex() == 9) {  //CONTRACT_TYPE
				    		
				    		data=dom_buy_flag+"";

				    	}
									    
						
				    	else {
				    		
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	
//						System.out.println(index+"=="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT ENT_BY FROM FMS_TRADER_CONT_BU WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
			    			+ " AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev_no);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, dom_buy_flag );
			    	stmt.setString(8, plant_seq );
//			    	
			    	
			    	
			    	rset = stmt.executeQuery();
			    				    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("") && cd!=null) {
				    	
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","), conn,"");
				    	stmt1.executeUpdate();

				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	
				    	skipped_count++;
				    	stmt1.close();
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","), conn, "E");
				    }				    
				    rset.close();
				    stmt.close();			
				}				

				// cargo whose contract is not there in fms :\
				String ent_dt="",ent_by="",cont_ref_no="",cont_type="",sn_no="",sn_rev="",cont_rev="";
				
				queryString = "SELECT CONT_REF_NO,CONT_NO,COUNTERPARTY_CD,CONTRACT_TYPE,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),ENT_BY,CONT_REV "
						+ "FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD = '2'";
		    	stmt = conn.prepareStatement(queryString);
		    	rset = stmt.executeQuery();
		    	
		    	while(rset.next())
		    	{
//		    		AMNS-0-1-D-21059-0
		    		cont_ref_no=rset.getString(1);
		    	    cont_no=rset.getString(2);
		    	    cont_type = rset.getString(4);
		    	    cd=rset.getString(3);
		    	    ent_dt=rset.getString(5);
		    	    ent_by=rset.getString(6);
		    	    cont_rev=rset.getString(7);
		    		
		    	    
		    	    sn_no=cont_ref_no.split("-")[1];
		    	    sn_rev=cont_ref_no.split("-")[5];
		    	    
		    		if(sn_no.equals("0") && sn_rev.equals("0"))
		    		{
		    			queryString1 = "INSERT INTO FMS_TRADER_CONT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT,CONTRACT_TYPE)"
		    					+ "VALUES(?,?,?,?,?,?,'1',?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?)";
				    	stmt1 = conn.prepareStatement(queryString1);
				    	stmt1.setString(1, company_cd);
				    	stmt1.setString(2, cd );
				    	stmt1.setString(3, sn_no);
				    	stmt1.setString(4, sn_rev);
				    	stmt1.setString(5, cont_no);
				    	stmt1.setString(6, cont_rev);
				    	stmt1.setString(7, ent_by);
				    	stmt1.setString(8, ent_dt);
				    	stmt1.setString(9, cont_type);
				    	
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    	logger.data(fname,(company_cd+","+cd+", "+sn_no+", "+sn_rev+","+cont_no+", "+cont_type+","+ent_dt+","+ent_by+","+"Cargo without contract in FMS ,"), conn, "");
		    		}
		    	}
		    	rset.close();
		    	stmt.close();
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count+",,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, "<<END>><<FMS_BUY_DAILY_BUYER_NOM>>", conn);
			
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
	
	public void FMS_TRADER_CONT_PLANT() throws IOException, SQLException {

		function_nm="FMS_TRADER_CONT_PLANT()";
		try {
			table_name = "FMS_TRADER_CONT_PLANT";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_TRADER_CONT_PLANT>>,,,,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);

			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
	
		
			queryString1= "INSERT INTO FMS_TRADER_CONT_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE)"
					+"VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_PLANT_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_PLANT_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,CONTRACT_TYPE,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					String agmt_no = "",cont_type="",  cont_rev = "",cont_ref_no = "" ,cont_no="",contract_type="",agmt_rev_no="",trade_ref_no="";
					String plant_seq_no="";
					abbr = "";
					cd = "";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
				    
					row = rowIterator.next();
				    cellIterator = row.cellIterator();
				    while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						data = null;
						
				    	if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 
				    		cont_ref_no=(cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		cont_ref_no=cont_ref_no.substring(1, cont_ref_no.length()-1);
				    		
				    		trade_ref_no=(cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		trade_ref_no=trade_ref_no.substring(1, trade_ref_no.length()-1);
				    		
				    		cont_type= cont_ref_no.split("-")[3];
				    		abbr = cont_ref_no.split("-")[0];
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// COUNTERPARTY_CD
				    		if(cont_type.equals("I")) {
				    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
				    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ?";
				    		stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cont_ref_no);
							stmt.setString(2, company_cd);
							
							rset = stmt.executeQuery();
							if (rset.next()) {
				    			cd = rset.getString(1);
				    			agmt_no = rset.getString(2);
				    			agmt_rev_no = rset.getString(3);
				    			cont_no = rset.getString(4);
				    			cont_rev = rset.getString(5);
				    			contract_type = rset.getString(6);
				    		}
							rset.close();
				    		stmt.close();
							if(cd.equals("") || cd.equals("0")) {
								cd= null;
								data = cd;
							}
							else {
				    		data = cd;
							}
				    	}
				    		else if(cont_type.equals("D") || cont_type.equals("T")) {
				    			queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
				    					+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ?";
					    		stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cont_ref_no);
								stmt.setString(2, company_cd);
								
								rset = stmt.executeQuery();
								while (rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			contract_type = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								if(cd.equals("") || cd.equals("0")) {
									cd= null;
									data = cd;
								}
								else {
					    		data = cd;
								}
				    	}
				    }
				    
				    	
				    	else if(cell.getColumnIndex() == 2) {
				    		data = agmt_no+"";
				    		
				    	}
						else if(cell.getColumnIndex() == 3) {
								data = agmt_rev_no+"";			    		
					     }
						else if(cell.getColumnIndex()==4) {
							data = cont_no+"";
						}
						else if(cell.getColumnIndex()==5) {
							data = cont_rev+"";
						}
						else if(cell.getColumnIndex()==6) {
							plant_seq_no = cell.getStringCellValue();
							plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
							data = plant_seq_no;
						}
						
						else if(cell.getColumnIndex()==10) {
							data = contract_type+"";
						}
				    	
				    	
				    	
				    	
				   else {
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}

				    	stmt1.setString(index++, data);
				    }
			
			    	queryString = "SELECT ENT_DT FROM FMS_TRADER_CONT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND AGMT_NO = ?  AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ?  AND PLANT_SEQ_NO = ? AND CONTRACT_TYPE = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev_no);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, plant_seq_no);
			    	stmt.setString(8,contract_type );
			    	
			    	rset = stmt.executeQuery();
			    				    	
				    if (row.getRowNum() != 0 && !rset.next() && agmt_no!=null && cd!=null) {

				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+","+cont_rev+","+plant_seq_no+","+contract_type+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	
				    	skipped_count++;
				    	stmt1.close();
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+","+cont_rev+","+plant_seq_no+","+contract_type+","), conn,"E");
				    }				    
				    rset.close();
				    stmt.close();			
				}	
				
				//tradercontrct plant whose contract is not there in fms :
				String ent_dt="",ent_by="",cont_ref_no="",cont_type="",sn_no="",sn_rev="",cont_rev="";
				
					queryString = "SELECT CONT_REF_NO,CONT_NO,COUNTERPARTY_CD,CONTRACT_TYPE,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),ENT_BY,CONT_REV "
							+ "FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD = '2' ";
			    	stmt = conn.prepareStatement(queryString);
			    	rset = stmt.executeQuery();
			    	
			    	while(rset.next())
			    	{
			    		
	//		    		AMNS-0-1-D-21059-0
			    	    cont_ref_no=rset.getString(1);
			    	    cont_no=rset.getString(2);
			    	    cont_type = rset.getString(4);
			    	    cd=rset.getString(3);
			    	    ent_dt=rset.getString(5);
			    	    ent_by=rset.getString(6);
			    	    cont_rev=rset.getString(7);
			    		
			    	    sn_no=cont_ref_no.split("-")[1];
			    	    sn_rev=cont_ref_no.split("-")[5];
			    	    
			    		if(sn_no.equals("0") && sn_rev.equals("0"))
			    		{
			    			
			    			queryString1 = "INSERT INTO FMS_TRADER_CONT_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,"
			    					+ "CONT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT,CONTRACT_TYPE)"
			    					+ "VALUES(?,?,?,?,?,?,'1','100',?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?) ";
					    	stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, company_cd);
					    	stmt1.setString(2, cd );
					    	stmt1.setString(3, sn_no);
					    	stmt1.setString(4, sn_rev);
					    	stmt1.setString(5, cont_no);
					    	stmt1.setString(6, cont_rev);
					    	stmt1.setString(7, ent_by);
					    	stmt1.setString(8, ent_dt);
				    	stmt1.setString(9, cont_type);
				    	
				    	stmt1.executeUpdate();
				    	stmt1.close();
//				    	logger_count++;
				    	logger.data(fname,(company_cd+","+cd+", "+sn_no+", "+sn_rev+","+cont_no+", "+cont_type+","+ent_dt+","+ent_by+","+"Cargo without contract in FMS ,"), conn, "");
		    		}
		    	}
		    	rset.close();
		    	stmt.close();

				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count+",,,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count+",,,,,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,,"), conn); 
						
			logger.checkpoint(fname, "<<END>><<FMS_TRADER_CONT_PLANT>>,,,,,,,,", conn);
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
	public void FMS_BUY_DAILY_SELLER_NOM() throws IOException, SQLException {

		function_nm="FMS_BUY_DAILY_SELLER_NOM()";
		try {
			table_name = "FMS_BUY_DAILY_SELLER_NOM";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BUY_DAILY_SELLER_NOM>>,,,,,," , conn);
				
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			queryString1= "INSERT INTO FMS_BUY_DAILY_SELLER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD"
					+ ",TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO ) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,"
					+ "?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_DAILY_SELLER_NOM_"+start_end_dt+".csv");
			if(file1.exists()) {
	
				String fileName = migration_setup_dir + "EXPORT/FMS_BUY_DAILY_SELLER_NOM_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				

				String line = br.readLine();
					
//				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP", conn);
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,TRANS_CD,TRANS_SEQ,BU_SEQ,GAS_DT,NOM_REV_NO,CARGO_NO,CONT_REF_NO,TIMESTAMP", conn);
				
				while ((line = br.readLine()) != null) {  
					total_count++;
					String  cont_type = "",dom_buy_flag="",cont_ref_no="",cargo_no="",trade_ref_no="";
					String cont_no ="", cont_rev="" ,agmt_rev_no="", agmt_no = "", plant_seq="" , trans_cd="", trans_seq="", bu_seq="",nom_rev_no="",gas_dt="";
					cd = "";
					data = null;
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);

					for (int i = 0; i < line.split(",").length; i++)
				    {
//						cell = cellIterator.next();
						data = null;
				    	
						if (i == 0) {	// Counterparty_Abbr, Company Code 
							cont_ref_no = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
				    		if (cont_ref_no!=null) {
//								cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
								cont_type = cont_ref_no.split("-")[3];
					    		abbr = cont_ref_no.split("-")[0];
							}
				    		else
				    		{
				    			cont_type=null;
				    			abbr=null;
				    		}
							trade_ref_no = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
//				    		if (trade_ref_no!=null) {
//								trade_ref_no = trade_ref_no.substring(1, trade_ref_no.length() - 1);
//							}
							
				    		data = company_cd;
				    	}

						else if (i == 1 && cont_type!=null) {	// COUNTERPARTY_CD
				    		if(cont_type.equals("I")) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ?  ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cont_ref_no);
								stmt.setString(2, company_cd);
								rset = stmt.executeQuery();
								while (rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								
								
					    		data = cd;
				    		}
				    		else if(cont_type.equals("D") || cont_type.equals("T") ) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ?";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, trade_ref_no);
								stmt.setString(2, company_cd);
								
								rset = stmt.executeQuery();
								while (rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								
								
					    		data = cd;
				    		}
				    	}
				    	else if(i == 2) { //AGMT_NO
				    		data=agmt_no+"";
				    	
				    	}
				    		
				    	else if(i == 3) { //AGMT_REV_NO
				    					    		
				    		data=agmt_rev_no+"";
		
				    	}	    		
				    	else if(i == 4) { //CONT_NO
				    	
				    		data=cont_no+"";
				    	}
				    	else if(i == 5 ) { //CONT_REV
				    		
				    		data=cont_rev+"";
				    	}
				    	else if(i == 12) { //DOM_BUY_FLAG , CONTRACT_TYPE
				    		
				    		data=dom_buy_flag+"";

				    	}


		
				    	else {
//				    			
				    		if (i ==11) {	
					    			
					    		gas_dt=line.split(",")[i];
//					    		gas_dt = gas_dt.substring(1, gas_dt.length()-1);		    			
					    	}

				    		if (i ==8) {	
						    			
						    	trans_cd=line.split(",")[i];
//						    	trans_cd = trans_cd.substring(1, trans_cd.length()-1);
						    }
				    			 
				    		if (i ==9) {	
						    			
						    	trans_seq=line.split(",")[i];
//						    	trans_seq = trans_seq.substring(1, trans_seq.length()-1);
						    			
						    }
				    		if (i ==6) {	
						    			
						    	nom_rev_no=line.split(",")[i];
//						    	nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length()-1);
						    			
						    }
				    		
				    		if(i == 7) { // PLANT_SEQ_NO
				    			
				    			plant_seq=line.split(",")[i];
//				    			plant_seq = plant_seq.substring(1, plant_seq.length()-1);
						    
				    		}
				    		
				    		
				    		if(i == 10) { // BU_SEQ_NO
				    			
				    			bu_seq=line.split(",")[i];
//				    			bu_seq = bu_seq.substring(1, bu_seq.length()-1);
						    
				    		}
				    			 
				    		if (i ==22) {	
						    			
						    	cargo_no=line.split(",")[i];
//						    	cargo_no = cargo_no.substring(1, cargo_no.length()-1);

						    }
					    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
				    	}
				    	
						
				    	stmt1.setString(index++, data);
				    }

			    	queryString = "SELECT QTY_MMBTU FROM FMS_BUY_DAILY_SELLER_NOM WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
			    			+ " AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND TRANSPORTER_CD=? AND "
			    			+ "TRANS_SEQ=? AND BU_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND NOM_REV_NO = ?    AND CARGO_NO=?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev_no);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, dom_buy_flag );
			    	stmt.setString(8, plant_seq );
			    	stmt.setString(9, trans_cd );
			    	stmt.setString(10, trans_seq);
			    	stmt.setString(11, bu_seq );
			    	stmt.setString(12, gas_dt );
			    	stmt.setString(13, nom_rev_no );
			    	stmt.setString(14, cargo_no );


			    	
			    	rset = stmt.executeQuery();
			    				    	
				    if (!rset.next() && !cont_no.equals("") && cd!=null &&  !plant_seq.equals("") && !bu_seq.equals("")) {
				    	
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+nom_rev_no+","+cargo_no+","+cont_ref_no+","), conn,"");
				    	stmt1.executeUpdate();

				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	
				    	skipped_count++;
				    	stmt1.close();
				    	
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+nom_rev_no+","+cargo_no+","+cont_ref_no+","), conn,"E");
				    }				    
				    rset.close();
				    stmt.close();			
				}
				br.close();
			}

				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count+",,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, "<<END>><<FMS_BUY_DAILY_SELLER_NOM>>", conn);
			
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
	
	
	public void FMS_BUY_DAILY_ALLOCATION() throws IOException, SQLException {

		function_nm="FMS_BUY_DAILY_ALLOCATION()";
		try {
			table_name = "FMS_BUY_DAILY_ALLOCATION";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BUY_DAILY_ALLOCATION>>,,,,,," , conn);
				
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			queryString1= "INSERT INTO FMS_BUY_DAILY_ALLOCATION(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD"
					+ ",TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO ) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,"
					+ "?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_DAILY_ALLOCATION_"+start_end_dt+".csv");
			if(file1.exists()) {

				String fileName = migration_setup_dir + "EXPORT/FMS_BUY_DAILY_ALLOCATION_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

				String line = br.readLine();
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,TRANS_CD,TRANS_SEQ,BU_SEQ,GAS_DT,NOM_REV_NO,CARGO_NO,CONT_REF_NO,TIMESTAMP", conn);
				
				while ((line = br.readLine()) != null) {  
					total_count++;
					String  cont_type = "",dom_buy_flag="",cont_ref_no="",cargo_no="",trade_ref_no="";
					String cont_no ="", cont_rev="" ,agmt_rev_no="", agmt_no = "", plant_seq="" , trans_cd="", trans_seq="", bu_seq="",nom_rev_no="",gas_dt="";
					cd = "";
					data = null;

					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
				    

				    for (int i = 0; i < line.split(",").length; i++)
				    {
//						cell = cellIterator.next();
						data = null;
				    	
						if (i == 0) {	// Counterparty_Abbr, Company Code 
							cont_ref_no = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
				    		if (cont_ref_no!=null) {
//								cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
								cont_type = cont_ref_no.split("-")[3];
					    		abbr = cont_ref_no.split("-")[0];
							}
				    		else
				    		{
				    			cont_type=null;
				    			abbr=null;
				    		}
							trade_ref_no = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);

							
				    		data = company_cd;
				    	}

						else if (i == 1 && cont_type!=null) {	// COUNTERPARTY_CD
				    		if(cont_type.equals("I")) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cont_ref_no);
								stmt.setString(2, company_cd);
								
								rset = stmt.executeQuery();
								while (rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								
								
					    		data = cd;
				    		}
				    		else if(cont_type.equals("D") || cont_type.equals("T") ) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ?";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, trade_ref_no);
								stmt.setString(2, company_cd);
								
								rset = stmt.executeQuery();
								while (rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								
								
					    		data = cd;
				    		}
				    	}
				    	else if(i == 2) { //AGMT_NO
				    		data=agmt_no+"";
				    	
				    	}
				    		
				    	else if(i == 3) { //AGMT_REV_NO
				    					    		
				    		data=agmt_rev_no+"";
		
				    	}	    		
				    	else if(i == 4) { //CONT_NO
				    	
				    		data=cont_no+"";
				    	}
				    	else if(i == 5 ) { //CONT_REV
				    		
				    		data=cont_rev+"";
				    	}
				    	else if(i == 12) { //DOM_BUY_FLAG , CONTRACT_TYPE
				    		
				    		data=dom_buy_flag+"";

				    	}

		
				    	else {
//				    			
				    		if (i ==11) {	
					    			
					    		gas_dt=line.split(",")[i];
//					    		gas_dt = gas_dt.substring(1, gas_dt.length()-1);		    			
					    	}

				    		if (i ==8) {	
						    			
						    	trans_cd=line.split(",")[i];
//						    	trans_cd = trans_cd.substring(1, trans_cd.length()-1);
						    }
				    			 
				    		if (i ==9) {	
						    			
						    	trans_seq=line.split(",")[i];
//						    	trans_seq = trans_seq.substring(1, trans_seq.length()-1);
						    			
						    }
				    		if (i ==6) {	
						    			
						    	nom_rev_no=line.split(",")[i];
//						    	nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length()-1);
						    			
						    }
				    		
				    		if(i == 7) { // PLANT_SEQ_NO
				    			
				    			plant_seq=line.split(",")[i];
//				    			plant_seq = plant_seq.substring(1, plant_seq.length()-1);
						    
				    		}
				    		
				    		
				    		if(i == 10) { // BU_SEQ_NO
				    			
				    			bu_seq=line.split(",")[i];
//				    			bu_seq = bu_seq.substring(1, bu_seq.length()-1);
						    
				    		}
				    			 
				    		if (i ==22) {	
						    			
						    	cargo_no=line.split(",")[i];
//						    	cargo_no = cargo_no.substring(1, cargo_no.length()-1);

						    }
					    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
				    	}
				    	
						
				    	stmt1.setString(index++, data);
				    }

			    	queryString = "SELECT QTY_MMBTU FROM FMS_BUY_DAILY_ALLOCATION WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
			    			+ " AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND TRANSPORTER_CD=? AND "
			    			+ "TRANS_SEQ=? AND BU_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND NOM_REV_NO = ?    AND CARGO_NO=?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev_no);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, dom_buy_flag );
			    	stmt.setString(8, plant_seq );
			    	stmt.setString(9, trans_cd );
			    	stmt.setString(10, trans_seq);
			    	stmt.setString(11, bu_seq );
			    	stmt.setString(12, gas_dt );
			    	stmt.setString(13, nom_rev_no );
			    	stmt.setString(14, cargo_no );


			    	
			    	rset = stmt.executeQuery();
			    				    	
				    if (!rset.next() && !cont_no.equals("") && cd!=null &&  !plant_seq.equals("") && !bu_seq.equals("")) {
				    	
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+nom_rev_no+","+cargo_no+","+cont_ref_no+","), conn,"");
				    	stmt1.executeUpdate();

				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	
				    	skipped_count++;
				    	stmt1.close();
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+plant_seq+","+trans_cd+","+trans_seq+","+bu_seq+","+gas_dt+","+nom_rev_no+","+cargo_no+","+cont_ref_no+","), conn,"E");
				    }				    
				    rset.close();
				    stmt.close();			
				}
				br.close();
				}

				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count+",,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, "<<END>><<FMS_BUY_DAILY_ALLOCATION>>", conn);
			
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
	
	
	public void FMS_BUY_DAILY_ALLOCATION_MM() throws IOException, SQLException {

		function_nm="FMS_BUY_DAILY_ALLOCATION_MM()";
		try {
			table_name = "FMS_BUY_DAILY_ALLOCATION_MM";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_BUY_DAILY_ALLOCATION_MM>>,,,,,," , conn);
				
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			queryString1= "INSERT INTO FMS_BUY_DAILY_ALLOCATION_MM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD"
					+ ",TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,SEQ_NO,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,EMAIL_SENT,CARGO_NO,DTL_CATEGORY,MOLECULE_MAP ) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,"
					+ "?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BUY_DAILY_ALLOCATION_MM_"+start_end_dt+".csv");
			if(file1.exists()) {

				String fileName = migration_setup_dir + "EXPORT/FMS_BUY_DAILY_ALLOCATION_MM_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

				String line = br.readLine();
				
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP", conn);
				
				while ((line = br.readLine()) != null) {  
					total_count++;
					String  cont_type = "",dom_buy_flag="",cont_ref_no="",cargo_no="",trade_ref_no="";
					String cont_no ="", cont_rev="" ,agmt_rev_no="", agmt_no = "", plant_seq="" , trans_cd="", trans_seq="", bu_seq="",nom_rev_no="",gas_dt="";
					cd = "";
					data = null;
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);

					for (int i = 0; i < line.split(",").length; i++)
				    {
//						cell = cellIterator.next();
						data = null;
				    	
						if (i == 0) {	// Counterparty_Abbr, Company Code 
							cont_ref_no = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
				    		if (cont_ref_no!=null) {
//								cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
								cont_type = cont_ref_no.split("-")[3];
					    		abbr = cont_ref_no.split("-")[0];
							}
				    		else
				    		{
				    			cont_type=null;
				    			abbr=null;
				    		}
							trade_ref_no = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
//				    		if (trade_ref_no!=null) {
//								trade_ref_no = trade_ref_no.substring(1, trade_ref_no.length() - 1);
//							}
							
				    		data = company_cd;
				    	}

						else if (i == 1 && cont_type!=null) {	// COUNTERPARTY_CD
				    		if(cont_type.equals("I")) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cont_ref_no);
								stmt.setString(2, company_cd);
								
								rset = stmt.executeQuery();
								while (rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								
								
					    		data = cd;
				    		}
				    		else if(cont_type.equals("D") || cont_type.equals("T") ) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE "
					    				+ "FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ?";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, trade_ref_no);
								stmt.setString(2, company_cd);
								
								rset = stmt.executeQuery();
								while (rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag = rset.getString(6);
					    		}
								rset.close();
					    		stmt.close();
								
								
					    		data = cd;
				    		}
				    	}
				    	else if(i == 2) { //AGMT_NO
				    		data=agmt_no+"";
				    	
				    	}
				    		
				    	else if(i == 3) { //AGMT_REV_NO
				    					    		
				    		data=agmt_rev_no+"";
		
				    	}	    		
				    	else if(i == 4) { //CONT_NO
				    	
				    		data=cont_no+"";
				    	}
				    	else if(i == 5 ) { //CONT_REV
				    		
				    		data=cont_rev+"";
				    	}
				    	else if(i == 12) { //DOM_BUY_FLAG , CONTRACT_TYPE
				    		
				    		data=dom_buy_flag+"";

				    	}

				    	else if(i == 26) { // MOLECULE_MAP

					    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
				    		
					    	queryString="SELECT PROD_CD, MOLE_CD FROM FMS_PRODUCT_MOLECULE_MST WHERE ENT_PROFILE = ? AND MOLE_ABBR = ? ";
				    		stmt=conn.prepareStatement(queryString);
				    		stmt.setString(1, company_cd);
				    		stmt.setString(2, data);
				    		rset=stmt.executeQuery();
				    		if (rset.next()) {
				    			data=rset.getString(1)+"-"+rset.getString(2);
				    		}
				    		else
				    		{
				    			data=null;
				    		}
				    		
				    		stmt.close();
				    		rset.close();
				    	}
		
				    	else {
				    			
				    		if (i ==11) {	
					    			
					    		gas_dt=line.split(",")[i];		    			
					    	}

				    		if (i ==8) {	
						    			
						    	trans_cd=line.split(",")[i];
						    }
				    			 
				    		if (i ==9) {	
						    			
						    	trans_seq=line.split(",")[i];
						    			
						    }
				    		if (i ==6) {	
						    			
						    	nom_rev_no=line.split(",")[i];
						    			
						    }
				    		
				    		if(i == 7) { // PLANT_SEQ_NO
				    			
				    			plant_seq=line.split(",")[i];
						    
				    		}
				    		
				    		
				    		if(i == 10) { // BU_SEQ_NO
				    			
				    			bu_seq=line.split(",")[i];
						    
				    		}
				    			 
				    		if (i == 24) {	
						    			
						    	cargo_no=line.split(",")[i];

						    }
					    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
				    	}
				    	
						
				    	stmt1.setString(index++, data);
				    }

			    	queryString = "SELECT QTY_MMBTU FROM FMS_BUY_DAILY_ALLOCATION_MM WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
			    			+ " AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND TRANSPORTER_CD=? AND "
			    			+ "TRANS_SEQ=? AND BU_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND NOM_REV_NO = ?    AND CARGO_NO=?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev_no);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, dom_buy_flag );
			    	stmt.setString(8, plant_seq );
			    	stmt.setString(9, trans_cd );
			    	stmt.setString(10, trans_seq);
			    	stmt.setString(11, bu_seq );
			    	stmt.setString(12, gas_dt );
			    	stmt.setString(13, nom_rev_no );
			    	stmt.setString(14, cargo_no );


			    	
			    	rset = stmt.executeQuery();
			    				    	
				    if (!rset.next() && !cont_no.equals("") && cd!=null &&  !plant_seq.equals("") && !bu_seq.equals("")) {
				    	
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","), conn,"");
				    	stmt1.executeUpdate();

				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","), conn, "E");
				    }				    
				    rset.close();
				    stmt.close();			
				}
				br.close();
				}
				

				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";
				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count+",,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, "<<END>><<FMS_BUY_DAILY_ALLOCATION_MM>>", conn);
			
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
	
	public void FMS_TRADER_CONT_SPLIT_PLANT() throws IOException, SQLException {

		function_nm="FMS_TRADER_CONT_SPLIT_PLANT()";
		try {
			table_name = "FMS_TRADER_CONT_SPLIT_PLANT";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_TRADER_CONT_SPLIT_PLANT>>,,,,,," , conn);
				
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			queryString1= "INSERT INTO FMS_TRADER_CONT_SPLIT_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_SPLIT_PLANT_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_SPLIT_PLANT_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {  
					total_count++;
					String  cont_type = "",dom_buy_flag="",cont_ref_no="",trade_ref_no="",sp_trd_cd="",sp_trd_abbr="";
					String cont_no ="", cont_rev="" ,agmt_rev_no="", agmt_no = "", plant_seq="" ;
					cd = "0";
					data = null;
//					int m=1;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
				    
					row = rowIterator.next();
				    cellIterator = row.cellIterator();
				    while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						data = null;
				    	
						if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 
							cont_ref_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (cont_ref_no!=null) {
								cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
								abbr = cont_ref_no.split("-")[0];
					    		cont_type= cont_ref_no.split("-")[3];
							}
				    		else
				    		{
				    			abbr = null;
				    			cont_type= null;
				    			
				    		}
							trade_ref_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (trade_ref_no!=null) {
								trade_ref_no = trade_ref_no.substring(1, trade_ref_no.length() - 1);
							}
							
				    		
				    		
				    		data = company_cd;
				    	}
						else if (cell.getColumnIndex() == 1 && cont_type!=null) {	// Counterparty_Cd
								
								if(cont_type.equals("I")) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ?  AND COMPANY_CD = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cont_ref_no);
					    		stmt.setString(2, company_cd);
					    		
					    		rset = stmt.executeQuery();
					    		while(rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag= rset.getString(6);
					    			
					    		}
					    		rset.close();
					    		stmt.close();				    		
					    		data=cd;
								}
								else if(cont_type.equals("D") || cont_type.equals("T")) {
						    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, trade_ref_no);
						    		stmt.setString(2, company_cd);
						    		rset = stmt.executeQuery();
						    		while(rset.next()) {
						    			cd = rset.getString(1);
						    			agmt_no = rset.getString(2);
						    			agmt_rev_no = rset.getString(3);
						    			cont_no = rset.getString(4);
						    			cont_rev = rset.getString(5);
						    			dom_buy_flag= rset.getString(6);
						    			
						    		}
						    		rset.close();
						    		stmt.close();				    		
						    		data=cd;
								}
					    	}
				    	else if(cell.getColumnIndex() == 2) { //AGMT_NO
				    		data=agmt_no+"";
				    	}
				    	
				    	else if(cell.getColumnIndex() == 3) { //AGMT_REV_NO
				    		data=agmt_rev_no+"";
				    	}
				    	else if(cell.getColumnIndex() == 4) {  //CONT_NO
				    		
				    		data=cont_no+"";
			    		}
				    	else if(cell.getColumnIndex() == 5) {  //CONT_REV
				    		
				    		data=cont_rev+"";
				    	}
				    	else if(cell.getColumnIndex() == 6) {  //CONTRACT_TYPE
				    		
				    		data=dom_buy_flag+"";

				    	}
				    	else if(cell.getColumnIndex() == 7) {  //CONTRACT_TYPE
				    		

				    		sp_trd_abbr =  (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if(sp_trd_abbr!=null)
				    		{
				    			sp_trd_abbr = sp_trd_abbr.substring(1, sp_trd_abbr.length()-1);
				    		}
				    		
				    		queryString= "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1,sp_trd_abbr);
				    		rset = stmt.executeQuery();
				    		while(rset.next()) {
				    		sp_trd_cd= rset.getString(1);	
				    	    }
				    		if(sp_trd_cd.equals("")) {
				    			sp_trd_cd=null;
				    			data=sp_trd_cd;
				    		}
				    		else if(sp_trd_cd.equals("null")) {
				    			sp_trd_cd=null;
				    			data=sp_trd_cd;
				    		}
				    		rset.close();
				    		stmt.close();				    		
				    		data=sp_trd_cd+"";
									    
				    	}	
				    	else {
				    		
				    		if(cell.getColumnIndex()==8) {
					    		plant_seq=(cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    		if (plant_seq!=null ) {
									plant_seq = plant_seq.substring(1, plant_seq.length() - 1);
								}
				    		}
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	
//						System.out.println(index+"===>"+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT ENT_BY FROM FMS_TRADER_CONT_SPLIT_PLANT WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
			    			+ " AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? AND SPLIT_TRADER_CD = ? AND PLANT_SEQ_NO=?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev_no);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, dom_buy_flag );
			    	stmt.setString(8, sp_trd_cd );
			    	stmt.setString(9, plant_seq );
//			    	
			    	
			    	
			    	rset = stmt.executeQuery();
			    				    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("") && cd!=null && sp_trd_cd!=null) {
				    	
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","), conn,"");
				    	stmt1.executeUpdate();

				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count+",,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, "<<END>><<FMS_TRADER_CONT_SPLIT_PLANT>>", conn);
			
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
	
	
	public void FMS_TRADER_CONT_PLANT_CHRG() throws IOException, SQLException {

		function_nm="FMS_TRADER_CONT_PLANT_CHRG()";
		try {
			table_name = "FMS_TRADER_CONT_PLANT_CHRG";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_TRADER_CONT_PLANT_CHRG>>,,,,,," , conn);
				
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;
			
			queryString1= "INSERT INTO FMS_TRADER_CONT_PLANT_CHRG(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "PLANT_SEQ_NO,EFF_DT,CHARGE_ABBR,CHARGE_RATE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT)"
					+ " VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_PLANT_CHRG_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_TRADER_CONT_PLANT_CHRG_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {  
					total_count++;
					String  cont_type = "",dom_buy_flag="",cont_ref_no="",trade_ref_no="",sp_trd_cd="",chrg_abbr="",chrg_nm="",eff_dt="";
					String cont_no ="", cont_rev="" ,agmt_rev_no="", agmt_no = "", plant_seq="" ;
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
							cont_ref_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (cont_ref_no!=null) {
								cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
								abbr = cont_ref_no.split("-")[0];
					    		cont_type= cont_ref_no.split("-")[3];
							}
				    		else
				    		{
				    			abbr = null;
					    		cont_type= null;
				    		}
							trade_ref_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (trade_ref_no!=null) {
								trade_ref_no = trade_ref_no.substring(1, trade_ref_no.length() - 1);
							}
							
				    		
				    		
				    		data = company_cd;
				    	}
						else if (cell.getColumnIndex() == 1 && cont_type!=null) {	// Counterparty_Cd
								
								if(cont_type.equals("I")) {
					    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ?  AND COMPANY_CD= ?";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cont_ref_no);
					    		stmt.setString(2, company_cd);
					    		
					    		rset = stmt.executeQuery();
					    		while(rset.next()) {
					    			cd = rset.getString(1);
					    			agmt_no = rset.getString(2);
					    			agmt_rev_no = rset.getString(3);
					    			cont_no = rset.getString(4);
					    			cont_rev = rset.getString(5);
					    			dom_buy_flag= rset.getString(6);
					    			
					    		}
					    		rset.close();
					    		stmt.close();				    		
					    		data=cd;
								}
								else if(cont_type.equals("D") || cont_type.equals("T") ) {
						    		queryString = "SELECT COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD= ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, trade_ref_no);
						    		stmt.setString(2, company_cd);
						    		
						    		rset = stmt.executeQuery();
						    		while(rset.next()) {
						    			cd = rset.getString(1);
						    			agmt_no = rset.getString(2);
						    			agmt_rev_no = rset.getString(3);
						    			cont_no = rset.getString(4);
						    			cont_rev = rset.getString(5);
						    			dom_buy_flag= rset.getString(6);
						    			
						    		}
						    		rset.close();
						    		stmt.close();				    		
						    		data=cd;
								}
					    	}
				    	else if(cell.getColumnIndex() == 2) { //AGMT_NO
				    		data=agmt_no+"";
				    	}
				    	
				    	else if(cell.getColumnIndex() == 3) { //AGMT_REV_NO
				    		data=agmt_rev_no+"";
				    	}
				    	else if(cell.getColumnIndex() == 4) {  //CONT_NO
				    		
				    		data=cont_no+"";
			    		}
				    	else if(cell.getColumnIndex() == 5) {  //CONT_REV
				    		
				    		data=cont_rev+"";
				    	}
				    	else if(cell.getColumnIndex() == 6) {  //CONTRACT_TYPE
				    		
				    		data=dom_buy_flag+"";

				    	}
				    	else if(cell.getColumnIndex() == 9) {  //CONTRACT_TYPE
				    		chrg_nm=(cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (chrg_nm!=null) {
								chrg_nm = chrg_nm.substring(1, chrg_nm.length() - 1);
							}
				    		
							queryString="SELECT CHARGE_ABBR FROM FMS_CHARGE_MST WHERE CHARGE_NAME = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1,chrg_nm );
				    		rset = stmt.executeQuery();
				    		while(rset.next()) {
				    			chrg_abbr=rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		data=chrg_abbr;
				    		}
						
				    	
				    	else {
				    		
				    		if(cell.getColumnIndex()==7) {
					    		plant_seq=(cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    		if (plant_seq!=null) {
									plant_seq = plant_seq.substring(1, plant_seq.length() - 1);
								}
				    		}
				    		if(cell.getColumnIndex()==8) {
				    			
					    		eff_dt=(cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    		if (eff_dt!=null) {
									eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
								}
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	
//						System.out.println(index+"===>"+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT ENT_BY FROM FMS_TRADER_CONT_PLANT_CHRG WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=?"
			    			+ " AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND"
			    			+ " EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND CHARGE_ABBR=?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev_no);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, dom_buy_flag );
			    	stmt.setString(8, plant_seq );
			    	stmt.setString(9, eff_dt );
			    	stmt.setString(10,chrg_abbr );

//			    	
			    	
			    	
			    	rset = stmt.executeQuery();
			    				    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("") && cd!=null && sp_trd_cd!=null &&  !chrg_abbr.equals("")) {
				    	
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","), conn,"");
				    	stmt1.executeUpdate();

				    	stmt1.close();
				    	
				    	logger_count++;
				    }else {
				    	stmt1.close();
				    	skipped_count++;
				    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count+",,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,"), conn); 
						
			logger.checkpoint(fname, "<<END>><<FMS_TRADER_CONT_PLANT_CHRG>>", conn);
			
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
	public void FMS_PUR_SG_INV_MST() throws IOException, SQLException {

		function_nm="FMS_PUR_SG_INV_MST()";
		try {
			table_name = "FMS_PUR_SG_INV_MST";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_PUR_SG_INV_MST>>,,,,,,,,,,,,,,,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;

			queryString1="INSERT INTO FMS_PUR_SG_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PERIOD_START_DT,PERIOD_END_DT,FINANCIAL_YEAR,"//7
					+ "BU_UNIT,BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,DUE_DT,ALLOC_QTY, "//17
					+ "SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN, "//30
					+ "ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT, "
					+ "TXN_CHARGE,TXN_AMOUNT,TAX_TXN_AMT,TAX_TXN_CD,TAX_TXN_EFF_DT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,TCS_CERT_FLAG,TCS_AMT,TCS_FACTOR, "
					+ "TCS_STRUCT_CD,TCS_EFF_DT,SAP_EXCHNG_RATE,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,TCS_TDS,SYS_INV_NO,PAY_INSERT_BY,PAY_INSERT_DT,PAY_RECV_AMT,PAY_RECV_DT,PAY_REMARK,PAY_UPDATE_BY, "
					+ "PAY_UPDATE_DT,CARGO_NO,BOE_NO,INV_FLAG,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,SUG_QTY,SUG_PERCENT,QTY_UNIT,"
					+ "TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,OTHER_CHARGES,MARKET_MARGIN_AMT,OTHER_CHARGES_AMT,FIN_SYS)"
					+ "VALUES(?,?,?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			Map<String, Integer> invseq = new HashMap<String, Integer>();
			file1 = new File(migration_setup_dir + "EXPORT/FMS_PUR_SG_INV_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_PUR_SG_INV_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,"
						+ " PLANT_SEQ, BU_UNIT, FREQ, PERIOD_START_DT, PERIOD_END_DT, INVOICE_SEQ, FINANCIAL_YEAR, INV_FLAG,CONTACT_PERSON_CD,"
						+ "BU_CONTACT_PERSON_CD,INVOICE_NO,SALE_PRICE,SYS_INVOICE_NO,CARGO_NO,CARGO_REF_NO,AGMT_TYPE,BOE_NO,DUE_DATE,TIMESTAMP", conn);
				int inv_seq_no=1;

				while (rowIterator.hasNext()) {
				
					
					String agmt_no="",agmt_rev="",cont_no="",cont_rev="",contract_type="",cargo_no="",bu_seq="",boe_no="",bu_contact_person_cd="",agmt_type="",contact_person_cd="",inv_no="",tax_amt="",invoice_amt1="",tax_str_eff_dt="",tax_str_cd="";
					String exp_from_dt="",exp_to_dt="",allo_qty="",sale_price="",financial_year="",fin_yr="",inv_flag="",freq="",sys_invoice_no="",due_date="",exchange_rate_value="",invoice_amt="",period_start_dt="",period_end_dt="";
					String sap_app="",sap_by="",sap_dt="",pay_recv_amt="",net_pay_cf="";
					double sale_amt = 0 ;
					double gross1=0;
					double profm_inv_amt=0;
					String cd_paid_amt="";
					int boe_no_count=1;
					abbr = "";
					cd = "0";
					data = null;
					index = 1;
					row = rowIterator.next();
					{	
							
							stmt1 = conn.prepareStatement(queryString1);
							abbr = "";
							cd = "0";
							data = null;
							index = 1;
							
						    cellIterator = row.cellIterator();
						    while (cellIterator.hasNext()) {
								cell = cellIterator.next();
								data = null;
								
						    	if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 
						    		cargo_ref_no = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
						    		
						    		if (cargo_ref_no!=null) {
										cargo_ref_no = cargo_ref_no.substring(1, cargo_ref_no.length() - 1);
										inv_flag = cargo_ref_no.split("-")[1];
										cargo_ref_no = cargo_ref_no.split("-")[0];
									}
						    		else {
						    			inv_flag=null;
						    		}
						    		
									data = company_cd;
						    	}
						    	else if (cell.getColumnIndex() == 1) {	//Counterparty_Cd
						    		
						    		queryString = "SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,AGMT_TYPE "
						    				+ "FROM FMS_TRADER_CARGO_MST WHERE  CARGO_REF= ? AND COMPANY_CD = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cargo_ref_no);
						    		stmt.setString(2, company_cd);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			cd=rset.getString(1);
						    			agmt_no=rset.getString(2);
						    			agmt_rev=rset.getString(3);
						    			cont_no=rset.getString(4);
						    			cont_rev=rset.getString(5);
						    			contract_type=rset.getString(6);
						    			cargo_no=rset.getString(7);
						    			agmt_type=rset.getString(8);
						    			
						    		}
						    		else
						    		{
						    			cd=null;
						    			agmt_no=null;
						    			agmt_rev=null;
						    			cont_no=null;
						    			cont_rev=null;
						    			contract_type=null;
						    			cargo_no=null;
						    			agmt_type=null;
						    		}
						    		rset.close();
						    		stmt.close();
						    		data=cd;

						    	}
						    	
						    	else if(cell.getColumnIndex() == 2)//agmt_no
						    	{
						    		data=agmt_no;
						    	}
						    	else if(cell.getColumnIndex() == 3)//agmt_rev
						    	{
						    		data=agmt_rev;
						    	}
						    	else if(cell.getColumnIndex() == 4)//cont_no
						    	{
						    		data=cont_no;
						    	}
						    	else if(cell.getColumnIndex() == 5)//cont_rev
						    	{
						    		data=cont_rev;
						    	}
						    	else if(cell.getColumnIndex() == 6)//contract type
						    	{
						    		data=contract_type;
						    	}
						    	else if(cell.getColumnIndex()==7)
						    	{
						    		period_start_dt= (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
						    		if(period_start_dt != null) {
						    			period_start_dt = period_start_dt.substring(1, period_start_dt.length()-1);
							    	}
						    		data=period_start_dt;
						    		exp_from_dt=period_start_dt;
						    		
						    	}
						    	
						    	else if(cell.getColumnIndex()==8)
						    	{
						    		period_end_dt= (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
						    		if(period_end_dt != null) {
						    			period_end_dt = period_end_dt.substring(1, period_end_dt.length()-1);
							    	}
						    		data=period_end_dt;
						    		exp_to_dt=period_end_dt;
						    		
						    	}
						    	else if(cell.getColumnIndex() == 9)//financial year:
						    	{
		
						    		financial_year = (cell.getStringCellValue().contains("'null'") ? null: cell.getStringCellValue());
						    		if (financial_year!=null) {
										financial_year = financial_year.substring(1, financial_year.length() - 1);
									}
									data=financial_year;
									
									if(financial_year!=null && !financial_year.equals("") && !financial_year.equals("-"))
									{
										String[] temp = financial_year.split("-");
										fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());//fin_yr for sysinvoice_no
									}
						    	}
						    	else if(cell.getColumnIndex() == 10 && cd!=null)//BU_UNIT
						    	{

						    		boe_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    		if(boe_no != null) {
						    			boe_no = boe_no.substring(1, boe_no.length()-1);
							    	}
						    		
//						    		SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B WHERE C.COMPANY_CD=B.COMPANY_CD "
//											+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
//											+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO AND C.AGMT_TYPE=B.AGMT_TYPE
						    		queryString = "SELECT BU_SEQ,PLANT_SEQ "
						    				+ "FROM FMS_BUY_CARGO_NOM_BOE "
						    				+ "WHERE  COUNTERPARTY_CD = ? AND  AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE= ? AND AGMT_TYPE= ?  "
						    				+ "AND CARGO_NO = ?  AND BOE_NO = ? AND COMPANY_CD =?   AND  NOM_REV = ( SELECT MAX(NOM_REV)"
						    				+ "FROM FMS_BUY_CARGO_NOM_BOE  "
						    				+ "WHERE  COUNTERPARTY_CD = ? AND  AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE= ? AND AGMT_TYPE= ? "
						    				+ " AND BOE_NO = ? AND COMPANY_CD = ? AND CARGO_NO = ? )";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, contract_type);
						    		stmt.setString(7, agmt_type);
						    		stmt.setString(8, cargo_no);
						    		stmt.setString(9, boe_no);
						    		stmt.setString(10, company_cd);
						    		
						    		stmt.setString(11, cd);
						    		stmt.setString(12, agmt_no);
						    		stmt.setString(13, agmt_rev);
						    		stmt.setString(14, cont_no);
						    		stmt.setString(15, cont_rev);
						    		stmt.setString(16, contract_type);
						    		stmt.setString(17, agmt_type);
						    		stmt.setString(18, boe_no);
						    		stmt.setString(19, company_cd);
						    		stmt.setString(20, cargo_no);
						    		
						    	
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			bu_seq=rset.getString(1);
						    			plant_seq_no=rset.getString(2);
						    		}	
						    		else {
						    			bu_seq=null;
						    			plant_seq_no=null;
						    		}
						    		
						    		rset.close();
						    		stmt.close();
						    		data=bu_seq;
		
						    	}
						    	else if(cell.getColumnIndex() == 11 && bu_seq!=null )//bu_contact_person_cd
						    	{
							    	queryString="SELECT SEQ_NO "
											+ "FROM FMS_ENTITY_CONTACT_MST A "
											+ "WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' AND ENTITY='B' AND ADDR_FLAG=? "
											+ "AND RM_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y' ";
									stmt=conn.prepareStatement(queryString);
								
									if(bu_seq.equals("1")) 
									{
										stmt.setString(1, "P1");
									}
									else if(bu_seq.equals("2")) 
									{
										stmt.setString(1, "P2");
									}
									else
									{
										stmt.setString(1, "P2");
									}
									
									rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			bu_contact_person_cd=rset.getString(1);
						    		}	
						    		else {
						    			bu_contact_person_cd="1";
						    		}
						    		rset.close();
						    		stmt.close();
						    		
						    		data=bu_contact_person_cd;

						    	}
						    	
						    	else if(cell.getColumnIndex() == 12)//plant_seq_no
						    	{
						    		data=plant_seq_no;
						    	}
						    	else if(cell.getColumnIndex() == 13 )//contact_person_cd is zero for fms :as there is no contact person
						    	{
							    	queryString="SELECT SEQ_NO "
											+ "FROM FMS_ENTITY_CONTACT_MST A "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND ENTITY=? AND ADDR_FLAG=? AND RM_FLAG=? "
											+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
											+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
											+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
											+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
									stmt=conn.prepareStatement(queryString);
									stmt.setString(1, "2");
									stmt.setString(2, cd);
									stmt.setString(3, "T");
									stmt.setString(4, "P"+plant_seq_no);
									stmt.setString(5, "Y");
									stmt.setString(6, "Y");
									stmt.setString(7, "Y");
									
									rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			contact_person_cd=rset.getString(1);
						    		}
						    		else {
						    			contact_person_cd="1";
						    		}
						    		rset.close();
						    		stmt.close();

						    		
						    		data=contact_person_cd;
						    	}
						    	else if(cell.getColumnIndex() == 14 && cd!=null && contact_person_cd!=null  && plant_seq_no!=null && inv_flag!=null)//invoice_seq_no
						    	{
						    		if (invseq.containsKey(financial_year + inv_flag  )) {
						    			
						    			inv_seq_no=invseq.get(financial_year + inv_flag);
						    			inv_seq_no=inv_seq_no+1;
										invseq.put(financial_year + inv_flag ,inv_seq_no);
										
										
										
									} else {
										inv_seq_no=1;
										invseq.put(financial_year + inv_flag,inv_seq_no);
										
									}
						    		data=inv_seq_no+"";
						    		
						    	}
						    	else if(cell.getColumnIndex() == 18 && plant_seq_no!=null)//due date:
						    	{
						    		due_date = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
						    		if(due_date != null) {
						    			due_date = due_date.substring(1, due_date.length()-1);
							    	}
							    	data=due_date;
						    	}
						    	else if(cell.getColumnIndex() == 19)//ALLO_QTY
						    	{
						    		allo_qty = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
						    		if(allo_qty != null) {
						    			allo_qty = allo_qty.substring(1, allo_qty.length()-1);
							    	}
						    		data=allo_qty;
						    	}
						    	else if(cell.getColumnIndex() == 20)//SALE_PRICE
						    	{
						    		
						    			sale_price = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
						    			if(sale_price != null) {
						    				sale_price = sale_price.substring(1, sale_price.length()-1);
								    	}
						    			data=sale_price;
						    		
						    	}
						    	else if(cell.getColumnIndex() == 22 && sale_price != null && allo_qty != null )//SALE_AMT:SALEPRICE*ALL_QTY
						    	{
						    		
						    			
						    			 
						    			 if(inv_flag.equals("CP") || inv_flag.equals("CF")) // as exchange_rate_value for custom duty
						    			 {
						    				 data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
						    				 if(data != null) {
								    			data = data.substring(1, data.length()-1);
						    				 }
						    				 sale_amt=Double.parseDouble(data);
						    			 }
						    			 else
						    			 {
						    				 double price = Double.parseDouble(sale_price);
							    			 double qty = Double.parseDouble(allo_qty);
							    			 sale_amt = price * qty; // keep it as double
							    			 data=sale_amt+"";
						    			 }
						    		
//						    		 double price = Double.parseDouble(sale_price);
//					    			 double qty = Double.parseDouble(allo_qty);
//					    			 sale_amt = price * qty; // keep it as double
//					    			 data=sale_amt+"";
					    			
						    			
						    		
						    	}
						    	else if(cell.getColumnIndex() == 25)//exchange_rate_value
						    	{
						    		exchange_rate_value = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							    	if(exchange_rate_value != null) {
							    		exchange_rate_value = exchange_rate_value.substring(1, exchange_rate_value.length()-1);
							    	}
						    		data=exchange_rate_value;
						    	}
						    	else if (cell.getColumnIndex() == 26) {		//INVOICE_RAISED_IN
									
									
//						    		 ("COMPANY_CD", "COUNTERPARTY_CD", "AGMT_NO", "AGMT_REV", "CONT_NO", "CONT_REV", "CONTRACT_TYPE", "PLANT_SEQ_NO")
									queryString = "SELECT INVOICE_CUR_CD FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND "
											+ "AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, company_cd);
									stmt.setString(2, cd);
									stmt.setString(3, agmt_no);
									stmt.setString(4, agmt_rev);
									stmt.setString(5, cont_no);
									stmt.setString(6, cont_rev);
									stmt.setString(7, contract_type);
									stmt.setString(8, plant_seq_no);
									rset = stmt.executeQuery();
									if(rset.next()) {
										data = rset.getString(1);
									}
									else {
										data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
										if (data != null) {
											data = data.substring(1, data.length() - 1);
										}
									}
									rset.close();
									stmt.close();
									
									if(inv_flag.equals("CP") || inv_flag.equals("CF")) //for custom duty manually added '1' inv_raised in INR in Purchase Register 
						    		{
						    			 data=1+"";
						    		}
								}
						    	else if(cell.getColumnIndex() == 27 && inv_flag!=null )//Gross_amt :=sale_amt
						    	{
						    		if(inv_flag.equals("CF")  || inv_flag.equals("CP") )
						    		{
						    			 data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
					    				 if(data != null) {
							    			data = data.substring(1, data.length()-1);
					    				 }
					    				 gross1=Double.parseDouble(data);
					    				 
						    		}
						    		else
						    		{
						    		data=sale_amt+"";
						    		}
						    		
						    	}
						    	else if(cell.getColumnIndex() == 28 && inv_flag!=null)//tax_amt
						    	{
						    		if(inv_flag.equals("CP"))//FOR  CP =INVOICE_AMT
						    		{
						    			tax_amt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
								    	if(tax_amt != null) {
								    		tax_amt = tax_amt.substring(1, tax_amt.length()-1);
								    	}
							    		data=tax_amt;
						    		}
						    		else if(inv_flag.equals("CF"))//CF:
						    		{		

						    			tax_amt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
								    	if(tax_amt != null) {
								    		tax_amt = tax_amt.substring(1, tax_amt.length()-1);
								    	}
							    		
						    			data=tax_amt;
						    		}
						    		else if(inv_flag.equals("PF") || inv_flag.equals("P") || inv_flag.equals("F") )// NO TAX FOR PF,P 
						    		{
						    			data=null;
						    		}
						    	}
						    	
						    	else if(cell.getColumnIndex() == 29 && inv_flag!=null)
						    	{
						    		tax_str_eff_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							    	if(tax_str_eff_dt != null) {
							    		tax_str_eff_dt = tax_str_eff_dt.substring(1, tax_str_eff_dt.length()-1);
						    	    }
							    	if(inv_flag.equals("CP") || inv_flag.equals("CF"))
							    	{
							    		queryString="SELECT TAX_STRUCT_CD   "
						    					+ "FROM FMS_CUSTOM_TAX_STRUCT_DTL  "
						    					+ "WHERE EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
										stmt=conn.prepareStatement(queryString);
										stmt.setString(1, tax_str_eff_dt);
										
										rset = stmt.executeQuery();
										if (rset.next()) {
											
											tax_str_cd=rset.getString(1);
							    		}
										else
										{
											tax_str_cd=null;
										}
										
							    		rset.close();
							    		stmt.close();
							    		
							    		data=tax_str_cd;
							    		
							    		
							    		
//							    		FMS_TAX_STRUCTURE
							    		queryString="SELECT TO_CHAR(APP_DATE,'DD/MM/YYYY hh24:mi:ss')   "
						    					+ "FROM FMS_TAX_STRUCTURE  "
						    					+ "WHERE TAX_STR_CD=?";
										stmt=conn.prepareStatement(queryString);
										stmt.setString(1, tax_str_cd);
										
										rset = stmt.executeQuery();
										if (rset.next()) {
											
											tax_str_eff_dt=rset.getString(1);
							    		}
										else
										{
											tax_str_eff_dt=null;
										}
										
							    		rset.close();
							    		stmt.close();
							    		
							    	}
							    	else {
							    		data=null;
							    	}
							    	
						    	}
						    	else if(cell.getColumnIndex() == 30 && inv_flag!=null )
						    	{
						    		
							    	if(inv_flag.equals("CP") || inv_flag.equals("CF"))
							    	{
							    		data=tax_str_eff_dt;						    	
							    	}
							    	else {
							    		data=null;
							    	}	
							    	
						    	}
						    	else if(cell.getColumnIndex() == 31 && inv_flag!=null)// tax_amt
						    	{
							    		if(inv_flag.equals("CP"))//invoice_amt=Gross_amt 
							    		{
							    			invoice_amt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
									    	if(invoice_amt != null) {
									    		invoice_amt = invoice_amt.substring(1, invoice_amt.length()-1);
									    	}
									    	invoice_amt = tax_amt;
								    		data=invoice_amt;
							    		}
							    		else if(inv_flag.equals("CF"))
							    		{
							    			tax_amt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
									    	if(tax_amt != null) {
									    		
									    		tax_amt = tax_amt.substring(1, tax_amt.length()-1);
									    		data=tax_amt;
									    		
									    		queryString="SELECT INVOICE_AMT,NET_PAYABLE_AMT  "
								    					+ "FROM FMS_PUR_SG_INV_MST A "
								    					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? "
								    					+ "AND A.CONTRACT_TYPE=? AND A.CARGO_NO=? AND A.BOE_NO=? AND A.PDF_INV_DTL IS NOT NULL "
								    					+ "AND A.INV_FLAG='CP' ";
												stmt=conn.prepareStatement(queryString);
												stmt.setString(1, "2");
												stmt.setString(2, cd);
												stmt.setString(3, agmt_no);
												stmt.setString(4, cont_no);
												stmt.setString(5, contract_type);
												stmt.setString(6, cargo_no);
												stmt.setString(7, boe_no);
												
												rset = stmt.executeQuery();
												if (rset.next()) {
													net_pay_cf=rset.getString(2);
									    		}
												rset.close();
												stmt.close();
									    		
									    	}
								    	}
								    	else//FOR P,PF,F
								    	{
								    		
								    		data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
									    	if(data != null) {
									    		data = data.substring(1, data.length()-1);
								    	    }
//								    	data=sale_amt+"";
								    	}
						    	}
						    	else if(cell.getColumnIndex() == 34 && inv_flag!=null )//NET_PAYABLE_AMT=Gross_amt 
						    	{
						    		if(inv_flag.equals("CP"))
						    		{
							    		data=tax_amt;
						    		}
						    		else if(inv_flag.equals("CF"))
						    		{
						    			data= cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
								    	if(data != null) {
								    		data = data.substring(1, data.length()-1);
								    	}	
						    		}
						    		else {
						    			data=sale_amt+"";
						    		}
						    		
						    	}
						    	else if(cell.getColumnIndex() == 61 )//SAP_APPROVAL
						    	{
						    		sap_app= cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							    	if(sap_app != null) {
							    		sap_app = sap_app.substring(1, sap_app.length()-1);
							    	}
							    	data=sap_app;
						    		
						    	}
						    	else if(cell.getColumnIndex() == 62 )//SAP_APPROVED_BY
						    	{
						    		sap_by= cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							    	if(sap_by != null) {
							    		sap_by = sap_by.substring(1, sap_by.length()-1);
							    	}
							    	data=sap_by;
						    		
						    	}
						    	else if(cell.getColumnIndex() == 63 )//SAP_APPROVED_DT
						    	{
						    		sap_dt= cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							    	if(sap_dt != null) {
							    		sap_dt = sap_dt.substring(1, sap_dt.length()-1);
							    	}
							    	data=sap_dt;
						    	}
						    	else if(cell.getColumnIndex() == 69 && contract_type!=null && inv_flag!=null )//SYS_INV_NO-PURCHASE(TYPE N)
						    	{
						    		String invoice_prefix=utilBean.getInvoicePrefix(conn,"2");
						    		if(contract_type.equals("N") && !cargo_no.equals(""))
									{
										if(inv_flag.equals("P") || inv_flag.equals("PF"))
										{
											sys_invoice_no=invoice_prefix+""+inv_flag+""+contract_type+"S"+utilBean.PrePaddingZero((inv_seq_no+""), 4)+"/"+fin_yr;
										}
										else if(inv_flag.equals("F"))
										{
											sys_invoice_no=invoice_prefix+""+contract_type+"S"+utilBean.PrePaddingZero((inv_seq_no+""), 4)+"/"+fin_yr;
										}
										else if(inv_flag.equals("CF"))
										{
											sys_invoice_no=invoice_prefix+""+contract_type+"CD"+utilBean.PrePaddingZero((inv_seq_no+""), 4)+"/"+fin_yr;
										}
										else if(inv_flag.equals("CP"))
										{
											sys_invoice_no=invoice_prefix+"P"+contract_type+"CD"+utilBean.PrePaddingZero((inv_seq_no+""), 4)+"/"+fin_yr;
										}
									}
						    		data = sys_invoice_no;	
						    		
						    	}
						    	else if(cell.getColumnIndex() == 72 )//PAY_RECV_AMT
						    	{
						    		pay_recv_amt= cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							    	if(pay_recv_amt != null) {
							    		pay_recv_amt = pay_recv_amt.substring(1, pay_recv_amt.length()-1);
							    		data=pay_recv_amt;
							    	}
							    	else
							    	{
							    		data=pay_recv_amt;
							    	}
						    	}
						    	
						    	else if(cell.getColumnIndex() == 77)//cargo_no
						    	{
						    		data=cargo_no;
//						    		if(cargo_ref_no.equals("19028")) {
//						    			System.out.println(data);
//						    		}
						    	
						    	}
						    	else if(cell.getColumnIndex() == 78)//boe_no
						    	{
						    		data=boe_no;
						    	}
						    	else if(cell.getColumnIndex() == 79 && inv_flag!=null)//boe_no
						    	{	
						    		data=inv_flag;
						    	}
						    	else if(cell.getColumnIndex() == 84)//cd_pd_amt only for custom final
						    	{
						    		
						    		if(inv_flag.equals("CF"))
						    		{
						    			data=net_pay_cf;
						    		}
						    		else
						    		{
						    			data=null;
						    		}
						    		
						    	}
	
						    	else {
							    		
							    		if (cell.getColumnIndex() == 15) {	//inv_no
							    			inv_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
									    	if(inv_no != null) {
									    		inv_no = inv_no.substring(1, inv_no.length()-1);
									    	}
//									    	System.out.println("==>"+inv_no);
							    		}
							    		if (cell.getColumnIndex() == 17) {	//FREQ
							    			freq = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
									    	if(freq != null) {
									    		freq = freq.substring(1, freq.length()-1);
									    	}
							    		}
							    		if (cell.getColumnIndex() == 18) {	//due date
							    			due_date = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
									    	if(due_date != null) {
									    		due_date = due_date.substring(1, due_date.length()-1);
									    	}
							    		}
							    		
								    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
								    	if(data != null) {
								    		data = data.substring(1, data.length()-1);
								    	}
						    	}
						    	
						    	
						    	stmt1.setString(index++, data);
						    
						    }  
//	("COMPANY_CD", "COUNTERPARTY_CD", "AGMT_NO", "AGMT_REV", "CONT_NO", "CONT_REV", "CONTRACT_TYPE", "PLANT_SEQ", "BU_UNIT", 
//						    "FREQ", "PERIOD_START_DT", "PERIOD_END_DT", "INVOICE_SEQ", "FINANCIAL_YEAR", "INV_FLAG")    	

						    queryString = "SELECT PLANT_SEQ "
									+ "FROM FMS_PUR_SG_INV_MST "
									+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ?  "
									+ "AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT = ? AND FREQ = ? "
									+ "AND PERIOD_START_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND PERIOD_END_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND FINANCIAL_YEAR = ? AND INV_FLAG = ? "
									+ "AND PLANT_SEQ = ?  AND INVOICE_SEQ = ?";
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, company_cd);   	
					     	stmt.setString(2, cd);   
					     	stmt.setString(3, agmt_no);   	
					     	stmt.setString(4, agmt_rev); 
					     	stmt.setString(5, cont_no);   	
					     	stmt.setString(6, cont_rev);   
					     	stmt.setString(7, contract_type);   	
					     	stmt.setString(8, bu_seq);
					     	stmt.setString(9, freq);
					     	stmt.setString(10, exp_from_dt);
					     	stmt.setString(11, exp_to_dt);
					     	stmt.setString(12,financial_year);
					    	stmt.setString(13,inv_flag);
					    	stmt.setString(14,plant_seq_no);
					    	stmt.setInt(15,inv_seq_no);
					    	
					    	rset = stmt.executeQuery();

						    if (row.getRowNum() != 0 && !rset.next() && agmt_no!=null && sale_price != null && cd!=null &&  !inv_no.equals("") && bu_seq!=null && plant_seq_no!=null && !contract_type.equals("")  && contact_person_cd!=null && bu_contact_person_cd!=null && due_date!=null     ) {				
						   
						    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev+", "+cont_no+","+cont_rev+", "+contract_type+", "+plant_seq_no+", "+bu_seq+","+freq+","+exp_from_dt+", "+exp_to_dt+", "+inv_seq_no+", "+financial_year+","+inv_flag+","+contact_person_cd+","+bu_contact_person_cd+","+inv_no+","+sale_price+","+sys_invoice_no+","+cargo_no+","+cargo_ref_no+","+agmt_type+","+boe_no+","+due_date+","), conn,"");
						    	stmt1.executeUpdate();
						    	stmt1.close();
						    	logger_count++;	
						    	total_count++;
						    }
						    else {
						    	skipped_count++;
						    	stmt1.close();
						    	total_count++;
						    	logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev+", "+cont_no+","+cont_rev+", "+contract_type+", "+plant_seq_no+", "+bu_seq+","+freq+","+exp_from_dt+", "+exp_to_dt+", "+inv_seq_no+", "+financial_year+","+inv_flag+","+contact_person_cd+","+bu_contact_person_cd+","+inv_no+","+sale_price+","+sys_invoice_no+","+cargo_no+","+cargo_ref_no+","+agmt_type+","+boe_no+","+due_date+","), conn, "E");

						    	if (invseq.containsKey(financial_year + inv_flag )) {
									inv_seq_no = invseq.get(financial_year + inv_flag );
									inv_seq_no=inv_seq_no-1;
									invseq.put(financial_year + inv_flag , inv_seq_no);
								}
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

			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL :,"+total_count+",,,,,,,,,,,,,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED :,"+logger_count+",,,,,,,,,,,,,,,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA :,"+skipped_count+",,,,,,,,,,,,,,,,,,"), conn); 
			
						
			logger.checkpoint(fname, "<<END>><<FMS_PUR_SG_INV_MST>>,,,,,,,,,,,,,,,,,,,", conn);
			
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
	
	public void FMS_CUSTOM_PD_BOND_DTL() throws IOException, SQLException {

		function_nm="FMS_CUSTOM_PD_BOND_DTL()";
		try {
			table_name = "FMS_CUSTOM_PD_BOND_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_CUSTOM_PD_BOND_DTL>>,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;

			queryString1="INSERT INTO FMS_CUSTOM_PD_BOND_DTL(COMPANY_CD,SEQ_NO,CAL_YEAR,PD_BOND,PD_BOND_UNIT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT)"
					+ "VALUES(?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CUSTOM_PD_BOND_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CUSTOM_PD_BOND_DTL_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
				
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}
				
			logger.checkpoint(fname, "COMPANY_CD,SEQ_NO ,CAL_YEAR,PD_BOND,TIMESTAMP", conn);
			
			String cal_yr="",seq="";
			String pd_bond="",pd_bond1="";
			data = null;
			while (rowIterator.hasNext()) {
				
				total_count++;
				index = 1;
				row = rowIterator.next();
				stmt1 = conn.prepareStatement(queryString1);

			    cellIterator = row.cellIterator();
					    while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						
									
				    	if (cell.getColumnIndex() == 0) {	
				    		data="2";
				    		cal_yr = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(cal_yr != null) {
					    		cal_yr = cal_yr.substring(1, cal_yr.length()-1);
					    	}
				    	}
				    	else if(cell.getColumnIndex() == 1)
				    	{
				    		seq = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(seq != null) {
					    		seq = seq.substring(1, seq.length()-1);
					    	}
					    	data=seq;
				    	}
				    	else if(cell.getColumnIndex() == 2)
				    	{
				    		data=cal_yr;
				    	}
						else if(cell.getColumnIndex() == 3 ) {
							pd_bond = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(pd_bond != null) {
					    		pd_bond = pd_bond.substring(1, pd_bond.length()-1);
					    		if(pd_bond.length()>=13 )
						    	{
					    			double bondValue = Double.parseDouble(pd_bond); 
						     pd_bond1 = nf.format(bondValue); 
						     data=pd_bond1+"";
						    	}
					    		else {
					    		data=pd_bond+"";}
					    	}
					    			    		
						}
						else {
				    		
				    		data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
//				    	System.out.println(":"+data+":");
				    	stmt1.setString(index++, data);
			   }
					    //check for duplicate:"COMPANY_CD", "SEQ_NO", "CAL_YEAR")
				    queryString = "SELECT  SEQ_NO "
							+ "FROM FMS_CUSTOM_PD_BOND_DTL "
							+ "WHERE COMPANY_CD = ? AND SEQ_NO = ? AND CAL_YEAR =? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);   	
			     	stmt.setString(2, seq);   
			     	stmt.setString(3, cal_yr);
			     	
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && cal_yr!=null && !rset.next() ) {
				    	logger.data(fname,(company_cd+","+seq+","+cal_yr+","+pd_bond+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;				    	
				    }
				    else {
				    	skipped_count++;
				    	stmt1.close();
				    	logger.data(fname,(company_cd+","+seq+","+cal_yr+","+pd_bond+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL :,"+total_count+",,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED :,"+logger_count+",,,,,"), conn); 
						
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA :,"+skipped_count+",,,,,"), conn); 
			
			logger.checkpoint1(fname1,logger_count+",", conn);
						
			logger.checkpoint(fname, "<<END>><<FMS_CUSTOM_PD_BOND_DTL>>,,,,", conn);
			
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

	
	public void FMS_PUR_SG_INV_MST_PURCHASE() throws IOException, SQLException {
		
		function_nm="FMS_PUR_SG_INV_MST_PURCHASE()";
		try {
			table_name = "FMS_PUR_SG_INV_MST_PURCHASE";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_PUR_SG_INV_MST_PURCHASE>>,,,,,," , conn);
			
			data = "";
			int count2=0;
			int extr_count=0;
			
			queryString1= "INSERT INTO FMS_PUR_SG_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,PLANT_SEQ,"
					+ "CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,"
					+ "EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,"
					+ "CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,FINANCIAL_YEAR,TXN_CHARGE,"
					+ "TXN_AMOUNT,TAX_TXN_AMT,TAX_TXN_CD,TAX_TXN_EFF_DT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,TCS_CERT_FLAG,TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TCS_EFF_DT,"
					+ "SAP_EXCHNG_RATE,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,TCS_TDS,SYS_INV_NO,PAY_INSERT_BY,PAY_INSERT_DT,"
					+ "PAY_RECV_AMT,PAY_RECV_DT,PAY_REMARK,PAY_UPDATE_BY,PAY_UPDATE_DT,CARGO_NO,BOE_NO,INV_FLAG,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,SUG_QTY,"
					+ "SUG_PERCENT,QTY_UNIT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,OTHER_CHARGES,MARKET_MARGIN_AMT,OTHER_CHARGES_AMT,FIN_SYS)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,"
					+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,"
					+ "?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,"
					+ "?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),"
					+ "?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),"
					+ "?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_PUR_SG_INV_MST_PURCHASE_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_PUR_SG_INV_MST_PURCHASE_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
			
				logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,DOM_BUY_FLAG,"
						+ "BU_SEQ,PLANT_SEQ,INV_SEQ,FREQ,PLANT_CONTACT,BU_CONTACT,START_DT,END_DT,INVOICE_NO,CONT_REF,TIMESTAMP", conn);
				while (rowIterator.hasNext()) {  

					row = rowIterator.next();
					
						
					extr_count++;
						String dom_buy_flag="",cont_ref_no="",inv_no="",data1="",trans_chrg="",other_chrg="",tds_flag="";
						String cont_no ="", cont_rev="" ,agmt_rev_no="", agmt_no = "", plant_seq="" , bu_seq="";
						cd = "0";
						String inv_raised_in ="",tax="",tax_cd="",start_dt="",end_dt="",freq="",fin_year="",inv_flag="",plant_contact="",bu_contact="",tcs_flag="",tax_eff_dt="";

						int max_cd=0;
						data = null;
						BigDecimal alloc_qty = BigDecimal.ZERO;
						BigDecimal price = BigDecimal.ZERO;
						BigDecimal sale_amt = BigDecimal.ZERO;
						BigDecimal exch_val = BigDecimal.ZERO;
						BigDecimal gross = BigDecimal.ZERO;
						BigDecimal tax_amt = BigDecimal.ZERO;
						BigDecimal invoice_amt=BigDecimal.ZERO;
						BigDecimal inv_amt1 = BigDecimal.ZERO;
						BigDecimal netpayable = BigDecimal.ZERO;
						BigDecimal tcs_amt = BigDecimal.ZERO;
						BigDecimal transChrg = BigDecimal.ZERO;
						BigDecimal otherChrg =BigDecimal.ZERO;
//						int m=1;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
					
					cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						data = null;
						
						if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 
							abbr = cell.getStringCellValue().contains("null") ? null :  cell.getStringCellValue();
							if (abbr!=null) {
								abbr = abbr.substring(1, abbr.length() - 1);
							}
							data = company_cd;
						}
						else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
							cont_ref_no = cell.getStringCellValue().contains("null") ? null :  cell.getStringCellValue();
							if (cont_ref_no != null) {
								cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
								
								if( cont_ref_no.split("-")[1].equals("I")) {
									queryString3 = "SELECT COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV FROM "
											+ "FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ? AND COMPANY_CD = ?";
									stmt3 = conn.prepareStatement(queryString3);
									stmt3.setString(1, "%"+cont_ref_no+"%");
									stmt3.setString(2, company_cd);
									rset3 = stmt3.executeQuery();
									while(rset3.next()) {
										agmt_no = rset3.getString(2);
										agmt_rev_no = rset3.getString(3);
										cont_no = rset3.getString(4);
										cont_rev = rset3.getString(5);
											
									}
									rset3.close();
									stmt3.close();
										
									
									}
								else {
									queryString = "SELECT COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV FROM "
											+ "FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ? AND COMPANY_CD=? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, "%"+cont_ref_no+"%");
									stmt.setString(2, company_cd);
									rset = stmt.executeQuery();
									while(rset.next()) {
										agmt_no = rset.getString(2);
										agmt_rev_no = rset.getString(3);
										cont_no = rset.getString(4);
										cont_rev = rset.getString(5);
											
									}
									rset.close();
									stmt.close();
										
									
								}
							}
								
							
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1,abbr);
							rset = stmt.executeQuery();
							if(rset.next()) {
								cd = rset.getString(1);
							}
							else {
								cd=null;
							}
							rset.close();
							stmt.close();
							data=cd;
							
						}
						else if(cell.getColumnIndex() == 2) { //AGMT_NO
							data=agmt_no+"";
						}
						
						else if(cell.getColumnIndex() == 3) { //AGMT_REV_NO
							data=agmt_rev_no+"";
						}
						else if(cell.getColumnIndex() == 4) {  //CONT_NO
							
							data=cont_no+"";
						}
						else if(cell.getColumnIndex() == 5) {  //CONT_REV
							
							data=cont_rev+"";
						}
						else if(cell.getColumnIndex() == 6) {  //CONTRACT_TYPE
							dom_buy_flag = cell.getStringCellValue().contains("null") ? null :  cell.getStringCellValue();
							dom_buy_flag = dom_buy_flag.substring(1,dom_buy_flag.length()-1);
							
							data=dom_buy_flag+"";
							
						}
						else if(cell.getColumnIndex() == 7) {  //BU_UNIT
							
							bu_seq = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							bu_seq = bu_seq.substring(1,bu_seq.length()-1);
							data = bu_seq;
						}	
						else if(cell.getColumnIndex() == 8) {  //BU_CONTACT
							
							queryString= "SELECT SEQ_NO "
									+ "FROM FMS_ENTITY_CONTACT_MST "
									+ "WHERE COMPANY_CD = ? AND  COUNTERPARTY_CD = '2' AND ENTITY = 'B' AND ADDR_FLAG = ? AND ADDR_IS_ACTIVE = 'Y' AND ACTIVE_FLAG = 'Y' AND RM_FLAG = 'Y' ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1,company_cd);
							stmt.setString(2,"P"+bu_seq);
							rset = stmt.executeQuery();
							if(rset.next()) {
								data = rset.getString(1);	
							}
							else {
								data = "1" ;
							}
							rset.close();
							stmt.close();
							
							bu_contact = data;
						}
						else if(cell.getColumnIndex() == 9) {  //PLANT_SEQ
						
								plant_seq = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (plant_seq != null) {
									plant_seq = plant_seq.substring(1, plant_seq.length() - 1);
								}
								
							data = plant_seq;
						}
						else if(cell.getColumnIndex() == 10) {  //PLANT_CONTACT
							
							queryString= "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD = ? AND "
									+ " COUNTERPARTY_CD = ? AND ENTITY = 'T' AND ADDR_FLAG = ? AND ADDR_IS_ACTIVE = 'Y' AND ACTIVE_FLAG = 'Y' AND RM_FLAG = 'Y' ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1,company_cd);
							stmt.setString(2,cd);
							stmt.setString(3,"P"+plant_seq);
							rset = stmt.executeQuery();
							if(rset.next()) {
								data = rset.getString(1);	
							}
							else {
								data = "1" ;
							}
							rset.close();
							stmt.close();
							
							plant_contact = data;
						}
						else if(cell.getColumnIndex() == 11) {	//INVOICE_SEQ
							fin_year = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							fin_year = fin_year.substring(1,fin_year.length()-1);
							queryString = "SELECT MAX(INVOICE_SEQ) FROM FMS_PUR_SG_INV_MST WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, fin_year);
							rset = stmt.executeQuery();
							if(rset.next()) {
								max_cd = rset.getInt(1);
								max_cd++;
							}
							else {
								max_cd = 1;  
							}
							rset.close();
							stmt.close();
							
//							inv_seq = max_cd+"";
							data = max_cd+"";
//							if(cont_ref_no.equals("-D-21035-4"))
//							{
//								System.out.println("==>"+max_cd+",,,"+cd);
//							}
						}
						
						else if(cell.getColumnIndex() == 14) {	//FREQ
							freq = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if (freq != null) {
								freq = freq.substring(1, freq.length() - 1);
							}
							
							data = freq;
							
						}
						
						
						else if(cell.getColumnIndex() == 18) {	//ALLOC_QTY
						
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if (data != null) {
								data = data.substring(1, data.length() - 1);
							}
							alloc_qty = new BigDecimal(data);
							data = alloc_qty+"";	

						}
						else if(cell.getColumnIndex() == 19) {	//SALE_PRICE
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							data = data.substring(1,data.length()-1);
							price = new BigDecimal(data);
							data = price+"";
						}
						else if(cell.getColumnIndex() == 21) {	//SALE_AMT
							
						    // Multiply for sale amount
							sale_amt = alloc_qty.multiply(price);
							data = sale_amt+"";
//							if(cont_no.equals("822005"))
//							{
//								System.out.println(""+sale_amt+":"+alloc_qty+":"+price);
//							}
//							if(cont_no.equals("824016"))
//							{
//								System.out.println(":"+sale_amt+":"+price+":"+alloc_qty);
//							}
							
						}
						
						
						else if(cell.getColumnIndex() == 24) {	//EXCHG_RATE_VALUE
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if(data != null) {
								data = data.substring(1,data.length()-1);
//								exch_val = Double.parseDouble(data);
								exch_val = new BigDecimal(data);
							}
							data = exch_val+"";
						}
						else if (cell.getColumnIndex() == 25) {		//INVOICE_RAISED_IN
							queryString = "SELECT INVOICE_CUR_CD FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND "
									+ "AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, company_cd);
							stmt.setString(2, cd);
							stmt.setString(3, agmt_no);
							stmt.setString(4, agmt_rev_no);
							stmt.setString(5, cont_no);
							stmt.setString(6, cont_rev);
							stmt.setString(7, dom_buy_flag);
							stmt.setString(8, plant_seq);
							rset = stmt.executeQuery();
							if(rset.next()) {
								data = rset.getString(1);
							}
							else {
								data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
								if (data != null) {
									data = data.substring(1, data.length() - 1);
								}
							}
							inv_raised_in=data;
							rset.close();
							stmt.close();
							
//							if(cont_no.equals("822005"))
//							{
//								System.out.println(":"+data+":"+agmt_no+":"+dom_buy_flag+":"+plant_seq);
//							}
						}
						else if(cell.getColumnIndex() == 26) {	//GROSS_AMT
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if (data != null) {
								data = data.substring(1, data.length() - 1);
								
								if(inv_raised_in.equals("2"))
								{
									
									data = data.split(":")[1]; 
								    BigDecimal gross1 = new BigDecimal(data); 
								    gross = gross1;
								}else
								{
									data=data.split(":")[0];
								    BigDecimal gross1 = new BigDecimal(data); 
								    gross = gross1;
									
								}
								
							}
							data = gross+"";
						}
						else if(cell.getColumnIndex() == 27) {	//TAX_AMT

							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if (data != null) {
								data = data.substring(1, data.length() - 1);
								tax_amt = new BigDecimal(data);
							}
							
							
							//							tax = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
//							if (tax != null) {
//							    tax = tax.substring(1, tax.length() - 1); // Remove quotes
//
//							    if (tax.contains("%")) {
//							 String taxRateStr = tax.split("%")[0];
//
//							 BigDecimal taxRate = new BigDecimal(taxRateStr); 
//							 // % value "5" for 5%
//
//							 if (!taxRateStr.equals("0") && gross.compareTo(BigDecimal.ZERO) != 0) {
//							 	tax_amt = gross.multiply(taxRate).divide(new BigDecimal("100"));
//							 } else if (!taxRateStr.equals("0") && gross.compareTo(BigDecimal.ZERO) == 0) {
//							 	tax_amt = sale_amt.multiply(taxRate).divide(new BigDecimal("100"));
//							 } else if (taxRateStr.equals("0")) {
//							 	tax_amt = BigDecimal.ZERO;
//							 }
//
//							  
//							    }
//							}
							
							data = tax_amt+"";
//							data = data * gross / 100;
						}
						else if(cell.getColumnIndex() == 28) {	//TAX_STRUCT_CD
							tax_cd = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if(tax_cd != null) {
							tax_cd = tax_cd.substring(1,tax_cd.length()-1);
							
								queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, tax_cd);
								rset = stmt.executeQuery();
								if(rset.next()) {
									tax_cd = rset.getString(1);
								}
								rset.close();
								stmt.close();
							}
							else {
								tax_cd = "0";
							}
							data = tax_cd;
						}
						
						else if(cell.getColumnIndex() == 29){	//TAX_EFF_DT
							queryString = "SELECT TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, tax);
							rset = stmt.executeQuery();
							if(rset.next()) {
								data = rset.getString(1);
							}
							else {
								data = null;
							}
							rset.close();
							stmt.close();
						}
						
						else if(cell.getColumnIndex() == 30){	//INVOICE_AMT
//							if( Double.doubleToRawLongBits(gross)!=Double.doubleToRawLongBits(0))
//							if(gross!=0.0) 
//							{
							if (tax_amt != null) {
							    invoice_amt = gross.add(tax_amt);
							}
							data1 = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(data1 != null) {
								data1 = data1.substring(1, data1.length()-1);
							
								trans_chrg=data1.split(":")[0];
								other_chrg=data1.split(":")[1];
								if(!trans_chrg.equals("null"))
								{
									transChrg = new BigDecimal(trans_chrg);
									invoice_amt = invoice_amt.add(transChrg);
								}
								
								if(!other_chrg.equals("null"))
								{
									otherChrg = new BigDecimal(other_chrg);
									invoice_amt = invoice_amt.add(otherChrg);
								}
								
								data = invoice_amt+"";
							}
							else
							{
								data = invoice_amt+"";
							}
							
						}
						
						else if(cell.getColumnIndex() == 33){	//NET_PAYABLE_AMT=((inv_amt+tax_amt)*0.1)+inv_amt+tax_amt)
							
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(data != null) {
								data = data.substring(1, data.length()-1);
								if(data.equals("Y"))
								{
//									tcs_flag=data;
									
									inv_amt1 = invoice_amt.multiply(new BigDecimal("0.001")); 
									tcs_amt = inv_amt1;
									
//									netpayable=inv_amt1+invoice_amt;
									netpayable = invoice_amt.add(inv_amt1);
//									System.out.println("netpayable:"+netpayable);
									
									data=netpayable+"";

								}
								else
								{
//									invoice_amt=invoice_amt+Double.parseDouble(trans_chrg)+Double.parseDouble(other_chrg);
//									invoice_amt = invoice_amt;
									data = invoice_amt+"";
								}
							}
						}
						else if(cell.getColumnIndex() == 55)//tcs_flag
						{
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
							// if inv is in USD (IF CER FLAG : N ) THEN TCS_FLAG NULL
							tcs_flag=data;
//							if(inv_raised_in.equals("2")) {
//								if(tcs_flag.equals("N")) {
//									tcs_flag=null;
//									}
//							}
//							data=tcs_flag;
						}
						else if(cell.getColumnIndex() == 56)//tcs_amt
						{
							if(tcs_flag!=null && tcs_flag.equals("Y"))
							{
								data=tcs_amt+"";
							}
							else {
								data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
								if(data != null) {
									data = data.substring(1, data.length()-1);
								}
							}
							
						}
						else if(cell.getColumnIndex() == 58)//tcs_cd
						{
							
							if(tcs_flag!=null &&  tcs_flag.equals("Y"))
							{
								queryString = "SELECT TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'),TAX_STRUCT_CD  "
										+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TCS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, cd);
								stmt.setString(2, "T");
								stmt.setString(3,company_cd);
								rset = stmt.executeQuery();
								if(rset.next()) {
									tax_eff_dt=rset.getString(1);
									data = rset.getString(2);
								}
								else {
									data = null;
									tax_eff_dt=null;
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
							
						}
						else if(cell.getColumnIndex()==59)
						{
							if(tcs_flag!=null && tcs_flag.equals("Y"))
							{
								data=tax_eff_dt;
							}
							else {
								data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
								if(data != null) {
									data = data.substring(1, data.length()-1);
								}
							}
							
						}
						else if(cell.getColumnIndex()==64)
						{
							if(tcs_flag!=null && tcs_flag.equals("N") && !inv_raised_in.equals("2") && !inv_raised_in.equals("null"))
							{
								 if (gross.compareTo(BigDecimal.ZERO) != 0) { // Check if gross is NOT zero
								 BigDecimal rate = new BigDecimal("0.1");
								 BigDecimal tds_Amt = gross.multiply(rate).divide(new BigDecimal("100"));
								 data = tds_Amt + "";
								    } else {
								 data = null;
								    }
							}
							else
							{
								data=null;
							}
							
						}
//						else if(cell.getColumnIndex()==64)
//						{
//							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//							if(data != null) {
//								data = data.substring(1, data.length()-1);
//							}
//							
//							if(tcs_flag!=null && tcs_flag.equals("N") && inv_raised_in.equals("2") && !inv_raised_in.equals("null"))
//							{
//								data=null;
//							}
//							
//						}
						else if(cell.getColumnIndex() == 68)// TCS TDS FLAG
						{
							if(tcs_flag!=null)
							{
								if(tcs_flag.equals("Y"))
								{
									data="TCS";
								}else if(tcs_flag.equals("N") && !inv_raised_in.equals("2") )
								{
									data="TDS";
								}
								else
								{
									data="NA";
								}
							}
							else {
								data="NA";
							}
						}
						else if(cell.getColumnIndex() == 69 && dom_buy_flag!=null && inv_flag!=null )//SYS_INV_NO-type-D,T,I
				    	{
				    		String invoice_prefix=utilBean.getInvoicePrefix(conn,"2");
				    		String invoice_seq_no=max_cd+"";
				    		String system_invoice_no=invoice_prefix+""+dom_buy_flag+"S"+utilBean.PrePaddingZero(invoice_seq_no, 4)+"/"+fin_year;	
				    		data = system_invoice_no;	
				    		
				    	}
						else {
							if (cell.getColumnIndex() == 12) {	//inv_no
				    			inv_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						    	if(inv_no != null) {
						    		inv_no = inv_no.substring(1, inv_no.length()-1);
						    	}
				    		}
							
							if(cell.getColumnIndex() == 15) {
								start_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
								if(start_dt!=null)
								{
									start_dt = start_dt.substring(1,start_dt.length()-1);
								}
							}
							
							else if(cell.getColumnIndex() == 16) {
								end_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
								if(end_dt!=null)
								{
									end_dt = end_dt.substring(1,end_dt.length()-1);
								}
							}
							
							
							else if(cell.getColumnIndex() == 79) {
								inv_flag = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
								if(end_dt!=null)
								{
									inv_flag = inv_flag.substring(1,inv_flag.length()-1);
								}
							}
							
							
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
						}
						
						stmt1.setString(index++, data);
					}
				
					queryString = "SELECT ENT_BY FROM FMS_PUR_SG_INV_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? "
							+ " AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT = ? AND PLANT_SEQ = ? "
							+ " AND FREQ = ? AND PERIOD_START_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND PERIOD_END_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') "
							+ " AND FINANCIAL_YEAR = ? AND INV_FLAG = ? AND INVOICE_SEQ = ? ";
//					System.out.println(queryString);
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev_no);
					stmt.setString(5, cont_no);
					stmt.setString(6, cont_rev);
					stmt.setString(7, dom_buy_flag );
					stmt.setString(8, bu_seq );
					stmt.setString(9, plant_seq );
					stmt.setString(10, freq );
					stmt.setString(11, start_dt);
					stmt.setString(12, end_dt);
					stmt.setString(13, fin_year);
					stmt.setString(14, inv_flag);
					stmt.setInt(15, max_cd);
					
					rset = stmt.executeQuery();
					
					if (row.getRowNum() != 0 && !rset.next()  && !cont_no.equals("") && cd!=null  && freq != null && plant_contact != null && bu_contact != null  && plant_seq!=null) {
						
						logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+bu_seq+","+plant_seq+","+max_cd+","+freq+","+plant_contact+","+bu_contact+","+start_dt+","+end_dt+","+inv_no+","+cont_ref_no.substring(1,cont_ref_no.length())+","), conn,"");
						stmt1.executeUpdate();
						
						stmt1.close();
						
						logger_count++;
						count2++;
					}else {
						stmt1.close();
						skipped_count++;
						logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+bu_seq+","+plant_seq+","+max_cd+","+freq+","+plant_contact+","+bu_contact+","+start_dt+","+end_dt+","+inv_no+","+cont_ref_no.substring(1,cont_ref_no.length())+","), conn, "E");
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
//			conn.commit();
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+extr_count+",,,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+count2+",,,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>><<FMS_PUR_SG_INV_MST_PURCHASE>>", conn);
			
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

	public void FMS_PUR_SG_INV_TAX_DTL() throws IOException, SQLException {

		function_nm="FMS_PUR_SG_INV_TAX_DTL()";
		try {
			
			
			System.out.println("<<START>><<FMS_PUR_SG_INV_TAX_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_PUR_SG_INV_TAX_DTL>>,", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String tax_struct_cd = "",tax_code="",desc="",desc_nm="",bcd="";
			
			columns = "COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,INV_FLAG";
			
			logger.checkpoint(fname, "COMNPANY_CD,CONT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,INV_FLAG,TIMESTAMP", conn);
			
			queryString = " SELECT COMPANY_CD, CONTRACT_TYPE, INVOICE_SEQ, FINANCIAL_YEAR, TAX_STRUCT_CD, NULL, TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY HH24:MI:SS'), NULL,"
					+ " TAX_AMT, GROSS_AMT, ENT_BY, TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, INV_FLAG,ASSESSABLE_AMT, CONT_NO  FROM FMS_PUR_SG_INV_MST WHERE COMPANY_CD = ? ";//AND INV_FLAG IN ('CP','CF')";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			queryString1 = "INSERT INTO FMS_PUR_SG_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
					+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,INV_FLAG) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')"
					+ ",?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";

			while (rset.next()) {
				
				tax_struct_cd = rset.getString(5);
				String desc1="",eff_dt="";
				String count_value="",bcd_amt="",assessable_amt="";
			
				//==========count loop
				int count_desc=1;
				bcd_amt = "";
				for(int j=0;j<count_desc;j++)
				{
					
					queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, tax_struct_cd);
					rset2 = stmt2.executeQuery();
					while(rset2.next()) {
						
					stmt1 = conn.prepareStatement(queryString1);
					
						for(int i = 0;i < columns.split(",").length;i++) 
						{
							data = "";
							
							data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
							if(i == 4) {	//TAX_STRUCT_CD-5
								if(tax_struct_cd != null) {
									tax_struct_cd = rset.getString(5);
								}
								else {
									tax_struct_cd = "0";
								}
								data = tax_struct_cd;
							}
							else if(i == 5) {
								
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
									else {
										desc = null;
									}
								
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
							
							else if(i == 6) {	//TAX_EFF_DT
								data = eff_dt;
							}
							else if(i == 7) {	//TAX_DESCR
							   if(tax_struct_cd != null) {
									if(!tax_struct_cd.equals("0")) {
										data=desc;
									}
							   }
							    else {
										data = null;
								}
							
							
							}
							else if(i==8)
							{
								
								double tax_amt = 0.0;
								if (desc != null) {
								    if ( !desc.contains("on") ) {
								    	
								 count_value = desc.split("%")[0];
								 count_value = count_value.split(" ")[1]; 

								     assessable_amt = rset.getString(16);
								     tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(assessable_amt)) / 100;
								     bcd_amt = tax_amt + "";
								     bcd=bcd_amt;

								    }
								    else if (desc.contains("on")) {

								 count_value = desc.split("%")[0];
								 count_value = count_value.split(" ")[1]; 
								 tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(bcd_amt)) / 100;
								    
								    }
								}
								data = tax_amt + "";

							}
							
							else if(i == 9 && desc != null && desc.contains("on")) {

								data = bcd;
							}
							
							stmt1.setString(i+1,data);
								
							}
					
					
					
				//for data already exists..
				queryString5 = "SELECT COMPANY_CD FROM FMS_PUR_SG_INV_TAX_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE = ?  AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? AND INV_FLAG = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, rset.getString(2));
				stmt5.setString(3, rset.getString(3));
				stmt5.setString(4, rset.getString(4));
				stmt5.setString(5, tax_struct_cd);
				stmt5.setString(6, tax_code);
				stmt5.setString(7, rset.getString(15));
			
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next() && tax_struct_cd != null && tax_code != null) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + tax_struct_cd + "," + tax_code+ "," + rset.getString(15) + " , " ), conn, "");

					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + tax_struct_cd + "," + tax_code+ "," + rset.getString(15) + " , " ), conn, "E");
					
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
			
		
			System.out.println("<<END>><<FMS_PUR_SG_INV_TAX_DTL>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_PUR_SG_INV_TAX_DTL>>,", conn);
			
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
		}
	}
public void FMS_PUR_PG_INV_MST() throws IOException, SQLException {
	
	function_nm="FMS_PUR_PG_INV_MST()";
	try {
		table_name = "FMS_PUR_PG_INV_MST";
		System.out.println("<<START>><<"+table_name+">>");
		logger.checkpoint(fname,"\n<<START>>,<<FMS_PUR_PG_INV_MST>>,,,,,,,,," , conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;   
		total_count = 0;
		
		queryString1= "INSERT INTO FMS_PUR_PG_INV_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_CONTACT_PERSON_CD,PLANT_SEQ,"
				+ "CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,"
				+ "EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,"
				+ "CHECKED_FLAG,CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,FINANCIAL_YEAR,TXN_CHARGE,"
				+ "TXN_AMOUNT,TAX_TXN_AMT,TAX_TXN_CD,TAX_TXN_EFF_DT,PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,TCS_CERT_FLAG,TCS_AMT,TCS_FACTOR,TCS_STRUCT_CD,TCS_EFF_DT,"
				+ "SAP_EXCHNG_RATE,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,TDS_AMT,TDS_FACTOR,TDS_STRUCT_CD,TDS_EFF_DT,TCS_TDS,SYS_INV_NO,PAY_INSERT_BY,PAY_INSERT_DT,"
				+ "PAY_RECV_AMT,PAY_RECV_DT,PAY_REMARK,PAY_UPDATE_BY,PAY_UPDATE_DT,CARGO_NO,BOE_NO,INV_FLAG,CIF_AMT,ASSESSABLE_AMT,REMARK,DIFF_PRICE,CD_PAID_AMT,SUG_QTY,"
				+ "SUG_PERCENT,QTY_UNIT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,OTHER_CHARGES,MARKET_MARGIN_AMT,OTHER_CHARGES_AMT,FIN_SYS)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,"
				+ "?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,"
				+ "?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),"
				+ "?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),"
				+ "?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?)";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_PUR_PG_INV_MST_"+start_end_dt+".xlsx");
		if(file1.exists()) {
			
			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_PUR_PG_INV_MST_"+start_end_dt+".xlsx"));
			
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,DOM_BUY_FLAG,BU_SEQ,PLANT_SEQ,INV_SEQ,FREQ,PLANT_CONTACT,BU_CONTACT,START_DT,END_DT,INVOICE_NO,TIMESTAMP", conn);
			while (rowIterator.hasNext()) {  
		
				row = rowIterator.next();

					total_count++;
					String  dom_buy_flag="",cont_ref_no="",tax_eff_dt="",inv_no="",inv_raised_in="",data1="",trans_chrg="",other_chrg="",tds_flag="";
					String cont_no ="", cont_rev="" ,agmt_rev_no="", agmt_no = "", plant_seq="" , bu_seq="";
					cd = "0";
					String tax="",tax_cd="",start_dt="",end_dt="",inv_seq="",freq="",fin_year="",inv_flag="",plant_contact="",bu_contact="",tcs_amt="",tcs_flag="";

					double alloc_qty=0,gross=0,tax_amt=0,invoice_amt=0,price=0,sale_amt=0,exch_val=0,inv_amt1=0,netpayable=0;
					 double tds_Amt=0;
//					int max_cd=0;
					 String inv_pg_seq_no="";
					data = null;
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
				
				cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					cell = cellIterator.next();
					data = null;
					
					if (cell.getColumnIndex() == 0) {	// Counterparty_Abbr, Company Code 
						abbr = cell.getStringCellValue().contains("null") ? null :  cell.getStringCellValue();
						if (abbr!=null) {
							abbr = abbr.substring(1, abbr.length() - 1);
						}
						data = company_cd;
					}
					else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
						cont_ref_no = cell.getStringCellValue().contains("null") ? null :  cell.getStringCellValue();
						if (cont_ref_no != null) {
							cont_ref_no = cont_ref_no.substring(1, cont_ref_no.length() - 1);
							
							if( cont_ref_no.split("-")[1].equals("I")) {
								queryString3 = "SELECT COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ? AND COMPANY_CD = ?";
								stmt3 = conn.prepareStatement(queryString3);
								stmt3.setString(1, "%"+cont_ref_no+"%");
								stmt3.setString(2, company_cd);
								rset3 = stmt3.executeQuery();
								while(rset3.next()) {
									agmt_no = rset3.getString(2);
									agmt_rev_no = rset3.getString(3);
									cont_no = rset3.getString(4);
									cont_rev = rset3.getString(5);
										
								}
								rset3.close();
								stmt3.close();
									
								
								}
							else {
								queryString = "SELECT COUNTERPARTY_CD, AGMT_NO,AGMT_REV,CONT_NO,CONT_REV FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ? AND COMPANY_CD=? ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, "%"+cont_ref_no+"%");
								stmt.setString(2, company_cd);
								rset = stmt.executeQuery();
								while(rset.next()) {
									agmt_no = rset.getString(2);
									agmt_rev_no = rset.getString(3);
									cont_no = rset.getString(4);
									cont_rev = rset.getString(5);
										
								}
								rset.close();
								stmt.close();
									
								
							}
						}
							
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1,abbr);
						rset = stmt.executeQuery();
						if(rset.next()) {
							cd = rset.getString(1);
						}
						else {
							cd=null;
						}
						rset.close();
						stmt.close();
						data=cd;
						
					}
					else if(cell.getColumnIndex() == 2) { //AGMT_NO
						data=agmt_no+"";
					}
					
					else if(cell.getColumnIndex() == 3) { //AGMT_REV_NO
						data=agmt_rev_no+"";
					}
					else if(cell.getColumnIndex() == 4) {  //CONT_NO
						
						data=cont_no+"";
					}
					else if(cell.getColumnIndex() == 5) {  //CONT_REV
						
						data=cont_rev+"";
					}
					else if(cell.getColumnIndex() == 6) {  //CONTRACT_TYPE
						dom_buy_flag = cell.getStringCellValue().contains("null") ? null :  cell.getStringCellValue();
						dom_buy_flag = dom_buy_flag.substring(1,dom_buy_flag.length()-1);
						
						data=dom_buy_flag+"";
						
					}
					else if(cell.getColumnIndex() == 7) {  //BU_UNIT
						
						bu_seq = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						bu_seq = bu_seq.substring(1,bu_seq.length()-1);
						data = bu_seq;
					}	
					else if(cell.getColumnIndex() == 8) {  //BU_CONTACT
						
						queryString= "SELECT SEQ_NO "
								+ "FROM FMS_ENTITY_CONTACT_MST "
								+ "WHERE COMPANY_CD = ? AND  COUNTERPARTY_CD = '2' AND ENTITY = 'B' "
								+ "AND ADDR_FLAG = ? AND ADDR_IS_ACTIVE = 'Y' AND ACTIVE_FLAG = 'Y' AND RM_FLAG = 'Y' ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1,company_cd);
						stmt.setString(2,"P"+bu_seq);
						rset = stmt.executeQuery();
						if(rset.next()) {
							data = rset.getString(1);	
						}
						else {
							data = "1" ;
						}
						rset.close();
						stmt.close();
						
						bu_contact = data;
					}
					else if(cell.getColumnIndex() == 9) {  //PLANT_SEQ

							plant_seq = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if (plant_seq != null) {
								plant_seq = plant_seq.substring(1, plant_seq.length() - 1);
							}
							
						data = plant_seq;
					}
					else if(cell.getColumnIndex() == 10) {  //PLANT_CONTACT
						
						queryString= "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD = ? AND "
								+ " COUNTERPARTY_CD = ? AND ENTITY = 'T' AND ADDR_FLAG = ? "
								+ "AND ADDR_IS_ACTIVE = 'Y' AND ACTIVE_FLAG = 'Y' AND RM_FLAG = 'Y' ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1,company_cd);
						stmt.setString(2,cd);
						stmt.setString(3,"P"+plant_seq);
						rset = stmt.executeQuery();
						if(rset.next()) {
							data = rset.getString(1);	
						}
						else {
							data = "1" ;
						}
						rset.close();
						stmt.close();
						
						plant_contact = data;
					}
					else if(cell.getColumnIndex() == 11) {	//INVOICE_SEQ
						String inv_no_sg="",pr_st="",pr_end="";
						inv_no_sg = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						inv_no_sg = inv_no_sg.substring(1,inv_no_sg.length()-1);
//						queryString = "SELECT MAX(INVOICE_SEQ) FROM FMS_PUR_SG_INV_MST WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? ";
//						stmt = conn.prepareStatement(queryString);
//						stmt.setString(1, company_cd);
//						stmt.setString(2, fin_year);
//						rset = stmt.executeQuery();
//						if(rset.next()) {
//							max_cd = rset.getInt(1);
//							max_cd++;
//						}
//						else {
//							max_cd = 1;  
//						}
//						rset.close();
//						stmt.close();
//						data = max_cd+"";
						if(inv_no_sg!=null &&  inv_no_sg.contains("@"))
						{
							freq=inv_no_sg.split("@")[1];
							pr_st=inv_no_sg.split("@")[2];
							pr_end=inv_no_sg.split("@")[3];
							
//							System.out.println("===>"+inv_no_sg+":"+bu_seq+":"+plant_seq+":"+dom_buy_flag);
							queryString = "SELECT INVOICE_SEQ FROM FMS_PUR_SG_INV_MST "
									+ "WHERE COMPANY_CD = ? AND BU_UNIT = ? "
									+ "AND PLANT_SEQ = ? AND INV_FLAG = ? AND CONTRACT_TYPE = ? AND AGMT_NO = ?  AND AGMT_REV =?"
									+ "AND CONT_NO = ? AND CONT_REV = ? AND FREQ = ? AND COUNTERPARTY_CD = ? "
									+ "AND PERIOD_START_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND PERIOD_END_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1,company_cd);
							stmt.setString(2,bu_seq);
							stmt.setString(3,plant_seq);
							stmt.setString(4,"F");
							stmt.setString(5,dom_buy_flag);
							stmt.setString(6,agmt_no);
							stmt.setString(7,agmt_rev_no);
							stmt.setString(8,cont_no);
							stmt.setString(9,cont_rev);
							stmt.setString(10,freq);
							stmt.setString(11,cd);
							stmt.setString(12,pr_st);
							stmt.setString(13,pr_end);
							
							rset = stmt.executeQuery();
							if(rset.next()) {
								inv_pg_seq_no=rset.getString(1);
							}
							else {
								inv_pg_seq_no="";
							}
							rset.close();
							stmt.close();
							
						}
						
					
						data=inv_pg_seq_no;
					}


					else if(cell.getColumnIndex() == 14) {	//FREQ
						freq = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						if (freq != null) {
							freq = freq.substring(1, freq.length() - 1);
						}
						
						data = freq;
						
					}
					
					
					else if(cell.getColumnIndex() == 18) {	//ALLOC_QTY
						
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						if (data != null) {
							data = data.substring(1, data.length() - 1);
						}
						alloc_qty = Double.parseDouble(data);
						data = alloc_qty+"";

					}
					else if(cell.getColumnIndex() == 19) {	//SALE_PRICE
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						data = data.substring(1,data.length()-1);
						
						price = Double.parseDouble(data);
						
						data = price+"";
					}
					else if(cell.getColumnIndex() == 21) {	//SALE_AMT
						sale_amt = alloc_qty * price;
						data = sale_amt+"";
						
					}
					else if(cell.getColumnIndex() == 24) {	//EXCHG_RATE_VALUE
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						if(data != null) {
							data = data.substring(1,data.length()-1);
							exch_val = Double.parseDouble(data);
						}
						else {
							exch_val = 0;
						}
						data = exch_val+"";
					}
					
					
					
					else if (cell.getColumnIndex() == 25) {		//INVOICE_RAISED_IN
						queryString = "SELECT INVOICE_CUR_CD FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND "
								+ "AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_rev_no);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, dom_buy_flag);
						stmt.setString(8, plant_seq);
						rset = stmt.executeQuery();
						if(rset.next()) {
							data = rset.getString(1);
						}
						else {
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if (data != null) {
								data = data.substring(1, data.length() - 1);
							}
						}
						inv_raised_in=data;
						rset.close();
						stmt.close();

					}
					else if(cell.getColumnIndex() == 26) {	//GROSS_AMT

						if(inv_raised_in.equals("2"))
						{
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if (data != null) {
								data = data.substring(1, data.length() - 1);
							}
							data=data.split(",")[1];
						}
						else
						{
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if (data != null) {
								data = data.substring(1, data.length() - 1);
							}
							data=data.split(",")[0];
						}
						gross=Double.parseDouble(data);

					}
//					else if(cell.getColumnIndex() == 27) {	//TAX_AMT
//
//						tax = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
//						tax = tax.substring(1,tax.length()-1);
//
//						data = tax+"";
//					}
					else if(cell.getColumnIndex() == 27) {	//TAX_AMT

						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						if (data != null) {
							data = data.substring(1, data.length() - 1);
							tax =data;
						}
						data = tax+"";
					}
					
					
					else if(cell.getColumnIndex() == 28) {	//TAX_STRUCT_CD
						tax_cd = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						if(tax_cd != null) {
						tax_cd = tax_cd.substring(1,tax_cd.length()-1);
						
							queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, tax_cd);
							rset = stmt.executeQuery();
							if(rset.next()) {
								tax_cd = rset.getString(1);
							}
							rset.close();
							stmt.close();
						}
						else {
							tax_cd = "0";
						}
						data = tax_cd;
					}
					
					else if(cell.getColumnIndex() == 29){	//TAX_EFF_DT
						queryString = "SELECT TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, tax_cd);
						rset = stmt.executeQuery();
						if(rset.next()) {
							data = rset.getString(1);
						}
						else {
							data = null;
						}
						rset.close();
						stmt.close();
					}
					
					else if(cell.getColumnIndex() == 30){	//INVOICE_AMT
//						cell.setCellValue("'"+value+","+rset2.getString(22)+":"+rset2.getString(89)+"="+rset2.getString(93)+"'");
						data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
						if (data != null) {
							data = data.substring(1, data.length() - 1);
						}
						data1=data.split(":")[1];
						
						if(inv_raised_in.equals("2"))
						{
							data=data.split(",")[1];
							data=data.split(":")[0];
						}
						else
						{
							data=data.split(",")[0];
						}
//							invoice_amt = gross + tax_amt;
						invoice_amt=Double.parseDouble(data);
						
						trans_chrg=data1.split("=")[0];
						other_chrg=data1.split("=")[1];
						
						if(!tax.equals("") || !tax.equals("null"))
						{
							
							invoice_amt=invoice_amt+Double.parseDouble(tax);
							
						}
						
						
						if(!trans_chrg.equals("null"))// transp_chrg_amt
						{
							invoice_amt=invoice_amt+Double.parseDouble(trans_chrg);
						}
						
						if(!other_chrg.equals("null"))//other_chrg  amt
						{
							invoice_amt=invoice_amt+Double.parseDouble(other_chrg);
						}
//						if(cont_no.equals("824016"))
//						{
//							System.out.println("=====>"+invoice_amt+":"+other_chrg+":"+trans_chrg);
//						}
						
						data=invoice_amt+"";
						
						
						
						
					}
					
					
					else if(cell.getColumnIndex() == 33){	//NET_PAYABLE_AMT=((inv_amt+tax_amt)*0.1)+inv_amt+tax_amt)
						
						data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						if(data != null) {
							data = data.substring(1, data.length()-1);

							if(data.equals("Y"))
							{
//								tcs_flag=data;
								inv_amt1= (invoice_amt)*0.1/100;
								tcs_amt=inv_amt1+"";
								netpayable=inv_amt1+invoice_amt;
								data=netpayable+"";
							}
							else
							{
//								double tax_amt1=Double.parseDouble(tax);
//								data = invoice_amt+tax_amt1+"";
								data = invoice_amt+"";
//								tcs_flag="N";
							}
						}
						
					}
					else if(cell.getColumnIndex() == 55)//tcs_flag
					{
						data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						if(data != null) {
							data = data.substring(1, data.length()-1);
						}
						// if inv is in USD (IF CER FLAG : N ) THEN TCS_FLAG NULL
						tcs_flag=data;
//						if(inv_raised_in.equals("2")) {
//							if(tcs_flag.equals("N")) {
//								tcs_flag=null;
//								}
//						}
//						data=tcs_flag;
					}
					else if(cell.getColumnIndex() == 56)//tcs_amt
					{
						if(tcs_flag!=null && tcs_flag.equals("Y"))
						{
							data=tcs_amt+"";
						}
						else {
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
						}
						
					}
					else if(cell.getColumnIndex() == 58)//tcs_cd
					{
						if(tcs_flag!=null && tcs_flag.equals("Y"))
						{
							queryString = "SELECT TO_CHAR(EFF_DT, 'DD/MM/YYYY HH24:MI:SS'),TAX_STRUCT_CD  "
									+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TCS'  AND TAX_STRUCT_DTL ='TCS 0.1%' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, cd);
							stmt.setString(2, "T");
							stmt.setString(3,company_cd);
							rset = stmt.executeQuery();
							if(rset.next()) {
								tax_eff_dt=rset.getString(1);
								data = rset.getString(2);
							}
							else {
								data = null;
								tax_eff_dt=null;
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
						
					}
					else if(cell.getColumnIndex()==59)
					{
						if(tcs_flag!=null && tcs_flag.equals("Y"))
						{
							data=tax_eff_dt;
						}
						else {
							data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							if(data != null) {
								data = data.substring(1, data.length()-1);
							}
						}
						
					}
					else if(cell.getColumnIndex()==64)
					{
						if(tcs_flag!=null && tcs_flag.equals("N") && !inv_raised_in.equals("2") && !inv_raised_in.equals("null"))
						{
							
							if (Double.doubleToRawLongBits(gross) != Double.doubleToRawLongBits(0.0)) {
						  tds_Amt = (gross * Double.parseDouble("0.1")) / 100;
						 data = tds_Amt + "";
						
						    } else {
						 data = null;
						    }
						}else
						{
							data=null;
						}
						
					}
					else if(cell.getColumnIndex() == 69 && dom_buy_flag!=null && inv_flag!=null )//SYS_INV_NO-type-D,T,I
			    	{
			    		String invoice_prefix=utilBean.getInvoicePrefix(conn,"2");
			    		String invoice_seq_no=inv_pg_seq_no+"";
			    		String system_invoice_no=invoice_prefix+""+dom_buy_flag+"S"+utilBean.PrePaddingZero(invoice_seq_no, 4)+"/"+fin_year;	
			    		data = system_invoice_no;	
			    		
			    	}
					else if(cell.getColumnIndex() == 68)// TCS TDS FLAG
					{
						if(tcs_flag!=null)
						{
							if(tcs_flag.equals("Y"))
							{
								data="TCS";
							}
							else if(tcs_flag.equals("N") && !inv_raised_in.equals("2") )
							{
								data="TDS";
							}
							else
							{
								data="NA";
							}
						}
						else {
							data="NA";
						}
					}
					else {
						
						if(cell.getColumnIndex() == 12) {
							inv_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							inv_no = inv_no.substring(1,inv_no.length()-1);
						}
						if(cell.getColumnIndex() == 15) {
							start_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							start_dt = start_dt.substring(1,start_dt.length()-1);
						}
						
						else if(cell.getColumnIndex() == 16) {
							end_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							end_dt = end_dt.substring(1,end_dt.length()-1);
						}
						else if(cell.getColumnIndex() == 46) {
							fin_year = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							fin_year = fin_year.substring(1,fin_year.length()-1);
						}
						else if(cell.getColumnIndex() == 79) {
							inv_flag = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
							inv_flag = inv_flag.substring(1,inv_flag.length()-1);
						}
						
						
						data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
						if(data != null) {
							data = data.substring(1, data.length()-1);
						}
					}
					
				stmt1.setString(index++, data);
				//	System.out.println(":"+data);
					}
//				COMPANY_CD", "COUNTERPARTY_CD", "AGMT_NO", "AGMT_REV", "CONT_NO", "CONT_REV", "CONTRACT_TYPE", "PLANT_SEQ", "BU_UNIT", "FREQ",
//				"PERIOD_START_DT", "PERIOD_END_DT", "INVOICE_SEQ", "FINANCIAL_YEAR", "INV_FLAG
				queryString = "SELECT ENT_BY FROM FMS_PUR_PG_INV_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? "
						+ " AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT = ? AND PLANT_SEQ = ? "
						+ "  AND FREQ = ? AND PERIOD_START_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') AND PERIOD_END_DT = TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') "
						+ " AND FINANCIAL_YEAR = ? AND INV_FLAG = ? ";

				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.setString(2, cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, agmt_rev_no);
				stmt.setString(5, cont_no);
				stmt.setString(6, cont_rev);
				stmt.setString(7, dom_buy_flag );
				stmt.setString(8, bu_seq );
				stmt.setString(9, plant_seq );
				stmt.setString(10, freq );
				stmt.setString(11, start_dt);
				stmt.setString(12, end_dt);
				stmt.setString(13, fin_year);
				stmt.setString(14, inv_flag);
				
				rset = stmt.executeQuery();
//				COMNPANY_CD,CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,DOM_BUY_FLAG,BU_SEQ,PLANT_SEQ,INV_SEQ,FREQ,TIMESTAMP
				
				if (row.getRowNum() != 0 && !rset.next()  && !cont_no.equals("") && cd!=null  && freq != null && plant_contact != null
						&& bu_contact != null && plant_seq!=null && !inv_pg_seq_no.equals("")) {
					
					logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+bu_seq+","+plant_seq+","+inv_seq+","+freq+","+plant_contact+","+bu_contact+","+start_dt+","+end_dt+","+inv_no+","), conn,"");
					stmt1.executeUpdate();
					
					stmt1.close();
					
					logger_count++;
				}
					else {
					stmt1.close();
					skipped_count++;
					logger.data(fname,(company_cd+","+cd+", "+agmt_no+", "+agmt_rev_no+","+cont_no+", "+cont_rev+","+dom_buy_flag+","+bu_seq+","+plant_seq+","+inv_seq+","+freq+","+plant_contact+","+bu_contact+","+start_dt+","+end_dt+","+inv_no+","), conn, "E");
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
//		conn.commit();
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : "+total_count+",,,,,,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : "+logger_count+",,,,,,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA "+skipped_count+",,,,,,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>><<FMS_PUR_PG_INV_MST>>", conn);
		
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




public void FMS_PUR_PG_INV_TAX_DTL() throws IOException, SQLException {

	function_nm="FMS_PUR_PG_INV_TAX_DTL()";
	try {
		
		
		System.out.println("<<START>><<FMS_PUR_PG_INV_TAX_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_PUR_PG_INV_TAX_DTL>>,", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd = "",tax="",tax_code="";
		
		columns = "COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,INV_FLAG";
		
		queryString = "SELECT COMPANY_CD, CONTRACT_TYPE, INVOICE_SEQ, FINANCIAL_YEAR, TAX_STRUCT_CD, NULL, TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY HH24:MI:SS'), NULL,"
				+ " TAX_AMT, INVOICE_AMT, ENT_BY, TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'), NULL, NULL, INV_FLAG FROM FMS_PUR_PG_INV_MST WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		
		queryString1 = "INSERT INTO FMS_PUR_PG_INV_TAX_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,INV_FLAG) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS')"
				+ ",?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
	
		
		
		while (rset.next()) {
			stmt1 = conn.prepareStatement(queryString1);
				tax_struct_cd = rset.getString(5);
				
				for(int i = 0;i < columns.split(",").length;i++) {
					data = "";
					data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
					
					if(i == 4) {	//TAX_STRUCT_CD
						if(tax_struct_cd != null) {
							tax_struct_cd = rset.getString(5);
						}
						else {
							tax_struct_cd = "0";
						}
						data = tax_struct_cd;
					}
					else if(i == 5) {	//TAX_CODE
						if (tax_struct_cd != null) {
							if (!tax_struct_cd.equals("0")) {
								queryString2 = "SELECT TAX_CODE FROM FMS_TAX_STRUCTURE_DTL WHERE TAX_STR_CD = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, tax_struct_cd);
								rset2 = stmt2.executeQuery();
								if (rset2.next()) {
									data = rset2.getString(1);
								}
								else {
									data = "0";
								}
								rset2.close();
								stmt2.close();
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
					else if(i == 7) {	//TAX_DESCR
						if(tax_struct_cd != null) {
							if(!tax_struct_cd.equals("0")) {
								queryString2 = "SELECT DESCR FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
								stmt2 = conn.prepareStatement(queryString2);
								stmt2.setString(1, tax_struct_cd);
								rset2 = stmt2.executeQuery();
								if(rset2.next()) {
									data = rset2.getString(1);
								}
								else {
									data = null;
								}
								rset2.close();
								stmt2.close();
								}
						}
							else {
								data = null;
							}
						tax = data;
					}
					
					stmt1.setString(i+1,data);
				}
				
			//for data already exists..
			queryString5 = "SELECT COMPANY_CD FROM FMS_PUR_PG_INV_TAX_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE = ?  "
					+ "AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? AND INV_FLAG = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, tax_struct_cd);
			stmt5.setString(6, tax_code);
			stmt5.setString(7, rset.getString(15));
		
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() && tax_struct_cd != null && tax_code != null) {
				
				stmt1.executeUpdate();
				stmt1.close();
				
				logger_count++;
			}
			else {
				
				stmt1.close();
				skipped_count++; 	
			}
			stmt5.close();
			rset5.close();
		}

		
		rset.close();
		stmt.close();
		
		

		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
	
		System.out.println("<<END>><<FMS_PUR_PG_INV_TAX_DTL>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_PUR_PG_INV_TAX_DTL>>,", conn);
	
	
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
public void LOG_TRADER_CONT_MST() throws IOException, SQLException {

	function_nm="LOG_TRADER_CONT_MST()";
	try {
		
		System.out.println("<<START>><<LOG_TRADER_CONT_MST>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<LOG_TRADER_CONT_MST>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		
		columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
				+ "LOG_SEQ_NO,LOG_BY,LOG_DT,CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,"
				+ "AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,"
				+ "TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
				+ "SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,"
				+ "DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,"
				+ "RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,CONT_STATUS,TXN_CHARGE,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,DDA_DT,"
				+ "DDA_TIME,TXN_UNIT,DAY_DEF_CLAUSE,MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,"
				+ "PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
				+ "LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,"
				+ "TERMINATE_FORCE,CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK";
		
		queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
				+ "NULL,ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),CONT_NAME,CONT_REF_NO,TRADE_REF_NO,"
				+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY HH24:MI:SS'),SIGNING_TIME,"
				+ "TO_CHAR(START_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "TO_CHAR(END_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "AGMT_BASE,AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,"
				+ "VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,SPLIT_FLAG,SPLIT_TYPE,"
				+ "BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,"
				+ "SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,"
				+ "MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,"
				+ "TO_CHAR(FCC_DATE,'DD/MM/YYYY HH24:MI:SS'),REMARK,"
				+ "TO_CHAR(RENEWAL_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),ENT_BY,"
				+ "TO_CHAR(MODIFY_DT,'DD/MM/YYYY HH24:MI:SS'),MODIFY_BY,CONT_STATUS,"
				+ "TXN_CHARGE,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,"
				+ "TO_CHAR(DDA_DT,'DD/MM/YYYY HH24:MI:SS'),DDA_TIME,TXN_UNIT,DAY_DEF_CLAUSE,MEASUREMENT,"
				+ "MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,"
				+ "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,"
				+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,"
				+ "TO_CHAR(CLOSE_EFF_DT,'DD/MM/YYYY HH24:MI:SS'),CLOSURE_REMARK  "
				+ "FROM FMS_TRADER_CONT_MST  WHERE COMPANY_CD = ? ORDER BY CONT_NO ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		
		queryString1 = "INSERT INTO LOG_TRADER_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
				+ "LOG_SEQ_NO,LOG_BY,LOG_DT,CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,"
				+ "AGMT_TYPE,TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,"
				+ "SPLIT_FLAG,SPLIT_TYPE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,"
				+ "SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,"
				+ "MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,"
				+ "CONT_STATUS,TXN_CHARGE,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,DDA_DT,DDA_TIME,TXN_UNIT,DAY_DEF_CLAUSE,"
				+ "MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,"
				+ "OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
				+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,CLOSURE_REQUEST_FLAG,"
				+ "CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'), "
				+ "?,?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?, "
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?)";
		
		
		int log_seq_no = 1;
		String lastContNo = "";
		
		while (rset.next()) {
				stmt1 = conn.prepareStatement(queryString1);
				
				for(int i = 0;i < columns.split(",").length;i++) {
					data = "";
					data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
					
					if(i == 7) {
						String currentContNo = rset.getString(5);
					    if (!currentContNo.equals(lastContNo)) {
					 log_seq_no = 1;
					 lastContNo = currentContNo;
					    }else {
					    	log_seq_no++;
					    }
					    data = log_seq_no+"";
					}
					
//					System.out.println((i+1)+":"+data);
					
					stmt1.setString(i+1,data);
				}
//				System.out.println(log_seq_no);
			//for data already exists..
//				"COMPANY_CD", "COUNTERPARTY_CD", "AGMT_NO", "AGMT_REV", "CONT_NO", "CONT_REV", "CONTRACT_TYPE", "LOG_SEQ_NO")
			queryString5 = "SELECT COUNTERPARTY_CD FROM LOG_TRADER_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_NO=? AND AGMT_REV = ?   AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND LOG_SEQ_NO = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(5));
			stmt5.setString(6, rset.getString(6));
			stmt5.setString(7, rset.getString(7));
			stmt5.setInt(8, log_seq_no);
		
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
	
				stmt1.executeUpdate();
				stmt1.close();
				
				logger_count++;
			}
			else {
				
				stmt1.close();
				skipped_count++; 
		
			}
			stmt5.close();
			rset5.close();
			}
		rset.close();
		stmt.close();
		
		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
		System.out.println("<<END>><<LOG_TRADER_CONT_MST>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<LOG_TRADER_CONT_MST>>,", conn);
		
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


public void LOG_TRADER_CN_MST() throws IOException, SQLException {

	function_nm="LOG_TRADER_CN_MST()";
	try {
		
		System.out.println("<<START>><<LOG_TRADER_CN_MST>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<LOG_TRADER_CN_MST>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		
		columns = "COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, AGMT_BASE, "
				+ "AGMT_TYP, CONTRACT_TYPE, LOG_SEQ_NO, LOG_BY, LOG_DT, CONT_NO, CONT_REV, "
				+ "CONT_NAME, CONT_REF_NO, CONT_STATUS, DDA_DT, DDA_TIME, SIGNING_DT, SIGNING_TIME,"
				+ " START_DT, END_DT, ENT_DT, ENT_BY, MODIFY_DT, MODIFY_BY, REV_DT, NUM_CARGO,"
				+ " DAY_DEF_FLAG, DAY_START_TIME, DAY_END_TIME, DEMURRAGE_RATE, DEMURRAGE_RATE_UNIT, "
				+ "ALW_LAYTIME_HRS, ALW_LAYTIME_MNS, MEASUREMENT, MEAS_STANDARD, MEAS_TEMPERATURE, "
				+ "PRESSURE_MIN_BAR, PRESSURE_MAX_BAR, OFF_SPEC_GAS, SPEC_GAS_ENERGY_BASE, SPEC_GAS_MIN_ENERGY, "
				+ "SPEC_GAS_MAX_ENERGY, LIABILITY, LIABILITY_CLAUSE, BILLING_FLAG, BILLING_CLAUSE, TERMINATE_FLAG,"
				+ " TERMINATE_CLAUSE, TERMINATE_PLANED, TERMINATE_FORCE, DEMURRAGE, DAY_DEF_CLAUSE, DEMURRAGE_CLAUSE,"
				+ " MEASUREMENT_CLAUSE, OFF_SPEC_GAS_CLAUSE, FCC_FLAG, FCC_BY, FCC_DATE ";
		
		queryString = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, AGMT_BASE, AGMT_TYP, CONTRACT_TYPE, "
				+ "NULL, ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'), CONT_NO, CONT_REV, CONT_NAME,"
				+ " CONT_REF_NO, CONT_STATUS, TO_CHAR(DDA_DT, 'DD/MM/YYYY HH24:MI:SS'), DDA_TIME, "
				+ "TO_CHAR(SIGNING_DT, 'DD/MM/YYYY HH24:MI:SS'), SIGNING_TIME, TO_CHAR(START_DT, 'DD/MM/YYYY HH24:MI:SS'),"
				+ " TO_CHAR(END_DT, 'DD/MM/YYYY HH24:MI:SS'), TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'), ENT_BY, "
				+ "TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'), MODIFY_BY, TO_CHAR(REV_DT, 'DD/MM/YYYY HH24:MI:SS'),"
				+ " NUM_CARGO, DAY_DEF_FLAG, DAY_START_TIME, DAY_END_TIME ,"
				+ " DEMURRAGE_RATE, DEMURRAGE_RATE_UNIT, ALW_LAYTIME_HRS, ALW_LAYTIME_MNS, MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,"
				+ " PRESSURE_MIN_BAR, PRESSURE_MAX_BAR, OFF_SPEC_GAS, "
				+ "SPEC_GAS_ENERGY_BASE, SPEC_GAS_MIN_ENERGY, SPEC_GAS_MAX_ENERGY, LIABILITY, LIABILITY_CLAUSE, BILLING_FLAG, "
				+ "BILLING_CLAUSE, TERMINATE_FLAG, TERMINATE_CLAUSE, TERMINATE_PLANED, TERMINATE_FORCE,DEMURRAGE,DAY_DEF_CLAUSE,DEMURRAGE_CLAUSE,"
				+ "MEASUREMENT_CLAUSE, OFF_SPEC_GAS_CLAUSE ,FCC_FLAG, FCC_BY, "
				+ "TO_CHAR(FCC_DATE, 'DD/MM/YYYY HH24:MI:SS') "
				+ "FROM FMS_TRADER_CN_MST  WHERE COMPANY_CD = ? ORDER BY CONT_NO ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		
		queryString1 = "INSERT INTO LOG_TRADER_CN_MST(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE, AGMT_NO, AGMT_REV, "
				+ "AGMT_BASE, AGMT_TYP, CONTRACT_TYPE, LOG_SEQ_NO, "
				+ "LOG_BY, LOG_DT, CONT_NO, CONT_REV, CONT_NAME, CONT_REF_NO, "
				+ "CONT_STATUS, DDA_DT, DDA_TIME, SIGNING_DT, SIGNING_TIME, START_DT,"
				+ " END_DT, ENT_DT, ENT_BY, MODIFY_DT, MODIFY_BY, REV_DT, NUM_CARGO, "
				+ "DAY_DEF_FLAG, DAY_START_TIME, DAY_END_TIME, DEMURRAGE_RATE, DEMURRAGE_RATE_UNIT,"
				+ " ALW_LAYTIME_HRS, ALW_LAYTIME_MNS, MEASUREMENT, MEAS_STANDARD, MEAS_TEMPERATURE, "
				+ "PRESSURE_MIN_BAR, PRESSURE_MAX_BAR, OFF_SPEC_GAS, SPEC_GAS_ENERGY_BASE, SPEC_GAS_MIN_ENERGY, "
				+ "SPEC_GAS_MAX_ENERGY, LIABILITY, LIABILITY_CLAUSE, BILLING_FLAG, BILLING_CLAUSE, TERMINATE_FLAG, "
				+ "TERMINATE_CLAUSE, TERMINATE_PLANED, TERMINATE_FORCE, DEMURRAGE, DAY_DEF_CLAUSE, DEMURRAGE_CLAUSE, "
				+ "MEASUREMENT_CLAUSE, OFF_SPEC_GAS_CLAUSE, FCC_FLAG, FCC_BY, FCC_DATE ) "
				+ "VALUES(?,?,?,?,?,?,?,?,"
				+ "?,?,TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ,?,?,?,?,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS') ,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS')  )";
		
		
		int log_seq_no = 1;
		String lastContNo = "";
		
		while (rset.next()) {
				stmt1 = conn.prepareStatement(queryString1);
				
				for(int i = 0;i < columns.split(",").length;i++) {
					data = "";
					data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
					
					if(i == 8) {
						String currentContNo = rset.getString(12);
					    if (!currentContNo.equals(lastContNo)) {
					 log_seq_no = 1;
					 lastContNo = currentContNo;
					    }else {
					    	log_seq_no++;
					    }
					    data = log_seq_no+"";
					}
					
//					System.out.println((i+1)+":"+data);
					
					stmt1.setString(i+1,data);
				}

			//for data already exists..
//"COMPANY_CD", "COUNTERPARTY_CD", "AGMT_NO", "AGMT_REV", "CONT_NO", "CONT_REV", "CONTRACT_TYPE", "AGMT_TYPE", "LOG_SEQ_NO")
			queryString5 = "SELECT COUNTERPARTY_CD FROM LOG_TRADER_CN_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_NO=? AND AGMT_REV = ?   AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE =?  AND LOG_SEQ_NO = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(4));
			stmt5.setString(4, rset.getString(5));
			stmt5.setString(5, rset.getString(12));
			stmt5.setString(6, rset.getString(13));
			stmt5.setString(7, rset.getString(8));
			stmt5.setString(8, rset.getString(3));
			stmt5.setInt(9,log_seq_no);
		
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
			
				stmt1.executeUpdate();
				stmt1.close();
				
				logger_count++;
			}
			else {
				
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
		}
		rset.close();
		stmt.close();
		
		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
		System.out.println("<<END>><<LOG_TRADER_CN_MST>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<LOG_TRADER_CN_MST>>,", conn);
		
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

public void LOG_TRADER_CARGO_MST() throws IOException, SQLException {

	function_nm="LOG_TRADER_CARGO_MST()";
	try {
		
		System.out.println("<<START>><<LOG_TRADER_CARGO_MST>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<LOG_TRADER_CARGO_MST>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		
		columns = "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,"
				+ "CONTRACT_TYPE,CONT_NO,CARGO_NO,LOG_SEQ_NO,LOG_BY,LOG_DT,"
				+ "CARGO_REF,CARGO_STATUS,CARGO_QTY,RATE,RATE_UNIT,START_DT,END_DT,ENT_DT,ENT_BY"
				+ ",MODIFY_DT,MODIFY_BY,AGMT_REV,CONT_REV,TOLERANCE";

		
		queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,"
				+ " CONTRACT_TYPE,CONT_NO,CARGO_NO,NULL,ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),"
				+ "CARGO_REF,CARGO_STATUS,CARGO_QTY,RATE,RATE_UNIT,TO_CHAR(START_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(END_DT, 'DD/MM/YYYY HH24:MI:SS'),"
				+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),ENT_BY,"
				+ "TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'),MODIFY_BY,AGMT_REV,CONT_REV,TOLERANCE "
				+ "FROM FMS_TRADER_CARGO_MST  WHERE COMPANY_CD = ? ORDER BY CONT_NO ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		
		queryString1 = "INSERT INTO LOG_TRADER_CARGO_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,"
				+ "CONTRACT_TYPE,CONT_NO,CARGO_NO,LOG_SEQ_NO,LOG_BY,LOG_DT,CARGO_REF,CARGO_STATUS,CARGO_QTY,"
				+ "RATE,RATE_UNIT,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,AGMT_REV,CONT_REV,TOLERANCE ) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,"
				+ "TO_DATE(?,'DD/MM/YYYY HH24:MI:SS'),?,?,?,?)";
		
		
		int log_seq_no = 1;
		String lastContNo = "";
		
		while (rset.next()) {
				stmt1 = conn.prepareStatement(queryString1);
				
				for(int i = 0;i < columns.split(",").length;i++) {
					data = "";
					data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
					
					if(i == 7) {
						String currentContNo = rset.getString(6);
					    if (!currentContNo.equals(lastContNo)) {
					 log_seq_no = 1;
					 lastContNo = currentContNo;
					    }else {
					    	log_seq_no++;
					    }
					    data = log_seq_no+"";
					}
					
//					System.out.println((i+1)+":"+data);
					
					stmt1.setString(i+1,data);
				}
//				("COMPANY_CD", "COUNTERPARTY_CD", "AGMT_NO", "CONTRACT_TYPE", "CONT_NO", "CARGO_NO", "AGMT_REV", "CONT_REV", "AGMT_TYPE", "LOG_SEQ_NO")
			queryString5 = "SELECT COUNTERPARTY_CD FROM LOG_TRADER_CARGO_MST "
					+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					+ "AND AGMT_NO=?   AND CONTRACT_TYPE = ? AND CONT_NO =?  "
					+ "AND CARGO_NO = ? AND AGMT_REV = ? AND CONT_REV = ? AND AGMT_TYPE = ?  AND LOG_SEQ_NO=?  ";

			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(4));
			stmt5.setString(4, rset.getString(5));
			stmt5.setString(5, rset.getString(6));
			stmt5.setString(6, rset.getString(7));
			stmt5.setString(7, rset.getString(22));
			stmt5.setString(8, rset.getString(23));
			stmt5.setString(9, rset.getString(3));
			stmt5.setInt(10,log_seq_no);
		
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
			
				stmt1.executeUpdate();
				stmt1.close();
				
				logger_count++;
			}
			else {
				
				stmt1.close();
				skipped_count++; 
			}
			stmt5.close();
			rset5.close();
		}
		rset.close();
		stmt.close();
		
		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
		System.out.println("<<END>><<LOG_TRADER_CARGO_MST>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<LOG_TRADER_CARGO_MST>>,", conn);
		
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

