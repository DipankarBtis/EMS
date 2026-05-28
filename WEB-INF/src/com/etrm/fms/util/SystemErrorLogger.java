package com.etrm.fms.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

//Coded By          : Harsh Patel
//Code Reviewed by	: Jayasri Dhar 
//CR Date			: 04/04/2024 
//Status	  		: Developing
public class SystemErrorLogger 
{
	Connection conn; 
	PreparedStatement stmt;
	ResultSet rset;
	String queryString="";
	String queryString1="";
	
	private static final Logger LOGGER = Logger.getLogger(SystemErrorLogger.class.getName());
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	public void InsertErrorLogger(String file_name,String func_name,Exception ex)
	{
		synchronized(this)
		{
			try
			{
				Timestamp ts = new Timestamp(System.currentTimeMillis());
		        String tsString = ts.toString().substring(0, 19);
		        String tsDate = tsString.substring(0, 10);
		        String time = tsString.substring(11, 19);
		        
		        Context initContext = new InitialContext();
			    Context envContext  = (Context)initContext.lookup("java:/comp/env");
				DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
			    if(ds != null)
			    {
			    	conn = ds.getConnection();       
			        if(conn != null)
			        {
						String error_msg=ex.toString();
						error_msg=escObj.replaceSingleQuotes(error_msg);
						
						String info="";
						
						StackTraceElement[] StackTrace = ex.getStackTrace();
						info="**********EMS Error "+tsDate+" "+time+"**********\n";
						info+=error_msg+"\n";
						String stack_trace = "";
				        for(StackTraceElement st : StackTrace)  
				        {  
				        	info+=""+st+"\n";
				            // print the stack trace
				        	if (st.toString().contains("com.etrm.fms.") || st.toString().contains("automation."))
				        	{
				        		if(!stack_trace.equals(""))
				        		{
				        			stack_trace+="\n";
				        		}
				        		stack_trace+=st.toString();
				        	}
				        }  
				        info+="**************************************************\n";
				        
				        LOGGER.log(Level.WARNING,info);
						
						String temp_error_cd="";
						if(!tsDate.equals(""))
						{
							String split[] = tsDate.split("-");
							temp_error_cd=split[0].substring(2,split[0].length());
							temp_error_cd=temp_error_cd+""+split[1];
						}
						
						int err_id=Integer.parseInt(temp_error_cd)*10000;
						String error_cd="";
						
						queryString="SELECT MAX(ERROR_CD) "
								+ "FROM FMS_ERROR_LOG "
								+ "WHERE ERROR_CD LIKE ?";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, temp_error_cd+"%");
						rset=stmt.executeQuery();
						if(rset.next())
						{
							if(rset.getInt(1) > 0)
							{
								error_cd=""+(rset.getInt(1)+1);
							}
							else
							{
								error_cd=""+(err_id+1);
							}
						}
						else
						{
							error_cd=""+(err_id+1);
						}
						rset.close();
						stmt.close();
					
						
						queryString = "INSERT INTO FMS_ERROR_LOG(ERROR_CD, ENT_DT, SRC_FILE_NM, FUNCTION_NM, ERROR_MSG, STACK_TRACE) "
				        		+ "VALUES(?,SYSDATE,?,?,?,?)";
						stmt=conn.prepareStatement(queryString);
				        stmt.setString(1, error_cd);
				        stmt.setString(2, file_name);
				        stmt.setString(3, func_name);
				        stmt.setString(4, error_msg);
				        stmt.setString(5, stack_trace);
				        stmt.executeUpdate();
				        stmt.close();
				        
				        conn.commit();
			        }
			        conn.close();
					conn = null;
			    }
			}
			catch(Exception e)
			{
				LOGGER.log(Level.WARNING,"context:",e);
			}
			finally
			{
				try
				{
					if(conn != null){conn.close();}
					if(stmt != null){stmt.close();}
					if(rset != null){rset.close();}
				}
				catch(Exception e1)
				{
					LOGGER.log(Level.WARNING,"context:",e1);
				}
			}
		}
	}
}
