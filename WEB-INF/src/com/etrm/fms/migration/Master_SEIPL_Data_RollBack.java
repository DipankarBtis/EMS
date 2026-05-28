package com.etrm.fms.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.prefs.Preferences;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;

public class Master_SEIPL_Data_RollBack {

	/*public static void main(String[] args) {
		Master_Excel_Roll_Back rb = new Master_Excel_Roll_Back();
		rb.init();
	}
	
	}
	
	class Master_Excel_Roll_Back {*/
	
	String db_src_file_name = "Master_SEIPL_Data_RollBack.java";
	String migration_setup_dir = "";
	String sysDateTime = "";
	String checked_values = "", msg = "", msg_type = "";
	
	String fname ="";
	String fname_error = "";
	
	//bellow fname1  is for csv file function start & function end only 
	//logger.checkpoint1(fname1,function_nm+"-D"+","+start_end_dt+",", conn); (-D : EXTRACTOR) 
	String fname1 = "";

	String function_nm = "";
	String start_end_dt = null;
	
	PreparedStatement stmt, st_del;
	Connection conn;
	ResultSet rset;
	
	File file1 = null;
	FileInputStream file = null;
	XSSFWorkbook workbook = null;
	XSSFSheet sheet = null;
	Iterator<Row> rowIterator = null;
	Iterator<Cell> cellIterator = null;
	Row row;
	Cell cell;
	
	int logger_count = 0;
	String[] data1 = null;
	String data = "", column = "" ;
	String transporter_map = "", meter_map = "";
	String query_select = "", query_delete = "",query_fetch_columnname = "",query_data_left = "",query_String="";
	String cd ="",eff_dt ="",seq_no="",abbr="",catg="",web_addr="",addr_type="",entity="",company_cd="2",ent="";
	
