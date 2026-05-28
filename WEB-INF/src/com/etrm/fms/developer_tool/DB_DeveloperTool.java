package com.etrm.fms.developer_tool;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

public class DB_DeveloperTool 
{
	String db_src_file_name="DB_DeveloperTool.java";

	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt_tmp;
	ResultSet rset; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset_tmp;
	/*String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";*/
	
	public static String sf_xml_inbound_dir="";
	public static String sf_xml_outbound_dir="";
	public static String sap_dir="";
	public static String ack_sap_dir="";
	public static String daily_rpt_dir="";
	public static String zema_dir="";
	
	DateUtil utilDate = new DateUtil();
	UtilBean utilBean = new UtilBean();
	
	public void init()
	{
		String function_nm="init()";
		try
		{
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
	    		if(conn != null)  
	    		{
					if(callFlag.equalsIgnoreCase("FILE_DIR"))
					{
						fetch_files_info();
					}
					else if(callFlag.equalsIgnoreCase("GRANT_TABLE"))
					{
						fetch_table_info();
					}
					else if(callFlag.equalsIgnoreCase("FILE_DOWNLOAD"))
					{
						String genPathString="";
						
						sf_xml_inbound_dir=utilBean.getAutomationKeyDetail(conn,"SF_XML_INBOUND");
			            sf_xml_outbound_dir=utilBean.getAutomationKeyDetail(conn,"SF_XML_OUTBOUND_"+comp_cd);
			           
			            sap_dir=utilBean.getAutomationKeyDetail(conn,"SAP_PATH_"+comp_cd);
			            ack_sap_dir=utilBean.getAutomationKeyDetail(conn,"SAP_ACK_PATH");
			            
			            daily_rpt_dir=utilBean.getAutomationKeyDetail(conn,"DAILY_RPT_PATH");
			            zema_dir=utilBean.getAutomationKeyDetail(conn,"ZEMA_SRC_PATH");
						
						if(setFolder.equals("SF_INB_XML"))
						{
							genPathString = sf_xml_inbound_dir;
							
							File fileSaveDir = new File(genPathString);
					        if(!fileSaveDir.exists()) 
					        {
					            fileSaveDir.mkdirs();
					        }
						}
						else if(setFolder.equals("SF_OUB_XML"))
						{
							genPathString = sf_xml_outbound_dir;
							
							File fileSaveDir = new File(genPathString);
					        if(!fileSaveDir.exists()) 
					        {
					        	 fileSaveDir.mkdirs();
					        }
						}
						else if(setFolder.equals("SAP"))
						{
							genPathString = sap_dir;
							
							File fileSaveDir = new File(genPathString);
					        if(!fileSaveDir.exists()) 
					        {
					        	 fileSaveDir.mkdirs();
					        }
						}
						else if(setFolder.equals("ACK_SAP"))
						{
							genPathString = ack_sap_dir;
							
							File fileSaveDir = new File(genPathString);
					        if(!fileSaveDir.exists()) 
					        {
					        	 fileSaveDir.mkdirs();
					        }
						}
						else if(setFolder.equals("DRPT"))
						{
							genPathString = daily_rpt_dir;
							
							File fileSaveDir = new File(genPathString);
					        if(!fileSaveDir.exists()) 
					        {
					        	 fileSaveDir.mkdirs();
					        }
						}
						else if(setFolder.equals("ZEMA"))
						{
							genPathString = zema_dir;
							
							File fileSaveDir = new File(genPathString);
					        if(!fileSaveDir.exists()) 
					        {
					        	 fileSaveDir.mkdirs();
					        }
						}
						
						if(!inner_path.equals(""))
						{
							genPathString = genPathString+inner_path;
						}

						path = genPathString;
						path_sub = genPathString;
						path_sub_sub = genPathString;
						
						fetch_files_info_for_download();
					}
					else if(callFlag.equalsIgnoreCase("DTLESS_FILE_DOWNLOAD"))
					{
						dtless_fetch_files_info_for_download();
					}
	    		}
	    		conn.close();
    			conn = null;
	    	}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally
	    {
	    	if(rset != null){try{rset.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_tmp != null){try{stmt_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void fetch_files_info()
	{
		String function_nm="fetch_files_info()";
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
			File fObj = new File(path);
			if(fObj.isDirectory())
			{
				File a[] = fObj.listFiles();
				for(int i=0; i<a.length; i++)
				{
					if(a[i].getName().startsWith("."))
					{
						
					}
					else if(a[i].isFile())
					{
						VFOLDER.add(""+a[i].getName());
						
						String dt = sdf.format(a[i].lastModified());
			        	String split_dt[] = dt.split(" ");
			        	String queryString="SELECT TO_CHAR(TO_DATE(?,'MM/DD/YYYY'),'DD/MM/YYYY') FROM DUAL";
			        	stmt=conn.prepareStatement(queryString);
			        	stmt.setString(1, split_dt[0]);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							VUPDATE_ON.add(rset.getString(1)==null?"":rset.getString(1)+" "+split_dt[1]);
						}
						rset.close();
						stmt.close();
						long filesizeinbyte = a[i].length();
						long filesizeiKB = filesizeinbyte / 1024;
						
						VBYTES.add(filesizeinbyte);
						VPATH.add("");
						VTYPE.add("F");
					}
					else if(a[i].isDirectory())
					{
						VFOLDER.add(""+a[i].getName());
						VUPDATE_ON.add("");
						VBYTES.add("");
						VPATH.add(path_sub+""+a[i].getName()+"/");
						VTYPE.add("D");
					}
				}
			}
			String[] temp = path_sub.split("/");
			int index = temp.length-1;
			for(int i=0; i<index;i++)
			{
				temp_path+=""+temp[i]+"/";
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void fetch_files_info_for_download() 
	{
	    String function_nm = "fetch_files_info_for_download()";
	    try 
	    {

	        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	        String formattedFromDt="";
	        String formattedToDt="";
	        
	        if(!from_dt.equals("") && !to_dt.equals(""))
	        {
	        	formattedFromDt = from_dt.split("/")[2]+from_dt.split("/")[1]+from_dt.split("/")[0];
		        formattedToDt = to_dt.split("/")[2]+to_dt.split("/")[1]+to_dt.split("/")[0];
	        }
	        else
	        {
	        	String sysDtString = utilDate.getSysdate();
	        	//String lastMonthDt = utilDate.getDate(sysDtString, "-30");
	        	
	        	formattedFromDt = sysDtString.split("/")[2]+sysDtString.split("/")[1]+sysDtString.split("/")[0];
		        formattedToDt = sysDtString.split("/")[2]+sysDtString.split("/")[1]+sysDtString.split("/")[0];
			}
	        
	        String startDate = formattedFromDt;
	        String endDate = formattedToDt;

	        File fObj = new File(path_sub);
	        if (fObj.isDirectory())
	        {
	        	// Using FilenameFilter to filter files based on date range
	        	File[] filteredFiles = fObj.listFiles(new FilenameFilter() 
	        	{
	        		@Override
	        		public boolean accept(File dir, String name) 
	        		{
	        			File file = new File(dir, name);
	                    
	        			if (file.isDirectory())
	                    {
	                        return true;
	                    }

	                    // Only for files, applying the date filtering
	                    if (setFolder.equals("SF_INB_XML") || setFolder.equals("SF_OUB_XML") || 
	                    	setFolder.equals("DRPT") || setFolder.equals("ZEMA"))
	                    {
	                        return containsValidDateyyyyMMdd(file, name, startDate, endDate);  // Apply yyyyMMdd date filter for files
	                    } 
	                    else if (setFolder.equals("SAP") || setFolder.equals("ACK_SAP"))
	                    {
	                        return containsValidDateddmmyyyyhhmm(file, name, startDate, endDate);  // Apply ddMMyyyyHHmm date filter for files
	                    }
	                    // Add if the other case is needed
	                    else
	                    {
	                    	return false;  // Return false for any other case
	                    }
	        		}
	        	});
	        	
	        	// Process only the filtered files
	            for (File file : filteredFiles)
	            {
	            	if (!file.isFile()) 
	                {
	                	VFOLDER.add("" + file.getName());

	                	VUPDATE_ON.add("");
	                	
	                	long filesizeinbyte = file.length();
	                    long filesizeiKB = filesizeinbyte / 1024;

	                    VBYTES.add(filesizeinbyte);
	                    VPATH.add(file.getName()+"/");
	                    VTYPE.add("D");
	                }
	            	else if (file.isFile()) 
	                {
	                    // File contains a valid date within the range, proceed with processing
	                    VFOLDER.add("" + file.getName());

	                    String dt = sdf.format(file.lastModified());
	                    String split_dt[] = dt.split(" ");
	                    String queryString = "SELECT TO_CHAR(TO_DATE(?,'MM/DD/YYYY'),'DD/MM/YYYY') "
	                    		+ " FROM DUAL";
	                    stmt = conn.prepareStatement(queryString);
	                    stmt.setString(1, split_dt[0]);
	                    
	                    rset = stmt.executeQuery();
	                    if (rset.next()) {
	                        VUPDATE_ON.add(rset.getString(1) == null ? "" : rset.getString(1) + " " + split_dt[1]);
	                    }
	                    rset.close();
	                    stmt.close();
	                    long filesizeinbyte = file.length();
	                    long filesizeiKB = filesizeinbyte / 1024;

	                    VBYTES.add(filesizeinbyte);
	                    VPATH.add("");
	                    VTYPE.add("F");
	                }
	            }
	        }

	        // Process sub-path logic if necessary
	        String[] temp = path_sub_sub.split("/");
	        int index = temp.length - 1;
	        for (int i = 0; i < index; i++)
	        {
	            temp_path += "" + temp[i] + "/";
	        }
	        
	        fetch_path = path;
	    	fetch_dwn_path = ""+path;
	    	fetch_path_sub = path_sub;
	    	fetch_path_sub_sub = path_sub_sub;
	    	
	    } 
	    catch (Exception e)
	    {
	        new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	    }
	}

	/*
	This function is previously used for all the downloads reports, 
	Currently used only without date filtered/
	*/
	public void dtless_fetch_files_info_for_download()   
	{
		String function_nm = "dtless_fetch_files_info_for_download()";
		try 
		{
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
			File fObj = new File(path_sub);
			if(fObj.isDirectory())
			{
				File a[] = fObj.listFiles();
				for(int i=0; i<a.length; i++)
				{
					if(a[i].getName().startsWith("."))
					{
						
					}
					else if(a[i].isFile())
					{
						VFOLDER.add(""+a[i].getName());
						
						String dt = sdf.format(a[i].lastModified());
			        	String split_dt[] = dt.split(" ");
			        	String queryString="SELECT TO_CHAR(TO_DATE(?,'MM/DD/YYYY'),'DD/MM/YYYY') FROM DUAL";
			        	stmt=conn.prepareStatement(queryString);
			        	stmt.setString(1, split_dt[0]);
						ResultSet rset=stmt.executeQuery();
						if(rset.next())
						{
							VUPDATE_ON.add(rset.getString(1)==null?"":rset.getString(1)+" "+split_dt[1]);
						}
						rset.close();
						stmt.close();
						long filesizeinbyte = a[i].length();
						long filesizeiKB = filesizeinbyte / 1024;
						
						VBYTES.add(filesizeinbyte);
						VPATH.add("");
						VTYPE.add("F");
					}
					else if(a[i].isDirectory())
					{
						VFOLDER.add(""+a[i].getName());
						VUPDATE_ON.add("");
						VBYTES.add("");
						VPATH.add(path_sub_sub+""+a[i].getName()+"/");
						VTYPE.add("D");
					}
				}
			}
			String[] temp = path_sub_sub.split("/");
			int index = temp.length-1;
			for(int i=0; i<index;i++)
			{
				temp_path+=""+temp[i]+"/";
			}
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	/**
	 * This function checks if the file name contains a date in the format YYYYMMDD,
	 * and if that date is within the provided start and end date range.
	 */
	private boolean containsValidDateyyyyMMdd(File  fileType,String filename, String startDate, String endDate)
	{
	    // Regular expression to extract a date in YYYYMMDD format from the filename
	    String regex = "\\d{8}";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(filename);

    	// Check if a date is found in the filename
	    if (matcher.find()) 
	    {
	    	String date = matcher.group(0);  // Extract the matched date

	    	// Check if the extracted date is a valid date in the format YYYYMMDD
	    	try 
	    	{
	    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	    	    dateFormat.setLenient(false);  // Disallow invalid dates like "20221345"

	    	    // Parse the extracted date
	    	    dateFormat.parse(date);

	    	    boolean result = date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0;
	    	    return result;
	    	} 
	    	catch (Exception e)
	    	{
	    	    return false;
	    	}

	    }
	    else if(fileType.isDirectory() && !fileType.isFile()) 
		{
            return true;
		}
	    
	    return false;
	}
	
	/**
	 * This function checks if the file name contains a date in the format ddmmyyyyhhmm,
	 * and if that date is within the provided start and end date range.
	 */
	private boolean containsValidDateddmmyyyyhhmm(File fileType, String filename, String startDate, String endDate)
	{
	    // Regular expression to extract a date in ddMMyyyyHHmm format from the filename (12 digits)
	    String regex = "\\d{12}";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(filename);

	    // Check if a date is found in the filename
	    if (matcher.find()) 
	    {
	        String date = matcher.group(0);  // Extract the matched date (12 digits: ddMMyyyyHHmm)

	        // Check if the extracted date is a valid date in the format ddMMyyyyHHmm
	        try 
	        {
	        	// To parse the extracted date in the format ddMMyyyyHHmm
	        	SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmm");
	        	dateFormat.setLenient(false);  // Disallow invalid dates like "301013251300" (invalid day)

	        	// Parse the date from the filename
	        	Date parsedDate = null;
	        	try 
	        	{
	        	    parsedDate = dateFormat.parse(date); // 'date' is extracted from the filename
	        	} 
	        	catch (Exception e)
	        	{
	        		return false;
	        	}

	        	// Convert startDate and endDate (yyyyMMdd) to ddMMyyyyHHmm format
	        	SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
	        	SimpleDateFormat outputFormat = new SimpleDateFormat("ddMMyyyy");

	        	// Parse and convert startDate and endDate to ddMMyyyyHHmm format
	        	String formattedStartDate = outputFormat.format(inputFormat.parse(startDate)) + "0000"; // start time is 00:00
	        	String formattedEndDate = outputFormat.format(inputFormat.parse(endDate)) + "2359"; // end time is 23:59

	        	Date parsedStartDate = null;
	        	Date parsedEndDate = null;

	        	try 
	        	{
	        	    parsedStartDate = dateFormat.parse(formattedStartDate);
	        	    parsedEndDate = dateFormat.parse(formattedEndDate);
	        	} 
	        	catch (Exception e1)
	        	{
	        		return false;
	        	}

	        	// Now perform the inclusive comparison
	        	if (parsedDate != null && parsedStartDate != null && parsedEndDate != null)
	        	{
	        	    return parsedDate.compareTo(parsedStartDate) >= 0 && parsedDate.compareTo(parsedEndDate) <= 0;
	        	}

	        	// In case the date parsing fails (null values)
	        	return false;

	        } 
	        catch (Exception e)
	        {
	            return false;
	        }
	    }
	    else if (fileType.isDirectory() && !fileType.isFile()) 
	    {
	        // If it's a directory, return true to skip further checking
	        return true;
	    }

	    return false;  // Return false if no valid date found
	}
	
	public void fetch_table_info()
	{
		String function_nm="fetch_table_info()";

		try
		{
			String sysdt=utilDate.getSysdate();
			String retention_dt = utilDate.getDate(sysdt, "-9125");// System date - 25 years
			
			String queryString="SELECT TNAME "
					+ "FROM TAB "
					+ "WHERE TABTYPE=? AND TNAME NOT LIKE ? ORDER BY TNAME";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "TABLE");
			stmt.setString(2, "ARC_%");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String table_nm=rset.getString(1)==null?"":rset.getString(1);
				VTABLE_NM.add(table_nm);
				
				int row=0;
				String queryString1temp="SELECT COUNT(*) FROM "+table_nm;
				String queryString1= queryString1temp;
				stmt1=conn.prepareStatement(queryString1);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					row=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				Vector temp_cols = new Vector();
				int count_cols=0;
				String queryString2="SELECT COUNT(*) FROM COLS "
						+ "WHERE UPPER(TABLE_NAME) LIKE ? AND UPPER(COLUMN_NAME) LIKE ?";
				stmt1=conn.prepareStatement(queryString2);
				stmt1.setString(1, table_nm.toUpperCase());
				stmt1.setString(2, "ENT_DT");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					count_cols=rset1.getInt(1);
					if(count_cols>0) {
					temp_cols.add("ENT_DT");
					}
				}
				rset1.close();
				stmt1.close();
				
				String archive_data="N";
				String archive_logic="";
				String queryString3="SELECT ARCHIVE_DATA,ARCHIVE_LOGIC "
						+ "FROM FMS_DATA_DISPOSAL "
						+ "WHERE DB_TABLE = ? ";
				stmt1=conn.prepareStatement(queryString3);
				stmt1.setString(1, table_nm.toUpperCase());
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					archive_data=rset1.getString(1)==null?"":rset1.getString(1);
					archive_logic=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				VROW.add(row);
				VARCHIVE_FLAG.add(archive_data);
				VARCHIVE_LOGIC.add(temp_cols);
				VARCHIVE_LOGIC_VALUE.add(archive_logic);
				
				int archive_row=0;
				if(count_cols>0 && !archive_logic.equals("") && archive_data.equals("Y"))
				{
					String queryString4temp="SELECT COUNT(*) "
							+ "FROM "+table_nm+" "
							+ "WHERE ENT_DT < TO_DATE(?,'DD/MM/YYYY')";
					String queryString4= queryString4temp;
					stmt1=conn.prepareStatement(queryString4);
					stmt1.setString(1, retention_dt);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						archive_row=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
				}
				VARCHIVABLE_ROW.add(archive_row);
				VRETENTION_DT.add(retention_dt);
				
				int is_arc_tbl_exist=0;
				String queryString5="SELECT COUNT(*) "
						+ "FROM ALL_TABLES "
						+ "WHERE TABLE_NAME = ? ";
				stmt1=conn.prepareStatement(queryString5);
				stmt1.setString(1, "ARC_"+table_nm);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					is_arc_tbl_exist=rset1.getInt(1);
				}
				rset1.close();
				stmt1.close();
				
				int archived_row=0;
				if(is_arc_tbl_exist > 0)
				{
					VARCHIVED_TABLE_NAME.add("ARC_"+table_nm);
					
					String queryString6temp="SELECT COUNT(*) FROM ARC_"+table_nm;
					String queryString6= queryString6temp;
					stmt1=conn.prepareStatement(queryString6);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						archived_row=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					VARCHIVED_TABLE_NAME.add("");
				}
				
				VARCHIVED_ROW.add(archived_row);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	
	String path="";
	String path_sub="";
	String path_sub_sub="";
	String temp_path="";
	String inner_path="";
	
	String fetch_path = "";
	String fetch_dwn_path = "";
	String fetch_path_sub = "";
	String fetch_path_sub_sub = "";
	
	String setFolder="";
	
	String from_dt = "";
	String to_dt = "";
	
	public void setPath(String path) {this.path = path;}
	public void setPath_sub(String path_sub) {this.path_sub = path_sub;}
	public void setPath_sub_sub(String path_sub_sub) {this.path_sub_sub = path_sub_sub;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setSetFolder(String setFolder) {this.setFolder = setFolder;}
	public void setInner_path(String inner_path) {this.inner_path = inner_path;}
	
	public String getFetch_path() {return fetch_path;}
	public String getFetch_dwn_path() {return fetch_dwn_path;}
	public String getFetch_path_sub() {return fetch_path_sub;}
	public String getFetch_path_sub_sub() {return fetch_path_sub_sub;}
	public String getTemp_path() {return temp_path;}
	
	Vector VFOLDER = new Vector();
	Vector VTYPE = new Vector();
	Vector VUPDATE_ON = new Vector();
	Vector VBYTES = new Vector();
	Vector VPATH = new Vector();
	
	Vector VTABLE_NM = new Vector();
	Vector VROW = new Vector();
	
	Vector VARCHIVE_FLAG = new Vector();
	Vector VARCHIVE_LOGIC = new Vector();
	Vector VARCHIVE_LOGIC_VALUE = new Vector();
	Vector VARCHIVABLE_ROW = new Vector();
	Vector VRETENTION_DT = new Vector();
	
	Vector VARCHIVED_TABLE_NAME = new Vector();
	Vector VARCHIVED_ROW = new Vector();
	
	public Vector getVTYPE() {return VTYPE;}
	public Vector getVFOLDER() {return VFOLDER;}
	public Vector getVUPDATE_ON() {return VUPDATE_ON;}
	public Vector getVBYTES() {return VBYTES;}
	public Vector getVPATH() {return VPATH;}
	
	public Vector getVTABLE_NM() {return VTABLE_NM;}
	public Vector getVROW() {return VROW;}
	public Vector getVARCHIVE_FLAG() {return VARCHIVE_FLAG;}
	public Vector getVARCHIVE_LOGIC() {return VARCHIVE_LOGIC;}
	public Vector getVARCHIVE_LOGIC_VALUE() {return VARCHIVE_LOGIC_VALUE;}
	public Vector getVARCHIVABLE_ROW() {return VARCHIVABLE_ROW;}
	public Vector getVRETENTION_DT() {return VRETENTION_DT;}
	
	public Vector getVARCHIVED_TABLE_NAME() {return VARCHIVED_TABLE_NAME;}
	public Vector getVARCHIVED_ROW() {return VARCHIVED_ROW;}
}
