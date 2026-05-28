package com.etrm.fms.migration;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.sales_invoice.DataBean_Sales_Invoice;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;


public class Sales_SEIPL_Data_Reader {

	/*public static void main(String[] args) 
	{
		Sales_Excel_Reader ex = new Sales_Excel_Reader();
		ex.init();
	}
	
	}
	
	class Sales_Excel_Reader {*/

	String db_src_file_name="Sales_SEIPL_Data_Reader.java";

	String dbline = "", username = "", encrypted = "", password = "";
	String migration_setup_dir = "";
	String pdf_path = "";
	
	String queryString="", queryString1="",queryString2="",queryString3="",queryString4="",queryString5="",query_insert="";
	Connection conn;
	ResultSet rset,rset1,rset2,rset3,rset4,rset5,rset_insert;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt5,stmt7,stmt_insert;
	
	String data = "", table_name = "", function_nm = "", columns = "", company_cd = "2", sale_price="", sale_price_unit="", alloc_dt="",cargo_no="",mod_seq="",name="";
	int num = 1, index = 0;

	String agmt_no = "", agmt_rev = "", trans_cd = "", seq_no = "", agmt_type = "",cont_no="",cont_rev="",state_code="",eff_dt="",contract_type="",fin_yr="",inv_seq="",chrg_abbr="",cont_cd="",addr_flag="";
	
	String abbr = "", cd = "1",cont_type="",plant_seq_no="",nom_rev_no="",gas_dt="",bu_seq_no="",trans_seq = "",cont_ref = "",cont_name="",agmt_base="",bu_cont_person_cd="",new_inv="";
	
	String sysDateTime = "",a_typ="",price_req ="",price_appr="",exchg_cd="",sec_ref_no="",buy_sale="C",cargo_ref="",tax_cd="",bu_plant_state="",agmt_ref="",pro_abbr="",pro_cd="",mole_cd="";
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
			
			fname = "DataLogs/Reader/Sales_SEIPL_Data_Reader(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/Reader/Sales_SEIPL_Data_Reader_Error(log)"+sysDateTime+".csv";
			
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
					
					if (checked_values.contains("FMS_AGMT_MST,")) {	
						FMS_AGMT_MST();
					}
					if (checked_values.contains("FMS_AGMT_TRANSPTR,")) {
						FMS_AGMT_TRANSPTR();
					}
					if (checked_values.contains("FMS_AGMT_PLANT,")) {
						FMS_AGMT_PLANT();
					}
					if (checked_values.contains("FMS_AGMT_BU,")) {
						FMS_AGMT_BU();
					}
					if (checked_values.contains("FMS_SUPPLY_AGMT_LIABILITY,")) { 
						FMS_SUPPLY_AGMT_LIABILITY();
					}
					if (checked_values.contains("FMS_AGMT_BILLING_DTL,")) {
						FMS_AGMT_BILLING_DTL();
					}
					if (checked_values.contains("FMS_AGMT_DEACTIVATION_DTL,")) {
						FMS_AGMT_DEACTIVATION_DTL();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_MST,")) { 
						FMS_SUPPLY_CONT_MST(); 					
					}
					if (checked_values.contains("LOG_SUPPLY_CONT_MST,")) {
						LOG_SUPPLY_CONT_MST();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_TRANSPTR,")) { 
						FMS_SUPPLY_CONT_TRANSPTR();				
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_PLANT,")) { 
						FMS_SUPPLY_CONT_PLANT(); 					
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_BU,")) { 
						FMS_SUPPLY_CONT_BU(); 					
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_LIABILITY,")) { 
						FMS_SUPPLY_CONT_LIABILITY(); 					
					}
					if (checked_values.contains("FMS_SUPPLY_BILLING_DTL,")) { 
						FMS_SUPPLY_BILLING_DTL();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_DCQ_DTL,")) {
						FMS_SUPPLY_CONT_DCQ_DTL();
					}
					if (checked_values.contains("FMS_SUPPLY_CONT_PLANT_CHRG,")) {
						FMS_SUPPLY_CONT_PLANT_CHRG();
					}
					if (checked_values.contains("FMS_SUPPLY_PURCHASE_MAP_DTL,")) {
						FMS_SUPPLY_PURCHASE_MAP_DTL();
					}
					if (checked_values.contains("FMS_SUPPLY_ALLOC_REVISED,")) {
						FMS_SUPPLY_ALLOC_REVISED();
					}
					if (checked_values.contains("FMS_LTCORA_AGMT_MST,")) { 
						FMS_LTCORA_AGMT_MST();
						FMS_LTCORA_AGMT_BU();
					}
					if (checked_values.contains("FMS_LTCORA_AGMT_TRANSPTR,")) { 
						FMS_LTCORA_AGMT_TRANSPTR();
					}
					if (checked_values.contains("FMS_LTCORA_AGMT_PLANT,")) {
						FMS_LTCORA_AGMT_PLANT();
					}
					if (checked_values.contains("FMS_LTCORA_AGMT_LIABILITY,")) { 
						FMS_LTCORA_AGMT_LIABILITY();
					}
					if (checked_values.contains("FMS_LTCORA_AGMT_BILLING_DTL,")) {
						FMS_LTCORA_AGMT_BILLING_DTL();
					}
					if (checked_values.contains("FMS_LTCORA_CONT_MST,")) {
						FMS_LTCORA_CONT_MST();
						FMS_LTCORA_CONT_BU();
					}
					if (checked_values.contains("LOG_LTCORA_CONT_MST,")) { 
						LOG_LTCORA_CONT_MST();
					}
					if (checked_values.contains("FMS_LTCORA_CONT_TRANSPTR,")) { 
						FMS_LTCORA_CONT_TRANSPTR();				
					}
					if (checked_values.contains("FMS_LTCORA_CONT_PLANT,")) { 
						FMS_LTCORA_CONT_PLANT(); 					
					}
					if (checked_values.contains("FMS_LTCORA_CONT_LIABILITY,")) { 
						FMS_LTCORA_CONT_LIABILITY(); 					
					}
					if (checked_values.contains("FMS_LTCORA_CONT_BILLING_DTL,")) { 
						FMS_LTCORA_CONT_BILLING_DTL();
					}
					if (checked_values.contains("FMS_LTCORA_CONT_STRG_CRG,")) { 
						FMS_LTCORA_CONT_STRG_CRG();
					}
					if (checked_values.contains("FMS_LTCORA_CONT_CARGO_DTL,")) { 
						FMS_LTCORA_CONT_CARGO_DTL();
						LOG_LTCORA_CONT_CARGO_DTL();
					}
					if (checked_values.contains("FMS_LTCORA_CONT_CARGO_ADQ,")) { 
						FMS_LTCORA_CONT_CARGO_ADQ();
					}
					if (checked_values.contains("FMS_LTCORA_CONT_CARGO_CSOC,")) { 
						FMS_LTCORA_CONT_CARGO_CSOC();
					}
					if (checked_values.contains("FMS_LTCORA_CONT_CARGO_MOD,")) { 
						FMS_LTCORA_CONT_CARGO_MOD();
					}
					if (checked_values.contains("FMS_CONT_PRICE_DTL(S),")) { 
						FMS_CONT_PRICE_DTL();
					}
					if (checked_values.contains("FMS_CONT_PRICE_MIN_DTL(S),")) {
						FMS_CONT_PRICE_MIN_DTL();
					}
					if (checked_values.contains("FMS_SECURITY_DEAL_MAP(S),")) {
						FMS_SECURITY_DEAL_MAP();
					}
					if (checked_values.contains("FMS_SECURITY_MST(S),")) {
						FMS_SECURITY_MST();
					}				
					if (checked_values.contains("LOG_FMS_SECURITY_MST(S),")) {
						LOG_FMS_SECURITY_MST();
					}
					if (checked_values.contains("FMS_DAILY_BUYER_NOM,")) {
						FMS_DAILY_BUYER_NOM();
					}
					if (checked_values.contains("FMS_DAILY_BUYER_NOM_DTL,")) {
						FMS_DAILY_BUYER_NOM_DTL();
					}	
					if (checked_values.contains("FMS_DAILY_SELLER_NOM,")) {
						FMS_DAILY_SELLER_NOM();
					}
					if (checked_values.contains("FMS_DAILY_SELLER_NOM_DTL,")) {
						FMS_DAILY_SELLER_NOM_DTL();
					}
					if (checked_values.contains("FMS_METER_TICKET_READING,")) { 
						FMS_METER_TICKET_READING();
					}
//					if (checked_values.contains("FMS_METER_TICKET_READING_TEMP,")) { 
//						FMS_METER_TICKET_READING_TEMP();
//					}
					if (checked_values.contains("FMS_DAILY_ALLOCATION_DTL,")) { 
						FMS_DAILY_ALLOCATION_DTL();
					}
					if (checked_values.contains("FMS_DAILY_ALLOCATION_DTL_CT,")) { 
						FMS_DAILY_ALLOCATION_DTL_CT();
					}
					if (checked_values.contains("FMS_INVOICE_MST,")) { 
						FMS_INVOICE_MST();
						FMS_INV_TAX_DTL();
					}
					if (checked_values.contains("FMS_INVOICE_MST_UPDATE,")) { 
						FMS_INVOICE_MST_UPDATE();
					}
					if (checked_values.contains("FMS_INVOICE_MST_TAXCD_UPDATE,")) { 
						FMS_INVOICE_MST_TAXCD_UPDATE();
						FMS_INVOICE_MST_GROSSAMT_UPDATE();
						FMS_INV_TAX_DTL_UPDATE();
					}
					if (checked_values.contains("FMS_INVOICE_DTL,")) { 
						FMS_INVOICE_DTL();
					}
					if (checked_values.contains("FMS_INVOICE_IRN_DTL,")) { 
						FMS_INVOICE_IRN_DTL();
					}
					if (checked_values.contains("FMS_INV_STORAGE_CRG_DTL,")) { 
						FMS_INV_STORAGE_CRG_DTL();
					}
					if (checked_values.contains("FMS_FFLOW_INV_MST,")) { 
						FMS_FFLOW_INV_MST();
						FMS_FFLOW_INV_TAX_DTL();
					}
					if (checked_values.contains("FMS_FFLOW_INV_MST_UPDATE,")) { 
						FMS_FFLOW_INV_MST_UPDATE();
					}
					if (checked_values.contains("FMS_FFLOW_INV_DTL,")) { 
						FMS_FFLOW_INV_DTL();
					}
					if (checked_values.contains("FMS_INV_FILE_DTL,")) { 
						FMS_INV_FILE_DTL();
					}
					if (checked_values.contains("FMS_INV_FILE_DTL_UPDATE,")) { 
						FMS_INV_FILE_DTL_UPDATE();
					}
					if (checked_values.contains("FMS_FFLOW_INV_FILE_DTL,")) { 
						FMS_FFLOW_INV_FILE_DTL();
					}
					if (checked_values.contains("FMS_FFLOW_INV_FILE_DTL_UPDATE,")) { 
						FMS_FFLOW_INV_FILE_DTL_UPDATE();
					}
					if (checked_values.contains("FMS_INV_ADV_DTL,")) { 
						FMS_INV_ADV_DTL();
						FMS_SECURITY_TAX_DTL();
					}
					if (checked_values.contains("FMS_INV_PAY_RECV_DTL,")) { 
						FMS_INV_PAY_RECV_DTL();
					}
                    if (checked_values.contains("FMS_INVOICE_MST_CR_DR,")) 
					{ 
	                    FMS_INVOICE_MST_CR_DR();
	                    FMS_INV_TAX_DTL_CR_DR();
					}
					if (checked_values.contains("FMS_INV_CRDR_REF,")) 
					{ 
                        FMS_INV_CRDR_REF();
                        FMS_INV_CRDR_TAX_DTL();
					}
					if (checked_values.contains("FMS_INV_FILE_DTL_CR_DR,")) 
					{ 
						FMS_INV_FILE_DTL_CR_DR();
					}
					if (checked_values.contains("FMS_INVOICE_MST_LP,")) 
					{ 
						FMS_INVOICE_MST_LP();
						FMS_INV_TAX_DTL_LP();
					}
					if (checked_values.contains("FMS_INV_FILE_DTL_LP,")) 
					{ 
						FMS_INV_FILE_DTL_LP();
					}
					if (checked_values.contains("FMS_INV_ADV_DTL_UPDATE,")) 
					{ 
						FMS_INV_ADV_DTL_UPDATE();
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
				if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(rset_insert != null){try{rset_insert.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if(stmt_insert != null){try{stmt_insert.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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

	public void FMS_AGMT_MST() throws IOException, SQLException {

		function_nm="FMS_AGMT_MST()";
		try {
			
			table_name = "FMS_AGMT_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  

			/*queryString1 = "INSERT INTO FMS_AGMT_MST VALUES(";
			
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";*/
			
			queryString1 = "INSERT INTO FMS_AGMT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,SIGNING_DT,START_DT,END_DT,RENEWAL_DT,AGMT_BASE,AGMT_TYP,STATUS,BUYER_NOM_CLAUSE,BUYER_NOM,BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_CLAUSE,SELLER_NOM,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_PERCENTAGE,MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,FLAG,REV_DT,REMARKS,LIABILITY_CLAUSE,BILLING_CLAUSE,LC_CLAUSE,RENEWAL_FLAG,PRE_APPROVAL_DATE,PRE_APPROVAL,PRE_APPROVAL_BY,REOPEN_REQUEST_FLAG,REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVAL_DT,REOPEN_REQUEST_BY,REOPEN_APPROVE_BY,REMARK,CONT_NAME,AGMT_REF_NO,BILLING_FLAG,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,BUYER_NOM_CUTOFF,MEAS_CLAUSE,SPEC_CLAUSE,LIABILITY,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE) VALUES(?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					agmt_no = ""; agmt_rev = "";
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
						/*else if (cell.getColumnIndex() == 36 || cell.getColumnIndex() == 49 || cell.getColumnIndex() == 54) {	// Emp_Cd
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
				    	else if (cell.getColumnIndex() == 58) {	// AGMT_REF_NO
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    		data = data.split("-")[0].substring(0,1) + cd + "-" + data.split("-")[1];
					    	}
					    	
				    	}
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// agmt_type
				    			agmt_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_type != null) {
									agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 3) {	// agmt_no
				    			agmt_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_no != null) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 4) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_TYPE = 'F' AND AGMT_REV = ?  ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	rset = stmt.executeQuery();
			    	
				    if (!rset.next() && !cd.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + (cd+":"+abbr) + "," + agmt_type + "," + agmt_rev + "," + agmt_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + (cd+":"+abbr) + " , " + agmt_type + "," + agmt_rev + "," + agmt_no + ","), conn, "E");
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

	public void FMS_AGMT_TRANSPTR() throws IOException, SQLException {

		function_nm="FMS_AGMT_TRANSPTR()";
		try {
			
			table_name = "FMS_AGMT_TRANSPTR";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			/*queryString1 = "INSERT INTO FMS_AGMT_TRANSPTR VALUES(";
			
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";*/
			queryString1 = "INSERT INTO FMS_AGMT_TRANSPTR(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_TRANSPTR_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_TRANSPTR_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					agmt_no = ""; agmt_rev = ""; trans_cd = ""; seq_no = "";
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
				    	else if (cell.getColumnIndex() == 5) {	// TRANSPORTER 
				    		 //trans_abbr
				    		abbr = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (abbr != null) {
				    			abbr = abbr.substring(1, abbr.length() - 1);

					    		if(abbr.contains("GAIL DAHEJ") || abbr.contains("GAILHAZSEI") || abbr.contains("GAIL HAZ") || 
					    				abbr.contains("PIL HAZ") || abbr.contains("PIL BHAD") || abbr.contains("PIL ANK") || 
					    				abbr.contains("PIL MSK") || abbr.contains("GAIL HAZ") || abbr.contains("GAIL DAHEJ") 
					    				|| abbr.contains("RGPL SHAHD")) {
						    		queryString = "SELECT A.COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
						    				+ "WHERE B.PLANT_ABBR = ? AND B.ENTITY = 'R' AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1,abbr);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			trans_cd = rset.getString(1);
						    		} else {
						    			trans_cd = "";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}else {
					    			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1,abbr );
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			trans_cd = rset.getString(1);
						    		} else {
						    			trans_cd = "";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
				    		}
				    		data = trans_cd;

				    	}
						/*else if (cell.getColumnIndex() == 8) {	// Emp_Cd
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
				    			if (agmt_no != null) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 4) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 6) {	// PLANT_SEQ_NO
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_TRANSPTR WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_TYPE = 'F' AND AGMT_REV = ? AND TRANSPORTER_CD = ? AND PLANT_SEQ_NO = ?  ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, trans_cd);
			    	stmt.setString(6, seq_no);
			    	rset = stmt.executeQuery();
			    	
				    if (!rset.next() && !cd.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," +  (cd) + "," + agmt_type + "," + agmt_rev + "," + agmt_no + "," + trans_cd + "," + seq_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," +  (cd) + "," + agmt_type + "," + agmt_rev + "," + agmt_no + "," + trans_cd + "," + seq_no + ","), conn, "E");
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

	public void FMS_AGMT_PLANT() throws IOException, SQLException {

		function_nm="FMS_AGMT_PLANT()";
		try {
			
			table_name = "FMS_AGMT_PLANT";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			/*queryString1 = "INSERT INTO FMS_AGMT_PLANT VALUES(";
			
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";*/
			queryString1 = "INSERT INTO FMS_AGMT_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,SPLIT_VALUE,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_PLANT_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_PLANT_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ABBR,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					agmt_no = ""; agmt_rev = ""; trans_cd = ""; seq_no = "";
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
				    			cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 7) {	// Emp_Cd
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
				    			if (agmt_no != null) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 4) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 5) {	// PLANT_SEQ_NO
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_TYPE = 'F' AND AGMT_REV = ? AND  PLANT_SEQ_NO = ?  ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, seq_no);
			    	rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + " , " + abbr  + "," + agmt_rev + "," + agmt_no + "," + seq_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + " , " + abbr  + "," + agmt_rev + "," + agmt_no + "," + seq_no + ","), conn, "E");
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
			//conn.commit();
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

	public void FMS_AGMT_BU() throws IOException, SQLException {

		function_nm="FMS_AGMT_BU()";
		try {
			
			table_name = "FMS_AGMT_BU";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			/*queryString1 = "INSERT INTO FMS_AGMT_BU VALUES(";
			
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";*/
			queryString1 = "INSERT INTO FMS_AGMT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_BU_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_BU_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					agmt_no = ""; agmt_rev = ""; seq_no = "";
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
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 7) {	// Emp_Cd
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
				    			if (agmt_no != null) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 4) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 5) {	// PLANT_SEQ_NO
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_TYPE = 'F' AND AGMT_REV = ? AND  PLANT_SEQ_NO = ?  ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, seq_no);
			    	rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + " , " + abbr + "," + agmt_type + "," + agmt_rev + "," + agmt_no + "," + seq_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + " , " + abbr + "," + agmt_type + "," + agmt_rev + "," + agmt_no + "," + seq_no + ","), conn, "E");
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
			//conn.commit();
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
	
	public void FMS_SUPPLY_AGMT_LIABILITY() throws IOException, SQLException {
		
		function_nm="FMS_SUPPLY_AGMT_LIABILITY()";
		try {
			
			table_name = "FMS_SUPPLY_AGMT_LIABILITY";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String ld_promise = "",top_promise="";
			
			queryString1 = "INSERT INTO FMS_SUPPLY_AGMT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,"
					+ "LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,"
					+ "TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,"
					+ "MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_AGMT_LIABILITY_"+start_end_dt+".xlsx");
			
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_AGMT_LIABILITY_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++; 
					agmt_no = ""; agmt_rev = ""; seq_no = "";
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
						
				    	if (cell.getColumnIndex() == 0) {
				    		
				    		abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (abbr != null) {
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
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	
				    	else if(cell.getColumnIndex() == 7) {
				    		ld_promise = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!ld_promise.equals("NULL")) {
								ld_promise = ld_promise.substring(1, ld_promise.length() - 1);
							}
							if("Daily".equals(ld_promise)) {
				    			ld_promise = "D";
				    		}
				    		else if("Weekly".equals(ld_promise)) {
				    			ld_promise = "W";
				    		}
				    		else if("Fortnightly".equals(ld_promise)) {
				    			ld_promise = "F";
				    		}
				    		else if("Monthly".equals(ld_promise)) {
				    			ld_promise = "M";
				    		}
				    		else if("Quarterly".equals(ld_promise)) {
				    			ld_promise = "Q";
				    		}
				    		else if("Invoice Cycle".equals(ld_promise)) {
				    			ld_promise = "IC";
				    		}
				    		else if("TCQ".equals(ld_promise)) {
				    			ld_promise = "T";
				    		}
				    		else if("Defined Period".equals(ld_promise)) {
				    			ld_promise = "DP";
				    		}
				    		else if("Supply Period".equals(ld_promise)) {
				    			ld_promise = "SP";
				    		}else {
				    			ld_promise = null;
				    		}
				    		
				    		data = ld_promise;
				    	}
				    	else if(cell.getColumnIndex() == 13) {
				    		top_promise = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!top_promise.equals("NULL")) {
				    			top_promise = top_promise.substring(1, top_promise.length() - 1);
							}
				    		
				    		if("Daily".equals(top_promise)) {
				    			top_promise = "D";
				    		}
				    		else if("Weekly".equals(top_promise)) {
				    			top_promise = "W";
				    		}
				    		else if("Fortnightly".equals(top_promise)) {
				    			top_promise = "F";
				    		}
				    		else if("Monthly".equals(top_promise)) {
				    			top_promise = "M";
				    		}
				    		else if("Quarterly".equals(top_promise)) {
				    			top_promise = "Q";
				    		}
				    		else if("Invoice Cycle".equals(top_promise)) {
				    			top_promise = "IC";
				    		}
				    		else if("TCQ".equals(top_promise)) {
				    			top_promise = "T";
				    		}
				    		else if("Defined Period".equals(top_promise)) {
				    			top_promise = "DP";
				    		}
				    		else if("Supply Period".equals(top_promise)) {
				    			top_promise = "SP";
				    		}
				    		else {
				    			top_promise = null;
				    		}
				    		
				    		data = top_promise;
				    	}
				    	
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// agmt_type
				    			agmt_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_type != null) {
				    				agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 3) {	// agmt_no
				    			agmt_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_no != null) {
				    				agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 4) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
				    			    						    		

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}	
				    	//System.out.println(index+"-"+data);
				    	stmt1.setString(index++, data);		    	
				    }
					
				    queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_AGMT_LIABILITY WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND AGMT_TYPE = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);	
			    	stmt.setString(5, agmt_type);	

		    	
			    	rset = stmt.executeQuery();
			    	
			    	 if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + cd + " , "  + agmt_type + " , " + agmt_no + "," + agmt_rev + "," + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
					}
			    	 else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+"," + cd + " , "  + agmt_type + " , " + agmt_no + "," + agmt_rev + "," + ","), conn, "E");
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

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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
	
	
	public void FMS_AGMT_BILLING_DTL() throws IOException, SQLException {

		function_nm="FMS_AGMT_BILLING_DTL()";
		try {
			
			table_name = "FMS_AGMT_BILLING_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			/*queryString1 = "INSERT INTO FMS_AGMT_BILLING_DTL VALUES(";
			
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";*/
			queryString1 = "INSERT INTO FMS_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,BILLING_FREQ,"
					+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
					+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,"
					+ "ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_BILLING_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_BILLING_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					agmt_no = ""; agmt_rev = ""; agmt_type="";
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
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else if (cell.getColumnIndex() == 2) {	// agmt_type
			    			agmt_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (agmt_type != null) {
			    				agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
							}
			    			data = agmt_type;
			    		}
				    	else if (cell.getColumnIndex() == 3) {	// agmt_no
			    			agmt_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
			    			data = agmt_no;
			    		}
				    	else if (cell.getColumnIndex() == 4) {	// agmt_rev
			    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (agmt_rev != null) {
								agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
							}
			    			data = agmt_rev;
			    		}	
				    	else if(cell.getColumnIndex() == 6) {
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name != null) {
								name = name.substring(1, name.length() - 1);
								
								if(name.equals("T")) {
					    			name = "T";
					    		}else if(name.equals("B")) {
					    			name = "B";
					    		}else if(name.equals("Y")) {
					    			name = null;
					    		}
							}
				    		data = name;
				    	}
				    	else if(cell.getColumnIndex() == 11) {
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name != null) {
								name = name.substring(1, name.length() - 1);
							}
							queryString = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) = ? ";
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, name);
					    	rset = stmt.executeQuery();
					    	if (rset.next()) {
					    		exchg_cd = rset.getString(1);
					    	}else {
					    		exchg_cd =  null;
					    	}
					    	rset.close();
					    	stmt.close();
					    	data = exchg_cd;
				    	}
				    	else if(cell.getColumnIndex() == 14) {
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name != null) {
								name = name.substring(1, name.length() - 1);
							}
							queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, name);
					    	rset = stmt.executeQuery();
					    	if (rset.next()) {
					    		exchg_cd = rset.getString(1);
					    	}
					    	rset.close();
					    	stmt.close();
					    	data = exchg_cd;
				    	}
				    	else if (cell.getColumnIndex() == 27) { //PLANT_SEQ_NO
				    		
				    		queryString = "SELECT PLANT_SEQ_NO FROM FMS_AGMT_PLANT WHERE COUNTERPARTY_CD = ? "
				    				+ " AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cd);
				    		stmt.setString(2, agmt_no);
				    		stmt.setString(3, agmt_rev);
				    		stmt.setString(4, agmt_type);
				    		
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			seq_no= rset.getString(1);
				    		} else {
				    			seq_no = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = seq_no;
				    		
				    	}
				    	else if (cell.getColumnIndex() == 28) { //holiday_state
					    		queryString = "SELECT B.STATE_CODE FROM  FMS_COUNTERPARTY_PLANT_DTL A,FMS_STATE_MST B WHERE A.COUNTERPARTY_CD = ? AND B.STATE_NM = A.PLANT_STATE ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			state_code=rset.getString(1);
					    		}
					    		else
					    		{
					    			state_code=null;
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = state_code;
				    	}
						/*else if (cell.getColumnIndex() == 7) {	// Emp_Cd
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
				    	
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_TYPE = 'F' AND AGMT_REV = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("") && !seq_no.equals("") ) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + " , " + abbr + "," + agmt_type + "," + agmt_rev + "," + agmt_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + " , " + abbr + "," + agmt_type + "," + agmt_rev + "," + agmt_no + ","), conn, "E");
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
			//conn.commit();
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
	
	public void FMS_AGMT_DEACTIVATION_DTL() throws IOException, SQLException {

		function_nm="FMS_AGMT_DEACTIVATION_DTL()";
		try {
			
			table_name = "FMS_AGMT_DEACTIVATION_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String seq_no="";

			/*queryString1 = "INSERT INTO FMS_AGMT_BILLING_DTL VALUES(";
			
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";*/
			queryString1 = "INSERT INTO FMS_AGMT_DEACTIVATION_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,SEQ_NO,FROM_DT,"
					+ "TO_DT,STATUS,ENT_BY,ENT_DT,FLAG,REMARK) VALUES(?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_AGMT_DEACTIVATION_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_AGMT_DEACTIVATION_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					agmt_no = ""; agmt_rev = ""; agmt_type="";seq_no="";
					
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
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						/*else if (cell.getColumnIndex() == 7) {	// Emp_Cd
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
				    		if (cell.getColumnIndex() == 2) {	// agmt_type
				    			agmt_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_type != null) {
				    				agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 3) {	// agmt_no
				    			agmt_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_no != null) {
									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 4) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}	
				    		if (cell.getColumnIndex() == 5) {	// seq_no
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
				    				seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}	

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}				   
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_AGMT_DEACTIVATION_DTL "
			    			+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_TYPE = 'F' "
			    			+ "AND AGMT_REV = ? AND SEQ_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, seq_no);
			    	rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + " , " + abbr + "," + agmt_type + "," + agmt_rev + "," 
						+ agmt_no + ","+ seq_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + " , " + abbr + "," + agmt_type + "," + agmt_rev + "," + agmt_no + "," + seq_no + ","), conn, "E");
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
			//conn.commit();
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
	
	public void FMS_SUPPLY_CONT_MST() throws IOException, SQLException {

		function_nm="FMS_SUPPLY_CONT_MST()";
		try {
			
			table_name = "FMS_SUPPLY_CONT_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  

			/*queryString1 = "INSERT INTO FMS_SUPPLY_CONT_MST VALUES(";
			
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				if(rset.getString(2).equals("DATE")) {
					queryString1 += "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),";
				}
				else {
					queryString1 += "?,";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1 = queryString1.substring(0, queryString1.length()-1);
			queryString1 += ")";*/
			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
				    + "CONTRACT_TYPE,CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,"
				    + "TCQ,DCQ,VARIABLE_DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,"
				    + "BUYER_MONTH_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,"
				    + "DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,"
				    + "MODIFY_DT,MODIFY_BY,CONT_STATUS,IS_ALLOCATED,DDA_DT,DDA_TIME,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,TXN_CHARGE,BUYER_NOM_CUTOFF,"
				    + "TXN_UNIT,TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_CLOSE,TCQ_REQUEST_QTY,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,CHANGE_DATE_REQ,"
				    + "MEASUREMENT,MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,"
				    + "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,"
				    + "TERMINATE_PLANED,TERMINATE_FORCE,SF_GEN_DT,CLOSURE_REQUEST_FLAG,CLOSE_EFF_DT,CLOSURE_ALLOC_QTY,CLOSURE_REMARK,ADV_ADJUST) VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				    + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				    + "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,"
				    + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
				    + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					agmt_no = ""; agmt_rev = "";
					abbr = "";
					cd = "0";
					data = null;
					String cont_name="",a_base="",a_type="",spec_gas="";
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
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
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
				    					    	
				    	else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null && (cont_ref.startsWith("L") || cont_ref.startsWith("X"))) {
								agmt_no = "0";
							}else if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}				    						    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_rev != null && (cont_ref.startsWith("L") || cont_ref.startsWith("X"))) {
				    			agmt_rev = "0";
							}
				    		else if (agmt_rev != null) {
								agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
							}				    		
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (cont_no != null) {
								cont_no = cont_no.substring(1, cont_no.length() - 1);
							}
							no = Integer.parseInt(cont_no);
							
							queryString2 = "SELECT CONT_NO FROM FMS_SUPPLY_CONT_MST WHERE CONT_REF_NO LIKE ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, (cont_ref.length() > 3 ? cont_ref.substring(0, cont_ref.length()-1) : cont_ref) + "%");
							stmt2.setString(2, cd);
							rset2 = stmt2.executeQuery();
							if (rset2.next() && (cont_ref.startsWith("L") || cont_ref.startsWith("X"))) {
								no = rset2.getInt(1);
							}
							else {
					    		if (cont_ref.startsWith("L")) {
					    			queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_SUPPLY_CONT_MST WHERE CONTRACT_TYPE = 'L' AND CONT_NO LIKE ? ";
						    		stmt = conn.prepareStatement(queryString);	
						    		stmt.setString(1, "2"+cont_no.substring(2)+"%");
						    		rset = stmt.executeQuery();
						    		if (rset.next() && rset.getInt(1) > 0) {
						    			no = rset.getInt(1);
						    				
						    		}else{				    		
						    			no = 1000;
	//					    			queryString2 = "SELECT TO_CHAR(SYSDATE, 'YYYY') FROM DUAL";
	//					    			stmt2 = conn.prepareStatement(queryString2);
	//					    			rset2 = stmt2.executeQuery();				    			
						    			
	//					    			if(rset2.next()) {				    				
						    				no = no * Integer.parseInt("2" + cont_no.substring(2));
						    				no++;
	//					    			}
	//					    			rset2.close();
	//					    			stmt2.close();	
						    		}
						    		rset.close();
						    		stmt.close();					    						    		
								}else if(cont_ref.startsWith("X")) {
									queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_SUPPLY_CONT_MST WHERE CONTRACT_TYPE = 'X' AND CONT_NO LIKE ?  ";
						    		stmt = conn.prepareStatement(queryString);	
						    		stmt.setString(1, "1"+cont_no.substring(2)+"%");
						    		rset = stmt.executeQuery();
						    		if (rset.next() && rset.getInt(1) > 0) {
						    			no = rset.getInt(1);					    				
						    		}else{					    		
						    			no = 1000;
	//					    			queryString2 = "SELECT TO_CHAR(SYSDATE, 'YYYY') FROM DUAL";
	//					    			stmt2 = conn.prepareStatement(queryString2);
	//					    			rset2 = stmt2.executeQuery();				    			
						    			
	//					    			if(rset2.next()) {				    				
						    				no = no * Integer.parseInt("1" + cont_no.substring(2));
						    				no++;
	//					    			}
	//					    			rset2.close();
	//					    			stmt2.close();	
						    		}
						    		rset.close();
						    		stmt.close();			
								}
				    		}
							rset2.close();
							stmt2.close();
				    		
				    		data = no+"";
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (cont_rev != null) {
				    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
							}	
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //Cont_type
				    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (cont_type != null) {
				    			cont_type = cont_type.substring(1, cont_type.length() - 1);
							}	
				    		data = cont_type;
				    	}
				    	
				    	
				    	else if (cell.getColumnIndex() == 7) { 
				    		if (cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			cont_name = abbr+"-SEIPL-"+no+"-REV"+cont_rev;					    		 
							}else {
				    		cont_name = abbr+"-SEIPL-FGSA"+agmt_no+"-REV"+agmt_rev+" S"+cont_no+"-REV"+cont_rev;
							}
				    		data = cont_name; 
				    	}
				    	
				    	
				    	else if(cell.getColumnIndex() == 14) {
//				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			a_base = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    			if(a_base!=null) {
				    				a_base = a_base.substring(1, a_base.length() - 1);
				    			}
//				    		}else 
				    			if(cont_type.equals("S") && a_base == null){
					    		queryString = "SELECT AGMT_BASE,AGMT_TYP FROM FMS_AGMT_MST WHERE COUNTERPARTY_CD = ? AND AGMT_TYPE = 'F' AND AGMT_NO = ? AND AGMT_REV = ?";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, agmt_no);
					    		stmt.setString(3, agmt_rev);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			a_base= rset.getString(1);
					    			a_typ= rset.getString(2);
					    		} else {
					    			a_base= null;
					    			a_typ= null;
					    		}
					    		rset.close();
					    		stmt.close();
					    		
				    		}
				    		data = a_base;
				    	}
				    	else if(cell.getColumnIndex() == 15) {
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			a_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    			if (a_type!=null) {
									a_type = a_type.substring(1, a_type.length() - 1);
								}
				    		}else if(cont_type.equals("S")){
				    			a_type = a_typ;
				    		}
				    		data = a_type;
				    	}
				    	
				    	else if(cell.getColumnIndex() == 71) {
				    		spec_gas = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (spec_gas!=null) {
								spec_gas = spec_gas.substring(1, spec_gas.length() - 1);
							}
							spec_gas = ("0".equals(spec_gas)) ? "0" : (("1".equals(spec_gas)) ? "GCV" : (("2".equals(spec_gas)) ? "NCV" : null));
				    		data = spec_gas;
				    	}
				    					    					  				    	
						/*else if (cell.getColumnIndex() == 36 || cell.getColumnIndex() == 49 || cell.getColumnIndex() == 54) {	// Emp_Cd
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
				    	//System.out.println(index+"==="+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?  ";
			    			if(cont_type.equals("S")) {
			    				queryString += "AND CONT_NO = ? ";
			    			}else {
			    				queryString += "AND CONT_REF_NO = ? ";
			    			}
			    			
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev); 	    	
			    	stmt.setString(5, cont_rev);
			    	stmt.setString(6, cont_type);			   

			    	if(cont_type.equals("S")) {
				    	stmt.setInt(7, no);
	    			}else {
	    				stmt.setString(7, cont_ref);
	    			}
			    	rset = stmt.executeQuery();
			    	
				    if (!rset.next() && !cd.equals("") ) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + "," + agmt_no + "," + agmt_rev + "," + no + ","+ cont_rev + ","+ cont_type + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + "," + agmt_no + "," + agmt_rev + "," + no + ","+ cont_rev + ","+ cont_type + ","), conn, "E");
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
	

	public void FMS_SUPPLY_CONT_TRANSPTR() throws IOException, SQLException {

		function_nm="FMS_SUPPLY_CONT_TRANSPTR()";
		try {
			
			table_name = "FMS_SUPPLY_CONT_TRANSPTR";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  

			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_TRANSPTR(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRANSPORTER_CD,PLANT_SEQ_NO,ENT_BY,ENT_DT)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_TRANSPTR_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_TRANSPTR_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANSPORTER_CD, PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					agmt_no = ""; agmt_rev = "";seq_no="";
					abbr = "";
					cd = "0";
					String trans_abbr="";
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
				    		
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
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
				    	else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
				    		}
				    		data = cont_type;
				    	}
				    	else if (cell.getColumnIndex() == 7) { //trans_abbr
				    		trans_abbr = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (trans_abbr != null) {
				    			trans_abbr = trans_abbr.substring(1, trans_abbr.length() - 1);
							}
				    		
				    		if(trans_abbr.contains("GAIL DAHEJ") || trans_abbr.contains("GAILHAZSEI") || trans_abbr.contains("GAIL HAZ") || 
				    				trans_abbr.contains("PIL HAZ") || trans_abbr.contains("PIL BHAD") || trans_abbr.contains("PIL ANK") || 
				    				trans_abbr.contains("PIL MSK") || trans_abbr.contains("GAIL HAZ") || trans_abbr.contains("GAIL DAHEJ") 
				    				|| trans_abbr.contains("RGPL SHAHD")) {
					    		queryString = "SELECT A.COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
					    				+ "WHERE B.PLANT_ABBR = ? AND B.ENTITY = 'R' AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1,trans_abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		} else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}else {
				    			queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ?";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1,trans_abbr );
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		} else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
				    		data = trans_cd;

				    	}
				    	else if (cell.getColumnIndex() == 8) { //seq_no
				    		seq_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (seq_no != null) {
				    			seq_no = seq_no.substring(1, seq_no.length() - 1);
							}	
				    		data = seq_no;
				    	}
				    	else {	
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	//System.out.println(index+"="+data);
				    	stmt1.setString(index++, data);
				    }
				     
				    queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_TRANSPTR "
				    		    + " WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? "
				    		    + "AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND TRANSPORTER_CD = ? AND PLANT_SEQ_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev); 
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, cont_type);
			    	stmt.setString(8, trans_cd);
			    	stmt.setString(9, seq_no);
			    	rset = stmt.executeQuery();
			    	
				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !cont_no.equals("")) {
						logger.data(fname, (company_cd+"," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev+ "," + contract_type + "," + trans_cd + "," + seq_no + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev+ "," + contract_type + "," + trans_cd + "," + seq_no + ","), conn, "E");
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

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,,,", conn);
			
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
	
	
	
	public void FMS_SUPPLY_CONT_PLANT() throws IOException, SQLException {

		function_nm="FMS_SUPPLY_CONT_PLANT()";
		try {
			
			table_name = "FMS_SUPPLY_CONT_PLANT";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_PLANT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "PLANT_SEQ_NO,ENT_BY,ENT_DT,TRANSPORTATION_CHARGE,MARKET_MARGIN,OTHER_CHARGES) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,COUNTERPARTY_ABBR,CONTRACT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,CONT_NO,CONT_REV,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					agmt_no = ""; agmt_rev = ""; trans_cd = ""; seq_no = "";
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
				    		
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		} else {
				    			cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
				    		}
				    		data = cont_type;
				    	}
						
				    	else {
				    	
				    		if (cell.getColumnIndex() == 7) {	// PLANT_SEQ_NO
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}
							
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
			    			+ "AND AGMT_NO = ? AND AGMT_REV = ? AND  PLANT_SEQ_NO = ?  AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, seq_no);
			    	stmt.setString(6, cont_no);
			    	stmt.setString(7, cont_rev);
			    	stmt.setString(8, cont_type);
			    	rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
						//System.out.println(queryString1);
//				    	logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,COUNTERPARTY_ABBR,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
						logger.data(fname, (company_cd+"," + cd + " , " + abbr + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + seq_no + "," + cont_no + ","+ cont_rev + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + " , " + abbr + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + seq_no + "," + cont_no + ","+ cont_rev + ","), conn, "E");
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
			//conn.commit();
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
	
	
	public void FMS_SUPPLY_CONT_BU() throws IOException, SQLException {
		
		function_nm="FMS_SUPPLY_CONT_BU()";
		try {
			
			table_name = "FMS_SUPPLY_CONT_BU";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_BU(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_BU_"+start_end_dt+".xlsx");
			
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_BU_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++; 
					agmt_no = ""; agmt_rev = ""; seq_no = "";
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
						
				    	if (cell.getColumnIndex() == 0) {
				    		
				    		abbr = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!abbr.equals("NULL")) {
								abbr = abbr.substring(1, abbr.length() - 1);
							}
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
						else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
				    		}
				    		data = cont_type;
				    	}
				    	else {
				    		
				    		if (cell.getColumnIndex() == 7) {	// PLANT_SEQ_NO
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}				    	
				    	stmt1.setString(index++, data);		    	
				    }
					
				    queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO = ?  ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, cont_type);
			    	stmt.setString(8, seq_no);
			    	rset = stmt.executeQuery();
			    	
			    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + seq_no + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
					}
			    	 else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + seq_no + ","), conn, "E");
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

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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
	
	public void FMS_SUPPLY_CONT_LIABILITY() throws IOException, SQLException {
		
		function_nm="FMS_SUPPLY_CONT_LIABILITY()";
		try {
			
			table_name = "FMS_SUPPLY_CONT_LIABILITY";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String ld_promise = "",top_promise="";
			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,"
					+ "CONT_REV,LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_FROM,LQ_DAMG_TO,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
					+ "	LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_FROM,TAKE_PAY_TO,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,"
					+ "	TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,"
					+ "	MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT) VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_LIABILITY_"+start_end_dt+".xlsx");
			
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_LIABILITY_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++; 
					agmt_no = ""; agmt_rev = ""; seq_no = ""; agmt_type = "F";
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
						
				    	if (cell.getColumnIndex() == 0) {
				    		
				    		abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (abbr != null) {
								abbr = abbr.substring(1, abbr.length() - 1);
							}
				    		data = company_cd;
				    	}
				    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	} else if(cell.getColumnIndex() == 2) {
				    		data = agmt_type;
				    		
				    	}
				    	else if(cell.getColumnIndex() == 3) {
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = 'L' ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Agmt_rev
				    		if(!cont_ref.startsWith("L")) {
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 5) { //contract_type
				    		if(!cont_ref.startsWith("L")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
				    		}
				    		data = cont_type;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //Cont_no
				    		if(!cont_ref.startsWith("L")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 7) { //Cont_rev
				    		if(!cont_ref.startsWith("L")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    					    	
				    	else if(cell.getColumnIndex() == 10) {
				    		ld_promise = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!ld_promise.equals("NULL")) {
								ld_promise = ld_promise.substring(1, ld_promise.length() - 1);
							}
				    		
				    		if("Daily".equals(ld_promise)) {
				    			ld_promise = "D";
				    		}
				    		else if("Weekly".equals(ld_promise)) {
				    			ld_promise = "W";
				    		}
				    		else if("Fortnightly".equals(ld_promise)) {
				    			ld_promise = "F";
				    		}
				    		else if("Monthly".equals(ld_promise)) {
				    			ld_promise = "M";
				    		}
				    		else if("Quarterly".equals(ld_promise)) {
				    			ld_promise = "Q";
				    		}
				    		else if("Invoice Cycle".equals(ld_promise)) {
				    			ld_promise = "IC";
				    		}
				    		else if("TCQ".equals(ld_promise)) {
				    			ld_promise = "T";
				    		}
				    		else if("Defined Period".equals(ld_promise)) {
				    			ld_promise = "DP";
				    		}
				    		else if("Supply Period".equals(ld_promise)) {
				    			ld_promise = "SP";
				    		}else {
				    			ld_promise = null;
				    		}
				    		
				    		data = ld_promise;
				    	}
				    	else if(cell.getColumnIndex() == 18) {
				    		top_promise = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!top_promise.equals("NULL")) {
								top_promise = top_promise.substring(1, top_promise.length() - 1);
							}
							if("Daily".equals(top_promise)) {
				    			top_promise = "D";
				    		}
				    		else if("Weekly".equals(top_promise)) {
				    			top_promise = "W";
				    		}
				    		else if("Fortnightly".equals(top_promise)) {
				    			top_promise = "F";
				    		}
				    		else if("Monthly".equals(top_promise)) {
				    			top_promise = "M";
				    		}
				    		else if("Quarterly".equals(top_promise)) {
				    			top_promise = "Q";
				    		}
				    		else if("Invoice Cycle".equals(top_promise)) {
				    			top_promise = "IC";
				    		}
				    		else if("TCQ".equals(top_promise)) {
				    			top_promise = "T";
				    		}
				    		else if("Defined Period".equals(top_promise)) {
				    			top_promise = "DP";
				    		}
				    		else if("Supply Period".equals(top_promise)) {
				    			top_promise = "SP";
				    		}
				    		else {
				    			top_promise = null;
				    		}
				    		
				    		data = top_promise;
				    	}
				    	
				    	else {
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}	
				    	stmt1.setString(index++, data);		    	
				    }
					
				    queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_LIABILITY WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND AGMT_TYPE = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, cont_no);
			    	stmt.setString(6, cont_rev);
			    	stmt.setString(7, cont_type);
			    	stmt.setString(8, agmt_type);
		    	
			    	rset = stmt.executeQuery();
			    	
			    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
					}
			    	 else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + ","), conn, "E");
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

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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
	

	public void FMS_SUPPLY_BILLING_DTL() throws IOException, SQLException {

		function_nm="FMS_SUPPLY_BILLING_DTL()";
		try {
			
			table_name = "FMS_SUPPLY_BILLING_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  

			
			queryString1 = "INSERT INTO FMS_SUPPLY_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,"
					+ "EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,"
					+ "BILLING_DAYS,EFF_DT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?)";
					
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_BILLING_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_BILLING_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_TYPE,EFF_DT,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;  
					
					agmt_no = ""; agmt_rev = "";cont_type="";state_code="";
					
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
				    		
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
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
						else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			queryString = "SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			cont_no = rset.getString(2);
					    			cont_type = rset.getString(3);
					    		} else {
					    			agmt_no = "";
					    			cont_no = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
//				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
//					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//					    		if (agmt_rev != null) {
//									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
//								}
//				    		}
				    		agmt_rev = "0";
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
//				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
//					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
//					    		if (cont_rev != null) {
//					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//								}	
//				    		}
				    		cont_rev = "0";
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
				    		}
				    		data = cont_type;
				    	}
				    	else if(cell.getColumnIndex() == 8) {
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name != null) {
								name = name.substring(1, name.length() - 1);
							}
				    		
//				    		if(name.equals("T")) {
//				    			name = "T";
//				    		}else if(name.equals("B")) {
//				    			name = "B";
//				    		}else if(name.equals("Y")) {
//				    			name = null;
//				    		}
				    		name = "B";
				    		data = name;
				    	}
				    	else if(cell.getColumnIndex() == 13) {
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name != null) {
								name = name.substring(1, name.length() - 1);
							}
							queryString = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) = ? ";
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, name);
					    	rset = stmt.executeQuery();
					    	if (rset.next()) {
					    		exchg_cd = rset.getString(1);
					    	}else {
					    		exchg_cd =  null;
					    	}
					    	rset.close();
					    	stmt.close();
					    	data = exchg_cd;
				    	}
				    	else if(cell.getColumnIndex() == 16) {
				    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    		if (name!=null) {
								name = name.substring(1, name.length() - 1);
							}
							queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
					    	stmt = conn.prepareStatement(queryString);
					    	stmt.setString(1, name);
					    	rset = stmt.executeQuery();
					    	if (rset.next()) {
					    		exchg_cd = rset.getString(1);
					    	}
					    	rset.close();
					    	stmt.close();
					    	data = exchg_cd;
				    	}
				    	
				    	else if (cell.getColumnIndex() == 31) { //PLANT_SEQ_NO
				    		
				    		plant_seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (plant_seq_no != null) {
			    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
							}
			    			data = plant_seq_no;
				    		
				    	}
				    	else if (cell.getColumnIndex() == 32) { //holiday_state
					    		queryString = "SELECT B.TIN FROM  FMS_COUNTERPARTY_PLANT_DTL A,FMS_STATE_MST B WHERE A.COUNTERPARTY_CD = ? AND A.SEQ_NO = ? AND B.STATE_NM = A.PLANT_STATE ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, plant_seq_no);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			state_code=rset.getString(1);
					    		}
					    		else
					    		{
					    			state_code=null;
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = state_code;
				    	}
				    	
			    
				    	else {	
				    		if(cell.getColumnIndex() == 28) {
				    			eff_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (eff_dt != null) {
				    				eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
								}
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	//System.out.println(":"+index+":"+data);
				    	stmt1.setString(index++, data);
				    
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? "
			    			+ "AND CONT_NO = ? AND CONTRACT_TYPE = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') "
			    			+ "AND PLANT_SEQ_NO = ?";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, cont_no);
			    	stmt.setString(5, cont_type);
			    	stmt.setString(6, eff_dt);
			    	stmt.setString(7, plant_seq_no);
			    	rset = stmt.executeQuery();
			    	
				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("") ) {						

						logger.data(fname, (company_cd+"," + cd + "," +agmt_no+","+ agmt_rev + "," + cont_no + ","+cont_rev+","+cont_type+","+eff_dt+","+seq_no ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + "," +agmt_no+","+ agmt_rev + "," + cont_no + ","+cont_rev+","+cont_type+","+eff_dt+","+seq_no ), conn, "E");
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

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,", conn);
			
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
	
	public void FMS_SUPPLY_CONT_DCQ_DTL() throws IOException, SQLException {

		function_nm="FMS_SUPPLY_CONT_DCQ_DTL()";
		try {
			
			table_name = "FMS_SUPPLY_CONT_DCQ_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_DCQ_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,SEQ_NO,"
					+ "FROM_DT,TO_DT,DCQ,REMARK,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
					+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_DCQ_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_DCQ_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					agmt_no = ""; agmt_rev = ""; seq_no = "" ; cont_type = "";
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
				    		
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
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
						else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
				    		}
				    		data = cont_type;
				    	}
						
				    	else {
				    		
				    		if (cell.getColumnIndex() == 7) {	// seq_no
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
				    				seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}
				    		

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    
				    }
				     
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_DCQ_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
							+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND SEQ_NO = ?";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev);
					stmt.setString(5, cont_no);
					stmt.setString(6, cont_rev);
					stmt.setString(7, cont_type);
					stmt.setString(8, seq_no);
					rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
						
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + ","), conn, "E");
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
			//conn.commit();
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
	
	public void FMS_SUPPLY_CONT_PLANT_CHRG() throws IOException, SQLException {
		
		function_nm="FMS_SUPPLY_CONT_PLANT_CHRG()";
		try {
			
			table_name = "FMS_SUPPLY_CONT_PLANT_CHRG";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			
			queryString1 = "INSERT INTO FMS_SUPPLY_CONT_PLANT_CHRG(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "PLANT_SEQ_NO,EFF_DT,CHARGE_ABBR,CHARGE_RATE,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY) VALUES(?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT_CHRG_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_CONT_PLANT_CHRG_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					agmt_no = ""; agmt_rev = ""; seq_no = "";
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
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		} else {
				    			cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	
				    	else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}
				    		}
				    		data = cont_type;
				    	}
				    		
				    	else if(cell.getColumnIndex() == 9) {
				    		chrg_abbr=(cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (chrg_abbr!=null) {
				    			chrg_abbr = chrg_abbr.substring(1, chrg_abbr.length() - 1);
							}
							queryString="SELECT CHARGE_ABBR FROM FMS_CHARGE_MST WHERE CHARGE_NAME = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1,chrg_abbr );
				    		rset = stmt.executeQuery();
				    		while(rset.next()) {
				    			chrg_abbr=rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		
				    		if(chrg_abbr.equals("Other Charges")) {
				    			chrg_abbr = "OC";
				    		}else if(chrg_abbr.equals("Marketing Margin")) {
				    			chrg_abbr = "MM";
				    		}else if(chrg_abbr.equals("Transportation Charges")) {
				    			chrg_abbr = "TC";
				    		}
				    		
				    		data = chrg_abbr;
				    	}
				    	else {
				    		
				    		if (cell.getColumnIndex() == 7) {	// PLANT_SEQ_NO
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 8) {	// EFF_DT
				    			eff_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (eff_dt != null) {
				    				eff_dt = eff_dt.substring(1, eff_dt.length() - 1);
								}
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    
				    }
				     
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_CONT_PLANT_CHRG WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
							+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ_NO =? "
							+ "AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CHARGE_ABBR = ?";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev);
					stmt.setString(5, cont_no);
					stmt.setString(6, cont_rev);
					stmt.setString(7, cont_type);
					stmt.setString(8, seq_no);
					stmt.setString(9, eff_dt);
					stmt.setString(10, chrg_abbr);

					rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
						
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + ","), conn, "E");
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
			//conn.commit();
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
	
	
	public void LOG_SUPPLY_CONT_MST() throws IOException, SQLException {

		function_nm="LOG_SUPPLY_CONT_MST()";
		try {
			
			table_name = "LOG_SUPPLY_CONT_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			String meas_cls="", off_spec_cls="", liab="",liab_cls="",bill="",bill_cls="",term="",term_cls="",term_pln="",term_frc="",sf_gen_dt="",close_eff_dt="",closure_req_flg="",closure_alloc_qty="",closure_remark="";
			
			queryString1 = "INSERT INTO LOG_SUPPLY_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,LOG_SEQ_NO,"
					+ "LOG_BY,LOG_DT,CONT_NAME,CONT_REF_NO,TRADE_REF_NO,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,AGMT_BASE,AGMT_TYPE,TCQ,DCQ,"
					+ "VARIABLE_DCQ,QUANTITY_UNIT,RATE,RATE_UNIT,VARIABLE_RATE,POST_MARGIN,TRANSPORTATION_CHARGE,BUYER_NOM_FLAG,BUYER_MONTH_NOM,"
					+ "BUYER_WEEK_NOM,BUYER_DAILY_NOM,SELLER_NOM_FLAG,SELLER_MONTH_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF_FLAG,DAY_START_TIME,"
					+ "DAY_END_TIME,MDCQ_FLAG,MDCQ_PERCENTAGE,FCC_FLAG,FCC_BY,FCC_DATE,REMARK,RENEWAL_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,"
					+ "CONT_STATUS,IS_ALLOCATED,DDA_DT,DDA_TIME,BUYER_FORNGT_NOM,SELLER_FORNGT_NOM,TXN_CHARGE,BUYER_NOM_CUTOFF,TXN_UNIT,"
					+ "TCQ_SIGN,TCQ_REQUEST_FLAG,TCQ_REQUEST_CLOSE,TCQ_REQUEST_QTY,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG,CHANGE_DATE_REQ,MEASUREMENT,"
					+ "MEASUREMENT_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,OFF_SPEC_GAS_CLAUSE,"
					+ "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,"
					+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,SF_GEN_DT,CLOSE_EFF_DT,CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,"
					+ "CLOSURE_REMARK,ADV_ADJUST) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/LOG_SUPPLY_CONT_MST_"+start_end_dt+".csv");
			if(file1.exists()) {

				
				String fileName = migration_setup_dir + "EXPORT/LOG_SUPPLY_CONT_MST_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	
				
				// Below block of code is for unique SEIPL data
				String line = br.readLine();

				logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,LOG_SEQ_NO,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					agmt_no = ""; agmt_rev = ""; seq_no = "" ; cont_type = "";
					abbr = "";
					cd = "0";
					data = null;
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++) 
				     {
						data = null;
				    	if (i == 0) {	// Counterparty_Abbr, Company Code 
				    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				    		data = company_cd;
				    	}
				    	else if (i == 1) {	// Counterparty_Cd
				    		
				    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				    		
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
				    	else if (i == 2) { //Agmt_no
				    		
				    		agmt_no = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				    		
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_NAME,AGMT_BASE,"
				    					+ "AGMT_TYPE,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG FROM FMS_SUPPLY_CONT_MST WHERE "
				    					+ "COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    			cont_name = rset.getString(6);
					    			agmt_base = rset.getString(7);
					    			agmt_type = rset.getString(8);
					    			price_req = rset.getString(9);
					    			price_appr = rset.getString(10);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    			cont_name = "";
					    			agmt_base = "";
					    			agmt_type = "";
					    			price_req = null;
					    			price_appr = null;
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    						    		
				    		data  = agmt_no;
				    	}
				    	else if (i == 3) { //Agmt_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
				    			agmt_rev = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				    		}
							data = agmt_rev;
				    	}
				    	else if (i == 4) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
				    			cont_no = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (i == 5) { //Cont_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
				    			cont_rev = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (i == 6) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
				    			cont_type = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);	
				    		}
				    		data = cont_type;
				    	}
				    	
				    	else if(i == 10) {
				    		if (!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
				    			queryString = "SELECT CONT_NAME,AGMT_BASE,AGMT_TYPE,PRICE_REQUEST_FLAG,PRICE_APPROVE_FLAG FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = 'S'";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, agmt_no);
					    		stmt.setString(3, agmt_rev);
					    		stmt.setString(4, cont_no);
					    		stmt.setString(5, cont_rev);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cont_name = rset.getString(1);
					    			agmt_base = rset.getString(2);
					    			agmt_type = rset.getString(3);
					    			price_req = rset.getString(4);
					    			price_appr = rset.getString(5);
					    		}else {
					    			cont_name = "";
					    			agmt_base = "";
					    			agmt_type = "";
					    			price_req = null;
					    			price_appr = null;
					    			
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
				    		data = cont_name;				    	
				    	}
				    	else if(i == 17) {
				    		data = agmt_base;
				    	}
				    	else if(i == 18) {	
				    		data = agmt_type;
				    	}
				    	else if(i == 63) {
				    		data = price_req;
				    	}
				    	else if(i == 64) {
				    		data = price_appr;
				    	}
				    	else if(i == 67) {
				    		
				    		queryString = "SELECT MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE,LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,"
				    				+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,TO_CHAR(SF_GEN_DT, 'DD/MM/YYYY hh24:mi:ss'),"
				    				+ "TO_CHAR(CLOSE_EFF_DT, 'DD/MM/YYYY hh24:mi:ss'),CLOSURE_REQUEST_FLAG,"
				    				+ "CLOSURE_ALLOC_QTY,CLOSURE_REMARK "
				    				+ "FROM FMS_SUPPLY_CONT_MST "
				    				+ "WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND "
				    				+ "CONT_REV = ? AND CONTRACT_TYPE = ?";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1,cd);
				    		stmt.setString(2, agmt_no);
				    		stmt.setString(3, agmt_rev);
				    		stmt.setString(4, cont_no);
				    		stmt.setString(5, cont_rev);
				    		stmt.setString(6, cont_type);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			
				    			meas_cls=rset.getString(1);
				    			off_spec_cls=rset.getString(2);
				    			liab=rset.getString(3);
				    			liab_cls=rset.getString(4);
				    			bill=rset.getString(5);
				    			bill_cls=rset.getString(6);
				    			term=rset.getString(7);
				    			term_cls=rset.getString(8);
				    			term_pln=rset.getString(9);
				    			term_frc=rset.getString(10);
				    			sf_gen_dt=rset.getString(11);
				    			close_eff_dt=rset.getString(12);
				    			closure_req_flg=rset.getString(13);
				    			closure_alloc_qty=rset.getString(14);
				    			closure_remark=rset.getString(15);
				    		}
				    		else {
				    			meas_cls="";
				    			off_spec_cls="";
				    			liab="";
				    			liab_cls="";
				    			bill="";
				    			bill_cls="";
				    			term="";
				    			term_cls="";
				    			term_pln="";
				    			term_frc="";
				    			sf_gen_dt="";
				    			close_eff_dt="";
				    			closure_req_flg="";
				    			closure_alloc_qty="";
				    			closure_remark="";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = meas_cls;
				    	}
				    	else if(i == 73) {
				    		data=off_spec_cls;
				    	}
				    	else if(i == 77) {
				    		data=liab;
				    	}
				    	else if(i == 78) {
				    		data=liab_cls;
				    	}
				    	else if(i == 79) {
				    		data=bill;
				    	}
				    	else if(i == 80) {
				    		data=bill_cls;
				    	}
				    	else if(i == 81) {
				    		data=term;
				    	}
				    	else if(i == 82) {
				    		data=term_cls;
				    	}
				    	else if(i == 83) {
				    		data=term_pln;
				    	}
				    	else if(i == 84) {
				    		data=term_frc;
				    	}
				    	else if(i == 85) {
				    		data=sf_gen_dt;
				    	}
				    	else if(i == 86) {
				    		data=close_eff_dt;
				    	}
				    	else if(i == 87) {
				    		data=closure_req_flg;
				    	}
				    	else if(i == 88) {
				    		data=closure_alloc_qty;
				    	}
				    	else if(i == 89) {
				    		data=closure_remark;
				    	}
				    	else {
				    		
				    		if (i == 7) {	// seq_no
				    			seq_no = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				    		}

				    		data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
					    	}
//				    	System.out.println(index+"-"+data);
				    	stmt1.setString(index++, data);
				    
				    }
				     
					queryString = "SELECT COUNTERPARTY_CD FROM LOG_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
							+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND LOG_SEQ_NO = ?";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev);
					stmt.setString(5, cont_no);
					stmt.setString(6, cont_rev);
					stmt.setString(7, cont_type);
					stmt.setString(8, seq_no);
					rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
						
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + seq_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + seq_no + ","), conn, "E");
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
			//conn.commit();
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

	
	
	
		
	public void FMS_SUPPLY_PURCHASE_MAP_DTL() throws IOException, SQLException {
		
		function_nm="FMS_SUPPLY_PURCHASE_MAP_DTL()";
		try {
			
			table_name = "FMS_SUPPLY_PURCHASE_MAP_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String rate_unit="", pur_cont_no="",new_pur_cont_no="";;
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			
			queryString1 = "INSERT INTO FMS_SUPPLY_PURCHASE_MAP_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "PUR_CONT_NO,ALLOC_QTY,QTY_UNIT,SALE_PRICE,RATE_UNIT,COST_PRICE,CP_UNIT,MARGIN,TOTAL_MARGIN,STATUS,ENT_BY,ENT_DT,MODIFY_BY,"
					+ "MODIFY_DT,APRV_BY,APRV_DT,AVG_MARGIN,AUTH_BY,AUTH_DT,CARGO_NO) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_PURCHASE_MAP_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_PURCHASE_MAP_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NEW_PUR_CONT_NO,OLD_PUR_CONT_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					agmt_no = ""; agmt_rev = ""; seq_no = "";
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
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		} else {
				    			cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	
				    	else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);//.equals("X") ? "I" : cont_ref.split("-")[0]
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}
				    		}
				    		data = cont_type;
				    	}
				    	
				    	else if (cell.getColumnIndex() == 7) {	// pur_cont_no
				    		pur_cont_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (pur_cont_no != null) {
			    				pur_cont_no = pur_cont_no.substring(1, pur_cont_no.length() - 1);
							}
			    			
			    			if((pur_cont_no.contains("D")) || (pur_cont_no.contains("I")) || (pur_cont_no.contains("T"))) {
			    				queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ?";
		    					stmt = conn.prepareStatement(queryString);
		    					stmt.setString(1, "%"+pur_cont_no+"%");
					    		rset = stmt.executeQuery();
					    		if(rset.next()) {
					    			new_pur_cont_no = rset.getString(1);
					    		}else {
					    			new_pur_cont_no = "";
					    		}
					    		rset.close();
					    		stmt.close();
		    				}
			    			else if(pur_cont_no.length()==1) {
		    					new_pur_cont_no = "0";
		    				}
//		    				else if (pur_cont_no.contains("-") && pur_cont_no.contains("I")){
//		    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ?";
//		    					stmt = conn.prepareStatement(queryString);
//		    					stmt.setString(1, "%"+pur_cont_no+"%");
//					    		rset = stmt.executeQuery();
//					    		if(rset.next()) {
//					    			new_pur_cont_no = rset.getString(1);
//					    		}else {
//					    			new_pur_cont_no = "";
//					    		}
//					    		rset.close();
//					    		stmt.close();
//		    				}
		    				else {
		    					queryString = "SELECT CONT_NO, CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ?";
		    					stmt = conn.prepareStatement(queryString);
		    					stmt.setString(1, pur_cont_no);
					    		rset = stmt.executeQuery();
					    		if(rset.next()) {
					    			new_pur_cont_no = rset.getString(1);
					    			cargo_no = rset.getString(2);
					    		}else {
					    			new_pur_cont_no = "";
					    		}
					    		rset.close();
					    		stmt.close();
		    				}
				    		data = 	new_pur_cont_no;		    			
						}
				    	
				    	else if (cell.getColumnIndex() == 11) {	//rate_unit
				    		rate_unit = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (rate_unit != null) {
			    				rate_unit = rate_unit.substring(1, rate_unit.length() - 1);
			    				data=rate_unit.trim();
							}
				    	}
				    	
				    	else if (cell.getColumnIndex() == 26) {// cargo_no
				    		if ((!pur_cont_no.contains("D")) || (!pur_cont_no.contains("I")) || (!pur_cont_no.contains("T"))) {	
			    			
				    			data = cargo_no;
				    		}else {
					    		cargo_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (cargo_no != null) {
				    				cargo_no = cargo_no.substring(1, cargo_no.length() - 1);
								}
				    			data = cargo_no;
				    		}
			    		
			    		}

				    	else {
//				    		if (cell.getColumnIndex() == 26) {	// seq_no
//				    			cargo_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//				    			if (cargo_no != null) {
//				    				cargo_no = cargo_no.substring(1, cargo_no.length() - 1);
//								}
//				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    
				    }
				     
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
							+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PUR_CONT_NO = ? AND CARGO_NO = ?";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev);
					stmt.setString(5, cont_no);
					stmt.setString(6, cont_rev);
					stmt.setString(7, cont_type);
					stmt.setString(8, new_pur_cont_no);
					stmt.setString(9, cargo_no);
					rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !new_pur_cont_no.equals("")) {
						
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + ","+ new_pur_cont_no + ","+pur_cont_no+","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + ","+ new_pur_cont_no + ","+pur_cont_no+","), conn, "E");
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
			//conn.commit();
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
	
	
	public void FMS_SUPPLY_ALLOC_REVISED() throws IOException, SQLException {
		
		function_nm="FMS_SUPPLY_ALLOC_REVISED()";
		try {
			
			table_name = "FMS_SUPPLY_ALLOC_REVISED";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			String pur_cont_no="",new_pur_cont_no="";;
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			
			queryString1 = "INSERT INTO FMS_SUPPLY_ALLOC_REVISED(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "MODIFICATION_SEQ_NO,PUR_CONT,NEW_PRICE_EFF_DT,ORI_SALE_PRICE,NEW_SALE_PRICE,ORI_MARGIN,NEW_MARGIN,ORI_AVG_MARGIN,NEW_AVG_MARGIN,"
					+ "ORI_TOT_MARGIN,NEW_TOT_MARGIN,ENT_BY,ENT_DT,APPROVE_BY,APPROVE_DT,FLAG,REMARK,ALLOC_QTY,CARGO_NO) VALUES(?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_ALLOC_REVISED_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SUPPLY_ALLOC_REVISED_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PUR_CONT,MODIFICATION_SEQ,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					agmt_no = ""; agmt_rev = ""; seq_no = "";
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
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		} else {
				    			cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	
				    	else if (cell.getColumnIndex() == 2) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);//.equals("X") ? "I" : cont_ref.split("-")[0]
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}
				    		
							data = agmt_no;
				    	}
				    	else if (cell.getColumnIndex() == 3) { //Agmt_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 4) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 5) { //Cont_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 6) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}
				    		}
				    		data = cont_type;
				    	}
				    	
				    	else if (cell.getColumnIndex() == 8) {	// pur_cont_no
				    		pur_cont_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (pur_cont_no != null) {
			    				pur_cont_no = pur_cont_no.substring(1, pur_cont_no.length() - 1);
							}
			    			
			    			if((pur_cont_no.contains("D")) || (pur_cont_no.contains("I")) || (pur_cont_no.contains("T"))) {
		    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ?";
		    					stmt = conn.prepareStatement(queryString);
		    					stmt.setString(1, "%"+pur_cont_no+"%");
					    		rset = stmt.executeQuery();
					    		if(rset.next()) {
					    			new_pur_cont_no = rset.getString(1);
					    		}else {
					    			new_pur_cont_no = "0";
					    		}
					    		rset.close();
					    		stmt.close();
		    				}

//		    				else if (pur_cont_no.contains("-") && pur_cont_no.contains("I")){
//		    					queryString = "SELECT CONT_NO FROM FMS_TRADER_CONT_MST WHERE CONT_REF_NO LIKE ?";
//		    					stmt = conn.prepareStatement(queryString);
//		    					stmt.setString(1, "%"+pur_cont_no+"%");
//					    		rset = stmt.executeQuery();
//					    		if(rset.next()) {
//					    			new_pur_cont_no = rset.getString(1);
//					    		}else {
//					    			new_pur_cont_no = "";
//					    		}
//					    		rset.close();
//					    		stmt.close();
//		    				}
		    				else {
		    					queryString = "SELECT CONT_NO, CARGO_NO FROM FMS_TRADER_CARGO_MST WHERE CARGO_REF = ?";
		    					stmt = conn.prepareStatement(queryString);
		    					stmt.setString(1, pur_cont_no);
					    		rset = stmt.executeQuery();
					    		if(rset.next()) {
					    			new_pur_cont_no = rset.getString(1);
					    			cargo_no = rset.getString(2);
					    		}else {
					    			new_pur_cont_no = "0";
					    		}
					    		rset.close();
					    		stmt.close();
		    				}
				    		data = 	new_pur_cont_no;	    			
						}
				    		
				    	else if (cell.getColumnIndex() == 25) {// cargo_no
				    		if ((!pur_cont_no.contains("D")) || (!pur_cont_no.contains("I")) || (!pur_cont_no.contains("T"))) {	
			    			
				    			data = cargo_no;
				    		}else {
					    		cargo_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (cargo_no != null) {
				    				cargo_no = cargo_no.substring(1, cargo_no.length() - 1);
								}
				    			data = cargo_no;
				    		}
			    		
			    		}
				    	
				    	else {
				    		if (cell.getColumnIndex() == 7) {	// mod_seq
				    			mod_seq = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (mod_seq != null) {
				    				mod_seq = mod_seq.substring(1, mod_seq.length() - 1);
								}
				    		}
//				    		if (cell.getColumnIndex() == 25) {	// cargo_no
//				    			cargo_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//				    			if (cargo_no != null) {
//				    				cargo_no = cargo_no.substring(1, cargo_no.length() - 1);
//								}
//				    		}
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	//System.out.println(index+"-"+data);
				    	stmt1.setString(index++, data);
				    
				    }
				     
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_SUPPLY_ALLOC_REVISED WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
							+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND MODIFICATION_SEQ_NO = ? "
							+ "AND PUR_CONT = ? AND CARGO_NO = ?";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev);
					stmt.setString(5, cont_no);
					stmt.setString(6, cont_rev);
					stmt.setString(7, cont_type);
					stmt.setString(8, mod_seq);
					stmt.setString(9, new_pur_cont_no);
					stmt.setString(10, cargo_no);
					rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("") && !new_pur_cont_no.equals("") && !agmt_no.equals("")) {
						
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + new_pur_cont_no +"," + mod_seq + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (abbr + "," + cd + "," + cont_type + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + new_pur_cont_no +"," + mod_seq + ","), conn, "E");
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
			//conn.commit();
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
	
	
	public void FMS_LTCORA_AGMT_MST() throws IOException, SQLException {

		function_nm="FMS_LTCORA_AGMT_MST()";
		try {
			
			table_name = "FMS_LTCORA_AGMT_MST";
			System.out.println("<<START>><<"+table_name+">>");
			String buy_sale="",agmt_ref_no="";
			data = "";
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;  
			
			queryString1 = "INSERT INTO FMS_LTCORA_AGMT_MST(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,AGMT_BASE,"
					+ "AGMT_TYP,SIGNING_DT,START_DT,END_DT,STATUS,BUYER_NOM,BUYER_NOM_CLAUSE,BUYER_MONTH_NOM,BUYER_FORNGT_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,"
					+ "BUYER_NOM_CUTOFF,SELLER_NOM,SELLER_NOM_CLAUSE,SELLER_MONTH_NOM,SELLER_FORNGT_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_DEF_CLAUSE,"
					+ "DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_CLAUSE,MDCQ_PERCENTAGE,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,"
					+ "PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIAB_CLAUSE,BILLING_FLAG,"
					+ "BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,REMARK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_AGMT_MST_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_AGMT_MST_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,TIMESTAMP,", conn);
				
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
				    	else if (cell.getColumnIndex() == 4) {	// agmt_no
			    			agmt_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
								data = agmt_no;
			    		}
				    	else {
				    		if (cell.getColumnIndex() == 2) {	// buy_sale
				    			buy_sale = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (buy_sale != null) {
				    				buy_sale = buy_sale.substring(1, buy_sale.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 3) {	// agmt_type
				    			agmt_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_type != null) {
									agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 7) {	// agmt_ref_no
				    			agmt_ref_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_ref_no != null) {
				    				agmt_ref_no = agmt_ref_no.substring(1, agmt_ref_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 5) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
//				    	System.out.println(index+":"+data);
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_AGMT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND  AGMT_TYPE = 'A' AND AGMT_REV = ? AND AGMT_REF_NO = ? ";
			    	if (!agmt_ref_no.startsWith("R-")) {
			    		queryString	+= " AND AGMT_NO = ?  ";
			    	}
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_rev);
			    	stmt.setString(4, agmt_ref_no);
			    	if (!agmt_ref_no.startsWith("R-")) {
			    		stmt.setString(5, agmt_no);
			    	}
			    	
			    	rset = stmt.executeQuery();
				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + "," +buy_sale+ "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + " , " +buy_sale+ "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","), conn, "E");
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
	
	
	public void FMS_LTCORA_AGMT_BU() throws IOException, SQLException {

		function_nm="FMS_LTCORA_AGMT_BU()";
		try {
			
			System.out.println("<<START>><<FMS_LTCORA_AGMT_BU>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_LTCORA_AGMT_BU>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			
			columns = "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
			
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,'1',ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_LTCORA_AGMT_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			
			queryString1 = "INSERT INTO FMS_LTCORA_AGMT_BU(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
			
			while (rset.next()) {
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						stmt1.setString(i+1,data);
					}
					
				//for data already exists..
				queryString5 = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_AGMT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_TYPE = 'A' AND AGMT_NO = ? AND AGMT_REV = ? AND PLANT_SEQ_NO = '1' ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, rset.getString(2));
				stmt5.setString(3, rset.getString(5));
				stmt5.setString(4, rset.getString(6));
			
				rset5 = stmt5.executeQuery();
				
				if (!rset5.next() ) {
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
	//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
				}
				stmt5.close();
				rset5.close();
				}
			rset.close();
			stmt.close();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";
			
			System.out.println("<<END>><<FMS_LTCORA_AGMT_BU>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_LTCORA_AGMT_BU>>,", conn);
			
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
	
	
	public void FMS_LTCORA_AGMT_TRANSPTR() throws IOException, SQLException {

		function_nm="FMS_LTCORA_AGMT_TRANSPTR()";
		try {
			
			table_name = "FMS_LTCORA_AGMT_TRANSPTR";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			queryString1 = "INSERT INTO FMS_LTCORA_AGMT_TRANSPTR(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_AGMT_TRANSPTR_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_AGMT_TRANSPTR_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,TRANSPORTER_CD,PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					agmt_no = ""; agmt_rev = ""; trans_cd = ""; seq_no = "";
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
				    		agmt_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!agmt_ref.equals("NULL")) {
								agmt_ref = agmt_ref.substring(1, agmt_ref.length() - 1);
							}
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
				    	else if(cell.getColumnIndex() == 4) {
				    		agmt_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    			if (!agmt_no.equals("NULL")) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
							if(agmt_ref.startsWith("R")) {
				    			queryString3 = "SELECT AGMT_NO FROM FMS_LTCORA_AGMT_MST WHERE AGMT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2'";
					    		stmt3 = conn.prepareStatement(queryString3);
					    		stmt3.setString(1, agmt_ref);
					    		stmt3.setString(2, cd);
					    		rset3 = stmt3.executeQuery();
					    		
					    		if (rset3.next()) {
					    			agmt_no = rset3.getString(1);
					    		}
					    		rset3.close();
					    		stmt3.close();
				    		}
				    		data = agmt_no;	
				    	}
				    	else if (cell.getColumnIndex() == 6) {	// TRANSPORTER Counterparty_Cd
				    		abbr = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!abbr.equals("NULL")) {
								abbr = abbr.substring(1, abbr.length() - 1);
							}
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			data = rset.getString(1);
				    		}
				    		rset.close();
				    		stmt.close();
				    		trans_cd = data;
				    	}
					
				    	else {
				    		if (cell.getColumnIndex() == 3) {	// agmt_type
				    			agmt_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_type != null) {
									agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
								}
				    		}				    		
//				    		if (cell.getColumnIndex() == 4) {	// agmt_no
//				    			agmt_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//				    			if (agmt_no != null) {
//									agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
//								}
//				    		}
				    		if (cell.getColumnIndex() == 5) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 7) {	// PLANT_SEQ_NO
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_AGMT_TRANSPTR WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_NO = ? AND AGMT_TYPE = 'A' AND AGMT_REV = ? AND TRANSPORTER_CD = ? AND PLANT_SEQ_NO = ?  ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, trans_cd);
			    	stmt.setString(6, seq_no);
			    	rset = stmt.executeQuery();
			    	
				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + "," + buy_sale + ","+ agmt_type + "," + agmt_no + "," + agmt_rev + "," + trans_cd + "," + seq_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + "," + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + "," + trans_cd + "," + seq_no + ","), conn, "E");
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
	
	public void FMS_LTCORA_AGMT_PLANT() throws IOException, SQLException {

		function_nm="FMS_LTCORA_AGMT_PLANT()";
		try {
			
			table_name = "FMS_LTCORA_AGMT_PLANT";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			queryString1 = "INSERT INTO FMS_LTCORA_AGMT_PLANT(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_AGMT_PLANT_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_AGMT_PLANT_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					agmt_no = ""; agmt_rev = ""; trans_cd = ""; seq_no = "";
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
				    		agmt_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!agmt_ref.equals("NULL")) {
								agmt_ref = agmt_ref.substring(1, agmt_ref.length() - 1);
							}
							queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		} else {
				    			cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else if(cell.getColumnIndex() == 4) {
				    		agmt_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    			if (!agmt_no.equals("NULL")) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
							if(agmt_ref.startsWith("R")) {
				    			queryString3 = "SELECT AGMT_NO FROM FMS_LTCORA_AGMT_MST WHERE AGMT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2'";
					    		stmt3 = conn.prepareStatement(queryString3);
					    		stmt3.setString(1, agmt_ref);
					    		stmt3.setString(2, cd);
					    		rset3 = stmt3.executeQuery();
					    		
					    		if (rset3.next()) {
					    			agmt_no = rset3.getString(1);
					    		}
					    		rset3.close();
					    		stmt3.close();
				    		}
				    		data = agmt_no;	
				    	}
				    	else {
				    		if (cell.getColumnIndex() == 3) {	// agmt_type
				    			agmt_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_type != null) {
				    				agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 5) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 6) {	// PLANT_SEQ_NO
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no != null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_AGMT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_NO = ? AND AGMT_TYPE = 'A' AND AGMT_REV = ? AND  PLANT_SEQ_NO = ?  ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);
			    	stmt.setString(5, seq_no);
			    	rset = stmt.executeQuery();
			    	

				    if (!rset.next() && !cd.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + "," + buy_sale + ","+ agmt_type + "," + agmt_no + "," + agmt_rev + "," + seq_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + "," + buy_sale + ","+ agmt_type + "," + agmt_no + "," + agmt_rev + "," + seq_no + ","), conn, "E");
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
			//conn.commit();
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
	
	
public void FMS_LTCORA_AGMT_LIABILITY() throws IOException, SQLException {
		
		function_nm="FMS_LTCORA_AGMT_LIABILITY()";
		try {
			
			table_name = "FMS_LTCORA_AGMT_LIABILITY";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String ld_promise = "",top_promise="";
			
			queryString1 = "INSERT INTO FMS_LTCORA_AGMT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,"
					+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,LIAB_TAKE_PAY,"
					+ "TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,"
					+ "TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,"
					+ "ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_AGMT_LIABILITY_"+start_end_dt+".xlsx");
			
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_AGMT_LIABILITY_"+start_end_dt+".xlsx"));
				
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++; 
					agmt_no = ""; agmt_rev = "";
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
						
				    	if (cell.getColumnIndex() == 0) {
				    		
				    		abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    		if (abbr!=null) {
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
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	
				    	else if(cell.getColumnIndex() == 8) {
				    		ld_promise = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!ld_promise.equals("NULL")) {
								ld_promise = ld_promise.substring(1, ld_promise.length() - 1);
							}
							if("Daily".equals(ld_promise)) {
				    			ld_promise = "D";
				    		}
				    		else if("Weekly".equals(ld_promise)) {
				    			ld_promise = "W";
				    		}
				    		else if("Fortnightly".equals(ld_promise)) {
				    			ld_promise = "F";
				    		}
				    		else if("Monthly".equals(ld_promise)) {
				    			ld_promise = "M";
				    		}
				    		else if("Quarterly".equals(ld_promise)) {
				    			ld_promise = "Q";
				    		}
				    		else if("Invoice Cycle".equals(ld_promise)) {
				    			ld_promise = "IC";
				    		}
				    		else if("TCQ".equals(ld_promise)) {
				    			ld_promise = "T";
				    		}
				    		else if("Defined Period".equals(ld_promise)) {
				    			ld_promise = "DP";
				    		}
				    		else if("Supply Period".equals(ld_promise)) {
				    			ld_promise = "SP";
				    		}else {
				    			ld_promise = null;
				    		}
				    		
				    		data = ld_promise;
				    	}
				    	else if(cell.getColumnIndex() == 14) {
				    		top_promise = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!top_promise.equals("NULL")) {
								top_promise = top_promise.substring(1, top_promise.length() - 1);
							}
							if("Daily".equals(top_promise)) {
				    			top_promise = "D";
				    		}
				    		else if("Weekly".equals(top_promise)) {
				    			top_promise = "W";
				    		}
				    		else if("Fortnightly".equals(top_promise)) {
				    			top_promise = "F";
				    		}
				    		else if("Monthly".equals(top_promise)) {
				    			top_promise = "M";
				    		}
				    		else if("Quarterly".equals(top_promise)) {
				    			top_promise = "Q";
				    		}
				    		else if("Invoice Cycle".equals(top_promise)) {
				    			top_promise = "IC";
				    		}
				    		else if("TCQ".equals(top_promise)) {
				    			top_promise = "T";
				    		}
				    		else if("Defined Period".equals(top_promise)) {
				    			top_promise = "DP";
				    		}
				    		else if("Supply Period".equals(top_promise)) {
				    			top_promise = "SP";
				    		}
				    		else {
				    			top_promise = null;
				    		}
				    		
				    		data = top_promise;
				    	}
				    	
				    	else {
				    		if (cell.getColumnIndex() == 3) {	// agmt_type
				    			agmt_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_type != null) {
				    				agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 4) {	// agmt_no
				    			agmt_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_no != null) {
				    				agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 5) {	// agmt_rev
				    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
				    			    						    		

					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}	
				    	//System.out.println(index+"-"+data);
				    	stmt1.setString(index++, data);		    	
				    }
					
				    queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_AGMT_LIABILITY WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND AGMT_TYPE = ? AND BUY_SALE = 'C' ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, cd);
			    	stmt.setString(3, agmt_no);
			    	stmt.setString(4, agmt_rev);	
			    	stmt.setString(5, agmt_type);	

		    	
			    	rset = stmt.executeQuery();
			    	
			    	 if (!rset.next() && !cd.equals("")) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + " , " + agmt_type + " , " + agmt_no + "," + agmt_rev + "," + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
					}
			    	 else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + " , "  + agmt_type + " , " + agmt_no + "," + agmt_rev + "," + ","), conn, "E");
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

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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

public void FMS_LTCORA_AGMT_BILLING_DTL() throws IOException, SQLException {

	function_nm="FMS_LTCORA_AGMT_BILLING_DTL()";
	try {
		
		table_name = "FMS_LTCORA_AGMT_BILLING_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 

		queryString1 = "INSERT INTO FMS_LTCORA_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,"
				+ "BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,"
				+ "INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,"
				+ "MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,?,?,?,?,?,?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_AGMT_BILLING_DTL_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_AGMT_BILLING_DTL_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
			
			while (rowIterator.hasNext()) {
				total_count++;
				agmt_no = ""; agmt_rev = ""; agmt_type="";
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
			    		agmt_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!agmt_ref.equals("NULL")) {
							agmt_ref = agmt_ref.substring(1, agmt_ref.length() - 1);
						}
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		}else {
			    			cd = "";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    	}
			    	else if (cell.getColumnIndex() == 4) {	// agmt_no
			    		agmt_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
		    			if (!agmt_no.equals("NULL")) {
							agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
						}
						if(agmt_ref.startsWith("R")) {
			    			queryString3 = "SELECT AGMT_NO FROM FMS_LTCORA_AGMT_MST WHERE AGMT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2'";
				    		stmt3 = conn.prepareStatement(queryString3);
				    		stmt3.setString(1, agmt_ref);
				    		stmt3.setString(2, cd);
				    		rset3 = stmt3.executeQuery();
				    		
				    		if (rset3.next()) {
				    			agmt_no = rset3.getString(1);
				    		}
				    		rset3.close();
				    		stmt3.close();
			    		}
			    		data = agmt_no;	
		    		}
			    	else if (cell.getColumnIndex() == 3) {	// agmt_type
		    			agmt_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
		    			if (agmt_type != null) {
		    				agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
						}
		    			data = agmt_type;
		    		}
			    	else if (cell.getColumnIndex() == 5) {	// agmt_rev
		    			agmt_rev = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
		    			if (agmt_rev != null) {
							agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
						}
		    			data = agmt_rev;
		    		}	
			    	else if(cell.getColumnIndex() == 12) {
			    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if (name != null) {
							name = name.substring(1, name.length() - 1).trim();
						}
						queryString = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) LIKE ? ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, "%"+name+"%");
				    	rset = stmt.executeQuery();
				    	if (rset.next()) {
				    		exchg_cd = rset.getString(1);
				    	}else {
				    		exchg_cd =  null;
				    	}
				    	rset.close();
				    	stmt.close();
				    	data = exchg_cd;
			    	}
			    	else if(cell.getColumnIndex() == 15) {
			    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if (name!=null) {
							name = name.substring(1, name.length() - 1);
						}
						queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) LIKE ? ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, "%"+name+"%");
				    	rset = stmt.executeQuery();
				    	if (rset.next()) {
				    		exchg_cd = rset.getString(1);
				    	}
				    	rset.close();
				    	stmt.close();
				    	data = exchg_cd;
			    	}
			    	else if(cell.getColumnIndex() == 19) {
			    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if (name != null) {
							name = name.substring(1, name.length() - 1);
						}
						queryString = "SELECT TAX_STRUCT_CD FROM FMS_ENTITY_SERVICE_TAX_DTL WHERE UPPER(TAX_STRUCT_DTL) = ? ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, name);
				    	rset = stmt.executeQuery();
				    	if (rset.next()) {
				    		tax_cd = rset.getString(1);
				    	}else {
				    		tax_cd =  null;
				    	}
				    	rset.close();
				    	stmt.close();
				    	data = tax_cd;
			    	}
				
			    	else {
			    		
			    		if (cell.getColumnIndex() == 29) {	// PLANT_SEQ_NO
			    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (seq_no != null) {
								seq_no = seq_no.substring(1, seq_no.length() - 1);
							}
			    		}
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}	
//			    	System.out.println(index+"=="+data);
			    	stmt1.setString(index++, data);
			    }
			     
		    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_NO = ? AND AGMT_TYPE = 'A' AND AGMT_REV = ? AND PLANT_SEQ_NO = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
		    	stmt.setString(4, agmt_rev);
		    	stmt.setString(5, seq_no);
		    	rset = stmt.executeQuery();
		    	

			    if (!rset.next() && !cd.equals("") && !seq_no.equals("") ) {
					//System.out.println(queryString1);
					
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","), conn, "E");
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
		//conn.commit();
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

public void FMS_LTCORA_CONT_MST() throws IOException, SQLException {

	function_nm="FMS_LTCORA_CONT_MST()";
	try {
		
		table_name = "FMS_LTCORA_CONT_MST";
		System.out.println("<<START>><<"+table_name+">>");
		String buy_sale="";
		data = "";
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		
		data = "";
		logger_count = 0;
		skipped_count = 0;   
		total_count = 0;  
		
		queryString1 = "INSERT INTO FMS_LTCORA_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,"
				+ "CONT_NO,CONT_REV,CONT_NAME,CONT_REF_NO,CONT_STATUS,AGMT_BASE,DDA_DT,DDA_TIME,DDA_NOTE,SIGNING_DT,SIGNING_TIME,"
				+ "START_DT,END_DT,NO_OF_CARGO,LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,TARIFF_DISCOUNT,VOL_DISCOUNT,VOL_DISCOUNT_UNIT,"
				+ "ADV_ADJUST,ADV_ADJUST_TYPE,ADV_ADJUST_AMOUNT,ADV_ADJUST_UNIT,EXTEND_STORAGE,DISCOUNT_DAYS,STORAGE_TARIFF_MODE,"
				+ "STORAGE_TARIFF_UNIT,STORAGE_TARIFF,BUYER_NOM,BUYER_NOM_CLAUSE,BUYER_MONTH_NOM,BUYER_FORNGT_NOM,BUYER_WEEK_NOM,"
				+ "BUYER_DAILY_NOM,BUYER_NOM_CUTOFF,SELLER_NOM,SELLER_NOM_CLAUSE,SELLER_MONTH_NOM,SELLER_FORNGT_NOM,SELLER_WEEK_NOM,"
				+ "SELLER_DAILY_NOM,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_CLAUSE,MDCQ_PERCENTAGE,MEASUREMENT,"
				+ "MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,SPEC_GAS_ENERGY_BASE,"
				+ "SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIAB_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,"
				+ "TERMINATE_PLANED,TERMINATE_FORCE,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT,FCC_FLAG,FCC_BY,FCC_DATE,CLOSURE_REQUEST_FLAG,"
				+ "CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK,TLU_FLAG)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_MST_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_MST_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,TIMESTAMP,", conn);
			
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
			    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
							cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
						}
			    		
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
			    	else if (cell.getColumnIndex() == 4) {	// agmt_no
			    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
			    		if (agmt_no!=null) {
							agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
						}
						if(cont_ref.startsWith("R")) {
			    			queryString3 = "SELECT AGMT_NO,AGMT_REV FROM FMS_LTCORA_AGMT_MST WHERE AGMT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2' ";
				    		stmt3 = conn.prepareStatement(queryString3);
				    		stmt3.setString(1, cont_ref.substring(0,cont_ref.length()-2));
				    		stmt3.setString(2, cd);
				    		rset3 = stmt3.executeQuery();
				    		
				    		if (rset3.next()) {
				    			agmt_no = rset3.getString(1);
				    			agmt_rev = rset3.getString(2);
				    		}
				    		rset3.close();
				    		stmt3.close();
			    		}
			    		data = agmt_no;
		    		}
			    	else if (cell.getColumnIndex() == 5) {	// agmt_rev
			    		if(!cont_ref.startsWith("R")) {
			    			agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_rev != null) {
				    			agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
							}
			    		}
			    		data = agmt_rev;
		    		}
			    	else if (cell.getColumnIndex() == 6) {	// cont_type
			    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
			    		if (cont_type != null) {
			    			cont_type = cont_type.substring(1, cont_type.length() - 1);
						}	
			    		data = cont_type;
		    		}
			    	else if (cell.getColumnIndex() == 7) {	// cont_no
			    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
			    		if (cont_no!=null) {
							cont_no = cont_no.substring(1, cont_no.length() - 1);
						}
						no = Integer.parseInt(cont_no);
			    		
			    		
			    		queryString2 = "SELECT CONT_NO FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO LIKE ? AND COMPANY_CD = '2'";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, ((cont_ref.length() > 3 && !cont_ref.startsWith("R")) ? cont_ref.substring(0, cont_ref.length()-1) : cont_ref) + "%");
//						stmt2.setString(2, cd);
						rset2 = stmt2.executeQuery();
						if (rset2.next() && (cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R"))) {
							no = rset2.getInt(1);
						}
						else {
				    		if (cont_ref.startsWith("C")) {
				    			queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_LTCORA_CONT_MST WHERE CONT_NO LIKE ? AND COMPANY_CD = '2'";
					    		stmt = conn.prepareStatement(queryString);
//					    		stmt.setString(1, cd);
					    		stmt.setString(1, cont_no.substring(2)+"%");
	
					    		rset = stmt.executeQuery();
					    		
					    		if (rset.next() && rset.getInt(1) > 0) {
					    			no = rset.getInt(1);
					    				
					    		}else{				    		
					    			no = 1000;
	//				    			queryString2 = "SELECT TO_CHAR(SYSDATE, 'YYYY') FROM DUAL";
	//				    			stmt2 = conn.prepareStatement(queryString2);
	//				    			rset2 = stmt2.executeQuery();				    			
					    			
	//				    			if(rset2.next()) {				    				
					    				no = no * Integer.parseInt(cont_no.substring(2));
					    				no++;
	//				    			}
	//				    			rset2.close();
	//				    			stmt2.close();	
					    		}
					    		rset.close();
					    		stmt.close();					    						    		
							}else if(cont_ref.startsWith("A")) {
								queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_LTCORA_CONT_MST WHERE CONT_NO LIKE ? AND COMPANY_CD = '2'";
					    		stmt = conn.prepareStatement(queryString);
//					    		stmt.setString(1, cd);
					    		stmt.setString(1, cont_no.substring(2)+"%");
					    		rset = stmt.executeQuery();
					    		if (rset.next() && rset.getInt(1) > 0) {
					    			no = rset.getInt(1);					    				
					    		}else{					    		
					    			no = 1000;
	//				    			queryString2 = "SELECT TO_CHAR(SYSDATE, 'YYYY') FROM DUAL";
	//				    			stmt2 = conn.prepareStatement(queryString2);
	//				    			rset2 = stmt2.executeQuery();				    			
	//				    			
	//				    			if(rset2.next()) {				    				
					    				no = no * Integer.parseInt(cont_no.substring(2));
					    				no++;
	//				    			}
	//				    			rset2.close();
	//				    			stmt2.close();	
					    		}
					    		rset.close();
					    		stmt.close();			
							}
							else if(cont_ref.startsWith("R")) {
								queryString = "SELECT (MAX(CONT_NO)+1) FROM FMS_LTCORA_CONT_MST WHERE CONT_NO LIKE ? AND COMPANY_CD = '2'";
					    		stmt = conn.prepareStatement(queryString);
//					    		stmt.setString(1, cd);
					    		stmt.setString(1, cont_no.substring(2)+"%");
					    		rset = stmt.executeQuery();
					    		if (rset.next() && rset.getInt(1) > 0) {
					    			no = rset.getInt(1);					    				
					    		}else{					    		
					    			no = 1000;
	//				    			queryString2 = "SELECT TO_CHAR(SYSDATE, 'YYYY') FROM DUAL";
	//				    			stmt2 = conn.prepareStatement(queryString2);
	//				    			rset2 = stmt2.executeQuery();				    			
	//				    			
	//				    			if(rset2.next()) {				    				
					    				no = no * Integer.parseInt(cont_no.substring(2));
					    				no++;
	//				    			}
	//				    			rset2.close();
	//				    			stmt2.close();	
					    		}
					    		rset.close();
					    		stmt.close();			
							}
			    		}
						rset2.close();
						stmt2.close();
						
			    		data = no+"";
		    		}
			    	else if (cell.getColumnIndex() == 8) {	// cont_rev
			    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
			    		if (cont_rev != null) {
			    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
						}	
			    		data = cont_rev;
		    		}
			    	else if (cell.getColumnIndex() == 9) { 
			    		cont_name = abbr+"-SEIPL-LTCORA"+agmt_no+"-REV"+agmt_rev+" "+cont_type+no+"-REV"+cont_rev;					    		 
			    		data = cont_name; 
			    	}
			    	else if (cell.getColumnIndex() == 86) { 
			    		data = null; 
			    	}
			    	else {
			    		if (cell.getColumnIndex() == 2) {	// buy_sale
			    			buy_sale = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (buy_sale != null) {
			    				buy_sale = buy_sale.substring(1, buy_sale.length() - 1);
							}
			    		}
			    		else if (cell.getColumnIndex() == 3) {	// agmt_type
			    			agmt_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (agmt_type != null) {
								agmt_type = agmt_type.substring(1, agmt_type.length() - 1);
							}
			    		}
			    		
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}
//			    	System.out.println(index + "===" + data);
			    	stmt1.setString(index++, data);
			    }
			     
		    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND  AGMT_TYPE = 'A' AND AGMT_NO = ? AND CONTRACT_TYPE = ? AND CONT_REV = ? AND CONT_REF_NO = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
		    	stmt.setString(4, cont_type);
		    	stmt.setString(5, cont_rev);
		    	stmt.setString(6, cont_ref);
//		    	System.out.println(company_cd+"==="+cd+"==="+agmt_no+"==="+agmt_rev+"==="+cont_type+"==="+cont_rev+"==="+cont_ref+"==="+no);
		    	rset = stmt.executeQuery();
		    	
			    if (!rset.next() && !cd.equals("")) {
					//System.out.println(queryString1);
					
					logger.data(fname, (company_cd+"," + cd + "," +buy_sale+ "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + ","+ no + "," + cont_rev + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+"," + cd + " , " +buy_sale+ "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + ","+ no + "," + cont_rev + ","), conn, "E");
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

public void FMS_LTCORA_CONT_BU() throws IOException, SQLException {

	function_nm="FMS_LTCORA_CONT_BU()";
	try {
		
		System.out.println("<<START>><<FMS_LTCORA_CONT_BU>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_LTCORA_CONT_BU>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		
		columns = "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT";
		
		queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,'1',ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		
		queryString1 = "INSERT INTO FMS_LTCORA_CONT_BU(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
		
		while (rset.next()) {
				stmt1 = conn.prepareStatement(queryString1);
				
				for(int i = 0;i < columns.split(",").length;i++) {
					data = "";
					data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
					
					stmt1.setString(i+1,data);
				}
				
			//for data already exists..
			queryString5 = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_BU WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_TYPE = 'A' AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND PLANT_SEQ_NO = '1' ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(5));
			stmt5.setString(4, rset.getString(6));
			stmt5.setString(5, rset.getString(7));
			stmt5.setString(6, rset.getString(8));
			stmt5.setString(7, rset.getString(9));
		
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				
//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
				
				stmt1.executeUpdate();
				stmt1.close();
				
				logger_count++;
			}
			else {
				
				stmt1.close();
				skipped_count++; 
				
//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
				
			}
			stmt5.close();
			rset5.close();
			}
		rset.close();
		stmt.close();
		
		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
		System.out.println("<<END>><<FMS_LTCORA_CONT_BU>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_LTCORA_CONT_BU>>,", conn);
		
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
public void LOG_LTCORA_CONT_MST() throws IOException, SQLException {
	function_nm="LOG_LTCORA_CONT_MST()";
	try {
		
		table_name = "LOG_LTCORA_CONT_MST";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		
		queryString1 = "INSERT INTO LOG_LTCORA_CONT_MST(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,"
				+ "LOG_SEQ_NO,LOG_BY,LOG_DT,CONT_NAME,CONT_REF_NO,CONT_STATUS,AGMT_BASE,DDA_DT,DDA_TIME,DDA_NOTE,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,"
				+ "NO_OF_CARGO,LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,TARIFF_DISCOUNT,VOL_DISCOUNT,VOL_DISCOUNT_UNIT,ADV_ADJUST,ADV_ADJUST_TYPE,"
				+ "ADV_ADJUST_AMOUNT,ADV_ADJUST_UNIT,EXTEND_STORAGE,DISCOUNT_DAYS,STORAGE_TARIFF_MODE,STORAGE_TARIFF_UNIT,STORAGE_TARIFF,BUYER_NOM,"
				+ "BUYER_NOM_CLAUSE,BUYER_MONTH_NOM,BUYER_FORNGT_NOM,BUYER_WEEK_NOM,BUYER_DAILY_NOM,BUYER_NOM_CUTOFF,SELLER_NOM,SELLER_NOM_CLAUSE,"
				+ "SELLER_MONTH_NOM,SELLER_FORNGT_NOM,SELLER_WEEK_NOM,SELLER_DAILY_NOM,DAY_DEF,DAY_DEF_CLAUSE,DAY_START_TIME,DAY_END_TIME,MDCQ,MDCQ_CLAUSE,"
				+ "MDCQ_PERCENTAGE,MEASUREMENT,MEAS_CLAUSE,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_CLAUSE,"
				+ "SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIAB_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,"
				+ "TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT,FCC_FLAG,FCC_BY,FCC_DATE,CLOSURE_REQUEST_FLAG,"
				+ "CLOSURE_ALLOC_QTY,CLOSE_EFF_DT,CLOSURE_REMARK,TLU_FLAG) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,"
				+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/LOG_LTCORA_CONT_MST_"+start_end_dt+".csv");
		if(file1.exists()) {

			String fileName = migration_setup_dir + "EXPORT/LOG_LTCORA_CONT_MST_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
				// Below block of code is for unique SEIPL data
				String line = br.readLine();

				logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,LOG_SEQ_NO,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					
					agmt_no = ""; agmt_rev = ""; seq_no = "" ; 
					abbr = "";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++)
				    {
//					cell = cellIterator.next();
						data = null;
				    	if (i == 0) {	// Counterparty_Abbr, Company Code 
				    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				    		data = company_cd;
				    	}
				    	else if (i == 1) {	// Counterparty_Cd
				    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				    		
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
				    		
				    		queryString2 = "SELECT AGMT_NO, AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = '2' AND COUNTERPARTY_CD = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, cont_ref);
							stmt2.setString(2, cd);
							rset2 = stmt2.executeQuery();
							
							if (rset2.next()) {
				    			agmt_no = rset2.getString(1);
				    			agmt_rev = rset2.getString(2);
				    			cont_no = rset2.getString(3);
				    			cont_rev = rset2.getString(4);
				    			cont_type = rset2.getString(5);
				    		}
				    		rset2.close();
				    		stmt2.close();
				    	}
						else if (i == 4) { //Agmt_no
							data = agmt_no;
				    	}
				    	else if (i == 5) { //Agmt_rev
							data = agmt_rev;
				    	}
				    	else if (i == 6) { //cont_type
				    		data = cont_type;
				    	}
				    	else if (i == 7) { //Cont_no
				    		data = cont_no;
				    	}
				    	else if (i == 8) { //Cont_rev
				    		data = cont_rev;
				    	}
				    	else if (i == 12) { //cont_name
				    		cont_name = abbr+"-SEIPL-LTCORA"+agmt_no+"-REV"+agmt_rev+" "+cont_type+no+"-REV"+cont_rev;					    		 
				    		data = cont_name; 
				    	}
				    	else if (i == 90) { 
				    		data = null; 
				    	}
				    	else {				    		
				    		if (i == 2) {	// buy_sale
				    			buy_sale = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    		}
				    		if (i == 3) {	// agmt_type
				    			agmt_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    		}
				    		if (i == 9) {	// log_seq_no
				    			seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    		}

					    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    	}
				    	}
				    	
//				    	System.out.println(index+"-"+data);
				    	stmt1.setString(index++, data);
				    
				    }
				     
					queryString = "SELECT COUNTERPARTY_CD FROM LOG_LTCORA_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND  AGMT_TYPE = 'A' "
							+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CONT_REF_NO = ? AND LOG_SEQ_NO = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, agmt_no);
					stmt.setString(4, agmt_rev);
					stmt.setString(5, cont_type);
					stmt.setString(6, cont_no);
					stmt.setString(7, cont_rev);
					stmt.setString(8, cont_ref);
					stmt.setString(9, seq_no);
					rset = stmt.executeQuery();

				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")){
						
						logger.data(fname, (abbr + "," + cd + "," + buy_sale + "," + agmt_type+ "," + agmt_no + "," + agmt_rev + ","+ cont_type+ "," + cont_no + "," + cont_rev + "," + seq_no + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (abbr + "," + cd + "," + buy_sale + "," + agmt_type+ "," + agmt_no + "," + agmt_rev + ","+ cont_type+ "," + cont_no + "," + cont_rev + "," + seq_no + "," ), conn, "E");
					}
				    
				    rset.close();
				    stmt.close();
				}
				br.close();
			}

//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";				
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		//conn.commit();
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
public void FMS_LTCORA_CONT_TRANSPTR() throws IOException, SQLException {

	function_nm="FMS_LTCORA_CONT_TRANSPTR()";
	try {
		
		table_name = "FMS_LTCORA_CONT_TRANSPTR";
		System.out.println("<<START>><<"+table_name+">>");
		
		data = "";
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,,,,,,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		
		data = "";
		logger_count = 0;
		skipped_count = 0;   
		total_count = 0;  

		queryString1 = "INSERT INTO FMS_LTCORA_CONT_TRANSPTR(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRANSPORTER_CD,PLANT_SEQ_NO,ENT_BY,ENT_DT,AGMT_TYPE)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_TRANSPTR_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_TRANSPTR_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, TRANSPORTER_CD, PLANT_SEQ_NO, AGMT_TYPE, TIMESTAMP,", conn);
			
			while (rowIterator.hasNext()) {
				total_count++;  
				
				agmt_no = ""; agmt_rev = "";seq_no="";
				abbr = "";
				cd = "0";
				agmt_type = "";
    			cont_type = "";
    			cont_no = "";
    			cont_rev = "";
				String trans_abbr="", agmt_rev_no="";
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
							abbr = abbr.substring(1, abbr.length() - 3);
						}
						agmt_rev_no = (cell.getStringCellValue().contains("'null'") ? "'NULL'" : cell.getStringCellValue());
			    		agmt_rev_no = agmt_rev_no.substring(1, agmt_rev_no.length()-1);
			    		if(!agmt_rev_no.contains("NULL")) {
			    			agmt_rev_no = agmt_rev_no.split("-")[1]; 
			    		}
			    		data = company_cd;
			    		
			    	}
			    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
			    		
			    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
							cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
						}
			    		
						agmt_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!agmt_ref.equals("NULL")) {
							agmt_ref = agmt_ref.substring(1, agmt_ref.length() - 3);
						}
			    		
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
			    		
			    		if(cont_ref.startsWith("R")) {
			    			
			    		}
			    		queryString3 = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND AGMT_REV = ? AND COMPANY_CD = '2' ORDER BY CONT_REV ";
			    		stmt3 = conn.prepareStatement(queryString3);
			    		stmt3.setString(1, cont_ref);
			    		stmt3.setString(2, cd);
			    		stmt3.setString(3, agmt_rev_no);
			    		rset3 = stmt3.executeQuery();
			    		
			    		while (rset3.next()) {
			    			agmt_type = rset3.getString(1);
			    			agmt_no = rset3.getString(2);
			    			agmt_rev = rset3.getString(3);
			    			cont_type = rset3.getString(4);
			    			cont_no = rset3.getString(5);
			    			cont_rev = rset3.getString(6);
			    		}
			    		
			    		rset3.close();
			    		stmt3.close();
			    	}
			    	else if(cell.getColumnIndex() == 3) {				    					    						    		
			    		data = agmt_no;					    	
			    	}					    		
			    	else if(cell.getColumnIndex() == 4) {				    					    		
			    		data = agmt_rev;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 5) {				    					    		
			    		data = cont_no;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 6) {				    					    		
			    		data  = cont_rev;					    	
			    	}	
			    	else if(cell.getColumnIndex() == 7) {				    						    					    						    		
		    			data = cont_type;			    						    			
			    	}
			    	else if (cell.getColumnIndex() == 8) { //trans_abbr
			    		trans_abbr = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
			    		if (trans_abbr != null) {
			    			trans_abbr = trans_abbr.substring(1, trans_abbr.length() - 1);
						}
			    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1,trans_abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			trans_cd = rset.getString(1);
			    		} else {
			    			trans_cd = "";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = trans_cd;
			    	}
			    	else if(cell.getColumnIndex() == 12) {				    					    		
			    		data  = agmt_type;					    		
			    	}
			    	else {	
			    						    		
			    		if (cell.getColumnIndex() == 9) {	// PLANT_SEQ_NO
			    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (seq_no != null) {
								seq_no = seq_no.substring(1, seq_no.length() - 1);
							}
			    		}
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}
//			    	System.out.println(index + ":" + data);
			    	stmt1.setString(index++, data);
			    }
			     
			    queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_TRANSPTR "
			    		    + " WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? "
			    		    + "AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND TRANSPORTER_CD = ? AND PLANT_SEQ_NO = ? AND AGMT_TYPE = 'A' ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
		    	stmt.setString(4, agmt_rev); 
		    	stmt.setString(5, cont_no);
		    	stmt.setString(6, cont_rev);
		    	stmt.setString(7, cont_type);
		    	stmt.setString(8, trans_cd);
		    	stmt.setString(9, seq_no);
		    	rset = stmt.executeQuery();
		    	
			    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
					logger.data(fname, (company_cd+"," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev+ "," + cont_type + "," + trans_cd + "," + seq_no + ","+ agmt_type + "," ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+"," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev+ "," + cont_type + "," + trans_cd + "," + seq_no + ","+ agmt_type + ","), conn, "E");
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

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,,,", conn);
		
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

public void FMS_LTCORA_CONT_PLANT() throws IOException, SQLException {

	function_nm="FMS_LTCORA_CONT_PLANT()";
	try {
		
		table_name = "FMS_LTCORA_CONT_PLANT";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 

		
		queryString1 = "INSERT INTO FMS_LTCORA_CONT_PLANT(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,PLANT_SEQ_NO,ENT_BY,ENT_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_PLANT_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_PLANT_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
			
			while (rowIterator.hasNext()) {
				agmt_no = ""; agmt_rev = ""; seq_no = ""; agmt_type = "";
				abbr = ""; cont_no = ""; cont_type=""; cont_rev="";
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
			    		
			    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
							cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
						}
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		} else {
			    			cd ="";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    		
			    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2' ORDER BY CONT_REV ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, cont_ref);
			    		stmt.setString(2, cd);
			    		rset = stmt.executeQuery();
			    		while (rset.next()) {
			    			agmt_type = rset.getString(1);
			    			agmt_no = rset.getString(2);
			    			agmt_rev = rset.getString(3);
			    			cont_type = rset.getString(4);
			    			cont_no = rset.getString(5);
			    			cont_rev = rset.getString(6);
			    		}
			    		rset.close();
			    		stmt.close();
			    	}
			    	else if(cell.getColumnIndex() == 3) {				    					    						    		
			    		data = agmt_type;					    	
			    	}
			    	else if(cell.getColumnIndex() == 4) {				    					    						    		
			    		data = agmt_no;					    	
			    	}					    		
			    	else if(cell.getColumnIndex() == 5) {				    					    		
			    		data = agmt_rev;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 6) {				    					    		
			    		data = cont_type;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 7) {				    					    		
			    		data  = cont_no;					    	
			    	}	
			    	else if(cell.getColumnIndex() == 8) {				    						    					    						    		
		    			data = cont_rev;			    						    			
			    	}
			    	else {
			    		
			    		if (cell.getColumnIndex() == 9) {	// PLANT_SEQ_NO
			    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (seq_no != null) {
								seq_no = seq_no.substring(1, seq_no.length() - 1);
							}
			    		}
						
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}
			    	stmt1.setString(index++, data);
			    }
			     
		    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_PLANT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
		    			+ "AND AGMT_NO = ? AND AGMT_REV = ? AND  PLANT_SEQ_NO = ?  AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
		    	stmt.setString(4, agmt_rev);
		    	stmt.setString(5, seq_no);
		    	stmt.setString(6, cont_no);
		    	stmt.setString(7, cont_rev);
		    	stmt.setString(8, cont_type);
		    	rset = stmt.executeQuery();
		    	

			    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
					//System.out.println(queryString1);
					
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + "," + cont_type + "," + cont_no + "," + cont_rev + "," + seq_no + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + "," + cont_type + "," + cont_no + "," + cont_rev + "," + seq_no + ","), conn, "E");
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
		//conn.commit();
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

public void FMS_LTCORA_CONT_LIABILITY() throws IOException, SQLException {
	
	function_nm="FMS_LTCORA_CONT_LIABILITY()";
	try {
		
		table_name = "FMS_LTCORA_CONT_LIABILITY";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		String ld_promise = "",top_promise="";
		
		queryString1 = "INSERT INTO FMS_LTCORA_CONT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,"
				+ "CONTRACT_TYPE,CONT_NO,CONT_REV,LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,LIAB_TAKE_PAY,"
				+ "TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,"
				+ "TAKE_PAY_RMK,LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_BY,"
				+ "ENT_DT,MODIFY_DT,MODIFY_BY,REV_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_LIABILITY_"+start_end_dt+".xlsx");
		
		if(file1.exists()) {
			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_LIABILITY_"+start_end_dt+".xlsx"));
			
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}
			
			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,TIMESTAMP,", conn);
			
			while (rowIterator.hasNext()) {
				total_count++; 
				agmt_no = ""; agmt_rev = "";
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
					
			    	if (cell.getColumnIndex() == 0) {
			    		
			    		abbr = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
			    		if (abbr != null) {
							abbr = abbr.substring(1, abbr.length() - 1);
						}
						data = company_cd;
			    	}
			    	else if (cell.getColumnIndex() == 1) {	// Counterparty_Cd
			    		
			    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
							cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
						}
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		} else {
			    			cd ="";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    		
			    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2'";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, cont_ref);
			    		stmt.setString(2, cd);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			agmt_type = rset.getString(1);
			    			agmt_no = rset.getString(2);
			    			agmt_rev = rset.getString(3);
			    			cont_type = rset.getString(4);
			    			cont_no = rset.getString(5);
			    			cont_rev = rset.getString(6);
			    		}
			    		else {
			    			agmt_type = "";
			    			agmt_no = "";
			    			agmt_rev = "";
			    			cont_type = "";
			    			cont_no = "";
			    			cont_rev = "";
			    		}				    		
			    		rset.close();
			    		stmt.close();
			    	}
			    	else if(cell.getColumnIndex() == 3) {				    					    						    		
			    		data = agmt_type;					    	
			    	}
			    	else if(cell.getColumnIndex() == 4) {				    					    						    		
			    		data = agmt_no;					    	
			    	}					    		
			    	else if(cell.getColumnIndex() == 5) {				    					    		
			    		data = agmt_rev;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 6) {				    					    		
			    		data = cont_type;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 7) {				    					    		
			    		data  = cont_no;					    	
			    	}	
			    	else if(cell.getColumnIndex() == 8) {				    						    					    						    		
		    			data = cont_rev;			    						    			
			    	}
			    	
			    	else if(cell.getColumnIndex() == 11) {
			    		ld_promise = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!ld_promise.equals("NULL")) {
							ld_promise = ld_promise.substring(1, ld_promise.length() - 1);
						}
						if("Daily".equals(ld_promise)) {
			    			ld_promise = "D";
			    		}
			    		else if("Weekly".equals(ld_promise)) {
			    			ld_promise = "W";
			    		}
			    		else if("Fortnightly".equals(ld_promise)) {
			    			ld_promise = "F";
			    		}
			    		else if("Monthly".equals(ld_promise)) {
			    			ld_promise = "M";
			    		}
			    		else if("Quarterly".equals(ld_promise)) {
			    			ld_promise = "Q";
			    		}
			    		else if("Invoice Cycle".equals(ld_promise)) {
			    			ld_promise = "IC";
			    		}
			    		else if("TCQ".equals(ld_promise)) {
			    			ld_promise = "T";
			    		}
			    		else if("Defined Period".equals(ld_promise)) {
			    			ld_promise = "DP";
			    		}
			    		else if("Supply Period".equals(ld_promise)) {
			    			ld_promise = "SP";
			    		}else {
			    			ld_promise = null;
			    		}
			    		
			    		data = ld_promise;
			    	}
			    	else if(cell.getColumnIndex() == 17) {
			    		top_promise = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!top_promise.equals("NULL")) {
							top_promise = top_promise.substring(1, top_promise.length() - 1);
						}
						if("Daily".equals(top_promise)) {
			    			top_promise = "D";
			    		}
			    		else if("Weekly".equals(top_promise)) {
			    			top_promise = "W";
			    		}
			    		else if("Fortnightly".equals(top_promise)) {
			    			top_promise = "F";
			    		}
			    		else if("Monthly".equals(top_promise)) {
			    			top_promise = "M";
			    		}
			    		else if("Quarterly".equals(top_promise)) {
			    			top_promise = "Q";
			    		}
			    		else if("Invoice Cycle".equals(top_promise)) {
			    			top_promise = "IC";
			    		}
			    		else if("TCQ".equals(top_promise)) {
			    			top_promise = "T";
			    		}
			    		else if("Defined Period".equals(top_promise)) {
			    			top_promise = "DP";
			    		}
			    		else if("Supply Period".equals(top_promise)) {
			    			top_promise = "SP";
			    		}
			    		else {
			    			top_promise = null;
			    		}
			    		
			    		data = top_promise;
			    	}
			    	
			    	else {
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}	
			    	//System.out.println(index+"-"+data);
			    	stmt1.setString(index++, data);		    	
			    }
				
			    queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_LIABILITY WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND AGMT_TYPE = ? AND BUY_SALE = 'C' AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
		    	stmt.setString(4, agmt_rev);	
		    	stmt.setString(5, agmt_type);	
		    	stmt.setString(6, cont_type);
		    	stmt.setString(7, cont_no);	
		    	stmt.setString(8, cont_rev);	

	    	
		    	rset = stmt.executeQuery();
		    	
		    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + " , " + agmt_type + " , " + agmt_no + "," + agmt_rev + "," +cont_type+ ","+ cont_no + "," +cont_rev+ ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
				}
		    	 else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + " , " + agmt_type + " , " + agmt_no + "," + agmt_rev + "," +cont_type+ ","+ cont_no + "," +cont_rev+ ","), conn, "E");
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

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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

public void FMS_LTCORA_CONT_BILLING_DTL() throws IOException, SQLException {

	function_nm="FMS_LTCORA_CONT_BILLING_DTL()";
	try {
		
		table_name = "FMS_LTCORA_CONT_BILLING_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 

		queryString1 = "INSERT INTO FMS_LTCORA_CONT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
				+ "BILLING_FREQ,BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
				+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,BILLING_DAYS,"
				+ "EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,?,?,?,?,?,?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_BILLING_DTL_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_BILLING_DTL_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO,TIMESTAMP,", conn);
			
			while (rowIterator.hasNext()) {
				total_count++;
				agmt_no = ""; agmt_rev = ""; agmt_type="";
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
			    		
			    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
							cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
						}
			    		
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		} else {
			    			cd ="";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    		
			    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2'";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, cont_ref);
			    		stmt.setString(2, cd);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			agmt_type = rset.getString(1);
			    			agmt_no = rset.getString(2);
			    			agmt_rev = rset.getString(3);
			    			cont_type = rset.getString(4);
			    			cont_no = rset.getString(5);
			    			cont_rev = rset.getString(6);
			    		}
			    		else {
			    			agmt_type = "";
			    			agmt_no = "";
			    			agmt_rev = "";
			    			cont_type = "";
			    			cont_no = "";
			    			cont_rev = "";
			    		}				    		
			    		rset.close();
			    		stmt.close();
			    	}
			    	else if(cell.getColumnIndex() == 3) {				    					    						    		
			    		data = agmt_type;					    	
			    	}
			    	else if(cell.getColumnIndex() == 4) {				    					    						    		
			    		data = agmt_no;					    	
			    	}					    		
			    	else if(cell.getColumnIndex() == 5) {				    					    		
			    		data = agmt_rev;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 6) {				    					    		
			    		data = cont_no;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 7) {				    					    		
			    		data  = cont_rev;					    	
			    	}	
			    	else if(cell.getColumnIndex() == 8) {				    						    					    						    		
		    			data = cont_type;			    						    			
			    	}
			    	else if(cell.getColumnIndex() == 15) {
			    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if (name != null) {
							name = name.substring(1, name.length() - 1).trim();
						}
						queryString = "SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE UPPER(INT_RATE_NM) LIKE ? ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, "%"+name+"%");
				    	rset = stmt.executeQuery();
				    	if (rset.next()) {
				    		exchg_cd = rset.getString(1);
				    	}else {
				    		exchg_cd =  null;
				    	}
				    	rset.close();
				    	stmt.close();
				    	data = exchg_cd;
			    	}
			    	else if(cell.getColumnIndex() == 18) {
			    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if (name!=null) {
							name = name.substring(1, name.length() - 1);
						}
						queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) LIKE ? ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, "%"+name+"%");
				    	rset = stmt.executeQuery();
				    	if (rset.next()) {
				    		exchg_cd = rset.getString(1);
				    	}
				    	rset.close();
				    	stmt.close();
				    	data = exchg_cd;
			    	}
//			    	else if (cell.getColumnIndex() == 32) { //PLANT_SEQ_NO
//			    		
//			    		queryString = "SELECT PLANT_SEQ_NO FROM FMS_LTCORA_CONT_PLANT WHERE COUNTERPARTY_CD = ? AND AGMT_NO= ? AND AGMT_REV= ? AND AGMT_TYPE= ? "
//			    				+ "AND CONT_NO= ? AND CONT_REV= ? AND CONTRACT_TYPE= ?";
//			    		stmt = conn.prepareStatement(queryString);
//			    		stmt.setString(1, cd);
//			    		stmt.setString(2, agmt_no);
//			    		stmt.setString(3, agmt_rev);
//			    		stmt.setString(4, agmt_type);
//			    		stmt.setString(5, cont_no);
//			    		stmt.setString(6, cont_rev);
//			    		stmt.setString(7, cont_type);
//			    		
//			    		rset = stmt.executeQuery();
//			    		if (rset.next()) {
//			    			seq_no= rset.getString(1);
//			    		} else {
//			    			seq_no = "";
//			    		}
//			    		rset.close();
//			    		stmt.close();
//			    		data = seq_no;
//			    		
//			    	}
			    	
			    	else if(cell.getColumnIndex() == 22) {
			    		name = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if (name != null) {
							name = name.substring(1, name.length() - 1);
						}
						queryString = "SELECT TAX_STRUCT_CD FROM FMS_ENTITY_SERVICE_TAX_DTL WHERE UPPER(TAX_STRUCT_DTL) = ? ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, name);
				    	rset = stmt.executeQuery();
				    	if (rset.next()) {
				    		tax_cd = rset.getString(1);
				    	}else {
				    		tax_cd =  null;
				    	}
				    	rset.close();
				    	stmt.close();
				    	data = tax_cd;
			    	}

			    	else {
			    		if (cell.getColumnIndex() == 32) {	// PLANT_SEQ_NO
			    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (seq_no != null) {
								seq_no = seq_no.substring(1, seq_no.length() - 1);
							}
			    		}
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}	
//			    	System.out.println(index+"=="+data);
			    	stmt1.setString(index++, data);
			    }
			     
		    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_BILLING_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_NO = ? AND AGMT_TYPE = 'A' AND AGMT_REV = ? AND CONT_NO= ? AND CONT_REV= ? AND CONTRACT_TYPE= ? AND PLANT_SEQ_NO = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
		    	stmt.setString(4, agmt_rev);
		    	stmt.setString(5, cont_no);
	    		stmt.setString(6, cont_rev);
	    		stmt.setString(7, cont_type);
	    		stmt.setString(8, seq_no);
		    	rset = stmt.executeQuery();
		    	

			    if (!rset.next() && !cd.equals("") && !seq_no.equals("") && !agmt_no.equals("")) {
					//System.out.println(queryString1);
					
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_no + "," + cont_rev + "," + cont_type + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_no + "," + cont_rev + "," + cont_type + ","), conn, "E");
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
		//conn.commit();
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

public void FMS_LTCORA_CONT_STRG_CRG() throws IOException, SQLException {

	function_nm="FMS_LTCORA_CONT_STRG_CRG()";
	try {
		
		table_name = "FMS_LTCORA_CONT_STRG_CRG";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 

		queryString1 = "INSERT INTO FMS_LTCORA_CONT_STRG_CRG(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,SEQ_NO,FROM_DAYS,TO_DAYS,STORAGE_RATE,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,TILL_END)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_STRG_CRG_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_STRG_CRG_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,SEQ_NO,TIMESTAMP,", conn);
			
			while (rowIterator.hasNext()) {
				total_count++;
				agmt_no = ""; agmt_rev = ""; agmt_type="";
				abbr = "";
				cd = "0";
				data = null;
		    	String seq_no = "";
				
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
			    		
			    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
							cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
						}
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		} else {
			    			cd ="";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    		
			    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2'";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, cont_ref);
			    		stmt.setString(2, cd);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			agmt_type = rset.getString(1);
			    			agmt_no = rset.getString(2);
			    			agmt_rev = rset.getString(3);
			    			cont_type = rset.getString(4);
			    			cont_no = rset.getString(5);
			    			cont_rev = rset.getString(6);
			    		}
			    		else {
			    			agmt_type = "";
			    			agmt_no = "";
			    			agmt_rev = "";
			    			cont_type = "";
			    			cont_no = "";
			    			cont_rev = "";
			    		}				    		
			    		rset.close();
			    		stmt.close();
			    	}
			    	else if(cell.getColumnIndex() == 3) {				    					    						    		
			    		data = agmt_type;					    	
			    	}
			    	else if(cell.getColumnIndex() == 4) {				    					    						    		
			    		data = agmt_no;					    	
			    	}					    		
			    	else if(cell.getColumnIndex() == 5) {				    					    		
			    		data = agmt_rev;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 6) {				    					    		
			    		data = cont_type;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 7) {				    					    		
			    		data  = cont_no;					    	
			    	}	
			    	else if(cell.getColumnIndex() == 8) {				    						    					    						    		
		    			data = cont_rev;			    						    			
			    	}
//			    	else if (cell.getColumnIndex() == 9) {	// seq_no
//		    			queryString = "SELECT MAX(SEQ_NO+1) FROM FMS_LTCORA_CONT_STRG_CRG WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' "
//		    					+ "AND AGMT_TYPE = 'A' AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? ";
//		    			stmt = conn.prepareStatement(queryString);
//		    			stmt.setString(1, cd);
//		    			stmt.setString(2, agmt_no);
//		    			stmt.setString(3, agmt_rev);
//		    			stmt.setString(4, cont_type);
//		    			stmt.setString(5, cont_no);
//		    			stmt.setString(6, cont_rev);
//			    		rset = stmt.executeQuery();
//			    		if (rset.next() && rset.getInt(1)>0) {
//			    			seq_no = rset.getInt(1)+"";
//			    		}else {
//			    			seq_no = cell.getStringCellValue();
//			    			if (seq_no != null) {
//								seq_no = seq_no.substring(1, seq_no.length() - 1);
//							}
//			    		}
//			    		rset.close();
//			    		stmt.close();
//			    		data = seq_no+""; 
//			    		
//		    		}
				
			    	else {
			    		if (cell.getColumnIndex() == 9) {	// ADQ_DT
			    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (seq_no != null) {
			    				seq_no = seq_no.substring(1, seq_no.length() - 1);
							}else {
								seq_no="";
							}
			    		}
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}	
			    	//System.out.println(index+"==="+data);
			    	stmt1.setString(index++, data);
			    }
			     
		    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_STRG_CRG WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_TYPE = 'A' AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND SEQ_NO = ?";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
    			stmt.setString(4, agmt_rev);
    			stmt.setString(5, cont_type);
    			stmt.setString(6, cont_no);
    			stmt.setString(7, cont_rev);
    			stmt.setString(8, seq_no);
		    	rset = stmt.executeQuery();
		    	

			    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
					//System.out.println(queryString1);
					
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + "," + cont_no + "," + cont_rev + ","+ seq_no + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + "," + cont_no + "," + cont_rev + ","+ seq_no + ","), conn, "E");
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
		//conn.commit();
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

public void FMS_LTCORA_CONT_CARGO_DTL() throws IOException, SQLException {

	function_nm="FMS_LTCORA_CONT_CARGO_DTL()";
	try {
		
		table_name = "FMS_LTCORA_CONT_CARGO_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 

		queryString1 = "INSERT INTO FMS_LTCORA_CONT_CARGO_DTL(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,"
				+ "CARGO_NO,CARGO_REF,SHIP_CD,SUPP_CD,EDQ_FROM_DT,EDQ_TO_DT,ACTUAL_RECPT_DT,EDQ_QTY,CSOC_QTY,BOE_QTY,BOE_NO,BOE_DT,QQ_NO,QQ_DT,ENT_BY,ENT_DT,"
				+ "MODIFY_DT,MODIFY_BY,STORAGE_DAYS,STORAGE_EXT_DAYS,CARGO_STATUS,SF_GEN_DT,ATTACH_LNG_CARGO,CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_CARGO_DTL_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_CARGO_DTL_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,TIMESTAMP,", conn);
			
			while (rowIterator.hasNext()) {
				total_count++;
				agmt_no = ""; agmt_rev = ""; agmt_type="";
				abbr = "";
				cd = "0";
				data = null;
				String cargo_no="",ship_cd="",supp_cd="";
				
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
			    		
			    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
							cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
						}
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		} else {
			    			cd ="";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    		
			    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2'";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, cont_ref);
			    		stmt.setString(2, cd);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			agmt_type = rset.getString(1);
			    			agmt_no = rset.getString(2);
			    			agmt_rev = rset.getString(3);
			    			cont_type = rset.getString(4);
			    			cont_no = rset.getString(5);
			    			cont_rev = rset.getString(6);
			    		}				
			    		else {
			    			agmt_type = "";
			    			agmt_no = "";
			    			agmt_rev = "";
			    			cont_type = "";
			    			cont_no = "";
			    			cont_rev = "";
			    		}	    		
			    		rset.close();
			    		stmt.close();
			    	}
			    	else if(cell.getColumnIndex() == 3) {				    					    						    		
			    		data = agmt_type;					    	
			    	}
			    	else if(cell.getColumnIndex() == 4) {				    					    						    		
			    		data = agmt_no;					    	
			    	}					    		
			    	else if(cell.getColumnIndex() == 5) {				    					    		
			    		data = agmt_rev;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 6) {				    					    		
			    		data = cont_type;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 7) {				    					    		
			    		data  = cont_no;					    	
			    	}	
			    	else if(cell.getColumnIndex() == 8) {				    						    					    						    		
		    			data = cont_rev;			    						    			
			    	}
			    	else if(cell.getColumnIndex() == 9) {	
			    		cargo_no = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cargo_no.equals("NULL")) {
							cargo_no = cargo_no.substring(1, cargo_no.length() - 1);
						}
						data = cargo_no;
//			    		queryString = "SELECT MAX(CARGO_NO+1) FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' "
//		    					+ "AND AGMT_TYPE = 'A' AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? ";
//		    			stmt = conn.prepareStatement(queryString);
//		    			stmt.setString(1, cd);
//		    			stmt.setString(2, agmt_no);
//		    			stmt.setString(3, agmt_rev);
//		    			stmt.setString(4, cont_type);
//		    			stmt.setString(5, cont_no);
//		    			stmt.setString(6, cont_rev);
//			    		rset = stmt.executeQuery();
//			    		if (rset.next() && rset.getInt(1)>0) {
//			    			cargo_no = rset.getInt(1)+"";
//			    		}else {
//			    			cargo_no = cell.getStringCellValue();
//			    			cargo_no = cargo_no.substring(1, cargo_no.length()-1);
//			    		}
//			    		rset.close();
//			    		stmt.close();
//			    		data = cargo_no+""; 		    						    			
			    	}
			    	else if (cell.getColumnIndex() == 11) {	// Ship_cd
			    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    		queryString = "SELECT SHIP_CD FROM FMS_SHIP_MST WHERE SHIP_NAME = ? ";
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
			    	else if (cell.getColumnIndex() == 12) {	// Supp_cd
			    		data = (cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue());
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ?  ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, data);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
				    		supp_cd = rset.getString(1);
			    		}
			    		else  {
			    			supp_cd = null;
			    		}
			    		rset.close();
			    		stmt.close();
			    		data  = supp_cd;
			    	}
			    	else {
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}	
//			    	System.out.println(index+"=="+data);
			    	stmt1.setString(index++, data);
			    }
			     
		    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_NO = ? AND AGMT_TYPE = 'A' AND AGMT_REV = ? AND CONT_NO= ? AND CONT_REV= ? AND CONTRACT_TYPE= ? AND CARGO_NO = ?";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
		    	stmt.setString(4, agmt_rev);
		    	stmt.setString(5, cont_no);
	    		stmt.setString(6, cont_rev);
	    		stmt.setString(7, cont_type);
	    		stmt.setString(8, cargo_no);
		    	rset = stmt.executeQuery();
		    	

			    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
					//System.out.println(queryString1);
					
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + "," + cont_no + "," + cont_rev + ","+ cargo_no + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + "," + cont_no + "," + cont_rev + ","+ cargo_no + ","), conn, "E");
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
		//conn.commit();
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
public void LOG_LTCORA_CONT_CARGO_DTL() throws IOException, SQLException {

	function_nm="LOG_LTCORA_CONT_CARGO_DTL()";
	try {
		
		System.out.println("<<START>><<LOG_LTCORA_CONT_CARGO_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<LOG_LTCORA_CONT_CARGO_DTL>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		
		columns = "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,LOG_SEQ_NO,LOG_BY,LOG_DT,"
				+ "CARGO_REF,SHIP_CD,SUPP_CD,EDQ_FROM_DT,EDQ_TO_DT,ACTUAL_RECPT_DT,EDQ_QTY,CSOC_QTY,BOE_QTY,BOE_NO,BOE_DT,QQ_NO,QQ_DT,ENT_BY,ENT_DT,"
				+ "MODIFY_DT,MODIFY_BY,STORAGE_DAYS,STORAGE_EXT_DAYS,CARGO_STATUS,SF_GEN_DT,ATTACH_LNG_CARGO,CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,"
				+ "CLOSE_EFF_DT";
		
		queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NULL,ENT_BY,TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),CARGO_REF,SHIP_CD,"
				+ "SUPP_CD,TO_CHAR(EDQ_FROM_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(EDQ_TO_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(ACTUAL_RECPT_DT, 'DD/MM/YYYY HH24:MI:SS'),"
				+ "EDQ_QTY,CSOC_QTY,BOE_QTY,BOE_NO,TO_CHAR(BOE_DT, 'DD/MM/YYYY HH24:MI:SS'),QQ_NO,TO_CHAR(QQ_DT, 'DD/MM/YYYY HH24:MI:SS'),ENT_BY,"
				+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(MODIFY_DT, 'DD/MM/YYYY HH24:MI:SS'),MODIFY_BY,STORAGE_DAYS,STORAGE_EXT_DAYS,CARGO_STATUS,TO_CHAR(SF_GEN_DT, 'DD/MM/YYYY HH24:MI:SS'),ATTACH_LNG_CARGO,"
				+ "CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,TO_CHAR(CLOSE_EFF_DT, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = ? ORDER BY AGMT_NO,CONT_NO,ENT_DT ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		
		queryString1 = "INSERT INTO LOG_LTCORA_CONT_CARGO_DTL(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,"
				+ "LOG_SEQ_NO,LOG_BY,LOG_DT,CARGO_REF,SHIP_CD,SUPP_CD,EDQ_FROM_DT,EDQ_TO_DT,ACTUAL_RECPT_DT,EDQ_QTY,CSOC_QTY,BOE_QTY,BOE_NO,BOE_DT,QQ_NO,QQ_DT,ENT_BY,"
				+ "ENT_DT,MODIFY_DT,MODIFY_BY,STORAGE_DAYS,STORAGE_EXT_DAYS,CARGO_STATUS,SF_GEN_DT,ATTACH_LNG_CARGO,CLOSURE_REQUEST_FLAG,CLOSURE_ALLOC_QTY,CLOSE_EFF_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,LOG_SEQ_NO,TIMESTAMP,", conn);
		int log_seq_no = 1;
		String lastContNo = "";
		
		while (rset.next()) {
				stmt1 = conn.prepareStatement(queryString1);
				
				for(int i = 0;i < columns.split(",").length;i++) {
					data = "";
					
					data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
					
					if(i == 10) {
						String currentContNo = rset.getString(8);
					    if (!currentContNo.equals(lastContNo)) {
					        log_seq_no = 1;
					        lastContNo = currentContNo;
					    }else {
					    	log_seq_no++;
					    }
					    data = log_seq_no+"";
					}
					
					stmt1.setString(i+1,data);
				}
				
			//for data already exists..
			queryString5 = "SELECT COUNTERPARTY_CD FROM LOG_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_TYPE = 'A' AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_NO = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(5));
			stmt5.setString(4, rset.getString(6));
			stmt5.setString(5, rset.getString(7));
			stmt5.setString(6, rset.getString(8));
			stmt5.setString(7, rset.getString(9));
			stmt5.setString(8, rset.getString(10));
		
			rset5 = stmt5.executeQuery();
			
			if (!rset5.next() ) {
				
//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
				
				stmt1.executeUpdate();
				stmt1.close();
				
				logger_count++;
			}
			else {
				
				stmt1.close();
				skipped_count++; 
				
//				logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + inv_type + "," + tax_dtls[0] + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
				
			}
			stmt5.close();
			rset5.close();
			}
		rset.close();
		stmt.close();
		
		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
		System.out.println("<<END>><<LOG_LTCORA_CONT_CARGO_DTL>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_LTCORA_CONT_BU>>,", conn);
		
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
public void FMS_LTCORA_CONT_CARGO_ADQ() throws IOException, SQLException {

	function_nm="FMS_LTCORA_CONT_CARGO_ADQ()";
	try {
		
		table_name = "FMS_LTCORA_CONT_CARGO_ADQ";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 

		queryString1 = "INSERT INTO FMS_LTCORA_CONT_CARGO_ADQ(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ADQ_DT,ADQ_QTY,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_CARGO_ADQ_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_CARGO_ADQ_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,SEQ_NO,TIMESTAMP,", conn);
			
			while (rowIterator.hasNext()) {
				total_count++;
				agmt_no = ""; agmt_rev = ""; agmt_type="";
				abbr = "";
				cd = "0";
				data = null;
		    	String cargo_no = "", adq_dt="", cargo_ref="";
				
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
			    		
			    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
							cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
						}
			    		
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		} else {
			    			cd ="";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    		
			    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2'";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, cont_ref);
			    		stmt.setString(2, cd);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			agmt_type = rset.getString(1);
			    			agmt_no = rset.getString(2);
			    			agmt_rev = rset.getString(3);
			    			cont_type = rset.getString(4);
			    			cont_no = rset.getString(5);
			    			cont_rev = rset.getString(6);
			    		}	
			    		else {
			    			agmt_type = "";
			    			agmt_no = "";
			    			agmt_rev = "";
			    			cont_type = "";
			    			cont_no = "";
			    			cont_rev = "";
			    		}				    		
			    		rset.close();
			    		stmt.close();
			    	}
			    	else if(cell.getColumnIndex() == 3) {				    					    						    		
			    		data = agmt_type;					    	
			    	}
			    	else if(cell.getColumnIndex() == 4) {				    					    						    		
			    		data = agmt_no;					    	
			    	}					    		
			    	else if(cell.getColumnIndex() == 5) {				    					    		
			    		data = agmt_rev;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 6) {				    					    		
			    		data = cont_type;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 7) {				    					    		
			    		data  = cont_no;					    	
			    	}	
			    	else if(cell.getColumnIndex() == 8) {				    						    					    						    		
		    			data = cont_rev;	
			    	}
			    	
			    	else if (cell.getColumnIndex() == 9) {	// cargo_no
			    		
			    		cargo_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
			    			cargo_ref = cargo_ref.substring(1, cargo_ref.length() - 1);
						}
						queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' "
		    					+ "AND AGMT_TYPE = 'A' AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_REF = ?";
		    			stmt = conn.prepareStatement(queryString);
		    			stmt.setString(1, cd);
		    			stmt.setString(2, agmt_no);
		    			stmt.setString(3, agmt_rev);
		    			stmt.setString(4, cont_type);
		    			stmt.setString(5, cont_no);
		    			stmt.setString(6, cont_rev);
		    			stmt.setString(7, cargo_ref);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cargo_no = rset.getString(1);
			    		}else {
			    			cargo_no = "0";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cargo_no; 
		    		}
			    	else {
			    		
			    		if (cell.getColumnIndex() == 10) {	// ADQ_DT
			    			adq_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (adq_dt != null) {
			    				adq_dt = adq_dt.substring(1, adq_dt.length() - 1);
							}else {
								adq_dt="";
							}
			    		}
			    		
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}	
			    	stmt1.setString(index++, data);
			    }
			     
		    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_CARGO_ADQ WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_TYPE = 'A' AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_NO = ? AND ADQ_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss')";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
    			stmt.setString(4, agmt_rev);
    			stmt.setString(5, cont_type);
    			stmt.setString(6, cont_no);
    			stmt.setString(7, cont_rev);
    			stmt.setString(8, cargo_no);
    			stmt.setString(9, adq_dt);
		    	rset = stmt.executeQuery();
		    	

			    if (!rset.next() && !cd.equals("") && !adq_dt.equals("") && !agmt_type.equals("") ) {
					//System.out.println(queryString1);
					
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + "," + cont_no + "," + cont_rev + ","+ seq_no + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + "," + cont_no + "," + cont_rev + ","+ seq_no + ","), conn, "E");
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
		//conn.commit();
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

public void FMS_LTCORA_CONT_CARGO_CSOC() throws IOException, SQLException {

	function_nm="FMS_LTCORA_CONT_CARGO_CSOC()";
	try {
		
		table_name = "FMS_LTCORA_CONT_CARGO_CSOC";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		String csoc_seq_no="";

		queryString1 = "INSERT INTO FMS_LTCORA_CONT_CARGO_CSOC(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,CSOC_SEQ_NO,FROM_DT,TO_DT,CSOC,REMARK,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
		stmt1 = conn.prepareStatement(queryString1);
		
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_CARGO_CSOC_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_CARGO_CSOC_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,CSOC_SEQ_NO,TIMESTAMP,", conn);
			
			while (rowIterator.hasNext()) {
				total_count++;
				agmt_no = ""; agmt_rev = ""; agmt_type="";
				abbr = "";
				cd = "0";
				data = null;
		    	String cargo_no = "";
				
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
			    		
			    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
							cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
						}
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		} else {
			    			cd ="";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    		
			    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ?";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, cont_ref);
			    		stmt.setString(2, cd);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			agmt_type = rset.getString(1);
			    			agmt_no = rset.getString(2);
			    			agmt_rev = rset.getString(3);
			    			cont_type = rset.getString(4);
			    			cont_no = rset.getString(5);
			    			cont_rev = rset.getString(6);
			    		}			
			    		else {
			    			agmt_type = "";
			    			agmt_no = "";
			    			agmt_rev = "";
			    			cont_type = "";
			    			cont_no = "";
			    			cont_rev = "";
			    		}		    		
			    		rset.close();
			    		stmt.close();
			    	}
			    	else if(cell.getColumnIndex() == 3) {				    					    						    		
			    		data = agmt_type;					    	
			    	}
			    	else if(cell.getColumnIndex() == 4) {				    					    						    		
			    		data = agmt_no;					    	
			    	}					    		
			    	else if(cell.getColumnIndex() == 5) {				    					    		
			    		data = agmt_rev;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 6) {				    					    		
			    		data = cont_type;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 7) {				    					    		
			    		data  = cont_no;					    	
			    	}	
			    	else if(cell.getColumnIndex() == 8) {				    						    					    						    		
		    			data = cont_rev;	
			    	}
			    	else {
			    		
			    		if (cell.getColumnIndex() == 9) {	// cargo_no
			    			cargo_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (cargo_no != null) {
			    				cargo_no = cargo_no.substring(1, cargo_no.length() - 1);
							}
			    		}
			    		else if (cell.getColumnIndex() == 10) {	// csoc_seq_no
			    			csoc_seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (csoc_seq_no != null) {
			    				csoc_seq_no = csoc_seq_no.substring(1, csoc_seq_no.length() - 1);
							}
			    		}
			    		
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}
//			    	System.out.println(index+"=="+data);
			    	stmt1.setString(index++, data);
			    }
			     
		    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_CARGO_CSOC WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_TYPE = 'A' AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_NO = ? AND CSOC_SEQ_NO = ?";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
    			stmt.setString(4, agmt_rev);
    			stmt.setString(5, cont_type);
    			stmt.setString(6, cont_no);
    			stmt.setString(7, cont_rev);
    			stmt.setString(8, cargo_no);
    			stmt.setString(9, csoc_seq_no);
		    	rset = stmt.executeQuery();
		    	

			    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
					//System.out.println(queryString1);
					
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + "," + cont_no + "," + cont_rev + ","+ cargo_no + ","+ csoc_seq_no + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + "," + cont_no + "," + cont_rev + ","+ cargo_no + ","+ csoc_seq_no + ","), conn, "E");
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
		//conn.commit();
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

public void FMS_LTCORA_CONT_CARGO_MOD() throws IOException, SQLException {

	function_nm="FMS_LTCORA_CONT_CARGO_MOD()";
	try {
		
		table_name = "FMS_LTCORA_CONT_CARGO_MOD";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 

		queryString1 = "INSERT INTO FMS_LTCORA_CONT_CARGO_MOD(COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APPROVAL_FLAG,APPROVE_BY,APPROVAL_DT)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_CARGO_MOD_"+start_end_dt+".xlsx");
		if(file1.exists()) {

			
			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_LTCORA_CONT_CARGO_MOD_"+start_end_dt+".xlsx"));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			
			// Below block of code is for unique SEIPL data
			rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {	// For skipping the first row
				rowIterator.next();
			}

			logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,CSOC_SEQ_NO,TIMESTAMP,", conn);
			
			while (rowIterator.hasNext()) {
				total_count++;
				agmt_no = ""; agmt_rev = ""; agmt_type="";
				abbr = "";
				cd = "0";
				data = null;
		    	String cargo_no = "";
				
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
			    		
			    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if (!cont_ref.equals("NULL")) {
							cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
						}
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		} else {
			    			cd ="";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    		
			    		queryString = "SELECT AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD = '2'";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, cont_ref);
			    		stmt.setString(2, cd);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			agmt_type = rset.getString(1);
			    			agmt_no = rset.getString(2);
			    			agmt_rev = rset.getString(3);
			    			cont_type = rset.getString(4);
			    			cont_no = rset.getString(5);
			    			cont_rev = rset.getString(6);
			    		}		
			    		else {
			    			agmt_type = "";
			    			agmt_no = "";
			    			agmt_rev = "";
			    			cont_type = "";
			    			cont_no = "";
			    			cont_rev = "";
			    		}			    		
			    		rset.close();
			    		stmt.close();
			    	}
			    	else if(cell.getColumnIndex() == 3) {				    					    						    		
			    		data = agmt_type;					    	
			    	}
			    	else if(cell.getColumnIndex() == 4) {				    					    						    		
			    		data = agmt_no;					    	
			    	}					    		
			    	else if(cell.getColumnIndex() == 5) {				    					    		
			    		data = agmt_rev;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 6) {				    					    		
			    		data = cont_type;					    		
			    	}	
			    	else if(cell.getColumnIndex() == 7) {				    					    		
			    		data  = cont_no;					    	
			    	}	
			    	else if(cell.getColumnIndex() == 8) {				    						    					    						    		
		    			data = cont_rev;	
			    	}
			    	else {
			    		
			    		if (cell.getColumnIndex() == 9) {	// cargo_no
			    			cargo_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (cargo_no != null) {
			    				cargo_no = cargo_no.substring(1, cargo_no.length() - 1);
							}
			    		}
			    		
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}
			    	//System.out.println(index+"=="+data);
			    	stmt1.setString(index++, data);
			    }
			     
		    	queryString = "SELECT COUNTERPARTY_CD FROM FMS_LTCORA_CONT_CARGO_MOD WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND BUY_SALE = 'C' AND AGMT_TYPE = 'A' AND AGMT_NO = ? AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND CARGO_NO = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, agmt_no);
    			stmt.setString(4, agmt_rev);
    			stmt.setString(5, cont_type);
    			stmt.setString(6, cont_no);
    			stmt.setString(7, cont_rev);
    			stmt.setString(8, cargo_no);
		    	rset = stmt.executeQuery();
		    	

			    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
					//System.out.println(queryString1);
					
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + "," + cont_no + "," + cont_rev + ","+ cargo_no + ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+"," + cd + " , " + buy_sale + "," + agmt_type + "," + agmt_no + "," + agmt_rev + ","+ cont_type + "," + cont_no + "," + cont_rev + ","+ cargo_no + ","), conn, "E");
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
		//conn.commit();
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
	
	public void FMS_CONT_PRICE_DTL() throws IOException, SQLException {

		function_nm="FMS_CONT_PRICE_DTL(S)()";
		try {
			table_name = "FMS_CONT_PRICE_DTL(S)";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_CONT_PRICE_DTL(S)>>,,,," , conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;

//	    	queryString1 = "INSERT INTO FMS_CONT_PRICE_DTL VALUES(";
	    	
//			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, table_name);
//			rset = stmt.executeQuery();
//			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//			}
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//				System.out.println(rset.getString(1));
//			}
			
//			rset.close();
//			stmt.close();
//			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			queryString1="INSERT INTO FMS_CONT_PRICE_DTL(COMPANY_CD, MAPPING_ID, CONTRACT_TYPE, SEQ_NO, FROM_DT, TO_DT, PRICE_TYPE,"
					+ " CURVE_NM, SLOPE, CONST, QUANTITY_UNIT, RATE, RATE_UNIT, REMARKS, ENT_BY, ENT_DT, MODIFY_BY, MODIFY_DT, PRICE_RANGE,"
					+ " PRICE_START_DT, PRICE_END_DT, PHYS_CURVE_NM, CURVE_LOGIC, FORMULA)"
					+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL(S)_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_DTL(S)_"+start_end_dt+".xlsx"));

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
					String seq_no = "",map_id="",cont_type="";
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
				    			cd=rset.getString(1);
				    		}else  {
				    			cd = "";
				    		}	
				    		
				    		rset.close();
				    		stmt.close();
					    					    						    						    		
				    		map_id  = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());		
				    		if (!map_id.equals("NULL")) {
								map_id = map_id.substring(1, map_id.length() - 1);
							}
							if(map_id.startsWith("L")) {
				    			String[] parts = map_id.split("-");	
				    			agmt_no = parts[2];
					            cont_no = parts[4];
					            cont_rev = parts[5];
					            
					            cont_ref = "L-"+agmt_no+"-"+cont_no+"-"+cont_rev;
					            
					            queryString2 = "SELECT CONT_NO FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = 'L' ";
					    		stmt2 = conn.prepareStatement(queryString2);
					    		stmt2.setString(1, cd);
					    		stmt2.setString(2, cont_ref);
					    		rset2 = stmt2.executeQuery();
					    		if (rset2.next()) {					    		
					    			cont_no = rset2.getString(1);					    			
					    		} else {
					    			cont_no = "";

					    		}
					    		rset2.close();
					    		stmt2.close();
				    		}else {
				    			String[] parts = map_id.split("-");	
					            agmt_no = parts[1];
					            cont_no = parts[3];
				    		}
				            
				    		map_id = cd+"-"+agmt_no+"-"+cont_no;	
				    		
				    		data = map_id;
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
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();;
				    			if (seq_no!=null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}				    	
				    		if (cell.getColumnIndex() == 2) {	// cont_type
				    			cont_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();;
				    			if (cont_type!=null) {
									cont_type = cont_type.substring(1, cont_type.length() - 1);
								}
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
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !cont_no.equals("") && !cd.equals("")) {				
				    	//System.out.println(queryString1);
				    	logger.data(fname,(company_cd+","+map_id+" ,"+cont_type+" ,"+seq_no+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;				    	
				    }else {
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
						
			logger.checkpoint(fname, "<<END>><<FMS_CONT_PRICE_DTL>>", conn);
			
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
			if (file != null) {
				file.close();
			}
		}
		
	}
	
	public void FMS_CONT_PRICE_MIN_DTL() throws IOException, SQLException {

		function_nm="FMS_CONT_PRICE_MIN_DTL(S)()";
		try {
			table_name = "FMS_CONT_PRICE_MIN_DTL(S)";
			System.out.println("<<START>><<"+table_name+">>");
			logger.checkpoint(fname,"\n<<START>>,<<FMS_CONT_PRICE_MIN_DTL(S)>>,,,," , conn);
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;   
			total_count = 0;

//	    	queryString1 = "INSERT INTO FMS_CONT_PRICE_MIN_DTL VALUES(";
	    	
			queryString = "SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, table_name);
			rset = stmt.executeQuery();
			
//			while (rset.next()) {
//				if(rset.getString(2).equals("DATE")) {
//					queryString1 += "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),";
//				}
//				else {
//					queryString1 += "?,";
//				}
//				System.out.print(rset.getString(1)+",");
//			}
			rset.close();
			stmt.close();
			
//			queryString1 = queryString1.substring(0, queryString1.length()-1);
//			queryString1 += ")";
			
			queryString1="INSERT INTO FMS_CONT_PRICE_MIN_DTL(COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,FROM_DT,TO_DT,CURVE_LOGIC,CURVE_NM,SLOPE,"
					+ "CONST,QUANTITY_UNIT,RATE,RATE_UNIT,PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,REMARKS,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT)"
					+ " VALUES(?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, ?, "
					+ "?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ " ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss')"
					+ ")";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL(S)_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CONT_PRICE_MIN_DTL(S)_"+start_end_dt+".xlsx"));

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
					String agmt_no="";
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
				    			cd=rset.getString(1);
				    		}else  {
				    			cd = null;
				    		}	
				    		
				    		rset.close();
				    		stmt.close();
					    					    						    						    		
				    		map_id  = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());		
				    		if (!map_id.equals("NULL")) {
								map_id = map_id.substring(1, map_id.length() - 1);
							}
							if(map_id.startsWith("L")) {
				    			String[] parts = map_id.split("-");	
				    			agmt_no = parts[2];
					            cont_no = parts[4];
					            cont_rev = parts[5];
					            
					            cont_ref = "L-"+agmt_no+"-"+cont_no+"-"+cont_rev;
					            
					            queryString2 = "SELECT CONT_NO FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = 'L' ";
					    		stmt2 = conn.prepareStatement(queryString2);
					    		stmt2.setString(1, cd);
					    		stmt2.setString(2, cont_ref);
					    		rset2 = stmt2.executeQuery();
					    		if (rset2.next()) {					    		
					    			cont_no = rset2.getString(1);					    			
					    		} else {
					    			cont_no = null;

					    		}
					    		rset2.close();
					    		stmt2.close();
				    		}else {
				    			String[] parts = map_id.split("-");	
					            agmt_no = parts[1];
					            cont_no = parts[3];
				    		}
				            
				    		map_id = cd+"-"+agmt_no+"-"+cont_no;	
				    		
				    		data = map_id;
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
				    		
				    		if (cell.getColumnIndex() == 2) {	// cont_type
				    			cont_type = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (cont_type!=null) {
									cont_type = cont_type.substring(1, cont_type.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 3) {	// seq_no
				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_no!=null) {
									seq_no = seq_no.substring(1, seq_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 7) {	// curve_nm
				    			curve_nm = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (curve_nm!=null) {
									curve_nm = curve_nm.substring(1, curve_nm.length() - 1);
								}
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT SEQ_NO FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = ? AND MAPPING_ID = ? AND CONTRACT_TYPE = ? AND SEQ_NO = ? AND CURVE_NM = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, map_id);
			    	stmt.setString(3, cont_type);
			    	stmt.setString(4, seq_no);
			    	stmt.setString(5, curve_nm);
			    	rset = stmt.executeQuery();
			    	
				    if (row.getRowNum() != 0 && !rset.next() && !agmt_no.equals("") ) {
				    	//System.out.println(queryString1);		    	
				    	logger.data(fname,(company_cd+","+map_id+", "+cont_type+", "+seq_no+", "+curve_nm+","), conn,"");
				    	stmt1.executeUpdate();
				    	stmt1.close();
				    	logger_count++;
				    } else {
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
						
			logger.checkpoint(fname, "<<END>><<FMS_CONT_PRICE_MIN_DTL>>", conn);
			
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
			if (file != null) {
				file.close();
			}
		}
		
	}
		
	public void FMS_SECURITY_DEAL_MAP() throws IOException, SQLException {

		function_nm="FMS_SECURITY_DEAL_MAP(S)()";
		try {
			
			table_name = "FMS_SECURITY_DEAL_MAP(S)";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			
			queryString1 = "INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO, "
					+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?) ";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP(S)_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECURITY_DEAL_MAP(S)_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_ABBR,COUNTERPARTY_CD, SEQ_NO, SEC_REF_NO, MAP_SEQ_NO, SEQ_REV_NO, GX, AGMT_NO,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					String map_seq_no="",seq_rev_no="";
					abbr = "";seq_no="";
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
				    		cont_ref = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref.equals("NULL")) {
								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, abbr);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    		}else {
				    			cd = "";
				    		}
				    		rset.close();
				    		stmt.close();
				    		data = cd;
				    	}
				    	else if(cell.getColumnIndex() == 2) {	//SEQ_NO
//				    		queryString = "SELECT MAX(SEQ_NO)+1 FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? ";
//				    		stmt = conn.prepareStatement(queryString);
//				    		stmt.setString(1, company_cd);
//				    		stmt.setString(2, cd);
//				    		rset = stmt.executeQuery();
//				    		if (rset.next() && rset.getString(1)!=null && rset.getInt(1) > 0) {
//				    			seq_no = rset.getString(1);
//				    		}else {
//				    			seq_no = "1";
//				    		}
//				    		rset.close();
//				    		stmt.close();
				    		seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (seq_no != null) {
			    				seq_no = seq_no.substring(1, seq_no.length() - 1);
							}
				    		data = seq_no;
				    	}
				    	else if(cell.getColumnIndex() == 3) {	//MAP_SEQ_NO
//				    		queryString = "SELECT MAX(MAP_SEQ_NO)+1 FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? ";
//				    		stmt = conn.prepareStatement(queryString);
//				    		stmt.setString(1, company_cd);
//				    		stmt.setString(2, cd);
//				    		stmt.setString(3, seq_no);
//				    		rset = stmt.executeQuery();
//				    		if (rset.next() && rset.getString(1)!=null) {
//				    			map_seq_no = rset.getString(1);
//				    		}else {
//				    			map_seq_no = "1";
//				    		}
//				    		rset.close();
//				    		stmt.close();
				    		map_seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (map_seq_no != null) {
			    				map_seq_no = map_seq_no.substring(1, map_seq_no.length() - 1);
							}
				    		data = map_seq_no;
				    	}
						else if (cell.getColumnIndex() == 5) { //Agmt_no
				    		agmt_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
				    		if (agmt_no != null) {
								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
							}
				    		if(cont_ref.startsWith("L")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		stmt.setString(3, cont_ref.split("-")[0]);
					    		
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
				    		}else if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
				    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, cd);
					    		stmt.setString(2, cont_ref);
					    		
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			agmt_no = rset.getString(1);
					    			agmt_rev = rset.getString(2);
					    			cont_no = rset.getString(3);
					    			cont_rev = rset.getString(4);
					    			cont_type = rset.getString(5);
					    		} else {
					    			agmt_no = "";
					    			agmt_rev = "";
					    			cont_no = "";
					    			cont_rev = "";
					    			cont_type = "";
					    		}
					    		rset.close();
					    		stmt.close();
//					    		System.out.println(agmt_no+"="+agmt_rev+"="+cont_no+"="+cont_rev+"="+cont_type);
				    		}
				    		
							data = agmt_no;
				    	}
						else if (cell.getColumnIndex() == 6) { //Agmt_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
					    		agmt_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (agmt_rev != null) {
									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
								}
				    		}
							data = agmt_rev;
				    	}
				    	else if (cell.getColumnIndex() == 7) { //Cont_no
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
					    		cont_no = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_no != null) {
					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
								}
				    		}
				    		data = cont_no;
				    	}
			    		
				    	else if (cell.getColumnIndex() == 8) { //Cont_rev
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
					    		cont_rev = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_rev != null) {
					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
								}	
				    		}
				    		data = cont_rev;
				    	}
				    	else if (cell.getColumnIndex() == 9) { //contract_type
				    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
					    		cont_type = (cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue());
					    		if (cont_type != null) {
					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
								}	
				    		}
				    		data = cont_type;
				    	}
				    	else {
//				    		if (cell.getColumnIndex() == 2) {	// seq_no
//				    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//				    			if (seq_no != null) {
//				    				seq_no = seq_no.substring(1, seq_no.length() - 1);
//								}
//					    	}
//				    		if (cell.getColumnIndex() == 3) {	// map_seq_no
//				    			map_seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//				    			if (map_seq_no != null) {
//				    				map_seq_no = map_seq_no.substring(1, map_seq_no.length() - 1);
//								}
//				    		}
				    		if (cell.getColumnIndex() == 4) {	// sec_ref_no
				    			sec_ref_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (sec_ref_no != null) {
				    				sec_ref_no = sec_ref_no.substring(1, sec_ref_no.length() - 1);
								}
				    		}
				    		if (cell.getColumnIndex() == 16) {	// seq_rev_no
				    			seq_rev_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (seq_rev_no != null) {
				    				seq_rev_no = seq_rev_no.substring(1, seq_rev_no.length() - 1);
								}
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}
				    	//System.out.println(index+"-"+data);
				    	stmt1.setString(index++, data);
				    
				    }
//				    COMPANY_CD", "COUNTERPARTY_CD", "SEQ_NO", "MAP_SEQ_NO", "SEQ_REV_NO", "GX
					queryString = "SELECT COUNTERPARTY_CD FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
							+ "AND SEQ_NO =? AND MAP_SEQ_NO = ?"
							+ " AND  SEQ_REV_NO = ? AND GX = ? AND SEC_REF_NO = ? AND CONTRACT_TYPE = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? ";
					
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, cd);
					stmt.setString(3, seq_no);
					stmt.setString(4, map_seq_no);
					stmt.setString(5, seq_rev_no);
					stmt.setString(6, "K");
					stmt.setString(7, sec_ref_no);
					stmt.setString(8, cont_type);
					stmt.setString(9, agmt_no);
					stmt.setString(10, agmt_rev);
					stmt.setString(11, cont_no);
					stmt.setString(12, cont_rev);
					
					rset = stmt.executeQuery();
			    	
				    if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
						
						logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + ","  + sec_ref_no + "," + map_seq_no + "," + seq_rev_no + "," + "K" + ","  + agmt_no + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + sec_ref_no + "," + map_seq_no + "," + seq_rev_no + "," + "K" + "," + agmt_no + ","), conn, "E");
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
			//conn.commit();
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
		
	}//end of FMS_SECURITY_DEAL_MAP
	
	
	public void FMS_SECURITY_MST() throws IOException, SQLException {

		function_nm="FMS_SECURITY_MST(S)()";
		try {
			
			table_name = "FMS_SECURITY_MST(S)";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String map="";
			
			queryString1 = "INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,"
					+ "VARIATION_VALUE,VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,"
					+ "EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,APRV_DT,APRV_BY,SEQ_REV_NO,GX,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?) ";
			stmt1 = conn.prepareStatement(queryString1);
		
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SECURITY_MST(S)_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECURITY_MST(S)_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_ABBR,COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX, SEC_REF,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
				String seq_rev_no="",bank_cd="",g_cd="";
				abbr = "";seq_no="";
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
			    		map = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if(map!=null) {
			    			map = map.substring(1,map.length()-1);
			    		}
			    		
			    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		} else {
			    			cd ="";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    	}
			    	else if(cell.getColumnIndex() == 2) {	//SEQ_NO
			    		seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if (seq_no != null) {
			    			seq_no = seq_no.substring(1, seq_no.length() - 1);
			    		}
//			    		sec_ref_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//			    		if(sec_ref_no!=null)
//			    		{
//			    			sec_ref_no = sec_ref_no.substring(1, sec_ref_no.length() - 1);
//			    			
//			    			if(map.startsWith("S")) {
//			    			queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? AND CONTRACT_TYPE = ? "
//			    					+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? "
//			    					+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";	
//			    			stmt = conn.prepareStatement(queryString);
//				    		stmt.setString(1, company_cd);
//				    		stmt.setString(2, cd);
//				    		stmt.setString(3, sec_ref_no);
//				    		stmt.setString(4, map.split("-")[0]);
//				    		stmt.setString(5, map.split("-")[1]);
//				    		stmt.setString(6, map.split("-")[2]);
//				    		stmt.setString(7, map.split("-")[3]);
//				    		stmt.setString(8, map.split("-")[4]);
//				    		stmt.setString(9, company_cd);
//				    		stmt.setString(10, cd);
//				    		stmt.setString(11, sec_ref_no);
//				    		rset = stmt.executeQuery();
//				    		if (rset.next() && rset.getString(1)!=null) {
//				    			seq_no = rset.getString(1);
//				    		}else {
//				    			seq_no = "0";
//				    		}
//				    		rset.close();
//				    		stmt.close();
//			    			}
//			    			else if(map.startsWith("L")) {
//			    				queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
//			    						+ "AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
//			    				stmt = conn.prepareStatement(queryString);
//			    				stmt.setString(1, company_cd);
//			    				stmt.setString(2, cd);
//			    				stmt.setString(3, map);
//			    				stmt.setString(4, map.split("-")[0]);
//			    				rset = stmt.executeQuery();
//					    		if (rset.next() && rset.getString(1)!=null) {
//					    			map = rset.getString(1)+"-"+rset.getString(2)+"-"+rset.getString(3)+"-"+rset.getString(4)+"-"+rset.getString(5);
//					    		}
//					    		rset.close();
//					    		stmt.close();
//			    				
//				    			queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? AND CONTRACT_TYPE = ? "
//				    					+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? "
//				    					+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";	
//				    			stmt = conn.prepareStatement(queryString);
//					    		stmt.setString(1, company_cd);
//					    		stmt.setString(2, cd);
//					    		stmt.setString(3, sec_ref_no);
//					    		stmt.setString(4, map.split("-")[0]);
//					    		stmt.setString(5, map.split("-")[1]);
//					    		stmt.setString(6, map.split("-")[2]);
//					    		stmt.setString(7, map.split("-")[3]);
//					    		stmt.setString(8, map.split("-")[4]);
//					    		stmt.setString(9, company_cd);
//					    		stmt.setString(10, cd);
//					    		stmt.setString(11, sec_ref_no);
//					    		rset = stmt.executeQuery();
//					    		if (rset.next() && rset.getString(1)!=null) {
//					    			seq_no = rset.getString(1);
//					    		}else {
//					    			seq_no = "0";
//					    		}
//					    		rset.close();
//					    		stmt.close();
//				    				
//			    			}
//			    			else {
//				    		queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? "
//				    				+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";
//				    		stmt = conn.prepareStatement(queryString);
//				    		stmt.setString(1, company_cd);
//				    		stmt.setString(2, cd);
//				    		stmt.setString(3, sec_ref_no);
//				    		stmt.setString(4, company_cd);
//				    		stmt.setString(5, cd);
//				    		stmt.setString(6, sec_ref_no);
//				    		rset = stmt.executeQuery();
//				    		if (rset.next() && rset.getString(1)!=null) {
//				    			seq_no = rset.getString(1);
//				    		}else {
//				    			seq_no = "0";
//				    		}
//				    		rset.close();
//				    		stmt.close();
//			    			}
//			    		}
			    		data = seq_no;
			    	}
			    	else if (cell.getColumnIndex() == 7) {//GUARANTOR_CD
			    		g_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if(g_cd!=null)
			    		{
			    			g_cd = g_cd.substring(1, g_cd.length() - 1);
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, g_cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			g_cd = rset.getString(1);
				    		} else {
				    			g_cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
			    		}
			    		data=g_cd;
			    		
			    	}
			    	else if (cell.getColumnIndex() == 12  || cell.getColumnIndex() == 14 || cell.getColumnIndex() ==  16) {
			    		bank_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if(bank_cd!=null) {
				    		bank_cd = bank_cd.substring(1, bank_cd.length() - 1);
							queryString2 = "SELECT BANK_CD FROM FMS_BANK_MST WHERE  BANK_NAME = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1,bank_cd );
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								bank_cd=rset2.getString(1);
							}
							else {
								bank_cd=null;
							}
							rset2.close();
							stmt2.close();
						}
			    		data=bank_cd;
			    		
					}
			    	
			    	else {
//			    		if (cell.getColumnIndex() == 2) {	// seq_no
//			    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//			    			if (seq_no != null) {
//			    				seq_no = seq_no.substring(1, seq_no.length() - 1);
//							}
//				    	}
			    		
			    		if (cell.getColumnIndex() == 35) {	// seq_rev_no
			    			seq_rev_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (seq_rev_no != null) {
			    				seq_rev_no = seq_rev_no.substring(1, seq_rev_no.length() - 1);
							}
			    		}
			    		
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}
			    
			    	stmt1.setString(index++, data);
			    
			    }
				queryString = "SELECT COUNTERPARTY_CD FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
						+ "AND SEQ_NO =?  AND  SEQ_REV_NO = ? AND GX = ? ";
				
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.setString(2, cd);
				stmt.setString(3, seq_no);
				stmt.setString(4, seq_rev_no);
				stmt.setString(5, "K");
				rset = stmt.executeQuery();
		    	
			    if (!rset.next() && !cd.equals("") && !seq_no.equals("0")) {
					
					logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + seq_rev_no + "," + "K" + "," + sec_ref_no+ ","), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + seq_rev_no + "," + "K" + ","+ sec_ref_no+ ","), conn, "E");
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

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,", conn);
			
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
		
	}//end of FMS_SECURITY_MST
	
	
	public void LOG_FMS_SECURITY_MST() throws IOException, SQLException {

		function_nm="LOG_FMS_SECURITY_MST(S)()";
		try {
			
			table_name = "LOG_FMS_SECURITY_MST(S)";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			String prev_seq_no="";
			int no=0;
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String map="";
			
			queryString1 = "INSERT INTO LOG_FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,"
					+ "SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,"
					+ "VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,"
					+ "CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,"
					+ "REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,APRV_DT,APRV_BY,SEQ_REV_NO,GX,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,"
					+ "LOG_SEQ_NO,LOG_ENT_DT,PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, "
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,? "
					+ ") ";
			stmt1 = conn.prepareStatement(queryString1);
		
			
			file1 = new File(migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST(S)_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/LOG_FMS_SECURITY_MST(S)_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,ABBR , COUNTERPARTY_CD, SEQ_NO, SEQ_REV_NO, GX, LOG_SEQ_NO,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) {
				String seq_rev_no="",bank_cd="",g_cd="",log_seq_no="";
				abbr = "";seq_no="";
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
			    		map = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
			    		if(map!=null) {
			    			map = map.substring(1,map.length()-1);
			    		}
			    		
			    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
			    		stmt = conn.prepareStatement(queryString);
			    		stmt.setString(1, abbr);
			    		rset = stmt.executeQuery();
			    		if (rset.next()) {
			    			cd = rset.getString(1);
			    		} else {
			    			cd ="";
			    		}
			    		rset.close();
			    		stmt.close();
			    		data = cd;
			    	}
			    	else if(cell.getColumnIndex() == 2) {	//SEQ_NO
			    		sec_ref_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if(sec_ref_no!=null)
			    		{
			    			sec_ref_no = sec_ref_no.substring(1, sec_ref_no.length() - 1);
			    			
			    			if(map.startsWith("S")) {
			    			queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? AND CONTRACT_TYPE = ? "
			    					+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? ";
//			    					+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";	
			    			stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, company_cd);
				    		stmt.setString(2, cd);
				    		stmt.setString(3, sec_ref_no);
				    		stmt.setString(4, map.split("-")[0]);
				    		stmt.setString(5, map.split("-")[1]);
				    		stmt.setString(6, map.split("-")[2]);
				    		stmt.setString(7, map.split("-")[3]);
				    		stmt.setString(8, map.split("-")[4]);
//				    		stmt.setString(9, company_cd);
//				    		stmt.setString(10, cd);
//				    		stmt.setString(11, sec_ref_no);
				    		rset = stmt.executeQuery();
//				    		System.out.println(":"+cd+":"+sec_ref_no+":"+map.split("-")[0]+":"+map.split("-")[1]+":"+map.split("-")[2]+":"+map.split("-")[3]+":"+map.split("-")[4]);
				    		if (rset.next() && rset.getString(1)!=null) {
				    			seq_no = rset.getString(1);
				    		}else {
				    			seq_no = "0";
				    		}
				    		rset.close();
				    		stmt.close();
			    			}
			    			else if(map.startsWith("L")) {
			    				queryString = "SELECT CONTRACT_TYPE, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
			    						+ "AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
			    				stmt = conn.prepareStatement(queryString);
			    				stmt.setString(1, company_cd);
			    				stmt.setString(2, cd);
			    				stmt.setString(3, map);
			    				stmt.setString(4, map.split("-")[0]);
			    				rset = stmt.executeQuery();
					    		if (rset.next() && rset.getString(1)!=null) {
					    			map = rset.getString(1)+"-"+rset.getString(2)+"-"+rset.getString(3)+"-"+rset.getString(4)+"-"+rset.getString(5);
					    		}
					    		rset.close();
					    		stmt.close();
			    				
				    			queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? AND CONTRACT_TYPE = ? "
				    					+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? ";
//				    					+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";	
				    			stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, company_cd);
					    		stmt.setString(2, cd);
					    		stmt.setString(3, sec_ref_no);
					    		stmt.setString(4, map.split("-")[0]);
					    		stmt.setString(5, map.split("-")[1]);
					    		stmt.setString(6, map.split("-")[2]);
					    		stmt.setString(7, map.split("-")[3]);
					    		stmt.setString(8, map.split("-")[4]);
//					    		stmt.setString(9, company_cd);
//					    		stmt.setString(10, cd);
//					    		stmt.setString(11, sec_ref_no);
					    		rset = stmt.executeQuery();
					    		if (rset.next() && rset.getString(1)!=null) {
					    			seq_no = rset.getString(1);
					    		}else {
					    			seq_no = "0";
					    		}
					    		rset.close();
					    		stmt.close();
				    				
			    			}
			    			else {
				    		queryString = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ? ";
//				    				+ "AND SEQ_NO NOT IN (SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEC_REF_NO = ?)";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, company_cd);
				    		stmt.setString(2, cd);
				    		stmt.setString(3, sec_ref_no);
//				    		stmt.setString(4, company_cd);
//				    		stmt.setString(5, cd);
//				    		stmt.setString(6, sec_ref_no);
				    		rset = stmt.executeQuery();
				    		if (rset.next() && rset.getString(1)!=null) {
				    			seq_no = rset.getString(1);
				    		}else {
				    			seq_no = "0";
				    		}
				    		rset.close();
				    		stmt.close();
			    			}
			    		}
			    		data = seq_no;
			    	}
			    	else if (cell.getColumnIndex() == 7) {//GUARANTOR_CD
			    		g_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if(g_cd!=null)
			    		{
			    			g_cd = g_cd.substring(1, g_cd.length() - 1);
				    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, g_cd);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			g_cd = rset.getString(1);
				    		} else {
				    			g_cd ="";
				    		}
				    		rset.close();
				    		stmt.close();
			    		}
			    		data=g_cd;
			    		
			    	}
			    	else if (cell.getColumnIndex() == 12  || cell.getColumnIndex() == 14 || cell.getColumnIndex() ==  16) {
			    		bank_cd = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    		if(bank_cd!=null) {
				    		bank_cd = bank_cd.substring(1, bank_cd.length() - 1);
							queryString2 = "SELECT BANK_CD FROM FMS_BANK_MST WHERE  BANK_NAME = ? ";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1,bank_cd );
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								bank_cd=rset2.getString(1);
							}
							else {
								bank_cd=null;
							}
							rset2.close();
							stmt2.close();
						}
			    		data=bank_cd;
			    		
					}
			    	else if(cell.getColumnIndex() == 43)//LOG_SEQ_NO
					{
						log_seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(log_seq_no != null) {
				    		log_seq_no = log_seq_no.substring(1, log_seq_no.length()-1);
				    	}
				    	data=log_seq_no;
					}
			    	
			    	else {
//			    		if (cell.getColumnIndex() == 2) {	// seq_no
//			    			seq_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
//			    			if (seq_no != null) {
//			    				seq_no = seq_no.substring(1, seq_no.length() - 1);
//							}
//				    	}
			    		
			    		if (cell.getColumnIndex() == 35) {	// seq_rev_no
			    			seq_rev_no = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (seq_rev_no != null) {
			    				seq_rev_no = seq_rev_no.substring(1, seq_rev_no.length() - 1);
							}
			    		}
			    		
				    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    	if(data != null) {
				    		data = data.substring(1, data.length()-1);
				    	}
			    	}
			    
			    	stmt1.setString(index++, data);
			    
			    }
			    //for checking duplicate 
			    queryString = "SELECT SEQ_NO FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = ? "
			    		+ "AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = ? AND LOG_SEQ_NO =?  ";
//			    AND SEC_REF_NO = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.setString(2, cd);
		    	stmt.setString(3, seq_no);
		    	stmt.setString(4, seq_rev_no);
		    	stmt.setString(5, "K");
		    	stmt.setString(6, log_seq_no);
//		    	stmt.setString(7,sec_ref_no );
		    	
				
				rset = stmt.executeQuery();
		    	
			    if (!rset.next() && !cd.equals("") && !seq_no.equals("")) {
					
					logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + seq_rev_no + "," + "K" + ","+log_seq_no+","+sec_ref_no+","), conn, "");
					stmt1.executeUpdate();
					stmt1.close();
					
					logger_count++;
				}
				else {
					stmt1.close();
					skipped_count++;     
					logger.data(fname, (company_cd+","+abbr + "," + cd + "," + seq_no + "," + seq_rev_no + "," + "K" + ","+log_seq_no+","+sec_ref_no+","), conn, "E");
				}
			    
			    rset.close();
			    stmt.close();
           }
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";	
				System.out.println(":"+logger_count);
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
		
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
		
	}//end of LOG_FMS_SECURITY_MST
	
	
	
	
	public void FMS_DAILY_BUYER_NOM() throws IOException, SQLException {
		function_nm="FMS_DAILY_BUYER_NOM()";
		try {
			
			table_name = "FMS_DAILY_BUYER_NOM";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;
			skipped_count = 0;
			total_count = 0;
			
			queryString1 = "INSERT INTO FMS_DAILY_BUYER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
					+ "TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".csv");
			if(file1.exists()) {

				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();

					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ,CONTRACT_TYPE,NOM_REV,TRANS_SEQ,GAS_DT,QTY_MMBTU,CARGO_NO,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						String qty_mmbtu="";
						agmt_no = ""; agmt_rev = ""; seq_no = "" ; nom_rev_no = ""; trans_cd = "";
						abbr = "";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) 
						for (int i = 0; i < line.split(",").length; i++)
					    {
//						cell = cellIterator.next();
							data = null;
//						System.out.println(line.split(",")[i]);
					    	if (i == 0) {	// Counterparty_Abbr, Company Code 
					    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!abbr.equals("NULL")) {
//								abbr = abbr.substring(1, abbr.length() - 1);
//							}
					    		data = company_cd;
					    	}
					    	else if (i == 1) {	// Counterparty_Cd
					    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!cont_ref.equals("NULL")) {
//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
//							}
					    		
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd ="";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							else if (i == 2) { //Agmt_no
					    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (agmt_no != null) {
//								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
//							}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		stmt.setString(3, cont_ref.split("-")[0]);
						    		
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
								else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    		}
						    		rset.close();
						    		stmt.close();
//						    		System.out.println(agmt_no+"="+agmt_rev+"="+cont_no+"="+cont_rev+"="+cont_type);
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (i == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (agmt_rev != null) {
//										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
//									}
					    		}
//					    		
								data = agmt_rev;
					    	}
					    	else if (i == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_no != null) {
//						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
//									}
					    		}
					    		data = cont_no;
					    	}
					    	else if (i == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_rev != null) {
//						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//									}	
					    		}
					    		data = cont_rev;
					    	}

					    	else if (i == 12) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//					    		if (cont_type != null) {
//					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
//								}	
					    		}
					    		data = cont_type;
					    	}
					    	else if (i == 8) {	// trans_cd
					    		trans_cd = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
//				    		if (trans_cd!=null) {
//				    			trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
//							}
					    		
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, trans_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		}else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = trans_cd;
							}
					    	
					    	else if (i == 9) {	// trans_seq
								trans_seq = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
								if(trans_seq != null) {
//								trans_seq = trans_seq.substring(1, trans_seq.length()-1);
									
									queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ?  AND ENTITY = 'R' AND COMPANY_CD = '2' ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, trans_seq);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										trans_seq = rset.getString(1);
									}
									else{
										trans_seq="";
										
									}
									rset.close();
									stmt.close();
						    	}
								data=trans_seq;
							}
					    	
					    	else if(i == 10) { // BU_SEQ
					    		

				    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			
					    		if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
					    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_LTCORA_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq_no = rset.getString(1);
						    		}
						    		else if(!rset.next()){
						    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//						    			if (bu_seq_no!=null) {
//											bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//										}
						    		}
						    		rset.close();
						    		stmt.close();	
					    		}
					    		else if (bu_seq_no == null) {
					    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq_no = rset.getString(1);
						    		}
						    		else if(!rset.next()){
//						    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    			bu_seq_no = "1";
//						    			if (bu_seq_no!=null) {
//											bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//										}
						    		}
						    		rset.close();
						    		stmt.close();	
					    		}
					    		
					    		data  = bu_seq_no;
					    	}
					    	
					    	else if (i == 22) {	// cargo_no
					    		if(cont_type.equals("O") || cont_type.equals("Q")) {
					    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
									
									queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									stmt.setString(2, cargo_ref);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										cargo_no = rset.getString(1);
									}
									else{
										cargo_no="0";
										
									}
									rset.close();
									stmt.close();
					    		}
					    		else {
					    		
									cargo_no="0";
								}
					    		
								data=cargo_no;
								
								//System.out.println(cargo_ref+"=="+cargo_no);
							}
					    	else {				    		
					    		if (i == 6) {	// nom_rev_no
					    			nom_rev_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
					    		}
					    		if (i == 7) {	// plant_seq
					    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (plant_seq_no != null) {
//				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
//								}
					    		}
					    		if (i == 11) {	// gas_dt
					    			gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gas_dt != null) {
//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//								}
					    		}
					    		if (i == 18) {	// qty_mmbtu
					    			qty_mmbtu = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gen_time != null) {
//				    				gen_time = gen_time.substring(1, gen_time.length() - 1);
//								}
					    		}							    		

						    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
					    	}
					    	
//					    	System.out.println(index+"-"+data);
					    	stmt1.setString(index++, data);
					    
					    }
					     
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_DAILY_BUYER_NOM WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? "
								+ "AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CONTRACT_TYPE = ? AND CARGO_NO =? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, nom_rev_no);
						stmt.setString(8, plant_seq_no);
						stmt.setString(9, trans_cd);
						stmt.setString(10, trans_seq);
						stmt.setString(11, bu_seq_no);
						stmt.setString(12, gas_dt);
						stmt.setString(13, cont_type);
						stmt.setString(14, cargo_no);
						rset = stmt.executeQuery();

					    if (!rset.next() && !cd.equals("") && !trans_seq.equals("") && !agmt_no.equals("")){
							
							logger.data(fname, (abbr + "," + cd + "," + agmt_no + "," + agmt_rev + ","+ cont_no + "," + cont_rev + "," + plant_seq_no + "," + cont_type + "," + nom_rev_no + "," + trans_seq + "," + gas_dt + "," + qty_mmbtu + "," + cargo_no + "," ), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + plant_seq_no + "," + cont_type + "," + nom_rev_no + "," + trans_seq + "," + gas_dt + "," + qty_mmbtu + "," + cargo_no + "," ), conn, "E");
						}
					    
					    rset.close();
					    stmt.close();
					}
					br.close();
				}

//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
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
	
	public void FMS_DAILY_BUYER_NOM_DTL() throws IOException, SQLException {

		function_nm="FMS_DAILY_BUYER_NOM_DTL()";
		try {
			
			table_name = "FMS_DAILY_BUYER_NOM_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String qty_mmbtu="";
			
			queryString1 = "INSERT INTO FMS_DAILY_BUYER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
					+ "TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,SEQ_NO,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,CT_REF,UTR_NO,"
					+ "ENT_BY,ENT_DT,CARGO_NO,SF_ID) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_DTL_"+start_end_dt+".csv");
			if(file1.exists()) {


				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_DTL_"+start_end_dt+".csv";
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_DTL_"+start_end_dt+".xlsx"));
					String line = br.readLine();
					
					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,GAS_DT,BU_SEQ,CONTRACT_TYPE,SEQ_NO,QTY_MMBTU,CARGO_NO,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) 
					{
						agmt_no = ""; agmt_rev = ""; plant_seq_no = "" ; nom_rev_no = ""; trans_cd = "";
						abbr = "";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) 
						for (int i = 0; i < line.split(",").length; i++)
					    {
//						cell = cellIterator.next();
							data = null;
							
					    	if (i == 0) {	// Counterparty_Abbr, Company Code 
					    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!abbr.equals("NULL")) {
//								abbr = abbr.substring(1, abbr.length() - 1);
//							}
					    		data = company_cd;
					    	}
					    	else if (i == 1) {	// Counterparty_Cd
					    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!cont_ref.equals("NULL")) {
//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
//							}
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd ="";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							else if (i == 2) { //Agmt_no
					    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (agmt_no != null) {
//								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
//							}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		stmt.setString(3, cont_ref.split("-")[0]);
						    		
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    		}
						    		rset.close();
						    		stmt.close();
//						    		System.out.println(cd + ">>"+cont_ref + "::" + agmt_no + cont_type);
					    		}
								data = agmt_no;
					    	}
					    	else if (i == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (agmt_rev != null) {
//										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
//									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (i == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_no != null) {
//						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
//									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (i == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_rev != null) {
//						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//									}	
					    		}
					    		data = cont_rev;
					    	}
					    	
					    	
					    	else if (i == 12) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_type != null) {
//						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
//									}	
					    		}
					    		data = cont_type;
					    	}
					    	
					    	else if (i == 8) {	// trans_cd
					    		trans_cd = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
//				    		if (trans_cd!=null) {
//								trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
//							}
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, trans_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		}else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = trans_cd;
							}
					    	
					    	else if (i == 9) {	// trans_seq
								trans_seq = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
								if(trans_seq != null) {
//								trans_seq = trans_seq.substring(1, trans_seq.length()-1);
									
									queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ?  AND ENTITY = 'R' AND COMPANY_CD = '2' ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, trans_seq);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										trans_seq = rset.getString(1);
									}
									else{
										trans_seq = "";
									}
									rset.close();
									stmt.close();
						    	}
								data=trans_seq;
							}
					    	
					    	else if(i == 10) { // BU_SEQ
					    		

				    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			
					    		if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
					    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_LTCORA_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq_no = rset.getString(1);
						    		}
						    		else if(!rset.next()){
						    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//						    			if (bu_seq_no!=null) {
//											bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//										}
						    		}
						    		rset.close();
						    		stmt.close();	
					    		}
					    		else if (bu_seq_no == null) {
					    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq_no = rset.getString(1);
						    		}
						    		else if(!rset.next()){
//						    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    			bu_seq_no = "1";
//						    			if (bu_seq_no!=null) {
//											bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//										}
						    		}
						    		rset.close();
						    		stmt.close();	
					    		}
					    		
					    		data  = bu_seq_no;
					    	}
					    	else if (i == 25) {	// cargo_no
					    		if(cont_type.equals("O") || cont_type.equals("Q")) {
					    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
									
									queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									stmt.setString(2, cargo_ref);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										cargo_no = rset.getString(1);
									}
									else{
										cargo_no="0";
										
									}
									rset.close();
									stmt.close();
					    		}
					    		else {
						    		
									cargo_no="0";
								}
								data=cargo_no;
							}
					    	else {				    		
					    		if (i == 6) {	// nom_rev_no
					    			nom_rev_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
					    		}
					    		if (i == 7) {	// plant_seq
					    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (plant_seq_no != null) {
//				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
//								}
					    		}
					    		if (i == 11) {	// gas_dt
					    			gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gas_dt != null) {
//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//								}
					    		}
					    		if (i == 13) {	// seq_no
					    			seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (seq_no != null) {
//				    				seq_no = seq_no.substring(1, seq_no.length() - 1);
//								}
					    		}
					    		if (i == 19) {	// qty_mmbtu
					    			qty_mmbtu = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (seq_no != null) {
//				    				seq_no = seq_no.substring(1, seq_no.length() - 1);
//								}
					    		}
					    									    		

						    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
					    	}
					    	stmt1.setString(index++, data);
					    
					    }
					     
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_DAILY_BUYER_NOM_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? "
								+ "AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND "
								+ "CONTRACT_TYPE = ? AND SEQ_NO = ? AND CARGO_NO = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, nom_rev_no);
						stmt.setString(8, plant_seq_no);
						stmt.setString(9, trans_cd);
						stmt.setString(10, trans_seq);
						stmt.setString(11, bu_seq_no);
						stmt.setString(12, gas_dt);
						stmt.setString(13, cont_type);
						stmt.setString(14, seq_no);
						stmt.setString(15, cargo_no);
						rset = stmt.executeQuery();
						
					    if (!rset.next() && !cd.equals("") && !trans_seq.equals("") && !agmt_no.equals("")){
							logger.data(fname, (abbr + "," + cd + "," + agmt_no + "," + agmt_rev + ","+ cont_no + "," + cont_rev + "," + nom_rev_no + "," + plant_seq_no + "," + trans_cd + ","+ trans_seq + "," + gas_dt + "," + bu_seq_no + "," + cont_type + ","+ seq_no + "," + qty_mmbtu + "," + cargo_no + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + agmt_no + "," + agmt_rev + ","+ cont_no + "," + cont_rev + "," + nom_rev_no + "," + plant_seq_no + "," + trans_cd + ","+ trans_seq + "," + gas_dt + "," + bu_seq_no + "," + cont_type + ","+ seq_no + "," + qty_mmbtu + "," + cargo_no + ","), conn, "E");
						}
					    
					    rset.close();
					    stmt.close();
					}
					br.close();
				}

//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}

				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
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
	
	public void FMS_DAILY_SELLER_NOM() throws IOException, SQLException {

		function_nm="FMS_DAILY_SELLER_NOM()";
		try {
			
			table_name = "FMS_DAILY_SELLER_NOM";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			queryString1 = "INSERT INTO FMS_DAILY_SELLER_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
					+ "TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_SELLER_NOM_"+start_end_dt+".csv");
			if(file1.exists()) {

				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_SELLER_NOM_"+start_end_dt+".csv";
				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_SELLER_NOM_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();
					
					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ,CONTRACT_TYPE,NOM_REV,TRANSPORTER_CD,TRANS_SEQ,GAS_DT,BU_SEQ,CARGO_NO,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						String qty_mmbtu="";
						agmt_no = ""; agmt_rev = ""; seq_no = "" ; nom_rev_no = ""; trans_cd = "";
						abbr = "";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) {
						for (int i = 0; i < line.split(",").length; i++)
					    {
//						cell = cellIterator.next();
							data = null;
							
					    	if (i == 0) {	// Counterparty_Abbr, Company Code 
					    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!abbr.equals("NULL")) {
//								abbr = abbr.substring(1, abbr.length() - 1);
//							}
					    		data = company_cd;
					    	}
					    	else if (i == 1) {	// Counterparty_Cd
					    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!cont_ref.equals("NULL")) {
//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
//							}
					    		
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd ="";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							else if (i == 2) { //Agmt_no
								agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (agmt_no != null) {
//								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
//							}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		stmt.setString(3, cont_ref.split("-")[0]);
						    		
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (i == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
					    			agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (agmt_rev != null) {
//										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
//									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (i == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
					    			cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_no != null) {
//						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
//									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (i == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
					    			cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_rev != null) {
//						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//									}	
					    		}
					    		data = cont_rev;
					    	}
					    	
					    	
					    	else if (i == 12) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
					    			cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_type != null) {
//						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
//									}	
					    		}
					    		data = cont_type;
					    	}
					    	
					    	else if (i == 8) {	// trans_cd
					    		trans_cd = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
//				    		if (trans_cd!=null) {
//				    			trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
//							}
					    		
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, trans_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		}else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = trans_cd;
							}
					    	
					    	else if (i == 9) {	// trans_seq
					    		trans_seq = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
								if(trans_seq != null) {
//								trans_seq = trans_seq.substring(1, trans_seq.length()-1);
									
									queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ?  AND ENTITY = 'R' AND COMPANY_CD = '2' ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, trans_seq);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										trans_seq = rset.getString(1);
									}
									else{
										trans_seq = "";
									}
									rset.close();
									stmt.close();
						    	}
								data=trans_seq;
							}
					    	
					    	else if(i == 10) { // BU_SEQ
					    		

				    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			
					    		if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
					    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_LTCORA_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq_no = rset.getString(1);
						    		}
						    		else if(!rset.next()){
						    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//						    			if (bu_seq_no!=null) {
//											bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//										}
						    		}
						    		rset.close();
						    		stmt.close();	
					    		}
					    		else if (bu_seq_no == null) {
					    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq_no = rset.getString(1);
						    		}
						    		else if(!rset.next()){
//						    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    			bu_seq_no = "1";
//						    			if (bu_seq_no!=null) {
//											bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//										}
						    		}
						    		rset.close();
						    		stmt.close();	
					    		}
					    		
					    		data  = bu_seq_no;
					    	}
					    	else if (i == 22) {	// cargo_no
					    		if(cont_type.equals("O") || cont_type.equals("Q")) {
					    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
									
									queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									stmt.setString(2, cargo_ref);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										cargo_no = rset.getString(1);
									}
									else{
										cargo_no="0";
										
									}
									rset.close();
									stmt.close();
					    		}
					    		else {
						    		
									cargo_no="0";
								}
								data=cargo_no;
							}
					    	
					    	else {				    		
					    		if (i == 6) {	// nom_rev_no
					    			nom_rev_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
					    		}
					    		if (i == 7) {	// plant_seq
					    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (plant_seq_no != null) {
//				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
//								}
					    		}
					    		if (i == 11) {	// gas_dt
					    			gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gas_dt != null) {
//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//								}
					    		}
					    		if (i == 18) {	// qty_mmbtu
					    			qty_mmbtu = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gen_time != null) {
//				    				gen_time = gen_time.substring(1, gen_time.length() - 1);
//								}
					    		}							    		

						    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
					    	}
					    	stmt1.setString(index++, data);
					    
					    }
					     
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_DAILY_SELLER_NOM WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? "
								+ "AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CONTRACT_TYPE = ? AND CARGO_NO = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, nom_rev_no);
						stmt.setString(8, plant_seq_no);
						stmt.setString(9, trans_cd);
						stmt.setString(10, trans_seq);
						stmt.setString(11, bu_seq_no);
						stmt.setString(12, gas_dt);
						stmt.setString(13, cont_type);
						stmt.setString(14, cargo_no);
						rset = stmt.executeQuery();

					    if (!rset.next() && !cd.equals("") && !trans_seq.equals("") && !agmt_no.equals("")){
							
							logger.data(fname, (abbr + "," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + plant_seq_no + "," + cont_type + "," + nom_rev_no + "," + trans_cd + "," + trans_seq + "," + gas_dt + "," + bu_seq_no + "," + qty_mmbtu + "," + cargo_no + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + plant_seq_no + "," + cont_type + "," + nom_rev_no + "," + trans_cd + "," + trans_seq + "," + gas_dt + "," + bu_seq_no + ","+ qty_mmbtu + "," + cargo_no + ","), conn, "E");
						}
					    
					    rset.close();
					    stmt.close();
					}
					br.close();
				}
				
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
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
	
	public void FMS_DAILY_SELLER_NOM_DTL() throws IOException, SQLException {

		function_nm="FMS_DAILY_SELLER_NOM_DTL()";
		try {
			
			table_name = "FMS_DAILY_SELLER_NOM_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			String qty_mmbtu="";
			
			queryString1 = "INSERT INTO FMS_DAILY_SELLER_NOM_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,"
					+ "TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,SEQ_NO,GEN_DT,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_SCM,CT_REF,UTR_NO,"
					+ "ENT_BY,ENT_DT,CARGO_NO,SF_ID) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_SELLER_NOM_DTL_"+start_end_dt+".csv");
			if(file1.exists()) {

				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_SELLER_NOM_DTL_"+start_end_dt+".csv";
				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_SELLER_NOM_DTL_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();
					
					logger.checkpoint(fname, "COUNTERPARTY_ABBR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,PLANT_SEQ,CONTRACT_TYPE,NOM_REV,TRANSPORTER_CD,TRANS_SEQ,GAS_DT,BU_SEQ,QTY_MMBTU,CARGO_NO,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						agmt_no = ""; agmt_rev = ""; plant_seq_no = "" ; nom_rev_no = ""; trans_cd = "";
						abbr = "";
						cd = "0";
						data = null;
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) {
						for (int i = 0; i < line.split(",").length; i++)
					    {
//						cell = cellIterator.next();
							data = null;
							
					    	if (i == 0) {	// Counterparty_Abbr, Company Code 
					    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!abbr.equals("NULL")) {
//								abbr = abbr.substring(1, abbr.length() - 1);
//							}
					    		data = company_cd;
					    	}
					    	else if (i == 1) {	// Counterparty_Cd
					    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!cont_ref.equals("NULL")) {
//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
//							}
					    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, abbr);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			cd = rset.getString(1);
					    		} else {
					    			cd ="";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = cd;
					    	}
							else if (i == 2) { //Agmt_no
					    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (agmt_no != null) {
//								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
//							}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		stmt.setString(3, cont_ref.split("-")[0]);
						    		
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    		}
						    		rset.close();
						    		stmt.close();
//						    		System.out.println(agmt_no+"="+agmt_rev+"="+cont_no+"="+cont_rev+"="+cont_type);
					    		}
					    		
								data = agmt_no;
					    	}
					    	else if (i == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (agmt_rev != null) {
//										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
//									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (i == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_no != null) {
//						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
//									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (i == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_rev != null) {
//						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//									}	
					    		}
					    		data = cont_rev;
					    	}
					    	
					    	else if (i == 12) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_type != null) {
//						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
//									}	
					    		}
					    		data = cont_type;
					    	}
							
					    	else if (i == 8) {	// trans_cd
					    		trans_cd = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (trans_cd!=null) {
//								trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
//							}
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, trans_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		}else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = trans_cd;
							}
					    	
					    	else if (i == 9) {	// trans_seq
								trans_seq = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								if(trans_seq != null) {
//								trans_seq = trans_seq.substring(1, trans_seq.length()-1);
									
									queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ?  AND ENTITY = 'R' AND COMPANY_CD = '2' ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, trans_seq);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										trans_seq = rset.getString(1);
									}
									else{
										trans_seq = "";
									}
									rset.close();
									stmt.close();
						    	}
								data=trans_seq;
							}
					    	
					    	else if(i == 10) { // BU_SEQ
					    		

				    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			
					    		if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
					    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_LTCORA_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq_no = rset.getString(1);
						    		}
						    		else if(!rset.next()){
						    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//						    			if (bu_seq_no!=null) {
//											bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//										}
						    		}
						    		rset.close();
						    		stmt.close();	
					    		}
					    		else if (bu_seq_no == null) {
					    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq_no = rset.getString(1);
						    		}
						    		else if(!rset.next()){
//						    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    			bu_seq_no = "1";
//						    			if (bu_seq_no!=null) {
//											bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//										}
						    		}
						    		rset.close();
						    		stmt.close();	
					    		}
					    		
					    		data  = bu_seq_no;
					    	}
					    	
					    	else if (i == 25) {	// cargo_no
					    		if(cont_type.equals("O") || cont_type.equals("Q")) {
					    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
									
									queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									stmt.setString(2, cargo_ref);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										cargo_no = rset.getString(1);
									}
									else{
										cargo_no="0";
										
									}
									rset.close();
									stmt.close();
					    		}
					    		else {
						    		
									cargo_no="0";
								}
						    	
								data=cargo_no;
							}
					    
					    	else {				    		
					    		if (i == 6) {	// nom_rev_no
					    			nom_rev_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
					    		}
					    		if (i == 7) {	// plant_seq
					    			plant_seq_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    			if (plant_seq_no != null) {
//				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
//								}
					    		}
					    		if (i == 11) {	// gas_dt
					    			gas_dt = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    			if (gas_dt != null) {
//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//								}
					    		}
					    		if (i == 13) {	// seq_no
					    			seq_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    			if (seq_no != null) {
//				    				seq_no = seq_no.substring(1, seq_no.length() - 1);
//								}
					    		}
					    		if (i == 19) {	// qty_mmbtu
					    			qty_mmbtu = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    			if (seq_no != null) {
//				    				seq_no = seq_no.substring(1, seq_no.length() - 1);
//								}
					    		}
					    									    		

						    	data = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
					    	}
					    	stmt1.setString(index++, data);
					    
					    }
					     
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_DAILY_SELLER_NOM_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO=? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? "
								+ "AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND "
								+ "CONTRACT_TYPE = ? AND SEQ_NO = ?";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_rev);
						stmt.setString(5, cont_no);
						stmt.setString(6, cont_rev);
						stmt.setString(7, nom_rev_no);
						stmt.setString(8, plant_seq_no);
						stmt.setString(9, trans_cd);
						stmt.setString(10, trans_seq);
						stmt.setString(11, bu_seq_no);
						stmt.setString(12, gas_dt);
						stmt.setString(13, cont_type);
						stmt.setString(14, seq_no);
						rset = stmt.executeQuery();
						
					    if (!rset.next() && !cd.equals("") && !trans_seq.equals("") && !agmt_no.equals("")){
							logger.data(fname, (abbr + "," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + plant_seq_no + "," + cont_type + "," + nom_rev_no + "," + trans_cd + "," + trans_seq + "," + gas_dt + "," + bu_seq_no + ","+ qty_mmbtu + ","  + cargo_no + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							logger_count++;
						}
						else {
							stmt1.close();
							skipped_count++;     
							logger.data(fname, (abbr + "," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + plant_seq_no + "," + cont_type + "," + nom_rev_no + "," + trans_cd + "," + trans_seq + "," + gas_dt + "," + bu_seq_no + "," + qty_mmbtu + ","  + cargo_no + ","), conn, "E");
						}
					    
					    rset.close();
					    stmt.close();
					}
					br.close();
				}
				
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";				
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			//conn.commit();
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
	
	
	public void FMS_METER_TICKET_READING() throws IOException, SQLException {
		
		function_nm="FMS_METER_TICKET_READING()";
		try {
			
			table_name = "FMS_METER_TICKET_READING";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			// SagarB20251107 Added below block of code for checking if migrated data already exists or not
			int exist_data = 0;
			String scm = "0";
			queryString = "SELECT COUNT(COMPANY_CD) FROM FMS_METER_TICKET_READING WHERE COMPANY_CD = ? AND GAS_DT < TO_DATE('01/01/2025', 'DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, company_cd);
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				exist_data = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			queryString1 = "INSERT INTO FMS_METER_TICKET_READING(COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,"
					+ "METER_SEQ,GAS_DT,GEN_DT,GEN_TIME,QTY_MMBTU,QTY_SCM,QTY_BTU,RECONCIL_QTY_MMBTU,RECONCIL_QTY_SCM,"
					+ "RECONCIL_QTY_BTU,TOTAL_QTY_MMBTU,CALC_GCV,CALC_NCV,DEFINE_GCV,DEFINE_NCV,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,TOTAL_QTY_SCM) "
					+ "VALUES(?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_METER_TICKET_READING_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_METER_TICKET_READING_"+start_end_dt+".csv";
				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_METER_TICKET_READING_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
									String line = br.readLine();
									
									logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, PLANT_SEQ, METER_TYPE, METER_SEQ, GAS_DT,TIMESTAMP,", conn);
									while ((line = br.readLine()) != null && exist_data == 0) {
										total_count++; 
										abbr = "";seq_no="";
										String meter_type="";
										cd = "0";
										
										index = 1;
										stmt1 = conn.prepareStatement(queryString1);
										
					//					row = rowIterator.next();
					//				    cellIterator = row.cellIterator();
									    
										for (int i = 0; i < line.split(",").length; i++) 
									    {
					//				    	cell = cellIterator.next();
											data = null;
									    	if (i == 0) {
									    		
									    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!abbr.equals("NULL")) {
					//								abbr = abbr.substring(1, abbr.length() - 1);
					//							}
									    		data = company_cd;
									    	}
									    	else if (i == 1) {	// Counterparty_Cd
									    		
									    		seq_no = line.split(",")[i].contains("'null'") ? null : line.split(",")[i];
					    		
									    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, abbr);
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			cd = rset.getString(1);
									    		}else {
									    			cd = "";
									    		}
									    		rset.close();
									    		stmt.close();
									    		data = cd;
									    	}
									    	else if(i == 2 )//plant_seq_no
									    	{	
									    		 data= line.split(",")[i].contains("'null'") ? null : line.split(",")[i];
													/*if (data!=null && data.length() > 20) {
														  data = data.substring(0, data.length() - 5);
													}*/
									    		queryString = "SELECT METER_SEQ,PLANT_SEQ,METER_TYPE FROM FMS_METER_MST WHERE METER_REF LIKE ? AND COMPANY_CD = '2'";
									    		stmt = conn.prepareStatement(queryString);
									    		if(data.contains("PIL Gadi")) 
									    		{
									    			stmt.setString(1,"PIL Gadi%");
									    		}
									    		// SagarB20251107 Added below block to handle BHD-1(M3) condition 
									    		else if(data.contains("BHD-1")) 
									    		{
									    			stmt.setString(1,"BHD-1%");
									    		}
									    		// SagarB20251121 Added below block to handle PILHAZSEI(M9) condition 
									    		else if(data.contains("PILHAZSEI")) 
									    		{
									    			stmt.setString(1,"PILHAZSEI%");
									    		}
									    		// SagarB20251121 Added below block to handle PIL Oduru condition 
									    		else if(data.contains("null(M7)")) 
									    		{
									    			stmt.setString(1,"PIL Oduru%");
									    		}
									    		else if(data.contains("PILMSK(M1)") || data.contains("Delivery point(M3)")) 
									    		{
									    			data = "PIL MSK";
									    			stmt.setString(1, data);
									    		}
									    		else 
									    		{
													if (data.length() > 20) {
														stmt.setString(1,data.substring(0, 20)+"%");
//														System.out.println(data.substring(0, 20));
													}
													else {
														stmt.setString(1,data+"%");
													}
									    		}
									    		rset = stmt.executeQuery();
									    		if (rset.next() && data.contains("(M")) {		// SagarB20251107 Added data.contains condition
									    			seq_no = rset.getString(1);
									    			plant_seq_no = rset.getString(2);
									    			meter_type=rset.getString(3);
									    			
									    			// SagarB20251107 Added below block of code to handle same meter ref condition between Pipeinfra Plant-3 and Plant-8
									    			if (data.contains("BHD-1")) {
									    				queryString2 = "SELECT METER_SEQ,PLANT_SEQ,METER_TYPE FROM FMS_METER_MST WHERE COMPANY_CD = ? AND PLANT_SEQ = (SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY = 'R' AND PLANT_ABBR = 'PIL BHAD') AND COUNTERPARTY_CD = (SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? AND ENTITY = 'R' AND PLANT_ABBR = 'PIL BHAD')";
									    				stmt2 = conn.prepareStatement(queryString2);
									    				stmt2.setString(1, company_cd);
									    				stmt2.setString(2, company_cd);
									    				stmt2.setString(3, company_cd);
									    				rset2 = stmt2.executeQuery();
									    				if (rset2.next()) {
											    			seq_no = rset2.getString(1);
											    			plant_seq_no = rset2.getString(2);
											    			meter_type=rset2.getString(3);
									    				}
									    				rset2.close();
									    				stmt2.close();
									    				
									    			}
									    			
									    		}
									    		// else if(!rset.next()){	SagarB20251107 Commented this line with respect to above chnage in condition
									    		else {
									    			queryString2 = "SELECT A.METER_SEQ,A.PLANT_SEQ,A.METER_TYPE FROM FMS_METER_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
									    					+ "WHERE B.COMPANY_CD  = '2' AND B.PLANT_ABBR = ? AND B.ENTITY = 'R' AND B.COUNTERPARTY_CD = ? AND A.PLANT_SEQ = B.SEQ_NO"
									    					+ " AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD ORDER BY A.METER_SEQ";
										    		stmt2 = conn.prepareStatement(queryString2);
										    		stmt2.setString(1,data);
										    		stmt2.setString(2,cd);
										    		rset2 = stmt2.executeQuery();
										    		if (rset2.next()) {
										    			seq_no = rset2.getString(1);
										    			plant_seq_no = rset2.getString(2);
										    			meter_type="R";
										    		}else if(!rset2.next()){
										    			queryString3 = "SELECT A.SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL A, FMS_COUNTERPARTY_MST B WHERE COMPANY_CD  = '2' AND B.COUNTERPARTY_ABBR = ? AND A.ENTITY = 'R' "
										    					+ "AND A.COUNTERPARTY_CD = ? AND A.SEQ_NO = ? AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD ";
											    		stmt3 = conn.prepareStatement(queryString3);
											    		stmt3.setString(1, data);
											    		stmt3.setString(2,cd);
											    		stmt3.setString(3,"1");
											    		rset3 = stmt3.executeQuery();
											    		if (rset3.next()) {
											    			seq_no = rset3.getString(1);
											    			plant_seq_no = rset3.getString(1);
											    			meter_type="R";
											    		}else {
											    			seq_no = null;
											    			plant_seq_no = null;
											    			meter_type="R";
											    		}
											    		rset3.close();
											    		stmt3.close();
							
										    		}
										    		rset2.close();
										    		stmt2.close();
									    		}
									    		rset.close();
									    		stmt.close();
									    		data = plant_seq_no;
									    	}
									    	else if(i == 3 )//meter type
									    	{	
									    		data = meter_type;
									    	}
									    	else if(i == 4 )//meter seq_no
									    	{	
									    		data = seq_no;
									    	}
									    	/// SagarB20251107 Added below block of code for summation of MMBTU and SCM
									    	else if (i == 8) {

										    	data = line.split(",")[i].contains("null") ? "0" : line.split(",")[i];
										    	scm = "0";
											    queryString = "SELECT QTY_MMBTU, QTY_SCM FROM FMS_METER_TICKET_READING WHERE COMPANY_CD =? AND "
											    		+ " COUNTERPARTY_CD = ? AND PLANT_SEQ = ? AND METER_TYPE = ? AND METER_SEQ = ? AND "
											    		+ " GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss')";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, company_cd);
										    	stmt.setString(2, cd);
										    	stmt.setString(3,plant_seq_no);
										    	stmt.setString(4, meter_type);
										    	stmt.setString(5, seq_no);
										    	stmt.setString(6, gas_dt);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		data = (Double.parseDouble(data)+rset.getDouble(1))+"";
										    		scm = rset.getString(2);
										    	}
										    	rset.close();
										    	stmt.close();
										    	
									    	}
									    	else if (i == 9) {
										    	data = line.split(",")[i].contains("null") ? "0" : line.split(",")[i];
										    	data = (Double.parseDouble(scm) + Double.parseDouble(data)) + "";
									    	}
									    	
									    	else {
									    		if (i == 3) {	// meter_type
									    			meter_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    			if (meter_type != null) {
					//				    				meter_type = meter_type.substring(1, meter_type.length() - 1);
					//								}
									    		}
									    		if (i == 5) {	// GAS_DT
									    			gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    			if (gas_dt != null) {
					//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
					//								}
									    		}
									    		
										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
									    	}	
									    	
									    	//System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);		    	
									    }
									    
										//checking for duplicate data
									    queryString = "SELECT COUNTERPARTY_CD FROM FMS_METER_TICKET_READING WHERE COMPANY_CD =? AND "
									    		+ " COUNTERPARTY_CD = ? AND PLANT_SEQ = ? AND METER_TYPE = ? AND METER_SEQ = ? AND "
									    		+ " GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss')";
								    	stmt = conn.prepareStatement(queryString);
								    	stmt.setString(1, company_cd);
								    	stmt.setString(2, cd);
								    	stmt.setString(3,plant_seq_no);
								    	stmt.setString(4, meter_type);
								    	stmt.setString(5, seq_no);
								    	stmt.setString(6, gas_dt);
								    	
								    	rset = stmt.executeQuery();
								    	
								    	 
								    	 if (rset.next() && !cd.equals("") && plant_seq_no!=null  && seq_no!=null) {
								    		 queryString2 = "DELETE FROM FMS_METER_TICKET_READING WHERE COMPANY_CD =? AND "
								    		 		+ " COUNTERPARTY_CD = ? AND PLANT_SEQ = ? AND METER_TYPE = ? AND METER_SEQ = ? AND "
								    		 		+ " GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss')";
								    		 stmt2 = conn.prepareStatement(queryString2);
										    	stmt2.setString(1, company_cd);
										    	stmt2.setString(2, cd);
										    	stmt2.setString(3,plant_seq_no);
										    	stmt2.setString(4, meter_type);
										    	stmt2.setString(5, seq_no);
										    	stmt2.setString(6, gas_dt);
										    	stmt2.executeUpdate();
										    	stmt2.close();
										    	
											logger.data(fname, (company_cd+"(Summed Data)," + cd + " , " + plant_seq_no + "," + meter_type + "," + seq_no + "," + gas_dt + "," ), conn, "");
											
											stmt1.executeUpdate();
											stmt1.close();
											
											logger_count++;
								    	 }
								    	 else if (!rset.next() && !cd.equals("") && plant_seq_no!=null  && seq_no!=null) {
												logger.data(fname, (company_cd+"," + cd + " , " + plant_seq_no + "," + meter_type + "," + seq_no + "," + gas_dt + "," ), conn, "");
												
												stmt1.executeUpdate();
												stmt1.close();
												
												logger_count++;
											}
								    	 else {
											stmt1.close();
											skipped_count++;     
											logger.data(fname, (company_cd+"," + cd + " , " + plant_seq_no + "," + meter_type + "," + seq_no + "," + gas_dt + "," ), conn, "E");
										}
							    	 rset.close();
									 stmt.close();
									}
									
									br.close();
				}
				
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
			
//				rowIterator = sheet.iterator();
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";		
		
			}
			
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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
	}//END of meter ticket reading
	
//public void FMS_METER_TICKET_READING_TEMP() throws IOException, SQLException {
//		
//		function_nm="FMS_METER_TICKET_READING_TEMP()";
//		try {
//			
//			table_name = "FMS_METER_TICKET_READING_TEMP";
//			System.out.println("<<START>><<"+table_name+">>");
//			
//			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
//			
//			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
//			
//			data = "";
//			logger_count = 0;   
//			skipped_count = 0;   
//			total_count = 0; 
//			
//			queryString1 = "INSERT INTO FMS_METER_TICKET_READING_TEMP(COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,"
//					+ "METER_SEQ,GAS_DT,GEN_DT,GEN_TIME,QTY_MMBTU,QTY_SCM,QTY_BTU,RECONCIL_QTY_MMBTU,RECONCIL_QTY_SCM,"
//					+ "RECONCIL_QTY_BTU,TOTAL_QTY_MMBTU,CALC_GCV,CALC_NCV,DEFINE_GCV,DEFINE_NCV,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,TOTAL_QTY_SCM) "
//					+ "VALUES(?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,"
//					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?)";
//			stmt1 = conn.prepareStatement(queryString1);
//			
//			file1 = new File(migration_setup_dir + "EXPORT/FMS_METER_TICKET_READING_TEMP_"+start_end_dt+".csv");
//			
//			if(file1.exists()) {
//				
//				String fileName = migration_setup_dir + "EXPORT/FMS_METER_TICKET_READING_TEMP_"+start_end_dt+".csv";
//				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_METER_TICKET_READING_TEMP_"+start_end_dt+".xlsx"));
//				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//					//				if (rowIterator.hasNext()) {	// For skipping the first row
//					//					rowIterator.next();
//					//				}
//									String line = br.readLine();
//									
//									logger.checkpoint(fname, "COMPANY_CD, COUNTERPARTY_CD, PLANT_SEQ, METER_TYPE, METER_SEQ, GAS_DT,TIMESTAMP,", conn);
//									while ((line = br.readLine()) != null) {
//										total_count++; 
//										abbr = "";seq_no="";
//										String meter_type="";
//										cd = "0";
//										
//										index = 1;
//										stmt1 = conn.prepareStatement(queryString1);
//										
//					//					row = rowIterator.next();
//					//				    cellIterator = row.cellIterator();
//									    
//										for (int i = 0; i < line.split(",").length; i++) 
//									    {
//					//				    	cell = cellIterator.next();
//											data = null;
//									    	if (i == 0) {
//									    		
//									    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//					//				    		if (!abbr.equals("NULL")) {
//					//								abbr = abbr.substring(1, abbr.length() - 1);
//					//							}
//									    		data = company_cd;
//									    	}
//									    	else if (i == 1) {	// Counterparty_Cd
//									    		
//									    		seq_no = line.split(",")[i].contains("'null'") ? null : line.split(",")[i];
//					    		
//									    		queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
//									    		stmt = conn.prepareStatement(queryString);
//									    		stmt.setString(1, abbr);
//									    		rset = stmt.executeQuery();
//									    		if (rset.next()) {
//									    			cd = rset.getString(1);
//									    		}else {
//									    			cd = "";
//									    		}
//									    		rset.close();
//									    		stmt.close();
//									    		data = cd;
//									    	}
//									    	else if(i == 2 )//plant_seq_no
//									    	{	
//									    		 data= line.split(",")[i].contains("'null'") ? null : line.split(",")[i];
//												if (data!=null && data.length() > 20) {
//									    			  data = data.substring(0, data.length() - 5);
//									    		}
//									    		queryString = "SELECT METER_SEQ,PLANT_SEQ,METER_TYPE FROM FMS_METER_MST WHERE METER_REF LIKE ? AND COMPANY_CD = '2'";
//									    		stmt = conn.prepareStatement(queryString);
//									    		stmt.setString(1,data+"%");
//									    		rset = stmt.executeQuery();
//									    		if (rset.next()) {
//									    			seq_no = rset.getString(1);
//									    			plant_seq_no = rset.getString(2);
//									    			meter_type=rset.getString(3);
//									    		}
//									    		else if(!rset.next()){
//									    			queryString2 = "SELECT A.METER_SEQ,A.PLANT_SEQ,A.METER_TYPE FROM FMS_METER_MST A, FMS_COUNTERPARTY_PLANT_DTL B "
//									    					+ "WHERE B.COMPANY_CD  = '2' AND B.PLANT_ABBR = ? AND B.ENTITY = 'R' AND B.COUNTERPARTY_CD = ? AND A.PLANT_SEQ = B.SEQ_NO"
//									    					+ " AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD";
//										    		stmt2 = conn.prepareStatement(queryString2);
//										    		stmt2.setString(1,data);
//										    		stmt2.setString(2,cd);
//										    		rset2 = stmt2.executeQuery();
//										    		if (rset2.next()) {
//										    			seq_no = rset2.getString(1);
//										    			plant_seq_no = rset2.getString(2);
//										    			meter_type="R";
//										    		}else if(!rset2.next()){
//										    			queryString3 = "SELECT A.SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL A, FMS_COUNTERPARTY_MST B WHERE COMPANY_CD  = '2' AND B.COUNTERPARTY_ABBR = ? AND A.ENTITY = 'R' "
//										    					+ "AND A.COUNTERPARTY_CD = ? AND A.SEQ_NO = ? AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD ";
//											    		stmt3 = conn.prepareStatement(queryString3);
//											    		stmt3.setString(1, data);
//											    		stmt3.setString(2,cd);
//											    		stmt3.setString(3,seq_no);
//											    		rset3 = stmt3.executeQuery();
//											    		if (rset3.next()) {
//											    			seq_no = rset3.getString(1);
//											    			plant_seq_no = rset3.getString(1);
//											    			meter_type="R";
//											    		}else {
//											    			seq_no = null;
//											    			plant_seq_no = null;
//											    			meter_type="R";
//											    		}
//											    		rset3.close();
//											    		stmt3.close();
//							
//										    		}
//										    		rset2.close();
//										    		stmt2.close();
//									    		}
//									    		rset.close();
//									    		stmt.close();
//									    		data = plant_seq_no;
//									    	}
//									    	else if(i == 3 )//meter type
//									    	{	
//									    		data = meter_type;
//									    	}
//									    	else if(i == 4 )//meter seq_no
//									    	{	
//									    		data = seq_no;
//									    	}
//									    	else {
//									    		if (i == 3) {	// meter_type
//									    			meter_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//				    			if (meter_type != null) {
//					//				    				meter_type = meter_type.substring(1, meter_type.length() - 1);
//					//								}
//									    		}
//									    		if (i == 5) {	// GAS_DT
//									    			gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//				    			if (gas_dt != null) {
//					//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//					//								}
//									    		}
//									    		
//										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//					    	if(data != null) {
//					//					    		data = data.substring(1, data.length()-1);
//					//					    	}
//									    	}	
//									    	
//									    	//System.out.println(index+"-"+data);
//									    	stmt1.setString(index++, data);		    	
//									    }
//									    
//										//checking for duplicate data
//									    queryString = "SELECT COUNTERPARTY_CD FROM FMS_METER_TICKET_READING_TEMP WHERE COMPANY_CD =? AND "
//									    		+ " COUNTERPARTY_CD = ? AND PLANT_SEQ = ? AND METER_TYPE = ? AND METER_SEQ = ? AND "
//									    		+ " GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss')";
//								    	stmt = conn.prepareStatement(queryString);
//								    	stmt.setString(1, company_cd);
//								    	stmt.setString(2, cd);
//								    	stmt.setString(3,plant_seq_no);
//								    	stmt.setString(4, meter_type);
//								    	stmt.setString(5, seq_no);
//								    	stmt.setString(6, gas_dt);
//								    	
//								    	rset = stmt.executeQuery();
//								    	
//								    	 if (!rset.next() && !cd.equals("") && plant_seq_no!=null  && seq_no!=null) {
//											logger.data(fname, (company_cd+"," + cd + " , " + plant_seq_no + "," + meter_type + "," + seq_no + "," + gas_dt + "," ), conn, "");
//											
//											stmt1.executeUpdate();
//											stmt1.close();
//											
//											logger_count++;
//										}
//								    	 else {
//											stmt1.close();
//											skipped_count++;     
//											logger.data(fname, (company_cd+"," + cd + " , " + plant_seq_no + "," + meter_type + "," + seq_no + "," + gas_dt + "," ), conn, "E");
//										}
//							    	 rset.close();
//									 stmt.close();
//									}
//									
//									br.close();
//				}
//				
////				workbook = new XSSFWorkbook(file);
////				sheet = workbook.getSheetAt(0);
//			
////				rowIterator = sheet.iterator();
//				
//				msg = "Data has been Inserted Successfully in Database.";
//				msg_type = "S";		
//		
//			}
//			
//			else {
//				msg = "Excel File not found while Execution. Program Terminated.";
//				msg_type = "E";
//			}
//			
//			System.out.println("<<END>><<"+table_name+">>");
//			System.out.println();
//
//			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 
//
//			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
//			
//			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
//			
//			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
//			
//			logger.checkpoint1(fname1,logger_count+",", conn);
//			
//		} 
//		catch (Exception e)	{			
//			msg = "One of the Functions faced an Error. Data Not Inserted.";
//			msg_type = "E";
//			
//			conn.rollback();
//			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//			logger.error(fname, e, function_nm, conn, fname_error);			
//		}
//		
//		finally {
//			conn.commit();
//			if (file != null) {
//				file.close();
//			}
//		}
//	}//END of meter ticket reading
//
	public void FMS_DAILY_ALLOCATION_DTL() throws IOException, SQLException {
		
		function_nm="FMS_DAILY_ALLOCATION_DTL()";
		try {
			
			table_name = "FMS_DAILY_ALLOCATION_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			queryString1 = "INSERT INTO FMS_DAILY_ALLOCATION_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,"
					+ "CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,GEN_DT,GEN_TIME,BASE,"
					+ "GCV,NCV,QTY_MMBTU,QTY_SCM,ENT_BY,ENT_DT,CARGO_NO) VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_ALLOCATION_DTL_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_ALLOCATION_DTL_"+start_end_dt+".csv";
				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_ALLOCATION_DTL_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
//				
//				if (rowIterator.hasNext()) {	// For skipping the first row
//					rowIterator.next();
//				}
					String line = br.readLine();
					
					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV_NO,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,QTY_MMBTU,CARGO_NO,TIMESTAMP,", conn);
					
					while ((line = br.readLine()) != null) {
						total_count++; 
						agmt_no = ""; agmt_rev = ""; seq_no = "";
						abbr = "";
						cd = "0";
						data = null;
						String qty_mmbtu ="";
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
//					row = rowIterator.next();
//				    cellIterator = row.cellIterator();
//				    while (cellIterator.hasNext()) {
						for (int i = 0; i < line.split(",").length; i++)
					    {	
//				    	cell = cellIterator.next();
							data = null;
							
					    	if (i == 0) {
					    		
					    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!abbr.equals("NULL")) {
//								abbr = abbr.substring(1, abbr.length() - 1);
//							}
					    		data = company_cd;
					    	}
					    	else if (i == 1) {	// Counterparty_Cd
					    		
					    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//				    		if (!cont_ref.equals("NULL")) {
//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
//							}
					    		
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
							else if (i == 2) { //Agmt_no
					    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//				    		if (agmt_no != null) {
//								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
//							}
					    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		stmt.setString(3, cont_ref.split("-")[0]);
						    		
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
					    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
					    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, cont_ref);
						    		
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    		}
						    		rset.close();
						    		stmt.close();
					    		}
								data = agmt_no;
					    	}
					    	else if (i == 3) { //Agmt_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (agmt_rev != null) {
//										agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
//									}
					    		}
								data = agmt_rev;
					    	}
					    	else if (i == 4) { //Cont_no
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_no != null) {
//						    			cont_no = cont_no.substring(1, cont_no.length() - 1);
//									}
					    		}
					    		data = cont_no;
					    	}
				    		
					    	else if (i == 5) { //Cont_rev
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_rev != null) {
//						    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
//									}	
					    		}
					    		data = cont_rev;
					    	}
					    	else if (i == 12) { //contract_type
					    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
						    		cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
//						    		if (cont_type != null) {
//						    			cont_type = cont_type.substring(1, cont_type.length() - 1);
//									}	
					    		}
					    		data = cont_type;
					    	}
					    	
					    	else if (i == 8) {	// trans_cd
					    		trans_cd = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
//				    		if (trans_cd!=null) {
//								trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
//							}
								queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
					    		stmt = conn.prepareStatement(queryString);
					    		stmt.setString(1, trans_cd);
					    		rset = stmt.executeQuery();
					    		if (rset.next()) {
					    			trans_cd = rset.getString(1);
					    		}else {
					    			trans_cd = "";
					    		}
					    		rset.close();
					    		stmt.close();
					    		data = trans_cd;
							}
					    	
					    	else if (i == 9) {	// trans_seq
								trans_seq = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
								if(trans_seq != null) {
//								trans_seq = trans_seq.substring(1, trans_seq.length()-1);
									
									queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ?  AND ENTITY = 'R' AND COMPANY_CD = '2' ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, trans_seq);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										trans_seq = rset.getString(1);
									}
									else{
										trans_seq = "";
									}
									rset.close();
									stmt.close();
						    	}
								data=trans_seq;
							}
					    	
					    	else if(i == 10) { // BU_SEQ
					    		

				    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				    			
				    			if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
					    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_LTCORA_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq_no = rset.getString(1);
						    		}
						    		else if(!rset.next()){
						    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//						    			if (bu_seq_no!=null) {
//											bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//										}
						    		}
						    		rset.close();
						    		stmt.close();	
					    		}
					    		else if (bu_seq_no == null) {
					    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
						    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cd);
						    		stmt.setString(2, agmt_no);
						    		stmt.setString(3, agmt_rev);
						    		stmt.setString(4, cont_no);
						    		stmt.setString(5, cont_rev);
						    		stmt.setString(6, cont_type);
						    		rset = stmt.executeQuery();
						    		
						    		if (rset.next()) {
						    			bu_seq_no = rset.getString(1);
						    		}
						    		else if(!rset.next()){
//						    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    			bu_seq_no = "1";
//						    			if (bu_seq_no!=null) {
//											bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//										}
						    		}
						    		rset.close();
						    		stmt.close();	
					    		}
					    		
					    		data  = bu_seq_no;
					    	}
					    	else if (i == 22) {	// cargo_no
					    		if(cont_type.equals("O") || cont_type.equals("Q")) {
					    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
									
									queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, cd);
									stmt.setString(2, cargo_ref);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										cargo_no = rset.getString(1);
									}
									else{
										cargo_no="0";
										
									}
									rset.close();
									stmt.close();
					    		}
					    		else {
						    		
									cargo_no="0";
								}
						    	
								data=cargo_no;
							}
					    	else {
					    		if (i == 6) {	// nom_rev
					    			nom_rev_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (nom_rev_no != null) {
//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
//								}
					    		}
					    				
					    		if (i == 7) {	// plant_seq
					    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (plant_seq_no != null) {
//				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
//								}
					    		}
//					    		if (i == 9) {	// trans_seq
//					    			trans_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
////				    			if (trans_seq != null) {
////				    				trans_seq = trans_seq.substring(1, trans_seq.length() - 1);
////								}
//					    		}				    		
					    		if (i == 11) {	// gas_dt
					    			gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gas_dt != null) {
//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//								}
					    		}
					    		if (i == 18) {	// qty_mmbtu
					    			qty_mmbtu = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    			if (gas_dt != null) {
//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
//								}
					    		}
					    		
						    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					    	if(data != null) {
//					    		data = data.substring(1, data.length()-1);
//					    	}
					    	}	
					    	//System.out.println(index+"-"+data);
					    	stmt1.setString(index++, data);		    	
					    }
						
						queryString = "SELECT COUNTERPARTY_CD FROM FMS_DAILY_ALLOCATION_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
					    		+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? "
					    		+ "AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') "
					    		+ "AND CONTRACT_TYPE = ? AND CARGO_NO = ? ";
				    	stmt = conn.prepareStatement(queryString);
				    	stmt.setString(1, company_cd);
				    	stmt.setString(2, cd);
				    	stmt.setString(3, agmt_no);
				    	stmt.setString(4, agmt_rev);
				    	stmt.setString(5, cont_no);
				    	stmt.setString(6, cont_rev);
				    	stmt.setString(7, nom_rev_no);
				    	stmt.setString(8, plant_seq_no);
				    	stmt.setString(9, trans_cd);
				    	stmt.setString(10, trans_seq);
				    	stmt.setString(11, bu_seq_no);
				    	stmt.setString(12, gas_dt);
				    	stmt.setString(13, cont_type);
				    	stmt.setString(14, cargo_no);
					
						rset = stmt.executeQuery();
						
						 if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
								//System.out.println(queryString1);
								
								logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + ","+ nom_rev_no + " , " + plant_seq_no + "," + trans_cd + "," + trans_seq + "," + bu_seq_no + "," + gas_dt + "," + cont_type + "," + qty_mmbtu + "," + cargo_no + "," ), conn, "");
								
								stmt1.executeUpdate();
								stmt1.close();
								
								logger_count++;
						}
						 else {
								stmt1.close();
								skipped_count++;     
								logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + ","+ nom_rev_no + " , " + plant_seq_no + "," + trans_cd + "," + trans_seq + "," + bu_seq_no + "," + gas_dt + "," + cont_type + "," + qty_mmbtu + "," + cargo_no + "," ), conn, "E");
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
			
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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
	
	
	public void FMS_DAILY_ALLOCATION_DTL_CT() throws IOException, SQLException {
		
		function_nm="FMS_DAILY_ALLOCATION_DTL_CT()";
		try {
			
			table_name = "FMS_DAILY_ALLOCATION_DTL_CT";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			queryString1 = "INSERT INTO FMS_DAILY_ALLOCATION_DTL_CT(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,"
					+ "CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,SEQ_NO,GEN_DT,GEN_TIME,BASE,"
					+ "GCV,NCV,QTY_MMBTU,QTY_SCM,CT_REF,UTR_NO,ENT_BY,ENT_DT,CARGO_NO,DTL_CATEGORY,MOLECULE_MAP) VALUES(?,?,?,?,?,?,?,?,?,"
					+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_DAILY_ALLOCATION_DTL_CT_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_DAILY_ALLOCATION_DTL_CT_"+start_end_dt+".csv";
				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_ALLOCATION_DTL_CT_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
									String line = br.readLine();
									
									logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV_NO,CONT_NO,CONT_REV,NOM_REV_NO,PLANT_SEQ,TRANSPORTER_CD,TRANS_SEQ,BU_SEQ,GAS_DT,CONTRACT_TYPE,QTY_MMBTU,CARGO_NO,TIMESTAMP,", conn);
									
									while ((line = br.readLine()) != null) {
										total_count++; 
										agmt_no = ""; agmt_rev = ""; seq_no = "";
										abbr = "";
										cd = "0";
										data = null;
										String qty_mmbtu="";
										index = 1;
										stmt1 = conn.prepareStatement(queryString1);
										
					//					row = rowIterator.next();
					//				    cellIterator = row.cellIterator();
					//				    while (cellIterator.hasNext()) {
										for (int i = 0; i < line.split(",").length; i++)
									    {	
					//				    	cell = cellIterator.next();
											data = null;
											
									    	if (i == 0) {
									    		
									    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!abbr.equals("NULL")) {
					//								abbr = abbr.substring(1, abbr.length() - 1);
					//							}
									    		data = company_cd;
									    	}
									    	else if (i == 1) {	// Counterparty_Cd
									    		
									    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!cont_ref.equals("NULL")) {
					//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
					//							}
									    		
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
											else if (i == 2) { //Agmt_no
									    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//				    		if (agmt_no != null) {
					//								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
					//							}
									    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, cont_ref);
										    		stmt.setString(3, cont_ref.split("-")[0]);
										    		
										    		
										    		rset = stmt.executeQuery();
										    		if (rset.next()) {
										    			agmt_no = rset.getString(1);
										    			agmt_rev = rset.getString(2);
										    			cont_no = rset.getString(3);
										    			cont_rev = rset.getString(4);
										    			cont_type = rset.getString(5);
										    		} else {
										    			agmt_no = "";
										    			agmt_rev = "";
										    			cont_no = "";
										    			cont_rev = "";
										    			cont_type = "";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, cont_ref);
										    		
										    		rset = stmt.executeQuery();
										    		if (rset.next()) {
										    			agmt_no = rset.getString(1);
										    			agmt_rev = rset.getString(2);
										    			cont_no = rset.getString(3);
										    			cont_rev = rset.getString(4);
										    			cont_type = rset.getString(5);
										    		} else {
										    			agmt_no = "";
										    			agmt_rev = "";
										    			cont_no = "";
										    			cont_rev = "";
										    			cont_type = "";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		
												data = agmt_no;
									    	}
									    	else if (i == 3) { //Agmt_rev
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
										    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (agmt_rev != null) {
					//									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
					//								}
									    		}
												data = agmt_rev;
									    	}
									    	else if (i == 4) { //Cont_no
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (cont_no != null) {
					//					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
					//								}
									    		}
									    		data = cont_no;
									    	}
								    		
									    	else if (i == 5) { //Cont_rev
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (cont_rev != null) {
					//					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
					//								}	
									    		}
									    		data = cont_rev;
									    	}
									    	else if (i == 12) { //contract_type
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (cont_type != null) {
					//					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
					//								}	
									    		}
									    		data = cont_type;
									    	}
									    	
									    	else if (i == 8) {	// trans_cd
									    		trans_cd = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
//								    		if (trans_cd!=null) {
//												trans_cd = trans_cd.substring(1, trans_cd.length() - 1);
//											}
												queryString = "SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, trans_cd);
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			trans_cd = rset.getString(1);
									    		}else {
									    			trans_cd = "";
									    		}
									    		rset.close();
									    		stmt.close();
									    		data = trans_cd;
											}
									    	
									    	else if (i == 9) {	// trans_seq
												trans_seq = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
												if(trans_seq != null) {
//												trans_seq = trans_seq.substring(1, trans_seq.length()-1);
													
													queryString = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ?  AND ENTITY = 'R' AND COMPANY_CD = '2' ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, trans_seq);
													rset = stmt.executeQuery();
													
													if (rset.next()) {
														trans_seq = rset.getString(1);
													}
													else{
														trans_seq = "";
													}
													rset.close();
													stmt.close();
										    	}
												data=trans_seq;
											}
									    	
									    	else if(i == 10) { // BU_SEQ
									    		

								    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    			
									    		if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
									    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_LTCORA_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
										    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, agmt_no);
										    		stmt.setString(3, agmt_rev);
										    		stmt.setString(4, cont_no);
										    		stmt.setString(5, cont_rev);
										    		stmt.setString(6, cont_type);
										    		rset = stmt.executeQuery();
										    		
										    		if (rset.next()) {
										    			bu_seq_no = rset.getString(1);
										    		}
										    		else if(!rset.next()){
										    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//										    			if (bu_seq_no!=null) {
//															bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//														}
										    		}
										    		rset.close();
										    		stmt.close();	
									    		}
									    		else if (bu_seq_no == null) {
									    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
										    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, agmt_no);
										    		stmt.setString(3, agmt_rev);
										    		stmt.setString(4, cont_no);
										    		stmt.setString(5, cont_rev);
										    		stmt.setString(6, cont_type);
										    		rset = stmt.executeQuery();
										    		
										    		if (rset.next()) {
										    			bu_seq_no = rset.getString(1);
										    		}
										    		else if(!rset.next()){
//										    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										    			bu_seq_no = "1";
//										    			if (bu_seq_no!=null) {
//															bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
//														}
										    		}
										    		rset.close();
										    		stmt.close();	
									    		}
									    		
									    		data  = bu_seq_no;
									    	}
									    	else if (i == 25) {	// cargo_no
									    		
									    		if(cont_type.equals("O") || cont_type.equals("Q")) {
									    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
													
													queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, cd);
													stmt.setString(2, cargo_ref);
													rset = stmt.executeQuery();
													
													if (rset.next()) {
														cargo_no = rset.getString(1);
													}
													else{
														cargo_no="0";
														
													}
													rset.close();
													stmt.close();
									    		}
									    		else {
										    		
													cargo_no="0";
												}
												data=cargo_no;
											}
									    	else if(i == 27) {
									    		pro_abbr  = (line.split(",")[i].contains("'null'") ? null : line.split(",")[i]);
									    		queryString = "SELECT PROD_CD,MOLE_CD FROM FMS_PRODUCT_MOLECULE_MST WHERE MOLE_ABBR = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, pro_abbr);
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			pro_cd = rset.getString(1);
									    			mole_cd = rset.getString(2);
									    		}
									    		else {
									    			pro_cd = null;
									    			mole_cd = null;
									    		}
									    		rset.close();
									    		stmt.close();
									    		
									    		if(pro_cd != null && mole_cd != null) {
									    			data = pro_cd+"-"+mole_cd;
									    		}else {
									    			data = null;
									    		}
									    		
									    	}
									    	else {
									    		if (i == 6) {	// nom_rev
									    			nom_rev_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    			if (nom_rev_no != null) {
					//				    				nom_rev_no = nom_rev_no.substring(1, nom_rev_no.length() - 1);
					//								}
									    		}
									    				
									    		if (i == 7) {	// plant_seq
									    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    			if (plant_seq_no != null) {
					//				    				plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
					//								}
									    		}
//									    		if (i == 9) {	// trans_seq
//									    			trans_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//				    			if (trans_seq != null) {
//					//				    				trans_seq = trans_seq.substring(1, trans_seq.length() - 1);
//					//								}
//									    		}				    		
									    		if (i == 11) {	// gas_dt
									    			gas_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    			if (gas_dt != null) {
					//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
					//								}
									    		}
									    		if (i == 19) {	// qty_mmbtu
									    			qty_mmbtu = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    			if (gas_dt != null) {
					//				    				gas_dt = gas_dt.substring(1, gas_dt.length() - 1);
					//								}
									    		}
									    		
										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
									    	}	
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);		    	
									    }
										
									    queryString = "SELECT COUNTERPARTY_CD FROM FMS_DAILY_ALLOCATION_DTL_CT WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
									    		+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND NOM_REV_NO = ? AND PLANT_SEQ = ? "
									    		+ "AND TRANSPORTER_CD = ? AND TRANS_SEQ = ? AND BU_SEQ = ? AND GAS_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') "
									    		+ "AND CONTRACT_TYPE = ? AND CARGO_NO = ? ";
								    	stmt = conn.prepareStatement(queryString);
								    	stmt.setString(1, company_cd);
								    	stmt.setString(2, cd);
								    	stmt.setString(3, agmt_no);
								    	stmt.setString(4, agmt_rev);
								    	stmt.setString(5, cont_no);
								    	stmt.setString(6, cont_rev);
								    	stmt.setString(7, nom_rev_no);
								    	stmt.setString(8, plant_seq_no);
								    	stmt.setString(9, trans_cd);
								    	stmt.setString(10, trans_seq);
								    	stmt.setString(11, bu_seq_no);
								    	stmt.setString(12, gas_dt);
								    	stmt.setString(13, cont_type);
								    	stmt.setString(14, cargo_no);
							    	
								    	rset = stmt.executeQuery();
								    	
								    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("")) {
												//System.out.println(queryString1);
												
												logger.data(fname, (company_cd+"," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + ","+ nom_rev_no + " , " + plant_seq_no + "," + trans_cd + "," + trans_seq + "," + bu_seq_no + "," + gas_dt + "," + cont_type + "," +qty_mmbtu+ "," +cargo_no + "," ), conn, "");
												
												stmt1.executeUpdate();
												stmt1.close();
												
												logger_count++;
										}
								    	 else {
												stmt1.close();
												skipped_count++;     
												logger.data(fname, (company_cd+"," + cd + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + ","+ nom_rev_no + " , " + plant_seq_no + "," + trans_cd + "," + trans_seq + "," + bu_seq_no + "," + gas_dt + "," + cont_type + "," +qty_mmbtu+ "," + cargo_no + "," ), conn, "E");
										}
								    	 
								    	 rset.close();
										 stmt.close();
									}
									
									br.close();
				}
				
//				workbook = new XSSFWorkbook(file);
//				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";		
		
			}
			
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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
	
	
	public void FMS_INVOICE_MST() throws IOException, SQLException {
		
		function_nm="FMS_INVOICE_MST()";
		try {
			

			DateUtil utilDate = new DateUtil();
			DataBean_Sales_Invoice bill_freq = new DataBean_Sales_Invoice();
			
			table_name = "FMS_INVOICE_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			queryString1 = "INSERT INTO FMS_INVOICE_MST (COMPANY_CD, COUNTERPARTY_CD, FINANCIAL_YEAR, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
			           + "CONTRACT_TYPE, BU_UNIT, BU_STATE_TIN, BU_CONTACT_PERSON_CD, PLANT_SEQ, CONTACT_PERSON_CD, INVOICE_SEQ, INVOICE_NO, "
			           + "INVOICE_DT, FREQ, PERIOD_START_DT, PERIOD_END_DT, DUE_DT, ALLOC_QTY, SALE_PRICE, SALE_PRICE_UNIT, SALE_AMT, "
			           + "EXCHG_RATE_CD, EXCHG_RATE_DT, EXCHG_RATE_VALUE, INVOICE_RAISED_IN, GROSS_AMT, TAX_AMT, TAX_STRUCT_CD, TAX_EFF_DT, "
			           + "INVOICE_AMT, NET_PAYABLE_AMT, INV_STATUS, REMARK_1, REMARK_2, CHECKED_FLAG, CHECKED_BY, CHECKED_DT, AUTHORIZED_FLAG, "
			           + "AUTHORIZED_BY, AUTHORIZED_DT, APPROVED_FLAG, APPROVED_BY, APPROVED_DT, ENT_BY, ENT_DT, EXCHG_RATE_TYPE, MODIFY_BY, "
			           + "MODIFY_DT, TCS_TDS, TCS_AMT, TCS_FACTOR, INVOICE_ID_SEQ, PAY_RECV_AMT, PAY_RECV_DT, PAY_INSERT_BY, PAY_INSERT_DT, "
			           + "PAY_UPDATE_BY, PAY_UPDATE_DT, PAY_REMARK, TDS_GROSS_PERCENT, TDS_GROSS_AMT, TDS_TAX_PERCENT, TDS_TAX_AMT, PDF_INV_DTL, "
			           + "PRINT_BY_ORI, PRINT_DT_ORI, PRINT_BY_TRI, PRINT_DT_TRI, PRINT_BY_DUP, PRINT_DT_DUP, TRANSPORTATION_CHARGE, "
			           + "TRANSPORTATION_AMOUNT, SAP_APPROVAL, SAP_APPROVED_BY,SAP_APPROVED_DT,TCS_STRUCT_CD, TCS_EFF_DT, TDS_STRUCT_CD, TDS_EFF_DT,"
			           + "MARKET_MARGIN, OTHER_CHARGES, MARKET_MARGIN_AMT, OTHER_CHARGES_AMT,CARGO_NO,INV_FLAG,SUG_QTY,SUG_PERCENT,FIN_SYS,HOLD_AMT) "
			           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
			           + " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
			           + "?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, "
			           + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, ?, "
			           + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, "
			           + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, ?, ?,  TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, "
			           + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?,?,?,?, "
			           + " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
			           + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			Map<String, Integer> invseq = new HashMap<String, Integer>();
			file1 = new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_"+start_end_dt+".xlsx"));
				String fileName = migration_setup_dir + "EXPORT/FMS_INVOICE_MST_"+start_end_dt+".csv";
				try (//				workbook = new XSSFWorkbook(file);
				//				sheet = workbook.getSheetAt(0);
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
									String line = br.readLine();
									
									logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ_NO,CONTACT_PERSON_CD,TIMESTAMP,", conn);
									int inv_seq_no=1;
									
									while ((line = br.readLine()) != null) {
										total_count++; 
										String dt="",financial_year="",contact_person_cd="",mail="",billing_eff_dt = "", freq = "";
										agmt_no = ""; agmt_rev = ""; seq_no = "";
										abbr = "";
										cd = "0";
										data = null;
										
										index = 1;
										stmt1 = conn.prepareStatement(queryString1);
										
					//					row = rowIterator.next();
					//				    cellIterator = row.cellIterator();
					//				    while (cellIterator.hasNext()) {
										for (int i = 0; i < line.split(",").length; i++)
									    {	
					//				    	cell = cellIterator.next();
											data = null;
											
									    	if (i == 0) {
									    		
									    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!abbr.equals("NULL")) {
					//								abbr = abbr.substring(1, abbr.length() - 1);
					//							}
									    		data = company_cd;
									    	}
									    	else if (i == 1) {	// Counterparty_Cd
									    		
									    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!cont_ref.equals("NULL")) {
					//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
					//							}
									    		
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
									    	else if(i == 2) {
									    		financial_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		data = financial_year;
									    	}
											else if (i == 3) { //Agmt_no
									    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//				    		if (agmt_no != null) {
					//								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
					//							}
									    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, cont_ref);
										    		stmt.setString(3, cont_ref.split("-")[0]);
										    		
										    		
										    		rset = stmt.executeQuery();
										    		if (rset.next()) {
										    			agmt_no = rset.getString(1);
										    			agmt_rev = rset.getString(2);
										    			cont_no = rset.getString(3);
										    			cont_rev = rset.getString(4);
										    			cont_type = rset.getString(5);
										    			billing_eff_dt = rset.getString(6);
										    		} else {
										    			agmt_no = "";
										    			agmt_rev = "";
										    			cont_no = "";
										    			cont_rev = "";
										    			cont_type = "";
										    			billing_eff_dt = "";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, cont_ref);
										    		
										    		rset = stmt.executeQuery();
										    		if (rset.next()) {
										    			agmt_no = rset.getString(1);
										    			agmt_rev = rset.getString(2);
										    			cont_no = rset.getString(3);
										    			cont_rev = rset.getString(4);
										    			cont_type = rset.getString(5);
										    			billing_eff_dt = rset.getString(6);
										    		} else {
										    			agmt_no = "";
										    			agmt_rev = "";
										    			cont_no = "";
										    			cont_rev = "";
										    			cont_type = "";
										    			billing_eff_dt = "";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		
												data = agmt_no;
									    	}
									    	else if (i == 4) { //Agmt_rev
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (agmt_rev != null) {
					//									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
					//								}
									    		}
												data = agmt_rev;
									    	}
									    	else if (i == 5) { //Cont_no
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (cont_no != null) {
					//					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
					//								}
									    		}
									    		data = cont_no;
									    	}
								    		
									    	else if (i == 6) { //Cont_rev
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (cont_rev != null) {
					//					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
					//								}	
									    		}
									    		data = cont_rev;
									    	}
									    	else if (i == 7) { //contract_type
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (cont_type != null) {
					//					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
					//								}	
									    		}
									    		data = cont_type;
									    	}
									    					    	
									    	else if(i == 8) { // BU_SEQ
									    		bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    		if (bu_seq_no!=null) {
					//								bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
					//							}
												data  = bu_seq_no;
									    	}
									    	else if(i ==9) {
									    		queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
									    				+ " AND COUNTERPARTY_CD='2' AND ENTITY='B' AND SEQ_NO = ?";
												stmt=conn.prepareStatement(queryString);
												stmt.setString(1, bu_seq_no);
												rset = stmt.executeQuery();
									    		if (rset.next()) {				    			
									    			bu_plant_state = rset.getString(1);
									    		}else {
									    			bu_plant_state  ="0";
									    		}	
									    		rset.close();
									    		stmt.close();
									    		
									    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
												stmt=conn.prepareStatement(queryString);
												stmt.setString(1, bu_plant_state);
												rset = stmt.executeQuery();
									    		if (rset.next()) {				    			
									    			state_code = rset.getString(1);
									    		}else {
									    			state_code  ="0";
									    		}	
									    		rset.close();
									    		stmt.close();
									    		
									    		data = state_code;
									    		
									    	}
									    	else if(i == 10) { //BU_CONTACT_PERSON

									    		if(cont_type.equals("O") || cont_type.equals("Q")) {
									    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
													
													queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, cd);
													stmt.setString(2, cargo_ref);
													rset = stmt.executeQuery();
													
													if (rset.next()) {
														cargo_no = rset.getString(1);
													}
													else{
														cargo_no="0";
													}
													rset.close();
													stmt.close();
									    		}else {
									    			cargo_no = "0";	
									    		}
									    		
									    		queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
									    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y' ORDER BY CONTACT_PERSON ";
												stmt=conn.prepareStatement(queryString);
												
												String addr_flag = "";
												if(bu_seq_no.equals("1")) {								
													addr_flag = "P1";
												}else if(bu_seq_no.equals("2")){
													addr_flag = "P2";
												}else if(bu_seq_no.equals("3")){
													addr_flag = "P3";
												}
					
												stmt.setString(1, addr_flag);
												rset = stmt.executeQuery();
												
									    		if (rset.next()) {				    			
									    			bu_cont_person_cd=rset.getString(1);
									    		}else {
									    			bu_cont_person_cd ="0";
									    		}	
									    		
									    		rset.close();
									    		stmt.close();
									    		data=bu_cont_person_cd;
									    	}
//									    	else if(i == 11) { // PLANT_SEQ
//									    		if(cont_type.equals("O") || cont_type.equals("Q")) {
//									    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_LTCORA_CONT_PLANT WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
//										    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
//										    		stmt = conn.prepareStatement(queryString);
//										    		stmt.setString(1, cd);
//										    		stmt.setString(2, agmt_no);
//										    		stmt.setString(3, agmt_rev);
//										    		stmt.setString(4, cont_no);
//										    		stmt.setString(5, cont_rev);
//										    		stmt.setString(6, cont_type);
//										    		rset = stmt.executeQuery();
//										    		
//										    		if (rset.next()) {
//										    			plant_seq_no = rset.getString(1);
//										    		}else if(!rset.next()){
//										    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//										    		}
//										    		rset.close();
//										    		stmt.close();	
//									    		}else {
//									    			queryString = "SELECT PLANT_SEQ_NO FROM FMS_SUPPLY_CONT_PLANT WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
//										    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
//										    		stmt = conn.prepareStatement(queryString);
//										    		stmt.setString(1, cd);
//										    		stmt.setString(2, agmt_no);
//										    		stmt.setString(3, agmt_rev);
//										    		stmt.setString(4, cont_no);
//										    		stmt.setString(5, cont_rev);
//										    		stmt.setString(6, cont_type);
//										    		rset = stmt.executeQuery();
//										    		
//										    		if (rset.next()) {
//										    			plant_seq_no = rset.getString(1);
//										    		}else if(!rset.next()){
//										    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//						//				    			if (plant_seq_no!=null) {
//						//									plant_seq_no = plant_seq_no.substring(1, plant_seq_no.length() - 1);
//						//								}
//										    		}
//										    		rset.close();
//										    		stmt.close();	
//									    		}
//									    		data  = plant_seq_no;
//									    	}
									    	else if (i == 11) {	//plant_seq
								    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				//				    			if (inv_seq != null) {
				//				    				inv_seq = inv_seq.substring(1, inv_seq.length() - 1);
				//								}
								    			data = plant_seq_no; 
								    		}
									    	else if(i == 12)
									    	{
									    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		
									    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
														
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, mail);
										    	stmt.setString(2, cd);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		contact_person_cd = rset.getString(1);
										    	}
										    	else {
										    		contact_person_cd = "1";
										    	}
										    	rset.close();
										    	stmt.close();
									    		data=contact_person_cd;
									    	}
									    	else if(i == 13) {
									    		if (invseq.containsKey(financial_year)) {
									    			
									    			inv_seq_no=invseq.get(financial_year);
									    			inv_seq_no=inv_seq_no+1;
													invseq.put(financial_year,inv_seq_no);
													
												} else {
													inv_seq_no=1;
													invseq.put(financial_year,inv_seq_no);
													
												}
									    		data=inv_seq_no+"";
									    	}
									    	else if(i == 14) {
									    		String inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@", "/");
									    		data = inv_no;
									    	}
									    	else if (i == 16) {

										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										    	freq = data;
										    	
									    	}
											else if (i == 17 && !freq.contains("0") && (cont_ref.startsWith("L") || cont_ref.startsWith("X") || cont_ref.startsWith("C") || cont_ref.startsWith("A") || !cont_ref.startsWith("R"))) {
												billing_eff_dt = "";
												data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
											
												if (data != null) {
													data = data.split(" ")[0];
												}
												bill_freq.setBilling_cycle(freq);
												bill_freq.setMonth(data.split("/")[1]);
												bill_freq.setYear(data.split("/")[2]);
												bill_freq.getBillingCyclePeriod();
												
												if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
											    	queryString = "SELECT TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY') FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? AND CARGO_NO = ? ";
											    	stmt = conn.prepareStatement(queryString);
											    	stmt.setString(1, agmt_no);
//											    	stmt.setString(2, agmt_rev);
											    	stmt.setString(2, cont_no);
//											    	stmt.setString(4, cont_rev);
											    	stmt.setString(3, cont_type);
											    	stmt.setString(4, cd);
											    	stmt.setString(5, cargo_no);
											    	rset = stmt.executeQuery();
											    	
											    	if (rset.next()) {
											    		billing_eff_dt = rset.getString(1);
											    	}
											    	
											    	rset.close();
											    	stmt.close();
											    	
												}
												
												else if(cont_ref.startsWith("L") || cont_ref.startsWith("X") || !cont_ref.startsWith("R")) {
											    	queryString = "SELECT TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? ";
											    	stmt = conn.prepareStatement(queryString);
											    	stmt.setString(1, agmt_no);
											    	stmt.setString(2, agmt_rev);
											    	stmt.setString(3, cont_no);
											    	stmt.setString(4, cont_rev);
											    	stmt.setString(5, cont_type);
											    	stmt.setString(6, cd);
											    	rset = stmt.executeQuery();
											    	
											    	if (rset.next()) {
											    		billing_eff_dt = rset.getString(1);
											    	}
											    	
											    	rset.close();
											    	stmt.close();
											    	
												}
												
												if (data != null) {
													data = data.split(" ")[0];
												}
												int isGreater=utilDate.getDays(billing_eff_dt, bill_freq.getPeriod_start_dt());
												

												
												if(isGreater>1)
												{
													data=billing_eff_dt;
												}
												else {
													data = bill_freq.getPeriod_start_dt();
												}
												
												data = data + " 00:00:00";
											//												System.out.println(freq+"=="+data);

//												if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
//													System.out.println(agmt_no+"=="+agmt_no+"=="+cont_no+"=="+cont_rev+"=="+cd+"==="+cont_ref);
//													System.out.println(billing_eff_dt+"===="+freq+"=="+data);
//												}
												
											}
											else if (i == 18 && !freq.contains("0") && (cont_ref.startsWith("L") || cont_ref.startsWith("X") || cont_ref.startsWith("C") || cont_ref.startsWith("A") || !cont_ref.startsWith("R"))) {
												billing_eff_dt = "";
												data = line.split(",")[i].contains("null") ? null : line.split(",")[i];

												if (data != null) {
													data = data.split(" ")[0];
												}
												bill_freq.setBilling_cycle(freq);
												bill_freq.setMonth(data.split("/")[1]);
												bill_freq.setYear(data.split("/")[2]);
												bill_freq.getBillingCyclePeriod();
												
												
												if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
											    	queryString = "SELECT TO_CHAR(TO_DATE(ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY') FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? AND CARGO_NO = ? ";
											    	stmt = conn.prepareStatement(queryString);
											    	stmt.setString(1, agmt_no);
//											    	stmt.setString(2, agmt_rev);
											    	stmt.setString(2, cont_no);
//											    	stmt.setString(4, cont_rev);
											    	stmt.setString(3, cont_type);
											    	stmt.setString(4, cd);
											    	stmt.setString(5, cargo_no);
											    	rset = stmt.executeQuery();
											    	
											    	if (rset.next()) {
											    		billing_eff_dt = rset.getString(1);
											    	}
											    	
											    	rset.close();
											    	stmt.close();
											    	
												}
												
												else if(cont_ref.startsWith("L") || cont_ref.startsWith("X") || !cont_ref.startsWith("R")) {
											    	queryString = "SELECT TO_CHAR(END_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? ";
											    	stmt = conn.prepareStatement(queryString);
											    	stmt.setString(1, agmt_no);
											    	stmt.setString(2, agmt_rev);
											    	stmt.setString(3, cont_no);
											    	stmt.setString(4, cont_rev);
											    	stmt.setString(5, cont_type);
											    	stmt.setString(6, cd);
											    	rset = stmt.executeQuery();
											    	
											    	if (rset.next()) {
											    		billing_eff_dt = rset.getString(1);
											    	}
											    	
											    	rset.close();
											    	stmt.close();
											    	
												}
											
												int isGreater=utilDate.getDays(billing_eff_dt, bill_freq.getPeriod_end_dt());
												
												if(isGreater<=0)
												{
													data=billing_eff_dt;
												}
												else {
													data = bill_freq.getPeriod_end_dt();
												}
												
												data = data + " 00:00:00";
//												if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
//													System.out.println(agmt_no+"=="+agmt_no+"=="+cont_no+"=="+cont_rev+"=="+cd+"==="+cont_ref);
//													System.out.println(billing_eff_dt+"===="+freq+"=="+data+"==="+bill_freq.getPeriod_end_dt());
//												}
												
											}
									    	else if(i == 24) {
									    		name = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    		if (name!=null) {
					//								name = name.substring(1, name.length() - 1);
					//							}
									    		exchg_cd="";
												queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, name);
										    	rset = stmt.executeQuery();
										    	if (rset.next()) {
										    		exchg_cd = rset.getString(1);
										    	}
										    	else {
										    		exchg_cd = "0";
										    	}
										    	rset.close();
										    	stmt.close();
										    	data = exchg_cd;
									    	}
									    	else if(i == 30) {
									    		
									    		String temp_struct = "";
									    		dt = "";
												name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@ ", ", ");;
									    
									    		if (!name.contains(", ")) {
													queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, name);
													rset = stmt.executeQuery();
													if (rset.next()) {
														temp_struct = rset.getString(1);
														dt = rset.getString(2);
													}
													rset.close();
													stmt.close();
												}
									    		else {

													int flag = 0;
													queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, "%"+name.split(", ")[0]+"%");
													rset = stmt.executeQuery();
													
													while (rset.next()) {
														
														for (int j = 0; j < name.split(", ").length; j++) {
															if (rset.getString(2).contains(name.split(", ")[j])) {
																flag = 1;
															}
															else {
																flag = 0;
																break;
															}
														}
														
														if (flag == 1) {
															temp_struct = rset.getString(1);
															name=rset.getString(2);
															dt = rset.getString(3);
															break;
														}
													}
													rset.close();
													stmt.close();
												}
											    	data = temp_struct;
									    	}
									    	else if(i == 31) {
									    		data = dt;
									    	}
//									    	else if(i == 51) {
//									    		if(cont_type.equals("O") || cont_type.equals("Q")) {
//									    			queryString = "SELECT TAX_APP  "
//															+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y' AND INVOICE_TYPE IN ('SI', 'ST')";
//													stmt = conn.prepareStatement(queryString);
//													stmt.setString(1, cd);
//													stmt.setString(2, "C");
//													stmt.setString(3,company_cd);
//													rset = stmt.executeQuery();
//													if(rset.next()) {
//														data = rset.getString(1);
//													}
//													else {
//														data = "NA";
//													}
//													rset.close();
//													stmt.close();	
//									    		}
//									    		
//									    	}
									    	else if(i == 78) {
									    		queryString = "SELECT TAX_STRUCT_CD  "
														+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TCS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, cd);
												stmt.setString(2, "C");
												stmt.setString(3,company_cd);
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
									    	else if(i == 80) {
									    		queryString = "SELECT TAX_STRUCT_CD  "
														+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TDS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, cd);
												stmt.setString(2, "C");
												stmt.setString(3,company_cd);
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
									    	else if(i == 86) {
//									    		if(cont_type.equals("O") || cont_type.equals("Q")) {
//									    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//													
//													queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
//													stmt = conn.prepareStatement(queryString);
//													stmt.setString(1, cd);
//													stmt.setString(2, cargo_ref);
//													rset = stmt.executeQuery();
//													
//													if (rset.next()) {
//														cargo_no = rset.getString(1);
//													}
//													else{
//														cargo_no="0";
//													}
//													rset.close();
//													stmt.close();
//									    		}else {
//									    			cargo_no = "0";	
//									    		}
//									    		
//									    			
									    		data  = cargo_no;
									    	}
									    	
									    	else {			    	
//									    		if (i == 2) {	//fin_yr
//									    			fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//				    			if (fin_yr != null) {
//					//				    				fin_yr = fin_yr.substring(1, fin_yr.length() - 1);
//					//								}
//									    		}
									    		
//									    		if (i == 13) {	//invoice_seq
//									    			inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//				    			if (inv_seq != null) {
//					//				    				inv_seq = inv_seq.substring(1, inv_seq.length() - 1);
//					//								}
//									    		}
									    		
//									    		if (i == 11) {	//plant_seq
//									    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//				    			if (inv_seq != null) {
//					//				    				inv_seq = inv_seq.substring(1, inv_seq.length() - 1);
//					//								}
//									    		}
									    		
									    		
										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
									    	}	
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);		    	
									    }
										
									    queryString = "SELECT COUNTERPARTY_CD FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
									    		+ "AND FINANCIAL_YEAR = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT =? "
									    		+ "AND BU_STATE_TIN = ?  AND PLANT_SEQ = ? AND INVOICE_SEQ = ? AND CARGO_NO = ?";
								    	stmt = conn.prepareStatement(queryString);
								    	stmt.setString(1, company_cd);
								    	stmt.setString(2, cd);
								    	stmt.setString(3, fin_yr);
								    	stmt.setString(4, agmt_no);
								    	stmt.setString(5, agmt_rev);
								    	stmt.setString(6, cont_no);
								    	stmt.setString(7, cont_rev);
								    	stmt.setString(8, cont_type);
								    	stmt.setString(9, bu_seq_no);
								    	stmt.setString(10, state_code);
								    	stmt.setString(11, plant_seq_no);
								    	stmt.setInt(12, inv_seq_no);
								    	stmt.setString(13, cargo_no);
								    	
								    	rset = stmt.executeQuery();
								    	
								    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") ) {
												//System.out.println(queryString1);
												
												logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," ), conn, "");
												
												stmt1.executeUpdate();
												stmt1.close();
												
												logger_count++;
										}
								    	 else {
												stmt1.close();
												skipped_count++;     
												logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," ), conn, "E");
												
												if (invseq.containsKey(financial_year)) {
													inv_seq_no = invseq.get(financial_year);
													inv_seq_no=inv_seq_no-1;
													invseq.put(financial_year, inv_seq_no);
												}
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
			
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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
	
public void FMS_INVOICE_MST_UPDATE() throws IOException, SQLException {
		
		function_nm="FMS_INVOICE_MST_UPDATE()";
		try {
			

			
			table_name = "FMS_INVOICE_MST_UPDATE";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			queryString1 = "UPDATE FMS_INVOICE_MST SET TDS_GROSS_PERCENT = ?, TDS_GROSS_AMT = ?, TDS_TAX_PERCENT = ?, TDS_TAX_AMT = ?, "
					+ " PAY_RECV_AMT = ? "
					+ "WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND INVOICE_ID_SEQ = ? "
					+ "AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND CARGO_NO = ? AND PLANT_SEQ = ? AND COMPANY_CD = '2' ";
			
			stmt1 = conn.prepareStatement(queryString1);
			file1 = new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_UPDATE_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_"+start_end_dt+".xlsx"));
				String fileName = migration_setup_dir + "EXPORT/FMS_INVOICE_MST_UPDATE_"+start_end_dt+".csv";
				try (//				workbook = new XSSFWorkbook(file);
				//				sheet = workbook.getSheetAt(0);
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
									String line = br.readLine();
									
									logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,BU_UNIT,PLANT_SEQ,CONTRACT_TYPE,CARGO_NO,INVOICE_NO,FINANCIAL_YEAR,INVOICE_ID_SEQ,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,PAY_RECV_AMT,TRANSPORTATION_AMOUNT,TIMESTAMP,", conn);
									
									while ((line = br.readLine()) != null) {
										total_count++; 
										String invoice_no = "", invoice_id_seq = "", tds_gross_percent = "", tds_tax_percent = "", tds_tax_amt = "", tds_gross_amt = "", plant_seq_no = "", pay_recv_amt = "", trans_amt = "";
										agmt_no = ""; agmt_rev = ""; seq_no = "";
										abbr = "";
										cd = "0";
										data = null;
										
										index = 1;
										
										queryString1 = "UPDATE FMS_INVOICE_MST SET TDS_GROSS_PERCENT = ?, TDS_GROSS_AMT = ?, TDS_TAX_PERCENT = ?, TDS_TAX_AMT = ?, "
												+ " PAY_RECV_AMT = ? "
												+ "WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND INVOICE_ID_SEQ = ? "
												+ "AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND CARGO_NO = ? AND PLANT_SEQ = ? AND COMPANY_CD = '2' ";
										stmt1 = conn.prepareStatement(queryString1);
										
										for (int i = 0; i < line.split(",").length; i++)
									    {	
											data = null;
											if (i == 0) {
									    		abbr = (line.split(",")[i].contains("null") ? "NULL" : line.split(",")[i]);
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
											
											else if (i == 1) {	//agmt_no
									    		
									    		cont_ref = (line.split(",")[i].contains("null") ? "NULL" : line.split(",")[i]);
									    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, cont_ref);
										    		stmt.setString(3, cont_ref.split("-")[0]);
										    		
										    		
										    		rset = stmt.executeQuery();
										    		if (rset.next()) {
										    			agmt_no = rset.getString(1);
										    			agmt_rev = rset.getString(2);
										    			cont_no = rset.getString(3);
										    			cont_rev = rset.getString(4);
										    			cont_type = rset.getString(5);
										    		} else {
										    			agmt_no = "";
										    			agmt_rev = "";
										    			cont_no = "";
										    			cont_rev = "";
										    			cont_type = "";
										    		}
										    		rset.close();
										    		stmt.close();
										    		data = agmt_no;
									    		}
									    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, cont_ref);
										    		
										    		rset = stmt.executeQuery();
										    		if (rset.next()) {
										    			agmt_no = rset.getString(1);
										    			agmt_rev = rset.getString(2);
										    			cont_no = rset.getString(3);
										    			cont_rev = rset.getString(4);
										    			cont_type = rset.getString(5);
										    		} else {
										    			agmt_no = "";
										    			agmt_rev = "";
										    			cont_no = "";
										    			cont_rev = "";
										    			cont_type = "";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		else {
									    			agmt_no = cont_ref;
									    		}
									    		data = agmt_no;
									    	}
											else if (i == 2) { //Agmt_rev
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		}
												data = agmt_rev;
									    	}
									    	else if (i == 3) { //Cont_no
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		}
									    		data = cont_no;
									    	}
								    		
									    	else if (i == 4) { //Cont_rev
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		}
									    		data = cont_rev;
									    	}
									    	else if(i == 5) { // BU_SEQ
									    		bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
												data  = bu_seq_no;
									    	}
									    	else if(i == 8) { // cargo_no
									    		if(cont_type.equals("O") || cont_type.equals("Q")) {
									    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
													
													queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, cd);
													stmt.setString(2, cargo_ref);
													rset = stmt.executeQuery();
													
													if (rset.next()) {
														cargo_no = rset.getString(1);
													}
													else{
														cargo_no="0";
													}
													rset.close();
													stmt.close();
									    		}else {
									    			cargo_no = "0";	
									    		}
												data  = cargo_no;
									    	}
									    	if (i == 13) {
									    		

										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//										    	if (cont_type.equals("X")) {
//										    		queryString = "SELECT (NVL(GROSS_AMT, 0) + NVL(TRANSPORTATION_AMOUNT, 0) + NVL(OTHER_CHARGES_AMT, 0) + NVL(MARKET_MARGIN_AMT, 0)) * ? / 100 FROM FMS_INVOICE_MST "
//										    				+ "WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND INVOICE_ID_SEQ = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND CARGO_NO = ? AND PLANT_SEQ = ? ";
//										    		stmt = conn.prepareStatement(queryString);
//										    		stmt.setString(1, tds_gross_percent);
//										    		stmt.setString(2, cd);
//										    		stmt.setString(3, agmt_no);
//										    		stmt.setString(4, agmt_rev);
//										    		stmt.setString(5, cont_no);
//										    		stmt.setString(6, cont_rev);
//										    		stmt.setString(7, invoice_id_seq);
//										    		stmt.setString(8, fin_yr);
//										    		stmt.setString(9, cont_type);
//										    		stmt.setString(10, cargo_no);
//										    		stmt.setString(11, plant_seq_no);
//										    		rset = stmt.executeQuery();
//										    		
//										    		if (rset.next()) {
//										    			data = rset.getString(1) ;
//										    		}
//										    		else {
//										    			data = "0";
//										    		}
//										    		rset.close();
//										    		stmt.close();
//										    	}
									    		
								    			stmt1.setString(2, data);
								    			tds_gross_amt = data;
									    		
									    	}
									    	
									    	else {
									    		if (i == 6) {	// plant_seq_no
									    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}
									    		if (i == 7) {	// cont_type
									    			if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    				cont_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										    		}
									    			
									    		}
									    		if (i == 9) {	// invoice_no
									    			invoice_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}
									    		if (i == 10) {	// fin_yr
									    			fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}			    	
									    		if (i == 11) {	// invoice_id_seq
									    			invoice_id_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}			    	
									    		if (i == 12) {	// tds_gross_percent
									    			tds_gross_percent = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    			stmt1.setString(1, tds_gross_percent);
									    		}
									    		if (i == 14) {	// tds_tax_percent
									    			tds_tax_percent = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    			stmt1.setString(3, tds_tax_percent);
									    		}			    	
									    		if (i == 15) {	// tds_tax_amt
									    			tds_tax_amt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    			stmt1.setString(4, tds_tax_amt);
									    		}			    	
									    		if (i == 16) {	// pay_recv_amt
									    			pay_recv_amt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
													stmt1.setString(5, pay_recv_amt);
									    		}
									    		if (i == 17) {	// trans_amt
									    			trans_amt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}
									    		
										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    	}	
//									    	System.out.println(index+"-"+data);
//									    	stmt1.setString(index++, data);		    	
									    }
										
										stmt1.setString(6, cd);
						    			stmt1.setString(7, agmt_no);
						    			stmt1.setString(8, agmt_rev);
						    			stmt1.setString(9, cont_no);
						    			stmt1.setString(10, cont_rev);
						    			stmt1.setString(11, invoice_id_seq);
						    			stmt1.setString(12, fin_yr);
						    			stmt1.setString(13, cont_type);
						    			stmt1.setString(14, cargo_no);
						    			stmt1.setString(15, plant_seq_no);
						    			
										//System.out.println(queryString1);
								
										
										stmt1.executeUpdate();
										stmt1.close();
										
										logger_count++;
										
										// TRANSPORTATION AMOUNT
										if (trans_amt != null) {
											queryString1 = "UPDATE FMS_INVOICE_MST SET TRANSPORTATION_AMOUNT = ? "
												+ "WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND INVOICE_ID_SEQ = ? "
												+ "AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND CARGO_NO = ? AND PLANT_SEQ = ? AND COMPANY_CD = '2' ";
											stmt1 = conn.prepareStatement(queryString1);
	
											
											stmt1.setString(1, trans_amt);
											stmt1.setString(2, cd);
							    			stmt1.setString(3, agmt_no);
							    			stmt1.setString(4, agmt_rev);
							    			stmt1.setString(5, cont_no);
							    			stmt1.setString(6, cont_rev);
							    			stmt1.setString(7, invoice_id_seq);
							    			stmt1.setString(8, fin_yr);
							    			stmt1.setString(9, cont_type);
							    			stmt1.setString(10, cargo_no);
							    			stmt1.setString(11, plant_seq_no);
							    			stmt1.executeUpdate();
							    			stmt1.close();
							    			
										}
										

										// TCS FACTOR AMOUNT
											queryString1 = "UPDATE FMS_INVOICE_MST SET TCS_FACTOR = NULL, TCS_AMT = NULL "
												+ "WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND INVOICE_ID_SEQ = ? "
												+ "AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND CARGO_NO = ? AND PLANT_SEQ = ? AND COMPANY_CD = '2' AND TCS_TDS = 'TCS' AND TCS_FACTOR IS NOT NULL AND TDS_GROSS_PERCENT IS NOT NULL ";
											stmt1 = conn.prepareStatement(queryString1);
	
											
											stmt1.setString(1, cd);
							    			stmt1.setString(2, agmt_no);
							    			stmt1.setString(3, agmt_rev);
							    			stmt1.setString(4, cont_no);
							    			stmt1.setString(5, cont_rev);
							    			stmt1.setString(6, invoice_id_seq);
							    			stmt1.setString(7, fin_yr);
							    			stmt1.setString(8, cont_type);
							    			stmt1.setString(9, cargo_no);
							    			stmt1.setString(10, plant_seq_no);
							    			if (stmt1.executeUpdate() > 0) {
							    				logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + bu_seq_no + "," + plant_seq_no + "," + cont_type + "," + cargo_no + "," + invoice_no + " (TCS and TDS both are applicable) , " + fin_yr + "," + invoice_id_seq + "," + tds_gross_percent + "," + tds_gross_amt + "," + tds_tax_percent + "," + tds_tax_amt + "," + pay_recv_amt + "," + trans_amt + ","), conn, "");
							    			}
							    			else {
							    				logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + bu_seq_no + "," + plant_seq_no + "," + cont_type + "," + cargo_no + "," + invoice_no + " , " + fin_yr + "," + invoice_id_seq + "," + tds_gross_percent + "," + tds_gross_amt + "," + tds_tax_percent + "," + tds_tax_amt + "," + pay_recv_amt + "," + trans_amt + ","), conn, "");
							    			}
							    			
							    			stmt1.close();
										
										
									}
									br.close();
				}
				
				
				queryString1 = "UPDATE FMS_INVOICE_MST SET TCS_FACTOR = NULL , TCS_AMT = NULL WHERE COMPANY_CD = ? AND TCS_TDS = 'TDS' ";
				stmt1 = conn.prepareStatement(queryString);
				stmt1.setString(1, company_cd);
				stmt1.executeUpdate();
				stmt1.close();
				
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";		
		
			}
			
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			
			
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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
	
public void FMS_INVOICE_MST_TAXCD_UPDATE() throws IOException, SQLException {
	
	function_nm="FMS_INVOICE_MST_TAXCD_UPDATE()";
	try {
		

		DateUtil utilDate = new DateUtil();
		DataBean_Sales_Invoice bill_freq = new DataBean_Sales_Invoice();
		
		table_name = "FMS_INVOICE_MST_TAXCD_UPDATE";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		
		// Update query for updating TAX_STRUCT_CD for LTCORA contracts (contract_type Q and O) 
		queryString1 = "UPDATE FMS_INVOICE_MST SET TAX_STRUCT_CD = ? "
				+ " WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND FINANCIAL_YEAR = ? AND AGMT_NO = ? "
				+ " AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT = ? "
				+ " AND BU_STATE_TIN = ? AND PLANT_SEQ = ? AND INVOICE_SEQ = ? AND INVOICE_NO = ? AND "
				+ " PERIOD_START_DT = TO_DATE(?, 'DD/MM/YYYY') AND PERIOD_END_DT = TO_DATE(?, 'DD/MM/YYYY') AND CARGO_NO = ? ";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		Map<String, Integer> invseq = new HashMap<String, Integer>();
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_TAXCD_UPDATE_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
//			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_TAXCD_UPDATE_"+start_end_dt+".xlsx"));
			String fileName = migration_setup_dir + "EXPORT/FMS_INVOICE_MST_TAXCD_UPDATE_"+start_end_dt+".csv";
			try (//				workbook = new XSSFWorkbook(file);
			//				sheet = workbook.getSheetAt(0);
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				//				if (rowIterator.hasNext()) {	// For skipping the first row
				//					rowIterator.next();
				//				}
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ_NO,CONTACT_PERSON_CD,TAX_STRUCT_CD,TIMESTAMP,", conn);
								int inv_seq_no=1;
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									String dt="",financial_year="",contact_person_cd="",mail="",billing_eff_dt = "", freq = "", tax_struct_cd = "";
									agmt_no = ""; agmt_rev = ""; seq_no = "";
									abbr = "";
									cd = "0";
									data = null;
									
									index = 2;
									stmt1 = conn.prepareStatement(queryString1);
									
				//					row = rowIterator.next();
				//				    cellIterator = row.cellIterator();
				//				    while (cellIterator.hasNext()) {
									for (int i = 0; i < line.split(",").length; i++)
								    {	
				//				    	cell = cellIterator.next();
										data = null;
										
								    	if (i == 0) {
								    		
								    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				//				    		if (!abbr.equals("NULL")) {
				//								abbr = abbr.substring(1, abbr.length() - 1);
				//							}
								    		data = company_cd;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if (i == 1) {	// Counterparty_Cd
								    		
								    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				//				    		if (!cont_ref.equals("NULL")) {
				//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
				//							}
								    		
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
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if(i == 2) {
								    		financial_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		data = financial_year;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
										else if (i == 3) { //Agmt_no
								    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//				    		if (agmt_no != null) {
				//								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
				//							}
								    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
								    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, cont_ref);
									    		stmt.setString(3, cont_ref.split("-")[0]);
									    		
									    		
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			agmt_no = rset.getString(1);
									    			agmt_rev = rset.getString(2);
									    			cont_no = rset.getString(3);
									    			cont_rev = rset.getString(4);
									    			cont_type = rset.getString(5);
									    			billing_eff_dt = rset.getString(6);
									    		} else {
									    			agmt_no = "";
									    			agmt_rev = "";
									    			cont_no = "";
									    			cont_rev = "";
									    			cont_type = "";
									    			billing_eff_dt = "";
									    		}
									    		rset.close();
									    		stmt.close();
								    		}
								    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
								    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, cont_ref);
									    		
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			agmt_no = rset.getString(1);
									    			agmt_rev = rset.getString(2);
									    			cont_no = rset.getString(3);
									    			cont_rev = rset.getString(4);
									    			cont_type = rset.getString(5);
									    			billing_eff_dt = rset.getString(6);
									    		} else {
									    			agmt_no = "";
									    			agmt_rev = "";
									    			cont_no = "";
									    			cont_rev = "";
									    			cont_type = "";
									    			billing_eff_dt = "";
									    		}
									    		rset.close();
									    		stmt.close();
								    		}
								    		
											data = agmt_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if (i == 4) { //Agmt_rev
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//					    		if (agmt_rev != null) {
				//									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
				//								}
								    		}
											data = agmt_rev;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if (i == 5) { //Cont_no
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//					    		if (cont_no != null) {
				//					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
				//								}
								    		}
								    		data = cont_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
							    		
								    	else if (i == 6) { //Cont_rev
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//					    		if (cont_rev != null) {
				//					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
				//								}	
								    		}
								    		data = cont_rev;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if (i == 7) { //contract_type
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//					    		if (cont_type != null) {
				//					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
				//								}	
								    		}
								    		data = cont_type;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    					    	
								    	else if(i == 8) { // BU_SEQ
								    		bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				//				    		if (bu_seq_no!=null) {
				//								bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
				//							}
											data  = bu_seq_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if(i ==9) {
								    		queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
								    				+ " AND COUNTERPARTY_CD='2' AND ENTITY='B' AND SEQ_NO = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_seq_no);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			bu_plant_state = rset.getString(1);
								    		}else {
								    			bu_plant_state  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_plant_state);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			state_code = rset.getString(1);
								    		}else {
								    			state_code  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		data = state_code;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    		
								    	}
								    	else if(i == 10) { //BU_CONTACT_PERSON

								    		if(cont_type.equals("O") || cont_type.equals("Q")) {
								    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
												
												queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, cd);
												stmt.setString(2, cargo_ref);
												rset = stmt.executeQuery();
												
												if (rset.next()) {
													cargo_no = rset.getString(1);
												}
												else{
													cargo_no="0";
												}
												rset.close();
												stmt.close();
								    		}else {
								    			cargo_no = "0";	
								    		}
								    		
								    		queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
								    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y' ORDER BY CONTACT_PERSON ";
											stmt=conn.prepareStatement(queryString);
											
											String addr_flag = "";
											if(bu_seq_no.equals("1")) {								
												addr_flag = "P1";
											}else if(bu_seq_no.equals("2")){
												addr_flag = "P2";
											}else if(bu_seq_no.equals("3")){
												addr_flag = "P3";
											}
				
											stmt.setString(1, addr_flag);
											rset = stmt.executeQuery();
											
								    		if (rset.next()) {				    			
								    			bu_cont_person_cd=rset.getString(1);
								    		}else {
								    			bu_cont_person_cd ="0";
								    		}	
								    		
								    		rset.close();
								    		stmt.close();
								    		data=bu_cont_person_cd;
								    	}
								    	else if (i == 11) {	//plant_seq
							    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
			//				    			if (inv_seq != null) {
			//				    				inv_seq = inv_seq.substring(1, inv_seq.length() - 1);
			//								}
							    			data = plant_seq_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	 
							    		}
								    	else if(i == 12)
								    	{
								    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
								    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
													
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, mail);
									    	stmt.setString(2, cd);
									    	rset = stmt.executeQuery();
									    	
									    	if (rset.next()) {
									    		contact_person_cd = rset.getString(1);
									    	}
									    	else {
									    		contact_person_cd = "1";
									    	}
									    	rset.close();
									    	stmt.close();
								    		data=contact_person_cd;
								    	}
								    	else if(i == 14) {
								    		String inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@", "/");
								    		queryString = "SELECT INVOICE_SEQ, TAX_STRUCT_CD FROM FMS_INVOICE_MST WHERE"
								    				+ " COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND FINANCIAL_YEAR = ? AND AGMT_NO = ? "
								    				+ " AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT = ? "
								    				+ " AND BU_STATE_TIN = ? AND PLANT_SEQ = ? AND INVOICE_NO = ? AND CARGO_NO = ? ";
								    		stmt = conn.prepareStatement(queryString);
								    		stmt.setString(1, company_cd);
								    		stmt.setString(2, cd);
								    		stmt.setString(3, financial_year);
								    		stmt.setString(4, agmt_no);
								    		stmt.setString(5, agmt_rev);
								    		stmt.setString(6, cont_no);
								    		stmt.setString(7, cont_rev);
								    		stmt.setString(8, cont_type);
								    		stmt.setString(9, bu_seq_no);
								    		stmt.setString(10, state_code);
								    		stmt.setString(11, plant_seq_no);
								    		stmt.setString(12, inv_no);
								    		stmt.setString(13, cargo_no);
								    		rset = stmt.executeQuery();
								    		
								    		if (rset.next()) {
								    			inv_seq_no = rset.getInt(1);
								    			tax_struct_cd = rset.getString(2);
								    		}
								    		else {
								    			inv_seq_no = 0;
								    			tax_struct_cd = "0";
								    		}
								    		rset.close();
								    		stmt.close();

//									    	System.out.println(index+"-"+inv_seq_no);
									    	stmt1.setInt(index++, inv_seq_no);	
								    		
								    		data = inv_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if (i == 16) {

									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    	freq = data;
									    	
								    	}
										else if (i == 17 && !freq.contains("0") && (cont_ref.startsWith("L") || cont_ref.startsWith("X") || cont_ref.startsWith("C") || cont_ref.startsWith("A") || !cont_ref.startsWith("R"))) {
											billing_eff_dt = "";
											data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										
											if (data != null) {
												data = data.split(" ")[0];
											}
											bill_freq.setBilling_cycle(freq);
											bill_freq.setMonth(data.split("/")[1]);
											bill_freq.setYear(data.split("/")[2]);
											bill_freq.getBillingCyclePeriod();
											
											if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
										    	queryString = "SELECT TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY') FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? AND CARGO_NO = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, agmt_no);
//										    	stmt.setString(2, agmt_rev);
										    	stmt.setString(2, cont_no);
//										    	stmt.setString(4, cont_rev);
										    	stmt.setString(3, cont_type);
										    	stmt.setString(4, cd);
										    	stmt.setString(5, cargo_no);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		billing_eff_dt = rset.getString(1);
										    	}
										    	
										    	rset.close();
										    	stmt.close();
										    	
											}
											
											else if(cont_ref.startsWith("L") || cont_ref.startsWith("X") || !cont_ref.startsWith("R")) {
										    	queryString = "SELECT TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, agmt_no);
										    	stmt.setString(2, agmt_rev);
										    	stmt.setString(3, cont_no);
										    	stmt.setString(4, cont_rev);
										    	stmt.setString(5, cont_type);
										    	stmt.setString(6, cd);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		billing_eff_dt = rset.getString(1);
										    	}
										    	
										    	rset.close();
										    	stmt.close();
										    	
											}
											
											if (data != null) {
												data = data.split(" ")[0];
											}
											int isGreater=utilDate.getDays(billing_eff_dt, bill_freq.getPeriod_start_dt());
											

											
											if(isGreater>1)
											{
												data=billing_eff_dt;
											}
											else {
												data = bill_freq.getPeriod_start_dt();
											}
											
//											data = data + " 00:00:00";
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
//											System.out.println(freq+"=="+data);

//											if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
//												System.out.println(agmt_no+"=="+agmt_no+"=="+cont_no+"=="+cont_rev+"=="+cd+"==="+cont_ref);
//												System.out.println(billing_eff_dt+"===="+freq+"=="+data);
//											}
											
										}
										else if (i == 18 && !freq.contains("0") && (cont_ref.startsWith("L") || cont_ref.startsWith("X") || cont_ref.startsWith("C") || cont_ref.startsWith("A") || !cont_ref.startsWith("R"))) {
											billing_eff_dt = "";
											data = line.split(",")[i].contains("null") ? null : line.split(",")[i];

											if (data != null) {
												data = data.split(" ")[0];
											}
											bill_freq.setBilling_cycle(freq);
											bill_freq.setMonth(data.split("/")[1]);
											bill_freq.setYear(data.split("/")[2]);
											bill_freq.getBillingCyclePeriod();
											
											
											if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
										    	queryString = "SELECT TO_CHAR(TO_DATE(ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY') FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? AND CARGO_NO = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, agmt_no);
//										    	stmt.setString(2, agmt_rev);
										    	stmt.setString(2, cont_no);
//										    	stmt.setString(4, cont_rev);
										    	stmt.setString(3, cont_type);
										    	stmt.setString(4, cd);
										    	stmt.setString(5, cargo_no);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		billing_eff_dt = rset.getString(1);
										    	}
										    	
										    	rset.close();
										    	stmt.close();
										    	
											}
											
											else if(cont_ref.startsWith("L") || cont_ref.startsWith("X") || !cont_ref.startsWith("R")) {
										    	queryString = "SELECT TO_CHAR(END_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, agmt_no);
										    	stmt.setString(2, agmt_rev);
										    	stmt.setString(3, cont_no);
										    	stmt.setString(4, cont_rev);
										    	stmt.setString(5, cont_type);
										    	stmt.setString(6, cd);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		billing_eff_dt = rset.getString(1);
										    	}
										    	
										    	rset.close();
										    	stmt.close();
										    	
											}
										
											int isGreater=utilDate.getDays(billing_eff_dt, bill_freq.getPeriod_end_dt());
											
											if(isGreater<=0)
											{
												data=billing_eff_dt;
											}
											else {
												data = bill_freq.getPeriod_end_dt();
											}
											
//											data = data + " 00:00:00";
//											if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
//												System.out.println(agmt_no+"=="+agmt_no+"=="+cont_no+"=="+cont_rev+"=="+cd+"==="+cont_ref);
//												System.out.println(billing_eff_dt+"===="+freq+"=="+data+"==="+bill_freq.getPeriod_end_dt());
//											}

//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
										}
								    	else if(i == 24) {
								    		name = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				//				    		if (name!=null) {
				//								name = name.substring(1, name.length() - 1);
				//							}
								    		exchg_cd="";
											queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, name);
									    	rset = stmt.executeQuery();
									    	if (rset.next()) {
									    		exchg_cd = rset.getString(1);
									    	}
									    	else {
									    		exchg_cd = "0";
									    	}
									    	rset.close();
									    	stmt.close();
									    	data = exchg_cd;
								    	}
								    	else if(i == 30) {
								    		
								    		String temp_struct = "";
								    		dt = "";
											name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@ ", ", ");;
								    
								    		if (!name.contains(", ")) {
												queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? AND PAY_RECV = 'R' ";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, name);
												rset = stmt.executeQuery();
												if (rset.next()) {
													temp_struct = rset.getString(1);
													dt = rset.getString(2);
												}
												rset.close();
												stmt.close();
											}
								    		else {

												int flag = 0;
												queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? AND PAY_RECV = 'R' ";
												for (int j = 1; j < name.split(", ").length; j++) {
													queryString += " AND DESCR LIKE ? ";
												}
												stmt = conn.prepareStatement(queryString);
												for (int j = 0; j < name.split(", ").length; j++) {
													stmt.setString(j+1, "%"+name.split(", ")[j]+"%");
												}
												rset = stmt.executeQuery();
												
												while (rset.next()) {
													
													for (int j = 0; j < name.split(", ").length; j++) {
														if (rset.getString(2).contains(name.split(", ")[j]) && rset.getString(2).split(", ").length == name.split(", ").length) {
															flag = 1;
															break;
														}
														else {
															flag = 0;
														}
													}
													
													if (flag == 1) {
														temp_struct = rset.getString(1);
														name=rset.getString(2);
														dt = rset.getString(3);
														break;
													}
												}
												rset.close();
												stmt.close();
											}
										    	data = temp_struct;
//										    	System.out.println(name+"::"+temp_struct+"=="+tax_struct_cd);
										    	if (!temp_struct.equals(tax_struct_cd) && !temp_struct.equals("")) {
											    	stmt1.setString(1, temp_struct);	
											    	tax_struct_cd = temp_struct;
										    	}
										    	else {
										    		tax_struct_cd = "0";
											    	stmt1.setString(1, tax_struct_cd);	
										    	}
//										    	System.out.println("1-"+data);
								    	}
								    	else if(i == 31) {
								    		data = dt;
								    	}
								    	else if(i == 78) {
								    		queryString = "SELECT TAX_STRUCT_CD  "
													+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TCS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
											stmt = conn.prepareStatement(queryString);
											stmt.setString(1, cd);
											stmt.setString(2, "C");
											stmt.setString(3,company_cd);
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
								    	else if(i == 80) {
								    		queryString = "SELECT TAX_STRUCT_CD  "
													+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TDS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
											stmt = conn.prepareStatement(queryString);
											stmt.setString(1, cd);
											stmt.setString(2, "C");
											stmt.setString(3,company_cd);
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
								    	else if(i == 86) {
//								    		
//								    			
								    		data  = cargo_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	
								    	else {			
								    		
								    		
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    	if (i == 17 || i == 18) {

//										    	System.out.println(index+"-"+data.split(" ")[0]);
										    	stmt1.setString(index++, data.split(" ")[0]);	
									    	}
								    	}	
//								    	System.out.println(index+"-"+data);
//								    	stmt1.setString(index++, data);		    	
								    }
									
								    queryString = "SELECT COUNTERPARTY_CD FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								    		+ "AND FINANCIAL_YEAR = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT =? "
								    		+ "AND BU_STATE_TIN = ?  AND PLANT_SEQ = ? AND INVOICE_SEQ = ? AND CARGO_NO = ?";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, cd);
							    	stmt.setString(3, fin_yr);
							    	stmt.setString(4, agmt_no);
							    	stmt.setString(5, agmt_rev);
							    	stmt.setString(6, cont_no);
							    	stmt.setString(7, cont_rev);
							    	stmt.setString(8, cont_type);
							    	stmt.setString(9, bu_seq_no);
							    	stmt.setString(10, state_code);
							    	stmt.setString(11, plant_seq_no);
							    	stmt.setInt(12, inv_seq_no);
							    	stmt.setString(13, cargo_no);
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !tax_struct_cd.equals("0") && inv_seq_no != 0) {
											//System.out.println(queryString1);
											
											logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," + tax_struct_cd  + "," ), conn, "");
											
											stmt1.executeUpdate();
											stmt1.close();
											
											logger_count++;
									}
							    	 else {
											stmt1.close();
											skipped_count++;     
											logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," + tax_struct_cd + "," ), conn, "E");
											
											if (invseq.containsKey(financial_year)) {
												inv_seq_no = invseq.get(financial_year);
												inv_seq_no=inv_seq_no-1;
												invseq.put(financial_year, inv_seq_no);
											}
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
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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

public void FMS_INVOICE_MST_GROSSAMT_UPDATE() throws IOException, SQLException {
	
	function_nm="FMS_INVOICE_MST_GROSSAMT_UPDATE()";
	try {
		

		DateUtil utilDate = new DateUtil();
		DataBean_Sales_Invoice bill_freq = new DataBean_Sales_Invoice();
		
		table_name = "FMS_INVOICE_MST_GROSSAMT_UPDATE";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		
		// Update query for updating TAX_STRUCT_CD for LTCORA contracts (contract_type Q and O) 
		queryString1 = "UPDATE FMS_INVOICE_MST SET SALE_AMT = ?, GROSS_AMT = ? "
				+ " WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND FINANCIAL_YEAR = ? AND AGMT_NO = ? "
				+ " AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT = ? "
				+ " AND BU_STATE_TIN = ? AND PLANT_SEQ = ? AND INVOICE_SEQ = ? AND INVOICE_NO = ? AND "
				+ " PERIOD_START_DT = TO_DATE(?, 'DD/MM/YYYY') AND PERIOD_END_DT = TO_DATE(?, 'DD/MM/YYYY') AND CARGO_NO = ? AND SALE_AMT = 0 AND GROSS_AMT = 0 ";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		Map<String, Integer> invseq = new HashMap<String, Integer>();
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_TAXCD_UPDATE_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
//			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_TAXCD_UPDATE_"+start_end_dt+".xlsx"));
			String fileName = migration_setup_dir + "EXPORT/FMS_INVOICE_MST_TAXCD_UPDATE_"+start_end_dt+".csv";
			try (//				workbook = new XSSFWorkbook(file);
			//				sheet = workbook.getSheetAt(0);
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				//				if (rowIterator.hasNext()) {	// For skipping the first row
				//					rowIterator.next();
				//				}
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ_NO,CONTACT_PERSON_CD,SALE_AMT,GROSS_AMT,TIMESTAMP,", conn);
								int inv_seq_no=1;
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									String dt="",financial_year="",contact_person_cd="",mail="",billing_eff_dt = "", freq = "", sale_amt = "", gross_amt = "";
									agmt_no = ""; agmt_rev = ""; seq_no = "";
									abbr = "";
									cd = "0";
									data = null;
									
									index = 3;
									stmt1 = conn.prepareStatement(queryString1);
									
				//					row = rowIterator.next();
				//				    cellIterator = row.cellIterator();
				//				    while (cellIterator.hasNext()) {
									for (int i = 0; i < line.split(",").length; i++)
								    {	
				//				    	cell = cellIterator.next();
										data = null;
										
								    	if (i == 0) {
								    		
								    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				//				    		if (!abbr.equals("NULL")) {
				//								abbr = abbr.substring(1, abbr.length() - 1);
				//							}
								    		data = company_cd;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if (i == 1) {	// Counterparty_Cd
								    		
								    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
				//				    		if (!cont_ref.equals("NULL")) {
				//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
				//							}
								    		
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
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if(i == 2) {
								    		financial_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		data = financial_year;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
										else if (i == 3) { //Agmt_no
								    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//				    		if (agmt_no != null) {
				//								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
				//							}
								    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
								    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, cont_ref);
									    		stmt.setString(3, cont_ref.split("-")[0]);
									    		
									    		
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			agmt_no = rset.getString(1);
									    			agmt_rev = rset.getString(2);
									    			cont_no = rset.getString(3);
									    			cont_rev = rset.getString(4);
									    			cont_type = rset.getString(5);
									    			billing_eff_dt = rset.getString(6);
									    		} else {
									    			agmt_no = "";
									    			agmt_rev = "";
									    			cont_no = "";
									    			cont_rev = "";
									    			cont_type = "";
									    			billing_eff_dt = "";
									    		}
									    		rset.close();
									    		stmt.close();
								    		}
								    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
								    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, cont_ref);
									    		
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			agmt_no = rset.getString(1);
									    			agmt_rev = rset.getString(2);
									    			cont_no = rset.getString(3);
									    			cont_rev = rset.getString(4);
									    			cont_type = rset.getString(5);
									    			billing_eff_dt = rset.getString(6);
									    		} else {
									    			agmt_no = "";
									    			agmt_rev = "";
									    			cont_no = "";
									    			cont_rev = "";
									    			cont_type = "";
									    			billing_eff_dt = "";
									    		}
									    		rset.close();
									    		stmt.close();
								    		}
								    		
											data = agmt_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if (i == 4) { //Agmt_rev
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//					    		if (agmt_rev != null) {
				//									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
				//								}
								    		}
											data = agmt_rev;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if (i == 5) { //Cont_no
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//					    		if (cont_no != null) {
				//					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
				//								}
								    		}
								    		data = cont_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
							    		
								    	else if (i == 6) { //Cont_rev
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//					    		if (cont_rev != null) {
				//					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
				//								}	
								    		}
								    		data = cont_rev;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if (i == 7) { //contract_type
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//					    		if (cont_type != null) {
				//					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
				//								}	
								    		}
								    		data = cont_type;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    					    	
								    	else if(i == 8) { // BU_SEQ
								    		bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				//				    		if (bu_seq_no!=null) {
				//								bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
				//							}
											data  = bu_seq_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if(i ==9) {
								    		queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
								    				+ " AND COUNTERPARTY_CD='2' AND ENTITY='B' AND SEQ_NO = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_seq_no);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			bu_plant_state = rset.getString(1);
								    		}else {
								    			bu_plant_state  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_plant_state);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			state_code = rset.getString(1);
								    		}else {
								    			state_code  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		data = state_code;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    		
								    	}
								    	else if(i == 10) { //BU_CONTACT_PERSON

								    		if(cont_type.equals("O") || cont_type.equals("Q")) {
								    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
												
												queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, cd);
												stmt.setString(2, cargo_ref);
												rset = stmt.executeQuery();
												
												if (rset.next()) {
													cargo_no = rset.getString(1);
												}
												else{
													cargo_no="0";
												}
												rset.close();
												stmt.close();
								    		}else {
								    			cargo_no = "0";	
								    		}
								    		
								    		queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
								    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y' ORDER BY CONTACT_PERSON ";
											stmt=conn.prepareStatement(queryString);
											
											String addr_flag = "";
											if(bu_seq_no.equals("1")) {								
												addr_flag = "P1";
											}else if(bu_seq_no.equals("2")){
												addr_flag = "P2";
											}else if(bu_seq_no.equals("3")){
												addr_flag = "P3";
											}
				
											stmt.setString(1, addr_flag);
											rset = stmt.executeQuery();
											
								    		if (rset.next()) {				    			
								    			bu_cont_person_cd=rset.getString(1);
								    		}else {
								    			bu_cont_person_cd ="0";
								    		}	
								    		
								    		rset.close();
								    		stmt.close();
								    		data=bu_cont_person_cd;
								    	}
								    	else if (i == 11) {	//plant_seq
							    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
			//				    			if (inv_seq != null) {
			//				    				inv_seq = inv_seq.substring(1, inv_seq.length() - 1);
			//								}
							    			data = plant_seq_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	 
							    		}
								    	else if(i == 12)
								    	{
								    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
								    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
													
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, mail);
									    	stmt.setString(2, cd);
									    	rset = stmt.executeQuery();
									    	
									    	if (rset.next()) {
									    		contact_person_cd = rset.getString(1);
									    	}
									    	else {
									    		contact_person_cd = "1";
									    	}
									    	rset.close();
									    	stmt.close();
								    		data=contact_person_cd;
								    	}
								    	else if(i == 14) {
								    		String inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@", "/");
								    		queryString = "SELECT INVOICE_SEQ, TAX_STRUCT_CD FROM FMS_INVOICE_MST WHERE"
								    				+ " COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND FINANCIAL_YEAR = ? AND AGMT_NO = ? "
								    				+ " AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT = ? "
								    				+ " AND BU_STATE_TIN = ? AND PLANT_SEQ = ? AND INVOICE_NO = ? AND CARGO_NO = ? ";
								    		stmt = conn.prepareStatement(queryString);
								    		stmt.setString(1, company_cd);
								    		stmt.setString(2, cd);
								    		stmt.setString(3, financial_year);
								    		stmt.setString(4, agmt_no);
								    		stmt.setString(5, agmt_rev);
								    		stmt.setString(6, cont_no);
								    		stmt.setString(7, cont_rev);
								    		stmt.setString(8, cont_type);
								    		stmt.setString(9, bu_seq_no);
								    		stmt.setString(10, state_code);
								    		stmt.setString(11, plant_seq_no);
								    		stmt.setString(12, inv_no);
								    		stmt.setString(13, cargo_no);
								    		rset = stmt.executeQuery();
								    		
								    		if (rset.next()) {
								    			inv_seq_no = rset.getInt(1);
//								    			tax_struct_cd = rset.getString(2);
								    		}
								    		else {
								    			inv_seq_no = 0;
//								    			tax_struct_cd = "0";
								    		}
								    		rset.close();
								    		stmt.close();

//									    	System.out.println(index+"-"+inv_seq_no);
									    	stmt1.setInt(index++, inv_seq_no);	
								    		
								    		data = inv_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	else if (i == 16) {

									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    	freq = data;
									    	
								    	}
										else if (i == 17 && !freq.contains("0") && (cont_ref.startsWith("L") || cont_ref.startsWith("X") || cont_ref.startsWith("C") || cont_ref.startsWith("A") || !cont_ref.startsWith("R"))) {
											billing_eff_dt = "";
											data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										
											if (data != null) {
												data = data.split(" ")[0];
											}
											bill_freq.setBilling_cycle(freq);
											bill_freq.setMonth(data.split("/")[1]);
											bill_freq.setYear(data.split("/")[2]);
											bill_freq.getBillingCyclePeriod();
											
											if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
										    	queryString = "SELECT TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY') FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? AND CARGO_NO = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, agmt_no);
//										    	stmt.setString(2, agmt_rev);
										    	stmt.setString(2, cont_no);
//										    	stmt.setString(4, cont_rev);
										    	stmt.setString(3, cont_type);
										    	stmt.setString(4, cd);
										    	stmt.setString(5, cargo_no);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		billing_eff_dt = rset.getString(1);
										    	}
										    	
										    	rset.close();
										    	stmt.close();
										    	
											}
											
											else if(cont_ref.startsWith("L") || cont_ref.startsWith("X") || !cont_ref.startsWith("R")) {
										    	queryString = "SELECT TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, agmt_no);
										    	stmt.setString(2, agmt_rev);
										    	stmt.setString(3, cont_no);
										    	stmt.setString(4, cont_rev);
										    	stmt.setString(5, cont_type);
										    	stmt.setString(6, cd);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		billing_eff_dt = rset.getString(1);
										    	}
										    	
										    	rset.close();
										    	stmt.close();
										    	
											}
											
											if (data != null) {
												data = data.split(" ")[0];
											}
											int isGreater=utilDate.getDays(billing_eff_dt, bill_freq.getPeriod_start_dt());
											

											
											if(isGreater>1)
											{
												data=billing_eff_dt;
											}
											else {
												data = bill_freq.getPeriod_start_dt();
											}
											
//											data = data + " 00:00:00";
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
										//												System.out.println(freq+"=="+data);

//											if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
//												System.out.println(agmt_no+"=="+agmt_no+"=="+cont_no+"=="+cont_rev+"=="+cd+"==="+cont_ref);
//												System.out.println(billing_eff_dt+"===="+freq+"=="+data);
//											}
											
										}
										else if (i == 18 && !freq.contains("0") && (cont_ref.startsWith("L") || cont_ref.startsWith("X") || cont_ref.startsWith("C") || cont_ref.startsWith("A") || !cont_ref.startsWith("R"))) {
											billing_eff_dt = "";
											data = line.split(",")[i].contains("null") ? null : line.split(",")[i];

											if (data != null) {
												data = data.split(" ")[0];
											}
											bill_freq.setBilling_cycle(freq);
											bill_freq.setMonth(data.split("/")[1]);
											bill_freq.setYear(data.split("/")[2]);
											bill_freq.getBillingCyclePeriod();
											
											
											if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
										    	queryString = "SELECT TO_CHAR(TO_DATE(ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY') FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? AND CARGO_NO = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, agmt_no);
//										    	stmt.setString(2, agmt_rev);
										    	stmt.setString(2, cont_no);
//										    	stmt.setString(4, cont_rev);
										    	stmt.setString(3, cont_type);
										    	stmt.setString(4, cd);
										    	stmt.setString(5, cargo_no);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		billing_eff_dt = rset.getString(1);
										    	}
										    	
										    	rset.close();
										    	stmt.close();
										    	
											}
											
											else if(cont_ref.startsWith("L") || cont_ref.startsWith("X") || !cont_ref.startsWith("R")) {
										    	queryString = "SELECT TO_CHAR(END_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, agmt_no);
										    	stmt.setString(2, agmt_rev);
										    	stmt.setString(3, cont_no);
										    	stmt.setString(4, cont_rev);
										    	stmt.setString(5, cont_type);
										    	stmt.setString(6, cd);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		billing_eff_dt = rset.getString(1);
										    	}
										    	
										    	rset.close();
										    	stmt.close();
										    	
											}
										
											int isGreater=utilDate.getDays(billing_eff_dt, bill_freq.getPeriod_end_dt());
											
											if(isGreater<=0)
											{
												data=billing_eff_dt;
											}
											else {
												data = bill_freq.getPeriod_end_dt();
											}
											
//											data = data + " 00:00:00";
//											if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
//												System.out.println(agmt_no+"=="+agmt_no+"=="+cont_no+"=="+cont_rev+"=="+cd+"==="+cont_ref);
//												System.out.println(billing_eff_dt+"===="+freq+"=="+data+"==="+bill_freq.getPeriod_end_dt());
//											}

//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
										}
								    	// Update SALE_AMT and GROSS_AMT
										else if (i == 23) {
								    		data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		sale_amt = data;
											stmt1.setString(1, data);
										}
										else if (i == 28) {
											data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
											gross_amt = data;
											stmt1.setString(2, data);
										}
								    	
								    	else if(i == 24) {
								    		name = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				//				    		if (name!=null) {
				//								name = name.substring(1, name.length() - 1);
				//							}
								    		exchg_cd="";
											queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, name);
									    	rset = stmt.executeQuery();
									    	if (rset.next()) {
									    		exchg_cd = rset.getString(1);
									    	}
									    	else {
									    		exchg_cd = "0";
									    	}
									    	rset.close();
									    	stmt.close();
									    	data = exchg_cd;
								    	}
								    	else if(i == 30) {
								    		
								    		String temp_struct = "";
								    		dt = "";
											name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@ ", ", ");;
								    
								    		if (!name.contains(", ")) {
												queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? AND PAY_RECV = 'R' ";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, name);
												rset = stmt.executeQuery();
												if (rset.next()) {
													temp_struct = rset.getString(1);
													dt = rset.getString(2);
												}
												rset.close();
												stmt.close();
											}
								    		else {

												int flag = 0;
												queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? AND PAY_RECV = 'R' ";
												for (int j = 1; j < name.split(", ").length; j++) {
													queryString += " AND DESCR LIKE ? ";
												}
												stmt = conn.prepareStatement(queryString);
												for (int j = 0; j < name.split(", ").length; j++) {
													stmt.setString(j+1, "%"+name.split(", ")[j]+"%");
												}
												rset = stmt.executeQuery();
												
												while (rset.next()) {
													
													for (int j = 0; j < name.split(", ").length; j++) {
														if (rset.getString(2).contains(name.split(", ")[j])) {
															flag = 1;
														}
														else {
															flag = 0;
															break;
														}
													}
													
													if (flag == 1) {
														temp_struct = rset.getString(1);
														name=rset.getString(2);
														dt = rset.getString(3);
														break;
													}
												}
												rset.close();
												stmt.close();
											}
										    	data = temp_struct;
								    	}
								    	else if(i == 31) {
								    		data = dt;
								    	}
								    	else if(i == 78) {
								    		queryString = "SELECT TAX_STRUCT_CD  "
													+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TCS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
											stmt = conn.prepareStatement(queryString);
											stmt.setString(1, cd);
											stmt.setString(2, "C");
											stmt.setString(3,company_cd);
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
								    	else if(i == 80) {
								    		queryString = "SELECT TAX_STRUCT_CD  "
													+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TDS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
											stmt = conn.prepareStatement(queryString);
											stmt.setString(1, cd);
											stmt.setString(2, "C");
											stmt.setString(3,company_cd);
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
								    	else if(i == 86) {
//								    		
//								    			
								    		data  = cargo_no;
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);	
								    	}
								    	
								    	else {			
								    		
								    		
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    	if (i == 17 || i == 18) {

//										    	System.out.println(index+"-"+data.split(" ")[0]);
										    	stmt1.setString(index++, data.split(" ")[0]);	
									    	}
								    	}	
//								    	System.out.println(index+"-"+data);
//								    	stmt1.setString(index++, data);		    	
								    }
									
								    queryString = "SELECT COUNTERPARTY_CD FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								    		+ "AND FINANCIAL_YEAR = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT =? "
								    		+ "AND BU_STATE_TIN = ?  AND PLANT_SEQ = ? AND INVOICE_SEQ = ? AND CARGO_NO = ?";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, cd);
							    	stmt.setString(3, fin_yr);
							    	stmt.setString(4, agmt_no);
							    	stmt.setString(5, agmt_rev);
							    	stmt.setString(6, cont_no);
							    	stmt.setString(7, cont_rev);
							    	stmt.setString(8, cont_type);
							    	stmt.setString(9, bu_seq_no);
							    	stmt.setString(10, state_code);
							    	stmt.setString(11, plant_seq_no);
							    	stmt.setInt(12, inv_seq_no);
							    	stmt.setString(13, cargo_no);
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !gross_amt.equals("0") && !sale_amt.equals("0") && inv_seq_no != 0) {
											//System.out.println(queryString1);
											
											
											if (stmt1.executeUpdate() > 0) {
												logger_count++;
												logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," + sale_amt  + "," + gross_amt  + "," ), conn, "");
											}
											else {
												skipped_count++; 
												logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," + sale_amt + "," + gross_amt  + "," ), conn, "E");
											}
											stmt1.close();
											
											
									}
							    	 else {
											stmt1.close();
											skipped_count++; 
											logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," + sale_amt + "," + gross_amt  + "," ), conn, "E");
											
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
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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

public void FMS_INV_TAX_DTL_UPDATE() throws IOException, SQLException {

	function_nm="FMS_INV_TAX_DTL_UPDATE()";
	try {
		
		
		System.out.println("<<START>><<FMS_INV_TAX_DTL_UPDATE>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_INV_TAX_DTL_UPDATE>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		
		queryString = "DELETE FROM FMS_INV_TAX_DTL WHERE COMPANY_CD = '2' ";
		stmt = conn.prepareStatement(queryString);
		stmt.executeUpdate();
		stmt.close();
		conn.commit();
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss'),"
				+ "NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
				+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),GROSS_AMT"
				+ "	FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? "
				+ " AND TAX_AMT != 0";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,"
				+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(5);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			
			int count_desc=1;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					
					stmt1 = conn.prepareStatement(queryString1);
					
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
//								else {
//									desc = null;
//								}
							
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
//							        else {
							            gross_amt = rset.getString(15);
							            tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100;
							            adv_amt = tax_amt + "";
							            adv=adv_amt;
//							        }
							    }
							    else if (desc.contains("on")) {

							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
							        tax_amt = (Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100;
							           
							    }
							}
							data = tax_amt + "";

						}
						
						else if(i == 9 && desc != null && desc.contains("on")) {

							data = adv;
						}
						
						stmt1.setString(i+1,data);
							
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_INV_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(5));
			stmt5.setString(6, tax_code);
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4)+ "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4)+ "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
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
		
	
		System.out.println("<<END>><<FMS_INV_TAX_DTL_UPDATE>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_INV_TAX_DTL_UPDATE>>,", conn);
		
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

	
	
	public void FMS_INVOICE_DTL() throws IOException, SQLException {
		
		function_nm="FMS_INVOICE_DTL()";
		try {
			
			table_name = "FMS_INVOICE_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			queryString1 = "INSERT INTO FMS_INVOICE_DTL (COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "BU_STATE_TIN,PLANT_SEQ,INVOICE_SEQ,ALLOCATION_DT,DAILY_QTY,SALE_PRICE,SALE_PRICE_UNIT,AMT_INR,AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_VALUE,"
					+ "ENT_BY,ENT_DT,FLAG,MODIFY_BY,MODIFY_DT,CARGO_NO) "
			        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, ?, ?, ?, "
			        + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_INVOICE_DTL_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_INVOICE_DTL_"+start_end_dt+".csv";
				try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_DTL_"+start_end_dt+".xlsx"));
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					//				if (rowIterator.hasNext()) {	// For skipping the first row
					//					rowIterator.next();
					//				}
									String line = br.readLine();
									
									logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,PLANT_SEQ_NO,INVOICE_SEQ,ALLOCATION_DT,CARGO_NO,TIMESTAMP,", conn);
									
									while ((line = br.readLine()) != null) {
										total_count++; 
										agmt_no = ""; agmt_rev = ""; seq_no = "";
										abbr = "";
										cd = "0";
										data = null;
										
										index = 1;
										stmt1 = conn.prepareStatement(queryString1);
										
					//					row = rowIterator.next();
					//				    cellIterator = row.cellIterator();
					//				    while (cellIterator.hasNext()) {
										for (int i = 0; i < line.split(",").length; i++)
									    {	
					//				    	cell = cellIterator.next();
											data = null;
											
									    	if (i == 0) {
									    		
									    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!abbr.equals("NULL")) {
					//								abbr = abbr.substring(1, abbr.length() - 1);
					//							}
												data = company_cd;
									    	}
									    	else if (i == 1) {	// Counterparty_Cd
									    		
									    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
					//				    		if (!cont_ref.equals("NULL")) {
					//								cont_ref = cont_ref.substring(1, cont_ref.length() - 1);
					//							}
									    		
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
											else if (i == 3) { //Agmt_no
									    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//				    		if (agmt_no != null) {
					//								agmt_no = agmt_no.substring(1, agmt_no.length() - 1);
					//							}
									    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, cont_ref);
										    		stmt.setString(3, cont_ref.split("-")[0]);
										    		
										    		
										    		rset = stmt.executeQuery();
										    		if (rset.next()) {
										    			agmt_no = rset.getString(1);
										    			agmt_rev = rset.getString(2);
										    			cont_no = rset.getString(3);
										    			cont_rev = rset.getString(4);
										    			cont_type = rset.getString(5);
										    		} else {
										    			agmt_no = "";
										    			agmt_rev = "";
										    			cont_no = "";
										    			cont_rev = "";
										    			cont_type = "";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, cont_ref);
										    		
										    		rset = stmt.executeQuery();
										    		if (rset.next()) {
										    			agmt_no = rset.getString(1);
										    			agmt_rev = rset.getString(2);
										    			cont_no = rset.getString(3);
										    			cont_rev = rset.getString(4);
										    			cont_type = rset.getString(5);
										    		} else {
										    			agmt_no = "";
										    			agmt_rev = "";
										    			cont_no = "";
										    			cont_rev = "";
										    			cont_type = "";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		
												data = agmt_no;
									    	}
									    	else if (i == 4) { //Agmt_rev
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (agmt_rev != null) {
					//									agmt_rev = agmt_rev.substring(1, agmt_rev.length() - 1);
					//								}
									    		}
												data = agmt_rev;
									    	}
									    	else if (i == 5) { //Cont_no
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (cont_no != null) {
					//					    			cont_no = cont_no.substring(1, cont_no.length() - 1);
					//								}
									    		}
									    		data = cont_no;
									    	}
								    		
									    	else if (i == 6) { //Cont_rev
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (cont_rev != null) {
					//					    			cont_rev = cont_rev.substring(1, cont_rev.length() - 1);
					//								}	
									    		}
									    		data = cont_rev;
									    	}
									    	else if (i == 7) { //contract_type
									    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
										    		cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
					//					    		if (cont_type != null) {
					//					    			cont_type = cont_type.substring(1, cont_type.length() - 1);
					//								}	
									    		}
									    		data = cont_type;
									    	}
									    					    	
									    	else if(i == 8) { // BU_SEQ
									    		alloc_dt = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
									    		
									    		queryString = "SELECT BU_UNIT FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
									    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, agmt_no);
									    		stmt.setString(3, agmt_rev);
									    		stmt.setString(4, cont_no);
									    		stmt.setString(5, cont_rev);
									    		stmt.setString(6, cont_type);
									    		rset = stmt.executeQuery();
									    		
									    		if (rset.next()) {
									    			bu_seq_no = rset.getString(1);
									    		}else {
									    			bu_seq_no = "";
									    		}
					
									    		rset.close();
									    		stmt.close();		    		
									    		data  = bu_seq_no;
									    	}
									    	else if(i ==9) {
									    		cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
									    		
									    		if(cont_type.equals("O") || cont_type.equals("Q")) {
															
															queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
															stmt = conn.prepareStatement(queryString);
															stmt.setString(1, cd);
															stmt.setString(2, cargo_ref);
															rset = stmt.executeQuery();
															
															if (rset.next()) {
																cargo_no = rset.getString(1);
															}
															else{
																cargo_no="0";
															}
															rset.close();
															stmt.close();
											    	} else {
											    			cargo_no = "0";	
											    	}
									    		
									    		queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
									    				+ " AND COUNTERPARTY_CD='2' AND ENTITY='B' AND SEQ_NO = ?";
												stmt=conn.prepareStatement(queryString);
												stmt.setString(1, bu_seq_no);
												rset = stmt.executeQuery();
									    		if (rset.next()) {				    			
									    			bu_plant_state = rset.getString(1);
									    		}else {
									    			bu_plant_state  ="0";
									    		}	
									    		rset.close();
									    		stmt.close();
									    		
									    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
												stmt=conn.prepareStatement(queryString);
												stmt.setString(1, bu_plant_state);
												rset = stmt.executeQuery();
									    		if (rset.next()) {				    			
									    			state_code = rset.getString(1);
									    		}else {
									    			state_code  ="0";
									    		}	
									    		rset.close();
									    		stmt.close();
									    		
									    		data = state_code;
									    		
									    	}
									    	else if(i == 10) { // PLANT_SEQ
									    		plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		data = plant_seq_no; 
									    	}
									    	else if(i == 11) { // INVOICE_SEQ
									    		String new_inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		if(new_inv_seq!=null) {
									    			new_inv_seq = new_inv_seq.split("\\(")[0];	
									    		}
									    		
									    		queryString = "SELECT INVOICE_SEQ, BU_UNIT, BU_STATE_TIN FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND FINANCIAL_YEAR = ? "
									    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND PLANT_SEQ = ? AND CARGO_NO = ? " ;
									    		if (new_inv_seq != null) {
									    			queryString += " AND INVOICE_NO = ? ";
									    		}
									    		else {
									    			queryString += " AND INVOICE_NO IS NULL ";
									    		}
									    		if (!alloc_dt.equals("")) {
									    			queryString += " AND PERIOD_START_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND PERIOD_END_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ";
									    		}
									    		int index = 1;
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(index++, cd);
									    		stmt.setString(index++, fin_yr);
									    		stmt.setString(index++, agmt_no);
									    		stmt.setString(index++, agmt_rev);
									    		stmt.setString(index++, cont_no);
									    		stmt.setString(index++, cont_rev);
									    		stmt.setString(index++, cont_type);
//									    		stmt.setString(index++, bu_seq_no);
//									    		stmt.setString(index++, state_code);
									    		stmt.setString(index++, plant_seq_no);
									    		stmt.setString(index++, cargo_no);
									    		if (new_inv_seq != null) {
										    		stmt.setString(index++, new_inv_seq);
									    		}
									    		if (!alloc_dt.equals("")) {
									    			stmt.setString(index++, alloc_dt);
									    			stmt.setString(index++, alloc_dt);
									    		}
									    		rset = stmt.executeQuery();
									    		
									    		if (rset.next()) {
									    			inv_seq = rset.getString(1);
									    			bu_seq_no = rset.getString(2);
									    			state_code = rset.getString(3);
									    		}else {
									    			inv_seq = "0";
									    			bu_seq_no = "0";
									    			state_code = "0";
//									    			System.out.println(cd+"=="+fin_yr+"=="+agmt_no+"=="+agmt_rev+"=="+cont_no+"=="+cont_rev+"=="+cont_type+"=="+plant_seq_no+"=="+cargo_no+"=="+new_inv_seq+"=="+alloc_dt);
									    		}
									    		rset.close();
									    		stmt.close();	
									    		data  = inv_seq;
									    		
									    		stmt1.setString(9, bu_seq_no);
									    		stmt1.setString(10, state_code);
									    		
									    			
									    		
									    	}
									    	else if(i == 14) { // SALE_PRICE
									    		
									    		queryString = "SELECT SALE_PRICE FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
									    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, agmt_no);
									    		stmt.setString(3, agmt_rev);
									    		stmt.setString(4, cont_no);
									    		stmt.setString(5, cont_rev);
									    		stmt.setString(6, cont_type);
									    		rset = stmt.executeQuery();
									    		
									    		if (rset.next()) {
									    			sale_price = rset.getString(1);
									    		}else {
									    			sale_price = "";
									    		}
									    		rset.close();
									    		stmt.close();	
									    		data  = sale_price;
									    	}
									    	else if(i == 15) { // SALE_PRICE_UNIT
									    		queryString = "SELECT SALE_PRICE_UNIT FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
									    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, agmt_no);
									    		stmt.setString(3, agmt_rev);
									    		stmt.setString(4, cont_no);
									    		stmt.setString(5, cont_rev);
									    		stmt.setString(6, cont_type);
									    		rset = stmt.executeQuery();
									    		
									    		if (rset.next()) {
									    			sale_price_unit = rset.getString(1);
									    		}else {
									    			sale_price_unit = "";
									    		}
									    		rset.close();
									    		stmt.close();	
									    		data  = sale_price_unit;
									    	}
									    	else if(i == 18) {
									    		name = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    		if (name!=null) {
					//								name = name.substring(1, name.length() - 1);
					//							}
									    		exchg_cd="";
												queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, name);
										    	rset = stmt.executeQuery();
										    	if (rset.next()) {
										    		exchg_cd = rset.getString(1);
										    	}
										    	else {
										    		exchg_cd = "0";
										    	}
										    	rset.close();
										    	stmt.close();
										    	data = exchg_cd;
									    	}
									    	else if(i == 25) {
									    		
										    	if(cont_type.equals("O") || cont_type.equals("Q")) {
//										    	cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
														
														queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
														stmt = conn.prepareStatement(queryString);
														stmt.setString(1, cd);
														stmt.setString(2, cargo_ref);
														rset = stmt.executeQuery();
														
														if (rset.next()) {
															cargo_no = rset.getString(1);
														}
														else{
															cargo_no="0";
														}
														rset.close();
														stmt.close();
										    	} else {
										    			cargo_no = "0";	
										    	}
										    	data  = cargo_no;
									    	}
									    	else {			    	
									    		if (i == 2) {	//fin_yr
									    			fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    			if (fin_yr != null) {
					//				    				fin_yr = fin_yr.substring(1, fin_yr.length() - 1);
					//								}
									    		}
									    		
//									    		if (i == 9) {	//state_tin
//									    			state_code = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//				    			if (state_code != null) {
//					//				    				state_code = state_code.substring(1, state_code.length() - 1);
//					//								}
//									    		}
//									    		
//									    		if (i == 11) {	//invoice_seq
//									    			inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//				    			if (inv_seq != null) {
//					//				    				inv_seq = inv_seq.substring(1, inv_seq.length() - 1);
//					//								}
//									    		}
									    		if (i == 12) {	//alloc_dt
									    			alloc_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    			if (alloc_dt != null) {
					//				    				alloc_dt = alloc_dt.substring(1, alloc_dt.length() - 1);
					//								}
									    		}
									    		
									    		
										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
									    	}	
									    	//System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);			    	
									    }
										
									    queryString = "SELECT COUNTERPARTY_CD FROM FMS_INVOICE_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
									    		+ "AND FINANCIAL_YEAR = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND BU_UNIT =? "
									    		+ "AND BU_STATE_TIN = ?  AND PLANT_SEQ = ? AND INVOICE_SEQ = ? AND ALLOCATION_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND CARGO_NO = ? ";
								    	stmt = conn.prepareStatement(queryString);
								    	stmt.setString(1, company_cd);
								    	stmt.setString(2, cd);
								    	stmt.setString(3, fin_yr);
								    	stmt.setString(4, agmt_no);
								    	stmt.setString(5, agmt_rev);
								    	stmt.setString(6, cont_no);
								    	stmt.setString(7, cont_rev);
								    	stmt.setString(8, cont_type);
								    	stmt.setString(9, bu_seq_no);
								    	stmt.setString(10, state_code);
								    	stmt.setString(11, plant_seq_no);
								    	stmt.setString(12, inv_seq);
								    	stmt.setString(13, alloc_dt);
								    	stmt.setString(14, cargo_no);
								    	
								    	rset = stmt.executeQuery();
								    	
								    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !bu_seq_no.equals("") ) {
												//System.out.println(queryString1);
												
												logger.data(fname, (company_cd+"," + cd + " , " + fin_yr + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + ","+ bu_seq_no + " , " + state_code + "," + plant_seq_no + "," + inv_seq + "," + alloc_dt + "," + cargo_no + ","), conn, "");
												
												stmt1.executeUpdate();
												stmt1.close();
												
												logger_count++;
										}
								    	 else {
												stmt1.close();
												skipped_count++;     
												logger.data(fname, (company_cd+"," + cd + " , " + fin_yr + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + ","+ bu_seq_no + " , " + state_code + "," + plant_seq_no + "," + inv_seq + "," + alloc_dt + "," + cargo_no + ","), conn, "E");
										}
								    	 
								    	 rset.close();
										 stmt.close();
									}
				}
				
				// Below block of code is for unique SEIPL data
//				rowIterator = sheet.iterator();
				
				msg = "Data has been Inserted Successfully in Database.";
				msg_type = "S";		
		
			}
			
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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
	
	public void FMS_INVOICE_IRN_DTL() throws IOException, SQLException {

		function_nm="FMS_INVOICE_IRN_DTL()";
		try {
			
			System.out.println("<<START>><<FMS_INVOICE_IRN_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_INVOICE_IRN_DTL>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   

			queryString1 = "INSERT INTO FMS_INVOICE_IRN_DTL(COMPANY_CD,INVOICE_NO,IRN_NO,XLS_FILE_NM,SIGN_QR_CODE,ENT_BY,ENT_DT) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			stmt1 = conn.prepareStatement(queryString1);
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_INVOICE_IRN_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {
				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_IRN_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for inserting SEIPL data
				rowIterator = sheet.iterator();
				rowIterator.next();
				

				logger.checkpoint(fname, "COMPANY_CD,INVOICE_NO,IRN_NO,TIMESTAMP,", conn);

				while (rowIterator.hasNext()) {
					total_count++;  
					
						data = "";
						String irn_no="";
						seq_no="";
						
						index = 1;
						stmt1 = conn.prepareStatement(queryString1);
						
						row = rowIterator.next();
						cellIterator = row.cellIterator(); 
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							if (cell.getColumnIndex() == 1) {
								seq_no = cell.getStringCellValue();
								seq_no = seq_no.substring(1, seq_no.length()-1);
							}
							if (cell.getColumnIndex() == 2) {
								irn_no = cell.getStringCellValue();
								irn_no = irn_no.substring(1, irn_no.length()-1);
							}
							
							
							data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
							if(data != null) {
						    	data = data.substring(1, data.length()-1);
						    }
							stmt1.setString(index++, data);
						}
						
						queryString = "SELECT INVOICE_NO FROM FMS_INVOICE_IRN_DTL WHERE COMPANY_CD = ? AND INVOICE_NO = ? AND IRN_NO = ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.setString(2, seq_no);
						stmt.setString(3, irn_no);
						
						rset = stmt.executeQuery();
						
						if (!rset.next()) {
							//System.out.println(queryString1);
							
							logger.data(fname, (company_cd + "," + seq_no + "," + irn_no + ","), conn, "");
							
							stmt1.executeUpdate();
							stmt1.close();
							
							logger_count++;
						}
						else {
							stmt1.close();
							
							skipped_count++;     
							logger.data(fname, (company_cd + "," + cd + "," + eff_dt + ","), conn, "E");
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
			//conn.commit();
			
			System.out.println("<<END>><<FMS_INVOICE_IRN_DTL>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_INVOICE_IRN_DTL>>,,", conn);
			
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
	
	public void FMS_INV_TAX_DTL() throws IOException, SQLException {

		function_nm="FMS_INV_TAX_DTL()";
		try {
			
			
			System.out.println("<<START>><<FMS_INV_TAX_DTL>>");
			
			logger.checkpoint(fname, "\n<<START>>,<<FMS_INV_TAX_DTL>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0;   
			String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
			
//			String inv_type = "";
//			String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//			String[] tax_dtls = new String[5];
			
			columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
					+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			
			queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss'),"
					+ "NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
					+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),GROSS_AMT"
					+ "	FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1,company_cd);
			rset = stmt.executeQuery();
			
			queryString1 = "INSERT INTO FMS_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,"
					+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
			
			
			logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
			
			while (rset.next()) {
				tax_struct_cd = rset.getString(5);
				String desc1="";
				String count_value="",adv_amt="",gross_amt="";
				
				int count_desc=1;
				adv_amt = "";
				
				for(int j=0;j<count_desc;j++) {
					
					queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, tax_struct_cd);
					rset2 = stmt2.executeQuery();
					
					if(rset2.next()) {
						
						stmt1 = conn.prepareStatement(queryString1);
						
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
//									else {
//										desc = null;
//									}
								
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
//								        else {
								            gross_amt = rset.getString(15);
								            tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100);
								            adv_amt = tax_amt + "";
								            adv=adv_amt;
//								        }
								    }
								    else if (desc.contains("on")) {

								        count_value = desc.split("%")[0];
								        count_value = count_value.split(" ")[1]; 
								        tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100);
								           
								    }
								}
								data = tax_amt + "";

							}
							
							else if(i == 9 && desc != null && desc.contains("on")) {

								data = adv;
							}
							
							stmt1.setString(i+1,data);
								
							}
					
					
				//for data already exists..
				queryString5 = "SELECT TAX_AMT FROM FMS_INV_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
						+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, company_cd);
				stmt5.setString(2, rset.getString(2));
				stmt5.setString(3, rset.getString(3));
				stmt5.setString(4, rset.getString(4));
				stmt5.setString(5, rset.getString(5));
				stmt5.setString(6, tax_code);
				rset5 = stmt5.executeQuery();
				
					if (!rset5.next() ) {
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						logger_count++;
					}
					else {
						
						stmt1.close();
						skipped_count++; 
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
						
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
			
		
			System.out.println("<<END>><<FMS_INV_TAX_DTL>>");
			System.out.println();
		
			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<FMS_INV_TAX_DTL>>,", conn);
			
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
	
	public void FMS_INV_STORAGE_CRG_DTL() throws IOException, SQLException {

		function_nm="FMS_INV_STORAGE_CRG_DTL()";
		try {
			
			table_name = "FMS_INV_STORAGE_CRG_DTL";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 

			queryString1 = "INSERT INTO FMS_INV_STORAGE_CRG_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,STORAGE_DT,OPEN_BALANCE_QTY,OFFTAKE_QTY,RATE,RATE_TYPE,DAY_DISCOUNT)"
					+ "VALUES(?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString1);
			
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_INV_STORAGE_CRG_DTL_"+start_end_dt+".xlsx");
			if(file1.exists()) {

				
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INV_STORAGE_CRG_DTL_"+start_end_dt+".xlsx"));

				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				
				// Below block of code is for unique SEIPL data
				rowIterator = sheet.iterator();
				if (rowIterator.hasNext()) {	// For skipping the first row
					rowIterator.next();
				}

				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,STORAGE_DT,TIMESTAMP,", conn);
				
				while (rowIterator.hasNext()) {
					total_count++;
					agmt_no = ""; agmt_rev = ""; agmt_type="";
					abbr = "";
					cd = "0";
					data = null;
			    	String strg_dt="",cont_ref_id="";
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					row = rowIterator.next();
				    cellIterator = row.cellIterator();
				    while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						data = null;
						
				    	if (cell.getColumnIndex() == 0) {	// Company Code 
				    		cont_ref_id = (cell.getStringCellValue().contains("'null'") ? "NULL" : cell.getStringCellValue());
				    		if (!cont_ref_id.equals("NULL")) {
				    			cont_ref = cont_ref_id.substring(1, cont_ref_id.lastIndexOf("-"));
							}
							data = company_cd;
				    	}
				    	else if(cell.getColumnIndex() == 1) {
				    		
				    		strg_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
			    			if (strg_dt != null) {
			    				strg_dt = strg_dt.substring(1, strg_dt.length() - 1);
							}else {
								strg_dt="";
							}
				    		
				    		if (!cont_ref_id.equals("NULL")) {
				    			String[] parts = cont_ref_id.split("-");
				    		    cargo_no = parts[parts.length - 1].replaceAll("'","");  
							}
				    		
				    		queryString = "SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV FROM FMS_LTCORA_CONT_MST WHERE CONT_REF_NO = ? AND COMPANY_CD = '2' ";
				    		stmt = conn.prepareStatement(queryString);
				    		stmt.setString(1, cont_ref);
				    		rset = stmt.executeQuery();
				    		if (rset.next()) {
				    			cd = rset.getString(1);
				    			agmt_no = rset.getString(2);
				    			agmt_rev = rset.getString(3);
				    			cont_type = rset.getString(4);
				    			cont_no = rset.getString(5);
				    			cont_rev = rset.getString(6);
				    		}
				    		else {
				    			cd = "";
				    			agmt_no = "";
				    			agmt_rev = "";
				    			cont_type = "";
				    			cont_no = "";
				    			cont_rev = "";
				    		}				    		
				    		rset.close();
				    		stmt.close();
				    		
				    		queryString2 = "SELECT INVOICE_SEQ, BU_STATE_TIN FROM FMS_INVOICE_MST WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? "
				    				+ "AND AGMT_REV = ? AND CONTRACT_TYPE = ? AND CONT_NO = ? AND CONT_REV = ? AND COMPANY_CD = '2' AND INV_FLAG = 'ST' AND CARGO_NO = ? ";
				    		
				    		if (!strg_dt.equals("")) {
				    			queryString2 += " AND PERIOD_START_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND PERIOD_END_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') ";
				    		}
				    		stmt2 = conn.prepareStatement(queryString2);
				    		stmt2.setString(1, cd);
				    		stmt2.setString(2, agmt_no);
				    		stmt2.setString(3, agmt_rev);
				    		stmt2.setString(4, cont_type);
				    		stmt2.setString(5, cont_no);
				    		stmt2.setString(6, cont_rev);
				    		stmt2.setString(7, cargo_no);
				    		if (!strg_dt.equals("")) {
				    			stmt2.setString(8, strg_dt);
				    			stmt2.setString(9, strg_dt);
				    		}
				    		rset2 = stmt2.executeQuery();
				    		if (rset2.next()) {
				    			inv_seq = rset2.getString(1);
				    			state_code = rset2.getString(2);
				    		}
				    		else {
				    			inv_seq = "0";
				    			state_code = "0";
				    		}				    		
				    		rset2.close();
				    		stmt2.close();
				    		
				    		data = state_code;
				    	}
				    	else if(cell.getColumnIndex() == 2) {
				    		data = inv_seq;
				    	}
				    	else {
				    		
				    		if (cell.getColumnIndex() == 3) {	// fin_yr
				    			fin_yr = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (fin_yr != null) {
				    				fin_yr = fin_yr.substring(1, fin_yr.length() - 1);
								}else {
									fin_yr="";
								}
				    		}
				    		
				    		if (cell.getColumnIndex() == 4) {	// strg_dt
				    			strg_dt = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
				    			if (strg_dt != null) {
				    				strg_dt = strg_dt.substring(1, strg_dt.length() - 1);
								}else {
									strg_dt="";
								}
				    		}
				    		
					    	data = cell.getStringCellValue().contains("null") ? null : cell.getStringCellValue();
					    	if(data != null) {
					    		data = data.substring(1, data.length()-1);
					    	}
				    	}	
				    	stmt1.setString(index++, data);
				    }
				     
			    	queryString = "SELECT COMPANY_CD FROM FMS_INV_STORAGE_CRG_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? AND FINANCIAL_YEAR = ? AND STORAGE_DT = TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') AND INVOICE_SEQ = ? ";
			    	stmt = conn.prepareStatement(queryString);
			    	stmt.setString(1, company_cd);
			    	stmt.setString(2, state_code);
			    	stmt.setString(3, fin_yr);
	    			stmt.setString(4, strg_dt);
	    			stmt.setString(5, inv_seq);
			    	rset = stmt.executeQuery();
			    	

				    if (!rset.next()) {
						//System.out.println(queryString1);
						
						logger.data(fname, (company_cd+"," + state_code + " , " + inv_seq + "," + fin_yr + "," + strg_dt + ","), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd+"," + state_code + " , " + inv_seq + "," + fin_yr + "," + strg_dt + ","), conn, "E");
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
			//conn.commit();
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
	
public void FMS_FFLOW_INV_MST() throws IOException, SQLException {
		
		function_nm="FMS_FFLOW_INV_MST()";
		try {
			

			DateUtil utilDate = new DateUtil();
			DataBean_Sales_Invoice bill_freq = new DataBean_Sales_Invoice();
			
			table_name = "FMS_FFLOW_INV_MST";
			System.out.println("<<START>><<"+table_name+">>");
			
			logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
			
			data = "";
			logger_count = 0;   
			skipped_count = 0;   
			total_count = 0; 
			
			queryString1 = "INSERT INTO FMS_FFLOW_INV_MST(COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,"
					+ "BU_STATE_TIN,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,INVOICE_DT,INVOICE_CATEGORY,FREQ,PERIOD_START_DT,"
					+ "PERIOD_END_DT,DUE_DT,INVOICE_TYPE,LINKED_INVOICE,NUM_LINE,NOTE,GROSS_AMT_USD,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,"
					+ "GROSS_AMT_INR,TAX_AMT,TAX_STRUCT_CD,TAX_EFF_DT,INVOICE_AMT,ADJUST_SIGN,ADJUST_AMT,NET_PAYABLE_AMT,INV_STATUS,CHECKED_FLAG,CHECKED_BY,"
					+ "CHECKED_DT,APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,OTHER_INV_STR,AMT_WORD,PDF_INV_DTL,PRINT_BY_ORI,"
					+ "PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP,PRINT_DT_DUP,INVOICE_ID_SEQ,TDS_STRUCT_CD,TDS_EFF_DT,TCS_STRUCT_CD,TCS_EFF_DT,"
					+ "SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,PAY_RECV_AMT,PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT,PAY_REMARK,"
					+ "TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,TCS_FACTOR,TCS_TDS,TCS_AMT,ALLOC_QTY,"
					+ "SUB_INV_TYPE,CARGO_NO,FIN_SYS,HOLD_AMT)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,"
					+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
					+ "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			stmt1 = conn.prepareStatement(queryString1);
			Map<String, Integer> invseq = new HashMap<String, Integer>();
			file1 = new File(migration_setup_dir + "EXPORT/FMS_FFLOW_INV_MST_"+start_end_dt+".csv");
			
			if(file1.exists()) {
				
				String fileName = migration_setup_dir + "EXPORT/FMS_FFLOW_INV_MST_"+start_end_dt+".csv";
				
				try (
				 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
									String line = br.readLine();
									
									logger.checkpoint(fname, "COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,INVOICE_TYPE,INVOICE_NO,TIMESTAMP,", conn);
									int inv_seq_no=1;
									
									while ((line = br.readLine()) != null) {
										total_count++; 
										String dt="",financial_year="",contact_person_cd="",mail="",billing_eff_dt = "", freq = "", start_dt = "", end_dt = "";
										String addr_flag = "", inv_type="",tds_per="",inv_no="",dr_cr="",p_seq="";
										agmt_no = ""; agmt_rev = ""; seq_no = "";
										abbr = "";
										cd = "0";
										data = null;
										
										index = 1;
										stmt1 = conn.prepareStatement(queryString1);
										
										for (int i = 0; i < line.split(",").length; i++)
									    {	
											data = null;
											
									    	if (i == 0) {
									    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
									    		data = company_cd;
									    	}
									    	else if(i == 1) {
									    		financial_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		data = financial_year;
									    	}
									    	else if (i == 2) {	// Counterparty_Cd
									    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);

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
											else if (i == 3) { //Agmt_no
									    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		
									    		 if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
									    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
										    		stmt = conn.prepareStatement(queryString);
										    		stmt.setString(1, cd);
										    		stmt.setString(2, cont_ref);
										    		
										    		rset = stmt.executeQuery();
										    		if (rset.next()) {
										    			agmt_no = rset.getString(1);
										    			agmt_rev = rset.getString(2);
										    			cont_no = rset.getString(3);
										    			cont_rev = rset.getString(4);
										    			cont_type = rset.getString(5);
										    			billing_eff_dt = rset.getString(6);
										    		} else {
										    			agmt_no = "";
										    			agmt_rev = "";
										    			cont_no = "";
										    			cont_rev = "";
										    			cont_type = "";
										    			billing_eff_dt = "";
										    		}
										    		rset.close();
										    		stmt.close();
									    		}
									    		 else if(cont_ref.startsWith("L")) {
										    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
											    		stmt = conn.prepareStatement(queryString);
											    		stmt.setString(1, cd);
											    		stmt.setString(2, cont_ref);
											    		
											    		rset = stmt.executeQuery();
											    		if (rset.next()) {
											    			agmt_no = rset.getString(1);
											    			agmt_rev = rset.getString(2);
											    			cont_no = rset.getString(3);
											    			cont_rev = rset.getString(4);
											    			cont_type = rset.getString(5);
											    			billing_eff_dt = rset.getString(6);
											    		} else {
											    			agmt_no = "";
											    			agmt_rev = "";
											    			cont_no = "";
											    			cont_rev = "";
											    			cont_type = "";
											    			billing_eff_dt = "";
											    		}
											    		rset.close();
											    		stmt.close();
										    		}
												data = agmt_no;
									    	}
									    	else if (i == 4) { //Agmt_rev
									    		if(!cont_ref.startsWith("C") && !cont_ref.startsWith("A")&& !cont_ref.startsWith("L")) {
									    			agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		}
												data = agmt_rev;
									    	}
									    	else if (i == 5) { //Cont_no
									    		if(!cont_ref.startsWith("C") && !cont_ref.startsWith("A")&& !cont_ref.startsWith("L")) {
									    			cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		}
									    		data = cont_no;
									    	}
								    		
									    	else if (i == 6) { //Cont_rev
									    		if(!cont_ref.startsWith("C") && !cont_ref.startsWith("A")&& !cont_ref.startsWith("L")) {
									    			cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		}
									    		data = cont_rev;
									    	}
									    	else if (i == 7) { //contract_type
									    		if(!cont_ref.startsWith("C") && !cont_ref.startsWith("A")&& !cont_ref.startsWith("L")) {
									    			cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
									    		}
									    		data = cont_type;
									    	}
									    					    	
									    	else if(i == 8) { // BU_SEQ
									    		bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    		if (bu_seq_no!=null) {
					//								bu_seq_no = bu_seq_no.substring(1, bu_seq_no.length() - 1);
					//							}
												data  = bu_seq_no;
									    	}
									    	else if(i ==9) {
									    		queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
									    				+ " AND COUNTERPARTY_CD='2' AND ENTITY='B' AND SEQ_NO = ?";
												stmt=conn.prepareStatement(queryString);
												stmt.setString(1, bu_seq_no);
												rset = stmt.executeQuery();
									    		if (rset.next()) {				    			
									    			bu_plant_state = rset.getString(1);
									    		}else {
									    			bu_plant_state  ="0";
									    		}	
									    		rset.close();
									    		stmt.close();
									    		
									    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
												stmt=conn.prepareStatement(queryString);
												stmt.setString(1, bu_plant_state);
												rset = stmt.executeQuery();
									    		if (rset.next()) {				    			
									    			state_code = rset.getString(1);
									    		}else {
									    			state_code  ="0";
									    		}	
									    		rset.close();
									    		stmt.close();
									    		
									    		data = state_code;
									    		
									    	}
									    	else if(i == 10) { //BU_CONTACT_PERSON

									    		dr_cr = line.split(",")[i].contains("null") ? null : line.split(",")[i];	
//									    		cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//													
//													queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
//													stmt = conn.prepareStatement(queryString);
//													stmt.setString(1, cd);
//													stmt.setString(2, cargo_ref);
//													rset = stmt.executeQuery();
//													
//													if (rset.next()) {
//														cargo_no = rset.getString(1);
//													}
//													else{
//														cargo_no="0";
//													}
//													rset.close();
//													stmt.close();
									    		
									    		queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
									    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y' ORDER BY CONTACT_PERSON ";
												stmt=conn.prepareStatement(queryString);
												
												
												if(bu_seq_no.equals("1")) {								
													addr_flag = "P1";
												}else if(bu_seq_no.equals("2")){
													addr_flag = "P2";
												}else if(bu_seq_no.equals("3")){
													addr_flag = "P3";
												}
					
												stmt.setString(1, addr_flag);
												rset = stmt.executeQuery();
												
									    		if (rset.next()) {				    			
									    			bu_cont_person_cd=rset.getString(1);
									    		}else {
									    			bu_cont_person_cd ="0";
									    		}	
									    		
									    		rset.close();
									    		stmt.close();
									    		data=bu_cont_person_cd;
									    	}
									    	else if (i == 11) {	//addr_flag
									    		p_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		
									    		if(dr_cr!=null) {
									    		if((dr_cr.equals("dr") || dr_cr.equals("cr")) ){
									    		queryString="SELECT ADDR_FLAG FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD= ? "
									    				+ "AND ENTITY='C' AND SEQ_NO =? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y' ";
												stmt=conn.prepareStatement(queryString);
                                                   stmt.setString(1,cd);
                                                   stmt.setString(2, p_seq.substring(1));
                                                   rset = stmt.executeQuery();
                                                   if(rset.next()) {
                                                	   p_seq = rset.getString(1);
                                                   }
                                                   rset.close();
                                                   stmt.close();
                                                   data = p_seq; 
									    		}
									    		}
									    		else {
								    			data = addr_flag; 
									    		}
								    		}
									    	
									    	else if(i == 12)
									    	{
									    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		
									    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
														
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, mail);
										    	stmt.setString(2, cd);
										    	rset = stmt.executeQuery();
										    	
										    	if (rset.next()) {
										    		contact_person_cd = rset.getString(1);
										    	}
										    	else {
										    		contact_person_cd = "0";
										    	}
										    	rset.close();
										    	stmt.close();
									    		data=contact_person_cd;
									    	}
									    	else if(i == 13) {
									    		if (invseq.containsKey(financial_year)) {
									    			
									    			inv_seq_no=invseq.get(financial_year);
									    			inv_seq_no=inv_seq_no+1;
													invseq.put(financial_year,inv_seq_no);
													
												} else {
													inv_seq_no=1;
													invseq.put(financial_year,inv_seq_no);
													
												}
									    		data=inv_seq_no+"";
									    	}
									    	
									    	else if(i==14) {
									    		inv_no =line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		data = inv_no;
									    	}
									    	else if (i == 17) {

										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
										    	freq = data;
										    	
									    	}
//											else if (i == 18 && !freq.contains("0") && (cont_ref.startsWith("L") || cont_ref.startsWith("X") || cont_ref.startsWith("C") || cont_ref.startsWith("A") || !cont_ref.startsWith("R"))) {
//												billing_eff_dt = "";
//												data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//											
//												if (data != null) {
//													data = data.split(" ")[0];
//												}
//												bill_freq.setBilling_cycle(freq);
//												bill_freq.setMonth(data.split("/")[1]);
//												bill_freq.setYear(data.split("/")[2]);
//												bill_freq.getBillingCyclePeriod();
//												
//												if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
//											    	queryString = "SELECT TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY') FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? AND CARGO_NO = ? ";
//											    	stmt = conn.prepareStatement(queryString);
//											    	stmt.setString(1, agmt_no);
////											    	stmt.setString(2, agmt_rev);
//											    	stmt.setString(2, cont_no);
////											    	stmt.setString(4, cont_rev);
//											    	stmt.setString(3, cont_type);
//											    	stmt.setString(4, cd);
//											    	stmt.setString(5, cargo_no);
//											    	rset = stmt.executeQuery();
//											    	
//											    	if (rset.next()) {
//											    		billing_eff_dt = rset.getString(1);
//											    	}
//											    	
//											    	rset.close();
//											    	stmt.close();
//											    	
//												}
//												
//												else if(cont_ref.startsWith("L") || cont_ref.startsWith("X") || !cont_ref.startsWith("R")) {
//											    	queryString = "SELECT TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? ";
//											    	stmt = conn.prepareStatement(queryString);
//											    	stmt.setString(1, agmt_no);
//											    	stmt.setString(2, agmt_rev);
//											    	stmt.setString(3, cont_no);
//											    	stmt.setString(4, cont_rev);
//											    	stmt.setString(5, cont_type);
//											    	stmt.setString(6, cd);
//											    	rset = stmt.executeQuery();
//											    	
//											    	if (rset.next()) {
//											    		billing_eff_dt = rset.getString(1);
//											    	}
//											    	
//											    	rset.close();
//											    	stmt.close();
//											    	
//												}
//												
//												if (data != null) {
//													data = data.split(" ")[0];
//												}
//												int isGreater=utilDate.getDays(billing_eff_dt, bill_freq.getPeriod_start_dt());
//												
//
//												
//												if(isGreater>1)
//												{
//													data=billing_eff_dt;
//												}
//												else {
//													data = bill_freq.getPeriod_start_dt();
//												}
//												
//												data = data + " 00:00:00";
//											//												System.out.println(freq+"=="+data);
//
////												if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
////													System.out.println(agmt_no+"=="+agmt_no+"=="+cont_no+"=="+cont_rev+"=="+cd+"==="+cont_ref);
////													System.out.println(billing_eff_dt+"===="+freq+"=="+data);
////												}
//												
//											}
//											else if (i == 19 && !freq.contains("0") && (cont_ref.startsWith("L") || cont_ref.startsWith("X") || cont_ref.startsWith("C") || cont_ref.startsWith("A") || !cont_ref.startsWith("R"))) {
//												billing_eff_dt = "";
//												data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//
//												if (data != null) {
//													data = data.split(" ")[0];
//												}
//												bill_freq.setBilling_cycle(freq);
//												bill_freq.setMonth(data.split("/")[1]);
//												bill_freq.setYear(data.split("/")[2]);
//												bill_freq.getBillingCyclePeriod();
//												
//												
//												if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
//											    	queryString = "SELECT TO_CHAR(TO_DATE(ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY') FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? AND CARGO_NO = ? ";
//											    	stmt = conn.prepareStatement(queryString);
//											    	stmt.setString(1, agmt_no);
////											    	stmt.setString(2, agmt_rev);
//											    	stmt.setString(2, cont_no);
////											    	stmt.setString(4, cont_rev);
//											    	stmt.setString(3, cont_type);
//											    	stmt.setString(4, cd);
//											    	stmt.setString(5, cargo_no);
//											    	rset = stmt.executeQuery();
//											    	
//											    	if (rset.next()) {
//											    		billing_eff_dt = rset.getString(1);
//											    	}
//											    	
//											    	rset.close();
//											    	stmt.close();
//											    	
//												}
//												
//												else if(cont_ref.startsWith("L") || cont_ref.startsWith("X") || !cont_ref.startsWith("R")) {
//											    	queryString = "SELECT TO_CHAR(END_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND COUNTERPARTY_CD = ? ";
//											    	stmt = conn.prepareStatement(queryString);
//											    	stmt.setString(1, agmt_no);
//											    	stmt.setString(2, agmt_rev);
//											    	stmt.setString(3, cont_no);
//											    	stmt.setString(4, cont_rev);
//											    	stmt.setString(5, cont_type);
//											    	stmt.setString(6, cd);
//											    	rset = stmt.executeQuery();
//											    	
//											    	if (rset.next()) {
//											    		billing_eff_dt = rset.getString(1);
//											    	}
//											    	
//											    	rset.close();
//											    	stmt.close();
//											    	
//												}
//											
//												int isGreater=utilDate.getDays(billing_eff_dt, bill_freq.getPeriod_end_dt());
//												
//												if(isGreater<=0)
//												{
//													data=billing_eff_dt;
//												}
//												else {
//													data = bill_freq.getPeriod_end_dt();
//												}
//												
//												data = data + " 00:00:00";
////												if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
////													System.out.println(agmt_no+"=="+agmt_no+"=="+cont_no+"=="+cont_rev+"=="+cd+"==="+cont_ref);
////													System.out.println(billing_eff_dt+"===="+freq+"=="+data+"==="+bill_freq.getPeriod_end_dt());
////												}
//												
//											}
									    	else if(i == 26) {
									    		name = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    		if (name!=null) {
					//								name = name.substring(1, name.length() - 1);
					//							}
									    		exchg_cd="";
												queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
										    	stmt = conn.prepareStatement(queryString);
										    	stmt.setString(1, name);
										    	rset = stmt.executeQuery();
										    	if (rset.next()) {
										    		exchg_cd = rset.getString(1);
										    	}
										    	else {
										    		exchg_cd = "0";
										    	}
										    	rset.close();
										    	stmt.close();
										    	data = exchg_cd;
									    	}
									    	else if(i == 32) {
									    		
									    		String temp_struct = "";
									    		dt = "";
												name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@ ", ", ");
									    
												if(name!= null) {
													if (!name.contains(", ")) {
														queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
														stmt = conn.prepareStatement(queryString);
														stmt.setString(1, name);
														rset = stmt.executeQuery();
														if (rset.next()) {
															temp_struct = rset.getString(1);
															dt = rset.getString(2);
														}
														rset.close();
														stmt.close();
													}
										    		else {

														int flag = 0;
														queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
														stmt = conn.prepareStatement(queryString);
														stmt.setString(1, "%"+name.split(", ")[0]+"%");
														rset = stmt.executeQuery();
														
														while (rset.next()) {
															
															for (int j = 0; j < name.split(", ").length; j++) {
																if (rset.getString(2).contains(name.split(", ")[j])) {
																	flag = 1;
																}
																else {
																	flag = 0;
																	break;
																}
															}
															
															if (flag == 1) {
																temp_struct = rset.getString(1);
																name=rset.getString(2);
																dt = rset.getString(3);
																break;
															}
														}
														rset.close();
														stmt.close();
													}
												}
									    		
											   	data = temp_struct;
									    	}
									    	else if(i == 33) {
									    		data = dt;
									    	}
//									    	else if(i == 78) {
//									    		queryString = "SELECT TAX_STRUCT_CD  "
//														+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TCS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
//												stmt = conn.prepareStatement(queryString);
//												stmt.setString(1, cd);
//												stmt.setString(2, "C");
//												stmt.setString(3,company_cd);
//												rset = stmt.executeQuery();
//												if(rset.next()) {
//													data = rset.getString(1);
//												}
//												else {
//													data = null;
//												}
//												rset.close();
//												stmt.close();
//									    	}
//									    	else if(i == 80) {
//									    		queryString = "SELECT TAX_STRUCT_CD  "
//														+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TDS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
//												stmt = conn.prepareStatement(queryString);
//												stmt.setString(1, cd);
//												stmt.setString(2, "C");
//												stmt.setString(3,company_cd);
//												rset = stmt.executeQuery();
//												if(rset.next()) {
//													data = rset.getString(1);
//												}
//												else {
//													data = null;
//												}
//												rset.close();
//												stmt.close();
//									    	}
									    	else if(i == 59) {
									    		String tds_desc = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		if(tds_desc!=null) {
									    			queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1,tds_desc);
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
									    	}
									    	else if(i == 73) {
									    		tds_per = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		data = tds_per;
									    	}
									    	else if(i == 80) {
									    		
									    		if(tds_per!=null) {
									    			data = "TDS";
									    		}else {
													data = "NA";
									    		}
									    	}
									    	else if(i == 84) {
									    		if(cont_type.equals("O") || cont_type.equals("Q")) {
//									    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
													
													queryString = "SELECT CARGO_NO FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND INVOICE_DT >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND INVOICE_DT <= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND CONTRACT_TYPE = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, cd);
													stmt.setString(2, start_dt);
													stmt.setString(3, end_dt);
													stmt.setString(4, cont_type);
													rset = stmt.executeQuery();
													
													if (rset.next()) {
														cargo_no = rset.getString(1);
													}
													else{
														cargo_no="0";
													}
													rset.close();
													stmt.close();
									    		}else {
									    			cargo_no = "0";	
									    		}
									    		data  = cargo_no;
									    	}
									    	
									    	else {			    	
//									    		if (i == 2) {	//fin_yr
//									    			fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//				    			if (fin_yr != null) {
//					//				    				fin_yr = fin_yr.substring(1, fin_yr.length() - 1);
//					//								}
//									    		}
									    		
									    		if (i == 21) {	//invoice_type
									    			inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//				    			if (inv_seq != null) {
					//				    				inv_seq = inv_seq.substring(1, inv_seq.length() - 1);
					//								}
									    		}
									    		if(i == 18) {
									    			start_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}
									    		if(i == 19) {
									    			end_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}
//									    		if(i == 75) {
//									    			tds_per = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//									    		}
									    		
//									    		if (i == 11) {	//plant_seq
//									    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//					//				    			if (inv_seq != null) {
//					//				    				inv_seq = inv_seq.substring(1, inv_seq.length() - 1);
//					//								}
//									    		}
									    		
									    		
										    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
					//					    	if(data != null) {
					//					    		data = data.substring(1, data.length()-1);
					//					    	}
									    	}	
//									    	System.out.println(index+"-"+data);
									    	stmt1.setString(index++, data);		    	
									    }
//										System.out.println();
									    queryString = "SELECT COUNTERPARTY_CD FROM FMS_FFLOW_INV_MST WHERE COMPANY_CD = ? "
									    		+ "AND FINANCIAL_YEAR = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ?";
								    	stmt = conn.prepareStatement(queryString);
								    	stmt.setString(1, company_cd);
								    	stmt.setString(2, financial_year);
								    	stmt.setString(3, state_code);
								    	stmt.setInt(4, inv_seq_no);
								    	stmt.setString(5, inv_type);
								    	
								    	rset = stmt.executeQuery();
								    	
								    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") ) {
												//System.out.println(queryString1);
												
												logger.data(fname, (company_cd+ "," + financial_year + "," + state_code + "," + inv_seq_no + "," + inv_type + ","+ inv_no+","), conn, "");
												stmt1.executeUpdate();
												stmt1.close();
												
												logger_count++;
										}
								    	 else {
												stmt1.close();
												skipped_count++;     
												logger.data(fname, (company_cd+ "," + financial_year + "," + state_code + "," + inv_seq_no + "," + inv_type + ","+inv_no+","), conn, "E");
												
												if (invseq.containsKey(financial_year)) {
													inv_seq_no = invseq.get(financial_year);
													inv_seq_no=inv_seq_no-1;
													invseq.put(financial_year, inv_seq_no);
												}
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
			
			System.out.println("<<END>><<"+table_name+">>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

			logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
			
			logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} 
		catch (Exception e)	{			
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


public void FMS_FFLOW_INV_MST_UPDATE() throws IOException, SQLException {
	
	function_nm="FMS_FFLOW_INV_MST_UPDATE()";
	try {
		

		
		table_name = "FMS_FFLOW_INV_MST_UPDATE";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		queryString1 = "UPDATE FMS_INVOICE_MST SET TDS_GROSS_PERCENT = ?, TDS_GROSS_AMT = ?, TDS_TAX_PERCENT = ?, TDS_TAX_AMT = ?, "
				+ " PAY_RECV_AMT = ? "
				+ "WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND INVOICE_ID_SEQ = ? "
				+ "AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? AND CARGO_NO = ? AND PLANT_SEQ = ? AND COMPANY_CD = '2' ";
		
		queryString1 = "UPDATE FMS_FFLOW_INV_MST SET TDS_GROSS_PERCENT = ?, TDS_GROSS_AMT = ?, TDS_TAX_PERCENT = ?, TDS_TAX_AMT = ?, PAY_RECV_AMT = ? "
				+ "WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND INVOICE_ID_SEQ = ? "
				+ "AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? " /*AND CARGO_NO = ?AND PLANT_SEQ = ? */ + " AND COMPANY_CD = '2' ";
		
		stmt1 = conn.prepareStatement(queryString1);
		file1 = new File(migration_setup_dir + "EXPORT/FMS_FFLOW_INV_MST_UPDATE_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
//			file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_"+start_end_dt+".xlsx"));
			String fileName = migration_setup_dir + "EXPORT/FMS_FFLOW_INV_MST_UPDATE_"+start_end_dt+".csv";
			try (//				workbook = new XSSFWorkbook(file);
			//				sheet = workbook.getSheetAt(0);
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				//				if (rowIterator.hasNext()) {	// For skipping the first row
				//					rowIterator.next();
				//				}
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,BU_UNIT,PLANT_SEQ,CONTRACT_TYPE,CARGO_NO,INVOICE_NO,FINANCIAL_YEAR,INVOICE_ID_SEQ,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT,PAY_RECV_AMT,TIMESTAMP,", conn);
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									String invoice_no = "", invoice_id_seq = "", tds_gross_percent = "", tds_tax_percent = "", tds_tax_amt = "", tds_gross_amt = "", plant_seq_no = "", pay_recv_amt = "";
									agmt_no = ""; agmt_rev = ""; seq_no = "";
									abbr = "";
									cd = "0";
									data = null;
									
									index = 1;
									stmt1 = conn.prepareStatement(queryString1);
									
									for (int i = 0; i < line.split(",").length; i++)
								    {	
										data = null;
										if (i == 0) {
								    		abbr = (line.split(",")[i].contains("null") ? "NULL" : line.split(",")[i]);
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
										
										else if (i == 1) {	//agmt_no
								    		
								    		cont_ref = (line.split(",")[i].contains("null") ? "NULL" : line.split(",")[i]);
								    		if(cont_ref.startsWith("L") || cont_ref.startsWith("X")) {
								    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ? AND CONTRACT_TYPE = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, cont_ref);
									    		stmt.setString(3, cont_ref.split("-")[0]);
									    		
									    		
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			agmt_no = rset.getString(1);
									    			agmt_rev = rset.getString(2);
									    			cont_no = rset.getString(3);
									    			cont_rev = rset.getString(4);
									    			cont_type = rset.getString(5);
									    		} else {
									    			agmt_no = "";
									    			agmt_rev = "";
									    			cont_no = "";
									    			cont_rev = "";
									    			cont_type = "";
									    		}
									    		rset.close();
									    		stmt.close();
									    		data = agmt_no;
								    		}
								    		else if(cont_ref.startsWith("C") || cont_ref.startsWith("A") || cont_ref.startsWith("R")) {
								    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, TO_CHAR(START_DT, 'DD/MM/YYYY') FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, cont_ref);
									    		
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			agmt_no = rset.getString(1);
									    			agmt_rev = rset.getString(2);
									    			cont_no = rset.getString(3);
									    			cont_rev = rset.getString(4);
									    			cont_type = rset.getString(5);
									    		} else {
									    			agmt_no = "";
									    			agmt_rev = "";
									    			cont_no = "";
									    			cont_rev = "";
									    			cont_type = "";
									    		}
									    		rset.close();
									    		stmt.close();
								    		}
								    		else {
								    			agmt_no = cont_ref;
								    		}
								    		data = agmt_no;
								    	}
										else if (i == 2) { //Agmt_rev
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		}
											data = agmt_rev;
								    	}
								    	else if (i == 3) { //Cont_no
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		}
								    		data = cont_no;
								    	}
							    		
								    	else if (i == 4) { //Cont_rev
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
									    		cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		}
								    		data = cont_rev;
								    	}
								    	else if(i == 5) { // BU_SEQ
								    		bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
											data  = bu_seq_no;
								    	}
//								    	else if(i == 8) { // cargo_no
//								    		if(cont_type.equals("O") || cont_type.equals("Q")) {
//								    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//												
//												queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
//												stmt = conn.prepareStatement(queryString);
//												stmt.setString(1, cd);
//												stmt.setString(2, cargo_ref);
//												rset = stmt.executeQuery();
//												
//												if (rset.next()) {
//													cargo_no = rset.getString(1);
//												}
//												else{
//													cargo_no="0";
//												}
//												rset.close();
//												stmt.close();
//								    		}else {
//								    			cargo_no = "0";	
//								    		}
//											data  = cargo_no;
//								    	}
								    	if (i == 13) {
//								    		queryString = "SELECT (NVL(GROSS_AMT_INR, 0) + NVL(TRANSPORTATION_AMOUNT, 0)) * ? / 100 FROM FMS_FFLOW_INV_MST "
//								    				+ "WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND INVOICE_ID_SEQ = ? AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? ";/*+" AND CARGO_NO = ?  AND PLANT_SEQ = ? ";*/
//								    		stmt = conn.prepareStatement(queryString);
//								    		stmt.setString(1, tds_gross_percent);
//								    		stmt.setString(2, cd);
//								    		stmt.setString(3, agmt_no);
//								    		stmt.setString(4, agmt_rev);
//								    		stmt.setString(5, cont_no);
//								    		stmt.setString(6, cont_rev);
//								    		stmt.setString(7, invoice_id_seq);
//								    		stmt.setString(8, fin_yr);
//								    		stmt.setString(9, cont_type);
////								    		stmt.setString(10, cargo_no);
////								    		stmt.setString(10, plant_seq_no);
//								    		rset = stmt.executeQuery();
//								    		
//								    		if (rset.next()) {
//								    			data = rset.getString(1) ;
//								    		}
//								    		else {
//								    			data = "0";
//								    		}
//								    		rset.close();
//								    		stmt.close();
//								    		
//							    			stmt1.setString(2, data);
//							    			tds_gross_amt = data;
							    			

									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							    			stmt1.setString(2, data);
							    			tds_gross_amt = data;
								    		
								    	}
								    	
								    	else {
								    		if (i == 6) {	// plant_seq_no
								    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		if (i == 7) {	// cont_type
								    			if(!cont_ref.startsWith("L") && !cont_ref.startsWith("X") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A") && !cont_ref.startsWith("R")) {
								    				cont_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
									    		}
								    		}
								    		if (i == 9) {	// invoice_no
								    			invoice_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
								    		if (i == 10) {	// fin_yr
								    			fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}			    	
								    		if (i == 11) {	// invoice_id_seq
								    			invoice_id_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}			    	
								    		if (i == 12) {	// tds_gross_percent
								    			tds_gross_percent = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    			stmt1.setString(1, tds_gross_percent);
								    		}
								    		if (i == 14) {	// tds_tax_percent
								    			tds_tax_percent = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    			stmt1.setString(3, tds_tax_percent);
								    		}			    	
								    		if (i == 15) {	// tds_tax_amt
								    			tds_tax_amt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    			stmt1.setString(4, tds_tax_amt);
								    		}			    	
								    		if (i == 16) {	// pay_recv_amt
								    			pay_recv_amt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
												stmt1.setString(5, pay_recv_amt);
								    		}
								    		
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    	}	
//								    	System.out.println(index+"-"+data);
//								    	stmt1.setString(index++, data);		    	
								    }
									
									stmt1.setString(6, cd);
					    			stmt1.setString(7, agmt_no);
					    			stmt1.setString(8, agmt_rev);
					    			stmt1.setString(9, cont_no);
					    			stmt1.setString(10, cont_rev);
					    			stmt1.setString(11, invoice_id_seq);
					    			stmt1.setString(12, fin_yr);
					    			stmt1.setString(13, cont_type);
//					    			stmt1.setString(13, cargo_no);
//					    			stmt1.setString(13, plant_seq_no);
											//System.out.println(queryString1);
									
											logger.data(fname, (company_cd+"," + cd + " , " + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + bu_seq_no + "," + plant_seq_no + "," + cont_type + "," + cargo_no + "," + invoice_no + " , " + fin_yr + "," + invoice_id_seq + "," + tds_gross_percent + "," + tds_gross_amt + "," + tds_tax_percent + "," + tds_tax_amt + "," + pay_recv_amt + ","), conn, "");
											
											stmt1.executeUpdate();
											stmt1.close();
											
											logger_count++;
									
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
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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


	
public void FMS_FFLOW_INV_DTL() throws IOException, SQLException {
	
	function_nm="FMS_FFLOW_INV_DTL()";
	try {
		
		table_name = "FMS_FFLOW_INV_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 

		String line_no="",inv_type="";
		
		queryString1 = "INSERT INTO FMS_FFLOW_INV_DTL (COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
				+ "BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_TYPE,INVOICE_SEQ,INVOICE_NO,LINE_NO,LINE_DESC,UNIT,"
				+ "QTY,RATE,AMOUNT,ENT_BY,ENT_DT,CARGO_NO) "
		        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
		        + "?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?)";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_FFLOW_INV_DTL_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_FFLOW_INV_DTL_"+start_end_dt+".csv";
			try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_DTL_"+start_end_dt+".xlsx"));
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				//				if (rowIterator.hasNext()) {	// For skipping the first row
				//					rowIterator.next();
				//				}
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_TYPE,INVOICE_SEQ,LINE_NO,TIMESTAMP,", conn);
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									agmt_no = ""; agmt_rev = ""; seq_no = "";
									abbr = "";
									cd = "0";
									data = null;
									
									index = 1;
									stmt1 = conn.prepareStatement(queryString1);
									for (int i = 0; i < line.split(",").length; i++)
								    {	
				//				    	cell = cellIterator.next();
										data = null;
										
								    	if (i == 0) {
								    		
								    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
											data = company_cd;
								    	}
								    	else if (i == 1) {	// Counterparty_Cd
								    		
								    		fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
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
								    		data = fin_yr;
								    	}
								    	else if (i == 2) {	//cd
								    		cont_ref = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		
											data = cd;
								    	}
										else if (i == 3) { //Agmt_no
											
								    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
				//				    		
								    		if(cont_ref.startsWith("C") || cont_ref.startsWith("A")) {
								    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, cont_ref);
									    		
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			agmt_no = rset.getString(1);
									    			agmt_rev = rset.getString(2);
									    			cont_no = rset.getString(3);
									    			cont_rev = rset.getString(4);
									    			cont_type = rset.getString(5);
									    		} else {
									    			agmt_no = "";
									    			agmt_rev = "";
									    			cont_no = "";
									    			cont_rev = "";
									    			cont_type = "";
									    		}
									    		rset.close();
									    		stmt.close();
								    		}
								    		
								    		else if(cont_ref.startsWith("L")) {
								    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND CONT_REF_NO = ?";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, cont_ref);
									    		
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			agmt_no = rset.getString(1);
									    			agmt_rev = rset.getString(2);
									    			cont_no = rset.getString(3);
									    			cont_rev = rset.getString(4);
									    			cont_type = rset.getString(5);
									    		} else {
									    			agmt_no = "";
									    			agmt_rev = "";
									    			cont_no = "";
									    			cont_rev = "";
									    			cont_type = "";
									    		}
									    		rset.close();
									    		stmt.close();
								    		}
											data = agmt_no;
								    	}
								    	else if (i == 4) { //Agmt_rev
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
									    		agmt_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		}
											data = agmt_rev;
								    	}
								    	else if (i == 5) { //Cont_no
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
								    			cont_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		}
								    		data = cont_no;
								    	}
								    	else if (i == 6) { //Cont_rev
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
								    			cont_rev = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		}
								    		data = cont_rev;
								    	}
								    	else if (i == 7) { //contract_type
								    		if(!cont_ref.startsWith("L") && !cont_ref.startsWith("C") && !cont_ref.startsWith("A")) {
								    			cont_type = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    		}
								    		data = cont_type;
								    	}
								    					    	
								    	else if(i == 8) { // BU_SEQ
								    		
								    		queryString = "SELECT BU_UNIT FROM FMS_FFLOW_INV_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
								    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
								    		stmt = conn.prepareStatement(queryString);
								    		stmt.setString(1, cd);
								    		stmt.setString(2, agmt_no);
								    		stmt.setString(3, agmt_rev);
								    		stmt.setString(4, cont_no);
								    		stmt.setString(5, cont_rev);
								    		stmt.setString(6, cont_type);
								    		rset = stmt.executeQuery();
								    		
								    		if (rset.next()) {
								    			bu_seq_no = rset.getString(1);
								    			
								    		}else {
								    			bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		}
				
								    		rset.close();
								    		stmt.close();		    		
								    		data  = bu_seq_no;
								    	}
								    	else if(i ==9) {
								    		
								    		queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
								    				+ " AND COUNTERPARTY_CD='2' AND ENTITY='B' AND SEQ_NO = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_seq_no);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			bu_plant_state = rset.getString(1);
								    		}else {
								    			bu_plant_state  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_plant_state);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			state_code = rset.getString(1);
								    		}else {
								    			state_code  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		data = state_code;
								    		
								    	}
								    	else if(i == 10) {
								    		new_inv = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
								    		queryString = "SELECT BU_CONTACT_PERSON_CD,ADDR_FLAG,CONTACT_PERSON_CD,INVOICE_SEQ,CARGO_NO FROM FMS_FFLOW_INV_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? "
								    				+ "AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND INVOICE_NO = ?";
								    		stmt = conn.prepareStatement(queryString);
								    		stmt.setString(1, cd);
								    		stmt.setString(2, agmt_no);
								    		stmt.setString(3, agmt_rev);
								    		stmt.setString(4, cont_no);
								    		stmt.setString(5, cont_rev);
								    		stmt.setString(6, cont_type);
								    		stmt.setString(7, new_inv);
								    		rset = stmt.executeQuery();
								    		
//								    			System.out.println(cd+"="+agmt_no+"="+agmt_rev+"="+cont_no+"="+cont_type+"="+new_inv);
								    		if (rset.next()) {
								    			bu_cont_person_cd =rset.getString(1);
								    			addr_flag = rset.getString(2);
								    			cont_cd = rset.getString(3);
								    			inv_seq = rset.getString(4);
								    			cargo_no = rset.getString(5);
								    			
								    			
								    		}else {
								    			bu_cont_person_cd ="0";
								    			addr_flag = null;
								    			cont_cd = null;
								    			inv_seq = null;
								    			cargo_no ="0";
								    		}
								    		rset.close();
								    		stmt.close();	
								    		data  = bu_cont_person_cd;
								    		
								    	}
								    	else if(i == 11) { // INVOICE_SEQ
								    		data  = addr_flag;
								    		
								    	}
								    	else if(i == 12) { // SALE_PRICE
								    		data  = cont_cd;
								    	}
								    	else if(i == 14) { // SALE_PRICE_UNIT
								    		data  = inv_seq;
								    	}
								    	else if(i == 24) { // SALE_PRICE_UNIT
								    		data  = cargo_no;
								    	}
								    	else {			    	
								    		if (i == 13) {	//alloc_dt
								    			inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				//				    			if (alloc_dt != null) {
				//				    				alloc_dt = alloc_dt.substring(1, alloc_dt.length() - 1);
				//								}
								    		}
								    		
								    		if (i == 16) {	//alloc_dt
								    			line_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				//				    			if (alloc_dt != null) {
				//				    				alloc_dt = alloc_dt.substring(1, alloc_dt.length() - 1);
				//								}
								    		}
								    		
								    		
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
				//					    	if(data != null) {
				//					    		data = data.substring(1, data.length()-1);
				//					    	}
								    	}	
//								    	System.out.println(index+"-"+data);
								    	stmt1.setString(index++, data);			    	
								    }
									
								    queryString = "SELECT COUNTERPARTY_CD FROM FMS_FFLOW_INV_DTL WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
								    		+ "AND BU_STATE_TIN = ?  AND INVOICE_TYPE = ? AND INVOICE_SEQ = ? AND LINE_NO = ? ";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, fin_yr);
							    	stmt.setString(3, state_code);
							    	stmt.setString(4, inv_type);
							    	stmt.setString(5, inv_seq);
							    	stmt.setString(6, line_no);
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && !bu_seq_no.equals("")) {
										//System.out.println(queryString1);
											
										logger.data(fname, (company_cd+"," + cd + " , " + fin_yr + " , " + state_code + "," + inv_type + "," + inv_seq + "," + line_no + ","), conn, "");
										
										stmt1.executeUpdate();
										stmt1.close();
										
										logger_count++;
									}
							    	 else {
										stmt1.close();
										skipped_count++;     
										logger.data(fname, (company_cd+"," + cd + " , " + fin_yr + " , " + state_code + "," + inv_type + "," + inv_seq + "," + line_no + ","), conn, "E");
									}
							    	 
							    	 rset.close();
									 stmt.close();
								}
			}
			
			// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";		
	
		}
		
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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

public void FMS_FFLOW_INV_TAX_DTL() throws IOException, SQLException {

	function_nm="FMS_FFLOW_INV_TAX_DTL()";
	try {
		
		
		System.out.println("<<START>><<FMS_FFLOW_INV_TAX_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_FFLOW_INV_TAX_DTL>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,NULL,TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss'),"
				+ "NULL,TAX_AMT,GROSS_AMT_INR,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
				+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),GROSS_AMT_INR"
				+ "	FROM FMS_FFLOW_INV_MST WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_FFLOW_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,"
				+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(6);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			
			int count_desc=1;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					
					stmt1 = conn.prepareStatement(queryString1);
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						if(i == 5) {	//TAX_STRUCT_CD
							if(tax_struct_cd != null) {
								tax_struct_cd = rset.getString(6);
							}
							else {
								tax_struct_cd = "0";
							}
							data = tax_struct_cd;
						}
						else if(i == 6) {
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
//								else {
//									desc = null;
//								}
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
						
						else if(i == 7) {	//TAX_EFF_DT
							data = eff_dt;
						}
						else if(i == 8) {	//TAX_DESCR
						   if(tax_struct_cd != null) {
								if(!tax_struct_cd.equals("0")) {
									data=desc;
								}
						   }
						    else {
									data = null;
							}
						}
						else if(i==9)
						{
							
							double tax_amt = 0.0;
							if (desc != null) {
							    if ( !desc.contains("on") ) {
							    	
							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
//							        else {
							            gross_amt = rset.getString(16);
							            if(count_value!=null && gross_amt!=null) {
							            tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100);
							            }
							            adv_amt = tax_amt + "";
							            adv=adv_amt;
//							        }
							    }
							    else if (desc.contains("on")) {

							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
							        tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100);
							           
							    }
							}
							data = tax_amt + "";

						}
						
						else if(i == 10 && desc != null && desc.contains("on")) {

							data = adv;
						}
						
//						System.out.println(i+1 +"=="+ data );
						stmt1.setString(i+1,data);
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_FFLOW_INV_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? AND INVOICE_TYPE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(6));
			stmt5.setString(6, tax_code);
			stmt5.setString(7, rset.getString(5));
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
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
		
	
		System.out.println("<<END>><<FMS_FFLOW_INV_TAX_DTL>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_FFLOW_INV_TAX_DTL>>,", conn);
		
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

public void FMS_INV_FILE_DTL() throws IOException, SQLException {
	function_nm="FMS_INV_FILE_DTL()";
	try {
		
		table_name = "FMS_INV_FILE_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
		
		queryString1 = "INSERT INTO FMS_INV_FILE_DTL(COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,"
				+ "PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT) "
				+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INV_FILE_DTL_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_INV_FILE_DTL_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	//file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_DAILY_BUYER_NOM_"+start_end_dt+".xlsx"));
				// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
//			if (rowIterator.hasNext()) {	// For skipping the first row
//				rowIterator.next();
//			}
				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,INVOICE_NO,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String new_inv="";
					abbr = "";
					bu_seq="";inv_seq="";fin_yr="";pdf_type="";file_nm="";sign_by="";sign_by_cd="";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++)
					{
//					cell = cellIterator.next();
						data = null;
						if(i == 0) {
							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							
							String[] parts = abbr.split("@");
							abbr=parts[0];
							cont_type=parts[1];
							
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
				    		
							data = company_cd;
						}
						else if(i == 1) {
							fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							data = fin_yr;
						}
						else if(i == 2) {
							inv_seq = line.split(",")[i];
							
							new_inv = inv_seq.split("@")[1];
							inv_seq = inv_seq.split("@")[0];
							
//							queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN FROM FMS_INVOICE_MST WHERE INVOICE_ID_SEQ = ? AND FINANCIAL_YEAR = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? ";
//							stmt = conn.prepareStatement(queryString);
//							stmt.setString(1, inv_seq);
//							stmt.setString(2, fin_yr);
//							stmt.setString(3, cd);
//							stmt.setString(4, cont_type);
//							
//							rset = stmt.executeQuery();
//							if (rset.next()) {
//								seq_no = rset.getString(1);
//								bu_seq = rset.getString(2);
//							} else {
//								seq_no ="0";
//								bu_seq ="0";
//							}
//							rset.close();
//							stmt.close();
							
							if(!new_inv.equals("null") && !new_inv.equals("")) {
								queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN FROM FMS_INVOICE_MST WHERE INVOICE_NO = ? AND FINANCIAL_YEAR = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = '2' ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, new_inv);
								stmt.setString(2, fin_yr);
								stmt.setString(3, cd);
								stmt.setString(4, cont_type);
								rset = stmt.executeQuery();
								if (rset.next()) {
									seq_no = rset.getString(1);
									bu_seq = rset.getString(2);
								} else {
									seq_no ="0";
									bu_seq ="0";
								}
								rset.close();
								stmt.close();
							}else {
								queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN FROM FMS_INVOICE_MST WHERE INVOICE_ID_SEQ = ? AND FINANCIAL_YEAR = ? AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = '2' ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, inv_seq);
								stmt.setString(2, fin_yr);
								stmt.setString(3, cd);
								stmt.setString(4, cont_type);
								rset = stmt.executeQuery();
								if (rset.next()) {
									seq_no = rset.getString(1);
									bu_seq = rset.getString(2);
								} else {
									seq_no ="0";
									bu_seq ="0";
								}
								rset.close();
								stmt.close();
							}
							
							data = bu_seq;
						}
						
						else if(i == 3) {
							data = seq_no;
							
//							
						}
						else if (i == 14) {	// SIGNED_ENT_BY
							if(sign_by!=null) {
								if(sign_by.contains("@")) {
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMAIL_ID) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sign_by.toUpperCase());
									rset = stmt.executeQuery();
									if (rset.next()) {
										sign_by_cd = rset.getString(1);
									} else {
										sign_by_cd ="0";
									}
									rset.close();
									stmt.close();
								}
								else {
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sign_by.toUpperCase());
									rset = stmt.executeQuery();
									if (rset.next()) {
										sign_by_cd = rset.getString(1);
									} else {
										sign_by_cd ="0";
									}
									rset.close();
									stmt.close();
								}
							}
							else {
								sign_by_cd = "0";
							}
							data = sign_by_cd;
						}
						
						
						else {
//							if(i == 1) {	//BU_UNIT
//								bu_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							}
//							else if(i == 2) {	//INVOICE_SEQ
//								inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							}
//							else if(i == 3) {	//FINANCIAL_YEAR
//								fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							}
							 if (i == 4) {	// PDF_TYPE
								pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 5) {	//FILE_NAME
								file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 11) {	//SIGNED_BY
								sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_INV_FILE_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND PDF_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, seq_no);
					stmt.setString(4, fin_yr);
					stmt.setString(5, pdf_type);
					rset = stmt.executeQuery();
					
					if (!rset.next()){
						
						logger.data(fname, (company_cd + "," + bu_seq + "," + seq_no + ","  + new_inv  + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + seq_no + "," + new_inv  + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
					}
					
					rset.close();
					stmt.close();
				}
				br.close();
			}
			
//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";				
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		//conn.commit();
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

public void FMS_INV_FILE_DTL_UPDATE() throws IOException, SQLException {

	function_nm="FMS_INV_FILE_DTL_UPDATE()";
	try {
		
		System.out.println("<<START>><<FMS_INV_FILE_DTL_UPDATE>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_INV_FILE_DTL_UPDATE>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		String file_nm = "",file_new="",pdf_type="",cont_desc="";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,PDF_SIGNED FROM FMS_INV_FILE_DTL WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP", conn);
	
		while (rset.next()) {
			total_count++;
			
			if(!rset.getString(6).contains("-SUG-") && !(rset.getString(6).contains("-O.pdf") || rset.getString(6).contains("-D.pdf") || rset.getString(6).contains("-T.pdf"))) {
				file_nm = rset.getString(6).split(".pdf")[0];
				file_nm += "-"+rset.getString(5)+".pdf"; 
			}
			else {
				file_nm = rset.getString(6);
			}
			
				if (file_nm!=null) {
					if(file_nm.contains("-S-") || file_nm.contains("-K-")) {
						cont_desc = "SALES_";
					}
					else if(file_nm.contains("-L-")) {
						cont_desc = "LOA_";
					}
					else if(file_nm.contains("-B-")) {
						cont_desc = "STORAGE_";
					}
					else if(file_nm.contains("-SUG-")) {
						cont_desc = "SUG_";
					}
					else if(file_nm.contains("-C-")) {
						cont_desc = "LTCORA_";
					}
//					file1 = new File(pdf_path+"sales_invoice/" + cont_desc + file_nm);
					if(rset.getString(7)!=null && rset.getString(7).equals("Y") && !file_nm.contains(cont_desc)) {
						file_new = cont_desc+file_nm;
						
						queryString1 = "UPDATE FMS_INV_FILE_DTL SET FILE_NAME = ? WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? "
								+ "AND FINANCIAL_YEAR = ? AND PDF_TYPE = ?";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1,file_new);
						stmt1.setString(2,rset.getString(1));
						stmt1.setString(3,rset.getString(2));
						stmt1.setString(4,rset.getString(3));
						stmt1.setString(5,rset.getString(4));
						stmt1.setString(6,rset.getString(5));
						stmt1.executeUpdate();
						stmt1.close();
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(5) + "," + file_new + " , " ), conn, "");
						logger_count++;
					}
					else if(rset.getString(7)==null || !rset.getString(7).equals("Y")){
						queryString1 = "UPDATE FMS_INV_FILE_DTL SET FILE_NAME = ? WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? "
								+ "AND FINANCIAL_YEAR = ? AND PDF_TYPE = ?";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1,file_nm);
						stmt1.setString(2,rset.getString(1));
						stmt1.setString(3,rset.getString(2));
						stmt1.setString(4,rset.getString(3));
						stmt1.setString(5,rset.getString(4));
						stmt1.setString(6,rset.getString(5));
						stmt1.executeUpdate();
						stmt1.close();    
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(5) + "," + file_nm + " , " ), conn, "");
						logger_count++;
					}
					else {
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(5) + "," + file_nm + " , " ), conn, "E");
						skipped_count++;     
					}
				}
			}
		rset.close();
		stmt.close();
		
		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
		System.out.println("<<END>><<FMS_INV_FILE_DTL_UPDATE>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_INV_FILE_DTL_UPDATE>>,", conn);
		
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

public void FMS_FFLOW_INV_FILE_DTL() throws IOException, SQLException {
	function_nm="FMS_FFLOW_INV_FILE_DTL()";
	try {
		
		table_name = "FMS_FFLOW_INV_FILE_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
		
		queryString1 = "INSERT INTO FMS_FFLOW_INV_FILE_DTL(COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,INVOICE_TYPE,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,"
				+ "PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_FFLOW_INV_FILE_DTL_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_FFLOW_INV_FILE_DTL_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	

				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String new_inv="", inv_type="";
					abbr = "";
					bu_seq="";inv_seq="";fin_yr="";pdf_type="";file_nm="";sign_by="";sign_by_cd="";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++)
					{
//					cell = cellIterator.next();
						data = null;
						if(i == 0) {
							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							
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
				    		
							data = company_cd;
						}
						else if(i == 1) {
							fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							data = fin_yr;
						}
						else if(i == 2) {
							inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN,INVOICE_TYPE FROM FMS_FFLOW_INV_MST WHERE INVOICE_ID_SEQ = ? AND FINANCIAL_YEAR = ? AND COUNTERPARTY_CD = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, inv_seq);
							stmt.setString(2, fin_yr);
							stmt.setString(3, cd);
							rset = stmt.executeQuery();
							if (rset.next()) {
								inv_seq = rset.getString(1);
								bu_seq = rset.getString(2);
								inv_type = rset.getString(3);
							} else {
								inv_seq ="0";
								bu_seq ="0";
								inv_type ="DI";
							}
							rset.close();
							stmt.close();
							
							data = bu_seq;
						}
						
						else if(i == 3) {
							data = inv_seq;
						}
						else if(i == 4) {
							data = inv_type;
						}
						else if (i == 15) {	// SIGNED_ENT_BY
							if(sign_by!=null) {
								if(sign_by.contains("@")) {
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMAIL_ID) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sign_by.toUpperCase());
									rset = stmt.executeQuery();
									if (rset.next()) {
										sign_by_cd = rset.getString(1);
									} else {
										sign_by_cd ="0";
									}
									rset.close();
									stmt.close();
								}
								else {
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sign_by.toUpperCase());
									rset = stmt.executeQuery();
									if (rset.next()) {
										sign_by_cd = rset.getString(1);
									} else {
										sign_by_cd ="0";
									}
									rset.close();
									stmt.close();
								}
							}
							else {
								sign_by_cd = "0";
							}
							data = sign_by_cd;
						}
						
						
						else {
//							if(i == 1) {	//BU_UNIT
//								bu_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							}
//							else if(i == 2) {	//INVOICE_SEQ
//								inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							}
//							 if(i == 4) {	//inv_type
//								inv_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							}
							 if (i == 5) {	// PDF_TYPE
								pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 6) {	//FILE_NAME
								file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 12) {	//SIGNED_BY
								sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_FFLOW_INV_FILE_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND INVOICE_TYPE = ? AND PDF_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, inv_seq);
					stmt.setString(4, fin_yr);
					stmt.setString(5, inv_type);
					stmt.setString(6, pdf_type);
					rset = stmt.executeQuery();
					
					if (!rset.next()){
						
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
					}
					
					rset.close();
					stmt.close();
				}
				br.close();
			}
			
//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";				
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		//conn.commit();
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


public void FMS_FFLOW_INV_FILE_DTL_UPDATE() throws IOException, SQLException {

	function_nm="FMS_FFLOW_INV_FILE_DTL_UPDATE()";
	try {
		
		System.out.println("<<START>><<FMS_FFLOW_INV_FILE_DTL_UPDATE>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_FFLOW_INV_FILE_DTL_UPDATE>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		String file_nm = "",file_new="",pdf_type="",cont_desc="";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,PDF_SIGNED FROM FMS_FFLOW_INV_FILE_DTL WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,PDF_TYPE,FILE_NAME,TIMESTAMP", conn);
	
		while (rset.next()) {
			total_count++;
			
				if (!(rset.getString(7).contains("-O.pdf") || rset.getString(7).contains("-D.pdf") || rset.getString(7).contains("-T.pdf"))) {
					file_nm = rset.getString(7).split(".pdf")[0];
					file_nm += "-" + rset.getString(6) + ".pdf";
				}
				else {
					file_nm = rset.getString(7);
				}
				
				if (file_nm!=null) {
					if(file_nm.contains("-E-")) {
						cont_desc = "DEFICIENCY_";
					}
					else if(file_nm.contains("-I-") || file_nm.contains("-M-")) {
						cont_desc = "LATEPAY_";
					}
//					file1 = new File(pdf_path+"freeflow_invoice/" + cont_desc + file_nm);
					if(rset.getString(8)!=null && rset.getString(8).equals("Y") && !file_nm.contains(cont_desc)) {
						file_new = cont_desc+file_nm;
						
						queryString1 = "UPDATE FMS_FFLOW_INV_FILE_DTL SET FILE_NAME = ? WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? "
								+ "AND FINANCIAL_YEAR = ? AND PDF_TYPE = ?";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1,file_new);
						stmt1.setString(2,rset.getString(1));
						stmt1.setString(3,rset.getString(2));
						stmt1.setString(4,rset.getString(3));
						stmt1.setString(5,rset.getString(4));
						stmt1.setString(6,rset.getString(5));
						stmt1.setString(7,rset.getString(6));
						stmt1.executeUpdate();
						stmt1.close();
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(5) + "," + rset.getString(6) + "," + file_new + " , " ), conn, "");
						logger_count++;
					}
					else if(rset.getString(8)==null || !rset.getString(8).equals("Y")){
						queryString1 = "UPDATE FMS_FFLOW_INV_FILE_DTL SET FILE_NAME = ? WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? "
								+ "AND FINANCIAL_YEAR = ? AND PDF_TYPE = ?";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1,file_nm);
						stmt1.setString(2,rset.getString(1));
						stmt1.setString(3,rset.getString(2));
						stmt1.setString(4,rset.getString(3));
						stmt1.setString(5,rset.getString(4));
						stmt1.setString(6,rset.getString(5));
						stmt1.setString(7,rset.getString(6));
						stmt1.executeUpdate();
						stmt1.close();    
						
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(5) + "," + rset.getString(6) + "," + file_new + " , " ), conn, "");
						logger_count++;
					}
					else {
						logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(5) + "," + rset.getString(6) + "," + file_nm + " , " ), conn, "E");
						skipped_count++;     
					}
				}
			}
		rset.close();
		stmt.close();
		
		msg = "Data has been Inserted Successfully in Database.";
		msg_type = "S";
		
		System.out.println("<<END>><<FMS_FFLOW_INV_FILE_DTL_UPDATE>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_FFLOW_INV_FILE_DTL_UPDATE>>,", conn);
		
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

public void FMS_INV_ADV_DTL() throws IOException, SQLException {
	function_nm="FMS_INV_ADV_DTL()";
	try {
		
		table_name = "FMS_INV_ADV_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr;
		String storage = "";
		
		queryString1 = "INSERT INTO FMS_INV_ADV_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,SEC_INT_REF,INV_COMPONENT,AMOUNT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INV_ADV_DTL_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_INV_ADV_DTL_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	

				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String  compo="",sec_int="",plant_seq="",tds_cd="",tds_amt="",tds_per="";
					double tax_amt = 0,gross_amt=0;
					abbr = "";
					bu_seq="";inv_seq="";fin_yr="";
					String sec_ref="";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < 11; i++)
					{
//					cell = cellIterator.next();
						data = null;
						if(i == 0) {
							fin_yr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							data = company_cd;
						}
						else if(i == 1) {
							seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,GROSS_AMT,TAX_AMT FROM FMS_INVOICE_MST WHERE INVOICE_NO = ? AND FINANCIAL_YEAR = ? AND COMPANY_CD = '2' ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, seq_no);
							stmt.setString(2, fin_yr);
							rset = stmt.executeQuery();
							if (rset.next()) {
								inv_seq = rset.getString(1);
								bu_seq = rset.getString(2);
								cd = rset.getString(3);
								agmt_no = rset.getString(4);
								agmt_rev = rset.getString(5);
								cont_no = rset.getString(6);
								cont_rev = rset.getString(7);
								cont_type = rset.getString(8);
								gross_amt = rset.getDouble(9);
								tax_amt = rset.getDouble(10);
								
							} else {
								inv_seq ="0";
								bu_seq ="0";
								cd = "0";
								agmt_no = "0";
								agmt_rev = "0";
								cont_no = "0";
								cont_rev = "0";
								cont_type = "0";
								gross_amt = 0;
								tax_amt = 0;
							}
							rset.close();
							stmt.close();
							
							data = bu_seq;
						}
						else if(i == 2) {
							data = inv_seq;
						}
						else if(i == 3) {
							data = fin_yr;
						}
						else {
							if (i == 4) {	// PDF_TYPE
								sec_int = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 5) {	//FILE_NAME
								compo = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					queryString = "SELECT COMPANY_CD FROM FMS_INV_ADV_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND SEC_INT_REF = ? AND INV_COMPONENT = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, inv_seq);
					stmt.setString(4, fin_yr);
					stmt.setString(5, sec_int);
					stmt.setString(6, compo);
					rset = stmt.executeQuery();
					
					if (!rset.next()){
						
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + sec_int + "," + compo  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						//INSERT SECURITY
						if(!storage.contains(sec_int)) {
							Map<String, Integer> seq = new HashMap<String, Integer>();

							columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT";
							
							queryString4 = "SELECT COMPANY_CD,COUNTERPARTY_CD,NULL,'0',NULL,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,"
									+ "TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,NULL,NULL,'0',NULL,'K',NULL,ADV_ADJUST "
									+ "FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = ? AND ADV_ADJUST = 'Y' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? AND AGMT_REV = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1,company_cd);
							stmt4.setString(2,cd);
							stmt4.setString(3,agmt_no);
							stmt4.setString(4,cont_no);
							stmt4.setString(5,agmt_rev);
							stmt4.setString(6,cont_rev);
							stmt4.setString(7,cont_type);
							
							rset4 = stmt4.executeQuery();
							
							queryString3 = "INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT) "
									+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
							
//							logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
							
							while (rset4.next()) {
									stmt3 = conn.prepareStatement(queryString3);
									
//									cd= rset4.getString(2);
//									agmt_no = rset4.getString(6);
//									cont_no = rset4.getString(8);
									
									
									if(!seq.containsKey(cd+agmt_no+cont_no)) {
										queryString2 = "SELECT A.SEQ_NO,A.SEC_REF_NO,B.COUNTERPARTY_ABBR FROM FMS_SECURITY_DEAL_MAP A, FMS_COUNTERPARTY_MST B "
												+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND "
												+ "A.SEQ_NO = (SELECT MAX(C.SEQ_NO) FROM FMS_SECURITY_DEAL_MAP C "
												+ "WHERE A.COMPANY_CD = C.COMPANY_CD AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD)";
										stmt2 = conn.prepareStatement(queryString2);
										stmt2.setString(1, company_cd);
										stmt2.setString(2, cd);
										rset2 = stmt2.executeQuery();
										if(rset2.next())
										{
											seq.put(cd+agmt_no+cont_no, rset2.getInt(1));
											
//											sec_ref = rset2.getString(2);
											abbr = rset2.getString(3);
											
										}
										else {
											seq.put(cd+agmt_no+cont_no, 0);
											sec_ref = "-S-";
										}
										rset2.close();
										stmt2.close();
									}
									
									for(int i = 0;i < columns.split(",").length;i++) {
										data = "";
										data = rset4.getString(i+1) == null ? "" : rset4.getString(i+1);
										
										if(i == 2) {
											seq_no = seq.get(cd+agmt_no+cont_no)+1 +"";
											data = seq_no;
											seq.put(cd+agmt_no+cont_no, Integer.parseInt(data));
											
										}
										else if(i == 4) {
//											data = sec_ref+"-"+seq_no ;
											sec_ref = abbr+"-S-"+seq_no;
											data = sec_ref;
										}
										
//										System.out.println(i+1+"=="+data);
										
											stmt3.setString(i+1,data);
									}
									
								//for data already exists..
								queryString5 = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND MAP_SEQ_NO = ? AND SEQ_REV_NO = ?  AND GX = ? ";
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, company_cd);
								stmt5.setString(2, cd);
								stmt5.setString(3, seq_no);
								stmt5.setString(4, rset4.getString(4));
								stmt5.setString(5, rset4.getString(17));
								stmt5.setString(6, rset4.getString(19));
							
								rset5 = stmt5.executeQuery();
								
								if (!rset5.next() ) {
									
									logger.data(fname, ( company_cd + "," + cd + "," + seq_no + "," + rset4.getString(4) + "," + rset4.getString(17) + "," + rset4.getString(19) + "," + rset4.getString(19) + " , " ), conn, "");
									
									stmt3.executeUpdate();
									storage += sec_int +",";
									stmt3.close();
									
									
								}
								else {
									
									stmt3.close();
									skipped_count++; 

									seq.put(cd+agmt_no+cont_no, seq.get(cd)-1);
									
									logger.data(fname, ( company_cd + "," + cd + "," + seq_no + "," + rset4.getString(4) + "," + rset4.getString(17) + "," + rset4.getString(19) + "," + rset4.getString(19) + " , " ), conn, "E");
									
								}
								stmt5.close();
								rset5.close();
								}
							rset4.close();
							stmt4.close();
							
							//FOR ADV ADJUST SECURITY MST
							String numvalue = "", gross = "", tax_per = "";
							double val = 0;
							double tds_amt1 = 0;
							columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,"
									+ "VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,"
									+ "EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,"
									+ "APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,"
									+ "PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD";
							
							queryString4 = "SELECT COMPANY_CD,COUNTERPARTY_CD,NULL,NULL,'R','ADV','LTCORA',NULL,ADV_ADJUST_UNIT,NULL,ADV_ADJUST_AMOUNT, "
									+ "NULL,NULL,NULL,NULL ,NULL,NULL,NULL, NULL, NULL,NULL, NULL, NULL,NULL,NULL,'O', NULL, NULL, 'Y',ENT_BY, "
									+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL, NULL,NULL, NULL,'0', 'K',NULL, NULL, NULL, NULL, NULL, NULL, "
									+ "NULL ,'CR',NULL,NULL,NULL,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT, 'DD/MM/YYYY HH24:MI:SS'),ADV_ADJUST_AMOUNT "
									+ "FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = ? AND ADV_ADJUST = 'Y' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? AND AGMT_REV = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1,company_cd);
							stmt4.setString(2,cd);
							stmt4.setString(3,agmt_no);
							stmt4.setString(4,cont_no);
							stmt4.setString(5,agmt_rev);
							stmt4.setString(6,cont_rev);
							stmt4.setString(7,cont_type);
							rset4 = stmt4.executeQuery();
							
							queryString3 = "INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,"
									+ "CURRENCY,VARIATION_VALUE,VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,"
									+ "RECEIPT_DT,ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,"
									+ "APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,PG_REF,CR_DR,"
									+ "TAX_AMT,TAX_STRUCT_CD,TDS_STRUCT_CD,TDS_AMT,BU_UNIT,PLANT_SEQ,GROSS_AMT) "
									+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
									+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
									+ "?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, "
									+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?,?,?,?,?,?,?,?,?)";
							
//							logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
							
							while (rset4.next()) {
									stmt3 = conn.prepareStatement(queryString3);
									
									
									for(int i = 0;i < columns.split(",").length;i++) {
										data = "";
										data = rset4.getString(i+1) == null ? "" : rset4.getString(i+1);
										
										if(i == 2) {
											data = seq_no;
										}
										else if(i == 3) {
											data = sec_ref;
										}
										else if(i == 9) {
											tax_per = (line.split(",")[i].contains("null") || line.split(",")[i].contains("@")) ? "1" : line.split(",")[i];
											data = null;
										}
										else if(i == 10) {
											data = rset4.getString(55);
//											double gross_inr = Double.valueOf(gross);
//											gross = (rset4.getDouble(55) - (rset4.getDouble(55) * (Integer.parseInt(tax_per)/(100 + Integer.parseInt(tax_per)))))+"";
											gross = BigDecimal.valueOf(rset4.getDouble(55)).subtract(BigDecimal.valueOf(rset4.getDouble(55)).multiply(new BigDecimal(tax_per).divide(BigDecimal.valueOf(100).add(new BigDecimal(tax_per)), 10, BigDecimal.ROUND_HALF_UP))).toPlainString();
										}
										else if(i == 18) {
											data = rset4.getString(54);
										}
										else if(i == 45) {
											for(int j = 11; j < line.split(",").length; j++) {
												data = null;
												data = line.split(",")[j].contains("null") ? null : line.split(",")[j];
												
												if (j == 11) {
													data = BigDecimal.valueOf(rset4.getDouble(55)).subtract(new BigDecimal(gross)).toPlainString();
												}
												else if(j == 12) {
													queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1, data.replaceAll("@", ","));
													rset = stmt.executeQuery();
													
													if (rset.next()) {
														data = rset.getString(1);
							
													}
													rset.close();
													stmt.close();
												}
												else if(j == 13) {
													String  percentage="";
													tds_per = line.split(",")[j].contains("null") ? null : line.split(",")[j];
													String[] parts = tds_per.split(" ");
													percentage = parts[1];
													numvalue=percentage.replaceAll("%", "");
													val = Double.valueOf(numvalue);
													
													queryString = "SELECT TAX_STR_CD  "
															+ "FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
													stmt = conn.prepareStatement(queryString);
													stmt.setString(1,tds_per);
													rset = stmt.executeQuery();
													if(rset.next()) {
														tds_cd = rset.getString(1);
													}
													else {
														tds_cd = null;
													}
													rset.close();
													stmt.close();
													
													data = tds_cd;
												}
												else if(j == 14) {
													tds_amt1 = rset4.getDouble(55) * val /  100 ;
													data = tds_amt1+"";
												}
												else if(j == 17) {
//													double gross = rset4.getDouble(55) - tds_amt1 ;
													data = gross;
												}
												stmt3.setString(++i,data);
											}
											i--;
										}
//										System.out.println(i+1+"=="+data);
										stmt3.setString(i+1,data);
									}
									
								//for data already exists..
								queryString5 = "SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND SEQ_REV_NO = '0' AND GX = 'K' ";
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, company_cd);
						    	stmt5.setString(2, cd);
						    	stmt5.setString(3, seq_no);
//						    	stmt5.setString(4, seq_rev_no);
//						    	stmt5.setString(4, gx);
							
								rset5 = stmt5.executeQuery();
								
								if (!rset5.next() ) {
									
									logger.data(fname, ( company_cd + "," + cd + "," + seq_no + "," + rset4.getString(4) + "," + rset4.getString(17) + "," + rset4.getString(19) + "," + rset4.getString(19) + " , " ), conn, "");
									
									stmt3.executeUpdate();
									stmt3.close();
									
//									logger_count++;
								}
								else {
									
									stmt3.close();
									skipped_count++; 

//									seq.put(cd, seq.get(cd)-1);
									
									logger.data(fname, ( company_cd + "," + cd + "," + seq_no + "," + rset4.getString(4) + "," + rset4.getString(17) + "," + rset4.getString(19) + "," + rset4.getString(19) + " , " ), conn, "E");
									
								}
								stmt5.close();
								rset5.close();
								}
							rset4.close();
							stmt4.close();
						}
						
//						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + sec_int + "," + compo  + "," ), conn, "E");
					}
					
					rset.close();
					stmt.close();
				}
				br.close();
			}
			
//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";				
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		//conn.commit();
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


public void FMS_SECURITY_TAX_DTL() throws IOException, SQLException {

	function_nm="FMS_SECURITY_TAX_DTL()";
	try {
		
		
		System.out.println("<<START>><<FMS_SECURITY_TAX_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_SECURITY_TAX_DTL>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEQ_REV_NO,GX,TAX_STRUCT_CD,TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEQ_REV_NO,GX,TAX_STRUCT_CD,NULL,NULL,TAX_AMT,NULL,"
				+ "ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY,TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),(VALUE-TAX_AMT)"
				+ "FROM FMS_SECURITY_MST WHERE COMPANY_CD = '2' ";
		stmt = conn.prepareStatement(queryString);
//		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_SECURITY_TAX_DTL(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEQ_REV_NO,GX,TAX_STRUCT_CD,TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(6);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			
			int count_desc=1;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					
					stmt1 = conn.prepareStatement(queryString1);
					double base_amt = 0;
					
					for(int i = 0;i < columns.split(",").length;i++) {
						data = "";
						data = rset.getString(i+1) == null ? "" : rset.getString(i+1);
						
						if(i == 5) {	//TAX_STRUCT_CD
							if(tax_struct_cd != null) {
								tax_struct_cd = rset.getString(6);
							}
							else {
								tax_struct_cd = "0";
							}
							data = tax_struct_cd;
						}
						else if(i == 6) {
							
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
//								else {
//									desc = null;
//								}
							
								if (!tax_struct_cd.equals("0")) {
									if(desc!=null)
									{
										desc_nm=desc.split(" ")[0];
									}
									
									// Calculating Tax Base Amount
//									base_amt = ((Integer.parseInt(rset.getString(15)) * 100) / Integer.parseInt(desc.split(" ")[1].split("%")[0])) + "";
//									base_amt = (Integer.parseInt(rset.getString(15)) * Integer.parseInt(desc.split(" ")[1].split("%")[0])/100)+"";
									base_amt = (Double.parseDouble(rset.getString(15)) *  Double.parseDouble(desc.split(" ")[1].split("%")[0]) / 100);
									
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
						
//						else if(i == 7) {	//TAX_EFF_DT
//							data = eff_dt;
//						}
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
//						else if(i==8)
//						{
//							
//							double tax_amt = 0.0;
//							if (desc != null) {
//							    if ( !desc.contains("on") ) {
//							    	
//							        count_value = desc.split("%")[0];
//							        count_value = count_value.split(" ")[1]; 
////							        else {
//							            gross_amt = rset.getString(16);
//							            tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100);
//							            adv_amt = tax_amt + "";
//							            adv=adv_amt;
////							        }
//							    }
//							    else if (desc.contains("on")) {
//
//							        count_value = desc.split("%")[0];
//							        count_value = count_value.split(" ")[1]; 
//							        tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100);
//							           
//							    }
//							}
//							data = tax_amt + "";
//
//						}
						else if(i == 8) {

							data = base_amt+"";
						}
						else if(i == 9) {

							data = rset.getString(15);
						}
						
						stmt1.setString(i+1,data);
							
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_SECURITY_TAX_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ?  AND "
					+ "SEQ_NO = ? AND SEQ_REV_NO = ? AND GX = 'K' AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, tax_struct_cd);
			stmt5.setString(6, tax_code);
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4)+ "," + rset.getString(5) + "," + tax_struct_cd + "," + tax_code + "," ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4)+ "," + rset.getString(5) + "," + tax_struct_cd + "," + tax_code + "," ), conn, "E");
					
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
		
	
		System.out.println("<<END>><<FMS_SECURITY_TAX_DTL>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_SECURITY_TAX_DTL>>,", conn);
		
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


public void FMS_INV_PAY_RECV_DTL() throws IOException, SQLException {
	
	function_nm="FMS_INV_PAY_RECV_DTL()";
	try {
		
		table_name = "FMS_INV_PAY_RECV_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		
		String inv_no = "", bu_state = "", fin_yr = "", seq = "", cont_type = "";

		
		queryString1 = "INSERT INTO FMS_INV_PAY_RECV_DTL (COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,PAY_RECV_DT,PAY_RECV_AMT,PAY_REMARK,ENT_BY,ENT_DT) "
		        + "VALUES(?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'))";
		
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INV_PAY_RECV_DTL_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_INV_PAY_RECV_DTL_"+start_end_dt+".csv";
			try (//				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INVOICE_DTL_"+start_end_dt+".xlsx"));
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				//				if (rowIterator.hasNext()) {	// For skipping the first row
				//					rowIterator.next();
				//				}
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,SEQ_NO,TIMESTAMP,", conn);
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									data = null;
									
									inv_no = "";
									bu_state = "";
									fin_yr = "";
									seq = "";
									cont_type = "";
									
									index = 1;
									stmt1 = conn.prepareStatement(queryString1);
									for (int i = 0; i < line.split(",").length; i++)
								    {	
				//				    	cell = cellIterator.next();
										data = null;
										
								    	if (i == 0) {
								    		cont_type = line.split(",")[i].contains("null") ? "" : line.split(",")[i];
											data = company_cd;
								    	}
								    	else if (i == 1) {
								    		inv_no = line.split(",")[i].contains("null") ? "" : line.split(",")[i];
								    		
								    		if (inv_no != null && cont_type != null && !cont_type.equals("E")) {
								    		
									    		queryString = "SELECT INVOICE_SEQ, FINANCIAL_YEAR, BU_STATE_TIN FROM FMS_INVOICE_MST WHERE INVOICE_NO = ? AND COMPANY_CD = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, inv_no);
									    		stmt.setString(2, company_cd);
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			inv_no = rset.getString(1);
									    			fin_yr = rset.getString(2);
									    			bu_state = rset.getString(3);
									    			data = bu_state;
									    			
									    		}
									    		else {
									    			queryString2 = "SELECT INVOICE_SEQ, FINANCIAL_YEAR, BU_STATE_TIN FROM FMS_FFLOW_INV_MST WHERE INVOICE_NO = ? AND COMPANY_CD = ? ";
										    		stmt2 = conn.prepareStatement(queryString2);
										    		stmt2.setString(1, inv_no);
										    		stmt2.setString(2, company_cd);
										    		rset2 = stmt2.executeQuery();
										    		if (rset2.next()) {
										    			inv_no = rset2.getString(1);
										    			fin_yr = rset2.getString(2);
										    			bu_state = rset2.getString(3);
										    			data = bu_state;
										    		}
										    		rset2.close();
										    		stmt2.close();
									    		}
									    		rset.close();
									    		stmt.close();
								    		}
								    		else if(inv_no != null && cont_type != null && cont_type.equals("E")) {
									    		
									    		queryString = "SELECT INVOICE_SEQ, FINANCIAL_YEAR, BU_STATE_TIN FROM FMS_FFLOW_INV_MST WHERE INVOICE_NO = ? AND COMPANY_CD = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, inv_no);
									    		stmt.setString(2, company_cd);
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			inv_no = rset.getString(1);
									    			fin_yr = rset.getString(2);
									    			bu_state = rset.getString(3);
									    			data = bu_state;
									    			
									    		}
									    		rset.close();
									    		stmt.close();
								    		}
								    		
								    	}
								    	else if (i == 2) {
								    		data = inv_no;
								    	}
								    	else if (i == 3) {
								    		data = fin_yr;
								    	}
								    	else {	
								    		
								    		if (i == 4) {
								    			seq = line.split(",")[i].contains("null") ? "" : line.split(",")[i];
								    		}
								    		
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    	}	
//								    	System.out.println(index+"-"+data);
								    	stmt1.setString(index++, data);			    	
								    }
									
								    queryString = "SELECT INVOICE_SEQ FROM FMS_INV_PAY_RECV_DTL WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? "
								    		+ "AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND SEQ_NO = ? ";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, fin_yr);
							    	stmt.setString(3, bu_state);
							    	stmt.setString(4, inv_no);
							    	stmt.setString(5, seq);
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !seq.equals("") && !inv_no.equals("") && !fin_yr.equals("") && !bu_state.equals("")) {
										//System.out.println(queryString1);
											
										logger.data(fname, (company_cd + "," + bu_state + " , " + inv_no + " , " + fin_yr + "," + seq + ","), conn, "");
										
										stmt1.executeUpdate();
										stmt1.close();
										
										logger_count++;
									}
							    	 else {
										stmt1.close();
										skipped_count++;     
										logger.data(fname, (company_cd + "," + bu_state + " , " + inv_no + " , " + fin_yr + "," + seq + ","), conn, "E");
									}
							    	 
							    	 rset.close();
									 stmt.close();
								}
			}
			
			// Below block of code is for unique SEIPL data
//			rowIterator = sheet.iterator();
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";		
	
		}
		
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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


//FMS_INVOICE_MST_CR_DR
public void FMS_INVOICE_MST_CR_DR() throws IOException, SQLException {
	
	function_nm="FMS_INVOICE_MST_CR_DR()";
	try {
		

		DateUtil utilDate = new DateUtil();
		DataBean_Sales_Invoice bill_freq = new DataBean_Sales_Invoice();
		
		table_name = "FMS_INVOICE_MST_CR_DR";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		String inv_seq="",dr_cr_flg="",inv_no="",bu="",mst_inv_no="";
		String pr_strt_dt="",freq="",contact_person_cd="",mail="";
		String inv_raise = "",tax_des="",pr_end_dt="",tax="",gross_amt="";
		String oc="",oa="",tc="",ta="",mm="",mc="",flg="",pre_sale_amt="";
		String final_tax = "",sug_per="",hold_amt="";
		double sale_amt=0,tax_amt=0;
		int flag=0;
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		
		queryString1 = "INSERT INTO FMS_INVOICE_MST(COMPANY_CD,FINANCIAL_YEAR,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV, "
				+ "CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ,CONTACT_PERSON_CD, "
				+ "INV_FLAG,INVOICE_NO,INVOICE_DT,FREQ,PERIOD_START_DT,PERIOD_END_DT,DUE_DT, "
				+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT, "
				+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,"
				+ "MARKET_MARGIN,OTHER_CHARGES,MARKET_MARGIN_AMT,OTHER_CHARGES_AMT,TAX_STRUCT_CD,TAX_AMT,TAX_EFF_DT, "
				+ "INVOICE_AMT,NET_PAYABLE_AMT,INV_STATUS,REMARK_1,REMARK_2,CHECKED_FLAG, "
				+ "CHECKED_BY,CHECKED_DT,AUTHORIZED_FLAG,AUTHORIZED_BY,AUTHORIZED_DT, "
				+ "APPROVED_FLAG,APPROVED_BY,APPROVED_DT,ENT_BY,ENT_DT,EXCHG_RATE_TYPE, "
				+ "MODIFY_BY,MODIFY_DT,TCS_TDS,TCS_AMT,TCS_FACTOR,INVOICE_ID_SEQ,PAY_RECV_AMT, "
				+ "PAY_RECV_DT,PAY_INSERT_BY,PAY_INSERT_DT,PAY_UPDATE_BY,PAY_UPDATE_DT, "
				+ "PAY_REMARK,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT,TDS_TAX_AMT, "
				+ "PDF_INV_DTL,PRINT_BY_ORI,PRINT_DT_ORI,PRINT_BY_TRI,PRINT_DT_TRI,PRINT_BY_DUP, "
				+ "PRINT_DT_DUP,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,TCS_STRUCT_CD, "
				+ "TCS_EFF_DT,TDS_STRUCT_CD,TDS_EFF_DT, "
				+ "CARGO_NO,INVOICE_SEQ, "
				+ "SUG_QTY,SUG_PERCENT,FIN_SYS,HOLD_AMT,REF_NO,CRITERIA,DISCOUNT_DAYS,INT_RATE, "
				+ "IMB_AMT,IMB_QTY,SHIPAY_AMT,SHIPAY_QTY,OVRUN_AMT,OVRUN_QTY) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, "
				+ " ?, ?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), "
				+ "?, ?, ?, ?, ?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), "
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), "
				+ "?, ?, ?, ?, ?,?, "
				+ "?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?, ?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), "//40
				+ "?, ?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?, "
				+ "?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,?,?,?,?, "
				+ "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), "
				+ "?, ?,?,?,?, "
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?, "
				+ "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?, ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?, "
				+ "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'),?,TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), "
				+ "?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?)";
				

		stmt1 = conn.prepareStatement(queryString1);
		Map<String, Integer> invseq = new HashMap<String, Integer>();
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_CR_DR_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_INVOICE_MST_CR_DR_"+start_end_dt+".csv";
			try (
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,INVOICE_NO,INVOICE_SEQ,REF_NO,TIMESTAMP,", conn);
								int inv_seq_no=0;
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									String dt="",financial_year="";
									abbr = "";
									cd = "0";
									data = null;
									
									index = 1;
									stmt1 = conn.prepareStatement(queryString1);
									
									for (int i = 0; i < line.split(",").length; i++)
								    {	
										data = null;
										
										data = null;
										
								    	if (i == 0) 
								    	{
								    		abbr = line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i];
								    		mst_inv_no = abbr.split("&")[1].replace("@", "/");
								    		abbr = abbr.split("&")[0];
								    		data = company_cd;
//								    		System.out.println("abbr "+abbr);
								    	}
								    	
								    	else if(i == 1) {
								    		financial_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		data = financial_year;
								    	}
                                        else if (i == 2) {	// Counterparty_Cd
								    		
								    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
								    		
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
										else if (i == 3) { //Agmt_no
								    		agmt_no = (line.split(",")[i].contains("null") ? null : line.split(",")[i]);
								    			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD FROM FMS_INVOICE_MST WHERE COMPANY_CD = '2' AND  COUNTERPARTY_CD = ? AND INVOICE_NO = ? ";
									    		stmt = conn.prepareStatement(queryString);
									    		stmt.setString(1, cd);
									    		stmt.setString(2, mst_inv_no);
									    		
									    		
									    		rset = stmt.executeQuery();
									    		if (rset.next()) {
									    			agmt_no = rset.getString(1);
									    			agmt_rev = rset.getString(2);
									    			cont_no = rset.getString(3);
									    			cont_rev = rset.getString(4);
									    			cont_type = rset.getString(5);
									    			bu_cont_person_cd = rset.getString(6);
									    			contact_person_cd = rset.getString(7);
									    		} else {
									    			agmt_no = "";
									    			agmt_rev = "";
									    			cont_no = "";
									    			cont_rev = "";
									    			cont_type = "";
									    			bu_cont_person_cd = "";
									    			contact_person_cd = "";
									    		}
									    		rset.close();
									    		stmt.close();
								    		
											data = agmt_no;
								    	}
								    	else if (i == 4) { //Agmt_rev
											data = agmt_rev;
								    	}
								    	else if (i == 5) { //Cont_no
								    		data = cont_no;
								    	}
							    		
								    	else if (i == 6) { //Cont_rev
								    		data = cont_rev;
								    	}
								    	else if (i == 7) { //contract_type
								    		data = cont_type;
								    	}
								    					    	
								    	else if(i == 8) { // BU_SEQ
								    		bu_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
											data  = bu_seq_no;
								    	}
								    	else if(i ==9) {
								    		queryString="SELECT PLANT_STATE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD='2'"
								    				+ " AND COUNTERPARTY_CD='2' AND ENTITY='B' AND SEQ_NO = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_seq_no);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			bu_plant_state = rset.getString(1);
								    		}else {
								    			bu_plant_state  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		queryString="SELECT TIN FROM FMS_STATE_MST WHERE STATE_NM = ?";
											stmt=conn.prepareStatement(queryString);
											stmt.setString(1, bu_plant_state);
											rset = stmt.executeQuery();
								    		if (rset.next()) {				    			
								    			state_code = rset.getString(1);
								    		}else {
								    			state_code  ="0";
								    		}	
								    		rset.close();
								    		stmt.close();
								    		
								    		data = state_code;
								    		
								    	}
								    	else if(i == 10) { //BU_CONTACT_PERSON

								    		if(cont_type.equals("O") || cont_type.equals("Q")) {
								    			cargo_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
												
												queryString = "SELECT CARGO_NO FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND CARGO_REF = ?";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, cd);
												stmt.setString(2, cargo_ref);
												rset = stmt.executeQuery();
												
												if (rset.next()) {
													cargo_no = rset.getString(1);
												}
												else{
													cargo_no="0";
												}
												rset.close();
												stmt.close();
								    		}else {
								    			cargo_no = "0";	
								    		}
								    		
//								    		queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
//								    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y' ORDER BY CONTACT_PERSON ";
//											stmt=conn.prepareStatement(queryString);
//											
//											String addr_flag = "";
//											if(bu_seq_no.equals("1")) {								
//												addr_flag = "P1";
//											}else if(bu_seq_no.equals("2")){
//												addr_flag = "P2";
//											}else if(bu_seq_no.equals("3")){
//												addr_flag = "P3";
//											}
//				
//											stmt.setString(1, addr_flag);
//											rset = stmt.executeQuery();
//											
//								    		if (rset.next()) {				    			
//								    			bu_cont_person_cd=rset.getString(1);
//								    		}else {
//								    			bu_cont_person_cd ="0";
//								    		}	
//								    		
//								    		rset.close();
//								    		stmt.close();
								    		data=bu_cont_person_cd;
								    	}
								    	else if (i == 11) {	//plant_seq
							    			plant_seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							    			data = plant_seq_no; 
							    		}
								    	else if(i == 12)
								    	{
								    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		
//								    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
//													
//									    	stmt = conn.prepareStatement(queryString);
//									    	stmt.setString(1, mail);
//									    	stmt.setString(2, cd);
//									    	rset = stmt.executeQuery();
//									    	
//									    	if (rset.next()) {
//									    		contact_person_cd = rset.getString(1);
//									    	}
//									    	else {
//									    		contact_person_cd = "1";
//									    	}
//									    	rset.close();
//									    	stmt.close();
								    		data=contact_person_cd;
								    	}
								    	else if(i == 13) 
								    	{
								    		dr_cr_flg = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		data=dr_cr_flg;
								    	}
								    	else if(i == 14) {
								    		inv_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		data = inv_no;
								    	}
								    	else if (i == 16) {

								    		queryString = "SELECT FREQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY HH24:MI:SS') FROM FMS_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO  = ? "
									    			+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? "
									    			+ "AND CONTRACT_TYPE = ? AND INVOICE_NO = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, cd);
									    	stmt.setString(3, agmt_no);
									    	stmt.setString(4, agmt_rev);
									    	stmt.setString(5, cont_no);
									    	stmt.setString(6, cont_rev);
									    	stmt.setString(7, cont_type);
									    	stmt.setString(8, mst_inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		freq = rset.getString(1);
									    		pr_strt_dt = rset.getString(2);
									    		pr_end_dt = rset.getString(3);
									    	}
									    	else
									    	{
									    		freq="";
									    	}
									    	stmt.close();
									    	rset.close();
									    	
								    		data = freq;
									    	
								    	}
								    	else if(i==17)
								    	{
								    		data = pr_strt_dt;
								    	}
								    	else if(i==18)
								    	{
								    		data  = pr_end_dt;
								    	}
								    	else if(i==20)
								    	{
								    		pre_sale_amt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		data = pre_sale_amt;
								    	}
								    	else if(i == 24) {
								    		name = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		exchg_cd="";
											queryString = "SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE UPPER(EXC_RATE_NM) = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, name);
									    	rset = stmt.executeQuery();
									    	if (rset.next()) {
									    		exchg_cd = rset.getString(1);
									    	}
									    	else {
									    		exchg_cd = "0";
									    	}
									    	rset.close();
									    	stmt.close();
									    	data = exchg_cd;
								    	}
								    	else if(i==27)
								    	{
								    		
								    		flg = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		queryString = "SELECT INVOICE_RAISED_IN "
								    				+ "FROM FMS_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO  = ? "
									    			+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? "
									    			+ "AND CONTRACT_TYPE = ? AND INVOICE_NO = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, cd);
									    	stmt.setString(3, agmt_no);
									    	stmt.setString(4, agmt_rev);
									    	stmt.setString(5, cont_no);
									    	stmt.setString(6, cont_rev);
									    	stmt.setString(7, cont_type);
									    	stmt.setString(8, mst_inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		inv_raise = rset.getString(1);
									    	}
									    	else
									    	{
									    		inv_raise="1";
									    	}
									    	stmt.close();
									    	rset.close();
									    	data = inv_raise;
								    	}
								    	else if(i==28)
								    	{
								    		gross_amt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		data = gross_amt;
								    	}
								    	else if(i == 29) 
								    	{
								    		String TC = "",TA="";
								    		String temp_tc = "",temp_ta="";
								    		queryString = "SELECT TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT "
								    				+ "FROM FMS_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO  = ? "
									    			+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? "
									    			+ "AND CONTRACT_TYPE = ? AND INVOICE_NO = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, cd);
									    	stmt.setString(3, agmt_no);
									    	stmt.setString(4, agmt_rev);
									    	stmt.setString(5, cont_no);
									    	stmt.setString(6, cont_rev);
									    	stmt.setString(7, cont_type);
									    	stmt.setString(8, mst_inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		TC = rset.getString(1);
									    		if(flg!=null && (flg.equals("QTY") ||flg.equals("PRICE")))
									    		{
									    			tc = "0";
									    			temp_tc = rset.getString(1);
									    			if(pre_sale_amt!=null && temp_tc!=null)
									    			{
									    			   ta = String.valueOf(Double.parseDouble(temp_tc)*Double.parseDouble(pre_sale_amt));
									    			}
									    			else
									    			{
									    				ta = "0";
									    			}
									    		}
									    		else if (flg!=null && !(flg.equals("QTY") ||flg.equals("PRICE")) && (TC == null || "0".equals(TC)))
										    	{
									    			tc = rset.getString(1);
										    		ta = rset.getString(2);
										    	}
									    		else if(flg!=null && !(flg.equals("QTY") ||flg.equals("PRICE")) && (TC!=null || !TC.equals("0")))
										    	{
									    			tc = "0" ;
										    		ta = "0" ;
										    	}
									    		else
									    		{
									    			tc = rset.getString(1);
										    		ta = rset.getString(2);
									    		}
									    		
									    	}
									    	else
									    	{
									    		tc = "0";
									    		ta = "0";
									    	}
									    	stmt.close();
									    	rset.close();
									    	data = tc;
								    	}
								    	
								    	else if(i == 30) 
								    	{
								    		data = ta;
								    	}
								    	else if(i == 31) 
								    	{
								    		String temp_mm = "",temp_mc="";
								    		String MM = "";
								    		queryString = "SELECT MARKET_MARGIN,MARKET_MARGIN_AMT "
								    				+ "FROM FMS_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO  = ? "
									    			+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? "
									    			+ "AND CONTRACT_TYPE = ? AND INVOICE_NO = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, cd);
									    	stmt.setString(3, agmt_no);
									    	stmt.setString(4, agmt_rev);
									    	stmt.setString(5, cont_no);
									    	stmt.setString(6, cont_rev);
									    	stmt.setString(7, cont_type);
									    	stmt.setString(8, mst_inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		MM = rset.getString(1);
									    		if(flg!=null && !(flg.equals("QTY") ||flg.equals("PRICE")) && (MM==null || MM.equals("0") ))
										    	{
									    			mm = rset.getString(1);
										    		mc = rset.getString(2);
										    	}
									    		else if(flg!=null && !(flg.equals("QTY") ||flg.equals("PRICE")) && (MM!=null || !MM.equals("0")))
										    	{
									    			tc = "0" ;
										    		ta = "0" ;
										    	}
									    		else
									    		{
									    			temp_mm = rset.getString(1);
									    			mm = "0";
										    		temp_mc = rset.getString(2);
										    		if(pre_sale_amt!=null && temp_mm!=null)
									    			{
										    			mc = String.valueOf(Double.parseDouble(temp_mm)*Double.parseDouble(pre_sale_amt));
									    			}
										    		else
										    		{
										    			mc="0";
										    		}
										    		
									    		}
									    		
									    	}
									    	else
									    	{
									    		mm = "0" ;
									    		mc = "0" ;
									    	}
									    	stmt.close();
									    	rset.close();
									    	data = mm;
								    	}
								    	else if(i == 32) 
								    	{
								    		
								    		String temp_oc = "",temp_oa="";
								    		String OC="";
								    		queryString = "SELECT OTHER_CHARGES,OTHER_CHARGES_AMT "
								    				+ "FROM FMS_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO  = ? "
									    			+ "AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? "
									    			+ "AND CONTRACT_TYPE = ? AND INVOICE_NO = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, cd);
									    	stmt.setString(3, agmt_no);
									    	stmt.setString(4, agmt_rev);
									    	stmt.setString(5, cont_no);
									    	stmt.setString(6, cont_rev);
									    	stmt.setString(7, cont_type);
									    	stmt.setString(8, mst_inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		OC = rset.getString(1);
									    		if(flg!=null && !(flg.equals("QTY") ||flg.equals("PRICE")) && (OC==null || OC.equals("0")))
										    	{
									    			oc = rset.getString(1);
										    		oa = rset.getString(2);
										    	}
									    		else if(flg!=null && !(flg.equals("QTY") ||flg.equals("PRICE")) && (OC!=null || !OC.equals("0") ))
										    	{
									    			oc = "0" ;
										    		oa = "0" ;
										    	}
									    		else
									    		{
									    			temp_oc = rset.getString(1);
									    			temp_oc = "0";
										    		temp_oa = rset.getString(2);
										    		if(pre_sale_amt!=null && temp_oc!=null)
									    			{
										    			oa = String.valueOf(Double.parseDouble(temp_oc)*Double.parseDouble(pre_sale_amt));
									    			}
										    		else
										    		{
										    			oa = "0";
										    		}
										    		
									    		}
									    	}
									    	else
									    	{
									    		oc = "0" ;
									    		oa = "0" ;
									    	}
									    	stmt.close();
									    	rset.close();
									    	data = oc;
								    	}
								    	else if(i == 33) 
								    	{
								    		data = mc;
								    	}
								    	else if(i == 34) 
								    	{
								    		data = oa;
								    	}
								    	else if(i == 35) {
								    		
								    		String temp_struct = "";
								    		dt = "";
								    		tax_des = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		if (tax_des == null) {
								    		    continue; 
								    		}
								    		if (tax_des.contains("-")) {
								    		    name = tax_des.replaceAll("-", ", ");
								    		    tax = name;
								    		} 
								    		else 
								    		{
								    		    name = tax_des;
								    		    tax = name;
								    		}
								    		//tax_amt
								    		
								    		if(flg!=null && (flg.equals("QTY") ||flg.equals("PRICE")))
								    		{
									    		sale_amt = Double.parseDouble(gross_amt)+Double.parseDouble(ta)+Double.parseDouble(mc)+Double.parseDouble(oa);
									    		double taxPercent = 0.0;

									    		if (tax != null && !tax.isEmpty()) 
									    		{
									    		    String[] parts = tax.split(",");

									    		    for (String part : parts) 
									    		    {
									    		        part = part.trim();
									    		        int percentIndex = part.indexOf('%');

									    		        if (percentIndex > 0) 
									    		        {
									    		            String number = part.substring(0, percentIndex).replaceAll("[^0-9.]", "");
									    		            taxPercent += Double.parseDouble(number);
									    		        }
									    		    }
									    		     tax_amt = sale_amt * taxPercent / 100;
									    		}
									    		else
									    		{
									    			 tax_amt = 0;
									    		}
								    		}
								    		
								    		
								            //tax_sruct_cd 
								    		if (!name.contains(", ")) {
												queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, name);
												rset = stmt.executeQuery();
												if (rset.next()) {
													temp_struct = rset.getString(1);
													dt = rset.getString(2);
												}
												rset.close();
												stmt.close();
											}
								    		else {

												flag = 0;
												queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
												stmt = conn.prepareStatement(queryString);
												stmt.setString(1, "%"+name.split(", ")[0]+"%");
												rset = stmt.executeQuery();
												
												while (rset.next()) {
													
													for (int j = 0; j < name.split(", ").length; j++) {
														if (rset.getString(2).contains(name.split(", ")[j])) {
															flag = 1;
														}
														else {
															flag = 0;
															break;
														}
													}
													
													if (flag == 1) {
														temp_struct = rset.getString(1);
														name=rset.getString(2);
														dt = rset.getString(3);
														break;
													}
												}
												rset.close();
												stmt.close();
											}
										    	data = temp_struct;
								    	}
								    	else if(i == 36) 
								    	{
								    		
								    		final_tax  = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		if(flg!=null && (flg.equals("QTY") ||flg.equals("PRICE")))
								    		{
								    			data = tax_amt+"";
								    		}
								    		else
								    		{
								    			data = final_tax;
								    		}
								    		
								    	}
								    	else if(i == 37) 
								    	{
								    		data = dt;
								    	}
								    	
								    	else if(i == 38) 
								    	{
								    		String inv_amt  = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		double net_amt = 0;
								    		if(flg!=null && (flg.equals("QTY") ||flg.equals("PRICE")))
								    		{
								    			net_amt = tax_amt+sale_amt;
								    			data = net_amt+"";
								    		}
								    		else
								    		{
								    			data = inv_amt;
								    		}
								    		 
								    	}
								    	else if(i == 39) 
								    	{
								    		String inv_amt  = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		double net_amt = 0;
								    		if(flg!=null && (flg.equals("QTY") ||flg.equals("PRICE")))
								    		{
								    			net_amt = tax_amt+sale_amt;
								    			data = net_amt+"";
								    		}
								    		else
								    		{
								    			data = inv_amt;
								    		}
								    	}
								    	
								    	else if(i == 82) 
								    	{
								    		queryString = "SELECT TAX_STRUCT_CD  "
													+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TCS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
											stmt = conn.prepareStatement(queryString);
											stmt.setString(1, cd);
											stmt.setString(2, "C");
											stmt.setString(3,company_cd);
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
								    	else if(i == 84) {
								    		queryString = "SELECT TAX_STRUCT_CD  "
													+ "FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_APP = 'TDS' AND COUNTERPARTY_CD = ? AND ENTITY =? AND  COMPANY_CD =? AND FLAG ='Y'";
											stmt = conn.prepareStatement(queryString);
											stmt.setString(1, cd);
											stmt.setString(2, "C");
											stmt.setString(3,company_cd);
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
								    	
								    	else if(i == 86) {
								    		data  = cargo_no;
								    	}
								    	else if(i == 87) 
								    	{
								    		
								    		int count=0;
								    		queryString = "SELECT MAX(INVOICE_SEQ) "
								    				+ "FROM FMS_INVOICE_MST "
								    				+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? AND BU_STATE_TIN =? ";
								    		stmt = conn.prepareStatement(queryString);
								    		stmt.setString(1, company_cd);
								    		stmt.setString(2, financial_year);
								    		stmt.setString(3, state_code);
								    		rset = stmt.executeQuery();
								    		if(rset.next())
								    		{
								    			count = rset.getInt(1);
								    		}
								    		else
								    		{
								    			count=0;
								    		}
								    		stmt.close();
									    	rset.close();
									    	if(count>0)
									    	{
									    		inv_seq_no = count+1;
									    	}
									    	else
									    	{
									    		inv_seq_no = 1;
									    	}
								    		data=inv_seq_no+"";
								    	}
								    	
								    	else if(i==88)
								    	{
								    		queryString = "SELECT SUG_QTY,SUG_PERCENT,HOLD_AMT "
													+ "FROM FMS_INVOICE_MST WHERE INVOICE_NO = ? AND COUNTERPARTY_CD = ? AND COMPANY_CD =? ";
											stmt = conn.prepareStatement(queryString);
											stmt.setString(1, mst_inv_no);
											stmt.setString(2, cd);
											stmt.setString(3,company_cd);
											rset = stmt.executeQuery();
											if(rset.next()) {
												data = rset.getString(1);
												sug_per = rset.getString(2);
												hold_amt = rset.getString(3);
											}
											else {
												data = null;
												sug_per = null;
												hold_amt = null;
											}
											rset.close();
											stmt.close();
								    	}
								    	else if(i==89)
								    	{
								    		data = sug_per;
								    	}
								    	else if(i==91)
								    	{
								    		data = hold_amt;
								    	}
								    	else if(i==92)
								    	{
								    		String ref_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		ref_no = ref_no.replace("@", "/");
								    		data = ref_no;
								    	}
								    	else 
								    	{			    	
								    		
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    	}	
//								    	System.out.println(index+"-"+data);
								    	stmt1.setString(index++, data);		    	
								    }
									
								    queryString = "SELECT COUNTERPARTY_CD FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								    		+ "AND FINANCIAL_YEAR = ? AND CONTRACT_TYPE = ? "
								    		+ "AND INVOICE_SEQ = ? ";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, cd);
							    	stmt.setString(3, financial_year);
							    	stmt.setString(4, cont_type);
							    	stmt.setString(5, inv_seq_no+"");
							    	
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") && inv_raise !=null && flg!= null) {
											logger.data(fname, (company_cd+"," + cd + "," + financial_year+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type  + "," +inv_no+","+ inv_seq_no + ","+mst_inv_no+","), conn, "");
											
											stmt1.executeUpdate();
											stmt1.close();
											
											logger_count++;
									}
							    	 else {
											stmt1.close();
											skipped_count++;     
											logger.data(fname, (company_cd+"," + cd + "," + financial_year+","+agmt_no+","+agmt_rev+","+cont_no+","+cont_rev+","+cont_type  + "," +inv_no+","+ inv_seq_no + ","+mst_inv_no+","), conn, "E");

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
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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


//FMS_INV_TAX_DTL_CR_DR
public void FMS_INV_TAX_DTL_CR_DR() throws IOException, SQLException {

	function_nm="FMS_INV_TAX_DTL_CR_DR()";
	try {
		
		
		System.out.println("<<START>><<FMS_INV_TAX_DTL_CR_DR>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_INV_TAX_DTL_CR_DR>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		double tax_amt = 0,final_amt=0;
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss'),"
				+ "NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
				+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),GROSS_AMT"
				+ "	FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? AND INV_FLAG IN ('CR','DR') ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,"
				+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(5);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			
			int count_desc=1;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					
					stmt1 = conn.prepareStatement(queryString1);
					
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
									tax_amt = rset.getDouble(9);
									final_amt = tax_amt/2;
								}
								else {
									tax_amt = rset.getDouble(9);
									final_amt = tax_amt;
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
							data = final_amt + "";
						}
						
						else if(i == 9 && desc != null && desc.contains("on")) {

							data = adv;
						}
						
						stmt1.setString(i+1,data);
							
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_INV_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(5));
			stmt5.setString(6, tax_code);
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
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
		
	
		System.out.println("<<END>><<FMS_INV_TAX_DTL_CR_DR>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_INV_TAX_DTL_CR_DR>>,", conn);
		
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



//FMS_INV_CRDR_REF
public void FMS_INV_CRDR_REF() throws IOException, SQLException {
	
	function_nm="FMS_INV_CRDR_REF()";
	try {
		

		DateUtil utilDate = new DateUtil();
		DataBean_Sales_Invoice bill_freq = new DataBean_Sales_Invoice();
		
		table_name = "FMS_INV_CRDR_REF";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		String financial_year="",inv_amt="",inv_raise="",inv_no="",bu="";
		String gross_amt="",temp_struct="",tc="",ta="",mm="",ma="",oc="",oa="";
		String mst_inv_no="",criteria="",sale_amt ="",tax_des="";
		String temp_tc="",temp_oc="",temp_mm="",tax="",tcs_factor="",tcs_amt="";
		double tax_amt=0,final_sale_amt=0;
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		
		queryString1 =  "INSERT INTO FMS_INV_CRDR_REF (COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT, "
			  + "EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT, "
			  + "TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,"
			  + "TAX_STRUCT_CD,TAX_AMT,INVOICE_AMT,NET_PAYABLE_AMT,TCS_TDS,TCS_AMT,TCS_FACTOR,TDS_GROSS_PERCENT,TDS_GROSS_AMT,TDS_TAX_PERCENT, "
			  + "TDS_TAX_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
			  + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, "
			  + "?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?, "
			  + "?,?,?,?,?,?,"
			  + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			  + "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";

		
		stmt1 = conn.prepareStatement(queryString1);
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INV_CRDR_REF_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_INV_CRDR_REF_"+start_end_dt+".csv";
			try (
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
								String line = br.readLine();
								
								logger.checkpoint(fname, "COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,INVOICE_NO,TIMESTAMP,", conn);
								int inv_seq_no=1;
								
								while ((line = br.readLine()) != null) {
									total_count++; 
									abbr = "";
									cd = "0";
									data = null;
									
									index = 1;
									stmt1 = conn.prepareStatement(queryString1);
									
									for (int i = 0; i < line.split(",").length; i++)
								    {	
										data = null;
										
								    	if (i == 0) {
								    		
								    		inv_no = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
								    		inv_no = inv_no.split("&")[1];
								    		data = company_cd;
								    	}
								    	
								    	else if(i==1) {
								    		financial_year = line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i];
								    		data = financial_year;
								    	}
								    	
								    	else if (i == 2) 
								    	{	
								    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
								    		queryString = "SELECT INVOICE_RAISED_IN,BU_STATE_TIN,INVOICE_SEQ,GROSS_AMT, "
								    				+ "EXCHG_RATE_CD,TAX_STRUCT_CD,INVOICE_AMT,CRITERIA FROM FMS_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND INVOICE_NO  = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		inv_raise = rset.getString(1);
									    		bu = rset.getString(2);
									    		inv_seq_no = rset.getInt(3);
//									    		gross_amt = rset.getString(4);
									    		exchg_cd = rset.getString(5);
									    		temp_struct = rset.getString(6);
//									    		inv_amt = rset.getString(7);
									    		criteria = rset.getString(8);
									    	}
									    	else
									    	{
									    		inv_raise="1";
									    		inv_amt=null;
									    	}
									    	stmt.close();
									    	rset.close();
									    	data = bu;
								    	}
								    	else if(i==3)
								    	{
								    		mst_inv_no = line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i].replaceAll("@", "/");
								    		data = inv_seq_no+"";
								    	}
								    	else if(i==4)
								    	{
								    		sale_amt = line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i];
								    		data = sale_amt;
								    	}
								    	else if(i == 8) {
								    		
									    	data = exchg_cd;
								    	}
								    	else if(i==11)
								    	{
									    	data = inv_raise;
								    	}

								    	else if(i==12)
								    	{
								    		gross_amt = line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i];
									    	data = gross_amt;
								    	}
								    	
								    	else if(i==13)
								    	{
								    		
								    		queryString = "SELECT TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT "
								    				+ "FROM FMS_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND INVOICE_NO  = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, mst_inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		
									    		if ("QTY".equalsIgnoreCase(criteria) ||"EXCHG".equalsIgnoreCase(criteria)
									    				||"PRICE".equalsIgnoreCase(criteria))
									    		{
									    			temp_tc = rset.getString(1);
									    			if(sale_amt!=null && temp_tc!=null)
									    			{
									    			   ta = String.valueOf(Double.parseDouble(temp_tc)*Double.parseDouble(sale_amt));
									    			   tc = rset.getString(1);
									    			}
									    			else
									    			{
									    				ta = "0";
									    				tc = "0";
									    			}
									    		}
									    		else
									    		{
									    			tc = rset.getString(1);
									    			ta = rset.getString(2);
									    		}

									    	}
									    	else
									    	{
									    		tc = "0";
									    		ta = "0";
									    	}
									    	stmt.close();
									    	rset.close();
									    	data = tc;
								    	}
								    	else if(i==14)
								    	{
								    		data = ta;
								    	}
								    	else if(i==15)
								    	{
								    		queryString = "SELECT MARKET_MARGIN,MARKET_MARGIN_AMT "
								    				+ "FROM FMS_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND INVOICE_NO  = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, mst_inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		
									    		if ("QTY".equalsIgnoreCase(criteria) ||"EXCHG".equalsIgnoreCase(criteria)
									    				||"PRICE".equalsIgnoreCase(criteria))
									    		{
									    			temp_mm = rset.getString(1);
									    			if(sale_amt!=null && temp_mm!=null)
									    			{
									    			   ma = String.valueOf(Double.parseDouble(temp_mm)*Double.parseDouble(sale_amt));
									    			   mm = rset.getString(1);
									    			}
									    			else
									    			{
									    				mm = "0";
									    				ma = "0";
									    			}
									    		}
									    		else
									    		{
										    		mm = rset.getString(1);
										    		ma = rset.getString(2);
									    		}

									    	}
									    	else
									    	{
									    		mm = "0";
									    		ma = "0";
									    	}
									    	stmt.close();
									    	rset.close();
								    		data = mm;
								    	}
								    	else if(i==16)
								    	{
								    		data = ma;
								    	}
								    	else if(i==17)
								    	{
								    		
								    		queryString = "SELECT OTHER_CHARGES,OTHER_CHARGES_AMT "
								    				+ "FROM FMS_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND INVOICE_NO  = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, mst_inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		
									    		if ("QTY".equalsIgnoreCase(criteria) ||"EXCHG".equalsIgnoreCase(criteria)
									    				||"PRICE".equalsIgnoreCase(criteria))
									    		{
									    			temp_oc = rset.getString(1);
									    			if(sale_amt!=null && temp_oc!=null)
									    			{
									    			   oa = String.valueOf(Double.parseDouble(temp_oc)*Double.parseDouble(sale_amt));
									    			   oc = rset.getString(1);
									    			}
									    			else
									    			{
									    				oc = "0";
									    				oa = "0";
									    			}
									    		}
									    		else
									    		{
										    		oc = rset.getString(1);
										    		oa = rset.getString(2);
									    		}

									    	}
									    	else
									    	{
									    		oc = "0";
									    		oa = "0";
									    	}
									    	stmt.close();
									    	rset.close();
								    		data = oc;
								    	}
								    	else if(i==18)
								    	{
								    		data = oa;
								    	}
								    	else if(i==19)
								    	{
								    		
								    		tax_des = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		if (tax_des == null) {
								    		    continue; 
								    		}
								    		if (tax_des.contains("-")) {
								    		    name = tax_des.replaceAll("-", ", ");
								    		} 
								    		else 
								    		{
								    		    name = tax_des;
								    		}
//								    		System.out.println("name "+name);
								    		//tax_amt
								    		
								    		if ("QTY".equalsIgnoreCase(criteria) ||"EXCHG".equalsIgnoreCase(criteria)
								    				||"PRICE".equalsIgnoreCase(criteria))
								    		{
//								    			if("QTY".equalsIgnoreCase(criteria) ||"EXCHG".equalsIgnoreCase(criteria))
//								    			{
								    			   final_sale_amt = Double.parseDouble(gross_amt)+Double.parseDouble(ta)+Double.parseDouble(ma)+Double.parseDouble(oa);
//								    			}
//								    			else if("EXCHG".equalsIgnoreCase(criteria))
//								    			{
//								    				final_sale_amt = Double.parseDouble(gross_amt);
//								    			}
									    		double taxPercent = 0.0;

									    		if (name != null && !name.isEmpty()) 
									    		{
									    		    String[] parts = name.split(",");

									    		    for (String part : parts) 
									    		    {
									    		        part = part.trim();
									    		        int percentIndex = part.indexOf('%');

									    		        if (percentIndex > 0) 
									    		        {
									    		            String number = part.substring(0, percentIndex).replaceAll("[^0-9.]", "");
									    		            taxPercent += Double.parseDouble(number);
									    		        }
									    		    }
									    		     tax_amt = final_sale_amt * taxPercent / 100;
									    		}
									    		else
									    		{
									    			 tax_amt = 0;
									    		}
								    		}

								    		
								    		data = temp_struct;
								    	}
								    	else if(i==20)
								    	{
								    		tax = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		if ("QTY".equalsIgnoreCase(criteria) ||"EXCHG".equalsIgnoreCase(criteria)
								    				||"PRICE".equalsIgnoreCase(criteria))
								    		{
								    		 data = tax_amt+"";
								    		}
								    		else
								    		{
								    		  data = tax+"";
								    		}
								    	}
								    	else if(i==21)
								    	{
								    		String net_amt1 = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		double net_amt = 0;
								    		if ("QTY".equalsIgnoreCase(criteria) ||"EXCHG".equalsIgnoreCase(criteria)
								    				||"PRICE".equalsIgnoreCase(criteria))
								    		{
								    			net_amt = tax_amt+final_sale_amt;
								    			data = net_amt+"";
								    		}
								    		else
								    		{
//								    			net_amt = tax+final_sale_amt;
								    			data = net_amt1;
								    		}

								    	}
								    	else if(i==22)
								    	{
								    		String net_amt1 = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    		double net_amt = 0;
								    		if ("QTY".equalsIgnoreCase(criteria) ||"EXCHG".equalsIgnoreCase(criteria)
								    				||"PRICE".equalsIgnoreCase(criteria))
								    		{
								    			net_amt = tax_amt+final_sale_amt;
								    			data = net_amt+"";
								    		}
								    		else
								    		{
//								    			net_amt = tax+final_sale_amt;
								    			data = net_amt1;
								    		}

								    	}
								    	else if(i==23)
								    	{
								    		String tcs_tds="";
								    		queryString = "SELECT TCS_TDS,TCS_AMT,TCS_FACTOR "
								    				+ "FROM FMS_INVOICE_MST "
									    			+ "WHERE COMPANY_CD = ? AND INVOICE_NO  = ? ";
									    	stmt = conn.prepareStatement(queryString);
									    	stmt.setString(1, company_cd);
									    	stmt.setString(2, inv_no);
									    	rset = stmt.executeQuery();
									    	if(rset.next())
									    	{
									    		tcs_tds = rset.getString(1);
									    		tcs_amt = rset.getString(2);
									    		tcs_factor = rset.getString(3);
									    	}
									    	else
									    	{
									    		tcs_tds = null;
									    		tcs_amt = null;
									    		tcs_factor = null;
									    	}
									    	stmt.close();
									    	rset.close();
								    		data = tcs_tds;

								    	}
								    	else if(i==24)
								    	{
								    		data = tcs_amt;
								    	}
								    	else if(i==25)
								    	{
								    		data = tcs_factor;
								    	}
								    	else 
								    	{			    	
									    	data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								    	}	
//								    	System.out.println(index+"-"+data);
								    	stmt1.setString(index++, data);		    	
								    }
									
								    queryString = "SELECT INVOICE_SEQ FROM FMS_INV_CRDR_REF WHERE COMPANY_CD = ? AND BU_STATE_TIN = ? "
								    		+ "AND FINANCIAL_YEAR = ? AND INVOICE_SEQ = ? ";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, bu);
							    	stmt.setString(3, financial_year);
							    	stmt.setString(4, inv_seq_no+"");
							    	
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !bu.equals("") ) 
							    	 {
							    		 logger.data(fname, (company_cd+"," + financial_year+"," +bu+","+ inv_seq_no + ","+inv_no+","), conn, "");
											
											stmt1.executeUpdate();
											stmt1.close();
											
											logger_count++;
									}
							    	 else {
											stmt1.close();
											skipped_count++;     
											logger.data(fname, (company_cd+"," + financial_year+"," +bu+","+ inv_seq_no + ","+inv_no+","), conn, "E");
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
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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

//FMS_INV_CRDR_TAX_DTL
public void FMS_INV_CRDR_TAX_DTL() throws IOException, SQLException {

	function_nm="FMS_INV_CRDR_TAX_DTL()";
	try {
		
		
		System.out.println("<<START>><<FMS_INV_CRDR_TAX_DTL>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_INV_CRDR_TAX_DTL>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,NULL,"
				+ "NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
				+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),GROSS_AMT"
				+ "	FROM FMS_INV_CRDR_REF WHERE COMPANY_CD = ? ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_INV_CRDR_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,"
				+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(5);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			double tax_amt = 0,final_tax=0;
			
			int count_desc=1;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					
					stmt1 = conn.prepareStatement(queryString1);
					
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
									tax_amt = rset.getDouble(9);
									final_tax = tax_amt/2;
								}
								else {
									tax_amt = rset.getDouble(9);
									final_tax = tax_amt;
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
							
//							double tax_amt = 0.0;
//							if (desc != null) {
//							    if ( !desc.contains("on") ) {
//							    	
//							        count_value = desc.split("%")[0];
//							        count_value = count_value.split(" ")[1]; 
////							        else {
//							            gross_amt = rset.getString(15);
//							            tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100);
//							            adv_amt = tax_amt + "";
//							            adv=adv_amt;
////							        }
//							    }
//							    else if (desc.contains("on")) {
//
//							        count_value = desc.split("%")[0];
//							        count_value = count_value.split(" ")[1]; 
//							        tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100);
//							           
//							    }
//							}
							data = final_tax + "";

						}
						
						else if(i == 9 && desc != null && desc.contains("on")) {

							data = adv;
						}
						
						stmt1.setString(i+1,data);
							
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_INV_CRDR_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(5));
			stmt5.setString(6, tax_code);
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5)+ "," + rset.getString(12) + " , " ), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(12) + " , " ), conn, "E");
					
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
		
	
		System.out.println("<<END>><<FMS_INV_CRDR_TAX_DTL>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_INV_CRDR_TAX_DTL>>,", conn);
		
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


//FMS_INV_FILE_DTL_CR_DR
public void FMS_INV_FILE_DTL_CR_DR() throws IOException, SQLException {
	function_nm="FMS_INV_FILE_DTL_CR_DR()";
	try {
		
		table_name = "FMS_INV_FILE_DTL_CR_DR";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
		
		queryString1 = "INSERT INTO FMS_INV_FILE_DTL(COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT, "
				+ "PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT,EMAIL_SENT,EMAIL_SENT_BY,EMAIL_SENT_DT) "
				+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss') )";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INV_FILE_DTL_CR_DR_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_INV_FILE_DTL_CR_DR_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,INVOICE_NO,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String new_inv="";
					abbr = "";
					bu_seq="";inv_seq="";fin_yr="";pdf_type="";file_nm="";sign_by="";sign_by_cd="";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++)
					{
						data = null;
						if(i == 0) {
							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							
							String[] parts = abbr.split("@");
							abbr=parts[0];
							cont_type=parts[1];
							
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
				    		
							data = company_cd;
						}
						else if(i == 1) {
							fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							new_inv = fin_yr.split("@")[1];
							fin_yr = fin_yr.split("@")[0];
							data = fin_yr;
						}
						else if(i == 2) {
							inv_seq = line.split(",")[i];
							
//							new_inv = inv_seq.split("@")[1];
							inv_seq = inv_seq.split("@")[0];
							
							if(!new_inv.equals("null") && !new_inv.equals("")) {
								queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN FROM FMS_INVOICE_MST WHERE INVOICE_NO = ? AND FINANCIAL_YEAR = ? "
										+ "AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = '2' AND INV_FLAG IN ('CR','DR') ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, new_inv);
								stmt.setString(2, fin_yr);
								stmt.setString(3, cd);
								stmt.setString(4, cont_type);
								rset = stmt.executeQuery();
								if (rset.next()) {
									seq_no = rset.getString(1);
									bu_seq = rset.getString(2);
								} else {
									seq_no ="0";
									bu_seq ="0";
								}
								rset.close();
								stmt.close();
							}else {
								queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN FROM FMS_INVOICE_MST WHERE INVOICE_ID_SEQ = ? AND FINANCIAL_YEAR = ? "
										+ "AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = '2' AND INV_FLAG IN ('CR','DR') ";
								stmt = conn.prepareStatement(queryString);
								stmt.setString(1, inv_seq);
								stmt.setString(2, fin_yr);
								stmt.setString(3, cd);
								stmt.setString(4, cont_type);
								rset = stmt.executeQuery();
								if (rset.next()) {
									seq_no = rset.getString(1);
									bu_seq = rset.getString(2);
								} else {
									seq_no ="0";
									bu_seq ="0";
								}
								rset.close();
								stmt.close();
							}
							
							data = bu_seq;
						}
						
						else if(i == 3) {
							data = seq_no;
							
//							
						}
						else if (i == 14) {	// SIGNED_ENT_BY
							if(sign_by!=null) {
								if(sign_by.contains("@")) {
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMAIL_ID) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sign_by.toUpperCase());
									rset = stmt.executeQuery();
									if (rset.next()) {
										sign_by_cd = rset.getString(1);
									} else {
										sign_by_cd ="0";
									}
									rset.close();
									stmt.close();
								}
								else {
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sign_by.toUpperCase());
									rset = stmt.executeQuery();
									if (rset.next()) {
										sign_by_cd = rset.getString(1);
									} else {
										sign_by_cd ="0";
									}
									rset.close();
									stmt.close();
								}
							}
							else {
								sign_by_cd = "0";
							}
							data = sign_by_cd;
						}
						
						
						else {
							 if (i == 4) {	// PDF_TYPE
								pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 5) {	//FILE_NAME
								file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 11) {	//SIGNED_BY
								sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_INV_FILE_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND PDF_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, seq_no);
					stmt.setString(4, fin_yr);
					stmt.setString(5, pdf_type);
					rset = stmt.executeQuery();
					
					if (!rset.next()){
						
						logger.data(fname, (company_cd + "," + bu_seq + "," + seq_no + ","  + new_inv  + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + seq_no + "," + new_inv  + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
					}
					
					rset.close();
					stmt.close();
				}
				br.close();
			}
			
//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";				
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		//conn.commit();
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

public void FMS_INVOICE_MST_LP() throws IOException, SQLException {
	
	function_nm="FMS_INVOICE_MST_LP()";
	try {
		

		DateUtil utilDate = new DateUtil();
		DataBean_Sales_Invoice bill_freq = new DataBean_Sales_Invoice();
		
		table_name = "FMS_INVOICE_MST_LP";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0; 
		
		queryString1 = "INSERT INTO FMS_INVOICE_MST (COMPANY_CD, FINANCIAL_YEAR, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
		           + "CONTRACT_TYPE, BU_UNIT, BU_STATE_TIN, BU_CONTACT_PERSON_CD, PLANT_SEQ, CONTACT_PERSON_CD, INVOICE_SEQ, INVOICE_NO, "
		           + "INVOICE_DT, FREQ, PERIOD_START_DT, PERIOD_END_DT, DUE_DT, ALLOC_QTY, SALE_PRICE, SALE_PRICE_UNIT, SALE_AMT, "
		           + "EXCHG_RATE_CD, EXCHG_RATE_DT, EXCHG_RATE_VALUE, INVOICE_RAISED_IN, GROSS_AMT, TAX_AMT, TAX_STRUCT_CD, TAX_EFF_DT, "
		           + "INVOICE_AMT, NET_PAYABLE_AMT, INV_STATUS, REMARK_1, REMARK_2, CHECKED_FLAG, CHECKED_BY, CHECKED_DT, AUTHORIZED_FLAG, "
		           + "AUTHORIZED_BY, AUTHORIZED_DT, APPROVED_FLAG, APPROVED_BY, APPROVED_DT, ENT_BY, ENT_DT, EXCHG_RATE_TYPE, MODIFY_BY, "
		           + "MODIFY_DT, TCS_TDS, TCS_AMT, TCS_FACTOR, INVOICE_ID_SEQ, PAY_RECV_AMT, PAY_RECV_DT, PAY_INSERT_BY, PAY_INSERT_DT, "
		           + "PAY_UPDATE_BY, PAY_UPDATE_DT, PAY_REMARK, TDS_GROSS_PERCENT, TDS_GROSS_AMT, TDS_TAX_PERCENT, TDS_TAX_AMT, PDF_INV_DTL, "
		           + "PRINT_BY_ORI, PRINT_DT_ORI, PRINT_BY_TRI, PRINT_DT_TRI, PRINT_BY_DUP, PRINT_DT_DUP, TRANSPORTATION_CHARGE, "
		           + "TRANSPORTATION_AMOUNT, SAP_APPROVAL, SAP_APPROVED_BY,SAP_APPROVED_DT,TCS_STRUCT_CD, TCS_EFF_DT, TDS_STRUCT_CD, TDS_EFF_DT,"
		           + "MARKET_MARGIN, OTHER_CHARGES, MARKET_MARGIN_AMT, OTHER_CHARGES_AMT,CARGO_NO,INV_FLAG,SUG_QTY,SUG_PERCENT,FIN_SYS,HOLD_AMT,"
		           + "REF_NO,CRITERIA,DISCOUNT_DAYS,INT_RATE,IMB_AMT,IMB_QTY,SHIPAY_AMT,SHIPAY_QTY,OVRUN_AMT,OVRUN_QTY) "
		           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
		           + " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
		           + "?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, "
		           + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?, ?, "
		           + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, "
		           + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, ?, ?,  TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, "
		           + "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?,?,?,?, "
		           + " TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
		           + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		stmt1 = conn.prepareStatement(queryString1);
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INVOICE_MST_LP_"+start_end_dt+".csv");
		
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_INVOICE_MST_LP_"+start_end_dt+".csv";
			try (
					
			 BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			 String line = br.readLine();
								
					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,BU_STATE_TIN,BU_CONTACT_PERSON_CD,PLANT_SEQ_NO,CONTACT_PERSON_CD,INV_SEQ,START_DT,END_DT,TIMESTAMP,", conn);
					int inv_seq_no=1;
								
						while ((line = br.readLine()) != null) {
							total_count++; 
							String dt="",financial_year="",contact_person_cd="",mail="",start_dt = "", end_dt = "",inv_id_seq="",due_dt="",pay_recv_dt="",disc_days="";
							agmt_no = ""; agmt_rev = ""; seq_no = "";
							abbr = "";
							cd = "0";
							data = null;
									
							index = 1;
							stmt1 = conn.prepareStatement(queryString1);
									
							for (int i = 0; i < line.split(",").length; i++)
							{	
								data = null;
										
								if (i == 0) {
						    		abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
						    		data = company_cd;
						    	}
						    	else if(i == 1) {
						    		financial_year = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    		data = financial_year;
						    	}
						    	else if (i == 2) {	// Counterparty_Cd
						    		cont_ref = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);

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
						    	else if (i == 3) { //Agmt_no
						    		queryString = "SELECT AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE, BU_UNIT, BU_STATE_TIN, "
						    				+ "PLANT_SEQ, CARGO_NO,TO_CHAR(PAY_RECV_DT, 'DD/MM/YYYY'), TO_CHAR(DUE_DT, 'DD/MM/YYYY') "
						    				+ "FROM FMS_INVOICE_MST WHERE INVOICE_NO = ? AND COMPANY_CD = ? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, cont_ref);
						    		stmt.setString(2, company_cd);
						    		rset = stmt.executeQuery();
						    		if (rset.next()) {
						    			agmt_no = rset.getString(1);
						    			agmt_rev = rset.getString(2);
						    			cont_no = rset.getString(3);
						    			cont_rev = rset.getString(4);
						    			cont_type = rset.getString(5);
						    			bu_seq_no = rset.getString(6);
						    			state_code = rset.getString(7);
						    			plant_seq_no = rset.getString(8);
						    			cargo_no = rset.getString(9);
						    			if(cont_ref.equals("S0016/21-22")){
						    				pay_recv_dt = "12/05/2021";
						    			}else {
						    				pay_recv_dt = rset.getString(10);
						    			}
						    			due_dt = rset.getString(11);
						    		} else {
						    			agmt_no = "";
						    			agmt_rev = "";
						    			cont_no = "";
						    			cont_rev = "";
						    			cont_type = "";
						    			bu_seq_no = "";
						    			state_code = "";
						    			plant_seq_no = "";
						    			cargo_no = "0";
						    			pay_recv_dt = "";
						    			due_dt = "";
						    		}
						    		rset.close();
						    		stmt.close();
						    		
									data = agmt_no;
						    	}
						    	else if(i == 4) {
						    		data = agmt_rev;
						    	}
						    	else if(i == 5) {
						    		data = cont_no;
						    	}
						    	else if(i == 6) {
						    		data = cont_rev;
						    	}
						    	else if(i == 7) {
						    		data = cont_type;
						    	}
						    	else if(i == 8) {
						    		data = bu_seq_no;
						    	}
						    	else if(i == 9) {
						    		data = state_code;
						    	}
						    	else if(i == 10) { //BU_CONTACT_PERSON

						    		queryString="SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD='2' AND COUNTERPARTY_CD='2' "
						    				+ "AND ENTITY='B' AND ADDR_FLAG=? AND INV_FLAG='Y' AND ACTIVE_FLAG='Y' AND ADDR_IS_ACTIVE='Y' ORDER BY CONTACT_PERSON ";
									stmt=conn.prepareStatement(queryString);
									
									
									if(bu_seq_no.equals("1")) {								
										addr_flag = "P1";
									}else if(bu_seq_no.equals("2")){
										addr_flag = "P2";
									}else if(bu_seq_no.equals("3")){
										addr_flag = "P3";
									}
		
									stmt.setString(1, addr_flag);
									rset = stmt.executeQuery();
									
						    		if (rset.next()) {				    			
						    			bu_cont_person_cd=rset.getString(1);
						    		}else {
						    			bu_cont_person_cd ="0";
						    		}	
						    		
						    		rset.close();
						    		stmt.close();
						    		data=bu_cont_person_cd;
						    	}
						    	else if(i == 11) {
						    		data = plant_seq_no;
						    	}
						    	else if(i == 12)
						    	{
						    		mail = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    		
						    		queryString = "SELECT SEQ_NO FROM FMS_ENTITY_CONTACT_MST WHERE EMAIL = ? AND COMPANY_CD= '2' AND COUNTERPARTY_CD = ? ";
											
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, mail);
							    	stmt.setString(2, cd);
							    	rset = stmt.executeQuery();
							    	
							    	if (rset.next()) {
							    		contact_person_cd = rset.getString(1);
							    	}
							    	else {
							    		contact_person_cd = "0";
							    	}
							    	rset.close();
							    	stmt.close();
						    		data=contact_person_cd;
						    	}
						    	else if(i == 13) 
						    	{
						    		int count=0;
						    		queryString = "SELECT MAX(INVOICE_SEQ) "
						    				+ "FROM FMS_INVOICE_MST "
						    				+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? AND BU_STATE_TIN =? ";
						    		stmt = conn.prepareStatement(queryString);
						    		stmt.setString(1, company_cd);
						    		stmt.setString(2, financial_year);
						    		stmt.setString(3, state_code);
						    		rset = stmt.executeQuery();
						    		if(rset.next())
						    		{
						    			count = rset.getInt(1);
						    		}
						    		else
						    		{
						    			count=0;
						    		}
						    		stmt.close();
							    	rset.close();
							    	if(count>0)
							    	{
							    		inv_seq_no = count+1;
							    	}
							    	else
							    	{
							    		inv_seq_no = 1;
							    	}
						    		data=inv_seq_no+"";
						    	}
						    	else if(i == 17) {
						    		
						    		start_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    		
						    		if(cont_type.equals("O") || cont_type.equals("Q")) {
						    			queryString = "SELECT TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY'), TO_CHAR(TO_DATE(ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY') "
							    				+ "FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? AND CARGO_NO = ? AND COMPANY_CD = ?";
							    		stmt = conn.prepareStatement(queryString);
										stmt.setString(1, cd);
										stmt.setString(2, agmt_no);
										stmt.setString(3, agmt_rev);
										stmt.setString(4, cont_no);
										stmt.setString(5, cont_rev);
										stmt.setString(6, cont_type);
										stmt.setString(7, cargo_no);
										stmt.setString(8, company_cd);
										rset = stmt.executeQuery();
										
										if (rset.next()) {
											start_dt = rset.getString(1);
											end_dt = rset.getString(2);
										}
										rset.close();
										stmt.close();
						    		}
						    		else { 
						    			queryString = "SELECT TO_CHAR(START_DT, 'DD/MM/YYYY'), TO_CHAR(END_DT, 'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST"
						    						+ " WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
						    			stmt = conn.prepareStatement(queryString);
										stmt.setString(1, company_cd);
										stmt.setString(2, cd);
										stmt.setString(3, agmt_no);
										stmt.setString(4, agmt_rev);
										stmt.setString(5, cont_no);
										stmt.setString(6, cont_rev);
										stmt.setString(7, cont_type);
										rset = stmt.executeQuery();
										if (rset.next()) {
											start_dt = rset.getString(1);
											end_dt = rset.getString(2);
										}
										rset.close();
										stmt.close();
						    			
						    			
						    		}
						    		data=start_dt+"";
						    	}
						    	else if(i == 18) {
						    		if(!cont_type.equals("O") && !cont_type.equals("Q")) {
						    			end_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    		}
						    		data=end_dt+"";
						    	}
						    	else if(i == 30) {
						    		
						    		String temp_struct = "";
						    		dt = "";
									name = line.split(",")[i].contains("null") ? null : line.split(",")[i].replaceAll("@ ", ", ");
						    
									if(name!= null) {
										if (!name.contains(", ")) {
											queryString = "SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
											stmt = conn.prepareStatement(queryString);
											stmt.setString(1, name);
											rset = stmt.executeQuery();
											if (rset.next()) {
												temp_struct = rset.getString(1);
												dt = rset.getString(2);
											}
											rset.close();
											stmt.close();
										}
							    		else {

											int flag = 0;
											queryString = "SELECT TAX_STR_CD, DESCR, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS')FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
											stmt = conn.prepareStatement(queryString);
											stmt.setString(1, "%"+name.split(", ")[0]+"%");
											rset = stmt.executeQuery();
											
											while (rset.next()) {
												
												for (int j = 0; j < name.split(", ").length; j++) {
													if (rset.getString(2).contains(name.split(", ")[j])) {
														flag = 1;
													}
													else {
														flag = 0;
														break;
													}
												}
												
												if (flag == 1) {
													temp_struct = rset.getString(1);
													name=rset.getString(2);
													dt = rset.getString(3);
													break;
												}
											}
											rset.close();
											stmt.close();
										}
									}
						    		
								   	data = temp_struct;
						    	}
						    	else if(i == 31) {
						    		data = dt;
						    	}
						    	else if(i == 86) {
						    		data = cargo_no;
						    	}
						    	else if( i == 94) {
						    		disc_days = (line.split(",")[i].contains("null") ? "0" : line.split(",")[i]);
						    		float int_days = utilDate.getDays(pay_recv_dt, due_dt)-1-Float.parseFloat(disc_days);
						    		data = int_days + "";
								}
								else {	
									
//									if(i == 17) {
//						    			start_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//						    		}
//						    		if(i == 18) {
//						    			end_dt = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//						    		}
						    		if(i == 54) {
						    			inv_id_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						    		}
								    		
									data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
								}	
//								    System.out.println(index+"-"+data);
								    stmt1.setString(index++, data);		    	
							}
									
								    queryString = "SELECT COUNTERPARTY_CD FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								    		+ "AND FINANCIAL_YEAR = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONT_REV = ? AND "
								    		+ "CONTRACT_TYPE = ? AND BU_UNIT =? AND BU_STATE_TIN = ?  AND PLANT_SEQ = ? AND INVOICE_ID_SEQ = ? "
								    		+ "AND PERIOD_START_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') AND PERIOD_END_DT = TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') "
								    		+ "AND CARGO_NO = ?";
							    	stmt = conn.prepareStatement(queryString);
							    	stmt.setString(1, company_cd);
							    	stmt.setString(2, cd);
							    	stmt.setString(3, financial_year);
							    	stmt.setString(4, agmt_no);
							    	stmt.setString(5, agmt_rev);
							    	stmt.setString(6, cont_no);
							    	stmt.setString(7, cont_rev);
							    	stmt.setString(8, cont_type);
							    	stmt.setString(9, bu_seq_no);
							    	stmt.setString(10, state_code);
							    	stmt.setString(11, plant_seq_no);
							    	stmt.setString(12, inv_id_seq);
							    	stmt.setString(13, start_dt);
							    	stmt.setString(14, end_dt);
							    	stmt.setString(15, cargo_no);
							    	
							    	rset = stmt.executeQuery();
							    	
							    	 if (!rset.next() && !cd.equals("") && !agmt_no.equals("") ) {
											//System.out.println(queryString1);
											
											logger.data(fname, (company_cd+"," + cd + " , " + financial_year + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," + inv_id_seq + ","+start_dt + "," + end_dt + ","), conn, "");
											
											stmt1.executeUpdate();
											stmt1.close();
											
											logger_count++;
									}
							    	 else {
											stmt1.close();
											skipped_count++;     
											logger.data(fname, (company_cd+"," + cd + "," + financial_year + "," + agmt_no + "," + agmt_rev + "," + cont_no + "," + cont_rev + "," + cont_type  + "," + bu_seq_no + "," + state_code + "," + bu_cont_person_cd + "," + plant_seq_no + "," + contact_person_cd + "," + inv_id_seq + ","+ start_dt + "," + end_dt + ","), conn, "E");
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
		
		System.out.println("<<END>><<"+table_name+">>");
		System.out.println();

		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 

		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
		
		logger.checkpoint1(fname1,logger_count+",", conn);
		
	} 
	catch (Exception e)	{			
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
	

public void FMS_INV_TAX_DTL_LP() throws IOException, SQLException {

	function_nm="FMS_INV_TAX_DTL_LP()";
	try {
		
		
		System.out.println("<<START>><<FMS_INV_TAX_DTL_LP>>");
		
		logger.checkpoint(fname, "\n<<START>>,<<FMS_INV_TAX_DTL_LP>>,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;   
		skipped_count = 0;   
		total_count = 0;   
		String tax_struct_cd="",tax_code="",desc="", desc_nm="",adv="";
		
//		String inv_type = "";
//		String ent_by = "0";	// This will contain ID of BIPSUP. Will be inserted 0 if not found any.
//		String[] tax_dtls = new String[5];
		
		columns = "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,"
				+ "TAX_AMT,TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
		
		queryString = "SELECT COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,NULL,TO_CHAR(TAX_EFF_DT, 'dd/mm/yyyy hh24:mi:ss'),"
				+ "NULL,TAX_AMT,GROSS_AMT,ENT_BY, TO_CHAR(ENT_DT, 'dd/mm/yyyy hh24:mi:ss'), MODIFY_BY, "
				+ "TO_CHAR(MODIFY_DT, 'dd/mm/yyyy hh24:mi:ss'),GROSS_AMT"
				+ "	FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? AND INV_FLAG = 'LP' AND CONTRACT_TYPE IN ('O','Q') ";
		stmt = conn.prepareStatement(queryString);
		stmt.setString(1,company_cd);
		rset = stmt.executeQuery();
		
		queryString1 = "INSERT INTO FMS_INV_TAX_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_EFF_DT,TAX_DESCR,TAX_AMT,"
				+ "TAX_BASE_AMT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?,"
				+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		
		logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,TAX_STRUCT_CD,TAX_CODE,TAX_AMT,TIMESTAMP", conn);
		
		while (rset.next()) {
			tax_struct_cd = rset.getString(5);
			String desc1="";
			String count_value="",adv_amt="",gross_amt="";
			
			int count_desc=1;
			double tax_amt = 0.0;
			adv_amt = "";
			
			for(int j=0;j<count_desc;j++) {
				
				queryString2 = "SELECT DESCR, TO_CHAR(APP_DATE,'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE TAX_STR_CD = ? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, tax_struct_cd);
				rset2 = stmt2.executeQuery();
				
				if(rset2.next()) {
					
					stmt1 = conn.prepareStatement(queryString1);
					
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
//								else {
//									desc = null;
//								}
							
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
							
							
							if (desc != null) {
							    if ( !desc.contains("on") ) {
							    	
							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
//							        else {
							            gross_amt = rset.getString(15);
							            tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(gross_amt)) / 100);
							            adv_amt = tax_amt + "";
							            adv=adv_amt;
//							        }
							    }
							    else if (desc.contains("on")) {

							        count_value = desc.split("%")[0];
							        count_value = count_value.split(" ")[1]; 
							        tax_amt = Math.round((Double.parseDouble(count_value) * Double.parseDouble(adv_amt)) / 100);
							           
							    }
							}
							data = tax_amt + "";

						}
						
						else if(i == 9 && desc != null && desc.contains("on")) {

							data = adv;
						}
						
						stmt1.setString(i+1,data);
							
						}
				
				
			//for data already exists..
			queryString5 = "SELECT TAX_AMT FROM FMS_INV_TAX_DTL WHERE COMPANY_CD = ? AND BU_STATE_TIN = ?  AND "
					+ "INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND TAX_STRUCT_CD = ? AND TAX_CODE = ? ";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(1, company_cd);
			stmt5.setString(2, rset.getString(2));
			stmt5.setString(3, rset.getString(3));
			stmt5.setString(4, rset.getString(4));
			stmt5.setString(5, rset.getString(5));
			stmt5.setString(6, tax_code);
			rset5 = stmt5.executeQuery();
			
				if (!rset5.next() ) {
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4)+ "," + tax_struct_cd + " , " + tax_code + " , "+ tax_amt+ " , "), conn, "");
					
					stmt1.executeUpdate();
					stmt1.close();
					logger_count++;
				}
				else {
					
					stmt1.close();
					skipped_count++; 
					
					logger.data(fname, ( company_cd + "," + rset.getString(2) + "," + rset.getString(3) + "," + rset.getString(4)+ "," + tax_struct_cd + " , " + tax_code + " , "+ tax_amt+ " , "), conn, "E");
					
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
		
	
		System.out.println("<<END>><<FMS_INV_TAX_DTL_LP>>");
		System.out.println();
	
		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+","), conn); 
		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+","), conn); 
		
		logger.checkpoint(fname, "<<END>>,<<FMS_INV_TAX_DTL_LP>>,", conn);
		
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

public void FMS_INV_FILE_DTL_LP() throws IOException, SQLException {
	function_nm="FMS_INV_FILE_DTL_LP()";
	try {
		
		table_name = "FMS_INV_FILE_DTL_LP";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr,pdf_type,file_nm,sign_by,sign_by_cd;
		
		queryString1 = "INSERT INTO FMS_INV_FILE_DTL(COMPANY_CD,FINANCIAL_YEAR,BU_STATE_TIN,INVOICE_SEQ,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,"
				+ "PDF_SIGNED,SIGNED_BY,SIGNED_DT,SIGNED_ENT_BY,PDF_CONTENT,SF_GEN_DT,EMAIL_SENT,EMAIL_SENT_BY,EMAIL_SENT_DT) "
				+ "VALUES(?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
				+ "?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		stmt1 = conn.prepareStatement(queryString1);
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INV_FILE_DTL_LP_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_INV_FILE_DTL_LP_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	
				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String new_inv="";
					abbr = "";
					bu_seq="";inv_seq="";fin_yr="";pdf_type="";file_nm="";sign_by="";sign_by_cd="";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < line.split(",").length; i++)
					{
//					cell = cellIterator.next();
						data = null;
						if(i == 0) {
							abbr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							
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
				    		
							data = company_cd;
						}
						else if(i == 1) {
							fin_yr = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							data = fin_yr;
						}
						else if(i == 2) {
							inv_seq = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN FROM FMS_INVOICE_MST WHERE INVOICE_ID_SEQ = ? AND FINANCIAL_YEAR = ? AND COUNTERPARTY_CD = ? AND INV_FLAG = 'LP' AND COMPANY_CD = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, inv_seq);
							stmt.setString(2, fin_yr);
							stmt.setString(3, cd);
							stmt.setString(4, company_cd);
							
							rset = stmt.executeQuery();
							if (rset.next()) {
								seq_no = rset.getString(1);
								bu_seq = rset.getString(2);
							} else {
								seq_no ="0";
								bu_seq ="0";
							}
							rset.close();
							stmt.close();
							
							data = bu_seq;
						}
						
						else if(i == 3) {
							data = seq_no;
						}
						else if (i == 14) {	// SIGNED_ENT_BY
							if(sign_by!=null) {
								if(sign_by.contains("@")) {
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMAIL_ID) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sign_by.toUpperCase());
									rset = stmt.executeQuery();
									if (rset.next()) {
										sign_by_cd = rset.getString(1);
									} else {
										sign_by_cd ="0";
									}
									rset.close();
									stmt.close();
								}
								else {
									queryString = "SELECT EMP_CD FROM FMS_EMP_MST WHERE UPPER(EMP_NM) = ? ";
									stmt = conn.prepareStatement(queryString);
									stmt.setString(1, sign_by.toUpperCase());
									rset = stmt.executeQuery();
									if (rset.next()) {
										sign_by_cd = rset.getString(1);
									} else {
										sign_by_cd ="0";
									}
									rset.close();
									stmt.close();
								}
							}
							else {
								sign_by_cd = "0";
							}
							data = sign_by_cd;
						}
						
						
						else {
							 if (i == 4) {	// PDF_TYPE
								pdf_type = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 5) {	//FILE_NAME
								file_nm = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							else if(i == 11) {	//SIGNED_BY
								sign_by = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//				    	if(data != null) {
//				    		data = data.substring(1, data.length()-1);
//				    	}
						}
						

//						System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					
					queryString = "SELECT COMPANY_CD FROM FMS_INV_FILE_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND PDF_TYPE = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, seq_no);
					stmt.setString(4, fin_yr);
					stmt.setString(5, pdf_type);
					rset = stmt.executeQuery();
					
					if (!rset.next() || rset.getString(1) == null){
						logger.data(fname, (company_cd + "," + bu_seq + "," + seq_no + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + seq_no + "," + fin_yr + "," + pdf_type + "," + file_nm  + "," ), conn, "E");
					}
					
					rset.close();
					stmt.close();
				}
				br.close();
			}
			
//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
			
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

//public void FMS_INV_ADV_DTL_ADD() throws IOException, SQLException {
//	function_nm="FMS_INV_ADV_DTL_ADD()";
//	try {
//		
//		table_name = "FMS_INV_ADV_DTL_ADD";
//		System.out.println("<<START>><<"+table_name+">>");
//		
//		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
//		
//		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
//		
//		data = "";
//		logger_count = 0;
//		skipped_count = 0;
//		total_count = 0;
//		String bu_seq,inv_seq,fin_yr;
//		String storage = "";
//		
//		queryString1 = "INSERT INTO FMS_INV_ADV_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,SEC_INT_REF,INV_COMPONENT,AMOUNT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
//				+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
//		
//		file1 = new File(migration_setup_dir + "EXPORT/FMS_INV_ADV_DTL_ADD_"+start_end_dt+".csv");
//		if(file1.exists()) {
//			
//			String fileName = migration_setup_dir + "EXPORT/FMS_INV_ADV_DTL_ADD_"+start_end_dt+".csv";
//			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	
//
//				String line = br.readLine();
//				
//				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
//				
//				while ((line = br.readLine()) != null) {
//					String  compo="",sec_int="",plant_seq="",tds_cd="",tds_amt="",tds_per="";
//					double tax_amt = 0,gross_amt=0;
//					abbr = "";
//					bu_seq="";inv_seq="";fin_yr="";
//					String sec_ref="";
//					cd = "0";
//					data = null;
//					
//					index = 1;
//					stmt1 = conn.prepareStatement(queryString1);
//					
//					for (int i = 0; i < 11; i++)
//					{
////					cell = cellIterator.next();
//						data = null;
//						if(i == 0) {
//							fin_yr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
//							data = company_cd;
//						}
//						else if(i == 1) {
//							seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							
//							queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,GROSS_AMT,TAX_AMT FROM FMS_INVOICE_MST WHERE INVOICE_NO = ? AND FINANCIAL_YEAR = ? AND COMPANY_CD = '2' ";
//							stmt = conn.prepareStatement(queryString);
//							stmt.setString(1, seq_no);
//							stmt.setString(2, fin_yr);
//							rset = stmt.executeQuery();
//							if (rset.next()) {
//								inv_seq = rset.getString(1);
//								bu_seq = rset.getString(2);
//								cd = rset.getString(3);
//								agmt_no = rset.getString(4);
//								agmt_rev = rset.getString(5);
//								cont_no = rset.getString(6);
//								cont_rev = rset.getString(7);
//								cont_type = rset.getString(8);
//								gross_amt = rset.getDouble(9);
//								tax_amt = rset.getDouble(10);
//								
//							} else {
//								inv_seq ="0";
//								bu_seq ="0";
//								cd = "0";
//								agmt_no = "0";
//								agmt_rev = "0";
//								cont_no = "0";
//								cont_rev = "0";
//								cont_type = "0";
//								gross_amt = 0;
//								tax_amt = 0;
//							}
//							rset.close();
//							stmt.close();
//							
//							data = bu_seq;
//						}
//						else if(i == 2) {
//							data = inv_seq;
//						}
//						else if(i == 3) {
//							data = fin_yr;
//						}
//						else {
//							if (i == 4) {	// PDF_TYPE
//								sec_int = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							}
//							else if(i == 5) {	//FILE_NAME
//								compo = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//							}
//							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
//						}
//						
//				    	System.out.println(index+" : >>>"+data);
//						stmt1.setString(index++, data);
//						
//					}
//					queryString = "SELECT COMPANY_CD FROM FMS_INV_ADV_DTL WHERE "
//							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND SEC_INT_REF = ? AND INV_COMPONENT = ? ";
//					stmt = conn.prepareStatement(queryString);
//					stmt.setString(1, company_cd);
//					stmt.setString(2, bu_seq);
//					stmt.setString(3, inv_seq);
//					stmt.setString(4, fin_yr);
//					stmt.setString(5, sec_int);
//					stmt.setString(6, compo);
//					rset = stmt.executeQuery();
//					
//					if (!rset.next()){
//						
//						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + sec_int + "," + compo  + "," ), conn, "");
//						
//						stmt1.executeUpdate();
//						stmt1.close();
//						
//						//INSERT SECURITY
////						if(!storage.contains(sec_int)) 
//						{
//							
//							Map<String, Integer> seq = new HashMap<String, Integer>();
//
//							columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT";
//							
//							queryString4 = "SELECT COMPANY_CD,COUNTERPARTY_CD,NULL,'0',NULL,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,"
//									+ "TO_CHAR(ENT_DT,'DD/MM/YYYY hh24:mi:ss'),NULL,NULL,NULL,NULL,'0',NULL,'K',NULL,ADV_ADJUST "
//									+ "FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = ? AND ADV_ADJUST = 'Y' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? AND AGMT_REV = ? AND CONT_REV = ? AND CONTRACT_TYPE = ? ";
//							stmt4 = conn.prepareStatement(queryString4);
//							stmt4.setString(1,company_cd);
//							stmt4.setString(2,cd);
//							stmt4.setString(3,agmt_no);
//							stmt4.setString(4,cont_no);
//							stmt4.setString(5,agmt_rev);
//							stmt4.setString(6,cont_rev);
//							stmt4.setString(7,cont_type);
//							
//							rset4 = stmt4.executeQuery();
//							
//							queryString3 = "INSERT INTO FMS_SECURITY_DEAL_MAP(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,MAP_SEQ_NO,SEC_REF_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,APRV_BY,APRV_DT,SEQ_REV_NO,ENTITY_CD,GX,SHARE_PERCENT) "
//									+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,?,?,?)";
//							
////							logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
//							
//							while (rset4.next()) {
//									stmt3 = conn.prepareStatement(queryString3);
//									
////									cd= rset4.getString(2);
////									agmt_no = rset4.getString(6);
////									cont_no = rset4.getString(8);
//									
//									
//									if(!seq.containsKey(cd+agmt_no+cont_no)) {
//										queryString2 = "SELECT A.SEQ_NO,A.SEC_REF_NO,B.COUNTERPARTY_ABBR FROM FMS_SECURITY_DEAL_MAP A, FMS_COUNTERPARTY_MST B "
//												+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND "
//												+ "A.SEQ_NO = (SELECT MAX(C.SEQ_NO) FROM FMS_SECURITY_DEAL_MAP C "
//												+ "WHERE A.COMPANY_CD = C.COMPANY_CD AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD)";
//										stmt2 = conn.prepareStatement(queryString2);
//										stmt2.setString(1, company_cd);
//										stmt2.setString(2, cd);
//										rset2 = stmt2.executeQuery();
//										if(rset2.next())
//										{
//											seq.put(cd+agmt_no+cont_no, rset2.getInt(1));
//											
////											sec_ref = rset2.getString(2);
//											abbr = rset2.getString(3);
//											
//										}
//										else {
//											seq.put(cd+agmt_no+cont_no, 0);
//											sec_ref = "-S-";
//										}
//										rset2.close();
//										stmt2.close();
//									}
//									
//									for(int i = 0;i < columns.split(",").length;i++) {
//										data = "";
//										data = rset4.getString(i+1) == null ? "" : rset4.getString(i+1);
//										
//										if(i == 2) {
//											seq_no = seq.get(cd+agmt_no+cont_no)+1 +"";
//											data = seq_no;
//											seq.put(cd+agmt_no+cont_no, Integer.parseInt(data));
//											
//										}
//										else if(i == 4) {
////											data = sec_ref+"-"+seq_no ;
//											sec_ref = abbr+"-S-"+seq_no;
//											data = sec_ref;
//										}
//										
////										System.out.println(i+1+"=="+data);
//										
//											stmt3.setString(i+1,data);
//									}
//									
//								//for data already exists..
//								queryString5 = "SELECT SEQ_NO FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND MAP_SEQ_NO = ? AND SEQ_REV_NO = ?  AND GX = ? ";
//								stmt5 = conn.prepareStatement(queryString5);
//								stmt5.setString(1, company_cd);
//								stmt5.setString(2, cd);
//								stmt5.setString(3, seq_no);
//								stmt5.setString(4, rset4.getString(4));
//								stmt5.setString(5, rset4.getString(17));
//								stmt5.setString(6, rset4.getString(19));
//							
//								rset5 = stmt5.executeQuery();
//								
//								if (!rset5.next() ) {
//									
//									logger.data(fname, ( company_cd + "," + cd + "," + seq_no + "," + rset4.getString(4) + "," + rset4.getString(17) + "," + rset4.getString(19) + "," + rset4.getString(19) + " , " ), conn, "");
//									
//									stmt3.executeUpdate();
//									storage += sec_int +",";
//									stmt3.close();
//									
//									
//								}
//								else {
//									
//									stmt3.close();
//									skipped_count++; 
//
//									seq.put(cd+agmt_no+cont_no, seq.get(cd)-1);
//									
//									logger.data(fname, ( company_cd + "," + cd + "," + seq_no + "," + rset4.getString(4) + "," + rset4.getString(17) + "," + rset4.getString(19) + "," + rset4.getString(19) + " , " ), conn, "E");
//									
//								}
//								stmt5.close();
//								rset5.close();
//								}
//							rset4.close();
//							stmt4.close();
//							
//							//FOR ADV ADJUST SECURITY MST
//							String numvalue = "", gross = "", tax_per = "";
//							double val = 0;
//							double tds_amt1 = 0;
//							columns = "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,CURRENCY,VARIATION_VALUE,VALUE,"
//									+ "VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,RECEIPT_DT,ISSUE_DT,"
//									+ "EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,"
//									+ "APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,"
//									+ "PG_REF,CR_DR,TDS_AMT,TDS_STRUCT_CD";
//							
//							queryString4 = "SELECT COMPANY_CD,COUNTERPARTY_CD,NULL,NULL,'R','ADV','LTCORA',NULL,ADV_ADJUST_UNIT,NULL,ADV_ADJUST_AMOUNT, "
//									+ "NULL,NULL,NULL,NULL ,NULL,NULL,NULL, NULL, NULL,NULL, NULL, NULL,NULL,NULL,'O', NULL, NULL, 'Y',ENT_BY, "
//									+ "TO_CHAR(ENT_DT, 'DD/MM/YYYY HH24:MI:SS'),NULL, NULL,NULL, NULL,'0', 'K',NULL, NULL, NULL, NULL, NULL, NULL, "
//									+ "NULL ,'CR',NULL,NULL,NULL,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT, 'DD/MM/YYYY HH24:MI:SS'),ADV_ADJUST_AMOUNT "
//									+ "FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = ? AND ADV_ADJUST = 'Y' AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? AND AGMT_REV = ? AND CONT_REV = ? AND CONTRACT_TYPE = ?";
//							stmt4 = conn.prepareStatement(queryString4);
//							stmt4.setString(1,company_cd);
//							stmt4.setString(2,cd);
//							stmt4.setString(3,agmt_no);
//							stmt4.setString(4,cont_no);
//							stmt4.setString(5,agmt_rev);
//							stmt4.setString(6,cont_rev);
//							stmt4.setString(7,cont_type);
//							rset4 = stmt4.executeQuery();
//							
//							queryString3 = "INSERT INTO FMS_SECURITY_MST(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEC_REF_NO,SEC_CATEGORY,SEC_TYPE,DEAL_TYPE,GUARANTOR_CD,"
//									+ "CURRENCY,VARIATION_VALUE,VALUE,VALUE_FLUC,ISS_BANK_CD,ISS_BANK_REF,ADV_BANK_CD,ADV_BANK_REF,CONFIRM_BANK_CD,CONFIRM_BANK_REF,"
//									+ "RECEIPT_DT,ISSUE_DT,EXPIRE_DT,RENEW_DT,CANCEL_DT,TENOR,REVIEW_DT,STATUS,REMARKS,FLAG,INORDER_HIST,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,"
//									+ "APRV_DT,APRV_BY,SEQ_REV_NO,GX,SAP_APPROVAL,SAP_APPROVED_BY,SAP_APPROVED_DT,SAP_REVERSAL,SAP_REVERSAL_BY,SAP_REVERSAL_DT,PG_REF,CR_DR,"
//									+ "TAX_AMT,TAX_STRUCT_CD,TDS_STRUCT_CD,TDS_AMT,BU_UNIT,PLANT_SEQ,GROSS_AMT) "
//									+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), "
//									+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),"
//									+ "?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?, ?, ?, ?, "
//									+ "TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?, ?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'), ?,?,?,?,?,?,?,?,?)";
//							
////							logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,BUY_SALE,AGMT_TYPE,AGMT_NO,AGMT_REV,PLANT_SEQ_NO", conn);
//							
//							while (rset4.next()) {
//									stmt3 = conn.prepareStatement(queryString3);
//									
//									
//									for(int i = 0;i < columns.split(",").length;i++) {
//										data = "";
//										data = rset4.getString(i+1) == null ? "" : rset4.getString(i+1);
//										
//										if(i == 2) {
//											data = seq_no;
//										}
//										else if(i == 3) {
//											data = sec_ref;
//										}
//										else if(i == 9) {
//											tax_per = (line.split(",")[i].contains("null") || line.split(",")[i].contains("@")) ? "1" : line.split(",")[i];
//											data = null;
//										}
//										else if(i == 10) {
//											data = rset4.getString(55);
////											double gross_inr = Double.valueOf(gross);
////											gross = (rset4.getDouble(55) - (rset4.getDouble(55) * (Integer.parseInt(tax_per)/(100 + Integer.parseInt(tax_per)))))+"";
//											gross = BigDecimal.valueOf(rset4.getDouble(55)).subtract(BigDecimal.valueOf(rset4.getDouble(55)).multiply(new BigDecimal(tax_per).divide(BigDecimal.valueOf(100).add(new BigDecimal(tax_per)), 10, BigDecimal.ROUND_HALF_UP))).toPlainString();
//										}
//										else if(i == 18) {
//											data = rset4.getString(54);
//										}
//										else if(i == 45) {
//											for(int j = 11; j < line.split(",").length; j++) {
//												data = null;
//												data = line.split(",")[j].contains("null") ? null : line.split(",")[j];
//												
//												if (j == 11) {
//													data = BigDecimal.valueOf(rset4.getDouble(55)).subtract(new BigDecimal(gross)).toPlainString();
//												}
//												else if(j == 12) {
//													queryString = "SELECT TAX_STR_CD FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
//													stmt = conn.prepareStatement(queryString);
//													stmt.setString(1, data.replaceAll("@", ","));
//													rset = stmt.executeQuery();
//													
//													if (rset.next()) {
//														data = rset.getString(1);
//							
//													}
//													rset.close();
//													stmt.close();
//												}
//												else if(j == 13) {
//													String  percentage="";
//													tds_per = line.split(",")[j].contains("null") ? null : line.split(",")[j];
//													String[] parts = tds_per.split(" ");
//													percentage = parts[1];
//													numvalue=percentage.replaceAll("%", "");
//													val = Double.valueOf(numvalue);
//													
//													queryString = "SELECT TAX_STR_CD  "
//															+ "FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
//													stmt = conn.prepareStatement(queryString);
//													stmt.setString(1,tds_per);
//													rset = stmt.executeQuery();
//													if(rset.next()) {
//														tds_cd = rset.getString(1);
//													}
//													else {
//														tds_cd = null;
//													}
//													rset.close();
//													stmt.close();
//													
//													data = tds_cd;
//												}
//												else if(j == 14) {
//													tds_amt1 = rset4.getDouble(55) * val /  100 ;
//													data = tds_amt1+"";
//												}
//												else if(j == 17) {
////													double gross = rset4.getDouble(55) - tds_amt1 ;
//													data = gross;
//												}
//												stmt3.setString(++i,data);
//											}
//											i--;
//										}
////										System.out.println(i+1+"=="+data);
//										stmt3.setString(i+1,data);
//									}
//									
//								//for data already exists..
//								queryString5 = "SELECT SEQ_NO FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND SEQ_NO = ? AND SEQ_REV_NO = '0' AND GX = 'K' ";
//								stmt5 = conn.prepareStatement(queryString5);
//								stmt5.setString(1, company_cd);
//						    	stmt5.setString(2, cd);
//						    	stmt5.setString(3, seq_no);
////						    	stmt5.setString(4, seq_rev_no);
////						    	stmt5.setString(4, gx);
//							
//								rset5 = stmt5.executeQuery();
//								
//								if (!rset5.next() ) {
//									
//									logger.data(fname, ( company_cd + "," + cd + "," + seq_no + "," + rset4.getString(4) + "," + rset4.getString(17) + "," + rset4.getString(19) + "," + rset4.getString(19) + " , " ), conn, "");
//									
//									stmt3.executeUpdate();
//									stmt3.close();
//									
////									logger_count++;
//								}
//								else {
//									
//									stmt3.close();
//									skipped_count++; 
//
////									seq.put(cd, seq.get(cd)-1);
//									
//									logger.data(fname, ( company_cd + "," + cd + "," + seq_no + "," + rset4.getString(4) + "," + rset4.getString(17) + "," + rset4.getString(19) + "," + rset4.getString(19) + " , " ), conn, "E");
//									
//								}
//								stmt5.close();
//								rset5.close();
//								}
//							rset4.close();
//							stmt4.close();
//						}
//						
////						stmt1.close();
//						
//						logger_count++;
//					}
//					else {
//						stmt1.close();
//						skipped_count++;     
//						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + sec_int + "," + compo  + "," ), conn, "E");
//					}
//					
//					rset.close();
//					stmt.close();
//				}
//				br.close();
//			}
//			
////			workbook = new XSSFWorkbook(file);
////			sheet = workbook.getSheetAt(0);
//			
//			msg = "Data has been Inserted Successfully in Database.";
//			msg_type = "S";				
//		}
//		else {
//			msg = "Excel File not found while Execution. Program Terminated.";
//			msg_type = "E";
//		}
//		//conn.commit();
//		System.out.println("<<END>><<"+table_name+">>");
//		System.out.println();
//		
//		logger.checkpoint(fname, ("\nTOTAL DATA IN EXCEL : ,"+total_count+",,,,,,"), conn); 
//		
//		logger.checkpoint(fname, ("\nTOTAL DATA INSERTED : ,"+logger_count+",,,,,,"), conn); 
//		
//		logger.checkpoint(fname, ("\nTOTAL SKIPPED DATA ,"+skipped_count+",,,,,,"), conn); 
//		
//		logger.checkpoint(fname, "<<END>>,<<"+table_name+">>,,,,,,", conn);
//		
//		logger.checkpoint1(fname1,logger_count+",", conn);
//		
//	}
//	catch(Exception e)
//	{
//		
//		msg = "One of the Functions faced an Error. Data Not Inserted.";
//		msg_type = "E";
//		
//		conn.rollback();
//		new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//		logger.error(fname, e, function_nm, conn, fname_error);
//	}
//	finally {
//		conn.commit();
//		if (file != null) {
//			file.close();
//		}
//	}
//	
//}

public void FMS_INV_ADV_DTL_UPDATE() throws IOException, SQLException {
	function_nm="FMS_INV_ADV_DTL_UPDATE()";
	try {
		
		table_name = "FMS_INV_ADV_DTL";
		System.out.println("<<START>><<"+table_name+">>");
		
		logger.checkpoint(fname, "\n<<START>>,<<"+table_name+">>,,", conn);
		
		logger.checkpoint1(fname1,function_nm+"R"+","+start_end_dt+",", conn);
		
		data = "";
		logger_count = 0;
		skipped_count = 0;
		total_count = 0;
		String bu_seq,inv_seq,fin_yr;
		String storage = "";
		
		queryString1 = "INSERT INTO FMS_INV_ADV_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,SEC_INT_REF,INV_COMPONENT,AMOUNT,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT) "
				+ "VALUES(?,?,?,?,?,?,?,?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'),?,TO_DATE(?, 'DD/MM/YYYY hh24:mi:ss'))";
		
		file1 = new File(migration_setup_dir + "EXPORT/FMS_INV_ADV_DTL_UPDATE_"+start_end_dt+".csv");
		if(file1.exists()) {
			
			String fileName = migration_setup_dir + "EXPORT/FMS_INV_ADV_DTL_UPDATE_"+start_end_dt+".csv";
			try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {	

				String line = br.readLine();
				
				logger.checkpoint(fname, "COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,TIMESTAMP,", conn);
				
				while ((line = br.readLine()) != null) {
					String  compo="",sec_int="",plant_seq="",tds_cd="",tds_amt="",tds_per="";
					double tax_amt = 0,gross_amt=0;
					abbr = "";
					bu_seq="";inv_seq="";fin_yr="";
					String sec_ref="";
					cd = "0";
					data = null;
					
					index = 1;
					stmt1 = conn.prepareStatement(queryString1);
					
					for (int i = 0; i < 11; i++)
					{
//					cell = cellIterator.next();
						data = null;
						if(i == 0) {
							fin_yr = (line.split(",")[i].contains("'null'") ? "NULL" : line.split(",")[i]);
							data = company_cd;
						}
						else if(i == 1) {
							seq_no = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							
							queryString = "SELECT INVOICE_SEQ,BU_STATE_TIN,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,GROSS_AMT,TAX_AMT FROM FMS_INVOICE_MST WHERE INVOICE_NO = ? AND FINANCIAL_YEAR = ? AND COMPANY_CD = '2' ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, seq_no);
							stmt.setString(2, fin_yr);
							rset = stmt.executeQuery();
							if (rset.next()) {
								inv_seq = rset.getString(1);
								bu_seq = rset.getString(2);
								cd = rset.getString(3);
								agmt_no = rset.getString(4);
								agmt_rev = rset.getString(5);
								cont_no = rset.getString(6);
								cont_rev = rset.getString(7);
								cont_type = rset.getString(8);
								gross_amt = rset.getDouble(9);
								tax_amt = rset.getDouble(10);
								
							} else {
								inv_seq ="0";
								bu_seq ="0";
								cd = "0";
								agmt_no = "0";
								agmt_rev = "0";
								cont_no = "0";
								cont_rev = "0";
								cont_type = "0";
								gross_amt = 0;
								tax_amt = 0;
							}
							rset.close();
							stmt.close();
							
							data = bu_seq;
						}
						else if(i == 2) {
							data = inv_seq;
						}
						else if(i == 3) {
							data = fin_yr;
						}
						else {
							if (i == 4) {	// PDF_TYPE
								sec_int = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							else if(i == 5) {	//FILE_NAME
								compo = line.split(",")[i].contains("null") ? null : line.split(",")[i];
							}
							data = line.split(",")[i].contains("null") ? null : line.split(",")[i];
						}
						
//				    	System.out.println(index+" : >>>"+data);
						stmt1.setString(index++, data);
						
					}
					queryString = "SELECT COMPANY_CD FROM FMS_INV_ADV_DTL WHERE "
							+ "COMPANY_CD = ? AND BU_STATE_TIN = ? AND INVOICE_SEQ = ? AND FINANCIAL_YEAR = ? AND SEC_INT_REF = ? AND INV_COMPONENT = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.setString(2, bu_seq);
					stmt.setString(3, inv_seq);
					stmt.setString(4, fin_yr);
					stmt.setString(5, sec_int);
					stmt.setString(6, compo);
					rset = stmt.executeQuery();
					
					if (!rset.next()){
						
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + sec_int + "," + compo  + "," ), conn, "");
						
						stmt1.executeUpdate();
						stmt1.close();
						logger_count++;
					}
					else {
						stmt1.close();
						skipped_count++;     
						logger.data(fname, (company_cd + "," + bu_seq + "," + inv_seq + "," + fin_yr + "," + sec_int + "," + compo  + "," ), conn, "E");
					}
					
					rset.close();
					stmt.close();
				}
				br.close();
			}
			
//			workbook = new XSSFWorkbook(file);
//			sheet = workbook.getSheetAt(0);
			
			msg = "Data has been Inserted Successfully in Database.";
			msg_type = "S";				
		}
		else {
			msg = "Excel File not found while Execution. Program Terminated.";
			msg_type = "E";
		}
		//conn.commit();
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
	
	public void setPdf_path(String path) {
		pdf_path = path;
	}
	
	public void setSysDateTime(String dt) {
		sysDateTime = dt;
	}
	
	public void setStart_End_Dt(String dt) {
		start_end_dt = dt;
	}

}