	DataMigration_Logger logger = new DataMigration_Logger();
	Migration_Plants_Exceptions mpe =new Migration_Plants_Exceptions();
	public void init() {
		function_nm = "init()";
		try {
//			getCustomerTraderList();

			fname= "DataLogs/RollBack/Master_SEIPL_Data_RollBack(log)"+sysDateTime+".csv";
			fname_error = "DataLogs/RollBack/Master_SEIPL_Data_RollBack_Error(log)"+sysDateTime+".csv";
			
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
				if (conn != null && !checked_values.equals(""))
				{
		    		preferences.put("Flag", "0");
					conn.setAutoCommit(false);
					
					
					
//					if (checked_values.contains("FMS_COUNTERPARTY_ADDR_MST")) {
//						FMS_COUNTERPARTY_ADDR_MST();
//					}
					
					
					if (checked_values.contains("FMS_SAC_MST")) {
		    			FMS_SAC_MST();
					}
					
					if (checked_values.contains("FMS_PRODUCT_MOLECULE_MST")) {
		    			FMS_PRODUCT_MOLECULE_MST();
					}
					
					if (checked_values.contains("FMS_PRODUCT_MST")) {
		    			FMS_PRODUCT_MST();
					}
					
					if (checked_values.contains("FMS_HOLIDAY_DTL")) {
		    			FMS_HOLIDAY_DTL();
					}
					
					if (checked_values.contains("FMS_CUSTOM_TAX_STRUCT_DTL")) {
		    			FMS_CUSTOM_TAX_STRUCT_DTL();
					}
					
					if (checked_values.contains("FMS_ENTITY_BU_SVC_TAX_DTL")) {
		    			FMS_ENTITY_BU_SVC_TAX_DTL();
					}
					
					if (checked_values.contains("FMS_ENTITY_SERVICE_TAX_DTL")) {
		    			FMS_ENTITY_SERVICE_TAX_DTL();
					}
					
					if (checked_values.contains("FMS_ENTITY_TAX_STRUCT_DTL")) {
		    			FMS_ENTITY_TAX_STRUCT_DTL();
					}
					
					if (checked_values.contains("FMS_COUNTERPARTY_BU_TAX")) {
						FMS_COUNTERPARTY_BU_TAX();
					}
					
					if (checked_values.contains("FMS_COUNTERPARTY_PLANT_TAX")) {
		    			FMS_COUNTERPARTY_PLANT_TAX();
					}
					
					if (checked_values.contains("FMS_METER_MST")) {
		    			FMS_METER_MST();
					}
					
					if (checked_values.contains("FMS_ENTITY_CONTACT_MST")) {
		    			FMS_ENTITY_CONTACT_MST();
					}
					
					if (checked_values.contains("FMS_SHIP_MST")) {
		    			FMS_SHIP_MST();
					}
					
					if(checked_values.contains("FMS_ENTITY_TURNOVER_DTL"))
					{
						FMS_ENTITY_TURNOVER_DTL();
					}
					
					if(checked_values.contains("FMS_BANK_MST"))
					{
						FMS_BANK_MST();
					}
					
					if(checked_values.contains("FMS_EXCHG_RATE_ENTRY"))
					{
						FMS_EXCHG_RATE_ENTRY();
					}
					
					if(checked_values.contains("FMS_INT_PAY_RATE_ENTRY"))
					{
						FMS_INT_PAY_RATE_ENTRY();	
					}
					
					if (checked_values.contains("FMS_COUNTERPARTY_BU_DTL"))
					{
						FMS_COUNTERPARTY_BU_DTL();
					}
					
					if (checked_values.contains("FMS_COUNTERPARTY_PLANT_DTL")) {
		    			FMS_COUNTERPARTY_PLANT_DTL();
					}
					
					if (checked_values.contains("FMS_COMPANY_OWNER_ADDR_MST")) {
		    			FMS_COMPANY_OWNER_ADDR_MST();
					}
					
					if (checked_values.contains("FMS_COMPANY_OWNER_MST")) {
//		    			FMS_COMPANY_OWNER_MST();
					}
					
					if (checked_values.contains("FMS_ENTITY_ADDR_MST")) {
		    			FMS_ENTITY_ADDR_MST();
					}
					
					if (checked_values.contains("FMS_SECTOR_MST")) {
						FMS_SECTOR_MST();
		    			FMS_SECTOR_DTL();
					}
					
					if (checked_values.contains("FMS_ENTITY_REQ_DTL")) {
						FMS_ENTITY_REQ_DTL();
					}
					
					if (checked_values.contains("FMS_COUNTERPARTY_MST")) {
						FMS_COUNTERPARTY_MST();
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
				if (st_del != null) {try {st_del.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
				if (conn != null) {try {conn.close();} catch (SQLException e) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			    
				if (file != null) {try {file.close();} catch (Exception e ) {new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			}
			
			catch (Exception e){
			    	new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					
					msg = "One of the Functions faced an Error. RollBack Terminated.";
					msg_type = "E";
			    }
			
		     }

	}

	public void FMS_COUNTERPARTY_MST() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_COUNTERPARTY_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_COUNTERPARTY_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query for update row when data are same
//			query_delete = "UPDATE FMS_COUNTERPARTY_MST SET CATEGORY = NULL,WEB_ADDR = NULL  WHERE COUNTERPARTY_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY hh24:mi:ss')";
			query_delete = "DELETE FROM FMS_COUNTERPARTY_MST WHERE ENT_PROFILE = '2' AND COUNTERPARTY_CD = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') ";

			column = "COUNTERPARTY_CD,EFF_DT,COUNTERPARTY_NM,COUNTERPARTY_ABBR,PAN_NO,PAN_ISSUE_DT,CATEGORY,WEB_ADDR,NOTES,STATUS,KYC,IGX,"
					+ "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,SAP_CODE,NCF_CATEGORY,ENT_PROFILE,MOD_PROFILE";

			data1 = new String[column.split(",").length];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COUNTERPARTY_CD,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext())
				{
						query_select = "SELECT  ENT_PROFILE, COUNTERPARTY_CD, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_COUNTERPARTY_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";cd="";
						int i = 0;
						
						while (cellIterator.hasNext()) 
						{
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);			

							if(cell.getColumnIndex() == 0) 
							{
								 abbr = cell.getStringCellValue().split("&@&")[0].substring(1, cell.getStringCellValue().split("&@&")[0].length());
								 
								 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
								 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,abbr);
						 		 rset = stmt.executeQuery(); 
						 		 
						 		 if(rset.next())
						 		 {
						 		 query_select = query_select + column.split(",")[i] + " = ? AND ";
						 		 cd=rset.getString(1);
						 		 data1[0]=cd;
						 		 }
						 		 
						 		 stmt.close();
						 		 rset.close();
						 		 i++;
							}
							else
							{
								data1[i] = data;
								if (data.equals("null")) {
								 query_select = query_select + column.split(",")[i] + " IS NULL AND ";
								 }else if (data.split("/").length == 3 &&  data.contains(":")  && data.length()==19) {
								 query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
								 }else {
								 query_select = query_select + column.split(",")[i] + " =? AND ";
								 }
								 i++;
							}
							
						}//while(column) completed
						query_select = query_select.substring(0, query_select.length() - 4);
			      
			            if(!cd.equals("")) 
			            {
			            	 stmt = conn.prepareStatement(query_select);
						     int k = 1;
						     for (int w = 0; w < (column.split(",").length); w++) 
						     {
							     if (!data1[w].equals("null")) 
							     {
								 stmt.setString(k, data1[w]);
								 k++;
							     }
						     }
						     
						     rset = stmt.executeQuery();
						          
						     if (rset.next())       
						     {
						    	ent=rset.getString(1);
						        cd=rset.getString(2);
						        eff_dt=rset.getString(3);
							     
							    st_del = conn.prepareStatement(query_delete);
							    st_del.setString(1, cd);
							    st_del.setString(2, eff_dt);
							
							    st_del.executeUpdate();
							  
							    logger.data(fname, (ent+","+cd + " , " +abbr+","+ eff_dt + " , "), conn, "");
							    logger_count++;   
							  
							    st_del.close();
						      }

						     rset.close();
						     stmt.close();
			            }
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT ENT_PROFILE, COUNTERPARTY_CD, COUNTERPARTY_ABBR, TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_COUNTERPARTY_MST WHERE ENT_PROFILE = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					ent=rset.getString(1);
					cd = rset.getString(2);
					abbr = rset.getString(3);
					eff_dt = rset.getString(4);
					logger.data(fname, (ent+","+cd + " , " + abbr + " , " + eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();


				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			
			System.out.println("<<ROLLBACK_END>><<FMS_COUNTERPARTY_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, "\nTOTAL DATA DELETED :, " + logger_count+",,,", conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_COUNTERPARTY_MST()>>,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
	}

	public void FMS_COUNTERPARTY_ADDR_MST() throws SQLException, IOException {
		function_nm = "FMS_COUNTERPARTY_ADDR_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_COUNTERPARTY_ADDR_MST()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_COUNTERPARTY_ADDR_MST>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query delete:
			query_delete = "DELETE FROM FMS_COUNTERPARTY_ADDR_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ADDRESS_TYPE=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY hh24:mi:ss')";

//			// column select:
//			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_ADDR_MST'  ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) {
//				column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
//			column = column.substring(0, column.length() - 1);
//			System.out.println("column"+column);

			column="COUNTERPARTY_CD,ADDRESS_TYPE,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			
			data1 = new String[column.split(",").length];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_ADDR_MST_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_ADDR_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();

				logger.checkpoint(fname, "COMPANY_CD,CD,ABBR,EFF_DT,TIMESTAMP>", conn);
				while (rowIterator.hasNext()) 
				{
						query_select = "SELECT  COMPANY_CD,COUNTERPARTY_CD,ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_COUNTERPARTY_ADDR_MST WHERE COMPANY_CD ='2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						
						data = "";
						String cd1="";
						int i = 0;
						
						while (cellIterator.hasNext()) 
						{
						     cell = cellIterator.next();
							 if(cell.getColumnIndex() == 0) 
							 {
								 data = cell.getStringCellValue();
								 data = data.substring(1, data.length() - 1);
						    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=?";
						    	 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,data);
						 		 rset = stmt.executeQuery(); 
						 		 if(rset.next())
						 		 {
						 		 cd1=rset.getString(1);
						 		 }
						 		 stmt.close();
						 		 rset.close();
								 
							 }
							 else 
							 {
								 data = cell.getStringCellValue();
								 data = data.substring(1, data.length() - 1);
								 
								 if(i==0){ data1[i]=cd1;}
								 else {  
									if(data.equals(" "))
									{
										data1[i]="null";
										data="null";
									 }
									 else {
									    data1[i] = data;
										  }
								  }
								 
								 //for creating query
     							 if (data.equals("null")) 
								 {query_select = query_select + column.split(",")[i] + " IS NULL AND ";
								 } else if (data.split("/").length == 3 &&  data.contains(":")  && data.length()==19) {
								 query_select = query_select + column.split(",")[i]+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
								 }else {
								 query_select = query_select + column.split(",")[i] + " =? AND ";
								 }
								 i++;
							 }
							 
						}//while(column) completed
						query_select = query_select.substring(0, query_select.length() - 4);

			            if(!cd1.equals(""))
			            {  
					        stmt = conn.prepareStatement(query_select);
						    int k = 1;
						    for (int w = 0; w < (column.split(",").length); w++) 
						    {  
						    	if(w==8){
							       data1[w]=Camel_Case_Converter(data1[w]);
						        }
						    	
						        if (!data1[w].equals("null")) {		
							       stmt.setString(k, data1[w]);
							       k++;
							    }
						    }
					        rset = stmt.executeQuery();
					        
//		            	    Delete :
					        if (rset.next())
					        {   
					        	 st_del = conn.prepareStatement(query_delete);
					        	 company_cd=rset.getString(1);
					             cd = rset.getString(2);
					             abbr = rset.getString(3);
					             eff_dt = rset.getString(4);
							
					             st_del.setString(1, rset.getString(2));
					             st_del.setString(2, rset.getString(3));
					             st_del.setString(3, rset.getString(4));
					             st_del.executeUpdate();
							
					             logger.data(fname, (company_cd+","+cd + " , " + abbr + " , " + eff_dt+","), conn, "");
					             logger_count++;
							
							     st_del.close();
						     }
						     rset.close();
						     stmt.close();
			             } 
			        
				}//while(row) completed
				
				// Data left
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_COUNTERPARTY_ADDR_MST WHERE COMPANY_CD = '2' ";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					cd = rset.getString(2);
					abbr = rset.getString(3);
					eff_dt = rset.getString(4);
					logger.data(fname, (company_cd+","+cd + " , " + abbr + " , " + eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();


				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_COUNTERPARTY_ADDR_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,"), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_COUNTERPARTY_ADDR_MST()>>,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);

		} catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}

	}

	public void FMS_ENTITY_REQ_DTL() throws SQLException, IOException {
		function_nm = "FMS_ENTITY_REQ_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_ENTITY_REQ_DTL()>>");
			
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_ENTITY_REQ_DTL>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query delete:
			query_delete = "DELETE FROM FMS_ENTITY_REQ_DTL WHERE COMPANY_CD= '2' AND COUNTERPARTY_CD =? AND SEQ_NO =?";
			
			// column select:
//			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_REQ_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			  column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
//			column = column.substring(0, column.length() - 1);
			column="COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,ENTITY,REMARK,APRV_NOTE,STATUS,REQ_BY,REQ_DT,APRV_BY,APRV_DT";
			
			company_cd="";
			
			data1 = new String[(column.split(",").length)-1];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_REQ_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_REQ_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				String cd1="";
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,ENTIY,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,ENTITY,REMARK,APRV_NOTE,STATUS,REQ_BY,REQ_DT,APRV_BY,APRV_DT  FROM FMS_ENTITY_REQ_DTL WHERE COMPANY_CD='2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					int i=0;
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						 cell = cellIterator.next();
					     if(cell.getColumnIndex() == 0) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 
					 		 if(rset.next())
					 		 {
					 		 cd1=rset.getString(1);
					 		 }
					 		 
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
							 if(i==0){
								 data1[i]=cd1;
							 }else{
							     data1[i] = data;
							 }
							 
							 if (data.equals("null")) {
								 query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3 &&  data.contains(":")  && data.length()==19) {
								 query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";} 
							 else {
							  query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						     i++;
							  
					      }
					     
				     }//while(column) completed
					 query_select = query_select.substring(0, query_select.length() - 4);

					 if(!cd1.equals("")) 
					 {
					         stmt = conn.prepareStatement(query_select);
					         int k = 1;
					         for (int w = 0; w < ( (column.split(",").length) -1 ); w++){
					        	 
							   	if (!data1[w].equals("null")){							
							        stmt.setString(k, data1[w]);
							        k++; 
						         }
					         }
						     rset = stmt.executeQuery();
						     
//  					     delete when data match :
						     if (rset.next())
						     {
						       company_cd=rset.getString(1);
						       seq_no=rset.getString(3);
						       entity=rset.getString(4);
						       
						       st_del = conn.prepareStatement(query_delete);   
						       st_del.setString(1, rset.getString(2));
						       st_del.setString(2, rset.getString(3));
						       st_del.executeUpdate();	
						       
						       logger.data(fname, (company_cd+","+cd1+","+seq_no+","+entity+","), conn, "");
						       logger_count++;

						       st_del.close();
						    }
						    rset.close();
						    stmt.close();
					 }
					 
				}//while(row) completed
				
				// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,SEQ_NO FROM FMS_ENTITY_REQ_DTL";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
					company_cd=rset.getString(1);
					seq_no=rset.getString(2);
					logger.data(fname, (company_cd+","+cd1+","+seq_no+","+entity+","), conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_ENTITY_REQ_DTL()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,"), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_ENTITY_REQ_DTL()>>,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
			}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
		
	}
	
	public void FMS_SECTOR_DTL() throws IOException, SQLException {
		function_nm = "FMS_SECTOR_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SECTOR_DTL>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SECTOR_DTL()>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
           
			// query:delete
			query_delete = "DELETE FROM FMS_SECTOR_DTL WHERE SECTOR_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY hh24:mi:ss')";

			column = "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE";

			data1 = new String[(column.split(",").length)];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SECTOR_MST_"+start_end_dt+".xlsx");
			if (file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECTOR_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				String cd = "";
				logger.checkpoint(fname, "CD,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
						query_select = "SELECT SECTOR_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_SECTOR_DTL WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						int i = 0;
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);
							
							if(cell.getColumnIndex() == 0 || cell.getColumnIndex() == 1 || cell.getColumnIndex() == 2 || cell.getColumnIndex() == 3 || cell.getColumnIndex() == 4 )
							{}
							else {
								data1[i] = data;
								
								if (data.equals("null")) {
									query_select = query_select + column.split(",")[i] + " IS NULL AND ";
									
								} else if (data.split("/").length == 3 &&  data.contains(":")  && data.length()==19) {
									query_select = query_select + column.split(",")[i] + " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
								
								} else {
									query_select = query_select + column.split(",")[i] + " =? AND ";
									
								}
								i++;
							
							}
							
						}
						query_select = query_select.substring(0, query_select.length() - 4);

						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < ((column.split(",").length)-1); w++) 
						{
							if (!data1[w].equals("null")) {
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						rset = stmt.executeQuery();

//			            delete if data match :
						if (rset.next()) {

							st_del = conn.prepareStatement(query_delete);
							cd = rset.getString(1);
							eff_dt = rset.getString(2);

							st_del.setString(1, rset.getString(1));
							st_del.setString(2, rset.getString(2));
							st_del.executeUpdate();
							
							logger.data(fname, (cd + " , " + eff_dt+" , "), conn, "");
							logger_count++;  
							st_del.close();
						}
						rset.close();
						stmt.close();
				}
				// left data:
				query_data_left = "SELECT SECTOR_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_SECTOR_DTL";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					
					cd = rset.getString(1);
					eff_dt = rset.getString(2);
					
					logger.data(fname, (cd + " , " + eff_dt+","), conn, "N");

				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_SECTOR_DTL()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+","), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SECTOR_DTL()>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally{
			if(file !=null)
			{file.close();}
		}

	}
	
	public void FMS_SECTOR_MST() throws IOException, SQLException {
		function_nm = "FMS_SECTOR_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SECTOR_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SECTOR_MST()>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
           
			// query:delete
			query_delete = "DELETE FROM FMS_SECTOR_MST WHERE SECTOR_CD=? AND SECTOR_ABBR=?";

			// query for fetch column name
//			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_SECTOR_MST'  ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) {
//				column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
//			column = column.substring(0, column.length() - 1);
			column="SECTOR_CD,SECTOR_NAME,SECTOR_ABBR,SECTOR_TYPE,STATUS_FLAG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,ENT_PROFILE,MOD_PROFILE";

			data1 = new String[column.split(",").length];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SECTOR_MST_"+start_end_dt+".xlsx");
			if (file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SECTOR_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				String cd = "", abbr = "";
				logger.checkpoint(fname, "CD,ABBR,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
						query_select = "SELECT SECTOR_CD,SECTOR_ABBR FROM FMS_SECTOR_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						int i = 0;
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);
							data1[i] = data;
							if (i == 0) {
							} else if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 &&  data.contains(":")  && data.length()==19) {
								query_select = query_select + column.split(",")[i]
										+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						}
						query_select = query_select.substring(0, query_select.length() - 4);

						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{
							if (w == 0) {
							} else if (!data1[w].equals("null")) {
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						rset = stmt.executeQuery();

//			            delete if data match :
						if (rset.next()) {
							st_del = conn.prepareStatement(query_delete);
							cd = rset.getString(1);
							abbr = rset.getString(2);
							
							st_del.setString(1, rset.getString(1));
							st_del.setString(2, rset.getString(2));
							st_del.executeUpdate();
							
							logger.data(fname, (cd + " , " + abbr+","), conn, "");
							logger_count++;  
							st_del.close();
						}
						rset.close();
						stmt.close();
				}
				// left data:
				query_data_left = "SELECT SECTOR_CD,SECTOR_ABBR FROM FMS_SECTOR_MST";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					cd = rset.getString(1);
					abbr = rset.getString(2);
					logger.data(fname, (cd + " , " + abbr+","), conn, "N");

				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_SECTOR_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+","), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SECTOR_MST()>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally{
			if(file !=null)
			{file.close();}
		}

	}
	
	
	public void FMS_ENTITY_ADDR_MST() throws IOException, SQLException {
		function_nm = "FMS_ENTITY_ADDR_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_ENTITY_ADDR_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_ENTITY_ADDR_MST()>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
		
			// query:delete
			query_delete = "DELETE FROM FMS_ENTITY_ADDR_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND ADDRESS_TYPE = ? AND  ENTITY = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') ";

		    // query for fetch column name
//			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_ADDR_MST'  ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			  column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
//			column = column.substring(0, column.length() - 1);
			column="COMPANY_CD,COUNTERPARTY_CD,ADDRESS_TYPE,ENTITY,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";

			company_cd="";
			
			data1 = new String[ (column.split(",").length)-1 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_ADDR_MST_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_ADDR_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ADRESS_TYPE,ENTITY,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,ADDRESS_TYPE,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_ENTITY_ADDR_MST WHERE COMPANY_CD = '2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="";
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
					     if(cell.getColumnIndex() == 0) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 {
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {
								 data1[i]=cd1;
							 }else
							 {
								 if(i==9 && data.equals(" ")){
									data1[i]="null";
									data="null";
							     }else {
									 data1[i] = data;
								 }
							 }
					    	 
							 if (data.equals("null")) 
							 {
							  query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							  query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							  query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						      i++;
							  
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
					
					if(!cd1.equals("")) 
					{			
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						     if(w==9) {data1[w]=Camel_Case_Converter(data1[w]);}
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
					
//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);
						
						  company_cd=rset.getString(1);
						  cd=rset.getString(2);
						  addr_type=rset.getString(3);
						  entity=rset.getString(4);
						  eff_dt=rset.getString(5);
						
						  st_del.setString(1, company_cd);
						  st_del.setString(2, cd);
						  st_del.setString(3, addr_type);
						  st_del.setString(4, entity);
						  st_del.setString(5, eff_dt);
						  st_del.executeUpdate();
						
						  logger.data(fname, company_cd+","+cd+","+addr_type+","+entity+","+eff_dt+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
				
				// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ADDRESS_TYPE,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM  FMS_ENTITY_ADDR_MST";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
		
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					addr_type=rset.getString(3);
					entity=rset.getString(4);
					eff_dt=rset.getString(5);
					
					logger.data(fname, company_cd+","+cd+","+addr_type+","+entity+","+eff_dt+",", conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_ENTITY_ADDR_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_ENTITY_ADDR_MST()>>,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file !=null)
			{file.close();}
		}
		
		
	}
	
	public void FMS_COMPANY_OWNER_MST() throws IOException, SQLException 
	{
		function_nm = "FMS_COMPANY_OWNER_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_COMPANY_OWNER_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_COMPANY_OWNER_MST()>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query:delete
			query_delete = "DELETE FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_CD = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') ";

		    // query for fetch column name
			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COMPANY_OWNER_MST' ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(query_fetch_columnname);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				column += rset.getString(1) + ",";
			}
			rset.close();
			stmt.close();
			column = column.substring(0, column.length() - 1);
			
			data1 = new String[ (column.split(",").length)-3 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COMPANY_OWNER_MST_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COMPANY_OWNER_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				company_cd="";
				
				logger.checkpoint(fname, "COMPANY_CD,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_CD = '2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					data = "";
					company_cd="";
					
					while (cellIterator.hasNext()) 
					{   
					         cell = cellIterator.next();
					     
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(cell.getColumnIndex()==0){}
							 else {
								 data1[i] = data; 
							 }
							 System.out.println("data:"+data1[i]+"i"+i);
							 
							 if(cell.getColumnIndex()==0){}
							 else if (data.equals("null")) 
							 {
							  query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							  query_select = query_select + column.split(",")[i] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							  query_select = query_select + column.split(",")[i] + " = ? AND ";
							 }
						      i++;
							  
					      
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
					
					 stmt = conn.prepareStatement(query_select);
					 int k = 1;
					 for (int w = 1; w < ((column.split(",").length) -3); w++) 
					 {
						 if (!data1[w].equals("null")) {		
						    stmt.setString(k, data1[w]);
							k++;
						   }
					 }
					 rset = stmt.executeQuery();
		
//		             delete if data match :
					 if (rset.next()) 
					 {
					    st_del = conn.prepareStatement(query_delete);
						company_cd=rset.getString(1);
						eff_dt=rset.getString(2);
						
						st_del.setString(1, company_cd);
						st_del.setString(2, eff_dt);
					    st_del.executeUpdate();
						
						logger.data(fname, company_cd+","+eff_dt+",", conn, "");
						logger_count++;  
						
						st_del.close();
					  }
					  rset.close();
					  stmt.close();
					
				}//while row completed
			
    			// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM  FMS_COMPANY_OWNER_MST";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
		
					company_cd=rset.getString(1);
					eff_dt=rset.getString(2);
					
					logger.data(fname, company_cd+","+eff_dt+",", conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_COMPANY_OWNER_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+","), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_COMPANY_OWNER_MST()>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}//try
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file !=null)
			{file.close();}
		}
		
	}
	public void FMS_COMPANY_OWNER_ADDR_MST() throws IOException, SQLException 
	{
		function_nm = "FMS_COMPANY_OWNER_ADDR_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_COMPANY_OWNER_ADDR_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_COMPANY_OWNER_ADDR_MST()>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query:delete
			query_delete = "DELETE FROM FMS_COMPANY_OWNER_ADDR_MST WHERE COMPANY_CD = ? AND ADDRESS_TYPE = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') ";

		    // query for fetch column name
//		    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COMPANY_OWNER_ADDR_MST'  ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			 column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "COMPANY_CD,ADDRESS_TYPE,EFF_DT,ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			

			data1 = new String[ (column.split(",").length) ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COMPANY_OWNER_ADDR_MST_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COMPANY_OWNER_ADDR_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				logger.checkpoint(fname, "COMPANY_CD,ADRESS_TYPE,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_COMPANY_OWNER_ADDR_MST WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="";
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
					     if(cell.getColumnIndex() == 0) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
					    	 query_String="SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 {
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {
								 data1[i]=cd1;
							 }else
							 {
								 if(i==8 && data.equals(" "))
								 {
									data1[i]="null";
									data="null";
							     }else {
									 data1[i] = data;
								 }
							 }
					    	 
							 if (data.equals("null")) 
							 {
							  query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							  query_select = query_select + column.split(",")[i] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							  query_select = query_select + column.split(",")[i] + " = ? AND ";
							 }
						     i++;
							  
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
					
					if(!cd1.equals("")) 
					{			
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						     if(w==8) {data1[w]=Camel_Case_Converter(data1[w]);}
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
					
//		                 delete if data match :
					     if (rset.next()) 
					     {
						    st_del = conn.prepareStatement(query_delete);
						
						    cd1=rset.getString(1);
						    addr_type=rset.getString(2);
						    eff_dt=rset.getString(3);
						
						    st_del.setString(1, cd1);
						    st_del.setString(2, addr_type);
						    st_del.setString(3, eff_dt);
						    st_del.executeUpdate();
						
						    logger.data(fname, cd1+","+addr_type+","+eff_dt+",", conn, "");
						    logger_count++;  
						
						    st_del.close();
					      }
					    rset.close();
					    stmt.close();
					  }
					
				}//while row completed
				
//				 LEFT DATA
				query_data_left = "SELECT COMPANY_CD,ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM  FMS_COMPANY_OWNER_ADDR_MST";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
		
					cd=rset.getString(1);
					addr_type=rset.getString(2);
					eff_dt=rset.getString(3);
					
					logger.data(fname,cd+","+addr_type+","+eff_dt+",", conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			System.out.println("<<ROLLBACK_END>><<FMS_COMPANY_OWNER_ADDR_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_COMPANY_OWNER_ADDR_MST()>>,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file !=null)
			{file.close();}
		}
	}
	
	
	public void FMS_COUNTERPARTY_PLANT_DTL() throws IOException, SQLException 
	{
		function_nm = "FMS_COUNTERPARTY_PLANT_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_COUNTERPARTY_PLANT_DTL>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_COUNTERPARTY_PLANT_DTL()>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			
			// query:delete
			query_delete = "DELETE FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ENTITY = ? AND SEQ_NO = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') ";

		    // query for fetch column name
//		    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_PLANT_DTL'  ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			 column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";
			
			data1 = new String[ (column.split(",").length)-1 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_PLANT_DTL_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_PLANT_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				company_cd="";
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ADRESS_TYPE,ENTITY,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_COUNTERPARTY_PLANT_DTL WHERE COMPANY_CD = '2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="",sector_cd="";
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
						if(cell.getColumnIndex() == 0 && !cell.getStringCellValue().equals("'SEIPL'")) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data.trim());
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					     }
					     else if(cell.getColumnIndex() == 0 && cell.getStringCellValue().equals("'SEIPL'"))
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else if(cell.getColumnIndex() == 12 )
					     {
					    	 data = cell.getStringCellValue();
					    	 data = data.substring(1, data.length() - 1);
					    	 
					    	 query_String="SELECT SECTOR_CD FROM FMS_SECTOR_MST WHERE SECTOR_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 
					 		 if(rset.next())
					 		 { 
					 		 sector_cd=rset.getString(1);
					 		 data=sector_cd;
					 		 data1[11]=sector_cd;
					 		 }else
					 		 {
					 			 data="null";
					 			 data1[11]=data;
					 		 }
					 		 
					 		 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else 
							 {
								query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
					 		   i++;
					 		 
					 		 stmt.close();
					 		 rset.close();
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {  data1[i]=cd1;
							 }
							 else
							 {  
								 data1[i] = data; 
							 }
					    	 
							 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						      i++;
							  
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
					
					if(!cd1.equals("")) 
					{	
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
					
//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);
						
						  company_cd=rset.getString(1);
						  cd=rset.getString(2);
						  entity=rset.getString(3);
						  seq_no=rset.getString(4);
						  eff_dt=rset.getString(5);
						
						  st_del.setString(1, cd);
						  st_del.setString(2, entity);
						  st_del.setString(3, seq_no);
						  st_del.setString(4, eff_dt);
						  st_del.executeUpdate();
						
						  logger.data(fname,company_cd+","+cd+","+entity+","+seq_no+","+eff_dt+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
				
				// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_COUNTERPARTY_PLANT_DTL";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
		
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					entity=rset.getString(3);
					seq_no=rset.getString(4);
					eff_dt=rset.getString(5);
					
					logger.data(fname, company_cd+","+cd+","+entity+","+seq_no+","+eff_dt+",", conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_COUNTERPARTY_PLANT_DTL()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_COUNTERPARTY_PLANT_DTL()>>,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	
	public void FMS_COUNTERPARTY_BU_DTL() throws IOException, SQLException {
	function_nm = "FMS_COUNTERPARTY_BU_DTL()";
			try {
				column=" ";
				logger_count=0;
				
				System.out.println("<<ROLLBACK_START>><<FMS_COUNTERPARTY_BU_DTL>>");
				logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_COUNTERPARTY_BU_DTL()>>,,,,", conn);
				
				logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
				
				// query:delete
				query_delete = "DELETE FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ENTITY = ? AND SEQ_NO = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss')";

			    // query for fetch column name
//			    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_BU_DTL' ORDER BY COLUMN_ID";
//				stmt = conn.prepareStatement(query_fetch_columnname);
//				rset = stmt.executeQuery();
//				while (rset.next()) 
//				{
//				 column += rset.getString(1) + ",";
//				}
//				rset.close();
//				stmt.close();
				column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,PLANT_ZONE,PLANT_CITY,PLANT_PIN,STATUS,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";
				
				data1 = new String[ (column.split(",").length)-1 ];
				file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_BU_DTL_"+start_end_dt+".xlsx");
				if(file1.exists())
				{
					file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_BU_DTL_"+start_end_dt+".xlsx"));
					workbook = new XSSFWorkbook(file);
					sheet = workbook.getSheetAt(0);
					rowIterator = sheet.iterator();
					rowIterator.next();
					
					company_cd="";

					logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,PLANT_ABBR,ENTITY,SEQ_NO,EFF_DT,TIMESTAMP", conn);
					while (rowIterator.hasNext()) 
					{
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,PLANT_ABBR,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_COUNTERPARTY_BU_DTL WHERE COMPANY_CD = '2' AND ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						int i = 0;
						String cd1="";
						data = "";
						
						while (cellIterator.hasNext()) 
						{   
							cell = cellIterator.next();
							if(cell.getColumnIndex() == 0 && !cell.getStringCellValue().equals("'SEIPL'")) 
						     {
						    	 data = cell.getStringCellValue();
								 data = data.substring(1, data.length() - 1);
								 
						    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
						    	 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,data);
						 		 rset = stmt.executeQuery(); 
						 		 if(rset.next())
						 		 { 
						 		 cd1=rset.getString(1);
						 		 }
						 		 stmt.close();
						 		 rset.close();
						 		
						     }
						     else if(cell.getColumnIndex() == 0 && cell.getStringCellValue().equals("'SEIPL'"))
						     {
						    	 data = cell.getStringCellValue();
								 data = data.substring(1, data.length() - 1);
								 
						    	 query_String="SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_ABBR=? ";
						    	 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,data);
						 		 rset = stmt.executeQuery(); 
						 		 if(rset.next())
						 		 { 
						 		 cd1=rset.getString(1);
						 		 }
						 		 stmt.close();
						 		 rset.close();
						     }
						     else 
						     {
							     data = cell.getStringCellValue();
								 data = data.substring(1, data.length() - 1);
								 if(i==0)
								 {  data1[i]=cd1;
								 }else
								 {  data1[i] = data; 
								 }
						    	 
								 if (data.equals("null")) 
								 {
								   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
								 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
								 {
								   query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
								 }else 
								 {
								   query_select = query_select + column.split(",")[i+1] + " = ? AND ";
								 }
							      i++;
								  
						      }
					     }//while cell complete	
						query_select = query_select.substring(0, query_select.length() - 4);
						
						if(!cd1.equals("")) 
						{			
						    stmt = conn.prepareStatement(query_select);
						    int k = 1;
						    for (int w = 0; w < ((column.split(",").length) -1); w++) 
						    {
							    
							     if (!data1[w].equals("null")) {		
								 stmt.setString(k, data1[w]);
								 k++;
							     }
						   }
						   rset = stmt.executeQuery();
						
//			               delete if data match :
						   if (rset.next()) 
						   {
							  st_del = conn.prepareStatement(query_delete);
							
							  company_cd=rset.getString(1);
							  cd=rset.getString(2);
							  abbr=rset.getString(3);
							  entity=rset.getString(4);
							  seq_no=rset.getString(5);
							  eff_dt=rset.getString(5);

							  st_del.setString(1, rset.getString(2));
							  st_del.setString(2, rset.getString(4));
							  st_del.setString(3, rset.getString(5));
							  st_del.setString(4, rset.getString(6));
							 
							  st_del.executeUpdate();
							 
							  logger.data(fname,company_cd+","+cd+","+abbr+","+entity+","+seq_no+","+eff_dt+",", conn, "");
							  logger_count++;  
							
							  st_del.close();
						    }
						    rset.close();
						    stmt.close();
						}
						
					}//while row completed
					
					// LEFT DATA
					query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,PLANT_ABBR,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_COUNTERPARTY_BU_DTL";
					stmt = conn.prepareStatement(query_data_left);
					rset = stmt.executeQuery();
					while (rset.next()) {
			
						company_cd=rset.getString(1);
						cd=rset.getString(2);
						abbr=rset.getString(3);
						entity=rset.getString(4);
						seq_no=rset.getString(5);
						eff_dt=rset.getString(6);
						
						logger.data(fname, company_cd+","+cd+","+abbr+","+entity+","+seq_no+","+eff_dt+",", conn, "N");
					}
					rset.close();
					stmt.close();

					msg = "Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
				}
				else {
					msg = "Excel File not found while Execution. Program Terminated.";
					msg_type = "E";
				}
				conn.commit();
				
				System.out.println("<<ROLLBACK_END>><<FMS_COUNTERPARTY_BU_DTL()>>");
				System.out.println();
				
				logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,,"), conn);
				logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_COUNTERPARTY_BU_DTL()>>,,,,", conn);

				logger.checkpoint1(fname1,logger_count+",", conn);
				
			}
			catch (Exception e){
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
				logger.error(fname, e, function_nm, conn, fname_error);
				
				msg = "One of the Functions faced an Error. RollBack Terminated.";
				msg_type = "E";
			}finally{
				if(file != null)
				{file.close();}
			}
		
	}
	
	
	public void FMS_INT_PAY_RATE_ENTRY() throws IOException, SQLException 
	{ function_nm = "FMS_INT_PAY_RATE_ENTRY()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_INT_PAY_RATE_ENTRY>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_INT_PAY_RATE_ENTRY()>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
		
			// query:delete
			query_delete = "DELETE FROM FMS_INT_PAY_RATE_ENTRY WHERE  INT_RATE_CD = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') ";

			// query for fetch column name
//			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_INT_PAY_RATE_ENTRY'  ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//				column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "INT_RATE_CD,EFF_DT,INT_VAL,CURRENCY_CD,REMARK,FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE";
			
			data1 = new String[ (column.split(",").length)];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_INT_PAY_RATE_ENTRY_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_INT_PAY_RATE_ENTRY_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				company_cd="";
				
				logger.checkpoint(fname, "INT_RATE_CD,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT INT_RATE_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_INT_PAY_RATE_ENTRY WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="";
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
					     if(cell.getColumnIndex() == 0) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(data.trim().equals("SB BR")) 
							 { data = "SBI BR"; }
					    	 query_String="SELECT INT_RATE_CD FROM FMS_INT_RATE_MST WHERE  UPPER(INT_RATE_NM) LIKE ?";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,"%"+data.trim()+"%");
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {  data1[i]=cd1;
							 }else
							 {  
								 data1[i] = data; 
							 }
							 
							 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i] + " = ? AND ";
							 }
						      i++;
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
		
					if(!cd1.equals("")) 
					{			
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
					
//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);
						
						  cd=rset.getString(1);
						  eff_dt=rset.getString(2);
						
						  st_del.setString(1, cd);
						  st_del.setString(2, eff_dt);
						  st_del.executeUpdate();
						  
						  logger.data(fname,cd+","+eff_dt+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
				
//				LEFT DATA
				query_data_left = "SELECT INT_RATE_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_INT_PAY_RATE_ENTRY";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
		
					cd=rset.getString(1);
					eff_dt=rset.getString(2);
					
					logger.data(fname, cd+","+eff_dt+",", conn, "N");
				}
				rset.close();
				stmt.close();
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_INT_PAY_RATE_ENTRY()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+","), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_INT_PAY_RATE_ENTRY()>>,", conn);	
			
			logger.checkpoint1(fname1,logger_count+",", conn);
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	
	
	public void FMS_EXCHG_RATE_ENTRY() throws IOException, SQLException {
		function_nm = "FMS_EXCHG_RATE_ENTRY()";
		try {
			column=" ";
			logger_count=0;
		
			System.out.println("<<ROLLBACK_START>><<FMS_EXCHG_RATE_ENTRY>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_EXCHG_RATE_ENTRY()>>,", conn);
		
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			// query:delete
			query_delete = "DELETE FROM FMS_EXCHG_RATE_ENTRY WHERE  EXCHG_RATE_CD =? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') ";

			// query for fetch column name
//			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_EXCHG_RATE_ENTRY' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//				column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column ="EXCHG_RATE_CD,EFF_DT,EXCHG_VAL,CURRENCY_CD,CURRENCY_CD_FROM,REMARK,FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE";
		
			data1 = new String[ (column.split(",").length)];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_EXCHG_RATE_ENTRY_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_EXCHG_RATE_ENTRY_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				
				logger.checkpoint(fname, "EXCHG_RATE_CD,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT EXCHG_RATE_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_EXCHG_RATE_ENTRY WHERE ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="",name="";
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
					     if(cell.getColumnIndex() == 0) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 name=data;
							 name=name.toUpperCase().trim();
							 
							    if (name.contains("CUSTOMS RATE")) {
									name = "CUSTOM EXCHANGE RATE";
								}
								else if (name.contains("RBI REFERENCE")) {
									name = "RBI REFERENCE RATE";
								}
								else if (name.contains("SBI MUMBAI TT AVERAGE")) {
									name = "SBI MUMBAI TT BUY SELL";
								}
								else if (name.contains("SBI TT BUYING")) {
									name = "SBI RATE BUY";
								}
								else if (name.contains("SBI TT SELLING")) {
									name = "SBI RATE SELL";
								}
								else if (name.contains("SBI TT BUY SELL")) {
									name = "SBI RATE BUY SELL";
								}
							 
					    	 query_String="SELECT EXC_RATE_CD FROM FMS_EXCHG_RATE_MST WHERE  UPPER(EXC_RATE_NM) = ?";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,name);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {  data1[i]=cd1;
							 }else
							 {  if(i==5) {
								 data=data.replace("'", " ");
								 data1[i]=data;
							      }
							    else { data1[i] = data; }
							 }
							
							 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i] + " = ? AND ";
							 }
						      i++;
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
					

			
					if(!cd1.equals("")) 
					{		
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length)-1 ); w++) 
					    {
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
//		               delete if data match :
					   if (rset.next()) 
					   { 
						  st_del = conn.prepareStatement(query_delete);
						  
						  cd=rset.getString(1);
						  eff_dt=rset.getString(2);
						
						  st_del.setString(1, cd);
						  st_del.setString(2, eff_dt);
						  st_del.executeUpdate();
						 
						  logger.data(fname,cd+","+eff_dt+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
				
//				LEFT DATA
				query_data_left = "SELECT EXCHG_RATE_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_EXCHG_RATE_ENTRY";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
		
					cd=rset.getString(1);
					eff_dt=rset.getString(2);
					
					logger.data(fname,cd+","+eff_dt+",", conn, "N");
				}
				rset.close();
				stmt.close();
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_EXCHG_RATE_ENTRY()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+","), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_EXCHG_RATE_ENTRY()>>,", conn);	
			
			logger.checkpoint1(fname1,logger_count+",", conn);
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	
	
	public void FMS_BANK_MST() throws IOException,SQLException {
		function_nm = "FMS_BANK_MST()";
		try {
			column="";
			logger_count=0;
			int max_cd = 1000;
			
			System.out.println("<<ROLLBACK_START>><<FMS_BANK_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_BANK_MST()>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			// query:delete
			query_delete = "DELETE FROM FMS_BANK_MST WHERE BANK_CD=? AND BANK_NAME =? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY hh24:mi:ss')";

			// query for fetch column name
//			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_BANK_MST' ORDER BY COLUMN_ID ";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) {
//				column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "BANK_NAME,BANK_ABBR,EFF_DT,BRANCH_NAME,ADDR,CITY,PIN,STATE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,REMARK,BRANCH_IFSC_CD,ACTIVE_FLAG,ENT_PROFILE,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT,MOD_PROFILE";
			
			data1 = new String[column.split(",").length];
			
			file1 = new File(migration_setup_dir + "EXPORT/FMS_BANK_MST_"+start_end_dt+".xlsx");
			if (file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_BANK_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				String bank_cd = "", bank_name = "" ;
				eff_dt="";
				logger.checkpoint(fname, "BANK_CD,BANK_NAME,BANK_ABBR,EFF_DATE,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
						query_select = "SELECT BANK_CD,BANK_NAME,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'),BANK_ABBR FROM FMS_BANK_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						int i = 0;
						
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);
						
							if (cell.getColumnIndex() == 0) {
								data=null;
							}
							else if (cell.getColumnIndex() == 9 && cell.getStringCellValue().length() > 2) {
								
								query_String = "SELECT COUNTRY_NM FROM FMS_COUNTRY_MST WHERE UPPER(COUNTRY_NM) LIKE ? ";
								stmt = conn.prepareStatement(query_String);
								stmt.setString(1,data);
								rset = stmt.executeQuery();
								if(rset.next()) {
									data = rset.getString(1);
								}
								else {
									data = Camel_Case_Converter(cell.getStringCellValue());
									if(!data.contains("null")) 
									{
									data = data.substring(1, data.length() - 1);
									}
									
								}
								rset.close();
								stmt.close();
							
								
							}
						    if(data!=null) {
						    	  data1[i] = data;
						    	 
						    	  if (data.equals("null")) {
						    	  		query_select = query_select + column.split(",")[i] + " IS NULL AND ";
						    	  } else if (data.split("/").length == 3 &&  data.contains(":")  && data.length()==19) {
						    	  		query_select = query_select + column.split(",")[i]
						    	  						+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
						    	  } else {
						    	  		query_select = query_select + column.split(",")[i] + " =? AND ";
						    	  }
						    	  i++;
						     }
						}
						query_select = query_select.substring(0, query_select.length() - 4);
                     
                        stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) {
							
							 if (!data1[w].equals("null")) {
								stmt.setString(k, data1[w]);
								k++;
							}
							
						}
						rset = stmt.executeQuery();

//			            delete if data match :
						if (rset.next()) {
							st_del = conn.prepareStatement(query_delete);
							bank_cd = rset.getString(1);
							bank_name = rset.getString(2);
							eff_dt = rset.getString(3);
							abbr = rset.getString(4);
							st_del.setString(1, rset.getString(1));
							st_del.setString(2, rset.getString(2));
							st_del.setString(3, rset.getString(3));
							st_del.executeUpdate();
							logger.data(fname, (bank_cd + ","+ bank_name + "," + abbr + " , " + eff_dt +","), conn, "");
							logger_count++;  
							st_del.close();
						}
						rset.close();
						stmt.close();
				}
				// left data:
				query_data_left = "SELECT BANK_CD,BANK_NAME,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'),BANK_ABBR FROM FMS_BANK_MST";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					bank_cd = rset.getString(1);
					bank_name = rset.getString(2);
					eff_dt = rset.getString(3);
					abbr = rset.getString(4);
					logger.data(fname,(bank_cd + ","+ bank_name + "," + abbr + " , " + eff_dt +","), conn, "N");

				}
				rset.close();
				stmt.close();

			    msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_BANK_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,"), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_BANK_MST()>>,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		} finally{
			file.close();
		}

	}
	
	
	public void FMS_ENTITY_TURNOVER_DTL() throws IOException, SQLException 
	{
		function_nm = "FMS_ENTITY_TURNOVER_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_ENTITY_TURNOVER_DTL>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_ENTITY_TURNOVER_DTL()>>,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			// query:delete
			query_delete = "DELETE FROM FMS_ENTITY_TURNOVER_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ENTITY = ?  AND FINANCIAL_YEAR = ? ";

		    // query for fetch column name
//		    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_TURNOVER_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			 column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY,FINANCIAL_YEAR,TURNOVER_CD,TURNOVER_FLAG,ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT";
			
			data1 = new String[ (column.split(",").length)-1 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_TURNOVER_DTL_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_TURNOVER_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				String financial_yr="";
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ENTITY,FINANCIAL_YEAR,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,FINANCIAL_YEAR FROM FMS_ENTITY_TURNOVER_DTL WHERE COMPANY_CD = '2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="";
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
					     if(cell.getColumnIndex() == 0 && !cell.getStringCellValue().equals("'SEIPL'")) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data.trim());
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else if(cell.getColumnIndex() == 0 && cell.getStringCellValue().equals("'SEIPL'"))
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {  data1[i]=cd1;
							 }else
							 {  data1[i] = data; 
							 }
					    	 
							 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						      i++;
							  
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
					
					if(!cd1.equals("")) 
					{			
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						    
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
					
//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);

						  company_cd=rset.getString(1);
						  cd=rset.getString(2);
						  entity=rset.getString(3);
						  financial_yr=rset.getString(4);
						
						  st_del.setString(1,rset.getString(2));
						  st_del.setString(2,rset.getString(3));
						  st_del.setString(3,rset.getString(4));
						  st_del.executeUpdate();
						  
						  logger.data(fname,company_cd+","+cd+","+entity+","+financial_yr+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
				
				// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,FINANCIAL_YEAR FROM FMS_ENTITY_TURNOVER_DTL ";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
		
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					entity=rset.getString(3);
					financial_yr=rset.getString(4);
					
					logger.data(fname, company_cd+","+cd+","+entity+","+financial_yr+",", conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_ENTITY_TURNOVER_DTL()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_ENTITY_TURNOVER_DTL()>>,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	
	public void FMS_SHIP_MST() throws IOException,SQLException {
		function_nm = "FMS_SHIP_MST()";
		try {
			column="";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SHIP_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SHIP_MST()>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			// query:delete
			query_delete = "DELETE FROM FMS_SHIP_MST WHERE EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND SHIP_CD=? AND SHIP_NAME = ?";

			// query for fetch column name
//			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_SHIP_MST'  ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) {
//				column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column ="EFF_DT,SHIP_CD,SHIP_NAME,SHIP_CALL_SIGN,SHIP_FLAG,SHIP_IMO_NO,SHIP_CLASS_SOC,INMARSAT_NO,SHIP_OWNER_NAME,SHIP_OPERATOR_NAME,SHIP_FAX_NO,SHIP_TELEX_NO,SHIP_EMAIL,GROSS_TONNAGE,CARGO_CAPACITY,VOLUME_UNIT,PERCENTAGE_CAPACITY,SHIP_ITEM,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,ENT_PROFILE,MOD_PROFILE";

			data1 = new String[column.split(",").length];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SHIP_MST_"+start_end_dt+".xlsx");
			if (file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SHIP_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				String ship_nm="";
				cd = "";eff_dt="";
				logger.checkpoint(fname, "EFF_DT,SHIP_CD,SHIP_NAME,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
						query_select = "SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'), SHIP_CD, SHIP_NAME FROM FMS_SHIP_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						int i = 0;
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);
							data1[i] = data;
							if (i == 1) {
							} else if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 &&  data.contains(":")  && data.length()==19) {
								query_select = query_select + column.split(",")[i]
										+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						}
						query_select = query_select.substring(0, query_select.length() - 4);
                   
                        stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) {
							if (w == 1) {
							} else if (!data1[w].equals("null")) {
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						rset = stmt.executeQuery();

//			            delete if data match :
						if (rset.next()) {
							
							st_del = conn.prepareStatement(query_delete);
							
							eff_dt = rset.getString(1);
							cd = rset.getString(2);
							ship_nm = rset.getString(3);
							
							st_del.setString(1, rset.getString(1));
							st_del.setString(2, rset.getString(2));
							st_del.setString(3, rset.getString(3));
							
							st_del.executeUpdate();
							
							logger.data(fname, (eff_dt+" , " +cd +" , "+ ship_nm+","), conn, "");
							logger_count++;  
							
							st_del.close();
						}
						rset.close();
						stmt.close();
				}
				// left data:
				query_data_left = "SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'), SHIP_CD, SHIP_NAME FROM FMS_SHIP_MST";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					eff_dt = rset.getString(1);
					cd = rset.getString(2);
					ship_nm = rset.getString(3);
					logger.data(fname,(eff_dt+" , " +cd +" , "+ ship_nm+","), conn, "N");

				}
				rset.close();
				stmt.close();
			    msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}

			conn.commit();

			System.out.println("<<ROLLBACK_END>><<FMS_SHIP_MST()>>");
			System.out.println();

			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,"), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SHIP_MST()>>,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally{
			file.close();
		}
	}
	
	
	public void FMS_ENTITY_CONTACT_MST() throws IOException, SQLException 
	{
		function_nm = "FMS_ENTITY_CONTACT_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_ENTITY_CONTACT_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_ENTITY_CONTACT_MST()>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query:delete
			query_delete = "DELETE FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ENTITY = ? AND SEQ_NO = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ADDR_FLAG = ? AND TYPE = ? ";

		    // query for fetch column name
//		    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_CONTACT_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			 column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,CONTACT_PERSON,DESIGNATION,PHONE,MOBILE,FAX_1,FAX_2,EMAIL,ADDR_FLAG,ADDL_ADDR_LINE,NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,ADDR_IS_ACTIVE,TO_NOM,TO_INV,TO_FM,TO_PM,TO_JT,TO_OTHER,RM_FLAG,TO_RM,TYPE,F402_FLAG,TO_F402";
			
			data1 = new String[ (column.split(",").length)-1 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_CONTACT_MST_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_CONTACT_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				String addr_flag="",type="";
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,EFF_DT,ADDR_FLAG,TYPE,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					int plant_num = 1;
					
					for (int p = 0; p < plant_num; p++) {
						cellIterator = row.cellIterator();
						i = 0;data = "";String cd1="";
						query_select ="";
						query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'),ADDR_FLAG,TYPE FROM FMS_ENTITY_CONTACT_MST WHERE COMPANY_CD = '2' AND ";
					while (cellIterator.hasNext()) 
					{   
						
						cell = cellIterator.next();
						
						 if(cell.getColumnIndex() == 0 && !cell.getStringCellValue().equals("'SEIPL'")) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else if(cell.getColumnIndex() == 0 && cell.getStringCellValue().equals("'SEIPL'"))
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {  data1[i]=cd1;
							 }
							 else if(i==11)
							 {
//								addr_flag = cell.getStringCellValue();
//						    	addr_flag = addr_flag.substring(1, addr_flag.length()-1);
								plant_num = data.split(",").length;
								data = data.split(",")[p];
								data1[i] = data;
							
							 }
							 else
							 {  
								 if(data.equals("")) {
									 data="null";
									 data1[i] = data;
								 }
								 else {data1[i] = data; }
							 }
						
							 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						      i++;
						 	
							  
					      }
					
				     }//while cell complete	
					
					query_select = query_select.substring(0, query_select.length()-4);
					
					if(!cd1.equals("")) 
					{			
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
					
//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);
						  company_cd=rset.getString(1);
						  cd=rset.getString(2);
						  entity=rset.getString(3);
						  seq_no=rset.getString(4);
						  eff_dt=rset.getString(5);
						  addr_flag=rset.getString(6);
						  type=rset.getString(7);
		
						  st_del.setString(1, cd);
						  st_del.setString(2, entity);
						  st_del.setString(3, seq_no);
						  st_del.setString(4, eff_dt);
						  st_del.setString(5, addr_flag);
						  st_del.setString(6, type);
						  st_del.executeUpdate();
						 
						  logger.data(fname,company_cd+","+cd+","+entity+","+seq_no+","+eff_dt+","+addr_flag+","+type+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//for loop
					 
				}//while row complete
				
				// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'),ADDR_FLAG,TYPE FROM FMS_ENTITY_CONTACT_MST";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
		
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					entity=rset.getString(3);
					seq_no=rset.getString(4);
					eff_dt=rset.getString(5);
					addr_flag=rset.getString(6);
					type=rset.getString(7); 
					
					logger.data(fname, company_cd+","+cd+","+entity+","+seq_no+","+eff_dt+","+addr_flag+","+type+",", conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_ENTITY_CONTACT_MST>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,,,,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_ENTITY_CONTACT_MST()>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	
	
	public void FMS_METER_MST() throws IOException, SQLException 
	{
		function_nm = "FMS_METER_MST()";
		try {
			column=" ";
			logger_count=0;
			int gail_dahej = 1, gailhazsei = 1, gail_haz = 1, pil_haz = 1, pil_bhad = 1, pil_ank = 1, pil_msk = 1;

			System.out.println("<<ROLLBACK_START>><<FMS_METER_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_METER_MST()>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			// query:delete
			query_delete = "DELETE FROM FMS_METER_MST WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND PLANT_SEQ = ? AND METER_TYPE = ? AND METER_SEQ = ? ";

		    // query for fetch column name
//		    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_METER_MST' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			 column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,METER_SEQ,METER_ID,METER_REF,SPECIFICATION,NOTE,STATUS,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY";
			
			data1 = new String[ (column.split(",").length)-1 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_METER_MST_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_METER_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				String  p_seq_no = "", meter_type = "", meter_seq_no = "", org_abbr = "";
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,METER_SEQ,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,METER_SEQ FROM FMS_METER_MST WHERE COMPANY_CD = '2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="";
					data = "";
					abbr = ""; cd = ""; p_seq_no = ""; meter_type = ""; meter_seq_no = ""; org_abbr = "";
					
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
						 if(cell.getColumnIndex() == 0 && !cell.getStringCellValue().equals("'SEIPL'")) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1); 
							 data=data.trim();
							 abbr=data;
							 org_abbr=data;
							 
							  if (mpe.transporter_map.containsKey(abbr)) {						          
						            for (String key : mpe.transporter_map.keySet()) { 
						                if (key.contains(abbr)) {  
						                    org_abbr = abbr;  
						                    abbr = mpe.transporter_map.get(abbr);					                  
						                }
						            }
						        } 
								

							 	if (mpe.meter_map.containsKey(org_abbr)) {						          
						            for (String key : mpe.meter_map.keySet()) { 
						                if (key.contains(org_abbr)) {   
						                    org_abbr = mpe.meter_map.get(org_abbr);
					                    
						                }
						            }
						        } 
							 
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,abbr);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		 
					 		
					     }
					     else if(cell.getColumnIndex() == 0 && cell.getStringCellValue().equals("'SEIPL'"))
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					     }
					     else 
					     {
					    	 	data = cell.getStringCellValue();
					    	 	data = data.substring(1, data.length() - 1);
					    	 	
					    	 	if (cell.getColumnIndex() == 4) {
					    	 		//DIYA 20250226:ADD PLANTSEQ_NO
					    	 		meter_seq_no = cell.getStringCellValue();
									meter_seq_no = meter_seq_no.substring(1, meter_seq_no.length()-1);
									
									query_String = "SELECT C.PLANT_ABBR FROM FMS_COUNTERPARTY_PLANT_DTL C, FMS_COUNTERPARTY_MST A WHERE A.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND A.COUNTERPARTY_ABBR = ? AND C.SEQ_NO = ? AND C.ENTITY = 'R' ";
									stmt = conn.prepareStatement(query_String);
									stmt.setString(1, abbr);
									stmt.setString(2, p_seq_no);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										if (rset.getString(1).equals("GAILHAZSEI")) {
											meter_seq_no = gailhazsei+"";
											gailhazsei++;
										}
										else if (rset.getString(1).equals("GAIL DAHEJ")) {
											meter_seq_no = gail_dahej+"";
											gail_dahej++;
										}
										else if (rset.getString(1).equals("GAIL HAZ")) {
											meter_seq_no = gail_haz+"";
											gail_haz++;
										}
										else if (rset.getString(1).equals("PIL MSK")) {
											meter_seq_no = pil_msk+"";
											pil_msk++;
										}
										else if (rset.getString(1).equals("PIL ANK")) {
											meter_seq_no = pil_ank+"";
											pil_ank++;
										}
										else if (rset.getString(1).equals("PIL BHAD")) {
											meter_seq_no = pil_bhad+"";
											pil_bhad++;
										}
										else if (rset.getString(1).equals("PIL HAZ")) {
											meter_seq_no = pil_haz+"";
											pil_haz++;
										}
									}
									
									rset.close();
									stmt.close();
									
									data = meter_seq_no;
							
								}
					    	 	if (cell.getColumnIndex() == 2) {
					    	 		p_seq_no = cell.getStringCellValue();
									p_seq_no = p_seq_no.substring(1, p_seq_no.length()-1);
									
									query_String = "SELECT SEQ_NO FROM FMS_COUNTERPARTY_PLANT_DTL WHERE PLANT_ABBR = ? AND ENTITY = 'R' ";
									stmt = conn.prepareStatement(query_String);
									stmt.setString(1, org_abbr);
									rset = stmt.executeQuery();
									
									if (rset.next()) {
										p_seq_no = rset.getString(1);
									}
									rset.close();
									stmt.close();
									data = p_seq_no;
								}
					    	 	if (cell.getColumnIndex() == 3) {
									meter_type = cell.getStringCellValue();
									meter_type = meter_type.substring(1, meter_type.length()-1);
									data=meter_type;
								}								
								else if (cell.getColumnIndex() == 5) 
								{
									data = cell.getStringCellValue().contains("'null'") ? null : cell.getStringCellValue();
									if(data != null) {
							    		data = data.substring(1, data.length()-1);
							    	}
									data = abbr + "-P" + p_seq_no + "-M" + meter_seq_no; 
								}

					    	 	if(i==0)
					    	 	{  data1[i]=cd1;
					    	 	}
					    	 	else if(cell.getColumnIndex() == 9 && cell.getStringCellValue().contains("*")) 
					    	 	{	
									data = "N";
									data1[i]=data;
					    	 	}
					    	 	else
					    	 	{  
					    	 		if(data.equals(""))
									 	{
					    	 			data="null";
					    	 			data1[i]=data;
									 	}
					    	 		else{
									 data1[i] = data;
					    	 			}
								 
					    	 	}
					    	 	
					    	 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						      i++;
							  
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
					
					if(!cd1.equals("")) 
					{			
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						    
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
					
//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);
						
						  //Here =>PLANT_SEQ=SEQ_NO
						  company_cd=rset.getString(1);
						  cd=rset.getString(2);
						  seq_no=rset.getString(3);
						  meter_type=rset.getString(4);
						  meter_seq_no=rset.getString(5);
						
						  st_del.setString(1, cd);
						  st_del.setString(2, seq_no);
						  st_del.setString(3, meter_type);
						  st_del.setString(4, meter_seq_no);
						  st_del.executeUpdate();
						  
						  logger.data(fname,company_cd+","+cd+","+seq_no+","+meter_type+","+meter_seq_no+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
				
				// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,PLANT_SEQ,METER_TYPE,METER_SEQ FROM FMS_METER_MST";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
		
					company_cd=rset.getString(1);
					cd=rset.getString(2);
					seq_no=rset.getString(3);
					meter_type=rset.getString(4);
					meter_seq_no=rset.getString(5);
					
					logger.data(fname, company_cd+","+cd+","+seq_no+","+meter_type+","+meter_seq_no+"," , conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_METER_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_METER_MST()>>,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	
	
	public void FMS_COUNTERPARTY_PLANT_TAX() throws IOException, SQLException 
	{
		function_nm = "FMS_COUNTERPARTY_PLANT_TAX()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_COUNTERPARTY_PLANT_TAX>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_COUNTERPARTY_PLANT_TAX()>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query:delete                                                                    
			query_delete = "DELETE FROM FMS_COUNTERPARTY_PLANT_TAX WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ENTITY = ? AND PLANT_SEQ_NO = ? AND STAT_CD = ? ";

		    // query for fetch column name
		    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_PLANT_TAX' ORDER BY COLUMN_ID";
			stmt = conn.prepareStatement(query_fetch_columnname);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
			 column += rset.getString(1) + ",";
			}
			rset.close();
			stmt.close();
			column ="COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";
			
			data1 = new String[ (column.split(",").length)-1 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_PLANT_TAX_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_PLANT_TAX_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				String stat_cd="";
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD FROM FMS_COUNTERPARTY_PLANT_TAX WHERE COMPANY_CD = '2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="";
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
						 if(cell.getColumnIndex() == 0 && !cell.getStringCellValue().equals("'SEIPL'") ) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else if(cell.getColumnIndex() == 0 && cell.getStringCellValue().equals("'SEIPL'"))
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {  data1[i]=cd1;
							 }
							 else if(i==3)
							 {
								 if(data.toUpperCase().contains("GVAT TIN") || data.toUpperCase().contains("GST TIN")) {
									 data = "VAT TIN No.-S";
									}
									query_String = "SELECT STAT_CD, UPPER(STAT_NM), STAT_TYPE FROM FMS_GOVT_STAT_TAX ";
									stmt = conn.prepareStatement(query_String);
									rset = stmt.executeQuery();
									
									while (rset.next()) {
										if (data.contains("-") && data.split("-")[0].toUpperCase().contains(rset.getString(2)) && data.split("-")[1].toUpperCase().contains(rset.getString(3))) 
										{
											stat_cd = rset.getString(1);
										}
									}
									rset.close();
									stmt.close();

						 		    data1[i]=stat_cd;
						 		 
							 }
							 else	 
							 {  data1[i] = data; 
							 }
					    	
							 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						      i++;
							  
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
					
					if(!cd1.equals("")) 
					{	
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						    
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();

//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);
						 
						  //Here =>PLANT_SEQ=SEQ_NO
						  company_cd=rset.getString(1);
						  cd=rset.getString(2);
						  entity=rset.getString(3);
						  seq_no=rset.getString(4);
						  stat_cd=rset.getString(5);
						  
						  st_del.setString(1, cd);
						  st_del.setString(2, entity);
						  st_del.setString(3, seq_no);
						  st_del.setString(4, stat_cd);
						 
						  st_del.executeUpdate();
						
						  logger.data(fname,company_cd+","+cd+","+entity+","+seq_no+","+stat_cd+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
					
					// LEFT DATA
					query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD FROM FMS_COUNTERPARTY_PLANT_TAX";
					stmt = conn.prepareStatement(query_data_left);
					rset = stmt.executeQuery();
					while (rset.next()) {
						company_cd=rset.getString(1);
						cd=rset.getString(2);
						entity=rset.getString(3);
						seq_no=rset.getString(4);
						stat_cd=rset.getString(5);
						
						logger.data(fname, company_cd+","+cd+","+entity+","+seq_no+","+stat_cd+"," , conn, "N");
						
					}
					rset.close();
					stmt.close();

					msg = "Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_COUNTERPARTY_PLANT_TAX()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_COUNTERPARTY_PLANT_TAX()>>,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	public void FMS_COUNTERPARTY_BU_TAX() throws IOException, SQLException 
	{
		function_nm = "FMS_COUNTERPARTY_BU_TAX()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_COUNTERPARTY_BU_TAX>>");
			logger.checkpoint(fname,"\n<<ROLLBACK_START>>,<<FMS_COUNTERPARTY_BU_TAX()>>,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query:delete                                                                    
			query_delete = "DELETE FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ENTITY = ? AND PLANT_SEQ_NO = ? AND STAT_CD = ? ";

		    // query for fetch column name
//		    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_COUNTERPARTY_BU_TAX' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			 column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,STAT_NO,EFF_DT,FLAG,REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY";
			
			data1 = new String[ (column.split(",").length)-1 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_BU_TAX_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_COUNTERPARTY_BU_TAX_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				String stat_cd="";
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD FROM FMS_COUNTERPARTY_BU_TAX WHERE COMPANY_CD = '2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="";
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
						 if(cell.getColumnIndex() == 0 && !cell.getStringCellValue().equals("'SEIPL'") ) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else if(cell.getColumnIndex() == 0 && cell.getStringCellValue().equals("'SEIPL'"))
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COMPANY_CD FROM FMS_COMPANY_OWNER_MST WHERE COMPANY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {  data1[i]=cd1;
							 }
							 else if(i==3)
							 {
								 if(data.toUpperCase().contains("GVAT TIN") || data.toUpperCase().contains("GST TIN")) {
									 data = "VAT TIN No.-S";
									}
									query_String = "SELECT STAT_CD, UPPER(STAT_NM), STAT_TYPE FROM FMS_GOVT_STAT_TAX ";
									stmt = conn.prepareStatement(query_String);
									rset = stmt.executeQuery();
									
									while (rset.next()) {
										if (data.contains("-") && data.split("-")[0].toUpperCase().contains(rset.getString(2)) && data.split("-")[1].toUpperCase().contains(rset.getString(3))) 
										{
											stat_cd = rset.getString(1);
										}
									}
									rset.close();
									stmt.close();

						 		    data1[i]=stat_cd;
						 		 
							 }
							 else	 
							 {  data1[i] = data; 
							 }
					    	
							 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						      i++;
							  
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
			
					if(!cd1.equals("")) 
					{
						for(int k=0;k<data1.length;k++)
						{
							if(data1[1].contains("B")) {
							System.out.println("=>:"+k+":("+data1[k]+")");}
						}
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						    
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
					
//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);
						  //Here =>PLANT_SEQ=SEQ_NO
						  company_cd=rset.getString(1);
						  cd=rset.getString(2);
						  entity=rset.getString(3);
						  seq_no=rset.getString(4);
						  stat_cd=rset.getString(5);
						 
						  st_del.setString(1, cd);
						  st_del.setString(2, entity);
						  st_del.setString(3, seq_no);
						  st_del.setString(4, stat_cd);
						 
						  st_del.executeUpdate();
					
						  logger.data(fname,company_cd+","+cd+","+entity+","+seq_no+","+stat_cd+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
					
					// LEFT DATA
					query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,STAT_CD FROM FMS_COUNTERPARTY_BU_TAX";
					stmt = conn.prepareStatement(query_data_left);
					rset = stmt.executeQuery();
					while (rset.next()) {
						company_cd=rset.getString(1);
						cd=rset.getString(2);
						entity=rset.getString(3);
						seq_no=rset.getString(4);
						stat_cd=rset.getString(5);
						
						logger.data(fname, company_cd+","+cd+","+entity+","+seq_no+","+stat_cd+"," , conn, "N");
						
					}
					rset.close();
					stmt.close();

					msg = "Data has been Rollbacked Successfully from Database.";
					msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_COUNTERPARTY_BU_TAX()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_COUNTERPARTY_BU_TAX()>>,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	
	
	
	public void FMS_ENTITY_TAX_STRUCT_DTL() throws IOException, SQLException 
	{
		function_nm = "FMS_ENTITY_TAX_STRUCT_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_ENTITY_TAX_STRUCT_DTL>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_ENTITY_TAX_STRUCT_DTL()>>,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query:delete
			query_delete = "DELETE FROM FMS_ENTITY_TAX_STRUCT_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ENTITY = ? AND PLANT_SEQ_NO = ? AND BU_UNIT = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') ";

		    // query for fetch column name
//		    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_TAX_STRUCT_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			 column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TAX_STRUCT_DT,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT,SAP_TAX_CODE";
			
			data1 = new String[ (column.split(",").length)-1 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_TAX_STRUCT_DTL_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_TAX_STRUCT_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				String bu_unit="";
				String app_date="";
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,BU_UNIT,EFF_DT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,BU_UNIT,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_ENTITY_TAX_STRUCT_DTL WHERE COMPANY_CD = '2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="",tax_cd="";
					data = "";app_date="";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
					     if(cell.getColumnIndex() == 0) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data.trim());
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {  data1[i]=cd1;
							 }
							 else if(i==5) {
								 
								 data1[i] = data; 
								 query_String="SELECT TAX_STR_CD,TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
						    	 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,data);
						 		 rset = stmt.executeQuery(); 
						 		 
						 		 if(rset.next())
						 		 { 
						 		 tax_cd=rset.getString(1);
						 		 app_date=rset.getString(2);
						 		 data1[3]=tax_cd;
						 		 data1[4]=app_date;
						 		 }
						 		 
						 		 stmt.close();
						 		 rset.close();
							 }
							 else
							 {  data1[i] = data; 
							 }
							
					    	 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						      i++;
							  
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
				
					
					if(!cd1.equals("") && !tax_cd.equals("")) 
					{	
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						     
						     if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
						     
					   }
					   rset = stmt.executeQuery();
					   
//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);
						//HERE PLANT_SEQ_NO=SEQ_NO
						  company_cd=rset.getString(1);
						  cd=rset.getString(2);
						  entity=rset.getString(3);
						  seq_no=rset.getString(4);
						  bu_unit=rset.getString(5);
						  eff_dt=rset.getString(6);
						 
						  st_del.setString(1, cd);
						  st_del.setString(2, entity);
						  st_del.setString(3, seq_no);
						  st_del.setString(4, bu_unit);
						  st_del.setString(5, eff_dt);
						  st_del.executeUpdate();
						 
						  logger.data(fname,company_cd+","+cd+","+entity+","+seq_no+","+bu_unit+","+eff_dt+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
				
				// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,BU_UNIT,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss') FROM FMS_ENTITY_TAX_STRUCT_DTL";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
					  company_cd=rset.getString(1);
					  cd=rset.getString(2);
					  entity=rset.getString(3);
					  seq_no=rset.getString(4);
					  bu_unit=rset.getString(5);
					  eff_dt=rset.getString(6);
					
					logger.data(fname,company_cd+","+cd+","+entity+","+seq_no+","+bu_unit+","+eff_dt+",", conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_ENTITY_TAX_STRUCT_DTL()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,,,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_ENTITY_TAX_STRUCT_DTL()>>,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	
	
	public void FMS_ENTITY_SERVICE_TAX_DTL() throws IOException, SQLException 
	{
		function_nm = "FMS_ENTITY_SERVICE_TAX_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_ENTITY_SERVICE_TAX_DTL>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_ENTITY_SERVICE_TAX_DTL()>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query:delete
			query_delete = "DELETE FROM FMS_ENTITY_SERVICE_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ENTITY = ?  AND PLANT_SEQ_NO = ? AND INVOICE_TYPE = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND BU_UNIT = ?";

		    // query for fetch column name
//		    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_SERVICE_TAX_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			 column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TAX_STRUCT_DT,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT,SAP_TAX_CODE";
			
			data1 = new String[ (column.split(",").length)-1 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_SERVICE_TAX_DTL_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_SERVICE_TAX_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				String bu_unit="",invoice_type="";
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,INVOICE_TYPE,EFF_DT,BU_UNIT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,INVOICE_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'),BU_UNIT FROM FMS_ENTITY_SERVICE_TAX_DTL WHERE COMPANY_CD = '2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="",tax_cd="",app_date="";
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
					     if(cell.getColumnIndex() == 0) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {  data1[i]=cd1;
							 }
							 else if(i==5)
							 {
								 if(data.equals("null"))
							     {data1[i] = "0";
							     data="0";}
								 else {
								 data1[i] = data;} 
								 
							 }
							 else if(i==6){
								 data1[i] = data; 
								 query_String="SELECT TAX_STR_CD, TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
						    	 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,data);
						 		 rset = stmt.executeQuery(); 
						 		 
						 		 if(rset.next())
						 		 { 
						 		 tax_cd=rset.getString(1);
						 		 app_date=rset.getString(2);
						 		 data1[3]=tax_cd;
						 		 data1[4]=app_date;
						 		 }
						 		 
						 		 stmt.close();
						 		 rset.close();
							 }
							 else
							 {  data1[i] = data; 
							 }
							 
					    	 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						      i++;
							  
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
					
					if(!cd1.equals("") && !tax_cd.equals("")) 
					{	
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						     
					         if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
				
//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);
						  
						  //HERE PLANT_SEQ_NO=SEQ_NO
						  company_cd=rset.getString(1);
						  cd=rset.getString(2);
						  entity=rset.getString(3);
						  seq_no=rset.getString(4);
						  invoice_type=rset.getString(5);
						  eff_dt=rset.getString(6);
						  bu_unit=rset.getString(7);
						 
						  st_del.setString(1, cd);
						  st_del.setString(2, entity);
						  st_del.setString(3, seq_no);
						  st_del.setString(4, invoice_type);
						  st_del.setString(5, eff_dt);
						  st_del.setString(6, bu_unit);
						  st_del.executeUpdate();
						 
						  logger.data(fname,company_cd+","+cd+","+entity+","+seq_no+","+invoice_type+","+eff_dt+","+bu_unit+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
				
				// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,INVOICE_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'),BU_UNIT FROM FMS_ENTITY_SERVICE_TAX_DTL";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
					 company_cd=rset.getString(1);
					  cd=rset.getString(2);
					  entity=rset.getString(3);
					  seq_no=rset.getString(4);
					  invoice_type=rset.getString(5);
					  eff_dt=rset.getString(6);
					  bu_unit=rset.getString(7);
					
					logger.data(fname,company_cd+","+cd+","+entity+","+seq_no+","+invoice_type+","+eff_dt+","+bu_unit+",", conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_ENTITY_SERVICE_TAX_DTL()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,,,,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_ENTITY_SERVICE_TAX_DTL()>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	
	
	public void FMS_ENTITY_BU_SVC_TAX_DTL() throws IOException, SQLException 
	{
		function_nm = "FMS_ENTITY_BU_SVC_TAX_DTL()";
		try {
			column="";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_ENTITY_BU_SVC_TAX_DTL>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_ENTITY_BU_SVC_TAX_DTL()>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query:delete
			query_delete = "DELETE FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND COUNTERPARTY_CD = ? AND ENTITY = ?  AND PLANT_SEQ_NO = ? AND INVOICE_TYPE = ? AND EFF_DT = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND BU_UNIT = ?";

		    // query for fetch column name
//		    query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_ENTITY_BU_SVC_TAX_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			 column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,TAX_STRUCT_CD,TAX_STRUCT_DT,INVOICE_TYPE,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,BU_UNIT,EFF_DT,SAP_TAX_CODE";
			
			data1 = new String[ (column.split(",").length)-1 ];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_ENTITY_BU_SVC_TAX_DTL_"+start_end_dt+".xlsx");
			if(file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_ENTITY_BU_SVC_TAX_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				String bu_unit="",invoice_type="",app_date="";
				
				logger.checkpoint(fname, "COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,INVOICE_TYPE,EFF_DT,BU_UNIT,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,INVOICE_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'),BU_UNIT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE COMPANY_CD = '2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					int i = 0;
					String cd1="",tax_cd="";
					data = "";
					
					while (cellIterator.hasNext()) 
					{   
						cell = cellIterator.next();
					     if(cell.getColumnIndex() == 0) 
					     {
					    	 data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 
					    	 query_String="SELECT COUNTERPARTY_CD FROM FMS_COUNTERPARTY_MST WHERE COUNTERPARTY_ABBR=? ";
					    	 stmt=conn.prepareStatement(query_String);
					 		 stmt.setString(1,data);
					 		 rset = stmt.executeQuery(); 
					 		 if(rset.next())
					 		 { 
					 		 cd1=rset.getString(1);
					 		 }
					 		 stmt.close();
					 		 rset.close();
					 		
					     }
					     else 
					     {
						     data = cell.getStringCellValue();
							 data = data.substring(1, data.length() - 1);
							 if(i==0)
							 {  data1[i]=cd1;
							 }
							 else if(i==5)
							 {
								 if(data.equals("null"))
							     {data1[i] = "0";
							     data="0";}
								 else {
								 data1[i] = data;} 
								 
								 
							 }
							 else if(i==6){
								 int flag = 0;
									
									query_String = "SELECT TAX_STR_CD, DESCR FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
									stmt = conn.prepareStatement(query_String);
									stmt.setString(1, "%"+data.split(", ")[0]+"%");
									rset = stmt.executeQuery();
									
									while (rset.next()) {
										
										for (int m = 0; m < data.split(", ").length; m++) {
											if (rset.getString(2).contains(data.split(", ")[m])) {
												flag = 1;
											}
											else {
												flag = 0;
												break;
											}
										}
										
										if (flag == 1) {
											data = rset.getString(2);
											break;
										}
									}
									
									rset.close();
									stmt.close();
								 
								 data1[i] = data; 
								 query_String="SELECT TAX_STR_CD,TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') FROM FMS_TAX_STRUCTURE WHERE DESCR = ? ";
						    	 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,data);
						 		 rset = stmt.executeQuery(); 
						 		 
						 		 if(rset.next())
						 		 { 
						 		 tax_cd=rset.getString(1);
						 		 app_date=rset.getString(2);
						 		 data1[3]=tax_cd;
						 		 data1[4]=app_date;
						 		 }
						 		 
						 		 stmt.close();
						 		 rset.close();
							 }
							 else
							 {  data1[i] = data; 
							 }
							 
					    	 if (data.equals("null")) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " IS NULL AND ";
							 }else if (data.split("/").length == 3  &&  data.contains(":")  && data.length()==19) 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							 }else 
							 {
							   query_select = query_select + column.split(",")[i+1] + " = ? AND ";
							 }
						      i++;
							  
					      }
				     }//while cell complete	
					query_select = query_select.substring(0, query_select.length() - 4);
					
					
					if(!cd1.equals("") && !tax_cd.equals("")) 
					{	
					    stmt = conn.prepareStatement(query_select);
					    int k = 1;
					    for (int w = 0; w < ((column.split(",").length) -1); w++) 
					    {
						     
					         if (!data1[w].equals("null")) {		
							 stmt.setString(k, data1[w]);
							 k++;
						     }
					   }
					   rset = stmt.executeQuery();
				
//		               delete if data match :
					   if (rset.next()) 
					   {
						  st_del = conn.prepareStatement(query_delete);
						  
						  //HERE PLANT_SEQ_NO=SEQ_NO
						  company_cd=rset.getString(1);
						  cd=rset.getString(2);
						  entity=rset.getString(3);
						  seq_no=rset.getString(4);
						  invoice_type=rset.getString(5);
						  eff_dt=rset.getString(6);
						  bu_unit=rset.getString(7);
						 
						  st_del.setString(1, cd);
						  st_del.setString(2, entity);
						  st_del.setString(3, seq_no);
						  st_del.setString(4, invoice_type);
						  st_del.setString(5, eff_dt);
						  st_del.setString(6, bu_unit);
						  st_del.executeUpdate();
						 
						  logger.data(fname,company_cd+","+cd+","+entity+","+seq_no+","+invoice_type+","+eff_dt+","+bu_unit+",", conn, "");
						  logger_count++;  
						
						  st_del.close();
					    }
					    rset.close();
					    stmt.close();
					}
					
				}//while row completed
				
				// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY,PLANT_SEQ_NO,INVOICE_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss'),BU_UNIT FROM FMS_ENTITY_BU_SVC_TAX_DTL";
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				while (rset.next()) {
					 company_cd=rset.getString(1);
					  cd=rset.getString(2);
					  entity=rset.getString(3);
					  seq_no=rset.getString(4);
					  invoice_type=rset.getString(5);
					  eff_dt=rset.getString(6);
					  bu_unit=rset.getString(7);
					
					logger.data(fname,company_cd+","+cd+","+entity+","+seq_no+","+invoice_type+","+eff_dt+","+bu_unit+",", conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_ENTITY_BU_SVC_TAX_DTL()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,,,,,"), conn);
			logger.checkpoint(fname, "\n<<ROLLBACK_END>>,<<FMS_ENTITY_BU_SVC_TAX_DTL()>>,,,,,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		}finally{
			if(file != null)
			{file.close();}
		}
	}
	
	public void FMS_CUSTOM_TAX_STRUCT_DTL() throws SQLException, IOException {
		function_nm = "FMS_CUSTOM_TAX_STRUCT_DTL()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_CUSTOM_TAX_STRUCT_DTL()>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_CUSTOM_TAX_STRUCT_DTL>>,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);
			
			// query delete:
			query_delete = "DELETE FROM FMS_CUSTOM_TAX_STRUCT_DTL WHERE COMPANY_CD= '2' AND EFF_DT=TO_DATE(?,'DD/MM/YYYY hh24:mi:ss')";
			
			// column select:
//			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_CUSTOM_TAX_STRUCT_DTL' ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) 
//			{
//			  column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "EFF_DT,TAX_STRUCT_CD,TAX_STRUCT_DT,TAX_STRUCT_DTL,TAX_STRUCT_REMARK,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,SAP_TAX_CODE,COMPANY_CD";
			
			company_cd="";
			
			data1 = new String[(column.split(",").length)-1];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_CUSTOM_TAX_STRUCT_DTL_"+start_end_dt+".xlsx");
			if (file1.exists()) 
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_CUSTOM_TAX_STRUCT_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				
				String tax_cd="";
				
				logger.checkpoint(fname, "COMPANY_CD,TAX_STRUCT_CD,TIMESTAMP", conn);
				
				while (rowIterator.hasNext()) 
				{
					query_select = "SELECT COMPANY_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss')  FROM FMS_CUSTOM_TAX_STRUCT_DTL WHERE COMPANY_CD='2' AND ";
					row = rowIterator.next();
					cellIterator = row.cellIterator();
					
					int i=0;
					data = "";
					String app_date="";
					
					while (cellIterator.hasNext()) 
					{   	cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);
							
							if(cell.getColumnIndex()==11) {}
							else if(cell.getColumnIndex()==3)
							{
								int flag = 0;
									
								query_String = "SELECT TAX_STR_CD, DESCR FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
								stmt = conn.prepareStatement(query_String);
								stmt.setString(1, "%"+data.split(", ")[0]+"%");
								rset = stmt.executeQuery();
									
								while (rset.next()) 
								{
									 for(int m = 0; m < data.split(", ").length; m++) 
									 {
											if (rset.getString(2).contains(data.split(", ")[m])) {
												flag = 1;
											}
											else {
												flag = 0;
												break;
											}
									  }
									 if (flag == 1) {
										 data = rset.getString(2);
										 break;
										}
								}
									
								rset.close();
								stmt.close();
									
								
								data1[i]=data;

								 query_String="SELECT TAX_STR_CD,TO_CHAR(APP_DATE, 'DD/MM/YYYY HH24:MI:SS') DESCR FROM FMS_TAX_STRUCTURE WHERE DESCR LIKE ? ";
						    	 stmt=conn.prepareStatement(query_String);
						 		 stmt.setString(1,"%"+data+"%");
						 		 rset = stmt.executeQuery(); 
						 		 if(rset.next())
						 		 { 
						 		 tax_cd=rset.getString(1);
						 		 app_date=rset.getString(2);
						 		 data1[1]=tax_cd;
						 		 data1[2]=app_date; 
						
						 		 }
						 		 stmt.close();
						 		 rset.close();
								
							}
							else{data1[i] = data;}
					
							if(cell.getColumnIndex()==11) {}
							else if (data.equals("null")) {
								 query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							 }else if (data.split("/").length == 3 &&  data.contains(":")  && data.length()==19) {
								 query_select = query_select + column.split(",")[i] + " = TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";} 
							 else {
							  query_select = query_select + column.split(",")[i] + " = ? AND ";
							 }
						     i++;
					     
				     }//while(column) completed
					 query_select = query_select.substring(0, query_select.length() - 4);
					
					 
					 if(!tax_cd.equals("null"))
					 {
						 stmt = conn.prepareStatement(query_select);
						 int k = 1;
						 for (int w = 0; w < ( (column.split(",").length) -1 ); w++)
						 {
					        	if (!data1[w].equals("null"))
					        	{	stmt.setString(k, data1[w]);
							        k++; 
						         }
						 }
						 rset = stmt.executeQuery();
						     
//  					delete when data match :
						 if (rset.next())
						 {
							 company_cd=rset.getString(1);
							 eff_dt=rset.getString(2);

							 st_del = conn.prepareStatement(query_delete);   
							 st_del.setString(1, rset.getString(2));
							
							 st_del.executeUpdate();	
						       
							 logger.data(fname, (company_cd+","+eff_dt+","), conn, "");
							 logger_count++;

							 st_del.close();
						 }
						 rset.close();
						 stmt.close();
					 
					 }
				}//while(row) completed
				
				// LEFT DATA
				query_data_left = "SELECT COMPANY_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY hh24:mi:ss')  FROM FMS_CUSTOM_TAX_STRUCT_DTL";
				
				stmt = conn.prepareStatement(query_data_left);
				rset = stmt.executeQuery();
				
				while (rset.next()) {
					company_cd=rset.getString(1);
					eff_dt=rset.getString(2);
					
					logger.data(fname, (company_cd+","+eff_dt+","), conn, "N");
				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_CUSTOM_TAX_STRUCT_DTL()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+","), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_CUSTOM_TAX_STRUCT_DTL()>>,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			}
		catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally {
			if(file !=null)
			{file.close();}
		}
		
	}
	
	public void FMS_HOLIDAY_DTL() throws IOException, SQLException {
		function_nm = "FMS_HOLIDAY_DTL()";
		try {
			column="";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_HOLIDAY_DTL>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_HOLIDAY_DTL()>>,,", conn);
			
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);

			// query:delete
			query_delete = "DELETE FROM FMS_HOLIDAY_DTL WHERE HOLIDAY_DT=TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND HOLIDAY_NM = ? AND STATE_TIN=?";

			// query for fetch column name
			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_HOLIDAY_DTL' ORDER BY COLUMN_ID ";
			stmt = conn.prepareStatement(query_fetch_columnname);
			rset = stmt.executeQuery();
			while (rset.next()) {
				column += rset.getString(1) + ",";
			}
			rset.close();
			stmt.close();
			column = "HOLIDAY_DT,HOLIDAY_NM,HOLIDAY_DAY,STATE_TIN,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,FLAG,ENT_PROFILE,MOD_PROFILE";

			data1 = new String[column.split(",").length];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_HOLIDAY_DTL_"+start_end_dt+".xlsx");
			if (file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_HOLIDAY_DTL_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				String  holi_dt = "", holi_nm = "",state_tin = "";
				logger.checkpoint(fname, "HOLIDAY_DATE,HOLIDAY_NAME,STATE_TIN,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
						query_select = "SELECT TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY hh24:mi:ss'),HOLIDAY_NM,STATE_TIN FROM FMS_HOLIDAY_DTL WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						int i = 0;
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);
							data1[i] = data;					
							if (data.equals("null")) {
								query_select = query_select + column.split(",")[i] + " IS NULL AND ";
							} else if (data.split("/").length == 3 &&  data.contains(":")  && data.length()==19) {
								query_select = query_select + column.split(",")[i]
										+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
							} else {
								query_select = query_select + column.split(",")[i] + " =? AND ";
							}
							i++;
						}
						query_select = query_select.substring(0, query_select.length() - 4);
                   
                        stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) {
							if (!data1[w].equals("null")) {
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						rset = stmt.executeQuery();

//			            delete if data match :
						if (rset.next()) {
							st_del = conn.prepareStatement(query_delete);
							holi_dt = rset.getString(1);
							holi_nm = rset.getString(2);
							state_tin = rset.getString(3);
							
							st_del.setString(1, rset.getString(1));
							st_del.setString(2, rset.getString(2));
							st_del.setString(3, rset.getString(3));
							st_del.executeUpdate();
							
							logger.data(fname, (holi_dt + ","+holi_nm +","+state_tin+","), conn, "");
							logger_count++;  
							st_del.close();
						}
						rset.close();
						stmt.close();
				}
				// left data:
				query_data_left = "SELECT TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY hh24:mi:ss'),HOLIDAY_NM,STATE_TIN FROM FMS_HOLIDAY_DTL";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					holi_dt = rset.getString(1);
					holi_nm = rset.getString(2);
					state_tin = rset.getString(3);
					logger.data(fname, (holi_dt + ","+holi_nm +","+state_tin+","), conn, "N");

				}
				rset.close();
				stmt.close();
				
				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_HOLIDAY_DTL()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+",,"), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_HOLIDAY_DTL()>>,,", conn);
			
			logger.checkpoint1(fname1,logger_count+",", conn);
			
		} catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
		} finally{
			file.close();
		}
	}
	

	public void FMS_PRODUCT_MST() throws IOException, SQLException {
		function_nm = "FMS_PRODUCT_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_PRODUCT_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_PRODUCT_MST()>>,", conn);
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);//DIYA 20250226:ADD CHECKPOINT


			logger.checkpoint(fname, "PROD_NM,PROD_ABBR,PROD_DESC,TIMESTAMP", conn);

			// DOM GAS (CAPPED)
			query_delete = "DELETE FROM FMS_PRODUCT_MST WHERE PROD_NM = 'Domestic Gas (Capped)' AND PROD_ABBR = 'DOM GAS (CAPPED)' AND PROD_DESC = 'Used to identify molecules supplied from DOM gas capped' AND ENT_BY = '1' AND MOD_BY IS NULL AND MOD_DT IS NULL AND PROD_FLAG = 'Y' AND ENT_PROFILE = ? AND MOD_PROFILE IS NULL ";
			st_del = conn.prepareStatement(query_delete);
			st_del.setString(1, company_cd);
			if(st_del.executeUpdate() > 0) {
				logger.data(fname, ("Domestic Gas (Capped),DOM GAS (CAPPED),Used to identify molecules supplied from DOM gas capped,"), conn, "");
				logger_count++;  
			}
			
			st_del.close();

			// RLNG
			query_delete = "DELETE FROM FMS_PRODUCT_MST WHERE PROD_NM = 'Regassified LNG' AND PROD_ABBR = 'RLNG' AND PROD_DESC IS NULL AND ENT_BY = '1' AND MOD_BY IS NULL AND MOD_DT IS NULL AND PROD_FLAG = 'Y' AND ENT_PROFILE = ? AND MOD_PROFILE IS NULL ";
			st_del = conn.prepareStatement(query_delete);
			st_del.setString(1, company_cd);
			
			if(st_del.executeUpdate() > 0) {
				logger.data(fname, ("Regassified LNG,RLNG,,"), conn, "");
				logger_count++;  
			}
			
			st_del.close();

			// DLNG
			query_delete = "DELETE FROM FMS_PRODUCT_MST WHERE PROD_NM = 'Downstream LNG' AND PROD_ABBR = 'DLNG' AND PROD_DESC IS NULL AND ENT_BY = '1' AND MOD_BY IS NULL AND MOD_DT IS NULL AND PROD_FLAG = 'Y' AND ENT_PROFILE = ? AND MOD_PROFILE IS NULL ";
			st_del = conn.prepareStatement(query_delete);
			st_del.setString(1, company_cd);
			
			if(st_del.executeUpdate() > 0) {
				logger.data(fname, ("Downstream LNG,DLNG,,"), conn, "");
				logger_count++;  
			}
			
			st_del.close();

			// DOMGAS
			query_delete = "DELETE FROM FMS_PRODUCT_MST WHERE PROD_NM = 'Domestic Gas' AND PROD_ABBR = 'DOMGAS' AND PROD_DESC IS NULL AND ENT_BY = '1' AND MOD_BY IS NULL AND MOD_DT IS NULL AND PROD_FLAG = 'Y' AND ENT_PROFILE = ? AND MOD_PROFILE IS NULL ";
			st_del = conn.prepareStatement(query_delete);
			st_del.setString(1, company_cd);
			
			if(st_del.executeUpdate() > 0) {
				logger.data(fname, ("Domestic Gas,DOMGAS,,"), conn, "");
				logger_count++;  
			}
			
			st_del.close();
			
			msg = "Data has been Rollbacked Successfully from Database.";
			msg_type = "S";
		
			
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_PRODUCT_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+","), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_PRODUCT_MST()>>,", conn);
			logger.checkpoint1(fname1,logger_count+",", conn);//DIYA 20250226:ADD CHECKPOINT
			
			
		} catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally
		{
			if(file !=null)
			{file.close();}
		}

	}
	

	public void FMS_PRODUCT_MOLECULE_MST() throws IOException, SQLException {
		function_nm = "FMS_PRODUCT_MOLECULE_MST()";
		try {
			column=" ";
			logger_count=0;
			
			int prod_cd = 0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_PRODUCT_MOLECULE_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_PRODUCT_MOLECULE_MST()>>,", conn);
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);//DIYA 20250226:ADD CHECKPOINT

			
			logger.checkpoint(fname, "MOLE_NM,MOLE_ABBR,TIMESTAMP", conn);

			// D6 - DOM GAS (CAPPED)
			query_select = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_ABBR = 'DOM GAS (CAPPED)' ";
			stmt = conn.prepareStatement(query_select);
			rset = stmt.executeQuery();
			
			if(rset.next()) {
				prod_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			query_delete = "DELETE FROM FMS_PRODUCT_MOLECULE_MST WHERE PROD_CD = ? AND MOLE_NM = 'D6' AND MOLE_ABBR = 'D6' AND MOLE_DESC IS NULL AND ENT_BY = '1' AND MOD_BY IS NULL AND MOD_DT IS NULL AND MOLE_FLAG = 'Y' AND ENT_PROFILE = ? AND MOD_PROFILE IS NULL ";
			st_del = conn.prepareStatement(query_delete);
			st_del.setInt(1, prod_cd);
			st_del.setString(2, company_cd);
			if(st_del.executeUpdate() > 0) {
				logger.data(fname, ("D6,D6,"), conn, "");
				logger_count++;  
			}
			
			st_del.close();
			
			

			// D6 - DOMGAS
			query_select = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_ABBR = 'DOMGAS' ";
			stmt = conn.prepareStatement(query_select);
			rset = stmt.executeQuery();
			
			if(rset.next()) {
				prod_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			query_delete = "DELETE FROM FMS_PRODUCT_MOLECULE_MST WHERE PROD_CD = ? AND MOLE_NM = 'D6' AND MOLE_ABBR = 'D6' AND MOLE_DESC IS NULL AND ENT_BY = '1' AND MOD_BY IS NULL AND MOD_DT IS NULL AND MOLE_FLAG = 'Y' AND ENT_PROFILE = ? AND MOD_PROFILE IS NULL ";
			st_del = conn.prepareStatement(query_delete);
			st_del.setInt(1, prod_cd);
			st_del.setString(2, company_cd);
			if(st_del.executeUpdate() > 0) {
				logger.data(fname, ("D6,D6,"), conn, "");
				logger_count++;  
			}
			
			st_del.close();
			
			

			// Imported - RLNG
			query_select = "SELECT PROD_CD FROM FMS_PRODUCT_MST WHERE PROD_ABBR = 'RLNG' ";
			stmt = conn.prepareStatement(query_select);
			rset = stmt.executeQuery();
			
			if(rset.next()) {
				prod_cd = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			query_delete = "DELETE FROM FMS_PRODUCT_MOLECULE_MST WHERE PROD_CD = ? AND MOLE_NM = 'Imported' AND MOLE_ABBR = 'Imported' AND MOLE_DESC IS NULL AND ENT_BY = '1' AND MOD_BY IS NULL AND MOD_DT IS NULL AND MOLE_FLAG = 'Y' AND ENT_PROFILE = ? AND MOD_PROFILE IS NULL ";
			st_del = conn.prepareStatement(query_delete);
			st_del.setInt(1, prod_cd);
			st_del.setString(2, company_cd);
			if(st_del.executeUpdate() > 0) {
				logger.data(fname, ("D6,D6,"), conn, "");
				logger_count++;  
			}
			
			st_del.close();

			
			msg = "Data has been Rollbacked Successfully from Database.";
			msg_type = "S";
		
			
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_PRODUCT_MOLECULE_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+","), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_PRODUCT_MOLECULE_MST()>>,", conn);
			logger.checkpoint1(fname1,logger_count+",", conn);//DIYA 20250226:ADD CHECKPOINT
			
			
		} catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally
		{
			if(file !=null)
			{file.close();}
		}

	}
	
	public void FMS_SAC_MST() throws IOException, SQLException {
		function_nm = "FMS_SAC_MST()";
		try {
			column=" ";
			logger_count=0;
			
			System.out.println("<<ROLLBACK_START>><<FMS_SAC_MST>>");
			logger.checkpoint(fname, "\n<<ROLLBACK_START>>,<<FMS_SAC_MST()>>,", conn);
			logger.checkpoint1(fname1,function_nm+"D"+","+start_end_dt+",", conn);//DIYA 20250226:ADD CHECKPOINT

			// query:delete
			query_delete = "DELETE FROM FMS_SAC_MST WHERE SAC_CD=?";

			// query for fetch column name
//			query_fetch_columnname = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FMS_SAC_MST'  ORDER BY COLUMN_ID";
//			stmt = conn.prepareStatement(query_fetch_columnname);
//			rset = stmt.executeQuery();
//			while (rset.next()) {
//				column += rset.getString(1) + ",";
//			}
//			rset.close();
//			stmt.close();
			column = "SAC_CODE,SAC_DESC,REMARKS,ENT_BY,ENT_DT,MODIFY_DT,MODIFY_BY,SAC_FLAG,ENT_PROFILE,MOD_PROFILE";

			data1 = new String[column.split(",").length];
			file1 = new File(migration_setup_dir + "EXPORT/FMS_SAC_MST_"+start_end_dt+".xlsx");
			if (file1.exists())
			{
				file = new FileInputStream(new File(migration_setup_dir + "EXPORT/FMS_SAC_MST_"+start_end_dt+".xlsx"));
				workbook = new XSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
				rowIterator.next();
				cd = ""; abbr = "";
				logger.checkpoint(fname, "SAC_CD,TIMESTAMP", conn);
				while (rowIterator.hasNext()) 
				{
						query_select = "SELECT SAC_CD FROM FMS_SAC_MST WHERE ";
						row = rowIterator.next();
						cellIterator = row.cellIterator();
						int i = 0;
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();
							data = cell.getStringCellValue();
							data = data.substring(1, data.length() - 1);
							if(cell.getColumnIndex()==0)
							{}
							else {
							data1[i] = data;
							 if (data.equals("null")) {
									query_select = query_select + column.split(",")[i] + " IS NULL AND ";
								} else if (data.split("/").length == 3 &&  data.contains(":")  && data.length()==19) {
									query_select = query_select + column.split(",")[i]
											+ " =TO_DATE(?,'DD/MM/YYYY hh24:mi:ss') AND ";
								} else {
									query_select = query_select + column.split(",")[i] + " =? AND ";
								}
								i++;
							}
						}
						
						query_select = query_select.substring(0, query_select.length() - 4);
						
						stmt = conn.prepareStatement(query_select);
						int k = 1;
						for (int w = 0; w < (column.split(",").length); w++) 
						{ if (!data1[w].equals("null")) {
								stmt.setString(k, data1[w]);
								k++;
							}
						}
						rset = stmt.executeQuery();

//			            delete if data match :
						if (rset.next()) {
							st_del = conn.prepareStatement(query_delete);
							cd = rset.getString(1);
//							abbr = rset.getString(2);
							
							st_del.setString(1, rset.getString(1));
//							st_del.setString(2, rset.getString(2));
							st_del.executeUpdate();
							
							logger.data(fname, (cd + ","), conn, "");
							logger_count++;  
							st_del.close();
						}
						rset.close();
						stmt.close();
				}
				// left data:
				query_data_left = "SELECT SAC_CD FROM FMS_SAC_MST";
				stmt = conn.prepareStatement(query_data_left);
				
				rset = stmt.executeQuery();
				while (rset.next()) {
					cd = rset.getString(1);
//					abbr = rset.getString(2);
					logger.data(fname, (cd +","), conn, "N");

				}
				rset.close();
				stmt.close();

				msg = "Data has been Rollbacked Successfully from Database.";
				msg_type = "S";
			}
			else {
				msg = "Excel File not found while Execution. Program Terminated.";
				msg_type = "E";
			}
			conn.commit();
			
			System.out.println("<<ROLLBACK_END>><<FMS_SAC_MST()>>");
			System.out.println();
			
			logger.checkpoint(fname, ("\nTOTAL DATA DELETED : ," + logger_count+","), conn);
			logger.checkpoint(fname, "<<ROLLBACK_END>>,<<FMS_SAC_MST()>>,", conn);
			logger.checkpoint1(fname1,logger_count+",", conn);//DIYA 20250226:ADD CHECKPOINT
			
		} catch (Exception e){
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			logger.error(fname, e, function_nm, conn, fname_error);
			
			msg = "One of the Functions faced an Error. RollBack Terminated.";
			msg_type = "E";
		} finally{
			if(file !=null)
			{file.close();}
		}
	}
	
	
	
// 	public void getCustomerTraderList() {
// 		function_nm = "getCustomerTraderList()";
// 		try
// 		{
// 			String strline = "";
			
// 			File fsetup=new File(migration_setup_dir+"Migration_Plants_Exceptions.txt");
// 			String file_path=fsetup.getAbsolutePath();
// 			FileInputStream f1 = new FileInputStream(file_path);
// 			DataInputStream in = new DataInputStream(f1);
// 			try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			
// 				while((strline = br.readLine())!=null)
// 				{
// 					if(strline.startsWith("TRANSPORTER-MAPPING"))
// 					{
// 						String  tmp[]=strline.split("TRANSPORTER-MAPPING: ");
// 						transporter_map = tmp[1].toString();
// 					}
// 					if(strline.startsWith("METER-MAPPING"))
// 					{
// 						String  tmp[]=strline.split("METER-MAPPING: ");
// 						meter_map = tmp[1].toString();
// 					}
// 				}
// 			}
			
// //			System.out.println(customer_delete);
// //			System.out.println(trader_delete);
// //			System.out.println(customer_map);
// //			System.out.println(trader_map);
// 		}
// 		catch (Exception e) 
// 		{
// 			//LOGGER.log(Level.WARNING, "Error in Master_SEIPL_Data_Extractor.java -> Master_Excel_Extractor -> getCustomerTraderList() ", e);
// 			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e); 
// 		}
// 	}
	
	
	public String Camel_Case_Converter(String value) 
	{
//		value = value.substring(1, value.length()-1);
		String converted_string = value; 
		
		if (value.length() > 3 && !value.contains("null")) {
			if (value.contains(" ")) {
				converted_string = "";
				for (int i = 0; i < value.split(" ").length; i++) 
				{
					if (!value.split(" ")[i].substring(0, 1).equals("(") && !value.split(" ")[i].contains(")")) 
					{
						converted_string += value.split(" ")[i].substring(0, 1).toUpperCase();
						converted_string = converted_string + value.split(" ")[i].substring(1).toLowerCase() + " ";
					}
					else 
					{
						converted_string += value.split(" ")[i] + " ";
					}
				}
				converted_string = converted_string.substring(0, converted_string.length()-1);
			}
			else {
				converted_string = value.substring(0, 1).toUpperCase();
				converted_string = converted_string + value.substring(1).toLowerCase();
			}
		}
		else {
			converted_string = "null";
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
