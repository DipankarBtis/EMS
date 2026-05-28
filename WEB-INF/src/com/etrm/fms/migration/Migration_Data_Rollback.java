package com.etrm.fms.migration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;

public class Migration_Data_Rollback {

	
	String db_src_file_name = "Migration_Data_Rollback.java";
	String migration_setup_dir = "";
	String sysDateTime = "";
	String checked_values = "", msg = "", msg_type = "", checked_id = "";
	
	final String company_cd = "2";
	
	String fname ="";
	String fname_error = "";
	String fname1 = "";

	String function_nm = "";
	String start_end_dt = null;
	
	String queryString = "";
	PreparedStatement stmt;
	Connection conn;
	
	
	int logger_count = 0;
	
	DataMigration_Logger logger = new DataMigration_Logger();

	public void init() {
		function_nm = "init()";
		try {

			fname= "DataLogs/RollBack/Migration_Data_Rollback(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/RollBack/Migration_Data_Rollback_Error(log)"+sysDateTime+".csv";
			
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
	    		preferences.put("Flag", "0");
	    		conn = ds.getConnection();
	    		
				if (conn != null && !checked_values.equals(""))
				{
					conn.setAutoCommit(false);
					
					if (checked_id.contains("MASTER")) {
						MASTER();
					}
					if (checked_id.contains("PURCHASE")) {
						PURCHASE();
					}
					if (checked_id.contains("SALES")) {
						SALES();
					}
					if (checked_id.contains("TRANSPORT")) {
						TRANSPORT();
					}
					if (checked_id.contains("TERMINAL OPERATIONS")) {
						TERMINAL_OPERATIONS();
					}
					if (checked_id.contains("RISK MGMT")) {
						RISK_MGMT();
					}
					if (checked_id.contains("DLNG")) {
						DLNG();
					}
					if (checked_id.contains("DERIVATIVES")) {
						DERIVATIVES();
					}
					if (checked_id.contains("INTERFACE")) {
						INTERFACE();
					}
					if (checked_id.contains("OTHER INVOICE")) {
						OTHER_INVOICE();
					}
					
		    		preferences.put("Flag", "1");
				}
				else {
					msg = "No Checkbox was selected. RollBack Terminated.";
					msg_type = "E";
				}
	    	}
	    	
		}catch (Exception e) {
			
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
			
		}
		finally{
			
			try{
				if (stmt != null) {try {stmt.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if (conn != null) {try {conn.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			
			catch (Exception e){
			    	new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					
					msg = "One of the Functions faced an Error. RollBack Terminated.";
					msg_type = "E";
			    }
			
		     }

	}

	public void MASTER() throws SQLException, IOException {
		function_nm = "MASTER()";
		try {
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<MASTER>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<MASTER>>,", conn);
			
			logger.checkpoint1(fname1,"\n"+function_nm+"D"+","+start_end_dt+",", conn);
			
			if (checked_values.contains("FMS_COUNTERPARTY_MST")) {
				
				queryString = "DELETE FROM FMS_COUNTERPARTY_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_ENTITY_REQ_DTL")) {
				
				queryString = "DELETE FROM FMS_ENTITY_REQ_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
					
			if (checked_values.contains("FMS_SECTOR_MST")) {
			//	FMS_SECTOR_MST();
				queryString = "DELETE FROM FMS_SECTOR_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
		    //	FMS_SECTOR_DTL();
				queryString = "DELETE FROM FMS_SECTOR_DTL WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_ENTITY_ADDR_MST")) {
		    //	FMS_ENTITY_ADDR_MST();
				queryString = "DELETE FROM FMS_ENTITY_ADDR_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_COMPANY_OWNER_ADDR_MST")) {
		    //	FMS_COMPANY_OWNER_ADDR_MST();
				queryString = "DELETE FROM FMS_COMPANY_OWNER_ADDR_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_COUNTERPARTY_PLANT_DTL")) {
		    //	FMS_COUNTERPARTY_PLANT_DTL();
				queryString = "DELETE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_COUNTERPARTY_BU_DTL"))
			{
			//	FMS_COUNTERPARTY_BU_DTL();
				queryString = "DELETE FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if(checked_values.contains("FMS_INT_PAY_RATE_ENTRY"))
			{
			//	FMS_INT_PAY_RATE_ENTRY();	
				queryString = "DELETE FROM FMS_INT_PAY_RATE_ENTRY WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if(checked_values.contains("FMS_EXCHG_RATE_ENTRY"))
			{
			//	FMS_EXCHG_RATE_ENTRY();
				queryString = "DELETE FROM FMS_EXCHG_RATE_ENTRY WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if(checked_values.contains("FMS_BANK_MST"))
			{
			//	FMS_BANK_MST();
				queryString = "DELETE FROM FMS_BANK_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if(checked_values.contains("FMS_ENTITY_TURNOVER_DTL"))
			{
			//	FMS_ENTITY_TURNOVER_DTL();
				queryString = "DELETE FROM FMS_ENTITY_TURNOVER_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_SHIP_MST")) {
		    //	FMS_SHIP_MST();
				queryString = "DELETE FROM FMS_SHIP_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}

			if (checked_values.contains("FMS_ENTITY_CONTACT_MST")) {
		    //	FMS_ENTITY_CONTACT_MST();
				queryString = "DELETE FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_METER_MST")) {
		    //	FMS_METER_MST();
				queryString = "DELETE FROM FMS_METER_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_COUNTERPARTY_PLANT_TAX")) {
		    //	FMS_COUNTERPARTY_PLANT_TAX();
				queryString = "DELETE FROM FMS_COUNTERPARTY_PLANT_TAX WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_COUNTERPARTY_BU_TAX")) {
			//	FMS_COUNTERPARTY_BU_TAX();
				queryString = "DELETE FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_ENTITY_TAX_STRUCT_DTL")) {
		    //	FMS_ENTITY_TAX_STRUCT_DTL();
				queryString = "DELETE FROM FMS_ENTITY_TAX_STRUCT_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_ENTITY_SERVICE_TAX_DTL")) {
		    //	FMS_ENTITY_SERVICE_TAX_DTL();
				queryString = "DELETE FROM FMS_ENTITY_SERVICE_TAX_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_ENTITY_BU_SVC_TAX_DTL")) {
		    //	FMS_ENTITY_BU_SVC_TAX_DTL();
				queryString = "DELETE FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if (checked_values.contains("FMS_CUSTOM_TAX_STRUCT_DTL")) {
		    //	FMS_CUSTOM_TAX_STRUCT_DTL();
				queryString = "DELETE FROM FMS_CUSTOM_TAX_STRUCT_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_HOLIDAY_DTL")) {
		    //	FMS_HOLIDAY_DTL();
				queryString = "DELETE FROM FMS_HOLIDAY_DTL WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_PRODUCT_MST")) {
		    //	FMS_PRODUCT_MST();
				queryString = "DELETE FROM FMS_PRODUCT_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if (checked_values.contains("FMS_PRODUCT_MOLECULE_MST")) {
		    //	FMS_PRODUCT_MOLECULE_MST();
				queryString = "DELETE FROM FMS_PRODUCT_MOLECULE_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
					
			if (checked_values.contains("FMS_SAC_MST")) {
		    //	FMS_SAC_MST();
				queryString = "DELETE FROM FMS_SAC_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if (checked_values.contains("FMS_ENTITY_TCS_TDS_MST")) {
		    //	FMS_ENTITY_TCS_TDS_MST();
				queryString = "DELETE FROM FMS_ENTITY_TCS_TDS_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			System.out.println("<<ROLLBACK_END>><<MASTER>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<MASTER>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}
	
	
	public void PURCHASE() throws SQLException, IOException {
		function_nm = "PURCHASE()";
		try {
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<PURCHASE>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<PURCHASE>>,", conn);
			
			logger.checkpoint1(fname1,"\n"+function_nm+"D"+","+start_end_dt+",", conn);
			
			if(checked_values.contains("FMS_TRADER_AGMT_MST,")) {
				// FMS_TRADER_AGMT_MST
				queryString = "DELETE FROM FMS_TRADER_AGMT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if(checked_values.contains("FMS_TRADER_AGMT_PLANT,")) {
				// FMS_TRADER_AGMT_PLANT
				queryString = "DELETE FROM FMS_TRADER_AGMT_PLANT WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if(checked_values.contains("FMS_TRADER_AGMT_BU,")) {
				// FMS_TRADER_AGMT_BU
				queryString = "DELETE FROM FMS_TRADER_AGMT_BU WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if(checked_values.contains("FMS_TRADER_AGMT_BILLING_DTL,")) {
				// FMS_TRADER_AGMT_BILLING_DTL
				queryString = "DELETE FROM FMS_TRADER_AGMT_BILLING_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if (checked_values.contains("FMS_TRADER_CN_MST,")) {
				//	FMS_TRADER_CN_MST();
				queryString = "DELETE FROM FMS_TRADER_CN_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			
			    //	FMS_TRADER_CONT_PLANT();
				queryString = "DELETE FROM FMS_TRADER_CONT_PLANT WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				
			    //	FMS_TRADER_CONT_BU();
				queryString = "DELETE FROM FMS_TRADER_CONT_BU WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if (checked_values.contains("FMS_TRADER_CARGO_MST,")) {
			    //	FMS_TRADER_CARGO_MST();
				queryString = "DELETE FROM FMS_TRADER_CARGO_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if (checked_values.contains("FMS_TRADER_CARGO_MST,")) {
			    //	FMS_TRADER_BILLING_DTL();
				queryString = "DELETE FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if (checked_values.contains("FMS_PSEUDO_CARGO_DTL,")) {
			    //	FMS_TRADER_BILLING_DTL();
				queryString = "DELETE FROM FMS_PSEUDO_CARGO_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if (checked_values.contains("FMS_SECURITY_DEAL_MAP(P),")) {
			    //	FMS_SECURITY_DEAL_MAP();
					queryString = "DELETE FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ?  AND CONTRACT_TYPE IN ('N','D','T','0')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_SECURITY_MST(P),")) {
			    //	FMS_SECURITY_MST();
					queryString = "DELETE FROM FMS_SECURITY_MST WHERE COMPANY_CD = ?  ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("LOG_FMS_SECURITY_MST(P),")) {
			    //	LOG_FMS_SECURITY_MST();
					queryString = "DELETE FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = ?  ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_CONT_PRICE_DTL(P),")) {
			    //	FMS_CONT_PRICE_DTL();
					queryString = "DELETE FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = ?   AND CONTRACT_TYPE IN ('N','D','T','I') ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_CONT_PRICE_MIN_DTL(P),")) {
			    //	FMS_CONT_PRICE_MIN_DTL();
					queryString = "DELETE FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('N','D','T','I') ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_CARGO_SVC_CONT_MST,")) {
			    //	FMS_CARGO_SVC_CONT_MST();
					queryString = "DELETE FROM FMS_CARGO_SVC_CONT_MST WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_CARGO_SVC_CONT_SVC_BU,")) {
			    //	FMS_CARGO_SVC_CONT_SVC_BU();
					queryString = "DELETE FROM FMS_CARGO_SVC_CONT_SVC_BU WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_CARGO_SVC_CONT_SVC_BU,")) {
			    //	FMS_CARGO_SVC_CONT_BU();
					queryString = "DELETE FROM FMS_CARGO_SVC_CONT_BU WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_BUY_CARGO_NOM,")) {
			    //	FMS_BUY_CARGO_NOM();
					queryString = "DELETE FROM FMS_BUY_CARGO_NOM WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_BUY_CARGO_NOM_BL,")) {
			    //	FMS_BUY_CARGO_NOM_BL();
					queryString = "DELETE FROM FMS_BUY_CARGO_NOM_BL WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_BUY_CARGO_NOM_BOE,")) {
			    //	FMS_BUY_CARGO_NOM_BOE();
					queryString = "DELETE FROM FMS_BUY_CARGO_NOM_BOE WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_BUY_CARGO_ALLOC,")) {
			    //	FMS_BUY_CARGO_ALLOC();
					queryString = "DELETE FROM FMS_BUY_CARGO_ALLOC WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_BUY_CARGO_ALLOC_BL,")) {
			    //	FMS_BUY_CARGO_ALLOC_BL();
					queryString = "DELETE FROM FMS_BUY_CARGO_ALLOC_BL WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_BUY_CARGO_ALLOC_BOE,")) {
			    //	FMS_BUY_CARGO_ALLOC_BOE();
					queryString = "DELETE FROM FMS_BUY_CARGO_ALLOC_BOE WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_TRADER_CONT_MST,")) {
			    //	FMS_TRADER_CONT_MST();
					queryString = "DELETE FROM FMS_TRADER_CONT_MST WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			if (checked_values.contains("FMS_TRADER_CONT_BU,")) {
			   
					queryString = "DELETE FROM FMS_TRADER_CONT_BU WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			if (checked_values.contains("FMS_TRADER_CONT_SPLIT_PLANT,")) {

					queryString = "DELETE FROM FMS_TRADER_CONT_SPLIT_PLANT WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			if (checked_values.contains("FMS_TRADER_CONT_PLANT,")) {
			
					queryString = "DELETE FROM FMS_TRADER_CONT_PLANT WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			if (checked_values.contains("FMS_TRADER_CONT_PLANT_CHRG,")) {
			   
					queryString = "DELETE FROM FMS_TRADER_CONT_PLANT_CHRG WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			if (checked_values.contains("FMS_TRADER_BILLING_DTL,")) {
			    //	FMS_TRADER_BILLING_DTL();
					queryString = "DELETE FROM FMS_TRADER_BILLING_DTL WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
		
			if (checked_values.contains("FMS_BUY_DAILY_BUYER_NOM,")) {
			
					queryString = "DELETE FROM FMS_BUY_DAILY_BUYER_NOM WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			if (checked_values.contains("FMS_BUY_DAILY_SELLER_NOM,")) {
			
					queryString = "DELETE FROM FMS_BUY_DAILY_SELLER_NOM WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			if (checked_values.contains("FMS_BUY_DAILY_ALLOCATION,")) {
			   
					queryString = "DELETE FROM FMS_BUY_DAILY_ALLOCATION WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}

			if (checked_values.contains("FMS_BUY_DAILY_ALLOCATION_MM,")) {
			   
					queryString = "DELETE FROM FMS_BUY_DAILY_ALLOCATION_MM WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			
			
			if (checked_values.contains("FMS_PUR_SG_INV_MST,")) {
			    //	FMS_PUR_SG_INV_MST();
					queryString = "DELETE FROM FMS_PUR_SG_INV_MST WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					queryString = "DELETE FROM FMS_PUR_SG_INV_TAX_DTL WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			if (checked_values.contains("FMS_CUSTOM_PD_BOND_DTL,")) {
			 
					queryString = "DELETE FROM FMS_CUSTOM_PD_BOND_DTL WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			if (checked_values.contains("FMS_PUR_PG_INV_MST,")) {
			 
					queryString = "DELETE FROM FMS_PUR_PG_INV_MST WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					queryString = "DELETE FROM FMS_PUR_PG_INV_TAX_DTL WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			if (checked_values.contains("LOG_TRADER_CONT_MST,")) {
				 
				queryString = "DELETE FROM LOG_TRADER_CONT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
		}
			if (checked_values.contains("LOG_TRADER_CN_MST,")) {
				 
				queryString = "DELETE FROM LOG_TRADER_CN_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
		}
			if (checked_values.contains("LOG_TRADER_CARGO_MST,")) {
				 
				queryString = "DELETE FROM LOG_TRADER_CARGO_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
		}
			
			
			System.out.println("<<ROLLBACK_END>><<PURCHASE>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<PURCHASE>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}

	public void SALES() throws SQLException, IOException {
		function_nm = "SALES()";
		try {
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<SALES>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<SALES>>,", conn);
			
			logger.checkpoint1(fname1,"\n"+function_nm+"D"+","+start_end_dt+",", conn);
			
			if(checked_values.contains("FMS_AGMT_MST,")) {
	
				queryString = "DELETE FROM FMS_AGMT_MST WHERE COMPANY_CD = ? AND AGMT_TYPE = 'F' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_AGMT_TRANSPTR,")) {
	
				queryString = "DELETE FROM FMS_AGMT_TRANSPTR WHERE COMPANY_CD = ? AND AGMT_TYPE = 'F'";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_AGMT_PLANT,")) {
			
				queryString = "DELETE FROM FMS_AGMT_PLANT WHERE COMPANY_CD = ? AND AGMT_TYPE = 'F'";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_AGMT_BU,")) {
			
				queryString = "DELETE FROM FMS_AGMT_BU WHERE COMPANY_CD = ? AND AGMT_TYPE = 'F'";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_AGMT_LIABILITY,")) {
		
				queryString = "DELETE FROM FMS_SUPPLY_AGMT_LIABILITY WHERE COMPANY_CD = ? AND AGMT_TYPE = 'F'";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_AGMT_BILLING_DTL,")) {
		
				queryString = "DELETE FROM FMS_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND AGMT_TYPE = 'F'";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_AGMT_DEACTIVATION_DTL,")) {
		
				queryString = "DELETE FROM FMS_AGMT_DEACTIVATION_DTL WHERE COMPANY_CD = ? AND AGMT_TYPE = 'F'";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_CONT_MST,")) {
		
				queryString = "DELETE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_CONT_TRANSPTR,")) {
	
				queryString = "DELETE FROM FMS_SUPPLY_CONT_TRANSPTR WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_CONT_PLANT,")) {
	
				queryString = "DELETE FROM FMS_SUPPLY_CONT_PLANT WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_CONT_BU,")) {
		
				queryString = "DELETE FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_CONT_LIABILITY,")) {
			
				queryString = "DELETE FROM FMS_SUPPLY_CONT_LIABILITY WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_BILLING_DTL,")) {
	
				queryString = "DELETE FROM FMS_SUPPLY_BILLING_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_CONT_PRICE_DTL(S),")) {
		
				queryString = "DELETE FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_CONT_PRICE_MIN_DTL(S),")) {
			
				queryString = "DELETE FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SECURITY_DEAL_MAP(S),")) {
			
				queryString = "DELETE FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X','Q','O')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SECURITY_MST(S),")) {
				// FMS_TRADER_AGMT_MST
				queryString = "DELETE FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("LOG_FMS_SECURITY_MST(S),")) {
				
				queryString = "DELETE FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_CONT_DCQ_DTL,")) {
		
				queryString = "DELETE FROM FMS_SUPPLY_CONT_DCQ_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_CONT_PLANT_CHRG,")) {
		
				queryString = "DELETE FROM FMS_SUPPLY_CONT_PLANT_CHRG WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("LOG_SUPPLY_CONT_MST,")) {
			
				queryString = "DELETE FROM LOG_SUPPLY_CONT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_PURCHASE_MAP_DTL,")) {
			
				queryString = "DELETE FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_SUPPLY_ALLOC_REVISED,")) {
			
				queryString = "DELETE FROM FMS_SUPPLY_ALLOC_REVISED WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_AGMT_MST,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_AGMT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_AGMT_TRANSPTR,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_AGMT_TRANSPTR WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_AGMT_PLANT,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_AGMT_PLANT WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_AGMT_LIABILITY,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_AGMT_LIABILITY WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_AGMT_BILLING_DTL,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_AGMT_BILLING_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_MST,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_MST,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_BU WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("LOG_LTCORA_CONT_MST,")) {
				
				queryString = "DELETE FROM LOG_LTCORA_CONT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_TRANSPTR,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_TRANSPTR WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_PLANT,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_PLANT WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_LIABILITY,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_LIABILITY WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_BILLING_DTL,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_BILLING_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_STRG_CRG,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_STRG_CRG WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_CARGO_DTL,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_CARGO_DTL,")) {
				
				queryString = "DELETE FROM LOG_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_CARGO_ADQ,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_CARGO_ADQ WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_CARGO_CSOC,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_CARGO_CSOC WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_LTCORA_CONT_CARGO_MOD,")) {
				
				queryString = "DELETE FROM FMS_LTCORA_CONT_CARGO_MOD WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_DAILY_BUYER_NOM,")) {
			
				queryString = "DELETE FROM FMS_DAILY_BUYER_NOM WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X','Q','O')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_DAILY_BUYER_NOM_DTL,")) {
			
				queryString = "DELETE FROM FMS_DAILY_BUYER_NOM_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X','Q','O')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_DAILY_SELLER_NOM,")) {
				
				queryString = "DELETE FROM FMS_DAILY_SELLER_NOM WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X','Q','O')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_DAILY_SELLER_NOM_DTL,")) {
			
				queryString = "DELETE FROM FMS_DAILY_SELLER_NOM_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X','Q','O')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_METER_TICKET_READING,")) {
				
				queryString = "DELETE FROM FMS_METER_TICKET_READING WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_DAILY_ALLOCATION_DTL,")) {
				
				queryString = "DELETE FROM FMS_DAILY_ALLOCATION_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X','Q','O')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_DAILY_ALLOCATION_DTL_CT,")) {
				
				queryString = "DELETE FROM FMS_DAILY_ALLOCATION_DTL_CT WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X','Q','O')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_INVOICE_MST,")) {
				
				queryString = "DELETE FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X','Q','O')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				queryString = "DELETE FROM FMS_INV_TAX_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if(checked_values.contains("FMS_INVOICE_MST_LP,")) {
				
				queryString = "DELETE FROM FMS_INV_TAX_DTL A "
                		+ "WHERE A.COMPANY_CD = ? "
                		+ "AND EXISTS (SELECT 1 "
                		+ "FROM FMS_INVOICE_MST B "
                		+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
                		+ "AND B.BU_STATE_TIN = A.BU_STATE_TIN "
                		+ "AND B.INVOICE_SEQ = A.INVOICE_SEQ "
                		+ "AND B.FINANCIAL_YEAR = A.FINANCIAL_YEAR "
                		+ "AND B.INV_FLAG = 'LP') ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				queryString = "DELETE FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? AND INV_FLAG = 'LP' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				// FFLOW tables
				queryString = "DELETE FROM FMS_FFLOW_INV_MST WHERE COMPANY_CD = ? AND INVOICE_TYPE = 'LP' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();

				queryString = "DELETE FROM FMS_FFLOW_INV_DTL WHERE COMPANY_CD = ? AND INVOICE_TYPE = 'LP' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				queryString = "DELETE FROM FMS_FFLOW_INV_TAX_DTL WHERE COMPANY_CD = ? AND INVOICE_TYPE = 'LP' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_INVOICE_DTL,")) {
				
				queryString = "DELETE FROM FMS_INVOICE_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('S','L','X','Q','O')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_INVOICE_IRN_DTL,")) {
			
				queryString = "DELETE FROM FMS_INVOICE_IRN_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_INV_STORAGE_CRG_DTL,")) {
				
				queryString = "DELETE FROM FMS_INV_STORAGE_CRG_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_FFLOW_INV_MST,")) {
							
				queryString = "DELETE FROM FMS_FFLOW_INV_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";

				queryString = "DELETE FROM FMS_FFLOW_INV_TAX_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if(checked_values.contains("FMS_INV_FILE_DTL,")) {
				
				queryString = "DELETE FROM FMS_INV_FILE_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_INV_FILE_DTL_LP,")) {
				
				queryString = "DELETE FROM FMS_INV_FILE_DTL A "
                		+ "WHERE A.COMPANY_CD = ? "
                		+ "AND EXISTS (SELECT 1 "
                		+ "FROM FMS_INVOICE_MST B "
                		+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
                		+ "AND B.BU_STATE_TIN = A.BU_STATE_TIN "
                		+ "AND B.INVOICE_SEQ = A.INVOICE_SEQ "
                		+ "AND B.FINANCIAL_YEAR = A.FINANCIAL_YEAR "
                		+ "AND B.INV_FLAG = 'LP') ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				queryString = "DELETE FROM FMS_FFLOW_INV_FILE_DTL WHERE COMPANY_CD = ? AND INVOICE_TYPE = 'LP' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_FFLOW_INV_DTL,")) {
				
				queryString = "DELETE FROM FMS_FFLOW_INV_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_FFLOW_INV_FILE_DTL,")) {
				
				queryString = "DELETE FROM FMS_FFLOW_INV_FILE_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			if(checked_values.contains("FMS_INV_ADV_DTL,")) {
				
				queryString = "DELETE FROM FMS_INV_ADV_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_SECURITY_DEAL_MAP A WHERE A.COMPANY_CD = ? AND "
						+ "A.SEQ_NO IN (SELECT B.SEQ_NO FROM FMS_SECURITY_MST B WHERE "
						+ "B.COMPANY_CD = A.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.SEC_TYPE = 'ADV' AND B.DEAL_TYPE = 'LTCORA') AND "
						+ "A.COUNTERPARTY_CD IN (SELECT B.COUNTERPARTY_CD FROM FMS_SECURITY_MST B WHERE "
						+ "B.COMPANY_CD = A.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.SEC_TYPE = 'ADV' AND B.DEAL_TYPE = 'LTCORA')  ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? AND SEC_TYPE = 'ADV' AND DEAL_TYPE = 'LTCORA' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_SECURITY_TAX_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if(checked_values.contains("FMS_INV_PAY_RECV_DTL,")) {
				
				queryString = "DELETE FROM FMS_INV_PAY_RECV_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
            if(checked_values.contains("FMS_INVOICE_MST_CR_DR,")) 
            {
				
                queryString = "DELETE FROM FMS_INV_TAX_DTL A "
                		+ "WHERE A.COMPANY_CD = ? "
                		+ "AND EXISTS (SELECT 1 "
                		+ "FROM FMS_INVOICE_MST B "
                		+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
                		+ "AND B.BU_STATE_TIN = A.BU_STATE_TIN "
                		+ "AND B.INVOICE_SEQ = A.INVOICE_SEQ "
                		+ "AND B.FINANCIAL_YEAR = A.FINANCIAL_YEAR "
                		+ "AND B.INV_FLAG IN ('CR','DR')) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_INVOICE_MST WHERE COMPANY_CD = ? AND INV_FLAG IN ('CR','DR')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
            
            
            if(checked_values.contains("FMS_INV_CRDR_REF,")) 
            {
				
				queryString = "DELETE FROM FMS_INV_CRDR_REF WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_INV_CRDR_TAX_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
            if(checked_values.contains("FMS_INV_FILE_DTL_CR_DR,"))
            {
				
                queryString = "DELETE FROM FMS_INV_FILE_DTL A "
                		+ "WHERE A.COMPANY_CD = ? "
                		+ "AND EXISTS (SELECT 1 "
                		+ "FROM FMS_INVOICE_MST B "
                		+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
                		+ "AND B.BU_STATE_TIN = A.BU_STATE_TIN "
                		+ "AND B.INVOICE_SEQ = A.INVOICE_SEQ "
                		+ "AND B.FINANCIAL_YEAR = A.FINANCIAL_YEAR "
                		+ "AND B.INV_FLAG IN ('CR','DR')) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}

			
			System.out.println("<<ROLLBACK_END>><<SALES>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<SALES>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
				} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}
	public void TRANSPORT() throws SQLException, IOException {
		function_nm = "TRANSPORT()";
		try {
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<TRANSPORT>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<TRANSPORT>>,", conn);
			
			logger.checkpoint1(fname1,"\n"+function_nm+"D"+","+start_end_dt+",", conn);
			
			if (checked_values.contains("FMS_GTA_AGMT_MST")) {
				
				queryString = "DELETE FROM FMS_GTA_AGMT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_GTA_AGMT_MST")) {
				
				queryString = "DELETE FROM FMS_GTA_AGMT_TRANS_BU WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_GTA_ENTRY_POINT")) {
				
				queryString = "DELETE FROM FMS_GTA_ENTRY_POINT WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_GTA_EXIT_POINT")) {
				
				queryString = "DELETE FROM FMS_GTA_EXIT_POINT WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_GTA_AGMT_BILLING_DTL")) {
				
				queryString = "DELETE FROM FMS_GTA_AGMT_BILLING_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_GTA_CONT_MST")) {
				
				queryString = "DELETE FROM FMS_GTA_CONT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_GTA_CONT_MST")) {
				
				queryString = "DELETE FROM FMS_GTA_CONT_BU WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_GTA_CONT_MST")) {
				
				queryString = "DELETE FROM FMS_GTA_CONT_TRANS_BU WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
            if (checked_values.contains("FMS_GTA_BILLING_DTL")) {
				
				queryString = "DELETE FROM FMS_GTA_BILLING_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			  }
			
			if (checked_values.contains("FMS_GTA_CONT_MAP")) {
				
				queryString = "DELETE FROM FMS_GTA_CONT_MAP WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_DAILY_TRANSPORTER_NOM")) {
				
				queryString = "DELETE FROM FMS_DAILY_TRANSPORTER_NOM WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_DAILY_TRANSPORTER_SCH")) {
				
				queryString = "DELETE FROM FMS_DAILY_TRANSPORTER_SCH WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_DAILY_TRANSPORTER_ALLOC")) {
				
				queryString = "DELETE FROM FMS_DAILY_TRANSPORTER_ALLOC WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			
			if (checked_values.contains("FMS_GTA_SG_INV_MST")) {
				
				queryString = "DELETE FROM FMS_GTA_SG_INV_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_GTA_SG_INV_TAX_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				
			}
			
			
			if (checked_values.contains("FMS_GTA_PG_INV_MST")) {
				
				queryString = "DELETE FROM FMS_GTA_PG_INV_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_GTA_PG_INV_TAX_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				
			}
			
			 if (checked_values.contains("FMS_GTA_FFLOW_INV_MST")) {
					
					queryString = "DELETE FROM FMS_GTA_FFLOW_INV_MST WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
					
					queryString = "DELETE FROM FMS_GTA_FFLOW_INV_TAX_DTL WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
					
				}
			
            if (checked_values.contains("FMS_GTA_FFLOW_INV_DTL")) {
				
				queryString = "DELETE FROM FMS_GTA_FFLOW_INV_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			System.out.println("<<ROLLBACK_END>><<TRANSPORT>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<TRANSPORT>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}

	public void TERMINAL_OPERATIONS() throws SQLException, IOException {
		function_nm = "TERMINAL_OPERATIONS()";
		try {
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<TERMINAL_OPERATIONS>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<TERMINAL_OPERATIONS>>,", conn);
			
			logger.checkpoint1(fname1,"\n"+function_nm+"D"+","+start_end_dt+",", conn);
			
			if (checked_values.contains("FMS_TANK_MST")) {
				
				queryString = "DELETE FROM FMS_TANK_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_TANK_CONSUMPTION_MST")) {
				
				queryString = "DELETE FROM FMS_TANK_CONSUMPTION_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_TANK_INVENTORY_DTL")) {
				
				queryString = "DELETE FROM FMS_TANK_INVENTORY_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			
			System.out.println("<<ROLLBACK_END>><<TERMINAL_OPERATIONS>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<TERMINAL_OPERATIONS>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
				} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}

	public void RISK_MGMT() throws SQLException, IOException {
		function_nm = "RISK_MGMT()";
		try {
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<RISK_MGMT>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<RISK_MGMT>>,", conn);
			
			logger.checkpoint1(fname1,"\n"+function_nm+"D"+","+start_end_dt+",", conn);
			
			if (checked_values.contains("FMS_LIMIT_MST")) {
				
				queryString = "DELETE FROM FMS_LIMIT_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_LIMIT_DTL")) {
				
				queryString = "DELETE FROM FMS_LIMIT_DTL WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_MR_EXPO_EOD_MST")) {
				
				queryString = "DELETE FROM FMS_MR_EXPO_EOD_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_MR_EXPO_EOD_DTL")) {
				
				queryString = "DELETE FROM FMS_MR_EXPO_EOD_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_MR_CONT_TAQ_DTL")) {
				
				queryString = "DELETE FROM FMS_MR_CONT_TAQ_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_SECURITY_DEAL_MAP")) {
				
				queryString = "DELETE FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_SECURITY_MST")) {
				
				queryString = "DELETE FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("LOG_FMS_SECURITY_MST")) {
				
				queryString = "DELETE FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_SECURITY_FILE_DTL")) {
				
				queryString = "DELETE FROM FMS_SECURITY_FILE_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			
			System.out.println("<<ROLLBACK_END>><<RISK_MGMT>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<RISK_MGMT>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
				} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}
	
	public void DLNG() throws SQLException, IOException {
		function_nm = "DLNG()";
		try {
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<DLNG>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<DLNG>>,", conn);
			
			logger.checkpoint1(fname1,"\n"+function_nm+"D"+","+start_end_dt+",", conn);
			
			if (checked_values.contains("FMS_TRUCK_TRANSPORTER_MST")) {
				
				queryString = "DELETE FROM FMS_TRUCK_TRANSPORTER_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_TRUCK_TRANS_CONTACT_MST")) {
				
				queryString = "DELETE FROM FMS_TRUCK_TRANS_CONTACT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_TRUCK_MST")) {
				
				queryString = "DELETE FROM FMS_TRUCK_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_TRUCK_DRIVER_MST")) {
				
				queryString = "DELETE FROM FMS_TRUCK_DRIVER_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
//			if (checked_values.contains("FMS_FILLING_STATION_MST")) {
//				
//				queryString = "DELETE FROM FMS_FILLING_STATION_MST WHERE ENT_PROFILE = ? ";
//				stmt = conn.prepareStatement(queryString);
//				stmt.setString(1, company_cd);
//				stmt.executeUpdate();
//				
//				conn.commit();
//				stmt.close();
//				
//				msg = "All Data has been Rollbacked Successfully from Database.";
//				msg_type = "S";
//				
//			}
			if (checked_values.contains("FMS_CHECKPOST_MST")) {
				
				queryString = "DELETE FROM FMS_CHECKPOST_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_TRUCK_TRANSPORTER_LINK")) {
				
				queryString = "DELETE FROM FMS_TRUCK_TRANSPORTER_LINK WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_TRUCK_DRIVER_TRANS_LINK")) {
				
				queryString = "DELETE FROM FMS_TRUCK_DRIVER_TRANS_LINK WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_TRUCK_DRIVER_LINK")) {
				
				queryString = "DELETE FROM FMS_TRUCK_DRIVER_LINK WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_LINK_CHECKPOST_PLANT")) {
				
				queryString = "DELETE FROM FMS_LINK_CHECKPOST_PLANT WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_AGMT_MST(DLNG)")) {
				
				queryString = "DELETE FROM FMS_AGMT_MST WHERE COMPANY_CD = ? AND AGMT_TYPE = 'D' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_AGMT_MST(DLNG)")) {
				
				queryString = "DELETE FROM FMS_AGMT_BU WHERE COMPANY_CD = ? AND AGMT_TYPE = 'D' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_AGMT_MST(DLNG)")) {
				
				queryString = "DELETE FROM FMS_AGMT_FILLING_STN WHERE COMPANY_CD = ? AND AGMT_TYPE = 'D' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_AGMT_MST(DLNG)")) {
				
				queryString = "DELETE FROM FMS_AGMT_PLANT WHERE COMPANY_CD = ? AND AGMT_TYPE = 'D' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
            
			if (checked_values.contains("FMS_AGMT_BILLING_DTL(DLNG)")) {
				
				queryString = "DELETE FROM FMS_AGMT_BILLING_DTL WHERE COMPANY_CD = ? AND AGMT_TYPE = 'D' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_AGMT_MST(DLNG)")) {
				
				queryString = "DELETE FROM FMS_AGMT_TRUCK_TRANS WHERE COMPANY_CD = ? AND AGMT_TYPE = 'D' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_AGMT_SVC_MST")) {
				
				queryString = "DELETE FROM FMS_AGMT_SVC_MST WHERE COMPANY_CD = ?  ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_SVC_CONT_MST")) {
				
				queryString = "DELETE FROM FMS_SVC_CONT_MST WHERE COMPANY_CD = ?  ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_SVC_CONT_MST")) {
				
				queryString = "DELETE FROM FMS_SVC_CONT_BU WHERE COMPANY_CD = ?  ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_SVC_CONT_MAP")) {
				
				queryString = "DELETE FROM FMS_SVC_CONT_MAP WHERE COMPANY_CD = ?  ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_SUPPLY_CONT_MST(DLNG)")) {
				
				queryString = "DELETE FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('E','F') ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			if (checked_values.contains("FMS_SUPPLY_CONT_MST(DLNG)")) {
				
				queryString = "DELETE FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('E','F') ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
            if (checked_values.contains("FMS_SUPPLY_CONT_PLANT(DLNG)")) {
				
				queryString = "DELETE FROM FMS_SUPPLY_CONT_PLANT WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('E','F') ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
//                if (checked_values.contains("FMS_SUPPLY_CONT_PLANT(DLNG)")) {
//    				queryString = "DELETE FROM FMS_SUPPLY_CONT_BU WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('E','F') ";
//    				stmt = conn.prepareStatement(queryString);
//    				stmt.setString(1, company_cd);
//    				stmt.executeUpdate();
//    				
//    				conn.commit();
//    				stmt.close();
//    				
//    				msg = "All Data has been Rollbacked Successfully from Database.";
//    				msg_type = "S";
//    				
//    			}
                if (checked_values.contains("FMS_SUPPLY_CONT_FILLING_STN")) {
				queryString = "DELETE FROM FMS_SUPPLY_CONT_FILLING_STN WHERE COMPANY_CD = ?  ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}

			 if (checked_values.contains("FMS_SUPPLY_BILLING_DTL(DLNG)")) {
				queryString = "DELETE FROM FMS_SUPPLY_BILLING_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN('F','E')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			 if (checked_values.contains("FMS_SUPPLY_CFORM_DTL(DLNG)")) {
					queryString = "DELETE FROM FMS_SUPPLY_CFORM_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN('F','E')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
					
				}
			 
			 if (checked_values.contains("FMS_CFORM_MST")) {
				 queryString = "DELETE FROM FMS_CFORM_MST WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("FMS_CFORM_DTL")) {
				 queryString = "DELETE FROM FMS_CFORM_DTL WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("FMS_SUPPLY_CONT_LIABILITY(DLNG)")) {
					
					queryString = "DELETE FROM FMS_SUPPLY_CONT_LIABILITY WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('F','E')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
					
				}
			 if (checked_values.contains("FMS_SUPPLY_CONT_TRUCK_TRANS")) {
					queryString = "DELETE FROM FMS_SUPPLY_CONT_TRUCK_TRANS WHERE COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
					
				}
			 
			 if (checked_values.contains("FMS_SECURITY_DEAL_MAP(DLNG)")) {
				 queryString = "DELETE FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN('F','E')";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("FMS_SECURITY_MST(DLNG)")) {
				 queryString = "DELETE FROM FMS_SECURITY_MST WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("FMS_SUPPLY_CONT_DCQ_DTL(DLNG)")) {
				 queryString = "DELETE FROM FMS_SUPPLY_CONT_DCQ_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN ('F','E')";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("FMS_SUPPLY_PURCHASE_MAP_DTL(DLNG)")) {
					queryString = "DELETE FROM FMS_SUPPLY_PURCHASE_MAP_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN('F','E')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
					
				}
				if (checked_values.contains("FMS_SUPPLY_ALLOC_REVISED(DLNG)")) {
					queryString = "DELETE FROM FMS_SUPPLY_ALLOC_REVISED WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN('F','E')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
					
				}
				if (checked_values.contains("FMS_CONT_PRICE_DTL(DLNG)")) {
					queryString = "DELETE FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN('F','E')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
					
				}
			 if (checked_values.contains("FMS_CONT_PRICE_MIN_DTL(DLNG)")) {
					queryString = "DELETE FROM FMS_CONT_PRICE_MIN_DTL WHERE COMPANY_CD = ? AND CONTRACT_TYPE IN('F','E')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
					
				}
				if (checked_values.contains("FMS_DLNG_BUYER_NOM(D),")) {
				 queryString = "DELETE FROM FMS_DLNG_BUYER_NOM WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("FMS_DLNG_BUYER_NOM_DTL(D),")) {
				 queryString = "DELETE FROM FMS_DLNG_BUYER_NOM_DTL WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 
			 if (checked_values.contains("FMS_DLNG_SELLER_NOM_DTL(D),")) {
				 queryString = "DELETE FROM FMS_DLNG_SELLER_NOM_DTL WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 
			 if (checked_values.contains("FMS_DLNG_ALLOC_MST(D),")) {
				 queryString = "DELETE FROM FMS_DLNG_ALLOC_MST WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 
			 if (checked_values.contains("FMS_DLNG_INVOICE_MST,")) {
				 queryString = "DELETE FROM FMS_DLNG_INVOICE_MST WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 
			 if (checked_values.contains("FMS_DLNG_INVOICE_MST,")) {
				 queryString = "DELETE FROM FMS_DLNG_INV_TAX_DTL WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("FMS_DLNG_INVOICE_MST_LP,")) {
				 queryString = "DELETE FROM FMS_DLNG_INVOICE_MST WHERE COMPANY_CD = ? AND INV_FLAG = 'LP' ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("FMS_DLNG_INV_FILE_DTL,")) {
				 queryString = "DELETE FROM FMS_DLNG_INV_FILE_DTL WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if(checked_values.contains("FMS_DLNG_INV_FILE_DTL_LP,")) {
					
					queryString = "DELETE FROM FMS_DLNG_INV_FILE_DTL A "
	                		+ "WHERE A.COMPANY_CD = ? "
	                		+ "AND EXISTS (SELECT 1 "
	                		+ "FROM FMS_DLNG_INVOICE_MST B "
	                		+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
	                		+ "AND B.BU_STATE_TIN = A.BU_STATE_TIN "
	                		+ "AND B.INVOICE_SEQ = A.INVOICE_SEQ "
	                		+ "AND B.FINANCIAL_YEAR = A.FINANCIAL_YEAR "
	                		+ "AND B.INV_FLAG = 'LP') ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, company_cd);
					stmt.executeUpdate();
					
					conn.commit();
					stmt.close();
					
					msg = "All Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			 
			 if (checked_values.contains("FMS_DLNG_FFLOW_INV_MST,")) {
				 queryString = "DELETE FROM FMS_DLNG_FFLOW_INV_MST WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
				 queryString = "DELETE FROM FMS_DLNG_FFLOW_INV_TAX_DTL WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("FMS_DLNG_FFLOW_INV_MST_SERV,")) {
				 
				 queryString = "DELETE FROM FMS_DLNG_FFLOW_INV_TAX_DTL A WHERE A.COMPANY_CD = ? AND A.INVOICE_TYPE = 'LP' ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
				 queryString = "DELETE FROM FMS_DLNG_FFLOW_INV_DTL A WHERE A.COMPANY_CD = ? AND A.INVOICE_TYPE = 'LP' ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
				 queryString = "DELETE FROM FMS_DLNG_FFLOW_INV_MST WHERE COMPANY_CD = ? AND INVOICE_TYPE = 'LP' ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 if (checked_values.contains("FMS_DLNG_FFLOW_INV_FILE_DTL_SERV,")) {
				 
				 queryString = "DELETE FROM FMS_DLNG_FFLOW_INV_FILE_DTL A WHERE A.COMPANY_CD = ? AND A.INVOICE_TYPE = 'LP' ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 if (checked_values.contains("FMS_DLNG_FFLOW_INV_DTL,")) { 
				 queryString = "DELETE FROM FMS_DLNG_FFLOW_INV_DTL WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 if (checked_values.contains("FMS_DLNG_FFLOW_INV_FILE_DTL,")) {
				 queryString = "DELETE FROM FMS_DLNG_FFLOW_INV_FILE_DTL WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("FMS_DLNG_INV_PAY_RECV_DTL,")) {
				 queryString = "DELETE FROM FMS_DLNG_INV_PAY_RECV_DTL WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
			 
			 if (checked_values.contains("LOG_FMS_SECURITY_MST(D)")) {
				    //	LOG_FMS_SECURITY_MST();
						queryString = "DELETE FROM LOG_FMS_SECURITY_MST WHERE COMPANY_CD = ?  ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, company_cd);
						stmt.executeUpdate();
						
						conn.commit();
						stmt.close();
						
						msg = "All Data has been Rollbacked Successfully from Database.";
						msg_type = "S";
			}
			 
			 
		    if (checked_values.contains("FMS_SVC_CONT_BILLING_DTL,")) {
				 queryString = "DELETE FROM FMS_SVC_CONT_BILLING_DTL WHERE COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1, company_cd);
				 stmt.executeUpdate();
				 
				 conn.commit();
				 stmt.close();
				 
				 msg = "All Data has been Rollbacked Successfully from Database.";
				 msg_type = "S";
				 
			 }
		    
		    if (checked_values.contains("FMS_DLNG_SVC_INVOICE_MST,")) {
		    	queryString = "DELETE FROM FMS_DLNG_SVC_INVOICE_MST WHERE COMPANY_CD = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.executeUpdate();
		    	
		    	conn.commit();
		    	stmt.close();
		    	
		    	msg = "All Data has been Rollbacked Successfully from Database.";
		    	msg_type = "S";
		    	
		    }
		    
		    if (checked_values.contains("FMS_DLNG_SVC_INVOICE_MST,")) {
		    	queryString = "DELETE FROM FMS_DLNG_SVC_INV_TAX_DTL WHERE COMPANY_CD = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.executeUpdate();
		    	
		    	conn.commit();
		    	stmt.close();
		    	
		    	msg = "All Data has been Rollbacked Successfully from Database.";
		    	msg_type = "S";
		    	
		    }
		    
		    if (checked_values.contains("FMS_DLNG_SVC_INVOICE_DTL,")) {
		    	queryString = "DELETE FROM FMS_DLNG_SVC_INVOICE_DTL WHERE COMPANY_CD = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.executeUpdate();
		    	
		    	conn.commit();
		    	stmt.close();
		    	
		    	msg = "All Data has been Rollbacked Successfully from Database.";
		    	msg_type = "S";
		    	
		    }
		    
		    if (checked_values.contains("FMS_DLNG_SVC_INV_FILE_DTL,")) {
		    	queryString = "DELETE FROM FMS_DLNG_SVC_INV_FILE_DTL WHERE COMPANY_CD = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, company_cd);
		    	stmt.executeUpdate();
		    	
		    	conn.commit();
		    	stmt.close();
		    	
		    	msg = "All Data has been Rollbacked Successfully from Database.";
		    	msg_type = "S";
		    	
		    }
		    
		    if(checked_values.contains("FMS_DLNG_INVOICE_MST_CR_DR,"))
            {
				
                queryString = "DELETE FROM FMS_DLNG_INV_TAX_DTL A "
                		+ "WHERE A.COMPANY_CD = ? "
                		+ "AND EXISTS (SELECT 1 "
                		+ "FROM FMS_DLNG_INVOICE_MST B "
                		+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
                		+ "AND B.BU_STATE_TIN = A.BU_STATE_TIN "
                		+ "AND B.INVOICE_SEQ = A.INVOICE_SEQ "
                		+ "AND B.FINANCIAL_YEAR = A.FINANCIAL_YEAR "
                		+ "AND B.INV_FLAG IN ('CR','DR')) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_DLNG_INVOICE_MST WHERE COMPANY_CD = ? AND INV_FLAG IN ('CR','DR')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
		    
		    if(checked_values.contains("FMS_DLNG_INV_CRDR_REF,")) 
            {
				
				queryString = "DELETE FROM FMS_DLNG_INV_CRDR_REF WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_DLNG_INV_CRDR_TAX_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
		    
		    if(checked_values.contains("FMS_DLNG_INV_CRDR_FILE_DTL,"))
            {
				
                queryString = "DELETE FROM FMS_DLNG_INV_FILE_DTL A "
                		+ "WHERE A.COMPANY_CD = ? "
                		+ "AND EXISTS (SELECT 1 "
                		+ "FROM FMS_DLNG_INVOICE_MST B "
                		+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
                		+ "AND B.BU_STATE_TIN = A.BU_STATE_TIN "
                		+ "AND B.INVOICE_SEQ = A.INVOICE_SEQ "
                		+ "AND B.FINANCIAL_YEAR = A.FINANCIAL_YEAR "
                		+ "AND B.INV_FLAG IN ('CR','DR')) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
				
			 
			
			System.out.println("<<ROLLBACK_END>><<DLNG>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<DLNG>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}
	
	public void DERIVATIVES() throws SQLException, IOException {
		function_nm = "DERIVATIVES()";
		try {
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<DERIVATIVES>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<DERIVATIVES>>,", conn);
			
			logger.checkpoint1(fname1,"\n"+function_nm+"D"+","+start_end_dt+",", conn);
			
			if (checked_values.contains("FMS_DERV_AGMT_MST")) {
				
				queryString = "DELETE FROM FMS_DERV_AGMT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				
				queryString = "DELETE FROM FMS_DERV_AGMT_BU WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				queryString = "DELETE FROM FMS_DERV_AGMT_PLANT WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_DERV_AGMT_BILLING_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_DERV_CONT_MST")) {
				
				queryString = "DELETE FROM FMS_DERV_CONT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				
				queryString = "DELETE FROM FMS_DERV_CONT_BU WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				queryString = "DELETE FROM FMS_DERV_CONT_PLANT WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
				
				queryString = "DELETE FROM FMS_SECURITY_DEAL_MAP WHERE COMPANY_CD = ? AND CONTRACT_TYPE = 'V'";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			
			if (checked_values.contains("FMS_DERV_INSTRUMENT_MST")) {
				
				queryString = "DELETE FROM FMS_DERV_INSTRUMENT_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_DERV_CONT_BILLING_DTL")) {
				
				queryString = "DELETE FROM FMS_DERV_CONT_BILLING_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			} 
			
			if (checked_values.contains("FMS_DERV_INVOICE_MST")) {
				
				queryString = "DELETE FROM FMS_DERV_INVOICE_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			} 

			if (checked_values.contains("FMS_DERV_INV_FILE_DTL,")) {
				
				queryString = "DELETE FROM FMS_DERV_INV_FILE_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}

			if (checked_values.contains("FMS_DERV_HEDGE_EXPOSURE_DTL")) {
				
				queryString = "DELETE FROM FMS_DERV_HEDGE_EXPOSURE_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			} 
			
			System.out.println("<<ROLLBACK_END>><<DERIVATIVES>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<DERIVATIVES>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}
	
	public void INTERFACE() throws SQLException, IOException {
		function_nm = "INTERFACE()";
		try {
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<INTERFACE>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<INTERFACE>>,", conn);
			
			logger.checkpoint1(fname1,"\n"+function_nm+"D"+","+start_end_dt+",", conn);
			
			if (checked_values.contains("FMS_ENTITY_ACCOUNT_CODE_SUN")) {
				
				queryString = "DELETE FROM FMS_ENTITY_ACCOUNT_CODE_SUN WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_TAX_STRUCTURE_SUN")) {
				
				queryString = "DELETE FROM FMS_TAX_STRUCTURE_SUN WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
					
			System.out.println("<<ROLLBACK_END>><<INTERFACE>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<INTERFACE>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}
	
	public void OTHER_INVOICE() throws SQLException, IOException {
		function_nm = "OTHER_INVOICE()";
		try {
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<OTHER_INVOICE>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<OTHER_INVOICE>>,", conn);
			
			logger.checkpoint1(fname1,"\n"+function_nm+"D"+","+start_end_dt+",", conn);
			
			if (checked_values.contains("FMS_OTH_ENTITY_MST")) {
				
				queryString = "DELETE FROM FMS_OTH_ENTITY_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_OTH_ENTITY_ADDR_MST")) {
				
				queryString = "DELETE FROM FMS_OTH_ENTITY_ADDR_MST WHERE ENT_PROFILE = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_OTH_INVOICE_MST")) {
				
				queryString = "DELETE FROM FMS_OTH_INVOICE_MST WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_OTH_INVOICE_DTL")) {
				
				queryString = "DELETE FROM FMS_OTH_INVOICE_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_OTH_INV_CRDR_REF")) {
				
				queryString = "DELETE FROM FMS_OTH_INV_CRDR_REF WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_OTH_INV_FILE_DTL")) {
				
				queryString = "DELETE FROM FMS_OTH_INV_FILE_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			if (checked_values.contains("FMS_OTH_INV_IRN_DTL")) {
				
				queryString = "DELETE FROM FMS_OTH_INV_IRN_DTL WHERE COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, company_cd);
				stmt.executeUpdate();
				
				conn.commit();
				stmt.close();
				
				msg = "All Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
				
			}
			
			System.out.println("<<ROLLBACK_END>><<OTHER_INVOICE>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<OTHER_INVOICE>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} 
	}
	
	// Setter-Getter methods
	public void setChecked_Values(String checked_val) {
		checked_values = checked_val;
	}
	
	
	public void setChecked_Ids(String checked_ids) {
		checked_id = checked_ids;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setSysDateTime(String dt) {
		sysDateTime = dt;
	}
	
	public String getMsg_Type() {
		return msg_type;
	}
	
	public void setMigration_Setup_Dir(String dir) {
		migration_setup_dir = dir;
	}
	
	public void setStart_End_Dt(String dt) {
		start_end_dt = dt;
	}	
}
