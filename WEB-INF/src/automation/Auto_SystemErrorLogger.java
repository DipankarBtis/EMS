package automation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Patel
//Code Reviewed by	: Jayasri Dhar 
//CR Date			: 04/04/2024 
//Status	  		: Developing
public class Auto_SystemErrorLogger 
{
	Connection conn; 
	PreparedStatement stmt;
	ResultSet rset;
	String queryString="";
	String queryString1="";
	
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
		        
		        conn=new Auto_DB_Connection().db_conn();
				if(conn != null)
		        {
					String error_msg=ex.toString();
					error_msg=escObj.replaceSingleQuotes(error_msg);
					
					StackTraceElement[] StackTrace = ex.getStackTrace();
					System.out.println("**********AutomationFMSng Error "+tsDate+" "+time+"**********");
					System.out.println(error_msg);
					String stack_trace = "";
			        for(StackTraceElement st : StackTrace)  
			        {  
			        	System.out.println(st);
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
			        System.out.println("**************************************************");
					
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
		        }
				conn.close();
				conn = null;
			}
			catch(Exception e)
			{
				StackTraceElement[] StackTrace = e.getStackTrace();
				for(StackTraceElement st : StackTrace)  
		        {  
		        	System.out.println(st);
		        }
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
					StackTraceElement[] StackTrace = e1.getStackTrace();
					for(StackTraceElement st : StackTrace)  
			        {  
			        	System.out.println(st);
			        }
				}
			}
    	}
	}
}
