package com.etrm.fms.migration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.etrm.fms.util.SystemErrorLogger;

public class DataMigration_Logger {
	
	String db_src_file_name="DataMigration_Logger.java";
	
	String query_logger="";
	ResultSet rset_logger;
	PreparedStatement stmt_logger;
	
	String function_nm = "";

	BufferedWriter out;
	
	public void checkpoint(String fname, String str, Connection conn) throws IOException {		// This is for logging the starting and Ending of the function. Ending will only be printed from here if the function is ended successfully
		function_nm = "checkpoint()";
		
		try {
			synchronized(this) {

				out = new BufferedWriter(new FileWriter(fname, true));
				
				query_logger = "SELECT TO_CHAR(SYSDATE, 'DD-MON-YYYY HH24:MI:SS')  FROM DUAL ";
				stmt_logger = conn.prepareStatement(query_logger);
				rset_logger = stmt_logger.executeQuery();
				if(rset_logger.next()) {
					if(!str.contains("TIMESTAMP")) {
						str = "\n" + str + " \t(" + rset_logger.getString(1)+")\n ";
					}
					else {
						str = "\n" + str;
					}
				}
				rset_logger.close();
				stmt_logger.close();
				
				out.write(str);
				out.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			out.close();
			if(rset_logger != null){try{rset_logger.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt_logger != null){try{stmt_logger.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		}
	}
	
	
	public void checkpoint1(String fname, String str, Connection conn) throws IOException {		
		function_nm = "checkpoint1()";
		try {
			synchronized(this) 
			{

				out = new BufferedWriter(new FileWriter(fname, true));

				query_logger = "SELECT TO_CHAR(SYSDATE, 'DD-MON-YYYY HH24:MI:SS')  FROM DUAL ";
				stmt_logger = conn.prepareStatement(query_logger);
				rset_logger = stmt_logger.executeQuery();
				if(rset_logger.next()) {
					if(str.contains("FMS") || str.contains("LOG")) 
					{
						str ="\n"+ str + " \t(" + rset_logger.getString(1)+") ";
					}
					else
					{
						str =","+ str + " \t(" + rset_logger.getString(1)+") ";
					}
				}
				rset_logger.close();
				stmt_logger.close();
				
				
				out.write(str);
				out.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			out.close();
			if(rset_logger != null){try{rset_logger.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt_logger != null){try{stmt_logger.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		}
	}
	
	
	public void data(String fname, String str, Connection conn, String sts) throws IOException {		// For Inserting Data in logger
		function_nm = "data()";
		
		try {
			synchronized(this) {

				out = new BufferedWriter(new FileWriter(fname, true));
				
				query_logger = "SELECT TO_CHAR(SYSDATE, 'DD-MON-YYYY HH24:MI:SS')  FROM DUAL ";
				stmt_logger = conn.prepareStatement(query_logger);
				rset_logger = stmt_logger.executeQuery();
				if(rset_logger.next()) {
					str = "\n" + str + " \t(" + rset_logger.getString(1)+")";
				}
				rset_logger.close();
				stmt_logger.close();
				if(sts.equals("N"))
				{
					str=str+"\t (Not Deleted)";
				}
				if(sts.equals("E")) {
					str = str + "\t (Not Inserted)";
				}
				
				out.write(str);
				out.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			out.close();
			if(rset_logger != null){try{rset_logger.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt_logger != null){try{stmt_logger.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		}
	
	}
	
	public void data_error(String fname, Exception ex, String data, Connection conn, String fname_error, String func_name) throws IOException {		// For Inserting Error StackTrace in logger
		function_nm = "data_error()";
		
		try {
			synchronized(this) {
	
				String error = "";
				
				out = new BufferedWriter(new FileWriter(fname, true));
	
				query_logger = "SELECT TO_CHAR(SYSDATE, 'DD-MON-YYYY HH24:MI:SS')  FROM DUAL ";
				stmt_logger = conn.prepareStatement(query_logger);
				rset_logger = stmt_logger.executeQuery();
				if(rset_logger.next()) {
					error = "\n**********AutomationEMS Error "+" (" + rset_logger.getString(1)+") "+"**********\n";
					error = error + ex.toString() + "\n";
					for(StackTraceElement st : ex.getStackTrace())  
			        {  
			        	System.out.println(st);
			            // print the stack trace
			        	if (st.toString().contains("com.etrm.fms.") || st.toString().contains("automation."))
			        	{
			        		if(!error.equals(""))
			        		{
			        			error+="\n";
			        		}
			        		error+=st.toString();
			        	}
			        } 
					error = error + "\n**************************************************" + "\n";
					error = error + "<<DATA "+data+" NOT INSERTED>>\n";
				}
				rset_logger.close();
				stmt_logger.close();
				
				out.write(error);
				out.close();
				
				// Logging Error in Separate File
				out = new BufferedWriter(new FileWriter(fname_error, true));
				out.write("\n<<ERROR IN "+func_name+">>");
				out.write(error);
				out.close();
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			out.close();
			if(rset_logger != null){try{rset_logger.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt_logger != null){try{stmt_logger.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		}
	
	}
	
	public void error(String fname, Exception ex, String func_name, Connection conn, String fname_error) throws IOException {		// For Inserting Error StackTrace in logger
		function_nm = "error()";
		try {
			synchronized(this) {

				String error = "";
				
				out = new BufferedWriter(new FileWriter(fname, true));
	
				query_logger = "SELECT TO_CHAR(SYSDATE, 'DD-MON-YYYY HH24:MI:SS')  FROM DUAL ";
				stmt_logger = conn.prepareStatement(query_logger);
				rset_logger = stmt_logger.executeQuery();
				if(rset_logger.next()) {
					error = "\n**********AutomationEMS Error "+" (" + rset_logger.getString(1)+") "+"**********\n";
					error = error + ex.toString() + "\n";
					for(StackTraceElement st : ex.getStackTrace())  
			        {  
			        	System.out.println(st);
			            // print the stack trace
			        	if (st.toString().contains("com.etrm.fms.") || st.toString().contains("automation."))
			        	{
			        		if(!error.equals(""))
			        		{
			        			error+="\n";
			        		}
			        		error+=st.toString();
			        	}
			        } 
					error = error + "\n**************************************************" + "\n";
					error = error + "\n<<FUNCTION "+func_name+" ENDED WITH ERRORS>>\n";
				}
				rset_logger.close();
				stmt_logger.close();
				
				
				out.write(error);
				out.close();
				
	
				// Logging Error in Separate File
				out = new BufferedWriter(new FileWriter(fname_error, true));
				out.write("\n<<ERROR IN "+func_name+">>");
				out.write(error);
				out.close();
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			out.close();
			if(rset_logger != null){try{rset_logger.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt_logger != null){try{stmt_logger.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		}
	
	}
	
	public void insert_data(String fname, String str, Connection conn) throws IOException {
		function_nm = "insert_data()";
		try {
			
			synchronized(this) {
				out = new BufferedWriter(new FileWriter(fname, true));
				
				str = str.substring(0, str.length()-1) + "\n";
				out.write(str);
				out.close();
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally {
			out.close();
		}
	}
	
}
